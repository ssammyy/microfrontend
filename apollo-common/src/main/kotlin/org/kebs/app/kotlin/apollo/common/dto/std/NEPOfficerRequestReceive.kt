package org.kebs.app.kotlin.apollo.common.dto.std

import java.sql.Timestamp
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
    var notification: String?=null,
    var typeOfNotification: String?=null
){

}

data class NepNotificationDto(
    var id: Long,
    var datePrepared: Timestamp,
    var notifyingMember: String?=null,
    var agencyResponsible:String?=null,
    var addressOfAgency:String?=null,
    var telephoneOfAgency: String?=null,
    var faxOfAgency: String?=null,
    var emailOfAgency: String?=null,
    var websiteOfAgency: String?=null,
    var notifiedUnderArticle: String?=null,
    var productsCovered: String?=null,
    var descriptionOfNotifiedDoc: String?=null,
    var objectiveAndRationale: String?=null,
    var relevantDocuments: String?=null,
    var proposedDateOfAdoption: Timestamp?=null,
    var proposedDateOfEntryIntoForce: Timestamp,
    var finalDateForComments: Timestamp,
    var textAvailableFrom: String?=null,
    var preparedBy: String?=null,
    var status: Long
){

}

data class NepDraftDecisionDto(
    var draftId: Long,
    var remarks: String?=null,
    var accentTo: String?=null,
    var notification: String?=null

){

}

data class NepDraftWtoDto(
    var notification: String?=null,
    var draftId: Long?=null,
    var status: Long?=null,
){

}