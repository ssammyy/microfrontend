package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_PVOC_APPLICATION_TYPE")
class PvocApplicationTypeEntity : Serializable {
    @get:Column(name = "ID", nullable = false, precision = 0)
    @get:Id
    var id: Long = 0
    @get:Column(name = "STATUS", nullable = true, precision = 0)
    @get:Basic
    var status: Long? = null
    @get:Column(name = "NAME", nullable = true, length = 200)
    @get:Basic
    var name: String? = null
    @get:Column(name = "DESCRIPTION", nullable = true, length = 200)
    @get:Basic
    var description: String? = null
    @get:Column(name = "VAR_FIELD_1", nullable = true, length = 350)
    @get:Basic
    var varField1: String? = null
    @get:Column(name = "VAR_FIELD_2", nullable = true, length = 350)
    @get:Basic
    var varField2: String? = null
    @get:Column(name = "VAR_FIELD_3", nullable = true, length = 350)
    @get:Basic
    var varField3: String? = null
    @get:Column(name = "VAR_FIELD_4", nullable = true, length = 350)
    @get:Basic
    var varField4: String? = null
    @get:Column(name = "VAR_FIELD_5", nullable = true, length = 350)
    @get:Basic
    var varField5: String? = null
    @get:Column(name = "VAR_FIELD_6", nullable = true, length = 350)
    @get:Basic
    var varField6: String? = null
    @get:Column(name = "VAR_FIELD_7", nullable = true, length = 350)
    @get:Basic
    var varField7: String? = null
    @get:Column(name = "VAR_FIELD_8", nullable = true, length = 350)
    @get:Basic
    var varField8: String? = null
    @get:Column(name = "VAR_FIELD_9", nullable = true, length = 350)
    @get:Basic
    var varField9: String? = null
    @get:Column(name = "VAR_FIELD_10", nullable = true, length = 350)
    @get:Basic
    var varField10: String? = null
    @get:Column(name = "CREATED_BY", nullable = false, length = 100)
    @get:Basic
    var createdBy: String? = null
    @get:Column(name = "CREATED_ON", nullable = false)
    @get:Basic
    var createdOn: Timestamp? = null
    @get:Column(name = "MODIFIED_BY", nullable = true, length = 100)
    @get:Basic
    var modifiedBy: String? = null
    @get:Column(name = "MODIFIED_ON", nullable = true)
    @get:Basic
    var modifiedOn: Timestamp? = null
    @get:Column(name = "DELETE_BY", nullable = true, length = 100)
    @get:Basic
    var deleteBy: String? = null
    @get:Column(name = "DELETED_ON", nullable = true)
    @get:Basic
    var deletedOn: Timestamp? = null
//    @get:OneToMany(mappedBy = "datKebsPvocApplicationTypeByApplicationType")
//    var pvocApplicationsById: Collection<PvocApplicationEntity>? = null

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as PvocApplicationTypeEntity
        return id == that.id &&
                status == that.status &&
                name == that.name &&
                description == that.description &&
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
        return Objects.hash(id, status, name, description, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn)
    }

}