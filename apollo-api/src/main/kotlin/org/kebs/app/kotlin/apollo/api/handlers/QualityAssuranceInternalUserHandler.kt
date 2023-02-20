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
import org.jasypt.encryption.StringEncryptor
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.InspectionReportDaoServices
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
    private val inspectionReportDaoServices: InspectionReportDaoServices,

    private val jasyptStringEncryptor: StringEncryptor,
    private val validator: Validator,

    ) : AbstractValidationHandler() {


    final val appId: Int = applicationMapProperties.mapQualityAssurance


    /*:::::::::::::::::::::::::::::::::::::::::::::START INTERNAL USER FUNCTIONALITY:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/


    fun notSupported(req: ServerRequest): ServerResponse = badRequest().body("Invalid Request: Not supported")

    fun getAllMyTaskList(req: ServerRequest): ServerResponse {
        return try {
            val page = commonDaoServices.extractPageRequest(req)
            val permitTypeID = req.paramOrNull("permitTypeID")?.toLong()
                ?: throw ExpectedDataNotFound("Required Permit Type ID, check config")
            qaDaoServices.findLoggedInUserTask(page, permitTypeID)
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
            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
//            
            qaDaoServices.findPermitDetails(permitID.toLong())
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
                    qaDaoServices.updatePermitSectionDetails(permitID, body)
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
            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
//            
            val body = req.body<CompletenessApplyDto>()
            val errors: Errors = BeanPropertyBindingResult(body, CompletenessApplyDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    qaDaoServices.updatePermitCompletenessDetails(permitID, body)
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

    fun updatePermitDetailsDifferenceStatusActivate(req: ServerRequest): ServerResponse {
        return try {
            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
            qaDaoServices.updatePermitDifferenceStatusDetails(permitID)
                .let {
                    ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun updatePermitDetailsAssignOfficer(req: ServerRequest): ServerResponse {
        return try {
            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
//            
            val body = req.body<AssignOfficerApplyDto>()
            val errors: Errors = BeanPropertyBindingResult(body, AssignOfficerApplyDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    qaDaoServices.updatePermitAssignOfficerDetails(permitID.toLong(), body)
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

    fun updatePermitDetailsAssignAssessor(req: ServerRequest): ServerResponse {
        return try {
            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")

            val body = req.body<AssignAssessorApplyDto>()
            val errors: Errors = BeanPropertyBindingResult(body, AssignAssessorApplyDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    qaDaoServices.updatePermitAssignAssessorDetails(permitID, body)
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
            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
//            
            val body = req.body<StandardsApplyDto>()
            val errors: Errors = BeanPropertyBindingResult(body, StandardsApplyDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    qaDaoServices.updatePermitStandardsDetails(permitID.toLong(), body)
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
            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
            val body = req.body<ScheduleInspectionApplyDto>()
            val errors: Errors = BeanPropertyBindingResult(body, ScheduleInspectionApplyDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    qaDaoServices.updatePermitScheduleInspectionDetails(permitID, body)
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

    fun updatePermitDetailsScheduleAssessmentVisit(req: ServerRequest): ServerResponse {
        return try {
            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
            val body = req.body<ScheduleInspectionApplyDto>()
            val errors: Errors = BeanPropertyBindingResult(body, ScheduleInspectionApplyDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    qaDaoServices.updatePermitScheduleAssessmentVisitDetails(permitID.toLong(), body)
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

    fun checkIfInspectionReportExists(req: ServerRequest): ServerResponse {
        return try {
            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")

            inspectionReportDaoServices.checkIfInspectionReportExists(permitID)
                .let {
                    ok().body(it)
                }


        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }
    fun getInspectionReport(req: ServerRequest): ServerResponse {
        return try {
            val inspectionReportId =
                req.paramOrNull("inspectionReportId")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")

            inspectionReportDaoServices.getInspectionReport(inspectionReportId)
                .let {
                    ok().body(it)
                }


        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }


    fun updatePermitDetailsInspectionCheckListNew(req: ServerRequest): ServerResponse {
        return try {
            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
            val body = req.body<TechnicalDetailsDto>()
            val errors: Errors = BeanPropertyBindingResult(body, TechnicalDetailsDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    inspectionReportDaoServices.inspectionReportNewSave(permitID, body)
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


    fun updateInspectionReportDetailSave(req: ServerRequest): ServerResponse {
        return try {
            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
            val body = req.body<InspectionDetailsDto>()
            val errors: Errors = BeanPropertyBindingResult(body, InspectionDetailsDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    inspectionReportDaoServices.inspectionReportDetailSave(permitID, body)
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

    fun updateInspectionReportDetailBSave(req: ServerRequest): ServerResponse {
        return try {
            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
            val body = req.body<InspectionDetailsDtoB>()
            val errors: Errors = BeanPropertyBindingResult(body, InspectionDetailsDtoB::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    inspectionReportDaoServices.inspectionReportDetailBSave(permitID, body)
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
    fun updateProductLabellingSave(req: ServerRequest): ServerResponse {
        return try {
            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
            val inspectionReportRecommendationID = req.paramOrNull("inspectionReportRecommendationID")?.toLong()
                ?: throw ExpectedDataNotFound("Required Permit ID, check config")

            val body = req.body<List<ProductLabellingDto>>()
            val errors: Errors = BeanPropertyBindingResult(body, ProductLabellingDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    inspectionReportDaoServices.productLabellingSave(permitID, inspectionReportRecommendationID, body)
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

    fun updateProductLabellingSaveB(req: ServerRequest): ServerResponse {
        return try {
            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
            val inspectionReportRecommendationID = req.paramOrNull("inspectionReportRecommendationID")?.toLong()
                ?: throw ExpectedDataNotFound("Required inspectionReportRecommendationID ID, check config")

            val body = req.body<List<ProductLabellingDto>>()
            val productLabel = inspectionReportDaoServices.mapDto(body)

            val errors: Errors = BeanPropertyBindingResult(body, ProductLabellingDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    inspectionReportDaoServices.productLabellingSaveB(permitID, inspectionReportRecommendationID, productLabel)
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

    fun updateInspectionReportStandardizationMarkSchemeSave(req: ServerRequest): ServerResponse {
        return try {
            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
            val body = req.body<StandardizationMarkScheme>()
            val errors: Errors = BeanPropertyBindingResult(body, StandardizationMarkScheme::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    inspectionReportDaoServices.inspectionReportStandardizationMarkSchemeSave(permitID, body)
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

    fun updateInspectionCheckListInspectionReportDetailsOPCSave(req: ServerRequest): ServerResponse {
        return try {
            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
            val inspectionReportRecommendationID = req.paramOrNull("inspectionReportRecommendationID")?.toLong()
                ?: throw ExpectedDataNotFound("Required Permit ID, check config")

            val body = req.body<List<OperationProcessAndControlsDetailsApplyDto>>()
            val errors: Errors =
                BeanPropertyBindingResult(body, OperationProcessAndControlsDetailsApplyDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    inspectionReportDaoServices.inspectionCheckListInspectionReportDetailsOPCSave(
                        permitID,
                        inspectionReportRecommendationID,
                        body
                    )
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

    fun updateInspectionCheckListInspectionHACCPImplementationSave(req: ServerRequest): ServerResponse {
        return try {
            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
            val inspectionReportRecommendationID = req.paramOrNull("inspectionReportRecommendationID")?.toLong()
                ?: throw ExpectedDataNotFound("Required Permit ID, check config")

            val body = req.body<HaccpImplementationDetailsApplyDto>()
            val errors: Errors = BeanPropertyBindingResult(body, HaccpImplementationDetailsApplyDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    inspectionReportDaoServices.inspectionCheckListInspectionHACCPImplementationSave(
                        permitID,
                        inspectionReportRecommendationID,
                        body
                    )
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


    fun updatePermitInspectionCheckListDetails(req: ServerRequest): ServerResponse {
        return try {
             val body = req.body<AllInspectionDetailsApplyDto>()
            val errors: Errors = BeanPropertyBindingResult(body, AllInspectionDetailsApplyDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    inspectionReportDaoServices.updatePermitInspectionCheckListDetails( body)
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


    fun updatePermitDetailsInspectionCheckListNewFinal(req: ServerRequest): ServerResponse {
        return try {
            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
            val errors: Errors = BeanPropertyBindingResult(permitID, AllInspectionDetailsApplyDto::class.java.name)
            validator.validate(permitID, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    inspectionReportDaoServices.submitFinalInspectionReport(permitID)
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

    fun updatePermitDetailsSaveSSFDetails(req: ServerRequest): ServerResponse {
        return try {
            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
//            
            val body = req.body<SSFDetailsApplyDto>()
            val errors: Errors = BeanPropertyBindingResult(body, SSFDetailsApplyDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    qaDaoServices.updatePermitAddSSFDetails(permitID.toLong(), body)
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

    fun updatePermitDetailsSaveSelectedLabPDF(req: ServerRequest): ServerResponse {
        return try {
            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
            val body = req.body<SaveLabPDFApplyDto>()
            val errors: Errors = BeanPropertyBindingResult(body, SaveLabPDFApplyDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    qaDaoServices.updatePermitSaveLabPDFSelectedDetails(permitID.toLong(), body)
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

    fun updatePermitDetailsLabResultsComplianceStatus(req: ServerRequest): ServerResponse {
        return try {
            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
            val body = req.body<SaveLabComplianceApplyDto>()
            val errors: Errors = BeanPropertyBindingResult(body, SaveLabComplianceApplyDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    qaDaoServices.updatePermitSaveLabSaveComplianceDetails(permitID.toLong(), body)
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

    fun updatePermitDetailsSSFCompliance(req: ServerRequest): ServerResponse {
        return try {
            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
            val body = req.body<SaveSSFComplianceApplyDto>()
            val errors: Errors = BeanPropertyBindingResult(body, SaveSSFComplianceApplyDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    qaDaoServices.updatePermitSaveSSFSaveComplianceDetails(permitID.toLong(), body)
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

    fun updatePermitDetailsSaveRecommendation(req: ServerRequest): ServerResponse {
        return try {
            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
            val body = req.body<SaveRecommendationApplyDto>()
            val errors: Errors = BeanPropertyBindingResult(body, SaveRecommendationApplyDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    qaDaoServices.updatePermitSaveRecommendationDetails(permitID.toLong(), body)
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

    fun updatePermitDetailsApproveRejectInspection(req: ServerRequest): ServerResponse {
        return try {
            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
            val body = req.body<ApproveInspectionReportApplyDto>()
            val errors: Errors = BeanPropertyBindingResult(body, ApproveInspectionReportApplyDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    qaDaoServices.updatePermitApproveRejectInspectionDetails(permitID.toLong(), body)
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

    fun updatePermitDetailsApproveRejectJustification(req: ServerRequest): ServerResponse {
        return try {
            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
            val body = req.body<ApproveRejectJustificationReportApplyDto>()
            val errors: Errors =
                BeanPropertyBindingResult(body, ApproveRejectJustificationReportApplyDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    qaDaoServices.updatePermitApproveRejectJustificationReportDetails(permitID.toLong(), body)
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

    fun updatePermitDetailsApproveRejectAssessmentReport(req: ServerRequest): ServerResponse {
        return try {
            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
            val body = req.body<ApproveRejectAssessmentReportApplyDto>()
            val errors: Errors = BeanPropertyBindingResult(body, ApproveRejectAssessmentReportApplyDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    qaDaoServices.updatePermitApproveRejectAssesmentReportDetails(permitID, body)
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

    fun updatePermitDetailsApproveRejectRecommendation(req: ServerRequest): ServerResponse {
        return try {
            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
            val body = req.body<RecommendationApplyDto>()
            val errors: Errors = BeanPropertyBindingResult(body, RecommendationApplyDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    qaDaoServices.updatePermitApproveRejectRecommendationDetails(permitID.toLong(), body)
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

    fun updatePermitDetailsApproveRejectPermitQAM(req: ServerRequest): ServerResponse {
        return try {
            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")

            val body = req.body<ApproveRejectPermitApplyDto>()
            val errors: Errors = BeanPropertyBindingResult(body, ApproveRejectPermitApplyDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    qaDaoServices.updatePermitApproveRejectPermitQAMDetails(permitID, body)
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

    fun updatePermitDetailsApproveRejectPermitPSC(req: ServerRequest): ServerResponse {
        return try {
            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")

            val body = req.body<ApproveRejectPermitApplyDto>()
            val errors: Errors = BeanPropertyBindingResult(body, ApproveRejectPermitApplyDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    qaDaoServices.updatePermitApproveRejectPermitPSCDetails(permitID, body)
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

    fun updatePermitDetailsApproveRejectPermitPAC(req: ServerRequest): ServerResponse {
        return try {
            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")

            val body = req.body<ApproveRejectPermitApplyDto>()
            val errors: Errors = BeanPropertyBindingResult(body, ApproveRejectPermitApplyDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    qaDaoServices.updatePermitApproveRejectPermitPACDetails(permitID, body)
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

    fun updatePermitDetailsApproveRejectPermitPCM(req: ServerRequest): ServerResponse {
        return try {
            val permitID =
                req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")

            val body = req.body<ApproveRejectPermitApplyDto>()
            val errors: Errors = BeanPropertyBindingResult(body, ApproveRejectPermitApplyDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    qaDaoServices.updatePermitApproveRejectPermitPCMDetails(permitID, body)
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



