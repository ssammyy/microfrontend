package org.kebs.app.kotlin.apollo.api.handlers.invoice

import mu.KotlinLogging
import okhttp3.internal.toLongOrDefault
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.payload.extractPage
import org.kebs.app.kotlin.apollo.api.payload.request.DemandNoteForm
import org.kebs.app.kotlin.apollo.api.payload.response.CallbackResponses
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.DestinationInspectionBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DestinationInspectionDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.sage.response.PaymentStatusResult
import org.kebs.app.kotlin.apollo.api.service.InvoicePaymentService
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.di.CdItemDetailsEntity
import org.kebs.app.kotlin.apollo.store.repo.di.IDemandNoteRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartHttpServletRequest
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.paramOrNull
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@Component
class InvoiceHandlers(
        private val demandNoteRepository: IDemandNoteRepository,
        private val commonDaoServices: CommonDaoServices,
        private val applicationMapProperties: ApplicationMapProperties,
        private val daoServices: DestinationInspectionDaoServices,
        private val diBpmn: DestinationInspectionBpmn,
        private val invoicePaymentService: InvoicePaymentService
) {
    final val errors = mutableMapOf<String, String>()
    final val DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy")
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
            val date = req.paramOrNull("date")?.let {
                var dt = LocalDate.now()
                if (!it.trim().isEmpty()) {
                    val parsedDate = LocalDate.from(DATE_FORMAT.parse(it))
                    dt = parsedDate
                }
                dt
            } ?: LocalDate.now()
            response.data = daoServices.listExchangeRates(DATE_FORMAT.format(date))
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
                        Pair("deleteSubmitEnabled", noteWithID.status == map.workingStatus),
                        Pair("items", noteItems),
                        Pair("note", noteWithID)
                )
                response.message = "Invoice details"
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response
            }
        } catch (e: Exception) {
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
            val itemList = mutableListOf<CdItemDetailsEntity>()
            val mapErrors = mutableMapOf<Long, String>()
            var totalItems = 0
            if (invoiceForm.includeAll) {
                daoServices.findCDItemsListWithCDID(cdDetails).forEach { item ->
                    val fees = invoiceForm.items.filter { it.itemId == item.id }
                    if (fees.isEmpty()) {
                        mapErrors.put(item.id!!, "Fee not selected with all items included")
                    } else {
                        try {
                            item.paymentFeeIdSelected = daoServices.findDIFee(fees[0].feeId)
                        } catch (ex: Exception) {
                            mapErrors.put(item.id!!, ex.localizedMessage)
                        }
                    }
                    itemList.add(item)
                }
                totalItems = itemList.size
            } else {
                invoiceForm.items.forEach {
                    val item = daoServices.findItemWithItemIDAndDocument(cdDetails, it.itemId)
                    // Add to list
                    try {
                        item.paymentFeeIdSelected = daoServices.findDIFee(it.feeId)
                    } catch (ex: Exception) {
                        mapErrors.put(item.id!!, ex.localizedMessage)
                    }
                    itemList.add(item)
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
            if (itemList.isEmpty() && totalItems > 0) {
                response.responseCode = ResponseCodes.FAILED_CODE
                response.message = "Consignment does not have items or none was selected"
                return ServerResponse.ok().body(response)
            }
            // Update if required
            itemList.forEach { item ->
                // Update item details
                if (!invoiceForm.presentment) {
                    val updatedItemDetails = daoServices.updateCdItemDetailsInDB(
                            commonDaoServices.updateDetails(
                                    item,
                                    item
                            ) as CdItemDetailsEntity, loggedInUser
                    )
                }
            }
            KotlinLogging.logger { }.info("Total Items: ${itemList.size}")
            // Calculate demand note amount and save
            val demandNote = daoServices.generateDemandNoteWithItemList(
                    itemList,
                    map,
                    cdDetails,
                    invoiceForm.presentment,
                    invoiceForm.amount,
                    loggedInUser
            )
            if (invoiceForm.presentment) {
                response.data = demandNote
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "Success"
            } else {
                demandNote.status = map.workingStatus
                demandNote.varField1 = invoiceForm.remarks
                demandNote.varField2 = (itemList.size == totalItems).toString()
                demandNote.varField3 = "NEW"
                daoServices.upDateDemandNote(demandNote)
                cdDetails.varField10 = "DEMAND NOTE GENERATED AWAITING SUBMISSION"
                daoServices.updateCdDetailsInDB(cdDetails, loggedInUser)
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
                    if (demandNote.status == map.workingStatus) {
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
                        cdDetails.varField10 = "DEMAND NOTE SUBMITTED AWAITING APPROVAL"
                        this.daoServices.updateCdDetailsInDB(cdDetails, commonDaoServices.getLoggedInUser())
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
                    diBpmn.triggerDemandNotePaidBpmTask(demandNote)
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
            response.data = demandNoteRepository.findAllByCdIdAndStatusIn(it.toLongOrDefault(0L), listOf(-1, 0, map.activeStatus, map.workingStatus, map.initStatus, map.invalidStatus))
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
                transactionStatus = status.get().toInt()
            }
            val date = req.param("date")
            val transactionNo = req.param("trx")
            val page = extractPage(req)

            val documents = this.invoicePaymentService.listTransactions(transactionStatus, date, transactionNo, page)
            response.data = documents.toList()
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

    fun paymentCallback(req: ServerRequest): ServerResponse {
        val result = CallbackResponses()
        try {
            val responseStatus = req.body(PaymentStatusResult::class.java)
            KotlinLogging.logger { }.info("Payment result: $responseStatus")
            if (this.invoicePaymentService.paymentReceived(responseStatus)) {
                result.message = "Success"
                result.status = ResponseCodes.SUCCESS_CODE
            } else {
                result.message = "Failed"
                result.status = ResponseCodes.FAILED_CODE
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Payment Callback failed:", ex)
            result.message = "Request failed please try again later"
            result.status = ResponseCodes.EXCEPTION_STATUS
        }
        return ServerResponse.ok().body(result)
    }
}

