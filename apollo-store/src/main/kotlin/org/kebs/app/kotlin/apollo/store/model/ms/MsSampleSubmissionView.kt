package org.kebs.app.kotlin.apollo.store.model.ms

import java.io.Serializable
import java.sql.Date
import javax.persistence.*

@Entity
@Table(name = "MS_SAMPLE_SUBMISSION", schema = "APOLLO", catalog = "")
class MsSampleSubmissionView : Serializable {
    @Id
    @Column(name = "ID")
    var id: String? = null

    @Column(name = "CREATED_USER_ID")
    @Basic
    var createdUserId: String? = null

    @Basic
    @Column(name = "NAME_PRODUCT")
    var nameProduct: String? = null

    @Basic
    @Column(name = "PACKAGING")
    var packaging: String? = null

    @Basic
    @Column(name = "LABELLING_IDENTIFICATION")
    var labellingIdentification: String? = null

    @Basic
    @Column(name = "FILE_REF_NUMBER")
    var fileRefNumber: String? = null

    @Column(name = "REFERENCES_STANDARDS")
    @Basic
    var referencesStandards: String? = null

    @Basic
    @Column(name = "SIZE_TEST_SAMPLE")
    var sizeTestSample: String? = null

    @Basic
    @Column(name = "SIZE_REF_SAMPLE")
    var sizeRefSample: String? = null

    @Basic
    @Column(name = "CONDITION")
    var condition: String? = null

    @Basic
    @Column(name = "SAMPLE_REFERENCES")
    var sampleReferences: String? = null

    @Basic
    @Column(name = "SENDERS_NAME")
    var sendersName: String? = null

    @Basic
    @Column(name = "DESIGNATION")
    var designation: String? = null

    @Basic
    @Column(name = "ADDRESS")
    var address: String? = null

    @Basic
    @Column(name = "SENDERS_DATE")
    var sendersDate: String? = null

    @Basic
    @Column(name = "RECEIVERS_NAME")
    var receiversName: String? = null

    @Basic
    @Column(name = "TEST_CHARGES_KSH")
    var testChargesKsh: String? = null

    @Basic
    @Column(name = "RECEIPT_LPO_NUMBER")
    var receiptLpoNumber: String? = null

    @Basic
    @Column(name = "INVOICE_NUMBER")
    var invoiceNumber: String? = null

    @Basic
    @Column(name = "DISPOSAL")
    var disposal: String? = null

    @Basic
    @Column(name = "REMARKS")
    var remarks: String? = null

    @Basic
    @Column(name = "SAMPLE_COLLECTION_NUMBER")
    var sampleCollectionNumber: String? = null

    @Basic
    @Column(name = "BS_NUMBER")
    var bsNumber: String? = null

    @Basic
    @Column(name = "PARAMETERS")
    var parameters: String? = null

    @Basic
    @Column(name = "LABORATORY_NAME")
    var laboratoryName: String? = null

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
    var lbIdDateOfManf: String? = null

    @Column(name = "LB_ID_EXPIRY_DATE")
    @Basic
    var lbIdExpiryDate: String? = null

    @Column(name = "RECEIVERS_DATE")
    @Basic
    var receiversDate: String? = null

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
}
