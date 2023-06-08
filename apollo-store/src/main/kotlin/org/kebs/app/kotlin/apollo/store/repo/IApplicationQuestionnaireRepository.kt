package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.ApplicationQuestionnaireEntity
import org.kebs.app.kotlin.apollo.store.model.PermitApplicationEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository

@Repository
interface IApplicationQuestionnaireRepository : HazelcastRepository<ApplicationQuestionnaireEntity, Long> {
    fun findByPermitId(permitId: Long?): ApplicationQuestionnaireEntity?
    fun findByDmarkForeignId(permitId: Long?): ApplicationQuestionnaireEntity
}