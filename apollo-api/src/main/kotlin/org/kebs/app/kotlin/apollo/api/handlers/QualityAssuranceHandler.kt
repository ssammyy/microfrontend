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
//import org.kebs.app.kotlin.apollo.api.ports.provided.createUserAlert
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DestinationInspectionDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.InvoiceDaoService
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.QADaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.lims.LimsServices
import org.kebs.app.kotlin.apollo.common.dto.FmarkEntityDto
import org.kebs.app.kotlin.apollo.common.dto.PermitEntityDto
import org.kebs.app.kotlin.apollo.common.dto.qa.CommonPermitDto
import org.kebs.app.kotlin.apollo.common.dto.qa.WorkPlanDto
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
    private val invoiceDaoService: InvoiceDaoService,
    private val countyRepo: ICountiesRepository,
    private val iLaboratoryRepo: ILaboratoryRepository,
    private val productSubCategoryRepo: IProductSubcategoryRepository,
    private val sampleStandardsRepository: ISampleStandardsRepository,
    private val  limsServices: LimsServices,
    private val qaDaoServices: QADaoServices

) {


    var employeeUserTypeId = 5L


    var manufacturerUserTypeId: Long = 4L

    private val qaEmployeesHomePage = "quality-assurance/employees-qa-home"
    private val qaCustomerHomePage = "quality-assurance/customer/customer-home"
    private val qaPermitListPage = "quality-assurance/permit-list"
    private val qaPermitDetailPage = "quality-assurance/customer/permit-details"
    private val qaSchemeDetailPage = "quality-assurance/customer/scheme-of-supervision-and-control-details"
    private val qaProductQualityStatusPage = "quality-assurance/product-quality-status"
    private val qaNewSchemeDetailPage = "quality-assurance/customer/new-scheme-of-supervision-details"
    private val qaNewPermitPage = "quality-assurance/customer/permit-application"
    private val qaNewSta3Page = "quality-assurance/customer/sta3-new-details"
    private val qaNewSta10Page = "quality-assurance/customer/sta10-new-application"
    private val qaNewSta10OfficerPage = "quality-assurance/customer/sta10-new-application-officer"
    private val qaNewSta10DetailsPage = "quality-assurance/customer/sta10-new-details"
    private val qaInvoiceGenerated = "quality-assurance/customer/generated-invoice"
    private val qaSSFDetailesPage = "quality-assurance/customer/ssf-details"
    private val qaAllWorkPlanCreatedListPage = "quality-assurance/created-workPlan-list.html"

    //Inspection details
    private val cdSampleCollectPage = "destination-inspection/cd-Inspection-documents/cd-inspection-sample-collect.html"
    private val cdSampleSubmitPage = "destination-inspection/cd-Inspection-documents/cd-inspection-sample-submit.html"

    final val appId: Int = applicationMapProperties.mapQualityAssurance

    @PreAuthorize(
        "hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_OFFICER_MODIFY') or hasAuthority('QA_HOD_READ') or hasAuthority('QA_MANAGER_ASSESSORS_READ') " +
                "or hasAuthority('QA_HOF_READ') or hasAuthority('QA_ASSESSORS_READ') or hasAuthority('QA_PAC_SECRETARY_READ') or hasAuthority('QA_PSC_MEMBERS_READ') or hasAuthority('QA_PCM_READ')"
    )
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
                    req.attributes()["permitFmarkType"] = qaDaoServices.findPermitType(applicationMapProperties.mapQAPermitTypeIdFmark)
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

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_OFFICER_READ') or hasAuthority('QA_HOD_READ') or hasAuthority('QA_MANAGER_ASSESSORS_READ')" +
            " or hasAuthority('QA_HOF_READ') or hasAuthority('QA_ASSESSORS_READ') or hasAuthority('QA_PAC_SECRETARY_READ') or hasAuthority('QA_PSC_MEMBERS_READ') or hasAuthority('QA_PCM_READ')")
    fun permitList(req: ServerRequest): ServerResponse {
        try {
            val auth = commonDaoServices.loggedInUserAuthentication()
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val map = commonDaoServices.serviceMapDetails(appId)
            val permitTypeID = req.paramOrNull("permitTypeID")?.toLong() ?: throw ExpectedDataNotFound("Required PermitType ID, check config")
            val permitType = qaDaoServices.findPermitType(permitTypeID)
            var permitList : List<PermitEntityDto>? = null
            when {
                auth.authorities.stream().anyMatch { authority -> authority.authority == "PERMIT_APPLICATION" } -> {
                    permitList = qaDaoServices.listPermits(qaDaoServices.findAllUserPermitWithPermitType(loggedInUser, permitTypeID), map)
                }
                auth.authorities.stream().anyMatch { authority -> authority.authority == "QA_MANAGER_ASSESSORS_READ" } -> {
                    permitList = qaDaoServices.listPermits(qaDaoServices.findAllQAMPermitListWithPermitType(loggedInUser, permitTypeID), map)

                }
                auth.authorities.stream().anyMatch { authority -> authority.authority == "QA_HOD_READ" } -> {
                    permitList = qaDaoServices.listPermits(qaDaoServices.findAllHODPermitListWithPermitType(loggedInUser, permitTypeID), map)

                }
                auth.authorities.stream().anyMatch { authority -> authority.authority == "QA_OFFICER_READ" } -> {
                    permitList = qaDaoServices.listPermits(qaDaoServices.findAllQAOPermitListWithPermitType(loggedInUser, permitTypeID), map)

                }
                auth.authorities.stream().anyMatch { authority -> authority.authority == "QA_ASSESSORS_READ" } -> {
                    permitList = qaDaoServices.listPermits(qaDaoServices.findAllAssessorPermitListWithPermitType(loggedInUser, permitTypeID), map)

                }
                auth.authorities.stream().anyMatch { authority -> authority.authority == "QA_PAC_SECRETARY_READ" } -> {
                    permitList = qaDaoServices.listPermits(qaDaoServices.findAllPacSecPermitListWithPermitType(loggedInUser, permitTypeID), map)

                }
                auth.authorities.stream().anyMatch { authority -> authority.authority == "QA_PCM_READ" } -> {
                    permitList = qaDaoServices.listPermits(qaDaoServices.findAllPCMPermitListWithPermitType(loggedInUser, permitTypeID), map)

                }
                auth.authorities.stream().anyMatch { authority -> authority.authority == "QA_PSC_MEMBERS_READ" } -> {
                    permitList = qaDaoServices.listPermits(qaDaoServices.findAllPSCPermitListWithPermitType(loggedInUser, permitTypeID), map)

                }
                else -> {
                    throw ExpectedDataNotFound("UNAUTHORISED LOGGED IN USER (ACCESS DENIED)")
                }
            }

            when (permitType.id) {
                applicationMapProperties.mapQAPermitTypeIdFmark -> {
                    req.attributes()["mySmarkPermits"] = qaDaoServices.findAllSmarkPermitWithNoFmarkGenerated(loggedInUser, applicationMapProperties.mapQAPermitTypeIdSmark, map.activeStatus, map.inactiveStatus)
                }
            }

            req.attributes().putAll(loadCommonUIComponents(map))
            req.attributes()["permitType"] = permitType
            req.attributes()["permitList"] = permitList
            return ok().render(qaPermitListPage, req.attributes())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }

    }




    @PreAuthorize("hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_OFFICER_MODIFY') or hasAuthority('QA_HOD_READ') " +
            "or hasAuthority('QA_MANAGER_ASSESSORS_READ') or hasAuthority('QA_HOF_READ') or hasAuthority('QA_ASSESSORS_READ') or hasAuthority('QA_PAC_SECRETARY_READ') or hasAuthority('QA_PSC_MEMBERS_READ') or hasAuthority('QA_PCM_READ')")
    fun permitDetails(req: ServerRequest): ServerResponse {
//        val permitUserId = req.paramOrNull("userID")?.toLong() ?: throw ExpectedDataNotFound("Required User ID, check config")
        val permitID = req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
       val map = commonDaoServices.serviceMapDetails(appId)
        val permit = qaDaoServices.findPermitBYID(permitID)

        req.attributes().putAll(loadCommonUIComponents(map))
        req.attributes().putAll(loadCommonPermitComponents(map,permit))
        req.attributes()["permit"] = qaDaoServices.permitDetails(permit,null,map)

        return ok().render(qaPermitDetailPage, req.attributes())
    }





    @PreAuthorize("hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_OFFICER_MODIFY') or hasAuthority('QA_HOD_READ') or hasAuthority('QA_MANAGER_ASSESSORS_READ') or hasAuthority('QA_HOF_READ') or hasAuthority('QA_ASSESSORS_READ') or hasAuthority('QA_PSC_MEMBERS_READ') or hasAuthority('QA_PCM_READ')")
    fun permitViewSmarkDetails(req: ServerRequest): ServerResponse {
        val smarkID = req.paramOrNull("smarkID")?.toLong() ?: throw ExpectedDataNotFound("Required Smark ID, check config")
       val map = commonDaoServices.serviceMapDetails(appId)
        val fmarkID = qaDaoServices.findFmarkWithSmarkId(smarkID)
        val permit = fmarkID.fmarkId?.let { qaDaoServices.findPermitBYID(it) } ?: throw ExpectedDataNotFound("Required Fmark ID MISSING")
        val plantAttached = permit.attachedPlantId?.let { qaDaoServices.findPlantDetails(it) }


        req.attributes().putAll(commonDaoServices.loadCommonUIComponents(map))

        if (permit.hofQamCompletenessStatus == map.activeStatus && permit.assignOfficerStatus != map.activeStatus) {
            req.attributes()["officers"] = permit.attachedPlantId?.let { qaDaoServices.findOfficersList(it, map, applicationMapProperties.mapQADesignationIDForQAOId) }
        }else if (permit.assignOfficerStatus == map.activeStatus) {
            req.attributes()["officerAssigned"] = permit.qaoId?.let { commonDaoServices.findUserByID(it) }
        }

        if (permit.justificationReportStatus == map.activeStatus && permit.assignAssessorStatus != map.activeStatus) {
            req.attributes()["assessors"] = permit.attachedPlantId?.let { qaDaoServices.findOfficersList(it, map, applicationMapProperties.mapQADesignationIDForAssessorId) }
        } else if (permit.assignAssessorStatus == map.activeStatus) {
            req.attributes()["assessorAssigned"] = permit.assessorId?.let { commonDaoServices.findUserByID(it) }
        }

        req.attributes()["fileParameters"] = qaDaoServices.findAllUploadedFileBYPermitID(smarkID)
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
        req.attributes()["plantAttached"] = plantAttached

        req.attributes()["oldVersionList"] = permit.permitRefNumber?.let { qaDaoServices.findAllOldPermitWithPermitID(it) }

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

        val permitID =
            req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
        req.attributes()["permit"] = loggedInUser.id?.let { qaDaoServices.findPermitBYUserIDAndId(permitID, it) }
            ?: throw ExpectedDataNotFound("User Id required")
        req.attributes()["message"] = applicationMapProperties.mapPermitNewMessage
        req.attributes()["QaSta3Entity"] = QaSta3Entity()
        return ok().render(qaNewSta3Page, req.attributes())

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    fun oldSta3(req: ServerRequest): ServerResponse {
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        val permitID =
            req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
        req.attributes()["message"] = applicationMapProperties.mapPermitRenewMessage
        req.attributes()["permit"] = loggedInUser.id?.let { qaDaoServices.findPermitBYUserIDAndId(permitID, it) }
            ?: throw ExpectedDataNotFound("User Id required")
        req.attributes()["QaSta3Entity"] = qaDaoServices.findSTA3WithPermitIDBY(permitID)
        return ok().render(qaNewSta3Page, req.attributes())

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    fun newSta10(req: ServerRequest): ServerResponse {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        val permitID =
            req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
        val permit = loggedInUser.id?.let { qaDaoServices.findPermitBYUserIDAndId(permitID, it) }
            ?: throw ExpectedDataNotFound("User Id required")
        req.attributes()["permit"] = permit
        req.attributes()["message"] = applicationMapProperties.mapPermitNewMessage
        req.attributes()["counties"] = countyRepo.findByStatusOrderByCounty(map.activeStatus)
        req.attributes()["applicationDate"] = commonDaoServices.getCurrentDate()
        req.attributes()["CommonPermitDto"] = qaDaoServices.companyDtoDetails(permit, map)
        req.attributes()["QaSta10Entity"] = QaSta10Entity()
        return ok().render(qaNewSta10Page, req.attributes())

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    fun oldSta10(req: ServerRequest): ServerResponse {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        val permitID =
            req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
        val permit = loggedInUser.id?.let { qaDaoServices.findPermitBYUserIDAndId(permitID, it) }
            ?: throw ExpectedDataNotFound("User Id required")

        req.attributes()["permit"] = permit
        req.attributes()["message"] = applicationMapProperties.mapPermitRenewMessage
        req.attributes()["counties"] = countyRepo.findByStatusOrderByCounty(map.activeStatus)
        req.attributes()["applicationDate"] = commonDaoServices.getCurrentDate()
        req.attributes()["CommonPermitDto"] = qaDaoServices.companyDtoDetails(permit, map)
        req.attributes()["QaSta10Entity"] =
            qaDaoServices.findSTA10WithPermitIDBY(permit.id ?: throw Exception("INVALID PERMIT ID"))
        return ok().render(qaNewSta10Page, req.attributes())

    }



    @PreAuthorize("hasAuthority('QA_MANAGER_ASSESSORS_MODIFY') or hasAuthority('QA_OFFICER_MODIFY')")
    fun newSta10Officer(req: ServerRequest): ServerResponse {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        val permitID =
            req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
        req.attributes()["permit"] = qaDaoServices.findPermitBYID(permitID)
        req.attributes()["counties"] = countyRepo.findByStatusOrderByCounty(map.activeStatus)
        req.attributes()["applicationDate"] = commonDaoServices.getCurrentDate()
        req.attributes()["QaSta10Entity"] = qaDaoServices.findSTA10WithPermitIDBY(permitID)
        return ok().render(qaNewSta10OfficerPage, req.attributes())

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_HOD_READ') or hasAuthority('QA_MANAGER_ASSESSORS_READ') or hasAuthority('QA_HOF_READ') or hasAuthority('QA_ASSESSORS_READ') or hasAuthority('QA_PSC_MEMBERS_READ') or hasAuthority('QA_PCM_READ')")
    fun viewSta3(req: ServerRequest): ServerResponse {
//        val permitUserId = req.paramOrNull("userID")?.toLong() ?: throw ExpectedDataNotFound("Required User ID, check config")

        val permitID = req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
        req.attributes()["permit"] =   qaDaoServices.findPermitBYID(permitID)
        req.attributes()["QaSta3Entity"] = qaDaoServices.findSTA3WithPermitIDBY(permitID)
        req.attributes()["message"] = applicationMapProperties.mapQualityAssuranceManufactureViewPage
        return ok().render(qaNewSta3Page, req.attributes())

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_HOD_READ') or hasAuthority('QA_MANAGER_ASSESSORS_READ') or hasAuthority('QA_HOF_READ') or hasAuthority('QA_OFFICER_MODIFY') or hasAuthority('QA_PSC_MEMBERS_READ') or hasAuthority('QA_PCM_READ')")
    fun viewSta10Officer(req: ServerRequest): ServerResponse {
//        val permitUserId = req.paramOrNull("userID")?.toLong() ?: throw ExpectedDataNotFound("Required User ID, check config")

        val permitID =
            req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
        req.attributes()["permit"] = qaDaoServices.findPermitBYID(permitID)
        req.attributes()["QaSta10Entity"] = qaDaoServices.findSTA10WithPermitIDBY(permitID)
        req.attributes()["message"] = applicationMapProperties.mapQualityAssuranceManufactureViewPage
        return ok().render(qaNewSta3Page, req.attributes())

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_HOD_READ') or hasAuthority('QA_MANAGER_ASSESSORS_READ') or hasAuthority('QA_HOF_READ') or hasAuthority('QA_OFFICER_MODIFY') or hasAuthority('QA_PSC_MEMBERS_READ') or hasAuthority('QA_PCM_READ')")
    fun viewRequestDetails(req: ServerRequest): ServerResponse {
//        val permitUserId = req.paramOrNull("userID")?.toLong() ?: throw ExpectedDataNotFound("Required User ID, check config")

        val permitID =
            req.paramOrNull("requestID")?.toLong() ?: throw ExpectedDataNotFound("Required Request ID, check config")
        val request = qaDaoServices.findRequestWithId(permitID)
        return ok().body(request)

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_HOD_READ') or hasAuthority('QA_MANAGER_ASSESSORS_READ') or hasAuthority('QA_HOF_READ') or hasAuthority('QA_OFFICER_MODIFY') or hasAuthority('QA_PSC_MEMBERS_READ') or hasAuthority('QA_PCM_READ')")
    fun viewSta10(req: ServerRequest): ServerResponse {
        val map = commonDaoServices.serviceMapDetails(appId)
//        val permitUserId = req.paramOrNull("userID")?.toLong() ?: throw ExpectedDataNotFound("Required User ID, check config")

        val permitID =
            req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
        val qaSta10Entity = qaDaoServices.findSTA10WithPermitIDBY(permitID)
        req.attributes().putAll(commonDaoServices.loadCommonUIComponents(map))
        val permit = qaDaoServices.findPermitBYID(permitID)
        req.attributes()["permit"] =  permit
        req.attributes()["plantAttached"] =  permit.attachedPlantId?.let { qaDaoServices.findPlantDetails(it) }
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
            permitStatus = applicationMapProperties.mapQaStatusPSubmission
        }

        qaDaoServices.permitUpdateDetails(permit,map, loggedInUser)

        return ok().render("${qaDaoServices.permitDetails}=${permit.id}&userID=${loggedInUser.id}")

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_OFFICER_MODIFY') or hasAuthority('QA_HOD_READ') " +
            "or hasAuthority('QA_MANAGER_ASSESSORS_READ') or hasAuthority('QA_HOF_READ') or hasAuthority('QA_ASSESSORS_READ') or hasAuthority('QA_PAC_SECRETARY_READ') or hasAuthority('QA_PSC_MEMBERS_READ') or hasAuthority('QA_PCM_READ')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
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


    @PreAuthorize("hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_HOD_READ') or hasAuthority('QA_MANAGER_ASSESSORS_READ') or hasAuthority('QA_HOF_READ') or hasAuthority('QA_OFFICER_MODIFY') or hasAuthority('QA_ASSESSORS_READ') or hasAuthority('QA_PSC_MEMBERS_READ') or hasAuthority('QA_PCM_READ')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun newSchemeSupervision(req: ServerRequest): ServerResponse {
        val permitID = req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
//        val map = commonDaoServices.serviceMapDetails(appId)
        val permit = qaDaoServices.findPermitBYID(permitID)

        req.attributes()["QaSchemeForSupervisionEntity"] = QaSchemeForSupervisionEntity()
        req.attributes()["permitDetails"] = permit

        return ok().render(qaNewSchemeDetailPage, req.attributes())

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_HOD_READ') or hasAuthority('QA_MANAGER_ASSESSORS_READ') or hasAuthority('QA_HOF_READ') or hasAuthority('QA_OFFICER_MODIFY') or hasAuthority('QA_ASSESSORS_READ') or hasAuthority('QA_PSC_MEMBERS_READ') or hasAuthority('QA_PCM_READ')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun updateSchemeSupervision(req: ServerRequest): ServerResponse {
        val permitID = req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
//        val map = commonDaoServices.serviceMapDetails(appId)
        val permit = qaDaoServices.findPermitBYID(permitID)

        req.attributes()["message"] = "updateScheme"
        req.attributes()["QaSchemeForSupervisionEntity"] = qaDaoServices.findSchemeOfSupervisionWithPermitIDBY(permitID)
        req.attributes()["permitDetails"] = permit

        return ok().render(qaNewSchemeDetailPage, req.attributes())

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_HOD_READ') or hasAuthority('QA_MANAGER_ASSESSORS_READ') or hasAuthority('QA_HOF_READ') or hasAuthority('QA_OFFICER_MODIFY') or hasAuthority('QA_PSC_MEMBERS_READ') or hasAuthority('QA_PCM_READ')")
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
        req.attributes()["invoiceBalanceDetails"] = invoiceDaoService.findInvoiceStgReconciliationDetails(invoiceDetails?.invoiceNumber?: throw ExpectedDataNotFound("INVALID REFERENCE CODE"))
        req.attributes()["product"] = permit.product?.let { commonDaoServices.findProductByID(it) }
        req.attributes()["permitType"] = permit.permitType?.let { qaDaoServices.findPermitType(it) }
        req.attributes()["mpesa"] = CdLaboratoryParametersEntity()
        req.attributes()["paymentMethods"] = paymentMethodsRepository.findAll()
        req.attributes()["appId"] = appId

        return ok().render(qaInvoiceGenerated, req.attributes())

    }


    @PreAuthorize("hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_HOD_READ') or hasAuthority('QA_MANAGER_ASSESSORS_READ') or hasAuthority('QA_HOF_READ') or hasAuthority('QA_OFFICER_MODIFY') or hasAuthority('QA_PSC_MEMBERS_READ') or hasAuthority('QA_PCM_READ')")
    fun getSSfDetails(req: ServerRequest): ServerResponse {
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        val permitID = req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
        val permit = qaDaoServices.findPermitBYID(permitID)
        val ssfDetails = qaDaoServices.findSampleSubmittedBYPermitID(permit.id?: throw ExpectedDataNotFound("MISSING PERMIT ID"))

        req.attributes()["ssfDetails"] = ssfDetails
        val labResultsParameters = qaDaoServices.findSampleLabTestResultsRepoBYBSNumber(
            ssfDetails.bsNumber ?: throw ExpectedDataNotFound("MISSING BS NUMBER")
        )
        KotlinLogging.logger { }.info { ssfDetails.bsNumber }
        req.attributes()["LabResultsParameters"] = labResultsParameters

        return ok().render(qaSSFDetailesPage, req.attributes())

    }

/*:::::::::::::::::::::::::::::::::::::::::::::START OF WORKPLAN(SURVEILLANCE) FUNCTIONS:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/


    @PreAuthorize(" hasAuthority('QA_OFFICER_READ')")
    fun allWorkPlanList(req: ServerRequest): ServerResponse {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)

        req.attributes()["allWorkPlanCreated"] = qaDaoServices.listWorkPlan(qaDaoServices.findALlCreatedWorkPlanWIthOfficerID(loggedInUser.id?:throw Exception("INVALID USER ID")), map)
        return ok().render(qaAllWorkPlanCreatedListPage, req.attributes())
    }

    @PreAuthorize(" hasAuthority('QA_OFFICER_READ')")
    fun workPlanDetails(req: ServerRequest): ServerResponse {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        val refNumber = req.paramOrNull("refNumber") ?: throw ExpectedDataNotFound("Required REF Number, check config")
        val workPlan = qaDaoServices.findCreatedWorkPlanWIthOfficerID(loggedInUser.id?:throw Exception("INVALID USER ID"), refNumber)

        val permit = qaDaoServices.findPermitBYID(workPlan.permitId?: throw Exception("INVALID PERMIT ID"))

        req.attributes().putAll(loadCommonUIComponents(map))
        req.attributes().putAll(loadCommonPermitComponents(map,permit))
        req.attributes()["permit"] = qaDaoServices.permitDetails(permit,null,map)

        return ok().render(qaPermitDetailPage, req.attributes())
    }


/*:::::::::::::::::::::::::::::::::::::::::::::END OF WORKPLAN(SURVEILLANCE) FUNCTIONS:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

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
            Pair("permitRequest", PermitUpdateDetailsRequestsEntity()),
            Pair("requestDetails", PermitUpdateDetailsRequestsEntity()),
            Pair("fmarkEntityDto", FmarkEntityDto()),
            Pair("SampleSubmissionDetails", QaSampleSubmissionEntity()),
            Pair("fmarkPermit", applicationMapProperties.mapQAPermitTypeIdFmark),
            Pair("dmarkPermit", applicationMapProperties.mapQAPermitTypeIDDmark),
            Pair("smarkPermit", applicationMapProperties.mapQAPermitTypeIdSmark),
            Pair("currentDate", commonDaoServices.getCurrentDate())
        )
    }

    private fun loadCommonPermitComponents(s: ServiceMapsEntity, permit: PermitApplicationsEntity): MutableMap<String, Any?> {
        return mutableMapOf(

            Pair("oldVersionList", qaDaoServices.findAllOldPermitWithPermitID( permit.permitRefNumber?:throw Exception("INVALID PERMIT REF NUMBER"))),
            Pair("permitType", qaDaoServices.findPermitType(permit.permitType?:throw Exception("INVALID PERMIT TYPE ID"))),
            Pair("fileParameters", qaDaoServices.findAllUploadedFileBYPermitID(permit.id?:throw Exception("INVALID PERMIT ID"))),
//            Pair("statusName", qaDaoServices.findPermitStatus(permit.permitStatus?:throw Exception("INVALID PERMIT STATUS VALUE"))),
            Pair("myRequests", qaDaoServices.findAllRequestByPermitID(permit.id?: throw Exception("INVALID PERMIT ID"))),
            Pair("userRequestTypes", qaDaoServices.findAllQaRequestTypes(s.activeStatus)),
            Pair("permitDetails", permit),
            Pair("plantsDetails", permit.userId?.let { qaDaoServices.findAllPlantDetails(it) } ?: throw ExpectedDataNotFound("Required User ID, from Permit Details")),
            Pair("officers", permit.attachedPlantId?.let { qaDaoServices.findOfficersList(it, s, applicationMapProperties.mapQADesignationIDForQAOId) }),
            Pair("assessorAssigned", permit.attachedPlantId?.let { qaDaoServices.findOfficersList(it, s, applicationMapProperties.mapQADesignationIDForAssessorId) })
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