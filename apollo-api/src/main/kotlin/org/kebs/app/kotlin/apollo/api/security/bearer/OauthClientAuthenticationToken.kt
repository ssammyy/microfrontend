package org.kebs.app.kotlin.apollo.api.security.bearer

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

class OauthClientAuthenticationToken(clientId: String, clientSecret: String, grantTypeZ: String) : UsernamePasswordAuthenticationToken(clientId, clientSecret) {
    private val grantType=grantTypeZ

    fun getGrantType() : String= this.grantType
}