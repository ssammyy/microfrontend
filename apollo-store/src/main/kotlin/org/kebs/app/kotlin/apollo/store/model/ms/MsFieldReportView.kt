package org.kebs.app.kotlin.apollo.store.model.ms

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "MS_FIELD_REPORT", schema = "APOLLO", catalog = "")
class MsFieldReportView : Serializable {
    @Column(name = "ID")
    @Id
    var id: String? = null

    @Basic
    @Column(name = "MS_WORKPLAN_GENERATED_ID")
    var msWorkplanGeneratedId: String? = null

    @Basic
    @Column(name = "CREATED_USER_ID")
    var createdUserId: String? = null

    @Basic
    @Column(name = "REPORT_REFERENCE")
    var reportReference: String? = null

    @Basic
    @Column(name = "REPORT_CLASSIFICATION")
    var reportClassification: String? = null

    @Basic
    @Column(name = "REPORT_TO")
    var reportTo: String? = null

    @Basic
    @Column(name = "REPORT_THROUGH")
    var reportThrough: String? = null

    @Basic
    @Column(name = "REPORT_FROM")
    var reportFrom: String? = null

    @Basic
    @Column(name = "REPORT_SUBJECT")
    var reportSubject: String? = null

    @Basic
    @Column(name = "REPORT_TITLE")
    var reportTitle: String? = null

    @Basic
    @Column(name = "REPORT_DATE")
    var reportDate: String? = null

    @Basic
    @Column(name = "REPORT_REGION")
    var reportRegion: String? = null

    @Basic
    @Column(name = "REPORT_DEPARTMENT")
    var reportDepartment: String? = null

    @Basic
    @Column(name = "REPORT_FUNCTION")
    var reportFunction: String? = null

    @Basic
    @Column(name = "BACKGROUND_INFORMATION")
    var backgroundInformation: String? = null

    @Basic
    @Column(name = "OBJECTIVE_INVESTIGATION")
    var objectiveInvestigation: String? = null

    @Basic
    @Column(name = "DATE_INVESTIGATION_INSPECTION")
    var dateInvestigationInspection: String? = null

    @Basic
    @Column(name = "METHODOLOGY_EMPLOYED")
    var methodologyEmployed: String? = null

    @Basic
    @Column(name = "ADDITIONAL_INFORMATION")
    var additionalInformation: String? = null

    @Basic
    @Column(name = "CONCLUSION")
    var conclusion: String? = null

    @Basic
    @Column(name = "RECOMMENDATIONS")
    var recommendations: String? = null

    @Basic
    @Column(name = "STATUS_ACTIVITY")
    var statusActivity: String? = null

    @Basic
    @Column(name = "FINAL_REMARK_HOD")
    var finalRemarkHod: String? = null

    @Basic
    @Column(name = "FINDINGS")
    var findings: String? = null

    @Basic
    @Column(name = "KEBS_INSPECTORS")
    var kebsInspectors: String? = null

    @Basic
    @Column(name = "VAR_FIELD_1")
    var varField1: String? = null

    @Basic
    @Column(name = "CREATED_ON")
    var createdOn: String? = null

    @Basic
    @Column(name = "CREATED_BY")
    var createdBy: String? = null

    @Basic
    @Column(name = "MODIFIED_BY")
    var modifiedBy: String? = null

    @Basic
    @Column(name = "MODIFIED_ON")
    var modifiedOn: String? = null

}
