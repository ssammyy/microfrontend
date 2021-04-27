package com.apollo.standardsdevelopment.repositories

import com.apollo.standardsdevelopment.models.DecisionFeedback
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DecisionFeedbackRepository :JpaRepository<DecisionFeedback,Long>{
}