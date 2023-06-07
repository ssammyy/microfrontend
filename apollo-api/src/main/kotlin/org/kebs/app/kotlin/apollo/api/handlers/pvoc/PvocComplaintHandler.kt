package org.kebs.app.kotlin.apollo.api.handlers.pvoc

import mu.KotlinLogging
import org.codehaus.jackson.map.ObjectMapper
import org.flowable.common.engine.api.FlowableObjectNotFoundException
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.payload.extractPage
import org.kebs.app.kotlin.apollo.api.payload.request.ComplaintStatusForm
import org.kebs.app.kotlin.apollo.api.payload.request.PvocComplaintForm
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.service.DaoValidatorService
import org.kebs.app.kotlin.apollo.api.service.PvocService
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.ServiceRequestsEntity
import org.kebs.app.kotlin.apollo.store.model.pvc.PvocWaiversApplicationEntity
import org.kebs.app.kotlin.apollo.store.repo.IPvocWaiversCategoriesRepo
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.paramOrNull

@Component
class PvocComplaintHandler(
        private val commonDaoServices: CommonDaoServices,
        private val applicationMapProperties: ApplicationMapProperties,
        private val mapper: ObjectMapper,
        private val iPvocWaiversCategoriesRepo: IPvocWaiversCategoriesRepo,
        private val pvocService: PvocService,
        private val validator: DaoValidatorService

) {
    final val appId = applicationMapProperties.mapImportInspection
    fun waiversForm(req: ServerRequest): ServerResponse =
            req.paramOrNull("token").let { token ->
                val map = commonDaoServices.serviceMapDetails(appId)
                val sr: ServiceRequestsEntity
                val payload = "VerifyToken [token= ${token}]"
                sr = commonDaoServices.mapServiceRequestForSuccess(map, payload, null)
                commonDaoServices.validateVerificationToken(sr, token)
                        .let {
                            commonDaoServices.getLoggedInUser().let { user ->
                                iPvocWaiversCategoriesRepo.findAllByStatus(1).let { categories ->
                                    req.attributes()["applicant"] = user
                                    req.attributes()["categories"] = categories
                                    req.attributes()["waiver"] = PvocWaiversApplicationEntity()
                                }
                            }
                            ServerResponse.ok().render("destination-inspection/pvoc/WaiversApplication", req.attributes())
                        }
            }

    fun companyComplaintHistory(req: ServerRequest): ServerResponse {
        val status = req.param("status").orElse("new")
        val response = this.pvocService.companyComplaintHistory(status, extractPage(req))
        return ServerResponse.ok().body(response)
    }

    fun loadComplaintCategories(req: ServerRequest): ServerResponse {
        val response = this.pvocService.getComplaintCategories()
        return ServerResponse.ok().body(response)
    }

    fun loadComplaintRecommendations(req: ServerRequest): ServerResponse {
        val response = this.pvocService.getRecommendations()
        return ServerResponse.ok().body(response)
    }

    fun fileComplaintRequest(req: ServerRequest): ServerResponse {
        var response: ApiResponseModel
        try {
            val multipartRequest = (req.servletRequest() as? MultipartHttpServletRequest)
                    ?: throw ResponseStatusException(
                            HttpStatus.NOT_ACCEPTABLE,
                            "Request is not a multipart request"
                    )

            val data = multipartRequest.getParameter("data")
            val complaint: PvocComplaintForm = this.mapper.readValue(data, PvocComplaintForm::class.java)
            // Validate and save data
            val errors = this.validator.validateInputWithInjectedValidator(complaint)
            if (errors == null) {
                val documents: List<MultipartFile> = multipartRequest.getFiles("files")
                response = this.pvocService.pvocFileComplaint(complaint, documents)
            } else {
                response = ApiResponseModel()
                response.responseCode = ResponseCodes.INVALID_CODE
                response.data = complaint
                response.errors = errors
                response.message = "Request data is invalid"
            }
        } catch (ex: FlowableObjectNotFoundException) {
            response = ApiResponseModel()
            response.message = "Complaint process definition not found"
            response.errors = ex.localizedMessage
            response.responseCode = ResponseCodes.FAILED_CODE
        } catch (ex: ResponseStatusException) {
            response = ApiResponseModel()
            response.message = "Invalid request data: " + ex.reason
            response.responseCode = ResponseCodes.FAILED_CODE
        } catch (ex: Exception) {
            KotlinLogging.logger {}.error("Failed to process request", ex)
            response = ApiResponseModel()
            response.message = "Failed to process request"
            response.responseCode = ResponseCodes.FAILED_CODE
        }
        return ServerResponse.ok().body(response)
    }

    fun approveCurrentComplaintTask(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            val complaintId = req.pathVariable("complaintId").toLong()
            val form = req.body(ComplaintStatusForm::class.java)
            this.validator.validateInputWithInjectedValidator(form)?.let {
                response.errors = it
                response.responseCode = ResponseCodes.INVALID_CODE
                response.message = "Please correct errors"
                response
            } ?: run {
                response = this.pvocService.approveCurrentComplaint(complaintId, form.action
                        ?: 0, form.status!!, form.taskId!!, form.remarks!!)
                response
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to approve complaint task", ex)
            response.message = "Request failed"
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
        }
        return ServerResponse.ok().body(response)
    }

    fun complaintApplications(req: ServerRequest): ServerResponse {
        val complaintStatus = req.pathVariable("applicationStatus")
        val keywords = req.param("keywords")
        val response = this.pvocService.listComplaints(complaintStatus.toUpperCase(), keywords.orElse(null), extractPage(req))
        return ServerResponse.ok().body(response)
    }

    fun complaintApplicationDetails(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            val complaintId = req.pathVariable("complaintId").toLong()
            response = this.pvocService.complaintDetails(complaintId)
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to fetch application", ex)
            response.message = "Request failed"
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
        }
        return ServerResponse.ok().body(response)
    }


}





