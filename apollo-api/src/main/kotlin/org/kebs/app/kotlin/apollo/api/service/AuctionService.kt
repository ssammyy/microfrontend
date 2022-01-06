package org.kebs.app.kotlin.apollo.api.service

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.payload.request.AuctionForm
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DestinationInspectionDaoServices
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.auction.AuctionItemDetails
import org.kebs.app.kotlin.apollo.store.model.auction.AuctionRequestHistory
import org.kebs.app.kotlin.apollo.store.model.auction.AuctionRequests
import org.kebs.app.kotlin.apollo.store.model.auction.AuctionUploadsEntity
import org.kebs.app.kotlin.apollo.store.repo.auction.*
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.math.BigDecimal
import java.sql.Date
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDate

@Service
class AuctionService(
        private val categoryRepo: IAuctionCategoryRepository,
        private val auctionRequestsRepository: IAuctionRequestsRepository,
        private val auctionUploadRepo: IAuctionUploadsEntityRepository,
        private val auctionItemsRepo: IAuctionItemDetailsRepository,
        private val auctionRequestHistoryRepo: IAuctionRequestHistoryRepository,
        private val commonDaoServices: CommonDaoServices,
        private val destinationInspectionDaoServices: DestinationInspectionDaoServices,
        private val applicationMapProperties: ApplicationMapProperties,
        private val apiClientService: ApiClientService
) {

    fun listAuctionCategories(): ApiResponseModel {
        val response = ApiResponseModel()
        response.data = this.categoryRepo.findByStatus(1)
        response.message = "Auction Categories"
        response.responseCode = ResponseCodes.SUCCESS_CODE
        return response
    }

    fun addReport(multipartFile: MultipartFile?, auction: AuctionRequests, description: String, fileRequired: Boolean): Long? {
        multipartFile?.let {
            val upload = AuctionUploadsEntity()
            upload.auctionId = auction
            upload.document = multipartFile.bytes
            upload.fileType = multipartFile.contentType
            upload.fileSize = multipartFile.size
            upload.name = multipartFile.name
            upload.description = description
            val saved = this.auctionUploadRepo.save(upload)
            return saved.id
        } ?: run {
            if (fileRequired) {
                throw ExpectedDataNotFound("Report is required for this item")
            }
        }
        return null
    }

    fun addAuctionHistory(auctionId: Long, auctionName: String, description: String, username: String?): Long? {
        val upload = AuctionRequestHistory()
        upload.auctionId = auctionId
        upload.actionName = auctionName
        upload.description = description
        upload.username = username
        upload.createdBy = username
        upload.createdOn = Timestamp.from(Instant.now())
        upload.description = description
        val saved = this.auctionRequestHistoryRepo.save(upload)
        return saved.id
    }

    fun requestPayment(auctionId: Long, remarks: String, feeId: Long): ApiResponseModel {
        val response = ApiResponseModel()
        val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
        val optional = this.auctionRequestsRepository.findById(auctionId)
        if (optional.isPresent) {
            val auctionRequest = optional.get()
            commonDaoServices.getLoggedInUser()?.let {
                val demandNote = destinationInspectionDaoServices.generateAuctionDemandNoteWithItemList(this.auctionItemsRepo.findByAuctionId(auctionId), map, auctionRequest, false, 0.0, it)
                auctionRequest.demandNoteId = demandNote.id
                auctionRequest.varField1 = "PENDING PAYMENT"
                auctionRequestsRepository.save(auctionRequest)
                response.data = demandNote.id
                response.message = "Success"
                response.responseCode = ResponseCodes.SUCCESS_CODE
                // Publish event to KRA
                val map = mutableMapOf<String, Any?>()
                map["invoice_ref"] = demandNote.demandNoteNumber
                map["amount"] = demandNote.totalAmount
                map["date"] = commonDaoServices.convertDateToString(demandNote.dateGenerated, "dd-MM-yyyy")
                this.processCallback(auctionRequest.createdBy, ResponseCodes.SUCCESS_CODE, "Demand Note generated", auctionRequest.auctionLotNo, "PAYMENT_REQUEST", map)
                // Add history
                addAuctionHistory(auctionId, "GENERATE-DEMAND-NOTE", remarks, commonDaoServices.loggedInUserAuthentication().name)
            } ?: throw ExpectedDataNotFound("Invalid user authenticated")
        } else {
            response.message = "Record not found"
            response.responseCode = ResponseCodes.NOT_FOUND
        }

        return response
    }

    fun processCallback(clientId: String?, responseCode: String, message: String, lotNo: String?, actionCode: String, extra: MutableMap<String, Any?>? = null) {
        try {
            clientId?.let {
                var map = mutableMapOf<String, Any?>()
                if (extra != null) {
                    map = extra
                }
                map["client_id"] = clientId
                map["status"] = responseCode
                map["status_desc"] = message
                map["lot_no"] = lotNo
                map["action_code"] = actionCode
                this.apiClientService.publishCallback(map, it)
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to send callback", ex)
        }
    }

    fun approveRejectAuctionGood(auctionId: Long, multipartFile: MultipartFile?, approved: Boolean, remarks: String): ApiResponseModel {
        val response = ApiResponseModel()
        val opttional = this.auctionRequestsRepository.findById(auctionId)
        if (opttional.isPresent) {
            val auction = opttional.get()
            val reportRequired = "VEHICLE".equals(auction.category?.categoryCode)
            if (approved) {
                auction.reportId = this.addReport(multipartFile, auction, "Approval Report", reportRequired)
                auction.approvalStatus = 1
                addAuctionHistory(auctionId, "APPROVE-REQUEST", remarks, commonDaoServices.loggedInUserAuthentication().name)
                this.processCallback(auction.createdBy, ResponseCodes.SUCCESS_CODE, "Auction Request approved", auction.auctionLotNo, "APPROVED")
            } else {
                auction.reportId = this.addReport(multipartFile, auction, "Rejection Report", reportRequired)
                auction.approvalStatus = 2
                addAuctionHistory(auctionId, "REJECT-REQUEST", remarks, commonDaoServices.loggedInUserAuthentication().name)
                this.processCallback(auction.createdBy, ResponseCodes.SUCCESS_CODE, "Auction Request approved", auction.auctionLotNo, "REJECTED")
            }
            auction.remarks = remarks
            auction.approvedRejectedOn = Timestamp.from(Instant.now())
            this.auctionRequestsRepository.save(auction)
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Success"
        } else {
            response.responseCode = ResponseCodes.NOT_FOUND
            response.message = "Request not found"
        }
        return response
    }

    fun listAuctionGood(keywords: String?, status: String, page: PageRequest): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val pg = when {
                StringUtils.hasLength(keywords) -> this.auctionRequestsRepository.findByAuctionLotNoContains(keywords!!, page)
                else -> {
                    when (status) {
                        "ASSIGNED" -> this.auctionRequestsRepository.findByAssignedOfficer(this.commonDaoServices.getLoggedInUser(), page)
                        "COMPLETED" -> this.auctionRequestsRepository.findByApprovalStatus(1, page)
                        "NEW" -> this.auctionRequestsRepository.findByApprovalStatusInAndAssignedOfficerIsNull(listOf(0, 1), page)
                        else -> null
                    }
                }
            }
            if (pg != null) {
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "Auction goods"
                response.data = pg.toList()
                response.totalCount = pg.totalElements
                response.totalPages = pg.totalPages
            } else {
                response.responseCode = ResponseCodes.FAILED_CODE
                response.message = "Invalid auction status"
            }
        } catch (ex: Exception) {
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Failed to fetch auction goods"
        }
        return response
    }

    fun assignAuctionRequest(auctionId: Long, remarks: String, officerId: Long): ApiResponseModel {
        val response = ApiResponseModel()
        val opttional = this.auctionRequestsRepository.findById(auctionId)
        if (opttional.isPresent) {
            val auction = opttional.get()
            // Assigned
            if (auction.assignedOfficer == null) {
                auction.assigner = this.commonDaoServices.getLoggedInUser()
                auction.assignedOfficer = commonDaoServices.findUserByID(officerId)
                auction.assignedOn = Date.valueOf(LocalDate.now())
                addAuctionHistory(auctionId, "IO-ASSIGN", remarks, auction.assignedOfficer?.userName)
                response.message = "Assigned officer"
            } else {
                auction.assigner = this.commonDaoServices.getLoggedInUser()
                auction.assignedOfficer = commonDaoServices.findUserByID(officerId)
                addAuctionHistory(auctionId, "IO-REASSIGN", remarks, auction.assignedOfficer?.userName)
                response.message = "Reassigned officer"
            }
            this.auctionRequestsRepository.save(auction)
            response.responseCode = ResponseCodes.SUCCESS_CODE
        } else {
            response.responseCode = ResponseCodes.NOT_FOUND
            response.message = "Request not found"
        }
        return response
    }

    fun auctionGoodDetails(auctionId: Long): ApiResponseModel {
        val response = ApiResponseModel()
        val opttional = this.auctionRequestsRepository.findById(auctionId)
        if (opttional.isPresent) {
            val auction = opttional.get()
            // Assigned
            if (auction.assignedOfficer == null) {
                auction.assignedOfficer = this.commonDaoServices.getLoggedInUser()
                auction.assignedOn = Date.valueOf(LocalDate.now())
                this.auctionRequestsRepository.save(auction)
                addAuctionHistory(auctionId, "AUTO-ASSIGN", "Assign IO", auction.assignedOfficer?.userName)
            }
            val map = mutableMapOf<String, Any>()
            map["auction_details"] = auction
            map["attachments"] = auctionUploadRepo.findByAuctionId(auctionId)
            map["items"] = auctionItemsRepo.findByAuctionId(auctionId)
            map["history"] = auctionRequestHistoryRepo.findByAuctionIdAndStatus(auctionId, 1)
            response.data = map
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Auction goods"
        } else {
            response.responseCode = ResponseCodes.NOT_FOUND
            response.message = "Request not found"
        }
        return response
    }

    @Transactional
    fun addAuctionRequest(form: AuctionForm): ApiResponseModel {
        val response = ApiResponseModel()

        val auctionRequest = AuctionRequests()
        form.fillDetails(auctionRequest)
        auctionRequest.createdBy = commonDaoServices.loggedInUserAuthentication().name
        auctionRequest.createdOn = Timestamp.from(Instant.now())
        auctionRequest.category = this.categoryRepo.findByCategoryCode(form.categoryCode?.toUpperCase()
                ?: "NA").orElse(null)
        val saved = this.auctionRequestsRepository.save(auctionRequest)
        val totalAmount = BigDecimal.ZERO
        for (itm in form.items!!) {
            val item = AuctionItemDetails()
            itm.fillDetails(item)
            itm.quantity?.let { q ->
                itm.unitPrice?.let { p ->
                    item.totalPrice = q.multiply(p)
                    totalAmount.plus(item.totalPrice ?: BigDecimal.ZERO)
                }
            }
            when (item.itemType) {
                "VEHICLE" -> {
                    try {
                        item.corId = this.destinationInspectionDaoServices.findCORByChassisNumber(item.chassisNo!!)
                    } catch (ex: Exception) {
                    }
                }
            }
            item.auctionId = saved.id
            this.auctionItemsRepo.save(item)
        }
        try {
            addAuctionHistory(saved.id!!, "REQUEST-RECEIVED", "Received auction request", commonDaoServices.loggedInUserAuthentication().name)
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to add auction", ex)
        }
        response.responseCode = ResponseCodes.SUCCESS_CODE
        response.message = "Request received"
        return response
    }

}