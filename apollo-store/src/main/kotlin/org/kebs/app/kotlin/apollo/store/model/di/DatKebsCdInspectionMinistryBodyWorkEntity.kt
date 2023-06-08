package org.kebs.app.kotlin.apollo.store.model.di

import java.sql.Timestamp
import java.util.*
import javax.persistence.*

//@Entity
//@Table(name = "DAT_KEBS_CD_INSPECTION_MINISTRY_BODY_WORK", schema = "APOLLO", catalog = "")
class DatKebsCdInspectionMinistryBodyWorkEntity {
    @get:Column(name = "ID")
    @get:Id
    var id: Long = 0

    @get:Column(name = "OVERALL_APPEARANCE_STATUS")
    @get:Basic
    var overallAppearanceStatus: String? = null

    @get:Column(name = "OVERALL_APPEARANCE_REMARKS")
    @get:Basic
    var overallAppearanceRemarks: String? = null

    @get:Column(name = "OVERALL_APPEARANCE_FIRST_REINSPECTION")
    @get:Basic
    var overallAppearanceFirstReinspection: String? = null

    @get:Column(name = "OVERALL_APPEARANCE_SECOND_REINSPECTION")
    @get:Basic
    var overallAppearanceSecondReinspection: String? = null

    @get:Column(name = "CONDITION_OF_PAINT_STATUS")
    @get:Basic
    var conditionOfPaintStatus: String? = null

    @get:Column(name = "CONDITION_OF_PAINT_REMARKS")
    @get:Basic
    var conditionOfPaintRemarks: String? = null

    @get:Column(name = "CONDITION_OF_PAINT_FIRST_REINSPECTION")
    @get:Basic
    var conditionOfPaintFirstReinspection: String? = null

    @get:Column(name = "CONDITION_OF_PAINT_SECOND_REINSPECTION")
    @get:Basic
    var conditionOfPaintSecondReinspection: String? = null

    @get:Column(name = "DOORS_STATUS")
    @get:Basic
    var doorsStatus: String? = null

    @get:Column(name = "DOORS_REMARKS")
    @get:Basic
    var doorsRemarks: String? = null

    @get:Column(name = "DOORS_FIRST_REINSPECTION")
    @get:Basic
    var doorsFirstReinspection: String? = null

    @get:Column(name = "DOORS_SECOND_REINSPECTION")
    @get:Basic
    var doorsSecondReinspection: String? = null

    @get:Column(name = "WINDOWS_STATUS")
    @get:Basic
    var windowsStatus: String? = null

    @get:Column(name = "WINDOWS_REMARKS")
    @get:Basic
    var windowsRemarks: String? = null

    @get:Column(name = "WINDOWS_FIRST_REINSPECTION")
    @get:Basic
    var windowsFirstReinspection: String? = null

    @get:Column(name = "WINDOWS_SECOND_REINSPECTION")
    @get:Basic
    var windowsSecondReinspection: String? = null

    @get:Column(name = "SUNROOF_STATUS")
    @get:Basic
    var sunroofStatus: String? = null

    @get:Column(name = "SUNROOF_REMARKS")
    @get:Basic
    var sunroofRemarks: String? = null

    @get:Column(name = "SUNROOF_FIRST_REINSPECTION")
    @get:Basic
    var sunroofFirstReinspection: String? = null

    @get:Column(name = "SUNROOF_SECOND_REINSPECTION")
    @get:Basic
    var sunroofSecondReinspection: String? = null

    @get:Column(name = "EXTERNAL_MIRRORS_STATUS")
    @get:Basic
    var externalMirrorsStatus: String? = null

    @get:Column(name = "EXTERNAL_MIRRORS_REMARKS")
    @get:Basic
    var externalMirrorsRemarks: String? = null

    @get:Column(name = "EXTERNAL_MIRRORS_FIRST_REINSPECTION")
    @get:Basic
    var externalMirrorsFirstReinspection: String? = null

    @get:Column(name = "EXTERNAL_MIRRORS_SECOND_REINSPECTION")
    @get:Basic
    var externalMirrorsSecondReinspection: String? = null

    @get:Column(name = "GLASSES_STATUS")
    @get:Basic
    var glassesStatus: String? = null

