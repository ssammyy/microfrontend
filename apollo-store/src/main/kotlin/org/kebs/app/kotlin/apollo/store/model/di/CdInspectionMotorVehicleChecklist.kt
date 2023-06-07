package org.kebs.app.kotlin.apollo.store.model.di

import java.io.Serializable
import java.sql.Date
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_CD_INSPECTION_MOTOR_VEHICLE_CHECKLIST")
class CdInspectionMotorVehicleChecklist : Serializable {

    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_CD_INSPECTION_MOTOR_VEHICLE_CHECKLIST_SEQ_GEN", sequenceName = "DAT_KEBS_CD_INSPECTION_MOTOR_VEHICLE_CHECKLIST_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_CD_INSPECTION_MOTOR_VEHICLE_CHECKLIST_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = null

    @Column(name = "ITEM_COUNT")
    @Basic
    var itemCount: Int? = null

    @Column(name = "SERIAL_NUMBER")
    @Basic
    var serialNumber: String? = ""

    @Column(name = "MAKE_VEHICLE")
    @Basic
    var makeVehicle: String? = ""

    @Column(name = "CHASSIS_NO")
    @Basic
    var chassisNo: String? = ""

    @Column(name = "ENGINE_NO_CAPACITY")
    @Basic
    var engineNoCapacity: String? = ""

    @Column(name = "MANUFACTURE_DATE")
    @Basic
    var manufactureDate: Date? = null

    @Column(name = "REGISTRATION_DATE")
    @Basic
    var registrationDate: Date? = null

    @Column(name = "ODEMETRE_READING")
    @Basic
    var odemetreReading: String? = ""

    @Column(name = "DRIVE_RHD_LHD")
    @Basic
    var driveRhdLhd: String? = ""

    @Column(name = "TRANSMISSION_AUTO_MANUAL")
    @Basic
    var transmissionAutoManual: String? = ""

    @Column(name = "COLOUR")
    @Basic
    var colour: String? = ""

    @Column(name = "OVERALL_APPEARANCE")
    @Basic
    var overallAppearance: String? = ""

    @Column(name = "REMARKS", length = 512)
    @Basic
    var remarks: String? = ""

    @Column(name = "DESCRIPTION", length = 512)
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

    @JoinColumn(name = "INSPECTION_CHECKLIST_ID", referencedColumnName = "ID")
    @ManyToOne
    var inspectionChecklistType: CdChecklistTypesEntity? = null
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as CdInspectionMotorVehicleChecklist
        return id == that.id &&
                serialNumber == that.serialNumber &&
                makeVehicle == that.makeVehicle &&
                chassisNo == that.chassisNo &&
                engineNoCapacity == that.engineNoCapacity &&
                manufactureDate == that.manufactureDate &&
                registrationDate == that.registrationDate &&
                odemetreReading == that.odemetreReading &&
                driveRhdLhd == that.driveRhdLhd &&
                transmissionAutoManual == that.transmissionAutoManual &&
                colour == that.colour &&
                overallAppearance == that.overallAppearance &&
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
        return Objects.hash(id, serialNumber, makeVehicle, chassisNo, engineNoCapacity, manufactureDate, registrationDate,
                odemetreReading, driveRhdLhd, transmissionAutoManual, colour, overallAppearance, remarks, description,
                status, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9,
                varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn)
    }

    override fun toString(): String {
        return "CdInspectionMotorVehicleChecklist(id=$id, serialNumber=$serialNumber, makeVehicle=$makeVehicle, chassisNo=$chassisNo, engineNoCapacity=$engineNoCapacity, manufactureDate=$manufactureDate, registrationDate=$registrationDate, odemetreReading=$odemetreReading, driveRhdLhd=$driveRhdLhd, transmissionAutoManual=$transmissionAutoManual, colour=$colour, overallAppearance=$overallAppearance, remarks=$remarks, description=$description, status=$status, createdBy=$createdBy, createdOn=$createdOn, modifiedBy=$modifiedBy, modifiedOn=$modifiedOn, deleteBy=$deleteBy, deletedOn=$deletedOn)"
    }

}