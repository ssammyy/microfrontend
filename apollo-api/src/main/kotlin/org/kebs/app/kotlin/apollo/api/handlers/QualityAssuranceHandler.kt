/*
 *
 *  *
 *  *
 *  *  *    Copyright (c) ${YEAR}.   BSK Global Technologies
 *  *  *
 *  *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *    you may not use this file except in compliance with the License.
 *  *  *    You may obtain a copy of the License at
 *  *  *
 *  *  *       http://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  *    Unless required by applicable law or agreed to in writing, software
 *  *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *   See the License for the specific language governing permissions and
 *  *  *   limitations under the License.
 *  *
 *
 */

package org.kebs.app.kotlin.apollo.api.handlers

import org.kebs.app.kotlin.apollo.api.ports.provided.dao.QualityAssuranceDaoServices
import org.kebs.app.kotlin.apollo.common.exceptions.*
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.qa.PermitApplicationsEntity
import org.kebs.app.kotlin.apollo.store.repo.qa.IPermitTypesEntityRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.ServerResponse.ok
import org.springframework.web.servlet.function.paramOrNull


@Component
class QualityAssuranceHandler(
        private val applicationMapProperties: ApplicationMapProperties,
        private val daoServices: QualityAssuranceDaoServices,
        private val permitTypesRepo: IPermitTypesEntityRepository

) {


     var employeeUserTypeId=5L


     var manufacturerUserTypeId: Long = 4L

    private final val qaHomePage = "quality-assurance/home"
    private final val qaCustomerHomePage = "quality-assurance/customer/customer-home"
    private final val qaPermitListPage = "quality-assurance/permit-list"
    private final val qaPermitDetailPage = "quality-assurance/permit-list"
    private final val qaNewPermitPage = "quality-assurance/customer/permit-application"
//    final val appId: Int = applicationMapProperties.mapQualityAssurance

    //    fun home(req: ServerRequest): ServerResponse = ok().render(qaHomePage)
    fun home(req: ServerRequest): ServerResponse =
            daoServices.loggedInUserDetails()
                    .let { usersEntity ->
                        daoServices.serviceMapDetails()
                                .let { map ->
                                    return if (usersEntity.userTypes == applicationMapProperties.mapUserTypeManufacture) {
                                        req.attributes()["permitTypes"] = permitTypesRepo.findByStatus(map.activeStatus)
                                        req.attributes()["map"] = map
                                        ok().render(qaCustomerHomePage, req.attributes())
                                    } else {
                                        ok().render(qaHomePage, req.attributes())
                                    }
                                }
                    }

    fun permitList(req: ServerRequest): ServerResponse =
            daoServices.loggedInUserDetails()
                    .let { usersEntity ->
                        daoServices.serviceMapDetails()
                                .let {
                                    req.paramOrNull("permitTypeID")
                                            ?.let { permitTypeID ->
                                                val permitType = permitTypesRepo.findById(permitTypeID.toLong())
                                                req.attributes()["permitType"] = permitType
                                                permitType.get().id?.let{
                                                    req.attributes()["permitList"] = daoServices.findAllByManufactureAndType(usersEntity, it)
                                                }

                                                return ok().render(qaPermitListPage, req.attributes())
                                            }
                                            ?: throw ExpectedDataNotFound("Required PermitType ID, check config")
                                }
                    }

    fun newPermit(req: ServerRequest): ServerResponse =
            daoServices.loggedInUserDetails()
                    .let { usersEntity ->
                        daoServices.serviceMapDetails()
                                .let {
                                    req.paramOrNull("permitTypeID")
                                            ?.let { permitTypeID ->
                                                req.attributes()["permitType"] = permitTypesRepo.findById(permitTypeID.toLong())
                                                req.attributes()["permit"] = PermitApplicationsEntity()
                                                return ok().render(qaNewPermitPage, req.attributes())
                                            }
                                            ?: throw ExpectedDataNotFound("Required PermitType ID, check config")
                                }
                    }

    fun permitDetails(req: ServerRequest): ServerResponse =
            daoServices.loggedInUserDetails()
                    .let {
                        daoServices.serviceMapDetails()
                                .let { map ->
                                    req.paramOrNull("permitID")
                                            ?.let { permitID ->
//                                                val permitType = daoServices.findPermitTypeByPermitType(permitTypeID.toLong())
//                                                req.attributes()["permitType"] = permitType
                                                req.attributes()["permitDetails"] = daoServices.findByPermitId(permitID.toLong())
                                                return ok().render(qaPermitDetailPage, req.attributes())
                                            }
                                            ?: throw ExpectedDataNotFound("Required Permit ID, check config")
                                }
                    }

}