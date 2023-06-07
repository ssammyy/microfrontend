package org.kebs.app.kotlin.apollo.api.handlers.reports

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.payload.extractPage
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.service.reports.DIReports
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

class ReportRequest {
    var filters: Map<String, String>? = null
    var reportName: String? = null
    var orderBy: List<String>? = null
}

@Component
class ReportsHandler(val diReports: DIReports, private val commonDaoServices: CommonDaoServices) {
    fun consignmentReport(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        try {
            val page = extractPage(req)
            val data = req.body(ReportRequest::class.java)
            val auth = this.commonDaoServices.loggedInUserAuthentication()
            val filters = data.filters!!.toMutableMap()
            when {
                auth.authorities.stream()
                    .anyMatch { authority -> authority.authority == "DI_INSPECTION_OFFICER_READ" } -> {
                    val user = commonDaoServices.getLoggedInUser()
                    filters["owner"] = user?.userName ?: ""
                    filters["owner_id"] = (user?.id ?: 0).toString()
                }
                filters.containsKey("owner") -> {
                    if ("yes".equals(filters.get("owner"), true)) {
                        val user = commonDaoServices.getLoggedInUser()
                        filters["req_assigner"] = user?.userName ?: ""
                        filters["req_assigner_id"] = (user?.id ?: 0).toString()
                    }
                }
            }
            return ServerResponse.ok().body(
                this.diReports.generateReport(
                    data.filters!!, data.reportName ?: "", page.pageNumber,
                    page.pageSize.toLong()
                )
            )
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to  get requests", ex)
            response.responseCode = ResponseCodes.INVALID_CODE
            response.message = "Failed to process request"
        }
        return ServerResponse.ok().body(response)
    }
}