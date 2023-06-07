package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_PERMIT")
class PermitApplicationEntity: Serializable {
    @Column(name = "ID")
    @Id
    @SequenceGenerator(name = "DAT_KEBS_PERMIT_SEQ_GEN", sequenceName = "DAT_KEBS_PERMIT_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_PERMIT_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long? = 0


    @Column(name = "KEBS_PRODUCT_CATEGORY")
    @Basic
    var kebsProductCategory: String? = null

    @Column(name = "DEFERRAL_FEEDBACK")
    @Basic
    var deferralFeedback: String? = null

    /*
    @JoinColumn(name = "SAMPLE_SUBMITTED_ID", referencedColumnName = "ID")
    @ManyToOne
    var sampleSubmittedId: CdSampleSubmissionItemsEntity? = null
    */

    @Column(name = "SAMPLE_SUBMITTED_ID")
    @Basic
    var sampleSubmittedId: Long? = null

    @Column(name = "SAMPLE_COLLECTION_STATUS")
    @Basic
    var sampleCollectionStatus: Int? = null

    @Column(name = "SAMPLE_SUBMITTED_STATUS")
    @Basic
    var sampleSubmittedStatus: Int? = null

    @Column(name = "JUSTIFICATION_REPORT_STATUS")
    @Basic
    var justificationReportStatus: Int? = null

    @Column(name = "EXTRA_DOCUMENTS")
    @Basic
    var extraDocuments: String? = null


    @Column(name = "NO_OF_SITES_PRODUCING_THE_PRODUCT")
    @Basic
    var noOfSitesProducingTheBrand: Long? = null

    @Column(name = "LAB_COMPLIANCE_STATUS")
    @Basic
    var labComplianceStatus: Int? = null

    @Column(name = "PERMIT_NUMBER")
    @Basic
    var permitNumber: String? = null

    @Column(name = "PRODUCT_SUB_CATEGORY")
    @Basic
    var productSubCategory: Long? = null

//    @JoinColumn(name = "PRODUCT_SUB_CATEGORY", referencedColumnName = "ID")
//    @ManyToOne
//    var productSubCategory: ProductSubcategoryEntity? = null

    @Column(name = "PERMIT_TYPE")
    @Basic
    var permitType: Long? = null


    @Column(name = "PERMIT_AWARD_STATUS_PSC_PCM")
    @Basic
    var permitAwardStatusPscPcm: Int? = null

//    @JoinColumn(name = "PERMIT_TYPE", referencedColumnName = "ID")
//    @ManyToOne
//    var permitType: PermitTypesEntity? = null

    @JoinColumn(name = "MANUFACTURER_ENTITY", referencedColumnName = "ID")
    @ManyToOne
    var manufacturerEntity: ManufacturersEntity? = null

    @JoinColumn(name = "BRAND_ID", referencedColumnName = "ID")
    @ManyToOne
    var brandId: ManufactureBrandEntity? = null

    @JoinColumn(name = "PROCESS_STATUS", referencedColumnName = "ID")
    @ManyToOne
    var processStatus: ProcessStatusEntity? = null

    @Column(name = "LAB_REPORT_STATUS")
    @Basic
    var labReportStatus: Int? = null

    @Column(name = "HOF_PERMIT_RECOMMENDATION")
    @Basic
    var hofPermitRecommendation: Int? = null

    @Column(name = "FACTORY_INSPECTION_STATUS")
    @Basic
    var factoryInspectionStatus: Int? = null


    @Column(name = "DESCRIPTIONS")
    @Basic
    var description: String? = null

    @Column(name = "SAMPLE_SUBMISSION_STATUS")
    @Basic
    var sampleSubmissionStatus: Int? = null

    @Column(name = "LAB_SAMPLES_ANALYSIS_COMPLETE")
    @Basic
    var labSamplesAnalysisComplete: Int? = null

    @Column(name = "LAB_REPORT_ACCEPTANCE_STATUS")
    @Basic
    var labReportAcceptanceStatus: Int? = null

    @Column(name = "COMPANY_COMPLIANCE_STATUS")
    @Basic
    var companyComplianceStatus: Int? = null

    @Column(name = "APPLICATION_COMPLETENESS_STATUS")
    @Basic
    var applicationCompletenessStatus: Int? = null

    @Column(name = "BS_NUMBER")
    @Basic
    var bsNumber: String? = null

    @Column(name = "TRADE_MARK")
    @Basic
    var tradeMark: String? = null

    @Column(name = "KS_NUMBER")
    @Basic
    var ksNumber: String? = null

    @Column(name = "STANDARD_TITLE")
    @Basic
    var standardTitle: String? = null

    @Column(name = "DATE_CREATED")
    @Basic
    var dateCreated: Timestamp? = null

    @Column(name = "STATUS")
    @Basic
    var status: Int? = 0

    @Column(name = "LAB_REPORTS_FILEPATH")
    @Basic
    var labReportsFilepath: String? = null

    @Column(name = "MANUFACTURER_ID")
    @Basic
    var manufacturerId: Long? = null

//    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
//    @ManyToOne
//    var userId: UsersEntity? = null

    @Column(name = "FACTORY_INSPECTION_REPORT_STATUS")
    @Basic
    var factoryInspectionReportStatus: Int? = null

    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    @ManyToOne
    var userId: UsersEntity? = null

    @JoinColumn(name = "STANDARD_ID", referencedColumnName = "ID")
    @ManyToOne
    var standardId: SampleStandardsEntity? = null

    @Column(name = "MANUFACTURER")
    @Basic
    var manufacturer: Long? = null

    @Column(name = "PERMIT_APPROVAL_STATUS_PSC")
    @Basic
    var permitApprovalStatusPsc: Int? = null


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

    @Column(name = "TOTAL_AMOUNT_TO_PAY")
    @Basic
    var totalAmountToPay: String? = null

    @Column(name = "MODIFIED_STATUS")
    @Basic
    var modifiedStatus: Long? = null

    @Column(name = "RENEWED_DATE")
    @Basic
    var renewedDate: Timestamp? = null

    @Column(name = "EXPIRY_DATE")
    @Basic
    var expiryDate: Timestamp? = null

    @Column(name = "VALIDITY_PERIOD")
    @Basic
    var validityPeriod: Long? = null

    @Column(name = "PAYMENT_STATUS")
    @Basic
    var paymentStatus: Int? = null

    @Column(name = "RENEWED_STATUS")
    @Basic
    var renewedStatus: Long? = null

    @Column(name = "SAVE_REASON")
    @Basic
    var saveReason: String? = null

    @Column(name = "PRODUCT_CATEGORY")
    @Basic
    var productCategory: Long? = null

    @Column(name = "BROAD_PRODUCT_CATEGORY")
    @Basic
    var broadProductCategory: Long? = null

    @Column(name = "PRODUCT")
    @Basic
    var product: Long? = null

//    @JoinColumn(name = "PRODUCT_CATEGORY", referencedColumnName = "ID")
//    @ManyToOne
//    var productCategory: KebsProductCategoriesEntity? = null
//
//    @JoinColumn(name = "BROAD_PRODUCT_CATEGORY", referencedColumnName = "ID")
//    @ManyToOne
//    var broadProductCategory: BroadProductCategoryEntity? = null
////
//    @JoinColumn(name = "PRODUCT", referencedColumnName = "ID")
//    @ManyToOne
//    var productId: ProductsEntity? = null

    @Column(name = "QAO")
    @Basic
    var qao: Long? = null

//    @JoinColumn(name = "QAO", referencedColumnName = "ID")
//    @ManyToOne
//    var qao: UsersEntity? = null

    @Column(name = "DATE_AWARDED")
    @Basic
    var dateAwarded: Timestamp? = null

    @Column(name = "SCHEME_OF_SUPERVISION")
    @Basic
    var schemeOfSupervision: String? = null

    @Column(name = "SITE_APPLICABLE")
    @Basic
    var siteApplicable: String? = null

    @Column(name = "MANUFACTURER_NAME")
    @Basic
    var manufacturerName: String? = null

    @Column(name = "FACTORY_INSPECTION_SCHEDULE_STATUS")
    @Basic
    var factoryInspectionScheduleStatus: Int? = null

    @Column(name = "SCHEME_OF_SUPERVISION_STATUS")
    @Basic
    var schemeOfSupervisionStatus: Int? = null

    @Column(name = "LAB_REPORTS_FILEPATH_1", nullable = true)
    @Basic
    var labReportsFilepath1: String? = null

    @Column(name = "LAB_REPORTS_FILEPATH_2", nullable = true)
    @Basic
    var labReportsFilepath2: String? = null

    @Column(name = "LAB_REPORTS_FILEPATH_3", nullable = true)
    @Basic
    var labReportsFilepath3: String? = null

    @Column(name = "SCHEME_OF_SUPERVISION_ACCEPTANCE_STATUS")
    @Basic
    var schemeOfSupervisionAcceptanceStatus: Int? = null

    @Column(name = "APP_REVIEW_PROCESS_INSTANCE_ID")
    @Basic
    var appReviewProcessInstanceId: String? = null

    @Column(name = "APP_REVIEW_TASK_ID")
    @Basic
    var appReviewTaskId: String? = null

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

    @Column(name = "SF_MARK_INSPECTION_TASK_ID")
    @Basic
    var sfMarkInspectionTaskId: String? = null

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

    @Column(name = "DMARK_CONFORMITY_STATUS_APPROVAL")
    @Basic
    var dmarkConformityStatusApproval: Int? = null

    @Column(name = "II_REPORTING_PROCESS_INSTANCE_ID")
    @Basic
    var iiReportingProcessInstanceId: String? = null

    @Column(name = "DM_ASSESSMENT_STATUS")
    @Basic
    var dmAssessmentStatus: Int? = null

    @Column(name = "PERMIT_SURVEILLANCE_COMPLETENESS_STATUS")
    @Basic
    var permitSurveillanceCompletenessStatus: Int? = null

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


    //    @Column(name = "CERTIFICATE_FILE")
//    @Basic
//    var certificateFile: String? = null
//
//    @Column(name = "TESTING_RECORD_FILE")
//    @Basic
//    var testingRecordFile: String? = null
//
//    @Column(name = "PROCESS_MONITORING_RECORDS_FILE")
//    @Basic
//    var processMonitoringRecordsFile: String? = null
    @Column(name = "STREET_ADDRESS")
    @Basic
    var streetAddress: String? = null

    @Column(name = "FOREIGN_APPLICATION")
    @Basic
    var foreignApplication: Int? = null

    @Column(name = "BUILDING")
    @Basic
    var building: String? = null

    @Column(name = "JUSTIFICATION_APPROVAL_STATUS")
    @Basic
    var justificationApprovalStatus: Int? = null

    @Column(name = "ALLOCATED_TO_ASSESSOR")
    @Basic
    var allocatedToAssessor: Long? = null


    @Column(name = "CITY")
    @Basic
    var city: String? = null

    @Column(name = "STATE")
    @Basic
    var state: String? = null

    @Column(name = "ZIP_CODE")
    @Basic
    var zipCode: String? = null

    @Column(name = "COUNTRY")
    @Basic
    var country: String? = null

    @Column(name = "JUSTIFICATION_SCHEDULE_DATE")
    @Basic
    var justificationScheduleDate: String? = null

    @Column(name = "MANUFACTURER_COMPANY_NAME")
    @Basic
    var manufacturerCompanyName: String? = null

    @Column(name = "COMPANY_REPRESENTATIVE_EMAIL")
    @Basic
    var companyRepresentativeEmail: String? = null

    @Column(name = "COMPANY_REPRESENTATIVE")
    @Basic
    var companyRepresentative: String? = null

    @Column(name = "EMAIL")
    @Basic
    var email: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as PermitApplicationEntity
        return id == that.id && status == that.status &&
                siteApplicable == that.siteApplicable &&
                streetAddress ==  that.streetAddress &&
                building == that.building &&
                labReportsFilepath3 == that.labReportsFilepath3 &&
                labComplianceStatus == that.labComplianceStatus &&
                bsNumber == that.bsNumber &&
                justificationReportStatus == that.justificationReportStatus &&
                city == that.city &&
                justificationScheduleDate == that.justificationScheduleDate &&
                companyRepresentative == that.companyRepresentative &&
                manufacturerCompanyName == that.manufacturerCompanyName &&
                state == that.state &&
                dmarkConformityStatusApproval == that.dmarkConformityStatusApproval &&
                allocatedToAssessor == that.allocatedToAssessor &&
                foreignApplication == that.foreignApplication &&
                country == that.country &&
                zipCode == that.zipCode &&
                email == that.companyRepresentativeEmail &&
                companyRepresentativeEmail == that.companyRepresentativeEmail &&
                permitApprovalStatusPsc == that.permitApprovalStatusPsc &&
                permitAwardStatusPscPcm == that.permitAwardStatusPscPcm &&
                manufacturerEntity == that.manufacturerEntity &&
                factoryInspectionStatus == that.factoryInspectionStatus &&
                sampleSubmittedId == that.sampleSubmittedId &&
                deferralFeedback == that.deferralFeedback &&
//                testingRecordFile == that.testingRecordFile &&
//                processMonitoringRecordsFile == that.processMonitoringRecordsFile &&
//                certificateFile == that.certificateFile &&
                schemeOfSupervisionAcceptanceStatus == that.schemeOfSupervisionAcceptanceStatus &&
//                userId == that.userId &&
                schemeOfSupervisionStatus == that.schemeOfSupervisionStatus &&
                applicationCompletenessStatus == that.applicationCompletenessStatus &&
                factoryInspectionScheduleStatus == that.factoryInspectionScheduleStatus &&
                schemeOfSupervision == that.schemeOfSupervision &&
                labReportsFilepath2 == that.labReportsFilepath2 &&
                labReportsFilepath1 == that.labReportsFilepath1 &&
                noOfSitesProducingTheBrand == that.noOfSitesProducingTheBrand &&
                kebsProductCategory == that.kebsProductCategory &&
                labReportsFilepath == that.labReportsFilepath &&
                processStatus == that.processStatus &&
                manufacturer == that.manufacturer &&
                productSubCategory == that.productSubCategory &&
                permitType == that.permitType &&
                justificationApprovalStatus == that.justificationApprovalStatus &&
                labReportStatus == that.labReportStatus &&
                product == that.product &&
                hofPermitRecommendation == that.hofPermitRecommendation &&
                standardId == that.standardId &&
                sampleCollectionStatus == that.sampleCollectionStatus &&
                permitNumber == that.permitNumber &&
                description == that.description &&
                tradeMark == that.tradeMark &&
                ksNumber == that.ksNumber &&
                standardTitle == that.standardTitle &&
                dateCreated == that.dateCreated &&
                permitSurveillanceCompletenessStatus == that.permitSurveillanceCompletenessStatus &&
                manufacturerId == that.manufacturerId &&
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
                deletedOn == that.deletedOn &&
                totalAmountToPay == that.totalAmountToPay &&
                modifiedStatus == that.modifiedStatus &&
                renewedDate == that.renewedDate &&
                expiryDate == that.expiryDate &&
                validityPeriod == that.validityPeriod &&
                paymentStatus == that.paymentStatus &&
                renewedStatus == that.renewedStatus &&
                saveReason == that.saveReason &&
                productCategory == that.productCategory &&
                broadProductCategory == that.broadProductCategory &&
                qao == that.qao &&
                dateAwarded == that.dateAwarded
    }

