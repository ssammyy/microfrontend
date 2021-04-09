package org.kebs.app.kotlin.apollo.store.model;

import java.io.Serializable
import java.sql.Date
import java.sql.Time
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_MINISTRY_INSPECTION_MOTOR")
class MinistryInspectionMotorEntity : Serializable  {
    @Id
    @Column(name = "ID")
    var id: Long? = 0

    @Column(name = "OVERALL_APPEARANCE")
    @Basic
    var overallAppearance: String? = null


    @Column(name = "OVERALL_APPEARANCE_STATUS")
    @Basic
    var overallAppearanceStatus: String? = null


    @Column(name = "OVERALL_APPEARANCE_REMARKS")
    @Basic
    var overallAppearanceRemarks: String? = null


    @Column(name = "CONDITION_OF_PAINT")
    @Basic
    var conditionOfPaint: String? = null


    @Column(name = "CONDITION_OF_PAINT_STATUS")
    @Basic
    var conditionOfPaintStatus: String? = null


    @Column(name = "CONDITION_OF_PAINT_REMARKS")
    @Basic
    var conditionOfPaintRemarks: String? = null


    @Column(name = "DOORS")
    @Basic
    var doors: String? = null


    @Column(name = "DOORS_STATUS")
    @Basic
    var doorsStatus: String? = null


    @Column(name = "DOORS_REMARKS")
    @Basic
    var doorsRemarks: String? = null


    @Column(name = "WINDOWS")
    @Basic
    var windows: String? = null


    @Column(name = "WINDOWS_STATUS")
    @Basic
    var windowsStatus: String? = null


    @Column(name = "WINDOWS_REMARKS")
    @Basic
    var windowsRemarks: String? = null


    @Column(name = "SUNROOF")
    @Basic
    var sunroof: String? = null


    @Column(name = "SUNROOF_REMARKS")
    @Basic
    var sunroofRemarks: String? = null


    @Column(name = "SUNROOF_STATUS")
    @Basic
    var sunroofStatus: String? = null


    @Column(name = "EXTERNAL_MIRRORS")
    @Basic
    var externalMirrors: String? = null


    @Column(name = "EXTERNAL_MIRRORS_STATUS")
    @Basic
    var externalMirrorsStatus: String? = null


    @Column(name = "EXTERNAL_MIRRORS_REMARKS")
    @Basic
    var externalMirrorsRemarks: String? = null


    @Column(name = "GLASSES")
    @Basic
    var glasses: String? = null


    @Column(name = "GLASSES_STATUS")
    @Basic
    var glassesStatus: String? = null


    @Column(name = "GLASSES_REMARKS")
    @Basic
    var glassesRemarks: String? = null


    @Column(name = "WIPERS_WASHERS")
    @Basic
    var wipersWashers: String? = null


    @Column(name = "WIPERS_WASHERS_STATUS")
    @Basic
    var wipersWashersStatus: String? = null


    @Column(name = "WIPERS_WASHERS_REMARKS")
    @Basic
    var wipersWashersRemarks: String? = null


    @Column(name = "SEATS")
    @Basic
    var seats: String? = null


    @Column(name = "SEATS_STATUS")
    @Basic
    var seatsStatus: String? = null


    @Column(name = "SEATS_REMARKS")
    @Basic
    var seatsRemarks: String? = null


    @Column(name = "TRIM_MOULDING")
    @Basic
    var trimMoulding: String? = null


    @Column(name = "TRIM_MOULDING_STATUS")
    @Basic
    var trimMouldingStatus: String? = null


    @Column(name = "TRIM_MOULDING_REMARKS")
    var trimMouldingRemarks: String? = null


    @Column(name = "SAFETY_BELTS")
    @Basic
    var safetyBelts: String? = null


    @Column(name = "SAFETY_BELTS_STATUS")
    @Basic
    var safetyBeltsStatus: String? = null


    @Column(name = "SAFETY_BELTS_REMARKS")
    @Basic
    var safetyBeltsRemarks: String? = null


    @Column(name = "STEERING_WHEEL")
    @Basic
    var steeringWheel: String? = null


    @Column(name = "STEERING_WHEEL_STATUS")
    @Basic
    var steeringWheelStatus: String? = null


    @Column(name = "STEERING_WHEEL_REMARKS")
    @Basic
    var steeringWheelRemarks: String? = null


    @Column(name = "BRAKE_PEDAL")
    @Basic
    var brakePedal: String? = null


    @Column(name = "BRAKE_PEDAL_STATUS")
    @Basic
    var brakePedalStatus: String? = null


    @Column(name = "BRAKE_PEDAL_REMARKS")
    @Basic
    var brakePedalRemarks: String? = null


    @Column(name = "CLUTCH_PEDAL")
    @Basic
    var clutchPedal: String? = null


    @Column(name = "CLUTCH_PEDAL_STATUS")
    @Basic
    var clutchPedalStatus: String? = null


    @Column(name = "CLUTCH_PEDAL_REMARKS")
    @Basic
    var clutchPedalRemarks: String? = null


    @Column(name = "PARKING_BRAKE_LEVER")
    @Basic
    var parkingBrakeLever: String? = null


    @Column(name = "PARKING_BRAKE_LEVER_STATUS")
    @Basic
    var parkingBrakeLeverStatus: String? = null


    @Column(name = "PARKING_BRAKE_LEVER_REMARKS")
    @Basic
    var parkingBrakeLeverRemarks: String? = null


    @Column(name = "HEADLIGHTS")
    @Basic
    var headlights: String? = null


    @Column(name = "HEADLIGHTS_STATUS")
    @Basic
    var headlightsStatus: String? = null


    @Column(name = "HEADLIGHTS_REMARKS")
    @Basic
    var headlightsRemarks: String? = null


    @Column(name = "PARKING_LIGHTS")
    @Basic
    var parkingLights: String? = null


    @Column(name = "PARKING_LIGHTS_STATUS")
    @Basic
    var parkingLightsStatus: String? = null


    @Column(name = "PARKING_LIGHTS_REMARKS")
    @Basic
    var parkingLightsRemarks: String? = null


    @Column(name = "DIRECTION_INDICATORS")
    @Basic
    var directionIndicators: String? = null


    @Column(name = "DIRECTION_INDICATORS_STATUS")
    @Basic
    var directionIndicatorsStatus: String? = null


    @Column(name = "DIRECTION_INDICATORS_REMARKS")
    @Basic
    var directionIndicatorsRemarks: String? = null


    @Column(name = "REVERSING_LIGHT")
    @Basic
    var reversingLight: String? = null


    @Column(name = "REVERSING_LIGHT_STATUS")
    @Basic
    var reversingLightStatus: String? = null


    @Column(name = "REVERSING_LIGHT_REMARKS")
    @Basic
    var reversingLightRemarks: String? = null


    @Column(name = "COURTESY_LIGHT")
    @Basic
    var courtesyLight: String? = null


    @Column(name = "COURTESY_LIGHT_STATUS")
    @Basic
    var courtesyLightStatus: String? = null


    @Column(name = "COURTESY_LIGHT_REMARKS")
    var courtesyLightRemarks: String? = null


    @Column(name = "REAR_NO_PLATE_LIGHT")
    var rearNoPlateLight: String? = null


    @Column(name = "REAR_NO_PLATE_LIGHT_STATUS")
    var rearNoPlateLightStatus: String? = null


    @Column(name = "REAR_NO_PLATE_LIGHT_REMARKS")
    var rearNoPlateLightRemarks: String? = null


    @Column(name = "STOP_LIGHTS")
    @Basic
    var stopLights: String? = null


    @Column(name = "STOP_LIGHTS_STATUS")
    @Basic
    var stopLightsStatus: String? = null


    @Column(name = "STOP_LIGHTS_REMARKS")
    @Basic
    var stopLightsRemarks: String? = null


    @Column(name = "FRONT_BUMBER")
    @Basic
    var frontBumber: String? = null


    @Column(name = "FRONT_BUMBER_STATUS")
    @Basic
    var frontBumberStatus: String? = null


    @Column(name = "FRONT_BUMBER_REMARKS")
    @Basic
    var frontBumberRemarks: String? = null


    @Column(name = "ROOF_RACK")
    @Basic
    var roofRack: String? = null


    @Column(name = "ROOF_RACK_STATUS")
    @Basic
    var roofRackStatus: String? = null


    @Column(name = "ROOF_RACK_REMARKS")
    @Basic
    var roofRackRemarks: String? = null


    @Column(name = "ANTENNA")
    @Basic
    var antenna: String? = null


    @Column(name = "ANTENNA_STATUS")
    @Basic
    var antennaStatus: String? = null


    @Column(name = "ANTENNA_REMARKS")
    @Basic
    var antennaRemarks: String? = null


    @Column(name = "BONNET")
    @Basic
    var bonnet: String? = null


    @Column(name = "BONNET_STATUS")
    @Basic
    var bonnetStatus: String? = null


    @Column(name = "BONNET_REMARKS")
    @Basic
    var bonnetRemarks: String? = null


    @Column(name = "ENGINE")
    @Basic
    var engine: String? = null


    @Column(name = "ENGINE_STATUS")
    @Basic
    var engineStatus: String? = null


    @Column(name = "ENGINE_REMARKS")
    @Basic
    var engineRemarks: String? = null


    @Column(name = "BATTERY")
    @Basic
    var battery: String? = null


    @Column(name = "BATTERY_STATUS")
    @Basic
    var batteryStatus: String? = null


    @Column(name = "BATTERY_REMARKS")
    @Basic
    var batteryRemarks: String? = null


    @Column(name = "BATTERY_CARRIER")
    @Basic
    var batteryCarrier: String? = null


    @Column(name = "BATTERY_CARRIER_STATUS")
    @Basic
    var batteryCarrierStatus: String? = null


    @Column(name = "BATTERY_CARRIER_REMARKS")
    @Basic
    var batteryCarrierRemarks: String? = null


    @Column(name = "WIRING_HARNESS")
    @Basic
    var wiringHarness: String? = null


    @Column(name = "WIRING_HARNESS_STATUS")
    @Basic
    var wiringHarnessStatus: String? = null


    @Column(name = "WIRING_HARNESS_REMARKS")
    @Basic
    var wiringHarnessRemarks: String? = null


    @Column(name = "STARTER_MOTOR")
    @Basic
    var starterMotor: String? = null


    @Column(name = "STARTER_MOTOR_STATUS")
    @Basic
    var starterMotorStatus: String? = null


    @Column(name = "STARTER_MOTOR_REMARKS")
    @Basic
    var starterMotorRemarks: String? = null


