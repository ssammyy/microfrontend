package org.kebs.app.kotlin.apollo.common.dto.qa

import java.math.BigDecimal
import java.sql.Date
import java.sql.Timestamp

data class ST10Dto(
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

data class CommonPermitDto(
        var firmName: String? = null,
        var directorList: String? = null,
        var postalAddress: String? = null,
        var physicalAddress: String? = null,
        var contactPerson: String? = null,
        var telephoneNo: String? = null,
        var email: String? = null,
        var fax: String? = null,
        var county: String? = null,
        var town: String? = null,
        var region: String? = null,
        var companyID: Long? = null,
        var plantID: Long? = null,
        var countyID: Long? = null,
        var townID: Long? = null,
        var regionID: Long? = null,
)

data class WorkPlanDto(
        var firmName: String? = null,
        var refNumber: String? = null,
        var permitNumber: String? = null,
        var physicalAddress: String? = null,
        var town: String? = null,
        var productBrand: String? = null,
        var issueDate: Date? = null,
        var expiryDate: Date? = null,
        var visitsScheduled: Date? = null,
)

data class InvoiceDto(
        var batchID: Long? = null,
        var firmName: String? = null,
        var postalAddress: String? = null,
        var physicalAddress: String? = null,
        var contactPerson: String? = null,
        var telephoneNo: String? = null,
        var email: String? = null,
        var invoiceNumber: String? = null,
        var receiptNo: String? = null,
        var paidDate: Timestamp? = null,
        var totalAmount: BigDecimal? = null,
        var paidStatus: Boolean? = null,
        var plantId: Long? = null,
)

data class PermitInvoiceDto(
        var permitID: Long? = null,
        var invoiceNumber: String? = null,
        var commodityDescription: String? = null,
        var brandName: String? = null,
        var totalAmount: BigDecimal? = null,
        var paidStatus: Boolean? = null,
)

data class NewBatchInvoiceDto(
        var batchID: Long = -1L,
        var plantID: Long? = null,
        var permitID: Long? = null,
)

data class PermitDetailsDto(
        var Id: Long? = null,
        var permitNumber: String? = null,
        var permitRefNumber: String? = null,
        var firmName: String? = null,
        var postalAddress: String? = null,
        var physicalAddress: String? = null,
        var contactPerson: String? = null,
        var telephoneNo: String? = null,
        var regionPlantValue: String? = null,
        var countyPlantValue: String? = null,
        var townPlantValue: String? = null,
        var location: String? = null,
        var street: String? = null,
        var buildingName: String? = null,
        var nearestLandMark: String? = null,
        var faxNo: String? = null,
        var plotNo: String? = null,
        var email: String? = null,
        var createdOn: Timestamp? = null,
        var dateOfIssue: Date? = null,
        var dateOfExpiry: Date? = null,
        var commodityDescription: String? = null,
        var brandName: String? = null,
        var standardNumber: String? = null,
        var standardTitle: String? = null,
        var permitForeignStatus: Boolean? = null,
        var assignOfficer: String? = null,
        var assignAssessor: String? = null,
        var divisionValue: String? = null,
        var sectionValue: String? = null,
        var inspectionDate: Date? = null,
        var inspectionScheduledStatus: Boolean? = null,
        var assessmentDate: Date? = null,
        var assessmentScheduledStatus: Boolean? = null,
        var processStatusName: String? = null,
        var versionNumber: Long? = null,
        var fmarkGenerated: Boolean? = null,
        var recommendationRemarks: String? = null,
)





