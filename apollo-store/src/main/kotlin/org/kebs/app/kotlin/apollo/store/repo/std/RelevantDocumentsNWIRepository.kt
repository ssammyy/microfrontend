package org.kebs.app.kotlin.apollo.store.repo.std


import org.kebs.app.kotlin.apollo.store.model.std.RelevantDocumentsNWI
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RelevantDocumentsNWIRepository: JpaRepository<RelevantDocumentsNWI,String> {
    fun findByItemId(itemId: String): MutableList<RelevantDocumentsNWI>
}
