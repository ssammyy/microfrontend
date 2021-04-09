package org.kebs.app.kotlin.apollo.store.model


import org.kebs.app.kotlin.apollo.store.model.di.CdLaboratoryEntity
import java.io.Serializable
import java.sql.Date
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_CD_SAMPLE_SUBMISSION_ITEMS")
class CdSampleSubmissionItemsEntity : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_CD_SAMPLE_SUBMISSION_ITEMS_SEQ_GEN", allocationSize = 1, sequenceName = "DAT_KEBS_CD_SAMPLE_SUBMISSION_ITEMS_SEQ")
    @GeneratedValue(generator = "DAT_KEBS_CD_SAMPLE_SUBMISSION_ITEMS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = 0

    @Column(name = "ITEM_HSCODE")
    @Basic
    var itemHscode: String? = null

    @Column(name = "LABELING")
    @Basic
    var labeling: String? = null

    @Column(name = "SAMPLE_REF_NUMBER")
    @Basic
    var sampleRefNumber: String? = null

    @Column(name = "REFERENCE_STANDARD_SPECIFICATION")
    @Basic
    var referenceStandardSpecification: String? = null

    @Column(name = "FILE_REFERENCE_NUMBER")
    @Basic
    var fileReferenceNumber: String? = null

    /*
    @JoinColumn(name = "SELECTED_LAB", referencedColumnName = "ID")
    @ManyToOne
    var selectedLab: CdLaboratoryEntity? = null
    */

//    @Column(name = "SELECTED_LAB")
//    @Basic
//    var selectedLab: Long? = null

    /*
    @JoinColumn(name = "WORKPLAN_ID", referencedColumnName = "ID")
    @ManyToOne
    var workplanId: MsWorkplanGeneratedEntity? = null
    */

    @Column(name = "WORKPLAN_ID", nullable = true)
    @Basic
    var workplanId: Long? = null

    @Column(name = "OBJECT_ID")
    @Basic
    var objectId: Int? = null

    @Column(name = "SAMPLES_COMPLETENESS")
    @Basic
    var samplesCompleteness: Int? = null

    @Column(name = "SAMPLE_SUBMISSION_FILE_PATH")
    @Basic
    var sampleSubmissionFileFilePath: String? = null

    @Column(name = "SAMPLES_COMPLETENESS_REMARKS")
    @Basic
    var samplesCompletenessRemarks: String? = null

    /*
    @JoinColumn(name = "PERMIT_ID")
    @ManyToOne
    var permitId: PermitApplicationEntity? = null
    */

    @Column(name = "PERMIT_ID")
    @Basic
    var permitId: Long? = null

    @Column(name = "REFERENCE_STANDARDS")
    @Basic
    var referenceStandards: String? = null

    @Column(name = "BS_NUMBER")
    @Basic
    var bsNumber: String? = null

    @Column(name = "ITEM_ID")
    @Basic
    var itemId: Long? = null

    @Column(name = "NAME_PRODUCT")
    @Basic
    var nameProduct: String? = null

    @Column(name = "PACKAGING")
    @Basic
    var packaging: String? = null

    @Column(name = "LB_ID_TRADE_MARK")
    @Basic
    var lbIdTradeMark: String? = null

    @Column(name = "LB_ID_DATE_OF_MANF")
    @Basic
    var lbIdDateOfManf: Date? = null

    @Column(name = "LB_ID_EXPIRY_DATE")
    @Basic
    var lbIdExpiryDate: Date? = null

    @Column(name = "LB_ID_CONT_DECL")
    @Basic
    var lbIdContDecl: String? = null

    @Column(name = "LB_ID_BATCH_NO")
    @Basic
    var lbIdBatchNo: String? = null

    @Column(name = "LB_ID_ANY_AOMARKING")
    @Basic
    var lbIdAnyAomarking: String? = null

    @Column(name = "SIZE_TEST_SAMPLE")
    @Basic
    var sizeTestSample: String? = null

    @Column(name = "SIZE_REF_SAMPLE")
    @Basic
    var sizeRefSample: String? = null

    @Column(name = "CUSTOMER_NAME_ORG")
    @Basic
    var customerNameOrg: String? = null

    @Column(name = "CUSTOMER_NAME_ADDRESS")
    @Basic
    var customerNameAddress: String? = null

    @Column(name = "CUSTOMER_NAME_TELEPHONE")
    @Basic
    var customerNameTelephone: String? = null

    @Column(name = "CUSTOMER_NAME_EMAIL")
    @Basic
    var customerNameEmail: String? = null

    @Column(name = "CUSTOMER_NAME_DATE")
    @Basic
    var customerNameDate: Date? = null

    @Column(name = "FILE_REFERENCE_NO")
    @Basic
    var fileReferenceNo: String? = null

    @Column(name = "REF_NUMBER")
    @Basic
    var refNumber: String? = null

    @Column(name = "SCF_NO")
    @Basic
    var scfNo: String? = null

    @Column(name = "SAMPLE_REFERENCE_NO")
    @Basic
    var sampleReferenceNo: String? = null

    @Column(name = "CONDITION_SAMPLE")
    @Basic
    var conditionSample: String? = null

    @Column(name = "TEST_REQUEST")
    @Basic
    var testRequest: String? = null

    @Column(name = "TEST_CHARGES")
    @Basic
    var testCharges: String? = null

    @Column(name = "RECEIPT_NO")
    @Basic
    var receiptNo: String? = null

    @Column(name = "INVOICE_NO")
    @Basic
    var invoiceNo: String? = null

    @Column(name = "RECEIVERS_NAME")
    @Basic
    var receiversName: String? = null

    @Column(name = "COUNTRY_OF_ORIGIN")
    @Basic
    var countryOfOrigin: String? = null

    @Column(name = "PACKAGING_OF_PRODUCTS")
    @Basic
    var packagingOfProducts: String? = null

    @Column(name = "STA_30_SL_NO")
    @Basic
    var sta30SlNo: String? = null

    @Column(name = "STATUS_OF_CONSIGNMENT")
    @Basic
    var statusOfConsignment: String? = null

    @Column(name = "OFFICER_DESIGNATION")
    @Basic
    var officerDesignation: String? = null

    @Column(name = "SUBMITTING_OFFICER")
    @Basic
    var submittingOfficer: String? = null

    @Column(name = "OFFICER_STATION")
    @Basic
    var officerStation: String? = null

    @Column(name = "CONDITION_OF_SAMPLE_TIME_RECEPTION")
    @Basic
    var conditionOfSampleTimeReception: String? = null

    @Column(name = "DETAILS_OF_CONSIGNMENT")
    @Basic
    var detailsOfConsignment: String? = null

    @Column(name = "CUSTOMS_IE_NO")
    @Basic
    var customsIeNo: String? = null

    @Column(name = "PRO_NO")
    @Basic
    var proNo: String? = null

    @Column(name = "SUBMITTING_DATE")
    @Basic
    var submittingDate: Date? = null

    @Column(name = "RECEIVERS_DATE")
    @Basic
    var receiversDate: Date? = null

    @Column(name = "TESTLAB_NAME")
    @Basic
    var testLabName: String? = null

    @Column(name = "NOTE_TRANS_RESULTS")
    @Basic
    var noteTransResults: String? = null

    @Column(name = "DISPOSAL")
    @Basic
    var disposal: String? = null

    @Column(name = "DESTROY_RETURN")
    @Basic
    var destroyReturn: String? = null

    @Column(name = "COC_NUMBER")
    @Basic
    var cocNumber: String? = null

    @Column(name = "REMARKS", length = 3800)
    @Basic
    var remarks: String? = null

    @Column(name = "LABORATORY")
    @Basic
    var laboratory: String? = null

    @Column(name = "UCR_NUMBER")
    @Basic
    var ucrNumber: String? = null

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
        val that = other as CdSampleSubmissionItemsEntity
        return id == that.id &&
                workplanId == that.workplanId &&
                sampleSubmissionFileFilePath == that.sampleSubmissionFileFilePath &&
                permitId == that.permitId &&
                objectId == that.objectId &&
                labeling == that.labeling &&
                sampleRefNumber == that.sampleRefNumber &&
//                selectedLab == that.selectedLab &&
                fileReferenceNumber == that.fileReferenceNumber &&
                referenceStandardSpecification == that.referenceStandardSpecification &&
                samplesCompleteness == that.samplesCompleteness &&
                samplesCompletenessRemarks == that.samplesCompletenessRemarks &&


                officerStation == that.officerStation &&
                officerDesignation == that.officerDesignation &&
                submittingOfficer == that.submittingOfficer &&
                submittingDate == that.submittingDate &&
                proNo == that.proNo &&
                customsIeNo == that.customsIeNo &&
                detailsOfConsignment == that.detailsOfConsignment &&
                conditionOfSampleTimeReception == that.conditionOfSampleTimeReception &&
                statusOfConsignment == that.statusOfConsignment &&
                sta30SlNo == that.sta30SlNo &&
                packagingOfProducts == that.packagingOfProducts &&
                countryOfOrigin == that.countryOfOrigin &&
                receiversDate == that.receiversDate &&
                bsNumber == that.bsNumber &&
                refNumber == that.refNumber &&
                referenceStandards == that.referenceStandards &&
                testLabName == that.testLabName &&
                itemHscode == that.itemHscode &&
                itemId == that.itemId &&
                nameProduct == that.nameProduct &&
                packaging == that.packaging &&
                lbIdTradeMark == that.lbIdTradeMark &&
                lbIdDateOfManf == that.lbIdDateOfManf &&
                lbIdExpiryDate == that.lbIdExpiryDate &&
                lbIdContDecl == that.lbIdContDecl &&
                lbIdBatchNo == that.lbIdBatchNo &&
                lbIdAnyAomarking == that.lbIdAnyAomarking &&
                sizeTestSample == that.sizeTestSample &&
                sizeRefSample == that.sizeRefSample &&
                customerNameOrg == that.customerNameOrg &&
                customerNameAddress == that.customerNameAddress &&
                customerNameTelephone == that.customerNameTelephone &&
                customerNameEmail == that.customerNameEmail &&
                customerNameDate == that.customerNameDate &&
                fileReferenceNo == that.fileReferenceNo &&
                scfNo == that.scfNo &&
                sampleReferenceNo == that.sampleReferenceNo &&
                conditionSample == that.conditionSample &&
                testRequest == that.testRequest &&
                testCharges == that.testCharges &&
                receiptNo == that.receiptNo &&
                invoiceNo == that.invoiceNo &&
                receiversName == that.receiversName &&
                noteTransResults == that.noteTransResults &&
                disposal == that.disposal &&
                destroyReturn == that.destroyReturn &&
                cocNumber == that.cocNumber &&
                remarks == that.remarks &&
                laboratory == that.laboratory &&
                ucrNumber == that.ucrNumber &&
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
        return Objects.hash(
                id,
//                selectedLab,
                samplesCompletenessRemarks,
                labeling,
                sampleRefNumber,
                fileReferenceNo,
                referenceStandardSpecification,
                permitId,
                workplanId,
                sampleSubmissionFileFilePath,
                samplesCompleteness,
                objectId,
                receiversDate,
                testLabName,
                itemHscode,
                bsNumber,
                refNumber,
                itemId,
                officerStation,
                officerDesignation,
                submittingOfficer,
                submittingDate,
                proNo,
                customsIeNo,
                detailsOfConsignment,
                conditionOfSampleTimeReception,
                statusOfConsignment,
                sta30SlNo,
                packagingOfProducts,
                countryOfOrigin,
                nameProduct,
                packaging,
                lbIdTradeMark,
                lbIdDateOfManf,
                lbIdExpiryDate,
                lbIdContDecl,
                lbIdBatchNo,
                lbIdAnyAomarking,
                sizeTestSample,
                sizeRefSample,
                customerNameOrg,
                customerNameAddress,
                customerNameTelephone,
                customerNameEmail,
                customerNameDate,
                fileReferenceNo,
                referenceStandards,
                scfNo,
                sampleReferenceNo,
                conditionSample,
                testRequest,
                testCharges,
                receiptNo,
                invoiceNo,
                receiversName,
                noteTransResults,
                disposal,
                destroyReturn,
                cocNumber,
                remarks,
                laboratory,
                ucrNumber,
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