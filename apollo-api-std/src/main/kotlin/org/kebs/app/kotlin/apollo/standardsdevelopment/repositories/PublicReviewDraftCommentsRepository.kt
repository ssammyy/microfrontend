package org.kebs.app.kotlin.apollo.standardsdevelopment.repositories

import org.kebs.app.kotlin.apollo.standardsdevelopment.models.PublicReviewDraftComments
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository

interface PublicReviewDraftCommentsRepository : JpaRepository<PublicReviewDraftComments, Long>
