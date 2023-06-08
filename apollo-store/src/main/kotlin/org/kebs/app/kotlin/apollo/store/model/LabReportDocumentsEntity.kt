package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Time
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_LAB_REPORT_DOCUMENTS")
class LabReportDocumentsEntity: Serializable {
    @Column(name = "ID")
    @Id
    @SequenceGenerator(name = "DAT_KEBS_LAB_REPORT_DOCUMENTS_SEQ_GEN", sequenceName = "DAT_KEBS_LAB_REPORT_DOCUMENTS_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_LAB_REPORT_DOCUMENTS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long = 0

    @Column(name = "FILEPATH")
    @Basic
    var filepath: String? = null

    @JoinColumn(name = "PERMIT_ID", referencedColumnName = "ID")
    @ManyToOne
    var permitId: PermitApplicationEntity? = null

    @JoinColumn(name = "SAMPLE_SUBMISSION_ENTITY", referencedColumnName = "ID")
    @ManyToOne
    var sampleSubmissionEntity: CdSampleSubmissionItemsEntity? = null

    @JoinColumn(name = "LAB_REPORT", referencedColumnName = "ID")
    @ManyToOne
    var labReport: LabReportsEntity? = null

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

    @Column(name = "TRANSACTION_DATE")
    @Basic
    var transactionDate: Date? = null

    @Column(name = "DOCUMENT_TYPE")
    @Basic
    var documentType: String? = null

    @Column(name = "FILE_TYPE")
    @Basic
    var fileType: String? = null

    @Column(name = "NAME")
    @Basic
    var name: String? = null

    @Lob
    @Column(name = "DOCUMENT")
    var document: ByteArray? = null


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other  as LabReportDocumentsEntity
        return id == that.id &&
                filepath == that.filepath &&
                description == that.description &&
                status == that.status &&
                varField1 == that.varField1 &&
                varField2 == that.varField2 &&
                varField3 == that.varField3 &&
                varField4 == that.varField4 &&
                varField5 == that.varField5 &&
                permitId == that.permitId &&
                varField6 == that.varField6 &&
                varField7 == that.varField7 &&
                varField8 == that.varField8 &&
                varField9 == that.varField9 &&
                varField10 == that.varField10 &&
                createdBy == that.createdBy &&
                createdOn == that.createdOn &&
                modifiedBy == that.modifiedBy &&
                sampleSubmissionEntity == that.sampleSubmissionEntity &&
                modifiedOn == that.modifiedOn &&
                deleteBy == that.deleteBy &&
                deletedOn == that.deletedOn &&
                transactionDate == that.transactionDate &&
                documentType == that.documentType &&
                fileType == that.fileType &&
                labReport == that.labReport &&
                name == that.name &&
                Arrays.equals(document, that.document)
    }

    override fun hashCode(): Int {
        var result = Objects.hash(id, filepath, labReport, description, sampleSubmissionEntity, permitId, status, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn, transactionDate, documentType, fileType, name)
        result = 31 * result + Arrays.hashCode(document)
        return result
    }
}