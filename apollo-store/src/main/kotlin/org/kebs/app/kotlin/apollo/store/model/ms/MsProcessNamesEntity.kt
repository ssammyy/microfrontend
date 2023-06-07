package org.kebs.app.kotlin.apollo.store.model.ms

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "CFG_KEBS_MS_PROCESS_NAMES", schema = "APOLLO", catalog = "")
class MsProcessNamesEntity : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "CFG_KEBS_MS_PROCESS_NAMES_SEQ_GEN", allocationSize = 1, sequenceName = "CFG_KEBS_MS_PROCESS_NAMES_SEQ")
    @GeneratedValue(generator = "CFG_KEBS_MS_PROCESS_NAMES_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = null

    @Basic
    @Column(name = "PROCESS_BY")
    var processBy: String? = null

    @Basic
    @Column(name = "PROCESS_NAME")
    var processName: String? = null

    @Basic
    @Column(name = "TIMELINES_DAY")
    var timelinesDay: Int? = null

    @Basic
    @Column(name = "DESCRIPTION")
    var description: String? = null

    @Basic
    @Column(name = "COMPLAINT_STATUS")
    var complaintStatus: Int? = null

    @Basic
    @Column(name = "WORK_PLAN_STATUS")
    var workPlanStatus: Int? = null

    @Basic
    @Column(name = "FUEL_STATUS")
    var fuelStatus: Int? = null

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
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as MsProcessNamesEntity
        return id == that.id && processBy == that.processBy && processName == that.processName && description == that.description && complaintStatus == that.complaintStatus && workPlanStatus == that.workPlanStatus && fuelStatus == that.fuelStatus && status == that.status && varField1 == that.varField1 && varField2 == that.varField2 && varField3 == that.varField3 && varField4 == that.varField4 && varField5 == that.varField5 && varField6 == that.varField6 && varField7 == that.varField7 && varField8 == that.varField8 && varField9 == that.varField9 && varField10 == that.varField10 && createdBy == that.createdBy && createdOn == that.createdOn && modifiedBy == that.modifiedBy && modifiedOn == that.modifiedOn && deleteBy == that.deleteBy && deletedOn == that.deletedOn
    }

    override fun hashCode(): Int {
        return Objects.hash(
            id,
            processBy,
            processName,
            description,
            complaintStatus,
            workPlanStatus,
            fuelStatus,
            status,
            varField1,
            varField2,
            varField3,
            varField4,
            varField5,
            varField6,
            varField7,
            varField8,
            varField9,
            varField10,
            createdBy,
            createdOn,
            modifiedBy,
            modifiedOn,
            deleteBy,
            deletedOn
        )
    }
}
