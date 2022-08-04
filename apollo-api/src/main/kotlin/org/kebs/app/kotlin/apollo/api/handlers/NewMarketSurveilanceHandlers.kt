/*
 *
 * $$$$$$$\   $$$$$$\  $$\   $$\        $$$$$$\  $$\       $$$$$$\  $$$$$$$\   $$$$$$\  $$\
 * $$  __$$\ $$  __$$\ $$ | $$  |      $$  __$$\ $$ |     $$  __$$\ $$  __$$\ $$  __$$\ $$ |
 * $$ |  $$ |$$ /  \__|$$ |$$  /       $$ /  \__|$$ |     $$ /  $$ |$$ |  $$ |$$ /  $$ |$$ |
 * $$$$$$$\ |\$$$$$\  $$$$$  /        $$ |$$$$\ $$ |     $$ |  $$ |$$$$$$$\ |$$$$$$$$ |$$ |
 * $$  __$$\  \____$$\ $$  $$<         $$ |\_$$ |$$ |     $$ |  $$ |$$  __$$\ $$  __$$ |$$ |
 * $$ |  $$ |$$\   $$ |$$ |\$\        $$ |  $$ |$$ |     $$ |  $$ |$$ |  $$ |$$ |  $$ |$$ |
 * $$$$$$$  |\$$$$$  |$$ | \$\       \$$$$$  |$$$$$$$$\ $$$$$$  |$$$$$$$  |$$ |  $$ |$$$$$$$$\
 * \_______/  \______/ \__|  \__|       \______/ \________|\______/ \_______/ \__|  \__|\________|
 * $$$$$$$$\ $$$$$$$$\  $$$$$$\  $$\   $$\ $$\   $$\  $$$$$$\  $$\       $$$$$$\   $$$$$$\  $$$$$$\ $$$$$$$$\  $$$$$$\
 * \__$$  __|$$  _____|$$  __$$\ $$ |  $$ |$$$\  $$ |$$  __$$\ $$ |     $$  __$$\ $$  __$$\ \_$$  _|$$  _____|$$  __$$\
 *    $$ |   $$ |      $$ /  \__|$$ |  $$ |$$$$\ $$ |$$ /  $$ |$$ |     $$ /  $$ |$$ /  \__|  $$ |  $$ |      $$ /  \__|
 *    $$ |   $$$$$\    $$ |      $$$$$$$$ |$$ $$\$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |$$$$\   $$ |  $$$$$\    \$$$$$\
 *    $$ |   $$  __|   $$ |      $$  __$$ |$$ \$$$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |\_$$ |  $$ |  $$  __|    \____$$\
 *    $$ |   $$ |      $$ |  $$\ $$ |  $$ |$$ |\$$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |  $$ |  $$ |  $$ |      $$\   $$ |
 *    $$ |   $$$$$$$$\ \$$$$$  |$$ |  $$ |$$ | \$ | $$$$$$  |$$$$$$$$\ $$$$$$  |\$$$$$  |$$$$$$\ $$$$$$$$\ \$$$$$  |
 *    \__|   \________| \______/ \__|  \__|\__|  \__| \______/ \________|\______/  \______/ \______|\________| \______/
 *
 *
 *
 *
 *
 *   Copyright (c) 2020.  BSK
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.kebs.app.kotlin.apollo.api.handlers


import com.google.gson.Gson
import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.*
import org.kebs.app.kotlin.apollo.api.ports.provided.validation.AbstractValidationHandler
import org.kebs.app.kotlin.apollo.common.dto.ms.*
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.springframework.stereotype.Component
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.Errors
import org.springframework.validation.Validator
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body
import org.springframework.web.servlet.function.paramOrNull


@Component
class NewMarketSurveillanceHandler(
    private val applicationMapProperties: ApplicationMapProperties,
    private val masterDataDaoService: MasterDataDaoService,
    private val marketSurveillanceFuelDaoServices: MarketSurveillanceFuelDaoServices,
    private val marketSurveillanceComplaintDaoServices: MarketSurveillanceComplaintProcessDaoServices,
    private val marketSurveillanceWorkPlanDaoServices: MarketSurveillanceWorkPlanDaoServices,
    private val commonDaoServices: CommonDaoServices,
    private val validator: Validator,
) : AbstractValidationHandler() {

    final val appId: Int = applicationMapProperties.mapMarketSurveillance

    /*******************************************************************************************************************************************************************************************************************************************************************************************/
                            /**************************************
                             ******THE START OF MS COMMON DETAILS*******
                             **************************************/
    /*******************************************************************************************************************************************************************************************************************************************************************************************/


    fun notSupported(req: ServerRequest): ServerResponse = ServerResponse.badRequest().body("Invalid Request: Not supported")

    fun msDepartments(req: ServerRequest): ServerResponse {
        try {
            val map = commonDaoServices.serviceMapDetails(appId)
            marketSurveillanceComplaintDaoServices.listMsDepartments(map.activeStatus)
                ?.let { return ServerResponse.ok().body(it) }
                ?: throw NullValueNotAllowedException("No Ms Departments found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }
    }

    fun msRecommendationList(req: ServerRequest): ServerResponse {
        try {
            val map = commonDaoServices.serviceMapDetails(appId)
            marketSurveillanceWorkPlanDaoServices.listMsRecommendation(map.activeStatus)
                ?.let {
                    return ServerResponse.ok().body(it) }
                ?: throw NullValueNotAllowedException("No Ms Recommendation found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }
    }


    fun msDivisions(req: ServerRequest): ServerResponse {
        try {
//            val map = commonDaoServices.serviceMapDetails(appId)
//            val departmentID = req.pathVariable("departmentID").toLong()
            masterDataDaoService.getAllDivisions()
                ?.let { return ServerResponse.ok().body(it) }
                ?: throw NullValueNotAllowedException("No Ms Departments found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }
    }

    fun msStandardsCategory(req: ServerRequest): ServerResponse {
        try {
            masterDataDaoService.getAllStandardProductCategory()
                ?.let { return ServerResponse.ok().body(it) }
                ?: throw NullValueNotAllowedException("No Standard Product Category found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }
    }

    fun msProductCategories(req: ServerRequest): ServerResponse {
        try {
            masterDataDaoService.getAllProductCategories()
                ?.let { return ServerResponse.ok().body(it) }
                ?: throw NullValueNotAllowedException("No Standard Product Category found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }
    }

    fun msCountries(req: ServerRequest): ServerResponse {
        try {
            masterDataDaoService.getAllCountries()
                ?.let { return ServerResponse.ok().body(it) }
                ?: throw NullValueNotAllowedException("No Country List found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }
    }

    fun msBroadProductCategory(req: ServerRequest): ServerResponse {
        try {
            masterDataDaoService.getAllBroadProductCategory()
                ?.let { return ServerResponse.ok().body(it) }
                ?: throw NullValueNotAllowedException("No Standard Product Category found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }
    }

    fun msProducts(req: ServerRequest): ServerResponse {
        try {
            masterDataDaoService.getAllProducts()
                ?.let { return ServerResponse.ok().body(it) }
                ?: throw NullValueNotAllowedException("No Standard Product Category found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }
    }

    fun msProductSubcategory(req: ServerRequest): ServerResponse {
        try {
            masterDataDaoService.getAllProductSubcategory()
                ?.let { return ServerResponse.ok().body(it) }
                ?: throw NullValueNotAllowedException("No Standard Product Category found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }
    }

    fun getAllLaboratoryList(req: ServerRequest): ServerResponse {
        return try {
//            val page = commonDaoServices.extractPageRequest(req)
            marketSurveillanceFuelDaoServices.getAllLaboratoryList()
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    /*******************************************************************************************************************************************************************************************************************************************************************************************/
                                /**************************************
                                 ******THE START OF MS FUEL DETAILS*******
                                 **************************************/
    /*******************************************************************************************************************************************************************************************************************************************************************************************/


    fun getAllFuelBatchList(req: ServerRequest): ServerResponse {
        return try {
            val page = commonDaoServices.extractPageRequest(req)
            marketSurveillanceFuelDaoServices.getAllFuelBatchList(page)
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun getAllFuelInspectionList(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val page = commonDaoServices.extractPageRequest(req)
            marketSurveillanceFuelDaoServices.getAllFuelInspectionListBasedOnBatchRefNo(batchReferenceNo,page)
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun getFuelInspectionDetails(req: ServerRequest): ServerResponse {
        return try {
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required  batchReferenceNo, check parameters")
            marketSurveillanceFuelDaoServices.getFuelInspectionDetailsBasedOnRefNo(referenceNo, batchReferenceNo)
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun saveNewFuelScheduleBatch(req: ServerRequest): ServerResponse {
        return try {
            val body = req.body<BatchFileFuelSaveDto>()
            val errors: Errors = BeanPropertyBindingResult(body, BatchFileFuelSaveDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    val page = commonDaoServices.extractPageRequest(req)
                    marketSurveillanceFuelDaoServices.createNewFuelBatch(body,page)
                        .let {
                            ServerResponse.ok().body(it)
                        }
                }
                else -> {
                    onValidationErrors(errors)
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun closeFuelBatchEntry(req: ServerRequest): ServerResponse {
        return try {
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val page = commonDaoServices.extractPageRequest(req)
            marketSurveillanceFuelDaoServices.closeFuelBatchCreated(referenceNo,page)
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun saveNewFuelSchedule(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val body = req.body<FuelEntityDto>()
            val errors: Errors = BeanPropertyBindingResult(body, FuelEntityDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    val page = commonDaoServices.extractPageRequest(req)
                    marketSurveillanceFuelDaoServices.createNewFuelSchedule(body, batchReferenceNo, page)
                        .let {
                            ServerResponse.ok().body(it)
                        }
                }
                else -> {
                    onValidationErrors(errors)
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun updateFuelScheduleAssignOfficer(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val body = req.body<FuelEntityAssignOfficerDto>()
            val errors: Errors = BeanPropertyBindingResult(body, FuelEntityAssignOfficerDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceFuelDaoServices.getFuelInspectionDetailsAssignOfficer(referenceNo,batchReferenceNo,body)
                        .let {
                            ServerResponse.ok().body(it)
                        }
                }
                else -> {
                    onValidationErrors(errors)
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun setFuelScheduleRapidTest(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val body = req.body<FuelEntityRapidTestDto>()
            val errors: Errors = BeanPropertyBindingResult(body, FuelEntityRapidTestDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceFuelDaoServices.postFuelInspectionDetailsRapidTest(referenceNo,batchReferenceNo,body)
                        .let {
                            ServerResponse.ok().body(it)
                        }
                }
                else -> {
                    onValidationErrors(errors)
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun setFuelScheduleSampleCollection(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val body = req.body<SampleCollectionDto>()
            val errors: Errors = BeanPropertyBindingResult(body, SampleCollectionDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceFuelDaoServices.postFuelInspectionDetailsSampleCollection(referenceNo,batchReferenceNo,body)
                        .let {
                            ServerResponse.ok().body(it)
                        }
                }
                else -> {
                    onValidationErrors(errors)
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun setFuelScheduleSampleSubmission(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val body = req.body<SampleSubmissionDto>()
            val errors: Errors = BeanPropertyBindingResult(body, SampleSubmissionDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceFuelDaoServices.postFuelInspectionDetailsSampleSubmission(referenceNo,batchReferenceNo,body)
                        .let {
                            ServerResponse.ok().body(it)
                        }
                }
                else -> {
                    onValidationErrors(errors)
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun setFuelScheduleSampleSubmissionBsNumber(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val body = req.body<BSNumberSaveDto>()
            val errors: Errors = BeanPropertyBindingResult(body, BSNumberSaveDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceFuelDaoServices.postFuelInspectionDetailsSampleSubmissionBSNumber(referenceNo,batchReferenceNo,body)
                        .let {
                            ServerResponse.ok().body(it)
                        }
                }
                else -> {
                    onValidationErrors(errors)
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun saveFuelScheduleLabResultsPDFSelected(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val body = req.body<PDFSaveComplianceStatusDto>()
            val errors: Errors = BeanPropertyBindingResult(body, PDFSaveComplianceStatusDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceFuelDaoServices.postFuelInspectionDetailsLabPDFSelected(referenceNo,batchReferenceNo,body)
                        .let {
                            ServerResponse.ok().body(it)
                        }
                }
                else -> {
                    onValidationErrors(errors)
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun saveFuelScheduleSSFComplianceStatusAdd(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val body = req.body<SSFSaveComplianceStatusDto>()
            val errors: Errors = BeanPropertyBindingResult(body, SSFSaveComplianceStatusDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceFuelDaoServices.postFuelInspectionDetailsSSFSaveComplianceStatus(referenceNo,batchReferenceNo,body)
                        .let {
                            ServerResponse.ok().body(it)
                        }
                }
                else -> {
                    onValidationErrors(errors)
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun saveFuelScheduledRemediationInvoice(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val body = req.body<CompliantRemediationDto>()
            val errors: Errors = BeanPropertyBindingResult(body, CompliantRemediationDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceFuelDaoServices.postFuelInspectionDetailsRemediationInvoice(referenceNo,batchReferenceNo,body)
                        .let {
                            ServerResponse.ok().body(it)
                        }
                }
                else -> {
                    onValidationErrors(errors)
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun saveFuelScheduleRemediationAfterPayment(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val body = req.body<CompliantRemediationDto>()
            val errors: Errors = BeanPropertyBindingResult(body, CompliantRemediationDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceFuelDaoServices.postFuelInspectionDetailsScheduleRemediationAfterPayment(referenceNo,batchReferenceNo,body)
                        .let {
                            ServerResponse.ok().body(it)
                        }
                }
                else -> {
                    onValidationErrors(errors)
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun saveFuelScheduleUpdateRemediation(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val body = req.body<RemediationDto>()
            val errors: Errors = BeanPropertyBindingResult(body, RemediationDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceFuelDaoServices.postFuelInspectionDetailsUpdateRemediation(referenceNo,batchReferenceNo,body)
                        .let {
                            ServerResponse.ok().body(it)
                        }
                }
                else -> {
                    onValidationErrors(errors)
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun endFuelInspection(req: ServerRequest): ServerResponse {
        return try {
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required  batchReferenceNo, check parameters")
            marketSurveillanceFuelDaoServices.endFuelInspectionDetailsBasedOnRefNo(referenceNo, batchReferenceNo)
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }



    /*******************************************************************************************************************************************************************************************************************************************************************************************/
                                            /**************************************
                                             ******THE START OF MS COMPLAINT*******
                                             **************************************/
    /*******************************************************************************************************************************************************************************************************************************************************************************************/


    fun saveNewComplaint(req: ServerRequest): ServerResponse {
        return try {
//            val body = req.body<NewComplaintDto>()
            val docFile = req.paramOrNull("docFile") ?: throw ExpectedDataNotFound("Required  docFile, check parameters")
            val data = req.paramOrNull("data") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val gson = Gson()
            val body = gson.fromJson(data, NewComplaintDto::class.java)
//            val referenceNo = req.headers().contentType().MultipartFile("docFile") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val errors: Errors = BeanPropertyBindingResult(body, NewComplaintDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    val page = commonDaoServices.extractPageRequest(req)
                    marketSurveillanceComplaintDaoServices.saveNewComplaint(body,docFile as List<MultipartFile>)
                        .let {
                            ServerResponse.ok().body(it)
                        }
                }
                else -> {
                    onValidationErrors(errors)
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }


    fun getAllComplaintList(req: ServerRequest): ServerResponse {
        return try {
            val page = commonDaoServices.extractPageRequest(req)
            marketSurveillanceComplaintDaoServices.msComplaintLists(page)
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun getAllComplaintOnGoingList(req: ServerRequest): ServerResponse {
        return try {
            val page = commonDaoServices.extractPageRequest(req)
            marketSurveillanceComplaintDaoServices.msComplaintOnGoingLists(page)
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun getAllComplaintNewList(req: ServerRequest): ServerResponse {
        return try {
            val page = commonDaoServices.extractPageRequest(req)
            marketSurveillanceComplaintDaoServices.msComplaintNewLists(page)
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun getAllComplaintCompletedList(req: ServerRequest): ServerResponse {
        return try {
            val page = commonDaoServices.extractPageRequest(req)
            marketSurveillanceComplaintDaoServices.msComplaintAllCompletedLists(page)
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun getAllComplaintMyTaskList(req: ServerRequest): ServerResponse {
        return try {
            val page = commonDaoServices.extractPageRequest(req)
            marketSurveillanceComplaintDaoServices.msComplaintMyTaskLists(page)
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun getComplaintDetails(req: ServerRequest): ServerResponse {
        return try {
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            marketSurveillanceComplaintDaoServices.getComplaintDetailsBasedOnRefNo(referenceNo)
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun updateComplaintByAccepting(req: ServerRequest): ServerResponse {
        return try {
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val body = req.body<ComplaintApproveDto>()
            val errors: Errors = BeanPropertyBindingResult(body, ComplaintApproveDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceComplaintDaoServices.updateComplaintAcceptStatus(referenceNo,body)
                        .let {
                            ServerResponse.ok().body(it)
                        }
                }
                else -> {
                    onValidationErrors(errors)
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun updateComplaintByRejecting(req: ServerRequest): ServerResponse {
        return try {
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val body = req.body<ComplaintRejectDto>()
            val errors: Errors = BeanPropertyBindingResult(body, ComplaintRejectDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceComplaintDaoServices.updateComplaintRejectStatus(referenceNo,body)
                        .let {
                            ServerResponse.ok().body(it)
                        }
                }
                else -> {
                    onValidationErrors(errors)
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun updateComplaintByMandatedForOga(req: ServerRequest): ServerResponse {
        return try {
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val body = req.body<ComplaintAdviceRejectDto>()
            val errors: Errors = BeanPropertyBindingResult(body, ComplaintAdviceRejectDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceComplaintDaoServices.updateComplaintMandateForOgaStatus(referenceNo,body)
                        .let {
                            ServerResponse.ok().body(it)
                        }
                }
                else -> {
                    onValidationErrors(errors)
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun updateComplaintByAssigningHof(req: ServerRequest): ServerResponse {
        return try {
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val body = req.body<ComplaintAssignDto>()
            val errors: Errors = BeanPropertyBindingResult(body, ComplaintAssignDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceComplaintDaoServices.updateComplaintAssignHOFStatus(referenceNo,body)
                        .let {
                            ServerResponse.ok().body(it)
                        }
                }
                else -> {
                    onValidationErrors(errors)
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }



    fun updateComplaintByAssigningIO(req: ServerRequest): ServerResponse {
        return try {
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val body = req.body<ComplaintAssignDto>()
            val errors: Errors = BeanPropertyBindingResult(body, ComplaintAssignDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceComplaintDaoServices.updateComplaintAssignIOStatus(referenceNo,body)
                        .let {
                            ServerResponse.ok().body(it)
                        }
                }
                else -> {
                    onValidationErrors(errors)
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }


    fun updateComplaintByAddingClassificationDetails(req: ServerRequest): ServerResponse {
        return try {
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val body = req.body<ComplaintClassificationDto>()
            val errors: Errors = BeanPropertyBindingResult(body, ComplaintClassificationDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceComplaintDaoServices.updateComplaintByAddingClassification(referenceNo,body)
                        .let {
                            ServerResponse.ok().body(it)
                        }
                }
                else -> {
                    onValidationErrors(errors)
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun addComplaintToWorkPlanDetails(req: ServerRequest): ServerResponse {
        return try {
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val body = req.body<WorkPlanEntityDto>()
            val errors: Errors = BeanPropertyBindingResult(body, WorkPlanEntityDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceComplaintDaoServices.createNewWorkPlanScheduleFromComplaint(referenceNo,body)
                        .let {
                            ServerResponse.ok().body(it)
                        }
                }
                else -> {
                    onValidationErrors(errors)
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }


    fun updateComplaintByStartingMsProcess(req: ServerRequest): ServerResponse {
        return try {
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val body = req.body<ComplaintClassificationDto>()
            val errors: Errors = BeanPropertyBindingResult(body, ComplaintClassificationDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceComplaintDaoServices.updateComplaintByAddingClassification(referenceNo,body)
                        .let {
                            ServerResponse.ok().body(it)
                        }
                }
                else -> {
                    onValidationErrors(errors)
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    /*******************************************************************************************************************************************************************************************************************************************************************************************/
                                        /**************************************
                                         ******THE START OF MS WORK-PLAN*******
                                         **************************************/
    /*******************************************************************************************************************************************************************************************************************************************************************************************/


    fun getAllWorkPlanBatchList(req: ServerRequest): ServerResponse {
        return try {
            val page = commonDaoServices.extractPageRequest(req)
            marketSurveillanceWorkPlanDaoServices.getAllWorkPlanBatchList(page)
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun getAllWorkPlanBatchListClosed(req: ServerRequest): ServerResponse {
        return try {
            val page = commonDaoServices.extractPageRequest(req)
            marketSurveillanceWorkPlanDaoServices.getAllWorkPlanBatchListClosed(page)
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun getAllWorkPlanBatchListOpen(req: ServerRequest): ServerResponse {
        return try {
            val page = commonDaoServices.extractPageRequest(req)
            marketSurveillanceWorkPlanDaoServices.getAllWorkPlanBatchListOpen(page)
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun saveNewWorkPlanBatch(req: ServerRequest): ServerResponse {
        return try {
            val page = commonDaoServices.extractPageRequest(req)
            marketSurveillanceWorkPlanDaoServices.createNewWorkPlanBatch(page)
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun updateWorkPlanByAssigningIO(req: ServerRequest): ServerResponse {
        return try {
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required  batchReferenceNo, check parameters")
            val body = req.body<ComplaintAssignDto>()
            val errors: Errors = BeanPropertyBindingResult(body, ComplaintAssignDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceWorkPlanDaoServices.updateWorkPlanScheduleAssignIOStatus(referenceNo,batchReferenceNo,body)
                        .let {
                            ServerResponse.ok().body(it)
                        }
                }
                else -> {
                    onValidationErrors(errors)
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun updateWorkPlanByAssigningHof(req: ServerRequest): ServerResponse {
        return try {
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required  batchReferenceNo, check parameters")
            val body = req.body<ComplaintAssignDto>()
            val errors: Errors = BeanPropertyBindingResult(body, ComplaintAssignDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceWorkPlanDaoServices.updateWorkPlanScheduleAssignIOStatus(referenceNo,batchReferenceNo,body)
                        .let {
                            ServerResponse.ok().body(it)
                        }
                }
                else -> {
                    onValidationErrors(errors)
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun closeWorkPlanBatchEntry(req: ServerRequest): ServerResponse {
        return try {
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val page = commonDaoServices.extractPageRequest(req)
            marketSurveillanceWorkPlanDaoServices.closeWorkPlanBatchCreated(referenceNo,page)
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }


    fun saveNewWorkPlanSchedule(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val body = req.body<WorkPlanEntityDto>()
            val errors: Errors = BeanPropertyBindingResult(body, FuelEntityDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    val page = commonDaoServices.extractPageRequest(req)
                    marketSurveillanceWorkPlanDaoServices.createNewWorkPlanSchedule(body, batchReferenceNo, page)
                        .let {
                            ServerResponse.ok().body(it)
                        }
                }
                else -> {
                    onValidationErrors(errors)
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }


    fun getAllWorkPlanList(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val page = commonDaoServices.extractPageRequest(req)
            marketSurveillanceWorkPlanDaoServices.getAllWorPlanInspectionListBasedOnBatchRefNo(batchReferenceNo,page)
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

        fun getAllWorkPlanOnGoingList(req: ServerRequest): ServerResponse {
        return try {
            val page = commonDaoServices.extractPageRequest(req)
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            marketSurveillanceWorkPlanDaoServices.getAllWorPlanInspectionAllNotCompletedLists(batchReferenceNo,page)
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

//    fun getAllWorkPlanNewList(req: ServerRequest): ServerResponse {
//        return try {
//            val page = commonDaoServices.extractPageRequest(req)
//            marketSurveillanceWorkPlanDaoServices.msComplaintNewLists(page)
//                .let {
//                    ServerResponse.ok().body(it)
//                }
//        } catch (e: Exception) {
//            KotlinLogging.logger { }.error(e.message)
//            KotlinLogging.logger { }.debug(e.message, e)
//            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
//        }
//    }

    fun getAllWorkPlanCompletedList(req: ServerRequest): ServerResponse {
        return try {
            val page = commonDaoServices.extractPageRequest(req)
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            marketSurveillanceWorkPlanDaoServices.getAllWorPlanInspectionAllCompletedLists(batchReferenceNo,page)
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun getAllWorkPlanMyTaskList(req: ServerRequest): ServerResponse {
        return try {
            val page = commonDaoServices.extractPageRequest(req)
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            marketSurveillanceWorkPlanDaoServices.getAllWorPlanInspectionListMyTask(batchReferenceNo,page)
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }


    fun getWorkPlanInspectionDetails(req: ServerRequest): ServerResponse {
        return try {
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required  batchReferenceNo, check parameters")
            marketSurveillanceWorkPlanDaoServices.getWorkPlanScheduleInspectionDetailsBasedOnRefNo(referenceNo, batchReferenceNo)
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun startWorkPlanInspectionOnsiteDetails(req: ServerRequest): ServerResponse {
        return try {
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required  batchReferenceNo, check parameters")
            marketSurveillanceWorkPlanDaoServices.startWorkPlanScheduleInspectionOnsiteDetailsBasedOnRefNo(referenceNo, batchReferenceNo)
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }
    fun endWorkPlanInspectionOnsiteDetails(req: ServerRequest): ServerResponse {
        return try {
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required  batchReferenceNo, check parameters")
            marketSurveillanceWorkPlanDaoServices.endWorkPlanScheduleInspectionOnsiteDetailsBasedOnRefNo(referenceNo, batchReferenceNo)
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun submitWorkPlanScheduleEntry(req: ServerRequest): ServerResponse {
        return try {

            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val page = commonDaoServices.extractPageRequest(req)
            marketSurveillanceWorkPlanDaoServices.submitWorkPlanScheduleCreated(batchReferenceNo,referenceNo,page)
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun updateWorkPlanScheduleApproval(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val body = req.body<WorkPlanScheduleApprovalDto>()
            val errors: Errors = BeanPropertyBindingResult(body, WorkPlanScheduleApprovalDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceWorkPlanDaoServices.updateWorkPlanScheduleInspectionDetailsApprovalStatus(referenceNo,batchReferenceNo,body)
                        .let {
                            ServerResponse.ok().body(it)
                        }
                }
                else -> {
                    onValidationErrors(errors)
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun updateWorkPlanClientAppealed(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val body = req.body<ApprovalDto>()
            val errors: Errors = BeanPropertyBindingResult(body, ApprovalDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceWorkPlanDaoServices.updateWorkPlanScheduleInspectionDetailsClientAppealed(referenceNo,batchReferenceNo,body)
                        .let {
                            ServerResponse.ok().body(it)
                        }
                }
                else -> {
                    onValidationErrors(errors)
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun updateWorkPlanClientAppealSuccesful(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val body = req.body<ApprovalDto>()
            val errors: Errors = BeanPropertyBindingResult(body, ApprovalDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceWorkPlanDaoServices.updateWorkPlanScheduleInspectionDetailsClientAppealSuccessfully(referenceNo,batchReferenceNo,body)
                        .let {
                            ServerResponse.ok().body(it)
                        }
                }
                else -> {
                    onValidationErrors(errors)
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun updateWorkPlanScheduleApprovalPreliminaryReportHOF(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val body = req.body<ApprovalDto>()
            val errors: Errors = BeanPropertyBindingResult(body, ApprovalDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceWorkPlanDaoServices.updateWorkPlanScheduleInspectionDetailsApprovalPreliminaryReportHOF(referenceNo,batchReferenceNo,body)
                        .let {
                            ServerResponse.ok().body(it)
                        }
                }
                else -> {
                    onValidationErrors(errors)
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun updateWorkPlanScheduleApprovalPreliminaryReportHOD(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val body = req.body<ApprovalDto>()
            val errors: Errors = BeanPropertyBindingResult(body, ApprovalDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceWorkPlanDaoServices.updateWorkPlanScheduleInspectionDetailsApprovalPreliminaryReportHOD(referenceNo,batchReferenceNo,body)
                        .let {
                            ServerResponse.ok().body(it)
                        }
                }
                else -> {
                    onValidationErrors(errors)
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun addWorkPlanScheduleFinalRecommendationByHOD(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val body = req.body<WorkPlanFinalRecommendationDto>()
            val errors: Errors = BeanPropertyBindingResult(body, WorkPlanFinalRecommendationDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceWorkPlanDaoServices.addWorkPlanScheduleFinalRecommendationByHOD(referenceNo,batchReferenceNo,body)
                        .let {
                            ServerResponse.ok().body(it)
                        }
                }
                else -> {
                    onValidationErrors(errors)
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }
    fun addWorkPlanScheduleFeedBackByHOD(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val body = req.body<WorkPlanFeedBackDto>()
            val errors: Errors = BeanPropertyBindingResult(body, WorkPlanFeedBackDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceWorkPlanDaoServices.addWorkPlanScheduleFeedBackByHOD(referenceNo,batchReferenceNo,body)
                        .let {
                            ServerResponse.ok().body(it)
                        }
                }
                else -> {
                    onValidationErrors(errors)
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun addWorkPlanScheduleChargeSheet(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val body = req.body<ChargeSheetDto>()
            val errors: Errors = BeanPropertyBindingResult(body, ChargeSheetDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceWorkPlanDaoServices.addWorkPlanInspectionDetailsChargeSheet(referenceNo,batchReferenceNo,body)
                        .let {
                            ServerResponse.ok().body(it)
                        }
                }
                else -> {
                    onValidationErrors(errors)
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun addWorkPlanDataReportSheet(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val body = req.body<DataReportDto>()
            val errors: Errors = BeanPropertyBindingResult(body, DataReportDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceWorkPlanDaoServices.addWorkPlanInspectionDetailsDataReport(referenceNo,batchReferenceNo,body)
                        .let {
                            ServerResponse.ok().body(it)
                        }
                }
                else -> {
                    onValidationErrors(errors)
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun addWorkPlanSeizureDeclaration(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val body = req.body<SeizureDeclarationDto>()
            val errors: Errors = BeanPropertyBindingResult(body, SeizureDeclarationDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceWorkPlanDaoServices.addWorkPlanInspectionDetailsSeizureDeclaration(referenceNo,batchReferenceNo,body)
                        .let {
                            ServerResponse.ok().body(it)
                        }
                }
                else -> {
                    onValidationErrors(errors)
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun addWorkPlanInspectionInvestigationReport(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val body = req.body<InspectionInvestigationReportDto>()
            val errors: Errors = BeanPropertyBindingResult(body, InspectionInvestigationReportDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceWorkPlanDaoServices.addWorkPlanInspectionDetailsInspectionInvestigationReport(referenceNo,batchReferenceNo,body)
                        .let {
                            ServerResponse.ok().body(it)
                        }
                }
                else -> {
                    onValidationErrors(errors)
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }


    fun addWorkPlanScheduleSampleCollection(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val body = req.body<SampleCollectionDto>()
            val errors: Errors = BeanPropertyBindingResult(body, SampleCollectionDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceWorkPlanDaoServices.addWorkPlanInspectionDetailsSampleCollection(referenceNo,batchReferenceNo,body)
                        .let {
                            ServerResponse.ok().body(it)
                        }
                }
                else -> {
                    onValidationErrors(errors)
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun addWorkPlanScheduleSampleSubmission(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val body = req.body<SampleSubmissionDto>()
            val errors: Errors = BeanPropertyBindingResult(body, SampleSubmissionDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceWorkPlanDaoServices.addWorkPlanInspectionDetailsSampleSubmission(referenceNo,batchReferenceNo,body)
                        .let {
                            ServerResponse.ok().body(it)
                        }
                }
                else -> {
                    onValidationErrors(errors)
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun addWorkPlanScheduleSampleSubmissionBsNumber(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val body = req.body<BSNumberSaveDto>()
            val errors: Errors = BeanPropertyBindingResult(body, BSNumberSaveDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceWorkPlanDaoServices.addWorkPlanInspectionDetailsSampleSubmissionBSNumber(referenceNo,batchReferenceNo,body)
                        .let {
                            ServerResponse.ok().body(it)
                        }
                }
                else -> {
                    onValidationErrors(errors)
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun saveWorkPlanScheduleLabResultsPDFSelected(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val body = req.body<PDFSaveComplianceStatusDto>()
            val errors: Errors = BeanPropertyBindingResult(body, PDFSaveComplianceStatusDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceWorkPlanDaoServices.addWorkPlanInspectionDetailsLabPDFSelected(referenceNo,batchReferenceNo,body)
                        .let {
                            ServerResponse.ok().body(it)
                        }
                }
                else -> {
                    onValidationErrors(errors)
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun saveWorkPlanScheduleSSFComplianceStatusAdd(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val body = req.body<SSFSaveComplianceStatusDto>()
            val errors: Errors = BeanPropertyBindingResult(body, SSFSaveComplianceStatusDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceWorkPlanDaoServices.addWorkPlanInspectionDetailsSSFSaveComplianceStatus(referenceNo,batchReferenceNo,body)
                        .let {
                            ServerResponse.ok().body(it)
                        }
                }
                else -> {
                    onValidationErrors(errors)
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun addWorkPlanSchedulePreliminaryReport(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val body = req.body<PreliminaryReportDto>()
            val errors: Errors = BeanPropertyBindingResult(body, PreliminaryReportDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceWorkPlanDaoServices.addWorkPlanInspectionDetailsPreliminaryReport(referenceNo,batchReferenceNo,body)
                        .let {
                            ServerResponse.ok().body(it)
                        }
                }
                else -> {
                    onValidationErrors(errors)
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun addWorkPlanScheduleFinalPreliminaryReport(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val body = req.body<PreliminaryReportFinalDto>()
            val errors: Errors = BeanPropertyBindingResult(body, PreliminaryReportFinalDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceWorkPlanDaoServices.updateWorkPlanScheduleInspectionDetailsFinalPreliminaryReport(referenceNo,batchReferenceNo,body)
                        .let {
                            ServerResponse.ok().body(it)
                        }
                }
                else -> {
                    onValidationErrors(errors)
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }



}


