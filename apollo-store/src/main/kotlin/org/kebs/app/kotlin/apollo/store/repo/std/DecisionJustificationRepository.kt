package org.kebs.app.kotlin.apollo.store.repo.std

import org.kebs.app.kotlin.apollo.store.model.std.DecisionJustification
import org.springframework.data.jpa.repository.JpaRepository

interface DecisionJustificationRepository: JpaRepository<DecisionJustification,Long> {

    fun findAllByJustificationId(justificationId:String): List<DecisionJustification>

}
