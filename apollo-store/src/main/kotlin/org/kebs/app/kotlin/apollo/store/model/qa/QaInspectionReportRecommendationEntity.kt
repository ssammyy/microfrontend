package org.kebs.app.kotlin.apollo.store.model.qa

import java.io.Serializable
import java.sql.Date
import java.sql.Time
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KERBS_QA_INSPECTION_REPORT_RECOMMENDATION")
class QaInspectionReportRecommendationEntity : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(
        name = "DAT_KERBS_QA_INSPECTION_REPORT_RECOMMENDATION_SEQ_GEN",
        allocationSize = 1,
        sequenceName = "DAT_KERBS_QA_INSPECTION_REPORT_RECOMMENDATION_SEQ"
    )
    @GeneratedValue(
        generator = "DAT_KERBS_QA_INSPECTION_REPORT_RECOMMENDATION_SEQ_GEN",
        strategy = GenerationType.SEQUENCE
    )
    @Id
    var id: Long? = null

    @Column(name = "RECOMMENDATIONS")
    @Basic
    var recommendations: String? = null

    @Column(name = "FOLLOW_PREVIOUS_RECOMMENDATIONS_NON_CONFORMITIES")
    @Basic
    var followPreviousRecommendationsNonConformities: String? = null

    @Column(name = "REF_NO")
    @Basic
    var refNo: String? = null

    @Column(name = "INSPECTOR_COMMENTS")
    @Basic
    var inspectorComments: String? = null

    @Column(name = "INSPECTOR_NAME")
    @Basic
    var inspectorName: String? = null

    @Column(name = "INSPECTOR_DATE")
    @Basic
    var inspectorDate: Date? = null

    @Column(name = "SUPERVISOR_COMMENTS")
    @Basic
    var supervisorComments: String? = null

    @Column(name = "SUPERVISOR_NAME")
    @Basic
    var supervisorName: String? = null

    @Column(name = "SUPERVISOR_DATE")
    @Basic
    var supervisorDate: Date? = null

    @Column(name = "DESCRIPTION")
    @Basic
    var description: String? = null

    @Column(name = "PERMIT_ID")
    @Basic
    var permitId: Long? = null

    @Column(name = "PERMIT_REF_NUMBER")
    @Basic
    var permitRefNumber: String? = null

    @Column(name = "FILLED_QPSMS_STATUS")
    @Basic
    var filledQpsmsStatus: Int? = null

    @Column(name = "FILLED_INSPECTION_TESTING_STATUS")
    @Basic
    var filledInspectionTestingStatus: Int? = null

    @Column(name = "FILLED_STANDARDIZATION_MARK_SCHEME_STATUS")
    @Basic
    var filledStandardizationMarkSchemeStatus: Int? = null

    @Column(name = "FILLED_OPC_STATUS")
    @Basic
    var filledOpcStatus: Int? = null

    @Column(name = "FILLED_HACCP_IMPLEMENTATION_STATUS")
    @Basic
    var filledHaccpImplementationStatus: Int? = null

    @Column(name = "SUBMITTED_INSPECTION_REPORT_STATUS")
    @Basic
    var submittedInspectionReportStatus: Int? = null

    @Column(name = "SUPERVISOR_FILLED_STATUS")
    @Basic
    var supervisorFilledStatus: Int? = null

    @Column(name = "APPROVED_REJECTED_STATUS")
    @Basic
    var approvedRejectedStatus: Int? = null

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
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as QaInspectionReportRecommendationEntity
        return id == that.id && recommendations == that.recommendations
                && refNo == that.refNo
                && filledQpsmsStatus == that.filledQpsmsStatus
                && filledInspectionTestingStatus == that.filledInspectionTestingStatus
                && filledStandardizationMarkSchemeStatus == that.filledStandardizationMarkSchemeStatus
                && filledHaccpImplementationStatus == that.filledHaccpImplementationStatus
                && supervisorFilledStatus == that.supervisorFilledStatus
                && approvedRejectedStatus == that.approvedRejectedStatus
                && filledOpcStatus == that.filledOpcStatus
                && permitRefNumber == that.permitRefNumber
                && submittedInspectionReportStatus == that.submittedInspectionReportStatus && inspectorComments == that.inspectorComments && inspectorName == that.inspectorName && inspectorDate == that.inspectorDate && supervisorComments == that.supervisorComments && supervisorName == that.supervisorName && supervisorDate == that.supervisorDate && description == that.description && permitId == that.permitId && status == that.status && varField1 == that.varField1 && varField2 == that.varField2 && varField3 == that.varField3 && varField4 == that.varField4 && varField5 == that.varField5 && varField6 == that.varField6 && varField7 == that.varField7 && varField8 == that.varField8 && varField9 == that.varField9 && varField10 == that.varField10 && createdBy == that.createdBy && createdOn == that.createdOn && modifiedBy == that.modifiedBy && modifiedOn == that.modifiedOn && deleteBy == that.deleteBy && deletedOn == that.deletedOn
    }

    override fun hashCode(): Int {
        return Objects.hash(
            id,
            refNo,
            supervisorFilledStatus,
            approvedRejectedStatus,
            filledQpsmsStatus,
            permitRefNumber,
            filledInspectionTestingStatus,
            filledStandardizationMarkSchemeStatus,
            filledOpcStatus,
            filledHaccpImplementationStatus,
            submittedInspectionReportStatus,
            recommendations,
            inspectorComments,
            inspectorName,
            inspectorDate,
            supervisorComments,
            supervisorName,
            supervisorDate,
            description,
            permitId,
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
