package org.kebs.app.kotlin.apollo.api.ports.provided.criteria

data class SearchCriteria(
    var key: String? = null,
    var operation: String? = null,
    var value: Any? = null
)