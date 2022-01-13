package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.pvc.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.hazelcast.repository.HazelcastRepository
import java.sql.Date

interface IwaiversApplicationRepo : HazelcastRepository<PvocWaiversApplicationEntity, Long> {
    fun findAllByStatusAndApplicantNameOrderByCreatedOnDesc(status: Int, applicantName: String, pageable: Pageable) : Page<PvocWaiversApplicationEntity>?
    fun findAllByStatusAndApplicantNameAndCreatedOnBetweenOrderByCreatedOnDesc(status: Int, applicantName: String, from: Date, to: Date, pageable: Pageable) : Page<PvocWaiversApplicationEntity>?
    fun findAllByStatusOrderByCreatedOnDesc(status: Int, pageable: Pageable) : Page<PvocWaiversApplicationEntity>?

    fun findAllByStatusAndCreatedOnBetweenOrderByCreatedOnDesc(status: Int, from: Date, to: Date, pageable: Pageable) : Page<PvocWaiversApplicationEntity>?
    fun findAllByReviewStatusOrderByCreatedOnDesc(reviewStatus: String, pageable: Pageable) : Page<PvocWaiversApplicationEntity>
    fun findAllByNscApprovalStatusOrderByCreatedOnDesc(nscReviewStatus: String, pageable: Pageable) : Page<PvocWaiversApplicationEntity>
    fun findAllByCsApprovalStatusOrderByCreatedOnDesc(csReviewStatus: String, pageable: Pageable) : Page<PvocWaiversApplicationEntity>
    fun findAllByCreatedBy(userName: String?, page: Pageable): Page<PvocWaiversApplicationEntity>?
    fun findFirstByCreatedByAndId(username: String?, id: Long): PvocWaiversApplicationEntity?
}

interface IPvocMasterListRepo : HazelcastRepository<PvocMasterListEntity, Long>{
    fun findAllByWaiversApplicationId (waiversApplicationId: Long) : List<PvocMasterListEntity>?
}

interface IPvocWaiversStatusRepo : HazelcastRepository<PvocWaiversStatusEntity, Long>{
}

interface IPvocWaiversRemarksRepo : HazelcastRepository<PvocWaiversRemarksEntity, Long>{
    fun findAllByWaiverId(waiverId : Long) : List<PvocWaiversRemarksEntity>?
    fun findAllByWaiverReportId(waiverReportId : Long) : List<PvocWaiversRemarksEntity>?
    fun findAllByMinuteId(minuteId : Long) : List<PvocWaiversRemarksEntity>?
    fun findAllByCocTimelineId(cocTimelineId: Long) : List<PvocWaiversRemarksEntity>?
}

interface IPvocWaiversCategoriesRepo : HazelcastRepository<PvocWaiversCategoriesEntity, Long>{
    fun findAllByStatus(status: Int) : List<PvocWaiversCategoriesEntity>?
}

interface IPvocWaiversCategoryDocumentsRepo : HazelcastRepository<PvocWaiversCategoriesDocumentsEntity, Long>{
    fun findAllByStatusAndCategoryId(status: Int, categoryId: Long) : List<PvocWaiversCategoriesDocumentsEntity>?
}

interface IPvocComplaintRepo : HazelcastRepository<PvocComplaintEntity, Long> {
    fun findAllByStatusOrderByCreatedOnDesc(status: Int, pageable: Pageable) : Page<PvocComplaintEntity>?
    fun countAllByRefPrefix(prefix: String): Long
    fun findAllByReviewStatus(status: String, pageable: Pageable) : Page<PvocComplaintEntity>
    fun findAllByRefNoContains(refNo: String, pageable: Pageable) : Page<PvocComplaintEntity>
    fun findAllByReviewStatusAndRefNoContains(status: String,refNo: String, pageable: Pageable) : Page<PvocComplaintEntity>
    fun findAllByStatusAndCreatedOnBetweenOrderByCreatedOnDesc(status: Int, fromDate: Date, toDate :Date, pageable: Pageable) : Page<PvocComplaintEntity>?
}

interface IPvocComplaintCategoryRepo : HazelcastRepository<PvocComplaintCategoryEntity, Long>{
    fun findAllByStatus(status: Int) : List<PvocComplaintCategoryEntity>
}

interface IPvocComplaintCertificationsSubCategoryRepo :  HazelcastRepository<PvocComplaintCertificationSubCategoriesEntity, Long>{
//    fun findAllByStatusAndPvocComplaintCategoryByComplainCategoryId(status: Int, pvocComplaintCategoryByComplainCategoryId: PvocComplaintCategoryEntity) : List<PvocComplaintCertificationSubCategoriesEntity>?
    fun findAllByStatusAndPvocComplaintCategoryByComplainCategoryId(status: Int, pvocComplaintCategoryByComplainCategoryId: PvocComplaintCategoryEntity) : List<PvocComplaintCertificationSubCategoriesEntity>?
}