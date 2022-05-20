package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.di.CdImporterDetailsEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface IRiskInspectionCheckRepository : HazelcastRepository<RiskProfileEntity, Long>{
    fun findByHsCode(hsCode: String): RiskProfileEntity?
}

@Repository
interface IDestinationInspectionRepository : HazelcastRepository<ConsignmentDocumentEntity, Long>{
    override fun findAll(): List<ConsignmentDocumentEntity>

    override fun findById(id: Long): Optional<ConsignmentDocumentEntity>

    override fun <S : ConsignmentDocumentEntity?> save(entity: S): S

    //fun findByCocIdIsNotNull(page: Pageable?): Page<ConsignmentDocumentEntity>?

    fun findByArrivalPointAndCocIdIsNotNull(arrivalPoint: CfgArrivalPointEntity, pageable: Pageable): Page<ConsignmentDocumentEntity>?

    fun findByArrivalPointAndAssignedStatusAndCocIdIsNotNull(arrivalPoint: CfgArrivalPointEntity, assignedStatus: Int, pageable: Pageable): Page<ConsignmentDocumentEntity>?

    fun findAllByAssignedIo(assignedIo: Long) : List<ConsignmentDocumentEntity>?

    fun findConsignmentDocumentEntitiesByApplicantIdEndsWithAndApproveName(applicantId: CdApplicantDetailsEntity, approveName: String):List<ConsignmentDocumentEntity>?

    // fun findAllByCreatedByBetween(createdDate: TIMESTAMP, createdDate: CreatedDate) : List<ConsignmentDocumentEntity>?

    //fun findBySupervisorIdAndCocIdIsNotNull(supervisorId: Long, pageable: Pageable): Page<ConsignmentDocumentEntity>?
    fun findBySupervisorIdAndNcrIdNotNull(supervisorId: Long, pageable: Pageable): Page<ConsignmentDocumentEntity>?

    fun findBySupervisorIdAndCorIdNotNull(supervisorId: Long, pageable: Pageable): Page<ConsignmentDocumentEntity>?
    fun findByMinistryInspectionStatus(ministryInspectionStatus: Int, pageable: Pageable): Page<ConsignmentDocumentEntity>?

    fun findByAssignedIoAndCocIdIsNotNull(assignedIo: Long, pageable: Pageable): Page<ConsignmentDocumentEntity>?

    fun findFirstByImporterIdAndCocIdIsNotNull(importerId: CdImporterDetailsEntity, pageable: Pageable): Page<ConsignmentDocumentEntity>?


    fun findByArrivalPointAndBlacklistStatusAndBlacklistApprovedAndCocIdIsNotNull(arrivalPoint: CfgArrivalPointEntity, blacklistStatus: Int, blacklistApproved: Int, pageable: Pageable): Page<ConsignmentDocumentEntity>?

    fun findByArrivalPointAndTargetedStatusAndTargetApprovedAndCocIdIsNotNull(arrivalPoint: CfgArrivalPointEntity, targetedStatus: Int, targetApproved: Int, pageable: Pageable): Page<ConsignmentDocumentEntity>?

    fun findByAssignedIoAndCorIdIsNotNull(assignedIo: Long, pageable: Pageable): Page<ConsignmentDocumentEntity>?

    fun findByAssignedStatusAndCorIdNotNull(assignedStatus: Int) : List<ConsignmentDocumentEntity>?


//    @Query("SELECT a from  ConsignmentDocumentEntity where a.applicationDate >= :fromDate and a.applicationDate <= :toDate")
//    fun filterDateRange(@Param("fromDate") fromDate: Time, @Param("toDate") toDate: Time): List<ConsignmentDocumentEntity>?

    fun findAllByApplicationDateBetweenAndCorIdNotNull(fromDate: Date, toDate: Date) : List<ConsignmentDocumentEntity>?
    fun findAllByApplicationDateBetweenAndNcrIdNotNull(fromDate: Date, toDate: Date) : List<ConsignmentDocumentEntity>?
}

@Repository
interface ICocItemRepository : HazelcastRepository<CocItemsEntity, Long>{
    fun findByCocId(cocId: Long): List<CocItemsEntity>?
}



@Repository
interface IRiskInspectionCheck : HazelcastRepository<RiskProfileEntity, Long>{
    fun findByHsCode(hsCode: String): RiskProfileEntity?
}

@Repository
interface ICorsItems : HazelcastRepository<CorsEntity, Long>{
    fun findByIdAndStatus(id: Long, status: Int): CorsEntity
}







//@Repository
//interface ICheckList : HazelcastRepository<CdInspectionChecklistEntity, Long>{
//    fun findByUcrNumber(ucrNumber: String): CdInspectionChecklistEntity?
//
//}





@Repository
interface IRemarksRepository : HazelcastRepository<RemarksEntity, Long>{
    fun findByConsignmentDocumentId(consignmentDocumentId: Long): List<RemarksEntity>
    fun findByUserId(user_id: Long) : List<RemarksEntity>
    fun findAllByConsignmentDocumentId(consignmentDocumentId: Long) : List<RemarksEntity>
    fun findAllByPvocExceptionApplicationId(pvocExceptionApplicationId: Long) : List<RemarksEntity>?
}

@Repository
interface ICocsRepository : HazelcastRepository<CocsEntity, Long> {
    fun findByUcrNumberAndCocType(ucrNumber: String, docType: String): CocsEntity?
    fun findByUcrNumberAndCocTypeAndVersion(ucrNumber: String, docType: String, version: Long?): CocsEntity?
    fun findFirstByCocNumber(cocNumber: String): CocsEntity?
    fun findFirstByCocNumberAndCocNumberIsNotNullOrCoiNumberAndCoiNumberIsNotNull(cocNumber: String, coiNumber: String): Optional<CocsEntity>
    fun findFirstByCocNumberIsNotNullAndCocTypeAndConsignmentDocIdIsNotNull(cocType: String): CocsEntity?
    fun findFirstByCoiNumberIsNotNullAndCocTypeAndConsignmentDocIdIsNotNullOrderByCreatedOnDesc(cocType: String): CocsEntity?
    fun findAllByRouteAndShipmentSealNumbersIsNull(route: String, pageable: Pageable): Page<CocsEntity>?
    fun findAllByReportGenerationStatus(reportGenerationStatus: Int): List<CocsEntity>

    @Query(value = "select count(*) as cc from DAT_KEBS_COCS where to_char(COC_ISSUE_DATE,'YYYY')=:gYear and COC_TYPE=:cocType", nativeQuery = true)
    fun countAllByYearGenerate(gYear: Long, cocType: String): Long
}



@Repository
interface ILocalCocItems : HazelcastRepository<CdLocalCocItemsEntity, Long>{
    fun findByUcrNumber(ucrNumber: String): CdLocalCocItemsEntity?
}

@Repository
interface IRiskTypes : HazelcastRepository<CfgRiskTypesEntity, Long> {
//    fun findByUcrNumber(ucrNumber: String): CfgRiskTypesEntity?
}

@Repository
interface IRiskProfile : HazelcastRepository<RiskProfileEntity, Long> {
    fun findByImporterName(importerName: String): RiskProfileEntity?
}

//@Repository
//interface ICocsRepository : HazelcastRepository<CocsEntity, Long>{
//    fun findByUcrNumber(ucrNumber: String): CocsEntity?
//}
