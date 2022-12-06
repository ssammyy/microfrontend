package org.kebs.app.kotlin.apollo.api.service

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.payload.request.AuctionForm
import org.kebs.app.kotlin.apollo.api.payload.request.AuctionItem
import org.kebs.app.kotlin.apollo.api.payload.request.DemandNoteRequestForm
import org.kebs.app.kotlin.apollo.api.payload.request.DemandNoteRequestItem
import org.kebs.app.kotlin.apollo.api.payload.response.AuctionRequestDto
import org.kebs.app.kotlin.apollo.api.payload.response.AuctionUploadDao
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DaoService
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DestinationInspectionDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.PaymentPurpose
import org.kebs.app.kotlin.apollo.common.dto.AuditItemEntityDto
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.InvalidValueException
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.auction.AuctionItemDetails
import org.kebs.app.kotlin.apollo.store.model.auction.AuctionRequestHistory
import org.kebs.app.kotlin.apollo.store.model.auction.AuctionRequests
import org.kebs.app.kotlin.apollo.store.model.auction.AuctionUploadsEntity
import org.kebs.app.kotlin.apollo.store.model.di.CdDemandNoteItemsDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.di.DestinationInspectionFeeEntity
import org.kebs.app.kotlin.apollo.store.repo.IUserRoleAssignmentsRepository
import org.kebs.app.kotlin.apollo.store.repo.auction.*
import org.kebs.app.kotlin.apollo.store.repo.di.IDestinationInspectionFeeRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.io.InputStreamReader
import java.io.Reader
import java.math.BigDecimal
import java.sql.Date
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

enum class AuctionGoodStatus(val status: Int) {
    NEW(0), APPROVED(1), REJECTED(2), OTHER(3), HOLD(4), PAYMENT_COMPLETED(5), PAYMENT_PENDING(7), CONDITIONAL_APPROVAL(8);
}

