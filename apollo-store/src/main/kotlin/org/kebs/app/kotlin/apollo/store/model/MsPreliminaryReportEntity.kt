package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Date
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_MS_PRELIMINARY_REPORT")
class MsPreliminaryReportEntity: Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_MS_PRELIMINARY_REPORT_SEQ_GEN", allocationSize = 1, sequenceName = "DAT_KEBS_MS_PRELIMINARY_REPORT_SEQ")
    @GeneratedValue(generator = "DAT_KEBS_MS_PRELIMINARY_REPORT_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long = 0

    @Column(name = "REPORT_TO")
    @Basic
    var reportTo: String? = null

    @Column(name = "REPORT_FROM")
    @Basic
    var reportFrom: String? = null

    @Column(name = "REPORT_SUBJECT")
    @Basic
    var reportSubject: String? = null

    @Column(name = "REPORT_TITLE")
    @Basic
    var reportTitle: String? = null

    @Column(name = "REPORT_DATE")
    @Basic
    var reportDate: Date? = null

    @Column(name = "SURVEILLANCE_DATE_FROM")
    @Basic
    var surveillanceDateFrom: Date? = null

    @Column(name = "SURVEILLANCE_DATE_TO")
    @Basic
    var surveillanceDateTo: Date? = null

    @Column(name = "REPORT_BACKGROUND")
    @Basic
    var reportBackground: String? = null

    @Column(name = "KEBS_OFFICERS_NAME")
    @Basic
    var kebsOfficersName: String? = null

    @Column(name = "SURVEILLANCE_OBJECTIVE")
    @Basic
    var surveillanceObjective: String? = null

    @Column(name = "SURVEILLANCE_CONCLUSIONS")
    @Basic
    var surveillanceConclusions: String? = null

    @Column(name = "SURVEILLANCE_RECOMMENDATION")
    @Basic
    var surveillanceRecommendation: String? = null

    @Column(name = "REMARKS")
    @Basic
    var remarks: String? = null

    @Column(name = "DESCRIPTION")
    @Basic
    var description: String? = null

    @Column(name = "REJECTED_BY_HOD")
    @Basic
    var rejectedByHod: String? = null

    @Column(name = "REJECTED_HOD")
    @Basic
    var rejectedHod: String? = null

    @Column(name = "REJECTED_REMARKS_HOD")
    @Basic
    var rejectedRemarksHod: String? = null

    @Column(name = "APPROVED_REMARKS_HOD")
    @Basic
    var approvedRemarksHod: String? = null

    @Column(name = "APPROVED_BY_HOD")
    @Basic
    var approvedByHod: String? = null

    @Column(name = "APPROVED_ON_HOD")
    @Basic
    var approvedOnHod: Date? = null

    @Column(name = "APPROVED_STATUS_HOD")
    @Basic
    var approvedStatusHod: Int? = 0

    @Column(name = "APPROVED_HOD")
    @Basic
    var approvedHod: String? = null

    @Column(name = "REJECTED_ON_HOD")
    @Basic
    var rejectedOnHod: Date? = null

    @Column(name = "REJECTED_STATUS_HOD")
    @Basic
    var rejectedStatusHod: Int? = 0



    @Column(name = "REJECTED_BY_HOD_FINAL")
    @Basic
    var rejectedByHodFinal: String? = null

    @Column(name = "REJECTED_HOD_FINAL")
    @Basic
    var rejectedHodFinal: String? = null

    @Column(name = "REJECTED_REMARKS_HOD_FINAL")
    @Basic
    var rejectedRemarksHodFinal: String? = null

    @Column(name = "APPROVED_REMARKS_HOD_FINAL")
    @Basic
    var approvedRemarksHodFinal: String? = null

    @Column(name = "APPROVED_BY_HOD_FINAL")
    @Basic
    var approvedByHodFinal: String? = null

    @Column(name = "APPROVED_ON_HOD_FINAL")
    @Basic
    var approvedOnHodFinal: Date? = null

    @Column(name = "APPROVED_STATUS_HOD_FINAL")
    @Basic
    var approvedStatusHodFinal: Int? = 0

    @Column(name = "APPROVED_HOD_FINAL")
    @Basic
    var approvedHodFinal: String? = null

    @Column(name = "REJECTED_ON_HOD_FINAL")
    @Basic
    var rejectedOnHodFinal: Date? = null

    @Column(name = "REJECTED_STATUS_HOD_FINAL")
    @Basic
    var rejectedStatusHodFinal: Int? = 0


    @Column(name = "REJECTED_BY_HOF_FINAL")
    @Basic
    var rejectedByHofFinal: String? = null

    @Column(name = "REJECTED_HOF_FINAL")
    @Basic
    var rejectedHofFinal: String? = null

    @Column(name = "REJECTED_REMARKS_HOF_FINAL")
    @Basic
    var rejectedRemarksHofFinal: String? = null

    @Column(name = "APPROVED_REMARKS_HOF_FINAL")
    @Basic
    var approvedRemarksHofFinal: String? = null

    @Column(name = "APPROVED_BY_HOF_FINAL")
    @Basic
    var approvedByHofFinal: String? = null

    @Column(name = "APPROVED_ON_HOF_FINAL")
    @Basic
    var approvedOnHofFinal: Date? = null

    @Column(name = "APPROVED_STATUS_HOF_FINAL")
    @Basic
    var approvedStatusHofFinal: Int? = 0

    @Column(name = "APPROVED_HOF_FINAL")
    @Basic
    var approvedHofFinal: String? = null

    @Column(name = "REJECTED_ON_HOF_FINAL")
    @Basic
    var rejectedOnHofFinal: Date? = null

    @Column(name = "REJECTED_STATUS_HOF_FINAL")
    @Basic
    var rejectedStatusHofFinal: Int? = 0



    @Column(name = "REJECTED_BY")
    @Basic
    var rejectedBy: String? = null

    @Column(name = "REJECTED")
    @Basic
    var rejected: String? = null

    @Column(name = "REJECTED_REMARKS")
    @Basic
    var rejectedRemarks: String? = null

    @Column(name = "APPROVED_REMARKS")
    @Basic
    var approvedRemarks: String? = null

    @Column(name = "APPROVED_BY")
    @Basic
    var approvedBy: String? = null

    @Column(name = "APPROVED_ON")
    @Basic
    var approvedOn: Date? = null

    @Column(name = "APPROVED_STATUS")
    @Basic
    var approvedStatus: Int? = 0

    @Column(name = "APPROVED")
    @Basic
    var approved: String? = null

    @Column(name = "REJECTED_ON")
    @Basic
    var rejectedOn: Date? = null

    @Column(name = "REJECTED_STATUS")
    @Basic
    var rejectedStatus: Int? = 0

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

    @JoinColumn(name = "MS_WORKPLAN_GENERATED_ID", referencedColumnName = "ID")
    @ManyToOne
    var workPlanGeneratedID: MsWorkPlanGeneratedEntity? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as MsPreliminaryReportEntity
        return id == that.id &&
                remarks == that.remarks &&
                description == that.description &&
                rejected == that.rejected &&
                rejectedOn == that.rejectedOn &&
                rejectedBy == that.rejectedBy &&
                rejectedStatus == that.rejectedStatus &&
                approved == that.approved &&
                approvedBy == that.approvedBy &&
                approvedOn == that.approvedOn &&
                approvedStatus == that.approvedStatus &&
                rejectedRemarks == that.rejectedRemarks &&
                approvedRemarks == that.approvedRemarks &&

                rejectedHod == that.rejectedHod &&
                rejectedOnHod == that.rejectedOnHod &&
                rejectedByHod == that.rejectedByHod &&
                rejectedStatusHod == that.rejectedStatusHod &&
                approvedHod == that.approvedHod &&
                approvedByHod == that.approvedByHod &&
                approvedOnHod == that.approvedOnHod &&
                approvedStatusHod == that.approvedStatusHod &&
                rejectedRemarksHod == that.rejectedRemarksHod &&
                approvedRemarksHod == that.approvedRemarksHod &&

                rejectedHodFinal == that.rejectedHodFinal &&
                rejectedOnHodFinal == that.rejectedOnHodFinal &&
                rejectedByHodFinal == that.rejectedByHodFinal &&
                rejectedStatusHodFinal == that.rejectedStatusHodFinal &&
                approvedHodFinal == that.approvedHodFinal &&
                approvedByHodFinal == that.approvedByHodFinal &&
                approvedOnHodFinal == that.approvedOnHodFinal &&
                approvedStatusHodFinal == that.approvedStatusHodFinal &&
                rejectedRemarksHodFinal == that.rejectedRemarksHodFinal &&
                approvedRemarksHodFinal == that.approvedRemarksHodFinal &&


                rejectedHofFinal == that.rejectedHofFinal &&
                rejectedOnHofFinal == that.rejectedOnHofFinal &&
                rejectedByHofFinal == that.rejectedByHofFinal &&
                rejectedStatusHofFinal == that.rejectedStatusHofFinal &&
                approvedHofFinal == that.approvedHofFinal &&
                approvedByHofFinal == that.approvedByHofFinal &&
                approvedOnHofFinal == that.approvedOnHofFinal &&
                approvedStatusHofFinal == that.approvedStatusHofFinal &&
                rejectedRemarksHofFinal == that.rejectedRemarksHofFinal &&
                approvedRemarksHofFinal == that.approvedRemarksHofFinal &&


                reportTo == that.reportTo &&
                reportFrom == that.reportFrom &&
                reportSubject == that.reportSubject &&
                reportTitle == that.reportTitle &&
                reportDate == that.reportDate &&
                surveillanceDateFrom == that.surveillanceDateFrom &&
                surveillanceDateTo == that.surveillanceDateTo &&
                reportBackground == that.reportBackground &&
                kebsOfficersName == that.kebsOfficersName &&
                surveillanceObjective == that.surveillanceObjective &&
                surveillanceConclusions == that.surveillanceConclusions &&
                surveillanceRecommendation == that.surveillanceRecommendation &&

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
        return Objects.hash(id, remarks,
                reportTo, reportFrom, reportSubject, reportTitle, reportDate, surveillanceDateFrom, surveillanceDateTo, reportBackground, kebsOfficersName, surveillanceObjective, surveillanceConclusions, surveillanceRecommendation,
                approved, approvedRemarks, rejectedRemarks, approvedBy, approvedOn, approvedStatus, rejected, rejectedOn, rejectedBy, rejectedStatus,
                approvedHod, approvedRemarksHod, rejectedRemarksHod, approvedByHod, approvedOnHod, approvedStatusHod, rejectedHod, rejectedOnHod, rejectedByHod, rejectedStatusHod,
                approvedHodFinal, approvedRemarksHodFinal, rejectedRemarksHodFinal, approvedByHodFinal, approvedOnHodFinal, approvedStatusHodFinal, rejectedHodFinal, rejectedOnHodFinal, rejectedByHodFinal, rejectedStatusHodFinal,
                approvedHofFinal, approvedRemarksHofFinal, rejectedRemarksHofFinal, approvedByHofFinal, approvedOnHofFinal, approvedStatusHofFinal, rejectedHofFinal, rejectedOnHofFinal, rejectedByHofFinal, rejectedStatusHofFinal,
                description, status, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn)
    }
}