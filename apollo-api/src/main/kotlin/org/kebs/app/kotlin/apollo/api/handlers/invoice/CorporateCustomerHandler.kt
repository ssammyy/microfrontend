package org.kebs.app.kotlin.apollo.api.handlers.invoice;

import org.kebs.app.kotlin.apollo.api.payload.ApiClientForm
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.payload.extractPage
import org.kebs.app.kotlin.apollo.api.payload.request.CorporateForm
import org.kebs.app.kotlin.apollo.api.payload.request.CorporateStatusUpdateForm
import org.kebs.app.kotlin.apollo.api.service.CorporateCustomerService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

@Component
class CorporateCustomerHandler(
        private val corporateService: CorporateCustomerService
) {
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
            response = this.corporateService.addCorporateCustomer(form)
        } catch (ex: Exception) {
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
            response = this.corporateService.updateCorporateCustomer(form, corporateId)
        } catch (ex: Exception) {
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
