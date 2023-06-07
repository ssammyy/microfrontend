package org.kebs.app.kotlin.apollo.store.model.std

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name="SD_COM_STANDARD_JC")
class ComStandardJC  : Serializable {

    @Column(name = "ID")
    @SequenceGenerator(
        name = "SD_COM_STANDARD_JC_SEQ_GEN",
        allocationSize = 1,
        sequenceName = "SD_COM_STANDARD_JC_SEQ"
    )
    @GeneratedValue(generator = "SD_COM_STANDARD_JC_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = 0

    @Column(name="REQUEST_NUMBER")
    @Basic
    var requestNumber:String?=null

    @Column(name="DATE_OF_FORMATION")
    @Basic
    var dateOfFormation: Timestamp?=null

    @Column(name="ID_OF_JC")
    @Basic
    var idOfJc:String?=null

    @Column(name="NAME_OF_JC")
    @Basic
    var nameOfJc:String?=null

    //  @Transient
    @Column(name = "TASK_ID")
    @Basic
    var taskId: String? = null

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

    @Column(name="ASSIGNED_TO")
    @Basic
    var assignedTo:Long?=null

    @Column(name = "PROCESS_ID")
    @Basic
    var processId: String? = null

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
        val that = other as ComStandardJC
        return id == that.id && requestNumber == that.requestNumber && dateOfFormation == that.dateOfFormation && nameOfJc == that.nameOfJc && idOfJc == that.idOfJc   && varField2 == that.varField2 && varField3 == that.varField3 && varField4 == that.varField4 && varField5 == that.varField5 && varField6 == that.varField6 && varField7 == that.varField7 && varField8 == that.varField8 && assignedTo == that.assignedTo && processId == that.processId && createdBy == that.createdBy && createdOn == that.createdOn && modifiedBy == that.modifiedBy && modifiedOn == that.modifiedOn && deleteBy == that.deleteBy && deletedOn == that.deletedOn
    }

    override fun hashCode(): Int {
        var result = Objects.hash(
            id,
            requestNumber,
            dateOfFormation,
            dateOfFormation,
            idOfJc,
            nameOfJc,
            varField2,
            varField3,
            varField4,
            varField5,
            varField6,
            varField7,
            varField8,
            assignedTo,
            processId,
            createdBy,
            createdOn,
            modifiedBy,
            modifiedOn,
            deleteBy,
            deletedOn
        )
        result = 31 * result
        return result
    }


}