@Service
class AuctionService(
        private val categoryRepo: IAuctionCategoryRepository,
        private val auctionRequestsRepository: IAuctionRequestsRepository,
        private val auctionUploadRepo: IAuctionUploadsEntityRepository,
        private val auctionItemsRepo: IAuctionItemDetailsRepository,
        private val auctionRequestHistoryRepo: IAuctionRequestHistoryRepository,
        private val commonDaoServices: CommonDaoServices,
        private val destinationInspectionDaoServices: DestinationInspectionDaoServices,
        private val daoService: DaoService,
        private val validatorService: DaoValidatorService,
        private val applicationMapProperties: ApplicationMapProperties,
        private val apiClientService: ApiClientService,
        private val roleAssignmentsRepository: IUserRoleAssignmentsRepository,
        private val feeRepository: IDestinationInspectionFeeRepository,
        private val invoicePaymentService: InvoicePaymentService
) {
    val REPORT_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd")
    fun listAuctionCategories(): ApiResponseModel {
        val response = ApiResponseModel()
        response.data = this.categoryRepo.findByStatus(1)
        response.message = "Auction Categories"
        response.responseCode = ResponseCodes.SUCCESS_CODE
        return response
    }

    fun downloadAuctionAttachment(auctionId: Long, attachmentId: Long): AuctionUploadsEntity? {
        return this.auctionUploadRepo.findByIdAndAuctionId_Id(attachmentId, auctionId).orElse(null)
    }

    fun getReportTimestamp(dateStr: String, first: Boolean): Timestamp {
        val date = REPORT_DATE_FORMAT.parse(dateStr)
        val dateTime = when (first) {
            true -> LocalDateTime.of(LocalDate.from(date), LocalTime.MIDNIGHT)
            else -> LocalDateTime.of(LocalDate.from(date), LocalTime.MAX)
        }
        return Timestamp.valueOf(dateTime)
    }

    fun downloadAuctionReport(stardDate: String, endDate: String, status: String): List<AuctionRequestDto> {
        val startTimestamp = getReportTimestamp(stardDate, true)
        val endTimestamp = getReportTimestamp(endDate, false)
        // Get individual items instead
        val auctionReportDetails = when (status.toLowerCase()) {
            "rejected" -> this.auctionItemsRepo.findByAuctionId_ApprovalStatusInAndAuctionId_ApprovedRejectedOnBetween(
                    arrayListOf(AuctionGoodStatus.REJECTED.status),
                    startTimestamp, endTimestamp)
            "approved" -> this.auctionItemsRepo.findByAuctionId_ApprovalStatusInAndAuctionId_ApprovedRejectedOnBetween(
                    arrayListOf(AuctionGoodStatus.CONDITIONAL_APPROVAL.status, AuctionGoodStatus.APPROVED.status),
                    startTimestamp, endTimestamp)
            "all" -> this.auctionItemsRepo.findByAuctionId_ApprovalStatusInAndAuctionId_ApprovedRejectedOnBetween(
                    arrayListOf(AuctionGoodStatus.CONDITIONAL_APPROVAL.status, AuctionGoodStatus.APPROVED.status, AuctionGoodStatus.REJECTED.status, AuctionGoodStatus.HOLD.status),
                    startTimestamp, endTimestamp)
            else -> throw ExpectedDataNotFound("Invalid auction status selected")
        }
        return AuctionRequestDto.fromItemList(auctionReportDetails)
    }

    fun addReport(multipartFile: MultipartFile?, auction: AuctionRequests, description: String, fileRequired: Boolean): Long? {
        multipartFile?.let {
            val upload = AuctionUploadsEntity()
            upload.auctionId = auction
            upload.document = multipartFile.bytes
            upload.fileType = multipartFile.contentType
            upload.fileSize = multipartFile.size
            upload.name = multipartFile.originalFilename
            upload.description = description
            upload.createdBy = commonDaoServices.loggedInUserDetails().userName
            upload.createdOn = Timestamp.from(Instant.now())
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
        upload.status = 1
        upload.createdOn = Timestamp.from(Instant.now())
        upload.description = description
        val saved = this.auctionRequestHistoryRepo.save(upload)
        return saved.id
    }

    fun createPaymentRequest(
        request: AuctionRequests,
        fee: DestinationInspectionFeeEntity,
        route: String
    ): DemandNoteRequestForm {
        val demandNoteReq = DemandNoteRequestForm()
        demandNoteReq.product = "AUCTION"
        demandNoteReq.name = request.importerName
        demandNoteReq.amount = BigDecimal.ZERO.toDouble()
        demandNoteReq.entryNo = request.auctionLotNo ?: ""
        // Entry point
        demandNoteReq.customsOffice = request.location
        request.cfsId?.let {
            demandNoteReq.entryPoint = it.altCfsCode ?: it.cfsCode ?: ""
            if ("JKIA".equals(request.location, true)) {
                demandNoteReq.courier = it.cfsCode
                demandNoteReq.customsOffice = "JKA"
            } else {
                demandNoteReq.courier = ""
            }
        }

        demandNoteReq.ablNumber = request.serialNumber
        demandNoteReq.invoicePrefix = "AG"
        demandNoteReq.presentment = false
        demandNoteReq.importerPin = request.importerPin
        demandNoteReq.ucrNumber = request.auctionLotNo
        demandNoteReq.address = request.location
        demandNoteReq.phoneNumber = request.importerPhone
        demandNoteReq.referenceNumber = request.auctionLotNo
        demandNoteReq.referenceId = request.id
        val itemDetailsList = mutableListOf<DemandNoteRequestItem>()
        val items = this.auctionItemsRepo.findByAuctionId_Id(request.id!!)
        for (element in items) {
            val itm = DemandNoteRequestItem()
            itm.route = route
            itm.quantity = element.quantity?.toLong() ?: 0
            itm.currency = element.currency
            itm.productName = element.itemName
            itm.itemId = element.id
            itm.itemValue = element.totalPrice ?: BigDecimal.ZERO
            itm.fee = fee
            itemDetailsList.add(itm)
        }
        demandNoteReq.items = itemDetailsList
        return demandNoteReq
    }

    fun requestPayment(auctionId: Long, remarks: String, feeId: Long, route: String): ApiResponseModel {
        val response = ApiResponseModel()
        val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
        val optional = this.auctionRequestsRepository.findById(auctionId)
        val fee = feeRepository.findById(feeId)
        if (optional.isPresent && fee.isPresent) {
            val auctionRequest = optional.get()
            commonDaoServices.getLoggedInUser()?.let {
                val demandNoteReq = createPaymentRequest(auctionRequest, fee.get(), route)
                val returnList = mutableListOf<CdDemandNoteItemsDetailsEntity>()
                val demandNote = this.invoicePaymentService.generateDemandNoteWithItemList(
                    demandNoteReq,
                    returnList,
                    map,
                    PaymentPurpose.AUDIT,
                    it
                )
                auctionRequest.demandNoteId = demandNote.id
                auctionRequest.approvalStatus = AuctionGoodStatus.PAYMENT_PENDING.status
                auctionRequest.varField1 = "PENDING PAYMENT"
                auctionRequestsRepository.save(auctionRequest)
                response.data = demandNote.id
                response.message = "Success"
                response.responseCode = ResponseCodes.SUCCESS_CODE
                // Publish event to KRA
                val mapKra = mutableMapOf<String, Any?>()
                mapKra["invoice_ref"] = demandNote.demandNoteNumber
                mapKra["amount"] = demandNote.totalAmount
                mapKra["date"] = commonDaoServices.convertDateToString(demandNote.dateGenerated, "dd-MM-yyyy")
                this.processCallback(auctionRequest.createdBy, ResponseCodes.SUCCESS_CODE, "Demand Note generated", auctionRequest.auctionLotNo, "PAYMENT_REQUEST", mapKra)
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

    fun approveRejectAuctionGood(auctionId: Long, multipartFile: MultipartFile?, approved: String, remarks: String, witnessName: String, witnessDesc: String, witnessEmail: String): ApiResponseModel {
        val response = ApiResponseModel()
        val opttional = this.auctionRequestsRepository.findById(auctionId)
        if (opttional.isPresent) {
            val auction = opttional.get()
            val reportRequired = "VEHICLE".equals(auction.category?.categoryCode)
            when (approved) {
                "APPROVE" -> {
                    auction.reportId = this.addReport(multipartFile, auction, "Approval Report", reportRequired)
                    auction.approvalStatus = AuctionGoodStatus.APPROVED.status
                    addAuctionHistory(auctionId, "APPROVE-REQUEST", remarks, commonDaoServices.loggedInUserAuthentication().name)
                    this.processCallback(auction.createdBy, ResponseCodes.SUCCESS_CODE, "Auction Request approved", auction.auctionLotNo, approved)
                }
                "CONDITIONAL" -> {
                    auction.reportId = this.addReport(multipartFile, auction, "Rejection Report", reportRequired)
                    auction.approvalStatus = AuctionGoodStatus.CONDITIONAL_APPROVAL.status
                    addAuctionHistory(auctionId, "CONDITION-APPROVAL-REQUEST", remarks, commonDaoServices.loggedInUserAuthentication().name)
                    this.processCallback(auction.createdBy, ResponseCodes.SUCCESS_CODE, "Auction Request conditionally approved", auction.auctionLotNo, approved)
                }
                "REJECT" -> {
                    auction.reportId = this.addReport(multipartFile, auction, "Rejection Report", reportRequired)
                    auction.approvalStatus = AuctionGoodStatus.REJECTED.status
                    addAuctionHistory(auctionId, "REJECT-REQUEST", remarks, commonDaoServices.loggedInUserAuthentication().name)
                    this.processCallback(auction.createdBy, ResponseCodes.SUCCESS_CODE, "Auction Request approved", auction.auctionLotNo, approved)
                }
                "HOLD" -> {
                    auction.reportId = this.addReport(multipartFile, auction, "Rejection Report", reportRequired)
                    auction.approvalStatus = AuctionGoodStatus.HOLD.status
                    addAuctionHistory(auctionId, "HOLD-REQUEST", remarks, commonDaoServices.loggedInUserAuthentication().name)
                    this.processCallback(auction.createdBy, ResponseCodes.SUCCESS_CODE, "Auction Request approval held", auction.auctionLotNo, approved)
                }
                else -> throw ExpectedDataNotFound("Invalid approval status selected: $approved")
            }
            auction.witnessDesignation = witnessDesc
            auction.witnessName = witnessName
            auction.witnessEmail = witnessEmail
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
                    when (status.toLowerCase()) {
                        "rejected" -> this.auctionRequestsRepository.findByApprovalStatusIn(listOf(AuctionGoodStatus.REJECTED.status), page)
                        "hold" -> this.auctionRequestsRepository.findByApprovalStatusIn(listOf(AuctionGoodStatus.HOLD.status, AuctionGoodStatus.PAYMENT_PENDING.status), page)
                        "approved" -> this.auctionRequestsRepository.findByApprovalStatusIn(listOf(AuctionGoodStatus.APPROVED.status, AuctionGoodStatus.CONDITIONAL_APPROVAL.status), page)
                        "new" -> this.auctionRequestsRepository.findByApprovalStatusInAndAssignedOfficerIsNull(listOf(AuctionGoodStatus.NEW.status), page)
                        "assigned" -> this.auctionRequestsRepository.findByApprovalStatusInAndAssignedOfficer(listOf(AuctionGoodStatus.NEW.status, AuctionGoodStatus.PAYMENT_COMPLETED.status), this.commonDaoServices.loggedInUserDetails(), page)
                        else -> null
                    }
                }
            }
            if (pg != null) {
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "Auction goods"
                response.data = AuctionRequestDto.fromList(pg.toList())
                response.totalCount = pg.totalElements
                response.totalPages = pg.totalPages
            } else {
                response.responseCode = ResponseCodes.FAILED_CODE
                response.message = "Invalid auction status: $status"
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to list items: ", ex)
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Failed to fetch auction goods"
        }
        return response
    }

    fun assignAuctionRequest(auctionId: Long, remarks: String, officerId: Long, reassign: Boolean?): ApiResponseModel {
        val response = ApiResponseModel()
        val opttional = this.auctionRequestsRepository.findById(auctionId)
        if (opttional.isPresent) {
            val auction = opttional.get()
            // Assigned
            response.responseCode = ResponseCodes.SUCCESS_CODE
            if (auction.assignedOfficer == null) {
                auction.assigner = this.commonDaoServices.getLoggedInUser()
                auction.assignedOfficer = commonDaoServices.findUserByID(officerId)
                auction.assignedOn = Date.valueOf(LocalDate.now())
                addAuctionHistory(auctionId, "IO-ASSIGN", remarks, auction.assignedOfficer?.userName)
                response.message = "Assigned officer"
            } else if (reassign == true) {
                auction.assigner = this.commonDaoServices.getLoggedInUser()
                auction.assignedOfficer = commonDaoServices.findUserByID(officerId)
                addAuctionHistory(auctionId, "IO-REASSIGN", remarks, auction.assignedOfficer?.userName)
                response.message = "Reassigned officer"
            } else {
                response.responseCode = ResponseCodes.FAILED_CODE
                response.message = "Already assigned"
            }
            this.auctionRequestsRepository.save(auction)
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
            val userEntity = this.commonDaoServices.getLoggedInUser()
            if (auction.assignedOfficer == null) {
                val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
                val inspectorCount = this.roleAssignmentsRepository.checkUserHasRole("DI_Inspection_Officers", map.activeStatus, userEntity?.id
                        ?: 0)
                if (inspectorCount > 0) {
                    auction.assignedOfficer = userEntity
                    auction.assignedOn = Date.valueOf(LocalDate.now())
                    this.auctionRequestsRepository.save(auction)
                    addAuctionHistory(auctionId, "AUTO-ASSIGN", "Assign IO", auction.assignedOfficer?.userName)
                }
            }
            val dataMap = mutableMapOf<String, Any>()
            val auctionDto = AuctionRequestDto.fromEntity(auction)
            auctionDto.myTask = userEntity?.userName.equals(auction.assignedOfficer?.userName, true)
            auctionDto.isSupervisor = this.commonDaoServices.currentUserDiSupervisor()
            auctionDto.isInspector = this.commonDaoServices.currentUserDiOfficer()
            dataMap["auction_details"] = auctionDto
            dataMap["attachments"] = AuctionUploadDao.fromList(auctionUploadRepo.findByAuctionId(auction))
            dataMap["items"] = auctionItemsRepo.findByAuctionId_Id(auctionId)
            dataMap["history"] = auctionRequestHistoryRepo.findByAuctionIdAndStatusOrderByCreatedOnDesc(auctionId, 1)
            auction.demandNoteId?.let { demandNoteId ->
                this.destinationInspectionDaoServices.findDemandNoteWithID(demandNoteId)?.let { demandNote ->
                    dataMap["payment"] = demandNote
                }
            }
            response.data = dataMap
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
        this.auctionRequestsRepository.findByAuctionLotNo(form.auctionLotNo)?.let {
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Auction with lot no exists: " + form.auctionLotNo
            return response
        }
        val cfsCode = this.destinationInspectionDaoServices.findCfsCd((form.cfsCode ?: "").trim().toUpperCase())
        if (cfsCode == null) {
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "CFS code ${form.cfsCode} does not exists, please enter a valid CFS code "
            return response
        }
        KotlinLogging.logger { }.info("Adding auction with cfs code: ${form.cfsCode}")
        val auctionRequest = AuctionRequests()
        form.fillDetails(auctionRequest)
        auctionRequest.cfsId = cfsCode
        auctionRequest.approvalStatus = AuctionGoodStatus.NEW.status
        auctionRequest.status = 0
        auctionRequest.varField4 = form.cfsCode
        auctionRequest.serialNumber = "NA"
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
            item.auctionId = saved
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

    // Fill Form for upload
    fun addAuctionItem(item: AuditItemEntityDto, categoryCode: String?, auctionDate: Date) {
        val request = AuctionForm()
        request.itemLocation = item.location
        request.auctionLotNo = item.lotNumber
        request.auctionDate = auctionDate
        request.shipmentPort = item.shipName
        request.arrivalDate = item.dateOfArrival
        request.cfsCode = item.cfsCode
        request.country = item.country
        // Check chassis number for category
        if (StringUtils.hasLength(item.containerChassisNumber)) {
            request.categoryCode = "VEHICLE"
        } else {
            request.categoryCode = "GOODS"
        }
        request.importerName = item.consignee
        request.importerPhone = item.consignee
        request.containerSize = item.containerSize
        val tmpItem = AuctionItem()

        tmpItem.chassisNo = item.containerChassisNumber
        tmpItem.itemName = item.description
        tmpItem.itemType = item.manifestNo
        tmpItem.serialNo = item.blNo
        tmpItem.itemType = request.categoryCode
        tmpItem.unitPrice = BigDecimal.ZERO
        tmpItem.quantity = BigDecimal.valueOf(1)
        request.items = arrayListOf(tmpItem)
        this.addAuctionRequest(request)
    }

    fun uploadAttachment(multipartFile: MultipartFile?, remarks: String, auctionId: Long): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val opttional = this.auctionRequestsRepository.findById(auctionId)
            if (opttional.isPresent) {
                this.addReport(multipartFile, opttional.get(), remarks, true)
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "Attachment received"
            } else {
                response.responseCode = ResponseCodes.NOT_FOUND
                response.message = "Record not found"
            }
        } catch (ex: Exception) {
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Failed, file was not saved"
        }
        return response
    }

    fun uploadAuctionGoods(multipartFile: MultipartFile, fileType: String?, categoryCode: String?, listingDate: Date, cfsCode: String?): ApiResponseModel {
        val response = ApiResponseModel()
        val endsWith = multipartFile.originalFilename?.endsWith(".txt")
        if (!(multipartFile.contentType == "text/csv" || endsWith == true)) {
            throw InvalidValueException("Incorrect file type received, try again later")
        }
        var separator = ','
        if ("TSV".equals(fileType)) {
            KotlinLogging.logger { }.info("TAB SEPARATED DATA")
            separator = '\t'
        } else {
            KotlinLogging.logger { }.info("COMMA SEPARATED DATA")
        }
        val targetReader: Reader = InputStreamReader(ByteArrayInputStream(multipartFile.bytes))
        val audits = this.daoService.readAuditCsvFile(separator, targetReader)
        val errors = mutableListOf<Any>()
        for (a in audits) {
            if (!StringUtils.hasLength(a.cfsCode)) {
                a.cfsCode = cfsCode
            }
            validatorService.validateInputWithInjectedValidator(a)?.let {
                errors.add(it)
            } ?: run {
                this.addAuctionItem(a, categoryCode, listingDate)
            }
        }
        if (errors.isEmpty()) {
            response.message = "Success"
            response.responseCode = ResponseCodes.SUCCESS_CODE
        } else {
            response.errors = errors
            response.message = "Detected some invalid data in request file"
            response.responseCode = ResponseCodes.INVALID_CODE
        }
        return response
    }

}