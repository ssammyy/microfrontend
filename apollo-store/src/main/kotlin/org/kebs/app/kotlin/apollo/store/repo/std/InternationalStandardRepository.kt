package org.kebs.app.kotlin.apollo.store.repo.std

import org.kebs.app.kotlin.apollo.store.model.SlVisitUploadsEntity
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
import java.sql.Timestamp

@Repository
interface InternationalStandardRepository {
}

@Repository
interface ISAdoptionCommentsRepository : JpaRepository<ISAdoptionComments, Long> {
    fun findByProposalID(id: Long): MutableIterable<ISAdoptionComments>?
    @Query(
        value = "SELECT ID as id,USER_ID as userId,ADOPTION_PROPOSAL_COMMENT as adoptionComment,COMMENT_TIME as commentTime,PROPOSAL_ID as proposalId,TITLE as title," +
                "DOCUMENT_TYPE as documentType,CLAUSE as clause,ORGANIZATION as comNameOfOrganization,PARAGRAPH as paragraph,TYPE_OF_COMMENT as typeOfComment,PROPOSED_CHANGE as proposedChange," +
                "ADOPT_STANDARD as adopt,REASON as reasonsForNotAcceptance,RECOMMENDATIONS as recommendations,NAME_OF_RESPONDENT as nameOfRespondent,POSITION_OF_RESPONDENT as positionOfRespondent," +
                "NAME_OF_ORGANIZATION as nameOfOrganization,cast(DATE_OF_APPLICATION as varchar(200)) AS dateOfApplication,SCOPE as scope,OBSERVATION as observation  " +
                "FROM SD_ADOPTION_PROPOSAL_COMMENTS  WHERE PROPOSAL_ID=:id ORDER BY ID DESC",
        nativeQuery = true
    )
    fun getProposalComments(id: Long): MutableList<ISProposalComments>


}

interface ComJcJustificationRepository : JpaRepository<ComJcJustification, Long> {
}

interface ComJcJustificationUploadsRepository : JpaRepository<ComJcJustificationUploads, Long> {
}
interface ComStandardRequestUploadsRepository : JpaRepository<ComStandardRequestUploads, Long> {
    fun findAllByComStdRequestId(id: Long): ComStandardRequestUploads
}

interface CompanyStandardRepository : JpaRepository<CompanyStandard, Long> {
    fun findAllByOrderByIdDesc(): MutableList<CompanyStandard>

    @Query(
        value = "SELECT ID as id, TITLE as title,SCOPE as scope,NORMATIVE_REFERENCE AS normativeReference,SYMBOLS_ABBREVIATED_TERMS AS symbolsAbbreviatedTerms,CLAUSE as clause," +
                "SPECIAL as special,COMPANY_STANDARD_NUMBER as comStdNumber,DOCUMENT_TYPE as documentType,PREPARED_BY as preparedBy," +
                "cast(UPLOAD_DATE as varchar(200)) AS uploadDate,REQUEST_NUMBER AS requestNumber FROM SD_COM_STANDARD " +
                "WHERE  STATUS='0' ORDER BY ID DESC ",
        nativeQuery = true
    )
    fun getUploadedDraft(): MutableList<COMUploadedDraft>

    @Query(
        value = "SELECT s.ID as id, s.TITLE as title,s.SCOPE as scope,s.NORMATIVE_REFERENCE AS normativeReference,s.SYMBOLS_ABBREVIATED_TERMS AS symbolsAbbreviatedTerms,s.CLAUSE as clause," +
                "s.SPECIAL as special,s.COMPANY_STANDARD_NUMBER as comStdNumber,s.DOCUMENT_TYPE as documentType,s.PREPARED_BY as preparedBy," +
                "cast(s.UPLOAD_DATE as varchar(200)) AS uploadDate,s.REQUEST_NUMBER AS requestNumber,s.STATUS as status,s.REQUEST_ID as requestId,s.DRAFT_ID as draftId," +
                "s.DEPARTMENT as departmentId,d.NAME as departmentName,s.SUBJECT as subject,s.DESCRIPTION as description,s.CONTACT_ONE_FULL_NAME as contactOneFullName,s.CONTACT_ONE_TELEPHONE as contactOneTelephone,s.CONTACT_ONE_EMAIL as contactOneEmail,\n" +
                "s.CONTACT_TWO_FULL_NAME as contactTwoFullName,s.CONTACT_TWO_TELEPHONE as contactTwoTelephone,s.CONTACT_TWO_EMAIL as contactTwoEmail,s.CONTACT_THREE_FULL_NAME as contactThreeFullName,s.CONTACT_THREE_TELEPHONE as contactThreeTelephone,s.STANDARD_TYPE as standardType,\n" +
                "s.CONTACT_THREE_EMAIL as contactThreeEmail,s.COMPANY_NAME as companyName,s.COMPANY_PHONE as companyPhone FROM SD_COM_STANDARD s LEFT JOIN SD_DEPARTMENT d ON d.ID=s.DEPARTMENT " +
                "WHERE  s.STATUS IN('1','3','4','5','6','7') ORDER BY s.ID DESC",
        nativeQuery = true
    )
    fun getComStdPublishing(): MutableList<ComStandard>

    @Query(
        value = "SELECT s.ID as id, s.TITLE as title,s.SCOPE as scope,s.NORMATIVE_REFERENCE AS normativeReference,s.SYMBOLS_ABBREVIATED_TERMS AS symbolsAbbreviatedTerms,s.CLAUSE as clause," +
                "s.SPECIAL as special,s.COMPANY_STANDARD_NUMBER as comStdNumber,s.DOCUMENT_TYPE as documentType,s.PREPARED_BY as preparedBy," +
                "cast(s.UPLOAD_DATE as varchar(200)) AS uploadDate,s.REQUEST_NUMBER AS requestNumber,s.STATUS as status,s.REQUEST_ID as requestId,s.DRAFT_ID as draftId," +
                "s.DEPARTMENT as departmentId,d.NAME as departmentName,s.SUBJECT as subject,s.DESCRIPTION as description,s.CONTACT_ONE_FULL_NAME as contactOneFullName,s.CONTACT_ONE_TELEPHONE as contactOneTelephone,s.CONTACT_ONE_EMAIL as contactOneEmail,\n" +
                "s.CONTACT_TWO_FULL_NAME as contactTwoFullName,s.CONTACT_TWO_TELEPHONE as contactTwoTelephone,s.CONTACT_TWO_EMAIL as contactTwoEmail,s.CONTACT_THREE_FULL_NAME as contactThreeFullName,s.CONTACT_THREE_TELEPHONE as contactThreeTelephone,s.STANDARD_TYPE as standardType,\n" +
                "s.CONTACT_THREE_EMAIL as contactThreeEmail,s.COMPANY_NAME as companyName,s.COMPANY_PHONE as companyPhone FROM SD_COM_STANDARD s LEFT JOIN SD_DEPARTMENT d ON d.ID=s.DEPARTMENT " +
                "WHERE  s.STATUS =8 AND s.STANDARD_TYPE='International Standard' ORDER BY s.ID DESC",
        nativeQuery = true
    )
    fun getAppStdPublishing(): MutableList<ComStandard>

