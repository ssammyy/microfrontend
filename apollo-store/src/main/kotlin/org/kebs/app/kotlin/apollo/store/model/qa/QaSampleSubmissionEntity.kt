package org.kebs.app.kotlin.apollo.store.model.qa

import java.io.Serializable
import java.sql.Date
import java.sql.Time
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_QA_SAMPLE_SUBMISSION")
class QaSampleSubmissionEntity : Serializable {
    @Column(name = "ID")
    @Id
    @SequenceGenerator(name = "DAT_KEBS_QA_SAMPLE_SUBMISSION_SEQ_GEN", sequenceName = "DAT_KEBS_QA_SAMPLE_SUBMISSION_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_QA_SAMPLE_SUBMISSION_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long? = 0

    @Column(name = "PERMIT_ID")
    @Basic
    var permitId: Long? = null

    @Column(name = "FUEL_INSPECTION_ID")
    @Basic
    var fuelInspectionId: Long? = null

    @Column(name = "WORKPLAN_GENERATED_ID")
    @Basic
    var workplanGeneratedId: Long? = null

    @Column(name = "PERMIT_REF_NUMBER")
    @Basic
    var permitRefNumber: String? = null

    @Column(name = "COMPLIANCE_REMARKS")
    @Basic
    var complianceRemarks: String? = null

    @Column(name = "CD_ITEM_ID")
    @Basic
    var cdItemId: Long? = null

    @Column(name = "SSF_NO")
    @Basic
    var ssfNo: String? = null

    @Column(name = "SSF_SUBMISSION_DATE")
    @Basic
    var ssfSubmissionDate: Date? = null

    @Column(name = "BS_NUMBER")
    @Basic
    var bsNumber: String? = null

    @Column(name = "BRAND_NAME")
    @Basic
    var brandName: String? = null

    @Column(name = "CATEGORY")
    @Basic
    var category: String? = null


    @Column(name = "PRODUCT_DESCRIPTION")
    @Basic
    var productDescription: String? = null

    @Column(name = "SAMPLE_STATUS")
    @Basic
    var sampleStatus: String? = null

    @Column(name = "RESULTS_DATE")
    @Basic
    var resultsDate: Date? = null

    @Column(name = "RESULTS_ANALYSIS")
    @Basic
    var resultsAnalysis: Int? = null

    @Column(name = "DESCRIPTION")
    @Basic
    var description: String? = null

    @Column(name = "PDF_SELECTED_NAME")
    @Basic
    var pdfSelectedName: String? = null

    @Column(name = "LAB_REPORT_FILE_ID")
    @Basic
    var labReportFileId: Long? = null

    @Column(name = "RETURN_OR_DISPOSE")
    @Basic
    var returnOrDispose: String? = null

    @Column(name = "TEST_PARAMETERS")
    @Basic
    var testParameters: String? = null

    @Column(name = "LABORATORY_NAME")
    @Basic
    var laboratoryName: String? = null

    @Column(name = "CONDITION_OF_SAMPLE")
    @Basic
    var conditionOfSample: String? = null

    @Column(name = "STATUS")
    @Basic
    var status: Int? = null

    @Column(name = "LAB_RESULTS_STATUS")
    @Basic
    var labResultsStatus: Int? = null

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
