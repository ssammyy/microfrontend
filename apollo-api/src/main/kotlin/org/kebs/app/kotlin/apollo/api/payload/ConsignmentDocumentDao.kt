package org.kebs.app.kotlin.apollo.api.payload

import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.TaskDetails
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.di.DiTaskDetails
import org.kebs.app.kotlin.apollo.store.model.ServiceMapsEntity
import org.kebs.app.kotlin.apollo.store.model.di.*
import org.springframework.security.core.Authentication
import java.io.Serializable
import java.math.BigDecimal
import java.sql.Date
import java.sql.Timestamp
import javax.persistence.*

fun checkHasAuthority(role: String, auth: Authentication): Boolean {
    return auth.authorities.stream().anyMatch { authority -> authority.authority == role }
}

class ConsignmentEnableUI {
    var owner: Boolean? = null
    var canChange: Boolean = false
    var demandNotePaid: Boolean = true
    var supervisor: Boolean? = null
    var inspector: Boolean? = null
    var demandNote: Boolean = false
    var demandNoteRejected: Boolean = false
    var demandNoteDisabled: Boolean = false
    var sendCoi: Boolean? = null
    var targetItem: Boolean? = null
    var targeted: Boolean = false
    var targetRejected: Boolean = false
    var targetDisabled: Boolean = false
    var supervisorTarget: Boolean? = null
    var attachments: Boolean? = null
    var approveReject: Boolean? = null
    var compliant: Boolean? = null
    var corRequest: Boolean? = null
    var cocRequest: Boolean? = null
    var assigned: Boolean? = null
    var completed: Boolean? = null
    var idfAvailable: Boolean = false
    var cocAvailable: Boolean = false
    var corAvailable: Boolean = false
    var ncrAvailable: Boolean = false
    var coiAvailable: Boolean = false
    var complianceDisabled: Boolean = false
    var declarationDocument: Boolean = false
    var riskProfileImporter: Boolean = false
    var riskProfileConsignor: Boolean = false
    var riskProfileConsignee: Boolean = false
    var canInspect: Boolean? = null
    var checklistFilled: Boolean = false
    var hasPort: Boolean? = null
    var demandNoteRequired: Boolean=false
    var hasActiveProcess = false

    companion object {
        fun fromEntity(cd: ConsignmentDocumentDetailsEntity, map: ServiceMapsEntity, authentication: Authentication): ConsignmentEnableUI {
            val modify = checkHasAuthority("DI_OFFICER_CHARGE_READ", authentication)
            val change = checkHasAuthority("DI_INSPECTION_OFFICER_READ", authentication)

            val ui = ConsignmentEnableUI().apply {
                supervisor = modify
                inspector = change
                hasActiveProcess = cd.diProcessStatus == map.activeStatus
                targetDisabled = (cd.targetStatus == map.initStatus || cd.targetStatus == map.invalidStatus)
                assigned = cd.assignedInspectionOfficer != null
                targeted = cd.targetStatus == map.activeStatus
                targetRejected = cd.targetStatus == map.invalidStatus
                idfAvailable = cd.idfNumber != null
                demandNoteRejected = cd.sendDemandNote == map.invalidStatus
                demandNoteDisabled = (cd.sendDemandNote == map.initStatus || cd.sendDemandNote == map.activeStatus || cd.inspectionChecklist == map.activeStatus)
                owner = cd.assignedInspectionOfficer?.userName == authentication.name
                demandNote = cd.sendDemandNote == map.activeStatus
                sendCoi = modify && cd.localCoi == map.activeStatus
                targetItem = change && cd.targetStatus != map.activeStatus
                supervisorTarget = !(cd.targetStatus == map.initStatus || cd.targetStatus == map.invalidStatus)
                attachments = (change || modify)
                ncrAvailable = !cd.ncrNumber.isNullOrEmpty()
                checklistFilled = cd.inspectionChecklist == map.activeStatus
                hasPort = (cd.portOfArrival != null && cd.freightStation != null)
                completed = cd.approveRejectCdStatusType?.let { it.modificationAllowed != map.activeStatus } == true || cd.oldCdStatus != null
                approveReject = (cd.targetApproveStatus == null || cd.inspectionDateSetStatus == map.activeStatus) && modify
            }
            cd.cdStandardsTwo?.let {
                ui.demandNoteRequired="DES_INSP".equals(it.cocType, true)
            }
            ui.complianceDisabled = (cd.compliantStatus == map.activeStatus || cd.compliantStatus == map.initStatus) || !ui.checklistFilled || ui.targetRejected
            ui.approveReject = !(ui.targetDisabled && ui.demandNoteDisabled && ui.complianceDisabled)
            cd.cdType?.let {
                ui.cocAvailable = it.localCocStatus == map.activeStatus && (cd.localCocOrCorStatus == map.activeStatus || cd.localCoi == map.activeStatus)
                ui.corAvailable = it.localCorStatus == map.activeStatus && cd.localCocOrCorStatus == map.activeStatus
                ui.coiAvailable = it.localCocStatus == map.activeStatus && cd.localCoi == map.activeStatus
                ui.corRequest = it.localCorStatus == map.activeStatus

                ui.cocRequest = it.localCocStatus == map.activeStatus
                ui.canInspect = it.inspectionStatus == map.activeStatus
            }
            return ui
        }
    }
}