    @Query(
        value = "SELECT s.ID as id, s.TITLE as title,s.SCOPE as scope,s.NORMATIVE_REFERENCE AS normativeReference,s.SYMBOLS_ABBREVIATED_TERMS AS symbolsAbbreviatedTerms,s.CLAUSE as clause," +
                "s.SPECIAL as special,s.COMPANY_STANDARD_NUMBER as comStdNumber,s.DOCUMENT_TYPE as documentType,s.PREPARED_BY as preparedBy," +
                "cast(s.UPLOAD_DATE as varchar(200)) AS uploadDate,s.REQUEST_NUMBER AS requestNumber,s.STATUS as status,s.REQUEST_ID as requestId,s.DRAFT_ID as draftId," +
                "s.DEPARTMENT as departmentId,d.NAME as departmentName,s.SUBJECT as subject,s.DESCRIPTION as description,s.CONTACT_ONE_FULL_NAME as contactOneFullName,s.CONTACT_ONE_TELEPHONE as contactOneTelephone,s.CONTACT_ONE_EMAIL as contactOneEmail,\n" +
                "s.CONTACT_TWO_FULL_NAME as contactTwoFullName,s.CONTACT_TWO_TELEPHONE as contactTwoTelephone,s.CONTACT_TWO_EMAIL as contactTwoEmail,s.CONTACT_THREE_FULL_NAME as contactThreeFullName,s.CONTACT_THREE_TELEPHONE as contactThreeTelephone,s.STANDARD_TYPE as standardType,\n" +
                "s.CONTACT_THREE_EMAIL as contactThreeEmail,s.COMPANY_NAME as companyName,s.COMPANY_PHONE as companyPhone FROM SD_COM_STANDARD s LEFT JOIN SD_DEPARTMENT d ON d.ID=s.DEPARTMENT " +
                "WHERE  s.STATUS =9 AND s.STANDARD_TYPE='International Standard' ORDER BY s.ID DESC",
        nativeQuery = true
    )
    fun getAppStd(): MutableList<ComStandard>

    @Query(
        value = "SELECT s.ID as id, s.TITLE as title,s.SCOPE as scope,s.NORMATIVE_REFERENCE AS normativeReference,s.SYMBOLS_ABBREVIATED_TERMS AS symbolsAbbreviatedTerms,s.CLAUSE as clause," +
                "s.SPECIAL as special,s.COMPANY_STANDARD_NUMBER as comStdNumber,s.DOCUMENT_TYPE as documentType,s.PREPARED_BY as preparedBy," +
                "cast(s.UPLOAD_DATE as varchar(200)) AS uploadDate,s.REQUEST_NUMBER AS requestNumber,s.STATUS as status,s.REQUEST_ID as requestId,s.DRAFT_ID as draftId," +
                "s.DEPARTMENT as departmentId,d.NAME as departmentName,s.SUBJECT as subject,s.DESCRIPTION as description,s.CONTACT_ONE_FULL_NAME as contactOneFullName,s.CONTACT_ONE_TELEPHONE as contactOneTelephone,s.CONTACT_ONE_EMAIL as contactOneEmail,\n" +
                "s.CONTACT_TWO_FULL_NAME as contactTwoFullName,s.CONTACT_TWO_TELEPHONE as contactTwoTelephone,s.CONTACT_TWO_EMAIL as contactTwoEmail,s.CONTACT_THREE_FULL_NAME as contactThreeFullName,s.CONTACT_THREE_TELEPHONE as contactThreeTelephone,s.STANDARD_TYPE as standardType,\n" +
                "s.CONTACT_THREE_EMAIL as contactThreeEmail,s.COMPANY_NAME as companyName,s.COMPANY_PHONE as companyPhone FROM SD_COM_STANDARD s LEFT JOIN SD_DEPARTMENT d ON d.ID=s.DEPARTMENT " +
                "WHERE  s.STATUS=0 AND s.STANDARD_TYPE='Company Standard' ORDER BY s.ID DESC",
        nativeQuery = true
    )
    fun getComStdForEditing(): MutableList<ComStandard>

    @Query(
        value = "SELECT s.ID as id, s.TITLE as title,s.SCOPE as scope,s.NORMATIVE_REFERENCE AS normativeReference,s.SYMBOLS_ABBREVIATED_TERMS AS symbolsAbbreviatedTerms,s.CLAUSE as clause," +
                "s.SPECIAL as special,s.COMPANY_STANDARD_NUMBER as comStdNumber,s.DOCUMENT_TYPE as documentType,s.PREPARED_BY as preparedBy," +
                "cast(s.UPLOAD_DATE as varchar(200)) AS uploadDate,s.REQUEST_NUMBER AS requestNumber,s.STATUS as status,s.REQUEST_ID as requestId,s.DRAFT_ID as draftId," +
                "s.DEPARTMENT as departmentId,d.NAME as departmentName,s.SUBJECT as subject,s.DESCRIPTION as description,\n" +
                "s.STANDARD_TYPE as standardType " +
                "FROM SD_COM_STANDARD s LEFT JOIN SD_DEPARTMENT d ON d.ID=s.DEPARTMENT " +
                "WHERE  s.STATUS=0 AND s.STANDARD_TYPE='Kenya Standard' ORDER BY s.ID DESC",
        nativeQuery = true
    )
    fun getWorkShopStdForEditing(): MutableList<ComStandard>

    @Query(
        value = "SELECT s.ID as id, s.TITLE as title,s.SCOPE as scope,s.NORMATIVE_REFERENCE AS normativeReference,s.SYMBOLS_ABBREVIATED_TERMS AS symbolsAbbreviatedTerms,s.CLAUSE as clause," +
                "s.SPECIAL as special,s.COMPANY_STANDARD_NUMBER as comStdNumber,s.DOCUMENT_TYPE as documentType,s.PREPARED_BY as preparedBy," +
                "cast(s.UPLOAD_DATE as varchar(200)) AS uploadDate,s.REQUEST_NUMBER AS requestNumber,s.STATUS as status,s.REQUEST_ID as requestId,s.DRAFT_ID as draftId," +
                "s.DEPARTMENT as departmentId,d.NAME as departmentName,s.SUBJECT as subject,s.DESCRIPTION as description,\n" +
                "s.STANDARD_TYPE as standardType " +
                "FROM SD_COM_STANDARD s LEFT JOIN SD_DEPARTMENT d ON d.ID=s.DEPARTMENT " +
                "WHERE  s.STATUS=0  ORDER BY s.ID DESC",
        nativeQuery = true
    )
    fun getStdForEditing(): MutableList<ComStandard>



