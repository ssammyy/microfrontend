package com.apollo.standardsdevelopment.repositories

import com.apollo.standardsdevelopment.models.StandardDraft
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StandardDraftRepository : JpaRepository<StandardDraft,Long>{
}