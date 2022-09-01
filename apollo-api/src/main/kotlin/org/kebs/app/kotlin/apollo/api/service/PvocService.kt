package org.kebs.app.kotlin.apollo.api.service

import mu.KotlinLogging
import org.flowable.common.engine.api.FlowableException
import org.kebs.app.kotlin.apollo.api.controllers.diControllers.pvoc.ExceptionPayload
import org.kebs.app.kotlin.apollo.api.handlers.forms.WaiverApplication
import org.kebs.app.kotlin.apollo.api.notifications.NotificationCodes
import org.kebs.app.kotlin.apollo.api.notifications.NotificationService
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.payload.request.PvocComplaintForm
import org.kebs.app.kotlin.apollo.api.payload.response.*
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.PvocBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DestinationInspectionDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.ReviewStatus
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.store.model.pvc.*
import org.kebs.app.kotlin.apollo.store.repo.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.math.BigDecimal
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

enum class DocumentTypes {
    COMPLAINT, WAIVER, EXEMPTION, OTHER;
}

enum class PvocExemptionStatus(val status: String, val code: Int) {
    NEW_APPLICATIONS("NEW", 0), PVOC_APPROVED("PVOC_APPROVE", 1), PVOC_REJECTED("PVOC_REJECTED", 2), DEFERRED("DEFERRED", 3), CERT_APPROVED("CERT_APPROVE", 1), CERT_REJECTED("CERT_REJECTED", 1)
}

