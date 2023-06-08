package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Time
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_CORS_ITEMS")
class CorsItemsEntity : Serializable {
    @get:Column(name = "ID")
    @get:Id
    var id: Long = 0

    @get:Column(name = "ITEM_HS_CODE")
    @get:Basic
    var itemHsCode: String? = null

    @get:Column(name = "QUANTITY")
    @get:Basic
    var quantity: Long? = null

    @get:Column(name = "PACKAGE_QUANTITY")
    @get:Basic
    var packageQuantity: Long? = null

    @get:Column(name = "UNIT_PRICE_NCY")
    @get:Basic
    var unitPriceNcy: String? = null

    @get:Column(name = "ITEM_GROSS_WEIGHT")
    @get:Basic
    var itemGrossWeight: Long? = null

    @get:Column(name = "MAKE")
    @get:Basic
    var make: String? = null

    @get:Column(name = "HS_DESCRIPTION")
    @get:Basic
    var hsDescription: String? = null

    @get:Column(name = "UNIT_QUANTITY")
    @get:Basic
    var unitQuantity: String? = null

    @get:Column(name = "FOREIGN_CURRENCY_CODE")
    @get:Basic
    var foreignCurrencyCode: String? = null

    @get:Column(name = "CHASIS_NO")
    @get:Basic
    var chasisNo: String? = null

    @get:Column(name = "MODEL_NO")
    @get:Basic
    var modelNo: String? = null

    @get:Column(name = "SUPPLIMENTARYQUANTITY")
    @get:Basic
    var supplimentaryquantity: Long? = null

    @get:Column(name = "UNITPRICE_NCY")
    @get:Basic
    var unitpriceNcy: Long? = null

    @get:Column(name = "COUNTRYOFORIGIN")
    @get:Basic
    var countryoforigin: String? = null

    @get:Column(name = "VEHICLE_STATUS")
    @get:Basic
    var vehicleStatus: String? = null

    @get:Column(name = "APPLICANTREMARKS")
    @get:Basic
    var applicantremarks: String? = null

    @get:Column(name = "PACKAGETYPE")
    @get:Basic
    var packagetype: String? = null

    @get:Column(name = "TOTALPRICE_FCY")
    @get:Basic
    var totalpriceFcy: Long? = null

    @get:Column(name = "UNITPRICE_FCY")
    @get:Basic
    var unitpriceFcy: Long? = null

    @get:Column(name = "ITEM_NET_WEIGHT")
    @get:Basic
    var itemNetWeight: Long? = null

    @get:Column(name = "YEAR_OF_REGISTRATION")
    @get:Basic
    var yearOfRegistration: Time? = null

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

    @get:Column(name = "ITEM_DESCRIPTION")
    @get:Basic
    var itemDescription: String? = null

    @get:Column(name = "TOTAT_PRICE_NCY")
    @get:Basic
    var totatPriceNcy: Long? = null

    @get:Column(name = "CD_ID")
    @get:Basic
    var cdId: Long? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as CorsItemsEntity
        return id == that.id &&
                itemHsCode == that.itemHsCode &&
                quantity == that.quantity &&
                packageQuantity == that.packageQuantity &&
                unitPriceNcy == that.unitPriceNcy &&
                itemGrossWeight == that.itemGrossWeight &&
                make == that.make &&
                hsDescription == that.hsDescription &&
                unitQuantity == that.unitQuantity &&
                foreignCurrencyCode == that.foreignCurrencyCode &&
                chasisNo == that.chasisNo &&
                modelNo == that.modelNo &&
                supplimentaryquantity == that.supplimentaryquantity &&
                unitpriceNcy == that.unitpriceNcy &&
                countryoforigin == that.countryoforigin &&
                vehicleStatus == that.vehicleStatus &&
                applicantremarks == that.applicantremarks &&
                packagetype == that.packagetype &&
                totalpriceFcy == that.totalpriceFcy &&
                unitpriceFcy == that.unitpriceFcy &&
                itemNetWeight == that.itemNetWeight &&
                yearOfRegistration == that.yearOfRegistration &&
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
                itemDescription == that.itemDescription &&
                totatPriceNcy == that.totatPriceNcy &&
                cdId == that.cdId
    }

    override fun hashCode(): Int {
        return Objects.hash(id, itemHsCode, quantity, packageQuantity, unitPriceNcy, itemGrossWeight, make, hsDescription, unitQuantity, foreignCurrencyCode, chasisNo, modelNo, supplimentaryquantity, unitpriceNcy, countryoforigin, vehicleStatus, applicantremarks, packagetype, totalpriceFcy, unitpriceFcy, itemNetWeight, yearOfRegistration, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn, itemDescription, totatPriceNcy, cdId)
    }
}