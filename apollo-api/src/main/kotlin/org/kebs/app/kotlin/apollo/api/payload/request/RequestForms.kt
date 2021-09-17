package org.kebs.app.kotlin.apollo.api.payload.request

import org.kebs.app.kotlin.apollo.store.model.SampleSubmissionDocumentsEntity
import org.kebs.app.kotlin.apollo.store.model.di.*
import org.kebs.app.kotlin.apollo.store.model.qa.QaSampleCollectionEntity
import org.kebs.app.kotlin.apollo.store.model.qa.QaSampleSubmissionEntity
import java.sql.Date
import java.time.Instant
import kotlin.random.Random
import kotlin.random.nextUInt
import kotlin.random.nextULong

class AgrochemCheckListItems {
    val category: String? = null
    var itemId: Long? = null
    var compliant: String? = null
    var sampled: String? = null
    var serialNumber: String? = null
    var brand: String? = null
    var ksEasApplicable: String? = null
    var quantityVerified: String? = null
    var dateMfgPackaging: String? = null
    var dateExpiry: String? = null
    var mfgName: String? = null
    var mfgAddress: String? = null
    var compositionIngredients: String? = null
    var storageCondition: String? = null
    var appearance: String? = null
    var certMarksPvocDoc: String? = null
    var remarks: String? = null
}

class AgrochemCheckList {
    var items: List<AgrochemCheckListItems>? = null
    var remarks: String? = null
}

class EngineeringCheckListItems {
    val category: String? = null
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
    var remarks: String? = null
}

class EngineeringChecklist {
    var items: List<EngineeringCheckListItems>? = null
    var remarks: String? = null
}

class VehicleCheckListItems {
    val stationId: Long = 0
    val category: String? = null
    var itemId: Long? = null
    var compliant: String? = null
    var submitToMinistry: String? = null
    var serialNumber: String? = null
    var makeVehicle: String? = null
    var chassisNo: String? = null
    var engineNoCapacity: String? = null
    var manufacturerDate: String? = null
    var registrationDate: String? = null
    var odemetreReading: String? = null
    var driveRhdLhd: String? = null
    var transmissionAutoManual: String? = null
    var colour: String? = null
    var overallAppearance: String? = null
    var remarks: String? = null
}

class VehicleInspectionCheckList {
    var serialNumber: String? = null
    var items: List<VehicleCheckListItems>? = null
    var remarks: String? = null
}

class OtherCheckListItems {
    val category: String? = null
    var itemId: Long? = null
    var compliant: String? = null
    var sampled: String? = null
    var serialNumber: String? = null
    var brand: String? = null
    var ksEasApplicable: String? = null
    var quantityVerified: String? = null
    var packagingLabelling: String? = null
    var physicalCondition: String? = null
    var defects: String? = null
    var presenceAbsenceBanned: String? = null
    var documentation: String? = null
}

class OtherCheckList {
    var serialNumber: String? = null

    var items: List<OtherCheckListItems>? = null
    var remarks: String? = null
}

class CheckListForm {
    var inspection: String? = null
    var clearingAgent: String? = null
    var overallRemarks: String? = null
    var customsEntryNumber: String? = null
    var engineering: EngineeringChecklist? = null
    var others: OtherCheckList? = null
    var agrochem: AgrochemCheckList? = null
    var vehicle: VehicleInspectionCheckList? = null
    fun generalChecklist(): CdInspectionGeneralEntity {
        val dt = CdInspectionGeneralEntity()
        dt.inspection = inspection
        dt.customsEntryNumber = customsEntryNumber
        dt.clearingAgent = clearingAgent
        dt.overallRemarks = overallRemarks
        return dt
    }

