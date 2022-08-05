package org.kebs.app.kotlin.apollo.api.ports.provided.dao

import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.controllers.qaControllers.ReportsController
import org.kebs.app.kotlin.apollo.api.ports.provided.emailDTO.*
import org.kebs.app.kotlin.apollo.api.ports.provided.lims.LimsServices
import org.kebs.app.kotlin.apollo.common.dto.ms.*
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.utils.generateRandomText
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.di.CdLaboratoryEntity
import org.kebs.app.kotlin.apollo.store.model.ms.*
import org.kebs.app.kotlin.apollo.store.model.qa.QaSampleLabTestResultsEntity
import org.kebs.app.kotlin.apollo.store.model.qa.QaSampleSubmissionEntity
import org.kebs.app.kotlin.apollo.store.model.qa.QaSampleSubmittedPdfListDetailsEntity
import org.kebs.app.kotlin.apollo.store.repo.IServiceRequestsRepository
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
import java.io.File
import java.math.BigDecimal
import java.sql.Date
import java.sql.Timestamp
import java.time.Instant


@Service
class MarketSurveillanceFuelDaoServices(
    private val applicationMapProperties: ApplicationMapProperties,
    private val fuelBatchRepo: IFuelBatchRepository,
    private val processNameRepo: IMsProcessNamesRepository,
    private val limsServices: LimsServices,
    private val remarksRepo: IMsRemarksComplaintRepository,
    private val laboratoryRepo: ILaboratoryRepository,
    private val fuelRemediationInvoiceChargesRepo: IFuelRemediationChargesRepository,
    private val sampleSubmissionSavedPdfListRepo: IQaSampleSubmittedPdfListRepository,
    private val workPlanYearsCodesRepo: IWorkplanYearsCodesRepository,
    private val workPlanMonthlyCodesRepo: IWorkplanMonthlyCodesRepository,
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
    private val commonDaoServices: CommonDaoServices
) {

    private final val designationID: Long = 81
    private final val directorDermyValueID: Long = 0
    private final val activeStatus: Int = 1
    private final val remunerationChargesId: Long = 1
    private final val percentage: Long = 100
    private final val subsistenceChargesId: Long = 2
    private final val transportAirTicketsChargesId: Long = 3

    final var appId = applicationMapProperties.mapMarketSurveillance

    @Lazy
    @Autowired
    lateinit var reportsControllers: ReportsController

    @PreAuthorize("hasAuthority('EPRA')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun createNewFuelBatch(body: BatchFileFuelSaveDto,page: PageRequest): FuelInspectionScheduleListDetailsDto {
        val map = commonDaoServices.serviceMapDetails(appId)
//        var sr = commonDaoServices.createServiceRequest(map)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val currentYear = commonDaoServices.getCurrentYear()
        val currentMonth = commonDaoServices.getCurrentMonth()
        val fuelPlanYearCode = findWorkPlanYearsCode(currentYear, map)?: throw ExpectedDataNotFound("Fuel Schedule Current Year Code Not Found")
        val fuelPlanMonthCode = findWorkPlanMonthCode(currentMonth, map)?: throw ExpectedDataNotFound("Fuel Schedule Current Month Code Not Found")
        val fileSaved = fuelCreateBatch(body,fuelPlanYearCode,fuelPlanMonthCode, map, loggedInUser)
        val remarksDto = RemarksToAddDto()
        with(remarksDto){
        remarksDescription= body.remarks
            remarksStatus= "N/A"
            processID = applicationMapProperties.mapMSCreateBatch
            userId= loggedInUser.id
        }
        val remarksSaved = fuelAddRemarksDetails(0L,remarksDto,map,loggedInUser,fileSaved.second.id)

        if (fileSaved.first.status == map.successStatus) {
            val fileInspectionList = findAllFuelInspectionListBasedOnBatchIDPageRequest(fileSaved.second.id,page).toList()
            return mapFuelInspectionListDto(fileInspectionList,mapFuelBatchDetailsDto(fileSaved.second, map))
        } else {
            throw ExpectedDataNotFound(fileSaved.first.responseMessage)
        }
    }

    @PreAuthorize("hasAuthority('EPRA')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun closeFuelBatchCreated(referenceNumber: String,page: PageRequest): List<FuelBatchDetailsDto> {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val batchDetail = findFuelBatchDetailByReferenceNumber(referenceNumber)

        with(batchDetail) {
            status = map.activeStatus
            batchClosed = map.activeStatus
            userTaskId = applicationMapProperties.mapMSUserTaskNameMANAGERPETROLEUM
        }

        val fileSaved = updateFuelBatch(batchDetail, map, loggedInUser)

        if (fileSaved.first.status == map.successStatus) {

            val fileInspectionList = findAllFuelInspectionListBasedOnBatchID(batchDetail.id)
            fileInspectionList.forEach { it ->
                with(it) {
                    timelineStartDate = commonDaoServices.getCurrentDate()
                    timelineEndDate = applicationMapProperties.mapMSAssignOfficer.let { findProcessNameByID( it, 1).timelinesDay?.let {it2-> commonDaoServices.addYDayToDate(commonDaoServices.getCurrentDate(), it2) } }
                    msProcessId = applicationMapProperties.mapMSAssignOfficer
                    userTaskId = applicationMapProperties.mapMSUserTaskNameMANAGERPETROLEUM
                }
                updateFuelInspectionDetails(it, map, loggedInUser)
            }

            val managerPetroleumList = commonDaoServices.findOfficersListBasedOnRegionCountyAndRole(applicationMapProperties.mapMSMappedManagerPetroliumROLEID,
                fileSaved.second.countyId ?: throw ExpectedDataNotFound("MISSING BATCH COUNTY ID"),
                fileSaved.second.regionId ?: throw ExpectedDataNotFound("MISSING BATCH REGION ID")
            )

            runBlocking {
            managerPetroleumList
                ?.forEach { mp->
                    val scheduleEmailDetails =  FuelScheduledDTO()
                    with(scheduleEmailDetails){
                        baseUrl= applicationMapProperties.baseUrlValue
                        fullName = commonDaoServices.concatenateName(mp)
                        refNumber = fileSaved.second.referenceNumber
                        yearCodeName = fileSaved.second.yearNameId?.let { findPlanYear(it).yearName }
                        dateSubmitted = fileSaved.second.transactionDate

                    }
                    commonDaoServices.sendEmailWithUserEntity(mp, applicationMapProperties.mapMsFuelScheduleMPNotification, scheduleEmailDetails, map, fileSaved.first)
                }
            }

            val fuelBatchList = findAllFuelBatchListBasedOnPageable(page).toList()
            return mapFuelBatchListDto(fuelBatchList, map)
        } else {
            throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved.first))
        }
    }

    @PreAuthorize("hasAuthority('EPRA')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun createNewFuelSchedule(body: FuelEntityDto, referenceNumber: String, page: PageRequest): FuelInspectionScheduleListDetailsDto {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val batchDetail = findFuelBatchDetailByReferenceNumber(referenceNumber)
        val fileSaved = fuelCreateSchedule(body, batchDetail.id, map, loggedInUser)
        val remarksDto = RemarksToAddDto()
        with(remarksDto){
            remarksDescription= body.remarks
            remarksStatus= "N/A"
            processID = fileSaved.second.msProcessId
            userId= loggedInUser.id
        }

        if (fileSaved.first.status == map.successStatus) {
            val remarksSaved = fuelAddRemarksDetails(fileSaved.second.id,remarksDto, map, loggedInUser)
            if (remarksSaved.first.status == map.successStatus){
                val fileInspectionList = findAllFuelInspectionListBasedOnBatchIDPageRequest(fileSaved.second.id,page).toList()
                return mapFuelInspectionListDto(fileInspectionList,mapFuelBatchDetailsDto(batchDetail, map))
            } else {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(remarksSaved.first))
            }
        } else {
            throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved.first))
        }
    }

    @PreAuthorize("hasAuthority('MS_IOP_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun getAllLaboratoryList(): List<LaboratoryDto> {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        val laboratoryList = laboratoryRepo.findByStatus(map.activeStatus)
        return mapLaboratoryListDto(laboratoryList)
    }

    @PreAuthorize("hasAuthority('EPRA') or  hasAuthority('MS_MP_MODIFY') or  hasAuthority('MS_IOP_MODIFY')")
   @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun getAllFuelBatchList(page: PageRequest): List<FuelBatchDetailsDto> {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val auth = commonDaoServices.loggedInUserAuthentication()
        val loggedInUserProfile = commonDaoServices.findUserProfileByUserID(loggedInUser)
        val map = commonDaoServices.serviceMapDetails(appId)
        var fuelBatchList : List<MsFuelBatchInspectionEntity>? = null
        when {
            auth.authorities.stream().anyMatch { authority -> authority.authority == "EPRA" } -> {
                fuelBatchList = findAllFuelBatchListBasedOnPageable(page).toList()
            }
            auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_MP_MODIFY" } -> {
                fuelBatchList = findAllFuelBatchListBasedOnPageableRegion(loggedInUserProfile.regionId?.id?: throw ExpectedDataNotFound("Missing region value on your user profile  details"),map.activeStatus,page).toList()
            }
            auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_IOP_MODIFY" } -> {
                fuelBatchList = findAllFuelBatchListBasedOnPageableCountyAndRegion(loggedInUserProfile.countyID?.id?: throw ExpectedDataNotFound("Missing county value on your user profile  details"),loggedInUserProfile.regionId?.id?: throw ExpectedDataNotFound("Missing region value on your user profile  details"),map.activeStatus,page).toList()
            }
        }

        return mapFuelBatchListDto(fuelBatchList, map)
    }


    @PreAuthorize("hasAuthority('EPRA') or  hasAuthority('MS_MP_MODIFY') or  hasAuthority('MS_IOP_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun getAllFuelInspectionListBasedOnBatchRefNo(batchReferenceNo: String,page: PageRequest): FuelInspectionScheduleListDetailsDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val auth = commonDaoServices.loggedInUserAuthentication()
        val loggedInUserProfile = commonDaoServices.findUserProfileByUserID(loggedInUser)
        val map = commonDaoServices.serviceMapDetails(appId)
        var findBatchDetail : MsFuelBatchInspectionEntity? = null
        var fileInspectionList : List<MsFuelInspectionEntity>? = null
        if (auth.authorities.stream().anyMatch { authority -> authority.authority == "EPRA" }) {
            findBatchDetail = findFuelBatchDetailByReferenceNumber(batchReferenceNo)
            fileInspectionList = findAllFuelInspectionListBasedOnBatchIDPageRequest(findBatchDetail.id,page).toList()
        }
        else if (auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_MP_MODIFY" }) {
            findBatchDetail = findFuelBatchDetailByReferenceNumberAndRegionId(batchReferenceNo,loggedInUserProfile.regionId?.id?: throw ExpectedDataNotFound("Missing region value on your user profile  details"))
            fileInspectionList = findAllFuelInspectionListBasedOnBatchIDPageRequest(findBatchDetail.id,page).toList()
        }
        else if (auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_IOP_MODIFY" }) {
            findBatchDetail = findFuelBatchDetailByReferenceNumberAndRegionIdAndCountyId(batchReferenceNo,loggedInUserProfile.regionId?.id?: throw ExpectedDataNotFound("Missing region value on your user profile  details"),loggedInUserProfile.countyID?.id?: throw ExpectedDataNotFound("Missing county value on your user profile  details"),)
            fileInspectionList = findAllFuelInspectionListBasedOnBatchIDAndAssignedIO(findBatchDetail.id,loggedInUser.id ?: throw ExpectedDataNotFound("Missing Logged in user ID"))
        }else{
            throw ExpectedDataNotFound("YOU CAN'T ACCESS FILES WITH THOSE RIGHTS")
        }

        return mapFuelInspectionListDto(fileInspectionList,mapFuelBatchDetailsDto(findBatchDetail, map))
    }

    @PreAuthorize("hasAuthority('EPRA') or  hasAuthority('MS_MP_MODIFY') or  hasAuthority('MS_IOP_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun getFuelInspectionDetailsBasedOnRefNo(referenceNo: String, batchReferenceNo: String): FuelInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        val fileInspectionDetail = findFuelInspectionDetailByReferenceNumber(referenceNo)
        val batchDetails = findFuelBatchDetailByReferenceNumber(batchReferenceNo)
//        val fuelInspectionOfficer = findFuelInspectionOfficerAssigned(fileInspectionDetail, map.activeStatus)

        return fuelInspectionMappingCommonDetails(fileInspectionDetail, map, batchDetails)
    }

    @PreAuthorize("hasAuthority('MS_IOP_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun endFuelInspectionDetailsBasedOnRefNo(referenceNo: String, batchReferenceNo: String): FuelInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var fileInspectionDetail = findFuelInspectionDetailByReferenceNumber(referenceNo)
        val batchDetails = findFuelBatchDetailByReferenceNumber(batchReferenceNo)
        with(fileInspectionDetail){
            msProcessId = applicationMapProperties.mapMSFuelInspectionEnded
            inspectionCompleteStatus = map.activeStatus
            userTaskId = null
        }
        val detailsSaved = updateFuelInspectionDetails(fileInspectionDetail, map, loggedInUser)
        fileInspectionDetail = detailsSaved.second
        val emailValuesStationOwner = FuelScheduledRemediationEndedDTO()
        with(emailValuesStationOwner) {
            baseUrl = applicationMapProperties.baseUrlValue
            fullName = fileInspectionDetail.company
            refNumber = fileInspectionDetail.referenceNumber
            remediationDate= fileInspectionDetail.inspectionDateFrom
        }
        runBlocking {commonDaoServices.sendEmailWithUserEmail(
            fileInspectionDetail.stationOwnerEmail ?: throw ExpectedDataNotFound("Missing Station Owner Email"),
            applicationMapProperties.mapMsFuelInspectionScheduleEndedNotification,
            emailValuesStationOwner, map, detailsSaved.first)
        }
//        val emailValuesEpra = FuelScheduledRemediationEndedDTO()
//        with(emailValuesEpra) {
//            baseUrl = applicationMapProperties.baseUrlValue
//            fullName = commonDaoServices.concatenateName(fileInspectionDetail.)
//            refNumber = fileInspectionDetail.referenceNumber
//        }
//        commonDaoServices.sendEmailWithUserEntity(officerDetails, applicationMapProperties.mapMsFuelAssignedIONotification, emailValuesStationOwner, map, fileSaved2.first)
        return fuelInspectionMappingCommonDetails(fileInspectionDetail, map, batchDetails)
    }

    @PreAuthorize("hasAuthority('MS_MP_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun getFuelInspectionDetailsAssignOfficer(
        referenceNo: String,
        batchReferenceNo: String,
        body: FuelEntityAssignOfficerDto
    ): FuelInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var fileInspectionDetail = findFuelInspectionDetailByReferenceNumber(referenceNo)
        val batchDetails = findFuelBatchDetailByReferenceNumber(batchReferenceNo)
        val fileOfficerSaved = fuelInspectionDetailsAssignOfficer(body, fileInspectionDetail, map, loggedInUser)
        val remarksDto = RemarksToAddDto()
        with(remarksDto){
            remarksDescription= body.remarks
            remarksStatus= "N/A"
            processID = fileInspectionDetail.msProcessId
            userId= loggedInUser.id
        }

        when (fileOfficerSaved.first.status) {
            map.successStatus -> {
                with(fileInspectionDetail) {
                    userTaskId = applicationMapProperties.mapMSUserTaskNameOFFICER
                    msProcessId = applicationMapProperties.mapMSRapidTest
                    assignedOfficerStatus = map.activeStatus
                }
                val fileSaved2 = updateFuelInspectionDetails(fileInspectionDetail, map, loggedInUser)

                if (fileSaved2.first.status == map.successStatus) {
                    fileInspectionDetail = fileSaved2.second
                    val remarksSaved = fuelAddRemarksDetails(fileSaved2.second.id,remarksDto, map, loggedInUser)
                    if (remarksSaved.first.status == map.successStatus){
                        val officerDetails = commonDaoServices.findUserByID(fileOfficerSaved.second.assignedIo?:throw ExpectedDataNotFound("MISSING OFFICER ASSIGNED DETAILS"))
                        val dataValue = FuelScheduledAssignedDTO()
                        with(dataValue) {
                            baseUrl = applicationMapProperties.baseUrlValue
                            fullName = commonDaoServices.concatenateName(officerDetails)
                            refNumber = fileSaved2.second.referenceNumber
                            commentRemarks = fileOfficerSaved.second.remarks
                            dateSubmitted = fileOfficerSaved.second.transactionDate
                        }
                        runBlocking {
                            commonDaoServices.sendEmailWithUserEntity(officerDetails, applicationMapProperties.mapMsFuelAssignedIONotification, dataValue, map, fileSaved2.first)
                        }
                        return fuelInspectionMappingCommonDetails(fileInspectionDetail, map, batchDetails)
                    }
                    else {
                        throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved2.first))
                    }
                }
                else {
                    throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved2.first))
                }
            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileOfficerSaved.first))
            }
        }
    }

    @PreAuthorize("hasAuthority('MS_IOP_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun postFuelInspectionDetailsRapidTest(
        referenceNo: String,
        batchReferenceNo: String,
        body: FuelEntityRapidTestDto
    ): FuelInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var fileInspectionDetail = findFuelInspectionDetailByReferenceNumber(referenceNo)
        val batchDetails = findFuelBatchDetailByReferenceNumber(batchReferenceNo)
        val remarksDto = RemarksToAddDto()
        with(remarksDto){
            remarksDescription= body.rapidTestRemarks
            processID = fileInspectionDetail.msProcessId
            userId= loggedInUser.id
        }

        var remarksStatusAdded = "N/A"
        when {
            body.rapidTestStatus -> {
                //Rapid Test Passed
                with(fileInspectionDetail){
                    msProcessId = applicationMapProperties.mapMSEndFuel
                    rapidTestPassed = map.activeStatus
                    rapidTestPassedOn = commonDaoServices.getCurrentDate()
                    rapidTestPassedBy = commonDaoServices.concatenateName(loggedInUser)
                    rapidTestPassedRemarks = body.rapidTestRemarks
                    remarksStatusAdded = "PASSED"
                }

            }
            else -> {
                //Rapid Test Failed
                with(fileInspectionDetail){
                    msProcessId = applicationMapProperties.mapMSSampleCollection
                    rapidTestFailed = map.inactiveStatus
                    rapidTestFailedOn = commonDaoServices.getCurrentDate()
                    rapidTestFailedBy = commonDaoServices.concatenateName(loggedInUser)
                    rapidTestFailedRemarks = body.rapidTestRemarks
                    remarksStatusAdded = "FAILED"
                }
            }
        }
        val fileSaved = updateFuelInspectionDetails(fileInspectionDetail, map, loggedInUser)

        if (fileSaved.first.status == map.successStatus) {
            fileInspectionDetail = fileSaved.second
            remarksDto.remarksStatus = remarksStatusAdded
            val remarksSaved = fuelAddRemarksDetails(fileSaved.second.id,remarksDto, map, loggedInUser)
            if (remarksSaved.first.status == map.successStatus){
                return fuelInspectionMappingCommonDetails(fileInspectionDetail, map, batchDetails)
            }else {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved.first))
            }
        }
        else {
            throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved.first))
        }


    }

    @PreAuthorize("hasAuthority('MS_IOP_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun postFuelInspectionDetailsSampleCollection(
        referenceNo: String,
        batchReferenceNo: String,
        body: SampleCollectionDto
    ): FuelInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var fileInspectionDetail = findFuelInspectionDetailByReferenceNumber(referenceNo)
        val batchDetails = findFuelBatchDetailByReferenceNumber(batchReferenceNo)
        val savedSampleCollection = addSampleCollectAdd(body,fileInspectionDetail,null, map, loggedInUser)

        when (savedSampleCollection.first.status) {
                    map.successStatus -> {
                        body.productsList?.forEach { param->
                            addSampleCollectParamAdd(param,savedSampleCollection.second,map,loggedInUser)
                        }
                        with(fileInspectionDetail){
                            timelineStartDate = commonDaoServices.getCurrentDate()
                            timelineEndDate = applicationMapProperties.mapMSSampleSubmision.let { findProcessNameByID( it, 1).timelinesDay?.let {it2-> commonDaoServices.addYDayToDate(commonDaoServices.getCurrentDate(), it2) } }
                            msProcessId = applicationMapProperties.mapMSSampleSubmision
                            sampleCollectionStatus = map.activeStatus
                        }
                        fileInspectionDetail = updateFuelInspectionDetails(fileInspectionDetail, map, loggedInUser).second
                        return fuelInspectionMappingCommonDetails(fileInspectionDetail, map, batchDetails)
                    }
                    else -> {
                        throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(savedSampleCollection.first))
                    }
                }


    }

    @PreAuthorize("hasAuthority('MS_IOP_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun postFuelInspectionDetailsSampleSubmission(
        referenceNo: String,
        batchReferenceNo: String,
        body: SampleSubmissionDto
    ): FuelInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var fileInspectionDetail = findFuelInspectionDetailByReferenceNumber(referenceNo)
        val batchDetails = findFuelBatchDetailByReferenceNumber(batchReferenceNo)
        val sampleCollected = findSampleCollectedDetailByFuelInspectionID(fileInspectionDetail.id)
        val savedSampleSubmission = addSampleSubmissionAdd(body,fileInspectionDetail,null,sampleCollected?: throw ExpectedDataNotFound("MISSING SAMPLE COLLECTED FOR FUEL INSPECTION REF NO $referenceNo"), map, loggedInUser)

        when (savedSampleSubmission.first.status) {
                    map.successStatus -> {
                        body.parametersList?.forEach { param->
                            addSampleSubmissionParamAdd(param,savedSampleSubmission.second,map,loggedInUser)
                        }
                        with(fileInspectionDetail){
                            timelineStartDate = commonDaoServices.getCurrentDate()
                            timelineEndDate = applicationMapProperties.mapMSBsNumber.let { findProcessNameByID( it, 1).timelinesDay?.let {it2-> commonDaoServices.addYDayToDate(commonDaoServices.getCurrentDate(), it2) } }
                            msProcessId = applicationMapProperties.mapMSBsNumber
                            sampleSubmittedStatus = map.activeStatus
                        }
                        fileInspectionDetail = updateFuelInspectionDetails(fileInspectionDetail, map, loggedInUser).second
                        return fuelInspectionMappingCommonDetails(fileInspectionDetail, map, batchDetails)
                    }
                    else -> {
                        throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(savedSampleSubmission.first))
                    }
                }


    }

    @PreAuthorize("hasAuthority('MS_IOP_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun postFuelInspectionDetailsSampleSubmissionBSNumber(
        referenceNo: String,
        batchReferenceNo: String,
        body: BSNumberSaveDto
    ): FuelInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var fileInspectionDetail = findFuelInspectionDetailByReferenceNumber(referenceNo)
        val batchDetails = findFuelBatchDetailByReferenceNumber(batchReferenceNo)
        val sampleCollected = findSampleCollectedDetailByFuelInspectionID(fileInspectionDetail.id)
        val sampleSubmission = findSampleSubmissionDetailBySampleCollectedID(sampleCollected?.id?: throw ExpectedDataNotFound("MISSING SAMPLE COLLECTED FOR FUEL INSPECTION REF NO $referenceNo"))?: throw ExpectedDataNotFound("MISSING SAMPLE SUBMITTED FOR FUEL INSPECTION WITH REF NO $referenceNo")
        with(sampleSubmission){
            bsNumber = body.bsNumber
            sampleBsNumberDate = body.submittedDate
            sampleBsNumberRemarks = body.remarks
            labResultsStatus = map.inactiveStatus
        }
        val updatedSampleSubmission = sampleSubmissionUpdateDetails(sampleSubmission,map, loggedInUser)
        val remarksDto = RemarksToAddDto()
        with(remarksDto){
            remarksDescription= body.remarks
            remarksStatus= "N/A"
            processID = fileInspectionDetail.msProcessId
            userId= loggedInUser.id
        }

        when (updatedSampleSubmission.first.status) {
            map.successStatus -> {
                val savedBsNumber = ssfSaveBSNumber(updatedSampleSubmission.second,fileInspectionDetail,null,loggedInUser, map)
                when (savedBsNumber.first.status) {
                    map.successStatus -> {
                        with(fileInspectionDetail){
                            timelineStartDate = commonDaoServices.getCurrentDate()
                            timelineEndDate = applicationMapProperties.mapMSPendingLabResults.let { findProcessNameByID( it, 1).timelinesDay?.let {it2-> commonDaoServices.addYDayToDate(commonDaoServices.getCurrentDate(), it2) } }
                            msProcessId = applicationMapProperties.mapMSPendingLabResults
                            userTaskId = applicationMapProperties.mapMSUserTaskNameLAB
                            bsNumberStatus = 1
                        }
                        fileInspectionDetail = updateFuelInspectionDetails(fileInspectionDetail, map, loggedInUser).second
                        val remarksSaved = fuelAddRemarksDetails(fileInspectionDetail.id,remarksDto, map, loggedInUser)
                        when (remarksSaved.first.status) {
                            map.successStatus -> {
                                return fuelInspectionMappingCommonDetails(fileInspectionDetail, map, batchDetails)

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

    @PreAuthorize("hasAuthority('MS_IOP_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun postFuelInspectionDetailsLabPDFSelected(
        referenceNo: String,
        batchReferenceNo: String,
        body: PDFSaveComplianceStatusDto
    ): FuelInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        val fileInspectionDetail = findFuelInspectionDetailByReferenceNumber(referenceNo)
        val batchDetails = findFuelBatchDetailByReferenceNumber(batchReferenceNo)
        val fileContent = limsServices.mainFunctionLimsGetPDF(body.bsNumber, body.PDFFileName)
        val savedPDFLabResultFile = addInspectionSaveLIMSPDFSelected(fileContent,body,true,map,loggedInUser)

        val remarksDto = RemarksToAddDto()
        with(remarksDto){
            remarksDescription= body.complianceRemarks
            remarksStatus = when {
                body.complianceStatus -> {
                    "COMPLIANT"
                }
                else -> {
                    "NON-COMPLIANT"
                }
            }
            processID = applicationMapProperties.mapMSSaveSelectedLabResults
            userId= loggedInUser.id
        }

        if (savedPDFLabResultFile.first.status == map.successStatus) {
            val remarksSaved = fuelAddRemarksDetails(fileInspectionDetail.id,remarksDto, map, loggedInUser)
            if (remarksSaved.first.status == map.successStatus){
                fileInspectionDetail.msProcessId = applicationMapProperties.mapMSComplanceStatus
                val fileSaved = updateFuelInspectionDetails(fileInspectionDetail, map, loggedInUser)
                return fuelInspectionMappingCommonDetails(fileSaved.second, map, batchDetails)
            } else {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(remarksSaved.first))
            }

        }
        else {
            throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(savedPDFLabResultFile.first))
        }
    }

    @PreAuthorize("hasAuthority('MS_IOP_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun postFuelInspectionDetailsSSFSaveComplianceStatus(
        referenceNo: String,
        batchReferenceNo: String,
        body: SSFSaveComplianceStatusDto
    ): FuelInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var fileInspectionDetail = findFuelInspectionDetailByReferenceNumber(referenceNo)
        val batchDetails = findFuelBatchDetailByReferenceNumber(batchReferenceNo)
        val savedSSfComplianceStatus = ssfLabUpdateDetails(body,loggedInUser,map)
        val remarksDto = RemarksToAddDto()
        with(remarksDto){
            remarksDescription= body.complianceRemarks
            remarksStatus = when {
                body.complianceStatus -> {
                    "COMPLIANT"
                }
                else -> {
                    "NON-COMPLIANT"
                }
            }
            processID = fileInspectionDetail.msProcessId
            userId= loggedInUser.id
        }

        if (savedSSfComplianceStatus.first.status == map.successStatus) {
            with(fileInspectionDetail){
                when {
                    body.complianceStatus -> {
                        timelineStartDate = commonDaoServices.getCurrentDate()
                        timelineEndDate = applicationMapProperties.mapMSRemediationSchedule.let { findProcessNameByID( it, 1).timelinesDay?.let {it2-> commonDaoServices.addYDayToDate(commonDaoServices.getCurrentDate(), it2) } }
                        msProcessId = applicationMapProperties.mapMSRemediationSchedule
                        compliantStatus = 1
                        notCompliantStatus =  0
                        remediationStatus =0
                        remediationPaymentStatus = 1
                        compliantStatusDate = commonDaoServices.getCurrentDate()
                        compliantStatusBy = commonDaoServices.concatenateName(loggedInUser)
                        compliantStatusRemarks = body.complianceRemarks
                    }
                    else -> {
                        timelineStartDate = commonDaoServices.getCurrentDate()
                        timelineEndDate = applicationMapProperties.mapMSRemediationInvoice.let { findProcessNameByID( it, 1).timelinesDay?.let {it2-> commonDaoServices.addYDayToDate(commonDaoServices.getCurrentDate(), it2) } }
                        msProcessId = applicationMapProperties.mapMSRemediationInvoice
                        notCompliantStatus =  0
                        compliantStatus = 0
                        notCompliantStatusDate = commonDaoServices.getCurrentDate()
                        notCompliantStatusBy = commonDaoServices.concatenateName(loggedInUser)
                        notCompliantStatusRemarks = body.complianceRemarks
                        remediationStatus = 0
                        remediationPaymentStatus = 0
                    }
                }
            }
            fileInspectionDetail = updateFuelInspectionDetails(fileInspectionDetail, map, loggedInUser).second
            val dataValue = FuelScheduledLabResultsDTO()
            with(dataValue) {
                baseUrl = applicationMapProperties.baseUrlValue
                fullName = fileInspectionDetail.company
                refNumber = fileInspectionDetail.referenceNumber
                compliantDetails =mapCompliantStatusDto(fileInspectionDetail,map)
                dateSubmitted = savedSSfComplianceStatus.second.ssfSubmissionDate
            }

            val remarksSaved = fuelAddRemarksDetails(fileInspectionDetail.id,remarksDto, map, loggedInUser)
            if (remarksSaved.first.status == map.successStatus){
                /*
                * Todo: ADD function for sending Lab results
                * */
                val labReportSentStatus = false
                findSampleSubmittedListPdfBYSSFid(savedSSfComplianceStatus.second.id?: throw ExpectedDataNotFound("Missing SSF ID"))
                    ?.forEach {saveSSFPdf->
                        val fileUploaded = findUploadedFileBYId(saveSSFPdf.msPdfSavedId ?: throw ExpectedDataNotFound("MISSING LAB REPORT FILE ID STATUS"))
                        val fileContent = limsServices.mainFunctionLimsGetPDF(
                            savedSSfComplianceStatus.second.bsNumber ?: throw ExpectedDataNotFound("MISSING LBS NUMBER"),
                            saveSSFPdf.pdfName ?: throw ExpectedDataNotFound("MISSING FILE NAME")
                        )

                        val mappedFileClass = commonDaoServices.mapClass(fileUploaded)
                        runBlocking { commonDaoServices.sendEmailWithUserEmail(
                            fileInspectionDetail.stationOwnerEmail ?: throw ExpectedDataNotFound("Missing Station Owner Email"),
                            applicationMapProperties.mapMsFuelInspectionLabResultsNotification,
                            dataValue,
                            map,
                            savedSSfComplianceStatus.first,
                            fileContent.path
                        )
                        }
                    }
                return fuelInspectionMappingCommonDetails(fileInspectionDetail, map, batchDetails)
            }else {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(remarksSaved.first))
            }

        }
        else {
            throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(savedSSfComplianceStatus.first))
        }
    }

    @PreAuthorize("hasAuthority('MS_IOP_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun postFuelInspectionDetailsRemediationInvoice(
        referenceNo: String,
        batchReferenceNo: String,
        body: CompliantRemediationDto
    ): FuelInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var fileInspectionDetail = findFuelInspectionDetailByReferenceNumber(referenceNo)
        val batchDetails = findFuelBatchDetailByReferenceNumber(batchReferenceNo)