    @get:Column(name = "GLASSES_REMARKS")
    @get:Basic
    var glassesRemarks: String? = null

    @get:Column(name = "GLASSES_FIRST_REINSPECTION")
    @get:Basic
    var glassesFirstReinspection: String? = null

    @get:Column(name = "GLASSES_SECOND_REINSPECTION")
    @get:Basic
    var glassesSecondReinspection: String? = null

    @get:Column(name = "WIPERS_AND_WASHERS_STATUS")
    @get:Basic
    var wipersAndWashersStatus: String? = null

    @get:Column(name = "WIPERS_AND_WASHERS_REMARKS")
    @get:Basic
    var wipersAndWashersRemarks: String? = null

    @get:Column(name = "WIPERS_AND_WASHERS_FIRST_REINSPECTION")
    @get:Basic
    var wipersAndWashersFirstReinspection: String? = null

    @get:Column(name = "WIPERS_AND_WASHERS_SECOND_REINSPECTION")
    @get:Basic
    var wipersAndWashersSecondReinspection: String? = null

    @get:Column(name = "SEATS_STATUS")
    @get:Basic
    var seatsStatus: String? = null

    @get:Column(name = "SEATS_REMARKS")
    @get:Basic
    var seatsRemarks: String? = null

    @get:Column(name = "SEATS_FIRST_REINSPECTION")
    @get:Basic
    var seatsFirstReinspection: String? = null

    @get:Column(name = "SEATS_SECOND_REINSPECTION")
    @get:Basic
    var seatsSecondReinspection: String? = null

    @get:Column(name = "MOULDING_STATUS")
    @get:Basic
    var mouldingStatus: String? = null

    @get:Column(name = "MOULDING_REMARKS")
    @get:Basic
    var mouldingRemarks: String? = null

    @get:Column(name = "MOULDING_FIRST_REINSPECTION")
    @get:Basic
    var mouldingFirstReinspection: String? = null

    @get:Column(name = "MOULDING_SECOND_REINSPECTION")
    @get:Basic
    var mouldingSecondReinspection: String? = null

    @get:Column(name = "SAFETY_BELTS_STATUS")
    @get:Basic
    var safetyBeltsStatus: String? = null

    @get:Column(name = "SAFETY_BELTS_REMARKS")
    @get:Basic
    var safetyBeltsRemarks: String? = null

    @get:Column(name = "SAFETY_BELTS_FIRST_REINSPECTION")
    @get:Basic
    var safetyBeltsFirstReinspection: String? = null

    @get:Column(name = "SAFETY_BELTS_SECOND_REINSPECTION")
    @get:Basic
    var safetyBeltsSecondReinspection: String? = null

    @get:Column(name = "STEERING_WHEEL_STATUS")
    @get:Basic
    var steeringWheelStatus: String? = null

    @get:Column(name = "STEERING_WHEEL_REMARKS")
    @get:Basic
    var steeringWheelRemarks: String? = null

    @get:Column(name = "STEERING_WHEEL_FIRST_REINSPECTION")
    @get:Basic
    var steeringWheelFirstReinspection: String? = null

    @get:Column(name = "STEERING_WHEEL_SECOND_REINSPECTION")
    @get:Basic
    var steeringWheelSecondReinspection: String? = null

    @get:Column(name = "BRAKE_PEDAL_STATUS")
    @get:Basic
    var brakePedalStatus: String? = null

    @get:Column(name = "BRAKE_PEDAL_REMARKS")
    @get:Basic
    var brakePedalRemarks: String? = null

    @get:Column(name = "BRAKE_PEDAL_FIRST_REINSPECTION")
    @get:Basic
    var brakePedalFirstReinspection: String? = null

    @get:Column(name = "BRAKE_PEDAL_SECOND_REINSPECTION")
    @get:Basic
    var brakePedalSecondReinspection: String? = null

    @get:Column(name = "CLUTCH_PEDAL_STATUS")
    @get:Basic
    var clutchPedalStatus: String? = null

    @get:Column(name = "CLUTCH_PEDAL_REMARKS")
    @get:Basic
    var clutchPedalRemarks: String? = null

    @get:Column(name = "CLUTCH_PEDAL_FIRST_REINSPECTION")
    @get:Basic
    var clutchPedalFirstReinspection: String? = null

