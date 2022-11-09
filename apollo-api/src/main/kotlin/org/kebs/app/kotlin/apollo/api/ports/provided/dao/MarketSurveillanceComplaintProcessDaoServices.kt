package org.kebs.app.kotlin.apollo.api.ports.provided.dao

import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.json.JSONObject
import org.kebs.app.kotlin.apollo.api.controllers.qaControllers.ReportsController
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.MarketSurveillanceBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.emailDTO.*
import org.kebs.app.kotlin.apollo.api.ports.provided.lims.LimsServices
import org.kebs.app.kotlin.apollo.common.dto.ApiResponseModel
import org.kebs.app.kotlin.apollo.common.dto.ms.*
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.utils.generateRandomText
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.ms.*
import org.kebs.app.kotlin.apollo.store.repo.IServiceRequestsRepository
import org.kebs.app.kotlin.apollo.store.repo.di.ILaboratoryRepository
import org.kebs.app.kotlin.apollo.store.repo.ms.*
import org.kebs.app.kotlin.apollo.store.repo.qa.IQaSampleLabTestResultsRepository
import org.kebs.app.kotlin.apollo.store.repo.qa.IQaSampleSubmissionRepository
import org.kebs.app.kotlin.apollo.store.repo.qa.IQaSampleSubmittedPdfListRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDate


