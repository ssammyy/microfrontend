package org.kebs.app.kotlin.apollo.store.model.ms

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_MS_REMARKS", schema = "APOLLO", catalog = "")
class MsRemarksEntity : Serializable {
    @SequenceGenerator(name = "DAT_KEBS_MS_REMARKS_SEQ_GEN", allocationSize = 1, sequenceName = "DAT_KEBS_MS_REMARKS_SEQ")
    @GeneratedValue(generator = "DAT_KEBS_MS_REMARKS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    @Column(name = "ID")
    var id: Long? = null

    @Basic
    @Column(name = "REMARKS_DESCRIPTION")
    var remarksDescription: String? = null

    @Basic
    @Column(name = "REMARKS_STATUS")
    var remarksStatus: String? = null

    @Basic
    @Column(name = "PROCESS_BY")
    var processBy: String? = null

    @Basic
    @Column(name = "PROCESS_NAME")
    var processName: String? = null

    @Column(name = "MS_PROCESS_ID")
    @Basic
    var msProcessId: Long? = null


    @Basic
    @Column(name = "DESCRIPTION")
    var description: String? = null

    @Basic
    @Column(name = "USER_ID")
    var userId: Long? = null

    @Basic
    @Column(name = "COMPLAINT_ID")
    var complaintId: Long? = null

    @Basic
    @Column(name = "FUEL_INSPECTION_ID")
    var fuelInspectionId: Long? = null

    @Basic
    @Column(name = "WORK_PLAN_ID")
    var workPlanId: Long? = null

    @Basic
    @Column(name = "FUEL_BATCH_ID")
    var fuelBatchId: Long? = null

    @Basic
    @Column(name = "STATUS")
    var status: Int? = null

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
