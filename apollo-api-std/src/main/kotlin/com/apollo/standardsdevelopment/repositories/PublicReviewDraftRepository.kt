package com.apollo.standardsdevelopment.repositories

import com.apollo.standardsdevelopment.models.CommitteeDraftsPD
import com.apollo.standardsdevelopment.models.PublicReviewDraft
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository

interface PublicReviewDraftRepository : JpaRepository<PublicReviewDraft, Long> {
}
