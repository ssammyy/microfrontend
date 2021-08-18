package org.kebs.app.kotlin.apollo.api.payload

import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.di.CdItemDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.di.CdStandardsEntity
import org.kebs.app.kotlin.apollo.store.model.di.CdStatusTypesEntity
import org.kebs.app.kotlin.apollo.store.model.di.ConsignmentDocumentDetailsEntity
import java.math.BigDecimal
import java.sql.Date
import java.sql.Timestamp
import javax.persistence.*

class ConsignmentDocument {
    var id: Long? = null
    var uuid: String? = null
    var issuedDateTime: String? = null
    var localCoiRemarks: String? = null
    var sendCoiRemarks: String? = null
    var ucrNumber: String? = null
    var version: Long? = null
    var idfNumber: String? = null
    var cocNumber: String? = null
    var sendDemandNoteRemarks: String? = null
    var sendDemandNote: Int? = null
    var localCoi: Int? = null
    var oldCdStatus: Int? = null
    var sendCoiStatus: Int? = null
    var localCocOrCorStatus: Int? = null
    var localCocOrCorRemarks: String? = null
    var localCocOrCorDate: Date? = null
    var compliantStatus: Int? = null
    var compliantRemarks: String? = null
    var compliantDate: Date? = null
    var blacklistApprovedStatus: Int? = null
    var blacklistApprovedRemarks: String? = null
    var blacklistApprovedDate: Date? = null
    var blacklistStatus: Int? = null
    var blacklistRemarks: String? = null
    var blacklistDate: Date? = null
    var blacklistId: Int? = null
    var assignedStatus: Int? = null
    var assignedRemarks: String? = null
    var assignPortRemarks: String? = null
    var assignedDate: Date? = null
    var reassignedStatus: Int? = null
    var reassignedRemarks: String? = null
    var reassignedDate: Date? = null
    var processRejectionStatus: Int? = null
    var processRejectionDate: Date? = null
    var processRejectionRemarks: String? = null
    var docTypeId: Long? = null
    var cdType: Long? = null
    var portOfArrival: Long? = null
    var freightStation: Long? = null
    var cdImporter: Long? = null
    var csApprovalStatus: Int? = null
    var approveRejectCdStatus: Int? = null
    var approveRejectCdDate: Date? = null
    var approveRejectCdRemarks: String? = null
    var cdRefNumber: String? = null
    var inspectionNotificationStatus: Int? = null
    var inspectionRemarks: String? = null
    var inspectionDate: Date? = null
    var targetApproveStatus: Int? = null
    var targetStatus: Int? = null
    var targetApproveRemarks: String? = null
    var targetApproveDate: Date? = null
    var targetDate: Date? = null
    var targetReason: String? = null
    var description: String? = null
    var status: Int? = null
    var applicantName: String? = null
    var applicationDate: Timestamp? = null
    var assignedTo: String? = null
    var declarationNumber: String? = null
    var applicationRefNo: String? = null

    companion object {
        fun fromEntity(doc: ConsignmentDocumentDetailsEntity): ConsignmentDocument {
            val dt = ConsignmentDocument()
            dt.id = doc.id
            dt.uuid = doc.uuid
            dt.cocNumber = doc.cocNumber
            dt.cdRefNumber = doc.cdRefNumber
            dt.ucrNumber = doc.ucrNumber
            dt.applicantName = doc.createdBy
            dt.applicationDate = doc.createdOn
            dt.approveRejectCdDate = doc.approveRejectCdDate
            dt.approveRejectCdStatus = doc.approveRejectCdStatus
            dt.assignedStatus = doc.assignedStatus
            doc.assignedInspectionOfficer?.let {
                dt.assignedTo = it.firstName + " " + it.lastName
            }
            doc.cdStandard?.let {
                dt.declarationNumber = it.declarationNumber
                dt.applicationRefNo = it.applicationRefNo
            }
            dt.cdType = dt.cdType
            return dt
        }

        fun fromList(docs: List<ConsignmentDocumentDetailsEntity>): List<ConsignmentDocument> {
            val documents = mutableListOf<ConsignmentDocument>()
            docs.forEach {
                documents.add(fromEntity(it))
            }
            return documents
        }
    }
}

class CdItemDetailsDao {
    var id: Long? = null
    var confirmFeeIdSelected: Long? = null
    var uuid: String? = null
    var itemDescription: String? = null
    var itemHsCode: String? = null
    var ownerPin: String? = null
    var ownerName: String? = null
    var internalFileNumber: String? = null
    var internalProductNo: String? = null
    var productTechnicalName: String? = null
    var productBrandName: String? = null
    var productActiveIngredients: String? = null
    var productPackagingDetails: String? = null
    var productClassCode: String? = null
    var productClassDescription: String? = null
    var dnoteStatus: Int? = null
    var inspectionReportStatus: Int? = null
    var itemNo: Long? = null
    var sampleBsNumberStatus: Int? = null
    var sampleSubmissionStatus: Int? = null
    var ministrySubmissionStatus: Int? = null
    var checklistStatus: Int? = null
    var quantity: BigDecimal? = null
    var packageQuantity: BigDecimal? = null
    var unitPriceNcy: BigDecimal? = null
    var itemGrossWeight: String? = null
    var hsDescription: String? = null
    var unitOfQuantity: String? = null
    var unitOfQuantityDesc: String? = null
    var foreignCurrencyCode: String? = null
    var totalPriceNcy: BigDecimal? = null
    var applicantRemarks: String? = null
    var supplimentaryQuantity: String? = null
    var unitPriceFcy: BigDecimal? = null
    var countryOfOrgin: String? = null
    var countryOfOrginDesc: String? = null
    var packageType: String? = null
    var packageTypeDesc: String? = null
    var marksAndContainers: String? = null
    var totalPriceFcy: BigDecimal? = null
    var itemNetWeight: String? = null
    var ucrNumber: String? = null
    var localCoi: Int? = null
    var inspectionNotificationStatus: Int? = null

    companion object {
        fun fromEntity(item: CdItemDetailsEntity): CdItemDetailsDao {
            val dt = CdItemDetailsDao()
            dt.id = item.id
            dt.uuid = item.uuid
            dt.itemNo = item.itemNo
            dt.itemDescription = item.itemDescription
            dt.itemHsCode = item.itemHsCode
            dt.quantity = item.quantity
            dt.unitOfQuantityDesc = item.unitOfQuantityDesc
            dt.unitOfQuantity = item.unitOfQuantity
            dt.countryOfOrgin = item.countryOfOrgin
            dt.ownerPin = item.ownerPin
            // Add document details
            item.cdDocId?.let { cd ->
                dt.localCoi = cd.localCoi
                dt.inspectionNotificationStatus = cd.inspectionNotificationStatus
            }
            return dt
        }

        fun fromList(items: List<CdItemDetailsEntity>): List<CdItemDetailsDao> {
            val dtList = mutableListOf<CdItemDetailsDao>()
            items.forEach {
                dtList.add(fromEntity(it))
            }
            return dtList
        }
    }
}