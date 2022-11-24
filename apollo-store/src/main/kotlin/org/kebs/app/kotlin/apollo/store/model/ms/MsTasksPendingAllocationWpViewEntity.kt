package org.kebs.app.kotlin.apollo.store.model.ms

import java.io.Serializable
import java.sql.Date
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "MS_TASKS_PENDING_ALLOCATION_WP_VIEW", schema = "APOLLO", catalog = "")
class MsTasksPendingAllocationWpViewEntity: Serializable {
    @Id
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
    var officerId: Long? = null

    @Basic
    @Column(name = "HOD_RM_ASSIGNED")
    var hodRmAssigned: Long? = null

    @Basic
    @Column(name = "HOF_ASSIGNED")
    var hofAssigned: Long? = null

    @Basic
    @Column(name = "USER_TASK_ID")
    var userTaskId: Long? = null

    @Basic
    @Column(name = "COMPLAINT_ID")
    var complaintId: Long? = null

    @Basic
    @Column(name = "FINAL_REPORT_GENERATED")
    var finalReportGenerated: Byte? = null

    @Basic
    @Column(name = "INVEST_INSPECT_REPORT_STATUS")
    var investInspectReportStatus: Byte? = null

    @Basic
    @Column(name = "MS_PRELIMINARY_REPORT_STATUS")
    var msPreliminaryReportStatus: Byte? = null

    @Basic
    @Column(name = "DATA_REPORT_STATUS")
    var dataReportStatus: Byte? = null

    @Basic
    @Column(name = "FIELD_REPORT_STATUS")
    var fieldReportStatus: Byte? = null

    @Basic
    @Column(name = "MS_PROCESS_ID")
    var msProcessId: Long? = null

    @Basic
    @Column(name = "MS_PROCESS_ENDED_STATUS")
    var msProcessEndedStatus: Byte? = null

    @Basic
    @Column(name = "TASK_OVER_DUE")
    var taskOverDue: String? = null

}
