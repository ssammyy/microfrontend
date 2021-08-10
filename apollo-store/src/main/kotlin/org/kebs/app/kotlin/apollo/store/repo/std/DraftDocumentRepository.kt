package org.kebs.app.kotlin.apollo.store.repo.std


import org.kebs.app.kotlin.apollo.store.model.std.DraftDocument
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DraftDocumentRepository : JpaRepository<DraftDocument, String> {
    fun findByItemId(itemId: String): MutableList<DraftDocument>

}
