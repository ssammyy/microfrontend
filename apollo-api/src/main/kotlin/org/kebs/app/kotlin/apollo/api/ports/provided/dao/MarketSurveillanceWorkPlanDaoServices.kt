package org.kebs.app.kotlin.apollo.api.ports.provided.dao

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.controllers.qaControllers.ReportsController
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.MarketSurveillanceBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.emailDTO.*
import org.kebs.app.kotlin.apollo.api.ports.provided.lims.LimsServices
import org.kebs.app.kotlin.apollo.common.dto.ms.*
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.utils.generateRandomText
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.ms.*
import org.kebs.app.kotlin.apollo.store.model.qa.QaSampleSubmissionEntity
import org.kebs.app.kotlin.apollo.store.repo.IServiceRequestsRepository
import org.kebs.app.kotlin.apollo.store.repo.IWorkPlanCreatedRepository
import org.kebs.app.kotlin.apollo.store.repo.di.ILaboratoryRepository
import org.kebs.app.kotlin.apollo.store.repo.ms.*
import org.kebs.app.kotlin.apollo.store.repo.qa.IQaSampleLabTestResultsRepository
import org.kebs.app.kotlin.apollo.store.repo.qa.IQaSampleSubmissionRepository
import org.kebs.app.kotlin.apollo.store.repo.qa.IQaSampleSubmittedPdfListRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.lang.reflect.Type
import java.sql.Date
import java.sql.Timestamp
import java.time.Instant
import java.util.*


