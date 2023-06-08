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

    @Column(name = "LAB_RESULTS_REPORT_STATUS")
    @Basic
    var labResultsReportStatus: Int? = null

    @Column(name = "SSC_STATUS")
    @Basic
    var sscStatus: Int? = null

    @Column(name = "JUSTIFICATION_REPORT_STATUS")
    @Basic
    var justificationReportStatus: Int? = null

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
}
