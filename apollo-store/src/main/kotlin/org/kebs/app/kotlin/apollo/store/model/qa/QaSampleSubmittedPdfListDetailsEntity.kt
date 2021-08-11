package org.kebs.app.kotlin.apollo.store.model.qa

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_QA_SAMPLE_SUBMITTED_PDF_LIST_DETAILS")
class QaSampleSubmittedPdfListDetailsEntity : Serializable {
    @Column(name = "ID")
    @Id
    @SequenceGenerator(
        name = "DAT_KEBS_QA_SAMPLE_SUBMITTED_PDF_LIST_DETAILS_SEQ_GEN",
        sequenceName = "DAT_KEBS_QA_SAMPLE_SUBMITTED_PDF_LIST_DETAILS_SEQ",
        allocationSize = 1
    )
    @GeneratedValue(
        generator = "DAT_KEBS_QA_SAMPLE_SUBMITTED_PDF_LIST_DETAILS_SEQ_GEN",
        strategy = GenerationType.SEQUENCE
    )
    var id: Long? = 0

    @Column(name = "PDF_SAVED_ID")
    @Basic
    var pdfSavedId: Long? = null

    @Column(name = "PDF_NAME")
    @Basic
    var pdfName: String? = null

    @Column(name = "SFF_ID")
    @Basic
    var sffId: Long? = null

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

    @Column(name = "COMPLIANCE_REMARKS")
    @Basic
    var complianceRemarks: String? = null

    @Column(name = "COMPLIANCE_STATUS")
    @Basic
    var complianceStatus: Int? = null

    @Column(name = "SENT_TO_MANUFACTURER_STATUS")
    @Basic
    var sentToManufacturerStatus: Int? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as QaSampleSubmittedPdfListDetailsEntity
        return id == that.id &&
                pdfSavedId == that.pdfSavedId &&
                sentToManufacturerStatus == that.sentToManufacturerStatus &&
                pdfName == that.pdfName && sffId == that.sffId && description == that.description && status == that.status && varField1 == that.varField1 && varField2 == that.varField2 && varField3 == that.varField3 && varField4 == that.varField4 && varField5 == that.varField5 && varField6 == that.varField6 && varField7 == that.varField7 && varField8 == that.varField8 && varField9 == that.varField9 && varField10 == that.varField10 && createdBy == that.createdBy && createdOn == that.createdOn && modifiedBy == that.modifiedBy && modifiedOn == that.modifiedOn && deleteBy == that.deleteBy && deletedOn == that.deletedOn && complianceRemarks == that.complianceRemarks && complianceStatus == that.complianceStatus
    }

    override fun hashCode(): Int {
        return Objects.hash(
            id,
            pdfSavedId,
            sentToManufacturerStatus,
            pdfName,
            sffId,
            description,
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
            deletedOn,
            complianceRemarks,
            complianceStatus
        )
    }
}