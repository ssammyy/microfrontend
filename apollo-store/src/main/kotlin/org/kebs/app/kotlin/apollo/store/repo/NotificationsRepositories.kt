/*
 *                            Copyright (c) 2020.  BSK
 *                            Licensed under the Apache License, Version 2.0 (the "License");
 *                            you may not use this file except in compliance with the License.
 *                            You may obtain a copy of the License at
 *
 *                                http://www.apache.org/licenses/LICENSE-2.0
 *
 *                            Unless required by applicable law or agreed to in writing, software
 *                            distributed under the License is distributed on an "AS IS" BASIS,
 *                            WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *                            See the License for the specific language governing permissions and
 *                            limitations under the License.
 */

package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface INotificationsRepository : HazelcastRepository<NotificationsEntity, Int> {

    /**
     * TODO: Review queries below for correctness as on DB the fields combinations expected to return a single value are not unique on the DB, enforce uniqueness on the DB and/or correct the queries
     */
    fun findFirstByRequestTopicAndEventStatusAndNotificationTypeOrderByNotificationType(requestTopic: String?, eventStatus: String?, notificationType: NotificationTypesEntity?): NotificationsEntity?
    fun findByNotificationTypeAndServiceMapIdAndServiceRequestStatusAndStatus(notificationType: NotificationTypesEntity, serviceMapId: ServiceMapsEntity, serviceRequestStatus: Int, status: Int): NotificationsEntity?
    fun findByServiceMapIdAndServiceRequestStatusAndStatus(serviceMapId: ServiceMapsEntity?, serviceRequestStatus: Int?, status: Int?): Collection<NotificationsEntity>?
    fun findByServiceMapIdAndEventStatusAndStatus(serviceMapId: ServiceMapsEntity, eventStatus: String, status: Int): Collection<NotificationsEntity>?
    fun findByServiceMapIdAndUuidAndStatus(serviceMapId: ServiceMapsEntity, uuid: String, status: Int): Collection<NotificationsEntity>?
    fun findFirstByEventStatusOrderByEventStatus(eventStatus: String): NotificationsEntity?
    fun findFirstByRequestTopic(requestTopic: String): NotificationsEntity?

    fun findByServiceMapIdAndServiceRequestStatusAndStatusAndNotificationType(serviceMapId: ServiceMapsEntity, serviceRequestStatus: Int?, status: Int?, notificationType: NotificationTypesEntity?): NotificationsEntity?

}

@Repository
interface INotificationTypesRepository : HazelcastRepository<NotificationTypesEntity, Int>

@Repository
interface INotificationsBufferRepository : HazelcastRepository<NotificationsBufferEntity, Long>{
//    override fun findAll( pageable: Pageable): Page<NotificationsBufferEntity>?
    fun findByRecipient(recipient: String, pageable: Pageable): Page<NotificationsBufferEntity>?
    fun findByRecipient(recipient: String): List<NotificationsBufferEntity>?

    fun findByIdAndRecipient(id: Long, recipient: String):List<NotificationsBufferEntity>?
}

