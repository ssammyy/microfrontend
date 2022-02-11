package org.kebs.app.kotlin.apollo.api.service

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.*
import org.kebs.app.kotlin.apollo.api.ports.provided.sage.response.PaymentStatusResult
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.CdDemandNoteEntity
import org.kebs.app.kotlin.apollo.store.model.di.CurrencyExchangeRates
import org.kebs.app.kotlin.apollo.store.repo.di.ICfgCurrencyExchangeRateRepository
import org.kebs.app.kotlin.apollo.store.repo.di.IDemandNoteRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.io.InputStreamReader
import java.io.Reader
import java.sql.Timestamp
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*


@Service("invoiceService")
class InvoicePaymentService(
        private val iDemandNoteRepo: IDemandNoteRepository,
        private val auditService: ConsignmentDocumentAuditService,
        private val reportsDaoService: ReportsDaoService,
        private val exchangeRateRepository: ICfgCurrencyExchangeRateRepository,
        private val service: DaoService,
        private val daoServices: DestinationInspectionDaoServices,
        private val invoiceDaoService: InvoiceDaoService,
        private val commonDaoServices: CommonDaoServices,
        private val applicationMapProperties: ApplicationMapProperties,
        private val billingService: BillingService
) {
    var dateTimeFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    fun invoiceDetails(demandNoteId: Long): HashMap<String, Any> {
        var map = hashMapOf<String, Any>()
        daoServices.findDemandNoteWithID(demandNoteId)?.let { demandNote ->
            map["preparedBy"] = demandNote.generatedBy.toString()
            map["datePrepared"] = demandNote.dateGenerated.toString()
            map["demandNoteNo"] = demandNote.demandNoteNumber.toString()
            map["importerName"] = demandNote.nameImporter.toString()
            map["importerAddress"] = demandNote.address.toString()
            map["importerTelephone"] = demandNote.telephone.toString()
            map["ablNo"] = demandNote.entryAblNumber.toString()
            map["totalAmount"] = demandNote.totalAmount.toString()
            map["receiptNo"] = demandNote.receiptNo.toString()
            map = reportsDaoService.addBankAndMPESADetails(map, demandNote.demandNoteNumber ?: "")
        }
        return map
    }

    fun rejectDemandNoteGeneration(cdUuid: String, demandNoteId: Long, remarks: String): Boolean {
        try {
            val consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
            val demandNote = iDemandNoteRepo.findById(demandNoteId)
            if (demandNote.isPresent) {
                val demand = demandNote.get()
                val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
                demand.status = map.invalidStatus
                demand.varField3 = "REJECTED"
                demand.varField10 = remarks
                consignmentDocument.varField10 = "Demand note rejected"
                consignmentDocument.sendDemandNote = map.invalidStatus
                consignmentDocument.diProcessStatus = map.inactiveStatus
                consignmentDocument.diProcessInstanceId = null
                consignmentDocument.cdStandard?.let { cdStd ->
                    daoServices.updateCDStatus(cdStd, ConsignmentDocumentStatus.PAYMENT_REJECTED)
                }
                this.daoServices.updateCdDetailsInDB(consignmentDocument, null)
                this.iDemandNoteRepo.save(demand)
                this.auditService.addHistoryRecord(consignmentDocument.id!!, consignmentDocument.ucrNumber, remarks, "REJECT DEMAND NOTE", "Demand note ${demandNoteId} rejected")
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("DEMAND NOTE ERROR", ex)
        }
        return true
    }

    fun approveDemandNoteGeneration(cdUuid: String, demandNoteId: Long, supervisor: String, remarks: String): Boolean {
        try {
            val consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
            val demandNote = iDemandNoteRepo.findById(demandNoteId)
            if (demandNote.isPresent) {
                val demand = demandNote.get()
                val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
                demand.status = map.initStatus
                demand.varField3 = "APPROVED"
                demand.varField10 = remarks
                // Update CD status
                consignmentDocument.cdStandard?.let { cdStd ->
                    daoServices.updateCDStatus(cdStd, ConsignmentDocumentStatus.PAYMENT_APPROVED)
                }
                consignmentDocument.sendDemandNote = map.activeStatus
                this.daoServices.updateCdDetailsInDB(consignmentDocument, null)
                // Update demand note
                this.iDemandNoteRepo.save(demand)
                this.auditService.addHistoryRecord(consignmentDocument.id!!, consignmentDocument.ucrNumber, remarks, "APPROVED DEMAND NOTE", "Demand note ${demandNoteId} approved")
            }

        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("DEMAND NOTE ERROR", ex)
        }
        return true
    }

    fun updateDemandNoteCdStatus(cdUuid: String, demandNoteId: Long): Boolean {
        // Send demand note to user
        val demandNote = iDemandNoteRepo.findById(demandNoteId)
        try {
            // Update submission status
            if (demandNote.isPresent) {
                val consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
                val demand = demandNote.get()
                // Update CD status
                consignmentDocument.cdStandard?.let { cdStd ->
                    daoServices.updateCDStatus(
                            cdStd,
                            ConsignmentDocumentStatus.PAYMENT_APPROVED
                    )
                }
                demand.varField3 = "UPDATED DEMAND NOTE STATUS"
                consignmentDocument.varField10 = "Demand Approved, awaiting payment"
                this.daoServices.updateCdDetailsInDB(consignmentDocument, null)
                // Update Demand note status
                this.iDemandNoteRepo.save(demand)
            }
        } catch (ex: Exception) {
            if (demandNote.isPresent) {
                val demand = demandNote.get()
                demand.status = 0
                demand.varField3 = "SUBMIT TO KENTRADE FAILED"
                demand.varField5 = ex.localizedMessage
                this.iDemandNoteRepo.save(demand)
            }
        }
        return true
    }

    fun generateInvoiceBatch(cdUuid: String, demandNoteId: Long): Boolean {
        KotlinLogging.logger { }.info("INVOICE GENERATION: $demandNoteId")
        try {
            val consignmentDocument = daoServices.findCDWithUuid(cdUuid)
            //Send Demand Note
            val demandNote = daoServices.findDemandNoteWithID(demandNoteId)!!
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            // Try to add transaction to current bill or generate batch payment
            val transportDetails=daoServices.findCdTransportDetails(consignmentDocument.cdTransport?:0)
            this.billingService.registerBillTransaction(demandNote, transportDetails.blawb, map)?.let { billTrx ->
                demandNote.paymentStatus = 1
                demandNote.receiptNo = billTrx.tempReceiptNumber
                demandNote.billId = billTrx.id
                this.iDemandNoteRepo.save(demandNote)
                KotlinLogging.logger { }.info("Added demand note to bill transaction:${demandNote.demandNoteNumber} <=> ${billTrx.tempReceiptNumber}")
            } ?: run {
                demandNote.demandNoteNumber?.let {
                    KotlinLogging.logger { }.info("Generate demand note Sage:${demandNote.demandNoteNumber}")
                    val loggedInUser = commonDaoServices.findUserByUserName(demandNote.createdBy ?: "NA")
                    // Create payment batch
                    val batchInvoiceDetail = invoiceDaoService.createBatchInvoiceDetails(loggedInUser.userName!!, it)
                    // Add demand note details to batch
                    val updateBatchInvoiceDetail = invoiceDaoService.addInvoiceDetailsToBatchInvoice(
                            demandNote,
                            applicationMapProperties.mapInvoiceTransactionsForDemandNote,
                            loggedInUser,
                            batchInvoiceDetail
                    )
                    // Find recipient of the invoice from importer details
                    val importerDetails = consignmentDocument.cdImporter?.let {
                        daoServices.findCDImporterDetails(it)
                    }
                    val myAccountDetails = InvoiceDaoService.InvoiceAccountDetails()
                    with(myAccountDetails) {
                        accountName = importerDetails?.name
                        accountNumber = importerDetails?.pin
                        currency = applicationMapProperties.mapInvoiceTransactionsLocalCurrencyPrefix
                    }
                    KotlinLogging.logger { }.info("ADD STAGING TO TABLE: $demandNoteId")
                    // Create payment on staging table
                    invoiceDaoService.createPaymentDetailsOnStgReconciliationTable(
                            loggedInUser.userName!!,
                            updateBatchInvoiceDetail,
                            myAccountDetails
                    )

                } ?: ExpectedDataNotFound("Demand note number not set")
            }
            return true
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to generate batch number", ex)
        }
        return false
    }

    fun updateDemandNoteSw(cdUuid: String, demandNoteId: Long): Boolean {
        KotlinLogging.logger { }.info("UPDATE DEMAND NOTE ON SW")
        try {
            val consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
            //2. Update payment status on KenTrade
            daoServices.findDemandNoteWithID(demandNoteId)?.let {
                daoServices.sendDemandNotGeneratedToKWIS(it)
                consignmentDocument.varField10 = "DEMAND NOTE SENT TO KENTRADE, AWAITING PAYMENT"
                this.daoServices.updateCdDetailsInDB(consignmentDocument, null)
            }
            return true
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to update status", ex)
            throw ex
        }
    }

    fun sendDemandNoteStatus(demandNoteId: Long): Boolean {
        daoServices.findDemandNoteWithID(demandNoteId)?.let { demandNote ->
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            //1. Update Demand Note Status
            demandNote.swStatus = map.activeStatus
            demandNote.varField10 = "PAYMENT COMPLETED"
            val demandNoteDetails = daoServices.upDateDemandNote(demandNote)
            val consignmentDocument = demandNoteDetails.cdId?.let { cdId -> daoServices.findCD(cdId) }
            // 2. Update CD status
            consignmentDocument?.cdStandard?.let { cdStd ->
                daoServices.updateCDStatus(
                        cdStd,
                        ConsignmentDocumentStatus.PAYMENT_MADE
                )
            }
        }
        return true
    }

    fun invoicePaymentCompleted(cdId: Long, demandNoteId: Long): Boolean {
        try {
            val consignmentDocument = this.daoServices.findCD(cdId)
            // 1. Send demand payment status to SW
            daoServices.findDemandNoteWithID(demandNoteId)?.let { demandNote ->
                daoServices.sendDemandNotePayedStatusToKWIS(demandNote)
            } ?: throw ExpectedDataNotFound("Demand note with $demandNoteId was not found")
            // 2. Update application status
            consignmentDocument.varField10 = "DEMAND NOTE PAID,AWAITING INSPECTION"
            // 3. Clear Payment process completed
            consignmentDocument.diProcessStatus = 0
            consignmentDocument.diProcessInstanceId = null
            this.daoServices.updateCdDetailsInDB(consignmentDocument, null)
            return true
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("INVOICE UPDATE FAILED", ex)
        }
        return false
    }

    fun uploadExchangeRates(multipartFile: MultipartFile, fileType: String) {
        var separator = ','
        if ("TSV".equals(fileType)) {
            KotlinLogging.logger { }.info("TAB SEPARATED DATA")
            separator = '\t'
        } else {
            KotlinLogging.logger { }.info("COMMA SEPARATED DATA")
        }
        val loggedInUser = commonDaoServices.getLoggedInUser()
        val targetReader: Reader = InputStreamReader(ByteArrayInputStream(multipartFile.bytes))
        val exchangeRates = service.readExchangeRatesFromController(separator, targetReader)
        for (rate in exchangeRates) {
            val exchangeRateEntity = CurrencyExchangeRates()
            with(exchangeRateEntity) {
                applicableDate = Timestamp.from(Instant.now())
                currencyCode = rate.currencyCode
                exchangeRate = rate.exchangeRate
                description = rate.description
                status = 1
                createdBy = loggedInUser?.userName
                createdOn = Timestamp.from(Instant.now())
                modifiedOn = Timestamp.from(Instant.now())
                modifiedBy = loggedInUser?.userName
            }
            this.exchangeRateRepository.save(exchangeRateEntity)
        }
    }

    fun toSqlDate(date: LocalDateTime): java.sql.Date {
        val defaultZoneId = ZoneOffset.systemDefault()
        val sDate = Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
        return java.sql.Date(sDate.time)
    }

    fun toSqlTimestamp(date: LocalDateTime): java.sql.Timestamp {
        val sDate = Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
        return java.sql.Timestamp(sDate.time)
    }

    /**
     * List any active demand note transaction(approved by supervisor)
     */
    fun listTransactions(status: Int?, date: Optional<String>, transactionNo: Optional<String>, page: PageRequest): Page<CdDemandNoteEntity> {
        val demandNotes: Page<CdDemandNoteEntity>
        val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
        when {
            transactionNo.isPresent -> {
                KotlinLogging.logger { }.info("Search by transaction NO")
                demandNotes = iDemandNoteRepo.findByDemandNoteNumberContainingAndStatusOrderByIdAsc(transactionNo.get(), map.activeStatus, page)
            }
            date.isPresent -> {
                val searchDate = LocalDate.parse(date.get(), dateTimeFormat)
                val startDate = searchDate.atStartOfDay()
                val endDate = searchDate.plusDays(1).atStartOfDay()
                KotlinLogging.logger { }.info("Start Date: ${startDate}, End Date: ${endDate}")
                demandNotes = when (status) {
                    null -> iDemandNoteRepo.findByModifiedOnAndModifiedOnLessThanAndStatusOrderByIdAsc(dateTimeFormat.format(startDate), listOf(map.activeStatus, map.initStatus, map.workingStatus), page)
                    else -> iDemandNoteRepo.findByModifiedOnAndModifiedOnLessThanAndPaymentStatusAndStatusOrderByIdAsc(dateTimeFormat.format(startDate), status, listOf(map.activeStatus, map.initStatus, map.workingStatus), page)
                }
            }
            status != null -> {
                KotlinLogging.logger { }.info("Search by transaction status")
                demandNotes = iDemandNoteRepo.findByPaymentStatusAndStatusOrderByIdAsc(status, map.activeStatus, page)
            }
            else -> {
                KotlinLogging.logger { }.info("Search by all transactions")
                demandNotes = iDemandNoteRepo.findByStatusOrderByIdAsc(map.activeStatus, page)
            }
        }
        return demandNotes
    }

    fun getTransactionStatsOnDate(date: Optional<String>): Any? {
        val currentDate = date.orElse(dateTimeFormat.format(LocalDate.now()))
        return iDemandNoteRepo.transactionStats(currentDate)
    }

    fun paymentReceived(responseStatus: PaymentStatusResult): Boolean {
        return true
    }

}