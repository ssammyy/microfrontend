package org.kebs.app.kotlin.apollo.store.repo.std

import org.kebs.app.kotlin.apollo.store.model.std.DraughtDocuments
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DraughtDocumentRepository: JpaRepository<DraughtDocuments,String> {
    fun findByItemId(itemId: String): MutableList<DraughtDocuments>

}