@Service
class MarketSurveillanceComplaintProcessDaoServices(
    private val applicationMapProperties: ApplicationMapProperties,
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
    private val acknowledgementTimelineViewRepo: IMsAcknowledgementTimelineViewRepository,
    private val complaintFeedbackTimelineViewRepo: IMsComplaintFeedbackViewRepository,
    private val reportSubmittedTimelineViewRepo: IMsReportSubmittedCpViewRepository,
    private val sampleSubmittedTimelineViewRepo: IMsSampleSubmittedCpViewRepository,
    private val commonDaoServices: CommonDaoServices
) {
    final var complaintSteps: Int = 6
    private final val activeStatus: Int = 1

    final var appId = applicationMapProperties.mapMarketSurveillance

    @Lazy
    @Autowired
    lateinit var reportsControllers: ReportsController

    @Lazy
    @Autowired
    lateinit var msWorkPlanDaoServices: MarketSurveillanceWorkPlanDaoServices


    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun saveNewComplaint(body: NewComplaintDto, docFile: List<MultipartFile>?): MSComplaintSubmittedSuccessful {
        try {
            val map = commonDaoServices.serviceMapDetails(appId)
            val msType = findMsTypeDetailsWithUuid(applicationMapProperties.mapMsComplaintTypeUuid)
            val sr: ServiceRequestsEntity
            var payload: String

            val complaint = saveNewComplaint(body.complaintDetails, map, msType, body.customerDetails)
            payload = "${commonDaoServices.createJsonBodyFromEntity(complaint)}"

            val complaintCustomers = saveNewComplaintCustomers(body.customerDetails, map, complaint.second)
            payload += "${commonDaoServices.createJsonBodyFromEntity(complaintCustomers)}"
//
            val complaintLocation = saveNewComplaintLocation(body.locationDetails, body.complaintDetails, map, complaint.second)
            payload += "${commonDaoServices.createJsonBodyFromEntity(complaintLocation)}"

            when {
                docFile!=null -> {
                    saveComplaintFiles(docFile, map, complaint.second)
                }
            }



            val designationsEntity = commonDaoServices.findDesignationByID(applicationMapProperties.mapMsComplaintAndWorkPlanDesignationHOD)
            val regionsEntity =
                body.locationDetails.county?.let {
                    commonDaoServices.findCountiesEntityByCountyId(it, map.activeStatus).regionId?.let {
                        commonDaoServices.findRegionEntityByRegionID(
                            it, map.activeStatus
                        )
                    }
                }
//            val departmentsEntity = complaint.complaintDepartment?.let { commonDaoServices.findDepartmentByID(it) }

            val complaintID = complaint.second.id ?: throw  ExpectedDataNotFound("Complaint ID is missing")
//            val hodID = hod?.id ?: throw  ExpectedDataNotFound("HOD user ID is missing")
            val customerEmail = body.customerDetails.emailAddress ?: throw  ExpectedDataNotFound("Complaint Customers Email is missing")
//            marketSurveillanceBpmn.startMSComplaintProcess(complaintID, hodID, customerEmail)
//                ?: throw  ExpectedDataNotFound("marketSurveillanceBpmn process error")
//
//            marketSurveillanceBpmn.msSubmitComplaintComplete(complaintID, hodID)

//            with(complaint.second) {
//                hodAssigned = hod.id
//            }
            val updatedComplaint = complaint.second
//            complaint = commonDaoServices.updateDetails(updatedComplaint, complaint) as ComplaintEntity
//            complaint.second =updatedComplaint

            sr = commonDaoServices.mapServiceRequestForSuccessUserNotRegistered(
                map,
                payload,
                "${body.customerDetails.firstName} ${body.customerDetails.lastName}"
            )
            val complainantEmailComposed = complaintSubmittedDTOEmailCompose(updatedComplaint)
            runBlocking {
                commonDaoServices.sendEmailWithUserEmail(
                    customerEmail,
                    applicationMapProperties.mapMsComplaintAcknowledgementNotification,
                    complainantEmailComposed,
                    map,
                    sr
                )
            }

            val hod = commonDaoServices.findUserProfileWithDesignationRegionDepartmentAndStatus(
                designationsEntity,
                regionsEntity?: throw ExpectedDataNotFound("No region value In the Complaint Location where [complaint ID =${complaint.second.id}]"),
                map.activeStatus
            )

            runBlocking {

            hod.forEach {hd->
                    val complaintReceivedEmailComposed = hd.userId?.let { complaintReceivedDTOEmailCompose(updatedComplaint, it) }
                    hd.userId?.let {
                        if (complaintReceivedEmailComposed != null) {
                            commonDaoServices.sendEmailWithUserEntity(it, applicationMapProperties.mapMsComplaintSubmittedHodNotification, complaintReceivedEmailComposed, map, sr)
                        }
                    }
                }
            }


            /**
             * TODO: Lets discuss to understand better how to keep track of schedules
             */

            updatedComplaint.let {
                return MSComplaintSubmittedSuccessful(it.referenceNumber,true,"Complaint submitted successful with ref number ${it.referenceNumber}, Check you Email for further Investigation", null)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
            KotlinLogging.logger { }.debug(e.message, e)
            return MSComplaintSubmittedSuccessful(null,false,null, e.message ?: "Unknown Error")
        }
    }

    @PreAuthorize("hasAuthority('MS_IO_READ') or hasAuthority('MS_HOD_READ') or hasAuthority('MS_RM_READ') or hasAuthority('MS_HOF_READ') or hasAuthority('MS_DIRECTOR_READ')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun msComplaintLists(page: PageRequest): ApiResponseModel {
        val response: ApiResponseModel
        val auth = commonDaoServices.loggedInUserAuthentication()
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
//        val userProfile = commonDaoServices.findUserProfileByUserID(loggedInUser)
            when {
                auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_IO_READ" } -> {
                    response = listMsComplaints(complaintsRepo.findByAssignedIo(loggedInUser.id ?: throw ExpectedDataNotFound("Missing Logged In User ID"),page), map)
                }
                auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_HOD_READ" } -> {
                    response = listMsComplaints(complaintsRepo.findByHodAssigned(loggedInUser.id ?: throw ExpectedDataNotFound("Missing Logged In User ID"),page), map)
                }
                auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_RM_READ" } -> {
                    response = listMsComplaints(complaintsRepo.findByHodAssigned(loggedInUser.id ?: throw ExpectedDataNotFound("Missing Logged In User ID"),page), map)
                }
                auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_HOF_READ" } -> {
                    response =  listMsComplaints(complaintsRepo.findByHofAssigned(loggedInUser.id ?: throw ExpectedDataNotFound("Missing Logged In User ID"),page), map)
                }
                auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_DIRECTOR_READ" } -> {
                    response = listMsComplaints(complaintsRepo.findAllByOrderByIdDesc(page), map)
                }
                else -> throw ExpectedDataNotFound("Can't access this page Due to Invalid authority")
            }

        return  response
    }


    @PreAuthorize("hasAuthority('MS_IO_READ') or hasAuthority('MS_HOD_READ') or hasAuthority('MS_RM_READ') or hasAuthority('MS_HOF_READ') or hasAuthority('MS_DIRECTOR_READ')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun msAcknowledgementReportTimeLineLists(page: PageRequest): ApiResponseModel {
        val complaintList = acknowledgementTimelineViewRepo.findAll(page)

        return commonDaoServices.setSuccessResponse(complaintList.toList(),complaintList.totalPages,complaintList.number,complaintList.totalElements)
    }

    @PreAuthorize("hasAuthority('MS_IO_READ') or hasAuthority('MS_HOD_READ') or hasAuthority('MS_RM_READ') or hasAuthority('MS_HOF_READ') or hasAuthority('MS_DIRECTOR_READ')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun msComplaintFeedbackReportTimeLineLists(page: PageRequest): ApiResponseModel {
        val complaintList = complaintFeedbackTimelineViewRepo.findAll(page)

        return commonDaoServices.setSuccessResponse(complaintList.toList(),complaintList.totalPages,complaintList.number,complaintList.totalElements)
    }

    @PreAuthorize("hasAuthority('MS_IO_READ') or hasAuthority('MS_HOD_READ') or hasAuthority('MS_RM_READ') or hasAuthority('MS_HOF_READ') or hasAuthority('MS_DIRECTOR_READ')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun msReportSubmittedReportTimeLineLists(page: PageRequest): ApiResponseModel {
        val complaintList = reportSubmittedTimelineViewRepo.findAll(page)

        return commonDaoServices.setSuccessResponse(complaintList.toList(),complaintList.totalPages,complaintList.number,complaintList.totalElements)
    }

    @PreAuthorize("hasAuthority('MS_IO_READ') or hasAuthority('MS_HOD_READ') or hasAuthority('MS_RM_READ') or hasAuthority('MS_HOF_READ') or hasAuthority('MS_DIRECTOR_READ')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun msSampleSubmittedReportTimeLineLists(page: PageRequest): ApiResponseModel {
        val complaintList = sampleSubmittedTimelineViewRepo.findAll(page)

        return commonDaoServices.setSuccessResponse(complaintList.toList(),complaintList.totalPages,complaintList.number,complaintList.totalElements)
    }
    
    @PreAuthorize("hasAuthority('MS_IO_READ') or hasAuthority('MS_HOD_READ') or hasAuthority('MS_RM_READ') or hasAuthority('MS_HOF_READ') or hasAuthority('MS_DIRECTOR_READ')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun msComplaintMyTaskLists(page: PageRequest): ApiResponseModel {
        val response: ApiResponseModel
        val auth = commonDaoServices.loggedInUserAuthentication()
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val userProfile = commonDaoServices.findUserProfileByUserID(loggedInUser)
            when {
                auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_IO_READ" } -> {
                    response = listMsComplaints(complaintsRepo.findByAssignedIoAndUserTaskId(loggedInUser.id ?: throw ExpectedDataNotFound("Missing Logged In User ID"),applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO,page), map)
                }
                auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_HOD_READ" } -> {
                    response = listMsComplaints(complaintsRepo.findByHodAssignedAndUserTaskId(loggedInUser.id ?: throw ExpectedDataNotFound("Missing Logged In User ID"),applicationMapProperties.mapMSCPWorkPlanUserTaskNameHodRm,page), map)
                }
                 auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_RM_READ" } -> {
                    response = listMsComplaints(complaintsRepo.findByHodAssignedAndUserTaskId(loggedInUser.id ?: throw ExpectedDataNotFound("Missing Logged In User ID"),applicationMapProperties.mapMSCPWorkPlanUserTaskNameHodRm,page), map)
                }
                auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_HOF_READ" } -> {
                    response =  listMsComplaints(complaintsRepo.findByHofAssignedAndUserTaskId(loggedInUser.id ?: throw ExpectedDataNotFound("Missing Logged In User ID"),applicationMapProperties.mapMSCPWorkPlanUserTaskNameHof,page), map)
                }
                auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_DIRECTOR_READ" } -> {
                    response = listMsComplaints(complaintsRepo.findAllByOrderByIdDesc(page), map)
                }
                else -> throw ExpectedDataNotFound("Can't access this page Due to Invalid authority")
            }

        return  response
    }
    
    @PreAuthorize("hasAuthority('MS_IO_READ') or hasAuthority('MS_HOD_READ') or hasAuthority('MS_RM_READ') or hasAuthority('MS_HOF_READ') or hasAuthority('MS_DIRECTOR_READ')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun msComplaintNewLists(page: PageRequest): ApiResponseModel {
        val response: ApiResponseModel
        val auth = commonDaoServices.loggedInUserAuthentication()
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val userProfile = commonDaoServices.findUserProfileByUserID(loggedInUser)
        when {
            auth.authorities.stream().anyMatch { authority ->
                authority.authority == "MS_IO_READ"
                        || authority.authority == "MS_HOD_READ"
                        || authority.authority == "MS_HOF_READ"
                        || authority.authority == "MS_DIRECTOR_READ"
                        || authority.authority == "MS_RM_READ" } -> {
                val complaintList = complaintsRepo.findNewComplaint(applicationMapProperties.mapMSCPWorkPlanUserTaskNameHodRm, applicationMapProperties.msComplaintProcessOnlineSubmitted?: throw ExpectedDataNotFound("Missing Process ID, Complaint Submitted"),userProfile.regionId?.id ?: throw ExpectedDataNotFound("Missing Logged In Region ID"),userProfile.countyID?.id ?: throw ExpectedDataNotFound("Missing Logged In County ID"))
                val usersPage: Page<ComplaintEntity> = PageImpl(complaintList, page, complaintList.size.toLong())
                response = listMsComplaints(usersPage,map)
            }
            else -> throw ExpectedDataNotFound("Can't access this page Due to Invalid authority")
        }

        return  response
    }

    @PreAuthorize("hasAuthority('MS_IO_READ') or hasAuthority('MS_HOD_READ') or hasAuthority('MS_RM_READ') or hasAuthority('MS_HOF_READ') or hasAuthority('MS_DIRECTOR_READ')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun msComplaintAllCompletedLists(page: PageRequest): ApiResponseModel {
        val response: ApiResponseModel
        val auth = commonDaoServices.loggedInUserAuthentication()
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val userProfile = commonDaoServices.findUserProfileByUserID(loggedInUser)
        when {
            auth.authorities.stream().anyMatch { authority ->
                authority.authority == "MS_IO_READ"
                        || authority.authority == "MS_HOD_READ"
                        || authority.authority == "MS_HOF_READ"
                        || authority.authority == "MS_DIRECTOR_READ"
                        || authority.authority == "MS_RM_READ" } -> {
                val complaintList = complaintsRepo.findAllByMsComplaintEndedStatusOrderByIdDesc(map.activeStatus, page)
                response = listMsComplaints(complaintList,map)
            }
            else -> throw ExpectedDataNotFound("Can't access this page Due to Invalid authority")
        }

        return  response
    }
    
    @PreAuthorize("hasAuthority('MS_IO_READ') or hasAuthority('MS_HOD_READ') or hasAuthority('MS_RM_READ') or hasAuthority('MS_HOF_READ') or hasAuthority('MS_DIRECTOR_READ')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun msComplaintOnGoingLists(page: PageRequest): ApiResponseModel {
        val response: ApiResponseModel
        val auth = commonDaoServices.loggedInUserAuthentication()
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val userProfile = commonDaoServices.findUserProfileByUserID(loggedInUser)
        when {
            auth.authorities.stream().anyMatch { authority ->
                authority.authority == "MS_IO_READ"
                        || authority.authority == "MS_HOD_READ"
                        || authority.authority == "MS_HOF_READ"
                        || authority.authority == "MS_DIRECTOR_READ"
                        || authority.authority == "MS_RM_READ" } -> {
                val complaintList = complaintsRepo.findOngoingTask(userProfile.regionId?.id ?: throw ExpectedDataNotFound("Missing Logged In Region ID"),userProfile.countyID?.id ?: throw ExpectedDataNotFound("Missing Logged In County ID"))
                val usersPage: Page<ComplaintEntity> = PageImpl(complaintList, page, complaintList.size.toLong())
                response = listMsComplaints(usersPage,map)
            }
            else -> throw ExpectedDataNotFound("Can't access this page Due to Invalid authority")
        }

        return  response
    }

    @PreAuthorize("hasAuthority('MS_IO_READ') or hasAuthority('MS_HOD_READ') or hasAuthority('MS_RM_READ') or hasAuthority('MS_HOF_READ') or hasAuthority('MS_DIRECTOR_READ')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun getComplaintDetailsBasedOnRefNo(referenceNo: String): AllComplaintsDetailsDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        val comp = findComplaintByRefNumber(referenceNo)
        return complaintInspectionMappingCommonDetails(comp, map)
    }

    @PreAuthorize("hasAuthority('MS_HOD_MODIFY') or hasAuthority('MS_RM_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun updateComplaintAcceptStatus(
        referenceNo: String,
        body: ComplaintApproveDto
    ): AllComplaintsDetailsDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        val complaintFound = findComplaintByRefNumber(referenceNo)

        with(complaintFound) {
            hodAssigned = loggedInUser.id
            msProcessId = applicationMapProperties.msComplaintProcessMandate
            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameHodRm
            complaintDepartment = body.department
            division = body.division
            approved = map.activeStatus
            approvedRemarks = body.approvedRemarks
            approvedBy = commonDaoServices.getUserName(loggedInUser)
            approvedDate = commonDaoServices.getCurrentDate()
        }

        val remarksDto = RemarksToAddDto()
        with(remarksDto){
            remarksDescription= body.approvedRemarks
            remarksStatus= "APPROVED"
            processID = complaintFound.msProcessId
            userId= loggedInUser.id
        }

        val complaintUpdated = updateComplaintDetailsInDB(complaintFound, map, loggedInUser)

        when (complaintUpdated.first.status) {
            map.successStatus -> {
                val remarksSaved = fuelAddRemarksDetails(complaintFound.id?: throw ExpectedDataNotFound("Missing Complaint ID"),remarksDto, map, loggedInUser)
                 when (remarksSaved.first.status) {
                    map.successStatus -> {

                        return complaintInspectionMappingCommonDetails(complaintUpdated.second, map)
                    }
                    else -> {
                        throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(remarksSaved.first))
                    }
                }

            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(complaintUpdated.first))
            }
        }

    }

    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun createNewWorkPlanScheduleFromComplaint(referenceNo: String,body: WorkPlanEntityDto): AllComplaintsDetailsDto {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val msType = findMsTypeDetailsWithUuid(applicationMapProperties.mapMsComplaintTypeUuid)
        val complaintFound = findComplaintByRefNumber(referenceNo)
        val currentYear = msWorkPlanDaoServices.getCurrentYear()
        val workPlanYearCodes = msWorkPlanDaoServices.findWorkPlanYearsCodesEntity(currentYear, map)
        var userWorkPlan = msWorkPlanDaoServices.findWorkPlanCreatedComplaintEntity(loggedInUser, workPlanYearCodes, map.activeStatus)
//        val checkCreationDate = msWorkPlanDaoServices.isWithinRange(commonDaoServices.getCurrentDate(), workPlanYearCodes)
        return when {
            userWorkPlan != null -> {
                allWorkPlanComplaintsDetailsDto(body, complaintFound, msType, userWorkPlan, map, loggedInUser)
            }
            else -> {
                userWorkPlan = msWorkPlanDaoServices.createWorkPlanYear(loggedInUser, map, workPlanYearCodes,true).second
                allWorkPlanComplaintsDetailsDto(body, complaintFound, msType, userWorkPlan, map, loggedInUser)

            }
        }

    }


    fun allWorkPlanComplaintsDetailsDto(
        body: WorkPlanEntityDto,
        complaintFound: ComplaintEntity,
        msType: MsTypesEntity,
        userWorkPlan: WorkPlanCreatedEntity,
        map: ServiceMapsEntity,
        loggedInUser: UsersEntity
    ): AllComplaintsDetailsDto {
        val fileSaved = msWorkPlanDaoServices.saveNewWorkPlanActivityFromComplaint(body, complaintFound, msType, userWorkPlan, map, loggedInUser)
        when (fileSaved.first.status) {
            map.successStatus -> {
                with(complaintFound) {
                    msProcessId = applicationMapProperties.msComplaintProcessStarted
                    userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
                    msProcessStatus = map.activeStatus
                }
                val complaintUpdated = updateComplaintDetailsInDB(complaintFound, map, loggedInUser).second
                with(userWorkPlan) {
                    endedDate = commonDaoServices.getCurrentDate()
                    endedStatus = map.activeStatus
                    status = map.activeStatus
                    batchClosed = map.activeStatus
                    userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameHodRm
                }

                msWorkPlanDaoServices.updateWorkPlanBatch(userWorkPlan, map, loggedInUser)

                return complaintInspectionMappingCommonDetails(complaintUpdated, map)
            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved.first))
            }
        }
    }


    @PreAuthorize("hasAuthority('MS_HOD_MODIFY') or hasAuthority('MS_RM_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun updateComplaintRejectStatus(
        referenceNo: String,
        body: ComplaintRejectDto
    ): AllComplaintsDetailsDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        val complaintFound = findComplaintByRefNumber(referenceNo)

        with(complaintFound) {
            hodAssigned = loggedInUser.id
            msProcessId = applicationMapProperties.msComplaintProcessRejected
            userTaskId = null
            msComplaintEndedStatus = map.activeStatus
//            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameComplainant
            rejected = map.activeStatus
            rejectedRemarks = body.rejectedRemarks
            rejectedBy = commonDaoServices.getUserName(loggedInUser)
            rejectedDate = commonDaoServices.getCurrentDate()
        }
        val remarksDto = RemarksToAddDto()
        with(remarksDto){
            remarksDescription= body.rejectedRemarks
            remarksStatus= "REJECTED"
            processID = complaintFound.msProcessId
            userId= loggedInUser.id
        }

        val complaintUpdated = updateComplaintDetailsInDB(complaintFound, map, loggedInUser)

        when (complaintUpdated.first.status) {
            map.successStatus -> {
                val remarksSaved = fuelAddRemarksDetails(complaintFound.id?: throw ExpectedDataNotFound("Missing Complaint ID"),remarksDto, map, loggedInUser)
                 when (remarksSaved.first.status) {
                    map.successStatus -> {
                        val complainantEmailComposed = complaintRejectedDTOEmailCompose(complaintUpdated.second)
                        runBlocking {
                            commonDaoServices.sendEmailWithUserEmail(
                                findComplaintCustomerByComplaintID(complaintFound.id?: throw ExpectedDataNotFound("Missing Complaint ID")).emailAddress?: throw ExpectedDataNotFound("Missing Complainant Email Address"),
                                applicationMapProperties.mapMsComplaintAcknowledgementRejectionNotification,
                                complainantEmailComposed,
                                map,
                                complaintUpdated.first
                            )
                        }
                        return complaintInspectionMappingCommonDetails(complaintUpdated.second, map)
                    }
                    else -> {
                        throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(remarksSaved.first))
                    }
                }

            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(complaintUpdated.first))
            }
        }

    }

    @PreAuthorize("hasAuthority('MS_HOD_MODIFY') or hasAuthority('MS_RM_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun updateComplaintForAmendmentByUser(
        referenceNo: String,
        body: ComplaintAdviceRejectDto
    ): AllComplaintsDetailsDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        val complaintFound = findComplaintByRefNumber(referenceNo)

        with(complaintFound) {
            hodAssigned = loggedInUser.id
            msProcessId = applicationMapProperties.msComplaintProcessNotMandate
            userTaskId = null
            msComplaintEndedStatus = map.activeStatus
//            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameComplainant
            amendmentStatus = map.activeStatus
            amendmentRemarks = body.amendmentRemarks
            rejectedRemarks = body.rejectedRemarks
            rejectedBy = commonDaoServices.getUserName(loggedInUser)
            rejectedDate = commonDaoServices.getCurrentDate()
        }

        val remarksDto = RemarksToAddDto()
        with(remarksDto){
            remarksDescription= body.rejectedRemarks
            remarksStatus= "REJECTED FOR AMENDMENT"
            processID = complaintFound.msProcessId
            userId= loggedInUser.id
        }

        val complaintUpdated = updateComplaintDetailsInDB(complaintFound, map, loggedInUser)

        when (complaintUpdated.first.status) {
            map.successStatus -> {
                val remarksSaved = fuelAddRemarksDetails(complaintFound.id?: throw ExpectedDataNotFound("Missing Complaint ID"),remarksDto, map, loggedInUser)
                 when (remarksSaved.first.status) {
                    map.successStatus -> {
                        val complainantEmailComposed = complaintRejectedWithAdviceOGADTOEmailCompose(complaintUpdated.second)
                        runBlocking {
                            commonDaoServices.sendEmailWithUserEmail(
                                findComplaintCustomerByComplaintID(complaintFound.id?: throw ExpectedDataNotFound("Missing Complaint ID")).emailAddress?: throw ExpectedDataNotFound("Missing Complainant Email Address"),
                                applicationMapProperties.mapMsComplaintAcknowledgementRejectionRejectedForAmendmentNotification,
                                complainantEmailComposed,
                                map,
                                complaintUpdated.first
                            )
                        }
                        return complaintInspectionMappingCommonDetails(complaintUpdated.second, map)
                    }
                    else -> {
                        throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(remarksSaved.first))
                    }
                }

            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(complaintUpdated.first))
            }
        }

    }

    @PreAuthorize("hasAuthority('MS_HOD_MODIFY') or hasAuthority('MS_RM_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun updateComplaintMandateForOgaStatus(
        referenceNo: String,
        body: ComplaintAdviceRejectDto
    ): AllComplaintsDetailsDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        val complaintFound = findComplaintByRefNumber(referenceNo)

        with(complaintFound) {
            hodAssigned = loggedInUser.id
            msProcessId = applicationMapProperties.msComplaintProcessNotMandate
            userTaskId = null
            msComplaintEndedStatus = map.activeStatus
//            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameComplainant
            mandateForOga = map.activeStatus
            advisedWhereto = body.advisedWhereToRemarks
            rejectedRemarks = body.rejectedRemarks
            rejectedBy = commonDaoServices.getUserName(loggedInUser)
            rejectedDate = commonDaoServices.getCurrentDate()
        }

        val remarksDto = RemarksToAddDto()
        with(remarksDto){
            remarksDescription= body.rejectedRemarks
            remarksStatus= "REJECTED/OGA MANDATE"
            processID = complaintFound.msProcessId
            userId= loggedInUser.id
        }

        val complaintUpdated = updateComplaintDetailsInDB(complaintFound, map, loggedInUser)

        when (complaintUpdated.first.status) {
            map.successStatus -> {
                val remarksSaved = fuelAddRemarksDetails(complaintFound.id?: throw ExpectedDataNotFound("Missing Complaint ID"),remarksDto, map, loggedInUser)
                 when (remarksSaved.first.status) {
                    map.successStatus -> {
                        val complainantEmailComposed = complaintRejectedWithAdviceOGADTOEmailCompose(complaintUpdated.second)
                        runBlocking {
                            commonDaoServices.sendEmailWithUserEmail(
                                findComplaintCustomerByComplaintID(complaintFound.id?: throw ExpectedDataNotFound("Missing Complaint ID")).emailAddress?: throw ExpectedDataNotFound("Missing Complainant Email Address"),
                                applicationMapProperties.mapMsComplaintAcknowledgementRejectionWIthOGANotification,
                                complainantEmailComposed,
                                map,
                                complaintUpdated.first
                            )
                        }
                        return complaintInspectionMappingCommonDetails(complaintUpdated.second, map)
                    }
                    else -> {
                        throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(remarksSaved.first))
                    }
                }

            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(complaintUpdated.first))
            }
        }

    }

    @PreAuthorize("hasAuthority('MS_HOD_MODIFY') or hasAuthority('MS_RM_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun updateComplaintReAssignRegionStatus(
        referenceNo: String,
        body: RegionReAssignDto
    ): AllComplaintsDetailsDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        val complaintFound = findComplaintByRefNumber(referenceNo)
        var complaintLocationDetails = findComplaintLocationByComplaintID(complaintFound.id?: throw ExpectedDataNotFound("Missing complaint ID"))

        with(complaintLocationDetails) {
            county = body.countyID
            town = body.townID
            region = county?.let { commonDaoServices.findCountiesEntityByCountyId(it, map.activeStatus).regionId }
        }
        complaintLocationDetails = complaintLocationRepo.save(complaintLocationDetails)