class ConsignmentDocumentDao {
    var id: Long? = null
    var uuid: String? = null
    var ucrNumber: String? = null
    var version: Long? = null
    var idfNumber: String? = null
    var cocNumber: String? = null
    var sendDemandNote: Int? = null
    var localCoi: Int? = null
    var compliantStatus: Int? = null
    var compliantRemarks: String? = null
    var blacklistStatus: Int? = null
    var blacklistRemarks: String? = null
    var assignedStatus: Int? = null
    var docTypeId: Long? = null
    var docType: Long? = null
    var cdType: Long? = null
    var cdTypeCategory: String? = null
    var cdTypeName: String? = null
    var cdTypeDescription: String? = null
    var portOfArrival: Long? = null
    var freightStation: String? = null
    var freightStationId: Long? = null
    var cdImporter: Long? = null
    var approveRejectCdStatus: Int? = null
    var approveRejectCdDate: Date? = null
    var cdRefNumber: String? = null
    var inspectionDate: Date? = null
    var targetStatus: Int? = null
    var description: String? = null
    var status: Int? = null
    var applicantName: String? = null
    var applicationDate: Timestamp? = null
    var assignedTo: String? = null
    var assignedOn: Date? = null
    var declarationNumber: String? = null
    var applicationRefNo: String? = null
    var summaryPageURL: String? = null
    var approvalStatus: String? = null
    var applicationStatus: String? = null
    var assigned: Boolean = false
    var lastModifiedOn: Timestamp? = null
    var lastModifiedBy: String? = null
    var isNcrDocument: Boolean = false
    var taskDetails: DiTaskDetails? = null

