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

package org.kebs.app.kotlin.apollo.config.properties.map.apps

import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import java.math.BigDecimal

@Configuration
@EncryptablePropertySource("file:\${CONFIG_PATH}/application-map.properties")
class ApplicationMapProperties {

    val mapKeswsErrorDocument: String? = "ERR_MSG"
    val mapKeswsCancellationDocument: String? = "KRA_DCL_"

    @Value("\${org.kebs.app.kotlin.apollo.application.map.currency.code}")
    val applicationCurrencyCode: String = "KSH"

    @Value("\${org.kebs.app.kotlin.apollo.application.map.currency.conversion.enable:false}")
    val applicationCurrencyConversionEnabled: Boolean = false

    @Value("\${org.kebs.app.kotlin.apollo.application.map.manufacturer.role.id}")
    val manufacturerRoleId: Long? = null

    @Value("\${org.kebs.app.kotlin.apollo.application.map.test.email.address}")
    val defaultTestEmailAddres: String? = null

    @Value("\${org.kebs.app.kotlin.apollo.application.map.manufacturer.admin.role.id}")
    val manufacturerAdminRoleId: Long? = null

    @Value("\${org.kebs.app.kotlin.apollo.application.map.base.url.value}")
    val baseUrlValue: String = "https://127.0.0.1:8005/api"

    @Value("\${org.kebs.app.kotlin.apollo.application.map.base.url.value.itax}")
    val itaxBaseUrlValue: String = "https://itax.kra.go.ke/"

    @Value("\${org.kebs.app.kotlin.apollo.application.map.base.url.value}")
    val baseUrlValueb: String = "https://127.0.0.1:8005"

    @Value("\${org.kebs.app.kotlin.apollo.application.map.base.url.value.angular}")
    val baseUrlQRValue: String = "https://127.0.0.1/qr-code-qa-permit-scan"

    @Value("\${org.kebs.app.kotlin.apollo.application.map.permit.questionnaire}")
    val mapPermitQuestionnaire: Int? = null