//        with(complaintFound){
//            msProcessId = applicationMapProperties.msComplaintProcessAssignHOF
//            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameHof
//        }

        val remarksDto = RemarksToAddDto()
        with(remarksDto){
            remarksDescription= body.reassignedRemarks
            remarksStatus= "N/A"
//            processID = complaintFound.msProcessId
            userId= loggedInUser.id
        }

        val complaintUpdated = updateComplaintDetailsInDB(complaintFound, map, loggedInUser)

        when (complaintUpdated.first.status) {
            map.successStatus -> {
                val remarksSaved = fuelAddRemarksDetails(complaintFound.id?: throw ExpectedDataNotFound("Missing Complaint ID"),remarksDto, map, loggedInUser)
                 when (remarksSaved.first.status) {
                    map.successStatus -> {
                        return complaintInspectionMappingCommonDetails(complaintUpdated.second, map)
                    }
                    else -> {
                        throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(remarksSaved.first))
                    }
                }

            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(complaintUpdated.first))
            }
        }

    }

    @PreAuthorize("hasAuthority('MS_HOD_MODIFY') or hasAuthority('MS_RM_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun updateComplaintAssignHOFStatus(
        referenceNo: String,
        body: ComplaintAssignDto
    ): AllComplaintsDetailsDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        val complaintFound = findComplaintByRefNumber(referenceNo)

        val hofDetailsFound =commonDaoServices.findUserByID(body.assignedIo?: throw ExpectedDataNotFound("Missing Assigned HOF ID"))
        with(complaintFound) {
            timelineStartDate = commonDaoServices.getCurrentDate()
            timelineEndDate = applicationMapProperties.msComplaintProcessAssignHOF?.let {timeLine->
                findMsProcessComplaintByID(1, timeLine )?.timelinesDay}?.let {daysCount->
                commonDaoServices.addDaysSkippingWeekends(LocalDate.now(), daysCount)?.let {daysConvert-> commonDaoServices.localDateToTimestamp(daysConvert) }
            }
            msProcessId = applicationMapProperties.msComplaintProcessAssignHOF
            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameHof
            hofAssigned = hofDetailsFound.id
            msHofAssignedDate = commonDaoServices.getCurrentDate()
            assignedBy = commonDaoServices.getUserName(loggedInUser)
        }

        val remarksDto = RemarksToAddDto()
        with(remarksDto){
            remarksDescription= body.assignedRemarks
            remarksStatus= "N/A"
            processID = complaintFound.msProcessId
            userId= loggedInUser.id
        }

        val complaintUpdated = updateComplaintDetailsInDB(complaintFound, map, loggedInUser)

        when (complaintUpdated.first.status) {
            map.successStatus -> {
                val remarksSaved = fuelAddRemarksDetails(complaintFound.id?: throw ExpectedDataNotFound("Missing Complaint ID"),remarksDto, map, loggedInUser)
                 when (remarksSaved.first.status) {
                    map.successStatus -> {
                        runBlocking {

                            val complaintReceivedEmailComposed = complaintReceivedDTOEmailCompose(complaintUpdated.second, hofDetailsFound)
                            commonDaoServices.sendEmailWithUserEntity(
                                hofDetailsFound,
                                applicationMapProperties.mapMsComplaintApprovedHofNotification,
                                complaintReceivedEmailComposed,
                                map,
                                complaintUpdated.first
                            )
                        }
                        return complaintInspectionMappingCommonDetails(complaintUpdated.second, map)
                    }
                    else -> {
                        throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(remarksSaved.first))
                    }
                }

            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(complaintUpdated.first))
            }
        }

    }

    @PreAuthorize("hasAuthority('MS_HOF_MODIFY') or hasAuthority('MS_HOD_MODIFY') or hasAuthority('MS_RM_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun updateComplaintAssignIOStatus(
        referenceNo: String,
        body: ComplaintAssignDto
    ): AllComplaintsDetailsDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        val complaintFound = findComplaintByRefNumber(referenceNo)

        with(complaintFound) {
            timelineStartDate = commonDaoServices.getCurrentDate()
            timelineEndDate = applicationMapProperties.msComplaintProcessAssignOfficer?.let {timeLine->
                findMsProcessComplaintByID(1, timeLine )?.timelinesDay}?.let {daysCount->
                commonDaoServices.addDaysSkippingWeekends(LocalDate.now(), daysCount)?.let {daysConvert-> commonDaoServices.localDateToTimestamp(daysConvert) }
            }
            msProcessId = applicationMapProperties.msComplaintProcessAssignOfficer
            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
            assignedRemarks = body.assignedRemarks
            assignedIoStatus = map.activeStatus
            assignedIo = commonDaoServices.findUserByID(body.assignedIo?: throw ExpectedDataNotFound("Missong Assigned IO ID")).id
            assignedDate = commonDaoServices.getCurrentDate()
            assignedBy = commonDaoServices.getUserName(loggedInUser)
        }

        val remarksDto = RemarksToAddDto()
        with(remarksDto){
            remarksDescription= body.assignedRemarks
            remarksStatus= "N/A"
            processID = complaintFound.msProcessId
            userId= loggedInUser.id
        }

        val complaintUpdated = updateComplaintDetailsInDB(complaintFound, map, loggedInUser)

        when (complaintUpdated.first.status) {
            map.successStatus -> {
                val remarksSaved = fuelAddRemarksDetails(complaintFound.id?: throw ExpectedDataNotFound("Missing Complaint ID"),remarksDto, map, loggedInUser)
                 when (remarksSaved.first.status) {
                    map.successStatus -> {
                        return complaintInspectionMappingCommonDetails(complaintUpdated.second, map)
                    }
                    else -> {
                        throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(remarksSaved.first))
                    }
                }

            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(complaintUpdated.first))
            }
        }

    }

    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun updateComplaintByAddingClassification(
        referenceNo: String,
        body: ComplaintClassificationDto
    ): AllComplaintsDetailsDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        val complaintFound = findComplaintByRefNumber(referenceNo)

        with(complaintFound) {
            msProcessId = applicationMapProperties.msComplaintProcessClassificationDetails
            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
            standardCategory = body.productClassification
            broadProductCategory = body.broadProductCategory
            productCategory = body.productCategory
            product = body.myProduct
            productSubCategory = body.productSubcategory
            classificationDetailsStatus = map.activeStatus
        }

        val remarksDto = RemarksToAddDto()
        with(remarksDto){
            remarksDescription= body.classificationRemarks
            remarksStatus= "N/A"
            processID = complaintFound.msProcessId
            userId= loggedInUser.id
        }

        val complaintUpdated = updateComplaintDetailsInDB(complaintFound, map, loggedInUser)

        when (complaintUpdated.first.status) {
            map.successStatus -> {
                val remarksSaved = fuelAddRemarksDetails(complaintFound.id?: throw ExpectedDataNotFound("Missing Complaint ID"),remarksDto, map, loggedInUser)
                 when (remarksSaved.first.status) {
                    map.successStatus -> {
                        return complaintInspectionMappingCommonDetails(complaintUpdated.second, map)
                    }
                    else -> {
                        throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(remarksSaved.first))
                    }
                }

            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(complaintUpdated.first))
            }
        }

    }


    fun fuelAddRemarksDetails(
        cpID: Long,
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
                complaintId = cpID
                msProcessId = body.processID
                status = map.activeStatus
                createdBy = commonDaoServices.concatenateName(user)
                createdOn = commonDaoServices.getTimestamp()
            }
            remarks = remarksRepo.save(remarks)

            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(remarks)} "
            sr.names = "Complaint process steps Remarks Save file"

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


    fun msComplaintMappedDetails(map: ServiceMapsEntity, complaintApproveDto: ComplaintApproveDto?,
                                 complaintRejectDto: ComplaintRejectDto?,
                                 complaintAdviceRejectDto: ComplaintAdviceRejectDto?,
                                 complaintAssignDto: ComplaintAssignDto?): ComplaintApproveRejectAssignDto {
        val comp = ComplaintApproveRejectAssignDto()
        with(comp) {
            when {
                complaintApproveDto != null -> {
                    division = complaintApproveDto.division
                    approved = map.activeStatus
                    approvedRemarks = complaintApproveDto.approvedRemarks
                }
                complaintRejectDto != null -> {
                    rejected = map.activeStatus
                    rejectedRemarks = complaintRejectDto.rejectedRemarks
                }
                complaintAdviceRejectDto != null -> {
                    rejected = map.activeStatus
                    mandateForOga = map.activeStatus
                    advisedWhereToRemarks = complaintAdviceRejectDto.advisedWhereToRemarks
                    rejectedRemarks = complaintAdviceRejectDto.rejectedRemarks
                }
                complaintAssignDto != null -> {
                    assignedIo = complaintAssignDto.assignedIo
                    assignedIoRemarks = complaintAssignDto.assignedRemarks
                    assignedIoStatus = map.activeStatus
                }
            }

        }
        return comp
    }


    fun returnAssignDetails(complaintLocationDetails: ComplaintLocationEntity?, map: ServiceMapsEntity, complaintID: Long, comp: ComplaintEntity): Triple<DesignationsEntity, RegionsEntity, DepartmentsEntity> {
        val designationsEntity = commonDaoServices.findDesignationByID(applicationMapProperties.mapMsComplaintAndWorkPlanDesignationHOF)
        val regionsEntity = complaintLocationDetails?.region?.let { commonDaoServices.findRegionEntityByRegionID(it, map.activeStatus) }
            ?: throw ExpectedDataNotFound("Region Details for Complaint location With Complaint ID $complaintID, does not exist")
        val departmentsEntity = comp.complaintDepartment?.let { commonDaoServices.findDepartmentByID(it) }
            ?: throw ExpectedDataNotFound("Department details for complaint With ID $complaintID, does not Exist")
        return Triple(designationsEntity, regionsEntity, departmentsEntity)
    }

    fun complaintReceivedDTOEmailCompose(dataDetails: ComplaintEntity, user: UsersEntity): ComplaintAssignedDTO {
        val dataValue = ComplaintAssignedDTO()
        with(dataValue) {
            refNumber = dataDetails.referenceNumber
            baseUrl = applicationMapProperties.baseUrlValue
            fullName = commonDaoServices.concatenateName(user)
            assignIORemarks = dataDetails.assignedRemarks
            assignHOFRemarks = dataDetails.approvedRemarks
//            commentRemarks = dataDetails.approvedRemarks
            dateSubmitted = dataDetails.transactionDate
        }

        return dataValue
    }

    fun complaintRejectedDTOEmailCompose(dataDetails: ComplaintEntity): CustomerComplaintRejectedDTO {
        val dataValue = CustomerComplaintRejectedDTO()
        with(dataValue) {
            refNumber = dataDetails.referenceNumber
            baseUrl = applicationMapProperties.baseUrlValue
            complaintTitle = dataDetails.complaintTitle
            fullName = dataDetails.createdBy
            commentRemarks = dataDetails.rejectedRemarks
            dateSubmitted = dataDetails.transactionDate
        }

        return dataValue
    }

    fun complaintRejectedWithAdviceOGADTOEmailCompose(dataDetails: ComplaintEntity): CustomerComplaintRejectedWIthOGADTO {
        val dataValue = CustomerComplaintRejectedWIthOGADTO()
        with(dataValue) {
            refNumber = dataDetails.referenceNumber
            baseUrl = applicationMapProperties.baseUrlValue
            complaintTitle = dataDetails.complaintTitle
            fullName = dataDetails.createdBy
            commentRemarks = dataDetails.rejectedRemarks
            adviceRemarks = dataDetails.advisedWhereto
            dateSubmitted = dataDetails.transactionDate
        }

        return dataValue
    }

    fun getUserWithProfile(departmentsEntity: DepartmentsEntity, regionsEntity: RegionsEntity, designationsEntity: DesignationsEntity, map: ServiceMapsEntity): UsersEntity {
        return commonDaoServices.findUserProfileWithDesignationRegionDepartmentAndStatus(designationsEntity, regionsEntity, departmentsEntity, map.activeStatus).userId
            ?: throw ExpectedDataNotFound("NO ${designationsEntity.designationName} Found on REGION ${regionsEntity.region} and Department ${departmentsEntity.department} Whose status is ${map.activeStatus}")
    }

    fun listMsComplaints(complaints: Page<ComplaintEntity>, map: ServiceMapsEntity): ApiResponseModel {
        val complaintList = mutableListOf<ComplaintsListDto>()
        complaints.toList().map { comp ->
            complaintList.add(
                ComplaintsListDto(
                    comp.referenceNumber,
                    comp.complaintTitle,
                    comp.targetedProducts,
//                    comp.complaintCategory,
                    comp.transactionDate,
                    comp.msProcessId?.let { findMsProcessComplaintByID(1, it)?.processName }
                )
            )
        }

        return commonDaoServices.setSuccessResponse(complaintList,complaints.totalPages,complaints.number,complaints.totalElements)
    }


    fun saveNewComplaint(complaintDto: ComplaintDto,
                         map: ServiceMapsEntity,
                         msType: MsTypesEntity,
                         complaintCustomersDto: ComplaintCustomersDto
    ): Pair<ServiceRequestsEntity, ComplaintEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var complaint = ComplaintEntity()
        try {

            with(complaint) {
                complaintTitle = complaintDto.complaintTitle
                complaintDetails = complaintDto.complaintDescription
                complaintSampleDetails = complaintDto.complaintSampleDetails
                remedySought = complaintDto.remedySought
                targetedProducts = complaintDto.productBrand
//                complaintDepartment = complaintDto.complaintCategory
//                standardCategory = complaintDto.productClassification
//                broadProductCategory = complaintDto.broadProductCategory
//                productCategory = complaintDto.productCategory
//                product = complaintDto.myProduct
//                productSubCategory = complaintDto.productSubcategory

                uuid = commonDaoServices.generateUUIDString()
                msTypeId = msType.id
                transactionDate = commonDaoServices.getCurrentDate()
                status = map.initStatus
                createdBy = complaintCustomersDto.firstName?.let { complaintCustomersDto.lastName?.let { it1 -> commonDaoServices.concatenateName(it, it1) } }
                createdOn = commonDaoServices.getTimestamp()
                submissionDate = commonDaoServices.getTimestamp()
                serviceMapsId = map.id
                msProcessStatus = map.inactiveStatus
                timelineStartDate = commonDaoServices.getCurrentDate()
                timelineEndDate = applicationMapProperties.msComplaintProcessOnlineSubmitted?.let {timeLine->
                    findMsProcessComplaintByID(1, timeLine )?.timelinesDay}?.let {daysCount->
                    commonDaoServices.addDaysSkippingWeekends(LocalDate.now(), daysCount)?.let {daysConvert-> commonDaoServices.localDateToTimestamp(daysConvert) }
                }
                msProcessId = applicationMapProperties.msComplaintProcessOnlineSubmitted
                userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameHodRm
                progressValue = progressSteps(complaintSteps).getInt("step-1")
                progressStep = "COMPLAINT SUBMITTED"
                referenceNumber = "${msType.markRef}${generateRandomText(7, map.secureRandom, map.messageDigestAlgorithm, true)}".toUpperCase()
            }
            complaint = complaintsRepo.save(complaint)

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(complaintDto)}"
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepo.save(sr)

        }
        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return Pair(sr, complaint)
    }

    fun saveComplaintProcess(complaintFound: ComplaintEntity,
                         map: ServiceMapsEntity,
                         processValueID: Long, user: UsersEntity
    ): Pair<ServiceRequestsEntity, ComplaintEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var complaint = complaintFound
        try {

            with(complaint) {
                msProcessId = processValueID
                modifiedBy = commonDaoServices.concatenateName(user)
                modifiedOn = commonDaoServices.getTimestamp()
            }
            complaint = complaintsRepo.save(complaint)

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(complaint)}"
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepo.save(sr)

        }
        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return Pair(sr, complaint)
    }

    fun saveComplaintFiles(
        docFile: List<MultipartFile>,
        map: ServiceMapsEntity,
        complaintDetails: ComplaintEntity
    ) {
        val savedAllFilesUploads =  false
        docFile.forEach { fileDoc ->
            var sr = commonDaoServices.createServiceRequest(map)
            try {
                var upload = MsUploadsEntity()
                with(upload) {
                    msComplaintId = complaintDetails.id
                    complaintUploads = 1
                    ordinaryStatus = 0
                    versionNumber = 1
                    name = fileDoc.originalFilename
                    fileType = fileDoc.contentType
                    documentType = "COMPLAINT FILE"
                    document = fileDoc.bytes
                    transactionDate = commonDaoServices.getCurrentDate()
                    status = 1
                    createdBy = complaintDetails.createdBy
                    createdOn = commonDaoServices.getTimestamp()
                }
                upload = msUploadRepo.save(upload)
            } catch (e: Exception) {
                KotlinLogging.logger { }.error(e.message, e)
                //            KotlinLogging.logger { }.trace(e.message, e)
                sr.payload = "${fileDoc.bytes}"
                sr.status = sr.serviceMapsId?.exceptionStatus
                sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
                sr.responseMessage = e.message
                sr = serviceRequestsRepo.save(sr)

            }
            KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        }
    }

    fun updateComplaintDetailsInDB(
        updateCD: ComplaintEntity,
       map: ServiceMapsEntity,
       user: UsersEntity):
    Pair<ServiceRequestsEntity, ComplaintEntity> {
        var sr = commonDaoServices.createServiceRequest(map)
        var updatedComplaint = updateCD
        try {
            with(updatedComplaint) {
                modifiedBy = commonDaoServices.concatenateName(user)
                modifiedOn = commonDaoServices.getTimestamp()
            }
            KotlinLogging.logger { }.info { "MY UPDATED COMPLAINT WITH ID =  ${updateCD.id}" }
            updatedComplaint = complaintsRepo.save(updateCD)
            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(updatedComplaint)} "
            sr.names = "Complaint Updated successfully file"

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = map.successStatus
            sr = serviceRequestsRepo.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(updatedComplaint)}"
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepo.save(sr)

        }
        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return Pair(sr, updatedComplaint)

    }

    fun getComplaintID(comp: ComplaintEntity, refNumber: String): Long {
        return comp.id ?: throw ExpectedDataNotFound("Complaint ID IS NULL for complaint with ref number $refNumber")
    }

    fun saveNewComplaintCustomers(complaintCustomersDto: ComplaintCustomersDto, map: ServiceMapsEntity, complaint: ComplaintEntity): ComplaintCustomersEntity {
        var complaintCustomers = ComplaintCustomersEntity()

        with(complaintCustomers) {
            firstName = complaintCustomersDto.firstName
            lastName = complaintCustomersDto.lastName
            mobilePhoneNumber = complaintCustomersDto.phoneNumber
            emailAddress = complaintCustomersDto.emailAddress
            postalAddress = complaintCustomersDto.postalAddress
            physicalAddress = complaintCustomersDto.physicalAddress
            transactionDate = commonDaoServices.getCurrentDate()
            status = map.activeStatus
            createdBy = complaintCustomersDto.firstName?.let { complaintCustomersDto.lastName?.let { it1 -> commonDaoServices.concatenateName(it, it1) } }
            createdOn = commonDaoServices.getTimestamp()
            complaintId = complaint.id
        }
        complaintCustomers = complaintCustomersRepo.save(complaintCustomers)
        return complaintCustomers
    }

    fun saveNewComplaintLocation(complaintLocationDto: ComplaintLocationDto, complaintDto: ComplaintDto, map: ServiceMapsEntity, complaint: ComplaintEntity): ComplaintLocationEntity {
        var complaintLocation = ComplaintLocationEntity()
        with(complaintLocation) {
            county = complaintLocationDto.county
            town = complaintLocationDto.town
            email = complaintLocationDto.email
            nameContactPerson = complaintLocationDto.nameContactPerson
            phoneNumber = complaintLocationDto.phoneNumber
            telephoneNumber = complaintLocationDto.telephoneNumber
            businessAddress = complaintLocationDto.businessAddress
            marketCenter = complaintLocationDto.marketCenter
            building = complaintLocationDto.buildingName
            productBrand = complaintDto.productBrand
            region = county?.let { commonDaoServices.findCountiesEntityByCountyId(it, map.activeStatus).regionId }
            transactionDate = commonDaoServices.getCurrentDate()
            status = map.activeStatus
            createdBy = complaint.createdBy
            createdOn = commonDaoServices.getTimestamp()
            complaintId = complaint.id
        }
        complaintLocation = complaintLocationRepo.save(complaintLocation)
        return complaintLocation
    }

    fun findMsTypeDetailsWithUuid(uuid: String): MsTypesEntity {
        msTypesRepo.findByUuid(uuid)
            ?.let { msTypeDetails ->
                return msTypeDetails
            }
            ?: throw Exception("MS Type Details with the following uuid = ${uuid}, does not Exist")
    }

    fun findMsProcessComplaintByID(complaintStatus: Int, processValueID: Long): MsProcessNamesEntity? {
        return processNameRepo.findByComplaintStatusAndId(complaintStatus, processValueID)
    }

    fun progressSteps(stepsValue: Int): JSONObject {
        val progressObject = JSONObject()
        var step = 0
        for (progress in stepsValue downTo 1) {
            step++
            val dividend = 100
            val quotient: Int = dividend / progress
//            val remainder = dividend % divisor
            progressObject.put("step-$step", quotient)

        }
        return progressObject
    }

    fun complaintSubmittedDTOEmailCompose(dataDetails: ComplaintEntity): CustomerComplaintSubmittedDTO {
        val dataValue = CustomerComplaintSubmittedDTO()
        with(dataValue) {
            refNumber = dataDetails.referenceNumber
            baseUrl = applicationMapProperties.baseUrlValue
            fullName = dataDetails.createdBy
            dateSubmitted = dataDetails.transactionDate
        }

        return dataValue
    }

    fun findComplaintByRefNumber(refNumber: String): ComplaintEntity {
        complaintsRepo.findByReferenceNumber(refNumber)
            ?.let { complaint ->
                return complaint
            }
            ?: throw ExpectedDataNotFound("Complaint with [refNumber = ${refNumber}], does not Exist")
    }

    fun complaintInspectionMappingCommonDetails(
        cp: ComplaintEntity,
        map: ServiceMapsEntity
    ): AllComplaintsDetailsDto {
        val (comp, complaintCustomersDetails, complaintLocationDetails) = complaintDetails(cp)
        val complaintFilesSaved = findUploadedFileForComplaints(comp.id?: throw ExpectedDataNotFound("MISSING COMPLAINT ID"))
        val complaintRemarks = findRemarksForComplaints(comp.id?: throw ExpectedDataNotFound("MISSING COMPLAINT ID"))
        val officerList = commonDaoServices.findOfficersListBasedOnRole(
            applicationMapProperties.mapMSComplaintWorkPlanMappedOfficerROLEID,
            complaintLocationDetails.county ?: throw ExpectedDataNotFound("MISSING COMPLAINT COUNTY ID"),
            complaintLocationDetails.region ?: throw ExpectedDataNotFound("MISSING COMPLAINT REGION ID")
        )

        val hofList = commonDaoServices.findOfficersListBasedOnRole(
            applicationMapProperties.mapMSComplaintWorkPlanMappedHOFROLEID,
            complaintLocationDetails.county ?: throw ExpectedDataNotFound("MISSING COMPLAINT COUNTY ID"),
            complaintLocationDetails.region ?: throw ExpectedDataNotFound("MISSING COMPLAINT REGION ID")
        )


        val acceptanceStatus = mapAcceptanceStatusDto(comp, map)
        var acceptanceDone = false
        if (acceptanceStatus!=null){
            acceptanceDone = true
        }

        var timelineOverDue =false
        if (comp.timelineEndDate!= null){
        if (comp.timelineEndDate!!>commonDaoServices.getCurrentDate()){
            timelineOverDue = true
        }}

        return mapComplaintInspectionDto(
            mapComplaintDto(comp, complaintCustomersDetails, complaintLocationDetails, complaintFilesSaved, map, timelineOverDue),
            acceptanceDone,
            comp.msProcessStatus==1,
            officerList,
            hofList,
            complaintRemarks,
            comp.assignedIo?.let { commonDaoServices.findUserByID(it) },
            comp.hofAssigned?.let { commonDaoServices.findUserByID(it) },
            acceptanceStatus,
            null,
            null,
            null,
        )
    }

    fun mapComplaintInspectionDto(
        complaintsDetails: ComplaintsDetailsDto,
        acceptanceDone : Boolean,
        msProcessStatus : Boolean,
        officerList: List<UsersEntity>?,
        hofList: List<UsersEntity>?,
        remarksList: List<MsRemarksEntity>?,
        officersAssigned : UsersEntity?,
        hofAssigned : UsersEntity?,
        acceptanceResults : MsComplaintAcceptanceStatusDto?,
        sampleCollected: SampleCollectionDto?,
        sampleSubmitted: SampleSubmissionDto?,
        sampleLabResults: MSSSFLabResultsDto?,
    ): AllComplaintsDetailsDto {
        val workPlanSchedule = complaintsDetails.id?.let { msWorkPlanDaoServices.findWorkPlanScheduleByComplaintID(it) }
        return AllComplaintsDetailsDto(
            complaintsDetails,
            acceptanceDone,
            acceptanceResults,
            officerList?.let { mapOfficerListDto(it) },
            hofList?.let { mapOfficerListDto(it) },
            officersAssigned?.let { mapOfficerDto (it) },
            hofAssigned?.let { mapOfficerDto (it) },
            remarksList?.let { mapRemarksListDto(it) },
            sampleCollected,
            sampleSubmitted,
            sampleLabResults,
            msProcessStatus,
            workPlanSchedule?.referenceNumber,
            workPlanSchedule?.workPlanYearId?.let { msWorkPlanDaoServices.findWorkPlanBatchByWorkPlanScheduleID(it)?.referenceNumber }
        )
    }

    fun mapAcceptanceStatusDto(comp: ComplaintEntity, map: ServiceMapsEntity): MsComplaintAcceptanceStatusDto? {

        return when {
            comp.approved==map.activeStatus -> {
                MsComplaintAcceptanceStatusDto(
                    comp.approvedRemarks,
                    comp.advisedWhereto,
                    comp.approved==1,
                    comp.rejected==1,
                    comp.mandateForOga==1
                )
            }
            comp.rejected==map.activeStatus -> {
                MsComplaintAcceptanceStatusDto(
                    comp.rejectedRemarks,
                    comp.advisedWhereto,
                    comp.approved==1,
                    comp.rejected==1,
                    comp.mandateForOga==1
                )
            }
            comp.mandateForOga==map.activeStatus -> {
                MsComplaintAcceptanceStatusDto(
                    comp.advisedWhereto,
                    comp.advisedWhereto,
                    comp.approved==1,
                    comp.rejected==1,
                    comp.mandateForOga==1
                )
            }
            else -> null
        }

    }


    fun mapOfficerDto(officer: UsersEntity): MsUsersDto {
        return MsUsersDto(
            officer.id,
            officer.firstName,
            officer.lastName,
            officer.userName,
            officer.email,
            officer.cellphone,
            officer.status == 1,
        )

    }

    fun mapStandardDetailsDto(stdDetails: SampleStandardsEntity): StandardDetailsDto {
        return StandardDetailsDto(
            stdDetails.standardTitle,
            stdDetails.standardNumber,
            stdDetails.ics,
            stdDetails.hsCode,
            stdDetails.subCategoryId,
        )

    }

    fun mapComplaintDto(
        comp: ComplaintEntity,
        complaintCustomersDetails: ComplaintCustomersEntity,
        complaintLocationDetails: ComplaintLocationEntity,
        complaintFilesSaved: List<MsUploadsEntity>?,
        map: ServiceMapsEntity,
        timelineOverDue: Boolean
    ): ComplaintsDetailsDto {
        return  ComplaintsDetailsDto(
            comp.id,
            comp.referenceNumber,
            complaintCustomersDetails.firstName?.let { complaintCustomersDetails.lastName?.let { it1 -> commonDaoServices.concatenateName(it, it1) } },
            complaintCustomersDetails.emailAddress,
            complaintCustomersDetails.mobilePhoneNumber,
            complaintCustomersDetails.postalAddress,
            complaintCustomersDetails.physicalAddress,
            comp.complaintSampleDetails,
            comp.remedySought,
            complaintLocationDetails.email,
            complaintLocationDetails.nameContactPerson,
            complaintLocationDetails.phoneNumber,
            complaintLocationDetails.telephoneNumber,
            complaintLocationDetails.businessAddress,
            comp.complaintDepartment?.let { commonDaoServices.findDepartmentByID(it).department },
            comp.complaintTitle,
            comp.complaintDetails,
            comp.standardCategory?.let { commonDaoServices.findStandardCategoryByID(it).standardCategory },
            comp.broadProductCategory?.let { commonDaoServices.findBroadCategoryByID(it).category },
            comp.productCategory?.let { commonDaoServices.findProductCategoryByID(it).name },
            comp.productSubCategory?.let { commonDaoServices.findProductSubCategoryByID(it).name },
            comp.product?.let { commonDaoServices.findProductByID(it).name },
            complaintLocationDetails.productBrand,
            complaintLocationDetails.county?.let { commonDaoServices.findCountiesEntityByCountyId(it, map.activeStatus).county },
            complaintLocationDetails.town?.let { commonDaoServices.findTownEntityByTownId(it).town },
            complaintLocationDetails.marketCenter,
            complaintLocationDetails.building,
            comp.transactionDate,
            comp.msProcessId?.let { findMsProcessComplaintByID(1, it)?.processName },
//            msInspectionOfficer,
//            msDivisionList,

            comp.approved == 1,
            comp.assignedIoStatus == 1,
            comp.rejected == 1,
            comp.classificationDetailsStatus == 1,
            complaintFilesSaved?.let { mapFileListDto(it) },
            comp.productSubCategory?.let { commonDaoServices.findSampleStandardsByID(it) }?.let { mapStandardDetailsDto(it) },
            comp.timelineStartDate,
            comp.timelineEndDate,
            timelineOverDue
        )

    }

    fun mapOfficerListDto(officerList: List<UsersEntity>): List<MsUsersDto> {
        return officerList.map {
            MsUsersDto(
                it.id,
                it.firstName,
                it.lastName,
                it.userName,
                it.email,
                it.cellphone,
                it.status == 1,
            )
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

    fun mapFileListDto(fileFoundList: List<MsUploadsEntity>): List<ComplaintsFilesFoundDto> {
        return fileFoundList.map {
            ComplaintsFilesFoundDto(
                it.id,
                it.documentType,
                it.name,
                it.fileType
            )
        }
    }


    fun findUploadedFileForComplaints(complaintID: Long): List<MsUploadsEntity>? {
        return msUploadRepo.findAllByMsComplaintIdAndComplaintUploads(complaintID, 1)
    }

    fun findRemarksForComplaints(complaintID: Long): List<MsRemarksEntity>? {
        return remarksRepo.findAllByComplaintIdOrderByIdAsc(complaintID)
    }

    fun complaintDetails( comp: ComplaintEntity): Triple<ComplaintEntity, ComplaintCustomersEntity, ComplaintLocationEntity> {
        val complaintCustomersDetails = findComplaintCustomerByComplaintID(comp.id?: throw ExpectedDataNotFound("Missing complaint ID"))
        val complaintLocationDetails = findComplaintLocationByComplaintID(comp.id?: throw ExpectedDataNotFound("Missing complaint ID"))
        return Triple(comp, complaintCustomersDetails, complaintLocationDetails)
    }

    fun findComplaintCustomerByComplaintID(complaintID: Long): ComplaintCustomersEntity {
        complaintCustomersRepo.findByComplaintId(complaintID)
            ?.let { complaintCustomer ->
                return complaintCustomer
            }
            ?: throw ExpectedDataNotFound("Complaint Customer with Complaint [ID = ${complaintID}], does not Exist")
    }

    fun findWorkPlanScheduleByComplaintID(complaintID: Long): ComplaintCustomersEntity {
        complaintCustomersRepo.findByComplaintId(complaintID)
            ?.let { complaintCustomer ->
                return complaintCustomer
            }
            ?: throw ExpectedDataNotFound("Complaint Customer with Complaint [ID = ${complaintID}], does not Exist")
    }

    fun findComplaintLocationByComplaintID(complaintID: Long): ComplaintLocationEntity {
        complaintLocationRepo.findByComplaintId(complaintID)
            ?.let { complaintLocation ->
                return complaintLocation
            }
            ?: throw ExpectedDataNotFound("Complaint Location with Complaint [ID = ${complaintID}], does not Exist")
    }


    fun listMsDepartments(status: Int): List<MsDepartmentDto>? {
        val directoratesEntity = commonDaoServices.findDirectorateByID(applicationMapProperties.mapMsDirectorateID)
        return commonDaoServices.findDepartmentByDirectorate(directoratesEntity, status).sortedBy { it.id }.map { MsDepartmentDto(it.id, it.department, it.descriptions, it.directorateId?.id, it.status == 1) }
    }

    fun listMsDivision(status: Int, departmentID: Long): List<MsDivisionDto>? {
        val department = commonDaoServices.findDepartmentByID(departmentID)
        return commonDaoServices.findDivisionByDepartmentId(department, status).sortedBy { it.id }.map { MsDivisionDto(it.id, it.division, it.descriptions, it.status, it.departmentId?.id) }
    }

    fun findProcessNameByID(processID: Long, status: Int): MsProcessNamesEntity {
        processNameRepo.findByComplaintStatusAndId(status,processID)
            ?.let {
                return it
            }
            ?: throw ExpectedDataNotFound("Fuel Process Details found with ID : $processID")
    }
}


