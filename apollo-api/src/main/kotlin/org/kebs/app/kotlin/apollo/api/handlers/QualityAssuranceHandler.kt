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
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.QualityAssuranceBpmn
//import org.kebs.app.kotlin.apollo.api.ports.provided.createUserAlert
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DestinationInspectionDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.InvoiceDaoService
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.QADaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.lims.LimsServices
import org.kebs.app.kotlin.apollo.common.dto.FmarkEntityDto
import org.kebs.app.kotlin.apollo.common.dto.qa.*
import org.kebs.app.kotlin.apollo.common.exceptions.*
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.di.*
import org.kebs.app.kotlin.apollo.store.model.qa.*
import org.kebs.app.kotlin.apollo.store.repo.*
import org.kebs.app.kotlin.apollo.store.repo.di.ILaboratoryRepository
import org.kebs.app.kotlin.apollo.store.repo.qa.IQaBatchInvoiceRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.ServerResponse.ok
import org.springframework.web.servlet.function.body
import org.springframework.web.servlet.function.paramOrNull


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
    private val limsServices: LimsServices,
    private val invoiceBatchRepo: IQaBatchInvoiceRepository,
    private val qualityAssuranceBpmn: QualityAssuranceBpmn,
    private val qaDaoServices: QADaoServices

) {


    var employeeUserTypeId = 5L


    var manufacturerUserTypeId: Long = 4L

    private val qaEmployeesHomePage = "quality-assurance/employees-qa-home"
    private val qaCustomerHomePage = "quality-assurance/customer/customer-home"
    private val qaPermitListPage = "quality-assurance/permit-list"
    private val qaPermitInvoiceListPage = "quality-assurance/permit-invoice-list.html"
    private val qaPermitDetailPage = "quality-assurance/customer/permit-details"
    private val qaSchemeDetailPage = "quality-assurance/customer/scheme-of-supervision-and-control-details"
    private val qaProductQualityStatusPage = "quality-assurance/product-quality-status"
    private val qaNewSchemeDetailPage = "quality-assurance/customer/new-scheme-of-supervision-details"
    private val qaNewPermitPage = "quality-assurance/customer/permit-application"
    private val qaNewPermitInvoicePage = "quality-assurance/permit-batch-invoice-list"
    private val qaNewSta3Page = "quality-assurance/customer/sta3-new-details"
    private val qaNewSta10Page = "quality-assurance/customer/sta10-new-application"
    private val qaNewSta10OfficerPage = "quality-assurance/customer/sta10-new-application-officer"
    private val qaNewSta10DetailsPage = "quality-assurance/customer/sta10-new-details"
    private val qaInvoiceGenerated = "quality-assurance/customer/generated-invoice"
    private val qaSSFDetailesPage = "quality-assurance/customer/ssf-details"
    private val qaAllWorkPlanCreatedListPage = "quality-assurance/created-workPlan-list.html"
    private val qaInspectionReportPage = "quality-assurance/customer/inspection-report-new-details"

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
//            var viewPage = qaPermitListPage
            val permitTypeID = req.paramOrNull("permitTypeID")?.toLong() ?: throw ExpectedDataNotFound("Required PermitType ID, check config")
            val permitType = qaDaoServices.findPermitType(permitTypeID)
            req.attributes().putAll(loadCommonUIComponents(map))
            req.attributes()["permitType"] = permitType

            when (permitType.id) {
                applicationMapProperties.mapQAPermitTypeIdFmark -> {
                    req.attributes()["mySmarkPermits"] = qaDaoServices.findAllSmarkPermitWithNoFmarkGenerated(loggedInUser, applicationMapProperties.mapQAPermitTypeIdSmark, map.activeStatus, map.inactiveStatus)
                }
                applicationMapProperties.mapQAPermitTypeIdInvoices -> {
//                    val plantsDetails = qaDaoServices.findAllPlantDetails(loggedInUser.id?:throw Exception("INVALID USER ID"))
                    val allUnpaidInvoices= qaDaoServices.findALlPermitInvoicesCreatedByUserWithNoPaymentStatus(loggedInUser.id?:throw Exception("INVALID USER ID"), map.inactiveStatus)

                    req.attributes()["NewBatchInvoiceDto"] = NewBatchInvoiceDto()
//                    req.attributes()["plantsDetails"] = plantsDetails
                    req.attributes()["allPermitInvoiceList"] = qaDaoServices.listPermitsInvoices(allUnpaidInvoices, null, map)
                    req.attributes()["allBatchPermitInvoiceList"] = qaDaoServices.findALlBatchInvoicesWithUserID(
                        loggedInUser.id ?: throw Exception("INVALID USER ID")
                    )
                    return ok().render(qaPermitInvoiceListPage, req.attributes())
//                    viewPage = qaDaoServices.batchInvoiceList
                }
            }

            var permitListAllApplications: List<PermitEntityDto>? = null
            var permitListMyTasks: List<PermitEntityDto>? = null
            var permitListAllComplete: List<PermitEntityDto>? = null
            when {
                auth.authorities.stream().anyMatch { authority -> authority.authority == "PERMIT_APPLICATION" } -> {
                    permitListAllApplications = qaDaoServices.listPermits(qaDaoServices.findAllUserPermitWithPermitType(loggedInUser, permitTypeID), map)
                    permitListAllComplete = qaDaoServices.listPermits(qaDaoServices.findAllUserPermitWithPermitTypeAwardedStatusIsNull(loggedInUser, permitTypeID), map)
                    permitListMyTasks = qaDaoServices.listPermits(qaDaoServices.findAllUserPermitWithPermitTypeAwardedStatusIsNullAndTaskID(loggedInUser, permitTypeID, applicationMapProperties.mapUserTaskNameMANUFACTURE), map)
                }
                auth.authorities.stream().anyMatch { authority -> authority.authority == "QA_MANAGER_ASSESSORS_READ" } -> {
                    permitListAllApplications = qaDaoServices.listPermits(qaDaoServices.findAllQAMPermitListWithPermitType(loggedInUser, permitTypeID), map)
                    permitListAllComplete = qaDaoServices.listPermits(qaDaoServices.findAllQAMPermitListWithPermitTypeAwardedStatusIsNotNull(loggedInUser, permitTypeID), map)
                    permitListMyTasks = qaDaoServices.listPermits(qaDaoServices.findAllQAMPermitListWithPermitTypeAwardedStatusIsNotNullTaskID(loggedInUser, permitTypeID, applicationMapProperties.mapUserTaskNameQAM), map)
                }
                auth.authorities.stream().anyMatch { authority -> authority.authority == "QA_HOD_READ" } -> {
                    permitListAllApplications = qaDaoServices.listPermits(qaDaoServices.findAllHODPermitListWithPermitType(loggedInUser, permitTypeID), map)
                    permitListAllComplete = qaDaoServices.listPermits(qaDaoServices.findAllHODPermitListWithPermitTypeAwardedStatusIsNotNull(loggedInUser, permitTypeID), map)
                    permitListMyTasks = qaDaoServices.listPermits(qaDaoServices.findAllHODPermitListWithPermitTypeTaskID(loggedInUser, permitTypeID, applicationMapProperties.mapUserTaskNameHOD), map)
                }
                auth.authorities.stream().anyMatch { authority -> authority.authority == "QA_OFFICER_READ" } -> {
                    permitListAllApplications = qaDaoServices.listPermits(qaDaoServices.findAllQAOPermitListWithPermitType(loggedInUser, permitTypeID), map)
                    permitListAllComplete = qaDaoServices.listPermits(qaDaoServices.findAllQAOPermitListWithPermitTypeAwardedStatusIsNotNull(loggedInUser, permitTypeID), map)
                    permitListMyTasks = qaDaoServices.listPermits(qaDaoServices.findAllQAOPermitListWithPermitTypeTaskID(loggedInUser, permitTypeID, applicationMapProperties.mapUserTaskNameQAO), map)

                }
                auth.authorities.stream().anyMatch { authority -> authority.authority == "QA_ASSESSORS_READ" } -> {
                    permitListAllApplications = qaDaoServices.listPermits(qaDaoServices.findAllAssessorPermitListWithPermitType(loggedInUser, permitTypeID), map)
                    permitListAllComplete = qaDaoServices.listPermits(qaDaoServices.findAllAssessorPermitListWithPermitTypeAwardedStatusIsNotNull(loggedInUser, permitTypeID), map)
                    permitListMyTasks = qaDaoServices.listPermits(qaDaoServices.findAllAssessorPermitListWithPermitTypeTaskID(loggedInUser, permitTypeID, applicationMapProperties.mapUserTaskNameASSESSORS), map)

                }
                auth.authorities.stream().anyMatch { authority -> authority.authority == "QA_PAC_SECRETARY_READ" } -> {
                    permitListAllApplications = qaDaoServices.listPermits(qaDaoServices.findAllPacSecPermitListWithPermitType(loggedInUser, permitTypeID), map)
                    permitListAllComplete = qaDaoServices.listPermits(qaDaoServices.findAllPacSecPermitListWithPermitTypeAwardedStatusIsNotNull(loggedInUser, permitTypeID), map)
                    permitListMyTasks = qaDaoServices.listPermits(qaDaoServices.findAllPacSecPermitListWithPermitTypeTaskID(loggedInUser, permitTypeID, applicationMapProperties.mapUserTaskNamePACSECRETARY), map)
                }
                auth.authorities.stream().anyMatch { authority -> authority.authority == "QA_PCM_READ" } -> {
                    permitListAllApplications = qaDaoServices.listPermits(qaDaoServices.findAllPCMPermitListWithPermitType(loggedInUser, permitTypeID), map)
                    permitListAllComplete = qaDaoServices.listPermits(qaDaoServices.findAllPCMPermitListWithPermitTypeAwardedStatusIsNotNull(loggedInUser, permitTypeID), map)
                    permitListMyTasks = qaDaoServices.listPermits(qaDaoServices.findAllPCMPermitListWithPermitTypeTaskID(loggedInUser,permitTypeID, applicationMapProperties.mapUserTaskNamePCM), map)

                }
                auth.authorities.stream().anyMatch { authority -> authority.authority == "QA_PSC_MEMBERS_READ" } -> {
                    permitListAllApplications = qaDaoServices.listPermits(qaDaoServices.findAllPSCPermitListWithPermitType(loggedInUser, permitTypeID), map)
                    permitListAllComplete = qaDaoServices.listPermits(qaDaoServices.findAllPSCPermitListWithPermitTypeAwardedStatusIsNotNull(loggedInUser, permitTypeID), map)
                    permitListMyTasks = qaDaoServices.listPermits(qaDaoServices.findAllPSCPermitListWithPermitTypeTaskID(loggedInUser, permitTypeID, applicationMapProperties.mapUserTaskNamePSC), map)
                }
                else -> {
                    throw ExpectedDataNotFound("UNAUTHORISED LOGGED IN USER (ACCESS DENIED)")
                }
            }


            req.attributes()["permitListAllApplications"] = permitListAllApplications
            req.attributes()["permitListAllComplete"] = permitListAllComplete
            req.attributes()["permitListMyTasks"] = permitListMyTasks
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
        val permitID = req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
        val map = commonDaoServices.serviceMapDetails(appId)
        val auth = commonDaoServices.loggedInUserAuthentication()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val permit = qaDaoServices.findPermitBYID(permitID)
        val departmentEntity = commonDaoServices.findDepartmentByID(applicationMapProperties.mapQADepertmentId)

        if (auth.authorities.stream().anyMatch { authority -> authority.authority == "MODIFY_COMPANY" }) {
            req.attributes()["plantsDetails"] = qaDaoServices.findAllPlantDetailsWithCompanyID(loggedInUser.companyId?:throw ExpectedDataNotFound("Missing COMPANY ID"))
        }
        req.attributes()["sections"] = loadSectionDetails(departmentEntity, map, req)
        req.attributes()["standardsList"] = qaDaoServices.findALlStandardsDetails(map.activeStatus)
        req.attributes().putAll(loadCommonUIComponents(map))
        req.attributes().putAll(loadCommonPermitComponents(map,permit))
        req.attributes()["permit"] = qaDaoServices.permitDetails(permit, map)

        return ok().render(qaPermitDetailPage, req.attributes())
    }





    @PreAuthorize("hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_OFFICER_MODIFY') or hasAuthority('QA_HOD_READ') or hasAuthority('QA_MANAGER_ASSESSORS_READ') or hasAuthority('QA_HOF_READ') or hasAuthority('QA_ASSESSORS_READ') or hasAuthority('QA_PSC_MEMBERS_READ') or hasAuthority('QA_PCM_READ')")
    fun permitViewSmarkDetails(req: ServerRequest): ServerResponse {
        val smarkID =
            req.paramOrNull("smarkID")?.toLong() ?: throw ExpectedDataNotFound("Required Smark ID, check config")
        val map = commonDaoServices.serviceMapDetails(appId)
        val fmarkID = qaDaoServices.findFmarkWithSmarkId(smarkID)
        val permit = fmarkID.fmarkId?.let { qaDaoServices.findPermitBYID(it) } ?: throw ExpectedDataNotFound("Required Fmark ID MISSING")
        val plantAttached = permit.attachedPlantId?.let { qaDaoServices.findPlantDetails(it) }


        req.attributes().putAll(loadCommonUIComponents(map))
        req.attributes().putAll(loadCommonPermitComponents(map, permit))
        req.attributes()["permit"] = qaDaoServices.permitDetails(permit, map)

        return ok().render(qaPermitDetailPage, req.attributes())
    }


    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    fun newPermit(req: ServerRequest): ServerResponse {
        val map = commonDaoServices.serviceMapDetails(appId)
        val permitTypeID = req.paramOrNull("permitTypeID")?.toLong() ?: throw ExpectedDataNotFound("Required PermitType ID, check config")
        val permitType = qaDaoServices.findPermitType(permitTypeID)
        val departmentEntity = commonDaoServices.findDepartmentByID(applicationMapProperties.mapQADepertmentId)

        req.attributes()["sections"] = loadSectionDetails(departmentEntity, map, req)
        req.attributes()["applicationDate"] = commonDaoServices.getCurrentDate()
        req.attributes()["permitType"] = permitType
        req.attributes()["permit"] = PermitApplicationsEntity()

//        req.attributes()["divisions"] = commonDaoServices.findDivisionByDepartmentId(departmentEntity, map.activeStatus)
        req.attributes()["standardCategory"] = standardCategoryRepo.findByStatusOrderByStandardCategory(map.activeStatus)
        return ok().render(qaNewPermitPage, req.attributes())

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    fun batchInvoiceList(req: ServerRequest): ServerResponse {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val plantsDetails = qaDaoServices.findAllPlantDetails(loggedInUser.id?:throw Exception("INVALID USER ID"))
        val allUnpaidInvoices= qaDaoServices.findALlPermitInvoicesCreatedByUserWithNoPaymentStatus(loggedInUser.id?:throw Exception("INVALID USER ID"), map.inactiveStatus)

        req.attributes()["QaBatchInvoiceEntity"] = QaBatchInvoiceEntity()
        req.attributes()["plantsDetails"] = plantsDetails
//        req.attributes()["allPermitInvoiceList"] = qaDaoServices.listPermitsInvoices(allUnpaidInvoices, map)
        req.attributes()["allBatchPermitInvoiceList"] = qaDaoServices.findALlBatchInvoicesWithUserID(loggedInUser.id?:throw Exception("INVALID USER ID"))
        return ok().render(qaPermitInvoiceListPage, req.attributes())

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    fun permitInvoiceList(req: ServerRequest): ServerResponse {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val plantID = req.pathVariable("plantID").toLong()
        val allUnpaidInvoices= qaDaoServices.findALlPermitInvoicesCreatedByUserWithNoPaymentStatus(loggedInUser.id?:throw Exception("INVALID USER ID"), map.inactiveStatus )

        val allPermitInvoiceList = qaDaoServices.listPermitsInvoices(allUnpaidInvoices,plantID, map)

        return ok().body(allPermitInvoiceList)

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    fun generatedInvoicePermit(req: ServerRequest): ServerResponse {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val allUnpaidInvoices= qaDaoServices.findALlPermitInvoicesCreatedByUserWithNoPaymentStatus(loggedInUser.id?:throw Exception("INVALID USER ID"), map.inactiveStatus)
//        val

//        req.attributes()["InvoiceDetails"] = qaDaoServices.populateInvoiceDetails()
//        req.attributes()["allPermitInvoiceList"] = qaDaoServices.listPermitsInvoices(allUnpaidInvoices, map)
        return ok().render(qaNewPermitInvoicePage, req.attributes())

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    fun batchInvoiceDetails(req: ServerRequest): ServerResponse {
        val map = commonDaoServices.serviceMapDetails(appId)
        val batchID = req.paramOrNull("batchID")?.toLong() ?: throw ExpectedDataNotFound("Required batch ID, check config")
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        val batchInvoiceEntity = qaDaoServices.findBatchInvoicesWithID(batchID)
//        val plantDetail = qaDaoServices.findPlantDetails(batchInvoiceEntity.plantId?:throw  Exception("INVALID PLANT ID FOUND"))
        val companyProfile = commonDaoServices.findCompanyProfile(loggedInUser.id?:throw  Exception("INVALID USER ID FOUND"))
        val allInvoicesInBatch= qaDaoServices.findALlInvoicesPermitWithBatchID(batchID)
        val allUnpaidInvoices= qaDaoServices.findALlPermitInvoicesCreatedByUserWithNoPaymentStatus(loggedInUser.id?:throw Exception("INVALID USER ID"), map.inactiveStatus)

        req.attributes()["NewBatchInvoiceDto"] = NewBatchInvoiceDto()
        req.attributes()["InvoiceDetails"] = qaDaoServices.populateInvoiceDetails(companyProfile,batchInvoiceEntity, map)
        req.attributes()["allBatchPermitInvoiceList"] = qaDaoServices.listPermitsInvoices(allInvoicesInBatch,null, map)
        req.attributes()["allPermitInvoiceList"] = qaDaoServices.listPermitsInvoices(allUnpaidInvoices,null, map)
        return ok().render(qaNewPermitInvoicePage, req.attributes())

    }

    private fun loadSectionDetails(
        departmentEntity: DepartmentsEntity,
        map: ServiceMapsEntity,
        req: ServerRequest
    ): MutableList<SectionsEntity> {
        val divisions = commonDaoServices.findDivisionByDepartmentId(departmentEntity, map.activeStatus)
        val sections = mutableListOf<SectionsEntity>()
        divisions.forEach { div ->
            val sectionFound = commonDaoServices.findAllSectionsListWithDivision(div, map.activeStatus)
            sectionFound.forEach { sec ->
                sections.add(sec)
            }
        }

       return sections
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
        val permitID = req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
        val permit = qaDaoServices.findPermitBYUserIDAndId(permitID, loggedInUser.id?: throw ExpectedDataNotFound("User Id required"))


        req.attributes()["message"] = applicationMapProperties.mapPermitRenewMessage
        req.attributes()["permit"] = permit
        req.attributes()["QaSta3Entity"] = qaDaoServices.findSTA3WithPermitRefNumber(permit.permitRefNumber?: throw Exception("INVALID PERMIT REF NUMBER"))
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
        req.attributes()["QaSta10Entity"] = qaDaoServices.findSTA10WithPermitRefNumberBY(permit.permitRefNumber?: throw ExpectedDataNotFound("INVALID PERMIT REF NUMBER"))
        return ok().render(qaNewSta10Page, req.attributes())

    }



    @PreAuthorize("hasAuthority('QA_MANAGER_ASSESSORS_MODIFY') or hasAuthority('QA_OFFICER_MODIFY')")
    fun newSta10Officer(req: ServerRequest): ServerResponse {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val permitID = req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
        val permit = qaDaoServices.findPermitBYID(permitID)
        req.attributes()["permit"] = permit
        req.attributes()["counties"] = countyRepo.findByStatusOrderByCounty(map.activeStatus)
        req.attributes()["applicationDate"] = commonDaoServices.getCurrentDate()
        req.attributes()["QaSta10Entity"] = qaDaoServices.findSTA10WithPermitRefNumberBY(permit.permitRefNumber?: throw ExpectedDataNotFound("INVALID PERMIT REF NUMBER"))
        return ok().render(qaNewSta10OfficerPage, req.attributes())

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_HOD_READ') or hasAuthority('QA_MANAGER_ASSESSORS_READ') or hasAuthority('QA_HOF_READ') or hasAuthority('QA_ASSESSORS_READ') or hasAuthority('QA_PSC_MEMBERS_READ') or hasAuthority('QA_PCM_READ')")
    fun viewSta3(req: ServerRequest): ServerResponse {
//        val permitUserId = req.paramOrNull("userID")?.toLong() ?: throw ExpectedDataNotFound("Required User ID, check config")

        val permitID = req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
      val permit  =   qaDaoServices.findPermitBYID(permitID)
        req.attributes()["permit"] =  permit
        req.attributes()["QaSta3Entity"] = qaDaoServices.findSTA3WithPermitRefNumber(permit.permitRefNumber?: throw ExpectedDataNotFound("INVALID PERMIT REF NUMBER"))
        req.attributes()["message"] = applicationMapProperties.mapQualityAssuranceManufactureViewPage
        return ok().render(qaNewSta3Page, req.attributes())

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_HOD_READ') or hasAuthority('QA_MANAGER_ASSESSORS_READ') or hasAuthority('QA_HOF_READ') or hasAuthority('QA_OFFICER_MODIFY') or hasAuthority('QA_PSC_MEMBERS_READ') or hasAuthority('QA_PCM_READ')")
    fun viewSta10Officer(req: ServerRequest): ServerResponse {
//        val permitUserId = req.paramOrNull("userID")?.toLong() ?: throw ExpectedDataNotFound("Required User ID, check config")

        val permitID = req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
        val permit = qaDaoServices.findPermitBYID(permitID)
        req.attributes()["permit"] = permit
        req.attributes()["QaSta10Entity"] = qaDaoServices.findSTA10WithPermitRefNumberBY(permit.permitRefNumber?: throw ExpectedDataNotFound("INVALID PERMIT REF NUMBER"))
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

        val permitID = req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
        val permit = qaDaoServices.findPermitBYID(permitID)
        val qaSta10Entity = qaDaoServices.findSTA10WithPermitRefNumberBY(permit.permitRefNumber?: throw ExpectedDataNotFound("INVALID PERMIT REF NUMBER"))
        req.attributes().putAll(commonDaoServices.loadCommonUIComponents(map))
        req.attributes()["permit"] = permit
        req.attributes()["fileParameters"] = qaDaoServices.findAllUploadedFileBYPermitRefNumberAndSta10Status(permit.permitRefNumber?: throw ExpectedDataNotFound("INVALID PERMIT REF NUMBER"), map.activeStatus)
        req.attributes()["plantAttached"] = permit.attachedPlantId?.let { qaDaoServices.findPlantDetails(it) }
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
        req.attributes()["schemeFound"] = qaDaoServices.findSchemeOfSupervisionWithPermitRefNumberBY(permit.permitRefNumber?: throw ExpectedDataNotFound("INVALID PERMIT REF NUMBER"))
        req.attributes()["ksApplicable"] = sampleStandardsRepository.findBySubCategoryId(permit.productSubCategory)
        req.attributes()["permitDetails"] = permit

        return ok().render(qaSchemeDetailPage, req.attributes())

    }

    @PreAuthorize("hasAuthority('QA_OFFICER_MODIFY') or hasAuthority('QA_HOD_READ')")
    fun generateProductQualityStatus(req: ServerRequest): ServerResponse {
        val permitID = req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
        val permit = qaDaoServices.findPermitBYID(permitID)

//        req.attributes()["product"] = productsRepo.findByIdOrNull(permit.product)
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
        req.attributes()["QaSchemeForSupervisionEntity"] = qaDaoServices.findSchemeOfSupervisionWithPermitRefNumberBY(permit.permitRefNumber?: throw ExpectedDataNotFound("INVALID PERMIT REF NUMBER"))
        req.attributes()["permitDetails"] = permit

        return ok().render(qaNewSchemeDetailPage, req.attributes())

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_HOD_READ') or hasAuthority('QA_MANAGER_ASSESSORS_READ') or hasAuthority('QA_HOF_READ') or hasAuthority('QA_OFFICER_MODIFY') or hasAuthority('QA_PSC_MEMBERS_READ') or hasAuthority('QA_PCM_READ')")
    fun getInvoiceDetails(req: ServerRequest): ServerResponse {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)

        val permitID = req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
        val permit = qaDaoServices.findPermitBYID(permitID)
        //Todo: Remove smart cast
        val invoiceDetails = permit.userId?.let { qaDaoServices.findPermitInvoiceByPermitRefNumber(permit.permitRefNumber?: throw ExpectedDataNotFound("INVALID PERMIT REF NUMBER"), it) }
        val applicationState: String = when {
            permit.status != 10 -> {
                "Application"
            }
            else -> {
                "Renewal"
            }
        }


        req.attributes()["invoice"] = invoiceDetails
        req.attributes()["phoneNumber"] = permit.telephoneNo
        req.attributes()["applicationState"] = applicationState
        req.attributes()["permit"] = qaDaoServices.permitDetails(permit, map)
        req.attributes()["product"] = permit.product?.let { commonDaoServices.findProductByID(it) }
        req.attributes()["permitType"] = permit.permitType?.let { qaDaoServices.findPermitType(it) }
        req.attributes()["mpesa"] = CdLaboratoryParametersEntity()
        req.attributes()["paymentMethods"] = paymentMethodsRepository.findAll()
        req.attributes()["appId"] = appId
        req.attributes().putAll(loadCommonUIComponents(map))

        return ok().render(qaInvoiceGenerated, req.attributes())

    }


    @PreAuthorize("hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_HOD_READ') or hasAuthority('QA_MANAGER_ASSESSORS_READ') or hasAuthority('QA_HOF_READ') or hasAuthority('QA_OFFICER_MODIFY') or hasAuthority('QA_PSC_MEMBERS_READ') or hasAuthority('QA_PCM_READ')")
    fun getSSfDetails(req: ServerRequest): ServerResponse {
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        val permitID = req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
        val permit = qaDaoServices.findPermitBYID(permitID)
        val ssfDetails = qaDaoServices.findSampleSubmittedBYPermitRefNumber(permit.permitRefNumber?: throw ExpectedDataNotFound("INVALID PERMIT REF NUMBER"))

        req.attributes()["ssfDetails"] = ssfDetails
        val labResultsParameters = qaDaoServices.findSampleLabTestResultsRepoBYBSNumber(ssfDetails.bsNumber ?: throw ExpectedDataNotFound("MISSING BS NUMBER"))
        KotlinLogging.logger { }.info { ssfDetails.bsNumber }
        req.attributes()["LabResultsParameters"] = labResultsParameters

        return ok().render(qaSSFDetailesPage, req.attributes())

    }


    @PreAuthorize(" hasAuthority('QA_OFFICER_READ')")
    fun checkLabResults(req: ServerRequest): ServerResponse {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        val bsNumber = req.paramOrNull("bsNumber") ?: throw ExpectedDataNotFound("Required Permit ID, check config")
        val ssfDetails = qaDaoServices.findSampleSubmittedBYBsNumber(bsNumber)
        limsServices.updateLabResultsWithDetails(bsNumber, map.inactiveStatus)

//        val permit = qaDaoServices.findPermitBYID(permitID)

        req.attributes()["ssfDetails"] = ssfDetails
        val labResultsParameters = qaDaoServices.findSampleLabTestResultsRepoBYBSNumber(
            ssfDetails.bsNumber ?: throw ExpectedDataNotFound("MISSING BS NUMBER")
        )
        KotlinLogging.logger { }.info { ssfDetails.bsNumber }
        req.attributes()["LabResultsParameters"] = labResultsParameters

        return ok().render(qaSSFDetailesPage, req.attributes())
    }

/*:::::::::::::::::::::::::::::::::::::::::::::START OF WORKPLAN(SURVEILLANCE) FUNCTIONS:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

    @PreAuthorize("hasAuthority('QA_HOD_READ') or hasAuthority('QA_MANAGER_ASSESSORS_READ') or hasAuthority('QA_HOF_READ') or hasAuthority('QA_OFFICER_MODIFY') or hasAuthority('QA_PSC_MEMBERS_READ') or hasAuthority('QA_PCM_READ')")
    fun inspectionReportDetails(req: ServerRequest): ServerResponse {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        val permitID =
            req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
        val permit = qaDaoServices.findPermitBYID(permitID)
        val inspectionReportRecommendation = qaDaoServices.findQaInspectionReportRecommendationBYPermitRefNumber(permit.permitRefNumber?: throw ExpectedDataNotFound("INVALID PERMIT REF NUMBER"))

        req.attributes()["OPCDetailsList"] = qaDaoServices.findAllQaInspectionOPCWithPermitRefNumber(permit.permitRefNumber?: throw ExpectedDataNotFound("INVALID PERMIT REF NUMBER"))
        req.attributes()["QaInspectionTechnicalEntity"] = qaDaoServices.findQaInspectionTechnicalBYPermitRefNumber(permit.permitRefNumber?: throw ExpectedDataNotFound("INVALID PERMIT REF NUMBER"))
        req.attributes()["QaInspectionReportRecommendationEntity"] = inspectionReportRecommendation

        when (inspectionReportRecommendation.filledOpcStatus) {
            map.activeStatus -> {
                req.attributes()["OPCDetailsList"] = qaDaoServices.findQaInspectionOpcBYPermitRefNumber(permit.permitRefNumber?: throw ExpectedDataNotFound("INVALID PERMIT REF NUMBER"))
            }
        }

        when (inspectionReportRecommendation.filledHaccpImplementationStatus) {
            map.activeStatus -> {
                req.attributes()["QaInspectionHaccpImplementationEntity"] =
                    qaDaoServices.findQaInspectionHaccpImplementationBYPermitRefNumber(permit.permitRefNumber?: throw ExpectedDataNotFound("INVALID PERMIT REF NUMBER"))
            }
            else -> {
                req.attributes()["QaInspectionHaccpImplementationEntity"] = QaInspectionHaccpImplementationEntity()
            }
        }

        when (inspectionReportRecommendation.submittedInspectionReportStatus) {
            map.activeStatus -> {
                req.attributes()["message"] = applicationMapProperties.mapQualityAssuranceManufactureViewPage
            }
        }

        req.attributes().putAll(loadCommonUIComponents(map))
        req.attributes()["fileParameters"] = qaDaoServices.findAllUploadedFileBYPermitRefNumberAndInspectionReportStatus(permit.permitRefNumber?: throw ExpectedDataNotFound("INVALID PERMIT REF NUMBER"), map.activeStatus)
        req.attributes()["QaInspectionOpcEntity"] = QaInspectionOpcEntity()
        req.attributes()["permit"] = qaDaoServices.permitDetails(permit, map)
        return ok().render(qaInspectionReportPage, req.attributes())
    }

    @PreAuthorize(" hasAuthority('QA_OFFICER_READ')")
    fun newInspectionReport(req: ServerRequest): ServerResponse {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        val permitID = req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
        val permit = qaDaoServices.findPermitBYID(permitID)

        req.attributes()["OPCDetailsList"] = qaDaoServices.findAllQaInspectionOPCWithPermitRefNumber(permit.permitRefNumber?: throw ExpectedDataNotFound("INVALID PERMIT REF NUMBER"))
        req.attributes()["QaInspectionHaccpImplementationEntity"] = QaInspectionHaccpImplementationEntity()
        req.attributes()["QaInspectionReportRecommendationEntity"] = QaInspectionReportRecommendationEntity()
        req.attributes()["QaInspectionOpcEntity"] = QaInspectionOpcEntity()
        req.attributes()["QaInspectionTechnicalEntity"] = QaInspectionTechnicalEntity()
        req.attributes()["permit"] = qaDaoServices.permitDetails(permit, map)
        return ok().render(qaInspectionReportPage, req.attributes())
    }


    @PreAuthorize(" hasAuthority('QA_OFFICER_READ')")
    fun allWorkPlanList(req: ServerRequest): ServerResponse {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)

        req.attributes()["allWorkPlanCreated"] = qaDaoServices.listWorkPlan(
            qaDaoServices.findALlCreatedWorkPlanWIthOfficerID(
                loggedInUser.id ?: throw Exception("INVALID USER ID")
            ), map
        )
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
        req.attributes()["permit"] = qaDaoServices.permitDetails(permit, map)

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

    private fun loadCommonUIComponents(s: ServiceMapsEntity): MutableMap<String, Any?> {
        return mutableMapOf(
            Pair("activeStatus", s.activeStatus),
            Pair("inActiveStatus", s.inactiveStatus),
            Pair("initStatus", s.initStatus),
            Pair("nullStatus", null),
            Pair("permitRequest", PermitUpdateDetailsRequestsEntity()),
            Pair("requestDetails", PermitUpdateDetailsRequestsEntity()),
            Pair("fmarkEntityDto", FmarkEntityDto()),
            Pair("SampleSubmissionDetails", QaSampleSubmissionEntity()),
            Pair("fmarkPermit", applicationMapProperties.mapQAPermitTypeIdFmark),
            Pair("dmarkPermit", applicationMapProperties.mapQAPermitTypeIDDmark),
            Pair("smarkPermit", applicationMapProperties.mapQAPermitTypeIdSmark),
            Pair("invoiceType", applicationMapProperties.mapQAPermitTypeIdInvoices),
            Pair("currentDate", commonDaoServices.getCurrentDate())
        )
    }

    private fun loadCommonPermitComponents(s: ServiceMapsEntity, permit: PermitApplicationsEntity): MutableMap<String, Any?> {
        return mutableMapOf(

            Pair(
                "oldVersionList",
                qaDaoServices.findAllOldPermitWithPermitRefNumber(
                    permit.permitRefNumber ?: throw Exception("INVALID PERMIT REF NUMBER")
                )
            ),
            Pair(
                "permitType",
                qaDaoServices.findPermitType(permit.permitType ?: throw Exception("INVALID PERMIT TYPE ID"))
            ),
            Pair("fileParameters", qaDaoServices.findAllUploadedFileBYPermitRefNumberAndOrdinarStatus(permit.permitRefNumber?: throw ExpectedDataNotFound("INVALID PERMIT REF NUMBER"), s.activeStatus)
            ),
            Pair(
                "cocParameters",
                qaDaoServices.findAllUploadedFileBYPermitRefNumberAndCocStatus(
                    permit.permitRefNumber?: throw ExpectedDataNotFound("INVALID PERMIT REF NUMBER"), s.activeStatus
                )
            ),
            Pair(
                "assessmentReportParameters",
                qaDaoServices.findAllUploadedFileBYPermitRefNumberAndAssessmentReportStatus(
                    permit.permitRefNumber?: throw ExpectedDataNotFound("INVALID PERMIT REF NUMBER"), s.activeStatus
                )
            ),
            Pair(
                "sscParameters",
                qaDaoServices.findAllUploadedFileBYPermitRefNumberAndSscStatus(
                    permit.permitRefNumber?: throw ExpectedDataNotFound("INVALID PERMIT REF NUMBER"), s.activeStatus
                )
            ),
//            Pair("statusName", qaDaoServices.findPermitStatus(permit.permitStatus?:throw Exception("INVALID PERMIT STATUS VALUE"))),
            Pair(
                "myRequests",
                qaDaoServices.findAllRequestByPermitRefNumber(permit.permitRefNumber?: throw ExpectedDataNotFound("INVALID PERMIT REF NUMBER"),)
            ),
            Pair("userRequestTypes", qaDaoServices.findAllQaRequestTypes(s.activeStatus)),
            Pair("permitDetails", permit),

            Pair(
                "officers",
                permit.attachedPlantId?.let {
                    qaDaoServices.findOfficersList(
                        it,
                        permit,
                        s,
                        applicationMapProperties.mapQADesignationIDForQAOId
                    )
                }),
            Pair(
                "assessorAssigned",
                permit.attachedPlantId?.let {
                    qaDaoServices.findOfficersList(
                        it,
                        permit,
                        s,
                        applicationMapProperties.mapQADesignationIDForAssessorId
                    )
                })
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


    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::QA ANGULAR MIGRATION:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/


    @PreAuthorize(
        "hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_OFFICER_READ') or hasAuthority('QA_HOD_READ') or hasAuthority('QA_MANAGER_ASSESSORS_READ')" +
                " or hasAuthority('QA_HOF_READ') or hasAuthority('QA_ASSESSORS_READ') or hasAuthority('QA_PAC_SECRETARY_READ') or hasAuthority('QA_PSC_MEMBERS_READ') or hasAuthority('QA_PCM_READ')"
    )
    fun sectionListMigration(req: ServerRequest): ServerResponse {
        try {
            val map = commonDaoServices.serviceMapDetails(appId)
            val departmentEntity = commonDaoServices.findDepartmentByID(applicationMapProperties.mapQADepertmentId)

            commonDaoServices.mapAllSectionsTogether(loadSectionDetails(departmentEntity, map, req)).let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }

    }

    @PreAuthorize(
        "hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_OFFICER_READ') or hasAuthority('QA_HOD_READ') or hasAuthority('QA_MANAGER_ASSESSORS_READ')" +
                " or hasAuthority('QA_HOF_READ') or hasAuthority('QA_ASSESSORS_READ') or hasAuthority('QA_PAC_SECRETARY_READ') or hasAuthority('QA_PSC_MEMBERS_READ') or hasAuthority('QA_PCM_READ')"
    )
    fun standardsListMigration(req: ServerRequest): ServerResponse {
        try {
            val map = commonDaoServices.serviceMapDetails(appId)

            qaDaoServices.mapAllStandardsTogether(qaDaoServices.findALlStandardsDetails(map.activeStatus)).let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }

    }

    @PreAuthorize(
        "hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_OFFICER_READ') or hasAuthority('QA_HOD_READ') or hasAuthority('QA_MANAGER_ASSESSORS_READ')" +
                " or hasAuthority('QA_HOF_READ') or hasAuthority('QA_ASSESSORS_READ') or hasAuthority('QA_PAC_SECRETARY_READ') or hasAuthority('QA_PSC_MEMBERS_READ') or hasAuthority('QA_PCM_READ')"
    )
    fun permitListMigration(req: ServerRequest): ServerResponse {
        try {
            val auth = commonDaoServices.loggedInUserAuthentication()
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val map = commonDaoServices.serviceMapDetails(appId)
            val permitTypeID = req.paramOrNull("permitTypeID")?.toLong()
                ?: throw ExpectedDataNotFound("Required PermitType ID, check config")
            val permitType = qaDaoServices.findPermitType(permitTypeID)

            var permitListAllApplications: List<PermitEntityDto>? = null
            when {
                auth.authorities.stream().anyMatch { authority -> authority.authority == "PERMIT_APPLICATION" } -> {
                    permitListAllApplications = qaDaoServices.listPermits(
                        qaDaoServices.findAllUserPermitWithPermitType(
                            loggedInUser,
                            permitTypeID
                        ), map
                    )
                }
                else -> {
                    throw ExpectedDataNotFound("UNAUTHORISED LOGGED IN USER (ACCESS DENIED)")
                }
            }

            return ok().body(permitListAllApplications)

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    fun permitApplySTA1Migration(req: ServerRequest): ServerResponse {
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val map = commonDaoServices.serviceMapDetails(appId)
            val auth = commonDaoServices.loggedInUserAuthentication()
            val permitTypeID = req.paramOrNull("permitTypeID")?.toLong()
                ?: throw ExpectedDataNotFound("Required PermitType ID, check config")
            val permitType = qaDaoServices.findPermitType(permitTypeID)
            val dto = req.body<STA1Dto>()
            val permit = PermitApplicationsEntity()
            with(permit) {
                commodityDescription = dto.commodityDescription
                tradeMark = dto.tradeMark
                applicantName = dto.applicantName
                sectionId = dto.sectionId
                permitForeignStatus = dto.permitForeignStatus
                attachedPlantId = when {
                    auth.authorities.stream().anyMatch { authority -> authority.authority == "MODIFY_COMPANY" } -> {
                        dto.attachedPlant
                    }
                    else -> {
                        loggedInUser.plantId
                    }
                }
            }

            val createdPermit = qaDaoServices.permitSave(permit, permitType, loggedInUser, map)

            qaDaoServices.permitDetails(createdPermit.second, map).let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    fun permitSubmitApplicationMigration(req: ServerRequest): ServerResponse {
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val map = commonDaoServices.serviceMapDetails(appId)
            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
            var permit = qaDaoServices.findPermitBYUserIDAndId(
                permitID,
                loggedInUser.id ?: throw ExpectedDataNotFound("MISSING USER ID")
            )
            val permitType = qaDaoServices.findPermitType(
                permit.permitType ?: throw ExpectedDataNotFound("Permit Type Id Not found")
            )

            //Calculate Invoice Details
            qaDaoServices.permitInvoiceCalculation(map, loggedInUser, permit, permitType)

            //Update Permit Details
            with(permit) {
                sendApplication = map.activeStatus
                invoiceGenerated = map.activeStatus
                permitStatus = applicationMapProperties.mapQaStatusPPayment

            }
            permit = qaDaoServices.permitUpdateDetails(permit, map, loggedInUser).second

            qaDaoServices.permitDetails(permit, map).let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    fun permitDetailsMigration(req: ServerRequest): ServerResponse {
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val map = commonDaoServices.serviceMapDetails(appId)
            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
            val permit = qaDaoServices.findPermitBYUserIDAndId(
                permitID,
                loggedInUser.id ?: throw ExpectedDataNotFound("MISSING USER ID")
            )

            qaDaoServices.mapAllPermitDetailsTogether(permit, map).let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    fun permitApplySTA3Migration(req: ServerRequest): ServerResponse {
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val map = commonDaoServices.serviceMapDetails(appId)
            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
            val permit = qaDaoServices.findPermitBYUserIDAndId(
                permitID,
                loggedInUser.id ?: throw ExpectedDataNotFound("MISSING USER ID")
            )
            val dto = req.body<STA3Dto>()
            val sta3 = qaDaoServices.mapDtoSTA3AndQaSta3Entity(dto)

            //Save the sta3 details first
            qaDaoServices.sta3NewSave(
                permit.permitRefNumber ?: throw Exception("MISSING PERMIT REF NUMBER"),
                sta3,
                loggedInUser,
                map
            )

            //create an entity that will update the permit transaction to the latest status
            var updatePermit = PermitApplicationsEntity()
            with(updatePermit) {
                id = permit.id
                sta3FilledStatus = map.activeStatus
                permitStatus = applicationMapProperties.mapQaStatusPSubmission
            }

            //update the permit with the created entity values
            updatePermit = qaDaoServices.permitUpdateDetails(
                commonDaoServices.updateDetails(
                    updatePermit,
                    permit
                ) as PermitApplicationsEntity, map, loggedInUser
            ).second

            qaDaoServices.permitDetails(updatePermit, map).let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    fun permitViewSTA3Migration(req: ServerRequest): ServerResponse {
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
            val permit = qaDaoServices.findPermitBYUserIDAndId(
                permitID,
                loggedInUser.id ?: throw ExpectedDataNotFound("MISSING USER ID")
            )
            val sta3 = qaDaoServices.findSTA3WithPermitRefNumber(
                permit.permitRefNumber ?: throw Exception("INVALID PERMIT REF NUMBER")
            )

            qaDaoServices.mapDtoSTA3View(sta3).let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    fun permitViewInvoiceDetailsMigration(req: ServerRequest): ServerResponse {
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val map = commonDaoServices.serviceMapDetails(appId)

            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
            val permit = qaDaoServices.findPermitBYID(permitID)

            val invoiceDetails = qaDaoServices.findPermitInvoiceByPermitRefNumber(
                permit.permitRefNumber ?: throw ExpectedDataNotFound("MISSING PERMIT REF NUMBER"),
                permit.userId ?: throw ExpectedDataNotFound("MISSING USER ID")
            )

            qaDaoServices.permitsInvoiceDTO(invoiceDetails, permit).let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    fun permitApplySTA10FirmDetailsMigration(req: ServerRequest): ServerResponse {
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val map = commonDaoServices.serviceMapDetails(appId)
            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
            val permit = qaDaoServices.findPermitBYUserIDAndId(
                permitID,
                loggedInUser.id ?: throw ExpectedDataNotFound("MISSING USER ID")
            )
            val qaSta10Entity = qaDaoServices.findSTA10WithPermitRefNumberBY(
                permit.permitRefNumber ?: throw ExpectedDataNotFound("INVALID PERMIT REF NUMBER")
            )

            val dto = req.body<STA10SectionADto>()
            val sta10 = qaDaoServices.mapDtoSTA10SectionAAndQaSta10Entity(dto)

            //Save the sta10 details first
            qaDaoServices.sta10NewSave(
                permit.permitRefNumber ?: throw Exception("MISSING PERMIT REF NUMBER"),
                sta10,
                loggedInUser,
                map
            )

            //create an entity that will update the permit transaction to the latest status
            var updatePermit = PermitApplicationsEntity()
            with(updatePermit) {
                id = permit.id
                sta10FilledStatus = map.inactiveStatus
                permitStatus = applicationMapProperties.mapQaStatusPSTA10Completion
            }

            //update the permit with the created entity values
            updatePermit = qaDaoServices.permitUpdateDetails(
                commonDaoServices.updateDetails(
                    updatePermit,
                    permit
                ) as PermitApplicationsEntity, map, loggedInUser
            ).second

            qaDaoServices.permitDetails(updatePermit, map).let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    fun permitViewSTA10FirmDetailsMigration(req: ServerRequest): ServerResponse {
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
            val permit = qaDaoServices.findPermitBYUserIDAndId(
                permitID,
                loggedInUser.id ?: throw ExpectedDataNotFound("MISSING USER ID")
            )
            val qaSta10Entity = qaDaoServices.findSTA10WithPermitRefNumberBY(
                permit.permitRefNumber ?: throw ExpectedDataNotFound("INVALID PERMIT REF NUMBER")
            )

            qaDaoServices.mapDtoSTA10SectionAAndQaSta10View(qaSta10Entity).let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    fun permitApplySTA10ProductsBeingManufacturedMigration(req: ServerRequest): ServerResponse {
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val map = commonDaoServices.serviceMapDetails(appId)
            val qaSta10ID = req.paramOrNull("qaSta10ID")?.toLong()
                ?: throw ExpectedDataNotFound("Required QA Sta10 ID, check config")
            val qaSta10 = qaDaoServices.findSta10BYID(qaSta10ID)
            val dto = req.body<STA10ProductsManufactureDto>()
            val sta10ProductManufacture = qaDaoServices.mapDtoSTA10SectionBAndQaProductManufacturedEntity(dto)

            //Save the sta10 details first
            qaDaoServices.sta10ManufactureProductNewSave(qaSta10ID, sta10ProductManufacture, loggedInUser, map)

            //Find all sta 10 products added
            val sta10Products = qaDaoServices.findProductsManufactureWithSTA10ID(qaSta10ID)
                ?: throw ExpectedDataNotFound("EMPTY RESULTS")

            qaDaoServices.listSTA10Product(sta10Products).let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }

    }


    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    fun permitViewSTA10ProductsBeingManufacturedMigration(req: ServerRequest): ServerResponse {
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val map = commonDaoServices.serviceMapDetails(appId)
            val qaSta10ID = req.paramOrNull("qaSta10ID")?.toLong()
                ?: throw ExpectedDataNotFound("Required QA Sta10 ID, check config")
            val qaSta10 = qaDaoServices.findSta10BYID(qaSta10ID)

            //Find all sta 10 products added
            val sta10Products = qaDaoServices.findProductsManufactureWithSTA10ID(qaSta10ID)
                ?: throw ExpectedDataNotFound("EMPTY RESULTS")

            qaDaoServices.listSTA10Product(sta10Products).let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    fun permitApplySTA10RawMaterialsMigration(req: ServerRequest): ServerResponse {
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val map = commonDaoServices.serviceMapDetails(appId)
            val qaSta10ID = req.paramOrNull("qaSta10ID")?.toLong()
                ?: throw ExpectedDataNotFound("Required QA Sta10 ID, check config")
            val qaSta10 = qaDaoServices.findSta10BYID(qaSta10ID)
            val dto = req.body<STA10RawMaterialsDto>()
            val sta10RawMaterials = qaDaoServices.mapDtoSTA10SectionBAndQaRawMaterialsEntity(dto)

            //Save the sta10 raw materials details first
            qaDaoServices.sta10RawMaterialsNewSave(qaSta10ID, sta10RawMaterials, loggedInUser, map)

            //Find all sta 10 raw materials add
            val sta10Raw =
                qaDaoServices.findRawMaterialsWithSTA10ID(qaSta10ID) ?: throw ExpectedDataNotFound("EMPTY RESULTS")

            qaDaoServices.listSTA10RawMaterials(sta10Raw).let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }

    }


    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    fun permitViewSTA10RawMaterialsMigration(req: ServerRequest): ServerResponse {
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val map = commonDaoServices.serviceMapDetails(appId)
            val qaSta10ID = req.paramOrNull("qaSta10ID")?.toLong()
                ?: throw ExpectedDataNotFound("Required QA Sta10 ID, check config")
            val qaSta10 = qaDaoServices.findSta10BYID(qaSta10ID)

            //Find all sta 10 raw materials add
            val sta10Raw =
                qaDaoServices.findRawMaterialsWithSTA10ID(qaSta10ID) ?: throw ExpectedDataNotFound("EMPTY RESULTS")

            qaDaoServices.listSTA10RawMaterials(sta10Raw).let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    fun permitApplySTA10MachineryAndPlantMigration(req: ServerRequest): ServerResponse {
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val map = commonDaoServices.serviceMapDetails(appId)
            val qaSta10ID = req.paramOrNull("qaSta10ID")?.toLong()
                ?: throw ExpectedDataNotFound("Required QA Sta10 ID, check config")
            val qaSta10 = qaDaoServices.findSta10BYID(qaSta10ID)
            val dto = req.body<STA10MachineryAndPlantDto>()
            val machinePlantsDetails = qaDaoServices.mapDtoSTA10SectionBAndMachineryAndPlantEntity(dto)

            //Save the sta10 Machine plant details first
            qaDaoServices.sta10MachinePlantNewSave(qaSta10ID, machinePlantsDetails, loggedInUser, map)

            //Find all sta 10 Machine plant add
            val sta10MachinePlant =
                qaDaoServices.findMachinePlantsWithSTA10ID(qaSta10ID) ?: throw ExpectedDataNotFound("EMPTY RESULTS")

            qaDaoServices.listSTA10MachinePlants(sta10MachinePlant).let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    fun permitViewSTA10MachineryAndPlantMigration(req: ServerRequest): ServerResponse {
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val map = commonDaoServices.serviceMapDetails(appId)
            val qaSta10ID = req.paramOrNull("qaSta10ID")?.toLong()
                ?: throw ExpectedDataNotFound("Required QA Sta10 ID, check config")
            val qaSta10 = qaDaoServices.findSta10BYID(qaSta10ID)

            //Find all sta 10 Machine plant add
            val sta10MachinePlant =
                qaDaoServices.findMachinePlantsWithSTA10ID(qaSta10ID) ?: throw ExpectedDataNotFound("EMPTY RESULTS")

            qaDaoServices.listSTA10MachinePlants(sta10MachinePlant).let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    fun permitApplySTA10ManufacturingProcessMigration(req: ServerRequest): ServerResponse {
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val map = commonDaoServices.serviceMapDetails(appId)
            val qaSta10ID = req.paramOrNull("qaSta10ID")?.toLong()
                ?: throw ExpectedDataNotFound("Required QA Sta10 ID, check config")
            val qaSta10 = qaDaoServices.findSta10BYID(qaSta10ID)
            val dto = req.body<STA10ManufacturingProcessDto>()
            val manufacturingProcessDetails = qaDaoServices.mapDtoSTA10SectionBAndManufacturingProcessEntity(dto)

            //Save the sta10 Manufacturing Process details first
            qaDaoServices.sta10ManufacturingProcessNewSave(qaSta10ID, manufacturingProcessDetails, loggedInUser, map)

            //Find all sta 10 Machine plant add
            val sta10ManufacturingProcess = qaDaoServices.findManufacturingProcessesWithSTA10ID(qaSta10ID)
                ?: throw ExpectedDataNotFound("EMPTY RESULTS")

            qaDaoServices.listSTA10ManufacturingProcess(sta10ManufacturingProcess).let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    fun permitViewSTA10ManufacturingProcessMigration(req: ServerRequest): ServerResponse {
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val map = commonDaoServices.serviceMapDetails(appId)
            val qaSta10ID = req.paramOrNull("qaSta10ID")?.toLong()
                ?: throw ExpectedDataNotFound("Required QA Sta10 ID, check config")
            val qaSta10 = qaDaoServices.findSta10BYID(qaSta10ID)

            //Find all sta 10 Machine plant add
            val sta10ManufacturingProcess = qaDaoServices.findManufacturingProcessesWithSTA10ID(qaSta10ID)
                ?: throw ExpectedDataNotFound("EMPTY RESULTS")

            qaDaoServices.listSTA10ManufacturingProcess(sta10ManufacturingProcess).let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }

    }


    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    fun invoiceListMigration(req: ServerRequest): ServerResponse {
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val map = commonDaoServices.serviceMapDetails(appId)
            val invoiceList =
                qaDaoServices.findALlInvoicesCreatedByUser(loggedInUser.id ?: throw Exception("INVALID USER ID"))

            qaDaoServices.listPermitsInvoices(invoiceList, null, map).let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    fun invoiceListNoBatchIDMigration(req: ServerRequest): ServerResponse {
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val map = commonDaoServices.serviceMapDetails(appId)
            val invoiceList = qaDaoServices.findALlPermitInvoicesCreatedByUserWithNoPaymentStatus(
                loggedInUser.id ?: throw Exception("INVALID USER ID"), map.inactiveStatus
            )

            qaDaoServices.listPermitsInvoices(invoiceList, null, map).let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    fun invoiceBatchListMigration(req: ServerRequest): ServerResponse {
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val batchInvoiceList =
                qaDaoServices.findALlBatchInvoicesWithUserID(loggedInUser.id ?: throw Exception("INVALID USER ID"))

            qaDaoServices.mapBatchInvoiceList(batchInvoiceList).let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    fun invoiceBatchSubmitMigration(req: ServerRequest): ServerResponse {
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val map = commonDaoServices.serviceMapDetails(appId)
            val dto = req.body<NewBatchInvoiceDto>()

            val batchInvoiceDetails = qaDaoServices.permitMultipleInvoiceSubmitInvoice(map, loggedInUser, dto).second

            qaDaoServices.mapBatchInvoiceDetails(batchInvoiceDetails, loggedInUser, map).let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    fun invoiceBatchRemoveMigration(req: ServerRequest): ServerResponse {
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val map = commonDaoServices.serviceMapDetails(appId)
            val dto = req.body<NewBatchInvoiceDto>()

            val batchInvoiceDetails = qaDaoServices.permitMultipleInvoiceRemoveInvoice(map, loggedInUser, dto).second

            qaDaoServices.mapBatchInvoiceDetails(batchInvoiceDetails, loggedInUser, map).let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    fun invoiceBatchAddMigration(req: ServerRequest): ServerResponse {
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val map = commonDaoServices.serviceMapDetails(appId)
            val dto = req.body<NewBatchInvoiceDto>()

            val batchInvoiceDetails = qaDaoServices.permitMultipleInvoiceCalculation(map, loggedInUser, dto).second

            qaDaoServices.mapBatchInvoiceDetails(batchInvoiceDetails, loggedInUser, map).let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }

    }
}