    override fun hashCode(): Int {
        return Objects.hash(id, permitNumber, factoryInspectionScheduleStatus, schemeOfSupervisionStatus, schemeOfSupervision,
            labReportsFilepath2, labReportsFilepath3, labReportsFilepath1, noOfSitesProducingTheBrand, processStatus,
            kebsProductCategory, labReportsFilepath, permitType, manufacturer, description,
            productSubCategory,
            siteApplicable,
            labComplianceStatus,
            streetAddress,
            justificationScheduleDate,
            city,
            state,
            deferralFeedback,
            country,
            companyRepresentativeEmail,
            sampleSubmittedId,
            email,
            zipCode,
            building,
            foreignApplication,
            manufacturerEntity,
            sampleCollectionStatus,
            companyRepresentative,
            bsNumber,
            justificationReportStatus,
            justificationApprovalStatus,
            applicationCompletenessStatus,
            manufacturerCompanyName,
            permitApprovalStatusPsc,
            permitAwardStatusPscPcm,
            tradeMark,
            allocatedToAssessor,
            dmarkConformityStatusApproval,
            factoryInspectionStatus,
            permitSurveillanceCompletenessStatus,
            product,
            hofPermitRecommendation,
            ksNumber, standardTitle, dateCreated, status, manufacturerId, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn, totalAmountToPay, modifiedStatus, renewedDate, expiryDate, validityPeriod, paymentStatus, renewedStatus, saveReason, productCategory,
            broadProductCategory,
            qao, dateAwarded, standardId)
    }
}