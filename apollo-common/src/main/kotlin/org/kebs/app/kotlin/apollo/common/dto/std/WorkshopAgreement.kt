package org.kebs.app.kotlin.apollo.common.dto.std


import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp
import java.util.ArrayList

class WorkshopAgreement(
    @JsonProperty("taskId") val taskId: String,
    @JsonProperty("accentTo") val accentTo: Boolean,
    @JsonProperty("processId") val processId: String,
    @JsonProperty("approvalID")  val approvalID: Long,
    @JsonProperty("comments") val comments: String,
    @JsonProperty("jstNumber")  val jstNumber: Long,
    @JsonProperty("assignedTo") var assignedTo: Long
    ) {
}
data class WorkshopPreliminaryDraft(
    val requestId: Long? = null,
    val id: Long? = null,
    val title: String? = null,
    val scope: String? = null,
    val normativeReference: String? = null,
    val symbolsAbbreviatedTerms: String? = null,
    val clause: String? = null,
    val special: String? = null,
    val diJNumber: Long? = null,
    val workShopDate: Timestamp,
    val nwaStandardNo: String? = null,
){}
data class JustificationNwa(
    var meetingDate: String? = null,
    var knwSecretary: String? = null,
    var knw: String? = null,
    var sl: String? = null,
    var requestedBy: String? = null,
    var issuesAddressed: String? = null,
    var knwAcceptanceDate: String? = null,
    var referenceMaterial: String? = null,
    var department: String? = null,
    var status: String? = null,
    var remarks: String? = null,
    var submissionDate: String? = null,
    var requestNumber: String? = null,
    var knwCommittee: String? = null,
    var departmentName: String? = null,
    var justificationFilesList: List<FilesListDto>? = null
){

}
data class FilesListDto(
    var id: Long? = null,
    var name: String? = null,
    var fileType: String? = null,
    var documentType: String? = null,
    var nwaDocumentId: Long? = null,
    var document: ByteArray? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FilesListDto

        if (id != other.id) return false
        if (name != other.name) return false
        if (fileType != other.fileType) return false
        if (documentType != other.documentType) return false
        if (nwaDocumentId != other.nwaDocumentId) return false
        if (document != null) {
            if (other.document == null) return false
            if (!document.contentEquals(other.document)) return false
        } else if (other.document != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (fileType?.hashCode() ?: 0)
        result = 31 * result + (documentType?.hashCode() ?: 0)
        result = 31 * result + (nwaDocumentId?.hashCode() ?: 0)
        result = 31 * result + (document?.contentHashCode() ?: 0)
        return result
    }
}
class NWAJustificationDecision(
    @JsonProperty("taskId") val taskId: String,
    @JsonProperty("processId") val processId: String,
    @JsonProperty("accentTo") val accentTo: Boolean,
    @JsonProperty("decision") val decision: String,
    @JsonProperty("approvalID")  val approvalID: Long,
    @JsonProperty("comments") val comments: String,
    @JsonProperty("assignedTo") var assignedTo: Long
    ) {
}

class KNWJustificationDecision(
    @JsonProperty("taskId") val taskId: String,
    @JsonProperty("justificationID") val justificationID: Long,
    @JsonProperty("accentTo") val accentTo: Boolean,
    @JsonProperty("approvalID")  val approvalID: Long,
    @JsonProperty("comments") val comments: String,
    @JsonProperty("assignedTo") var assignedTo: Long
) {
}

