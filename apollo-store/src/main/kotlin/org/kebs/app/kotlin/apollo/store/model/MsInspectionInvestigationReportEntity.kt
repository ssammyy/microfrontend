package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Date
import java.sql.Time
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_MS_INSPECTION_INVESTIGATION_REPORT")
class MsInspectionInvestigationReportEntity: Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_MS_INSPECTION_INVESTIGATION_REPORT_SEQ_GEN", allocationSize = 1, sequenceName = "DAT_KEBS_MS_INSPECTION_INVESTIGATION_REPORT_SEQ")
    @GeneratedValue(generator = "DAT_KEBS_MS_INSPECTION_INVESTIGATION_REPORT_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = 0

    @Column(name = "REPORT_REFERENCE")
    @Basic
    var reportReference: String? = null

    @Column(name = "CREATED_USER_ID")
    @Basic
    var createdUserId: Long? = null

    @Column(name = "REPORT_TO")
    @Basic
    var reportTo: String? = null

    @Column(name = "REPORT_THROUGH")
    @Basic
    var reportThrough: String? = null

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

    @Column(name = "REPORT_REGION")
    @Basic
    var reportRegion: String? = null

    @Column(name = "REPORT_DEPARTMENT")
    @Basic
    var reportDepartment: String? = null

    @Column(name = "REPORT_FUNCTION")
    @Basic
    var reportFunction: String? = null

    @Column(name = "BACKGROUND_INFORMATION")
    @Basic
    var backgroundInformation: String? = null

    @Column(name = "ADDITIONAL_INFORMATION")
    @Basic
    var additionalInformation: String? = null

    @Column(name = "ADDITIONAL_INFORMATION_STATUS")
    @Basic
    var additionalInformationStatus: Int? = 0

    @Column(name = "OBJECTIVE_INVESTIGATION")
    @Basic
    var objectiveInvestigation: String? = null

    @Column(name = "DATE_INVESTIGATION_INSPECTION")
    @Basic
    var dateInvestigationInspection: Date? = null

    @Column(name = "KEBS_INSPECTORS")
    @Basic
    var kebsInspectors: String? = null

    @Column(name = "METHODOLOGY_EMPLOYED")
    @Basic
    var methodologyEmployed: String? = null

    @Column(name = "FINDINGS")
    @Basic
    var findings: String? = null

    @Column(name = "CONCLUSION")
    @Basic
    var conclusion: String? = null

    @Column(name = "RECOMMENDATIONS")
    @Basic
    var recommendations: String? = null

    @Column(name = "STATUS_ACTIVITY")
    @Basic
    var statusActivity: String? = null

    @Column(name = "FINAL_REMARK_HOD")
    @Basic
    var finalRemarkHod: String? = null

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
