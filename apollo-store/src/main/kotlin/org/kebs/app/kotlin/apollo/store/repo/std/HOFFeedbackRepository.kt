package org.kebs.app.kotlin.apollo.store.repo.std

import org.kebs.app.kotlin.apollo.store.model.std.HOFFeedback
import org.kebs.app.kotlin.apollo.store.model.std.StandardRequest
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface HOFFeedbackRepository :JpaRepository<HOFFeedback,Long> {

    fun findTopBySdRequestIDOrderByIdDesc(sdRequestId: String): HOFFeedback?

}