class NWAPreliminaryDraftDecision(
    @JsonProperty("taskId") val taskId: String,
    @JsonProperty("accentTo") val accentTo: Boolean,
    @JsonProperty("approvalID")  val approvalID: Long,
    @JsonProperty("comments") val comments: String,
    @JsonProperty("diJNumber")  val diJNumber: Long,
    @JsonProperty("assignedTo")  var assignedTo: Long,
    @JsonProperty("processId")  val processId: String
    ) {
}
class NWAWorkshopDraftDecision(
    @JsonProperty("taskId") val taskId: String,
    @JsonProperty("accentTo") val accentTo: Boolean,
    @JsonProperty("approvalID")  val approvalID: Long,
    @JsonProperty("comments") val comments: String,
    @JsonProperty("assignedTo")  var assignedTo: Long,
    @JsonProperty("processId")  val processId: String
    ) {
}
class NWADiJustification(
    @JsonProperty("taskId") var taskId: String,
    @JsonProperty("identifiedNeed") var identifiedNeed: String,
    @JsonProperty("cost")  var cost: String,
    @JsonProperty("numberOfMeetings") var numberOfMeetings: String,
    @JsonProperty("dateOfApproval")  var dateOfApproval: String,
    @JsonProperty("kID")  var kID: Long,
    @JsonProperty("datePrepared")  var datePrepared: Timestamp,
){}
class EditCompanyStandard(
    @JsonProperty("taskId") var taskId: String,
    @JsonProperty("title") var title: String,
    @JsonProperty("scope")  var cost: String,
    @JsonProperty("normativeReference") var normativeReference: String,
    @JsonProperty("symbolsAbbreviatedTerms")  var symbolsAbbreviatedTerms: String,
    @JsonProperty("clause") var clause: String,
    @JsonProperty("special")  var special: String,
    @JsonProperty("comStdNumber")  var comStdNumber: String,
    @JsonProperty("savedRowID")  var id: Long,
){}
class ISDecision(
    @JsonProperty("processId") val processId: String,
    @JsonProperty("taskId") val taskId: String,
    @JsonProperty("accentTo") val accentTo: Boolean,
    @JsonProperty("approvalID")  val approvalID: Long,
    @JsonProperty("preparedBy")  val preparedBy: Long,
    @JsonProperty("reviewID")  val reviewID: Long,
    @JsonProperty("taskType")  val taskType: Long,
    @JsonProperty("assignedTo")  var assignedTo: Long,
    @JsonProperty("comments") val comments: String,
    @JsonProperty("proposalId") val proposalId: Long,
    @JsonProperty("standardID") val standardID: Long,
    @JsonProperty("description") val description: String,
    @JsonProperty("drafterId") val drafterId: Long,
) {
}
class ISTDecision(
    @JsonProperty("processId") val processId: String,
    @JsonProperty("taskId") val taskId: String,
    @JsonProperty("accentTo") val accentTo: Boolean,
    @JsonProperty("approvalID")  val approvalID: Long,
    @JsonProperty("preparedBy")  val preparedBy: Long,
    @JsonProperty("reviewID")  val reviewID: Long,
    @JsonProperty("taskType")  val taskType: Long,
    @JsonProperty("assignedTo")  var assignedTo: Long,
    @JsonProperty("comments") val comments: String,
    @JsonProperty("proposalId") val proposalId: Long,
    @JsonProperty("standardID") val standardID: Long,
    @JsonProperty("drafterId") val drafterId: Long,
) {
}
class ISTDecisions(
    @JsonProperty("accentTo") val accentTo: String,
    @JsonProperty("proposalId") val proposalId: Long,
    @JsonProperty("comments") val comments: String
){

}

