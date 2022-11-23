package org.kebs.app.kotlin.apollo.store.model.ms

import org.kebs.app.kotlin.apollo.store.model.ms.MsAllocatedTasksCpViewEntity
import org.kebs.app.kotlin.apollo.store.model.ms.MsAllocatedTasksWpViewEntity
import java.math.BigInteger
import java.sql.Date
import java.util.*
import javax.persistence.Basic
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "MS_ALLOCATED_TASKS_CP_VIEW", schema = "APOLLO", catalog = "")
class MsAllocatedTasksCpViewEntity {
    @Basic
    @Column(name = "REFERENCE_NUMBER")
    var referenceNumber: String? = null

    @Basic
    @Column(name = "TRANSACTION_DATE")
    var transactionDate: Date? = null

    @Basic
    @Column(name = "TIMELINE_START_DATE")
    var timelineStartDate: Date? = null

    @Basic
    @Column(name = "TIMELINE_END_DATE")
    var timelineEndDate: Date? = null

    @Basic
    @Column(name = "ASSIGNED_IO")
    var assignedIo: BigInteger? = null

    @Basic
    @Column(name = "HOD_ASSIGNED")
    var hodAssigned: BigInteger? = null

    @Basic
    @Column(name = "HOF_ASSIGNED")
    var hofAssigned: BigInteger? = null

    @Basic
    @Column(name = "USER_TASK_ID")
    var userTaskId: BigInteger? = null

    @Basic
    @Column(name = "MS_COMPLAINT_ENDED_STATUS")
    var msComplaintEndedStatus: BigInteger? = null

    @Basic
    @Column(name = "TASK_OVER_DUE")
    var taskOverDue: String? = null
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as MsAllocatedTasksCpViewEntity
        return referenceNumber == that.referenceNumber && transactionDate == that.transactionDate && timelineStartDate == that.timelineStartDate && timelineEndDate == that.timelineEndDate && assignedIo == that.assignedIo && hodAssigned == that.hodAssigned && hofAssigned == that.hofAssigned && userTaskId == that.userTaskId && msComplaintEndedStatus == that.msComplaintEndedStatus && taskOverDue == that.taskOverDue
    }

    override fun hashCode(): Int {
        return Objects.hash(
            referenceNumber,
            transactionDate,
            timelineStartDate,
            timelineEndDate,
            assignedIo,
            hodAssigned,
            hofAssigned,
            userTaskId,
            msComplaintEndedStatus,
            taskOverDue
        )
    }
}
