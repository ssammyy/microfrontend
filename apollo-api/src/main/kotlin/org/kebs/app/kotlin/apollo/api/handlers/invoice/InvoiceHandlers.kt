package org.kebs.app.kotlin.apollo.api.handlers.invoice

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import okhttp3.internal.toLongOrDefault
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.payload.extractPage
import org.kebs.app.kotlin.apollo.api.payload.request.DemandNoteForm
import org.kebs.app.kotlin.apollo.api.payload.request.DemandNoteRequestForm
import org.kebs.app.kotlin.apollo.api.payload.request.DemandNoteRequestItem
import org.kebs.app.kotlin.apollo.api.payload.response.CallbackResponses
import org.kebs.app.kotlin.apollo.api.payload.response.DemandNoteDto
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.DestinationInspectionBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DestinationInspectionDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.PaymentPurpose
import org.kebs.app.kotlin.apollo.api.ports.provided.sage.PostInvoiceToSageServices
import org.kebs.app.kotlin.apollo.api.ports.provided.sage.response.PaymentStatusResult
import org.kebs.app.kotlin.apollo.api.ports.provided.validation.AbstractValidationHandler
import org.kebs.app.kotlin.apollo.api.service.DaoValidatorService
import org.kebs.app.kotlin.apollo.api.service.InvoicePaymentService
import org.kebs.app.kotlin.apollo.common.dto.sage.response.SageNotificationResponse
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.InvalidValueException
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.di.CdDemandNoteItemsDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.di.CdItemDetailsEntity
import org.kebs.app.kotlin.apollo.store.repo.di.IDemandNoteRepository
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.Errors
import org.springframework.validation.Validator
import org.springframework.web.multipart.MultipartHttpServletRequest
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body
import java.sql.Timestamp
import java.time.Instant
import java.time.format.DateTimeParseException

