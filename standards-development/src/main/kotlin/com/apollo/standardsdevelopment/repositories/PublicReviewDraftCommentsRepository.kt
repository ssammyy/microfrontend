package com.apollo.standardsdevelopment.repositories

import com.apollo.standardsdevelopment.models.CommitteeDraftsPD
import com.apollo.standardsdevelopment.models.PublicReviewDraft
import com.apollo.standardsdevelopment.models.PublicReviewDraftComments
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository

interface PublicReviewDraftCommentsRepository : JpaRepository<PublicReviewDraftComments, Long> {
}
