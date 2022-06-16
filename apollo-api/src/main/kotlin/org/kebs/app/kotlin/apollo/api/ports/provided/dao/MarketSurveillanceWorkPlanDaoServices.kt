package org.kebs.app.kotlin.apollo.api.ports.provided.dao

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
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
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
    private val msFuelDaoServices: MarketSurveillanceFuelDaoServices
) {
    final var complaintSteps: Int = 6
    private final val activeStatus: Int = 1

    final var appId = applicationMapProperties.mapMarketSurveillance

    @Lazy
    @Autowired
    lateinit var reportsControllers: ReportsController


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
            status = map.activeStatus
            batchClosed = map.activeStatus
            userTaskId = applicationMapProperties.mapMSUserTaskNameMANAGERPETROLEUM
        }

        val fileSaved = updateWorkPlanBatch(batchDetail, map, loggedInUser)

        if (fileSaved.first.status == map.successStatus) {

            val fileInspectionList = findALlWorkPlanDetailsAssociatedWithWorkPlanID(batchDetail.id,page).toList()
            fileInspectionList.forEach { it ->
                with(it) {
                    msProcessId = applicationMapProperties.mapMSAssignOfficer
                    userTaskId = applicationMapProperties.mapMSUserTaskNameMANAGERPETROLEUM
                }
                updateWorkPlanInspectionDetails(it, map, loggedInUser)
            }

            val managerPetroleumList = commonDaoServices.findOfficersListBasedOnRegionCountyAndRole(applicationMapProperties.mapMSMappedManagerPetroliumROLEID,
                loggedInUserProfile.countyID?.id ?: throw ExpectedDataNotFound("MISSING BATCH COUNTY ID"),
                loggedInUserProfile.regionId?.id ?: throw ExpectedDataNotFound("MISSING BATCH REGION ID")
            )

            runBlocking {
                managerPetroleumList
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
        } else {
            throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved.first))
        }
    }


    @PreAuthorize("hasAuthority('MS_IO_READ') or hasAuthority('MS_HOD_READ') or hasAuthority('MS_RM_READ') or hasAuthority('MS_HOF_READ') or hasAuthority('MS_DIRECTOR_READ')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun getAllWorPlanInspectionListBasedOnBatchRefNo(batchReferenceNo: String,page: PageRequest): WorkPlanScheduleListDetailsDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val auth = commonDaoServices.loggedInUserAuthentication()
        val loggedInUserProfile = commonDaoServices.findUserProfileByUserID(loggedInUser)
        val map = commonDaoServices.serviceMapDetails(appId)
        var createdWorkPlan : WorkPlanCreatedEntity? = null
        var workPlanList : List<MsWorkPlanGeneratedEntity>? = null
        if (auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_IO_READ" }) {
            createdWorkPlan = findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
            workPlanList = findALlWorkPlanDetailsAssociatedWithWorkPlanID(createdWorkPlan.id,page).toList()
        }
//        else if (auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_MP_MODIFY" }) {
//            findBatchDetail = findFuelBatchDetailByReferenceNumberAndRegionId(batchReferenceNo,loggedInUserProfile.regionId?.id?: throw ExpectedDataNotFound("Missing region value on your user profile  details"))
//            fileInspectionList = findAllFuelInspectionListBasedOnBatchIDPageRequest(findBatchDetail.id,page).toList()
//        }
//        else if (auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_IOP_MODIFY" }) {
//            findBatchDetail = findFuelBatchDetailByReferenceNumberAndRegionIdAndCountyId(batchReferenceNo,loggedInUserProfile.regionId?.id?: throw ExpectedDataNotFound("Missing region value on your user profile  details"),loggedInUserProfile.countyID?.id?: throw ExpectedDataNotFound("Missing county value on your user profile  details"),)
//            fileInspectionList = findAllFuelInspectionListBasedOnBatchIDAndAssignedIO(findBatchDetail.id,loggedInUser.id ?: throw ExpectedDataNotFound("Missing Logged in user ID"))
//        }
        else{
            throw ExpectedDataNotFound("YOU CAN'T ACCESS FILES WITH THOSE RIGHTS")
        }

        return mapWorkPlanInspectionListDto(workPlanList,mapWorkPlanBatchDetailsDto(createdWorkPlan, map))
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
//        val fuelInspectionOfficer = findFuelInspectionOfficerAssigned(fileInspectionDetail, map.activeStatus)

        return workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)
    }

    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun createNewWorkPlanSchedule(body: WorkPlanEntityDto, referenceNumber: String, page: PageRequest): WorkPlanScheduleListDetailsDto {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val msType = findMsTypeDetailsWithUuid(applicationMapProperties.mapMsWorkPlanTypeUuid)
        val batchDetail  = findCreatedWorkPlanWIthRefNumber(referenceNumber)
        val fileSaved = saveNewWorkPlanActivity(body,msType, batchDetail, map, loggedInUser)
        val remarksDto = RemarksToAddDto()
        with(remarksDto){
            remarksDescription= body.remarks
            remarksStatus= map.activeStatus
            processID = fileSaved.second.msProcessId
            userId= loggedInUser.id
        }

        if (fileSaved.first.status == map.successStatus) {
            val remarksSaved = workPlanAddRemarksDetails(fileSaved.second.id,remarksDto, map, loggedInUser)
            if (remarksSaved.first.status == map.successStatus){
                val workPlanList = findALlWorkPlanDetailsAssociatedWithWorkPlanID(batchDetail.id,page).toList()
                return mapWorkPlanInspectionListDto(workPlanList,mapWorkPlanBatchDetailsDto(batchDetail, map))
            } else {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(remarksSaved.first))
            }
        } else {
            throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved.first))
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
            uuid = commonDaoServices.generateUUIDString()
            msTypeId = msType.id
            progressStep = "WorkPlan Generated"
            region = county?.let { commonDaoServices.findCountiesEntityByCountyId(it, map.activeStatus).regionId }
            referenceNumber = "${msType.markRef}${generateRandomText(map.transactionRefLength, map.secureRandom, map.messageDigestAlgorithm, true)}".toUpperCase()
            workPlanYearId = userWorkPlan.id
            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
