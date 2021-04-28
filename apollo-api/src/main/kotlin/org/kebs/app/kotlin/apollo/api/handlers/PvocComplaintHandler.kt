package org.kebs.app.kotlin.apollo.api.handlers

import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.repo.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.paramOrNull

@Component
class PvocComplaintHandler(
    private val commonDaoServices: CommonDaoServices,
    applicationMapProperties: ApplicationMapProperties,
    private val iPvocComplaintCertificationsSubCategoryRepo: IPvocComplaintCertificationsSubCategoryRepo,
    private val iPvocComplaintCategoryRepo: IPvocComplaintCategoryRepo,
    private val iUserProfilesRepository: IUserProfilesRepository,
    private val iPvocWaiversCategoriesRepo: IPvocWaiversCategoriesRepo

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


}





