/*
 *
 * $$$$$$$\   $$$$$$\  $$\   $$\        $$$$$$\  $$\       $$$$$$\  $$$$$$$\   $$$$$$\  $$\
 * $$  __$$\ $$  __$$\ $$ | $$  |      $$  __$$\ $$ |     $$  __$$\ $$  __$$\ $$  __$$\ $$ |
 * $$ |  $$ |$$ /  \__|$$ |$$  /       $$ /  \__|$$ |     $$ /  $$ |$$ |  $$ |$$ /  $$ |$$ |
 * $$$$$$$\ |\$$$$$\  $$$$$  /        $$ |$$$$\ $$ |     $$ |  $$ |$$$$$$$\ |$$$$$$$$ |$$ |
 * $$  __$$\  \____$$\ $$  $$<         $$ |\_$$ |$$ |     $$ |  $$ |$$  __$$\ $$  __$$ |$$ |
 * $$ |  $$ |$$\   $$ |$$ |\$\        $$ |  $$ |$$ |     $$ |  $$ |$$ |  $$ |$$ |  $$ |$$ |
 * $$$$$$$  |\$$$$$  |$$ | \$\       \$$$$$  |$$$$$$$$\ $$$$$$  |$$$$$$$  |$$ |  $$ |$$$$$$$$\
 * \_______/  \______/ \__|  \__|       \______/ \________|\______/ \_______/ \__|  \__|\________|
 * $$$$$$$$\ $$$$$$$$\  $$$$$$\  $$\   $$\ $$\   $$\  $$$$$$\  $$\       $$$$$$\   $$$$$$\  $$$$$$\ $$$$$$$$\  $$$$$$\
 * \__$$  __|$$  _____|$$  __$$\ $$ |  $$ |$$$\  $$ |$$  __$$\ $$ |     $$  __$$\ $$  __$$\ \_$$  _|$$  _____|$$  __$$\
 *    $$ |   $$ |      $$ /  \__|$$ |  $$ |$$$$\ $$ |$$ /  $$ |$$ |     $$ /  $$ |$$ /  \__|  $$ |  $$ |      $$ /  \__|
 *    $$ |   $$$$$\    $$ |      $$$$$$$$ |$$ $$\$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |$$$$\   $$ |  $$$$$\    \$$$$$\
 *    $$ |   $$  __|   $$ |      $$  __$$ |$$ \$$$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |\_$$ |  $$ |  $$  __|    \____$$\
 *    $$ |   $$ |      $$ |  $$\ $$ |  $$ |$$ |\$$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |  $$ |  $$ |  $$ |      $$\   $$ |
 *    $$ |   $$$$$$$$\ \$$$$$  |$$ |  $$ |$$ | \$ | $$$$$$  |$$$$$$$$\ $$$$$$  |\$$$$$  |$$$$$$\ $$$$$$$$\ \$$$$$  |
 *    \__|   \________| \______/ \__|  \__|\__|  \__| \______/ \________|\______/  \______/ \______|\________| \______/
 *
 *
 *
 *
 *
 *   Copyright (c) 2020.  BSK
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.kebs.app.kotlin.apollo.store.model.ms

import org.hibernate.annotations.DynamicUpdate
import org.kebs.app.kotlin.apollo.store.model.DepartmentsEntity
import org.kebs.app.kotlin.apollo.store.model.DivisionsEntity
import org.kebs.app.kotlin.apollo.store.model.ServiceMapsEntity
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import java.io.Serializable
import java.sql.Date
import java.sql.Timestamp
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@DynamicUpdate
@Table(name = "DAT_KEBS_MS_COMPLAINT")
class ComplaintEntity : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_MS_COMPLAINT_SEQ_GEN", allocationSize = 1, sequenceName = "DAT_KEBS_MS_COMPLAINT_SEQ")
    @GeneratedValue(generator = "DAT_KEBS_MS_COMPLAINT_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = 0

    @NotNull(message = "This field is required")
    @Column(name = "COMPLAINT_DETAILS")
    @Basic
    var complaintDetails: String? = null

    @Column(name = "COMPLAINT_SAMPLE_DETAILS")
    @Basic
    var complaintSampleDetails: String? = null

    @Column(name = "REMEDY_SOUGHT")
    @Basic
    var remedySought: String? = null

    @NotNull(message = "This field is required")
    @Column(name = "COMPLAINT_TITLE")
    @Basic
    var complaintTitle: String? = null

    @Column(name = "TIMELINE_START_DATE")
    @Basic
    var timelineStartDate: Date? = null

    @Column(name = "TIMELINE_END_DATE")
    @Basic
    var timelineEndDate: Date? = null

//    @Column(name = "TIMELINE_OVER_DUE")
//    @Basic
//    var timelineOverDue: Int? = null


//    @Transient
//    var confirmDivisionId: Long? = 0
//
//    @Transient
//    var assignedUser: Long? = 0

    @Transient
    var processValue: String? = null

//    @Transient
//    var confirmDepartmentId: Long? = 0

    @Column(name = "USER_TASK_ID")
    @Basic
    var userTaskId: Long? = null

    @Column(name = "UUID")
    @Basic
    var uuid: String? = null

    @Column(name = "REFERENCE_NUMBER")
    @Basic
    var referenceNumber: String? = null

    @Column(name = "ADVISED_WHERETO_BY")
    @Basic
    var advisedWheretoBy: String? = null

    @Column(name = "REVISION")
    @Basic
    var revision: Long? = null

    @Column(name = "MS_TYPE_ID")
    @Basic
    var msTypeId: Long? = null

//    @Column(name = "HOD_ASSIGNED")
//    @Basic
//    var hodAssigned: Long? = null

    @Column(name = "PRODUCT_CATEGORY")
    @Basic
    var productCategory: Long? = null

    @Column(name = "BROAD_PRODUCT_CATEGORY")
    @Basic
    var broadProductCategory: Long? = null

    @Column(name = "PRODUCT")
    @Basic
    var product: Long? = null

    @Column(name = "PRODUCT_CATEGORY_STRING")
    @Basic
    var productCategoryString: String? = null

    @Column(name = "BROAD_PRODUCT_CATEGORY_STRING")
    @Basic
    var broadProductCategoryString: String? = null

    @Column(name = "PRODUCT_STRING")
    @Basic
    var productString: String? = null

    @Column(name = "CLASSIFICATION_DETAILS_STATUS")
    @Basic
    var classificationDetailsStatus: Int? = null

    @Column(name = "STANDARD_CATEGORY")
    @Basic
    var standardCategory: Long? = null

    @Column(name = "PRODUCT_SUB_CATEGORY")
    @Basic
    var productSubCategory: Long? = null

    @Column(name = "STANDARD_CATEGORY_STRING")
    @Basic
    var standardCategoryString: String? = null

    @Column(name = "PRODUCT_SUB_CATEGORY_STRING")
    @Basic
    var productSubCategoryString: String? = null

    @Column(name = "STANDARD_TITLE")
    @Basic
    var standardTitle: String? = null

    @Column(name = "STANDARD_NUMBER")
    @Basic
    var standardNumber: String? = null

    @Column(name = "TRANSACTION_DATE")
    @Basic
    var transactionDate: Date? = null

    @Column(name = "ADVISED_WHERETO_ON")
    @Basic
    var advisedWheretoOn: Date? = null

    @Column(name = "MANDATE_FOR_OGA")
    @Basic
    var mandateForOga: Int? = null

    @Column(name = "AMENDMENT_STATUS")
    @Basic
    var amendmentStatus: Int? = null

    @Column(name = "AMENDMENT_REMARKS")
    @Basic
    var amendmentRemarks: String? = null

    @Column(name = "MS_COMPLAINT_ENDED_STATUS")
    @Basic
    var msComplaintEndedStatus: Int? = null

    @Column(name = "STATUS")
    @Basic
    var status: Int? = null

    @Column(name = "MS_PROCESS_STATUS")
    @Basic
    var msProcessStatus: Int? = null

    @Column(name = "PROGRESS_VALUE")
    @Basic
    var progressValue: Int? = null

    @Column(name = "DEPARTMENT")
    @Basic
    var department: String? = null

    @Column(name = "DIVISION")
    @Basic
    var division: Long? = null

    @Column(name = "MS_PROCESS_ID")
    @Basic
    var msProcessId: Long? = null

    @Column(name = "PROGRESS_STEP")
    @Basic
    var progressStep: String? = null

    @Column(name = "TARGETED_PRODUCTS")
    @Basic
    var targetedProducts: String? = null

    @Column(name = "REJECTED_BY")
    @Basic
    var rejectedBy: String? = null

    @Column(name = "ASSIGNED_BY")
    @Basic
    var assignedBy: String? = null

    @Column(name = "MS_PROCESS_STARTED_BY")
    @Basic
    var msProcessStartedBy: String? = null

    @Column(name = "MS_PROCESS_ENDED_BY")
    @Basic
    var msProcessEndedBy: String? = null

    @Column(name = "REJECTED_REMARKS")
    @Basic
    var rejectedRemarks: String? = null

    @Column(name = "ADVISED_WHERETO")
    @Basic
    var advisedWhereto: String? = null

    @Column(name = "ASSIGNED_REMARKS")
    @Basic
    var assignedRemarks: String? = null

    @Column(name = "APPROVED_REMARKS")
    @Basic
    var approvedRemarks: String? = null

    @Column(name = "APPROVED_BY")
    @Basic
    var approvedBy: String? = null

    @Column(name = "APPROVED")
    @Basic
    var approved: Int? = null

    @Column(name = "APPROVED_DATE")
    @Basic
    var approvedDate: Date? = null

    @Column(name = "MS_PROCESS_STARTED_ON")
    @Basic
    var msProcessStartedOn: Date? = null

    @Column(name = "MS_PROCESS_ENDED_ON")
    @Basic
    var msProcessEndedOn: Date? = null

    @Column(name = "ASSIGNED_ON")
    @Basic
    var assignedDate: Date? = null

    @Column(name = "REJECTED")
    @Basic
    var rejected: Int? = null

    @Column(name = "REJECTED_DATE")
    @Basic
    var rejectedDate: Date? = null

    @Column(name = "SUBMISSION_DATE")
    @Basic
    var submissionDate: Timestamp? = null

    @Column(name = "REVISION_DATE")
    @Basic
    var revisionDate: Timestamp? = null

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

//    @Column(name = "MS_COMPLAINT_STATUS")
//    @Basic
//    var msComplaintStatus: Int? = null

//    //Added for bpmn
//    @Column(name = "MS_COMPLAINT_STARTED_ON")
//    @Basic
//    var msComplaintStartedOn: Timestamp? = null
//
//    @Column(name = "MS_COMPLAINT_COMPLETED_ON")
//    @Basic
//    var msComplaintCompletedOn: Timestamp? = null
//
//    @Column(name = "MS_COMPLAINT_PROCESS_INSTANCE_ID")
//    @Basic
//    var msComplaintProcessInstanceId: String? = null

//    //Added for bpmn
//    @Column(name = "MS_FUEL_MONITORING_STATUS")
//    @Basic
//    var msFuelMonitoringStatus: Int? = null
//
//    @Column(name = "MS_FUEL_MONITORING_STARTED_ON")
//    @Basic
//    var msFuelMonitoringStartedOn: Timestamp? = null
//
//    @Column(name = "MS_FUEL_MONITORING_COMPLETED_ON")
//    @Basic
//    var msFuelMonitoringCompletedOn: Timestamp? = null
//
//    @Column(name = "MS_FUEL_MONITORING_PROCESS_INSTANCE_ID")
//    @Basic
//    var msFuelMonitoringProcessInstanceId: String? = null

//    //Added for bpmn
//    @Column(name = "MS_MARKET_SURVEILLANCE_STATUS")
//    @Basic
//    var msMarketSurveillanceStatus: Int? = null
//
//    @Column(name = "MS_MARKET_SURVEILLANCE_STARTED_ON")
//    @Basic
//    var msMarketSurveillanceStartedOn: Timestamp? = null
//
//    @Column(name = "MS_MARKET_SURVEILLANCE_COMPLETED_ON")
//    @Basic
//    var msMarketSurveillanceCompletedOn: Timestamp? = null
//
//    @Column(name = "MS_MARKET_SURVEILLANCE_PROCESS_INSTANCE_ID")
//    @Basic
//    var msMarketSurveillanceProcessInstanceId: String? = null


//    @JoinColumn(name = "SERVICE_MAPS_ID", referencedColumnName = "ID")
//    @ManyToOne
//    var serviceMapsId: ServiceMapsEntity? = null

//    @JoinColumn(name = "ASSIGNED_IO", referencedColumnName = "ID")
//    @ManyToOne
//    var assignedIo: UsersEntity? = null

    @Column(name = "SERVICE_MAPS_ID")
    @Basic
    var serviceMapsId: Int? = null

    @Column(name = "ASSIGNED_IO")
    @Basic
    var assignedIo: Long? = null

    @Column(name = "ASSIGNED_IO_STATUS")
    @Basic
    var assignedIoStatus: Int? = null

    @Column(name = "COMPLAINT_DEPARTMENT")
    @Basic
    var complaintDepartment: Long? = null


    @Column(name = "HOF_ASSIGNED")
    @Basic
    var hofAssigned: Long? = null

    @Column(name = "HOD_ASSIGNED")
    @Basic
    var hodAssigned: Long? = null

//    @JoinColumn(name = "COMPLAINT_DEPARTMENT", referencedColumnName = "ID")
//    @ManyToOne
//    var complaintDepartment: DepartmentsEntity? = null

//    @JoinColumn(name = "HOF_ASSIGNED", referencedColumnName = "ID")
//    @ManyToOne
//    var hofAssigned: UsersEntity? = null

//    @JoinColumn(name = "HOD_ASSIGNED", referencedColumnName = "ID")
//    @ManyToOne
//    var hodAssigned: UsersEntity? = null

    @Column(name = "MS_ASSIGNED_IO_DATE")
    @Basic
    var msAssignedIoDate: Date? = null

    @Column(name = "MS_HOF_ASSIGNED_DATE")
    @Basic
    var msHofAssignedDate: Date? = null

    @Column(name = "MS_COMPLAINT_STATUS")
    @Basic
    var msComplaintStatus: Int? = null

    @Column(name = "MS_COMPLAINT_STARTED_ON")
    @Basic
    var msComplaintStartedOn: Timestamp? = null

    @Column(name = "MS_COMPLAINT_COMPLETED_ON")
    @Basic
    var msComplaintCompletedOn: Timestamp? = null

    @Column(name = "MS_COMPLAINT_PROCESS_INSTANCE_ID")
    @Basic
    var msComplaintProcessInstanceId: String? = null

    @Column(name = "MS_FUEL_MONITORING_STATUS")
    @Basic
    var msFuelMonitoringStatus: Int? = null

    @Column(name = "MS_FUEL_MONITORING_STARTED_ON")
    @Basic
    var msFuelMonitoringStartedOn: Timestamp? = null

    @Column(name = "MS_FUEL_MONITORING_COMPLETED_ON")
    @Basic
    var msFuelMonitoringCompletedOn: Timestamp? = null

    @Column(name = "MS_FUEL_MONITORING_PROCESS_INSTANCE_ID")
    @Basic
    var msFuelMonitoringProcessInstanceId: String? = null

    @Column(name = "MS_MARKET_SURVEILLANCE_STATUS")
    @Basic
    var msMarketSurveillanceStatus: Int? = null

    @Column(name = "MS_MARKET_SURVEILLANCE_STARTED_ON")
    @Basic
    var msMarketSurveillanceStartedOn: Timestamp? = null

    @Column(name = "MS_MARKET_SURVEILLANCE_COMPLETED_ON")
    @Basic
    var msMarketSurveillanceCompletedOn: Timestamp? = null

    @Column(name = "MS_MARKET_SURVEILLANCE_PROCESS_INSTANCE_ID")
    @Basic
    var msMarketSurveillanceProcessInstanceId: String? = null


}
