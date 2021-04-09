package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_CD_INSPECTION_CHECKLIST")
class CdInspectionChecklistEntity: Serializable {
    @Column(name = "ID")
    @Id
    @SequenceGenerator(name = "DAT_KEBS_CD_INSPECTION_CHECKLIST_SEQ_GEN", allocationSize = 1, sequenceName = "DAT_KEBS_CD_INSPECTION_CHECKLIST_SEQ")
    @GeneratedValue(generator = "DAT_KEBS_CD_INSPECTION_CHECKLIST_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long? = 0

    @Column(name = "INSPECTION")
    @Basic
    var inspection: String? = null

    @Column(name = "CATEGORY")
    @Basic
    var category: String? = null

    @Column(name = "SAVE_REASON")
    @Basic
    var saveReason: String? = null

    @Column(name = "ENTRY_POINT")
    @Basic
    var entryPoint: String? = null

    @Column(name = "CFS")
    @Basic
    var cfs: String? = null

    @Column(name = "INSPECTION_DATE")
    @Basic
    var inspectionDate: java.sql.Date? = null

    @Column(name = "IMPORTERS_NAME")
    @Basic
    var importersName: String? = null

    @Column(name = "CLEARING_AGENT")
    @Basic
    var clearingAgent: String? = null

    @Column(name = "CUSTOMS_ENTRY_NUMBER")
    @Basic
    var customsEntryNumber: String? = null

    @Column(name = "IDF_NUMBER")
    @Basic
    var idfNumber: String? = null

    @Column(name = "UCR_NUMBER")
    @Basic
    var ucrNumber: String? = null

    @Column(name = "COC_NUMBER")
    @Basic
    var cocNumber: String? = null

    @Column(name = "FEE_PAID")
    @Basic
    var feePaid: String? = null

    @Column(name = "RECEIPT_NUMBER")
    @Basic
    var receiptNumber: String? = null

    @Column(name = "ITEMS_NAME")
    @Basic
    var itemsName: String? = null

    @Column(name = "SERIAL_NUMBER")
    @Basic
    var serialNumber: String? = null

    @Column(name = "PRODUCT_DESCRIPTION")
    @Basic
    var productDescription: String? = null

    @Column(name = "PRODUCT_DESCRIPTION_REMARKS")
    @Basic
    var productDescriptionRemarks: String? = null

    @Column(name = "BRAND")
    @Basic
    var brand: String? = null

    @Column(name = "KS_EAS_APPLICABLE")
    @Basic
    var ksEasApplicable: String? = null

    @Column(name = "QUANTITY_DECLARED")
    @Basic
    var quantityDeclared: String? = null

    @Column(name = "QUANTITY_VERIFIED")
    @Basic
    var quantityVerified: String? = null

    @Column(name = "DATE_MFG_PACKAGING")
    @Basic
    var dateMfgPackaging: String? = null

    @Column(name = "DATE_EXPIRY")
    @Basic
    var dateExpiry: String? = null

    @Column(name = "MFG_NAME")
    @Basic
    var mfgName: String? = null

    @Column(name = "MFG_ADDRESS")
    @Basic
    var mfgAddress: String? = null

    @Column(name = "COMPOSITION_INGREDIENTS")
    @Basic
    var compositionIngredients: String? = null

    @Column(name = "STORAGE_CONDITION")
    @Basic
    var storageCondition: String? = null

    @Column(name = "APPEARANCE")
    @Basic
    var appearance: String? = null

    @Column(name = "CERT_MARKS_PVOC_DOC")
    @Basic
    var certMarksPvocDoc: String? = null

    @Column(name = "SAMPLED")
    @Basic
    var sampled: String? = null

    @Column(name = "BATCH_NO_MODEL_TYPE_REF")
    @Basic
    var batchNoModelTypeRef: String? = null

    @Column(name = "FIBER_COMPOSITION")
    @Basic
    var fiberComposition: String? = null

    @Column(name = "INSTRUCTIONS_USE_MANUAL")
    @Basic
    var instructionsUseManual: String? = null

    @Column(name = "WARRANTY_PERIOD_DOCUMENTAION")
    @Basic
    var warrantyPeriodDocumentaion: String? = null

    @Column(name = "SAFETY_CAUTIONARY_REMARKS")
    @Basic
    var safetyCautionaryRemarks: String? = null

    @Column(name = "DISPOSAL_INSTRUCTION")
    @Basic
    var disposalInstruction: String? = null

    @Column(name = "SIZE_CLASS_CAPACITY")
    @Basic
    var sizeClassCapacity: String? = null

    @Column(name = "MAKE_VEHICLE")
    @Basic
    var makeVehicle: String? = null

    @Column(name = "CHASIS_NO")
    @Basic
    var chasisNo: String? = null

    @Column(name = "ENGINE_NO_CAPACITY")
    @Basic
    var engineNoCapacity: String? = null

    @Column(name = "MANUFACTURE_DATE")
    @Basic
    var manufactureDate: java.sql.Date? = null

    @Column(name = "REGISTARTION_DATE")
    @Basic
    var registartionDate: java.sql.Date? = null

    @Column(name = "ODEMETRE_READING")
    @Basic
    var odemetreReading: String? = null

    @Column(name = "DRIVE_RHD_LHD")
    @Basic
    var driveRhdLhd: String? = null

    @Column(name = "TRANSMISION_AUTO_MANUAL")
    @Basic
    var transmisionAutoManual: String? = null

    @Column(name = "COLOUR")
    @Basic
    var colour: String? = null

    @Column(name = "OVERAL_APPEARANCE")
    @Basic
    var overalAppearance: String? = null

    @Column(name = "REMARKS")
    @Basic
    var remarks: String? = null

    @Column(name = "PACKAGING_LABELLIG")
    @Basic
    var packagingLabellig: String? = null

    @Column(name = "PHYSICAL_CONDITION")
    @Basic
    var physicalCondition: String? = null

    @Column(name = "DEFECTS")
    @Basic
    var defects: String? = null

    @Column(name = "PRESENCE_ABSENCE_BANNED")
    @Basic
    var presenceAbsenceBanned: String? = null

    @Column(name = "DOCUMENTATION")
    @Basic
    var documentation: String? = null

    @Column(name = "ITEM_TYPE")
    @Basic
    var itemType: String? = null

    @Column(name = "CD_ID_NUMBER")
    @Basic
    var cdIdNumber: Long? = null

    @Column(name = "OVERAL_REMARKS")
    @Basic
    var overalRemarks: String? = null

    @Column(name = "KEBS_OFFICER")
    @Basic
    var kebsOfficer: String? = null

    @Column(name = "OFFICER_DATE")
    @Basic
    var officerDate: Date? = null
//    private var cSName: String? = null
//    private var cSDate: Time? = null
//    private var oICName: String? = null
//    private var oICDate: Time? = null

    @Column(name = "STATUS")
    @Basic
    var status: Int? = null

    @Column(name = "VAR_FIELD_1")
    @Basic
    var varField1: String? = null

    @Column(name = "VAR_FIELD_2")
    @Basic
    var varField2: String? = null

    @Column(name = "VAR_FIELD_3")
    @Basic
    var varField3: String? = null

    @Column(name = "VAR_FIELD_4")
    @Basic
    var varField4: String? = null

    @Column(name = "VAR_FIELD_5")
    @Basic
    var varField5: String? = null

    @Column(name = "VAR_FIELD_6")
    @Basic
    var varField6: String? = null

    @Column(name = "VAR_FIELD_7")
    @Basic
    var varField7: String? = null

    @Column(name = "VAR_FIELD_8")
    @Basic
    var varField8: String? = null

    @Column(name = "VAR_FIELD_9")
    @Basic
    var varField9: String? = null

    @Column(name = "VAR_FIELD_10")
    @Basic
    var varField10: String? = null

    @Column(name = "CREATED_BY")
    @Basic
    var createdBy: String? = null

    @Column(name = "CREATED_ON")
    @Basic
    var createdOn: Timestamp? = null

    @Column(name = "MODIFIED_BY")
    @Basic
    var modifiedBy: String? = null

    @Column(name = "MODIFIED_ON")
    @Basic
    var modifiedOn: Timestamp? = null

    @Column(name = "DELETE_BY")
    @Basic
    var deleteBy: String? = null

    @Column(name = "DELETED_ON")
    @Basic
    var deletedOn: Timestamp? = null

    @Column(name = "ITEM_ID")
    @Basic
    var itemId: Long? = null

    @Column(name = "ITEM_NUMBER")
    @Basic
    var itemNumber: Long? = null

    @Column(name = "PRODUCT_STATUS")
    @Basic
    var productStatus: Int? = null

    @Column(name = "BRAND_STATUS")
    @Basic
    var brandStatus: Int? = null

    @Column(name = "QUANTITY_DECLARED_STATUS")
    @Basic
    var quantityDeclaredStatus: Int? = null

    @Column(name = "KS_EAS_APPLICABLE_STATUS")
    @Basic
    var ksEasApplicableStatus: Int? = null

    @Column(name = "QUANTITY_VERIFIED_STATUS")
    @Basic
    var quantityVerifiedStatus: Int? = null

    @Column(name = "DATE_MFG_PACKAGING_STATUS")
    @Basic
    var dateMfgPackagingStatus: Int? = null

    @Column(name = "DATE_EXPIRY_STATUS")
    @Basic
    var dateExpiryStatus: Int? = null

    @Column(name = "SERIAL_NUMBER_STATUS")
    @Basic
    var serialNumberStatus: Int? = null

    @Column(name = "MFG_NAME_STATUS")
    @Basic
    var mfgNameStatus: Int? = null

    @Column(name = "COMPOSITION_INGREDIENTS_STATUS")
    @Basic
    var compositionIngredientsStatus: Int? = null

    @Column(name = "STORAGE_CONDITION_STATUS")
    @Basic
    var storageConditionStatus: Int? = null

    @Column(name = "APPEARANCE_STATUS")
    @Basic
    var appearanceStatus: Int? = null

    @Column(name = "QUANTITY_DECLARED_REMARKS")
    @Basic
    var quantityDeclaredRemarks: String? = null

    @Column(name = "COMPLIANT_STATUS")
    @Basic
    var compliantStatus: Int? = null

    @Column(name = "CERT_MARKS_PVOC_DOC_STATUS")
    @Basic
    var certMarksPvocDocStatus: Int? = null

    @Column(name = "C_S_NAME")
    @Basic
    var csName: String? = null


    @Column(name = "C_S_DATE")
    @Basic
    var csDate: Date? = null

    @Column(name = "O_I_C_NAME")
    @Basic
    var oicName: String? = null

    @Column(name = "O_I_C_DATE")
    @Basic
    var oicDate: Date? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as CdInspectionChecklistEntity
        return id == that.id &&
                itemType  == that.itemType &&
                cdIdNumber  == that.cdIdNumber &&
                productDescriptionRemarks == that.productDescriptionRemarks &&
                quantityDeclaredRemarks == that.quantityDeclaredRemarks &&
                inspection == that.inspection &&
                compliantStatus == that.compliantStatus &&
                category == that.category &&
                entryPoint == that.entryPoint &&
                cfs == that.cfs &&
                saveReason == that.saveReason &&
                serialNumberStatus == that.serialNumberStatus &&
                inspectionDate == that.inspectionDate &&
                importersName == that.importersName &&
                clearingAgent == that.clearingAgent &&
                customsEntryNumber == that.customsEntryNumber &&
                idfNumber == that.idfNumber &&
                ucrNumber == that.ucrNumber &&
                cocNumber == that.cocNumber &&
                feePaid == that.feePaid &&
                receiptNumber == that.receiptNumber &&
                itemsName == that.itemsName &&
                serialNumber == that.serialNumber &&
                productDescription == that.productDescription &&
                brand == that.brand &&
                ksEasApplicable == that.ksEasApplicable &&
                quantityDeclared == that.quantityDeclared &&
                quantityVerified == that.quantityVerified &&
                dateMfgPackaging == that.dateMfgPackaging &&
                dateExpiry == that.dateExpiry &&
                mfgName == that.mfgName &&
                mfgAddress == that.mfgAddress &&
                compositionIngredients == that.compositionIngredients &&
                storageCondition == that.storageCondition &&
                appearance == that.appearance &&
                certMarksPvocDoc == that.certMarksPvocDoc &&
                sampled == that.sampled &&
                batchNoModelTypeRef == that.batchNoModelTypeRef &&
                fiberComposition == that.fiberComposition &&
                instructionsUseManual == that.instructionsUseManual &&
                warrantyPeriodDocumentaion == that.warrantyPeriodDocumentaion &&
                safetyCautionaryRemarks == that.safetyCautionaryRemarks &&
                disposalInstruction == that.disposalInstruction &&
                sizeClassCapacity == that.sizeClassCapacity &&
                makeVehicle == that.makeVehicle &&
                chasisNo == that.chasisNo &&
                engineNoCapacity == that.engineNoCapacity &&
                manufactureDate == that.manufactureDate &&
                registartionDate == that.registartionDate &&
                odemetreReading == that.odemetreReading &&
                driveRhdLhd == that.driveRhdLhd &&
                transmisionAutoManual == that.transmisionAutoManual &&
                colour == that.colour &&
                overalAppearance == that.overalAppearance &&
                remarks == that.remarks &&
                packagingLabellig == that.packagingLabellig &&
                physicalCondition == that.physicalCondition &&
                defects == that.defects &&
                presenceAbsenceBanned == that.presenceAbsenceBanned &&
                documentation == that.documentation &&
                overalRemarks == that.overalRemarks &&
                kebsOfficer == that.kebsOfficer &&
                officerDate == that.officerDate &&
                csName == that.csName &&
                csDate == that.csDate &&
                oicName == that.oicName &&
                oicDate == that.oicDate &&
                status == that.status &&
                varField1 == that.varField1 &&
                varField2 == that.varField2 &&
                varField3 == that.varField3 &&
                varField4 == that.varField4 &&
                varField5 == that.varField5 &&
                varField6 == that.varField6 &&
                varField7 == that.varField7 &&
                varField8 == that.varField8 &&
                varField9 == that.varField9 &&
                varField10 == that.varField10 &&
                createdBy == that.createdBy &&
                createdOn == that.createdOn &&
                modifiedBy == that.modifiedBy &&
                modifiedOn == that.modifiedOn &&
                deleteBy == that.deleteBy &&
                deletedOn == that.deletedOn &&
                itemId == that.itemId &&
                itemNumber == that.itemNumber &&
                productStatus == that.productStatus &&
                brandStatus == that.brandStatus &&
                quantityDeclaredStatus == that.quantityDeclaredStatus &&
                ksEasApplicableStatus == that.ksEasApplicableStatus &&
                quantityVerifiedStatus == that.quantityVerifiedStatus &&
                dateMfgPackagingStatus == that.dateMfgPackagingStatus &&
                dateExpiryStatus == that.dateExpiryStatus &&
                mfgNameStatus == that.mfgNameStatus &&
                compositionIngredientsStatus == that.compositionIngredientsStatus &&
                storageConditionStatus == that.storageConditionStatus &&
                appearanceStatus == that.appearanceStatus &&
                certMarksPvocDocStatus == that.certMarksPvocDocStatus
    }

