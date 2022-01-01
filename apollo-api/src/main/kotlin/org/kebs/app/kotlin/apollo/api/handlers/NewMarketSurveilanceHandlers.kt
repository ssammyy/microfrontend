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
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.NewMarketSurveillanceDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.validation.AbstractValidationHandler
import org.kebs.app.kotlin.apollo.common.dto.ms.*
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.springframework.stereotype.Component
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
    private val marketSurveillanceDaoServices: NewMarketSurveillanceDaoServices,
    private val validator: Validator,
) : AbstractValidationHandler() {

    final val appId: Int = applicationMapProperties.mapMarketSurveillance

    fun notSupported(req: ServerRequest): ServerResponse = ServerResponse.badRequest().body("Invalid Request: Not supported")

    fun getAllFuelBatchList(req: ServerRequest): ServerResponse {
        return try {
            marketSurveillanceDaoServices.getAllFuelBatchList()
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
            marketSurveillanceDaoServices.getAllFuelInspectionListBasedOnBatchRefNo(batchReferenceNo)
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
            marketSurveillanceDaoServices.getFuelInspectionDetailsBasedOnRefNo(referenceNo, batchReferenceNo)
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
                    marketSurveillanceDaoServices.createNewFuelBatch(body)
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
            marketSurveillanceDaoServices.closeFuelBatchCreated(referenceNo)
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
                    marketSurveillanceDaoServices.createNewFuelSchedule(body, batchReferenceNo)
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
                    marketSurveillanceDaoServices.getFuelInspectionDetailsAssignOfficer(referenceNo,batchReferenceNo,body)
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
                    marketSurveillanceDaoServices.postFuelInspectionDetailsRapidTest(referenceNo,batchReferenceNo,body)
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
                    marketSurveillanceDaoServices.postFuelInspectionDetailsSampleCollection(referenceNo,batchReferenceNo,body)
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
                    marketSurveillanceDaoServices.postFuelInspectionDetailsSampleSubmission(referenceNo,batchReferenceNo,body)
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
            val bsNumber = req.paramOrNull("bsNumber") ?: throw ExpectedDataNotFound("Required  bsNumber, check parameters")
            marketSurveillanceDaoServices.postFuelInspectionDetailsSampleSubmissionBSNumber(referenceNo,batchReferenceNo,bsNumber)
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun getFuelScheduleSampleSubmissionBsNumber(req: ServerRequest): ServerResponse {
        return try {
            val batchReferenceNo = req.paramOrNull("batchReferenceNo") ?: throw ExpectedDataNotFound("Required Batch RefNumber, check parameters")
            val referenceNo = req.paramOrNull("referenceNo") ?: throw ExpectedDataNotFound("Required  referenceNo, check parameters")
            val bsNumber = req.paramOrNull("bsNumber") ?: throw ExpectedDataNotFound("Required  bsNumber, check parameters")
            marketSurveillanceDaoServices.postFuelInspectionDetailsSampleSubmissionBSNumber(referenceNo,batchReferenceNo,bsNumber)
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

}


