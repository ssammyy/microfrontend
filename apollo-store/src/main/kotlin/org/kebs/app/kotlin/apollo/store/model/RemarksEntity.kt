package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_REMARKS")
class RemarksEntity : Serializable{
    @Column(name = "ID")
    @Id
    @SequenceGenerator(name = "DAT_KEBS_REMARKS_SEQ_GEN", sequenceName = "DAT_KEBS_REMARKS_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_REMARKS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long? = 0

    @Column(name = "REMARK_STATUS")
    @Basic
    var remarkStatus: Int? = null

    @Column(name = "REMARKS_PROCESS")
    @Basic
    var remarksProcess: String? = null

    @Column(name = "REMARKS")
    @Basic
    var remarks: String? = null

    @Column(name = "USER_ID")
    @Basic
    var userId: Long? = null

    @Column(name = "CONSIGNMENT_DOCUMENT_ID")
    @Basic
    var consignmentDocumentId: Long? = null

    @Column(name = "DESCRIPTIONS")
    @Basic
    var descriptions: String? = null

    @Column(name = "PVOC_EXCEPTION_APPLICATION_ID")
    @Basic
    var pvocExceptionApplicationId: Long? = null

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

    @Column(name = "FIRST_NAME")
    @Basic
    var firstName: String? = null

    @Column(name = "LAST_NAME")
    @Basic
    var lastName: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as RemarksEntity
        return id == that.id &&
                remarksProcess == that.remarksProcess &&
                remarkStatus == that.remarkStatus &&
                remarks == that.remarks &&
                userId == that.userId &&
                consignmentDocumentId == that.consignmentDocumentId &&
                descriptions == that.descriptions &&
                pvocExceptionApplicationId == that.pvocExceptionApplicationId &&
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
                firstName == that.firstName &&
                lastName == that.lastName
    }

    override fun hashCode(): Int {
        return Objects.hash(id, remarkStatus, remarksProcess, remarks, userId, consignmentDocumentId, descriptions, pvocExceptionApplicationId,varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn, firstName, lastName)
    }
}