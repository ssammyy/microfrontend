package org.kebs.app.kotlin.apollo.store.repo.qa

import org.kebs.app.kotlin.apollo.store.model.qa.*
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import javax.persistence.NamedStoredProcedureQuery
import javax.persistence.ParameterMode
import javax.persistence.StoredProcedureParameter

@Repository
interface IPermitApplicationsRepository : HazelcastRepository<PermitApplicationsEntity, Long> {
    fun findByUserId(userId: Long): List<PermitApplicationsEntity>?
    fun findByAwardedPermitNumber(awardedPermitNumber: String): PermitApplicationsEntity?
    fun findTopByAwardedPermitNumberOrderByIdDesc(awardedPermitNumber: String): PermitApplicationsEntity?

    fun countByCompanyIdAndPermitAwardStatus(companyId: Long, permitAwardStatus: Int): Long
    fun countByCompanyIdAndPermitAwardStatusAndPermitExpiredStatus(
        companyId: Long,
        permitAwardStatus: Int,
        permitExpiredStatus: Int
    ): Long



    fun findByUserIdAndPermitType(userId: Long, permitType: Long): List<PermitApplicationsEntity>?
    fun findByUserIdAndPermitTypeAndOldPermitStatusIsNullAndPermitAwardStatusIsNotNull(
        userId: Long,
        permitType: Long
    ): List<PermitApplicationsEntity>?

//    fun findByRegioAndOldPermitStatusIsNullAndUserTaskId(
//        companyId: Long,userTaskId: Long
//    ): List<PermitApplicationsEntity>?

    fun findAllByOldPermitStatusIsNullAndUserTaskId(userTaskId: Long): List<PermitApplicationsEntity>?
    fun findAllByOldPermitStatusIsNullAndUserTaskIdAndPermitType(
        userTaskId: Long,
        permitType: Long
    ): List<PermitApplicationsEntity>?

    @Query(
        "SELECT pr.* FROM DAT_KEBS_PERMIT_TRANSACTION pr, DAT_KEBS_MANUFACTURE_PLANT_DETAILS B WHERE pr.ATTACHED_PLANT_ID = B.ID AND pr.PAID_STATUS = :paidStatus AND B.REGION = :region order by pr.ID",
        nativeQuery = true
    )
    fun findRbacPermitByRegionIDPaymentStatus(
        @Param("paidStatus") paidStatus: Int,
        @Param("region") region: Long
    ): List<PermitApplicationsEntity>?

    @Query(
        "SELECT pr.* FROM DAT_KEBS_PERMIT_TRANSACTION pr, DAT_KEBS_MANUFACTURE_PLANT_DETAILS B WHERE pr.ATTACHED_PLANT_ID = B.ID AND pr.PAID_STATUS = :paidStatus AND pr.PERMIT_TYPE = :permitType AND B.REGION = :region order by pr.ID",
        nativeQuery = true
    )
    fun findRbacPermitByRegionIDPaymentStatusAndPermitTypeID(
        @Param("permitType") permitType: Long,
        @Param("paidStatus") paidStatus: Int,
        @Param("region") region: Long
    ): List<PermitApplicationsEntity>?

    @Query(
        "SELECT pr.* FROM DAT_KEBS_PERMIT_TRANSACTION pr, DAT_KEBS_MANUFACTURE_PLANT_DETAILS B WHERE pr.ATTACHED_PLANT_ID = B.ID AND pr.PAID_STATUS = :paidStatus AND pr.PERMIT_TYPE = :permitType AND pr.SECTION_ID = :sectionID AND B.REGION = :region order by pr.ID",
        nativeQuery = true
    )
    fun findRbacPermitByRegionIDPaymentStatusAndPermitTypeIDAndSectionId(
        @Param("permitType") permitType: Long,
        @Param("paidStatus") paidStatus: Int,
        @Param("region") region: Long,
        @Param("sectionID") sectionID: Long
    ): List<PermitApplicationsEntity>?

