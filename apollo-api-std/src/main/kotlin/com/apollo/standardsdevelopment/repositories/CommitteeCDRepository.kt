package com.apollo.standardsdevelopment.repositories
import com.apollo.standardsdevelopment.models.CommitteeCD
import com.apollo.standardsdevelopment.models.CommitteeDrafts
import com.apollo.standardsdevelopment.models.CommitteePD
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommitteeCDRepository : JpaRepository<CommitteeCD, Long> {
}