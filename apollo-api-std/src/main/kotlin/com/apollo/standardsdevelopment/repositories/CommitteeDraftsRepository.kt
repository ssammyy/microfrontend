package com.apollo.standardsdevelopment.repositories

import com.apollo.standardsdevelopment.models.CommitteeDrafts
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommitteeDraftsRepository : JpaRepository<CommitteeDrafts, Long> {
}