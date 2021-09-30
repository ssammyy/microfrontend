package org.kebs.app.kotlin.apollo.store.model.di

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_CD_STANDARDS")
class CdStandardsEntity : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_CD_STANDARDS_SEQ_GEN", sequenceName = "DAT_KEBS_CD_STANDARDS_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_CD_STANDARDS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long = 0

    @Column(name = "APPLICATION_TYPE_CODE")
    @Basic
    var applicationTypeCode: String? = null

    @Column(name = "APPLICATION_TYPE_DESCRIPTION")
    @Basic
    var applicationTypeDescription: String? = null

    @Column(name = "DOCUMENT_TYPE_CODE")
    @Basic
    var documentTypeCode: String? = null

    @Column(name = "CMS_DOCUMENT_TYPE_CODE")
    @Basic
    var cmsDocumentTypeCode: String? = null

    @Column(name = "DOCUMENT_TYPE_DESCRIPTION")
    @Basic
    var documentTypeDescription: String? = null

    @Column(name = "CONSIGNMENT_TYPE_CODE")
    @Basic
    var consignmentTypeCode: String? = null

    @Column(name = "CONSIGNMENT_TYPE_DESCRIPTION")
    @Basic
    var consignmentTypeDescription: String? = null

    @Column(name = "MDA_CODE")
    @Basic
    var mdaCode: String? = null


    @Column(name = "EXPIRY_DATE")
    @Basic
    var expiryDate: String? = null

    @Column(name = "AMENDED_DATE")
    @Basic
    var amendedDate: String? = null

    @Column(name = "USED_STATUS")
    @Basic
    var usedStatus: String? = null

    @Column(name = "USED_DATE")
    @Basic
    var usedDate: String? = null

    @Column(name = "REFERENCED_PERMIT_EXEMPTION_NO")
    @Basic
    var referencedPermitExemptionNo: String? = null

    @Column(name = "REFERENCED_PERMIT_EXEMPTION_VERSION_NO")
    @Basic
    var referencedPermitExemptionVersionNo: String? = null

    @Column(name = "MDA_DESCRIPTION")
    @Basic
    var mdaDescription: String? = null

    @Column(name = "DOCUMENT_CODE")
    @Basic
    var documentCode: String? = null

    @Column(name = "DOCUMENT_DESCRIPTION")
    @Basic
    var documentDescription: String? = null

    @Column(name = "PROCESS_CODE")
    @Basic
    var processCode: String? = null

    @Column(name = "PROCESS_DESCRIPTION")
    @Basic
    var processDescription: String? = null

    @Column(name = "APPLICATION_DATE")
    @Basic
    var applicationDate: String? = null

    @Column(name = "UPDATED_DATE")
    @Basic
    var updatedDate: String? = null

    @Column(name = "APPROVAL_STATUS")
    @Basic
    var approvalStatus: String? = null

    @Column(name = "STATUS_ID")
    @Basic
    var statusId: Long? = null

    @Column(name = "APPROVAL_DATE")
    @Basic
    var approvalDate: String? = null

    @Column(name = "FINAL_APPROVAL_DATE")
    @Basic
    var finalApprovalDate: String? = null

    @Column(name = "APPLICATION_REF_NO")
    @Basic
    var applicationRefNo: String? = null

    @Column(name = "VERSION_NO")
    @Basic
    var versionNo: String? = null

    @Column(name = "UCR_NUMBER")
    @Basic
    var ucrNumber: String? = null

    @Column(name = "DECLARATION_NUMBER")
    @Basic
    var declarationNumber: String? = null

    @Column(name = "DESCRIPTION")
    @Basic
    var description: String? = null

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

    @JoinColumn(name = "SERVICE_PROVIDER", referencedColumnName = "ID")
    @ManyToOne
    var cdServiceProvider: CdServiceProviderEntity? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as CdStandardsEntity
        return id == that.id &&
                expiryDate == that.expiryDate &&
                amendedDate == that.amendedDate &&
                usedStatus == that.usedStatus &&
                usedDate == that.usedDate &&
                referencedPermitExemptionNo == that.referencedPermitExemptionNo &&
                referencedPermitExemptionVersionNo == that.referencedPermitExemptionVersionNo &&
                applicationTypeCode == that.applicationTypeCode &&
                applicationTypeDescription == that.applicationTypeDescription &&
                documentTypeCode == that.documentTypeCode &&
                cmsDocumentTypeCode == that.cmsDocumentTypeCode &&
                documentTypeDescription == that.documentTypeDescription &&
                consignmentTypeCode == that.consignmentTypeCode &&
                consignmentTypeDescription == that.consignmentTypeDescription &&
                mdaCode == that.mdaCode &&
                mdaDescription == that.mdaDescription &&
                documentCode == that.documentCode &&
                documentDescription == that.documentDescription &&
                processCode == that.processCode &&
                processDescription == that.processDescription &&
                applicationDate == that.applicationDate &&
                updatedDate == that.updatedDate &&
                approvalStatus == that.approvalStatus &&
                approvalDate == that.approvalDate &&
                finalApprovalDate == that.finalApprovalDate &&
                applicationRefNo == that.applicationRefNo &&
                versionNo == that.versionNo &&
                ucrNumber == that.ucrNumber &&
                declarationNumber == that.declarationNumber &&
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
        return Objects.hash(id, applicationTypeCode, expiryDate,
                amendedDate,
                usedStatus,
                usedDate,
                referencedPermitExemptionNo,
                referencedPermitExemptionVersionNo, applicationTypeDescription, documentTypeCode, cmsDocumentTypeCode, documentTypeDescription, consignmentTypeCode, consignmentTypeDescription, mdaCode, mdaDescription, documentCode, documentDescription, processCode, processDescription, applicationDate, updatedDate, approvalStatus, approvalDate, finalApprovalDate, applicationRefNo, versionNo, ucrNumber, declarationNumber, description, status, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn)
    }
}