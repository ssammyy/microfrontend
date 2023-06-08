package org.kebs.app.kotlin.apollo.store.model.di

import java.io.Serializable
import java.math.BigDecimal
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_DECLARATION_ITEM_DETAILS")
class DeclarationItemDetailsEntity : Serializable {

    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_DECLARATION_ITEM_DETAILS_SEQ_GEN", sequenceName = "DAT_KEBS_DECLARATION_ITEM_DETAILS_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_DECLARATION_ITEM_DETAILS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = null

    @Column(name = "ITEM_NUM", nullable = true, precision = 0)
    @Basic
    var itemNum: Int? = null

    @Column(name = "ITEM_PACKAGES", nullable = true, length = 20)
    @Basic
    var itemPackages: String? = null

    @Column(name = "COMMODITY_CODE", nullable = true, length = 20)
    @Basic
    var commodityCode: String? = null

    @Column(name = "ADDITIONAL_CODE_1", nullable = true, length = 10)
    @Basic
    var additionalCode1: String? = null

    @Column(name = "ADDITIONAL_CODE_2", nullable = true, length = 10)
    @Basic
    var additionalCode2: String? = null

    @Column(name = "ADDITIONAL_CODE_3", nullable = true, length = 10)
    @Basic
    var additionalCode3: String? = null

    @Column(name = "TARIFF_GOODS_DESC", nullable = true, length = 4000)
    @Basic
    var tariffGoodsDesc: String? = null

    @Column(name = "COMMERCIAL_GOODS", nullable = true, length = 200)
    @Basic
    var commercialGoods: String? = null

    @Column(name = "ORIGIN_COUNTRY", nullable = true, length = 10)
    @Basic
    var originCountry: String? = null

    @Column(name = "GROSS_MASS", nullable = true, precision = 4)
    @Basic
    var grossMass: BigDecimal? = null

    @Column(name = "PREFERENCE_NUM", nullable = true, length = 20)
    @Basic
    var preferenceNum: String? = null

    @Column(name = "REQUESTED_PRO", nullable = true, length = 10)
    @Basic
    var requestedPro: String? = null

    @Column(name = "PREVIOUS_PRO", nullable = true, length = 10)
    @Basic
    var previousPro: String? = null

    @Column(name = "CATEGORY_TYPE_PRO", nullable = true, length = 10)
    @Basic
    var categoryTypePro: String? = null

    @Column(name = "SUB_CATEGORY_PRO", nullable = true, length = 10)
    @Basic
    var subCategoryPro: String? = null

    @Column(name = "NET_MASS", nullable = true, precision = 4)
    @Basic
    var netMass: BigDecimal? = null

    @Column(name = "QUOTA", nullable = true, length = 20)
    @Basic
    var quota: String? = null

    @Column(name = "PRE_DOC_TYPE", nullable = true, length = 5)
    @Basic
    var preDocType: String? = null

    @Column(name = "PRE_DOC_ABB", nullable = true, length = 10)
    @Basic
    var preDocAbb: String? = null

    @Column(name = "PRE_DOC_FIRST_SUB_IDE", nullable = true, length = 30)
    @Basic
    var preDocFirstSubIde: String? = null

    @Column(name = "UNIT_NUM", nullable = true, precision = 4)
    @Basic
    var unitNum: BigDecimal? = null

    @Column(name = "UNIT_CODE", nullable = true, length = 20)
    @Basic
    var unitCode: String? = null

    @Column(name = "SUPPLEMENTARY_UNITS", nullable = true, length = 20)
    @Basic
    var supplementaryUnits: String? = null

    @Column(name = "SUPPLEMENTARY_UNITS_2", nullable = true, length = 20)
    @Basic
    var supplementaryUnits2: String? = null

    @Column(name = "ITEM_PRICE", nullable = true, precision = 4)
    @Basic
    var itemPrice: BigDecimal? = null

    @Column(name = "STAT_VALUE", nullable = true, precision = 4)
    @Basic
    var statValue: BigDecimal? = null

    @Column(name = "ITEM_FREIGHT_VALUE", nullable = true, precision = 4)
    @Basic
    var itemFreightValue: BigDecimal? = null

    @Column(name = "ITEM_OTHER_CHARGES", nullable = true, precision = 4)
    @Basic
    var itemOtherCharges: BigDecimal? = null

    @Column(name = "CUSTOMS_VALUE", nullable = true, precision = 4)
    @Basic
    var customsValue: BigDecimal? = null

    @Column(name = "CHASSIS_NUMBER", nullable = true, length = 50)
    @Basic
    var chassisNumber: String? = null

    @Column(name = "ENGINE_NUMBER", nullable = true, length = 50)
    @Basic
    var engineNumber: String? = null

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

    @JoinColumn(name = "DECLARATION_DETAILS_ID", referencedColumnName = "ID")
    @ManyToOne
    var declarationDetailsId: DeclarationDetailsEntity? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as DeclarationItemDetailsEntity
        return id == that.id && itemNum == that.itemNum && itemPackages == that.itemPackages && commodityCode == that.commodityCode && additionalCode1 == that.additionalCode1 && additionalCode2 == that.additionalCode2 && additionalCode3 == that.additionalCode3 && tariffGoodsDesc == that.tariffGoodsDesc && commercialGoods == that.commercialGoods && originCountry == that.originCountry && grossMass == that.grossMass && preferenceNum == that.preferenceNum && requestedPro == that.requestedPro && previousPro == that.previousPro && categoryTypePro == that.categoryTypePro && subCategoryPro == that.subCategoryPro && netMass == that.netMass && quota == that.quota && preDocType == that.preDocType && preDocAbb == that.preDocAbb && preDocFirstSubIde == that.preDocFirstSubIde && unitNum == that.unitNum && unitCode == that.unitCode && supplementaryUnits == that.supplementaryUnits && supplementaryUnits2 == that.supplementaryUnits2 && itemPrice == that.itemPrice && statValue == that.statValue && itemFreightValue == that.itemFreightValue && itemOtherCharges == that.itemOtherCharges && customsValue == that.customsValue && chassisNumber == that.chassisNumber && engineNumber == that.engineNumber && description == that.description && status == that.status && varField1 == that.varField1 && varField2 == that.varField2 && varField3 == that.varField3 && varField4 == that.varField4 && varField5 == that.varField5 && varField6 == that.varField6 && varField7 == that.varField7 && varField8 == that.varField8 && varField9 == that.varField9 && varField10 == that.varField10 && createdBy == that.createdBy && createdOn == that.createdOn && modifiedBy == that.modifiedBy && modifiedOn == that.modifiedOn && deleteBy == that.deleteBy && deletedOn == that.deletedOn
    }

    override fun hashCode(): Int {
        return Objects.hash(
            id,
            itemNum,
            itemPackages,
            commodityCode,
            additionalCode1,
            additionalCode2,
            additionalCode3,
            tariffGoodsDesc,
            commercialGoods,
            originCountry,
            grossMass,
            preferenceNum,
            requestedPro,
            previousPro,
            categoryTypePro,
            subCategoryPro,
            netMass,
            quota,
            preDocType,
            preDocAbb,
            preDocFirstSubIde,
            unitNum,
            unitCode,
            supplementaryUnits,
            supplementaryUnits2,
            itemPrice,
            statValue,
            itemFreightValue,
            itemOtherCharges,
            customsValue,
            chassisNumber,
            engineNumber,
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
