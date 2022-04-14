package org.kebs.app.kotlin.apollo.api.service

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.payload.request.CorporateForm
import org.kebs.app.kotlin.apollo.api.payload.request.CorporateStatusUpdateForm
import org.kebs.app.kotlin.apollo.api.payload.response.CorporateCustomerAccountDao
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.store.model.invoice.CorporateCustomerAccounts
import org.kebs.app.kotlin.apollo.store.repo.IBillingLimitsRepository
import org.kebs.app.kotlin.apollo.store.repo.ICorporateCustomerRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime

@Component
class CorporateCustomerService(
        private val corporateCustomersRepository: ICorporateCustomerRepository,
        private val billingLimitsRepository: IBillingLimitsRepository,
        private val commonDaoServices: CommonDaoServices,
) {

    fun listTransactionLimits(): ApiResponseModel {
        val response = ApiResponseModel()
        response.data = this.billingLimitsRepository.findAllByStatus(1)
        response.responseCode = ResponseCodes.SUCCESS_CODE
        response.message = "Success"
        return response
    }

    fun countAccountsToday(): String {
        val localDateTime: LocalDateTime = LocalDateTime.now()
        val startOfDay: LocalDateTime = localDateTime.with(LocalTime.MIN)
        val endOfDay: LocalDateTime = localDateTime.with(LocalTime.MAX)
        val cc = corporateCustomersRepository.countByCreatedOnBetween(Timestamp.valueOf(startOfDay), Timestamp.valueOf(endOfDay))
        return "%05x".format(cc)
    }

    fun addCorporateCustomer(form: CorporateForm): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            if (this.corporateCustomersRepository.findAllByCorporateIdentifier(form.corporateIdentifier).isEmpty) {
                val customer = CorporateCustomerAccounts()
                customer.corporateIdentifier = form.corporateIdentifier
                customer.contactEmail = form.contactEmail
                customer.corporatePhone = form.corporatePhone
                customer.lastPayment = Timestamp.from(Instant.now())
                customer.contactName = form.contactName
                customer.contactPhone = form.contactPhone
                val limits = billingLimitsRepository.findById(form.billingLimitId)
                if (limits.isPresent) {
                    customer.accountLimits = limits.get()
                }
                customer.corporateBillNumber = "KBN${commonDaoServices.convertDateToString(LocalDateTime.now(), "yyyyMMdd")}${countAccountsToday()}".toUpperCase()
                customer.corporateEmail = form.corporateEmail
                customer.corporateName = form.corporateName
                customer.corporateCode = form.corporateCode
                customer.corporateType = form.corporateType
                customer.isCiakMember = form.isCiakMember
                customer.paymentDays = form.mouDays
                customer.currentBalance = BigDecimal.ZERO
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
            KotlinLogging.logger { }.error("Failed to add account", ex)
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
            customer.paymentDays = form.mouDays
            val limits = billingLimitsRepository.findById(form.billingLimitId)
            if (limits.isPresent) {
                customer.accountLimits = limits.get()
            }
            customer.contactPhone = form.contactPhone
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

    fun corporateCustomerDetails(corporateId: Long): ApiResponseModel {
        val response = ApiResponseModel()
        val corporateIdentifier = this.corporateCustomersRepository.findById(corporateId)
        if (corporateIdentifier.isEmpty) {
            response.message = "Invalid corporate identifier"
            response.responseCode = ResponseCodes.NOT_FOUND
        } else {
            response.data = CorporateCustomerAccountDao.fromEntity(corporateIdentifier.get())
            response.message = "Success"
            response.responseCode = ResponseCodes.SUCCESS_CODE
        }
        return response
    }

    fun listCorporateCustomers(keywords: String?, page: PageRequest): ApiResponseModel {
        val response = ApiResponseModel()
        keywords?.let {
            val pg = this.corporateCustomersRepository.findAllByCorporateNameContains(it, page)
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Success"
            response.data = CorporateCustomerAccountDao.fromList(pg.toList())
            response.pageNo = pg.number
            response.totalPages = pg.totalPages
            response.totalCount = pg.totalElements
        } ?: run {
            val pg = this.corporateCustomersRepository.findAll(page)
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.data = CorporateCustomerAccountDao.fromList(pg.toList())
            response.message = "Success"
            response.pageNo = pg.number
            response.totalPages = pg.totalPages
            response.totalCount = pg.totalElements
            response
        }
        return response
    }
}