    @Query(
        value = "SELECT ID as id, TITLE as title,SCOPE as scope,NORMATIVE_REFERENCE AS normativeReference,SYMBOLS_ABBREVIATED_TERMS AS symbolsAbbreviatedTerms,CLAUSE as clause," +
                "SPECIAL as special,COMPANY_STANDARD_NUMBER as comStdNumber,DOCUMENT_TYPE as documentType,PREPARED_BY as preparedBy," +
                "cast(UPLOAD_DATE as varchar(200)) AS uploadDate,REQUEST_NUMBER AS requestNumber FROM SD_COM_STANDARD " +
                "WHERE  STATUS='1' ORDER BY ID DESC",
        nativeQuery = true
    )
    fun getApprovedEditedDraft(): MutableList<COMUploadedDraft>

    @Query(
        value = "SELECT ID as id, TITLE as title,SCOPE as scope,NORMATIVE_REFERENCE AS normativeReference,SYMBOLS_ABBREVIATED_TERMS AS symbolsAbbreviatedTerms,CLAUSE as clause," +
                "SPECIAL as special,COMPANY_STANDARD_NUMBER as comStdNumber,DOCUMENT_TYPE as documentType,PREPARED_BY as preparedBy," +
                "cast(UPLOAD_DATE as varchar(200)) AS uploadDate,REQUEST_NUMBER AS requestNumber FROM SD_COM_STANDARD " +
                "WHERE  STATUS='3' ORDER BY ID DESC ",
        nativeQuery = true
    )
    fun getComEditedDraft(): MutableList<COMUploadedDraft>

    @Query(
        value = "SELECT ID as id, TITLE as title,SCOPE as scope,NORMATIVE_REFERENCE AS normativeReference,SYMBOLS_ABBREVIATED_TERMS AS symbolsAbbreviatedTerms,CLAUSE as clause," +
                "SPECIAL as special,COMPANY_STANDARD_NUMBER as comStdNumber,DOCUMENT_TYPE as documentType,PREPARED_BY as preparedBy," +
                "cast(UPLOAD_DATE as varchar(200)) AS uploadDate,REQUEST_NUMBER AS requestNumber FROM SD_COM_STANDARD " +
                "WHERE  STATUS='4' ORDER BY ID DESC",
        nativeQuery = true
    )
    fun getDraughtedDraft(): MutableList<COMUploadedDraft>

    @Query(
        value = "SELECT ID as id, TITLE as title,SCOPE as scope,NORMATIVE_REFERENCE AS normativeReference,SYMBOLS_ABBREVIATED_TERMS AS symbolsAbbreviatedTerms,CLAUSE as clause," +
                "SPECIAL as special,COMPANY_STANDARD_NUMBER as comStdNumber,DOCUMENT_TYPE as documentType,PREPARED_BY as preparedBy," +
                "cast(UPLOAD_DATE as varchar(200)) AS uploadDate,REQUEST_NUMBER AS requestNumber FROM SD_COM_STANDARD " +
                "WHERE  STATUS='5' ORDER BY ID DESC",
        nativeQuery = true
    )
    fun getProofReadDraft(): MutableList<COMUploadedDraft>

    @Query(
        value = "SELECT ID as id, TITLE as title,SCOPE as scope,NORMATIVE_REFERENCE AS normativeReference,SYMBOLS_ABBREVIATED_TERMS AS symbolsAbbreviatedTerms,CLAUSE as clause," +
                "SPECIAL as special,COMPANY_STANDARD_NUMBER as comStdNumber,DOCUMENT_TYPE as documentType,PREPARED_BY as preparedBy," +
                "cast(UPLOAD_DATE as varchar(200)) AS uploadDate,REQUEST_NUMBER AS requestNumber FROM SD_COM_STANDARD " +
                "WHERE  STATUS='6' ORDER BY ID DESC",
        nativeQuery = true
    )
    fun getApprovedProofReadDraft(): MutableList<COMUploadedDraft>

    @Query(
        value = "SELECT ID as id, TITLE as title,SCOPE as scope,NORMATIVE_REFERENCE AS normativeReference,SYMBOLS_ABBREVIATED_TERMS AS symbolsAbbreviatedTerms,CLAUSE as clause," +
                "SPECIAL as special,COMPANY_STANDARD_NUMBER as comStdNumber,DOCUMENT_TYPE as documentType,PREPARED_BY as preparedBy," +
                "cast(UPLOAD_DATE as varchar(200)) AS uploadDate,REQUEST_NUMBER AS requestNumber FROM SD_COM_STANDARD " +
                "WHERE  STATUS='7' ORDER BY ID DESC",
        nativeQuery = true
    )
    fun getApprovedCompanyStdDraft(): MutableList<COMUploadedDraft>




