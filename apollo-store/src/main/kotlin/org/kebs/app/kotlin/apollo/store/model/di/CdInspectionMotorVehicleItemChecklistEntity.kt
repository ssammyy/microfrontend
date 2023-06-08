package org.kebs.app.kotlin.apollo.store.model.di

import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import java.io.Serializable
import java.sql.Date
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_CD_INSPECTION_MOTOR_VEHICLE_ITEM_CHECKLIST")
class CdInspectionMotorVehicleItemChecklistEntity : Serializable {

    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_CD_INSPECTION_MOTOR_VEHICLE_ITEM_CHECKLIST_SEQ_GEN", sequenceName = "DAT_KEBS_CD_INSPECTION_MOTOR_VEHICLE_ITEM_CHECKLIST_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_CD_INSPECTION_MOTOR_VEHICLE_ITEM_CHECKLIST_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = null

    @Transient
    var stationId: Long = 0L

    @Transient
    var temporalItemId: Long? = null

    @JoinColumn(name = "ITEM_ID", referencedColumnName = "ID")
    @ManyToOne
    var itemId: CdItemDetailsEntity? = null

    @Column(name = "SSF_ID")
    @Basic
    var ssfId: Long? = null

    @Column(name = "SERIAL_NUMBER")
    @Basic
    var serialNumber: String? = ""

    @Column(name = "MAKE_VEHICLE")
    @Basic
    var makeVehicle: String? = ""

    @Column(name = "CHASSIS_NO", nullable = true)
    @Basic
    var chassisNo: String? = ""

    @Column(name = "YEAR_OF_MANUFACTURE")
    @Basic
    var yearOfManufacture: String? = null

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

    @Column(name = "CATEGORY")
    @Basic
    var category: String? = null

    @Column(name = "COMPLIANT")
    @Basic
    var compliant: String? = null

    @Column(name = "SAMPLED")
    @Basic
    var sampled: String? = null

    @Column(name = "SAMPLE_UPDATED")
    @Basic
    var sampleUpdated: Int? = 0

    @Column(name = "OVERALL_APPEARANCE")
    @Basic
    var overallAppearance: String? = ""

    @Column(name = "REMARKS")
    @Basic
    var remarks: String? = ""

    @Column(name = "MINISTRY_REPORT_FILE")
    @Basic
    var ministryReportFile: ByteArray? = null

    @Column(name = "MINISTRY_REPORT_SUBMIT_STATUS")
    @Basic
    var ministryReportSubmitStatus: Int? = null

    @Column(name = "MINISTRY_REPORT_REINSPECTION_STATUS")
    @Basic
    var ministryReportReinspectionStatus: Int? = null

    @Column(name = "MINISTRY_REPORT_REINSPECTION_REMARKS")
    @Basic
    var ministryReportReinspectionRemarks: String? = null

    @Column(name = "MINISTRY_REPORT_REFERENCE")
    @Basic
    var ministryReportReference: String? = null

    @Column(name = "MINISTRY_REPORT_DATE")
    @Basic
    var ministryReportDate: Timestamp? = null

    @JoinColumn(name = "MINISTRY_STATION_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY)
    var ministryStationId: MinistryStationEntity? = null

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

    @JoinColumn(name = "INSPECTION_ID", referencedColumnName = "ID")
    @ManyToOne
    var inspection: CdInspectionMotorVehicleChecklist? = null

    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    @ManyToOne
    var assignedUser: UsersEntity? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as CdInspectionMotorVehicleItemChecklistEntity
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
                ministryReportFile.contentEquals(that.ministryReportFile) &&
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
                odemetreReading, driveRhdLhd, transmissionAutoManual, colour, overallAppearance, remarks, ministryReportFile, description,
                status, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9,
                varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn)
    }

    override fun toString(): String {
        return "CdInspectionMotorVehicleItemChecklistEntity(id=$id, serialNumber=$serialNumber, makeVehicle=$makeVehicle, chassisNo=$chassisNo, engineNoCapacity=$engineNoCapacity, manufactureDate=$manufactureDate, registrationDate=$registrationDate, odemetreReading=$odemetreReading, driveRhdLhd=$driveRhdLhd, transmissionAutoManual=$transmissionAutoManual, colour=$colour, overallAppearance=$overallAppearance, remarks=$remarks, description=$description, status=$status, createdBy=$createdBy, createdOn=$createdOn, modifiedBy=$modifiedBy, modifiedOn=$modifiedOn, deleteBy=$deleteBy, deletedOn=$deletedOn)"
    }

}