@Service("pvocService")
class PvocService(
        private val iPvocApplicationProductsRepo: IPvocApplicationProductsRepo,
        private val iPvocApplicationRepo: IPvocApplicationRepo,
        private val iPvocExceptionApplicationStatusEntityRepo: IPvocExceptionApplicationStatusEntityRepo,
        private val iPvocExceptionRawMaterialCategoryEntityRepo: IPvocExceptionRawMaterialCategoryEntityRepo,
        private val iPvocExceptionMainMachineryCategoryEntityRepo: IPvocExceptionMainMachineryCategoryEntityRepo,
        private val iPvocExceptionIndustrialSparesCategoryEntityRepo: IPvocExceptionIndustrialSparesCategoryEntityRepo,
        private val iPvocApplicationDocumentRepo: IPvocApplicationDocumentRepo,
        private val pvocBpmn: PvocBpmn,
        private val pvocComplaintRemarksEntityRepo: PvocComplaintRemarksEntityRepo,
        private val notificationService: NotificationService,
        private val partnerService: PvocPartnerService,
        private val waiversRemarksRepo: IPvocWaiversRemarksRepo,
        private val complaintRecommendationRepo: PvocComplainRecommendationEntityRepo,
        private val ipvocExemptionCertificateRepository: IPvocExceptionCertificateRepository,
        private val iPvocWaiversCategoriesRepo: IPvocWaiversCategoriesRepo,
        private val iPvocMasterListRepo: IPvocMasterListRepo,
        private val iwaiversApplicationRepo: IwaiversApplicationRepo,
        private val commonDaoServices: CommonDaoServices,
        private val daoServices: DestinationInspectionDaoServices,
        private val standardLevyRepo: IStandardLevyPaymentsRepository,
        private val complaintEntityRepo: IPvocComplaintRepo,
        private val complaintCategoryRepo: IPvocComplaintCategoryRepo,
        private val complaintSubCategoryRepo: IPvocComplaintCertificationsSubCategoryRepo
) {

    fun addUploads(auth: Authentication, reference: String, docType: DocumentTypes, files: List<MultipartFile>?) {
        files?.forEach { file ->
            val waiverDocs = PvocApplicationDocumentsEntity()
            waiverDocs.name = file.originalFilename
            waiverDocs.fileType = file.contentType
            waiverDocs.documentType = file.bytes
            waiverDocs.status = 1
            waiverDocs.modifiedBy = auth.name
            waiverDocs.modifiedOn = Timestamp.from(Instant.now())
            waiverDocs.createdBy = auth.name
            waiverDocs.createdOn = Timestamp.from(Instant.now())
            waiverDocs.refId = reference
            waiverDocs.refType = docType.name
            iPvocApplicationDocumentRepo.save(waiverDocs)
        }
    }

    fun pvocAgentQueryStatus(complaintId: Long, remarks: String, pvocOfficer: String) {
        val optional = this.complaintEntityRepo.findById(complaintId)
        if (optional.isPresent) {
            val complaint = optional.get()
            complaint.agentReviewRemarks = remarks
            complaint.agentRemarksOn = Timestamp.from(Instant.now())
            complaint.modifiedBy = commonDaoServices.findUserByUserName(pvocOfficer).userName
            complaint.modifiedOn = Timestamp.from(Instant.now())
            this.complaintEntityRepo.save(complaint)
            addComplaintRemarks(complaintId, remarks, "AGENT_QUERY")
        }
    }

    fun pvocOfficerReviewStatus(complaintId: Long, recommendedAction: Long, remarks: String, action: String, pvocOfficer: String) {
        val optional = this.complaintEntityRepo.findById(complaintId)
        if (optional.isPresent) {
            val complaint = optional.get()
            complaint.reviewStatus = action
            complaint.recomendation = remarks
            complaint.reviewedOn = Timestamp.from(Instant.now())
            this.complaintRecommendationRepo.findByIdOrNull(recommendedAction)?.let {
                complaint.recommendedAction = it
            }
            complaint.pvocUser = commonDaoServices.findUserByUserName(pvocOfficer)
            this.complaintEntityRepo.save(complaint)
            addComplaintRemarks(complaintId, remarks, "AGENT_REVIEW")
        }
    }

    fun mpvocReviewStatus(complaintId: Long, remarks: String, action: String, pvocOfficer: String) {
        val optional = this.complaintEntityRepo.findById(complaintId)
        if (optional.isPresent) {
            val complaint = optional.get()
            complaint.mpvoc = commonDaoServices.findUserByUserName(pvocOfficer)
            complaint.mpvocRecomendationDate = Timestamp.from(Instant.now())
            complaint.mpvocRemarks = remarks
            this.complaintEntityRepo.save(complaint)
        }
    }

    fun hodReviewStatus(complaintId: Long, remarks: String, action: String, pvocOfficer: String) {
        val optional = this.complaintEntityRepo.findById(complaintId)
        if (optional.isPresent) {
            val complaint = optional.get()
            complaint.hod = commonDaoServices.findUserByUserName(pvocOfficer)
            complaint.hodRemarks = remarks
            when (action) {
                "APPROVE" -> {
                    complaint.reviewStatus = ReviewStatus.APPROVE.name
                    complaint.status = ReviewStatus.APPROVE.code
                }
                "REJECT" -> {
                    complaint.reviewStatus = ReviewStatus.REJECTED.name
                    complaint.status = ReviewStatus.REJECTED.code
                }
                "DEFFERED" -> {
                    complaint.reviewStatus = ReviewStatus.DEFFERED.name
                    complaint.status = ReviewStatus.DEFFERED.code
                }
            }
            complaint.hodRecomendationDate = Timestamp.from(Instant.now())
            this.complaintEntityRepo.save(complaint)
        }
    }

    fun sendComplaintAcknowledgementEmail(complaintId: Long) {
        val optional = this.complaintEntityRepo.findById(complaintId)
        if (optional.isPresent) {
            val complaint = optional.get()
            // Send email
            this.sendComplaintEmail(complaint, NotificationCodes.COMPLAINT_RECEIVED.name, complaint.email ?: "")
        }
    }

    fun sendComplaintReviewEmail(complaintId: Long, remarks: String) {
        val optional = this.complaintEntityRepo.findById(complaintId)
        if (optional.isPresent) {
            val complaint = optional.get()
            // Send email
            if (complaint.status != ReviewStatus.DEFFERED.code) {
                this.sendComplaintEmail(complaint, complaint.reviewStatus ?: "UNKNOWN", complaint.email ?: "", remarks)
            }
        }
    }


    fun pvocComplaintProcessCompleted(complaintId: Long) {
        KotlinLogging.logger { }.info("Complain Process completed")
    }

    fun pvocOfficerActionCompleted(complaintId: Long, action: String, remarks: String, pvocOfficer: String) {
        this.sendComplaintReviewEmail(complaintId, remarks)
    }

    fun getRecommendations(): ApiResponseModel {
        val response = ApiResponseModel()
        val categories = this.complaintRecommendationRepo.findByStatus(1)
        response.data = PvocComplaintRecommendationDao.fromList(categories)
        response.responseCode = ResponseCodes.SUCCESS_CODE
        response.message = "Success"
        return response
    }

    fun addComplaintRemarks(complaintId: Long, remarks: String, remarkType: String): ApiResponseModel {
        val response = ApiResponseModel()
        val optional = this.complaintEntityRepo.findById(complaintId)
        if (optional.isPresent) {
            val complaint = optional.get()
            val rem = PvocComplaintRemarksEntity()
            rem.complaintId = complaint.id
            rem.remarks = remarks
            rem.createdBy = commonDaoServices.loggedInUserAuthentication().name
            rem.createdOn = Timestamp.from(Instant.now())
            rem.remarkAgainist = remarkType
            this.pvocComplaintRemarksEntityRepo.save(rem)
            response.data = rem
            response.message = "Remarks added"
            response.responseCode = ResponseCodes.SUCCESS_CODE
        } else {
            response.message = "Complaint not found"
            response.responseCode = ResponseCodes.NOT_FOUND
        }
        return response
    }

    fun sendComplaintEmail(complaintEntity: PvocComplaintEntity, emailType: String, recipient: String, remarks: String? = null) {
        val data = mutableMapOf<String, Any>()
        data["complaint"] = PvocComplaintDao.fromEntity(complaintEntity, false)
        data["referenceNumber"] = complaintEntity.refNo ?: "UNKNOWN"
        data["remarks"] = remarks ?: "NO REMARKS"
        this.notificationService.sendEmail(recipient, emailType, data)
    }


    fun approveCurrentComplaint(complaintId: Long, recommendedAction: Long, action: String, taskId: String, remarks: String): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val data = mutableMapOf<String, Any>()
            data["pvocOfficer"] = commonDaoServices.loggedInUserAuthentication().name
            data["action"] = action.toUpperCase()
            data["approve"] = action.toUpperCase()
            if (recommendedAction > 0) {
                data["recommendedAction"] = recommendedAction
            }
            data["remarks"] = remarks
            this.pvocBpmn.pvocCompleteTask(taskId, data)
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Task completed"
        } catch (ex: FlowableException) {
            KotlinLogging.logger { }.error("Failed", ex)
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Task failed: ${ex.message}"
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Task failed: ${ex.message}"
        }
        return response
    }

    fun listComplaints(complaintStatus: String, keywords: String?, page: PageRequest): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val pg = when (keywords) {
                null -> this.complaintEntityRepo.findAllByReviewStatus(complaintStatus, page)
                else -> this.complaintEntityRepo.findAllByRefNoContains(keywords, page)
            }
            response.data = PvocComplaintDao.fromList(pg.toList())
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Success"
            response.totalCount = pg.totalElements
            response.totalPages = pg.totalPages
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Complaint failed", ex)
            response.message = "Failed to get data: ${ex.message}"
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
        }
        return response
    }

    fun complaintDetails(complaintId: Long): ApiResponseModel {
        val response = ApiResponseModel()
        val optional = this.complaintEntityRepo.findById(complaintId)
        if (optional.isPresent) {
            val complaint = optional.get()
            val data = mutableMapOf<String, Any?>()
            this.daoServices.findCocByCocNumber(complaint.cocNumber ?: "")?.let { cocDetails ->
                data["cd_uuid"] = cocDetails.consignmentDocId?.uuid
                data["certificate_details"] = cocDetails
            }
            // Complaint tasks and auto assignment
            try {
                val auth = commonDaoServices.loggedInUserAuthentication()
                val activeUser = commonDaoServices.getLoggedInUser()
                when {
                    auth.authorities.stream().anyMatch { authority -> authority.authority == "PVOC_OFFICER" } -> {
                        data.put("is_pvoc_officer", true)
                        if (complaint.pvocUser == null) {
                            complaint.pvocUser = activeUser
                            this.complaintEntityRepo.save(complaint)
                        }
                        data.put("is_owner", complaint.pvocUser?.id == activeUser?.id)
                        data.put("tasks", this.pvocBpmn.getComplaintTasks(listOf("PVOC_OFFICER", "PVOC_QUERY"), complaint.id!!))
                    }
                    auth.authorities.stream().anyMatch { authority -> authority.authority == "MPVOC_OFFICER" } -> {
                        data.put("is_mpvoc_officer", true)
                        if (complaint.mpvoc == null) {
                            complaint.mpvoc = activeUser
                            this.complaintEntityRepo.save(complaint)
                        }
                        data.put("is_owner", complaint.mpvoc?.id == activeUser?.id)
                        data.put("tasks", this.pvocBpmn.getComplaintTasks(listOf("MPVOC_OFFICER", "MPVOC"), complaint.id!!))
                    }
                    auth.authorities.stream().anyMatch { authority -> authority.authority == "HOD_OFFICER" } -> {
                        data.put("is_hod_officer", true)
                        if (complaint.hod == null) {
                            complaint.hod = activeUser
                            this.complaintEntityRepo.save(complaint)
                        }
                        data.put("is_owner", complaint.hod?.id == activeUser?.id)
                        data.put("tasks", this.pvocBpmn.getComplaintTasks(listOf("HOD_OFFICER", "HOD"), complaint.id!!))
                    }
                }
            } catch (ex: Exception) {
                KotlinLogging.logger { }.error("Failed to add data", ex)
            }
            // Add details
            // TODO: remove this in production
            data.put("tasks", this.pvocBpmn.getComplaintTasks(listOf("HOD_OFFICER", "MPVOC_OFFICER", "PVOC_OFFICER", "PVOC_QUERY", "HOD"), complaint.id!!))
            data["assigned_officer"] = "${complaint.pvocUser?.firstName} ${complaint.pvocUser?.lastName}"
            data["complaint"] = PvocComplaintDao.fromEntity(complaint, false)
            data["attachments"] = PvocWaiverAttachmentDao.fromList(this.iPvocApplicationDocumentRepo.findFirstByRefIdAndRefType(complaint.refNo.orEmpty(), DocumentTypes.COMPLAINT.name))
            data["remarks"] = pvocComplaintRemarksEntityRepo.findAllByComplaintId(complaint.id!!)
            response.data = data
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Success"
        } else {
            response.message = "Record not found"
            response.responseCode = ResponseCodes.NOT_FOUND
        }
        return response
    }

    fun companyComplaintHistory(statusDesc: String, page: PageRequest): ApiResponseModel {
        val response = ApiResponseModel()
        val status = when (statusDesc) {
            "new" -> ComplaintStatus.NEW.code
            "approved" -> ComplaintStatus.PVOC_APPROVED.code
            "rejected" -> ComplaintStatus.PVOC_REJECTED.code
            else -> ComplaintStatus.NEW.code
        }
        val user = this.commonDaoServices.loggedInUserDetails()
        user.companyId?.let {
            val requests = this.complaintEntityRepo.findAllByStatusAndCompanyId(status, it, page)
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Success"
            response.data = PvocComplaintDao.fromList(requests.toList(), true)
            response.totalPages = requests.totalPages
            response.totalCount = requests.totalElements
            response
        } ?: run {
            response.message = "Invalid company setting"
            response.responseCode = ResponseCodes.FAILED_CODE
        }
        return response
    }

    fun getComplaintCategories(): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val categories = this.complaintCategoryRepo.findAllByStatus(1)
            response.data = PvocComplaintCategoryDao.fromList(categories)
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Success"
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Complaint categories: ", ex)
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Failed to load categories"
        }
        return response
    }

    fun generateComplaintRef(prefix: String): String {
        val count = complaintEntityRepo.countAllByRefPrefix(prefix)
        return "%s%05x".format(prefix, count + 1)
    }

    fun pvocFileComplaint(form: PvocComplaintForm, uploads: List<MultipartFile>): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val prefix = commonDaoServices.convertDateToString(LocalDateTime.now(), "yyyyMMdd")
            val complaint = PvocComplaintEntity()
            val user = commonDaoServices.getLoggedInUser()
            user?.companyId?.let { companyId ->
                val company = this.commonDaoServices.findCompanyProfileWithProfileID(companyId)

                form.fillDetails(complaint)
                complaint.reviewStatus = ComplaintStatus.NEW.name
                complaint.status = ComplaintStatus.NEW.code
                complaint.refPrefix = prefix
                complaint.companyId = companyId
                complaint.companyRegNumber = company?.registrationNumber
                complaint.directorIdNumber = company?.directorIdNumber
                complaint.refNo = generateComplaintRef(prefix)
                complaint.createdOn = Timestamp.from(Instant.now())
                complaint.createdBy = commonDaoServices.loggedInUserAuthentication().name
                val errors = mutableMapOf<String, String>()
                val category = complaintCategoryRepo.findById(form.categoryId!!)
                if (category.isPresent) {
                    complaint.compliantNature = category.get()
                } else {
                    errors["categoryId"] = "Please select category"
                }
                // Sub Category is optional
                if (form.subCategoryId == null) {
                    form.subCategoryId = 0
                }
                val subCategory = complaintSubCategoryRepo.findById(form.subCategoryId!!)
                if (subCategory.isPresent) {
                    complaint.compliantSubCategory = subCategory.get()
                } else if (form.subCategoryId!! > 0) {
                    errors["subCategoryId"] = "Please select sub category"
                }
                val agent = partnerService.getPartner(form.pvocAgent!!)
                if (agent != null) {
                    complaint.pvocAgent = agent
                } else {
                    errors["pvocAgent"] = "Please select PVOC agent"
                }
                if (errors.isEmpty()) {
                    val saved = this.complaintEntityRepo.save(complaint)
                    this.addUploads(commonDaoServices.loggedInUserAuthentication(), saved.refNo.orEmpty(), DocumentTypes.COMPLAINT, uploads)
                    response.data = saved.id
                    response.message = "Request received"
                    response.responseCode = ResponseCodes.SUCCESS_CODE
                    // Start BPM process
                    this.pvocBpmn.startPvocComplaintApplicationsProcess(saved)
                    this.complaintEntityRepo.save(saved)
                } else {
                    response.errors = errors
                    response.responseCode = ResponseCodes.INVALID_CODE
                    response.message = "Please make corrections and resubmit"
                }
                response
            } ?: run {
                response.responseCode = ResponseCodes.INVALID_CODE
                response.message = "Account is not a company"
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to add complaint: ", ex)
            response.message = "Invalid request"
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
        }
        return response
    }

    fun checkExemptionApplicable(): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val userDetails = commonDaoServices.getLoggedInUser()
            userDetails?.companyId?.let {
                this.standardLevyRepo.findFirstByManufacturerEntityOrderByCreatedOnDesc(it)?.let { levy ->
                    if (levy.levyPayable!! <= BigDecimal.ZERO) {
                        response.responseCode = ResponseCodes.SUCCESS_CODE
                        response.message = "Success"
                    } else {
                        response.data = levy.levyPayable
                        response.responseCode = ResponseCodes.FAILED_CODE
                        response.message = "not eligible for exemption application"
                    }
                    response
                } ?: run {
                    response.responseCode = ResponseCodes.NOT_FOUND
                    response.message = "Record not available"
                    response
                }

            } ?: run {
                response.responseCode = ResponseCodes.NOT_FOUND
                response.message = "User is not a company"
                response
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Error checking eligibility: ", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Request failed"
        }
        return response
    }

    var statuses = iPvocExceptionApplicationStatusEntityRepo.findFirstByMapId(1)
    fun waiverApplicationHistory(page: Int, size: Int): ApiResponseModel {
        val response = ApiResponseModel()
        SecurityContextHolder.getContext().authentication.name.let { username ->
            val requests = this.iwaiversApplicationRepo.findAllByCreatedBy(username, PageRequest.of(page, size))
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Success"
            response.data = requests?.toList()
            response.totalPages = requests?.totalPages
        }
        return response
    }


    fun updateWaiverTaskStatus(waiverId: Long, action: String, taskId: String, remarks: String): ApiResponseModel {
        val response = ApiResponseModel()
        val optional = this.iwaiversApplicationRepo.findById(waiverId)
        if (optional.isPresent) {
            val data = mutableMapOf<String, Any>()
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            data["pvocOfficer"] = loggedInUser.userName!!
            data["reviewOfficer"] = loggedInUser.userName!!
            data["reviewStatus"] = action
            data["remarks"] = remarks ?: "NO REMARKS"
            this.pvocBpmn.pvocCompleteTask(taskId, data)
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Task completed"
        } else {
            response.responseCode = ResponseCodes.NOT_FOUND
            response.message = "Invalid waiver identifier"
        }
        return response
    }

    fun kimsWaiverApplications(waiverStatus: String, keywords: String?, page: PageRequest): ApiResponseModel {
        val response = ApiResponseModel()
        val requests: Page<PvocWaiversApplicationEntity>?
        if (StringUtils.hasLength(keywords)) {
            requests = this.iwaiversApplicationRepo.findAllBySerialNoContains(keywords!!, page)
        } else {
            requests = when (waiverStatus) {
//                "ASSIGNED" -> this.iwaiversApplicationRepo.
                "NEW" -> this.iwaiversApplicationRepo.findAllByReviewStatusOrderByCreatedOnDesc("NEW", page)
                "DEFFERED" -> this.iwaiversApplicationRepo.findAllByReviewStatusOrderByCreatedOnDesc("DEFFERED", page)
                "REVIEW_REJECTED" -> this.iwaiversApplicationRepo.findAllByReviewStatusOrderByCreatedOnDesc("REVIEW_REJECTED", page)
                "REVIEW_APPROVED" -> this.iwaiversApplicationRepo.findAllByReviewStatusOrderByCreatedOnDesc("REVIEW_APPROVED", page)
                "NSC_APPROVED" -> this.iwaiversApplicationRepo.findAllByNscApprovalStatusOrderByCreatedOnDesc("APPROVED", page)
                "NSC_REJECTED" -> this.iwaiversApplicationRepo.findAllByNscApprovalStatusOrderByCreatedOnDesc("REJECTED", page)
                "CS_APPROVED" -> this.iwaiversApplicationRepo.findAllByCsApprovalStatusOrderByCreatedOnDesc("APPROVED", page)
                "CS_REJECTED" -> this.iwaiversApplicationRepo.findAllByCsApprovalStatusOrderByCreatedOnDesc("REJECTED", page)
                else -> null
            }
        }
        if (requests != null) {
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Success"
            response.data = PvocWaiverDao.fromList(requests.toList())
            response.totalPages = requests.totalPages
            response.totalCount = requests.totalElements
        } else {
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Invalid waiver application status"
        }
        return response
    }

    fun updateWaiverReviewStatus(requestId: Long, remarks: String, reviewOfficer: String, reviewStatus: String) {
        val optional = this.iwaiversApplicationRepo.findById(requestId)
        if (optional.isPresent) {
            val request = optional.get()
            request.reviewStatus = reviewStatus
            request.varField2 = remarks
            request.modifiedBy = reviewOfficer
            request.modifiedOn = Timestamp.from(Instant.now())
            this.iwaiversApplicationRepo.save(request)
        }
    }

    fun updateWaiverNscStatus(requestId: Long, remarks: String, reviewOfficer: String, approvalStatus: String) {
        val optional = this.iwaiversApplicationRepo.findById(requestId)
        if (optional.isPresent) {
            val request = optional.get()
            when (approvalStatus) {
                "APPROVED" -> {
                    request.nscApprovalStatus = "NSC_APPROVED"
                    request.rejectionStatus = 0
                }
                "REJECTED" -> {
                    request.rejectionStatus = 1
                    request.nscApprovalStatus = "NSC_REJECTED"
                }
            }
            request.varField2 = remarks
            request.modifiedBy = reviewOfficer
            request.modifiedOn = Timestamp.from(Instant.now())
            this.iwaiversApplicationRepo.save(request)
        }
    }

    fun updateWaiverCsStatus(requestId: Long, remarks: String, reviewOfficer: String, approvalStatus: String) {
        val optional = this.iwaiversApplicationRepo.findById(requestId)
        if (optional.isPresent) {
            val request = optional.get()
            request.csResponceMessage = remarks
            when (approvalStatus) {
                "APPROVED" -> {
                    request.csApprovalStatus = approvalStatus
                }
                "REJECTED" -> {
                    request.rejectionStatus = 1
                    request.nscApprovalStatus = approvalStatus
                }
            }
            request.varField2 = remarks
            request.modifiedBy = reviewOfficer
            request.modifiedOn = Timestamp.from(Instant.now())
            this.iwaiversApplicationRepo.save(request)
        }
    }

    fun sendWaiverRequestToCs(requestId: Long, reviewOfficer: String) {

    }

    fun sendWaiverApprovalLetter(requestId: Long, approvalStatus: String) {

    }

    fun exemptionApplicationHistory(page: Int, size: Int): ApiResponseModel {
        val response = ApiResponseModel()
        val userDetails = commonDaoServices.getLoggedInUser()
        userDetails?.email?.let { email ->
            val requests = this.iPvocApplicationRepo.findAllByCreatedByOrderByCreatedOnDesc(email, PageRequest.of(page, size))
            response.data = requests?.toList()
            response.message = "Success"
            response.totalPages = requests?.totalPages
            response.responseCode = ResponseCodes.SUCCESS_CODE
        } ?: run {
            response.message = "Could not get username"
            response.responseCode = ResponseCodes.FAILED_CODE
        }
        return response
    }

    fun manufacturerExemptionApplicationHistory(status: String, page: Int, size: Int): ApiResponseModel {
        val response = ApiResponseModel()
        val userDetails = commonDaoServices.getLoggedInUser()
        userDetails?.companyId?.let { companyId ->
            val requests = when (status.toLowerCase()) {
                "new" -> this.iPvocApplicationRepo.findAllByCompanyIdAndStatusIn(companyId, listOf(PvocExemptionStatus.NEW_APPLICATIONS.code), PageRequest.of(page, size))
                "approved" -> this.iPvocApplicationRepo.findAllByCompanyIdAndStatusIn(companyId, listOf(PvocExemptionStatus.PVOC_APPROVED.code, PvocExemptionStatus.CERT_APPROVED.code), PageRequest.of(page, size))
                "rejected" -> this.iPvocApplicationRepo.findAllByCompanyIdAndStatusIn(companyId, listOf(PvocExemptionStatus.PVOC_REJECTED.code, PvocExemptionStatus.CERT_REJECTED.code), PageRequest.of(page, size))
                "others" -> this.iPvocApplicationRepo.findAllByCompanyIdAndStatusIn(companyId, listOf(PvocExemptionStatus.DEFERRED.code), PageRequest.of(page, size))
                else -> throw ExpectedDataNotFound("Invalid application status: $status")
            }
            response.data = requests.toList()
            response.message = "Success"
            response.totalPages = requests.totalPages
            response.totalCount = requests.totalElements
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response
        } ?: run {
            response.message = "Could not get username"
            response.responseCode = ResponseCodes.FAILED_CODE
            response
        }
        return response
    }

    fun addWaiverRemarks(action: String, remarks: String, waiverId: Long, user: String) {
        commonDaoServices.findUserByUserName(user)?.let { u ->
            val waiverRemark = PvocWaiversRemarksEntity()
            waiverRemark.remarks = remarks
            waiverRemark.waiverAction = action
            waiverRemark.waiverId = waiverId
            waiverRemark.firstName = u.firstName
            waiverRemark.lastName = u.lastName
            waiverRemark.createdBy = u.userName
            this.waiversRemarksRepo.save(waiverRemark)
        }
    }

    @Transactional
    fun applyOrRenewWaiver(waiver: WaiverApplication, documents: List<MultipartFile>?): ApiResponseModel {
        val response = ApiResponseModel()
        val userDetails = commonDaoServices.loggedInUserDetails()
        userDetails.companyId?.let {
            val randomNumber = waiverSerialNumber()
            val waiverApp = PvocWaiversApplicationEntity()
            waiverApp.category = waiver.category
            waiverApp.address = waiver.postalAddress
            waiverApp.applicantName = waiver.applicantName
            waiverApp.productDescription = waiver.productDescription
            waiverApp.approvalStatus = 0
            waiverApp.companyId = userDetails.companyId
            waiverApp.emailAddress = waiver.emailAddress
            waiverApp.phoneNumber = waiver.telephoneNumber
            waiverApp.justification = waiver.justification
            waiverApp.reviewStatus = PvocExemptionStatus.NEW_APPLICATIONS.status
            waiverApp.kraPin = waiver.kraPin
            waiverApp.varField1 = waiver.contactPersonName
            waiverApp.varField2 = waiver.contactPersonPhone
            waiverApp.varField3 = waiver.contactPersonEmail
            waiverApp.createdBy = userDetails.userName
            waiverApp.createdOn = Timestamp.from(Instant.now())
            waiverApp.status = PvocExemptionStatus.NEW_APPLICATIONS.code
            waiverApp.serialNo = randomNumber
            iwaiversApplicationRepo.save(waiverApp)
                    .let { w ->
                        waiver.products?.forEach({ product ->
                            val waiverProduct = PvocMasterListEntity()
                            waiverProduct.hsCode = product.hsCode
                            waiverProduct.waiversApplicationId = waiverApp.id
                            waiverProduct.unit = product.productUnit.toString()
                            waiverProduct.quantity = product.quantity.toString()
                            waiverProduct.totalAmount = (product.productUnit * product.quantity).toString()
                            waiverProduct.origin = product.countryOfOrigin
                            waiverProduct.productDescription = product.productDescription
                            waiverProduct.serialNo = waiverProduct.serialNo
                            waiverProduct.currency = product.currency
                            waiverProduct.createdBy = userDetails.userName
                            waiverProduct.createdOn = waiverApp.createdOn
                            waiverProduct.status = 1
                            this.iPvocMasterListRepo.save(waiverProduct)
                        })
                        response.data = waiverApp
                        response.responseCode = ResponseCodes.SUCCESS_CODE
                        response.message = "Waiver application have been received"
                        // Start BPM process
                        userDetails.userName?.let { it1 ->
                            this.pvocBpmn.startPvocWaiversApplicationsProcess(w, it1)
                        }
                        this.addUploads(commonDaoServices.loggedInUserAuthentication(), w.serialNo.orEmpty(), DocumentTypes.WAIVER, documents)
                        iwaiversApplicationRepo.save(w)
                    }
        } ?: run {
            response.message = "Invalid account setup, user is not a company"
            response.responseCode = ResponseCodes.FAILED_CODE
        }
        return response
    }

    fun pvocAttachment(uploadId: String, docType: DocumentTypes): List<PvocApplicationDocumentsEntity>? {
        return this.iPvocApplicationDocumentRepo.findFirstByRefIdAndRefType(uploadId, docType.name)
    }

    fun updateReviewRequest(requestId: Long, validity: String, remarks: String, reviewOfficer: String, reviewStatus: String) {
        val optional = this.iPvocApplicationRepo.findById(requestId)
        if (optional.isPresent) {
            val request = optional.get()
            request.certificateValidity = validity
            request.reviewStatus = reviewStatus
            request.varField2 = remarks
            request.modifiedBy = reviewOfficer
            request.modifiedOn = Timestamp.from(Instant.now())
            this.iPvocApplicationRepo.save(request)
        }
    }

    fun updateCommitteeStatus(requestId: Long, remarks: String, reviewOfficer: String, approvalStatus: String) {
        val optional = this.iPvocApplicationRepo.findById(requestId)
        if (optional.isPresent) {
            val request = optional.get()
            when (approvalStatus) {
                "APPROVED" -> {
                    request.finalApproval = 1
                    request.finished = 1
                }
                "REJECTED" -> {
                    request.finalApproval = 2
                    request.finished = 1
                }
            }
            request.varField2 = remarks
            request.modifiedBy = reviewOfficer
            request.modifiedOn = Timestamp.from(Instant.now())
            this.iPvocApplicationRepo.save(request)
        }
    }

    fun approveDeferRejectExemption(requestId: Long, certValidity: String?, taskStatus: String, taskId: String, remarks: String): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val data = mutableMapOf<String, Any>()
            data["remarks"] = remarks
            data["approve"] = taskStatus
            val auth = commonDaoServices.loggedInUserAuthentication()
            // Section officer shall suggest certificate validity
            when {
                auth.authorities.stream().anyMatch { authority -> authority.authority == "PVOC_SECTION_OFFICER" } -> {
                    data["validity"] = certValidity ?: "3_MONTHS"
                }
            }

            this.pvocBpmn.pvocCompleteTask(taskId, data)
            response.message = "Success"
            response.responseCode = ResponseCodes.SUCCESS_CODE
        } catch (ex: Exception) {
            response.message = "Task approval failed"
            response.responseCode = ResponseCodes.FAILED_CODE
        }
        return response
    }

    fun generateExemptionCertificate(requestId: Long, reviewOfficer: String) {
        val optional = this.iPvocApplicationRepo.findById(requestId)
        if (optional.isPresent) {
            val request = optional.get()
            val exemption = PvocExceptionCertificate()
            exemption.applicationId = requestId
            exemption.createdBy = reviewOfficer
            exemption.modifiedBy = reviewOfficer
            exemption.certificateRevoked = false
            exemption.certificateVersion = 1
            exemption.certificateNumber = "COE".format("%0x05%0x03", requestId, exemption.certificateVersion)
            val issueDate = LocalDateTime.now()
            exemption.createdOn = Timestamp.valueOf(issueDate)
            exemption.expiresOn = Timestamp.valueOf(issueDate.plusYears(1))
            request.modifiedOn = Timestamp.from(Instant.now())
            val g = this.ipvocExemptionCertificateRepository.save(exemption)
            request.varField2 = g.certificateNumber
            this.iPvocApplicationRepo.save(request)
        }
    }

    fun completeExemptionApplication(requestId: Long) {
        val optional = this.iPvocApplicationRepo.findById(requestId)
        if (optional.isPresent) {
            val request = optional.get()
            request.finished = 1
            when (request.reviewStatus) {
                "APPROVED" -> {
                    // Send exemption certificate email
                }
                "REJECTED" -> {
                    // Send rejection email
                }
                else -> {

                }
            }
            iPvocApplicationRepo.save(request)
        }
    }

    fun manufacturerWaiverApplicationHistory(status: String, page: Int, size: Int): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val userDetails = commonDaoServices.getLoggedInUser()
            userDetails?.companyId?.let { company ->
                val requests = when (status.toLowerCase()) {
                    "new" -> this.iwaiversApplicationRepo.findByStatusInAndCompanyId(listOf(PvocExemptionStatus.NEW_APPLICATIONS.code), company, PageRequest.of(page, size))
                    "approved" -> this.iwaiversApplicationRepo.findByStatusInAndCompanyId(listOf(PvocExemptionStatus.CERT_APPROVED.code, PvocExemptionStatus.PVOC_APPROVED.code), company, PageRequest.of(page, size))
                    "rejected" -> this.iwaiversApplicationRepo.findByStatusInAndCompanyId(listOf(PvocExemptionStatus.CERT_REJECTED.code, PvocExemptionStatus.PVOC_REJECTED.code), company, PageRequest.of(page, size))
                    "others" -> this.iwaiversApplicationRepo.findByStatusInAndCompanyId(listOf(PvocExemptionStatus.DEFERRED.code), company, PageRequest.of(page, size))
                    else -> throw ExpectedDataNotFound("Invalid application status: $status")
                }
                response.data = requests.toList()
                response.message = "Success"
                response.totalPages = requests.totalPages
                response.totalCount = requests.totalElements
                response.responseCode = ResponseCodes.SUCCESS_CODE
            } ?: run {
                response.message = "Could not get username"
                response.responseCode = ResponseCodes.FAILED_CODE
            }
        } catch (ex: ExpectedDataNotFound) {
            response.message = ex.localizedMessage
            response.responseCode = ResponseCodes.FAILED_CODE
        } catch (ex: Exception) {
            response.message = "Could not process request"
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
        }
        return response
    }


    fun listOrSearchApplicationExceptions(status: String, keywords: String?, page: PageRequest): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val pg = when (status) {
                "NEW" -> this.iPvocApplicationRepo.findAllByReviewStatus(PvocExemptionStatus.NEW_APPLICATIONS.status, page)
                "APPROVED" -> this.iPvocApplicationRepo.findAllByReviewStatus(PvocExemptionStatus.PVOC_APPROVED.status, page)
                "REJECTED" -> this.iPvocApplicationRepo.findAllByReviewStatus(PvocExemptionStatus.PVOC_REJECTED.status, page)
                "DEFERRED" -> this.iPvocApplicationRepo.findAllByReviewStatus(PvocExemptionStatus.DEFERRED.status, page)
                "CERT_REJECTED" -> this.iPvocApplicationRepo.findAllByReviewStatus(PvocExemptionStatus.CERT_REJECTED.status, page)
                "CERT_APPROVED" -> this.iPvocApplicationRepo.findAllByReviewStatus(PvocExemptionStatus.CERT_APPROVED.status, page)
                else -> {
                    response.responseCode = ResponseCodes.FAILED_CODE
                    response.message = "Invalid request status: $status"
                    null
                }
            }
            if (pg != null) {
                response.data = PvocApplicationDto.fromList(pg.toList())
                response.message = "Exemption application"
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.totalPages = pg.totalPages
                response.totalCount = pg.totalElements
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to get exemption requests", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Request not found"
        }
        return response
    }

    fun waiverSerialNumber(): String {
        val yearIssued = commonDaoServices.convertDateToString(LocalDateTime.now(), "yyyyMMdd");
        val issuedThisYear = iwaiversApplicationRepo.countBySerialNoStartsWith(yearIssued)
        return String.format("%s%05d", yearIssued, issuedThisYear + 1)
    }

    fun exemptionSerialNumber(): String {
        val yearIssued = commonDaoServices.convertDateToString(LocalDateTime.now(), "yyyyMMdd");
        val issuedThisYear = iPvocApplicationRepo.countBySnStartsWith(yearIssued)
        return String.format("%s%05d", yearIssued, issuedThisYear + 1)
    }

    /**
     * Save application exemption.
     */
    @Transactional
    fun saveApplicationExemption(exemptionPayload: ExceptionPayload, documents: List<MultipartFile>?): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val manufacturer = exemptionPayload.manufacturer
            val products = exemptionPayload.products
            val rawMaterials = exemptionPayload.rawMaterials
            val spares = exemptionPayload.spares
            val mainMachinaries = exemptionPayload.mainMachinary
            val userDetails = commonDaoServices.getLoggedInUser()
            userDetails?.companyId?.let { company ->

                var pvocExceptionApp = PvocApplicationEntity()
                pvocExceptionApp.companyPinNo = manufacturer?.companyPinNo
                pvocExceptionApp.email = manufacturer?.email
                pvocExceptionApp.companyId = company
                pvocExceptionApp.sn = this.exemptionSerialNumber()
                pvocExceptionApp.status = PvocExemptionStatus.NEW_APPLICATIONS.code
                pvocExceptionApp.reviewStatus = PvocExemptionStatus.NEW_APPLICATIONS.status
                pvocExceptionApp.telephoneNo = manufacturer?.telephoneNo
                pvocExceptionApp.conpanyName = manufacturer?.companyName
                pvocExceptionApp.postalAadress = manufacturer?.postalAddress
                pvocExceptionApp.physicalLocation = manufacturer?.physicalLocation
                pvocExceptionApp.contactPersorn = manufacturer?.contactPersonName
                pvocExceptionApp.contactEmail = manufacturer?.contactPersonEmail
                pvocExceptionApp.contactName = manufacturer?.contactPersonPhone
                with(pvocExceptionApp) {
                    modifiedBy = userDetails.userName
                    createdBy = userDetails.email
                    termsConditions = 1
                    pvocWaStatus = 0
                    pvocEaStatus = 0
                    createdOn = Timestamp.from(Instant.now())
                    modifiedOn = Timestamp.from(Instant.now())
                    finalApproval = PvocExemptionStatus.NEW_APPLICATIONS.code
                    applicationDate = Date.from(Instant.now())
                    finished = 0
                }
                pvocExceptionApp = iPvocApplicationRepo.save(pvocExceptionApp)
                response.data = exemptionPayload
                response.message = "Success"
                response.responseCode = ResponseCodes.SUCCESS_CODE

                products?.forEach { product ->
                    val pvocApplicationProductsEntity = PvocApplicationProductsEntity()
                    with(pvocApplicationProductsEntity) {
                        productName = product.productName
                        brand = product.brandName
                        pvocApplicationId = pvocExceptionApp
                        expirelyDate = product.expriryDate.toString()
                        kebsStandardizationMarkPermit = product.permitNumber
                        createdBy = pvocExceptionApp.createdBy
                        createdOn = pvocExceptionApp.createdOn
                        iPvocApplicationProductsRepo.save(pvocApplicationProductsEntity)
                        KotlinLogging.logger { }.info { "Main App save OK" }
                    }
                }

                rawMaterials?.forEach { rawMat ->
                    val pvocRawMaterialCategory = PvocExceptionRawMaterialCategoryEntity()
                    with(pvocRawMaterialCategory) {
                        hsCode = rawMat.hsCode
                        rawMaterialDescription = rawMat.rawMaterialDescription
                        endProduct = rawMat.endProduct
                        countryOfOrgin = rawMat.countryOfOrigin
                        exceptionId = pvocExceptionApp.id
                        createdBy = pvocExceptionApp.createdBy
                        createdOn = pvocExceptionApp.createdOn
                        iPvocExceptionRawMaterialCategoryEntityRepo.save(pvocRawMaterialCategory)
                        KotlinLogging.logger { }.info { "Raw save OK" }
                    }
                }

                mainMachinaries?.forEach { machinary ->
                    val machine = PvocExceptionMainMachineryCategoryEntity()
                    with(machine) {
                        hsCode = machinary.hsCode
                        machineDescription = machinary.machineDescription
                        countryOfOrigin = machinary.countryOfOrigin
                        makeModel = machinary.makeModel
                        exceptionId = pvocExceptionApp.id
                        createdBy = pvocExceptionApp.createdBy
                        createdOn = pvocExceptionApp.createdOn
                        iPvocExceptionMainMachineryCategoryEntityRepo.save(machine)
                        KotlinLogging.logger { }.info { "Machine save OK" }
                    }
                }

                spares?.forEach { spare ->
                    val spareEntity = PvocExceptionIndustrialSparesCategoryEntity()
                    with(spareEntity) {
                        hsCode = spare.hsCode
                        machineToFit = spare.machineToFit
                        countryOfOrigin = spare.countryOfOrigin
                        industrialSpares = spare.industrialSpares
                        exceptionId = pvocExceptionApp.id
                        createdBy = pvocExceptionApp.createdBy
                        createdOn = pvocExceptionApp.createdOn
                        iPvocExceptionIndustrialSparesCategoryEntityRepo.save(spareEntity)
                        KotlinLogging.logger { }.info { "Spare save OK" }
                    }
                }
                this.addUploads(commonDaoServices.loggedInUserAuthentication(), pvocExceptionApp.sn.orEmpty(), DocumentTypes.EXEMPTION, documents)
                // Exemption application BPM process
                pvocBpmn.startPvocApplicationExemptionsProcess(pvocExceptionApp)
                // Save the update
                this.iPvocApplicationRepo.save(pvocExceptionApp)
            } ?: run {
                response.message = "User is not a company"
                response.responseCode = ResponseCodes.FAILED_CODE
            }
        } catch (ex: ExpectedDataNotFound) {
            response.message = ex.localizedMessage
            response.responseCode = ResponseCodes.FAILED_CODE
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to add exemption request: ", ex)
            response.message = "Failed to process request"
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
        }
        return response

    }

    fun removeExemptionItem(requestId: Long, itemId: Long, itemType: String): ApiResponseModel {
        val response = ApiResponseModel()
        when (itemType) {
            "SPARE" -> {
                val optional = iPvocExceptionIndustrialSparesCategoryEntityRepo.findAllByIdAndExceptionId(itemId, requestId)
                if (optional.isPresent) {
                    val spare = optional.get()
                    iPvocExceptionIndustrialSparesCategoryEntityRepo.delete(spare)
                    response.data = spare
                    response.message = "Deleted"
                    response.responseCode = ResponseCodes.SUCCESS_CODE
                } else {
                    response.message = "Record not found"
                    response.responseCode = ResponseCodes.NOT_FOUND
                }
            }
            "MACHINERY" -> {
                val optional = iPvocExceptionMainMachineryCategoryEntityRepo.findAllByIdAndExceptionId(itemId, requestId)
                if (optional.isPresent) {
                    val machine = optional.get()
                    iPvocExceptionMainMachineryCategoryEntityRepo.delete(machine)
                    response.data = machine
                    response.message = "Deleted"
                    response.responseCode = ResponseCodes.SUCCESS_CODE
                } else {
                    response.message = "Record not found"
                    response.responseCode = ResponseCodes.NOT_FOUND
                }
            }
            "RAW_MATERIAL" -> {
                val optional = iPvocExceptionRawMaterialCategoryEntityRepo.findAllByIdAndExceptionId(itemId, requestId)
                if (optional.isPresent) {
                    val rawMaterial = optional.get()
                    iPvocExceptionRawMaterialCategoryEntityRepo.delete(rawMaterial)
                    response.data = rawMaterial
                    response.message = "Deleted"
                    response.responseCode = ResponseCodes.SUCCESS_CODE
                } else {
                    response.message = "Record not found"
                    response.responseCode = ResponseCodes.NOT_FOUND
                }
            }
            "PRODUCT" -> {
                val optional = iPvocApplicationProductsRepo.findAllByIdAndPvocApplicationId_Id(itemId, requestId)
                if (optional.isPresent) {
                    val product = optional.get()
                    iPvocApplicationProductsRepo.delete(product)
                    response.data = product
                    response.message = "Deleted"
                    response.responseCode = ResponseCodes.SUCCESS_CODE
                } else {
                    response.message = "Record not found"
                    response.responseCode = ResponseCodes.NOT_FOUND
                }
            }
            else -> {
                response.message = "Invalid product type"
                response.responseCode = ResponseCodes.NOT_FOUND
            }
        }
        return response
    }

    fun retrieveWaiverById(id: Long): ApiResponseModel {
        val response = ApiResponseModel()
        val data = HashMap<String, Any?>()
        iwaiversApplicationRepo.findByIdOrNull(id)?.let { waiver ->
            try {
                val auth = commonDaoServices.loggedInUserAuthentication()
                val loggedInUser = commonDaoServices.getLoggedInUser()
                when {
                    auth.authorities.stream().anyMatch { authority -> authority.authority == "PVOC_WETC_MEMBER" } -> {
                        data.put("is_pvoc_officer", true)
                        // Auto Assign
                        if (waiver.wetcMember == null) {
                            waiver.wetcMember = loggedInUser?.id
                            iwaiversApplicationRepo.save(waiver)
                        }
                        data.put("is_owner", loggedInUser?.id == waiver.wetcMember)
                        data.put("tasks", this.pvocBpmn.getWaiverTasks("PVOC_OFFICER", waiver.id))
                    }
                    auth.authorities.stream().anyMatch { authority -> authority.authority == "PVOC_NAC_OFFICER" } -> {
                        data.put("is_nac_officer", true)
                        // Auto Assign
                        if (waiver.wetcSecretary == null) {
                            waiver.wetcSecretary = loggedInUser?.id
                            iwaiversApplicationRepo.save(waiver)
                        }
                        data.put("is_owner", loggedInUser?.id == waiver.wetcSecretary)
                        data.put("tasks", this.pvocBpmn.getWaiverTasks("NAC_OFFICER", waiver.id))
                    }
                    auth.authorities.stream().anyMatch { authority -> authority.authority == "PVOC_CS_OFFICER" } -> {
                        data.put("is_cs_officer", true)
                        // Auto Assign
                        if (waiver.wetcChairman == null) {
                            waiver.wetcChairman = loggedInUser?.id
                            iwaiversApplicationRepo.save(waiver)
                        }
                        data.put("is_owner", loggedInUser?.id == waiver.wetcChairman)
                        data.put("tasks", this.pvocBpmn.getWaiverTasks("CS_OFFICER", waiver.id))
                    }
                }
            } catch (ex: Exception) {
                KotlinLogging.logger { }.error("Failed to add data", ex)
            }

            data.put("products", PvocWaiverProductDao.fromList(this.iPvocMasterListRepo.findAllByWaiversApplicationId(waiver.id)))
            data.put("attachments", PvocWaiverAttachmentDao.fromList(iPvocApplicationDocumentRepo.findFirstByRefIdAndRefType(waiver.serialNo.orEmpty(), DocumentTypes.WAIVER.name)))
            data.put("waiver_details", PvocWaiverDao.fromEntity(waiver))
            data.put("waiver_history", PvocWaiverRemarksDao.fromList(this.waiversRemarksRepo.findAllByWaiverId(waiver.id)))

            response.data = data
            response.message = "Success"
            response.responseCode = ResponseCodes.SUCCESS_CODE
        } ?: run {
            response.message = "Waiver with $id does not exist"
            response.responseCode = ResponseCodes.NOT_FOUND
        }
        return response
    }

    fun listWaiverCategories(): ApiResponseModel {
        val response = ApiResponseModel()
        response.data = this.iPvocWaiversCategoriesRepo.findAllByStatus(1)
        response.responseCode = ResponseCodes.SUCCESS_CODE
        response.message = "Success"
        return response
    }

    fun retrieveMyExemptionApplicationById(id: Long): ApiResponseModel {
        val response = ApiResponseModel()
        val data = HashMap<String, Any?>()
        SecurityContextHolder.getContext().authentication.name.let { username ->
            iPvocApplicationRepo.findFirstByCreatedByAndId(username, id)?.let { exemption ->
                data.put("exemption", PvocApplicationDto.fromEntity(exemption))
                data.put("products", this.iPvocApplicationProductsRepo.findAllByPvocApplicationId_Id(exemption.id))
                data.put("rawMaterials", PvocExceptionRawMaterialDao.fromList(this.iPvocExceptionRawMaterialCategoryEntityRepo.findAllByExceptionId(exemption.id)))
                data.put("machinery", this.iPvocExceptionMainMachineryCategoryEntityRepo.findAllByExceptionId(exemption.id))
                data.put("spares", this.iPvocExceptionIndustrialSparesCategoryEntityRepo.findAllByExceptionId(exemption.id))
                response.data = data
                response.message = "Success"
                response.responseCode = ResponseCodes.SUCCESS_CODE
            } ?: run {
                response.message = "Exemption with $id does not exist"
                response.responseCode = ResponseCodes.NOT_FOUND
            }
        }
        return response
    }

    fun retrieveExemptionApplicationDetails(id: Long): ApiResponseModel {
        val response = ApiResponseModel()
        val data = HashMap<String, Any?>()
        val exemptionOptional = iPvocApplicationRepo.findById(id)
        if (exemptionOptional.isPresent) {
            val exemption = exemptionOptional.get()
            data.put("exemption", PvocApplicationDto.fromEntity(exemption))
            // Exemption details
            data.put("products", PvocApplicationProductDao.fromList(this.iPvocApplicationProductsRepo.findAllByPvocApplicationId_Id(exemption.id)))
            data.put("rawMaterials", PvocExceptionRawMaterialDao.fromList(this.iPvocExceptionRawMaterialCategoryEntityRepo.findAllByExceptionId(exemption.id)))
            data.put("machinery", PvocExceptionMainMachineryDao.fromList(this.iPvocExceptionMainMachineryCategoryEntityRepo.findAllByExceptionId(exemption.id)))
            data.put("spares", PvocExceptionIndustrialSparesDao.fromList(this.iPvocExceptionIndustrialSparesCategoryEntityRepo.findAllByExceptionId(exemption.id)))
            // Add Tasks based on roles
            try {
                val auth = commonDaoServices.loggedInUserAuthentication()
                when {
                    auth.authorities.stream().anyMatch { authority -> authority.authority == "PVOC_SECTION_OFFICER" } -> {
                        data.put("is_section_officer", true)
                        data.put("tasks", this.pvocBpmn.getExemptionTasks("SECTION_OFFICER", exemption.id!!))
                    }
                    auth.authorities.stream().anyMatch { authority -> authority.authority == "PVOC_EXEMPTION_CHAIRMAN" } -> {
                        data.put("is_section_officer", false)
                        data.put("tasks", this.pvocBpmn.getExemptionTasks("EXEMPTION_CHAIRMAN", exemption.id!!))
                    }
                }
            } catch (ex: Exception) {
                KotlinLogging.logger { }.error("Failed to add data", ex)
            }
            // Data
            response.data = data
            response.message = "Success"
            response.responseCode = ResponseCodes.SUCCESS_CODE
        } else {
            response.message = "Record not found"
            response.responseCode = ResponseCodes.NOT_FOUND
        }
        return response
    }

    fun findPvoceDocument(uploadId: String): PvocApplicationDocumentsEntity? {
        return iPvocApplicationDocumentRepo.findByIdOrNull(uploadId.toLong())
    }
}