    @Query("SELECT NVL (max(ID),0) as MaxId FROM SD_COM_STANDARD", nativeQuery = true)
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
        value = "SELECT u.EMAIL as userEmail,u.FIRST_NAME as firstName,u.LAST_NAME as lastName  FROM DAT_KEBS_USERS u LEFT JOIN CFG_USER_ROLES_ASSIGNMENTS a ON u.ID=a.USER_ID LEFT JOIN CFG_USER_ROLES r ON a.ROLE_ID=r.ID   WHERE r.ROLE_NAME='SD_HEAD_OF_ICT'",
        nativeQuery = true
    )
    fun getICTList(): MutableList<UserEmailListHolder>



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


    @Query(value = "SELECT r.ID as id,r.REQUEST_NUMBER as requestNumber,r.SUBMISSION_DATE as submissionDate,r.COMPANY_NAME as companyName," +
            "r.COMPANY_PHONE as companyPhone,r.COMPANY_EMAIL as companyEmail,t.NAME as tcName,d.NAME as departmentName,d.ID as departmentId,p.NAME as productName," +
            "s.NAME as productSubCategoryName,r.STATUS as status,r.SUBJECT as subject,r.DESCRIPTION as description,r.CONTACT_ONE_FULL_NAME as contactOneFullName," +
            "r.CONTACT_ONE_TELEPHONE as contactOneTelephone,r.CONTACT_ONE_EMAIL as contactOneEmail,r.CONTACT_TWO_FULL_NAME as contactTwoFullName," +
            "r.CONTACT_TWO_TELEPHONE as contactTwoTelephone,r.CONTACT_TWO_EMAIL as contactTwoEmail,r.CONTACT_THREE_FULL_NAME as contactThreeFullName," +
            "r.CONTACT_THREE_TELEPHONE as contactThreeTelephone,r.CONTACT_THREE_EMAIL as contactThreeEmail FROM SD_COM_STANDARD_REQUEST r LEFT JOIN SD_TECHNICAL_COMMITTEE t ON r.TC_ID=t.ID LEFT JOIN SD_DEPARTMENT d ON r.DEPARTMENT=d.ID LEFT JOIN SD_PRODUCTS p " +
            "ON r.PRODUCT=p.ID LEFT JOIN SD_PRODUCT_SUBCATEGORY s ON r.PRODUCT_SUB_CATEGORY=s.ID WHERE r.STATUS IN ('0','1','2') ORDER BY r.ID DESC", nativeQuery = true)
    fun getCompanyStandardRequest(): MutableList<ComStdRequest>

    @Query(value = "SELECT r.ID as id,r.REQUEST_NUMBER as requestNumber,r.SUBMISSION_DATE as submissionDate,r.COMPANY_NAME as companyName,r.STATUS as status," +
            "r.COMPANY_PHONE as companyPhone,r.COMPANY_EMAIL as companyEmail,t.NAME as tcName,d.NAME as departmentName,p.NAME as productName,s.NAME as productSubCategoryName " +
            "FROM SD_COM_STANDARD_REQUEST r LEFT JOIN SD_TECHNICAL_COMMITTEE t ON r.TC_ID=t.ID LEFT JOIN SD_DEPARTMENT d ON r.DEPARTMENT=d.ID LEFT JOIN SD_PRODUCTS p " +
            "ON r.PRODUCT=p.ID LEFT JOIN SD_PRODUCT_SUBCATEGORY s ON r.PRODUCT_SUB_CATEGORY=s.ID WHERE r.STATUS='1'  ", nativeQuery = true)
    fun getAssignedCompanyStandardRequest(): MutableList<ComStdRequest>

    @Query(value = "SELECT r.ID as id,r.REQUEST_NUMBER as requestNumber,r.SUBMISSION_DATE as submissionDate,r.COMPANY_NAME as companyName," +
            "r.COMPANY_PHONE as companyPhone,r.COMPANY_EMAIL as companyEmail,t.NAME as tcName,d.NAME as departmentName,d.ID as departmentId,p.NAME as productName," +
            "s.NAME as productSubCategoryName,r.STATUS as status,r.SUBJECT as subject,r.DESCRIPTION as description,r.CONTACT_ONE_FULL_NAME as contactOneFullName," +
            "r.CONTACT_ONE_TELEPHONE as contactOneTelephone,r.CONTACT_ONE_EMAIL as contactOneEmail,r.CONTACT_TWO_FULL_NAME as contactTwoFullName," +
            "r.CONTACT_TWO_TELEPHONE as contactTwoTelephone,r.CONTACT_TWO_EMAIL as contactTwoEmail,r.CONTACT_THREE_FULL_NAME as contactThreeFullName," +
            "r.CONTACT_THREE_TELEPHONE as contactThreeTelephone,r.CONTACT_THREE_EMAIL as contactThreeEmail FROM SD_COM_STANDARD_REQUEST r LEFT JOIN SD_TECHNICAL_COMMITTEE t ON r.TC_ID=t.ID LEFT JOIN SD_DEPARTMENT d ON r.DEPARTMENT=d.ID LEFT JOIN SD_PRODUCTS p " +
            "ON r.PRODUCT=p.ID LEFT JOIN SD_PRODUCT_SUBCATEGORY s ON r.PRODUCT_SUB_CATEGORY=s.ID WHERE r.STATUS IN ('0','1','2','3') ORDER BY r.ID DESC", nativeQuery = true)
    fun getCompanyStandardRequestProcess(): MutableList<ComStdRequest>



}
interface IStdStakeHoldersRepository : JpaRepository<IStandardStakeHolders, Long> {
    @Query(value = "SELECT NAME as name,EMAIL as email,TELEPHONE as telephone FROM SD_IS_STAKE_HOLDERS WHERE DRAFT_ID=:draftId  ", nativeQuery = true)
    fun getStakeHoldersList(@Param("draftId") draftId: Long?): MutableList<EmailList>
}

interface ComStdJointCommitteeRepository : JpaRepository<ComStandardJointCommittee, Long> {
    @Query(value = "SELECT NAME as name,EMAIL as email,TELEPHONE as telephone FROM DAT_KEBS_COM_JOINT_COMMITTEE WHERE REQUEST_ID=:requestId  ", nativeQuery = true)
    fun getCommitteeList(@Param("requestId") requestId: Long?): MutableList<EmailList>
}
interface ComStdActionRepository : JpaRepository<ComStdAction, Long> {
}

interface ComStdDraftRepository : JpaRepository<ComStdDraft, Long> {
    fun findAllByOrderByIdDesc(): MutableList<ComStdDraft>

    @Query("SELECT NVL (max(ID),0) as MaxId FROM SD_COM_STD_DRAFT WHERE STANDARD_TYPE='Company Standard'", nativeQuery = true)
    fun getMaxDraftId(): Long

    @Query("SELECT NVL (COMMENT_COUNT,0) as COMMENT_COUNT FROM SD_COM_STD_DRAFT WHERE ID=:draftID AND STANDARD_TYPE='Company Standard'", nativeQuery = true)
    fun getDraftCommentCount(@Param("draftID") draftID: Long?): Long

    @Query("SELECT NVL (COMMENT_COUNT,0) as COMMENT_COUNT FROM SD_COM_STD_DRAFT WHERE ID=:draftID AND STANDARD_TYPE='International Standard'", nativeQuery = true)
    fun getISDraftCommentCount(@Param("draftID") draftID: Long?): Long

    @Query("SELECT NVL (ADOPT,0) as ADOPT FROM SD_COM_STD_DRAFT WHERE ID=:draftID AND STANDARD_TYPE='International Standard'", nativeQuery = true)
    fun getISDraftAdoptCount(@Param("draftID") draftID: Long?): Long

    @Query("SELECT NVL (NOT_ADOPT,0) as NOT_ADOPT FROM SD_COM_STD_DRAFT WHERE ID=:draftID AND STANDARD_TYPE='International Standard'", nativeQuery = true)
    fun getISDraftNotAdoptCount(@Param("draftID") draftID: Long?): Long



    @Query(value = "SELECT * FROM SD_COM_STD_DRAFT WHERE ID=:comDraftID AND STATUS='0' AND STANDARD_TYPE='Company Standard' ORDER BY ID DESC", nativeQuery = true)
    fun getUploadedStdDraftForComment(@Param("comDraftID") comDraftID: Long? ): MutableList<ComStdDraft>

    @Query(value = "SELECT * FROM SD_COM_STD_DRAFT WHERE STATUS IN ('0') AND STANDARD_TYPE='Company Standard'  ORDER BY ID DESC", nativeQuery = true)
    fun getUploadedStdDraft(): MutableList<ComStdDraft>

    @Query(value = "SELECT * FROM SD_COM_STD_DRAFT WHERE ID=:comDraftID AND STATUS='1' AND STANDARD_TYPE='Company Standard' ORDER BY ID DESC ", nativeQuery = true)
    fun getApprovedStdDraft(@Param("comDraftID") comDraftID: Long?): MutableList<ComStdDraft>

    @Query(value = "SELECT * FROM SD_COM_STD_DRAFT WHERE STATUS='0' AND STANDARD_TYPE='Company Standard' ORDER BY ID DESC", nativeQuery = true)
    fun getStdDraftForEditing(): MutableList<ComStdDraft>

    @Query(value = "SELECT * FROM SD_COM_STD_DRAFT WHERE STATUS IN ('0') AND STANDARD_TYPE='Kenya Standard'  ORDER BY ID DESC", nativeQuery = true)
    fun getWorkShopStdDraft(): MutableList<ComStdDraft>


