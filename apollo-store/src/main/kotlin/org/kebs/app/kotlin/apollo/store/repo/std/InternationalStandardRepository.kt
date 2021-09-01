package org.kebs.app.kotlin.apollo.store.repo.std

import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface InternationalStandardRepository {
}
@Repository
interface ISAdoptionCommentsRepository : JpaRepository<ISAdoptionComments, Long> {
}

interface ComJcJustificationRepository: JpaRepository<ComJcJustification,Long> {
}

interface CompanyStandardRepository : JpaRepository<CompanyStandard,Long> {
}
interface ComStandardRequestRepository : JpaRepository<CompanyStandardRequest,Long> {
    fun findAllByOrderByIdDesc(): MutableList<CompanyStandardRequest>
}
interface ComStdActionRepository: JpaRepository<ComStdAction,Long> {
}
interface ComStdDraftRepository: JpaRepository<ComStdDraft,Long> {
}
interface ISAdoptionJustificationRepository : JpaRepository<ISAdoptionJustification, Long> {
}
interface ISAdoptionProposalRepository: JpaRepository<ISAdoptionProposal, Long> {
    fun findAllByOrderByIdDesc(): MutableList<ISAdoptionProposal>
}
interface ISGazettementRepository : JpaRepository<ISGazettement, Long> {
}
interface ISGazetteNoticeRepository : JpaRepository<ISGazetteNotice, Long> {
}
interface ISUploadStandardRepository : JpaRepository<ISUploadStandard, Long> {
}

interface NWADISDTJustificationRepository : HazelcastRepository<NWADiSdtJustification, Long>{
    @Query(value = "SELECT max(CDN)  FROM SD_NWA_DISDT_JUSTIFICATION", nativeQuery = true)
    fun getMaxCDN(): Long
}

interface NwaDocumentsRepository : JpaRepository<NwaDocument, String> {
    fun findByItemId(itemId: String): MutableList<NwaDocument>
}

interface NwaGazettementRepository: JpaRepository<NWAGazettement,Long>{

}

interface NwaGazetteNoticeRepository: JpaRepository<NWAGazetteNotice,Long>{

}

interface NwaJustificationRepository: JpaRepository<NWAJustification,Long>{
    fun findAllByOrderByIdDesc(): MutableList<NWAJustification>
}

interface NwaPreliminaryDraftRepository: JpaRepository<NWAPreliminaryDraft,Long>{
    fun findAllByOrderByIdDesc(): MutableList<NWAPreliminaryDraft>

}

interface NwaStandardRepository: JpaRepository<NWAStandard,Long>{
    fun findAllByOrderByIdDesc(): MutableList<NWAStandard>
}

interface NwaWorkShopDraftRepository: JpaRepository<NWAWorkShopDraft,Long>{

}

interface StandardRepository : JpaRepository<Standard, Long> {
    @Query(value = "SELECT * FROM SD_STANDARD_TBL WHERE STATUS = 'review'", nativeQuery = true)
    fun reviewedStandards(): Collection<Standard?>?
}

interface StandardReviewCommentsRepository : JpaRepository<StandardReviewComments,Long> {
}
interface StandardReviewFormRepository : JpaRepository<StandardReviewForm,String> {
    fun findByItemId(itemId: String): MutableList<StandardReviewForm>

}
interface StandardReviewRecommendationsRepository : JpaRepository<StandardReviewRecommendations,Long> {
}
interface StandardReviewRepository : JpaRepository<StandardReview, Long> {
}
interface UserListRepository : JpaRepository<UsersEntity,Long>{
    @Query("SELECT u.firstName,u.lastName,u.id FROM UsersEntity u WHERE u.id =:id")
    fun findNameById(@Param("id") id: Long?): String
}
interface DatKebsSdNwaUploadsEntityRepository : JpaRepository<DatKebsSdNwaUploadsEntity, Long> {
}
interface SDDIJustificationUploadsRepository : JpaRepository<SDDIJustificationUploads, Long> {
}

interface SdIsDocumentUploadsRepository : JpaRepository<SdIsDocumentUploads, Long> {
}

interface NWAPreliminaryDraftUploadsRepository : JpaRepository<NWAPreliminaryDraftUploads, Long> {
}
interface NWAWorkShopDraftUploadsRepository : JpaRepository<NWAWorkShopDraftUploads, Long> {
}
interface NWAStandardUploadsRepository : JpaRepository<NWAStandardUploads, Long> {
}
interface DepartmentListRepository : JpaRepository<Department,Long>{
    @Query("SELECT name FROM SD_DEPARTMENT WHERE id=:id", nativeQuery = true)
    fun findNameById(@Param("id") id: Long?): String
}

interface TechnicalComListRepository : JpaRepository<TechnicalCommittee,Long>{
    @Query("SELECT TC_TITLE FROM SD_TECHNICAL_COMMITTEE  WHERE id=:id", nativeQuery = true)
    fun findNameById(@Param("id") id: Long?): String
}
//interface UserNameRepository : JpaRepository<UsersEntity,Long>{
//    fun findByUserId(assignedTo: Long?) : MutableList<UsersEntity>
//    @Query("SELECT u.firstName,u.lastName FROM UsersEntity u WHERE u.id =:id")
//    fun findNameById(@Param("id") id: Long?): String
//}
