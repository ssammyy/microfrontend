package org.kebs.app.kotlin.apollo.api.ports.provided.mpesa.requests

class MpesaPushRequest {
    var subscriberId: String? = null
    var amount: String? = null
    var account: String? = null
    var transactionReference: String? = null
}

