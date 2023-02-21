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
    @Column(name = "SENDERS_DATE")
    var sendersDate: Date? = null

    @Basic
    @Column(name = "DATE_VISIT")
    var dateVisit: Date? = null

    @Basic
    @Column(name = "SAMPLE_REFERENCES")
    var sampleReferences: String? = null

    @Basic
    @Column(name = "SAMPLE_COLLECTION_DATE")
    var sampleCollectionDate: String? = null

    @Basic
    @Column(name = "RESULTS_DATE")
    var resultsDate: Date? = null

    @Basic
    @Column(name = "RESULT_SENT_DATE")
    var resultSentDate: Date? = null

    @Basic
    @Column(name = "OFFICER_ID")
    var officerId: Long? = null

    @Basic
    @Column(name = "COMPLAINT_DEPARTMENT")
    var complaintDepartment: Long? = null

    @Basic
    @Column(name = "DATEOF_VISIT")
    var dateofVisit: String? = null

    @Basic
    @Column(name = "SAMPLE_SUBMISSION_DATE")
    var sampleSubmissionDate: String? = null

    @Basic
    @Column(name = "MARKET_CENTRE")
    var outletName: String? = null

    @Basic
    @Column(name = "NAME_ADDRESS_OUTLET")
    var nameAddressOutlet: String? = null

    @Basic
    @Column(name = "PRODUCT_DESCRIPTION")
    var productDescription: String? = null

    @Basic
    @Column(name = "SECTOR")
    var sector: String? = null

    @Basic
    @Column(name = "UCR_PERMIT_NO")
    var ucrPermitNo: String? = null

    @Basic
    @Column(name = "SOURCE_PRODUCT_EVIDENCE")
    var sourceProductEvidence: String? = null

    @Basic
    @Column(name = "BRAND_AND_MANUFACTURER")
    var brandAndManufacturer: String? = null

    @Basic
    @Column(name = "NO_SAMPLES_TESTED")
    var noSamplesTested: String? = null

    @Basic
    @Column(name = "FAILED_PARAMETERS")
    var failedParameters: String? = null

    @Basic
    @Column(name = "ACTIONS_TAKEN")
    var actionsTaken: String? = null

    @Basic
    @Column(name = "DATEOF_TEST_REPORT")
    var dateofTestReport: String? = null

    @Basic
    @Column(name = "DATEOF_FORWARDING_TEST_RESULTS")
    var dateofForwardingTestResults: String? = null

    @Basic
    @Column(name = "COMPLIANCE_TESTING")
    var complianceTesting: String? = null

    @Basic
    @Column(name = "TCXB")
    var tcxb: String? = null

    @Basic
    @Column(name = "TIME_TAKEN_SUBMIT_SAMPLE")
    var timeTakenSubmitSample: String? = null

    @Basic
    @Column(name = "SUBMISSION_WITHIN2_DAYS")
    var submissionWithin2Days: String? = null

    @Basic
    @Column(name = "TIME_TAKEN_FORWARD_LETTERS")
    var timeTakenForwardLetters: String? = null

    @Basic
    @Column(name = "FORWARDING_WITHIN14_DAYS_TESTING")
    var forwardingWithin14DaysTesting: String? = null

    @Basic
    @Column(name = "BATCH_NO_DATE_MANUFACTURE")
    var batchNoDateManufacture: String? = null



}
