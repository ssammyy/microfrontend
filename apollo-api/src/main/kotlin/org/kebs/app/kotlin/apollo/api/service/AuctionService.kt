package org.kebs.app.kotlin.apollo.api.service

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.payload.request.AuctionForm
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DestinationInspectionDaoServices
import org.kebs.app.kotlin.apollo.store.model.auction.AuctionItemDetails
import org.kebs.app.kotlin.apollo.store.model.auction.AuctionRequests
import org.kebs.app.kotlin.apollo.store.model.auction.AuctionUploadsEntity
import org.kebs.app.kotlin.apollo.store.repo.auction.IAuctionCategoryRepository
import org.kebs.app.kotlin.apollo.store.repo.auction.IAuctionItemDetailsRepository
import org.kebs.app.kotlin.apollo.store.repo.auction.IAuctionRequestsRepository
import org.kebs.app.kotlin.apollo.store.repo.auction.IAuctionUploadsEntityRepository
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
        private val commonDaoServices: CommonDaoServices,
        private val destinationInspectionDaoServices: DestinationInspectionDaoServices
) {

    fun listAuctionCategories(): ApiResponseModel {
        val response = ApiResponseModel()
        response.data = this.categoryRepo.findByStatus(1)
        response.message = "Auction Categories"
        response.responseCode = ResponseCodes.SUCCESS_CODE
        return response
    }

    fun addReport(multipartFile: MultipartFile, auctionId: Long, description: String): Long {
        val upload = AuctionUploadsEntity()
        upload.auctionId = auctionId
        upload.document = multipartFile.bytes
        upload.fileType = multipartFile.contentType
        upload.fileSize = multipartFile.size
        upload.name = multipartFile.name
        upload.description = description
        val saved = this.auctionUploadRepo.save(upload)
        return saved.id
    }

    fun processCallback(clientId: String?, reponseCode: String, message: String, lotNo: String?) {

    }

    fun approveRejectAuctionGood(auctionId: Long, multipartFile: MultipartFile, approved: Boolean, remarks: String): ApiResponseModel {
        val response = ApiResponseModel()
        val opttional = this.auctionRequestsRepository.findById(auctionId)
        if (opttional.isPresent) {
            val auction = opttional.get()
            if (approved) {
                auction.reportId = this.addReport(multipartFile, auctionId, "Approval Report")
                auction.approvalStatus = 1
            } else {
                auction.reportId = this.addReport(multipartFile, auctionId, "Rejection Report")
                auction.approvalStatus = 2
            }
            auction.remarks = remarks
            auction.approvedRejectedOn = Timestamp.from(Instant.now())
            this.auctionRequestsRepository.save(auction)
            this.processCallback(auction.createdBy, ResponseCodes.SUCCESS_CODE, "Auction Request approved", auction.auctionLotNo)
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
            }
            val map = mutableMapOf<String, Any>()
            map["auction_details"] = auction
            map["attachments"] = auctionUploadRepo.findByAuctionId(auctionId)
            map["items"] = auctionItemsRepo.findByAuctionId(auctionId)
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

        response.responseCode = ResponseCodes.SUCCESS_CODE
        response.message = "Request received"
        return response
    }

}