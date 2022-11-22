package org.kebs.app.kotlin.apollo.store.repo.std

import org.kebs.app.kotlin.apollo.store.model.StandardLevyOperationsClosure
import org.kebs.app.kotlin.apollo.store.model.StandardLevySiteVisitRemarks
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
    fun findByProposalID(id: Long): MutableIterable<ISAdoptionComments>?
    @Query(
        value = "SELECT ID as id,USER_ID as userId,ADOPTION_PROPOSAL_COMMENT as adoptionComment,COMMENT_TIME as commentTime,PROPOSAL_ID as proposalId,TITLE as title," +
                "DOCUMENT_TYPE as documentType,CLAUSE as clause,ORGANIZATION as comNameOfOrganization,PARAGRAPH as paragraph,TYPE_OF_COMMENT as typeOfComment,PROPOSED_CHANGE as proposedChange " +
                "FROM SD_ADOPTION_PROPOSAL_COMMENTS  WHERE PROPOSAL_ID=:id",
        nativeQuery = true
    )
    fun getProposalComments(id: Long): MutableList<ISProposalComments>
}

interface ComJcJustificationRepository : JpaRepository<ComJcJustification, Long> {
}

interface ComJcJustificationUploadsRepository : JpaRepository<ComJcJustificationUploads, Long> {
}

interface CompanyStandardRepository : JpaRepository<CompanyStandard, Long> {
    fun findAllByOrderByIdDesc(): MutableList<CompanyStandard>

    @Query("SELECT MAX(ID) FROM SD_COM_STANDARD", nativeQuery = true)
    fun getMaxComStdId(): String

    @Query(
        value = "SELECT u.EMAIL as userEmail,u.FIRST_NAME as firstName,u.LAST_NAME as lastName  FROM DAT_KEBS_USERS u LEFT JOIN CFG_USER_ROLES_ASSIGNMENTS a ON u.ID=a.USER_ID LEFT JOIN CFG_USER_ROLES r ON a.ROLE_ID=r.ID   WHERE r.ROLE_NAME='SAC_SEC_SD'",
        nativeQuery = true
    )
    fun getSacSecEmailList(): MutableList<UserEmailListHolder>

    @Query(
        value = "SELECT u.EMAIL as userEmail,u.FIRST_NAME as firstName,u.LAST_NAME as lastName  FROM DAT_KEBS_USERS u WHERE u.ID=:id",
        nativeQuery = true
    )
    fun getUserEmail(@Param("id") id: Long?): MutableList<UserEmailListHolder>

    @Query(
        value = "SELECT u.EMAIL as userEmail,u.FIRST_NAME as firstName,u.LAST_NAME as lastName  FROM DAT_KEBS_USERS u LEFT JOIN CFG_USER_ROLES_ASSIGNMENTS a ON u.ID=a.USER_ID LEFT JOIN CFG_USER_ROLES r ON a.ROLE_ID=r.ID   WHERE r.ROLE_NAME='STAKEHOLDERS_SD'",
        nativeQuery = true
    )
    fun getStakeHoldersEmailList(): MutableList<UserEmailListHolder>


    @Query(
        value = "SELECT u.EMAIL as userEmail,u.FIRST_NAME as firstName,u.LAST_NAME as lastName  FROM DAT_KEBS_USERS u LEFT JOIN CFG_USER_ROLES_ASSIGNMENTS a ON u.ID=a.USER_ID LEFT JOIN CFG_USER_ROLES r ON a.ROLE_ID=r.ID   WHERE r.ROLE_NAME='TC_SEC_SD'",
        nativeQuery = true
    )
    fun getTcSecEmailList(): MutableList<UserEmailListHolder>



    @Query(
        value = "SELECT u.EMAIL as userEmail,u.FIRST_NAME as firstName,u.LAST_NAME as lastName  FROM DAT_KEBS_USERS u LEFT JOIN CFG_USER_ROLES_ASSIGNMENTS a ON u.ID=a.USER_ID LEFT JOIN CFG_USER_ROLES r ON a.ROLE_ID=r.ID   WHERE r.ROLE_NAME='HOP_SD'",
        nativeQuery = true
    )
    fun getHopEmailList(): MutableList<UserEmailListHolder>



