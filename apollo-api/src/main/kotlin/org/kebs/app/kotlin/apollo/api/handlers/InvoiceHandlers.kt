package org.kebs.app.kotlin.apollo.api.handlers

import mu.KotlinLogging
import okhttp3.internal.toLongOrDefault
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.payload.request.ConsignmentUpdateRequest
import org.kebs.app.kotlin.apollo.api.payload.request.DemandNoteForm
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.DestinationInspectionBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DestinationInspectionDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.InvoiceDaoService
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.di.CdItemDetailsEntity
import org.kebs.app.kotlin.apollo.store.repo.di.IDemandNoteRepository
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import java.sql.Timestamp
import java.time.Instant

@Component
class InvoiceHandlers(
        private val demandNoteRepository: IDemandNoteRepository,
        private val commonDaoServices: CommonDaoServices,
        private val applicationMapProperties: ApplicationMapProperties,
        private val daoServices: DestinationInspectionDaoServices,
        private val invoiceDaoService: InvoiceDaoService,
        private val diBpmn: DestinationInspectionBpmn
) {
    final val appId = applicationMapProperties.mapPermitApplication
    final val errors = mutableMapOf<String, String>()

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
                val map = commonDaoServices.serviceMapDetails(appId)
                response.data = mapOf(
                        Pair("deleteSubmitEnabled", noteWithID.status==map.workingStatus),
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
        val map = commonDaoServices.serviceMapDetails(appId)
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
                    if (demandNote.status!! < 0) {
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
                    if (demandNote.status!! < 0) {
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
                        val map = commonDaoServices.serviceMapDetails(appId)
                        this.diBpmn.startGenerateDemandNote(map, data, cdDetails)
                        daoServices.upDateDemandNote(demandNote)
                        response.responseCode = ResponseCodes.SUCCESS_CODE
                        response.message = "Demand note submitted"
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
            daoServices.findDemandNoteWithID(invoiceId)?.let {demandNote->
                if(demandNote.paymentStatus==map.activeStatus) {
                    response.responseCode = ResponseCodes.SUCCESS_CODE
                    response.message = "Demand note payment status submitted to KenTrade"
                    diBpmn.triggerDemandNotePaidBpmTask(demandNote)
                }else {
                    response.responseCode = ResponseCodes.FAILED_CODE
                    response.message = "Demand note payment is not paid"
                }
                response
            }?:run{
                response.responseCode=ResponseCodes.NOT_FOUND
                response.message="Invoice with id $invoiceId does not exist"
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
            response.data = demandNoteRepository.findAllByCdIdAndStatusIn(it.toLongOrDefault(0L), listOf(-1, 0, map.activeStatus, map.initStatus, map.invalidStatus))
            response.message = "Success"
            response.responseCode = ResponseCodes.SUCCESS_CODE
            return ServerResponse.ok().body(response)
        }
    }
}

