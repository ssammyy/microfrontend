package org.kebs.app.kotlin.apollo.store.repo.std


import org.kebs.app.kotlin.apollo.store.model.std.LiaisonOrganization
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LiaisonOrganizationRepository: JpaRepository<LiaisonOrganization, Long> {
}
