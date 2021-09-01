package org.kebs.app.kotlin.apollo.api.handlers

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.adaptor.kafka.producer.service.SendToKafkaQueue
import org.kebs.app.kotlin.apollo.common.dto.HashListDto
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.repo.*
import org.kebs.app.kotlin.apollo.store.repo.ms.*
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.ServerResponse.ok
import org.springframework.web.servlet.function.body

@Component
class ImagesHandlers(
    private val applicationMapProperties: ApplicationMapProperties,

    ) {
    fun smarkBackGroundImage(req: ServerRequest): ServerResponse {
        return try {
            ok().body(applicationMapProperties.mapSmarkBackgroundImagePath)
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }
    }

    fun dmarkBackGroundImage(req: ServerRequest): ServerResponse {
        return try {
            ok().body(applicationMapProperties.mapDmarkBackgroundImagePath)
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }
    }

    fun fmarkBackGroundImage(req: ServerRequest): ServerResponse {
        return try {
            ok().body(applicationMapProperties.mapFmarkBackgroundImagePath)
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }
    }

}
