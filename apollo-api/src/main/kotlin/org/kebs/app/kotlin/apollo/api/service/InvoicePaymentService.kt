package org.kebs.app.kotlin.apollo.api.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.payload.request.DemandGroupItem
import org.kebs.app.kotlin.apollo.api.payload.request.DemandNoteItem
import org.kebs.app.kotlin.apollo.api.payload.request.DemandNoteRequestForm
import org.kebs.app.kotlin.apollo.api.payload.request.DemandNoteRequestItem
import org.kebs.app.kotlin.apollo.api.payload.response.CallbackResponses
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.DestinationInspectionBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.*
import org.kebs.app.kotlin.apollo.api.ports.provided.sage.response.PaymentStatusResult
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.utils.generateRandomText
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.CdDemandNoteEntity
import org.kebs.app.kotlin.apollo.store.model.ServiceMapsEntity
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.di.*
import org.kebs.app.kotlin.apollo.store.repo.InvoiceBatchDetailsRepo
import org.kebs.app.kotlin.apollo.store.repo.auction.IAuctionRequestsRepository
import org.kebs.app.kotlin.apollo.store.repo.di.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.io.InputStreamReader
import java.io.Reader
import java.math.BigDecimal
import java.math.RoundingMode
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.ceil

enum class PaymentStatus(val code: Int) {
    NEW(0), PAID(1), DRAFT(3), BILLED(4), PARTIAL_PAYMENT(5), REJECTED(-1), REJECTED_AMENDMENT(-5);
}

