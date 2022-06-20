package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Date
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_MS_SEIZURE_DECLARATION")
class MsSeizureDeclarationEntity : Serializable{
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_MS_SEIZURE_DECLARATION_SEQ_GEN", allocationSize = 1, sequenceName = "DAT_KEBS_MS_SEIZURE_DECLARATION_SEQ")
    @GeneratedValue(generator = "DAT_KEBS_MS_SEIZURE_DECLARATION_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long = 0

    @Column(name = "SEIZURE_TO")
    @Basic
    var seizureTo: String? = null

    @Column(name = "SEIZURE_PREMISES")
    @Basic
    var seizurePremises: String? = null

    @Column(name = "SEIZURE_REQUIREMENTS_STANDARDS")
    @Basic
    var seizureRequirementsStandards: String? = null

    @Column(name = "GOODS_NAME")
    @Basic
    var goodsName: String? = null

    @Column(name = "GOODS_MANUFACTURE_TRADER")
    @Basic
    var goodsManufactureTrader: String? = null

    @Column(name = "GOODS_ADDRESS")
    @Basic
    var goodsAddress: String? = null

    @Column(name = "GOODS_PHYSICAL")
    @Basic
    var goodsPhysical: String? = null

    @Column(name = "GOODS_LOCATION")
    @Basic
    var goodsLocation: String? = null

    @Column(name = "GOODS_MARKED_BRANDED")
    @Basic
    var goodsMarkedBranded: String? = null

    @Column(name = "GOODS_PHYSICAL_SEAL")
    @Basic
    var goodsPhysicalSeal: String? = null

    @Column(name = "DESCRIPTION_GOODS")
    @Basic
    var descriptionGoods: String? = null

    @Column(name = "GOODS_QUANTITY")
    @Basic
    var goodsQuantity: String? = null

    @Column(name = "GOODS_THEREFOREI")
    @Basic
    var goodsThereforei: String? = null

    @Column(name = "NAME_INSPECTOR")
    @Basic
    var nameInspector: String? = null

    @Column(name = "DESIGNATION_INSPECTOR")
    @Basic
    var designationInspector: String? = null

    @Column(name = "DATE_INSPECTOR")
    @Basic
    var dateInspector: Date? = null

    @Column(name = "NAME_MANUFACTURE_TRADER")
    @Basic
    var nameManufactureTrader: String? = null

    @Column(name = "DESIGNATION_MANUFACTURE_TRADER")
    @Basic
    var designationManufactureTrader: String? = null

    @Column(name = "DATE_MANUFACTURE_TRADER")
    @Basic
    var dateManufactureTrader: Date? = null

    @Column(name = "NAME_WITNESS")
    @Basic
    var nameWitness: String? = null

    @Column(name = "DESIGNATION_WITNESS")
    @Basic
    var designationWitness: String? = null

    @Column(name = "DATE_WITNESS")
    @Basic
    var dateWitness: Date? = null

    @Column(name = "DECLARATION_TAKEN_BY")
    @Basic
    var declarationTakenBy: String? = null

    @Column(name = "DECLARATION_ON_THE")
    @Basic
    var declarationOnThe: String? = null

    @Column(name = "DECLARATION_DAY_OF")
    @Basic
    var declarationDayOf: Date? = null

    @Column(name = "DECLARATION_MY_NAME")
    @Basic
    var declarationMyName: String? = null

    @Column(name = "DECLARATION_IRESIDE_AT")
    @Basic
    var declarationIresideAt: String? = null

    @Column(name = "DECLARATION_IEMPLOYEED_AS")
    @Basic
    var declarationIemployeedAs: String? = null

    @Column(name = "DECLARATION_IEMPLOYEED_OF")
    @Basic
    var declarationIemployeedOf: String? = null

    @Column(name = "DECLARATION_SITUATED_AT")
    @Basic
    var declarationSituatedAt: String? = null

    @Column(name = "DECLARATION_STATE_THAT")
    @Basic
    var declarationStateThat: String? = null

    @Column(name = "DECLARATION_ID_NUMBER")
    @Basic
    var declarationIdNumber: String? = null

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

    @JoinColumn(name = "MS_WORKPLAN_GENERATED_ID", referencedColumnName = "ID")
    @ManyToOne
    var workPlanGeneratedID: MsWorkPlanGeneratedEntity? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as MsSeizureDeclarationEntity
        return id == that.id &&
                seizureTo == that.seizureTo &&
                seizurePremises == that.seizurePremises &&
                seizureRequirementsStandards == that.seizureRequirementsStandards &&
                goodsName == that.goodsName &&
                goodsManufactureTrader == that.goodsManufactureTrader &&
                goodsAddress == that.goodsAddress &&
                goodsPhysical == that.goodsPhysical &&
                goodsLocation == that.goodsLocation &&
                goodsMarkedBranded == that.goodsMarkedBranded &&
                goodsPhysicalSeal == that.goodsPhysicalSeal &&
                descriptionGoods == that.descriptionGoods &&
                goodsQuantity == that.goodsQuantity &&
                goodsThereforei == that.goodsThereforei &&
                nameInspector == that.nameInspector &&
                designationInspector == that.designationInspector &&
                dateInspector == that.dateInspector &&
                nameManufactureTrader == that.nameManufactureTrader &&
                designationManufactureTrader == that.designationManufactureTrader &&
                dateManufactureTrader == that.dateManufactureTrader &&
                nameWitness == that.nameWitness &&
                designationWitness == that.designationWitness &&
                dateWitness == that.dateWitness &&
                declarationTakenBy == that.declarationTakenBy &&
                declarationOnThe == that.declarationOnThe &&
                declarationDayOf == that.declarationDayOf &&
                declarationMyName == that.declarationMyName &&
                declarationIresideAt == that.declarationIresideAt &&
                declarationIemployeedAs == that.declarationIemployeedAs &&
                declarationIemployeedOf == that.declarationIemployeedOf &&
                declarationSituatedAt == that.declarationSituatedAt &&
                declarationStateThat == that.declarationStateThat &&
                declarationIdNumber == that.declarationIdNumber &&
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
        return Objects.hash(id, seizureTo, seizurePremises, seizureRequirementsStandards, goodsName, goodsManufactureTrader, goodsAddress, goodsPhysical, goodsLocation, goodsMarkedBranded, goodsPhysicalSeal, descriptionGoods, goodsQuantity, goodsThereforei, nameInspector, designationInspector, dateInspector, nameManufactureTrader, designationManufactureTrader, dateManufactureTrader, nameWitness, designationWitness, dateWitness, declarationTakenBy, declarationOnThe, declarationDayOf, declarationMyName, declarationIresideAt, declarationIemployeedAs, declarationIemployeedOf, declarationSituatedAt, declarationStateThat, declarationIdNumber, status, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn)
    }
}