    @get:Column(name = "CLUTCH_PEDAL_SECOND_REINSPECTION")
    @get:Basic
    var clutchPedalSecondReinspection: String? = null

    @get:Column(name = "PARKING_BRAKE_LEVER_STATUS")
    @get:Basic
    var parkingBrakeLeverStatus: String? = null

    @get:Column(name = "PARKING_BRAKE_LEVER_REMARKS")
    @get:Basic
    var parkingBrakeLeverRemarks: String? = null

    @get:Column(name = "PARKING_BRAKE_FIRST_REINSPECTION")
    @get:Basic
    var parkingBrakeFirstReinspection: String? = null

    @get:Column(name = "PARKING_BRAKE_SECOND_REINSPECTION")
    @get:Basic
    var parkingBrakeSecondReinspection: String? = null

    @get:Column(name = "HEADLIGHTS_STATUS")
    @get:Basic
    var headlightsStatus: String? = null

    @get:Column(name = "HEADLIGHTS_REMARKS")
    @get:Basic
    var headlightsRemarks: String? = null

    @get:Column(name = "HEADLIGHTS_FIRST_REINSPECTION")
    @get:Basic
    var headlightsFirstReinspection: String? = null

    @get:Column(name = "HEADLIGHTS_SECOND_REINSPECTION")
    @get:Basic
    var headlightsSecondReinspection: String? = null

    @get:Column(name = "PARKING_LIGHTS_STATUS")
    @get:Basic
    var parkingLightsStatus: String? = null

    @get:Column(name = "PARKING_LIGHTS_REMARKS")
    @get:Basic
    var parkingLightsRemarks: String? = null

    @get:Column(name = "PARKING_LIGHTS_FIRST_REINSPECTION")
    @get:Basic
    var parkingLightsFirstReinspection: String? = null

    @get:Column(name = "PARKING_LIGHTS_SECOND_REINSPECTION")
    @get:Basic
    var parkingLightsSecondReinspection: String? = null

    @get:Column(name = "DIRECTION_INDICATORS_STATUS")
    @get:Basic
    var directionIndicatorsStatus: String? = null

    @get:Column(name = "DIRECTION_INDICATORS_REMARKS")
    @get:Basic
    var directionIndicatorsRemarks: String? = null

    @get:Column(name = "DIRECTION_INDICATORS_FIRST_REINSPECTION")
    @get:Basic
    var directionIndicatorsFirstReinspection: String? = null

    @get:Column(name = "DIRECTION_INDICATORS_SECOND_REINSPECTION")
    @get:Basic
    var directionIndicatorsSecondReinspection: String? = null

    @get:Column(name = "REVERSING_LIGHT_STATUS")
    @get:Basic
    var reversingLightStatus: String? = null

    @get:Column(name = "REVERSING_LIGHT_REMARKS")
    @get:Basic
    var reversingLightRemarks: String? = null

    @get:Column(name = "REVERSING_LIGHT_FIRST_REINSPECTION")
    @get:Basic
    var reversingLightFirstReinspection: String? = null

    @get:Column(name = "REVERSING_LIGHT_SECOND_REINSPECTION")
    @get:Basic
    var reversingLightSecondReinspection: String? = null

    @get:Column(name = "COURTESY_LIGHT_STATUS")
    @get:Basic
    var courtesyLightStatus: String? = null

    @get:Column(name = "COURTESY_LIGHT_REMARKS")
    @get:Basic
    var courtesyLightRemarks: String? = null

    @get:Column(name = "COURTESY_LIGHT_FIRST_REINSPECTION")
    @get:Basic
    var courtesyLightFirstReinspection: String? = null

    @get:Column(name = "COURTESY_LIGHT_SECOND_REINSPECTION")
    @get:Basic
    var courtesyLightSecondReinspection: String? = null

    @get:Column(name = "REAR_NO_PLATE_LIGHT_REMARKS")
    @get:Basic
    var rearNoPlateLightRemarks: String? = null

    @get:Column(name = "REAR_NO_PLATE_LIGHT_STATUS")
    @get:Basic
    var rearNoPlateLightStatus: String? = null

    @get:Column(name = "REAR_NO_PLATE_LIGHT_FIRST_REINSPECTION")
    @get:Basic
    var rearNoPlateLightFirstReinspection: String? = null

