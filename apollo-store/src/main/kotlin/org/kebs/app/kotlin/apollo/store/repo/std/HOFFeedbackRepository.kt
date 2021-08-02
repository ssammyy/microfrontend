package org.kebs.app.kotlin.apollo.store.repo.std

import org.kebs.app.kotlin.apollo.store.model.std.HOFFeedback
import org.springframework.data.jpa.repository.JpaRepository

interface HOFFeedbackRepository :JpaRepository<HOFFeedback,Long> {
}
