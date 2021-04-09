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
    @Size(min = 5, max = 3000)
    @Column(name = "COMPLAINT_DETAILS")
    @Basic
    var complaintDetails: String? = null

    @NotNull(message = "This field is required")
    @Size(min = 5, max = 3000)
    @Column(name = "COMPLAINT_TITLE")
    @Basic
    var complaintTitle: String? = null

//    @Transient
//    var confirmDivisionId: Long? = 0
//
//    @Transient
//    var assignedUser: Long? = 0

    @Transient
    var processValue: String? = null

//    @Transient
//    var confirmDepartmentId: Long? = 0

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

    @Column(name = "STANDARD_CATEGORY")
    @Basic
    var standardCategory: Long? = null

    @Column(name = "PRODUCT_SUB_CATEGORY")
    @Basic
    var productSubCategory: Long? = null

    @Column(name = "TRANSACTION_DATE")
    @Basic
    var transactionDate: Date? = null

    @Column(name = "ADVISED_WHERETO_ON")
    @Basic
    var advisedWheretoOn: Date? = null

    @Column(name = "MANDATE_FOR_OGA")
    @Basic
    var mandateForOga: Int? = null

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as ComplaintEntity
        return id == that.id &&
                assignedIoStatus == that.assignedIoStatus &&
                referenceNumber == that.referenceNumber &&
                uuid == that.uuid &&
                assignedIo == that.assignedIo &&
                hodAssigned == that.hodAssigned &&
                hofAssigned == that.hofAssigned &&
                complaintDepartment == that.complaintDepartment &&
                msTypeId == that.msTypeId &&
                standardCategory == that.standardCategory &&
                productSubCategory == that.productSubCategory &&
                product == that.product &&
                productCategory == that.productCategory &&
                broadProductCategory == that.broadProductCategory &&
                msMarketSurveillanceProcessInstanceId == that.msMarketSurveillanceProcessInstanceId &&
                msMarketSurveillanceCompletedOn == that.msMarketSurveillanceCompletedOn &&
                msMarketSurveillanceStartedOn == that.msMarketSurveillanceStartedOn &&
                msMarketSurveillanceStatus == that.msMarketSurveillanceStatus &&
                msFuelMonitoringProcessInstanceId == that.msFuelMonitoringProcessInstanceId &&
                msFuelMonitoringCompletedOn == that.msFuelMonitoringCompletedOn &&
                msFuelMonitoringStartedOn == that.msFuelMonitoringStartedOn &&
                msFuelMonitoringStatus == that.msFuelMonitoringStatus &&
                msComplaintProcessInstanceId == that.msComplaintProcessInstanceId &&
                msComplaintCompletedOn == that.msComplaintCompletedOn &&
                msComplaintStartedOn == that.msComplaintStartedOn &&
                msComplaintStatus == that.msComplaintStatus &&
                msAssignedIoDate == that.msAssignedIoDate &&
                msHofAssignedDate == that.msHofAssignedDate &&
                approvedDate == that.approvedDate &&
                rejectedDate == that.rejectedDate &&
                department == that.department &&
                serviceMapsId == that.serviceMapsId &&
                division == that.division &&
                targetedProducts == that.targetedProducts &&
                msProcessStatus == that.msProcessStatus &&
                progressValue == that.progressValue &&
                progressStep == that.progressStep &&
                approvedBy == that.approvedBy &&
                rejectedBy == that.rejectedBy &&
                rejectedRemarks == that.rejectedRemarks &&
                approvedRemarks == that.approvedRemarks &&
                assignedDate == that.assignedDate &&
                assignedRemarks == that.assignedRemarks &&
                assignedBy == that.assignedBy &&
                msProcessStartedBy == that.msProcessStartedBy &&
                msProcessStartedOn == that.msProcessStartedOn &&
                msProcessEndedOn == that.msProcessEndedOn &&
                msProcessEndedBy == that.msProcessEndedBy &&
                advisedWhereto == that.advisedWhereto &&
//                confirmDepartmentId == that.confirmDepartmentId &&
                processValue == that.processValue &&
                advisedWheretoBy == that.advisedWheretoBy &&
                advisedWheretoOn == that.advisedWheretoOn &&
                mandateForOga == that.mandateForOga &&
//                confirmDivisionId == that.confirmDivisionId &&
//                assignedUser == that.assignedUser &&
                approved == that.approved &&
                rejected == that.rejected &&
                complaintDetails == that.complaintDetails &&
                complaintTitle == that.complaintTitle &&
                revision == that.revision &&
                status == that.status &&
                submissionDate == that.submissionDate &&
                revisionDate == that.revisionDate &&
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
        return Objects.hash(
                id,
                uuid,
                referenceNumber,
                assignedIo,
                msTypeId,
                complaintDepartment,
                hodAssigned,
                standardCategory,
                hofAssigned,
                productSubCategory,
                product,
                serviceMapsId,
                productCategory,
                assignedIoStatus,
                broadProductCategory,
//                confirmDepartmentId,
//                confirmDivisionId,
                department,
                progressValue,
                rejectedBy,
                rejectedRemarks,
                assignedBy,
                assignedRemarks,
                approvedRemarks,
                assignedDate,
                approvedBy,
                progressStep,
                advisedWheretoBy,
                advisedWheretoOn,
                mandateForOga,
                msProcessStartedBy,
                msProcessStartedOn,
                msProcessEndedBy,
                advisedWhereto,
                msProcessEndedOn,
                division,
                processValue,
                msProcessStatus,
                targetedProducts,
                msMarketSurveillanceProcessInstanceId,
                msMarketSurveillanceCompletedOn,
                msMarketSurveillanceStartedOn,
                msMarketSurveillanceStatus,
                msFuelMonitoringProcessInstanceId,
                msFuelMonitoringCompletedOn,
                msFuelMonitoringStartedOn,
                msFuelMonitoringStatus,
                msComplaintProcessInstanceId,
                msComplaintCompletedOn,
                msComplaintStartedOn,
                msComplaintStatus,
                msAssignedIoDate,
                msHofAssignedDate,
//                assignedUser,
                complaintDetails,
                complaintTitle,
                approved,
                approvedDate,
                rejectedDate,
                rejected,
                revision,
                status,
                submissionDate,
                revisionDate,
                varField1,
                varField2,
                varField3,
                varField4,
                varField5,
                varField6,
                varField7,
                varField8,
                varField9,
                varField10,
                createdBy,
                createdOn,
                modifiedBy,
                modifiedOn,
                deleteBy,
                deletedOn
        )
    }
}