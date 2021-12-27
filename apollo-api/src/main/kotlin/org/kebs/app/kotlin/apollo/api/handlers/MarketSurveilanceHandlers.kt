/*
 *
 * $$$$$$$\   $$$$$$\  $$\   $$\        $$$$$$\  $$\       $$$$$$\  $$$$$$$\   $$$$$$\  $$\
 * $$  __$$\ $$  __$$\ $$ | $$  |      $$  __$$\ $$ |     $$  __$$\ $$  __$$\ $$  __$$\ $$ |
 * $$ |  $$ |$$ /  \__|$$ |$$  /       $$ /  \__|$$ |     $$ /  $$ |$$ |  $$ |$$ /  $$ |$$ |
 * $$$$$$$\ |\$$$$$\  $$$$$  /        $$ |$$$$\ $$ |     $$ |  $$ |$$$$$$$\ |$$$$$$$$ |$$ |
 * $$  __$$\  \____$$\ $$  $$<         $$ |\_$$ |$$ |     $$ |  $$ |$$  __$$\ $$  __$$ |$$ |
 * $$ |  $$ |$$\   $$ |$$ |\$\        $$ |  $$ |$$ |     $$ |  $$ |$$ |  $$ |$$ |  $$ |$$ |
 * $$$$$$$  |\$$$$$  |$$ | \$\       \$$$$$  |$$$$$$$$\ $$$$$$  |$$$$$$$  |$$ |  $$ |$$$$$$$$\
 * \_______/  \______/ \__|  \__|       \______/ \________|\______/ \_______/ \__|  \__|\________|
 * $$$$$$$$\ $$$$$$$$\  $$$$$$\  $$\   $$\ $$\   $$\  $$$$$$\  $$\       $$$$$$\   $$$$$$\  $$$$$$\ $$$$$$$$\  $$$$$$\
 * \__$$  __|$$  _____|$$  __$$\ $$ |  $$ |$$$\  $$ |$$  __$$\ $$ |     $$  __$$\ $$  __$$\ \_$$  _|$$  _____|$$  __$$\
 *    $$ |   $$ |      $$ /  \__|$$ |  $$ |$$$$\ $$ |$$ /  $$ |$$ |     $$ /  $$ |$$ /  \__|  $$ |  $$ |      $$ /  \__|
 *    $$ |   $$$$$\    $$ |      $$$$$$$$ |$$ $$\$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |$$$$\   $$ |  $$$$$\    \$$$$$\
 *    $$ |   $$  __|   $$ |      $$  __$$ |$$ \$$$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |\_$$ |  $$ |  $$  __|    \____$$\
 *    $$ |   $$ |      $$ |  $$\ $$ |  $$ |$$ |\$$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |  $$ |  $$ |  $$ |      $$\   $$ |
 *    $$ |   $$$$$$$$\ \$$$$$  |$$ |  $$ |$$ | \$ | $$$$$$  |$$$$$$$$\ $$$$$$  |\$$$$$  |$$$$$$\ $$$$$$$$\ \$$$$$  |
 *    \__|   \________| \______/ \__|  \__|\__|  \__| \______/ \________|\______/  \______/ \______|\________| \______/
 *
 *
 *
 *
 *
 *   Copyright (c) 2020.  BSK
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.kebs.app.kotlin.apollo.api.handlers


import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.controllers.msControllers.MSController
import org.kebs.app.kotlin.apollo.api.controllers.msControllers.MSListWrapper
import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.MarketSurveillanceBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.createUserAlert
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.MarketSurveillanceDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.MasterDataDaoService
import org.kebs.app.kotlin.apollo.common.dto.ms.*
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.common.exceptions.ServiceMapNotFoundException
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.ms.ComplaintCustomersEntity
import org.kebs.app.kotlin.apollo.store.model.ms.ComplaintEntity
import org.kebs.app.kotlin.apollo.store.model.ms.ComplaintLocationEntity
import org.kebs.app.kotlin.apollo.store.model.ms.MsFuelInspectionEntity
import org.kebs.app.kotlin.apollo.store.repo.*
import org.kebs.app.kotlin.apollo.store.repo.di.ILaboratoryRepository
import org.kebs.app.kotlin.apollo.store.repo.ms.*
import org.springframework.beans.support.MutableSortDefinition
import org.springframework.beans.support.PagedListHolder
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.ServerResponse.badRequest
import org.springframework.web.servlet.function.ServerResponse.ok
import org.springframework.web.servlet.function.body
import org.springframework.web.servlet.function.paramOrNull
import java.sql.Date
import java.util.*


@Component
class MarketSurveillanceHandler(
        private val applicationMapProperties: ApplicationMapProperties,
        private val msTypesRepo: IMsTypesRepository,
        private val complainsRepo: IComplaintRepository,
        private val commonDaoServices: CommonDaoServices,
        private val daoServices: MarketSurveillanceDaoServices,
        private val masterDataDaoService: MasterDataDaoService,
        private val complaintsRepo: IComplaintRepository,
        private val surveyItemsRepo: ISurveyItemRepository,


        private val cfgRecommendationRepo: ICfgRecommendationRepository,
        private val serviceMapsRepo: IServiceMapsRepository,
        private val departmentRepo: IDepartmentsRepository,

        private val notifications: Notifications,
        private val countriesRepository: ICountriesRepository,
        private val divisionRepo: IDivisionsRepository,
        private val genarateWorkPlanRepo: IWorkPlanGenerateRepository,
        private val sampleCollectRepo: ISampleCollectionRepository,
        private val sampleCollectParameterRepo: ISampleCollectParameterRepository,
        private val sampleSubmitParameterRepo: ISampleSubmitParameterRepository,
        private val iLaboratoryRepo: ILaboratoryRepository,
        private val sampleSubmitRepo: IMSSampleSubmissionRepository,
        private val preliminaryRepo: IPreliminaryReportRepository,
        private val preliminaryOutletRepo: IPreliminaryOutletsRepository,
        private val finalReportRepo: IFinalReportRepository,
        private val dataReportRepo: IDataReportRepository,
        private val iFuelInspectionRepo: IFuelInspectionRepository,
        private val iFuelInspectionOfficerRepo: IFuelInspectionOfficerRepository,
//        private val daoServices: RegistrationDaoServices,
        private val dataReportParameterRepo: IDataReportParameterRepository,
        private val onsiteUploadRepo: IOnsiteUploadRepository,
        private val regionsRepository: IRegionsRepository,
        private val workPlanYearsCodesRepository: IWorkplanYearsCodesRepository,
        private val workPlanCreatedRepository: IWorkPlanCreatedRepository,
        private val userProfilesRepository: IUserProfilesRepository,
        private val designationRepository: IDesignationsRepository,
        private val countyRepo: ICountiesRepository,
        private val standardCategoryRepo: IStandardCategoryRepository,
        private val productCategoriesRepository: IKebsProductCategoriesRepository,
        private val broadProductCategoryRepository: IBroadProductCategoryRepository,
        private val productsRepo: IProductsRepository,
        private val productSubCategoryRepo: IProductSubcategoryRepository,
        private val sampleStandardsRepository: ISampleStandardsRepository,
        private val notificationRepo: INotificationsBufferRepository,
        private val workPlansRepo: IWorkplanRepository,
        private val complaintCustomersRepo: IComplaintCustomersRepository,
        private val usersRepo: IUserRepository,
        private val marketSurveillanceBpmn: MarketSurveillanceBpmn,
        private val userTypeRepo: IUserTypesEntityRepository,
        val townsRepo: ITownsRepository,
        val productBrandRepository: IManufacturerProductBrandRepository,
        private val msController: MSController
) {

    final val appId: Int = applicationMapProperties.mapMarketSurveillance
    private final val msAllComplaintsPage = "market-surveillance/complaints-list.html"
    private final val msHomePage = "market-surveillance/ms-home"
    private final val msComplaintsHomePage = "market-surveillance/add-new-complaint.html"
    private final val msAllWorkPlanCreatedListPage = "market-surveillance/created-workPlan-list.html"
    private final val msAllWorkPlansPage = "market-surveillance/workPlan-list.html"
    private final val msGenerateWorkPlanPage = "market-surveillance/generate-workPlan.html"
    private final val msViewWorkPlanDetailPage = "market-surveillance/workPlan-view.html"


    private final val createMsWorkPlanView = "market-survellance/officer-engineering/create_workplan"
    private final val itemsForSurveyView = "market-survellance/officer-engineering/surveyItems"
    final val msWorkPlanDetailsView = "market-survellance/workplan_details.html"
    final val createMsWorkPlanAddUserView = "market-survellance/officer-engineering/create_workplan"


    final val msComplaintsView = "market-surveillance/new-complaint.html"
    private final val msOfficerHomePage = "market-surveillance/officer-engineering-home"

    private final val msAllUploadsPage = "market-surveillance/uploads-view.html"
    private final val msPreliminaryPage = "market-surveillance/preliminary-report.html"
    private final val msFinalReportPage = "market-surveillance/final-report.html"
    private final val msChargeSheetPage = "market-surveillance/onsite-activities/charge-sheet.html"
    private final val msDataReportPage = "market-surveillance/onsite-activities/data-report.html"
    private final val msViewDataReportPage = "market-surveillance/onsite-activities/data-report-view.html"
    private final val msViewSampleCollectionPage = "market-surveillance/onsite-activities/sample-collect-view.html"
    private final val msViewSampleSubmissionPage = "market-surveillance/onsite-activities/sample-submit-view.html"
    private final val msViewLabParamsPage = "market-surveillance/onsite-activities/sample-lab-reports-view.html"
    private final val msViewPreliminaryPage = "market-surveillance/preliminary-report-view.html"
    private final val msViewFinalPage = "market-surveillance/final-report-view.html"
    private final val msSampleCollectionPage = "market-surveillance/onsite-activities/sample-collect-form.html"
    private final val msSampleSubmissionPage = "market-surveillance/onsite-activities/sample-submission-form.html"
    private final val msSeizureDeclarationPage = "market-surveillance/seizure-declaration.html"
    private final val msNotesViewPage = "market-surveillance/notes-view.html"
    private final val msInvestInspectReportPage = "market-surveillance/inspection-investigation-report.html"


    private final val msAllNotification = "market-surveillance/notification.html"
    private final val msViewComplaintPage = "market-surveillance/complaint-review.html"

    final val complaintSubmitSuccessView: String = ""
    private final val msReports = "market-survellance/officer-engineering/my_workplans.html"

    private final val errors = mutableMapOf<String, String>()

    private final val msFuelFormPage = "market-surveillance/add-new-fuel-visit.html"
    private final val msAllFuelsPage = "market-surveillance/fuel-visit-list.html"

    private final val msViewFuelDetailPage = "market-surveillance/fuel-review.html"

    final var redirectSiteComplaint = "redirect:/api/ms/all-complaints?currentPage=0&pageSize=10&listView=myTasks"

    //    api/ms/sample-submit-lab/(labParmID=${sampleSubmitParam?.id}, currentPage=0,pageSize=10,ViewType=${ViewType})
    final var redirectSiteSampleLabResults = "redirect:/api/ms/sample-submit-lab"
    final var redirectSiteSampleDetails = "redirect:/api/ms/sample-submit"
    final var redirectSiteWorkPlan = "redirect:/api/ms/all-workPlans?currentPage=0&pageSize=10&listView=myTasks"

    private final val designationID: Long = 81
    private final val myViewTask: String = "myTasks"
    private final val directorateID: Long = 2
    final var designationIDDI: Long = 23
    final var designationIDHOD: Long = 25
    final var designationIDHOF: Long = 24
    final var designationIDRM: Long = 27
    private final var designationIDIO: Long = 26
    final var designationIDMP: Long = 81
    final var designationIDIOP: Long = 82
    private final val activeStatus: Int = 1


    /*******************************************NEW MS DAO SERVICES FOR JSON OBJECTS PAGES******************************
     **************************************************************************************************************
     ***************************************************************************************************************/

    @PreAuthorize("hasAuthority('MS_IO_READ') or hasAuthority('MS_HOD_READ') or hasAuthority('MS_RM_READ') or hasAuthority('MS_HOF_READ') or hasAuthority('MS_DIRECTOR_READ')")
    fun msType(req: ServerRequest): ServerResponse {
        try {
            val map = commonDaoServices.serviceMapDetails(appId)
            daoServices.listMsTypes(map.activeStatus)
                    ?.let { return ok().body(it) }
                    ?: throw NullValueNotAllowedException("No Ms Types found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }
    }


    fun msDepartments(req: ServerRequest): ServerResponse {
        try {
            val map = commonDaoServices.serviceMapDetails(appId)
            daoServices.listMsDepartments(map.activeStatus)
                    ?.let { return ok().body(it) }
                    ?: throw NullValueNotAllowedException("No Ms Departments found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }
    }


    fun msDivisions(req: ServerRequest): ServerResponse {
        try {
//            val map = commonDaoServices.serviceMapDetails(appId)
//            val departmentID = req.pathVariable("departmentID").toLong()
            masterDataDaoService.getAllDivisions()
                    ?.let { return ok().body(it) }
                    ?: throw NullValueNotAllowedException("No Ms Departments found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }
    }


    fun msCounties(req: ServerRequest): ServerResponse {
        try {
            masterDataDaoService.getAllCounties()
                    ?.let { return ok().body(it) }
                    ?: throw NullValueNotAllowedException("No Standard Product Category found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }
    }

    fun msTowns(req: ServerRequest): ServerResponse {
        try {
            masterDataDaoService.getAllTowns()
                    ?.let { return ok().body(it) }
                    ?: throw NullValueNotAllowedException("No Standard Product Category found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }
    }

    fun msStandardsCategory(req: ServerRequest): ServerResponse {
        try {
            masterDataDaoService.getAllStandardProductCategory()
                    ?.let { return ok().body(it) }
                    ?: throw NullValueNotAllowedException("No Standard Product Category found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }
    }

    fun msProductCategories(req: ServerRequest): ServerResponse {
        try {
            masterDataDaoService.getAllProductCategories()
                    ?.let { return ok().body(it) }
                    ?: throw NullValueNotAllowedException("No Standard Product Category found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }
    }

    fun msBroadProductCategory(req: ServerRequest): ServerResponse {
        try {
            masterDataDaoService.getAllBroadProductCategory()
                    ?.let { return ok().body(it) }
                    ?: throw NullValueNotAllowedException("No Standard Product Category found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }
    }

    fun msProducts(req: ServerRequest): ServerResponse {
        try {
            masterDataDaoService.getAllProducts()
                    ?.let { return ok().body(it) }
                    ?: throw NullValueNotAllowedException("No Standard Product Category found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }
    }

    fun msProductSubcategory(req: ServerRequest): ServerResponse {
        try {
            masterDataDaoService.getAllProductSubcategory()
                    ?.let { return ok().body(it) }
                    ?: throw NullValueNotAllowedException("No Standard Product Category found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }
    }


    @PreAuthorize("hasAuthority('MS_IO_READ') or hasAuthority('MS_HOD_READ') or hasAuthority('MS_RM_READ') or hasAuthority('MS_HOF_READ') or hasAuthority('MS_DIRECTOR_READ')")
    fun msComplaintLists(req: ServerRequest): ServerResponse {
        try {
            val auth = commonDaoServices.loggedInUserAuthentication()
            val map = commonDaoServices.serviceMapDetails(appId)
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val myComplaints: List<ComplaintsDto>?
            when {
                auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_IO_READ" } -> {
                    myComplaints = loggedInUser.id?.let { it1 -> complainsRepo.findByAssignedIo(it1) }?.let { it2 -> daoServices.listMsComplaints(it2, map) }
                }
                auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_HOD_READ" } -> {
                    myComplaints = loggedInUser.id?.let { it1 -> complainsRepo.findByHodAssigned(it1) }?.let { it2 -> daoServices.listMsComplaints(it2, map) }
                }
                auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_RM_READ" } -> {
                    myComplaints = loggedInUser.id?.let { it1 -> complainsRepo.findByHodAssigned(it1) }?.let { it2 -> daoServices.listMsComplaints(it2, map) }
                }
                auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_HOF_READ" } -> {
                    myComplaints = loggedInUser.id?.let { it1 -> complainsRepo.findByHofAssigned(it1) }?.let { it2 -> daoServices.listMsComplaints(it2, map) }
                }
                auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_DIRECTOR_READ" } -> {
                    myComplaints = daoServices.listMsComplaints(complainsRepo.findAll(), map)
                }
                else -> throw ExpectedDataNotFound("Can't access this page Due to Invalid authority")
            }
            myComplaints?.let { compList ->
                return ok().body(compList)
            } ?: throw NullValueNotAllowedException("No Complaint Details found")

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }
    }

    @PreAuthorize("hasAuthority('MS_IO_READ') or hasAuthority('MS_HOD_READ') or hasAuthority('MS_RM_READ') or hasAuthority('MS_HOF_READ') or hasAuthority('MS_DIRECTOR_READ')")
    fun complaintsSearchListing(req: ServerRequest): ServerResponse {
        try {
            val dto = req.body<ComplaintSearchValues>()

            daoServices.complaintSearchResultListing(dto)?.let {
                KotlinLogging.logger { }.info("Record found ${it.count()}")
                return ok().body(it)
            }
                ?: throw NullValueNotAllowedException("Update failed")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }
    }


    @PreAuthorize("hasAuthority('MS_IO_READ') or hasAuthority('MS_HOD_READ') or hasAuthority('MS_RM_READ') or hasAuthority('MS_HOF_READ') or hasAuthority('MS_DIRECTOR_READ')")
    fun msComplaintDetails(req: ServerRequest): ServerResponse {
        return try {
            val map = commonDaoServices.serviceMapDetails(appId)
            val refNumber = req.paramOrNull("refNumber")
                ?: throw ExpectedDataNotFound("Required REF NUMBER, check config")
            val complaintDetails = daoServices.msComplaint(refNumber, map)
            ok().body(complaintDetails)
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            badRequest().body(e.message ?: "Unknown Error")
        }

    }

    @PreAuthorize("hasAuthority('MS_IO_MODIFY') or hasAuthority('MS_HOD_MODIFY') or hasAuthority('MS_RM_MODIFY') or hasAuthority('MS_HOF_MODIFY') or hasAuthority('MS_DIRECTOR_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun msComplaintUpdateApproveDetails(req: ServerRequest): ServerResponse {
        return try {
            val map = commonDaoServices.serviceMapDetails(appId)
            val refNumber = req.paramOrNull("refNumber")
                    ?: throw ExpectedDataNotFound("Required REF NUMBER, check config")
            val dto = req.body<ComplaintApproveDto>()
            val dtoBody = daoServices.msComplaintMappedDetails(map, dto, null, null, null)
            val complaintDetails = daoServices.msComplaintUpdate(refNumber, dtoBody, map)
            ok().body(complaintDetails)
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            badRequest().body(e.message ?: "Unknown Error")
        }
    }

    @PreAuthorize("hasAuthority('MS_IO_MODIFY') or hasAuthority('MS_HOD_MODIFY') or hasAuthority('MS_RM_MODIFY') or hasAuthority('MS_HOF_MODIFY') or hasAuthority('MS_DIRECTOR_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun msComplaintUpdateRejectDetails(req: ServerRequest): ServerResponse {
        return try {
            val map = commonDaoServices.serviceMapDetails(appId)
            val refNumber = req.paramOrNull("refNumber")
                    ?: throw ExpectedDataNotFound("Required REF NUMBER, check config")
            val dto = req.body<ComplaintRejectDto>()
            val dtoBody = daoServices.msComplaintMappedDetails(map, null, dto, null, null)
            val complaintDetails = daoServices.msComplaintUpdate(refNumber, dtoBody, map)
            ok().body(complaintDetails)
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            badRequest().body(e.message ?: "Unknown Error")
        }

    }


    @PreAuthorize("hasAuthority('MS_IO_MODIFY') or hasAuthority('MS_HOD_MODIFY') or hasAuthority('MS_RM_MODIFY') or hasAuthority('MS_HOF_MODIFY') or hasAuthority('MS_DIRECTOR_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun msComplaintUpdateAdviceRejectDetails(req: ServerRequest): ServerResponse {
        return try {
            val map = commonDaoServices.serviceMapDetails(appId)
            val refNumber = req.paramOrNull("refNumber")
                    ?: throw ExpectedDataNotFound("Required REF NUMBER, check config")
            val dto = req.body<ComplaintAdviceRejectDto>()
            val dtoBody = daoServices.msComplaintMappedDetails(map, null, null, dto, null)
            val complaintDetails = daoServices.msComplaintUpdate(refNumber, dtoBody, map)
            ok().body(complaintDetails)
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            badRequest().body(e.message ?: "Unknown Error")
        }

    }

    @PreAuthorize("hasAuthority('MS_IO_MODIFY') or hasAuthority('MS_HOD_MODIFY') or hasAuthority('MS_RM_MODIFY') or hasAuthority('MS_HOF_MODIFY') or hasAuthority('MS_DIRECTOR_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun msComplaintUpdateAssignIODetails(req: ServerRequest): ServerResponse {
        return try {
            val map = commonDaoServices.serviceMapDetails(appId)
            val refNumber = req.paramOrNull("refNumber")
                    ?: throw ExpectedDataNotFound("Required REF NUMBER, check config")
            val dto = req.body<ComplaintAssignDto>()
            val dtoBody = daoServices.msComplaintMappedDetails(map, null, null, null, dto)
            val complaintDetails = daoServices.msComplaintUpdate(refNumber, dtoBody, map)
            ok().body(complaintDetails)
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            badRequest().body(e.message ?: "Unknown Error")
        }

    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun msSaveNewComplaint(req: ServerRequest): ServerResponse {
        try {
            val map = commonDaoServices.serviceMapDetails(appId)
            val msType = daoServices.findMsTypeDetailsWithUuid(applicationMapProperties.mapMsComplaintTypeUuid)
            val sr: ServiceRequestsEntity
            var payload: String

            val dtoBody = req.body<NewComplaintDto>()

            var complaint = daoServices.saveNewComplaint(dtoBody.complaintDetails, msType, map, dtoBody.customerDetails)
            payload = "${this::msSaveNewComplaint.name} saved with id =[${complaint.id}]"

            val complaintCustomers = daoServices.saveNewComplaintCustomers(dtoBody.customerDetails, map, complaint)
            payload = "${payload}: File Saved [${this::msSaveNewComplaint.name} saved with id =[${complaintCustomers.id}]"

            val complaintLocation = daoServices.saveNewComplaintLocation(dtoBody.locationDetails, dtoBody.complaintDetails, map, complaint)
            payload = "${payload}: File Saved [${this::msSaveNewComplaint.name} saved with id =[${complaintLocation.id}]"

//            val documents = daoServices.complaintDocumentsSave(dtoBody.complaintFilesDetails, map, complaint)
//            payload = "${payload}: File(s) Saved [${this::msSaveNewComplaint.name} with id(s) =[${documents}]]"

            val designationsEntity = commonDaoServices.findDesignationByID(applicationMapProperties.mapMsComplaintAndWorkPlanDesignationHOD)
            val regionsEntity = complaintLocation.region?.let { commonDaoServices.findRegionEntityByRegionID(it, map.activeStatus) }
            val departmentsEntity = complaint.complaintDepartment?.let { commonDaoServices.findDepartmentByID(it) }
            val hod = departmentsEntity?.let {
                when {
                    regionsEntity != null -> {
                        commonDaoServices.findUserProfileWithDesignationRegionDepartmentAndStatus(designationsEntity, regionsEntity, it, map.activeStatus).userId
                    }
                    else -> throw ExpectedDataNotFound("No region value In the Complaint Location where [complaint ID =${complaint.id}]")
                }
            }

            val complaintID = complaint.id ?: throw  ExpectedDataNotFound("Complaint ID is missing")
            val hodID = hod?.id ?: throw  ExpectedDataNotFound("HOD user ID is missing")
            val customerEmail = complaintCustomers.emailAddress
                    ?: throw  ExpectedDataNotFound("Complaint Customers Email is missing")
            marketSurveillanceBpmn.startMSComplaintProcess(complaintID, hodID, customerEmail)
                    ?: throw  ExpectedDataNotFound("marketSurveillanceBpmn process error")

            marketSurveillanceBpmn.msSubmitComplaintComplete(complaintID, hodID)

            with(complaint) {
                hodAssigned = hod.id
            }
            val updatedComplaint = complaint
            complaint = commonDaoServices.updateDetails(updatedComplaint, complaint) as ComplaintEntity
            complaint = complaintsRepo.save(complaint)

            sr = commonDaoServices.mapServiceRequestForSuccessUserNotRegistered(map, payload, "${complaintCustomers.firstName} ${complaintCustomers.lastName}")
            val complainantEmailComposed = daoServices.complaintSubmittedDTOEmailCompose(complaint)
            val complaintReceivedEmailComposed = daoServices.complaintReceivedDTOEmailCompose(complaint, hod)
            commonDaoServices.sendEmailWithUserEmail(customerEmail, applicationMapProperties.mapMsComplaintAcknowledgementNotification, complainantEmailComposed, map, sr)
            commonDaoServices.sendEmailWithUserEntity(hod, applicationMapProperties.mapMsComplaintSubmittedHodNotification, complaintReceivedEmailComposed, map, sr)
            /**
             * TODO: Lets discuss to understand better how to keep track of schedules
             */

            complaint.let { return ok().body(MSComplaintSubmittedSuccessful("Complaint submitted successful with ref number ${it.referenceNumber} ")) }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }
    }


    /*******************************************OLD MS SERVICES FOR THYMELEAF PAGES******************************
     **************************************************************************************************************
     ***************************************************************************************************************/

    fun home(req: ServerRequest): ServerResponse =
            try {
                val map = commonDaoServices.serviceMapDetails(appId)
                commonDaoServices.loggedInUserDetails()
                req.attributes()["msTypes"] = msTypesRepo.findByStatus(map.activeStatus)
                req.attributes()["map"] = map
                ok().render(msHomePage, req.attributes())
            } catch (e: Exception) {
                createUserAlert(req, e)
            }

    fun complaintsNew(req: ServerRequest): ServerResponse =
            try {
                val map = commonDaoServices.serviceMapDetails(appId)

//                req.attributes()["pageViewLetter"] = applicationMapProperties.mapMsComplaintLetter
//                req.attributes()["pageViewOnline"] = applicationMapProperties.mapMsComplaintOnLine
                req.attributes()["msType"] = daoServices.findMsTypeDetailsWithUuid(applicationMapProperties.mapMsComplaintTypeUuid)
                req.attributes()["complaintEntity"] = ComplaintEntity()
                req.attributes()["complaintCustomersEntity"] = ComplaintCustomersEntity()
                req.attributes()["complaintDocumentsEntity"] = ComplaintDocumentsEntity()
                req.attributes()["complaintLocationEntity"] = ComplaintLocationEntity()
                req.attributes()["departments"] = daoServices.msDepartmentList(map)
                req.attributes()["counties"] = countyRepo.findByStatusOrderByCounty(map.activeStatus)
                req.attributes()["standardCategory"] = standardCategoryRepo.findByStatusOrderByStandardCategory(map.activeStatus)
                req.attributes()["maps"] = map
                ok().render(msComplaintsHomePage, req.attributes())

            } catch (e: Exception) {
                createUserAlert(req, e)
            }


    fun msList(req: ServerRequest): ServerResponse =
            try {
                val map = commonDaoServices.serviceMapDetails(appId)
                val loggedInUser = commonDaoServices.loggedInUserDetails()
                val regionID = commonDaoServices.findUserProfileByUserID(loggedInUser, map.activeStatus).regionId?.id
                val auth = commonDaoServices.loggedInUserAuthentication()
                req.paramOrNull("msTypeUuid")
                        ?.let { msTypeUuid ->
                            val msType = daoServices.findMsTypeDetailsWithUuid(msTypeUuid)
                            req.attributes()["msType"] = msType
                            when (msType.uuid) {
                                applicationMapProperties.mapMsComplaintTypeUuid -> {
                                    val myComplaints: List<ComplaintEntity>?
                                    when {
                                        auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_IO_READ" } -> {
                                            myComplaints = loggedInUser.id?.let { complainsRepo.findByAssignedIo(it) }
                                        }
                                        auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_HOD_READ" } -> {
                                            myComplaints = loggedInUser.id?.let { complainsRepo.findByHodAssigned(it) }
                                        }
                                        auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_HOF_READ" } -> {
                                            myComplaints = loggedInUser.id?.let { complainsRepo.findByHofAssigned(it) }
                                        }
                                        auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_RM_READ" } -> {
                                            myComplaints = loggedInUser.id?.let { complainsRepo.findByHodAssigned(it) }
                                        }
                                        auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_DIRECTOR_READ" } -> {
                                            myComplaints = complainsRepo.findAll()
                                        }
                                        else -> throw ExpectedDataNotFound("Can't access this page Due to Invalid authority")
                                    }
                                    req.attributes()["complaints"] = myComplaints
                                    req.attributes()["maps"] = map
                                    ok().render(msAllComplaintsPage, req.attributes())

                                }
                                applicationMapProperties.mapMsWorkPlanTypeUuid -> {
                                    val myWorkPlanCreated: List<WorkPlanCreatedEntity>?
                                    when {
                                        auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_IO_READ" } -> {
                                            myWorkPlanCreated = workPlanCreatedRepository.findByUserCreatedId(loggedInUser)
                                        }
                                        auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_HOD_READ" } -> {
                                            myWorkPlanCreated = regionID?.let { workPlanCreatedRepository.findByWorkPlanRegion(it) }
                                        }
                                        auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_HOF_READ" } -> {
                                            myWorkPlanCreated = regionID?.let { workPlanCreatedRepository.findByWorkPlanRegion(it) }
                                        }
                                        auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_RM_READ" } -> {
                                            myWorkPlanCreated = regionID?.let { workPlanCreatedRepository.findByWorkPlanRegion(it) }
                                        }
                                        auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_DIRECTOR_READ" } -> {
                                            myWorkPlanCreated = workPlanCreatedRepository.findAll()
                                        }

                                        else -> throw ExpectedDataNotFound("Cannot access this page Due to Invalid authorities")
                                    }
                                    req.attributes()["createdWorkPlans"] = myWorkPlanCreated
                                    ok().render(msAllWorkPlanCreatedListPage, req.attributes())
                                }
//                                applicationMapProperties.mapMsFuelTypeUuid -> {
//
//                                }
                                else -> throw ExpectedDataNotFound("There is no msType uuid associated with this [UUID=${msType.uuid}]")
                            }
                        }
                        ?: throw ExpectedDataNotFound("Required MS Type Uuid, check config")

            } catch (e: Exception) {
                createUserAlert(req, e)
            }

    fun viewComplaints(req: ServerRequest): ServerResponse =
            try {
                val map = commonDaoServices.serviceMapDetails(appId)
                val loggedInUser = commonDaoServices.loggedInUserDetails()
                val userProfilesEntity = commonDaoServices.findUserProfileByUserID(loggedInUser, map.activeStatus)
                val designationsEntity = commonDaoServices.findDesignationByID(applicationMapProperties.mapMsComplaintAndWorkPlanDesignationIO)
                req.paramOrNull("complaintUuid")
                        ?.let { complaintUuid ->
                            val complaintDetails = daoServices.findComplaintByUuid(complaintUuid)
                            val departmentsEntity = complaintDetails.complaintDepartment?.let { commonDaoServices.findDepartmentByID(it) }
                            val divisionsList = departmentsEntity?.let { commonDaoServices.findDivisionByDepartmentId(it, map.activeStatus) }

                            val regionsEntity = userProfilesEntity.regionId
                            val complaintLocationDetails = complaintDetails.id?.let { daoServices.findComplaintLocationByComplaintID(it) }
                            val complaintCustomersDetails = complaintDetails.id?.let { daoServices.findComplaintCustomerByComplaintID(it) }
                            val msInspectionOfficer = regionsEntity?.let {
                                when {
                                    departmentsEntity != null -> {
                                        commonDaoServices.findUserProfileListWithRegionDesignationDepartmentAndStatus(it, designationsEntity, departmentsEntity, map.activeStatus)
                                    }
                                    else -> throw ExpectedDataNotFound("complaintDepartment is null on Complaint Details")
                                }
                            }

                            req.attributes().putAll(commonDaoServices.loadCommonUIComponents(map))
                            req.attributes()["complaintEntity"] = complaintDetails
                            req.attributes()["complaintCustomersEntity"] = complaintCustomersDetails
                            req.attributes()["complaintLocationEntity"] = complaintLocationDetails
                            req.attributes()["complaintRemarksEntity"] = ComplaintRemarksEntity()
                            req.attributes()["msWorkPlanGeneratedEntity"] = MsWorkPlanGeneratedEntity()
                            req.attributes()["officers"] = msInspectionOfficer
                            req.attributes()["divisions"] = divisionsList
                            req.attributes()["maps"] = map
                            req.attributes()["standardCategory"] = standardCategoryRepo.findByIdOrNull(complaintDetails.standardCategory)
                            req.attributes()["productCategory"] = productCategoriesRepository.findByIdOrNull(complaintDetails.productCategory)
                            req.attributes()["broadProductCategory"] = broadProductCategoryRepository.findByIdOrNull(complaintDetails.broadProductCategory)
                            req.attributes()["product"] = productsRepo.findByIdOrNull(complaintDetails.product)
                            req.attributes()["productSubCategory"] = productSubCategoryRepo.findByIdOrNull(complaintDetails.productSubCategory)
                            req.attributes()["ksApplicable"] = sampleStandardsRepository.findBySubCategoryId(complaintDetails.productSubCategory)
                            complaintLocationDetails?.county?.let { complaintLocationDetails.town?.let { it1 -> loadCommonUIComponents(it, it1, map) } }?.let { req.attributes().putAll(it) }
                            ok().render(msViewComplaintPage, req.attributes())

                        }
                        ?: throw ExpectedDataNotFound("Complaint [id=${req.paramOrNull("complaintId")}] missing, Check Config")


            } catch (e: Exception) {
                createUserAlert(req, e)
            }

    fun workPlanCreationNew(req: ServerRequest): ServerResponse {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val currentYear = daoServices.getCurrentYear()
        req.paramOrNull("msTypeUuid")
                ?.let { msTypeUuid ->
                    val msType = daoServices.findMsTypeDetailsWithUuid(msTypeUuid)
                    val workPlanYearCodes = daoServices.findWorkPlanYearsCodesEntity(currentYear, map)
                    val userWorkPlan = daoServices.findWorkPlanCreatedEntity(loggedInUser, workPlanYearCodes)
                    val checkCreationDate = daoServices.isWithinRange(commonDaoServices.getCurrentDate(), workPlanYearCodes)
                    when {
                        checkCreationDate -> {
                            when (userWorkPlan) {
                                null -> {
                                    daoServices.createWorkPlanYear(loggedInUser, map, workPlanYearCodes)
                                    return ok().render(daoServices.redirectSiteComplaintPage + "${msType.uuid}")
                                }
                                else -> {
                                    throw ExpectedDataNotFound("You already have a work plan for this year")
                                }
                            }
                        }
                        else -> {
                            throw ExpectedDataNotFound("You Cannot create a workPlan due to the current date of the Year")
                        }
                    }

                }
                ?: throw ServiceMapNotFoundException("Missing ms Type Uuid, recheck configuration")
    }

    fun allWorkPlanCreatedList(req: ServerRequest): ServerResponse {
        val map = commonDaoServices.serviceMapDetails(appId)

        req.paramOrNull("createdWorkPlanUuid")
                ?.let { createdWorkPlanUuid ->
                    val createdWorkPlan = daoServices.findCreatedWorkPlanWIthUuid(createdWorkPlanUuid)
                    val workPlanList = daoServices.findALlWorkPlanDetailsAssociatedWithWorkPlanID(createdWorkPlan.id)
                    req.attributes()["workPlans"] = workPlanList
//                    req.attributes().putAll(loadCommonCreatedWorkPlanUIComponents(createdWorkPlan.id))
                    req.attributes()["createdWorkPlan"] = createdWorkPlan
                    req.attributes()["maps"] = map
                    return ok().render(msAllWorkPlansPage, req.attributes())

                }
                ?: throw ServiceMapNotFoundException("Missing created WorkPlan Uuid, recheck configuration")
    }

    fun loadWorkPlanForm(req: ServerRequest): ServerResponse {

        val map = commonDaoServices.serviceMapDetails(appId)
        req.paramOrNull("createdWorkPlanUuid")
                ?.let { createdWorkPlanUuid ->
                    req.attributes()["counties"] = countyRepo.findByStatusOrderByCounty(map.activeStatus)
                    req.attributes()["msWorkPlanGeneratedEntity"] = MsWorkPlanGeneratedEntity()
                    req.attributes()["standardCategory"] = standardCategoryRepo.findByStatusOrderByStandardCategory(map.activeStatus)
                    req.attributes()["regions"] = regionsRepository.findByStatusOrderByRegion(map.activeStatus)
                    req.attributes()["workPlanYears"] = workPlanYearsCodesRepository.findByStatusOrderByYearName(map.activeStatus)
                    req.attributes()["departments"] = daoServices.msDepartmentList(map)
                    req.attributes()["maps"] = map
                    req.attributes()["createdWorkPlan"] = daoServices.findCreatedWorkPlanWIthUuid(createdWorkPlanUuid)
                    return ok().render(msGenerateWorkPlanPage, req.attributes())
                }
                ?: throw ServiceMapNotFoundException("Missing created WorkPlan Uuid, recheck configuration")
    }


    fun viewWorkPlans(req: ServerRequest): ServerResponse {
        val map = commonDaoServices.serviceMapDetails(appId)
        req.paramOrNull("workPlanUuid")
                ?.let { workPlanUuid ->
                    val workPlanDetails = daoServices.findWorkPlanActivityWithUuid(workPlanUuid)
                    req.attributes()["msWorkPlanGeneratedEntity"] = MsWorkPlanGeneratedEntity()
                    req.attributes()["onSiteUploadsEntity"] = MsOnSiteUploadsEntity()
                    req.attributes()["workPlan"] = workPlanDetails
                    req.attributes()["maps"] = map
                    workPlanDetails.county?.let { workPlanDetails.townMarketCenter?.let { it1 -> loadCommonUIComponents(it, it1, map) } }?.let { req.attributes().putAll(it) }
                    req.attributes()["departmentDetails"] = departmentRepo.findByIdOrNull(workPlanDetails.complaintDepartment)
                    req.attributes()["divisionDetails"] = divisionRepo.findByIdOrNull(workPlanDetails.divisionId)
                    if (workPlanDetails.complaintId != null) {
                        req.attributes()["complaintDetails"] = daoServices.assignComplaintDetails(workPlanDetails.complaintId)
                    }
//                    req.attributes()["officerDetails"] = usersRepo.findByIdOrNull(workPlanDetails.officerId)
                    req.attributes()["standardCategory"] = standardCategoryRepo.findByIdOrNull(workPlanDetails.standardCategory)
                    req.attributes()["productCategory"] = productCategoriesRepository.findByIdOrNull(workPlanDetails.productCategory)
                    req.attributes()["broadProductCategory"] = broadProductCategoryRepository.findByIdOrNull(workPlanDetails.broadProductCategory)
                    req.attributes()["product"] = productsRepo.findByIdOrNull(workPlanDetails.product)
                    req.attributes()["productSubCategory"] = productSubCategoryRepo.findByIdOrNull(workPlanDetails.productSubCategory)
                    req.attributes()["ksApplicable"] = sampleStandardsRepository.findBySubCategoryId(workPlanDetails.productSubCategory)
                    req.attributes()["ViewType"] = "workPlan"
                    return ok().render(msViewWorkPlanDetailPage, req.attributes())

                }
                ?: throw ServiceMapNotFoundException("Missing created WorkPlan Uuid, recheck configuration")
    }

    fun workPlansAddingFinished(req: ServerRequest): ServerResponse {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        req.paramOrNull("createdWorkPlanUuid")
                ?.let { createdWorkPlanUuid ->
                    val createdWorkPlan = daoServices.findCreatedWorkPlanWIthUuid(createdWorkPlanUuid)
                    with(createdWorkPlan) {
                        endedDate = commonDaoServices.getCurrentDate()
                        endedStatus = map.activeStatus
                    }

                    daoServices.updateCreatedWorkPlan(createdWorkPlan, loggedInUser, map.inactiveStatus)
                    val allWorkPlans = daoServices.findAllWorkPlansWithCreatedWorkPlanID(createdWorkPlan.id)
//                    req.attributes().putAll(loadCommonCreatedWorkPlanUIComponents(createdWorkPlanID.toLong()))
                    req.attributes()["workPlans"] = allWorkPlans
                    req.attributes()["createdWorkPlan"] = createdWorkPlan
                    req.attributes()["maps"] = map
                    return ok().render(msAllWorkPlansPage, req.attributes())
                }
                ?: throw ServiceMapNotFoundException("Missing created WorkPlan Uuid, recheck configuration")

    }

    fun allWorkPlansApprove(req: ServerRequest): ServerResponse {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        req.paramOrNull("createdWorkPlanUuid")
                ?.let { createdWorkPlanUuid ->
                    val createdWorkPlan = daoServices.findCreatedWorkPlanWIthUuid(createdWorkPlanUuid)
                    val allWorkPlans = daoServices.findAllWorkPlansWithCreatedWorkPlanID(createdWorkPlan.id)
                    daoServices.approveAllWorkPlans(allWorkPlans, loggedInUser, map)
                    daoServices.updateCreatedWorkPlan(createdWorkPlan, loggedInUser, map.activeStatus)
                    req.attributes()["workPlans"] = allWorkPlans
                    req.attributes()["createdWorkPlan"] = createdWorkPlan
                    req.attributes()["maps"] = map
                    return ok().render(msAllWorkPlansPage, req.attributes())
                }
                ?: throw ServiceMapNotFoundException("Missing created WorkPlan Uuid, recheck configuration")
    }

    fun allWorkPlansReject(req: ServerRequest): ServerResponse {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        req.paramOrNull("createdWorkPlanUuid")
                ?.let { createdWorkPlanUuid ->
                    val createdWorkPlan = daoServices.findCreatedWorkPlanWIthUuid(createdWorkPlanUuid)
                    val allWorkPlans = daoServices.findAllWorkPlansWithCreatedWorkPlanID(createdWorkPlan.id)
                    daoServices.rejectAllWorkPlans(allWorkPlans, loggedInUser, map)
                    daoServices.updateCreatedWorkPlan(createdWorkPlan, loggedInUser, map.inactiveStatus)
                    req.attributes()["workPlans"] = allWorkPlans
                    req.attributes()["createdWorkPlan"] = createdWorkPlan
                    req.attributes()["maps"] = map
                    return ok().render(msAllWorkPlansPage, req.attributes())
                }
                ?: throw ServiceMapNotFoundException("Missing created WorkPlan Uuid, recheck configuration")
    }


    fun uploadsView(req: ServerRequest): ServerResponse {
        val map = commonDaoServices.serviceMapDetails(appId)
        req.paramOrNull("workPlanUuid")
                ?.let { workPlanUuid ->
                    val workPlanDetails = daoServices.findWorkPlanActivityWithUuid(workPlanUuid)
                    req.attributes()["uploads"] = daoServices.findALlOnsiteUploadsWithActivityID(workPlanDetails, map)
                    req.attributes()["maps"] = map
                    return ok().render(msAllUploadsPage, req.attributes())
                }
                ?: throw ServiceMapNotFoundException("Missing created WorkPlan Uuid, recheck configuration")
    }

    private fun loadCommonUIComponents(countyID: Long, townID: Long, map: ServiceMapsEntity): MutableMap<String, Any> {
        return mutableMapOf(
                Pair("countyName", commonDaoServices.findCountiesEntityByCountyId(countyID, map.activeStatus)),
                Pair("townDetails", commonDaoServices.findTownEntityByTownId(townID))
        )
    }

    fun officerHome(req: ServerRequest): ServerResponse = ok().render(msOfficerHomePage)

    fun reviewComplaint(req: ServerRequest): ServerResponse =
            serviceMapsRepo.findByIdAndStatus(req.pathVariable("appId").toIntOrNull(), activeStatus)
                    ?.let { map ->
                        complainsRepo.findByIdOrNull(req.pathVariable("complaintId").toLongOrNull())
                                ?.let { complain ->
                                    req.attributes()["complaintEntity"] = complain
                                    req.attributes()["map"] = map
                                    return ok().render(msComplaintsHomePage, req.attributes())
                                }
                                ?: throw ExpectedDataNotFound("Complaint [id=${req.pathVariable("complaintId")}] does not exist")
                    }
                    ?: throw ServiceMapNotFoundException("Missing application mapping for [id=$appId], recheck configuration")


    fun fuelInspectionNew(req: ServerRequest): ServerResponse =
            serviceMapsRepo.findByIdAndStatus(appId, activeStatus)
                    ?.let { map ->
//                        designationRepository.findByDesignationNameAndStatus("MS IO", map.activeStatus)
//                                ?.let { designationsEntity ->
//                                    userProfilesRepository.findByDesignationIdAndStatus(designationsEntity, map.activeStatus)
//                                            ?.let { msInspectionOfficer ->
                        req.attributes()["counties"] = countyRepo.findByStatusOrderByCounty(map.activeStatus)
                        req.attributes()["fuelEntity"] = MsFuelInspectionEntity()
                        req.attributes()["fuelOfficerInspectEntity"] = MsFuelInspectionOfficersEntity()
//                                                req.attributes()["officers"] = msInspectionOfficer

                        req.attributes()["regions"] = regionsRepository.findByStatusOrderByRegion(map.activeStatus)

                        req.attributes()["maps"] = map
                        return ok().render(msFuelFormPage, req.attributes())
//                                            }
//
//                                }

                    }
                    ?: throw ServiceMapNotFoundException("Missing application mapping for [id=$appId], recheck configuration")


//    fun allComplaints(req: ServerRequest): ServerResponse =
//            serviceMapsRepo.findByIdAndStatus(appId, activeStatus)
//                    ?.let { map ->
//                        SecurityContextHolder.getContext().authentication?.name
//                                ?.let { username ->
//                                    usersRepo.findByUserName(username)
//                                            ?.let { loggedInUser ->
//                                                loggedInUser.id
//                                                        ?.let { userId ->
//                                                            when {
//                                                                req.paramOrNull("listView").equals("myTasks") -> {
//                                                                    var myComplaints: List<ComplaintEntity>? = null
//                                                                    commonDaoServices.loggedInUserAuthentication().let { auth ->
//                                                                        when {
//                                                                            auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_IO_READ" } -> {
//                                                                                myComplaints = complainsRepo.findByAssignedIo(loggedInUser)
//                                                                            }
//                                                                            auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_HOD_READ" } -> {
//                                                                                myComplaints = complainsRepo.findByHodAssigned(loggedInUser)
//                                                                            }
//                                                                            auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_HOF_READ" } -> {
//                                                                                myComplaints = complainsRepo.findByHofAssigned(loggedInUser)
//                                                                            }
//                                                                            auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_RM_READ" } -> {
//                                                                                myComplaints = complainsRepo.findByHodAssigned(loggedInUser)
//                                                                            }
//                                                                            auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_DIRECTOR_READ" } -> {
//                                                                                myComplaints = complainsRepo.findAll()
//                                                                            }
//                                                                            else -> throw ExpectedDataNotFound("can't access this page Due to Invalid authority")
//                                                                        }
//                                                                    }
//                                                                    req.attributes()["complaints"] = myComplaints
//                                                                    req.attributes()["ViewType"] = "myTasks"
//                                                                }
////                                                                req.paramOrNull("listView").equals("myTasks") -> {
////                                                                    marketSurveillanceBpmn.fetchAllTasksByAssignee(userId)
////                                                                            ?.let { listTaskDetails ->
////                                                                                val tasks = mutableListOf<ComplaintEntity?>()
////                                                                                listTaskDetails.sortedByDescending { it.objectId }
////                                                                                        .forEach { taskDetails ->
////                                                                                            if (complainsRepo.findByIdAndMsProcessStatus(taskDetails.objectId, map.inactiveStatus) != null) {
////                                                                                                tasks.add(complainsRepo.findByIdAndMsProcessStatus(taskDetails.objectId, map.inactiveStatus))
////                                                                                            }
////                                                                                        }
////                                                                                req.attributes()["complaints"] = tasks
////                                                                                req.attributes()["ViewType"] = "myTasks"
////                                                                            }
////                                                                            ?: throw ExpectedDataNotFound("The user with [id=$userId], does not have tasks")
////                                                                }
//                                                            }
//                                                            req.attributes()["maps"] = map
//                                                            ok().render(msAllComplaintsPage, req.attributes())
//
//
//                                                        }
//
//                                            }
//                                }
//                    }
//                    ?: throw ServiceMapNotFoundException("Missing application mapping for [id=$appId], recheck configuration")

    fun allFuels(req: ServerRequest): ServerResponse =
            serviceMapsRepo.findByIdAndStatus(appId, activeStatus)
                    ?.let { map ->
                        SecurityContextHolder.getContext().authentication?.name
                                ?.let { username ->
                                    usersRepo.findByUserName(username)
                                            ?.let { loggedInUser ->
                                                req.paramOrNull("currentPage")
                                                        ?.let { currentPage ->
                                                            req.paramOrNull("pageSize")
                                                                    ?.let { pageSize ->
                                                                        PageRequest.of(currentPage.toInt(), pageSize.toInt())
                                                                                .let { page ->
                                                                                    loggedInUser.id
                                                                                            ?.let { userId ->
                                                                                                marketSurveillanceBpmn.fetchAllTasksByAssignee(userId)
                                                                                                        ?.let { listTaskDetails ->
                                                                                                            val tasks = mutableListOf<MsFuelInspectionEntity?>()
                                                                                                            listTaskDetails.sortedByDescending { it.objectId }
                                                                                                                    .forEach { taskDetails ->
                                                                                                                        if (iFuelInspectionRepo.findByIdOrNull(taskDetails.objectId) != null) {
                                                                                                                            tasks.add(iFuelInspectionRepo.findByIdOrNull(taskDetails.objectId))
                                                                                                                        }
                                                                                                                    }
                                                                                                            // Creation
                                                                                                            val listPage: PagedListHolder<*> = PagedListHolder<MsFuelInspectionEntity?>(tasks, MutableSortDefinition(false))
                                                                                                            listPage.pageSize = page.pageSize // number of items per page
                                                                                                            listPage.page = page.pageNumber


//                                                                                                            req.attributes()["complaints"] = tasks
                                                                                                            when {
                                                                                                                req.paramOrNull("listView").equals("allFuel") -> {
                                                                                                                    req.attributes()["fuels"] = iFuelInspectionRepo.findAllByOrderByIdDesc(page)
                                                                                                                    req.attributes()["ViewType"] = "allFuel"

                                                                                                                }
                                                                                                                req.paramOrNull("listView").equals("myTasks") -> {
                                                                                                                    req.attributes()["fuels"] = listPage.pageList
                                                                                                                    req.attributes()["ViewType"] = "myTasks"
                                                                                                                }
                                                                                                            }
                                                                                                            req.attributes()["maps"] = map
                                                                                                            ok().render(msAllFuelsPage, req.attributes())


                                                                                                        }

                                                                                            }

                                                                                }
                                                                    }
                                                        }
                                            }
                                }
                    }
                    ?: throw ServiceMapNotFoundException("Missing application mapping for [id=$appId], recheck configuration")


//    fun viewFuelDetails(req: ServerRequest): ServerResponse =
//            serviceMapsRepo.findByIdAndStatus(appId, activeStatus)
//                    ?.let { map ->
//                        req.paramOrNull("fuelInspectId")
//                                ?.let { fuelInspectId ->
//                                    req.paramOrNull("ViewType")
//                                            ?.let { ViewType ->
//                                                SecurityContextHolder.getContext().authentication?.name
//                                                        ?.let { username ->
//                                                            usersRepo.findByUserName(username)
//                                                                    ?.let {
////                                                            userProfilesRepository.findByUserIdAndStatus(loggedInUser, map.activeStatus)
////                                                                    ?.let { userProfilesEntity ->
//                                                                        iFuelInspectionRepo.findByIdOrNull(fuelInspectId.toLong())
//                                                                                ?.let { fuelInspectDetails ->
////                                                                        iFuelInspectionOfficerRepo.findByMsFuelInspectionId(fuelInspectDetails)
////                                                                                ?.let { msFuelInspectionOfficersEntity ->
//                                                                                    designationRepository.findByIdOrNull(designationIDIOP)
//                                                                                            ?.let { designationsEntity ->
//                                                                                                fuelInspectDetails.regionId?.let {
//                                                                                                    userProfilesRepository.findByRegionIdAndDesignationIdAndStatus(it, designationsEntity, map.activeStatus)
//                                                                                                            ?.let { msInspectionOfficer ->
//                                                                                                                sampleCollectRepo.findByMsFuelInspectionId(fuelInspectDetails)
//                                                                                                                        ?.let { fetchedSampleCollect ->
//                                                                                                                            req.attributes()["sampCollect"] = fetchedSampleCollect
//                                                                                                                        }
//
//                                                                                                                //                                                                                    preliminaryRepo.findByWorkPlanGeneratedID(workPlanDetails)
//                                                                                                                //                                                                                            ?.let { preliminaryReportDetails ->
//                                                                                                                //                                                                                                req.attributes()["preliminaryReport"] = preliminaryReportDetails
//                                                                                                                //                                                                                            }
//                                                                                                                //                                                                                    req.attributes()["msWorkPlanGeneratedEntity"] = MsWorkplanGeneratedEntity()
//                                                                                                                req.attributes()["fuelEntity"] = MsFuelInspectionEntity()
//                                                                                                                req.attributes()["fuelRemediationEntity"] = MsFuelRemediationEntity()
//                                                                                                                req.attributes()["fuelRemediationInvoiceEntity"] = MsFuelRemedyInvoicesEntity()
//                                                                                                                req.attributes()["fuelOfficerInspectEntity"] = MsFuelInspectionOfficersEntity()
//                                                                                                                req.attributes()["fuel"] = fuelInspectDetails
//                                                                                                                req.attributes()["ViewType"] = ViewType
//                                                                                                                req.attributes()["officers"] = msInspectionOfficer
//                                                                                                                req.attributes()["maps"] = map
//                                                                                                                return ok().render(msViewFuelDetailPage, req.attributes())
//                                                                                                            }
//                                                                                                }
//                                                                                            }
////                                                                                }
////                                                                                ?: throw ExpectedDataNotFound("Fuel To inspect officer Details with the following [id=${req.paramOrNull("fuelInspectId")}] does not exist")
//
//
//                                                                                }
//                                                                                ?: throw ExpectedDataNotFound("Fuel To inspect with the following [id=${req.paramOrNull("fuelInspectId")}] does not exist")
//
////                                                                    }
//                                                                    }
//                                                                    ?: throw ExpectedDataNotFound("User Logged in does not exist in the system")
//                                                        }
//                                                        ?: throw ExpectedDataNotFound("NO user has Logged In")
//                                            }
//                                            ?: throw ExpectedDataNotFound("ViewType  Was not passed e.g allComplaint, my tasks")
//
//                                }
//                                ?: throw ExpectedDataNotFound("fuel Inspect Id [id=${req.paramOrNull("fuelInspectId")}] does not exist")
//                    }
//                    ?: throw ServiceMapNotFoundException("Missing application mapping for [id=$appId], recheck configuration")


    fun viewOnsiteButtons(req: ServerRequest): ServerResponse =
            serviceMapsRepo.findByIdAndStatus(appId, activeStatus)
                    ?.let { map ->
                        req.paramOrNull("workPlanID")
                                ?.let { workPlanID ->
                                    req.paramOrNull("docType")
                                            ?.let { docType ->
                                                genarateWorkPlanRepo.findByIdOrNull(workPlanID.toLong())
                                                        ?.let { fetchedWorkPlanEntity ->
                                                            when (docType) {
                                                                "onSiteStart" -> {
                                                                    fetchedWorkPlanEntity.onsiteStartStatus = map.activeStatus
                                                                    fetchedWorkPlanEntity.onsiteEndStatus = map.inactiveStatus
                                                                    fetchedWorkPlanEntity.onsiteStartDate = Date(Date().time)

                                                                    ok().body(genarateWorkPlanRepo.save(fetchedWorkPlanEntity))
                                                                }
                                                                "onSiteEnd" -> {
                                                                    fetchedWorkPlanEntity.id?.let { fetchedWorkPlanID ->
                                                                        sampleSubmitRepo.findByWorkPlanGeneratedID(fetchedWorkPlanEntity)
                                                                                ?.let {
                                                                                    //                                                                                    fetchedWorkPlanEntity.id?.let { it1 ->

                                                                                    marketSurveillanceBpmn.msMsOnsiteActivitiesComplete(fetchedWorkPlanID)
                                                                                            .let {
                                                                                                marketSurveillanceBpmn.msMsSubmitLabSamplesComplete(fetchedWorkPlanID)
                                                                                                        .let {
                                                                                                            fetchedWorkPlanEntity.sendSffStatus = map.activeStatus
                                                                                                            fetchedWorkPlanEntity.onsiteEndStatus = map.activeStatus
                                                                                                            fetchedWorkPlanEntity.onsiteEndDate = Date(Date().time)
                                                                                                            fetchedWorkPlanEntity.sendSffDate = Date(Date().time)
                                                                                                            ok().body(genarateWorkPlanRepo.save(fetchedWorkPlanEntity))

                                                                                                        }
                                                                                                //                                                                                        }

                                                                                            }

                                                                                }
                                                                    }

                                                                }
                                                                else -> {
                                                                    throw ExpectedDataNotFound("Data should not be empty")
                                                                }
                                                            }

                                                        }

                                            }
                                }
                    }
                    ?: throw ServiceMapNotFoundException("Missing application mapping for [id=$appId], recheck configuration")

    fun viewDataReportSeizedGoods(req: ServerRequest): ServerResponse =
            serviceMapsRepo.findByIdAndStatus(appId, activeStatus)
                    ?.let { map ->
                        req.paramOrNull("workPlanID")
                                ?.let { workPlanID ->
                                    var pageLoad = msViewDataReportPage
                                    genarateWorkPlanRepo.findByIdOrNull(workPlanID.toLong())
                                            ?.let { fetchedWorkPlanEntity ->
                                                dataReportRepo.findByWorkPlanGeneratedID(fetchedWorkPlanEntity)
                                                        ?.let { fetchedDataReport ->
                                                            dataReportParameterRepo.findByDataReportId(fetchedDataReport)
                                                                    .let { dataReportParams ->

                                                                        if (req.paramOrNull("docType").equals("close")) {
                                                                            fetchedWorkPlanEntity.dataReportGoodsStatus = map.activeStatus
                                                                            genarateWorkPlanRepo.save(fetchedWorkPlanEntity)
                                                                            pageLoad = daoServices.redirectSiteMsWorkPlanActivityPage + "${fetchedWorkPlanEntity.uuid}"
                                                                            return ok().render(pageLoad)
                                                                        }
                                                                        req.attributes()["dataReport"] = fetchedDataReport
                                                                        req.attributes()["dataReportParams"] = dataReportParams
                                                                        req.attributes()["dataReportParamEntity"] = MsDataReportParametersEntity()

                                                                    }
                                                        }
                                                ok().render(pageLoad, req.attributes())
                                            }


                                }
                    }
                    ?: throw ServiceMapNotFoundException("Missing application mapping for [id=$appId], recheck configuration")

    fun viewSampleCollect(req: ServerRequest): ServerResponse =
            serviceMapsRepo.findByIdAndStatus(appId, activeStatus)
                    ?.let { map ->
                        var pageLoad = msViewSampleCollectionPage
                        req.paramOrNull("fuelInspectID")
                                ?.let { fuelInspectID ->
                                    iFuelInspectionRepo.findByIdOrNull(fuelInspectID.toLong())
                                            ?.let { msFuelInspectionEntity ->
                                                sampleCollectRepo.findByMsFuelInspectionId(msFuelInspectionEntity)
                                                        ?.let { fetchedSampleCollect ->
                                                            sampleCollectParameterRepo.findBySampleCollectionId(fetchedSampleCollect)
                                                                    .let { sampleCollectParams ->
                                                                        req.paramOrNull("ViewType")
                                                                                ?.let { ViewType ->
                                                                                    if (req.paramOrNull("docType").equals("close")) {
                                                                                        msFuelInspectionEntity.scfLabparamsStatus = map.activeStatus
                                                                                        iFuelInspectionRepo.save(msFuelInspectionEntity)
                                                                                        marketSurveillanceBpmn.msFmFillSampleCollectionFormComplete(msFuelInspectionEntity.id)
                                                                                        pageLoad = "/api/ms/all-fuels?currentPage=0&pageSize=10&listView=myTasks"

                                                                                    }
                                                                                    req.attributes()["ViewType"] = ViewType
                                                                                    req.attributes()["detailID"] = fetchedSampleCollect.msFuelInspectionId?.id
                                                                                    req.attributes()["sampleCollect"] = fetchedSampleCollect
                                                                                    req.attributes()["sampleCollectParams"] = sampleCollectParams
                                                                                    req.attributes()["sampleCollectParamsEntity"] = MsCollectionParametersEntity()
                                                                                    return ok().render(pageLoad, req.attributes())
                                                                                }
                                                                    }
                                                        }

                                            }
                                }
                        req.paramOrNull("workPlanID")
                                ?.let { workPlanID ->
                                    genarateWorkPlanRepo.findByIdOrNull(workPlanID.toLong())
                                            ?.let { fetchedWorkPlanEntity ->
                                                sampleCollectRepo.findByWorkPlanGeneratedID(fetchedWorkPlanEntity)
                                                        ?.let { fetchedSampleCollect ->
                                                            sampleCollectParameterRepo.findBySampleCollectionId(fetchedSampleCollect)
                                                                    .let { sampleCollectParams ->
                                                                        if (req.paramOrNull("docType").equals("close")) {
                                                                            fetchedWorkPlanEntity.scfLabparamsStatus = map.activeStatus
                                                                            genarateWorkPlanRepo.save(fetchedWorkPlanEntity)
                                                                            pageLoad = daoServices.redirectSiteMsWorkPlanActivityPage + "${fetchedWorkPlanEntity.uuid}"
                                                                        }
//                                                                                    req.attributes()["ViewType"] = ViewType
                                                                        req.attributes()["detailID"] = fetchedSampleCollect.workPlanGeneratedID?.id
                                                                        req.attributes()["sampleCollect"] = fetchedSampleCollect
                                                                        req.attributes()["sampleCollectParams"] = sampleCollectParams
                                                                        req.attributes()["sampleCollectParamsEntity"] = MsCollectionParametersEntity()
                                                                        ok().render(pageLoad, req.attributes())

                                                                    }
                                                        }

                                            }
                                }

                    }
                    ?: throw ServiceMapNotFoundException("Missing application mapping for [id=$appId], recheck configuration")

    fun viewSampleSubmit(req: ServerRequest): ServerResponse =
            SecurityContextHolder.getContext().authentication?.name
                    ?.let { username ->
                        usersRepo.findByUserName(username)
                                ?.let { loggedInUser ->
                                    serviceMapsRepo.findByIdAndStatus(appId, activeStatus)
                                            ?.let { map ->
                                                var pageLoad = msViewSampleSubmissionPage
                                                req.paramOrNull("fuelInspectID")
                                                        ?.let { fuelInspectID ->
                                                            iFuelInspectionRepo.findByIdOrNull(fuelInspectID.toLong())
                                                                    ?.let { msFuelInspectionEntity ->
                                                                        sampleSubmitRepo.findByMsFuelInspectionId(msFuelInspectionEntity)
                                                                                ?.let { fetchedSampleSubmit ->
                                                                                    sampleSubmitParameterRepo.findBySampleSubmissionId(fetchedSampleSubmit)
                                                                                            .let { sampleSubmitParams ->
                                                                                                req.paramOrNull("ViewType")
                                                                                                        ?.let { ViewType ->
                                                                                                            when {
                                                                                                                req.paramOrNull("docType").equals("close") -> {
                                                                                                                    msFuelInspectionEntity.ssfLabparamsStatus = map.activeStatus
                                                                                                                    iFuelInspectionRepo.save(msFuelInspectionEntity)
                                                                                                                    marketSurveillanceBpmn.msFmGenSSFAndSubmitComplete(msFuelInspectionEntity.id)
                                                                                                                            .let {
                                                                                                                                with(msFuelInspectionEntity) {
                                                                                                                                    sendSffStatus = map.activeStatus
                                                                                                                                    sendSffDate = Date(Date().time)
                                                                                                                                    lastModifiedBy = loggedInUser.firstName + " " + loggedInUser.lastName
                                                                                                                                    lastModifiedOn = java.sql.Timestamp.from(java.time.Instant.now())
                                                                                                                                }
                                                                                                                                val updatedFuel = iFuelInspectionRepo.save(msFuelInspectionEntity)
                                                                                                                                iFuelInspectionOfficerRepo.findByMsFuelInspectionId(updatedFuel)
                                                                                                                                        ?.let { MsFuelInspectionOfficersEntity ->
                                                                                                                                            MsFuelInspectionOfficersEntity.assignedIo?.email?.let { it1 -> sendBSSNumber(it1) }
                                                                                                                                        }

                                                                                                                                pageLoad = "redirect:/api/ms/fuel-detail?fuelInspectId=${msFuelInspectionEntity.id}&ViewType=${myViewTask}"
                                                                                                                                return ok().render(pageLoad)
                                                                                                                            }

                                                                                                                }
                                                                                                                req.paramOrNull("docType").equals("closeBs") -> {
                                                                                                                    with(msFuelInspectionEntity) {
                                                                                                                        bsNumberStatus = activeStatus
                                                                                                                        lastModifiedBy = loggedInUser.firstName + " " + loggedInUser.lastName
                                                                                                                        lastModifiedOn = java.sql.Timestamp.from(java.time.Instant.now())
                                                                                                                    }
                                                                                                                    iFuelInspectionRepo.save(msFuelInspectionEntity)
                                                                                                                            .let { savedMsFuelInspection ->
                                                                                                                                pageLoad = "redirect:/api/ms/fuel-detail?fuelInspectId=${savedMsFuelInspection.id}&ViewType=${myViewTask}"
                                                                                                                                return ok().render(pageLoad)
                                                                                                                            }

                                                                                                                }
                                                                                                                req.paramOrNull("docType").equals("closeSendResults") -> {
                                                                                                                    with(msFuelInspectionEntity) {
                                                                                                                        ownerAllLabResultsStatus = activeStatus
                                                                                                                        lastModifiedBy = loggedInUser.firstName + " " + loggedInUser.lastName
                                                                                                                        lastModifiedOn = java.sql.Timestamp.from(java.time.Instant.now())
                                                                                                                    }
                                                                                                                    iFuelInspectionRepo.save(msFuelInspectionEntity)
                                                                                                                            .let { savedMsFuelInspection ->
                                                                                                                                pageLoad = "redirect:/api/ms/fuel-detail?fuelInspectId=${savedMsFuelInspection.id}&ViewType=${myViewTask}"
                                                                                                                                return ok().render(pageLoad)
                                                                                                                            }

                                                                                                                }
                                                                                                            }
                                                                                                            req.attributes()["ViewType"] = ViewType
                                                                                                            req.attributes()["sampleSubmit"] = fetchedSampleSubmit
                                                                                                            req.attributes()["detailID"] = fetchedSampleSubmit.msFuelInspectionId?.id
                                                                                                            req.attributes()["sampleSubmitBsNumber"] = MsLaboratoryParametersEntity()
                                                                                                            req.attributes()["sampleSubmitParams"] = sampleSubmitParams
                                                                                                            req.attributes()["sampleSubmitParamsEntity"] = MsLaboratoryParametersEntity()
                                                                                                            req.attributes()["laboratories"] = iLaboratoryRepo.findAll()
                                                                                                            return ok().render(pageLoad, req.attributes())
                                                                                                        }
                                                                                            }
                                                                                }

                                                                    }
                                                        }
                                                req.paramOrNull("workPlanID")
                                                        ?.let { workPlanID ->
                                                            genarateWorkPlanRepo.findByIdOrNull(workPlanID.toLong())
                                                                    ?.let { fetchedWorkPlanEntity ->
                                                                        sampleSubmitRepo.findByWorkPlanGeneratedID(fetchedWorkPlanEntity)
                                                                                ?.let { fetchedSampleSubmit ->
                                                                                    sampleSubmitParameterRepo.findBySampleSubmissionId(fetchedSampleSubmit)
                                                                                            .let { sampleSubmitParams ->
                                                                                                if (req.paramOrNull("docType").equals("close")) {
                                                                                                    fetchedWorkPlanEntity.ssfLabparamsStatus = map.activeStatus
                                                                                                    genarateWorkPlanRepo.save(fetchedWorkPlanEntity)

                                                                                                    pageLoad = "redirect:/api/ms/workPlan-detail?workPlanId=${fetchedWorkPlanEntity.id}&ViewType=${myViewTask}"
                                                                                                    return ok().render(pageLoad)
                                                                                                } else if (req.paramOrNull("docType").equals("closeBs")) {
                                                                                                    with(fetchedWorkPlanEntity) {
                                                                                                        bsNumberStatus = activeStatus
//                                                                                                                                lastModifiedBy = loggedInUser.firstName + " " + loggedInUser.lastName
                                                                                                        modifiedOn = java.sql.Timestamp.from(java.time.Instant.now())
                                                                                                    }
                                                                                                    genarateWorkPlanRepo.save(fetchedWorkPlanEntity)
                                                                                                            .let {
                                                                                                                pageLoad = daoServices.redirectSiteMsWorkPlanActivityPage + "${fetchedWorkPlanEntity.uuid}"
                                                                                                                return ok().render(pageLoad)
                                                                                                            }

                                                                                                }
//                                                                                                            req.attributes()["ViewType"] = ViewType
                                                                                                req.attributes()["sampleSubmit"] = fetchedSampleSubmit
                                                                                                req.attributes()["detailID"] = fetchedSampleSubmit.workPlanGeneratedID?.id
                                                                                                req.attributes()["sampleSubmitBsNumber"] = MsLaboratoryParametersEntity()
                                                                                                req.attributes()["sampleSubmitParams"] = sampleSubmitParams
                                                                                                req.attributes()["sampleSubmitParamsEntity"] = MsLaboratoryParametersEntity()
                                                                                                req.attributes()["laboratories"] = iLaboratoryRepo.findAll()
                                                                                                return ok().render(pageLoad, req.attributes())

                                                                                            }
                                                                                }

                                                                    }
                                                        }

                                            }
                                            ?: throw ServiceMapNotFoundException("Missing application mapping for [id=$appId], recheck configuration")
                                }
                                ?: throw ExpectedDataNotFound("Logged in User with this [username=${username}] does not exist")
                    }
                    ?: throw ExpectedDataNotFound("User Is not Authenticated")

    fun viewLabParams(req: ServerRequest): ServerResponse =
            serviceMapsRepo.findByIdAndStatus(appId, activeStatus)
                    ?.let { map ->

                        var pageLoad = msViewLabParamsPage

                        req.paramOrNull("labParmID")
                                ?.let { labParmID ->
                                    sampleSubmitParameterRepo.findByIdOrNull(labParmID.toLong())
                                            ?.let { MsLaboratoryParameter ->
                                                req.paramOrNull("ViewType")
                                                        ?.let { ViewType ->
                                                            if (req.paramOrNull("docType").equals("close")) {

                                                                when (ViewType) {
                                                                    "fuel" -> {
                                                                        pageLoad = "redirect:/api/ms/fuel-detail?fuelInspectId=${MsLaboratoryParameter.sampleSubmissionId?.msFuelInspectionId?.id}&ViewType=${ViewType}"
                                                                        ok().render(pageLoad)
                                                                    }
                                                                    "workPlan" -> {

                                                                        pageLoad = daoServices.redirectSiteMsWorkPlanActivityPage + "${MsLaboratoryParameter.sampleSubmissionId?.workPlanGeneratedID?.uuid}"
                                                                        ok().render(pageLoad)
                                                                    }
                                                                    else -> throw ExpectedDataNotFound("User Is not Authenticated")

                                                                }


                                                            } else if (req.paramOrNull("docType").equals("sendResults")) {
                                                                when {
                                                                    ViewType == "fuel" -> {
                                                                        MsLaboratoryParameter.sampleSubmissionId?.msFuelInspectionId?.stationOwnerEmail?.let {
                                                                            sendStationOwnerEmail(it)
                                                                        }
                                                                        with(MsLaboratoryParameter) {
                                                                            ownerLabResultsStatus = map.activeStatus
                                                                        }
                                                                        sampleSubmitParameterRepo.save(MsLaboratoryParameter)
                                                                        pageLoad = redirectSiteSampleDetails + "?currentPage=0&pageSize=10&fuelInspectID=${MsLaboratoryParameter.sampleSubmissionId?.msFuelInspectionId?.id}&ViewType=${ViewType}"
                                                                        return ok().render(pageLoad)
//                                                                                                            pageLoad = "redirect:/api/ms/fuel-detail?fuelInspectId=${MsLaboratoryParameter.sampleSubmissionId?.msFuelInspectionId?.id}&ViewType=${ViewType}"
                                                                    }
                                                                    ViewType == "workPlan" -> {
                                                                        pageLoad = daoServices.redirectSiteMsWorkPlanActivityPage + "${MsLaboratoryParameter.sampleSubmissionId?.workPlanGeneratedID?.uuid}"
                                                                        return ok().render(pageLoad)
                                                                    }
                                                                    else -> throw ExpectedDataNotFound("User Is not Authenticated")
                                                                }
                                                            }
                                                            req.attributes()["ViewType"] = ViewType
                                                            req.attributes()["labParameters"] = MsLaboratoryParameter
                                                            req.attributes()["labParametersEntity"] = MsLaboratoryParametersEntity()
                                                            return ok().render(pageLoad, req.attributes())
                                                        }

                                            }
                                }

                    }
                    ?: throw ServiceMapNotFoundException("Missing application mapping for [id=$appId], recheck configuration")

    fun viewPreliminary(req: ServerRequest): ServerResponse =
            serviceMapsRepo.findByIdAndStatus(appId, activeStatus)
                    ?.let { map ->
                        req.paramOrNull("workPlanID")
                                ?.let { workPlanID ->
                                    req.paramOrNull("ViewType")
                                            ?.let { ViewType ->
                                                var pageLoad = msViewPreliminaryPage
                                                genarateWorkPlanRepo.findByIdOrNull(workPlanID.toLong())
                                                        ?.let { fetchedWorkPlanEntity ->
                                                            preliminaryRepo.findByWorkPlanGeneratedID(fetchedWorkPlanEntity)
                                                                    ?.let { fetchedPreliminary ->
                                                                        preliminaryOutletRepo.findByPreliminaryReportID(fetchedPreliminary)
                                                                                .let { outletsFound ->
                                                                                    when {
                                                                                        req.paramOrNull("docType").equals("close") -> {
                                                                                            fetchedWorkPlanEntity.preliminaryParamStatus = map.activeStatus
                                                                                            fetchedWorkPlanEntity.msFinalReportStatus = map.inactiveStatus
                                                                                            genarateWorkPlanRepo.save(fetchedWorkPlanEntity)

                                                                                            val hof = fetchedWorkPlanEntity.complaintDepartment?.let { fetchedWorkPlanEntity.region?.let { it1 -> msController.findRegionUserId(designationIDHOF, commonDaoServices.findDepartmentByID(it), map.activeStatus, commonDaoServices.findRegionEntityByRegionID(it1, map.activeStatus)) } }

                                                                                            if (hof != null) {
                                                                                                hof.id?.let {
                                                                                                    fetchedWorkPlanEntity.id?.let { it1 ->
                                                                                                        marketSurveillanceBpmn.msGeneratePreliminaryReportComplete(it1, it)
                                                                                                    }
                                                                                                }
                                                                                            }

                                                                                            pageLoad = daoServices.redirectSiteMsWorkPlanActivityPage + "${fetchedWorkPlanEntity.uuid}"
                                                                                            ok().render(pageLoad)

                                                                                        }
                                                                                        req.paramOrNull("docType").equals("closeFinal") -> {
                                                                                            fetchedWorkPlanEntity.msFinalReportStatus = map.activeStatus
                                                                                            genarateWorkPlanRepo.save(fetchedWorkPlanEntity)

                                                                                            val hof = fetchedWorkPlanEntity.complaintDepartment?.let { fetchedWorkPlanEntity.region?.let { it1 -> msController.findRegionUserId(designationIDHOF, commonDaoServices.findDepartmentByID(it), map.activeStatus, commonDaoServices.findRegionEntityByRegionID(it1, map.activeStatus)) } }

                                                                                            if (hof != null) {
                                                                                                hof.id?.let {
                                                                                                    fetchedWorkPlanEntity.id?.let { it1 ->
                                                                                                        marketSurveillanceBpmn.msGenerateFinalReportComplete(it1, it)
                                                                                                    }
                                                                                                }
                                                                                            }

                                                                                            pageLoad = redirectSiteWorkPlan
                                                                                            ok().render(pageLoad)
                                                                                        }
                                                                                        req.paramOrNull("docType").equals("appealed") -> {
                                                                                            fetchedWorkPlanEntity.clientAppealed = map.activeStatus
                                                                                            genarateWorkPlanRepo.save(fetchedWorkPlanEntity)
                                                                                        }
                                                                                        req.paramOrNull("docType").equals("appealedNotSuccessful") -> {
                                                                                            fetchedWorkPlanEntity.destractionStatus = map.activeStatus
                                                                                            genarateWorkPlanRepo.save(fetchedWorkPlanEntity)
                                                                                        }
                                                                                    }

                                                                                    req.attributes()["preliminary"] = fetchedPreliminary
                                                                                    req.attributes()["preliminaryOutlets"] = outletsFound
                                                                                    req.attributes()["ViewType"] = ViewType
                                                                                    req.attributes()["preliminaryOutletEntity"] = MsPreliminaryReportOutletsVisitedEntity()
                                                                                    req.attributes()["preliminaryReport"] = MsPreliminaryReportEntity()
                                                                                    req.attributes()["preliminaryEntity"] = MsPreliminaryReportEntity()
                                                                                    req.attributes()["recommendedWorkPlan"] = MsWorkPlanGeneratedEntity()
                                                                                    req.attributes()["onSiteUploadsEntity"] = MsOnSiteUploadsEntity()
                                                                                    req.attributes()["recommendations"] = cfgRecommendationRepo.findAll()

                                                                                    ok().render(pageLoad, req.attributes())
                                                                                }


                                                                    }

                                                        }

                                            }


                                }
                    }
                    ?: throw ServiceMapNotFoundException("Missing application mapping for [id=$appId], recheck configuration")

    fun viewFinal(req: ServerRequest): ServerResponse =
            serviceMapsRepo.findByIdAndStatus(appId, activeStatus)
                    ?.let { map ->
                        req.paramOrNull("workPlanID")
                                ?.let { workPlanID ->
                                    req.paramOrNull("ViewType")
                                            ?.let { ViewType ->

                                                genarateWorkPlanRepo.findByIdOrNull(workPlanID.toLong())
                                                        ?.let { fetchedWorkPlanEntity ->
                                                            finalReportRepo.findByWorkPlanGeneratedID(fetchedWorkPlanEntity)
                                                                    ?.let { fetchedFinal ->

                                                                        req.attributes()["finalRep"] = fetchedFinal
                                                                        req.attributes()["ViewType"] = ViewType
//                                                                        req.attributes()["sampleSubmitBsNumber"] = MsSampleSubmissionEntity()
//                                                                        req.attributes()["sampleSubmitParams"] = sampleSubmitParams
                                                                        req.attributes()["finalEntity"] = MsFinalReportEntity()

                                                                        ok().render(msViewFinalPage, req.attributes())

                                                                    }
                                                        }

                                            }

                                }
                    }
                    ?: throw ServiceMapNotFoundException("Missing application mapping for [id=$appId], recheck configuration")

    fun viewChargeSheet(req: ServerRequest): ServerResponse =
            serviceMapsRepo.findByIdAndStatus(appId, activeStatus)
                    ?.let { map ->
                        req.paramOrNull("workPlanID")
                                ?.let { workPlanID ->
                                    countriesRepository.findAll()
                                            .let { countries ->
                                                req.paramOrNull("ViewType")
                                                        ?.let { ViewType ->
                                                            req.attributes()["chargeSheet"] = MsChargeSheetEntity()
                                                            req.attributes()["workPlanId"] = workPlanID
                                                            req.attributes()["countries"] = countries
                                                            req.attributes()["ViewType"] = ViewType
//                        req.attributes()["complaintDocumentsEntity"] = ComplaintDocumentsEntity()
//                        req.attributes()["complaintLocationEntity"] = ComplaintLocationEntity()
//                        req.attributes()["regions"] = regionsRepository.findByStatusOrderByRegion(map.activeStatus)
//                        req.attributes()["kebsRemarksEntity"] = ComplaintKebsRemarksEntity()
//                        kebsRemarksEntity: ComplaintKebsRemarksEntity

                                                            req.attributes()["maps"] = map
                                                            ok().render(msChargeSheetPage, req.attributes())
                                                        }
                                            }
                                }
                    }
                    ?: throw ServiceMapNotFoundException("Missing application mapping for [id=$appId], recheck configuration")

    fun viewDataReport(req: ServerRequest): ServerResponse =
            serviceMapsRepo.findByIdAndStatus(appId, activeStatus)
                    ?.let { map ->
                        req.paramOrNull("workPlanID")
                                ?.let { workPlanID ->
                                    req.paramOrNull("ViewType")
                                            ?.let { ViewType ->
                                                countriesRepository.findAll()
                                                        .let { countries ->
                                                            req.attributes()["dataReport"] = MsDataReportEntity()
                                                            req.attributes()["workPlanId"] = workPlanID
                                                            req.attributes()["countries"] = countries
                                                            req.attributes()["ViewType"] = ViewType
                                                            req.attributes()["dataReportParameters"] = MSListWrapper().featureWrapper()

                                                            req.attributes()["maps"] = map
                                                            ok().render(msDataReportPage, req.attributes())
                                                        }
                                            }
                                }
                    }
                    ?: throw ServiceMapNotFoundException("Missing application mapping for [id=$appId], recheck configuration")

    fun viewPreliminaryReport(req: ServerRequest): ServerResponse =
            serviceMapsRepo.findByIdAndStatus(appId, activeStatus)
                    ?.let { map ->
                        req.paramOrNull("workPlanID")
                                ?.let { workPlanID ->
                                    countriesRepository.findAll()
                                            .let { countries ->
                                                req.paramOrNull("ViewType")
                                                        ?.let { ViewType ->
                                                            req.attributes()["preliminaryReport"] = MsPreliminaryReportEntity()
                                                            req.attributes()["workPlanId"] = workPlanID
                                                            req.attributes()["ViewType"] = ViewType
//                                                req.attributes()["countries"] = countries
//                                                req.attributes()["dataReportParameters"] = MSListWrapper().featureWrapper()

                                                            req.attributes()["maps"] = map
                                                            ok().render(msPreliminaryPage, req.attributes())
                                                        }
                                            }
                                }
                    }
                    ?: throw ServiceMapNotFoundException("Missing application mapping for [id=$appId], recheck configuration")

    fun viewFinalReport(req: ServerRequest): ServerResponse =
            serviceMapsRepo.findByIdAndStatus(appId, activeStatus)
                    ?.let { map ->
                        req.paramOrNull("workPlanID")
                                ?.let { workPlanID ->
                                    countriesRepository.findAll()
                                            .let { countries ->
                                                req.paramOrNull("ViewType")
                                                        ?.let { ViewType ->
                                                            req.attributes()["finalReport"] = MsFinalReportEntity()
                                                            req.attributes()["workPlanId"] = workPlanID
                                                            req.attributes()["ViewType"] = ViewType
//                                                req.attributes()["countries"] = countries
//                                                req.attributes()["dataReportParameters"] = MSListWrapper().featureWrapper()

                                                            req.attributes()["maps"] = map
                                                            ok().render(msFinalReportPage, req.attributes())
                                                        }
                                            }
                                }
                    }
                    ?: throw ServiceMapNotFoundException("Missing application mapping for [id=$appId], recheck configuration")

    fun viewSampleCollection(req: ServerRequest): ServerResponse =
            serviceMapsRepo.findByIdAndStatus(appId, activeStatus)
                    ?.let { map ->
                        req.paramOrNull("ViewType")
                                ?.let { ViewType ->
                                    req.paramOrNull("workPlanID")
                                            ?.let { workPlanID ->
                                                req.attributes()["valueID"] = workPlanID
                                            }
                                    req.paramOrNull("fuelInspectID")
                                            ?.let { fuelInspectID ->
                                                req.attributes()["valueID"] = fuelInspectID
                                            }
                                    req.attributes()["sampleCollect"] = MsSampleCollectionEntity()
                                    req.attributes()["ViewType"] = ViewType

                                    req.attributes()["maps"] = map
                                    ok().render(msSampleCollectionPage, req.attributes())
                                }
                    }
                    ?: throw ServiceMapNotFoundException("Missing application mapping for [id=$appId], recheck configuration")

    fun viewSampleSubmission(req: ServerRequest): ServerResponse =
            serviceMapsRepo.findByIdAndStatus(appId, activeStatus)
                    ?.let { map ->
                        req.paramOrNull("ViewType")
                                ?.let { ViewType ->
                                    req.paramOrNull("workPlanID")
                                            ?.let { workPlanID ->
                                                req.attributes()["valueID"] = workPlanID
                                            }
                                    req.paramOrNull("fuelInspectID")
                                            ?.let { fuelInspectID ->
                                                req.attributes()["valueID"] = fuelInspectID
                                            }
                                    req.attributes()["sampleSubmit"] = MsSampleSubmissionEntity()
                                    req.attributes()["ViewType"] = ViewType

                                    req.attributes()["maps"] = map
                                    ok().render(msSampleSubmissionPage, req.attributes())
                                }
                    }
                    ?: throw ServiceMapNotFoundException("Missing application mapping for [id=$appId], recheck configuration")

    fun viewSeizureDeclaration(req: ServerRequest): ServerResponse =
            serviceMapsRepo.findByIdAndStatus(appId, activeStatus)
                    ?.let { map ->
                        req.paramOrNull("workPlanID")
                                ?.let { workPlanID ->
                                    req.paramOrNull("ViewType")
                                            ?.let { ViewType ->
                                                req.attributes()["seizureDeclaration"] = MsSeizureDeclarationEntity()
                                                req.attributes()["workPlanId"] = workPlanID
                                                req.attributes()["ViewType"] = ViewType

                                                req.attributes()["maps"] = map
                                                ok().render(msSeizureDeclarationPage, req.attributes())
                                            }
                                }
                    }
                    ?: throw ServiceMapNotFoundException("Missing application mapping for [id=$appId], recheck configuration")

    fun viewNotesTaking(req: ServerRequest): ServerResponse =
            serviceMapsRepo.findByIdAndStatus(appId, activeStatus)
                    ?.let { map ->
                        req.paramOrNull("workPlanID")
                                ?.let { workPlanID ->
                                    req.paramOrNull("ViewType")
                                            ?.let { ViewType ->
                                                req.attributes()["seizureDeclaration"] = MsSeizureDeclarationEntity()
                                                req.attributes()["workPlanId"] = workPlanID
                                                req.attributes()["ViewType"] = ViewType

                                                req.attributes()["maps"] = map
                                                ok().render(msNotesViewPage, req.attributes())
                                            }
                                }
                    }
                    ?: throw ServiceMapNotFoundException("Missing application mapping for [id=$appId], recheck configuration")

    fun viewInvestigationInspection(req: ServerRequest): ServerResponse =
            serviceMapsRepo.findByIdAndStatus(appId, activeStatus)
                    ?.let { map ->
                        req.paramOrNull("workPlanID")
                                ?.let { workPlanID ->
                                    req.paramOrNull("ViewType")
                                            ?.let { ViewType ->
                                                req.attributes()["investInspectReport"] = MsInspectionInvestigationReportEntity()
                                                req.attributes()["workPlanId"] = workPlanID
                                                req.attributes()["ViewType"] = ViewType

                                                req.attributes()["maps"] = map
                                                ok().render(msInvestInspectReportPage, req.attributes())
                                            }
                                }
                    }
                    ?: throw ServiceMapNotFoundException("Missing application mapping for [id=$appId], recheck configuration")


    fun sendStationOwnerEmail(recipientEmail: String): Boolean {
        val subject = "LAB RESULTS"
        val messageBody = "Please Find The attached Lab Results of the samples collected and submitted for testing from your Petrol station"
        notifications.sendEmail(recipientEmail, subject, messageBody)
        return true
    }

    fun sendBSSNumber(recipientEmail: String) {
        val subject = "LAB BS NUMBER"
        val messageBody = "Please attach the BS NUMBER"
        notifications.sendEmail(recipientEmail, subject, messageBody)
    }


}


