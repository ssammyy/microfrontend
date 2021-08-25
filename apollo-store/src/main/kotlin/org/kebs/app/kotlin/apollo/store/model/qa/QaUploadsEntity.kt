package org.kebs.app.kotlin.apollo.store.model.qa

import java.io.Serializable
import java.sql.Time
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_QA_UPLOADS")
class QaUploadsEntity : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_QA_UPLOADS_SEQ_GEN", allocationSize = 1, sequenceName = "DAT_KEBS_QA_UPLOADS_SEQ")
    @GeneratedValue(generator = "DAT_KEBS_QA_UPLOADS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = 0

    @Column(name = "FILEPATH")
    @Basic
    var filepath: String? = null

    @Column(name = "PERMIT_REF_NUMBER")
    @Basic
    var permitRefNumber: String? = null

    @Column(name = "DESCRIPTION")
    @Basic
    var description: String? = null

    @Column(name = "NAME")
    @Basic
    var name: String? = null

    @Column(name = "INVOICE_STATUS")
    @Basic
    var invoiceStatus: Int? = null

    @Column(name = "SSF_UPLOADS")
    @Basic
    var ssfUploads: Int? = null

    @Column(name = "STA3_STATUS")
    @Basic
    var sta3Status: Int? = null

    @Column(name = "VERSION_NUMBER")
    @Basic
    var versionNumber: Long? = null

    @Column(name = "INSPECTION_REPORT_ID")
    @Basic
    var inspectionReportId: Long? = null

    @Column(name = "ASSESSMENT_REPORT_STATUS")
    @Basic
    var assessmentReportStatus: Int? = null

    @Column(name = "SSC_STATUS")
    @Basic
    var sscStatus: Int? = null

    @Column(name = "STA10_STATUS")
    @Basic
    var sta10Status: Int? = null

    @Column(name = "COC_STATUS")
    @Basic
    var cocStatus: Int? = null

    @Column(name = "INSPECTION_REPORT_STATUS")
    @Basic
    var inspectionReportStatus: Int? = null

    @Column(name = "ORDINARY_STATUS")
    @Basic
    var ordinaryStatus: Int? = null

    @Column(name = "FILE_TYPE")
    @Basic
    var fileType: String? = null

    @Column(name = "DOCUMENT_TYPE")
    @Basic
    var documentType: String? = null

    @Column(name = "DOCUMENT")
    @Lob
    var document: ByteArray? = null

    @Column(name = "TRANSACTION_DATE")
    @Basic
    var transactionDate: Date? = null

    @Column(name = "PERMIT_ID")
    @Basic
    var permitId: Long? = null

    @Column(name = "NON_MANUFACTURE_STATUS")
    @Basic
    var nonManufactureStatus: Int? = null

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
        val that = other as QaUploadsEntity
        return id == that.id &&
                filepath == that.filepath &&
                versionNumber == that.versionNumber &&
                inspectionReportId == that.inspectionReportId &&
                sta3Status == that.sta3Status &&
                sscStatus == that.sscStatus &&
                cocStatus == that.cocStatus &&
                inspectionReportStatus == that.inspectionReportStatus &&
                ordinaryStatus == that.ordinaryStatus &&
                ssfUploads == that.ssfUploads &&
                description == that.description &&
                name == that.name &&
                fileType == that.fileType &&
                sta10Status == that.sta10Status &&
                documentType == that.documentType &&
                Arrays.equals(document, that.document) &&
                transactionDate == that.transactionDate &&
                permitId == that.permitId &&
                nonManufactureStatus == that.nonManufactureStatus &&
                assessmentReportStatus == that.assessmentReportStatus &&
                invoiceStatus == that.invoiceStatus &&
                status == that.status &&
                permitRefNumber == that.permitRefNumber &&
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
        var result = Objects.hash(
            id,
            sta3Status,
            inspectionReportId,
            ssfUploads,
            invoiceStatus,
            assessmentReportStatus,
            versionNumber,
            cocStatus,
            sta10Status,
            sscStatus,
            inspectionReportStatus,
            ordinaryStatus,
            filepath,
            description,
            name,
            permitRefNumber,
            fileType,
            documentType,
            transactionDate,
            permitId,
            nonManufactureStatus,
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
        result = 31 * result + Arrays.hashCode(document)
        return result
    }
}