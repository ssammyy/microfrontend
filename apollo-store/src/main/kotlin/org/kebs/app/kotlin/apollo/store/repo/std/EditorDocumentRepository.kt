package org.kebs.app.kotlin.apollo.store.repo.std

import org.kebs.app.kotlin.apollo.store.model.std.EditorDocuments
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EditorDocumentRepository: JpaRepository<EditorDocuments,String> {
    fun findByItemId(itemId: String): MutableList<EditorDocuments>

}
