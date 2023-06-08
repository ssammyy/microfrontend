package org.kebs.app.kotlin.apollo.api.service

import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.payload.request.InternationalStandardMarkForm
import org.kebs.app.kotlin.apollo.api.payload.response.ISMApplicationsDto
import org.kebs.app.kotlin.apollo.api.payload.response.ISMExternalApplicationsDto
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DestinationInspectionDaoServices
import org.kebs.app.kotlin.apollo.store.model.ism.IsmApplications
import org.kebs.app.kotlin.apollo.store.repo.ism.IsmApplicationsRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId


@Service("ismService")
class InternationalStandardMarkService(
        private val destinationInspectionDaoServices: DestinationInspectionDaoServices,
        private val ismApplicationsRepository: IsmApplicationsRepository,
        private val commonDaoServices: CommonDaoServices,
        private val runtimeService: RuntimeService,
        private val taskService: TaskService,
) {
    fun listApplications(status: Int, page: PageRequest): ApiResponseModel {
        val response = ApiResponseModel()
        val pg = ismApplicationsRepository.findByRequestApproved(status, page)
        response.data = ISMApplicationsDto.fromList(pg.toList())
        response.totalPages = pg.totalPages
        response.totalCount = pg.totalElements
        response.message = "Success"
        response.responseCode = ResponseCodes.SUCCESS_CODE
        return response
    }

    fun listssApplications(emailAddress: String, page: PageRequest): ApiResponseModel {
        val response = ApiResponseModel()
        val pg = ismApplicationsRepository.findByEmailAddress(emailAddress, page)
        response.data = ISMExternalApplicationsDto.fromList(pg.toList())
        response.message = "Success"
        response.totalPages = pg.totalPages
        response.totalCount = pg.totalElements
        response.responseCode = ResponseCodes.SUCCESS_CODE
        return response
    }

    fun applyForISM(form: InternationalStandardMarkForm): ApiResponseModel {
        val response = ApiResponseModel()
        destinationInspectionDaoServices.findCdWithUcrNumberLatest(form.ucrNumber!!)?.let { consignment ->
            this.destinationInspectionDaoServices.findCocByUcrNumber(consignment.ucrNumber!!)?.let { coc ->
                val ismApplication = IsmApplications()
                ismApplication.consignmentId = consignment.id
                ismApplication.cocId = coc.id
                ismApplication.companyName = form.companyName
                ismApplication.emailAddress = form.emailAddress
                ismApplication.firstName = form.firstName
                ismApplication.middleName = form.middleName
                ismApplication.lastName = form.lastName
                ismApplication.ucrNumber = form.ucrNumber
                ismApplication.requestApproved = 0
                ismApplication.status = 1
                ismApplication.remarks = form.remarks
                if (consignment.cdType?.localCocStatus == 1) {
                    val saved = ismApplicationsRepository.save(ismApplication)
                    startBpmProcess(saved)
                    response.responseCode = ResponseCodes.SUCCESS_CODE
                    response.message = "Request received"
                } else {
                    ismApplication.approvalRemarks = "Auto-Rejected for non-coc consignment"
                    ismApplication.requestApproved = -1
                    ismApplicationsRepository.save(ismApplication)
                    response.message = ismApplication.approvalRemarks ?: "Request auto-rejected"
                }
                response
            } ?: run {
                response.responseCode = ResponseCodes.NOT_FOUND
                response.message = "COC has not been issued for this ucr number"
                response
            }
        } ?: run {
            response.responseCode = ResponseCodes.NOT_FOUND
            response.message = "Invalid ucr number"
            response
        }
        return response
    }

    /**
     * Start BPM process for approval
     */
    fun startBpmProcess(ismApplication: IsmApplications) {
        val data = mutableMapOf<String, Any?>()
        data.put("ismRequestId", ismApplication.id)
        val processInstance = runtimeService.startProcessInstanceByKey("ISMApprovalProcess", data)
        ismApplication.processId = processInstance.processInstanceId
        ismApplication.varField1 = processInstance.processDefinitionId
        this.ismApplicationsRepository.save(ismApplication)

    }

    fun approveRejectIsm(ismRequestId: Long, remarks: String, approved: Boolean): ApiResponseModel {
        val response = ApiResponseModel()
        val ismRequest = this.ismApplicationsRepository.findById(ismRequestId)
        if (ismRequest.isPresent) {
            val ismApplication = ismRequest.get()
            if (ismApplication.completed == false) {
                val tasks = taskService.createTaskQuery().processInstanceId(ismApplication.processId).active().list()
                if (tasks.isEmpty()) {
                    response.responseCode = ResponseCodes.FAILED_CODE
                    response.message = "Request task could not be found"
                } else {
                    val data = mutableMapOf<String, Any?>()
                    data.put("remarks", remarks)
                    data.put("approved", approved)
                    data.put("pvocOfficer", commonDaoServices.loggedInUserAuthentication().name)
                    // Execute BPM task for approval or rejection
                    taskService.complete(tasks[0].id, data)
                    response.responseCode = ResponseCodes.SUCCESS_CODE
                    response.message = "Approval request received"
                }
            } else {
                response.responseCode = ResponseCodes.FAILED_CODE
                response.message = "Request already completed"
            }
        } else {
            response.responseCode = ResponseCodes.NOT_FOUND
            response.message = "Request not found"
        }
        return response
    }

    fun countIsmIssuedToday(): Long {
        val localDateTime: LocalDateTime = LocalDateTime.now()
        val startOfDay: LocalDateTime = localDateTime.with(LocalTime.MIN)
        val endOfDay: LocalDateTime = localDateTime.with(LocalTime.MAX)
        return ismApplicationsRepository.countByRequestApprovedAndApprovedRejectedOnBetween(1, Timestamp.valueOf(startOfDay), Timestamp.valueOf(endOfDay))
    }

    fun approveIsmRequest(ismRequestId: Long, remarks: String, pvocOfficer: String) {
        val ismRequest = this.ismApplicationsRepository.findById(ismRequestId)
        if (ismRequest.isPresent) {
            val request = ismRequest.get()
            request.requestApproved = 1
            val todaysCount = countIsmIssuedToday()
            request.ismNumber = "ISM${this.commonDaoServices.convertDateToString(LocalDateTime.now(), "yyyyMMddmm")}${todaysCount}"
            request.approvalRemarks = remarks
            request.approvedBy = pvocOfficer
            request.approvedRejectedOn = Timestamp.from(Instant.now())
            this.ismApplicationsRepository.save(request)
        }
    }

    fun rejectIsmRequest(ismRequestId: Long, remarks: String, pvocOfficer: String) {
        val ismRequest = this.ismApplicationsRepository.findById(ismRequestId)
        if (ismRequest.isPresent) {
            val request = ismRequest.get()
            request.requestApproved = 2
            request.approvalRemarks = remarks
            request.approvedBy = pvocOfficer
            request.approvedRejectedOn = Timestamp.from(Instant.now())
            this.ismApplicationsRepository.save(request)
        }
    }

    fun sendRejectionEmail(ismRequestId: Long) {

    }

    fun sendIsmReport(ismRequestId: Long) {
        // TODO: send report
    }

    fun completeIsmRequest(ismRequestId: Long) {
        val ismRequest = this.ismApplicationsRepository.findById(ismRequestId)
        if (ismRequest.isPresent) {
            val request = ismRequest.get()
            request.completed = true
            request.completedOn = Timestamp.from(Instant.now())
            this.ismApplicationsRepository.save(request)
        }
    }

    fun getIsmApplication(requestId: Long): ApiResponseModel {
        val response = ApiResponseModel()
        val ismRequest = this.ismApplicationsRepository.findById(requestId)
        if (ismRequest.isPresent) {
            val request = ismRequest.get()
            val map = mutableMapOf<String, Any>()
            map["ism_request"] = ISMApplicationsDto.fromEntity(request)
            destinationInspectionDaoServices.findCocByUcrNumber(request.ucrNumber!!)?.let {
                map["certificate_details"] = it
                map["cd_uuid"] = it.consignmentDocId?.uuid ?: ""
            }
            response.data = map
            response.message = "Success"
            response.responseCode = ResponseCodes.SUCCESS_CODE
        } else {
            response.message = "ISM record not found"
            response.responseCode = ResponseCodes.NOT_FOUND
        }
        return response
    }

    fun getCustomerIsmApplications(emailAddress: String?, page: PageRequest): ApiResponseModel {
        val response = ApiResponseModel()
        val ismRequest = this.ismApplicationsRepository.findByEmailAddress(emailAddress, page)
        response.data = ISMExternalApplicationsDto.fromList(ismRequest.toList())
        response.pageNo = ismRequest.number
        response.totalCount = ismRequest.totalElements
        response.totalPages = ismRequest.totalPages
        response.message = "Success"
        response.responseCode = ResponseCodes.SUCCESS_CODE
        return response
    }

}
