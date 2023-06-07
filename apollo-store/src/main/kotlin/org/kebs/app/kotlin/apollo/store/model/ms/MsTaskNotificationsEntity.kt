package org.kebs.app.kotlin.apollo.store.model.ms

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_MS_TASK_NOTIFICATIONS", schema = "APOLLO", catalog = "")
class MsTaskNotificationsEntity : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_MS_TASK_NOTIFICATIONS_SEQ_GEN", allocationSize = 1, sequenceName = "DAT_MS_TASK_NOTIFICATIONS_SEQ")
    @GeneratedValue(generator = "DAT_MS_TASK_NOTIFICATIONS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = null

    @Basic
    @Column(name = "NOTIFICATION_MSG")
    var notificationMsg: String? = null

    @Basic
    @Column(name = "NOTIFICATION_BODY")
    var notificationBody: String? = null

    @Basic
    @Column(name = "NOTIFICATION_NAME")
    var notificationName: String? = null

    @Basic
    @Column(name = "NOTIFICATION_TYPE")
    var notificationType: String? = null

    @Basic
    @Column(name = "TASK_REF_NUMBER")
    var taskRefNumber: String? = null

    @Basic
    @Column(name = "FROM_USER_ID")
    var fromUserId: Long? = null

    @Basic
    @Column(name = "TO_USER_ID")
    var toUserId: Long? = null

    @Basic
    @Column(name = "READ_STATUS")
    var readStatus: Int? = null

    @Basic
    @Column(name = "STATUS")
    var status: Int? = null

    @Basic
    @Column(name = "DESCRIPTION")
    var description: String? = null

    @Basic
    @Column(name = "VAR_FIELD_1")
    var varField1: String? = null

    @Basic
    @Column(name = "VAR_FIELD_2")
    var varField2: String? = null

    @Basic
    @Column(name = "VAR_FIELD_3")
    var varField3: String? = null

    @Basic
    @Column(name = "VAR_FIELD_4")
    var varField4: String? = null

    @Basic
    @Column(name = "VAR_FIELD_5")
    var varField5: String? = null

    @Basic
    @Column(name = "VAR_FIELD_6")
    var varField6: String? = null

    @Basic
    @Column(name = "VAR_FIELD_7")
    var varField7: String? = null

    @Basic
    @Column(name = "VAR_FIELD_8")
    var varField8: String? = null

    @Basic
    @Column(name = "VAR_FIELD_9")
    var varField9: String? = null

    @Basic
    @Column(name = "VAR_FIELD_10")
    var varField10: String? = null

    @Basic
    @Column(name = "CREATED_BY")
    var createdBy: String? = null

    @Basic
    @Column(name = "CREATED_ON")
    var createdOn: Timestamp? = null

    @Basic
    @Column(name = "MODIFIED_BY")
    var modifiedBy: String? = null

    @Basic
    @Column(name = "MODIFIED_ON")
    var modifiedOn: Timestamp? = null

    @Basic
    @Column(name = "DELETE_BY")
    var deleteBy: String? = null

    @Basic
    @Column(name = "DELETED_ON")
    var deletedOn: Timestamp? = null

}