    @Value("\${org.kebs.app.kotlin.apollo.application.map.quality.assurance.processes}")
    val mapQualityAssurance: Int = 0

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.manufacture.roleName}")
    val mapQualityAssuranceManufactureRoleName: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.employee.roleName}")
    val mapQualityAssuranceEmployeeRoleName: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.manufacture.message.remove}")
    val mapQualityAssuranceRemoveMessage: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.manufacture.viewPage}")
    val mapQualityAssuranceManufactureViewPage: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.user.registration}")
    val mapUserRegistration: Int = 0

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.type.smark.mediumFirm.max.products}")
    val mapQaSmarkMediumMaxProduct: Int = 0

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.scheduled.date}")
    val mapQaScheduledDate: Int = 0

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.type.smark.juakaliFirm.max.products}")
    val mapQaSmarkJuakaliMaxProduct: Int = 0

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.type.dmark.id}")
    val mapQAPermitTypeIDDmark: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.user.task.name.ID.for.MANUFACTURE}")
    val mapUserTaskNameMANUFACTURE: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.user.task.name.ID.for.QAO}")
    val mapUserTaskNameQAO: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.user.task.name.ID.for.QAM}")
    val mapUserTaskNameQAM: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.user.task.name.ID.for.HOD}")
    val mapUserTaskNameHOD: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.user.task.name.ID.for.ms.users.epra}")
    val mapMSUserTaskNameEPRA: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.user.task.name.ID.for.ms.users.manager.petroleum}")
    val mapMSUserTaskNameMANAGERPETROLEUM: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.user.task.name.ID.for.ms.users.cp.complainant}")
    val mapMSCPWorkPlanUserTaskNameComplainant: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.user.task.name.ID.for.ms.users.cp.hodRm}")
    val mapMSCPWorkPlanUserTaskNameHodRm: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.user.task.name.ID.for.ms.users.cp.hof}")
    val mapMSCPWorkPlanUserTaskNameHof: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.user.task.name.ID.for.ms.users.cp.msIO}")
    val mapMSCPWorkPlanUserTaskNameIO: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.user.task.name.ID.for.ms.users.cpWkPlan.msDirector}")
    val mapMSCPWorkPlanUserTaskNameDirector: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.user.task.name.ID.for.ms.users.cpWkPlan.labTest}")
    val mapMSCPWorkPlanUserTaskNameLabTest: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.user.task.name.ID.for.ms.users.officer}")
    val mapMSUserTaskNameOFFICER: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.user.task.name.ID.for.ms.users.lab}")
    val mapMSUserTaskNameLAB: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.user.task.name.ID.for.ms.users.station.owner}")
    val mapMSUserTaskNameSTATIONOWNER: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.user.task.name.ID.for.HOF}")
    val mapUserTaskNameHOF: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.user.task.name.ID.for.PCM}")
    val mapUserTaskNamePCM: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.user.task.name.ID.for.ASSESSORS}")
    val mapUserTaskNameASSESSORS: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.user.task.name.ID.for.PSC}")
    val mapUserTaskNamePSC: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.user.task.name.ID.for.PAC_SECRETARY}")
    val mapUserTaskNamePACSECRETARY: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.suspended}")
    val mapQaStatusSuspended: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.unsuspended}")
    val mapQaStatusUnSuspended: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.user.task.name.ID.for.RM}")
    val mapUserTaskNameRM: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.department.id.for.users}")
    val mapQADepertmentId: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.user.role.qa.assessor.id}")
    val mapQAUserAssessorRoleId: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.user.role.qa.officer.id}")
    val mapQAUserOfficerRoleId: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.designation.id.for.QAM}")
    val mapQADesignationIDForQAMId: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.designation.id.for.QAO}")
    val mapQADesignationIDForQAOId: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.market.surveillance.mapped.officer.petroleum.roleID}")
    val mapMSFuelMappedOfficerROLEID: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.market.surveillance.mapped.complaint.officer.roleID}")
    val mapMSComplaintWorkPlanMappedOfficerROLEID: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.market.surveillance.mapped.complaint.hof.roleID}")
    val mapMSComplaintWorkPlanMappedHOFROLEID: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.market.surveillance.mapped.work.plan.rm.roleID}")
    val mapMSComplaintWorkPlanMappedRMROLEID: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.market.surveillance.mapped.work.plan.hod.roleID}")
    val mapMSComplaintWorkPlanMappedHODROLEID: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.market.surveillance.mapped.manager.petroleum.roleID}")
    val mapMSMappedManagerPetroliumROLEID: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.designation.id.for.Assessor}")
    val mapQADesignationIDForAssessorId: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.designation.id.for.PAC}")
    val mapQADesignationIDForPacSecId: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.designation.id.for.HOD}")
    val mapQADesignationIDForHODId: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.designation.id.for.PSC}")
    val mapQADesignationIDForPSCId: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.designation.id.for.PCM}")
    val mapQADesignationIDForPCMId: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.type.smark.id}")
    val mapQAPermitTypeIdSmark: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.type.fmark.id}")
    val mapQAPermitTypeIdFmark: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.type.invoices.id}")
    val mapQAPermitTypeIdInvoices: Long = 0L


    @Value("\${org.kebs.app.kotlin.apollo.application.map.invoice.transactions.prefix}")
    val mapInvoicesPrefix: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.invoice.transactions.for.permitWithHolding}")
    val mapInvoicesPermitWithHolding: BigDecimal = BigDecimal.ZERO

    @Value("\${org.kebs.app.kotlin.apollo.application.map.importer.details}")
    val mapImporterDetails: Int = 0

    @Value("\${org.kebs.app.kotlin.apollo.application.map.permit.application}")
    val mapPermitApplication: Int = 0

    @Value("\${org.kebs.app.kotlin.apollo.application.brs.configuration.id}")
    val mapBRSconfigID: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.fuel.inspection}")
    val mapQaFuelInspection: Int = 0

    @Value("\${org.kebs.app.kotlin.apollo.application.map.tax.rate}")
    val mapKebsTaxRate: BigDecimal = BigDecimal.ZERO

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.type.smark.mediumFirm.extra.products.cost}")
    val mapQaSmarkMediumExtraProductCost: BigDecimal = 0.0000.toBigDecimal()

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.type.smark.juakaliFirm.extra.products.cost}")
    val mapQaSmarkJuakaliExtraProductCost: BigDecimal = 0.0000.toBigDecimal()

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.type.fmark.domestic.amountToPay}")
    val mapQaDmarkDomesticAmountToPay: BigDecimal = 0.0000.toBigDecimal()

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.type.fmark.foregin.amountToPay}")
    val mapQaDmarkForeginAmountToPay: BigDecimal = 0.0000.toBigDecimal()

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.type.fmark.amountToPay}")
    val mapQaFmarkAmountToPay: BigDecimal = 0.0000.toBigDecimal()

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.type.fmark.type.foregin.status}")
    val mapQaDmarkForeginStatus: Int = 0

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.type.fmark.type.domestic.status}")
    val mapQaDmarkDomesticStatus: Int = 0

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.type.smark.turnover.for.largefirms.ID}")
    val mapQASmarkLargeFirmsTurnOverId: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.type.smark.turnover.for.mediumFirms.ID}")
    val mapQASmarkMediumTurnOverId: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.type.smark.turnover.for.juakaliFirm.ID}")
    val mapQASmarkJuakaliTurnOverId: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.user.request.importer}")
    val mapUserRequestImporter: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.user.request.manufacture}")
    val mapUserRequestManufacture: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.draft}")
    val mapQaStatusDraft: Long = 0L


    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.resubmitted}")
    val mapQaStatusResubmitted: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.pending_sta_3}")
    val mapQaStatusPSTA3: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.pending_sta_10}")
    val mapQaStatusPSTA10: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.pending_submission}")
    val mapQaStatusPSubmission: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.pending_payment}")
    val mapQaStatusPPayment: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.payment_done}")
    val mapQaStatusPaymentDone: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.submitted}")
    val mapQaStatusPSubmitted: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.pending_approval_for_completeness}")
    val mapQaStatusPApprovalCompletness: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.incomplete_application}")
    val mapQaStatusIncompleteAppl: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.pending_qao_assigning}")
    val mapQaStatusPQAOAssign: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.pending_scheduling_visit}")
    val mapQaStatusPShedulvisit: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.pending_standards_details}")
    val mapQaStatusPStandardsAdding: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.pending_generation_of_ssc}")
    val mapQaStatusPGenSSC: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.end_production}")
    val mapQaStatusEndProduction: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.pending_approval_of_ssc}")
    val mapQaStatusPApprSSC: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.coc_uploaded}")
    val mapQaStatusCocUploaded: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.ssc_rejected}")
    val mapQaStatusSSCRejected: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.pending_request_approval}")
    val mapQaStatusPRequestApproval: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.request_approved}")
    val mapQaStatusRequestApproved: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.request_rejected}")
    val mapQaStatusRequestRejected: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.pending_factory_inspection_forms}")
    val mapQaStatusPfactoryInsForms: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.pending_inspection_report}")
    val mapQaStatusPInspReport: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.pending_scf}")
    val mapQaStatusPSCF: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.pending_ssf}")
    val mapQaStatusPSSF: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.pending_inspection_report_approval}")
    val mapQaStatusPInspectionReportApproval: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.pending_new_inspection_Report}")
    val mapQaStatusInspectionReportRejected: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.pending_bs_number}")
    val mapQaStatusPBSNumber: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.pending_lab_results}")
    val mapQaStatusPLABResults: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.pending_lab_results_completeness}")
    val mapQaStatusPLABResultsCompletness: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.pending_compliance_status}")
    val mapQaStatusPCompliance: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.pending_recommendation}")
    val mapQaStatusPRecommendation: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.deferred_recommendation_qam}")
    val mapQaStatusDeferredRecommendationQAM: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.pending_recommendation_approval}")
    val mapQaStatusPRecommendationApproval: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.pending_psc_members_awarding}")
    val mapQaStatusPPSCMembersAward: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.pending_qam_hod_approval}")
    val mapQaStatusPHodQamApproval: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.deferred_by_psc_members}")
    val mapQaStatusDeferredPSCMembers: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.pending_re_inspection}")
    val mapQaStatusPendingReInspection: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.pending_pcm_awarding}")
    val mapQaStatusPPCMAwarding: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.pending_review_pcm}")
    val mapQaStatusPPCMReview: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.pending_sta10_completion}")
    val mapQaStatusPSTA10Completion: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.deferred_by_pcm}")
    val mapQaStatusDeferredPCM: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.permit_awarded}")
    val mapQaStatusPermitAwarded: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.pending_correction}")
    val mapQaStatusPendingCorrectionManf: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.pending_qam_hod_rejected}")
    val mapQaStatusRejectedByHodQam: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.permit_expired}")
    val mapQaStatusPermitExpired: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.permit_renewal}")
    val mapQaStatusPermitRenewalDraft: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.pending_generation_of_justification_report}")
    val mapQaStatusPGeneJustCationReport: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.rejected_justification_report}")
    val mapQaStatusRejectedJustCationReport: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.rejected_assessment_report}")
    val mapQaStatusRejectedAssessmentReport: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.pending_approval_of_justification_report}")
    val mapQaStatusPApprovalustCationReport: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.pending_assessor_assigning}")
    val mapQaStatusPAssesorAssigning: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.pending_factory_visit_schedule}")
    val mapQaStatusPFactoryVisitSchedule: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.pending_generation_of_assessment_report}")
    val mapQaStatusPGenerationAssesmentReport: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.pending_approval_of_assessment_report}")
    val mapQaStatusPApprovalAssesmentReport: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.pending_pac_secretary_awarding}")
    val mapQaStatusPPACSecretaryAwarding: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.deferred_by_pac_secretary}")
    val mapQaStatusDeferredByPACSecretary: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.pending_factory_surveillance}")
    val mapQaStatusPFactorySurveillance: Long = 0L


    @Value("\${org.kebs.app.kotlin.apollo.application.map.user.manufacture.role.id}")
    val mapUserManufactureRoleID: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.standards.development}")
    val mapStandardsDevelopment: Int = 0

    @Value("\${org.kebs.app.kotlin.apollo.application.map.market.surveillance}")
    val mapMarketSurveillance: Int = 0

    @Value("\${org.kebs.app.kotlin.apollo.application.map.market.surveillance.complaint.customer}")
    val mapMarketSurveillanceComplaintCustomer: Int = 0

    @Value("\${org.kebs.app.kotlin.apollo.application.map.import.inspection}")
    val mapImportInspection: Int = 0

    @Value("\${org.kebs.app.kotlin.apollo.application.map.permit.modified}")
    val mapPermitModified: Int = 0

    @Value("\${org.kebs.app.kotlin.apollo.application.map.invoice.transactions}")
    val mapInvoiceTransactions: Int = 0

    @Value("\${org.kebs.app.kotlin.apollo.application.map.lims}")
    val mapLimsTransactions: Int = 0

    @Value("\${org.kebs.app.kotlin.apollo.application.map.invoice.transactions.mpesa.details}")
    val mapMpesaDetails: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.invoice.transactions.bank.one.details}")
    val mapBankOneDetails: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.invoice.transactions.bank.two.details}")
    val mapBankTwoDetails: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.invoice.transactions.bank.three.details}")
    val mapBankThreeDetails: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.risk.profile}")
    val mapRiskProfile: Int = 0

    @Value("\${org.kebs.app.kotlin.apollo.application.map.risk.profile.importer}")
    val mapRiskProfileImporter: Int = 0

    @Value("\${org.kebs.app.kotlin.apollo.application.map.risk.profile.exporter}")
    val mapRiskProfileExporter: Int = 0

    @Value("\${org.kebs.app.kotlin.apollo.application.map.risk.profile.consignee}")
    val mapRiskProfileConsignee: Int = 0

    @Value("\${org.kebs.app.kotlin.apollo.application.map.risk.profile.consignor}")
    val mapRiskProfileConsignor: Int = 0

    @Value("\${org.kebs.app.kotlin.apollo.application.map.permit.award}")
    val mapPermitAward: Int = 0

    @Value("\${org.kebs.app.kotlin.apollo.application.map.permit.review}")
    val mapPermitReview: Int = 0

    @Value("\${org.kebs.app.kotlin.apollo.application.map.user.activation}")
    var mapUserActivation: Int? = null

    @Value("\${org.kebs.app.kotlin.apollo.application.map.manufacturer.registration}")
    val mapManufacturerRegistration: Int = 0

    @Value("\${org.kebs.app.kotlin.apollo.application.map.ministyInspection.save}")
    val mapMinistyInspectionSave: Int = 0

    @Value("\${org.kebs.app.kotlin.apollo.application.map.mpesa.config.integration}")
    val mapMpesaConfigIntegration: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.config.integration.max.amount}")
    val mapMpesaConfigIntegrationMaxAmount: BigDecimal = BigDecimal.ZERO

    @Value("\${org.kebs.app.kotlin.apollo.application.map.sage.config.integration}")
    val mapSageConfigIntegration: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.sage.config.integration.qa}")
    val mapSageConfigIntegrationQa: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.kra.config.integration}")
    val mapKraConfigIntegration: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.kra.config.integration.penalty}")
    val mapKraPenaltyConfigIntegration: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.lims.config.integration}")
    val mapLimsConfigIntegration: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.lims.config.integration.pdf}")
    val mapLimsConfigIntegrationPDF: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.lims.config.integration.List.pdf}")
    val mapLimsConfigIntegrationListPDF: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.sms.config.integration}")
    val mapSmsConfigIntegration: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.kesws}")
    val mapKesws: Int = 0

    @Value("\${org.kebs.app.kotlin.apollo.application.map.uss.rate.name}")
    val mapUssRateName: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.kesws.doc.type}")
    val mapKeswsDocType: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.resources.static.images.kebs.logo.path}")
    val mapKebsLogoPath: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.resources.static.images.kebs.ms.logo.path}")
    val mapKebsMSLogoPath: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.resources.static.images.kebs.ms.footer.path}")
    val mapKebsMSFooterPath: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.resources.static.images.kebs.testSignature.path}")
    val mapKebsTestSignaturePath: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.resources.static.images.checkmark.path}")
    val mapCheckmarkImagePath: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.resources.static.images.dmark.image.path}")
    val mapDmarkImagePath: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.resources.static.images.smark.image.path}")
    val mapSmarkImagePath: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.resources.static.images.fmark.image.path}")
    val mapFmarkImagePath: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.resources.static.images.mpesa.logo.path}")
    val mapMPESALogoPath: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.manufacture.message.renew}")
    val mapPermitRenewMessage: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.manufacture.message.new}")
    val mapPermitNewMessage: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.user.registration.activation.notification}")
    val mapUserRegistrationActivationNotification: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.user.registration.password.changed.notification}")
    val mapUserPasswordChangedNotification: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.user.registration.request.password.changed.notification}")
    val mapUserPasswordResetNotification: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.user.registration.entry.number.notification}")
    val mapUserEntryNumberNotification: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.resources.report.demand.note.path}")
    val mapReportDemandNotePath: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.resources.report.ministry.checklist.report.path}")
    val mapReportMinistryChecklistPath: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.resources.report.motor.vehicle.checklist.report.path}")
    val mapReportMotorVehicleChecklistPath: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.resources.report.local.cor.report.path}")
    val mapReportLocalCorPath: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.resources.report.local.coc.report.path}")
    val mapReportLocalCocPath: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.resources.report.demand.note.with.item.path}")
    val mapReportDemandNoteWithItemsPath: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.resources.report.proforma.invoice.with.item.path}")
    val mapReportProfomaInvoiceWithItemsPath: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.resources.report.qa.smark.permit.background.image.path}")
    val mapSmarkBackgroundImagePath: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.resources.report.qa.fmark.permit.background.image.path}")
    val mapFmarkBackgroundImagePath: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.resources.report.qa.dmark.permit.background.image.path}")
    val mapDmarkBackgroundImagePath: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.resources.report.break.down.invoice.with.item.path}")
    val mapReportBreakDownInvoiceWithItemsPath: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.resources.report.proforma.invoice.pdf.with.item.path}")
    val mapPDFProfomaInvoiceWithItemsPath: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.resources.report.pac.summary.report.path}")
    val mapReportPacSummaryReportPath: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.resources.report.qa.dmark.permit.report.path}")
    val mapReportDmarkPermitReportPath: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.resources.report.qa.smark.permit.report.path}")
    val mapReportSmarkPermitReportPath: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.resources.report.qa.fmark.permit.report.path}")
    val mapReportFmarkPermitReportPath: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.resources.report.ms.fuel.remediation.invoice.path}")
    val mapMSFuelInvoiceRemediationPath: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.resources.report.ms.fuel.sample.collection.path}")
    val mapMSSampleCollectionPath: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.resources.report.ms.fuel.sample.submission.path}")
    val mapMSSampleSubmissionPath: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.resources.report.ms.work.field.report.path}")
    val mapMSFieldReportPath: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.resources.report.sl.payment.collection.path}")
    val mapSLPaymentPath: String = ""

    @Value("\${destination.inspection.cd.status.type.coc.generated.and.send}")
    val mapDICdStatusTypeCOCGeneratedAndSendID: Long = 0L

    @Value("\${destination.inspection.cd.status.type.coi.generated.and.send}")
    val mapDICdStatusTypeCOIGeneratedAndSendID: Long = 0L

    @Value("\${destination.inspection.cd.status.type.cor.generated.and.send}")
    val mapDICdStatusTypeCORGeneratedAndSendID: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.kesws.config.integration}")
    val mapKeswsConfigIntegration: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.user.registration.user.role.id}")
    val mapUserRegistrationUserRoleID: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.user.registration.employee.role.id}")
    val mapUserRegistrationEmployeeRoleID: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.config.integration.callback.job}")
    val mapMpesaConfigIntegrationCallBackJob: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.config.integration.push.job}")
    val mapMpesaConfigIntegrationPushJob: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.user.profile.employee.userTypeID}")
    val mapUserTypeEmployee: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.user.profile.manufacturer.userTypeID}")
    val mapUserTypeManufacture: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.user.profile.ministry.userTypeID}")
    val mapUserTypeMinistry: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.user.profile.importer.userTypeID}")
    val mapUserTypeImporter: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.user.profile.contractors.type.id}")
    val mapUserTypeContractorID: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.user.profile.manufacture.type.id}")
    val mapUserTypeManufactureID: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.user.profile.employee.userType}")
    val mapUserTypeNameEmployee: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.user.profile.manufacturer.userType}")
    val mapUserTypeNameManufacture: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.user.profile.importer.userType}")
    val mapUserTypeNameImporter: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.market.surveillance.complaint.letter}")
    val mapMsComplaintLetter: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.market.surveillance.workPlanDestrctionID}")
    val mapMsWorkPlanDestrctionID: Long = 0

    @Value("\${org.kebs.app.kotlin.apollo.application.map.sftp.upload.file.name}")
    val mapSftpUploadName: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.sftp.download.file.replace.characters}")
    val mapSftpDownloadFileReplaceCharacters: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.sftp.download.file.replace.status.path}")
    val mapSftpDownloadFileReplaceStatusPath: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.sftp.download.file.replace.date.path}")
    val mapSftpDownloadFileReplaceDatePath: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.sftp.upload.file.name.destination}")
    val mapSftpUploadNameDestination: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.invoice.transactions.for.demandNote}")
    val mapInvoiceTransactionsForDemandNote: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.invoice.transactions.local.currency.prefix}")
    val mapInvoiceTransactionsLocalCurrencyPrefix: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.invoice.transactions.foreign.currency.prefix}")
    val mapInvoiceTransactionsForeignCurrencyPrefix: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.invoice.transactions.for.MS.fuelReconciliation}")
    val mapInvoiceTransactionsForMSFuelReconciliation: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.invoice.ms.petroleum.revenue.code}")
    val mapRevenueCodeForMSFuelInspection: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.invoice.ms.petroleum.revenue.desc}")
    val mapRevenueDescForMSFuelInspection: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.invoice.transactions.for.permit}")
    val mapInvoiceTransactionsForPermit: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.fuel.step.batch.creation}")
    val mapMSCreateBatch: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.fuel.step.batch.add.sites}")
    val mapMSAddSiteVisit: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.fuel.step.batch.close}")
    val mapMSCloseBatch: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.fuel.step.assign.officer}")
    val mapMSAssignOfficer: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.fuel.step.rapid.test}")
    val mapMSRapidTest: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.fuel.step.sample.collection}")
    val mapMSSampleCollection: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.fuel.step.sample.submission}")
    val mapMSSampleSubmision: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.fuel.step.bs.number}")
    val mapMSBsNumber: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.fuel.step.pending.lab.results}")
    val mapMSPendingLabResults: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.fuel.step.available.lab.results}")
    val mapMSLabResultsAvailabe: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.fuel.step.complance.status}")
    val mapMSComplanceStatus: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.fuel.step.remediation.schedule}")
    val mapMSRemediationSchedule: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.fuel.step.remediation.generation}")
    val mapMSRemediationInvoice: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.fuel.step.payment}")
    val mapMsPayment: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.fuel.step.remediation}")
    val mapMSRemediation: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.fuel.step.end.fuel}")
    val mapMSEndFuel: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.fuel.step.fuel.ended}")
    val mapMSFuelInspectionEnded: Long = 0L



    /**********************ALL MS PROCESS NAME START***********************************/
    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.step.generateWorkPlan}")
    val mapMSWorkPlanInspectionGenerateWorkPlan: Long = 0L
    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.step.submittedForApproval}")
    val mapMSWorkPlanInspectionSubmittedForApproval: Long = 0L
    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.step.approvedWorPlan}")
    val mapMSWorkPlanInspectionApprovedWorPlan: Long = 0L
    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.step.rejectedWorPlan}")
    val mapMSWorkPlanInspectionRejectedWorPlan: Long = 0L
    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.step.resubmitWorkPlanModification}")
    val mapMSWorkPlanInspectionResubmitWorkPlanModification: Long = 0L
    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.step.startOnSiteActivities}")
    val mapMSWorkPlanInspectionStartOnSiteActivities: Long = 0L
    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.step.endOnSiteActivities}")
    val mapMSWorkPlanInspectionEndOnSiteActivities: Long = 0L
    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.step.bsNumberAdded}")
    val mapMSWorkPlanInspectionBsNumberAdded: Long = 0L
    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.step.lIMSResultsAvailable}")
    val mapMSWorkPlanInspectionLIMSResultsAvailable: Long = 0L
    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.step.labResultsAnalysed}")
    val mapMSWorkPlanInspectionLabResultsAnalysed: Long = 0L
    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.step.preliminaryReportGenerated}")
    val mapMSWorkPlanInspectionPreliminaryReportGenerated: Long = 0L
    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.step.preliminaryReportApprovedHOF}")
    val mapMSWorkPlanInspectionPreliminaryReportApprovedHOF: Long = 0L
    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.step.preliminaryReportRejectedHOF}")
    val mapMSWorkPlanInspectionPreliminaryReportRejectedHOF: Long = 0L
    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.step.preliminaryReportModified}")
    val mapMSWorkPlanInspectionPreliminaryReportModified: Long = 0L
    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.step.preliminaryReportApprovedHODRM}")
    val mapMSWorkPlanInspectionPreliminaryReportApprovedHODRM: Long = 0L
    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.step.preliminaryReportRejectedHODRM}")
    val mapMSWorkPlanInspectionPreliminaryReportRejectedHODRM: Long = 0L
    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.step.finalReportGenerated}")
    val mapMSWorkPlanInspectionFinalReportGenerated: Long = 0L
    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.step.finalReportApprovedHOF}")
    val mapMSWorkPlanInspectionFinalReportApprovedHOF: Long = 0L
    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.step.finalReportRejectedHOF}")
    val mapMSWorkPlanInspectionFinalReportRejectedHOF: Long = 0L
    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.step.finalReportModified}")
    val mapMSWorkPlanInspectionFinalReportModified: Long = 0L
    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.step.finalReportApprovedHODRM}")
    val mapMSWorkPlanInspectionFinalReportApprovedHODRM: Long = 0L
    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.step.finalReportRejectedHODRM}")
    val mapMSWorkPlanInspectionFinalReportRejectedHODRM: Long = 0L
    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.step.recommendationsADDED}")
    val mapMSWorkPlanInspectionRecommendationsADDED: Long = 0L
    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.step.directorRemarksADDED}")
    val mapMSWorkPlanInspectionDirectorRemarksADDED: Long = 0L
    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.step.recommendationDoneMSIO}")
    val mapMSWorkPlanInspectionRecommendationDoneMSIO: Long = 0L
    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.step.msProcessEnded}")
    val mapMSWorkPlanInspectionMSProcessEnded: Long = 0L






    /**********************ALL MS PROCESS NAME END***********************************/


    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.step.labResultsPDFSave}")
    val mapMSWorkPlanInspectionLabResultsPDFSave: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.step.submissionApproval}")
    val mapMSWorkPlanInspectionSubmission: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.step.approveWorkPlan}")
    val mapMSWorkPlanInspectionApproveWorkPlan: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.step.rejectWorkPlan}")
    val mapMSWorkPlanInspectionRejectWorkPlan: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.step.startMsOnSite}")
    val mapMSWorkPlanInspectionStartMsOnSite: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.step.endMsOnSite}")
    val mapMSWorkPlanInspectionEndMsOnSite: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.step.addBsNumber}")
    val mapMSWorkPlanInspectionAddBsNumber: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.step.labResults}")
    val mapMSWorkPlanInspectionLabResults: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.step.analysesLabResults}")
    val mapMSWorkPlanInspectionAnalysesLabResults: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.step.generatePreliminaryReport}")
    val mapMSWorkPlanInspectionGeneratePreliminaryReport: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.step.approvePreliminaryReportHOF}")
    val mapMSWorkPlanInspectionApprovePreliminaryReportHOF: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.step.rejectPreliminaryReportHOF}")
    val mapMSWorkPlanInspectionRejectPreliminaryReportHOF: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.step.approvePreliminaryReportHODRM}")
    val mapMSWorkPlanInspectionApprovePreliminaryReportHODRM: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.step.rejectPreliminaryReportHODRM}")
    val mapMSWorkPlanInspectionRejectPreliminaryReportHODRM: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.step.generateFinalPreliminaryReport}")
    val mapMSWorkPlanInspectionGenerateFinalPreliminaryReport: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.step.approveFinalPreliminaryReportHOF}")
    val mapMSWorkPlanInspectionApproveFinalPreliminaryReportHOF: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.step.rejectFinalPreliminaryReportHOF}")
    val mapMSWorkPlanInspectionRejectFinalPreliminaryReportHOF: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.step.approveFinalPreliminaryReportHODRM}")
    val mapMSWorkPlanInspectionApproveFinalPreliminaryReportHODRM: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.step.rejectFinalPreliminaryReportHODRM}")
    val mapMSWorkPlanInspectionRejectFinalPreliminaryReportHODRM: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.step.finalRecommendationPendingDestructionNotice}")
    val mapMSWorkPlanInspectionFinalRecommendationPendingDestruction: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.step.finalRecommendationPendingFinalRemarks}")
    val mapMSWorkPlanInspectionFinalRecommendationPendingFinalRemarks: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.step.destructionAddedPendingAppeal}")
    val mapMSWorkPlanInspectionDestructionAddedPendingAppeal: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.step.pendingDestractionGoodReport}")
    val mapMSWorkPlanInspectionPendingDestractionGoodReport: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.step.clientAppealedWaitingSuccessfull}")
    val mapMSWorkPlanInspectionClientAppealedAwaitSuccessfull: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.step.destructionSuccessfulPendingFnalRemarks}")
    val mapMSWorkPlanInspectionDestructionSuccessfullPendingFinalRemarks: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.step.pendingFinalRemarksHODRM}")
    val mapMSWorkPlanInspectionPendingFinalRemarksHODRM: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.step.ENDMSProcess}")
    val mapMSWorkPlanInspectionENDMSProcess: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.fuel.step.save.lab.pdf}")
    val mapMSSaveSelectedLabResults: Long = 0L

