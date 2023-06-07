package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.PermitApplicationEntity
import org.kebs.app.kotlin.apollo.store.model.QuestionnaireSta10Entity
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository


@Repository
interface IQuestionnaireSta10Repository: HazelcastRepository<QuestionnaireSta10Entity, Long>{
    fun findByPermitId(permitId: Long?): QuestionnaireSta10Entity?
}