    @Query(
        "SELECT pr.* FROM DAT_KEBS_PERMIT_TRANSACTION pr, DAT_KEBS_MANUFACTURE_PLANT_DETAILS B WHERE pr.ATTACHED_PLANT_ID = B.ID AND pr.PAID_STATUS = :paidStatus AND pr.PERMIT_AWARD_STATUS = :permitAwardStatus AND pr.PERMIT_TYPE = :permitType AND B.REGION = :region order by pr.ID",
        nativeQuery = true
    )
    fun findRbacPermitByRegionIDPaymentStatusAndPermitTypeIDAndAwardedStatus(
        @Param("permitType") permitType: Long,
        @Param("paidStatus") paidStatus: Int,
        @Param("permitAwardStatus") permitAwardStatus: Int,
        @Param("region") region: Long
    ): List<PermitApplicationsEntity>?

    @Query(
        "SELECT pr.* FROM DAT_KEBS_PERMIT_TRANSACTION pr, DAT_KEBS_MANUFACTURE_PLANT_DETAILS B WHERE pr.ATTACHED_PLANT_ID = B.ID AND pr.PAID_STATUS = :paidStatus AND pr.PERMIT_AWARD_STATUS = :permitAwardStatus AND pr.PERMIT_TYPE = :permitType  AND pr.SECTION_ID = :sectionID AND B.REGION = :region order by pr.ID",
        nativeQuery = true
    )
    fun findRbacPermitByRegionIDPaymentStatusAndPermitTypeIDAndAwardedStatusAndSectionId(
        @Param("permitType") permitType: Long,
        @Param("paidStatus") paidStatus: Int,
        @Param("permitAwardStatus") permitAwardStatus: Int,
        @Param("region") region: Long,
        @Param("sectionID") sectionID: Long
    ): List<PermitApplicationsEntity>?

    @Query(
        "SELECT pr.* FROM DAT_KEBS_PERMIT_TRANSACTION pr, DAT_KEBS_MANUFACTURE_PLANT_DETAILS B WHERE pr.ATTACHED_PLANT_ID = B.ID AND pr.USER_TASK_ID = :userTaskId AND pr.PAID_STATUS = :paidStatus AND pr.OLD_PERMIT_STATUS is null AND B.REGION = :region order by pr.ID",
        nativeQuery = true
    )
    fun findRbacPermitByRegionIDPaymentStatusAndUserTaskID(
        @Param("paidStatus") paidStatus: Int,
        @Param("region") region: Long,
        @Param("userTaskId") userTaskId: Long
    ): List<PermitApplicationsEntity>?

    //    @Query(
//        "SELECT pr.* FROM DAT_KEBS_PERMIT_TRANSACTION pr, DAT_KEBS_MANUFACTURE_PLANT_DETAILS B WHERE " +
//                "pr.ATTACHED_PLANT_ID = B.ID AND pr.USER_TASK_ID = :userTaskId AND pr.PERMIT_TYPE = :permitType " +
//                "AND pr.PAID_STATUS = :paidStatus AND pr.OLD_PERMIT_STATUS = :oldPermitStatus AND B.REGION = :region order by pr.ID",
//        nativeQuery = true
//    )
    @Query(
        "SELECT pr.* FROM DAT_KEBS_PERMIT_TRANSACTION pr, DAT_KEBS_MANUFACTURE_PLANT_DETAILS B WHERE " +
                "pr.ATTACHED_PLANT_ID = B.ID AND pr.USER_TASK_ID = :userTaskId AND pr.PERMIT_TYPE = :permitType " +
                "AND pr.PAID_STATUS = :paidStatus AND pr.OLD_PERMIT_STATUS is null AND B.REGION = :region order by pr.ID",
        nativeQuery = true
    )
    fun findRbacPermitByRegionIDPaymentStatusAndUserTaskIDAndPermitType(
        @Param("permitType") permitType: Long,
        @Param("paidStatus") paidStatus: Int,
        @Param("region") region: Long,
        @Param("userTaskId") userTaskId: Long
    ): List<PermitApplicationsEntity>?

    @Query(
        "SELECT pr.* FROM DAT_KEBS_PERMIT_TRANSACTION pr, DAT_KEBS_MANUFACTURE_PLANT_DETAILS B WHERE " +
                "pr.ATTACHED_PLANT_ID = B.ID AND pr.USER_TASK_ID = :userTaskId AND pr.PERMIT_TYPE = :permitType AND pr.SECTION_ID = :sectionID " +
                "AND pr.PAID_STATUS = :paidStatus AND pr.OLD_PERMIT_STATUS is null AND B.REGION = :region order by pr.ID",
        nativeQuery = true
    )
    fun findRbacPermitByRegionIDPaymentStatusAndUserTaskIDAndPermitTypeAndSectionId(
        @Param("permitType") permitType: Long,
        @Param("paidStatus") paidStatus: Int,
        @Param("region") region: Long,
        @Param("userTaskId") userTaskId: Long,
        @Param("sectionID") sectionID: Long
    ): List<PermitApplicationsEntity>?


