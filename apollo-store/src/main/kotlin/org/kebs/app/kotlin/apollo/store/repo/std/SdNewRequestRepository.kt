package org.kebs.app.kotlin.apollo.store.repo.std


import org.kebs.app.kotlin.apollo.store.model.std.SdNewRequestEntity
import org.springframework.data.repository.CrudRepository

interface SdNewRequestRepository : CrudRepository<SdNewRequestEntity, Long> {
}