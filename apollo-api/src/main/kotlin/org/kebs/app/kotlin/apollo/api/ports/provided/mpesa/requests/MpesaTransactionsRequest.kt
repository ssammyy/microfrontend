package org.kebs.app.kotlin.apollo.api.ports.provided.mpesa.requests

import java.sql.Timestamp

class MpesaTransactionsRequest {
    var username: String? = null
    var phoneNumber: String? = null
    var amount: String? = null
    var transactionDate: Timestamp? = null
}