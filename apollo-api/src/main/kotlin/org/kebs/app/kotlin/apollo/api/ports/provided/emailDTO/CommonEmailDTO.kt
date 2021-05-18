package org.kebs.app.kotlin.apollo.api.ports.provided.emailDTO

import java.sql.Date
import java.sql.Timestamp


class RegistrationEmailDTO {

    var baseUrl: String? = null

    var fullName: String? = null

    var dateSubmitted: Date? = null

    var modifiedON: Timestamp? = null

    var otpGenerated: String? = null

    var otpGeneratedDate: Date? = null
}

class RegistrationForEntryNumberEmailDTO {

    var baseUrl: String? = null

    var fullName: String? = null

    var entryNumber: String? = null

    var dateSubmitted: Date? = null
}

