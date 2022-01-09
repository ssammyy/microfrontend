package org.kebs.app.kotlin.apollo.api.ports.provided.dao

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.ports.provided.lims.LimsServices
import org.kebs.app.kotlin.apollo.common.dto.ms.*
import org.kebs.app.kotlin.apollo.common.dto.qa.LimsFilesFoundDto
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.utils.generateRandomText
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.ms.*
import org.kebs.app.kotlin.apollo.store.model.qa.QaSampleLabTestResultsEntity
import org.kebs.app.kotlin.apollo.store.model.qa.QaSampleSubmissionEntity
import org.kebs.app.kotlin.apollo.store.model.qa.QaSampleSubmittedPdfListDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.qa.QaUploadsEntity
import org.kebs.app.kotlin.apollo.store.repo.IServiceRequestsRepository
import org.kebs.app.kotlin.apollo.store.repo.ms.*
import org.kebs.app.kotlin.apollo.store.repo.qa.IQaSampleLabTestResultsRepository
import org.kebs.app.kotlin.apollo.store.repo.qa.IQaSampleSubmissionRepository
import org.kebs.app.kotlin.apollo.store.repo.qa.IQaSampleSubmittedPdfListRepository
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
import kotlin.math.log


@Service
class NewMarketSurveillanceDaoServices(
    private val applicationMapProperties: ApplicationMapProperties,
    private val fuelBatchRepo: IFuelBatchRepository,
    private val limsServices: LimsServices,
    private val fuelRemediationInvoiceChargesRepo: IFuelRemediationChargesRepository,
    private val sampleSubmissionSavedPdfListRepo: IQaSampleSubmittedPdfListRepository,
    private val workPlanYearsCodesRepo: IWorkplanYearsCodesRepository,
    private val sampleCollectRepo: ISampleCollectionRepository,
    private val sampleLabTestResultsRepo: IQaSampleLabTestResultsRepository,
    private val fuelInspectionOfficerRepo: IFuelInspectionOfficerRepository,
    private val sampleCollectParameterRepo: ISampleCollectParameterRepository,
    private val sampleSubmitParameterRepo: ISampleSubmitParameterRepository,
    private val sampleSubmissionLabRepo: IQaSampleSubmissionRepository,
    private val msUploadRepo: IOnsiteUploadRepository,
    private val fuelRemediationRepo: IFuelRemediationRepository,
    private val sampleSubmitRepo: IMSSampleSubmissionRepository,
    private val fuelInspectionRepo: IFuelInspectionRepository,
    private val fuelRemediationInvoiceRepo: IFuelRemediationInvoiceRepository,
    private val invoiceDaoService: InvoiceDaoService,
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

    @PreAuthorize("hasAuthority('EPRA')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun createNewFuelBatch(body: BatchFileFuelSaveDto,page: PageRequest): FuelInspectionScheduleListDetailsDto {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val currentYear = commonDaoServices.getCurrentYear()
        val fuelPlanYearCode = findWorkPlanYearsCode(currentYear, map)?: throw ExpectedDataNotFound("Fuel Schedule Current Year Code Not Found")
        val fileSaved = fuelCreateBatch(body,fuelPlanYearCode, map, loggedInUser)

        if (fileSaved.first.status == map.successStatus) {
            val fileInspectionList = findAllFuelInspectionListBasedOnBatchIDPageRequest(fileSaved.second.id,page)
            return mapFuelInspectionListDto(fileInspectionList,mapFuelBatchDetailsDto(fileSaved.second, map))
        } else {
            throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved.first))
        }
    }

    @PreAuthorize("hasAuthority('EPRA')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun closeFuelBatchCreated(referenceNumber: String,page: PageRequest): Page<FuelBatchDetailsDto> {
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
                    userTaskId = applicationMapProperties.mapMSUserTaskNameMANAGERPETROLEUM
                }
                updateFuelInspectionDetails(it, map, loggedInUser)
            }
            /**
             * TODO: Lets discuss to understand better what how to assign HOD to a complaint is it based on Region or Randomly
             */

            val fuelBatchList = findAllFuelBatchListBasedOnPageable(page)
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

        if (fileSaved.first.status == map.successStatus) {
            val fileInspectionList = findAllFuelInspectionListBasedOnBatchIDPageRequest(fileSaved.second.id,page)
            return mapFuelInspectionListDto(fileInspectionList,mapFuelBatchDetailsDto(batchDetail, map))
        } else {
            throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved.first))
        }
    }

    @PreAuthorize("hasAuthority('EPRA')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun getAllFuelBatchList(page: PageRequest): Page<FuelBatchDetailsDto> {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        val fuelBatchList = findAllFuelBatchListBasedOnPageable(page)
        return mapFuelBatchListDto(fuelBatchList, map)
    }

    @PreAuthorize("hasAuthority('EPRA')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun getAllFuelInspectionListBasedOnBatchRefNo(batchReferenceNo: String,page: PageRequest): FuelInspectionScheduleListDetailsDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        val findBatchDetail = findFuelBatchDetailByReferenceNumber(batchReferenceNo)
        val fileInspectionList = findAllFuelInspectionListBasedOnBatchIDPageRequest(findBatchDetail.id,page)
        return mapFuelInspectionListDto(fileInspectionList,mapFuelBatchDetailsDto(findBatchDetail, map))
    }

    //    @PreAuthorize("hasAuthority('EPRA')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun getFuelInspectionDetailsBasedOnRefNo(referenceNo: String, batchReferenceNo: String): FuelInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        val fileInspectionDetail = findFuelInspectionDetailByReferenceNumber(referenceNo)
        val batchDetails = findFuelBatchDetailByReferenceNumber(batchReferenceNo)
        val fuelInspectionOfficer = findFuelInspectionOfficerAssigned(fileInspectionDetail, map.activeStatus)
        val officerList = commonDaoServices.findOfficersListBasedOnRegionCountyAndRole(
            applicationMapProperties.mapMSMappedOfficerROLEID,
            batchDetails.countyId ?: throw ExpectedDataNotFound("MISSING BATCH COUNTY ID"),
            batchDetails.regionId ?: throw ExpectedDataNotFound("MISSING BATCH REGION ID")
        )
