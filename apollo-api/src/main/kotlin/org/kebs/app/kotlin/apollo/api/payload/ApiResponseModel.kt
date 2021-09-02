package org.kebs.app.kotlin.apollo.api.payload

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.web.servlet.function.ServerRequest

class ApiResponseModel {
    var totalCount: Long?=null
    var extras: Any? = null
    lateinit var message: String
    lateinit var responseCode: String
    var data: Any? = null
    var errors: Any? = null
    var pageNo: Int? = null
    var totalPages: Int? = null
}

fun extractPage(req: ServerRequest, field: String = "id"): PageRequest {
    var page = 0
    var size = 20
    // get page
    req.param("page").ifPresent { p ->
        p.toIntOrNull()?.let {
            page = it
        }
    }
    // Get page size
    req.param("size").ifPresent { p ->
        p.toIntOrNull()?.let {
            if (it in 1..100) {
                size = it
            }
        }
    }
    var direction = "desc"
    req.param("direction").ifPresent {
        if ("asc".equals(it)) {
            direction = it
        }
    }
    if ("asc".equals(direction)) {
        return PageRequest.of(page, size, Sort.by(Sort.Order.asc(field)))
    }
    return PageRequest.of(page, size, Sort.by(Sort.Order.desc(field)))
}