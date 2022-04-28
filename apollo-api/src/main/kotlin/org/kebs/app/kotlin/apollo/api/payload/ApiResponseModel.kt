package org.kebs.app.kotlin.apollo.api.payload

import mu.KotlinLogging
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.web.servlet.function.ServerRequest

class ApiResponseModel {
    var totalCount: Long? = null
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
            if (it in 0..100) {
                page = it
            }
        }
    }
    // Get page size
    req.param("size").ifPresent { p ->
        p.toIntOrNull()?.let {
            size = if (it in 0..100) {
                it
            } else {
                20
            }
        }
    }
    var direction = "desc"
    req.param("direction").ifPresent {
        if ("asc".equals(it, true)) {
            direction = it
        }
    }
    KotlinLogging.logger { }.debug("Page: $page, Size: $size, Direction: $direction")
    if ("asc".equals(direction, true)) {
        return PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, field))
    }
    return PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, field))
}