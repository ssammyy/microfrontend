package org.kebs.app.kotlin.apollo.api.service

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.notifications.NotificationCodes
import org.kebs.app.kotlin.apollo.api.notifications.NotificationService
import org.kebs.app.kotlin.apollo.api.notifications.NotificationTypeCodes
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.payload.request.*
import org.kebs.app.kotlin.apollo.api.payload.response.PvocComplaintCategoryDao
import org.kebs.app.kotlin.apollo.api.payload.response.PvocComplaintDao
import org.kebs.app.kotlin.apollo.api.payload.response.PvocComplaintRecommendationDao
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DestinationInspectionDaoServices
import org.kebs.app.kotlin.apollo.store.model.pvc.PvocComplaintEntity
import org.kebs.app.kotlin.apollo.store.model.pvc.PvocComplaintRemarksEntity
import org.kebs.app.kotlin.apollo.store.repo.*
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime

enum class ComplaintStatus {
    NEW, PVOC_APPROVED, PVOC_REJECTED
}

@Service("pvocAgentService")
class PvocAgentService(
        private val complaintEntityRepo: IPvocComplaintRepo,
        private val complaintCategoryRepo: IPvocComplaintCategoryRepo,
        private val complaintRecommendationRepo: PvocComplainRecommendationEntityRepo,
        private val complaintSubCategoryRepo: IPvocComplaintCertificationsSubCategoryRepo,
        private val pvocComplaintRemarksEntityRepo: PvocComplaintRemarksEntityRepo,
        private val commonDaoServices: CommonDaoServices,
        private val daoServices: DestinationInspectionDaoServices,
        private val notificationService: NotificationService
) {
    fun getComplaintCategories(): ApiResponseModel {
        val response = ApiResponseModel()
        val categories = this.complaintCategoryRepo.findAllByStatus(1)
        response.data = PvocComplaintCategoryDao.fromList(categories)
        response.responseCode = ResponseCodes.SUCCESS_CODE
        response.message = "Success"
        return response
    }

    fun getRecommendations(): ApiResponseModel {
        val response = ApiResponseModel()
        val categories = this.complaintRecommendationRepo.findByStatus(1)
        response.data = PvocComplaintRecommendationDao.fromList(categories)
        response.responseCode = ResponseCodes.SUCCESS_CODE
        response.message = "Success"
        return response
    }

    fun generateComplaintRef(prefix: String): String {
        val count = complaintEntityRepo.countAllByRefPrefix(prefix)
        return "%s%05x".format(prefix, count)
    }

    fun pvocFileComplaint(form: PvocComplaintForm, uploads: MultipartFile): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val prefix = commonDaoServices.convertDateToString(LocalDateTime.now(), "yyyyMMdd")
            val complaint = PvocComplaintEntity()
            complaint.complaintName = form.complaintName
            complaint.address = form.address
            complaint.phoneNo = form.phoneNo
            complaint.cocNo = form.cocNo
            complaint.rfcNo = form.rfcNo
            complaint.reviewStatus = ComplaintStatus.NEW.name
            complaint.refPrefix = prefix
            complaint.refNo = generateComplaintRef(prefix)
            complaint.email = form.email
            complaint.generalDescription = form.complaintDescription
            complaint.createdOn = Timestamp.from(Instant.now())
            complaint.createdBy = commonDaoServices.loggedInUserAuthentication().name
            val errors = mutableMapOf<String, String>()
            val category = complaintCategoryRepo.findById(form.categoryId!!)
            if (category.isPresent) {
                complaint.compliantNature = category.get()
            } else {
                errors["categoryId"] = "Please select category"
            }
            val subCategory = complaintSubCategoryRepo.findById(form.subCategoryId!!)
            if (subCategory.isPresent) {
                complaint.compliantSubCategory = subCategory.get()
            } else {
                errors["subCategoryId"] = "Please select sub category"
            }
            if (errors.isEmpty()) {
                val saved = this.complaintEntityRepo.save(complaint)
                this.addUploads(saved, uploads)
                response.data = saved.id
                response.message = "Request received"
                response.responseCode = ResponseCodes.SUCCESS_CODE
            } else {
                response.errors = errors
                response.responseCode = ResponseCodes.INVALID_CODE
                response.message = "Please make corrections and resubmit"
            }
        } catch (ex: Exception) {
            response.message = "Invalid request"
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
        }
        return response
    }

    fun sendComplaintEmail(complaintEntity: PvocComplaintEntity, emailType: String, recipient: String, remarks: String? = null) {
        val data = mutableMapOf<String, Any>()
        data["complaint"] = PvocComplaintDao.fromEntity(complaintEntity)
        data["referenceNumber"] = complaintEntity.refNo ?: "UNKNOWN"
        data["remarks"] = remarks ?: "NO REMARKS"
        this.notificationService.sendEmail(recipient, emailType, data)
    }

    fun addUploads(complaintEntity: PvocComplaintEntity, files: MultipartFile) {

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
            this.daoServices.findCocByCocNumber(complaint.cocNo ?: "")?.let { cocDetails ->
                data["cd_uuid"] = cocDetails.consignmentDocId?.uuid
                data["certificate_details"] = cocDetails
            }
            data["assigned_officer"] = "${complaint.pvocUser?.firstName} ${complaint.pvocUser?.lastName}"
            data["pvoc_officer"] = this.commonDaoServices.currentUserHasRole("PVOC_OFFICER")
            data["mpvoc_officer"] = this.commonDaoServices.currentUserHasRole("MPVOC_OFFICER")
            data["complaint"] = PvocComplaintDao.fromEntity(complaint)
            response.data = data
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Success"
        } else {
            response.message = "Record not found"
            response.responseCode = ResponseCodes.NOT_FOUND
        }
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
            val saved = this.pvocComplaintRemarksEntityRepo.save(rem)
            response.data = rem
            response.message = "Remarks added"
            response.responseCode = ResponseCodes.SUCCESS_CODE
        } else {
            response.message = "Complaint not found"
            response.responseCode = ResponseCodes.NOT_FOUND
        }
        return response
    }

    fun pvocOfficerReviewStatus(complaintId: Long, recommendationId: Long, remarks: String, agentRemarks: String, action: String, pvocOfficer: String) {
        val optional = this.complaintEntityRepo.findById(complaintId)
        if (optional.isPresent) {
            val complaint = optional.get()
            complaint.reviewStatus = action
            val recommOptional = this.complaintRecommendationRepo.findById(recommendationId)
            if (recommOptional.isPresent) {
                complaint.recomendation = recommOptional.get().description
            }
            complaint.agentReviewRemarks = agentRemarks
            complaint.reviewedOn = Timestamp.from(Instant.now())
            complaint.pvocUser = commonDaoServices.findUserByUserName(pvocOfficer)
            this.complaintEntityRepo.save(complaint)
            addComplaintRemarks(complaintId, remarks, "AGENT_REVIEW")
        }
    }

    fun mpvocReviewStatus(complaintId: Long, remarks: String, recommendations: String, action: String, pvocOfficer: String) {
        val optional = this.complaintEntityRepo.findById(complaintId)
        if (optional.isPresent) {
            val complaint = optional.get()
            complaint.mpvoc = commonDaoServices.findUserByUserName(pvocOfficer)
            complaint.mpvocRecomendationDate = Timestamp.from(Instant.now())
            this.complaintEntityRepo.save(complaint)
        }
    }

    fun hodReviewStatus(complaintId: Long, remarks: String, recommendations: String, action: String, pvocOfficer: String) {
        val optional = this.complaintEntityRepo.findById(complaintId)
        if (optional.isPresent) {
            val complaint = optional.get()
            complaint.hod = commonDaoServices.findUserByUserName(pvocOfficer)
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

    fun sendComplaintReviewEmail(complaintId: Long) {
        val optional = this.complaintEntityRepo.findById(complaintId)
        if (optional.isPresent) {
            val complaint = optional.get()
            // Send email
            this.sendComplaintEmail(complaint, complaint.reviewStatus ?: "UNKNOWN", complaint.email ?: "")
        }
    }

    fun pvocQueryPartner() {

    }

    fun receiveCoc(coc: CocEntityForm): ApiResponseModel {
        val response = ApiResponseModel()

        return response
    }

    fun receiveCor(coc: CorEntityForm): ApiResponseModel {
        val response = ApiResponseModel()

        return response
    }

    fun receiveCoi(form: CoiEntityForm): Any {
        TODO("Not yet implemented")
    }

    fun receiveRfcCoi(form: RfcCoiEntityForm): Any {
        TODO("Not yet implemented")
    }

    fun pvocPartnerRiskProfile(form: RiskProfileForm) {

    }


}