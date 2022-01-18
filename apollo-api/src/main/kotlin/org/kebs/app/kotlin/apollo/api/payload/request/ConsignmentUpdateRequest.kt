package org.kebs.app.kotlin.apollo.api.payload.request;

class ConsignmentUpdateRequest {
    var approvalStatus: Int? = null
    var remarks: String? = null
    var compliant: String? = null
    var officerId: Long? = null
    var status: Int? = null
    var documentType: String? = null
    var confirmRequest: Boolean? = null
    var portOfArrival: Long? = null
    var cdStatusTypeId: Long? = null
    var compliantStatus: Int? = null
    var blacklistStatus: Int? = null
    var freightStation: Long? = null
    var blacklistId: Int? = null
    var targetStatus: Int? = null
    var targetApproveStatus: Int? = null
    var inspectionStatus: Int? = null
    var inspectionNotificationStatus: Int? = null
    var sendCoiStatus: Int? = null
    var reassign: Boolean = false
}
