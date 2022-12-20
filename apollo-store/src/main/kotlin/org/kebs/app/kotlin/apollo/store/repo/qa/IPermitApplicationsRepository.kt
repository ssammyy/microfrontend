package org.kebs.app.kotlin.apollo.store.repo.qa

import org.jetbrains.annotations.Nullable
import org.kebs.app.kotlin.apollo.store.model.qa.*
import org.kebs.app.kotlin.apollo.store.model.std.SampleSubmissionDTO
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
interface IPermitApplicationsRepository : HazelcastRepository<PermitApplicationsEntity, Long> {
    fun findByUserIdAndVarField9IsNull(userId: Long): List<PermitApplicationsEntity>?
    fun findByAwardedPermitNumber(awardedPermitNumber: String):  List<PermitApplicationsEntity>?
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

    @Query(
        value = "SELECT max(ch.PERMIT_REF_NUMBER) FROM DAT_KEBS_PERMIT_TRANSACTION ch",
        nativeQuery = true
    )
    fun getMaxId(): String?

    fun findAllByOldPermitStatusIsNullAndUserTaskId(userTaskId: Long): List<PermitApplicationsEntity>?
    fun findAllByOldPermitStatusIsNullAndUserTaskIdAndPermitType(
        userTaskId: Long,
        permitType: Long
    ): List<PermitApplicationsEntity>?


    fun findAllByOldPermitStatusIsNullAndUserTaskIdAndPermitTypeAndPermitStatus(
        userTaskId: Long,
        permitType: Long,
        permitStatus: Long

    ): List<PermitApplicationsEntity>?

    @Query(
        value = "{ CALL PROC_MIGRATE_NEW_USER_PERMITS(:VAR_USER_ID,:VAR_PERMIT_NUMBER, :VAR_ATTACHED_PLANT_ID ) }",
        nativeQuery = true
    )
    fun migratePermitsToNewUser(
        @Param("VAR_USER_ID") userId: Long,
        @Param("VAR_PERMIT_NUMBER") permitNumber: String,
        @Param("VAR_ATTACHED_PLANT_ID") plantId: Long
    ): String


    @Query(
        value = "UPDATE APOLLO.DAT_KEBS_PERMIT_TRANSACTION  t1\n" +
                "SET(t1.ENABLED,t1.PRODUCT_STANDARD, t1.STA10_FILLED_STATUS,t1.VERSION_NUMBER,t1.VAR_FIELD_7)=\n" +
                "    (\n" +
                "        SELECT t2.ENABLED,t2.PRODUCT_STANDARD, t2.STA10_FILLED_STATUS,t2.VERSION_NUMBER,5\n" +
                "        FROM APOLLO.DAT_KEBS_PERMIT_TRANSACTION  t2\n" +
                "        WHERE  t2.ID=:permit\n" +
                "\n" +
                "        )\n" +
                "WHERE t1.ID=:permitBeingUpdated",
        nativeQuery = true
    )
    fun updatePermitToNew(
        @Param("permit") permit: Long,
        @Param("permitBeingUpdated") permitBeingUpdated: Long
    ): String

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
    @Query(
        value = "{CALL PROC_LOAD_NEW_USER_PERMITS(:VAR_USER_ID,:VAR_PERMIT_NUMBER, :VAR_ATTACHED_PLANT_ID)}",
        nativeQuery = true
    )

    fun findByUserIdAndPermitRefNumberAndAttachedPlantId(
        @Param("VAR_USER_ID") userId: Long,
        @Param("VAR_PERMIT_NUMBER") permitNumber: String,
        @Param("VAR_ATTACHED_PLANT_ID") attachedPlantId: Long,

        ): Int

    // @Query(value = "{call sp_findBetween(:min, :max)}", nativeQuery = true)
    //fun findAllBetweenStoredProcedure(@Param("min") min: BigDecimal?, @Param("max") max: BigDecimal?): List<Product?>?


    fun findByUserIdAndPermitTypeAndOldPermitStatusIsNull(
        userId: Long,
        permitType: Long
    ): List<PermitApplicationsEntity>?

