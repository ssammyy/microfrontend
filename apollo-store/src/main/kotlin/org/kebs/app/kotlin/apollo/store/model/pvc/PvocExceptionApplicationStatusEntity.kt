package org.kebs.app.kotlin.apollo.store.model.pvc

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_PVOC_EXCEPTION_APPLICATION_STATUS")
class PvocExceptionApplicationStatusEntity : Serializable {
    @get:Column(name = "ID")
    @get:Id
    var id: Long = 0
    @get:Column(name = "MAP_ID")
    @get:Basic
    var mapId: Int? = null
    @get:Column(name = "STATUS")
    @get:Basic
    var status: Long? = null
    @get:Column(name = "INITIAL_STATUS")
    @get:Basic
    var initialStatus: String? = null
    @get:Column(name = "DIFFERED_STATUS")
    @get:Basic
    var differedStatus: String? = null
    @get:Column(name = "EXCEPTION_STATUS")
    @get:Basic
    var exceptionStatus: String? = null
    @get:Column(name = "REJECTED_STATUS")
    @get:Basic
    var rejectedStatus: String? = null
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
    @get:Column(name = "SECTION")
    @get:Basic
    var section: String? = null

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as PvocExceptionApplicationStatusEntity
        return id == that.id &&
                mapId == that.mapId &&
                status == that.status &&
                initialStatus == that.initialStatus &&
                differedStatus == that.differedStatus &&
                exceptionStatus == that.exceptionStatus &&
                rejectedStatus == that.rejectedStatus &&
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
                section == that.section
    }

    override fun hashCode(): Int {
        return Objects.hash(id, mapId, status, initialStatus, differedStatus, exceptionStatus, rejectedStatus, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn, section)
    }
}