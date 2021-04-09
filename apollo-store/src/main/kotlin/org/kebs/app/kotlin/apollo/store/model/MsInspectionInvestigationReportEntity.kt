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

    @Column(name = "OBJECTIVE_INVESTIGATION")
    @Basic
    var objectiveInvestigation: String? = null

    @Column(name = "DATE_INVESTIGATION_INSPECTION")
    @Basic
    var dateInvestigationInspection: Time? = null

    @Column(name = "KEBS_INSPECTORS")
    @Basic
    var kebsInspectors: String? = null

    @Column(name = "METHODOLOGY_EMPLOYED")
    @Basic
    var methodologyEmployed: String? = null

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
    var status: String? = null

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

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as MsInspectionInvestigationReportEntity
        return id == that.id &&
                reportReference == that.reportReference &&
                reportTo == that.reportTo &&
                reportThrough == that.reportThrough &&
                reportFrom == that.reportFrom &&
                reportSubject == that.reportSubject &&
                reportTitle == that.reportTitle &&
                reportDate == that.reportDate &&
                reportRegion == that.reportRegion &&
                reportDepartment == that.reportDepartment &&
                reportFunction == that.reportFunction &&
                backgroundInformation == that.backgroundInformation &&
                objectiveInvestigation == that.objectiveInvestigation &&
                dateInvestigationInspection == that.dateInvestigationInspection &&
                kebsInspectors == that.kebsInspectors &&
                methodologyEmployed == that.methodologyEmployed &&
                conclusion == that.conclusion &&
                recommendations == that.recommendations &&
                statusActivity == that.statusActivity &&
                finalRemarkHod == that.finalRemarkHod &&
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
        return Objects.hash(id, reportReference, reportTo, reportThrough, reportFrom, reportSubject, reportTitle, reportDate, reportRegion, reportDepartment, reportFunction, backgroundInformation, objectiveInvestigation, dateInvestigationInspection, kebsInspectors, methodologyEmployed, conclusion, recommendations, statusActivity, finalRemarkHod, status, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn)
    }
}