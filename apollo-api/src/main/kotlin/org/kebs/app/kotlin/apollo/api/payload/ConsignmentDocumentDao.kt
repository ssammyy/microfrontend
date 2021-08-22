package org.kebs.app.kotlin.apollo.api.payload

import org.kebs.app.kotlin.apollo.store.model.di.CdInspectionGeneralEntity
import org.kebs.app.kotlin.apollo.store.model.di.CdInspectionMotorVehicleItemChecklistEntity
import org.kebs.app.kotlin.apollo.store.model.di.CdItemDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.di.ConsignmentDocumentDetailsEntity
import java.math.BigDecimal
import java.sql.Date
import java.sql.Timestamp
import javax.persistence.Basic
import javax.persistence.Column
import javax.persistence.Transient

class ConsignmentDocumentDao {
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
    var cdTypeCategory: String? = null
    var cdTypeName: String? = null
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
    var summaryPageURL: String? = null

    companion object {
        fun fromEntity(doc: ConsignmentDocumentDetailsEntity): ConsignmentDocumentDao {
            val dt = ConsignmentDocumentDao()
            dt.id = doc.id
            dt.summaryPageURL = doc.summaryPageURL
            dt.uuid = doc.uuid
            doc.cdType?.let {
                dt.cdType = it.id
                dt.cdTypeCategory = it.category
                dt.cdTypeName = it.typeName
            }
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

        fun fromList(docs: List<ConsignmentDocumentDetailsEntity>): List<ConsignmentDocumentDao> {
            val documents = mutableListOf<ConsignmentDocumentDao>()
            docs.forEach {
                documents.add(fromEntity(it))
            }
            return documents
        }
    }
}

class CdItemDetailsDao {
    var inspectionReportApprovalStatus: Int? = null
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

class CdInspectionGeneralDao {
    var id: Long? = null
    var confirmItemType: Long? = null
    var inspection: String? = null
    var category: String? = null
    var entryPoint: String? = null
    var cfs: String? = null
    var inspectionDate: Date? = null
    var importersName: String? = null
    var clearingAgent: String? = null
    var customsEntryNumber: String? = null
    var idfNumber: String? = null
    var ucrNumber: String? = null
    var cocNumber: String? = null
    var feePaid: String? = null
    var receiptNumber: String? = null
    var overallRemarks: String? = null
    var complianceStatus: Int? = null
    var complianceRecommendations: String? = null
    var inspectionReportFile: ByteArray? = null
    var inspectionReportApprovalStatus: Int? = null
    var inspectionReportDisapprovalComments: String? = null
    var inspectionReportDisapprovalDate: Date? = null
    var inspectionReportApprovalComments: String? = null
    var inspectionReportApprovalDate: Date? = null
    var description: String? = null
    var inspectionReportRefNumber: String? = null
    var status: Int? = null

    companion object {
        fun fromEntity(general: CdInspectionGeneralEntity): CdInspectionGeneralDao {
            val dt = CdInspectionGeneralDao()
            dt.inspectionReportApprovalStatus = general.inspectionReportApprovalStatus
            dt.id = general.id
            dt.inspection = general.inspection
            dt.entryPoint = general.entryPoint
            dt.cfs = general.cfs
            dt.importersName = general.importersName
            dt.clearingAgent = general.clearingAgent
            dt.customsEntryNumber = general.customsEntryNumber
            dt.idfNumber = general.idfNumber
            dt.ucrNumber = general.ucrNumber
            dt.cocNumber = general.cocNumber
            dt.feePaid = general.feePaid
            dt.receiptNumber = general.receiptNumber
            return dt
        }

        fun fromList(generals: List<CdInspectionGeneralEntity>): List<CdInspectionGeneralDao> {
            val dtList = mutableListOf<CdInspectionGeneralDao>()
            generals.forEach {
                dtList.add(fromEntity(it))
            }
            return dtList
        }
    }
}

class CdInspectionMotorVehicleItemChecklistDao {
    var id: Long? = null
    var serialNumber: String? = ""
    var makeVehicle: String? = ""
    var chassisNo: String? = ""
    var engineNoCapacity: String? = ""
    var manufactureDate: Date? = null
    var registrationDate: Date? = null
    var odemetreReading: String? = ""
    var driveRhdLhd: String? = ""
    var transmissionAutoManual: String? = ""
    var colour: String? = ""
    var overallAppearance: String? = ""
    var remarks: String? = ""
    var ministryReportFile: ByteArray? = null
    var ministryReportSubmitStatus: Int? = null
    var ministryReportReinspectionStatus: Int? = null
    var ministryReportReinspectionRemarks: String? = null
    var description: String? = null
    var status: Int? = null

    companion object {
        fun fromEntity(general: CdInspectionMotorVehicleItemChecklistEntity): CdInspectionMotorVehicleItemChecklistDao {
            val dt = CdInspectionMotorVehicleItemChecklistDao()
            dt.id = general.id
            dt.serialNumber = general.serialNumber
            dt.makeVehicle = general.makeVehicle
            dt.chassisNo = general.chassisNo
            dt.engineNoCapacity = general.engineNoCapacity
            dt.manufactureDate = general.manufactureDate
            dt.registrationDate = general.registrationDate
            dt.odemetreReading = general.odemetreReading
            dt.driveRhdLhd = general.driveRhdLhd
            dt.transmissionAutoManual = general.transmissionAutoManual
            dt.colour = general.colour
            dt.overallAppearance = general.overallAppearance
            dt.remarks = general.remarks
            dt.ministryReportFile = general.ministryReportFile
            dt.ministryReportReinspectionStatus = general.ministryReportSubmitStatus
            dt.ministryReportReinspectionRemarks = general.ministryReportReinspectionRemarks
            dt.ministryReportSubmitStatus = general.ministryReportSubmitStatus
            return dt
        }

        fun fromList(generals: List<CdInspectionMotorVehicleItemChecklistEntity>): List<CdInspectionMotorVehicleItemChecklistDao> {
            val dtList = mutableListOf<CdInspectionMotorVehicleItemChecklistDao>()
            generals.forEach {
                dtList.add(fromEntity(it))
            }
            return dtList
        }
    }
}