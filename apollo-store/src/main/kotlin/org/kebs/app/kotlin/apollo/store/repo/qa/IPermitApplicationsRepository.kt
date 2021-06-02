package org.kebs.app.kotlin.apollo.store.repo.qa

import org.kebs.app.kotlin.apollo.store.model.importer.TemporaryImportApplicationsEntity
import org.kebs.app.kotlin.apollo.store.model.importer.TemporaryImportApplicationsUploadsEntity
import org.kebs.app.kotlin.apollo.store.model.qa.*
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository

@Repository
interface IPermitApplicationsRepository : HazelcastRepository<PermitApplicationsEntity, Long> {
    fun findByUserId(userId: Long): List<PermitApplicationsEntity>?
    fun findByUserIdAndPermitType(userId: Long, permitType: Long): List<PermitApplicationsEntity>?
    fun findByUserIdAndPermitTypeAndOldPermitStatusIsNull(userId: Long, permitType: Long): List<PermitApplicationsEntity>?
    fun findByUserIdAndPermitTypeAndOldPermitStatusIsNullAndPermitAwardStatus(userId: Long, permitType: Long, permitAwardStatus: Int): List<PermitApplicationsEntity>?
    fun findByUserIdAndPermitTypeAndOldPermitStatusIsNullAndPermitAwardStatusAndFmarkGenerated(userId: Long, permitType: Long, permitAwardStatus: Int, fmarkGenerated: Int): List<PermitApplicationsEntity>?
    fun findByQamIdAndPermitType(userId: Long, permitType: Long): List<PermitApplicationsEntity>?
    fun findByQamIdAndPermitTypeAndOldPermitStatusIsNull(userId: Long, permitType: Long): List<PermitApplicationsEntity>?
    fun findByHodIdAndPermitType(userId: Long, permitType: Long): List<PermitApplicationsEntity>?
    fun findByHodIdAndPermitTypeAndOldPermitStatusIsNull(userId: Long, permitType: Long): List<PermitApplicationsEntity>?
    fun findByQaoIdAndPermitType(userId: Long, permitType: Long): List<PermitApplicationsEntity>?
    fun findByQaoIdAndPermitTypeAndOldPermitStatusIsNull(userId: Long, permitType: Long): List<PermitApplicationsEntity>?
    fun findByAssessorIdAndPermitType(userId: Long, permitType: Long): List<PermitApplicationsEntity>?
    fun findByAssessorIdAndPermitTypeAndOldPermitStatusIsNull(userId: Long, permitType: Long): List<PermitApplicationsEntity>?
    fun findByPacSecIdAndPermitTypeAndOldPermitStatusIsNull(userId: Long, permitType: Long): List<PermitApplicationsEntity>?
    fun findByUserIdAndPermitTypeAndEndOfProductionStatus(userId: Long, permitType: Long, endOfProductionStatus: Int): List<PermitApplicationsEntity>?
    fun findByUserIdAndPermitTypeAndEndOfProductionStatusAndOldPermitStatusIsNull(userId: Long, permitType: Long, endOfProductionStatus: Int): List<PermitApplicationsEntity>?
    fun findByIdAndUserIdAndPermitType(id: Long, userId: Long, permitType: Long): PermitApplicationsEntity?
    fun findByPcmIdAndPermitTypeAndOldPermitStatusIsNull(userId: Long, permitType: Long): List<PermitApplicationsEntity>?
    fun findByPscMemberIdAndPermitTypeAndOldPermitStatusIsNull(userId: Long, permitType: Long): List<PermitApplicationsEntity>?
    fun findByIdAndUserId(id: Long, userId: Long): PermitApplicationsEntity?
    fun findByAwardedPermitNumberAndVersionNumber(
        awardedPermitNumber: String,
        versionNumber: Long
    ): PermitApplicationsEntity?

    fun findAllByPaidStatus(paymentStatus: Int): List<PermitApplicationsEntity>?
    fun findByPermitRefNumberAndOldPermitStatus(permitRefNumber: String, oldPermitStatus: Int): List<PermitApplicationsEntity>?
    fun findTopByPermitRefNumberOrderByIdDesc(permitRefNumber: String): PermitApplicationsEntity?
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
}

@Repository
interface IPermitUpdateDetailsRequestsRepository : HazelcastRepository<PermitUpdateDetailsRequestsEntity, Long> {
    fun findByStatusAndId(status: Int, id: Long): PermitUpdateDetailsRequestsEntity?
    fun findByPermitId(permitId: Long): PermitUpdateDetailsRequestsEntity?
    fun findByPermitIdAndRequestStatus(permitId: Long, requestStatus: Int): PermitUpdateDetailsRequestsEntity?
}

@Repository
interface IQaSampleSubmissionRepository : HazelcastRepository<QaSampleSubmissionEntity, Long> {
    fun findByStatusAndId(status: Int, id: Long): QaSampleSubmissionEntity?
    fun findByLabResultsStatus(labResultsStatus: Int): List<QaSampleSubmissionEntity>?
    fun findByPermitId(permitId: Long): QaSampleSubmissionEntity?
    fun findByCdItemId(cdItemId: Long): QaSampleSubmissionEntity?
    fun findByBsNumber(bsNumber: String): QaSampleSubmissionEntity?
}

@Repository
interface IQaSampleCollectionRepository : HazelcastRepository<QaSampleCollectionEntity, Long> {
    fun findByStatusAndId(status: Int, id: Long): QaSampleCollectionEntity?
    fun findByPermitId(permitId: Long): QaSampleCollectionEntity?
}

@Repository
interface IQaSta3EntityRepository : HazelcastRepository<QaSta3Entity, Long> {
    fun findByStatusAndId(status: Int, id: Long): QaSta3Entity?
    fun findByPermitId(permitId: Long): QaSta3Entity?
}

@Repository
interface IQaSta10EntityRepository : HazelcastRepository<QaSta10Entity, Long> {
    fun findByStatusAndId(status: Int, id: Long): QaSta10Entity?
    fun findByPermitId(permitId: Long): QaSta10Entity?
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
    fun findByPermitId(permitId: Long): List<QaUploadsEntity>?
    fun findByPermitIdAndDocumentType(permitId: Long, docType: String): QaUploadsEntity?
}

@Repository
interface IQaSmarkFmarkRepository : HazelcastRepository<QaSmarkFmarkEntity, Long> {
    fun findByFmarkId(fmarkID: Long): QaSmarkFmarkEntity?
    fun findBySmarkId(smarkID: Long): QaSmarkFmarkEntity?
}
