package org.kebs.app.kotlin.apollo.api.service

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DestinationInspectionDaoServices
import org.kebs.app.kotlin.apollo.store.repo.di.IDemandNoteRepository
import org.springframework.stereotype.Service

@Service("invoiceService")
class InvoicePaymentService(
        private val iDemandNoteRepo: IDemandNoteRepository,
        private val auditService: ConsignmentDocumentAuditService,
        private val daoServices: DestinationInspectionDaoServices
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

    fun approveDemandNoteGeneration(cdUuid: String,demandNoteId: Long,remarks: String): Boolean {
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
        return true
    }
}