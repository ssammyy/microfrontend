package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.PvocComplaintRemarksEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository

interface PvocComplaintRemarksEntityRepo : HazelcastRepository<PvocComplaintRemarksEntity, Long> {
    fun findAllByComplaintId(complaintId: Long) : List<PvocComplaintRemarksEntity>?
}