    companion object {
        fun fromEntity(doc: ConsignmentDocumentDetailsEntity, ncrId: String = ""): ConsignmentDocumentDao {
            val dt = ConsignmentDocumentDao()
            dt.id = doc.id
            dt.summaryPageURL = doc.summaryPageURL
            dt.uuid = doc.uuid
            dt.lastModifiedOn = doc.modifiedOn
            dt.lastModifiedBy = doc.modifiedBy
            doc.cdType?.let {
                dt.cdType = it.id
                dt.cdTypeCategory = it.category
                dt.cdTypeName = it.typeName
                dt.cdTypeDescription = it.description
                dt.isNcrDocument = it.uuid == ncrId
            }
            doc.freightStation?.let {
                dt.freightStation = it.cfsName
                dt.freightStationId = it.id
            }
            dt.applicationStatus = doc.varField10
            dt.assigned = doc.assignedInspectionOfficer != null
            dt.localCoi = doc.localCoi
            dt.sendDemandNote = doc.sendDemandNote
            dt.version = doc.version
            dt.docTypeId = doc.docTypeId
            dt.cocNumber = doc.cocNumber
            dt.cdRefNumber = doc.cdRefNumber
            dt.ucrNumber = doc.ucrNumber
            dt.applicantName = doc.createdBy
            dt.applicationDate = doc.createdOn
            dt.assignedStatus = doc.assignedStatus
            doc.assignedInspectionOfficer?.let {
                dt.assignedTo = it.firstName + " " + it.lastName
                dt.assignedOn = doc.assignedDate
            }
            doc.cdStandard?.let {
                dt.declarationNumber = it.declarationNumber
                dt.applicationRefNo = it.applicationRefNo
                dt.approvalStatus = it.approvalStatus
            }
            dt.cdType = dt.cdType
            return dt
        }

        fun fromList(docs: List<ConsignmentDocumentDetailsEntity>, ncrId: String = ""): List<ConsignmentDocumentDao> {
            val documents = mutableListOf<ConsignmentDocumentDao>()
            docs.forEach {
                documents.add(fromEntity(it, ncrId))
            }
            return documents
        }
    }
}


class CdDocumentModificationHistoryDao : Serializable {
    var id: Long = 0
    var cdId: Long? = null
    var name: String? = null
    var comment: String? = null
    var actionCode: String? = null
    var description: String? = null
    var status: Int? = null
    var createdBy: String? = null
    var createdOn: Timestamp? = null