    @Query(
        value = "SELECT MAX(u.ID) as userId  FROM DAT_KEBS_USERS u LEFT JOIN CFG_USER_ROLES_ASSIGNMENTS a ON u.ID=a.USER_ID LEFT JOIN CFG_USER_ROLES r ON a.ROLE_ID=r.ID   WHERE r.ROLE_NAME='HOD_TWO_SD'",
        nativeQuery = true
    )
    fun getHodId(): Long

    @Query(
        value = "SELECT MAX(u.ID) as userId  FROM DAT_KEBS_USERS u LEFT JOIN CFG_USER_ROLES_ASSIGNMENTS a ON u.ID=a.USER_ID LEFT JOIN CFG_USER_ROLES r ON a.ROLE_ID=r.ID   WHERE r.ROLE_NAME='PL_SD'",
        nativeQuery = true
    )
    fun getProjectLeaderId(): Long

    @Query(
        value = "SELECT MAX(u.ID) as userId  FROM DAT_KEBS_USERS u LEFT JOIN CFG_USER_ROLES_ASSIGNMENTS a ON u.ID=a.USER_ID LEFT JOIN CFG_USER_ROLES r ON a.ROLE_ID=r.ID   WHERE r.ROLE_NAME='HOP_SD'",
        nativeQuery = true
    )
    fun getHopId(): Long

    @Query(
        value = "SELECT MAX(u.ID) as userId  FROM DAT_KEBS_USERS u LEFT JOIN CFG_USER_ROLES_ASSIGNMENTS a ON u.ID=a.USER_ID LEFT JOIN CFG_USER_ROLES r ON a.ROLE_ID=r.ID   WHERE r.ROLE_NAME='JC_SEC_SD'",
        nativeQuery = true
    )
    fun getJcSecId(): Long

    @Query(
        value = "SELECT MAX(u.ID) as userId  FROM DAT_KEBS_USERS u LEFT JOIN CFG_USER_ROLES_ASSIGNMENTS a ON u.ID=a.USER_ID LEFT JOIN CFG_USER_ROLES r ON a.ROLE_ID=r.ID   WHERE r.ROLE_NAME='COM_SEC_SD'",
        nativeQuery = true
    )
    fun getComSecId(): Long

    @Query(
        value = "SELECT MAX(u.ID) as userId  FROM DAT_KEBS_USERS u LEFT JOIN CFG_USER_ROLES_ASSIGNMENTS a ON u.ID=a.USER_ID LEFT JOIN CFG_USER_ROLES r ON a.ROLE_ID=r.ID   WHERE r.ROLE_NAME='EDITOR_SD'",
        nativeQuery = true
    )
    fun getEditorId(): Long

    @Query(
        value = "SELECT MAX(u.ID) as userId  FROM DAT_KEBS_USERS u LEFT JOIN CFG_USER_ROLES_ASSIGNMENTS a ON u.ID=a.USER_ID LEFT JOIN CFG_USER_ROLES r ON a.ROLE_ID=r.ID   WHERE r.ROLE_NAME='DRAUGHTSMAN_SD'",
        nativeQuery = true
    )
    fun getDraughtsmanId(): Long

    @Query(
        value = "SELECT MAX(u.ID) as userId  FROM DAT_KEBS_USERS u LEFT JOIN CFG_USER_ROLES_ASSIGNMENTS a ON u.ID=a.USER_ID LEFT JOIN CFG_USER_ROLES r ON a.ROLE_ID=r.ID   WHERE r.ROLE_NAME='PROOFREADER_SD'",
        nativeQuery = true
    )
    fun getProofReaderId(): Long


    @Query(
        value = "SELECT MAX(u.ID) as userId  FROM DAT_KEBS_USERS u LEFT JOIN CFG_USER_ROLES_ASSIGNMENTS a ON u.ID=a.USER_ID LEFT JOIN CFG_USER_ROLES r ON a.ROLE_ID=r.ID   WHERE r.ROLE_NAME='TC_SEC_SD'",
        nativeQuery = true
    )
    fun getTcSecId(): Long

