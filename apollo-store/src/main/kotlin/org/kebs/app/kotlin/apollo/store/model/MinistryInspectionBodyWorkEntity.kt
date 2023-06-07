package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_MMINISTRY_INSPECTION_BODY_WORK")
class MinistryInspectionBodyWorkEntity : Serializable {
    @get:Column(name = "ID")
    @get:Id
    var id: Long = 0
    @get:Column(name = "STATUS")
    @get:Basic
    var status: Int? = null
    @get:Column(name = "GENERAR_INSPECTION")
    @get:Basic
    var generarInspection: Long? = null
    @get:Column(name = "OVERALL_APPEARANCE")
    @get:Basic
    var overallAppearance: String? = null
    @get:Column(name = "OVERALL_APPEARANCE_STATUS")
    @get:Basic
    var overallAppearanceStatus: String? = null
    @get:Column(name = "OVERALL_APPEARANCE_REMARKS")
    @get:Basic
    var overallAppearanceRemarks: String? = null
    @get:Column(name = "CONDITION_OF_PAINT")
    @get:Basic
    var conditionOfPaint: String? = null
    @get:Column(name = "CONDITION_OF_PAINT_STATUS")
    @get:Basic
    var conditionOfPaintStatus: String? = null
    @get:Column(name = "CONDITION_OF_PAINT_REMARKS")
    @get:Basic
    var conditionOfPaintRemarks: String? = null
    @get:Column(name = "DOORS")
    @get:Basic
    var doors: String? = null
    @get:Column(name = "DOORS_STATUS")
    @get:Basic
    var doorsStatus: String? = null
    @get:Column(name = "DOORS_REMARKS")
    @get:Basic
    var doorsRemarks: String? = null
    @get:Column(name = "WINDOWS")
    @get:Basic
    var windows: String? = null
    @get:Column(name = "WINDOWS_STATUS")
    @get:Basic
    var windowsStatus: String? = null
    @get:Column(name = "WINDOWS_REMARKS")
    @get:Basic
    var windowsRemarks: String? = null
    @get:Column(name = "SUNROOF")
    @get:Basic
    var sunroof: String? = null
    @get:Column(name = "SUNROOF_STATUS")
    @get:Basic
    var sunroofStatus: String? = null
    @get:Column(name = "SUNROOF_REMARKS")
    @get:Basic
    var sunroofRemarks: String? = null
    @get:Column(name = "EXTERNAL_MIRRORS")
    @get:Basic
    var externalMirrors: String? = null
    @get:Column(name = "EXTERNAL_MIRRORS_STATUS")
    @get:Basic
    var externalMirrorsStatus: String? = null
    @get:Column(name = "EXTERNAL_MIRRORS_REMARKS")
    @get:Basic
    var externalMirrorsRemarks: String? = null
    @get:Column(name = "GLASSES")
    @get:Basic
    var glasses: String? = null
    @get:Column(name = "GLASSES_STATUS")
    @get:Basic
    var glassesStatus: String? = null
    @get:Column(name = "GLASSES_REMARKS")
    @get:Basic
    var glassesRemarks: String? = null
    @get:Column(name = "WIPERS_AND_WASHERS")
    @get:Basic
    var wipersAndWashers: String? = null
    @get:Column(name = "WIPERS_AND_WASHERS_STATUS")
    @get:Basic
    var wipersAndWashersStatus: String? = null
    @get:Column(name = "WIPERS_AND_WASHERS_REMARKS")
    @get:Basic
    var wipersAndWashersRemarks: String? = null
    @get:Column(name = "SEATS")
    @get:Basic
    var seats: String? = null
    @get:Column(name = "SEATS_STATUS")
    @get:Basic
    var seatsStatus: String? = null
    @get:Column(name = "SEATS_REMARKS")
    @get:Basic
    var seatsRemarks: String? = null
    @get:Column(name = "MOULDING")
    @get:Basic
    var moulding: String? = null
    @get:Column(name = "MOULDING_STATUS")
    @get:Basic
    var mouldingStatus: String? = null
    @get:Column(name = "MOULDING_REMARKS")
    @get:Basic
    var mouldingRemarks: String? = null
    @get:Column(name = "SAFETY_BELTS")
    @get:Basic
    var safetyBelts: String? = null
    @get:Column(name = "SAFETY_BELTS_STATUS")
    @get:Basic
    var safetyBeltsStatus: String? = null
    @get:Column(name = "SAFETY_BELTS_REMARKS")
    @get:Basic
    var safetyBeltsRemarks: String? = null
    @get:Column(name = "STEERING_WHEEL")
    @get:Basic
    var steeringWheel: String? = null
    @get:Column(name = "STEERING_WHEEL_STATUS")
    @get:Basic
    var steeringWheelStatus: String? = null
    @get:Column(name = "STEERING_WHEEL_REMARKS")
    @get:Basic
    var steeringWheelRemarks: String? = null
    @get:Column(name = "BRAKE_PEDAL")
    @get:Basic
    var brakePedal: String? = null
    @get:Column(name = "BRAKE_PEDAL_STATUS")
    @get:Basic
    var brakePedalStatus: String? = null
    @get:Column(name = "BRAKE_PEDAL_REMARKS")
    @get:Basic
    var brakePedalRemarks: String? = null
    @get:Column(name = "CLUTCH_PEDAL")
    @get:Basic
    var clutchPedal: String? = null
    @get:Column(name = "CLUTCH_PEDAL_STATUS")
    @get:Basic
    var clutchPedalStatus: String? = null
    @get:Column(name = "CLUTCH_PEDAL_REMARKS")
    @get:Basic
    var clutchPedalRemarks: String? = null
    @get:Column(name = "PARKING_BRAKE_LEVER")
    @get:Basic
    var parkingBrakeLever: String? = null
    @get:Column(name = "PARKING_BRAKE_LEVER_STATUS")
    @get:Basic
    var parkingBrakeLeverStatus: String? = null
    @get:Column(name = "PARKING_BRAKE_LEVER_REMARKS")
    @get:Basic
    var parkingBrakeLeverRemarks: String? = null
    @get:Column(name = "HEADLIGHTS")
    @get:Basic
    var headlights: String? = null
    @get:Column(name = "HEADLIGHTS_STATUS")
    @get:Basic
    var headlightsStatus: String? = null
    @get:Column(name = "HEADLIGHTS_REMARKS")
    @get:Basic
    var headlightsRemarks: String? = null
    @get:Column(name = "PARKING_LIGHTS")
    @get:Basic
    var parkingLights: String? = null
    @get:Column(name = "PARKING_LIGHTS_STATUS")
    @get:Basic
    var parkingLightsStatus: String? = null
    @get:Column(name = "PARKING_LIGHTS_REMARKS")
    @get:Basic
    var parkingLightsRemarks: String? = null
    @get:Column(name = "DIRECTION_INDICATORS")
    @get:Basic
    var directionIndicators: String? = null
    @get:Column(name = "DIRECTION_INDICATORS_STATUS")
    @get:Basic
    var directionIndicatorsStatus: String? = null
    @get:Column(name = "DIRECTION_INDICATORS_REMARKS")
    @get:Basic
    var directionIndicatorsRemarks: String? = null
    @get:Column(name = "REVERSING_LIGHT")
    @get:Basic
    var reversingLight: String? = null
    @get:Column(name = "REVERSING_LIGHT_STATUS")
    @get:Basic
    var reversingLightStatus: String? = null
    @get:Column(name = "REVERSING_LIGHT_REMARKS")
    @get:Basic
    var reversingLightRemarks: String? = null
    @get:Column(name = "COURTESY_LIGHT")
    @get:Basic
    var courtesyLight: String? = null
    @get:Column(name = "COURTESY_LIGHT_STATUS")
    @get:Basic
    var courtesyLightStatus: String? = null
    @get:Column(name = "COURTESY_LIGHT_REMARKS")
    @get:Basic
    var courtesyLightRemarks: String? = null
    @get:Column(name = "REAR_NO_PLATE_LIGHT")
    @get:Basic
    var rearNoPlateLight: String? = null
    @get:Column(name = "REAR_NO_PLATE_LIGHT_REMARKS")
    @get:Basic
    var rearNoPlateLightRemarks: String? = null
    @get:Column(name = "REAR_NO_PLATE_LIGHT_STATUS")
    @get:Basic
    var rearNoPlateLightStatus: String? = null
    @get:Column(name = "STOP_LIGHTS")
    @get:Basic
    var stopLights: String? = null
    @get:Column(name = "STOP_LIGHTS_STATUS")
    @get:Basic
    var stopLightsStatus: String? = null
    @get:Column(name = "STOP_LIGHTS_REMARKS")
    @get:Basic
    var stopLightsRemarks: String? = null
    @get:Column(name = "FRONT_BUMPER")
    @get:Basic
    var frontBumper: String? = null
    @get:Column(name = "FRONT_BUMPER_STATUS")
    @get:Basic
    var frontBumperStatus: String? = null
    @get:Column(name = "FRONT_BUMPER_REMARKS")
    @get:Basic
    var frontBumperRemarks: String? = null
    @get:Column(name = "ROOF_RACK")
    @get:Basic
    var roofRack: String? = null
    @get:Column(name = "ROOF_RACK_STATUS")
    @get:Basic
    var roofRackStatus: String? = null
    @get:Column(name = "ROOF_RACK_REMARKS")
    @get:Basic
    var roofRackRemarks: String? = null
    @get:Column(name = "ANTENNA")
    @get:Basic
    var antenna: String? = null
    @get:Column(name = "ANTENNA_STATUS")
    @get:Basic
    var antennaStatus: String? = null
    @get:Column(name = "ANTENNA_REMARKS")
    @get:Basic
    var antennaRemarks: String? = null
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
        val that = o as MinistryInspectionBodyWorkEntity
        return id == that.id &&
                status == that.status &&
                generarInspection == that.generarInspection &&
                overallAppearance == that.overallAppearance &&
                overallAppearanceStatus == that.overallAppearanceStatus &&
                overallAppearanceRemarks == that.overallAppearanceRemarks &&
                conditionOfPaint == that.conditionOfPaint &&
                conditionOfPaintStatus == that.conditionOfPaintStatus &&
                conditionOfPaintRemarks == that.conditionOfPaintRemarks &&
                doors == that.doors &&
                doorsStatus == that.doorsStatus &&
                doorsRemarks == that.doorsRemarks &&
                windows == that.windows &&
                windowsStatus == that.windowsStatus &&
                windowsRemarks == that.windowsRemarks &&
                sunroof == that.sunroof &&
                sunroofStatus == that.sunroofStatus &&
                sunroofRemarks == that.sunroofRemarks &&
                externalMirrors == that.externalMirrors &&
                externalMirrorsStatus == that.externalMirrorsStatus &&
                externalMirrorsRemarks == that.externalMirrorsRemarks &&
                glasses == that.glasses &&
                glassesStatus == that.glassesStatus &&
                glassesRemarks == that.glassesRemarks &&
                wipersAndWashers == that.wipersAndWashers &&
                wipersAndWashersStatus == that.wipersAndWashersStatus &&
                wipersAndWashersRemarks == that.wipersAndWashersRemarks &&
                seats == that.seats &&
                seatsStatus == that.seatsStatus &&
                seatsRemarks == that.seatsRemarks &&
                moulding == that.moulding &&
                mouldingStatus == that.mouldingStatus &&
                mouldingRemarks == that.mouldingRemarks &&
                safetyBelts == that.safetyBelts &&
                safetyBeltsStatus == that.safetyBeltsStatus &&
                safetyBeltsRemarks == that.safetyBeltsRemarks &&
                steeringWheel == that.steeringWheel &&
                steeringWheelStatus == that.steeringWheelStatus &&
                steeringWheelRemarks == that.steeringWheelRemarks &&
                brakePedal == that.brakePedal &&
                brakePedalStatus == that.brakePedalStatus &&
                brakePedalRemarks == that.brakePedalRemarks &&
                clutchPedal == that.clutchPedal &&
                clutchPedalStatus == that.clutchPedalStatus &&
                clutchPedalRemarks == that.clutchPedalRemarks &&
                parkingBrakeLever == that.parkingBrakeLever &&
                parkingBrakeLeverStatus == that.parkingBrakeLeverStatus &&
                parkingBrakeLeverRemarks == that.parkingBrakeLeverRemarks &&
                headlights == that.headlights &&
                headlightsStatus == that.headlightsStatus &&
                headlightsRemarks == that.headlightsRemarks &&
                parkingLights == that.parkingLights &&
                parkingLightsStatus == that.parkingLightsStatus &&
                parkingLightsRemarks == that.parkingLightsRemarks &&
                directionIndicators == that.directionIndicators &&
                directionIndicatorsStatus == that.directionIndicatorsStatus &&
                directionIndicatorsRemarks == that.directionIndicatorsRemarks &&
                reversingLight == that.reversingLight &&
                reversingLightStatus == that.reversingLightStatus &&
                reversingLightRemarks == that.reversingLightRemarks &&
                courtesyLight == that.courtesyLight &&
                courtesyLightStatus == that.courtesyLightStatus &&
                courtesyLightRemarks == that.courtesyLightRemarks &&
                rearNoPlateLight == that.rearNoPlateLight &&
                rearNoPlateLightRemarks == that.rearNoPlateLightRemarks &&
                rearNoPlateLightStatus == that.rearNoPlateLightStatus &&
                stopLights == that.stopLights &&
                stopLightsStatus == that.stopLightsStatus &&
                stopLightsRemarks == that.stopLightsRemarks &&
                frontBumper == that.frontBumper &&
                frontBumperStatus == that.frontBumperStatus &&
                frontBumperRemarks == that.frontBumperRemarks &&
                roofRack == that.roofRack &&
                roofRackStatus == that.roofRackStatus &&
                roofRackRemarks == that.roofRackRemarks &&
                antenna == that.antenna &&
                antennaStatus == that.antennaStatus &&
                antennaRemarks == that.antennaRemarks &&
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
        return javaClass.hashCode()
    }


}