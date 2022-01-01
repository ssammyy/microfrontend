package org.kebs.app.kotlin.apollo.api.ports.provided.dao

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.common.dto.ms.*
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.utils.generateRandomText
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.ms.*
import org.kebs.app.kotlin.apollo.store.repo.IServiceRequestsRepository
import org.kebs.app.kotlin.apollo.store.repo.ms.*
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.sql.Timestamp
import java.time.Instant


@Service
class NewMarketSurveillanceDaoServices(
    private val applicationMapProperties: ApplicationMapProperties,
    private val fuelBatchRepo: IFuelBatchRepository,
    private val sampleCollectRepo: ISampleCollectionRepository,
    private val fuelInspectionOfficerRepo: IFuelInspectionOfficerRepository,
    private val sampleCollectParameterRepo: ISampleCollectParameterRepository,
    private val sampleSubmitParameterRepo: ISampleSubmitParameterRepository,
    private val sampleSubmitRepo: IMSSampleSubmissionRepository,
    private val fuelInspectionRepo: IFuelInspectionRepository,
    private val serviceRequestsRepo: IServiceRequestsRepository,
    private val commonDaoServices: CommonDaoServices
) {
    final var appId = applicationMapProperties.mapMarketSurveillance

    @PreAuthorize("hasAuthority('EPRA')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun createNewFuelBatch(body: BatchFileFuelSaveDto): List<FuelInspectionDto> {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val fileSaved = fuelCreateBatch(body, map, loggedInUser)

        if (fileSaved.first.status == map.successStatus) {
            val fileInspectionList = findAllFuelInspectionListBasedOnBatchID(fileSaved.second.id)
            return mapFuelInspectionListDto(fileInspectionList)
        } else {
            throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved.first))
        }
    }

    @PreAuthorize("hasAuthority('EPRA')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun closeFuelBatchCreated(referenceNumber: String): MsFuelBatchInspectionEntity {
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

            return fileSaved.second
        } else {
            throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved.first))
        }
    }

    @PreAuthorize("hasAuthority('EPRA')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun createNewFuelSchedule(body: FuelEntityDto, referenceNumber: String): List<FuelInspectionDto> {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val batchDetail = findFuelBatchDetailByReferenceNumber(referenceNumber)
        val fileSaved = fuelCreateSchedule(body, batchDetail.id, map, loggedInUser)

        if (fileSaved.first.status == map.successStatus) {
            val fileInspectionList = findAllFuelInspectionListBasedOnBatchID(batchDetail.id)
            return mapFuelInspectionListDto(fileInspectionList)
        } else {
            throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved.first))
        }
    }

    @PreAuthorize("hasAuthority('EPRA')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun getAllFuelBatchList(): List<FuelBatchDetailsDto> {
        val fuelBatchList = findAllFuelBatchList()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        return mapFuelBatchListDto(fuelBatchList, map)
    }

    @PreAuthorize("hasAuthority('EPRA')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun getAllFuelInspectionListBasedOnBatchRefNo(batchReferenceNo: String): List<FuelInspectionDto> {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        val findBatchDetail = findFuelBatchDetailByReferenceNumber(batchReferenceNo)
        val fileInspectionList = findAllFuelInspectionListBasedOnBatchID(findBatchDetail.id)
        return mapFuelInspectionListDto(fileInspectionList)
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
        val rapidTestStatus = mapRapidTestDto(fileInspectionDetail, map)
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
        val fileInspectionDetail = findFuelInspectionDetailByReferenceNumber(referenceNo)
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
        val fileInspectionDetail = findFuelInspectionDetailByReferenceNumber(referenceNo)
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
        val fileInspectionDetail = findFuelInspectionDetailByReferenceNumber(referenceNo)
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
                        val fileSaved = updateFuelInspectionDetails(fileInspectionDetail, map, loggedInUser)
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
        val fileInspectionDetail = findFuelInspectionDetailByReferenceNumber(referenceNo)
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
                        val fileSaved = updateFuelInspectionDetails(fileInspectionDetail, map, loggedInUser)
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
        bsNumberProvided: String
    ): FuelInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        val fileInspectionDetail = findFuelInspectionDetailByReferenceNumber(referenceNo)
        val batchDetails = findFuelBatchDetailByReferenceNumber(batchReferenceNo)
        val sampleCollected = findSampleCollectedDetailByFuelInspectionID(fileInspectionDetail.id)
        val sampleSubmission = findSampleSubmissionDetailBySampleCollectedID(sampleCollected?.id?: throw ExpectedDataNotFound("MISSING SAMPLE COLLECTED FOR FUEL INSPECTION REF NO $referenceNo"))?: throw ExpectedDataNotFound("MISSING SAMPLE SUBMITTED FOR FUEL INSPECTION WITH REF NO $referenceNo")
        with(sampleSubmission){
            bsNumber = bsNumberProvided
            labResultsStatus = map.inactiveStatus
        }
        val updatedSampleSubmission = fuelSampleSubmissionUpDate(sampleSubmission,map, loggedInUser)

        when (updatedSampleSubmission.first.status) {
            map.successStatus -> {
                with(fileInspectionDetail){
                    userTaskId = applicationMapProperties.mapMSUserTaskNameLAB
                }
                val fileSaved = updateFuelInspectionDetails(fileInspectionDetail, map, loggedInUser)
                return fuelInspectionMappingCommonDetails(fileInspectionDetail, map, batchDetails)
            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(updatedSampleSubmission.first))
            }
        }
    }


    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun getFuelInspectionDetailsSampleSubmissionBSNumber(
        referenceNo: String,
        batchReferenceNo: String,
        bsNumber: String
    ): FuelInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        val fileInspectionDetail = findFuelInspectionDetailByReferenceNumber(referenceNo)
        val batchDetails = findFuelBatchDetailByReferenceNumber(batchReferenceNo)
        val sampleCollected = findSampleCollectedDetailByFuelInspectionID(fileInspectionDetail.id)
        val sampleSubmission = findSampleSubmissionDetailBySampleCollectedID(sampleCollected?.id?: throw ExpectedDataNotFound("MISSING SAMPLE COLLECTED FOR FUEL INSPECTION REF NO $referenceNo"))
        if (bsNumber == sampleSubmission?.bsNumber){

        }else{
            throw ExpectedDataNotFound("THE BS NUMBER PROVIDED DOES NOT MATCH THE ONE ATTACHED ON SAMPLE SUBMISSION")
        }

        when (updatedSampleSubmission.first.status) {
            map.successStatus -> {
                with(fileInspectionDetail){
                    userTaskId = applicationMapProperties.mapMSUserTaskNameLAB
                }
                val fileSaved = updateFuelInspectionDetails(fileInspectionDetail, map, loggedInUser)
                return fuelInspectionMappingCommonDetails(fileInspectionDetail, map, batchDetails)
            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(updatedSampleSubmission.first))
            }
        }
    }

    fun fuelInspectionMappingCommonDetails(
        fileInspectionDetail: MsFuelInspectionEntity,
        map: ServiceMapsEntity,
        batchDetails: MsFuelBatchInspectionEntity
    ): FuelInspectionDto {
        val fuelInspectionOfficer = findFuelInspectionOfficerAssigned(fileInspectionDetail, map.activeStatus)
        val officerList = commonDaoServices.findOfficersListBasedOnRegionCountyAndRole(
            applicationMapProperties.mapMSMappedOfficerROLEID,
            batchDetails.countyId ?: throw ExpectedDataNotFound("MISSING BATCH COUNTY ID"),
            batchDetails.regionId ?: throw ExpectedDataNotFound("MISSING BATCH REGION ID")
        )
        val rapidTestStatus = mapRapidTestDto(fileInspectionDetail, map)
        val sampleCollected = findSampleCollectedDetailByFuelInspectionID(fileInspectionDetail.id)
        val sampleCollectedParamList = sampleCollected?.id?.let { findAllSampleCollectedParametersBasedOnSampleCollectedID(it) }
        val sampleCollectedDtoValues = sampleCollectedParamList?.let { mapSampleCollectedParamListDto(it) }?.let { mapSampleCollectedDto(sampleCollected, it) }
        val sampleSubmitted = findSampleSubmissionDetailBySampleCollectedID(fileInspectionDetail.id)
        val sampleSubmittedParamList = sampleSubmitted?.id?.let { findAllSampleSubmissionParametersBasedOnSampleSubmissionID(it) }
        val sampleSubmittedDtoValues = sampleSubmittedParamList?.let { mapSampleSubmissionParamListDto(it) }?.let { mapSampleSubmissionDto(sampleSubmitted, it) }
        return mapFuelInspectionDto(
            fileInspectionDetail,
            officerList,
            fuelInspectionOfficer?.assignedIo,
            rapidTestStatus,
            sampleCollectedDtoValues,
            sampleSubmittedDtoValues
        )
    }


    fun fuelCreateBatch(
        body: BatchFileFuelSaveDto,
        map: ServiceMapsEntity,
        user: UsersEntity
    ): Pair<ServiceRequestsEntity, MsFuelBatchInspectionEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var fuel = MsFuelBatchInspectionEntity()
        try {
            val county = commonDaoServices.findCountiesEntityByCountyId(body.county, map.activeStatus)
            val regionIDValue = county.regionId
                ?: ExpectedDataNotFound("The following County ${county.county}, with ID  = ${county.id} and status = ${county.status}, does not have a region mapped to")
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
                batchFileYear = body.batchFileYear
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
                fileRefNumber = body.fileRefNumber
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
            sr.names = "Fuel Inspection Sample Submission  Paramater To be tested Save file"

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

    fun findAllFuelInspectionListBasedOnBatchID(batchId: Long): List<MsFuelInspectionEntity> {
        fuelInspectionRepo.findAllByBatchId(batchId)
            ?.let {
                return it
            }
            ?: throw ExpectedDataNotFound("No Files found with the following BATCH ID ${batchId}")
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
            ?: throw ExpectedDataNotFound("No File found with the following Ref No $referenceNumber")
    }

    fun findSampleCollectedDetailByFuelInspectionID(fuelInspectionID: Long): MsSampleCollectionEntity? {
        return  sampleCollectRepo.findByMsFuelInspectionId(fuelInspectionID)
    }

    fun findSampleSubmissionDetailBySampleCollectedID(sampleCollectedID: Long): MsSampleSubmissionEntity? {
        return  sampleSubmitRepo.findBySampleCollectionNumber(sampleCollectedID)
    }

    fun mapFuelInspectionListDto(fuelInspectionList: List<MsFuelInspectionEntity>): List<FuelInspectionDto> {
        return fuelInspectionList.map {
            FuelInspectionDto(
                it.id,
                it.referenceNumber,
                it.company,
                it.petroleumProduct,
                it.physicalLocation,
                it.inspectionDateFrom,
                it.inspectionDateTo,

                )
        }
    }

    fun mapFuelInspectionDto(
        fuelInspectionList: MsFuelInspectionEntity,
        officerList: List<UsersEntity>?,
        officersAssigned : UsersEntity?,
        rapidTestResults: FuelEntityRapidTestDto?,
        sampleCollected: SampleCollectionDto?,
        sampleSubmitted: SampleSubmissionDto?
    ): FuelInspectionDto {
        return FuelInspectionDto(
            fuelInspectionList.id,
            fuelInspectionList.referenceNumber,
            fuelInspectionList.company,
            fuelInspectionList.petroleumProduct,
            fuelInspectionList.physicalLocation,
            fuelInspectionList.inspectionDateFrom,
            fuelInspectionList.inspectionDateTo,
            officerList?.let { mapOfficerListDto(it) },
            officersAssigned?.let { mapOfficerDto (it) },
            rapidTestResults?.rapidTestStatus,
            rapidTestResults?.rapidTestRemarks,
            sampleCollected,
            sampleSubmitted
        )
    }

    fun findAllFuelBatchList(): List<MsFuelBatchInspectionEntity> {
        fuelBatchRepo.findAllOrderByIdAsc()
            ?.let {
                return it
            }
            ?: throw ExpectedDataNotFound("No Files found")
    }

    fun findFuelBatchDetailByReferenceNumber(referenceNumber: String): MsFuelBatchInspectionEntity {
        fuelBatchRepo.findByReferenceNumber(referenceNumber)
            ?.let {
                return it
            }
            ?: throw ExpectedDataNotFound("No Files found")
    }

    fun findFuelInspectionOfficerAssigned(fuelInspection: MsFuelInspectionEntity, status: Int): MsFuelInspectionOfficersEntity? {
        return fuelInspectionOfficerRepo.findByMsFuelInspectionIdAndStatus(fuelInspection,status)
    }

    fun mapFuelBatchListDto(
        fuelBatchList: List<MsFuelBatchInspectionEntity>,
        map: ServiceMapsEntity
    ): List<FuelBatchDetailsDto> {
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
                it.batchFileYear,
                it.remarks,
                it.batchClosed
            )
        }
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
}