    @Query(
        value = "SELECT MAX(u.ID) as userId  FROM DAT_KEBS_USERS u LEFT JOIN CFG_USER_ROLES_ASSIGNMENTS a ON u.ID=a.USER_ID LEFT JOIN CFG_USER_ROLES r ON a.ROLE_ID=r.ID   WHERE r.ROLE_NAME='KNW_SEC_SD'",
        nativeQuery = true
    )
    fun getKnwSecId(): Long

    @Query(
        value = "SELECT MAX(u.ID) as userId  FROM DAT_KEBS_USERS u LEFT JOIN CFG_USER_ROLES_ASSIGNMENTS a ON u.ID=a.USER_ID LEFT JOIN CFG_USER_ROLES r ON a.ROLE_ID=r.ID   WHERE r.ROLE_NAME='SPC_SEC_SD'",
        nativeQuery = true
    )
    fun getSpcSecId(): Long

    @Query(
        value = "SELECT MAX(u.ID) as userId  FROM DAT_KEBS_USERS u LEFT JOIN CFG_USER_ROLES_ASSIGNMENTS a ON u.ID=a.USER_ID LEFT JOIN CFG_USER_ROLES r ON a.ROLE_ID=r.ID   WHERE r.ROLE_NAME='DI_SDT_SD'",
        nativeQuery = true
    )
    fun getDiDirectorId(): Long

    @Query(
        value = "SELECT MAX(u.ID) as userId  FROM DAT_KEBS_USERS u LEFT JOIN CFG_USER_ROLES_ASSIGNMENTS a ON u.ID=a.USER_ID LEFT JOIN CFG_USER_ROLES r ON a.ROLE_ID=r.ID   WHERE r.ROLE_NAME='SAC_SEC_SD'",
        nativeQuery = true
    )
    fun getSaSecId(): Long

    @Query(
        value = "SELECT MAX(u.ID) as userId  FROM DAT_KEBS_USERS u LEFT JOIN CFG_USER_ROLES_ASSIGNMENTS a ON u.ID=a.USER_ID LEFT JOIN CFG_USER_ROLES r ON a.ROLE_ID=r.ID   WHERE r.ROLE_NAME='HOD_SIC_SD'",
        nativeQuery = true
    )
    fun getHoSicId(): Long

}

interface ComStandardRequestRepository : JpaRepository<CompanyStandardRequest, Long> {
    fun findAllByOrderByIdDesc(): MutableList<CompanyStandardRequest>

    @Query(value = "SELECT * FROM SD_COM_STANDARD_REQUEST  ", nativeQuery = true)
    fun getCompanyStandardRequest(): MutableList<CompanyStandardRequest>

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

    @Query(value = "SELECT  ID as id,MEETING_DATE AS meetingDate,TC as tcId,TC_SEC as tcSec,SL_NUMBER as slNumber," +
            "EDITION as edition,REQUEST_NUMBER as requestNumber,REQUESTED_BY AS requestedBy,ISSUES_ADDRESSED as issuesAddressed," +
            "TC_ACCEPTANCE_DATE as tcAcceptanceDate,REFERENCE_MATERIAL AS referenceMaterial,DEPARTMENT as department,REMARKS as remarks," +
            "cast(SUBMISSION_DATE as varchar(200)) AS submissionDate,TC_COMMITTEE as tcCommittee,DEPARTMENT_NAME as departmentName,POSITIVE_VOTES as positiveVotes," +
            "NEGATIVE_VOTES as negativeVotes,TITLE as title,SCOPE as scope,NORMATIVE_REFERENCE as normativeReference,SYMBOLS_ABBREVIATED_TERMS as symbolsAbbreviatedTerms," +
            "CLAUSE as clause,SPECIAL as special,PROPOSAL_ID as proposalId FROM SD_ADOPTION_PROPOSAL_JUSTIFICATION  WHERE  STATUS='0'", nativeQuery = true)
    fun getISJustification(): MutableList<ISAdoptionProposalJustification>

