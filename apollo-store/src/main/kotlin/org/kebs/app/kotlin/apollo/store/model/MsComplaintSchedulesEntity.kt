package org.kebs.app.kotlin.apollo.store.model

import org.kebs.app.kotlin.apollo.store.model.ms.ComplaintEntity
import java.io.Serializable
import java.sql.Date
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_MS_COMPLAINT_SCHEDULES")
class MsComplaintSchedulesEntity : Serializable{
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_MS_COMPLAINT_SCHEDULES_SEQ_GEN", allocationSize = 1, sequenceName = "DAT_KEBS_MS_COMPLAINT_SCHEDULES_SEQ")
    @GeneratedValue(generator = "DAT_KEBS_MS_COMPLAINT_SCHEDULES_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long = 0

    @Column(name = "HOD_DESK_ARRIVAL_DATE")
    @Basic
    var hodDeskArrivalDate: Date? = null

    @Column(name = "HOD_DESK_DUE_DATE")
    @Basic
    var hodDeskDueDate: Date? = null

    @Column(name = "HOD_DESK_LEAVE_DAYS")
    @Basic
    var hodDeskLeaveDays: Int? = null

    @Column(name = "HOF_DESK_ARRIVAL_DATE")
    @Basic
    var hofDeskArrivalDate: Date? = null

    @Column(name = "HOF_DESK_DUE_DATE")
    @Basic
    var hofDeskDueDate: Date? = null

    @Column(name = "HOF_DESK_LEAVE_DAYS")
    @Basic
    var hofDeskLeaveDays: Int? = null

    @Column(name = "IO_DESK_ARRIVAL_DATE")
    @Basic
    var ioDeskArrivalDate: Date? = null

    @Column(name = "IO_DESK_ALERT_DATE")
    @Basic
    var ioDeskAlertDate: Date? = null

    @Column(name = "IO_DESK_ALERT_DAYS")
    @Basic
    var ioDeskAlertDays: Int? = null

    @Column(name = "HODIO_DESK_ARRIVAL_DATE")
    @Basic
    var hodioDeskArrivalDate: Date? = null

    @Column(name = "HODIO_DESK_DUE_DATE")
    @Basic
    var hodioDeskDueDate: Date? = null

    @Column(name = "HODIO_DESK_LEAVE_DAYS")
    @Basic
    var hodioDeskLeaveDays: Int? = null

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

    @JoinColumn(name = "COMPLAINT_ID", referencedColumnName = "ID")
    @ManyToOne
    var complaintId: ComplaintEntity? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as MsComplaintSchedulesEntity
        return id == that.id &&
                hodDeskArrivalDate == that.hodDeskArrivalDate &&
                hodDeskDueDate == that.hodDeskDueDate &&
                hodDeskLeaveDays == that.hodDeskLeaveDays &&
                hofDeskArrivalDate == that.hofDeskArrivalDate &&
                hofDeskDueDate == that.hofDeskDueDate &&
                hofDeskLeaveDays == that.hofDeskLeaveDays &&
                ioDeskArrivalDate == that.ioDeskArrivalDate &&
                ioDeskAlertDate == that.ioDeskAlertDate &&
                ioDeskAlertDays == that.ioDeskAlertDays &&
                hodioDeskArrivalDate == that.hodioDeskArrivalDate &&
                hodioDeskDueDate == that.hodioDeskDueDate &&
                hodioDeskLeaveDays == that.hodioDeskLeaveDays &&
                status == that.status &&
                varField1 == that.varField1 &&
                varField2 == that.varField2 &&
                varField3 == that.varField3 &&
                varField4 == that.varField4 &&
                varField5 == that.varField5 &&
                varField6 == that.varField6 &&
                varField7 == that.varField7 &&
                varField8 == that.varField8 &&
                varField9 == that.varField9 &&
                varField10 == that.varField10 &&
                createdBy == that.createdBy &&
                createdOn == that.createdOn &&
                modifiedBy == that.modifiedBy &&
                modifiedOn == that.modifiedOn &&
                deleteBy == that.deleteBy &&
                deletedOn == that.deletedOn
    }

    override fun hashCode(): Int {
        return Objects.hash(id, hodDeskArrivalDate, hofDeskDueDate, hodDeskLeaveDays, hodDeskDueDate, hofDeskArrivalDate, hofDeskLeaveDays, ioDeskAlertDate, ioDeskArrivalDate,  ioDeskAlertDays, hodioDeskArrivalDate, hodioDeskLeaveDays, status, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn)
    }
}