@Component
class InvoiceHandlers(
    private val demandNoteRepository: IDemandNoteRepository,
    private val commonDaoServices: CommonDaoServices,
    private val applicationMapProperties: ApplicationMapProperties,
    private val daoServices: DestinationInspectionDaoServices,
    private val diBpmn: DestinationInspectionBpmn,
    private val objectMapper: ObjectMapper,
    private val invoicePaymentService: InvoicePaymentService,
    private val sageServices: PostInvoiceToSageServices,
    private val daoValidatorService: DaoValidatorService,
    private val validator: Validator
) : AbstractValidationHandler() {
    final val errors = mutableMapOf<String, String>()

    fun applicationUploadExchangeRates(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        try {
            val multipartRequest = (req.servletRequest() as? MultipartHttpServletRequest)
                ?: throw ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE,
                    "Request is not a multipart request"
                )
            val multipartFile = multipartRequest.getFile("file")
            val fileType = multipartRequest.getParameter("file_type")
            if (multipartFile != null) {
                response.data = invoicePaymentService.uploadExchangeRates(multipartFile, fileType)
                response.message = "Success"
                response.responseCode = ResponseCodes.SUCCESS_CODE
            } else {
                response.message = "Failed, no file received"
                response.responseCode = ResponseCodes.FAILED_CODE
            }
        } catch (e: Exception) {
            response.message = "Invalid file type provided"
            response.responseCode = ResponseCodes.FAILED_CODE
        }
        return ServerResponse.ok().body(response)
    }

    fun applicationExchangeRates(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        try {
            val rangeType = req.param("rangeType").orElse("OTHER")
            when (rangeType) {
                "RANGE" -> {
                    response.data = mapOf(
                        Pair(
                            "today",
                            daoServices.listExchangeRatesRange(
                                req.param("date").orElse(""),
                                req.param("endDate").orElse("")
                            )
                        ),
                        Pair("active", daoServices.listCurrentExchangeRates(1))
                    )
                }
                else -> {
                    response.data = mapOf(
                        Pair("today", daoServices.listExchangeRates(req.param("date").orElse(""))),
                        Pair("active", daoServices.listCurrentExchangeRates(1))
                    )
                }
            }

            response.message = "Success"
            response.responseCode = ResponseCodes.SUCCESS_CODE
        } catch (e: Exception) {
            KotlinLogging.logger { }.error("FAILED to LOAD rates", e)
            response.message = "Invalid date provided"
            response.responseCode = ResponseCodes.FAILED_CODE
        }
        return ServerResponse.ok().body(response)
    }

    fun applicationFee(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        try {
            response.data = daoServices.listDIFee()
            response.message = "Success"
            response.responseCode = ResponseCodes.SUCCESS_CODE
        } catch (e: Exception) {
            response.message = e.localizedMessage
            response.responseCode = ResponseCodes.FAILED_CODE
        }
        return ServerResponse.ok().body(response)
    }

    fun cdInvoiceDetails(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        try {
            req.pathVariable("invoiceId").let { invoiceId ->
                val noteWithID = daoServices.findDemandNoteWithID(invoiceId.toLongOrDefault(0L))
                val noteItems = daoServices.findDemandNoteItemDetails(noteWithID?.id!!)
                val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
                KotlinLogging.logger { }.info("TTT: ${map.workingStatus}")
                response.data = mapOf(
                    Pair(
                        "deleteSubmitEnabled",
                        (noteWithID.status == map.workingStatus && noteWithID.postingStatus != map.activeStatus)
                    ),
                    Pair("items", noteItems),
                    Pair("note", DemandNoteDto.fromEntity(noteWithID, true))
                )
                response.message = "Invoice details"
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error("Failed to get demand note details", e)
            response.message = e.localizedMessage
            response.responseCode = ResponseCodes.FAILED_CODE
        }
        //
        return ServerResponse.ok()
            .body(response)
    }

    fun generateDemandNote(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        try {
            val cdUuid = req.pathVariable("cdUuid")
            val cdDetails = daoServices.findCDWithUuid(cdUuid)
            val invoiceForm = req.body(DemandNoteForm::class.java)
            val mapErrors = mutableMapOf<Long, String>()
            val totalItems: Int
            val demandRequest = DemandNoteRequestForm()
            if (invoiceForm.includeAll) {
                daoServices.findCDItemsListWithCDID(cdDetails).forEach { item ->
                    val fees = invoiceForm.items.filter { it.itemId == item.id }
                    if (fees.isEmpty()) {
                        mapErrors.put(item.id!!, "Fee not selected with all items included")
                    } else {
                        try {
                            val formItem = DemandNoteRequestItem()
                            formItem.fee = daoServices.findDIFee(fees[0].feeId)
                            formItem.itemValue = item.totalPriceNcy
                            formItem.productName = item.itemDescription ?: item.hsDescription
                                    ?: item.productTechnicalName
                            formItem.itemId = item.id
                            formItem.currency = item.foreignCurrencyCode
                            formItem.quantity = item.quantity?.toLong() ?: 0
                            demandRequest.addItem(formItem)
                            // Update demand note status
                            if (!invoiceForm.presentment) {
                                item.dnoteStatus = map.activeStatus
                                daoServices.updateCdItemDetailsInDB(
                                    commonDaoServices.updateDetails(
                                        item,
                                        item
                                    ) as CdItemDetailsEntity, loggedInUser
                                )
                            }
                        } catch (ex: Exception) {
                            mapErrors.put(item.id!!, ex.localizedMessage)
                        }
                    }

                }
                totalItems = invoiceForm.items.size
            } else {
                invoiceForm.items.forEach {
                    val item = daoServices.findItemWithItemIDAndDocument(cdDetails, it.itemId)
                    // Add to list
                    try {
                        val formItem = DemandNoteRequestItem()
                        formItem.fee = daoServices.findDIFee(it.feeId)
                        formItem.itemValue = item.totalPriceNcy
                        formItem.productName = item.itemDescription ?: item.hsDescription ?: item.productTechnicalName
                        formItem.itemId = item.id
                        formItem.currency = item.foreignCurrencyCode
                        formItem.quantity = item.quantity?.toLong() ?: 0
                        demandRequest.addItem(formItem)
                        // Update demand note status
                        if (!invoiceForm.presentment) {
                            item.dnoteStatus = map.activeStatus
                            daoServices.updateCdItemDetailsInDB(
                                commonDaoServices.updateDetails(
                                    item,
                                    item
                                ) as CdItemDetailsEntity, loggedInUser
                            )
                        }
                    } catch (ex: Exception) {
                        mapErrors.put(item.id!!, ex.localizedMessage)
                    }
                }
                totalItems = daoServices.findCDItemsListWithCDID(cdDetails).size
            }
            // Failed response
            if (mapErrors.isNotEmpty()) {
                response.message = "Invalid request"
                response.responseCode = ResponseCodes.FAILED_CODE
                response.errors = mapErrors
                return ServerResponse.ok().body(response)
            }
            // Reject for consignment with no items?
            if (demandRequest.items?.isEmpty() == true && totalItems > 0) {
                response.responseCode = ResponseCodes.FAILED_CODE
                response.message = "Consignment does not have items or none was selected"
                return ServerResponse.ok().body(response)
            }
            KotlinLogging.logger { }.info("Total Items: ${demandRequest.items?.size}")
            // Add consignment details to Demand note
            demandRequest.referenceId = cdDetails.id
            val cdImporter = cdDetails.cdImporter?.let { daoServices.findCDImporterDetails(it) }
            demandRequest.name = cdImporter?.name
            demandRequest.address = cdImporter?.email
            demandRequest.importerPin = cdImporter?.pin
            demandRequest.phoneNumber = cdImporter?.telephone
            demandRequest.referenceNumber = cdDetails.cdStandard?.applicationRefNo
            demandRequest.ablNumber = cdDetails.cdStandard?.declarationNumber ?: "UNKNOWN"
            demandRequest.product = cdDetails.ucrNumber ?: "UNKNOWN"
            // Add extra details
            cdDetails.cdTransport?.let {
                val transport = daoServices.findCdTransportDetails(it)
                demandRequest.customsOffice = transport.portOfArrival // JKIA or MSA
            }
            cdDetails.cdStandardsTwo?.let {
                demandRequest.courier = it.courierName
                demandRequest.courierPin = it.courierPin
            }

            demandRequest.entryNo = cdDetails.ucrNumber ?: ""
            cdDetails.freightStation?.let {
                demandRequest.revenueLineNumber = it.revenueLineNumber
                if ("JKIA".equals(demandRequest.customsOffice, true)) {
                    demandRequest.courier = it.cfsNumber ?: ""
                    demandRequest.entryPoint = it.altCfsCode ?: it.cfsCode
                } else {
                    demandRequest.courier = ""
                    demandRequest.entryPoint = when {
                        StringUtils.hasLength(it.altCfsCode) -> it.altCfsCode
                        else -> it.cfsCode
                    }
                }
            }

            // Calculate demand note amount and save
            val demandNoteItems = mutableListOf<CdDemandNoteItemsDetailsEntity>()
            val demandNote = invoicePaymentService.generateDemandNoteWithItemList(
                demandRequest,
                demandNoteItems,
                map,
                PaymentPurpose.CONSIGNMENT,
                loggedInUser
            )
            if (invoiceForm.presentment) {
                val dt = mutableMapOf<String, Any>()
                dt["demandNote"] = demandNote
                dt["items"] = demandNoteItems
                response.data = dt
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "Success"
            } else {
                val dt = mutableMapOf<String, Any>()
                dt["id"] = demandNote.id ?: 0
                demandNote.status = map.workingStatus
                demandNote.varField1 = invoiceForm.remarks
                demandNote.varField2 = (demandRequest.items?.size == totalItems).toString()
                demandNote.varField3 = "NEW"
                daoServices.upDateDemandNote(demandNote)
                cdDetails.varField10 = "DEMAND NOTE GENERATED AWAITING SUBMISSION"
                daoServices.updateCdDetailsInDB(cdDetails, loggedInUser)
                response.data = dt
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "Demand note generated, review under demand note tab and submit for approval"
            }

        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("DEMAND NOTE GENERATION ERROR", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed to submit request"
        }
        return ServerResponse.ok().body(response)
    }

    fun deleteDemandNote(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        try {
            req.pathVariable("invoiceId").let { invoiceId ->
                val demandNote = daoServices.findDemandNoteWithID(invoiceId.toLongOrDefault(0L))
                if (demandNote != null) {
                    val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
                    if (demandNote.status == map.workingStatus && demandNote.postingStatus != map.activeStatus) {
                        val loggedInUser = commonDaoServices.loggedInUserDetails()
                        demandNote.status = 50
                        demandNote.varField3 = "DELETED"
                        demandNote.deletedOn = Timestamp.from(Instant.now())
                        demandNote.deleteBy = loggedInUser.userName
                        daoServices.upDateDemandNote(demandNote)
                        response.responseCode = ResponseCodes.SUCCESS_CODE
                        response.message = "Demand not deleted"
                    } else {
                        response.responseCode = ResponseCodes.NOT_FOUND
                        response.message = "Demand not cannot be deleted after submission or deletion"
                    }
                } else {
                    response.responseCode = ResponseCodes.NOT_FOUND
                    response.message = "Demand not found"
                }
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("DEMAND NOTE DELETE ERROR", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Demand deletion failed"
        }
        return ServerResponse.ok().body(response)
    }

    fun submitDemandNoteRequest(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            val invoiceId = req.pathVariable("invoiceId").toLong()
            response = invoicePaymentService.generateOtherInvoiceBatch(invoiceId)
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to process request", ex)
            response.message = "Invalid identifier"
            response.responseCode = ResponseCodes.INVALID_CODE
        }
        return ServerResponse.ok().body(response)
    }


    fun submitDemandNoteForApproval(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        try {
            req.pathVariable("invoiceId").let { invoiceId ->
                val demandNote = daoServices.findDemandNoteWithID(invoiceId.toLongOrDefault(0L))
                if (demandNote != null) {
                    val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
                    if (demandNote.status == map.workingStatus) {
                        val cdDetails = daoServices.findCD(demandNote.cdId!!)
                        val data = mutableMapOf<String, Any?>()
                        data["demandNoteId"] = demandNote.id
                        data["amount"] = demandNote.totalAmount
                        data["supervisor"] = cdDetails.assigner?.userName
                        data["remarks"] = demandNote.varField1
                        data["hasAllItems"] = demandNote.varField2?.toBoolean() ?: false
                        data["cdUuid"] = cdDetails.uuid
                        demandNote.status = 0
                        demandNote.varField3 = "SUBMITTED"

                        this.diBpmn.startGenerateDemandNote(map, data, cdDetails)
                        daoServices.upDateDemandNote(demandNote)
                        response.responseCode = ResponseCodes.SUCCESS_CODE
                        response.message = "Demand note submitted, awaiting supervisor approval"
                    } else {
                        response.responseCode = ResponseCodes.FAILED_CODE
                        response.message = "Demand has already been submitted"
                    }
                } else {
                    response.responseCode = ResponseCodes.NOT_FOUND
                    response.message = "Demand not found"
                }
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("DEMAND NOTE SUBMISSION ERROR", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Demand submission failed"
        }
        return ServerResponse.ok().body(response)
    }

    fun checkPaymentDemandNotePayment(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        try {
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            val invoiceId = req.pathVariable("invoiceId").toLongOrDefault(0L)
            daoServices.findDemandNoteWithID(invoiceId)?.let { demandNote ->
                if (demandNote.paymentStatus == map.activeStatus) {
                    response.responseCode = ResponseCodes.SUCCESS_CODE
                    response.message = "Demand note payment status submitted to KenTrade"
                    if (demandNote.paymentPurpose == PaymentPurpose.CONSIGNMENT.code) {
                        diBpmn.triggerDemandNotePaidBpmTask(demandNote)
                    }
                } else {
                    response.responseCode = ResponseCodes.FAILED_CODE
                    response.message = "Demand note payment is not paid"
                }
                response
            } ?: run {
                response.responseCode = ResponseCodes.NOT_FOUND
                response.message = "Invoice with id $invoiceId does not exist"
            }
            // Update demand note

        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("SIMULATE PAYMENT", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = ex.localizedMessage
        }
        return ServerResponse.ok().body(response)
    }

    fun listDemandNotes(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()

        req.pathVariable("cdId").let {
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            response.data = DemandNoteDto.fromList(
                demandNoteRepository.findAllByCdIdAndStatusIn(
                    it.toLongOrDefault(0L),
                    listOf(-1, 0, map.activeStatus, map.workingStatus, map.initStatus, map.invalidStatus)
                )
            )
            response.message = "Success"
            response.responseCode = ResponseCodes.SUCCESS_CODE
            return ServerResponse.ok().body(response)
        }
    }

    fun listAllDemandNotes(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        try {
            val status = req.param("status")
            var transactionStatus: Int? = null
            if (status.isPresent) {
                transactionStatus = status.get().toIntOrNull()
            }
            val date = req.param("date")
            val endDate = req.param("end_date")
            val transactionNo = req.param("trx")
            val page = extractPage(req)
            val documents = this.invoicePaymentService.listTransactions(
                transactionStatus,
                date,
                endDate,
                transactionNo,
                page
            )
            response.data = DemandNoteDto.fromList(documents.toList())
            response.pageNo = documents.number
            response.totalPages = documents.totalPages
            response.totalCount = documents.totalElements
            response.message = "Success"
            response.responseCode = ResponseCodes.SUCCESS_CODE
        } catch (ex: DateTimeParseException) {
            KotlinLogging.logger { }.error("invalid date transaction", ex)
            response.message = "Invalid date selected"
            response.responseCode = ResponseCodes.FAILED_CODE
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to load transaction", ex)
            response.message = "Could not load transactions"
            response.responseCode = ResponseCodes.FAILED_CODE
        }
        return ServerResponse.ok().body(response)
    }

    fun getDemandNoteStats(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        try {
            val date = req.param("date")
            response.data = this.invoicePaymentService.getTransactionStatsOnDate(date)
            response.message = "Success"
            response.responseCode = ResponseCodes.SUCCESS_CODE
        } catch (ex: DateTimeParseException) {
            response.message = "Invalid date selected"
            response.responseCode = ResponseCodes.FAILED_CODE
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to load transaction", ex)
            response.message = "Could not load transactions"
            response.responseCode = ResponseCodes.FAILED_CODE
        }
        return ServerResponse.ok().body(response)
    }

    @PreAuthorize("hasAuthority('PAYMENT')")
    fun paymentCallback(req: ServerRequest): ServerResponse {
        var result = CallbackResponses()
        try {
            val responseStatus = req.body(PaymentStatusResult::class.java)
            KotlinLogging.logger { }.info("Payment result: ${objectMapper.writeValueAsString(responseStatus)}")
            this.daoValidatorService.validateInputWithInjectedValidator(responseStatus)?.let {
                result.message = "Failed to process request"
                result.errors = it
                result.status = ResponseCodes.INVALID_CODE
                result
            } ?: run {
                result = this.invoicePaymentService.paymentReceived(responseStatus)
                result
            }
        } catch (ex: ExpectedDataNotFound) {
            result.message = ex.localizedMessage
            result.status = ResponseCodes.INVALID_CODE
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Payment Callback failed:", ex)
            result.message = "Request failed please try again later"
            result.status = ResponseCodes.EXCEPTION_STATUS
        }
        return ServerResponse.ok().body(result)
    }

    @PreAuthorize("hasAuthority('PAYMENT')")
    fun processPaymentSageNotification(req: ServerRequest): ServerResponse {
        return try {
            req.body<SageNotificationResponse>()
                .let { body ->
                    val errors: Errors = BeanPropertyBindingResult(body, SageNotificationResponse::class.java.name)
                    validator.validate(body, errors)
                    if (errors.allErrors.isEmpty()) {
                        val response = sageServices.processPaymentSageNotification(body)
                        ServerResponse.ok().body(response)

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

}

