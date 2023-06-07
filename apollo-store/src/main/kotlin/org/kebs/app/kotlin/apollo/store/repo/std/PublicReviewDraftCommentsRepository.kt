package org.kebs.app.kotlin.apollo.store.repo.std

import org.kebs.app.kotlin.apollo.store.model.std.PublicReviewDraftComments
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository

interface PublicReviewDraftCommentsRepository : JpaRepository<PublicReviewDraftComments, Long>
