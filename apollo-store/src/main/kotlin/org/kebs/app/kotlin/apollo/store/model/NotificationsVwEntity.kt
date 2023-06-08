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

package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "CFG_KEBS_NOTIFICATIONS_VW")
class NotificationsVwEntity : Serializable {
    @Id
    @Basic
    @Column(name = "ID", nullable = true, precision = 0)
    var id: Long? = null

    @Basic
    @Column(name = "NOTIFICATION_TYPE", nullable = true, length = 250)
    var notificationType: String? = null

    @Basic
    @Column(name = "SUBJECT", nullable = true, length = 255)
    var subject: String? = null

    @Basic
    @Column(name = "DESCRIPTION", nullable = true, length = 250)
    var description: String? = null

    @Basic
    @Column(name = "SENDER", nullable = true, length = 120)
    var sender: String? = null

    @Basic
    @Column(name = "TEMPLATE", nullable = true, length = 3000)
    var template: String? = null

    @Basic
    @Column(name = "PROCESSOR_CLASS_NAME", nullable = true, length = 180)
    var processorClassName: String? = null

    @Basic
    @Column(name = "REQUEST_TOPIC", nullable = true, length = 180)
    var requestTopic: String? = null

    @Basic
    @Column(name = "EVENT_STATUS", nullable = true, length = 15)
    var eventStatus: String? = null

    @Basic
    @Column(name = "METHOD_NAME", nullable = true, length = 180)
    var methodName: String? = null

    @Basic
    @Column(name = "SPEL_PROCESSOR", nullable = true, length = 255)
    var spelProcessor: String? = null

    @Basic
    @Column(name = "DELIMITER", nullable = true, length = 25)
    var delimiter: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val that = other as NotificationsVwEntity?

        if (if (id != null) id != that!!.id else that!!.id != null) return false
        if (if (notificationType != null) notificationType != that.notificationType else that.notificationType != null)
            return false
        if (if (subject != null) subject != that.subject else that.subject != null) return false
        if (if (description != null) description != that.description else that.description != null) return false
        if (if (sender != null) sender != that.sender else that.sender != null) return false
        if (if (template != null) template != that.template else that.template != null) return false
        if (if (processorClassName != null) processorClassName != that.processorClassName else that.processorClassName != null)
            return false
        if (if (requestTopic != null) requestTopic != that.requestTopic else that.requestTopic != null) return false
        if (if (eventStatus != null) eventStatus != that.eventStatus else that.eventStatus != null) return false
        if (if (methodName != null) methodName != that.methodName else that.methodName != null) return false
        if (if (spelProcessor != null) spelProcessor != that.spelProcessor else that.spelProcessor != null)
            return false
        return !if (delimiter != null) delimiter != that.delimiter else that.delimiter != null

    }

    override fun hashCode(): Int {
        var result = if (id != null) id!!.hashCode() else 0
        result = 31 * result + if (notificationType != null) notificationType!!.hashCode() else 0
        result = 31 * result + if (subject != null) subject!!.hashCode() else 0
        result = 31 * result + if (description != null) description!!.hashCode() else 0
        result = 31 * result + if (sender != null) sender!!.hashCode() else 0
        result = 31 * result + if (template != null) template!!.hashCode() else 0
        result = 31 * result + if (processorClassName != null) processorClassName!!.hashCode() else 0
        result = 31 * result + if (requestTopic != null) requestTopic!!.hashCode() else 0
        result = 31 * result + if (eventStatus != null) eventStatus!!.hashCode() else 0
        result = 31 * result + if (methodName != null) methodName!!.hashCode() else 0
        result = 31 * result + if (spelProcessor != null) spelProcessor!!.hashCode() else 0
        result = 31 * result + if (delimiter != null) delimiter!!.hashCode() else 0
        return result
    }
}
