package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.customdto.PvocReconciliationReportDto
import org.kebs.app.kotlin.apollo.store.model.di.CdItemDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.pvc.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.sql.Date
import java.util.*

interface IPvocApplicationRepo : HazelcastRepository<PvocApplicationEntity, Long> {
    fun findByIdAndPvocWaStatus(id: Long, pvocWaStatus: Int): PvocApplicationEntity?
    fun findAllByFinalApproval(status: Int, pageable: Pageable): Page<PvocApplicationEntity>
    fun findAllByReviewStatus(status: Int, pageable: Pageable): Page<PvocApplicationEntity>
    fun findAllByStatus(status: Int, pageable: Pageable): Page<PvocApplicationEntity>?
    fun findAllByCreatedByOrderByCreatedOnDesc(username: String, pageable: Pageable): Page<PvocApplicationEntity>?
    fun findFirstByConpanyNameAndCompanyPinNo(conpanyName: String, companyPinNo: String): PvocApplicationEntity?
    fun findFirstByConpanyNameAndCompanyPinNoAndFinished(conpanyName: String, companyPinNo: String, finished: Int): PvocApplicationEntity?
    fun findAllByCreatedOnBetween(createdOn: Date, createdOn2: Date, pageable: Pageable): Page<PvocApplicationEntity>?
    fun findAllByConpanyNameAndStatus(conpanyName: String, status: Int, pageable: Pageable): Page<PvocApplicationEntity>?
    fun findAllByApplicationDateBetweenAndConpanyNameAndStatus(applicationDate: java.util.Date, applicationDate2: java.util.Date, conpanyName: String, status: Int, pageable: Pageable): Page<PvocApplicationEntity>?
    fun findAllByCreatedOnBetweenAndConpanyNameAndStatus(createdOn: Date, createdOn2: Date, conpanyName: String, status: Int, pageable: Pageable): Page<PvocApplicationEntity>?
    fun findByIdIsIn(ids: List<Long>): List<PvocApplicationEntity>?

    fun findAllByConpanyNameAndFinished(conpanyName: String, finished: Int): List<PvocApplicationEntity>?
    fun findFirstByCreatedByAndId(username: String?, id: Long): PvocApplicationEntity?
}

interface IPvocExceptionApplicationStatusEntityRepo : HazelcastRepository<PvocExceptionApplicationStatusEntity, Long> {
    fun findFirstByMapId(mapId: Int): PvocExceptionApplicationStatusEntity?
    fun findFirstByCreatedByAndId(username: String?, id: Long): PvocExceptionApplicationStatusEntity?
}

interface IPvocApplicationProductsRepo : HazelcastRepository<PvocApplicationProductsEntity, Long> {
    fun findAllByPvocApplicationId(pvocApplicationId: PvocApplicationEntity): List<PvocApplicationProductsEntity>?
    fun findAllByPvocApplicationId_Id(pvocApplicationId: Long?): List<PvocApplicationProductsEntity>?
    fun findAllByIdAndExceptionId(itemId: Long, requestId: Long): Optional<PvocApplicationProductsEntity>
}

interface IPvocAgentMonitoringStatusEntityRepo : HazelcastRepository<PvocAgentMonitoringStatusEntity, Long> {
    fun findAllByStatus(status: Int): List<PvocAgentMonitoringStatusEntity>?
}

interface IPvocExceptionCertificateRepository : HazelcastRepository<PvocExceptionCertificate, Long> {
}

interface IPvocApplicationTypesRepo : HazelcastRepository<PvocApplicationTypeEntity, Long>

interface IPvocApplicationOrigninCountryEntityRepo : HazelcastRepository<PvocApplicationOrigninCountryEntity, Long>

interface IPvocApplicationExceptionCategoriesEntityRepo : HazelcastRepository<PvocApplicationExceptionCategoriesEntity, Long> {
    fun findAllByStatus(status: Int): List<PvocApplicationExceptionCategoriesEntity>?
}

