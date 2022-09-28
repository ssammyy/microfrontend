package org.kebs.app.kotlin.apollo.store.model.ms

import java.io.Serializable
import java.sql.Date
import java.sql.Time
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_MS_FUEL_INSPECTION")
class MsFuelInspectionEntity : Serializable{
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_MS_FUEL_INSPECTION_SEQ_GEN", allocationSize = 1, sequenceName = "DAT_KEBS_MS_FUEL_INSPECTION_SEQ")
    @GeneratedValue(generator = "DAT_KEBS_MS_FUEL_INSPECTION_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long = 0

    @Transient
    var processStage: String? = null

    @Column(name = "REFERENCE_NUMBER")
    @Basic
    var referenceNumber: String? = null

    @Column(name = "BATCH_ID")
    @Basic
    var batchId: Long? = null

    @Column(name = "MS_TEAMS_ID")
    @Basic
    var msTeamsId: Long? = null

    @Column(name = "MS_COUNTY_ID")
    @Basic
    var msCountyId: Long? = null

    @Column(name = "TIMELINE_START_DATE")
    @Basic
    var timelineStartDate: Date? = null

    @Column(name = "TIMELINE_END_DATE")
    @Basic
    var timelineEndDate: Date? = null

    @Column(name = "USER_TASK_ID")
    @Basic
    var userTaskId: Long? = null

    @Column(name = "MS_PROCESS_ID")
    @Basic
    var msProcessId: Long? = null

    @Column(name = "FUEL_REPORT_ID")
    @Basic
    var fuelReportId: Long? = null

    @Column(name = "RAPID_TEST_FAILED_REMARKS")
    @Basic
    var rapidTestFailedRemarks: String? = null

    @Column(name = "RAPID_TESTED_PASSED_REMARKS")
    @Basic
    var rapidTestPassedRemarks: String? = null

    @Column(name = "RAPID_TEST_FAILED_BY")
    @Basic
    var rapidTestFailedBy: String? = null

    @Column(name = "RAPID_TESTED_PASSED_BY")
    @Basic
    var rapidTestPassedBy: String? = null

    @Column(name = "STATION_OWNER_EMAIL")
    @Basic
    var stationOwnerEmail: String? = null

    @Column(name = "COMPANY_KRA_PIN")
    @Basic
    var companyKraPin: String? = null

    @Column(name = "TOWN_ID")
    @Basic
    var townId: Long? = 0

    @Column(name = "NOT_COMPLIANT_STATUS_BY")
    @Basic
    var notCompliantStatusBy: String? = null

    @Column(name = "NOT_COMPLIANT_STATUS_DATE")
    @Basic
    var notCompliantStatusDate: Date? = null

    @Column(name = "COMPLIANT_STATUS_BY")
    @Basic
    var compliantStatusBy: String? = null

    @Column(name = "COMPLIANT_STATUS_REMARKS")
    @Basic
    var compliantStatusRemarks: String? = null

    @Column(name = "NOT_COMPLIANT_STATUS_REMARKS")
    @Basic
    var notCompliantStatusRemarks: String? = null

    @Column(name = "REMEDIATION_COMPLETE_REMARKS")
    @Basic
    var remediationCompleteRemarks: String? = null

    @Column(name = "COMPLIANT_STATUS_DATE")
    @Basic
    var compliantStatusDate: Date? = null

    @Column(name = "RAPID_TEST_FAILED")
    @Basic
    var rapidTestFailed: Int? = null

    @Column(name = "REMEDIATION_STATUS")
    @Basic
    var remediationStatus: Int? = null

    @Column(name = "REMEDIATION_PAYMENT_STATUS")
    @Basic
    var remediationPaymentStatus: Int? = null

    @Column(name = "OWNER_ALL_LAB_RESULTS_STATUS")
    @Basic
    var ownerAllLabResultsStatus: Int? = null

    @Column(name = "COMPLIANT_STATUS")
    @Basic
    var compliantStatus: Int? = null

    @Column(name = "NOT_COMPLIANT_STATUS")
    @Basic
    var notCompliantStatus: Int? = null

    @Column(name = "RAPID_TEST_PASSED")
    @Basic
    var rapidTestPassed: Int? = null

    @Column(name = "REMEDIATION_COMPLETE_STATUS")
    @Basic
    var remendiationCompleteStatus: Int? = null

    @Column(name = "INSPECTION_COMPLETE_STATUS")
    @Basic
    var inspectionCompleteStatus: Int? = null

    @Column(name = "ASSIGNED_OFFICER_STATUS")
    @Basic
    var assignedOfficerStatus: Int? = null

    @Column(name = "RAPID_TEST_FAILED_ON")
    @Basic
    var rapidTestFailedOn: Date? = null

    @Column(name = "RAPID_TESTED_PASSED_ON")
    @Basic
    var rapidTestPassedOn: Date? = null

    @Column(name = "SCF_LABPARAMS_STATUS")
    @Basic
    var scfLabparamsStatus: Int? = null

    @Column(name = "BS_NUMBER_STATUS")
    @Basic
    var bsNumberStatus: Int? = null

    @Column(name = "SSF_LABPARAMS_STATUS")
    @Basic
    var ssfLabparamsStatus: Int? = null

    @Column(name = "SAMPLE_COLLECTION_STATUS")
    @Basic
    var sampleCollectionStatus: Int? = null

    @Column(name = "SAMPLE_SUBMITTED_STATUS")
    @Basic
    var sampleSubmittedStatus: Int? = null

    @Column(name = "SEND_SSF_DATE")
    @Basic
    var sendSffDate: Date? = null

    @Column(name = "SEND_SSF_STATUS")
    @Basic
    var sendSffStatus: Int? = null

    @Column(name = "INSPECTION_DATE")
    @Basic
    var inspectionDate: Date? = null

    @Column(name = "INSPECTION_DATE_FROM")
    @Basic
    var inspectionDateFrom: Date? = null

    @Column(name = "INSPECTION_DATE_TO")
    @Basic
    var inspectionDateTo: Date? = null

    @Column(name = "COMPANY")
    @Basic
    var company: String? = null

    @Column(name = "PETROLEUM_PRODUCT")
    @Basic
    var petroleumProduct: String? = null

    @Column(name = "PHYSICAL_LOCATION")
    @Basic
    var physicalLocation: String? = null

    @Column(name = "INSPECTION_REQUEST_SOURCE")
    @Basic
    var inspectionRequestSource: String? = null

    @Column(name = "TRANSACTION_DATE")
    @Basic
    var transactionDate: Date? = null

    @Column(name = "STATUS")
    @Basic
    var status: Int? = null

    @Column(name = "REMARKS")
    @Basic
    var remarks: String? = null

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

    @Column(name = "LAST_MODIFIED_BY")
    @Basic
    var lastModifiedBy: String? = null

    @Column(name = "LAST_MODIFIED_ON")
    @Basic
    var lastModifiedOn: Timestamp? = null

    @Column(name = "UPDATE_BY")
    @Basic
    var updateBy: String? = null

    @Column(name = "UPDATED_ON")
    @Basic
    var updatedOn: Timestamp? = null

    @Column(name = "DELETE_BY")
    @Basic
    var deleteBy: String? = null

    @Column(name = "DELETED_ON")
    @Basic
    var deletedOn: Timestamp? = null

    @Column(name = "VERSION")
    @Basic
    var version: Long? = null

    @Column(name = "MS_FUEL_STATUS")
    @Basic
    var msFuelStatus: Int? = null

    @Column(name = "MS_FUEL_STARTED_ON")
    @Basic
    var msFuelStartedOn: Timestamp? = null

    @Column(name = "MS_FUEL_COMPLETED_ON")
    @Basic
    var msFuelCompletedOn: Timestamp? = null

    @Column(name = "MS_FUEL_PROCESS_INSTANCE_ID")
    @Basic
    var msFuelProcessInstanceId: String? = null

}
