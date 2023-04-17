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


import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.*
import org.kebs.app.kotlin.apollo.api.ports.provided.validation.AbstractValidationHandler
import org.kebs.app.kotlin.apollo.common.dto.ms.*
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.Errors
import org.springframework.validation.Validator
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

    fun msDashBoardDetails(req: ServerRequest): ServerResponse {
        try {
            marketSurveillanceComplaintDaoServices.msDashBoardAllDetails()
                .let {
                    return ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }
    }

    fun msSearchPermitNumberDetails(req: ServerRequest): ServerResponse {
        try {
            val permitNumber = req.paramOrNull("permitNumber") ?: throw ExpectedDataNotFound("Required permitNumber, check parameters")
            marketSurveillanceComplaintDaoServices.msSearchPermitNumber(permitNumber)
                .let {
                    return ServerResponse.ok().body(it)
                }?: throw NullValueNotAllowedException("No Permit number found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }
    }

    fun msSearchUCRNumberDetails(req: ServerRequest): ServerResponse {
        try {
            val ucrNumber = req.paramOrNull("ucrNumber") ?: throw ExpectedDataNotFound("Required ucrNumber, check parameters")
            marketSurveillanceComplaintDaoServices.msSearchUCRNumber(ucrNumber)
                .let {
                    return ServerResponse.ok().body(it)
                }?: throw NullValueNotAllowedException("No UCR Number found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }
    }

    fun msOfficerListDetails(req: ServerRequest): ServerResponse {
        try {
            marketSurveillanceComplaintDaoServices.msOfficerListDetails()
                ?.let {
                    return ServerResponse.ok().body(it)
                }?: throw NullValueNotAllowedException("No Ms Officer list found")
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

    fun msNotificationTaskList(req: ServerRequest): ServerResponse {
        try {
            val map = commonDaoServices.serviceMapDetails(appId)
            marketSurveillanceWorkPlanDaoServices.listMsNotificationTasks(map.inactiveStatus)
                ?.let {
                    return ServerResponse.ok().body(it) }
                ?: throw NullValueNotAllowedException("No Ms Recommendation found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }
    }


    fun msNotificationTaskRead(req: ServerRequest): ServerResponse {
        try {
            val taskRefNumber = req.paramOrNull("taskRefNumber") ?: throw ExpectedDataNotFound("Required taskRefNumber, check parameters")
            marketSurveillanceWorkPlanDaoServices.updateTaskRead(taskRefNumber)
                ?.let {
                    return ServerResponse.ok().body(it) }
                ?: throw NullValueNotAllowedException("No Ms Notification found found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }
    }


    fun msDivisions(req: ServerRequest): ServerResponse {
        try {
            masterDataDaoService.getAllDivisions()
                ?.let { return ServerResponse.ok().body(it) }
                ?: throw NullValueNotAllowedException("No Ms Departments found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }
    }

//    fun msOfficerList(req: ServerRequest): ServerResponse {
//        try {
//            masterDataDaoService.msOfficerListDetails()
//                ?.let { return ServerResponse.ok().body(it) }
//                ?: throw NullValueNotAllowedException("No Ms Departments found")
//        } catch (e: Exception) {
//            KotlinLogging.logger { }.error(e.message)
//            KotlinLogging.logger { }.debug(e.message, e)
//            return ServerResponse.badRequest().body(e.message ?: "Unknown Error")
//        }
//    }

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

    fun msLaboratories(req: ServerRequest): ServerResponse {
        try {
            masterDataDaoService.getAllLaboratories()
                ?.let { return ServerResponse.ok().body(it) }
                ?: throw NullValueNotAllowedException("No Laboratory found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }
    }
    fun msNonCompliance(req: ServerRequest): ServerResponse {
        try {
            masterDataDaoService.getAllNonCompliance()
                ?.let { return ServerResponse.ok().body(it) }
                ?: throw NullValueNotAllowedException("No Non Compliance found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }
    }

    fun countiesListingAdmin(req: ServerRequest): ServerResponse {
        try {
            masterDataDaoService.getAllCounties()
                ?.let { return ServerResponse.ok().body(it) }
                ?: throw NullValueNotAllowedException("No Town List found")

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }

    fun townsListingAdmin(req: ServerRequest): ServerResponse {
        try {
            masterDataDaoService.getAllTowns()
                ?.let { return ServerResponse.ok().body(it) }
                ?: throw NullValueNotAllowedException("No Town List found")

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }

    fun regionListingAdmin(req: ServerRequest): ServerResponse {
        try {
            masterDataDaoService.getAllRegions()
                ?.let { return ServerResponse.ok().body(it) }
                ?: throw NullValueNotAllowedException("No Town List found")

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }


    fun standardsList(req: ServerRequest): ServerResponse {
        try {
            masterDataDaoService.getAllStandardsDetails()
                ?.let { return ServerResponse.ok().body(it) }
                ?: throw NullValueNotAllowedException("No Standards List found")

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }

    }

    fun msPredefinedResources(req: ServerRequest): ServerResponse {
        try {
            masterDataDaoService.getAllPredefinedResourcesRequired()
                ?.let { return ServerResponse.ok().body(it) }
                ?: throw NullValueNotAllowedException("No Predefined Resources Required found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }
    }

    fun msOGAList(req: ServerRequest): ServerResponse {
        try {
            masterDataDaoService.getAllOGAList()
                ?.let { return ServerResponse.ok().body(it) }
                ?: throw NullValueNotAllowedException("No OGA found")
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
            val teamsReferenceNo = req.paramOrNull("teamsReferenceNo") ?: throw ExpectedDataNotFound("Required  teamsReferenceNo, check parameters")
            val countyReferenceNo = req.paramOrNull("countyReferenceNo") ?: throw ExpectedDataNotFound("Required  countyReferenceNo, check parameters")
            val page = commonDaoServices.extractPageRequest(req)
            marketSurveillanceFuelDaoServices.getAllFuelInspectionListBasedOnBatchRefNo(batchReferenceNo,teamsReferenceNo,countyReferenceNo,page)
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun getFuelBatchTeamsList(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val page = commonDaoServices.extractPageRequest(req)
            marketSurveillanceFuelDaoServices.getFuelTeamsList(batchReferenceNo,page)
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun getFuelInspectionTeamsDetails(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required  batchReferenceNo, check parameters")
            val teamsReferenceNo = req.paramOrNull("teamsReferenceNo") ?: throw ExpectedDataNotFound("Required  teamsReferenceNo, check parameters")
            marketSurveillanceFuelDaoServices.getFuelInspectionTeamsDetailsBasedOnRefNo(batchReferenceNo,teamsReferenceNo)
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
            val teamsReferenceNo = req.paramOrNull("teamsReferenceNo") ?: throw ExpectedDataNotFound("Required  teamsReferenceNo, check parameters")
            val countyReferenceNo = req.paramOrNull("countyReferenceNo") ?: throw ExpectedDataNotFound("Required  countyReferenceNo, check parameters")
            marketSurveillanceFuelDaoServices.getFuelInspectionDetailsBasedOnRefNo(referenceNo, batchReferenceNo,teamsReferenceNo,countyReferenceNo)
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

    fun saveNewFuelScheduleTeam(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val body = req.body<TeamsFuelSaveDto>()
            val errors: Errors = BeanPropertyBindingResult(body, BatchFileFuelSaveDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    val page = commonDaoServices.extractPageRequest(req)
                    marketSurveillanceFuelDaoServices.createNewFuelTeams(body,batchReferenceNo,page)
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

    fun saveNewFuelSchedule(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val teamsReferenceNo = req.paramOrNull("teamsReferenceNo") ?: throw ExpectedDataNotFound("Required  teamsReferenceNo, check parameters")
            val countyReferenceNo = req.paramOrNull("countyReferenceNo") ?: throw ExpectedDataNotFound("Required  countyReferenceNo, check parameters")
            val body = req.body<FuelEntityDto>()
            val errors: Errors = BeanPropertyBindingResult(body, FuelEntityDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    val page = commonDaoServices.extractPageRequest(req)
                    marketSurveillanceFuelDaoServices.createNewFuelSchedule(body, batchReferenceNo,teamsReferenceNo,countyReferenceNo, page)
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
            val teamsReferenceNo = req.paramOrNull("teamsReferenceNo") ?: throw ExpectedDataNotFound("Required  teamsReferenceNo, check parameters")
            val countyReferenceNo = req.paramOrNull("countyReferenceNo") ?: throw ExpectedDataNotFound("Required  countyReferenceNo, check parameters")
            val body = req.body<FuelEntityAssignOfficerDto>()
            val errors: Errors = BeanPropertyBindingResult(body, FuelEntityAssignOfficerDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceFuelDaoServices.getFuelInspectionDetailsAssignOfficer(referenceNo,batchReferenceNo,teamsReferenceNo,countyReferenceNo,body)
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
            val teamsReferenceNo = req.paramOrNull("teamsReferenceNo") ?: throw ExpectedDataNotFound("Required  teamsReferenceNo, check parameters")
            val countyReferenceNo = req.paramOrNull("countyReferenceNo") ?: throw ExpectedDataNotFound("Required  countyReferenceNo, check parameters")
            val body = req.body<FuelEntityRapidTestDto>()
            val errors: Errors = BeanPropertyBindingResult(body, FuelEntityRapidTestDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceFuelDaoServices.postFuelInspectionDetailsRapidTest(referenceNo,batchReferenceNo,teamsReferenceNo,countyReferenceNo,body)
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

    fun addFuelScheduleRapidTestProducts(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val teamsReferenceNo = req.paramOrNull("teamsReferenceNo") ?: throw ExpectedDataNotFound("Required  teamsReferenceNo, check parameters")
            val countyReferenceNo = req.paramOrNull("countyReferenceNo") ?: throw ExpectedDataNotFound("Required  countyReferenceNo, check parameters")
            val body = req.body<RapidTestProductsDto>()
            val errors: Errors = BeanPropertyBindingResult(body, RapidTestProductsDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceFuelDaoServices.postFuelInspectionDetailsRapidTestProducts(referenceNo,batchReferenceNo,teamsReferenceNo,countyReferenceNo,body)
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
            val teamsReferenceNo = req.paramOrNull("teamsReferenceNo") ?: throw ExpectedDataNotFound("Required  teamsReferenceNo, check parameters")
            val countyReferenceNo = req.paramOrNull("countyReferenceNo") ?: throw ExpectedDataNotFound("Required  countyReferenceNo, check parameters")
            val body = req.body<SampleCollectionDto>()
            val errors: Errors = BeanPropertyBindingResult(body, SampleCollectionDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceFuelDaoServices.postFuelInspectionDetailsSampleCollection(referenceNo,batchReferenceNo,teamsReferenceNo,countyReferenceNo,body)
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
            val teamsReferenceNo = req.paramOrNull("teamsReferenceNo") ?: throw ExpectedDataNotFound("Required  teamsReferenceNo, check parameters")
            val countyReferenceNo = req.paramOrNull("countyReferenceNo") ?: throw ExpectedDataNotFound("Required  countyReferenceNo, check parameters")
            val body = req.body<SampleSubmissionDto>()
            val errors: Errors = BeanPropertyBindingResult(body, SampleSubmissionDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceFuelDaoServices.postFuelInspectionDetailsSampleSubmission(referenceNo,batchReferenceNo,teamsReferenceNo,countyReferenceNo,body)
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

    fun endsSampleSubmissionAdding(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val teamsReferenceNo = req.paramOrNull("teamsReferenceNo") ?: throw ExpectedDataNotFound("Required  teamsReferenceNo, check parameters")
            val countyReferenceNo = req.paramOrNull("countyReferenceNo") ?: throw ExpectedDataNotFound("Required  countyReferenceNo, check parameters")
            marketSurveillanceFuelDaoServices.postFuelInspectionEndSampleSubmission(referenceNo,batchReferenceNo,teamsReferenceNo,countyReferenceNo)
                .let {
                    ServerResponse.ok().body(it)
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
            val teamsReferenceNo = req.paramOrNull("teamsReferenceNo") ?: throw ExpectedDataNotFound("Required  teamsReferenceNo, check parameters")
            val countyReferenceNo = req.paramOrNull("countyReferenceNo") ?: throw ExpectedDataNotFound("Required  countyReferenceNo, check parameters")
            val body = req.body<BSNumberSaveDto>()
            val errors: Errors = BeanPropertyBindingResult(body, BSNumberSaveDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceFuelDaoServices.postFuelInspectionDetailsSampleSubmissionBSNumber(referenceNo,batchReferenceNo,teamsReferenceNo,countyReferenceNo,body)
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

    fun setFuelScheduleEndSampleSubmissionBsNumber(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val teamsReferenceNo = req.paramOrNull("teamsReferenceNo") ?: throw ExpectedDataNotFound("Required  teamsReferenceNo, check parameters")
            val countyReferenceNo = req.paramOrNull("countyReferenceNo") ?: throw ExpectedDataNotFound("Required  countyReferenceNo, check parameters")
            marketSurveillanceFuelDaoServices.postFuelInspectionDetailsEndSampleSubmissionBSNumber(referenceNo,batchReferenceNo,teamsReferenceNo,countyReferenceNo)
                .let {
                    ServerResponse.ok().body(it)
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
            val teamsReferenceNo = req.paramOrNull("teamsReferenceNo") ?: throw ExpectedDataNotFound("Required  teamsReferenceNo, check parameters")
            val countyReferenceNo = req.paramOrNull("countyReferenceNo") ?: throw ExpectedDataNotFound("Required  countyReferenceNo, check parameters")
            val body = req.body<PDFSaveComplianceStatusDto>()
            val errors: Errors = BeanPropertyBindingResult(body, PDFSaveComplianceStatusDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceFuelDaoServices.postFuelInspectionDetailsLabPDFSelected(referenceNo,batchReferenceNo,teamsReferenceNo,countyReferenceNo,body)
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
            val teamsReferenceNo = req.paramOrNull("teamsReferenceNo") ?: throw ExpectedDataNotFound("Required  teamsReferenceNo, check parameters")
            val countyReferenceNo = req.paramOrNull("countyReferenceNo") ?: throw ExpectedDataNotFound("Required  countyReferenceNo, check parameters")
            val body = req.body<SSFSaveComplianceStatusDto>()
            val errors: Errors = BeanPropertyBindingResult(body, SSFSaveComplianceStatusDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceFuelDaoServices.postFuelInspectionDetailsSSFSaveComplianceStatus(referenceNo,batchReferenceNo,teamsReferenceNo,countyReferenceNo,body)
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

    fun saveFuelScheduleSSFFinalComplianceStatusAdd(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val teamsReferenceNo = req.paramOrNull("teamsReferenceNo") ?: throw ExpectedDataNotFound("Required  teamsReferenceNo, check parameters")
            val countyReferenceNo = req.paramOrNull("countyReferenceNo") ?: throw ExpectedDataNotFound("Required  countyReferenceNo, check parameters")
            val body = req.body<SSFSaveComplianceStatusDto>()
            val errors: Errors = BeanPropertyBindingResult(body, SSFSaveComplianceStatusDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceFuelDaoServices.postFuelInspectionDetailsSSFFinalSaveComplianceStatus(referenceNo,batchReferenceNo,teamsReferenceNo,countyReferenceNo,body)
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
            val teamsReferenceNo = req.paramOrNull("teamsReferenceNo") ?: throw ExpectedDataNotFound("Required  teamsReferenceNo, check parameters")
            val countyReferenceNo = req.paramOrNull("countyReferenceNo") ?: throw ExpectedDataNotFound("Required  countyReferenceNo, check parameters")
            val body = req.body<CompliantRemediationDto>()
            val errors: Errors = BeanPropertyBindingResult(body, CompliantRemediationDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceFuelDaoServices.postFuelInspectionDetailsRemediationInvoice(referenceNo,batchReferenceNo,teamsReferenceNo,countyReferenceNo,body)
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
            val teamsReferenceNo = req.paramOrNull("teamsReferenceNo") ?: throw ExpectedDataNotFound("Required  teamsReferenceNo, check parameters")
            val countyReferenceNo = req.paramOrNull("countyReferenceNo") ?: throw ExpectedDataNotFound("Required  countyReferenceNo, check parameters")
            val body = req.body<CompliantRemediationDto>()
            val errors: Errors = BeanPropertyBindingResult(body, CompliantRemediationDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceFuelDaoServices.postFuelInspectionDetailsScheduleRemediationAfterPayment(
                        referenceNo,teamsReferenceNo,countyReferenceNo,batchReferenceNo,body
                    )
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
            val teamsReferenceNo = req.paramOrNull("teamsReferenceNo") ?: throw ExpectedDataNotFound("Required  teamsReferenceNo, check parameters")
            val countyReferenceNo = req.paramOrNull("countyReferenceNo") ?: throw ExpectedDataNotFound("Required  countyReferenceNo, check parameters")
            val body = req.body<RemediationDto>()
            val errors: Errors = BeanPropertyBindingResult(body, RemediationDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceFuelDaoServices.postFuelInspectionDetailsUpdateRemediation(referenceNo,batchReferenceNo,teamsReferenceNo,countyReferenceNo,body)
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
            val teamsReferenceNo = req.paramOrNull("teamsReferenceNo") ?: throw ExpectedDataNotFound("Required  teamsReferenceNo, check parameters")
            val countyReferenceNo = req.paramOrNull("countyReferenceNo") ?: throw ExpectedDataNotFound("Required  countyReferenceNo, check parameters")
            val body = req.body<EndFuelDto>()
            val errors: Errors = BeanPropertyBindingResult(body, EndFuelDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceFuelDaoServices.endFuelInspectionDetailsBasedOnRefNo(referenceNo, batchReferenceNo,teamsReferenceNo,countyReferenceNo,body)
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
                                             ******THE START OF MS COMPLAINT*******
                                             **************************************/
    /*******************************************************************************************************************************************************************************************************************************************************************************************/


    fun saveNewComplaint(req: ServerRequest): ServerResponse {
        return try {
            val body = req.body<NewComplaintDto>()
//            val docFile = req.paramOrNull("docFile") ?: throw ExpectedDataNotFound("Required  docFile, check parameters")
//            val data = req.paramOrNull("data") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
//            val gson = Gson()
//            val body = gson.fromJson(data, NewComplaintDto::class.java)
//            val referenceNo = req.headers().contentType().MultipartFile("docFile") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val errors: Errors = BeanPropertyBindingResult(body, NewComplaintDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    val page = commonDaoServices.extractPageRequest(req)
                    marketSurveillanceComplaintDaoServices.saveNewComplaint(body,null)
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

    fun getAllComplaintReportTimeLineList(req: ServerRequest): ServerResponse {
        return try {
            val page = commonDaoServices.extractPageRequest(req, "transactionDate")
            val reportType = req.paramOrNull("reportType") ?: throw ExpectedDataNotFound("Required reportType, check parameters")
            marketSurveillanceComplaintDaoServices.msAllComplaintReportTimeLineLists(page, reportType)
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun getAllConsumerComplaintReportList(req: ServerRequest): ServerResponse {
        return try {
            val page = commonDaoServices.extractPageRequest(req)
            marketSurveillanceComplaintDaoServices.msAllConsumerComplaintReportLists(page)
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun getAllSubmittedSamplesSummaryReportList(req: ServerRequest): ServerResponse {
        return try {
            val page = commonDaoServices.extractPageRequest(req)
            marketSurveillanceComplaintDaoServices.msAllSubmittedSamplesSummaryReportLists(page)
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun getAllFieldInspectionSummaryReportList(req: ServerRequest): ServerResponse {
        return try {
            val page = commonDaoServices.extractPageRequest(req)
            marketSurveillanceComplaintDaoServices.msAllFieldInspectionSummaryReportLists(page)
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun getAllWorkPlanMonitoringToolReportList(req: ServerRequest): ServerResponse {
        return try {
            val page = commonDaoServices.extractPageRequest(req)
            marketSurveillanceComplaintDaoServices.msAllWorkPlanMonitoringToolReportLists(page)
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun getAllSeizedGoodsReportList(req: ServerRequest): ServerResponse {
        return try {
            val page = commonDaoServices.extractPageRequest(req)
            marketSurveillanceComplaintDaoServices.msAllSeizedGoodsReportLists(page)
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun getAllSeizedGoodsViewList(req: ServerRequest): ServerResponse {
        return try {
            val page = commonDaoServices.extractPageRequest(req, "referenceNumber")
//            val reportType = req.paramOrNull("reportType") ?: throw ExpectedDataNotFound("Required reportType, check parameters")
            marketSurveillanceComplaintDaoServices.msSeizedGoodsViewLists(page)
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun putAllComplaintSearchList(req: ServerRequest): ServerResponse {
        return try {
            val page = commonDaoServices.extractPageRequest(req, "transactionDate")
            val reportType = req.paramOrNull("reportType") ?: throw ExpectedDataNotFound("Required reportType, check parameters")
            val body = req.body<ComplaintViewSearchValues>()
            val errors: Errors = BeanPropertyBindingResult(body, ComplaintViewSearchValues::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceComplaintDaoServices.msComplaintViewSearchLists(page,body,reportType)
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

    fun putAllConsumerComplaintSearchList(req: ServerRequest): ServerResponse {
        return try {
            val page = commonDaoServices.extractPageRequest(req)
            val body = req.body<ConsumerComplaintViewSearchValues>()
            val errors: Errors = BeanPropertyBindingResult(body, ConsumerComplaintViewSearchValues::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceComplaintDaoServices.msConsumerComplaintViewSearchLists(page,body)
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

    fun putAllSubmittedSamplesSummarySearchList(req: ServerRequest): ServerResponse {
        return try {
            val page = commonDaoServices.extractPageRequest(req)
            val body = req.body<SubmittedSamplesSummaryViewSearchValues>()
            val errors: Errors = BeanPropertyBindingResult(body, SubmittedSamplesSummaryViewSearchValues::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceComplaintDaoServices.msSubmittedSamplesSummaryViewSearchLists(page,body)
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

    fun putAllFieldInspectionSummarySearchList(req: ServerRequest): ServerResponse {
        return try {
            val page = commonDaoServices.extractPageRequest(req)
            val body = req.body<FieldInspectionSummarySearch>()
            val errors: Errors = BeanPropertyBindingResult(body, FieldInspectionSummarySearch::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceComplaintDaoServices.msFieldInspectionSummaryViewSearchLists(page,body)
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

    fun putAllWorkPlanMonitoringToolSearchList(req: ServerRequest): ServerResponse {
        return try {
            val page = commonDaoServices.extractPageRequest(req)
            val body = req.body<ConsumerComplaintViewSearchValues>()
            val errors: Errors = BeanPropertyBindingResult(body, ConsumerComplaintViewSearchValues::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceComplaintDaoServices.msWorkPlanMonitoringToolViewSearchLists(page,body)
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


    fun putAllSeizedGoodsViewSearchList(req: ServerRequest): ServerResponse {
        return try {
            val page = commonDaoServices.extractPageRequest(req)
            val body = req.body<SeizeViewSearchValues>()
            val errors: Errors = BeanPropertyBindingResult(body, SeizeViewSearchValues::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceComplaintDaoServices.msSeizeViewSearchLists(page,body)
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

    fun putAllSampleProductsSearchList(req: ServerRequest): ServerResponse {
        return try {
            val page = commonDaoServices.extractPageRequest(req, "referenceNumber")
//            val reportType = req.paramOrNull("reportType") ?: throw ExpectedDataNotFound("Required reportType, check parameters")
            val body = req.body<SampleProductViewSearchValues>()
            val errors: Errors = BeanPropertyBindingResult(body, SampleProductViewSearchValues::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceComplaintDaoServices.msSampleProductViewSearchLists(page,body)
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
    fun putAllSeizedGoodsSearchList(req: ServerRequest): ServerResponse {
        return try {
            val page = commonDaoServices.extractPageRequest(req, "referenceNumber")
//            val reportType = req.paramOrNull("reportType") ?: throw ExpectedDataNotFound("Required reportType, check parameters")
            val body = req.body<SeizedGoodsViewSearchValues>()
            val errors: Errors = BeanPropertyBindingResult(body, SeizedGoodsViewSearchValues::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceComplaintDaoServices.msSeizedGoodsViewSearchLists(page,body)
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

    fun getStatusReportComplaintInvestigationList(req: ServerRequest): ServerResponse {
        return try {
            val page = commonDaoServices.extractPageRequest(req, "transactionDate")
            marketSurveillanceComplaintDaoServices.msStatusReportComplaintInvestigationLists(page)
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun getPerformanceOfSelectedProductViewList(req: ServerRequest): ServerResponse {
        return try {
            val page = commonDaoServices.extractPageRequest(req, "referenceNumber")
            marketSurveillanceComplaintDaoServices.msPerformanceOfSelectedProductViewLists(page)
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun getAllComplaintFeedbackReportTimeLineList(req: ServerRequest): ServerResponse {
        return try {
            val page = commonDaoServices.extractPageRequest(req, "transactionDate")
            marketSurveillanceComplaintDaoServices.msComplaintFeedbackReportTimeLineLists(page)
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun getAllReportSubmittedReportTimeLineList(req: ServerRequest): ServerResponse {
        return try {
            val page = commonDaoServices.extractPageRequest(req, "transactionDate")
            marketSurveillanceComplaintDaoServices.msReportSubmittedReportTimeLineLists(page)
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun getAllSampleSubmittedReportTimeLineList(req: ServerRequest): ServerResponse {
        return try {
            val page = commonDaoServices.extractPageRequest(req, "transactionDate")
            marketSurveillanceComplaintDaoServices.msSampleSubmittedReportTimeLineLists(page)
                .let {
                    ServerResponse.ok().body(it)
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

    fun getAllComplaintMyTaskListRegionChanged(req: ServerRequest): ServerResponse {
        return try {
            val page = commonDaoServices.extractPageRequest(req)
            marketSurveillanceComplaintDaoServices.msComplaintNewListsRegionChanged(page)
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


    fun getAllWorkPlanAllocatedTaskList(req: ServerRequest): ServerResponse {
        return try {
            val page = commonDaoServices.extractPageRequest(req,"referenceNumber")
            marketSurveillanceWorkPlanDaoServices.msWorkPlanAllocatedTaskListsView(page, false)
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }


    fun getAllWorkPlanReportsPendingReviewList(req: ServerRequest): ServerResponse {
        return try {
            val page = commonDaoServices.extractPageRequest(req,"referenceNumber")
            marketSurveillanceWorkPlanDaoServices.msWorkPlanReportsPendingReviewListsView(page, false)
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun getAllWorkPlanJuniorTaskOverdueWPReviewList(req: ServerRequest): ServerResponse {
        return try {
            val page = commonDaoServices.extractPageRequest(req,"referenceNumber")
            marketSurveillanceWorkPlanDaoServices.msWorkPlanJuniorTaskOverDueListsView(page, false)
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun getAllWorkPlanJuniorTaskOverdueWPCPReviewList(req: ServerRequest): ServerResponse {
        return try {
            val page = commonDaoServices.extractPageRequest(req,"referenceNumber")
            marketSurveillanceWorkPlanDaoServices.msWorkPlanJuniorTaskOverDueListsView(page, true)
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }


    fun getAllWorkPlanAllocatedTaskOverDueList(req: ServerRequest): ServerResponse {
        return try {
            val page = commonDaoServices.extractPageRequest(req,"referenceNumber")
            marketSurveillanceWorkPlanDaoServices.msWorkPlanAllocatedTaskListsOverDueView(page, false)
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun getAllWorkPlanComplaintAllocatedTaskList(req: ServerRequest): ServerResponse {
        return try {
            val page = commonDaoServices.extractPageRequest(req,"referenceNumber")
            marketSurveillanceWorkPlanDaoServices.msWorkPlanAllocatedTaskListsView(page, true)
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun getAllWorkPlanComplaintReportsPendingReviewList(req: ServerRequest): ServerResponse {
        return try {
            val page = commonDaoServices.extractPageRequest(req,"referenceNumber")
            marketSurveillanceWorkPlanDaoServices.msWorkPlanReportsPendingReviewListsView(page, true)
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun getAllWorkPlanComplaintAllocatedTaskOverDueList(req: ServerRequest): ServerResponse {
        return try {
            val page = commonDaoServices.extractPageRequest(req,"referenceNumber")
            marketSurveillanceWorkPlanDaoServices.msWorkPlanAllocatedTaskListsOverDueView(page, true)
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun getAllComplaintAllocatedTaskList(req: ServerRequest): ServerResponse {
        return try {
            val page = commonDaoServices.extractPageRequest(req,"referenceNumber")
            marketSurveillanceComplaintDaoServices.msComplaintAllocatedTaskListsView(page)
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun getAllComplaintPendingAllocationList(req: ServerRequest): ServerResponse {
        return try {
            val page = commonDaoServices.extractPageRequest(req,"referenceNumber")
            marketSurveillanceComplaintDaoServices.msComplaintPendingAllocationListsView(page)
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun getAllComplaintAllocatedOverDueTaskList(req: ServerRequest): ServerResponse {
        return try {
            val page = commonDaoServices.extractPageRequest(req,"referenceNumber")
            marketSurveillanceComplaintDaoServices.msComplaintOverDueTaskListsView(page)
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

    fun updateComplaintByForAmendmentUser(req: ServerRequest): ServerResponse {
        return try {
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val body = req.body<ComplaintAdviceRejectDto>()
            val errors: Errors = BeanPropertyBindingResult(body, ComplaintAdviceRejectDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceComplaintDaoServices.updateComplaintForAmendmentByUser(referenceNo,body)
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

    fun updateComplaintByReAssigningRegion(req: ServerRequest): ServerResponse {
        return try {
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val body = req.body<RegionReAssignDto>()
            val errors: Errors = BeanPropertyBindingResult(body, RegionReAssignDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceComplaintDaoServices.updateComplaintReAssignRegionStatus(referenceNo,body)
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
            val complaintStatus = req.paramOrNull("complaintStatus") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            marketSurveillanceWorkPlanDaoServices.getAllWorkPlanBatchList(page,commonDaoServices.toBoolean(complaintStatus))
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
            val complaintStatus = req.paramOrNull("complaintStatus") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            marketSurveillanceWorkPlanDaoServices.getAllWorkPlanBatchListClosed(page,commonDaoServices.toBoolean(complaintStatus))
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

//    fun getReassignedComplaints(){
//        println("DO sSOMETHING ABOUT GETTING THE REASSIGNED COMPLAINTS")
//    }

    fun getAllWorkPlanBatchListOpen(req: ServerRequest): ServerResponse {
        return try {
            val page = commonDaoServices.extractPageRequest(req)
            val complaintStatus = req.paramOrNull("complaintStatus") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            marketSurveillanceWorkPlanDaoServices.getAllWorkPlanBatchListOpen(page,commonDaoServices.toBoolean(complaintStatus))
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
            val errors: Errors = BeanPropertyBindingResult(body, WorkPlanEntityDto::class.java.name)
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

    fun updateWorkPlanSchedule(req: ServerRequest): ServerResponse {
        return try {
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required  batchReferenceNo, check parameters")
            val updateDetails = req.paramOrNull("updateDetails") ?: throw ExpectedDataNotFound("Required  updateDetails, check parameters")
            val body = req.body<WorkPlanEntityDto>()
            val errors: Errors = BeanPropertyBindingResult(body, WorkPlanEntityDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    val page = commonDaoServices.extractPageRequest(req)
                    marketSurveillanceWorkPlanDaoServices.updateNewWorkPlanSchedule(body, updateDetails.toInt()==1, batchReferenceNo,referenceNo, page)
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
            val complaintStatus = req.paramOrNull("complaintStatus") ?: throw ExpectedDataNotFound("Required Complaint Status, check parameters")
            val page = commonDaoServices.extractPageRequest(req)
            marketSurveillanceWorkPlanDaoServices.getAllWorPlanInspectionListBasedOnBatchRefNo(batchReferenceNo,commonDaoServices.toBoolean(complaintStatus),page)
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
            val complaintStatus = req.paramOrNull("complaintStatus") ?: throw ExpectedDataNotFound("Required Complaint Status, check parameters")
            marketSurveillanceWorkPlanDaoServices.getAllWorPlanInspectionAllNotCompletedLists(batchReferenceNo,commonDaoServices.toBoolean(complaintStatus),page)
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
            val complaintStatus = req.paramOrNull("complaintStatus") ?: throw ExpectedDataNotFound("Required Complaint Status, check parameters")
            marketSurveillanceWorkPlanDaoServices.getAllWorPlanInspectionAllCompletedLists(batchReferenceNo,commonDaoServices.toBoolean(complaintStatus),page)
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

     fun getAllWorkPlanSameDateList(req: ServerRequest): ServerResponse {
        return try {
            val page = commonDaoServices.extractPageRequest(req)
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val complaintStatus = req.paramOrNull("complaintStatus") ?: throw ExpectedDataNotFound("Required Complaint Status, check parameters")
            marketSurveillanceWorkPlanDaoServices.getAllWorPlanInspectionAllSameDateLists(batchReferenceNo,commonDaoServices.toBoolean(complaintStatus),page)
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
            val complaintStatus = req.paramOrNull("complaintStatus") ?: throw ExpectedDataNotFound("Required Complaint Status, check parameters")
            marketSurveillanceWorkPlanDaoServices.getAllWorPlanInspectionListMyTask(batchReferenceNo,commonDaoServices.toBoolean(complaintStatus),page)
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
            val body = req.body<WorkPlanScheduleOnsiteDto>()
            val errors: Errors = BeanPropertyBindingResult(body, WorkPlanScheduleOnsiteDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceWorkPlanDaoServices.startWorkPlanScheduleInspectionOnsiteDetailsBasedOnRefNo(referenceNo, batchReferenceNo,body)
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

    fun endWorkPlanInspectionAllRecommendationDone(req: ServerRequest): ServerResponse {
        return try {
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required  batchReferenceNo, check parameters")
            marketSurveillanceWorkPlanDaoServices.endWorkPlanScheduleInspectionAllRecommendationDoneDetailsBasedOnRefNo(referenceNo, batchReferenceNo)
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
            val productReferenceNo = req.paramOrNull("productReferenceNo") ?: throw ExpectedDataNotFound("Required  productReferenceNo, check parameters")
            val body = req.body<ApprovalDto>()
            val errors: Errors = BeanPropertyBindingResult(body, ApprovalDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceWorkPlanDaoServices.updateWorkPlanScheduleInspectionDetailsClientAppealed(productReferenceNo,referenceNo,batchReferenceNo,body)
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

    fun updateWorkPlanClientAppealSuccessful(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val productReferenceNo = req.paramOrNull("productReferenceNo") ?: throw ExpectedDataNotFound("Required  productReferenceNo, check parameters")
            val body = req.body<ApprovalDto>()
            val errors: Errors = BeanPropertyBindingResult(body, ApprovalDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceWorkPlanDaoServices.updateWorkPlanScheduleInspectionDetailsClientAppealSuccessfully(productReferenceNo,referenceNo,batchReferenceNo,body)
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

    fun updateWorkPlanScheduleFinalRemarkOnSized(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val body = req.body<WorkPlanFeedBackDto>()
            val errors: Errors = BeanPropertyBindingResult(body, WorkPlanFeedBackDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceWorkPlanDaoServices.workPlanScheduleFinalRemarkOnSized(referenceNo,batchReferenceNo,body)
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
            val finalReportStatus = req.paramOrNull("finalReportStatus") ?: throw ExpectedDataNotFound("Required  finalReportStatus, check parameters")
            val body = req.body<ApprovalDto>()
            val errors: Errors = BeanPropertyBindingResult(body, ApprovalDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceWorkPlanDaoServices.updateWorkPlanScheduleInspectionDetailsApprovalPreliminaryReportHOF(referenceNo,batchReferenceNo,body, finalReportStatus.toInt()==1)
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
            val finalReportStatus = req.paramOrNull("finalReportStatus") ?: throw ExpectedDataNotFound("Required  finalReportStatus, check parameters")
            val body = req.body<ApprovalDto>()
            val errors: Errors = BeanPropertyBindingResult(body, ApprovalDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceWorkPlanDaoServices.updateWorkPlanScheduleInspectionDetailsApprovalPreliminaryReportHOD(referenceNo,batchReferenceNo,body, finalReportStatus.toInt()==1)
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

    fun updateWorkPlanScheduleApprovalPreliminaryReportDirector(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val finalReportStatus = req.paramOrNull("finalReportStatus") ?: throw ExpectedDataNotFound("Required  finalReportStatus, check parameters")
            val body = req.body<ApprovalDto>()
            val errors: Errors = BeanPropertyBindingResult(body, ApprovalDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceWorkPlanDaoServices.updateWorkPlanScheduleInspectionDetailsApprovalPreliminaryReportDirector(referenceNo,batchReferenceNo,body, finalReportStatus.toInt()==1)
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
            val productReferenceNo = req.paramOrNull("productReferenceNo") ?: throw ExpectedDataNotFound("Required  productReferenceNo, check parameters")
            val body = req.body<WorkPlanFinalRecommendationDto>()
            val errors: Errors = BeanPropertyBindingResult(body, WorkPlanFinalRecommendationDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceWorkPlanDaoServices.addWorkPlanScheduleFinalRecommendationByHOD(referenceNo,batchReferenceNo,body,productReferenceNo)
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

    fun addWorkPlanScheduleEndFinalRecommendationAddingByHOD(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            marketSurveillanceWorkPlanDaoServices.addWorkPlanScheduleEndFinalRecommendationByHOD(referenceNo,batchReferenceNo)
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun addWorkPlanScheduleEndFinalRecommendationAddingByDirector(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            marketSurveillanceWorkPlanDaoServices.addWorkPlanScheduleEndFinalRecommendationByDIRECTOR(referenceNo,batchReferenceNo)
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun addWorkPlanScheduleFeedBackByHOD(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val body = req.body<WorkPlanFeedBackDto>()
            val errors: Errors = BeanPropertyBindingResult(body, WorkPlanFeedBackDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceWorkPlanDaoServices.addWorkPlanScheduleFeedBackByHOD(referenceNo,batchReferenceNo,body, null)
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

    fun addWorkPlanScheduleFeedBackByDirector(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val productReferenceNo = req.paramOrNull("productReferenceNo") ?: throw ExpectedDataNotFound("Required  productReferenceNo, check parameters")
            val body = req.body<WorkPlanFeedBackDto>()
            val errors: Errors = BeanPropertyBindingResult(body, WorkPlanFeedBackDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceWorkPlanDaoServices.addWorkPlanScheduleFeedBackByDirector(productReferenceNo,referenceNo,batchReferenceNo,body)
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

    fun endAddingWorkPlanDataReportSheet(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            marketSurveillanceWorkPlanDaoServices.endAddingWorkPlanInspectionDetailsDataReport(referenceNo,batchReferenceNo)
                .let {
                    ServerResponse.ok().body(it)
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
            val body = req.body<SeizureDto>()
            val errors: Errors = BeanPropertyBindingResult(body, SeizureDto::class.java.name)
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
    fun addWorkPlanEndSeizureDeclaration(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            marketSurveillanceWorkPlanDaoServices.addWorkPlanInspectionDetailsEndSeizureDeclaration(referenceNo,batchReferenceNo)
                .let {
                    ServerResponse.ok().body(it)
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
    fun addWorkPlanScheduleEndSampleSubmission(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            marketSurveillanceWorkPlanDaoServices.addWorkPlanInspectionDetailsEndSampleSubmission(referenceNo,batchReferenceNo)
                .let {
                    ServerResponse.ok().body(it)
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

    fun addWorkPlanScheduleSampleSubmissionEndBsNumber(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            marketSurveillanceWorkPlanDaoServices.addWorkPlanInspectionDetailsSSFEndBSNumberAdding(referenceNo,batchReferenceNo)
                .let {
                    ServerResponse.ok().body(it)
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

    fun saveWorkPlanScheduleSSFComplianceStatusSend(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val body = req.body<SSFSendingComplianceStatus>()
            val errors: Errors = BeanPropertyBindingResult(body, SSFSendingComplianceStatus::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceWorkPlanDaoServices.sendWorkPlanInspectionDetailsSSFSavedComplianceStatusFile(referenceNo,batchReferenceNo,body)
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
    fun saveWorkPlanScheduleSSFComplianceStatusNotSend(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val body = req.body<SSFSendingComplianceStatus>()
            val errors: Errors = BeanPropertyBindingResult(body, SSFSendingComplianceStatus::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceWorkPlanDaoServices.NotSendWorkPlanInspectionDetailsSSFSavedComplianceStatusFile(referenceNo,batchReferenceNo,body)
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

    fun saveWorkPlanScheduleFinalSSFComplianceStatusAdd(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val body = req.body<SSFSaveFinalComplianceStatusDto>()
            val errors: Errors = BeanPropertyBindingResult(body, SSFSaveFinalComplianceStatusDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceWorkPlanDaoServices.addWorkPlanInspectionDetailsSSFFinalSaveComplianceStatus(referenceNo,batchReferenceNo,body)
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
            val body = req.body<InspectionInvestigationReportDto>()
            val errors: Errors = BeanPropertyBindingResult(body, InspectionInvestigationReportDto::class.java.name)
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

    fun addWorkPlanSchedulePreliminaryReportHofHodDirector(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val body = req.body<InspectionInvestigationReportDto>()
            val errors: Errors = BeanPropertyBindingResult(body, InspectionInvestigationReportDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    marketSurveillanceWorkPlanDaoServices.addWorkPlanInspectionDetailsPreliminaryReportHodHofDirecterVersions(referenceNo,batchReferenceNo,body)
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

//    fun addWorkPlanScheduleFinalPreliminaryReport(req: ServerRequest): ServerResponse {
//        return try {
//            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
//            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
//            val finalReportStatus = req.paramOrNull("finalReportStatus") ?: throw ExpectedDataNotFound("Required  finalReportStatus, check parameters")
//            val body = req.body<PreliminaryReportDto>()
//            val errors: Errors = BeanPropertyBindingResult(body, PreliminaryReportDto::class.java.name)
//            validator.validate(body, errors)
//            when {
//                errors.allErrors.isEmpty() -> {
//                    marketSurveillanceWorkPlanDaoServices.updateWorkPlanScheduleInspectionDetailsFinalPreliminaryReport(referenceNo,batchReferenceNo,body,finalReportStatus.toInt()==1)
//                        .let {
//                            ServerResponse.ok().body(it)
//                        }
//                }
//                else -> {
//                    onValidationErrors(errors)
//                }
//            }
//        } catch (e: Exception) {
//            KotlinLogging.logger { }.error(e.message)
//            KotlinLogging.logger { }.debug(e.message, e)
//            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
//        }
//    }



}