    @Column(name = "ALTERNATOR_GENERATOR")
    @Basic
    var alternatorGenerator: String? = null


    @Column(name = "ALTERNATOR_GENERATOR_STATUS")
    @Basic
    var alternatorGeneratorStatus: String? = null


    @Column(name = "ALTERNATOR_GENERATOR_REMARKS")
    @Basic
    var alternatorGeneratorRemarks: String? = null


    @Column(name = "RADIATOR")
    @Basic
    var radiator: String? = null

    @Column(name = "RADIATOR_STATUS")
    @Basic
    var radiatorStatus: String? = null


    @Column(name = "RADIATOR_REMARKS")
    @Basic
    var radiatorRemarks: String? = null


    @Column(name = "RADIATOR_HOSES")
    @Basic
    var radiatorHoses: String? = null


    @Column(name = "RADIATOR_HOSES_STATUS")
    @Basic
    var radiatorHosesStatus: String? = null


    @Column(name = "RADIATOR_HOSES_REMARKS")
    @Basic
    var radiatorHosesRemarks: String? = null


    @Column(name = "WATER_PUMP")
    @Basic
    var waterPump: String? = null


    @Column(name = "WATER_PUMP_STATUS")
    @Basic
    var waterPumpStatus: String? = null


    @Column(name = "WATER_PUMP_REMARKS")
    @Basic
    var waterPumpRemarks: String? = null


    @Column(name = "CARBURETOR")
    @Basic
    var carburetor: String? = null


    @Column(name = "CARBURETOR_STATUS")
    @Basic
    var carburetorStatus: String? = null


    @Column(name = "CARBURETOR_REMARKS")
    @Basic
    var carburetorRemarks: String? = null


    @Column(name = "HIGH_TENSION_CABLES")
    @Basic
    var highTensionCables: String? = null


    @Column(name = "HIGH_TENSION_CABLES_STATUS")
    @Basic
    var highTensionCablesStatus: String? = null


    @Column(name = "HIGH_TENSION_CABLES_REMARKS")
    @Basic
    var highTensionCablesRemarks: String? = null


    @Column(name = "AC_CONDENSER")
    @Basic
    var acCondenser: String? = null


    @Column(name = "AC_CONDENSER_STATUS")
    @Basic
    var acCondenserStatus: String? = null


    @Column(name = "AC_CONDENSER_REMARKS")
    @Basic
    var acCondenserRemarks: String? = null


    @Column(name = "POWER_STEERING")
    @Basic
    var powerSteering: String? = null


    @Column(name = "POWER_STEERING_STATUS")
    @Basic
    var powerSteeringStatus: String? = null


    @Column(name = "POWER_STEERING_REMARKS")
    @Basic
    var powerSteeringRemarks: String? = null


    @Column(name = "BRAKE_MASTER_CYLINDER")
    @Basic
    var brakeMasterCylinder: String? = null


    @Column(name = "BRAKE_MASTER_CYLINDER_STATUS")
    @Basic
    var brakeMasterCylinderStatus: String? = null


    @Column(name = "BRAKE_MASTER_CYLINDER_REMARKS")
    @Basic
    var brakeMasterCylinderRemarks: String? = null


    @Column(name = "CLUTCH_MASTER_CYLIDER")
    @Basic
    var clutchMasterCylider: String? = null


    @Column(name = "CLUTCH_MASTER_CYLIDER_STATUS")
    @Basic
    var clutchMasterCyliderStatus: String? = null


    @Column(name = "CLUTCH_MASTER_CYLIDER_REMARKS")
    @Basic
    var clutchMasterCyliderRemarks: String? = null


    @Column(name = "BRAKE_SYSTEM")
    @Basic
    var brakeSystem: String? = null


    @Column(name = "BRAKE_SYSTEM_STATUS")
    @Basic
    var brakeSystemStatus: String? = null


    @Column(name = "BRAKE_SYSTEM_REMARKS")
    @Basic
    var brakeSystemRemarks: String? = null


    @Column(name = "FUEL_PIPES")
    @Basic
    var fuelPipes: String? = null


    @Column(name = "FUEL_PIPES_STATUS")
    @Basic
    var fuelPipesStatus: String? = null


    @Column(name = "FUEL_PIPES_REMARKS")
    @Basic
    var fuelPipesRemarks: String? = null


    @Column(name = "FLEXIBLE_BRAKE_PIPES")
    @Basic
    var flexibleBrakePipes: String? = null


    @Column(name = "FLEXIBLE_BRAKE_PIPES_STATUS")
    @Basic
    var flexibleBrakePipesStatus: String? = null


    @Column(name = "FLEXIBLE_BRAKE_PIPES_REMARKS")
    @Basic
    var flexibleBrakePipesRemarks: String? = null


    @Column(name = "WINDSCREEN_WASHER_BOTTLE")
    @Basic
    var windscreenWasherBottle: String? = null


    @Column(name = "WINDSCREEN_WASHER_BOTTLE_STATUS")
    @Basic
    var windscreenWasherBottleStatus: String? = null


    @Column(name = "WINDSCREEN_WASHER_BOTTLE_REMARKS")
    @Basic
    var windscreenWasherBottleRemarks: String? = null


    @Column(name = "BOOT_LID")
    @Basic
    var bootLid: String? = null


    @Column(name = "BOOT_LID_STATUS")
    @Basic
    var bootLidStatus: String? = null


    @Column(name = "BOOT_LID_REMARKS")
    @Basic
    var bootLidRemarks: String? = null


    @Column(name = "LIFE_SAVER")
    @Basic
    var lifeSaver: String? = null


    @Column(name = "LIFE_SAVER_STATUS")
    @Basic
    var lifeSaverStatus: String? = null


    @Column(name = "LIFE_SAVER_REMARKS")
    @Basic
    var lifeSaverRemarks: String? = null


    @Column(name = "RADIOTOR_WATER")
    @Basic
    var radiotorWater: String? = null


    @Column(name = "RADIOTOR_WATER_STATUS")
    @Basic
    var radiotorWaterStatus: String? = null


    @Column(name = "RADIOTOR_WATER_REMARKS")
    @Basic
    var radiotorWaterRemarks: String? = null


    @Column(name = "ENGINE_OIL")
    @Basic
    var engineOil: String? = null


    @Column(name = "ENGINE_OIL_STATUS")
    @Basic
    var engineOilStatus: String? = null


    @Column(name = "ENGINE_OIL_REMARKS")
    @Basic
    var engineOilRemarks: String? = null


    @Column(name = "STARTING_ENGINE")
    @Basic
    var startingEngine: String? = null


    @Column(name = "STARTING_ENGINE_STATUS")
    @Basic
    var startingEngineStatus: String? = null


    @Column(name = "STARTING_ENGINE_REMARKS")
    @Basic
    var startingEngineRemarks: String? = null


    @Column(name = "IDELE_SPEED_NOISES")
    @Basic
    var ideleSpeedNoises: String? = null


    @Column(name = "IDELE_SPEED_NOISES_STATUS")
    @Basic
    var ideleSpeedNoisesStatus: String? = null


    @Column(name = "IDELE_SPEED_NOISES_REMARKS")
    @Basic
    var ideleSpeedNoisesRemarks: String? = null


    @Column(name = "HIGH_SPEED_NOISES")
    @Basic
    var highSpeedNoises: String? = null


    @Column(name = "HIGH_SPEED_NOISES_STATUS")
    @Basic
    var highSpeedNoisesStatus: String? = null


    @Column(name = "HIGH_SPEED_NOISES_REMARKS")
    @Basic
    var highSpeedNoisesRemarks: String? = null


    @Column(name = "OIL_LEAKS")
    @Basic
    var oilLeaks: String? = null


    @Column(name = "OIL_LEAKS_STATUS")
    @Basic
    var oilLeaksStatus: String? = null


    @Column(name = "OIL_LEAKS_REMARKS")
    @Basic
    var oilLeaksRemarks: String? = null


    @Column(name = "WATER_LEAKS")
    @Basic
    var waterLeaks: String? = null


    @Column(name = "WATER_LEAKS_STATUS")
    @Basic
    var waterLeaksStatus: String? = null


    @Column(name = "WATER_LEAKS_REMARKS")
    @Basic
    var waterLeaksRemarks: String? = null


    @Column(name = "COOLING_SYSTEM_FUNCTION")
    @Basic
    var coolingSystemFunction: String? = null


    @Column(name = "COOLING_SYSTEM_FUNCTION_STATUS")
    @Basic
    var coolingSystemFunctionStatus: String? = null


    @Column(name = "COOLING_SYSTEM_FUNCTION_REMARKS")
    @Basic
    var coolingSystemFunctionRemarks: String? = null


    @Column(name = "ENGINE_OIL_PRESSURE")
    @Basic
    var engineOilPressure: String? = null


    @Column(name = "ENGINE_OIL_PRESSURE_STATUS")
    @Basic
    var engineOilPressureStatus: String? = null


    @Column(name = "ENGINE_OIL_PRESSURE_REMARKS")
    @Basic
    var engineOilPressureRemarks: String? = null


    @Column(name = "CHARGING_SYSTEM")
    @Basic
    var chargingSystem: String? = null


    @Column(name = "CHARGING_SYSTEM_STATUS")
    @Basic
    var chargingSystemStatus: String? = null


    @Column(name = "CHARGING_SYSTEM_REMARKS")
    @Basic
    var chargingSystemRemarks: String? = null


    @Column(name = "AC_OPERATION")
    @Basic
    var acOperation: String? = null


    @Column(name = "AC_OPERATION_STATUS")
    @Basic
    var acOperationStatus: String? = null


    @Column(name = "AC_OPERATION_REMARKS")
    @Basic
    var acOperationRemarks: String? = null


    @Column(name = "POWER_STEERING_OPERATION")
    @Basic
    var powerSteeringOperation: String? = null


    @Column(name = "POWER_STEERING_OPERATION_STATUS")
    @Basic
    var powerSteeringOperationStatus: String? = null


    @Column(name = "POWER_STEERING_OPERATION_REMARKS")
    @Basic
    var powerSteeringOperationRemarks: String? = null


    @Column(name = "FUEL_PUMP")
    @Basic
    var fuelPump: String? = null


    @Column(name = "FUEL_PUMP_STATUS")
    @Basic
    var fuelPumpStatus: String? = null


    @Column(name = "FUEL_PUMP_REMARKS")
    @Basic
    var fuelPumpRemarks: String? = null


    @Column(name = "VACUUM_PUMP")
    @Basic
    var vacuumPump: String? = null


    @Column(name = "VACUUM_PUMP_STATUS")
    @Basic
    var vacuumPumpStatus: String? = null


