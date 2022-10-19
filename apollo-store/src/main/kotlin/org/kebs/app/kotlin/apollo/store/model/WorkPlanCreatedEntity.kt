package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Date
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_WORKPLAN_CREATED")
class WorkPlanCreatedEntity : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_WORKPLAN_CREATED_SEQ_GEN", allocationSize = 1, sequenceName = "DAT_WORKPLAN_CREATED_SEQ")
    @GeneratedValue(generator = "DAT_WORKPLAN_CREATED_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long = 0


    @Transient
    var confirmYearNameId: Long? = null

    @Column(name = "WORKPLAN_REGION")
    @Basic
    var workPlanRegion: Long? = null

//    @Column(name = "USER_CREATED_ID")
//    @Basic
//    var userCreatedId: Long? = null

    @Column(name = "CREATED_DATE")
    @Basic
    var createdDate: Date? = null

    @Column(name = "CREATED_STATUS")
    @Basic
    var createdStatus: Int? = null

    @Column(name = "ENDED_DATE")
    @Basic
    var endedDate: Date? = null

    @Column(name = "ENDED_STATUS")
    @Basic
    var endedStatus: Int? = null

    @Column(name = "WORK_PLAN_STATUS")
    @Basic
    var workPlanStatus: Int? = null

    @Column(name = "COMPLAINT_STATUS")
    @Basic
    var complaintStatus: Int? = null

    @Column(name = "BATCH_CLOSED")
    @Basic
    var batchClosed: Int? = null

    @Column(name = "USER_TASK_ID")
    @Basic
    var userTaskId: Long? = null

    @Column(name = "REFERENCE_NUMBER")
    @Basic
    var referenceNumber: String? = null

    @Column(name = "UUID")
    @Basic
    var uuid: String? = null

    @Column(name = "DESCRIPTION")
    @Basic
    var description: String? = null

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

    @Column(name = "DELETE_BY")
    @Basic
    var deleteBy: String? = null

    @Column(name = "DELETED_ON")
    @Basic
    var deletedOn: Timestamp? = null

    @JoinColumn(name = "USER_CREATED_ID", referencedColumnName = "ID")
    @ManyToOne
    var userCreatedId: UsersEntity? = null

    @JoinColumn(name = "YEAR_NAME_ID", referencedColumnName = "ID")
    @ManyToOne
    var yearNameId: WorkplanYearsCodesEntity? = null
}
