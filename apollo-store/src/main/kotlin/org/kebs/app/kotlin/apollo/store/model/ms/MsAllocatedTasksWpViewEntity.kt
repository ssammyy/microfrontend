package org.kebs.app.kotlin.apollo.store.model.ms

import org.kebs.app.kotlin.apollo.store.model.ms.MsAllocatedTasksCpViewEntity
import org.kebs.app.kotlin.apollo.store.model.ms.MsAllocatedTasksWpViewEntity
import java.math.BigInteger
import java.sql.Date
import java.sql.Timestamp
import java.util.*
import javax.persistence.Basic
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "MS_ALLOCATED_TASKS_WP_VIEW", schema = "APOLLO", catalog = "")
class MsAllocatedTasksWpViewEntity {
    @Basic
    @Column(name = "REFERENCE_NUMBER")
    var referenceNumber: String? = null

    @Basic
    @Column(name = "CREATED_ON")
    var createdOn: Timestamp? = null

    @Basic
    @Column(name = "TIMELINE_START_DATE")
    var timelineStartDate: Date? = null

    @Basic
    @Column(name = "TIMELINE_END_DATE")
    var timelineEndDate: Date? = null

    @Basic
    @Column(name = "OFFICER_ID")
    var officerId: BigInteger? = null

    @Basic
    @Column(name = "HOD_RM_ASSIGNED")
    var hodRmAssigned: BigInteger? = null

    @Basic
    @Column(name = "HOF_ASSIGNED")
    var hofAssigned: BigInteger? = null

    @Basic
    @Column(name = "USER_TASK_ID")
    var userTaskId: BigInteger? = null

    @Basic
    @Column(name = "COMPLAINT_ID")
    var complaintId: BigInteger? = null

    @Basic
    @Column(name = "MS_PROCESS_ENDED_STATUS")
    var msProcessEndedStatus: Byte? = null

    @Basic
    @Column(name = "TASK_OVER_DUE")
    var taskOverDue: String? = null
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as MsAllocatedTasksWpViewEntity
        return referenceNumber == that.referenceNumber && createdOn == that.createdOn && timelineStartDate == that.timelineStartDate && timelineEndDate == that.timelineEndDate && officerId == that.officerId && hodRmAssigned == that.hodRmAssigned && hofAssigned == that.hofAssigned && userTaskId == that.userTaskId && complaintId == that.complaintId && msProcessEndedStatus == that.msProcessEndedStatus && taskOverDue == that.taskOverDue
    }

    override fun hashCode(): Int {
        return Objects.hash(
            referenceNumber,
            createdOn,
            timelineStartDate,
            timelineEndDate,
            officerId,
            hodRmAssigned,
            hofAssigned,
            userTaskId,
            complaintId,
            msProcessEndedStatus,
            taskOverDue
        )
    }
}
