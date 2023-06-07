package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Date
import java.sql.Time
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_SCHEDULER")
class SchedulerEntity  : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_SCHEDULER_SEQ_GEN", allocationSize = 1, sequenceName = "DAT_KEBS_SCHEDULER_SEQ")
    @GeneratedValue(generator = "DAT_KEBS_SCHEDULER_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = 0

    @Column(name = "TASK_TYPE")
    @Basic
    var taskType: String? = null

    @Column(name = "NOTIFICATION_ID")
    @Basic
    var notificationId: Int? = null

    @Column(name = "USER_ID")
    @Basic
    var userId: Long? = null

    @Column(name = "EMAIL_ADDRESS")
    @Basic
    var emailAddress: String? = null

    @Column(name = "STATUS")
    @Basic
    var status: Int? = null

    @Column(name = "CREATED_ON")
    @Basic
    var createdOn: Timestamp? = null

    @Column(name = "EXECUTED_ON")
    @Basic
    var executedOn: Timestamp? = null

    @Column(name = "CANCELLED_ON")
    @Basic
    var cancelledOn: Timestamp? = null

    @Column(name = "PROCESS_INSTANCE_ID")
    @Basic
    var processInstanceId: String? = null

    @Column(name = "TASK_ID")
    @Basic
    var taskId: String? = null

    @Column(name = "SCHEDULED_DATE")
    @Basic
    var scheduledDate: Timestamp? = null

    @Column(name = "TASK_DEFINITION_KEY")
    @Basic
    var taskDefinitionKey: String? = null

    @Column(name = "RESULT")
    @Basic
    var result: String? = null



    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as SchedulerEntity
        return id == that.id &&
                taskType == that.taskType &&
                notificationId == that.notificationId &&
                userId == that.userId &&
                emailAddress == that.emailAddress &&
                status == that.status &&
                createdOn == that.createdOn &&
                executedOn == that.executedOn &&
                cancelledOn == that.cancelledOn &&
                processInstanceId == that.processInstanceId &&
                taskId == that.taskId &&
                taskDefinitionKey == that.taskDefinitionKey &&
                result == that.result
    }

    override fun hashCode(): Int {
        return Objects.hash(id, taskType, notificationId, userId, emailAddress, status, createdOn, executedOn, cancelledOn,processInstanceId,taskId,taskDefinitionKey,result)
    }

}