    @Query(value = "SELECT  ID as id,MEETING_DATE AS meetingDate,TC as tcId,TC_SEC as tcSec,SL_NUMBER as slNumber," +
            "EDITION as edition,REQUEST_NUMBER as requestNumber,REQUESTED_BY AS requestedBy,ISSUES_ADDRESSED as issuesAddressed," +
            "TC_ACCEPTANCE_DATE as tcAcceptanceDate,REFERENCE_MATERIAL AS referenceMaterial,DEPARTMENT as department,REMARKS as remarks," +
            "cast(SUBMISSION_DATE as varchar(200)) AS submissionDate,TC_COMMITTEE as tcCommittee,DEPARTMENT_NAME as departmentName,POSITIVE_VOTES as positiveVotes," +
            "NEGATIVE_VOTES as negativeVotes,TITLE as title,SCOPE as scope,NORMATIVE_REFERENCE as normativeReference,SYMBOLS_ABBREVIATED_TERMS as symbolsAbbreviatedTerms," +
            "CLAUSE as clause,SPECIAL as special,PROPOSAL_ID as proposalId FROM SD_ADOPTION_PROPOSAL_JUSTIFICATION  WHERE  STATUS='1'", nativeQuery = true)
    fun getApprovedISJustification(): MutableList<ISAdoptionProposalJustification>
}

interface ISAdoptionProposalRepository : JpaRepository<ISAdoptionProposal, Long> {
    fun findAllByOrderByIdDesc(): MutableList<ISAdoptionProposal>
    @Query(
        value = "SELECT ID as id, DOC_NAME as docName,TITLE as title,CIRCULATION_DATE as circulationDate,NAME_OF_ORGANIZATION AS nameOfOrganization,NAME_OF_RESPONDENT AS nameOfRespondent,DATE_PREPARED as preparedDate," +
                "PROPOSAL_NUMBER as proposalNumber,UPLOADED_BY as uploadedBy,REMARKS as remarks,ASSIGNED_TO as assignedTo,CLOSING_DATE AS closingDate,SCOPE as scope,TC_SEC_NAME AS tcSecName," +
                "ADOPTION_ACCEPTABLE_AS_PRESENTED AS adoptionAcceptableAsPresented,REASONS_FOR_NOT_ACCEPTANCE AS reasonsForNotAcceptance FROM SD_ADOPTION_PROPOSAL " +
                "WHERE  STATUS='0' ",
        nativeQuery = true
    )
    fun getProposalDetails(): MutableList<ProposalDetails>

    @Query(
        value = "SELECT ID as id, DOC_NAME as docName,TITLE as title,CIRCULATION_DATE as circulationDate,NAME_OF_ORGANIZATION AS nameOfOrganization,NAME_OF_RESPONDENT AS nameOfRespondent,DATE_PREPARED as preparedDate," +
                "PROPOSAL_NUMBER as proposalNumber,UPLOADED_BY as uploadedBy,REMARKS as remarks,ASSIGNED_TO as assignedTo,CLOSING_DATE AS closingDate,SCOPE as scope,TC_SEC_NAME AS tcSecName," +
                "ADOPTION_ACCEPTABLE_AS_PRESENTED AS adoptionAcceptableAsPresented,REASONS_FOR_NOT_ACCEPTANCE AS reasonsForNotAcceptance FROM SD_ADOPTION_PROPOSAL " +
                "WHERE  STATUS='1' ",
        nativeQuery = true
    )
    fun getApprovedProposals(): MutableList<ProposalDetails>
}

interface ISGazettementRepository : JpaRepository<ISGazettement, Long> {
}

interface ISGazetteNoticeRepository : JpaRepository<ISGazetteNotice, Long> {
}

interface ISUploadStandardRepository : JpaRepository<ISUploadStandard, Long> {
    fun findAllByOrderByIdDesc(): MutableList<ISUploadStandard>

    @Query(
        value = "SELECT ID as id, TITLE as title,SCOPE as scope,NORMATIVE_REFERENCE AS normativeReference,SYMBOLS_ABBREVIATED_TERMS AS symbolsAbbreviatedTerms,CLAUSE as clause," +
                "SPECIAL as special,INTERNATIONAL_STANDARD_NUMBER as iSNumber,cast(UPLOAD_DATE as varchar(200)) AS uploadDate,JUSTIFICATION_NUMBER as justificationNo,PROPOSAL_ID AS proposalId FROM SD_IS_STANDARD_TB " +
                "WHERE  STATUS='0' ",
        nativeQuery = true
    )
    fun getUploadedDraft(): MutableList<ISUploadedDraft>

