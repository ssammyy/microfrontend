package org.kebs.app.kotlin.apollo.store.repo.std

import org.kebs.app.kotlin.apollo.store.model.std.ComStandardUploads
import org.kebs.app.kotlin.apollo.store.model.std.DatKebsSdStandardsEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface StandardsDocumentsRepository : JpaRepository<DatKebsSdStandardsEntity, Long> {
    @Query(
        value = "SELECT * FROM DAT_KEBS_SD_STANDARDS_UPLOADS WHERE STANDARD_DOCUMENT_ID = :sdDocumentId",
        nativeQuery = true
    )
    fun findStandardDocument(@Param("sdDocumentId") sdDocumentId: Long?): Collection<DatKebsSdStandardsEntity?>?

    @Query(value = "SELECT max(ID)  FROM DAT_KEBS_SD_STANDARDS_UPLOADS", nativeQuery = true)
    fun getMaxUploadedID(): Long
    fun findBySdDocumentId(id: Long): Collection<DatKebsSdStandardsEntity?>?

    @Query(
        value = "SELECT * FROM DAT_KEBS_SD_STANDARDS_UPLOADS WHERE STANDARD_DOCUMENT_ID = :id AND DOCUMENT_TYPE='COMPANY STANDARD'",
        nativeQuery = true
    )
    fun findStandardUpload(id: Long): DatKebsSdStandardsEntity


    fun findBySdDocumentIdAndDocumentType(id:Long, documentType: String) :Collection<DatKebsSdStandardsEntity?>?



    fun findBySdDocumentIdAndDocumentTypeAndDocumentTypeDef(id:Long, documentType: String,documentTypeDef: String) :Collection<DatKebsSdStandardsEntity?>?
    fun findBySdDocumentIdAndDocumentTypeDef(id: Long, documentTypeDef: String): DatKebsSdStandardsEntity



    //retrieve PD Drafts Doc
    @Query(
        value = "SELECT * FROM DAT_KEBS_SD_STANDARDS_UPLOADS WHERE ID = :sdDocumentId",
        nativeQuery = true
    )
    fun findSDocumentId(@Param("sdDocumentId") sdDocumentId: Long): DatKebsSdStandardsEntity




    //retrieve PD Drafts Docs
    @Query(
        value = "SELECT * FROM DAT_KEBS_SD_STANDARDS_UPLOADS WHERE STANDARD_DOCUMENT_ID = :sdDocumentId AND (DOCUMENT_TYPE='PD DOCUMENT' OR DOCUMENT_TYPE='DRAFT DOCUMENTS FOR PD' OR DOCUMENT_TYPE='MINUTES FOR PD'  )  ",
        nativeQuery = true
    )
    fun findStandardDocumentPdId(@Param("sdDocumentId") sdDocumentId: Long?): Collection<DatKebsSdStandardsEntity?>?

    @Transactional
    @Modifying
    @Query(
        value = "UPDATE DAT_KEBS_SD_STANDARDS_UPLOADS t1 SET t1.VAR_FIELD_1 = :pdId, t1.STANDARD_DOCUMENT_ID= :pdId WHERE t1.STANDARD_DOCUMENT_ID=:nwiID AND DOCUMENT_TYPE='MINUTES FOR PD' OR DOCUMENT_TYPE='DRAFT DOCUMENTS FOR PD'",
        nativeQuery = true
    )
    fun updateDocsWithPDid(@Param("pdId") pdId: Long, @Param("nwiID") nwiID: Long): Int

    @Transactional
    @Modifying
    @Query(
        value = "UPDATE DAT_KEBS_SD_STANDARDS_UPLOADS t1 SET t1.VAR_FIELD_2 = :cdId WHERE t1.STANDARD_DOCUMENT_ID=:pdID AND DOCUMENT_TYPE='MINUTES FOR CD' OR DOCUMENT_TYPE='DRAFT DOCUMENTS FOR CD'",
        nativeQuery = true
    )
    fun updateDocsWithCDid(@Param("cdId") cdId: Long, @Param("pdID") pdID: Long): Int


    //retrieve CD Drafts Docs
    @Query(
        value = "SELECT * FROM DAT_KEBS_SD_STANDARDS_UPLOADS WHERE STANDARD_DOCUMENT_ID = :sdDocumentId AND (DOCUMENT_TYPE='CD DOCUMENT' OR DOCUMENT_TYPE='DRAFT DOCUMENTS FOR CD' OR DOCUMENT_TYPE='MINUTES FOR CD') ",
        nativeQuery = true
    )
    fun findStandardDocumentCdId(@Param("sdDocumentId") sdDocumentId: Long?): Collection<DatKebsSdStandardsEntity?>?


    @Transactional
    @Modifying
    @Query(
        value = "UPDATE DAT_KEBS_SD_STANDARDS_UPLOADS t1 SET t1.VAR_FIELD_3 = :prdId WHERE t1.STANDARD_DOCUMENT_ID=:cdId AND DOCUMENT_TYPE='MINUTES FOR PRD' OR DOCUMENT_TYPE='DRAFT DOCUMENTS FOR PRD'",
        nativeQuery = true
    )
    fun updateDocsWithPRDid(@Param("cdId") cdId: Long, @Param("prdId") prdId: Long): Int


    //retrieve PRD Drafts Docs
    @Query(
        value = "SELECT * FROM DAT_KEBS_SD_STANDARDS_UPLOADS WHERE STANDARD_DOCUMENT_ID = :sdDocumentId  AND (DOCUMENT_TYPE='PRD DOCUMENT' OR DOCUMENT_TYPE='DRAFT DOCUMENTS FOR PRD' OR DOCUMENT_TYPE='MINUTES FOR PRD')",
        nativeQuery = true
    )
    fun findStandardDocumentPrdId(@Param("sdDocumentId") sdDocumentId: Long?): Collection<DatKebsSdStandardsEntity?>?


}
