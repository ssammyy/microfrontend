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
import java.util.*


@Service
class NewMarketSurveillanceDaoServices(
    private val applicationMapProperties: ApplicationMapProperties,
    private val fuelBatchRepo: IFuelBatchRepository,
    private val fuelInspectionOfficerRepo: IFuelInspectionOfficerRepository,
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
        val fuelInspectionOfficer = findFuelInspectionOfficerAssigned(fileInspectionDetail)
        val officerList = commonDaoServices.findOfficersListBasedOnRegionCountyAndRole(
            applicationMapProperties.mapMSMappedOfficerROLEID,
            batchDetails.countyId ?: throw ExpectedDataNotFound("MISSING BATCH COUNTY ID"),
            batchDetails.regionId ?: throw ExpectedDataNotFound("MISSING BATCH REGION ID")
        )
        return mapFuelInspectionDto(fileInspectionDetail, officerList,fuelInspectionOfficer?.assignedIo)
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
                        return mapFuelInspectionDto(fileInspectionDetail, officerList, fileOfficerSaved.second.assignedIo)
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
                referenceNumber =
                    "FL/FILE/${generateRandomText(5, map.secureRandom, map.messageDigestAlgorithm, true)}".toUpperCase()
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

    fun findFuelInspectionDetailByReferenceNumber(referenceNumber: String): MsFuelInspectionEntity {
        fuelInspectionRepo.findByReferenceNumber(referenceNumber)
            ?.let {
                return it
            }
            ?: throw ExpectedDataNotFound("No File found with the following Ref No ${referenceNumber}")
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
        officersAssigned : UsersEntity?
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
            officersAssigned?.let { mapOfficerDto (it) }
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

    fun findFuelInspectionOfficerAssigned(fuelInspection: MsFuelInspectionEntity): MsFuelInspectionOfficersEntity? {
        return fuelInspectionOfficerRepo.findByMsFuelInspectionId(fuelInspection)
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
}




