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

    @Column(name = "BROAD_PRODUCT_CATEGORY")
    @Basic
    var broadProductCategory: Long? = null

    @Column(name = "PRODUCT")
    @Basic
    var product: Long? = null

    @Column(name = "STANDARD_CATEGORY")
    @Basic
    var standardCategory: Long? = null

    @Column(name = "PRODUCT_SUB_CATEGORY")
    @Basic
    var productSubCategory: Long? = null

    @Column(name = "DEPARTMENT")
    @Basic
    var department: String? = null

    @Column(name = "UUID")
    @Basic
    var uuid: String? = null

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

    @JoinColumn(name = "SAMPLE_SUBMITTED_ID", referencedColumnName = "ID")
    @ManyToOne
    var sampleSubmittedId: CdSampleSubmissionItemsEntity? = null

    @Column(name = "DIVISION")
    @Basic
    var division: String? = null

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

    @Column(name = "ONSITE_END_STATUS")
    @Basic
    var onsiteEndStatus: Int? = 0

    @Column(name = "DESTRUCTION_STATUS")
    @Basic
    var destractionStatus: Int? = 0

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

    @Column(name = "STATUS")
    @Basic
    var status: Int? = null

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

    @Column(name = "COMPLAINT_DEPARTMENT")
    @Basic
    var complaintDepartment: Long? = null



//    @JoinColumn(name = "DESTRUCTION_DOC_ID", referencedColumnName = "ID")
//    @ManyToOne
//    var destractionDocId: MsOnsiteUploadsEntity? = null