    @Query(value = "SELECT * FROM SD_COM_STD_DRAFT WHERE STATUS IN ('4') AND STANDARD_TYPE='Kenya Standard'  ORDER BY ID DESC", nativeQuery = true)
    fun getWorkShopStdDraftForEditing(): MutableList<ComStdDraft>



}
interface ComStandardDraftCommentsRepository : JpaRepository<ComDraftComments, Long> {
    fun findByDraftIDOrderByIdDesc(id: Long): MutableIterable<ComDraftComments>?

    @Query(
        value = "SELECT ID as id  FROM SD_COM_DRAFT_COMMENTS  WHERE DRAFT_ID= :id ",
        nativeQuery = true
    )
    fun findAllCommentsId(@Param("id") id: Long?): List<SiteVisitListHolder>
}



interface ComStandardDraftUploadsRepository : JpaRepository<ComStandardDraftUploads, Long> {
    fun findByComDraftDocumentId(id: Long): ComStandardDraftUploads
    fun findAllById(id: Long): ComStandardDraftUploads

    @Query(
        value = "SELECT ID as id  FROM SD_COM_STD_DRAFT_UPLOADS  WHERE COM_DRAFT_DOCUMENT_ID= :id ",
        nativeQuery = true
    )
    fun findAllDocumentId(@Param("id") id: Long?): List<SiteVisitListHolder>

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
            "CLAUSE as clause,SPECIAL as special,PROPOSAL_ID as proposalId FROM SD_ADOPTION_PROPOSAL_JUSTIFICATION  WHERE  DRAFT_ID=:id ORDER BY ID DESC", nativeQuery = true)
    fun getISJustification(id: Long?): MutableList<ISAdoptionProposalJustification>

    @Query(value = "SELECT  ID as id,MEETING_DATE AS meetingDate,TC as tcId,TC_SEC as tcSec,SL_NUMBER as slNumber," +
            "EDITION as edition,REQUEST_NUMBER as requestNumber,REQUESTED_BY AS requestedBy,ISSUES_ADDRESSED as issuesAddressed," +
            "TC_ACCEPTANCE_DATE as tcAcceptanceDate,REFERENCE_MATERIAL AS referenceMaterial,DEPARTMENT as department,REMARKS as remarks," +
            "cast(SUBMISSION_DATE as varchar(200)) AS submissionDate,TC_COMMITTEE as tcCommittee,DEPARTMENT_NAME as departmentName,POSITIVE_VOTES as positiveVotes," +
            "NEGATIVE_VOTES as negativeVotes,TITLE as title,SCOPE as scope,NORMATIVE_REFERENCE as normativeReference,SYMBOLS_ABBREVIATED_TERMS as symbolsAbbreviatedTerms," +
            "CLAUSE as clause,SPECIAL as special,PROPOSAL_ID as proposalId FROM SD_ADOPTION_PROPOSAL_JUSTIFICATION  WHERE  STATUS='1' ORDER BY ID DESC", nativeQuery = true)
    fun getApprovedISJustification(): MutableList<ISAdoptionProposalJustification>
}

interface ISAdoptionProposalRepository : JpaRepository<ISAdoptionProposal, Long> {
    fun findAllByOrderByIdDesc(): MutableList<ISAdoptionProposal>
    @Query(
        value = "SELECT p.ID as id, p.DOC_NAME as docName,p.TITLE as title,p.CIRCULATION_DATE as circulationDate,p.NAME_OF_ORGANIZATION AS nameOfOrganization,p.NAME_OF_RESPONDENT AS nameOfRespondent,p.DATE_PREPARED as preparedDate," +
                "p.PROPOSAL_NUMBER as proposalNumber,p.UPLOADED_BY as uploadedBy,p.REMARKS as remarks,p.ASSIGNED_TO as assignedTo,p.CLOSING_DATE AS closingDate,p.SCOPE as scope,p.TC_SEC_NAME AS tcSecName," +
                "p.ADOPTION_ACCEPTABLE_AS_PRESENTED AS adoptionAcceptableAsPresented,p.REASONS_FOR_NOT_ACCEPTANCE AS reasonsForNotAcceptance,p.STANDARD_NUMBER as standardNumber,p.DEADLINE_DATE as deadlineDate,d.COMMENT_COUNT as noOfComments," +
                "d.ID as draftId,d.DRAFT_NUMBER as draftNumber,d.title as draftTitle,d.COM_STANDARD_NUMBER as iStandardNumber,d.COMPANY_NAME as companyName,d.CONTACT_ONE_EMAIL as contactOneEmail," +
                "d.CONTACT_ONE_FULL_NAME as contactOneFullName,d.CONTACT_ONE_TELEPHONE as contactOneTelephone,p.ADOPTION_LINK as adoptionLink,d.ADOPT as voteFor,d.NOT_ADOPT as voteAgainst " +
                "FROM SD_ADOPTION_PROPOSAL p LEFT JOIN SD_COM_STD_DRAFT d ON p.ID=d.PROPOSAL_ID WHERE TRUNC(p.CIRCULATION_DATE) < TRUNC( SYSDATE ) AND  TRUNC(p.CLOSING_DATE) > TRUNC( SYSDATE ) AND  d.STATUS=0 AND d.STANDARD_TYPE='International Standard'  ORDER BY p.ID DESC",
        nativeQuery = true

    )
    fun getProposalDetails(): MutableList<ProposalDetails>

    @Query(
        value = "SELECT p.ID as id, p.DOC_NAME as docName,p.TITLE as title,p.CIRCULATION_DATE as circulationDate,p.NAME_OF_ORGANIZATION AS nameOfOrganization,p.NAME_OF_RESPONDENT AS nameOfRespondent,p.DATE_PREPARED as preparedDate," +
                "p.PROPOSAL_NUMBER as proposalNumber,p.UPLOADED_BY as uploadedBy,p.REMARKS as remarks,p.ASSIGNED_TO as assignedTo,p.CLOSING_DATE AS closingDate,p.SCOPE as scope,p.TC_SEC_NAME AS tcSecName,p.TC_SEC_NAME as tcSecEmail," +
                "p.ADOPTION_ACCEPTABLE_AS_PRESENTED AS adoptionAcceptableAsPresented,p.REASONS_FOR_NOT_ACCEPTANCE AS reasonsForNotAcceptance,p.STANDARD_NUMBER as standardNumber,p.DEADLINE_DATE as deadlineDate,d.COMMENT_COUNT as noOfComments," +
                "d.ID as draftId,d.DRAFT_NUMBER as draftNumber,d.title as draftTitle,d.COM_STANDARD_NUMBER as iStandardNumber,d.COMPANY_NAME as companyName,d.CONTACT_ONE_EMAIL as contactOneEmail,d.ADOPT as voteFor,d.NOT_ADOPT as voteAgainst," +
                "d.CONTACT_ONE_FULL_NAME as contactOneFullName,d.CONTACT_ONE_TELEPHONE as contactOneTelephone,p.ADOPTION_LINK as adoptionLink FROM SD_ADOPTION_PROPOSAL p LEFT JOIN SD_COM_STD_DRAFT d ON p.ID=d.PROPOSAL_ID WHERE TRUNC(p.CIRCULATION_DATE) < TRUNC( SYSDATE ) AND  TRUNC(p.CLOSING_DATE) > TRUNC( SYSDATE ) AND d.ID=:id AND d.STATUS=0 AND d.STANDARD_TYPE='International Standard'  ORDER BY p.ID DESC",
        nativeQuery = true
    )
    fun getProposals(id: Long): MutableList<ProposalDetails>