class ISJustificationDecisions(
    @JsonProperty("accentTo") val accentTo: String,
    @JsonProperty("proposalId") val proposalId: Long,
    @JsonProperty("justificationId") val justificationId: Long,
    @JsonProperty("comments") val comments: String
){

}
class ISDraftDecisions(
    @JsonProperty("accentTo") val accentTo: String,
    @JsonProperty("proposalId") val proposalId: Long,
    @JsonProperty("justificationId") val justificationId: Long,
    @JsonProperty("draftId") val draftId: Long,
    @JsonProperty("comments") val comments: String,
    @JsonProperty("requestId") val requestId: Long,
    @JsonProperty("standardType") val standardType: String,
    @JsonProperty("draftStatus") val draftStatus: String,
    @JsonProperty("coverPageStatus") val coverPageStatus: String,
    @JsonProperty("id") val id: Long,
    @JsonProperty("assignedTo") val assignedTo: Long
){

}
class ISDrDecisions(
    @JsonProperty("proposalId") val proposalId: Long,
    @JsonProperty("draftId") val draftId: Long,
    @JsonProperty("comments") val comments: String,
    @JsonProperty("requestId") val requestId: Long,
    @JsonProperty("id") val id: Long
){

}
class StandardGazetteDto(
    @JsonProperty("description") val description: String,
    @JsonProperty("id") val id: Long
){


}
class ISDraftDecisionsStd(
    @JsonProperty("accentTo") val accentTo: String,
    @JsonProperty("proposalId") val proposalId: Long,
    @JsonProperty("justificationId") val justificationId: Long,
    @JsonProperty("draftId") val draftId: Long,
    @JsonProperty("comments") val comments: String?,
    @JsonProperty("title") val title: String?,
    @JsonProperty("normativeReference") val normativeReference: String?,
    @JsonProperty("symbolsAbbreviatedTerms") val symbolsAbbreviatedTerms: String?,
    @JsonProperty("clause") val clause: String?,
    @JsonProperty("scope") val scope: String?,
    @JsonProperty("special") val special: String?,
    @JsonProperty("standardNumber") val standardNumber: String?,
    @JsonProperty("standardType") val standardType: String?,
    @JsonProperty("requestId") val requestId: Long?,
    @JsonProperty("id") val id: Long

){

}

class IStandardUploadDto(
    @JsonProperty("proposalId") val proposalId: Long,
    @JsonProperty("draftId") val draftId: Long,
    @JsonProperty("comments") val comments: String?,
    @JsonProperty("title") val title: String?,
    @JsonProperty("normativeReference") val normativeReference: String?,
    @JsonProperty("symbolsAbbreviatedTerms") val symbolsAbbreviatedTerms: String?,
    @JsonProperty("clause") val clause: String?,
    @JsonProperty("scope") val scope: String?,
    @JsonProperty("special") val special: String?,
    @JsonProperty("standardNumber") val standardNumber: String?,
    @JsonProperty("id") val id: Long

){

}

class ISJustificationDecision(
    @JsonProperty("processId") val processId: String,
    @JsonProperty("proposalId") val proposalId: Long,
    @JsonProperty("assignedTo")  var assignedTo: Long,
    @JsonProperty("taskId") val taskId: String,
    @JsonProperty("accentTo") val accentTo: Boolean,
    @JsonProperty("approvalID")  val approvalID: Long,
    @JsonProperty("comments") val comments: String
) {
}
class ISDraftStdUpload(
    @JsonProperty("draftId") val draftId: Long
)
{

}
class ReviewDecision(
    @JsonProperty("comments") var comments: String,
    @JsonProperty("taskId") var taskId: String,
    @JsonProperty("processId")  var processId: String,
    @JsonProperty("assignedTo")  var assignedTo: Long,
    @JsonProperty("reviewID")  var reviewID: Long,
    @JsonProperty("taskType")  var taskType: Long,
    @JsonProperty("standardID")  val standardID: Long,
    @JsonProperty("accentTo")  var accentTo: Boolean
) {
}

class GazzettementDecision(
    @JsonProperty("comments") var comments: String,
    @JsonProperty("taskId") var taskId: String,
    @JsonProperty("processId")  var processId: String,
    @JsonProperty("assignedTo")  var assignedTo: Long,
    @JsonProperty("reviewID")  var reviewID: Long,
    @JsonProperty("taskType")  var taskType: Long,
    @JsonProperty("standardID")  val standardID: Long,
    @JsonProperty("accentTo")  var accentTo: Boolean,
    @JsonProperty("description")  var description: String
) {
}

class NwaJustificationDto(
    @JsonProperty("knw") var knw: String,
    @JsonProperty("dateOfMeeting") var dateOfMeeting: Timestamp,
    @JsonProperty("sl") var sl: String,
    @JsonProperty("requestedBy") var requestedBy: String,
    @JsonProperty("issuesAddressed") var issuesAddressed: String,
    @JsonProperty("acceptanceDate") var acceptanceDate: Timestamp,
    @JsonProperty("referenceMaterial") var referenceMaterial: String,
    @JsonProperty("department") var department: String,
    @JsonProperty("remarks") var remarks: String,
    @JsonProperty("requestId") var requestId: Long,
){

}
data class NwaDecisionOnJustificationDto(
    var comments: String?=null,
    var accentTo: String?=null,
    var requestId: Long?=null
){}

