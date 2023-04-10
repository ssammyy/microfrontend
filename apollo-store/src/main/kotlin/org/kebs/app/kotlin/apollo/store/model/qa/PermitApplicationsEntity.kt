package org.kebs.app.kotlin.apollo.store.model.qa

import java.io.Serializable
import java.math.BigDecimal
import java.sql.Date
import java.sql.Timestamp
import javax.persistence.*


@Entity
@Table(name = "DAT_KEBS_PERMIT_TRANSACTION")
class PermitApplicationsEntity : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(
        name = "DAT_KEBS_PERMIT_TRANSACTION_SEQ_GEN",
        allocationSize = 1,
        sequenceName = "DAT_KEBS_PERMIT_TRANSACTION_SEQ"
    )
    @GeneratedValue(generator = "DAT_KEBS_PERMIT_TRANSACTION_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = 0

    @Column(name = "APPLICANT_NAME")
    @Basic
    var applicantName: String? = null

    @Column(name = "FIRM_NAME")
    @Basic
    var firmName: String? = null

    @Column(name = "PERMIT_FEE_TOKEN")
    @Basic
    var permitFeeToken: String? = null

    @Column(name = "APPROVED_REJECTED_SCHEME_REMARKS")
    @Basic
    var approvedRejectedSchemeRemarks: String? = null

    @Column(name = "SSF_COMPLETED_STATUS")
    @Basic
    var ssfCompletedStatus: Int? = null

    @Column(name = "DELETED_STATUS")
    @Basic
    var deletedStatus: Int? = null


    @Column(name = "APPLICATION_STATUS")
    @Basic
    var applicationStatus: Int? = null

    @Column(name = "PROCESS_STEP")
    @Basic
    var processStep: Int? = null

    @Column(name = "PROCESS_STEP_NAME")
    @Basic
    var processStepName: String? = null

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

    @Column(name = "EFFECTIVE_DATE")
    @Basic
    var effectiveDate: Date? = null


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

    @Column(name = "ASSESSMENT_REPORT_ADDED_STATUS")
    @Basic
    var assessmentReportAddedStatus: Int? = null

    @Column(name = "LAB_RESULTS_OUTSIDE_ADDED_STATUS")
    @Basic
    var labResultsOutsideAddedStatus: Int? = null

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

    @Column(name = "JUSTIFICATION_REPORT_APPROVED")
    @Basic
    var justificationReportApproved: Int? = null

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

    @Column(name = "SUSPENSION_STATUS")
    @Basic
    var suspensionStatus: Int? = null

    @Column(name = "SUSPENSION_REMARKS")
    @Basic
    var suspensionRemarks: String? = null


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

    @Column(name = "SMARK_GENERATED_FROM")
    @Basic
    var smarkGeneratedFrom: Int? = null

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

//    @Column(name = "SUSPENSION_STATUS")
//    @Basic
//    var applicationSuspensionStatus: Int? = null

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

    @Column(name = "CHANGES_MADE_STATUS")
    @Basic
    var changesMadeStatus: Int? = null

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
    var fmarkGenerated: Int? = 0

    @Column(name = "FMARK_GENERATE_STATUS")
    @Basic
    var fmarkGenerateStatus: Int? = null

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

    @Column(name = "INVOICE_DIFFERENCE_GENERATED")
    @Basic
    var invoiceDifferenceGenerated: Int? = null


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


    @Column(name = "LEAD_ASSESSOR_ID")
    @Basic
    var leadAssessorId: Long? = null

    @Column(name = "RESUBMIT_APPLICATION_STATUS")
    @Basic
    var resubmitApplicationStatus: Int? = null

    @Column(name = "END_OF_PRODUCTION_REMARKS")
    @Basic
    var endOfProductionRemarks: String? = null

    @Column(name = "HOD_QAM_APPROVE_REJECT_STATUS")
    @Basic
    var hodQamApproveRejectStatus: Int? = null

    @Column(name = "HOD_QAM_APPROVE_REJECT_REMARKS")
    @Basic
    var hodQamApproveRejectRemarks: String? = null

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


    @Column(name = "PCM_REVIEW_APPROVAL_STATUS")
    @Basic
    var pcmReviewApprovalStatus: Int? = null


    @Column(name = "PCM_REVIEW_APPROVAL_REMARKS")
    @Basic
    var pcmReviewApprovalRemarks: String? = null


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

    @Column(name = "PAC_DECISION_STATUS")
    @Basic
    var pacDecisionStatus: Int? = null

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
    var dateOfVisit: Timestamp? = null

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


}
