package org.kebs.app.kotlin.apollo.api.handlers.di

import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.payload.request.LaboratoryForm
import org.kebs.app.kotlin.apollo.api.service.DaoValidatorService
import org.kebs.app.kotlin.apollo.api.service.LaboratoryService
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

@Component
class LabManagerHandler(
        private val laboratoryService: LaboratoryService,
        private val validatorService: DaoValidatorService
) {

    fun listLaboratories(req: ServerRequest): ServerResponse {
        return ServerResponse.ok()
                .body(laboratoryService.listAllLaboratories())
    }

    fun addLaboratory(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            val form = req.body(LaboratoryForm::class.java)
            validatorService.validateInputWithInjectedValidator(form)?.let {
                response.errors = it
                response.message = "Invalid request"
                response.responseCode = ResponseCodes.INVALID_CODE
                response
            } ?: run {
                response = laboratoryService.addLaboratory(form)
                response
            }
        } catch (ex: Exception) {
            response.message = "Failed to process request"
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
        }

        return ServerResponse.ok().body(response)
    }

    fun updateLaboratory(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            val labId = req.pathVariable("labId").toLong()
            val form = req.body(LaboratoryForm::class.java)
            validatorService.validateInputWithInjectedValidator(form)?.let {
                response.errors = it
                response.message = "Invalid request"
                response.responseCode = ResponseCodes.INVALID_CODE
                response
            } ?: run {
                response = laboratoryService.updateLaboratory(form, labId)
                response
            }
        } catch (ex: Exception) {
            response.message = "Failed to process request"
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
        }

        return ServerResponse.ok().body(response)
    }

    fun deleteLaboratory(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            val labId = req.pathVariable("labId").toLong()
            response = laboratoryService.deleteLaboratory(labId)
        } catch (ex: Exception) {
            response.message = "Failed to delete record"
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
        }

        return ServerResponse.ok().body(response)
    }
}