package org.kebs.app.kotlin.apollo.api.handlers.pvoc

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.payload.extractPage
import org.kebs.app.kotlin.apollo.api.payload.request.ComplaintStatusForm
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.service.DaoValidatorService
import org.kebs.app.kotlin.apollo.api.service.PvocAgentService
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.ServiceRequestsEntity
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.pvc.PvocComplaintCategoryEntity
import org.kebs.app.kotlin.apollo.store.model.pvc.PvocComplaintCertificationSubCategoriesEntity
import org.kebs.app.kotlin.apollo.store.model.pvc.PvocComplaintEntity
import org.kebs.app.kotlin.apollo.store.model.pvc.PvocWaiversApplicationEntity
import org.kebs.app.kotlin.apollo.store.repo.IPvocComplaintCategoryRepo
import org.kebs.app.kotlin.apollo.store.repo.IPvocComplaintCertificationsSubCategoryRepo
import org.kebs.app.kotlin.apollo.store.repo.IPvocWaiversCategoriesRepo
import org.kebs.app.kotlin.apollo.store.repo.IUserProfilesRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.paramOrNull

@Component
class PvocComplaintHandler(
        private val commonDaoServices: CommonDaoServices,
        private val applicationMapProperties: ApplicationMapProperties,
        private val iPvocComplaintCertificationsSubCategoryRepo: IPvocComplaintCertificationsSubCategoryRepo,
        private val iPvocComplaintCategoryRepo: IPvocComplaintCategoryRepo,
        private val iUserProfilesRepository: IUserProfilesRepository,
        private val iPvocWaiversCategoriesRepo: IPvocWaiversCategoriesRepo,
        private val agentService: PvocAgentService,
        private val validator: DaoValidatorService

) {
    final val appId = applicationMapProperties.mapImportInspection
    fun complaintForm(req: ServerRequest): ServerResponse =
            req.paramOrNull("token").let { token ->
                val map = commonDaoServices.serviceMapDetails(appId)
                val sr: ServiceRequestsEntity
                val payload = "VerifyToken [token= ${token}]"
                sr = commonDaoServices.mapServiceRequestForSuccess(map, payload, null)
                commonDaoServices.validateVerificationToken(sr, token)
                        .let {
                            commonDaoServices.findDivisionWIthId(3).let { division ->
                                division.let { it1 ->
                                    iUserProfilesRepository.findAllByDivisionIdAndStatus(it1, 1)
                                            ?.let { profiles ->
                                                req.attributes()["profiles"] = profiles
                                                req.attributes()["complaint"] = PvocComplaintEntity()
                                                req.attributes()["user"] = UsersEntity()
                                                req.attributes()["complaintsCategories"] =
                                                        iPvocComplaintCategoryRepo.findAllByStatus(1)
                                                req.attributes()["category"] = PvocComplaintCategoryEntity()
                                                req.attributes()["subCategory"] = PvocComplaintCertificationSubCategoriesEntity()
                                                iPvocComplaintCategoryRepo.findByIdOrNull(3).let { category ->
                                                    req.attributes()["complaintsCategoriesSubs"] = category?.let { it2 ->
                                                        iPvocComplaintCertificationsSubCategoryRepo.findAllByStatusAndPvocComplaintCategoryByComplainCategoryId(
                                                                1,
                                                                it2
                                                        )
                                                    }
                                                }
                                            }
                                } ?: throw Exception("Not found")
                            }

                        }
                ServerResponse.ok().render("destination-inspection/pvoc/complaint/ComplaintsForm", req.attributes())
            }

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
                response = this.agentService.approveCurrentComplaint(complaintId, form.action!!, form.taskId!!, form.remarks!!)
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
        val response = this.agentService.listComplaints(complaintStatus.toUpperCase(), keywords.orElse(null), extractPage(req))
        return ServerResponse.ok().body(response)
    }

    fun complaintApplicationDetails(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            val complaintId = req.pathVariable("complaintId").toLong()
            response = this.agentService.complaintDetails(complaintId)
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to fetch application", ex)
            response.message = "Request failed"
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
        }
        return ServerResponse.ok().body(response)
    }


}