    @get:Column(name = "REAR_NO_PLATE_LIGHT_SECOND_REINSPECTION")
    @get:Basic
    var rearNoPlateLightSecondReinspection: String? = null

    @get:Column(name = "STOP_LIGHTS_STATUS")
    @get:Basic
    var stopLightsStatus: String? = null

    @get:Column(name = "STOP_LIGHTS_REMARKS")
    @get:Basic
    var stopLightsRemarks: String? = null

    @get:Column(name = "STOP_LIGHTS_FIRST_REINSPECTION")
    @get:Basic
    var stopLightsFirstReinspection: String? = null

    @get:Column(name = "STOP_LIGHTS_SECOND_REINSPECTION")
    @get:Basic
    var stopLightsSecondReinspection: String? = null

    @get:Column(name = "FRONT_BUMPER_STATUS")
    @get:Basic
    var frontBumperStatus: String? = null

    @get:Column(name = "FRONT_BUMPER_REMARKS")
    @get:Basic
    var frontBumperRemarks: String? = null

    @get:Column(name = "FRONT_BUMPER_FIRST_REINSPECTION")
    @get:Basic
    var frontBumperFirstReinspection: String? = null

    @get:Column(name = "FRONT_BUMPER_SECOND_REINSPECTION")
    @get:Basic
    var frontBumperSecondReinspection: String? = null

    @get:Column(name = "ROOF_RACK_STATUS")
    @get:Basic
    var roofRackStatus: String? = null

    @get:Column(name = "ROOF_RACK_REMARKS")
    @get:Basic
    var roofRackRemarks: String? = null

    @get:Column(name = "ROOF_RACK_FIRST_REINSPECTION")
    @get:Basic
    var roofRackFirstReinspection: String? = null

    @get:Column(name = "ROOF_RACK_SECOND_REINSPECTION")
    @get:Basic
    var roofRackSecondReinspection: String? = null

    @get:Column(name = "ANTENNA_STATUS")
    @get:Basic
    var antennaStatus: String? = null

    @get:Column(name = "ANTENNA_REMARKS")
    @get:Basic
    var antennaRemarks: String? = null

    @get:Column(name = "ANTENNA_FIRST_REINSPECTION")
    @get:Basic
    var antennaFirstReinspection: String? = null

