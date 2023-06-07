package org.kebs.app.kotlin.apollo.store.model.qa

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_QA_SAMPLE_LAB_TEST_RESULTS")
class QaSampleLabTestResultsEntity : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_QA_SAMPLE_LAB_TEST_RESULTS_SEQ_GEN", allocationSize = 1, sequenceName = "DAT_KEBS_QA_SAMPLE_LAB_TEST_RESULTS_SEQ")
    @GeneratedValue(generator = "DAT_KEBS_QA_SAMPLE_LAB_TEST_RESULTS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = 0

    @Column(name = "ORDER_ID")
    @Basic
    var orderId: String? = null

    @Column(name = "SAMPLE_NUMBER")
    @Basic
    var sampleNumber: String? = null

    @Column(name = "TEST")
    @Basic
    var test: String? = null

    @Column(name = "PARAM")
    @Basic
    var param: String? = null

    @Column(name = "SORT_ORDER")
    @Basic
    var sortOrder: String? = null

    @Column(name = "METHOD")
    @Basic
    var method: String? = null

    @Column(name = "RESULT")
    @Basic
    var result: String? = null

    @Column(name = "NUMERIC_RESULT")
    @Basic
    var numericResult: String? = null

    @Column(name = "UNITS")
    @Basic
    var units: String? = null

    @Column(name = "DILUTION")
    @Basic
    var dilution: String? = null

    @Column(name = "ANALYSIS_VOLUME")
    @Basic
    var analysisVolume: String? = null

    @Column(name = "SAMPLE_TYPE")
    @Basic
    var sampleType: String? = null

    @Column(name = "QUALIFIER")
    @Basic
    var qualifier: String? = null

    @Column(name = "REP_LIMIT")
    @Basic
    var repLimit: String? = null

    @Column(name = "RETENTION_TIME")
    @Basic
    var retentionTime: String? = null

    @Column(name = "TIC")
    @Basic
    var tic: String? = null

    @Column(name = "RESULT_STATUS")
    @Basic
    var resultStatus: String? = null

    @Column(name = "ENTERED_DATE")
    @Basic
    var enteredDate: String? = null

    @Column(name = "ENTERED_BY")
    @Basic
    var enteredBy: String? = null

    @Column(name = "VALIDATED_DATE")
    @Basic
    var validatedDate: String? = null

    @Column(name = "VALIDATED_BY")
    @Basic
    var validatedBy: String? = null

    @Column(name = "APPROVED_DATE")
    @Basic
    var approvedDate: String? = null

    @Column(name = "APPROVED_BY")
    @Basic
    var approvedBy: String? = null

    @Column(name = "APPROVED_REASON")
    @Basic
    var approvedReason: String? = null

    @Column(name = "COMMNT")
    @Basic
    var commnt: String? = null

    @Column(name = "MEASURED_RESULT")
    @Basic
    var measuredResult: String? = null

    @Column(name = "PERCENT_MOISTURE")
    @Basic
    var percentMoisture: String? = null

    @Column(name = "METHOD_VOLUME")
    @Basic
    var methodVolume: String? = null

    @Column(name = "EXTRACT_VOLUME")
    @Basic
    var extractVolume: String? = null

    @Column(name = "METHOD_EXTRACT_VOLUME")
    @Basic
    var methodExtractVolume: String? = null

    @Column(name = "INSTRUMENT")
    @Basic
    var instrument: String? = null

    @Column(name = "RESULTS_USER1")
    @Basic
    var resultsUser1: String? = null

    @Column(name = "RESULTS_USER2")
    @Basic
    var resultsUser2: String? = null

    @Column(name = "RESULTS_USER3")
    @Basic
    var resultsUser3: String? = null

    @Column(name = "RESULTS_USER4")
    @Basic
    var resultsUser4: String? = null

    @Column(name = "REPORT")
    @Basic
    var report: String? = null

    @Column(name = "PARAM_ANALYST")
    @Basic
    var paramAnalyst: String? = null

    @Column(name = "PARAM_ANALYSIS_DATE_TIME")
    @Basic
    var paramAnalysisDateTime: String? = null

    @Column(name = "PRINTED")
    @Basic
    var printed: String? = null

    @Column(name = "PRINTED_AT")
    @Basic
    var printedAt: String? = null

    @Column(name = "TS")
    @Basic
    var ts: String? = null

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
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as QaSampleLabTestResultsEntity
        return id == that.id &&
                orderId == that.orderId &&
                sampleNumber == that.sampleNumber &&
                test == that.test &&
                param == that.param &&
                sortOrder == that.sortOrder &&
                method == that.method &&
                result == that.result &&
                numericResult == that.numericResult &&
                units == that.units &&
                dilution == that.dilution &&
                analysisVolume == that.analysisVolume &&
                sampleType == that.sampleType &&
                qualifier == that.qualifier &&
                repLimit == that.repLimit &&
                retentionTime == that.retentionTime &&
                tic == that.tic &&
                resultStatus == that.resultStatus &&
                enteredDate == that.enteredDate &&
                enteredBy == that.enteredBy &&
                validatedDate == that.validatedDate &&
                validatedBy == that.validatedBy &&
                approvedDate == that.approvedDate &&
                approvedBy == that.approvedBy &&
                approvedReason == that.approvedReason &&
                commnt == that.commnt &&
                measuredResult == that.measuredResult &&
                percentMoisture == that.percentMoisture &&
                methodVolume == that.methodVolume &&
                extractVolume == that.extractVolume &&
                methodExtractVolume == that.methodExtractVolume &&
                instrument == that.instrument &&
                resultsUser1 == that.resultsUser1 &&
                resultsUser2 == that.resultsUser2 &&
                resultsUser3 == that.resultsUser3 &&
                resultsUser4 == that.resultsUser4 &&
                report == that.report &&
                paramAnalyst == that.paramAnalyst &&
                paramAnalysisDateTime == that.paramAnalysisDateTime &&
                printed == that.printed &&
                printedAt == that.printedAt &&
                ts == that.ts &&
                description == that.description &&
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
            orderId,
            sampleNumber,
            test,
            param,
            sortOrder,
            method,
            result,
            numericResult,
            units,
            dilution,
            analysisVolume,
            sampleType,
            qualifier,
            repLimit,
            retentionTime,
            tic,
            resultStatus,
            enteredDate,
            enteredBy,
            validatedDate,
            validatedBy,
            approvedDate,
            approvedBy,
            approvedReason,
            commnt,
            measuredResult,
            percentMoisture,
            methodVolume,
            extractVolume,
            methodExtractVolume,
            instrument,
            resultsUser1,
            resultsUser2,
            resultsUser3,
            resultsUser4,
            report,
            paramAnalyst,
            paramAnalysisDateTime,
            printed,
            printedAt,
            ts,
            description,
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