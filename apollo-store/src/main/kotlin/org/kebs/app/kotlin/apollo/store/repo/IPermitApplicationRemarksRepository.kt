package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.PermitApplicationEntity
import org.kebs.app.kotlin.apollo.store.model.PermitApplicationRemarksEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.hazelcast.repository.HazelcastRepository

interface IPermitApplicationRemarksRepository: HazelcastRepository<PermitApplicationRemarksEntity, Long> {
    fun findByPermitIdOrderByIdDesc(permitId: PermitApplicationEntity): List<PermitApplicationRemarksEntity>
    fun findByPermitId(permitId: PermitApplicationEntity): List<PermitApplicationRemarksEntity>
    fun findAllByPermitId(permitId: PermitApplicationEntity, pageable: Pageable?): Page<PermitApplicationRemarksEntity>?
}