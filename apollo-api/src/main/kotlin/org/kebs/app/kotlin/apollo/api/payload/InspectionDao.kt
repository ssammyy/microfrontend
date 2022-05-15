package org.kebs.app.kotlin.apollo.api.payload;

import org.kebs.app.kotlin.apollo.store.model.di.*
import java.sql.Timestamp
import java.util.*


class InspectionGeneralDetailsDto {
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
    var ucrNumber: String? = null
    var cocNumber: String? = null
    var idfNumber: String? = null
    var currentStatus: String? = null
    var overallRemarks: String? = null
    var complianceStatus: Int? = null
    var complianceRecommendations: String? = null
    var inspectionReportApprovalStatus: Int? = null
    var inspectionReportDisapprovalComments: String? = null
    var inspectionReportDisapprovalDate: Date? = null
    var inspectionReportApprovalComments: String? = null
    var description: String? = null
    var inspectionOfficer: String? = null
    var version: Long? = null
    var status: Int? = null

    companion object {
        fun fromEntity(entity: CdInspectionGeneralEntity): InspectionGeneralDetailsDto {
            val dto = InspectionGeneralDetailsDto().apply {
                id = entity.id
                confirmItemType = entity.confirmItemType
                inspection = entity.inspection
                customsEntryNumber = entity.customsEntryNumber
                clearingAgent = entity.clearingAgent
                ucrNumber = entity.ucrNumber
                overallRemarks = entity.overallRemarks
                overallRemarks = entity.overallRemarks
                complianceRecommendations = entity.complianceRecommendations
                category = entity.category
                inspectionOfficer = entity.inspectionOfficer
                inspectionDate = entity.inspectionDate
                complianceStatus = entity.complianceStatus
                description = entity.description
                currentStatus = when (entity.currentChecklist) {
                    1 -> "CURRENT CHECKLIST"
                    else -> "OLD CHECKLIST"
                }
                version = entity.checklistVersion ?: 1
                status = entity.status
            }
            entity.cdDetails?.let {
                dto.cocNumber = it.cocNumber ?: "NA"
                dto.cfs = it.freightStation?.cfsName
                dto.idfNumber = it.idfNumber ?: "NA"
                dto.ucrNumber = it.ucrNumber ?: "NA"
            }
            return dto
        }

        fun fromList(entities: List<CdInspectionGeneralEntity>): List<InspectionGeneralDetailsDto> {
            val listData = mutableListOf<InspectionGeneralDetailsDto>()
            entities.forEach {
                listData.add(fromEntity(it))
            }
            return listData
        }
    }
}

class InspectionEngineeringItemDto {
    var id: Long? = null
    var inspection: Long? = null
    var inspectionDate: Date? = null
    var category: String? = null
    var itemId: Long? = null
    var compliant: String? = null
    var sampled: String? = null
    var serialNumber: String? = null
    var brand: String? = null
    var ksEasApplicable: String? = null
    var quantityVerified: String? = null
    var quantityDeclared: String? = null
    var instructionsUseManual: String? = null
    var warrantyPeriodDocumentation: String? = null
    var safetyCautionaryRemarks: String? = null
    var sizeClassCapacity: String? = null
    var certMarksPvocDoc: String? = null
    var disposalInstruction: String? = null
    var mfgNameAddress: String? = null
    var mfgName: String? = null
    var fiberComposition: String? = null
    var batchNoModelTypeRef: String? = null
    var sampleUpdated: Int? = null
    var description: String? = null
    var productDescription: String? = null
    var itemNumber: Long? = null
    var itemType: String? = null
    var csName: String? = null
    var csDate: String? = null
    var oicName: String? = null
    var oicDate: String? = null
    var createdOn: String? = null
    var status: Int? = null
    var remarks: String? = null

    companion object {
        fun fromEntity(entity: CdInspectionEngineeringItemChecklistEntity): InspectionEngineeringItemDto {
            val dto = InspectionEngineeringItemDto().apply {
                id = entity.id
                inspection = entity.inspection?.id
                brand = entity.brand.orEmpty()
                serialNumber = entity.serialNumber
                compliant = entity.compliant
                instructionsUseManual = entity.instructionsUseManual
                inspectionDate = entity.createdOn
                sampled = entity.sampled
                sampleUpdated = entity.sampleUpdated
                description = entity.description
                itemId = entity.itemId?.id
                category = entity.category
                quantityVerified = entity.quantityVerified
                quantityDeclared = entity.quantityVerified
                warrantyPeriodDocumentation = entity.warrantyPeriodDocumentation
                safetyCautionaryRemarks = entity.safetyCautionaryRemarks
                certMarksPvocDoc = entity.certMarksPvocDoc
                disposalInstruction = entity.disposalInstruction
                sizeClassCapacity = entity.sizeClassCapacity
                fiberComposition = entity.fiberComposition
                batchNoModelTypeRef = entity.batchNoModelTypeRef
                mfgNameAddress = entity.mfgNameAddress
                mfgName = entity.mfgNameAddress
                ksEasApplicable = entity.ksEasApplicable
                remarks = entity.remarks.orEmpty()
                status = entity.status
            }
            entity.itemId?.let {
                dto.itemNumber = it.itemNo
                dto.productDescription = it.itemDescription ?: it.hsDescription
                dto.itemId = it.id
                dto.itemType = it.checkListTypeId?.typeName
            }
            return dto
        }

        fun fromList(entities: List<CdInspectionEngineeringItemChecklistEntity>): List<InspectionEngineeringItemDto> {
            val listData = mutableListOf<InspectionEngineeringItemDto>()
            entities.forEach {
                listData.add(fromEntity(it))
            }
            return listData
        }
    }
}