//            msProcessId = applicationMapProperties.mapMSCloseBatch
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
                referenceNumber =it.referenceNumber,
                nameActivity =it.nameActivity,
                timeActivityDate =it.timeActivityDate,
                budget =it.budget,
                progressStep =it.msProcessId?.let { it1 -> findProcessNameByID(it1, 1).processName },
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

    fun findALlWorkPlanDetailsAssociatedWithWorkPlanID(createdWorkPlanID: Long,pageable: Pageable): Page<MsWorkPlanGeneratedEntity> {
        generateWorkPlanRepo.findByWorkPlanYearId(createdWorkPlanID,pageable)
            ?.let {
                return it
            }
            ?: throw ExpectedDataNotFound("No workPlan Details Associated with the following workPlan [ID = ${createdWorkPlanID}]")
    }

    fun findCreatedWorkPlanWIthRefNumber(referenceNumber: String): WorkPlanCreatedEntity {
        workPlanCreatedRepository.findByReferenceNumber(referenceNumber)
            ?.let { createdWorkPlan ->
                return createdWorkPlan
            }
            ?: throw ExpectedDataNotFound("Created Work Plan with the following Reference Number = ${referenceNumber}, does not Exist")
    }

    fun findWorkPlanActivityByReferenceNumber(referenceNumber: String): MsWorkPlanGeneratedEntity {
        generateWorkPlanRepo.findByReferenceNumber(referenceNumber)
            ?.let { activityWorkPlan ->
                return activityWorkPlan
            }
            ?: throw ExpectedDataNotFound("WorkPlan Activity with [referenceNumber = ${referenceNumber}], does not Exist")
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


        return mapWorkPlanInspectionDto(
            workPlanScheduledDetails,
            batchDetailsDto
        )
    }

    fun mapWorkPlanInspectionDto(
        wKP: MsWorkPlanGeneratedEntity,
        batchDetails: WorkPlanBatchDetailsDto
    ): WorkPlanInspectionDto {
        return WorkPlanInspectionDto(
                    wKP.id,
            wKP.productCategory?.let { commonDaoServices.findProductCategoryByID(it).name },
            wKP.broadProductCategory?.let { commonDaoServices.findBroadCategoryByID(it).category },
            wKP.product?.let { commonDaoServices.findProductByID(it).name },
            wKP.standardCategory?.let { commonDaoServices.findStandardCategoryByID(it).standardCategory },
            wKP.productSubCategory?.let { commonDaoServices.findProductSubCategoryByID(it).name },
            wKP.department,
            wKP.divisionId,
            wKP.sampleSubmittedId?.id,
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
            wKP.msProcessId?.let { it1 -> findProcessNameByID(it1, 1).processName },
            wKP.county,
            wKP.subcounty,
            wKP.townMarketCenter,
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
            wKP.region,
            wKP.complaintId,
            wKP.officerId,
            wKP.destructionDocId,
            wKP.complaintDepartment,
            wKP.referenceNumber,
            batchDetails
        )
    }

    fun findRemarksForWorkPlan(workPlanInspectionID: Long): List<MsRemarksEntity>? {
        return remarksRepo.findAllByWorkPlanIdOrderByIdAsc(workPlanInspectionID)
    }

}




