package org.kebs.app.kotlin.apollo.api.ports.provided.dao

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.ktor.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.controllers.qaControllers.ReportsController
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.MarketSurveillanceBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.emailDTO.*
import org.kebs.app.kotlin.apollo.api.ports.provided.lims.LimsServices
import org.kebs.app.kotlin.apollo.common.dto.ApiResponseModel
import org.kebs.app.kotlin.apollo.common.dto.PredefinedResourcesRequiredEntityDto
import org.kebs.app.kotlin.apollo.common.dto.ms.*
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.utils.generateRandomText
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.ms.*
import org.kebs.app.kotlin.apollo.store.model.qa.QaSampleSubmissionEntity
import org.kebs.app.kotlin.apollo.store.repo.INotificationsRepository
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
import java.math.BigDecimal
import java.sql.Date
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDate
import java.util.*


@Service
class MarketSurveillanceWorkPlanDaoServices(
    private val applicationMapProperties: ApplicationMapProperties,
    private val workPlanYearsCodesRepository: IWorkplanYearsCodesRepository,
    private val workPlanCreatedRepository: IWorkPlanCreatedRepository,
    private val workPlanCountiesTownsRepo: IMsWorkPlanCountiesTownsRepository,
    private val generateWorkPlanRepo: IWorkPlanGenerateRepository,
    private val chargeSheetRepo: IChargeSheetRepository,
    private val dataReportRepo: IDataReportRepository,
    private val dataReportParameterRepo: IDataReportParameterRepository,
    private val sampleSubmissionRepo: ISSFRepository,
    private val ssfLabParametersRepo: ISSFLabParameterRepository,
    private val seizureDeclarationRepo: IMsSeizureRepository,
    private val seizureRepo: IMSSeizureDeclarationRepository,
    private val investInspectReportRepo: IMSInvestInspectReportRepository,
    private val preliminaryRepo: IPreliminaryReportRepository,
    private val preliminaryOutletRepo: IPreliminaryOutletsRepository,
    private val recommendationRepo: ICfgRecommendationRepository,
    private val msTaskNotificationsRepo: IMsTaskNotificationsRepository,
    private val allocatedTasksWpViewRepo: IMsAllocatedTasksWpViewRepository,
    private val tasksPendingAllocationWpViewRepo: IMsTasksPendingAllocationWpViewRepository,

    private val msTypesRepo: IMsTypesRepository,
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
    private val workPlanProductsRepo: IWorkPlanProductsEntityRepository,
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
    private val notificationsRepo: INotificationsRepository,
    private val msFuelDaoServices: MarketSurveillanceFuelDaoServices,
    private val complaintsRepo: IComplaintRepository,
    private val complaintsCustomerRepo: IComplaintCustomersRepository,
    private val msComplaintDaoServices: MarketSurveillanceComplaintProcessDaoServices
) {
    final var complaintSteps: Int = 6
    private final val activeStatus: Int = 1
    private final val overDueValue = "YES"
    val gson = Gson()

    final var appId = applicationMapProperties.mapMarketSurveillance

    @Lazy
    @Autowired
    lateinit var reportsControllers: ReportsController

    fun listMsRecommendation(status: Int): List<MsRecommendationDto>? {
        val directoratesEntity = recommendationRepo.findAllByStatus(status)
        return directoratesEntity?.sortedBy { it.id }
            ?.map { MsRecommendationDto(it.id, it.recommendationName, it.description, it.status == 1) }
    }

    fun listMsNotificationTasks(status: Int): List<MsNotificationTaskDto>? {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val taskNotification =
            loggedInUser.id?.let { msTaskNotificationsRepo.findAllByReadStatusAndToUserId(status, it) }
        return taskNotification?.sortedBy { it.id }?.map {
            MsNotificationTaskDto(
                it.id,
                gson.fromJson(it.notificationBody, NotificationBodyDto::class.java),
                it.notificationMsg,
                it.notificationName,
                it.notificationType,
                it.fromUserId,
                it.toUserId,
                it.readStatus == 1
            )
        }
    }

    @PreAuthorize("hasAuthority('MS_IO_READ') or hasAuthority('MS_HOD_READ') or hasAuthority('MS_RM_READ') or hasAuthority('MS_HOF_READ') or hasAuthority('MS_DIRECTOR_READ')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun msWorkPlanAllocatedTaskListsView(page: PageRequest, complaint: Boolean): ApiResponseModel {
        val response: ApiResponseModel
        val auth = commonDaoServices.loggedInUserAuthentication()
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        when {
            complaint -> {
                when {
                    auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_IO_READ" } -> {
                        response = mapDashBoardWorkPlanInspectionListViewDto(
                            allocatedTasksWpViewRepo.findAllByOfficerIdAndComplaintIdIsNotNull(
                                loggedInUser.id ?: throw ExpectedDataNotFound("Missing Logged In User ID"),
                                page
                            )
                        )
                    }
                    else -> throw ExpectedDataNotFound("Can't access this page Due to Invalid authority")
                }
            }
            else -> {
                when {
                    auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_IO_READ" } -> {
                        response = mapDashBoardWorkPlanInspectionListViewDto(
                            allocatedTasksWpViewRepo.findAllByOfficerIdAndComplaintIdIsNull(
                                loggedInUser.id ?: throw ExpectedDataNotFound("Missing Logged In User ID"),
                                page
                            )
                        )
                    }
                    else -> throw ExpectedDataNotFound("Can't access this page Due to Invalid authority")
                }
            }
        }


        return response
    }

    @PreAuthorize("hasAuthority('MS_IO_READ') or hasAuthority('MS_HOD_READ') or hasAuthority('MS_RM_READ') or hasAuthority('MS_HOF_READ') or hasAuthority('MS_DIRECTOR_READ')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun msWorkPlanAllocatedTaskListsOverDueView(page: PageRequest, complaint: Boolean): ApiResponseModel {
        val response: ApiResponseModel
        val auth = commonDaoServices.loggedInUserAuthentication()
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        when {
            complaint -> {
                when {
                    auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_IO_READ" } -> {
                        response = mapDashBoardWorkPlanInspectionListViewDto(
                            allocatedTasksWpViewRepo.findAllByOfficerIdAndComplaintIdIsNotNull(
                                loggedInUser.id ?: throw ExpectedDataNotFound("Missing Logged In User ID"),
                                page
                            )
                        )
                    }
                    else -> throw ExpectedDataNotFound("Can't access this page Due to Invalid authority")
                }
            }
            else -> {
                when {
                    auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_IO_READ" } -> {
                        response = mapDashBoardWorkPlanInspectionListViewDto(
                            allocatedTasksWpViewRepo.findAllByOfficerIdAndComplaintIdIsNull(
                                loggedInUser.id ?: throw ExpectedDataNotFound("Missing Logged In User ID"),
                                page
                            )
                        )
                    }
                    else -> throw ExpectedDataNotFound("Can't access this page Due to Invalid authority")
                }
            }
        }


        return response
    }


    @PreAuthorize("hasAuthority('MS_IO_READ') or hasAuthority('MS_HOD_READ') or hasAuthority('MS_RM_READ') or hasAuthority('MS_HOF_READ') or hasAuthority('MS_DIRECTOR_READ')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun msWorkPlanReportsPendingReviewListsView(page: PageRequest, complaint: Boolean): ApiResponseModel {
        val response: ApiResponseModel
        val auth = commonDaoServices.loggedInUserAuthentication()
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        when {
            complaint -> {
                when {
                    auth.authorities.stream().anyMatch { authority ->
                        authority.authority
                        authority.authority == "MS_HOD_READ"
                                || authority.authority == "MS_DIRECTOR_READ"
                                || authority.authority == "MS_HOF_READ"
                                || authority.authority == "MS_RM_READ"
                    } -> {
                        response = mapDashBoardWorkPlanInspectionPendingAllocationListViewDto(
                            tasksPendingAllocationWpViewRepo.findAllByReportPendingReviewAndComplaintIdIsNotNull(
                                map.activeStatus,
                                page
                            )
                        )
                    }
                    else -> throw ExpectedDataNotFound("Can't access this page Due to Invalid authority")
                }
            }
            else -> {
                when {
                    auth.authorities.stream().anyMatch { authority ->
                        authority.authority
                        authority.authority == "MS_HOD_READ"
                                || authority.authority == "MS_DIRECTOR_READ"
                                || authority.authority == "MS_HOF_READ"
                                || authority.authority == "MS_RM_READ"
                    } -> {
                        response = mapDashBoardWorkPlanInspectionPendingAllocationListViewDto(
                            tasksPendingAllocationWpViewRepo.findAllByReportPendingReviewAndComplaintIdIsNull(
                                map.activeStatus,
                                page
                            )
                        )
                    }
                    else -> throw ExpectedDataNotFound("Can't access this page Due to Invalid authority")
                }
            }
        }


        return response
    }

    @PreAuthorize("hasAuthority('MS_IO_READ') or hasAuthority('MS_HOD_READ') or hasAuthority('MS_RM_READ') or hasAuthority('MS_HOF_READ') or hasAuthority('MS_DIRECTOR_READ')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun msWorkPlanJuniorTaskOverDueListsView(page: PageRequest, complaint: Boolean): ApiResponseModel {
        val response: ApiResponseModel
        val auth = commonDaoServices.loggedInUserAuthentication()
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        when {
            complaint -> {
                when {
                    auth.authorities.stream().anyMatch { authority ->
                        authority.authority
                        authority.authority == "MS_HOD_READ"
                                || authority.authority == "MS_DIRECTOR_READ"
                                || authority.authority == "MS_HOF_READ"
                                || authority.authority == "MS_RM_READ"
                    } -> {
                        response = mapDashBoardWorkPlanInspectionPendingAllocationListViewDto(
                            tasksPendingAllocationWpViewRepo.findAllByTaskOverDueAndComplaintIdIsNotNull(
                                overDueValue,
                                page
                            )
                        )
                    }
                    else -> throw ExpectedDataNotFound("Can't access this page Due to Invalid authority")
                }
            }
            else -> {
                when {
                    auth.authorities.stream().anyMatch { authority ->
                        authority.authority
                        authority.authority == "MS_HOD_READ"
                                || authority.authority == "MS_DIRECTOR_READ"
                                || authority.authority == "MS_HOF_READ"
                                || authority.authority == "MS_RM_READ"
                    } -> {
                        response = mapDashBoardWorkPlanInspectionPendingAllocationListViewDto(
                            tasksPendingAllocationWpViewRepo.findAllByTaskOverDueAndComplaintIdIsNull(
                                overDueValue,
                                page
                            )
                        )
                    }
                    else -> throw ExpectedDataNotFound("Can't access this page Due to Invalid authority")
                }
            }
        }


        return response
    }


    @PreAuthorize("hasAuthority('MS_IO_READ') or hasAuthority('MS_HOD_READ') or hasAuthority('MS_RM_READ') or hasAuthority('MS_HOF_READ') or hasAuthority('MS_DIRECTOR_READ')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun updateTaskRead(taskRefNumber: String): List<MsNotificationTaskDto>? {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        val taskNotification = msTaskNotificationsRepo.findByTaskRefNumber(taskRefNumber) ?: throw ExpectedDataNotFound(
            "MISSING NOTIFICATION WITH REF NUMBER $taskRefNumber"
        )
        updateNotificationTask(taskNotification, map, loggedInUser)

        return listMsNotificationTasks(map.inactiveStatus)
    }

    @PreAuthorize("hasAuthority('MS_IO_READ') or hasAuthority('MS_HOD_READ') or hasAuthority('MS_RM_READ') or hasAuthority('MS_HOF_READ') or hasAuthority('MS_DIRECTOR_READ')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun getAllWorkPlanBatchList(page: PageRequest, complaint: Boolean): List<WorkPlanBatchDetailsDto> {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val auth = commonDaoServices.loggedInUserAuthentication()
        val map = commonDaoServices.serviceMapDetails(appId)
        val regionID = commonDaoServices.findUserProfileByUserID(loggedInUser, map.activeStatus).regionId?.id
            ?: throw ExpectedDataNotFound("Logged IN User Is Missing Region ID")

        val myWorkPlanCreated: Page<WorkPlanCreatedEntity>?
        when {
            complaint -> {
                when {
                    auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_IO_READ" } -> {
                        myWorkPlanCreated = workPlanCreatedRepository.findByUserCreatedIdAndComplaintStatus(
                            loggedInUser,
                            page,
                            map.activeStatus
                        )
                    }
                    auth.authorities.stream().anyMatch { authority ->
                        authority.authority == "MS_HOD_READ"
                                || authority.authority == "MS_HOF_READ"
                                || authority.authority == "MS_RM_READ"
                    } -> {
                        myWorkPlanCreated = workPlanCreatedRepository.findByWorkPlanRegionAndComplaintStatus(
                            regionID,
                            page,
                            map.activeStatus
                        )
                    }
                    auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_DIRECTOR_READ" } -> {
                        myWorkPlanCreated = workPlanCreatedRepository.findAllByComplaintStatus(map.activeStatus, page)
                    }

                    else -> throw ExpectedDataNotFound("Can not access this page Due to Invalid authorities")
                }
            }
            else -> {
                when {
                    auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_IO_READ" } -> {
                        myWorkPlanCreated = workPlanCreatedRepository.findByUserCreatedIdAndWorkPlanStatus(
                            loggedInUser,
                            page,
                            map.activeStatus
                        )
                    }
                    auth.authorities.stream().anyMatch { authority ->
                        authority.authority == "MS_HOD_READ"
                                || authority.authority == "MS_HOF_READ"
                                || authority.authority == "MS_RM_READ"
                    } -> {
                        myWorkPlanCreated = workPlanCreatedRepository.findByWorkPlanRegionAndWorkPlanStatus(
                            regionID,
                            page,
                            map.activeStatus
                        )
                    }
                    auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_DIRECTOR_READ" } -> {
                        myWorkPlanCreated = workPlanCreatedRepository.findAllByWorkPlanStatus(map.activeStatus, page)
                    }

                    else -> throw ExpectedDataNotFound("Can not access this page Due to Invalid authorities")
                }
            }
        }
        return mapWorkPlanBatchListDto(myWorkPlanCreated)
    }

    @PreAuthorize("hasAuthority('MS_IO_READ') or hasAuthority('MS_HOD_READ') or hasAuthority('MS_RM_READ') or hasAuthority('MS_HOF_READ') or hasAuthority('MS_DIRECTOR_READ')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun getAllWorkPlanBatchListClosed(page: PageRequest, complaint: Boolean): List<WorkPlanBatchDetailsDto> {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val auth = commonDaoServices.loggedInUserAuthentication()
        val map = commonDaoServices.serviceMapDetails(appId)
        val regionID = commonDaoServices.findUserProfileByUserID(loggedInUser, map.activeStatus).regionId?.id
            ?: throw ExpectedDataNotFound("Logged IN User Is Missing Region ID")

        val myWorkPlanCreated: Page<WorkPlanCreatedEntity>?
        when {
            complaint -> {
                when {
                    auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_IO_READ" } -> {
                        myWorkPlanCreated =
                            workPlanCreatedRepository.findByUserCreatedIdAndBatchClosedAndComplaintStatus(
                                loggedInUser,
                                map.activeStatus,
                                map.activeStatus,
                                page
                            )
                    }
                    auth.authorities.stream().anyMatch { authority ->
                        authority.authority == "MS_HOD_READ"
                                || authority.authority == "MS_HOF_READ"
                                || authority.authority == "MS_RM_READ"
                    } -> {
                        myWorkPlanCreated =
                            workPlanCreatedRepository.findByWorkPlanRegionAndBatchClosedAndComplaintStatus(
                                regionID,
                                map.activeStatus,
                                map.activeStatus,
                                page
                            )
                    }
                    auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_DIRECTOR_READ" } -> {
                        myWorkPlanCreated = workPlanCreatedRepository.findAllByBatchClosedAndComplaintStatus(
                            map.activeStatus,
                            map.activeStatus,
                            page
                        )
                    }

                    else -> throw ExpectedDataNotFound("Can not access this page Due to Invalid authorities")
                }
            }
            else -> {
                when {
                    auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_IO_READ" } -> {
                        myWorkPlanCreated =
                            workPlanCreatedRepository.findByUserCreatedIdAndBatchClosedAndWorkPlanStatus(
                                loggedInUser,
                                map.activeStatus,
                                map.activeStatus,
                                page
                            )
                    }
                    auth.authorities.stream().anyMatch { authority ->
                        authority.authority == "MS_HOD_READ"
                                || authority.authority == "MS_HOF_READ"
                                || authority.authority == "MS_RM_READ"
                    } -> {
                        myWorkPlanCreated =
                            workPlanCreatedRepository.findByWorkPlanRegionAndBatchClosedAndWorkPlanStatus(
                                regionID,
                                map.activeStatus,
                                map.activeStatus,
                                page
                            )
                    }
                    auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_DIRECTOR_READ" } -> {
                        myWorkPlanCreated = workPlanCreatedRepository.findAllByBatchClosedAndWorkPlanStatus(
                            map.activeStatus,
                            map.activeStatus,
                            page
                        )
                    }

                    else -> throw ExpectedDataNotFound("Can not access this page Due to Invalid authorities")
                }
            }
        }

        return mapWorkPlanBatchListDto(myWorkPlanCreated)
    }

    @PreAuthorize("hasAuthority('MS_IO_READ') or hasAuthority('MS_HOD_READ') or hasAuthority('MS_RM_READ') or hasAuthority('MS_HOF_READ') or hasAuthority('MS_DIRECTOR_READ')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun getAllWorkPlanBatchListOpen(page: PageRequest, complaint: Boolean): List<WorkPlanBatchDetailsDto> {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val auth = commonDaoServices.loggedInUserAuthentication()
        val map = commonDaoServices.serviceMapDetails(appId)
        val regionID = commonDaoServices.findUserProfileByUserID(loggedInUser, map.activeStatus).regionId?.id
            ?: throw ExpectedDataNotFound("Logged IN User Is Missing Region ID")

        val myWorkPlanCreated: Page<WorkPlanCreatedEntity>?
        when {
            complaint -> {
                when {
                    auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_IO_READ" } -> {
                        myWorkPlanCreated =
                            workPlanCreatedRepository.findByUserCreatedIdAndBatchClosedAndComplaintStatus(
                                loggedInUser,
                                map.inactiveStatus,
                                map.activeStatus,
                                page
                            )
                    }
                    auth.authorities.stream().anyMatch { authority ->
                        authority.authority == "MS_HOD_READ"
                                || authority.authority == "MS_HOF_READ"
                                || authority.authority == "MS_RM_READ"
                    } -> {
                        myWorkPlanCreated =
                            workPlanCreatedRepository.findByWorkPlanRegionAndBatchClosedAndComplaintStatus(
                                regionID,
                                map.inactiveStatus,
                                map.activeStatus,
                                page
                            )
                    }
                    auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_DIRECTOR_READ" } -> {
                        myWorkPlanCreated = workPlanCreatedRepository.findAllByBatchClosedAndComplaintStatus(
                            map.inactiveStatus,
                            map.activeStatus,
                            page
                        )
                    }

                    else -> throw ExpectedDataNotFound("Can not access this page Due to Invalid authorities")
                }
            }
            else -> {
                when {
                    auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_IO_READ" } -> {
                        myWorkPlanCreated =
                            workPlanCreatedRepository.findByUserCreatedIdAndBatchClosedAndWorkPlanStatus(
                                loggedInUser,
                                map.inactiveStatus,
                                map.activeStatus,
                                page
                            )
                    }
                    auth.authorities.stream().anyMatch { authority ->
                        authority.authority == "MS_HOD_READ"
                                || authority.authority == "MS_HOF_READ"
                                || authority.authority == "MS_RM_READ"
                    } -> {
                        myWorkPlanCreated =
                            workPlanCreatedRepository.findByWorkPlanRegionAndBatchClosedAndWorkPlanStatus(
                                regionID,
                                map.inactiveStatus,
                                map.activeStatus,
                                page
                            )
                    }
                    auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_DIRECTOR_READ" } -> {
                        myWorkPlanCreated = workPlanCreatedRepository.findAllByBatchClosedAndWorkPlanStatus(
                            map.inactiveStatus,
                            map.activeStatus,
                            page
                        )
                    }

                    else -> throw ExpectedDataNotFound("Can not access this page Due to Invalid authorities")
                }
            }
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
        val userWorkPlan = findWorkPlanCreatedEntity(loggedInUser, workPlanYearCodes, map.activeStatus)
        val checkCreationDate = isWithinRange(commonDaoServices.getCurrentDate(), workPlanYearCodes)
        when {
            checkCreationDate -> {
                when (userWorkPlan) {
                    null -> {
                        createWorkPlanYear(loggedInUser, map, workPlanYearCodes, false)
                        val workPlanCreated = workPlanCreatedRepository.findByUserCreatedId(loggedInUser, page)
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
    fun closeWorkPlanBatchCreated(referenceNumber: String, page: PageRequest): List<WorkPlanBatchDetailsDto> {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val batchDetail = findCreatedWorkPlanWIthRefNumber(referenceNumber)
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
                val fileInspectionList = findALlWorkPlanDetailsAssociatedWithWorkPlanID(batchDetail.id, page).toList()
                fileInspectionList.forEach { it ->
                    with(it) {
                        timelineStartDate = commonDaoServices.getCurrentDate()
                        timelineEndDate = applicationMapProperties.mapMSWorkPlanInspectionSubmission.let { timeLine ->
                            findProcessNameByID(timeLine, 1).timelinesDay
                        }?.let { daysCount ->
                            commonDaoServices.addDaysSkippingWeekends(LocalDate.now(), daysCount)
                                ?.let { daysConvert -> commonDaoServices.localDateToTimestamp(daysConvert) }
                        }
                        msProcessId = applicationMapProperties.mapMSWorkPlanInspectionSubmission
                        userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameHodRm
                    }
                    updateWorkPlanInspectionDetails(it, map, loggedInUser)
                }

                val hodList = commonDaoServices.findOfficersListBasedOnRole(
                    applicationMapProperties.mapMSComplaintWorkPlanMappedHODROLEID,
                    loggedInUserProfile.regionId?.id ?: throw ExpectedDataNotFound("MISSING BATCH REGION ID")
                )

                val rmList = commonDaoServices.findOfficersListBasedOnRole(
                    applicationMapProperties.mapMSComplaintWorkPlanMappedRMROLEID,
                    loggedInUserProfile.regionId?.id ?: throw ExpectedDataNotFound("MISSING BATCH REGION ID")
                )

                    hodList
                        ?.forEach { mp ->
                            val scheduleEmailDetails = WorkPlanScheduledDTO()
                            with(scheduleEmailDetails) {
                                baseUrl = applicationMapProperties.baseUrlValue
                                fullName = commonDaoServices.concatenateName(mp)
                                refNumber = fileSaved.second.referenceNumber
                                yearCodeName = fileSaved.second.yearNameId?.yearName
                                dateSubmitted = commonDaoServices.getCurrentDate()

                            }
                            commonDaoServices.sendEmailWithUserEntity(
                                mp,
                                applicationMapProperties.mapMsFuelScheduleMPNotification,
                                scheduleEmailDetails,
                                map,
                                fileSaved.first
                            )
                        }
                    rmList
                        ?.forEach { mp ->
                            val scheduleEmailDetails = WorkPlanScheduledDTO()
                            with(scheduleEmailDetails) {
                                baseUrl = applicationMapProperties.baseUrlValue
                                fullName = commonDaoServices.concatenateName(mp)
                                refNumber = fileSaved.second.referenceNumber
                                yearCodeName = fileSaved.second.yearNameId?.yearName
                                dateSubmitted = commonDaoServices.getCurrentDate()

                            }
                            commonDaoServices.sendEmailWithUserEntity(
                                mp,
                                applicationMapProperties.mapMsFuelScheduleMPNotification,
                                scheduleEmailDetails,
                                map,
                                fileSaved.first
                            )
                        }


                val workBatchList = workPlanCreatedRepository.findByUserCreatedId(loggedInUser, page)
                return mapWorkPlanBatchListDto(workBatchList)
            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved.first))
            }
        }
    }

    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun submitWorkPlanScheduleCreated(
        batchReferenceNo: String,
        referenceNo: String,
        page: PageRequest
    ): WorkPlanInspectionDto {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        val batchDetail = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
        val loggedInUserProfile = commonDaoServices.findUserProfileByUserID(loggedInUser, map.activeStatus)

        return workPlanUpdate(
            batchDetail,
            map,
            loggedInUser,
            workPlanScheduled,
            loggedInUserProfile,
            batchReferenceNo
        )
    }

    fun workPlanUpdate(
        batchDetail: WorkPlanCreatedEntity,
        map: ServiceMapsEntity,
        loggedInUser: UsersEntity,
        workPlanScheduled: MsWorkPlanGeneratedEntity,
        loggedInUserProfile: UserProfilesEntity,
        batchReferenceNo: String
    ): WorkPlanInspectionDto {
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
                    if (submittedForApprovalStatus == map.activeStatus) {
                        resubmitStatus = map.activeStatus
                    }
                    submittedForApprovalStatus = map.activeStatus
                    reportPendingReview = map.activeStatus
                    timelineStartDate = commonDaoServices.getCurrentDate()
                    timelineEndDate =
                        applicationMapProperties.mapMSWorkPlanInspectionSubmittedForApproval.let { timeLine ->
                            findProcessNameByID(timeLine, 1).timelinesDay
                        }?.let { daysCount ->
                            commonDaoServices.addDaysSkippingWeekends(LocalDate.now(), daysCount)
                                ?.let { daysConvert -> commonDaoServices.localDateToTimestamp(daysConvert) }
                        }
                    approvedStatus = map.inactiveStatus
                    rejectedStatus = map.inactiveStatus
                    msProcessId = applicationMapProperties.mapMSWorkPlanInspectionSubmittedForApproval
                    userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameHodRm
                }
                updateWorkPlanInspectionDetails(workPlanScheduled, map, loggedInUser)

                val hodList = commonDaoServices.findOfficersListBasedOnRole(
                    applicationMapProperties.mapMSComplaintWorkPlanMappedHODROLEID,
                    loggedInUserProfile.regionId?.id ?: throw ExpectedDataNotFound("MISSING BATCH REGION ID")
                )

                val rmList = commonDaoServices.findOfficersListBasedOnRole(
                    applicationMapProperties.mapMSComplaintWorkPlanMappedRMROLEID,
                    loggedInUserProfile.regionId?.id ?: throw ExpectedDataNotFound("MISSING BATCH REGION ID")
                )

                GlobalScope.launch(Dispatchers.IO) {
                    delay(100L)
                    hodList
                        ?.forEach { mp ->
                            val scheduleEmailDetails = WorkPlanScheduledDTO()
                            with(scheduleEmailDetails) {
                                baseUrl = applicationMapProperties.baseUrlValue
                                fullName = commonDaoServices.concatenateName(mp)
                                refNumber = fileSaved.second.referenceNumber
                                batchRefNumber = batchReferenceNo
                                yearCodeName = fileSaved.second.yearNameId?.yearName
                                dateSubmitted = commonDaoServices.getCurrentDate()

                            }
                            val taskNotify = NotificationBodyDto().apply {
                                fromName = commonDaoServices.concatenateName(loggedInUser)
                                toName = commonDaoServices.concatenateName(mp)
                                batchReferenceNoFound = batchReferenceNo
                                referenceNoFound = workPlanScheduled.referenceNumber
                                dateAssigned = commonDaoServices.getCurrentDate()
                                processType = when {
                                    workPlanScheduled.complaintId != null -> {
                                        "COMPLAINT-PLAN"
                                    }
                                    else -> {
                                        "WORK-PLAN"
                                    }
                                }
                            }

                            createNotificationTask(
                                taskNotify,
                                applicationMapProperties.mapMsNotificationNewTask,
                                map, null, loggedInUser, mp
                            )
                            commonDaoServices.sendEmailWithUserEntity(
                                mp,
                                applicationMapProperties.mapMsWorkPlanScheduleSubmitedForApproval,
                                scheduleEmailDetails,
                                map,
                                fileSaved.first
                            )
                        }
                    rmList
                        ?.forEach { mp ->
                            val taskNotify = NotificationBodyDto().apply {
                                fromName = commonDaoServices.concatenateName(loggedInUser)
                                toName = commonDaoServices.concatenateName(mp)
                                batchReferenceNoFound = batchReferenceNo
                                referenceNoFound = workPlanScheduled.referenceNumber
                                dateAssigned = commonDaoServices.getCurrentDate()
                                processType = if (workPlanScheduled.complaintId != null) {
                                    "COMPLAINT-PLAN"
                                } else {
                                    "WORK-PLAN"
                                }
                            }

                            createNotificationTask(
                                taskNotify,
                                applicationMapProperties.mapMsNotificationNewTask,
                                map, null, loggedInUser, mp
                            )

                            val scheduleEmailDetails = WorkPlanScheduledDTO()
                            with(scheduleEmailDetails) {
                                baseUrl = applicationMapProperties.baseUrlValue
                                fullName = commonDaoServices.concatenateName(mp)
                                refNumber = fileSaved.second.referenceNumber
                                batchRefNumber = batchReferenceNo
                                yearCodeName = fileSaved.second.yearNameId?.yearName
                                dateSubmitted = commonDaoServices.getCurrentDate()

                            }
                            commonDaoServices.sendEmailWithUserEntity(
                                mp,
                                applicationMapProperties.mapMsWorkPlanScheduleSubmitedForApproval,
                                scheduleEmailDetails,
                                map,
                                fileSaved.first
                            )
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
    fun getAllWorPlanInspectionListBasedOnBatchRefNo(
        batchReferenceNo: String,
        complaintStatus: Boolean,
        page: PageRequest
    ): WorkPlanScheduleListDetailsDto {
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
                        findALlWorkPlanDetailsAssociatedWithWorkPlanIDWithComplaintIN(createdWorkPlan.id, page).toList()
                    }
                    else -> {
                        findALlWorkPlanDetailsAssociatedWithWorkPlanID(createdWorkPlan.id, page).toList()
                    }
                }

            }
            auth.authorities.stream().anyMatch { authority ->
                authority.authority == "MS_HOD_READ"
                        || authority.authority == "MS_RM_READ"
                        || authority.authority == "MS_HOF_READ"
            } -> {
                createdWorkPlan = findCreatedWorkPlanWIthRefNumberAndRegion(
                    batchReferenceNo,
                    loggedInUserProfile.regionId?.id
                        ?: throw ExpectedDataNotFound("Missing region value on your user profile  details")
                )
                workPlanList = when {
                    complaintStatus -> {
                        findALlWorkPlanDetailsAssociatedWithWorkPlanIDWithComplaintIN(createdWorkPlan.id, page).toList()
                    }
                    else -> {
                        findALlWorkPlanDetailsAssociatedWithWorkPlanID(createdWorkPlan.id, page).toList()
                    }
                }
            }
            auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_DIRECTOR_READ" } -> {
                createdWorkPlan = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
                workPlanList = when {
                    complaintStatus -> {
                        findALlWorkPlanDetailsAssociatedWithWorkPlanIDWithComplaintIN(createdWorkPlan.id, page).toList()
                    }
                    else -> {
                        findALlWorkPlanDetailsAssociatedWithWorkPlanID(createdWorkPlan.id, page).toList()
                    }
                }
            }
            else -> {
                throw ExpectedDataNotFound("Can't access this page Due to Invalid authority")
            }
        }

        return mapWorkPlanInspectionListDto(workPlanList, mapWorkPlanBatchDetailsDto(createdWorkPlan, map))
    }

    @PreAuthorize("hasAuthority('MS_IO_READ') or hasAuthority('MS_HOD_READ') or hasAuthority('MS_RM_READ') or hasAuthority('MS_HOF_READ') or hasAuthority('MS_DIRECTOR_READ')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun getAllWorPlanInspectionAllCompletedLists(
        batchReferenceNo: String,
        complaintStatus: Boolean,
        page: PageRequest
    ): WorkPlanScheduleListDetailsDto {
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
                        || authority.authority == "MS_RM_READ"
            } -> {
                createdWorkPlan = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
                workPlanList = when {
                    complaintStatus -> {
                        generateWorkPlanRepo.findByWorkPlanYearIdAndMsProcessEndedStatusAndComplaintIdIsNotNull(
                            createdWorkPlan.id,
                            map.activeStatus,
                            page
                        )?.toList()
                    }
                    else -> {
                        generateWorkPlanRepo.findByWorkPlanYearIdAndMsProcessEndedStatus(
                            createdWorkPlan.id,
                            map.activeStatus,
                            page
                        )?.toList()
                    }
                }


            }
            else -> throw ExpectedDataNotFound("Can't access this page Due to Invalid authority")
        }

        return mapWorkPlanInspectionListDto(workPlanList, mapWorkPlanBatchDetailsDto(createdWorkPlan, map))
    }

    @PreAuthorize("hasAuthority('MS_IO_READ') or hasAuthority('MS_HOD_READ') or hasAuthority('MS_RM_READ') or hasAuthority('MS_HOF_READ') or hasAuthority('MS_DIRECTOR_READ')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun getAllWorPlanInspectionAllSameDateLists(
        batchReferenceNo: String,
        complaintStatus: Boolean,
        page: PageRequest
    ): WorkPlanScheduleListDetailsDto {
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
                        || authority.authority == "MS_RM_READ"
            } -> {
                createdWorkPlan = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
                workPlanList = when {
                    complaintStatus -> {
                        generateWorkPlanRepo.getWorkPlanComplaintWithSameDetails(
                            createdWorkPlan.id,
                            loggedInUser.id!!
                        )
                    }
                    else -> {
                        generateWorkPlanRepo.getWorkPlanWithSameDetails(
                            createdWorkPlan.id,
                            loggedInUser.id!!
                        )
                    }
                }


            }
            else -> throw ExpectedDataNotFound("Can't access this page Due to Invalid authority")
        }

        return mapWorkPlanInspectionListDto(workPlanList, mapWorkPlanBatchDetailsDto(createdWorkPlan, map))
    }

    @PreAuthorize("hasAuthority('MS_IO_READ') or hasAuthority('MS_HOD_READ') or hasAuthority('MS_RM_READ') or hasAuthority('MS_HOF_READ') or hasAuthority('MS_DIRECTOR_READ')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun getAllWorPlanInspectionAllNotCompletedLists(
        batchReferenceNo: String,
        complaintStatus: Boolean,
        page: PageRequest
    ): WorkPlanScheduleListDetailsDto {
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
                        || authority.authority == "MS_RM_READ"
            } -> {
                createdWorkPlan = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
                workPlanList = when {
                    complaintStatus -> {
                        generateWorkPlanRepo.findByWorkPlanYearIdAndMsProcessEndedStatusAndComplaintIdIsNotNull(
                            createdWorkPlan.id,
                            map.inactiveStatus,
                            page
                        )?.toList()
                    }
                    else -> {
                        generateWorkPlanRepo.findByWorkPlanYearIdAndMsProcessEndedStatus(
                            createdWorkPlan.id,
                            map.inactiveStatus,
                            page
                        )?.toList()
                    }
                }
            }
            else -> throw ExpectedDataNotFound("Can't access this page Due to Invalid authority")
        }

        return mapWorkPlanInspectionListDto(workPlanList, mapWorkPlanBatchDetailsDto(createdWorkPlan, map))
    }

    @PreAuthorize("hasAuthority('MS_IO_READ') or hasAuthority('MS_HOD_READ') or hasAuthority('MS_RM_READ') or hasAuthority('MS_HOF_READ') or hasAuthority('MS_DIRECTOR_READ')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun getAllWorPlanInspectionListMyTask(
        batchReferenceNo: String,
        complaintStatus: Boolean,
        page: PageRequest
    ): WorkPlanScheduleListDetailsDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val auth = commonDaoServices.loggedInUserAuthentication()
        val loggedInUserProfile = commonDaoServices.findUserProfileByUserID(loggedInUser)
        val map = commonDaoServices.serviceMapDetails(appId)
        val createdWorkPlan: WorkPlanCreatedEntity?
        val workPlanList: List<MsWorkPlanGeneratedEntity>?
        when {
            auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_IO_READ" } -> {
                createdWorkPlan = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
                workPlanList = when {
                    complaintStatus -> {
                        generateWorkPlanRepo.findByWorkPlanYearIdAndOfficerIdAndUserTaskIdAndComplaintIdIsNotNull(
                            createdWorkPlan.id,
                            loggedInUser.id ?: throw ExpectedDataNotFound("Missing Logged In User ID"),
                            applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO,
                            page
                        )?.toList()
                    }
                    else -> {
                        generateWorkPlanRepo.findByWorkPlanYearIdAndOfficerIdAndUserTaskId(
                            createdWorkPlan.id,
                            loggedInUser.id ?: throw ExpectedDataNotFound("Missing Logged In User ID"),
                            applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO,
                            page
                        )?.toList()
                    }
                }
            }
            auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_HOF_READ" } -> {
                createdWorkPlan = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
                workPlanList = when {
                    complaintStatus -> {
                        generateWorkPlanRepo.findByWorkPlanYearIdAndHofAssignedAndUserTaskIdAndComplaintIdIsNotNull(
                            createdWorkPlan.id,
                            loggedInUser.id ?: throw ExpectedDataNotFound("Missing Logged In User ID"),
                            applicationMapProperties.mapMSCPWorkPlanUserTaskNameHof,
                            page
                        )?.toList()
                    }
                    else -> {
                        generateWorkPlanRepo.findByWorkPlanYearIdAndHofAssignedAndUserTaskId(
                            createdWorkPlan.id,
                            loggedInUser.id ?: throw ExpectedDataNotFound("Missing Logged In User ID"),
                            applicationMapProperties.mapMSCPWorkPlanUserTaskNameHof,
                            page
                        )?.toList()
                    }
                }
            }
            auth.authorities.stream().anyMatch { authority ->
                authority.authority == "MS_HOD_READ"
                        || authority.authority == "MS_RM_READ"
            } -> {
                createdWorkPlan = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
                workPlanList = when {
                    complaintStatus -> {
                        generateWorkPlanRepo.findByWorkPlanYearIdAndHodRmAssignedAndUserTaskIdAndComplaintIdIsNotNull(
                            createdWorkPlan.id,
                            loggedInUser.id ?: throw ExpectedDataNotFound("Missing Logged In User ID"),
                            applicationMapProperties.mapMSCPWorkPlanUserTaskNameHodRm,
                            page
                        )?.toList()
                    }
                    else -> {
                        generateWorkPlanRepo.findByWorkPlanYearIdAndHodRmAssignedAndUserTaskId(
                            createdWorkPlan.id,
                            loggedInUser.id ?: throw ExpectedDataNotFound("Missing Logged In User ID"),
                            applicationMapProperties.mapMSCPWorkPlanUserTaskNameHodRm,
                            page
                        )?.toList()
                    }
                }
            }
            auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_DIRECTOR_READ" } -> {
                createdWorkPlan = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
                workPlanList = when {
                    complaintStatus -> {
                        generateWorkPlanRepo.findByWorkPlanYearIdAndUserTaskIdAndComplaintIdIsNotNull(
                            createdWorkPlan.id,
                            applicationMapProperties.mapMSCPWorkPlanUserTaskNameDirector,
                            page
                        )?.toList()
                    }
                    else -> {
                        generateWorkPlanRepo.findByWorkPlanYearIdAndUserTaskId(
                            createdWorkPlan.id,
                            applicationMapProperties.mapMSCPWorkPlanUserTaskNameDirector,
                            page
                        )?.toList()
                    }
                }
            }
            else -> {
                throw ExpectedDataNotFound("Can't access this page Due to Invalid authority")
            }
        }

        return mapWorkPlanInspectionListDto(workPlanList, mapWorkPlanBatchDetailsDto(createdWorkPlan, map))
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
        val scheduleEmailDetails = WorkPlanScheduledDTO()
        var emailDetails = ""
        when {
            body.approvalStatus -> {
                when (batchDetails.batchClosed) {
                    map.activeStatus -> {
                        with(workPlanScheduled) {
                            hodRmAssigned = loggedInUser.id
                            timelineStartDate = commonDaoServices.getCurrentDate()
                            timelineEndDate =
                                applicationMapProperties.mapMSWorkPlanInspectionApprovedWorPlan.let { timeLine ->
                                    findProcessNameByID(timeLine, 1).timelinesDay
                                }?.let { daysCount ->
                                    commonDaoServices.addDaysSkippingWeekends(LocalDate.now(), daysCount)
                                        ?.let { daysConvert -> commonDaoServices.localDateToTimestamp(daysConvert) }
                                }
                            msProcessId = applicationMapProperties.mapMSWorkPlanInspectionApprovedWorPlan
                            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
                            approved = "APPROVED"
                            progressStep = approved
                            reportPendingReview = map.inactiveStatus
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
                        with(workPlanScheduled) {
                            hodRmAssigned = loggedInUser.id
                            msProcessId = applicationMapProperties.mapMSWorkPlanInspectionRejectedWorPlan
                            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
                            rejected = "REJECTED"
                            progressStep = rejected
                            rejectedBy = commonDaoServices.concatenateName(loggedInUser)
                            rejectedStatus = map.activeStatus
                            approvedStatus = map.inactiveStatus
                            updatedStatus = map.inactiveStatus
                            resubmitStatus = map.inactiveStatus
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
        with(remarksDto) {
            remarksDescription = body.remarks
            remarksStatus = scheduleEmailDetails.approvalStatus
            processID = workPlanScheduled.msProcessId
            userId = loggedInUser.id
        }

        when (fileSaved.first.status) {
            map.successStatus -> {

                val remarksSaved = workPlanAddRemarksDetails(fileSaved.second.id, remarksDto, map, loggedInUser)
                when (remarksSaved.first.status) {
                    map.successStatus -> {

                            val userDetails = workPlanScheduled.officerId?.let { commonDaoServices.findUserByID(it) }
                            val taskNotify = NotificationBodyDto().apply {
                                fromName = commonDaoServices.concatenateName(loggedInUser)
                                toName = userDetails?.let { commonDaoServices.concatenateName(it) }
                                batchReferenceNoFound = batchReferenceNo
                                referenceNoFound = workPlanScheduled.referenceNumber
                                dateAssigned = commonDaoServices.getCurrentDate()
                                processType = "WORK-PLAN"
                            }

                            createNotificationTask(
                                taskNotify,
                                applicationMapProperties.mapMsNotificationNewTask,
                                map, null, loggedInUser, userDetails
                            )


                            with(scheduleEmailDetails) {
                                baseUrl = applicationMapProperties.baseUrlValue
                                fullName = userDetails?.let { commonDaoServices.concatenateName(it) }
                                refNumber = referenceNo
                                batchRefNumber = batchReferenceNo
                                yearCodeName = batchDetails.yearNameId?.yearName
                                dateSubmitted = commonDaoServices.getCurrentDate()

                            }
                        GlobalScope.launch(Dispatchers.IO) {
                            delay(100L)
                            userDetails?.let {
                                commonDaoServices.sendEmailWithUserEntity(
                                    it,
                                    emailDetails,
                                    scheduleEmailDetails,
                                    map,
                                    fileSaved.first
                                )
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

    @PreAuthorize("hasAuthority('MS_HOF_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun updateWorkPlanSendingEmailDetailsApprovalHOFStatus(
        referenceNo: String,
        batchReferenceNo: String,
        body: WorkPlanSendResultsApprovalDto
    ): WorkPlanInspectionDto {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        var workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
        var savedSSfComplianceStatus = msFuelDaoServices.findSampleSubmittedBYID(body.ssfID)
        var remarkStatusValue = "N/A"

        with(savedSSfComplianceStatus) {
            hofApproveSendingEmailDate= commonDaoServices.getCurrentDate()
            when {
                body.approvalStatus -> {
                    hofApproveSendingEmailStatus= map.activeStatus
                    remarkStatusValue = "APPROVED BY HOF"
                }
                else -> {
                    hofApproveSendingEmailStatus= map.inactiveStatus
                    remarkStatusValue = "REJECTED BY HOF"
                }
            }
        }

        savedSSfComplianceStatus = sampleSubmissionLabRepo.save(savedSSfComplianceStatus)

        with(workPlanScheduled) {
            when {
                body.approvalStatus -> {
                    msProcessId = applicationMapProperties.mapMSWorkPlanInspectionApproveWorkPlanSendingEmailHOF
                    userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameHodRm
                }
                else -> {
                    msProcessId = applicationMapProperties.mapMSWorkPlanInspectionRejectWorkPlanSendingEmailHOF
                    userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
                }
            }
        }

        workPlanScheduled = updateWorkPlanInspectionDetails(workPlanScheduled, map, loggedInUser).second

        val remarksDto = RemarksToAddDto()
        with(remarksDto) {
            remarksDescription = body.remarks
            remarksStatus = remarkStatusValue
            processID = workPlanScheduled.msProcessId
            userId = loggedInUser.id
        }

        val remarksSaved = workPlanAddRemarksDetails(workPlanScheduled.id, remarksDto, map, loggedInUser)
        when (remarksSaved.first.status) {
            map.successStatus -> {
                return workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)
            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(remarksSaved.first))
            }
        }
    }

    @PreAuthorize("hasAuthority('MS_HOD_MODIFY') or hasAuthority('MS_RM_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun updateWorkPlanSendingEmailDetailsApprovalHODStatus(
        referenceNo: String,
        batchReferenceNo: String,
        body: WorkPlanSendResultsApprovalDto
    ): WorkPlanInspectionDto {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        var workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
        var savedSSfComplianceStatus = msFuelDaoServices.findSampleSubmittedBYID(body.ssfID)
        var remarkStatusValue = "N/A"

        with(savedSSfComplianceStatus) {
            hodApproveSendingEmailDate= commonDaoServices.getCurrentDate()
            when {
                body.approvalStatus -> {
                    hodApproveSendingEmailStatus= map.activeStatus
                    remarkStatusValue = "APPROVED BY HOD/RM"
                }
                else -> {
                    hodApproveSendingEmailStatus= map.inactiveStatus
                    remarkStatusValue = "REJECTED BY HOD/RM"
                }
            }
        }

        savedSSfComplianceStatus = sampleSubmissionLabRepo.save(savedSSfComplianceStatus)

        with(workPlanScheduled) {
            when {
                body.approvalStatus -> {
                    msProcessId = applicationMapProperties.mapMSWorkPlanInspectionApproveWorkPlanSendingEmailHODRM
                    userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
                }
                else -> {
                    msProcessId = applicationMapProperties.mapMSWorkPlanInspectionRejectWorkPlanSendingEmailHODRM
                    userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
                }
            }
        }

        workPlanScheduled = updateWorkPlanInspectionDetails(workPlanScheduled, map, loggedInUser).second


        val remarksDto = RemarksToAddDto()
        with(remarksDto) {
            remarksDescription = body.remarks
            remarksStatus = remarkStatusValue
            processID = workPlanScheduled.msProcessId
            userId = loggedInUser.id
        }

        val remarksSaved = workPlanAddRemarksDetails(workPlanScheduled.id, remarksDto, map, loggedInUser)
        when (remarksSaved.first.status) {
            map.successStatus -> {
                return workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)
            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(remarksSaved.first))
            }
        }
    }


    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun workPlanScheduleFinalRemarkOnSized(
        referenceNo: String,
        batchReferenceNo: String,
        body: WorkPlanFeedBackDto
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        val workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
        body.id?.let {
            findDataReportByWorkPlanInspectionIDAndID(workPlanScheduled.id, it)?.let { dataReport ->
                with(dataReport) {
                    finalActionOnSized = map.activeStatus
                    finalActionSeizedGoods = body.hodFeedBackRemarks
                    modifiedBy = commonDaoServices.concatenateName(loggedInUser)
                    modifiedOn = commonDaoServices.getTimestamp()
                }
                dataReportRepo.save(dataReport)

                return workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)
            }
        } ?: throw ExpectedDataNotFound("MISSING DATA REPORT FOR ID NUMBER : ${body.id}")

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


        val hofDetailsFound =
            commonDaoServices.findUserByID(body.assignedIo ?: throw ExpectedDataNotFound("Missing Assigned HOF ID"))
        with(workPlanScheduled) {
            hofAssigned = hofDetailsFound.id
        }

        val remarksDto = RemarksToAddDto()
        with(remarksDto) {
            remarksDescription = body.assignedRemarks
            remarksStatus = "N/A"
            processID = workPlanScheduled.msProcessId
            userId = loggedInUser.id
        }

        val fileSaved = updateWorkPlanInspectionDetails(workPlanScheduled, map, loggedInUser)
        workPlanScheduled = fileSaved.second
        when (fileSaved.first.status) {
            map.successStatus -> {

                val remarksSaved = workPlanAddRemarksDetails(fileSaved.second.id, remarksDto, map, loggedInUser)
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
            val usersEntity =
                commonDaoServices.findUserByID(body.assignedIo ?: throw ExpectedDataNotFound("Missing Assigned IO ID"))
            officerId = usersEntity.id
            officerName = commonDaoServices.concatenateName(usersEntity)
        }

        val remarksDto = RemarksToAddDto()
        with(remarksDto) {
            remarksDescription = body.assignedRemarks
            remarksStatus = "N/A"
            processID = workPlanScheduled.msProcessId
            userId = loggedInUser.id
        }

        val fileSaved = updateWorkPlanInspectionDetails(workPlanScheduled, map, loggedInUser)
        workPlanScheduled = fileSaved.second

        when (fileSaved.first.status) {
            map.successStatus -> {

                val remarksSaved = workPlanAddRemarksDetails(fileSaved.second.id, remarksDto, map, loggedInUser)
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
                        with(workPlanScheduled) {
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
                        with(workPlanScheduled) {
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
        with(remarksDto) {
            remarksDescription = body.remarks
            remarksStatus = "N/A"
            processID = workPlanScheduled.msProcessId
            userId = loggedInUser.id
        }

        when (fileSaved.first.status) {
            map.successStatus -> {

                val remarksSaved = workPlanAddRemarksDetails(fileSaved.second.id, remarksDto, map, loggedInUser)
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
        productReferenceNo: String,
        referenceNo: String,
        batchReferenceNo: String,
        body: ApprovalDto
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        val workPlanProduct = findWorkPlanProductByReferenceNumber(productReferenceNo)
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
        var remarkStatusValue = "N/A"

        when {
            body.approvalStatus -> {
                with(workPlanProduct) {
                    clientAppealed = map.activeStatus
//                    msProcessId = applicationMapProperties.mapMSWorkPlanInspectionClientAppealedAwaitSuccessfull
//                    userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
                    remarkStatusValue = "APPEAL SUCCESSFULLY"
                }
            }
            else -> {
                with(workPlanProduct) {
                    clientAppealed = map.inactiveStatus
                    destructionStatus = map.activeStatus
                    appealStatus = map.inactiveStatus
//                    msProcessId = applicationMapProperties.mapMSWorkPlanInspectionPendingDestractionGoodReport
//                    userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
                    remarkStatusValue = "APPEAL NOT SUCCESSFULLY"
                }
            }
        }

        val fileSaved = updateWorkPlanProductDetails(workPlanProduct, map, loggedInUser)
//        workPlanScheduled = fileSaved.second
        val remarksDto = RemarksToAddDto()
        with(remarksDto) {
            remarksDescription = body.remarks
            remarksStatus = remarkStatusValue
            processID = workPlanScheduled.msProcessId
            userId = loggedInUser.id
        }

        when (fileSaved.first.status) {
            map.successStatus -> {

                val remarksSaved = workPlanAddRemarksDetails(workPlanScheduled.id, remarksDto, map, loggedInUser)
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
        productReferenceNo: String,
        referenceNo: String,
        batchReferenceNo: String,
        body: ApprovalDto
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        val workPlanProduct = findWorkPlanProductByReferenceNumber(productReferenceNo)
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
        var remarkStatusValue = "N/A"

        if (body.approvalStatus) {
            with(workPlanProduct) {
                destructionStatus = map.inactiveStatus
                appealStatus = map.activeStatus
//                    msProcessId = applicationMapProperties.mapMSWorkPlanInspectionPendingFinalRemarksHODRM
//                    userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameHodRm
                remarkStatusValue = "APPEALED"
            }
        } else {
            with(workPlanProduct) {
                destructionStatus = map.activeStatus
                appealStatus = map.inactiveStatus
//                    msProcessId = applicationMapProperties.mapMSWorkPlanInspectionPendingDestractionGoodReport
//                    userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
                remarkStatusValue = "NOT APPEALED"
            }
        }
        val fileSaved = updateWorkPlanProductDetails(workPlanProduct, map, loggedInUser)
//        workPlanScheduled = fileSaved.second
        val remarksDto = RemarksToAddDto()
        with(remarksDto) {
            remarksDescription = body.remarks
            remarksStatus = remarkStatusValue
            processID = workPlanScheduled.msProcessId
            userId = loggedInUser.id
        }

        when (fileSaved.first.status) {
            map.successStatus -> {

                val remarksSaved = workPlanAddRemarksDetails(workPlanScheduled.id, remarksDto, map, loggedInUser)
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
        body: ApprovalDto,
        finalReportStatus: Boolean
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        var fetchedPreliminary = MsPreliminaryReportEntity()
        fetchedPreliminary = when {
            finalReportStatus -> {
                findPreliminaryReportByWorkPlanGeneratedIDAndFinalReportStatus(workPlanScheduled.id, 1)
                    ?: throw ExpectedDataNotFound("Missing Preliminary Report For Work Plan with REF NR ${workPlanScheduled.referenceNumber}, do Not Exists")
            }
            else -> {
                findPreliminaryReportByWorkPlanGeneratedIDAndFinalReportStatus(workPlanScheduled.id, 0)
                    ?: throw ExpectedDataNotFound("Missing Preliminary Report For Work Plan with REF NR ${workPlanScheduled.referenceNumber}, do Not Exists")
            }
        }
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
        val scheduleEmailDetails = WorkPlanScheduledDTO()
        var remarkStatusValue = "N/A"
        var emailDetails = ""
        when {
            body.approvalStatus -> {
                when {
                    finalReportStatus -> {
                        with(fetchedPreliminary) {
                            approvedHofFinal = "APPROVED FINAL REPORT"
                            approvedRemarksHofFinal = body.remarks
                            approvedByHofFinal = commonDaoServices.concatenateName(loggedInUser)
                            approvedStatusHofFinal = map.activeStatus
//                            reportPendingReview = map.inactiveStatus
                            rejectedStatusHofFinal = map.inactiveStatus
                            approvedOnHofFinal = commonDaoServices.getCurrentDate()
                            remarkStatusValue = approvedHofFinal as String
                            scheduleEmailDetails.approvalStatus = remarkStatusValue
                            emailDetails = applicationMapProperties.mapMsWorkPlanFinalPreliminaryApprovalByHOFEmail
                        }
                    }
                    else -> {
                        with(fetchedPreliminary) {
                            approved = "APPROVED"
                            approvedRemarks = body.remarks
                            approvedBy = commonDaoServices.concatenateName(loggedInUser)
                            approvedStatus = map.activeStatus
                            rejectedStatus = map.inactiveStatus
//                            reportPendingReview = map.inactiveStatus
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
                    finalReportStatus -> {
                        with(fetchedPreliminary) {
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
                        with(fetchedPreliminary) {
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

        with(workPlanScheduled) {
            hofAssigned = loggedInUser.id
            when {
                finalReportStatus -> {
                    when {
                        fetchedPreliminary.approvedStatusHofFinal == map.activeStatus -> {
                            msProcessId = applicationMapProperties.mapMSWorkPlanInspectionFinalReportApprovedHOF
                            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameHodRm

                        }
                        fetchedPreliminary.rejectedStatusHofFinal == map.activeStatus -> {
                            msProcessId = applicationMapProperties.mapMSWorkPlanInspectionFinalReportRejectedHOF
                            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
                            updatedStatus = map.inactiveStatus
                            resubmitStatus = map.inactiveStatus
                        }
                    }
                }
                else -> {
                    when {
                        fetchedPreliminary.approvedStatus == map.activeStatus -> {
                            timelineStartDate = commonDaoServices.getCurrentDate()
                            timelineEndDate =
                                applicationMapProperties.mapMSWorkPlanInspectionPreliminaryReportApprovedHOF.let { timeLine ->
                                    findProcessNameByID(timeLine, 1).timelinesDay
                                }?.let { daysCount ->
                                    commonDaoServices.addDaysSkippingWeekends(LocalDate.now(), daysCount)
                                        ?.let { daysConvert -> commonDaoServices.localDateToTimestamp(daysConvert) }
                                }
                            msProcessId = applicationMapProperties.mapMSWorkPlanInspectionPreliminaryReportApprovedHOF
                            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameHodRm
                        }
                        fetchedPreliminary.rejectedStatus == map.activeStatus -> {
                            msProcessId = applicationMapProperties.mapMSWorkPlanInspectionPreliminaryReportRejectedHOF
                            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
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
                with(remarksDto) {
                    remarksDescription = body.remarks
                    processID = workPlanScheduled.msProcessId
                    remarksStatus = remarkStatusValue
                    userId = loggedInUser.id
                }
                val remarksSaved = workPlanAddRemarksDetails(fileSaved.second.id, remarksDto, map, loggedInUser)
                when (remarksSaved.first.status) {
                    map.successStatus -> {

                            if (body.approvalStatus) {
                                val hodDetails =workPlanScheduled.hodRmAssigned?.let { commonDaoServices.findUserByID(it) }
                                val taskNotify = NotificationBodyDto().apply {
                                    fromName = commonDaoServices.concatenateName(loggedInUser)
                                    toName = hodDetails?.let { commonDaoServices.concatenateName(it) }
                                    batchReferenceNoFound = batchReferenceNo
                                    referenceNoFound = workPlanScheduled.referenceNumber
                                    dateAssigned = commonDaoServices.getCurrentDate()
                                    processType = when {
                                        workPlanScheduled.complaintId != null -> {
                                            "COMPLAINT-PLAN"
                                        }
                                        else -> {
                                            "WORK-PLAN"
                                        }
                                    }
                                }

                                createNotificationTask(
                                    taskNotify,
                                    applicationMapProperties.mapMsNotificationNewTask,
                                    map, null, loggedInUser, hodDetails
                                )
                                with(scheduleEmailDetails) {
                                    baseUrl = applicationMapProperties.baseUrlValue
                                    fullName = hodDetails?.let { commonDaoServices.concatenateName(it) }
                                    refNumber = referenceNo
                                    batchRefNumber = batchReferenceNo
                                    yearCodeName = batchDetails.yearNameId?.yearName
                                    dateSubmitted = commonDaoServices.getCurrentDate()

                                }
                                if (hodDetails != null) {
                                    commonDaoServices.sendEmailWithUserEntity(
                                        hodDetails,
                                        emailDetails,
                                        scheduleEmailDetails,
                                        map,
                                        remarksSaved.first
                                    )
                                }
                            } else {
                                val officerDetails =
                                    workPlanScheduled.officerId?.let { commonDaoServices.findUserByID(it) }
                                val taskNotify = NotificationBodyDto().apply {
                                    fromName = commonDaoServices.concatenateName(loggedInUser)
                                    toName = officerDetails?.let { commonDaoServices.concatenateName(it) }
                                    batchReferenceNoFound = batchReferenceNo
                                    referenceNoFound = workPlanScheduled.referenceNumber
                                    dateAssigned = commonDaoServices.getCurrentDate()
                                    if (workPlanScheduled.complaintId != null) {
                                        processType = "COMPLAINT-PLAN"
                                    } else {
                                        processType = "WORK-PLAN"
                                    }
                                }

                                createNotificationTask(
                                    taskNotify,
                                    applicationMapProperties.mapMsNotificationNewTask,
                                    map, null, loggedInUser, officerDetails
                                )
                                with(scheduleEmailDetails) {
                                    baseUrl = applicationMapProperties.baseUrlValue
                                    fullName = officerDetails?.let { commonDaoServices.concatenateName(it) }
                                    refNumber = referenceNo
                                    batchRefNumber = batchReferenceNo
                                    yearCodeName = batchDetails.yearNameId?.yearName
                                    dateSubmitted = commonDaoServices.getCurrentDate()

                                }
                                if (officerDetails != null) {
                                    commonDaoServices.sendEmailWithUserEntity(
                                        officerDetails,
                                        emailDetails,
                                        scheduleEmailDetails,
                                        map,
                                        remarksSaved.first
                                    )
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
        body: ApprovalDto,
        finalReportStatus: Boolean
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        var fetchedPreliminary = MsPreliminaryReportEntity()
        fetchedPreliminary = when {
            finalReportStatus -> {
                findPreliminaryReportByWorkPlanGeneratedIDAndFinalReportStatus(workPlanScheduled.id, 1)
                    ?: throw ExpectedDataNotFound("Missing Preliminary Report For Work Plan with REF NR ${workPlanScheduled.referenceNumber}, do Not Exists")
            }
            else -> {
                findPreliminaryReportByWorkPlanGeneratedIDAndFinalReportStatus(workPlanScheduled.id, 0)
                    ?: throw ExpectedDataNotFound("Missing Preliminary Report For Work Plan with REF NR ${workPlanScheduled.referenceNumber}, do Not Exists")
            }
        }
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
        val scheduleEmailDetails = WorkPlanScheduledDTO()
        var emailDetails = ""

        var remarkStatusValue = "N/A"
        when {
            body.approvalStatus -> {
                when {
                    finalReportStatus -> {
                        with(fetchedPreliminary) {
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
                    else -> {
                        with(fetchedPreliminary) {
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
                    finalReportStatus -> {
                        with(fetchedPreliminary) {
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
                    else -> {
                        with(fetchedPreliminary) {
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

        fetchedPreliminary = updatePreliminaryReportDetails(fetchedPreliminary, map, loggedInUser).second

        with(workPlanScheduled) {
            when {
                finalReportStatus -> {
                    when {
                        fetchedPreliminary.approvedStatusHodFinal == map.activeStatus -> {
                            msFinalReportStatus = map.activeStatus
                            reportPendingReview = map.inactiveStatus
                            msProcessId = applicationMapProperties.mapMSWorkPlanInspectionFinalReportApprovedHODRM
                            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameDirector
                            val directorDetails = commonDaoServices.findAllUsersByDesignation(
                                map,
                                applicationMapProperties.mapMsComplaintAndWorkPlanDesignationDirector
                            )
                            directorDetails.forEach { dt ->
                                val taskNotify = NotificationBodyDto().apply {
                                    fromName = commonDaoServices.concatenateName(loggedInUser)
                                    toName = dt.userId?.let { commonDaoServices.concatenateName(it) }
                                    batchReferenceNoFound = batchReferenceNo
                                    referenceNoFound = workPlanScheduled.referenceNumber
                                    dateAssigned = commonDaoServices.getCurrentDate()
                                    processType = when {
                                        workPlanScheduled.complaintId != null -> {
                                            "COMPLAINT-PLAN"
                                        }
                                        else -> {
                                            "WORK-PLAN"
                                        }
                                    }
                                }

                                createNotificationTask(
                                    taskNotify,
                                    applicationMapProperties.mapMsNotificationNewTask,
                                    map, null, loggedInUser, dt.userId
                                )
                            }
                        }
                        fetchedPreliminary.rejectedStatusHodFinal == map.activeStatus -> {
                            msProcessId = applicationMapProperties.mapMSWorkPlanInspectionFinalReportRejectedHODRM
                            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameHof
                            updatedStatus = map.inactiveStatus
                            resubmitStatus = map.inactiveStatus
                        }
                    }
                }
                else -> {
                    when {
                        fetchedPreliminary.approvedStatusHod == map.activeStatus -> {
                            msFinalReportStatus = map.inactiveStatus
                            preliminaryApprovedStatus = map.activeStatus
                            reportPendingReview = map.inactiveStatus
                            msProcessId = applicationMapProperties.mapMSWorkPlanInspectionPreliminaryReportApprovedHODRM
                            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
                        }
                        fetchedPreliminary.rejectedStatusHod == map.activeStatus -> {
                            msProcessId = applicationMapProperties.mapMSWorkPlanInspectionPreliminaryReportRejectedHODRM
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
                with(remarksDto) {
                    remarksDescription = body.remarks
                    processID = workPlanScheduled.msProcessId
                    remarksStatus = remarkStatusValue
                    userId = loggedInUser.id
                }
                val remarksSaved = workPlanAddRemarksDetails(fileSaved.second.id, remarksDto, map, loggedInUser)
                when (remarksSaved.first.status) {
                    map.successStatus -> {

                            val ioDetails = workPlanScheduled.officerId?.let { commonDaoServices.findUserByID(it) }
                            val taskNotify = NotificationBodyDto().apply {
                                fromName = commonDaoServices.concatenateName(loggedInUser)
                                toName = ioDetails?.let { commonDaoServices.concatenateName(it) }
                                batchReferenceNoFound = batchReferenceNo
                                referenceNoFound = workPlanScheduled.referenceNumber
                                dateAssigned = commonDaoServices.getCurrentDate()
                                processType = if (workPlanScheduled.complaintId != null) {
                                    "COMPLAINT-PLAN"
                                } else {
                                    "WORK-PLAN"
                                }
                            }

                            createNotificationTask(
                                taskNotify,
                                applicationMapProperties.mapMsNotificationNewTask,
                                map, null, loggedInUser, ioDetails
                            )
                            with(scheduleEmailDetails) {
                                baseUrl = applicationMapProperties.baseUrlValue
                                fullName = ioDetails?.let { commonDaoServices.concatenateName(it) }
                                refNumber = referenceNo
                                batchRefNumber = batchReferenceNo
                                yearCodeName = batchDetails.yearNameId?.yearName
                                dateSubmitted = commonDaoServices.getCurrentDate()

                            }
                            if (ioDetails != null) {
                                commonDaoServices.sendEmailWithUserEntity(
                                    ioDetails,
                                    emailDetails,
                                    scheduleEmailDetails,
                                    map,
                                    remarksSaved.first
                                )
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

    @PreAuthorize("hasAuthority('MS_DIRECTOR_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun updateWorkPlanScheduleInspectionDetailsApprovalPreliminaryReportDirector(
        referenceNo: String,
        batchReferenceNo: String,
        body: ApprovalDto,
        finalReportStatus: Boolean
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        var fetchedPreliminary = MsPreliminaryReportEntity()
        fetchedPreliminary = when {
            finalReportStatus -> {
                findPreliminaryReportByWorkPlanGeneratedIDAndFinalReportStatus(workPlanScheduled.id, 1)
                    ?: throw ExpectedDataNotFound("Missing Preliminary Report For Work Plan with REF NR ${workPlanScheduled.referenceNumber}, do Not Exists")
            }
            else -> {
                findPreliminaryReportByWorkPlanGeneratedIDAndFinalReportStatus(workPlanScheduled.id, 0)
                    ?: throw ExpectedDataNotFound("Missing Preliminary Report For Work Plan with REF NR ${workPlanScheduled.referenceNumber}, do Not Exists")
            }
        }
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
        val scheduleEmailDetails = WorkPlanScheduledDTO()
        var emailDetails = ""

        var remarkStatusValue = "N/A"
        when {
            body.approvalStatus -> {
                with(fetchedPreliminary) {
                    approvedDirectorFinal = "APPROVED"
                    approvedRemarksDirectorFinal = body.remarks
                    approvedByDirectorFinal = commonDaoServices.concatenateName(loggedInUser)
                    approvedStatusDirectorFinal = map.activeStatus
                    rejectedStatusDirectorFinal = map.inactiveStatus
                    approvedOnDirectorFinal = commonDaoServices.getCurrentDate()
                    remarkStatusValue = approvedDirectorFinal as String
                    scheduleEmailDetails.approvalStatus = remarkStatusValue
                    emailDetails = applicationMapProperties.mapMsWorkPlanFinalPreliminaryApprovalByDirectorEmail
                }


            }
            else -> {
                with(fetchedPreliminary) {
                    rejectedDirectorFinal = "REJECTED"
                    rejectedRemarksDirectorFinal = body.remarks
                    rejectedByDirectorFinal = commonDaoServices.concatenateName(loggedInUser)
                    approvedStatusDirectorFinal = map.inactiveStatus
                    rejectedStatusDirectorFinal = map.activeStatus
                    rejectedOnDirectorFinal = commonDaoServices.getCurrentDate()
                    remarkStatusValue = rejectedDirectorFinal as String
                    scheduleEmailDetails.approvalStatus = remarkStatusValue
                    emailDetails = applicationMapProperties.mapMsWorkPlanFinalPreliminaryRejectedByDirectorEmail
                }
            }
        }

        fetchedPreliminary = updatePreliminaryReportDetails(fetchedPreliminary, map, loggedInUser).second

        with(workPlanScheduled) {
            when {
                fetchedPreliminary.approvedStatusDirectorFinal == map.activeStatus -> {
                    msFinalReportStatus = map.activeStatus
                    reportPendingReview = map.inactiveStatus
                    msProcessId = applicationMapProperties.mapMSWorkPlanInspectionFinalReportApprovedDIRECTOR
                    userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameHodRm
                    directorAssigned = loggedInUser.id

                    val hodDetails =workPlanScheduled.hodRmAssigned?.let { commonDaoServices.findUserByID(it) }
                    val taskNotify = NotificationBodyDto().apply {
                        fromName = commonDaoServices.concatenateName(loggedInUser)
                        toName = hodDetails?.let { commonDaoServices.concatenateName(it) }
                        batchReferenceNoFound = batchReferenceNo
                        referenceNoFound = workPlanScheduled.referenceNumber
                        dateAssigned = commonDaoServices.getCurrentDate()
                        processType = when {
                            workPlanScheduled.complaintId != null -> {
                                "COMPLAINT-PLAN"
                            }
                            else -> {
                                "WORK-PLAN"
                            }
                        }
                    }

                    createNotificationTask(
                        taskNotify,
                        applicationMapProperties.mapMsNotificationNewTask,
                        map, null, loggedInUser, hodDetails
                    )
                }
                fetchedPreliminary.rejectedStatusDirectorFinal == map.activeStatus -> {
                    msProcessId = applicationMapProperties.mapMSWorkPlanInspectionFinalReportRejectedDirector
                    userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameHodRm
                    updatedStatus = map.inactiveStatus
                    resubmitStatus = map.inactiveStatus
                }
                else -> {

                }
            }

        }
        val fileSaved = updateWorkPlanInspectionDetails(workPlanScheduled, map, loggedInUser)

        when (fileSaved.first.status) {
            map.successStatus -> {
                workPlanScheduled = fileSaved.second
                val remarksDto = RemarksToAddDto()
                with(remarksDto) {
                    remarksDescription = body.remarks
                    processID = workPlanScheduled.msProcessId
                    remarksStatus = remarkStatusValue
                    userId = loggedInUser.id
                }
                val remarksSaved = workPlanAddRemarksDetails(fileSaved.second.id, remarksDto, map, loggedInUser)
                when (remarksSaved.first.status) {
                    map.successStatus -> {

                            val ioDetails = workPlanScheduled.officerId?.let { commonDaoServices.findUserByID(it) }
                            val taskNotify = NotificationBodyDto().apply {
                                fromName = commonDaoServices.concatenateName(loggedInUser)
                                toName = ioDetails?.let { commonDaoServices.concatenateName(it) }
                                batchReferenceNoFound = batchReferenceNo
                                referenceNoFound = workPlanScheduled.referenceNumber
                                dateAssigned = commonDaoServices.getCurrentDate()
                                processType = if (workPlanScheduled.complaintId != null) {
                                    "COMPLAINT-PLAN"
                                } else {
                                    "WORK-PLAN"
                                }
                            }

                            createNotificationTask(
                                taskNotify,
                                applicationMapProperties.mapMsNotificationNewTask,
                                map, null, loggedInUser, ioDetails
                            )
                            with(scheduleEmailDetails) {
                                baseUrl = applicationMapProperties.baseUrlValue
                                fullName = ioDetails?.let { commonDaoServices.concatenateName(it) }
                                refNumber = referenceNo
                                batchRefNumber = batchReferenceNo
                                yearCodeName = batchDetails.yearNameId?.yearName
                                dateSubmitted = commonDaoServices.getCurrentDate()

                            }
                            if (ioDetails != null) {
                                commonDaoServices.sendEmailWithUserEntity(
                                    ioDetails,
                                    emailDetails,
                                    scheduleEmailDetails,
                                    map,
                                    remarksSaved.first
                                )
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
        body: WorkPlanFinalRecommendationDto,
        productReferenceNo: String
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        val workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        val workPlanProduct = findWorkPlanProductByReferenceNumber(productReferenceNo)
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)

        var remarkStatusValue = "N/A"
        var emailDetails = ""
        val scheduleEmailDetails = WorkPlanScheduledDTO()
        val recommendationList = mutableListOf<String>()
        var recommendationDetailsValues:String? = null
        var destructionFound = false
        var numberTest = 1
        body.recommendationId.forEach { rec ->
            val recommendationDetails = recommendationRepo.findByIdOrNull(rec.recommendationId)
                ?: throw ExpectedDataNotFound("Missing Recommendation details with ID ${body.recommendationId}, do Not Exists")
            if (recommendationDetails.id == applicationMapProperties.mapMsWorkPlanDestrctionID) {
                destructionFound = true
            }
            recommendationDetailsValues = " $numberTest. ${recommendationDetails.recommendationName}; "
            numberTest++
        }

        with(workPlanProduct) {
            if (workPlanScheduled.finalReportGenerated == map.activeStatus) {
                hodRecommendationRemarks = body.hodRecommendationRemarks
                recommendation = commonDaoServices.convertClassToJson(body.recommendationId)
                remarkStatusValue = commonDaoServices.convertClassToJson(body.recommendationId).toString()
                hodRecommendationStatus = map.activeStatus
                varField1 = recommendationDetailsValues
                when {
                    destructionFound -> {
                        destructionRecommended = map.activeStatus
                        scheduleEmailDetails.approvalStatus = remarkStatusValue
                        emailDetails = applicationMapProperties.mapMsOfficerWorkPlanDestructionEmail
                    }
                    else -> {
                        destructionRecommended = map.inactiveStatus
                        scheduleEmailDetails.approvalStatus = remarkStatusValue
                        emailDetails = applicationMapProperties.mapMsOfficerWorkPlanNotDestructionEmail
                    }
                }

            }

        }
        val fileSaved = updateWorkPlanProductDetails(workPlanProduct, map, loggedInUser)

        when (fileSaved.first.status) {
            map.successStatus -> {
                val remarksDto = RemarksToAddDto()
                with(remarksDto) {
                    remarksDescription = body.hodRecommendationRemarks
//                    remarksStatus = remarkStatusValue
                    processID = workPlanScheduled.msProcessId
                    userId = loggedInUser.id
                }
                val remarksSaved = workPlanAddRemarksDetails(workPlanScheduled.id, remarksDto, map, loggedInUser)
                when (remarksSaved.first.status) {
                    map.successStatus -> {

//                            val ioDetails = workPlanScheduled.officerId?.let { commonDaoServices.findUserByID(it) }
//                            with(scheduleEmailDetails){
//                                baseUrl= applicationMapProperties.baseUrlValue
//                                fullName = ioDetails?.let { commonDaoServices.concatenateName(it) }
//                                refNumber = referenceNo
//                                batchRefNumber = batchReferenceNo
//                                yearCodeName = batchDetails.yearNameId?.yearName
//                                dateSubmitted = commonDaoServices.getCurrentDate()
//
//                            }
//                            if (ioDetails != null) {
//                                commonDaoServices.sendEmailWithUserEntity(ioDetails, emailDetails, scheduleEmailDetails, map, remarksSaved.first)
//                            }

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
    fun addWorkPlanScheduleEndFinalRecommendationByHOD(
        referenceNo: String,
        batchReferenceNo: String
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        val workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
        var destructionFound = false
        workPlanProductsRepo.findByWorkPlanId(workPlanScheduled.id)
            ?.forEach { prod ->
                if (prod.destructionRecommended == map.activeStatus) {
                    destructionFound = true
                }
            }

        with(workPlanScheduled) {
            hodRecommendationStatus = map.activeStatus
            reportPendingReview = map.activeStatus
            directorRecommendationRemarksStatus = map.activeStatus
            if (destructionFound) {
                destructionRecommended = map.activeStatus
            }
            timelineStartDate = commonDaoServices.getCurrentDate()
            timelineEndDate = applicationMapProperties.mapMSWorkPlanInspectionRecommendationsADDED.let { timeLine ->
                findProcessNameByID(timeLine, 1).timelinesDay
            }?.let { daysCount ->
                commonDaoServices.addDaysSkippingWeekends(LocalDate.now(), daysCount)
                    ?.let { daysConvert -> commonDaoServices.localDateToTimestamp(daysConvert) }
            }
            msProcessId = applicationMapProperties.mapMSWorkPlanInspectionRecommendationsADDED
//            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameDirector
            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
        }

        val fileSaved = updateWorkPlanInspectionDetails(workPlanScheduled, map, loggedInUser)

        when (fileSaved.first.status) {
            map.successStatus -> {
                val directorDetails = commonDaoServices.findAllUsersByDesignation(
                    map,
                    applicationMapProperties.mapMsComplaintAndWorkPlanDesignationDirector
                )
                directorDetails.forEach { dt ->
                    val taskNotify = NotificationBodyDto().apply {
                        fromName = commonDaoServices.concatenateName(loggedInUser)
                        toName = dt.userId?.let { commonDaoServices.concatenateName(it) }
                        batchReferenceNoFound = batchReferenceNo
                        referenceNoFound = workPlanScheduled.referenceNumber
                        dateAssigned = commonDaoServices.getCurrentDate()
                        processType = if (workPlanScheduled.complaintId != null) {
                            "COMPLAINT-PLAN"
                        } else {
                            "WORK-PLAN"
                        }
                    }

                    createNotificationTask(
                        taskNotify,
                        applicationMapProperties.mapMsNotificationNewTask,
                        map, null, loggedInUser, dt.userId
                    )
                }

                return workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)
            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved.first))
            }
        }

    }


    @PreAuthorize("hasAuthority('MS_DIRECTOR_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun addWorkPlanScheduleEndFinalRecommendationByDIRECTOR(
        referenceNo: String,
        batchReferenceNo: String
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        val workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)

        with(workPlanScheduled) {
            directorAssigned = loggedInUser.id
            directorRecommendationRemarksStatus = map.activeStatus
            reportPendingReview = map.inactiveStatus
            timelineStartDate = commonDaoServices.getCurrentDate()
            timelineEndDate = applicationMapProperties.mapMSWorkPlanInspectionDirectorRemarksADDED.let { timeLine ->
                findProcessNameByID(timeLine, 1).timelinesDay
            }?.let { daysCount ->
                commonDaoServices.addDaysSkippingWeekends(LocalDate.now(), daysCount)
                    ?.let { daysConvert -> commonDaoServices.localDateToTimestamp(daysConvert) }
            }
            msProcessId = applicationMapProperties.mapMSWorkPlanInspectionDirectorRemarksADDED
            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
        }

        val fileSaved = updateWorkPlanInspectionDetails(workPlanScheduled, map, loggedInUser)

        when (fileSaved.first.status) {
            map.successStatus -> {
                val ioDetails = workPlanScheduled.officerId?.let { commonDaoServices.findUserByID(it) }
                val taskNotify = NotificationBodyDto().apply {
                    fromName = commonDaoServices.concatenateName(loggedInUser)
                    toName = ioDetails?.let { commonDaoServices.concatenateName(it) }
                    batchReferenceNoFound = batchReferenceNo
                    referenceNoFound = workPlanScheduled.referenceNumber
                    dateAssigned = commonDaoServices.getCurrentDate()
                    processType = if (workPlanScheduled.complaintId != null) {
                        "COMPLAINT-PLAN"
                    } else {
                        "WORK-PLAN"
                    }
                }

                createNotificationTask(
                    taskNotify,
                    applicationMapProperties.mapMsNotificationNewTask,
                    map, null, loggedInUser, ioDetails
                )
                return workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)
            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved.first))
            }
        }

    }

    @PreAuthorize("hasAuthority('MS_HOD_MODIFY') or hasAuthority('MS_RM_MODIFY')")
    fun addWorkPlanScheduleFeedBackByHOD(
        referenceNo: String,
        batchReferenceNo: String,
        body: WorkPlanFeedBackDto,
        docFile: MultipartFile? = null
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
        var complaintDetailsFound: ComplaintEntity? = null

        with(workPlanScheduled) {
            msProcessEndedOn = commonDaoServices.getCurrentDate()
            msProcessEndedStatus = map.activeStatus
            msEndProcessRemarks = body.hodFeedBackRemarks
            msProcessId = applicationMapProperties.mapMSWorkPlanInspectionMSProcessEnded
            userTaskId = null
        }
        val fileSaved = updateWorkPlanInspectionDetails(workPlanScheduled, map, loggedInUser)

        if (workPlanScheduled.complaintId != null) {
            complaintsRepo.findByIdOrNull(workPlanScheduled.complaintId)
                ?.let { cp ->
                    with(cp) {
                        msProcessEndedOn = commonDaoServices.getCurrentDate()
                        msComplaintEndedStatus = map.activeStatus
                        msProcessId = applicationMapProperties.mapMSWorkPlanInspectionMSProcessEnded
                        userTaskId = null
                    }
                    complaintDetailsFound =
                        msComplaintDaoServices.updateComplaintDetailsInDB(cp, map, loggedInUser).second
                }
        }

        when (fileSaved.first.status) {
            map.successStatus -> {
                workPlanScheduled = fileSaved.second
                val remarksDto = RemarksToAddDto()
                with(remarksDto) {
                    remarksDescription = body.hodFeedBackRemarks
                    remarksStatus = "N/A"
                    processID = workPlanScheduled.msProcessId
                    userId = loggedInUser.id
                }
                val remarksSaved = workPlanAddRemarksDetails(fileSaved.second.id, remarksDto, map, loggedInUser)
                when (remarksSaved.first.status) {
                    map.successStatus -> {

                            val compliant =
                                complaintDetailsFound?.id?.let { complaintsCustomerRepo.findByComplaintId(it) }
//                            val compliant = complaintDetailsFound?.id?.let { complaintsCustomerRepo.findByComplaintId(it) } ?: throw ExpectedDataNotFound("Missing compliant Bio Details")
                            compliant?.emailAddress?.let {
                                commonDaoServices.sendEmailWithUserEmail(it,
                                    applicationMapProperties.mapMshodFinalFeedBackNotificationEmailComplinat,
                                    complaintDetailsFound!!, map, remarksSaved.first,
                                    docFile?.let { doc -> commonDaoServices.convertMultipartFileToFile(doc)?.absolutePath })
                            }
                            val ioDetails = workPlanScheduled.officerId?.let { commonDaoServices.findUserByID(it) }
                            val scheduleEmailDetails = WorkPlanScheduledDTO()
                            with(scheduleEmailDetails) {
                                baseUrl = applicationMapProperties.baseUrlValue
                                fullName = ioDetails?.let { commonDaoServices.concatenateName(it) }
                                refNumber = referenceNo
                                batchRefNumber = batchReferenceNo
                                remarksDetails = body.hodFeedBackRemarks
                                yearCodeName = batchDetails.yearNameId?.yearName
                                dateSubmitted = commonDaoServices.getCurrentDate()

                            }
                            ioDetails?.email?.let {
                                commonDaoServices.sendEmailWithUserEmail(
                                    it,
                                    applicationMapProperties.mapMsHodFinalFeedBackNotificationEmail,
                                    scheduleEmailDetails,
                                    map,
                                    remarksSaved.first
                                )
                            }
                            var taskNotify = NotificationBodyDto().apply {
                                fromName = commonDaoServices.concatenateName(loggedInUser)
                                toName = ioDetails?.let { commonDaoServices.concatenateName(it) }
                                batchReferenceNoFound = batchReferenceNo
                                referenceNoFound = workPlanScheduled.referenceNumber
                                dateAssigned = commonDaoServices.getCurrentDate()
                                processType = if (workPlanScheduled.complaintId != null) {
                                    "COMPLAINT-PLAN"
                                } else {
                                    "WORK-PLAN"
                                }
                            }

                            createNotificationTask(
                                taskNotify,
                                applicationMapProperties.mapMsNotificationNewTask,
                                map, null, loggedInUser, ioDetails
                            )
                            val hofDetails = workPlanScheduled.hofAssigned?.let { commonDaoServices.findUserByID(it) }
                            scheduleEmailDetails.fullName = hofDetails?.let { commonDaoServices.concatenateName(it) }
                            taskNotify = NotificationBodyDto().apply {
                                fromName = commonDaoServices.concatenateName(loggedInUser)
                                toName = hofDetails?.let { commonDaoServices.concatenateName(it) }
                                batchReferenceNoFound = batchReferenceNo
                                referenceNoFound = workPlanScheduled.referenceNumber
                                dateAssigned = commonDaoServices.getCurrentDate()
                                processType = if (workPlanScheduled.complaintId != null) {
                                    "COMPLAINT-PLAN"
                                } else {
                                    "WORK-PLAN"
                                }
                            }

                            createNotificationTask(
                                taskNotify,
                                applicationMapProperties.mapMsNotificationNewTask,
                                map, null, loggedInUser, hofDetails
                            )
                            hofDetails?.email?.let {
                                commonDaoServices.sendEmailWithUserEmail(
                                    it,
                                    applicationMapProperties.mapMsHodFinalFeedBackNotificationEmail,
                                    scheduleEmailDetails,
                                    map,
                                    remarksSaved.first
                                )
                            }
                            val directorDetails =
                                workPlanScheduled.directorAssigned?.let { commonDaoServices.findUserByID(it) }
                            scheduleEmailDetails.fullName =
                                directorDetails?.let { commonDaoServices.concatenateName(it) }
                            taskNotify = NotificationBodyDto().apply {
                                fromName = commonDaoServices.concatenateName(loggedInUser)
                                toName = directorDetails?.let { commonDaoServices.concatenateName(it) }
                                batchReferenceNoFound = batchReferenceNo
                                referenceNoFound = workPlanScheduled.referenceNumber
                                dateAssigned = commonDaoServices.getCurrentDate()
                                processType = if (workPlanScheduled.complaintId != null) {
                                    "COMPLAINT-PLAN"
                                } else {
                                    "WORK-PLAN"
                                }
                            }

                            createNotificationTask(
                                taskNotify,
                                applicationMapProperties.mapMsNotificationNewTask,
                                map, null, loggedInUser, directorDetails
                            )
                            directorDetails?.email?.let {
                                commonDaoServices.sendEmailWithUserEmail(
                                    it,
                                    applicationMapProperties.mapMsHodFinalFeedBackNotificationEmail,
                                    scheduleEmailDetails,
                                    map,
                                    remarksSaved.first
                                )
                            }
                            scheduleEmailDetails.fullName = workPlanScheduled.destructionClientFullName
                            workPlanScheduled.destructionClientEmail?.let {
                                commonDaoServices.sendEmailWithUserEmail(
                                    it,
                                    applicationMapProperties.mapMsHodFinalFeedBackNotificationEmail,
                                    scheduleEmailDetails,
                                    map,
                                    remarksSaved.first
                                )
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


    @PreAuthorize("hasAuthority('MS_DIRECTOR_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun addWorkPlanScheduleFeedBackByDirector(
        productReferenceNo: String,
        referenceNo: String,
        batchReferenceNo: String,
        body: WorkPlanFeedBackDto
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        val workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        val workPlanProduct = findWorkPlanProductByReferenceNumber(productReferenceNo)
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)

        with(workPlanProduct) {
            directorRecommendationStatus = map.activeStatus
            directorRecommendationRemarks = body.hodFeedBackRemarks

        }
        val fileSaved = updateWorkPlanProductDetails(workPlanProduct, map, loggedInUser)

        when (fileSaved.first.status) {
            map.successStatus -> {
                val remarksDto = RemarksToAddDto()
                with(remarksDto) {
                    remarksDescription = body.hodFeedBackRemarks
                    remarksStatus = "N/A"
                    processID = workPlanScheduled.msProcessId
                    userId = loggedInUser.id
                }
                val remarksSaved = workPlanAddRemarksDetails(workPlanScheduled.id, remarksDto, map, loggedInUser)
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

    //    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
//    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun updateWorkPlanScheduleInspectionDetailsFinalPreliminaryReport(
        referenceNo: String,
        batchReferenceNo: String,
        uploadDocID: Long,
        officerUpdate: Boolean
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
        var workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        val preliminaryDetailsFound =
            findPreliminaryReportByWorkPlanGeneratedIDAndFinalReportStatus(workPlanScheduled.id, map.activeStatus)
        workPlanInspectionDetailsPreliminaryReport(
            workPlanScheduled,
            map,
            loggedInUser,
            null,
            preliminaryDetailsFound?.id,
            true
        )
        if (officerUpdate) {
            with(workPlanScheduled) {
                if (finalReportGenerated == map.activeStatus) {
                    resubmitStatus = map.activeStatus
                    updatedStatus = map.activeStatus
                }
                latestFinalPreliminaryReport = uploadDocID
                reportPendingReview = map.activeStatus
                finalReportGenerated = map.activeStatus
                msProcessId = applicationMapProperties.mapMSWorkPlanInspectionGenerateFinalPreliminaryReport
                userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameHof
            }

            val fileSaved = updateWorkPlanInspectionDetails(workPlanScheduled, map, loggedInUser)

            when (fileSaved.first.status) {
                map.successStatus -> {
                    workPlanScheduled = fileSaved.second
                        val hofDetails = workPlanScheduled.hofAssigned?.let { commonDaoServices.findUserByID(it) }
                        val scheduleEmailDetails = WorkPlanScheduledDTO()
                        with(scheduleEmailDetails) {
                            baseUrl = applicationMapProperties.baseUrlValue
                            fullName = hofDetails?.let { commonDaoServices.concatenateName(it) }
                            refNumber = referenceNo
                            batchRefNumber = batchReferenceNo
                            yearCodeName = batchDetails.yearNameId?.yearName
                            dateSubmitted = commonDaoServices.getCurrentDate()

                        }
                        if (hofDetails != null) {
                            commonDaoServices.sendEmailWithUserEntity(
                                hofDetails,
                                applicationMapProperties.mapMsWorkPlanFinalPreliminarySubmitedApprovalEmailHOF,
                                scheduleEmailDetails,
                                map,
                                fileSaved.first
                            )
                        }


                }
                else -> {
                    throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved.first))
                }
            }
        } else {
            with(workPlanScheduled) {
                latestFinalPreliminaryReport = uploadDocID
            }

            workPlanScheduled = updateWorkPlanInspectionDetails(workPlanScheduled, map, loggedInUser).second
        }

        return workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)

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
        val workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
        val dataReportFileSaved = workPlanInspectionDetailsAddDataReport(body, workPlanScheduled, map, loggedInUser)

        when (dataReportFileSaved.first.status) {
            map.successStatus -> {
                val dataReportParamList = dataReportFileSaved.second.id.let { findDataReportParamsByDataReportID(it) }
                dataReportParamList?.forEach { paramRemove ->
                    val result: DataReportParamsDto? = body.productsList?.find { actor -> actor.id == paramRemove.id }
                    if (result == null) {
                        dataReportParameterRepo.deleteById(paramRemove.id)
                    }
                }

                body.productsList?.forEach { param ->
                    workPlanInspectionDetailsAddDataReportParams(param, dataReportFileSaved.second, map, loggedInUser)
                }
                return workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)
            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(dataReportFileSaved.first))
            }
        }
    }

    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun endAddingWorkPlanInspectionDetailsDataReport(
        referenceNo: String,
        batchReferenceNo: String
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)

        with(workPlanScheduled) {
            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
            dataReportStatus = map.activeStatus
            dataReportDate = commonDaoServices.getCurrentDate()
        }
        val fileSaved2 = updateWorkPlanInspectionDetails(workPlanScheduled, map, loggedInUser)
        when (fileSaved2.first.status) {
            map.successStatus -> {
                workPlanScheduled = fileSaved2.second
                return workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)
            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved2.first))
            }
        }
    }

    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun addWorkPlanInspectionDetailsSeizureDeclaration(
        referenceNo: String,
        batchReferenceNo: String,
        body: SeizureDto
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        val workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
        val productSized = workPlanInspectionDetailsAddSeizureDeclaration(body, workPlanScheduled, map, loggedInUser)
        when (productSized.first.status) {
            map.successStatus -> {
                return workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)
            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(productSized.first))
            }
        }
    }

    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun addWorkPlanInspectionDetailsEndSeizureDeclaration(
        referenceNo: String,
        batchReferenceNo: String
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
        with(workPlanScheduled) {
            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
//                    msProcessId = applicationMapProperties.mapMSRapidTest
            seizureDeclarationStatus = map.activeStatus
        }
        workPlanScheduled = updateWorkPlanInspectionDetails(workPlanScheduled, map, loggedInUser).second
        return workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)
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
        val dataFileSaved =
            workPlanInspectionDetailsAddInspectionInvestigationReport(body, workPlanScheduled, map, loggedInUser)
        val remarksDto = RemarksToAddDto()
        with(remarksDto) {
            remarksDescription = body.remarks
            remarksStatus = "N/A"
            processID = workPlanScheduled.msProcessId
            userId = loggedInUser.id
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
                        val remarksSaved =
                            workPlanAddRemarksDetails(fileSaved2.second.id, remarksDto, map, loggedInUser)
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
        val savedSampleCollection =
            msFuelDaoServices.addSampleCollectAdd(body, null, workPlanScheduled, map, loggedInUser)

        when (savedSampleCollection.first.status) {
            map.successStatus -> {
                body.productsList?.forEach { param ->
                    msFuelDaoServices.addSampleCollectParamAdd(param, savedSampleCollection.second, map, loggedInUser)
                }
                with(workPlanScheduled) {
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
        val savedSampleSubmission = msFuelDaoServices.addSampleSubmissionAdd(body, null, workPlanScheduled, sampleCollected, map, loggedInUser)

        when (savedSampleSubmission.first.status) {
            map.successStatus -> {
                val sampleSubmittedParamList = savedSampleSubmission.second.id.let {
                    msFuelDaoServices.findAllSampleSubmissionParametersBasedOnSampleSubmissionID(it)
                }
                sampleSubmittedParamList?.forEach { paramRemove ->
                    val result: SampleSubmissionItemsDto? =
                        body.parametersList?.find { actor -> actor.id == paramRemove.id }
                    if (result == null) {
                        sampleSubmitParameterRepo.deleteById(paramRemove.id)
                    }
                }

                body.parametersList?.forEach { param ->
                    msFuelDaoServices.addSampleSubmissionParamAdd(
                        param,
                        savedSampleSubmission.second,
                        map,
                        loggedInUser
                    )
                }
                with(workPlanScheduled) {
                    userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
//                    msProcessId = applicationMapProperties.mapMSWorkPlanInspectionAddBsNumber
//                    sampleSubmittedStatus = map.activeStatus
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
    fun addWorkPlanInspectionDetailsEndSampleSubmission(
        referenceNo: String,
        batchReferenceNo: String
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)

        with(workPlanScheduled) {
            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
//                    msProcessId = applicationMapProperties.mapMSWorkPlanInspectionAddBsNumber
            sampleSubmittedStatus = map.activeStatus
        }
        workPlanScheduled = updateWorkPlanInspectionDetails(workPlanScheduled, map, loggedInUser).second
        return workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)

    }

    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun addWorkPlanInspectionDetailsPreliminaryReport(
        referenceNo: String,
        batchReferenceNo: String,
        body: InspectionInvestigationReportDto
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
        val dataFileSaved = workPlanInspectionDetailsAddPreliminaryReport(body, workPlanScheduled, map, loggedInUser)
        val preliminaryDetailsFound = findPreliminaryReportByWorkPlanGeneratedIDAndFinalReportStatus(workPlanScheduled.id, map.inactiveStatus)

        if (dataFileSaved.first.status == map.successStatus) {
            when (workPlanScheduled.onsiteEndStatus) {
                map.activeStatus -> {
                    workPlanInspectionDetailsPreliminaryReport(
                        workPlanScheduled,
                        map,
                        loggedInUser,
                        dataFileSaved.second.id ?: throw ExpectedDataNotFound("MISSING PROGRESS REPORT ID"),
                        preliminaryDetailsFound?.id,
                        false
                    )
                    with(workPlanScheduled) {
                        latestPreliminaryReport = dataFileSaved.second.id
                        timelineStartDate = commonDaoServices.getCurrentDate()
                        timelineEndDate =
                            applicationMapProperties.mapMSWorkPlanInspectionPreliminaryReportGenerated.let { timeLine ->
                                findProcessNameByID(timeLine, 1).timelinesDay
                            }?.let { daysCount ->
                                commonDaoServices.addDaysSkippingWeekends(LocalDate.now(), daysCount)
                                    ?.let { daysConvert -> commonDaoServices.localDateToTimestamp(daysConvert) }
                            }
                        userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameHof
                        msProcessId = applicationMapProperties.mapMSWorkPlanInspectionPreliminaryReportGenerated
                        if (msPreliminaryReportStatus == map.activeStatus) {
                            resubmitStatus = map.activeStatus
                            updatedStatus = map.activeStatus
                        }
                        reportPendingReview = map.activeStatus
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
                    with(remarksDto) {
                        remarksDescription = body.remarks
                        remarksStatus = "N/A"
                        processID = workPlanScheduled.msProcessId
                        userId = loggedInUser.id
                    }
                    val remarksSaved = workPlanAddRemarksDetails(fileSaved2.second.id, remarksDto, map, loggedInUser)
                    when (remarksSaved.first.status) {
                        map.successStatus -> {
                            val hofList = commonDaoServices.findOfficersListBasedOnRole(
                                applicationMapProperties.mapMSComplaintWorkPlanMappedHOFROLEID,
                                workPlanScheduled.region ?: throw ExpectedDataNotFound("MISSING WORK-PLAN REGION ID")
                            )

                            GlobalScope.launch(Dispatchers.IO) {
                                delay(100L)
                                hofList
                                    ?.forEach { mp ->
                                        val taskNotify = NotificationBodyDto().apply {
                                            fromName = commonDaoServices.concatenateName(loggedInUser)
                                            toName = commonDaoServices.concatenateName(mp)
                                            batchReferenceNoFound = batchReferenceNo
                                            referenceNoFound = workPlanScheduled.referenceNumber
                                            dateAssigned = commonDaoServices.getCurrentDate()
                                            if (workPlanScheduled.complaintId != null) {
                                                processType = "COMPLAINT-PLAN"
                                            } else {
                                                processType = "WORK-PLAN"
                                            }
                                        }

                                        createNotificationTask(
                                            taskNotify,
                                            applicationMapProperties.mapMsNotificationNewTask,
                                            map, null, loggedInUser, mp
                                        )
                                        val scheduleEmailDetails = WorkPlanScheduledDTO()
                                        with(scheduleEmailDetails) {
                                            baseUrl = applicationMapProperties.baseUrlValue
                                            fullName = commonDaoServices.concatenateName(mp)
                                            refNumber = referenceNo
                                            batchRefNumber = batchReferenceNo
                                            yearCodeName = batchDetails.yearNameId?.yearName
                                            dateSubmitted = commonDaoServices.getCurrentDate()

                                        }
                                        commonDaoServices.sendEmailWithUserEntity(
                                            mp,
                                            applicationMapProperties.mapMsWorkPlanPreliminarySubmitedApprovalEmailHOF,
                                            scheduleEmailDetails,
                                            map,
                                            remarksSaved.first
                                        )
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
        } else {
            throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(dataFileSaved.first))
        }
    }


    @PreAuthorize("hasAuthority('MS_HOD_MODIFY') or hasAuthority('MS_RM_MODIFY') or hasAuthority('MS_DIRECTOR_MODIFY') or hasAuthority('MS_HOF_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun addWorkPlanInspectionDetailsPreliminaryReportHodHofDirecterVersions(
        referenceNo: String,
        batchReferenceNo: String,
        body: InspectionInvestigationReportDto
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
        val dataFileSaved = workPlanInspectionDetailsAddPreliminaryReport(body, workPlanScheduled, map, loggedInUser)
        val preliminaryDetailsFound =
            findPreliminaryReportByWorkPlanGeneratedIDAndFinalReportStatus(workPlanScheduled.id, map.inactiveStatus)

        if (dataFileSaved.first.status == map.successStatus) {
            workPlanInspectionDetailsPreliminaryReport(
                workPlanScheduled,
                map,
                loggedInUser,
                dataFileSaved.second.id ?: throw ExpectedDataNotFound("MISSING PROGRESS REPORT ID"),
                preliminaryDetailsFound?.id,
                false
            )
            with(workPlanScheduled) {
                latestPreliminaryReport = dataFileSaved.second.id
            }
            val fileSaved2 = updateWorkPlanInspectionDetails(workPlanScheduled, map, loggedInUser)
            when (fileSaved2.first.status) {
                map.successStatus -> {
                    workPlanScheduled = fileSaved2.second
                    val remarksDto = RemarksToAddDto()
                    with(remarksDto) {
                        remarksDescription = body.remarks
                        remarksStatus = "N/A"
                        processID = workPlanScheduled.msProcessId
                        userId = loggedInUser.id
                    }
                    val remarksSaved = workPlanAddRemarksDetails(fileSaved2.second.id, remarksDto, map, loggedInUser)
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
        } else {
            throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(dataFileSaved.first))
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
        val workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
        val sampleSubmission = findSampleSubmissionDetailByWorkPlanGeneratedIDAndSSFID(workPlanScheduled.id, body.ssfID?:throw ExpectedDataNotFound("MISSING SSF ID"))
            ?: throw ExpectedDataNotFound("MISSING SAMPLE SUBMITTED FOR WORK-PLAN SCHEDULED WITH REF NO $referenceNo")
        sampleSubmissionLabRepo.findByBsNumber(body.bsNumber.uppercase())
            ?.let {
                throw ExpectedDataNotFound("BS NUMBER ALREADY EXIST")
            } ?: kotlin.run {
            sampleSubmitRepo.findByBsNumber(body.bsNumber.uppercase())
                ?.let {
                    throw ExpectedDataNotFound("BS NUMBER ALREADY EXIST")
                } ?: kotlin.run {
                with(sampleSubmission) {
                    bsNumber = body.bsNumber.uppercase()
                    sampleReferences = body.bsNumber.uppercase()
                    sampleBsNumberDate = body.submittedDate
                    sampleBsNumberRemarks = body.remarks
                    labResultsStatus = map.inactiveStatus
                }
                val updatedSampleSubmission = msFuelDaoServices.sampleSubmissionUpdateDetails(sampleSubmission, map, loggedInUser)
                val remarksDto = RemarksToAddDto()
                with(remarksDto) {
                    remarksDescription = body.remarks
                    remarksStatus = "N/A"
                    processID = workPlanScheduled.msProcessId
                    userId = loggedInUser.id
                }

                if (updatedSampleSubmission.first.status == map.successStatus) {
                    val remarksSaved = workPlanAddRemarksDetails(workPlanScheduled.id, remarksDto, map, loggedInUser)
                    if (remarksSaved.first.status == map.successStatus) {
                        return workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)
                    } else {
                        throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(remarksSaved.first))
                    }
                } else {
                    throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(updatedSampleSubmission.first))
                }
            }
        }

    }


    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun addWorkPlanInspectionDetailsSSFEndBSNumberAdding(
        referenceNoPassed: String,
        batchReferenceNo: String
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNoPassed)
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
        val sampleSubmission = findSampleSubmissionDetailByWorkPlanGeneratedID(workPlanScheduled.id)
            ?: throw ExpectedDataNotFound("MISSING SAMPLE(S) SUBMITTED FOR WORK-PLAN SCHEDULED WITH REF NO $referenceNoPassed")
        sampleSubmission.forEach { spb ->
            msFuelDaoServices.ssfSaveBSNumber(spb, null, workPlanScheduled, loggedInUser, map)
            val productsSave = WorkPlanProductsEntity()
            with(productsSave) {
                referenceNo = "PRODUCT#${
                    generateRandomText(
                        map.transactionRefLength,
                        map.secureRandom,
                        map.messageDigestAlgorithm,
                        true
                    )
                }".toUpperCase()
                productName = spb.nameProduct
                productBrand = spb.lbIdTradeMark
                ssfId = spb.id
                workPlanId = workPlanScheduled.id
                createdBy = commonDaoServices.concatenateName(loggedInUser)
                createdOn = commonDaoServices.getTimestamp()
            }
            workPlanProductsRepo.save(productsSave)
        }
        with(workPlanScheduled) {
            bsNumberStatus = 1
            bsNumberDatedAdded = commonDaoServices.getCurrentDate()
            timelineStartDate = commonDaoServices.getCurrentDate()
            timelineEndDate = applicationMapProperties.mapMSWorkPlanInspectionBsNumberAdded.let { timeLine ->
                findProcessNameByID(timeLine, 1).timelinesDay
            }?.let { daysCount ->
                commonDaoServices.addDaysSkippingWeekends(LocalDate.now(), daysCount)
                    ?.let { daysConvert -> commonDaoServices.localDateToTimestamp(daysConvert) }
            }
            msProcessId = applicationMapProperties.mapMSWorkPlanInspectionBsNumberAdded
            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
        }
        workPlanScheduled = updateWorkPlanInspectionDetails(workPlanScheduled, map, loggedInUser).second

        return workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)
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
        val savedPDFLabResultFile =
            msFuelDaoServices.addInspectionSaveLIMSPDFSelected(fileContent, body, false, map, loggedInUser)

        when (savedPDFLabResultFile.first.status) {
            map.successStatus -> {
                with(workPlanScheduled) {
//                    msProcessId = applicationMapProperties.mapMSWorkPlanInspectionLabResultsPDFSave
                    userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
                }
                workPlanScheduled = updateWorkPlanInspectionDetails(workPlanScheduled, map, loggedInUser).second
                val remarksDto = RemarksToAddDto()
                with(remarksDto) {
                    remarksDescription = body.complianceRemarks
                    remarksStatus = "N/A"
                    processID = workPlanScheduled.msProcessId
                    userId = loggedInUser.id
                }
                val remarksSaved = workPlanAddRemarksDetails(workPlanScheduled.id, remarksDto, map, loggedInUser)
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
        val workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
        val savedSSfComplianceStatus = msFuelDaoServices.ssfLabUpdateDetails(body, loggedInUser, map)

        var remarkStatusValue = "N/A"
        val remarksDto = RemarksToAddDto()
        with(remarksDto) {
            remarksDescription = body.complianceRemarks
            remarksStatus = when {
                body.complianceStatus -> {
                    "COMPLIANT"
                }
                else -> {
                    "NON-COMPLIANT"
                }
            }
            processID = workPlanScheduled.msProcessId
            userId = loggedInUser.id
        }
        if (savedSSfComplianceStatus.first.status == map.successStatus) {
            val remarksSaved = workPlanAddRemarksDetails(workPlanScheduled.id, remarksDto, map, loggedInUser)
            when (remarksSaved.first.status) {
                map.successStatus -> {
                    return workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)
                }
                else -> {
                    throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(remarksSaved.first))
                }
            }
        } else {
            throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(savedSSfComplianceStatus.first))
        }
    }

    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun sendWorkPlanInspectionDetailsSSFSavedComplianceStatusFile(
        referenceNo: String,
        batchReferenceNo: String,
        body: SSFSendingComplianceStatus
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        val workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
        var savedSSfComplianceStatus = msFuelDaoServices.findSampleSubmittedBYID(body.ssfID ?: throw ExpectedDataNotFound("Missing SSF ID"))
        with(savedSSfComplianceStatus) {
            resultSentDate = commonDaoServices.getCurrentDate()
            resultsSent = map.activeStatus
            varField1 = body.failedParameters
        }

        savedSSfComplianceStatus = sampleSubmissionLabRepo.save(savedSSfComplianceStatus)
        val sampleFound = sampleSubmitRepo.findByBsNumber(
            savedSSfComplianceStatus.bsNumber?.uppercase() ?: throw ExpectedDataNotFound("MISSING LBS NUMBER")
        )
        val labReportSentStatus = false
        msFuelDaoServices.findSampleSubmittedListPdfBYSSFid(
            savedSSfComplianceStatus.id ?: throw ExpectedDataNotFound("Missing SSF ID")
        )
            ?.forEach { saveSSFPdf ->
                val fileUploaded = msFuelDaoServices.findUploadedFileBYId(
                    saveSSFPdf.msPdfSavedId ?: throw ExpectedDataNotFound("MISSING LAB REPORT FILE ID STATUS")
                )
                val fileContent = limsServices.mainFunctionLimsGetPDF(
                    savedSSfComplianceStatus.bsNumber ?: throw ExpectedDataNotFound("MISSING LBS NUMBER"),
                    saveSSFPdf.pdfName ?: throw ExpectedDataNotFound("MISSING FILE NAME")
                )

                val dataValue = FuelScheduledLabResultsDTO()
                with(dataValue) {
                    sourceProductEvidence = dataReportRepo.findByIdOrNull(sampleFound?.dataReportID?: -1L)?.outletName
                    failedResults = savedSSfComplianceStatus.failedParameters
                    standardName = sampleFound?.referencesStandards
                    baseUrl = applicationMapProperties.baseUrlValue
                    refNumber = savedSSfComplianceStatus.bsNumber
                    compliantDetails = mapCompliantStatusDto(savedSSfComplianceStatus, map)
                }

                val complianceStatus = savedSSfComplianceStatus.resultsAnalysis == 1
                var sr = commonDaoServices.createServiceRequest(map)
                if (complianceStatus) {
                        body.outLetEmail?.let {
                            commonDaoServices.sendEmailWithUserEmail(
                                it,
                                applicationMapProperties.mapMsWorkPlanInspectionLabResultsCompliantNotification,
                                dataValue,
                                map,
                                sr,
                                fileContent.path
                            )
                        }
                        body.manufactureEmail?.let {
                            commonDaoServices.sendEmailWithUserEmail(
                                it,
                                applicationMapProperties.mapMsWorkPlanInspectionLabResultsCompliantNotification,
                                dataValue,
                                map,
                                sr,
                                fileContent.path
                            )
                        }
                        body.complainantEmail?.let {
                            commonDaoServices.sendEmailWithUserEmail(
                                it,
                                applicationMapProperties.mapMsWorkPlanInspectionLabResultsCompliantNotification,
                                dataValue,
                                map,
                                sr,
                                fileContent.path
                            )
                        }

                } else {
                        body.outLetEmail?.let {
                            commonDaoServices.sendEmailWithUserEmail(
                                it,
                                applicationMapProperties.mapMsWorkPlanInspectionLabResultsNotCompliantNotification,
                                dataValue,
                                map,
                                sr,
                                fileContent.path
                            )
                        }
                        body.manufactureEmail?.let {
                            commonDaoServices.sendEmailWithUserEmail(
                                it,
                                applicationMapProperties.mapMsWorkPlanInspectionLabResultsNotCompliantNotification,
                                dataValue,
                                map,
                                sr,
                                fileContent.path
                            )
                        }
                        body.complainantEmail?.let {
                            commonDaoServices.sendEmailWithUserEmail(
                                it,
                                applicationMapProperties.mapMsWorkPlanInspectionLabResultsNotCompliantNotification,
                                dataValue,
                                map,
                                sr,
                                fileContent.path
                            )
                        }

                }

            }
        return workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)

    }

    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun NotSendWorkPlanInspectionDetailsSSFSavedComplianceStatusFile(
        referenceNo: String,
        batchReferenceNo: String,
        body: SSFSendingComplianceStatus
    ): WorkPlanInspectionDto {
        val map = commonDaoServices.serviceMapDetails(appId)
        val workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
        var savedSSfComplianceStatus = msFuelDaoServices.findSampleSubmittedBYID(body.ssfID ?: throw ExpectedDataNotFound("Missing SSF ID"))
        with(savedSSfComplianceStatus) {
//            resultSentDate = commonDaoServices.getCurrentDate()
            resultsSent = map.activeStatus
//            varField1 = body.failedParameters
        }

        savedSSfComplianceStatus = sampleSubmissionLabRepo.save(savedSSfComplianceStatus)

        return workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)

    }

    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun addWorkPlanInspectionDetailsSSFFinalSaveComplianceStatus(
        referenceNo: String,
        batchReferenceNo: String,
        body: SSFSaveFinalComplianceStatusDto
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
//        val savedSSfComplianceStatus = msFuelDaoServices.ssfLabUpdateDetails(body,loggedInUser,map)

        var remarkStatusValue = "N/A"

//        if (savedSSfComplianceStatus.first.status == map.successStatus) {
        with(workPlanScheduled) {
            when {
                body.complianceStatus -> {
                    compliantStatus = 1
                    workPlanCompliantStatus = 1
                    notCompliantStatus = 0
                    compliantStatusDate = commonDaoServices.getCurrentDate()
                    compliantStatusBy = commonDaoServices.concatenateName(loggedInUser)
                    compliantStatusRemarks = body.complianceRemarks
                    totalCompliance = body.totalCompliance
                    remarkStatusValue = "COMPLIANT"
                }
                else -> {
                    workPlanCompliantStatus = 0
                    notCompliantStatus = 0
                    compliantStatus = 0
                    notCompliantStatusDate = commonDaoServices.getCurrentDate()
                    notCompliantStatusBy = commonDaoServices.concatenateName(loggedInUser)
                    notCompliantStatusRemarks = body.complianceRemarks
                    totalCompliance = body.totalCompliance
                    remarkStatusValue = "NOT-COMPLIANT"
                }
            }
        }
        with(workPlanScheduled) {
            timelineStartDate = commonDaoServices.getCurrentDate()
            timelineEndDate = applicationMapProperties.mapMSWorkPlanInspectionLabResultsAnalysed.let { timeLine ->
                findProcessNameByID(timeLine, 1).timelinesDay
            }?.let { daysCount ->
                commonDaoServices.addDaysSkippingWeekends(LocalDate.now(), daysCount)
                    ?.let { daysConvert -> commonDaoServices.localDateToTimestamp(daysConvert) }
            }
            msProcessId = applicationMapProperties.mapMSWorkPlanInspectionLabResultsAnalysed
            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
        }
        workPlanScheduled = updateWorkPlanInspectionDetails(workPlanScheduled, map, loggedInUser).second
        val remarksDto = RemarksToAddDto()
        with(remarksDto) {
            remarksDescription = body.complianceRemarks
            processID = workPlanScheduled.msProcessId
            userId = loggedInUser.id
        }

        remarksDto.remarksStatus = remarkStatusValue
        val remarksSaved = workPlanAddRemarksDetails(workPlanScheduled.id, remarksDto, map, loggedInUser)
        when (remarksSaved.first.status) {
            map.successStatus -> {
                return workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)
            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(remarksSaved.first))
            }
        }

//        }
//        else {
//            throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(savedSSfComplianceStatus.first))
//        }
    }


    @PreAuthorize("hasAuthority('MS_IO_READ') or hasAuthority('MS_HOD_READ') or hasAuthority('MS_RM_READ') or hasAuthority('MS_HOF_READ') or hasAuthority('MS_DIRECTOR_READ')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun getWorkPlanScheduleInspectionDetailsBasedOnRefNo(
        referenceNo: String,
        batchReferenceNo: String
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        val workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
//        val fuelInspectionOfficer = findFuelInspectionOfficerAssigned(workPlanScheduled, map.activeStatus)

        return workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)
    }

    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun startWorkPlanScheduleInspectionOnsiteDetailsBasedOnRefNo(
        referenceNo: String,
        batchReferenceNo: String,
        body: WorkPlanScheduleOnsiteDto
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)

        with(workPlanScheduled) {
            onsiteStartStatus = map.activeStatus
            onsiteEndStatus = map.inactiveStatus
            onsiteStartDate = commonDaoServices.getCurrentDate()
            onsiteStartDateAdded = body.startDate
            onsiteEndDateAdded = body.endDate
            numberOfDays = body.numberOfDays
            timelineStartDate = commonDaoServices.getCurrentDate()
            timelineEndDate = applicationMapProperties.mapMSWorkPlanInspectionStartOnSiteActivities.let { timeLine ->
                findProcessNameByID(timeLine, 1).timelinesDay
            }?.let { daysCount ->
                commonDaoServices.addDaysSkippingWeekends(LocalDate.now(), daysCount)
                    ?.let { daysConvert -> commonDaoServices.localDateToTimestamp(daysConvert) }
            }
            msProcessId = applicationMapProperties.mapMSWorkPlanInspectionStartOnSiteActivities
            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
        }
        workPlanScheduled = updateWorkPlanInspectionDetails(workPlanScheduled, map, loggedInUser).second
        return workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)
    }

    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun endWorkPlanScheduleInspectionOnsiteDetailsBasedOnRefNo(
        referenceNo: String,
        batchReferenceNo: String
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)

        with(workPlanScheduled) {
            sendSffStatus = map.activeStatus
            onsiteEndStatus = map.activeStatus
            onsiteEndDate = commonDaoServices.getCurrentDate()
            onsiteTat = onsiteEndDateAdded?.let { commonDaoServices.getCalculatedDaysInLong(onsiteEndDate!!, it) }
            sendSffDate = commonDaoServices.getCurrentDate()
            timelineStartDate = commonDaoServices.getCurrentDate()
            timelineEndDate = applicationMapProperties.mapMSWorkPlanInspectionEndOnSiteActivities.let { timeLine ->
                findProcessNameByID(timeLine, 1).timelinesDay
            }?.let { daysCount ->
                commonDaoServices.addDaysSkippingWeekends(LocalDate.now(), daysCount)
                    ?.let { daysConvert -> commonDaoServices.localDateToTimestamp(daysConvert) }
            }
            msProcessId = applicationMapProperties.mapMSWorkPlanInspectionEndOnSiteActivities
            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
        }
        workPlanScheduled = updateWorkPlanInspectionDetails(workPlanScheduled, map, loggedInUser).second
        return workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)
    }

    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun endWorkPlanScheduleInspectionAllRecommendationDoneDetailsBasedOnRefNo(
        referenceNo: String,
        batchReferenceNo: String
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)

        with(workPlanScheduled) {
            recommendationDoneStatus = map.activeStatus
            timelineStartDate = commonDaoServices.getCurrentDate()
            timelineEndDate = applicationMapProperties.mapMSWorkPlanInspectionRecommendationDoneMSIO.let { timeLine ->
                findProcessNameByID(timeLine, 1).timelinesDay
            }?.let { daysCount ->
                commonDaoServices.addDaysSkippingWeekends(LocalDate.now(), daysCount)
                    ?.let { daysConvert -> commonDaoServices.localDateToTimestamp(daysConvert) }
            }
            msProcessId = applicationMapProperties.mapMSWorkPlanInspectionRecommendationDoneMSIO
            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameHodRm
        }
        workPlanScheduled = updateWorkPlanInspectionDetails(workPlanScheduled, map, loggedInUser).second
        return workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)
    }

    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun createNewWorkPlanSchedule(
        body: WorkPlanEntityDto,
        referenceNumber: String,
        page: PageRequest
    ): WorkPlanScheduleListDetailsDto {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val msType = findMsTypeDetailsWithUuid(applicationMapProperties.mapMsWorkPlanTypeUuid)
        val batchDetail = findCreatedWorkPlanWIthRefNumber(referenceNumber)

        val fileSaved = saveNewWorkPlanActivity(body, msType, batchDetail, map, loggedInUser)
        when (fileSaved.first.status) {
            map.successStatus -> {
                body.workPlanCountiesTowns?.forEach { param ->
                    workPlanInspectionDetailsAddCountiesTowns(param, fileSaved.second, map, loggedInUser)
                }
                val workPlanList = findALlWorkPlanDetailsAssociatedWithWorkPlanID(batchDetail.id, page).toList()
                return mapWorkPlanInspectionListDto(workPlanList, mapWorkPlanBatchDetailsDto(batchDetail, map))
            }
            else -> {
                throw ExpectedDataNotFound(
                    commonDaoServices.failedStatusDetails(
                        fileSaved.first ?: throw ExpectedDataNotFound("Missing WorkPlan Details To save")
                    )
                )
            }
        }

    }

    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun updateNewWorkPlanSchedule(
        body: WorkPlanEntityDto,
        submitForApproval: Boolean,
        batchReferenceNo: String,
        referenceNo: String,
        page: PageRequest
    ): WorkPlanInspectionDto {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val loggedInUserProfile = commonDaoServices.findUserProfileByUserID(loggedInUser, map.activeStatus)
        val batchDetails = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
        val workPlanScheduled = findWorkPlanActivityByReferenceNumber(referenceNo)
        with(workPlanScheduled) {
            complaintDepartment = body.complaintDepartment
            divisionId = body.divisionId
            nameActivity = body.nameActivity
            timeActivityDate = body.timeActivityDate
            timeActivityEndDate = body.timeActivityEndDate
            region = commonDaoServices.findUserProfileByUserID(loggedInUser).regionId?.id
            county = commonDaoServices.findUserProfileByUserID(loggedInUser).countyID?.id
            townMarketCenter =  commonDaoServices.findUserProfileByUserID(loggedInUser).townID?.id
            locationActivityOther = body.locationActivityOther
            standardCategory = body.standardCategory
            rationale = body.rationale
            scopeOfCoverage = body.scopeOfCoverage
            productString = body.productString
            resourcesRequired = body.resourcesRequired?.let { commonDaoServices.convertClassToJson(it) }
            budget = body.budget
            when (submittedForApprovalStatus) {
                map.activeStatus -> {
                    userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameHodRm
                    msProcessId = applicationMapProperties.mapMSWorkPlanInspectionResubmitWorkPlanModification
                    status = map.initStatus
                    updatedStatus = map.activeStatus
                    resubmitStatus = map.inactiveStatus
                    updatedRemarks = body.remarks
                    approvedStatus = map.inactiveStatus
                    rejectedStatus = map.inactiveStatus
                }
                else -> {
                    userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
                    msProcessId = applicationMapProperties.mapMSWorkPlanInspectionGenerateWorkPlan
                }
            }

            officerId = loggedInUser.id
            officerName = commonDaoServices.concatenateName(loggedInUser)
        }

        val fileSaved = updateWorkPlanInspectionDetails(workPlanScheduled, map, loggedInUser)

        val dataCountiesTownsList = fileSaved.second.id.let { findCountiesTownsByWorkPlanID(it) }
        dataCountiesTownsList?.forEach { paramRemove ->
            val result: WorkPlanCountyTownDto? = body.workPlanCountiesTowns?.find { actor -> actor.id == paramRemove.id }
            if (result == null) {
                workPlanCountiesTownsRepo.deleteById(paramRemove.id)
            }
        }

        body.workPlanCountiesTowns?.forEach { param ->
            workPlanInspectionDetailsAddCountiesTowns(param, fileSaved.second, map, loggedInUser)
        }

        val remarksDto = RemarksToAddDto()
        with(remarksDto) {
            remarksDescription = body.remarks
            remarksStatus = "WORK-PLAN UPDATED/CHANGED"
            processID = workPlanScheduled.msProcessId
            userId = loggedInUser.id
        }
        val remarksSaved = workPlanAddRemarksDetails(fileSaved.second.id, remarksDto, map, loggedInUser)

        when (fileSaved.first.status) {
            map.successStatus -> {
                return when {
                    submitForApproval -> {
                        workPlanUpdate(
                            batchDetails,
                            map,
                            loggedInUser,
                            workPlanScheduled,
                            loggedInUserProfile,
                            batchReferenceNo
                        )
                    }
                    else -> {
                        workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)
                    }
                }
            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved.first))
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

    fun updateWorkPlanProductDetails(
        body: WorkPlanProductsEntity,
        map: ServiceMapsEntity,
        user: UsersEntity
    ): Pair<ServiceRequestsEntity, WorkPlanProductsEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var productDetails = body
        try {
            with(productDetails) {
                modifiedBy = commonDaoServices.concatenateName(user)
                modifiedOn = commonDaoServices.getTimestamp()
            }
            productDetails = workPlanProductsRepo.save(productDetails)

            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(productDetails)}"
            sr.names = "Work Plan Products Update file"

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = map.successStatus
            sr = serviceRequestsRepo.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(body)}"
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepo.save(sr)

        }
        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return Pair(sr, productDetails)
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

    fun createNotificationTask(
        body: NotificationBodyDto,
        uuid: String,
        map: ServiceMapsEntity,
        userFrom: String?,
        userFromDB: UsersEntity,
        userSendTo: UsersEntity?
    ): Pair<ServiceRequestsEntity, MsTaskNotificationsEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        val mapNotifications = notificationsRepo.findByUuid(uuid)
        var taskNotify = MsTaskNotificationsEntity()
        try {
            with(taskNotify) {
                taskRefNumber =
                    "TASK#${generateRandomText(3, map.secureRandom, map.messageDigestAlgorithm, true)}".toUpperCase()
                body.taskRefNumber = taskRefNumber
                notificationType = mapNotifications?.notificationType?.typeCode
                notificationName = mapNotifications?.notificationType?.description
                notificationMsg = mapNotifications?.let { commonDaoServices.composeMessage(body, it) }
                notificationBody = commonDaoServices.convertClassToJson(body)
                if (userFrom == null) {
                    fromUserId = userFromDB.id
                }
                toUserId = userSendTo?.id
                readStatus = map.inactiveStatus
                createdBy = userFrom ?: commonDaoServices.concatenateName(userFromDB)

                createdOn = commonDaoServices.getTimestamp()
            }
            taskNotify = msTaskNotificationsRepo.save(taskNotify)

            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(taskNotify)}"
            sr.names = "Notification Details"

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
        return Pair(sr, taskNotify)
    }

    fun checkAllOverdueTasks(map: ServiceMapsEntity): Pair<ServiceRequestsEntity, MsTaskNotificationsEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        val mapNotifications = notificationsRepo.findByUuid(applicationMapProperties.mapMsNotificationOverDueTask)
        var taskNotify = MsTaskNotificationsEntity()
        try {
            val complaintList = complaintsRepo.findAllByMsComplaintEndedStatusOrderByIdDesc(map.inactiveStatus)
            complaintList?.forEach { cp ->
                if (cp.timelineEndDate != null) {
                    if (cp.timelineEndDate!! > commonDaoServices.getCurrentDate()) {
                        var userFrom: String? = null
                        var userFromDB: UsersEntity? = null
                        var userSendTo: UsersEntity? = null
                        when (cp.msProcessId) {
                            applicationMapProperties.msComplaintProcessOnlineSubmitted -> {
                                userFrom = cp.createdBy
                                userSendTo = cp.hodAssigned?.let { commonDaoServices.findUserByID(it) }
                            }
                            applicationMapProperties.msComplaintProcessAssignHOF -> {
                                userFromDB = cp.hodAssigned?.let { commonDaoServices.findUserByID(it) }
                                userSendTo = cp.hofAssigned?.let { commonDaoServices.findUserByID(it) }
                            }
                            applicationMapProperties.msComplaintProcessAssignOfficer -> {
                                userFromDB = cp.hofAssigned?.let { commonDaoServices.findUserByID(it) }
                                userSendTo = cp.assignedIo?.let { commonDaoServices.findUserByID(it) }
                            }
                        }

                        val task = NotificationBodyDto().apply {
                            fromName = userFrom ?: userFromDB?.let { commonDaoServices.concatenateName(it) }
                            toName = userSendTo?.let { commonDaoServices.concatenateName(it) }
                            referenceNoFound = cp.referenceNumber
                            dateAssigned = commonDaoServices.getCurrentDate()
                            processType = "COMPLAINT"
                        }

                        with(taskNotify) {
                            taskRefNumber = "TASK#${
                                generateRandomText(
                                    3,
                                    map.secureRandom,
                                    map.messageDigestAlgorithm,
                                    true
                                )
                            }".toUpperCase()
                            task.taskRefNumber = taskRefNumber
                            notificationType = mapNotifications?.notificationType?.typeCode
                            notificationName = mapNotifications?.notificationType?.description
                            notificationMsg = mapNotifications?.let { commonDaoServices.composeMessage(task, it) }
                            notificationBody = commonDaoServices.convertClassToJson(task)
                            fromUserId = userFromDB?.id
                            toUserId = userSendTo?.id
                            readStatus = map.inactiveStatus
                            createdBy = userFrom ?: userFromDB?.let { commonDaoServices.concatenateName(it) }
                            createdOn = commonDaoServices.getTimestamp()
                        }
                        taskNotify = msTaskNotificationsRepo.save(taskNotify)
                    }
                }
            }

            val workPlanDetails = generateWorkPlanRepo.findAllByMsProcessEndedStatus(map.inactiveStatus)
            workPlanDetails?.forEach { wp ->
                if (wp.timelineEndDate != null) {
                    if (wp.timelineEndDate!! > commonDaoServices.getCurrentDate()) {
                        var userFrom: String? = null
                        var userFromDB: UsersEntity? = null
                        var userSendTo: UsersEntity? = null
                        when (wp.msProcessId) {
                            applicationMapProperties.mapMSWorkPlanInspectionGenerateWorkPlan -> {
                                userFrom = wp.createdBy
                                userSendTo = wp.officerId?.let { commonDaoServices.findUserByID(it) }
                            }
                            applicationMapProperties.mapMSWorkPlanInspectionSubmittedForApproval -> {
                                userFromDB = wp.hodRmAssigned?.let { commonDaoServices.findUserByID(it) }
                                userSendTo = wp.hodRmAssigned?.let { commonDaoServices.findUserByID(it) }
                            }
                            applicationMapProperties.mapMSWorkPlanInspectionApprovedWorPlan -> {
                                userFromDB = wp.hodRmAssigned?.let { commonDaoServices.findUserByID(it) }
                                userSendTo = wp.officerId?.let { commonDaoServices.findUserByID(it) }
                            }
                            applicationMapProperties.mapMSWorkPlanInspectionRejectedWorPlan -> {
                                userFromDB = wp.hodRmAssigned?.let { commonDaoServices.findUserByID(it) }
                                userSendTo = wp.officerId?.let { commonDaoServices.findUserByID(it) }
                            }
                            applicationMapProperties.mapMSWorkPlanInspectionStartOnSiteActivities -> {
                                userFromDB = wp.officerId?.let { commonDaoServices.findUserByID(it) }
                                userSendTo = wp.officerId?.let { commonDaoServices.findUserByID(it) }
                            }
                            applicationMapProperties.mapMSWorkPlanInspectionEndOnSiteActivities -> {
                                userFromDB = wp.officerId?.let { commonDaoServices.findUserByID(it) }
                                userSendTo = wp.officerId?.let { commonDaoServices.findUserByID(it) }
                            }
                            applicationMapProperties.mapMSWorkPlanInspectionBsNumberAdded -> {
                                userFromDB = wp.officerId?.let { commonDaoServices.findUserByID(it) }
                                userSendTo = wp.officerId?.let { commonDaoServices.findUserByID(it) }
                            }
                            applicationMapProperties.mapMSWorkPlanInspectionLabResults -> {
                                userFromDB = wp.officerId?.let { commonDaoServices.findUserByID(it) }
                                userSendTo = wp.officerId?.let { commonDaoServices.findUserByID(it) }
                            }
                            applicationMapProperties.mapMSWorkPlanInspectionLabResultsAnalysed -> {
                                userFrom = "LIMS SYSTEM"
                                userSendTo = wp.officerId?.let { commonDaoServices.findUserByID(it) }
                            }
                            applicationMapProperties.mapMSWorkPlanInspectionPreliminaryReportGenerated -> {
                                userFromDB = wp.officerId?.let { commonDaoServices.findUserByID(it) }
                                userSendTo = wp.hofAssigned?.let { commonDaoServices.findUserByID(it) }
                            }
                            applicationMapProperties.mapMSWorkPlanInspectionFinalReportApprovedHOF -> {
                                userFromDB = wp.hofAssigned?.let { commonDaoServices.findUserByID(it) }
                                userSendTo = wp.hodRmAssigned?.let { commonDaoServices.findUserByID(it) }
                            }
                            applicationMapProperties.mapMSWorkPlanInspectionFinalReportRejectedHOF -> {
                                userFromDB = wp.hofAssigned?.let { commonDaoServices.findUserByID(it) }
                                userSendTo = wp.officerId?.let { commonDaoServices.findUserByID(it) }
                            }
                            applicationMapProperties.mapMSWorkPlanInspectionPreliminaryReportApprovedHOF -> {
                                userFromDB = wp.hofAssigned?.let { commonDaoServices.findUserByID(it) }
                                userSendTo = wp.hodRmAssigned?.let { commonDaoServices.findUserByID(it) }
                            }
                            applicationMapProperties.mapMSWorkPlanInspectionPreliminaryReportRejectedHOF -> {
                                userFromDB = wp.hofAssigned?.let { commonDaoServices.findUserByID(it) }
                                userSendTo = wp.officerId?.let { commonDaoServices.findUserByID(it) }
                            }
                            applicationMapProperties.mapMSWorkPlanInspectionFinalReportApprovedHODRM -> {
                                userFromDB = wp.hodRmAssigned?.let { commonDaoServices.findUserByID(it) }
                                userSendTo = wp.hodRmAssigned?.let { commonDaoServices.findUserByID(it) }
                            }
                            applicationMapProperties.mapMSWorkPlanInspectionFinalReportRejectedHODRM -> {
                                userFromDB = wp.hodRmAssigned?.let { commonDaoServices.findUserByID(it) }
                                userSendTo = wp.hofAssigned?.let { commonDaoServices.findUserByID(it) }
                            }
                            applicationMapProperties.mapMSWorkPlanInspectionPreliminaryReportApprovedHODRM -> {
                                userFromDB = wp.hodRmAssigned?.let { commonDaoServices.findUserByID(it) }
                                userSendTo = wp.officerId?.let { commonDaoServices.findUserByID(it) }
                            }
                            applicationMapProperties.mapMSWorkPlanInspectionPreliminaryReportRejectedHODRM -> {
                                userFromDB = wp.hodRmAssigned?.let { commonDaoServices.findUserByID(it) }
                                userSendTo = wp.hofAssigned?.let { commonDaoServices.findUserByID(it) }
                            }
                            applicationMapProperties.mapMSWorkPlanInspectionRecommendationsADDED -> {
                                userFromDB = wp.hodRmAssigned?.let { commonDaoServices.findUserByID(it) }
                                userSendTo = wp.directorAssigned?.let { commonDaoServices.findUserByID(it) }
                            }
                            applicationMapProperties.mapMSWorkPlanInspectionRecommendationDoneMSIO -> {
                                userFromDB = wp.officerId?.let { commonDaoServices.findUserByID(it) }
                                userSendTo = wp.hodRmAssigned?.let { commonDaoServices.findUserByID(it) }
                            }
                        }

                        val task = NotificationBodyDto().apply {
                            fromName = userFrom ?: userFromDB?.let { commonDaoServices.concatenateName(it) }
                            toName = userSendTo?.let { commonDaoServices.concatenateName(it) }
                            referenceNoFound = wp.referenceNumber
                            dateAssigned = commonDaoServices.getCurrentDate()
                            processType = "COMPLAINT"
                        }

                        with(taskNotify) {
                            taskRefNumber = "TASK#${
                                generateRandomText(
                                    3,
                                    map.secureRandom,
                                    map.messageDigestAlgorithm,
                                    true
                                )
                            }".toUpperCase()
                            task.taskRefNumber = taskRefNumber
                            notificationType = mapNotifications?.notificationType?.typeCode
                            notificationName = mapNotifications?.notificationType?.description
                            notificationMsg = mapNotifications?.let { commonDaoServices.composeMessage(task, it) }
                            notificationBody = commonDaoServices.convertClassToJson(task)
                            fromUserId = userFromDB?.id
                            toUserId = userSendTo?.id
                            readStatus = map.inactiveStatus
                            createdBy = userFrom ?: userFromDB?.let { commonDaoServices.concatenateName(it) }
                            createdOn = commonDaoServices.getTimestamp()
                        }
                        taskNotify = msTaskNotificationsRepo.save(taskNotify)
                    }
                }
            }


            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(taskNotify)}"
            sr.names = "Notification Details"

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = map.successStatus
            sr = serviceRequestsRepo.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
//            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(body)}"
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepo.save(sr)

        }
        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return Pair(sr, taskNotify)
    }

    fun updateNotificationTask(
        taskBody: MsTaskNotificationsEntity,
        map: ServiceMapsEntity,
        user: UsersEntity
    ): Pair<ServiceRequestsEntity, MsTaskNotificationsEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var taskNotify = taskBody
        try {
            with(taskNotify) {
                readStatus = map.activeStatus
                modifiedBy = commonDaoServices.concatenateName(user)
                modifiedOn = commonDaoServices.getTimestamp()
            }
            taskNotify = msTaskNotificationsRepo.save(taskNotify)

            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(taskNotify)}"
            sr.names = "Notification Details Read"

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = map.successStatus
            sr = serviceRequestsRepo.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(taskBody)}"
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepo.save(sr)

        }
        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return Pair(sr, taskNotify)
    }


    fun createWorkPlanYear(
        loggedInUser: UsersEntity,
        map: ServiceMapsEntity,
        workPlanYearCodes: WorkplanYearsCodesEntity,
        complaint: Boolean
    ): Pair<ServiceRequestsEntity, WorkPlanCreatedEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var workPlanCreated = WorkPlanCreatedEntity()
        try {
            with(workPlanCreated) {
                uuid = commonDaoServices.generateUUIDString()
                workPlanRegion = commonDaoServices.findUserProfileByUserID(loggedInUser, map.activeStatus).regionId?.id
                    ?: throw ExpectedDataNotFound("Logged IN User Is Missing Region ID")
                referenceNumber = "WORKPLAN#${
                    generateRandomText(
                        5,
                        map.secureRandom,
                        map.messageDigestAlgorithm,
                        true
                    )
                }".toUpperCase()
                yearNameId = workPlanYearCodes
                userCreatedId = loggedInUser
                when {
                    complaint -> {
                        complaintStatus = map.activeStatus
                    }
                    else -> {
                        workPlanStatus = map.activeStatus
                    }
                }
                batchClosed = map.activeStatus
                createdDate = commonDaoServices.getCurrentDate()
                createdStatus = map.activeStatus
                endedDate = commonDaoServices.getCurrentDate()
                endedStatus = map.activeStatus
                batchClosed = map.activeStatus
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

            dataReportRepo.findByIdOrNull(body.id ?: -1L)
                ?.let { updateDataReport ->
                    with(updateDataReport) {
                        docList = body.docList?.let { commonDaoServices.convertClassToJson(it) }
                        referenceNumber = body.referenceNumber
                        physicalLocation = body.physicalLocation
                        outletName = body.outletName
                        phoneNumber = body.phoneNumber
                        emailAddress = body.emailAddress
                        inspectionDate = commonDaoServices.getCurrentDate()
                        inspectorName = body.inspectorName
                        function = body.function
                        department = body.department
                        regionName = body.regionName
                        town = body.town
                        marketCenter = body.marketCenter
                        outletDetails = body.outletDetails
                        mostRecurringNonCompliant = body.mostRecurringNonCompliant
                        additionalNonComplianceDetails = body.additionalNonComplianceDetails
                        personMet = body.personMet
                        summaryFindingsActionsTaken = body.summaryFindingsActionsTaken
                        samplesDrawnAndSubmitted = body.samplesDrawnAndSubmitted
                        sourceOfProductAndEvidence = body.sourceOfProductAndEvidence
                        finalActionSeizedGoods = body.finalActionSeizedGoods
                        totalComplianceScore = body.totalComplianceScore
                        numberOfProducts = body.numberOfProducts
                        workPlanGeneratedID = workPlanScheduled.id
                        remarks = body.remarks
                        status = map.activeStatus
                        modifiedBy = commonDaoServices.concatenateName(user)
                        modifiedOn = commonDaoServices.getTimestamp()
                    }
                    saveDataReport = dataReportRepo.save(updateDataReport)

                } ?: kotlin.run {
                with(saveDataReport) {
                    docList = body.docList?.let { commonDaoServices.convertClassToJson(it) }
                    referenceNumber = body.referenceNumber
                    physicalLocation = body.physicalLocation
                    outletName = body.outletName
                    phoneNumber = body.phoneNumber
                    emailAddress = body.emailAddress
                    inspectionDate = commonDaoServices.getCurrentDate()
                    inspectorName = body.inspectorName
                    function = body.function
                    department = body.department
                    regionName = body.regionName
                    town = body.town
                    marketCenter = body.marketCenter
                    outletDetails = body.outletDetails
                    mostRecurringNonCompliant = body.mostRecurringNonCompliant
                    additionalNonComplianceDetails = body.additionalNonComplianceDetails
                    personMet = body.personMet
                    summaryFindingsActionsTaken = body.summaryFindingsActionsTaken
                    samplesDrawnAndSubmitted = body.samplesDrawnAndSubmitted
                    sourceOfProductAndEvidence = body.sourceOfProductAndEvidence
                    finalActionSeizedGoods = body.finalActionSeizedGoods
                    totalComplianceScore = body.totalComplianceScore
                    remarks = body.remarks
                    numberOfProducts = body.numberOfProducts
                    workPlanGeneratedID = workPlanScheduled.id
                    status = map.activeStatus
                    createdBy = commonDaoServices.concatenateName(user)
                    createdOn = commonDaoServices.getTimestamp()

                }
                saveDataReport = dataReportRepo.save(saveDataReport)
            }


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

    fun workPlanInspectionDetailsAddSSF(
        body: SampleSubmissionDto,
        workPlanScheduled: MsWorkPlanGeneratedEntity,
        map: ServiceMapsEntity,
        user: UsersEntity
    ): Pair<ServiceRequestsEntity, MsSampleSubmissionEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var saveSSF = MsSampleSubmissionEntity()
        try {

            sampleSubmissionRepo.findByIdOrNull(body.id ?: -1L)
                ?.let { updateSSF ->
                    with(updateSSF) {
                        docList = body.docList?.let { commonDaoServices.convertClassToJson(it) }
                        nameProduct = body.nameProduct
                        packaging = body.packaging
                        labellingIdentification = body.labellingIdentification
                        fileRefNumber = body.fileRefNumber
                        referencesStandards = body.referencesStandards
                        sizeTestSample = body.sizeTestSample
                        sizeRefSample = body.sizeRefSample
                        condition = body.condition
                        sampleReferences = body.sampleReferences
                        sendersName = body.sendersName
                        designation = body.designation
                        address = body.address
                        sendersDate = commonDaoServices.getCurrentDate()
                        receiversDate = body.receiversDate
                        receiversName = body.receiversName
                        testChargesKsh = body.testChargesKsh
                        receiptLpoNumber = body.receiptLpoNumber
                        invoiceNumber = body.invoiceNumber
                        disposal = body.disposal
                        remarks = body.remarks
                        countryOfOrigin = body.countryOfOrigin
                        sampleCollectionNumber = body.sampleCollectionNumber
                        lbIdAnyAomarking = body.lbIdAnyAomarking
                        lbIdBatchNo = body.lbIdBatchNo
                        lbIdContDecl = body.lbIdContDecl
                        lbIdDateOfManf = body.lbIdDateOfManf
                        sampleCollectionDate = commonDaoServices.getCurrentDate()
                        lbIdExpiryDate = body.lbIdExpiryDate
                        lbIdTradeMark = body.lbIdTradeMark
                        noteTransResults = body.noteTransResults
                        scfNo = body.scfNo
                        cocNumber = body.cocNumber
                        bsNumber = body.bsNumber
                        productDescription = body.productDescription
                        sourceProductEvidence = body.sourceProductEvidence
                        dataReportID = body.dataReportID
                        workPlanGeneratedID = workPlanScheduled.id
                        status = map.activeStatus
                        modifiedBy = commonDaoServices.concatenateName(user)
                        modifiedOn = commonDaoServices.getTimestamp()
                    }
                    saveSSF = sampleSubmissionRepo.save(updateSSF)

                } ?: kotlin.run {
                with(saveSSF) {
                    docList = body.docList?.let { commonDaoServices.convertClassToJson(it) }
                    nameProduct = body.nameProduct
                    packaging = body.packaging
                    labellingIdentification = body.labellingIdentification
                    fileRefNumber = "REF/SSF/${generateRandomText(5, map.secureRandom, map.messageDigestAlgorithm, true)}".toUpperCase()
                    referencesStandards = body.referencesStandards
                    sizeTestSample = body.sizeTestSample
                    sizeRefSample = body.sizeRefSample
                    condition = body.condition
                    sampleReferences = body.sampleReferences
                    sendersName = body.sendersName
                    designation = body.designation
                    address = body.address
                    sendersDate = commonDaoServices.getCurrentDate()
                    receiversDate = body.receiversDate
                    receiversName = body.receiversName
                    testChargesKsh = body.testChargesKsh
                    receiptLpoNumber = body.receiptLpoNumber
                    invoiceNumber = body.invoiceNumber
                    disposal = body.disposal
                    remarks = body.remarks
                    countryOfOrigin = body.countryOfOrigin
                    sampleCollectionNumber = body.sampleCollectionNumber
                    lbIdAnyAomarking = body.lbIdAnyAomarking
                    lbIdBatchNo = body.lbIdBatchNo
                    lbIdContDecl = body.lbIdContDecl
                    lbIdDateOfManf = body.lbIdDateOfManf
                    sampleCollectionDate = commonDaoServices.getCurrentDate()
                    lbIdExpiryDate = body.lbIdExpiryDate
                    lbIdTradeMark = body.lbIdTradeMark
                    noteTransResults = body.noteTransResults
                    scfNo = body.scfNo
                    cocNumber = body.cocNumber
                    bsNumber = body.bsNumber
                    productDescription = body.productDescription
                    sourceProductEvidence = body.sourceProductEvidence
                    dataReportID = body.dataReportID
                    workPlanGeneratedID = workPlanScheduled.id
                    status = map.activeStatus
                    createdBy = commonDaoServices.concatenateName(user)
                    createdOn = commonDaoServices.getTimestamp()

                }
                saveSSF = sampleSubmissionRepo.save(saveSSF)
            }


            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(saveSSF)}"
            sr.names = "Save SSF Details"

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
        return Pair(sr, saveSSF)
    }

    fun workPlanInspectionDetailsAddCountiesTowns(
        body: WorkPlanCountyTownDto,
        workPlanScheduled: MsWorkPlanGeneratedEntity,
        map: ServiceMapsEntity,
        user: UsersEntity
    ): Pair<ServiceRequestsEntity, MsWorkPlanCountiesTownsEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var saveDataCountiesTowns = MsWorkPlanCountiesTownsEntity()
        try {
            workPlanCountiesTownsRepo.findByIdOrNull(body.id ?: -1L)?.let { param ->
                with(param) {
                    regionID = body.regionId
                    countyId = body.countyId
                    townsId = body.townsId
                    workPlanId = workPlanScheduled.id
                    status = map.activeStatus
                    modifiedBy = commonDaoServices.concatenateName(user)
                    modifiedOn = commonDaoServices.getTimestamp()

                }
                saveDataCountiesTowns = workPlanCountiesTownsRepo.save(param)
            } ?: kotlin.run {
                with(saveDataCountiesTowns) {
                    regionID = body.regionId
                    countyId = body.countyId
                    townsId = body.townsId
                    workPlanId = workPlanScheduled.id
                    status = map.activeStatus
                    createdBy = commonDaoServices.concatenateName(user)
                    createdOn = commonDaoServices.getTimestamp()

                }
                saveDataCountiesTowns = workPlanCountiesTownsRepo.save(saveDataCountiesTowns)
            }

            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(saveDataCountiesTowns)}"
            sr.names = "Save Data Counties Towns Details"

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
        return Pair(sr, saveDataCountiesTowns)
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
            dataReportParameterRepo.findByIdOrNull(body.id ?: -1L)?.let { param ->
                with(param) {
                    typeBrandName = body.typeBrandName
                    productName = body.productName
                    localImport = body.localImport
                    permitNumber = body.permitNumber
                    ucrNumber = body.ucrNumber
                    complianceInspectionParameter = body.complianceInspectionParameter
                    measurementsResults = body.measurementsResults
                    remarks = body.remarks
                    importerManufacturer = body.importerManufacturer
                    dataReportId = dataReport.id
                    status = map.activeStatus
                    modifiedBy = commonDaoServices.concatenateName(user)
                    modifiedOn = commonDaoServices.getTimestamp()

                }
                saveDataReport = dataReportParameterRepo.save(param)
            } ?: kotlin.run {
                with(saveDataReport) {
                    typeBrandName = body.typeBrandName
                    productName = body.productName
                    localImport = body.localImport
                    permitNumber = body.permitNumber
                    ucrNumber = body.ucrNumber
                    complianceInspectionParameter = body.complianceInspectionParameter
                    measurementsResults = body.measurementsResults
                    remarks = body.remarks
                    importerManufacturer = body.importerManufacturer
                    dataReportId = dataReport.id
                    status = map.activeStatus
                    createdBy = commonDaoServices.concatenateName(user)
                    createdOn = commonDaoServices.getTimestamp()

                }
                saveDataReport = dataReportParameterRepo.save(saveDataReport)
            }


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

    fun workPlanInspectionDetailsAddSSFLabParams(
        body: SampleSubmissionItemsDto,
        SSFReport: MsSampleSubmissionEntity,
        map: ServiceMapsEntity,
        user: UsersEntity
    ): Pair<ServiceRequestsEntity, MsLaboratoryParametersEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var saveSSFLabParams = MsLaboratoryParametersEntity()
        try {
            ssfLabParametersRepo.findByIdOrNull(body.id ?: -1L)?.let { param ->
                with(param) {
                    sampleSubmissionId = body.id
                    laboratoryName = body.laboratoryName
                    parameters = body.parameters
                    status = map.activeStatus
                    modifiedBy = commonDaoServices.concatenateName(user)
                    modifiedOn = commonDaoServices.getTimestamp()

                }
                saveSSFLabParams = sampleSubmitParameterRepo.save(param)
            } ?: kotlin.run {
                with(saveSSFLabParams) {
                    sampleSubmissionId = body.id
                    laboratoryName = body.laboratoryName
                    parameters = body.parameters
                    status = map.activeStatus
                    createdBy = commonDaoServices.concatenateName(user)
                    createdOn = commonDaoServices.getTimestamp()

                }
                saveSSFLabParams = sampleSubmitParameterRepo.save(saveSSFLabParams)
            }


            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(saveSSFLabParams)}"
            sr.names = "Save SSF Lab Parameters Details"

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
        return Pair(sr, saveSSFLabParams)
    }

    fun workPlanInspectionDetailsAddSeizureDeclaration(
        body: SeizureDto,
        workPlanScheduled: MsWorkPlanGeneratedEntity,
        map: ServiceMapsEntity,
        user: UsersEntity
    ): Pair<ServiceRequestsEntity, MsSeizureEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var saveData = MsSeizureEntity()
        try {

            seizureDeclarationRepo.findByIdOrNull(body.id ?: -1L)
                ?.let { sd ->
                    saveData = saveSeizureParams(sd, body, workPlanScheduled, map, user, true)
                } ?: kotlin.run {
                saveData = saveSeizureParams(saveData, body, workPlanScheduled, map, user, false)
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

    fun workPlanInspectionDetailsAddMainSeizure(
        body: SeizureListDto,
        workPlanScheduled: MsWorkPlanGeneratedEntity,
        map: ServiceMapsEntity,
        user: UsersEntity
    ): Pair<ServiceRequestsEntity, MsSeizureDeclarationEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var saveData = MsSeizureDeclarationEntity()
        try {

            seizureRepo.findByIdOrNull(body.id ?: -1L)
                ?.let { sd ->
                    saveData = saveSeizure(sd, body, workPlanScheduled, map, user, true)
                } ?: kotlin.run {
                saveData = saveSeizure(saveData, body, workPlanScheduled, map, user, false)
            }

            saveData = seizureRepo.save(saveData)

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

    fun saveSeizure(
        saveData: MsSeizureDeclarationEntity,
        body: SeizureListDto,
        workPlanScheduled: MsWorkPlanGeneratedEntity,
        map: ServiceMapsEntity,
        user: UsersEntity,
        update: Boolean
    ): MsSeizureDeclarationEntity {
        with(saveData) {
            marketTownCenter = body.marketTownCenter
            serialNumber = body.serialNumber
            productField = body.productField
            nameOfOutlet = body.nameOfOutlet
            docId = body.docID
            additionalOutletDetails = body.additionalOutletDetails
            nameSeizingOfficer = body.nameSeizingOfficer
            workPlanGeneratedID = workPlanScheduled.id
            status = map.activeStatus
            when {
                update -> {
                    modifiedBy = commonDaoServices.concatenateName(user)
                    modifiedOn = commonDaoServices.getTimestamp()
                }
                else -> {
                    createdBy = commonDaoServices.concatenateName(user)
                    createdOn = commonDaoServices.getTimestamp()
                }
            }

        }

        return saveData
    }

    fun saveSeizureParams(
        saveData: MsSeizureEntity,
        body: SeizureDto,
        workPlanScheduled: MsWorkPlanGeneratedEntity,
        map: ServiceMapsEntity,
        user: UsersEntity,
        update: Boolean
    ): MsSeizureEntity {
        with(saveData) {
            marketTownCenter = body.marketTownCenter
            dateSeizure = body.dateSeizure
            dateDestructed = body.dateDestructed
            dateRelease = body.dateRelease
            productsRelease = body.productsRelease
            nameOfOutlet = body.nameOfOutlet
            descriptionProductsSeized = body.descriptionProductsSeized
            brand = body.brand
            product = body.product
            sector = body.sector
            docId = body.docID
            dateOfSeizure = commonDaoServices.getCurrentDate()
            mainSeizureId = body.mainSeizureID
            additionalOutletDetails = body.additionalOutletDetails
            reasonSeizure = body.reasonSeizure
            nameSeizingOfficer = body.nameSeizingOfficer
            seizureSerial = body.seizureSerial
            quantity = body.quantity
            unit = body.unit
            estimatedCost = body.estimatedCost
            currentLocation = body.currentLocation
            productsDestruction = body.productsDestruction
            workPlanGeneratedID = workPlanScheduled.id
            status = map.activeStatus
            when {
                update -> {
                    modifiedBy = commonDaoServices.concatenateName(user)
                    modifiedOn = commonDaoServices.getTimestamp()
                }
                else -> {
                    createdBy = commonDaoServices.concatenateName(user)
                    createdOn = commonDaoServices.getTimestamp()
                }
            }

        }

        return saveData
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

            investInspectReportRepo.findByIdOrNull(body.id ?: -1L)
                ?.let { fdr ->
                    saveData = msFieldReport(fdr, body, workPlanScheduled, map, user, false)
                } ?: kotlin.run {
                saveData = msFieldReport(saveData, body, workPlanScheduled, map, user, false)
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

    fun msFieldReport(
        saveData: MsInspectionInvestigationReportEntity,
        body: InspectionInvestigationReportDto,
        workPlanScheduled: MsWorkPlanGeneratedEntity,
        map: ServiceMapsEntity,
        user: UsersEntity,
        update: Boolean
    ): MsInspectionInvestigationReportEntity {
        with(saveData) {
            when (body.reportFunction) {
                "Food" -> {
                    reportReference = "KEBS/MS/XXX/FOO/R1/${generateRandomText(5, map.secureRandom, map.messageDigestAlgorithm, true)}".toUpperCase().addRegion(body.reportRegion.toString())
                }
                "Agriculture" -> {
                    reportReference = "KEBS/MS/XXX/AGR/R1/${generateRandomText(5, map.secureRandom, map.messageDigestAlgorithm, true)}".toUpperCase().addRegion(body.reportRegion.toString())
                }
                "Chemical" -> {
                    reportReference = "KEBS/MS/XXX/CHEM/R1/${generateRandomText(5, map.secureRandom, map.messageDigestAlgorithm, true)}".toUpperCase().addRegion(body.reportRegion.toString())
                }
                "Civil" -> {
                    reportReference = "KEBS/MS/XXX/CIV/R1/${generateRandomText(5, map.secureRandom, map.messageDigestAlgorithm, true)}".toUpperCase().addRegion(body.reportRegion.toString())
                }
                "Electrical" -> {
                    reportReference = "KEBS/MS/XXX/ELEC/R1/${generateRandomText(5, map.secureRandom, map.messageDigestAlgorithm, true)}".toUpperCase().addRegion(body.reportRegion.toString())
                }
                "Mechanical" -> {
                    reportReference = "KEBS/MS/XXX/MECH/R1/${generateRandomText(5, map.secureRandom, map.messageDigestAlgorithm, true)}".toUpperCase().addRegion(body.reportRegion.toString())
                }
                "Textile" -> {
                    reportReference = "KEBS/MS/XXX/TEX/R1/${generateRandomText(5, map.secureRandom, map.messageDigestAlgorithm, true)}".toUpperCase().addRegion(body.reportRegion.toString())
                }
                else -> {
                    reportReference = "REF/INITIAL/${generateRandomText(5, map.secureRandom, map.messageDigestAlgorithm, true)}".toUpperCase().addRegion(body.reportRegion.toString())
                }
            }


            reportClassification = body.reportClassification
            reportTo = body.reportTo
            reportThrough = body.reportThrough
            reportFrom = body.reportFrom
            reportSubject = body.reportSubject
            reportTitle = body.reportTitle
            reportDate = body.reportDate
            reportRegion = body.reportRegion
            reportDepartment = body.reportDepartment
            reportFunction = body.reportFunction
            backgroundInformation = body.backgroundInformation
            objectiveInvestigation = body.objectiveInvestigation
            startDateInvestigationInspection = body.startDateInvestigationInspection
            endDateInvestigationInspection = body.endDateInvestigationInspection
            kebsInspectors = body.kebsInspectors?.let { commonDaoServices.convertClassToJson(it) }
            methodologyEmployed = body.methodologyEmployed
            findings = body.findings
            summaryOfFindings = body.summaryOfFindings
            createdUserId = user.id
            conclusion = body.conclusion
            recommendations = body.recommendations
            statusActivity = body.statusActivity
            finalRemarkHod = body.finalRemarkHod
            workPlanGeneratedID = workPlanScheduled.id
            isPreliminaryReport = map.inactiveStatus
            status = map.activeStatus
            when {
                update -> {
                    modifiedBy = commonDaoServices.concatenateName(user)
                    modifiedOn = commonDaoServices.getTimestamp()
                }
                else -> {
                    createdBy = commonDaoServices.concatenateName(user)
                    createdOn = commonDaoServices.getTimestamp()
                }
            }

        }
        return saveData
    }

    fun String.changeToR2(): String {
        if (this.contains("R1")){
            return this.replace("R1", "R2")
        }else{
            return this
        }
    }

    fun String.addRegion(region: String): String{
        return when {
            region.contains("NAIROBI REGION") -> this.replace("XXX", "HQ")
            region.contains("COAST REGION") -> this.replace("XXX", "COR")
            region.contains("LAKE REGION") -> this.replace("XXX", "LAR")
            region.contains("SOUTH RIFT REGION") -> this.replace("XXX", "SRR")
            region.contains("NORTH RIFT REGION") -> this.replace("XXX", "NRR")
            region.contains("NORTH EASTERN REGION") -> this.replace("XXX", "NER")
            region.contains("MOUNT KENYA REGION") -> this.replace("XXX", "MKR")
            else -> this
        }
    }




    fun msFieldReportWhichIsPreliminaryReport(
        saveData: MsInspectionInvestigationReportEntity,
        body: InspectionInvestigationReportDto,
        workPlanScheduled: MsWorkPlanGeneratedEntity,
        map: ServiceMapsEntity,
        user: UsersEntity,
        versionValue: Int,
        update: Boolean
    ): MsInspectionInvestigationReportEntity {
        with(saveData) {
            reportReference = body.reportReference?.changeToR2()
            reportClassification = body.reportClassification
            reportTo = body.reportTo
            reportThrough = body.reportThrough
            reportFrom = body.reportFrom
            reportSubject = body.reportSubject
            reportTitle = body.reportTitle
            reportDate = body.reportDate
            reportRegion = body.reportRegion
            reportDepartment = body.reportDepartment
            reportFunction = body.reportFunction
            backgroundInformation = body.backgroundInformation
            objectiveInvestigation = body.objectiveInvestigation
            startDateInvestigationInspection = body.startDateInvestigationInspection
            endDateInvestigationInspection = body.endDateInvestigationInspection
            kebsInspectors = body.kebsInspectors?.let { commonDaoServices.convertClassToJson(it) }
            methodologyEmployed = body.methodologyEmployed
            findings = body.findings
            summaryOfFindings = body.summaryOfFindings
            createdUserId = user.id
            conclusion = body.conclusion
            recommendations = body.recommendations
            statusActivity = body.statusActivity
            bsNumbersList = body.bsNumbersList?.let { commonDaoServices.convertClassToJson(it) }
            finalRemarkHod = body.finalRemarkHod
            workPlanGeneratedID = workPlanScheduled.id
            version = versionValue
            isPreliminaryReport = map.activeStatus
            status = map.activeStatus
            changesMade = body.changesMade
            summaryOfFindings = body.summaryOfFindings
            when {
                update -> {
                    modifiedBy = commonDaoServices.concatenateName(user)
                    modifiedOn = commonDaoServices.getTimestamp()
                }
                else -> {
                    createdBy = commonDaoServices.concatenateName(user)
                    createdOn = commonDaoServices.getTimestamp()
                }
            }

        }
        return saveData
    }


    fun workPlanInspectionDetailsAddPreliminaryReport(
        body: InspectionInvestigationReportDto,
        workPlanScheduled: MsWorkPlanGeneratedEntity,
        map: ServiceMapsEntity,
        user: UsersEntity
    ): Pair<ServiceRequestsEntity, MsInspectionInvestigationReportEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var saveData = MsInspectionInvestigationReportEntity()
        try {
            var versionValue = 1
            val saveDataFound = findPreliminaryReportTopVersionByWorkPlanInspectionID(workPlanScheduled.id, map.activeStatus)
            when {
                saveDataFound != null -> {
                    versionValue = saveDataFound.version?.plus(1)!!
                    saveData.id = null
                    saveData = msFieldReportWhichIsPreliminaryReport(
                        saveData,
                        body,
                        workPlanScheduled,
                        map,
                        user,
                        versionValue,
                        false
                    )
                }
                else -> {
                    saveData.id = null
                    saveData = msFieldReportWhichIsPreliminaryReport(
                        saveData,
                        body,
                        workPlanScheduled,
                        map,
                        user,
                        versionValue,
                        false
                    )
                }
            }

//            investInspectReportRepo.findByIdOrNull(body.id?: -1L)
//                ?.let { fdr->
//                    saveData= msFieldReportWhichIsPreliminaryReport(fdr, body, workPlanScheduled, map, user, versionValue,false)
//                }?: kotlin.run {
//
//            }

            saveData = investInspectReportRepo.save(saveData)
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

    fun workPlanInspectionDetailsPreliminaryReport(
        workPlanScheduled: MsWorkPlanGeneratedEntity,
        map: ServiceMapsEntity,
        user: UsersEntity,
        preliminaryReportID: Long?,
        updateID: Long?,
        finalReport: Boolean
    ): Pair<ServiceRequestsEntity, MsPreliminaryReportEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var saveData = MsPreliminaryReportEntity()
        try {

            preliminaryRepo.findByIdOrNull(updateID ?: -1L)
                ?.let { prelimFound ->
                    with(prelimFound) {
                        latestVersionID = preliminaryReportID
                        finalReportStatus = when {
                            finalReport -> {
                                map.activeStatus
                            }
                            else -> {
                                map.inactiveStatus
                            }
                        }
                        workPlanGeneratedID = workPlanScheduled.id
                        status = map.activeStatus
                        modifiedBy = commonDaoServices.concatenateName(user)
                        modifiedOn = commonDaoServices.getTimestamp()
                    }
                    saveData = preliminaryRepo.save(prelimFound)
                }
                ?: kotlin.run {
                    with(saveData) {
                        latestVersionID = preliminaryReportID
                        workPlanGeneratedID = workPlanScheduled.id
                        finalReportStatus = when {
                            finalReport -> {
                                map.activeStatus
                            }
                            else -> {
                                map.inactiveStatus
                            }
                        }
                        status = map.activeStatus
                        createdBy = commonDaoServices.concatenateName(user)
                        createdOn = commonDaoServices.getTimestamp()
                    }
                    saveData = preliminaryRepo.save(saveData)
                }

            sr.payload = "VALUES To BE SABEDS"
            sr.names = "Save Preliminary Report Details"

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = map.successStatus
            sr = serviceRequestsRepo.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.payload = "Test DATAD"
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepo.save(sr)

        }
        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return Pair(sr, saveData)
    }

    fun workPlanInspectionDetailsPartiallySavePreliminaryReportAndFieldReport(
        body: InspectionInvestigationReportDto,
        workPlanScheduled: MsWorkPlanGeneratedEntity,
        map: ServiceMapsEntity,
        user: UsersEntity,
        isFieldReport: Boolean
    ): Pair<ServiceRequestsEntity, MsInspectionInvestigationReportEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var saveData = MsInspectionInvestigationReportEntity()
        try {

            investInspectReportRepo.findByIdOrNull(body.id ?: -1L)
                ?.let { fdr ->
                    saveData = when {
                        isFieldReport -> {
                            msFieldReport(fdr, body, workPlanScheduled, map, user, false)
                        }
                        else -> {
                            msFieldReportWhichIsPreliminaryReport(
                                fdr,
                                body,
                                workPlanScheduled,
                                map,
                                user,
                                fdr.version!!,
                                false
                            )
                        }
                    }
                } ?: throw ExpectedDataNotFound("No Data Was Found with following ID : ${body.id}")

            saveData = investInspectReportRepo.save(saveData)
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
        workPlanDetails: MsWorkPlanGeneratedEntity,
        versionNumberDetails: Long? = null,
        isFinalReport: Int? = null
    ): Pair<ServiceRequestsEntity, MsUploadsEntity> {
        var upload = MsUploadsEntity()
        var sr = commonDaoServices.createServiceRequest(map)
        try {
            with(upload) {
                msWorkplanGeneratedId = workPlanDetails.id
                if (isFinalReport == 1) {
                    isUploadFinalReport = 1
                }
                workPlanUploads = 1
                ordinaryStatus = 0
                versionNumber = when {
                    versionNumberDetails != null -> {
                        versionNumberDetails
                    }
                    else -> {
                        1
                    }
                }

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

            generateWorkPlanRepo.findByIdOrNull(body.id ?: -1L)
                ?.let { updateWorkPlan ->
                    workPlanSchedule =
                        addNewAndUpdateWorkPlan(updateWorkPlan, body, map, msType, userWorkPlan, usersEntity, true)
                } ?: kotlin.run {
                workPlanSchedule =
                    addNewAndUpdateWorkPlan(workPlanSchedule, body, map, msType, userWorkPlan, usersEntity, false)
            }

            workPlanSchedule = generateWorkPlanRepo.save(workPlanSchedule)

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

    fun addNewAndUpdateWorkPlan(
        workPlanSchedule: MsWorkPlanGeneratedEntity,
        body: WorkPlanEntityDto,
        map: ServiceMapsEntity,
        msType: MsTypesEntity,
        userWorkPlan: WorkPlanCreatedEntity,
        usersEntity: UsersEntity,
        updateDetails: Boolean
    ): MsWorkPlanGeneratedEntity {
        with(workPlanSchedule) {
            complaintDepartment = body.complaintDepartment
            divisionId = body.divisionId
            nameActivity = body.nameActivity
            rationale = body.rationale
            scopeOfCoverage = body.scopeOfCoverage
            timeActivityDate = body.timeActivityDate
            timeActivityEndDate = body.timeActivityEndDate
            region = commonDaoServices.findUserProfileByUserID(usersEntity).regionId?.id
            county = commonDaoServices.findUserProfileByUserID(usersEntity).countyID?.id
            townMarketCenter =  commonDaoServices.findUserProfileByUserID(usersEntity).townID?.id
            locationActivityOther = body.locationActivityOther
            standardCategory = body.standardCategory
            broadProductCategory = body.broadProductCategory
            productCategory = body.productCategory
            product = body.product
            productSubCategory = body.productSubCategory
            standardCategoryString = body.standardCategoryString
            broadProductCategoryString = body.broadProductCategoryString
            productCategoryString = body.productCategoryString
            productString = body.productString
            productSubCategoryString = body.productSubCategoryString
            resourcesRequired = body.resourcesRequired?.let { commonDaoServices.convertClassToJson(it) }
            budget = body.budget
            msProcessEndedStatus = map.inactiveStatus
            updatedStatus = map.inactiveStatus
            resubmitStatus = map.inactiveStatus
            uuid = commonDaoServices.generateUUIDString()
            msTypeId = msType.id
            progressStep = "WorkPlan Generated"
            region = county?.let { commonDaoServices.findCountiesEntityByCountyId(it, map.activeStatus).regionId }
            when {
                updateDetails -> {
                    modifiedBy = commonDaoServices.concatenateName(usersEntity)
                    modifiedOn = commonDaoServices.getTimestamp()
                }
                else -> {
                    referenceNumber = "${msType.markRef}${
                        generateRandomText(
                            5,
                            map.secureRandom,
                            map.messageDigestAlgorithm,
                            true
                        )
                    }".toUpperCase()
                    createdBy = commonDaoServices.concatenateName(usersEntity)
                    createdOn = commonDaoServices.getTimestamp()
                }
            }
            workPlanYearId = userWorkPlan.id
            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
            msProcessId = applicationMapProperties.mapMSWorkPlanInspectionGenerateWorkPlan
            status = map.initStatus
            officerId = usersEntity.id
            officerName = commonDaoServices.concatenateName(usersEntity)

        }
        return workPlanSchedule
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
        val (comp, complaintCustomersDetails, complaintLocationDetails) = msComplaintDaoServices.complaintDetails(
            complaint
        )
        try {
            with(workPlanSchedule) {
                complaintId = comp.id
                complaintDepartment = comp.complaintDepartment
                divisionId = comp.division
                nameActivity = body.nameActivity
                timeActivityDate = body.timeActivityDate
                timeActivityDate = body.timeActivityEndDate
                rationale = body.rationale
                scopeOfCoverage = body.scopeOfCoverage
                county = complaintLocationDetails.county
                townMarketCenter = complaintLocationDetails.town
                locationActivityOther = complaintLocationDetails.marketCenter
                standardCategory = comp.standardCategory
                broadProductCategory = comp.broadProductCategory
                productCategory = comp.productCategory
                product = comp.product
                productSubCategory = comp.productSubCategory
                resourcesRequired = body.resourcesRequired?.let { commonDaoServices.convertClassToJson(it) }
                standardCategoryString = comp.standardCategoryString
                broadProductCategoryString = comp.broadProductCategoryString
                productCategoryString = comp.productCategoryString
                productString = comp.productString
                productSubCategoryString = comp.productSubCategoryString
                budget = body.budget
                uuid = commonDaoServices.generateUUIDString()
                msTypeId = msType.id
                submittedForApprovalStatus = map.activeStatus
                progressStep = "WorkPlan Generated"
                region = complaintLocationDetails.region
                referenceNumber = "${msType.markRef}${
                    generateRandomText(
                        5,
                        map.secureRandom,
                        map.messageDigestAlgorithm,
                        true
                    )
                }".toUpperCase()
                workPlanYearId = userWorkPlan.id
                hodRmAssigned = comp.hodAssigned
                timelineStartDate = commonDaoServices.getCurrentDate()
                timelineEndDate = applicationMapProperties.mapMSWorkPlanInspectionApprovedWorPlan.let { timeLine ->
                    findProcessNameByID(timeLine, 1).timelinesDay
                }?.let { daysCount ->
                    commonDaoServices.addDaysSkippingWeekends(LocalDate.now(), daysCount)
                        ?.let { daysConvert -> commonDaoServices.localDateToTimestamp(daysConvert) }
                }
                msProcessId = applicationMapProperties.mapMSWorkPlanInspectionApprovedWorPlan
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

            workPlanSchedule = generateWorkPlanRepo.save(workPlanSchedule)

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
        flBatchId: Long? = null,
    ): Pair<ServiceRequestsEntity, MsRemarksEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var remarks = MsRemarksEntity()
        try {
            with(remarks) {
                remarksDescription = body.remarksDescription
                remarksStatus = body.remarksStatus
                userId = body.userId
                workPlanId = workPlanInspectionId
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
            workPlanList != null -> {
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

    fun mapWorkPlanInspectionListDto(
        workPlanList: List<MsWorkPlanGeneratedEntity>?,
        createdWorkPlan: WorkPlanBatchDetailsDto
    ): WorkPlanScheduleListDetailsDto {
        val workPlanInspectionScheduledList = mutableListOf<WorkPlanInspectionDto>()
        workPlanList?.map {
            workPlanInspectionScheduledList.add(
                WorkPlanInspectionDto(
                    id = it.id,
                    nameActivity = it.nameActivity,
                    budget = it.budget,
                    progressStep = it.msProcessId?.let { it1 -> findProcessNameByID(it1, 1).processName },
                    timeActivityDate = it.timeActivityDate,
                    timeActivityEndDate = it.timeActivityEndDate,
                    referenceNumber = it.referenceNumber,
                    product = it.productString,

                )
            )
        }

        return WorkPlanScheduleListDetailsDto(
            workPlanInspectionScheduledList,
            createdWorkPlan
        )
    }

    fun mapDashBoardWorkPlanInspectionListViewDto(workPlanList: Page<MsAllocatedTasksWpViewEntity>): ApiResponseModel {
        val workPlanInspectionScheduledList = mutableListOf<WorkPlanInspectionDto>()
        workPlanList.map {
            workPlanInspectionScheduledList.add(
                WorkPlanInspectionDto(
                    nameActivity = it.nameActivity,
                    budget = it.budget,
                    progressStep = it.msProcessId?.let { it1 -> findProcessNameByID(it1, 1).processName },
                    timeActivityDate = it.timeActivityDate,
                    referenceNumber = it.referenceNumber,
                    batchRefNumber = it.batchRefNumber
                )
            )
        }

        return commonDaoServices.setSuccessResponse(
            workPlanInspectionScheduledList,
            workPlanList.totalPages,
            workPlanList.number,
            workPlanList.totalElements
        )
    }

    fun mapDashBoardWorkPlanInspectionPendingAllocationListViewDto(workPlanList: Page<MsTasksPendingAllocationWpViewEntity>): ApiResponseModel {
        val workPlanInspectionScheduledList = mutableListOf<WorkPlanInspectionDto>()
        workPlanList.map {
            workPlanInspectionScheduledList.add(
                WorkPlanInspectionDto(
                    nameActivity = it.nameActivity,
                    budget = it.budget,
                    progressStep = it.msProcessId?.let { it1 -> findProcessNameByID(it1, 1).processName },
                    timeActivityDate = it.timeActivityDate,
                    referenceNumber = it.referenceNumber,
                    batchRefNumber = it.batchRefNumber
                )
            )
        }

        return commonDaoServices.setSuccessResponse(
            workPlanInspectionScheduledList,
            workPlanList.totalPages,
            workPlanList.number,
            workPlanList.totalElements
        )
    }

    fun findProcessNameByID(processID: Long, status: Int): MsProcessNamesEntity {
        processNameRepo.findByWorkPlanStatusAndId(status, processID)
            ?.let {
                return it
            }
            ?: throw ExpectedDataNotFound("Missing WorkPlan Process Details found with ID : $processID")
    }

    fun mapWorkPlanBatchDetailsDto(
        workPlanBatch: WorkPlanCreatedEntity,
        map: ServiceMapsEntity
    ): WorkPlanBatchDetailsDto {
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

    fun mapSeizureDetailsDto(
        data: MsSeizureDeclarationEntity,
        data2: List<SeizureDto>
    ): SeizureListDto {
        return SeizureListDto(
            data.id,
            data.docId,
            data.marketTownCenter,
            data.productField,
            data.serialNumber,
            data.nameOfOutlet,
            data.nameSeizingOfficer,
            data.additionalOutletDetails,
            data2
        )

    }

    fun mapSeizureDeclarationDetailsDto(
        data: List<MsSeizureEntity>
    ): List<SeizureDto> {
        return data.map { seizureDeclaration: MsSeizureEntity ->
            SeizureDto(
                seizureDeclaration.id,
                seizureDeclaration.docId,
                seizureDeclaration.mainSeizureId,
                seizureDeclaration.marketTownCenter,
                seizureDeclaration.nameOfOutlet,
                seizureDeclaration.descriptionProductsSeized,
                seizureDeclaration.brand,
                seizureDeclaration.product,
                seizureDeclaration.sector,
                seizureDeclaration.reasonSeizure,
                seizureDeclaration.nameSeizingOfficer,
                seizureDeclaration.seizureSerial,
                seizureDeclaration.quantity,
                seizureDeclaration.unit,
                seizureDeclaration.estimatedCost,
                seizureDeclaration.currentLocation,
                seizureDeclaration.productsDestruction,
                seizureDeclaration.additionalOutletDetails,
                null,
                null,
                null,
                null,
                seizureDeclaration.dateOfSeizure,
            )
        }

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
            dataReport.physicalLocation,
            dataReport.outletName,
            dataReport.phoneNumber,
            dataReport.emailAddress,
            dataReport.mostRecurringNonCompliant,
            dataReport.additionalNonComplianceDetails,
            dataReport.personMet,
            dataReport.summaryFindingsActionsTaken,
            dataReport.samplesDrawnAndSubmitted,
            dataReport.sourceOfProductAndEvidence,
            dataReport.finalActionSeizedGoods,
            dataReport.totalComplianceScore,
            dataReport.numberOfProducts,
            dataReport.remarks,
            dataReportParam,
            dataReport.docList?.let { mapUploadListListDto(it) }
        )
    }

    fun mapInspectionInvestigationDetailsDto(
        inspectionInvestigation: MsInspectionInvestigationReportEntity
    ): InspectionInvestigationReportDto {
        return InspectionInvestigationReportDto(
            inspectionInvestigation.id,
            inspectionInvestigation.reportReference,
            inspectionInvestigation.reportClassification,
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
            inspectionInvestigation.startDateInvestigationInspection,
            inspectionInvestigation.endDateInvestigationInspection,
            inspectionInvestigation.kebsInspectors?.let { mapKEBSOfficersNameListDto(it) },
            inspectionInvestigation.methodologyEmployed,
            inspectionInvestigation.findings,
            inspectionInvestigation.conclusion,
            inspectionInvestigation.recommendations,
            inspectionInvestigation.statusActivity,
            inspectionInvestigation.finalRemarkHod,
            null,
            gson.fromJson(inspectionInvestigation.additionalInformation, FieldReportAdditionalInfo::class.java),
            inspectionInvestigation.additionalInformationStatus == 1,
            null,
            null,
            null,
            null,
            null,
            inspectionInvestigation.summaryOfFindings,
        )
    }

    fun mapInspectionInvestigationDetailsListDto(
        data: List<MsInspectionInvestigationReportEntity>
    ): List<InspectionInvestigationReportDto> {
        return data.map {
            InspectionInvestigationReportDto(
                it.id,
                it.reportReference,
                it.reportClassification,
                it.reportTo,
                it.reportThrough,
                it.reportFrom,
                it.reportSubject,
                it.reportTitle,
                it.reportDate,
                it.reportRegion,
                it.reportDepartment,
                it.reportFunction,
                it.backgroundInformation,
                it.objectiveInvestigation,
                it.startDateInvestigationInspection,
                it.endDateInvestigationInspection,
                it.kebsInspectors?.let { it2 -> mapKEBSOfficersNameListDto(it2) },
                it.methodologyEmployed,
                it.findings,
                it.conclusion,
                it.recommendations,
                it.statusActivity,
                it.finalRemarkHod,
                null,
                gson.fromJson(it.additionalInformation, FieldReportAdditionalInfo::class.java),
                it.additionalInformationStatus == 1,
                it.bsNumbersList?.let { it2 -> mapBSNumberListDto(it2) },
                it.version,
                it.createdBy,
                it.createdOn,
                it.changesMade,
                it.summaryOfFindings,
            )
        }
    }

    fun mapDataReportParamListDto(data: List<MsDataReportParametersEntity>): List<DataReportParamsDto> {
        return data.map {
            DataReportParamsDto(
                it.id,
                it.productName,
                it.typeBrandName,
                it.localImport,
                it.permitNumber,
                it.ucrNumber,
                it.complianceInspectionParameter,
                it.measurementsResults,
                it.remarks,
                it.importerManufacturer,
            )
        }
    }

    fun findALlWorkPlanDetailsAssociatedWithWorkPlanID(
        createdWorkPlanID: Long,
        pageable: Pageable
    ): Page<MsWorkPlanGeneratedEntity> {
        generateWorkPlanRepo.findByWorkPlanYearId(createdWorkPlanID, pageable)
            ?.let {
                return it
            }
            ?: throw ExpectedDataNotFound("No workPlan Details Associated with the following workPlan [ID = ${createdWorkPlanID}]")
    }

    fun findALlWorkPlanDetailsAssociatedWithWorkPlanIDWithComplaintIN(
        createdWorkPlanID: Long,
        pageable: Pageable
    ): Page<MsWorkPlanGeneratedEntity> {
        generateWorkPlanRepo.findByWorkPlanYearIdAndComplaintIdIsNotNull(createdWorkPlanID, pageable)
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

    fun findWorkPlanProductByReferenceNumber(referenceNumber: String): WorkPlanProductsEntity {
        workPlanProductsRepo.findByReferenceNo(referenceNumber)
            ?.let { activityWorkPlan ->
                return activityWorkPlan
            }
            ?: throw ExpectedDataNotFound("WorkPlan Product with [referenceNumber = ${referenceNumber}], does not Exist")
    }

    fun findWorkPlanProductByWorkPlanID(workPlanID: Long): List<WorkPlanProductsEntity>? {
        return workPlanProductsRepo.findByWorkPlanId(workPlanID)
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

    fun findWorkPlanCreatedEntity(
        loggedInUser: UsersEntity,
        workPlanYearCodes: WorkplanYearsCodesEntity
    ): WorkPlanCreatedEntity? {
        return workPlanCreatedRepository.findByUserCreatedIdAndYearNameId(loggedInUser, workPlanYearCodes)
    }

    fun findWorkPlanCreatedEntity(
        loggedInUser: UsersEntity,
        workPlanYearCodes: WorkplanYearsCodesEntity,
        workPlanStatus: Int
    ): WorkPlanCreatedEntity? {
        return workPlanCreatedRepository.findByUserCreatedIdAndYearNameIdAndWorkPlanStatus(
            loggedInUser,
            workPlanYearCodes,
            workPlanStatus
        )
    }

    fun findWorkPlanCreatedComplaintEntity(
        loggedInUser: UsersEntity,
        workPlanYearCodes: WorkplanYearsCodesEntity,
        complaintStatus: Int
    ): WorkPlanCreatedEntity? {
        return workPlanCreatedRepository.findByUserCreatedIdAndYearNameIdAndComplaintStatus(
            loggedInUser,
            workPlanYearCodes,
            complaintStatus
        )
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
        val workPlanCountiesTowns = findCountiesTownsByWorkPlanID(workPlanScheduledDetails.id)
        val officerList = commonDaoServices.findOfficersListBasedOnRole(
            applicationMapProperties.mapMSComplaintWorkPlanMappedOfficerROLEID,
            workPlanScheduledDetails.region ?: throw ExpectedDataNotFound("MISSING WORK-PLAN REGION ID")
        )

        val hofList = commonDaoServices.findOfficersListBasedOnRole(
            applicationMapProperties.mapMSComplaintWorkPlanMappedHOFROLEID,
            workPlanScheduledDetails.region ?: throw ExpectedDataNotFound("MISSING WORK-PLAN REGION ID")
        )

        val chargeSheet = findChargeSheetByWorkPlanInspectionID(workPlanScheduledDetails.id)
        val chargeSheetDto = chargeSheet?.let { mapChargeSheetDetailsDto(it) }

        val seizureDtoList = mutableListOf<SeizureListDto>()
        findSeizureByWorkPlanInspectionID(workPlanScheduledDetails.id)
            ?.forEach { seizure ->
                val seizureDeclarationList = findSeizureDeclarationByWorkPlanInspectionID(workPlanScheduledDetails.id, seizure.id)
                val seizureDeclarationDtoList = seizureDeclarationList?.let { mapSeizureDeclarationDetailsDto(it) }
                val seizureDto = seizureDeclarationDtoList?.let { mapSeizureDetailsDto(seizure, it) }
                if (seizureDto != null) {
                    seizureDtoList.add(seizureDto)
                }
            }


        val dataReportDtoList = mutableListOf<DataReportDto>()
        var overAllCompliance = BigDecimal.ZERO
        findDataReportListByWorkPlanInspectionID(workPlanScheduledDetails.id)
            ?.forEach { dataReport ->
                val dataReportParameters = dataReport.id.let { findDataReportParamsByDataReportID(it) }
                val dataReportParametersDto = dataReportParameters?.let { mapDataReportParamListDto(it) }
                val dataReportDto = dataReport.let { dataReportParametersDto?.let { it1 -> mapDataReportDetailsDto(it, it1) } }
                if (dataReportDto != null) {
                    dataReportDtoList.add(dataReportDto)
                }
                overAllCompliance= dataReport.totalComplianceScore?.toBigDecimal()?.let { overAllCompliance.plus(it) }!!
            }


        val preliminaryReportList = findPreliminaryReportListByWorkPlanInspectionID(workPlanScheduledDetails.id, map.activeStatus)
        val preliminaryReportListDto = preliminaryReportList?.let { mapInspectionInvestigationDetailsListDto(it) }

        val inspectionInvestigation =findInspectionInvestigationByWorkPlanInspectionID(workPlanScheduledDetails.id, map.inactiveStatus)
        val inspectionInvestigationDto = inspectionInvestigation?.let { mapInspectionInvestigationDetailsDto(it) }

        val sampleCollected = findSampleCollectedDetailByWorkPlanInspectionID(workPlanScheduledDetails.id)
        val sampleCollectedParamList =
            sampleCollected?.id?.let { msFuelDaoServices.findAllSampleCollectedParametersBasedOnSampleCollectedID(it) }
        val sampleCollectedDtoValues =
            sampleCollectedParamList?.let { msFuelDaoServices.mapSampleCollectedParamListDto(it) }
                ?.let { msFuelDaoServices.mapSampleCollectedDto(sampleCollected, it) }

        val sampleSubmittedDtoList = mutableListOf<SampleSubmissionDto>()
        val labResultsDtoList = mutableListOf<MSSSFLabResultsDto>()
        var bsNumberCountAdded = 0
        var analysisLabCountDone = 0
        var analysisLabCountDoneAndSent = 0
        findSampleSubmissionDetailByWorkPlanGeneratedID(workPlanScheduledDetails.id)
            ?.forEach { sampleSubmitted ->
                val sampleSubmittedParamList = sampleSubmitted.id.let {
                    msFuelDaoServices.findAllSampleSubmissionParametersBasedOnSampleSubmissionID(it)
                }
                val sampleSubmittedDtoValues = sampleSubmittedParamList?.let { msFuelDaoServices.mapSampleSubmissionParamListDto(it) }
                        ?.let { msFuelDaoServices.mapSampleSubmissionDto(sampleSubmitted, it) }
                if (sampleSubmittedDtoValues != null) {
                    sampleSubmittedDtoList.add(sampleSubmittedDtoValues)
                    if (sampleSubmittedDtoValues.bsNumber != null) {
                        bsNumberCountAdded++
                        val labResultsParameters = sampleSubmittedDtoValues.bsNumber?.let {
                            msFuelDaoServices.findSampleLabTestResultsRepoBYBSNumber(it)
                        }
                        val ssfDetailsLab = findSampleSubmittedByWorkPlanGeneratedIDAndBsNumber(
                            workPlanScheduledDetails.id,
                            sampleSubmittedDtoValues.bsNumber!!
                        )
                        val savedPDFFilesLims = ssfDetailsLab?.id?.let {
                            msFuelDaoServices.findSampleSubmittedListPdfBYSSFid(it)
                                ?.let { ssfDetails -> msFuelDaoServices.mapLabPDFFilesListDto(ssfDetails) }
                        }
                        val ssfResultsListCompliance = ssfDetailsLab?.let { msFuelDaoServices.mapSSFComplianceStatusDetailsDto(it) }

                        if (ssfDetailsLab?.analysisDone == map.activeStatus) {
                            analysisLabCountDone++
                            if (ssfDetailsLab.resultsSent == map.activeStatus) {
                                analysisLabCountDoneAndSent++
                            }
                        }

                        val limsPDFFiles = ssfDetailsLab?.bsNumber?.let {
                            msFuelDaoServices.mapLIMSSavedFilesDto(
                                it,
                                savedPDFFilesLims
                            )
                        }
                        val labResultsDto = msFuelDaoServices.mapLabResultsDetailsDto(
                            ssfResultsListCompliance,
                            savedPDFFilesLims,
                            limsPDFFiles,
                            labResultsParameters?.let { msFuelDaoServices.mapLabResultsParamListDto(it) })
                        labResultsDtoList.add(labResultsDto)
                    }
                }
            }


        val compliantDetailsStatus = mapCompliantStatusDto(workPlanScheduledDetails, map)
        var compliantStatusDone = false
        if (compliantDetailsStatus != null) {
            compliantStatusDone = true
        }

        var timelineOverDue = false
        if (workPlanScheduledDetails.timelineEndDate != null) {
            if (workPlanScheduledDetails.timelineEndDate!! > commonDaoServices.getCurrentDate()) {
                timelineOverDue = true
            }
        }

        var updateWorkPlan: WorkPlanEntityDto? = null
        if (workPlanScheduledDetails.onsiteStartStatus != map.activeStatus) {
            updateWorkPlan = mapWorkPlanUpdateEntity(workPlanScheduledDetails, map,workPlanCountiesTowns)
        }


        val preliminaryReport = findPreliminaryReportByWorkPlanGeneratedIDAndFinalReportStatus(
            workPlanScheduledDetails.id,
            map.inactiveStatus
        )
        val preliminaryReportParamList = preliminaryReport?.id?.let { findPreliminaryReportParams(it) }
        val preliminaryReportDtoValues = preliminaryReportParamList?.let { mapPreliminaryParamListDto(it) }
            ?.let { mapPreliminaryReportDto(preliminaryReport, it) }

        val preliminaryReportFinal = findPreliminaryReportByWorkPlanGeneratedIDAndFinalReportStatus(
            workPlanScheduledDetails.id,
            map.activeStatus
        )
        val preliminaryReportParamListFinal = preliminaryReportFinal?.id?.let { findPreliminaryReportParams(it) }
        val preliminaryReportDtoValuesFinal = preliminaryReportParamListFinal?.let { mapPreliminaryParamListDto(it) }
            ?.let { mapPreliminaryReportDto(preliminaryReportFinal, it) }

        val workPlanProducts = findWorkPlanProductByWorkPlanID(workPlanScheduledDetails.id)
        val workPlanProductsDto = workPlanProducts?.let { mapWorkPlanProductsListDto(it) }


        return mapWorkPlanInspectionDto(
            workPlanScheduledDetails,
            workPlanCountiesTowns,
            officerList,
            hofList,
            map,
            batchDetailsDto,
            compliantStatusDone,
            timelineOverDue,
            workPlanInspectionRemarks,
            workPlanFiles,
            chargeSheetDto,
            seizureDtoList,
            inspectionInvestigationDto,
            preliminaryReportListDto,
            dataReportDtoList,
            sampleCollectedDtoValues,
            sampleSubmittedDtoList,
            labResultsDtoList,
            preliminaryReportDtoValues,
            updateWorkPlan,
            bsNumberCountAdded,
            analysisLabCountDone,
            analysisLabCountDoneAndSent,
            workPlanProductsDto?.second,
            workPlanProductsDto?.first,
            preliminaryReportDtoValuesFinal,
            overAllCompliance = when {overAllCompliance != BigDecimal.ZERO -> { overAllCompliance.div(dataReportDtoList.size.toBigDecimal()) }else -> { BigDecimal.ZERO } }

        )
    }

    private fun mapWorkPlanUpdateEntity(wkp: MsWorkPlanGeneratedEntity, map:ServiceMapsEntity,workPlanCountiesTowns: List<MsWorkPlanCountiesTownsEntity>?): WorkPlanEntityDto {
        return WorkPlanEntityDto(
            wkp.id,
            wkp.complaintDepartment,
            wkp.divisionId,
            wkp.nameActivity,
            wkp.rationale,
            wkp.scopeOfCoverage,
            wkp.timeActivityDate,
            wkp.timeActivityEndDate,
            wkp.region,
            wkp.county,
            wkp.townMarketCenter,
            wkp.locationActivityOther,
            wkp.standardCategory,
            wkp.broadProductCategory,
            wkp.productCategory,
            wkp.product,
            wkp.productSubCategory,
            wkp.resourcesRequired?.let { mapPredefinedResourcesRequiredListDto(it) },
            wkp.budget,
            wkp.updatedRemarks,
            wkp.standardCategoryString,
            wkp.broadProductCategoryString,
            wkp.productCategoryString,
            wkp.productString,
            wkp.productSubCategoryString,
            workPlanCountiesTowns?.let { mapCountiesTownsListDto(it, map) },
        )
    }

    fun mapWorkPlanInspectionDto(
        wKP: MsWorkPlanGeneratedEntity,
        workPlanCountiesTowns: List<MsWorkPlanCountiesTownsEntity>?,
        officerList: List<UsersEntity>?,
        hofList: List<UsersEntity>?,
        map: ServiceMapsEntity,
        batchDetails: WorkPlanBatchDetailsDto,
        compliantStatusDone: Boolean,
        timelineOverDue: Boolean,
        remarksList: List<MsRemarksEntity>?,
        workPlanFilesSaved: List<MsUploadsEntity>?,
        chargeSheet: ChargeSheetDto?,
        seizureDeclarationDto: List<SeizureListDto>?,
        inspectionInvestigationDto: InspectionInvestigationReportDto?,
        preliminaryReportListDto: List<InspectionInvestigationReportDto>?,
        dataReportDto: List<DataReportDto>?,
        sampleCollected: SampleCollectionDto?,
        sampleSubmitted: List<SampleSubmissionDto>?,
        sampleLabResults: List<MSSSFLabResultsDto>?,
        preliminaryReport: PreliminaryReportDto?,
        updateWorkPlan: WorkPlanEntityDto?,
        bsNumberCountAdded: Int,
        analysisLabCountDone: Int,
        analysisLabCountDoneAndSent: Int,
        productListRecommendationAddedCount: Int?,
        productList: List<WorkPlanProductDto>?,
        preliminaryReportFinal: PreliminaryReportDto?,
        overAllCompliance: BigDecimal,
    ): WorkPlanInspectionDto {
        return WorkPlanInspectionDto(
            wKP.id,
            wKP.productCategoryString,
//            wKP.productCategory?.let { commonDaoServices.findProductCategoryByID(it).name },
            wKP.broadProductCategoryString,
//            wKP.broadProductCategory?.let { commonDaoServices.findBroadCategoryByID(it).category },
            wKP.productString,
//            wKP.product?.let { commonDaoServices.findProductByID(it).name },
            wKP.standardCategoryString,
//            wKP.standardCategory?.let { commonDaoServices.findStandardCategoryByID(it).standardCategory },
            wKP.productSubCategoryString,
//            wKP.productSubCategory?.let { commonDaoServices.findProductSubCategoryByID(it).name },
            wKP.divisionId?.let { commonDaoServices.findDivisionWIthId(it).division },
            wKP.msProcessEndedOn,
            wKP.timelineStartDate,
            wKP.timelineEndDate,
            timelineOverDue,
//            wKP.sampleSubmittedId?.id,
            wKP.division,
            wKP.officerName,
            wKP.nameActivity,
            wKP.rationale,
            wKP.scopeOfCoverage,
            wKP.targetedProducts,
            wKP.resourcesRequired?.let { mapPredefinedResourcesRequiredListDto(it) },
            wKP.budget,
            wKP.approvedOn,
            wKP.approvedStatus == 1,
            wKP.workPlanYearId,
            wKP.clientAppealed == 1,
            wKP.directorRecommendationRemarksStatus == 1,
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
            wKP.workPlanCompliantStatus == 1,
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
            wKP.onsiteStartDateAdded,
            wKP.onsiteEndDateAdded,
            wKP.onsiteTat,
            wKP.sendSffDate,
            wKP.sendSffStatus == 1,
            wKP.onsiteEndStatus == 1,
            wKP.destractionStatus == 1,
            wKP.rejectedBy,
            wKP.rejected,
            wKP.msEndProcessRemarks,
            wKP.rejectedRemarks,
            wKP.approvedRemarks,
            wKP.progressValue == 1,
            wKP.msProcessId?.let { it1 -> findProcessNameByID(it1, map.activeStatus).processName },
            wKP.county?.let { commonDaoServices.findCountiesEntityByCountyId(it, map.activeStatus).county },
            wKP.subcounty,
            wKP.townMarketCenter?.let { commonDaoServices.findTownEntityByTownId(it).town },
            wKP.locationActivityOther,
            wKP.timeActivityDate,
            wKP.timeActivityEndDate,
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
            wKP.productSubCategory?.let { commonDaoServices.findSampleStandardsByID(it) }
                ?.let { msComplaintDaoServices.mapStandardDetailsDto(it) },
            remarksList?.let { mapRemarksListDto(it) },
            workPlanFilesSaved?.let { mapFileListDto(it) },
            chargeSheet,
            seizureDeclarationDto,
            inspectionInvestigationDto,
            preliminaryReportListDto,
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
            preliminaryReportFinal,
            officerList?.let { msComplaintDaoServices.mapOfficerListDto(it) },
            hofList?.let { msComplaintDaoServices.mapOfficerListDto(it) },
            updateWorkPlan,
            wKP.updatedStatus == 1,
            wKP.resubmitStatus == 1,
            wKP.recommendationDoneStatus == 1,
            bsNumberCountAdded,
            analysisLabCountDone,
            analysisLabCountDoneAndSent,
            productListRecommendationAddedCount,
            productList,
            workPlanCountiesTowns?.let { mapCountiesTownsListDto(it, map) },
            wKP.totalCompliance,
            wKP.totalCompliance,
            commonDaoServices.getCurrentDate(),
            wKP.latestPreliminaryReport,
            wKP.latestFinalPreliminaryReport,
            complaintsRepo.findByIdOrNull(wKP.complaintId?: -1L)?.referenceNumber,
            overAllCompliance

        )
    }

    fun mapFileListDto(fileFoundList: List<MsUploadsEntity>): List<WorkPlanFilesFoundDto> {
        return fileFoundList.map {
            WorkPlanFilesFoundDto(
                it.id,
                it.documentType,
                it.name,
                it.fileType,
                it.ordinaryStatus,
                it.isUploadFinalReport,
                it.versionNumber,
                it.createdBy,
                it.createdOn
            )
        }
    }

    fun mapCountiesTownsListDto(dataFoundList: List<MsWorkPlanCountiesTownsEntity>, map:ServiceMapsEntity): List<WorkPlanCountyTownDto> {
        return dataFoundList.map {
            WorkPlanCountyTownDto(
                it.id,
                it.regionID?.let { it1 -> commonDaoServices.findRegionEntityByRegionID(it1, map.activeStatus).id  },
                it.regionID?.let { it1 -> commonDaoServices.findRegionEntityByRegionID(it1, map.activeStatus).region  },
                it.countyId?.let { it1 -> commonDaoServices.findCountiesEntityByCountyId(it1, map.activeStatus).id },
                it.countyId?.let { it1 -> commonDaoServices.findCountiesEntityByCountyId(it1, map.activeStatus).county },
                it.townsId?.let { it1 -> commonDaoServices.findTownEntityByTownId(it1).id },
                it.townsId?.let { it1 -> commonDaoServices.findTownEntityByTownId(it1).town },
            )
        }
    }

    fun mapCompliantStatusDto(
        compliantDetails: QaSampleSubmissionEntity,
        map: ServiceMapsEntity
    ): SSFCompliantStatusDto? {

        return when {
            compliantDetails.resultsAnalysis == map.activeStatus -> {
                SSFCompliantStatusDto(
                    compliantDetails.complianceRemarks,
                    compliantDetails.resultsAnalysis == 1
                )
            }
            compliantDetails.resultsAnalysis == map.inactiveStatus -> {
                SSFCompliantStatusDto(
                    compliantDetails.complianceRemarks,
                    compliantDetails.resultsAnalysis == 1
                )
            }
            else -> null
        }

    }

    fun mapCompliantStatusDto(
        compliantDetails: MsWorkPlanGeneratedEntity,
        map: ServiceMapsEntity
    ): SSFCompliantStatusDto? {

        return when {
            compliantDetails.compliantStatus == map.activeStatus -> {
                SSFCompliantStatusDto(
                    compliantDetails.compliantStatusRemarks,
                    compliantDetails.compliantStatus == 1
                )
            }
            compliantDetails.notCompliantStatus == map.inactiveStatus -> {
                SSFCompliantStatusDto(
                    compliantDetails.notCompliantStatusRemarks,
                    compliantDetails.notCompliantStatus == 1
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
        val userListType: Type = object : TypeToken<ArrayList<KebsOfficersName?>?>() {}.type
        return gson.fromJson(officersName, userListType)
    }

    fun mapBSNumberListDto(bsNumber: String): List<String>? {
        val userListType: Type = object : TypeToken<ArrayList<String?>?>() {}.type
        return gson.fromJson(bsNumber, userListType)
    }

    fun mapUploadListListDto(docList: String): List<Long>? {
        val userListType: Type = object : TypeToken<ArrayList<Long?>?>() {}.type
        return gson.fromJson(docList, userListType)
    }

    fun mapRecommendationListDto(recommendation: String): List<RecommendationDto>? {

        val userListType: Type = object : TypeToken<ArrayList<RecommendationDto?>?>() {}.type
        return gson.fromJson(recommendation, userListType)
    }

    fun mapPredefinedResourcesRequiredListDto(predefinedResourcesRequired: String): List<PredefinedResourcesRequiredEntityDto>? {

        val userListType: Type = object : TypeToken<ArrayList<PredefinedResourcesRequiredEntityDto?>?>() {}.type
        return gson.fromJson(predefinedResourcesRequired, userListType)
    }

    fun mapPreliminaryReportDto(
        data: MsPreliminaryReportEntity,
        data2: List<PreliminaryReportItemsDto>
    ): PreliminaryReportDto {
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
            data.approvedStatusHofFinal == 1,
            data.rejectedStatusHofFinal == 1,
            data.approvedStatus == 1,
            data.rejectedStatus == 1,
            data.approvedStatusHodFinal == 1,
            data.rejectedStatusHodFinal == 1,
            data.approvedStatusHod == 1,
            data.rejectedStatusHod == 1,
            data.approvedStatusDirectorFinal == 1,
            data.rejectedStatusDirectorFinal == 1,
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

    fun mapWorkPlanProductsListDto(productList: List<WorkPlanProductsEntity>): Pair<List<WorkPlanProductDto>, Int> {
        var countRecommendationsAdded = 0
        val productListDto = productList.map {
            if (it.hodRecommendationStatus == 1) {
                countRecommendationsAdded = countRecommendationsAdded++
            }
            WorkPlanProductDto(
                it.id,
                it.productName,
                it.productBrand,
                it.referenceNo,
                it.recommendation?.let { it1 -> mapRecommendationListDto(it1) },
                it.destructionRecommended == 1,
                it.hodRecommendationStatus == 1,
                it.hodRecommendationRemarks,
                it.directorRecommendationStatus == 1,
                it.directorRecommendationRemarks,
                it.clientAppealed == 1,
                it.destructionStatus == 1,
                it.appealStatus == 1,
                it.destructionNotificationStatus == 1,
                it.destructionNotificationDocId,
                it.workPlanId,
                it.ssfId,
                it.destructionClientEmail,
                it.destructionClientFullName,
                it.destructionNotificationDate,
                it.destructionDocId,
                it.destructedStatus == 1,
            )
        }

        return Pair(productListDto, countRecommendationsAdded)
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
        return chargeSheetRepo.findByWorkPlanGeneratedID(workPlanInspectionID)
    }

    fun findSeizureDeclarationByWorkPlanInspectionID(
        workPlanInspectionID: Long,
        mainSeizureId: Long
    ): List<MsSeizureEntity>? {
        return seizureDeclarationRepo.findByWorkPlanGeneratedIDAndMainSeizureId(workPlanInspectionID, mainSeizureId)
    }

    fun findSeizureByWorkPlanInspectionID(workPlanInspectionID: Long): List<MsSeizureDeclarationEntity>? {
        return seizureRepo.findByWorkPlanGeneratedID(workPlanInspectionID)
    }

    fun findDataReportByWorkPlanInspectionIDAndID(workPlanInspectionID: Long, ID: Long): MsDataReportEntity? {
        return dataReportRepo.findByWorkPlanGeneratedIDAndId(workPlanInspectionID, ID)
    }

    fun findDataReportListByWorkPlanInspectionID(workPlanInspectionID: Long): List<MsDataReportEntity>? {
        return dataReportRepo.findByWorkPlanGeneratedID(workPlanInspectionID)
    }

    fun findInspectionInvestigationByWorkPlanInspectionID(
        workPlanInspectionID: Long,
        isPreliminaryReport: Int
    ): MsInspectionInvestigationReportEntity? {
        return investInspectReportRepo.findByWorkPlanGeneratedIDAndIsPreliminaryReport(
            workPlanInspectionID,
            isPreliminaryReport
        )
    }

    fun findPreliminaryReportListByWorkPlanInspectionID(
        workPlanInspectionID: Long,
        isPreliminaryReport: Int
    ): List<MsInspectionInvestigationReportEntity>? {
        return investInspectReportRepo.findByIsPreliminaryReportAndWorkPlanGeneratedID(
            isPreliminaryReport,
            workPlanInspectionID
        )
    }

    fun findPreliminaryReportTopVersionByWorkPlanInspectionID(
        workPlanInspectionID: Long,
        isPreliminaryReport: Int
    ): MsInspectionInvestigationReportEntity? {
        return investInspectReportRepo.findTopByWorkPlanGeneratedIDAndIsPreliminaryReportOrderByIdDesc(
            workPlanInspectionID,
            isPreliminaryReport
        )
    }

    fun findDataReportParamsByDataReportID(dataReportID: Long): List<MsDataReportParametersEntity>? {
        return dataReportParameterRepo.findByDataReportId(dataReportID)
    }

    fun findSSFLabParamsBySSFID(SSFID: Long): List<MsLaboratoryParametersEntity>? {
        return ssfLabParametersRepo.findBysampleSubmissionId(SSFID)
    }

    fun findCountiesTownsByWorkPlanID(workPlanInspectionID: Long): List<MsWorkPlanCountiesTownsEntity>? {
        return workPlanCountiesTownsRepo.findAllByWorkPlanId(workPlanInspectionID)
    }

    fun findSampleCollectedDetailByWorkPlanInspectionID(workPlanInspectionID: Long): MsSampleCollectionEntity? {
        return sampleCollectRepo.findByWorkPlanGeneratedID(workPlanInspectionID)
    }

    fun findSampleSubmissionDetailByWorkPlanGeneratedID(workPlanInspectionID: Long): List<MsSampleSubmissionEntity>? {
        return sampleSubmitRepo.findByWorkPlanGeneratedID(workPlanInspectionID)
    }

    fun findSampleSubmissionDetailByWorkPlanGeneratedIDAndSSFID(
        workPlanInspectionID: Long,
        ssfID: Long
    ): MsSampleSubmissionEntity? {
        return sampleSubmitRepo.findByWorkPlanGeneratedIDAndId(workPlanInspectionID, ssfID)
    }

    fun findSampleSubmittedByWorkPlanGeneratedID(workPlanInspectionID: Long): QaSampleSubmissionEntity? {
        return sampleSubmissionLabRepo.findByWorkplanGeneratedId(workPlanInspectionID)
    }

    fun findSampleSubmittedByWorkPlanGeneratedIDAndBsNumber(
        workPlanInspectionID: Long,
        bsNumber: String
    ): QaSampleSubmissionEntity? {
        return sampleSubmissionLabRepo.findByWorkplanGeneratedIdAndBsNumber(workPlanInspectionID, bsNumber)
    }

    fun findPreliminaryReportByWorkPlanGeneratedID(workPlanInspectionID: Long): MsPreliminaryReportEntity? {
        return preliminaryRepo.findByWorkPlanGeneratedID(workPlanInspectionID)
    }

    fun findPreliminaryReportByWorkPlanGeneratedIDAndFinalReportStatus(
        workPlanInspectionID: Long,
        finalReportStatus: Int
    ): MsPreliminaryReportEntity? {
        return preliminaryRepo.findByWorkPlanGeneratedIDAndFinalReportStatus(workPlanInspectionID, finalReportStatus)
    }

}




