package org.kebs.app.kotlin.apollo.api.handlers.di

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.payload.extractPage
import org.kebs.app.kotlin.apollo.api.payload.request.*
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.sage.PostInvoiceToSageServices
import org.kebs.app.kotlin.apollo.api.service.DaoValidatorService
import org.kebs.app.kotlin.apollo.api.service.DestinationInspectionConfigService
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

@Component
class ConfigurationsHandler(
        private val validationService: DaoValidatorService,
        private val configService: DestinationInspectionConfigService,
        private val sageService: PostInvoiceToSageServices,
        private val commonDaoServices: CommonDaoServices,
        private val properties: ApplicationMapProperties,
) {
    fun listRevenueLines(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        try {
            val map = commonDaoServices.serviceMapDetails(properties.mapImportInspection)
            response.data = sageService.listRevenueLines(map)
            response.message = "Success"
            response.responseCode = ResponseCodes.SUCCESS_CODE
        } catch (ex: ExpectedDataNotFound) {
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = ex.localizedMessage
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to get revenue lines")
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed to process request"
            response.errors = ex.localizedMessage
        }

        return ServerResponse.ok().body(response)
    }

    fun listBillLimits(req: ServerRequest): ServerResponse {
        val page = extractPage(req)
        return ServerResponse.ok().body(configService.listBillTypes(page))
    }

    fun removeBillLimit(req: ServerRequest): ServerResponse {
        val limitId = req.pathVariable("limitId").toLong()
        return ServerResponse.ok().body(configService.removeBillLimit(limitId))
    }

    fun addBillingLimit(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            val form = req.body(BillingLimitForm::class.java)
            validationService.validateInputWithInjectedValidator(form)?.let {
                response.errors = it
                response.responseCode = ResponseCodes.INVALID_CODE
                response.message = "Invalid request received"
                response
            } ?: run {
                response = configService.addBillingLimits(form)
                response
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to add bill limit", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed to add bill limit"
        }
        return ServerResponse.ok()
                .body(response)
    }

    fun updateBillingLimit(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            val limitId = req.pathVariable("limitId").toLong()
            val form = req.body(BillingLimitForm::class.java)
            validationService.validateInputWithInjectedValidator(form)?.let {
                response.errors = it
                response.responseCode = ResponseCodes.INVALID_CODE
                response.message = "Invalid request received"
                response
            } ?: run {
                response = configService.updateBillType(form, limitId)
                response
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to update bill limit", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed to update bill limit"
        }
        return ServerResponse.ok()
                .body(response)
    }

    fun addCfsStations(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            val form = req.body(CfsStationForm::class.java)
            validationService.validateInputWithInjectedValidator(form)?.let {
                response.errors = it
                response.responseCode = ResponseCodes.INVALID_CODE
                response.message = "Invalid request received"
                response
            } ?: run {
                response = configService.addCfsStation(form)
                response
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to add CFS", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed to add CFS stations"
        }
        return ServerResponse.ok()
                .body(response)
    }

    fun updateCfsStation(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            val cfsId = req.pathVariable("cfsId").toLong()
            val form = req.body(CfsStationForm::class.java)
            validationService.validateInputWithInjectedValidator(form)?.let {
                response.errors = it
                response.responseCode = ResponseCodes.INVALID_CODE
                response.message = "Invalid request received"
                response
            } ?: run {
                response = configService.updateCfsStation(form, cfsId)
                response
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to update CFS", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed to update CFS stations"
        }
        return ServerResponse.ok()
                .body(response)
    }

    fun removeCfsStation(req: ServerRequest): ServerResponse {
        val cfsId = req.pathVariable("cfsId").toLong()
        return ServerResponse.ok().body(configService.removeCfsStation(cfsId))
    }

    fun listCfsStations(req: ServerRequest): ServerResponse {
        val page = extractPage(req)
        return ServerResponse.ok().body(configService.listCfsStations(page))
    }

    fun listPvocRegions(req: ServerRequest): ServerResponse {
        val page = extractPage(req)
        return ServerResponse.ok().body(configService.listPvocRegions(page))
    }

    fun deletePvocRegion(req: ServerRequest): ServerResponse {
        val regionId = req.pathVariable("regionId").toLong()
        return ServerResponse.ok().body(configService.removePvocRegion(regionId))
    }

    fun addPvocRegion(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            val form = req.body(PvocPartnerRegionForm::class.java)
            validationService.validateInputWithInjectedValidator(form)?.let {
                response.errors = it
                response.responseCode = ResponseCodes.INVALID_CODE
                response.message = "Invalid request received"
                response
            } ?: run {
                response = configService.addPvocRegion(form)
                response
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to add PVOC region", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed to add PVOC region"
        }
        return ServerResponse.ok()
                .body(response)
    }

    fun updatePvocRegion(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            val regionId = req.pathVariable("regionId").toLong()
            val form = req.body(PvocPartnerRegionForm::class.java)
            validationService.validateInputWithInjectedValidator(form)?.let {
                response.errors = it
                response.responseCode = ResponseCodes.INVALID_CODE
                response.message = "Invalid request received"
                response
            } ?: run {
                response = configService.updatePvocRegion(form, regionId)
                response
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to update PVOC region", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed to update PVOC region"
        }
        return ServerResponse.ok()
                .body(response)
    }

    fun listPvocCountries(req: ServerRequest): ServerResponse {
        val page = extractPage(req)
        return ServerResponse.ok().body(configService.listPvocCountries(page))
    }

    fun deletePvocCountry(req: ServerRequest): ServerResponse {
        val countryId = req.pathVariable("countryId").toLong()
        return ServerResponse.ok().body(configService.removePvocCountry(countryId))
    }

    fun addPvocCountry(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            val form = req.body(PvocPartnerCountryForm::class.java)
            validationService.validateInputWithInjectedValidator(form)?.let {
                response.errors = it
                response.responseCode = ResponseCodes.INVALID_CODE
                response.message = "Invalid request received"
                response
            } ?: run {
                response = configService.addPvocCountry(form)
                response
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to add PVOC country", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed to add PVOC country"
        }
        return ServerResponse.ok()
                .body(response)
    }

    fun updatePvocCountry(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            val countryId = req.pathVariable("countryId").toLong()
            val form = req.body(PvocPartnerCountryForm::class.java)
            validationService.validateInputWithInjectedValidator(form)?.let {
                response.errors = it
                response.responseCode = ResponseCodes.INVALID_CODE
                response.message = "Invalid request received"
                response
            } ?: run {
                response = configService.updatePvocCountry(form, countryId)
                response
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to add PVOC country", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed to add PVOC country"
        }
        return ServerResponse.ok()
                .body(response)
    }

    fun listDestinationInspectionFee(req: ServerRequest): ServerResponse {
        val page = extractPage(req)
        return ServerResponse.ok().body(configService.listDestinationInspectionFee(page))
    }

    //    @PreAuthorize("hasAnyAuthority('DI_ADMIN_WRITE','DI_ADMIN')")
    fun deleteDestinationInspectionFee(req: ServerRequest): ServerResponse {
        val feeId = req.pathVariable("feeId").toLong()
        return ServerResponse.ok().body(configService.removeDestinationInspectionFee(feeId))
    }

    //    @PreAuthorize("hasAnyAuthority('DI_ADMIN_WRITE','DI_ADMIN')")
    fun destinationInspectionFeeDetails(req: ServerRequest): ServerResponse {
        val feeId = req.pathVariable("feeId").toLong()
        return ServerResponse.ok().body(configService.getDestinationInspectionFee(feeId))
    }

    //    @PreAuthorize("hasAnyAuthority('DI_ADMIN_WRITE','DI_ADMIN')")
    fun addDestinationInspectionFee(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            val form = req.body(DIFees::class.java)
            validationService.validateInputWithInjectedValidator(form)?.let {
                response.errors = it
                response.responseCode = ResponseCodes.INVALID_CODE
                response.message = "Invalid request received"
                response
            } ?: run {
                response = configService.addDestinationInspectionFee(form)
                response
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to add fee", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed to add inspection fee"
        }
        return ServerResponse.ok()
                .body(response)
    }

    //    @PreAuthorize("hasAnyAuthority('DI_ADMIN_WRITE','DI_ADMIN')")
    fun updateDestinationInspectionFee(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            val feeId = req.pathVariable("feeId").toLong()
            val form = req.body(DIFees::class.java)
            validationService.validateInputWithInjectedValidator(form)?.let {
                response.errors = it
                response.responseCode = ResponseCodes.INVALID_CODE
                response.message = "Invalid request received"
                response
            } ?: run {
                response = configService.updateDestinationInspectionFee(form, feeId)
                response
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to update fee", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed to update inspection fee"
        }
        return ServerResponse.ok()
                .body(response)
    }

    //    @PreAuthorize("hasAnyAuthority('DI_ADMIN_WRITE','DI_ADMIN')")
    fun addDestinationInspectionFeeRange(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            val feeId = req.pathVariable("feeId").toLong()
            val form = req.body(DIRageFees::class.java)
            validationService.validateInputWithInjectedValidator(form)?.let {
                response.errors = it
                response.responseCode = ResponseCodes.INVALID_CODE
                response.message = "Invalid request received"
                response
            } ?: run {
                response = configService.addDestinationInspectionFeeRange(form, feeId)
                response
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to add fee range", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed to add inspection fee range"
        }
        return ServerResponse.ok()
                .body(response)
    }

    //    @PreAuthorize("hasAnyAuthority('DI_ADMIN_WRITE','DI_ADMIN')")
    fun updateDestinationInspectionFeeRange(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            val feeId = req.pathVariable("feeId").toLong()
            val rangeId = req.pathVariable("rangeId").toLong()
            val form = req.body(DIRageFees::class.java)
            form.rangeId = rangeId
            validationService.validateInputWithInjectedValidator(form)?.let {
                response.errors = it
                response.responseCode = ResponseCodes.INVALID_CODE
                response.message = "Invalid request received"
                response
            } ?: run {
                response = configService.updateDestinationInspectionFeeRange(form, feeId)
                response
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to update fee range", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed to update inspection fee range"
        }
        return ServerResponse.ok()
                .body(response)
    }

    fun listCustomsOffices(req: ServerRequest): ServerResponse {
        val page = extractPage(req)
        return ServerResponse.ok().body(configService.listCustomsOffice(page))
    }

    //    @PreAuthorize("hasAnyAuthority('DI_ADMIN_WRITE','DI_ADMIN')")
    fun deleteCustomOffice(req: ServerRequest): ServerResponse {
        val officeId = req.pathVariable("officeId").toLong()
        return ServerResponse.ok().body(configService.removeCustomOffice(officeId))
    }

    fun addCustomsOffice(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            val form = req.body(CustomOfficeForm::class.java)
            validationService.validateInputWithInjectedValidator(form)?.let {
                response.errors = it
                response.responseCode = ResponseCodes.INVALID_CODE
                response.message = "Invalid request received"
                response
            } ?: run {
                response = configService.addCustomOffice(form)
                response
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to add customs office", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed to add customs office"
        }
        return ServerResponse.ok()
                .body(response)
    }

    fun updateCustomsOffice(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            val officeId = req.pathVariable("officeId").toLong()
            val form = req.body(CustomOfficeForm::class.java)
            validationService.validateInputWithInjectedValidator(form)?.let {
                response.errors = it
                response.responseCode = ResponseCodes.INVALID_CODE
                response.message = "Invalid request received"
                response
            } ?: run {
                response = configService.updateCustomsOffice(form, officeId)
                response
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to update customs office", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed to update customs office"
        }
        return ServerResponse.ok()
                .body(response)
    }

}
