package org.kebs.app.kotlin.apollo.api.ports.provided.mpesa.requests

import java.sql.Timestamp

class MpesaTransactionsRequest {
    var username: String? = null
    var subscriberId: String? = null
    var amountToPay: String? = null
    var transactionDate: Timestamp? = null
    var transactionReference: String? = null
}