//        val savedSSfComplianceStatus = ssfLabUpdateDetails(body,loggedInUser,map)
        val fuelRemediationInvoice = MsFuelRemedyInvoicesEntity().apply {
            fuelInspectionRefNumber = fileInspectionDetail.referenceNumber
            volumeFuelRemediated = body.volumeFuelRemediated
            subsistenceTotalNights = body.subsistenceTotalNights
            transportAirTicket = body.transportAirTicket
            transportInkm = body.transportInkm
        }
        val savedRemediationInvoice = saveFuelRemediationInvoiceDetails(fuelRemediationInvoice,fileInspectionDetail, loggedInUser,map)
        val fuelRemediation = MsFuelRemediationEntity().apply {
            proFormaInvoiceStatus = 0
            createdBy = commonDaoServices.concatenateName(loggedInUser)
            createdOn = commonDaoServices.getTimestamp()
//            dateOfRemediation = body.dateOfRemediation
        }
        val savedRemediation = saveFuelRemediationDetails(fuelRemediation,fileInspectionDetail.id, loggedInUser,map)

        when (savedRemediationInvoice.first.status) {
            map.successStatus -> {
                /*
                            * Todo add function for sending Email with remediation invoice
                            * */
                val remarksDto = RemarksToAddDto()
                with(remarksDto){
                    remarksDescription= body.remarks
                    remarksStatus= "N/A"
                    processID = fileInspectionDetail.msProcessId
                    userId= loggedInUser.id
                }
                with(fileInspectionDetail){
                    msProcessId = applicationMapProperties.mapMsPayment
                    remediationPaymentStatus = map.inactiveStatus
                    userTaskId = applicationMapProperties.mapMSUserTaskNameSTATIONOWNER
                }
                fileInspectionDetail= updateFuelInspectionDetails(fileInspectionDetail, map, loggedInUser).second


                val remarksSaved = fuelAddRemarksDetails(fileInspectionDetail.id,remarksDto, map, loggedInUser)


                runBlocking { commonDaoServices.sendEmailWithUserEmail(
                    fileInspectionDetail.stationOwnerEmail ?: throw ExpectedDataNotFound("MISSING USER ID"),
                    applicationMapProperties.mapMsFuelInspectionRemediationInvoiceNotification,
                    fileInspectionDetail, map,
                    savedRemediationInvoice.first,
                    invoiceRemediationPDF(fileInspectionDetail.id).path
                )
                }

                return fuelInspectionMappingCommonDetails(fileInspectionDetail, map, batchDetails)
            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(savedRemediationInvoice.first))
            }
        }
    }


    @PreAuthorize("hasAuthority('MS_IOP_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun postFuelInspectionDetailsScheduleRemediationAfterPayment(
        referenceNo: String,
        batchReferenceNo: String,
        body: CompliantRemediationDto
    ): FuelInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var fileInspectionDetail = findFuelInspectionDetailByReferenceNumber(referenceNo)
        val batchDetails = findFuelBatchDetailByReferenceNumber(batchReferenceNo)
