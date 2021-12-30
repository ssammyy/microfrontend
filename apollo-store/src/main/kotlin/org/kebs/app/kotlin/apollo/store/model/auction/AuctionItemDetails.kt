package org.kebs.app.kotlin.apollo.store.model.auction

import org.kebs.app.kotlin.apollo.store.model.CorsBakEntity
import org.kebs.app.kotlin.apollo.store.model.CorsEntity
import java.io.Serializable
import java.math.BigDecimal
import java.sql.Date
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_AUCTION_ITEM_DETAILS")
class AuctionItemDetails : Serializable {

    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_AUCTION_ITEM_DETAILS_SEQ_GEN", sequenceName = "DAT_KEBS_AUCTION_ITEM_DETAILS_COR_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_AUCTION_ITEM_DETAILS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = null

    @Column(name = "AUCTION_ID")
    @Basic
    var auctionId: Long? = null

    @Column(name = "SERIAL_NO")
    @Basic
    var serialNo: String? = null

    @Column(name = "ITEM_NAME")
    @Basic
    var itemName: String? = null

    @Column(name = "QUANTITY")
    @Basic
    var quantity: BigDecimal? = null

    @Column(name = "TARE_WEIGHT")
    @Basic
    var tareWeight: BigDecimal? = null

    @Column(name = "UNIT_PRICE")
    @Basic
    var unitPrice: BigDecimal? = null

    @Column(name = "TOTAL_PRICE")
    @Basic
    var totalPrice: BigDecimal? = null

    @JoinColumn(name = "COR_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY)
    var corId: CorsBakEntity? = null

    @Column(name = "ITEM_TYPE")
    @Basic
    var itemType: String? = null // VEHICLES/GOODS

    @Column(name = "CHASSIS_NO")
    @Basic
    var chassisNo: String? = null

    @Column(name = "BODY_TYPE")
    @Basic
    var bodyType: String? = null

    @Column(name = "FUEL")
    @Basic
    var fuel: String? = null

    @Column(name = "TYPE_OF_VEHICLE")
    @Basic
    var typeOfVehicle: String? = null

    @Column(name = "VEHICLE_MAKE")
    @Basic
    var make: String? = null

    @Column(name = "VEHICLE_MODEL")
    @Basic
    var model: String? = null

    @Column(name = "INSPECTION_MILEAGE")
    @Basic
    var inspectionMileage: String? = null

    @Column(name = "UNITS_OF_MILEAGE")
    @Basic
    var unitsOfMileage: String? = null

    @Column(name = "COLOR")
    @Basic
    var color: String? = null

    @Column(name = "TRANSMISSION")
    @Basic
    var transmission: String? = null

    @Column(name = "INSPECTION_DETAILS")
    @Basic
    var inspectionDetails: String? = null

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
        val that = other as AuctionItemDetails
        return id == that.id &&
                chassisNo == that.chassisNo &&
                bodyType == that.bodyType &&
                fuel == that.fuel &&
                tareWeight == that.tareWeight &&
                typeOfVehicle == that.typeOfVehicle &&
                make == that.make &&
                model == that.model &&
                inspectionMileage == that.inspectionMileage &&
                unitsOfMileage == that.unitsOfMileage &&
                color == that.color &&
                transmission == that.transmission &&
                inspectionDetails == that.inspectionDetails &&
                status == that.status &&
                varField1 == that.varField1 &&
                varField2 == that.varField2 &&
                varField3 == that.varField3 &&
                varField4 == that.varField4 &&
                varField5 == that.varField5 &&
                createdBy == that.createdBy &&
                createdOn == that.createdOn &&
                modifiedBy == that.modifiedBy &&
                modifiedOn == that.modifiedOn &&
                deleteBy == that.deleteBy &&
                deletedOn == that.deletedOn
    }

    override fun hashCode(): Int {
        return Objects.hash(id, chassisNo, bodyType, fuel, tareWeight, typeOfVehicle, make, model, inspectionMileage, unitsOfMileage, color, transmission, inspectionDetails, status, varField1, varField2, varField3, varField4, varField5, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn)
    }
}