    @Query(
        value = "SELECT ID as id, TITLE as title,SCOPE as scope,NORMATIVE_REFERENCE AS normativeReference,SYMBOLS_ABBREVIATED_TERMS AS symbolsAbbreviatedTerms,CLAUSE as clause," +
                "SPECIAL as special,INTERNATIONAL_STANDARD_NUMBER as iSNumber,cast(UPLOAD_DATE as varchar(200)) AS uploadDate,JUSTIFICATION_NUMBER as justificationNo,PROPOSAL_ID AS proposalId FROM SD_IS_STANDARD_TB " +
                "WHERE  STATUS='1' ",
        nativeQuery = true
    )
    fun getApprovedDraft(): MutableList<ISUploadedDraft>

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

    @Query(value = "SELECT max(CDN)  FROM SD_NWA_JUSTIFICATION", nativeQuery = true)
    fun getMaxCDN(): Long

    @Query(value = "SELECT  ID as id,cast(DATE_OF_MEETING as varchar(200)) AS dateOfMeeting,KNW_SEC as knwSec,SL_NUMBER as slNumber,REQUEST_NUMBER as requestNumber," +
            "REQUESTED_BY as requestedBy,ISSUES_ADDRESSED as issuesAddressed,cast(ACCEPTANCE_DATE as varchar(200)) AS acceptanceDate,REFERENCE_MATERIAL as referenceMaterial," +
            "REMARKS as remarks,cast(SUBMISSION_DATE as varchar(200)) AS submissionDate,KNW_COMMITTEE as knwCommittee,DEPARTMENT_NAME as departmentName " +
            "FROM SD_NWA_JUSTIFICATION  WHERE  JUSTIFICATION_STATUS='0'", nativeQuery = true)
    fun getNwaJustification(): MutableList<KnwaJustification>

    @Query(value = "SELECT  ID as id,cast(DATE_OF_MEETING as varchar(200)) AS dateOfMeeting,KNW_SEC as knwSec,SL_NUMBER as slNumber,REQUEST_NUMBER as requestNumber," +
            "REQUESTED_BY as requestedBy,ISSUES_ADDRESSED as issuesAddressed,cast(ACCEPTANCE_DATE as varchar(200)) AS acceptanceDate,REFERENCE_MATERIAL as referenceMaterial," +
            "REMARKS as remarks,cast(SUBMISSION_DATE as varchar(200)) AS submissionDate,KNW_COMMITTEE as knwCommittee,DEPARTMENT_NAME as departmentName " +
            "FROM SD_NWA_JUSTIFICATION  WHERE  JUSTIFICATION_STATUS='1'", nativeQuery = true)
    fun getApprovedJustification(): MutableList<KnwaJustification>

}

interface NwaPreliminaryDraftRepository : JpaRepository<NWAPreliminaryDraft, Long> {
    fun findAllByOrderByIdDesc(): MutableList<NWAPreliminaryDraft>

    @Query(value = "SELECT  ID as id,TITLE as title,DIJST_NUMBER as diJNumber,REMARKS as remarks,SCOPE as scope,NORMATIVE_REFERENCE as normativeReference," +
            "SYMBOLS_ABBREVIATED_TERMS as symbolsAbbreviatedTerms,CLAUSE as clause,cast(DATE_PD_PREPARED as varchar(200)) AS datePdPrepared," +
            "cast(WORK_SHOP_DATE as varchar(200)) AS workShopDate,SPECIAL as special,CD_APP_NUMBER as cdAppNumber,PREPARED_BY as preparedBy," +
            "JUSTIFICATION_NUMBER as justificationNumber FROM SD_NWA_PRELIMINARY_DRAFT  WHERE  STATUS='0'", nativeQuery = true)
    fun getPreliminaryDraft(): MutableList<NwaPDraft>

