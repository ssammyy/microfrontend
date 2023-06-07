package org.kebs.app.kotlin.apollo.store.model

import org.kebs.app.kotlin.apollo.store.model.di.CdChecklistTypesEntity
import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_CD_CHECKLIST")
class CdChecklistEntity: Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_CD_CHECKLIST_SEQ_GEN", allocationSize = 1, sequenceName = "DAT_KEBS_CD_CHECKLIST_SEQ")
    @GeneratedValue(generator = "DAT_KEBS_CD_CHECKLIST_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = 0

    @Column(name = "DESG_CONST_STORAGE_VESSELS")
    @Basic
    var desgConstStorageVessels: String? = null

    @Column(name = "DESG_CONST_BRANCH_MANHOLES")
    @Basic
    var desgConstBranchManholes: String? = null

    @Column(name = "SIZING_SELECT_FITING_LPGAS")
    @Basic
    var sizingSelectFitingLpgas: String? = null

    @Column(name = "ELECTRONIC_BONDING")
    @Basic
    var electronicBonding: String? = null

    @Column(name = "CONS_FILL_WITHRD_EQUALIZ")
    @Basic
    var consFillWithrdEqualiz: String? = null

    @Column(name = "SIZING_SELECT_PRESU_VALVES")
    @Basic
    var sizingSelectPresuValves: String? = null

    @Column(name = "SELE_SUITABLE_STORAGE")
    @Basic
    var seleSuitableStorage: String? = null

    @Column(name = "SELCT_TEMP_INSTRUME_STORAGE")
    @Basic
    var selctTempInstrumeStorage: String? = null

    @Column(name = "SITABLE_PRESSUER_STORAGE")
    @Basic
    var sitablePressuerStorage: String? = null

    @Column(name = "DESIGN_CONS_MOUNT_SUPPORTS")
    @Basic
    var designConsMountSupports: String? = null

    @Column(name = "UCRNUMBER")
    @Basic
    var ucrnumber: String? = null

    @Column(name = "STATUS")
    @Basic
    var status: Long? = null

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

    @Column(name = "CHECKLIST_VALUES")
    @Basic
    var checklistValues: Timestamp? = null

    @JoinColumn(name = "CHECKLIST_TYPE_ID", referencedColumnName = "ID")
    @ManyToOne
    var checkListTypeId: CdChecklistTypesEntity? = null


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as CdChecklistEntity
        return id == that.id &&
                checklistValues == that.checklistValues &&
                desgConstStorageVessels == that.desgConstStorageVessels &&
                desgConstBranchManholes == that.desgConstBranchManholes &&
                sizingSelectFitingLpgas == that.sizingSelectFitingLpgas &&
                electronicBonding == that.electronicBonding &&
                consFillWithrdEqualiz == that.consFillWithrdEqualiz &&
                sizingSelectPresuValves == that.sizingSelectPresuValves &&
                seleSuitableStorage == that.seleSuitableStorage &&
                selctTempInstrumeStorage == that.selctTempInstrumeStorage &&
                sitablePressuerStorage == that.sitablePressuerStorage &&
                designConsMountSupports == that.designConsMountSupports &&
                ucrnumber == that.ucrnumber &&
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
        return Objects.hash(id, desgConstStorageVessels, checklistValues, desgConstBranchManholes, sizingSelectFitingLpgas, electronicBonding, consFillWithrdEqualiz, sizingSelectPresuValves, seleSuitableStorage, selctTempInstrumeStorage, sitablePressuerStorage, designConsMountSupports, ucrnumber, status, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn)
    }
}