//    @Value("\${org.kebs.app.kotlin.apollo.application.map.sftp.download.file.name}")
//    val mapSftpDownloadName: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.market.surveillance.complaint.online}")
    val mapMsComplaintOnLine: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.complaint.submit.acknowledgement.notification}")
    val mapMsComplaintAcknowledgementNotification: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.complaint.rejected.notification.compliant}")
    val mapMsComplaintAcknowledgementRejectionNotification: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.complaint.oga.mandate.notification.compliant}")
    val mapMsComplaintAcknowledgementRejectionWIthOGANotification: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.complaint.rejected.for.amendment.notification.compliant}")
    val mapMsComplaintAcknowledgementRejectionRejectedForAmendmentNotification: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.fuel.scheduled.notification}")
    val mapMsFuelScheduleMPNotification: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.hodRMApprovalEmail}")
    val mapMsWorkPlanScheduleSubmitedForApproval: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.hofPreliminaryApprovalEmail}")
    val mapMsWorkPlanPreliminarySubmitedApprovalEmailHOF: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.hofFinalPreliminaryApprovalEmail}")
    val mapMsWorkPlanFinalPreliminarySubmitedApprovalEmailHOF: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.HofApprovePreliminaryEmail}")
    val mapMsWorkPlanPreliminaryApprovalByHOFEmail: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.HofRejectedPreliminaryEmail}")
    val mapMsWorkPlanPreliminaryRejectedByHOFEmail: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.HofApproveFinalPreliminaryEmail}")
    val mapMsWorkPlanFinalPreliminaryApprovalByHOFEmail: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.HofRejectedFinalPreliminaryEmail}")
    val mapMsWorkPlanFinalPreliminaryRejectedByHOFEmail: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.HodApprovePreliminaryEmail}")
    val mapMsWorkPlanPreliminaryApprovalByHODEmail: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.HodRejectedPreliminaryEmail}")
    val mapMsWorkPlanPreliminaryRejectedByHODEmail: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.HodApproveFinalPreliminaryEmail}")
    val mapMsWorkPlanFinalPreliminaryApprovalByHODEmail: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.HodRejectedFinalPreliminaryEmail}")
    val mapMsWorkPlanFinalPreliminaryRejectedByHODEmail: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.officerWorkPlanRejectedEmail}")
    val mapMsWorkPlanScheduleSubmitedApprovalRejected: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.officerWorkPlanApprovedEmail}")
    val mapMsWorkPlanScheduleSubmitedApprovalApproved: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.officerWorkPlanDestructionEmail}")
    val mapMsOfficerWorkPlanDestructionEmail: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.officerWorkPlanNotDestructionEmail}")
    val mapMsOfficerWorkPlanNotDestructionEmail: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.officerSendDestructionNotificationEmail}")
    val mapMsOfficerSendDestructionNotificationEmail: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.hodFinalFeedBackNotificationEmailComplinat}")
    val mapMshodFinalFeedBackNotificationEmailComplinat: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.officerSendDestructionNotificationHODEmail}")
    val mapMsOfficerSendDestructionNotificationHODEmail: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.hodFinalFeedBackNotificationEmail}")
    val mapMsHodFinalFeedBackNotificationEmail: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.complaint.approved.notification.hof}")
    val mapMsComplaintApprovedHofNotification: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.complaint.submit.notification.hod}")
    val mapMsComplaintSubmittedHodNotification: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.newTask.notification}")
    val mapMsNotificationNewTask: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.overDue.task.notification}")
    val mapMsNotificationOverDueTask: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.labResults.task.notification}")
    val mapMsNotificationLabResultsTask: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.complaint.assigned.notification.io}")
    val mapMsComplaintAssignedIONotification: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.fuel.assigned.notification.io}")
    val mapMsFuelAssignedIONotification: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.fuel.remediation.invoice.notification}")
    val mapMsFuelInspectionRemediationInvoiceNotification: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.fuel.remediation.schedule.notification}")
    val mapMsFuelInspectionRemediationScheduleNotification: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.fuel.schedule.end.notification}")
    val mapMsFuelInspectionScheduleEndedNotification: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.fuel.lab.results.notification}")
    val mapMsFuelInspectionLabResultsNotification: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.lab.results.notification.io}")
    val mapMsLabResultsIONotification: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.work.plan.labResultNotificationEmail}")
    val mapMsLabResultNotificationEmail: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.market.surveillance.complaint.type.uuid}")
    val mapMsComplaintTypeUuid: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.market.surveillance.workPlan.type.uuid}")
    val mapMsWorkPlanTypeUuid: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.market.surveillance.fuel.type.uuid}")
    val mapMsFuelTypeUuid: String = ""

    @Value("\${destination.inspection.cd.status.type.assignIO.id}")
    val mapDIStatusTypeAssignIoId: Long = 0L

    @Value("\${destination.inspection.cd.status.type.reassignIO.id}")
    val mapDIStatusTypeReassignIoId: Long = 0L

    @Value("\${destination.inspection.cd.status.type.awaiting.targeting.approval.id}")
    val mapDIStatusTypeAwaitingTargetingApprovalId: Long = 0L

    @Value("\${destination.inspection.cd.status.type.targeted.approved.id}")
    val mapDIStatusTypeTargetedApprovedId: Long = 0L

    @Value("\${destination.inspection.cd.status.type.kra.verification.send.id}")
    val mapDIStatusTypeKraVerificationSendId: Long = 0L

    @Value("\${destination.inspection.cd.status.type.kra.verification.approved.id}")
    val mapDIStatusTypeKraVerificationApprovedId: Long = 0L

    @Value("\${destination.inspection.cd.status.type.inspection.start.id}")
    val mapDIStatusTypeInspectionStartId: Long = 0L

    @Value("\${destination.inspection.cd.status.type.inspection.ended.id}")
    val mapDIStatusTypeInspectionEndId: Long = 0L

    @Value("\${destination.inspection.cd.status.type.inspection.checklist.id}")
    val mapDIStatusTypeInspectionChecklistId: Long = 0L

    @Value("\${destination.inspection.cd.status.type.ministry.inspection.uploaded}")
    val mapDIStatusTypeMinistryInspectionUploadedId: Long = 0L

    @Value("\${destination.inspection.cd.status.type.inspection.sample.collection.id}")
    val mapDIStatusTypeInspectionSampleCollectionId: Long = 0L

    @Value("\${destination.inspection.cd.status.type.inspection.sample.submission.id}")
    val mapDIStatusTypeInspectionSampleSubmissionId: Long = 0L

    @Value("\${destination.inspection.cd.status.type.inspection.sample.bsNumber.id}")
    val mapDIStatusTypeInspectionSampleBsNumberId: Long = 0L

    @Value("\${destination.inspection.cd.status.type.inspection.sample.results.awaiting.ID}")
    val mapDIStatusTypeInspectionSampleResultsAwaitingId: Long = 0L

    @Value("\${destination.inspection.cd.status.type.inspection.sample.results.received.ID}")
    val mapDIStatusTypeInspectionSampleResultsReceivedId: Long = 0L

    @Value("\${destination.inspection.cd.status.type.inspection.sample.results.compliant.ID}")
    val mapDIStatusTypeInspectionSampleResultsCompliantId: Long = 0L

    @Value("\${destination.inspection.cd.status.type.inspection.sample.results.not.compliant.ID}")
    val mapDIStatusTypeInspectionSampleResultsNotCompliantId: Long = 0L

    @Value("\${destination.inspection.cd.status.type.inspection.generate.inspection.report.ID}")
    val mapDIStatusTypeInspectionGenerateInspectionReportId: Long = 0L

    @Value("\${destination.inspection.cd.status.type.inspection.approve.inspection.report.ID}")
    val mapDIStatusTypeInspectionApproveInspectionReportId: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.market.surveillance.complaint.and.workPlan.designation.inspection.officer.id}")
    val mapMsComplaintAndWorkPlanDesignationIO: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.market.surveillance.complaint.and.workPlan.designation.director.id}")
    val mapMsComplaintAndWorkPlanDesignationDirector: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.market.surveillance.complaint.and.workPlan.designation.hod.id}")
    val mapMsComplaintAndWorkPlanDesignationHOD: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.market.surveillance.complaint.and.workPlan.designation.hof.id}")
    val mapMsComplaintAndWorkPlanDesignationHOF: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.market.surveillance.complaint.and.workPlan.designation.regional.manager.id}")
    val mapMsComplaintAndWorkPlanDesignationRM: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.market.surveillance.fuel.designation.manager.petroleum.id}")
    val mapMsFuelMP: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.market.surveillance.fuel.designation.inspection.officer.petroleum.id}")
    val mapMsFuelIOP: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.market.surveillance.directorate.id}")
    val mapMsDirectorateID: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.destination.inspection.inspection.officer.designation.id}")
    val mapDiInspectionOfficerDesignationId: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.destination.inspection.coc.type.local.id}")
    val mapDICocTypeLocalID: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.destination.inspection.coc.type.foreign.id}")
    val mapDICocTypeForeignID: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.destination.inspection.coc.type.local.localType.EXE.id}")
    val mapDICocTypeLocalLocalTypeEXEID: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.destination.inspection.officer.incharge.designation.id}")
    val mapDIOfficerInChargeID: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.destination.inspection.coc.type.local.localType.PGES.id}")
    val mapDICocTypeLocalLocalTypePGESID: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.destination.inspection.coc.type.local.localType.MEP.id}")
    val mapDICocTypeLocalLocalTypeMEPID: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.destination.inspection.coc.type.local.localType.DG.id}")
    val mapDICocTypeLocalLocalTypeDGID: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.destination.inspection.coc.type.local.localType.DMSG.id}")
    val mapDICocTypeLocalLocalTypeDMSGID: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.destination.inspection.coc.type.local.localType.EAC.id}")
    val mapDICocTypeLocalLocalTypeEACID: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.destination.inspection.coc.type.local.localType.UPERR.id}")
    val mapDICocTypeLocalLocalTypeUPERRID: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.destination.inspection.coc.type.local.localType.ISGCDG.id}")
    val mapDICocTypeLocalLocalTypeISGCDGID: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.destination.inspection.coc.type.local.localType.RIMP.id}")
    val mapDICocTypeLocalLocalTypeRIMPID: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.destination.inspection.coc.type.local.localType.TIMP.id}")
    val mapDICocTypeLocalLocalTypeTIMPID: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.destination.inspection.coc.type.local.localType.MWG.id}")
    val mapDICocTypeLocalLocalTypeMWGID: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.destination.inspection.coc.type.local.localType.TIDF.id}")
    val mapDICocTypeLocalLocalTypeTIDFID: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.motor.vehicle.report.disapproved}")
    val mapMVReportDissaprovedNotification: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.destination.inspection.coi.route}")
    val mapDICOIRoute: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.destination.inspection.coc.route}")
    val mapDICOCRoutes: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.motor.vehicle.report.approved}")
    val mapMVReportApprovedNotification: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.destination.inspection.bsNumber}")
    val mapDIBsNumberNotification: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.importer.di.application.cs.approval.type.uuid}")
    val mapDIApplicationCSApprovalTypeUuid: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.importer.di.application.temporary.import.type.uuid}")
    val mapDIApplicationTemporaryImportTypeUuid: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.destination.inspection.labResults}")
    val mapDILabResultsNotification: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.destination.inspection.report.approval}")
    val mapDIReportApprovalNotification: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.destination.inspection.report.approved}")
    val mapDIReportApprovedNotification: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.destination.inspection.report.disApproved}")
    val mapDIReportDisApprovedNotification: String = ""

    /*
    Jsch SFTP Configs
     */
    @Value("\${org.kebs.app.kotlin.apollo.application.map.sftp.client.protocol}")
    val mapSftpClientProtocol: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.sftp.client.host}")
    val mapSftpHost: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.sftp.client.port}")
    val mapSftpPort: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.sftp.client.username}")
    val mapSftpUsername: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.sftp.client.password}")
    val mapSftpPassword: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.sftp.client.upload.root}")
    val mapSftpUploadRoot: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.sftp.client.download.root}")
    val mapSftpDownloadRoot: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.sftp.client.processed.root}")
    val mapSftpProcessedRoot: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.sftp.client.unprocessable.root}")
    val mapSftpUnprocessableRoot: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.sftp.client.session.connection.timeout}")
    val mapSftpSessionConnectTimeout: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.sftp.client.channel.connection.timeout}")
    val mapSftpChannelConnectedTimeout: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.kesws.cd.doctype}")
    val mapKeswsCdDoctype: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.kesws.cor.doctype}")
    val mapKeswsCorDoctype: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.kesws.coc.doctype}")
    val mapKeswsCocDoctype: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.kesws.coi.doctype}")
    val mapKeswsCoiDoctype: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.kesws.idf.doctype}")
    val mapKeswsBaseDocumentDoctype: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.kesws.cd.approval.doctype}")
    val mapKeswsCdApprovalDoctype: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.kesws.demand.doctype}")
    val mapKeswsDemandNoteDoctype: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.kesws.on.hold.doctype}")
    val mapKeswsOnHoldDoctype: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.kesws.demand.pay.doctype}")
    val mapKeswsDemandNotePayDoctype: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.kesws.ucr.res.doctype}")
    val mapKeswsUcrResDoctype: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.kesws.declaration.doctype}")
    val mapKeswsDeclarationDoctype: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.kesws.declaration.verification.doctype}")
    val mapKeswsDeclarationVerificationDoctype: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.kesws.manifest.doctype}")
    val mapKeswsManifestDoctype: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.kesws.air.manifest.doctype}")
    val mapKeswsAirManifestDoctype: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.kesws.checking.officer}")
    val mapKeswsCheckingOfficer: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.cors.urls}")
    val mapCorsUrls: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.email.username}")
    val mapApplicationEmailUsername: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.email.password}")
    val mapApplicationEmailPassword: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.email.from}")
    val mapApplicationEmailFrom: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.email.smtp.start.tls.enable}")
    val mapApplicationEmailSmtpStartTlsEnable: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.email.smtp.host}")
    val mapApplicationEmailSmtpHost: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.email.smtp.port}")
    val mapApplicationEmailSmtpPort: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.email.smtp.auth}")
    val mapApplicationEmailSmtpAuth: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.email.protocol}")
    val mapApplicationEmailProtocol: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.sl.level.one.approval.authorityId}")
    val slLevelOneApprovalAuthorityId: Long? = null

    @Value("\${org.kebs.app.kotlin.apollo.sl.level.two.approval.authorityId}")
    val slLevelTwoApprovalAuthorityId: Long? = null

    @Value("\${org.kebs.app.kotlin.apollo.sl.employee.role.id}")
    val slEmployeeRoleId: Long? = null

    @Value("\${org.kebs.app.kotlin.apollo.application.transaction.success.status}")
    val transactionSuccessStatus: Int = 30

    @Value("\${org.kebs.app.kotlin.apollo.application.transaction.active.status}")
    val transactionActiveStatus: Int = 1

    @Value("\${org.kebs.app.kotlin.apollo.application.transaction.test.status}")
    val transactionTestStatus: Int = 2

    @Value("\${org.kebs.app.kotlin.apollo.application.transaction.inactive.status}")
    val transactionInactiveStatus: Int = 0

    @Value("\${org.kebs.app.kotlin.apollo.application.transaction.success.status.response}")
    val transactionSuccessStatusResponse: String = "00"

    @Value("\${org.kebs.app.kotlin.apollo.application.transaction.failed.status}")
    val transactionFailedStatus: Int = 25

    @Value("\${org.kebs.app.kotlin.apollo.application.transaction.exception.status}")
    val transactionExceptionStatus: Int = 99

    @Value("\${org.kebs.app.kotlin.apollo.application.map.complaint.process.onLine}")
    val msComplaintProcessOnlineSubmitted: Long? = null

    @Value("\${org.kebs.app.kotlin.apollo.application.map.complaint.process.letter}")
    val msComplaintProcessLetterSubmitted: Long? = null

    @Value("\${org.kebs.app.kotlin.apollo.application.map.complaint.process.classification-details}")
    val msComplaintProcessClassificationDetails: Long? = null

    @Value("\${org.kebs.app.kotlin.apollo.application.map.complaint.process.mandate}")
    val msComplaintProcessMandate: Long? = null

    @Value("\${org.kebs.app.kotlin.apollo.application.map.complaint.process.msProcessStated}")
    val msComplaintProcessStarted: Long? = null

    @Value("\${org.kebs.app.kotlin.apollo.application.map.complaint.process.not.mandate}")
    val msComplaintProcessNotMandate: Long? = null

    @Value("\${org.kebs.app.kotlin.apollo.application.map.complaint.process.rejected}")
    val msComplaintProcessRejected: Long? = null

    @Value("\${org.kebs.app.kotlin.apollo.application.map.complaint.process.assign.officer}")
    val msComplaintProcessAssignOfficer: Long? = null

    @Value("\${org.kebs.app.kotlin.apollo.application.map.complaint.process.assign.hof}")
    val msComplaintProcessAssignHOF: Long? = null

    @Value("\${org.kebs.app.kotlin.apollo.application.page.start}")
    val pageStart: Int = 1

    @Value("\${org.kebs.app.kotlin.apollo.application.page.records}")
    val pageRecords: Int = 1

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.status.suspended}")
    val suspended: Long? = null

    @Value("\${org.kebs.app.kotlin.apollo.resources.report.sl.active.collection.path}")
    val mapSLActiveFirmsPath: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.resources.report.sl.closed.collection.path}")
    val mapSLClosedFirmsPath: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.resources.report.sl.dormant.collection.path}")
    val mapSLDormantFirmsPath: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.resources.report.sl.levyPayment.collection.path}")
    val mapSLevyPaymentPath: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.resources.report.sl.levyPenalty.collection.path}")
    val mapSLevyPenaltyPath: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.resources.report.sl.registered.collection.path}")
    val mapSLRegisteredFirmsPath: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.resources.search.qa.permit}")
    val mapSearchForQAPermit: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.kebs.msg.config.integration}")
    val mapKebsMsgConfigIntegration: Long = 0L

    /**
     * TODO: Why do we need this
     */

    val assigneeId: Long = 101
    val qaoAssigneeId: Long = 402

    //    val directorAssigneeId: Long = 103
    val hofAssigneeId: Long = 401
    val custAssigneeId: Long = 105
    val labAssigneeId: Long = 419
    val pscAssigneeId: Long = 430
    val pcmAssigneeId: Long = 535
    val pmAssigneeId: Long = 583
    val hofId: Long = 401

    //    DMARK
    val qaHod: Long = 534
    val assessorId: Long = 541
    val applicationName: String = "KIMS"

    // Create user with PAC roles, authorities
    val pacAssigneeId: Long = 581
    val directorAssigneeId: Long = 582
    val principalLevyOfficer: Long = 681
    val slAssistantManager: Long = 682
    val slManager: Long = 683

    val thirtyDayWindow: Int = 243
    val ninetyDayWindow: Int = 244

    //    val localFilePath = "C:\\Users\\thor\\Desktop\\BSK\\apollo\\apollo-api\\uploads\\"
    //val localFilePath = "/home/james/Projects/kebs/apollo/apollo-api/uploads/"
    val localFilePath = "/var/uploads/apollo/"

}