    fun engineeringChecklistItems(): List<CdInspectionEngineeringItemChecklistEntity> {
        val listItems = mutableListOf<CdInspectionEngineeringItemChecklistEntity>()
        this.engineering?.items?.forEach { item ->
            val dt = CdInspectionEngineeringItemChecklistEntity()
            dt.itemId = item.itemId
            dt.compliant = item.compliant
            dt.category = item.category
            dt.sampled = item.sampled
            dt.serialNumber = "EG${item.itemId}${Random(12).nextULong()}"
            dt.quantityVerified = item.quantityVerified
            dt.warrantyPeriodDocumentation = item.warrantyPeriodDocumentation
            dt.safetyCautionaryRemarks = item.safetyCautionaryRemarks
            dt.sizeClassCapacity = item.sizeClassCapacity
            dt.certMarksPvocDoc = item.certMarksPvocDoc
            dt.disposalInstruction = item.disposalInstruction
            dt.sampled = item.sampled
            dt.compliant = item.compliant
            dt.category = item.category
            dt.remarks = item.remarks
            dt.sizeClassCapacity = item.sizeClassCapacity
            dt.instructionsUseManual = item.instructionsUseManual
            dt.fiberComposition = item.fiberComposition
            dt.batchNoModelTypeRef = item.batchNoModelTypeRef
            dt.mfgNameAddress = item.mfgNameAddress
            dt.ksEasApplicable = item.ksEasApplicable
            dt.quantityVerified = item.quantityVerified
            dt.brand = item.brand
            dt.status = 1
            listItems.add(dt)
        }
        return listItems
    }

    fun engineeringChecklist(): CdInspectionEngineeringChecklist {
        val dt = CdInspectionEngineeringChecklist()
        dt.itemCount = engineering?.items?.size ?: 0
        dt.remarks = engineering?.remarks
        dt.status = 1
        return dt
    }

    fun agrochemChecklistItems(): List<CdInspectionAgrochemItemChecklistEntity> {
        val listItems = mutableListOf<CdInspectionAgrochemItemChecklistEntity>()
        agrochem?.items?.forEach { item ->
            val dt = CdInspectionAgrochemItemChecklistEntity()
            dt.itemId = item.itemId
            dt.compliant = item.compliant
            dt.category = item.category
            dt.sampled = item.sampled
            dt.appearance = item.appearance
            dt.serialNumber = "AC${item.itemId}${Random(12).nextULong()}"
            dt.brand = item.brand
            dt.certMarksPvocDoc = item.certMarksPvocDoc
            dt.ksEasApplicable = item.ksEasApplicable
            dt.quantityVerified = item.quantityVerified
            dt.dateMfgPackaging = Date.valueOf(item.dateMfgPackaging)
            dt.dateExpiry = Date.valueOf(item.dateExpiry)
            dt.mfgName = item.mfgName
            dt.mfgAddress = item.mfgAddress
            dt.compositionIngredients = item.compositionIngredients
            dt.storageCondition = item.storageCondition
            dt.remarks = agrochem?.remarks
            dt.status = 1
            listItems.add(dt)
        }
        return listItems
    }

    fun agrochemChecklist(): CdInspectionAgrochemChecklist {
        val dt = CdInspectionAgrochemChecklist()
        dt.itemCount = agrochem?.items?.size
        dt.remarks = agrochem?.remarks
        dt.status = 1
        return dt
    }

    fun otherChecklistItems(): List<CdInspectionOtherItemChecklistEntity> {
        val listItems = mutableListOf<CdInspectionOtherItemChecklistEntity>()
        others?.items?.forEach { item ->
            val dt = CdInspectionOtherItemChecklistEntity()
            dt.itemId = item.itemId
            dt.compliant = item.compliant
            dt.serialNumber = "OT${item.itemId}${Random(12).nextULong()}"
            dt.category = item.category
            dt.sampled = item.sampled
            dt.serialNumber = item.serialNumber
            dt.brand = item.brand
            dt.ksEasApplicable = item.ksEasApplicable
            dt.quantityVerified = item.quantityVerified
            dt.packagingLabelling = item.packagingLabelling
            dt.physicalCondition = item.physicalCondition
            dt.defects = item.defects
            dt.presenceAbsenceBanned = item.presenceAbsenceBanned
            dt.documentation = item.documentation
            dt.remarks = others?.remarks
            dt.status = 1
            listItems.add(dt)
        }
        return listItems
    }

