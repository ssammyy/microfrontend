package org.kebs.app.kotlin.apollo.store.model.di

import java.io.Serializable
import java.math.BigDecimal
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_IDF_ITEM_DETAILS")
class IDFItemDetailsEntity : Serializable {

    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_IDF_ITEM_DETAILS_SEQ_GEN", sequenceName = "DAT_KEBS_IDF_ITEM_DETAILS_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_IDF_ITEM_DETAILS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = null

    @Column(name = "CATEGORY_TYPE_PRO", nullable = true, length = 200)
    @Basic
    var categoryTypePro: String? = null

    @Column(name = "SUB_CATEGORY_PRO", nullable = true, length = 200)
    @Basic
    var subCategoryPro: String? = null

    @Column(name = "PREVIOUS_PRO", nullable = true, length = 200)
    @Basic
    var previousPro: String? = null

    @Column(name = "TARIFF_GOODS_DESC", nullable = true, length = 200)
    @Basic
    var tariffGoodsDesc: String? = null

    @Column(name = "ITEM_PRICE", nullable = true, precision = 0)
    @Basic
    var itemPrice: BigDecimal? = null

    @Column(name = "ITEM_NUM", nullable = true, precision = 0)
    @Basic
    var itemNum: Long? = null

    @Column(name = "NET_MASS", nullable = true, precision = 0)
    @Basic
    var netMass: Long? = null

    @Column(name = "COMMERCIAL_GOODS", nullable = true, length = 200)
    @Basic
    var commercialGoods: String? = null

    @Column(name = "ITEM_FREIGHT_VALUE", nullable = true, precision = 0)
    @Basic
    var itemFreightValue: Double? = null

    @Column(name = "COMMODITY_CODE", nullable = true, length = 200)
    @Basic
    var commodityCode: String? = null

    @Column(name = "INSURANCE_VALUE", nullable = true, precision = 0)
    @Basic
    var insuranceValue: BigDecimal? = null

    @Column(name = "CUSTOMS_VALUE", nullable = true, precision = 0)
    @Basic
    var customsValue: BigDecimal? = null

    @Column(name = "ORIGIN_COUNTRY", nullable = true, length = 200)
    @Basic
    var originCountry: String? = null

    @Column(name = "UNIT_NUM", nullable = true, precision = 0)
    @Basic
    var unitNum: Long? = null

    @Column(name = "UNIT_CODE", nullable = true, length = 200)
    @Basic
    var unitCode: String? = null

    @Column(name = "REQUESTED_PRO", nullable = true, length = 200)
    @Basic
    var requestedPro: String? = null

    @Column(name = "ITEM_OTHER_CHARGES", nullable = true, precision = 0)
    @Basic
    var itemOtherCharges: BigDecimal? = null

    @Column(name = "ITEM_FOB_VALUE", nullable = true, precision = 0)
    @Basic
    var itemFobValue: BigDecimal? = null

    @Column(name = "DESCRIPTION", nullable = true, length = 200)
    @Basic
    var description: String? = null

    @Column(name = "STATUS", nullable = true, precision = 0)
    @Basic
    var status: Long? = null

    @Column(name = "VAR_FIELD_1", nullable = true, length = 350)
    @Basic
    var varField1: String? = null

    @Column(name = "VAR_FIELD_2", nullable = true, length = 350)
    @Basic
    var varField2: String? = null

    @Column(name = "VAR_FIELD_3", nullable = true, length = 350)
    @Basic
    var varField3: String? = null

    @Column(name = "VAR_FIELD_4", nullable = true, length = 350)
    @Basic
    var varField4: String? = null

    @Column(name = "VAR_FIELD_5", nullable = true, length = 350)
    @Basic
    var varField5: String? = null

    @Column(name = "VAR_FIELD_6", nullable = true, length = 350)
    @Basic
    var varField6: String? = null

    @Column(name = "VAR_FIELD_7", nullable = true, length = 350)
    @Basic
    var varField7: String? = null

    @Column(name = "VAR_FIELD_8", nullable = true, length = 350)
    @Basic
    var varField8: String? = null

    @Column(name = "VAR_FIELD_9", nullable = true, length = 350)
    @Basic
    var varField9: String? = null

    @Column(name = "VAR_FIELD_10", nullable = true, length = 350)
    @Basic
    var varField10: String? = null

    @Column(name = "CREATED_BY", nullable = false, length = 100)
    @Basic
    var createdBy: String? = null

    @Column(name = "CREATED_ON", nullable = false)
    @Basic
    var createdOn: Timestamp? = null

    @Column(name = "MODIFIED_BY", nullable = true, length = 100)
    @Basic
    var modifiedBy: String? = null

    @Column(name = "MODIFIED_ON", nullable = true)
    @Basic
    var modifiedOn: Timestamp? = null

    @Column(name = "DELETE_BY", nullable = true, length = 100)
    @Basic
    var deleteBy: String? = null

    @Column(name = "DELETED_ON", nullable = true)
    @Basic
    var deletedOn: Timestamp? = null

    @JoinColumn(name = "IDF_DETAILS_ID", referencedColumnName = "ID")
    @ManyToOne
    var idfDetails: IDFDetailsEntity? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as IDFItemDetailsEntity
        return id == that.id && categoryTypePro == that.categoryTypePro && subCategoryPro == that.subCategoryPro && previousPro == that.previousPro && tariffGoodsDesc == that.tariffGoodsDesc && itemPrice == that.itemPrice && itemNum == that.itemNum && netMass == that.netMass && commercialGoods == that.commercialGoods && itemFreightValue == that.itemFreightValue && commodityCode == that.commodityCode && insuranceValue == that.insuranceValue && customsValue == that.customsValue && originCountry == that.originCountry && unitNum == that.unitNum && unitCode == that.unitCode && requestedPro == that.requestedPro && itemOtherCharges == that.itemOtherCharges && itemFobValue == that.itemFobValue && description == that.description && status == that.status && varField1 == that.varField1 && varField2 == that.varField2 && varField3 == that.varField3 && varField4 == that.varField4 && varField5 == that.varField5 && varField6 == that.varField6 && varField7 == that.varField7 && varField8 == that.varField8 && varField9 == that.varField9 && varField10 == that.varField10 && createdBy == that.createdBy && createdOn == that.createdOn && modifiedBy == that.modifiedBy && modifiedOn == that.modifiedOn && deleteBy == that.deleteBy && deletedOn == that.deletedOn
    }

    override fun hashCode(): Int {
        return Objects.hash(
            id,
            categoryTypePro,
            subCategoryPro,
            previousPro,
            tariffGoodsDesc,
            itemPrice,
            itemNum,
            netMass,
            commercialGoods,
            itemFreightValue,
            commodityCode,
            insuranceValue,
            customsValue,
            originCountry,
            unitNum,
            unitCode,
            requestedPro,
            itemOtherCharges,
            itemFobValue,
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
