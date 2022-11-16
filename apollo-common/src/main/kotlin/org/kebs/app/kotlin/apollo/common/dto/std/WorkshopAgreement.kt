package org.kebs.app.kotlin.apollo.common.dto.std


import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp

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