    @Query(
        value = "SELECT p.ID as id, p.DOC_NAME as docName,p.TITLE as title,p.CIRCULATION_DATE as circulationDate,p.NAME_OF_ORGANIZATION AS nameOfOrganization,p.NAME_OF_RESPONDENT AS nameOfRespondent,p.DATE_PREPARED as preparedDate," +
                "p.PROPOSAL_NUMBER as proposalNumber,p.UPLOADED_BY as uploadedBy,p.REMARKS as remarks,p.ASSIGNED_TO as assignedTo,p.CLOSING_DATE AS closingDate,p.SCOPE as scope,p.TC_SEC_NAME AS tcSecName,p.TC_SEC_NAME as tcSecEmail," +
                "p.ADOPTION_ACCEPTABLE_AS_PRESENTED AS adoptionAcceptableAsPresented,p.REASONS_FOR_NOT_ACCEPTANCE AS reasonsForNotAcceptance,p.STANDARD_NUMBER as standardNumber,p.DEADLINE_DATE as deadlineDate,d.COMMENT_COUNT as noOfComments," +
                "d.ID as draftId,d.DRAFT_NUMBER as draftNumber,d.title as draftTitle,d.COM_STANDARD_NUMBER as iStandardNumber,d.COMPANY_NAME as companyName,d.CONTACT_ONE_EMAIL as contactOneEmail,d.ADOPT as voteFor,d.NOT_ADOPT as voteAgainst," +
                "d.CONTACT_ONE_FULL_NAME as contactOneFullName,d.CONTACT_ONE_TELEPHONE as contactOneTelephone FROM SD_ADOPTION_PROPOSAL p LEFT JOIN SD_COM_STD_DRAFT d ON p.ID=d.PROPOSAL_ID WHERE  d.STATUS=1 AND d.STANDARD_TYPE='International Standard'  ORDER BY p.ID DESC",
        nativeQuery = true
    )

    fun getApprovedProposals(): MutableList<ProposalDetails>

    @Query(
        value = "SELECT p.ID as id, p.DOC_NAME as docName,p.TITLE as title,p.CIRCULATION_DATE as circulationDate,p.NAME_OF_ORGANIZATION AS nameOfOrganization,p.NAME_OF_RESPONDENT AS nameOfRespondent,p.DATE_PREPARED as preparedDate," +
                "p.PROPOSAL_NUMBER as proposalNumber,p.UPLOADED_BY as uploadedBy,p.REMARKS as remarks,p.ASSIGNED_TO as assignedTo,p.CLOSING_DATE AS closingDate,p.SCOPE as scope,p.TC_SEC_NAME AS tcSecName," +
                "p.ADOPTION_ACCEPTABLE_AS_PRESENTED AS adoptionAcceptableAsPresented,p.REASONS_FOR_NOT_ACCEPTANCE AS reasonsForNotAcceptance,p.STANDARD_NUMBER as standardNumber,p.DEADLINE_DATE as deadlineDate,d.COMMENT_COUNT as noOfComments," +
                "d.ID as draftId,d.DRAFT_NUMBER as draftNumber,d.title as draftTitle,d.COM_STANDARD_NUMBER as iStandardNumber,d.COMPANY_NAME as companyName,d.CONTACT_ONE_EMAIL as contactOneEmail,d.ADOPT as voteFor,d.NOT_ADOPT as voteAgainst," +
                "d.CONTACT_ONE_FULL_NAME as contactOneFullName,d.CONTACT_ONE_TELEPHONE as contactOneTelephone FROM SD_ADOPTION_PROPOSAL p LEFT JOIN SD_COM_STD_DRAFT d ON p.ID=d.PROPOSAL_ID WHERE  d.STATUS=3 AND d.STANDARD_TYPE='International Standard'  ORDER BY p.ID DESC",
        nativeQuery = true
    )

    fun getISJustification(): MutableList<ProposalDetails>

    @Query(
        value = "SELECT p.ID as id, p.DOC_NAME as docName,p.TITLE as title,p.CIRCULATION_DATE as circulationDate,p.NAME_OF_ORGANIZATION AS nameOfOrganization,p.NAME_OF_RESPONDENT AS nameOfRespondent,p.DATE_PREPARED as preparedDate," +
                "p.PROPOSAL_NUMBER as proposalNumber,p.UPLOADED_BY as uploadedBy,p.REMARKS as remarks,p.ASSIGNED_TO as assignedTo,p.CLOSING_DATE AS closingDate,p.SCOPE as scope,p.TC_SEC_NAME AS tcSecName," +
                "p.ADOPTION_ACCEPTABLE_AS_PRESENTED AS adoptionAcceptableAsPresented,p.REASONS_FOR_NOT_ACCEPTANCE AS reasonsForNotAcceptance,p.STANDARD_NUMBER as standardNumber,p.DEADLINE_DATE as deadlineDate,d.COMMENT_COUNT as noOfComments," +
                "d.ID as draftId,d.DRAFT_NUMBER as draftNumber,d.title as draftTitle,d.COM_STANDARD_NUMBER as iStandardNumber,d.COMPANY_NAME as companyName,d.CONTACT_ONE_EMAIL as contactOneEmail,d.ADOPT as voteFor,d.NOT_ADOPT as voteAgainst," +
                "d.CONTACT_ONE_FULL_NAME as contactOneFullName,d.CONTACT_ONE_TELEPHONE as contactOneTelephone FROM SD_ADOPTION_PROPOSAL p LEFT JOIN SD_COM_STD_DRAFT d ON p.ID=d.PROPOSAL_ID WHERE  d.STATUS=4 AND d.STANDARD_TYPE='International Standard'  ORDER BY p.ID DESC",
        nativeQuery = true
    )

    fun getApprovedJustification(): MutableList<ProposalDetails>

    @Query("SELECT NVL (NUMBER_OF_COMMENTS,0) as NUMBER_OF_COMMENTS FROM SD_ADOPTION_PROPOSAL WHERE ID=:proposalID", nativeQuery = true)
    fun getCommentCount(@Param("proposalID") proposalID: Long?): Long
}

interface ISGazettementRepository : JpaRepository<ISGazettement, Long> {
}

interface ISGazetteNoticeRepository : JpaRepository<ISGazetteNotice, Long> {
}

interface ISUploadStandardRepository : JpaRepository<ISUploadStandard, Long> {
    fun findAllByOrderByIdDesc(): MutableList<ISUploadStandard>

