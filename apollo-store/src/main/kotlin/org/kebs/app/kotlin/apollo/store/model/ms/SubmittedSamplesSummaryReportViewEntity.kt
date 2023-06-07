package org.kebs.app.kotlin.apollo.store.model.ms

import org.kebs.app.kotlin.apollo.store.model.ms.SubmittedSamplesSummaryReportViewEntity
import java.io.Serializable
import java.math.BigInteger
import java.sql.Date
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "SUBMITTED_SAMPLES_SUMMARY_REPORT_VIEW", schema = "APOLLO", catalog = "")
class SubmittedSamplesSummaryReportViewEntity : Serializable {
    @Id
    @Column(name = "ID")
    var id: Long? = null

    @Basic
    @Column(name = "NAME_PRODUCT")
    var nameProduct: String? = null

    @Basic
    @Column(name = "PRODUCT_DESCRIPTION")
    var productDescription: String? = null

    @Basic
    @Column(name = "BATCH_NUMBER")
    var batchNumber: String? = null

    @Basic
    @Column(name = "DATE_OF_MANUF")
    var dateOfManuf: String? = null

    @Basic
    @Column(name = "BS_NUMBER")
    var bsNumber: String? = null

    @Basic
    @Column(name = "OFFICER_NAME")
    var officerName: String? = null

    @Basic
    @Column(name = "SAMPLE_COLLECTION_DATE")
    var sampleCollectionDate: String? = null

    @Basic
    @Column(name = "SAMPLE_SUBMISSION_DATE")
    var sampleSubmissionDate: String? = null

    @Basic
    @Column(name = "MARKET_CENTER")
    var marketCentre: String? = null

    @Basic
    @Column(name = "OUTLET_NAME")
    var outletName: String? = null

    @Basic
    @Column(name = "OUTLET_DETAILS")
    var outletDetails: String? = null

    @Basic
    @Column(name = "DEPARTMENT")
    var department: String? = null

    @Basic
    @Column(name = "FUNCTION")
    var function: String? = null

    @Basic
    @Column(name = "SOURCE_OF_PRODUCT_AND_EVIDENCE")
    var sourceProductEvidence: String? = null

    @Basic
    @Column(name = "INSPECTION_DATE")
    var inspectionDate: String? = null

    @Basic
    @Column(name = "INSPECTION_DATE_AS_DATE")
    var inspectionDateAsDate: Date? = null

    @Basic
    @Column(name = "IMPORTER_MANUFACTURER")
    var importerManufacturer: String? = null

    @Basic
    @Column(name = "PERMIT_NUMBER")
    var permitNumber: String? = null

    @Basic
    @Column(name = "UCR_NUMBER")
    var ucrNumber: String? = null

    @Basic
    @Column(name = "ACTIONS_TAKEN")
    var actionsTaken: String? = null

    @Basic
    @Column(name = "FAILED_PARAMETERS")
    var failedParameters: String? = null

    @Basic
    @Column(name = "RESULTS_DATE")
    var resultsDate: String? = null

    @Basic
    @Column(name = "RESULT_SENT_DATE")
    var resultSentDate: String? = null

    @Basic
    @Column(name = "COMPLIANCE_TESTING")
    var complianceTesting: String? = null

    @Basic
    @Column(name = "TIME_TAKEN_TO_FORWARD_LETTERS")
    var timeTakenForwardLetters: String? = null


    @Basic
    @Column(name = "TIME_TAKEN_SUBMIT_SAMPLE")
    var timeTakenSubmitSample: String? = null

    @Basic
    @Column(name = "SUBMISSION_WITHIN2_DAYS")
    var submissionWithin2Days: String? = null


}
