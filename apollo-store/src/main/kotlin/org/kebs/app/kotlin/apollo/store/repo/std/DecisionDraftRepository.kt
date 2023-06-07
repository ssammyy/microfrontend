package org.kebs.app.kotlin.apollo.store.repo.std

import org.kebs.app.kotlin.apollo.store.model.std.DecisionDraft
import org.springframework.data.jpa.repository.JpaRepository

interface DecisionDraftRepository: JpaRepository<DecisionDraft,Long> {

}
