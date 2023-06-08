/*
 *
 *  *
 *  *
 *  *  *    Copyright (c) ${YEAR}.   BSK Global Technologies
 *  *  *
 *  *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *    you may not use this file except in compliance with the License.
 *  *  *    You may obtain a copy of the License at
 *  *  *
 *  *  *       http://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  *    Unless required by applicable law or agreed to in writing, software
 *  *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *   See the License for the specific language governing permissions and
 *  *  *   limitations under the License.
 *  *
 *
 */

package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.NotificationsVwEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Deprecated("Replaced the view with component tables in INotificationsRepository")
@Repository
interface KebsNotificationsVwRepository : HazelcastRepository<NotificationsVwEntity, Long> {
    @Query(value = "SELECT * FROM CFG_KEBS_NOTIFICATIONS_VW c WHERE c.REQUEST_TOPIC = ?1 and c.EVENT_STATUS = ?2 and c.NOTIFICATION_TYPE = ?3", nativeQuery = true)
    fun findByRequestTopicAndEventStatusAndNotificationType(requestTopic: String?, eventStatus: String?, type: Int?): NotificationsVwEntity?

    @Query(value = "SELECT * FROM CFG_KEBS_NOTIFICATIONS_VW WHERE CFG_KEBS_NOTIFICATIONS_VW.EVENT_STATUS = ?1", nativeQuery = true)
    fun findByEventStatus(eventStatus: String?): NotificationsVwEntity?

    @Query(value = "SELECT * FROM CFG_KEBS_NOTIFICATIONS_VW WHERE CFG_KEBS_NOTIFICATIONS_VW.REQUEST_TOPIC = ?1", nativeQuery = true)
    fun findByRequestTopic(topic: String?): NotificationsVwEntity?

    @Query(value = "SELECT * FROM CFG_KEBS_NOTIFICATIONS_VW WHERE CFG_KEBS_NOTIFICATIONS_VW.ID = ?1", nativeQuery = true)
    fun findByCustomId(id: Long): NotificationsVwEntity?
}