class InspectionEngineeringDetailsDto {
    var id: Long? = null
    var serialNumber: String? = null
    var itemTypeId: Long? = null
    var itemTypeName: String? = null
    var inspection: Long? = null
    var inspectionDate: Timestamp? = null
    var compliant: String? = null
    var ucrNumber: String? = null
    var instructionsUseManual: String? = null
    var inspectionReportApprovalComments: String? = null
    var description: String? = null
    var remarks: String? = null
    var status: Int? = null
    var items: List<InspectionEngineeringItemDto>? = null

    companion object {
        fun fromEntity(entity: CdInspectionEngineeringChecklist): InspectionEngineeringDetailsDto {
            val dto = InspectionEngineeringDetailsDto().apply {
                id = entity.id
                inspection = entity.inspectionGeneral?.id
                serialNumber = entity.serialNumber
                remarks = entity.remarks
                inspectionDate = entity.createdOn
                description = entity.description
                status = entity.status
            }
            entity.inspectionGeneral?.cdDetails?.let {
                dto.ucrNumber = it.ucrNumber
            }
            entity.inspectionChecklistType?.let {
                dto.itemTypeId = it.id
                dto.itemTypeName = it.typeName
            }
            return dto
        }
    }
}


class InspectionAgrochemItemDto {
    var id: Long? = null
    var productDescription: String? = null
    var confirmItemType: Long? = null
    var inspection: Long? = null
    var category: String? = null
    var entryPoint: String? = null
    var cfs: String? = null
    var compositionIngredients: String? = null
    var storageCondition: String? = null
    var certMarksPvocDoc: String? = null
    var ksEasApplicable: String? = null
    var appearance: String? = null
    var inspectionDate: String? = null
    var dateMfgPackaging: String? = null
    var dateExpiry: String? = null
    var quantityDeclared: String? = null
    var quantityVerified: String? = null
    var mfgName: String? = null
    var importersName: String? = null
    var compliant: String? = null
    var customsEntryNumber: String? = null
    var description: String? = null
    var sampled: String? = null
    var remarks: String? = null
    var status: Int? = null
    var sampleUpdated: Int? = null
    var brand: String? = null
    var itemNumber: Long? = null
    var itemType: String? = null
    var csName: String? = null
    var csDate: String? = null
    var oicName: String? = null
    var oicDate: String? = null
    var createdOn: String? = null
    var serialNumber: String? = null
    var ssfId: Long? = null

    companion object {
        fun fromEntity(entity: CdInspectionAgrochemItemChecklistEntity): InspectionAgrochemItemDto {
            val dto = InspectionAgrochemItemDto().apply {
                id = entity.id
                inspection = entity.inspection?.id
                brand = entity.brand
                serialNumber = entity.serialNumber
                compliant = entity.compliant
                category = entity.category
                inspectionDate = entity.createdOn.toString()
                sampled = entity.sampled
                ssfId = entity.ssfId
                certMarksPvocDoc = entity.certMarksPvocDoc
                compositionIngredients = entity.compositionIngredients
                storageCondition = entity.storageCondition
                dateMfgPackaging = entity.dateMfgPackaging?.toString()
                appearance = entity.appearance
                quantityVerified = entity.quantityVerified
                quantityDeclared = entity.quantityDeclared
                dateExpiry = entity.dateExpiry?.toString()
                mfgName = entity.mfgName
                ksEasApplicable = entity.ksEasApplicable
                csName = ""
                remarks = entity.remarks
                createdOn = entity.createdOn.toString()
                sampleUpdated = entity.sampleUpdated
                description = entity.description
                status = entity.status
            }
            entity.itemId?.let {
                dto.itemNumber = it.itemNo
                dto.itemType = it.checkListTypeId?.typeName
                dto.productDescription = it.itemDescription

            }
            return dto
        }

        fun fromList(entities: List<CdInspectionAgrochemItemChecklistEntity>): List<InspectionAgrochemItemDto> {
            val listData = mutableListOf<InspectionAgrochemItemDto>()
            entities.forEach {
                listData.add(fromEntity(it))
            }
            return listData
        }
    }
}

