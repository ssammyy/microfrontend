package org.kebs.app.kotlin.apollo.store.repo.std
import org.kebs.app.kotlin.apollo.store.model.std.StandardJustification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StandardJustificationRepository : JpaRepository<StandardJustification,Long> {
    fun findByRequestNo( requestNo: String?) : StandardJustification
}
