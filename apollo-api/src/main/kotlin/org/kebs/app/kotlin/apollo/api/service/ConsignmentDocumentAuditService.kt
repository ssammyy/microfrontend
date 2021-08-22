package org.kebs.app.kotlin.apollo.api.service;

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.CdDocumentModificationHistoryDao
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DestinationInspectionDaoServices
import org.kebs.app.kotlin.apollo.store.model.di.CdDocumentModificationHistory
import org.kebs.app.kotlin.apollo.store.repo.di.ICdDocumentModificationHistoryRepository;
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service;

@Service
class ConsignmentDocumentAuditService(
        private val cdHistory: ICdDocumentModificationHistoryRepository,
        private val daoServices: DestinationInspectionDaoServices,
) {
    fun addHistoryRecord(cdId: Long?, comment: String?, action: String, narration: String) {
        val history = CdDocumentModificationHistory()
        SecurityContextHolder.getContext().authentication.principal.let {
            history.createdBy = it.toString()
            history.actionCode = action
            history.cdId = cdId
            history.comment = comment
            history.description = narration
            this.cdHistory.save(history)
        }
    }

    fun listDocumentHistory(cdId: Long): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val history = cdHistory.findAllByCdId(cdId)
            response.data = CdDocumentModificationHistoryDao.fromList(history)
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Success"
        } catch (ex: Exception) {
            KotlinLogging.logger(javaClass.name).error { ex }
            response.responseCode = ResponseCodes.NOT_FOUND
            response.message = "Consignment document not found"
        }
        return response
    }
}
