package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Time
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_INSPECTION_LAB_TESTS_REPORT_FORM")
class LabTestsReportEntity: Serializable {
    @Column(name = "ID")
    @Id
    var id: Long = 0

    @Column(name = "PARAMETERS")
    @Basic
    var parameters: String? = null

    @Column(name = "RESULTS")
    @Basic
    var results: String? = null

    @Column(name = "REQUIREMENTS")
    @Basic
    var requirements: String? = null

    @Column(name = "TEST_METHOD_NO")
    @Basic
    var testMethodNo: String? = null

    @Column(name = "LOD")
    @Basic
    var lod: String? = null

    @Column(name = "TEST_DATE")
    @Basic
    var testDate: Time? = null

    @Column(name = "ATTACHMENT")
    @Basic
    var attachment: String? = null

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

    @Column(name = "LAST_MODIFIED_BY")
    @Basic
    var lastModifiedBy: String? = null

    @Column(name = "LAST_MODIFIED_ON")
    @Basic
    var lastModifiedOn: Timestamp? = null

    @Column(name = "UPDATE_BY")
    @Basic
    var updateBy: String? = null

    @Column(name = "UPDATED_ON")
    @Basic
    var updatedOn: Timestamp? = null

    @Column(name = "DELETE_BY")
    @Basic
    var deleteBy: String? = null

    @Column(name = "DELETED_ON")
    @Basic
    var deletedOn: Timestamp? = null

    @Column(name = "VERSION")
    @Basic
    var version: Long? = null

    @Column(name = "DESCRIPTION_OF_SAMPLE")
    @Basic
    var descriptionOfSample: String? = null

    @Column(name = "SAMPLE_SUBMITTED_BY")
    @Basic
    var sampleSubmittedBy: String? = null

    @Column(name = "CUSTOMER_CONTACT")
    @Basic
    var customerContact: String? = null

    @Column(name = "CUSTOMER_ADDRESS")
    @Basic
    var customerAddress: String? = null

    @Column(name = "ACCEPTANCE_CRITERIA")
    @Basic
    var acceptanceCriteria: String? = null

    @Column(name = "BS_NUMBER")
    @Basic
    var bsNumber: String? = null

    @Column(name = "LAB_REF_NO")
    @Basic
    var labRefNo: String? = null

    @Column(name = "DATE_OF_RECEIPT")
    @Basic
    var dateOfReceipt: Time? = null

    @Column(name = "DATE_ANALYSIS_STARTED")
    @Basic
    var dateAnalysisStarted: Time? = null

    @Column(name = "REPORT_UUID")
    @Basic
    var reportUuid: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as LabTestsReportEntity
        return id == that.id &&
                parameters == that.parameters &&
                results == that.results &&
                requirements == that.requirements &&
                testMethodNo == that.testMethodNo &&
                lod == that.lod &&
                testDate == that.testDate &&
                attachment == that.attachment &&
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
                lastModifiedBy == that.lastModifiedBy &&
                lastModifiedOn == that.lastModifiedOn &&
                updateBy == that.updateBy &&
                updatedOn == that.updatedOn &&
                deleteBy == that.deleteBy &&
                deletedOn == that.deletedOn &&
                version == that.version &&
                descriptionOfSample == that.descriptionOfSample &&
                sampleSubmittedBy == that.sampleSubmittedBy &&
                customerContact == that.customerContact &&
                customerAddress == that.customerAddress &&
                acceptanceCriteria == that.acceptanceCriteria &&
                bsNumber == that.bsNumber &&
                labRefNo == that.labRefNo &&
                dateOfReceipt == that.dateOfReceipt &&
                dateAnalysisStarted == that.dateAnalysisStarted &&
                reportUuid == that.reportUuid
    }

    override fun hashCode(): Int {
        return Objects.hash(id, parameters, results, requirements, testMethodNo, lod, testDate, attachment, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, lastModifiedBy, lastModifiedOn, updateBy, updatedOn, deleteBy, deletedOn, version, descriptionOfSample, sampleSubmittedBy, customerContact, customerAddress, acceptanceCriteria, bsNumber, labRefNo, dateOfReceipt, dateAnalysisStarted, reportUuid)
    }
}