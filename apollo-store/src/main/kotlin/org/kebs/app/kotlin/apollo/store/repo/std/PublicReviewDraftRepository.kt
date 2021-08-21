package org.kebs.app.kotlin.apollo.store.repo.std

import org.kebs.app.kotlin.apollo.store.model.std.PublicReviewDraft
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository

interface PublicReviewDraftRepository : JpaRepository<PublicReviewDraft, Long>
