package org.kebs.app.kotlin.apollo.store.repo.qa

import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.importer.TemporaryImportApplicationsEntity
import org.kebs.app.kotlin.apollo.store.model.importer.TemporaryImportApplicationsUploadsEntity
import org.kebs.app.kotlin.apollo.store.model.qa.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository

@Repository
interface IPermitApplicationsRepository : HazelcastRepository<PermitApplicationsEntity, Long> {
    fun findByUserId(userId: Long): List<PermitApplicationsEntity>?
    fun findByUserIdAndPermitType(userId: Long, permitType: Long): List<PermitApplicationsEntity>?
    fun findByUserIdAndPermitTypeAndOldPermitStatusIsNullAndPermitAwardStatusIsNotNull(
        userId: Long,
        permitType: Long
    ): List<PermitApplicationsEntity>?

    fun findByUserIdAndPermitTypeAndOldPermitStatusIsNull(
        userId: Long,
        permitType: Long
    ): List<PermitApplicationsEntity>?

    fun findByCompanyIdAndOldPermitStatusIsNull(
        companyID: Long
    ): List<PermitApplicationsEntity>?

    fun findByUserIdAndPermitTypeAndOldPermitStatusIsNullAndPermitAwardStatus(
        userId: Long,
        permitType: Long,
        permitAwardStatus: Int
    ): List<PermitApplicationsEntity>?

    fun findByUserIdAndPermitTypeAndOldPermitStatusIsNullAndUserTaskId(
        userId: Long,
        permitType: Long,
        userTaskId: Long
    ): List<PermitApplicationsEntity>?

    fun findByUserIdAndPermitTypeAndOldPermitStatusIsNullAndPermitAwardStatusAndFmarkGenerated(
        userId: Long,
        permitType: Long,
        permitAwardStatus: Int,
        fmarkGenerated: Int
    ): List<PermitApplicationsEntity>?

    fun findByQamIdAndPermitType(userId: Long, permitType: Long): List<PermitApplicationsEntity>?
    fun findByQamIdAndPermitTypeAndOldPermitStatusIsNull(
        userId: Long,
        permitType: Long
    ): List<PermitApplicationsEntity>?

    fun findByQamIdAndPermitTypeAndOldPermitStatusIsNullAndUserTaskId(
        qamId: Long,
        permitType: Long,
        userTaskId: Long
    ): List<PermitApplicationsEntity>?

    fun findByQamIdAndPermitTypeAndOldPermitStatusIsNullAndPermitAwardStatusIsNotNull(
        userId: Long,
        permitType: Long
    ): List<PermitApplicationsEntity>?

    fun findByHodIdAndPermitType(userId: Long, permitType: Long): List<PermitApplicationsEntity>?
    fun findByHodIdAndPermitTypeAndOldPermitStatusIsNull(
        userId: Long,
        permitType: Long
    ): List<PermitApplicationsEntity>?

    fun findByHodIdAndPermitTypeAndOldPermitStatusIsNullAndUserTaskId(
        hodId: Long,
        permitType: Long,
        userTaskId: Long
    ): List<PermitApplicationsEntity>?

    fun findByHodIdAndPermitTypeAndOldPermitStatusIsNullAndPermitAwardStatusIsNotNull(
        userId: Long,
        permitType: Long
    ): List<PermitApplicationsEntity>?

    fun findByQaoIdAndPermitType(userId: Long, permitType: Long): List<PermitApplicationsEntity>?
    fun findByQaoIdAndPermitTypeAndOldPermitStatusIsNull(
        userId: Long,
        permitType: Long
    ): List<PermitApplicationsEntity>?

    fun findByQaoIdAndPermitTypeAndOldPermitStatusIsNullAndUserTaskId(
        qaoId: Long,
        permitType: Long,
        userTaskId: Long
    ): List<PermitApplicationsEntity>?

