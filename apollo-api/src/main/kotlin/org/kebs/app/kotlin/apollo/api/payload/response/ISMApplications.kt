package org.kebs.app.kotlin.apollo.api.payload.response

import org.kebs.app.kotlin.apollo.store.model.ism.IsmApplications
import java.sql.Timestamp

class ISMApplicationsDto {
    var applicationId: Long = 0
    var ucrNumber: String? = null
    var companyName: String? = null
    var firstName: String? = null
    var middleName: String? = null
    var lastName: String? = null
    var emailAddress: String? = null
    var remarks: String? = null
    var approvalRemarks: String? = null
    var approvedRejectedOn: Timestamp? = null
    var requestApproved: String? = null
    var approvedRejectedBy: String? = null
    var completed: String? = null
    var completedOn: Timestamp? = null
    var status: Long? = null

    companion object {
        fun fromEntity(application: IsmApplications): ISMApplicationsDto {
            val app = ISMApplicationsDto()
            app.apply {
                applicationId = application.id
                ucrNumber = application.ucrNumber
                companyName = application.companyName
                firstName = application.firstName
                middleName = application.middleName
                lastName = application.lastName
                emailAddress = application.emailAddress
                remarks = application.remarks
                approvalRemarks = application.approvalRemarks
                approvedRejectedOn = application.approvedRejectedOn
                approvedRejectedBy = application.approvedBy
                completedOn = application.completedOn
                status = application.status
            }
            app.completed = when (application.completed) {
                true -> "YES"
                else -> "NO"
            }
            app.requestApproved = when (application.requestApproved) {
                0 -> "INITIATED"
                1 -> "APPROVED"
                2 -> "REJECTED"
                else -> "UNKNOWN"
            }
            return app
        }
        fun fromList(applications: List<IsmApplications>): List<ISMApplicationsDto> {
            val dtos = mutableListOf<ISMApplicationsDto>()
            applications.forEach { d ->
                dtos.add(fromEntity(d))
            }
            return dtos
        }
    }
}


class ISMExternalApplicationsDto {
    var applicationId: Long = 0
    var ucrNumber: String? = null
    var companyName: String? = null
    var firstName: String? = null
    var middleName: String? = null
    var lastName: String? = null
    var emailAddress: String? = null
    var remarks: String? = null
    var approvalRemarks: String? = null
    var approvedRejectedOn: Timestamp? = null
    var requestApproved: String? = null
    var completed: String? = null
    var completedOn: Timestamp? = null

    companion object {
        fun fromEntity(application: IsmApplications): ISMExternalApplicationsDto {
            val app = ISMExternalApplicationsDto()
            app.apply {
                applicationId = application.id
                ucrNumber = application.ucrNumber
                companyName = application.companyName
                firstName = application.firstName
                middleName = application.middleName
                lastName = application.lastName
                emailAddress = application.emailAddress
                remarks = application.remarks
                approvalRemarks = application.approvalRemarks
                approvedRejectedOn = application.approvedRejectedOn
                completedOn = application.completedOn
            }
            app.completed = when (application.completed) {
                true -> "YES"
                else -> "NO"
            }
            app.requestApproved = when (application.requestApproved) {
                0 -> "INITIATED"
                1 -> "APPROVED"
                2 -> "REJECTED"
                else -> "UNKNOWN"
            }
            return app
        }

        fun fromList(applications: List<IsmApplications>): List<ISMExternalApplicationsDto> {
            val dtos = mutableListOf<ISMExternalApplicationsDto>()
            applications.forEach { d ->
                dtos.add(fromEntity(d))
            }
            return dtos
        }
    }
}
