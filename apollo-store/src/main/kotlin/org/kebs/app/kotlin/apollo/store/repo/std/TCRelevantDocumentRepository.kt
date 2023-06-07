package org.kebs.app.kotlin.apollo.store.repo.std

import org.kebs.app.kotlin.apollo.store.model.std.TCRelevantDocument
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TCRelevantDocumentRepository: JpaRepository<TCRelevantDocument,String> {
    fun findByItemId(itemId: String): MutableList<TCRelevantDocument>

}