    fun findByQaoIdAndPermitTypeAndOldPermitStatusIsNullAndPermitAwardStatusIsNotNull(
        userId: Long,
        permitType: Long
    ): List<PermitApplicationsEntity>?

    fun findByAssessorIdAndPermitType(userId: Long, permitType: Long): List<PermitApplicationsEntity>?
    fun findByAssessorIdAndPermitTypeAndOldPermitStatusIsNull(
        userId: Long,
        permitType: Long
    ): List<PermitApplicationsEntity>?

    fun findByAssessorIdAndPermitTypeAndOldPermitStatusIsNullAndUserTaskId(
        assessorId: Long,
        permitType: Long,
        userTaskId: Long
    ): List<PermitApplicationsEntity>?

    fun findByAssessorIdAndPermitTypeAndOldPermitStatusIsNullAndPermitAwardStatusIsNotNull(
        userId: Long,
        permitType: Long
    ): List<PermitApplicationsEntity>?

    fun findByPacSecIdAndPermitTypeAndOldPermitStatusIsNull(
        userId: Long,
        permitType: Long
    ): List<PermitApplicationsEntity>?

    fun findByPacSecIdAndPermitTypeAndOldPermitStatusIsNullAndUserTaskId(
        pacSecId: Long,
        permitType: Long,
        userTaskId: Long
    ): List<PermitApplicationsEntity>?

    fun findByPacSecIdAndPermitTypeAndOldPermitStatusIsNullAndPermitAwardStatusIsNotNull(
        userId: Long,
        permitType: Long
    ): List<PermitApplicationsEntity>?

    fun findByUserIdAndPermitTypeAndEndOfProductionStatus(
        userId: Long,
        permitType: Long,
        endOfProductionStatus: Int
    ): List<PermitApplicationsEntity>?

    fun findByUserIdAndPermitTypeAndEndOfProductionStatusAndPermitAwardStatus(
        userId: Long,
        permitType: Long,
        endOfProductionStatus: Int,
        permitAwardStatus: Int
    ): List<PermitApplicationsEntity>?

    fun findByPermitTypeAndEndOfProductionStatusAndPermitAwardStatusAndAttachedPlantId(
        permitType: Long, endOfProductionStatus: Int, permitAwardStatus: Int, attachedPlantId: Long
    ): List<PermitApplicationsEntity>?

    fun findByUserIdAndPermitTypeAndEndOfProductionStatusAndOldPermitStatusIsNull(
        userId: Long,
        permitType: Long,
        endOfProductionStatus: Int
    ): List<PermitApplicationsEntity>?

    fun findByIdAndUserIdAndPermitType(id: Long, userId: Long, permitType: Long): PermitApplicationsEntity?
    fun findByPcmIdAndPermitTypeAndOldPermitStatusIsNull(
        userId: Long,
        permitType: Long
    ): List<PermitApplicationsEntity>?

    fun findByPcmIdAndPermitTypeAndOldPermitStatusIsNullAndUserTaskId(
        pcmId: Long,
        permitType: Long,
        userTaskId: Long
    ): List<PermitApplicationsEntity>?

    fun findByPcmIdAndPermitTypeAndOldPermitStatusIsNullAndPermitAwardStatusIsNotNull(
        userId: Long,
        permitType: Long
    ): List<PermitApplicationsEntity>?

    fun findByPscMemberIdAndPermitTypeAndOldPermitStatusIsNull(
        userId: Long,
        permitType: Long
    ): List<PermitApplicationsEntity>?

    fun findByPscMemberIdAndPermitTypeAndOldPermitStatusIsNullAndUserTaskId(
        pscMemberId: Long,
        permitType: Long,
        userTaskId: Long
    ): List<PermitApplicationsEntity>?

    fun findByPscMemberIdAndPermitTypeAndOldPermitStatusIsNullAndPermitAwardStatusIsNotNull(
        userId: Long,
        permitType: Long
    ): List<PermitApplicationsEntity>?