@Service
class MarketSurveillanceWorkPlanDaoServices(
    private val applicationMapProperties: ApplicationMapProperties,
    private val workPlanYearsCodesRepository: IWorkplanYearsCodesRepository,
    private val workPlanCreatedRepository: IWorkPlanCreatedRepository,
    private val generateWorkPlanRepo: IWorkPlanGenerateRepository,
    private val chargeSheetRepo: IChargeSheetRepository,
    private val dataReportRepo: IDataReportRepository,
    private val dataReportParameterRepo: IDataReportParameterRepository,
    private val seizureDeclarationRepo: IMSSeizureDeclarationRepository,
    private val investInspectReportRepo: IMSInvestInspectReportRepository,
    private val preliminaryRepo: IPreliminaryReportRepository,
    private val preliminaryOutletRepo: IPreliminaryOutletsRepository,
    private val recommendationRepo: ICfgRecommendationRepository,

    private val msTypesRepo: IMsTypesRepository,
    private val complaintsRepo: IComplaintRepository,
    private val complaintCustomersRepo: IComplaintCustomersRepository,
    private val complaintLocationRepo: IComplaintLocationRepository,
    private val remarksRepo: IMsRemarksComplaintRepository,
    private val processNameRepo: IMsProcessNamesRepository,
    private val marketSurveillanceBpmn: MarketSurveillanceBpmn,
    private val complaintsDocRepo: IComplaintDocumentsRepository,
    private val limsServices: LimsServices,
    private val laboratoryRepo: ILaboratoryRepository,
    private val fuelRemediationInvoiceChargesRepo: IFuelRemediationChargesRepository,
    private val sampleSubmissionSavedPdfListRepo: IQaSampleSubmittedPdfListRepository,
    private val workPlanYearsCodesRepo: IWorkplanYearsCodesRepository,
    private val sampleCollectRepo: ISampleCollectionRepository,
    private val sampleLabTestResultsRepo: IQaSampleLabTestResultsRepository,
    private val fuelInspectionOfficerRepo: IFuelInspectionOfficerRepository,
    private val sampleCollectParameterRepo: ISampleCollectParameterRepository,
    private val sampleSubmitParameterRepo: ISampleSubmitParameterRepository,
    private val sampleSubmissionLabRepo: IQaSampleSubmissionRepository,
    private val msUploadRepo: IMsUploadsRepository,
    private val fuelRemediationRepo: IFuelRemediationRepository,
    private val sampleSubmitRepo: IMSSampleSubmissionRepository,
    private val fuelInspectionRepo: IFuelInspectionRepository,
    private val fuelRemediationInvoiceRepo: IFuelRemediationInvoiceRepository,
    private val invoiceDaoService: InvoiceDaoService,
    private val reportsDaoService: ReportsDaoService,
    private val serviceRequestsRepo: IServiceRequestsRepository,
    private val commonDaoServices: CommonDaoServices,
    private val msFuelDaoServices: MarketSurveillanceFuelDaoServices,
    private val msComplaintDaoServices: MarketSurveillanceComplaintProcessDaoServices
) {
    final var complaintSteps: Int = 6
    private final val activeStatus: Int = 1

    final var appId = applicationMapProperties.mapMarketSurveillance

    @Lazy
    @Autowired
    lateinit var reportsControllers: ReportsController

    fun listMsRecommendation(status: Int): List<MsRecommendationDto>? {
        val directoratesEntity = recommendationRepo.findAllByStatus(status)
        return directoratesEntity?.sortedBy { it.id }?.map { MsRecommendationDto(it.id, it.recommendationName, it.description, it.status == 1) }
    }


    @PreAuthorize("hasAuthority('MS_IO_READ') or hasAuthority('MS_HOD_READ') or hasAuthority('MS_RM_READ') or hasAuthority('MS_HOF_READ') or hasAuthority('MS_DIRECTOR_READ')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun getAllWorkPlanBatchList(page: PageRequest): List<WorkPlanBatchDetailsDto> {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val auth = commonDaoServices.loggedInUserAuthentication()
        val map = commonDaoServices.serviceMapDetails(appId)
        val regionID = commonDaoServices.findUserProfileByUserID(loggedInUser, map.activeStatus).regionId?.id ?: throw ExpectedDataNotFound("Logged IN User Is Missing Region ID")

        val myWorkPlanCreated: Page<WorkPlanCreatedEntity>?
        when {
            auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_IO_READ" } -> {
                myWorkPlanCreated = workPlanCreatedRepository.findByUserCreatedId(loggedInUser,page)
            }
            auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_HOD_READ" } -> {
                myWorkPlanCreated = workPlanCreatedRepository.findByWorkPlanRegion(regionID,page)
            }
            auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_HOF_READ" } -> {
                myWorkPlanCreated = workPlanCreatedRepository.findByWorkPlanRegion(regionID,page)
            }
            auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_RM_READ" } -> {
                myWorkPlanCreated = workPlanCreatedRepository.findByWorkPlanRegion(regionID,page)
            }
            auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_DIRECTOR_READ" } -> {
                myWorkPlanCreated = workPlanCreatedRepository.findAll(page)
            }

            else -> throw ExpectedDataNotFound("Can not access this page Due to Invalid authorities")
        }
        return mapWorkPlanBatchListDto(myWorkPlanCreated)
    }

    @PreAuthorize("hasAuthority('MS_IO_READ') or hasAuthority('MS_HOD_READ') or hasAuthority('MS_RM_READ') or hasAuthority('MS_HOF_READ') or hasAuthority('MS_DIRECTOR_READ')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun getAllWorkPlanBatchListClosed(page: PageRequest): List<WorkPlanBatchDetailsDto> {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val auth = commonDaoServices.loggedInUserAuthentication()
        val map = commonDaoServices.serviceMapDetails(appId)
        val regionID = commonDaoServices.findUserProfileByUserID(loggedInUser, map.activeStatus).regionId?.id ?: throw ExpectedDataNotFound("Logged IN User Is Missing Region ID")

        val myWorkPlanCreated: Page<WorkPlanCreatedEntity>?
        when {
            auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_IO_READ" } -> {
                myWorkPlanCreated = workPlanCreatedRepository.findByUserCreatedIdAndBatchClosed(loggedInUser,map.activeStatus,page)
            }
            auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_HOD_READ" } -> {
                myWorkPlanCreated = workPlanCreatedRepository.findByWorkPlanRegionAndBatchClosed(regionID,map.activeStatus,page)
            }
            auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_HOF_READ" } -> {
                myWorkPlanCreated = workPlanCreatedRepository.findByWorkPlanRegionAndBatchClosed(regionID,map.activeStatus,page)
            }
            auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_RM_READ" } -> {
                myWorkPlanCreated = workPlanCreatedRepository.findByWorkPlanRegionAndBatchClosed(regionID,map.activeStatus,page)
            }
            auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_DIRECTOR_READ" } -> {
                myWorkPlanCreated = workPlanCreatedRepository.findAllByBatchClosed(map.activeStatus,page)
            }

            else -> throw ExpectedDataNotFound("Can not access this page Due to Invalid authorities")
        }
        return mapWorkPlanBatchListDto(myWorkPlanCreated)
    }

    @PreAuthorize("hasAuthority('MS_IO_READ') or hasAuthority('MS_HOD_READ') or hasAuthority('MS_RM_READ') or hasAuthority('MS_HOF_READ') or hasAuthority('MS_DIRECTOR_READ')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun getAllWorkPlanBatchListOpen(page: PageRequest): List<WorkPlanBatchDetailsDto> {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val auth = commonDaoServices.loggedInUserAuthentication()
        val map = commonDaoServices.serviceMapDetails(appId)
        val regionID = commonDaoServices.findUserProfileByUserID(loggedInUser, map.activeStatus).regionId?.id ?: throw ExpectedDataNotFound("Logged IN User Is Missing Region ID")

        val myWorkPlanCreated: Page<WorkPlanCreatedEntity>?
        when {
            auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_IO_READ" } -> {
                myWorkPlanCreated = workPlanCreatedRepository.findByUserCreatedIdAndBatchClosed(loggedInUser,map.inactiveStatus,page)
            }
            auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_HOD_READ" } -> {
                myWorkPlanCreated = workPlanCreatedRepository.findByWorkPlanRegionAndBatchClosed(regionID,map.inactiveStatus,page)
            }
            auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_HOF_READ" } -> {
                myWorkPlanCreated = workPlanCreatedRepository.findByWorkPlanRegionAndBatchClosed(regionID,map.inactiveStatus,page)
            }
            auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_RM_READ" } -> {
                myWorkPlanCreated = workPlanCreatedRepository.findByWorkPlanRegionAndBatchClosed(regionID,map.inactiveStatus,page)
            }
            auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_DIRECTOR_READ" } -> {
                myWorkPlanCreated = workPlanCreatedRepository.findAllByBatchClosed(map.inactiveStatus,page)
            }

            else -> throw ExpectedDataNotFound("Can not access this page Due to Invalid authorities")
        }
        return mapWorkPlanBatchListDto(myWorkPlanCreated)
    }

    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun createNewWorkPlanBatch(page: PageRequest): List<WorkPlanBatchDetailsDto> {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val currentYear = getCurrentYear()
        val workPlanYearCodes = findWorkPlanYearsCodesEntity(currentYear, map)
        val userWorkPlan = findWorkPlanCreatedEntity(loggedInUser, workPlanYearCodes)
        val checkCreationDate = isWithinRange(commonDaoServices.getCurrentDate(), workPlanYearCodes)
        when {
            checkCreationDate -> {
                when (userWorkPlan) {
                    null -> {
                        createWorkPlanYear(loggedInUser, map, workPlanYearCodes)
                        val workPlanCreated = workPlanCreatedRepository.findByUserCreatedId(loggedInUser,page)
                        return mapWorkPlanBatchListDto(workPlanCreated)
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

    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun closeWorkPlanBatchCreated(referenceNumber: String,page: PageRequest): List<WorkPlanBatchDetailsDto> {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val batchDetail  = findCreatedWorkPlanWIthRefNumber(referenceNumber)
        val loggedInUserProfile = commonDaoServices.findUserProfileByUserID(loggedInUser, map.activeStatus)

        with(batchDetail) {
            endedDate = commonDaoServices.getCurrentDate()
            endedStatus = map.activeStatus
            status = map.activeStatus
            batchClosed = map.activeStatus
            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameHodRm
        }

        val fileSaved = updateWorkPlanBatch(batchDetail, map, loggedInUser)

        when (fileSaved.first.status) {
            map.successStatus -> {
                val fileInspectionList = findALlWorkPlanDetailsAssociatedWithWorkPlanID(batchDetail.id,page).toList()
                fileInspectionList.forEach { it ->
                    with(it) {
                        timelineStartDate = commonDaoServices.getCurrentDate()
                        timelineEndDate = applicationMapProperties.mapMSWorkPlanInspectionSubmission.let { findProcessNameByID( it, 1).timelinesDay?.let {it2-> commonDaoServices.addYDayToDate(commonDaoServices.getCurrentDate(), it2) } }
                        msProcessId = applicationMapProperties.mapMSWorkPlanInspectionSubmission
                        userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameHodRm
                    }
                    updateWorkPlanInspectionDetails(it, map, loggedInUser)
                }

                val hodList = commonDaoServices.findOfficersListBasedOnRole(applicationMapProperties.mapMSComplaintWorkPlanMappedHODROLEID,
                    loggedInUserProfile.countyID?.id ?: throw ExpectedDataNotFound("MISSING BATCH COUNTY ID"),
                    loggedInUserProfile.regionId?.id ?: throw ExpectedDataNotFound("MISSING BATCH REGION ID")
                )

                val rmList = commonDaoServices.findOfficersListBasedOnRole(applicationMapProperties.mapMSComplaintWorkPlanMappedRMROLEID,
                    loggedInUserProfile.countyID?.id ?: throw ExpectedDataNotFound("MISSING BATCH COUNTY ID"),
                    loggedInUserProfile.regionId?.id ?: throw ExpectedDataNotFound("MISSING BATCH REGION ID")
                )

                runBlocking {
                    hodList
                        ?.forEach { mp->
                            val scheduleEmailDetails =  WorkPlanScheduledDTO()
                            with(scheduleEmailDetails){
                                baseUrl= applicationMapProperties.baseUrlValue
                                fullName = commonDaoServices.concatenateName(mp)
                                refNumber = fileSaved.second.referenceNumber
                                yearCodeName = fileSaved.second.yearNameId?.yearName
                                dateSubmitted = commonDaoServices.getCurrentDate()

                            }
                            commonDaoServices.sendEmailWithUserEntity(mp, applicationMapProperties.mapMsFuelScheduleMPNotification, scheduleEmailDetails, map, fileSaved.first)
                        }
                    rmList
                        ?.forEach { mp->
                            val scheduleEmailDetails =  WorkPlanScheduledDTO()
                            with(scheduleEmailDetails){
                                baseUrl= applicationMapProperties.baseUrlValue
                                fullName = commonDaoServices.concatenateName(mp)
                                refNumber = fileSaved.second.referenceNumber
                                yearCodeName = fileSaved.second.yearNameId?.yearName
                                dateSubmitted = commonDaoServices.getCurrentDate()

                            }
                            commonDaoServices.sendEmailWithUserEntity(mp, applicationMapProperties.mapMsFuelScheduleMPNotification, scheduleEmailDetails, map, fileSaved.first)
                        }
                }

                val workBatchList = workPlanCreatedRepository.findByUserCreatedId(loggedInUser,page)
                return mapWorkPlanBatchListDto(workBatchList)
            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved.first))
            }
        }
    }

    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun submitWorkPlanScheduleCreated(batchReferenceNo: String, referenceNo: String,page: PageRequest): WorkPlanInspectionDto {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        val batchDetail = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
        val loggedInUserProfile = commonDaoServices.findUserProfileByUserID(loggedInUser, map.activeStatus)

        with(batchDetail) {
            endedDate = commonDaoServices.getCurrentDate()
            endedStatus = map.activeStatus
            status = map.activeStatus
            batchClosed = map.activeStatus
            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameHodRm
        }

        val fileSaved = updateWorkPlanBatch(batchDetail, map, loggedInUser)

        when (fileSaved.first.status) {
            map.successStatus -> {
                with(workPlanScheduled) {
                    if (submittedForApprovalStatus == map.activeStatus){
                        resubmitStatus = map.activeStatus
                    }
                    submittedForApprovalStatus= map.activeStatus
                    timelineStartDate = commonDaoServices.getCurrentDate()
                    timelineEndDate = applicationMapProperties.mapMSWorkPlanInspectionSubmission.let { findProcessNameByID( it, 1).timelinesDay?.let {it2-> commonDaoServices.addYDayToDate(commonDaoServices.getCurrentDate(), it2) } }
                    msProcessId = applicationMapProperties.mapMSWorkPlanInspectionSubmission
                    userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameHodRm
                }
                updateWorkPlanInspectionDetails(workPlanScheduled, map, loggedInUser)

                val hodList = commonDaoServices.findOfficersListBasedOnRole(applicationMapProperties.mapMSComplaintWorkPlanMappedHODROLEID,
                    loggedInUserProfile.countyID?.id ?: throw ExpectedDataNotFound("MISSING BATCH COUNTY ID"),
                    loggedInUserProfile.regionId?.id ?: throw ExpectedDataNotFound("MISSING BATCH REGION ID")
                )

                val rmList = commonDaoServices.findOfficersListBasedOnRole(applicationMapProperties.mapMSComplaintWorkPlanMappedRMROLEID,
                    loggedInUserProfile.countyID?.id ?: throw ExpectedDataNotFound("MISSING BATCH COUNTY ID"),
                    loggedInUserProfile.regionId?.id ?: throw ExpectedDataNotFound("MISSING BATCH REGION ID")
                )

                runBlocking {
                    hodList
                        ?.forEach { mp->
                            val scheduleEmailDetails =  WorkPlanScheduledDTO()
                            with(scheduleEmailDetails){
                                baseUrl= applicationMapProperties.baseUrlValue
                                fullName = commonDaoServices.concatenateName(mp)
                                refNumber = fileSaved.second.referenceNumber
                                batchRefNumber = batchReferenceNo
                                yearCodeName = fileSaved.second.yearNameId?.yearName
                                dateSubmitted = commonDaoServices.getCurrentDate()

                            }
                            commonDaoServices.sendEmailWithUserEntity(mp, applicationMapProperties.mapMsWorkPlanScheduleSubmitedForApproval, scheduleEmailDetails, map, fileSaved.first)
                        }
                    rmList
                        ?.forEach { mp->
                            val scheduleEmailDetails =  WorkPlanScheduledDTO()
                            with(scheduleEmailDetails){
                                baseUrl= applicationMapProperties.baseUrlValue
                                fullName = commonDaoServices.concatenateName(mp)
                                refNumber = fileSaved.second.referenceNumber
                                batchRefNumber = batchReferenceNo
                                yearCodeName = fileSaved.second.yearNameId?.yearName
                                dateSubmitted = commonDaoServices.getCurrentDate()

                            }
                            commonDaoServices.sendEmailWithUserEntity(mp, applicationMapProperties.mapMsWorkPlanScheduleSubmitedForApproval, scheduleEmailDetails, map, fileSaved.first)
                        }
                }

                return workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetail)
            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved.first))
            }
        }
    }


    @PreAuthorize("hasAuthority('MS_IO_READ') or hasAuthority('MS_HOD_READ') or hasAuthority('MS_RM_READ') or hasAuthority('MS_HOF_READ') or hasAuthority('MS_DIRECTOR_READ')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun getAllWorPlanInspectionListBasedOnBatchRefNo(batchReferenceNo: String, complaintStatus:Boolean, page: PageRequest): WorkPlanScheduleListDetailsDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val auth = commonDaoServices.loggedInUserAuthentication()
        val loggedInUserProfile = commonDaoServices.findUserProfileByUserID(loggedInUser)
        val map = commonDaoServices.serviceMapDetails(appId)
        val createdWorkPlan: WorkPlanCreatedEntity?
        var workPlanList: List<MsWorkPlanGeneratedEntity>?
        when {
            auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_IO_READ" } -> {
                createdWorkPlan = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
                workPlanList = when {
                    complaintStatus -> {
                        findALlWorkPlanDetailsAssociatedWithWorkPlanIDWithComplaintIN(createdWorkPlan.id,page).toList()
                    }
                    else -> {
                        findALlWorkPlanDetailsAssociatedWithWorkPlanID(createdWorkPlan.id,page).toList()
                    }
                }

            }
            auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_HOD_READ"
                        || authority.authority == "MS_RM_READ"
                        || authority.authority == "MS_HOF_READ"
            } -> {
                createdWorkPlan = findCreatedWorkPlanWIthRefNumberAndRegion(batchReferenceNo, loggedInUserProfile.regionId?.id?:throw ExpectedDataNotFound("Missing region value on your user profile  details"))
                workPlanList = when {
                    complaintStatus -> {
                        findALlWorkPlanDetailsAssociatedWithWorkPlanIDWithComplaintIN(createdWorkPlan.id,page).toList()
                    }
                    else -> {
                        findALlWorkPlanDetailsAssociatedWithWorkPlanID(createdWorkPlan.id,page).toList()
                    }
                }
            }
            auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_DIRECTOR_READ" } -> {
                createdWorkPlan = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
                workPlanList = when {
                    complaintStatus -> {
                        findALlWorkPlanDetailsAssociatedWithWorkPlanIDWithComplaintIN(createdWorkPlan.id,page).toList()
                    }
                    else -> {
                        findALlWorkPlanDetailsAssociatedWithWorkPlanID(createdWorkPlan.id,page).toList()
                    }
                }
            }
            else -> {
                throw ExpectedDataNotFound("Can't access this page Due to Invalid authority")
            }
        }

        return mapWorkPlanInspectionListDto(workPlanList,mapWorkPlanBatchDetailsDto(createdWorkPlan, map))
    }

    @PreAuthorize("hasAuthority('MS_IO_READ') or hasAuthority('MS_HOD_READ') or hasAuthority('MS_RM_READ') or hasAuthority('MS_HOF_READ') or hasAuthority('MS_DIRECTOR_READ')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun getAllWorPlanInspectionAllCompletedLists(batchReferenceNo: String,complaintStatus:Boolean,page: PageRequest): WorkPlanScheduleListDetailsDto {
        val auth = commonDaoServices.loggedInUserAuthentication()
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val userProfile = commonDaoServices.findUserProfileByUserID(loggedInUser)
        val createdWorkPlan: WorkPlanCreatedEntity?
        val workPlanList: List<MsWorkPlanGeneratedEntity>?
        when {
            auth.authorities.stream().anyMatch { authority ->
                authority.authority == "MS_IO_READ"
                        || authority.authority == "MS_HOD_READ"
                        || authority.authority == "MS_HOF_READ"
                        || authority.authority == "MS_DIRECTOR_READ"
                        || authority.authority == "MS_RM_READ" } -> {
                createdWorkPlan = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
                workPlanList = when {
                    complaintStatus -> {
                        generateWorkPlanRepo.findByWorkPlanYearIdAndMsProcessEndedStatusAndComplaintIdIsNotNull(createdWorkPlan.id,map.activeStatus,page)?.toList()
                    }
                    else -> {
                        generateWorkPlanRepo.findByWorkPlanYearIdAndMsProcessEndedStatus(createdWorkPlan.id,map.activeStatus,page)?.toList()
                    }
                }


            }
            else -> throw ExpectedDataNotFound("Can't access this page Due to Invalid authority")
        }

        return mapWorkPlanInspectionListDto(workPlanList,mapWorkPlanBatchDetailsDto(createdWorkPlan, map))
    }

    @PreAuthorize("hasAuthority('MS_IO_READ') or hasAuthority('MS_HOD_READ') or hasAuthority('MS_RM_READ') or hasAuthority('MS_HOF_READ') or hasAuthority('MS_DIRECTOR_READ')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun getAllWorPlanInspectionAllNotCompletedLists(batchReferenceNo: String,complaintStatus:Boolean,page: PageRequest): WorkPlanScheduleListDetailsDto {
        val auth = commonDaoServices.loggedInUserAuthentication()
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val userProfile = commonDaoServices.findUserProfileByUserID(loggedInUser)
        val createdWorkPlan: WorkPlanCreatedEntity?
        val workPlanList: List<MsWorkPlanGeneratedEntity>?
        when {
            auth.authorities.stream().anyMatch { authority ->
                authority.authority == "MS_IO_READ"
                        || authority.authority == "MS_HOD_READ"
                        || authority.authority == "MS_HOF_READ"
                        || authority.authority == "MS_DIRECTOR_READ"
                        || authority.authority == "MS_RM_READ" } -> {
                createdWorkPlan = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
                workPlanList = when {
                    complaintStatus -> {
                        generateWorkPlanRepo.findByWorkPlanYearIdAndMsProcessEndedStatusAndComplaintIdIsNotNull(createdWorkPlan.id,map.inactiveStatus,page)?.toList()
                    }
                    else -> {
                        generateWorkPlanRepo.findByWorkPlanYearIdAndMsProcessEndedStatus(createdWorkPlan.id,map.inactiveStatus,page)?.toList()
                    }
                }
            }
            else -> throw ExpectedDataNotFound("Can't access this page Due to Invalid authority")
        }

        return mapWorkPlanInspectionListDto(workPlanList,mapWorkPlanBatchDetailsDto(createdWorkPlan, map))
    }

    @PreAuthorize("hasAuthority('MS_IO_READ') or hasAuthority('MS_HOD_READ') or hasAuthority('MS_RM_READ') or hasAuthority('MS_HOF_READ') or hasAuthority('MS_DIRECTOR_READ')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun getAllWorPlanInspectionListMyTask(batchReferenceNo: String,complaintStatus:Boolean,page: PageRequest): WorkPlanScheduleListDetailsDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val auth = commonDaoServices.loggedInUserAuthentication()
        val loggedInUserProfile = commonDaoServices.findUserProfileByUserID(loggedInUser)
        val map = commonDaoServices.serviceMapDetails(appId)
        val createdWorkPlan: WorkPlanCreatedEntity?
        var workPlanList: List<MsWorkPlanGeneratedEntity>?
        when {
            auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_IO_READ" } -> {
                createdWorkPlan = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
                workPlanList = when {
                    complaintStatus -> {
                        generateWorkPlanRepo.findByWorkPlanYearIdAndOfficerIdAndUserTaskIdAndComplaintIdIsNotNull(createdWorkPlan.id,loggedInUser.id ?: throw ExpectedDataNotFound("Missing Logged In User ID"), applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO,page)?.toList()
                    }
                    else -> {
                        generateWorkPlanRepo.findByWorkPlanYearIdAndOfficerIdAndUserTaskId(createdWorkPlan.id,loggedInUser.id ?: throw ExpectedDataNotFound("Missing Logged In User ID"), applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO,page)?.toList()
                    }
                }
            }
            auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_HOF_READ" } -> {
                createdWorkPlan = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
                workPlanList = when {
                    complaintStatus -> {
                        generateWorkPlanRepo.findByWorkPlanYearIdAndHofAssignedAndUserTaskIdAndComplaintIdIsNotNull(createdWorkPlan.id,loggedInUser.id ?: throw ExpectedDataNotFound("Missing Logged In User ID"), applicationMapProperties.mapMSCPWorkPlanUserTaskNameHof,page)?.toList()
                    }
                    else -> {
                        generateWorkPlanRepo.findByWorkPlanYearIdAndHofAssignedAndUserTaskId(createdWorkPlan.id,loggedInUser.id ?: throw ExpectedDataNotFound("Missing Logged In User ID"), applicationMapProperties.mapMSCPWorkPlanUserTaskNameHof,page)?.toList()
                    }
                }
            }
            auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_HOD_READ"
                    || authority.authority == "MS_RM_READ"
            } -> {
                createdWorkPlan = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
                workPlanList = when {
                    complaintStatus -> {
                        generateWorkPlanRepo.findByWorkPlanYearIdAndHodRmAssignedAndUserTaskIdAndComplaintIdIsNotNull(createdWorkPlan.id,loggedInUser.id ?: throw ExpectedDataNotFound("Missing Logged In User ID"), applicationMapProperties.mapMSCPWorkPlanUserTaskNameHodRm,page)?.toList()
                    }
                    else -> {
                        generateWorkPlanRepo.findByWorkPlanYearIdAndHodRmAssignedAndUserTaskId(createdWorkPlan.id,loggedInUser.id ?: throw ExpectedDataNotFound("Missing Logged In User ID"), applicationMapProperties.mapMSCPWorkPlanUserTaskNameHodRm,page)?.toList()
                    }
                }
            }
            auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_DIRECTOR_READ" } -> {
                createdWorkPlan = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
                workPlanList = when {
                    complaintStatus -> {
                        generateWorkPlanRepo.findByWorkPlanYearIdAndUserTaskIdAndComplaintIdIsNotNull(createdWorkPlan.id, applicationMapProperties.mapMSCPWorkPlanUserTaskNameDirector,page)?.toList()
                    }
                    else -> {
                        generateWorkPlanRepo.findByWorkPlanYearIdAndUserTaskId(createdWorkPlan.id, applicationMapProperties.mapMSCPWorkPlanUserTaskNameDirector,page)?.toList()
                    }
                }
            }
            else -> {
                throw ExpectedDataNotFound("Can't access this page Due to Invalid authority")
            }
        }

        return mapWorkPlanInspectionListDto(workPlanList,mapWorkPlanBatchDetailsDto(createdWorkPlan, map))
    }



    @PreAuthorize("hasAuthority('MS_HOD_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun updateWorkPlanScheduleInspectionDetailsApprovalStatus(
        referenceNo: String,
        batchReferenceNo: String,
        body: WorkPlanScheduleApprovalDto
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
        val scheduleEmailDetails =  WorkPlanScheduledDTO()
        var emailDetails = ""
        when {
            body.approvalStatus -> {
                when (batchDetails.batchClosed) {
                    map.activeStatus -> {
                        with(workPlanScheduled){
                            hodRmAssigned = loggedInUser.id
                            timelineStartDate = commonDaoServices.getCurrentDate()
                            timelineEndDate = applicationMapProperties.mapMSWorkPlanInspectionApproveWorkPlan.let { findProcessNameByID( it, 1).timelinesDay?.let {it2-> commonDaoServices.addYDayToDate(commonDaoServices.getCurrentDate(), it2) } }
                            msProcessId = applicationMapProperties.mapMSWorkPlanInspectionApproveWorkPlan
                            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
                            approved = "APPROVED"
                            progressStep = approved
                            approvedBy = commonDaoServices.concatenateName(loggedInUser)
                            approvedStatus = map.activeStatus
                            rejectedStatus = map.inactiveStatus
                            updatedStatus = map.inactiveStatus
                            resubmitStatus = map.inactiveStatus
                            approvedOn = commonDaoServices.getCurrentDate()
                            scheduleEmailDetails.approvalStatus = approved
                            emailDetails = applicationMapProperties.mapMsWorkPlanScheduleSubmitedApprovalApproved
                        }
                    }
                }
            }
            else -> {
                when (batchDetails.batchClosed) {
                    map.activeStatus -> {
                        with(workPlanScheduled){
                            hodRmAssigned = loggedInUser.id
                            msProcessId = applicationMapProperties.mapMSWorkPlanInspectionRejectWorkPlan
                            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
                            rejected = "REJECTED"
                            progressStep = rejected
                            rejectedBy = commonDaoServices.concatenateName(loggedInUser)
                            rejectedStatus = map.activeStatus
                            approvedStatus = map.inactiveStatus
                            updatedStatus= map.inactiveStatus
                            resubmitStatus= map.inactiveStatus
                            rejectedOn = commonDaoServices.getCurrentDate()
                            scheduleEmailDetails.approvalStatus = rejected
                            emailDetails = applicationMapProperties.mapMsWorkPlanScheduleSubmitedApprovalRejected
                        }
                    }
                }
            }
        }
        val fileSaved = updateWorkPlanInspectionDetails(workPlanScheduled, map, loggedInUser)
        workPlanScheduled = fileSaved.second
        val remarksDto = RemarksToAddDto()
        with(remarksDto){
            remarksDescription= body.remarks
            remarksStatus = scheduleEmailDetails.approvalStatus
            processID = workPlanScheduled.msProcessId
            userId= loggedInUser.id
        }

        when (fileSaved.first.status) {
            map.successStatus -> {

                val remarksSaved = workPlanAddRemarksDetails(fileSaved.second.id,remarksDto, map, loggedInUser)
                when (remarksSaved.first.status) {
                    map.successStatus -> {
                        runBlocking {
                            val userDetails = workPlanScheduled.officerId?.let { commonDaoServices.findUserByID(it) }
                            with(scheduleEmailDetails){
                                baseUrl= applicationMapProperties.baseUrlValue
                                fullName = userDetails?.let { commonDaoServices.concatenateName(it) }
                                refNumber = referenceNo
                                batchRefNumber = batchReferenceNo
                                yearCodeName = batchDetails.yearNameId?.yearName
                                dateSubmitted = commonDaoServices.getCurrentDate()

                            }
                            userDetails?.let { commonDaoServices.sendEmailWithUserEntity(it, emailDetails, scheduleEmailDetails, map, fileSaved.first) }
                        }

                        return workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)
                    }
                    else -> {
                        throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved.first))
                    }
                }
            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved.first))
            }
        }

    }

    @PreAuthorize("hasAuthority('MS_HOD_MODIFY') or hasAuthority('MS_RM_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun updateWorkPlanScheduleAssignHOFStatus(
        referenceNo: String,
        batchReferenceNo: String,
        body: ComplaintAssignDto
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)


        val hofDetailsFound =commonDaoServices.findUserByID(body.assignedIo?: throw ExpectedDataNotFound("Missing Assigned HOF ID"))
        with(workPlanScheduled) {
            hofAssigned = hofDetailsFound.id
        }

        val remarksDto = RemarksToAddDto()
        with(remarksDto){
            remarksDescription= body.assignedRemarks
            remarksStatus= "N/A"
            processID = workPlanScheduled.msProcessId
            userId= loggedInUser.id
        }

        val fileSaved = updateWorkPlanInspectionDetails(workPlanScheduled, map, loggedInUser)
        workPlanScheduled = fileSaved.second
        when (fileSaved.first.status) {
            map.successStatus -> {

                val remarksSaved = workPlanAddRemarksDetails(fileSaved.second.id,remarksDto, map, loggedInUser)
                when (remarksSaved.first.status) {
                    map.successStatus -> {
                        return workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)
                    }
                    else -> {
                        throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved.first))
                    }
                }
            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved.first))
            }
        }

    }

    @PreAuthorize("hasAuthority('MS_HOF_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun updateWorkPlanScheduleAssignIOStatus(
        referenceNo: String,
        batchReferenceNo: String,
        body: ComplaintAssignDto
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)


        with(workPlanScheduled) {
            msProcessId = applicationMapProperties.msComplaintProcessAssignOfficer
            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
            val usersEntity = commonDaoServices.findUserByID(body.assignedIo?: throw ExpectedDataNotFound("Missing Assigned IO ID"))
            officerId = usersEntity.id
            officerName = commonDaoServices.concatenateName(usersEntity)
        }

        val remarksDto = RemarksToAddDto()
        with(remarksDto){
            remarksDescription= body.assignedRemarks
            remarksStatus= "N/A"
            processID = workPlanScheduled.msProcessId
            userId= loggedInUser.id
        }

        val fileSaved = updateWorkPlanInspectionDetails(workPlanScheduled, map, loggedInUser)
        workPlanScheduled = fileSaved.second

        when (fileSaved.first.status) {
            map.successStatus -> {

                val remarksSaved = workPlanAddRemarksDetails(fileSaved.second.id,remarksDto, map, loggedInUser)
                when (remarksSaved.first.status) {
                    map.successStatus -> {
                        return workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)
                    }
                    else -> {
                        throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved.first))
                    }
                }
            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved.first))
            }
        }

    }

    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun updateWorkPlanScheduleInspectionDetailsDestructionNoticeUpload(
        referenceNo: String,
        batchReferenceNo: String,
        body: WorkPlanScheduleApprovalDto
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)

        when {
            body.approvalStatus -> {
                when (batchDetails.batchClosed) {
                    map.activeStatus -> {
                        with(workPlanScheduled){
                            hodRmAssigned = loggedInUser.id
                            msProcessId = applicationMapProperties.mapMSWorkPlanInspectionApproveWorkPlan
                            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
                            approved = "APPROVED"
                            progressStep = approved
                            approvedBy = commonDaoServices.concatenateName(loggedInUser)
                            approvedStatus = map.activeStatus
                            rejectedStatus = map.inactiveStatus
                            approvedOn = commonDaoServices.getCurrentDate()
                        }
                    }
                }
            }
            else -> {
                when (batchDetails.batchClosed) {
                    map.activeStatus -> {
                        with(workPlanScheduled){
                            hodRmAssigned = loggedInUser.id
                            msProcessId = applicationMapProperties.mapMSWorkPlanInspectionRejectWorkPlan
                            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
                            rejected = "REJECTED"
                            progressStep = rejected
                            rejectedBy = commonDaoServices.concatenateName(loggedInUser)
                            rejectedStatus = map.activeStatus
                            approvedStatus = map.inactiveStatus
                            rejectedOn = commonDaoServices.getCurrentDate()
                        }
                    }
                }
            }
        }
        val fileSaved = updateWorkPlanInspectionDetails(workPlanScheduled, map, loggedInUser)
        workPlanScheduled = fileSaved.second
        val remarksDto = RemarksToAddDto()
        with(remarksDto){
            remarksDescription= body.remarks
            remarksStatus= "N/A"
            processID = workPlanScheduled.msProcessId
            userId= loggedInUser.id
        }

        when (fileSaved.first.status) {
            map.successStatus -> {

                val remarksSaved = workPlanAddRemarksDetails(fileSaved.second.id,remarksDto, map, loggedInUser)
                when (remarksSaved.first.status) {
                    map.successStatus -> {
                        return workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)
                    }
                    else -> {
                        throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved.first))
                    }
                }
            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved.first))
            }
        }

    }

    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun updateWorkPlanScheduleInspectionDetailsClientAppealed(
        referenceNo: String,
        batchReferenceNo: String,
        body: ApprovalDto
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
        var remarkStatusValue = "N/A"

        when {
            body.approvalStatus -> {
                with(workPlanScheduled){
                    clientAppealed = map.activeStatus
                    msProcessId = applicationMapProperties.mapMSWorkPlanInspectionClientAppealedAwaitSuccessfull
                    userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
                    remarkStatusValue = "APPEAL SUCCESSFULLY"
                }
            }
            else -> {
                with(workPlanScheduled){
                    clientAppealed = map.inactiveStatus
                    msProcessId = applicationMapProperties.mapMSWorkPlanInspectionPendingDestractionGoodReport
                    userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
                    remarkStatusValue = "APPEAL NOT SUCCESSFULLY"
                }
            }
        }

        val fileSaved = updateWorkPlanInspectionDetails(workPlanScheduled, map, loggedInUser)
        workPlanScheduled = fileSaved.second
        val remarksDto = RemarksToAddDto()
        with(remarksDto){
            remarksDescription= body.remarks
            remarksStatus= remarkStatusValue
            processID = workPlanScheduled.msProcessId
            userId= loggedInUser.id
        }

        when (fileSaved.first.status) {
            map.successStatus -> {

                val remarksSaved = workPlanAddRemarksDetails(fileSaved.second.id,remarksDto, map, loggedInUser)
                when (remarksSaved.first.status) {
                    map.successStatus -> {
                        return workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)
                    }
                    else -> {
                        throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved.first))
                    }
                }
            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved.first))
            }
        }

    }

    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun updateWorkPlanScheduleInspectionDetailsClientAppealSuccessfully(
        referenceNo: String,
        batchReferenceNo: String,
        body: ApprovalDto
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
        var remarkStatusValue = "N/A"

        if (body.approvalStatus) {
                with(workPlanScheduled){
                    destractionStatus = map.inactiveStatus
                    appealStatus = map.activeStatus
                    msProcessId = applicationMapProperties.mapMSWorkPlanInspectionPendingFinalRemarksHODRM
                    userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameHodRm
                    remarkStatusValue = "APPEALED"
                }
        }
        else {
                with(workPlanScheduled){
                    destractionStatus = map.activeStatus
                    appealStatus = map.inactiveStatus
                    msProcessId = applicationMapProperties.mapMSWorkPlanInspectionPendingDestractionGoodReport
                    userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
                    remarkStatusValue = "NOT APPEALED"
                }
        }
        val fileSaved = updateWorkPlanInspectionDetails(workPlanScheduled, map, loggedInUser)
        workPlanScheduled = fileSaved.second
        val remarksDto = RemarksToAddDto()
        with(remarksDto){
            remarksDescription= body.remarks
            remarksStatus= remarkStatusValue
            processID = workPlanScheduled.msProcessId
            userId= loggedInUser.id
        }

        when (fileSaved.first.status) {
            map.successStatus -> {

                val remarksSaved = workPlanAddRemarksDetails(fileSaved.second.id,remarksDto, map, loggedInUser)
                when (remarksSaved.first.status) {
                    map.successStatus -> {
                        return workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)
                    }
                    else -> {
                        throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved.first))
                    }
                }
            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved.first))
            }
        }

    }


    @PreAuthorize("hasAuthority('MS_HOF_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun updateWorkPlanScheduleInspectionDetailsApprovalPreliminaryReportHOF(
        referenceNo: String,
        batchReferenceNo: String,
        body: ApprovalDto
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        var fetchedPreliminary = findPreliminaryReportByWorkPlanGeneratedID(workPlanScheduled.id)?: throw ExpectedDataNotFound("Missing Preliminary Report For Work Plan with REF NR ${workPlanScheduled.referenceNumber}, do Not Exists")
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
        val scheduleEmailDetails =  WorkPlanScheduledDTO()
        var remarkStatusValue = "N/A"
        var emailDetails = ""
        when {
            body.approvalStatus -> {
                when {
                    workPlanScheduled.preliminaryApprovedStatus== map.activeStatus && workPlanScheduled.msFinalReportStatus == map.inactiveStatus -> {
                        with(fetchedPreliminary){
                            approvedHofFinal = "APPROVED FINAL REPORT"
                            approvedRemarksHofFinal = body.remarks
                            approvedByHofFinal = commonDaoServices.concatenateName(loggedInUser)
                            approvedStatusHofFinal = map.activeStatus
                            rejectedStatusHofFinal = map.inactiveStatus
                            approvedOnHofFinal = commonDaoServices.getCurrentDate()
                            remarkStatusValue = approvedHofFinal as String
                            scheduleEmailDetails.approvalStatus = remarkStatusValue
                            emailDetails = applicationMapProperties.mapMsWorkPlanFinalPreliminaryApprovalByHOFEmail
                        }
                    }
                    else -> {
                        with(fetchedPreliminary){
                            approved = "APPROVED"
                            approvedRemarks = body.remarks
                            approvedBy = commonDaoServices.concatenateName(loggedInUser)
                            approvedStatus = map.activeStatus
                            rejectedStatus = map.inactiveStatus
                            approvedOn = commonDaoServices.getCurrentDate()
                            remarkStatusValue = approved as String
                            scheduleEmailDetails.approvalStatus = remarkStatusValue
                            emailDetails = applicationMapProperties.mapMsWorkPlanPreliminaryApprovalByHOFEmail
                        }
                    }
                }

            }
            else -> {
                when {
                    workPlanScheduled.preliminaryApprovedStatus== map.activeStatus && workPlanScheduled.msFinalReportStatus == map.inactiveStatus -> {
                        with(fetchedPreliminary){
                            rejectedHofFinal = "REJECTED FINAL REPORT"
                            rejectedRemarksHofFinal = body.remarks
                            rejectedByHofFinal = commonDaoServices.concatenateName(loggedInUser)
                            approvedStatusHofFinal = map.inactiveStatus
                            rejectedStatusHofFinal = map.activeStatus
                            rejectedOnHofFinal = commonDaoServices.getCurrentDate()
                            remarkStatusValue = rejectedHofFinal as String
                            scheduleEmailDetails.approvalStatus = remarkStatusValue
                            emailDetails = applicationMapProperties.mapMsWorkPlanFinalPreliminaryRejectedByHOFEmail
                        }
                    }
                    else -> {
                        with(fetchedPreliminary){
                            rejected = "REJECTED"
                            rejectedRemarks = body.remarks
                            rejectedBy = commonDaoServices.concatenateName(loggedInUser)
                            approvedStatus = map.inactiveStatus
                            rejectedStatus = map.activeStatus
                            rejectedOn = commonDaoServices.getCurrentDate()
                            remarkStatusValue = rejected as String
                            scheduleEmailDetails.approvalStatus = remarkStatusValue
                            emailDetails = applicationMapProperties.mapMsWorkPlanPreliminaryRejectedByHOFEmail
                        }
                    }
                }

            }
        }

        fetchedPreliminary = updatePreliminaryReportDetails(fetchedPreliminary, map, loggedInUser).second

        with(workPlanScheduled){
            hofAssigned = loggedInUser.id
            when {
                workPlanScheduled.preliminaryApprovedStatus== map.activeStatus && workPlanScheduled.msFinalReportStatus == map.inactiveStatus -> {
                    when {
                        fetchedPreliminary.approvedStatusHofFinal== map.activeStatus -> {
                            msProcessId = applicationMapProperties.mapMSWorkPlanInspectionApproveFinalPreliminaryReportHOF
                            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameHodRm

                        }
                        fetchedPreliminary.rejectedStatusHofFinal== map.activeStatus -> {
                            msProcessId = applicationMapProperties.mapMSWorkPlanInspectionRejectFinalPreliminaryReportHOF
                            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
                            updatedStatus= map.inactiveStatus
                            resubmitStatus= map.inactiveStatus
                        }
                    }
                }
                else -> {
                    when {
                        fetchedPreliminary.approvedStatus== map.activeStatus -> {
                            timelineStartDate = commonDaoServices.getCurrentDate()
                            timelineEndDate = applicationMapProperties.mapMSWorkPlanInspectionApprovePreliminaryReportHOF.let { findProcessNameByID( it, 1).timelinesDay?.let {it2-> commonDaoServices.addYDayToDate(commonDaoServices.getCurrentDate(), it2) } }
                            msProcessId = applicationMapProperties.mapMSWorkPlanInspectionApprovePreliminaryReportHOF
                            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameHodRm
                        }
                        fetchedPreliminary.rejectedStatus== map.activeStatus -> {
                            msProcessId = applicationMapProperties.mapMSWorkPlanInspectionRejectPreliminaryReportHOF
                            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
                            updatedStatus= map.inactiveStatus
                            resubmitStatus= map.inactiveStatus
                        }
                    }

                }
            }

        }
        val fileSaved = updateWorkPlanInspectionDetails(workPlanScheduled, map, loggedInUser)

        when (fileSaved.first.status) {
            map.successStatus -> {
                workPlanScheduled = fileSaved.second
                val remarksDto = RemarksToAddDto()
                with(remarksDto){
                    remarksDescription= body.remarks
                    processID = workPlanScheduled.msProcessId
                    remarksStatus = remarkStatusValue
                    userId= loggedInUser.id
                }
                val remarksSaved = workPlanAddRemarksDetails(fileSaved.second.id,remarksDto, map, loggedInUser)
                when (remarksSaved.first.status) {
                    map.successStatus -> {

                        runBlocking {
                            val hodDetails = workPlanScheduled.hodRmAssigned?.let { commonDaoServices.findUserByID(it) }
                            with(scheduleEmailDetails){
                                baseUrl= applicationMapProperties.baseUrlValue
                                fullName = hodDetails?.let { commonDaoServices.concatenateName(it) }
                                refNumber = referenceNo
                                batchRefNumber = batchReferenceNo
                                yearCodeName = batchDetails.yearNameId?.yearName
                                dateSubmitted = commonDaoServices.getCurrentDate()

                            }
                            if (hodDetails != null) {
                                commonDaoServices.sendEmailWithUserEntity(hodDetails, emailDetails, scheduleEmailDetails, map, remarksSaved.first)
                            }
                        }
                        return workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)
                    }
                    else -> {
                        throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved.first))
                    }
                }
            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved.first))
            }
        }

    }

    @PreAuthorize("hasAuthority('MS_HOD_MODIFY') or hasAuthority('MS_RM_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun updateWorkPlanScheduleInspectionDetailsApprovalPreliminaryReportHOD(
        referenceNo: String,
        batchReferenceNo: String,
        body: ApprovalDto
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        var fetchedPreliminary = findPreliminaryReportByWorkPlanGeneratedID(workPlanScheduled.id) ?: throw ExpectedDataNotFound("Missing Preliminary Report For Work Plan with REF NR ${workPlanScheduled.referenceNumber}, do Not Exists")
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
        val scheduleEmailDetails =  WorkPlanScheduledDTO()
        var emailDetails = ""

        var remarkStatusValue = "N/A"
        when {
            body.approvalStatus -> {
                when {
                    workPlanScheduled.preliminaryApprovedStatus== map.activeStatus
                            && workPlanScheduled.msFinalReportStatus == map.inactiveStatus
                            && fetchedPreliminary.approvedStatusHofFinal == map.activeStatus -> {
                        with(fetchedPreliminary){
                            approvedHodFinal = "APPROVED"
                            approvedRemarksHodFinal = body.remarks
                            approvedByHodFinal = commonDaoServices.concatenateName(loggedInUser)
                            approvedStatusHodFinal = map.activeStatus
                            rejectedStatusHodFinal = map.inactiveStatus
                            approvedOnHodFinal = commonDaoServices.getCurrentDate()
                            remarkStatusValue = approvedHodFinal as String
                            scheduleEmailDetails.approvalStatus = remarkStatusValue
                            emailDetails = applicationMapProperties.mapMsWorkPlanFinalPreliminaryApprovalByHODEmail
                        }
                    }
                    fetchedPreliminary.approvedStatus == map.activeStatus -> {
                        with(fetchedPreliminary){
                            approvedHod = "APPROVED"
                            approvedRemarksHod = body.remarks
                            approvedByHod = commonDaoServices.concatenateName(loggedInUser)
                            approvedStatusHod = map.activeStatus
                            rejectedStatusHod = map.inactiveStatus
                            approvedOnHod = commonDaoServices.getCurrentDate()
                            remarkStatusValue = approvedHod as String
                            scheduleEmailDetails.approvalStatus = remarkStatusValue
                            emailDetails = applicationMapProperties.mapMsWorkPlanPreliminaryApprovalByHODEmail
                        }
                    }
                }


            }
            else -> {
                when {
                    workPlanScheduled.preliminaryApprovedStatus== map.activeStatus
                            && workPlanScheduled.msFinalReportStatus == map.inactiveStatus
                            && fetchedPreliminary.approvedStatusHofFinal == map.activeStatus -> {
                        with(fetchedPreliminary){
                            rejectedHodFinal = "REJECTED"
                            rejectedRemarksHodFinal = body.remarks
                            rejectedByHodFinal = commonDaoServices.concatenateName(loggedInUser)
                            approvedStatusHodFinal = map.inactiveStatus
                            rejectedStatusHodFinal = map.activeStatus
                            rejectedOnHodFinal = commonDaoServices.getCurrentDate()
                            remarkStatusValue = rejectedHodFinal as String
                            scheduleEmailDetails.approvalStatus = remarkStatusValue
                            emailDetails = applicationMapProperties.mapMsWorkPlanFinalPreliminaryRejectedByHODEmail
                        }
                    }
                    fetchedPreliminary.approvedStatus == map.activeStatus -> {
                        with(fetchedPreliminary){
                            rejectedHod = "REJECTED"
                            rejectedRemarksHod = body.remarks
                            rejectedByHod = commonDaoServices.concatenateName(loggedInUser)
                            approvedStatusHod = map.inactiveStatus
                            rejectedStatusHod = map.activeStatus
                            rejectedOnHod = commonDaoServices.getCurrentDate()
                            remarkStatusValue = rejectedHod as String
                            scheduleEmailDetails.approvalStatus = remarkStatusValue
                            emailDetails = applicationMapProperties.mapMsWorkPlanPreliminaryRejectedByHODEmail
                        }
                    }
                }
            }
        }

        fetchedPreliminary =  updatePreliminaryReportDetails(fetchedPreliminary, map, loggedInUser).second

        with(workPlanScheduled){
            when {
                workPlanScheduled.preliminaryApprovedStatus== map.activeStatus
                        && workPlanScheduled.msFinalReportStatus == map.inactiveStatus
                        && fetchedPreliminary.approvedStatusHofFinal == map.activeStatus -> {
                    when {
                        fetchedPreliminary.approvedStatusHodFinal== map.activeStatus -> {
                            msFinalReportStatus = map.activeStatus
                            msProcessId = applicationMapProperties.mapMSWorkPlanInspectionApproveFinalPreliminaryReportHODRM
                            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameHodRm
                        }
                        fetchedPreliminary.rejectedStatusHodFinal== map.activeStatus -> {
                            msProcessId = applicationMapProperties.mapMSWorkPlanInspectionRejectFinalPreliminaryReportHODRM
                            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameHof
                            updatedStatus = map.inactiveStatus
                            resubmitStatus = map.inactiveStatus
                        }
                    }
                }
                fetchedPreliminary.approvedStatus == map.activeStatus -> {
                    when {
                        fetchedPreliminary.approvedStatusHod== map.activeStatus -> {
                            msFinalReportStatus = map.inactiveStatus
                            preliminaryApprovedStatus = map.activeStatus
                            msProcessId = applicationMapProperties.mapMSWorkPlanInspectionApprovePreliminaryReportHODRM
                            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
                        }
                        fetchedPreliminary.rejectedStatusHod== map.activeStatus -> {
                            msProcessId = applicationMapProperties.mapMSWorkPlanInspectionRejectPreliminaryReportHODRM
                            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameHof
                            updatedStatus = map.inactiveStatus
                            resubmitStatus = map.inactiveStatus
                        }
                    }

                }
            }

        }
        val fileSaved = updateWorkPlanInspectionDetails(workPlanScheduled, map, loggedInUser)

        when (fileSaved.first.status) {
            map.successStatus -> {
                workPlanScheduled = fileSaved.second
                val remarksDto = RemarksToAddDto()
                with(remarksDto){
                    remarksDescription= body.remarks
                    processID = workPlanScheduled.msProcessId
                    remarksStatus = remarkStatusValue
                    userId= loggedInUser.id
                }
                val remarksSaved = workPlanAddRemarksDetails(fileSaved.second.id,remarksDto, map, loggedInUser)
                when (remarksSaved.first.status) {
                    map.successStatus -> {
                        runBlocking {
                            val ioDetails = workPlanScheduled.officerId?.let { commonDaoServices.findUserByID(it) }
                            with(scheduleEmailDetails){
                                baseUrl= applicationMapProperties.baseUrlValue
                                fullName = ioDetails?.let { commonDaoServices.concatenateName(it) }
                                refNumber = referenceNo
                                batchRefNumber = batchReferenceNo
                                yearCodeName = batchDetails.yearNameId?.yearName
                                dateSubmitted = commonDaoServices.getCurrentDate()

                            }
                            if (ioDetails != null) {
                                commonDaoServices.sendEmailWithUserEntity(ioDetails, emailDetails, scheduleEmailDetails, map, remarksSaved.first)
                            }
                        }
                        return workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)
                    }
                    else -> {
                        throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved.first))
                    }
                }
            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved.first))
            }
        }

    }

    @PreAuthorize("hasAuthority('MS_HOD_MODIFY') or hasAuthority('MS_RM_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun addWorkPlanScheduleFinalRecommendationByHOD(
        referenceNo: String,
        batchReferenceNo: String,
        body: WorkPlanFinalRecommendationDto
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        val fetchedPreliminary = findPreliminaryReportByWorkPlanGeneratedID(workPlanScheduled.id) ?: throw ExpectedDataNotFound("Missing Preliminary Report For Work Plan with REF NR ${workPlanScheduled.referenceNumber}, do Not Exists")
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)

        var remarkStatusValue = "N/A"
        var emailDetails = ""
        val scheduleEmailDetails =  WorkPlanScheduledDTO()
        val recommendationDetails = recommendationRepo.findByIdOrNull(body.recommendationId) ?: throw ExpectedDataNotFound("Missing Recommendation details with ID ${body.recommendationId}, do Not Exists")
        with(workPlanScheduled){
            when {
                workPlanScheduled.msFinalReportStatus == map.activeStatus && fetchedPreliminary.approvedStatusHodFinal == map.activeStatus -> {
                    hodRecommendationRemarks = body.hodRecommendationRemarks
                    hodRecommendation = recommendationDetails.recommendationName
                    remarkStatusValue = recommendationDetails.recommendationName.toString()
                    hodRecommendationStatus = map.activeStatus
                    when (recommendationDetails.id) {
                        applicationMapProperties.mapMsWorkPlanDestrctionID -> {
                            destructionRecommended = map.activeStatus
                            msProcessId = applicationMapProperties.mapMSWorkPlanInspectionFinalRecommendationPendingDestruction
                            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
                            scheduleEmailDetails.approvalStatus = remarkStatusValue
                            emailDetails = applicationMapProperties.mapMsOfficerWorkPlanDestructionEmail
                        }
                        else -> {
                            destructionRecommended = map.inactiveStatus
                            msProcessId = applicationMapProperties.mapMSWorkPlanInspectionFinalRecommendationPendingFinalRemarks
                            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameHodRm
                            scheduleEmailDetails.approvalStatus = remarkStatusValue
                            emailDetails = applicationMapProperties.mapMsOfficerWorkPlanNotDestructionEmail
                        }
                    }

                }
            }

        }
        val fileSaved = updateWorkPlanInspectionDetails(workPlanScheduled, map, loggedInUser)

        when (fileSaved.first.status) {
            map.successStatus -> {
                workPlanScheduled = fileSaved.second
                val remarksDto = RemarksToAddDto()
                with(remarksDto){
                    remarksDescription= body.hodRecommendationRemarks
                    remarksStatus = remarkStatusValue
                    processID = workPlanScheduled.msProcessId
                    userId= loggedInUser.id
                }
                val remarksSaved = workPlanAddRemarksDetails(fileSaved.second.id,remarksDto, map, loggedInUser)
                when (remarksSaved.first.status) {
                    map.successStatus -> {
                        runBlocking {
                            val ioDetails = workPlanScheduled.officerId?.let { commonDaoServices.findUserByID(it) }
                            with(scheduleEmailDetails){
                                baseUrl= applicationMapProperties.baseUrlValue
                                fullName = ioDetails?.let { commonDaoServices.concatenateName(it) }
                                refNumber = referenceNo
                                batchRefNumber = batchReferenceNo
                                yearCodeName = batchDetails.yearNameId?.yearName
                                dateSubmitted = commonDaoServices.getCurrentDate()

                            }
                            if (ioDetails != null) {
                                commonDaoServices.sendEmailWithUserEntity(ioDetails, emailDetails, scheduleEmailDetails, map, remarksSaved.first)
                            }
                        }
                        return workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)
                    }
                    else -> {
                        throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved.first))
                    }
                }
            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved.first))
            }
        }

    }

    @PreAuthorize("hasAuthority('MS_HOD_MODIFY') or hasAuthority('MS_RM_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun addWorkPlanScheduleFeedBackByHOD(
        referenceNo: String,
        batchReferenceNo: String,
        body: WorkPlanFeedBackDto
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)

        with(workPlanScheduled){
            msProcessEndedStatus = map.activeStatus
            msEndProcessRemarks = body.hodFeedBackRemarks
            msProcessId = applicationMapProperties.mapMSWorkPlanInspectionENDMSProcess
            userTaskId = null

        }
        val fileSaved = updateWorkPlanInspectionDetails(workPlanScheduled, map, loggedInUser)

        when (fileSaved.first.status) {
            map.successStatus -> {
                workPlanScheduled = fileSaved.second
                val remarksDto = RemarksToAddDto()
                with(remarksDto){
                    remarksDescription= body.hodFeedBackRemarks
                    remarksStatus= "N/A"
                    processID = workPlanScheduled.msProcessId
                    userId= loggedInUser.id
                }
                val remarksSaved = workPlanAddRemarksDetails(fileSaved.second.id,remarksDto, map, loggedInUser)
                when (remarksSaved.first.status) {
                    map.successStatus -> {
                        runBlocking {
                            val ioDetails = workPlanScheduled.officerId?.let { commonDaoServices.findUserByID(it) }
                            val scheduleEmailDetails =  WorkPlanScheduledDTO()
                            with(scheduleEmailDetails){
                                baseUrl= applicationMapProperties.baseUrlValue
                                fullName = ioDetails?.let { commonDaoServices.concatenateName(it) }
                                refNumber = referenceNo
                                batchRefNumber = batchReferenceNo
                                remarksDetails = body.hodFeedBackRemarks
                                yearCodeName = batchDetails.yearNameId?.yearName
                                dateSubmitted = commonDaoServices.getCurrentDate()

                            }
                            ioDetails?.email?.let { commonDaoServices.sendEmailWithUserEmail(it, applicationMapProperties.mapMsHodFinalFeedBackNotificationEmail, scheduleEmailDetails, map, remarksSaved.first) }
                            val hofDetails = workPlanScheduled.hofAssigned?.let { commonDaoServices.findUserByID(it) }
                            scheduleEmailDetails.fullName = hofDetails?.let { commonDaoServices.concatenateName(it) }
                            hofDetails?.email?.let { commonDaoServices.sendEmailWithUserEmail(it, applicationMapProperties.mapMsHodFinalFeedBackNotificationEmail, scheduleEmailDetails, map, remarksSaved.first) }
                            val directorDetails = workPlanScheduled.directorAssigned?.let { commonDaoServices.findUserByID(it) }
                            scheduleEmailDetails.fullName = directorDetails?.let { commonDaoServices.concatenateName(it) }
                            directorDetails?.email?.let { commonDaoServices.sendEmailWithUserEmail(it, applicationMapProperties.mapMsHodFinalFeedBackNotificationEmail, scheduleEmailDetails, map, remarksSaved.first) }
                            scheduleEmailDetails.fullName = workPlanScheduled.destructionClientFullName
                            workPlanScheduled.destructionClientEmail?.let { commonDaoServices.sendEmailWithUserEmail(it, applicationMapProperties.mapMsHodFinalFeedBackNotificationEmail, scheduleEmailDetails, map, remarksSaved.first) }
                        }

                        return workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)
                    }
                    else -> {
                        throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved.first))
                    }
                }
            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved.first))
            }
        }

    }

    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun updateWorkPlanScheduleInspectionDetailsFinalPreliminaryReport(
        referenceNo: String,
        batchReferenceNo: String,
        body: PreliminaryReportFinalDto
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
        var workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        var fetchedPreliminary = findPreliminaryReportByWorkPlanGeneratedID(workPlanScheduled.id) ?: throw ExpectedDataNotFound("Missing Preliminary Report For Work Plan with REF NR ${workPlanScheduled.referenceNumber}, do Not Exists")

        with(fetchedPreliminary){
            surveillanceConclusions = body.surveillanceConclusions
            surveillanceRecommendation = body.surveillanceRecommendation
        }

        fetchedPreliminary =  updatePreliminaryReportDetails(fetchedPreliminary, map, loggedInUser).second

        with(workPlanScheduled){
            if (finalReportGenerated == map.activeStatus){
                resubmitStatus = map.activeStatus
                updatedStatus = map.activeStatus
            }
            finalReportGenerated= map.activeStatus
            msProcessId = applicationMapProperties.mapMSWorkPlanInspectionGenerateFinalPreliminaryReport
            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameHof
        }

        val fileSaved = updateWorkPlanInspectionDetails(workPlanScheduled, map, loggedInUser)

        when (fileSaved.first.status) {
            map.successStatus -> {
                workPlanScheduled = fileSaved.second
                val remarksDto = RemarksToAddDto()
                with(remarksDto){
                    remarksDescription= body.remarks
                    remarksStatus= "N/A"
                    processID = workPlanScheduled.msProcessId
                    userId= loggedInUser.id
                }
                val remarksSaved = workPlanAddRemarksDetails(fileSaved.second.id,remarksDto, map, loggedInUser)
                when (remarksSaved.first.status) {
                    map.successStatus -> {
                        runBlocking {
                            val hofDetails = workPlanScheduled.hofAssigned?.let { commonDaoServices.findUserByID(it) }
                                    val scheduleEmailDetails =  WorkPlanScheduledDTO()
                                    with(scheduleEmailDetails){
                                        baseUrl= applicationMapProperties.baseUrlValue
                                        fullName = hofDetails?.let { commonDaoServices.concatenateName(it) }
                                        refNumber = referenceNo
                                        batchRefNumber = batchReferenceNo
                                        yearCodeName = batchDetails.yearNameId?.yearName
                                        dateSubmitted = commonDaoServices.getCurrentDate()

                                    }
                            if (hofDetails != null) {
                                commonDaoServices.sendEmailWithUserEntity(hofDetails, applicationMapProperties.mapMsWorkPlanFinalPreliminarySubmitedApprovalEmailHOF, scheduleEmailDetails, map, remarksSaved.first)
                            }

                        }
                        return workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)
                    }
                    else -> {
                        throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved.first))
                    }
                }
            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved.first))
            }
        }

    }

    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun addWorkPlanInspectionDetailsChargeSheet(
        referenceNo: String,
        batchReferenceNo: String,
        body: ChargeSheetDto
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
        val chargeSheetFileSaved = workPlanInspectionDetailsAddChargeSheet(body, workPlanScheduled, map, loggedInUser)
//        val remarksDto = RemarksToAddDto()
//        with(remarksDto){
//            remarksDescription= body.remarks
//            remarksStatus= "N/A"
//            processID = workPlanScheduled.msProcessId
//            userId= loggedInUser.id
//        }

        when (chargeSheetFileSaved.first.status) {
            map.successStatus -> {
                with(workPlanScheduled) {
                    userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
//                    msProcessId = applicationMapProperties.mapMSRapidTest
                    chargeSheetStatus = map.activeStatus
                }
                val fileSaved2 = updateWorkPlanInspectionDetails(workPlanScheduled, map, loggedInUser)
                when (fileSaved2.first.status) {
                    map.successStatus -> {
                        workPlanScheduled = fileSaved2.second
                        return workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)
//                        val remarksSaved = workPlanAddRemarksDetails(fileSaved2.second.id,remarksDto, map, loggedInUser)
//                        when (remarksSaved.first.status) {
//                            map.successStatus -> {
//                                return workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)
//                            }
//                            else -> {
//                                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved2.first))
//                            }
//                        }
                    }
                    else -> {
                        throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved2.first))
                    }
                }
            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(chargeSheetFileSaved.first))
            }
        }
    }

    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun addWorkPlanInspectionDetailsDataReport(
        referenceNo: String,
        batchReferenceNo: String,
        body: DataReportDto
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
        val dataReportFileSaved = workPlanInspectionDetailsAddDataReport(body, workPlanScheduled, map, loggedInUser)
//        val remarksDto = RemarksToAddDto()
//        with(remarksDto){
//            remarksDescription= body.remarks
//            remarksStatus= "N/A"
//            processID = workPlanScheduled.msProcessId
//            userId= loggedInUser.id
//        }

        when (dataReportFileSaved.first.status) {
            map.successStatus -> {
                body.productsList?.forEach { param->
                    workPlanInspectionDetailsAddDataReportParams(param, dataReportFileSaved.second, map, loggedInUser)
                }
                with(workPlanScheduled) {
                    userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
//                    msProcessId = applicationMapProperties.mapMSRapidTest
                    dataReportStatus = map.activeStatus
                }
                val fileSaved2 = updateWorkPlanInspectionDetails(workPlanScheduled, map, loggedInUser)
                when (fileSaved2.first.status) {
                    map.successStatus -> {
                        workPlanScheduled = fileSaved2.second
                        return workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)
//                        val remarksSaved = workPlanAddRemarksDetails(fileSaved2.second.id,remarksDto, map, loggedInUser)
//                        when (remarksSaved.first.status) {
//                            map.successStatus -> {
//                                return workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)
//                            }
//                            else -> {
//                                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved2.first))
//                            }
//                        }
                    }
                    else -> {
                        throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved2.first))
                    }
                }
            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(dataReportFileSaved.first))
            }
        }
    }

    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun addWorkPlanInspectionDetailsSeizureDeclaration(
        referenceNo: String,
        batchReferenceNo: String,
        body: SeizureDeclarationDto
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
        val dataFileSaved = workPlanInspectionDetailsAddSeizureDeclaration(body, workPlanScheduled, map, loggedInUser)
//        val remarksDto = RemarksToAddDto()
//        with(remarksDto){
//            remarksDescription= body.remarks
//            remarksStatus= "N/A"
//            processID = workPlanScheduled.msProcessId
//            userId= loggedInUser.id
//        }

        when (dataFileSaved.first.status) {
            map.successStatus -> {
                with(workPlanScheduled) {
                    userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
//                    msProcessId = applicationMapProperties.mapMSRapidTest
                    seizureDeclarationStatus = map.activeStatus
                }
                val fileSaved2 = updateWorkPlanInspectionDetails(workPlanScheduled, map, loggedInUser)
                when (fileSaved2.first.status) {
                    map.successStatus -> {
                        workPlanScheduled = fileSaved2.second
//                        val remarksSaved = workPlanAddRemarksDetails(fileSaved2.second.id,remarksDto, map, loggedInUser)
//                        when (remarksSaved.first.status) {
//                            map.successStatus -> {
                                return workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)
//                            }
//                            else -> {
//                                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved2.first))
//                            }
//                        }
                    }
                    else -> {
                        throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved2.first))
                    }
                }
            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(dataFileSaved.first))
            }
        }
    }

    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun addWorkPlanInspectionDetailsInspectionInvestigationReport(
        referenceNo: String,
        batchReferenceNo: String,
        body: InspectionInvestigationReportDto
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
        val dataFileSaved = workPlanInspectionDetailsAddInspectionInvestigationReport(body, workPlanScheduled, map, loggedInUser)
        val remarksDto = RemarksToAddDto()
        with(remarksDto){
            remarksDescription= body.remarks
            remarksStatus= "N/A"
            processID = workPlanScheduled.msProcessId
            userId= loggedInUser.id
        }

        when (dataFileSaved.first.status) {
            map.successStatus -> {
                with(workPlanScheduled) {
                    userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
//                    msProcessId = applicationMapProperties.mapMSRapidTest
                    investInspectReportStatus = map.activeStatus
                }
                val fileSaved2 = updateWorkPlanInspectionDetails(workPlanScheduled, map, loggedInUser)
                when (fileSaved2.first.status) {
                    map.successStatus -> {
                        workPlanScheduled = fileSaved2.second
                        val remarksSaved = workPlanAddRemarksDetails(fileSaved2.second.id,remarksDto, map, loggedInUser)
                        when (remarksSaved.first.status) {
                            map.successStatus -> {
                                return workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)
                            }
                            else -> {
                                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved2.first))
                            }
                        }
                    }
                    else -> {
                        throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved2.first))
                    }
                }
            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(dataFileSaved.first))
            }
        }
    }

    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun addWorkPlanInspectionDetailsSampleCollection(
        referenceNo: String,
        batchReferenceNo: String,
        body: SampleCollectionDto
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
        val savedSampleCollection = msFuelDaoServices.addSampleCollectAdd(body,null,workPlanScheduled, map, loggedInUser)

        when (savedSampleCollection.first.status) {
            map.successStatus -> {
                body.productsList?.forEach { param->
                    msFuelDaoServices.addSampleCollectParamAdd(param,savedSampleCollection.second,map,loggedInUser)
                }
                with(workPlanScheduled){
                    userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
//                    msProcessId = applicationMapProperties.mapMSSampleSubmision
                    sampleCollectionStatus = map.activeStatus
                }
                workPlanScheduled = updateWorkPlanInspectionDetails(workPlanScheduled, map, loggedInUser).second
                return workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)
            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(savedSampleCollection.first))
            }
        }


    }

    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun addWorkPlanInspectionDetailsSampleSubmission(
        referenceNo: String,
        batchReferenceNo: String,
        body: SampleSubmissionDto
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
        val sampleCollected = findSampleCollectedDetailByWorkPlanInspectionID(workPlanScheduled.id)
        val savedSampleSubmission = msFuelDaoServices.addSampleSubmissionAdd(body,null,workPlanScheduled,sampleCollected, map, loggedInUser)

        when (savedSampleSubmission.first.status) {
            map.successStatus -> {
                body.parametersList?.forEach { param->
                    msFuelDaoServices.addSampleSubmissionParamAdd(param,savedSampleSubmission.second,map,loggedInUser)
                }
                with(workPlanScheduled){
                    userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
//                    msProcessId = applicationMapProperties.mapMSWorkPlanInspectionAddBsNumber
                    sampleSubmittedStatus = map.activeStatus
                }
                workPlanScheduled = updateWorkPlanInspectionDetails(workPlanScheduled, map, loggedInUser).second
                return workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)
            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(savedSampleSubmission.first))
            }
        }


    }

    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun addWorkPlanInspectionDetailsPreliminaryReport(
        referenceNo: String,
        batchReferenceNo: String,
        body: PreliminaryReportDto
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
        val dataFileSaved = workPlanInspectionDetailsAddPreliminaryReport(body, workPlanScheduled, map, loggedInUser)

        when (dataFileSaved.first.status) {
            map.successStatus -> {
                body.parametersList?.forEach { param->
                    addPreliminaryReportParamAdd(param,dataFileSaved.second,map,loggedInUser)
                }
                when (workPlanScheduled.onsiteEndStatus) {
                    map.activeStatus -> {
                        with(workPlanScheduled) {
                            timelineStartDate = commonDaoServices.getCurrentDate()
                            timelineEndDate = applicationMapProperties.mapMSWorkPlanInspectionGeneratePreliminaryReport.let { findProcessNameByID( it, 1).timelinesDay?.let {it2-> commonDaoServices.addYDayToDate(commonDaoServices.getCurrentDate(), it2) } }
                            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameHof
                            msProcessId = applicationMapProperties.mapMSWorkPlanInspectionGeneratePreliminaryReport
                            if (msPreliminaryReportStatus == map.activeStatus){
                                resubmitStatus = map.activeStatus
                                updatedStatus = map.activeStatus
                            }
                            msPreliminaryReportStatus = map.activeStatus
                        }
                    }
                    else -> {
                        throw ExpectedDataNotFound("MS ON-SITE ACTIVITIES NOT ENDED YET")
                    }
                }

                val fileSaved2 = updateWorkPlanInspectionDetails(workPlanScheduled, map, loggedInUser)
                when (fileSaved2.first.status) {
                    map.successStatus -> {
                        workPlanScheduled = fileSaved2.second
                        val remarksDto = RemarksToAddDto()
                        with(remarksDto){
                            remarksDescription= body.remarks
                            remarksStatus= "N/A"
                            processID = workPlanScheduled.msProcessId
                            userId= loggedInUser.id
                        }
                        val remarksSaved = workPlanAddRemarksDetails(fileSaved2.second.id,remarksDto, map, loggedInUser)
                        when (remarksSaved.first.status) {
                            map.successStatus -> {
                                val hofList = commonDaoServices.findOfficersListBasedOnRole(applicationMapProperties.mapMSComplaintWorkPlanMappedHOFROLEID,
                                    workPlanScheduled.county ?: throw ExpectedDataNotFound("MISSING WORK-PLAN COUNTY ID"),
                                    workPlanScheduled.region ?: throw ExpectedDataNotFound("MISSING WORK-PLAN REGION ID")
                                )

                                runBlocking {
                                    hofList
                                        ?.forEach { mp->
                                            val scheduleEmailDetails =  WorkPlanScheduledDTO()
                                            with(scheduleEmailDetails){
                                                baseUrl= applicationMapProperties.baseUrlValue
                                                fullName = commonDaoServices.concatenateName(mp)
                                                refNumber = referenceNo
                                                batchRefNumber = batchReferenceNo
                                                yearCodeName = batchDetails.yearNameId?.yearName
                                                dateSubmitted = commonDaoServices.getCurrentDate()

                                            }
                                            commonDaoServices.sendEmailWithUserEntity(mp, applicationMapProperties.mapMsWorkPlanPreliminarySubmitedApprovalEmailHOF, scheduleEmailDetails, map, remarksSaved.first)
                                        }
                                }
                                return workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)
                            }
                            else -> {
                                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved2.first))
                            }
                        }
                    }
                    else -> {
                        throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved2.first))
                    }
                }
            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(dataFileSaved.first))
            }
        }
    }

    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun addWorkPlanInspectionDetailsSampleSubmissionBSNumber(
        referenceNo: String,
        batchReferenceNo: String,
        body: BSNumberSaveDto
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
        val sampleCollected = findSampleCollectedDetailByWorkPlanInspectionID(workPlanScheduled.id)
        val sampleSubmission = when {
            sampleCollected!= null -> {
                msFuelDaoServices.findSampleSubmissionDetailBySampleCollectedID(sampleCollected.id?: throw ExpectedDataNotFound("MISSING SAMPLE COLLECTED FOR WORK-PLAN SCHEDULED INSPECTION REF NO $referenceNo"))?: throw ExpectedDataNotFound("MISSING SAMPLE SUBMITTED FOR WORK-PLAN SCHEDULED WITH REF NO $referenceNo")
            }
            else -> {
                findSampleSubmissionDetailByWorkPlanGeneratedID(workPlanScheduled.id)?: throw ExpectedDataNotFound("MISSING SAMPLE SUBMITTED FOR WORK-PLAN SCHEDULED WITH REF NO $referenceNo")
            }
        }

        with(sampleSubmission){
            bsNumber = body.bsNumber
            sampleBsNumberDate = body.submittedDate
            sampleBsNumberRemarks = body.remarks
            labResultsStatus = map.inactiveStatus
        }
        val updatedSampleSubmission = msFuelDaoServices.sampleSubmissionUpdateDetails(sampleSubmission,map, loggedInUser)

        when (updatedSampleSubmission.first.status) {
            map.successStatus -> {
                val savedBsNumber = msFuelDaoServices.ssfSaveBSNumber(updatedSampleSubmission.second,null,workPlanScheduled,loggedInUser, map)
                when (savedBsNumber.first.status) {
                    map.successStatus -> {
                        with(workPlanScheduled){
                            timelineStartDate = commonDaoServices.getCurrentDate()
                            timelineEndDate = applicationMapProperties.mapMSWorkPlanInspectionAddBsNumber.let { findProcessNameByID( it, 1).timelinesDay?.let {it2-> commonDaoServices.addYDayToDate(commonDaoServices.getCurrentDate(), it2) } }
                            msProcessId = applicationMapProperties.mapMSWorkPlanInspectionAddBsNumber
                            userTaskId = applicationMapProperties.mapMSUserTaskNameLAB
                            bsNumberStatus = 1
                            ssfLabparamsStatus = map.activeStatus
                        }
                        workPlanScheduled = updateWorkPlanInspectionDetails(workPlanScheduled, map, loggedInUser).second
                        val remarksDto = RemarksToAddDto()
                        with(remarksDto){
                            remarksDescription= body.remarks
                            remarksStatus= "N/A"
                            processID = workPlanScheduled.msProcessId
                            userId= loggedInUser.id
                        }
                        val remarksSaved = workPlanAddRemarksDetails(workPlanScheduled.id,remarksDto, map, loggedInUser)
                        when (remarksSaved.first.status) {
                            map.successStatus -> {
                                return workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)

                            }
                            else -> {
                                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(remarksSaved.first))
                            }
                        }
                    }
                    else -> {
                        throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(savedBsNumber.first))
                    }
                }

            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(updatedSampleSubmission.first))
            }
        }
    }

    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun addWorkPlanInspectionDetailsLabPDFSelected(
        referenceNo: String,
        batchReferenceNo: String,
        body: PDFSaveComplianceStatusDto
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
        val fileContent = limsServices.mainFunctionLimsGetPDF(body.bsNumber, body.PDFFileName)
        val savedPDFLabResultFile = msFuelDaoServices.addInspectionSaveLIMSPDFSelected(fileContent,body,false,map,loggedInUser)

        when (savedPDFLabResultFile.first.status) {
            map.successStatus -> {
                with(workPlanScheduled){
                    msProcessId = applicationMapProperties.mapMSWorkPlanInspectionLabResultsPDFSave
                    userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
                }
                workPlanScheduled = updateWorkPlanInspectionDetails(workPlanScheduled, map, loggedInUser).second
                val remarksDto = RemarksToAddDto()
                with(remarksDto){
                    remarksDescription= body.complianceRemarks
                    remarksStatus= "N/A"
                    processID = workPlanScheduled.msProcessId
                    userId= loggedInUser.id
                }
                val remarksSaved = workPlanAddRemarksDetails(workPlanScheduled.id,remarksDto, map, loggedInUser)
                when (remarksSaved.first.status) {
                    map.successStatus -> {
                        return workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)
                    }
                    else -> {
                        throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(remarksSaved.first))
                    }
                }

            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(savedPDFLabResultFile.first))
            }
        }
    }

    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun addWorkPlanInspectionDetailsSSFSaveComplianceStatus(
        referenceNo: String,
        batchReferenceNo: String,
        body: SSFSaveComplianceStatusDto
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
        val savedSSfComplianceStatus = msFuelDaoServices.ssfLabUpdateDetails(body,loggedInUser,map)

        var remarkStatusValue = "N/A"

        when (savedSSfComplianceStatus.first.status) {
            map.successStatus -> {
                with(workPlanScheduled){
                    when {
                        body.complianceStatus -> {
                            compliantStatus = 1
                            notCompliantStatus =  0
                            compliantStatusDate = commonDaoServices.getCurrentDate()
                            compliantStatusBy = commonDaoServices.concatenateName(loggedInUser)
                            compliantStatusRemarks = body.complianceRemarks
                            remarkStatusValue = "COMPLIANT"
                        }
                        else -> {
                            notCompliantStatus =  0
                            compliantStatus = 0
                            notCompliantStatusDate = commonDaoServices.getCurrentDate()
                            notCompliantStatusBy = commonDaoServices.concatenateName(loggedInUser)
                            notCompliantStatusRemarks = body.complianceRemarks
                            remarkStatusValue = "NOT-COMPLIANT"
                        }
                    }
                }
                with(workPlanScheduled){
                    timelineStartDate = commonDaoServices.getCurrentDate()
                    timelineEndDate = applicationMapProperties.mapMSWorkPlanInspectionAnalysesLabResults.let { findProcessNameByID( it, 1).timelinesDay?.let {it2-> commonDaoServices.addYDayToDate(commonDaoServices.getCurrentDate(), it2) } }
                    msProcessId = applicationMapProperties.mapMSWorkPlanInspectionAnalysesLabResults
                    userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
                }
                workPlanScheduled = updateWorkPlanInspectionDetails(workPlanScheduled, map, loggedInUser).second
                val remarksDto = RemarksToAddDto()
                with(remarksDto){
                    remarksDescription= body.complianceRemarks
                    processID = workPlanScheduled.msProcessId
                    userId= loggedInUser.id
                }

                remarksDto.remarksStatus = remarkStatusValue
                val remarksSaved = workPlanAddRemarksDetails(workPlanScheduled.id,remarksDto, map, loggedInUser)
                when (remarksSaved.first.status) {
                    map.successStatus -> {
                        return workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)
                    }
                    else -> {
                        throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(remarksSaved.first))
                    }
                }

            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(savedSSfComplianceStatus.first))
            }
        }
    }
    