class InspectionAgrochemDetailsDto {
    var id: Long? = null
    var serialNumber: String? = null
    var itemTypeId: Long? = null
    var itemTypeName: String? = null
    var inspection: Long? = null
    var inspectionDate: Timestamp? = null
    var compliant: String? = null
    var ucrNumber: String? = null
    var instructionsUseManual: String? = null
    var inspectionReportApprovalComments: String? = null
    var description: String? = null
    var status: Int? = null
    var remarks: String? = null
    var items: List<InspectionAgrochemItemDto>? = null

    companion object {
        fun fromEntity(entity: CdInspectionAgrochemChecklist): InspectionAgrochemDetailsDto {
            val dto = InspectionAgrochemDetailsDto().apply {
                id = entity.id
                inspection = entity.inspectionGeneral?.id
                serialNumber = entity.serialNumber
                inspectionDate = entity.createdOn
                remarks = entity.remarks
                description = entity.description
                status = entity.status
            }
            entity.inspectionGeneral?.cdDetails?.let {
                dto.ucrNumber = it.ucrNumber
            }
            entity.inspectionChecklistType?.let {
                dto.itemTypeId = it.id
                dto.itemTypeName = it.typeName
            }
            return dto
        }
    }
}


class InspectionOtherItemDto {
    var id: Long? = null
    var confirmItemType: Long? = null
    var inspection: Long? = null
    var category: String? = null
    var entryPoint: String? = null
    var cfs: String? = null
    var inspectionDate: Timestamp? = null
    var importersName: String? = null
    var compliant: String? = null
    var quantityDeclared: String? = null
    var quantityVerified: String? = null
    var ksEasApplicable: String? = null
    var packagingLabellig: String? = null
    var physicalCondition: String? = null
    var presenceAbsenceBanned: String? = null
    var documentation: String? = null
    var defects: String? = null
    var remarks: String? = null
    var description: String? = null
    var sampled: String? = null
    var status: Int? = null
    var sampleUpdated: Int? = null
    var brand: String? = null
    var serialNumber: String? = null
    var ssfId: Long? = null
    var productDescription: String? = null
    var itemNumber: Long? = null
    var itemType: String? = null
    var csName: String? = null
    var csDate: String? = null
    var oicName: String? = null
    var oicDate: String? = null
    var createdOn: String? = null

    companion object {
        fun fromEntity(entity: CdInspectionOtherItemChecklistEntity): InspectionOtherItemDto {
            val dto = InspectionOtherItemDto().apply {
                id = entity.id
                inspection = entity.inspection?.id
                brand = entity.brand
                remarks = entity.remarks
                serialNumber = entity.serialNumber
                compliant = entity.compliant
                category = entity.category
                quantityDeclared = entity.quantityDeclared
                quantityVerified = entity.quantityVerified
                packagingLabellig = entity.packagingLabelling
                physicalCondition = entity.physicalCondition
                presenceAbsenceBanned = entity.presenceAbsenceBanned
                ksEasApplicable = entity.ksEasApplicable
                documentation = entity.documentation
                defects = entity.defects
                inspectionDate = entity.createdOn
                sampled = entity.sampled
                ssfId = entity.ssfId
                sampleUpdated = entity.sampleUpdated
                description = entity.description
                status = entity.status
            }
            entity.itemId?.let {
                dto.createdOn = entity.createdOn.toString()
                dto.itemNumber = it.itemNo
                dto.productDescription = it.itemDescription ?: it.hsDescription
                dto.itemType = it.checkListTypeId?.typeName
            }
            return dto
        }

        fun fromList(entities: List<CdInspectionOtherItemChecklistEntity>): List<InspectionOtherItemDto> {
            val listData = mutableListOf<InspectionOtherItemDto>()
            entities.forEach {
                listData.add(fromEntity(it))
            }
            return listData
        }
    }
}

class InspectionOtherDetailsDto {
    var id: Long? = null
    var serialNumber: String? = null
    var itemTypeId: Long? = null
    var itemTypeName: String? = null
    var inspection: Long? = null
    var inspectionDate: Timestamp? = null
    var compliant: String? = null
    var ucrNumber: String? = null
    var instructionsUseManual: String? = null
    var inspectionReportApprovalComments: String? = null
    var description: String? = null
    var status: Int? = null
    var remarks: String? = null
    var items: List<InspectionOtherItemDto>? = null

