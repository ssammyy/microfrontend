package org.kebs.app.kotlin.apollo.store.repo.std

import org.kebs.app.kotlin.apollo.store.model.std.Ballot
import org.kebs.app.kotlin.apollo.store.model.std.Comments
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentsRepository : JpaRepository<Comments, Long>
{
    fun findByUserIdAndPdIdAndStatus(userId:Long,PdId:Long,status:String):List<Comments>
    fun findByPdId(PdId: Long):List<Comments>
}
