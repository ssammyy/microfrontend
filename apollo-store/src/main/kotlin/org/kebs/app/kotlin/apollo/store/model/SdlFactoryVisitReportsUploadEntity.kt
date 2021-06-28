package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_SDL_FACTORY_VISIT_REPORTS_UPLOAD")
class SdlFactoryVisitReportsUploadEntity : Serializable {

    @Id
    @SequenceGenerator(name = "DAT_KEBS_SDL_FACTORY_VISIT_REPORTS_UPLOAD_SEQ_GEN", sequenceName = "DAT_KEBS_SDL_FACTORY_VISIT_REPORTS_UPLOAD_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_SDL_FACTORY_VISIT_REPORTS_UPLOAD_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    var id: Long? = null
        private set

    @Column(name = "DATA")
    @Basic
    var data: ByteArray? = null

    @Column(name = "NAME")
    @Basic
    var name: String? = null

    @Column(name = "TYPE")
    @Basic
    var type: String? = null

    @Column(name = "REPORT_ID")
    @Basic
    var reportId: Long? = null

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

    fun setId(id: Long) {
        this.id = id
    }

    fun setId(id: Long?) {
        this.id = id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as SdlFactoryVisitReportsUploadEntity
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