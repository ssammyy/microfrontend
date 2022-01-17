package org.kebs.app.kotlin.apollo.store.model.pvc

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_PVOC_WAIVERS_STATUS")
class PvocWaiversStatusEntity :Serializable {
    @get:Column(name = "ID")
    @get:Id
    var id: Long = 0
    @get:Column(name = "STATUS")
    @get:Basic
    var status: Long? = null
    @get:Column(name = "APPROVAL")
    @get:Basic
    var approval: String? = null
    @get:Column(name = "DEFFERAL")
    @get:Basic
    var defferal: String? = null
    @get:Column(name = "REJECTION")
    @get:Basic
    var rejection: String? = null
    @get:Column(name = "WETC_CHAIRMAN")
    @get:Basic
    var wetcChairman: String? = null
    @get:Column(name = "SECRETARY_NCS")
    @get:Basic
    var secretaryNcs: String? = null
    @get:Column(name = "CS")
    @get:Basic
    var cs: String? = null
    @get:Column(name = "APPROVAL_STATUS")
    @get:Basic
    var approvalStatus: Int? = null
    @get:Column(name = "DEFFERAL_STATUS")
    @get:Basic
    var defferalStatus: Int? = null
    @get:Column(name = "REJECTION_STATUS")
    @get:Basic
    var rejectionStatus: Int? = null
    @get:Column(name = "VAR_FIELD_1")
    @get:Basic
    var varField1: String? = null
    @get:Column(name = "VAR_FIELD_2")
    @get:Basic
    var varField2: String? = null
    @get:Column(name = "VAR_FIELD_3")
    @get:Basic
    var varField3: String? = null
    @get:Column(name = "VAR_FIELD_4")
    @get:Basic
    var varField4: String? = null
    @get:Column(name = "VAR_FIELD_5")
    @get:Basic
    var varField5: String? = null
    @get:Column(name = "VAR_FIELD_6")
    @get:Basic
    var varField6: String? = null
    @get:Column(name = "VAR_FIELD_7")
    @get:Basic
    var varField7: String? = null
    @get:Column(name = "VAR_FIELD_8")
    @get:Basic
    var varField8: String? = null
    @get:Column(name = "VAR_FIELD_9")
    @get:Basic
    var varField9: String? = null
    @get:Column(name = "VAR_FIELD_10")
    @get:Basic
    var varField10: String? = null
    @get:Column(name = "CREATED_BY")
    @get:Basic
    var createdBy: String? = null
    @get:Column(name = "CREATED_ON")
    @get:Basic
    var createdOn: Timestamp? = null
    @get:Column(name = "MODIFIED_BY")
    @get:Basic
    var modifiedBy: String? = null
    @get:Column(name = "MODIFIED_ON")
    @get:Basic
    var modifiedOn: Timestamp? = null
    @get:Column(name = "DELETE_BY")
    @get:Basic
    var deleteBy: String? = null
    @get:Column(name = "DELETED_ON")
    @get:Basic
    var deletedOn: Timestamp? = null

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as PvocWaiversStatusEntity
        return id == that.id &&
                status == that.status &&
                approval == that.approval &&
                defferal == that.defferal &&
                rejection == that.rejection &&
                wetcChairman == that.wetcChairman &&
                secretaryNcs == that.secretaryNcs &&
                cs == that.cs &&
                approvalStatus == that.approvalStatus &&
                defferalStatus == that.defferalStatus &&
                rejectionStatus == that.rejectionStatus &&
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
        return Objects.hash(id, status, approval, defferal, rejection, wetcChairman, secretaryNcs, cs, approvalStatus, defferalStatus, rejectionStatus, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn)
    }
}