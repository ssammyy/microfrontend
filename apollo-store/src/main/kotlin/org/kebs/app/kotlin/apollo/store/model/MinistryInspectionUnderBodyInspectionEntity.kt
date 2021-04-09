package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_MMINISTRY_INSPECTION_UNDER_BODY_INSPECTION")
class MinistryInspectionUnderBodyInspectionEntity : Serializable {
    @get:Column(name = "ID")
    @get:Id
    var id: Long = 0
    @get:Column(name = "STATUS")
    @get:Basic
    var status: Int? = null
    @get:Column(name = "GENERAR_INSPECTION")
    @get:Basic
    var generarInspection: Long? = null
    @get:Column(name = "LEAF_SPRINGS")
    @get:Basic
    var leafSprings: String? = null
    @get:Column(name = "LEAF_SPRINGS_STATUS")
    @get:Basic
    var leafSpringsStatus: String? = null
    @get:Column(name = "LEAF_SPRINGS_REMARKS")
    @get:Basic
    var leafSpringsRemarks: String? = null
    private var uBolts: String? = null
    private var uBoltsStatus: String? = null
    private var uBoltsRemarks: String? = null
    @get:Column(name = "SPRING_BUSHES")
    @get:Basic
    var springBushes: String? = null
    @get:Column(name = "SPRING_BUSHES_STATUS")
    @get:Basic
    var springBushesStatus: String? = null
    @get:Column(name = "SPRING_BUSHES_REMARKS")
    @get:Basic
    var springBushesRemarks: String? = null
    @get:Column(name = "SPRING_PINS")
    @get:Basic
    var springPins: String? = null
    @get:Column(name = "SPRING_PINS_STATUS")
    @get:Basic
    var springPinsStatus: String? = null
    @get:Column(name = "SPRING_PINS_REMARKS")
    @get:Basic
    var springPinsRemarks: String? = null
    @get:Column(name = "COIL_SPRINGS")
    @get:Basic
    var coilSprings: String? = null
    @get:Column(name = "COIL_SPRINGS_STATUS")
    @get:Basic
    var coilSpringsStatus: String? = null
    @get:Column(name = "COIL_SPRINGS_REMARKS")
    @get:Basic
    var coilSpringsRemarks: String? = null
    @get:Column(name = "FRONT_SHOCK_ABSORBERS")
    @get:Basic
    var frontShockAbsorbers: String? = null
    @get:Column(name = "FRONT_SHOCK_ABSORBERS_STATUS")
    @get:Basic
    var frontShockAbsorbersStatus: String? = null
    @get:Column(name = "FRONT_SHOCK_ABSORBERS_REMARKS")
    @get:Basic
    var frontShockAbsorbersRemarks: String? = null
    @get:Column(name = "REAR_SHOCK_ABSORBERS")
    @get:Basic
    var rearShockAbsorbers: String? = null
    @get:Column(name = "REAR_SHOCK_ABSORBERS_STATUS")
    @get:Basic
    var rearShockAbsorbersStatus: String? = null
    @get:Column(name = "REAR_SHOCK_ABSORBERS_REMARKS")
    @get:Basic
    var rearShockAbsorbersRemarks: String? = null
    @get:Column(name = "SUB_FRAME_MOUNTINGS")
    @get:Basic
    var subFrameMountings: String? = null
    @get:Column(name = "SUB_FRAME_MOUNTINGS_STATUS")
    @get:Basic
    var subFrameMountingsStatus: String? = null
    @get:Column(name = "SUB_FRAME_MOUNTINGS_REMARKS")
    @get:Basic
    var subFrameMountingsRemarks: String? = null
    @get:Column(name = "ENGINE_MOUNTINGS")
    @get:Basic
    var engineMountings: String? = null
    @get:Column(name = "ENGINE_MOUNTINGS_STATUS")
    @get:Basic
    var engineMountingsStatus: String? = null
    @get:Column(name = "ENGINE_MOUNTINGS_REMARKS")
    @get:Basic
    var engineMountingsRemarks: String? = null
    @get:Column(name = "GEAR_BOX_MOUNTINGS")
    @get:Basic
    var gearBoxMountings: String? = null
    @get:Column(name = "GEAR_BOX_MOUNTINGS_STATUS")
    @get:Basic
    var gearBoxMountingsStatus: String? = null
    @get:Column(name = "GEAR_BOX_MOUNTINGS_REMARKS")
    @get:Basic
    var gearBoxMountingsRemarks: String? = null
    @get:Column(name = "STABILIZER_BUSHES")
    @get:Basic
    var stabilizerBushes: String? = null
    @get:Column(name = "STABILIZER_BUSHES_STATUS")
    @get:Basic
    var stabilizerBushesStatus: String? = null
    @get:Column(name = "STABILIZER_BUSHES_REMARKS")
    @get:Basic
    var stabilizerBushesRemarks: String? = null
    @get:Column(name = "FUEL_TANK")
    @get:Basic
    var fuelTank: String? = null
    @get:Column(name = "FUEL_TANK_STATUS")
    @get:Basic
    var fuelTankStatus: String? = null
    @get:Column(name = "FUEL_TANK_REMARKS")
    @get:Basic
    var fuelTankRemarks: String? = null
    @get:Column(name = "FUEL_LINES")
    @get:Basic
    var fuelLines: String? = null
    @get:Column(name = "FUEL_LINES_STATUS")
    @get:Basic
    var fuelLinesStatus: String? = null
    @get:Column(name = "FUEL_LINES_REMARKS")
    @get:Basic
    var fuelLinesRemarks: String? = null
    @get:Column(name = "BRAKE_LINES")
    @get:Basic
    var brakeLines: String? = null
    @get:Column(name = "BRAKE_LINES_STATUS")
    @get:Basic
    var brakeLinesStatus: String? = null
    @get:Column(name = "BRAKE_LINES_REMARKS")
    @get:Basic
    var brakeLinesRemarks: String? = null
    @get:Column(name = "EXHAUST_SYSTEM")
    @get:Basic
    var exhaustSystem: String? = null
    @get:Column(name = "EXHAUST_SYSTEM_STATUS")
    @get:Basic
    var exhaustSystemStatus: String? = null
    @get:Column(name = "EXHAUST_SYSTEM_REMARKS")
    @get:Basic
    var exhaustSystemRemarks: String? = null
    @get:Column(name = "TRANSMISSION")
    @get:Basic
    var transmission: String? = null
    @get:Column(name = "TRANSMISSION_STATUS")
    @get:Basic
    var transmissionStatus: String? = null
    @get:Column(name = "TRANSMISSION_REMARKS")
    @get:Basic
    var transmissionRemarks: String? = null
    @get:Column(name = "STEERING_BOX")
    @get:Basic
    var steeringBox: String? = null
    @get:Column(name = "STEERING_BOX_STATUS")
    @get:Basic
    var steeringBoxStatus: String? = null
    @get:Column(name = "STEERING_BOX_REMARKS")
    @get:Basic
    var steeringBoxRemarks: String? = null
    @get:Column(name = "CHASSIS")
    @get:Basic
    var chassis: String? = null
    @get:Column(name = "CHASSIS_STATUS")
    @get:Basic
    var chassisStatus: String? = null
    @get:Column(name = "CHASSIS_REMARKS")
    @get:Basic
    var chassisRemarks: String? = null
    @get:Column(name = "MONO_BLOCK_BODY")
    @get:Basic
    var monoBlockBody: String? = null
    @get:Column(name = "MONO_BLOCK_BODY_STATUS")
    @get:Basic
    var monoBlockBodyStatus: String? = null
    @get:Column(name = "MONO_BLOCK_BODY_REMARKS")
    @get:Basic
    var monoBlockBodyRemarks: String? = null
    @get:Column(name = "FRONT_SUB_FRAME")
    @get:Basic
    var frontSubFrame: String? = null
    @get:Column(name = "FRONT_SUB_FRAME_STATUS")
    @get:Basic
    var frontSubFrameStatus: String? = null
    @get:Column(name = "FRONT_SUB_FRAME_REMARKS")
    @get:Basic
    var frontSubFrameRemarks: String? = null
    @get:Column(name = "REAR_SUB_FRAME")
    @get:Basic
    var rearSubFrame: String? = null
    @get:Column(name = "REAR_SUB_FRAME_STATUS")
    @get:Basic
    var rearSubFrameStatus: String? = null
    @get:Column(name = "REAR_SUB_FRAME_REMARKS")
    @get:Basic
    var rearSubFrameRemarks: String? = null
    @get:Column(name = "FRONT_AXLE")
    @get:Basic
    var frontAxle: String? = null
    @get:Column(name = "FRONT_AXLE_STATUS")
    @get:Basic
    var frontAxleStatus: String? = null
    @get:Column(name = "FRONT_AXLE_REMARKS")
    @get:Basic
    var frontAxleRemarks: String? = null
    @get:Column(name = "REAR_AXLE")
    @get:Basic
    var rearAxle: String? = null
    @get:Column(name = "REAR_AXLE_STATUS")
    @get:Basic
    var rearAxleStatus: String? = null
    @get:Column(name = "REAR_AXLE_REMARKS")
    @get:Basic
    var rearAxleRemarks: String? = null
    @get:Column(name = "SECOND_REAR_AXLE")
    @get:Basic
    var secondRearAxle: String? = null
    @get:Column(name = "SECOND_REAR_AXLE_STATUS")
    @get:Basic
    var secondRearAxleStatus: String? = null
    @get:Column(name = "SECOND_REAR_AXLE_REMARKS")
    @get:Basic
    var secondRearAxleRemarks: String? = null
    @get:Column(name = "WHEEL_GEOMETRY")
    @get:Basic
    var wheelGeometry: String? = null
    @get:Column(name = "WHEEL_GEOMETRY_STATUS")
    @get:Basic
    var wheelGeometryStatus: String? = null
    @get:Column(name = "WHEEL_GEOMETRY_REMARKS")
    @get:Basic
    var wheelGeometryRemarks: String? = null
    @get:Column(name = "KING_PIN_BUSHES")
    @get:Basic
    var kingPinBushes: String? = null
    @get:Column(name = "KING_PIN_BUSHES_STATUS")
    @get:Basic
    var kingPinBushesStatus: String? = null
    @get:Column(name = "KING_PIN_BUSHES_REMARKS")
    @get:Basic
    var kingPinBushesRemarks: String? = null
    @get:Column(name = "BALL_JOINTS")
    @get:Basic
    var ballJoints: String? = null
    @get:Column(name = "BALL_JOINTS_STATUS")
    @get:Basic
    var ballJointsStatus: String? = null
    @get:Column(name = "BALL_JOINTS_REMARKS")
    @get:Basic
    var ballJointsRemarks: String? = null
    @get:Column(name = "STEERING_SYSTEM")
    @get:Basic
    var steeringSystem: String? = null
    @get:Column(name = "STEERING_SYSTEM_STATUS")
    @get:Basic
    var steeringSystemStatus: String? = null
    @get:Column(name = "STEERING_SYSTEM_REMARKS")
    @get:Basic
    var steeringSystemRemarks: String? = null
    @get:Column(name = "BRAKE_DISC")
    @get:Basic
    var brakeDisc: String? = null
    @get:Column(name = "BRAKE_DISC_STATUS")
    @get:Basic
    var brakeDiscStatus: String? = null
    @get:Column(name = "BRAKE_DISC_REMARKS")
    @get:Basic
    var brakeDiscRemarks: String? = null
    @get:Column(name = "CALIPERS")
    @get:Basic
    var calipers: String? = null
    @get:Column(name = "CALIPERS_STATUS")
    @get:Basic
    var calipersStatus: String? = null
    @get:Column(name = "CALIPERS_REMARKS")
    @get:Basic
    var calipersRemarks: String? = null
    @get:Column(name = "BRAKE_PAD_LIFE")
    @get:Basic
    var brakePadLife: String? = null
    @get:Column(name = "BRAKE_PAD_LIFE_STATUS")
    @get:Basic
    var brakePadLifeStatus: String? = null
    @get:Column(name = "BRAKE_PAD_LIFE_REMARKS")
    @get:Basic
    var brakePadLifeRemarks: String? = null
    @get:Column(name = "BRAKE_DRUMS")
    @get:Basic
    var brakeDrums: String? = null
    @get:Column(name = "BRAKE_DRUMS_STATUS")
    @get:Basic
    var brakeDrumsStatus: String? = null
    @get:Column(name = "BRAKE_DRUMS_REMARKS")
    @get:Basic
    var brakeDrumsRemarks: String? = null
    @get:Column(name = "WHEEL_CYLINDERS")
    @get:Basic
    var wheelCylinders: String? = null
    @get:Column(name = "WHEEL_CYLINDERS_STATUS")
    @get:Basic
    var wheelCylindersStatus: String? = null
    @get:Column(name = "WHEEL_CYLINDERS_REMARKS")
    @get:Basic
    var wheelCylindersRemarks: String? = null
    @get:Column(name = "REAR_OIL_SEALS")
    @get:Basic
    var rearOilSeals: String? = null
    @get:Column(name = "REAR_OIL_SEALS_STATUS")
    @get:Basic
    var rearOilSealsStatus: String? = null
    @get:Column(name = "REAR_OIL_SEALS_REMARKS")
    @get:Basic
    var rearOilSealsRemarks: String? = null
    @get:Column(name = "FRONT_LH_DRIVING_SHAFT")
    @get:Basic
    var frontLhDrivingShaft: String? = null
    @get:Column(name = "FRONT_LH_DRIVING_SHAFT_STATUS")
    @get:Basic
    var frontLhDrivingShaftStatus: String? = null
    @get:Column(name = "FRONT_LH_DRIVING_SHAFT_REMARKS")
    @get:Basic
    var frontLhDrivingShaftRemarks: String? = null
    @get:Column(name = "FRONT_RF_DRIVING_SHAFT")
    @get:Basic
    var frontRfDrivingShaft: String? = null
    @get:Column(name = "FRONT_RF_DRIVING_SHAFT_STATUS")
    @get:Basic
    var frontRfDrivingShaftStatus: String? = null
    @get:Column(name = "FRONT_RF_DRIVING_SHAFT_REMARKS")
    @get:Basic
    var frontRfDrivingShaftRemarks: String? = null
    @get:Column(name = "FRONT_DIFFERENTIAL")
    @get:Basic
    var frontDifferential: String? = null
    @get:Column(name = "FRONT_DIFFERENTIAL_STATUS")
    @get:Basic
    var frontDifferentialStatus: String? = null
    @get:Column(name = "FRONT_DIFFERENTIAL_REMARKS")
    @get:Basic
    var frontDifferentialRemarks: String? = null
    @get:Column(name = "REAR_DIFFERENTIAL")
    @get:Basic
    var rearDifferential: String? = null
    @get:Column(name = "REAR_DIFFERENTIAL_STATUS")
    @get:Basic
    var rearDifferentialStatus: String? = null
    @get:Column(name = "REAR_DIFFERENTIAL_REMARKS")
    @get:Basic
    var rearDifferentialRemarks: String? = null
    @get:Column(name = "TRANSFER_CASE")
    @get:Basic
    var transferCase: String? = null
    @get:Column(name = "TRANSFER_CASE_STATUS")
    @get:Basic
    var transferCaseStatus: String? = null
    @get:Column(name = "TRANSFER_CASE_REMARKS")
    @get:Basic
    var transferCaseRemarks: String? = null
    @get:Column(name = "FRONT_PROPELLER_SHAFT")
    @get:Basic
    var frontPropellerShaft: String? = null
    @get:Column(name = "FRONT_PROPELLER_SHAFT_STATUS")
    @get:Basic
    var frontPropellerShaftStatus: String? = null
    @get:Column(name = "FRONT_PROPELLER_SHAFT_REMARKS")
    @get:Basic
    var frontPropellerShaftRemarks: String? = null
    @get:Column(name = "CENTER_BEARING")
    @get:Basic
    var centerBearing: String? = null
    @get:Column(name = "CENTER_BEARING_STATUS")
    @get:Basic
    var centerBearingStatus: String? = null
    @get:Column(name = "CENTER_BEARING_REMARKS")
    @get:Basic
    var centerBearingRemarks: String? = null
    @get:Column(name = "FRONT_REAR_OILS_SEALS")
    @get:Basic
    var frontRearOilsSeals: String? = null
    @get:Column(name = "FRONT_REAR_OILS_SEALS_STATUS")
    @get:Basic
    var frontRearOilsSealsStatus: String? = null
    @get:Column(name = "FRONT_REAR_OILS_SEALS_REMARKS")
    @get:Basic
    var frontRearOilsSealsRemarks: String? = null
    @get:Column(name = "REAR_LH_DRIVE_SHAFT")
    @get:Basic
    var rearLhDriveShaft: String? = null
    @get:Column(name = "REAR_LH_DRIVE_SHAFT_STATUS")
    @get:Basic
    var rearLhDriveShaftStatus: String? = null
    @get:Column(name = "REAR_LH_DRIVE_SHAFT_REMARKS")
    @get:Basic
    var rearLhDriveShaftRemarks: String? = null
    @get:Column(name = "REAR_RH_DRIVE_SHAFT")
    @get:Basic
    var rearRhDriveShaft: String? = null
    @get:Column(name = "REAR_RH_DRIVE_SHAFT_STATUS")
    @get:Basic
    var rearRhDriveShaftStatus: String? = null
    @get:Column(name = "REAR_RH_DRIVE_SHAFT_REMARKS")
    @get:Basic
    var rearRhDriveShaftRemarks: String? = null
    @get:Column(name = "HAND_BRAKE_CABLES")
    @get:Basic
    var handBrakeCables: String? = null
    @get:Column(name = "HAND_BRAKE_CABLES_STATUS")
    @get:Basic
    var handBrakeCablesStatus: String? = null
    @get:Column(name = "HAND_BRAKE_CABLES_REMARKS")
    @get:Basic
    var handBrakeCablesRemarks: String? = null
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

