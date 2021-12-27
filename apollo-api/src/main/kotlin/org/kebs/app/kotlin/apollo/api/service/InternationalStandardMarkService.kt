package org.kebs.app.kotlin.apollo.api.service

import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.payload.request.InternationalStandardMarkForm
import org.kebs.app.kotlin.apollo.api.payload.response.ISMApplicationsDto
import org.kebs.app.kotlin.apollo.api.payload.response.ISMExternalApplicationsDto
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DestinationInspectionDaoServices
import org.kebs.app.kotlin.apollo.store.model.ism.IsmApplications
import org.kebs.app.kotlin.apollo.store.repo.ism.IsmApplicationsRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.Instant

@Service
class InternationalStandardMarkService(
        private val destinationInspectionDaoServices: DestinationInspectionDaoServices,
        private val ismApplicationsRepository: IsmApplicationsRepository
) {
    fun listApplications(status: Int, page: PageRequest): ApiResponseModel {
        val response = ApiResponseModel()
        val pg = ismApplicationsRepository.findByRequestApproved(status, page)
        response.data = ISMApplicationsDto.fromList(pg.toList())
        response.totalPages = pg.totalPages
        response.totalCount = pg.totalElements
        response.responseCode = ResponseCodes.SUCCESS_CODE
        return response
    }

    fun listApplicantApplications(emailAddress: String, page: PageRequest): ApiResponseModel {
        val response = ApiResponseModel()
        val pg = ismApplicationsRepository.findByEmailAddress(emailAddress, page)
        response.data = ISMExternalApplicationsDto.fromList(pg.toList())
        response.totalPages = pg.totalPages
        response.totalCount = pg.totalElements
        response.responseCode = ResponseCodes.SUCCESS_CODE
        return response
    }

    fun applyForISM(form: InternationalStandardMarkForm): ApiResponseModel {
        val response = ApiResponseModel()
        destinationInspectionDaoServices.findCdWithUcrNumber(form.ucrNumber!!)?.let { consignment ->
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

    fun startBpmProcess(ismApplication: IsmApplications) {
        // TODO: Start BPM process for approval
    }

    fun approveIsmRequest(ismRequestId: Long, remarks: String, pvocOfficer: String) {
        val ismRequest = this.ismApplicationsRepository.findById(ismRequestId)
        if (ismRequest.isPresent) {
            val request = ismRequest.get()
            request.requestApproved = 1
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
            request.completed = true
            request.completedOn = Timestamp.from(Instant.now())
            response.data = this.ismApplicationsRepository.save(request)
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
