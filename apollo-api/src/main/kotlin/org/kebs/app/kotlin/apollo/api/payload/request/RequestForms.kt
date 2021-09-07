package org.kebs.app.kotlin.apollo.api.payload.request

import org.kebs.app.kotlin.apollo.store.model.SampleSubmissionDocumentsEntity
import org.kebs.app.kotlin.apollo.store.model.di.*
import org.kebs.app.kotlin.apollo.store.model.qa.QaSampleSubmissionEntity
import java.sql.Date

class AgrochemCheckList{
    var serialNumber: String?=null
    var brand: String?=null
    var ksEasApplicable: String?=null
    var quantityVerified:String?=null
    var dateMfgPackaging: String?=null
    var dateExpiry: String?=null
    var mfgName: String?=null
    var mfgAddress: String?=null
    var compositionIngredients:String?=null
    var storageCondition: String?=null
    var appearance: String?=null
    var certMarksPvocDoc:String?=null
    var sampled: String?=null
    var remarks:String?=null
}
class EngineeringChecklist{
    var serialNumber: String?=null
    var brand: String?=null
    var ksEasApplicable: String?=null
    var quantityVerified: String?=null
    var mfgNameAddress: String?=null
    var batchNoModelTypeRef: String?=null
    var fiberComposition: String?=null
    var instructionsUseManual:String?=null
    var warrantyPeriodDocumentation: String?=null
    var safetyCautionaryRemarks: String?=null
    var sizeClassCapacity: String?=null
    var certMarksPvocDoc: String?=null
    var disposalInstruction: String?=null
    var sampled: String?=null
    var remarks: String?=null
}
class VehicleInspectionCheckList{
    var serialNumber: String?=null
    var makeVehicle: String?=null
    var chassisNo: String?=null
    var engineNoCapacity: String?=null
    var manufacturerDate: String?=null
    var registrationDate: String?=null
    var odemetreReading: String?=null
    var driveRhdLhd: String?=null
    var transmissionAutoManual: String?=null
    var colour: String?=null
    var overallAppearance: String?=null
    var remarks:String?=null
}
class OtherCheckList{
    var serialNumber: String?=null
    var brand: String?=null
    var ksEasApplicable: String?=null
    var quantityVerified: String?=null
    var packagingLabelling: String?=null
    var physicalCondition: String?=null
    var defects: String?=null
    var presenceAbsenceBanned:String?=null
    var documentation: String?=null
    var sampled: String?=null
    var remarks:String?=null
}
class CheckListForm {
    var category:String?=null
    var inspection:String?=null
    var confirmItemType:String?=null
    var clearingAgent: String?=null
    var overallRemarks: String?=null
    var customsEntryNumber: String?=null
    var engineering: EngineeringChecklist?=null
    var others: OtherCheckList?=null
    var agrochem: AgrochemCheckList?=null
    var vehicle: VehicleInspectionCheckList?=null
    fun generalChecklist():CdInspectionGeneralEntity{
        val dt=CdInspectionGeneralEntity()
        dt.category=category
        dt.inspection=inspection
        dt.customsEntryNumber=customsEntryNumber
        dt.clearingAgent=clearingAgent
        dt.overallRemarks=overallRemarks
        return dt
    }
    fun engineeringChecklist(): CdInspectionEngineeringItemChecklistEntity{
        val dt=CdInspectionEngineeringItemChecklistEntity()
        dt.serialNumber=engineering?.serialNumber
        dt.brand=engineering?.brand
        dt.ksEasApplicable=engineering?.ksEasApplicable
        dt.quantityVerified=engineering?.quantityVerified
        dt.mfgNameAddress=engineering?.mfgNameAddress
        dt.batchNoModelTypeRef=engineering?.batchNoModelTypeRef
        dt.fiberComposition=engineering?.fiberComposition
        dt.instructionsUseManual=engineering?.instructionsUseManual
        dt.warrantyPeriodDocumentation=engineering?.warrantyPeriodDocumentation
        dt.safetyCautionaryRemarks=engineering?.safetyCautionaryRemarks
        dt.sizeClassCapacity=engineering?.sizeClassCapacity
        dt.certMarksPvocDoc=engineering?.certMarksPvocDoc
        dt.disposalInstruction=engineering?.disposalInstruction
        dt.sampled=engineering?.sampled
        dt.remarks=engineering?.remarks
        return dt
    }
    fun agrochemChecklist(): CdInspectionAgrochemItemChecklistEntity{
        val dt=CdInspectionAgrochemItemChecklistEntity()
        dt.appearance=agrochem?.appearance
        dt.brand=agrochem?.brand
        dt.certMarksPvocDoc=agrochem?.certMarksPvocDoc
        dt.ksEasApplicable=agrochem?.ksEasApplicable
        dt.quantityVerified=agrochem?.quantityVerified
        dt.dateMfgPackaging= Date.valueOf(agrochem?.dateMfgPackaging)
        dt.dateExpiry=Date.valueOf(agrochem?.dateExpiry)
        dt.mfgName=agrochem?.mfgName
        dt.mfgAddress=agrochem?.mfgAddress
        dt.compositionIngredients=agrochem?.compositionIngredients
        dt.storageCondition=agrochem?.storageCondition
        dt.sampled=agrochem?.sampled
        dt.remarks=agrochem?.remarks
        return dt
    }
    fun otherChecklist(): CdInspectionOtherItemChecklistEntity {
        val dt=CdInspectionOtherItemChecklistEntity()
        dt.serialNumber=others?.serialNumber
        dt.brand=others?.brand
        dt.ksEasApplicable=others?.ksEasApplicable
        dt.quantityVerified=others?.quantityVerified
        dt.packagingLabelling=others?.packagingLabelling
        dt.physicalCondition=others?.physicalCondition
        dt.defects=others?.defects
        dt.presenceAbsenceBanned=others?.presenceAbsenceBanned
        dt.documentation=others?.documentation
        dt.sampled=others?.sampled
        dt.remarks=others?.remarks
        return dt
    }
    fun vehicleChecklist(): CdInspectionMotorVehicleItemChecklistEntity {
        val dt=CdInspectionMotorVehicleItemChecklistEntity()
        dt.serialNumber=vehicle?.serialNumber
        dt.makeVehicle=vehicle?.makeVehicle
        dt.chassisNo=vehicle?.chassisNo
        dt.engineNoCapacity=vehicle?.engineNoCapacity
        dt.manufactureDate= Date.valueOf(vehicle?.manufacturerDate)
        dt.registrationDate=Date.valueOf(vehicle?.registrationDate)
        dt.odemetreReading=vehicle?.odemetreReading
        dt.driveRhdLhd=vehicle?.driveRhdLhd
        dt.transmissionAutoManual=vehicle?.transmissionAutoManual
        dt.colour=vehicle?.colour
        dt.overallAppearance=vehicle?.overallAppearance
        dt.remarks=vehicle?.remarks
        return dt
    }
}

class SsfForm{
    var description:String?=null
    var ssfNo: String?=null
    var ssfSubmissionDate: String?=null
    var bsNumber: String?=null
    var brandName: String?=null
    var productDescription: String?=null
    fun ssf(): QaSampleSubmissionEntity{
        val df=QaSampleSubmissionEntity()
        df.description=description
        df.ssfNo=ssfNo
        df.brandName=brandName
        df.ssfSubmissionDate= Date.valueOf(ssfSubmissionDate)
        df.bsNumber=bsNumber
        df.productDescription=productDescription
        return df
    }
}