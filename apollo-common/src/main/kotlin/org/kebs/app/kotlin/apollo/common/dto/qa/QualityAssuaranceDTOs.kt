package org.kebs.app.kotlin.apollo.common.dto.qa

import java.sql.Date

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




