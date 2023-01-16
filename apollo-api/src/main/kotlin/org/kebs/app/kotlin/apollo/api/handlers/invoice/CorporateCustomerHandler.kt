package org.kebs.app.kotlin.apollo.api.handlers.invoice;

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.payload.extractPage
import org.kebs.app.kotlin.apollo.api.payload.request.CorporateForm
import org.kebs.app.kotlin.apollo.api.payload.request.CorporateStatusUpdateForm
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.service.BillStatus
import org.kebs.app.kotlin.apollo.api.service.BillingService
import org.kebs.app.kotlin.apollo.api.service.CorporateCustomerService
import org.kebs.app.kotlin.apollo.api.service.DaoValidatorService
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.paramOrNull

@Component
class CorporateCustomerHandler(
    private val corporateService: CorporateCustomerService,
    private val billService: BillingService,
    private val commonDaoServices: CommonDaoServices,
    private val daoValidatorService: DaoValidatorService
) {
    fun corporateDetails(req: ServerRequest): ServerResponse {
        val corporateId = req.pathVariable("corporateId").toLong()
        return ServerResponse.ok().body(corporateService.corporateCustomerDetails(corporateId))
    }

    fun currentCorporateBills(req: ServerRequest): ServerResponse {
        val corporateId = req.pathVariable("corporateId").toLong()
        return ServerResponse.ok().body(billService.corporateBills(corporateId, extractPage(req)))
    }

    fun corporateBillDetails(req: ServerRequest): ServerResponse {
        val corporateId = req.pathVariable("corporateId").toLong()
        val billId = req.pathVariable("billId").toLong()
        return ServerResponse.ok().body(billService.billTransactions(billId, corporateId))
    }

    fun corporateBillStatus(req: ServerRequest): ServerResponse {
        val corporateId = req.pathVariable("corporateId").toLong()
        val billStatus = req.pathVariable("billStatus")
        val statues = mutableListOf<Int>()
        when (billStatus) {
            "paid" -> {
                statues.add(BillStatus.PAID.status)
            }
            "pending" -> {
                statues.add(BillStatus.OPEN.status)
                statues.add(BillStatus.PENDING_PAYMENT.status)
                statues.add(BillStatus.CLOSED.status)
            }
            else -> {
                val res = ApiResponseModel()
                res.message = "Invalid bill statue: $billStatus"
                res.responseCode = ResponseCodes.INVALID_CODE
                return ServerResponse.ok().body(res)
            }
        }
        return ServerResponse.ok().body(billService.corporateBillByPaymentStatus(corporateId, statues))
    }

    fun listCorporateBill(req: ServerRequest): ServerResponse {
        val keywords = req.paramOrNull("keyword")
        val billStatus = req.paramOrNull("billStatus")
        val page = extractPage(req)
        val statues = mutableListOf<Int>()
        if (keywords.isNullOrEmpty()) {
            when (billStatus) {
                "paid" -> {
                    statues.add(BillStatus.PAID.status)
                }
                "open" -> {
                    statues.add(BillStatus.OPEN.status)
                }
                "pending" -> {
                    statues.add(BillStatus.PENDING_PAYMENT.status)
                    statues.add(BillStatus.CLOSED.status)
                }
                else -> {
                    val res = ApiResponseModel()
                    res.message = "Invalid bill status: $billStatus"
                    res.responseCode = ResponseCodes.INVALID_CODE
                    return ServerResponse.ok().body(res)
                }
            }
        } else {
            // Remove open bills on search result
            val auth = commonDaoServices.loggedInUserAuthentication()
            when {
                auth.authorities.stream()
                    .anyMatch { authority -> authority.authority == "DI_OFFICER_CHARGE_READ" } -> {
                    statues.add(BillStatus.PENDING_PAYMENT.status)
                    statues.add(BillStatus.CLOSED.status)
                    statues.add(BillStatus.OPEN.status)
                    statues.add(BillStatus.PAID.status)

                }
                else -> {
                    statues.add(BillStatus.PAID.status)
                    statues.add(BillStatus.PENDING_PAYMENT.status)
                    statues.add(BillStatus.CLOSED.status)
                }
            }

        }
        return ServerResponse.ok().body(billService.corporateBillByPaymentStatus(page, keywords, statues))
    }

    fun listCorporateBillingLimits(req: ServerRequest): ServerResponse {
        return ServerResponse.ok().body(corporateService.listTransactionLimits())
    }

    fun listCorporateCustomers(req: ServerRequest): ServerResponse {
        val page = extractPage(req)
        val keywords = req.param("keywords")
        return ServerResponse.ok()
            .body(corporateService.listCorporateCustomers(keywords.orElse(null), page))
    }

    fun addCorporateCustomer(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            val form = req.body(CorporateForm::class.java)
            daoValidatorService.validateInputWithInjectedValidator(form)?.let {
                response.data = form
                response.responseCode = ResponseCodes.INVALID_CODE
                response.errors = it
                response.message = "Invalid request data, please make changes indicated"
                response
            } ?: run {
                response = this.corporateService.addCorporateCustomer(form)
                response
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to add account", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed to create corporate account"
            response.errors = ex.toString()
        }
        return ServerResponse.ok().body(response)
    }

    fun updateCorporateCustomer(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            val corporateId = req.pathVariable("corporateId").toLong()
            val form = req.body(CorporateForm::class.java)
            daoValidatorService.validateInputWithInjectedValidator(form)?.let {
                response.data = form
                response.responseCode = ResponseCodes.INVALID_CODE
                response.errors = it
                response.message = "Invalid request data, please make changes indicated"
                response
            } ?: run {
                response = this.corporateService.updateCorporateCustomer(form, corporateId)
                response
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to update account", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed to update corporate account"
            response.errors = ex.toString()
        }
        return ServerResponse.ok().body(response)
    }

    fun updateCorporateCustomerStatus(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            val corporateId = req.pathVariable("corporateId").toLong()
            val form = req.body(CorporateStatusUpdateForm::class.java)
            response = this.corporateService.updateCorporateStatus(form, corporateId)
        } catch (ex: Exception) {
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed to update corporate account"
            response.errors = ex.toString()
        }
        return ServerResponse.ok().body(response)
    }

    fun closeBillPayment(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            val billId = req.pathVariable("billId").toLong()
            val form = req.body(CorporateStatusUpdateForm::class.java)
            response = this.billService.closeAndGenerateBill(billId, form.remarks ?: "")
        } catch (ex: ExpectedDataNotFound) {
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = ex.localizedMessage
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to close bill", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed to update bill status"
            response.errors = ex.toString()
        }
        return ServerResponse.ok().body(response)
    }
}