    fun findByIdAndUserId(id: Long, userId: Long): PermitApplicationsEntity?
    fun findByAwardedPermitNumberAndVersionNumber(
        awardedPermitNumber: String,
        versionNumber: Long
    ): PermitApplicationsEntity?

    fun findAllByPaidStatus(paymentStatus: Int): List<PermitApplicationsEntity>?
    fun findByPermitRefNumberAndOldPermitStatus(
        permitRefNumber: String,
        oldPermitStatus: Int
    ): List<PermitApplicationsEntity>?

    fun findTopByPermitRefNumberOrderByIdDesc(permitRefNumber: String): PermitApplicationsEntity?
    fun findByIdAndAttachedPlantId(id: Long, attachedPlantId: Long): PermitApplicationsEntity?
}


@Repository
interface IPermitTypesEntityRepository : HazelcastRepository<PermitTypesEntity, Long> {
    fun findByStatus(status: Int): List<PermitTypesEntity>?
    fun findByStatusAndId(status: Long, id: Long): PermitTypesEntity?
    fun findByTypeName(typeName: String): PermitTypesEntity?
    fun findByIdAndStatus(id: Long, status: Long): List<PermitTypesEntity>
}

@Repository
interface IQaProcessStatusRepository : HazelcastRepository<QaProcessStatusEntity, Long> {
    fun findByProcessStatusName(processStatusName: String): QaProcessStatusEntity?
    fun findByProcessStatusNameAndStatus(processStatusName: String, status: Long): QaProcessStatusEntity?
    fun findByStatus(status: Int): List<QaProcessStatusEntity>?
}

@Repository
interface IQaSchemeForSupervisionRepository : HazelcastRepository<QaSchemeForSupervisionEntity, Long> {
    fun findByStatusAndId(status: Int, id: Long): QaSchemeForSupervisionEntity?
    fun findByPermitId(permitId: Long): QaSchemeForSupervisionEntity?
    fun findByPermitRefNumber(permitRefNumber: String): QaSchemeForSupervisionEntity?
}

@Repository
interface IPermitUpdateDetailsRequestsRepository : HazelcastRepository<PermitUpdateDetailsRequestsEntity, Long> {
    fun findByStatusAndId(status: Int, id: Long): PermitUpdateDetailsRequestsEntity?
    fun findByPermitId(permitId: Long): List<PermitUpdateDetailsRequestsEntity>?
    fun findByPermitRefNumber(permitRefNumber: String): List<PermitUpdateDetailsRequestsEntity>?
    fun findByPermitIdAndRequestStatus(permitId: Long, requestStatus: Int): PermitUpdateDetailsRequestsEntity?
    fun findByPermitRefNumberAndRequestStatus(permitRefNumber: String, requestStatus: Int): PermitUpdateDetailsRequestsEntity?
}

@Repository
interface IQaSampleSubmissionRepository : HazelcastRepository<QaSampleSubmissionEntity, Long> {
    fun findByStatusAndId(status: Int, id: Long): QaSampleSubmissionEntity?
    fun findByLabResultsStatus(labResultsStatus: Int): List<QaSampleSubmissionEntity>?
    fun findByLabResultsStatusAndBsNumber(labResultsStatus: Int, bsNumber: String): QaSampleSubmissionEntity?
    fun findByPermitId(permitId: Long): QaSampleSubmissionEntity?
    fun findByPermitRefNumber(permitRefNumber: String): QaSampleSubmissionEntity?
    fun findByCdItemId(cdItemId: Long): QaSampleSubmissionEntity?
    fun findByBsNumber(bsNumber: String): QaSampleSubmissionEntity?
}

@Repository
interface IQaSampleCollectionRepository : HazelcastRepository<QaSampleCollectionEntity, Long> {
    fun findByStatusAndId(status: Int, id: Long): QaSampleCollectionEntity?
    fun findByPermitId(permitId: Long): QaSampleCollectionEntity?
//    fun findByPermitRefNumber(permitId: Long): QaSampleCollectionEntity?
}