    @Column(name = "VACUUM_PUMP_REMARKS")
    @Basic
    var vacuumPumpRemarks: String? = null


    @Column(name = "ENGINE_STOPPER")
    @Basic
    var engineStopper: String? = null


    @Column(name = "ENGINE_STOPPER_STATUS")
    @Basic
    var engineStopperStatus: String? = null


    @Column(name = "ENGINE_STOPPER_REMARKS")
    @Basic
    var engineStopperRemarks: String? = null


    @Column(name = "EXHAUST_EMISSION")
    @Basic
    var exhaustEmission: String? = null


    @Column(name = "EXHAUST_EMISSION_STATUS")
    @Basic
    var exhaustEmissionStatus: String? = null


    @Column(name = "EXHAUST_EMISSION_REMARKS")
    @Basic
    var exhaustEmissionRemarks: String? = null


    @Column(name = "LEAF_SPRINGS")
    @Basic
    var leafSprings: String? = null


    @Column(name = "LEAF_SPRINGS_STATUS")
    @Basic
    var leafSpringsStatus: String? = null


    @Column(name = "LEAF_SPRINGS_REMARKS")
    @Basic
    var leafSpringsRemarks: String? = null


    @Column(name = "U_BOLTS")
    @Basic
    var uBolts: String? = null


    @Column(name = "U_BOLTS_STATUS")
    @Basic
    var uBoltsStatus: String? = null


    @Column(name = "U_BOLTS_REMARKS")
    @Basic
    var uBoltsRemarks: String? = null


    @Column(name = "SPRING_BUSHES")
    @Basic
    var springBushes: String? = null


    @Column(name = "SPRING_BUSHES_STATUS")
    @Basic
    var springBushesStatus: String? = null


    @Column(name = "SPRING_BUSHES_REMARKS")
    @Basic
    var springBushesRemarks: String? = null


    @Column(name = "SPRING_PINS")
    @Basic
    var springPins: String? = null


    @Column(name = "SPRING_PINS_STATUS")
    @Basic
    var springPinsStatus: String? = null


    @Column(name = "SPRING_PINS_REMARKS")
    @Basic
    var springPinsRemarks: String? = null


    @Column(name = "COIL_SPRINGS")
    @Basic
    var coilSprings: String? = null


    @Column(name = "COIL_SPRINGS_STATUS")
    @Basic
    var coilSpringsStatus: String? = null


    @Column(name = "COIL_SPRINGS_REMARKS")
    @Basic
    var coilSpringsRemarks: String? = null


    @Column(name = "FRONT_SHOCK_ABSORBERS")
    @Basic
    var frontShockAbsorbers: String? = null


    @Column(name = "FRONT_SHOCK_ABSORBERS_STATUS")
    @Basic
    var frontShockAbsorbersStatus: String? = null


    @Column(name = "FRONT_SHOCK_ABSORBERS_REMARKS")
    @Basic
    var frontShockAbsorbersRemarks: String? = null


    @Column(name = "REAR_SHOCK_ABSORBERS")
    @Basic
    var rearShockAbsorbers: String? = null


    @Column(name = "REAR_SHOCK_ABSORBERS_STATUS")
    @Basic
    var rearShockAbsorbersStatus: String? = null


    @Column(name = "REAR_SHOCK_ABSORBERS_REMARKS")
    @Basic
    var rearShockAbsorbersRemarks: String? = null


    @Column(name = "SUBFRAME_MOUNTINGS")
    @Basic
    var subframeMountings: String? = null


    @Column(name = "SUBFRAME_MOUNTINGS_STATUS")
    @Basic
    var subframeMountingsStatus: String? = null

    @Column(name = "SUBFRAME_MOUNTINGS_REMARKS")
    @Basic
    var subframeMountingsRemarks: String? = null

    @Column(name = "ENGINE_MOUNTINGS")
    @Basic
    var engineMountings: String? = null

    @Column(name = "ENGINE_MOUNTINGS_STATUS")
    @Basic
    var engineMountingsStatus :String? = null
    
    @Column(name = "ENGINE_MOUNTINGS_REMARKS")
    @Basic
    var engineMountingsRemarks: String? = null

    @Column(name = "GEAR_BOX_MOUNTINGS")
    @Basic
    var gearBoxMountings: String? = null

    @Column(name = "GEAR_BOX_MOUNTINGS_STATUS")
    @Basic
    var gearBoxMountingsStatus: String? = null

    @Column(name = "GEAR_BOX_MOUNTINGS_REMARKS")
    @Basic
    var gearBoxMountingsRemarks: String? = null

    @Column(name = "STABILIZER_BUSHES")
    @Basic
    var stabilizerBushes: String? = null

    @Column(name = "STABILIZER_BUSHES_STATUS")
    @Basic
    var stabilizerBushesStatus: String? = null

    @Column(name = "STABILIZER_BUSHES_REMARKS")
    @Basic
    var stabilizerBushesRemarks: String? = null

    @Column(name = "FUEL_TANK")
    @Basic
    var fuelTank: String? = null

    @Column(name = "FUEL_TANK_STATUS")
    @Basic
    var fuelTankStatus: String? = null

