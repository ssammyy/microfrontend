package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
//@Table(name = "DAT_PETROLEUM_INSTALLATION_INSPECTION")
@Table(name = "DAT_PETROLEUM_INSTALLATION_INSPECTION")
class PetroleumInstallationInspectionEntity: Serializable {
    @Column(name = "ID")
    @Id
    @SequenceGenerator(name = "DAT_PETROLEUM_INSTALLATION_INSPECTION_SEQ_GEN", sequenceName = "DAT_PETROLEUM_INSTALLATION_INSPECTION_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_PETROLEUM_INSTALLATION_INSPECTION_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long = 0

    @Column(name = "STATION_NAME")
    @Basic
    var stationName: String? = null

    @Column(name = "DIRECTORS_APPROVAL")
    @Basic
    var directorsApproval: Int? = null

    @Column(name = "PETROLEUM_MANAGER_REPORT_APPROVAL")
    @Basic
    var petroleumManagerReportApproval: Int? = null

    @Column(name = "DIRECTOR_ASSIGNEE")
    @Basic
    var directorAssignee: Long? = null

    @Column(name = "PETROLEUM_MANAGER_ASSIGNEE")
    @Basic
    var petroleumManagerAssignee: Long? = null

    @Column(name = "LOCATION")
    @Basic
    var location: String? = null

    @Column(name = "CONTACT_EMAIL")
    @Basic
    var contactEmail: String? = null

    @Column(name = "INSPECTION_REPORT_AVAILABILITY")
    @Basic
    var inspectionReportAvailability: Int? = null

    @Column(name = "INSPECTION_REPORT_APPROVAL")
    @Basic
    var inspectionReportApproval: Int? = null

    @Column(name = "SCHEDULED_DATE")
    @Basic
    var scheduledDate: String? = null

    @Column(name = "INSPECTION_STATUS")
    @Basic
    var inspectionStatus: Long? = null

    @Column(name = "SCHEDULESTATUS")
    @Basic
    var schedulestatus: Long? = null

    @Column(name = "VISITSTATUS")
    @Basic
    var visitstatus: Long? = null

    @Column(name = "STATUS")
    @Basic
    var status: Long? = null

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

    @Column(name = "INVOICE_PAYMENT_STATUS")
    @Basic
    var invoicePaymentStatus: Int? = null

    @Column(name = "INVOICE_STATUS")
    @Basic
    var invoiceStatus: Int? = null

    @Column(name = "INSPECTION_STAGE")
    @Basic
    var inspectionStage: String? = null

    @Column(name = "DELETE_BY")
    @Basic
    var deleteBy: String? = null

    @Column(name = "ASSIGNED_OFFICER")
    @Basic
    var assignedOfficer: Long? = null

    @Column(name = "DELETED_ON")
    @Basic
    var deletedOn: Timestamp? = null

    @Column(name = "SECTION")
    @Basic
    var section: String? = null

    @Column(name = "IIS_STATUS")
    @Basic
    var iisStatus: Int? = null

    @Column(name = "IIS_STARTED_ON")
    @Basic
    var iisStartedOn: Timestamp? = null

    @Column(name = "IIS_COMPLETED_ON")
    @Basic
    var iisCompletedOn: Timestamp? = null

    @Column(name = "IIS_PROCESS_INSTANCE_ID")
    @Basic
    var iisProcessInstanceId: String? = null

    @Column(name = "IIR_STATUS")
    @Basic
    var iirStatus: Int? = null

    @Column(name = "IIR_STARTED_ON")
    @Basic
    var iirStartedOn: Timestamp? = null

    @Column(name = "IIR_COMPLETED_ON")
    @Basic
    var iirCompletedOn: Timestamp? = null

    @Column(name = "IIR_PROCESS_INSTANCE_ID")
    @Basic
    var iirProcessInstanceId: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as PetroleumInstallationInspectionEntity
        return id == that.id &&
                stationName == that.stationName &&
                location == that.location &&
                contactEmail == that.contactEmail &&
                inspectionStatus == that.inspectionStatus &&
                schedulestatus == that.schedulestatus &&
                visitstatus == that.visitstatus &&
                status == that.status &&
                assignedOfficer == that.assignedOfficer &&
                scheduledDate == that.scheduledDate &&
                varField1 == that.varField1 &&
                varField2 == that.varField2 &&
                inspectionReportApproval == that.inspectionReportApproval &&
                invoicePaymentStatus == that.invoicePaymentStatus &&
                varField3 == that.varField3 &&
                inspectionStage == that.inspectionStage &&
                varField4 == that.varField4 &&
                directorAssignee == that.directorAssignee &&
                varField5 == that.varField5 &&
                invoiceStatus == that.invoiceStatus &&
                varField6 == that.varField6 &&
                inspectionReportAvailability == that.inspectionReportAvailability &&
                varField7 == that.varField7 &&
                varField8 == that.varField8 &&
                varField9 == that.varField9 &&
                petroleumManagerAssignee == that.petroleumManagerAssignee &&
                varField10 == that.varField10 &&
                petroleumManagerReportApproval == that.petroleumManagerReportApproval &&
                createdBy == that.createdBy &&
                createdOn == that.createdOn &&
                modifiedBy == that.modifiedBy &&
                modifiedOn == that.modifiedOn &&
                deleteBy == that.deleteBy &&
                deletedOn == that.deletedOn &&
                directorsApproval == that.directorsApproval &&
                section == that.section
    }

    override fun hashCode(): Int {
        return Objects.hash(id, stationName, directorsApproval, petroleumManagerReportApproval, petroleumManagerAssignee, directorAssignee, inspectionReportApproval, inspectionReportAvailability, inspectionStage, invoiceStatus, invoicePaymentStatus, assignedOfficer, scheduledDate, location, contactEmail, inspectionStatus, schedulestatus, visitstatus, status, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn, section)
    }
}