package org.kebs.app.kotlin.apollo.store.repo.std


import org.kebs.app.kotlin.apollo.store.model.std.ReferenceMaterialJustification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReferenceMaterialJustificationRepository: JpaRepository<ReferenceMaterialJustification,String> {
    fun findByItemId(itemId: String): MutableList<ReferenceMaterialJustification>

}
