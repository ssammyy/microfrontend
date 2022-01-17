package org.kebs.app.kotlin.apollo.api.payload

import com.fasterxml.jackson.annotation.JsonProperty

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

class OauthClientLoginForm {
    @JsonProperty("client_id")
    var clientId: String? = null
    @JsonProperty("client_secret")
    var clientSecret: String? = null
    @JsonProperty("grant_type")
    var grantType: String? = null
}

class OauthClientLoginResult {
    @JsonProperty("token_type")
    var tokenType: String? = null
    @JsonProperty("access_token")
    var accessToken: String? = null
    @JsonProperty("expires_in")
    var expiresIn: Long? = null
}