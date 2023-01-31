package org.kebs.app.kotlin.apollo.api.service;

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.CdDocumentModificationHistoryDao
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DestinationInspectionDaoServices
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.di.CdDocumentModificationHistory
import org.kebs.app.kotlin.apollo.store.repo.di.ICdDocumentModificationHistoryRepository
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.LocalDateTime

@Service
class ConsignmentDocumentAuditService(
        private val cdHistory: ICdDocumentModificationHistoryRepository,
        private val commonDaoServices: CommonDaoServices,
        private val daoServices: DestinationInspectionDaoServices,
) {
    fun addHistoryRecord(cdId: Long?,ucrNumber: String?, comment: String?, action: String, narration: String, username: String? = null) {
        try {
            val history = CdDocumentModificationHistory()
            val user: UsersEntity? = username?.let {
                commonDaoServices.findUserByUserName(username)
            } ?: commonDaoServices.getLoggedInUser()
            user?.let {
                history.name = "${it.firstName} ${it.lastName}"
                history.createdBy = it.userName
                history.modifiedBy = it.userName
                history.createdOn = Timestamp.valueOf(LocalDateTime.now())
                history.modifiedOn = Timestamp.valueOf(LocalDateTime.now())
                history.actionCode = action
                history.cdId = cdId
                history.ucrNumber = ucrNumber
                history.status = 1
                history.comment = comment
                history.description = narration
                // SAVE RECORD
                KotlinLogging.logger { }.info { history }
                this.cdHistory.save(history)
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("FAILED TO ADD", ex)
        }
    }

    fun listDocumentHistory(cdId: Long): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val consignmentDocument=daoServices.findCD(cdId)
            // Find by ucrNumber
            consignmentDocument.ucrNumber?.let {
                val history = cdHistory.findAllByUcrNumberOrCdIdOrderByIdDesc(it, cdId)
                response.data = CdDocumentModificationHistoryDao.fromList(history)
            }
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