    companion object {
        fun fromEntity(cdHistory: CdDocumentModificationHistory): CdDocumentModificationHistoryDao {
            val history = CdDocumentModificationHistoryDao()
            history.actionCode = cdHistory.actionCode
            history.comment = cdHistory.comment
            history.description = cdHistory.description
            history.cdId = cdHistory.cdId
            history.id = cdHistory.id
            history.name = cdHistory.name
            history.createdBy = cdHistory.createdBy
            history.createdOn = cdHistory.createdOn
            return history
        }

        fun fromList(list: List<CdDocumentModificationHistory>): List<CdDocumentModificationHistoryDao> {
            val documents = mutableListOf<CdDocumentModificationHistoryDao>()
            list.forEach {
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
    var itemHsCodeDescription: String? = null
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
    var sampledStatus: Int? = null
    var sampleBsNumberStatus: Int? = null
    var sampleSubmissionStatus: Int? = null
    var ministrySubmissionStatus: Int? = null
    var sampledCollectedStatus: Int? = null
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
    var inspectionDate: Date? = null
    var isVehicle = false
    var ministrySubmitted = false

    companion object {
        fun fromEntity(item: CdItemDetailsEntity, details: Boolean = false): CdItemDetailsDao {
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
                dt.inspectionDate = cd.inspectionDate
                cd.cdType?.let { cdType ->
                    dt.isVehicle = cdType.category.equals("VEHICLES", true)
                }

            }
            dt.apply {
                sampleBsNumberStatus = item.sampleBsNumberStatus
                sampleSubmissionStatus = item.sampleSubmissionStatus
                sampledCollectedStatus = item.sampledCollectedStatus
                sampledStatus = item.sampledStatus
                unitOfQuantity = item.unitOfQuantity
                packageQuantity = item.packageQuantity
                totalPriceNcy = item.totalPriceNcy
                unitPriceNcy = item.unitPriceNcy
                totalPriceFcy = item.totalPriceFcy
                unitPriceFcy = item.unitPriceFcy
            }
            dt.ministrySubmitted = item.ministrySubmissionStatus == 1
            // Other details
            if (details) {
                dt.apply {
                    itemHsCodeDescription = item.hsDescription
                    supplimentaryQuantity = item.supplimentaryQuantity
                    unitPriceNcy = item.unitPriceNcy
                    totalPriceNcy = item.totalPriceNcy
                    marksAndContainers = item.marksAndContainers
                    itemNetWeight = item.itemNetWeight
                    itemGrossWeight = item.itemGrossWeight
                    packageType = item.packageType
                    packageTypeDesc = item.packageTypeDesc
                    applicantRemarks = item.applicantRemarks
                    productClassCode = item.productClassCode
                    productClassDescription = item.productClassDescription
                    foreignCurrencyCode = item.foreignCurrencyCode
                }
            }
            return dt
        }

        fun fromList(items: List<CdItemDetailsEntity>, details: Boolean = false): List<CdItemDetailsDao> {
            val dtList = mutableListOf<CdItemDetailsDao>()
            items.forEach {
                dtList.add(fromEntity(it, details))
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
            dt.ucrNumber = general.ucrNumber
            general.cdDetails?.let {
                dt.cocNumber = it.cocNumber
                dt.idfNumber = it.idfNumber
                dt.cfs = it.freightStation?.cfsName
            }
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
    var ministryInspectionComplete: Boolean = false
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
            dt.ministryInspectionComplete = general.ministryReportFile != null
//            dt.ministryReportReinspectionStatus = general.ministryReportSubmitStatus
//            dt.ministryReportReinspectionRemarks = general.ministryReportReinspectionRemarks
//            dt.ministryReportSubmitStatus = general.ministryReportSubmitStatus
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

class CdStandardsEntityDao : Serializable {
    var id: Long = 0
    var applicationTypeCode: String? = null
    var applicationTypeDescription: String? = null
    var documentTypeCode: String? = null
    var cmsDocumentTypeCode: String? = null
    var documentTypeDescription: String? = null
    var consignmentTypeCode: String? = null
    var consignmentTypeDescription: String? = null
    var mdaCode: String? = null
    var expiryDate: String? = null
    var amendedDate: String? = null
    var usedStatus: String? = null
    var usedDate: String? = null
    var referencedPermitExemptionNo: String? = null
    var referencedPermitExemptionVersionNo: String? = null
    var mdaDescription: String? = null
    var documentCode: String? = null
    var documentDescription: String? = null
    var processCode: String? = null
    var processDescription: String? = null
    var applicationDate: String? = null
    var updatedDate: String? = null
    var approvalStatus: String? = null
    var approvalDate: String? = null
    var finalApprovalDate: String? = null
    var applicationRefNo: String? = null
    var versionNo: String? = null
    var ucrNumber: String? = null
    var declarationNumber: String? = null
    var description: String? = null
    var status: Int? = null

    companion object {
        fun fromEntity(standerd: CdStandardsEntity): CdStandardsEntityDao {
            return CdStandardsEntityDao()
                    .apply {
                        mdaCode = standerd.mdaCode
                        mdaDescription = standerd.mdaDescription
                        consignmentTypeCode = standerd.consignmentTypeCode
                        cmsDocumentTypeCode = standerd.cmsDocumentTypeCode
                        documentTypeCode = standerd.documentTypeCode
                        applicationTypeCode = standerd.applicationTypeCode
                        documentTypeDescription = standerd.documentTypeDescription
                        applicationTypeDescription = standerd.applicationTypeDescription
                        documentDescription = standerd.documentDescription
                        consignmentTypeCode = standerd.consignmentTypeCode
                        consignmentTypeDescription = standerd.consignmentTypeDescription
                        applicationRefNo = standerd.applicationRefNo
                        declarationNumber = standerd.declarationNumber
                        ucrNumber = standerd.ucrNumber
                    }
        }
    }
}

class DiUploadsEntityDao : Serializable {
    var id: Long = 0
    var filepath: String? = null
    var description: String? = null
    var name: String? = null
    var fileType: String? = null
    var fileSize: Long? = null
    var documentType: String? = null

    companion object {
        fun fromEntity(upload: DiUploadsEntity) = DiUploadsEntityDao()
                .apply {
                    id = upload.id
                    filepath = upload.filepath
                    description = upload.description
                    name = upload.name
                    fileType = upload.fileType
                    fileSize = upload.fileSize
                    documentType = upload.documentType
                }

        fun fromList(uploads: List<DiUploadsEntity>): List<DiUploadsEntityDao> {
            val data = mutableListOf<DiUploadsEntityDao>()
            uploads.forEach {
                data.add(fromEntity(it))
            }
            return data
        }
    }
}


class ConsignmentDocumentTypesEntityDao : Serializable {
    var id: Long = 0
    var uuid: String? = null
    var demandNotePrefix: String? = null
    var typeName: String? = null
    var description: String? = null
    var status: Int? = null
    var inspectionStatus: Int? = null
    var localCocStatus: Int? = null
    var localCorStatus: Int? = null
    var category: String? = null

    companion object {
        fun fromEntity(upload: ConsignmentDocumentTypesEntity) = ConsignmentDocumentTypesEntityDao()
                .apply {
                    id = upload.id
                    uuid = upload.uuid
                    demandNotePrefix = upload.demandNotePrefix
                    typeName = upload.typeName
                    description = upload.description
                    status = upload.status
                    inspectionStatus = upload.inspectionStatus
                    localCocStatus = upload.localCocStatus
                    localCorStatus = upload.localCorStatus
                    category = upload.category
                }

        fun fromList(uploads: List<ConsignmentDocumentTypesEntity>): List<ConsignmentDocumentTypesEntityDao> {
            val data = mutableListOf<ConsignmentDocumentTypesEntityDao>()
            uploads.forEach {
                data.add(fromEntity(it))
            }
            return data
        }
    }
}

class CdItemNonStandardEntityDto : Serializable {
    var id: Long? = null
    var chassisNo: String? = null
    var usedIndicator: String? = null
    var vehicleYear: String? = null
    var vehicleModel: String? = null
    var vehicleMake: String? = null
    var description: String? = null
    var status: Long? = null

    companion object {
        fun fromEntity(upload: CdItemNonStandardEntity) = CdItemNonStandardEntityDto()
                .apply {
                    id = upload.id
                    chassisNo = upload.chassisNo
                    usedIndicator = upload.usedIndicator
                    vehicleYear = upload.vehicleYear
                    vehicleMake = upload.vehicleMake
                    description = upload.description
                    status = upload.status
                }

        fun fromList(uploads: List<CdItemNonStandardEntity>): List<CdItemNonStandardEntityDto> {
            val data = mutableListOf<CdItemNonStandardEntityDto>()
            uploads.forEach {
                data.add(fromEntity(it))
            }
            return data
        }
    }
}

class CdStandardTwoEntityDao : Serializable {
    var id: Long = 0
    var purposeOfImport: String? = null
    var cocType: String? = null
    var localCocType: String? = null
    var description: String? = null
    var status: Int? = null
    var conditionsOfApproval: String? = null
    var applicantRemarks: String? = null
    var mdaRemarks: String? = null
    var customsRemarks: String? = null
    var cdProcessingFeeId: Long? = null
    var cdDocumentFeeId: Long? = null

    companion object {
        fun fromEntity(standerd: CdStandardsTwoEntity): CdStandardTwoEntityDao {
            return CdStandardTwoEntityDao()
                    .apply {
                        id = standerd.id
                        purposeOfImport = standerd.purposeOfImport
                        cocType = standerd.cocType
                        localCocType = standerd.localCocType
                        description = standerd.description
                        status = standerd.status
                        conditionsOfApproval = standerd.conditionsOfApproval
                        applicantRemarks = standerd.applicantRemarks
                        mdaRemarks = standerd.mdaRemarks
                        customsRemarks = standerd.customsRemarks
                        cdProcessingFeeId = standerd.cdProcessingFeeId
                        cdDocumentFeeId = standerd.cdDocumentFeeId
                    }
        }
    }
}