class NwaJustificationAction(
    @JsonProperty("justificationID") val justificationID: Long,
    @JsonProperty("accentTo") val accentTo: String,
    @JsonProperty("comments") val comments: String
) {
}

class PreliminaryDraftDTO(
    @JsonProperty("title") val title: String,
    @JsonProperty("scope") val scope: String,
    @JsonProperty("normativeReference") val normativeReference: String,
    @JsonProperty("symbolsAbbreviatedTerms") val symbolsAbbreviatedTerms: String,
    @JsonProperty("clause") val clause: String,
    @JsonProperty("special") val special: String,
    @JsonProperty("justificationNumber") val justificationNumber: Long,
    @JsonProperty("workShopDate") val workShopDate: Timestamp,
){}

class NwaPDraftAction(
    @JsonProperty("preliminaryDraftID") val preliminaryDraftID: Long,
    @JsonProperty("justificationID") val justificationID: Long,
    @JsonProperty("draftID") val draftID: Long,
    @JsonProperty("accentTo") val accentTo: String,
    @JsonProperty("comments") val comments: String,
    @JsonProperty("title") val title: String,
    @JsonProperty("scope") val scope: String,
    @JsonProperty("normativeReference") val normativeReference: String,
    @JsonProperty("symbolsAbbreviatedTerms") val symbolsAbbreviatedTerms: String,
    @JsonProperty("clause") val clause: String,
    @JsonProperty("special") val special: String,
) {
}
data class ISAdoptionProposalDto(
    var proposal_doc_name: String?=null,
    var circulationDate: Timestamp,
    var closingDate: String?=null,
    var tcSecName: String?=null,
    var title: String?=null,
    var requestId: Long?=null,
    var scope: String?=null,
    var iStandardNumber: String?=null,
    var adoptionAcceptableAsPresented: String?=null,
    var reasonsForNotAcceptance: String?=null,
    var recommendations: String?=null,
    var nameOfRespondent: String?=null,
    var positionOfRespondent: String?=null,
    var nameOfOrganization: String?=null,
    var dateOfApplication: String?=null,
    var adoptionProposalLink: String?=null,
    var uploadedBy: String?=null,
    var stakeholdersList: MutableList<NamesList>?=null,
    //var addStakeholdersList: List<String>?=null,
    var addStakeholdersList: MutableList<IStakeholderDTO>?=null,
    var departmentId: Long?=null,
    var departmentName: String?=null

){

}

data class IStakeholderDTO(

    var stakeHolderEmail: String?=null,
    var stakeHolderName: String?=null,
    var stakeHolderPhone: String?=null
){

}

data class ISPropComments(
    var commentTitle: String?=null,
    var scope: String?=null,
    var clause: String?=null,
    var proposalID: Long?=null,
    var standardNumber: String?=null,
    var commentDocumentType: String?=null,
    var recommendations: String?=null,
    var nameOfRespondent: String?=null,
    var positionOfRespondent: String?=null,
    var nameOfOrganization: String?=null,
    var preparedDate: Timestamp?=null,
    var paragraph: String?=null,
    var typeOfComment: String?=null,
    var comment: String?=null,
    var proposedChange: String?=null,
    var observation: String?=null,
){

}


data class ISAdoptionProposalComments(
    var user_id:String? =null,
    var adoption_proposal_comment:String? =null,
    var comment_time: Timestamp?=null,
    var proposalID:Long? =null,
    var commentTitle:String? =null,
    var commentDocumentType:String? =null,
    var comNameOfOrganization:String? =null,
    var comClause:String? =null,
    var comParagraph:String? =null,
    var typeOfComment:String? =null,
    var proposedChange:String? =null,
    var adopt :String? =null,
    var reasonsForNotAcceptance :String? =null,
    var recommendations :String? =null,
    var nameOfRespondent :String? =null,
    var positionOfRespondent :String? =null,
    var nameOfOrganization :String? =null,
    var dateOfApplication:Timestamp? =null
){

}

