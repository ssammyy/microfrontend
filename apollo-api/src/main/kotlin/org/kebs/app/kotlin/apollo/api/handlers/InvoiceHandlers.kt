package org.kebs.app.kotlin.apollo.api.handlers

import mu.KotlinLogging
import okhttp3.internal.toLongOrDefault
import org.apache.http.HttpStatus
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.payload.request.ConsignmentUpdateRequest
import org.kebs.app.kotlin.apollo.api.payload.request.DemandNoteForm
import org.kebs.app.kotlin.apollo.api.ports.provided.createUserAlert
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DestinationInspectionDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.InvoiceDaoService
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.ReportsDaoService
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.di.CdItemDetailsEntity
import org.kebs.app.kotlin.apollo.store.repo.IInvoiceRepository
import org.kebs.app.kotlin.apollo.store.repo.IServiceMapsRepository
import org.kebs.app.kotlin.apollo.store.repo.di.IDemandNoteItemsDetailsRepository
import org.kebs.app.kotlin.apollo.store.repo.di.IDemandNoteRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.paramOrNull
import java.util.stream.Collectors
import java.util.stream.IntStream

@Component
class InvoiceHandlers(
        private val invoiceRepository: IInvoiceRepository,
        private val demandNoteRepository: IDemandNoteRepository,
        private val serviceMapsRepo: IServiceMapsRepository,
        private val commonDaoServices: CommonDaoServices,
        private val applicationMapProperties: ApplicationMapProperties,
        private val daoServices: DestinationInspectionDaoServices,
        private val invoiceDaoService: InvoiceDaoService,
        private val reportsDaoService: ReportsDaoService
) {
    final val appId = applicationMapProperties.mapPermitApplication
    private final val invoiceHomePage = "quality-assurance/customer/my-invoices"
    final val errors = mutableMapOf<String, String>()

    fun cdInvoiceDetails(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        try {
            req.paramOrNull("cdUuid")
                    ?.let { cdUuid ->
                        val map = commonDaoServices.serviceMapDetails(appId)
                        val cdDetails = daoServices.findCDWithUuid(cdUuid)
                        val itemListWithDemandNote = daoServices.findCDItemsListWithCDIDAndDemandNoteStatus(cdDetails, map)
                        response.data = daoServices.findDemandNoteListFromCdItemList(itemListWithDemandNote, map.inactiveStatus)
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
            if (invoiceForm.includeAll) {
                itemList.addAll(daoServices.findCDItemsListWithCDID(cdDetails))
            } else {
                invoiceForm.items.forEach {
                    val item = daoServices.findItemWithItemIDAndDocument(cdDetails, it.itemId)
                    // Add to list
                    itemList.add(item)
                }
            }
            // Reject for consignment with no items?
            if(itemList.isEmpty()){
                response.responseCode = ResponseCodes.FAILED_CODE
                response.message = "Consignment does not have items or none was selected"
                return ServerResponse.ok().body(response)
            }
            // Update if required
            itemList.forEach{item->
                //Values before update of item Details
                if (item.confirmFeeIdSelected != null) {
                    item.apply {
                        paymentFeeIdSelected = confirmFeeIdSelected?.let { daoServices.findDIFee(it) }
                    }
                } else {
                    item.paymentFeeIdSelected=daoServices.findDIFee(1)
                }
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
                    loggedInUser
            )
            response.data = demandNote
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Success"
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
                invoiceDaoService.createBatchInvoiceDetails(loggedInUser, it)
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
                                                loggedInUser,
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

    fun downloadDemandNote(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        try {
            req.pathVariable("demandNoteId").let {
                var map = hashMapOf<String, Any>()

                val demandNote = daoServices.findDemandNoteWithID(it.toLongOrDefault(0L))
                val demandNoteItemList = demandNote?.id?.let { daoServices.findDemandNoteItemDetails(it) }
                        ?: throw ExpectedDataNotFound("No List Of Details Available does not exist")

                map["preparedBy"] = demandNote.generatedBy.toString()
                map["datePrepared"] = demandNote.dateGenerated.toString()
                map["demandNoteNo"] = demandNote.demandNoteNumber.toString()
                map["importerName"] = demandNote.nameImporter.toString()
                map["importerAddress"] = demandNote.address.toString()
                map["importerTelephone"] = demandNote.telephone.toString()
                map["ablNo"] = demandNote.entryAblNumber.toString()
                map["totalAmount"] = demandNote.totalAmount.toString()
                map["receiptNo"] = demandNote.receiptNo.toString()

                map = reportsDaoService.addBankAndMPESADetails(map)

                val extractReport = reportsDaoService.extractReport(
                        map,
                        applicationMapProperties.mapReportDemandNoteWithItemsPath,
                        demandNoteItemList
                )
                // Response with file
                return ServerResponse.ok()
                        .header("Content-Disposition", "inline; filename=NOTE-${it}.pdf;")
                        .contentType(MediaType.APPLICATION_PDF)
                        .contentLength(extractReport.size().toLong())
                        .body(extractReport)
            }
        } catch (ex: Exception) {
            KotlinLogging.logger {  }.error { ex }
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed to generate demand note"
        }
        return ServerResponse.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .body(response)
    }
}

