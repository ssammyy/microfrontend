package org.kebs.app.kotlin.apollo.api.payload

import javax.persistence.Column

class ApiClientForm {
    var clientType: String? = null
    var clientName: String? = null
    var clientRole: String? = null
    var callbackURL: String? = null
    var eventsURL: String? = null // System events
    var descriptions: String? = null
}

class ApiClientUpdateForm {
    var clientId: String? = null
    var actionCode: String? = null
    var remarks: String? = null
}