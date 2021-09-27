package org.kebs.app.kotlin.apollo.api.service

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DestinationInspectionDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.InvoiceDaoService
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.repo.di.IDemandNoteRepository
import org.springframework.stereotype.Service

@Service("invoiceService")
class InvoicePaymentService(
        private val iDemandNoteRepo: IDemandNoteRepository,
        private val auditService: ConsignmentDocumentAuditService,
        private val daoServices: DestinationInspectionDaoServices,
        private val invoiceDaoService: InvoiceDaoService,
        private val commonDaoServices: CommonDaoServices,
        private val applicationMapProperties: ApplicationMapProperties
) {

    fun rejectDemandNoteGeneration(cdUuid: String, demandNoteId: Long, remarks: String): Boolean {
        try {
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
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
                consignmentDocument.varField10 = "Demand Approved"
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
                consignmentDocument.varField10 = "UPDATED DEMAND NOTE STATUS"
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
                // Create payment on staging table
                invoiceDaoService.createPaymentDetailsOnStgReconciliationTable(
                        loggedInUser.userName!!,
                        updateBatchInvoiceDetail,
                        myAccountDetails
                )

            }
            return true
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to generate batch number", ex)
        }
        return false
    }

    fun updateDemandNoteSw(cdUuid: String, demandNoteId: Long): Boolean {
        try {
            val consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
            //2. Update payment status on KenTrade
            daoServices.sendDemandNotGeneratedToKWIS(demandNoteId)
            consignmentDocument.varField10 = "DEMAND NOTE SENT TO KENTRADE"
            this.daoServices.updateCdDetailsInDB(consignmentDocument, null)
            return true
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to update status", ex)
        }
        return false
    }

    fun invoicePaymentCompleted(cdUuid: String, demandNoteId: Long): Boolean {
        try {
            val consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
            // 1. Send demand payment status to SW
            daoServices.sendDemandNotePayedStatusToKWIS(demandNoteId)
            // 2. Update CD status
            consignmentDocument.cdStandard?.let { cdStd ->
                daoServices.updateCDStatus(
                        cdStd,
                        daoServices.paymentMadeStatus.toLong()
                )
            }
            // Update application status
            consignmentDocument.varField10 = "INVOICE PAYMENT UPDATE ON SW"
            this.daoServices.updateCdDetailsInDB(consignmentDocument, null)
            return true
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("INVOICE UPDATE FAILED", ex)
        }
        return false
    }
}