package org.kebs.app.kotlin.apollo.store.repo.std

import org.kebs.app.kotlin.apollo.store.model.std.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface CommentsRepository : JpaRepository<Comments, Long> {
    //gets user logged in comments on preliminary drafts
    @Query(
        "SELECT t.ID AS commentsID,t.TITLE,t.DOCUMENT_TYPE, cast(t.CIRCULATION_DATE as varchar(200)) AS CIRCULATION_DATE ,cast(t.CLOSING_DATE as varchar(200)) AS CLOSING_DATE,t.STATUS,t.ORGANIZATION,t.CLAUSE,t.PARAGRAPH,t.COMMENT_TYPE,t.PROPOSED_CHANGE,t.OBSERVATION,cast(t.CREATED_ON as varchar(200)) AS CREATED_ON,t.COMMENTS_MADE,d.PD_NAME,(dku.FIRST_NAME || ' ' || dku.LAST_NAME)  AS COMMENT_BY,(dku.FIRST_NAME || ' ' || dku.LAST_NAME) AS RECEIVED_BY, t.RECIPIENT_ID FROM SD_COMMENTS t Join COMMITTEEPD d ON t.PD_ID = d.ID join DAT_KEBS_USERS DKU on t.CREATED_BY = DKU.ID  WHERE t.STATUS = 1 and t.CREATED_BY=:loggedInUserId ORDER BY t.ID DESC",
        nativeQuery = true
    )
    fun getUserLoggedInCommentsOnPreliminaryDraft(@Param("loggedInUserId") loggedInUserId: Long): List<CommentsWithPdId>


    //gets comments based on Preliminary Draft
    @Query(
        "SELECT t.ID AS commentsID,t.TITLE,t.DOCUMENT_TYPE, cast(t.CIRCULATION_DATE as varchar(200)) AS CIRCULATION_DATE ,cast(t.CLOSING_DATE as varchar(200)) AS CLOSING_DATE,t.STATUS,t.ORGANIZATION,t.CLAUSE,t.PARAGRAPH,t.COMMENT_TYPE,t.PROPOSED_CHANGE,t.OBSERVATION,cast(t.CREATED_ON as varchar(200)) AS CREATED_ON,t.COMMENTS_MADE,d.PD_NAME,(dku.FIRST_NAME || ' ' || dku.LAST_NAME)  AS COMMENT_BY,(DKUV.FIRST_NAME || ' ' || dku.LAST_NAME) AS RECEIVED_BY, t.RECIPIENT_ID FROM SD_COMMENTS t Join CommitteePD d ON t.PD_ID = d.ID join DAT_KEBS_USERS DKU on t.CREATED_BY = DKU.ID join DAT_KEBS_USERS DKUV on t.RECIPIENT_ID = DKUV.ID WHERE t.STATUS = 1 and t.PD_ID=:pdId ORDER BY t.ID DESC",
        nativeQuery = true
    )
    fun findByPdId(@Param("pdId") pdId: Long): List<CommentsWithPdId>

    //gets all comments on all Preliminary Drafts
    @Query(
        "SELECT t.ID AS commentsID,t.TITLE,t.DOCUMENT_TYPE, cast(t.CIRCULATION_DATE as varchar(200)) AS CIRCULATION_DATE ,cast(t.CLOSING_DATE as varchar(200)) AS CLOSING_DATE,t.STATUS,t.ORGANIZATION,t.CLAUSE,t.PARAGRAPH,t.COMMENT_TYPE,t.PROPOSED_CHANGE,t.OBSERVATION,cast(t.CREATED_ON as varchar(200)) AS CREATED_ON,t.COMMENTS_MADE,d.PD_NAME,(dku.FIRST_NAME || ' ' || dku.LAST_NAME)  AS COMMENT_BY,(DKUV.FIRST_NAME || ' ' || dku.LAST_NAME) AS RECEIVED_BY, t.RECIPIENT_ID FROM SD_COMMENTS t Join CommitteePD d ON t.PD_ID = d.ID join DAT_KEBS_USERS DKU on t.CREATED_BY = DKU.ID join DAT_KEBS_USERS DKUV on t.RECIPIENT_ID = DKUV.ID WHERE t.STATUS = 1 ORDER BY t.ID DESC",
        nativeQuery = true
    )
    fun getAllCommentsOnPreliminaryDraft(): List<CommentsWithPdId>

    //gets number of comments on Committee Draft
    @Query
        ("Select Count(t.id)As NumberOfComments from SD_COMMENTS t where CD_ID=:cdID", nativeQuery = true)
    fun getNumberOfComments(@Param("cdID") cdID: Long): String


    //gets all comments on all Committee Drafts

    @Query(
        "SELECT t.ID AS commentsID,t.TITLE,t.DOCUMENT_TYPE, cast(t.CIRCULATION_DATE as varchar(200)) AS CIRCULATION_DATE ,cast(t.CLOSING_DATE as varchar(200)) AS CLOSING_DATE,t.STATUS,t.ORGANIZATION,t.CLAUSE,t.PARAGRAPH,t.COMMENT_TYPE,t.PROPOSED_CHANGE,t.OBSERVATION,cast(t.CREATED_ON as varchar(200)) AS CREATED_ON,t.COMMENTS_MADE,d.CD_NAME,(dku.FIRST_NAME || ' ' || dku.LAST_NAME)  AS COMMENT_BY,(DKUV.FIRST_NAME || ' ' || dku.LAST_NAME) AS RECEIVED_BY, t.RECIPIENT_ID FROM SD_COMMENTS t Join SD_COMMITTEE_CD d ON t.CD_ID = d.ID join DAT_KEBS_USERS DKU on t.CREATED_BY = DKU.ID join DAT_KEBS_USERS DKUV on t.RECIPIENT_ID = DKUV.ID WHERE t.STATUS = 1 ORDER BY t.ID DESC",
        nativeQuery = true
    )
    fun getAllCommentsOnCommitteeDraft(): List<CommentsWithCdId>

    //gets user logged in comments on committee drafts
    @Query(
        "SELECT t.id,t.ID AS commentsID,t.TITLE,t.DOCUMENT_TYPE, cast(t.CIRCULATION_DATE as varchar(200)) AS CIRCULATION_DATE ,cast(t.CLOSING_DATE as varchar(200)) AS CLOSING_DATE,t.STATUS,t.ORGANIZATION,t.CLAUSE,t.PARAGRAPH,t.COMMENT_TYPE,t.PROPOSED_CHANGE,t.OBSERVATION,cast(t.CREATED_ON as varchar(200)) AS CREATED_ON,t.COMMENTS_MADE,d.CD_NAME,(dku.FIRST_NAME || ' ' || dku.LAST_NAME)  AS COMMENT_BY FROM SD_COMMENTS t Join SD_COMMITTEE_CD d ON t.CD_ID = d.ID join DAT_KEBS_USERS DKU on t.CREATED_BY = DKU.ID  WHERE t.STATUS = 1 and t.CREATED_BY=:loggedInUserId and t.DOCUMENT_TYPE='CD' ORDER BY t.ID DESC",
       
       
        nativeQuery = true
    )
    fun getUserLoggedInCommentsOnCommitteeDraft(@Param("loggedInUserId") loggedInUserId: Long): List<CommentsWithCdId>

    fun findAllByUserIdAndDocumentType(userId:Long?,documentType:String):List<Comments>


    //gets comments based on Committee Draft
    @Query(
        "SELECT t.ID AS commentsID,t.TITLE,t.DOCUMENT_TYPE, cast(t.CIRCULATION_DATE as varchar(200)) AS CIRCULATION_DATE ,cast(t.CLOSING_DATE as varchar(200)) AS CLOSING_DATE,t.STATUS,t.ORGANIZATION,t.CLAUSE,t.PARAGRAPH,t.COMMENT_TYPE,t.PROPOSED_CHANGE,t.OBSERVATION,cast(t.CREATED_ON as varchar(200)) AS CREATED_ON,t.COMMENTS_MADE,d.CD_NAME,(dku.FIRST_NAME || ' ' || dku.LAST_NAME)  AS COMMENT_BY FROM SD_COMMENTS t Join SD_COMMITTEE_CD d ON t.CD_ID = d.ID join DAT_KEBS_USERS DKU on t.CREATED_BY = DKU.ID WHERE t.STATUS = 1 and t.CD_ID=:cdId ORDER BY t.ID DESC",
        nativeQuery = true
    )
    fun findByCdId(@Param("cdId") cdId: Long): List<CommentsWithCdId>


    //gets comments based on Public Review Draft
    @Query(
        "SELECT t.ID AS commentsID,t.TITLE,t.DOCUMENT_TYPE, t.CREATED_BY AS unLoggedInUserEmail,  t.VAR_FIELD_5 AS unLoggedInUserNames, t.VAR_FIELD_6 AS unLoggedInUserPhone,  t.CREATED_BY,  cast(t.CIRCULATION_DATE as varchar(200)) AS CIRCULATION_DATE ,cast(t.CLOSING_DATE as varchar(200)) AS CLOSING_DATE,t.STATUS,t.ORGANIZATION,t.CLAUSE,t.PARAGRAPH,t.COMMENT_TYPE,t.PROPOSED_CHANGE,t.OBSERVATION,cast(t.CREATED_ON as varchar(200)) AS CREATED_ON,t.COMMENTS_MADE,d.PRD_NAME,(dku.FIRST_NAME || ' ' || dku.LAST_NAME)  AS COMMENT_BY,(DKUV.FIRST_NAME || ' ' || dku.LAST_NAME) AS RECEIVED_BY, t.RECIPIENT_ID FROM SD_COMMENTS t Join SD_PUBLIC_REVIEW_DRAFTS d ON t.PRD_ID = d.ID left join DAT_KEBS_USERS DKU on t.CREATED_BY = DKU.ID join DAT_KEBS_USERS DKUV on t.RECIPIENT_ID = DKUV.ID WHERE t.STATUS = 1 and t.PRD_ID=:prdID ORDER BY t.ID DESC",
        nativeQuery = true
    )
    fun findByPrdId(@Param("prdID") prdID: Long): List<CommentsWithPrdId>


    //gets user logged in comments on Public Review drafts
    @Query(
        "SELECT t.ID AS commentsID,t.TITLE,t.DOCUMENT_TYPE, cast(t.CIRCULATION_DATE as varchar(200)) AS CIRCULATION_DATE ,cast(t.CLOSING_DATE as varchar(200)) AS CLOSING_DATE,t.STATUS,t.ORGANIZATION,t.CLAUSE,t.PARAGRAPH,t.COMMENT_TYPE,t.PROPOSED_CHANGE,t.OBSERVATION,cast(t.CREATED_ON as varchar(200)) AS CREATED_ON,t.COMMENTS_MADE,d.PRD_NAME,(dku.FIRST_NAME || ' ' || dku.LAST_NAME)  AS COMMENT_BY,(DKUV.FIRST_NAME || ' ' || dku.LAST_NAME) AS RECEIVED_BY, t.RECIPIENT_ID FROM SD_COMMENTS t Join SD_PUBLIC_REVIEW_DRAFTS d ON t.PRD_ID = d.ID join DAT_KEBS_USERS DKU on t.CREATED_BY = DKU.ID join DAT_KEBS_USERS DKUV on t.CREATED_BY  = DKUV.ID WHERE t.STATUS = 1 and t.CREATED_BY=:loggedInUserId ORDER BY t.ID DESC",
        nativeQuery = true
    )
    fun getUserLoggedInCommentsOnPublicReviewDraft(@Param("loggedInUserId") loggedInUserId: Long): List<CommentsWithPrdId>


}
