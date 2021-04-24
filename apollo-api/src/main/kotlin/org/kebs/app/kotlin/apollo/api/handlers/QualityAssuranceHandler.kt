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

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.QADaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.QualityAssuranceDaoServices
import org.kebs.app.kotlin.apollo.common.exceptions.*
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.qa.PermitApplicationsEntity
import org.kebs.app.kotlin.apollo.store.model.qa.QaSta3Entity
import org.kebs.app.kotlin.apollo.store.repo.*
import org.kebs.app.kotlin.apollo.store.repo.qa.IPermitTypesEntityRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.ServerResponse.ok
import org.springframework.web.servlet.function.paramOrNull


@Component
class QualityAssuranceHandler(
    private val applicationMapProperties: ApplicationMapProperties,
    private val commonDaoServices: CommonDaoServices,
    private val standardCategoryRepo: IStandardCategoryRepository,
    private val productCategoriesRepository: IKebsProductCategoriesRepository,
    private val broadProductCategoryRepository: IBroadProductCategoryRepository,
    private val productsRepo: IProductsRepository,
    private val productSubCategoryRepo: IProductSubcategoryRepository,
    private val sampleStandardsRepository: ISampleStandardsRepository,
    private val qaDaoServices: QADaoServices

) {


    var employeeUserTypeId = 5L


    var manufacturerUserTypeId: Long = 4L

    private final val qaHomePage = "quality-assurance/home"
    private final val qaCustomerHomePage = "quality-assurance/customer/customer-home"
    private final val qaPermitListPage = "quality-assurance/permit-list"
    private final val qaPermitDetailPage = "quality-assurance/customer/permit-details"
    private final val qaNewPermitPage = "quality-assurance/customer/permit-application"
    private final val qaNewSta3Page = "quality-assurance/customer/permit-application"

    final val appId: Int = applicationMapProperties.mapQualityAssurance

    fun home(req: ServerRequest): ServerResponse {
        try {
            val auth = commonDaoServices.loggedInUserAuthentication()
            val map = commonDaoServices.serviceMapDetails(appId)
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            return when {
                auth.authorities.stream()
                    .anyMatch { authority -> authority.authority == applicationMapProperties.mapQualityAssuranceManufactureRoleName } -> {
                    req.attributes()["permitTypes"] = qaDaoServices.findPermitTypesList(map.activeStatus)
                    req.attributes()["map"] = map
                    ok().render(qaCustomerHomePage, req.attributes())
                }
                else -> {
                    ok().render(qaHomePage, req.attributes())
                }
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }
    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    fun permitList(req: ServerRequest): ServerResponse {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val permitTypeID = req.paramOrNull("permitTypeID")?.toLong()
            ?: throw ExpectedDataNotFound("Required PermitType ID, check config")
        val permitType = qaDaoServices.findPermitType(permitTypeID)

        req.attributes()["permitType"] = permitType
        req.attributes()["permitList"] = qaDaoServices.findAllUserPermitWithPermitType(loggedInUser, permitTypeID)
        return ok().render(qaPermitListPage, req.attributes())
    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    fun permitDetails(req: ServerRequest): ServerResponse {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val permitID =
            req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
        val permit = loggedInUser.id?.let { qaDaoServices.findPermitBYUserIDAndId(permitID, it) }?: throw ExpectedDataNotFound("User Id required")
        req.attributes()["standardCategory"] = standardCategoryRepo.findByIdOrNull(permit.standardCategory)
        req.attributes()["productCategory"] = productCategoriesRepository.findByIdOrNull(permit.productCategory)
        req.attributes()["broadProductCategory"] = broadProductCategoryRepository.findByIdOrNull(permit.broadProductCategory)
        req.attributes()["product"] = productsRepo.findByIdOrNull(permit.product)
        req.attributes()["productSubCategory"] = productSubCategoryRepo.findByIdOrNull(permit.productSubCategory)
        req.attributes()["ksApplicable"] = sampleStandardsRepository.findBySubCategoryId(permit.productSubCategory)
        req.attributes()["permitType"] = permit.permitType?.let { qaDaoServices.findPermitType(it) }
        req.attributes()["permitDetails"] = permit
        return ok().render(qaPermitDetailPage, req.attributes())


    }


    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    fun newPermit(req: ServerRequest): ServerResponse {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val permitTypeID = req.paramOrNull("permitTypeID")?.toLong()
            ?: throw ExpectedDataNotFound("Required PermitType ID, check config")
        val permitType = qaDaoServices.findPermitType(permitTypeID)

        req.attributes()["applicationDate"] = commonDaoServices.getCurrentDate()
        req.attributes()["permitType"] = permitType
        req.attributes()["permit"] = PermitApplicationsEntity()
        req.attributes()["standardCategory"] = standardCategoryRepo.findByStatusOrderByStandardCategory(map.activeStatus)
        return ok().render(qaNewPermitPage, req.attributes())

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    fun newSta3(req: ServerRequest): ServerResponse {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        val permitID = req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
        req.attributes()["permit"] =  loggedInUser.id?.let { qaDaoServices.findPermitBYUserIDAndId(permitID, it) }?: throw ExpectedDataNotFound("User Id required")
        req.attributes()["QaSta3Entity"] = QaSta3Entity()
        return ok().render(qaNewSta3Page, req.attributes())

    }


}