package org.kebs.app.kotlin.apollo.api.flux.handlers

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.flux.ports.dto.pvoc.*
import org.kebs.app.kotlin.apollo.api.flux.ports.provided.dao.pvoc.PvocServiceFlux
import org.kebs.app.kotlin.apollo.api.flux.ports.provided.validation.AbstractValidationHandler
import org.kebs.app.kotlin.apollo.common.exceptions.InvalidValueException
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.store.model.CocItemsEntity
import org.kebs.app.kotlin.apollo.store.model.CocsEntity
import org.kebs.app.kotlin.apollo.store.model.CoiItemsEntity
import org.kebs.app.kotlin.apollo.store.model.RiskProfileDataEntity
import org.kebs.app.kotlin.apollo.store.model.pvc.PvocQueriesDataEntity
import org.kebs.app.kotlin.apollo.store.model.pvc.PvocSealIssuesEntity
import org.kebs.app.kotlin.apollo.store.model.pvc.PvocStdMonitoringDataEntity
import org.springframework.stereotype.Component
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.Errors
import org.springframework.validation.Validator
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBodyOrNull
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.Instant


@JsonIgnoreProperties(ignoreUnknown = true)
class CustomCOCDTO{

    var cocNumber: String? = null

    var idfNumber: String? = null

    var rfiNumber: String? = null

    var ucrNumber: String? = null

    @JsonProperty("")
    var cocItems: List<CustomCOCDTO>? = null
}

class CustomCOCItemDTO {

    var shipmentLineNumber: String? = null
}

