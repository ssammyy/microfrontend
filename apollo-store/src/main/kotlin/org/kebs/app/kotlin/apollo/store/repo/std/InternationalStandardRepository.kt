package org.kebs.app.kotlin.apollo.store.repo.std

import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.qa.QaRawMaterialEntity
import org.kebs.app.kotlin.apollo.store.model.qa.QaUploadsEntity
import org.kebs.app.kotlin.apollo.store.model.registration.CompanyProfileEntity
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

interface ComJcJustificationRepository : JpaRepository<ComJcJustification, Long> {
}

interface ComJcJustificationUploadsRepository : JpaRepository<ComJcJustificationUploads, Long> {
}

interface CompanyStandardRepository : JpaRepository<CompanyStandard, Long> {
    fun findAllByOrderByIdDesc(): MutableList<CompanyStandard>

    @Query("SELECT MAX(ID) FROM SD_COM_STANDARD", nativeQuery = true)
    fun getMaxComStdId(): String

}

interface ComStandardRequestRepository : JpaRepository<CompanyStandardRequest, Long> {
    fun findAllByOrderByIdDesc(): MutableList<CompanyStandardRequest>

}

interface ComStdActionRepository : JpaRepository<ComStdAction, Long> {
}

interface ComStdDraftRepository : JpaRepository<ComStdDraft, Long> {
    fun findAllByOrderByIdDesc(): MutableList<ComStdDraft>

    @Query("SELECT MAX(ID) FROM SD_COM_STANDARD_DRAFT", nativeQuery = true)
    fun getMaxDraftId(): String

}

interface ComStandardDraftUploadsRepository : JpaRepository<ComStandardDraftUploads, Long> {
    fun findByComDraftDocumentId(id: Long): ComStandardDraftUploads
}

interface ComStandardUploadsRepository : JpaRepository<ComStandardUploads, Long> {
    fun findByComStdDocumentId(id: Long): ComStandardUploads

}

interface ISAdoptionJustificationRepository : JpaRepository<ISAdoptionJustification, Long> {
    fun findAllByOrderByIdDesc(): MutableList<ISAdoptionJustification>
}

interface ISAdoptionProposalRepository : JpaRepository<ISAdoptionProposal, Long> {
    fun findAllByOrderByIdDesc(): MutableList<ISAdoptionProposal>
}

interface ISGazettementRepository : JpaRepository<ISGazettement, Long> {
}

interface ISGazetteNoticeRepository : JpaRepository<ISGazetteNotice, Long> {
}

interface ISUploadStandardRepository : JpaRepository<ISUploadStandard, Long> {
    fun findAllByOrderByIdDesc(): MutableList<ISUploadStandard>
}

interface ISJustificationUploadsRepository : JpaRepository<ISJustificationUploads, Long> {
    fun findByIsJSDocumentId(id: Long): ISJustificationUploads
}

interface ISStandardUploadsRepository : JpaRepository<ISStandardUploads, Long> {
    fun findByIsStdDocumentId(id: Long): ISStandardUploads

}
interface SDISGazetteNoticeUploadsRepository : JpaRepository<SDISGazetteNoticeUploads, Long> {
    fun findByIsGnDocumentId(id: Long): SDISGazetteNoticeUploads
}

interface NWADISDTJustificationRepository : HazelcastRepository<NWADiSdtJustification, Long> {
    @Query(value = "SELECT max(CDN)  FROM SD_NWA_DISDT_JUSTIFICATION", nativeQuery = true)
    fun getMaxCDN(): Long
}

interface NwaDocumentsRepository : JpaRepository<NwaDocument, String> {
    fun findByItemId(itemId: String): MutableList<NwaDocument>
}

interface NwaGazettementRepository : JpaRepository<NWAGazettement, Long> {

}

interface NwaGazetteNoticeRepository : JpaRepository<NWAGazetteNotice, Long> {

}

interface NwaJustificationRepository : JpaRepository<NWAJustification, Long> {
    fun findAllByOrderByIdDesc(): MutableList<NWAJustification>
}

interface NwaPreliminaryDraftRepository : JpaRepository<NWAPreliminaryDraft, Long> {
    fun findAllByOrderByIdDesc(): MutableList<NWAPreliminaryDraft>

}

interface NwaStandardRepository : JpaRepository<NWAStandard, Long> {
    fun findAllByOrderByIdDesc(): MutableList<NWAStandard>
}

interface NwaWorkShopDraftRepository : JpaRepository<NWAWorkShopDraft, Long> {

}

interface StandardRepository : JpaRepository<Standard, Long> {
    @Query(value = "SELECT * FROM SD_STANDARD_TBL WHERE STATUS = 'review'", nativeQuery = true)
    fun reviewedStandards(): Collection<Standard?>?
}

interface StandardReviewCommentsRepository : JpaRepository<StandardReviewComments, Long> {
}

interface StandardReviewFormRepository : JpaRepository<StandardReviewForm, String> {
    fun findByItemId(itemId: String): MutableList<StandardReviewForm>

}

interface StandardReviewRecommendationsRepository : JpaRepository<StandardReviewRecommendations, Long> {
}

interface StandardReviewRepository : JpaRepository<StandardReview, Long> {
}

