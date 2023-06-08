package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Time
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_CD_INSPECTION_CHECKLIST")
class CdInspectionChecklistEntity_1 : Serializable{
    @Column(name = "ID", nullable = false, precision = 0)
    @SequenceGenerator(name = "DAT_KEBS_CD_INSPECTION_CHECKLIST_SEQ_GEN", allocationSize = 1, sequenceName = "DAT_KEBS_CD_INSPECTION_CHECKLIST_SEQ")
    @GeneratedValue(generator = "DAT_KEBS_CD_INSPECTION_CHECKLIST_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = 0

    @Column(name = "INSPECTION", nullable = true, length = 200)
    @Basic
    var inspection: String? = null

    @Column(name = "CATEGORY", nullable = true, length = 200)
    @Basic
    var category: String? = null

    @Column(name = "ENTRY_POINT", nullable = true, length = 200)
    @Basic
    var entryPoint: String? = null

    @Column(name = "CFS", nullable = true, length = 200)
    @Basic
    var cfs: String? = null

    @Column(name = "INSPECTION_DATE", nullable = true)
    @Basic
    var inspectionDate: Date? = null

    @Column(name = "IMPORTERS_NAME", nullable = true, length = 200)
    @Basic
    var importersName: String? = null

    @Column(name = "CLEARING_AGENT", nullable = true, length = 200)
    @Basic
    var clearingAgent: String? = null

    @Column(name = "CUSTOMS_ENTRY_NUMBER", nullable = true, length = 200)
    @Basic
    var customsEntryNumber: String? = null

    @Column(name = "IDF_NUMBER", nullable = true, length = 200)
    @Basic
    var idfNumber: String? = null

    @Column(name = "UCR_NUMBER", nullable = true, length = 200)
    @Basic
    var ucrNumber: String? = null

    @Column(name = "COC_NUMBER", nullable = true, length = 200)
    @Basic
    var cocNumber: String? = null

    @Column(name = "FEE_PAID", nullable = true, length = 200)
    @Basic
    var feePaid: String? = null

    @Column(name = "RECEIPT_NUMBER", nullable = true, length = 200)
    @Basic
    var receiptNumber: String? = null

    @Column(name = "ITEMS_NAME", nullable = true, length = 200)
    @Basic
    var itemsName: String? = null

    @Column(name = "SERIAL_NUMBER", nullable = true, length = 200)
    @Basic
    var serialNumber: String? = null

    @Column(name = "PRODUCT_DESCRIPTION", nullable = true, length = 200)
    @Basic
    var productDescription: String? = null

    @Column(name = "BRAND", nullable = true, length = 200)
    @Basic
    var brand: String? = null

    @Column(name = "KS_EAS_APPLICABLE", nullable = true, length = 200)
    @Basic
    var ksEasApplicable: String? = null

    @Column(name = "QUANTITY_DECLARED", nullable = true, length = 200)
    @Basic
    var quantityDeclared: String? = null

    @Column(name = "QUANTITY_VERIFIED", nullable = true, length = 200)
    @Basic
    var quantityVerified: String? = null

    @Column(name = "DATE_MFG_PACKAGING", nullable = true)
    @Basic
    var dateMfgPackaging: Date? = null

    @Column(name = "DATE_EXPIRY", nullable = true)
    @Basic
    var dateExpiry: Date? = null

    @Column(name = "MFG_NAME", nullable = true, length = 200)
    @Basic
    var mfgName: String? = null

    @Column(name = "MFG_ADDRESS", nullable = true, length = 200)
    @Basic
    var mfgAddress: String? = null

    @Column(name = "COMPOSITION_INGREDIENTS", nullable = true, length = 3800)
    @Basic
    var compositionIngredients: String? = null

    @Column(name = "STORAGE_CONDITION", nullable = true, length = 200)
    @Basic
    var storageCondition: String? = null

    @Column(name = "APPEARANCE", nullable = true, length = 200)
    @Basic
    var appearance: String? = null

    @Column(name = "CERT_MARKS_PVOC_DOC", nullable = true, length = 200)
    @Basic
    var certMarksPvocDoc: String? = null

    @Column(name = "SAMPLED", nullable = true, length = 200)
    @Basic
    var sampled: String? = null

    @Column(name = "BATCH_NO_MODEL_TYPE_REF", nullable = true, length = 200)
    @Basic
    var batchNoModelTypeRef: String? = null

    @Column(name = "FIBER_COMPOSITION", nullable = true, length = 200)
    @Basic
    var fiberComposition: String? = null

    @Column(name = "INSTRUCTIONS_USE_MANUAL", nullable = true, length = 200)
    @Basic
    var instructionsUseManual: String? = null

    @Column(name = "WARRANTY_PERIOD_DOCUMENTAION", nullable = true, length = 200)
    @Basic
    var warrantyPeriodDocumentaion: String? = null

    @Column(name = "SAFETY_CAUTIONARY_REMARKS", nullable = true, length = 200)
    @Basic
    var safetyCautionaryRemarks: String? = null

    @Column(name = "DISPOSAL_INSTRUCTION", nullable = true, length = 200)
    @Basic
    var disposalInstruction: String? = null

    @Column(name = "SIZE_CLASS_CAPACITY", nullable = true, length = 200)
    @Basic
    var sizeClassCapacity: String? = null

    @Column(name = "MAKE_VEHICLE", nullable = true, length = 200)
    @Basic
    var makeVehicle: String? = null

    @Column(name = "CHASIS_NO", nullable = true, length = 200)
    @Basic
    var chasisNo: String? = null

    @Column(name = "ENGINE_NO_CAPACITY", nullable = true, length = 200)
    @Basic
    var engineNoCapacity: String? = null

    @Column(name = "MANUFACTURE_DATE", nullable = true)
    @Basic
    var manufactureDate: Date? = null

    @Column(name = "REGISTARTION_DATE", nullable = true)
    @Basic
    var registartionDate: Time? = null

    @Column(name = "ODEMETRE_READING", nullable = true, length = 200)
    @Basic
    var odemetreReading: String? = null

    @Column(name = "DRIVE_RHD_LHD", nullable = true, length = 200)
    @Basic
    var driveRhdLhd: String? = null

    @Column(name = "TRANSMISION_AUTO_MANUAL", nullable = true, length = 200)
    @Basic
    var transmisionAutoManual: String? = null

    @Column(name = "COLOUR", nullable = true, length = 200)
    @Basic
    var colour: String? = null

    @Column(name = "OVERAL_APPEARANCE", nullable = true, length = 200)
    @Basic
    var overalAppearance: String? = null

    @Column(name = "REMARKS", nullable = true, length = 200)
    @Basic
    var remarks: String? = null

    @Column(name = "PACKAGING_LABELLIG", nullable = true, length = 200)
    @Basic
    var packagingLabellig: String? = null

    @Column(name = "PHYSICAL_CONDITION", nullable = true, length = 200)
    @Basic
    var physicalCondition: String? = null

    @Column(name = "DEFECTS", nullable = true, length = 200)
    @Basic
    var defects: String? = null

    @Column(name = "PRESENCE_ABSENCE_BANNED", nullable = true, length = 200)
    @Basic
    var presenceAbsenceBanned: String? = null

    @Column(name = "DOCUMENTATION", nullable = true, length = 200)
    @Basic
    var documentation: String? = null

    @Column(name = "OVERAL_REMARKS", nullable = true, length = 200)
    @Basic
    var overalRemarks: String? = null

    @Column(name = "KEBS_OFFICER", nullable = true, length = 200)
    @Basic
    var kebsOfficer: String? = null

    @Column(name = "OFFICER_DATE", nullable = true)
    @Basic
    var officerDate: Date? = null

    @Column(name = "STATUS", nullable = true, precision = 0)
    @Basic
    var status: Long? = null

    @Column(name = "VAR_FIELD_1", nullable = true, length = 350)
    @Basic
    var varField1: String? = null

    @Column(name = "VAR_FIELD_2", nullable = true, length = 350)
    @Basic
    var varField2: String? = null

    @Column(name = "VAR_FIELD_3", nullable = true, length = 350)
    @Basic
    var varField3: String? = null

    @Column(name = "VAR_FIELD_4", nullable = true, length = 350)
    @Basic
    var varField4: String? = null

    @Column(name = "VAR_FIELD_5", nullable = true, length = 350)
    @Basic
    var varField5: String? = null

    @Column(name = "VAR_FIELD_6", nullable = true, length = 350)
    @Basic
    var varField6: String? = null

    @Column(name = "VAR_FIELD_7", nullable = true, length = 350)
    @Basic
    var varField7: String? = null

    @Column(name = "VAR_FIELD_8", nullable = true, length = 350)
    @Basic
    var varField8: String? = null

    @Column(name = "VAR_FIELD_9", nullable = true, length = 350)
    @Basic
    var varField9: String? = null

    @Column(name = "VAR_FIELD_10", nullable = true, length = 350)
    @Basic
    var varField10: String? = null

    @Column(name = "CREATED_BY", nullable = false, length = 100)
    @Basic
    var createdBy: String? = null

    @Column(name = "CREATED_ON", nullable = false)
    @Basic
    var createdOn: Timestamp? = null

    @Column(name = "MODIFIED_BY", nullable = true, length = 100)
    @Basic
    var modifiedBy: String? = null

    @Column(name = "MODIFIED_ON", nullable = true)
    @Basic
    var modifiedOn: Timestamp? = null

    @Column(name = "DELETE_BY", nullable = true, length = 100)
    @Basic
    var deleteBy: String? = null

    @Column(name = "DELETED_ON", nullable = true)
    @Basic
    var deletedOn: Timestamp? = null

    @Column(name = "ITEM_ID", nullable = true, length = 200)
    @Basic
    var itemId: Long? = null

    @Column(name = "ITEM_NUMBER")
    @Basic
    var itemNumber: Long? = 0

    @Basic
    @Column(name = "C_S_NAME", nullable = true, length = 200)
    var cSName: String? = null


    @Basic
    @Column(name = "C_S_DATE", nullable = true)
    var cSDate: Date? = null

    @Basic
    @Column(name = "O_I_C_NAME", nullable = true, length = 200)
    var oICName: String? = null

    @Basic
    @Column(name = "O_I_C_DATE", nullable = true)
    var oICDate: Date? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as CdInspectionChecklistEntity_1
        return id == that.id &&
                inspection == that.inspection &&
                category == that.category &&
                entryPoint == that.entryPoint &&
                cfs == that.cfs &&
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
                cSName == that.cSName &&
                cSDate == that.cSDate &&
                oICName == that.oICName &&
                oICDate == that.oICDate &&
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
                deletedOn == that.deletedOn
    }

