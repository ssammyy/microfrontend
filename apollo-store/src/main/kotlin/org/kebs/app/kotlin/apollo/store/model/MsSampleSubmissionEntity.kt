package org.kebs.app.kotlin.apollo.store.model

import org.kebs.app.kotlin.apollo.store.model.ms.MsFuelInspectionEntity
import org.kebs.app.kotlin.apollo.store.model.ms.MsSampleCollectionEntity
import java.io.Serializable
import java.math.BigDecimal
import java.sql.Date
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_MS_SAMPLE_SUBMISSION")
class MsSampleSubmissionEntity : Serializable{
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_MS_SAMPLE_SUBMISSION_SEQ_GEN", allocationSize = 1, sequenceName = "DAT_KEBS_MS_SAMPLE_SUBMISSION_SEQ")
    @GeneratedValue(generator = "DAT_KEBS_MS_SAMPLE_SUBMISSION_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long = 0

    @Column(name = "LAB_RESULTS_STATUS")
    @Basic
    var labResultsStatus: Int? = null

    @Column(name = "CREATED_USER_ID")
    @Basic
    var createdUserId: Long? = null

    @Column(name = "NAME_PRODUCT")
    @Basic
    var nameProduct: String? = null

    @Column(name = "PRODUCT_DESCRIPTION")
    @Basic
    var productDescription: String? = null

    @Column(name = "SOURCE_PRODUCT_EVIDENCE")
    @Basic
    var sourceProductEvidence: String? = null

    @Column(name = "LB_ID_ANY_AOMARKING")
    @Basic
    var lbIdAnyAomarking: String? = null

    @Column(name = "LB_ID_BATCH_NO")
    @Basic
    var lbIdBatchNo: String? = null

    @Column(name = "LB_ID_CONT_DECL")
    @Basic
    var lbIdContDecl: String? = null

    @Column(name = "LB_ID_DATE_OF_MANF")
    @Basic
    var lbIdDateOfManf: Date? = null

    @Column(name = "LB_ID_EXPIRY_DATE")
    @Basic
    var lbIdExpiryDate: Date? = null

    @Column(name = "RECEIVERS_DATE")
    @Basic
    var receiversDate: Date? = null

    @Column(name = "SAMPLE_COLLECTION_DATE")
    @Basic
    var sampleCollectionDate: Date? = null

    @Column(name = "LB_ID_TRADE_MARK")
    @Basic
    var lbIdTradeMark: String? = null

    @Column(name = "NOTE_TRANS_RESULTS")
    @Basic
    var noteTransResults: String? = null

    @Column(name = "SCF_NO")
    @Basic
    var scfNo: String? = null

    @Column(name = "COC_NUMBER")
    @Basic
    var cocNumber: String? = null

    @Column(name = "BS_NUMBER")
    @Basic
    var bsNumber: String? = null

    @Column(name = "LAB_REF")
    @Basic
    var labRef: String? = null

    @Column(name = "ADDITIONAL_INF_PROVIDED_CUSTOMER")
    @Basic
    var additionalInfProvidedCustomer: String? = null

    @Column(name = "DATE_ANALYSIS_STARTED")
    @Basic
    var dateAnalsyisStarted: Date? = null

    @Column(name = "DATE_OF_RECEIPT")
    @Basic
    var dateOfReceipt: Date? = null

    @Column(name = "SIZE_TEST_SAMPLE")
    @Basic
    var sizeTestSample: String? = null

    @Column(name = "SIZE_REF_SAMPLE")
    @Basic
    var sizeRefSample: String? = null

    @Column(name = "FILE_REF_NUMBER")
    @Basic
    var fileRefNumber: String? = null

    @Column(name = "PACKAGING")
    @Basic
    var packaging: String? = null

    @Column(name = "LABELLING_IDENTIFICATION")
    @Basic
    var labellingIdentification: String? = null

    @Column(name = "CONDITION")
    @Basic
    var condition: String? = null

    @Column(name = "REFERENCES_STANDARDS")
    @Basic
    var referencesStandards: String? = null

    @Column(name = "SENDERS_NAME")
    @Basic
    var sendersName: String? = null

    @Column(name = "DESIGNATION")
    @Basic
    var designation: String? = null

    @Column(name = "SIGNATURE")
    @Basic
    var signature: String? = null

    @Column(name = "ADDRESS")
    @Basic
    var address: String? = null

    @Column(name = "SENDERS_DATE")
    @Basic
    var sendersDate: Date? = null

    @Column(name = "RECEIVERS_NAME")
    @Basic
    var receiversName: String? = null

    @Column(name = "SAMPLE_REFERENCES")
    @Basic
    var sampleReferences: String? = null

    @Column(name = "TEST_CHARGES_KSH")
    @Basic
    var testChargesKsh: BigDecimal? = null

    @Column(name = "RECEIPT_LPO_NUMBER")
    @Basic
    var receiptLpoNumber: String? = null

    @Column(name = "INVOICE_NUMBER")
    @Basic
    var invoiceNumber: String? = null

    @Column(name = "DISPOSAL")
    @Basic
    var disposal: String? = null

    @Column(name = "REMARKS")
    @Basic
    var remarks: String? = null

    @Column(name = "STATUS")
    @Basic
    var status: Int? = 0

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

    @Column(name = "MS_WORKPLAN_GENERATED_ID")
    @Basic
    var workPlanGeneratedID: Long? = null

    @Column(name = "MS_FUEL_INSPECTION_ID")
    @Basic
    var msFuelInspectionId: Long? = null

    @Column(name = "SAMPLE_COLLECTION_NUMBER")
    @Basic
    var sampleCollectionNumber: Long? = null

    @Column(name = "SAMPLE_BS_NUMBER_DATE")
    @Basic
    var sampleBsNumberDate: Date? = null

    @Column(name = "SAMPLE_BS_NUMBER_REMARKS")
    @Basic
    var sampleBsNumberRemarks: String? = null
}