    companion object {
        fun fromEntity(entity: CdInspectionOtherChecklist): InspectionOtherDetailsDto {
            val dto = InspectionOtherDetailsDto().apply {
                id = entity.id
                inspection = entity.inspectionGeneral?.id
                serialNumber = entity.serialNumber
                inspectionDate = entity.createdOn
                description = entity.description
                remarks = entity.remarks
                status = entity.status
            }
            entity.inspectionGeneral?.cdDetails?.let {
                dto.ucrNumber = it.ucrNumber
            }
            entity.inspectionChecklistType?.let {
                dto.itemTypeId = it.id
                dto.itemTypeName = it.typeName
            }
            return dto
        }
    }
}


class InspectionMotorVehicleItemDto {
    var id: Long? = null
    var inspection: Long? = null
    var category: String? = null
    var entryPoint: String? = null
    var inspectionDate: Timestamp? = null
    var makeVehicle: String? = null
    var chassisNo: String? = null
    var registrationDate: String? = null
    var manufactureDate: String? = null
    var driveRhdLhd: String? = null
    var compliant: String? = null
    var color: String? = null
    var overallAppearance: String? = null
    var remarks: String? = null
    var description: String? = null
    var itemType: String? = null
    var productDescription: String? = null
    var status: Int? = null
    var itemNumber: String? = null
    var sampleUpdated: Int? = null
    var serialNumber: String? = null
    var ministryStation: String? = null
    var ministryInspection: String? = null
    var odemetreReading: String? = null
    var transmissionAutoManual: String? = null
    var engineNoCapacity: String? = null
    var hasMinistryInspection: Boolean? = null
    var ministryInspectionActive: Boolean? = null
    var ssfId: Long? = null
    var csName: String? = null
    var csDate: String? = null
    var oicName: String? = null
    var oicDate: String? = null
    var createdOn: String? = null

    companion object {
        fun fromEntity(entity: CdInspectionMotorVehicleItemChecklistEntity): InspectionMotorVehicleItemDto {
            val dto = InspectionMotorVehicleItemDto().apply {
                id = entity.id
                hasMinistryInspection = entity.ministryReportFile != null
                ministryInspectionActive = "YES".equals(entity.sampled)
                remarks = entity.remarks
                makeVehicle = entity.makeVehicle
                inspection = entity.inspection?.id
                ministryInspection = entity.sampled
                serialNumber = entity.serialNumber
                compliant = entity.compliant
                category = entity.category
                color = entity.colour
                chassisNo = entity.chassisNo
                overallAppearance = entity.overallAppearance
                driveRhdLhd = entity.driveRhdLhd
                odemetreReading = entity.odemetreReading
                transmissionAutoManual = entity.transmissionAutoManual
                engineNoCapacity = entity.engineNoCapacity
                registrationDate = entity.registrationDate?.toString() ?: ""
                manufactureDate = entity.manufactureDate?.toString() ?: ""
                inspectionDate = entity.createdOn
                ssfId = entity.ssfId
                sampleUpdated = entity.sampleUpdated
                description = entity.description
                status = entity.status
                createdOn = entity.createdOn?.toString()
            }
            entity.itemId?.let {
                dto.productDescription = it.itemDescription
                dto.itemType = it.checkListTypeId?.typeName
                dto.itemNumber = it.itemNo.toString()
            }
            entity.ministryStationId?.let {
                dto.ministryStation = it.stationName
            }
            return dto
        }

        fun fromList(entities: List<CdInspectionMotorVehicleItemChecklistEntity>): List<InspectionMotorVehicleItemDto> {
            val listData = mutableListOf<InspectionMotorVehicleItemDto>()
            entities.forEach {
                listData.add(fromEntity(it))
            }
            return listData
        }
    }
}

class InspectionMotorVehicleDetailsDto {
    var id: Long? = null
    var serialNumber: String? = null
    var itemTypeId: Long? = null
    var itemTypeName: String? = null
    var odemetreReading: String? = null
    var inspection: Long? = null
    var inspectionDate: Timestamp? = null
    var ucrNumber: String? = null
    var instructionsUseManual: String? = null
    var inspectionReportApprovalComments: String? = null
    var description: String? = null
    var remarks: String? = null
    var status: Int? = null
    var items: List<InspectionMotorVehicleItemDto>? = null

    companion object {
        fun fromEntity(entity: CdInspectionMotorVehicleChecklist): InspectionMotorVehicleDetailsDto {
            val dto = InspectionMotorVehicleDetailsDto().apply {
                id = entity.id
                odemetreReading = entity.odemetreReading
                inspection = entity.inspectionGeneral?.id
                serialNumber = entity.serialNumber
                remarks = entity.remarks
                inspectionDate = entity.createdOn
                description = entity.description
                status = entity.status
            }
            entity.inspectionGeneral?.cdDetails?.let {
                dto.ucrNumber = it.ucrNumber
            }
            entity.inspectionChecklistType?.let {
                dto.itemTypeId = it.id
                dto.itemTypeName = it.typeName
            }
            return dto
        }
    }
}