    @Column(name = "FUEL_TANK_REMARKS")
    @Basic
    var fuelTankRemarks: String? = null

    
    @Column(name = "FUEL_LINES")
    @Basic
    var fuelLines: String? = null

    
    @Column(name = "FUEL_LINES_STATUS")
    @Basic
    var fuelLinesStatus: String? = null

    
    @Column(name = "FUEL_LINES_REMARKS")
    @Basic
    var fuelLinesRemarks: String? = null

    
    @Column(name = "BRAKE_LINES")
    @Basic
    var brakeLines: String? = null

    
    @Column(name = "BRAKE_LINES_STATUS")
    @Basic
    var brakeLinesStatus: String? = null

    
    @Column(name = "BRAKE_LINES_REMARKS")
    @Basic
    var brakeLinesRemarks: String? = null

    
    @Column(name = "EXHAUST_SYSTEM")
    @Basic
    var exhaustSystem: String? = null

    
    @Column(name = "EXHAUST_SYSTEM_STATUS")
    @Basic
    var exhaustSystemStatus: String? = null

    
    @Column(name = "EXHAUST_SYSTEM_REMARKS")
    @Basic
    var exhaustSystemRemarks: String? = null

    
    @Column(name = "TRANSMISSION")
    @Basic
    var transmission: String? = null

    
    @Column(name = "TRANSMISSION_STATUS")
    @Basic
    var transmissionStatus: String? = null

    
    @Column(name = "TRANSMISSION_REMARKS")
    @Basic
    var transmissionRemarks: String? = null

    
    @Column(name = "STEERING_BOX")
    @Basic
    var steeringBox: String? = null

    
    @Column(name = "STEERING_BOX_STATUS")
    @Basic
    var steeringBoxStatus: String? = null

    
    @Column(name = "STEERING_BOX_REMARKS")
    @Basic
    var steeringBoxRemarks: String? = null

    
    @Column(name = "CHASSIS")
    @Basic
    var chassis: String? = null

    
    @Column(name = "CHASSIS_STATUS")
    @Basic
    var chassisStatus: String? = null

    
    @Column(name = "CHASSIS_REMARKS")
    @Basic
    var chassisRemarks: String? = null

    
    @Column(name = "MONOBLACK_BODY")
    @Basic
    var monoblackBody: String? = null

    
    @Column(name = "MONOBLACK_BODY_STATUS")
    @Basic
    var monoblackBodyStatus: String? = null

    
    @Column(name = "MONOBLACK_BODY_REMARKS")
    @Basic
    var monoblackBodyRemarks: String? = null

    
    @Column(name = "FRONT_SUB_FRAME")
    @Basic
    var frontSubFrame: String? = null

    
    @Column(name = "FRONT_SUB_FRAME_STATUS")
    @Basic
    var frontSubFrameStatus: String? = null

    
    @Column(name = "FRONT_SUB_FRAME_REMARKS")
    @Basic
    var frontSubFrameRemarks: String? = null

    
    @Column(name = "REAR_SUB_FRAME")
    @Basic
    var rearSubFrame: String? = null

    
    @Column(name = "REAR_SUB_FRAME_STATUS")
    @Basic
    var rearSubFrameStatus: String? = null

    
    @Column(name = "REAR_SUB_FRAME_REMARKS")
    @Basic
    var rearSubFrameRemarks: String? = null

    
    @Column(name = "FRONT_AXLE")
    @Basic
    var frontAxle: String? = null

    
    @Column(name = "FRONT_AXLE_STATUS")
    @Basic
    var frontAxleStatus: String? = null

    
    @Column(name = "FRONT_AXLE_REMARKS")
    @Basic
    var frontAxleRemarks: String? = null

    
    @Column(name = "REAR_AXLE")
    @Basic
    var rearAxle: String? = null

    
    @Column(name = "REAR_AXLE_STATUS")
    @Basic
    var rearAxleStatus: String? = null

    
    @Column(name = "REAR_AXLE_REMARKS")
    @Basic
    var rearAxleRemarks: String? = null

    
    @Column(name = "SECOND_REAR_AXLE")
    @Basic
    var secondRearAxle: String? = null

    
    @Column(name = "SECOND_REAR_AXLE_STATUS")
    @Basic
    var secondRearAxleStatus: String? = null

    
    @Column(name = "SECOND_REAR_AXLE_REMARKS")
    @Basic
    var secondRearAxleRemarks: String? = null

    
    @Column(name = "WHEEL_GEOMETRY")
    @Basic
    var wheelGeometry: String? = null

    
    @Column(name = "WHEEL_GEOMETRY_STATUS")
    @Basic
    var wheelGeometryStatus: String? = null

    
    @Column(name = "WHEEL_GEOMETRY_REMARKS")
    @Basic
    var wheelGeometryRemarks: String? = null

    
    @Column(name = "KINGPIN_BUSHES")
    @Basic
    var kingpinBushes: String? = null

    
    @Column(name = "KINGPIN_BUSHES_STATUS")
    @Basic
    var kingpinBushesStatus: String? = null

    
    @Column(name = "KINGPIN_BUSHES_REMARKS")
    @Basic
    var kingpinBushesRemarks: String? = null

    
    @Column(name = "BALL_JOINTS")
    @Basic
    var ballJoints: String? = null

    
    @Column(name = "BALL_JOINTS_STATUS")
    @Basic
    var ballJointsStatus: String? = null

    
    @Column(name = "BALL_JOINTS_REMARKS")
    @Basic
    var ballJointsRemarks: String? = null

    
    @Column(name = "STEERING_SYSTEM")
    @Basic
    var steeringSystem: String? = null

    
    @Column(name = "STEERING_SYSTEM_STATUS")
    @Basic
    var steeringSystemStatus: String? = null

    
    @Column(name = "STEERING_SYSTEM_REMARKS")
    @Basic
    var steeringSystemRemarks: String? = null

    
    @Column(name = "BRAKE_DISC")
    @Basic
    var brakeDisc: String? = null

    
    @Column(name = "BRAKE_DISC_STATUS")
    @Basic
    var brakeDiscStatus: String? = null

    
    @Column(name = "BRAKE_DISC_REMARKS")
    @Basic
    var brakeDiscRemarks: String? = null

    
    @Column(name = "CALIPERS")
    @Basic
    var calipers: String? = null

    
    @Column(name = "CALIPERS_STATUS")
    @Basic
    var calipersStatus: String? = null

    
    @Column(name = "CALIPERS_REMARKS")
    @Basic
    var calipersRemarks: String? = null

    
    @Column(name = "BRAKE_PAD_LIFE")
    @Basic
    var brakePadLife: String? = null

    
    @Column(name = "BRAKE_PAD_LIFE_STATUS")
    @Basic
    var brakePadLifeStatus: String? = null

    
    @Column(name = "BRAKE_PAD_LIFE_REMARKS")
    @Basic
    var brakePadLifeRemarks: String? = null

    
    @Column(name = "BRAKE_DRUMS")
    @Basic
    var brakeDrums: String? = null

    
    @Column(name = "BRAKE_DRUMS_STATUS")
    @Basic
    var brakeDrumsStatus: String? = null

    
    @Column(name = "BRAKE_DRUMS_REMARKS")
    @Basic
    var brakeDrumsRemarks: String? = null

    
    @Column(name = "WHEEL_CYLINDERS")
    @Basic
    var wheelCylinders: String? = null

    
    @Column(name = "WHEEL_CYLINDERS_STATUS")
    @Basic
    var wheelCylindersStatus: String? = null

    
    @Column(name = "WHEEL_CYLINDERS_REMARKS")
    @Basic
    var wheelCylindersRemarks: String? = null

    
    @Column(name = "REAR_OIL_SEALS")
    @Basic
    var rearOilSeals: String? = null

    
    @Column(name = "REAR_OIL_SEALS_STATUS")
    @get:Basic
    var rearOilSealsStatus: String? = null

    
    @Column(name = "REAR_OIL_SEALS_REMARKS")
    @get:Basic
    var rearOilSealsRemarks: String? = null

    
    @Column(name = "FRONT_LH_DRIVING_SHAFT")
    @get:Basic
    var frontLhDrivingShaft: String? = null

    
    @Column(name = "FRONT_LH_DRIVING_SHAFT_STATUS")
    @get:Basic
    var frontLhDrivingShaftStatus: String? = null

    
    @Column(name = "FRONT_LH_DRIVING_SHAFT_REMARKS")
    @get:Basic
    var frontLhDrivingShaftRemarks: String? = null

    
    @Column(name = "FRONT_RH_DRIVING_SHAFT")
    @get:Basic
    var frontRhDrivingShaft: String? = null

    
    @Column(name = "FRONT_RH_DRIVING_SHAFT_STATUS")
    @get:Basic
    var frontRhDrivingShaftStatus: String? = null

    
    @Column(name = "FRONT_RH_DRIVING_SHAFT_REMARKS")
    @get:Basic
    var frontRhDrivingShaftRemarks: String? = null

    
    @Column(name = "FRONT_DIFFERENCIAL")
    @get:Basic
    var frontDifferencial: String? = null

    
    @Column(name = "FRONT_DIFFERENCIAL_STATUS")
    @get:Basic
    var frontDifferencialStatus: String? = null

    
    @Column(name = "FRONT_DIFFERENCIAL_REMARKS")
    @get:Basic
    var frontDifferencialRemarks: String? = null

    
    @Column(name = "REAR_DIFFERENCIAL")
    @get:Basic
    var rearDifferencial: String? = null

    
    @Column(name = "REAR_DIFFERENCIAL_STATUS")
    @get:Basic
    var rearDifferencialStatus: String? = null

    
    @Column(name = "REAR_DIFFERENCIAL_REMARKS")
    @get:Basic
    var rearDifferencialRemarks: String? = null

    
    @Column(name = "TRANSFER_CASE")
    @get:Basic
    var transferCase: String? = null

    
    @Column(name = "TRANSFER_CASE_STATUS")
    @get:Basic
    var transferCaseStatus: String? = null

    
    @Column(name = "TRANSFER_CASE_REMARKS")
    @get:Basic
    var transferCaseRemarks: String? = null

    
    @Column(name = "FRONT_PROPELLER_SHAFT")
    @get:Basic
    var frontPropellerShaft: String? = null

    
    @Column(name = "FRONT_PROPELLER_SHAFT_STATUS")
    @get:Basic
    var frontPropellerShaftStatus: String? = null

    
    @Column(name = "FRONT_PROPELLER_SHAFT_REMARKS")
    @get:Basic
    var frontPropellerShaftRemarks: String? = null

    
    @Column(name = "REAR_PROPELLER_SHAFT")
    @get:Basic
    var rearPropellerShaft: String? = null

    
    @Column(name = "REAR_PROPELLER_SHAFT_STATUS")
    @get:Basic
    var rearPropellerShaftStatus: String? = null

    
    @Column(name = "REAR_PROPELLER_SHAFT_REMARKS")
    @get:Basic
    var rearPropellerShaftRemarks: String? = null

    
    @Column(name = "CENTRE_BEARING")
    @get:Basic
    var centreBearing: String? = null

    
    @Column(name = "CENTRE_BEARING_STATUS")
    @get:Basic
    var centreBearingStatus: String? = null

    
    @Column(name = "CENTRE_BEARING_REMARKS")
    @get:Basic
    var centreBearingRemarks: String? = null

    
    @Column(name = "FRONT_REAR_OIL_SEALS")
    @get:Basic
    var frontRearOilSeals: String? = null

    
    @Column(name = "FRONT_REAR_OIL_SEALS_STATUS")
    @get:Basic
    var frontRearOilSealsStatus: String? = null

    
    @Column(name = "FRONT_REAR_OIL_SEALS_REMARKS")
    @get:Basic
    var frontRearOilSealsRemarks: String? = null

    
    @Column(name = "REAR_LH_DRIVE_SHAFT")
    @get:Basic
    var rearLhDriveShaft: String? = null

    
    @Column(name = "REAR_LH_DRIVE_SHAFT_STATUS")
    @get:Basic
    var rearLhDriveShaftStatus: String? = null

    
    @Column(name = "REAR_LH_DRIVE_SHAFT_REMARKS")
    @get:Basic
    var rearLhDriveShaftRemarks: String? = null

    
    @Column(name = "REAR_RH_DRIVE_SHAFT")
    @get:Basic
    var rearRhDriveShaft: String? = null

    
    @Column(name = "REAR_RH_DRIVE_SHAFT_STATUS")
    @get:Basic
    var rearRhDriveShaftStatus: String? = null

    
    @Column(name = "REAR_RH_DRIVE_SHAFT_REMARKS")
    @get:Basic
    var rearRhDriveShaftRemarks: String? = null

    
    @Column(name = "HAND_BRAKE_CABLES")
    @get:Basic
    var handBrakeCables: String? = null

    
    @Column(name = "HAND_BRAKE_CABLES_STATUS")
    @get:Basic
    var handBrakeCablesStatus: String? = null

    
    @Column(name = "HAND_BRAKE_CABLES_REMARKS")
    @get:Basic
    var handBrakeCablesRemarks: String? = null

    
    @Column(name = "DRIVING_SEAT_ADJUSTMENTS")
    @get:Basic
    var drivingSeatAdjustments: String? = null

    
    @Column(name = "DRIVING_SEAT_ADJUSTMENTS_STATUS")
    @get:Basic
    var drivingSeatAdjustmentsStatus: String? = null

    
    @Column(name = "DRIVING_SEAT_ADJUSTMENTS_REMARKS")
    @get:Basic
    var drivingSeatAdjustmentsRemarks: String? = null

    
    @Column(name = "DRIVING_PERFORMANCES")
    @get:Basic
    var drivingPerformances: String? = null

    
    @Column(name = "DRIVING_PERFORMANCES_STATUS")
    @get:Basic
    var drivingPerformancesStatus: String? = null

    
    @Column(name = "DRIVING_PERFORMANCES_REMARKS")
    @get:Basic
    var drivingPerformancesRemarks: String? = null

    
    @Column(name = "EMERGENCY_BRAKE")
    @get:Basic
    var emergencyBrake: String? = null

    
    @Column(name = "EMERGENCY_BRAKE_STATUS")
    @get:Basic
    var emergencyBrakeStatus: String? = null

    
    @Column(name = "EMERGENCY_BRAKE_REMARKS")
    @get:Basic
    var emergencyBrakeRemarks: String? = null

    
    @Column(name = "GEAR_SHIFT")
    @get:Basic
    var gearShift: String? = null

    
    @Column(name = "GEAR_SHIFT_STATUS")
    @get:Basic
    var gearShiftStatus: String? = null