//        val savedSSfComplianceStatus = ssfLabUpdateDetails(body,loggedInUser,map)
        var fuelRemediation = MsFuelRemediationEntity()
        findFuelScheduledRemediationDetails(fileInspectionDetail.id)
            ?.let { remediation ->
                with(remediation){
                    dateOfRemediation = body.dateOfRemediation
                    modifiedBy = commonDaoServices.concatenateName(loggedInUser)
                    modifiedOn = commonDaoServices.getTimestamp()
                }
                fuelRemediation = remediation
            }
            ?: kotlin.run {
                with(fuelRemediation){
                    proFormaInvoiceStatus = 1
                    dateOfRemediation = body.dateOfRemediation
                    createdBy = commonDaoServices.concatenateName(loggedInUser)
                    createdOn = commonDaoServices.getTimestamp()
                }
            }

        val savedRemediation = saveFuelRemediationDetails(fuelRemediation,fileInspectionDetail.id, loggedInUser,map)
        when (savedRemediation.first.status) {
            map.successStatus -> {

                    /*
                    * Todo add function for sending Email with remediation scheduled date
                    * */
                val remarksDto = RemarksToAddDto()
                with(remarksDto){
                    remarksDescription= body.remarks
                    remarksStatus= "N/A"
                    processID = fileInspectionDetail.msProcessId
                    userId= loggedInUser.id
                }

                with(fileInspectionDetail){
                    remediationStatus = map.activeStatus
                    msProcessId = applicationMapProperties.mapMSRemediation
                }
                fileInspectionDetail= updateFuelInspectionDetails(fileInspectionDetail, map, loggedInUser).second

                val remarksSaved = fuelAddRemarksDetails(fileInspectionDetail.id,remarksDto, map, loggedInUser)

                val emailValue = FuelScheduledRemediationDateDTO()
                with(emailValue) {
                    baseUrl = applicationMapProperties.baseUrlValue
                    fullName = fileInspectionDetail.company
                    refNumber = fileInspectionDetail.referenceNumber
                    remediationDate = savedRemediation.second.dateOfRemediation
                }

                runBlocking {commonDaoServices.sendEmailWithUserEmail(
                    fileInspectionDetail.stationOwnerEmail ?: throw ExpectedDataNotFound("MISSING USER ID"),
                    applicationMapProperties.mapMsFuelInspectionRemediationScheduleNotification,
                    emailValue,
                    map,
                    savedRemediation.first
                )}
                return fuelInspectionMappingCommonDetails(fileInspectionDetail, map, batchDetails)
            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(savedRemediation.first))
            }
        }

    }

    @PreAuthorize("hasAuthority('MS_IOP_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun postFuelInspectionDetailsUpdateRemediation(
        referenceNo: String,
        batchReferenceNo: String,
        body: RemediationDto
    ): FuelInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var fileInspectionDetail = findFuelInspectionDetailByReferenceNumber(referenceNo)
        val batchDetails = findFuelBatchDetailByReferenceNumber(batchReferenceNo)
        val fuelRemediation = findFuelScheduledRemediationDetails(fileInspectionDetail.id)?: throw ExpectedDataNotFound("NO FUEL REMEDIATION DETAILS FOUND FOR FUEL INSPECTION REF NO $referenceNo ")
            with(fuelRemediation){
                productType = body.productType
                quantityOfFuel = body.quantityOfFuel
                contaminatedFuelType = body.contaminatedFuelType
                applicableKenyaStandard = body.applicableKenyaStandard
                remediationProcedure = body.remediationProcedure
                volumeOfProductContaminated = body.volumeOfProductContaminated
                volumeAdded = body.volumeAdded
                totalVolume = body.totalVolume
                status = map.activeStatus
        }
        val updatedRemediation = updateFuelRemediationDetails(fuelRemediation, loggedInUser,map)

        when (updatedRemediation.first.status) {
            map.successStatus -> {
                with(fileInspectionDetail){
                    timelineStartDate = commonDaoServices.getCurrentDate()
                    timelineEndDate = applicationMapProperties.mapMSEndFuel.let { findProcessNameByID( it, 1).timelinesDay?.let {it2-> commonDaoServices.addYDayToDate(commonDaoServices.getCurrentDate(), it2) } }
                    msProcessId = applicationMapProperties.mapMSEndFuel
                    remendiationCompleteStatus = map.activeStatus
                }

                fileInspectionDetail = updateFuelInspectionDetails(fileInspectionDetail, map, loggedInUser).second
                return fuelInspectionMappingCommonDetails(fileInspectionDetail, map, batchDetails)
            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(updatedRemediation.first))
            }
        }
    }


