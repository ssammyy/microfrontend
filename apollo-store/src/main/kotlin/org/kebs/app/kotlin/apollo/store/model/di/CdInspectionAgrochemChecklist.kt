package org.kebs.app.kotlin.apollo.store.model.di

import java.io.Serializable
import java.sql.Date
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_CD_INSPECTION_AGROCHEM_CHECKLIST")
class CdInspectionAgrochemChecklist : Serializable {

    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_CD_INSPECTION_AGROCHEM_CHECKLIST_SEQ_GEN", sequenceName = "DAT_KEBS_CD_INSPECTION_AGROCHEM_CHECKLIST_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_CD_INSPECTION_AGROCHEM_CHECKLIST_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = null

    @Column(name = "ITEM_COUNT")
    @Basic
    var itemCount: Int? = null

    @Column(name = "SERIAL_NUMBER")
    @Basic
    var serialNumber: String? = null

    @Column(name = "PRODUCT_DESCRIPTION")
    @Basic
    var productDescription: String? = null

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
    var dateMfgPackaging: Date? = null

    @Column(name = "DATE_EXPIRY")
    @Basic
    var dateExpiry: Date? = null

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

    @Column(name = "REMARKS")
    @Basic
    var remarks: String? = null

    @Column(name = "DESCRIPTION")
    @Basic
    var description: String? = null

    @JoinColumn(name = "INSPECTION_GENERAL_ID", referencedColumnName = "ID")
    @ManyToOne
    var inspectionGeneral: CdInspectionGeneralEntity? = null

    @JoinColumn(name = "INSPECTION_CHECKLIST_ID", referencedColumnName = "ID")
    @ManyToOne
    var inspectionChecklistType: CdChecklistTypesEntity? = null

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as CdInspectionAgrochemChecklist
        return id == that.id &&
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
                remarks == that.remarks &&
                description == that.description &&
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
        return Objects.hash(id, serialNumber, productDescription, brand, ksEasApplicable, quantityDeclared, quantityVerified, dateMfgPackaging, dateExpiry, mfgName, mfgAddress, compositionIngredients, storageCondition, appearance, certMarksPvocDoc, sampled, remarks, description, status, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn)
    }
}