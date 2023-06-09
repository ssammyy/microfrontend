package org.kebs.app.kotlin.apollo.store.repo.std
import org.kebs.app.kotlin.apollo.store.model.std.StandardJustification
import org.kebs.app.kotlin.apollo.store.model.std.VotesWithNWIId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StandardJustificationRepository : JpaRepository<StandardJustification,Long> {
    fun findByRequestNo( requestNo: String?) : StandardJustification
    fun findByNwiIdOrderById( nwiId: String?) : StandardJustification

    fun findByStatusOrderByIdAsc( status: String?) : List<StandardJustification>

    fun findByStatusAndTcSecretary( status: String?, tcSecretary:String?) : List<StandardJustification>

    fun findByTcSecretary( tcSecretary:String?) : List<StandardJustification>



    fun findByNwiId(nwiId: String?):List<StandardJustification>

}