//    @JoinColumn(name = "COMPLAINT_DEPARTMENT", referencedColumnName = "ID")
//    @ManyToOne
//    var complaintDepartment: DepartmentsEntity? = null

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as MsWorkPlanGeneratedEntity
        return id == that.id &&
                divisionId == that.divisionId &&
                msTypeId == that.msTypeId &&
                uuid == that.uuid &&
                complaintDepartment == that.complaintDepartment &&
                destructionDocId == that.destructionDocId &&
                officerId == that.officerId &&
                complaintId == that.complaintId &&
                standardCategory == that.standardCategory &&
                productSubCategory == that.productSubCategory &&
                product == that.product &&
                productCategory == that.productCategory &&
                broadProductCategory == that.broadProductCategory &&
                referenceNumber == that.referenceNumber &&
                workPlanYearId == that.workPlanYearId &&
                department == that.department &&
                division == that.division &&
                officerName == that.officerName &&
                nameActivity == that.nameActivity &&
                targetedProducts == that.targetedProducts &&
                resourcesRequired == that.resourcesRequired &&
                budget == that.budget &&
                rejected == that.rejected &&
                sampleSubmittedId == that.sampleSubmittedId &&
                rejectedOn == that.rejectedOn &&
                rejectedBy == that.rejectedBy &&
                rejectedStatus == that.rejectedStatus &&
                approved == that.approved &&
                approvedBy == that.approvedBy &&
                approvedOn == that.approvedOn &&
                approvedStatus == that.approvedStatus &&
                rejectedRemarks == that.rejectedRemarks &&
                approvedRemarks == that.approvedRemarks &&
                chargeSheetStatus == that.chargeSheetStatus &&
                dataReportStatus == that.dataReportStatus &&
                sampleCollectionStatus == that.sampleCollectionStatus &&
                sampleSubmittedStatus == that.sampleSubmittedStatus &&
                investInspectReportStatus == that.investInspectReportStatus &&
                seizureDeclarationStatus == that.seizureDeclarationStatus &&
                onsiteStartStatus == that.onsiteStartStatus &&
                onsiteEndStatus == that.onsiteEndStatus &&
                sendSffStatus == that.sendSffStatus &&
                msPreliminaryReportStatus == that.msPreliminaryReportStatus &&
                preliminaryApprovedStatus == that.preliminaryApprovedStatus &&
                msFinalReportStatus == that.msFinalReportStatus &&
                finalApprovedStatus == that.finalApprovedStatus &&
                ssfLabparamsStatus == that.ssfLabparamsStatus &&
                scfLabparamsStatus == that.scfLabparamsStatus &&
                dataReportGoodsStatus == that.dataReportGoodsStatus &&
                bsNumberStatus == that.bsNumberStatus &&
                preliminaryParamStatus == that.preliminaryParamStatus &&
                hodRecommendationStatus == that.hodRecommendationStatus &&
                hodRecommendationStart == that.hodRecommendationStart &&
                hodRecommendation == that.hodRecommendation &&
                hodRecommendationRemarks == that.hodRecommendationRemarks &&
                clientAppealed == that.clientAppealed &&
                destractionStatus == that.destractionStatus &&
                confirmDivisionId == that.confirmDivisionId &&
                progressValue == that.progressValue &&
                progressStep == that.progressStep &&
                onsiteStartDate == that.onsiteStartDate &&
                onsiteEndDate == that.onsiteEndDate &&
                sendSffDate == that.sendSffDate &&
                recommendationId == that.recommendationId &&
                destructionNotificationStatus == that.destructionNotificationStatus &&
                destructionNotificationDate == that.destructionNotificationDate &&
                destructionClientEmail == that.destructionClientEmail &&
                msEndProcessRemarks == that.msEndProcessRemarks &&
                process == that.process &&
                region == that.region &&
                county == that.county &&
                subcounty == that.subcounty &&
                townMarketCenter == that.townMarketCenter &&
                locationActivityOther == that.locationActivityOther &&
                timeActivityDate == that.timeActivityDate &&
                timeDateReportSubmitted == that.timeDateReportSubmitted &&
                timeActivityRemarks == that.timeActivityRemarks &&
                rescheduledDateNotVisited == that.rescheduledDateNotVisited &&
                rescheduledDateReportSubmitted == that.rescheduledDateReportSubmitted &&
                rescheduledActivitiesRemarks == that.rescheduledActivitiesRemarks &&
                activityUndertakenPeriod == that.activityUndertakenPeriod &&
                nameHof == that.nameHof &&
                reviewSupervisorDate == that.reviewSupervisorDate &&
                reviewSupervisorRemarks == that.reviewSupervisorRemarks &&
                status == that.status &&
                varField1 == that.varField1 &&
                varField2 == that.varField2 &&
                varField3 == that.varField3 &&
                varField4 == that.varField4 &&
                varField5 == that.varField5 &&
                varField6 == that.varField6 &&
                varField7 == that.varField7 &&
                varField8 == that.varField8 &&
                varField9 == that.varField9 &&
                varField10 == that.varField10 &&
                createdBy == that.createdBy &&
                createdOn == that.createdOn &&
                modifiedBy == that.modifiedBy &&
                modifiedOn == that.modifiedOn &&
                deleteBy == that.deleteBy &&
                deletedOn == that.deletedOn
    }

    override fun hashCode(): Int {
        return Objects.hash(id,workPlanYearId,divisionId,
                referenceNumber, department, division, officerName, nameActivity, targetedProducts, resourcesRequired, budget,
                region,
                uuid,
                msTypeId,
                complaintDepartment,
                destructionDocId,
                officerId,
                complaintId,
                approved,
                standardCategory,
                productSubCategory,
                product,
                productCategory,
                broadProductCategory,
                progressValue , onsiteStartDate, onsiteEndDate, sendSffDate,recommendationId,msEndProcessRemarks,process,confirmDivisionId,destructionNotificationDate, destructionNotificationStatus, destructionClientEmail,
                progressStep ,preliminaryApprovedStatus,finalApprovedStatus, hodRecommendationStatus,clientAppealed, destractionStatus, hodRecommendationStart, hodRecommendation, hodRecommendationRemarks, sendSffStatus, preliminaryParamStatus, bsNumberStatus, scfLabparamsStatus, dataReportGoodsStatus, ssfLabparamsStatus, msFinalReportStatus, msPreliminaryReportStatus, onsiteStartStatus, onsiteEndStatus, sampleSubmittedStatus, seizureDeclarationStatus, investInspectReportStatus, sampleCollectionStatus, dataReportStatus, chargeSheetStatus, approvedRemarks, rejectedRemarks, approvedBy, approvedOn, approvedStatus, rejected, rejectedOn, rejectedBy, rejectedStatus, county, subcounty, townMarketCenter, locationActivityOther, timeActivityDate, timeDateReportSubmitted, timeActivityRemarks, rescheduledDateNotVisited, rescheduledDateReportSubmitted, rescheduledActivitiesRemarks, activityUndertakenPeriod, nameHof, reviewSupervisorDate, reviewSupervisorRemarks, status, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn)

    }
}
