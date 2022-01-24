package org.kebs.app.kotlin.apollo.api.payload.response

import org.kebs.app.kotlin.apollo.store.model.auction.AuctionRequests
import org.kebs.app.kotlin.apollo.store.model.auction.AuctionUploadsEntity
import java.sql.Date
import java.sql.Timestamp

class AuctionRequestDto {
    var requestId: Long? = null
    var consignmentId: Long? = null // If it was inspected, this will be filled
    var categoryId: Long? = null // If it was inspected, this will be filled
    var categoryName: String? = null
    var auctionLotNo: String? = null
    var auctionDate: String? = null
    var shipmentPort: String? = null
    var shipmentDate: Timestamp? = null
    var arrivalDate: String? = null
    var importerName: String? = null
    var goodsDesc: String? = null
    var containerDets: String?=null
    var manifestNo: String? = null
    var blNo: String? = null
    var location: String? = null
    var importerPhone: String? = null
    var assigner: String? = null
    var assignedOfficer: String? = null
    var assignedOn: Date? = null
    var approvalStatus: Int? = null
    var approvalStatusDesc: String? = null
    var completed: String? = null
    var assigned: Boolean? = null
    var myTask: Boolean? = null
    var isSupervisor: Boolean? = null
    var isInspector: Boolean? = null
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
                auctionDate = auction.auctionDate?.toString()?:"NA"
                shipmentPort = auction.shipmentPort
                shipmentDate = auction.shipmentDate
                arrivalDate = auction.arrivalDate?.toString()
                importerName = auction.importerName
                importerPhone = auction.importerPhone
                serialNumber = auction.serialNumber
                reportId = auction.reportId
                manifestNo="NA"
                blNo="NA"
                goodsDesc=""
                containerDets=auction.location?:""
                approvalStatus = auction.approvalStatus
                approvedRejectedOn = auction.approvedRejectedOn
                assignedOn = auction.assignedOn
                demandNoteId = auction.demandNoteId
                containerSize = auction.containerSize
                remarks = auction.remarks
                status = auction.status
                assigned = false
            }
            dto.completed = when (auction.approvalStatus) {
                0 -> "NO"
                else -> "YES"
            }
            dto.approvalStatusDesc = when (auction.approvalStatus) {
                0 -> "NEW"
                1 -> "APPROVED"
                2 -> "REJECTED"
                else -> "UNKNOWN"
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

        fun fromList(auctions: List<AuctionRequests>): List<AuctionRequestDto> {
            val dtos = mutableListOf<AuctionRequestDto>()
            auctions.forEach { dtos.add(fromEntity(it)) }
            return dtos
        }
    }
}

class AuctionUploadDao {
    var uploadId: Long = 0
    var auctionId: Long? = null
    var filepath: String? = null
    var description: String? = null
    var name: String? = null
    var fileType: String? = null
    var documentType: String? = null
    var transactionDate: Date? = null
    var fileSize: Long? = null
    var status: Int? = null
    var createdBy: String? = null
    var createdOn: Timestamp? = null

    companion object {
        fun fromEntity(upload: AuctionUploadsEntity) = AuctionUploadDao()
                .apply {
                    uploadId = upload.id
                    auctionId = upload.auctionId?.id
                    filepath = upload.filepath
                    name = upload.name
                    fileType = upload.fileType
                    transactionDate = upload.transactionDate
                    fileSize = upload.fileSize
                    description = upload.description
                    status = upload.status
                    createdBy = upload.createdBy
                    createdOn = upload.createdOn
                }

        fun fromList(uploads: List<AuctionUploadsEntity>): List<AuctionUploadDao> {
            val data = mutableListOf<AuctionUploadDao>()
            uploads.forEach {
                data.add(fromEntity(it))
            }
            return data
        }
    }
}