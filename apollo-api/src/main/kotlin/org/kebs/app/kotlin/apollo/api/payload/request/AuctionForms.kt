package org.kebs.app.kotlin.apollo.api.payload.request

import com.fasterxml.jackson.annotation.JsonFormat
import org.kebs.app.kotlin.apollo.store.model.auction.AuctionItemDetails
import org.kebs.app.kotlin.apollo.store.model.auction.AuctionRequests
import java.math.BigDecimal
import java.sql.Date
import java.sql.Timestamp
import javax.persistence.Basic
import javax.persistence.Column
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

class AuctionItem {
    @NotEmpty(message = "Item serial number is required")
    var serialNo: String? = null

    @NotEmpty(message = "Item name is required")
    var itemName: String? = null
    var quantity: BigDecimal? = null
    var tareWeight: BigDecimal? = null
    var unitPrice: BigDecimal? = null
    var totalPrice: BigDecimal? = null

    @NotEmpty(message = "Item type is required")
    var itemType: String? = null // VEHICLES/GOODS

    // For vehicles
    var chassisNo: String? = null
    var bodyType: String? = null
    var fuel: String? = null
    var typeOfVehicle: String? = null
    var make: String? = null
    var model: String? = null
    var inspectionMileage: String? = null
    var unitsOfMileage: String? = null
    var color: String? = null
    var transmission: String? = null
    fun fillDetails(item: AuctionItemDetails) {
        item.serialNo = this.serialNo
        item.itemName = this.itemName
        item.quantity = this.quantity
        item.tareWeight = this.tareWeight
        item.unitPrice = this.unitPrice
        item.itemType = this.itemType
        item.chassisNo = this.chassisNo
        item.bodyType = this.bodyType
        item.fuel = this.fuel
        item.typeOfVehicle = this.typeOfVehicle
        item.make = this.make
        item.model = this.model
        item.inspectionMileage = this.inspectionMileage
        item.color = this.color
        item.transmission = this.transmission
    }
}

class AuctionForm {
    @NotEmpty(message = "Category should be VEHICLES or GOODS")
    var categoryCode: String? = null

    @NotEmpty(message = "Lot Number is required")
    var auctionLotNo: String? = null

    @NotNull(message = "Auction date is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    var auctionDate: Date? = null

    @NotEmpty(message = "Shipment location is required")
    var shipmentPort: String? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    var shipmentDate: Timestamp? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    var arrivalDate: Timestamp? = null

    @NotEmpty(message = "Importer name is required")
    var importerName: String? = null

    @NotEmpty(message = "Auction item location is required")
    var itemLocation: String? = null
    var importerPhone: String? = null

    var containerSize: String? = null

    @Size(min = 1, max = 200, message = "At least one item is required")
    var items: List<AuctionItem>? = null

    fun fillDetails(request: AuctionRequests) {
        request.location = this.itemLocation
        request.auctionLotNo = this.auctionLotNo
        request.auctionDate = this.auctionDate
        request.shipmentPort = this.shipmentPort
        request.shipmentDate = this.shipmentDate
        request.arrivalDate = this.arrivalDate
        request.importerName = this.importerName
        request.importerPhone = this.importerPhone
        request.containerSize=this.containerSize
    }

}

class AuctionAssignForm {
    @NotNull(message = "Please select inspection officer")
    val officerId: Long? = null

    @NotNull(message = "Please enter assignment remarks")
    val remarks: String? = null
}

class AuctionDemandNoteForm {
    @NotNull(message = "Please select inspection fee")
    val feeId: Long? = null

    @NotNull(message = "Please enter inspection remarks")
    val remarks: String? = null

}