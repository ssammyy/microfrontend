/*
 *
 *  *
 *  *
 *  *  *    Copyright (c) ${YEAR}.   BSK Global Technologies
 *  *  *
 *  *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *    you may not use this file except in compliance with the License.
 *  *  *    You may obtain a copy of the License at
 *  *  *
 *  *  *       http://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  *    Unless required by applicable law or agreed to in writing, software
 *  *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *   See the License for the specific language governing permissions and
 *  *  *   limitations under the License.
 *  *
 *
 */

package org.kebs.app.kotlin.apollo.api.handlers

//import org.kebs.app.kotlin.apollo.api.ports.provided.createUserAlert

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.QADaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.validation.AbstractValidationHandler
import org.kebs.app.kotlin.apollo.common.dto.qa.*
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.springframework.stereotype.Component
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.Errors
import org.springframework.validation.Validator
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.ServerResponse.badRequest
import org.springframework.web.servlet.function.ServerResponse.ok
import org.springframework.web.servlet.function.body
import org.springframework.web.servlet.function.paramOrNull


@Component
class QualityAssuranceInternalUserHandler(
    private val applicationMapProperties: ApplicationMapProperties,
    private val commonDaoServices: CommonDaoServices,
    private val qaDaoServices: QADaoServices,
    private val validator: Validator,

    ): AbstractValidationHandler() {


    final val appId: Int = applicationMapProperties.mapQualityAssurance


    /*:::::::::::::::::::::::::::::::::::::::::::::START INTERNAL USER FUNCTIONALITY:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/


    fun notSupported(req: ServerRequest): ServerResponse = badRequest().body("Invalid Request: Not supported")

    fun getAllMyTaskList(req: ServerRequest): ServerResponse {
        return try {
            val page = commonDaoServices.extractPageRequest(req)
            val permitTypeID = req.paramOrNull("permitTypeID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit Type ID, check config")
            qaDaoServices.findLoggedInUserTask(page,permitTypeID)
                .let {
                    ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun getPermitDetails(req: ServerRequest): ServerResponse {
        return try {
            val permitID = req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
            qaDaoServices.findPermitDetails(permitID)
                .let {
                    ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun updatePermitDetailsSection(req: ServerRequest): ServerResponse {
        return try {
            val permitID = req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
            val body = req.body<SectionApplyDto>()
            val errors: Errors = BeanPropertyBindingResult(body, SectionApplyDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    qaDaoServices.updatePermitSectionDetails(permitID,body)
                        .let {
                            ok().body(it)
                        }
                }
                else -> {
                    onValidationErrors(errors)
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }
    fun updatePermitDetailsCompleteness(req: ServerRequest): ServerResponse {
        return try {
            val permitID = req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
            val body = req.body<CompletenessApplyDto>()
            val errors: Errors = BeanPropertyBindingResult(body, CompletenessApplyDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    qaDaoServices.updatePermitCompletenessDetails(permitID,body)
                        .let {
                            ok().body(it)
                        }
                }
                else -> {
                    onValidationErrors(errors)
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }
    fun updatePermitDetailsAssignOfficer(req: ServerRequest): ServerResponse {
        return try {
            val permitID = req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
            val body = req.body<AssignOfficerApplyDto>()
            val errors: Errors = BeanPropertyBindingResult(body, AssignOfficerApplyDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    qaDaoServices.updatePermitAssignOfficerDetails(permitID,body)
                        .let {
                            ok().body(it)
                        }
                }
                else -> {
                    onValidationErrors(errors)
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }
    fun updatePermitDetailsStandards(req: ServerRequest): ServerResponse {
        return try {
            val permitID = req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
            val body = req.body<StandardsApplyDto>()
            val errors: Errors = BeanPropertyBindingResult(body, StandardsApplyDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    qaDaoServices.updatePermitStandardsDetails(permitID,body)
                        .let {
                            ok().body(it)
                        }
                }
                else -> {
                    onValidationErrors(errors)
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }
    fun updatePermitDetailsScheduleInspection(req: ServerRequest): ServerResponse {
        return try {
            val permitID = req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
            val body = req.body<ScheduleInspectionApplyDto>()
            val errors: Errors = BeanPropertyBindingResult(body, ScheduleInspectionApplyDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    qaDaoServices.updatePermitScheduleInspectionDetails(permitID,body)
                        .let {
                            ok().body(it)
                        }
                }
                else -> {
                    onValidationErrors(errors)
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }
    fun updatePermitDetailsInspectionCheckList(req: ServerRequest): ServerResponse {
        return try {
            val permitID = req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
            val body = req.body<AllInspectionDetailsApplyDto>()
            val errors: Errors = BeanPropertyBindingResult(body, AllInspectionDetailsApplyDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    qaDaoServices.updatePermitInspectionCheckListDetails(permitID,body)
                        .let {
                            ok().body(it)
                        }
                }
                else -> {
                    onValidationErrors(errors)
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }











    /*:::::::::::::::::::::::::::::::::::::::::::::END INTERNAL USER FUNCTIONALITY:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/


}