@Service("invoiceService")
class InvoicePaymentService(
    private val iDemandNoteRepo: IDemandNoteRepository,
    private val iDemandNoteItemRepo: IDemandNoteItemsDetailsRepository,
    private val iDemandNoteGroupedItemsRepository: IDemandNoteGroupedItemsRepository,
    private val auditService: ConsignmentDocumentAuditService,
    private val reportsDaoService: ReportsDaoService,
    private val exchangeRateRepository: ICfgCurrencyExchangeRateRepository,
    private val service: DaoService,
    private val notifications: Notifications,
    private val diBpmn: DestinationInspectionBpmn,
    private val auctionRequestsRepository: IAuctionRequestsRepository,
    private val invoiceBatchDetailsRepo: InvoiceBatchDetailsRepo,
    // Demand notes
    private val currencyExchangeRateRepository: ICfgCurrencyExchangeRateRepository,
    private val paymentRepository: CdDemandNotePaymentRepository,
    private val feeRangesRepository: InspectionFeeRangesRepository,
    private val daoServices: DestinationInspectionDaoServices,
    private val invoiceDaoService: InvoiceDaoService,
    private val commonDaoServices: CommonDaoServices,
    private val applicationMapProperties: ApplicationMapProperties,
    private val amountInWordsService: AmountInWordsService,
    private val billingService: BillingService
) {
    final val DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    var dateTimeFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    fun invoiceDetails(demandNoteRef: String, ucrNumber: String? = null): Pair<Long, HashMap<String, Any>> {
        var map = hashMapOf<String, Any>()
        var demandNoteId: Long = 0
        daoServices.findDemandNoteWithReference(demandNoteRef)?.let { demandNote ->
            if (demandNote.entryNo?.equals(ucrNumber, true) == true) {
                demandNoteId = demandNote.id ?: 0
                map = this.invoidDetailsMap(demandNote)
            } else {
                throw ExpectedDataNotFound("Invalid UCR number for demand note number ${demandNoteRef}")
            }

        } ?: throw ExpectedDataNotFound("Invalid demand note number")
        return Pair(demandNoteId, map)
    }

    fun invoidDetailsMap(demandNote: CdDemandNoteEntity): HashMap<String, Any> {
        val map = hashMapOf<String, Any>()
        map["preparedBy"] = demandNote.generatedBy.toString()
        map["datePrepared"] = demandNote.dateGenerated.toString()
        map["demandNoteNo"] = demandNote.postingReference ?: "UNKNOWN"
        map["importerName"] = demandNote.nameImporter.toString()
        map["importerAddress"] = demandNote.address.toString()
        map["importerTelephone"] = demandNote.telephone.toString()
        map["ablNo"] = demandNote.entryAblNumber.toString()
        map["ucrNo"] = demandNote.entryNo.toString()
        map["totalAmount"] = demandNote.totalAmount.toString()
        map["receiptNo"] = demandNote.receiptNo.toString()
        map["amountInWords"] = demandNote.totalAmount?.let { amountInWordsService.amountToWords(it) } ?: ""
        return reportsDaoService.addBankAndMPESADetails(map, demandNote.postingReference ?: "UNKNOWN")
    }

    fun generateDemandNoteFile(demandNote: CdDemandNoteEntity): String {
        val demandNoteItemList = daoServices.findDemandNoteItemDetails(demandNote.id ?: 0)
        val map = invoidDetailsMap(demandNote)
        map["imagePath"] = commonDaoServices.resolveAbsoluteFilePath(applicationMapProperties.mapKebsLogoPath)
        val extractReport = reportsDaoService.extractReport(
            map,
            "classpath:reports/KebsDemandNoteItems.jrxml",
            demandNoteItemList
        )
        val demandNoteNumber = map["demandNoteNo"] as String
        // Response with file
        val filePath = "/tmp/DEMAND-NOTE-${demandNote.ucrNumber}-${demandNoteNumber}.pdf"
        reportsDaoService.createFileFromBytes(extractReport, filePath)
        return filePath
    }

    fun invoiceDetails(demandNoteId: Long): HashMap<String, Any> {
        var map = hashMapOf<String, Any>()
        daoServices.findDemandNoteWithID(demandNoteId)?.let { demandNote ->
            map = this.invoidDetailsMap(demandNote)
        }
        return map
    }


    fun rejectDemandNoteGeneration(cdUuid: String, demandNoteId: Long, remarks: String): Boolean {
        try {
            var consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
            val demandNote = iDemandNoteRepo.findById(demandNoteId)
            if (demandNote.isPresent) {
                val demand = demandNote.get()
                val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
                demand.status = map.invalidStatus
                demand.paymentStatus = PaymentStatus.REJECTED.code
                demand.varField3 = "REJECTED"
                demand.varField10 = remarks
                consignmentDocument.varField10 = "Demand note rejected"
                consignmentDocument.sendDemandNote = map.invalidStatus
                consignmentDocument.status = ConsignmentApprovalStatus.UNDER_INSPECTION.code
                consignmentDocument.diProcessStatus = map.inactiveStatus
                consignmentDocument.diProcessInstanceId = null
                consignmentDocument =
                    daoServices.updateCDStatus(consignmentDocument, ConsignmentDocumentStatus.PAYMENT_REJECTED)
                this.daoServices.updateCdDetailsInDB(consignmentDocument, null)
                this.iDemandNoteRepo.save(demand)
                this.auditService.addHistoryRecord(
                    consignmentDocument.id!!,
                    consignmentDocument.ucrNumber,
                    remarks,
                    "REJECT DEMAND NOTE",
                    "Demand note ${demandNoteId} rejected"
                )
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
                demand.paymentStatus = PaymentStatus.NEW.code
                demand.varField3 = "APPROVED"
                demand.varField10 = remarks

                // Update CD status
                consignmentDocument.sendDemandNote = map.activeStatus
                this.daoServices.updateCdDetailsInDB(consignmentDocument, null)
                // Update demand note
                this.iDemandNoteRepo.save(demand)
                this.auditService.addHistoryRecord(
                    consignmentDocument.id!!,
                    consignmentDocument.ucrNumber,
                    remarks,
                    "APPROVED DEMAND NOTE",
                    "Demand note ${demandNoteId} approved"
                )
            }


        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("DEMAND NOTE ERROR", ex)
        }
        return true
    }

    fun sendDemandNoteEmail(recipientEmail: String, filePath: String, demandNote: String): Boolean {
        val subject = "Demand Note: $demandNote"
        val messageBody =
            "Dear Customer,  \n" +
                    "\n " +
                    "A demand note related to your consignment has been generated on KIMS. Kindly make payment using the reference number and payment methods indicated in the attachment." +
                    "\nWarm regard,"
        var emailAddress = recipientEmail
        if (!applicationMapProperties.defaultTestEmailAddres.isNullOrEmpty()) {
            emailAddress = applicationMapProperties.defaultTestEmailAddres.orEmpty()
        }
        notifications.sendEmail(emailAddress, subject, messageBody, filePath)
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
                if (demand.paymentStatus == PaymentStatus.BILLED.code) {
                    demand.varField3 = "BILLED DEMAND NOTE STATUS"
                    consignmentDocument.varField10 = "Demand Approved, billed to user"
                    consignmentDocument.status = ConsignmentApprovalStatus.UNDER_INSPECTION.code
                    daoServices.updateCDStatus(
                        consignmentDocument,
                        ConsignmentDocumentStatus.PAYMENT_MADE
                    )
                } else {
                    demand.varField3 = "UPDATED DEMAND NOTE STATUS"
                    consignmentDocument.status = ConsignmentApprovalStatus.WAITING.code
                    consignmentDocument.varField10 = "Demand Approved, awaiting payment"
                    daoServices.updateCDStatus(
                        consignmentDocument,
                        ConsignmentDocumentStatus.PAYMENT_APPROVED
                    )
                    // Send demand note to vendor, only non-billed demand notes should be sent
                    try {
                        consignmentDocument.cdImporter?.let {
                            daoServices.findCDImporterDetails(it)
                        }?.let { importer ->
                            val filePath = generateDemandNoteFile(demand)
                            this.sendDemandNoteEmail(importer.email ?: "", filePath, demand.demandNoteNumber ?: "")
                        }
                    } catch (ex: Exception) {
                        KotlinLogging.logger { }.error("Failed to send demand note to importer", ex)
                    }
                }
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
            if (demandNote.varField5.isNullOrEmpty()) {
                demandNote.varField5 =
                    "SAGEREF${generateRandomText(3, map.secureRandom, map.messageDigestAlgorithm, true).toUpperCase()}"
            }
            // Try to add transaction to current bill or generate batch payment
            val loggedInUser = commonDaoServices.findUserByUserName(demandNote.createdBy ?: "NA")
            this.billingService.registerBillTransaction(
                demandNote, demandNote.courierPin, consignmentDocument.cdType?.varField1
                    ?: "", map
            )?.let { billTrx ->
                demandNote.paymentStatus = PaymentStatus.BILLED.code
                demandNote.receiptNo = billTrx.tempReceiptNumber
                demandNote.billId = billTrx.id
                val dn = this.iDemandNoteRepo.save(demandNote)
                KotlinLogging.logger { }
                    .info("Added demand note to bill transaction:${demandNote.demandNoteNumber} <=> ${billTrx.tempReceiptNumber}")
                // Create bill payment on SAGE
                invoiceDaoService.postRequestToSage(
                    loggedInUser.userName!!,
                    dn
                )
                KotlinLogging.logger { }
                    .info("Posted demand note to SAGE:${demandNote.demandNoteNumber} <=> ${billTrx.tempReceiptNumber}")
            } ?: run {
                demandNote.demandNoteNumber?.let {
                    KotlinLogging.logger { }.info("Generate demand note Sage:${demandNote.demandNoteNumber}")
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
                    val myAccountDetails = InvoiceDaoService.InvoiceAccountDetails()
                    with(myAccountDetails) {
                        accountName = demandNote.nameImporter
                        accountNumber = demandNote.importerPin
                        currency = applicationMapProperties.mapInvoiceTransactionsLocalCurrencyPrefix
                    }
                    KotlinLogging.logger { }.info("SENDING to SAGE FOR PAYMENT: $demandNoteId")
                    // Create payment on S                                                                                                                                                                                                                              AGE
                    invoiceDaoService.postRequestToSage(
                        loggedInUser.userName!!,
                        demandNote
                    )
                } ?: ExpectedDataNotFound("Demand note number not set")
            }
            return true
        } catch (ex: ExpectedDataNotFound) {
            throw ex
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to generate batch number", ex)
            throw ex
        }
    }

    fun generateOtherInvoiceBatch(demandNoteId: Long): ApiResponseModel {
        val response = ApiResponseModel()
        KotlinLogging.logger { }.info("INVOICE GENERATION: $demandNoteId")
        try {
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            //Send Demand Note
            val demandNote = daoServices.findDemandNoteWithID(demandNoteId)!!
            if (demandNote.postingStatus == 1) {
                response.responseCode = ResponseCodes.INVALID_CODE
                response.responseCode = "Demand note is already posted"
                return response
            } else if (demandNote.varField5.isNullOrEmpty()) {
                demandNote.varField5 =
                    "SAGEREF${generateRandomText(3, map.secureRandom, map.messageDigestAlgorithm, true).toUpperCase()}"
            }

            // Try to add transaction to current bill or generate batch payment
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
                val myAccountDetails = InvoiceDaoService.InvoiceAccountDetails()
                with(myAccountDetails) {
                    accountName = demandNote.nameImporter
                    accountNumber = demandNote.importerPin
                    currency = applicationMapProperties.mapInvoiceTransactionsLocalCurrencyPrefix
                }
                KotlinLogging.logger { }.info("SENDING to SAGE FOR PAYMENT: $demandNoteId")
                // Create payment on SAGE
                invoiceDaoService.postRequestToSage(
                    loggedInUser.userName!!,
                    demandNote
                )
                response.message = "Payment request submitted"
                response.responseCode = ResponseCodes.SUCCESS_CODE
            } ?: ExpectedDataNotFound("Demand note number not set")
        } catch (ex: ExpectedDataNotFound) {
            response.message = ex.localizedMessage
            response.responseCode = ResponseCodes.FAILED_CODE
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to generate batch number", ex)
            response.errors = ex.localizedMessage
            response.message = " Request failed, please try again later"
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
        }
        return response
    }

    fun updateDemandNoteSw(cdUuid: String, demandNoteId: Long): Boolean {
        KotlinLogging.logger { }.info("UPDATE DEMAND NOTE ON SW")
        try {
            val consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
            //2. Update payment status on KenTrade
            daoServices.findDemandNoteWithID(demandNoteId)?.let {
                daoServices.sendDemandNotGeneratedToKWIS(it, consignmentDocument.version?.toString() ?: "1")
                consignmentDocument.varField10 = "DEMAND NOTE SENT TO KENTRADE, AWAITING PAYMENT"
                this.daoServices.updateCdDetailsInDB(consignmentDocument, null)
                // Send payment status if its billed
                if (it.paymentStatus == PaymentStatus.BILLED.code) {
                    diBpmn.triggerDemandNotePaidBpmTask(it)
                }
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
            val consignmentDocument = daoServices.findCD(demandNoteDetails.cdId!!)
            // 2. Update CD status
            when (demandNote.paymentStatus) {
                PaymentStatus.PAID.code -> {
                    daoServices.updateCDStatus(
                        consignmentDocument,
                        ConsignmentDocumentStatus.PAYMENT_MADE
                    )
                }
                PaymentStatus.BILLED.code -> {
                    daoServices.updateCDStatus(
                        consignmentDocument,
                        ConsignmentDocumentStatus.PAYMENT_BILLED
                    )
                }
                PaymentStatus.PARTIAL_PAYMENT.code -> {
                    daoServices.updateCDStatus(
                        consignmentDocument,
                        ConsignmentDocumentStatus.PARTIAL_PAYMENT_MADE
                    )
                }
            }
            return true
        }
        return false
    }

    fun invoicePaymentCompleted(cdId: Long, demandNoteId: Long, amount: BigDecimal? = null): Boolean {
        try {
            var consignmentDocument = this.daoServices.findCD(cdId)
            // 1. Send demand payment status to SW
            val demandNote = daoServices.findDemandNoteWithID(demandNoteId)?.let { demandNote ->
                if (amount != null) {
                    daoServices.sendDemandNotePayedStatusToKWIS(
                        demandNote, amount, consignmentDocument.version?.toString()
                            ?: "1"
                    )
                } else {
                    daoServices.sendDemandNotePayedStatusToKWIS(
                        demandNote, demandNote.totalAmount, consignmentDocument.version?.toString()
                            ?: "1"
                    )
                }
                demandNote
            } ?: throw ExpectedDataNotFound("Demand note with $demandNoteId was not found")

            // 2. Update application status
            consignmentDocument.varField10 = "DEMAND NOTE PAID,AWAITING INSPECTION"
            // 3. Clear Payment process completed
            consignmentDocument.diProcessStatus = 0
            consignmentDocument.diProcessInstanceId = null
            consignmentDocument.status = ConsignmentApprovalStatus.UNDER_INSPECTION.code
            consignmentDocument = this.daoServices.updateCdDetailsInDB(consignmentDocument, null)
            // 4. Update payment status for invoice
            if (demandNote.paymentStatus == PaymentStatus.PAID.code) {
                this.daoServices.updateCDStatus(consignmentDocument, ConsignmentDocumentStatus.PAYMENT_MADE)
            } else if (demandNote.paymentStatus == PaymentStatus.BILLED.code) {
                this.daoServices.updateCDStatus(consignmentDocument, ConsignmentDocumentStatus.PAYMENT_BILLED)
            } else {
                this.daoServices.updateCDStatus(consignmentDocument, ConsignmentDocumentStatus.PARTIAL_PAYMENT_MADE)
            }
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
            // Clear the old rate
            this.exchangeRateRepository.findAllByCurrencyCodeAndCurrentRate(rate.currencyCode!!, 1).forEach { oldRate ->
                oldRate.currentRate = 0
                oldRate.changeDate = Timestamp.from(Instant.now())
                oldRate.modifiedOn = Timestamp.from(Instant.now())
                oldRate.modifiedBy = loggedInUser?.userName
                this.exchangeRateRepository.save(oldRate)
            }
            // Add new rate
            with(exchangeRateEntity) {
                applicableDate = Timestamp.from(Instant.now())
                currencyCode = rate.currencyCode
                exchangeRate = rate.exchangeRate
                description = rate.description
                currentRate = 1
                status = 1
                createdBy = loggedInUser?.userName
                createdOn = Timestamp.from(Instant.now())
                modifiedOn = Timestamp.from(Instant.now())
                modifiedBy = loggedInUser?.userName
            }
            this.exchangeRateRepository.save(exchangeRateEntity)
        }
    }

    /**
     * List any active demand note transaction(approved by supervisor)
     */
    fun listTransactions(
        status: Int?,
        date: Optional<String>,
        endDate: Optional<String>,
        transactionNo: Optional<String>,
        page: PageRequest,
    ): Page<CdDemandNoteEntity> {
        val demandNotes: Page<CdDemandNoteEntity>
        val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
        when {
            transactionNo.isPresent -> {
                KotlinLogging.logger { }.info("Search by transaction NO")
                demandNotes =
                    iDemandNoteRepo.findByDemandNoteNumberContainingOrPostingReferenceContainingAndPaymentStatusInOrderByIdAsc(
                        transactionNo.get(),
                        transactionNo.get(),
                        listOf(map.activeStatus, 0),
                        page
                    )
            }
            date.isPresent && endDate.isPresent -> {
                val searchDate = LocalDate.parse(date.get(), dateTimeFormat).atStartOfDay()
                val periodEnd = LocalDate.parse(endDate.get(), dateTimeFormat).plusDays(1).atStartOfDay()
                KotlinLogging.logger { }.info("Date range: Start Date: ${searchDate}, End Date: $periodEnd")
                demandNotes = iDemandNoteRepo.findByDateGenerateBetweenAndPaymentStatusOrderByIdAsc(
                    dateTimeFormat.format(searchDate),
                    dateTimeFormat.format(periodEnd),
                    listOf(map.activeStatus, map.initStatus, map.workingStatus),
                    page
                )
            }
            date.isPresent -> {
                val searchDate = LocalDate.parse(date.get(), dateTimeFormat)
                val startDate = searchDate.atStartOfDay()
                val periodEnd = searchDate.plusDays(1).atStartOfDay()
                KotlinLogging.logger { }.info("Start Date: ${startDate}, End Date: $periodEnd")
                demandNotes = when (status) {
                    null -> iDemandNoteRepo.findByModifiedOnAndModifiedOnLessThanAndPaymentStatusOrderByIdAsc(
                        dateTimeFormat.format(startDate),
                        listOf(map.activeStatus, map.initStatus, map.workingStatus),
                        page
                    )
                    else -> iDemandNoteRepo.findByModifiedOnAndModifiedOnLessThanAndPaymentStatusAndStatusOrderByIdAsc(
                        dateTimeFormat.format(startDate),
                        status,
                        listOf(map.activeStatus, map.initStatus, map.workingStatus),
                        page
                    )
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

    fun receivePayment(demandNote: CdDemandNoteEntity, responseStatus: PaymentStatusResult): CdDemandNoteEntity {
        val paymentRef = responseStatus.response?.paymentReferenceNo?.trim()?.toUpperCase() ?: ""
        var payment = this.paymentRepository.findByReceiptNumber(paymentRef)
        if (payment != null) {
            throw ExpectedDataNotFound("Payment with receipt number already exists, duplicate receipt number")
        }
        payment = CdDemandNotePaymentEntity()
        payment.demandNoteId = demandNote
        payment.previousBalance = demandNote.currentBalance ?: demandNote.totalAmount ?: BigDecimal.ZERO
        payment.amount = responseStatus.response?.paymentAmount ?: BigDecimal.ZERO
        payment.balanceAfter = demandNote.currentBalance?.minus(payment.amount ?: BigDecimal.ZERO)
        payment.referenceNumber = responseStatus.response?.additionalInfo
        payment.paymentSource = responseStatus.response?.paymentCode
        payment.receiptNumber = paymentRef
        payment.receiptDate = responseStatus.response?.paymentDate
        payment.varField5 = responseStatus.response?.additionalInfo
        payment.varField6 = responseStatus.response?.currencyCode
        payment.varField7 = responseStatus.response?.paymentDescription
        commonDaoServices.loggedInUserAuthentication().let {
            payment.modifiedBy = it.name
            payment.createdBy = it.name
        }
        payment.status = 0
        if ("00" == responseStatus.header?.statusCode) {
            payment.status = 1
        }
        payment.createdOn = commonDaoServices.getTimestamp()
        payment.modifiedOn = commonDaoServices.getTimestamp()
        val updatedPayment = paymentRepository.save(payment)
        // Last payment reference
        demandNote.paymentSource = responseStatus.response?.paymentCode
        demandNote.receiptDate = responseStatus.response?.paymentDate
        demandNote.varField5 = responseStatus.response?.additionalInfo
        demandNote.varField6 = responseStatus.response?.currencyCode
        demandNote.varField7 = responseStatus.response?.paymentDescription
        demandNote.receiptNo = responseStatus.response?.paymentReferenceNo
        // Update demand note
        if (payment.status == 1) {
            paymentRepository.sumByDemandNoteIdAndStatus(demandNote.id!!, 1)?.let {
                demandNote.amountPaid = it
                demandNote.currentBalance = demandNote.totalAmount?.minus(demandNote.amountPaid ?: BigDecimal.ZERO)
                updatedPayment.balanceAfter = demandNote.currentBalance
                paymentRepository.save(updatedPayment)
            } ?: throw ExpectedDataNotFound("Could not calculate the total")
        }
        // Payment
        return iDemandNoteRepo.save(demandNote)
    }

    fun paymentReceived(responseStatus: PaymentStatusResult): CallbackResponses {
        val response = CallbackResponses()
        val paymentRef = responseStatus.response?.demandNoteNo?.trim()?.toUpperCase()
            ?: ""
        iDemandNoteRepo.findByPostingReference(paymentRef)?.let { demandNote ->
            if (demandNote.paymentStatus == PaymentStatus.PAID.code) {
                throw ExpectedDataNotFound("Payment has already been made, duplicate callback")
            }
            // Check duplicate receipts
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            val demandNoteUpdated = receivePayment(demandNote, responseStatus)
            if (demandNoteUpdated.currentBalance!! <= BigDecimal.ZERO) {
                demandNoteUpdated.paymentStatus = map.activeStatus
            } else {
                demandNoteUpdated.paymentStatus = PaymentStatus.PARTIAL_PAYMENT.code
            }
            // Check for payment completed status
            if (demandNoteUpdated.paymentStatus == map.activeStatus) {
                demandNoteUpdated.paymentDate = Timestamp.from(Instant.now())
                demandNoteUpdated.receiptDate = responseStatus.response?.paymentDate
                invoiceBatchDetailsRepo.findByBatchNumber(demandNoteUpdated.demandNoteNumber!!)?.let { batch ->
                    batch.receiptNumber = responseStatus.response?.paymentReferenceNo?.trim()?.toUpperCase() ?: ""
                    batch.paymentStarted = map.activeStatus
                    batch.receiptDate = responseStatus.response?.paymentDate
                    invoiceBatchDetailsRepo.save(batch)
                }
                response.status = ResponseCodes.SUCCESS_CODE
                response.message = "Full Payment received"
                // Complete any bill payment with Demand Note
                if (demandNoteUpdated.billId != null) {
                    billingService.demandNoteTransactionPaid(
                        demandNoteUpdated.demandNoteNumber
                            ?: "", demandNoteUpdated.receiptNo
                            ?: "", demandNoteUpdated.billId!!
                    )
                }
            } else {
                response.data = mutableMapOf(
                    Pair("amountPaid", demandNoteUpdated.amountPaid),
                    Pair("totalAmount", demandNoteUpdated.totalAmount)
                )
                response.status = ResponseCodes.SUCCESS_CODE
                response.message = "Partial Payment received"
            }
            iDemandNoteRepo.save(demandNoteUpdated)
            // Completion event
            GlobalScope.launch(Dispatchers.IO) {
                delay(100L)
                when (demandNoteUpdated.paymentPurpose) {
                    PaymentPurpose.CONSIGNMENT.code -> {
                        // Complete billed
                        when (demandNote.billId) {
                            0.toLong(), null -> invoicePaymentCompleted(
                                demandNoteUpdated.cdId!!,
                                demandNoteUpdated.id!!,
                                responseStatus.response?.paymentAmount
                            )
                        }
                    }
                    PaymentPurpose.AUDIT.code -> {
                        if (demandNoteUpdated.paymentStatus == map.activeStatus) {
                            paymentCompleted(demandNoteUpdated.cdId!!, demandNoteUpdated.id!!)
                        }
                    }
                    else -> {
                        KotlinLogging.logger { }
                            .info("Unhandled payment completion ${demandNoteUpdated.id}-${demandNoteUpdated.paymentPurpose}")
                    }
                }
            }
        }
            ?: throw ExpectedDataNotFound("Invalid transaction reference number: ${responseStatus.response?.demandNoteNo}")

        return response
    }

    fun paymentCompleted(auctionId: Long, demandNoteId: Long) {
        val opttional = this.auctionRequestsRepository.findById(auctionId)
        if (opttional.isPresent) {
            val auctionRequest = opttional.get()
            auctionRequest.status = AuctionGoodStatus.PAYMENT_COMPLETED.status
            auctionRequest.varField7 = "PAYMENT MADE"
            auctionRequest.modifiedOn = commonDaoServices.getTimestamp()
            auctionRequestsRepository.save(auctionRequest)
        }
    }

    fun createPaymentItemDetails(
        cdDetails: ConsignmentDocumentDetailsEntity,
        itm: DemandNoteItem,
        presentment: Boolean,
        map: ServiceMapsEntity,
        loggedInUser: UsersEntity
    ): DemandNoteRequestItem {
        val formItem = DemandNoteRequestItem()
        formItem.route = cdDetails.cdStandardsTwo?.cocType ?: "L"
        formItem.fee = daoServices.findDIFee(itm.feeId)
        if (itm.items.isNullOrEmpty()) {
            val item = daoServices.findItemWithItemIDAndDocument(cdDetails, itm.itemId)
            formItem.itemValue = item.totalPriceNcy
            formItem.productName = item.itemDescription ?: item.hsDescription ?: item.productTechnicalName
            formItem.itemId = item.id

            formItem.currency = item.foreignCurrencyCode
            formItem.quantity = item.quantity?.toLong() ?: 0
            // Update demand note status
            if (presentment) {
                item.dnoteStatus = map.activeStatus
                daoServices.updateCdItemDetailsInDB(
                    commonDaoServices.updateDetails(
                        item,
                        item
                    ) as CdItemDetailsEntity, loggedInUser
                )
            }
        } else {
            formItem.itemValue = BigDecimal.ZERO
            formItem.quantity = 0
            formItem.productName = "Grouped pricing"
            formItem.items = mutableListOf()
            formItem.quantity = (itm.items?.size ?: 0).toLong()
            itm.items?.forEach {
                val item = daoServices.findItemWithItemIDAndDocument(cdDetails, it)
                formItem.itemValue = formItem.itemValue?.plus(item.totalPriceNcy ?: BigDecimal.ZERO) ?: BigDecimal.ZERO
                formItem.currency = item.foreignCurrencyCode

                // Add group items
                val groupItem = DemandGroupItem()
                groupItem.currency = item.foreignCurrencyCode
                groupItem.itemId = item.id
                groupItem.itemValue = item.totalPriceNcy
                groupItem.productName = item.itemDescription ?: item.hsDescription ?: item.productTechnicalName
                groupItem.quantity = item.quantity?.toLong() ?: 0
                formItem.items?.add(groupItem)
                // Update demand note status
                if (presentment) {
                    item.dnoteStatus = map.activeStatus
                    daoServices.updateCdItemDetailsInDB(
                        commonDaoServices.updateDetails(
                            item,
                            item
                        ) as CdItemDetailsEntity, loggedInUser
                    )
                }
            }
        }
        return formItem
    }

    @Transactional
    fun generateDemandNoteWithItemList(
        form: DemandNoteRequestForm,
        items: MutableList<CdDemandNoteItemsDetailsEntity>,
        map: ServiceMapsEntity,
        purpose: PaymentPurpose,
        user: UsersEntity,
    ): CdDemandNoteEntity {
        return iDemandNoteRepo.findFirstByCdIdAndStatusInAndPaymentStatusIn(
            form.referenceId!!,
            listOf(map.workingStatus),
            listOf(PaymentStatus.DRAFT.code)
        )
            ?.let { demandNote ->
                var demandNoteDetails = demandNote
                // Remove all items for update
                val demandNoteItems = iDemandNoteItemRepo.findByDemandNoteId(demandNote.id!!)
                for (itm in demandNoteItems) {
                    iDemandNoteItemRepo.delete(itm)
                }
                //Call Function to add Item Details To be attached To The Demand note
                demandNote.totalAmount = BigDecimal.ZERO
                demandNote.amountPayable = BigDecimal.ZERO
                demandNote.courierPin = form.courierPin
                demandNote.revenueLine = form.revenueLineNumber
                demandNote.courier = form.courier
                form.items?.forEach {
                    items.add(addItemDetailsToDemandNote(it, demandNoteDetails, map, form.presentment, user))
                }
                // Foreign CoR without Items
                if (form.items?.isEmpty() == true) {
                    demandNoteDetails.totalAmount = form.amount.toBigDecimal()
                    demandNoteDetails.amountPayable = form.amount.toBigDecimal()
                }
                //Calculate the total Amount for Items In one Cd To be paid For
                if (!form.presentment) {
                    demandNoteDetails = calculateTotalAmountDemandNote(demandNoteDetails, user, form.presentment)
                }
                demandNoteDetails
            }
            ?: kotlin.run {
                var demandNote = CdDemandNoteEntity()
                with(demandNote) {
                    cdId = form.referenceId
                    nameImporter = form.name
                    importerPin = form.importerPin
                    address = form.address
                    telephone = form.phoneNumber
                    cdRefNo = form.referenceNumber
                    shippingAgent = form.customsOffice
                    courier = form.courier
                    entryNo = form.entryNo
                    entryPoint = form.entryPoint
                    entryAblNumber = form.ablNumber
                    totalAmount = BigDecimal.ZERO
                    amountPayable = BigDecimal.ZERO
                    revenueLine = form.revenueLineNumber
                    receiptNo = "NOT PAID"
                    product = form.product ?: "UNKNOWN"
                    rate = "UNKNOWN"
                    currency = applicationMapProperties.applicationCurrencyCode
                    courierPin = form.courierPin
                    ucrNumber = form.ucrNumber
                    cfvalue = BigDecimal.ZERO
                    //Generate Demand note number
                    demandNoteNumber =
                        "KIMS${form.invoicePrefix}${
                            generateRandomText(
                                5,
                                map.secureRandom,
                                map.messageDigestAlgorithm,
                                true
                            )
                        }".toUpperCase()
                    paymentStatus = PaymentStatus.DRAFT.code
                    paymentPurpose = purpose.code
                    dateGenerated = commonDaoServices.getCurrentDate()
                    generatedBy = commonDaoServices.concatenateName(user)
                    status = map.workingStatus
                    varField3 = "DRAFT"
                    createdOn = commonDaoServices.getTimestamp()
                    createdBy = commonDaoServices.getUserName(user)
                }
                if (!form.presentment) {
                    demandNote = iDemandNoteRepo.save(demandNote)
                }
                //Call Function to add Item Details To be attached To The Demand note
                form.items?.forEach {
                    items.add(addItemDetailsToDemandNote(it, demandNote, map, form.presentment, user))
                }
                if (!form.presentment) {
                    demandNote = calculateTotalAmountDemandNote(demandNote, user, form.presentment)
                }
                // Foreign CoR/CoC without Items
                if (form.items?.isEmpty() == true) {
                    demandNote.totalAmount = form.amount.toBigDecimal().setScale(2, RoundingMode.UP)
                    demandNote.amountPayable = form.amount.toBigDecimal().setScale(2, RoundingMode.UP)
                }
                return demandNote

            }
    }

    private fun demandNoteCalculation(
        demandNoteItem: CdDemandNoteItemsDetailsEntity,
        diInspectionFeeId: DestinationInspectionFeeEntity,
        currencyCode: String,
        documentType: String,
        itemId: Long?
    ) {
        val cfiValue = demandNoteItem.cfvalue ?: BigDecimal.ZERO
        var minimumUsd: BigDecimal? = null
        var maximumUsd: BigDecimal? = null
        var minimumKes: BigDecimal = BigDecimal.ZERO
        var maximumKes: BigDecimal = BigDecimal.ZERO
        var fixedAmount: BigDecimal = BigDecimal.ZERO
        var rate: BigDecimal? = null
        var fee: InspectionFeeRanges? = null
        //1. Handle ranges in the fee depending on amounts
        var feeType = diInspectionFeeId.rateType
        var checkMinMaxValues = true;
        if ("RANGE".equals(diInspectionFeeId.rateType)) {
            var documentOrigin = "LOCAL"
            when (documentType) {
                "L" -> {
                    documentOrigin = "LOCAL"
                }
                "F" -> {
                    documentOrigin = "PVOC"
                }
                "A" -> {
                    documentOrigin = "AUCTION"
                }
                else -> {
                    documentOrigin = "LOCAL"
                }
            }
            KotlinLogging.logger { }.info("$documentOrigin CFI DOCUMENT TYPE = $currencyCode-$cfiValue")
            val feeRange =
                this.feeRangesRepository.findByInspectionFeeAndMinimumKshGreaterThanEqualAndMaximumKshLessThanEqual(
                    diInspectionFeeId.id,
                    cfiValue,
                    documentOrigin
                )
            if (feeRange.isEmpty()) {
                throw Exception("Item details with Id = ${itemId}, does not Have any range For payment Fee Id Selected $cfiValue")
            } else {
                fee = feeRange.get(0)
            }
            minimumUsd = fee.minimumUsd?.let { convertAmount(it, "USD") }
            maximumUsd = fee.maximumUsd?.let { convertAmount(it, "USD") }
            minimumKes = BigDecimal.ZERO
            maximumKes = BigDecimal.ZERO
            // Min/Max values
            if (minimumUsd != null || maximumUsd != null) {
                checkMinMaxValues = false
            }
            fixedAmount = fee.fixedAmount ?: BigDecimal.ZERO
            feeType = fee.rateType
            rate = fee.rate
        } else {
            minimumUsd = diInspectionFeeId.minimumUsd?.let { convertAmount(it.toBigDecimal(), "USD") }
            maximumUsd = diInspectionFeeId.higher?.let { convertAmount(it.toBigDecimal(), "USD") }
            minimumKes = diInspectionFeeId.minimumKsh?.toBigDecimal() ?: BigDecimal.ZERO
            maximumKes = diInspectionFeeId.maximumKsh?.toBigDecimal() ?: BigDecimal.ZERO
            fixedAmount = diInspectionFeeId.amountKsh?.toBigDecimal() ?: BigDecimal.ZERO
            rate = diInspectionFeeId.rate
            feeType = diInspectionFeeId.rateType
        }
        val percentage = 100
        var amount: BigDecimal? = null
        KotlinLogging.logger { }.info("$feeType CFI AMOUNT BEFORE CALCULATION = $currencyCode-$cfiValue")
        //2. Calculate based on the ranges provided
        demandNoteItem.rate = "0.00"
        when (feeType) {
            "PERCENTAGE" -> {
                demandNoteItem.rate = rate?.setScale(2, RoundingMode.UP)?.toString()
                amount = cfiValue.multiply(rate).divide(percentage.toBigDecimal())
                demandNoteItem.amountPayable = BigDecimal(amount?.toDouble() ?: 0.0)
                KotlinLogging.logger { }.info("$feeType MY AMOUNT BEFORE CALCULATION = $currencyCode-$amount")
                //3.  APPLY MAX AND MIN VALUES Prefer USD setting to KES setting
                if (checkMinMaxValues) {
                    amount?.let {
                        when (minimumUsd) {
                            null -> {
                                demandNoteItem.minimumAmount = minimumKes
                                demandNoteItem.maximumAmount = maximumKes
                                if (it < minimumKes && minimumKes > BigDecimal.ZERO) {
                                    amount = minimumKes
                                } else if (it > maximumKes && maximumKes < BigDecimal.ZERO) {
                                    amount = maximumKes
                                }
                            }
                            else -> {
                                demandNoteItem.minimumAmount = minimumUsd
                                demandNoteItem.maximumAmount = maximumUsd
                                if (it < minimumUsd && minimumUsd > BigDecimal.ZERO) {
                                    amount = minimumUsd
                                } else if (maximumUsd != null && it > maximumUsd && maximumUsd > BigDecimal.ZERO) {
                                    amount = maximumUsd
                                }
                            }
                        }
                    }
                }
                demandNoteItem.adjustedAmount = roundUpNextInteger(amount?.setScale(2, RoundingMode.UP))
                KotlinLogging.logger { }.info("$feeType MY AMOUNT AFTER CALCULATION = $amount")
            }
            "FIXED" -> {
                amount = fixedAmount
                KotlinLogging.logger { }.info("FIXED AMOUNT BEFORE CALCULATION = $fixedAmount")
                demandNoteItem.amountPayable = BigDecimal.ZERO
                demandNoteItem.adjustedAmount = roundUpNextInteger(fixedAmount.setScale(2, RoundingMode.UP))
                demandNoteItem.amountPayable = fixedAmount.setScale(2, RoundingMode.UP)
            }
            "MANUAL" -> {
                // Not-Applicable to items
                amount = BigDecimal.ZERO
                demandNoteItem.adjustedAmount = roundUpNextInteger(amount?.setScale(2, RoundingMode.UP))
                demandNoteItem.amountPayable = amount?.setScale(2, RoundingMode.UP)
                KotlinLogging.logger { }.info("MANUAL AMOUNT BEFORE CALCULATION = $amount")
            }
            else -> {
                amount = BigDecimal.ZERO
                demandNoteItem.adjustedAmount = roundUpNextInteger(amount?.setScale(2, RoundingMode.UP))
                demandNoteItem.amountPayable = amount?.setScale(2, RoundingMode.UP)
            }
        }
    }

    fun convertAmount(amount: BigDecimal?, currencyCode: String): BigDecimal {
        return currencyExchangeRateRepository.findFirstByCurrencyCodeAndApplicableDateOrderByApplicableDateDesc(
            currencyCode,
            DATE_FORMAT.format(LocalDate.now())
        )?.let { exchangeRateEntity ->
            return amount?.times(
                exchangeRateEntity.exchangeRate
                    ?: BigDecimal.ZERO
            ) ?: BigDecimal.ZERO
        } ?: run {
            currencyExchangeRateRepository.findFirstByCurrencyCodeAndCurrentRateAndStatus(currencyCode, 1, 1)
                ?.let { exchangeRateEntity ->
                    return amount?.times(
                        exchangeRateEntity.exchangeRate
                            ?: BigDecimal.ZERO
                    ) ?: BigDecimal.ZERO
                } ?: run {
                throw ExpectedDataNotFound("Conversion rate for currency $currencyCode not found")
            }
        }
    }

    fun convertAmount(amount: BigDecimal?, currencyCode: String, demandNoteItem: CdDemandNoteItemsDetailsEntity?) {
//        val exchangeRate=BigDecimal.ZERO
        currencyExchangeRateRepository.findFirstByCurrencyCodeAndApplicableDateOrderByApplicableDateDesc(
            currencyCode,
            DATE_FORMAT.format(LocalDate.now())
        )?.let { exchangeRateEntity ->
            demandNoteItem?.exchangeRateId = exchangeRateEntity.id
            demandNoteItem?.rate = exchangeRateEntity.exchangeRate?.toPlainString() ?: "0.00"
            demandNoteItem?.cfvalue = amount?.times(
                exchangeRateEntity.exchangeRate
                    ?: BigDecimal.ZERO
            ) ?: BigDecimal.ZERO
        } ?: run {
            KotlinLogging.logger { }.warn("Exchange rate for today not found, using the last known exchange rate")
            currencyExchangeRateRepository.findFirstByCurrencyCodeAndCurrentRateAndStatus(currencyCode, 1, 1)
                ?.let { exchangeRateEntity ->
                    demandNoteItem?.cfvalue = amount?.times(
                        exchangeRateEntity.exchangeRate
                            ?: BigDecimal.ZERO
                    ) ?: BigDecimal.ZERO
                    demandNoteItem?.exchangeRateId = exchangeRateEntity.id
                    demandNoteItem?.rate = exchangeRateEntity.exchangeRate?.toPlainString() ?: "0.00"
                } ?: run {
                demandNoteItem?.cfvalue = amount ?: BigDecimal.ZERO
                throw ExpectedDataNotFound("Conversion rate for currency $currencyCode not found")
            }
        }
    }

    private fun addItemDetailsToDemandNote(
        itemDetails: DemandNoteRequestItem, demandNote: CdDemandNoteEntity, map: ServiceMapsEntity,
        presentment: Boolean,
        user: UsersEntity
    ): CdDemandNoteItemsDetailsEntity {
        val fee = itemDetails.fee
            ?: throw Exception("Item details with Id = ${itemDetails.itemId}, does not Have any Details For payment Fee Id Selected ")
        var demandNoteItem =
            iDemandNoteItemRepo.findByItemIdAndDemandNoteIdAndFeeId(itemDetails.itemId, demandNote.id, fee.id)
        if (demandNoteItem == null) {
            demandNoteItem = CdDemandNoteItemsDetailsEntity()
        }
        // Apply currency conversion rates for today
        if (applicationMapProperties.applicationCurrencyConversionEnabled) {
            when (itemDetails.currency?.toUpperCase()) {
                applicationMapProperties.applicationCurrencyCode, "KES", "KSH" -> {
                    demandNoteItem.cfvalue = itemDetails.itemValue
                    demandNote.rate = "0.00"
                }
                else -> {
                    convertAmount(itemDetails.itemValue, "${itemDetails.currency}", demandNoteItem)
                    demandNote.rate = demandNoteItem.rate ?: "0.00"
                    KotlinLogging.logger { }
                        .warn("Exchange Rate for ${itemDetails.currency}:${itemDetails.itemValue} => ${demandNoteItem.cfvalue}")
                }
            }
        } else {
            demandNoteItem.cfvalue = itemDetails.itemValue
            demandNote.rate = "0.00"
        }
        // Add extra data
        with(demandNoteItem) {
            itemId = itemDetails.itemId
            varField1 = itemDetails.quantity.toString()
            demandNoteId = demandNote.id
            feeId = fee.id
            product = itemDetails.productName
            rate = fee.rate?.toString()
            rateType = fee.rateType
            feeName = fee.name ?: fee.description
            // Demand note Calculation Details
            status = map.activeStatus
            createdOn = commonDaoServices.getTimestamp()
            createdBy = commonDaoServices.getUserName(user)
        }
        // Apply fee type and adjustments
        demandNoteCalculation(
            demandNoteItem,
            fee,
            applicationMapProperties.applicationCurrencyCode,
            itemDetails.route,
            itemDetails.itemId
        )
        // Skip saving for presentment
        if (!presentment) {
            if (!itemDetails.items.isNullOrEmpty()) {
                demandNoteItem.groupType = "GROUP"
            }
            val saved = iDemandNoteItemRepo.save(demandNoteItem)
            // Add Grouped Items
            itemDetails.items?.forEach { itm ->
                val groupItem = CdDemandNoteGroupedItemsEntity()
                groupItem.cfValue = itm.itemValue
                groupItem.itemId = itm.itemId
                groupItem.description = itm.productName
                groupItem.product = itm.productName
                groupItem.status = 1
                groupItem.itemGroupId = saved.id
                this.iDemandNoteGroupedItemsRepository.save(groupItem)
            }
        }
        var calculatedAmount = BigDecimal.ZERO
        try {
            val d = demandNote.varField4?.toBigDecimal() ?: BigDecimal.ZERO
            calculatedAmount = d
        } catch (ignored: Exception) {
        }
        // Add this for presentment purposes
        demandNote.totalAmount = demandNote.totalAmount?.plus(demandNoteItem.adjustedAmount ?: BigDecimal.ZERO)
        demandNote.amountPayable = demandNote.amountPayable?.plus(demandNoteItem.amountPayable ?: BigDecimal.ZERO)
        calculatedAmount = calculatedAmount.plus(demandNoteItem.amountPayable ?: BigDecimal.ZERO)
        demandNote.varField4 = calculatedAmount.setScale(2, RoundingMode.UP).toString()
        demandNote.cfvalue = demandNote.cfvalue?.plus(demandNote.cfvalue ?: BigDecimal.ZERO)
        return demandNoteItem
    }

    private fun roundUpNextInteger(dd: BigDecimal?): BigDecimal {
        return dd?.let { ddd ->
            val upped = ceil(ddd.toDouble())
            BigDecimal.valueOf(upped).setScale(2, RoundingMode.HALF_UP)
        } ?: BigDecimal.ZERO
    }

    private fun calculateTotalAmountDemandNote(
        demandNote: CdDemandNoteEntity,
        user: UsersEntity,
        presentment: Boolean,
    ): CdDemandNoteEntity {
        demandNote.amountPayable = BigDecimal.ZERO
        demandNote.totalAmount = BigDecimal.ZERO
        demandNote.cfvalue = BigDecimal.ZERO
        iDemandNoteItemRepo.findByDemandNoteId(demandNote.id!!).forEach { demandNoteItem ->
            demandNote.amountPayable = demandNote.amountPayable?.plus(
                demandNoteItem.amountPayable
                    ?: BigDecimal.ZERO
            )
            demandNote.totalAmount = demandNote.totalAmount?.plus(
                demandNoteItem.adjustedAmount
                    ?: BigDecimal.ZERO
            )
            demandNote.cfvalue = demandNote.cfvalue?.plus(
                demandNoteItem.cfvalue
                    ?: BigDecimal.ZERO
            )

        }

        demandNote.cfvalue = demandNote.cfvalue?.setScale(2, RoundingMode.HALF_UP)
        demandNote.totalAmount = roundUpNextInteger(demandNote.totalAmount?.setScale(2, RoundingMode.HALF_UP))
        demandNote.amountPayable = roundUpNextInteger(demandNote.amountPayable?.setScale(2, RoundingMode.HALF_UP))

        if (presentment) {
            return demandNote
        }
        return daoServices.upDateDemandNoteWithUser(demandNote, user)
    }

}
