package org.kebs.app.kotlin.apollo.api.handlers.invoice;

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.payload.ApiClientForm
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.payload.extractPage
import org.kebs.app.kotlin.apollo.api.payload.request.CorporateForm
import org.kebs.app.kotlin.apollo.api.payload.request.CorporateStatusUpdateForm
import org.kebs.app.kotlin.apollo.api.service.BillingService
import org.kebs.app.kotlin.apollo.api.service.CorporateCustomerService;
import org.kebs.app.kotlin.apollo.api.service.DaoValidatorService
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

@Component
class CorporateCustomerHandler(
        private val corporateService: CorporateCustomerService,
        private val billService: BillingService,
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
                statues.add(1)
            }
            "pending" -> {
                statues.add(0)
                statues.add(10)
                statues.add(15)
            }
            else -> {
                val res = ApiResponseModel()
                res.message = "Invalid bill statue: ${billStatus}"
                res.responseCode = ResponseCodes.INVALID_CODE
                return ServerResponse.ok().body(res)
            }
        }
        return ServerResponse.ok().body(billService.corporateBillByPaymentStatus(corporateId, statues))
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
}