//        val rapidTestStatus = mapRapidTestDto(fileInspectionDetail, map)
        return fuelInspectionMappingCommonDetails(fileInspectionDetail, map, batchDetails)
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun endFuelInspectionDetailsBasedOnRefNo(referenceNo: String, batchReferenceNo: String): FuelInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var fileInspectionDetail = findFuelInspectionDetailByReferenceNumber(referenceNo)
        val batchDetails = findFuelBatchDetailByReferenceNumber(batchReferenceNo)
        with(fileInspectionDetail){
            inspectionCompleteStatus = map.activeStatus
            userTaskId = null
        }
        fileInspectionDetail = updateFuelInspectionDetails(fileInspectionDetail, map, loggedInUser).second
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

        when (fileOfficerSaved.first.status) {
            map.successStatus -> {
                with(fileInspectionDetail) {
                    userTaskId = applicationMapProperties.mapMSUserTaskNameOFFICER
                    assignedOfficerStatus = map.activeStatus
                }
                val fileSaved2 = updateFuelInspectionDetails(fileInspectionDetail, map, loggedInUser)
                when (fileSaved2.first.status) {
                    map.successStatus -> {
                        fileInspectionDetail = fileSaved2.second
                        /*****
                         * Todo add notification for sending details to user
                         *
                         */
                        val officerList = commonDaoServices.findOfficersListBasedOnRegionCountyAndRole(applicationMapProperties.mapMSMappedOfficerROLEID, batchDetails.countyId ?: throw ExpectedDataNotFound("MISSING BATCH COUNTY ID"), batchDetails.regionId ?: throw ExpectedDataNotFound("MISSING BATCH REGION ID"))
                        val rapidTestStatus = mapRapidTestDto(fileInspectionDetail, map)
                        return fuelInspectionMappingCommonDetails(fileInspectionDetail, map, batchDetails)
                    }
                    else -> {
                        throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved2.first))
                    }
                }
            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileOfficerSaved.first))
            }
        }
    }

    @PreAuthorize("hasAuthority('MS_MP_MODIFY')")
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

        when {
            body.rapidTestStatus -> {
                //Rapid Test Passed
                with(fileInspectionDetail){
                    rapidTestPassed = map.activeStatus
                    rapidTestPassedOn = commonDaoServices.getCurrentDate()
                    rapidTestPassedBy = commonDaoServices.concatenateName(loggedInUser)
                    rapidTestPassedRemarks = body.rapidTestRemarks
                }

            }
            else -> {
                //Rapid Test Failed
                with(fileInspectionDetail){
                    rapidTestFailed = map.activeStatus
                    rapidTestFailedOn = commonDaoServices.getCurrentDate()
                    rapidTestFailedBy = commonDaoServices.concatenateName(loggedInUser)
                    rapidTestFailedRemarks = body.rapidTestRemarks
                }
            }
        }
        val fileSaved = updateFuelInspectionDetails(fileInspectionDetail, map, loggedInUser)


        when (fileSaved.first.status) {
                    map.successStatus -> {
                        fileInspectionDetail = fileSaved.second
                        val fuelInspectionOfficer = findFuelInspectionOfficerAssigned(fileInspectionDetail, map.activeStatus)
                        val officerList = commonDaoServices.findOfficersListBasedOnRegionCountyAndRole(applicationMapProperties.mapMSMappedOfficerROLEID, batchDetails.countyId ?: throw ExpectedDataNotFound("MISSING BATCH COUNTY ID"), batchDetails.regionId ?: throw ExpectedDataNotFound("MISSING BATCH REGION ID"))
                        val rapidTestStatus = mapRapidTestDto(fileInspectionDetail, map)
                        return fuelInspectionMappingCommonDetails(fileInspectionDetail, map, batchDetails)
                    }
                    else -> {
                        throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved.first))
                    }
                }


    }

    @PreAuthorize("hasAuthority('MS_MP_MODIFY')")
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
        val savedSampleCollection = fuelSampleCollectAdd(body,fileInspectionDetail, map, loggedInUser)

        when (savedSampleCollection.first.status) {
                    map.successStatus -> {
                        body.productsList?.forEach { param->
                            fuelSampleCollectParamAdd(param,savedSampleCollection.second,map,loggedInUser)
                        }
                        with(fileInspectionDetail){
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

    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
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
        val savedSampleSubmission = fuelSampleSubmissionAdd(body,fileInspectionDetail,sampleCollected?: throw ExpectedDataNotFound("MISSING SAMPLE COLLECTED FOR FUEL INSPECTION REF NO $referenceNo"), map, loggedInUser)

        when (savedSampleSubmission.first.status) {
                    map.successStatus -> {
                        body.parametersList?.forEach { param->
                            fuelSampleSubmissionParamAdd(param,savedSampleSubmission.second,map,loggedInUser)
                        }
                        with(fileInspectionDetail){
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

    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
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
        val updatedSampleSubmission = fuelSampleSubmissionUpDate(sampleSubmission,map, loggedInUser)

        when (updatedSampleSubmission.first.status) {
            map.successStatus -> {
                val savedBsNumber = ssfSaveBSNumber(updatedSampleSubmission.second,fileInspectionDetail,loggedInUser, map)
                when (savedBsNumber.first.status) {
                    map.successStatus -> {
                        with(fileInspectionDetail){
                            userTaskId = applicationMapProperties.mapMSUserTaskNameLAB
                            bsNumberStatus = 1
                        }
                        fileInspectionDetail = updateFuelInspectionDetails(fileInspectionDetail, map, loggedInUser).second
                        return fuelInspectionMappingCommonDetails(fileInspectionDetail, map, batchDetails)
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
        val savedPDFLabResultFile = fuelInspectionSaveLIMSPDFSelected(fileContent,body.ssfID,map,loggedInUser)

        if (savedPDFLabResultFile.first.status == map.successStatus) {
//            with(fileInspectionDetail){
//                userTaskId = applicationMapProperties.mapMSUserTaskNameLAB
//            }
//            val fileSaved = updateFuelInspectionDetails(fileInspectionDetail, map, loggedInUser)
            return fuelInspectionMappingCommonDetails(fileInspectionDetail, map, batchDetails)
        }
        else {
            throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(savedPDFLabResultFile.first))
        }
    }

    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
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

        if (savedSSfComplianceStatus.first.status == map.successStatus) {
            with(fileInspectionDetail){
                if (body.complianceStatus) {
                    compliantStatus = 1
                    notCompliantStatus =  0
                    remediationStatus =1
                    remediationPaymentStatus = 1
                    compliantStatusDate = commonDaoServices.getCurrentDate()
                    compliantStatusBy = commonDaoServices.concatenateName(loggedInUser)
                    compliantStatusRemarks = body.complianceRemarks
                }
                else {
                    notCompliantStatus =  1
                    compliantStatus = 0
                    notCompliantStatusDate = commonDaoServices.getCurrentDate()
                    notCompliantStatusBy = commonDaoServices.concatenateName(loggedInUser)
                    notCompliantStatusRemarks = body.complianceRemarks
                    remediationStatus = 1
                    remediationPaymentStatus = 0
                }
            }
            fileInspectionDetail = updateFuelInspectionDetails(fileInspectionDetail, map, loggedInUser).second
            /*
            * Todo: ADD function for sending Lab results
            * */
            return fuelInspectionMappingCommonDetails(fileInspectionDetail, map, batchDetails)
        }
        else {
            throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(savedSSfComplianceStatus.first))
        }
    }

    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
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
                with(fileInspectionDetail){
                    remediationPaymentStatus = map.inactiveStatus
                    userTaskId = applicationMapProperties.mapMSUserTaskNameSTATIONOWNER
                }
                fileInspectionDetail= updateFuelInspectionDetails(fileInspectionDetail, map, loggedInUser).second
                return fuelInspectionMappingCommonDetails(fileInspectionDetail, map, batchDetails)
            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(savedRemediationInvoice.first))
            }
        }
    }


    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
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
                with(fileInspectionDetail){
                    remediationStatus = map.activeStatus
                }
                fileInspectionDetail= updateFuelInspectionDetails(fileInspectionDetail, map, loggedInUser).second
                return fuelInspectionMappingCommonDetails(fileInspectionDetail, map, batchDetails)
            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(savedRemediation.first))
            }
        }

    }

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
        }
        val updatedRemediation = updateFuelRemediationDetails(fuelRemediation, loggedInUser,map)

        when (updatedRemediation.first.status) {
            map.successStatus -> {
                with(fileInspectionDetail){
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

    fun fuelInspectionMappingCommonDetails(
        fileInspectionDetail: MsFuelInspectionEntity,
        map: ServiceMapsEntity,
        batchDetails: MsFuelBatchInspectionEntity
    ): FuelInspectionDto {
        val batchDetailsDto = mapFuelBatchDetailsDto(batchDetails, map)
        val fuelInspectionOfficer = findFuelInspectionOfficerAssigned(fileInspectionDetail, map.activeStatus)
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

        val compliantDetailsStatus = mapCompliantStatusDto(fileInspectionDetail, map)
        var compliantStatusDone = false
        if (compliantDetailsStatus!=null){
            compliantStatusDone = true
        }
        val sampleCollected = findSampleCollectedDetailByFuelInspectionID(fileInspectionDetail.id)
        val sampleCollectedParamList = sampleCollected?.id?.let { findAllSampleCollectedParametersBasedOnSampleCollectedID(it) }
        val sampleCollectedDtoValues = sampleCollectedParamList?.let { mapSampleCollectedParamListDto(it) }?.let { mapSampleCollectedDto(sampleCollected, it) }
        val sampleSubmitted = findSampleSubmissionDetailBySampleCollectedID(fileInspectionDetail.id)
        val sampleSubmittedParamList = sampleSubmitted?.id?.let { findAllSampleSubmissionParametersBasedOnSampleSubmissionID(it) }
        val sampleSubmittedDtoValues = sampleSubmittedParamList?.let { mapSampleSubmissionParamListDto(it) }?.let { mapSampleSubmissionDto(sampleSubmitted, it) }
        val labResultsParameters = sampleSubmitted?.bsNumber?.let { findSampleLabTestResultsRepoBYBSNumber(it) }
        val ssfDetailsLab = findSampleSubmittedBYFuelInspectionId(fileInspectionDetail.id)
        val savedPDFFilesLims = ssfDetailsLab?.id?.let { findSampleSubmittedListPdfBYSSFid(it)?.let { mapLabPDFFilesListDto(it) } }
        val ssfResultsListCompliance = ssfDetailsLab?.let { mapSSFComplianceStatusDetailsDto(it) }
        val limsPDFFiles = ssfDetailsLab?.bsNumber?.let { mapLIMSSavedFilesDto(it,savedPDFFilesLims)}
        val labResultsDto = mapLabResultsDetailsDto(ssfResultsListCompliance,savedPDFFilesLims,limsPDFFiles,labResultsParameters?.let { mapLabResultsParamListDto(it) })
        val fuelRemediationDto = findFuelScheduledRemediationDetails(fileInspectionDetail.id)?.let { mapFuelRemediationDto(it) }
        return mapFuelInspectionDto(
            fileInspectionDetail,
            batchDetailsDto,
            rapidTestDone,
            compliantStatusDone,
            officerList,
            fuelInspectionOfficer?.assignedIo,
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
        map: ServiceMapsEntity,
        user: UsersEntity
    ): Pair<ServiceRequestsEntity, MsFuelBatchInspectionEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var fuel = MsFuelBatchInspectionEntity()
        try {
            val county = commonDaoServices.findCountiesEntityByCountyId(body.county, map.activeStatus)
            val regionIDValue = county.regionId ?: ExpectedDataNotFound("The following County ${county.county}, with ID  = ${county.id} and status = ${county.status}, does not have a region mapped to")
            val userFuelScheduleBatch = findFuelBatchDetailByYearNameIDCountyRegion(fuelPlanYearName.id, body.county,commonDaoServices.findRegionEntityByRegionID(regionIDValue as Long, map.activeStatus).id)
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
                        remarks = body.remarks
                        transactionDate = commonDaoServices.getCurrentDate()
                        status = map.initStatus
                        batchClosed = map.inactiveStatus
                        userTaskId = applicationMapProperties.mapMSUserTaskNameEPRA
                        createdBy = commonDaoServices.concatenateName(user)
                        createdOn = commonDaoServices.getTimestamp()
                    }
                    fuel = fuelBatchRepo.save(fuel)

                    sr.payload = "${commonDaoServices.createJsonBodyFromEntity(fuel)}"
                    sr.names = "Fuel creation of batch file"

                    sr.responseStatus = sr.serviceMapsId?.successStatusCode
                    sr.responseMessage = "Success ${sr.payload}"
                    sr.status = map.successStatus
                    sr = serviceRequestsRepo.save(sr)
                    sr.processingEndDate = Timestamp.from(Instant.now())
                }
                else -> {
                    throw ExpectedDataNotFound("You already have a fuel schedule batch plan for this year")
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
            fuelInspectionOfficerRepo.findByMsFuelInspectionIdAndStatus(fileInspectionDetail,map.activeStatus)
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
                assignedIo = commonDaoServices.findUserByID(body.assignedUserID)
                remarks = body.remarks
                transactionDate = commonDaoServices.getCurrentDate()
                status = map.activeStatus
                createdBy = commonDaoServices.concatenateName(user)
                createdOn = commonDaoServices.getTimestamp()
                msFuelInspectionId = fileInspectionDetail
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

    fun fuelSampleCollectAdd(
        body: SampleCollectionDto,
        fileInspectionDetail: MsFuelInspectionEntity,
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
                msFuelInspectionId = fileInspectionDetail.id
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

    fun fuelSampleCollectParamAdd(
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

    fun fuelSampleSubmissionAdd(
        body: SampleSubmissionDto,
        fileInspectionDetail: MsFuelInspectionEntity,
        sampleCollected: MsSampleCollectionEntity,
        map: ServiceMapsEntity,
        user: UsersEntity
    ): Pair<ServiceRequestsEntity, MsSampleSubmissionEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var fuelSampleSubmission = MsSampleSubmissionEntity()
        try {
            with(fuelSampleSubmission) {
                nameProduct = body.nameProduct
                packaging = body.packaging
                labellingIdentification = body.labellingIdentification
                fileRefNumber = "REF/SSF/MSFUEL/${generateRandomText(5, map.secureRandom, map.messageDigestAlgorithm, true)}".toUpperCase()
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
                sampleCollectionNumber = sampleCollected.id
                msFuelInspectionId = fileInspectionDetail.id
                status = map.activeStatus
                createdBy = commonDaoServices.concatenateName(user)
                createdOn = commonDaoServices.getTimestamp()
            }
            fuelSampleSubmission = sampleSubmitRepo.save(fuelSampleSubmission)

            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(fuelSampleSubmission)} "
            sr.names = "Fuel Inspection Sample Submission Save file"

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
        return Pair(sr, fuelSampleSubmission)
    }

    fun fuelSampleSubmissionParamAdd(
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
        fileInspectionDetail: MsFuelInspectionEntity,
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
                        fuelInspectionId = fileInspectionDetail.id
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


    fun fuelSampleSubmissionUpDate(
        sampleSubmission: MsSampleSubmissionEntity,
        map: ServiceMapsEntity,
        user: UsersEntity
    ): Pair<ServiceRequestsEntity, MsSampleSubmissionEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var fuelSampleSubmission = sampleSubmission
        try {
            with(fuelSampleSubmission) {
                modifiedBy = commonDaoServices.concatenateName(user)
                modifiedOn = commonDaoServices.getTimestamp()
            }
            fuelSampleSubmission = sampleSubmitRepo.save(fuelSampleSubmission)

            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(fuelSampleSubmission)} "
            sr.names = "Fuel Inspection Sample Submission Update file"

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
        return Pair(sr, fuelSampleSubmission)
    }

    fun fuelInspectionSaveLIMSPDFSelected(
        fileContent: File,
        ssfID: Long,
        map: ServiceMapsEntity,
        user: UsersEntity
    ): Pair<ServiceRequestsEntity, QaSampleSubmissionEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        val ssfDetails = findSampleSubmittedBYID(ssfID)
        try {

            var upload = MsOnSiteUploadsEntity()
            with(upload) {
                msFuelInspectionId = ssfDetails.fuelInspectionId
                ssfUploads = 1
                ordinaryStatus = 0
                versionNumber = 1
            }
            upload = uploadMSFile(upload, commonDaoServices.convertFileToMultipartFile(fileContent), "LAB RESULTS PDF", user, map).second

            val ssfPdfDetails = QaSampleSubmittedPdfListDetailsEntity()
            with(ssfPdfDetails) {
                sffId = ssfDetails.id
                pdfName = fileContent.name
                pdfSavedId = upload.id
                createdBy = commonDaoServices.concatenateName(user)
                createdOn = commonDaoServices.getTimestamp()
            }
            sampleSubmissionSavedPdfListRepo.save(ssfPdfDetails)

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
        body: MsOnSiteUploadsEntity,
        docFile: MultipartFile,
        doc: String,
        user: UsersEntity,
        map: ServiceMapsEntity
    ): Pair<ServiceRequestsEntity, MsOnSiteUploadsEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var onsiteUploads = body
        try {

            with(onsiteUploads) {
                name = commonDaoServices.saveDocuments(docFile)
                fileType = commonDaoServices.getFileTypeByMimetypesFileTypeMap(docFile.name)
                documentType = doc
                document = docFile.bytes
                transactionDate = commonDaoServices.getCurrentDate()
                status = map.activeStatus
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
                remunerationVat = remuneration?.get(vatTotal)
                remunerationTotal = remuneration?.get(totalValue)?.toBigDecimal()

                //Part of Subsistence calculation
                subsistenceTotalNights = fuelRemediationInvoiceEntity.subsistenceTotalNights
                subsistenceRate = subsistence?.get(rateValue)
                subsistenceRateNightTotal = subsistence?.get(subTotal)
                subsistenceVat = subsistence?.get(vatTotal)
                subsistenceTotal = subsistence?.get(totalValue)?.toBigDecimal()

                //Part of Subsistence calculation
                transportAirTicket = fuelRemediationInvoiceEntity.transportAirTicket
                transportInkm = fuelRemediationInvoiceEntity.transportInkm
                transportRate = transportAirTickets[rateValue]
                transportTotalKmrate = transportAirTickets[subTotal]
                transportVat = transportAirTickets[vatTotal]
                transportTotal = transportAirTickets[totalValue]?.toBigDecimal()

                //All calculated Grand total Value
                transportGrandTotal = remunerationTotal?.let { subsistenceTotal?.let { it1 -> transportTotal?.let { it2 -> fuelGrandTotal(it, it1, it2) } } }

                fuelInspectionId = fuelInspection.id
                transactionDate =commonDaoServices.getCurrentDate()
                invoiceDate = commonDaoServices.getCurrentDate()
                status = activeStatus
                createdBy = commonDaoServices.concatenateName(user)
                createdOn = commonDaoServices.getTimestamp()
            }
            fuelRemediationInvoice = fuelRemediationInvoiceRepo.save(fuelRemediationInvoice)

            val batchInvoiceDetail = invoiceDaoService.createBatchInvoiceDetails(
                user,
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
                accountName = fuelInspection.company
                accountNumber = fuelInspection.stationOwnerEmail
                currency = applicationMapProperties.mapInvoiceTransactionsLocalCurrencyPrefix
            }

            invoiceDaoService.createPaymentDetailsOnStgReconciliationTable(
                user,
                updateBatchInvoiceDetail,
                myAccountDetails
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
                userTaskId = applicationMapProperties.mapMSUserTaskNameEPRA
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
        user: UsersEntity
    ): Pair<ServiceRequestsEntity, MsFuelRemedyInvoicesEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var fuelRemediationInvoice = body
        try {
            with(fuelRemediationInvoice) {
                lastModifiedBy = commonDaoServices.concatenateName(user)
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

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun updateRemediationDetailsAfterPaymentDone() {
        val map = commonDaoServices.serviceMapDetails(appId)
        //Find all permits with Paid status
        val invoices = fuelRemediationInvoiceRepo.findAllByPaymentStatus(map.activeStatus)
        if  (invoices != null){
            for (inv in invoices) {

                try {
                   var remediationDetails = findFuelScheduledRemediationDetails(inv.fuelInspectionId?: throw ExpectedDataNotFound("MISSING FUEL INSPECTION ID"))?: throw ExpectedDataNotFound("NO REMEDIATION DETAILS FOUND")
                    with(remediationDetails){
                        proFormaInvoiceNo = inv.invoiceNumber
                        invoiceAmount = inv.transportGrandTotal
                        feePaidReceiptNo = inv.receiptNo
                        dateOfPayment = inv.paymentDate
                        modifiedBy = "SCHEDULER"
                        modifiedOn = commonDaoServices.getTimestamp()
                    }
                    remediationDetails = fuelRemediationRepo.save(remediationDetails)

                    var fuelInspection = findFuelInspectionDetailByID(remediationDetails.fuelInspectionId?: throw ExpectedDataNotFound("MISSING FUEL INSPECTION ID"))
                    with(fuelInspection){
                        remediationPaymentStatus = map.activeStatus
                        userTaskId = applicationMapProperties.mapMSUserTaskNameOFFICER
                        lastModifiedBy = "SCHEDULER"
                        lastModifiedOn = commonDaoServices.getTimestamp()
                    }
                    fuelInspection = fuelInspectionRepo.save(fuelInspection)

                } catch (e: Exception) {
                    KotlinLogging.logger { }.error(e.message)
                    KotlinLogging.logger { }.debug(e.message, e)

                    continue
                }
//            permitRepo.save(permit)
            }
        }

    }

    fun mapFuelInspectionListDto(fuelInspectionList: Page<MsFuelInspectionEntity>, batchDetails: FuelBatchDetailsDto): FuelInspectionScheduleListDetailsDto {
        val fuelInspectionScheduledList = mutableListOf<FuelInspectionDto>()
        fuelInspectionList.map {fuelInspectionScheduledList.add(FuelInspectionDto(it.id, it.referenceNumber, it.company, it.petroleumProduct, it.physicalLocation, it.inspectionDateFrom, it.inspectionDateTo, it.processStage, it.status==1))}

        return FuelInspectionScheduleListDetailsDto(
            fuelInspectionScheduledList,
            batchDetails
        )
    }

    fun mapFuelInspectionDto(
        fuelInspectionList: MsFuelInspectionEntity,
        batchDetails: FuelBatchDetailsDto,
        rapidTestDone: Boolean,
        compliantStatusDone: Boolean,
        officerList: List<UsersEntity>?,
        officersAssigned : UsersEntity?,
        rapidTestResults: FuelEntityRapidTestDto?,
        sampleCollected: SampleCollectionDto?,
        sampleSubmitted: SampleSubmissionDto?,
        sampleLabResults: MSSSFLabResultsDto?,
        fuelRemediationDto: FuelRemediationDto?
    ): FuelInspectionDto {
        return FuelInspectionDto(
            fuelInspectionList.id,
            fuelInspectionList.referenceNumber,
            fuelInspectionList.company,
            fuelInspectionList.petroleumProduct,
            fuelInspectionList.physicalLocation,
            fuelInspectionList.inspectionDateFrom,
            fuelInspectionList.inspectionDateTo,
            fuelInspectionList.processStage,
            fuelInspectionList.status==1,
            fuelInspectionList.assignedOfficerStatus==1,
            rapidTestDone,
            fuelInspectionList.sampleCollectionStatus==1,
            fuelInspectionList.sampleSubmittedStatus==1,
            fuelInspectionList.bsNumberStatus==1,
            compliantStatusDone,
            fuelInspectionList.remediationStatus==1,
            fuelInspectionList.remediationPaymentStatus==1,
            fuelInspectionList.inspectionCompleteStatus==1,
            batchDetails,
            officerList?.let { mapOfficerListDto(it) },
            officersAssigned?.let { mapOfficerDto (it) },
            rapidTestResults,
            sampleCollected,
            sampleSubmitted,
            sampleLabResults,
            fuelRemediationDto
        )
    }

    fun findAllFuelBatchList(): List<MsFuelBatchInspectionEntity> {
        fuelBatchRepo.findAllOrderByIdAsc()
            ?.let {
                return it
            }
            ?: throw ExpectedDataNotFound("No Files found")
    }

    fun findAllFuelBatchListBasedOnPageable(pageable: Pageable): Page<MsFuelBatchInspectionEntity> {
        fuelBatchRepo.findAll(pageable)
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

    fun findSampleSubmittedListPdfBYSSFid(ssfID: Long): List<QaSampleSubmittedPdfListDetailsEntity>? {
        return sampleSubmissionSavedPdfListRepo.findBySffId(ssfID)
    }

    fun findFuelRemediationInvoice(fuelInspectionID: Long): MsFuelRemedyInvoicesEntity? {
        return fuelRemediationInvoiceRepo.findByFuelInspectionId(fuelInspectionID)
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

    fun findFuelScheduledRemediationDetails(fuelInspectionId: Long): MsFuelRemediationEntity? {
        return fuelRemediationRepo.findByFuelInspectionId(fuelInspectionId)
    }

    fun findFuelInspectionOfficerAssigned(fuelInspection: MsFuelInspectionEntity, status: Int): MsFuelInspectionOfficersEntity? {
        return fuelInspectionOfficerRepo.findByMsFuelInspectionIdAndStatus(fuelInspection,status)
    }

    fun isWithinRange(checkDate: Date, workPlanYearCodes: WorkplanYearsCodesEntity): Boolean {
        return !(checkDate.before(workPlanYearCodes.workplanCreationStartDate) || checkDate.after(workPlanYearCodes.workplanCreationEndDate))
    }

    fun findWorkPlanYearsCode(currentYear: String, map: ServiceMapsEntity): WorkplanYearsCodesEntity? {
       return workPlanYearsCodesRepo.findByYearNameAndStatus(currentYear, map.activeStatus)
    }

    fun findPlanYear(yearNameId: Long): WorkplanYearsCodesEntity {
        workPlanYearsCodesRepo.findByIdOrNull(yearNameId)
           ?.let {
               return it
           }
           ?: throw ExpectedDataNotFound("No Plan Year name found with ID $yearNameId")
    }

    fun mapFuelBatchListDto(
        fuelBatchList: Page<MsFuelBatchInspectionEntity>,
        map: ServiceMapsEntity
    ): Page<FuelBatchDetailsDto> {
        val fuelBatchListDto = mutableListOf<FuelBatchDetailsDto>()
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
                it.remarks,
                it.batchClosed == 1
            )
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
                it.status == 1,
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

    fun mapSampleSubmissionParamListDto(data: List<MsLaboratoryParametersEntity>): List<SampleSubmissionItemsDto> {
        return data.map {
            SampleSubmissionItemsDto(
                it.parameters,
                it.laboratoryName
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
                ssfPdfRemarks.pdfSavedId,
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

    fun mapFuelRemediationDto(data: MsFuelRemediationEntity): FuelRemediationDto {
        return FuelRemediationDto(
            data.productType,
            data.applicableKenyaStandard,
            data.remediationProcedure,
            data.volumeOfProductContaminated,
            data.contaminatedFuelType,
            data.quantityOfFuel,
            data.volumeAdded,
            data.totalVolume,
            data.proFormaInvoiceStatus,
            data.proFormaInvoiceNo,
            data.invoiceAmount,
            data.feePaidReceiptNo,
            data.dateOfRemediation,
            data.dateOfPayment,
        )
    }

     fun mapSampleSubmissionDto(data: MsSampleSubmissionEntity, data2:List<SampleSubmissionItemsDto>): SampleSubmissionDto {
        return SampleSubmissionDto(
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
            rapidTest.rapidTestFailed==map.activeStatus -> {
                 FuelEntityRapidTestDto(
                    rapidTest.rapidTestPassedRemarks,
                    rapidTest.rapidTestPassed==1
                )
            }
            else -> null
        }

    }

    fun mapCompliantStatusDto(compliantDetails: MsFuelInspectionEntity, map: ServiceMapsEntity): FuelEntityCompliantStatusDto? {

        return when {
            compliantDetails.compliantStatus==map.activeStatus -> {
                FuelEntityCompliantStatusDto(
                    compliantDetails.compliantStatusRemarks,
                    compliantDetails.compliantStatus==1
                )
            }
            compliantDetails.notCompliantStatus==map.activeStatus -> {
                FuelEntityCompliantStatusDto(
                    compliantDetails.notCompliantStatusRemarks,
                    compliantDetails.notCompliantStatus==1
                )
            }
            else -> null
        }

    }
}




