package org.kebs.app.kotlin.apollo.store.model.pvc

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_PVOC_WAIVERS_APPLICATION")
class PvocWaiversApplicationEntity : Serializable {
    @Column(name = "ID")
    @Id
    @SequenceGenerator(name = "DAT_KEBS_PVOC_WAIVERS_APPLICATION_SEQ_GEN", sequenceName = "DAT_KEBS_PVOC_WAIVERS_APPLICATION_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_PVOC_WAIVERS_APPLICATION_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long = 0

    @Column(name = "COMPANY_ID")
    @Basic
    var companyId: Long? = null

    @Column(name = "STATUS")
    @Basic
    var status: Int? = null

    @Column(name = "APPLICANT_NAME")
    @Basic
    var applicantName: String? = null

    @Column(name = "PHONE_NUMBER")
    @Basic
    var phoneNumber: String? = null

    @Column(name = "EMAIL_ADDRESS")
    @Basic
    var emailAddress: String? = null

    @Column(name = "KRA_PIN")
    @Basic
    var kraPin: String? = null

    @Column(name = "ADDRESS")
    @Basic
    var address: String? = null

    @Column(name = "CATEGORY")
    @Basic
    var category: String? = null

    @Column(name = "JUSTIFICATION")
    @Basic
    var justification: String? = null

    @Column(name = "PRODUCT_DESCRIPTION")
    @Basic
    var productDescription: String? = null

    @Column(name = "DOCUMENTATION")
    @Basic
    var documentation: String? = null

    @Column(name = "REVIEW_STATUS")
    @Basic
    var reviewStatus: String? = null

    @Column(name = "NSC_APPROVAL_STATUS")
    @Basic
    var nscApprovalStatus: String? = null

    @Column(name = "CS_APPROVAL_STATUS")
    @Basic
    var csApprovalStatus: String? = null

    @Column(name = "SERIAL_NO")
    @Basic
    var serialNo: String? = null

    @Column(name = "CS_RESPONCE_CODE")
    @Basic
    var csResponceCode: String? = null

    @Column(name = "CS_RESPONCE_MESSAGE")
    @Basic
    var csResponceMessage: String? = null

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

    @Column(name = "APPROVAL_STATUS")
    @Basic
    var approvalStatus: Int? = null

    @Column(name = "REJECTION_STATUS")
    @Basic
    var rejectionStatus: Int? = null

    @Column(name = "DEFFERAL_STATUS")
    @Basic
    var defferalStatus: Int? = null

    @Column(name = "WETC_SECRETARY")
    @Basic
    var wetcSecretary: Long? = null

    @Column(name = "WETC_CHAIRMAN")
    @Basic
    var wetcChairman: Long? = null

    @Column(name = "NCS_SECRETARY")
    @Basic
    var ncsSecretary: Long? = null

    @Column(name = "CS")
    @Basic
    var cs: Long? = null

    @Column(name = "PVOC_WA_STATUS")
    @Basic
    var pvocWaStatus: Int? = null

    @Column(name = "PVOC_WA_STARTED_ON")
    @Basic
    var pvocWaStartedOn: Timestamp? = null

    @Column(name = "PVOC_WA_COMPLETED_ON")
    @Basic
    var pvocWaCompletedOn: Timestamp? = null

    @Column(name = "PVOC_WA_PROCESS_INSTANCE_ID")
    @Basic
    var pvocWaProcessInstanceId: String? = null

    @Column(name = "WETC_MEMBER")
    @Basic
    var wetcMember: Long? = null


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as PvocWaiversApplicationEntity
        return id == that.id &&
                status == that.status &&
                applicantName == that.applicantName &&
                phoneNumber == that.phoneNumber &&
                emailAddress == that.emailAddress &&
                kraPin == that.kraPin &&
                address == that.address &&
                category == that.category &&
                justification == that.justification &&
                productDescription == that.productDescription &&
                documentation == that.documentation &&
                reviewStatus == that.reviewStatus &&
                serialNo == that.serialNo &&
                csResponceCode == that.csResponceCode &&
                csResponceMessage == that.csResponceMessage &&
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
                approvalStatus == that.approvalStatus &&
                rejectionStatus == that.rejectionStatus &&
                defferalStatus == that.defferalStatus &&
                wetcSecretary == that.wetcSecretary &&
                wetcChairman == that.wetcChairman &&
                ncsSecretary == that.ncsSecretary &&
                pvocWaStatus == that.pvocWaStatus &&
                pvocWaStartedOn == that.pvocWaStartedOn &&
                pvocWaCompletedOn == that.pvocWaCompletedOn &&
                pvocWaProcessInstanceId == that.pvocWaProcessInstanceId &&
                wetcMember == that.wetcMember &&
                cs == that.cs


    }

    override fun hashCode(): Int {
        return Objects.hash(id, status, applicantName, phoneNumber, emailAddress, kraPin, address, category, justification, productDescription, documentation, reviewStatus, serialNo, csResponceCode, csResponceMessage, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn, approvalStatus, rejectionStatus, defferalStatus, wetcSecretary, wetcChairman, ncsSecretary, pvocWaStatus, pvocWaStartedOn, pvocWaCompletedOn, pvocWaProcessInstanceId, pvocWaCompletedOn, wetcMember, cs)
    }
}