@Repository
interface IQaPersonnelInchargeEntityRepository : HazelcastRepository<QaPersonnelInchargeEntity, Long> {
    fun findBySta10Id(sta10Id: Long): List<QaPersonnelInchargeEntity>?
}

@Repository
interface IQaSta3EntityRepository : HazelcastRepository<QaSta3Entity, Long> {
    fun findByStatusAndId(status: Int, id: Long): QaSta3Entity?
    fun findByPermitId(permitId: Long): QaSta3Entity?
    fun findByPermitRefNumber(permitRefNumber: String): QaSta3Entity?
}

@Repository
interface IQaInspectionHaccpImplementationRepository : HazelcastRepository<QaInspectionHaccpImplementationEntity, Long> {
    fun findByStatusAndId(status: Int, id: Long): QaInspectionHaccpImplementationEntity?
    fun findByPermitId(permitId: Long): QaInspectionHaccpImplementationEntity?
    fun findByPermitRefNumber(permitRefNumber: String): QaInspectionHaccpImplementationEntity?
}

@Repository
interface IQaInspectionReportRecommendationRepository : HazelcastRepository<QaInspectionReportRecommendationEntity, Long> {
    fun findByStatusAndId(status: Int, id: Long): QaInspectionReportRecommendationEntity?
    fun findByPermitId(permitId: Long): QaInspectionReportRecommendationEntity?
    fun findByPermitRefNumber(permitRefNumber: String): QaInspectionReportRecommendationEntity?
}

@Repository
interface IQaInspectionOpcEntityRepository : HazelcastRepository<QaInspectionOpcEntity, Long> {
    fun findByStatusAndId(status: Int, id: Long): QaInspectionOpcEntity?
    fun findByPermitId(permitId: Long): List<QaInspectionOpcEntity>?
    fun findByPermitRefNumber(permitRefNumber: String): List<QaInspectionOpcEntity>?
}

@Repository
interface IQaInspectionTechnicalRepository : HazelcastRepository<QaInspectionTechnicalEntity, Long> {
    fun findByStatusAndId(status: Int, id: Long): QaInspectionTechnicalEntity?
    fun findByPermitId(permitId: Long): QaInspectionTechnicalEntity?
    fun findByPermitRefNumber(permitRefNumber: String): QaInspectionTechnicalEntity?
}

@Repository
interface IQaSta10EntityRepository : HazelcastRepository<QaSta10Entity, Long> {
    fun findByStatusAndId(status: Int, id: Long): QaSta10Entity?
    fun findByPermitId(permitId: Long): QaSta10Entity?
    fun findByPermitRefNumber(permitRefNumber: String): QaSta10Entity?
}

@Repository
interface IQaSampleLabTestResultsRepository : HazelcastRepository<QaSampleLabTestResultsEntity, Long> {
    //    fun findByOrderId(orderId: String): QaSampleLabTestResultsEntity?
    fun findByOrderId(orderId: String): List<QaSampleLabTestResultsEntity>?
}

@Repository
interface IQaSampleLabTestParametersRepository : HazelcastRepository<QaSampleLabTestParametersEntity, Long> {
//    fun findByOrderId(orderId: String): QaSampleLabTestParametersEntity?
    fun findByOrderId(orderId: String): List<QaSampleLabTestParametersEntity>?
}

@Repository
interface IQaMachineryRepository : HazelcastRepository<QaMachineryEntity, Long> {
    fun findBySta10Id(sta10Id: Long): List<QaMachineryEntity>?
}


@Repository
interface IQaRawMaterialRepository: HazelcastRepository<QaRawMaterialEntity, Long> {
    fun findBySta10Id(sta10Id: Long): List<QaRawMaterialEntity>?
}

@Repository
interface IQaManufactureProcessRepository: HazelcastRepository<QaManufacturingProcessEntity, Long> {
    fun findBySta10Id(sta10Id: Long): List<QaManufacturingProcessEntity>?
}

