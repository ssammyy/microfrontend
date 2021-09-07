package org.kebs.app.kotlin.apollo.api.service

import mu.KotlinLogging
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
        private val applicationMapProperties: ApplicationMapProperties
) {

    fun rejectDemandNoteGeneration(cdUuid: String,demandNoteId: Long,remarks: String): Boolean {
        try {
            val consignmentDocument=this.daoServices.findCDWithUuid(cdUuid)
            val demandNote = iDemandNoteRepo.findById(demandNoteId)
            if(demandNote.isPresent){
                val demand=demandNote.get()
                demand.status=2
                this.iDemandNoteRepo.save(demand)
                this.auditService.addHistoryRecord(consignmentDocument.id!!,remarks,"REJECT DEMAND NOTE","Demand note ${demandNoteId} rejected")
            }
        }catch (ex: Exception){
            KotlinLogging.logger {  }.error("DEMAND NOTE ERROR", ex)
        }
        return true
    }

    fun approveDemandNoteGeneration(cdUuid: String,demandNoteId: Long,supervisor: String,remarks: String): Boolean {
        try {
            val consignmentDocument=this.daoServices.findCDWithUuid(cdUuid)
            val demandNote = iDemandNoteRepo.findById(demandNoteId)
            if(demandNote.isPresent){
                val demand=demandNote.get()
                demand.status=1
                this.iDemandNoteRepo.save(demand)
                this.auditService.addHistoryRecord(consignmentDocument.id!!,remarks,"APPROVED DEMAND NOTE","Demand note ${demandNoteId} approved")
            }

        }catch (ex: Exception){
            KotlinLogging.logger {  }.error("DEMAND NOTE ERROR", ex)
        }
        return true
    }

    fun sendDemandNote(cdUuid: String,demandNoteId: Long): Boolean{
        // Send demand note to user
        this.daoServices.sendDemandNotGeneratedToKWIS(demandNoteId)
        return true
    }

    fun invoicePaymentCompleted(cdUuid: String, demandNoteId: Long,receiptNo: String): Boolean{
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
        }catch (ex: Exception){
            KotlinLogging.logger {  }.error("INVOICE UPDATE FAILED",ex)
        }
        return false
    }
}