//    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
//    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
//    fun getFuelInspectionDetailsSampleSubmissionBSNumber(
//        referenceNo: String,
//        batchReferenceNo: String,
//        bsNumber: String
//    ): FuelInspectionDto {
//        val loggedInUser = commonDaoServices.loggedInUserDetails()
//        val map = commonDaoServices.serviceMapDetails(appId)
//        val fileInspectionDetail = findFuelInspectionDetailByReferenceNumber(referenceNo)
//        val batchDetails = findFuelBatchDetailByReferenceNumber(batchReferenceNo)
//        val sampleCollected = findSampleCollectedDetailByFuelInspectionID(fileInspectionDetail.id)
//        val sampleSubmission = findSampleSubmissionDetailBySampleCollectedID(sampleCollected?.id?: throw ExpectedDataNotFound("MISSING SAMPLE COLLECTED FOR FUEL INSPECTION REF NO $referenceNo"))
//        if (bsNumber == sampleSubmission?.bsNumber){
//
//        }else{
//            throw ExpectedDataNotFound("THE BS NUMBER PROVIDED DOES NOT MATCH THE ONE ATTACHED ON SAMPLE SUBMISSION")
//        }
//
//        when (updatedSampleSubmission.first.status) {
//            map.successStatus -> {
//                with(fileInspectionDetail){
//                    userTaskId = applicationMapProperties.mapMSUserTaskNameLAB
//                }
//                val fileSaved = updateFuelInspectionDetails(fileInspectionDetail, map, loggedInUser)
//                return fuelInspectionMappingCommonDetails(fileInspectionDetail, map, batchDetails)
//            }
//            else -> {
//                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(updatedSampleSubmission.first))
//            }
//        }
//    }

    fun findAllUserTaskMSByTaskID() {

    }


    fun invoiceRemediationPDF(
        fuelInspectionID: Long
    ): File {


        val map = hashMapOf<String, Any>()
        map["imagePath"] = commonDaoServices.resolveAbsoluteFilePath(applicationMapProperties.mapKebsLogoPath)
        val invoiceRemediationDetails = fuelRemediationInvoiceRepo.findFirstByFuelInspectionId(fuelInspectionID)
        val fuelRemediationDetailsDto = mapFuelRemediationDetails(invoiceRemediationDetails)

        return reportsDaoService.generateEmailPDFReportWithDataSource(
            "Remediation-Invoice-${fuelRemediationDetailsDto[0].invoiceNumber}.pdf",
            map,
            applicationMapProperties.mapMSFuelInvoiceRemediationPath,
            fuelRemediationDetailsDto
        )
    }

    fun fuelInspectionMappingCommonDetails(
        fileInspectionDetail: MsFuelInspectionEntity,
        map: ServiceMapsEntity,
        batchDetails: MsFuelBatchInspectionEntity
    ): FuelInspectionDto {
        val batchDetailsDto = mapFuelBatchDetailsDto(batchDetails, map)
        val fuelInspectionOfficer = findFuelInspectionOfficerAssigned(fileInspectionDetail, map.activeStatus)
        val fuelInspectionRemarks = findRemarksForFuel(fileInspectionDetail.id)
        val officerList = commonDaoServices.findOfficersListBasedOnRegionCountyAndRole(
                applicationMapProperties.mapMSMappedOfficerROLEID,
                batchDetails.countyId ?: throw ExpectedDataNotFound("MISSING BATCH COUNTY ID"),
                batchDetails.regionId ?: throw ExpectedDataNotFound("MISSING BATCH REGION ID")
            )


        val rapidTestStatus = mapRapidTestDto(fileInspectionDetail, map)
        var rapidTestDone = false
        if (rapidTestStatus!=null){
            rapidTestDone = true
        }

        var timelineOverDue =false
        if (fileInspectionDetail.timelineEndDate!= null){
        if (fileInspectionDetail.timelineEndDate!!>commonDaoServices.getCurrentDate()){
            timelineOverDue = true
        }}

        val compliantDetailsStatus = mapCompliantStatusDto(fileInspectionDetail, map)
        var compliantStatusDone = false
        if (compliantDetailsStatus!=null){
            compliantStatusDone = true
        }
        val sampleCollected = findSampleCollectedDetailByFuelInspectionID(fileInspectionDetail.id)
        val sampleCollectedParamList = sampleCollected?.id?.let { findAllSampleCollectedParametersBasedOnSampleCollectedID(it) }
        val sampleCollectedDtoValues = sampleCollectedParamList?.let { mapSampleCollectedParamListDto(it) }?.let { mapSampleCollectedDto(sampleCollected, it) }

        val sampleSubmitted = findSampleSubmissionDetailByFuelInspectionID(fileInspectionDetail.id)
        val sampleSubmittedParamList = sampleSubmitted?.id?.let { findAllSampleSubmissionParametersBasedOnSampleSubmissionID(it) }
        val sampleSubmittedDtoValues = sampleSubmittedParamList?.let { mapSampleSubmissionParamListDto(it) }?.let { mapSampleSubmissionDto(sampleSubmitted, it) }

        val labResultsParameters = sampleSubmitted?.bsNumber?.let { findSampleLabTestResultsRepoBYBSNumber(it) }
        val ssfDetailsLab = findSampleSubmittedBYFuelInspectionId(fileInspectionDetail.id)
        val savedPDFFilesLims = ssfDetailsLab?.id?.let { findSampleSubmittedListPdfBYSSFid(it)?.let { ssfDetails->mapLabPDFFilesListDto(ssfDetails) } }
//        val savedPDFFilesLims = ssfDetailsLab?.id?.let { findSampleSubmittedListPdfBYSSFid(it)?.let { mapLabPDFFilesListDto(it) } }
        val ssfResultsListCompliance = ssfDetailsLab?.let { mapSSFComplianceStatusDetailsDto(it) }
        val limsPDFFiles = ssfDetailsLab?.bsNumber?.let { mapLIMSSavedFilesDto(it,savedPDFFilesLims)}
        val labResultsDto = mapLabResultsDetailsDto(ssfResultsListCompliance,savedPDFFilesLims,limsPDFFiles,labResultsParameters?.let { mapLabResultsParamListDto(it) })
        val remediationDetails = findFuelScheduledRemediationDetails(fileInspectionDetail.id)
        val invoiceRemediationDetails = fuelRemediationInvoiceRepo.findFirstByFuelInspectionId(fileInspectionDetail.id)
        var invoiceCreatedStatus = false
        if (invoiceRemediationDetails.isNotEmpty()){
            invoiceCreatedStatus = true
        }
        val fuelRemediationDto = remediationDetails?.let { mapFuelRemediationDto(it,invoiceCreatedStatus) }
        return mapFuelInspectionDto(
            fileInspectionDetail,
            batchDetailsDto,
            rapidTestDone,
            compliantStatusDone,
            timelineOverDue,
            officerList,
            fuelInspectionRemarks,
            fuelInspectionOfficer?.assignedIo?.let { commonDaoServices.findUserByID(it) },
            rapidTestStatus,
            sampleCollectedDtoValues,
            sampleSubmittedDtoValues,
            labResultsDto,
            fuelRemediationDto
        )
    }


    fun fuelCreateBatch(
        body: BatchFileFuelSaveDto,
        fuelPlanYearName :WorkplanYearsCodesEntity,
        fuelPlanMonthName :WorkplanMonthlyCodesEntity,
        map: ServiceMapsEntity,
        user: UsersEntity
    ): Pair<ServiceRequestsEntity, MsFuelBatchInspectionEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var fuel = MsFuelBatchInspectionEntity()
        try {
            val county = commonDaoServices.findCountiesEntityByCountyId(body.county, map.activeStatus)
            val regionIDValue = county.regionId ?: ExpectedDataNotFound("The following County ${county.county}, with ID  = ${county.id} and status = ${county.status}, does not have a region mapped to")
            val userFuelScheduleBatch = findFuelBatchDetailByYearNameIDCountyRegionAndTown(fuelPlanYearName.id,fuelPlanMonthName.id, body.county,body.town,commonDaoServices.findRegionEntityByRegionID(regionIDValue as Long, map.activeStatus).id)
            when (userFuelScheduleBatch) {
                null -> {
                    with(fuel) {
                        referenceNumber = "FL/BATCH/${
                            generateRandomText(
                                5,
                                map.secureRandom,
                                map.messageDigestAlgorithm,
                                true
                            )
                        }".toUpperCase()
                        countyId = county.id
                        regionId = commonDaoServices.findRegionEntityByRegionID(regionIDValue as Long, map.activeStatus).id
                        townId = commonDaoServices.findTownEntityByTownId(body.town).id
                        yearNameId = fuelPlanYearName.id
                        monthNameId = fuelPlanMonthName.id
                        remarks = body.remarks
                        transactionDate = commonDaoServices.getCurrentDate()
                        status = map.initStatus
                        batchClosed = map.inactiveStatus
                        userTaskId = applicationMapProperties.mapMSUserTaskNameEPRA
                        createdBy = commonDaoServices.concatenateName(user)
                        createdOn = commonDaoServices.getTimestamp()
                    }
                    fuel = fuelBatchRepo.save(fuel)

                    sr.payload = commonDaoServices.createJsonBodyFromEntity(fuel).toString()
                    sr.names = "Fuel creation of batch file"

                    sr.responseStatus = sr.serviceMapsId?.successStatusCode
                    sr.responseMessage = "Success ${sr.payload}"
                    sr.status = map.successStatus
                    sr = serviceRequestsRepo.save(sr)
                    sr.processingEndDate = Timestamp.from(Instant.now())
                }
                else -> {
                    throw ExpectedDataNotFound("You already have a fuel schedule batch plan for the Year(${fuelPlanYearName.yearName}) and Month (${fuelPlanMonthName.description})")
                }
            }


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
        return Pair(sr, fuel)
    }

    fun fuelInspectionDetailsAssignOfficer(
        body: FuelEntityAssignOfficerDto,
        fileInspectionDetail: MsFuelInspectionEntity,
        map: ServiceMapsEntity,
        user: UsersEntity
    ): Pair<ServiceRequestsEntity, MsFuelInspectionOfficersEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var fuelOfficerInspectEntity = MsFuelInspectionOfficersEntity()
        try {
            //If the file exists update it if not move to next function
            fuelInspectionOfficerRepo.findByMsFuelInspectionIdAndStatus(fileInspectionDetail.id,map.activeStatus)
                ?.let { it->
                    with(it){
                        remarks = body.remarks
                        status = map.inactiveStatus
                        lastModifiedBy = commonDaoServices.concatenateName(user)
                        lastModifiedOn = commonDaoServices.getTimestamp()
                    }
                    fuelInspectionOfficerRepo.save(it)
                }

            with(fuelOfficerInspectEntity) {
                assignedIo = body.assignedUserID
                remarks = body.remarks
                transactionDate = commonDaoServices.getCurrentDate()
                status = map.activeStatus
                createdBy = commonDaoServices.concatenateName(user)
                createdOn = commonDaoServices.getTimestamp()
                msFuelInspectionId = fileInspectionDetail.id
            }
            fuelOfficerInspectEntity = fuelInspectionOfficerRepo.save(fuelOfficerInspectEntity)

            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(fuelOfficerInspectEntity)}"
            sr.names = "Fuel Inspection Assign Officer Details"

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
        return Pair(sr, fuelOfficerInspectEntity)
    }

    fun updateFuelBatch(
        body: MsFuelBatchInspectionEntity,
        map: ServiceMapsEntity,
        user: UsersEntity
    ): Pair<ServiceRequestsEntity, MsFuelBatchInspectionEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var fuelBatch = body
        try {
            with(fuelBatch) {
                lastModifiedBy = commonDaoServices.concatenateName(user)
                lastModifiedOn = commonDaoServices.getTimestamp()
            }
            fuelBatch = fuelBatchRepo.save(fuelBatch)

            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(fuelBatch)}"
            sr.names = "Fuel Inspection Batch Update file"

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
        return Pair(sr, fuelBatch)
    }

    fun addSampleCollectAdd(
        body: SampleCollectionDto,
        fuelInspectionDetail: MsFuelInspectionEntity?,
        workPlanInspectionDetail: MsWorkPlanGeneratedEntity?,
        map: ServiceMapsEntity,
        user: UsersEntity
    ): Pair<ServiceRequestsEntity, MsSampleCollectionEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var fuelSampleCollect = MsSampleCollectionEntity()
        try {
            with(fuelSampleCollect) {
                nameManufacturerTrader = body.nameManufacturerTrader
                addressManufacturerTrader = body.addressManufacturerTrader
                samplingMethod = body.samplingMethod
                reasonsCollectingSamples = body.reasonsCollectingSamples
                anyRemarks = body.anyRemarks
                designationOfficerCollectingSample = body.designationOfficerCollectingSample
                nameOfficerCollectingSample = body.nameOfficerCollectingSample
                dateOfficerCollectingSample = body.dateOfficerCollectingSample
                nameWitness = body.nameWitness
                designationWitness = body.designationWitness
                dateWitness = body.dateWitness
                if (fuelInspectionDetail !=null){
                    msFuelInspectionId = fuelInspectionDetail.id
                }else if (workPlanInspectionDetail != null){
                    workPlanGeneratedID = workPlanInspectionDetail.id
                }

                status = map.activeStatus
                createdBy = commonDaoServices.concatenateName(user)
                createdOn = commonDaoServices.getTimestamp()
            }
            fuelSampleCollect = sampleCollectRepo.save(fuelSampleCollect)

            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(fuelSampleCollect)} "
            sr.names = "Fuel Inspection Sample Collect Save file"

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
        return Pair(sr, fuelSampleCollect)
    }

    fun addSampleCollectParamAdd(
        body: SampleCollectionItemsDto,
        fuelSampleCollect: MsSampleCollectionEntity,
        map: ServiceMapsEntity,
        user: UsersEntity
    ): Pair<ServiceRequestsEntity, MsCollectionParametersEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var param = MsCollectionParametersEntity()
        try {
            with(param) {
                productBrandName = body.productBrandName
                batchNo = body.batchNo
                batchSize = body.batchSize
                sampleSize = body.sampleSize
                sampleCollectionId = fuelSampleCollect.id
                status = map.activeStatus
                createdBy = commonDaoServices.concatenateName(user)
                createdOn = commonDaoServices.getTimestamp()
            }
            param = sampleCollectParameterRepo.save(param)

            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(param)} "
            sr.names = "Fuel Inspection Sample Collect  Product Save file"

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

    fun fuelAddRemarksDetails(
        flInspId: Long,
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
                if (flBatchId==null){
                    fuelInspectionId = flInspId
                }else{
                    fuelBatchId = flBatchId
                }
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

    fun addSampleSubmissionAdd(
        body: SampleSubmissionDto,
        fuelInspectionDetail: MsFuelInspectionEntity?,
        workPlanInspectionDetail: MsWorkPlanGeneratedEntity?,
        sampleCollected: MsSampleCollectionEntity?,
        map: ServiceMapsEntity,
        user: UsersEntity
    ): Pair<ServiceRequestsEntity, MsSampleSubmissionEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var sampleSubmission = MsSampleSubmissionEntity()
        try {
            with(sampleSubmission) {
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
                sendersDate = body.sendersDate
                receiversName = body.receiversName
                testChargesKsh = body.testChargesKsh
                receiptLpoNumber = body.receiptLpoNumber
                invoiceNumber = body.invoiceNumber
                disposal = body.disposal
                remarks = body.remarks

                if (sampleCollected !=null){
                    sampleCollectionNumber = sampleCollected.id
                }

                if (fuelInspectionDetail !=null){
                    msFuelInspectionId = fuelInspectionDetail.id
                }else if (workPlanInspectionDetail != null){
                    workPlanGeneratedID = workPlanInspectionDetail.id
                }
                status = map.activeStatus
                createdBy = commonDaoServices.concatenateName(user)
                createdOn = commonDaoServices.getTimestamp()
            }
            sampleSubmission = sampleSubmitRepo.save(sampleSubmission)

            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(sampleSubmission)} "
            sr.names = "Fuel Inspection or WorkPlan Sample Submission Save file"

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
        return Pair(sr, sampleSubmission)
    }

    fun addSampleSubmissionParamAdd(
        body: SampleSubmissionItemsDto,
        fuelSampleSubmission: MsSampleSubmissionEntity,
        map: ServiceMapsEntity,
        user: UsersEntity
    ): Pair<ServiceRequestsEntity, MsLaboratoryParametersEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var param = MsLaboratoryParametersEntity()
        try {
                with(param) {
                    parameters = body.parameters
                    laboratoryName = body.laboratoryName
                    sampleSubmissionId = fuelSampleSubmission.id
                    status = map.activeStatus
                    createdBy = commonDaoServices.concatenateName(user)
                    createdOn = commonDaoServices.getTimestamp()
                }
                param = sampleSubmitParameterRepo.save(param)

            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(param)} "
            sr.names = "Fuel Inspection Sample Submission  Parameter To be tested Save file"

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

    fun ssfSaveBSNumber(
        sampleSubmission: MsSampleSubmissionEntity,
        fuelInspectionDetail: MsFuelInspectionEntity?,
        workPlanInspectionDetail: MsWorkPlanGeneratedEntity?,
        user: UsersEntity,
        map: ServiceMapsEntity
    ): Pair<ServiceRequestsEntity, QaSampleSubmissionEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var saveSSFBSNumber = QaSampleSubmissionEntity()
        try {
            sampleSubmissionLabRepo.findByBsNumber(sampleSubmission.bsNumber?.toUpperCase()?: throw ExpectedDataNotFound("MISSING BS NUMBER"))
                ?.let {
                     throw ExpectedDataNotFound("BS NUMBER ALREADY EXIST")
                }
                ?: kotlin.run {
                    with(saveSSFBSNumber) {
                        ssfNo = sampleSubmission.sampleReferences
                        brandName = sampleSubmission.nameProduct
                        ssfSubmissionDate = sampleSubmission.sendersDate
                        bsNumber = sampleSubmission.bsNumber?.toUpperCase()
                        if (fuelInspectionDetail !=null){
                            fuelInspectionId = fuelInspectionDetail.id
                        }else if (workPlanInspectionDetail != null){
                            workplanGeneratedId = workPlanInspectionDetail.id
                        }
                        status = map.activeStatus
                        labResultsStatus = map.inactiveStatus
                        createdBy = commonDaoServices.concatenateName(user)
                        createdOn = commonDaoServices.getTimestamp()
                    }

                    saveSSFBSNumber = sampleSubmissionLabRepo.save(saveSSFBSNumber)

                    sr.payload = "${commonDaoServices.createJsonBodyFromEntity(saveSSFBSNumber)} "
                    sr.names = "Fuel Inspection Sample Submission Bs Number Submitted Save file"

                    sr.responseStatus = sr.serviceMapsId?.successStatusCode
                    sr.responseMessage = "Success ${sr.payload}"
                    sr.status = map.successStatus
                    sr = serviceRequestsRepo.save(sr)
                    sr.processingEndDate = Timestamp.from(Instant.now())
                }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(sampleSubmission)}"
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepo.save(sr)

        }

        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return Pair(sr, saveSSFBSNumber)
    }


    fun sampleSubmissionUpdateDetails(
        sampleSubmission: MsSampleSubmissionEntity,
        map: ServiceMapsEntity,
        user: UsersEntity
    ): Pair<ServiceRequestsEntity, MsSampleSubmissionEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var UpdateSampleSubmission = sampleSubmission
        try {
            with(UpdateSampleSubmission) {
                modifiedBy = commonDaoServices.concatenateName(user)
                modifiedOn = commonDaoServices.getTimestamp()
            }
            UpdateSampleSubmission = sampleSubmitRepo.save(UpdateSampleSubmission)

            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(UpdateSampleSubmission)} "
            sr.names = "Sample Submission Update file"

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = map.successStatus
            sr = serviceRequestsRepo.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(sampleSubmission)}"
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepo.save(sr)

        }
        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return Pair(sr, UpdateSampleSubmission)
    }

    fun addInspectionSaveLIMSPDFSelected(
        fileContent: File,
        body: PDFSaveComplianceStatusDto,
        isFuelInspection: Boolean,
        map: ServiceMapsEntity,
        user: UsersEntity
    ): Pair<ServiceRequestsEntity, QaSampleSubmissionEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        val ssfDetails = findSampleSubmittedBYID(body.ssfID)
        try {

            val docFile = commonDaoServices.convertFileToMultipartFile(fileContent)
            var upload = MsUploadsEntity()
            with(upload) {
                when {
                    isFuelInspection -> {
                        msFuelInspectionId = ssfDetails.fuelInspectionId
                    }
                    else -> {
                        msWorkplanGeneratedId= ssfDetails.workplanGeneratedId
                    }
                }

                ssfUploads = 1
                ordinaryStatus = 0
                versionNumber = 1
                name = fileContent.name
                fileType = commonDaoServices.getFileTypeByMimetypesFileTypeMap(docFile.name)
                documentType = "LAB RESULTS PDF"
                document = docFile.bytes
                transactionDate = commonDaoServices.getCurrentDate()
                status = 1
                createdBy = commonDaoServices.concatenateName(user)
                createdOn = commonDaoServices.getTimestamp()
            }
            upload = msUploadRepo.save(upload)
//            upload = uploadMSFile(upload, commonDaoServices.convertFileToMultipartFile(fileContent), "LAB RESULTS PDF", user, map).second

            var ssfPdfDetails = QaSampleSubmittedPdfListDetailsEntity()
            with(ssfPdfDetails) {
                complianceRemarks = body.complianceRemarks
                complianceStatus = when {
                    body.complianceStatus -> {
                        1
                    }else -> {
                        0
                    }
                }
                sffId = ssfDetails.id
                pdfName = fileContent.name
                msPdfSavedId = upload.id
                status = map.activeStatus
                createdBy = commonDaoServices.concatenateName(user)
                createdOn = commonDaoServices.getTimestamp()
            }
            ssfPdfDetails = sampleSubmissionSavedPdfListRepo.save(ssfPdfDetails)

            sr.payload = "SSF Updated [updatePermit= ${ssfDetails.id}]"
            sr.names = "LIMS LAB RESULTS PDF FILE SAVED"
            sr.varField1 = "${ssfDetails.id}"

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = map.successStatus
            sr = serviceRequestsRepo.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepo.save(sr)

        }

        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return Pair(sr, ssfDetails)
    }

    fun ssfLabUpdateDetails(
        body: SSFSaveComplianceStatusDto,
        user: UsersEntity,
        map: ServiceMapsEntity
    ): Pair<ServiceRequestsEntity, QaSampleSubmissionEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var saveSSF = findSampleSubmittedBYID(body.ssfID)
        try {

            with(saveSSF) {
                complianceRemarks = body.complianceRemarks
                resultsAnalysis = when {
                    body.complianceStatus -> {
                        1
                    }
                    else -> {
                        0
                    }
                }
                modifiedBy = commonDaoServices.concatenateName(user)
                modifiedOn = commonDaoServices.getTimestamp()
            }

            saveSSF = sampleSubmissionLabRepo.save(saveSSF)

            sr.payload = "New SSF Saved [BRAND name${saveSSF.brandName} and ${saveSSF.id}]"
            sr.names = "${saveSSF.brandName}"

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = map.successStatus
            sr = serviceRequestsRepo.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(saveSSF)} and ${commonDaoServices.createJsonBodyFromEntity(body)}"
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepo.save(sr)

        }

        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return Pair(sr, saveSSF)
    }

    fun uploadMSFile(
        body: MsUploadsEntity,
        docFile: MultipartFile,
        doc: String,
        user: UsersEntity,
        map: ServiceMapsEntity
    ): Pair<ServiceRequestsEntity, MsUploadsEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var onsiteUploads = body
        try {

            with(onsiteUploads) {
                name = commonDaoServices.saveDocuments(docFile)
                fileType = commonDaoServices.getFileTypeByMimetypesFileTypeMap(docFile.name)
                documentType = doc
                document = docFile.bytes
                transactionDate = commonDaoServices.getCurrentDate()
                status = 1
                createdBy = commonDaoServices.concatenateName(user)
                createdOn = commonDaoServices.getTimestamp()
            }
            onsiteUploads = msUploadRepo.save(onsiteUploads)

            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(onsiteUploads)} "
            sr.names = "File Upload"

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = map.successStatus
            sr = serviceRequestsRepo.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(onsiteUploads)} and ${commonDaoServices.createJsonBodyFromEntity(onsiteUploads)}"
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepo.save(sr)
        }
        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return Pair(sr, onsiteUploads)
    }


 fun saveFuelRemediationDetails(
        body: MsFuelRemediationEntity,
        fuelInspectionID: Long,
        user: UsersEntity,
        map: ServiceMapsEntity
    ): Pair<ServiceRequestsEntity, MsFuelRemediationEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var fuelRemediation = body
        try {

            with(fuelRemediation) {
                fuelInspectionId = fuelInspectionID
                status = map.activeStatus
            }
            fuelRemediation = fuelRemediationRepo.save(fuelRemediation)

            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(fuelRemediation)}"
            sr.names = "Fuel Remediation create"

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = map.successStatus
            sr = serviceRequestsRepo.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(fuelRemediation)}"
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepo.save(sr)

        }

        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return Pair(sr, fuelRemediation)
    }

    fun updateFuelRemediationDetails(
        body: MsFuelRemediationEntity,
        user: UsersEntity,
        map: ServiceMapsEntity
    ): Pair<ServiceRequestsEntity, MsFuelRemediationEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var fuelRemediation = body
        try {

            with(fuelRemediation) {
                modifiedBy = commonDaoServices.concatenateName(user)
                modifiedOn = commonDaoServices.getTimestamp()
            }
            fuelRemediation = fuelRemediationRepo.save(fuelRemediation)

            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(fuelRemediation)}"
            sr.names = "Fuel Remediation create"

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = map.successStatus
            sr = serviceRequestsRepo.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(fuelRemediation)}"
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepo.save(sr)

        }

        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return Pair(sr, fuelRemediation)
    }

    fun saveFuelRemediationInvoiceDetails(
        fuelRemediationInvoiceEntity: MsFuelRemedyInvoicesEntity,
        fuelInspection: MsFuelInspectionEntity,
        user: UsersEntity,
        map: ServiceMapsEntity
    ): Pair<ServiceRequestsEntity, MsFuelRemedyInvoicesEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var fuelRemediationInvoice = fuelRemediationInvoiceEntity
        try {
            val remuneration = fuelRemediationInvoiceEntity.volumeFuelRemediated?.let { fuelRemunerationRateVatCalculation(it) }
            val subsistence = fuelRemediationInvoiceEntity.subsistenceTotalNights?.let { fuelSubsistenceRateVatCalculation(it) }
            val transportAirTickets = fuelTransportAirTicketsRateVatCalculation(fuelRemediationInvoiceEntity.transportAirTicket, fuelRemediationInvoiceEntity.transportInkm)

            val rateValue = 0
            val subTotal = 1
            val vatTotal = 2
            val totalValue = 3

//            var fuelRemediationInvoice = fuelRemediationInvoiceEntity
            with(fuelRemediationInvoice) {
                invoiceNumber = applicationMapProperties.mapInvoicesPrefix + generateRandomText(5, map.secureRandom, map.messageDigestAlgorithm, true).toUpperCase()
                //Part of Remuneration calculation
                volumeFuelRemediated = fuelRemediationInvoiceEntity.volumeFuelRemediated
                remunerationRateLiter = remuneration?.get(rateValue)
                remunerationSubTotal = remuneration?.get(subTotal)
                remunerationVat = remuneration?.get(vatTotal)?.toBigDecimal()
                remunerationTotal = remuneration?.get(totalValue)?.toBigDecimal()

                //Part of Subsistence calculation
                subsistenceTotalNights = fuelRemediationInvoiceEntity.subsistenceTotalNights
                subsistenceRate = subsistence?.get(rateValue)
                subsistenceRateNightTotal = subsistence?.get(subTotal)
                subsistenceVat = subsistence?.get(vatTotal)?.toBigDecimal()
                subsistenceTotal = subsistence?.get(totalValue)?.toBigDecimal()

                //Part of Subsistence calculation
                transportAirTicket = fuelRemediationInvoiceEntity.transportAirTicket
                transportInkm = fuelRemediationInvoiceEntity.transportInkm
                transportRate = transportAirTickets[rateValue]
                transportTotalKmrate = transportAirTickets[subTotal]
                transportVat = transportAirTickets[vatTotal]?.toBigDecimal()
                transportTotal = transportAirTickets[totalValue]?.toBigDecimal()

                //All calculated Grand total Value
                transportGrandTotal = remunerationTotal?.let { subsistenceTotal?.let { it1 -> transportTotal?.let { it2 -> fuelGrandTotal(it, it1, it2) } } }
                totalTaxAmount = transportVat?.let { subsistenceVat?.let { it1 -> remunerationVat?.let { it2 -> fuelGrandTaxTotal(it, it1, it2) } } }

                fuelInspectionId = fuelInspection.id
                transactionDate =commonDaoServices.getCurrentDate()
                invoiceDate = commonDaoServices.getCurrentDate()
                status = activeStatus
                createdBy = commonDaoServices.concatenateName(user)
                createdOn = commonDaoServices.getTimestamp()
            }
            fuelRemediationInvoice = fuelRemediationInvoiceRepo.save(fuelRemediationInvoice)

            val batchInvoiceDetail = invoiceDaoService.createBatchInvoiceDetails(
                commonDaoServices.concatenateName(user),
                fuelRemediationInvoice.invoiceNumber ?: throw Exception("MISSING INVOICE NUMBER")
            )

            val updateBatchInvoiceDetail = invoiceDaoService.addInvoiceDetailsToBatchInvoice(
                fuelRemediationInvoice,
                applicationMapProperties.mapInvoiceTransactionsForMSFuelReconciliation,
                user,
                batchInvoiceDetail
            )

            //Todo: Payment selection
            val myAccountDetails = InvoiceDaoService.InvoiceAccountDetails()
            with(myAccountDetails) {
                reveneCode = applicationMapProperties.mapRevenueCodeForMSFuelInspection
                revenueDesc = applicationMapProperties.mapRevenueDescForMSFuelInspection
                accountName = fuelInspection.company
                accountNumber = fuelInspection.companyKraPin
                currency = applicationMapProperties.mapInvoiceTransactionsLocalCurrencyPrefix
            }

            invoiceDaoService.createPaymentDetailsOnStgReconciliationTable(
                commonDaoServices.concatenateName(user),
                updateBatchInvoiceDetail,
                myAccountDetails,
                applicationMapProperties.mapInvoiceTransactionsForMSFuelReconciliation
            )

            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(fuelRemediationInvoice)}"
            sr.names = "Fuel Remediation create and saved to Payment Reconciliation table"

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = map.successStatus
            sr = serviceRequestsRepo.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(fuelRemediationInvoice)}"
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepo.save(sr)

        }

        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return Pair(sr, fuelRemediationInvoice)
    }

    fun findMSInvoicesWithRefNO(refNumber: String): MsFuelRemedyInvoicesEntity {
        fuelRemediationInvoiceRepo.findByInvoiceNumber(refNumber)
            ?.let { it ->
                return it
            }
            ?: throw ExpectedDataNotFound("Invoices With [refNumber = ${refNumber}], does not Exist")
    }



    fun fuelCreateSchedule(
        body: FuelEntityDto,
        batchIdFound: Long,
        map: ServiceMapsEntity,
        user: UsersEntity
    ): Pair<ServiceRequestsEntity, MsFuelInspectionEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var fuelDetail = MsFuelInspectionEntity()
        try {
            with(fuelDetail) {
                referenceNumber = "FL/FILE/${generateRandomText(5, map.secureRandom, map.messageDigestAlgorithm, true)}".toUpperCase()
                company = body.company
                batchId = batchIdFound
                petroleumProduct = body.petroleumProduct
                physicalLocation = body.physicalLocation
                inspectionDateFrom = body.inspectionDateFrom
                inspectionDateTo = body.inspectionDateTo
                stationOwnerEmail = body.stationOwnerEmail
                companyKraPin = body.stationKraPin
                userTaskId = applicationMapProperties.mapMSUserTaskNameEPRA
                msProcessId = applicationMapProperties.mapMSCloseBatch
                remarks = body.remarks
                transactionDate = commonDaoServices.getCurrentDate()
                status = map.initStatus
                createdBy = commonDaoServices.concatenateName(user)
                createdOn = commonDaoServices.getTimestamp()
            }
            fuelDetail = fuelInspectionRepo.save(fuelDetail)

            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(fuelDetail)}"
            sr.names = "Fuel Schedule Created"

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
        return Pair(sr, fuelDetail)
    }

    fun updateFuelInspectionDetails(
        body: MsFuelInspectionEntity,
        map: ServiceMapsEntity,
        user: UsersEntity
    ): Pair<ServiceRequestsEntity, MsFuelInspectionEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var fuelBatch = body
        try {
            with(fuelBatch) {
                lastModifiedBy = commonDaoServices.concatenateName(user)
                lastModifiedOn = commonDaoServices.getTimestamp()
            }
            fuelBatch = fuelInspectionRepo.save(fuelBatch)

            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(fuelBatch)}"
            sr.names = "Fuel Inspection Update file"

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
        return Pair(sr, fuelBatch)
    }

    fun updateFuelInspectionRemediationInvoiceDetails(
        body: MsFuelRemedyInvoicesEntity,
        map: ServiceMapsEntity,
        user: String
    ): Pair<ServiceRequestsEntity, MsFuelRemedyInvoicesEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var fuelRemediationInvoice = body
        try {
            with(fuelRemediationInvoice) {
                lastModifiedBy = user
                lastModifiedOn = commonDaoServices.getTimestamp()
            }
            fuelRemediationInvoice = fuelRemediationInvoiceRepo.save(fuelRemediationInvoice)

            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(fuelRemediationInvoice)}"
            sr.names = "Fuel Inspection Remediation Invoice Updated file"

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
        return Pair(sr, fuelRemediationInvoice)
    }

    fun findAllFuelInspectionListBasedOnBatchID(batchId: Long): List<MsFuelInspectionEntity> {
        fuelInspectionRepo.findAllByBatchId(batchId)
            ?.let {
                return it
            }
            ?: throw ExpectedDataNotFound("No Files found with the following BATCH ID ${batchId}")
    }

    fun findAllFuelInspectionListBasedOnBatchIDPageRequest(batchId: Long,pageable: Pageable): Page<MsFuelInspectionEntity> {
        fuelInspectionRepo.findAllByBatchId(batchId,pageable)
            ?.let {
                return it
            }
            ?: throw ExpectedDataNotFound("No Fuel Schedules found with the following BATCH ID $batchId")
    }

    fun findAllFuelInspectionListBasedOnBatchIDAndAssignedIO(batchId: Long,assignedIoID: Long): List<MsFuelInspectionEntity>? {
       return  fuelInspectionRepo.findAllByBatchIdAndAssignOfficer(batchId,assignedIoID)
//            ?.let {
//                return it
//            }
//            ?: throw ExpectedDataNotFound("No Fuel Schedules found with the following BATCH ID $batchId")
    }

    fun findAllSampleCollectedParametersBasedOnSampleCollectedID(sampleCollectedID: Long): List<MsCollectionParametersEntity>? {
        return sampleCollectParameterRepo.findBySampleCollectionId(sampleCollectedID)
    }

    fun findAllSampleSubmissionParametersBasedOnSampleSubmissionID(sampleSubmissionID: Long): List<MsLaboratoryParametersEntity>? {
        return sampleSubmitParameterRepo.findBySampleSubmissionId(sampleSubmissionID)
    }

    fun findFuelInspectionDetailByReferenceNumber(referenceNumber: String): MsFuelInspectionEntity {
        fuelInspectionRepo.findByReferenceNumber(referenceNumber)
            ?.let {
                return it
            }
            ?: throw ExpectedDataNotFound("No Fuel Scheduled found with the following Ref No $referenceNumber")
    }

    fun findFuelInspectionDetailByID(id: Long): MsFuelInspectionEntity {
        fuelInspectionRepo.findByIdOrNull(id)
            ?.let {
                return it
            }
            ?: throw ExpectedDataNotFound("No Fuel Scheduled found with the following ID $id")
    }

    fun findSampleCollectedDetailByFuelInspectionID(fuelInspectionID: Long): MsSampleCollectionEntity? {
        return  sampleCollectRepo.findByMsFuelInspectionId(fuelInspectionID)
    }

    fun findSampleLabTestResultsRepoBYBSNumber(bsNumber: String): List<QaSampleLabTestResultsEntity>? {
        return  sampleLabTestResultsRepo.findByOrderId(bsNumber)
//            ?.let {
//            return it
//        } ?: throw ExpectedDataNotFound("No Results found with the following [bsNumber=$bsNumber]")
    }

    fun findSampleSubmissionDetailBySampleCollectedID(sampleCollectedID: Long): MsSampleSubmissionEntity? {
        return  sampleSubmitRepo.findBySampleCollectionNumber(sampleCollectedID)
    }

    fun findSampleSubmissionDetailByFuelInspectionID(fuelInspectionID: Long): MsSampleSubmissionEntity? {
        return  sampleSubmitRepo.findByMsFuelInspectionId(fuelInspectionID)
    }

