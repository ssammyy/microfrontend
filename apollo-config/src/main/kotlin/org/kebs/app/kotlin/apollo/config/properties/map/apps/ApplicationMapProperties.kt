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

//    @Value("\${org.kebs.app.kotlin.apollo.application.map.base.url.key}")
//    val baseUrlKey: String="BASE-URL"

    @Value("\${org.kebs.app.kotlin.apollo.application.map.base.url.value}")
    val baseUrlValue: String="https://127.0.0.1:8005/api"

    @Value("\${org.kebs.app.kotlin.apollo.application.map.permit.questionnaire}")
    val mapPermitQuestionnaire: Int? = null

    @Value("\${org.kebs.app.kotlin.apollo.application.map.quality.assurance.processes}")
    val mapQualityAssurance: Int = 0

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.manufacture.roleName}")
    val mapQualityAssuranceManufactureRoleName: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.manufacture.viewPage}")
    val mapQualityAssuranceManufactureViewPage: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.user.registration}")
    val mapUserRegistration: Int = 0

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.type.smark.mediumFirm.max.products}")
    val mapQaSmarkMediumMaxProduct: Int = 0

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.type.smark.juakaliFirm.max.products}")
    val mapQaSmarkJuakaliMaxProduct: Int = 0

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.type.dmark.id}")
    val mapQAPermitTypeIDDmark: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.department.id.for.users}")
    val mapQADepertmentId: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.designation.id.for.QAM}")
    val mapQADesignationIDForQAMId: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.designation.id.for.QAO}")
    val mapQADesignationIDForQAOId: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.designation.id.for.Assessor}")
    val mapQADesignationIDForAssessorId: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.designation.id.for.HOD}")
    val mapQADesignationIDForHODId: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.type.smark.id}")
    val mapQAPermitTypeIdSmark: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.quality.assurance.map.permit.type.fmark.id}")
    val mapQAPermitTypeIdFmark: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.application.map.importer.details}")
    val mapImporterDetails: Int = 0

    @Value("\${org.kebs.app.kotlin.apollo.application.map.permit.application}")
    val mapPermitApplication: Int = 0

    @Value("\${org.kebs.app.kotlin.apollo.application.map.fuel.inspection}")
    val mapQaFuelInspection: Int = 0

    @Value("\${org.kebs.app.kotlin.apollo.application.map.tax.rate}")
    val mapKebsTaxRate: BigDecimal = 0.0000.toBigDecimal()

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

    @Value("\${org.kebs.app.kotlin.apollo.resources.static.images.mpesa.logo.path}")
    val mapMPESALogoPath: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.user.registration.activation.notification}")
    val mapUserRegistrationActivationNotification: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.user.registration.password.changed.notification}")
    val mapUserPasswordChangedNotification: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.user.registration.request.password.changed.notification}")
    val mapUserPasswordResetNotification: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.resources.report.demand.note.path}")
    val mapReportDemandNotePath: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.resources.report.demand.note.with.item.path}")
    val mapReportDemandNoteWithItemsPath: String = ""

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

    @Value("\${org.kebs.app.kotlin.apollo.application.map.user.profile.employee.userType}")
    val mapUserTypeNameEmployee: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.user.profile.manufacturer.userType}")
    val mapUserTypeNameManufacture: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.user.profile.importer.userType}")
    val mapUserTypeNameImporter: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.market.surveillance.complaint.letter}")
    val mapMsComplaintLetter: String = ""

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

    @Value("\${org.kebs.app.kotlin.apollo.application.map.invoice.transactions.for.permit}")
    val mapInvoiceTransactionsForPermit: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.sftp.download.file.userName}")
    val mapSFTPUserName: String = ""

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

    @Value("\${org.kebs.app.kotlin.apollo.application.map.complaint.approved.notification.hof}")
    val mapMsComplaintApprovedHofNotification: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.complaint.submit.notification.hod}")
    val mapMsComplaintSubmittedHodNotification: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.complaint.assigned.notification.io}")
    val mapMsComplaintAssignedIONotification: String = ""

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

    @Value("\${org.kebs.app.kotlin.apollo.application.map.kesws.demand.pay.doctype}")
    val mapKeswsDemandNotePayDoctype: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.kesws.ucr.res.doctype}")
    val mapKeswsUcrResDoctype: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.kesws.declaration.doctype}")
    val mapKeswsDeclarationDoctype: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.kesws.manifest.doctype}")
    val mapKeswsManifestDoctype: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.application.map.cors.urls}")
    val mapCorsUrls: String = ""

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
