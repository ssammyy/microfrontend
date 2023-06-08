package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Time
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_MOTOR_VEHICLE_INSPECTION")
class MotorVehicleInspectionEntity: Serializable {
    @get:Column(name = "ID")
    @get:Id
    @SequenceGenerator(name = "DAT_KEBS_MOTOR_VEHICLE_INSPECTION_SEQ_GEN", sequenceName = "DAT_KEBS_MOTOR_VEHICLE_INSPECTION_seq", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_MOTOR_VEHICLE_INSPECTION_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long? = 0

    @get:Column(name = "SN")
    @get:Basic
    var sn: String? = null

    @get:Column(name = "SN_REMARKS")
    @get:Basic
    var snRemarks: String? = null

    @get:Column(name = "SN_STATUS")
    @get:Basic
    var snStatus: Long? = null

    @get:Column(name = "MAKE_OF_VEHICLE")
    @get:Basic
    var makeOfVehicle: String? = null

    @get:Column(name = "MAKE_OF_VEHICLE_REMARKS")
    @get:Basic
    var makeOfVehicleRemarks: String? = null

    @get:Column(name = "MAKE_OF_VEHICLE_STATUS")
    @get:Basic
    var makeOfVehicleStatus: Long? = null

    @get:Column(name = "CHASIS_NUMBER")
    @get:Basic
    var chasisNumber: String? = null

    @get:Column(name = "CHASIS_NUMBER_REMARKS")
    @get:Basic
    var chasisNumberRemarks: String? = null

    @get:Column(name = "CHASIS_NUMBER_STATUS")
    @get:Basic
    var chasisNumberStatus: Long? = null

    @get:Column(name = "ENGINE_NUMBER")
    @get:Basic
    var engineNumber: String? = null

    @get:Column(name = "ENGINE_NUMBER_REMARKS")
    @get:Basic
    var engineNumberRemarks: String? = null

    @get:Column(name = "ENGINE_NUMBER_STATUS")
    @get:Basic
    var engineNumberStatus: Long? = null

    @get:Column(name = "MANUFACTURE_DATE")
    @get:Basic
    var manufactureDate: String? = null

    @get:Column(name = "MANUFACTURE_DATE_REMARKS")
    @get:Basic
    var manufactureDateRemarks: String? = null

    @get:Column(name = "MANUFACTURE_DATE_STATUS")
    @get:Basic
    var manufactureDateStatus: Long? = null

    @get:Column(name = "REGISTRATION_DATE")
    @get:Basic
    var registrationDate: String? = null

    @get:Column(name = "REGISTRATION_DATE_REMARKS")
    @get:Basic
    var registrationDateRemarks: String? = null

    @get:Column(name = "REGISTRATION_DATE_STATUS")
    @get:Basic
    var registrationDateStatus: Long? = null

    @get:Column(name = "ODOMETER_READING")
    @get:Basic
    var odometerReading: String? = null

    @get:Column(name = "ODOMETER_READING_REMARKS")
    @get:Basic
    var odometerReadingRemarks: String? = null

    @get:Column(name = "ODOMETER_READING_STATUS")
    @get:Basic
    var odometerReadingStatus: Long? = null

    @get:Column(name = "DRIVE")
    @get:Basic
    var drive: String? = null

    @get:Basic
    @get:Column(name = "DRIVE_REMARKS")
    var driveRemarks: String? = null

    @get:Basic
    @get:Column(name = "DRIVE_STATUS")
    var driveStatus: Long? = null

    @get:Column(name = "TRANSMISSION")
    @get:Basic
    var transmission: String? = null

    @get:Column(name = "TRANSMISSION_REMARKS")
    @get:Basic
    var transmissionRemarks: String? = null

    @get:Column(name = "TRANSMISSION_STATUS")
    @get:Basic
    var transmissionStatus: Long? = null

    @get:Column(name = "COLOR")
    @get:Basic
    var color: String? = null

    @get:Column(name = "COLOR_REMARKS")
    @get:Basic
    var colorRemarks: String? = null

    @get:Column(name = "COLOR_STATUS")
    @get:Basic
    var colorStatus: Long? = null

    @get:Column(name = "OVERAL_APPEARANCE")
    @get:Basic
    var overalAppearance: String? = null

    @get:Column(name = "SUBMITTED_DATE")
    @get:Basic
    var submittedDate: Time? = null

    @get:Column(name = "REMARKS")
    @get:Basic
    var remarks: String? = null

    @get:Column(name = "ATTACHMENTS")
    @get:Basic
    var attachments: String? = null

    @get:Column(name = "VAR_FIELD_1")
    @get:Basic
    var varField1: String? = null

    @get:Column(name = "VAR_FIELD_2")
    @get:Basic
    var varField2: String? = null

    @get:Column(name = "VAR_FIELD_3")
    @get:Basic
    var varField3: String? = null

    @get:Column(name = "VAR_FIELD_4")
    @get:Basic
    var varField4: String? = null

    @get:Column(name = "VAR_FIELD_5")
    @get:Basic
    var varField5: String? = null

    @get:Column(name = "VAR_FIELD_6")
    @get:Basic
    var varField6: String? = null

    @get:Column(name = "VAR_FIELD_7")
    @get:Basic
    var varField7: String? = null

    @get:Column(name = "VAR_FIELD_8")
    @get:Basic
    var varField8: String? = null

    @get:Column(name = "VAR_FIELD_9")
    @get:Basic
    var varField9: String? = null

    @get:Column(name = "VAR_FIELD_10")
    @get:Basic
    var varField10: String? = null

    @get:Column(name = "CREATED_BY")
    @get:Basic
    var createdBy: String? = null

    @get:Column(name = "CREATED_ON")
    @get:Basic
    var createdOn: Timestamp? = null

    @get:Column(name = "MODIFIED_BY")
    @get:Basic
    var modifiedBy: String? = null

    @get:Column(name = "MODIFIED_ON")
    @get:Basic
    var modifiedOn: Timestamp? = null

    @get:Column(name = "DELETE_BY")
    @get:Basic
    var deleteBy: String? = null

    @get:Column(name = "DELETE_DON")
    @get:Basic
    var deleteDon: Timestamp? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as MotorVehicleInspectionEntity
        return id == that.id &&
                sn == that.sn &&
                snRemarks == that.snRemarks &&
                snStatus == that.snStatus &&
                makeOfVehicle == that.makeOfVehicle &&
                makeOfVehicleRemarks == that.makeOfVehicleRemarks &&
                makeOfVehicleStatus == that.makeOfVehicleStatus &&
                chasisNumber == that.chasisNumber &&
                chasisNumberRemarks == that.chasisNumberRemarks &&
                chasisNumberStatus == that.chasisNumberStatus &&
                engineNumber == that.engineNumber &&
                engineNumberRemarks == that.engineNumberRemarks &&
                engineNumberStatus == that.engineNumberStatus &&
                manufactureDate == that.manufactureDate &&
                manufactureDateRemarks == that.manufactureDateRemarks &&
                manufactureDateStatus == that.manufactureDateStatus &&
                registrationDate == that.registrationDate &&
                registrationDateRemarks == that.registrationDateRemarks &&
                registrationDateStatus == that.registrationDateStatus &&
                odometerReading == that.odometerReading &&
                odometerReadingRemarks == that.odometerReadingRemarks &&
                odometerReadingStatus == that.odometerReadingStatus &&
                drive == that.drive &&
                driveRemarks == that.driveRemarks &&
                driveStatus == that.driveStatus &&
                transmission == that.transmission &&
                transmissionRemarks == that.transmissionRemarks &&
                transmissionStatus == that.transmissionStatus &&
                color == that.color &&
                colorRemarks == that.colorRemarks &&
                colorStatus == that.colorStatus &&
                overalAppearance == that.overalAppearance &&
                submittedDate == that.submittedDate &&
                remarks == that.remarks &&
                attachments == that.attachments &&
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
                deleteDon == that.deleteDon
    }

    override fun hashCode(): Int {
        return Objects.hash(id, sn, snRemarks, snStatus, makeOfVehicle, makeOfVehicleRemarks, makeOfVehicleStatus, chasisNumber, chasisNumberRemarks, chasisNumberStatus, engineNumber, engineNumberRemarks, engineNumberStatus, manufactureDate, manufactureDateRemarks, manufactureDateStatus, registrationDate, registrationDateRemarks, registrationDateStatus, odometerReading, odometerReadingRemarks, odometerReadingStatus, drive, driveRemarks, driveStatus, transmission, transmissionRemarks, transmissionStatus, color, colorRemarks, colorStatus, overalAppearance, submittedDate, remarks, attachments, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deleteDon)
    }
}