    fun findByCompanyIdAndOldPermitStatusIsNull(
        companyID: Long
    ): List<PermitApplicationsEntity>?

    fun findByCompanyIdAndPermitTypeAndOldPermitStatusIsNullAndVarField9IsNull(
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

    fun findByUserIdAndPermitTypeAndOldPermitStatusIsNullAndPermitAwardStatusAndVersionNumberIsNotNull(
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

    fun findByUserIdAndPermitTypeAndOldPermitStatusIsNullAndPaidStatus(
        userId: Long,
        permitType: Long,
        paidStatus: Int,
    ): List<PermitApplicationsEntity>?

    fun findByPermitTypeAndPaidStatusIsNotNull(
        permitType: Long
    ): List<PermitApplicationsEntity>?

    fun findByPermitTypeAndOldPermitStatusIsNullAndPermitAwardStatus(
        permitType: Long,
        permitAwardStatus: Int,
    ): List<PermitApplicationsEntity>?



    fun findByPermitTypeAndOldPermitStatusIsNotNullAndPermitAwardStatus(
        permitType: Long,
        permitAwardStatus: Int,
    ): List<PermitApplicationsEntity>?

    fun findByPermitTypeAndPermitAwardStatusIsNull(
        permitType: Long,
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

    fun findByPermitTypeAndOldPermitStatusIsNull(
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

    fun findByPermitTypeAndOldPermitStatusIsNullAndPermitAwardStatusIsNotNull(
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

    fun findTopByPermitRefNumberOrderByIdAsc(permitRefNumber: String): PermitApplicationsEntity?
    fun findByIdAndAttachedPlantId(id: Long, attachedPlantId: Long): PermitApplicationsEntity?

    @Transactional
    @Modifying
    @Query(
        value = "UPDATE  APOLLO.DAT_KEBS_PERMIT_TRANSACTION t1 SET t1.VAR_FIELD_9='1' WHERE t1.ID =:permitID ",
        nativeQuery = true
    )
    fun deletePermit(@Param("permitID") permitID: Long)

    @Query(
        value = "SELECT a.* from APOLLO.DAT_KEBS_PERMIT_TRANSACTION a  inner join DAT_KEBS_COMPANY_PROFILE b on a.COMPANY_ID = b.ID where (:startDate is null or a.CREATED_ON >=TO_DATE(:startDate)) and (:endDate is null or a.CREATED_ON <=TO_DATE(:endDate)) and (:regionId is null or b.REGION =TO_NUMBER(:regionId)) and (:sectionId is null or a.SECTION_ID =TO_NUMBER(:sectionId)) and (:permitStatus is null or a.PERMIT_STATUS =TO_NUMBER(:permitStatus)) and(:officerId is null or a.HOF_ID=TO_NUMBER(:officerId)) and(:firmCategory is null or b.FIRM_CATEGORY =TO_NUMBER(:firmCategory)) and(:permitType is null or PERMIT_TYPE =TO_NUMBER(:permitType)) and(:productDescription is null or PRODUCT_NAME like '%'||:productDescription||'%') and PAID_STATUS is not null",
        nativeQuery = true
    )
    fun findFilteredPermits(
        @Param("startDate") startDate: Date?,
        @Param("endDate") endDate: Date?,
        @Param("regionId") regionId: Long?,
        @Param("sectionId") sectionId: Long?,
        @Param("permitStatus") permitStatus: Long?,
        @Param("officerId") officerId: Long?,
        @Param("firmCategory") firmCategory: Long?,
        @Param("permitType") permitType: Long?,
        @Param("productDescription") productDescription: String?
    ): List<PermitApplicationsEntity>?


    @Query(
        value = "SELECT a.* from APOLLO.DAT_KEBS_PERMIT_TRANSACTION a  inner join DAT_KEBS_COMPANY_PROFILE b on a.COMPANY_ID = b.ID where (:startDate is null or a.CREATED_ON >=TO_DATE(:startDate)) and (:endDate is null or a.CREATED_ON <=TO_DATE(:endDate)) and (:regionId is null or b.REGION =TO_NUMBER(:regionId)) and (:sectionId is null or a.SECTION_ID =TO_NUMBER(:sectionId)) and (:permitStatus is null or a.PERMIT_STATUS =TO_NUMBER(:permitStatus)) and(:officerId is null or a.HOF_ID=TO_NUMBER(:officerId)) and(:firmCategory is null or b.FIRM_CATEGORY =TO_NUMBER(:firmCategory)) and(:permitType is null or PERMIT_TYPE =TO_NUMBER(:permitType)) and(:productDescription is null or PRODUCT_NAME like '%'||:productDescription||'%') and OLD_PERMIT_STATUS is null and PERMIT_AWARD_STATUS=1",
        nativeQuery = true
    )
    fun findFilteredAwardedPermits(
        @Param("startDate") startDate: Date?,
        @Param("endDate") endDate: Date?,
        @Param("regionId") regionId: Long?,
        @Param("sectionId") sectionId: Long?,
        @Param("permitStatus") permitStatus: Long?,
        @Param("officerId") officerId: Long?,
        @Param("firmCategory") firmCategory: Long?,
        @Param("permitType") permitType: Long?,
        @Param("productDescription") productDescription: String?
    ): List<PermitApplicationsEntity>?


    @Query(
        value = "SELECT a.* from APOLLO.DAT_KEBS_PERMIT_TRANSACTION a  inner join DAT_KEBS_COMPANY_PROFILE b on a.COMPANY_ID = b.ID where (:startDate is null or a.CREATED_ON >=TO_DATE(:startDate)) and (:endDate is null or a.CREATED_ON <=TO_DATE(:endDate)) and (:regionId is null or b.REGION =TO_NUMBER(:regionId)) and (:sectionId is null or a.SECTION_ID =TO_NUMBER(:sectionId)) and (:permitStatus is null or a.PERMIT_STATUS =TO_NUMBER(:permitStatus)) and(:officerId is null or a.HOF_ID=TO_NUMBER(:officerId)) and(:firmCategory is null or b.FIRM_CATEGORY =TO_NUMBER(:firmCategory)) and(:permitType is null or PERMIT_TYPE =TO_NUMBER(:permitType)) and(:productDescription is null or PRODUCT_NAME like '%'||:productDescription||'%') and OLD_PERMIT_STATUS is not null and PERMIT_AWARD_STATUS=1",
        nativeQuery = true
    )
    fun findFilteredRenewedPermits(
        @Param("startDate") startDate: Date?,
        @Param("endDate") endDate: Date?,
        @Param("regionId") regionId: Long?,
        @Param("sectionId") sectionId: Long?,
        @Param("permitStatus") permitStatus: Long?,
        @Param("officerId") officerId: Long?,
        @Param("firmCategory") firmCategory: Long?,
        @Param("permitType") permitType: Long?,
        @Param("productDescription") productDescription: String?
    ): List<PermitApplicationsEntity>?

    @Query(
        value = "SELECT a.* from APOLLO.DAT_KEBS_PERMIT_TRANSACTION a  inner join DAT_KEBS_COMPANY_PROFILE b on a.COMPANY_ID = b.ID where (:startDate is null or a.CREATED_ON >=TO_DATE(:startDate)) and (:endDate is null or a.CREATED_ON <=TO_DATE(:endDate)) and (:regionId is null or b.REGION =TO_NUMBER(:regionId)) and (:sectionId is null or a.SECTION_ID =TO_NUMBER(:sectionId)) and (:permitStatus is null or a.PERMIT_STATUS =TO_NUMBER(:permitStatus)) and(:officerId is null or a.HOF_ID=TO_NUMBER(:officerId)) and(:firmCategory is null or b.FIRM_CATEGORY =TO_NUMBER(:firmCategory)) and(:permitType is null or PERMIT_TYPE =TO_NUMBER(:permitType)) and(:productDescription is null or PRODUCT_NAME like '%'||:productDescription||'%') and PERMIT_AWARD_STATUS is  null",
        nativeQuery = true
    )
    fun findFilteredDejectedPermits(
        @Param("startDate") startDate: Date?,
        @Param("endDate") endDate: Date?,
        @Param("regionId") regionId: Long?,
        @Param("sectionId") sectionId: Long?,
        @Param("permitStatus") permitStatus: Long?,
        @Param("officerId") officerId: Long?,
        @Param("firmCategory") firmCategory: Long?,
        @Param("permitType") permitType: Long?,
        @Param("productDescription") productDescription: String?
    ): List<PermitApplicationsEntity>?


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
interface IQaAwardedPermitTrackerEntityRepository : HazelcastRepository<QaAwardedPermitTrackerEntity, Long> {
    @Query(
        value = "SELECT max(ch.AWARDED_PERMIT_NUMBER) FROM DAT_KEBS_AWARDED_PERMIT_TRACKER ch",
        nativeQuery = true
    )
    fun getMaxId(): Long?
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
    fun findFirstByPermitIdAndProcessByAndRemarksStatus(permitId: Long, processBy: String, remarksStatus:Int): QaRemarksEntity?
}

@Repository
interface IQaInvoiceMasterDetailsRepository : HazelcastRepository<QaInvoiceMasterDetailsEntity, Long> {
    fun findByPermitId(permitId: Long): QaInvoiceMasterDetailsEntity?
    fun findAllByBatchInvoiceNo(
        batchInvoiceNo: Long
    ): List<QaInvoiceMasterDetailsEntity>?

    fun findByBatchInvoiceNo(
        batchInvoiceNo: Long
    ): QaInvoiceMasterDetailsEntity?

    fun findAllByUserIdAndPaymentStatusAndBatchInvoiceNoIsNull(
        userId: Long,
        paymentStatus: Int
    ): List<QaInvoiceMasterDetailsEntity>?


    fun findAllByUserIdAndReceiptNoIsNotNull(userId: Long): List<QaInvoiceMasterDetailsEntity>?

    fun findAllByUserIdAndVarField1IsNull(userId: Long): List<QaInvoiceMasterDetailsEntity>?
    fun findByPermitRefNumberAndUserIdAndPermitId(
        permitRefNumber: String,
        userId: Long,
        permitId: Long
    ): QaInvoiceMasterDetailsEntity?
//    fun findByStatus(status: Int): List<QaInvoiceMasterDetailsEntity>?

    @Transactional
    @Modifying
    @Query(
        value = "UPDATE  APOLLO.DAT_KEBS_QA_INVOICE_MASTER_DETAILS t1 SET t1.VAR_FIELD_9='1' WHERE t1.ID =:invoiceId ",
        nativeQuery = true
    )
    fun deleteInvoice(@Param("invoiceId") invoiceId: Long)


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
    fun findByLabResultsStatusAndBsNumberIsNotNull(labResultsStatus: Int): List<QaSampleSubmissionEntity>
    fun findByLabResultsStatusAndBsNumber(labResultsStatus: Int, bsNumber: String): QaSampleSubmissionEntity?
    fun findByPermitId(permitId: Long): QaSampleSubmissionEntity?
    fun findTopByPermitRefNumberOrderByIdDesc(permitRefNumber: String): QaSampleSubmissionEntity?

    @Query(
        "SELECT u.SSF_SUBMISSION_DATE,DKPT.FIRM_NAME,DKPT.PHYSICAL_ADDRESS,DKPT.TELEPHONE_NO,DKPT.REGION, sns.SECTION, DKPT.PRODUCT, u.BRAND_NAME, SS.STANDARD_TITLE, u.COMPLIANCE_REMARKS,dkpt.COMPLIANT_STATUS, DKPT.INSPECTION_DATE,u.RESULTS_DATE FROM DAT_KEBS_QA_SAMPLE_SUBMISSION u inner join APOLLO.DAT_KEBS_PERMIT_TRANSACTION DKPT on u.PERMIT_ID = DKPT.ID inner join CFG_SAMPLE_STANDARDS Ss on DKPT.PRODUCT_STANDARD = Ss.ID inner join CFG_KEBS_SECTIONS Sns on DKPT.SECTION_ID = Sns.ID  WHERE u.PERMIT_ID!=0 AND DKPT.PERMIT_TYPE= :permitType",
        nativeQuery = true
    )
    fun findSamplesSubmitted(
        @Param("permitType") permitType: Long,

        ): MutableList<SampleSubmissionDTO>?


    //    fun findTopByPermitRefNumberOrderByIdDescAndStatus(permitRefNumber: String, status: Int): List<QaSampleSubmissionEntity>?
    fun findByPermitRefNumberAndStatusAndPermitId(
        permitRefNumber: String,
        status: Int,
        permitId: Long
    ): List<QaSampleSubmissionEntity>?

    fun findByCdItemId(cdItemId: Long): QaSampleSubmissionEntity?
    fun findByFuelInspectionId(fuelInspectionId: Long): QaSampleSubmissionEntity?
    fun findByFuelInspectionIdAndBsNumber(fuelInspectionId: Long, bsNumber: String): QaSampleSubmissionEntity?
    fun findByWorkplanGeneratedId(workPlanInspectionID: Long): QaSampleSubmissionEntity?
    fun findByWorkplanGeneratedIdAndBsNumber(workPlanInspectionID: Long, bsNumber: String): QaSampleSubmissionEntity?
    fun findByBsNumber(bsNumber: String): QaSampleSubmissionEntity?
}

@Repository
interface IQaSampleSubmittedPdfListRepository : HazelcastRepository<QaSampleSubmittedPdfListDetailsEntity, Long> {
    fun findByStatusAndId(status: Int, id: Long): QaSampleSubmittedPdfListDetailsEntity?
    fun findBySffId(sffId: Long): List<QaSampleSubmittedPdfListDetailsEntity>?
    fun findBySffIdAndSsfFileStatus(sffId: Long, ssfFileStatus: String): List<QaSampleSubmittedPdfListDetailsEntity>?
    fun findBySffIdAndSentToManufacturerStatus(
        sffId: Long,
        sentToManufacturerStatus: Int
    ): List<QaSampleSubmittedPdfListDetailsEntity>?

    fun findBySffIdAndSentToManufacturerStatusAndSsfFileStatus(
        sffId: Long, sentToManufacturerStatus: Int, ssfFileStatus: String
    ): List<QaSampleSubmittedPdfListDetailsEntity>?
//    fun findByPermitRefNumber(permitId: Long): QaSampleCollectionEntity?
}

@Repository
interface IQaSampleCollectionRepository : HazelcastRepository<QaSampleCollectionEntity, Long> {
    fun findByStatusAndId(status: Int, id: Long): QaSampleCollectionEntity?
    fun findByPermitId(permitId: Long): QaSampleCollectionEntity?
    fun findByItemId(itemId: Long): QaSampleCollectionEntity?
//    fun findByPermitRefNumber(permitId: Long): QaSampleCollectionEntity?
}

@Repository
interface IQaSampleCollectionLaboratoryRequestsRepository : HazelcastRepository<QaSCFLaboratoryRequestsEntity, Long> {
    fun findByStatusAndId(status: Int, id: Long): QaSCFLaboratoryRequestsEntity?
    fun findBySsfId(ssfId: Long): List<QaSCFLaboratoryRequestsEntity>
}

@Repository
interface IQaPersonnelInchargeEntityRepository : HazelcastRepository<QaPersonnelInchargeEntity, Long> {
    fun findBySta10Id(sta10Id: Long): List<QaPersonnelInchargeEntity>?


    @Query(
        value = "INSERT INTO DAT_KEBS_QA_PERSONNEL_INCHARGE t1\n" +
                "(\n" +
                " t1.PERSONNEL_NAME,t1.QUALIFICATION_INSTITUTION,t1.DATE_OF_EMPLOYMENT,t1.STATUS,t1.STA10_ID\n" +
                ")\n" +
                "SELECT   t2.PERSONNEL_NAME,t2.QUALIFICATION_INSTITUTION,t2.DATE_OF_EMPLOYMENT,t2.STATUS,:sta10IdToBeUpdated\n" +
                "FROM DAT_KEBS_QA_PERSONNEL_INCHARGE t2 WHERE t2.STA10_ID =:sta10Id", nativeQuery = true
    )
    fun updatePersonnel(
        @Param("sta10Id") sta10Id: Long,
        @Param("sta10IdToBeUpdated") sta10IdToBeUpdated: Long
    ): String


}

@Repository
interface IQaSta3EntityRepository : HazelcastRepository<QaSta3Entity, Long> {
    fun findByStatusAndId(status: Int, id: Long): QaSta3Entity?
    fun findByPermitId(permitId: Long): QaSta3Entity?
    fun findTopByPermitRefNumberOrderByIdDesc(permitRefNumber: String): QaSta3Entity?
    fun findByPermitRefNumberAndPermitId(permitRefNumber: String, permitId: Long): QaSta3Entity?
}

@Repository
interface IQaInspectionHaccpImplementationRepository :
    HazelcastRepository<QaInspectionHaccpImplementationEntity, Long> {
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
interface IQaInspectionReportRecommendationRepository :
    HazelcastRepository<QaInspectionReportRecommendationEntity, Long> {
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

    @Query(
        value = "UPDATE APOLLO.DAT_KEBS_QA_STA10  t1\n" +
                "SET(t1.TOTAL_NUMBER_PERSONNEL,\n" +
                "    t1.TOTAL_NUMBER_FEMALE,\n" +
                "    t1.TOTAL_NUMBER_MALE,\n" +
                "    t1.TOTAL_NUMBER_PERMANENT_EMPLOYEES,\n" +
                "   t1.TOTAL_NUMBER_CASUAL_EMPLOYEES,\n" +
                "   t1.AVERAGE_VOLUME_PRODUCTION_MONTH,\n" +
                "   t1.HANDLED_MANUFACTURING_PROCESS_RAW_MATERIALS,\n" +
                "   t1.HANDLED_MANUFACTURING_PROCESS_INPROCESS_PRODUCTS,\n" +
                "   t1.HANDLED_MANUFACTURING_PROCESS_FINAL_PRODUCT,\n" +
                "   t1.STRATEGY_INPLACE_RECALLING_PRODUCTS,\n" +
                "   t1.STATE_FACILITY_CONDITIONS_RAW_MATERIALS,\n" +
                "   t1.STATE_FACILITY_CONDITIONS_END_PRODUCT,\n" +
                "   t1.TESTING_FACILITIES_EXIST_SPECIFY_EQUIPMENT,\n" +
                "   t1.TESTING_FACILITIES_EXIST_STATE_PARAMETERS_TESTED,\n" +
                "   t1.TESTING_FACILITIES_SPECIFY_PARAMETERS_TESTED,\n" +
                "   t1.CALIBRATION_EQUIPMENT_LAST_CALIBRATED,\n" +
                "   t1.HANDLING_CONSUMER_COMPLAINTS,\n" +
                "   t1.COMPANY_REPRESENTATIVE,\n" +
                "   t1.STATUS)=\n" +
                "       (\n" +
                "           SELECT\n" +
                "               t2.TOTAL_NUMBER_PERSONNEL,\n" +
                "               t2.TOTAL_NUMBER_FEMALE,\n" +
                "               t2.TOTAL_NUMBER_MALE,\n" +
                "               t2.TOTAL_NUMBER_PERMANENT_EMPLOYEES,\n" +
                "               t2.TOTAL_NUMBER_CASUAL_EMPLOYEES,\n" +
                "               t2.AVERAGE_VOLUME_PRODUCTION_MONTH,\n" +
                "               t2.HANDLED_MANUFACTURING_PROCESS_RAW_MATERIALS,\n" +
                "               t2.HANDLED_MANUFACTURING_PROCESS_INPROCESS_PRODUCTS,\n" +
                "               t2.HANDLED_MANUFACTURING_PROCESS_FINAL_PRODUCT,\n" +
                "               t2.STRATEGY_INPLACE_RECALLING_PRODUCTS,\n" +
                "               t2.STATE_FACILITY_CONDITIONS_RAW_MATERIALS,\n" +
                "               t2.STATE_FACILITY_CONDITIONS_END_PRODUCT,\n" +
                "               t2.TESTING_FACILITIES_EXIST_SPECIFY_EQUIPMENT,\n" +
                "               t2.TESTING_FACILITIES_EXIST_STATE_PARAMETERS_TESTED,\n" +
                "               t2.TESTING_FACILITIES_SPECIFY_PARAMETERS_TESTED,\n" +
                "               t2.CALIBRATION_EQUIPMENT_LAST_CALIBRATED,\n" +
                "               t2.HANDLING_CONSUMER_COMPLAINTS,\n" +
                "               t2.COMPANY_REPRESENTATIVE,\n" +
                "               t2.STATUS\n" +
                "           FROM APOLLO.DAT_KEBS_QA_STA10  t2\n" +
                "           WHERE  t2.PERMIT_ID=:permit\n" +
                "\n" +
                "       )\n" +
                "WHERE t1.PERMIT_ID=:permitBeingUpdated",
        nativeQuery = true
    )

    fun updatePermitWithSta10Data(
        @Param("permit") permit: Long,
        @Param("permitBeingUpdated") permitBeingUpdated: Long
    ): String


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

    @Query(
        value = "INSERT INTO DAT_KEBS_QA_MACHINE t1\n" +
                "(\n" +
                " t1.COUNTRY_OF_ORIGIN,t1.MACHINE_NAME,t1.TYPE_MODEL,t1.STATUS,t1.STA10_ID\n" +
                ")\n" +
                "SELECT   t2.COUNTRY_OF_ORIGIN,t2.MACHINE_NAME,t2.TYPE_MODEL,t2.STATUS,:sta10IdToBeUpdated\n" +
                "FROM DAT_KEBS_QA_MACHINE t2 WHERE t2.STA10_ID =:sta10Id", nativeQuery = true
    )
    fun updateMachinery(
        @Param("sta10Id") sta10Id: Long,
        @Param("sta10IdToBeUpdated") sta10IdToBeUpdated: Long
    ): String

}


@Repository
interface IQaRawMaterialRepository : HazelcastRepository<QaRawMaterialEntity, Long> {
    fun findBySta10Id(sta10Id: Long): List<QaRawMaterialEntity>?

    @Query(
        value = "INSERT INTO DAT_KEBS_QA_RAW_MATERIAL tt1\n" +
                "(\n" +
                "    tt1.NAME,tt1.ORIGIN,tt1.STATUS,tt1.SPECIFICATIONS,tt1.QUALITY_CHECKS_TESTING_RECORDS,tt1.STA10_ID\n" +
                ")\n" +
                "SELECT   tt2.NAME,tt2.ORIGIN,tt2.STATUS,tt2.SPECIFICATIONS,tt2.QUALITY_CHECKS_TESTING_RECORDS,:sta10IdToBeUpdated\n" +
                "FROM DAT_KEBS_QA_RAW_MATERIAL tt2 WHERE tt2.STA10_ID =:sta10Id", nativeQuery = true
    )
    fun updateRawMaterials(
        @Param("sta10Id") sta10Id: Long,
        @Param("sta10IdToBeUpdated") sta10IdToBeUpdated: Long
    ): String


}

@Repository
interface IQaManufactureProcessRepository : HazelcastRepository<QaManufacturingProcessEntity, Long> {
    fun findBySta10Id(sta10Id: Long): List<QaManufacturingProcessEntity>?

    @Query(
        value = "INSERT INTO DAT_KEBS_QA_MANUFACTURING_PROCESS t1\n" +
                "(\n" +
                "    t1.PROCESS_FLOW_OF_PRODUCTION,t1.OPERATIONS,t1.CRITICAL_PROCESS_PARAMETERS_MONITORED,t1.FREQUENCY,t1.PROCESS_MONITORING_RECORDS,t1.STATUS,t1.STA10_ID\n" +
                ")\n" +
                "SELECT\n" +
                "    t2.PROCESS_FLOW_OF_PRODUCTION,t2.OPERATIONS,t2.CRITICAL_PROCESS_PARAMETERS_MONITORED,t2.FREQUENCY,t2.PROCESS_MONITORING_RECORDS,t2.STATUS,:sta10IdToBeUpdated\n" +
                "FROM DAT_KEBS_QA_MANUFACTURING_PROCESS t2 WHERE t2.STA10_ID =:sta10Id\n", nativeQuery = true
    )
    fun updateManufacturing(
        @Param("sta10Id") sta10Id: Long,
        @Param("sta10IdToBeUpdated") sta10IdToBeUpdated: Long
    ): String
}

@Repository
interface IQaProductBrandEntityRepository : HazelcastRepository<QaProductManufacturedEntity, Long> {
    fun findBySta10Id(sta10Id: Long): List<QaProductManufacturedEntity>?

    @Query(
        value = "INSERT INTO DAT_KEBS_QA_PRODUCT t1\n" +
                "    (t1.AVAILABLE, t1.PRODUCT_BRAND, t1.PRODUCT_NAME, t1.STATUS, t1.STA10_ID)\n" +
                "SELECT '0', t2.TRADE_MARK, t2.PRODUCT_NAME, '1', :sta10IdToBeUpdated\n" +
                "FROM DAT_KEBS_PERMIT_TRANSACTION t2\n" +
                "WHERE t2.ID = :permit", nativeQuery = true
    )
    fun updateProduct(
        @Param("permit") permit: Long,
        @Param("sta10IdToBeUpdated") sta10IdToBeUpdated: Long
    ): String
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
interface IPaymentRevenueCodesEntityRepository : HazelcastRepository<PaymentRevenueCodesEntity, Long> {
    fun findByRegionId(regionId: Long): List<PaymentRevenueCodesEntity>?
    fun findByRegionIdAndPermitTypeId(regionId: Long, permitTypeId: Long): PaymentRevenueCodesEntity?
}

@Repository
interface IQaWorkplanRepository : HazelcastRepository<QaWorkplanEntity, Long> {
    fun findByPermitNumber(permitNumber: String): List<QaWorkplanEntity>?
    fun findByOfficerId(officerId: Long): List<QaWorkplanEntity>?
    fun findByOfficerIdAndRefNumber(officerId: Long, refNumber: String): QaWorkplanEntity?
}

@Repository
interface IQaBatchInvoiceRepository : HazelcastRepository<QaBatchInvoiceEntity, Long> {
    fun findByUserIdAndInvoiceNumber(userId: Long, refNumber: String): QaBatchInvoiceEntity?
    fun findByInvoiceNumber(refNumber: String): QaBatchInvoiceEntity?
    fun findByInvoiceBatchNumberId(invoiceBatchNumberId: Long): QaBatchInvoiceEntity?
    fun findByUserIdAndInvoiceNumberAndPlantId(
        userId: Long,
        invoiceNumber: String,
        plantId: Long
    ): QaBatchInvoiceEntity?

    fun findByUserId(userId: Long): List<QaBatchInvoiceEntity>?
}

@Repository
interface PermitRepository : JpaRepository<PermitApplicationsEntity, Int>,
    JpaSpecificationExecutor<PermitApplicationsEntity> {

    override fun findAll(@Nullable spec: Specification<PermitApplicationsEntity?>?): MutableList<PermitApplicationsEntity>

}


@Repository
interface IPermitMigrationApplicationsEntityRepository : HazelcastRepository<PermitMigrationApplicationsEntity, Long> {
    fun findByPermitNumber(permitNumber: String): List<PermitMigrationApplicationsEntity>?
}
@Repository
interface IPermitMigrationApplicationsFmarkEntityRepository : HazelcastRepository<PermitMigrationApplicationsEntityFmark, Long> {
    fun findByPermitNumber(permitNumber: String): List<PermitMigrationApplicationsEntityFmark>?
}

@Repository
interface IPermitMigrationApplicationsDmarkEntityRepository : HazelcastRepository<PermitMigrationApplicationsEntityDmark, Long> {
    fun findByPermitNumber(permitNumber: String): List<PermitMigrationApplicationsEntityDmark>?
}

