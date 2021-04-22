package com.apollo.standardsdevelopment.repositories

import com.apollo.standardsdevelopment.models.TechnicalCommittee
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TechnicalCommitteeRepository : JpaRepository<TechnicalCommittee,Long>{
}