//    @PreAuthorize("hasAuthority('MS_IO_READ') or hasAuthority('MS_HOD_READ') or hasAuthority('MS_RM_READ') or hasAuthority('MS_HOF_READ') or hasAuthority('MS_DIRECTOR_READ')")
//    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
//    fun msWorPlanInspectionOnGoingLists(page: PageRequest): ApiResponseModel {
//        val response: ApiResponseModel
//        val auth = commonDaoServices.loggedInUserAuthentication()
//        val map = commonDaoServices.serviceMapDetails(appId)
//        val loggedInUser = commonDaoServices.loggedInUserDetails()
//        val userProfile = commonDaoServices.findUserProfileByUserID(loggedInUser)
//        when {
//            auth.authorities.stream().anyMatch { authority ->
//                authority.authority == "MS_IO_READ"
//                        || authority.authority == "MS_HOD_READ"
//                        || authority.authority == "MS_HOF_READ"
//                        || authority.authority == "MS_DIRECTOR_READ"
//                        || authority.authority == "MS_RM_READ" } -> {
//                val complaintList = complaintsRepo.findOngoingTask(userProfile.regionId?.id ?: throw ExpectedDataNotFound("Missing Logged In Region ID"),userProfile.countyID?.id ?: throw ExpectedDataNotFound("Missing Logged In County ID"))
//                val usersPage: Page<ComplaintEntity> = PageImpl(complaintList, page, complaintList.size.toLong())
//                response = listMsComplaints(usersPage,map)
//            }
//            else -> throw ExpectedDataNotFound("Can't access this page Due to Invalid authority")
//        }
//
//        return  response
//    }

    @PreAuthorize("hasAuthority('MS_IO_READ') or hasAuthority('MS_HOD_READ') or hasAuthority('MS_RM_READ') or hasAuthority('MS_HOF_READ') or hasAuthority('MS_DIRECTOR_READ')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun getWorkPlanScheduleInspectionDetailsBasedOnRefNo(referenceNo: String, batchReferenceNo: String): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        val workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
//        val fuelInspectionOfficer = findFuelInspectionOfficerAssigned(workPlanScheduled, map.activeStatus)

        return workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)
    }

    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun startWorkPlanScheduleInspectionOnsiteDetailsBasedOnRefNo(referenceNo: String, batchReferenceNo: String): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)

        with(workPlanScheduled){
            onsiteStartStatus = map.activeStatus
            onsiteEndStatus = map.inactiveStatus
            onsiteStartDate = commonDaoServices.getCurrentDate()
            timelineStartDate = commonDaoServices.getCurrentDate()
            timelineEndDate = applicationMapProperties.mapMSWorkPlanInspectionStartMsOnSite.let { findProcessNameByID( it, 1).timelinesDay?.let {it2-> commonDaoServices.addYDayToDate(commonDaoServices.getCurrentDate(), it2) } }
            msProcessId = applicationMapProperties.mapMSWorkPlanInspectionStartMsOnSite
            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
        }
        workPlanScheduled = updateWorkPlanInspectionDetails(workPlanScheduled, map, loggedInUser).second
        return workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)
    }

    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun endWorkPlanScheduleInspectionOnsiteDetailsBasedOnRefNo(referenceNo: String, batchReferenceNo: String): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)

        with(workPlanScheduled){
            sendSffStatus = map.activeStatus
            onsiteEndStatus = map.activeStatus
            onsiteEndDate = commonDaoServices.getCurrentDate()
            sendSffDate = commonDaoServices.getCurrentDate()
            timelineStartDate = commonDaoServices.getCurrentDate()
            timelineEndDate = applicationMapProperties.mapMSWorkPlanInspectionEndMsOnSite.let { findProcessNameByID( it, 1).timelinesDay?.let {it2-> commonDaoServices.addYDayToDate(commonDaoServices.getCurrentDate(), it2) } }
            msProcessId = applicationMapProperties.mapMSWorkPlanInspectionEndMsOnSite
            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
        }
        workPlanScheduled = updateWorkPlanInspectionDetails(workPlanScheduled, map, loggedInUser).second
        return workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)
    }

    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun createNewWorkPlanSchedule(body: WorkPlanEntityDto, referenceNumber: String, page: PageRequest): WorkPlanScheduleListDetailsDto {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val msType = findMsTypeDetailsWithUuid(applicationMapProperties.mapMsWorkPlanTypeUuid)
        val batchDetail  = findCreatedWorkPlanWIthRefNumber(referenceNumber)
        when (batchDetail.batchClosed) {
            map.activeStatus -> {
                throw ExpectedDataNotFound("The WorkPlan Batch Detail was closed, you can't add a new Work-Plan")
            }
            else -> {
                val fileSaved = saveNewWorkPlanActivity(body,msType, batchDetail, map, loggedInUser)

                when (fileSaved.first.status) {
                    map.successStatus -> {
                        val workPlanList = findALlWorkPlanDetailsAssociatedWithWorkPlanID(batchDetail.id,page).toList()
                        return mapWorkPlanInspectionListDto(workPlanList,mapWorkPlanBatchDetailsDto(batchDetail, map))
                    }
                    else -> {
                        throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved.first))
                    }
                }
            }
        }

    }

    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun updateNewWorkPlanSchedule(body: WorkPlanEntityDto, batchReferenceNo: String,referenceNo: String, page: PageRequest): WorkPlanInspectionDto {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val batchDetails  = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
        val workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        with(workPlanScheduled){
            complaintDepartment = body.complaintDepartment
            divisionId = body.divisionId
            nameActivity = body.nameActivity
            timeActivityDate = body.timeActivityDate
            county = body.county
            townMarketCenter = body.townMarketCenter
            locationActivityOther = body.locationActivityOther
            standardCategory = body.standardCategory
            broadProductCategory = body.broadProductCategory
            productCategory = body.productCategory
            product = body.product
            productSubCategory = body.productSubCategory
            resourcesRequired = body.resourcesRequired
            budget = body.budget
            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
            msProcessId = applicationMapProperties.mapMSWorkPlanInspectionGenerateWorkPlan
            status = map.initStatus
            updatedStatus = map.activeStatus
            resubmitStatus = map.inactiveStatus
            officerId = loggedInUser.id
            officerName = commonDaoServices.concatenateName(loggedInUser)
        }

        val fileSaved = updateWorkPlanInspectionDetails(workPlanScheduled, map, loggedInUser)

        when (fileSaved.first.status) {
            map.successStatus -> {
               return workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)
            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved.first))
            }
        }

    }


    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun createNewWorkPlanScheduleFromComplaint(body: WorkPlanEntityDto, referenceNumber: String, page: PageRequest): WorkPlanScheduleListDetailsDto {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val msType = findMsTypeDetailsWithUuid(applicationMapProperties.mapMsWorkPlanTypeUuid)
        val batchDetail  = findCreatedWorkPlanWIthRefNumber(referenceNumber)
        when (batchDetail.batchClosed) {
            map.activeStatus -> {
                throw ExpectedDataNotFound("The WorkPlan Batch Detail was closed, you can't add a new Work-Plan")
            }
            else -> {
                val fileSaved = saveNewWorkPlanActivity(body,msType, batchDetail, map, loggedInUser)

                when (fileSaved.first.status) {
                    map.successStatus -> {
                        val workPlanList = findALlWorkPlanDetailsAssociatedWithWorkPlanID(batchDetail.id,page).toList()
                        return mapWorkPlanInspectionListDto(workPlanList,mapWorkPlanBatchDetailsDto(batchDetail, map))
                    }
                    else -> {
                        throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved.first))
                    }
                }
            }
        }

    }


    fun updateWorkPlanInspectionDetails(
        body: MsWorkPlanGeneratedEntity,
        map: ServiceMapsEntity,
        user: UsersEntity
    ): Pair<ServiceRequestsEntity, MsWorkPlanGeneratedEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var workPlanDetails = body
        try {
            with(workPlanDetails) {
                modifiedBy = commonDaoServices.concatenateName(user)
                modifiedOn = commonDaoServices.getTimestamp()
            }
            workPlanDetails = generateWorkPlanRepo.save(workPlanDetails)

            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(workPlanDetails)}"
            sr.names = "Work Plan Inspection Update file"

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = map.successStatus
            sr = serviceRequestsRepo.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(body)}"
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepo.save(sr)

        }
        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return Pair(sr, workPlanDetails)
    }

    fun updatePreliminaryReportDetails(
        body: MsPreliminaryReportEntity,
        map: ServiceMapsEntity,
        user: UsersEntity
    ): Pair<ServiceRequestsEntity, MsPreliminaryReportEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var preliminaryReportDetails = body
        try {
            with(preliminaryReportDetails) {
                modifiedBy = commonDaoServices.concatenateName(user)
                modifiedOn = commonDaoServices.getTimestamp()
            }
            preliminaryReportDetails = preliminaryRepo.save(preliminaryReportDetails)

            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(preliminaryReportDetails)}"
            sr.names = "Preliminary Report Update file"

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = map.successStatus
            sr = serviceRequestsRepo.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(body)}"
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepo.save(sr)

        }
        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return Pair(sr, preliminaryReportDetails)
    }



    fun updateWorkPlanBatch(
        body: WorkPlanCreatedEntity,
        map: ServiceMapsEntity,
        user: UsersEntity
    ): Pair<ServiceRequestsEntity, WorkPlanCreatedEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var workBatch = body
        try {
            with(workBatch) {
                modifiedBy = commonDaoServices.concatenateName(user)
                modifiedOn = commonDaoServices.getTimestamp()
            }
            workBatch = workPlanCreatedRepository.save(workBatch)

            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(workBatch)}"
            sr.names = "Work Plan Inspection Batch Update file"

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = map.successStatus
            sr = serviceRequestsRepo.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(body)}"
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepo.save(sr)

        }
        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return Pair(sr, workBatch)
    }


    fun createWorkPlanYear(loggedInUser: UsersEntity,
                           map: ServiceMapsEntity,
                           workPlanYearCodes: WorkplanYearsCodesEntity
    ): Pair<ServiceRequestsEntity, WorkPlanCreatedEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var workPlanCreated = WorkPlanCreatedEntity()
        try {
                with(workPlanCreated) {
                    uuid = commonDaoServices.generateUUIDString()
                    workPlanRegion = commonDaoServices.findUserProfileByUserID(loggedInUser, map.activeStatus).regionId?.id?: throw ExpectedDataNotFound("Logged IN User Is Missing Region ID")
                    referenceNumber = "WORKPLAN#${generateRandomText(5, map.secureRandom, map.messageDigestAlgorithm, true)}".toUpperCase()
                    yearNameId = workPlanYearCodes
                    userCreatedId = loggedInUser
                    batchClosed = map.inactiveStatus
                    createdDate = commonDaoServices.getCurrentDate()
                    createdStatus = map.activeStatus
                    status = map.activeStatus
                    createdBy = commonDaoServices.concatenateName(loggedInUser)
                    createdOn = commonDaoServices.getTimestamp()
                }
            workPlanCreated = workPlanCreatedRepository.save(workPlanCreated)

            sr.payload = commonDaoServices.createJsonBodyFromEntity(workPlanCreated).toString()
            sr.names = "WorkPlan creation of batch File"

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = map.successStatus
            sr = serviceRequestsRepo.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(workPlanYearCodes)}"
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepo.save(sr)

        }
        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return Pair(sr, workPlanCreated)
    }

    fun workPlanInspectionDetailsAddChargeSheet(
        body: ChargeSheetDto,
        workPlanScheduled: MsWorkPlanGeneratedEntity,
        map: ServiceMapsEntity,
        user: UsersEntity
    ): Pair<ServiceRequestsEntity, MsChargeSheetEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var saveChargeSheet = MsChargeSheetEntity()
        try {

            with(saveChargeSheet) {
                christianName = body.christianName
                surname = body.surname
                sex = body.sex
                nationality = body.nationality
                age = body.age
                addressDistrict = body.addressDistrict
                addressLocation = body.addressLocation
                firstCount = body.firstCount
                particularsOffenceOne = body.particularsOffenceOne
                secondCount = body.secondCount
                particularsOffenceSecond = body.particularsOffenceSecond
                dateArrest = body.dateArrest
                withWarrant = body.withWarrant
                applicationMadeSummonsSue = body.applicationMadeSummonsSue
                dateApprehensionCourt = body.dateApprehensionCourt
                bondBailAmount = body.bondBailAmount
                remandedAdjourned = body.remandedAdjourned
                complainantName = body.complainantName
                complainantAddress = body.complainantAddress
                prosecutor = body.prosecutor
                witnesses = body.witnesses
                sentence = body.sentence
                finePaid = body.finePaid
                courtName = body.courtName
                courtDate = body.courtDate
                workPlanGeneratedID = workPlanScheduled.id
                status = map.activeStatus
                createdBy = commonDaoServices.concatenateName(user)
                createdOn = commonDaoServices.getTimestamp()

            }
            saveChargeSheet = chargeSheetRepo.save(saveChargeSheet)

            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(saveChargeSheet)}"
            sr.names = "Save CHarge Sheet Details"

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = map.successStatus
            sr = serviceRequestsRepo.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(body)}"
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepo.save(sr)

        }
        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return Pair(sr, saveChargeSheet)
    }

    fun workPlanInspectionDetailsAddDataReport(
        body: DataReportDto,
        workPlanScheduled: MsWorkPlanGeneratedEntity,
        map: ServiceMapsEntity,
        user: UsersEntity
    ): Pair<ServiceRequestsEntity, MsDataReportEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var saveDataReport = MsDataReportEntity()
        try {

            with(saveDataReport) {
                referenceNumber = body.referenceNumber
                inspectionDate = body.inspectionDate
                inspectorName = body.inspectorName
                function = body.function
                department = body.department
                regionName = body.regionName
                town = body.town
                marketCenter = body.marketCenter
                outletDetails = body.outletDetails
                personMet = body.personMet
                summaryFindingsActionsTaken = body.summaryFindingsActionsTaken
                finalActionSeizedGoods = body.finalActionSeizedGoods
                workPlanGeneratedID = workPlanScheduled.id
                status = map.activeStatus
                createdBy = commonDaoServices.concatenateName(user)
                createdOn = commonDaoServices.getTimestamp()

            }
            saveDataReport = dataReportRepo.save(saveDataReport)

            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(saveDataReport)}"
            sr.names = "Save Data Report Details"

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = map.successStatus
            sr = serviceRequestsRepo.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(body)}"
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepo.save(sr)

        }
        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return Pair(sr, saveDataReport)
    }

    fun workPlanInspectionDetailsAddDataReportParams(
        body: DataReportParamsDto,
        dataReport: MsDataReportEntity,
        map: ServiceMapsEntity,
        user: UsersEntity
    ): Pair<ServiceRequestsEntity, MsDataReportParametersEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var saveDataReport = MsDataReportParametersEntity()
        try {

            with(saveDataReport) {
                typeBrandName= body.typeBrandName
                localImport= body.localImport
                complianceInspectionParameter= body.complianceInspectionParameter
                measurementsResults= body.measurementsResults
                remarks= body.remarks
                dataReportId = dataReport.id
                status = map.activeStatus
                createdBy = commonDaoServices.concatenateName(user)
                createdOn = commonDaoServices.getTimestamp()

            }
            saveDataReport = dataReportParameterRepo.save(saveDataReport)

            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(saveDataReport)}"
            sr.names = "Save Data Report params Details"

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = map.successStatus
            sr = serviceRequestsRepo.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(body)}"
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepo.save(sr)

        }
        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return Pair(sr, saveDataReport)
    }

    fun workPlanInspectionDetailsAddSeizureDeclaration(
        body: SeizureDeclarationDto,
        workPlanScheduled: MsWorkPlanGeneratedEntity,
        map: ServiceMapsEntity,
        user: UsersEntity
    ): Pair<ServiceRequestsEntity, MsSeizureDeclarationEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var saveData = MsSeizureDeclarationEntity()
        try {

            with(saveData) {
                seizureTo = body.seizureTo
                seizurePremises = body.seizurePremises
                seizureRequirementsStandards = body.seizureRequirementsStandards
                goodsName = body.goodsName
                goodsManufactureTrader = body.goodsManufactureTrader
                goodsAddress = body.goodsAddress
                goodsPhysical = body.goodsPhysical
                goodsLocation = body.goodsLocation
                goodsMarkedBranded = body.goodsMarkedBranded
                goodsPhysicalSeal = body.goodsPhysicalSeal
                descriptionGoods = body.descriptionGoods
                goodsQuantity = body.goodsQuantity
                goodsThereforei = body.goodsThereforei
                nameInspector = body.nameInspector
                designationInspector = body.designationInspector
                dateInspector = body.dateInspector
                nameManufactureTrader = body.nameManufactureTrader
                designationManufactureTrader = body.designationManufactureTrader
                dateManufactureTrader = body.dateManufactureTrader
                nameWitness = body.nameWitness
                designationWitness = body.designationWitness
                dateWitness = body.dateWitness
                declarationTakenBy = body.declarationTakenBy
                declarationOnThe = body.declarationOnThe
                declarationDayOf = body.declarationDayOf
                declarationMyName = body.declarationMyName
                declarationIresideAt = body.declarationIresideAt
                declarationIemployeedAs = body.declarationIemployeedAs
                declarationIemployeedOf = body.declarationIemployeedOf
                declarationSituatedAt = body.declarationSituatedAt
                declarationStateThat = body.declarationStateThat
                declarationIdNumber = body.declarationIdNumber
                workPlanGeneratedID = workPlanScheduled.id
                status = map.activeStatus
                createdBy = commonDaoServices.concatenateName(user)
                createdOn = commonDaoServices.getTimestamp()

            }
            saveData = seizureDeclarationRepo.save(saveData)

            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(saveData)}"
            sr.names = "Save Seizure DeclarationDetails"

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = map.successStatus
            sr = serviceRequestsRepo.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(body)}"
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepo.save(sr)

        }
        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return Pair(sr, saveData)
    }


    fun workPlanInspectionDetailsAddInspectionInvestigationReport(
        body: InspectionInvestigationReportDto,
        workPlanScheduled: MsWorkPlanGeneratedEntity,
        map: ServiceMapsEntity,
        user: UsersEntity
    ): Pair<ServiceRequestsEntity, MsInspectionInvestigationReportEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var saveData = MsInspectionInvestigationReportEntity()
        try {

            with(saveData) {
                reportReference =body.reportReference
                reportTo =body.reportTo
                reportThrough =body.reportThrough
                reportFrom =body.reportFrom
                reportSubject =body.reportSubject
                reportTitle =body.reportTitle
                reportDate =body.reportDate
                reportRegion =body.reportRegion
                reportDepartment =body.reportDepartment
                reportFunction =body.reportFunction
                backgroundInformation =body.backgroundInformation
                objectiveInvestigation =body.objectiveInvestigation
                dateInvestigationInspection =body.dateInvestigationInspection
                kebsInspectors = body.kebsInspectors?.let { commonDaoServices.convertClassToJson(it) }
                methodologyEmployed =body.methodologyEmployed
                findings =body.findings
                conclusion =body.conclusion
                recommendations =body.recommendations
                statusActivity =body.statusActivity
                finalRemarkHod =body.finalRemarkHod
                workPlanGeneratedID = workPlanScheduled.id
                status = map.activeStatus
                createdBy = commonDaoServices.concatenateName(user)
                createdOn = commonDaoServices.getTimestamp()

            }
            saveData = investInspectReportRepo.save(saveData)

            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(saveData)}"
            sr.names = "Save Invest Inspect Report Details"

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = map.successStatus
            sr = serviceRequestsRepo.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(body)}"
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepo.save(sr)

        }
        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return Pair(sr, saveData)
    }


     fun workPlanInspectionDetailsAddPreliminaryReport(
        body: PreliminaryReportDto,
        workPlanScheduled: MsWorkPlanGeneratedEntity,
        map: ServiceMapsEntity,
        user: UsersEntity
    ): Pair<ServiceRequestsEntity, MsPreliminaryReportEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var saveData = MsPreliminaryReportEntity()
        try {

            preliminaryRepo.findByIdOrNull(body.id ?: -1L)
                ?.let { prelimFound->
                    with(prelimFound){
                        reportTo = body.reportTo
                        reportFrom = body.reportFrom
                        reportSubject = body.reportSubject
                        reportTitle = body.reportTitle
                        reportDate = body.reportDate
                        surveillanceDateFrom = body.surveillanceDateFrom
                        surveillanceDateTo = body.surveillanceDateTo
                        reportBackground = body.reportBackground
                        kebsOfficersName = body.kebsOfficersName?.let { commonDaoServices.convertClassToJson(it) }
                        surveillanceObjective = body.surveillanceObjective
                        surveillanceConclusions = body.surveillanceConclusions
                        surveillanceRecommendation = body.surveillanceRecommendation
                        remarks = body.remarks
                        workPlanGeneratedID = workPlanScheduled.id
                        status = map.activeStatus
                        modifiedBy = commonDaoServices.concatenateName(user)
                        modifiedOn = commonDaoServices.getTimestamp()
                    }
                    saveData = preliminaryRepo.save(prelimFound)
            }
                ?: kotlin.run {
                    with(saveData) {
                        reportTo = body.reportTo
                        reportFrom = body.reportFrom
                        reportSubject = body.reportSubject
                        reportTitle = body.reportTitle
                        reportDate = body.reportDate
                        surveillanceDateFrom = body.surveillanceDateFrom
                        surveillanceDateTo = body.surveillanceDateTo
                        reportBackground = body.reportBackground
                        kebsOfficersName = body.kebsOfficersName?.let { commonDaoServices.convertClassToJson(it) }
                        surveillanceObjective = body.surveillanceObjective
                        surveillanceConclusions = body.surveillanceConclusions
                        surveillanceRecommendation = body.surveillanceRecommendation
                        remarks = body.remarks
                        workPlanGeneratedID = workPlanScheduled.id
                        status = map.activeStatus
                        createdBy = commonDaoServices.concatenateName(user)
                        createdOn = commonDaoServices.getTimestamp()
                    }
                    saveData = preliminaryRepo.save(saveData)
                }

            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(saveData)}"
            sr.names = "Save Preliminary Report Details"

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = map.successStatus
            sr = serviceRequestsRepo.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(body)}"
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepo.save(sr)

        }
        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return Pair(sr, saveData)
    }

    fun addPreliminaryReportParamAdd(
        body: PreliminaryReportItemsDto,
        preliminaryReport: MsPreliminaryReportEntity,
        map: ServiceMapsEntity,
        user: UsersEntity
    ): Pair<ServiceRequestsEntity, MsPreliminaryReportOutletsVisitedEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var param = MsPreliminaryReportOutletsVisitedEntity()
        try {
            preliminaryOutletRepo.findByIdOrNull(body.id ?: -1L)
                ?.let { foundParam ->
                    with(foundParam) {
                        marketCenter = body.marketCenter
                        nameOutlet = body.nameOutlet
                        sector = body.sector
                        dateVisit = body.dateVisit
                        numberProductsPhysicalInspected = body.numberProductsPhysicalInspected
                        compliancePhysicalInspection = body.compliancePhysicalInspection
                        remarks = body.remarks
                        preliminaryReportID = preliminaryReport.id
                        status = map.activeStatus
                        modifiedBy = commonDaoServices.concatenateName(user)
                        modifiedOn = commonDaoServices.getTimestamp()
                    }
                    param = preliminaryOutletRepo.save(foundParam)
                }
                ?: kotlin.run {
                    with(param) {
                        marketCenter = body.marketCenter
                        nameOutlet = body.nameOutlet
                        sector = body.sector
                        dateVisit = body.dateVisit
                        numberProductsPhysicalInspected = body.numberProductsPhysicalInspected
                        compliancePhysicalInspection = body.compliancePhysicalInspection
                        remarks = body.remarks
                        preliminaryReportID = preliminaryReport.id
                        status = map.activeStatus
                        createdBy = commonDaoServices.concatenateName(user)
                        createdOn = commonDaoServices.getTimestamp()
                    }
                    param = preliminaryOutletRepo.save(param)
                }

            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(param)} "
            sr.names = "Preliminary Report Parameter To be tested Save file"

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = map.successStatus
            sr = serviceRequestsRepo.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(body)}"
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepo.save(sr)

        }
        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return Pair(sr, param)
    }

    fun saveOnsiteUploadFiles(
        docFile: MultipartFile,
        map: ServiceMapsEntity,
        user: UsersEntity,
        docTypeName: String,
        workPlanDetails: MsWorkPlanGeneratedEntity
    ): Pair<ServiceRequestsEntity, MsUploadsEntity> {
        var upload = MsUploadsEntity()
            var sr = commonDaoServices.createServiceRequest(map)
            try {
                with(upload) {
                    msWorkplanGeneratedId = workPlanDetails.id
                    workPlanUploads = 1
                    ordinaryStatus = 0
                    versionNumber = 1
                    name = docFile.originalFilename
                    fileType = docFile.contentType
                    documentType = docTypeName
                    document = docFile.bytes
                    transactionDate = commonDaoServices.getCurrentDate()
                    status = 1
                    createdBy = commonDaoServices.concatenateName(user)
                    createdOn = commonDaoServices.getTimestamp()
                }
                upload = msUploadRepo.save(upload)
//                sr.payload = "${commonDaoServices.createJsonBodyFromEntity(workPlanSchedule)}"
                sr.names = "Upload saved $docTypeName"

                sr.responseStatus = sr.serviceMapsId?.successStatusCode
                sr.responseMessage = "Success ${sr.payload}"
                sr.status = map.successStatus
                sr = serviceRequestsRepo.save(sr)
                sr.processingEndDate = Timestamp.from(Instant.now())
            } catch (e: Exception) {
                KotlinLogging.logger { }.error(e.message, e)
                //            KotlinLogging.logger { }.trace(e.message, e)
                sr.payload = "${docFile.bytes}"
                sr.status = sr.serviceMapsId?.exceptionStatus
                sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
                sr.responseMessage = e.message
                sr = serviceRequestsRepo.save(sr)

            }
            KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
            return Pair(sr, upload)

    }



    fun saveNewWorkPlanActivity(
        body: WorkPlanEntityDto,
        msType: MsTypesEntity,
        userWorkPlan: WorkPlanCreatedEntity,
        map: ServiceMapsEntity,
        usersEntity: UsersEntity
    ): Pair<ServiceRequestsEntity, MsWorkPlanGeneratedEntity> {
        var workPlanSchedule = MsWorkPlanGeneratedEntity()
        var sr = commonDaoServices.createServiceRequest(map)
        try {
        with(workPlanSchedule) {
            complaintDepartment = body.complaintDepartment
            divisionId = body.divisionId
            nameActivity = body.nameActivity
            timeActivityDate = body.timeActivityDate
            county = body.county
            townMarketCenter = body.townMarketCenter
            locationActivityOther = body.locationActivityOther
            standardCategory = body.standardCategory
            broadProductCategory = body.broadProductCategory
            productCategory = body.productCategory
            product = body.product
            productSubCategory = body.productSubCategory
            resourcesRequired = body.resourcesRequired
            budget = body.budget
            msProcessEndedStatus = map.inactiveStatus
            updatedStatus = map.inactiveStatus
            resubmitStatus = map.inactiveStatus
            uuid = commonDaoServices.generateUUIDString()
            msTypeId = msType.id
            progressStep = "WorkPlan Generated"
            region = county?.let { commonDaoServices.findCountiesEntityByCountyId(it, map.activeStatus).regionId }
            referenceNumber = "${msType.markRef}${generateRandomText(map.transactionRefLength, map.secureRandom, map.messageDigestAlgorithm, true)}".toUpperCase()
            workPlanYearId = userWorkPlan.id
            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
            msProcessId = applicationMapProperties.mapMSWorkPlanInspectionGenerateWorkPlan
            status = map.initStatus
            officerId = usersEntity.id
            officerName = commonDaoServices.concatenateName(usersEntity)
            createdBy = commonDaoServices.concatenateName(usersEntity)
            createdOn = commonDaoServices.getTimestamp()
        }

            workPlanSchedule =generateWorkPlanRepo.save(workPlanSchedule)

            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(workPlanSchedule)}"
            sr.names = "WorkPlan Inspection file Created"

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = map.successStatus
            sr = serviceRequestsRepo.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(body)}"
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepo.save(sr)

        }
        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return Pair(sr, workPlanSchedule)

    }

    fun saveNewWorkPlanActivityFromComplaint(
        body: WorkPlanEntityDto,
        complaint: ComplaintEntity,
        msType: MsTypesEntity,
        userWorkPlan: WorkPlanCreatedEntity,
        map: ServiceMapsEntity,
        usersEntity: UsersEntity
    ): Pair<ServiceRequestsEntity, MsWorkPlanGeneratedEntity> {
        var workPlanSchedule = MsWorkPlanGeneratedEntity()
        var sr = commonDaoServices.createServiceRequest(map)
        val (comp, complaintCustomersDetails, complaintLocationDetails) = msComplaintDaoServices.complaintDetails(complaint)
        try {
        with(workPlanSchedule) {
            complaintId = comp.id
            complaintDepartment = comp.complaintDepartment
            divisionId = comp.division
            nameActivity = body.nameActivity
            timeActivityDate = body.timeActivityDate
            county = complaintLocationDetails.county
            townMarketCenter = complaintLocationDetails.town
            locationActivityOther = complaintLocationDetails.marketCenter
            standardCategory = comp.standardCategory
            broadProductCategory = comp.broadProductCategory
            productCategory = comp.productCategory
            product = comp.product
            productSubCategory = comp.productSubCategory
            resourcesRequired = body.resourcesRequired
            budget = body.budget
            uuid = commonDaoServices.generateUUIDString()
            msTypeId = msType.id
            submittedForApprovalStatus= map.activeStatus
            progressStep = "WorkPlan Generated"
            region = county?.let { commonDaoServices.findCountiesEntityByCountyId(it, map.activeStatus).regionId }
            referenceNumber = "${msType.markRef}${generateRandomText(map.transactionRefLength, map.secureRandom, map.messageDigestAlgorithm, true)}".toUpperCase()
            workPlanYearId = userWorkPlan.id
            hodRmAssigned = comp.hodAssigned
            timelineStartDate = commonDaoServices.getCurrentDate()
            timelineEndDate = applicationMapProperties.mapMSWorkPlanInspectionApproveWorkPlan.let { findProcessNameByID( it, 1).timelinesDay?.let {it2-> commonDaoServices.addYDayToDate(commonDaoServices.getCurrentDate(), it2) } }
            msProcessId = applicationMapProperties.mapMSWorkPlanInspectionApproveWorkPlan
            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
            approved = "APPROVED"
            progressStep = approved
            approvedBy = commonDaoServices.concatenateName(usersEntity)
            approvedStatus = map.activeStatus
            rejectedStatus = map.inactiveStatus
            updatedStatus = map.inactiveStatus
            resubmitStatus = map.inactiveStatus
            approvedOn = commonDaoServices.getCurrentDate()
            msProcessEndedStatus = map.inactiveStatus
            status = map.initStatus
            officerId = usersEntity.id
            officerName = commonDaoServices.concatenateName(usersEntity)
            createdBy = commonDaoServices.concatenateName(usersEntity)
            createdOn = commonDaoServices.getTimestamp()
        }

            workPlanSchedule =generateWorkPlanRepo.save(workPlanSchedule)

            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(workPlanSchedule)}"
            sr.names = "WorkPlan Inspection file Created"

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = map.successStatus
            sr = serviceRequestsRepo.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(body)}"
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepo.save(sr)

        }
        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return Pair(sr, workPlanSchedule)

    }

    fun findMsTypeDetailsWithUuid(uuid: String): MsTypesEntity {
        msTypesRepo.findByUuid(uuid)
            ?.let { msTypeDetails ->
                return msTypeDetails
            }
            ?: throw Exception("MS Type Details with the following uuid = ${uuid}, does not Exist")
    }

    fun workPlanAddRemarksDetails(
        workPlanInspectionId: Long,
        body: RemarksToAddDto,
        map: ServiceMapsEntity,
        user: UsersEntity,
        flBatchId: Long?=null,
    ): Pair<ServiceRequestsEntity, MsRemarksEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var remarks = MsRemarksEntity()
        try {
            with(remarks) {
                remarksDescription = body.remarksDescription
                remarksStatus = body.remarksStatus
                userId = body.userId
                workPlanId= workPlanInspectionId
                msProcessId = body.processID
                status = map.activeStatus
                createdBy = commonDaoServices.concatenateName(user)
                createdOn = commonDaoServices.getTimestamp()
            }
            remarks = remarksRepo.save(remarks)

            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(remarks)} "
            sr.names = "Fuel Inspection Remarks Save file"

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = map.successStatus
            sr = serviceRequestsRepo.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(body)}"
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepo.save(sr)

        }
        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return Pair(sr, remarks)
    }



    private fun mapWorkPlanBatchListDto(
        workPlanList: Page<WorkPlanCreatedEntity>?
    ): List<WorkPlanBatchDetailsDto> {
        val workPlanBatchListDto = mutableListOf<WorkPlanBatchDetailsDto>()
        when {
            workPlanList!=null -> {
                return workPlanList.toList().map {
                    WorkPlanBatchDetailsDto(
                        it.id,
                        it.workPlanRegion,
                        it.createdDate,
                        it.createdStatus == 1,
                        it.endedDate,
                        it.endedStatus == 1,
                        it.workPlanStatus == 1,
                        it.referenceNumber,
                        it.userCreatedId?.let { it1 -> commonDaoServices.concatenateName(it1) },
                        it.yearNameId?.yearName,
                        it.batchClosed == 1,
                    )
                }
            }
            else -> {
                return workPlanBatchListDto
            }
        }
    }

    fun mapWorkPlanInspectionListDto(workPlanList: List<MsWorkPlanGeneratedEntity>?, createdWorkPlan: WorkPlanBatchDetailsDto): WorkPlanScheduleListDetailsDto {
        val workPlanInspectionScheduledList = mutableListOf<WorkPlanInspectionDto>()
        workPlanList?.map {workPlanInspectionScheduledList.add(
            WorkPlanInspectionDto(
                id =it.id,
                nameActivity =it.nameActivity,
                budget =it.budget,
                progressStep =it.msProcessId?.let { it1 -> findProcessNameByID(it1, 1).processName },
                timeActivityDate =it.timeActivityDate,
                referenceNumber =it.referenceNumber
            )
        )}

        return WorkPlanScheduleListDetailsDto(
            workPlanInspectionScheduledList,
            createdWorkPlan
        )
    }

    fun findProcessNameByID(processID: Long, status: Int): MsProcessNamesEntity {
        processNameRepo.findByWorkPlanStatusAndId(status,processID)
            ?.let {
                return it
            }
            ?: throw ExpectedDataNotFound("WorkPlan Process Details found with ID : $processID")
    }

    fun mapWorkPlanBatchDetailsDto(
        workPlanBatch: WorkPlanCreatedEntity,
        map: ServiceMapsEntity
    ): WorkPlanBatchDetailsDto{
        return WorkPlanBatchDetailsDto(
            workPlanBatch.id,
            workPlanBatch.workPlanRegion,
            workPlanBatch.createdDate,
            workPlanBatch.createdStatus == 1,
            workPlanBatch.endedDate,
            workPlanBatch.endedStatus == 1,
            workPlanBatch.workPlanStatus == 1,
            workPlanBatch.referenceNumber,
            workPlanBatch.userCreatedId?.let { it1 -> commonDaoServices.concatenateName(it1) },
            workPlanBatch.yearNameId?.yearName,
            workPlanBatch.batchClosed == 1,
        )
    }

    fun mapChargeSheetDetailsDto(
        chargeSheet: MsChargeSheetEntity
    ): ChargeSheetDto {
        return ChargeSheetDto(
                    chargeSheet.id,
                    chargeSheet.christianName,
                    chargeSheet.surname,
                    chargeSheet.sex,
                    chargeSheet.nationality,
                    chargeSheet.age,
                    chargeSheet.addressDistrict,
                    chargeSheet.addressLocation,
                    chargeSheet.firstCount,
                    chargeSheet.particularsOffenceOne,
                    chargeSheet.secondCount,
                    chargeSheet.particularsOffenceSecond,
                    chargeSheet.dateArrest,
                    chargeSheet.withWarrant,
                    chargeSheet.applicationMadeSummonsSue,
                    chargeSheet.dateApprehensionCourt,
                    chargeSheet.bondBailAmount,
                    chargeSheet.remandedAdjourned,
                    chargeSheet.complainantName,
                    chargeSheet.complainantAddress,
                    chargeSheet.prosecutor,
                    chargeSheet.witnesses,
                    chargeSheet.sentence,
                    chargeSheet.finePaid,
                    chargeSheet.courtName,
                    chargeSheet.courtDate,
        )
    }

    fun mapSeizureDeclarationDetailsDto(
        seizureDeclaration: MsSeizureDeclarationEntity
    ): SeizureDeclarationDto {
        return SeizureDeclarationDto(
                    seizureDeclaration.id,
                    seizureDeclaration.seizureTo,
                    seizureDeclaration.seizurePremises,
                    seizureDeclaration.seizureRequirementsStandards,
                    seizureDeclaration.goodsName,
                    seizureDeclaration.goodsManufactureTrader,
                    seizureDeclaration.goodsAddress,
                    seizureDeclaration.goodsPhysical,
                    seizureDeclaration.goodsLocation,
                    seizureDeclaration.goodsMarkedBranded,
                    seizureDeclaration.goodsPhysicalSeal,
                    seizureDeclaration.descriptionGoods,
                    seizureDeclaration.goodsQuantity,
                    seizureDeclaration.goodsThereforei,
                    seizureDeclaration.nameInspector,
                    seizureDeclaration.designationInspector,
                    seizureDeclaration.dateInspector,
                    seizureDeclaration.nameManufactureTrader,
                    seizureDeclaration.designationManufactureTrader,
                    seizureDeclaration.dateManufactureTrader,
                    seizureDeclaration.nameWitness,
                    seizureDeclaration.designationWitness,
                    seizureDeclaration.dateWitness,
                    seizureDeclaration.declarationTakenBy,
                    seizureDeclaration.declarationOnThe,
                    seizureDeclaration.declarationDayOf,
                    seizureDeclaration.declarationMyName,
                    seizureDeclaration.declarationIresideAt,
                    seizureDeclaration.declarationIemployeedAs,
                    seizureDeclaration.declarationIemployeedOf,
                    seizureDeclaration.declarationSituatedAt,
                    seizureDeclaration.declarationStateThat,
                    seizureDeclaration.declarationIdNumber,
        )
    }

    fun mapDataReportDetailsDto(
        dataReport: MsDataReportEntity,
        dataReportParam: List<DataReportParamsDto>
    ): DataReportDto {
        return DataReportDto(
                    dataReport.id,
                    dataReport.referenceNumber,
                    dataReport.inspectionDate,
                    dataReport.inspectorName,
                    dataReport.function,
                    dataReport.department,
                    dataReport.regionName,
                    dataReport.town,
                    dataReport.marketCenter,
                    dataReport.outletDetails,
                    dataReport.personMet,
                    dataReport.summaryFindingsActionsTaken,
                    dataReport.finalActionSeizedGoods,
            null,
                    dataReportParam,
        )
    }

    fun mapInspectionInvestigationDetailsDto(
        inspectionInvestigation: MsInspectionInvestigationReportEntity
    ): InspectionInvestigationReportDto {
        return InspectionInvestigationReportDto(
                    inspectionInvestigation.id,
                    inspectionInvestigation.reportReference,
                    inspectionInvestigation.reportTo,
                    inspectionInvestigation.reportThrough,
                    inspectionInvestigation.reportFrom,
                    inspectionInvestigation.reportSubject,
                    inspectionInvestigation.reportTitle,
                    inspectionInvestigation.reportDate,
                    inspectionInvestigation.reportRegion,
                    inspectionInvestigation.reportDepartment,
                    inspectionInvestigation.reportFunction,
                    inspectionInvestigation.backgroundInformation,
                    inspectionInvestigation.objectiveInvestigation,
                    inspectionInvestigation.dateInvestigationInspection,
                    inspectionInvestigation.kebsInspectors?.let { mapKEBSOfficersNameListDto(it) },
                    inspectionInvestigation.methodologyEmployed,
                    inspectionInvestigation.findings,
                    inspectionInvestigation.conclusion,
                    inspectionInvestigation.recommendations,
                    inspectionInvestigation.statusActivity,
                    inspectionInvestigation.finalRemarkHod,
                    null,
        )
    }

    fun mapDataReportParamListDto(data: List<MsDataReportParametersEntity>): List<DataReportParamsDto> {
        return data.map {
            DataReportParamsDto(
                it.id,
                it.typeBrandName,
                it.localImport,
                it.complianceInspectionParameter,
                it.measurementsResults,
                it.remarks,
            )
        }
    }

    fun findALlWorkPlanDetailsAssociatedWithWorkPlanID(createdWorkPlanID: Long,pageable: Pageable): Page<MsWorkPlanGeneratedEntity> {
        generateWorkPlanRepo.findByWorkPlanYearId(createdWorkPlanID,pageable)
            ?.let {
                return it
            }
            ?: throw ExpectedDataNotFound("No workPlan Details Associated with the following workPlan [ID = ${createdWorkPlanID}]")
    }

    fun findALlWorkPlanDetailsAssociatedWithWorkPlanIDWithComplaintIN(createdWorkPlanID: Long,pageable: Pageable): Page<MsWorkPlanGeneratedEntity> {
        generateWorkPlanRepo.findByWorkPlanYearIdAndComplaintIdIsNotNull(createdWorkPlanID,pageable)
            ?.let {
                return it
            }
            ?: throw ExpectedDataNotFound("No Complaint Plan Details Associated with the following workPlan [ID = ${createdWorkPlanID}]")
    }

    fun findCreatedWorkPlanWIthRefNumber(referenceNumber: String): WorkPlanCreatedEntity {
        workPlanCreatedRepository.findByReferenceNumber(referenceNumber)
            ?.let { createdWorkPlan ->
                return createdWorkPlan
            }
            ?: throw ExpectedDataNotFound("Created Work Plan with the following Reference Number = ${referenceNumber}, does not Exist")
    }

    fun findWorkPlanScheduleByComplaintID(complaintID: Long): MsWorkPlanGeneratedEntity? {
        return generateWorkPlanRepo.findByComplaintId(complaintID)
    }

    fun findWorkPlanBatchByWorkPlanScheduleID(workPlanScheduleID: Long): WorkPlanCreatedEntity? {
        return workPlanCreatedRepository.findByIdOrNull(workPlanScheduleID)
    }

    fun findCreatedWorkPlanWIthRefNumberAndRegion(referenceNumber: String, regionID: Long): WorkPlanCreatedEntity {
        workPlanCreatedRepository.findByReferenceNumberAndWorkPlanRegion(referenceNumber, regionID)
            ?.let { createdWorkPlan ->
                return createdWorkPlan
            }
            ?: throw ExpectedDataNotFound("Created Work Plan with the following Reference Number = ${referenceNumber} and Region ID = ${regionID}, does not Exist")
    }


    fun findWorkPlanActivityByReferenceNumber(referenceNumber: String): MsWorkPlanGeneratedEntity {
        generateWorkPlanRepo.findByReferenceNumber(referenceNumber)
            ?.let { activityWorkPlan ->
                return activityWorkPlan
            }
            ?: throw ExpectedDataNotFound("WorkPlan Activity with [referenceNumber = ${referenceNumber}], does not Exist")
    }

    fun findWorkPlanActivityByID(workPlanID: Long): MsWorkPlanGeneratedEntity {
        generateWorkPlanRepo.findByIdOrNull(workPlanID)
            ?.let { activityWorkPlan ->
                return activityWorkPlan
            }
            ?: throw ExpectedDataNotFound("WorkPlan Activity with [ID = ${workPlanID}], does not Exist")
    }

    fun findWorkPlanYearsCodesEntity(currentYear: String, map: ServiceMapsEntity): WorkplanYearsCodesEntity {
        workPlanYearsCodesRepository.findByYearNameAndStatus(currentYear, map.activeStatus)
            ?.let {
                return it
            }
            ?: throw ExpectedDataNotFound("Workplan Years Codes with [status=$map.activeStatus], do Not Exists")
    }

    fun findWorkPlanCreatedEntity(loggedInUser: UsersEntity, workPlanYearCodes: WorkplanYearsCodesEntity): WorkPlanCreatedEntity? {
        return workPlanCreatedRepository.findByUserCreatedIdAndYearNameId(loggedInUser, workPlanYearCodes)
    }

    fun isWithinRange(checkDate: Date, workPlanYearCodes: WorkplanYearsCodesEntity): Boolean {
        return !(checkDate.before(workPlanYearCodes.workplanCreationStartDate) || checkDate.after(workPlanYearCodes.workplanCreationEndDate))
    }

    fun getCurrentYear(): String {
        val year = Calendar.getInstance()[Calendar.YEAR]
        return year.toString()
    }

    fun workPlanInspectionMappingCommonDetails(
        workPlanScheduledDetails: MsWorkPlanGeneratedEntity,
        map: ServiceMapsEntity,
        batchDetails: WorkPlanCreatedEntity
    ): WorkPlanInspectionDto {
        val batchDetailsDto = mapWorkPlanBatchDetailsDto(batchDetails, map)
        val workPlanInspectionRemarks = findRemarksForWorkPlan(workPlanScheduledDetails.id)
        val workPlanFiles = findUploadedFileForWorkPlans(workPlanScheduledDetails.id)
        val officerList = commonDaoServices.findOfficersListBasedOnRole(
            applicationMapProperties.mapMSComplaintWorkPlanMappedOfficerROLEID,
            workPlanScheduledDetails.county ?: throw ExpectedDataNotFound("MISSING WORK-PLAN COUNTY ID"),
            workPlanScheduledDetails.region ?: throw ExpectedDataNotFound("MISSING WORK-PLAN REGION ID")
        )

        val hofList = commonDaoServices.findOfficersListBasedOnRole(
            applicationMapProperties.mapMSComplaintWorkPlanMappedHOFROLEID,
            workPlanScheduledDetails.county ?: throw ExpectedDataNotFound("MISSING WORK-PLAN COUNTY ID"),
            workPlanScheduledDetails.region ?: throw ExpectedDataNotFound("MISSING WORK-PLAN REGION ID")
        )

        val chargeSheet = findChargeSheetByWorkPlanInspectionID(workPlanScheduledDetails.id)
        val chargeSheetDto = chargeSheet?.let { mapChargeSheetDetailsDto(it) }

        val seizureDeclaration  = findSeizureDeclarationByWorkPlanInspectionID(workPlanScheduledDetails.id)
        val seizureDeclarationDto = seizureDeclaration?.let { mapSeizureDeclarationDetailsDto(it) }

        val dataReport  = findDataReportByWorkPlanInspectionID(workPlanScheduledDetails.id)
        val dataReportParameters  = dataReport?.id?.let { findDataReportParamsByDataReportID(it) }
        val dataReportParametersDto = dataReportParameters?.let { mapDataReportParamListDto(it) }
        val dataReportDto = dataReport?.let { dataReportParametersDto?.let { it1 -> mapDataReportDetailsDto(it, it1) } }

        val inspectionInvestigation  = findInspectionInvestigationByWorkPlanInspectionID(workPlanScheduledDetails.id)
        val inspectionInvestigationDto = inspectionInvestigation?.let { mapInspectionInvestigationDetailsDto(it) }

        val sampleCollected = findSampleCollectedDetailByWorkPlanInspectionID(workPlanScheduledDetails.id)
        val sampleCollectedParamList = sampleCollected?.id?.let { msFuelDaoServices.findAllSampleCollectedParametersBasedOnSampleCollectedID(it) }
        val sampleCollectedDtoValues = sampleCollectedParamList?.let { msFuelDaoServices.mapSampleCollectedParamListDto(it) }?.let { msFuelDaoServices.mapSampleCollectedDto(sampleCollected, it) }


        val sampleSubmitted = findSampleSubmissionDetailByWorkPlanGeneratedID(workPlanScheduledDetails.id)
        val sampleSubmittedParamList = sampleSubmitted?.id?.let { msFuelDaoServices.findAllSampleSubmissionParametersBasedOnSampleSubmissionID(it) }
        val sampleSubmittedDtoValues = sampleSubmittedParamList?.let { msFuelDaoServices.mapSampleSubmissionParamListDto(it) }?.let { msFuelDaoServices.mapSampleSubmissionDto(sampleSubmitted, it) }

        val labResultsParameters = sampleSubmitted?.bsNumber?.let { msFuelDaoServices.findSampleLabTestResultsRepoBYBSNumber(it) }
        val ssfDetailsLab = findSampleSubmittedByWorkPlanGeneratedID(workPlanScheduledDetails.id)
        val savedPDFFilesLims = ssfDetailsLab?.id?.let { msFuelDaoServices.findSampleSubmittedListPdfBYSSFid(it)?.let { ssfDetails->msFuelDaoServices.mapLabPDFFilesListDto(ssfDetails) } }
        val ssfResultsListCompliance = ssfDetailsLab?.let { msFuelDaoServices.mapSSFComplianceStatusDetailsDto(it) }
        val limsPDFFiles = ssfDetailsLab?.bsNumber?.let { msFuelDaoServices.mapLIMSSavedFilesDto(it,savedPDFFilesLims)}
        val labResultsDto = msFuelDaoServices.mapLabResultsDetailsDto(ssfResultsListCompliance,savedPDFFilesLims,limsPDFFiles,labResultsParameters?.let { msFuelDaoServices.mapLabResultsParamListDto(it) })

        val compliantDetailsStatus = mapCompliantStatusDto(workPlanScheduledDetails, map)
        var compliantStatusDone = false
        if (compliantDetailsStatus!=null){
            compliantStatusDone = true
        }

        var timelineOverDue =false
        if (workPlanScheduledDetails.timelineEndDate!= null){
            if (workPlanScheduledDetails.timelineEndDate!! >commonDaoServices.getCurrentDate()){
                timelineOverDue = true
            }
        }

        var updateWorkPlan: WorkPlanEntityDto? = null
        if (workPlanScheduledDetails.approvedStatus == map.inactiveStatus && workPlanScheduledDetails.rejectedStatus== map.activeStatus){
            updateWorkPlan = mapWorkPlanUpdateEntity(workPlanScheduledDetails)
        }


        val preliminaryReport  = findPreliminaryReportByWorkPlanGeneratedID(workPlanScheduledDetails.id)
        val preliminaryReportParamList = preliminaryReport?.id?.let { findPreliminaryReportParams(it) }
        val preliminaryReportDtoValues = preliminaryReportParamList?.let { mapPreliminaryParamListDto(it) }?.let { mapPreliminaryReportDto(preliminaryReport, it) }

        return mapWorkPlanInspectionDto(
            workPlanScheduledDetails,
            officerList,
            hofList,
            map,
            batchDetailsDto,
            compliantStatusDone,
            timelineOverDue,
            workPlanInspectionRemarks,
            workPlanFiles,
            chargeSheetDto,
            seizureDeclarationDto,
            inspectionInvestigationDto,
            dataReportDto,
            sampleCollectedDtoValues,
            sampleSubmittedDtoValues,
            labResultsDto,
            preliminaryReportDtoValues,
            updateWorkPlan

        )
    }

    private fun mapWorkPlanUpdateEntity(wkp: MsWorkPlanGeneratedEntity): WorkPlanEntityDto {
        return WorkPlanEntityDto(
                    wkp.id,
                    wkp.complaintDepartment,
                    wkp.divisionId,
                    wkp.nameActivity,
                    wkp.timeActivityDate,
                    wkp.county,
                    wkp.townMarketCenter,
                    wkp.locationActivityOther,
                    wkp.standardCategory,
                    wkp.broadProductCategory,
                    wkp.productCategory,
                    wkp.product,
                    wkp.productSubCategory,
                    wkp.resourcesRequired,
                    wkp.budget,
                    null,
        )
    }

    fun mapWorkPlanInspectionDto(
        wKP: MsWorkPlanGeneratedEntity,
        officerList: List<UsersEntity>?,
        hofList: List<UsersEntity>?,
        map: ServiceMapsEntity,
        batchDetails: WorkPlanBatchDetailsDto,
        compliantStatusDone: Boolean,
        timelineOverDue: Boolean,
        remarksList: List<MsRemarksEntity>?,
        workPlanFilesSaved: List<MsUploadsEntity>?,
        chargeSheet: ChargeSheetDto?,
        seizureDeclarationDto: SeizureDeclarationDto?,
        inspectionInvestigationDto: InspectionInvestigationReportDto?,
        dataReportDto: DataReportDto?,
        sampleCollected: SampleCollectionDto?,
        sampleSubmitted: SampleSubmissionDto?,
        sampleLabResults: MSSSFLabResultsDto?,
        preliminaryReport: PreliminaryReportDto?,
        updateWorkPlan: WorkPlanEntityDto?,


        ): WorkPlanInspectionDto {
        return WorkPlanInspectionDto(
            wKP.id,
            wKP.productCategory?.let { commonDaoServices.findProductCategoryByID(it).name },
            wKP.broadProductCategory?.let { commonDaoServices.findBroadCategoryByID(it).category },
            wKP.product?.let { commonDaoServices.findProductByID(it).name },
            wKP.standardCategory?.let { commonDaoServices.findStandardCategoryByID(it).standardCategory },
            wKP.productSubCategory?.let { commonDaoServices.findProductSubCategoryByID(it).name },
            wKP.divisionId?.let { commonDaoServices.findDivisionWIthId(it).division },
            wKP.timelineStartDate,
                    wKP.timelineEndDate,
            timelineOverDue,
//            wKP.sampleSubmittedId?.id,
            wKP.division,
            wKP.officerName,
            wKP.nameActivity,
            wKP.targetedProducts,
            wKP.resourcesRequired,
            wKP.budget,
            wKP.approvedOn,
            wKP.approvedStatus == 1,
            wKP.workPlanYearId,
            wKP.clientAppealed == 1,
            wKP.hodRecommendationStatus == 1,
            wKP.hodRecommendationStart == 1,
            wKP.hodRecommendation,
            wKP.destructionNotificationStatus == 1,
            wKP.destructionNotificationDate,
            wKP.hodRecommendationRemarks,
            wKP.preliminaryParamStatus == 1,
            wKP.dataReportGoodsStatus == 1,
            wKP.scfLabparamsStatus == 1,
            wKP.bsNumberStatus == 1,
            wKP.ssfLabparamsStatus == 1,
            wKP.msPreliminaryReportStatus == 1,
            wKP.preliminaryApprovedStatus == 1,
            wKP.msFinalReportStatus == 1,
            wKP.finalApprovedStatus == 1,
            wKP.chargeSheetStatus == 1,
            wKP.investInspectReportStatus == 1,
            wKP.sampleCollectionStatus == 1,
            wKP.sampleSubmittedStatus == 1,
            wKP.seizureDeclarationStatus == 1,
            wKP.dataReportStatus == 1,
            wKP.approvedBy,
            wKP.approved,
            wKP.rejectedOn,
            wKP.rejectedStatus == 1,
            wKP.submittedForApprovalStatus == 1,
            wKP.onsiteStartStatus == 1,
            wKP.onsiteStartDate,
            wKP.onsiteEndDate,
            wKP.sendSffDate,
            wKP.sendSffStatus == 1,
            wKP.onsiteEndStatus == 1,
            wKP.destractionStatus == 1,
            wKP.rejectedBy,
            wKP.rejected,
            wKP.msEndProcessRemarks,
            wKP.rejectedRemarks,
            wKP.approvedRemarks,
            wKP.progressValue==1,
            wKP.msProcessId?.let { it1 -> findProcessNameByID(it1, map.activeStatus).processName },
            wKP.county?.let { commonDaoServices.findCountiesEntityByCountyId(it,map.activeStatus).county },
            wKP.subcounty,
            wKP.townMarketCenter?.let { commonDaoServices.findTownEntityByTownId(it).town },
            wKP.locationActivityOther,
            wKP.timeActivityDate,
            wKP.timeDateReportSubmitted,
            wKP.timeActivityRemarks,
            wKP.rescheduledDateNotVisited,
            wKP.rescheduledDateReportSubmitted,
            wKP.rescheduledActivitiesRemarks,
            wKP.activityUndertakenPeriod,
            wKP.nameHof,
            wKP.reviewSupervisorDate,
            wKP.reviewSupervisorRemarks,
            wKP.destructionClientEmail,
            wKP.region?.let { commonDaoServices.findRegionEntityByRegionID(it, map.activeStatus).region },
            wKP.complaintId,
            wKP.officerId?.let { commonDaoServices.findUserByID(it) }?.let { msFuelDaoServices.mapOfficerDto(it) },
            wKP.hodRmAssigned?.let { commonDaoServices.findUserByID(it) }?.let { msFuelDaoServices.mapOfficerDto(it) },
            wKP.hofAssigned?.let { commonDaoServices.findUserByID(it) }?.let { msFuelDaoServices.mapOfficerDto(it) },
            wKP.destructionDocId,
            wKP.scfDocId,
            wKP.ssfDocId,
            wKP.seizureDocId,
            wKP.declarationDocId,
            wKP.chargeSheetDocId,
            wKP.dataReportDocId,
            wKP.complaintDepartment?.let { commonDaoServices.findDepartmentByID(it).department },
            wKP.referenceNumber,
            batchDetails,
            wKP.productSubCategory?.let { commonDaoServices.findSampleStandardsByID(it) }?.let { msComplaintDaoServices.mapStandardDetailsDto(it) },
            remarksList?.let { mapRemarksListDto(it) },
            workPlanFilesSaved?.let { mapFileListDto(it) },
            chargeSheet,
            seizureDeclarationDto,
            inspectionInvestigationDto,
            dataReportDto,
            sampleCollected,
            sampleSubmitted,
            sampleLabResults,
            compliantStatusDone,
            wKP.destructionRecommended == 1,
            wKP.finalReportGenerated == 1,
            wKP.appealStatus == 1,
            wKP.msProcessEndedStatus == 1,
            preliminaryReport,
            officerList?.let { msComplaintDaoServices.mapOfficerListDto(it) },
            hofList?.let { msComplaintDaoServices.mapOfficerListDto(it) },
            updateWorkPlan,
            wKP.updatedStatus== 1,
            wKP.resubmitStatus== 1,
        )
    }

    fun mapFileListDto(fileFoundList: List<MsUploadsEntity>): List<WorkPlanFilesFoundDto> {
        return fileFoundList.map {
            WorkPlanFilesFoundDto(
                it.id,
                it.documentType,
                it.name,
                it.fileType
            )
        }
    }

    fun mapCompliantStatusDto(compliantDetails: MsWorkPlanGeneratedEntity, map: ServiceMapsEntity): SSFCompliantStatusDto? {

        return when {
            compliantDetails.compliantStatus==map.activeStatus -> {
                SSFCompliantStatusDto(
                    compliantDetails.compliantStatusRemarks,
                    compliantDetails.compliantStatus==1
                )
            }
            compliantDetails.notCompliantStatus==map.inactiveStatus -> {
                SSFCompliantStatusDto(
                    compliantDetails.notCompliantStatusRemarks,
                    compliantDetails.notCompliantStatus==1
                )
            }
            else -> null
        }

    }


    fun mapRemarksListDto(remarksList: List<MsRemarksEntity>): List<MSRemarksDto> {

        return remarksList.map {
            MSRemarksDto(
                it.id,
                it.remarksDescription,
                it.remarksStatus,
                it.msProcessId?.let { it1 -> findProcessNameByID(it1, 1).processBy },
                it.msProcessId?.let { it1 -> findProcessNameByID(it1, 1).processName },
            )
        }
    }

    fun mapKEBSOfficersNameListDto(officersName: String): List<KebsOfficersName>? {
        val gson = Gson()
        val userListType: Type = object : TypeToken<ArrayList<KebsOfficersName?>?>() {}.type
        return gson.fromJson(officersName, userListType)
    }

    fun mapPreliminaryReportDto(data: MsPreliminaryReportEntity, data2:List<PreliminaryReportItemsDto>): PreliminaryReportDto {
        return PreliminaryReportDto(
            data.id,
            data.reportTo,
            data.reportFrom,
            data.reportSubject,
            data.reportTitle,
            data.reportDate,
            data.surveillanceDateFrom,
            data.surveillanceDateTo,
            data.reportBackground,
            data.kebsOfficersName?.let { mapKEBSOfficersNameListDto(it) },
            data.surveillanceObjective,
            data.surveillanceConclusions,
            data.surveillanceRecommendation,
            data.remarks,
            data2,
                    data.approvedStatusHofFinal==1,
                    data.rejectedStatusHofFinal==1,
                    data.approvedStatus==1,
                    data.rejectedStatus==1,
                    data.approvedStatusHodFinal==1,
                    data.rejectedStatusHodFinal==1,
                    data.approvedStatusHod==1,
                    data.rejectedStatusHod==1,
        )
    }

    fun mapPreliminaryParamListDto(paramList: List<MsPreliminaryReportOutletsVisitedEntity>): List<PreliminaryReportItemsDto> {

        return paramList.map {
            PreliminaryReportItemsDto(
                it.id,
                it.marketCenter,
                it.nameOutlet,
                it.sector,
                it.dateVisit,
                it.numberProductsPhysicalInspected,
                it.compliancePhysicalInspection,
                it.remarks,
                it.preliminaryReportID,
            )
        }
    }

    fun findRemarksForWorkPlan(workPlanInspectionID: Long): List<MsRemarksEntity>? {
        return remarksRepo.findAllByWorkPlanIdOrderByIdAsc(workPlanInspectionID)
    }

    fun findUploadedFileForWorkPlans(workPlanInspectionID: Long): List<MsUploadsEntity>? {
        return msUploadRepo.findAllByMsWorkplanGeneratedIdAndWorkPlanUploads(workPlanInspectionID, 1)
    }

    fun findPreliminaryReportParams(preliminaryID: Long): List<MsPreliminaryReportOutletsVisitedEntity> {
        return preliminaryOutletRepo.findByPreliminaryReportID(preliminaryID)
    }

    fun findChargeSheetByWorkPlanInspectionID(workPlanInspectionID: Long): MsChargeSheetEntity? {
        return  chargeSheetRepo.findByWorkPlanGeneratedID(workPlanInspectionID)
    }

    fun findSeizureDeclarationByWorkPlanInspectionID(workPlanInspectionID: Long): MsSeizureDeclarationEntity? {
        return  seizureDeclarationRepo.findByWorkPlanGeneratedID(workPlanInspectionID)
    }

    fun findDataReportByWorkPlanInspectionID(workPlanInspectionID: Long): MsDataReportEntity? {
        return  dataReportRepo.findByWorkPlanGeneratedID(workPlanInspectionID)
    }

    fun findInspectionInvestigationByWorkPlanInspectionID(workPlanInspectionID: Long): MsInspectionInvestigationReportEntity? {
        return  investInspectReportRepo.findByWorkPlanGeneratedID(workPlanInspectionID)
    }

    fun findDataReportParamsByDataReportID(dataReportID: Long): List<MsDataReportParametersEntity>? {
        return  dataReportParameterRepo.findByDataReportId(dataReportID)
    }

    fun findSampleCollectedDetailByWorkPlanInspectionID(workPlanInspectionID: Long): MsSampleCollectionEntity? {
        return  sampleCollectRepo.findByWorkPlanGeneratedID(workPlanInspectionID)
    }

    fun findSampleSubmissionDetailByWorkPlanGeneratedID(workPlanInspectionID: Long): MsSampleSubmissionEntity? {
        return  sampleSubmitRepo.findByWorkPlanGeneratedID(workPlanInspectionID)
    }

    fun findSampleSubmittedByWorkPlanGeneratedID(workPlanInspectionID: Long): QaSampleSubmissionEntity? {
        return sampleSubmissionLabRepo.findByWorkplanGeneratedId(workPlanInspectionID)
    }

    fun findPreliminaryReportByWorkPlanGeneratedID(workPlanInspectionID: Long): MsPreliminaryReportEntity? {
        return preliminaryRepo.findByWorkPlanGeneratedID(workPlanInspectionID)
//            ?.let {
//                return it
//            }
//            ?: throw ExpectedDataNotFound("Missing Preliminary Report For Work Plan with ID ${workPlanInspectionID}, do Not Exists")
    }

}




