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

//import org.kebs.app.kotlin.apollo.api.ports.provided.createUserAlert

import mu.KotlinLogging
import org.jasypt.encryption.StringEncryptor
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.QualityAssuranceBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DestinationInspectionDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.InvoiceDaoService
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.QADaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.lims.LimsServices
import org.kebs.app.kotlin.apollo.common.dto.FmarkEntityDto
import org.kebs.app.kotlin.apollo.common.dto.MPesaMessageDto
import org.kebs.app.kotlin.apollo.common.dto.MPesaPushDto
import org.kebs.app.kotlin.apollo.common.dto.qa.*
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.di.*
import org.kebs.app.kotlin.apollo.store.model.qa.*
import org.kebs.app.kotlin.apollo.store.repo.*
import org.kebs.app.kotlin.apollo.store.repo.di.ILaboratoryRepository
import org.kebs.app.kotlin.apollo.store.repo.qa.IQaBatchInvoiceRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.ServerResponse.badRequest
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
    private val qaDaoServices: QADaoServices,
    private val jasyptStringEncryptor: StringEncryptor,

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
    private val qaSSFListDetailesPage = "quality-assurance/customer/ssf-list"
    private val qaAllWorkPlanCreatedListPage = "quality-assurance/created-workPlan-list.html"
    private val qaInspectionReportPage = "quality-assurance/customer/inspection-report-new-details"
    private val qaInspectionReportListsPage = "quality-assurance/customer/inspection-report-list"
    private val qaReportsPage = "quality-assurance/employees-reports-home"


    //Inspection details
    private val cdSampleCollectPage = "destination-inspection/cd-Inspection-documents/cd-inspection-sample-collect.html"
    private val cdSampleSubmitPage = "destination-inspection/cd-Inspection-documents/cd-inspection-sample-submit.html"

    final val appId: Int = applicationMapProperties.mapQualityAssurance

    fun notSupported(req: ServerRequest): ServerResponse = badRequest().body("Invalid Request: Not supported")

    @PreAuthorize(
        "hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_OFFICER_READ') or hasAuthority('QA_HOD_READ') or hasAuthority('QA_MANAGER_READ') " +
                "or hasAuthority('QA_HOF_READ') or hasAuthority('QA_RM_READ') or hasAuthority('QA_ASSESSORS_READ') or hasAuthority('QA_PAC_SECRETARY_READ') or hasAuthority('QA_PSC_MEMBERS_READ')" +
                " or hasAuthority('QA_PCM_READ') or hasAuthority('QA_DIRECTOR_READ')"
    )
    fun home(req: ServerRequest): ServerResponse {
        try {
            val auth = commonDaoServices.loggedInUserAuthentication()
            val map = commonDaoServices.serviceMapDetails(appId)
            return when {
                auth.authorities.stream()
                    .anyMatch { authority -> authority.authority == applicationMapProperties.mapQualityAssuranceManufactureRoleName } -> {
                    req.attributes()["permitTypes"] = qaDaoServices.findPermitTypesList(map.activeStatus)
                    req.attributes()["map"] = map
                    ok().render(qaCustomerHomePage, req.attributes())
                }

                else -> {
                    req.attributes()["permitFmarkType"] =
                        qaDaoServices.findPermitType(applicationMapProperties.mapQAPermitTypeIdFmark)
                    req.attributes()["permitSmarkType"] =
                        qaDaoServices.findPermitType(applicationMapProperties.mapQAPermitTypeIdSmark)
                    req.attributes()["permitDmarkType"] =
                        qaDaoServices.findPermitType(applicationMapProperties.mapQAPermitTypeIDDmark)
                    req.attributes()["map"] = map
                    ok().render(qaEmployeesHomePage, req.attributes())
                }
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            throw e
        }
    }

    @PreAuthorize(
        " hasAuthority('QA_OFFICER_READ') or hasAuthority('QA_HOD_READ') or hasAuthority('QA_MANAGER_READ') " +
                "or hasAuthority('QA_HOF_READ') or hasAuthority('QA_RM_READ') or hasAuthority('QA_ASSESSORS_READ') or hasAuthority('QA_PAC_SECRETARY_READ') or hasAuthority('QA_PSC_MEMBERS_READ')" +
                " or hasAuthority('QA_PCM_READ') or hasAuthority('QA_DIRECTOR_READ')"
    )
    fun reports(req: ServerRequest): ServerResponse {
        try {
            val map = commonDaoServices.serviceMapDetails(appId)

            req.attributes()["permitFmarkType"] =
                qaDaoServices.findPermitType(applicationMapProperties.mapQAPermitTypeIdFmark)
            req.attributes()["permitSmarkType"] =
                qaDaoServices.findPermitType(applicationMapProperties.mapQAPermitTypeIdSmark)
            req.attributes()["permitDmarkType"] =
                qaDaoServices.findPermitType(applicationMapProperties.mapQAPermitTypeIDDmark)
            req.attributes()["map"] = map
            return ok().render(qaReportsPage, req.attributes())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            throw e
        }
    }

    @PreAuthorize(
        "hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_OFFICER_READ') or hasAuthority('QA_HOD_READ') or hasAuthority('QA_MANAGER_READ')" +
                " or hasAuthority('QA_HOF_READ') or hasAuthority('QA_RM_READ') or hasAuthority('QA_ASSESSORS_READ') or hasAuthority('QA_PAC_SECRETARY_READ') or " +
                "hasAuthority('QA_PSC_MEMBERS_READ') or hasAuthority('QA_PCM_READ') or hasAuthority('QA_DIRECTOR_READ')"
    )
    fun permitList(req: ServerRequest): ServerResponse {
        try {
            val auth = commonDaoServices.loggedInUserAuthentication()
            val loggedInUser = commonDaoServices.loggedInUserDetails()

            val map = commonDaoServices.serviceMapDetails(appId)
//            var viewPage = qaPermitListPage
            val permitTypeID = req.paramOrNull("permitTypeID")?.toLong()
                ?: throw ExpectedDataNotFound("Required PermitType ID, check config")
            val permitType = qaDaoServices.findPermitType(permitTypeID)
            req.attributes().putAll(loadCommonUIComponents(map))
            req.attributes()["permitType"] = permitType

            when (permitType.id) {
                applicationMapProperties.mapQAPermitTypeIdFmark -> {
                    req.attributes()["mySmarkPermits"] = qaDaoServices.findAllSmarkPermitWithNoFmarkGenerated(
                        loggedInUser,
                        applicationMapProperties.mapQAPermitTypeIdSmark,
                        map.activeStatus,
                        map.inactiveStatus
                    )
                }

                applicationMapProperties.mapQAPermitTypeIdInvoices -> {
//                    val plantsDetails = qaDaoServices.findAllPlantDetails(loggedInUser.id?:throw Exception("INVALID USER ID"))
                    val allUnpaidInvoices = qaDaoServices.findALlPermitInvoicesCreatedByUserWithNoPaymentStatus(
                        loggedInUser.id ?: throw Exception("INVALID USER ID"), map.inactiveStatus
                    )

                    req.attributes()["NewBatchInvoiceDto"] = NewBatchInvoiceDto()
//                    req.attributes()["plantsDetails"] = plantsDetails
                    req.attributes()["allPermitInvoiceList"] =
                        qaDaoServices.listPermitsInvoices(allUnpaidInvoices, null, map)
                    req.attributes()["allBatchPermitInvoiceList"] = qaDaoServices.findALlBatchInvoicesWithUserID(
                        loggedInUser.id ?: throw Exception("INVALID USER ID")
                    )
                    return ok().render(qaPermitInvoiceListPage, req.attributes())
//                    viewPage = qaDaoServices.batchInvoiceList
                }
            }

            var permitListMyTasksAddedTogether = mutableListOf<PermitEntityDto>()
            var permitListAllApplicationsAddedTogether = mutableListOf<PermitEntityDto>()
            var permitListAllCompleteAddedTogether = mutableListOf<PermitEntityDto>()


            //Get logged in user Task required there attention
            permitListMyTasksAddedTogether =
                findLoggedInUserTask(auth, loggedInUser, permitTypeID, map, permitListMyTasksAddedTogether)

            //All permit applications And Awarded List
            val results = loggedInUserAwardedPermitsAndApplicationPermits(
                auth,
                loggedInUser,
                permitTypeID,
                map,
                permitListAllApplicationsAddedTogether,
                permitListAllCompleteAddedTogether
            )

            permitListAllApplicationsAddedTogether = results.first

            permitListAllCompleteAddedTogether = results.second

            req.attributes()["permitListAllApplications"] = permitListAllApplicationsAddedTogether.distinct()
            req.attributes()["permitListAllComplete"] = permitListAllCompleteAddedTogether.distinct()
            req.attributes()["permitListMyTasks"] = permitListMyTasksAddedTogether.distinct()
            return ok().render(qaPermitListPage, req.attributes())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            throw e
        }

    }

    fun loggedInUserAwardedPermitsAndApplicationPermits(
        auth: Authentication,
        loggedInUser: UsersEntity,
        permitTypeID: Long,
        map: ServiceMapsEntity,
        permitListAllApplicationsAddedTogether: MutableList<PermitEntityDto>,
        permitListAllCompleteAddedTogether: MutableList<PermitEntityDto>
    ): Pair<MutableList<PermitEntityDto>, MutableList<PermitEntityDto>> {
        println(auth.authorities)
        auth.authorities.forEach { a ->

            if (a.authority == "QA_OFFICER_READ") {
                qaDaoServices.listPermits(
                    qaDaoServices.findAllQAOPermitListWithPermitType(
                        loggedInUser,
                        permitTypeID
                    ), map
                ).let { permitListAllApplicationsAddedTogether.addAll(it) }

                qaDaoServices.listPermits(
                    qaDaoServices.findAllQAOPermitListWithPermitTypeAwardedStatusIsNotNull(
                        loggedInUser,
                        permitTypeID
                    ), map
                ).let { permitListAllCompleteAddedTogether.addAll(it) }
            }
            if (a.authority == "QA_DIRECTOR_READ") {
                qaDaoServices.listPermits(
                    qaDaoServices.findAllPermitListWithPermitType(
                        permitTypeID
                    ), map
                ).let { permitListAllApplicationsAddedTogether.addAll(it) }

                qaDaoServices.listPermits(
                    qaDaoServices.findAllPermitListWithPermitTypeAwardedStatusIsNotNull(
                        permitTypeID
                    ), map
                ).let { permitListAllCompleteAddedTogether.addAll(it) }
            }

            if (a.authority == "QA_ASSESSORS_READ") {
                qaDaoServices.listPermits(
                    qaDaoServices.findAllAssessorPermitListWithPermitType(
                        loggedInUser,
                        permitTypeID
                    ), map
                ).let { permitListAllApplicationsAddedTogether.addAll(it) }

                qaDaoServices.listPermits(
                    qaDaoServices.findAllAssessorPermitListWithPermitTypeAwardedStatusIsNotNull(
                        loggedInUser,
                        permitTypeID
                    ), map
                ).let { permitListAllCompleteAddedTogether.addAll(it) }
            }

            if (a.authority == "QA_HOD_READ") {
                qaDaoServices.findAllApplicationsQAMHODRMHOFByRegion(
                    loggedInUser,
                    auth,
                    "QA_HOD_READ",
                    permitTypeID,
                    map
                )?.let { qaDaoServices.listPermits(it, map) }?.let { permitListAllApplicationsAddedTogether.addAll(it) }

                qaDaoServices.findAllApplicationsAwardedQAMHODRMHOFByRegion(
                    loggedInUser,
                    auth,
                    "QA_HOD_READ",
                    permitTypeID,
                    map
                )?.let { qaDaoServices.listPermits(it, map) }?.let { permitListAllCompleteAddedTogether.addAll(it) }
            }

            if (a.authority == "QA_HOF_READ") {
                qaDaoServices.findAllApplicationsQAMHODRMHOFByRegion(
                    loggedInUser,
                    auth,
                    "QA_HOF_READ",
                    permitTypeID,
                    map
                )?.let { qaDaoServices.listPermits(it, map) }?.let { permitListAllApplicationsAddedTogether.addAll(it) }

                qaDaoServices.findAllApplicationsAwardedQAMHODRMHOFByRegion(
                    loggedInUser,
                    auth,
                    "QA_HOF_READ",
                    permitTypeID,
                    map
                )?.let { qaDaoServices.listPermits(it, map) }?.let { permitListAllCompleteAddedTogether.addAll(it) }
            }

            if (a.authority == "QA_RM_READ") {
                qaDaoServices.findAllApplicationsQAMHODRMHOFByRegion(
                    loggedInUser,
                    auth,
                    "QA_RM_READ",
                    permitTypeID,
                    map
                )?.let { qaDaoServices.listPermits(it, map) }?.let { permitListAllApplicationsAddedTogether.addAll(it) }

                qaDaoServices.findAllApplicationsAwardedQAMHODRMHOFByRegion(
                    loggedInUser,
                    auth,
                    "QA_RM_READ",
                    permitTypeID,
                    map
                )?.let { qaDaoServices.listPermits(it, map) }?.let { permitListAllCompleteAddedTogether.addAll(it) }
            }

            if (a.authority == "QA_MANAGER_READ") {
                qaDaoServices.findAllApplicationsQAMHODRMHOFByRegion(
                    loggedInUser,
                    auth,
                    "QA_MANAGER_READ",
                    permitTypeID,
                    map
                )?.let { qaDaoServices.listPermits(it, map) }?.let { permitListAllApplicationsAddedTogether.addAll(it) }

                qaDaoServices.findAllApplicationsAwardedQAMHODRMHOFByRegion(
                    loggedInUser,
                    auth,
                    "QA_MANAGER_READ",
                    permitTypeID,
                    map
                )?.let { qaDaoServices.listPermits(it, map) }?.let { permitListAllCompleteAddedTogether.addAll(it) }
            }

            if (a.authority == "QA_PAC_SECRETARY_READ" || a.authority == "QA_PSC_MEMBERS_READ" || a.authority == "QA_PCM_READ") {
                qaDaoServices.findAllFirmsInKenyaPermitsApplicationsWithPermitTypeAndPaidStatus(
                    permitTypeID,
                    map.initStatus
                ).let { qaDaoServices.listPermits(it, map) }.let { permitListAllApplicationsAddedTogether.addAll(it) }

                qaDaoServices.findAllFirmInKenyaPermitsAwardedWithPermitType(map.activeStatus, permitTypeID)
                    .let { qaDaoServices.listPermits(it, map) }.let { permitListAllCompleteAddedTogether.addAll(it) }
            }
        }

        return Pair(permitListAllApplicationsAddedTogether, permitListAllCompleteAddedTogether)
    }

    fun findLoggedInUserTask(
        auth: Authentication,
        loggedInUser: UsersEntity,
        permitTypeID: Long,
        map: ServiceMapsEntity,
        permitListMyTasksAddedTogether: MutableList<PermitEntityDto>
    ): MutableList<PermitEntityDto> {
        auth.authorities.forEach { a ->
            //                if (a.authority == "PERMIT_APPLICATION"){
            //                    permitListMyTasks =  qaDaoServices.findAllUserTasksManufactureByTaskIDAndPermitType(loggedInUser, auth,permitTypeID, applicationMapProperties.mapUserTaskNameMANUFACTURE)?.let { qaDaoServices.listPermits(it, map) }
            //                    permitListMyTasksAddedTogether.addAll(permitListMyTasks)
            //                    permitListAllApplications = qaDaoServices.findAllFirmPermitsAwardedWithPermitType(loggedInUser.companyId ?: throw Exception("MISSING COMPANY ID"),  map.activeStatus, permitTypeID).let { qaDaoServices.listPermits(it, map) }
            //                }

            if (a.authority == "QA_OFFICER_READ") {
                qaDaoServices.listPermits(
                    qaDaoServices.findAllQAOPermitListWithPermitTypeTaskID(
                        loggedInUser,
                        permitTypeID,
                        applicationMapProperties.mapUserTaskNameQAO
                    ), map
                ).let { permitListMyTasksAddedTogether.addAll(it) }
            }

            if (a.authority == "QA_ASSESSORS_READ") {
                qaDaoServices.listPermits(
                    qaDaoServices.findAllAssessorPermitListWithPermitTypeTaskID(
                        loggedInUser,
                        permitTypeID,
                        applicationMapProperties.mapUserTaskNameASSESSORS
                    ), map
                ).let { permitListMyTasksAddedTogether.addAll(it) }
            }

            if (a.authority == "QA_HOD_READ") {
                qaDaoServices.findAllUserTasksQAMHODRMHOFByTaskID(
                    loggedInUser,
                    auth,
                    "QA_HOD_READ",
                    permitTypeID,
                    map,
                    applicationMapProperties.mapUserTaskNameHOD
                )?.let { qaDaoServices.listPermits(it, map) }?.let { permitListMyTasksAddedTogether.addAll(it) }
            }

            if (a.authority == "QA_HOF_READ") {
                qaDaoServices.findAllUserTasksQAMHODRMHOFByTaskID(
                    loggedInUser,
                    auth,
                    "QA_HOF_READ",
                    permitTypeID,
                    map,
                    applicationMapProperties.mapUserTaskNameHOF
                )?.let { qaDaoServices.listPermits(it, map) }?.let { permitListMyTasksAddedTogether.addAll(it) }
            }

            if (a.authority == "QA_RM_READ") {
                qaDaoServices.findAllUserTasksQAMHODRMHOFByTaskID(
                    loggedInUser,
                    auth,
                    "QA_RM_READ",
                    permitTypeID,
                    map,
                    applicationMapProperties.mapUserTaskNameRM
                )?.let { qaDaoServices.listPermits(it, map) }?.let { permitListMyTasksAddedTogether.addAll(it) }
            }

            if (a.authority == "QA_MANAGER_READ") {
                qaDaoServices.findAllUserTasksQAMHODRMHOFByTaskID(
                    loggedInUser,
                    auth,
                    "QA_MANAGER_READ",
                    permitTypeID,
                    map,
                    applicationMapProperties.mapUserTaskNameQAM
                )?.let { qaDaoServices.listPermits(it, map) }?.let { permitListMyTasksAddedTogether.addAll(it) }
            }

            if (a.authority == "QA_PAC_SECRETARY_READ") {
                qaDaoServices.findAllUserTasksPACPSCPCMByTaskID(
                    loggedInUser,
                    auth,
                    "QA_PAC_SECRETARY_READ",
                    permitTypeID,
                    applicationMapProperties.mapUserTaskNamePACSECRETARY
                )?.let { qaDaoServices.listPermits(it, map) }?.let { permitListMyTasksAddedTogether.addAll(it) }
            }

            if (a.authority == "QA_PSC_MEMBERS_READ") {
                qaDaoServices.findAllUserTasksPACPSCPCMByTaskID(
                    loggedInUser,
                    auth,
                    "QA_PSC_MEMBERS_READ",
                    permitTypeID,
                    applicationMapProperties.mapUserTaskNamePSC
                )?.let { qaDaoServices.listPermits(it, map) }?.let { permitListMyTasksAddedTogether.addAll(it) }
            }

            if (a.authority == "QA_PCM_READ") {
                qaDaoServices.findAllUserTasksPACPSCPCMByTaskID(
                    loggedInUser,
                    auth,
                    "QA_PCM_READ",
                    permitTypeID,
                    applicationMapProperties.mapUserTaskNamePCM
                )?.let { qaDaoServices.listPermits(it, map) }?.let { permitListMyTasksAddedTogether.addAll(it) }
            }

        }

        return permitListMyTasksAddedTogether
    }

    @PreAuthorize(
        "hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_OFFICER_READ') or hasAuthority('QA_HOD_READ') " +
                "or hasAuthority('QA_MANAGER_READ') or hasAuthority('QA_HOF_READ') or hasAuthority('QA_RM_READ') or hasAuthority('QA_ASSESSORS_READ') or hasAuthority('QA_PAC_SECRETARY_READ') or hasAuthority('QA_PSC_MEMBERS_READ') or hasAuthority('QA_PCM_READ')"
    )
    fun permitDetails(req: ServerRequest): ServerResponse {

        val encryptedPermitId =
            req.paramOrNull("permitID") ?: throw ExpectedDataNotFound("Required Permit ID, check config")

        val permitID = jasyptStringEncryptor.decrypt(encryptedPermitId).toLong()
        val map = commonDaoServices.serviceMapDetails(appId)
        val auth = commonDaoServices.loggedInUserAuthentication()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val permit = qaDaoServices.findPermitBYID(permitID)
        val departmentEntity = commonDaoServices.findDepartmentByID(applicationMapProperties.mapQADepertmentId)


        if (auth.authorities.stream().anyMatch { authority -> authority.authority == "MODIFY_COMPANY" }) {
            req.attributes()["plantsDetails"] = qaDaoServices.findAllPlantDetailsWithCompanyID(
                loggedInUser.companyId ?: throw ExpectedDataNotFound("Missing COMPANY ID")
            )
        }

        var batchDetail: Long? = null
        if (permit.invoiceGenerated == 1) {
            batchDetail = qaDaoServices.findPermitInvoiceByPermitRefNumberANdPermitID(
                permit.permitRefNumber ?: throw ExpectedDataNotFound("PERMIT REF NUMBER NOT FOUND"),
                permit.userId ?: throw ExpectedDataNotFound("MISSING USER ID"),
                permitID
            ).batchInvoiceNo
        }


        req.attributes()["loggedInUserID"] = loggedInUser.id
        req.attributes()["batchID"] = batchDetail
        req.attributes()["invoiceDetails"] = QaInvoiceDetailsEntity()
        req.attributes()["sections"] = loadSectionDetails(departmentEntity, map, req)
        req.attributes()["remarksParameters"] = qaDaoServices.findALlRemarksDetailsPerPermit(permitID)
        req.attributes()["standardsList"] = qaDaoServices.findALlStandardsDetails(map.activeStatus)
        req.attributes().putAll(loadCommonUIComponents(map))
        req.attributes().putAll(loadCommonPermitComponents(map, permit))
        req.attributes()["permit"] = qaDaoServices.permitDetails(permit, map)

        req.attributes()["encryptedPermitId"] = jasyptStringEncryptor.encrypt(permit.id.toString())
        req.attributes()["encryptedUserId"] = jasyptStringEncryptor.encrypt(permit.userId.toString())

        return ok().render(qaPermitDetailPage, req.attributes())
    }


    @PreAuthorize(
        "hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_OFFICER_READ') or hasAuthority('QA_HOD_READ') " +
                "or hasAuthority('QA_MANAGER_READ') or hasAuthority('QA_HOF_READ') or hasAuthority('QA_RM_READ') or hasAuthority('QA_ASSESSORS_READ') or hasAuthority('QA_PAC_SECRETARY_READ') or hasAuthority('QA_PSC_MEMBERS_READ') or hasAuthority('QA_PCM_READ')"
    )
    fun permitViewSmarkDetails(req: ServerRequest): ServerResponse {
        val smarkID =
            req.paramOrNull("smarkID")?.toLong() ?: throw ExpectedDataNotFound("Required Smark ID, check config")
        val map = commonDaoServices.serviceMapDetails(appId)
        val fmarkID = qaDaoServices.findFmarkWithSmarkId(smarkID)
        val permit = fmarkID.fmarkId?.let { qaDaoServices.findPermitBYID(it) }
            ?: throw ExpectedDataNotFound("Required Fmark ID MISSING")
        val plantAttached = permit.attachedPlantId?.let { qaDaoServices.findPlantDetails(it) }


        req.attributes().putAll(loadCommonUIComponents(map))
        req.attributes().putAll(loadCommonPermitComponents(map, permit))
        req.attributes()["permit"] = qaDaoServices.permitDetails(permit, map)

        return ok().render(qaPermitDetailPage, req.attributes())
    }


    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    fun newPermit(req: ServerRequest): ServerResponse {
        val map = commonDaoServices.serviceMapDetails(appId)
        val permitTypeID = req.paramOrNull("permitTypeID")?.toLong()
            ?: throw ExpectedDataNotFound("Required PermitType ID, check config")
        val permitType = qaDaoServices.findPermitType(permitTypeID)
        val departmentEntity = commonDaoServices.findDepartmentByID(applicationMapProperties.mapQADepertmentId)

        req.attributes()["sections"] = loadSectionDetails(departmentEntity, map, req)
        req.attributes()["applicationDate"] = commonDaoServices.getCurrentDate()
        req.attributes()["permitType"] = permitType
        req.attributes()["permit"] = PermitApplicationsEntity()

//        req.attributes()["divisions"] = commonDaoServices.findDivisionByDepartmentId(departmentEntity, map.activeStatus)
        req.attributes()["standardCategory"] =
            standardCategoryRepo.findByStatusOrderByStandardCategory(map.activeStatus)
        return ok().render(qaNewPermitPage, req.attributes())

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    fun batchInvoiceList(req: ServerRequest): ServerResponse {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val plantsDetails = qaDaoServices.findAllPlantDetails(loggedInUser.id ?: throw Exception("INVALID USER ID"))
        val allUnpaidInvoices = qaDaoServices.findALlPermitInvoicesCreatedByUserWithNoPaymentStatus(
            loggedInUser.id ?: throw Exception("INVALID USER ID"), map.inactiveStatus
        )

        req.attributes()["QaBatchInvoiceEntity"] = QaBatchInvoiceEntity()
        req.attributes()["plantsDetails"] = plantsDetails
//        req.attributes()["allPermitInvoiceList"] = qaDaoServices.listPermitsInvoices(allUnpaidInvoices, map)
        req.attributes()["allBatchPermitInvoiceList"] =
            qaDaoServices.findALlBatchInvoicesWithUserID(loggedInUser.id ?: throw Exception("INVALID USER ID"))
        return ok().render(qaPermitInvoiceListPage, req.attributes())

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    fun permitInvoiceList(req: ServerRequest): ServerResponse {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val plantID = req.pathVariable("plantID").toLong()
        val allUnpaidInvoices = qaDaoServices.findALlPermitInvoicesCreatedByUserWithNoPaymentStatus(
            loggedInUser.id ?: throw Exception("INVALID USER ID"), map.inactiveStatus
        )

        val allPermitInvoiceList = qaDaoServices.listPermitsInvoices(allUnpaidInvoices, plantID, map)

        return ok().body(allPermitInvoiceList)

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    fun generatedInvoicePermit(req: ServerRequest): ServerResponse {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val allUnpaidInvoices = qaDaoServices.findALlPermitInvoicesCreatedByUserWithNoPaymentStatus(
            loggedInUser.id ?: throw Exception("INVALID USER ID"), map.inactiveStatus
        )
//        val

//        req.attributes()["InvoiceDetails"] = qaDaoServices.populateInvoiceDetails()
//        req.attributes()["allPermitInvoiceList"] = qaDaoServices.listPermitsInvoices(allUnpaidInvoices, map)
        return ok().render(qaNewPermitInvoicePage, req.attributes())

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    fun batchInvoiceDetails(req: ServerRequest): ServerResponse {
        val map = commonDaoServices.serviceMapDetails(appId)
        val batchID =
            req.paramOrNull("batchID")?.toLong() ?: throw ExpectedDataNotFound("Required batch ID, check config")
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        val batchInvoiceEntity = qaDaoServices.findBatchInvoicesWithID(batchID)
//        val plantDetail = qaDaoServices.findPlantDetails(batchInvoiceEntity.plantId?:throw  Exception("INVALID PLANT ID FOUND"))
        val companyProfile =
            commonDaoServices.findCompanyProfile(loggedInUser.id ?: throw Exception("INVALID USER ID FOUND"))
        val allInvoicesInBatch = qaDaoServices.findALlInvoicesPermitWithBatchID(batchID)
        val allUnpaidInvoices = qaDaoServices.findALlPermitInvoicesCreatedByUserWithNoPaymentStatus(
            loggedInUser.id ?: throw Exception("INVALID USER ID"), map.inactiveStatus
        )

        req.attributes()["NewBatchInvoiceDto"] = NewBatchInvoiceDto()
        req.attributes()["InvoiceDetails"] =
            qaDaoServices.populateInvoiceDetails(companyProfile, batchInvoiceEntity, map)
        req.attributes()["allBatchPermitInvoiceList"] = qaDaoServices.listPermitsInvoices(allInvoicesInBatch, null, map)
        req.attributes()["allPermitInvoiceList"] = qaDaoServices.listPermitsInvoices(allUnpaidInvoices, null, map)
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
        val permitID =
            req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
        val permit = qaDaoServices.findPermitBYUserIDAndId(
            permitID,
            loggedInUser.id ?: throw ExpectedDataNotFound("User Id required")
        )


        req.attributes()["message"] = applicationMapProperties.mapPermitRenewMessage
        req.attributes()["permit"] = permit
        req.attributes()["QaSta3Entity"] = qaDaoServices.findSTA3WithPermitIDAndRefNumber(
            permit.permitRefNumber ?: throw Exception("INVALID PERMIT REF NUMBER"), permitID
        )
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
        req.attributes()["QaSta10Entity"] = qaDaoServices.findSTA10WithPermitRefNumberANdPermitID(
            permit.permitRefNumber ?: throw Exception("INVALID PERMIT REF NUMBER"), permitID
        )
        return ok().render(qaNewSta10Page, req.attributes())

    }


    @PreAuthorize("hasAuthority('QA_MANAGER_MODIFY') or hasAuthority('QA_OFFICER_MODIFY')")
    fun newSta10Officer(req: ServerRequest): ServerResponse {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val permitID =
            req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
        val permit = qaDaoServices.findPermitBYID(permitID)
        req.attributes()["permit"] = permit
        req.attributes()["counties"] = countyRepo.findByStatusOrderByCounty(map.activeStatus)
        req.attributes()["applicationDate"] = commonDaoServices.getCurrentDate()
        req.attributes()["QaSta10Entity"] = qaDaoServices.findSTA10WithPermitRefNumberANdPermitID(
            permit.permitRefNumber ?: throw Exception("INVALID PERMIT REF NUMBER"), permitID
        )
        return ok().render(qaNewSta10OfficerPage, req.attributes())

    }

    @PreAuthorize(
        "hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_OFFICER_READ') or hasAuthority('QA_HOD_READ') " +
                "or hasAuthority('QA_MANAGER_READ') or hasAuthority('QA_HOF_READ') or hasAuthority('QA_RM_READ') or hasAuthority('QA_ASSESSORS_READ') or hasAuthority('QA_PAC_SECRETARY_READ') or hasAuthority('QA_PSC_MEMBERS_READ') or hasAuthority('QA_PCM_READ')"
    )
    fun viewSta3(req: ServerRequest): ServerResponse {
//        val permitUserId = req.paramOrNull("userID")?.toLong() ?: throw ExpectedDataNotFound("Required User ID, check config")

        val permitID =
            req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
        val permit = qaDaoServices.findPermitBYID(permitID)
        req.attributes()["permit"] = permit
        req.attributes()["QaSta3Entity"] = qaDaoServices.findSTA3WithPermitIDAndRefNumber(
            permit.permitRefNumber ?: throw ExpectedDataNotFound("INVALID PERMIT REF NUMBER"), permitID
        )
        req.attributes()["message"] = applicationMapProperties.mapQualityAssuranceManufactureViewPage
        return ok().render(qaNewSta3Page, req.attributes())

    }

    @PreAuthorize(
        "hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_OFFICER_READ') or hasAuthority('QA_HOD_READ') " +
                "or hasAuthority('QA_MANAGER_READ') or hasAuthority('QA_HOF_READ') or hasAuthority('QA_RM_READ') or hasAuthority('QA_ASSESSORS_READ') or hasAuthority('QA_PAC_SECRETARY_READ') or hasAuthority('QA_PSC_MEMBERS_READ') or hasAuthority('QA_PCM_READ')"
    )
    fun viewSta10Officer(req: ServerRequest): ServerResponse {
//        val permitUserId = req.paramOrNull("userID")?.toLong() ?: throw ExpectedDataNotFound("Required User ID, check config")

        val permitID =
            req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
        val permit = qaDaoServices.findPermitBYID(permitID)
        req.attributes()["permit"] = permit
        req.attributes()["QaSta10Entity"] = qaDaoServices.findSTA10WithPermitRefNumberANdPermitID(
            permit.permitRefNumber ?: throw Exception("INVALID PERMIT REF NUMBER"), permitID
        )
        req.attributes()["message"] = applicationMapProperties.mapQualityAssuranceManufactureViewPage
        return ok().render(qaNewSta3Page, req.attributes())

    }

    @PreAuthorize(
        "hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_OFFICER_READ') or hasAuthority('QA_HOD_READ') " +
                "or hasAuthority('QA_MANAGER_READ') or hasAuthority('QA_HOF_READ') or hasAuthority('QA_RM_READ') or hasAuthority('QA_ASSESSORS_READ') or hasAuthority('QA_PAC_SECRETARY_READ') or hasAuthority('QA_PSC_MEMBERS_READ') or hasAuthority('QA_PCM_READ')"
    )
    fun viewRequestDetails(req: ServerRequest): ServerResponse {
//        val permitUserId = req.paramOrNull("userID")?.toLong() ?: throw ExpectedDataNotFound("Required User ID, check config")

        val permitID =
            req.paramOrNull("requestID")?.toLong() ?: throw ExpectedDataNotFound("Required Request ID, check config")
        val request = qaDaoServices.findRequestWithId(permitID)
        return ok().body(request)

    }

    @PreAuthorize(
        "hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_OFFICER_READ') or hasAuthority('QA_HOD_READ') " +
                "or hasAuthority('QA_MANAGER_READ') or hasAuthority('QA_HOF_READ') or hasAuthority('QA_RM_READ') or hasAuthority('QA_ASSESSORS_READ') or hasAuthority('QA_PAC_SECRETARY_READ') or hasAuthority('QA_PSC_MEMBERS_READ') or hasAuthority('QA_PCM_READ')"
    )
    fun viewSta10(req: ServerRequest): ServerResponse {
        val map = commonDaoServices.serviceMapDetails(appId)
//        val permitUserId = req.paramOrNull("userID")?.toLong() ?: throw ExpectedDataNotFound("Required User ID, check config")


        val encryptedPermitId =
            req.paramOrNull("permitID") ?: throw ExpectedDataNotFound("Required Permit ID, check config")
        val permitID = jasyptStringEncryptor.decrypt(encryptedPermitId).toLong()

        val permit = qaDaoServices.findPermitBYID(permitID)
        val qaSta10Entity = qaDaoServices.findSTA10WithPermitRefNumberANdPermitID(
            permit.permitRefNumber ?: throw Exception("INVALID PERMIT REF NUMBER"), permitID
        )
        req.attributes().putAll(commonDaoServices.loadCommonUIComponents(map))
        req.attributes()["permit"] = permit
        req.attributes()["fileParameters"] = qaDaoServices.findAllUploadedFileBYPermitRefNumberAndSta10Status(
            permit.permitRefNumber ?: throw ExpectedDataNotFound("INVALID PERMIT REF NUMBER"), map.activeStatus
        )
        req.attributes()["plantAttached"] = permit.attachedPlantId?.let { qaDaoServices.findPlantDetails(it) }
        req.attributes()["regionDetails"] =
            qaSta10Entity.region?.let { commonDaoServices.findRegionEntityByRegionID(it, map.activeStatus).region }
        req.attributes()["countyDetails"] =
            qaSta10Entity.county?.let { commonDaoServices.findCountiesEntityByCountyId(it, map.activeStatus).county }
        req.attributes()["townDetails"] = qaSta10Entity.town?.let { commonDaoServices.findTownEntityByTownId(it).town }
        req.attributes()["QaSta10Entity"] = qaSta10Entity
        req.attributes().putAll(loadSTA10UIComponents())
        req.attributes()["productsManufacturedList"] =
            qaSta10Entity.id?.let { qaDaoServices.findProductsManufactureWithSTA10ID(it) }
        req.attributes()["rawMaterialList"] = qaSta10Entity.id?.let { qaDaoServices.findRawMaterialsWithSTA10ID(it) }
        req.attributes()["machinePlantList"] = qaSta10Entity.id?.let { qaDaoServices.findMachinePlantsWithSTA10ID(it) }
        req.attributes()["manufacturingProcessesList"] =
            qaSta10Entity.id?.let { qaDaoServices.findManufacturingProcessesWithSTA10ID(it) }

//        req.attributes()["message"] = applicationMapProperties.mapQualityAssuranceManufactureViewPage
        return ok().render(qaNewSta10DetailsPage, req.attributes())

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION') ")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun submitSta10(req: ServerRequest): ServerResponse {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        val permitID =
            req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
        val permit = loggedInUser.id?.let { qaDaoServices.findPermitBYUserIDAndId(permitID, it) }
            ?: throw ExpectedDataNotFound("Required User ID, check config")
        with(permit) {
            sta10FilledStatus = map.activeStatus
            permitStatus = applicationMapProperties.mapQaStatusPSubmission
        }

        qaDaoServices.permitUpdateDetails(permit, map, loggedInUser)

        return ok().render("${qaDaoServices.permitDetails}=${permit.id}&userID=${loggedInUser.id}")

    }

    @PreAuthorize(
        "hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_OFFICER_READ') or hasAuthority('QA_HOD_READ') " +
                "or hasAuthority('QA_MANAGER_READ') or hasAuthority('QA_HOF_READ') or hasAuthority('QA_RM_READ') or hasAuthority('QA_ASSESSORS_READ') or hasAuthority('QA_PAC_SECRETARY_READ') or hasAuthority('QA_PSC_MEMBERS_READ') or hasAuthority('QA_PCM_READ')"
    )
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun generatedSchemeSupervision(req: ServerRequest): ServerResponse {
        val permitID =
            req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
//        val map = commonDaoServices.serviceMapDetails(appId)
        val permit = qaDaoServices.findPermitBYID(permitID)

        req.attributes()["product"] = productsRepo.findByIdOrNull(permit.product)
        req.attributes()["schemeFound"] = qaDaoServices.findSchemeOfSupervisionWithPermitRefNumberBY(
            permit.permitRefNumber ?: throw ExpectedDataNotFound("INVALID PERMIT REF NUMBER")
        )
        req.attributes()["ksApplicable"] = sampleStandardsRepository.findBySubCategoryId(permit.productSubCategory)
        req.attributes()["permitDetails"] = permit

        return ok().render(qaSchemeDetailPage, req.attributes())

    }

    @PreAuthorize(
        "hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_OFFICER_READ') or hasAuthority('QA_HOD_READ') " +
                "or hasAuthority('QA_MANAGER_READ') or hasAuthority('QA_HOF_READ') or hasAuthority('QA_RM_READ') or hasAuthority('QA_ASSESSORS_READ') or hasAuthority('QA_PAC_SECRETARY_READ') or hasAuthority('QA_PSC_MEMBERS_READ') or hasAuthority('QA_PCM_READ')"
    )
    fun generateProductQualityStatus(req: ServerRequest): ServerResponse {
        val permitID =
            req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
        val permit = qaDaoServices.findPermitBYID(permitID)

//        req.attributes()["product"] = productsRepo.findByIdOrNull(permit.product)
        req.attributes()["permitDetails"] = permit

        return ok().render(qaProductQualityStatusPage, req.attributes())
    }


    @PreAuthorize("hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_HOD_READ') or hasAuthority('QA_MANAGER_READ') or hasAuthority('QA_HOF_READ') or hasAuthority('QA_OFFICER_MODIFY') or hasAuthority('QA_ASSESSORS_READ') or hasAuthority('QA_PSC_MEMBERS_READ') or hasAuthority('QA_PCM_READ')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun newSchemeSupervision(req: ServerRequest): ServerResponse {
        val permitID =
            req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
//        val map = commonDaoServices.serviceMapDetails(appId)
        val permit = qaDaoServices.findPermitBYID(permitID)

        req.attributes()["QaSchemeForSupervisionEntity"] = QaSchemeForSupervisionEntity()
        req.attributes()["permitDetails"] = permit

        return ok().render(qaNewSchemeDetailPage, req.attributes())

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_HOD_READ') or hasAuthority('QA_MANAGER_READ') or hasAuthority('QA_HOF_READ') or hasAuthority('QA_OFFICER_MODIFY') or hasAuthority('QA_ASSESSORS_READ') or hasAuthority('QA_PSC_MEMBERS_READ') or hasAuthority('QA_PCM_READ')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun updateSchemeSupervision(req: ServerRequest): ServerResponse {
        val permitID =
            req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
//        val map = commonDaoServices.serviceMapDetails(appId)
        val permit = qaDaoServices.findPermitBYID(permitID)

        req.attributes()["message"] = "updateScheme"
        req.attributes()["QaSchemeForSupervisionEntity"] = qaDaoServices.findSchemeOfSupervisionWithPermitRefNumberBY(
            permit.permitRefNumber ?: throw ExpectedDataNotFound("INVALID PERMIT REF NUMBER")
        )
        req.attributes()["permitDetails"] = permit

        return ok().render(qaNewSchemeDetailPage, req.attributes())

    }

    @PreAuthorize(
        "hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_OFFICER_READ') or hasAuthority('QA_HOD_READ') " +
                "or hasAuthority('QA_MANAGER_READ') or hasAuthority('QA_HOF_READ') or hasAuthority('QA_RM_READ') or hasAuthority('QA_ASSESSORS_READ') or hasAuthority('QA_PAC_SECRETARY_READ') or hasAuthority('QA_PSC_MEMBERS_READ') or hasAuthority('QA_PCM_READ')"
    )
    fun getInvoiceDetails(req: ServerRequest): ServerResponse {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)

        val permitID =
            req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
        val permit = qaDaoServices.findPermitBYID(permitID)
        //Todo: Remove smart cast
        val invoiceDetails = permit.userId?.let {
            qaDaoServices.findPermitInvoiceByPermitRefNumberANdPermitID(
                permit.permitRefNumber ?: throw ExpectedDataNotFound("INVALID PERMIT REF NUMBER"),
                it,
                permit.id ?: throw ExpectedDataNotFound("INVALID PERMIT REF NUMBER")
            )
        }
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


    @PreAuthorize(
        "hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_OFFICER_READ') or hasAuthority('QA_HOD_READ') " +
                "or hasAuthority('QA_MANAGER_READ') or hasAuthority('QA_HOF_READ') or hasAuthority('QA_RM_READ') or hasAuthority('QA_ASSESSORS_READ') or hasAuthority('QA_PAC_SECRETARY_READ') or hasAuthority('QA_PSC_MEMBERS_READ') or hasAuthority('QA_PCM_READ')"
    )
    fun getSSfDetails(req: ServerRequest): ServerResponse {
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        val ssfID = req.paramOrNull("ssfID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
//        val permit = qaDaoServices.findPermitBYID(permitID)
        val ssfDetails = qaDaoServices.findSampleSubmittedBYID(ssfID)
        val permit = qaDaoServices.findPermitBYID(
            ssfDetails.permitId ?: throw ExpectedDataNotFound("Required Permit ID, check config")
        )
        req.attributes()["permitDetails"] = permit
        req.attributes()["ssfDetails"] = ssfDetails
        val labResultsParameters = qaDaoServices.findSampleLabTestResultsRepoBYBSNumber(
            ssfDetails.bsNumber ?: throw ExpectedDataNotFound("MISSING BS NUMBER")
        )
        KotlinLogging.logger { }.info { ssfDetails.bsNumber }

        val savedPDFFiles = qaDaoServices.findSampleSubmittedListPdfBYSSFid(ssfID)

        val result = mutableListOf<LimsFilesFoundDto>()
        val finalResult = mutableListOf<LimsFilesFoundDto>()

        limsServices.checkPDFFiles(ssfDetails.bsNumber ?: throw ExpectedDataNotFound("MISSING BS NUMBER"))
            ?.forEach { fpdf ->
                if (savedPDFFiles.isNotEmpty()) {
                    savedPDFFiles.firstOrNull { it.pdfName == fpdf }
                        ?.let {
                            val limsDto = LimsFilesFoundDto(
                                true,
                                fpdf
                            )
                            result.add(limsDto)
                        }
                        ?: run {
                            val limsDto = LimsFilesFoundDto(
                                false,
                                fpdf
                            )
                            result.add(limsDto)
                        }
                } else {
                    val limsDto = LimsFilesFoundDto(
                        false,
                        fpdf
                    )
                    result.add(limsDto)
                }

            }


//        }
        req.attributes()["LabResultsParameters"] = labResultsParameters
        req.attributes()["savedPDFFiles"] = savedPDFFiles
        req.attributes()["foundPDFFiles"] = result.distinct()
        req.attributes()["complianceDetails"] = QaSampleSubmittedPdfListDetailsEntity()
        req.attributes()["SampleSubmissionDetails"] = QaSampleSubmissionEntity()

        return ok().render(qaSSFDetailesPage, req.attributes())

    }

    @PreAuthorize(
        "hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_OFFICER_READ') or hasAuthority('QA_HOD_READ') " +
                "or hasAuthority('QA_MANAGER_READ') or hasAuthority('QA_HOF_READ') or hasAuthority('QA_RM_READ') or hasAuthority('QA_ASSESSORS_READ') or hasAuthority('QA_PAC_SECRETARY_READ') or hasAuthority('QA_PSC_MEMBERS_READ') or hasAuthority('QA_PCM_READ')"
    )
    fun getSSfListDetails(req: ServerRequest): ServerResponse {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)

        val permitID =
            req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
        val permit = qaDaoServices.findPermitBYID(permitID)
        val allSSFDetailsList = qaDaoServices.findSampleSubmittedListBYPermitRefNumberAndPermitID(
            permit.permitRefNumber ?: throw ExpectedDataNotFound("INVALID PERMIT REF NUMBER"),
            map.activeStatus,
            permitID
        )
        req.attributes()["allSSFDetailsList"] = allSSFDetailsList
        req.attributes()["permitDetails"] = permit
        req.attributes()["SampleSubmissionDetails"] = QaSampleSubmissionEntity()

        return ok().render(qaSSFListDetailesPage, req.attributes())
    }

    @PreAuthorize(
        "hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_OFFICER_READ') or hasAuthority('QA_HOD_READ') " +
                "or hasAuthority('QA_MANAGER_READ') or hasAuthority('QA_HOF_READ') or hasAuthority('QA_RM_READ') or hasAuthority('QA_ASSESSORS_READ') or hasAuthority('QA_PAC_SECRETARY_READ') or hasAuthority('QA_PSC_MEMBERS_READ') or hasAuthority('QA_PCM_READ')"
    )
    fun getInspectionListDetails(req: ServerRequest): ServerResponse {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)

        val permitID =
            req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
        val permit = qaDaoServices.findPermitBYID(permitID)
        val allInspectionReportDetailsList = qaDaoServices.findInspectionReportListBYPermitRefNumberAndPermitID(
            permit.permitRefNumber ?: throw ExpectedDataNotFound("INVALID PERMIT REF NUMBER"),
            map.activeStatus,
            permitID
        )

        req.attributes()["allInspectionReportDetailsList"] = allInspectionReportDetailsList
        req.attributes()["permitDetails"] = permit

        return ok().render(qaInspectionReportListsPage, req.attributes())
    }


    @PreAuthorize(" hasAuthority('QA_OFFICER_READ') or hasAuthority('QA_ASSESSORS_READ')")
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

    @PreAuthorize(
        "hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_OFFICER_READ') or hasAuthority('QA_HOD_READ') " +
                "or hasAuthority('QA_MANAGER_READ') or hasAuthority('QA_HOF_READ') or hasAuthority('QA_RM_READ') or hasAuthority('QA_ASSESSORS_READ') or hasAuthority('QA_PAC_SECRETARY_READ') or hasAuthority('QA_PSC_MEMBERS_READ') or hasAuthority('QA_PCM_READ')"
    )
    fun inspectionReportDetails(req: ServerRequest): ServerResponse {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
//        val permitID = req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
//
        val inspectReportID = req.paramOrNull("inspectionReportID")?.toLong()
            ?: throw ExpectedDataNotFound("Required Inspection ID, check config")
        val inspectionReportRecommendation = qaDaoServices.findQaInspectionReportRecommendationBYID(inspectReportID)
        val permit = qaDaoServices.findPermitBYID(
            inspectionReportRecommendation.permitId ?: throw ExpectedDataNotFound("Required Permit ID, check config")
        )

        req.attributes()["OPCDetailsList"] = qaDaoServices.findAllQaInspectionOPCWithInspectReportID(inspectReportID)
        req.attributes()["QaInspectionTechnicalEntity"] =
            qaDaoServices.findQaInspectionTechnicalBYInspectReportID(inspectReportID)
        req.attributes()["QaInspectionReportRecommendationEntity"] = inspectionReportRecommendation

        when (inspectionReportRecommendation.filledOpcStatus) {
            map.activeStatus -> {
                req.attributes()["OPCDetailsList"] = qaDaoServices.findQaInspectionOpcBYInspectReportID(inspectReportID)
            }
        }

        when (inspectionReportRecommendation.filledHaccpImplementationStatus) {
            map.activeStatus -> {
                req.attributes()["QaInspectionHaccpImplementationEntity"] =
                    qaDaoServices.findQaInspectionHaccpImplementationBYInspectReportID(inspectReportID)
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
        req.attributes()["fileParameters"] =
            qaDaoServices.findAllUploadedFileBYInspectionReportIDAndInspectionReportStatus(
                inspectReportID,
                map.activeStatus
            )
        req.attributes()["QaInspectionOpcEntity"] = QaInspectionOpcEntity()
        req.attributes()["permit"] = qaDaoServices.permitDetails(permit, map)
        return ok().render(qaInspectionReportPage, req.attributes())
    }

    @PreAuthorize("hasAuthority('QA_OFFICER_READ') or hasAuthority('QA_ASSESSORS_READ')")
    fun newInspectionReport(req: ServerRequest): ServerResponse {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        val permitID =
            req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
        val permit = qaDaoServices.findPermitBYID(permitID)

        req.attributes()["OPCDetailsList"] = qaDaoServices.findAllQaInspectionOPCWithPermitRefNumber(
            permit.permitRefNumber ?: throw ExpectedDataNotFound("INVALID PERMIT REF NUMBER")
        )
        req.attributes()["QaInspectionHaccpImplementationEntity"] = QaInspectionHaccpImplementationEntity()
        req.attributes()["QaInspectionReportRecommendationEntity"] = QaInspectionReportRecommendationEntity()
        req.attributes()["QaInspectionOpcEntity"] = QaInspectionOpcEntity()
        req.attributes()["QaInspectionTechnicalEntity"] = QaInspectionTechnicalEntity()
        req.attributes()["permit"] = qaDaoServices.permitDetails(permit, map)
        return ok().render(qaInspectionReportPage, req.attributes())
    }


    @PreAuthorize(" hasAuthority('QA_OFFICER_READ') or hasAuthority('QA_ASSESSORS_READ')")
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

    @PreAuthorize(" hasAuthority('QA_OFFICER_READ') or hasAuthority('QA_ASSESSORS_READ')")
    fun workPlanDetails(req: ServerRequest): ServerResponse {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        val refNumber = req.paramOrNull("refNumber") ?: throw ExpectedDataNotFound("Required REF Number, check config")
        val workPlan = qaDaoServices.findCreatedWorkPlanWIthOfficerID(
            loggedInUser.id ?: throw Exception("INVALID USER ID"),
            refNumber
        )

        val permit = qaDaoServices.findPermitBYID(workPlan.permitId ?: throw Exception("INVALID PERMIT ID"))

        req.attributes().putAll(loadCommonUIComponents(map))
        req.attributes().putAll(loadCommonPermitComponents(map, permit))
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
            Pair("resubmitApplication", ResubmitApplicationDto()),
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

    private fun loadCommonPermitComponents(
        s: ServiceMapsEntity,
        permit: PermitApplicationsEntity
    ): MutableMap<String, Any?> {
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
            Pair(
                "fileParameters",
                qaDaoServices.findAllUploadedFileBYPermitRefNumberAndOrdinarStatus(
                    permit.permitRefNumber ?: throw ExpectedDataNotFound("INVALID PERMIT REF NUMBER"), s.activeStatus
                )
            ),
            Pair(
                "fileSta3Parameters",
                qaDaoServices.findAllUploadedFileBYPermitIDAndSta3Status(
                    permit.id ?: throw ExpectedDataNotFound("MISSING PERMIT ID"), s.activeStatus
                )
            ),
            Pair(
                "fileSta10Parameters",
                qaDaoServices.findAllUploadedFileBYPermitIDAndSta10Status(
                    permit.id ?: throw ExpectedDataNotFound("MISSING PERMIT ID"), s.activeStatus
                )
            ),
            Pair(
                "cocParameters",
                qaDaoServices.findAllUploadedFileBYPermitRefNumberAndCocStatus(
                    permit.permitRefNumber ?: throw ExpectedDataNotFound("INVALID PERMIT REF NUMBER"), s.activeStatus
                )
            ),
            Pair(
                "assessmentReportParameters",
                qaDaoServices.findAllUploadedFileBYPermitRefNumberAndAssessmentReportStatus(
                    permit.permitRefNumber ?: throw ExpectedDataNotFound("INVALID PERMIT REF NUMBER"), s.activeStatus
                )
            ),
            Pair(
                "sscParameters",
                qaDaoServices.findAllUploadedFileBYPermitRefNumberAndSscStatus(
                    permit.permitRefNumber ?: throw ExpectedDataNotFound("INVALID PERMIT REF NUMBER"), s.activeStatus
                )
            ),

            Pair(
                "justificationReportParameters",
                qaDaoServices.findAllUploadedFileBYPermitRefNumberAndJustificationReportStatusAndPermitId(
                    permit.permitRefNumber ?: throw ExpectedDataNotFound("INVALID PERMIT REF NUMBER"),
                    s.activeStatus,
                    permit.id ?: throw ExpectedDataNotFound("INVALID PERMIT ID"),
                )
            ),
//            Pair("statusName", qaDaoServices.findPermitStatus(permit.permitStatus?:throw Exception("INVALID PERMIT STATUS VALUE"))),
            Pair(
                "myRequests",
                qaDaoServices.findAllRequestByPermitRefNumber(
                    permit.permitRefNumber ?: throw ExpectedDataNotFound("INVALID PERMIT REF NUMBER"),
                    permit.id ?: throw ExpectedDataNotFound("INVALID PERMIT ID")
                )
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
                        applicationMapProperties.mapQAUserOfficerRoleId
                    )
                }),
            Pair(
                "assessorAssigned",
                permit.attachedPlantId?.let {
                    qaDaoServices.findOfficersList(
                        it,
                        permit,
                        s,
                        applicationMapProperties.mapQAUserAssessorRoleId
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
        "hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_OFFICER_READ') or hasAuthority('QA_HOD_READ') " +
                "or hasAuthority('QA_MANAGER_READ') or hasAuthority('QA_HOF_READ') or hasAuthority('QA_RM_READ') or hasAuthority('QA_ASSESSORS_READ') or hasAuthority('QA_PAC_SECRETARY_READ') or hasAuthority('QA_PSC_MEMBERS_READ') or hasAuthority('QA_PCM_READ')"
    )
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
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
            return badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }

    @PreAuthorize(
        "hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_OFFICER_READ') or hasAuthority('QA_HOD_READ') " +
                "or hasAuthority('QA_MANAGER_READ') or hasAuthority('QA_HOF_READ') or hasAuthority('QA_RM_READ') or hasAuthority('QA_ASSESSORS_READ') or hasAuthority('QA_PAC_SECRETARY_READ') or hasAuthority('QA_PSC_MEMBERS_READ') or hasAuthority('QA_PCM_READ')"
    )
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun taskListMigration(req: ServerRequest): ServerResponse {
        try {
            val map = commonDaoServices.serviceMapDetails(appId)
            val departmentEntity = commonDaoServices.findDepartmentByID(applicationMapProperties.mapQADepertmentId)

            commonDaoServices.mapAllSectionsTogether(loadSectionDetails(departmentEntity, map, req)).let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }

    @PreAuthorize(
        "hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_OFFICER_READ') or hasAuthority('QA_HOD_READ') " +
                "or hasAuthority('QA_MANAGER_READ') or hasAuthority('QA_HOF_READ') or hasAuthority('QA_RM_READ') or hasAuthority('QA_ASSESSORS_READ') or hasAuthority('QA_PAC_SECRETARY_READ') or hasAuthority('QA_PSC_MEMBERS_READ') or hasAuthority('QA_PCM_READ')"
    )
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun standardsListMigration(req: ServerRequest): ServerResponse {
        try {
            val map = commonDaoServices.serviceMapDetails(appId)

            qaDaoServices.mapAllStandardsTogether(qaDaoServices.findALlStandardsDetails(map.activeStatus)).let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_OFFICER_READ') or hasAuthority('QA_HOD_READ') or hasAuthority('QA_MANAGER_READ')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun branchListMigration(req: ServerRequest): ServerResponse {
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val map = commonDaoServices.serviceMapDetails(appId)
            val auth = commonDaoServices.loggedInUserAuthentication()
            if (auth.authorities.stream().anyMatch { authority -> authority.authority == "MODIFY_COMPANY" }) {
                qaDaoServices.mapAllPlantsTogether(
                    qaDaoServices.findAllPlantDetailsWithCompanyID(
                        loggedInUser.companyId ?: throw ExpectedDataNotFound("Missing COMPANY ID")
                    ), map
                ).let {
                    return ok().body(it)
                }
            } else {
                qaDaoServices.mapAllPlantsTogether(
                    qaDaoServices.findAllPlantDetailsWithIDAndStatus(
                        map.activeStatus, loggedInUser.plantId ?: throw ExpectedDataNotFound("Missing PLANT ID")
                    ), map
                ).let {
                    return ok().body(it)
                }
            }


        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }

    @PreAuthorize(
        "hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_OFFICER_READ') or hasAuthority('QA_HOD_READ') or hasAuthority('QA_MANAGER_READ')" +
                " or hasAuthority('QA_HOF_READ') or hasAuthority('QA_ASSESSORS_READ') or hasAuthority('QA_PAC_SECRETARY_READ') or hasAuthority('QA_PSC_MEMBERS_READ') or hasAuthority('QA_PCM_READ')"
    )
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun permitListMigration(req: ServerRequest): ServerResponse {
        try {
            val auth = commonDaoServices.loggedInUserAuthentication()
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val map = commonDaoServices.serviceMapDetails(appId)
            val permitTypeID = req.paramOrNull("permitTypeID")?.toLong()
                ?: throw ExpectedDataNotFound("Required PermitType ID, check config")
            val permitType = qaDaoServices.findPermitType(permitTypeID)

            var permitListAllApplications: List<PermitEntityDto>? = null
            permitListAllApplications = when {
                auth.authorities.stream()
                    .anyMatch { authority -> authority.authority == "MODIFY_COMPANY" } && auth.authorities.stream()
                    .anyMatch { authority -> authority.authority == "PERMIT_APPLICATION" } -> {
                    qaDaoServices.listFirmPermitListWithPermitType(
                        loggedInUser.companyId ?: throw Exception("MISSING COMPANY ID"), permitTypeID, map
                    )
                }

                auth.authorities.stream()
                    .anyMatch { authority -> authority.authority != "MODIFY_COMPANY" } && auth.authorities.stream()
                    .anyMatch { authority -> authority.authority == "PERMIT_APPLICATION" } -> {
                    qaDaoServices.listBranchListWithPermitType(
                        loggedInUser.plantId ?: throw Exception("MISSING PLANT ID"), permitTypeID, map
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
            return badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_OFFICER_READ') or hasAuthority('QA_HOD_READ') or hasAuthority('QA_MANAGER_READ') or hasAuthority('QA_HOF_READ') or hasAuthority('QA_ASSESSORS_READ') or hasAuthority('QA_PAC_SECRETARY_READ') or hasAuthority('QA_PSC_MEMBERS_READ') or hasAuthority('QA_PCM_READ')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun permitListAwardedMigrationb(req: ServerRequest): ServerResponse {
        try {
            val auth = commonDaoServices.loggedInUserAuthentication()
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val map = commonDaoServices.serviceMapDetails(appId)
            val permitNumber = req.paramOrNull("permitNumber")?.toString()
                ?: throw ExpectedDataNotFound("Required PermitNumber, check config")

            var permitListAllApplications: List<PermitApplicationsEntity>? = null
            when {
                auth.authorities.stream().anyMatch { authority -> authority.authority == "PERMIT_APPLICATION" } -> {
                    permitListAllApplications = qaDaoServices.findAllLoadedPermitList(
                        loggedInUser,
                        permitNumber,
                        loggedInUser,
                    )
                }

                else -> {
                    throw ExpectedDataNotFound("UNAUTHORISED LOGGED IN USER (ACCESS DENIED)")
                }
            }

            return ok().body(permitListAllApplications)

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_OFFICER_READ') or hasAuthority('QA_HOD_READ') or hasAuthority('QA_MANAGER_READ') or hasAuthority('QA_HOF_READ') or hasAuthority('QA_ASSESSORS_READ') or hasAuthority('QA_PAC_SECRETARY_READ') or hasAuthority('QA_PSC_MEMBERS_READ') or hasAuthority('QA_PCM_READ')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun loadAllMyPermits(req: ServerRequest): ServerResponse {
        try {
            val auth = commonDaoServices.loggedInUserAuthentication()
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val map = commonDaoServices.serviceMapDetails(appId)

            var permitListAllApplications: List<PermitApplicationsEntity>? = null
            when {
                auth.authorities.stream().anyMatch { authority -> authority.authority == "PERMIT_APPLICATION" } -> {
                    permitListAllApplications = qaDaoServices.findAllMyPermits(
                        loggedInUser,
                    )
                }

                else -> {
                    throw ExpectedDataNotFound("UNAUTHORISED LOGGED IN USER (ACCESS DENIED)")
                }
            }

            return ok().body(permitListAllApplications)

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_OFFICER_READ') or hasAuthority('QA_HOD_READ') or hasAuthority('QA_MANAGER_READ') or hasAuthority('QA_HOF_READ') or hasAuthority('QA_ASSESSORS_READ') or hasAuthority('QA_PAC_SECRETARY_READ') or hasAuthority('QA_PSC_MEMBERS_READ') or hasAuthority('QA_PCM_READ')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun deleteAPermit(req: ServerRequest): ServerResponse {
        return try {
            val map = commonDaoServices.serviceMapDetails(appId)
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")

            val permit = qaDaoServices.findPermitBYID(permitID)

            permit.id?.let { qaDaoServices.deletePermit(it) }

            return ok().body(permit)


        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }


    @PreAuthorize("hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_OFFICER_READ') or hasAuthority('QA_HOD_READ') or hasAuthority('QA_MANAGER_READ') or hasAuthority('QA_HOF_READ') or hasAuthority('QA_ASSESSORS_READ') or hasAuthority('QA_PAC_SECRETARY_READ') or hasAuthority('QA_PSC_MEMBERS_READ') or hasAuthority('QA_PCM_READ')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun permitListAwardedMigration(req: ServerRequest): ServerResponse {
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
                        qaDaoServices.findAllUserPermitWithPermitTypeAwardedStatus(
                            loggedInUser,
                            permitTypeID,
                            map.activeStatus
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
            return badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_OFFICER_READ') or hasAuthority('QA_HOD_READ') or hasAuthority('QA_MANAGER_READ') or hasAuthority('QA_HOF_READ') or hasAuthority('QA_ASSESSORS_READ') or hasAuthority('QA_PAC_SECRETARY_READ') or hasAuthority('QA_PSC_MEMBERS_READ') or hasAuthority('QA_PCM_READ')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun permitCompletelyListAwardedMigration(req: ServerRequest): ServerResponse {
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
                        qaDaoServices.findAllUserCompletelyPermitWithPermitTypeAwardedStatus(
                            loggedInUser,
                            permitTypeID,
                            map.activeStatus
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
            return badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_OFFICER_READ') or hasAuthority('QA_HOD_READ') or hasAuthority('QA_MANAGER_READ') or hasAuthority('QA_HOF_READ') or hasAuthority('QA_ASSESSORS_READ') or hasAuthority('QA_PAC_SECRETARY_READ') or hasAuthority('QA_PSC_MEMBERS_READ') or hasAuthority('QA_PCM_READ')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun permitListAwardedGenerateFmarkMigration(req: ServerRequest): ServerResponse {
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
                        qaDaoServices.findAllSmarkPermitWithNoFmarkGenerated(
                            loggedInUser,
                            permitTypeID,
                            map.activeStatus,
                            map.inactiveStatus
                        ), map
                    )
//                    permitListAllApplications = qaDaoServices.listPermits(
//                        qaDaoServices.findAllUserPermitWithPermitTypeAwardedStatus(
//                            loggedInUser,
//                            permitTypeID,
//                            map.activeStatus
//                        ), map
//                    )
                }

                else -> {
                    throw ExpectedDataNotFound("UNAUTHORISED LOGGED IN USER (ACCESS DENIED)")
                }
            }

            return ok().body(permitListAllApplications)

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }

    @PreAuthorize(
        "hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_OFFICER_READ') or hasAuthority('QA_HOD_READ') or hasAuthority('QA_MANAGER_READ')" +
                " or hasAuthority('QA_HOF_READ') or hasAuthority('QA_ASSESSORS_READ') or hasAuthority('QA_PAC_SECRETARY_READ') or hasAuthority('QA_PSC_MEMBERS_READ') or hasAuthority('QA_PCM_READ')"
    )
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun permitTaskListMigration(req: ServerRequest): ServerResponse {
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val auth = commonDaoServices.loggedInUserAuthentication()
            val map = commonDaoServices.serviceMapDetails(appId)
//            val allMyTasks = qaDaoServices.getTaskListPermit(loggedInUser.id ?: throw ExpectedDataNotFound("MISSING USER ID"))
            val permitListMyTasks = qaDaoServices.findAllUserTasksManufactureByTaskID(
                loggedInUser,
                auth,
                applicationMapProperties.mapUserTaskNameMANUFACTURE
            )?.let {
                qaDaoServices.listPermits(
                    it, map
                )
            } ?: throw ExpectedDataNotFound("NO TASK FOUND")
//            permitListMyTasks = qaDaoServices.listPermits(
//                qaDaoServices.findAllUserTasksByTaskID(
//                    loggedInUser,
//                    applicationMapProperties.mapUserTaskNameMANUFACTURE
//                ), map
//            )

            permitListMyTasks.let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }

    @PreAuthorize(
        "hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_OFFICER_READ') or hasAuthority('QA_HOD_READ') or hasAuthority('QA_MANAGER_READ')" +
                " or hasAuthority('QA_HOF_READ') or hasAuthority('QA_ASSESSORS_READ') or hasAuthority('QA_PAC_SECRETARY_READ') or hasAuthority('QA_PSC_MEMBERS_READ') or hasAuthority('QA_PCM_READ')"
    )
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun firmPermitListMigration(req: ServerRequest): ServerResponse {
        return try {
            val auth = commonDaoServices.loggedInUserAuthentication()
            val map = commonDaoServices.serviceMapDetails(appId)
            val companyID = req.paramOrNull("companyID")?.toLong()
                ?: throw ExpectedDataNotFound("Required COMPANY ID, check config")
            val companyEntity = commonDaoServices.findCompanyProfileWithID(companyID)

            val permitListAllApplications: List<PermitEntityDto> = qaDaoServices.listFirmPermitList(companyID, map)

            ok().body(permitListAllApplications)

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }


    @PreAuthorize(
        "hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_OFFICER_READ') or hasAuthority('QA_HOD_READ') or hasAuthority('QA_MANAGER_READ')" +
                " or hasAuthority('QA_HOF_READ') or hasAuthority('QA_ASSESSORS_READ') or hasAuthority('QA_PAC_SECRETARY_READ') or hasAuthority('QA_PSC_MEMBERS_READ') or hasAuthority('QA_PCM_READ')"
    )
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun firmBranchPermitListMigration(req: ServerRequest): ServerResponse {
        return try {
            val auth = commonDaoServices.loggedInUserAuthentication()
            val map = commonDaoServices.serviceMapDetails(appId)
            val branchID =
                req.paramOrNull("branchID")?.toLong() ?: throw ExpectedDataNotFound("Required branch ID, check config")
            val branchEntity = qaDaoServices.findPlantDetails(branchID)

            var permitListAllApplications: List<PermitEntityDto> = qaDaoServices.listBranchList(branchID, map)

            ok().body(permitListAllApplications)

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }


    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun permitMPesaPushStk(req: ServerRequest): ServerResponse {
        return try {
            val map = commonDaoServices.serviceMapDetails(appId)
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val dto = req.body<MPesaPushDto>()

            val invoiceEntity = qaDaoServices.findBatchInvoicesWithID(dto.entityValueID)
            qaDaoServices.permitInvoiceSTKPush(map, loggedInUser, dto.phoneNumber, invoiceEntity)

            val messageDto = MPesaMessageDto(
                "Check You phone for an STK Push,If You can't see the push either pay with Bank or MPesa Paybill number"
            )
            ok().body(messageDto)

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
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
                fmarkGenerateStatus = dto.createFmark ?: 0
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

            qaDaoServices.mapDtoSTA1View(createdPermit.second).let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun permitProcessStepMigration(req: ServerRequest): ServerResponse {
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val map = commonDaoServices.serviceMapDetails(appId)
            val auth = commonDaoServices.loggedInUserAuthentication()
            val dto = req.body<PermitProcessStepDto>()

            val permitDetails = qaDaoServices.findPermitBYID(
                dto.permitID ?: throw ExpectedDataNotFound("Required permit ID, check config")
            )

            //updating of Details in DB
            permitDetails.processStep = dto.processStep ?: 1
            val updateResults = qaDaoServices.permitUpdateDetails(permitDetails, map, loggedInUser)

            qaDaoServices.permitDetails(updateResults.second, map).let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun permitApproveRejectSSCMigration(req: ServerRequest): ServerResponse {
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val map = commonDaoServices.serviceMapDetails(appId)
            val permitID = req.paramOrNull("permitID")?.toLong()
                ?: throw ExpectedDataNotFound("Required PermitType ID, check config")
            val permitDetails = qaDaoServices.findPermitBYID(permitID)

            val dto = req.body<SSCApprovalRejectionDto>()
            val permit = PermitApplicationsEntity()
            with(permit) {
                id = permitID
                approvedRejectedScheme = dto.approvedRejectedScheme
                approvedRejectedSchemeRemarks = dto.approvedRejectedSchemeRemarks
            }

            //updating of Details in DB
            val updateResults = qaDaoServices.permitUpdateDetails(
                commonDaoServices.updateDetails(
                    permit,
                    permitDetails
                ) as PermitApplicationsEntity, map, loggedInUser
            )
            qaDaoServices.approvedRejectedSSC(dto, map, updateResults.second, loggedInUser)

            qaDaoServices.permitDetails(updateResults.second, map).let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun permitResubmitMigration(req: ServerRequest): ServerResponse {
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val map = commonDaoServices.serviceMapDetails(appId)
            val permitID = req.paramOrNull("permitID")?.toLong()
                ?: throw ExpectedDataNotFound("Required PermitType ID, check config")
            val permitDetails = qaDaoServices.findPermitBYID(permitID)

            val dto = req.body<ResubmitApplicationDto>()

            //updating of Details in DB
            val updateResults = qaDaoServices.permitResubmitDetails(permitDetails, dto, map, loggedInUser)

            qaDaoServices.permitDetails(updateResults.second, map).let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun permitUpdateSTA1Migration(req: ServerRequest): ServerResponse {
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val map = commonDaoServices.serviceMapDetails(appId)
            val auth = commonDaoServices.loggedInUserAuthentication()
            val permitID = req.paramOrNull("permitID")?.toLong()
                ?: throw ExpectedDataNotFound("Required PermitType ID, check config")
            val permitDetails = qaDaoServices.findPermitBYID(permitID)

            val dto = req.body<STA1Dto>()
            val permit = PermitApplicationsEntity()
            with(permit) {
                id = permitDetails.id
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

            //updating of Details in DB
            val updateResults = qaDaoServices.permitUpdateDetails(
                commonDaoServices.updateDetails(
                    permit,
                    permitDetails
                ) as PermitApplicationsEntity, map, loggedInUser
            )

            qaDaoServices.mapDtoSTA1View(updateResults.second).let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun permitSubmitApplicationInvoiceMigration(req: ServerRequest): ServerResponse {
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

            //Create FMARK From SMark
//            if (permit.fmarkGenerateStatus == 1) {
//                qaDaoServices.permitGenerateFmark(map, loggedInUser, permit).first
//            }
            //Update Permit Details
            with(permit) {
                applicationStatus = map.activeStatus
            }

            permit = qaDaoServices.permitUpdateDetails(permit, map, loggedInUser).second

            //Calculate Invoice Details
            val invoiceCreated = qaDaoServices.permitInvoiceCalculation(map, loggedInUser, permit, null).second

            //Update Permit Details
            with(permit) {
                sendApplication = map.activeStatus
                endOfProductionStatus = map.inactiveStatus
                invoiceGenerated = map.activeStatus
                permitStatus = applicationMapProperties.mapQaStatusPPayment
            }
            permit = qaDaoServices.permitUpdateDetails(permit, map, loggedInUser).second


//            //Start DMARK PROCESS
//            qualityAssuranceBpmn.startQADmAppPaymentProcess(
//                permit.id ?: throw Exception("MISSING PERMIT ID"), null
//            )
//
//            //Complete Submit Application
//            qualityAssuranceBpmn.qaDmSubmitApplicationComplete(permit.id ?: throw Exception("MISSING PERMIT ID"), permit.renewalStatus == 1,
//                permit.permitForeignStatus == 1)

            qaDaoServices.mapAllPermitDetailsTogether(
                permit,
                null,
                map
            ).let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            throw e
        }

    }


    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun permitSubmitApplicationReviewMigration(req: ServerRequest): ServerResponse {
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val map = commonDaoServices.serviceMapDetails(appId)
            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
            var permit = qaDaoServices.findPermitBYUserIDAndId(
                permitID,
                loggedInUser.id ?: throw ExpectedDataNotFound("MISSING USER ID")
            )

            //Update Permit Details
            with(permit) {
                resubmitApplicationStatus = map.initStatus
                sendForPcmReview = map.activeStatus
//                pcmId = qaDaoServices.assignNextOfficerWithDesignation(
//                    permit,
//                    map,
//                    applicationMapProperties.mapQADesignationIDForPCMId
//                )?.id
                permitStatus = applicationMapProperties.mapQaStatusPPCMReview
                userTaskId = applicationMapProperties.mapUserTaskNamePCM
            }
            permit = qaDaoServices.permitUpdateDetails(permit, map, loggedInUser).second

            //Start DMARK PROCESS
//            qualityAssuranceBpmn.startQADmAppPaymentProcess(
//                permit.id ?: throw Exception("MISSING PERMIT ID"), null
//            )
//
//            //Complete Submit Application
//            qualityAssuranceBpmn.qaDmSubmitApplicationComplete(permit.id ?: throw Exception("MISSING PERMIT ID"), permit.renewalStatus == 1,
//                permit.permitForeignStatus == 1)

            qaDaoServices.mapAllPermitDetailsTogether(permit, null, map).let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun permitSubmitApplicationQAMHODReviewMigration(req: ServerRequest): ServerResponse {
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val map = commonDaoServices.serviceMapDetails(appId)
            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
            var permit = qaDaoServices.findPermitBYUserIDAndId(
                permitID,
                loggedInUser.id ?: throw ExpectedDataNotFound("MISSING USER ID")
            )

            //Update Permit Details
            with(permit) {
                resubmitApplicationStatus = map.initStatus
                hofQamCompletenessStatus = null
                hofQamCompletenessRemarks = null
                permitStatus = applicationMapProperties.mapQaStatusPApprovalCompletness
                userTaskId = when (permit.permitType) {
                    applicationMapProperties.mapQAPermitTypeIDDmark -> {
                        applicationMapProperties.mapUserTaskNameHOD
                    }

                    else -> {
                        applicationMapProperties.mapUserTaskNameQAM
                    }
                }

            }
            permit = qaDaoServices.permitUpdateDetails(permit, map, loggedInUser).second

//            val userDetailEmail = when (permit.permitType) {
//                applicationMapProperties.mapQAPermitTypeIDDmark -> {
//                    commonDaoServices.findUserByID(permit.hodId ?: throw ExpectedDataNotFound("MISSING HOD ID")).email
//                        ?: throw ExpectedDataNotFound("MISSING HOD EMAIL")
//                }
//                else -> {
//                    commonDaoServices.findUserByID(permit.qamId ?: throw ExpectedDataNotFound("MISSING QAM ID")).email
//                        ?: throw ExpectedDataNotFound("MISSING QAM EMAIL")
//                }
//            }

//            qaDaoServices.sendEmailWithTaskDetails(
//                userDetailEmail,
//                permit.permitRefNumber ?: throw ExpectedDataNotFound("MISSING PERMIT REF NUMBER")
//            )

            qaDaoServices.mapAllPermitDetailsTogether(permit, null, map).let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun permitAttachUploadOrdinaryMigration(req: ServerRequest): ServerResponse {
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val auth = commonDaoServices.loggedInUserAuthentication()
            val map = commonDaoServices.serviceMapDetails(appId)
            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
//            val docFile: MultipartFile = req.paramOrNull("docFile").toMultipartData()

            var permit: PermitApplicationsEntity? = null
            permit = when {
                auth.authorities.stream()
                    .anyMatch { authority -> authority.authority == "MODIFY_COMPANY" } && auth.authorities.stream()
                    .anyMatch { authority -> authority.authority == "PERMIT_APPLICATION" } -> {
                    qaDaoServices.findPermitBYCompanyIDAndId(
                        permitID,
                        loggedInUser.companyId ?: throw ExpectedDataNotFound("MISSING COMPANY ID")
                    )
                }

                auth.authorities.stream()
                    .anyMatch { authority -> authority.authority != "MODIFY_COMPANY" } && auth.authorities.stream()
                    .anyMatch { authority -> authority.authority == "PERMIT_APPLICATION" } -> {
                    qaDaoServices.findPermitBYCompanyIDAndBranchIDAndId(
                        permitID,
                        loggedInUser.companyId ?: throw ExpectedDataNotFound("MISSING COMPANY ID"),
                        loggedInUser.plantId ?: throw ExpectedDataNotFound("MISSING PLANT ID")
                    )
                }

                else -> {
                    throw ExpectedDataNotFound("UNAUTHORISED LOGGED IN USER (ACCESS DENIED)")
                }
            }

            qaDaoServices.mapAllPermitDetailsTogether(permit, null, map).let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun permitAttachGetOrdinaryFilesListMigration(req: ServerRequest): ServerResponse {
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val auth = commonDaoServices.loggedInUserAuthentication()
            val map = commonDaoServices.serviceMapDetails(appId)
            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
//            val docFile: MultipartFile = req.paramOrNull("docFile").toMultipartData()
            val ordinaryFiles = qaDaoServices.findAllUploadedFileBYPermitIDAndOrdinarStatus(permitID, map.activeStatus)
            var permit: PermitApplicationsEntity? = null
            permit = when {
                auth.authorities.stream()
                    .anyMatch { authority -> authority.authority == "MODIFY_COMPANY" } && auth.authorities.stream()
                    .anyMatch { authority -> authority.authority == "PERMIT_APPLICATION" } -> {
                    qaDaoServices.findPermitBYCompanyIDAndId(
                        permitID,
                        loggedInUser.companyId ?: throw ExpectedDataNotFound("MISSING COMPANY ID")
                    )
                }

                auth.authorities.stream()
                    .anyMatch { authority -> authority.authority != "MODIFY_COMPANY" } && auth.authorities.stream()
                    .anyMatch { authority -> authority.authority == "PERMIT_APPLICATION" } -> {
                    qaDaoServices.findPermitBYCompanyIDAndBranchIDAndId(
                        permitID,
                        loggedInUser.companyId ?: throw ExpectedDataNotFound("MISSING COMPANY ID"),
                        loggedInUser.plantId ?: throw ExpectedDataNotFound("MISSING PLANT ID")
                    )
                }

                else -> {
                    throw ExpectedDataNotFound("UNAUTHORISED LOGGED IN USER (ACCESS DENIED)")
                }
            }

            qaDaoServices.mapAllPermitDetailsTogether(permit, null, map).let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun permitDetailsMigration(req: ServerRequest): ServerResponse {
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val auth = commonDaoServices.loggedInUserAuthentication()
            val map = commonDaoServices.serviceMapDetails(appId)
            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")

            var permit: PermitApplicationsEntity? = null
            permit = when {
                auth.authorities.stream()
                    .anyMatch { authority -> authority.authority == "MODIFY_COMPANY" } && auth.authorities.stream()
                    .anyMatch { authority -> authority.authority == "PERMIT_APPLICATION" } -> {
                    qaDaoServices.findPermitBYCompanyIDAndId(
                        permitID,
                        loggedInUser.companyId ?: throw ExpectedDataNotFound("MISSING COMPANY ID")
                    )
                }

                auth.authorities.stream()
                    .anyMatch { authority -> authority.authority != "MODIFY_COMPANY" } && auth.authorities.stream()
                    .anyMatch { authority -> authority.authority == "PERMIT_APPLICATION" } -> {
                    qaDaoServices.findPermitBYCompanyIDAndBranchIDAndId(
                        permitID,
                        loggedInUser.companyId ?: throw ExpectedDataNotFound("MISSING COMPANY ID"),
                        loggedInUser.plantId ?: throw ExpectedDataNotFound("MISSING PLANT ID")
                    )
                }

                else -> {
                    throw ExpectedDataNotFound("UNAUTHORISED LOGGED IN USER (ACCESS DENIED)")
                }
            }

//            val permit = qaDaoServices.findPermitBYUserIDAndId(permitID, loggedInUser.id ?: throw ExpectedDataNotFound("MISSING USER ID"))
            var batchDetail: Long? = null

            if (permit.sendApplication == map.activeStatus) {
                batchDetail = when {
                    permit.permitType == applicationMapProperties.mapQAPermitTypeIdFmark && permit.smarkGeneratedFrom == 1 -> {
                        val findSMarkID = qaDaoServices.findSmarkWithFmarkId(permitID).smarkId
                        val findSMark = qaDaoServices.findPermitBYCompanyIDAndId(
                            findSMarkID ?: throw Exception("NO SMARK ID FOUND WITH FMARK ID"),
                            loggedInUser.companyId ?: throw ExpectedDataNotFound("MISSING COMPANY ID")
                        )
                        qaDaoServices.findPermitInvoiceByPermitID(
                            findSMarkID
                        ).batchInvoiceNo

                    }

                    else -> {
                        qaDaoServices.findPermitInvoiceByPermitID(
                            permitID
                        ).batchInvoiceNo
                    }
                }

            }

            qaDaoServices.mapAllPermitDetailsTogether(permit, batchDetail, map).let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun permitApplySTA3Migration(req: ServerRequest): ServerResponse {
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val auth = commonDaoServices.loggedInUserAuthentication()
            val map = commonDaoServices.serviceMapDetails(appId)
            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
            var permit: PermitApplicationsEntity? = null
            permit = when {
                auth.authorities.stream()
                    .anyMatch { authority -> authority.authority == "MODIFY_COMPANY" } && auth.authorities.stream()
                    .anyMatch { authority -> authority.authority == "PERMIT_APPLICATION" } -> {
                    qaDaoServices.findPermitBYCompanyIDAndId(
                        permitID,
                        loggedInUser.companyId ?: throw ExpectedDataNotFound("MISSING COMPANY ID")
                    )
                }

                auth.authorities.stream()
                    .anyMatch { authority -> authority.authority != "MODIFY_COMPANY" } && auth.authorities.stream()
                    .anyMatch { authority -> authority.authority == "PERMIT_APPLICATION" } -> {
                    qaDaoServices.findPermitBYCompanyIDAndBranchIDAndId(
                        permitID,
                        loggedInUser.companyId ?: throw ExpectedDataNotFound("MISSING COMPANY ID"),
                        loggedInUser.plantId ?: throw ExpectedDataNotFound("MISSING PLANT ID")
                    )
                }

                else -> {
                    throw ExpectedDataNotFound("UNAUTHORISED LOGGED IN USER (ACCESS DENIED)")
                }
            }
            val dto = req.body<STA3Dto>()
            val sta3 = qaDaoServices.mapDtoSTA3AndQaSta3Entity(dto)


            //Save the sta3 details first
            val savedSta3 = qaDaoServices.sta3NewSave(
                permit.id ?: throw Exception("MISSING PERMIT ID"),
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
                permitStatus = applicationMapProperties.mapQaStatusDraft
            }

            //update the permit with the created entity values
            updatePermit = qaDaoServices.permitUpdateDetails(
                commonDaoServices.updateDetails(
                    updatePermit,
                    permit
                ) as PermitApplicationsEntity, map, loggedInUser
            ).second


            qaDaoServices.mapDtoSTA3View(savedSta3, permitID).let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }


    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun permitUpdateSTA3Migration(req: ServerRequest): ServerResponse {
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val auth = commonDaoServices.loggedInUserAuthentication()
            val map = commonDaoServices.serviceMapDetails(appId)
            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
            var permit: PermitApplicationsEntity? = null
            permit = when {
                auth.authorities.stream()
                    .anyMatch { authority -> authority.authority == "MODIFY_COMPANY" } && auth.authorities.stream()
                    .anyMatch { authority -> authority.authority == "PERMIT_APPLICATION" } -> {
                    qaDaoServices.findPermitBYCompanyIDAndId(
                        permitID,
                        loggedInUser.companyId ?: throw ExpectedDataNotFound("MISSING COMPANY ID")
                    )
                }

                auth.authorities.stream()
                    .anyMatch { authority -> authority.authority != "MODIFY_COMPANY" } && auth.authorities.stream()
                    .anyMatch { authority -> authority.authority == "PERMIT_APPLICATION" } -> {
                    qaDaoServices.findPermitBYCompanyIDAndBranchIDAndId(
                        permitID,
                        loggedInUser.companyId ?: throw ExpectedDataNotFound("MISSING COMPANY ID"),
                        loggedInUser.plantId ?: throw ExpectedDataNotFound("MISSING PLANT ID")
                    )
                }

                else -> {
                    throw ExpectedDataNotFound("UNAUTHORISED LOGGED IN USER (ACCESS DENIED)")
                }
            }
            val dto = req.body<STA3Dto>()
            val sta3 = qaDaoServices.mapDtoSTA3AndQaSta3Entity(dto)

            //Update the sta3 details first
            var sta3Found = qaDaoServices.findSTA3WithPermitIDAndRefNumber(
                permit.permitRefNumber ?: throw Exception("INVALID PERMIT REF NUMBER"), permitID
            )
            sta3.id = sta3Found.id
            sta3Found = qaDaoServices.sta3Update(
                commonDaoServices.updateDetails(sta3Found, sta3) as QaSta3Entity,
                map,
                loggedInUser
            )

            //create an entity that will update the permit transaction to the latest status
            var updatePermit = PermitApplicationsEntity()
            with(updatePermit) {
                id = permit.id
                sta3FilledStatus = map.activeStatus
                permitStatus = applicationMapProperties.mapQaStatusDraft
            }

            //update the permit with the created entity values
            updatePermit = qaDaoServices.permitUpdateDetails(
                commonDaoServices.updateDetails(
                    updatePermit,
                    permit
                ) as PermitApplicationsEntity, map, loggedInUser
            ).second


            qaDaoServices.mapDtoSTA3View(sta3Found, permitID).let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    fun updatePermitMigrated(req: ServerRequest): ServerResponse {
        return try {
            val map = commonDaoServices.serviceMapDetails(appId)
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
            val permitIdBeingMigrated = req.paramOrNull("permitIdBeingMigrated")?.toLong()
                ?: throw ExpectedDataNotFound("Required Permit ID, check config")

            val permit = qaDaoServices.findPermitBYID(permitID)
            val permitBeingUpdated = qaDaoServices.findPermitBYID(permitIdBeingMigrated)

            qaDaoServices.updatePermitWithSelectedPermit(permit.id, permitBeingUpdated.id)


//            qaDaoServices.permitInvoiceSTKPush(map, loggedInUser, dto.phoneNumber, invoiceEntity)

//            val messageDto = MigratedPermitDto(
//                invoiceEntity
//            )
            ok().body("Successful Migration")

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }


    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun permitViewSTA1Migration(req: ServerRequest): ServerResponse {
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val auth = commonDaoServices.loggedInUserAuthentication()
            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
            var permit: PermitApplicationsEntity? = null
            permit = when {
                auth.authorities.stream()
                    .anyMatch { authority -> authority.authority == "MODIFY_COMPANY" } && auth.authorities.stream()
                    .anyMatch { authority -> authority.authority == "PERMIT_APPLICATION" } -> {
                    qaDaoServices.findPermitBYCompanyIDAndId(
                        permitID,
                        loggedInUser.companyId ?: throw ExpectedDataNotFound("MISSING COMPANY ID")
                    )
                }

                auth.authorities.stream()
                    .anyMatch { authority -> authority.authority != "MODIFY_COMPANY" } && auth.authorities.stream()
                    .anyMatch { authority -> authority.authority == "PERMIT_APPLICATION" } -> {
                    qaDaoServices.findPermitBYCompanyIDAndBranchIDAndId(
                        permitID,
                        loggedInUser.companyId ?: throw ExpectedDataNotFound("MISSING COMPANY ID"),
                        loggedInUser.plantId ?: throw ExpectedDataNotFound("MISSING PLANT ID")
                    )
                }

                else -> {
                    throw ExpectedDataNotFound("UNAUTHORISED LOGGED IN USER (ACCESS DENIED)")
                }
            }

            qaDaoServices.mapDtoSTA1View(permit).let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun permitViewSTA3Migration(req: ServerRequest): ServerResponse {
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val auth = commonDaoServices.loggedInUserAuthentication()
            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
            var permit: PermitApplicationsEntity? = null
            permit = when {
                auth.authorities.stream()
                    .anyMatch { authority -> authority.authority == "MODIFY_COMPANY" } && auth.authorities.stream()
                    .anyMatch { authority -> authority.authority == "PERMIT_APPLICATION" } -> {
                    qaDaoServices.findPermitBYCompanyIDAndId(
                        permitID,
                        loggedInUser.companyId ?: throw ExpectedDataNotFound("MISSING COMPANY ID")
                    )
                }

                auth.authorities.stream()
                    .anyMatch { authority -> authority.authority != "MODIFY_COMPANY" } && auth.authorities.stream()
                    .anyMatch { authority -> authority.authority == "PERMIT_APPLICATION" } -> {
                    qaDaoServices.findPermitBYCompanyIDAndBranchIDAndId(
                        permitID,
                        loggedInUser.companyId ?: throw ExpectedDataNotFound("MISSING COMPANY ID"),
                        loggedInUser.plantId ?: throw ExpectedDataNotFound("MISSING PLANT ID")
                    )
                }

                else -> {
                    throw ExpectedDataNotFound("UNAUTHORISED LOGGED IN USER (ACCESS DENIED)")
                }
            }
            val sta3 = qaDaoServices.findSTA3WithPermitIDAndRefNumber(
                permit.permitRefNumber ?: throw Exception("INVALID PERMIT REF NUMBER"), permitID
            )

            qaDaoServices.mapDtoSTA3View(sta3, permitID).let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun permitViewInvoiceDetailsMigration(req: ServerRequest): ServerResponse {
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val map = commonDaoServices.serviceMapDetails(appId)

            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
            val permit = qaDaoServices.findPermitBYID(permitID)

            val invoiceDetails = qaDaoServices.findPermitInvoiceByPermitRefNumberANdPermitID(
                permit.permitRefNumber ?: throw ExpectedDataNotFound("MISSING PERMIT REF NUMBER"),
                permit.userId ?: throw ExpectedDataNotFound("MISSING USER ID"),
                permit.id ?: throw ExpectedDataNotFound("MISSING PERMIT ID")
            )

            qaDaoServices.permitsInvoiceDTO(invoiceDetails, permit).let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun permitApplySTA10FirmDetailsMigration(req: ServerRequest): ServerResponse {
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val auth = commonDaoServices.loggedInUserAuthentication()
            val map = commonDaoServices.serviceMapDetails(appId)
            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
            var permit: PermitApplicationsEntity? = null
            permit = when {
                auth.authorities.stream()
                    .anyMatch { authority -> authority.authority == "MODIFY_COMPANY" } && auth.authorities.stream()
                    .anyMatch { authority -> authority.authority == "PERMIT_APPLICATION" } -> {
                    qaDaoServices.findPermitBYCompanyIDAndId(
                        permitID,
                        loggedInUser.companyId ?: throw ExpectedDataNotFound("MISSING COMPANY ID")
                    )
                }

                auth.authorities.stream()
                    .anyMatch { authority -> authority.authority != "MODIFY_COMPANY" } && auth.authorities.stream()
                    .anyMatch { authority -> authority.authority == "PERMIT_APPLICATION" } -> {
                    qaDaoServices.findPermitBYCompanyIDAndBranchIDAndId(
                        permitID,
                        loggedInUser.companyId ?: throw ExpectedDataNotFound("MISSING COMPANY ID"),
                        loggedInUser.plantId ?: throw ExpectedDataNotFound("MISSING PLANT ID")
                    )
                }

                else -> {
                    throw ExpectedDataNotFound("UNAUTHORISED LOGGED IN USER (ACCESS DENIED)")
                }
            }
//            val qaSta10Entity = qaDaoServices.findSTA10WithPermitRefNumberBY(permit.permitRefNumber ?: throw ExpectedDataNotFound("INVALID PERMIT REF NUMBER"))

            val dto = req.body<STA10SectionADto>()
            var sta10 = qaDaoServices.mapDtoSTA10SectionAAndQaSta10Entity(dto)

            //Save the sta10 details first
            sta10 = qaDaoServices.sta10NewSave(
                permit,
                sta10,
                loggedInUser,
                map
            )

            //create an entity that will update the permit transaction to the latest status
            var updatePermit = PermitApplicationsEntity()
            with(updatePermit) {
                id = permit.id
                sta10FilledStatus = map.activeStatus
                permitStatus = applicationMapProperties.mapQaStatusDraft
            }

            //update the permit with the created entity values
            updatePermit = qaDaoServices.permitUpdateDetails(
                commonDaoServices.updateDetails(
                    updatePermit,
                    permit
                ) as PermitApplicationsEntity, map, loggedInUser
            ).second

            qaDaoServices.mapDtoSTA10SectionAAndQaSta10View(sta10).let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun permitUpdateSTA10FirmDetailsMigration(req: ServerRequest): ServerResponse {
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val auth = commonDaoServices.loggedInUserAuthentication()
            val map = commonDaoServices.serviceMapDetails(appId)
            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
            var permit: PermitApplicationsEntity? = null
            permit = when {
                auth.authorities.stream()
                    .anyMatch { authority -> authority.authority == "MODIFY_COMPANY" } && auth.authorities.stream()
                    .anyMatch { authority -> authority.authority == "PERMIT_APPLICATION" } -> {
                    qaDaoServices.findPermitBYCompanyIDAndId(
                        permitID,
                        loggedInUser.companyId ?: throw ExpectedDataNotFound("MISSING COMPANY ID")
                    )
                }

                auth.authorities.stream()
                    .anyMatch { authority -> authority.authority != "MODIFY_COMPANY" } && auth.authorities.stream()
                    .anyMatch { authority -> authority.authority == "PERMIT_APPLICATION" } -> {
                    qaDaoServices.findPermitBYCompanyIDAndBranchIDAndId(
                        permitID,
                        loggedInUser.companyId ?: throw ExpectedDataNotFound("MISSING COMPANY ID"),
                        loggedInUser.plantId ?: throw ExpectedDataNotFound("MISSING PLANT ID")
                    )
                }

                else -> {
                    throw ExpectedDataNotFound("UNAUTHORISED LOGGED IN USER (ACCESS DENIED)")
                }
            }
            val qaSta10Entity = qaDaoServices.findSTA10WithPermitRefNumberANdPermitID(
                permit.permitRefNumber ?: throw Exception("INVALID PERMIT REF NUMBER"), permitID
            )

            val dto = req.body<STA10SectionADto>()
            var sta10 = qaDaoServices.mapDtoSTA10SectionAAndQaSta10Entity(dto)

            with(sta10) {
                id = sta10.id
                closedProduction = map.inactiveStatus
                closedRawMaterials = map.inactiveStatus
                closedMachineryPlants = map.inactiveStatus
                closedManufacturingProcesses = map.inactiveStatus
            }
            sta10 = commonDaoServices.updateDetails(sta10, qaSta10Entity) as QaSta10Entity

            //Save the sta10 details first
            sta10 = qaDaoServices.sta10Update(sta10, map, loggedInUser)

            //create an entity that will update the permit transaction to the latest status
            var updatePermit = PermitApplicationsEntity()
            with(updatePermit) {
                id = permit.id
                sta10FilledStatus = map.activeStatus
                if (permit.sendApplication != 1) {
                    permitStatus = applicationMapProperties.mapQaStatusDraft
                }

            }

            //update the permit with the created entity values
            updatePermit = qaDaoServices.permitUpdateDetails(
                commonDaoServices.updateDetails(
                    updatePermit,
                    permit
                ) as PermitApplicationsEntity, map, loggedInUser
            ).second

            qaDaoServices.mapDtoSTA10SectionAAndQaSta10View(sta10).let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun permitViewSTA10FirmDetailsMigration(req: ServerRequest): ServerResponse {
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val auth = commonDaoServices.loggedInUserAuthentication()
            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
            var permit: PermitApplicationsEntity? = null
            permit = when {
                auth.authorities.stream()
                    .anyMatch { authority -> authority.authority == "MODIFY_COMPANY" } && auth.authorities.stream()
                    .anyMatch { authority -> authority.authority == "PERMIT_APPLICATION" } -> {
                    qaDaoServices.findPermitBYCompanyIDAndId(
                        permitID,
                        loggedInUser.companyId ?: throw ExpectedDataNotFound("MISSING COMPANY ID")
                    )
                }

                auth.authorities.stream()
                    .anyMatch { authority -> authority.authority != "MODIFY_COMPANY" } && auth.authorities.stream()
                    .anyMatch { authority -> authority.authority == "PERMIT_APPLICATION" } -> {
                    qaDaoServices.findPermitBYCompanyIDAndBranchIDAndId(
                        permitID,
                        loggedInUser.companyId ?: throw ExpectedDataNotFound("MISSING COMPANY ID"),
                        loggedInUser.plantId ?: throw ExpectedDataNotFound("MISSING PLANT ID")
                    )
                }

                else -> {
                    throw ExpectedDataNotFound("UNAUTHORISED LOGGED IN USER (ACCESS DENIED)")
                }
            }
            val qaSta10Entity = qaDaoServices.findSTA10WithPermitRefNumberANdPermitID(
                permit.permitRefNumber ?: throw Exception("INVALID PERMIT REF NUMBER"), permitID
            )

            qaDaoServices.mapDtoSTA10SectionAAndQaSta10View(qaSta10Entity).let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun permitViewSTA10AllDetailsMigration(req: ServerRequest): ServerResponse {
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val auth = commonDaoServices.loggedInUserAuthentication()
            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
            var permit: PermitApplicationsEntity? = null
            permit = when {
                auth.authorities.stream()
                    .anyMatch { authority -> authority.authority == "MODIFY_COMPANY" } && auth.authorities.stream()
                    .anyMatch { authority -> authority.authority == "PERMIT_APPLICATION" } -> {
                    qaDaoServices.findPermitBYCompanyIDAndId(
                        permitID,
                        loggedInUser.companyId ?: throw ExpectedDataNotFound("MISSING COMPANY ID")
                    )
                }

                auth.authorities.stream()
                    .anyMatch { authority -> authority.authority != "MODIFY_COMPANY" } && auth.authorities.stream()
                    .anyMatch { authority -> authority.authority == "PERMIT_APPLICATION" } -> {
                    qaDaoServices.findPermitBYCompanyIDAndBranchIDAndId(
                        permitID,
                        loggedInUser.companyId ?: throw ExpectedDataNotFound("MISSING COMPANY ID"),
                        loggedInUser.plantId ?: throw ExpectedDataNotFound("MISSING PLANT ID")
                    )
                }

                else -> {
                    throw ExpectedDataNotFound("UNAUTHORISED LOGGED IN USER (ACCESS DENIED)")
                }
            }
            val qaSta10Entity = qaDaoServices.findSTA10WithPermitRefNumberANdPermitID(
                permit.permitRefNumber ?: throw Exception("INVALID PERMIT REF NUMBER"), permitID
            )
            //Find all sta 10 personnel in charge  add
            val qaSta10ID = qaSta10Entity.id ?: throw ExpectedDataNotFound("MISSING STA 10 ID")
            val sta10Personnel =
                qaDaoServices.findPersonnelWithSTA10ID(qaSta10ID) ?: throw ExpectedDataNotFound("EMPTY RESULTS")
            val sta10Products = qaDaoServices.findProductsManufactureWithSTA10ID(qaSta10ID)
                ?: throw ExpectedDataNotFound("EMPTY RESULTS")
            val sta10Raw =
                qaDaoServices.findRawMaterialsWithSTA10ID(qaSta10ID) ?: throw ExpectedDataNotFound("EMPTY RESULTS")
            val sta10MachinePlant =
                qaDaoServices.findMachinePlantsWithSTA10ID(qaSta10ID) ?: throw ExpectedDataNotFound("EMPTY RESULTS")
            val sta10ManufacturingProcess = qaDaoServices.findManufacturingProcessesWithSTA10ID(qaSta10ID)
                ?: throw ExpectedDataNotFound("EMPTY RESULTS")
            val sta10FileList = qaDaoServices.findAllUploadedFileBYPermitIDAndSta10Status(
                permit.id ?: throw Exception("MISSING PERMIT ID"), 1
            )


            qaDaoServices.listSTA10ViewDetails(
                qaDaoServices.mapDtoSTA10SectionAAndQaSta10View(qaSta10Entity),
                qaDaoServices.listSTA10Personnel(sta10Personnel),
                qaDaoServices.listSTA10Product(sta10Products),
                qaDaoServices.listSTA10RawMaterials(sta10Raw),
                qaDaoServices.listSTA10MachinePlants(sta10MachinePlant),
                qaDaoServices.listSTA10ManufacturingProcess(sta10ManufacturingProcess),
                sta10FileList
            ).let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun permitApplySTA10ProductsBeingManufacturedMigration(req: ServerRequest): ServerResponse {
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val map = commonDaoServices.serviceMapDetails(appId)
            val qaSta10ID = req.paramOrNull("qaSta10ID")?.toLong()
                ?: throw ExpectedDataNotFound("Required QA Sta10 ID, check config")
            val qaSta10 = qaDaoServices.findSta10BYID(qaSta10ID)
            val dto = req.body<List<STA10ProductsManufactureDto>>()
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
            KotlinLogging.logger { }.error(e.message, e)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }


    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
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
            return badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun permitApplySTA10RawMaterialsMigration(req: ServerRequest): ServerResponse {
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val map = commonDaoServices.serviceMapDetails(appId)
            val qaSta10ID = req.paramOrNull("qaSta10ID")?.toLong()
                ?: throw ExpectedDataNotFound("Required QA Sta10 ID, check config")
            val qaSta10 = qaDaoServices.findSta10BYID(qaSta10ID)
            val dto = req.body<List<STA10RawMaterialsDto>>()
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
            return badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun permitApplySTA10PersonnelMigration(req: ServerRequest): ServerResponse {
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val map = commonDaoServices.serviceMapDetails(appId)
            val qaSta10ID = req.paramOrNull("qaSta10ID")?.toLong()
                ?: throw ExpectedDataNotFound("Required QA Sta10 ID, check config")
            val qaSta10 = qaDaoServices.findSta10BYID(qaSta10ID)
            val dto = req.body<List<STA10PersonnelDto>>()
            var sta10Personnel = qaDaoServices.mapDtoSTA10SectionBAndPersonnelEntity(dto)

            //Save the sta10 personnel in charge details first
            qaDaoServices.sta10PersonnelDetailsDetails(map, loggedInUser, qaSta10ID, sta10Personnel)

            //Find all sta 10 personnel in charge  add
            sta10Personnel =
                qaDaoServices.findPersonnelWithSTA10ID(qaSta10ID) ?: throw ExpectedDataNotFound("EMPTY RESULTS")

            qaDaoServices.listSTA10Personnel(sta10Personnel).let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }


    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun permitViewSTA10PersonnelMigration(req: ServerRequest): ServerResponse {
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val map = commonDaoServices.serviceMapDetails(appId)
            val qaSta10ID = req.paramOrNull("qaSta10ID")?.toLong()
                ?: throw ExpectedDataNotFound("Required QA Sta10 ID, check config")
            val qaSta10 = qaDaoServices.findSta10BYID(qaSta10ID)

            //Find all sta 10 personnel in charge  add
            val sta10Personnel =
                qaDaoServices.findPersonnelWithSTA10ID(qaSta10ID) ?: throw ExpectedDataNotFound("EMPTY RESULTS")

            qaDaoServices.listSTA10Personnel(sta10Personnel).let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }


    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
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
            return badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun permitApplySTA10MachineryAndPlantMigration(req: ServerRequest): ServerResponse {
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val map = commonDaoServices.serviceMapDetails(appId)
            val qaSta10ID = req.paramOrNull("qaSta10ID")?.toLong()
                ?: throw ExpectedDataNotFound("Required QA Sta10 ID, check config")
            val qaSta10 = qaDaoServices.findSta10BYID(qaSta10ID)
            val dto = req.body<List<STA10MachineryAndPlantDto>>()

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
            return badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
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
            return badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun permitApplySTA10ManufacturingProcessMigration(req: ServerRequest): ServerResponse {
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val map = commonDaoServices.serviceMapDetails(appId)
            val qaSta10ID = req.paramOrNull("qaSta10ID")?.toLong()
                ?: throw ExpectedDataNotFound("Required QA Sta10 ID, check config")
            val qaSta10 = qaDaoServices.findSta10BYID(qaSta10ID)
            val dto = req.body<List<STA10ManufacturingProcessDto>>()
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
            return badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
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
            return badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }


    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
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
            return badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun permitRenewMigration(req: ServerRequest): ServerResponse {
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val map = commonDaoServices.serviceMapDetails(appId)
            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
            val myRenewedPermit = qaDaoServices.permitUpdateNewWithSamePermitNumber(permitID, map, loggedInUser)

            qaDaoServices.mapAllPermitDetailsTogether(myRenewedPermit.second, null, map).let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
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
            return badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
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
            return badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun invoiceBatchSubmitMigration(req: ServerRequest): ServerResponse {
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val map = commonDaoServices.serviceMapDetails(appId)
            val dto = req.body<NewBatchInvoiceDto>()
            //Create invoice consolidation list
            var batchInvoiceDetails = qaDaoServices.permitMultipleInvoiceCalculation(map, loggedInUser, dto).second

//            find Permit Id
            val permit = dto.permitRefNumber?.let { qaDaoServices.findPermitWithPermitRefNumberLatest(it) }

            //Pass invoice Dto to Sage
            val permitType =
                qaDaoServices.findPermitType(permit?.permitType ?: throw ExpectedDataNotFound("Missing Permit Type ID"))


            //Add created invoice consolidated id to my batch id to be submitted
            val newBatchInvoiceDto = NewBatchInvoiceDto()
            with(newBatchInvoiceDto) {
                batchID =
                    batchInvoiceDetails.id ?: throw ExpectedDataNotFound("MISSING BATCH ID ON CREATED CONSOLIDATION")
            }
            KotlinLogging.logger { }.info("batch ID = ${newBatchInvoiceDto.batchID}")
            //submit to staging invoices
            batchInvoiceDetails = qaDaoServices.permitMultipleInvoiceSubmitInvoice(
                permit,
                permitType,
                map,
                loggedInUser,
                newBatchInvoiceDto
            ).second


            qaDaoServices.mapBatchInvoiceDetails(batchInvoiceDetails, loggedInUser, map).let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun invoiceBatchRemoveMigration(req: ServerRequest): ServerResponse {
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val map = commonDaoServices.serviceMapDetails(appId)
            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
            val dto = req.body<NewBatchInvoiceDto>()

            var batchInvoiceDetails = qaDaoServices.permitMultipleInvoiceRemoveInvoice(map, loggedInUser, dto).second

//            batchInvoiceDetails = qaDaoServices.permitMultipleInvoiceUpdateStagingInvoice(
//                map,
//                loggedInUser,
//                batchInvoiceDetails.id ?: throw ExpectedDataNotFound("MISSING BATCH ID")
//            ).second

            qaDaoServices.mapBatchInvoiceDetails(batchInvoiceDetails, loggedInUser, map).let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun invoiceBatchAddMigration(req: ServerRequest): ServerResponse {
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val map = commonDaoServices.serviceMapDetails(appId)
//            val permitID = req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
            val dto = req.body<NewBatchInvoiceDto>()
            //Create invoice consolidation list
            var batchInvoiceDetails = qaDaoServices.permitMultipleInvoiceCalculation(map, loggedInUser, dto).second
            //Add created invoice consolidated id to my batch id to be submitted
            val newBatchInvoiceDto = NewBatchInvoiceDto()
            with(newBatchInvoiceDto) {
                batchID =
                    batchInvoiceDetails.id ?: throw ExpectedDataNotFound("MISSING BATCH ID ON CREATED CONSOLIDATION")
            }
            KotlinLogging.logger { }.info("batch ID = ${newBatchInvoiceDto.batchID}")


//
//            batchInvoiceDetails = qaDaoServices.permitMultipleInvoiceUpdateStagingInvoice(
//                map,
//                loggedInUser,
//                batchInvoiceDetails.id ?: throw ExpectedDataNotFound("MISSING BATCH ID")
//            ).second

            qaDaoServices.mapBatchInvoiceDetails(batchInvoiceDetails, loggedInUser, map).let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun invoiceBatchDetailsMigration(req: ServerRequest): ServerResponse {
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val map = commonDaoServices.serviceMapDetails(appId)
            val batchID =
                req.paramOrNull("batchID")?.toLong() ?: throw ExpectedDataNotFound("Required batch ID, check config")
            val batchInvoiceDetails = qaDaoServices.findBatchInvoicesWithID(batchID)
            KotlinLogging.logger { }.info(":::::: BATCH INVOICE :::::::")
            qaDaoServices.mapBatchInvoiceDetails(batchInvoiceDetails, loggedInUser, map).let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun invoiceBatchDetailsBalanceMigration(req: ServerRequest): ServerResponse {
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val map = commonDaoServices.serviceMapDetails(appId)
            val batchID =
                req.paramOrNull("batchID")?.toLong() ?: throw ExpectedDataNotFound("Required batch ID, check config")
            val batchInvoiceDetails = qaDaoServices.findBatchInvoicesWithID(batchID)
            KotlinLogging.logger { }.info(":::::: BATCH INVOICE :::::::")
            qaDaoServices.mapBatchInvoiceDetailsBalance(batchInvoiceDetails, loggedInUser, map).let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun invoiceBatchDetailsPDFMigration(req: ServerRequest): ServerResponse {
        try {
            val batchID =
                req.paramOrNull("batchID")?.toLong() ?: throw ExpectedDataNotFound("Required batch ID, check config")
            qaDaoServices.getFileInvoicePDFForm(batchID).let {
                return ok().body(it)
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }


    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun permitFMARKGenerateMigration(req: ServerRequest): ServerResponse {
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val map = commonDaoServices.serviceMapDetails(appId)
            val auth = commonDaoServices.loggedInUserAuthentication()

            val dto = req.body<FmarkEntityDto>()
            val permitDetails = qaDaoServices.findPermitBYID(
                dto.smarkPermitID ?: throw ExpectedDataNotFound("Smark Permit id not found")
            )

            val fmarkGenerated = qaDaoServices.permitGenerateFmark(map, loggedInUser, permitDetails)

            qaDaoServices.mapAllPermitDetailsTogether(fmarkGenerated.second, null, map).let {
                return ok().body(it)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun certificateIssuedDetailsPDFMigration(req: ServerRequest): ServerResponse {
        try {
            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
            qaDaoServices.getFileCertificateIssuedPDFForm(permitID).let {
                return ok().body(it)
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    fun permitInvoiceListPaid(req: ServerRequest): ServerResponse {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val allpaidInvoices = qaDaoServices.findAllMyPayments(loggedInUser)


        return ok().body(allpaidInvoices)

    }
}