    @Column(name = "GEAR_SHIFT_REMARKS")
    @get:Basic
    var gearShiftRemarks: String? = null

    
    @Column(name = "STEERING_STABILITY")
    var steeringStability: String? = null

    
    @Column(name = "STEERING_STABILITY_STATUS")
    @get:Basic
    var steeringStabilityStatus: String? = null

    
    @Column(name = "STEERING_STABILITY_REMARKS")
    @get:Basic
    var steeringStabilityRemarks: String? = null

    
    @Column(name = "FRONT_SUSPENSION")
    @get:Basic
    var frontSuspension: String? = null

    
    @Column(name = "FRONT_SUSPENSION_STATUS")
    @get:Basic
    var frontSuspensionStatus: String? = null

    
    @Column(name = "FRONT_SUSPENSION_REMARKS")
    @get:Basic
    var frontSuspensionRemarks: String? = null

    
    @Column(name = "REAR_SUSPENSION")
    @get:Basic
    var rearSuspension: String? = null

    
    @Column(name = "REAR_SUSPENSION_STATUS")
    @get:Basic
    var rearSuspensionStatus: String? = null

    
    @Column(name = "REAR_SUSPENSION_REMARKS")
    @get:Basic
    var rearSuspensionRemarks: String? = null

    
    @Column(name = "GAUGES_AND_INSTRUMENTS")
    @get:Basic
    var gaugesAndInstruments: String? = null

    
    @Column(name = "GAUGES_AND_INSTRUMENTS_STATUS")
    @get:Basic
    var gaugesAndInstrumentsStatus: String? = null

    
    @Column(name = "GAUGES_AND_INSTRUMENTS_REMARKS")
    @get:Basic
    var gaugesAndInstrumentsRemarks: String? = null

    
    @Column(name = "ODEMETER")
    @get:Basic
    var odemeter: String? = null

    
    @Column(name = "ODEMETER_STATUS")
    @get:Basic
    var odemeterStatus: String? = null

    
    @Column(name = "ODEMETER_REMARKS")
    @get:Basic
    var odemeterRemarks: String? = null

    
    @Column(name = "HEATER")
    @get:Basic
    var heater: String? = null

    
    @Column(name = "HEATER_STATUS")
    @get:Basic
    var heaterStatus: String? = null

    
    @Column(name = "HEATER_REMARKS")
    @get:Basic
    var heaterRemarks: String? = null

    
    @Column(name = "DEFROSTER")
    @get:Basic
    var defroster: String? = null

    
    @Column(name = "DEFROSTER_STATUS")
    @get:Basic
    var defrosterStatus: String? = null

    
    @Column(name = "DEFROSTER_REMARKS")
    @get:Basic
    var defrosterRemarks: String? = null

    
    @Column(name = "AIRCON")
    @get:Basic
    var aircon: String? = null

    
    @Column(name = "AIRCON_STATUS")
    @get:Basic
    var airconStatus: String? = null

    
    @Column(name = "AIRCON_REMARKS")
    @get:Basic
    var airconRemarks: String? = null

    
    @Column(name = "WINDSCREEN_WIPERS")
    @get:Basic
    var windscreenWipers: String? = null

    
    @Column(name = "WINDSCREEN_WIPERS_STATUS")
    @get:Basic
    var windscreenWipersStatus: String? = null

    
    @Column(name = "WINDSCREEN_WIPERS_REMARKS")
    @get:Basic
    var windscreenWipersRemarks: String? = null

    
    @Column(name = "WASHERS")
    @get:Basic
    var washers: String? = null

    
    @Column(name = "WASHERS_STATUS")
    @get:Basic
    var washersStatus: String? = null

    
    @Column(name = "WASHERS_REMARKS")
    @get:Basic
    var washersRemarks: String? = null

    
    @Column(name = "HORN")
    @get:Basic
    var horn: String? = null

    
    @Column(name = "HORN_STATUS")
    @get:Basic
    var hornStatus: String? = null

    
    @Column(name = "HORN_REMARKS")
    @get:Basic
    var hornRemarks: String? = null

    
    @Column(name = "WHEEL_ALIGNMENT")
    @get:Basic
    var wheelAlignment: String? = null

    
    @Column(name = "WHEEL_ALIGNMENT_STATUS")
    @get:Basic
    var wheelAlignmentStatus: String? = null

    
    @Column(name = "WHEEL_ALIGNMENT_REMARKS")
    @get:Basic
    var wheelAlignmentRemarks: String? = null

    
    @Column(name = "PARKING_BRAKE")
    @get:Basic
    var parkingBrake: String? = null

    
    @Column(name = "PARKING_BRAKE_STATUS")
    @get:Basic
    var parkingBrakeStatus: String? = null

    
    @Column(name = "PARKING_BRAKE_REMARKS")
    @get:Basic
    var parkingBrakeRemarks: String? = null

    
    @Column(name = "FRONT_TYRES")
    @get:Basic
    var frontTyres: String? = null

    
    @Column(name = "FRONT_TYRES_STATUS")
    @get:Basic
    var frontTyresStatus: String? = null

    
    @Column(name = "FRONT_TYRES_REMARKS")
    @get:Basic
    var frontTyresRemarks: String? = null

    
    @Column(name = "REAR_TYRES_1ST_AXLE")
    @get:Basic
    var rearTyres1StAxle: String? = null

    
    @Column(name = "REAR_TYRES_1ST_AXLE_STATUS")
    @get:Basic
    var rearTyres1StAxleStatus: String? = null

    
    @Column(name = "REAR_TYRES_1ST_AXLE_REMARKS")
    @get:Basic
    var rearTyres1StAxleRemarks: String? = null

    
    @Column(name = "REAR_TYRES_2ST_AXLE")
    @get:Basic
    var rearTyres2StAxle: String? = null

    
    @Column(name = "REAR_TYRES_2ST_AXLE_STATUS")
    @get:Basic
    var rearTyres2StAxleStatus: String? = null

    
    @Column(name = "REAR_TYRES_2ST_AXLE_REMARKS")
    @get:Basic
    var rearTyres2StAxleRemarks: String? = null

    
    @Column(name = "REAR_TYRES_3ST_AXLE")
    @get:Basic
    var rearTyres3StAxle: String? = null

    
    @Column(name = "REAR_TYRES_3ST_AXLE_STATUS")
    @get:Basic
    var rearTyres3StAxleStatus: String? = null

    
    @Column(name = "REAR_TYRES_3ST_AXLE_REMARKS")
    @get:Basic
    var rearTyres3StAxleRemarks: String? = null


    @Column(name = "REAR_TYRES_4ST_AXLE")
    @get:Basic
    var rearTyres4StAxle: String? = null

    
    @Column(name = "REAR_TYRES_4ST_AXLE_STATUS")
    @get:Basic
    var rearTyres4StAxleStatus: String? = null


    @Column(name = "REAR_TYRES_4ST_AXLE_REMARKS")
    @get:Basic
    var rearTyres4StAxleRemarks: String? = null


    @Column(name = "INSPECTION_DATE")
    @get:Basic
    var inspectionDate: Date? = null

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
    @Column(name = "JACK_AND_HANDLE")
    @get:Basic
    var jackAndHandle: String? = null

    @Column(name = "JACK_AND_HANDLE_STATUS")
    @get:Basic
    var jackAndHandleStatus: String? = null

    @Column(name = "JACK_AND_HANDLE_REMARKS")
    @get:Basic
    var jackAndHandleRemarks: String? = null
    