data class ISProposalJustification(
    var meetingDate: String?=null,
    var slNumber: String?=null,
    var edition: String?=null,
    var standardNumber: String?=null,
    var department: String?=null,
    var requestedBy: String?=null,
    var scope: String?=null,
    var purposeAndApplication: String?=null,
    var intendedUsers: String?=null,
    var circulationDate: Timestamp?=null,
    var closingDate: Timestamp?=null,
    var tcAcceptanceDate: String?=null,
    var proposalId: Long?=null,
    var draftId: Long?=null,
    var justificationId: Long?=null,
    var issuesAddressed: String?=null,
    var title: String?=null,
    var referenceMaterial: String?=null,
    var tcSecName: String?=null


){

}

data class ISAdoptionJustifications(

    var tc_id: String?=null,
    var scope: String?=null,
    var purposeAndApplication: String?=null,
    var intendedUsers: String?=null,
    var circulationDate: Timestamp?=null,
    var closingDate: Timestamp?=null,

    var meetingDate:String?=null,
    var tcSec_id:String?=null,
    var slNumber:String?=null,
    var edition:String?=null,
    var requestedBy:String?=null,
    var issuesAddressed:String?=null,
    var tcAcceptanceDate:String?=null,
    var referenceMaterial:String?=null,
    var department:String?=null,
    var status:String?=null,
    var positiveVotes:Long?=null,
    var negativeVotes:Long?=null,
    var remarks:String?=null,
    var proposalId:Long?=null,
    var draftId:Long?=null
){

}

class ISDraftStandard(
    @JsonProperty("accentTo") val accentTo: String,
    @JsonProperty("proposalId") val proposalId: Long,
    @JsonProperty("justificationId") val justificationId: Long,
    @JsonProperty("draftId") val draftId: Long,
    @JsonProperty("comments") val comments: String
){

}

data class ISDraftDto(
    var proposalId:Long?=null,
    var justificationNo:Long?=null,
    var id:Long,
    var assignedTo:Long,
    var title:String?=null,
    var scope:String?=null,
    var normativeReference:String?=null,
    var symbolsAbbreviatedTerms:String?=null,
    var clause:String?=null,
    var standardNumber:String?=null,
    var preparedBy:String?=null,
    var docName:String?=null,
    var special:String?=null,
    var draughting:String?=null,
    var requestNumber:String?=null,
    var comStdNumber:String?=null,
    var documentType:String?=null,
){

}
data class ComDraftDto(
    var id:Long,
    var title:String?=null,
    var docName:String?=null,
    var draughting:String?=null,
    var requestId:Long?=null,
    var draftId:Long?=null,

){

}

data class CSDraftDto(
    var proposalId:Long?=null,
    var justificationNo:Long?=null,
    var id:Long,
    var title:String?=null,
    var scope:String?=null,
    var normativeReference:String?=null,
    var symbolsAbbreviatedTerms:String?=null,
    var clause:String?=null,
    var standardNumber:String?=null,
    var preparedBy:String?=null,
    var docName:String?=null,
    var special:String?=null,
    var draughting:String?=null,
    var requestNumber:String?=null,
    var comStdNumber:String?=null,
    var documentType:String?=null,
    var requestId:Long?=null,
    var draftId:Long?=null,
    var departmentId: Long?=null,
    var subject: String?=null,
    var description: String?=null,
    var contactOneFullName: String?=null,
    var contactOneTelephone: String?=null,
    var contactOneEmail: String?=null,
    var contactTwoFullName: String?=null,
    var contactTwoTelephone: String?=null,
    var contactTwoEmail: String?=null,
    var contactThreeFullName: String?=null,
    var contactThreeTelephone: String?=null,
    var contactThreeEmail: String?=null,
    var companyName: String?=null,
    var companyPhone: String?=null,
    var standardType: String?=null,
){

}


