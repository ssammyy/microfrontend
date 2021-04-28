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
import org.kebs.app.kotlin.apollo.common.exceptions.*
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.qa.*
import org.kebs.app.kotlin.apollo.store.repo.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
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
    private val paymentMethodsRepository: IPaymentMethodsRepository,
    private val countyRepo: ICountiesRepository,
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
    private final val qaNewSta3Page = "quality-assurance/customer/sta3-new-details"
    private final val qaNewSta10Page = "quality-assurance/customer/sta10-new-application"
    private final val qaNewSta10DetailsPage = "quality-assurance/customer/sta10-new-details"
    private final val qaInvoiceGenerated = "quality-assurance/customer/generated-invoice"

    final val appId: Int = applicationMapProperties.mapQualityAssurance

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    fun home(req: ServerRequest): ServerResponse {
        try {
            val auth = commonDaoServices.loggedInUserAuthentication()
            val map = commonDaoServices.serviceMapDetails(appId)
            return when {
                auth.authorities.stream().anyMatch { authority -> authority.authority == applicationMapProperties.mapQualityAssuranceManufactureRoleName } -> {
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
        val permitUserId = req.paramOrNull("userID")?.toLong() ?: throw ExpectedDataNotFound("Required User ID, check config")
        val permitID = req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
       val map = commonDaoServices.serviceMapDetails(appId)
        val permit = qaDaoServices.findPermitBYUserIDAndId(permitID, permitUserId)
        val plantAttached = permit.attachedPlantId?.let { qaDaoServices.findPlantDetails(it) }
        val docFileName : String? = null
        req.attributes()["fileParameters"] = qaDaoServices.findAllUploadedFileBYPermitID(permitID)
        req.attributes()["standardCategory"] = standardCategoryRepo.findByIdOrNull(permit.standardCategory)
        req.attributes()["productCategory"] = productCategoriesRepository.findByIdOrNull(permit.productCategory)
        req.attributes()["broadProductCategory"] = broadProductCategoryRepository.findByIdOrNull(permit.broadProductCategory)
        req.attributes()["product"] = productsRepo.findByIdOrNull(permit.product)
        req.attributes()["productSubCategory"] = productSubCategoryRepo.findByIdOrNull(permit.productSubCategory)
        req.attributes()["ksApplicable"] = sampleStandardsRepository.findBySubCategoryId(permit.productSubCategory)
        req.attributes()["sectionValue"] = permit.sectionId?.let { commonDaoServices.findSectionWIthId(it).section }
        req.attributes()["divisionValue"] = permit.divisionId?.let { commonDaoServices.findDivisionWIthId(it).division }
        req.attributes()["regionPlantValue"] = plantAttached?.region?.let { commonDaoServices.findRegionEntityByRegionID(it, map.activeStatus).region }
        req.attributes()["countyPlantValue"] = plantAttached?.county?.let { commonDaoServices.findCountiesEntityByCountyId(it, map.activeStatus).county }
        req.attributes()["townPlantValue"] = plantAttached?.town?.let { commonDaoServices.findTownEntityByTownId(it).town }
        req.attributes()["permitType"] = permit.permitType?.let { qaDaoServices.findPermitType(it) }
        req.attributes()["permitDetails"] = permit
        req.attributes()["docFileName"] =  docFileName
        req.attributes()["plantAttached"] = plantAttached
        req.attributes()["plantsDetails"] = qaDaoServices.findAllPlantDetails(permitUserId)
        return ok().render(qaPermitDetailPage, req.attributes())
    }


    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    fun newPermit(req: ServerRequest): ServerResponse {
        val map = commonDaoServices.serviceMapDetails(appId)
        val permitTypeID = req.paramOrNull("permitTypeID")?.toLong()
            ?: throw ExpectedDataNotFound("Required PermitType ID, check config")
        val permitType = qaDaoServices.findPermitType(permitTypeID)
        val departmentEntity = commonDaoServices.findDepartmentByID(applicationMapProperties.mapQADepertmentId)

        req.attributes()["applicationDate"] = commonDaoServices.getCurrentDate()
        req.attributes()["permitType"] = permitType
        req.attributes()["permit"] = PermitApplicationsEntity()
        req.attributes()["divisions"] = commonDaoServices.findDivisionByDepartmentId(departmentEntity, map.activeStatus)
        req.attributes()["standardCategory"] = standardCategoryRepo.findByStatusOrderByStandardCategory(map.activeStatus)
        return ok().render(qaNewPermitPage, req.attributes())

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    fun newSta3(req: ServerRequest): ServerResponse {
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        val permitID = req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
        req.attributes()["permit"] =  loggedInUser.id?.let { qaDaoServices.findPermitBYUserIDAndId(permitID, it) }?: throw ExpectedDataNotFound("User Id required")
        req.attributes()["QaSta3Entity"] = QaSta3Entity()
        return ok().render(qaNewSta3Page, req.attributes())

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    fun newSta10(req: ServerRequest): ServerResponse {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        val permitID = req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
        req.attributes()["permit"] =  loggedInUser.id?.let { qaDaoServices.findPermitBYUserIDAndId(permitID, it) }?: throw ExpectedDataNotFound("User Id required")
        req.attributes()["counties"] = countyRepo.findByStatusOrderByCounty(map.activeStatus)
        req.attributes()["applicationDate"] = commonDaoServices.getCurrentDate()
        req.attributes()["QaSta10Entity"] = QaSta10Entity()
        return ok().render(qaNewSta10Page, req.attributes())

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    fun viewSta3(req: ServerRequest): ServerResponse {
        val permitUserId = req.paramOrNull("userID")?.toLong() ?: throw ExpectedDataNotFound("Required User ID, check config")

        val permitID = req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
        req.attributes()["permit"] =   qaDaoServices.findPermitBYUserIDAndId(permitID, permitUserId)
        req.attributes()["QaSta3Entity"] = qaDaoServices.findSTA3WithPermitIDBY(permitID)
        req.attributes()["message"] = applicationMapProperties.mapQualityAssuranceManufactureViewPage
        return ok().render(qaNewSta3Page, req.attributes())

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    fun viewSta10(req: ServerRequest): ServerResponse {
        val map = commonDaoServices.serviceMapDetails(appId)
        val permitUserId = req.paramOrNull("userID")?.toLong() ?: throw ExpectedDataNotFound("Required User ID, check config")

        val permitID = req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
        val qaSta10Entity = qaDaoServices.findSTA10WithPermitIDBY(permitID)
        req.attributes()["permit"] =   qaDaoServices.findPermitBYUserIDAndId(permitID, permitUserId)
        req.attributes()["QaSta10Entity"] = qaDaoServices.findSTA10WithPermitIDBY(permitID)
        req.attributes()["regionDetails"] = qaSta10Entity.region?.let { commonDaoServices.findRegionEntityByRegionID(it, map.activeStatus).region }
        req.attributes()["countyDetails"] = qaSta10Entity.county?.let { commonDaoServices.findCountiesEntityByCountyId(it, map.activeStatus).county }
        req.attributes()["townDetails"] = qaSta10Entity.town?.let { commonDaoServices.findTownEntityByTownId(it).town }
        req.attributes()["QaSta10Entity"] = qaSta10Entity
        req.attributes().putAll(loadSTA10UIComponents())
        req.attributes()["productsManufacturedList"] = qaSta10Entity.id?.let { qaDaoServices.findProductsManufactureWithSTA10ID(it) }
        req.attributes()["rawMaterialList"] = qaSta10Entity.id?.let { qaDaoServices.findRawMaterialsWithSTA10ID(it) }
        req.attributes()["machinePlantList"] = qaSta10Entity.id?.let { qaDaoServices.findMachinePlantsWithSTA10ID(it) }
        req.attributes()["manufacturingProcessesList"] = qaSta10Entity.id?.let { qaDaoServices.findManufacturingProcessesWithSTA10ID(it) }

//        req.attributes()["message"] = applicationMapProperties.mapQualityAssuranceManufactureViewPage
        return ok().render(qaNewSta10DetailsPage, req.attributes())

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun submitSta10(req: ServerRequest): ServerResponse {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        val permitID = req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
        val permit = loggedInUser.id?.let { qaDaoServices.findPermitBYUserIDAndId(permitID, it) }?: throw ExpectedDataNotFound("Required User ID, check config")
        with(permit){
            sta10FilledStatus = map.activeStatus
        }

        qaDaoServices.permitUpdateDetails(permit,map, loggedInUser)

        return ok().render("${qaDaoServices.permitDetails}=${permit.id}&userID=${loggedInUser.id}")

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun getInvoiceDetails(req: ServerRequest): ServerResponse {
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        val permitID = req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
        val permit = loggedInUser.id?.let { qaDaoServices.findPermitBYUserIDAndId(permitID, it) }?: throw ExpectedDataNotFound("Required User ID, check config")
        //Todo: Remove smart cast
        val invoiceDetails = qaDaoServices.findPermitInvoiceByPermitID(permitID, loggedInUser.id!!)
        val applicationState: String = when {
            permit.status!=10 -> { "Application" }
            else -> { "Renewal" }
        }

        req.attributes()["invoice"] = invoiceDetails
        req.attributes()["phoneNumber"] = permit.telephoneNo
        req.attributes()["applicationState"] = applicationState
        req.attributes()["permit"] = permit
        req.attributes()["product"] = permit.product?.let { commonDaoServices.findProductByID(it) }
        req.attributes()["permitType"] = permit.permitType?.let { qaDaoServices.findPermitType(it) }
        req.attributes()["mpesa"] = CdLaboratoryParametersEntity()
        req.attributes()["paymentMethods"] = paymentMethodsRepository.findAll()
        req.attributes()["appId"] = appId

        return ok().render(qaInvoiceGenerated, req.attributes())

    }

//    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
//    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
//    fun submitPermit(req: ServerRequest): String? {
//        val map = commonDaoServices.serviceMapDetails(appId)
//        val loggedInUser = commonDaoServices.loggedInUserDetails()
//        val result: ServiceRequestsEntity?
//
//        val permitID = req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
//        val qaSta10Entity = qaDaoServices.findSTA10WithPermitIDBY(permitID)
//        var permit = loggedInUser.id?.let { qaDaoServices.findPermitBYUserIDAndId(permitID, it) }?: throw ExpectedDataNotFound("Required User ID, check config")
//        result = qaDaoServices.permitInvoiceCalculation(map, loggedInUser, permit)
//        with(permit){
//            sendApplication = map.activeStatus
//        }
//        permit = qaDaoServices.permitUpdateDetails(permit, loggedInUser)
//
//        val sm = CommonDaoServices.MessageSuccessFailDTO()
//        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permit.id}&userID=${loggedInUser.id}"
//        sm.message = "You have successful Submitted Your Application, an invoice has been generated, check Your permit detail"
//
//        return commonDaoServices.returnValues(result, map, sm)
//
//    }

    private fun loadSTA10UIComponents(): MutableMap<String, Any?> {

        return mutableMapOf(
            Pair("QaProductManufacturedEntity", QaProductManufacturedEntity()),
            Pair("QaRawMaterialEntity", QaRawMaterialEntity()),
            Pair("QaMachineryEntity", QaMachineryEntity()),
            Pair("QaManufacturingProcessEntity", QaManufacturingProcessEntity()),
            Pair("countries", commonDaoServices.findCountryList()),
        )
    }


}