package org.kebs.app.kotlin.apollo.standardsdevelopment.repositories
import org.kebs.app.kotlin.apollo.standardsdevelopment.models.CommitteePD
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommitteePDRepository:JpaRepository<CommitteePD,Long>
