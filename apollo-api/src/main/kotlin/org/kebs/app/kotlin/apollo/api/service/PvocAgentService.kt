package org.kebs.app.kotlin.apollo.api.service

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.notifications.NotificationCodes
import org.kebs.app.kotlin.apollo.api.notifications.NotificationService
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.payload.request.*
import org.kebs.app.kotlin.apollo.api.payload.response.PvocComplaintCategoryDao
import org.kebs.app.kotlin.apollo.api.payload.response.PvocComplaintDao
import org.kebs.app.kotlin.apollo.api.payload.response.PvocComplaintRecommendationDao
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.PvocBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DestinationInspectionDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.ForeignPvocIntegrations
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
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
        private val cocRepo: ICocsRepository,
        private val pvocBpmn: PvocBpmn,
        private val daoServices: DestinationInspectionDaoServices,
        private val notificationService: NotificationService,
        private val pvocIntegrations: ForeignPvocIntegrations,
        private val properties: ApplicationMapProperties
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
                // Start BPM process
                this.pvocBpmn.startPvocComplaintApplicationsProcess(saved)
                // Update process
                this.complaintEntityRepo.save(saved)
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

    fun approveCurrentComplaint(complaintId: Long, action: String, taskId: String, remarks: String): ApiResponseModel {
        val response = ApiResponseModel()
        val data = mutableMapOf<String, Any>()
        data["pvocOfficer"] = commonDaoServices.loggedInUserDetails().userName!!
        data["remarks"] = remarks ?: "NO REMARKS"
        this.pvocBpmn.pvocCompleteTask(taskId, data)
        response.responseCode = ResponseCodes.SUCCESS_CODE
        response.message = "Task completed"
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
            this.daoServices.findCocByCocNumber(complaint.cocNo ?: "")?.let { cocDetails ->
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
                        data.put("tasks", this.pvocBpmn.getComplaintTasks(listOf("REVIEW_RECOMMENDATION", "QUERY"), complaint.id!!))
                    }
                    auth.authorities.stream().anyMatch { authority -> authority.authority == "MPVOC_OFFICER" } -> {
                        data.put("is_mpvoc_officer", true)
                        if (complaint.mpvoc == null) {
                            complaint.mpvoc = activeUser
                            this.complaintEntityRepo.save(complaint)
                        }
                        data.put("is_owner", complaint.mpvoc?.id == activeUser?.id)
                        data.put("tasks", this.pvocBpmn.getComplaintTasks(listOf("MPVOC_APPROVAL"), complaint.id!!))
                    }
                    auth.authorities.stream().anyMatch { authority -> authority.authority == "HOD_OFFICER" } -> {
                        data.put("is_hod_officer", true)
                        if (complaint.hod == null) {
                            complaint.hod = activeUser
                            this.complaintEntityRepo.save(complaint)
                        }
                        data.put("is_owner", complaint.hod?.id == activeUser?.id)
                        data.put("tasks", this.pvocBpmn.getComplaintTasks(listOf("HOD_APPROVAL"), complaint.id!!))
                    }
                }
            } catch (ex: Exception) {
                KotlinLogging.logger { }.error("Failed to add data", ex)
            }
            // Add details
            data["assigned_officer"] = "${complaint.pvocUser?.firstName} ${complaint.pvocUser?.lastName}"
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

    fun pvocOfficerReviewStatus(complaintId: Long, remarks: String, action: String, pvocOfficer: String) {
        val optional = this.complaintEntityRepo.findById(complaintId)
        if (optional.isPresent) {
            val complaint = optional.get()
            complaint.reviewStatus = action
            complaint.recomendation = remarks
            complaint.reviewedOn = Timestamp.from(Instant.now())
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


    fun pvocComplaintProcessCompleted(complaintId: Long) {
    }


    fun receiveCoc(coc: CocEntityForm): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val actiVerUser = commonDaoServices.loggedInPartnerDetails()
            this.pvocIntegrations.foreignCoc(actiVerUser, coc, commonDaoServices.serviceMapDetails(properties.mapImportInspection))?.let { cocEntity ->
                this.daoServices.sendLocalCoc(cocEntity)
                response.data = coc
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "COC with ucr: " + coc.ucrNumber + " received"
                response
            } ?: run {
                response.responseCode = ResponseCodes.DUPLICATE_ENTRY_STATUS
                response.message = "COC with ucr: " + coc.ucrNumber + " already exists"
                response
            }
        } catch (ex: Exception) {
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Failed to add COC with ucr number" + coc.ucrNumber
            response.errors = ex.message
        }
        return response
    }

    fun receiveCor(cor: CorEntityForm): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val activerUser = commonDaoServices.loggedInPartnerDetails()
            this.pvocIntegrations.foreignCor(cor, commonDaoServices.serviceMapDetails(properties.mapImportInspection), activerUser)?.let { corEntity ->
                // Send COR to KWS
                this.daoServices.submitCoRToKesWS(corEntity)
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "COR with ucr: " + cor.ucrNumber + " received"
                response
            } ?: run {
                response.responseCode = ResponseCodes.DUPLICATE_ENTRY_STATUS
                response.message = "COR with ucr: " + cor.ucrNumber + " already exists"
                response
            }
        } catch (ex: Exception) {
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Failed to add COC with ucr number" + cor.ucrNumber
            response.errors = ex.message
        }
        return response
    }

    fun receiveCoi(form: CoiEntityForm): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val activerUser = commonDaoServices.loggedInPartnerDetails()
            this.pvocIntegrations.foreignCoi(activerUser, form, commonDaoServices.serviceMapDetails(properties.mapImportInspection))?.let { corEntity ->
                // Send Foreign COI to KEWS
                this.daoServices.sendLocalCoi(corEntity)
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "COI with ucr: " + form.ucrNumber + " received"
                response
            } ?: run {
                response.responseCode = ResponseCodes.DUPLICATE_ENTRY_STATUS
                response.message = "CORI with ucr: " + form.ucrNumber + " already exists"
                response
            }
        } catch (ex: Exception) {
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Failed to add COI with ucr number" + form.ucrNumber
            response.errors = ex.message
        }
        return response
    }

    fun receiveRfcCoi(form: RfcCoiEntityForm): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val activeUser = commonDaoServices.loggedInPartnerDetails()
            this.pvocIntegrations.foreignRfcCoi(form, commonDaoServices.serviceMapDetails(properties.mapImportInspection), activeUser)?.let { rfcEntity ->
                response.data = form
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "RFC with number: " + form.rfcNumber + " received"
                response
            } ?: run {
                response.responseCode = ResponseCodes.DUPLICATE_ENTRY_STATUS
                response.message = "RFC with ucr: " + form.ucrNumber + " already exists"
                response
            }
        } catch (ex: Exception) {
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Failed to add COI with ucr number" + form.ucrNumber
            response.errors = ex.message
        }
        return response
    }

    fun receiveNcr(ncr: CocEntityForm): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val activerUser = commonDaoServices.loggedInPartnerDetails()
            this.pvocIntegrations.foreignCoc(activerUser, ncr, commonDaoServices.serviceMapDetails(properties.mapImportInspection))?.let { ncrEntity ->
                // Update document Type
                ncrEntity.cocType = "NCR"
                cocRepo.save(ncrEntity)
                // Create response
                response.data = ncr
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "NCR with ucr: " + ncr.ucrNumber + " received"
                response
            } ?: run {
                response.responseCode = ResponseCodes.DUPLICATE_ENTRY_STATUS
                response.message = "NCR with ucr: " + ncr.ucrNumber + " already exists"
                response
            }
        } catch (ex: Exception) {
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Failed to add NCR with ucr number" + ncr.ucrNumber
            response.errors = ex.message
        }
        return response
    }

    fun addRiskProfile(form: RiskProfileForm): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val activeUser = commonDaoServices.loggedInPartnerDetails()
            this.pvocIntegrations.foreignRiskProfile(form, commonDaoServices.serviceMapDetails(properties.mapImportInspection), activeUser)?.let { riskEntity ->
                response.data = form
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "RISK for HS Code: " + form.hsCode + " received"
                response
            } ?: run {
                response.responseCode = ResponseCodes.DUPLICATE_ENTRY_STATUS
                response.message = "RISK for HS Code: " + form.hsCode + " already exists"
                response
            }
        } catch (ex: Exception) {
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Failed to add Risk HS code" + form.hsCode
            response.errors = ex.message
        }
        return response
    }


}