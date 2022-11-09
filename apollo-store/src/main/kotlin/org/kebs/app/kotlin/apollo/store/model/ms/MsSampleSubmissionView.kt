package org.kebs.app.kotlin.apollo.store.model.ms

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "MS_SAMPLE_SUBMISSION", schema = "APOLLO", catalog = "")
class MsSampleSubmissionView : Serializable {
    @Id
    @Column(name = "ID")
    var id: String? = null

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

    @Basic
    @Column(name = "REFERENCES_STANDARDS")
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
}
