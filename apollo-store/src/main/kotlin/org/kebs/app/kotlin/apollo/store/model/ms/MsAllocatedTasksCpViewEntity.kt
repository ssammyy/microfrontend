package org.kebs.app.kotlin.apollo.store.model.ms

import org.kebs.app.kotlin.apollo.store.model.ms.MsAllocatedTasksCpViewEntity
import org.kebs.app.kotlin.apollo.store.model.ms.MsAllocatedTasksWpViewEntity
import java.io.Serializable
import java.sql.Date
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "MS_ALLOCATED_TASKS_CP_VIEW", schema = "APOLLO", catalog = "")
class MsAllocatedTasksCpViewEntity : Serializable {
    @Id
    @Column(name = "REFERENCE_NUMBER")
    var referenceNumber: String? = null

    @Column(name = "MS_PROCESS_ID")
    @Basic
    var msProcessId: Long? = null

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
    var assignedIo: Long? = null

    @Basic
    @Column(name = "HOD_ASSIGNED")
    var hodAssigned: Long? = null

    @Basic
    @Column(name = "HOF_ASSIGNED")
    var hofAssigned: Long? = null

    @Basic
    @Column(name = "USER_TASK_ID")
    var userTaskId: Long? = null

    @Basic
    @Column(name = "MS_COMPLAINT_ENDED_STATUS")
    var msComplaintEndedStatus: Long? = null

    @Basic
    @Column(name = "TASK_OVER_DUE")
    var taskOverDue: String? = null
}
