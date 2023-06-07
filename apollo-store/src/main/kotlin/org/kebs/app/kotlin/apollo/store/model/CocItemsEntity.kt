package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.math.BigDecimal
import java.sql.Timestamp
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotEmpty

@Entity
@Table(name = "DAT_KEBS_COC_ITEMS")
class CocItemsEntity : Serializable {
    @Column(name = "ID", nullable = false, precision = 0)
    @Id
    @SequenceGenerator(name = "DAT_KEBS_COC_ITEMS_SEQ_GEN", sequenceName = "DAT_KEBS_COC_ITEMS_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_COC_ITEMS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long = 0

    @Column(name = "COC_ID", nullable = false, length = 50)
    @Basic
    var cocId: Long = 0

    @Column(name = "ITEM_ID", nullable = false, length = 50)
    @Basic
    var itemId: Long? = 0

    @Column(name = "SHIPMENT_SEAL_NUMBER")
    var shipmentSealNumbers: String? = null

    @Column(name = "SHIPMENT_CONTAINER_NUMBER")
    var shipmentContainerNumber: String? = null

    @Column(name = "SHIPMENT_GROSS_WEIGHT")
    var shipmentGrossWeight: Double? = null

    @Column(name = "SHIPMENT_LINE_NUMBER", nullable = false, precision = 2)
    @Basic
    var shipmentLineNumber: Long = 0

    @Column(name = "SHIPMENT_LINE_HSCODE", nullable = false, length = 50)
    @Basic
    var shipmentLineHscode: String? = null

    @Column(name = "SHIPMENT_LINE_QUANTITY", nullable = false, precision = 2)
    @Basic
    var shipmentLineQuantity: BigDecimal = BigDecimal.ZERO

    @NotEmpty(message = "Required field")
    @Column(name = "SHIPMENT_LINE_UNITOF_MEASURE", nullable = true, length = 400)
    @Basic
    var shipmentLineUnitofMeasure: String? = null

    @NotEmpty(message = "Required field")
    @Column(name = "SHIPMENT_LINE_DESCRIPTION", nullable = true, length = 4000)
    @Basic
    var shipmentLineDescription: String? = null

    @Column(name = "SHIPMENT_LINE_VIN", nullable = true, length = 150)
    @Basic
    var shipmentLineVin: String? = null

    @Column(name = "DECLARED_HS_CODE", nullable = true, length = 400)
    @Basic
    var declaredHsCode: String? = null

    @Column(name = "SHIPMENT_LINE_STICKER_NUMBER", nullable = true, length = 30)
    @Basic
    var shipmentLineStickerNumber: String? = null

    @Column(name = "SHIPMENT_LINE_ICS", nullable = true, length = 4000)
    @Basic
    var shipmentLineIcs: String? = null

    @Column(name = "SHIPMENT_LINE_STANDARDS_REFERENCE", nullable = true, length = 4000)
    @Basic
    var shipmentLineStandardsReference: String? = null

    @Column(name = "SHIPMENT_LINE_LICENCE_REFERENCE", nullable = true, length = 4000)
    @Basic
    var shipmentLineLicenceReference: String? = null

    @Column(name = "SHIPMENT_LINE_REGISTRATION", nullable = true, length = 100)
    @Basic
    var shipmentLineRegistration: String? = null

    @Column(name = "PRODUCT_CATEGORY", nullable = true, length = 100)
    @Basic
    var productCategory: String? = null

    @Column(name = "SHIPMENT_QUANTITY_DELIVERED", nullable = true, precision = 19, scale = 2)
    @Basic
    var shipmentQuantityDelivered: BigDecimal = BigDecimal.ZERO

    @Column(name = "OWNER_PIN", nullable = true, length = 100)
    @Basic
    var ownerPin: String? = null

    @Column(name = "OWNER_NAME", nullable = true, length = 100)
    @Basic
    var ownerName: String? = null

    @Column(name = "STATUS", nullable = true, precision = 0)
    @Basic
    var status: Int? = null

    @Column(name = "VAR_FIELD_1", nullable = true, length = 350)
    @Basic
    var cocNumber: String? = null

    @Column(name = "VAR_FIELD_2", nullable = true, length = 350)
    @Basic
    var coiNumber: String? = null

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

    @Column(name = "CREATED_BY", nullable = true, length = 100)
    @Basic
    var createdBy: String? = null

    @Column(name = "CREATED_ON", nullable = true)
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

    @Column(name = "SHIPMENT_LINE_BRANDNAME", nullable = true, length = 100)
    @Basic
    var shipmentLineBrandName: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as CocItemsEntity
        return id == that.id && shipmentLineNumber == that.shipmentLineNumber && shipmentLineQuantity == that.shipmentLineQuantity &&
                cocId == that.cocId &&
                shipmentLineHscode == that.shipmentLineHscode &&
                shipmentLineUnitofMeasure == that.shipmentLineUnitofMeasure &&
                shipmentLineDescription == that.shipmentLineDescription &&
                shipmentLineVin == that.shipmentLineVin &&
                shipmentLineStickerNumber == that.shipmentLineStickerNumber &&
                shipmentLineIcs == that.shipmentLineIcs &&
                shipmentLineStandardsReference == that.shipmentLineStandardsReference &&
                shipmentLineLicenceReference == that.shipmentLineLicenceReference &&
                shipmentLineRegistration == that.shipmentLineRegistration &&
                ownerPin == that.ownerPin &&
                ownerName == that.ownerName &&
                status == that.status &&
                cocNumber == that.cocNumber &&
                coiNumber == that.coiNumber &&
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
                shipmentLineBrandName == that.shipmentLineBrandName
    }

    override fun hashCode(): Int {
        return Objects.hash(
            id,
            cocId,
            shipmentLineNumber,
            shipmentLineHscode,
            shipmentLineQuantity,
            shipmentLineUnitofMeasure,
            shipmentLineDescription,
            shipmentLineVin,
            shipmentLineStickerNumber,
            shipmentLineIcs,
            shipmentLineStandardsReference,
            shipmentLineLicenceReference,
            shipmentLineRegistration,
            ownerPin,
            ownerName,
            status,
            cocNumber,
            coiNumber,
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
            shipmentLineBrandName
        )
    }
}


