package org.kebs.app.kotlin.apollo.common.dto.ms

import java.sql.Date

data class ComplaintApproveRejectAssignDto(
        var division: Long? = null,
        var approved: Int? = 0,
        var approvedRemarks: String? = null,
        var rejected: Int? = 0,
        var rejectedRemarks: String? = null,
        var mandateForOga: Int? = 0,
        var advisedWhereToRemarks: String? = null,
        var assignedIoStatus: Int? = null,
        var assignedIoRemarks: String? = null,
        var assignedIo: Long? = null
)

data class ComplaintApproveDto(
        var division: Long? = null,
        var approved: Int? = null,
        var approvedRemarks: String? = null
)

data class ComplaintRejectDto(
        var rejected: Int? = null,
        var rejectedRemarks: String? = null
)

//data class FindComplaintDetailsDto(
//        var refNumber: String? = null,
//)
data class ComplaintSearchValues(
    var refNumber: String? = null,
    var date: Date? = null,
//        var approvedStatus: Int? = null,
//        var assignedIOStatus: Int? = null,
//        var rejectedStatus: Int? = null,
//        var lastName: String? = null
)

data class ComplaintAdviceRejectDto(
        var mandateForOga: Int? = null,
        var advisedWhereToRemarks: String? = null,
        var rejectedRemarks: String? = null
)

data class ComplaintAssignDto(
        var assignedIoStatus: Int? = null,
        var assignedRemarks: String? = null,
        var assignedIo: Long? = null
)

data class ComplaintsDetailsDto(
        var id: Long? = null,
        var refNumber: String? = null,
        var complainantName: String? = null,
        var complainantEmail: String? = null,
        var complainantPhoneNumber: String? = null,
        var complainantPostalAddress: String? = null,
        var complaintCategory: String? = null,
        var complaintTitle: String? = null,
        var complaintDescription: String? = null,
        var broadProductCategory: String? = null,
        var productClassification: String? = null,
        var productSubcategory: String? = null,
        var productName: String? = null,
        var productBrand: String? = null,
        var county: String? = null,
        var town: String? = null,
        var marketCenter: String? = null,
        var buildingName: String? = null,
        var date: Date? = null,
        var status: String? = null,
        var officersList: List<MsUsersDto>? = null,
        var divisionList: List<MsDivisionDto>? = null,
        var approvedStatus: Int? = null,
        var assignedIOStatus: Int? = null,
        var rejectedStatusStatus: Int? = null

)

data class ComplaintsDto(
        var refNumber: String? = null,
        var complainantName: String? = null,
        var complaintCategory: String? = null,
        var complaintTitle: String? = null,
        var date: Date? = null,
        var status: String? = null
)

data class MsDepartmentDto(
        val id: Long? = null,
        val department: String? = null,
        val descriptions: String? = null,
        val directorateId: Long? = null,
        val status: Boolean? = null
)

data class MsUsersDto(
        var id: Long? = null,
        var firstName: String? = null,
        var lastName: String? = null,
        var userName: String? = null,
        var email: String? = null,
        val status: Boolean? = null
)


data class MsDivisionDto(
        val id: Long? = null,
        val division: String? = null,
        val descriptions: String? = null,
        val status: Int? = null,
        val departmentId: Long? = null
)

data class NewComplaintDto(
        val complaintDetails: ComplaintDto,
        val customerDetails: ComplaintCustomersDto,
        val locationDetails: ComplaintLocationDto
//        val complaintFilesDetails: ComplaintFilesDto,
)

data class ComplaintFilesDto(
        val fileDetails: List<FileDTO>? = null
)

data class FileDTO(
        var fileType: String? = null,
        var name: String? = null,
        var document: ByteArray? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FileDTO

        if (fileType != other.fileType) return false
        if (name != other.name) return false
        if (document != null) {
            if (other.document == null) return false
            if (!document.contentEquals(other.document)) return false
        } else if (other.document != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = fileType?.hashCode() ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (document?.contentHashCode() ?: 0)
        return result
    }
}

data class ComplaintDto(
        var complaintCategory: Long? = null,
        var complaintTitle: String? = null,
        var productClassification: Long? = null,
        var broadProductCategory: Long? = null,
        var productCategory: Long? = null,
        var myProduct: Long? = null,
        var productSubcategory: Long? = null,
        var productBrand: String? = null,
        var complaintDescription: String? = null
)

data class ComplaintCustomersDto(
        var firstName: String? = null,
        var lastName: String? = null,
        var phoneNumber: String? = null,
        var emailAddress: String? = null,
        var postalAddress: String? = null
)

data class ComplaintLocationDto(
        var county: Long? = null,
        var town: Long? = null,
        var marketCenter: String? = null,
        var buildingName: String? = null
)


data class MSTypeDto(
        var uuid: String? = null,
        var typeName: String? = null,
        var markRef: String? = null,
        var description: String? = null,
        var status: Int? = null
)

data class MSComplaintSubmittedSuccessful(
        var message: String? = null
)