    //    @Procedure(procedureName = "proc_load_new_user_permits")
    @Query(value = "{CALL PROC_LOAD_NEW_USER_PERMITS(:VAR_USER_ID,:VAR_PERMIT_NUMBER, :VAR_ATTACHED_PLANT_ID)}", nativeQuery = true)

    fun findByUserIdAndPermitRefNumberAndAttachedPlantId(
        @Param("VAR_USER_ID") userId: Long,
        @Param("VAR_PERMIT_NUMBER") permitNumber: String,
        @Param("VAR_ATTACHED_PLANT_ID") attachedPlantId: Long,

        ):Int

   // @Query(value = "{call sp_findBetween(:min, :max)}", nativeQuery = true)
    //fun findAllBetweenStoredProcedure(@Param("min") min: BigDecimal?, @Param("max") max: BigDecimal?): List<Product?>?


    fun findByUserIdAndPermitTypeAndOldPermitStatusIsNull(
        userId: Long,
        permitType: Long
    ): List<PermitApplicationsEntity>?

    fun findByCompanyIdAndOldPermitStatusIsNull(
        companyID: Long
    ): List<PermitApplicationsEntity>?

    fun findByCompanyIdAndPermitTypeAndOldPermitStatusIsNull(
        companyId: Long, permitType: Long
    ): List<PermitApplicationsEntity>?

    fun findByCompanyIdAndPermitTypeAndPermitAwardStatusAndOldPermitStatusIsNull(
        companyId: Long, permitType: Long, permitAwardStatus: Int
    ): List<PermitApplicationsEntity>?

    fun findByPermitTypeAndPermitAwardStatusAndOldPermitStatusIsNull(
        permitType: Long, permitAwardStatus: Int
    ): List<PermitApplicationsEntity>?

    fun findByPermitTypeAndPaidStatusAndPermitAwardStatusIsNullAndOldPermitStatusIsNull(
        permitType: Long, paidStatus: Int
    ): List<PermitApplicationsEntity>?

    fun findByCompanyIdAndOldPermitStatusIsNullAndUserTaskId(
        companyId: Long, userTaskId: Long
    ): List<PermitApplicationsEntity>?

    fun findByCompanyIdAndOldPermitStatusIsNullAndUserTaskIdAndPermitType(
        companyId: Long, userTaskId: Long, permitType: Long
    ): List<PermitApplicationsEntity>?

    fun findByAttachedPlantIdAndOldPermitStatusIsNull(attachedPlantId: Long): List<PermitApplicationsEntity>?

    fun findByAttachedPlantIdAndPermitTypeAndOldPermitStatusIsNull(
        attachedPlantId: Long,
        permitType: Long
    ): List<PermitApplicationsEntity>?

    fun findByAttachedPlantIdAndOldPermitStatusIsNullAndUserTaskId(
        attachedPlantId: Long, userTaskId: Long
    ): List<PermitApplicationsEntity>?

    fun findByAttachedPlantIdAndOldPermitStatusIsNullAndUserTaskIdAndPermitType(
        attachedPlantId: Long, userTaskId: Long, permitType: Long
    ): List<PermitApplicationsEntity>?

    fun findByUserIdAndPermitTypeAndOldPermitStatusIsNullAndPermitAwardStatus(
        userId: Long,
        permitType: Long,
        permitAwardStatus: Int
    ): List<PermitApplicationsEntity>?

//    fun findByUserIdAndPermitTypeAndOldPermitStatusIsNullAndPermitAwardStatusAndFmarkGenerated(
//        userId: Long, permitType: Long, permitAwardStatus: Int, fmarkGenerated: Int
//    ): List<PermitApplicationsEntity>?