    @Query(value = "SELECT  ID as id,TITLE as title,DIJST_NUMBER as diJNumber,REMARKS as remarks,SCOPE as scope,NORMATIVE_REFERENCE as normativeReference," +
            "SYMBOLS_ABBREVIATED_TERMS as symbolsAbbreviatedTerms,CLAUSE as clause,cast(DATE_PD_PREPARED as varchar(200)) AS datePdPrepared," +
            "cast(WORK_SHOP_DATE as varchar(200)) AS workShopDate,SPECIAL as special,CD_APP_NUMBER as cdAppNumber,PREPARED_BY as preparedBy," +
            "JUSTIFICATION_NUMBER as justificationNumber FROM SD_NWA_PRELIMINARY_DRAFT  WHERE  STATUS='2'", nativeQuery = true)
    fun getRejectedPreliminaryDraft(): MutableList<NwaPDraft>

}

interface NwaStandardRepository : JpaRepository<NWAStandard, Long> {
    fun findAllByOrderByIdDesc(): MutableList<NWAStandard>
}

interface NwaWorkShopDraftRepository : JpaRepository<NWAWorkShopDraft, Long> {

}

interface StandardRepository : JpaRepository<Standard, Long> {
    @Query(value = "SELECT * FROM SD_STANDARD_TBL WHERE STATUS = 'review'", nativeQuery = true)
    fun reviewedStandards(): Collection<Standard?>?
    @Query(
        value = "SELECT ID as id,TITLE as title,SCOPE as scope,NORMATIVE_REFERENCE as normativeReference,SYMBOLS_ABBREVIATED_TERMS as symbolsAbbreviatedTerms,CLAUSE as clause," +
                "SPECIAL as special,STANDARD_NUMBER as standardNumber,STANDARD_TYPE as standardType,cast(DATE_FORMED as varchar(200)) AS dateFormed " +
                "FROM SD_STANDARD_TBL WHERE STATUS='2' ",
        nativeQuery = true
    )
    fun getStandardsForReview(): MutableList<ReviewStandards>

    @Query(value = "SELECT max(SDN)  FROM SD_STANDARD_TBL", nativeQuery = true)
    fun getMaxSDN(): Long

    fun findAllByOrderByIdDesc(): MutableList<Standard>
}

interface StandardReviewCommentsRepository : JpaRepository<StandardReviewComments, Long> {
}

interface StandardReviewFormRepository : JpaRepository<StandardReviewForm, String> {
    fun findByItemId(itemId: String): MutableList<StandardReviewForm>

}

interface StandardReviewRecommendationsRepository : JpaRepository<StandardReviewRecommendations, Long> {
}

interface StandardReviewRepository : JpaRepository<StandardReview, Long> {
    @Query(
        value = "SELECT ID as id,TITLE as title,SCOPE as scope,NORMATIVE_REFERENCE as normativeReference,SYMBOLS_ABBREVIATED_TERMS as symbolsAbbreviatedTerms,CLAUSE as clause," +
                "SPECIAL as special,STANDARD_NUMBER as standardNumber,STANDARD_TYPE as standardType " +
                "FROM SD_STANDARD_REVIEW  ",
        nativeQuery = true
    )
    fun getStandardsProposalForComment(): MutableList<ReviewStandards>

}
interface StandardReviewProposalCommentsRepository : JpaRepository<StandardReviewProposalComments, Long> {
    @Query(
        value = "SELECT ID as id,USER_NAME as userName,ADOPTION_COMMENT as adoptionComment,COMMENT_TIME as commentTime,PROPOSAL_ID as proposalId,TITLE as title," +
                "DOCUMENT_TYPE as documentType,CLAUSE as clause,PARAGRAPH as paragraph,TYPE_OF_COMMENT as typeOfComment,PROPOSED_CHANGE as proposedChange " +
                "FROM SD_REVIEW_PROPOSAL_COMMENTS  WHERE PROPOSAL_ID=:id",
        nativeQuery = true
    )
    fun getStandardsProposalComments(id: Long): MutableList<ReviewStandards>

}

interface StandardReviewProposalRecommendationsRepo: JpaRepository<StandardReviewProposalRecommendations, Long>{

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