interface IPvocWaiversReportRepo : HazelcastRepository<PvocWaiversReportsEntity, Long> {
    fun findAllByStatus(status: Int, pageable: Pageable): Page<PvocWaiversReportsEntity>?


}

interface IPvocWaiversApplicationDocumentRepo : HazelcastRepository<PvocWaiversApplicationDocumentsEntity, Long> {
    fun findAllByWaiverId(waiverId: Long): List<PvocWaiversApplicationDocumentsEntity>?
    fun findFirstByWaiverIdAndReason(waiverId: Long, reason: String): PvocWaiversApplicationDocumentsEntity?
}

interface IPvocWaiversWetcMinutesEntityRepo : HazelcastRepository<PvocWaiversWetcMinutesEntity, Long> {
    fun findAllByStatusOrderByCreatedOnDesc(status: Int, pageable: Pageable): Page<PvocWaiversWetcMinutesEntity>?
    fun findAllByStatusAndCreatedOnBetweenOrderByCreatedOnDesc(status: Int, fromDate: Date, toDate: Date, pageable: Pageable): Page<PvocWaiversWetcMinutesEntity>?
}

interface IPvocWaiversRequestLetterRepo : HazelcastRepository<PvocWaiversRequestLetterEntity, Long> {
    fun findAllByStatus(status: Int, pageable: Pageable): Page<PvocWaiversRequestLetterEntity>?
}

interface ICdItemDetailsRepo : HazelcastRepository<CdItemDetailsEntity, Long> {
    fun findAllByStatusAndSampledStatus(status: Int, sampledStatus: Int): List<CdItemDetailsEntity>?

}

interface PvocTimelineDataPenaltyInvoiceEntityRepo : HazelcastRepository<PvocTimelineDataPenaltyInvoiceEntity, Long> {
    fun findAllByStatus(status: Int, pageable: Pageable): Page<PvocTimelineDataPenaltyInvoiceEntity>?
    fun findByCocId(cocId: Long): PvocTimelineDataPenaltyInvoiceEntity?

}

interface PvocPenaltyInvoicingEntityRepo : HazelcastRepository<PvocPenaltyInvoicingEntity, Long> {
    fun findAllByStatus(status: Int, pageable: Pageable): Page<PvocTimelineDataPenaltyInvoiceEntity>?
    fun findByCocId(cocId: Long): PvocTimelineDataPenaltyInvoiceEntity?
}

interface PvocApiClientRepo : HazelcastRepository<PvocAgentContractEntity, Long> {
    fun findByServiceRenderedIdAndName(serviceRenderedId: Long, name: String): PvocAgentContractEntity?
    fun findFirstByServiceRenderedIdAndName(serviceRenderedId: Long, name: String): PvocAgentContractEntity
    fun findByServiceRenderedIdAndPvocPartner(serviceRenderedId: Long, pvocPartner: Long): PvocAgentContractEntity?
}

interface PvocAgentContractEntityRepo : HazelcastRepository<PvocAgentContractEntity, Long> {
    fun findByServiceRenderedIdAndName(serviceRenderedId: Long, name: String): PvocAgentContractEntity?
    fun findFirstByServiceRenderedIdAndName(serviceRenderedId: Long, name: String): PvocAgentContractEntity
    fun findByServiceRenderedIdAndPvocPartner(serviceRenderedId: Long, pvocPartner: Long): PvocAgentContractEntity?
}

interface PvocRevenueReportEntityRepo : HazelcastRepository<PvocRevenueReportEntity, Long> {
    fun findByCocNo(cocNo: String): PvocRevenueReportEntity?
}

interface PvocReconciliationReportEntityRepo : HazelcastRepository<PvocReconciliationReportEntity, Long> {
    fun findAllByStatusAndReviewedOrReviewedIsNull(status: Int, reviewed: Int, pageable: Pageable): Page<PvocReconciliationReportEntity>
    fun findAllByStatusAndReviewedAndCreatedOnBetween(status: Int, reviewed: Int, createdOn: Date, createdOn2: Date, pageable: Pageable): Page<PvocReconciliationReportEntity>?

