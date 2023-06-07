package org.kebs.app.kotlin.apollo.store.model.qa

import java.sql.Timestamp
import javax.persistence.*
import java.io.Serializable


@Entity
@Table(name = " DAT_KEBS_QA_RAW_MATERIAL")
class QaRawMaterialEntity : Serializable {
    @Column(name = "ID")
    @Id
    @SequenceGenerator(name = "DAT_KEBS_QA_RAW_MATERIAL_SEQ_GEN", sequenceName = "DAT_KEBS_QA_RAW_MATERIAL_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_QA_RAW_MATERIAL_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long? = 0

    @Column(name = "DESCRIPTIONS")
    @Basic
    var descriptions: String? = null

    @Column(name = "STATUS")
    @Basic
    var status: Int? = null

    @Column(name = "name")
    @Basic
    var name: String? = null

    @Column(name = "STA10_ID")
    @Basic
    var sta10Id: Long? = null

    @Column(name = "ORIGIN")
    @Basic
    var origin: String? = null

    @Column(name = "SPECIFICATIONS")
    @Basic
    var specifications: String? = null

    @Column(name = "QUALITY_CHECKS_TESTING_RECORDS")
    @Basic
    var qualityChecksTestingRecords: String? = null

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
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as QaRawMaterialEntity

        if (id != other.id) return false
        if (status != other.status) return false
        if (name != other.name) return false

        if (qualityChecksTestingRecords != other.qualityChecksTestingRecords) return false
        if (descriptions != other.descriptions) return false
        if (sta10Id != other.sta10Id) return false
        if (origin != other.origin) return false
        if (specifications != other.specifications) return false
        if (varField1 != other.varField1) return false
        if (varField2 != other.varField2) return false
        if (varField3 != other.varField3) return false
        if (varField4 != other.varField4) return false
        if (varField5 != other.varField5) return false
        if (varField6 != other.varField6) return false
        if (varField7 != other.varField7) return false
        if (varField8 != other.varField8) return false
        if (varField9 != other.varField9) return false
        if (varField10 != other.varField10) return false
        if (createdBy != other.createdBy) return false
        if (createdOn != other.createdOn) return false
        if (modifiedBy != other.modifiedBy) return false
        if (modifiedOn != other.modifiedOn) return false
        if (deleteBy != other.deleteBy) return false
        if (deletedOn != other.deletedOn) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (status ?: 0)
        result = 31 * result + (descriptions?.hashCode() ?: 0)
        result = 31 * result + (qualityChecksTestingRecords?.hashCode() ?: 0)
        result = 31 * result + (sta10Id?.hashCode() ?: 0)
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (origin?.hashCode() ?: 0)
        result = 31 * result + (specifications?.hashCode() ?: 0)
        result = 31 * result + (varField1?.hashCode() ?: 0)
        result = 31 * result + (varField2?.hashCode() ?: 0)
        result = 31 * result + (varField3?.hashCode() ?: 0)
        result = 31 * result + (varField4?.hashCode() ?: 0)
        result = 31 * result + (varField5?.hashCode() ?: 0)
        result = 31 * result + (varField6?.hashCode() ?: 0)
        result = 31 * result + (varField7?.hashCode() ?: 0)
        result = 31 * result + (varField8?.hashCode() ?: 0)
        result = 31 * result + (varField9?.hashCode() ?: 0)
        result = 31 * result + (varField10?.hashCode() ?: 0)
        result = 31 * result + (createdBy?.hashCode() ?: 0)
        result = 31 * result + (createdOn?.hashCode() ?: 0)
        result = 31 * result + (modifiedBy?.hashCode() ?: 0)
        result = 31 * result + (modifiedOn?.hashCode() ?: 0)
        result = 31 * result + (deleteBy?.hashCode() ?: 0)
        result = 31 * result + (deletedOn?.hashCode() ?: 0)
        return result
    }


}