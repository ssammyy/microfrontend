package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_MMINISTRY_INSPECTION_ENGINE_COMPONENTS")
class MinistryInspectionEngineComponentsEntity : Serializable {
    @get:Column(name = "ID")
    @get:Id
    var id: Long = 0
    @get:Column(name = "STATUS")
    @get:Basic
    var status: Int? = null
    @get:Column(name = "GENERAR_INSPECTION")
    @get:Basic
    var generarInspection: Long? = null
    @get:Column(name = "BONNET")
    @get:Basic
    var bonnet: String? = null
    @get:Column(name = "BONNET_STATUS")
    @get:Basic
    var bonnetStatus: String? = null
    @get:Column(name = "BONNET_REMARKS")
    @get:Basic
    var bonnetRemarks: String? = null
    @get:Column(name = "ENGINE")
    @get:Basic
    var engine: String? = null
    @get:Column(name = "ENGINE_STATUS")
    @get:Basic
    var engineStatus: String? = null
    @get:Column(name = "ENGINE_REMARKS")
    @get:Basic
    var engineRemarks: String? = null
    @get:Column(name = "BATTERY")
    @get:Basic
    var battery: String? = null
    @get:Column(name = "BATTERY_STATUS")
    @get:Basic
    var batteryStatus: String? = null
    @get:Column(name = "BATTERY_REMARKS")
    @get:Basic
    var batteryRemarks: String? = null
    @get:Column(name = "BATTERY_CARRIER")
    @get:Basic
    var batteryCarrier: String? = null
    @get:Column(name = "BATTERY_CARRIER_STATUS")
    @get:Basic
    var batteryCarrierStatus: String? = null
    @get:Column(name = "BATTERY_CARRIER_REMARKS")
    @get:Basic
    var batteryCarrierRemarks: String? = null
    @get:Column(name = "WIRING_HARNESS")
    @get:Basic
    var wiringHarness: String? = null
    @get:Column(name = "WIRING_HARNESS_STATUS")
    @get:Basic
    var wiringHarnessStatus: String? = null
    @get:Column(name = "WIRING_HARNESS_REMARKS")
    @get:Basic
    var wiringHarnessRemarks: String? = null
    @get:Column(name = "STARTER_MOTOR")
    @get:Basic
    var starterMotor: String? = null
    @get:Column(name = "STARTER_MOTOR_STATUS")
    @get:Basic
    var starterMotorStatus: String? = null
    @get:Column(name = "STARTER_MOTOR_REMARKS")
    @get:Basic
    var starterMotorRemarks: String? = null
    @get:Column(name = "ALTERNATOR")
    @get:Basic
    var alternator: String? = null
    @get:Column(name = "ALTERNATOR_STATUS")
    @get:Basic
    var alternatorStatus: String? = null
    @get:Column(name = "ALTERNATOR_REMARKS")
    @get:Basic
    var alternatorRemarks: String? = null
    @get:Column(name = "RADIATOR")
    @get:Basic
    var radiator: String? = null
    @get:Column(name = "RADIATOR_STATUS")
    @get:Basic
    var radiatorStatus: String? = null
    @get:Column(name = "RADIATOR_REMARKS")
    @get:Basic
    var radiatorRemarks: String? = null
    @get:Column(name = "RADIOTOR_HOSES")
    @get:Basic
    var radiotorHoses: String? = null
    @get:Column(name = "RADIOTOR_HOSES_STATUS")
    @get:Basic
    var radiotorHosesStatus: String? = null
    @get:Column(name = "RADIOTOR_HOSES_REMARKS")
    @get:Basic
    var radiotorHosesRemarks: String? = null
    @get:Column(name = "WATER_PUMP")
    @get:Basic
    var waterPump: String? = null
    @get:Column(name = "WATER_PUMP_STATUS")
    @get:Basic
    var waterPumpStatus: String? = null
    @get:Column(name = "WATER_PUMP_REMARKS")
    @get:Basic
    var waterPumpRemarks: String? = null
    @get:Column(name = "CARBURETOR")
    @get:Basic
    var carburetor: String? = null
    @get:Column(name = "CARBURETOR_STATUS")
    @get:Basic
    var carburetorStatus: String? = null
    @get:Column(name = "CARBURETOR_REMARKS")
    @get:Basic
    var carburetorRemarks: String? = null
    @get:Column(name = "HIGH_TENSION_CABLES")
    @get:Basic
    var highTensionCables: String? = null
    @get:Column(name = "HIGH_TENSION_CABLES_STATUS")
    @get:Basic
    var highTensionCablesStatus: String? = null
    @get:Column(name = "HIGH_TENSION_CABLES_REMARKS")
    @get:Basic
    var highTensionCablesRemarks: String? = null
    @get:Column(name = "AC_CONDENSER")
    @get:Basic
    var acCondenser: String? = null
    @get:Column(name = "AC_CONDENSER_STATUS")
    @get:Basic
    var acCondenserStatus: String? = null
    @get:Column(name = "AC_CONDENSER_REMARKS")
    @get:Basic
    var acCondenserRemarks: String? = null
    @get:Column(name = "POWER_STEERING")
    @get:Basic
    var powerSteering: String? = null
    @get:Column(name = "POWER_STEERING_STATUS")
    @get:Basic
    var powerSteeringStatus: String? = null
    @get:Column(name = "POWER_STEERING_REMARKS")
    @get:Basic
    var powerSteeringRemarks: String? = null
    @get:Column(name = "BRAKE_MASTER_CYLINDER")
    @get:Basic
    var brakeMasterCylinder: String? = null
    @get:Column(name = "BRAKE_MASTER_CYLINDER_STATUS")
    @get:Basic
    var brakeMasterCylinderStatus: String? = null
    @get:Column(name = "BRAKE_MASTER_CYLINDER_REMARKS")
    @get:Basic
    var brakeMasterCylinderRemarks: String? = null
    @get:Column(name = "CLUTCH_MASTER_CYLINDER")
    @get:Basic
    var clutchMasterCylinder: String? = null
    @get:Column(name = "CLUTCH_MASTER_CYLINDER_STATUS")
    @get:Basic
    var clutchMasterCylinderStatus: String? = null
    @get:Column(name = "CLUTCH_MASTER_CYLINDER_REMARKS")
    @get:Basic
    var clutchMasterCylinderRemarks: String? = null
    @get:Column(name = "BRAKE_SYSTEM")
    @get:Basic
    var brakeSystem: String? = null
    @get:Column(name = "BRAKE_SYSTEM_STATUS")
    @get:Basic
    var brakeSystemStatus: String? = null
    @get:Column(name = "BRAKE_SYSTEM_REMARKS")
    @get:Basic
    var brakeSystemRemarks: String? = null
    @get:Column(name = "FUEL_PIPES")
    @get:Basic
    var fuelPipes: String? = null
    @get:Column(name = "FUEL_PIPES_STATUS")
    @get:Basic
    var fuelPipesStatus: String? = null
    @get:Column(name = "FUEL_PIPES_REMARKS")
    @get:Basic
    var fuelPipesRemarks: String? = null
    @get:Column(name = "FLEXIBLE_BRAKE_PIPES")
    @get:Basic
    var flexibleBrakePipes: String? = null
    @get:Column(name = "FLEXIBLE_BRAKE_PIPES_STATUS")
    @get:Basic
    var flexibleBrakePipesStatus: String? = null
    @get:Column(name = "FLEXIBLE_BRAKE_PIPES_REMARKS")
    @get:Basic
    var flexibleBrakePipesRemarks: String? = null
    @get:Column(name = "WINDSCREEN_WASHER_BOTTLE")
    @get:Basic
    var windscreenWasherBottle: String? = null
    @get:Column(name = "WINDSCREEN_WASHER_BOTTLE_STATUS")
    @get:Basic
    var windscreenWasherBottleStatus: String? = null
    @get:Column(name = "WINDSCREEN_WASHER_BOTTLE_REMARKS")
    @get:Basic
    var windscreenWasherBottleRemarks: String? = null
    @get:Column(name = "BOOT_LID")
    @get:Basic
    var bootLid: String? = null
    @get:Column(name = "BOOT_LID_STATUS")
    @get:Basic
    var bootLidStatus: String? = null
    @get:Column(name = "BOOT_LID_REMARKS")
    @get:Basic
    var bootLidRemarks: String? = null
    @get:Column(name = "JACK_AND_HANDLE")
    @get:Basic
    var jackAndHandle: String? = null
    @get:Column(name = "JACK_AND_HANDLE_STATUS")
    @get:Basic
    var jackAndHandleStatus: String? = null
    @get:Column(name = "JACK_AND_HANDLE_REMARKS")
    @get:Basic
    var jackAndHandleRemarks: String? = null
    @get:Column(name = "WHEEL_WRENCH")
    @get:Basic
    var wheelWrench: String? = null
    @get:Column(name = "WHEEL_WRENCH_STATUS")
    @get:Basic
    var wheelWrenchStatus: String? = null
    @get:Column(name = "WHEEL_WRENCH_REMARKS")
    @get:Basic
    var wheelWrenchRemarks: String? = null
    @get:Column(name = "TOOL_KIT")
    @get:Basic
    var toolKit: String? = null
    @get:Column(name = "TOOL_KIT_STATUS")
    @get:Basic
    var toolKitStatus: String? = null
    @get:Column(name = "TOOL_KIT_REMARKS")
    @get:Basic
    var toolKitRemarks: String? = null
    @get:Column(name = "LIFE_SAVER")
    @get:Basic
    var lifeSaver: String? = null
    @get:Column(name = "LIFE_SAVER_STATUS")
    @get:Basic
    var lifeSaverStatus: String? = null
    @get:Column(name = "LIFE_SAVER_REMARKS")
    @get:Basic
    var lifeSaverRemarks: String? = null
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
        val that = o as MinistryInspectionEngineComponentsEntity
        return id == that.id &&
                status == that.status &&
                generarInspection == that.generarInspection &&
                bonnet == that.bonnet &&
                bonnetStatus == that.bonnetStatus &&
                bonnetRemarks == that.bonnetRemarks &&
                engine == that.engine &&
                engineStatus == that.engineStatus &&
                engineRemarks == that.engineRemarks &&
                battery == that.battery &&
                batteryStatus == that.batteryStatus &&
                batteryRemarks == that.batteryRemarks &&
                batteryCarrier == that.batteryCarrier &&
                batteryCarrierStatus == that.batteryCarrierStatus &&
                batteryCarrierRemarks == that.batteryCarrierRemarks &&
                wiringHarness == that.wiringHarness &&
                wiringHarnessStatus == that.wiringHarnessStatus &&
                wiringHarnessRemarks == that.wiringHarnessRemarks &&
                starterMotor == that.starterMotor &&
                starterMotorStatus == that.starterMotorStatus &&
                starterMotorRemarks == that.starterMotorRemarks &&
                alternator == that.alternator &&
                alternatorStatus == that.alternatorStatus &&
                alternatorRemarks == that.alternatorRemarks &&
                radiator == that.radiator &&
                radiatorStatus == that.radiatorStatus &&
                radiatorRemarks == that.radiatorRemarks &&
                radiotorHoses == that.radiotorHoses &&
                radiotorHosesStatus == that.radiotorHosesStatus &&
                radiotorHosesRemarks == that.radiotorHosesRemarks &&
                waterPump == that.waterPump &&
                waterPumpStatus == that.waterPumpStatus &&
                waterPumpRemarks == that.waterPumpRemarks &&
                carburetor == that.carburetor &&
                carburetorStatus == that.carburetorStatus &&
                carburetorRemarks == that.carburetorRemarks &&
                highTensionCables == that.highTensionCables &&
                highTensionCablesStatus == that.highTensionCablesStatus &&
                highTensionCablesRemarks == that.highTensionCablesRemarks &&
                acCondenser == that.acCondenser &&
                acCondenserStatus == that.acCondenserStatus &&
                acCondenserRemarks == that.acCondenserRemarks &&
                powerSteering == that.powerSteering &&
                powerSteeringStatus == that.powerSteeringStatus &&
                powerSteeringRemarks == that.powerSteeringRemarks &&
                brakeMasterCylinder == that.brakeMasterCylinder &&
                brakeMasterCylinderStatus == that.brakeMasterCylinderStatus &&
                brakeMasterCylinderRemarks == that.brakeMasterCylinderRemarks &&
                clutchMasterCylinder == that.clutchMasterCylinder &&
                clutchMasterCylinderStatus == that.clutchMasterCylinderStatus &&
                clutchMasterCylinderRemarks == that.clutchMasterCylinderRemarks &&
                brakeSystem == that.brakeSystem &&
                brakeSystemStatus == that.brakeSystemStatus &&
                brakeSystemRemarks == that.brakeSystemRemarks &&
                fuelPipes == that.fuelPipes &&
                fuelPipesStatus == that.fuelPipesStatus &&
                fuelPipesRemarks == that.fuelPipesRemarks &&
                flexibleBrakePipes == that.flexibleBrakePipes &&
                flexibleBrakePipesStatus == that.flexibleBrakePipesStatus &&
                flexibleBrakePipesRemarks == that.flexibleBrakePipesRemarks &&
                windscreenWasherBottle == that.windscreenWasherBottle &&
                windscreenWasherBottleStatus == that.windscreenWasherBottleStatus &&
                windscreenWasherBottleRemarks == that.windscreenWasherBottleRemarks &&
                bootLid == that.bootLid &&
                bootLidStatus == that.bootLidStatus &&
                bootLidRemarks == that.bootLidRemarks &&
                jackAndHandle == that.jackAndHandle &&
                jackAndHandleStatus == that.jackAndHandleStatus &&
                jackAndHandleRemarks == that.jackAndHandleRemarks &&
                wheelWrench == that.wheelWrench &&
                wheelWrenchStatus == that.wheelWrenchStatus &&
                wheelWrenchRemarks == that.wheelWrenchRemarks &&
                toolKit == that.toolKit &&
                toolKitStatus == that.toolKitStatus &&
                toolKitRemarks == that.toolKitRemarks &&
                lifeSaver == that.lifeSaver &&
                lifeSaverStatus == that.lifeSaverStatus &&
                lifeSaverRemarks == that.lifeSaverRemarks &&
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
                bonnet, bonnetStatus, bonnetRemarks, engine, engineStatus, engineRemarks, battery,
                batteryStatus, batteryRemarks, batteryCarrier, batteryCarrierStatus, batteryCarrierRemarks,
                wiringHarness, wiringHarnessStatus, wiringHarnessRemarks, starterMotor, starterMotorStatus,
                starterMotorRemarks, alternator, alternatorStatus, alternatorRemarks, radiator, radiatorStatus,
                radiatorRemarks, radiotorHoses, radiotorHosesStatus, radiotorHosesRemarks, waterPump, waterPumpStatus, waterPumpRemarks,
                carburetor, carburetorStatus, carburetorRemarks, highTensionCables, highTensionCablesStatus, highTensionCablesRemarks,
                acCondenser, acCondenserStatus, acCondenserRemarks, powerSteering, powerSteeringStatus, powerSteeringRemarks, brakeMasterCylinder,
                brakeMasterCylinderStatus, brakeMasterCylinderRemarks, clutchMasterCylinder, clutchMasterCylinderStatus, clutchMasterCylinderRemarks,
                brakeSystem, brakeSystemStatus, brakeSystemRemarks, fuelPipes, fuelPipesStatus, fuelPipesRemarks, flexibleBrakePipes, flexibleBrakePipesStatus,
                flexibleBrakePipesRemarks, windscreenWasherBottle, windscreenWasherBottleStatus, windscreenWasherBottleRemarks, bootLid, bootLidStatus, bootLidRemarks,
                jackAndHandle, jackAndHandleStatus, jackAndHandleRemarks, wheelWrench, wheelWrenchStatus, wheelWrenchRemarks, toolKit, toolKitStatus, toolKitRemarks,
                lifeSaver, lifeSaverStatus, lifeSaverRemarks, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn, section)
    }
}