    override fun equals(other: Any?): Boolean {
        if (this  === other) return true
        if (other  == null || javaClass != other.javaClass) return false
        val that = other as MinistryInspectionMotorEntity
        return( id  == that.id  &&
                overallAppearance  ==  that.overallAppearance &&
                overallAppearanceStatus ==  that.overallAppearanceStatus &&
                overallAppearanceRemarks ==  that.overallAppearanceRemarks &&
                conditionOfPaint ==  that.conditionOfPaint &&
                conditionOfPaintStatus ==  that.conditionOfPaintStatus &&
                conditionOfPaintRemarks ==  that.conditionOfPaintRemarks &&
                doors ==  that.doors &&
                doorsStatus ==  that.doorsStatus &&
                doorsRemarks ==  that.doorsRemarks &&
                windows ==  that.windows &&
                windowsStatus ==  that.windowsStatus &&
                windowsRemarks ==  that.windowsRemarks &&
                sunroof ==  that.sunroof &&
                sunroofRemarks ==  that.sunroofRemarks &&
                sunroofStatus ==  that.sunroofStatus &&
                externalMirrors ==  that.externalMirrors &&
                externalMirrorsStatus ==  that.externalMirrorsStatus &&
                externalMirrorsRemarks ==  that.externalMirrorsRemarks &&
                glasses ==  that.glasses &&
                glassesStatus  ==  that.glassesStatus &&
                glassesRemarks ==  that.glassesRemarks &&
                wipersWashers ==  that.wipersWashers &&
                wipersWashersStatus ==  that.wipersWashersStatus &&
                wipersWashersRemarks ==  that.wipersWashersRemarks &&
                seats ==  that.seats &&
                seatsStatus ==  that.seatsStatus &&
                seatsRemarks ==  that.seatsRemarks &&
                trimMoulding ==  that.trimMoulding &&
                trimMouldingStatus ==  that.trimMouldingStatus &&
                trimMouldingRemarks ==  that.trimMouldingRemarks &&
                safetyBelts ==  that.safetyBelts &&
                safetyBeltsStatus ==  that.safetyBeltsStatus &&
                safetyBeltsRemarks ==  that.safetyBeltsRemarks &&
                steeringWheel ==  that.steeringWheel &&
                steeringWheelStatus ==  that.steeringWheelStatus &&
                steeringWheelRemarks ==  that.steeringWheelRemarks &&
                brakePedal ==  that.brakePedal &&
                brakePedalStatus ==  that.brakePedalStatus &&
                brakePedalRemarks ==  that.brakePedalRemarks &&
                clutchPedal ==  that.clutchPedal &&
                clutchPedalStatus ==  that.clutchPedalStatus &&
                clutchPedalRemarks ==  that.clutchPedalRemarks &&
                parkingBrakeLever ==  that.parkingBrakeLever &&
                parkingBrakeLeverStatus ==  that.parkingBrakeLeverStatus &&
                parkingBrakeLeverRemarks ==  that.parkingBrakeLeverRemarks &&
                headlights ==  that.headlights &&
                headlightsStatus ==  that.headlightsStatus &&
                headlightsRemarks ==  that.headlightsRemarks &&
                parkingLights ==  that.parkingLights &&
                parkingLightsStatus ==  that.parkingLightsStatus &&
                parkingLightsRemarks ==  that.parkingLightsRemarks &&
                directionIndicators ==  that.directionIndicators &&
                directionIndicatorsStatus ==  that.directionIndicatorsStatus &&
                directionIndicatorsRemarks ==  that.directionIndicatorsRemarks &&
                reversingLight ==  that.reversingLight &&
                reversingLightStatus ==  that.reversingLightStatus &&
                reversingLightRemarks ==  that.reversingLightRemarks &&
                courtesyLight ==  that.courtesyLight &&
                courtesyLightStatus ==  that.courtesyLightStatus &&
                courtesyLightRemarks ==  that.courtesyLightRemarks &&
                rearNoPlateLight ==  that.rearNoPlateLight &&
                rearNoPlateLightStatus ==  that.rearNoPlateLightStatus &&
                rearNoPlateLightRemarks ==  that.rearNoPlateLightRemarks &&
                stopLights ==  that.stopLights &&
                stopLightsStatus ==  that.stopLightsStatus &&
                stopLightsRemarks ==  that.stopLightsRemarks &&
                frontBumber ==  that.frontBumber &&
                frontBumberStatus ==  that.frontBumberStatus &&
                frontBumberRemarks ==  that.frontBumberRemarks &&
                roofRack ==  that.roofRack &&
                roofRackStatus ==  that.roofRackStatus &&
                roofRackRemarks ==  that.roofRackRemarks &&
                antenna ==  that.antenna &&
                antennaStatus ==  that.antennaStatus &&
                antennaRemarks ==  that.antennaRemarks &&
                bonnet ==  that.bonnet &&
                bonnetStatus ==  that.bonnetStatus &&
                bonnetRemarks ==  that.bonnetRemarks &&
                engine ==  that.engine &&
                engineStatus ==  that.engineStatus &&
                engineRemarks ==  that.engineRemarks &&
                battery ==  that.battery &&
                batteryStatus ==  that.batteryStatus &&
                batteryRemarks ==  that.batteryRemarks &&
                batteryCarrier ==  that.batteryCarrier &&
                batteryCarrierStatus ==  that.batteryCarrierStatus &&
                batteryCarrierRemarks ==  that.batteryCarrierRemarks &&
                wiringHarness ==  that.wiringHarness &&
                wiringHarnessStatus ==  that.wiringHarnessStatus &&
                wiringHarnessRemarks ==  that.wiringHarnessRemarks &&
                starterMotor ==  that.starterMotor &&
                starterMotorStatus ==  that.starterMotorStatus &&
                starterMotorRemarks ==  that.starterMotorRemarks &&
                alternatorGenerator ==  that.alternatorGenerator &&
                alternatorGeneratorStatus ==  that.alternatorGeneratorStatus &&
                alternatorGeneratorRemarks ==  that.alternatorGeneratorRemarks &&
                radiator ==  that.radiator &&
                radiatorStatus ==  that.radiatorStatus &&
                radiatorRemarks ==  that.radiatorRemarks &&
                radiatorHoses ==  that.radiatorHoses &&
                radiatorHosesStatus ==  that.radiatorHosesStatus &&
                radiatorHosesRemarks ==  that.radiatorHosesRemarks &&
                waterPump ==  that.waterPump &&
                waterPumpStatus ==  that.waterPumpStatus &&
                waterPumpRemarks ==  that.waterPumpRemarks &&
                carburetor ==  that.carburetor &&
                carburetorStatus ==  that.carburetorStatus &&
                carburetorRemarks ==  that.carburetorRemarks &&
                highTensionCables ==  that.highTensionCables &&
                highTensionCablesStatus ==  that.highTensionCablesStatus &&
                highTensionCablesRemarks ==  that.highTensionCablesRemarks &&
                acCondenser ==  that.acCondenser &&
                acCondenserStatus ==  that.acCondenserStatus &&
                acCondenserRemarks ==  that.acCondenserRemarks &&
                powerSteering ==  that.powerSteering &&
                powerSteeringStatus ==  that.powerSteeringStatus &&
                powerSteeringRemarks ==  that.powerSteeringRemarks &&
                brakeMasterCylinder ==  that.brakeMasterCylinder &&
                brakeMasterCylinderStatus ==  that.brakeMasterCylinderStatus &&
                brakeMasterCylinderRemarks ==  that.brakeMasterCylinderRemarks &&
                clutchMasterCylider ==  that.clutchMasterCylider &&
                clutchMasterCyliderStatus ==  that.clutchMasterCyliderStatus &&
                clutchMasterCyliderRemarks ==  that.clutchMasterCyliderRemarks &&
                brakeSystem ==  that.brakeSystem &&
                brakeSystemStatus ==  that.brakeSystemStatus &&
                brakeSystemRemarks ==  that.brakeSystemRemarks &&
                fuelPipes ==  that.fuelPipes &&
                fuelPipesStatus ==  that.fuelPipesStatus &&
                fuelPipesRemarks ==  that.fuelPipesRemarks &&
                flexibleBrakePipes ==  that.flexibleBrakePipes &&
                flexibleBrakePipesStatus ==  that.flexibleBrakePipesStatus &&
                flexibleBrakePipesRemarks ==  that.flexibleBrakePipesRemarks &&
                windscreenWasherBottle ==  that.windscreenWasherBottle &&
                windscreenWasherBottleStatus ==  that.windscreenWasherBottleStatus &&
                windscreenWasherBottleRemarks ==  that.windscreenWasherBottleRemarks &&
                bootLid ==  that.bootLid &&
                bootLidStatus ==  that.bootLidStatus &&
                bootLidRemarks ==  that.bootLidRemarks &&
                lifeSaver ==  that.lifeSaver &&
                lifeSaverStatus ==  that.lifeSaverStatus &&
                lifeSaverRemarks ==  that.lifeSaverRemarks &&
                radiotorWater ==  that.radiotorWater &&
                radiotorWaterStatus ==  that.radiotorWaterStatus &&
                radiotorWaterRemarks ==  that.radiotorWaterRemarks &&
                engineOil ==  that.engineOil &&
                engineOilStatus ==  that.engineOilStatus &&
                engineOilRemarks ==  that.engineOilRemarks &&
                startingEngine ==  that.startingEngine &&
                startingEngineStatus ==  that.startingEngineStatus &&
                startingEngineRemarks ==  that.startingEngineRemarks &&
                ideleSpeedNoises ==  that.ideleSpeedNoises &&
                ideleSpeedNoisesStatus ==  that.ideleSpeedNoisesStatus &&
                ideleSpeedNoisesRemarks ==  that.ideleSpeedNoisesRemarks &&
                highSpeedNoises ==  that.highSpeedNoises &&
                highSpeedNoisesStatus ==  that.highSpeedNoisesStatus &&
                highSpeedNoisesRemarks ==  that.highSpeedNoisesRemarks &&
                oilLeaks ==  that.oilLeaks &&
                oilLeaksStatus ==  that.oilLeaksStatus &&
                oilLeaksRemarks ==  that.oilLeaksRemarks &&
                waterLeaks ==  that.waterLeaks &&
                waterLeaksStatus ==  that.waterLeaksStatus &&
                waterLeaksRemarks ==  that.waterLeaksRemarks &&
                coolingSystemFunction ==  that.coolingSystemFunction &&
                coolingSystemFunctionStatus ==  that.coolingSystemFunctionStatus &&
                coolingSystemFunctionRemarks ==  that.coolingSystemFunctionRemarks &&
                engineOilPressure ==  that.engineOilPressure &&
                engineOilPressureStatus ==  that.engineOilPressureStatus &&
                engineOilPressureRemarks ==  that.engineOilPressureRemarks &&
                chargingSystem ==  that.chargingSystem &&
                chargingSystemStatus ==  that.chargingSystemStatus &&
                chargingSystemRemarks ==  that.chargingSystemRemarks &&
                acOperation ==  that.acOperation &&
                acOperationStatus ==  that.acOperationStatus &&
                acOperationRemarks ==  that.acOperationRemarks &&
                powerSteeringOperation ==  that.powerSteeringOperation &&
                powerSteeringOperationStatus ==  that.powerSteeringOperationStatus &&
                powerSteeringOperationRemarks ==  that.powerSteeringOperationRemarks &&
                fuelPump ==  that.fuelPump &&
                fuelPumpStatus ==  that.fuelPumpStatus &&
                fuelPumpRemarks ==  that.fuelPumpRemarks &&
                vacuumPump ==  that.vacuumPump &&
                vacuumPumpStatus ==  that.vacuumPumpStatus &&
                vacuumPumpRemarks ==  that.vacuumPumpRemarks &&
                engineStopper ==  that.engineStopper &&
                engineStopperStatus ==  that.engineStopperStatus &&
                engineStopperRemarks ==  that.engineStopperRemarks &&
                exhaustEmission ==  that.exhaustEmission &&
                exhaustEmissionStatus ==  that.exhaustEmissionStatus &&
                exhaustEmissionRemarks ==  that.exhaustEmissionRemarks &&
                leafSprings ==  that.leafSprings &&
                leafSpringsStatus ==  that.leafSpringsStatus &&
                leafSpringsRemarks ==  that.leafSpringsRemarks &&
                uBolts ==  that.uBolts &&
                uBoltsStatus ==  that.uBoltsStatus &&
                uBoltsRemarks ==  that.uBoltsRemarks &&
                springBushes ==  that.springBushes &&
                springBushesStatus ==  that.springBushesStatus &&
                springBushesRemarks ==  that.springBushesRemarks &&
                springPins ==  that.springPins &&
                springPinsStatus ==  that.springPinsStatus &&
                springPinsRemarks ==  that.springPinsRemarks &&
                coilSprings ==  that.coilSprings &&
                coilSpringsStatus ==  that.coilSpringsStatus &&
                coilSpringsRemarks ==  that.coilSpringsRemarks &&
                frontShockAbsorbers ==  that.frontShockAbsorbers &&
                frontShockAbsorbersStatus ==  that.frontShockAbsorbersStatus &&
                frontShockAbsorbersRemarks ==  that.frontShockAbsorbersRemarks &&
                rearShockAbsorbers ==  that.rearShockAbsorbers &&
                rearShockAbsorbersStatus ==  that.rearShockAbsorbersStatus &&
                rearShockAbsorbersRemarks ==  that.rearShockAbsorbersRemarks &&
                subframeMountings ==  that.subframeMountings &&
                subframeMountingsStatus ==  that.subframeMountingsStatus &&
                subframeMountingsRemarks ==  that.subframeMountingsRemarks &&
                engineMountings ==  that.engineMountings &&
                engineMountingsStatus ==  that.engineMountingsStatus &&
                engineMountingsRemarks ==  that.engineMountingsRemarks &&
                gearBoxMountings ==  that.gearBoxMountings &&
                gearBoxMountingsStatus ==  that.gearBoxMountingsStatus &&
                gearBoxMountingsRemarks ==  that.gearBoxMountingsRemarks &&
                stabilizerBushes ==  that.stabilizerBushes &&
                stabilizerBushesStatus ==  that.stabilizerBushesStatus &&
                stabilizerBushesRemarks ==  that.stabilizerBushesRemarks &&
                fuelTank ==  that.fuelTank &&
                fuelTankStatus ==  that.fuelTankStatus &&
                fuelTankRemarks ==  that.fuelTankRemarks &&
                fuelLines ==  that.fuelLines &&
                fuelLinesStatus ==  that.fuelLinesStatus &&
                fuelLinesRemarks ==  that.fuelLinesRemarks &&
                brakeLines ==  that.brakeLines &&
                brakeLinesStatus ==  that.brakeLinesStatus &&
                brakeLinesRemarks ==  that.brakeLinesRemarks &&
                exhaustSystem ==  that.exhaustSystem &&
                exhaustSystemStatus ==  that.exhaustSystemStatus &&
                exhaustSystemRemarks ==  that.exhaustSystemRemarks &&
                transmission ==  that.transmission &&
                transmissionStatus ==  that.transmissionStatus &&
                transmissionRemarks ==  that.transmissionRemarks &&
                steeringBox ==  that.steeringBox &&
                steeringBoxStatus ==  that.steeringBoxStatus &&
                steeringBoxRemarks ==  that.steeringBoxRemarks &&
                chassis ==  that.chassis &&
                chassisStatus ==  that.chassisStatus &&
                chassisRemarks ==  that.chassisRemarks &&
                monoblackBody ==  that.monoblackBody &&
                monoblackBodyStatus ==  that.monoblackBodyStatus &&
                monoblackBodyRemarks ==  that.monoblackBodyRemarks &&
                frontSubFrame ==  that.frontSubFrame &&
                frontSubFrameStatus ==  that.frontSubFrameStatus &&
                frontSubFrameRemarks ==  that.frontSubFrameRemarks &&
                rearSubFrame ==  that.rearSubFrame &&
                rearSubFrameStatus ==  that.rearSubFrameStatus &&
                rearSubFrameRemarks ==  that.rearSubFrameRemarks &&
                frontAxle ==  that.frontAxle &&
                frontAxleStatus ==  that.frontAxleStatus &&
                frontAxleRemarks ==  that.frontAxleRemarks &&
                rearAxle ==  that.rearAxle &&
                rearAxleStatus ==  that.rearAxleStatus &&
                rearAxleRemarks ==  that.rearAxleRemarks &&
                secondRearAxle ==  that.secondRearAxle &&
                secondRearAxleStatus ==  that.secondRearAxleStatus &&
                secondRearAxleRemarks ==  that.secondRearAxleRemarks &&
                wheelGeometry ==  that.wheelGeometry &&
                wheelGeometryStatus ==  that.wheelGeometryStatus &&
                wheelGeometryRemarks ==  that.wheelGeometryRemarks &&
                kingpinBushes ==  that.kingpinBushes &&
                kingpinBushesStatus ==  that.kingpinBushesStatus &&
                kingpinBushesRemarks ==  that.kingpinBushesRemarks &&
                ballJoints ==  that.ballJoints &&
                ballJointsStatus ==  that.ballJointsStatus &&
                ballJointsRemarks ==  that.ballJointsRemarks &&
                steeringSystem ==  that.steeringSystem &&
                steeringSystemStatus ==  that.steeringSystemStatus &&
                steeringSystemRemarks ==  that.steeringSystemRemarks &&
                brakeDisc ==  that.brakeDisc &&
                brakeDiscStatus ==  that.brakeDiscStatus &&
                brakeDiscRemarks ==  that.brakeDiscRemarks &&
                calipers ==  that.calipers &&
                calipersStatus ==  that.calipersStatus &&
                calipersRemarks ==  that.calipersRemarks &&
                brakePadLife ==  that.brakePadLife &&
                brakePadLifeStatus ==  that.brakePadLifeStatus &&
                brakePadLifeRemarks ==  that.brakePadLifeRemarks &&
                brakeDrums ==  that.brakeDrums &&
                brakeDrumsStatus ==  that.brakeDrumsStatus &&
                brakeDrumsRemarks ==  that.brakeDrumsRemarks &&
                wheelCylinders ==  that.wheelCylinders &&
                wheelCylindersStatus ==  that.wheelCylindersStatus &&
                wheelCylindersRemarks ==  that.wheelCylindersRemarks &&
                rearOilSeals ==  that.rearOilSeals &&
                rearOilSealsStatus ==  that.rearOilSealsStatus &&
                rearOilSealsRemarks ==  that.rearOilSealsRemarks &&
                frontLhDrivingShaft ==  that.frontLhDrivingShaft &&
                frontLhDrivingShaftStatus ==  that.frontLhDrivingShaftStatus &&
                frontLhDrivingShaftRemarks ==  that.frontLhDrivingShaftRemarks &&
                frontRhDrivingShaft ==  that.frontRhDrivingShaft &&
                frontRhDrivingShaftStatus ==  that.frontRhDrivingShaftStatus &&
                frontRhDrivingShaftRemarks ==  that.frontRhDrivingShaftRemarks &&
                frontDifferencial ==  that.frontDifferencial &&
                frontDifferencialStatus ==  that.frontDifferencialStatus &&
                frontDifferencialRemarks ==  that.frontDifferencialRemarks &&
                rearDifferencial ==  that.rearDifferencial &&
                rearDifferencialStatus ==  that.rearDifferencialStatus &&
                rearDifferencialRemarks ==  that.rearDifferencialRemarks &&
                transferCase ==  that.transferCase &&
                transferCaseStatus ==  that.transferCaseStatus &&
                transferCaseRemarks ==  that.transferCaseRemarks &&
                frontPropellerShaft ==  that.frontPropellerShaft &&
                frontPropellerShaftStatus ==  that.frontPropellerShaftStatus &&
                frontPropellerShaftRemarks ==  that.frontPropellerShaftRemarks &&
                rearPropellerShaft ==  that.rearPropellerShaft &&
                rearPropellerShaftStatus ==  that.rearPropellerShaftStatus &&
                rearPropellerShaftRemarks ==  that.rearPropellerShaftRemarks &&
                centreBearing ==  that.centreBearing &&
                centreBearingStatus ==  that.centreBearingStatus &&
                centreBearingRemarks ==  that.centreBearingRemarks &&
                frontRearOilSeals ==  that.frontRearOilSeals &&
                frontRearOilSealsStatus ==  that.frontRearOilSealsStatus &&
                frontRearOilSealsRemarks ==  that.frontRearOilSealsRemarks &&
                rearLhDriveShaft ==  that.rearLhDriveShaft &&
                rearLhDriveShaftStatus ==  that.rearLhDriveShaftStatus &&
                rearLhDriveShaftRemarks ==  that.rearLhDriveShaftRemarks &&
                rearRhDriveShaft ==  that.rearRhDriveShaft &&
                rearRhDriveShaftStatus ==  that.rearRhDriveShaftStatus &&
                rearRhDriveShaftRemarks ==  that.rearRhDriveShaftRemarks &&
                handBrakeCables ==  that.handBrakeCables &&
                handBrakeCablesStatus ==  that.handBrakeCablesStatus &&
                handBrakeCablesRemarks ==  that.handBrakeCablesRemarks &&
                drivingSeatAdjustments ==  that.drivingSeatAdjustments &&
                drivingSeatAdjustmentsStatus ==  that.drivingSeatAdjustmentsStatus &&
                drivingSeatAdjustmentsRemarks ==  that.drivingSeatAdjustmentsRemarks &&
                drivingPerformances ==  that.drivingPerformances &&
                drivingPerformancesStatus ==  that.drivingPerformancesStatus &&
                drivingPerformancesRemarks ==  that.drivingPerformancesRemarks &&
                emergencyBrake ==  that.emergencyBrake &&
                emergencyBrakeStatus ==  that.emergencyBrakeStatus &&
                emergencyBrakeRemarks ==  that.emergencyBrakeRemarks &&
                gearShift ==  that.gearShift &&
                gearShiftStatus ==  that.gearShiftStatus &&
                gearShiftRemarks ==  that.gearShiftRemarks &&
                steeringStability ==  that.steeringStability &&
                steeringStabilityStatus ==  that.steeringStabilityStatus &&
                steeringStabilityRemarks ==  that.steeringStabilityRemarks &&
                frontSuspension ==  that.frontSuspension &&
                frontSuspensionStatus ==  that.frontSuspensionStatus &&
                frontSuspensionRemarks ==  that.frontSuspensionRemarks &&
                rearSuspension ==  that.rearSuspension &&
                rearSuspensionStatus ==  that.rearSuspensionStatus &&
                rearSuspensionRemarks ==  that.rearSuspensionRemarks &&
                gaugesAndInstruments ==  that.gaugesAndInstruments &&
                gaugesAndInstrumentsStatus ==  that.gaugesAndInstrumentsStatus &&
                gaugesAndInstrumentsRemarks ==  that.gaugesAndInstrumentsRemarks &&
                odemeter ==  that.odemeter &&
                odemeterStatus ==  that.odemeterStatus &&
                odemeterRemarks ==  that.odemeterRemarks &&
                heater ==  that.heater &&
                heaterStatus ==  that.heaterStatus &&
                heaterRemarks ==  that.heaterRemarks &&
                defroster ==  that.defroster &&
                defrosterStatus ==  that.defrosterStatus &&
                defrosterRemarks ==  that.defrosterRemarks &&
                aircon ==  that.aircon &&
                airconStatus ==  that.airconStatus &&
                airconRemarks ==  that.airconRemarks &&
                windscreenWipers ==  that.windscreenWipers &&
                windscreenWipersStatus ==  that.windscreenWipersStatus &&
                windscreenWipersRemarks ==  that.windscreenWipersRemarks &&
                washers ==  that.washers &&
                washersStatus ==  that.washersStatus &&
                washersRemarks ==  that.washersRemarks &&
                horn ==  that.horn &&
                hornStatus ==  that.hornStatus &&
                hornRemarks ==  that.hornRemarks &&
                wheelAlignment ==  that.wheelAlignment &&
                wheelAlignmentStatus ==  that.wheelAlignmentStatus &&
                wheelAlignmentRemarks ==  that.wheelAlignmentRemarks &&
                parkingBrake ==  that.parkingBrake &&
                parkingBrakeStatus ==  that.parkingBrakeStatus &&
                parkingBrakeRemarks ==  that.parkingBrakeRemarks &&
                frontTyres ==  that.frontTyres &&
                frontTyresStatus ==  that.frontTyresStatus &&
                frontTyresRemarks ==  that.frontTyresRemarks &&
                rearTyres1StAxle ==  that.rearTyres1StAxle &&
                rearTyres1StAxleStatus ==  that.rearTyres1StAxleStatus &&
                rearTyres1StAxleRemarks ==  that.rearTyres1StAxleRemarks &&
                rearTyres2StAxle ==  that.rearTyres2StAxle &&
                rearTyres2StAxleStatus ==  that.rearTyres2StAxleStatus &&
                rearTyres2StAxleRemarks ==  that.rearTyres2StAxleRemarks &&
                rearTyres3StAxle ==  that.rearTyres3StAxle &&
                rearTyres3StAxleStatus ==  that.rearTyres3StAxleStatus &&
                rearTyres3StAxleRemarks ==  that.rearTyres3StAxleRemarks &&
                rearTyres4StAxle ==  that.rearTyres4StAxle &&
                rearTyres4StAxleStatus ==  that.rearTyres4StAxleStatus &&
                rearTyres4StAxleRemarks ==  that.rearTyres4StAxleRemarks &&
                inspectionDate ==  that.inspectionDate &&
                varField1 ==  that.varField1 &&
                varField2 ==  that.varField2 &&
                varField3 ==  that.varField3 &&
                varField4 ==  that.varField4 &&
                varField5 ==  that.varField5 &&
                varField6 ==  that.varField6 &&
                varField7 ==  that.varField7 &&
                varField8 ==  that.varField8 &&
                varField10 ==  that.varField10 &&
                createdBy ==  that.createdBy &&
                createdOn ==  that.createdOn &&
                modifiedBy ==  that.modifiedBy &&
                modifiedOn ==  that.modifiedOn &&
                deleteBy ==  that.deleteBy &&
                deletedOn ==  that.deletedOn &&
                jackAndHandle == that.jackAndHandle &&
                jackAndHandleStatus == that.jackAndHandleStatus &&
                jackAndHandleRemarks == that.jackAndHandleRemarks
                );

        
    }

