package org.kebs.app.kotlin.apollo.api.ports.provided.emailDTO

import java.sql.Date
import java.sql.Timestamp

class BSNumberLabDTO{

    var baseUrl: String? = null

//    var id: Long? = null

    var bsNumber: String? = null

    var date: Timestamp? = null

    var uuid: String? = null

    var fullName: String? = null

    var sampleRefNumber: String? = null

    var docTypeDetail: String? = null
}

class SampleResultsLabDTO{

    var baseUrl: String? = null

    var id: Long? = null

    var parameterName: String? = null

    var labName: String? = null

    var date: Timestamp? = null

    var uuid: String? = null

    var fullName: String? = null

    var sampleRefNumber: String? = null

    var docTypeDetail: String? = null
}

class InspectionReportApprovalDTO{

    var baseUrl: String? = null

    var uuid: String? = null

    var fullName: String? = null

    var refNumber: String? = null
}

class InspectionReportApprovedDTO{

    var baseUrl: String? = null

    var uuid: String? = null

    var fullName: String? = null

    var commentRemarks: String? = null

    var refNumber: String? = null

    var date: Date? = null
}

class InspectionReportDisApprovedDTO{

    var baseUrl: String? = null

    var uuid: String? = null

    var fullName: String? = null

    var commentRemarks: String? = null

    var refNumber: String? = null

    var date: Date? = null
}

