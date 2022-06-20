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

    @Column(name = "MS_WORKPLAN_GENERATED_ID")
    @Basic
    var workPlanGeneratedID: Long? = null
}