    override fun hashCode(): Int {
        return Objects.hash(id,itemType, saveReason, cdIdNumber, compliantStatus, quantityDeclaredRemarks, productDescriptionRemarks, inspection, category, serialNumberStatus, entryPoint, cfs, inspectionDate, importersName, clearingAgent, customsEntryNumber, idfNumber, ucrNumber, cocNumber, feePaid, receiptNumber, itemsName, serialNumber, productDescription, brand, ksEasApplicable, quantityDeclared, quantityVerified, dateMfgPackaging, dateExpiry, mfgName, mfgAddress, compositionIngredients, storageCondition, appearance, certMarksPvocDoc, sampled, batchNoModelTypeRef, fiberComposition, instructionsUseManual, warrantyPeriodDocumentaion, safetyCautionaryRemarks, disposalInstruction, sizeClassCapacity, makeVehicle, chasisNo, engineNoCapacity, manufactureDate, registartionDate, odemetreReading, driveRhdLhd, transmisionAutoManual, colour, overalAppearance, remarks, packagingLabellig, physicalCondition, defects, presenceAbsenceBanned, documentation, overalRemarks, kebsOfficer, officerDate, csName, csDate, oicName, oicDate, status, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn, itemId, itemNumber, productStatus, brandStatus, quantityDeclaredStatus, ksEasApplicableStatus, quantityVerifiedStatus, dateMfgPackagingStatus, dateExpiryStatus, mfgNameStatus, compositionIngredientsStatus, storageConditionStatus, appearanceStatus, certMarksPvocDocStatus)
    }
}