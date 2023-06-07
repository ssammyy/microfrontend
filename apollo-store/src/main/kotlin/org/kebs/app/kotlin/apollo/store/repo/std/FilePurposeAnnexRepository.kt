package org.kebs.app.kotlin.apollo.store.repo.std

import org.kebs.app.kotlin.apollo.store.model.std.FilePurposeAnnex
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FilePurposeAnnexRepository: JpaRepository<FilePurposeAnnex,String> {
    fun findByItemId(itemId: String): MutableList<FilePurposeAnnex>

}
