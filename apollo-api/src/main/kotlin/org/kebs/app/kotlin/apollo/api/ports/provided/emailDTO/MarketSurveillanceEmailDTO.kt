package org.kebs.app.kotlin.apollo.api.ports.provided.emailDTO

import org.kebs.app.kotlin.apollo.common.dto.ms.SSFCompliantStatusDto
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


class WorkPlanScheduledDTO {

    var baseUrl: String? = null

    var fullName: String? = null

    var refNumber: String? = null

    var batchRefNumber: String? = null

    var approvalStatus: String? = null

    var yearCodeName: String? = null

    var dateSubmitted: Date? = null
}

class FuelScheduledDTO {

    var baseUrl: String? = null

    var fullName: String? = null

    var refNumber: String? = null

    var yearCodeName: String? = null

    var dateSubmitted: Date? = null
}

class FuelScheduledAssignedDTO {

    var baseUrl: String? = null

    var fullName: String? = null

    var refNumber: String? = null

    var yearCodeName: String? = null

    var commentRemarks: String? = null

    var dateSubmitted: Date? = null
}

class FuelScheduledLabResultsDTO {

    var baseUrl: String? = null

    var fullName: String? = null

    var refNumber: String? = null

    var yearCodeName: String? = null

    var commentRemarks: String? = null

    var compliantDetails: SSFCompliantStatusDto? = null

    var dateSubmitted: Date? = null
}

class FuelScheduledRemediationDateDTO {

    var baseUrl: String? = null

    var fullName: String? = null

    var refNumber: String? = null

    var yearCodeName: String? = null

    var remediationDate: Date? = null
}

class FuelScheduledRemediationEndedDTO {

    var baseUrl: String? = null

    var fullName: String? = null

    var refNumber: String? = null

    var yearCodeName: String? = null

    var remediationDate: Date? = null
}
