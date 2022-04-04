package org.kebs.app.kotlin.apollo.api.service

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.payload.request.LaboratoryForm
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.store.model.di.CdLaboratoryEntity
import org.kebs.app.kotlin.apollo.store.repo.di.ILaboratoryRepository
import org.springframework.stereotype.Service

@Service
class LaboratoryService(
        private val commonDaoServices: CommonDaoServices,
        private val labRepository: ILaboratoryRepository
) {

    fun addLaboratory(form: LaboratoryForm): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val lab = CdLaboratoryEntity()
            lab.labName = form.laboratoryName
            lab.description = form.laboratoryDesc
            val auth = commonDaoServices.loggedInUserAuthentication()
            lab.modifiedBy = auth.name
            lab.createdBy = auth.name
            lab.status = 1
            lab.createdOn = commonDaoServices.getTimestamp()
            lab.modifiedOn = commonDaoServices.getTimestamp()
            this.labRepository.save(lab)
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Success"
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to add laboratory", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed to add laboratory, please try again later"
        }
        return response
    }

    fun updateLaboratory(form: LaboratoryForm, labId: Long): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val labOptional = this.labRepository.findById(labId)
            if (labOptional.isPresent) {
                val lab = labOptional.get()
                lab.labName = form.laboratoryName
                lab.status = 1
                lab.description = form.laboratoryDesc
                val auth = commonDaoServices.loggedInUserAuthentication()
                lab.modifiedBy = auth.name
                lab.modifiedOn = commonDaoServices.getTimestamp()
                this.labRepository.save(lab)
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "Success"
            } else {
                response.responseCode = ResponseCodes.NOT_FOUND
                response.message = "Record with id not found"
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to add laboratory", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed to update laboratory, please try again later"
        }
        return response
    }

    fun deleteLaboratory(labId: Long): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val labOptional = this.labRepository.findById(labId)
            if (labOptional.isPresent) {
                val lab = labOptional.get()
                lab.status = 2
                val auth = commonDaoServices.loggedInUserAuthentication()
                lab.deleteBy = auth.name
                lab.deletedOn = commonDaoServices.getTimestamp()
                this.labRepository.save(lab)
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "Record deleted"
            } else {
                response.responseCode = ResponseCodes.NOT_FOUND
                response.message = "Record with id not found"
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to add laboratory", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed to update laboratory, please try again later"
        }
        return response
    }

    fun listAllLaboratories(): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            response.data = this.labRepository.findByStatus(1)
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Record deleted"
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to add laboratory", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed to update laboratory, please try again later"
        }
        return response
    }
}