interface UserListRepository : JpaRepository<UsersEntity, Long> {
    @Query("SELECT u.firstName,u.lastName FROM UsersEntity u WHERE u.id =:id")
    fun findNameById(@Param("id") id: Long?): String

    @Query(value = "SELECT u.FIRST_NAME AS FIRSTNAME,u.LAST_NAME AS LASTNAME,u.ID AS ID FROM DAT_KEBS_USERS u JOIN CFG_USER_ROLES_ASSIGNMENTS c ON u.ID=c.USER_ID " +
            "JOIN CFG_USER_ROLES r ON c.ROLE_ID=r.ID WHERE r.ROLE_NAME IN ('SL_PL_OFFICER','SL_ASSISTANT_MGR','SL_MANAGER') ", nativeQuery = true)
    fun getSlLvThreeList(): List<UserDetailHolder>

    @Query(value = "SELECT * FROM DAT_KEBS_USERS  WHERE USER_TYPE IN ('63','62','61') ", nativeQuery = true)
    fun getSlLvThreeListv(): List<UsersEntity>

    @Query(value = "SELECT u.FIRST_NAME AS FIRSTNAME,u.LAST_NAME AS LASTNAME,u.ID AS ID FROM DAT_KEBS_USERS u JOIN CFG_USER_ROLES_ASSIGNMENTS c ON u.ID=c.USER_ID " +
            "JOIN CFG_USER_ROLES r ON c.ROLE_ID=r.ID WHERE r.ROLE_NAME IN ('SL_PL_OFFICER') ", nativeQuery = true)
    fun getPlList(): List<UserDetailHolder>

    @Query(value = "SELECT * FROM DAT_KEBS_USERS  WHERE USER_TYPE IN ('61') ", nativeQuery = true)
    fun getPlListv(): List<UsersEntity>

    @Query(value = "SELECT u.FIRST_NAME AS FIRSTNAME,u.LAST_NAME AS LASTNAME,u.ID AS ID FROM DAT_KEBS_USERS u JOIN CFG_USER_ROLES_ASSIGNMENTS c ON u.ID=c.USER_ID " +
            "JOIN CFG_USER_ROLES r ON c.ROLE_ID=r.ID WHERE r.ROLE_NAME IN ('SL_PL_OFFICER','SL_ASSISTANT_MGR') ", nativeQuery = true)
    fun getSlLvTwoList(): List<UserDetailHolder>

    @Query(value = "SELECT * FROM DAT_KEBS_USERS  WHERE USER_TYPE IN ('61','62') ", nativeQuery = true)
    fun getSlLvTwoListv(): List<UsersEntity>



}

interface DatKebsSdNwaUploadsEntityRepository : JpaRepository<DatKebsSdNwaUploadsEntity, Long> {
    @Query(value = "SELECT * FROM DAT_KEBS_SD_NWA_UPLOADS WHERE nwaDocumentId = :nwaDocumentId", nativeQuery = true)
    fun findNwaJustification(@Param("nwaDocumentId") nwaDocumentId: Long?): Collection<DatKebsSdNwaUploadsEntity?>?

    @Query(value = "SELECT max(ID)  FROM DAT_KEBS_SD_NWA_UPLOADS", nativeQuery = true)
    fun getMaxUploadedID(): Long
    fun findByNwaDocumentId(id: Long): DatKebsSdNwaUploadsEntity
}

interface SDDIJustificationUploadsRepository : JpaRepository<SDDIJustificationUploads, Long> {
    fun findByDiDocumentId(id: Long): SDDIJustificationUploads
}

interface SdIsDocumentUploadsRepository : JpaRepository<SdIsDocumentUploads, Long> {
    fun findByIsDocumentId(id: Long): SdIsDocumentUploads
}

interface NWAPreliminaryDraftUploadsRepository : JpaRepository<NWAPreliminaryDraftUploads, Long> {
    fun findByNwaPDDocumentId(id: Long): NWAPreliminaryDraftUploads
}

interface NWAWorkShopDraftUploadsRepository : JpaRepository<NWAWorkShopDraftUploads, Long> {
    fun findByNwaWDDocumentId(id: Long): NWAWorkShopDraftUploads
}

interface NWAStandardUploadsRepository : JpaRepository<NWAStandardUploads, Long> {
    fun findByNwaSDocumentId(id: Long): NWAStandardUploads
}

interface DepartmentListRepository : JpaRepository<Department, Long> {
    @Query("SELECT name FROM SD_DEPARTMENT WHERE id=:id", nativeQuery = true)
    fun findNameById(@Param("id") id: Long?): String
}

interface TechnicalComListRepository : JpaRepository<TechnicalCommittee, Long> {
    @Query("SELECT TC_TITLE FROM SD_TECHNICAL_COMMITTEE  WHERE id=:id", nativeQuery = true)
    fun findNameById(@Param("id") id: Long?): String
}

interface ComStandardJCRepository : JpaRepository<ComStandardJC, Long> {

}
//interface UserNameRepository : JpaRepository<UsersEntity,Long>{
//    fun findByUserId(assignedTo: Long?) : MutableList<UsersEntity>
//    @Query("SELECT u.firstName,u.lastName FROM UsersEntity u WHERE u.id =:id")
//    fun findNameById(@Param("id") id: Long?): String
//}
