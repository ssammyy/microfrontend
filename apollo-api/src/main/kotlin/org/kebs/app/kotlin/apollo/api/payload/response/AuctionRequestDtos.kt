package org.kebs.app.kotlin.apollo.api.payload.response

import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.auction.AuctionCategory
import org.kebs.app.kotlin.apollo.store.model.auction.AuctionRequests
import java.sql.Date
import java.sql.Timestamp
import javax.persistence.*

class AuctionRequestDto {
    var requestId: Long? = null
    var consignmentId: Long? = null // If it was inspected, this will be filled
    var categoryId: Long? = null // If it was inspected, this will be filled
    var categoryName: String? = null
    var auctionLotNo: String? = null
    var auctionDate: Date? = null
    var shipmentPort: String? = null
    var shipmentDate: Timestamp? = null
    var arrivalDate: Timestamp? = null
    var importerName: String? = null
    var location: String? = null
    var importerPhone: String? = null
    var assigner: String? = null
    var assignedOfficer: String? = null
    var assignedOn: Date? = null
    var approvalStatus: Int? = null
    var completed: String? = null
    var assigned: Boolean? = null
    var myTask: Boolean? = null
    var serialNumber: String? = null
    var approvedRejectedOn: Timestamp? = null
    var remarks: String? = null
    var reportId: Long? = null
    var demandNoteId: Long? = null
    var containerSize: String? = null
    var status: Int? = null

    companion object {
        fun fromEntity(auction: AuctionRequests): AuctionRequestDto {
            val dto = AuctionRequestDto()
            dto.apply {
                requestId = auction.id
                consignmentId = auction.consignmentId
                auctionLotNo = auction.auctionLotNo
                auctionDate = auction.auctionDate
                shipmentPort = auction.shipmentPort
                shipmentDate = auction.shipmentDate
                arrivalDate = auction.arrivalDate
                importerName = auction.importerName
                importerPhone = auction.importerPhone
                serialNumber = auction.serialNumber
                approvalStatus = auction.approvalStatus
                approvedRejectedOn = auction.approvedRejectedOn
                assignedOn = auction.assignedOn
                demandNoteId = auction.demandNoteId
                containerSize = auction.containerSize
                status = auction.status
                assigned = false
            }
            dto.completed = when (auction.approvalStatus) {
                0 -> "NO"
                else -> "YES"
            }
            auction.category?.let {
                dto.categoryName = it.categoryName
                dto.categoryId = it.id
            }
            dto.assigner = auction.assigner?.firstName
            auction.assignedOfficer?.let {
                dto.assignedOfficer = "${it.firstName} ${it.lastName}"
                dto.assigned = true
            }
            return dto
        }
    }
}