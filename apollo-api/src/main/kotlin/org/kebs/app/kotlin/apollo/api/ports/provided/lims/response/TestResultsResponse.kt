package org.kebs.app.kotlin.apollo.api.ports.provided.lims.response

import com.fasterxml.jackson.annotation.JsonProperty

class TestResult {
    @JsonProperty("OrderID")
    var orderID: String? = null

    @JsonProperty("SampleNumber")
    var sampleNumber: String? = null

    @JsonProperty("Test")
    var test: String? = null

    @JsonProperty("Param")
    var param: String? = null

    @JsonProperty("SortOrder")
    var sortOrder: String? = null

    @JsonProperty("Method")
    var method: String? = null

    @JsonProperty("Result")
    var result: String? = null

    @JsonProperty("NumericResult")
    var numericResult: String? = null

    @JsonProperty("Units")
    var units: String? = null

    @JsonProperty("Dilution")
    var dilution: String? = null

    @JsonProperty("AnalysisVolume")
    var analysisVolume: String? = null

    @JsonProperty("SampleType")
    var sampleType: String? = null

    @JsonProperty("Qualifier")
    var qualifier: String? = null

    @JsonProperty("RepLimit")
    var repLimit: String? = null

    @JsonProperty("RetentionTime")
    var retentionTime: String? = null

    @JsonProperty("TIC")
    var tIC: String? = null

    @JsonProperty("ResultStatus")
    var resultStatus: String? = null

    @JsonProperty("EnteredDate")
    var enteredDate: String? = null

    @JsonProperty("EnteredBy")
    var enteredBy: String? = null

    @JsonProperty("ValidatedDate")
    var validatedDate: String? = null

    @JsonProperty("ValidatedBy")
    var validatedBy: String? = null

    @JsonProperty("ApprovedDate")
    var approvedDate: String? = null

    @JsonProperty("ApprovedBy")
    var approvedBy: String? = null

    @JsonProperty("ApprovedReason")
    var approvedReason: String? = null

    @JsonProperty("Commnt")
    var commnt: String? = null

    @JsonProperty("MeasuredResult")
    var measuredResult: String? = null

    @JsonProperty("PercentMoisture")
    var percentMoisture: String? = null

    @JsonProperty("MethodVolume")
    var methodVolume: String? = null

    @JsonProperty("ExtractVolume")
    var extractVolume: String? = null

    @JsonProperty("MethodExtractVolume")
    var methodExtractVolume: String? = null

    @JsonProperty("Instrument")
    var instrument: String? = null

    @JsonProperty("Results_User1")
    var results_User1: String? = null

    @JsonProperty("Results_User2")
    var results_User2: String? = null

    @JsonProperty("Results_User3")
    var results_User3: String? = null

    @JsonProperty("Results_User4")
    var results_User4: String? = null

    @JsonProperty("Report")
    var report: String? = null
    var ts: String? = null

    @JsonProperty("ParamAnalyst")
    var paramAnalyst: String? = null

    @JsonProperty("ParamAnalysisDateTime")
    var paramAnalysisDateTime: String? = null

    @JsonProperty("Printed")
    var printed: String? = null

    @JsonProperty("PrintedAt")
    var printedAt: String? = null
}

class TestParameter {
    @JsonProperty("OrderID")
    var orderID: String? = null

    @JsonProperty("SampleNumber")
    var sampleNumber: String? = null

    @JsonProperty("Test")
    var test: String? = null

    @JsonProperty("Matrix")
    var matrix: String? = null

    @JsonProperty("Method")
    var method: String? = null

    @JsonProperty("TestGroup")
    var testGroup: String? = null

    @JsonProperty("Priority")
    var priority: String? = null

    @JsonProperty("CurrentDepartment")
    var currentDepartment: String? = null

    @JsonProperty("LastDepartment")
    var lastDepartment: String? = null

    @JsonProperty("REDepartment")
    var rEDepartment: String? = null

    @JsonProperty("TestPrice")
    var testPrice: String? = null

    @JsonProperty("CustomerTestPrice")
    var customerTestPrice: String? = null

    @JsonProperty("DueDate")
    var dueDate: String? = null

    @JsonProperty("DueDateFlag")
    var dueDateFlag: String? = null

    @JsonProperty("PrepDueDate")
    var prepDueDate: String? = null

    @JsonProperty("PrepMethod")
    var prepMethod: String? = null

    @JsonProperty("AnalysisDueDate")
    var analysisDueDate: String? = null

    @JsonProperty("AnalysisTime")
    var analysisTime: String? = null

    @JsonProperty("AnalysisEmployee")
    var analysisEmployee: String? = null

    @JsonProperty("KeepTest")
    var keepTest: String? = null

    @JsonProperty("Cancelled")
    var cancelled: String? = null

    @JsonProperty("HasResults")
    var hasResults: String? = null

    @JsonProperty("SupplyReconciled")
    var supplyReconciled: String? = null

    @JsonProperty("CustomParams")
    var customParams: String? = null

    @JsonProperty("Preservative")
    var preservative: String? = null

    @JsonProperty("BottleType")
    var bottleType: String? = null

    @JsonProperty("StorageLocation")
    var storageLocation: String? = null

    @JsonProperty("SampleDetails_User1")
    var sampleDetails_User1: String? = null

    @JsonProperty("SampleDetails_User2")
    var sampleDetails_User2: String? = null

    @JsonProperty("SampleDetails_User3")
    var sampleDetails_User3: String? = null

    @JsonProperty("SampleDetails_User4")
    var sampleDetails_User4: String? = null

    var ts: String? = null
}

class RootTestResultsAndParameters {
    var status: String? = null
    var message: String? = null
    var test_result: List<TestResult>? = null
    var test_parameter: List<TestParameter>? = null
}

class RootLabPdfList {
    var status: String? = null
    var message: String? = null
    var pdf_files: List<String>? = null
    var bs_number: String? = null
}
