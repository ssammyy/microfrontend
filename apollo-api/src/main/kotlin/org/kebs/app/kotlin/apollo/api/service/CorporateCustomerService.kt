package org.kebs.app.kotlin.apollo.api.service

import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.payload.request.CorporateForm
import org.kebs.app.kotlin.apollo.api.payload.request.CorporateStatusUpdateForm
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.store.model.invoice.CorporateCustomerAccounts
import org.kebs.app.kotlin.apollo.store.repo.ICorporateCustomerRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import java.sql.Timestamp
import java.time.Instant

@Component
class CorporateCustomerService(
        private val corporateCustomersRepository: ICorporateCustomerRepository,
        private val commonDaoServices: CommonDaoServices,
) {

    fun addCorporateCustomer(form: CorporateForm): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            if (this.corporateCustomersRepository.findAllByCorporateIdentifier(form.corporateIdentifier).isEmpty) {
                val customer = CorporateCustomerAccounts()
                customer.corporateIdentifier = form.corporateIdentifier
                customer.contactEmail = form.contactEmail
                customer.lastPayment=Timestamp.from(Instant.now())
                customer.contactName = form.contactName
                customer.contactPhone = form.contactPhone
                customer.corporateBillNumber = form.corporateBillNumber
                customer.corporateEmail = form.corporateEmail
                customer.corporateName = form.corporateName
                customer.corporateType = form.corporateType
                customer.status = 1
                customer.accountBlocked = 0
                customer.accountSuspendend = 0
                customer.createdOn = Timestamp.from(Instant.now())
                customer.createdBy = commonDaoServices.getLoggedInUser()?.userName
                val saved = this.corporateCustomersRepository.save(customer)
                response.data = saved.id
                response.message = "Account created"
                response.responseCode = ResponseCodes.SUCCESS_CODE
            } else {
                response.message = "Corporate identifier already exists"
                response.responseCode = ResponseCodes.DUPLICATE_ENTRY_STATUS
            }
        } catch (ex: Exception) {
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed to add account"
            response.errors = ex.toString()
        }
        return response
    }

    fun updateCorporateCustomer(form: CorporateForm, corporateId: Long): ApiResponseModel {
        val response = ApiResponseModel()
        val corporateIdentifier = this.corporateCustomersRepository.findById(corporateId)
        if (corporateIdentifier.isEmpty) {
            response.message = "Invalid corporate identifier"
            response.responseCode = ResponseCodes.NOT_FOUND
        } else {
            val customer = corporateIdentifier.get()
            customer.contactEmail = form.contactEmail
            customer.contactName = form.contactName
            customer.contactPhone = form.contactPhone
            customer.corporateBillNumber = form.corporateBillNumber
            customer.corporateEmail = form.corporateEmail
            customer.corporateName = form.corporateName
            customer.corporateType = form.corporateType
            customer.modifiedOn = Timestamp.from(Instant.now())
            customer.modifiedBy = commonDaoServices.getLoggedInUser()?.userName
            val saved = this.corporateCustomersRepository.save(customer)
            response.data = saved.id
            response.message = "Account updated"
            response.responseCode = ResponseCodes.SUCCESS_CODE
        }
        return response
    }

    fun updateCorporateStatus(form: CorporateStatusUpdateForm, corporateId: Long): ApiResponseModel {
        val response = ApiResponseModel()
        val corporateIdentifier = this.corporateCustomersRepository.findById(corporateId)
        if (corporateIdentifier.isEmpty) {
            response.message = "Invalid corporate identifier"
            response.responseCode = ResponseCodes.NOT_FOUND
        } else {
            val customer = corporateIdentifier.get()
            if (customer.status == 4) {
                response.message = "corporate account has been deleted"
                response.responseCode = ResponseCodes.INVALID_CODE
                return response
            }
            customer.varField1 = form.remarks
            when (form.actionCode) {
                "BLOCK" -> {
                    customer.accountBlocked = 1
                    this.corporateCustomersRepository.save(customer)
                    response.message = "Account blocked"
                    response.responseCode = ResponseCodes.SUCCESS_CODE
                }
                "UNBLOCK" -> {
                    customer.accountBlocked = 0
                    this.corporateCustomersRepository.save(customer)
                    response.message = "Account unblocked"
                    response.responseCode = ResponseCodes.SUCCESS_CODE
                }
                "SUSPEND" -> {
                    customer.accountSuspendend = 1
                    response.message = "Account suspended"
                    response.responseCode = ResponseCodes.SUCCESS_CODE
                }
                "UNSUSPEND" -> {
                    customer.accountSuspendend = 0
                    this.corporateCustomersRepository.save(customer)
                    response.message = "Account activated"
                    response.responseCode = ResponseCodes.SUCCESS_CODE
                }
                "DELETE" -> {
                    customer.status = 4
                    this.corporateCustomersRepository.save(customer)
                    response.message = "Account activated"
                    response.responseCode = ResponseCodes.SUCCESS_CODE
                }
                else -> {
                    response.message = "Invalid account action: ${form.actionCode}"
                    response.responseCode = ResponseCodes.INVALID_CODE
                }
            }
        }
        return response
    }

    fun listCorporateCustomers(keywords: String?, page: PageRequest): ApiResponseModel {
        val response = ApiResponseModel()
        keywords?.let {
            val pg = this.corporateCustomersRepository.findAllByCorporateNameContains(it, page)
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Success"
            response.pageNo = pg.number
            response.totalPages = pg.totalPages
            response.totalCount = pg.totalElements
        } ?: run {
            val pg = this.corporateCustomersRepository.findAll(page)
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Success"
            response.pageNo = pg.number
            response.totalPages = pg.totalPages
            response.totalCount = pg.totalElements
            response
        }
        return response
    }
}