package org.kebs.app.kotlin.apollo.api.errors

data class Error(
    val errorCoder: Int,
    val errorMessage: String?,
    val fieldErrors: List<String>
) {

    var error: String? = null


}