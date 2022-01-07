package org.kebs.app.kotlin.apollo.api.service

import org.kebs.app.kotlin.apollo.api.payload.ApiClientForm
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.payload.request.PvocPartnersForms
import org.kebs.app.kotlin.apollo.api.payload.response.PvocPartnerDto
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.store.model.pvc.PvocPartnersEntity
import org.kebs.app.kotlin.apollo.store.repo.IPvocPartnersRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import java.sql.Timestamp
import java.time.Instant

@Component
class PvocPartnerService(
        private val partnersRepository: IPvocPartnersRepository,
        private val commonDaoServices: CommonDaoServices,
        private val apiClientService: ApiClientService
) {

    fun addPartnerDetails(form: PvocPartnersForms): ApiResponseModel {
        val response = ApiResponseModel()
        if (partnersRepository.findByPartnerRefNo(form.partnerRefNo).isEmpty) {
            val partner = PvocPartnersEntity()
            // Fill with data
            form.addDetails(partner, false)
            partner.createdOn = Timestamp.from(Instant.now())
            partner.createdBy = this.commonDaoServices.getLoggedInUser()?.userName
            val saved = this.partnersRepository.save(partner)
            response.data = saved.id
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Partner added"
        } else {
            response.responseCode = ResponseCodes.DUPLICATE_ENTRY_STATUS
            response.message = "Partner with given reference number already exists"
        }
        return response
    }

    fun updatePartnerDetails(form: PvocPartnersForms, partnerId: Long): ApiResponseModel {
        val response = ApiResponseModel()
        val partnerOptional = partnersRepository.findById(partnerId)
        if (partnerOptional.isPresent) {
            val partner = partnerOptional.get()
            // Fill with data
            form.addDetails(partner, true)
            partner.modifiedOn = Timestamp.from(Instant.now())
            partner.modifiedBy = this.commonDaoServices.getLoggedInUser()?.userName
            val saved = this.partnersRepository.save(partner)
            response.data = saved.id
            response.responseCode = ResponseCodes.NOT_FOUND
            response.message = "Partner updated successfully"
        } else {
            response.responseCode = ResponseCodes.DUPLICATE_ENTRY_STATUS
            response.message = "Partner does not exist"
        }
        return response
    }

    fun addPartnerApiClient(form: ApiClientForm, partnerId: Long): ApiResponseModel {
        val response = ApiResponseModel()
        val partnerOptional = partnersRepository.findById(partnerId)
        if (partnerOptional.isPresent) {
            val partner = partnerOptional.get()
            // Fill with data
            val client = this.apiClientService.createApiClient(form)
            if (ResponseCodes.SUCCESS_CODE.equals(client.responseCode)) {
                val data = client.data as HashMap<String, Any>
                partner.apiClientId = data["record_id"] as Long
                partner.modifiedOn = Timestamp.from(Instant.now())
                partner.modifiedBy = this.commonDaoServices.getLoggedInUser()?.userName
                val saved = this.partnersRepository.save(partner)
                response.data = saved.id
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "Partner client created successfully"
            } else {
                response.responseCode = client.responseCode
                response.message = client.responseCode
            }
        } else {
            response.responseCode = ResponseCodes.DUPLICATE_ENTRY_STATUS
            response.message = "Partner does not exist"
        }
        return response
    }

    fun listPartners(keywords: String?, page: PageRequest): ApiResponseModel {
        val response = ApiResponseModel()
        keywords?.let {
            val pg = this.partnersRepository.findByPartnerRefNoContains(it, page)
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Search result"
            response.pageNo = pg.number
            response.totalCount = pg.totalElements
            response.totalPages = pg.totalPages
            response.data = PvocPartnerDto.fromList(pg.toList())
            response
        } ?: run {
            val pg = this.partnersRepository.findAll(page)
            response.data = PvocPartnerDto.fromList(pg.toList())
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Success"
            response.pageNo = pg.number
            response.totalCount = pg.totalElements
            response.totalPages = pg.totalPages
            response
        }
        return response
    }
}