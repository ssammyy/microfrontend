package org.kebs.app.kotlin.apollo.store.model.di

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_CD_INSPECTION_OTHER_ITEM_CHECKLIST")
class CdInspectionOtherItemChecklistEntity : Serializable {

    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_CD_INSPECTION_OTHER_ITEM_CHECKLIST_SEQ_GEN", sequenceName = "DAT_KEBS_CD_INSPECTION_OTHER_ITEM_CHECKLIST_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_CD_INSPECTION_OTHER_ITEM_CHECKLIST_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = null

    @Column(name = "SERIAL_NUMBER")
    @Basic
    var serialNumber: String? = null

    @Column(name = "PRODUCT_DESCRIPTION")
    @Basic
    var productDescription: String? = null

    @Column(name = "BRAND")
    @Basic
    var brand: String? = null

    @Column(name = "SAMPLED")
    @Basic
    var sampled: String? = null

    @Column(name = "KS_EAS_APPLICABLE")
    @Basic
    var ksEasApplicable: String? = null

    @Column(name = "QUANTITY_DECLARED")
    @Basic
    var quantityDeclared: String? = null

    @Column(name = "QUANTITY_VERIFIED")
    @Basic
    var quantityVerified: String? = null

    @Column(name = "PACKAGING_LABELLING")
    @Basic
    var packagingLabelling: String? = null

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

    @Column(name = "REMARKS")
    @Basic
    var remarks: String? = null

    @Column(name = "DESCRIPTION")
    @Basic
    var description: String? = null

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

    @JoinColumn(name = "INSPECTION_GENERAL_ID", referencedColumnName = "ID")
    @ManyToOne
    var inspectionGeneral: CdInspectionGeneralEntity? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as CdInspectionOtherItemChecklistEntity
        return id == that.id &&
                serialNumber == that.serialNumber &&
                productDescription == that.productDescription &&
                brand == that.brand &&
                sampled == that.sampled &&
                ksEasApplicable == that.ksEasApplicable &&
                quantityDeclared == that.quantityDeclared &&
                quantityVerified == that.quantityVerified &&
                packagingLabelling == that.packagingLabelling &&
                physicalCondition == that.physicalCondition &&
                defects == that.defects &&
                presenceAbsenceBanned == that.presenceAbsenceBanned &&
                documentation == that.documentation &&
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
        return Objects.hash(id, sampled, serialNumber, productDescription, brand, ksEasApplicable, quantityDeclared, quantityVerified, packagingLabelling, physicalCondition, defects, presenceAbsenceBanned, documentation, remarks, description, status, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn)
    }
}