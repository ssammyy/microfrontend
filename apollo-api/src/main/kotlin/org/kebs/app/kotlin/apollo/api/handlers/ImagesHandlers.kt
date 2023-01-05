package org.kebs.app.kotlin.apollo.api.handlers

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.adaptor.kafka.producer.service.SendToKafkaQueue
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.QADaoServices
import org.kebs.app.kotlin.apollo.common.dto.HashListDto
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.repo.*
import org.kebs.app.kotlin.apollo.store.repo.ms.*
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.ServerResponse.ok
import org.springframework.web.servlet.function.body
import org.springframework.web.servlet.function.paramOrNull

@Component
class ImagesHandlers(
    private val applicationMapProperties: ApplicationMapProperties,
    val qaDaoServices: QADaoServices

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

    fun permitQRCodeScanned(req: ServerRequest): ServerResponse {
        return try {
            val permitNumber = req.paramOrNull("permitNumber") ?: throw ExpectedDataNotFound("Required Permit Number, check config")
            val permit = qaDaoServices.findPermitBYPermitNumber(permitNumber)
            qaDaoServices.mapDtoQRCodeQaDetailsView(permit).let {
                ok().body(it)
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }
    }

}
