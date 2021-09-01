package org.kebs.app.kotlin.apollo.api.handlers

import mu.KotlinLogging
import okhttp3.internal.toLongOrDefault
import org.apache.http.HttpStatus
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.payload.request.ConsignmentUpdateRequest
import org.kebs.app.kotlin.apollo.api.payload.request.DemandNoteForm
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.DestinationInspectionBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DestinationInspectionDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.InvoiceDaoService
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.ReportsDaoService
import org.kebs.app.kotlin.apollo.api.service.InvoicePaymentService
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.di.CdItemDetailsEntity
import org.kebs.app.kotlin.apollo.store.repo.IInvoiceRepository
import org.kebs.app.kotlin.apollo.store.repo.IServiceMapsRepository
import org.kebs.app.kotlin.apollo.store.repo.di.IDemandNoteRepository
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.paramOrNull

@Component
class InvoiceHandlers(
        private val invoiceService: InvoicePaymentService,
        private val demandNoteRepository: IDemandNoteRepository,
        private val serviceMapsRepo: IServiceMapsRepository,
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
            req.paramOrNull("cdUuid")
                    ?.let { cdUuid ->
                        val cdDetails = daoServices.findCDWithUuid(cdUuid)
                        response.data = daoServices.findDemandNoteWithCdID(cdDetails.id!!)
                        response.message = "Success"
                        response.responseCode = ResponseCodes.SUCCESS_CODE
                        response
                    }
                    ?: run {
                        response.message = "Required uuid, check config"
                        response.responseCode = ResponseCodes.FAILED_CODE
                        response
                    }
        } catch (e: Exception) {
            response.message = e.localizedMessage
            response.responseCode = ResponseCodes.FAILED_CODE
        }
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
            var hasAllItems = false
            var totalItems = 0
            if (invoiceForm.includeAll) {
                daoServices.findCDItemsListWithCDID(cdDetails).forEach { item ->
                    val fees = invoiceForm.items.filter { it.itemId == item.id }
                    item.paymentFeeIdSelected = daoServices.findDIFee(fees[0].feeId)
                    itemList.add(item)
                }
                totalItems = itemList.size
                hasAllItems = true
            } else {
                invoiceForm.items.forEach {
                    val item = daoServices.findItemWithItemIDAndDocument(cdDetails, it.itemId)
                    // Add to list
                    item.paymentFeeIdSelected = daoServices.findDIFee(it.feeId)
                    itemList.add(item)
                }
                totalItems = daoServices.findCDItemsListWithCDID(cdDetails).size
                hasAllItems = itemList.size == totalItems
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
                val data = mutableMapOf<String, Any?>()
                data.put("demandNoteId", demandNote.id)
                data.put("amount", invoiceForm.amount)
                data.put("remarks", invoiceForm.remarks)
                data.put("hasAllItems", hasAllItems)
                data.put("cdUuid", cdUuid)
                this.diBpmn.startGenerateDemandNote(data, cdDetails)
            }

        } catch (ex: Exception) {
            KotlinLogging.logger { }.error { ex }
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = ex.localizedMessage
        }
        return ServerResponse.ok().body(response)
    }

    fun simulateDemandNotePayment(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        try {
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            val cdUuid = req.pathVariable("cdUuid")
            val consignmentDocument = daoServices.findCDWithUuid(cdUuid)
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            // Update demand note
            val form = req.body(ConsignmentUpdateRequest::class.java)
            with(consignmentDocument) {
                sendDemandNote = map.activeStatus
                sendDemandNoteRemarks = form.remarks
            }
            this.daoServices.updateCdDetailsInDB(consignmentDocument, loggedInUser)
            //Send Demand Note
            val demandNote = consignmentDocument.id?.let {
                daoServices.findDemandNoteWithCDID(
                        it
                )
            }
            //TODO: DemandNote Simulate payment Status
            demandNote?.demandNoteNumber?.let {
                invoiceDaoService.createBatchInvoiceDetails(loggedInUser.userName!!, it)
                        .let { batchInvoiceDetail ->
                            invoiceDaoService.addInvoiceDetailsToBatchInvoice(
                                    demandNote,
                                    applicationMapProperties.mapInvoiceTransactionsForDemandNote,
                                    loggedInUser,
                                    batchInvoiceDetail
                            )
                                    .let { updateBatchInvoiceDetail ->
                                        //Todo: Payment selection
                                        val importerDetails =
                                                consignmentDocument.cdImporter?.let {
                                                    daoServices.findCDImporterDetails(it)
                                                }
                                        val myAccountDetails =
                                                InvoiceDaoService.InvoiceAccountDetails()
                                        with(myAccountDetails) {
                                            accountName = importerDetails?.name
                                            accountNumber = importerDetails?.pin
                                            currency =
                                                    applicationMapProperties.mapInvoiceTransactionsLocalCurrencyPrefix
                                        }
                                        invoiceDaoService.createPaymentDetailsOnStgReconciliationTable(
                                                loggedInUser.userName!!,
                                                updateBatchInvoiceDetail,
                                                myAccountDetails
                                        )
                                        demandNote.id?.let { it1 ->
                                            daoServices.sendDemandNotGeneratedToKWIS(it1)
                                            consignmentDocument.cdStandard?.let { cdStd ->
                                                daoServices.updateCDStatus(
                                                        cdStd,
                                                        daoServices.awaitPaymentStatus.toLong()
                                                )

                                            }
                                        }
                                    }

                        }
            }
//                                                daoServices.demandNotePayment(demandNote, map, loggedInUser)
            //Update BPM payment required task
//                            val cdDetails = updatedItemDetails.cdDocId
//                            cdDetails?.id?.let { it1 ->
//                                cdDetails.assignedInspectionOfficer?.id?.let { it2 ->
//                                    diBpmn.diPaymentRequiredComplete(it1, it2, true)
//                                }
//                            }
        } catch (ex: Exception) {
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = ex.localizedMessage
        }
        return ServerResponse.ok().body(response)
    }

    fun listDemandNotes(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        req.pathVariable("cdId").let {
            response.data = demandNoteRepository.findAllByCdId(it.toLongOrDefault(0L))
            response.message = "Success"
            response.responseCode = ResponseCodes.SUCCESS_CODE
            return ServerResponse.ok().body(response)
        }
    }
}

