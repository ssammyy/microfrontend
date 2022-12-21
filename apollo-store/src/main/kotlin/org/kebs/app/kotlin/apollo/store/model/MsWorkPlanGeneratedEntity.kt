package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Date
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_MS_WORKPLAN_GENARATED")
class MsWorkPlanGeneratedEntity : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_MS_WORKPLAN_GENARATED_SEQ_GEN", allocationSize = 1, sequenceName = "DAT_KEBS_MS_WORKPLAN_GENARATED_SEQ")
    @GeneratedValue(generator = "DAT_KEBS_MS_WORKPLAN_GENARATED_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long = 0

    @Transient
    var process: String? = null

    @Column(name = "PRODUCT_CATEGORY")
    @Basic
    var productCategory: Long? = null

  @Column(name = "PRODUCT_CATEGORY_STRING")
  @Basic
  var productCategoryString: String? = null

  @Column(name = "TOTAL_COMPLIANCE")
  @Basic
  var totalCompliance: String? = null

    @Column(name = "BROAD_PRODUCT_CATEGORY")
    @Basic
    var broadProductCategory: Long? = null

  @Column(name = "BROAD_PRODUCT_CATEGORY_STRING")
  @Basic
  var broadProductCategoryString: String? = null

    @Column(name = "PRODUCT")
    @Basic
    var product: Long? = null

  @Column(name = "PRODUCT_STRING")
  @Basic
  var productString: String? = null

    @Column(name = "HOD_RM_ASSIGNED")
    @Basic
    var hodRmAssigned: Long? = null

    @Column(name = "HOF_ASSIGNED")
    @Basic
    var hofAssigned: Long? = null

    @Column(name = "DIRECTOR_ASSIGNED")
    @Basic
    var directorAssigned: Long? = null

    @Column(name = "STANDARD_CATEGORY")
    @Basic
    var standardCategory: Long? = null

  @Column(name = "STANDARD_CATEGORY_STRING")
  @Basic
  var standardCategoryString: String? = null

    @Column(name = "PRODUCT_SUB_CATEGORY")
    @Basic
    var productSubCategory: Long? = null

  @Column(name = "PRODUCT_SUB_CATEGORY_STRING")
  @Basic
  var productSubCategoryString: String? = null

    @Column(name = "DEPARTMENT")
    @Basic
    var department: String? = null

    @Column(name = "UUID")
    @Basic
    var uuid: String? = null

    @Column(name = "RATIONALE")
    @Basic
    var rationale: String? = null

  @Column(name = "SCOPE_OF_COVERAGE")
    @Basic
    var scopeOfCoverage: String? = null

    @Column(name = "MS_TYPE_ID")
    @Basic
    var msTypeId: Long? = null

    @Column(name = "DIVISION_ID")
    @Basic
    var divisionId: Long? = null

    @Column(name = "MS_PROCESS_ID")
    @Basic
    var msProcessId: Long? = null

    @Column(name = "USER_TASK_ID")
    @Basic
    var userTaskId: Long? = null

//    @JoinColumn(name = "DIVISION_ID", referencedColumnName = "ID")
//    @ManyToOne
//    var divisionId: DivisionsEntity? = null

    @Transient
    var confirmDivisionId: Long? = 0

//    @JoinColumn(name = "SAMPLE_SUBMITTED_ID", referencedColumnName = "ID")
//    @ManyToOne
//    var sampleSubmittedId: CdSampleSubmissionItemsEntity? = null

  @Column(name = "STANDARD_TITLE")
  @Basic
  var standardTitle: String? = null

  @Column(name = "STANDARD_NUMBER")
  @Basic
  var standardNumber: String? = null

    @Column(name = "COMPLIANT_STATUS")
    @Basic
    var compliantStatus: Int? = null

    @Column(name = "WORK_PLAN_COMPLIANT_STATUS")
    @Basic
    var workPlanCompliantStatus: Int? = null

    @Column(name = "SUBMITTED_FOR_APPROVAL_STATUS")
    @Basic
    var submittedForApprovalStatus: Int? = null

    @Column(name = "FINAL_REPORT_GENERATED")
    @Basic
    var finalReportGenerated: Int? = null

    @Column(name = "MS_PROCESS_ENDED_STATUS")
    @Basic
    var msProcessEndedStatus: Int? = null

  @Column(name = "MS_PROCESS_ENDED_ON")
  @Basic
  var msProcessEndedOn: Date? = null

    @Column(name = "NOT_COMPLIANT_STATUS")
    @Basic
    var notCompliantStatus: Int? = null

    @Column(name = "COMPLIANT_STATUS_DATE")
    @Basic
    var compliantStatusDate: Date? = null

    @Column(name = "COMPLIANT_STATUS_BY")
    @Basic
    var compliantStatusBy: String? = null

    @Column(name = "COMPLIANT_STATUS_REMARKS")
    @Basic
    var compliantStatusRemarks: String? = null

    @Column(name = "NOT_COMPLIANT_STATUS_BY")
    @Basic
    var notCompliantStatusBy: String? = null

    @Column(name = "NOT_COMPLIANT_STATUS_DATE")
    @Basic
    var notCompliantStatusDate: Date? = null

    @Column(name = "NOT_COMPLIANT_STATUS_REMARKS")
    @Basic
    var notCompliantStatusRemarks: String? = null

    @Column(name = "DIVISION")
    @Basic
    var division: String? = null

    @Column(name = "UPDATED_REMARKS")
    @Basic
    var updatedRemarks: String? = null

    @Column(name = "OFFICER_NAME")
    @Basic
    var officerName: String? = null

    @Column(name = "NAME_ACTIVITY")
    @Basic
    var nameActivity: String? = null

    @Column(name = "TARGETED_PRODUCTS")
    @Basic
    var targetedProducts: String? = null

    @Column(name = "RESOURCES_REQUIRED")
    @Basic
    var resourcesRequired: String? = null

    @Column(name = "BUDGET")
    @Basic
    var budget: String? = null

    @Column(name = "APPROVED_ON")
    @Basic
    var approvedOn: Date? = null

    @Column(name = "APPROVED_STATUS")
    @Basic
    var approvedStatus: Int? = 0

    @Column(name = "WORKPLAN_YEAR_ID")
    @Basic
    var workPlanYearId: Long? = 0

    @Column(name = "CLIENT_APPEALED")
    @Basic
    var clientAppealed: Int? = 0

    @Column(name = "HOD_RECOMMENDATION_STATUS")
    @Basic
    var hodRecommendationStatus: Int? = 0

    @Column(name = "DIRECTOR_RECOMMENDATION_REMARKS_STATUS")
    @Basic
    var directorRecommendationRemarksStatus: Int? = 0

    @Column(name = "DESTRUCTION_RECOMMENDED")
    @Basic
    var destructionRecommended: Int? = 0

    @Column(name = "HOD_RECOMMENDATION_START")
    @Basic
    var hodRecommendationStart: Int? = 0

    @Transient
    var recommendationId: Long? = 0

    @Column(name = "HOD_RECOMMENDATION")
    @Basic
    var hodRecommendation: String? = null

    @Column(name = "DESTRUCTION_NOTIFICATION_STATUS")
    @Basic
    var destructionNotificationStatus: Int? = 0

    @Column(name = "DESTRUCTION_NOTIFICATION_DATE")
    @Basic
    var destructionNotificationDate: Date? = null

    @Column(name = "TIMELINE_START_DATE")
    @Basic
    var timelineStartDate: Date? = null

    @Column(name = "TIMELINE_END_DATE")
    @Basic
    var timelineEndDate: Date? = null

    @Column(name = "HOD_RECOMMENDATION_REMARKS")
    @Basic
    var hodRecommendationRemarks: String? = null

    @Column(name = "PRELIMINARY_PARAM_STATUS")
    @Basic
    var preliminaryParamStatus: Int? = 0

    @Column(name = "DATA_REPORT_GOODS_STATUS")
    @Basic
    var dataReportGoodsStatus: Int? = 0

    @Column(name = "SCF_LABPARAMS_STATUS")
    @Basic
    var scfLabparamsStatus: Int? = 0

    @Column(name = "BS_NUMBER_STATUS")
    @Basic
    var bsNumberStatus: Int? = 0

    @Column(name = "BS_NUMBER_DATED_ADDED")
    @Basic
    var bsNumberDatedAdded: Date? = null

    @Column(name = "SSF_LABPARAMS_STATUS")
    @Basic
    var ssfLabparamsStatus: Int? = 0

    @Column(name = "MS_PRELIMINARY_REPORT_STATUS")
    @Basic
    var msPreliminaryReportStatus: Int? = 0

    @Column(name = "PRELIMINARY_APPROVED_STATUS")
    @Basic
    var preliminaryApprovedStatus: Int? = 0

    @Column(name = "MS_FINAL_REPORT_STATUS")
    @Basic
    var msFinalReportStatus: Int? = 0

    @Column(name = "FINAL_APPROVED_STATUS")
    @Basic
    var finalApprovedStatus: Int? = 0

    @Column(name = "CHARGE_SHEET_STATUS")
    @Basic
    var chargeSheetStatus: Int? = 0

    @Column(name = "FIELD_REPORT_STATUS")
    @Basic
    var fieldReportStatus: Int? = 0

    @Column(name = "INVEST_INSPECT_REPORT_STATUS")
    @Basic
    var investInspectReportStatus: Int? = 0

    @Column(name = "SAMPLE_COLLECTION_STATUS")
    @Basic
    var sampleCollectionStatus: Int? = 0

    @Column(name = "SAMPLE_SUBMITTED_STATUS")
    @Basic
    var sampleSubmittedStatus: Int? = 0

    @Column(name = "SEIZURE_DECLARATION_STATUS")
    @Basic
    var seizureDeclarationStatus: Int? = 0

    @Column(name = "DATA_REPORT_STATUS")
    @Basic
    var dataReportStatus: Int? = 0

    @Column(name = "DATA_REPORT_DATE")
    @Basic
    var dataReportDate: Date? = null

    @Column(name = "APPROVED_BY")
    @Basic
    var approvedBy: String? = null

    @Column(name = "APPROVED")
    @Basic
    var approved: String? = null

    @Column(name = "REJECTED_ON")
    @Basic
    var rejectedOn: Date? = null

    @Column(name = "REJECTED_STATUS")
    @Basic
    var rejectedStatus: Int? = 0

    @Column(name = "ONSITE_START_STATUS")
    @Basic
    var onsiteStartStatus: Int? = 0

    @Column(name = "ONSITE_START_DATE")
    @Basic
    var onsiteStartDate: Date? = null

    @Column(name = "ONSITE_END_DATE")
    @Basic
    var onsiteEndDate: Date? = null

    @Column(name = "SEND_SSF_DATE")
    @Basic
    var sendSffDate: Date? = null

    @Column(name = "SEND_SSF_STATUS")
    @Basic
    var sendSffStatus: Int? = 0

    @Column(name = "RECOMMENDATION_DONE_STATUS")
    @Basic
    var recommendationDoneStatus: Int? = 0

    @Column(name = "ONSITE_END_STATUS")
    @Basic
    var onsiteEndStatus: Int? = 0

    @Column(name = "DESTRUCTION_STATUS")
    @Basic
    var destractionStatus: Int? = 0

    @Column(name = "APPEAL_STATUS")
    @Basic
    var appealStatus: Int? = 0

    @Column(name = "REJECTED_BY")
    @Basic
    var rejectedBy: String? = null

    @Column(name = "REJECTED")
    @Basic
    var rejected: String? = null

    @Column(name = "MS_END_PROCESS_REMARKS")
    @Basic
    var msEndProcessRemarks: String? = null

    @Column(name = "REJECTED_REMARKS")
    @Basic
    var rejectedRemarks: String? = null

    @Column(name = "APPROVED_REMARKS")
    @Basic
    var approvedRemarks: String? = null

    @Column(name = "PROGRESS_VALUE")
    @Basic
    var progressValue: Int? = null

  @Column(name = "REPORT_PENDING_REVIEW")
    @Basic
    var reportPendingReview: Int? = null

    @Column(name = "PROGRESS_STEP")
    @Basic
    var progressStep: String? = null

    @Column(name = "COUNTY")
    @Basic
    var county: Long? = null

    @Column(name = "SUBCOUNTY")
    @Basic
    var subcounty: String? = null

    @Column(name = "TOWN_MARKET_CENTER")
    @Basic
    var townMarketCenter: Long? = null

    @Column(name = "LOCATION_ACTIVITY_OTHER")
    @Basic
    var locationActivityOther: String? = null

    @Column(name = "TIME_ACTIVITY_DATE")
    @Basic
    var timeActivityDate: Date? = null

    @Column(name = "TIME_ACTIVITY_END_DATE")
    @Basic
    var timeActivityEndDate: Date? = null

    @Column(name = "TIME_DATE_REPORT_SUBMITTED")
    @Basic
    var timeDateReportSubmitted: Date? = null

    @Column(name = "TIME_ACTIVITY_REMARKS")
    @Basic
    var timeActivityRemarks: String? = null

    @Column(name = "RESCHEDULED_DATE_NOT_VISITED")
    @Basic
    var rescheduledDateNotVisited: Date? = null

    @Column(name = "RESCHEDULED_DATE_REPORT_SUBMITTED")
    @Basic
    var rescheduledDateReportSubmitted: Date? = null

    @Column(name = "RESCHEDULED_ACTIVITIES_REMARKS")
    @Basic
    var rescheduledActivitiesRemarks: String? = null

    @Column(name = "ACTIVITY_UNDERTAKEN_PERIOD")
    @Basic
    var activityUndertakenPeriod: String? = null

    @Column(name = "NAME_HOF")
    @Basic
    var nameHof: String? = null

    @Column(name = "REVIEW_SUPERVISOR_DATE")
    @Basic
    var reviewSupervisorDate: Date? = null

    @Column(name = "REVIEW_SUPERVISOR_REMARKS")
    @Basic
    var reviewSupervisorRemarks: String? = null

    @Column(name = "DESTRUCTION_CLIENT_EMAIL")
    @Basic
    var destructionClientEmail: String? = null

    @Column(name = "DESTRUCTION_CLIENT_FULL_NAME")
    @Basic
    var destructionClientFullName: String? = null

    @Column(name = "STATUS")
    @Basic
    var status: Int? = null

    @Column(name = "UPDATED_STATUS")
    @Basic
    var updatedStatus: Int? = null

    @Column(name = "RESUBMIT_STATUS")
    @Basic
    var resubmitStatus: Int? = null

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

    @Column(name = "REGION")
    @Basic
    var region: Long? = null

    @Column(name = "COMPLAINT_ID")
    @Basic
    var complaintId: Long? = null

    @Column(name = "OFFICER_ID")
    @Basic
    var officerId: Long? = null

    @Column(name = "DESTRUCTION_DOC_ID")
    @Basic
    var destructionDocId: Long? = null

    @Column(name = "SCF_DOC_ID")
    @Basic
    var scfDocId: Long? = null

    @Column(name = "SSF_DOC_ID")
    @Basic
    var ssfDocId: Long? = null

    @Column(name = "SEIZURE_DOC_ID")
    @Basic
    var seizureDocId: Long? = null

    @Column(name = "DECLARATION_DOC_ID")
    @Basic
    var declarationDocId: Long? = null

    @Column(name = "CHARGE_SHEET_DOC_ID")
    @Basic
    var chargeSheetDocId: Long? = null

    @Column(name = "DATA_REPORT_DOC_ID")
    @Basic
    var dataReportDocId: Long? = null

    @Column(name = "DESTRUCTION_NOTIFICATION_DOC_ID")
    @Basic
    var destructionNotificationDocId: Long? = null

    @Column(name = "COMPLAINT_DEPARTMENT")
    @Basic
    var complaintDepartment: Long? = null


    @Column(name = "REFERENCE_NUMBER")
    @Basic
    var referenceNumber: String? = null

    @Column(name = "MS_STATUS")
    @Basic
    var msStatus: Int? = null

    @Column(name = "MS_STARTED_ON")
    @Basic
    var msStartedOn: Timestamp? = null

    @Column(name = "MS_COMPLETED_ON")
    @Basic
    var msCompletedOn: Timestamp? = null

    @Column(name = "MS_PROCESS_INSTANCE_ID")
    @Basic
    var msProcessInstanceId: String? = null

}