    fun otherChecklist(): CdInspectionOtherChecklist {
        val dt = CdInspectionOtherChecklist()
        dt.itemCount = others?.items?.size
        dt.remarks = others?.remarks
        dt.status = 1
        return dt
    }

    fun vehicleChecklistItems(): List<CdInspectionMotorVehicleItemChecklistEntity> {
        val listItems = mutableListOf<CdInspectionMotorVehicleItemChecklistEntity>()
        vehicle?.items?.forEach { item ->
            val dt = CdInspectionMotorVehicleItemChecklistEntity()
            dt.itemId = item.itemId
            dt.stationId = item.stationId
            dt.compliant = item.compliant
            dt.category = item.category
            dt.serialNumber = "MV${item.itemId}${Random(12).nextULong()}"
            dt.sampled = item.submitToMinistry
            dt.makeVehicle = item.makeVehicle
            dt.chassisNo = item.chassisNo
            dt.engineNoCapacity = item.engineNoCapacity
            dt.manufactureDate = Date.valueOf(item.manufacturerDate)
            dt.registrationDate = Date.valueOf(item.registrationDate)
            dt.odemetreReading = item.odemetreReading
            dt.driveRhdLhd = item.driveRhdLhd
            dt.transmissionAutoManual = item.transmissionAutoManual
            dt.colour = item.colour
            dt.overallAppearance = item.overallAppearance
            dt.remarks = item.remarks
            dt.status = 1
            listItems.add(dt)
        }
        return listItems
    }

    fun vehicleChecklist(): CdInspectionMotorVehicleChecklist {
        val dt = CdInspectionMotorVehicleChecklist()
        dt.itemCount = vehicle?.items?.size
        dt.remarks = vehicle?.remarks
        dt.status = 1
        return dt
    }
}

class SsfForm {
    var permitNumber: Long? = null
    var description: String? = null
    var ssfSubmissionDate: String? = null
    var brandName: String? = null
    var productDescription: String? = null
    fun ssf(): QaSampleSubmissionEntity {
        val df = QaSampleSubmissionEntity()
        df.description = description
        df.permitId = permitNumber
        df.brandName = brandName
        df.ssfSubmissionDate = Date(java.util.Date().time)
        df.productDescription = productDescription
        return df
    }
}

class SsfResultForm {
    var remarks: String? = null
    var ssfNo: String? = null
    var compliance: String? = null
    var bsNumber: String? = null
}

class ScfForm {
    var itemId: Long = 0
    var manufacturerTraderName: String? = null
    var manufacturerTraderAddress: String? = null
    var productName: String? = null
    var brandName: String? = null
    var batchNumber: String? = null
    var batchSize: String? = null
    var sampleSize: String? = null
    var samplingMethod: String? = null
    var reasonsForCollectingSamples: String? = null
    var remarks: String? = null
    var witnessName: String? = null
    var witnessDesignation: String? = null
    var dateSignedByWitness: String? = null
    fun scf(): QaSampleCollectionEntity {
        val df = QaSampleCollectionEntity()
        df.anyRemarks = remarks
        df.nameOfManufacture = manufacturerTraderName
        df.addressOfManufacture = manufacturerTraderAddress
        df.brandName = brandName
        df.batchNo = batchNumber
        df.batchSize = batchSize
        df.sampleSize = sampleSize
        df.samplingMethod = samplingMethod
        df.reasonForCollectingSample = reasonsForCollectingSamples
        df.officerDate = Date(java.util.Date().time)
        df.nameOfWitness = witnessName
        df.witnessDesignation = witnessDesignation
        df.witnessDesignation = Date(java.util.Date().time).toString()
        return df
    }
}