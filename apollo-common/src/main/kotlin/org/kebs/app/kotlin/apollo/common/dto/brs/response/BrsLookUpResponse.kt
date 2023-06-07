package org.kebs.app.kotlin.apollo.common.dto.brs.response

class BrsLookUpResponse {
    var records: MutableList<BrsLookUpRecords?>? = mutableListOf()
    var count: Int? = 0
}