    @Query(
        value = "SELECT ID as id, TITLE as title,SCOPE as scope,NORMATIVE_REFERENCE AS normativeReference,SYMBOLS_ABBREVIATED_TERMS AS symbolsAbbreviatedTerms,CLAUSE as clause," +
                "SPECIAL as special,INTERNATIONAL_STANDARD_NUMBER as iSNumber,DOCUMENT_TYPE as documentType,PREPARED_BY as preparedBy,cast(UPLOAD_DATE as varchar(200)) AS uploadDate," +
                "JUSTIFICATION_NUMBER as justificationNo,PROPOSAL_ID AS proposalId,DEADLINE_DATE as deadLine FROM SD_IS_STANDARD_TB " +
                "WHERE  STATUS IN('0','1','3','4','5','6','7') ORDER BY ID DESC",
        nativeQuery = true
    )
    fun getIsPublishingTasks(): MutableList<ISUploadedDraft>

    @Query(
        value = "SELECT ID as id, TITLE as title,SCOPE as scope,NORMATIVE_REFERENCE AS normativeReference,SYMBOLS_ABBREVIATED_TERMS AS symbolsAbbreviatedTerms,CLAUSE as clause," +
                "SPECIAL as special,INTERNATIONAL_STANDARD_NUMBER as iSNumber,DOCUMENT_TYPE as documentType,PREPARED_BY as preparedBy,cast(UPLOAD_DATE as varchar(200)) AS uploadDate,JUSTIFICATION_NUMBER as justificationNo,PROPOSAL_ID AS proposalId FROM SD_IS_STANDARD_TB " +
                "WHERE  STATUS='0' ORDER BY ID DESC",
        nativeQuery = true
    )
    fun getUploadedDraft(): MutableList<ISUploadedDraft>

    @Query(
        value = "SELECT ID as id, TITLE as title,SCOPE as scope,NORMATIVE_REFERENCE AS normativeReference,SYMBOLS_ABBREVIATED_TERMS AS symbolsAbbreviatedTerms,CLAUSE as clause," +
                "SPECIAL as special,INTERNATIONAL_STANDARD_NUMBER as iSNumber,cast(UPLOAD_DATE as varchar(200)) AS uploadDate,JUSTIFICATION_NUMBER as justificationNo,PROPOSAL_ID AS proposalId," +
                "DOCUMENT_TYPE as documentType,PREPARED_BY as preparedBy FROM SD_IS_STANDARD_TB " +
                "WHERE  STATUS='1' ORDER BY ID DESC",
        nativeQuery = true
    )
    fun getApprovedDraft(): MutableList<ISUploadedDraft>

    @Query(
        value = "SELECT ID as id, TITLE as title,SCOPE as scope,NORMATIVE_REFERENCE AS normativeReference,SYMBOLS_ABBREVIATED_TERMS AS symbolsAbbreviatedTerms,CLAUSE as clause," +
                "SPECIAL as special,DOCUMENT_TYPE as documentType,PREPARED_BY as preparedBy,INTERNATIONAL_STANDARD_NUMBER as iSNumber,cast(UPLOAD_DATE as varchar(200)) AS uploadDate,JUSTIFICATION_NUMBER as justificationNo,PROPOSAL_ID AS proposalId FROM SD_IS_STANDARD_TB " +
                "WHERE  STATUS='3' ORDER BY ID DESC",
        nativeQuery = true
    )
    fun getEditedDraft(): MutableList<ISUploadedDraft>

    @Query(
        value = "SELECT ID as id, TITLE as title,SCOPE as scope,NORMATIVE_REFERENCE AS normativeReference,SYMBOLS_ABBREVIATED_TERMS AS symbolsAbbreviatedTerms,CLAUSE as clause," +
                "SPECIAL as special,DOCUMENT_TYPE as documentType,PREPARED_BY as preparedBy,INTERNATIONAL_STANDARD_NUMBER as iSNumber,cast(UPLOAD_DATE as varchar(200)) AS uploadDate,JUSTIFICATION_NUMBER as justificationNo,PROPOSAL_ID AS proposalId FROM SD_IS_STANDARD_TB " +
                "WHERE  STATUS='4' ORDER BY ID DESC",
        nativeQuery = true
    )
    fun getDraughtedDraft(): MutableList<ISUploadedDraft>

    @Query(
        value = "SELECT ID as id, TITLE as title,SCOPE as scope,NORMATIVE_REFERENCE AS normativeReference,SYMBOLS_ABBREVIATED_TERMS AS symbolsAbbreviatedTerms,CLAUSE as clause," +
                "SPECIAL as special,DOCUMENT_TYPE as documentType,PREPARED_BY as preparedBy,INTERNATIONAL_STANDARD_NUMBER as iSNumber,cast(UPLOAD_DATE as varchar(200)) AS uploadDate,JUSTIFICATION_NUMBER as justificationNo,PROPOSAL_ID AS proposalId FROM SD_IS_STANDARD_TB " +
                "WHERE  STATUS='5' ORDER BY ID DESC",
        nativeQuery = true
    )
    fun getProofReadDraft(): MutableList<ISUploadedDraft>

    @Query(
        value = "SELECT ID as id, TITLE as title,SCOPE as scope,NORMATIVE_REFERENCE AS normativeReference,SYMBOLS_ABBREVIATED_TERMS AS symbolsAbbreviatedTerms,CLAUSE as clause," +
                "SPECIAL as special,DOCUMENT_TYPE as documentType,PREPARED_BY as preparedBy,INTERNATIONAL_STANDARD_NUMBER as iSNumber,cast(UPLOAD_DATE as varchar(200)) AS uploadDate,JUSTIFICATION_NUMBER as justificationNo,PROPOSAL_ID AS proposalId FROM SD_IS_STANDARD_TB " +
                "WHERE  STATUS='6' ORDER BY ID DESC",
        nativeQuery = true
    )
    fun getApprovedProofReadDraft(): MutableList<ISUploadedDraft>

    @Query(
        value = "SELECT ID as id, TITLE as title,SCOPE as scope,NORMATIVE_REFERENCE AS normativeReference,SYMBOLS_ABBREVIATED_TERMS AS symbolsAbbreviatedTerms,CLAUSE as clause," +
                "SPECIAL as special,DOCUMENT_TYPE as documentType,PREPARED_BY as preparedBy,INTERNATIONAL_STANDARD_NUMBER as iSNumber,cast(UPLOAD_DATE as varchar(200)) AS uploadDate,JUSTIFICATION_NUMBER as justificationNo,PROPOSAL_ID AS proposalId FROM SD_IS_STANDARD_TB " +
                "WHERE  STATUS='7' ORDER BY ID DESC",
        nativeQuery = true
    )
    fun getApprovedEditedDraft(): MutableList<ISUploadedDraft>



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

    @Query(
        value = "SELECT * FROM SD_NWA_JUSTIFICATION  WHERE REQUEST_ID=:requestId ",nativeQuery = true)
    fun getJustification(requestId: Long): MutableList<NWAJustification>

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

