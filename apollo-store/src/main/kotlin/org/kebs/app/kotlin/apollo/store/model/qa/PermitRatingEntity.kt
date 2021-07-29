package org.kebs.app.kotlin.apollo.store.model.qa

import java.io.Serializable
import java.math.BigDecimal
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "CFG_KEBS_PERMIT_RATING")
class PermitRatingEntity : Serializable {
    @Column(name = "ID")
    @Id
    @SequenceGenerator(
        name = "CFG_KEBS_PERMIT_RATING_SEQ_GEN",
        sequenceName = "CFG_KEBS_PERMIT_RATING_SEQ",
        allocationSize = 1
    )
    @GeneratedValue(generator = "CFG_KEBS_PERMIT_RATING_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long = 0

    @Column(name = "MIN")
    @Basic
    var min: BigDecimal? = null

    @Column(name = "MAX")
    @Basic
    var max: BigDecimal? = null

    @Column(name = "FIRM_FEE")
    @Basic
    var firmFee: BigDecimal? = null

    @Column(name = "PRODUCT_FEE")
    @Basic
    var productFee: BigDecimal? = null

    @Column(name = "EXTRA_PRODUCT_FEE")
    @Basic
    var extraProductFee: BigDecimal? = null

    @Column(name = "COUNT_BEFORE_FEE")
    @Basic
    var countBeforeFee: BigDecimal? = null

    @Column(name = "COUNT_BEFORE_FREE")
    @Basic
    var countBeforeFree: Long? = null

    @Column(name = "VALIDITY")
    @Basic
    var validity: Int? = null

    @Column(name = "INVOICE_DESC")
    @Basic
    var invoiceDesc: String? = null

    @Column(name = "DESC_NAME")
    @Basic
    var descName: String? = null

    @Column(name = "FIRM_TYPE")
    @Basic
    var firmType: String? = null

    @Column(name = "TAX_RATE")
    @Basic
    var taxRate: BigDecimal? = null

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
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as PermitRatingEntity
        return id == that.id && min == that.min && max == that.max && firmFee == that.firmFee && productFee == that.productFee && extraProductFee == that.extraProductFee && countBeforeFee == that.countBeforeFee && countBeforeFree == that.countBeforeFree && validity == that.validity && invoiceDesc == that.invoiceDesc && descName == that.descName && firmType == that.firmType && taxRate == that.taxRate && description == that.description && status == that.status && varField1 == that.varField1 && varField2 == that.varField2 && varField3 == that.varField3 && varField4 == that.varField4 && varField5 == that.varField5 && varField6 == that.varField6 && varField7 == that.varField7 && varField8 == that.varField8 && varField9 == that.varField9 && varField10 == that.varField10 && createdBy == that.createdBy && createdOn == that.createdOn && modifiedBy == that.modifiedBy && modifiedOn == that.modifiedOn && deleteBy == that.deleteBy && deletedOn == that.deletedOn
    }

    override fun hashCode(): Int {
        return Objects.hash(
            id,
            min,
            max,
            firmFee,
            productFee,
            extraProductFee,
            countBeforeFee,
            countBeforeFree,
            validity,
            invoiceDesc,
            descName,
            firmType,
            taxRate,
            description,
            status,
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
            deletedOn
        )
    }
}