    @Query(value = "SELECT u.FIRST_NAME AS FIRSTNAME,u.LAST_NAME AS LASTNAME,u.ID AS ID FROM DAT_KEBS_USERS u JOIN CFG_USER_ROLES_ASSIGNMENTS c ON u.ID=c.USER_ID " +
            "JOIN CFG_USER_ROLES r ON c.ROLE_ID=r.ID WHERE r.ROLE_NAME IN ('SL_ASSISTANT_MGR') ", nativeQuery = true)
    fun getApproveLevelOne(): List<UserDetailHolder>

    @Query(value = "SELECT u.FIRST_NAME AS FIRSTNAME,u.LAST_NAME AS LASTNAME,u.ID AS ID FROM DAT_KEBS_USERS u JOIN CFG_USER_ROLES_ASSIGNMENTS c ON u.ID=c.USER_ID " +
            "JOIN CFG_USER_ROLES r ON c.ROLE_ID=r.ID WHERE r.ROLE_NAME IN ('SL_MANAGER') ", nativeQuery = true)
    fun getApproveLevelTwo(): List<UserDetailHolder>

    @Query(value = "SELECT u.FIRST_NAME AS FIRSTNAME,u.LAST_NAME AS LASTNAME,u.ID AS ID FROM DAT_KEBS_USERS u JOIN CFG_USER_ROLES_ASSIGNMENTS c ON u.ID=c.USER_ID " +
            "JOIN CFG_USER_ROLES r ON c.ROLE_ID=r.ID WHERE r.ROLE_NAME IN ('SL_CHIEF_MANAGER') ", nativeQuery = true)
    fun getApproveLevelThree(): List<UserDetailHolder>

    @Query(value = "SELECT u.FIRST_NAME AS FIRSTNAME,u.LAST_NAME AS LASTNAME,u.ID AS ID FROM DAT_KEBS_USERS u JOIN CFG_USER_ROLES_ASSIGNMENTS c ON u.ID=c.USER_ID " +
            "JOIN CFG_USER_ROLES r ON c.ROLE_ID=r.ID WHERE r.ROLE_NAME IN ('SL_PL_OFFICER') ", nativeQuery = true)
    fun getAssignLevelOne(): List<UserDetailHolder>

    @Query(value = "SELECT u.FIRST_NAME AS FIRSTNAME,u.LAST_NAME AS LASTNAME,u.ID AS ID FROM DAT_KEBS_USERS u JOIN CFG_USER_ROLES_ASSIGNMENTS c ON u.ID=c.USER_ID " +
            "JOIN CFG_USER_ROLES r ON c.ROLE_ID=r.ID WHERE r.ROLE_NAME IN ('SL_PL_OFFICER','SL_ASSISTANT_MGR') ", nativeQuery = true)
    fun getAssignLevelTwo(): List<UserDetailHolder>

    @Query(value = "SELECT u.FIRST_NAME AS FIRSTNAME,u.LAST_NAME AS LASTNAME,u.ID AS ID FROM DAT_KEBS_USERS u JOIN CFG_USER_ROLES_ASSIGNMENTS c ON u.ID=c.USER_ID " +
            "JOIN CFG_USER_ROLES r ON c.ROLE_ID=r.ID WHERE r.ROLE_NAME IN ('SL_PL_OFFICER','SL_ASSISTANT_MGR','SL_MANAGER') ", nativeQuery = true)
    fun getAssignLevelThree(): List<UserDetailHolder>

    @Query(value = "SELECT * FROM DAT_KEBS_USERS  WHERE USER_TYPE IN ('61','62') ", nativeQuery = true)
    fun getSlLvTwoListv(): List<UsersEntity>

    @Query(value = "SELECT u.FIRST_NAME AS FIRSTNAME,u.LAST_NAME AS LASTNAME,u.ID AS ID FROM DAT_KEBS_USERS u JOIN CFG_USER_ROLES_ASSIGNMENTS c ON u.ID=c.USER_ID " +
            "JOIN CFG_USER_ROLES r ON c.ROLE_ID=r.ID WHERE r.ROLE_NAME IN ('KNW_SEC_SD','SD_TEST_ROLE') ", nativeQuery = true)
    fun getKnwSecretary(): List<UserDetailHolder>

