package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "CFG_KEBS_PROCESSES_STAGES")
class ProcessesStagesEntity  : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "CFG_KEBS_PROCESSES_STAGES_SEQ_GEN", sequenceName = "CFG_KEBS_PROCESSES_STAGES_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "CFG_KEBS_PROCESSES_STAGES_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long = 0

    @Column(name = "SERVICE_MAP_ID")
    @Basic
    var serviceMapId: Int? = null

    @Column(name = "PROCESS_1")
    @Basic
    var process1: String? = null

    @Column(name = "PROCESS_2")
    @Basic
    var process2: String? = null

    @Column(name = "PROCESS_3")
    @Basic
    var process3: String? = null

    @Column(name = "PROCESS_4")
    @Basic
    var process4: String? = null

    @Column(name = "PROCESS_5")
    @Basic
    var process5: String? = null

    @Column(name = "PROCESS_6")
    @Basic
    var process6: String? = null

    @Column(name = "PROCESS_7")
    @Basic
    var process7: String? = null

    @Column(name = "PROCESS_8")
    @Basic
    var process8: String? = null

    @Column(name = "PROCESS_9")
    @Basic
    var process9: String? = null

    @Column(name = "PROCESS_10")
    @Basic
    var process10: String? = null

    @Column(name = "PROCESS_11")
    @Basic
    var process11: String? = null

    @Column(name = "PROCESS_12")
    @Basic
    var process12: String? = null

    @Column(name = "PROCESS_13")
    @Basic
    var process13: String? = null

    @Column(name = "PROCESS_14")
    @Basic
    var process14: String? = null

    @Column(name = "PROCESS_15")
    @Basic
    var process15: String? = null

    @Column(name = "PROCESS_16")
    @Basic
    var process16: String? = null

    @Column(name = "PROCESS_17")
    @Basic
    var process17: String? = null

    @Column(name = "PROCESS_18")
    @Basic
    var process18: String? = null

    @Column(name = "PROCESS_19")
    @Basic
    var process19: String? = null

    @Column(name = "PROCESS_20")
    @Basic
    var process20: String? = null

    @Column(name = "PROCESS_21")
    @Basic
    var process21: String? = null

    @Column(name = "PROCESS_22")
    @Basic
    var process22: String? = null

    @Column(name = "PROCESS_23")
    @Basic
    var process23: String? = null

    @Column(name = "PROCESS_24")
    @Basic
    var process24: String? = null

    @Column(name = "PROCESS_25")
    @Basic
    var process25: String? = null

    @Column(name = "PROCESS_26")
    @Basic
    var process26: String? = null

    @Column(name = "PROCESS_27")
    @Basic
    var process27: String? = null

    @Column(name = "PROCESS_28")
    @Basic
    var process28: String? = null

    @Column(name = "PROCESS_29")
    @Basic
    var process29: String? = null

    @Column(name = "PROCESS_30")
    @Basic
    var process30: String? = null

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
    
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as  ProcessesStagesEntity
        return id == that.id &&
                serviceMapId == that.serviceMapId &&
                process1 == that.process1 &&
                process2 == that.process2 &&
                process3 == that.process3 &&
                process4 == that.process4 &&
                process5 == that.process5 &&
                process6 == that.process6 &&
                process7 == that.process7 &&
                process8 == that.process8 &&
                process9 == that.process9 &&
                process10 == that.process10 &&
                process11 == that.process11 &&
                process12 == that.process12 &&
                process13 == that.process13 &&
                process14 == that.process14 &&
                process15 == that.process15 &&
                process16 == that.process16 &&
                process17 == that.process17 &&
                process18 == that.process18 &&
                process19 == that.process19 &&
                process20 == that.process20 &&
                process21 == that.process21 &&
                process22 == that.process22 &&
                process23 == that.process23 &&
                process24 == that.process24 &&
                process25 == that.process25 &&
                process26 == that.process26 &&
                process27 == that.process27 &&
                process28 == that.process28 &&
                process29 == that.process29 &&
                process30 == that.process30 &&
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
        return Objects.hash(id, serviceMapId, process1, process2, process3, process4, process5, process6, process7, process8, process9, process10, process11, process12, process13, process14, process15, process16, process17, process18, process19, process20, process21, process22, process23, process24, process25, process26, process27, process28, process29, process30, description, status, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn)
    }
}