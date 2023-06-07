package org.kebs.app.kotlin.apollo.store.model.importer

import org.kebs.app.kotlin.apollo.store.model.SectionsEntity
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import java.io.Serializable
import java.sql.Date
import java.sql.Time
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_TEMPORARY_IMPORT_APPLICATIONS")
class TemporaryImportApplicationsEntity: Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_TEMPORARY_IMPORT_APPLICATIONS_SEQ_GEN", sequenceName = "DAT_KEBS_TEMPORARY_IMPORT_APPLICATIONS_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_TEMPORARY_IMPORT_APPLICATIONS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long = 0

    @Basic
    @Column(name = "UUID")
    var uuid: String? = null

    @Transient
    var confirmAssignedUserId: Long? = null

    @Transient
    var confirmEntryPoint: Long? = null

    @Column(name = "ENTRY_NUMBER")
    @Basic
    var entryNumber: String? = null

    @Column(name = "REF_NUMBER")
    @Basic
    var refNumber: String? = null

    @Column(name = "CD_ID")
    @Basic
    var cdId: Long? = null


    @Column(name = "BILL_OF_LANDING_AIRWAY_BILL")
    @Basic
    var billOfLandingAirwayBill: String? = null

    @Column(name = "PRODUCT_DESCRIPTION")
    @Basic
    var productDescription: String? = null

    @Column(name = "DATE_SHIPPED_ON_BOARD")
    @Basic
    var dateShippedOnBoard: Time? = null

    @Column(name = "IDF_NO")
    @Basic
    var idfNo: String? = null

    @Column(name = "UCR_NO")
    @Basic
    var ucrNo: String? = null

    @Column(name = "KRA_BOND")
    @Basic
    var kraBond: String? = null

    @Column(name = "ASSIGNED_USER_DATE")
    @Basic
    var assignedUserDate: Date? = null

    @Column(name = "ASSIGNED_USER_REMARKS")
    @Basic
    var assignedUserRemarks: String? = null

    @Column(name = "ASSIGNED_USER_STATUS")
    @Basic
    var assignedUserStatus: Int? = null

    @Column(name = "ISSUES_DATE")
    @Basic
    var issuesDate: Date? = null

    @Column(name = "ISSUES_REMARKS")
    @Basic
    var issuesRemarks: String? = null

    @Column(name = "ISSUES_STATUS")
    @Basic
    var issuesStatus: Int? = null

    @Column(name = "REJECTED_STATUS")
    @Basic
    var rejectedStatus: Int? = null

    @Column(name = "REJECTED_DATE")
    @Basic
    var rejectedDate: Date? = null

    @Column(name = "REJECTED_REMARKS")
    @Basic
    var rejectedRemarks: String? = null

    @Column(name = "SUPERVISOR_REJECTED_STATUS")
    @Basic
    var supervisorRejectedStatus: Int? = null

    @Column(name = "SUPERVISOR_REJECTED_DATE")
    @Basic
    var supervisorRejectedDate: Date? = null

    @Column(name = "SUPERVISOR_REJECTED_REMARKS")
    @Basic
    var supervisorRejectedRemarks: String? = null

    @Column(name = "SUPERVISOR_APPROVED_STATUS")
    @Basic
    var supervisorApprovedStatus: Int? = null

    @Column(name = "SUPERVISOR_APPROVED_DATE")
    @Basic
    var supervisorApprovedDate: Date? = null

    @Column(name = "SUPERVISOR_APPROVED_REMARKS")
    @Basic
    var supervisorApprovedRemarks: String? = null

    @Column(name = "APPROVAL_DATE")
    @Basic
    var approvalDate: Date? = null

    @Column(name = "APPROVAL_REMARKS")
    @Basic
    var approvalRemarks: String? = null

    @Column(name = "APPROVAL_STATUS")
    @Basic
    var approvalStatus: Int? = null

    @Column(name = "SENT_MD_DATE")
    @Basic
    var sentMdDate: Date? = null

    @Column(name = "SENT_MD_REMARKS")
    @Basic
    var sentMdRemarks: String? = null

    @Column(name = "SENT_MD_STATUS")
    @Basic
    var sentMdStatus: Int? = null

    @Column(name = "TRANSACTION_DATE")
    @Basic
    var transactionDate: Date? = null

    @Column(name = "DESCRIPTION")
    @Basic
    var description: String? = null

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

    @JoinColumn(name = "IMPORTER_ID", referencedColumnName = "ID")
    @ManyToOne
    var importerId: UsersEntity? = null

    @JoinColumn(name = "ASSIGNED_USER", referencedColumnName = "ID")
    @ManyToOne
    var assignedUser: UsersEntity? = null

    @JoinColumn(name = "APPLICATION_TYPE", referencedColumnName = "ID")
    @ManyToOne
    var applicationType: ImporterDiApplicationsTypesEntity? = null

    @JoinColumn(name = "ENTRY_POINT_ID", referencedColumnName = "ID")
    @ManyToOne
    var entryPointId: SectionsEntity? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as TemporaryImportApplicationsEntity
        return id == that.id &&
                uuid == that.uuid &&
                confirmAssignedUserId == that.confirmAssignedUserId &&
                refNumber == that.refNumber &&
                cdId == that.cdId &&
                entryNumber == that.entryNumber &&
                confirmEntryPoint == that.confirmEntryPoint &&
                billOfLandingAirwayBill == that.billOfLandingAirwayBill &&
                productDescription == that.productDescription &&
                dateShippedOnBoard == that.dateShippedOnBoard &&
                idfNo == that.idfNo &&
                ucrNo == that.ucrNo &&
                kraBond == that.kraBond &&
                sentMdDate == that.sentMdDate &&
                sentMdRemarks == that.sentMdRemarks &&
                sentMdStatus == that.sentMdStatus &&
                approvalDate == that.approvalDate &&
                approvalRemarks == that.approvalRemarks &&
                approvalStatus == that.approvalStatus &&
                rejectedDate == that.rejectedDate &&
                rejectedRemarks == that.rejectedRemarks &&
                rejectedStatus == that.rejectedStatus &&
                supervisorRejectedDate == that.supervisorRejectedDate &&
                supervisorRejectedRemarks == that.supervisorRejectedRemarks &&
                supervisorRejectedStatus == that.supervisorRejectedStatus &&
                supervisorApprovedDate == that.supervisorApprovedDate &&
                supervisorApprovedRemarks == that.supervisorApprovedRemarks &&
                supervisorApprovedStatus == that.supervisorApprovedStatus &&
                issuesStatus == that.issuesStatus &&
                issuesDate == that.issuesDate &&
                issuesRemarks == that.issuesRemarks &&
                assignedUserDate == that.assignedUserDate &&
                assignedUserRemarks == that.assignedUserRemarks &&
                assignedUserStatus == that.assignedUserStatus &&
                transactionDate == that.transactionDate &&
                description == that.description &&
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
        return Objects.hash(id, uuid, confirmAssignedUserId, cdId, refNumber, assignedUserRemarks, assignedUserDate, assignedUserStatus, rejectedDate, rejectedRemarks, rejectedStatus,supervisorRejectedDate, supervisorRejectedRemarks, supervisorRejectedStatus,supervisorApprovedDate, supervisorApprovedRemarks, supervisorApprovedStatus, approvalDate, approvalRemarks, approvalStatus, issuesRemarks, issuesDate, issuesStatus, sentMdDate, sentMdRemarks, sentMdStatus, entryNumber, confirmEntryPoint, billOfLandingAirwayBill, productDescription, dateShippedOnBoard, idfNo, ucrNo, kraBond, transactionDate, description, status, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn)
    }
}