    @Basic
    @Column(name = "U_BOLTS")
    fun getuBolts(): String? {
        return uBolts
    }

    fun setuBolts(uBolts: String?) {
        this.uBolts = uBolts
    }

    @Basic
    @Column(name = "U_BOLTS_STATUS")
    fun getuBoltsStatus(): String? {
        return uBoltsStatus
    }

    fun setuBoltsStatus(uBoltsStatus: String?) {
        this.uBoltsStatus = uBoltsStatus
    }

    @Basic
    @Column(name = "U_BOLTS_REMARKS")
    fun getuBoltsRemarks(): String? {
        return uBoltsRemarks
    }

    fun setuBoltsRemarks(uBoltsRemarks: String?) {
        this.uBoltsRemarks = uBoltsRemarks
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as MinistryInspectionUnderBodyInspectionEntity
        return id == that.id &&
                status == that.status &&
                generarInspection == that.generarInspection &&
                leafSprings == that.leafSprings &&
                leafSpringsStatus == that.leafSpringsStatus &&
                leafSpringsRemarks == that.leafSpringsRemarks &&
                uBolts == that.uBolts &&
                uBoltsStatus == that.uBoltsStatus &&
                uBoltsRemarks == that.uBoltsRemarks &&
                springBushes == that.springBushes &&
                springBushesStatus == that.springBushesStatus &&
                springBushesRemarks == that.springBushesRemarks &&
                springPins == that.springPins &&
                springPinsStatus == that.springPinsStatus &&
                springPinsRemarks == that.springPinsRemarks &&
                coilSprings == that.coilSprings &&
                coilSpringsStatus == that.coilSpringsStatus &&
                coilSpringsRemarks == that.coilSpringsRemarks &&
                frontShockAbsorbers == that.frontShockAbsorbers &&
                frontShockAbsorbersStatus == that.frontShockAbsorbersStatus &&
                frontShockAbsorbersRemarks == that.frontShockAbsorbersRemarks &&
                rearShockAbsorbers == that.rearShockAbsorbers &&
                rearShockAbsorbersStatus == that.rearShockAbsorbersStatus &&
                rearShockAbsorbersRemarks == that.rearShockAbsorbersRemarks &&
                subFrameMountings == that.subFrameMountings &&
                subFrameMountingsStatus == that.subFrameMountingsStatus &&
                subFrameMountingsRemarks == that.subFrameMountingsRemarks &&
                engineMountings == that.engineMountings &&
                engineMountingsStatus == that.engineMountingsStatus &&
                engineMountingsRemarks == that.engineMountingsRemarks &&
                gearBoxMountings == that.gearBoxMountings &&
                gearBoxMountingsStatus == that.gearBoxMountingsStatus &&
                gearBoxMountingsRemarks == that.gearBoxMountingsRemarks &&
                stabilizerBushes == that.stabilizerBushes &&
                stabilizerBushesStatus == that.stabilizerBushesStatus &&
                stabilizerBushesRemarks == that.stabilizerBushesRemarks &&
                fuelTank == that.fuelTank &&
                fuelTankStatus == that.fuelTankStatus &&
                fuelTankRemarks == that.fuelTankRemarks &&
                fuelLines == that.fuelLines &&
                fuelLinesStatus == that.fuelLinesStatus &&
                fuelLinesRemarks == that.fuelLinesRemarks &&
                brakeLines == that.brakeLines &&
                brakeLinesStatus == that.brakeLinesStatus &&
                brakeLinesRemarks == that.brakeLinesRemarks &&
                exhaustSystem == that.exhaustSystem &&
                exhaustSystemStatus == that.exhaustSystemStatus &&
                exhaustSystemRemarks == that.exhaustSystemRemarks &&
                transmission == that.transmission &&
                transmissionStatus == that.transmissionStatus &&
                transmissionRemarks == that.transmissionRemarks &&
                steeringBox == that.steeringBox &&
                steeringBoxStatus == that.steeringBoxStatus &&
                steeringBoxRemarks == that.steeringBoxRemarks &&
                chassis == that.chassis &&
                chassisStatus == that.chassisStatus &&
                chassisRemarks == that.chassisRemarks &&
                monoBlockBody == that.monoBlockBody &&
                monoBlockBodyStatus == that.monoBlockBodyStatus &&
                monoBlockBodyRemarks == that.monoBlockBodyRemarks &&
                frontSubFrame == that.frontSubFrame &&
                frontSubFrameStatus == that.frontSubFrameStatus &&
                frontSubFrameRemarks == that.frontSubFrameRemarks &&
                rearSubFrame == that.rearSubFrame &&
                rearSubFrameStatus == that.rearSubFrameStatus &&
                rearSubFrameRemarks == that.rearSubFrameRemarks &&
                frontAxle == that.frontAxle &&
                frontAxleStatus == that.frontAxleStatus &&
                frontAxleRemarks == that.frontAxleRemarks &&
                rearAxle == that.rearAxle &&
                rearAxleStatus == that.rearAxleStatus &&
                rearAxleRemarks == that.rearAxleRemarks &&
                secondRearAxle == that.secondRearAxle &&
                secondRearAxleStatus == that.secondRearAxleStatus &&
                secondRearAxleRemarks == that.secondRearAxleRemarks &&
                wheelGeometry == that.wheelGeometry &&
                wheelGeometryStatus == that.wheelGeometryStatus &&
                wheelGeometryRemarks == that.wheelGeometryRemarks &&
                kingPinBushes == that.kingPinBushes &&
                kingPinBushesStatus == that.kingPinBushesStatus &&
                kingPinBushesRemarks == that.kingPinBushesRemarks &&
                ballJoints == that.ballJoints &&
                ballJointsStatus == that.ballJointsStatus &&
                ballJointsRemarks == that.ballJointsRemarks &&
                steeringSystem == that.steeringSystem &&
                steeringSystemStatus == that.steeringSystemStatus &&
                steeringSystemRemarks == that.steeringSystemRemarks &&
                brakeDisc == that.brakeDisc &&
                brakeDiscStatus == that.brakeDiscStatus &&
                brakeDiscRemarks == that.brakeDiscRemarks &&
                calipers == that.calipers &&
                calipersStatus == that.calipersStatus &&
                calipersRemarks == that.calipersRemarks &&
                brakePadLife == that.brakePadLife &&
                brakePadLifeStatus == that.brakePadLifeStatus &&
                brakePadLifeRemarks == that.brakePadLifeRemarks &&
                brakeDrums == that.brakeDrums &&
                brakeDrumsStatus == that.brakeDrumsStatus &&
                brakeDrumsRemarks == that.brakeDrumsRemarks &&
                wheelCylinders == that.wheelCylinders &&
                wheelCylindersStatus == that.wheelCylindersStatus &&
                wheelCylindersRemarks == that.wheelCylindersRemarks &&
                rearOilSeals == that.rearOilSeals &&
                rearOilSealsStatus == that.rearOilSealsStatus &&
                rearOilSealsRemarks == that.rearOilSealsRemarks &&
                frontLhDrivingShaft == that.frontLhDrivingShaft &&
                frontLhDrivingShaftStatus == that.frontLhDrivingShaftStatus &&
                frontLhDrivingShaftRemarks == that.frontLhDrivingShaftRemarks &&
                frontRfDrivingShaft == that.frontRfDrivingShaft &&
                frontRfDrivingShaftStatus == that.frontRfDrivingShaftStatus &&
                frontRfDrivingShaftRemarks == that.frontRfDrivingShaftRemarks &&
                frontDifferential == that.frontDifferential &&
                frontDifferentialStatus == that.frontDifferentialStatus &&
                frontDifferentialRemarks == that.frontDifferentialRemarks &&
                rearDifferential == that.rearDifferential &&
                rearDifferentialStatus == that.rearDifferentialStatus &&
                rearDifferentialRemarks == that.rearDifferentialRemarks &&
                transferCase == that.transferCase &&
                transferCaseStatus == that.transferCaseStatus &&
                transferCaseRemarks == that.transferCaseRemarks &&
                frontPropellerShaft == that.frontPropellerShaft &&
                frontPropellerShaftStatus == that.frontPropellerShaftStatus &&
                frontPropellerShaftRemarks == that.frontPropellerShaftRemarks &&
                centerBearing == that.centerBearing &&
                centerBearingStatus == that.centerBearingStatus &&
                centerBearingRemarks == that.centerBearingRemarks &&
                frontRearOilsSeals == that.frontRearOilsSeals &&
                frontRearOilsSealsStatus == that.frontRearOilsSealsStatus &&
                frontRearOilsSealsRemarks == that.frontRearOilsSealsRemarks &&
                rearLhDriveShaft == that.rearLhDriveShaft &&
                rearLhDriveShaftStatus == that.rearLhDriveShaftStatus &&
                rearLhDriveShaftRemarks == that.rearLhDriveShaftRemarks &&
                rearRhDriveShaft == that.rearRhDriveShaft &&
                rearRhDriveShaftStatus == that.rearRhDriveShaftStatus &&
                rearRhDriveShaftRemarks == that.rearRhDriveShaftRemarks &&
                handBrakeCables == that.handBrakeCables &&
                handBrakeCablesStatus == that.handBrakeCablesStatus &&
                handBrakeCablesRemarks == that.handBrakeCablesRemarks &&
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
                leafSprings, leafSpringsStatus, leafSpringsRemarks, uBolts, uBoltsStatus, uBoltsRemarks,
                springBushes, springBushesStatus, springBushesRemarks, springPins, springPinsStatus, springPinsRemarks,
                coilSprings, coilSpringsStatus, coilSpringsRemarks, frontShockAbsorbers, frontShockAbsorbersStatus,
                frontShockAbsorbersRemarks, rearShockAbsorbers, rearShockAbsorbersStatus, rearShockAbsorbersRemarks,
                subFrameMountings, subFrameMountingsStatus, subFrameMountingsRemarks, engineMountings, engineMountingsStatus,
                engineMountingsRemarks, gearBoxMountings, gearBoxMountingsStatus, gearBoxMountingsRemarks,
                stabilizerBushes, stabilizerBushesStatus, stabilizerBushesRemarks, fuelTank, fuelTankStatus, fuelTankRemarks,
                fuelLines, fuelLinesStatus, fuelLinesRemarks, brakeLines, brakeLinesStatus, brakeLinesRemarks,
                exhaustSystem, exhaustSystemStatus, exhaustSystemRemarks, transmission, transmissionStatus, transmissionRemarks,
                steeringBox, steeringBoxStatus, steeringBoxRemarks, chassis, chassisStatus, chassisRemarks,
                monoBlockBody, monoBlockBodyStatus, monoBlockBodyRemarks, frontSubFrame, frontSubFrameStatus,
                frontSubFrameRemarks, rearSubFrame, rearSubFrameStatus, rearSubFrameRemarks, frontAxle, frontAxleStatus,
                frontAxleRemarks, rearAxle, rearAxleStatus, rearAxleRemarks, secondRearAxle, secondRearAxleStatus,
                secondRearAxleRemarks, wheelGeometry, wheelGeometryStatus, wheelGeometryRemarks, kingPinBushes, kingPinBushesStatus,
                kingPinBushesRemarks, ballJoints, ballJointsStatus, ballJointsRemarks, steeringSystem, steeringSystemStatus,
                steeringSystemRemarks, brakeDisc, brakeDiscStatus, brakeDiscRemarks, calipers, calipersStatus, calipersRemarks,
                brakePadLife, brakePadLifeStatus, brakePadLifeRemarks, brakeDrums, brakeDrumsStatus, brakeDrumsRemarks,
                wheelCylinders, wheelCylindersStatus, wheelCylindersRemarks, rearOilSeals, rearOilSealsStatus, rearOilSealsRemarks,
                frontLhDrivingShaft, frontLhDrivingShaftStatus, frontLhDrivingShaftRemarks, frontRfDrivingShaft, frontRfDrivingShaftStatus,
                frontRfDrivingShaftRemarks, frontDifferential, frontDifferentialStatus, frontDifferentialRemarks, rearDifferential,
                rearDifferentialStatus, rearDifferentialRemarks, transferCase, transferCaseStatus, transferCaseRemarks, frontPropellerShaft,
                frontPropellerShaftStatus, frontPropellerShaftRemarks, centerBearing, centerBearingStatus, centerBearingRemarks, frontRearOilsSeals,
                frontRearOilsSealsStatus, frontRearOilsSealsRemarks, rearLhDriveShaft, rearLhDriveShaftStatus, rearLhDriveShaftRemarks, rearRhDriveShaft,
                rearRhDriveShaftStatus, rearRhDriveShaftRemarks, handBrakeCables, handBrakeCablesStatus, handBrakeCablesRemarks, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn, section)
    }
}