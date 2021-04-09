package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.PermitApplicationEntity
import org.kebs.app.kotlin.apollo.store.model.SchedulerEntity
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository
import java.sql.Timestamp

@Repository
interface ISchedulerRepository : HazelcastRepository<SchedulerEntity, Long>{
    fun findByProcessInstanceIdAndTaskDefinitionKeyAndStatus(processInstanceId: String, taskDefinitionKey: String, status: Int): List<SchedulerEntity>?
    fun findByScheduledDateAndStatus (scheduledDate: Timestamp, status: Int): List<SchedulerEntity>?
    fun findByScheduledDateBetweenAndStatus (scheduledDateStart: Timestamp, scheduledDateEnd: Timestamp,status: Int): List<SchedulerEntity>?
}