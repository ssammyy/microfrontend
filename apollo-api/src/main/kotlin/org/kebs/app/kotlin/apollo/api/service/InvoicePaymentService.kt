package org.kebs.app.kotlin.apollo.api.service

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.*
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.di.CurrencyExchangeRates
import org.kebs.app.kotlin.apollo.store.repo.di.ICfgCurrencyExchangeRateRepository
import org.kebs.app.kotlin.apollo.store.repo.di.IDemandNoteRepository
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.io.InputStreamReader
import java.io.Reader
import java.sql.Timestamp
import java.time.Instant

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
        private val applicationMapProperties: ApplicationMapProperties
) {
    fun invoiceDetails(demandNoteId: Long): HashMap<String,Any>{
        var map = hashMapOf<String, Any>()

        daoServices.findDemandNoteWithID(demandNoteId)?.let {demandNote->
            map["preparedBy"] = demandNote.generatedBy.toString()
            map["datePrepared"] = demandNote.dateGenerated.toString()
            map["demandNoteNo"] = demandNote.demandNoteNumber.toString()
            map["importerName"] = demandNote.nameImporter.toString()
            map["importerAddress"] = demandNote.address.toString()
            map["importerTelephone"] = demandNote.telephone.toString()
            map["ablNo"] = demandNote.entryAblNumber.toString()
            map["totalAmount"] = demandNote.totalAmount.toString()
            map["receiptNo"] = demandNote.receiptNo.toString()

            map = reportsDaoService.addBankAndMPESADetails(map, demandNote.demandNoteNumber?:"")
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
                            daoServices.awaitPaymentStatus.toLong()
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
        KotlinLogging.logger {  }.info("INVOICE GENERATION: $demandNoteId")
        try {
            val consignmentDocument = daoServices.findCDWithUuid(cdUuid)
            //Send Demand Note
            val demandNote = daoServices.findDemandNoteWithID(demandNoteId)
            demandNote?.demandNoteNumber?.let {
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
                KotlinLogging.logger {  }.info("ADD STAGING TO TABLE: $demandNoteId")
                // Create payment on staging table
                invoiceDaoService.createPaymentDetailsOnStgReconciliationTable(
                        loggedInUser.userName!!,
                        updateBatchInvoiceDetail,
                        myAccountDetails
                )

            }?:ExpectedDataNotFound("Demand note number not set")
            return true
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to generate batch number", ex)
        }
        return false
    }

    fun updateDemandNoteSw(cdUuid: String, demandNoteId: Long): Boolean {
        KotlinLogging.logger {  }.info("UPDATE DEMAND NOTE ON SW")
        try {
            val consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
            //2. Update payment status on KenTrade
            daoServices.sendDemandNotGeneratedToKWIS(demandNoteId)
            consignmentDocument.varField10 = "DEMAND NOTE SENT TO KENTRADE, AWAITING PAYMENT"
            this.daoServices.updateCdDetailsInDB(consignmentDocument, null)
            return true
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to update status", ex)
            throw ex
        }
    }

    fun sendDemandNoteStatus(demandNoteId: Long): Boolean {
        daoServices.findDemandNoteWithID(demandNoteId)?.let {demandNote->
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            //1. Update Demand Note Status
            demandNote.swStatus=map.activeStatus
            demandNote.varField10="PAYMENT COMPLETED"
            val demandNoteDetails = daoServices.upDateDemandNote(demandNote)
            val consignmentDocument = demandNoteDetails.cdId?.let {cdId-> daoServices.findCD(cdId) }
            // 2. Update CD status
            consignmentDocument?.cdStandard?.let { cdStd ->
                daoServices.updateCDStatus(
                        cdStd,
                        daoServices.paymentMadeStatus.toLong()
                )
            }
        }
        return true
    }

    fun invoicePaymentCompleted(cdId: Long, demandNoteId: Long): Boolean {
        try {
            val consignmentDocument = this.daoServices.findCD(cdId)
            // 1. Send demand payment status to SW
            daoServices.sendDemandNotePayedStatusToKWIS(demandNoteId)
            // 2. Update application status
            consignmentDocument.varField10 = "DEMAND NOTE PAID,AWAITING INSPECTION"
            this.daoServices.updateCdDetailsInDB(consignmentDocument, null)
            return true
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("INVOICE UPDATE FAILED", ex)
        }
        return false
    }

    fun uploadExchangeRates(multipartFile: MultipartFile, fileType:String) {
        var separator = ','
        if ("TSV".equals(fileType)) {
            KotlinLogging.logger { }.info("TAB SEPARATED DATA")
            separator = '\t'
        } else {
            KotlinLogging.logger { }.info("COMMA SEPARATED DATA")
        }
        val loggedInUser=commonDaoServices.getLoggedInUser()
        val targetReader: Reader = InputStreamReader(ByteArrayInputStream(multipartFile.bytes))
        val exchangeRates=service.readExchangeRatesFromController(separator,targetReader)
        for(rate in exchangeRates) {
            val exchangeRateEntity= CurrencyExchangeRates()
            with(exchangeRateEntity){
                applicableDate= Timestamp.from(Instant.now())
                currencyCode=rate.currencyCode
                exchangeRate=rate.exchangeRate
                description=rate.description
                status=1
                createdBy=loggedInUser?.userName
                createdOn= Timestamp.from(Instant.now())
                modifiedOn= Timestamp.from(Instant.now())
                modifiedBy=loggedInUser?.userName
            }
            this.exchangeRateRepository.save(exchangeRateEntity)
        }
    }

}