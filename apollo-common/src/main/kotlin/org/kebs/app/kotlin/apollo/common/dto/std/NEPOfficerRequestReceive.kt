package org.kebs.app.kotlin.apollo.common.dto.std

import java.time.LocalDate

class NEPOfficerRequestReceive {
    val information_available: String = ""
    val response_email: String = ""
}

data class NepRequestsDto(
    var requesterName: String?=null,
    var requesterComment: String?=null,
    var requesterCountry: String?=null,
    var requesterEmail: String?=null,
    var requesterInstitution: String?=null,
    var requesterPhone: String?=null,
    var requesterSubject: String?=null,
    var requestDate: LocalDate = LocalDate.now()
){

}
data class NepInfoCheckDto(
    var comments: String?=null,
    var accentTo: String?=null,
    var requestId: Long,
    var enquiryId: Long,
    var feedbackSent: String?=null,
    var requesterEmail: String?=null,

    var requesterName: String?=null,
    var requesterPhone: String?=null,
    var requesterInstitution: String?=null,
    var requesterCountry: String?=null,
    var requesterSubject: String?=null,
    var requesterComment: String?=null,
){}