@Repository
interface IQaProductBrandEntityRepository: HazelcastRepository<QaProductManufacturedEntity, Long> {
    fun findBySta10Id(sta10Id: Long): List<QaProductManufacturedEntity>?
}


@Repository
interface ITurnOverRatesRepository : HazelcastRepository<TurnOverRatesEntity, Long> {
    fun findByIdAndFirmType(id: Long, firmType: String): TurnOverRatesEntity?
}

@Repository
interface IQaUploadsRepository : HazelcastRepository<QaUploadsEntity, Long> {
    fun findByPermitIdAndOrdinaryStatus(permitId: Long, ordinaryStatus: Int): List<QaUploadsEntity>?
    fun findByPermitRefNumberAndOrdinaryStatus(permitRefNumber: String, ordinaryStatus: Int): List<QaUploadsEntity>?
    fun findByPermitIdAndDocumentType(permitId: Long, docType: String): QaUploadsEntity?
    fun findByPermitRefNumberAndDocumentType(permitRefNumber: String, documentType: String): QaUploadsEntity?
    fun findByPermitIdAndCocStatus(permitId: Long, cocStatus: Int): List<QaUploadsEntity>?
    fun findByPermitRefNumberAndCocStatus(permitRefNumber: String, cocStatus: Int): List<QaUploadsEntity>?
    fun findByPermitIdAndAssessmentReportStatus(permitId: Long, assessmentReportStatus: Int): List<QaUploadsEntity>?
    fun findByPermitRefNumberAndAssessmentReportStatus(permitRefNumber: String, assessmentReportStatus: Int): List<QaUploadsEntity>?
    fun findByPermitIdAndSscStatus(permitId: Long, sscStatus: Int): List<QaUploadsEntity>?
    fun findByPermitRefNumberAndSscStatus(permitRefNumber: String, sscStatus: Int): List<QaUploadsEntity>?
    fun findByPermitIdAndInspectionReportStatus(permitId: Long, inspectionReportStatus: Int): List<QaUploadsEntity>?
    fun findByPermitRefNumberAndInspectionReportStatus(permitRefNumber: String, inspectionReportStatus: Int): List<QaUploadsEntity>?
    fun findByPermitIdAndSta10Status(permitId: Long, sta10Status: Int): List<QaUploadsEntity>?
    fun findByPermitRefNumberAndSta10Status(permitRefNumber: String, sta10Status: Int): List<QaUploadsEntity>?
}

@Repository
interface IQaSmarkFmarkRepository : HazelcastRepository<QaSmarkFmarkEntity, Long> {
    fun findByFmarkId(fmarkID: Long): QaSmarkFmarkEntity?
    fun findByFmarkPermitRefNumber(fmarkPermitRefNumber: String): QaSmarkFmarkEntity?
    fun findBySmarkId(smarkID: Long): QaSmarkFmarkEntity?
    fun findBySmarkPermitRefNumber(smarkPermitRefNumber: String): QaSmarkFmarkEntity?
}

@Repository
interface IQaWorkplanRepository: HazelcastRepository<QaWorkplanEntity, Long> {
    fun findByPermitNumber(permitNumber: String): List<QaWorkplanEntity>?
    fun findByOfficerId(officerId: Long): List<QaWorkplanEntity>?
    fun findByOfficerIdAndRefNumber(officerId: Long, refNumber: String): QaWorkplanEntity?
}

@Repository
interface IQaBatchInvoiceRepository: HazelcastRepository<QaBatchInvoiceEntity, Long> {
    fun findByUserIdAndInvoiceNumber(userId: Long, refNumber: String): QaBatchInvoiceEntity?
    fun findByUserIdAndInvoiceNumberAndPlantId(userId: Long, invoiceNumber: String, plantId: Long): QaBatchInvoiceEntity?
    fun findByUserId(userId: Long): List<QaBatchInvoiceEntity>?
}