data class ISComStdRequestDto(
    var companyName: String?=null,
    var companyPhone: String?=null,
    var companyEmail: String?=null,
    var departmentId: String?=null,
    var subject: String?=null,
    var description: String?=null,
    var contactDetails: MutableList<ISComStdContactDto>?=null
){

}

data class ISComStdContactDto(
    var contactOneFullName: String?=null,
    var contactOneTelephone: String?=null,
    var contactOneEmail: String?=null
){

}

data class ISCompanyStdRequestDto(
    var companyName: String?=null,
    var tcId: String?=null,
    var productId: String?=null,
    var productSubCategoryId: String?=null,
    var companyPhone: String?=null,
    var assignedTo: Long?=null,
    var companyEmail: String?=null,
    var requestId: Long,
    var departmentId: String?=null,
    var subject: String?=null,
    var description: String?=null,
    var contactOneFullName: String?=null,
    var contactOneTelephone: String?=null,
    var contactOneEmail: String?=null,
    var contactTwoFullName: String?=null,
    var contactTwoTelephone: String?=null,
    var contactTwoEmail: String?=null,
    var contactThreeFullName: String?=null,
    var contactThreeTelephone: String?=null,
    var contactThreeEmail: String?=null
){

}

data class JointCommitteeDto(
    var requestId: Long?=null,
    var name: List<String>?=null,
    var names: MutableList<NamesList>?=null
){

}

class NamesList {
    var email: String? = null
    var name: String? = null
}


data class ComTcJustificationDto(
    var proposer: String?=null,
    var purpose: String?=null,
    var subject: String?=null,
    var scope: String?=null,
    var targetDate: String?=null,
    var proposedRepresentation: String?=null,
    var programmeOfWork: String?=null,
    var organization: String?=null,
    var liaisonOrganization: String?=null,
    var dateOfPresentation: String?=null,
    var nameOfTC: String?=null,
    var referenceNumber: String?=null,
    var comRequestId: Long?=null
){

}

class ComJustificationDecisions(
    @JsonProperty("accentTo") val accentTo: String,
    @JsonProperty("id") val id: Long,
    @JsonProperty("requestId") val requestId: Long,
    @JsonProperty("comments") val comments: String
){

}

data class StandardReviewDto(
    val id: Long?=null,
    val title: String?=null,
    val standardNumber: String?=null,
    val standardType: String?=null,
    val documentType: String?=null,
    val dateFormed: Timestamp?=null,
    val circulationDate: Timestamp?=null,
    val edition: String?=null
)
{}

data class StandardReviewCommentDto(
    val id: Long?=null,
    val title: String?=null,
    val standardNumber: String?=null,
    val standardType: String?=null,
    val documentType: String?=null,
    val dateFormed: Timestamp?=null,
    val circulationDate: Timestamp?=null,
    val closingDate: Timestamp?=null,
    val nameOfTcSecretary: String?=null,
    val edition: String?=null,
    val choice: String?=null,
    val justification: String?=null,
    val nameOfRespondent: String?=null,
    val positionOfRespondent: String?=null,
    val nameOfOrganization: String?=null,
    val standardId: Long?=null
)
{}

data class StandardReviewRecommendationDto(
    val reviewId: Long?=null,
    val recommendation: String?=null,
    val feedback: Long?=null
){

}

data class SpcStandardReviewCommentDto(
    val id: Long?=null,
    val title: String?=null,
    val standardNumber: String?=null,
    val standardType: String?=null,
    val documentType: String?=null,
    val dateFormed: Timestamp?=null,
    val circulationDate: Timestamp?=null,
    val closingDate: Timestamp?=null,
    val nameOfTcSecretary: String?=null,
    val edition: String?=null,
    val choice: String?=null,
    val justification: String?=null,
    val nameOfRespondent: String?=null,
    val positionOfRespondent: String?=null,
    val nameOfOrganization: String?=null,
    val accentTo: String?=null,
    val requestNumber: String?=null,
    val remarks: String?=null,
    val subject: String?=null,
    val description: String?=null,
    val feedback: Long?=null,

)
{}

