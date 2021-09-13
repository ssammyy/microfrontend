package org.kebs.app.kotlin.apollo.store.model.di

import java.io.Serializable
import java.sql.Date
import java.sql.Timestamp
import java.util.*
import javax.persistence.*
import kotlin.jvm.Transient

@Entity
@Table(name = "DAT_KEBS_CD_INSPECTION_GENERAL")
class CdInspectionGeneralEntity : Serializable {
    
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_CD_INSPECTION_GENERAL_SEQ_GEN", sequenceName = "DAT_KEBS_CD_INSPECTION_GENERAL_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_CD_INSPECTION_GENERAL_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = null

    @Transient
    var confirmItemType: Long? = null

    @Column(name = "INSPECTION")
    @Basic
    var inspection: String? = null

    @Column(name = "CATEGORY")
    @Basic
    var category: String? = null

    @Column(name = "ENTRY_POINT")
    @Basic
    var entryPoint: String? = null

    @Column(name = "CFS")
    @Basic
    var cfs: String? = null

    @Column(name = "INSPECTION_DATE")
    @Basic
    var inspectionDate: Date? = null

    @Column(name = "IMPORTERS_NAME")
    @Basic
    var importersName: String? = null

    @Column(name = "CLEARING_AGENT")
    @Basic
    var clearingAgent: String? = null

    @Column(name = "CUSTOMS_ENTRY_NUMBER")
    @Basic
    var customsEntryNumber: String? = null

    @Column(name = "IDF_NUMBER")
    @Basic
    var idfNumber: String? = null

    @Column(name = "UCR_NUMBER")
    @Basic
    var ucrNumber: String? = null

    @Column(name = "COC_NUMBER")
    @Basic
    var cocNumber: String? = null

    @Column(name = "FEE_PAID")
    @Basic
    var feePaid: String? = null

    @Column(name = "RECEIPT_NUMBER")
    @Basic
    var receiptNumber: String? = null

    @Column(name = "OVERALL_REMARKS")
    @Basic
    var overallRemarks: String? = null

    @Column(name = "COMPLIANCE_STATUS")
    @Basic
    var complianceStatus: Int? = null

    @Column(name = "COMPLIANCE_RECOMMENDATIONS")
    @Basic
    var complianceRecommendations: String? = null

    @Column(name = "INSPECTION_REPORT_FILE")
    @Basic
    var inspectionReportFile: ByteArray? = null

    @Column(name = "INSPECTION_REPORT_APPROVAL_STATUS")
    @Basic
    var inspectionReportApprovalStatus: Int? = null

    @Column(name = "INSPECTION_REPORT_DISAPPROVAL_COMMENTS")
    @Basic
    var inspectionReportDisapprovalComments: String? = null

    @Column(name = "INSPECTION_REPORT_DISAPPROVAL_DATE")
    @Basic
    var inspectionReportDisapprovalDate: Date? = null

    @Column(name = "INSPECTION_REPORT_APPROVAL_COMMENTS")
    @Basic
    var inspectionReportApprovalComments: String? = null

    @Column(name = "INSPECTION_REPORT_APPROVAL_DATE")
    @Basic
    var inspectionReportApprovalDate: Date? = null

    @Column(name = "DESCRIPTION")
    @Basic
    var description: String? = null

    @Column(name = "INSPECTION_REPORT_REF_NUMBER")
    @Basic
    var inspectionReportRefNumber: String? = null

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

    @JoinColumn(name = "CD_DETAILS_ID", referencedColumnName = "ID")
    @ManyToOne
    var cdDetails: ConsignmentDocumentDetailsEntity? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as CdInspectionGeneralEntity
        return id == that.id &&
                inspectionReportRefNumber == that.inspectionReportRefNumber &&
                confirmItemType == that.confirmItemType &&
                inspection == that.inspection &&
                category == that.category &&
                entryPoint == that.entryPoint &&
                cfs == that.cfs &&
                inspectionDate == that.inspectionDate &&
                importersName == that.importersName &&
                clearingAgent == that.clearingAgent &&
                customsEntryNumber == that.customsEntryNumber &&
                idfNumber == that.idfNumber &&
                ucrNumber == that.ucrNumber &&
                cocNumber == that.cocNumber &&
                feePaid == that.feePaid &&
                receiptNumber == that.receiptNumber &&
                complianceStatus == that.complianceStatus &&
                complianceRecommendations == that.complianceRecommendations &&
                inspectionReportFile.contentEquals(that.inspectionReportFile) &&
                inspectionReportApprovalStatus == that.inspectionReportApprovalStatus &&
                inspectionReportDisapprovalComments == that.inspectionReportDisapprovalComments &&
                inspectionReportDisapprovalDate == that.inspectionReportDisapprovalDate &&
                inspectionReportApprovalComments == that.inspectionReportApprovalComments &&
                inspectionReportApprovalDate == that.inspectionReportApprovalDate &&
                description == that.description &&
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
        return Objects.hash(id,inspectionReportRefNumber, confirmItemType, inspection, category, entryPoint, cfs, inspectionDate, importersName, clearingAgent, customsEntryNumber, idfNumber, ucrNumber, cocNumber, feePaid, receiptNumber, complianceStatus, complianceRecommendations, inspectionReportFile, inspectionReportApprovalStatus, inspectionReportDisapprovalComments, inspectionReportDisapprovalDate, inspectionReportApprovalComments, inspectionReportApprovalDate, description, status, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn)
    }
}