    override fun hashCode(): Int {
        return Objects.hash(id, overallAppearance, overallAppearanceStatus, overallAppearanceRemarks, conditionOfPaint, conditionOfPaintStatus, conditionOfPaintRemarks, doors, doorsStatus, doorsRemarks, windows, windowsStatus, windowsRemarks, sunroof, sunroofRemarks, sunroofStatus, externalMirrors,
                externalMirrorsStatus, externalMirrorsRemarks, glasses, glassesStatus, glassesRemarks, wipersWashers, wipersWashersStatus, wipersWashersRemarks, seats, seatsStatus, seatsRemarks, trimMoulding, trimMouldingStatus, trimMouldingRemarks, safetyBelts, safetyBeltsStatus, safetyBeltsRemarks,
                steeringWheel, steeringWheelStatus, steeringWheelRemarks, brakePedal, brakePedalStatus, brakePedalRemarks, clutchPedal, clutchPedalStatus, clutchPedalRemarks, parkingBrakeLever, parkingBrakeLeverStatus, parkingBrakeLeverRemarks, headlights, headlightsStatus, headlightsRemarks, parkingLights,
                parkingLightsStatus, parkingLightsRemarks, directionIndicators, directionIndicatorsStatus, directionIndicatorsRemarks, reversingLight, reversingLightStatus, reversingLightRemarks, courtesyLight, courtesyLightStatus, courtesyLightRemarks, rearNoPlateLight, rearNoPlateLightStatus, rearNoPlateLightRemarks,
                stopLights, stopLightsStatus, stopLightsRemarks, frontBumber, frontBumberStatus, frontBumberRemarks, roofRack, roofRackStatus, roofRackRemarks, antenna, antennaStatus, antennaRemarks, bonnet, bonnetStatus, bonnetRemarks, engine, engineStatus, engineRemarks, battery, batteryStatus, batteryRemarks, batteryCarrier,
                batteryCarrierStatus, batteryCarrierRemarks, wiringHarness, wiringHarnessStatus, wiringHarnessRemarks, starterMotor, starterMotorStatus, starterMotorRemarks, alternatorGenerator, alternatorGeneratorStatus, alternatorGeneratorRemarks, radiator, radiatorStatus, radiatorRemarks, radiatorHoses, radiatorHosesStatus,
                radiatorHosesRemarks, waterPump, waterPumpStatus, waterPumpRemarks, carburetor, carburetorStatus, carburetorRemarks, highTensionCables, highTensionCablesStatus, highTensionCablesRemarks, acCondenser, acCondenserStatus, acCondenserRemarks, powerSteering, powerSteeringStatus, powerSteeringRemarks, brakeMasterCylinder,
                brakeMasterCylinderStatus, brakeMasterCylinderRemarks, clutchMasterCylider, clutchMasterCyliderStatus, clutchMasterCyliderRemarks, brakeSystem, brakeSystemStatus, brakeSystemRemarks, fuelPipes, fuelPipesStatus, fuelPipesRemarks, flexibleBrakePipes, flexibleBrakePipesStatus, flexibleBrakePipesRemarks, windscreenWasherBottle,
                windscreenWasherBottleStatus, windscreenWasherBottleRemarks, bootLid, bootLidStatus, bootLidRemarks, lifeSaver, lifeSaverStatus, lifeSaverRemarks, radiotorWater, radiotorWaterStatus, radiotorWaterRemarks, engineOil, engineOilStatus, engineOilRemarks, startingEngine, startingEngineStatus, startingEngineRemarks, ideleSpeedNoises,
                ideleSpeedNoisesStatus, ideleSpeedNoisesRemarks, highSpeedNoises, highSpeedNoisesStatus, highSpeedNoisesRemarks, oilLeaks, oilLeaksStatus, oilLeaksRemarks, waterLeaks, waterLeaksStatus, waterLeaksRemarks, coolingSystemFunction, coolingSystemFunctionStatus, coolingSystemFunctionRemarks, engineOilPressure, engineOilPressureStatus,
                engineOilPressureRemarks, chargingSystem, chargingSystemStatus, chargingSystemRemarks, acOperation, acOperationStatus, acOperationRemarks, powerSteeringOperation, powerSteeringOperationStatus, powerSteeringOperationRemarks, fuelPump, fuelPumpStatus, fuelPumpRemarks, vacuumPump, vacuumPumpStatus, vacuumPumpRemarks, engineStopper, engineStopperStatus, engineStopperRemarks, exhaustEmission, exhaustEmissionStatus, exhaustEmissionRemarks, leafSprings, leafSpringsStatus, leafSpringsRemarks, uBolts, uBoltsStatus, uBoltsRemarks, springBushes, springBushesStatus, springBushesRemarks, springPins, springPinsStatus, springPinsRemarks, coilSprings, coilSpringsStatus, coilSpringsRemarks, frontShockAbsorbers, frontShockAbsorbersStatus, frontShockAbsorbersRemarks, rearShockAbsorbers, rearShockAbsorbersStatus, rearShockAbsorbersRemarks, subframeMountings, subframeMountingsStatus, subframeMountingsRemarks, engineMountings, engineMountingsStatus, engineMountingsRemarks, gearBoxMountings, gearBoxMountingsStatus, gearBoxMountingsRemarks, stabilizerBushes, stabilizerBushesStatus, stabilizerBushesRemarks, fuelTank, fuelTankStatus, fuelTankRemarks, fuelLines, fuelLinesStatus, fuelLinesRemarks, brakeLines, brakeLinesStatus, brakeLinesRemarks, exhaustSystem, exhaustSystemStatus, exhaustSystemRemarks, transmission, transmissionStatus, transmissionRemarks, steeringBox, steeringBoxStatus, steeringBoxRemarks, chassis, chassisStatus, chassisRemarks, monoblackBody, monoblackBodyStatus, monoblackBodyRemarks, frontSubFrame, frontSubFrameStatus, frontSubFrameRemarks, rearSubFrame, rearSubFrameStatus, rearSubFrameRemarks, frontAxle, frontAxleStatus, frontAxleRemarks, rearAxle, rearAxleStatus, rearAxleRemarks, secondRearAxle, secondRearAxleStatus, secondRearAxleRemarks, wheelGeometry, wheelGeometryStatus, wheelGeometryRemarks, kingpinBushes, kingpinBushesStatus, kingpinBushesRemarks, ballJoints, ballJointsStatus, ballJointsRemarks, steeringSystem, steeringSystemStatus, steeringSystemRemarks, brakeDisc, brakeDiscStatus, brakeDiscRemarks, calipers, calipersStatus, calipersRemarks, brakePadLife, brakePadLifeStatus, brakePadLifeRemarks, brakeDrums, brakeDrumsStatus, brakeDrumsRemarks, wheelCylinders, wheelCylindersStatus, wheelCylindersRemarks, rearOilSeals, rearOilSealsStatus, rearOilSealsRemarks, frontLhDrivingShaft, frontLhDrivingShaftStatus, frontLhDrivingShaftRemarks, frontRhDrivingShaft, frontRhDrivingShaftStatus, frontRhDrivingShaftRemarks, frontDifferencial, frontDifferencialStatus, frontDifferencialRemarks, rearDifferencial, rearDifferencialStatus, rearDifferencialRemarks, transferCase, transferCaseStatus, transferCaseRemarks, frontPropellerShaft, frontPropellerShaftStatus, frontPropellerShaftRemarks, rearPropellerShaft, rearPropellerShaftStatus, rearPropellerShaftRemarks, centreBearing, centreBearingStatus, centreBearingRemarks, frontRearOilSeals, frontRearOilSealsStatus, frontRearOilSealsRemarks, rearLhDriveShaft, rearLhDriveShaftStatus, rearLhDriveShaftRemarks, rearRhDriveShaft, rearRhDriveShaftStatus, rearRhDriveShaftRemarks, handBrakeCables, handBrakeCablesStatus, handBrakeCablesRemarks, drivingSeatAdjustments, drivingSeatAdjustmentsStatus, drivingSeatAdjustmentsRemarks, drivingPerformances, drivingPerformancesStatus, drivingPerformancesRemarks, emergencyBrake, emergencyBrakeStatus, emergencyBrakeRemarks, gearShift, gearShiftStatus, gearShiftRemarks, steeringStability, steeringStabilityStatus, steeringStabilityRemarks, frontSuspension, frontSuspensionStatus, frontSuspensionRemarks, rearSuspension, rearSuspensionStatus, rearSuspensionRemarks, gaugesAndInstruments, gaugesAndInstrumentsStatus, gaugesAndInstrumentsRemarks, odemeter, odemeterStatus, odemeterRemarks, heater, heaterStatus, heaterRemarks, defroster, defrosterStatus, defrosterRemarks, aircon, airconStatus, airconRemarks, windscreenWipers, windscreenWipersStatus, windscreenWipersRemarks, washers, washersStatus, washersRemarks, horn, hornStatus, hornRemarks, wheelAlignment, wheelAlignmentStatus, wheelAlignmentRemarks, parkingBrake, parkingBrakeStatus, parkingBrakeRemarks, frontTyres, frontTyresStatus, frontTyresRemarks, rearTyres1StAxle, rearTyres1StAxleStatus, rearTyres1StAxleRemarks, rearTyres2StAxle, rearTyres2StAxleStatus, rearTyres2StAxleRemarks, rearTyres3StAxle, rearTyres3StAxleStatus, rearTyres3StAxleRemarks, rearTyres4StAxle, rearTyres4StAxleStatus, rearTyres4StAxleRemarks, inspectionDate, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn,
                jackAndHandle,jackAndHandleStatus,jackAndHandleRemarks)
    }
}
