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
            val consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
            val demandNote = iDemandNoteRepo.findById(demandNoteId)
            if (demandNote.isPresent) {
                val demand = demandNote.get()
                val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
                demand.status = map.invalidStatus
                demand.varField3 = "REJECTED"
                demand.varField10 = remarks
                consignmentDocument.varField10="Demand note rejected"
                consignmentDocument.sendDemandNote=0
                this.daoServices.updateCdDetailsInDB(consignmentDocument, null)
                this.iDemandNoteRepo.save(demand)
                this.auditService.addHistoryRecord(consignmentDocument.id!!,consignmentDocument.ucrNumber, remarks, "REJECT DEMAND NOTE", "Demand note ${demandNoteId} rejected")
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
                consignmentDocument.varField10="Demand Approved"
                consignmentDocument.sendDemandNote=map.activeStatus
                this.daoServices.updateCdDetailsInDB(consignmentDocument, null)
                // Update demand note
                this.iDemandNoteRepo.save(demand)
                this.auditService.addHistoryRecord(consignmentDocument.id!!,consignmentDocument.ucrNumber, remarks, "APPROVED DEMAND NOTE", "Demand note ${demandNoteId} approved")
            }

        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("DEMAND NOTE ERROR", ex)
        }
        return true
    }

    fun sendDemandNote(cdUuid: String, demandNoteId: Long): Boolean {
        // Send demand note to user
        val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
        val demandNote = iDemandNoteRepo.findById(demandNoteId)
        try {

            this.daoServices.sendDemandNotGeneratedToKWIS(demandNoteId)
            // Update submission status
            if (demandNote.isPresent) {
                val consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
                val demand = demandNote.get()
                demand.varField3 = "SUBMITTED TO KENTRADE"
                // Update CD status
                consignmentDocument.varField10="Demand submitted to KenTrade"
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

    fun invoicePaymentCompleted(cdUuid: String, demandNoteId: Long, receiptNo: String): Boolean {
        try {
            val consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
            val demandNote = this.iDemandNoteRepo.findById(demandNoteId).get()
            demandNote.demandNoteNumber?.let {
                invoiceDaoService.createBatchInvoiceDetails("system", it)
                        .let { batchInvoiceDetail ->
                            val importerDetails = consignmentDocument.cdImporter?.let {
                                daoServices.findCDImporterDetails(it)
                            }
                            val myAccountDetails =
                                    InvoiceDaoService.InvoiceAccountDetails()
                            with(myAccountDetails) {
                                accountName = importerDetails?.name
                                accountNumber = importerDetails?.pin
                                currency =
                                        applicationMapProperties.mapInvoiceTransactionsLocalCurrencyPrefix
                            }
                            invoiceDaoService.createPaymentDetailsOnStgReconciliationTable(
                                    "system",
                                    batchInvoiceDetail,
                                    myAccountDetails
                            )
                            // Send demand note to SW
                            demandNote.id?.let { it1 ->
                                daoServices.sendDemandNotGeneratedToKWIS(it1)
                                consignmentDocument.cdStandard?.let { cdStd ->
                                    daoServices.updateCDStatus(
                                            cdStd,
                                            daoServices.awaitPaymentStatus.toLong()
                                    )

                                }
                            }
                        }
            }
            return true
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("INVOICE UPDATE FAILED", ex)
        }
        return false
    }
}