//    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun updateRemediationDetailsAfterPaymentDone(inv: MsFuelRemedyInvoicesEntity) {
        val map = commonDaoServices.serviceMapDetails(appId)

                   var remediationDetails = findFuelScheduledRemediationDetails(inv.fuelInspectionId?: throw ExpectedDataNotFound("MISSING FUEL INSPECTION ID"))?: throw ExpectedDataNotFound("NO REMEDIATION DETAILS FOUND")
                    with(remediationDetails){
                        proFormaInvoiceNo = inv.sageInvoiceNumber
                        invoiceAmount = inv.transportGrandTotal
                        feePaidReceiptNo = inv.receiptNo
                        dateOfPayment = inv.paymentDate
                        modifiedBy = "SCHEDULER"
                        modifiedOn = commonDaoServices.getTimestamp()
                    }
                    remediationDetails = fuelRemediationRepo.save(remediationDetails)

                    var fuelInspection = findFuelInspectionDetailByID(remediationDetails.fuelInspectionId?: throw ExpectedDataNotFound("MISSING FUEL INSPECTION ID"))
                    with(fuelInspection){
                        timelineStartDate = commonDaoServices.getCurrentDate()
                        timelineEndDate = applicationMapProperties.mapMSRemediationSchedule.let { findProcessNameByID( it, 1).timelinesDay?.let {it2-> commonDaoServices.addYDayToDate(commonDaoServices.getCurrentDate(), it2) } }
                        msProcessId = applicationMapProperties.mapMSRemediationSchedule
                        remediationPaymentStatus = map.activeStatus
                        userTaskId = applicationMapProperties.mapMSUserTaskNameOFFICER
                        lastModifiedBy = "SCHEDULER"
                        lastModifiedOn = commonDaoServices.getTimestamp()
                    }
                    fuelInspection = fuelInspectionRepo.save(fuelInspection)

    }

    fun mapFuelInspectionListDto(fuelInspectionList: List<MsFuelInspectionEntity>?, batchDetails: FuelBatchDetailsDto): FuelInspectionScheduleListDetailsDto {
        val fuelInspectionScheduledList = mutableListOf<FuelInspectionDto>()
        fuelInspectionList?.map {fuelInspectionScheduledList.add(FuelInspectionDto(it.id, it.timelineStartDate,it.timelineEndDate,null,it.referenceNumber, it.company, it.companyKraPin, it.petroleumProduct, it.physicalLocation, it.inspectionDateFrom, it.inspectionDateTo,
            it.msProcessId?.let { it1 -> findProcessNameByID(it1, 1).processName }, it.assignedOfficerStatus==1, it.inspectionCompleteStatus==1))}


        return FuelInspectionScheduleListDetailsDto(
            fuelInspectionScheduledList,
            batchDetails
        )
    }


    fun mapRemarksListDto(remarksList: List<MsRemarksEntity>): List<MSRemarksDto> {

        return remarksList.map {
            MSRemarksDto(
                it.id,
                it.remarksDescription,
                it.msProcessId?.let { it1 -> findProcessNameByID(it1, 1).processBy },
                it.msProcessId?.let { it1 -> findProcessNameByID(it1, 1).processName },
            )
        }
    }

    fun mapFuelInspectionDto(
        fuelInspectionList: MsFuelInspectionEntity,
        batchDetails: FuelBatchDetailsDto,
        rapidTestDone: Boolean,
        compliantStatusDone: Boolean,
        timelineOverDue: Boolean,
        officerList: List<UsersEntity>?,
        remarksList: List<MsRemarksEntity>?,
        officersAssigned: UsersEntity?,
        rapidTestResults: FuelEntityRapidTestDto?,
        sampleCollected: SampleCollectionDto?,
        sampleSubmitted: SampleSubmissionDto?,
        sampleLabResults: MSSSFLabResultsDto?,
        fuelRemediationDto: FuelRemediationDto?
    ): FuelInspectionDto {
        return FuelInspectionDto(
            fuelInspectionList.id,
            fuelInspectionList.timelineStartDate,
            fuelInspectionList.timelineEndDate,
            timelineOverDue,
            fuelInspectionList.referenceNumber,
            fuelInspectionList.company,
            fuelInspectionList.companyKraPin,
            fuelInspectionList.petroleumProduct,
            fuelInspectionList.physicalLocation,
            fuelInspectionList.inspectionDateFrom,
            fuelInspectionList.inspectionDateTo,
            fuelInspectionList.msProcessId?.let { findProcessNameByID(it, 1).processName },
            fuelInspectionList.assignedOfficerStatus==1,
            fuelInspectionList.inspectionCompleteStatus==1,
            rapidTestDone,
            fuelInspectionList.sampleCollectionStatus==1,
            fuelInspectionList.sampleSubmittedStatus==1,
            fuelInspectionList.bsNumberStatus==1,
            compliantStatusDone,
            fuelInspectionList.remediationStatus==1,
            fuelInspectionList.remendiationCompleteStatus==1,
            fuelInspectionList.remediationPaymentStatus==1,
            batchDetails,
            officerList?.let { mapOfficerListDto(it) },
            remarksList?.let { mapRemarksListDto(it) },
            officersAssigned?.let { mapOfficerDto (it) },
            rapidTestResults,
            sampleCollected,
            sampleSubmitted,
            sampleLabResults,
            fuelRemediationDto
        )
    }