    @get:Column(name = "ANTENNA_SECOND_REINSPECTION")
    @get:Basic
    var antennaSecondReinspection: String? = null

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
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as DatKebsCdInspectionMinistryBodyWorkEntity
        return id == that.id &&
                overallAppearanceStatus == that.overallAppearanceStatus &&
                overallAppearanceRemarks == that.overallAppearanceRemarks &&
                overallAppearanceFirstReinspection == that.overallAppearanceFirstReinspection &&
                overallAppearanceSecondReinspection == that.overallAppearanceSecondReinspection &&
                conditionOfPaintStatus == that.conditionOfPaintStatus &&
                conditionOfPaintRemarks == that.conditionOfPaintRemarks &&
                conditionOfPaintFirstReinspection == that.conditionOfPaintFirstReinspection &&
                conditionOfPaintSecondReinspection == that.conditionOfPaintSecondReinspection &&
                doorsStatus == that.doorsStatus &&
                doorsRemarks == that.doorsRemarks &&
                doorsFirstReinspection == that.doorsFirstReinspection &&
                doorsSecondReinspection == that.doorsSecondReinspection &&
                windowsStatus == that.windowsStatus &&
                windowsRemarks == that.windowsRemarks &&
                windowsFirstReinspection == that.windowsFirstReinspection &&
                windowsSecondReinspection == that.windowsSecondReinspection &&
                sunroofStatus == that.sunroofStatus &&
                sunroofRemarks == that.sunroofRemarks &&
                sunroofFirstReinspection == that.sunroofFirstReinspection &&
                sunroofSecondReinspection == that.sunroofSecondReinspection &&
                externalMirrorsStatus == that.externalMirrorsStatus &&
                externalMirrorsRemarks == that.externalMirrorsRemarks &&
                externalMirrorsFirstReinspection == that.externalMirrorsFirstReinspection &&
                externalMirrorsSecondReinspection == that.externalMirrorsSecondReinspection &&
                glassesStatus == that.glassesStatus &&
                glassesRemarks == that.glassesRemarks &&
                glassesFirstReinspection == that.glassesFirstReinspection &&
                glassesSecondReinspection == that.glassesSecondReinspection &&
                wipersAndWashersStatus == that.wipersAndWashersStatus &&
                wipersAndWashersRemarks == that.wipersAndWashersRemarks &&
                wipersAndWashersFirstReinspection == that.wipersAndWashersFirstReinspection &&
                wipersAndWashersSecondReinspection == that.wipersAndWashersSecondReinspection &&
                seatsStatus == that.seatsStatus &&
                seatsRemarks == that.seatsRemarks &&
                seatsFirstReinspection == that.seatsFirstReinspection &&
                seatsSecondReinspection == that.seatsSecondReinspection &&
                mouldingStatus == that.mouldingStatus &&
                mouldingRemarks == that.mouldingRemarks &&
                mouldingFirstReinspection == that.mouldingFirstReinspection &&
                mouldingSecondReinspection == that.mouldingSecondReinspection &&
                safetyBeltsStatus == that.safetyBeltsStatus &&
                safetyBeltsRemarks == that.safetyBeltsRemarks &&
                safetyBeltsFirstReinspection == that.safetyBeltsFirstReinspection &&
                safetyBeltsSecondReinspection == that.safetyBeltsSecondReinspection &&
                steeringWheelStatus == that.steeringWheelStatus &&
                steeringWheelRemarks == that.steeringWheelRemarks &&
                steeringWheelFirstReinspection == that.steeringWheelFirstReinspection &&
                steeringWheelSecondReinspection == that.steeringWheelSecondReinspection &&
                brakePedalStatus == that.brakePedalStatus &&
                brakePedalRemarks == that.brakePedalRemarks &&
                brakePedalFirstReinspection == that.brakePedalFirstReinspection &&
                brakePedalSecondReinspection == that.brakePedalSecondReinspection &&
                clutchPedalStatus == that.clutchPedalStatus &&
                clutchPedalRemarks == that.clutchPedalRemarks &&
                clutchPedalFirstReinspection == that.clutchPedalFirstReinspection &&
                clutchPedalSecondReinspection == that.clutchPedalSecondReinspection &&
                parkingBrakeLeverStatus == that.parkingBrakeLeverStatus &&
                parkingBrakeLeverRemarks == that.parkingBrakeLeverRemarks &&
                parkingBrakeFirstReinspection == that.parkingBrakeFirstReinspection &&
                parkingBrakeSecondReinspection == that.parkingBrakeSecondReinspection &&
                headlightsStatus == that.headlightsStatus &&
                headlightsRemarks == that.headlightsRemarks &&
                headlightsFirstReinspection == that.headlightsFirstReinspection &&
                headlightsSecondReinspection == that.headlightsSecondReinspection &&
                parkingLightsStatus == that.parkingLightsStatus &&
                parkingLightsRemarks == that.parkingLightsRemarks &&
                parkingLightsFirstReinspection == that.parkingLightsFirstReinspection &&
                parkingLightsSecondReinspection == that.parkingLightsSecondReinspection &&
                directionIndicatorsStatus == that.directionIndicatorsStatus &&
                directionIndicatorsRemarks == that.directionIndicatorsRemarks &&
                directionIndicatorsFirstReinspection == that.directionIndicatorsFirstReinspection &&
                directionIndicatorsSecondReinspection == that.directionIndicatorsSecondReinspection &&
                reversingLightStatus == that.reversingLightStatus &&
                reversingLightRemarks == that.reversingLightRemarks &&
                reversingLightFirstReinspection == that.reversingLightFirstReinspection &&
                reversingLightSecondReinspection == that.reversingLightSecondReinspection &&
                courtesyLightStatus == that.courtesyLightStatus &&
                courtesyLightRemarks == that.courtesyLightRemarks &&
                courtesyLightFirstReinspection == that.courtesyLightFirstReinspection &&
                courtesyLightSecondReinspection == that.courtesyLightSecondReinspection &&
                rearNoPlateLightRemarks == that.rearNoPlateLightRemarks &&
                rearNoPlateLightStatus == that.rearNoPlateLightStatus &&
                rearNoPlateLightFirstReinspection == that.rearNoPlateLightFirstReinspection &&
                rearNoPlateLightSecondReinspection == that.rearNoPlateLightSecondReinspection &&
                stopLightsStatus == that.stopLightsStatus &&
                stopLightsRemarks == that.stopLightsRemarks &&
                stopLightsFirstReinspection == that.stopLightsFirstReinspection &&
                stopLightsSecondReinspection == that.stopLightsSecondReinspection &&
                frontBumperStatus == that.frontBumperStatus &&
                frontBumperRemarks == that.frontBumperRemarks &&
                frontBumperFirstReinspection == that.frontBumperFirstReinspection &&
                frontBumperSecondReinspection == that.frontBumperSecondReinspection &&
                roofRackStatus == that.roofRackStatus &&
                roofRackRemarks == that.roofRackRemarks &&
                roofRackFirstReinspection == that.roofRackFirstReinspection &&
                roofRackSecondReinspection == that.roofRackSecondReinspection &&
                antennaStatus == that.antennaStatus &&
                antennaRemarks == that.antennaRemarks &&
                antennaFirstReinspection == that.antennaFirstReinspection &&
                antennaSecondReinspection == that.antennaSecondReinspection &&
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
        return Objects.hash(id, overallAppearanceStatus, overallAppearanceRemarks, overallAppearanceFirstReinspection, overallAppearanceSecondReinspection, conditionOfPaintStatus, conditionOfPaintRemarks, conditionOfPaintFirstReinspection, conditionOfPaintSecondReinspection, doorsStatus, doorsRemarks, doorsFirstReinspection, doorsSecondReinspection, windowsStatus, windowsRemarks, windowsFirstReinspection, windowsSecondReinspection, sunroofStatus, sunroofRemarks, sunroofFirstReinspection, sunroofSecondReinspection, externalMirrorsStatus, externalMirrorsRemarks, externalMirrorsFirstReinspection, externalMirrorsSecondReinspection, glassesStatus, glassesRemarks, glassesFirstReinspection, glassesSecondReinspection, wipersAndWashersStatus, wipersAndWashersRemarks, wipersAndWashersFirstReinspection, wipersAndWashersSecondReinspection, seatsStatus, seatsRemarks, seatsFirstReinspection, seatsSecondReinspection, mouldingStatus, mouldingRemarks, mouldingFirstReinspection, mouldingSecondReinspection, safetyBeltsStatus, safetyBeltsRemarks, safetyBeltsFirstReinspection, safetyBeltsSecondReinspection, steeringWheelStatus, steeringWheelRemarks, steeringWheelFirstReinspection, steeringWheelSecondReinspection, brakePedalStatus, brakePedalRemarks, brakePedalFirstReinspection, brakePedalSecondReinspection, clutchPedalStatus, clutchPedalRemarks, clutchPedalFirstReinspection, clutchPedalSecondReinspection, parkingBrakeLeverStatus, parkingBrakeLeverRemarks, parkingBrakeFirstReinspection, parkingBrakeSecondReinspection, headlightsStatus, headlightsRemarks, headlightsFirstReinspection, headlightsSecondReinspection, parkingLightsStatus, parkingLightsRemarks, parkingLightsFirstReinspection, parkingLightsSecondReinspection, directionIndicatorsStatus, directionIndicatorsRemarks, directionIndicatorsFirstReinspection, directionIndicatorsSecondReinspection, reversingLightStatus, reversingLightRemarks, reversingLightFirstReinspection, reversingLightSecondReinspection, courtesyLightStatus, courtesyLightRemarks, courtesyLightFirstReinspection, courtesyLightSecondReinspection, rearNoPlateLightRemarks, rearNoPlateLightStatus, rearNoPlateLightFirstReinspection, rearNoPlateLightSecondReinspection, stopLightsStatus, stopLightsRemarks, stopLightsFirstReinspection, stopLightsSecondReinspection, frontBumperStatus, frontBumperRemarks, frontBumperFirstReinspection, frontBumperSecondReinspection, roofRackStatus, roofRackRemarks, roofRackFirstReinspection, roofRackSecondReinspection, antennaStatus, antennaRemarks, antennaFirstReinspection, antennaSecondReinspection, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn)
    }
}
