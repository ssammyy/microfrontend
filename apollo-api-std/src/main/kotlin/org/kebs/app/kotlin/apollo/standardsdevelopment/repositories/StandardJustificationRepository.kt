package org.kebs.app.kotlin.apollo.standardsdevelopment.repositories

import org.kebs.app.kotlin.apollo.standardsdevelopment.models.StandardJustification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StandardJustificationRepository : JpaRepository<StandardJustification,Long>
