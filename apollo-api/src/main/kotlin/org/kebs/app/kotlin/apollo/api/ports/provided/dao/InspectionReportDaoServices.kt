package org.kebs.app.kotlin.apollo.api.ports.provided.dao

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.controllers.qaControllers.ReportsController
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.QualityAssuranceBpmn
import org.kebs.app.kotlin.apollo.common.dto.ApiResponseModel
import org.kebs.app.kotlin.apollo.common.dto.qa.*
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.ServiceMapNotFoundException
import org.kebs.app.kotlin.apollo.common.utils.generateRandomText
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.ServiceMapsEntity
import org.kebs.app.kotlin.apollo.store.model.ServiceRequestsEntity
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.qa.*
import org.kebs.app.kotlin.apollo.store.repo.*
import org.kebs.app.kotlin.apollo.store.repo.qa.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.sql.Timestamp
import java.time.Instant


@Service
class InspectionReportDaoServices(
    private val applicationMapProperties: ApplicationMapProperties,
    private val commonDaoServices: CommonDaoServices,

    private val serviceRequestsRepository: IServiceRequestsRepository,
    private val qaInspectionOPCRepo: IQaInspectionOpcEntityRepository,
    private val qaInspectionProductLabelRepo: IQaInspectionProductLabelEntityRepository,

    private val qaInspectionTechnicalRepo: IQaInspectionTechnicalRepository,
    private val qaInspectionReportRecommendationRepo: IQaInspectionReportRecommendationRepository,
    private val qaInspectionHaccpImplementationRepo: IQaInspectionHaccpImplementationRepository,

    private val qaDaoServices: QADaoServices,


    ) {


    @Lazy
    @Autowired
    lateinit var qualityAssuranceBpmn: QualityAssuranceBpmn

    @Lazy
    @Autowired
    lateinit var reportsControllers: ReportsController

    @Lazy
    @Autowired
    lateinit var systemsAdminDaoService: SystemsAdminDaoService


    final var appId = applicationMapProperties.mapQualityAssurance

    /*:::::::::::::::::::::::::::::::::::::::::::::START INTERNAL USER FUNCTIONALITY: INSPECTION REPORT :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

    fun findInspectionReportByID(id: Long): QaInspectionReportRecommendationEntity {
        qaInspectionReportRecommendationRepo.findByIdOrNull(id)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No Inspection Report found with the following [ID=$id]")
    }


    fun inspectionReportNewSave(
        permitID: Long,
        body: TechnicalDetailsDto
    ): ApiResponseModel {

        try {
            val map = commonDaoServices.serviceMapDetails(appId)
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val permit = qaDaoServices.findPermitBYID(permitID)
            var qaInspectionReportRecommendation = QaInspectionReportRecommendationEntity()
            var inspectionTechnicalDetails = QaInspectionTechnicalEntity()

            //generate new technical report
            qaInspectionTechnicalRepo.findByIdOrNull(body.id ?: -1L)
                ?.let { iTDetails ->
                    inspectionTechnicalDetails =
                        saveNewInspectionCheckListRecommendation(body, iTDetails, map, loggedInUser, true)
                    inspectionTechnicalDetails = qaInspectionTechnicalRepo.save(inspectionTechnicalDetails)

                }
                ?: kotlin.run {
                    with(qaInspectionReportRecommendation) {
                        refNo =
                            "REF${
                                generateRandomText(
                                    5,
                                    map.secureRandom,
                                    map.messageDigestAlgorithm,
                                    true
                                )
                            }".toUpperCase()
                        permitId = permit.id
                        permitRefNumber = permit.permitRefNumber
                        filledQpsmsStatus = map.activeStatus
                        status = map.activeStatus
                        createdBy = commonDaoServices.concatenateName(loggedInUser)
                        createdOn = commonDaoServices.getTimestamp()
                    }
                    qaInspectionReportRecommendation =
                        qaInspectionReportRecommendationRepo.save(qaInspectionReportRecommendation)


                    with(inspectionTechnicalDetails) {
                        inspectionRecommendationId = qaInspectionReportRecommendation.id
                        permitId = permit.id
                        permitRefNumber = permit.permitRefNumber
                        status = map.activeStatus
                        createdBy = commonDaoServices.concatenateName(loggedInUser)
                        createdOn = commonDaoServices.getTimestamp()
                    }
                    inspectionTechnicalDetails = saveNewInspectionCheckListRecommendation(
                        body,
                        inspectionTechnicalDetails,
                        map,
                        loggedInUser,
                        false
                    )
                    inspectionTechnicalDetails = qaInspectionTechnicalRepo.save(inspectionTechnicalDetails)

                }
            val inspectionReportAllDetails =
                mapAllInspectionReportDetailsTogetherForInternalUsers(qaInspectionReportRecommendation, map)
            return commonDaoServices.setSuccessResponse(inspectionReportAllDetails, null, null, null)

        } catch (error: Exception) {
            return commonDaoServices.setErrorResponse(error.message ?: "UNKNOWN_ERROR")
        }
    }

    fun saveNewInspectionCheckListRecommendation(
        body: TechnicalDetailsDto,
        inspection: QaInspectionTechnicalEntity,
        map: ServiceMapsEntity,
        user: UsersEntity,
        update: Boolean
    ): QaInspectionTechnicalEntity {
        with(inspection) {
            firmImplementedAnyManagementSystem = body.firmImplementedAnyManagementSystem
            firmImplementedAnyManagementSystemRemarks = body.firmImplementedAnyManagementSystemRemarks
            indicateRelevantProductStandardCodes = body.indicateRelevantProductStandardCodes
            indicateRelevantProductStandardCodesRemarks = body.indicateRelevantProductStandardCodesRemarks
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
        return inspection
    }

    fun inspectionReportDetailSave(
        permitID: Long,
        body: InspectionDetailsDto
    ): ApiResponseModel {

        try {
            val map = commonDaoServices.serviceMapDetails(appId)
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            qaDaoServices.findPermitBYID(permitID)
            val qaInspectionReportRecommendation = QaInspectionReportRecommendationEntity()
            var inspectionTechnicalDetails: QaInspectionTechnicalEntity

            //update technical report
            qaInspectionTechnicalRepo.findByIdOrNull(body.id ?: -1L)
                ?.let { iTDetails ->
                    inspectionTechnicalDetails =
                        updateNewInspectionCheckListRecommendation(body, iTDetails, map, loggedInUser)
                    inspectionTechnicalDetails = qaInspectionTechnicalRepo.save(inspectionTechnicalDetails)
                }
            val inspectionReportAllDetails =
                mapAllInspectionReportDetailsTogetherForInternalUsers(qaInspectionReportRecommendation, map)
            return commonDaoServices.setSuccessResponse(inspectionReportAllDetails, null, null, null)

        } catch (error: Exception) {
            return commonDaoServices.setErrorResponse(error.message ?: "UNKNOWN_ERROR")
        }
    }

    fun updateNewInspectionCheckListRecommendation(
        body: InspectionDetailsDto,
        inspection: QaInspectionTechnicalEntity,
        map: ServiceMapsEntity,
        user: UsersEntity
    ): QaInspectionTechnicalEntity {
        with(inspection) {
            complianceApplicableStatutory = body.complianceApplicableStatutory
            complianceApplicableStatutoryRemarks = body.complianceApplicableStatutoryRemarks
            plantHouseKeeping = body.plantHouseKeeping
            plantHouseKeepingRemarks = body.plantHouseKeepingRemarks
            handlingComplaints = body.handlingComplaints
            handlingComplaintsRemarks = body.handlingComplaintsRemarks
            qualityControlPersonnel = body.qualityControlPersonnel
            qualityControlPersonnelRemarks = body.qualityControlPersonnelRemarks
            testingFacility = body.testingFacility
            testingFacilityRemarks = body.testingFacilityRemarks
            modifiedBy = commonDaoServices.concatenateName(user)
            modifiedOn = commonDaoServices.getTimestamp()


        }
        return inspection
    }

    fun inspectionReportDetailBSave(
        permitID: Long,
        body: InspectionDetailsDtoB
    ): ApiResponseModel {

        try {
            val map = commonDaoServices.serviceMapDetails(appId)
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            qaDaoServices.findPermitBYID(permitID)
            val qaInspectionReportRecommendation = QaInspectionReportRecommendationEntity()
            var inspectionTechnicalDetails: QaInspectionTechnicalEntity

            //update technical report
            qaInspectionTechnicalRepo.findByIdOrNull(body.id ?: -1L)
                ?.let { iTDetails ->
                    inspectionTechnicalDetails =
                        updateNewInspectionCheckListRecommendationB(body, iTDetails, map, loggedInUser)
                    inspectionTechnicalDetails = qaInspectionTechnicalRepo.save(inspectionTechnicalDetails)
                }
            val inspectionReportAllDetails =
                mapAllInspectionReportDetailsTogetherForInternalUsers(qaInspectionReportRecommendation, map)
            return commonDaoServices.setSuccessResponse(inspectionReportAllDetails, null, null, null)

        } catch (error: Exception) {
            return commonDaoServices.setErrorResponse(error.message ?: "UNKNOWN_ERROR")
        }
    }

    fun updateNewInspectionCheckListRecommendationB(
        body: InspectionDetailsDtoB,
        inspection: QaInspectionTechnicalEntity,
        map: ServiceMapsEntity,
        user: UsersEntity
    ): QaInspectionTechnicalEntity {
        with(inspection) {
            equipmentCalibration = body.equipmentCalibration
            equipmentCalibrationRemarks = body.equipmentCalibrationRemarks
            qualityRecords = body.qualityRecords
            qualityRecordsRemarks = body.qualityRecordsRemarks
            recordsNonconforming = body.recordsNonconforming
            recordsNonconformingRemarks = body.recordsNonconformingRemarks
            productRecallRecords = body.productRecallRecords
            productRecallRecordsRemarks = body.productRecallRecordsRemarks
            modifiedBy = commonDaoServices.concatenateName(user)
            modifiedOn = commonDaoServices.getTimestamp()


        }
        return inspection
    }


    fun productLabellingSave(
        permitID: Long,
        inspectionReportRecommendationID: Long,
        body: List<ProductLabellingDto>
    ): ApiResponseModel {

        try {
            val map = commonDaoServices.serviceMapDetails(appId)
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val permit = qaDaoServices.findPermitBYID(permitID)
            val qaInspectionReportRecommendation = findInspectionReportByID(inspectionReportRecommendationID)

            body.forEach { bodyDetails ->
                addInspectionCheckListProductLabelling(
                    bodyDetails,
                    qaInspectionReportRecommendation.id ?: throw Exception("MISSING INSPECTION RECOMMENDATION ID"),
                    permit.id ?: throw Exception("MISSING PERMIT ID"),
                    permit.permitRefNumber ?: throw Exception("MISSING PERMIT REF NUMBER"),
                    map,
                    loggedInUser
                )

            }
            val inspectionReportAllDetails =
                mapAllInspectionReportDetailsTogetherForInternalUsers(qaInspectionReportRecommendation, map)
            return commonDaoServices.setSuccessResponse(inspectionReportAllDetails, null, null, null)

        } catch (error: Exception) {
            return commonDaoServices.setErrorResponse(error.message ?: "UNKNOWN_ERROR")
        }
    }

    fun addInspectionCheckListProductLabelling(
        body: ProductLabellingDto,
        inspectionReportRecommendationID: Long,
        permitID: Long,
        permitRefNumber: String,
        map: ServiceMapsEntity,
        user: UsersEntity
    ): Pair<ServiceRequestsEntity, QaInspectionProductLabelEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var inspection = QaInspectionProductLabelEntity()
        try {
            qaInspectionProductLabelRepo.findByIdOrNull(body.id ?: -1L)
                ?.let { fdr ->
                    inspection = saveInspectionProductLabelling(
                        body,
                        inspectionReportRecommendationID,
                        permitID,
                        permitRefNumber,
                        fdr,
                        map,
                        user,
                        true
                    )
                } ?: kotlin.run {
                inspection = saveInspectionProductLabelling(
                    body,
                    inspectionReportRecommendationID,
                    permitID,
                    permitRefNumber,
                    inspection,
                    map,
                    user,
                    false
                )
            }

            inspection = qaInspectionProductLabelRepo.save(inspection)

            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(inspection)} "
            sr.names = "Inspection Details Save file"

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = map.successStatus
            sr = serviceRequestsRepository.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(body)}"
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepository.save(sr)

        }
        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return Pair(sr, inspection)
    }

    fun saveInspectionProductLabelling(
        body: ProductLabellingDto,
        inspectionReportRecommendationID: Long,
        permitID: Long,
        permitRefNUMBER: String,
        inspectionTechnical: QaInspectionProductLabelEntity,
        map: ServiceMapsEntity,
        user: UsersEntity,
        update: Boolean
    ): QaInspectionProductLabelEntity {
        with(inspectionTechnical) {
            standardMarking = body.standardMarking
            findings = body.findings
            statusOfCompliance = body.statusOfCompliance
            status = map.activeStatus
            inspectionRecommendationId = inspectionReportRecommendationID
            permitId = permitID
            permitRefNumber = permitRefNUMBER
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
        return inspectionTechnical
    }


    fun inspectionReportStandardizationMarkSchemeSave(
        permitID: Long,
        body: StandardizationMarkScheme
    ): ApiResponseModel {

        try {
            val map = commonDaoServices.serviceMapDetails(appId)
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            qaDaoServices.findPermitBYID(permitID)
            val qaInspectionReportRecommendation = QaInspectionReportRecommendationEntity()
            var inspectionTechnicalDetails: QaInspectionTechnicalEntity

            //update technical report
            qaInspectionTechnicalRepo.findByIdOrNull(body.id ?: -1L)
                ?.let { iTDetails ->
                    inspectionTechnicalDetails =
                        updateNewInspectionCheckListStandardizationMarkSchemeB(body, iTDetails, map, loggedInUser)
                    inspectionTechnicalDetails = qaInspectionTechnicalRepo.save(inspectionTechnicalDetails)
                }
            val inspectionReportAllDetails =
                mapAllInspectionReportDetailsTogetherForInternalUsers(qaInspectionReportRecommendation, map)
            return commonDaoServices.setSuccessResponse(inspectionReportAllDetails, null, null, null)

        } catch (error: Exception) {
            return commonDaoServices.setErrorResponse(error.message ?: "UNKNOWN_ERROR")
        }
    }

    fun updateNewInspectionCheckListStandardizationMarkSchemeB(
        body: StandardizationMarkScheme,
        inspection: QaInspectionTechnicalEntity,
        map: ServiceMapsEntity,
        user: UsersEntity
    ): QaInspectionTechnicalEntity {
        with(inspection) {
            validitySmarkPermit = body.validitySmarkPermit
            validitySmarkPermitRemarks = body.validitySmarkPermitRemarks
            useTheSmark = body.useTheSmark
            useTheSmarkRemarks = body.useTheSmarkRemarks
            changesAffectingProductCertification = body.changesAffectingProductCertification
            changesAffectingProductCertificationRemarks = body.changesAffectingProductCertificationRemarks
            changesBeenCommunicatedKebs = body.changesBeenCommunicatedKebs
            changesBeenCommunicatedKebsRemarks = body.changesBeenCommunicatedKebsRemarks
            samplesDrawn = body.samplesDrawn
            samplesDrawnRemarks = body.samplesDrawnRemarks
            modifiedBy = commonDaoServices.concatenateName(user)
            modifiedOn = commonDaoServices.getTimestamp()


        }
        return inspection
    }


    fun inspectionCheckListInspectionReportDetailsOPCSave(
        permitID: Long,
        inspectionReportRecommendationID: Long,
        body: List<OperationProcessAndControlsDetailsApplyDto>
    ): ApiResponseModel {

        try {
            val map = commonDaoServices.serviceMapDetails(appId)
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val permit = qaDaoServices.findPermitBYID(permitID)
            val qaInspectionReportRecommendation = findInspectionReportByID(inspectionReportRecommendationID)

            body.forEach { bodyDetails ->
                addInspectionCheckListInspectionReportDetailsOPC(
                    bodyDetails,
                    qaInspectionReportRecommendation.id ?: throw Exception("MISSING INSPECTION RECOMMENDATION ID"),
                    permit.id ?: throw Exception("MISSING PERMIT ID"),
                    permit.permitRefNumber ?: throw Exception("MISSING PERMIT REF NUMBER"),
                    map,
                    loggedInUser
                )

            }
            val inspectionReportAllDetails =
                mapAllInspectionReportDetailsTogetherForInternalUsers(qaInspectionReportRecommendation, map)
            return commonDaoServices.setSuccessResponse(inspectionReportAllDetails, null, null, null)

        } catch (error: Exception) {
            return commonDaoServices.setErrorResponse(error.message ?: "UNKNOWN_ERROR")
        }
    }

    fun addInspectionCheckListInspectionReportDetailsOPC(
        body: OperationProcessAndControlsDetailsApplyDto,
        inspectionReportRecommendationID: Long,
        permitID: Long,
        permitRefNumber: String,
        map: ServiceMapsEntity,
        user: UsersEntity
    ): Pair<ServiceRequestsEntity, QaInspectionOpcEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var inspection = QaInspectionOpcEntity()
        try {
            qaInspectionOPCRepo.findByIdOrNull(body.id ?: -1L)
                ?.let { fdr ->
                    inspection = saveInspectionCheckListOPC(
                        body,
                        inspectionReportRecommendationID,
                        permitID,
                        permitRefNumber,
                        fdr,
                        map,
                        user,
                        true
                    )
                } ?: kotlin.run {
                inspection = saveInspectionCheckListOPC(
                    body,
                    inspectionReportRecommendationID,
                    permitID,
                    permitRefNumber,
                    inspection,
                    map,
                    user,
                    false
                )
            }

            inspection = qaInspectionOPCRepo.save(inspection)

            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(inspection)} "
            sr.names = "Inspection Details Save file"

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = map.successStatus
            sr = serviceRequestsRepository.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(body)}"
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepository.save(sr)

        }
        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return Pair(sr, inspection)
    }


    fun saveInspectionCheckListOPC(
        body: OperationProcessAndControlsDetailsApplyDto,
        inspectionReportRecommendationID: Long,
        permitID: Long,
        permitRefNUMBER: String,
        inspectionTechnical: QaInspectionOpcEntity,
        map: ServiceMapsEntity,
        user: UsersEntity,
        update: Boolean
    ): QaInspectionOpcEntity {
        with(inspectionTechnical) {
            processFlow = body.processFlow
            operations = body.operations
            qualityChecks = body.qualityChecks
            frequency = body.frequency
            records = body.records
            findings = body.findings
            status = map.activeStatus
            inspectionRecommendationId = inspectionReportRecommendationID
            permitId = permitID
            permitRefNumber = permitRefNUMBER
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
        return inspectionTechnical
    }

    fun inspectionCheckListInspectionHACCPImplementationSave(
        permitID: Long,
        inspectionReportRecommendationID: Long,
        body: HaccpImplementationDetailsApplyDto
    ): ApiResponseModel {
        val map = commonDaoServices.serviceMapDetails(appId)
        val user = commonDaoServices.loggedInUserDetails()
        val permit = qaDaoServices.findPermitBYID(permitID)
        val permitRefNumber = permit.permitRefNumber ?: throw Exception("MISSING PERMIT REF NUMBER")
        val qaInspectionReportRecommendation = findInspectionReportByID(inspectionReportRecommendationID)

        var inspection = QaInspectionHaccpImplementationEntity()
        try {
            qaInspectionHaccpImplementationRepo.findByIdOrNull(body.id ?: -1L)
                ?.let { fdr ->
                    inspection = saveInspectionCheckListHaccpImplementation(
                        body,
                        inspectionReportRecommendationID,
                        permitID,
                        permitRefNumber,
                        fdr,
                        map,
                        user,
                        true
                    )
                } ?: kotlin.run {
                inspection = saveInspectionCheckListHaccpImplementation(
                    body,
                    inspectionReportRecommendationID,
                    permitID,
                    permitRefNumber,
                    inspection,
                    map,
                    user,
                    false
                )
            }

            inspection = qaInspectionHaccpImplementationRepo.save(inspection)

            val inspectionReportAllDetails =
                mapAllInspectionReportDetailsTogetherForInternalUsers(qaInspectionReportRecommendation, map)
            return commonDaoServices.setSuccessResponse(inspectionReportAllDetails, null, null, null)

        } catch (error: Exception) {
            return commonDaoServices.setErrorResponse(error.message ?: "UNKNOWN_ERROR")
        }
    }

    fun saveInspectionCheckListHaccpImplementation(
        body: HaccpImplementationDetailsApplyDto,
        inspectionReportRecommendationID: Long,
        permitID: Long,
        permitRefNUMBER: String,
        inspectionTechnical: QaInspectionHaccpImplementationEntity,
        map: ServiceMapsEntity,
        user: UsersEntity,
        update: Boolean
    ): QaInspectionHaccpImplementationEntity {
        with(inspectionTechnical) {
            designFacilitiesConstructionLayout = body.designFacilitiesConstructionLayout
            designFacilitiesConstructionLayoutRemarks = body.designFacilitiesConstructionLayoutRemarks
            maintenanceSanitationCleaningPrograms = body.maintenanceSanitationCleaningPrograms
            maintenanceSanitationCleaningProgramsRemarks = body.maintenanceSanitationCleaningProgramsRemarks
            personnelHygiene = body.personnelHygiene
            personnelHygieneRemarks = body.personnelHygieneRemarks
            transportationConveyance = body.transportationConveyance
            transportationConveyanceRemarks = body.transportationConveyanceRemarks
            determinationCriticalParameters = body.determinationCriticalParameters
            determinationCriticalParametersRemarks = body.determinationCriticalParametersRemarks
            evidenceCorrectiveActions = body.evidenceCorrectiveActions
            evidenceCorrectiveActionsRemarks = body.evidenceCorrectiveActionsRemarks
            status = map.activeStatus
            inspectionRecommendationId = inspectionReportRecommendationID
            permitId = permitID
            permitRefNumber = permitRefNUMBER
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
        return inspectionTechnical
    }


    @PreAuthorize("hasAuthority('QA_MANAGER_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun updatePermitInspectionCheckListDetails(
        permitID: Long,
        body: AllInspectionDetailsApplyDto
    ): ApiResponseModel {

        try {
            val map = commonDaoServices.serviceMapDetails(appId)
            val loggedInUser = commonDaoServices.loggedInUserDetails()

            var qaInspectionReportRecommendation = QaInspectionReportRecommendationEntity()
            qaInspectionReportRecommendationRepo.findByIdOrNull(body.id ?: -1L)
                ?.let { fdr ->
                    qaInspectionReportRecommendation =
                        saveInspectionCheckListRecommendation(body, fdr, map, loggedInUser, true)
                }
            qaInspectionReportRecommendation =
                qaInspectionReportRecommendationRepo.save(qaInspectionReportRecommendation)

            val inspectionReportAllDetails =
                mapAllInspectionReportDetailsTogetherForInternalUsers(qaInspectionReportRecommendation, map)
            return commonDaoServices.setSuccessResponse(inspectionReportAllDetails, null, null, null)


        } catch (error: Exception) {
            return commonDaoServices.setErrorResponse(error.message ?: "UNKNOWN_ERROR")
        }
    }


    fun saveInspectionCheckListRecommendation(
        body: AllInspectionDetailsApplyDto,
        inspection: QaInspectionReportRecommendationEntity,
        map: ServiceMapsEntity,
        user: UsersEntity,
        update: Boolean
    ): QaInspectionReportRecommendationEntity {
        with(inspection) {
            followPreviousRecommendationsNonConformities = body.followPreviousRecommendationsNonConformities
            recommendations = body.recommendations
            inspectorComments = body.inspectorComments
            inspectorName = body.inspectorName
            inspectorDate = body.inspectorDate
            supervisorComments = body.supervisorComments
            supervisorName = body.supervisorName
            supervisorDate = body.supervisorDate
//            varField1 = body.documentsID?.let { commonDaoServices.convertClassToJson(it) }
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
        return inspection
    }

    @PreAuthorize("hasAuthority('QA_MANAGER_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun submitFinalInspectionReport(
        permitID: Long,
    ): ApiResponseModel {
        try {
            val map = commonDaoServices.serviceMapDetails(appId)
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            var permit = qaDaoServices.findPermitBYID(permitID)
            with(permit) {
                inspectionReportGenerated = 1
                permitStatus = applicationMapProperties.mapQaStatusPfactoryInsForms
            }
            val updateResults = qaDaoServices.permitUpdateDetails(permit, map, loggedInUser)

            return when (updateResults.first.status) {
                map.successStatus -> {
                    permit = updateResults.second
                    val batchID: Long? = qaDaoServices.getBatchID(permit, map, permitID)
                    val batchIDDifference: Long? = qaDaoServices.getBatchIDDifference(permit, map, permitID)
                    val permitAllDetails =
                        qaDaoServices.mapAllPermitDetailsTogetherForInternalUsers(
                            permit,
                            batchID,
                            batchIDDifference,
                            map
                        )
                    commonDaoServices.setSuccessResponse(permitAllDetails, null, null, null)
                }

                else -> {
                    return commonDaoServices.setErrorResponse(
                        updateResults.first.responseMessage ?: "UNKNOWN_ERROR"
                    )
                }
            }

        } catch (error: Exception) {
            return commonDaoServices.setErrorResponse(error.message ?: "UNKNOWN_ERROR")

        }

    }


    fun mapAllInspectionReportDetailsTogetherForInternalUsers(
        inspectionReport: QaInspectionReportRecommendationEntity,
        map: ServiceMapsEntity,
    ): AllInspectionDetailsApplyDto {
        val inspectionReportId = inspectionReport.id ?: throw Exception("MISSING INSPECTION REPORT ID")



        return AllInspectionDetailsApplyDto(
            inspectionReportId,
            inspectionReportTechnicalDetails(inspectionReportId, map),
            inspectionReportInspectionDetails(inspectionReportId, map),
            inspectionReportInspectionDetailsB(inspectionReportId, map),
            productLabellingListDto(findAllProductLabellingByInspectionId(inspectionReportId)),
            inspectionReportStandardizationMarkScheme(inspectionReportId, map),
            findAllOperationProcessAndControlsListId(
                inspectionReportId ?: throw Exception("INVALID PERMIT REF NUMBER")
            ).let { operationProcessAndControlsListDto(it) },
            inspectionHaccpImplementationEntity(inspectionReportId, map),
            inspectionReport.followPreviousRecommendationsNonConformities,
            inspectionReport.recommendations,
            inspectionReport.inspectorComments,
            inspectionReport.inspectorName,
            inspectionReport.inspectorDate,
            inspectionReport.supervisorComments,
            inspectionReport.supervisorName,
            inspectionReport.supervisorDate,

            )
    }


    fun inspectionReportTechnicalDetails(
        inspectionReportTechnicalId: Long,
        map: ServiceMapsEntity
    ): TechnicalDetailsDto {

        val inspectionReportTechnical =
            qaInspectionTechnicalRepo.findByInspectionRecommendationId(inspectionReportTechnicalId)
                ?: throw ServiceMapNotFoundException("Inspection Report not found")
        val it = TechnicalDetailsDto()
        with(it)
        {
            id = inspectionReportTechnical.id
            inspectionRecommendationId = inspectionReportTechnical.inspectionRecommendationId
            firmImplementedAnyManagementSystem = inspectionReportTechnical.firmImplementedAnyManagementSystem
            firmImplementedAnyManagementSystemRemarks =
                inspectionReportTechnical.firmImplementedAnyManagementSystemRemarks
            indicateRelevantProductStandardCodes = inspectionReportTechnical.indicateRelevantProductStandardCodes
            indicateRelevantProductStandardCodesRemarks =
                inspectionReportTechnical.indicateRelevantProductStandardCodesRemarks

        }
        return it
    }


    fun inspectionReportInspectionDetails(
        inspectionReportTechnicalId: Long,
        map: ServiceMapsEntity
    ): InspectionDetailsDto {

        val inspectionReportTechnical =
            qaInspectionTechnicalRepo.findByInspectionRecommendationId(inspectionReportTechnicalId)
                ?: throw ServiceMapNotFoundException("Inspection Report not found")
        val it = InspectionDetailsDto()
        with(it)
        {
            id = inspectionReportTechnical.id
            inspectionRecommendationId = inspectionReportTechnical.inspectionRecommendationId
            complianceApplicableStatutory = inspectionReportTechnical.complianceApplicableStatutory
            complianceApplicableStatutoryRemarks =
                inspectionReportTechnical.complianceApplicableStatutoryRemarks
            plantHouseKeeping = inspectionReportTechnical.plantHouseKeeping
            plantHouseKeepingRemarks =
                inspectionReportTechnical.plantHouseKeepingRemarks
            handlingComplaints =
                inspectionReportTechnical.handlingComplaints
            handlingComplaintsRemarks =
                inspectionReportTechnical.handlingComplaintsRemarks
            qualityControlPersonnel =
                inspectionReportTechnical.qualityControlPersonnel
            qualityControlPersonnelRemarks =
                inspectionReportTechnical.qualityControlPersonnelRemarks
            testingFacility =
                inspectionReportTechnical.testingFacility
            testingFacilityRemarks =
                inspectionReportTechnical.testingFacilityRemarks

        }
        return it
    }

    fun inspectionReportInspectionDetailsB(
        inspectionReportTechnicalId: Long,
        map: ServiceMapsEntity
    ): InspectionDetailsDtoB {

        val inspectionReportTechnical =
            qaInspectionTechnicalRepo.findByInspectionRecommendationId(inspectionReportTechnicalId)
                ?: throw ServiceMapNotFoundException("Inspection Report not found")
        val it = InspectionDetailsDtoB()
        with(it)
        {
            id = inspectionReportTechnical.id
            inspectionRecommendationId = inspectionReportTechnical.inspectionRecommendationId
            equipmentCalibration = inspectionReportTechnical.equipmentCalibration
            equipmentCalibrationRemarks =
                inspectionReportTechnical.equipmentCalibrationRemarks
            qualityRecords = inspectionReportTechnical.qualityRecords
            qualityRecordsRemarks =
                inspectionReportTechnical.qualityRecordsRemarks
            recordsNonconforming =
                inspectionReportTechnical.recordsNonconforming
            recordsNonconformingRemarks =
                inspectionReportTechnical.recordsNonconformingRemarks
            productRecallRecords =
                inspectionReportTechnical.productRecallRecords
            productRecallRecordsRemarks =
                inspectionReportTechnical.productRecallRecordsRemarks
        }
        return it
    }


    fun findAllProductLabellingByInspectionId(
        inspectionId: Long,
    ): List<QaInspectionProductLabelEntity> {
        qaInspectionProductLabelRepo.findByInspectionRecommendationId(inspectionId)
            ?.let {
                return it
            } ?: throw ExpectedDataNotFound("No File found with the following [ INSPECTION ID =$inspectionId]")
    }


    fun productLabellingListDto(userList: List<QaInspectionProductLabelEntity>): List<ProductLabellingDto> {
        return userList.map { u ->
            ProductLabellingDto(
                u.id,
                u.inspectionRecommendationId,
                u.technicalInspectionId?.toLong(),
                u.standardMarking,
                u.findings,
                u.statusOfCompliance

            )
        }
    }


    fun inspectionReportStandardizationMarkScheme(
        inspectionReportTechnicalId: Long,
        map: ServiceMapsEntity
    ): StandardizationMarkScheme {

        val standardizationMarkScheme =
            qaInspectionTechnicalRepo.findByInspectionRecommendationId(inspectionReportTechnicalId)
                ?: throw ServiceMapNotFoundException("Inspection Report not found")
        val it = StandardizationMarkScheme()
        with(it)
        {
            id = standardizationMarkScheme.id
            validitySmarkPermit = standardizationMarkScheme.validitySmarkPermit
            validitySmarkPermitRemarks = standardizationMarkScheme.validitySmarkPermitRemarks
            useTheSmark =
                standardizationMarkScheme.useTheSmark
            useTheSmarkRemarks = standardizationMarkScheme.useTheSmarkRemarks
            changesAffectingProductCertification =
                standardizationMarkScheme.changesAffectingProductCertification
            changesAffectingProductCertificationRemarks =
                standardizationMarkScheme.changesAffectingProductCertificationRemarks
            changesBeenCommunicatedKebs =
                standardizationMarkScheme.changesBeenCommunicatedKebs
            changesBeenCommunicatedKebsRemarks =
                standardizationMarkScheme.changesBeenCommunicatedKebsRemarks
            samplesDrawn =
                standardizationMarkScheme.samplesDrawn
            samplesDrawnRemarks =
                standardizationMarkScheme.samplesDrawnRemarks
        }
        return it
    }


    fun findAllOperationProcessAndControlsListId(
        inspectionId: Long
    ): List<QaInspectionOpcEntity> {
        qaInspectionOPCRepo.findByInspectionRecommendationId(inspectionId)
            ?.let {
                return it
            } ?: throw ExpectedDataNotFound("No File found with the following [ INSPECTION ID =$inspectionId]")
    }

    fun operationProcessAndControlsListDto(userList: List<QaInspectionOpcEntity>): List<OperationProcessAndControlsDetailsApplyDto> {
        return userList.map { u ->
            OperationProcessAndControlsDetailsApplyDto(
                u.id,
                u.inspectionRecommendationId,
                u.processFlow,
                u.operations,
                u.qualityChecks,
                u.frequency,
                u.records,
                u.findings,

                )
        }
    }

    fun inspectionHaccpImplementationEntity(
        inspectionReportTechnicalId: Long,
        map: ServiceMapsEntity
    ): HaccpImplementationDetailsApplyDto {

        val inspectionHaccpImplementation =
            qaInspectionHaccpImplementationRepo.findByInspectionRecommendationId(inspectionReportTechnicalId)
                ?: throw ServiceMapNotFoundException("Inspection Report not found")
        val it = HaccpImplementationDetailsApplyDto()
        with(it)
        {
            id = inspectionHaccpImplementation.id
            inspectionRecommendationId = inspectionHaccpImplementation.inspectionRecommendationId
            designFacilitiesConstructionLayout = inspectionHaccpImplementation.designFacilitiesConstructionLayout
            designFacilitiesConstructionLayoutRemarks =
                inspectionHaccpImplementation.designFacilitiesConstructionLayoutRemarks
            maintenanceSanitationCleaningPrograms = inspectionHaccpImplementation.maintenanceSanitationCleaningPrograms
            maintenanceSanitationCleaningProgramsRemarks =
                inspectionHaccpImplementation.maintenanceSanitationCleaningProgramsRemarks
            personnelHygiene = inspectionHaccpImplementation.personnelHygiene
            personnelHygieneRemarks = inspectionHaccpImplementation.personnelHygieneRemarks
            transportationConveyance = inspectionHaccpImplementation.transportationConveyance
            transportationConveyanceRemarks = inspectionHaccpImplementation.transportationConveyanceRemarks
            determinationCriticalParameters = inspectionHaccpImplementation.determinationCriticalParameters
            determinationCriticalParametersRemarks =
                inspectionHaccpImplementation.determinationCriticalParametersRemarks
            evidenceCorrectiveActions = inspectionHaccpImplementation.evidenceCorrectiveActions
            evidenceCorrectiveActionsRemarks = inspectionHaccpImplementation.evidenceCorrectiveActionsRemarks


        }
        return it
    }


}
