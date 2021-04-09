package org.kebs.app.kotlin.apollo.api.handlers

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.common.dto.HashListDto
import org.kebs.app.kotlin.apollo.common.dto.UnHashListDto
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body

@Component
class UtilitiesHandler(
        private val commonDaoServices: CommonDaoServices
) {
    fun hashString(req: ServerRequest): ServerResponse {
        try {
            val dto = req.body<HashListDto>()
            val hashedString = dto.stringDetails?.let { commonDaoServices.hashString(it) }
            hashedString?.let { return ServerResponse.ok().body(it) }
                    ?: throw NullValueNotAllowedException("No Hashed String Found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }
    }

}





