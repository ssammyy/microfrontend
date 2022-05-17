package org.kebs.app.kotlin.apollo.store.model.di

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_CD_STANDARDS_TWO")
class CdStandardsTwoEntity : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_CD_STANDARDS_TWO_SEQ_GEN", sequenceName = "DAT_KEBS_CD_STANDARDS_TWO_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_CD_STANDARDS_TWO_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long = 0

    @Column(name = "PURPOSE_OF_IMPORT")
    @Basic
    var purposeOfImport: String? = null

    @Column(name = "COC_REF_NO")
    @Basic
    var cocRefNumber: String? = null

    @Column(name = "COC_TYPE")
    @Basic
    var cocType: String? = null

    @Column(name = "LOCAL_COC_TYPE")
    @Basic
    var localCocType: String? = null

    @Column(name = "DESCRIPTION")
    @Basic
    var description: String? = null

    @Column(name = "COURIER_PIN")
    @Basic
    var courierPin: String? = null

    @Column(name = "COURIER_NAME")
    @Basic
    var courierName: String? = null

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

    @Column(name = "CONDITIONS_OF_APPROVAL")
    @Basic
    var conditionsOfApproval: String? = null

    @Column(name = "APPLICANT_REMARKS")
    @Basic
    var applicantRemarks: String? = null

    @Column(name = "MDA_REMARKS")
    @Basic
    var mdaRemarks: String? = null

    @Column(name = "CUSTOMS_REMARKS")
    @Basic
    var customsRemarks: String? = null

    @Column(name = "CD_PROCESSING_FEE_ID")
    @Basic
    var cdProcessingFeeId: Long? = null

    @Column(name = "CD_DOCUMENT_FEE_ID")
    @Basic
    var cdDocumentFeeId: Long? = null
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as CdStandardsTwoEntity
        return id == that.id &&
                purposeOfImport == that.purposeOfImport &&
                cocType == that.cocType &&
                localCocType == that.localCocType &&
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
                deletedOn == that.deletedOn &&
                conditionsOfApproval == that.conditionsOfApproval &&
                applicantRemarks == that.applicantRemarks &&
                mdaRemarks == that.mdaRemarks &&
                customsRemarks == that.customsRemarks &&
                cdProcessingFeeId == that.cdProcessingFeeId &&
                cdDocumentFeeId == that.cdDocumentFeeId
    }

    override fun hashCode(): Int {
        return Objects.hash(id, purposeOfImport, cocType, localCocType, description, status, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn, conditionsOfApproval, applicantRemarks, mdaRemarks, customsRemarks, cdProcessingFeeId, cdDocumentFeeId)
    }
}