package org.kebs.app.kotlin.apollo.api.service

import org.kebs.app.kotlin.apollo.api.payload.ApiClientForm
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.payload.request.CorporateForm
import org.kebs.app.kotlin.apollo.api.payload.request.PvocPartnersForms
import org.kebs.app.kotlin.apollo.api.payload.response.PvocPartnerCountryDto
import org.kebs.app.kotlin.apollo.api.payload.response.PvocPartnerDto
import org.kebs.app.kotlin.apollo.api.payload.response.PvocPartnerTypeDto
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.store.model.pvc.PvocPartnersEntity
import org.kebs.app.kotlin.apollo.store.repo.IPvocPartnersRepository
import org.kebs.app.kotlin.apollo.store.repo.di.IPvocPartnerTypeRepository
import org.kebs.app.kotlin.apollo.store.repo.di.IPvocPartnersCountriesRepository
import org.kebs.app.kotlin.apollo.store.repo.di.IPvocPartnersRegion
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.sql.Timestamp
import java.time.Instant

@Component
class PvocPartnerService(
        private val corporateCustomerService: CorporateCustomerService,
        private val partnersRepository: IPvocPartnersRepository,
        private val commonDaoServices: CommonDaoServices,
        private val partnerCountryRepo: IPvocPartnersCountriesRepository,
        private val partnerCategoryRepo: IPvocPartnerTypeRepository,
        private val apiClientService: ApiClientService
) {
    fun listPartnerCategories(): ApiResponseModel {
        val response = ApiResponseModel()
        response.data = PvocPartnerTypeDto.fromList(partnerCategoryRepo.findAllByStatus(1))
        response.responseCode = ResponseCodes.SUCCESS_CODE
        response.message = "Success"
        return response
    }

    fun listPartnerCountries(): ApiResponseModel {
        val response = ApiResponseModel()
        response.data = PvocPartnerCountryDto.fromList(partnerCountryRepo.findAllByStatus(1))
        response.responseCode = ResponseCodes.SUCCESS_CODE
        response.message = "Success"
        return response
    }

    @Transactional
    fun addPartnerDetails(form: PvocPartnersForms): ApiResponseModel {
        val response = ApiResponseModel()
        if (partnersRepository.findByPartnerRefNo(form.partnerRefNo).isEmpty) {
            val partner = PvocPartnersEntity()
            // Fill with data
            form.addDetails(partner, false)
            form.partnerCountry?.let {
                val country = partnerCountryRepo.findById(it)
                if (country.isPresent) {
                    partner.partnerCountry = country.get()
                    partner.partnerRegion = country.get().regionId
                }
            }
            partner.createdOn = Timestamp.from(Instant.now())
            partner.createdBy = this.commonDaoServices.getLoggedInUser()?.userName
            val saved = this.partnersRepository.save(partner)
            addUpdateBilling(form, partner, false)
            response.data = PvocPartnerDto.fromEntity(saved)
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Partner added"
        } else {
            response.responseCode = ResponseCodes.DUPLICATE_ENTRY_STATUS
            response.message = "Partner with given reference number already exists"
        }
        return response
    }

    fun addUpdateBilling(form: PvocPartnersForms, partner: PvocPartnersEntity, update: Boolean) {
        val billingForm = CorporateForm()
        billingForm.apply {
            contactEmail = form.billingContactEmail
            contactName = form.billingContactName
            corporateIdentifier = form.partnerPin
            corporateName = form.partnerName
            corporateType = "PVOC"
            billingLimitId = form.billingLimitId
            corporatePhone = form.partnerTelephoneNumber
            corporateEmail = form.partnerEmail
            contactPhone = form.billingContactPhone
            isCiakMember = false
            mouDays = 2
        }
        // Add billing information on partner
        if (update) {
            partner.billingId?.let {
                val responseModel = this.corporateCustomerService.updateCorporateCustomer(billingForm, it)
                if ("00" != responseModel.responseCode) {
                    throw ExpectedDataNotFound(responseModel.message)
                }
                responseModel
            }
        } else {
            val responseModel = this.corporateCustomerService.addCorporateCustomer(billingForm)
            if ("00" == responseModel.responseCode) {
                partner.billingId = responseModel.data as Long
            } else {
                throw ExpectedDataNotFound(responseModel.message)
            }
        }

    }

    @Transactional
    fun updatePartnerDetails(form: PvocPartnersForms, partnerId: Long): ApiResponseModel {
        val response = ApiResponseModel()
        val partnerOptional = partnersRepository.findById(partnerId)
        if (partnerOptional.isPresent) {
            val partner = partnerOptional.get()
            // Fill with data
            form.addDetails(partner, true)
            // Add country and region
            form.partnerCountry?.let {
                partner.partnerCountry = partnerCountryRepo.findById(it).orElse(null)
                partner.partnerRegion = partner.partnerCountry?.regionId
            }
            addUpdateBilling(form, partner, true)
            partner.modifiedOn = Timestamp.from(Instant.now())
            partner.modifiedBy = this.commonDaoServices.getLoggedInUser()?.userName
            val saved = this.partnersRepository.save(partner)
            response.data = saved.id
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Partner updated successfully"
        } else {
            response.responseCode = ResponseCodes.NOT_FOUND
            response.message = "Partner does not exist"
        }
        return response
    }

    fun getPartnerDetails(partnerId: Long): ApiResponseModel {
        val response = ApiResponseModel()
        val partnerOptional = partnersRepository.findById(partnerId)
        if (partnerOptional.isPresent) {
            val partner = partnerOptional.get()
            // Fill with data
            val map = mutableMapOf<String, Any>()
            map.put("partner_details", PvocPartnerDto.fromEntity(partner))
            partner.apiClientId?.let { clientId ->
                this.apiClientService.getClientDetails(clientId)?.let { clientDao ->
                    map["api_client"] = clientDao
                }
            }

            response.data = map
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Partner updated successfully"
        } else {
            response.responseCode = ResponseCodes.NOT_FOUND
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