    override fun hashCode(): Int {
        return Objects.hash(
            id,
            inspection,
            category,
            entryPoint,
            cfs,
            inspectionDate,
            importersName,
            clearingAgent,
            customsEntryNumber,
            idfNumber,
            ucrNumber,
            cocNumber,
            feePaid,
            receiptNumber,
            itemsName,
            serialNumber,
            productDescription,
            brand,
            ksEasApplicable,
            quantityDeclared,
            quantityVerified,
            dateMfgPackaging,
            dateExpiry,
            mfgName,
            mfgAddress,
            compositionIngredients,
            storageCondition,
            appearance,
            certMarksPvocDoc,
            sampled,
            batchNoModelTypeRef,
            fiberComposition,
            instructionsUseManual,
            warrantyPeriodDocumentaion,
            safetyCautionaryRemarks,
            disposalInstruction,
            sizeClassCapacity,
            makeVehicle,
            chasisNo,
            engineNoCapacity,
            manufactureDate,
            registartionDate,
            odemetreReading,
            driveRhdLhd,
            transmisionAutoManual,
            colour,
            overalAppearance,
            remarks,
            packagingLabellig,
            physicalCondition,
            defects,
            presenceAbsenceBanned,
            documentation,
            overalRemarks,
            kebsOfficer,
            officerDate,
            cSName,
            cSDate,
            oICName,
            oICDate,
            status,
            varField1,
            varField2,
            varField3,
            varField4,
            varField5,
            varField6,
            varField7,
            varField8,
            varField9,
            varField10,
            createdBy,
            createdOn,
            modifiedBy,
            modifiedOn,
            deleteBy,
            deletedOn
        )
    }
}