package com.apollo.standardsdevelopment.repositories

import com.apollo.standardsdevelopment.models.CommitteeNWI
import com.apollo.standardsdevelopment.models.StandardNWI
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommitteeNWIRepository:JpaRepository<CommitteeNWI,Long> {
}