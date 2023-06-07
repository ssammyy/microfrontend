package org.kebs.app.kotlin.apollo.api.ports.provided.mpesa.requests

import java.math.BigDecimal

class MpesaPushRequest {
    var subscriberId: String? = null
    var amount: BigDecimal? = null
    var account: String? = null
    var transactionReference: String? = null
}

