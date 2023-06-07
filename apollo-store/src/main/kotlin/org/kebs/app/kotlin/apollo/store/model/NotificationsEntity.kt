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

package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "CFG_NOTIFICATIONS")
class NotificationsEntity : Serializable {

    @Id
    @SequenceGenerator(name = "CFG_NOTIFICATIONS_SEQ_GEN", allocationSize = 1, sequenceName = "CFG_NOTIFICATIONS_SEQ")
    @GeneratedValue(generator = "CFG_NOTIFICATIONS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    var id: Int? = 0

    @Basic
    @Column(name = "UUID")
    var uuid: String? = null

    @Basic
    @Column(name = "ACTOR_CLASS")
    var actorClass: String? = null

    @Basic
    @Column(name = "DESCRIPTION")
    var description: String? = null

    @JoinColumn(name = "NOTIFICATION_TYPE", referencedColumnName = "ID")
    @ManyToOne
    var notificationType: NotificationTypesEntity? = null

    @JoinColumn(name = "SERVICE_MAP_ID", referencedColumnName = "ID")
    @ManyToOne
    var serviceMapId: ServiceMapsEntity? = null

    @Basic
    @Column(name = "SENDER")
    var sender: String? = null

    @Basic
    @Column(name = "SPEL_PROCESSOR")
    var spelProcessor: String? = null

    @Basic
    @Column(name = "TEMPLATE_REFERENCE_NAME", nullable = true, length = 250)
    var referenceName: String? = null

    @Basic
    @Column(name = "EMAIL_TEMPLATE")
    var textTemplate: String? = null

    @Basic
    @Column(name = "SUBJECT")
    var subject: String? = null

    @Basic
    @Column(name = "ATTACHMENT_FILE_NAME")
    var attachmentFileName: String? = null

    @Basic
    @Column(name = "EVENT_STATUS")
    var eventStatus: String? = null

    @Basic
    @Column(name = "REQUEST_TOPIC")
    var requestTopic: String? = null

    @Basic
    @Column(name = "BEAN_PROCESSOR_NAME")
    var beanProcessorName: String? = null

    @Basic
    @Column(name = "METHOD_NAME")
    var methodName: String? = null

    @Basic
    @Column(name = "SERVICE_REQUEST_STATUS")
    var serviceRequestStatus: Int? = null

    @Column(name = "STATUS")
    @Basic
    var status: Int? = null

    @Column(name = "VAR_FIELD_1")
    @Basic
    var varField1: String? = null

    @Column(name = "VAR_FIELD_2")
    @Basic
    var varField2: String? = null

    @Column(name = "VAR_FIELD_3")
    @Basic
    var varField3: String? = null

    @Column(name = "VAR_FIELD_4")
    @Basic
    var varField4: String? = null

    @Column(name = "VAR_FIELD_5")
    @Basic
    var varField5: String? = null

    @Column(name = "VAR_FIELD_6")
    @Basic
    var varField6: String? = null

    @Column(name = "VAR_FIELD_7")
    @Basic
    var varField7: String? = null

    @Column(name = "VAR_FIELD_8")
    @Basic
    var varField8: String? = null

    @Column(name = "VAR_FIELD_9")
    @Basic
    var varField9: String? = null

    @Column(name = "VAR_FIELD_10")
    @Basic
    var varField10: String? = null

    @Column(name = "CREATED_BY")
    @Basic
    var createdBy: String? = null

    @Column(name = "CREATED_ON")
    @Basic
    var createdOn: Timestamp? = null

    @Column(name = "MODIFIED_BY")
    @Basic
    var modifiedBy: String? = null

    @Column(name = "MODIFIED_ON")
    @Basic
    var modifiedOn: Timestamp? = null

    @Column(name = "DELETED_BY")
    @Basic
    var deleteBy: String? = null

    @Column(name = "DELETED_ON")
    @Basic
    var deletedOn: Timestamp? = null
}