    @Query(
        value = "SELECT ID as id,TITLE as title,SCOPE as scope,NORMATIVE_REFERENCE as normativeReference,SYMBOLS_ABBREVIATED_TERMS as symbolsAbbreviatedTerms,CLAUSE as clause," +
                "SPECIAL as special,STANDARD_NUMBER as standardNumber,STANDARD_TYPE as standardType,cast(DATE_FORMED as varchar(200)) AS dateFormed " +
                "FROM SD_STANDARD_TBL WHERE STATUS='3' ",
        nativeQuery = true
    )
    fun getStandardsForRecommendation(): MutableList<ReviewStandards>

    @Query(
        value = "SELECT ID as id,TITLE as title,SCOPE as scope,NORMATIVE_REFERENCE as normativeReference,SYMBOLS_ABBREVIATED_TERMS as symbolsAbbreviatedTerms,CLAUSE as clause," +
                "SPECIAL as special,STANDARD_NUMBER as standardNumber,STANDARD_TYPE as standardType,cast(DATE_FORMED as varchar(200)) AS dateFormed " +
                "FROM SD_STANDARD_TBL WHERE STATUS='4' ",
        nativeQuery = true
    )
    fun getStandardsForSpcAction(): MutableList<ReviewStandards>

    @Query(value = "SELECT NVL (max(SDN),0) as SDN  FROM SD_STANDARD_TBL", nativeQuery = true)
    fun getMaxSDN(): Long

    @Query(value = "SELECT NVL (max(ISDN),0) as ISDN  FROM SD_STANDARD_TBL", nativeQuery = true)
    fun getMaxISDN(): Long

    fun findAllByOrderByIdDesc(): MutableList<Standard>

    @Query(
        value = "SELECT ID as id, TITLE as title,SCOPE as scope,NORMATIVE_REFERENCE AS normativeReference,SYMBOLS_ABBREVIATED_TERMS AS symbolsAbbreviatedTerms,CLAUSE as clause," +
                "SPECIAL as special,STANDARD_NUMBER as iSNumber,cast(DATE_FORMED as varchar(200)) AS uploadDate FROM SD_STANDARD_TBL " +
                "WHERE  STATUS='0' AND STANDARD_TYPE='International Standard' ",
        nativeQuery = true
    )
    fun getStandardForGazettement(): MutableList<ISUploadedDraft>

    @Query(value = "SELECT * FROM SD_STANDARD_TBL WHERE STANDARD_TYPE='International Standard' AND STATUS=0", nativeQuery = true)
    fun getInternationalStandards(): MutableList<Standard>

    @Query(value = "SELECT * FROM SD_STANDARD_TBL WHERE STANDARD_TYPE='Company Standard' AND STATUS=0", nativeQuery = true)
    fun getCompanyStandards(): MutableList<Standard>

    @Query(value = "SELECT * FROM SD_STANDARD_TBL WHERE  STATUS=0", nativeQuery = true)
    fun getStandards(): MutableList<Standard>
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

    @Query(value = "SELECT * FROM SD_STANDARD_REVIEW WHERE STATUS=0",nativeQuery = true)
    fun getReviewProposalForComment(): MutableList<StandardReview>

    @Query(value = "SELECT * FROM SD_STANDARD_REVIEW WHERE ID=:id AND STATUS=0",nativeQuery = true)
    fun getReviewProposalToComment(id: Long): MutableList<StandardReview>

    @Query(value = "SELECT * FROM SD_STANDARD_REVIEW WHERE STATUS=0",nativeQuery = true)
    fun getStandardsForRecommendation(): MutableList<StandardReview>

    @Query(value = "SELECT * FROM SD_STANDARD_REVIEW WHERE STATUS=1",nativeQuery = true)
    fun getStandardsForSpcAction(): MutableList<StandardReview>


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

    @Query(value = "SELECT (FIRST_NAME || ' '|| LAST_NAME) AS NAME,ID AS ID,EMAIL as EMAIL " +
            "FROM DAT_KEBS_USERS ", nativeQuery = true)
    fun getUsers(): MutableList<UserHolder>

    @Query(value = "SELECT u.FIRST_NAME AS FIRSTNAME,u.LAST_NAME AS LASTNAME,u.ID AS ID,u.EMAIL as EMAIL " +
            "FROM DAT_KEBS_USERS u ", nativeQuery = true)
    fun getUserList(): MutableList<UserDetailHolder>

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

    @Query(
        "SELECT  (FIRST_NAME || ' '|| LAST_NAME) AS NAME,u.ID as id,u.EMAIL as email,u.CELL_PHONE as telephone from APOLLO.CFG_USER_ROLES_ASSIGNMENTS r,APOLLO.DAT_KEBS_USERS u where  u.ID = r.USER_ID and r.ROLE_ID = 2522",
        nativeQuery = true
    )
    // Check for users who have sd access: Role Id:2522
    fun findStandardStakeholders(): List<UserDetailHolder>?






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

interface CompanyStandardRemarksRepository : JpaRepository<CompanyStandardRemarks, Long> {
    @Query("SELECT * FROM DAT_KEBS_COM_STD_REMARKS WHERE REQUEST_ID=:id ORDER BY ID DESC", nativeQuery = true)
    fun findCommentsOnDraft(id: Long): MutableIterable<CompanyStandardRemarks>?

    @Query("SELECT * FROM DAT_KEBS_COM_STD_REMARKS WHERE REQUEST_ID=:id AND STANDARD_TYPE='International Standard' ORDER BY ID DESC", nativeQuery = true)
    fun getDraftComments(id: Long): MutableIterable<CompanyStandardRemarks>?



    fun findByRequestIdOrderByIdDesc(id: Long): MutableIterable<CompanyStandardRemarks>?
}

interface ComContactDetailsRepository : JpaRepository<ComContactDetails, Long> {
    fun findByRequestIdOrderByIdDesc(id: Long): MutableList<ComContactDetails>?
    @Query(value = "SELECT FULL_NAME as name,EMAIL as email,TELEPHONE as telephone FROM SD_COM_STD_CONTACT_DETAILS WHERE REQUEST_ID=:requestId  ", nativeQuery = true)
    fun getComContactList(@Param("requestId") requestId: Long?): MutableList<EmailList>
}
interface SDWorkshopStdRepository : JpaRepository<SDWorkshopStd, Long> {
    fun findAllByOrderByIdDesc(): MutableList<SDWorkshopStd>

}

interface SDReviewCommentsRepository : JpaRepository<SDReviewComments, Long> {
    @Query(value = "SELECT * FROM SD_REVIEW_COMMENTS WHERE REVIEW_ID=:id ORDER BY ID DESC",nativeQuery = true)
    fun getProposalsComments(@Param("id") id: Long?): MutableList<SDReviewComments>
}



//interface UserNameRepository : JpaRepository<UsersEntity,Long>{
//    fun findByUserId(assignedTo: Long?) : MutableList<UsersEntity>
//    @Query("SELECT u.firstName,u.lastName FROM UsersEntity u WHERE u.id =:id")
//    fun findNameById(@Param("id") id: Long?): String
//}
