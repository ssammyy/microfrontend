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

data class DivResponseDto(
    var requesterFeedBack: String?=null,
    var requestId: Long?=null,
    var requesterid : Long?=null,
    var requesterSubject: String?=null,
){

}


data class NepInfoCheckDto(
    var comments: String?=null,
    var accentTo: String?=null,
    var requestId: Long?=null,
    var enquiryId: Long?=null,
    var feedbackSent: String?=null,
    var requesterEmail: String?=null,
    var subject: String?=null,
    var emailAddress: String?=null,
    var docUploadStatus: Long?=null,

    var requesterName: String?=null,
    var requesterPhone: String?=null,
    var requesterInstitution: String?=null,
    var requesterCountry: String?=null,
    var requesterSubject: String?=null,
    var requesterComment: String?=null,
    var requesterid: Long?=null
){}

data class NepDraftDto(
    var id: Long,
    var title: String?=null,
    var scope: String?=null,
    var normativeReference: String?=null,
    var symbolsAbbreviatedTerms: String?=null,
    var clause: String?=null,
    var special: String?=null,
    var preparedBy: String?=null,
    var notification: String?=null
){

}

data class NepDraftDecisionDto(
    var draftId: Long,
    var remarks: String?=null,
    var accentTo: String?=null

){

}
