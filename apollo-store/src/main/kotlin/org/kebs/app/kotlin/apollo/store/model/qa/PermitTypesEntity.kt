package org.kebs.app.kotlin.apollo.store.model.qa

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "CFG_PERMIT_TYPES")
class PermitTypesEntity : Serializable {
    @Column(name = "ID")
    @Id
    @SequenceGenerator(name = "CFG_PERMIT_TYPES_SEQ_GEN", sequenceName = "CFG_PERMIT_TYPES_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "CFG_PERMIT_TYPES_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long? = 0

    @Column(name = "STATUS")
    @Basic
    var status: Int? = null

    @Column(name = "ST3_STATUS")
    @Basic
    var st3Status: Int? = null

    @Column(name = "ST10_STATUS")
    @Basic
    var st10Status: Int? = null

    @Column(name = "LOCAL_FOREIGN_STATUS")
    @Basic
    var localForeignStatus: Int? = null

    @Column(name = "SME_DECLARATION_FORM")
    @Basic
    var smeDeclarationForm: Int? = null

    @Column(name = "PERMIT_AWARD_YEARS")
    @Basic
    var permitAwardYears: Int? = null

    @Column(name = "MARK")
    @Basic
    var mark: String? = null

    @Column(name = "IMAGE")
    @Basic
    var image: String? = null

    @Column(name = "MARK_NUMBER")
    @Basic
    var markNumber: String? = null

    @Column(name = "DESCRIPTIONS")
    @Basic
    var descriptions: String? = null

    @Column(name = "TYPE_NAME")
    @Basic
    var typeName: String? = null

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
        if (other == null || javaClass != other.javaClass) return false
        val that = other as PermitTypesEntity
        return id == that.id &&
                mark == that.mark &&
                smeDeclarationForm == that.smeDeclarationForm &&
                localForeignStatus == that.localForeignStatus &&
                permitAwardYears == that.permitAwardYears &&
                markNumber == that.markNumber &&
                status == that.status &&
                descriptions == that.descriptions &&
                typeName == that.typeName &&
                st3Status == that.st3Status &&
                st10Status == that.st10Status &&
                image == that.image &&
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
            status,
            descriptions,
            smeDeclarationForm,
            typeName,
            permitAwardYears,
            localForeignStatus,
            st3Status,
            st10Status,
            image,
            markNumber,
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
            deletedOn,
            mark
        )
    }
}