package org.kebs.app.kotlin.apollo.api.ports.provided.emailDTO

import java.sql.Date


class CustomerComplaintSubmittedDTO {

    var baseUrl: String? = null

    var fullName: String? = null

    var refNumber: String? = null

    var dateSubmitted: Date? = null
}

class CustomerComplaintRejectedDTO {

    var baseUrl: String? = null

    var uuid: String? = null

    var fullName: String? = null

    var complaintTitle: String? = null

    var commentRemarks: String? = null

    var refNumber: String? = null

    var dateSubmitted: Date? = null
}

class CustomerComplaintRejectedWIthOGADTO {

    var baseUrl: String? = null

    var uuid: String? = null

    var fullName: String? = null

    var complaintTitle: String? = null

    var commentRemarks: String? = null

    var adviceRemarks: String? = null

    var refNumber: String? = null

    var dateSubmitted: Date? = null
}

class ComplaintAssignedDTO {

    var baseUrl: String? = null

    var uuid: String? = null

    var fullName: String? = null

    var assignIORemarks: String? = null

    var assignHOFRemarks: String? = null

    var commentRemarks: String? = null

    var refNumber: String? = null

    var dateSubmitted: Date? = null
}

