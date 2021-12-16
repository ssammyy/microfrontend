package org.kebs.app.kotlin.apollo.api.payload.request

import java.sql.Timestamp
import javax.persistence.Column

class CorporateForm {
    var corporateIdentifier: String? = null
    var corporateName: String? = null
    var corporateType: String? = null // COURIER
    var corporateEmail: String? = null
    var corporateBillNumber: String? = null
    var contactName: String? = null
    var contactPhone: String? = null
    var contactEmail: String? = null
    var isCiakMember: Boolean? = false
    var mouDays: Int? = 0
}

class CorporateStatusUpdateForm {
    var actionCode: String? = null
    var remarks: String? = null
}