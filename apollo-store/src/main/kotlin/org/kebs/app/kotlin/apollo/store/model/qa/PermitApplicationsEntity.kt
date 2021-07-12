package org.kebs.app.kotlin.apollo.store.model.qa

import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import java.io.Serializable
import java.math.BigDecimal
import java.sql.Date
import java.sql.Timestamp
import java.util.*
import javax.persistence.*


@Entity
@Table(name = "DAT_KEBS_PERMIT_TRANSACTION")
class PermitApplicationsEntity:Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_PERMIT_TRANSACTION_SEQ_GEN", allocationSize = 1, sequenceName = "DAT_KEBS_PERMIT_TRANSACTION_SEQ")
    @GeneratedValue(generator = "DAT_KEBS_PERMIT_TRANSACTION_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = 0

    @Column(name = "APPLICANT_NAME")
    @Basic
    var applicantName: String? = null

    @Column(name = "FIRM_NAME")
    @Basic
    var firmName: String? = null

    @Column(name = "POSTAL_ADDRESS")
    @Basic
    var postalAddress: String? = null

    @Column(name = "POSITION")
    @Basic
    var position: String? = null

    @Column(name = "PERMIT_REF_NUMBER")
    @Basic
    var permitRefNumber: String? = null

    @Column(name = "COMMODITY_DESCRIPTION")
    @Basic
    var commodityDescription: String? = null

    @Column(name = "TELEPHONE_NO")
    @Basic
    var telephoneNo: String? = null

    @Column(name = "FAX_NO")
    @Basic
    var faxNo: String? = null

    @Column(name = "EMAIL")
    @Basic
    var email: String? = null

    @Column(name = "PHYSICAL_ADDRESS")
    @Basic
    var physicalAddress: String? = null

    @Column(name = "DESCRIPTION")
    @Basic
    var description: String? = null

    @Column(name = "PLOT_NO")
    @Basic
    var plotNo: String? = null


    @Column(name = "DESIGNATION")
    @Basic
    var designation: String? = null

    @Column(name = "USER_TASK_ID")
    @Basic
    var userTaskId: Long? = null


    @Column(name = "VAT_NO")
    @Basic
    var vatNo: String? = null

    @Column(name = "FACTORY_VISIT")
    @Basic
    var factoryVisit: Date? = null


    @Column(name = "region")
    @Basic
    var region: String? = null

    @Column(name = "INSPECTION_DATE")
    @Basic
    var inspectionDate: Date? = null

    @Column(name = "ASSESSMENT_DATE")
    @Basic
    var assessmentDate: Date? = null

    @Column(name = "INSPECTION_SCHEDULED_STATUS")
    @Basic
    var inspectionScheduledStatus: Int? = null

    @Column(name = "ASSESSMENT_SCHEDULED_STATUS")
    @Basic
    var assessmentScheduledStatus: Int? = null

    @Column(name = "GENERATE_SCHEME_STATUS")
    @Basic
    var generateSchemeStatus: Int? = null

    @Column(name = "COMPLIANT_STATUS")
    @Basic
    var compliantStatus: Int? = null

    @Column(name = "SEND_FOR_PCM_REVIEW")
    @Basic
    var sendForPcmReview: Int? = null

    @Column(name = "REQUEST_STATUS")
    @Basic
    var requestStatus: Int? = null

    @Column(name = "JUSTIFICATION_REPORT_STATUS")
    @Basic
    var justificationReportStatus: Int? = null

    @Column(name = "OLD_PERMIT_STATUS")
    @Basic
    var oldPermitStatus: Int? = null

    @Column(name = "RENEWAL_STATUS")
    @Basic
    var renewalStatus: Int? = null

    @Column(name = "PERMIT_REJECTED_CREATED_VERSION")
    @Basic
    var permitRejectedCreatedVersion: Int? = null

    @Column(name = "APPROVED_REJECTED_SCHEME")
    @Basic
    var approvedRejectedScheme: Int? = null

    @Column(name = "JUSTIFICATION_REPORT_REMARKS")
    @Basic
    var justificationReportRemarks: String? = null

    @Column(name = "ASSESSMENT_REPORT_REMARKS")
    @Basic
    var assessmentReportRemarks: String? = null

    @Column(name = "RESUBMIT_REMARKS")
    @Basic
    var resubmitRemarks: String? = null


//    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
//    @ManyToOne
//    var userId: UsersEntity? = null

//    @JoinColumn(name = "PERMIT_TYPE", referencedColumnName = "ID")
//    @ManyToOne
//    var permitType: PermitTypesEntity? = null


    @Column(name = "PERMIT_STATUS")
    @Basic
    var permitStatus: Long? = null

    @Column(name = "USER_ID")
    @Basic
    var userId: Long? = null

    @Column(name = "PERMIT_TYPE")
    @Basic
    var permitType: Long? = null

    @Column(name = "DIVISION_ID")
    @Basic
    var divisionId: Long? = null

    @Column(name = "INSPECTION_REPORT_ID")
    @Basic
    var inspectionReportId: Long? = null

    @Column(name = "SECTION_ID")
    @Basic
    var sectionId: Long? = null


    @Column(name = "KS_NUMBER")
    @Basic
    var ksNumber: String? = null


    @Column(name = "DATE_OF_ISSUE")
    @Basic
    var dateOfIssue: Date? = null

    @Column(name = "DATE_OF_EXPIRY")
    @Basic
    var dateOfExpiry: Date? = null

    @Column(name = "SUSPENSION_STATUS")
    @Basic
    var applicationSuspensionStatus: Int? = null

    @Column(name = "PRODUCT_NAME")
    @Basic
    var productName: String? = null


    @Column(name = "BS_NUMBER")
    @Basic
    var bsNumber: String? = null

    @Column(name = "COMPLIANT_REMARKS")
    @Basic
    var compliantRemarks: String? = null


    @Column(name = "RECOMMENDATION_APPROVAL_STATUS")
    @Basic
    var recommendationApprovalStatus: Int? = null

    @Column(name = "RECOMMENDATION_REMARKS")
    @Basic
    var recommendationRemarks: String? = null


    @Column(name = "RECOMMENDATION_APPROVAL_REMARKS")
    @Basic
    var recommendationApprovalRemarks: String? = null

    @Column(name = "TRADE_MARK")
    @Basic
    var tradeMark: String? = null


    @Column(name = "STA10_FILLED_STATUS")
    @Basic
    var sta10FilledStatus: Int? = null

    @Column(name = "STA10_FILLED_OFFICER_STATUS")
    @Basic
    var sta10FilledOfficerStatus: Int? = null


    @Column(name = "SCF_ID")
    @Basic
    var scfId: Long? = null

    @Column(name = "SSF_ID")
    @Basic
    var ssfId: Long? = null

    @Column(name = "SSC_ID")
    @Basic
    var sscId: Long? = null

    @Column(name = "TEST_REPORT_ID")
    @Basic
    var testReportId: Long? = null

    @Column(name = "PSC_MEMBER_ID")
    @Basic
    var pscMemberId: Long? = null

    @Column(name = "PCM_ID")
    @Basic
    var pcmId: Long? = null

    @Column(name = "QAM_ID")
    @Basic
    var qamId: Long? = null

    @Column(name = "HOD_ID")
    @Basic
    var hodId: Long? = null

    @Column(name = "RM_ID")
    @Basic
    var rmId: Long? = null

    @Column(name = "HOF_ID")
    @Basic
    var hofId: Long? = null

    @Column(name = "QAO_ID")
    @Basic
    var qaoId: Long? = null

    @Column(name = "ASSESSOR_ID")
    @Basic
    var assessorId: Long? = null

    @Column(name = "PAC_SEC_ID")
    @Basic
    var pacSecId: Long? = null

    @Column(name = "STA3_FILLED_STATUS")
    @Basic
    var sta3FilledStatus: Int? = null

    @Column(name = "STATUS")
    @Basic
    var status: Int? = null

    @Column(name = "END_OF_PRODUCTION_STATUS")
    @Basic
    var endOfProductionStatus: Int? = null

    @Column(name = "FMARK_GENERATED")
    @Basic
    var fmarkGenerated: Int? = null

    @Column(name = "PERMIT_FOREIGN_STATUS")
    @Basic
    var permitForeignStatus: Int? = null

    @Column(name = "PERMIT_AWARD_STATUS")
    @Basic
    var permitAwardStatus: Int? = null

    @Column(name = "ENABLED")
    @Basic
    var enabled: Int? = null

    @Column(name = "INVOICE_GENERATED")
    @Basic
    var invoiceGenerated: Int? = null


    @Column(name = "SME_FORM_FILLED_STATUS")
    @Basic
    var smeFormFilledStatus: Int? = null

    @Column(name = "SEND_APPLICATION")
    @Basic
    var sendApplication: Int? = null

    @Column(name = "PAID_STATUS")
    @Basic
    var paidStatus: Int? = null

    @Column(name = "PERMIT_EXPIRED_STATUS")
    @Basic
    var permitExpiredStatus: Int? = null

    @Column(name = "ASSIGN_OFFICER_STATUS")
    @Basic
    var assignOfficerStatus: Int? = null

    @Column(name = "ASSIGN_ASSESSOR_STATUS")
    @Basic
    var assignAssessorStatus: Int? = null

    @Column(name = "RESUBMIT_APPLICATION_STATUS")
    @Basic
    var resubmitApplicationStatus: Int? = null

    @Column(name = "END_OF_PRODUCTION_REMARKS")
    @Basic
    var endOfProductionRemarks: String? = null

    @Column(name = "TITLE")
    @Basic
    var title: String? = null

    @Column(name = "HOF_QAM_COMPLETENESS_STATUS")
    @Basic
    var hofQamCompletenessStatus: Int? = null


    @Column(name = "HOF_QAM_COMPLETENESS_REMARKS")
    @Basic
    var hofQamCompletenessRemarks: String? = null

    @Column(name = "FACTORY_INSPECTION_REPORT_APPROVED_REJECTED_STATUS")
    @Basic
    var factoryInspectionReportApprovedRejectedStatus: Int? = null


    @Column(name = "FACTORY_INSPECTION_REPORT_APPROVED_REJECTED_REMARKS")
    @Basic
    var factoryInspectionReportApprovedRejectedRemarks: String? = null

    @Column(name = "PSC_MEMBER_APPROVAL_STATUS")
    @Basic
    var pscMemberApprovalStatus: Int? = null


    @Column(name = "PSC_MEMBER_APPROVAL_REMARKS")
    @Basic
    var pscMemberApprovalRemarks: String? = null

    @Column(name = "PCM_APPROVAL_STATUS")
    @Basic
    var pcmApprovalStatus: Int? = null


    @Column(name = "PCM_APPROVAL_REMARKS")
    @Basic
    var pcmApprovalRemarks: String? = null

    @Column(name = "ASSESSMENT_CRITERIA")
    @Basic
    var assessmentCriteria: String? = null

    @Column(name = "PAC_DECISION_REMARKS")
    @Basic
    var pacDecisionRemarks: String? = null

    @Column(name = "HOD_APPROVE_ASSESSMENT_REMARKS")
    @Basic
    var hodApproveAssessmentRemarks: String? = null

    @Column(name = "HOD_APPROVE_ASSESSMENT_STATUS")
    @Basic
    var hodApproveAssessmentStatus: Int? = null

    //A Declaration Form

    @Column(name = "TOTAL_COST")
    @Basic
    var totalCost: BigDecimal? = null


    @Column(name = "TOTAL_PAYMENT")
    @Basic
    var totalPayment: BigDecimal? = null


    @Column(name = "ANNUAL_TURNOVER")
    @Basic
    var annualTurnOver: BigDecimal? = null


    @Column(name = "INSPECTOR_REMARKS")
    @Basic
    var inspectorsRemark: String? = null

    @Column(name = "ATTACHED_PLANT_REMARKS")
    @Basic
    var attachedPlantRemarks: String? = null

    @Column(name = "INSPECTOR_NAME")
    @Basic
    var inspectorsName: String? = null

    @Column(name = "DATE_OF_VISIT")
    @Basic
    var dateOfVisit :Timestamp? = null

    @Column(name = "PRODUCT_CATEGORY")
    @Basic
    var productCategory: Long? = null

    @Column(name = "BROAD_PRODUCT_CATEGORY")
    @Basic
    var broadProductCategory: Long? = null

    @Column(name = "PRODUCT")
    @Basic
    var product: Long? = null

    @Column(name = "VERSION_NUMBER")
    @Basic
    var versionNumber: Long? = null

    @Column(name = "PRODUCT_STANDARD")
    @Basic
    var productStandard: Long? = null

    @Column(name = "STANDARD_CATEGORY")
    @Basic
    var standardCategory: Long? = null

    @Column(name = "PRODUCT_SUB_CATEGORY")
    @Basic
    var productSubCategory: Long? = null

    @Column(name = "ATTACHED_PLANT_ID")
    @Basic
    var attachedPlantId: Long? = null

    @Column(name = "COC_ID")
    @Basic
    var cocId: Long? = null

    @Column(name = "INSPECTION_REPORT_GENERATED")
    @Basic
    var inspectionReportGenerated: Int? = null


    @Column(name = "AWARDED_PERMIT_NUMBER")
    @Basic
    var awardedPermitNumber: String? = null

    @Column(name = "BRAND_NAME_REQUEST")
    @Basic
    var brandNameRequest: String? = null

    @Column(name = "BRAND_NAME_REQUEST_STATUS")
    @Basic
    var brandNameRequestStatus: Long? = null

    @Column(name = "BRAND_NAME_REQUEST_REMARKS")
    @Basic
    var brandNameRequestRemarks: String? = null

    @Column(name = "BRAND_NAME_REQUEST_APPROVAL")
    @Basic
    var brandNameRequestApproval: String? = null

    @Column(name = "BRAND_NAME_REQUEST_STATUS_APPROVAL")
    @Basic
    var brandNameRequestStatusApproval: Long? = null

    @Column(name = "BRAND_NAME_REQUEST_REMARKS_APPROVAL")
    @Basic
    var brandNameRequestRemarksApproval: String? = null

    @Column(name = "END_PRODUCTION_REQUEST")
    @Basic
    var endProductionRequest: String? = null

    @Column(name = "END_PRODUCTION_REQUEST_STATUS")
    @Basic
    var endProductionRequestStatus: Long? = null

    @Column(name = "END_PRODUCTION_REQUEST_REMARKS")
    @Basic
    var endProductionRequestRemarks: String? = null

    @Column(name = "END_PRODUCTION_REQUEST_APPROVAL")
    @Basic
    var endProductionRequestApproval: String? = null

    @Column(name = "END_PRODUCTION_REQUEST_STATUS_APPROVAL")
    @Basic
    var endProductionRequestStatusApproval: Long? = null

    @Column(name = "COMPANY_ID")
    @Basic
    var companyId: Long? = null

    @Column(name = "END_PRODUCTION_REQUEST_REMARKS_APPROVAL")
    @Basic
    var endProductionRequestRemarksApproval: String? = null


    @Column(name = "VAR_FIELD_1")
    @Basic
    var varField1: String? = null

    @Column(name = "VAR_FIELD_2")
    @Basic
    var varField2: String? = null

    @Column(name = "VAR_FIELD_3")
    @Basic
    var varField3: String? = null

    @Column(name = "VAR_FIELD_4")
    @Basic
    var varField4: String? = null

    @Column(name = "VAR_FIELD_5")
    @Basic
    var varField5: String? = null

    @Column(name = "VAR_FIELD_6")
    @Basic
    var varField6: String? = null

    @Column(name = "VAR_FIELD_7")
    @Basic
    var varField7: String? = null

    @Column(name = "VAR_FIELD_8")
    @Basic
    var varField8: String? = null

    @Column(name = "VAR_FIELD_9")
    @Basic
    var varField9: String? = null

    @Column(name = "VAR_FIELD_10")
    @Basic
    var varField10: String? = null

    @Column(name = "CREATED_BY")
    @Basic
    var createdBy: String? = null

    @Column(name = "CREATED_ON")
    @Basic
    var createdOn: Timestamp? = null

    @Column(name = "MODIFIED_BY")
    @Basic
    var modifiedBy: String? = null

    @Column(name = "MODIFIED_ON")
    @Basic
    var modifiedOn: Timestamp? = null

    @Column(name = "DELETE_BY")
    @Basic
    var deleteBy: String? = null

    @Column(name = "DELETED_ON")
    @Basic
    var deletedOn: Timestamp? = null

    @Column(name = "APP_REVIEW_PROCESS_INSTANCE_ID")
    @Basic
    var appReviewProcessInstanceId: String? = null

    @Column(name = "APP_REVIEW_STATUS")
    @Basic
    var appReviewStatus: Int? = null

    @Column(name = "APP_REVIEW_STARTED_ON")
    @Basic
    var appReviewStartedOn: Timestamp? = null

    @Column(name = "APP_REVIEW_COMPLETED_ON")
    @Basic
    var appReviewCompletedOn: Timestamp? = null

    @Column(name = "SF_MARK_INSPECTION_PROCESS_INSTANCE_ID")
    @Basic
    var sfMarkInspectionProcessInstanceId: String? = null

    @Column(name = "SF_MARK_INSPECTION_STATUS")
    @Basic
    var sfMarkInspectionStatus: Int? = null

    @Column(name = "SF_MARK_INSPECTION_STARTED_ON")
    @Basic
    var sfMarkInspectionStartedOn: Timestamp? = null

    @Column(name = "SF_MARK_INSPECTION_COMPLETED_ON")
    @Basic
    var sfMarkInspectionCompletedOn: Timestamp? = null

    @Column(name = "DM_APP_REVIEW_STATUS")
    @Basic
    var dmAppReviewStatus: Int? = null

    @Column(name = "DM_APP_REVIEW_STARTED_ON")
    @Basic
    var dmAppReviewStartedOn: Timestamp? = null

    @Column(name = "DM_APP_REVIEW_COMPLETED_ON")
    @Basic
    var dmAppReviewCompletedOn: Timestamp? = null

    @Column(name = "DM_APP_REVIEW_PROCESS_INSTANCE_ID")
    @Basic
    var dmAppReviewProcessInstanceId: String? = null

    @Column(name = "SF_APP_PAYMENT_STATUS")
    @Basic
    var sfAppPaymentStatus: Int? = null

    @Column(name = "SF_APP_PAYMENT_STARTED_ON")
    @Basic
    var sfAppPaymentStartedOn: Timestamp? = null

    @Column(name = "SF_APP_PAYMENT_COMPLETED_ON")
    @Basic
    var sfAppPaymentCompletedOn: Timestamp? = null

    @Column(name = "SF_APP_PAYMENT_PROCESS_INSTANCE_ID")
    @Basic
    var sfAppPaymentProcessInstanceId: String? = null

    @Column(name = "SF_PERMIT_AWARD_STATUS")
    @Basic
    var sfPermitAwardStatus: Int? = null

    @Column(name = "SF_PERMIT_AWARD_STARTED_ON")
    @Basic
    var sfPermitAwardStartedOn: Timestamp? = null

    @Column(name = "SF_PERMIT_AWARD_COMPLETED_ON")
    @Basic
    var sfPermitAwardCompletedOn: Timestamp? = null

    @Column(name = "SF_PERMIT_AWARD_PROCESS_INSTANCE_ID")
    @Basic
    var sfPermitAwardProcessInstanceId: String? = null

    @Column(name = "II_SCHEDULE_STATUS")
    @Basic
    var iiScheduleStatus: Int? = null

    @Column(name = "II_SCHEDULE_STARTED_ON")
    @Basic
    var iiScheduleStartedOn: Timestamp? = null

    @Column(name = "II_SCHEDULE_COMPLETED_ON")
    @Basic
    var iiScheduleCompletedOn: Timestamp? = null

    @Column(name = "II_SCHEDULE_PROCESS_INSTANCE_ID")
    @Basic
    var iiScheduleProcessInstanceId: String? = null

    @Column(name = "II_REPORTING_STATUS")
    @Basic
    var iiReportingStatus: Int? = null

    @Column(name = "II_REPORTING_STARTED_ON")
    @Basic
    var iiReportingStartedOn: Timestamp? = null

    @Column(name = "II_REPORTING_COMPLETED_ON")
    @Basic
    var iiReportingCompletedOn: Timestamp? = null


    @Column(name = "II_REPORTING_PROCESS_INSTANCE_ID")
    @Basic
    var iiReportingProcessInstanceId: String? = null

    @Column(name = "DM_ASSESSMENT_STATUS")
    @Basic
    var dmAssessmentStatus: Int? = null

    @Column(name = "DM_ASSESSMENT_STARTED_ON")
    @Basic
    var dmAssessmentStartedOn: Timestamp? = null

    @Column(name = "DM_ASSESSMENT_COMPLETED_ON")
    @Basic
    var dmAssessmentCompletedOn: Timestamp? = null

    @Column(name = "DM_ASSESSMENT_PROCESS_INSTANCE_ID")
    @Basic
    var dmAssessmentProcessInstanceId: String? = null

    @Column(name = "DM_APP_PAYMENT_STATUS")
    @Basic
    var dmAppPaymentStatus: Int? = null

    @Column(name = "DM_APP_PAYMENT_STARTED_ON")
    @Basic
    var dmAppPaymentStartedOn: Timestamp? = null

    @Column(name = "DM_APP_PAYMENT_COMPLETED_ON")
    @Basic
    var dmAppPaymentCompletedOn: Timestamp? = null

    @Column(name = "DM_APP_PAYMENT_PROCESS_INSTANCE_ID")
    @Basic
    var dmAppPaymentProcessInstanceId: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as PermitApplicationsEntity
        return id == that.id && cocId == that.cocId
                && factoryVisit == that.factoryVisit
                && companyId == that.companyId
                && userTaskId == that.userTaskId
                && inspectionReportGenerated == that.inspectionReportGenerated
                && permitRejectedCreatedVersion == that.permitRejectedCreatedVersion
                && designation == that.designation && resubmitRemarks == that.resubmitRemarks && email == that.email && faxNo == that.faxNo && sscId == that.sscId && firmName == that.firmName && applicantName == that.applicantName && physicalAddress == that.physicalAddress && plotNo == that.plotNo && position == that.position && postalAddress == that.postalAddress && region == that.region && telephoneNo == that.telephoneNo && vatNo == that.vatNo && createdBy == that.createdBy && createdOn == that.createdOn && dateOfIssue == that.dateOfIssue && dateOfVisit == that.dateOfVisit && deleteBy == that.deleteBy && deletedOn == that.deletedOn && ksNumber == that.ksNumber && modifiedBy == that.modifiedBy && modifiedOn == that.modifiedOn && permitType == that.permitType && productName == that.productName && title == that.title && totalCost == that.totalCost && totalPayment == that.totalPayment && tradeMark == that.tradeMark && varField1 == that.varField1 && varField10 == that.varField10 && varField2 == that.varField2 && varField3 == that.varField3 && varField4 == that.varField4 && varField5 == that.varField5 && varField6 == that.varField6 && varField7 == that.varField7 && varField8 == that.varField8 && varField9 == that.varField9 && userId == that.userId && status == that.status && permitRefNumber == that.permitRefNumber && dateOfExpiry == that.dateOfExpiry && commodityDescription == that.commodityDescription && enabled == that.enabled && broadProductCategory == that.broadProductCategory && productCategory == that.productCategory && product == that.product && productSubCategory == that.productSubCategory && standardCategory == that.standardCategory && productStandard == that.productStandard && sta3FilledStatus == that.sta3FilledStatus && sta10FilledStatus == that.sta10FilledStatus && permitForeignStatus == that.permitForeignStatus && smeFormFilledStatus == that.smeFormFilledStatus && sendApplication == that.sendApplication && invoiceGenerated == that.invoiceGenerated && fmarkGenerated == that.fmarkGenerated && endOfProductionStatus == that.endOfProductionStatus && divisionId == that.divisionId && sectionId == that.sectionId && versionNumber == that.versionNumber && attachedPlantId == that.attachedPlantId && attachedPlantRemarks == that.attachedPlantRemarks && description == that.description && sta10FilledOfficerStatus == that.sta10FilledOfficerStatus && hofId == that.hofId && qamId == that.qamId && qaoId == that.qaoId && hofQamCompletenessRemarks == that.hofQamCompletenessRemarks && hofQamCompletenessStatus == that.hofQamCompletenessStatus && assignOfficerStatus == that.assignOfficerStatus && paidStatus == that.paidStatus && rmId == that.rmId && hodId == that.hodId && inspectionDate == that.inspectionDate && inspectionScheduledStatus == that.inspectionScheduledStatus && generateSchemeStatus == that.generateSchemeStatus && compliantStatus == that.compliantStatus && justificationReportStatus == that.justificationReportStatus && justificationReportRemarks == that.justificationReportRemarks && assignAssessorStatus == that.assignAssessorStatus && assessorId == that.assessorId && assessmentScheduledStatus == that.assessmentScheduledStatus && assessmentDate == that.assessmentDate && assessmentCriteria == that.assessmentCriteria && oldPermitStatus == that.oldPermitStatus && permitExpiredStatus == that.permitExpiredStatus && renewalStatus == that.renewalStatus && assessmentReportRemarks == that.assessmentReportRemarks && pacSecId == that.pacSecId && approvedRejectedScheme == that.approvedRejectedScheme && permitAwardStatus == that.permitAwardStatus && pacDecisionRemarks == that.pacDecisionRemarks && inspectionReportId == that.inspectionReportId && factoryInspectionReportApprovedRejectedStatus == that.factoryInspectionReportApprovedRejectedStatus && factoryInspectionReportApprovedRejectedRemarks == that.factoryInspectionReportApprovedRejectedRemarks && scfId == that.scfId && ssfId == that.ssfId && testReportId == that.testReportId && bsNumber == that.bsNumber && compliantRemarks == that.compliantRemarks && recommendationApprovalStatus == that.recommendationApprovalStatus && recommendationRemarks == that.recommendationRemarks && recommendationApprovalRemarks == that.recommendationApprovalRemarks && pscMemberId == that.pscMemberId && pcmId == that.pcmId && pscMemberApprovalStatus == that.pscMemberApprovalStatus && pscMemberApprovalRemarks == that.pscMemberApprovalRemarks && pcmApprovalStatus == that.pcmApprovalStatus && pcmApprovalRemarks == that.pcmApprovalRemarks && permitStatus == that.permitStatus && resubmitApplicationStatus == that.resubmitApplicationStatus && sendForPcmReview == that.sendForPcmReview && hodApproveAssessmentStatus == that.hodApproveAssessmentStatus && hodApproveAssessmentRemarks == that.hodApproveAssessmentRemarks && awardedPermitNumber == that.awardedPermitNumber && endOfProductionRemarks == that.endOfProductionRemarks && requestStatus == that.requestStatus && brandNameRequest == that.brandNameRequest && brandNameRequestStatus == that.brandNameRequestStatus && brandNameRequestRemarks == that.brandNameRequestRemarks && brandNameRequestApproval == that.brandNameRequestApproval && brandNameRequestStatusApproval == that.brandNameRequestStatusApproval && brandNameRequestRemarksApproval == that.brandNameRequestRemarksApproval && endProductionRequest == that.endProductionRequest && endProductionRequestStatus == that.endProductionRequestStatus && endProductionRequestRemarks == that.endProductionRequestRemarks && endProductionRequestApproval == that.endProductionRequestApproval && endProductionRequestStatusApproval == that.endProductionRequestStatusApproval && endProductionRequestRemarksApproval == that.endProductionRequestRemarksApproval
    }

    override fun hashCode(): Int {
        return Objects.hash(
            id,
            companyId,
            permitRejectedCreatedVersion,
            designation,
            userTaskId,
            factoryVisit,
            inspectionReportGenerated,
            cocId,
            resubmitRemarks,
            email,
            faxNo,
            firmName,
            applicantName,
            physicalAddress,
            plotNo,
            position,
            postalAddress,
            region,
            telephoneNo,
            vatNo,
            sscId,
            createdBy,
            createdOn,
            dateOfIssue,
            dateOfVisit,
            deleteBy,
            deletedOn,
            ksNumber,
            modifiedBy,
            modifiedOn,
            permitType,
            productName,
            title,
            totalCost,
            totalPayment,
            tradeMark,
            varField1,
            varField10,
            varField2,
            varField3,
            varField4,
            varField5,
            varField6,
            varField7,
            varField8,
            varField9,
            userId,
            status,
            permitRefNumber,
            dateOfExpiry,
            commodityDescription,
            enabled,
            broadProductCategory,
            productCategory,
            product,
            productSubCategory,
            standardCategory,
            productStandard,
            sta3FilledStatus,
            sta10FilledStatus,
            permitForeignStatus,
            smeFormFilledStatus,
            sendApplication,
            invoiceGenerated,
            fmarkGenerated,
            endOfProductionStatus,
            divisionId,
            sectionId,
            versionNumber,
            attachedPlantId,
            attachedPlantRemarks,
            description,
            sta10FilledOfficerStatus,
            hofId,
            qamId,
            qaoId,
            hofQamCompletenessRemarks,
            hofQamCompletenessStatus,
            assignOfficerStatus,
            paidStatus,
            rmId,
            hodId,
            inspectionDate,
            inspectionScheduledStatus,
            generateSchemeStatus,
            compliantStatus,
            justificationReportStatus,
            justificationReportRemarks,
            assignAssessorStatus,
            assessorId,
            assessmentScheduledStatus,
            assessmentDate,
            assessmentCriteria,
            oldPermitStatus,
            permitExpiredStatus,
            renewalStatus,
            assessmentReportRemarks,
            pacSecId,
            approvedRejectedScheme,
            permitAwardStatus,
            pacDecisionRemarks,
            inspectionReportId,
            factoryInspectionReportApprovedRejectedStatus,
            factoryInspectionReportApprovedRejectedRemarks,
            scfId,
            ssfId,
            testReportId,
            bsNumber,
            compliantRemarks,
            recommendationApprovalStatus,
            recommendationRemarks,
            recommendationApprovalRemarks,
            pscMemberId,
            pcmId,
            pscMemberApprovalStatus,
            pscMemberApprovalRemarks,
            pcmApprovalStatus,
            pcmApprovalRemarks,
            permitStatus,
            resubmitApplicationStatus,
            sendForPcmReview,
            hodApproveAssessmentStatus,
            hodApproveAssessmentRemarks,
            awardedPermitNumber,
            endOfProductionRemarks,
            requestStatus,
            brandNameRequest,
            brandNameRequestStatus,
            brandNameRequestRemarks,
            brandNameRequestApproval,
            brandNameRequestStatusApproval,
            brandNameRequestRemarksApproval,
            endProductionRequest,
            endProductionRequestStatus,
            endProductionRequestRemarks,
            endProductionRequestApproval,
            endProductionRequestStatusApproval,
            endProductionRequestRemarksApproval
        )
    }

}