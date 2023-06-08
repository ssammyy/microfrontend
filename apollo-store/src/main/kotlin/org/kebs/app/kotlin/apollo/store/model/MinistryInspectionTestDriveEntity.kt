package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_MMINISTRY_INSPECTION_TEST_DRIVE")
class MinistryInspectionTestDriveEntity: Serializable {
    @get:Column(name = "ID")
    @get:Id
    var id: Long = 0
    @get:Column(name = "STATUS")
    @get:Basic
    var status: Int? = null
    @get:Column(name = "GENERAR_INSPECTION")
    @get:Basic
    var generarInspection: Long? = null
    @get:Column(name = "DRIVING_SEAT_ADJUSTMENTS")
    @get:Basic
    var drivingSeatAdjustments: String? = null
    @get:Column(name = "DRIVING_SEAT_ADJUSTMENTS_STATUS")
    @get:Basic
    var drivingSeatAdjustmentsStatus: String? = null
    @get:Column(name = "DRIVING_SEAT_ADJUSTMENTS_REMARKS")
    @get:Basic
    var drivingSeatAdjustmentsRemarks: String? = null
    @get:Column(name = "DRIVING_PERFORMANCES")
    @get:Basic
    var drivingPerformances: String? = null
    @get:Column(name = "DRIVING_PERFORMANCES_STATUS")
    @get:Basic
    var drivingPerformancesStatus: String? = null
    @get:Column(name = "DRIVING_PERFORMANCES_REMARKS")
    @get:Basic
    var drivingPerformancesRemarks: String? = null
    @get:Column(name = "EMERGENCY_BRAKE")
    @get:Basic
    var emergencyBrake: String? = null
    @get:Column(name = "EMERGENCY_BRAKE_STATUS")
    @get:Basic
    var emergencyBrakeStatus: String? = null
    @get:Column(name = "EMERGENCY_BRAKE_REMARKS")
    @get:Basic
    var emergencyBrakeRemarks: String? = null
    @get:Column(name = "CLUTCH_PERFORMANCE")
    @get:Basic
    var clutchPerformance: String? = null
    @get:Column(name = "CLUTCH_PERFORMANCE_STATUS")
    @get:Basic
    var clutchPerformanceStatus: String? = null
    @get:Column(name = "CLUTCH_PERFORMANCE_REMARKS")
    @get:Basic
    var clutchPerformanceRemarks: String? = null
    @get:Column(name = "GEAR_SHIFT")
    @get:Basic
    var gearShift: String? = null
    @get:Column(name = "GEAR_SHIFT_STATUS")
    @get:Basic
    var gearShiftStatus: String? = null
    @get:Column(name = "GEAR_SHIFT_REMARKS")
    @get:Basic
    var gearShiftRemarks: String? = null
    @get:Column(name = "STEERING_STABILITY")
    @get:Basic
    var steeringStability: String? = null
    @get:Column(name = "STEERING_STABILITY_STATUS")
    @get:Basic
    var steeringStabilityStatus: String? = null
    @get:Column(name = "STEERING_STABILITY_REMARKS")
    @get:Basic
    var steeringStabilityRemarks: String? = null
    @get:Column(name = "FRONT_SUSPENSION")
    @get:Basic
    var frontSuspension: String? = null
    @get:Column(name = "FRONT_SUSPENSION_STATUS")
    @get:Basic
    var frontSuspensionStatus: String? = null
    @get:Column(name = "FRONT_SUSPENSION_REMARKS")
    @get:Basic
    var frontSuspensionRemarks: String? = null
    @get:Column(name = "REAR_SUSPENSION")
    @get:Basic
    var rearSuspension: String? = null
    @get:Column(name = "REAR_SUSPENSION_STATUS")
    @get:Basic
    var rearSuspensionStatus: String? = null
    @get:Column(name = "REAR_SUSPENSION_REMAKRS")
    @get:Basic
    var rearSuspensionRemakrs: String? = null
    @get:Column(name = "GAUGES_AND_INSTRUMENTS")
    @get:Basic
    var gaugesAndInstruments: String? = null
    @get:Column(name = "GAUGES_AND_INSTRUMENTS_STATUS")
    @get:Basic
    var gaugesAndInstrumentsStatus: String? = null
    @get:Column(name = "GAUGES_AND_INSTRUMENTS_REMARKS")
    @get:Basic
    var gaugesAndInstrumentsRemarks: String? = null
    @get:Column(name = "ODOMETER")
    @get:Basic
    var odometer: String? = null
    @get:Column(name = "ODOMETER_STATUS")
    @get:Basic
    var odometerStatus: String? = null
    @get:Column(name = "ODOMETER_REMARKS")
    @get:Basic
    var odometerRemarks: String? = null
    @get:Column(name = "HEATER")
    @get:Basic
    var heater: String? = null
    @get:Column(name = "HEATER_STATUS")
    @get:Basic
    var heaterStatus: String? = null
    @get:Column(name = "HEATER_REMARKS")
    @get:Basic
    var heaterRemarks: String? = null
    @get:Column(name = "DEFROSTER")
    @get:Basic
    var defroster: String? = null
    @get:Column(name = "DEFROSTER_STATUS")
    @get:Basic
    var defrosterStatus: String? = null
    @get:Column(name = "DEFROSTER_REMARKS")
    @get:Basic
    var defrosterRemarks: String? = null
    @get:Column(name = "AIR_CON")
    @get:Basic
    var airCon: String? = null
    @get:Column(name = "AIR_CON_STATUS")
    @get:Basic
    var airConStatus: String? = null
    @get:Column(name = "AIR_CON_REMAKRS")
    @get:Basic
    var airConRemakrs: String? = null
    @get:Column(name = "WINDSCREEN_WIPERS")
    @get:Basic
    var windscreenWipers: String? = null
    @get:Column(name = "WINDSCREEN_WIPERS_STATUS")
    @get:Basic
    var windscreenWipersStatus: String? = null
    @get:Column(name = "WINDSCREEN_WIPERS_REMARKS")
    @get:Basic
    var windscreenWipersRemarks: String? = null
    @get:Column(name = "WASHERS")
    @get:Basic
    var washers: String? = null
    @get:Column(name = "WASHERS_STATUS")
    @get:Basic
    var washersStatus: String? = null
    @get:Column(name = "WASHERS_REMARKS")
    @get:Basic
    var washersRemarks: String? = null
    @get:Column(name = "HORN")
    @get:Basic
    var horn: String? = null
    @get:Column(name = "HORN_STATUS")
    @get:Basic
    var hornStatus: String? = null
    @get:Column(name = "HORN_REMARKS")
    @get:Basic
    var hornRemarks: String? = null
    @get:Column(name = "WHEEL_ALIGNMENT")
    @get:Basic
    var wheelAlignment: String? = null
    @get:Column(name = "WHEEL_ALIGNMENT_STATUS")
    @get:Basic
    var wheelAlignmentStatus: String? = null
    @get:Column(name = "WHEEL_ALIGNMENT_REMARKS")
    @get:Basic
    var wheelAlignmentRemarks: String? = null
    @get:Column(name = "PARKING_BRAKE")
    @get:Basic
    var parkingBrake: String? = null
    @get:Column(name = "PARKING_BRAKE_STATUS")
    @get:Basic
    var parkingBrakeStatus: String? = null
    @get:Column(name = "PARKING_BRAKE_REMARKS")
    @get:Basic
    var parkingBrakeRemarks: String? = null
    @get:Column(name = "FRONT_TYRES")
    @get:Basic
    var frontTyres: String? = null
    @get:Column(name = "FRONT_TYRES_STATUS")
    @get:Basic
    var frontTyresStatus: String? = null
    @get:Column(name = "FRONT_TYRES_REMARKS")
    @get:Basic
    var frontTyresRemarks: String? = null
    @get:Column(name = "REAR_TYRES_1ST_AXLE")
    @get:Basic
    var rearTyres1StAxle: String? = null
    @get:Column(name = "REAR_TYRES_1ST_AXLE_STATUS")
    @get:Basic
    var rearTyres1StAxleStatus: String? = null
    @get:Column(name = "REAR_TYRES_1ST_AXLE_REMARKS")
    @get:Basic
    var rearTyres1StAxleRemarks: String? = null
    @get:Column(name = "REAR_TYRES_2ND_AXLE")
    @get:Basic
    var rearTyres2NdAxle: String? = null
    @get:Column(name = "REAR_TYRES_2ND_AXLE_STATUS")
    @get:Basic
    var rearTyres2NdAxleStatus: String? = null
    @get:Column(name = "REAR_TYRES_2ND_AXLE_REMARKS")
    @get:Basic
    var rearTyres2NdAxleRemarks: String? = null
    @get:Column(name = "REAR_TYRES_3RD_AXLE")
    @get:Basic
    var rearTyres3RdAxle: String? = null
    @get:Column(name = "REAR_TYRES_3RD_AXLE_STATUS")
    @get:Basic
    var rearTyres3RdAxleStatus: String? = null
    @get:Column(name = "REAR_TYRES_3RD_AXLE_REMARKS")
    @get:Basic
    var rearTyres3RdAxleRemarks: String? = null
    @get:Column(name = "REAR_TYRES_4TH_AXLE")
    @get:Basic
    var rearTyres4ThAxle: String? = null
    @get:Column(name = "REAR_TYRES_4TH_AXLE_STATUS")
    @get:Basic
    var rearTyres4ThAxleStatus: String? = null
    @get:Column(name = "REAR_TYRES_4TH_AXLE_REMARKS")
    @get:Basic
    var rearTyres4ThAxleRemarks: String? = null
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
    @get:Column(name = "DELETED_ON")
    @get:Basic
    var deletedOn: Timestamp? = null
    @get:Column(name = "SECTION")
    @get:Basic
    var section: String? = null

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as MinistryInspectionTestDriveEntity
        return id == that.id &&
                status == that.status &&
                generarInspection == that.generarInspection &&
                drivingSeatAdjustments == that.drivingSeatAdjustments &&
                drivingSeatAdjustmentsStatus == that.drivingSeatAdjustmentsStatus &&
                drivingSeatAdjustmentsRemarks == that.drivingSeatAdjustmentsRemarks &&
                drivingPerformances == that.drivingPerformances &&
                drivingPerformancesStatus == that.drivingPerformancesStatus &&
                drivingPerformancesRemarks == that.drivingPerformancesRemarks &&
                emergencyBrake == that.emergencyBrake &&
                emergencyBrakeStatus == that.emergencyBrakeStatus &&
                emergencyBrakeRemarks == that.emergencyBrakeRemarks &&
                clutchPerformance == that.clutchPerformance &&
                clutchPerformanceStatus == that.clutchPerformanceStatus &&
                clutchPerformanceRemarks == that.clutchPerformanceRemarks &&
                gearShift == that.gearShift &&
                gearShiftStatus == that.gearShiftStatus &&
                gearShiftRemarks == that.gearShiftRemarks &&
                steeringStability == that.steeringStability &&
                steeringStabilityStatus == that.steeringStabilityStatus &&
                steeringStabilityRemarks == that.steeringStabilityRemarks &&
                frontSuspension == that.frontSuspension &&
                frontSuspensionStatus == that.frontSuspensionStatus &&
                frontSuspensionRemarks == that.frontSuspensionRemarks &&
                rearSuspension == that.rearSuspension &&
                rearSuspensionStatus == that.rearSuspensionStatus &&
                rearSuspensionRemakrs == that.rearSuspensionRemakrs &&
                gaugesAndInstruments == that.gaugesAndInstruments &&
                gaugesAndInstrumentsStatus == that.gaugesAndInstrumentsStatus &&
                gaugesAndInstrumentsRemarks == that.gaugesAndInstrumentsRemarks &&
                odometer == that.odometer &&
                odometerStatus == that.odometerStatus &&
                odometerRemarks == that.odometerRemarks &&
                heater == that.heater &&
                heaterStatus == that.heaterStatus &&
                heaterRemarks == that.heaterRemarks &&
                defroster == that.defroster &&
                defrosterStatus == that.defrosterStatus &&
                defrosterRemarks == that.defrosterRemarks &&
                airCon == that.airCon &&
                airConStatus == that.airConStatus &&
                airConRemakrs == that.airConRemakrs &&
                windscreenWipers == that.windscreenWipers &&
                windscreenWipersStatus == that.windscreenWipersStatus &&
                windscreenWipersRemarks == that.windscreenWipersRemarks &&
                washers == that.washers &&
                washersStatus == that.washersStatus &&
                washersRemarks == that.washersRemarks &&
                horn == that.horn &&
                hornStatus == that.hornStatus &&
                hornRemarks == that.hornRemarks &&
                wheelAlignment == that.wheelAlignment &&
                wheelAlignmentStatus == that.wheelAlignmentStatus &&
                wheelAlignmentRemarks == that.wheelAlignmentRemarks &&
                parkingBrake == that.parkingBrake &&
                parkingBrakeStatus == that.parkingBrakeStatus &&
                parkingBrakeRemarks == that.parkingBrakeRemarks &&
                frontTyres == that.frontTyres &&
                frontTyresStatus == that.frontTyresStatus &&
                frontTyresRemarks == that.frontTyresRemarks &&
                rearTyres1StAxle == that.rearTyres1StAxle &&
                rearTyres1StAxleStatus == that.rearTyres1StAxleStatus &&
                rearTyres1StAxleRemarks == that.rearTyres1StAxleRemarks &&
                rearTyres2NdAxle == that.rearTyres2NdAxle &&
                rearTyres2NdAxleStatus == that.rearTyres2NdAxleStatus &&
                rearTyres2NdAxleRemarks == that.rearTyres2NdAxleRemarks &&
                rearTyres3RdAxle == that.rearTyres3RdAxle &&
                rearTyres3RdAxleStatus == that.rearTyres3RdAxleStatus &&
                rearTyres3RdAxleRemarks == that.rearTyres3RdAxleRemarks &&
                rearTyres4ThAxle == that.rearTyres4ThAxle &&
                rearTyres4ThAxleStatus == that.rearTyres4ThAxleStatus &&
                rearTyres4ThAxleRemarks == that.rearTyres4ThAxleRemarks &&
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
                deletedOn == that.deletedOn &&
                section == that.section
    }

    override fun hashCode(): Int {
        return Objects.hash(id, status, generarInspection,
                drivingSeatAdjustments, drivingSeatAdjustmentsStatus, drivingSeatAdjustmentsRemarks,
                drivingPerformances, drivingPerformancesStatus, drivingPerformancesRemarks,
                emergencyBrake, emergencyBrakeStatus, emergencyBrakeRemarks,
                clutchPerformance, clutchPerformanceStatus, clutchPerformanceRemarks,
                gearShift, gearShiftStatus, gearShiftRemarks, steeringStability, steeringStabilityStatus,
                steeringStabilityRemarks, frontSuspension, frontSuspensionStatus, frontSuspensionRemarks,
                rearSuspension, rearSuspensionStatus, rearSuspensionRemakrs, gaugesAndInstruments,
                gaugesAndInstrumentsStatus, gaugesAndInstrumentsRemarks, odometer, odometerStatus,
                odometerRemarks, heater, heaterStatus, heaterRemarks, defroster, defrosterStatus,
                defrosterRemarks, airCon, airConStatus, airConRemakrs, windscreenWipers, windscreenWipersStatus,
                windscreenWipersRemarks, washers, washersStatus, washersRemarks, horn, hornStatus, hornRemarks,
                wheelAlignment, wheelAlignmentStatus, wheelAlignmentRemarks, parkingBrake, parkingBrakeStatus,
                parkingBrakeRemarks, frontTyres, frontTyresStatus, frontTyresRemarks, rearTyres1StAxle,
                rearTyres1StAxleStatus, rearTyres1StAxleRemarks, rearTyres2NdAxle, rearTyres2NdAxleStatus,
                rearTyres2NdAxleRemarks, rearTyres3RdAxle, rearTyres3RdAxleStatus, rearTyres3RdAxleRemarks,
                rearTyres4ThAxle, rearTyres4ThAxleStatus, rearTyres4ThAxleRemarks, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn, section)
    }
}