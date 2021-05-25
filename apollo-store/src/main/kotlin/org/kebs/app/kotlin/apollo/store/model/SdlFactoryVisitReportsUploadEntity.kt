package org.kebs.app.kotlin.apollo.store.model

import org.kebs.app.kotlin.apollo.store.model.DatKebsSdlFactoryVisitReportsUploadEntity
import java.sql.Timestamp
import java.util.*
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "DAT_KEBS_SDL_FACTORY_VISIT_REPORTS_UPLOAD", schema = "APOLLO", catalog = "")
class DatKebsSdlFactoryVisitReportsUploadEntity {
    @get:Column(name = "ID")
    @get:GeneratedValue
    @get:Id
    var id: Long? = null
        private set

    @get:Column(name = "DATA")
    @get:Basic
    var data: ByteArray

    @get:Column(name = "NAME")
    @get:Basic
    var name: String? = null

    @get:Column(name = "TYPE")
    @get:Basic
    var type: String? = null

    @get:Column(name = "REPORT_ID")
    @get:Basic
    var reportId: Long? = null

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

    @get:Column(name = "LAST_MODIFIED_BY")
    @get:Basic
    var lastModifiedBy: String? = null

    @get:Column(name = "LAST_MODIFIED_ON")
    @get:Basic
    var lastModifiedOn: Timestamp? = null

    @get:Column(name = "UPDATE_BY")
    @get:Basic
    var updateBy: String? = null

    @get:Column(name = "UPDATED_ON")
    @get:Basic
    var updatedOn: Timestamp? = null

    @get:Column(name = "DELETE_BY")
    @get:Basic
    var deleteBy: String? = null

    @get:Column(name = "DELETED_ON")
    @get:Basic
    var deletedOn: Timestamp? = null

    @get:Column(name = "VERSION")
    @get:Basic
    var version: Long? = null
    fun setId(id: Long) {
        this.id = id
    }

    fun setId(id: Long?) {
        this.id = id
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as DatKebsSdlFactoryVisitReportsUploadEntity
        return id == that.id && Arrays.equals(
            data,
            that.data
        ) && name == that.name && type == that.type && reportId == that.reportId && varField1 == that.varField1 && varField2 == that.varField2 && varField3 == that.varField3 && varField4 == that.varField4 && varField5 == that.varField5 && varField6 == that.varField6 && varField7 == that.varField7 && varField8 == that.varField8 && varField9 == that.varField9 && varField10 == that.varField10 && createdBy == that.createdBy && createdOn == that.createdOn && lastModifiedBy == that.lastModifiedBy && lastModifiedOn == that.lastModifiedOn && updateBy == that.updateBy && updatedOn == that.updatedOn && deleteBy == that.deleteBy && deletedOn == that.deletedOn && version == that.version
    }

    override fun hashCode(): Int {
        var result = Objects.hash(
            id,
            name,
            type,
            reportId,
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
            lastModifiedBy,
            lastModifiedOn,
            updateBy,
            updatedOn,
            deleteBy,
            deletedOn,
            version
        )
        result = 31 * result + Arrays.hashCode(data)
        return result
    }
}