    //    fun findAllByStatusAndCreatedOnBetween(status: Int, createdOn: Timestamp, createdOn2: Timestamp) : List<PvocReconciliationReportEntity, Long>
    fun findByCertificateNo(certificationNo: String): PvocReconciliationReportEntity?

    @Query("SELECT SUM(coalesce(INSPECTION_FEE ,0)) AS inspectionfeesum, SUM(coalesce(VERIFICATION_FEE ,0)) AS verificationfeesum,\n" +
            "       SUM(coalesce(ROYALTIES_TO_KEBS ,0)) AS royaltiestokebssum " +
            "FROM DAT_KEBS_PVOC_RECONCILIATION_REPORT " +
            "WHERE CREATED_ON BETWEEN to_timestamp_tz (:dateFrom, 'YYYY-MM-DD')\n" +
            "          AND to_timestamp_tz(:dateTo, 'YYYY-MM-DD')", nativeQuery = true)
    fun getPvocReconciliationReportDto(@Param("dateFrom") dateFrom: String, @Param("dateTo") dateTo: String): PvocReconciliationReportDto?


    @Query("select to_char(CREATED_ON, 'YYYY/MM') as month, sum(INSPECTION_FEE) AS inspectionfeesum, sum(ROYALTIES_TO_KEBS) AS royaltiestokebssum , sum(VERIFICATION_FEE) AS verificationfeesum, " +
            "sum(PENALTY_INVOICED) AS penaltySum\n" +
            "from DAT_KEBS_PVOC_RECONCILIATION_REPORT\n" +
            "group by to_char(CREATED_ON, 'YYYY/MM')", nativeQuery = true)
    fun getSummaryByMonth(): List<PvocReconciliationReportDto>
}

interface IPvocExceptionIndustrialSparesCategoryEntityRepo : HazelcastRepository<PvocExceptionIndustrialSparesCategoryEntity, Long> {
    fun findAllByExceptionId(exceptionId: Long?): List<PvocExceptionIndustrialSparesCategoryEntity>
    fun findAllByIdAndExceptionId(id: Long,exceptionId: Long?): Optional<PvocExceptionIndustrialSparesCategoryEntity>
    fun findAllByExceptionIdAndReviewStatus(exceptionId: Long, reviewStatus: String): List<PvocExceptionIndustrialSparesCategoryEntity>
}

interface IPvocExceptionMainMachineryCategoryEntityRepo : HazelcastRepository<PvocExceptionMainMachineryCategoryEntity, Long> {
    fun findAllByExceptionId(exceptionId: Long?): List<PvocExceptionMainMachineryCategoryEntity>
    fun findAllByExceptionIdAndReviewStatus(exceptionId: Long, reviewStatus: String): List<PvocExceptionMainMachineryCategoryEntity>
    fun findAllByIdAndExceptionId( itemId: Long,requestId: Long): Optional<PvocExceptionMainMachineryCategoryEntity>
}

interface IPvocExceptionRawMaterialCategoryEntityRepo : HazelcastRepository<PvocExceptionRawMaterialCategoryEntity, Long> {
    fun findAllByExceptionId(exceptionId: Long?): List<PvocExceptionRawMaterialCategoryEntity>
    fun findAllByExceptionIdAndReviewStatus(exceptionId: Long, reviewStatus: String): List<PvocExceptionRawMaterialCategoryEntity>
    fun findAllByIdAndExceptionId(itemId: Long, requestId: Long): Optional<PvocExceptionRawMaterialCategoryEntity>
}

interface IPvocComplaintsEmailVerificationEntityRepo : HazelcastRepository<PvocComplaintsEmailVerificationEntity, Long> {

}

interface IPvocComplaintsTokenVerification : HazelcastRepository<PvocComplaintsTokenVerificationEntity, Long> {

}