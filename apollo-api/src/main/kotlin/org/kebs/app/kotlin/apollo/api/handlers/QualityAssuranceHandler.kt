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
import org.kebs.app.kotlin.apollo.api.ports.provided.createUserAlert
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DestinationInspectionDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.QADaoServices
import org.kebs.app.kotlin.apollo.common.exceptions.*
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.di.*
import org.kebs.app.kotlin.apollo.store.model.qa.*
import org.kebs.app.kotlin.apollo.store.repo.*
import org.kebs.app.kotlin.apollo.store.repo.di.ILaboratoryRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.ServerResponse.ok
import org.springframework.web.servlet.function.paramOrNull
import java.sql.Date


@Component
class QualityAssuranceHandler(
    private val applicationMapProperties: ApplicationMapProperties,
    private val commonDaoServices: CommonDaoServices,
    private val destinationDaoServices: DestinationInspectionDaoServices,
    private val standardCategoryRepo: IStandardCategoryRepository,
    private val productCategoriesRepository: IKebsProductCategoriesRepository,
    private val broadProductCategoryRepository: IBroadProductCategoryRepository,
    private val productsRepo: IProductsRepository,
    private val paymentMethodsRepository: IPaymentMethodsRepository,
    private val countyRepo: ICountiesRepository,
    private val iLaboratoryRepo: ILaboratoryRepository,
    private val productSubCategoryRepo: IProductSubcategoryRepository,
    private val sampleStandardsRepository: ISampleStandardsRepository,
    private val qaDaoServices: QADaoServices

) {


    var employeeUserTypeId = 5L


    var manufacturerUserTypeId: Long = 4L

    private final val qaEmployeesHomePage = "quality-assurance/employees-qa-home"
    private final val qaCustomerHomePage = "quality-assurance/customer/customer-home"
    private final val qaPermitListPage = "quality-assurance/permit-list"
    private final val qaPermitDetailPage = "quality-assurance/customer/permit-details"
    private final val qaSchemeDetailPage = "quality-assurance/customer/scheme-of-supervision-and-control-details"
    private final val qaProductQualityStatusPage = "quality-assurance/product-quality-status"
    private final val qaNewSchemeDetailPage = "quality-assurance/customer/new-scheme-of-supervision-details"
    private final val qaNewPermitPage = "quality-assurance/customer/permit-application"
    private final val qaNewSta3Page = "quality-assurance/customer/sta3-new-details"
    private final val qaNewSta10Page = "quality-assurance/customer/sta10-new-application"
    private final val qaNewSta10OfficerPage = "quality-assurance/customer/sta10-new-application-officer"
    private final val qaNewSta10DetailsPage = "quality-assurance/customer/sta10-new-details"
    private final val qaInvoiceGenerated = "quality-assurance/customer/generated-invoice"

    //Inspection details
    private final val cdSampleCollectPage = "destination-inspection/cd-Inspection-documents/cd-inspection-sample-collect.html"
    private final val cdSampleSubmitPage = "destination-inspection/cd-Inspection-documents/cd-inspection-sample-submit.html"

    final val appId: Int = applicationMapProperties.mapQualityAssurance

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_OFFICER_MODIFY') or hasAuthority('QA_HOD_READ') or hasAuthority('QA_MANAGER_ASSESSORS_READ') or hasAuthority('QA_HOF_READ')")
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
                    req.attributes()["permitSmarkType"] = qaDaoServices.findPermitType(applicationMapProperties.mapQAPermitTypeIdSmark)
                    req.attributes()["permitDmarkType"] = qaDaoServices.findPermitType(applicationMapProperties.mapQAPermitTypeIDDmark)
                    req.attributes()["map"] = map
                    ok().render(qaEmployeesHomePage, req.attributes())
                }
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }
    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_OFFICER_READ') or hasAuthority('QA_HOD_READ') or hasAuthority('QA_MANAGER_ASSESSORS_READ') or hasAuthority('QA_HOF_READ')")
    fun permitList(req: ServerRequest): ServerResponse {
        try {
            val auth = commonDaoServices.loggedInUserAuthentication()
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val map = commonDaoServices.serviceMapDetails(appId)
            val permitTypeID = req.paramOrNull("permitTypeID")?.toLong() ?: throw ExpectedDataNotFound("Required PermitType ID, check config")
            val permitType = qaDaoServices.findPermitType(permitTypeID)
            var permitList : List<PermitApplicationsEntity>? = null
            permitList = when {
                auth.authorities.stream().anyMatch { authority -> authority.authority == "PERMIT_APPLICATION" } -> {
                    qaDaoServices.findAllUserPermitWithPermitType(loggedInUser, permitTypeID)
                }
                auth.authorities.stream().anyMatch { authority -> authority.authority == "QA_MANAGER_ASSESSORS_READ" } -> {
                    qaDaoServices.findAllQAMPermitListWithPermitType(loggedInUser, permitTypeID)
                }
                auth.authorities.stream().anyMatch { authority -> authority.authority == "QA_HOD_READ" } -> {
                    qaDaoServices.findAllHODPermitListWithPermitType(loggedInUser, permitTypeID)
                }
                auth.authorities.stream().anyMatch { authority -> authority.authority == "QA_OFFICER_READ" } -> {
                    qaDaoServices.findAllQAOPermitListWithPermitType(loggedInUser, permitTypeID)
                }
                else -> {
                    throw ExpectedDataNotFound("UNAUTHORISED LOGGED IN USER (ACCESS DENIED)")
                }
            }

            req.attributes()["permitType"] = permitType
            req.attributes()["permitList"] = permitList
            return ok().render(qaPermitListPage, req.attributes())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_OFFICER_MODIFY') or hasAuthority('QA_HOD_READ') or hasAuthority('QA_MANAGER_ASSESSORS_READ') or hasAuthority('QA_HOF_READ') ")
    fun permitDetails(req: ServerRequest): ServerResponse {
        val permitUserId = req.paramOrNull("userID")?.toLong() ?: throw ExpectedDataNotFound("Required User ID, check config")
        val permitID = req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
       val map = commonDaoServices.serviceMapDetails(appId)
        val permit = qaDaoServices.findPermitBYID(permitID)
        val plantAttached = permit.attachedPlantId?.let { qaDaoServices.findPlantDetails(it) }
        val docFileName : String? = null

        req.attributes().putAll(commonDaoServices.loadCommonUIComponents(map))

        if (permit.hofQamCompletenessStatus == map.activeStatus && permit.assignOfficerStatus != map.activeStatus) {
            req.attributes()["officers"] = qaDaoServices.findOfficersList(permit, map)
        }else if (permit.assignOfficerStatus == map.activeStatus) {
            req.attributes()["officerAssigned"] = permit.qaoId?.let { commonDaoServices.findUserByID(it) }
        }


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

    @PreAuthorize("hasAuthority('QA_MANAGER_ASSESSORS_MODIFY') or hasAuthority('QA_OFFICER_MODIFY')")
    fun newSta10Officer(req: ServerRequest): ServerResponse {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        val permitID = req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
        req.attributes()["permit"] = qaDaoServices.findPermitBYID(permitID)
        req.attributes()["counties"] = countyRepo.findByStatusOrderByCounty(map.activeStatus)
        req.attributes()["applicationDate"] = commonDaoServices.getCurrentDate()
        req.attributes()["QaSta10Entity"] = qaDaoServices.findSTA10WithPermitIDBY(permitID)
        return ok().render(qaNewSta10OfficerPage, req.attributes())

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_HOD_READ') or hasAuthority('QA_MANAGER_ASSESSORS_READ') or hasAuthority('QA_HOF_READ')")
    fun viewSta3(req: ServerRequest): ServerResponse {
        val permitUserId = req.paramOrNull("userID")?.toLong() ?: throw ExpectedDataNotFound("Required User ID, check config")

        val permitID = req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
        req.attributes()["permit"] =   qaDaoServices.findPermitBYUserIDAndId(permitID, permitUserId)
        req.attributes()["QaSta3Entity"] = qaDaoServices.findSTA3WithPermitIDBY(permitID)
        req.attributes()["message"] = applicationMapProperties.mapQualityAssuranceManufactureViewPage
        return ok().render(qaNewSta3Page, req.attributes())

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_HOD_READ') or hasAuthority('QA_MANAGER_ASSESSORS_READ') or hasAuthority('QA_HOF_READ') or hasAuthority('QA_OFFICER_MODIFY')")
    fun viewSta10Officer(req: ServerRequest): ServerResponse {
        val permitUserId = req.paramOrNull("userID")?.toLong() ?: throw ExpectedDataNotFound("Required User ID, check config")

        val permitID = req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
        req.attributes()["permit"] =   qaDaoServices.findPermitBYUserIDAndId(permitID, permitUserId)
        req.attributes()["QaSta10Entity"] = qaDaoServices.findSTA10WithPermitIDBY(permitID)
        req.attributes()["message"] = applicationMapProperties.mapQualityAssuranceManufactureViewPage
        return ok().render(qaNewSta3Page, req.attributes())

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_HOD_READ') or hasAuthority('QA_MANAGER_ASSESSORS_READ') or hasAuthority('QA_HOF_READ') or hasAuthority('QA_OFFICER_MODIFY')")
    fun viewSta10(req: ServerRequest): ServerResponse {
        val map = commonDaoServices.serviceMapDetails(appId)
        val permitUserId = req.paramOrNull("userID")?.toLong() ?: throw ExpectedDataNotFound("Required User ID, check config")

        val permitID = req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
        val qaSta10Entity = qaDaoServices.findSTA10WithPermitIDBY(permitID)
        req.attributes()["permit"] =   qaDaoServices.findPermitBYUserIDAndId(permitID, permitUserId)
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

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION') ")
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

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_OFFICER_MODIFY')")
    fun generatedSchemeSupervision(req: ServerRequest): ServerResponse {
        val permitID = req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
//        val map = commonDaoServices.serviceMapDetails(appId)
        val permit = qaDaoServices.findPermitBYID(permitID)

        req.attributes()["product"] = productsRepo.findByIdOrNull(permit.product)
        req.attributes()["schemeFound"] = qaDaoServices.findSchemeOfSupervisionWithPermitIDBY(permitID)
        req.attributes()["ksApplicable"] = sampleStandardsRepository.findBySubCategoryId(permit.productSubCategory)
        req.attributes()["permitDetails"] = permit

        return ok().render(qaSchemeDetailPage, req.attributes())

    }

    @PreAuthorize("hasAuthority('QA_OFFICER_MODIFY') or hasAuthority('QA_HOD_READ')")
    fun generateProductQualityStatus(req: ServerRequest): ServerResponse {
        val permitID = req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
        val permit = qaDaoServices.findPermitBYID(permitID)

        req.attributes()["product"] = productsRepo.findByIdOrNull(permit.product)
        req.attributes()["permitDetails"] = permit

        return ok().render(qaProductQualityStatusPage, req.attributes())
    }


    @PreAuthorize("hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_OFFICER_MODIFY')")
    fun newSchemeSupervision(req: ServerRequest): ServerResponse {
        val permitID = req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
//        val map = commonDaoServices.serviceMapDetails(appId)
        val permit = qaDaoServices.findPermitBYID(permitID)

        req.attributes()["QaSchemeForSupervisionEntity"] = QaSchemeForSupervisionEntity()
        req.attributes()["permitDetails"] = permit

        return ok().render(qaNewSchemeDetailPage, req.attributes())

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_HOD_READ') or hasAuthority('QA_MANAGER_ASSESSORS_READ') or hasAuthority('QA_HOF_READ') or hasAuthority('QA_OFFICER_MODIFY')")
    fun getInvoiceDetails(req: ServerRequest): ServerResponse {
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        val permitID = req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
        val permit = qaDaoServices.findPermitBYID(permitID)
        //Todo: Remove smart cast
        val invoiceDetails = permit.userId?.let { qaDaoServices.findPermitInvoiceByPermitID(permitID, it) }
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


//    fun inspectionDetails(req: ServerRequest): ServerResponse =
//        try {
//            val map = commonDaoServices.serviceMapDetails(appId)
//            val permitID = req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
//            val permit =  qaDaoServices.findPermitBYID(permitID)
//
//                  val docType =  req.paramOrNull("docType") ?: throw ExpectedDataNotFound("Required docType, check config")
//
//
//                            req.attributes().putAll(loadCommonUIComponents(map))
//                            req.attributes().putAll(loadCDInspectionModuleUIComponents(map))
//                            req.attributes()["map"] = map
//                            var myPage = ""
//                            // TODO: 12/15/2020  create a page for Error for initializing myPage
//                            when (docType) {
//                                destinationDaoServices.sampCollectName -> {
//                                    var ksEasApplicable: String? = null
//                                    var dateExpiry: Date? = null
//                                    var dateMfgPackaging: Date? = null
//                                    //Get checklist type details
//                                    when (filledCheckList.checkListType?.uuid) {
//                                        destinationDaoServices.agrochemItemChecklistType -> {
//                                            val agrochem = daoServices.findInspectionAgrochemWithInspectionGeneral(filledCheckList)
//                                            dateExpiry = agrochem.dateExpiry
//                                            dateMfgPackaging = agrochem.dateMfgPackaging
//                                            ksEasApplicable = agrochem.ksEasApplicable
//                                        }
//                                        destinationDaoServices.engineeringItemChecklistType -> {
//                                            val engineering = daoServices.findInspectionEngineeringWithInspectionGeneral(filledCheckList)
//                                            ksEasApplicable = engineering.ksEasApplicable
//                                        }
//                                        destinationDaoServices.otherItemChecklistType -> {
//                                            val others = daoServices.findInspectionOthersWithInspectionGeneral(filledCheckList)
//                                            ksEasApplicable = others.ksEasApplicable
//                                        }
//                                    }
//                                    req.attributes()["dateExpiry"] = dateExpiry
//                                    req.attributes()["dateMfgPackaging"] = dateMfgPackaging
//                                    req.attributes()["ksEasApplicable"] = ksEasApplicable
//                                    req.attributes()["message"] = docType
//                                    myPage = cdSampleCollectPage
//                                }
//                                destinationDaoServices.sampSubmitName -> {
//                                    val sampleCollect = daoServices.findSavedSampleCollection(cdItemID)
//                                    req.attributes().putAll(sampleDetails(daoServices.findSavedGeneralInspection(cdItemDetails), sampleCollect))
//                                    req.attributes().putAll(sampleDatesDetails(sampleCollect))
//                                    req.attributes()["message"] = docType
//                                    myPage = cdSampleSubmitPage
//                                }
//                                destinationDaoServices.sampSubmitAddParamDetails -> {
//                                    val sampleCollect = daoServices.findSavedSampleCollection(cdItemID)
//                                    val sampleSubmit = daoServices.findSavedSampleSubmission(cdItemID)
//                                    val itemParameters = daoServices.findListSampleSubmissionParameter(sampleSubmit)
//                                    req.attributes().putAll(sampleDetails(daoServices.findSavedGeneralInspection(cdItemDetails), sampleCollect))
//                                    req.attributes().putAll(sampleDatesDetails(sampleCollect))
//                                    req.attributes().putAll(loadSampleViewDetailsUIComponents(docType, daoServices.findSavedSampleSubmission(cdItemID), itemParameters))
//                                    req.attributes()["sampleParam"] = CdSampleSubmissionParamatersEntity()
//                                    myPage = cdSampleSubmitPage
//                                }
//                                destinationDaoServices.bsNumber -> {
//                                    val sampleCollect = daoServices.findSavedSampleCollection(cdItemID)
//                                    val sampleSubmit = daoServices.findSavedSampleSubmission(cdItemID)
//                                    val itemParameters = daoServices.findListSampleSubmissionParameter(sampleSubmit)
//                                    req.attributes().putAll(sampleDetails(daoServices.findSavedGeneralInspection(cdItemDetails), sampleCollect))
//                                    req.attributes().putAll(sampleDatesDetails(sampleCollect))
//                                    req.attributes().putAll(loadSampleViewDetailsUIComponents(docType, daoServices.findSavedSampleSubmission(cdItemID), itemParameters))
//                                    myPage = cdSampleSubmitPage
//                                }
//                                destinationDaoServices.labResults -> {
//                                    val sampleCollect = daoServices.findSavedSampleCollection(cdItemID)
//                                    val sampleSubmit = daoServices.findSavedSampleSubmission(cdItemID)
//                                    val itemParameters = daoServices.findListSampleSubmissionParameter(sampleSubmit)
//                                    req.attributes()["allParameterCompleteResults"] = daoServices.checkIfResultsCompleted(itemParameters, map)
//                                    req.attributes().putAll(sampleDetails(daoServices.findSavedGeneralInspection(cdItemDetails), sampleCollect))
//                                    req.attributes().putAll(sampleDatesDetails(sampleCollect))
//                                    req.attributes().putAll(loadSampleViewDetailsUIComponents(docType, daoServices.findSavedSampleSubmission(cdItemID), itemParameters))
//                                    myPage = cdSampleSubmitPage
//                                }
//                                destinationDaoServices.sampSubmitParamName -> {
//                                    val sampleSubmit = daoServices.findSavedSampleSubmission(cdItemID)
//                                    req.attributes()["itemParameters"] = daoServices.findListSampleSubmissionParameter(sampleSubmit)
//                                    req.attributes()["sampleSubmit"] = sampleSubmit
//                                    req.attributes()["message"] = docType
//                                    myPage = cdSampleSubmitPage
//                                }
//                            }
//                            return ServerResponse.ok().render(myPage, req.attributes())
//
//
//
//
//        } catch (e: Exception) {
//            createUserAlert(req, e)
//        }

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

    private fun loadCommonUIComponents(s: ServiceMapsEntity): MutableMap<String, Any> {
        return mutableMapOf(
            Pair("activeStatus", s.activeStatus),
            Pair("inActiveStatus", s.inactiveStatus),
            Pair("initStatus", s.initStatus),
            Pair("currentDate", commonDaoServices.getCurrentDate())
        )
    }

    private fun loadCDInspectionModuleUIComponents(map: ServiceMapsEntity): MutableMap<String, Any?> {

        return mutableMapOf(

            Pair("sampleSubmit", CdSampleSubmissionItemsEntity()),
            Pair("sampleCollect", CdSampleCollectionEntity()),
            Pair("sampleParam", CdSampleSubmissionParamatersEntity()),
            Pair("generalCheckList", CdInspectionGeneralEntity()),
            Pair("agrochemItemInspectionChecklist", CdInspectionAgrochemItemChecklistEntity()),
            Pair("engineeringItemInspectionChecklist", CdInspectionEngineeringItemChecklistEntity()),
            Pair("otherItemInspectionChecklist", CdInspectionOtherItemChecklistEntity()),
            Pair("motorVehicleItemInspectionChecklist", CdInspectionMotorVehicleItemChecklistEntity()),

            Pair("laboratories", iLaboratoryRepo.findByStatus(commonDaoServices.activeStatus.toInt())),

            Pair("sampCollectName", destinationDaoServices.sampCollectName),
            Pair("sampSubmitName", destinationDaoServices.sampSubmitName),
            Pair("sampSubmitParamName", destinationDaoServices.sampSubmitParamName),
            Pair("sampSubmitAddParamDetails", destinationDaoServices.sampSubmitAddParamDetails),
            Pair("checkListName", destinationDaoServices.checkListName),
            Pair("bsNumber", destinationDaoServices.bsNumber),
            Pair("labResults", destinationDaoServices.labResults),
            Pair("labResultsAllComplete", destinationDaoServices.labResultsAllComplete),
            Pair("viewPage", destinationDaoServices.viewPage)


//                Pair("rfcCOCEntity", MainRfcCocEntity()),
//                Pair("rfcCOREntity", MainRfcCorEntity()),
//                Pair("rfcCoiItemEntity", MainRfcCoiItemsEntity())

        )
    }



}