//    fun findAllFuelBatchList(): List<MsFuelBatchInspectionEntity> {
//        fuelBatchRepo.findAllOrderByIdAsc()
//            ?.let {
//                return it
//            }
//            ?: throw ExpectedDataNotFound("No Files found")
//    }

    fun findAllFuelBatchListBasedOnPageable(pageable: Pageable): Page<MsFuelBatchInspectionEntity> {
        fuelBatchRepo.findAll(pageable)
            .let {
                return it
            }
//            ?: throw ExpectedDataNotFound("No Files found")
    }

    fun findProcessNameByID(processID: Long, status: Int): MsProcessNamesEntity {
        processNameRepo.findByFuelStatusAndId(status,processID)
            ?.let {
                return it
            }
            ?: throw ExpectedDataNotFound("Fuel Process Details found with ID : $processID")
    }

    fun findAllFuelBatchListBasedOnPageableCountyAndRegion(countyId: Long, regionId: Long, batchClosed: Int,pageable: Pageable): Page<MsFuelBatchInspectionEntity> {
        fuelBatchRepo.findAllByCountyIdAndRegionIdAndBatchClosed(countyId,regionId,batchClosed,pageable)
            .let {
                return it
            }
//            ?: throw ExpectedDataNotFound("No Files found")
    }

    fun findAllFuelBatchListBasedOnPageableRegion(regionId: Long,batchClosed: Int, pageable: Pageable): Page<MsFuelBatchInspectionEntity> {
        fuelBatchRepo.findAllByRegionIdAndBatchClosed(regionId,batchClosed,pageable)
            .let {
                return it
            }
//            ?: throw ExpectedDataNotFound("No Files found")
    }

    fun findFuelBatchDetailByReferenceNumber(referenceNumber: String): MsFuelBatchInspectionEntity {
        fuelBatchRepo.findByReferenceNumber(referenceNumber)
            ?.let {
                return it
            }
            ?: throw ExpectedDataNotFound("No Fuel Batch File found with Ref No #$referenceNumber ")
    }

    fun findFuelBatchDetailByReferenceNumberAndRegionId(referenceNumber: String,regionId: Long): MsFuelBatchInspectionEntity {
        fuelBatchRepo.findByReferenceNumberAndRegionId(referenceNumber,regionId)
            ?.let {
                return it
            }
            ?: throw ExpectedDataNotFound("No Fuel Batch File found with Ref No #$referenceNumber ")
    }

    fun findFuelBatchDetailByReferenceNumberAndRegionIdAndCountyId(referenceNumber: String,regionId: Long, countyId: Long): MsFuelBatchInspectionEntity {
        fuelBatchRepo.findByReferenceNumberAndRegionIdAndCountyId(referenceNumber,regionId,countyId)
            ?.let {
                return it
            }
            ?: throw ExpectedDataNotFound("No Fuel Batch File found with Ref No #$referenceNumber ")
    }

    fun findSampleSubmittedListPdfBYSSFid(ssfID: Long): List<QaSampleSubmittedPdfListDetailsEntity>? {
        return sampleSubmissionSavedPdfListRepo.findBySffId(ssfID)
    }

    fun findFuelRemediationInvoice(fuelInspectionID: Long): MsFuelRemedyInvoicesEntity? {
        return fuelRemediationInvoiceRepo.findByFuelInspectionId(fuelInspectionID)
    }

    fun findFuelInvoicesWithInvoiceBatchIDMapped(invoiceBatchMappedID: Long): MsFuelRemedyInvoicesEntity {
        fuelRemediationInvoiceRepo.findByInvoiceBatchNumberId(invoiceBatchMappedID)
            ?.let { it ->
                return it
            }
            ?: throw ExpectedDataNotFound("Invoices With [invoice Batch Mapped ID = ${invoiceBatchMappedID}], does not Exist")
    }

    fun findSampleSubmittedBYFuelInspectionId(fuelInspectionId: Long): QaSampleSubmissionEntity? {
        return sampleSubmissionLabRepo.findByFuelInspectionId(fuelInspectionId)
    }
    fun findSampleSubmittedBYID(ssfID: Long): QaSampleSubmissionEntity {
        sampleSubmissionLabRepo.findByIdOrNull(ssfID)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No sample submission found with the following ID number=$ssfID")
    }

    fun findFuelBatchDetailByYearNameIDCountyRegion(yearNameId: Long, countyId: Long, regionId: Long): MsFuelBatchInspectionEntity? {
        return fuelBatchRepo.findByYearNameIdAndCountyIdAndRegionId(yearNameId,countyId, regionId)
    }

    fun findFuelBatchDetailByYearNameIDCountyRegionAndTown(yearNameId: Long,yearMonthId: Long, countyId: Long, townId: Long, regionId: Long): MsFuelBatchInspectionEntity? {
        return fuelBatchRepo.findByYearNameIdAndCountyIdAndTownIdAndRegionIdAndMonthNameId(yearNameId,countyId,townId, regionId,yearMonthId)
    }

    fun findFuelScheduledRemediationDetails(fuelInspectionId: Long): MsFuelRemediationEntity? {
        return fuelRemediationRepo.findByFuelInspectionId(fuelInspectionId)
    }

    fun findFuelInspectionOfficerAssigned(fuelInspection: MsFuelInspectionEntity, status: Int): MsFuelInspectionOfficersEntity? {
        return fuelInspectionOfficerRepo.findByMsFuelInspectionIdAndStatus(fuelInspection.id,status)
    }

    fun isWithinRange(checkDate: Date, workPlanYearCodes: WorkplanYearsCodesEntity): Boolean {
        return !(checkDate.before(workPlanYearCodes.workplanCreationStartDate) || checkDate.after(workPlanYearCodes.workplanCreationEndDate))
    }

    fun findWorkPlanYearsCode(currentYear: String, map: ServiceMapsEntity): WorkplanYearsCodesEntity? {
       return workPlanYearsCodesRepo.findByYearNameAndStatus(currentYear, map.activeStatus)
    }

    fun findWorkPlanMonthCode(currentMonth: String, map: ServiceMapsEntity): WorkplanMonthlyCodesEntity? {
       return workPlanMonthlyCodesRepo.findByMonthlyNameAndStatus(currentMonth, map.activeStatus)
    }

    fun findPlanYear(yearNameId: Long): WorkplanYearsCodesEntity {
        workPlanYearsCodesRepo.findByIdOrNull(yearNameId)
           ?.let {
               return it
           }
           ?: throw ExpectedDataNotFound("No Plan Year name found with ID $yearNameId")
    }

    fun findPlanMonth(monthNameId: Long): WorkplanMonthlyCodesEntity {
        workPlanMonthlyCodesRepo.findByIdOrNull(monthNameId)
            ?.let {
                return it
            }
            ?: throw ExpectedDataNotFound("No Plan Month name found with ID $monthNameId")
    }

    fun mapFuelBatchListDto(
        fuelBatchList: List<MsFuelBatchInspectionEntity>?,
        map: ServiceMapsEntity
    ): List<FuelBatchDetailsDto> {
        val fuelBatchListDto = mutableListOf<FuelBatchDetailsDto>()
        when {
            fuelBatchList!=null -> {
                return fuelBatchList.map {
                    FuelBatchDetailsDto(
                        it.id,
                        it.regionId?.let { it1 -> commonDaoServices.findRegionEntityByRegionID(it1, map.activeStatus).region },
                        it.countyId?.let { it1 ->
                            commonDaoServices.findCountiesEntityByCountyId(
                                it1,
                                map.activeStatus
                            ).county
                        },
                        it.townId?.let { it1 -> commonDaoServices.findTownEntityByTownId(it1).town },
                        it.referenceNumber,
                        it.yearNameId?.let { it1 -> findPlanYear(it1).yearName },
                        it.monthNameId?.let { it1 -> findPlanMonth(it1).description },
                        it.remarks,
                        it.batchClosed == 1
                    )
                }
            }
            else -> {
                return fuelBatchListDto
            }
        }
    }

    fun mapFuelBatchDetailsDto(
        fuelBatchList: MsFuelBatchInspectionEntity,
        map: ServiceMapsEntity
    ): FuelBatchDetailsDto{
        return FuelBatchDetailsDto(
                fuelBatchList.id,
                fuelBatchList.regionId?.let { it1 -> commonDaoServices.findRegionEntityByRegionID(it1, map.activeStatus).region },
                fuelBatchList.countyId?.let { it1 ->
                    commonDaoServices.findCountiesEntityByCountyId(
                        it1,
                        map.activeStatus
                    ).county
                },
                fuelBatchList.townId?.let { it1 -> commonDaoServices.findTownEntityByTownId(it1).town },
                fuelBatchList.referenceNumber,
                fuelBatchList.yearNameId?.let { it1 -> findPlanYear(it1).yearName },
                fuelBatchList.monthNameId?.let { it1 -> findPlanMonth(it1).description },
                fuelBatchList.remarks,
                fuelBatchList.batchClosed == 1
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

    fun mapLaboratoryListDto(lab: List<CdLaboratoryEntity>): List<LaboratoryDto> {
        return lab.map {
            LaboratoryDto(
                it.id,
                it.labName,
                it.description,
                it.status == 1
            )
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

    fun mapSSFComplianceStatusDetailsDto(ssf: QaSampleSubmissionEntity): MSSSFComplianceStatusDetailsDto {
        return MSSSFComplianceStatusDetailsDto(
            ssf.id,
            ssf.bsNumber,
            ssf.complianceRemarks,
            ssf.resultsAnalysis == 1
        )

    }

    fun mapSampleCollectedParamListDto(data: List<MsCollectionParametersEntity>): List<SampleCollectionItemsDto> {
        return data.map {
            SampleCollectionItemsDto(
                it.id,
                it.productBrandName,
                it.batchNo,
                it.batchSize,
                it.sampleSize
            )
        }
    }

    fun findUploadedFileBYId(fileID: Long): MsUploadsEntity {
        msUploadRepo.findByIdOrNull(fileID)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No File found with the following [ id=$fileID]")
    }

    fun mapSampleSubmissionParamListDto(data: List<MsLaboratoryParametersEntity>): List<SampleSubmissionItemsDto> {
        return data.map {
            SampleSubmissionItemsDto(
                it.parameters,
                it.laboratoryName
            )
        }
    }



    fun mapFuelRemediationDetails(data: List<MsFuelRemedyInvoicesEntity>): List<FuelRemediationDetailsDTO> {
        return data.map {
            FuelRemediationDetailsDTO(
                it.id.toString(),
                        it.sageInvoiceNumber.toString(),
                        it.receiptNo.toString(),
                        it.invoiceBatchNumberId.toString(),
                        it.paymentStatus.toString(),
                        it.amount.toString(),
                        it.invoiceDate.toString(),
                        it.paymentDate.toString(),
                        it.transactionDate.toString(),
                        it.status.toString(),
                        it.remarks.toString(),
                        it.varField1.toString(),
                        it.varField2.toString(),
                        it.varField3.toString(),
                        it.varField4.toString(),
                        it.varField5.toString(),
                        it.varField6.toString(),
                        it.varField7.toString(),
                        it.varField8.toString(),
                        it.varField9.toString(),
                        it.varField10.toString(),
                        it.createdBy.toString(),
                        it.createdOn.toString(),
                        it.lastModifiedBy.toString(),
                        it.lastModifiedOn.toString(),
                        it.updateBy.toString(),
                        it.updatedOn.toString(),
                        it.deleteBy.toString(),
                        it.deletedOn.toString(),
                        it.version.toString(),
                        it.remunerationRateLiter.toString(),
                        it.remunerationSubTotal.toString(),
                        it.remunerationVat.toString(),
                        it.remunerationTotal.toString(),
                        it.volumeFuelRemediated.toString(),
                        it.subsistenceTotalNights.toString(),
                        it.subsistenceRate.toString(),
                        it.subsistenceRateNightTotal.toString(),
                        it.subsistenceVat.toString(),
                        it.subsistenceTotal.toString(),
                        it.transportAirTicket.toString(),
                        it.transportInkm.toString(),
                        it.transportRate.toString(),
                        it.transportTotalKmrate.toString(),
                        it.transportVat.toString(),
                        it.transportTotal.toString(),
                        it.transportGrandTotal.toString(),
                        it.fuelInspectionId.toString(),
                        it.fuelInspectionRefNumber.toString(),
            )
        }
    }

    fun mapLabResultsParamListDto(data: List<QaSampleLabTestResultsEntity>): List<LabResultsParamDto> {
        return data.map {
            LabResultsParamDto(
                it.param,
                it.result,
                it.method
            )
        }
    }

    fun fuelGrandTotal(totalValue1: BigDecimal, totalValue2: BigDecimal, totalValue3: BigDecimal): BigDecimal {
        return (totalValue1.plus(totalValue2).plus(totalValue3))
    }

    fun fuelGrandTaxTotal(totalValue1: BigDecimal, totalValue2: BigDecimal, totalValue3: BigDecimal): BigDecimal {
        return (totalValue1.plus(totalValue2).plus(totalValue3))
    }

    fun fuelTransportAirTicketsRateVatCalculation(airTicket: Long?, km: Long?): Array<Long?> {
        fuelRemediationInvoiceChargesRepo.findByIdOrNull(transportAirTicketsChargesId)
            ?.let { chargesRateVat ->
                val rateTransport = chargesRateVat.rate
                val subTotal = airTicket?.let { (km?.let { rateTransport?.times(it) })?.plus(it) }
//                    val subTotal = (airTicketAndKm(airTicket, km)?.let { rateTransport?.times(it) })
                val vatValue = (chargesRateVat.vatPercentage?.let { subTotal?.times(it) })?.div(percentage)
                val totalValue3 = vatValue?.let { subTotal?.plus(it) }

                return arrayOf(rateTransport, subTotal, vatValue, totalValue3)
            }
            ?: throw ExpectedDataNotFound("Fetched Fuel Remediation Charges Entity with [id=${remunerationChargesId}] does not exist")

    }

    fun fuelSubsistenceRateVatCalculation(totalNight: Long): Array<Long?> {
        fuelRemediationInvoiceChargesRepo.findByIdOrNull(subsistenceChargesId)
            ?.let { chargesRateVat ->
                val rateNight = chargesRateVat.rate
                val subTotal = (rateNight?.times(totalNight))
                val vatValue = (chargesRateVat.vatPercentage?.let { subTotal?.times(it) })?.div(percentage)
                val totalValue2 = vatValue?.let { subTotal?.plus(it) }

                return arrayOf(rateNight, subTotal, vatValue, totalValue2)
            }
            ?: throw ExpectedDataNotFound("Fetched Fuel Remediation Charges Entity with [id=${remunerationChargesId}] does not exist")

    }

    fun fuelRemunerationRateVatCalculation(volume: Long): Array<Long?> {
        fuelRemediationInvoiceChargesRepo.findByIdOrNull(remunerationChargesId)
            ?.let { chargesRateVat ->
                val rateLitre = chargesRateVat.rate
                val subTotal = (rateLitre?.times(volume))
                val vatValue = (chargesRateVat.vatPercentage?.let { subTotal?.times(it) })?.div(percentage)
                val totalValue1 = vatValue?.let { subTotal?.plus(it) }

                return arrayOf(rateLitre, subTotal, vatValue, totalValue1)
            }
            ?: throw ExpectedDataNotFound("Fetched Fuel Remediation Charges Entity with [id=${remunerationChargesId}] does not exist")

    }

    fun mapLIMSSavedFilesDto(bsNumber: String, savedPDFFiles:List<MSSSFPDFListDetailsDto>? ): List<LIMSFilesFoundDto>? {
        val result = mutableListOf<LIMSFilesFoundDto>()
        limsServices.checkPDFFiles(bsNumber)
            ?.forEach { fpdf ->
                if (savedPDFFiles?.isNotEmpty() == true) {
                    savedPDFFiles.firstOrNull { it.pdfName == fpdf }
                        ?.let {
                            val limsDto = LIMSFilesFoundDto(
                                true,
                                fpdf
                            )
                            result.add(limsDto)
                        }
                        ?: run {
                            val limsDto = LIMSFilesFoundDto(
                                false,
                                fpdf
                            )
                            result.add(limsDto)
                        }
                } else {
                    val limsDto = LIMSFilesFoundDto(
                        false,
                        fpdf
                    )
                    result.add(limsDto)
                }
            }
        return  result.distinct()
    }

    fun mapLabPDFFilesListDto(data: List<QaSampleSubmittedPdfListDetailsEntity>): List<MSSSFPDFListDetailsDto> {
        return data.map { ssfPdfRemarks ->
            MSSSFPDFListDetailsDto(
                ssfPdfRemarks.msPdfSavedId,
                ssfPdfRemarks.pdfName,
                ssfPdfRemarks.sffId,
                ssfPdfRemarks.complianceRemarks,
                ssfPdfRemarks.complianceStatus == 1,
            )

        }
    }

    fun mapLabResultsDto(data: List<LabResultsParamDto>): LabResultsDto {
        return LabResultsDto(
            data,
            )

    }

    fun mapLabResultsDetailsDto(
        ssfResultsList: MSSSFComplianceStatusDetailsDto?,
        savedPDFFiles:  List<MSSSFPDFListDetailsDto>?,
        limsPDFFiles: List<LIMSFilesFoundDto>?,
        parametersListTested: List<LabResultsParamDto>?
    ): MSSSFLabResultsDto {
        return MSSSFLabResultsDto(
            ssfResultsList,
            savedPDFFiles,
            limsPDFFiles,
            parametersListTested,
            )

    }

    fun mapSampleCollectedDto(data: MsSampleCollectionEntity, data2:List<SampleCollectionItemsDto>): SampleCollectionDto {
        return SampleCollectionDto(
            data.id,
            data.nameManufacturerTrader,
            data.addressManufacturerTrader,
            data.samplingMethod,
            data.reasonsCollectingSamples,
            data.anyRemarks,
            data.designationOfficerCollectingSample,
            data.nameOfficerCollectingSample,
            data.dateOfficerCollectingSample,
            data.nameWitness,
            data.designationWitness,
            data.dateWitness,
            data2
        )
    }

    fun mapFuelRemediationDto(data: MsFuelRemediationEntity,invoiceCreated: Boolean): FuelRemediationDto {
        return FuelRemediationDto(
            data.productType,
            data.applicableKenyaStandard,
            data.remediationProcedure,
            data.volumeOfProductContaminated,
            data.contaminatedFuelType,
            data.quantityOfFuel,
            data.volumeAdded,
            data.totalVolume,
            data.proFormaInvoiceStatus == 1,
            data.proFormaInvoiceNo,
            data.invoiceAmount,
            data.feePaidReceiptNo,
            data.dateOfRemediation,
            data.dateOfPayment,
            invoiceCreated
        )
    }

     fun mapSampleSubmissionDto(data: MsSampleSubmissionEntity, data2:List<SampleSubmissionItemsDto>): SampleSubmissionDto {
        return SampleSubmissionDto(
            data.id,
            data.nameProduct,
            data.packaging,
            data.labellingIdentification,
            data.fileRefNumber,
            data.referencesStandards,
            data.sizeTestSample,
            data.sizeRefSample,
            data.condition,
            data.sampleReferences,
            data.sendersName,
            data.designation,
            data.address,
            data.sendersDate,
            data.receiversName,
            data.testChargesKsh,
            data.receiptLpoNumber,
            data.invoiceNumber,
            data.disposal,
            data.remarks,
            data.sampleCollectionNumber,
            data.bsNumber,
            data2
        )
    }

    fun mapRapidTestDto(rapidTest: MsFuelInspectionEntity, map: ServiceMapsEntity): FuelEntityRapidTestDto? {

        return when {
            rapidTest.rapidTestPassed==map.activeStatus -> {
                 FuelEntityRapidTestDto(
                    rapidTest.rapidTestPassedRemarks,
                    rapidTest.rapidTestPassed==1
                )
            }
            rapidTest.rapidTestFailed==map.inactiveStatus -> {
                 FuelEntityRapidTestDto(
                    rapidTest.rapidTestFailedRemarks,
                    rapidTest.rapidTestFailed==1
                )
            }
            else -> null
        }

    }

    fun mapCompliantStatusDto(compliantDetails: MsFuelInspectionEntity, map: ServiceMapsEntity): SSFCompliantStatusDto? {

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

    fun findRemarksForFuel(fuelInspectionID: Long): List<MsRemarksEntity>? {
        return remarksRepo.findAllByFuelInspectionIdOrderByIdAsc(fuelInspectionID)
    }
}