    fun findByUserIdAndPermitTypeAndOldPermitStatusIsNullAndUserTaskId(
        userId: Long,
        permitType: Long,
        userTaskId: Long
    ): List<PermitApplicationsEntity>?

    fun findByUserIdAndOldPermitStatusIsNullAndUserTaskId(
        userId: Long,
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

    fun findByPermitTypeAndEndOfProductionStatusAndPermitAwardStatusAndAttachedPlantIdAndOldPermitStatusIsNull(
        permitType: Long, endOfProductionStatus: Int, permitAwardStatus: Int, attachedPlantId: Long
    ): List<PermitApplicationsEntity>?

    fun findByPermitTypeAndEndOfProductionStatusAndApplicationStatusAndAttachedPlantIdAndOldPermitStatusIsNull(
        permitType: Long, endOfProductionStatus: Int, applicationStatus: Int, attachedPlantId: Long
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
    fun findByIdAndCompanyId(id: Long, companyId: Long): PermitApplicationsEntity?
    fun findByIdAndCompanyIdAndAttachedPlantId(
        id: Long,
        companyId: Long,
        attachedPlantId: Long
    ): PermitApplicationsEntity?

    fun findByAwardedPermitNumberAndVersionNumber(
        awardedPermitNumber: String,
        versionNumber: Long
    ): PermitApplicationsEntity?

    fun findAllByPaidStatus(paymentStatus: Int): List<PermitApplicationsEntity>?
    fun findAllByPaidStatusAndPermitFeeToken(paidStatus: Int, permitFeeToken: String): List<PermitApplicationsEntity>?
    fun findAllByPermitFeeToken(permitFeeToken: String): List<PermitApplicationsEntity>?
    fun findByPermitRefNumberAndOldPermitStatus(
        permitRefNumber: String,
        oldPermitStatus: Int
    ): List<PermitApplicationsEntity>?

    fun findByPermitRefNumber(
        permitRefNumber: String
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
interface IQaInvoiceDetailsRepository : HazelcastRepository<QaInvoiceDetailsEntity, Long> {
    fun findByStatusAndInvoiceMasterId(status: Int, invoiceMasterId: Long): List<QaInvoiceDetailsEntity>?
    fun findByInvoiceMasterId(invoiceMasterId: Long): List<QaInvoiceDetailsEntity>?
//    fun findByProcessStatusNameAndStatus(processStatusName: String, status: Long): QaProcessStatusEntity?
//    fun findByStatus(status: Int): List<QaInvoiceDetailsEntity>?
}

@Repository
interface IQaRemarksEntityRepository : HazelcastRepository<QaRemarksEntity, Long> {
    //    fun findByStatusAndInvoiceMasterId(status: Int, invoiceMasterId: Long): List<QaRemarksEntity>?
    fun findByPermitId(invoiceMasterId: Long): List<QaRemarksEntity>?
//    fun findByProcessStatusNameAndStatus(processStatusName: String, status: Long): QaProcessStatusEntity?
//    fun findByStatus(status: Int): List<QaInvoiceDetailsEntity>?
}

@Repository
interface IQaInvoiceMasterDetailsRepository : HazelcastRepository<QaInvoiceMasterDetailsEntity, Long> {
    fun findByPermitId(permitId: Long): QaInvoiceMasterDetailsEntity?
    fun findAllByBatchInvoiceNo(
        batchInvoiceNo: Long
    ): List<QaInvoiceMasterDetailsEntity>?

    fun findAllByUserIdAndPaymentStatusAndBatchInvoiceNoIsNull(
        userId: Long,
        paymentStatus: Int
    ): List<QaInvoiceMasterDetailsEntity>?

    fun findAllByUserId(userId: Long): List<QaInvoiceMasterDetailsEntity>?
    fun findByPermitRefNumberAndUserIdAndPermitId(
        permitRefNumber: String,
        userId: Long,
        permitId: Long
    ): QaInvoiceMasterDetailsEntity?
//    fun findByStatus(status: Int): List<QaInvoiceMasterDetailsEntity>?
}

@Repository
interface IQaSchemeForSupervisionRepository : HazelcastRepository<QaSchemeForSupervisionEntity, Long> {
    fun findByStatusAndId(status: Int, id: Long): QaSchemeForSupervisionEntity?
    fun findByPermitId(permitId: Long): QaSchemeForSupervisionEntity?
    fun findTopByPermitRefNumberOrderByIdDesc(permitRefNumber: String): QaSchemeForSupervisionEntity?
    fun findByPermitRefNumberAndPermitId(permitRefNumber: String, permitId: Long): QaSchemeForSupervisionEntity?
}

@Repository
interface IPermitUpdateDetailsRequestsRepository : HazelcastRepository<PermitUpdateDetailsRequestsEntity, Long> {
    fun findByStatusAndId(status: Int, id: Long): PermitUpdateDetailsRequestsEntity?
    fun findByPermitId(permitId: Long): List<PermitUpdateDetailsRequestsEntity>?

    //    fun findTopByPermitRefNumberOrderByIdDesc(permitRefNumber: String): List<PermitUpdateDetailsRequestsEntity>?
    fun findByPermitRefNumberAndPermitId(
        permitRefNumber: String,
        permitId: Long
    ): List<PermitUpdateDetailsRequestsEntity>?
//    fun findByPermitIdAndRequestStatus(permitId: Long, requestStatus: Int): PermitUpdateDetailsRequestsEntity?
//    fun findByPermitRefNumberAndRequestStatus(permitRefNumber: String, requestStatus: Int): PermitUpdateDetailsRequestsEntity?
}

@Repository
interface IQaSampleSubmissionRepository : HazelcastRepository<QaSampleSubmissionEntity, Long> {
    fun findByStatusAndId(status: Int, id: Long): QaSampleSubmissionEntity?
    fun findByLabResultsStatus(labResultsStatus: Int): List<QaSampleSubmissionEntity>?
    fun findByLabResultsStatusAndBsNumber(labResultsStatus: Int, bsNumber: String): QaSampleSubmissionEntity?
    fun findByPermitId(permitId: Long): QaSampleSubmissionEntity?
    fun findTopByPermitRefNumberOrderByIdDesc(permitRefNumber: String): QaSampleSubmissionEntity?

    //    fun findTopByPermitRefNumberOrderByIdDescAndStatus(permitRefNumber: String, status: Int): List<QaSampleSubmissionEntity>?
    fun findByPermitRefNumberAndStatusAndPermitId(
        permitRefNumber: String,
        status: Int,
        permitId: Long
    ): List<QaSampleSubmissionEntity>?

    fun findByCdItemId(cdItemId: Long): QaSampleSubmissionEntity?
    fun findByBsNumber(bsNumber: String): QaSampleSubmissionEntity?
}

@Repository
interface IQaSampleSubmittedPdfListRepository : HazelcastRepository<QaSampleSubmittedPdfListDetailsEntity, Long> {
    fun findByStatusAndId(status: Int, id: Long): QaSampleSubmittedPdfListDetailsEntity?
    fun findBySffId(sffId: Long): List<QaSampleSubmittedPdfListDetailsEntity>?
    fun findBySffIdAndSentToManufacturerStatus(
        sffId: Long,
        sentToManufacturerStatus: Int
    ): List<QaSampleSubmittedPdfListDetailsEntity>?
//    fun findByPermitRefNumber(permitId: Long): QaSampleCollectionEntity?
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
    fun findTopByPermitRefNumberOrderByIdDesc(permitRefNumber: String): QaSta3Entity?
    fun findByPermitRefNumberAndPermitId(permitRefNumber: String, permitId: Long): QaSta3Entity?
}

@Repository
interface IQaInspectionHaccpImplementationRepository : HazelcastRepository<QaInspectionHaccpImplementationEntity, Long> {
    fun findByStatusAndId(status: Int, id: Long): QaInspectionHaccpImplementationEntity?
    fun findByPermitId(permitId: Long): QaInspectionHaccpImplementationEntity?
    fun findByInspectionRecommendationId(inspectionRecommendationId: Long): QaInspectionHaccpImplementationEntity?
    fun findTopByPermitRefNumberOrderByIdDesc(permitRefNumber: String): QaInspectionHaccpImplementationEntity?
    fun findByPermitRefNumberAndPermitId(
        permitRefNumber: String,
        permitId: Long
    ): QaInspectionHaccpImplementationEntity?
}

@Repository
interface IQaInspectionReportRecommendationRepository : HazelcastRepository<QaInspectionReportRecommendationEntity, Long> {
    fun findByStatusAndId(status: Int, id: Long): QaInspectionReportRecommendationEntity?
    fun findByPermitId(permitId: Long): List<QaInspectionReportRecommendationEntity>?
    fun findTopByPermitRefNumberOrderByIdDesc(permitRefNumber: String): QaInspectionReportRecommendationEntity?
    fun findByPermitRefNumberAndPermitId(
        permitRefNumber: String,
        permitId: Long
    ): List<QaInspectionReportRecommendationEntity>?
}

@Repository
interface IQaInspectionOpcEntityRepository : HazelcastRepository<QaInspectionOpcEntity, Long> {
    fun findByStatusAndId(status: Int, id: Long): QaInspectionOpcEntity?
    fun findByPermitId(permitId: Long): List<QaInspectionOpcEntity>?
    fun findByInspectionRecommendationId(inspectionRecommendationId: Long): List<QaInspectionOpcEntity>?
    fun findTopByPermitRefNumberOrderByIdDesc(permitRefNumber: String): List<QaInspectionOpcEntity>?
    fun findByPermitRefNumberAndPermitId(permitRefNumber: String, permitId: Long): List<QaInspectionOpcEntity>?
}

@Repository
interface IQaInspectionTechnicalRepository : HazelcastRepository<QaInspectionTechnicalEntity, Long> {
    fun findByStatusAndId(status: Int, id: Long): QaInspectionTechnicalEntity?
    fun findByPermitId(permitId: Long): QaInspectionTechnicalEntity?
    fun findByInspectionRecommendationId(inspectionRecommendationId: Long): QaInspectionTechnicalEntity?
    fun findTopByPermitRefNumberOrderByIdDesc(permitRefNumber: String): QaInspectionTechnicalEntity?
    fun findByPermitRefNumberAndPermitId(permitRefNumber: String, permitId: Long): QaInspectionTechnicalEntity?
}

@Repository
interface IQaSta10EntityRepository : HazelcastRepository<QaSta10Entity, Long> {
    fun findByStatusAndId(status: Int, id: Long): QaSta10Entity?
    fun findByPermitId(permitId: Long): QaSta10Entity?
    fun findTopByPermitRefNumberOrderByIdDesc(permitRefNumber: String): QaSta10Entity?
    fun findByPermitRefNumberAndPermitId(permitRefNumber: String, permitId: Long): QaSta10Entity?
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
interface IQaProductBrandEntityRepository : HazelcastRepository<QaProductManufacturedEntity, Long> {
    fun findBySta10Id(sta10Id: Long): List<QaProductManufacturedEntity>?
}


@Repository
interface IPermitRatingRepository : HazelcastRepository<PermitRatingEntity, Long> {
    fun findByIdAndFirmType(id: Long, firmType: String): PermitRatingEntity?
    fun findAllByStatus(status: Int): List<PermitRatingEntity>?
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
    fun findByPermitRefNumberAndAssessmentReportStatus(
        permitRefNumber: String,
        assessmentReportStatus: Int
    ): List<QaUploadsEntity>?

    fun findByPermitIdAndSscStatus(permitId: Long, sscStatus: Int): List<QaUploadsEntity>?
    fun findByPermitRefNumberAndSscStatus(permitRefNumber: String, sscStatus: Int): List<QaUploadsEntity>?
    fun findByPermitRefNumberAndJustificationReportStatusAndPermitId(
        permitRefNumber: String,
        justificationReportStatus: Int,
        permitId: Long
    ): List<QaUploadsEntity>?

    fun findByPermitIdAndInspectionReportStatus(permitId: Long, inspectionReportStatus: Int): List<QaUploadsEntity>?
    fun findByPermitRefNumberAndInspectionReportStatus(
        permitRefNumber: String,
        inspectionReportStatus: Int
    ): List<QaUploadsEntity>?

    fun findByInspectionReportIdAndInspectionReportStatus(
        inspectionReportId: Long,
        inspectionReportStatus: Int
    ): List<QaUploadsEntity>?

    fun findByPermitIdAndSta10Status(permitId: Long, sta10Status: Int): List<QaUploadsEntity>?
    fun findByPermitIdAndSta3Status(permitId: Long, sta10Status: Int): List<QaUploadsEntity>?
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
