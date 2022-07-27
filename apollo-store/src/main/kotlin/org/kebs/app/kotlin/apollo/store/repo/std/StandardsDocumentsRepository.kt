package org.kebs.app.kotlin.apollo.store.repo.std

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
    fun findBySdDocumentId(id: Long): DatKebsSdStandardsEntity
    fun findBySdDocumentIdAndDocumentTypeDef(id: Long, documentTypeDef: String): DatKebsSdStandardsEntity


    @Transactional
    @Modifying
    @Query(
        value = "UPDATE DAT_KEBS_SD_STANDARDS_UPLOADS t1 SET t1.VAR_FIELD_1 = :pdId WHERE t1.STANDARD_DOCUMENT_ID=:nwiID",
        nativeQuery = true
    )
    fun updateDocsWithPDid(@Param("pdId") pdId: Long, @Param("nwiID") nwiID: Long): Int

}
