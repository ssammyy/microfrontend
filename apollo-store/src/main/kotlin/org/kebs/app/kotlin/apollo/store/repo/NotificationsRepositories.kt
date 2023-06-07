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
    fun findByServiceRequestStatusAndStatus(serviceRequestStatus: Int?, status: Int?): Collection<NotificationsEntity>?
    fun findByUuidAndStatus(uuid: String, status: Int): Collection<NotificationsEntity>?
    fun findByUuid(uuid: String): NotificationsEntity?
    fun findByServiceMapIdAndUuidAndStatus(serviceMapId: ServiceMapsEntity, uuid: String, status: Int): Collection<NotificationsEntity>?
    fun findFirstByNotificationTypeAndReferenceNameOrderByNotificationType(notificationType: NotificationTypesEntity?, templateCode: String): Optional<NotificationsEntity>

}

@Repository
interface INotificationTypesRepository : HazelcastRepository<NotificationTypesEntity, Int>{
    fun findByTypeCode(code: String): Optional<NotificationTypesEntity>
}

@Repository
interface INotificationsBufferRepository : HazelcastRepository<NotificationsBufferEntity, Long>{
//    override fun findAll( pageable: Pageable): Page<NotificationsBufferEntity>?
    fun findByRecipient(recipient: String, pageable: Pageable): Page<NotificationsBufferEntity>?
    fun findByRecipient(recipient: String): List<NotificationsBufferEntity>?
//    fun findByRecipientAndOrderByIdDesc(recipient: String): List<NotificationsBufferEntity>?
    fun findAllByRecipientOrderByIdDesc(recipient: String): List<NotificationsBufferEntity>?

    fun findByIdAndRecipient(id: Long, recipient: String):List<NotificationsBufferEntity>?
}

