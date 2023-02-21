package org.kebs.app.kotlin.apollo.store.repo.std

import org.kebs.app.kotlin.apollo.store.model.UserRolesEntity
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface StandardRequestRepository:JpaRepository<StandardRequest, Long> {
    fun findAllByOrderByIdDesc(): MutableList<StandardRequest>
    fun findAllByStatusAndNwiStatusIsNull(status: String): List<StandardRequest>


    fun findAllById(id: Long): List<StandardRequest>

    @Query(
        value = "SELECT r.REQUEST_NUMBER as requestNumber,r.CREATED_ON as createdON,r.REQUESTOR_NAME as name,r.REQUESTOR_PHONE_NUMBER as phone,r.STATUS as status,u.FIRST_NAME as firstName,u.LAST_NAME as lastName " +
                "FROM SD_STANDARD_REQUEST r LEFT JOIN DAT_KEBS_USERS u ON r.TC_SEC_ASSIGNED=u.ID",nativeQuery = true)
    fun getReceivedStandardsReport(): MutableList<ReceivedStandards>

    @Query(
        value = "SELECT * FROM SD_STANDARD_REQUEST  WHERE LEVEL_OF_STANDARD='Kenya Standard' AND STATUS='Assigned To TC Sec' ORDER BY ID DESC",nativeQuery = true)
    fun getWorkshopStandards(): MutableList<StandardRequest>

    @Query(
        value = "SELECT * FROM SD_STANDARD_REQUEST  WHERE LEVEL_OF_STANDARD='Kenya Standard' AND STATUS='Workshop Justification Approval' ORDER BY ID DESC",nativeQuery = true)
    fun getWorkshopJustification(): MutableList<StandardRequest>

    @Query(
        value = "SELECT * FROM SD_STANDARD_REQUEST  WHERE LEVEL_OF_STANDARD='Kenya Standard' AND STATUS='Prepare Preliminary Draft' ORDER BY ID DESC",nativeQuery = true)
    fun getWorkshopForPDraft(): MutableList<StandardRequest>

    @Query(
        value = "SELECT * FROM SD_STANDARD_REQUEST  WHERE LEVEL_OF_STANDARD='Kenya Standard' AND STATUS='Make Changes to Preliminary Draft' ORDER BY ID DESC",nativeQuery = true)
    fun getWorkshopForEditing(): MutableList<StandardRequest>

    @Query(
        value= "SELECT d.ID as draftId,d.TITLE as title,d.SCOPE as scope,d.NORMATIVE_REFERENCE as normativeReference,d.SYMBOLS_ABBREVIATED_TERMS as symbolsAbbreviatedTerms," +
            "d.CLAUSE as clause,d.SPECIAL as special,d.UPLOAD_DATE as uploadDate,d.DEADLINE_DATE as deadlineDate,d.DRAFT_NUMBER as draftNumber,d.REMARKS as remarks," +
            "d.REQUEST_NUMBER as requestNumber,d.DEPARTMENT as departmentId,d.SUBJECT as subject,d.DESCRIPTION as description," +
            "d.STANDARD_TYPE as standardType,d.WORK_SHOP_DATE as workShopDate,r.ID as requestId, r.RANK as rank,r.REQUESTOR_NAME as name," +
            "r.REQUESTOR_PHONE_NUMBER as phone,r.REQUESTOR_EMAIL as email,r.SUBMISSION_DATE as submissionDate,r.PRODUCT_SUB_CATEGORY_ID as productSubCategoryId," +
            "r.TECHNICAL_COMMITTEE_ID as tcId,r.PRODUCT_ID as productId,r.ORGANISATION_NAME as organisationName," +
            "r.ECONOMIC_EFFICIENCY as economicEfficiency,r.HEALTH_SAFETY as healthSafety,r.ENVIRONMENT as environment,r.INTEGRATION as integration," +
            "r.EXPORT_MARKETS as exportMarkets,r.LEVEL_OF_STANDARD as levelOfStandard,r.NWA_CD_NUMBER as nwaCdNumber " +
            "FROM SD_STANDARD_REQUEST r LEFT JOIN SD_COM_STD_DRAFT d ON r.ID=d.REQUEST_ID WHERE d.STATUS=1 AND r.LEVEL_OF_STANDARD='Kenya Standard' AND r.STATUS='Make Changes to Preliminary Draft'",nativeQuery = true )

    fun getWorkShopDraftForEditing(): MutableList<NwaRequest>

    @Query(
        value = "SELECT * FROM SD_STANDARD_REQUEST  WHERE LEVEL_OF_STANDARD='Kenya Standard' AND STATUS='Preliminary Draft Prepared' ORDER BY ID DESC",nativeQuery = true)
    fun getPreparedPD(): MutableList<StandardRequest>





}
