package org.kebs.app.kotlin.apollo.api.payload;

import org.kebs.app.kotlin.apollo.store.model.di.*
import java.sql.Timestamp
import java.util.*
import kotlin.random.Random
import kotlin.random.nextULong


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
    var idfNumber: String? = null
    var ucrNumber: String? = null
    var cocNumber: String? = null
    var feePaid: String? = null
    var receiptNumber: String? = null
    var overallRemarks: String? = null
    var complianceStatus: Int? = null
    var complianceRecommendations: String? = null
    var inspectionReportApprovalStatus: Int? = null
    var inspectionReportDisapprovalComments: String? = null
    var inspectionReportDisapprovalDate: Date? = null
    var inspectionReportApprovalComments: String? = null
    var inspectionReportApprovalDate: Date? = null
    var description: String? = null
    var inspectionReportRefNumber: String? = null
    var status: Int? = null
    var checklistTypeName: String? = null
    var checklistTypeId: Long? = null

    companion object {
        fun fromEntity(entity: CdInspectionGeneralEntity): InspectionGeneralDetailsDto {
            val dto = InspectionGeneralDetailsDto().apply {
                id = entity.id
                confirmItemType = entity.confirmItemType
                inspection = entity.inspection
                idfNumber = entity.idfNumber
                ucrNumber = entity.ucrNumber
                overallRemarks = entity.overallRemarks
                feePaid = entity.feePaid
                receiptNumber = entity.receiptNumber
                overallRemarks = entity.overallRemarks
                complianceRecommendations = entity.complianceRecommendations
                category = entity.category
                inspectionDate = entity.inspectionDate
                complianceStatus = entity.complianceStatus
                description = entity.description
                status = entity.status
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
    var instructionsUseManual: String? = null
    var warrantyPeriodDocumentation: String? = null
    var safetyCautionaryRemarks: String? = null
    var sizeClassCapacity: String? = null
    var certMarksPvocDoc: String? = null
    var disposalInstruction: String? = null
    var mfgNameAddress: String? = null
    var fiberComposition: String? = null
    var batchNoModelTypeRef: String? = null
    var sampleUpdated: Int? = null
    var description: String? = null
    var status: Int? = null
    var remarks: String? = null

    companion object {
        fun fromEntity(entity: CdInspectionEngineeringItemChecklistEntity): InspectionEngineeringItemDto {
            val dto = InspectionEngineeringItemDto().apply {
                id = entity.id
                inspection = entity.inspection?.id
                brand = entity.brand
                serialNumber = entity.serialNumber
                compliant = entity.compliant
                instructionsUseManual = entity.instructionsUseManual
                inspectionDate = entity.createdOn
                sampled = entity.sampled
                sampleUpdated = entity.sampleUpdated
                description = entity.description
                itemId = entity.itemId
                category = entity.category
                quantityVerified = entity.quantityVerified
                warrantyPeriodDocumentation = entity.warrantyPeriodDocumentation
                safetyCautionaryRemarks = entity.safetyCautionaryRemarks
                certMarksPvocDoc = entity.certMarksPvocDoc
                disposalInstruction = entity.disposalInstruction
                remarks = entity.remarks
                sizeClassCapacity = entity.sizeClassCapacity
                fiberComposition = entity.fiberComposition
                batchNoModelTypeRef = entity.batchNoModelTypeRef
                mfgNameAddress = entity.mfgNameAddress
                ksEasApplicable = entity.ksEasApplicable
                status = entity.status
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
    var confirmItemType: Long? = null
    var inspection: Long? = null
    var category: String? = null
    var entryPoint: String? = null
    var cfs: String? = null
    var inspectionDate: Timestamp? = null
    var importersName: String? = null
    var compliant: String? = null
    var customsEntryNumber: String? = null
    var idfNumber: String? = null
    var cocNumber: String? = null
    var feePaid: String? = null
    var receiptNumber: String? = null
    var overallRemarks: String? = null
    var inspectionReportApprovalComments: String? = null
    var description: String? = null
    var sampled: String? = null
    var status: Int? = null
    var sampleUpdated: Int? = null
    var brand: String? = null
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
                inspectionDate = entity.createdOn
                sampled = entity.sampled
                ssfId = entity.ssfId
                sampleUpdated = entity.sampleUpdated
                description = entity.description
                status = entity.status
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
    var customsEntryNumber: String? = null
    var idfNumber: String? = null
    var cocNumber: String? = null
    var feePaid: String? = null
    var receiptNumber: String? = null
    var overallRemarks: String? = null
    var inspectionReportApprovalComments: String? = null
    var description: String? = null
    var sampled: String? = null
    var status: Int? = null
    var sampleUpdated: Int? = null
    var brand: String? = null
    var serialNumber: String? = null
    var ssfId: Long? = null

    companion object {
        fun fromEntity(entity: CdInspectionOtherItemChecklistEntity): InspectionOtherItemDto {
            val dto = InspectionOtherItemDto().apply {
                id = entity.id
                inspection = entity.inspection?.id
                brand = entity.brand
                serialNumber = entity.serialNumber
                compliant = entity.compliant
                category = entity.category
                inspectionDate = entity.createdOn
                sampled = entity.sampled
                ssfId = entity.ssfId
                sampleUpdated = entity.sampleUpdated
                description = entity.description
                status = entity.status
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
    var registrationDate: Date? = null
    var manufactureDate: Date? = null
    var compliant: String? = null
    var overallRemarks: String? = null
    var inspectionReportApprovalComments: String? = null
    var description: String? = null
    var makeVehicle: String? = null
    var status: Int? = null
    var sampleUpdated: Int? = null
    var serialNumber: String? = null
    var ministryStation: String? = null
    var ministryInspection: String? = null
    var odemetreReading: String? = null
    var transmissionAutoManual: String? = null
    var engineNoCapacity: String? = null
    var hasMinistryInspection: Boolean?=null
    var ministryInspectionActive: Boolean?=null
    var ssfId: Long? = null

    companion object {
        fun fromEntity(entity: CdInspectionMotorVehicleItemChecklistEntity): InspectionMotorVehicleItemDto {
            val dto = InspectionMotorVehicleItemDto().apply {
                id = entity.id
                hasMinistryInspection=entity.ministryReportFile!=null
                ministryInspectionActive=entity.ministryReportSubmitStatus==1
                overallRemarks = entity.remarks
                makeVehicle = entity.makeVehicle
                inspection = entity.inspection?.id
                makeVehicle = entity.makeVehicle
                ministryInspection = entity.sampled
                serialNumber = entity.serialNumber
                compliant = entity.compliant
                category = entity.category
                odemetreReading = entity.odemetreReading
                transmissionAutoManual = entity.transmissionAutoManual
                engineNoCapacity = entity.engineNoCapacity
                registrationDate = entity.registrationDate
                manufactureDate = entity.manufactureDate
                inspectionDate = entity.createdOn
                ministryInspection = entity.sampled
                ssfId = entity.ssfId
                sampleUpdated = entity.sampleUpdated
                description = entity.description
                status = entity.status
            }
            entity.ministryStationId?.let {
                dto.ministryStation=it.stationName
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
    var inspection: Long? = null
    var inspectionDate: Timestamp? = null
    var compliant: String? = null
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