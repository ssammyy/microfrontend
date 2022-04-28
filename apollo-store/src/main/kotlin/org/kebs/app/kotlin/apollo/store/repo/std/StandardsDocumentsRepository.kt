package org.kebs.app.kotlin.apollo.store.repo.std

import org.kebs.app.kotlin.apollo.store.model.std.DatKebsSdStandardsEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

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
    fun findBySdDocumentIdAndDocumentTypeDef(id: Long, documentTypeDef:String): DatKebsSdStandardsEntity

}