@Component
class PvocHandler(
    private val service: PvocServiceFlux,
    private val validator: Validator

) : AbstractValidationHandler() {

    suspend fun receiveCoc(req: ServerRequest): ServerResponse {
        return try {
            req.awaitBodyOrNull<CocsEntity>()
                ?.let { body ->
                    val errors: Errors = BeanPropertyBindingResult(body, CocsEntity::class.java.name)
                    validator.validate(body, errors)
                    if (errors.allErrors.isEmpty()) {
                        val response = service.saveCocData(body)
                        ServerResponse.ok().bodyValueAndAwait(response)

                    } else {
                        onValidationErrors(errors)
                    }
                }
                ?: throw InvalidValueException("No Body found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.debug(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            onErrors(e.message)

        }
    }

    suspend fun receiveCocWithItems(req: ServerRequest): ServerResponse {
        return try {
            req.awaitBodyOrNull<CocWithItems>()
                ?.let { body ->
                    val errors: Errors = BeanPropertyBindingResult(body, CocWithItems::class.java.name)
                    validator.validate(body, errors)
                    if (errors.allErrors.isEmpty()) {
                        body.coc?.coiNumber = "NA"
                        body.coc?.coiIssueDate = Timestamp.from(Instant.now())
                        body.coc?.coiRemarks = "NA"
                        body.items?.let {
                            for (item in it) {
//                                item.coiNumber = "NA"
                                item.ownerName = "NA"
                                item.ownerPin = "NA"
                            }
                        }
                        val response = service.saveCocDataWithItems(body)
                        ServerResponse.ok().bodyValueAndAwait(response)
                    } else {
                        onValidationErrors(errors)
                    }
                }
                ?: throw InvalidValueException("No Body found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.debug(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            onErrors(e.message)

        }
    }

    suspend fun receiveCOCItems(req: ServerRequest): ServerResponse {
        return try {
            req.awaitBodyOrNull<CocItemsEntity>()
                ?.let { body ->
                    val errors: Errors = BeanPropertyBindingResult(body, CocItemsEntity::class.java.name)
                    validator.validate(body, errors)
                    if (errors.allErrors.isEmpty()) {
                        ServerResponse.ok().bodyValueAndAwait(service.saveCocItemsData(body))

                    } else {
                        onValidationErrors(errors)
                    }
                }
                ?: throw InvalidValueException("No Body found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.debug(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            onErrors(e.message)

        }
    }

//    suspend fun receiveCOR(req: ServerRequest): ServerResponse {
//        return try {
//            req.awaitBodyOrNull<CorsBakEntity>()
//                ?.let { body ->
//                    val errors: Errors = BeanPropertyBindingResult(body, CorsBakEntity::class.java.name)
//                    validator.validate(body, errors)
//                    if (errors.allErrors.isEmpty()) {
//                     //   ServerResponse.ok().bodyValueAndAwait(service.saveCorData(body))
//                    } else {
//                        onValidationErrors(errors)
//                    }
//                }
//                ?: throw InvalidValueException("No Body found")
//        } catch (e: Exception) {
//            KotlinLogging.logger { }.debug(e.message, e)
//            KotlinLogging.logger { }.error(e.message)
//            onErrors(e.message)
//        }
//    }
//
//    suspend fun receiveCOI(req: ServerRequest): ServerResponse {
//        return try {
//            req.awaitBodyOrNull<CoisEntity>()
//                ?.let { body ->
//                    val errors: Errors = BeanPropertyBindingResult(body, CoisEntity::class.java.name)
//                    validator.validate(body, errors)
//                    if (errors.allErrors.isEmpty()) {
//                     //   ServerResponse.ok().bodyValueAndAwait(service.saveCoiData(body))
//
//                    } else {
//                        onValidationErrors(errors)
//                    }
//
//
//                }
//                ?: throw InvalidValueException("No Body found")
//        } catch (e: Exception) {
//            KotlinLogging.logger { }.debug(e.message, e)
//            KotlinLogging.logger { }.error(e.message)
//            onErrors(e.message)
//
//        }
//
//
//    }

    // coi with items
    suspend fun receiveCOIWithItems(req: ServerRequest): ServerResponse {
        return try {
            req.awaitBodyOrNull<CoiWithItems>()
                ?.let { body ->

                    val errors: Errors = BeanPropertyBindingResult(body, CoiWithItems::class.java.name)
                    validator.validate(body, errors)
                    if (errors.allErrors.isEmpty()) {
                        body.coi?.cocNumber = "NA"
                        body.coi?.cocIssueDate = Timestamp.from(Instant.now())
                        body.coi?.cocRemarks = "NA"
                        body.items?.let {
                            for (item in it) {
//                                item.cocNumber = "NA"
                            }
                        }
                        val response = service.saveCoiDataWithItems(body)
                        ServerResponse.ok().bodyValueAndAwait(response)
                    } else {
                        onValidationErrors(errors)
                    }
                }
                ?: throw InvalidValueException("No Body found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.debug(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            onErrors(e.message)

        }
    }

    //end

    suspend fun receiveRfcCOIWithItems(req: ServerRequest): ServerResponse {
        return try {
            req.awaitBodyOrNull<RfcCoiWithItems>()
                ?.let { body ->
                    val errors: Errors = BeanPropertyBindingResult(body, RfcCoiWithItems::class.java.name)
                    validator.validate(body, errors)
                    if (errors.allErrors.isEmpty()) {
                        ServerResponse.ok().bodyValueAndAwait(service.saveRfcCoiWithItemsData(body))

                    } else {
                        onValidationErrors(errors)
                    }
                }
                ?: throw InvalidValueException("No Body found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.debug(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            onErrors(e.message)

        }


    }
    //idf with items
    suspend fun receiveIdfWithItems(req: ServerRequest): ServerResponse {
        return try {
            req.awaitBodyOrNull<IdfWithItemsResponse>()
                ?.let { body ->
                    val errors: Errors = BeanPropertyBindingResult(body, IdfWithItemsResponse::class.java.name)
                    validator.validate(body, errors)
                    if (errors.allErrors.isEmpty()) {
                        ServerResponse.ok().bodyValueAndAwait(service.saveIdfWithItemsData(body))

                    } else {
                        onValidationErrors(errors)
                    }
                }
                ?: throw InvalidValueException("No Body found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.debug(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            onErrors(e.message)
        }
    }
    //recieve idf with items


    suspend fun receiveCOIItems(req: ServerRequest): ServerResponse {
        return try {
            req.awaitBodyOrNull<CoiItemsEntity>()
                ?.let { body ->
                    val errors: Errors = BeanPropertyBindingResult(body, CoiItemsEntity::class.java.name)
                    validator.validate(body, errors)
                    if (errors.allErrors.isEmpty()) {
                        ServerResponse.ok().bodyValueAndAwait(service.saveCoiItemsData(body))
                    } else {
                        onValidationErrors(errors)
                    }
                }
                ?: throw InvalidValueException("No Body found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.debug(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            onErrors(e.message)

        }

    }

    suspend fun receiveRiskProfile(req: ServerRequest): ServerResponse {

        return try {
            req.awaitBodyOrNull<RiskProfileDataEntity>()
                ?.let { body ->
                    val errors: Errors = BeanPropertyBindingResult(body, RiskProfileDataEntity::class.java.name)
                    validator.validate(body, errors)
                    if (errors.allErrors.isEmpty()) {
                        ServerResponse.ok().bodyValueAndAwait(service.saveRiskData(body))

                    } else {
                        onValidationErrors(errors)
                    }


                }
                ?: throw InvalidValueException("No Body found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.debug(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            onErrors(e.message)

        }


    }

    suspend fun receiveMonitoringTimelines(req: ServerRequest): ServerResponse {
        return try {
            req.awaitBodyOrNull<PvocSealIssuesEntity>()
                ?.let { body ->
                    val errors: Errors = BeanPropertyBindingResult(body, PvocSealIssuesEntity::class.java.name)
                    validator.validate(body, errors)
                    if (errors.allErrors.isEmpty()) {
                        ServerResponse.ok().bodyValueAndAwait(service.saveTimelinesData(body))

                    } else {
                        onValidationErrors(errors)
                    }


                }
                ?: throw InvalidValueException("No Body found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.debug(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            onErrors(e.message)

        }

    }

    suspend fun receiveMonitoringStandards(req: ServerRequest): ServerResponse {

        return try {
            req.awaitBodyOrNull<PvocStdMonitoringDataEntity>()
                ?.let { body ->
                    val errors: Errors = BeanPropertyBindingResult(body, PvocStdMonitoringDataEntity::class.java.name)
                    validator.validate(body, errors)
                    if (errors.allErrors.isEmpty()) {
                        ServerResponse.ok().bodyValueAndAwait(service.saveStandardsUsedData(body))

                    } else {
                        onValidationErrors(errors)
                    }


                }
                ?: throw InvalidValueException("No Body found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.debug(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            onErrors(e.message)

        }


    }

    suspend fun receiveMonitoringQueries(req: ServerRequest): ServerResponse {

        return try {
            req.awaitBodyOrNull<PvocQueriesDataEntity>()
                ?.let { body ->
                    val errors: Errors = BeanPropertyBindingResult(body, PvocQueriesDataEntity::class.java.name)
                    validator.validate(body, errors)
                    if (errors.allErrors.isEmpty()) {
                        ServerResponse.ok().bodyValueAndAwait(service.savePvocQueriesData(body))

                    } else {
                        onValidationErrors(errors)
                    }


                }
                ?: throw InvalidValueException("No Body found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.debug(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            onErrors(e.message)

        }


    }

    suspend fun getMonitoringQueriesData(req: ServerRequest): ServerResponse {
        return try {
            req.awaitBodyOrNull<PvocQueriesResponse>()
                ?.let { body ->
                    val errors: Errors = BeanPropertyBindingResult(body, PvocQueriesResponse::class.java.name)
                    validator.validate(body, errors)
                    if (errors.allErrors.isEmpty()) {
                        ServerResponse.ok().bodyValueAndAwait(service.getMonitoringQueriesData(body))

                    } else {
                        onValidationErrors(errors)
                    }
                }
                ?: throw InvalidValueException("No Body found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.debug(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            onErrors(e.message)

        }


    }

    suspend fun getIdfData(req: ServerRequest): ServerResponse {
        return try {
            val country = req.pathVariable("country")
            when {
                country.isBlank() -> throw NullValueNotAllowedException("Country cannot be blank")
                else -> {
                    ServerResponse.ok().bodyValueAndAwait(service.getIdfData(country))
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.debug(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            onErrors(e.message)
        }

    }

    suspend fun getIdfsData(req: ServerRequest): ServerResponse {
        return try {
            val country = req.pathVariable("country")
            when {
                country.isBlank() -> throw NullValueNotAllowedException("Country cannot be blank")
                else -> {
                    ServerResponse.ok().bodyValueAndAwait(service.getIdfsData(country))
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.debug(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            onErrors(e.message)
        }

    }

    suspend fun getIdfsDataAndItems(req: ServerRequest): ServerResponse {
        return try {
            val country = req.pathVariable("country")
            when {
                country.isBlank() -> throw NullValueNotAllowedException("Country cannot be blank")
                else -> {
                    ServerResponse.ok().bodyValueAndAwait(service.getIdfsData(country, true))
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.debug(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            onErrors(e.message)
        }

    }

    suspend fun getIdfItemsData(req: ServerRequest): ServerResponse {
        return try {


            val idfNumber = req.pathVariable("idfNumber")

            when {
                idfNumber.isBlank() -> throw NullValueNotAllowedException("Idf No cannot be blank")
                else -> {
                    ServerResponse.ok().bodyValueAndAwait(service.getIdfItemsData(idfNumber))
                }
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.debug(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            onErrors(e.message)
        }

    }

    suspend fun getInvoiceData(req: ServerRequest): ServerResponse {
        return try {

            val invoiceDate = SimpleDateFormat("yyyy-MM-dd").parse(req.pathVariable("invoiceDate"))
            val soldTo = req.pathVariable("soldTo")
            invoiceDate?.let {
                when {
                    soldTo.isBlank() -> throw NullValueNotAllowedException("SoldTo cannot be blank")
                    else -> {
                        ServerResponse.ok().bodyValueAndAwait(service.getInvoiceData(Date(it.time), soldTo))
                    }
                }
            }
                ?: throw NullValueNotAllowedException("Invoice Date should be provided")

        } catch (e: Exception) {
            KotlinLogging.logger { }.debug(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            onErrors(e.message)
        }

    }

    suspend fun getRiskProfileData(req: ServerRequest): ServerResponse {
        return try {

            val categorizationDate = SimpleDateFormat("yyyy-MM-dd").parse(req.pathVariable("categorizationDate"))
            categorizationDate?.let {
                ServerResponse.ok().bodyValueAndAwait(service.getRiskProfileData(Date(it.time)))
            }
                ?: throw NullValueNotAllowedException("Categorization Date should be provided")

        } catch (e: Exception) {
            KotlinLogging.logger { }.debug(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            onErrors(e.message)
        }

    }

    suspend fun getGoodsRfcData(req: ServerRequest): ServerResponse {
        return try {

            val rfcDate = SimpleDateFormat("yyyy-MM-dd").parse(req.pathVariable("rfcDate"))
            val partnerRef = req.pathVariable("partnerRef")
            rfcDate?.let {
                when {
                    partnerRef.isBlank() -> throw NullValueNotAllowedException("partnerRef cannot be blank")
                    else -> {
                        ServerResponse.ok().bodyValueAndAwait(service.getGoodsRfcData(partnerRef, Date(it.time)))
                    }
                }
            }
                ?: throw NullValueNotAllowedException("Invoice Date should be provided")

        } catch (e: Exception) {
            KotlinLogging.logger { }.debug(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            onErrors(e.message)
        }

    }


    suspend fun getCoiRfcData(req: ServerRequest): ServerResponse {
        return try {

            val rfcDate = SimpleDateFormat("yyyy-MM-dd").parse(req.pathVariable("rfcDate"))
            val partnerRef = req.pathVariable("partnerRef")
            rfcDate?.let {
                when {
                    partnerRef.isBlank() -> throw NullValueNotAllowedException("partnerRef cannot be blank")
                    else -> {
                        ServerResponse.ok().bodyValueAndAwait(service.getCoiRfcData(partnerRef, Date(it.time)))
                    }
                }
            }
                ?: throw NullValueNotAllowedException("RFC Date should be provided")

        } catch (e: Exception) {
            KotlinLogging.logger { }.debug(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            onErrors(e.message)
        }

    }

    suspend fun getCoiRfcDataAndItems(req: ServerRequest): ServerResponse {
        return try {

            val rfcDate = SimpleDateFormat("yyyy-MM-dd").parse(req.pathVariable("rfcDate"))
            val partnerRef = req.pathVariable("partnerRef")
            rfcDate?.let {
                when {
                    partnerRef.isBlank() -> throw NullValueNotAllowedException("partnerRef cannot be blank")
                    else -> {
                        ServerResponse.ok().bodyValueAndAwait(service.getCoiRfcData(partnerRef, Date(it.time), true))
                    }
                }
            }
                ?: throw NullValueNotAllowedException("RFC  Date should be provided")

        } catch (e: Exception) {
            KotlinLogging.logger { }.debug(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            onErrors(e.message)
        }

    }

    suspend fun getCoiRfcItemsData(req: ServerRequest): ServerResponse {
        return try {
            val rfcNumber = req.pathVariable("rfcNumber")
            when {
                rfcNumber.isBlank() -> throw NullValueNotAllowedException("rfcNumber cannot be blank")
                else -> {
                    ServerResponse.ok().bodyValueAndAwait(service.getCoiRfcItemsData(rfcNumber))
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.debug(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            onErrors(e.message)
        }

    }

    suspend fun getCorRfcData(req: ServerRequest): ServerResponse {
        return try {

            val rfcDate = SimpleDateFormat("yyyy-MM-dd").parse(req.pathVariable("rfcDate"))
            val partnerRef = req.pathVariable("partnerRef")
            rfcDate?.let {
                when {
                    partnerRef.isBlank() -> throw NullValueNotAllowedException("partnerRef cannot be blank")
                    else -> {
                        ServerResponse.ok().bodyValueAndAwait(service.getCorRfcData(partnerRef, Date(it.time)))
                    }
                }
            }
                ?: throw NullValueNotAllowedException("RFC  Date should be provided")

        } catch (e: Exception) {
            KotlinLogging.logger { }.debug(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            onErrors(e.message)
        }

    }


    private suspend inline fun <reified T : Any> ServerRequest.validateRequestTypeThen(then: (T) -> Any): ServerResponse {
        return try {

            this.awaitBodyOrNull<T>()?.let { body ->
                val errors: Errors = BeanPropertyBindingResult(body, T::class.java.name)
                validator.validate(body, errors)
                if (errors.allErrors.isEmpty()) {
                    return ServerResponse.ok().bodyValueAndAwait(then(body))
                } else {
                    onValidationErrors(errors)
                }
            }
                ?: throw NullValueNotAllowedException("Invalid Payload")

        } catch (e: Exception) {
            KotlinLogging.logger { }.debug(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            onErrors(e.message)
        }
    }
}