    @Query(value = "SELECT u.FIRST_NAME AS FIRSTNAME,u.LAST_NAME AS LASTNAME,u.ID AS ID FROM DAT_KEBS_USERS u JOIN CFG_USER_ROLES_ASSIGNMENTS c ON u.ID=c.USER_ID " +
            "JOIN CFG_USER_ROLES r ON c.ROLE_ID=r.ID WHERE r.ROLE_NAME IN ('SPC_SEC_SD','SD_TEST_ROLE') ", nativeQuery = true)
    fun getSpcSecretary(): List<UserDetailHolder>

    @Query(value = "SELECT u.FIRST_NAME AS FIRSTNAME,u.LAST_NAME AS LASTNAME,u.ID AS ID FROM DAT_KEBS_USERS u JOIN CFG_USER_ROLES_ASSIGNMENTS c ON u.ID=c.USER_ID " +
            "JOIN CFG_USER_ROLES r ON c.ROLE_ID=r.ID WHERE r.ROLE_NAME IN ('DI_SDT_SD','SD_TEST_ROLE') ", nativeQuery = true)
    fun getDirector(): List<UserDetailHolder>

    @Query(value = "SELECT u.FIRST_NAME AS FIRSTNAME,u.LAST_NAME AS LASTNAME,u.ID AS ID FROM DAT_KEBS_USERS u JOIN CFG_USER_ROLES_ASSIGNMENTS c ON u.ID=c.USER_ID " +
            "JOIN CFG_USER_ROLES r ON c.ROLE_ID=r.ID WHERE r.ROLE_NAME IN ('HOP_SD','SD_TEST_ROLE') ", nativeQuery = true)
    fun getHeadOfPublishing(): List<UserDetailHolder>

    @Query(value = "SELECT u.FIRST_NAME AS FIRSTNAME,u.LAST_NAME AS LASTNAME,u.ID AS ID FROM DAT_KEBS_USERS u JOIN CFG_USER_ROLES_ASSIGNMENTS c ON u.ID=c.USER_ID " +
            "JOIN CFG_USER_ROLES r ON c.ROLE_ID=r.ID WHERE r.ROLE_NAME IN ('SAC_SEC_SD','SD_TEST_ROLE') ", nativeQuery = true)
    fun getSacSecretary(): List<UserDetailHolder>

    @Query(value = "SELECT u.FIRST_NAME AS FIRSTNAME,u.LAST_NAME AS LASTNAME,u.ID AS ID FROM DAT_KEBS_USERS u JOIN CFG_USER_ROLES_ASSIGNMENTS c ON u.ID=c.USER_ID " +
            "JOIN CFG_USER_ROLES r ON c.ROLE_ID=r.ID WHERE r.ROLE_NAME IN ('HO_SIC_SD','SD_TEST_ROLE') ", nativeQuery = true)
    fun getHeadOfSic(): List<UserDetailHolder>




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
interface StandardNwaRemarksRepository : JpaRepository<StandardNwaRemarks, Long> {

}
interface StandardComRemarksRepository : JpaRepository<StandardComRemarks, Long> {
    fun findAllByApprovalIDOrderByIdDesc(id: Long): List<StandardComRemarks>?
}
interface InternationalStandardRemarksRepository : JpaRepository<InternationalStandardRemarks, Long> {
    fun findAllByProposalIdOrderByIdDesc(id: Long): MutableIterable<InternationalStandardRemarks>?

}
interface ReviewStandardRemarksRepository : JpaRepository<ReviewStandardRemarks, Long> {
    fun findAllByProposalIdOrderByIdDesc(id: Long): MutableIterable<ReviewStandardRemarks>?
}


//interface UserNameRepository : JpaRepository<UsersEntity,Long>{
//    fun findByUserId(assignedTo: Long?) : MutableList<UsersEntity>
//    @Query("SELECT u.firstName,u.lastName FROM UsersEntity u WHERE u.id =:id")
//    fun findNameById(@Param("id") id: Long?): String
//}
