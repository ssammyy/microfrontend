package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_MMINISTRY_INSPECTION_ENGINE_FUNCTIONING")
class MinistryInspectionEngineFunctioningEntity :Serializable {
    @get:Column(name = "ID")
    @get:Id
    var id: Long = 0
    @get:Column(name = "STATUS")
    @get:Basic
    var status: Int? = null
    @get:Column(name = "GENERAR_INSPECTION")
    @get:Basic
    var generarInspection: Long? = null
    @get:Column(name = "RADIATOR_WATER")
    @get:Basic
    var radiatorWater: String? = null
    @get:Column(name = "RADIATOR_WATER_STATUS")
    @get:Basic
    var radiatorWaterStatus: String? = null
    @get:Column(name = "RADIATOR_WATER_REMARKS")
    @get:Basic
    var radiatorWaterRemarks: String? = null
    @get:Column(name = "ENGINE_OIL")
    @get:Basic
    var engineOil: String? = null
    @get:Column(name = "ENGINE_OIL_STATUS")
    @get:Basic
    var engineOilStatus: String? = null
    @get:Column(name = "ENGINE_OIL_REMARKS")
    @get:Basic
    var engineOilRemarks: String? = null
    @get:Column(name = "STARTING_ENGINE")
    @get:Basic
    var startingEngine: String? = null
    @get:Column(name = "STARTING_ENGINE_STATUS")
    @get:Basic
    var startingEngineStatus: String? = null
    @get:Column(name = "STARTING_ENGINE_REMARKS")
    @get:Basic
    var startingEngineRemarks: String? = null
    @get:Column(name = "IDLE_SPEED_NOISES")
    @get:Basic
    var idleSpeedNoises: String? = null
    @get:Column(name = "IDLE_SPEED_NOISES_STATUS")
    @get:Basic
    var idleSpeedNoisesStatus: String? = null
    @get:Column(name = "IDLE_SPEED_NOISES_REMARKS")
    @get:Basic
    var idleSpeedNoisesRemarks: String? = null
    @get:Column(name = "HIGH_SPEED_NOISES")
    @get:Basic
    var highSpeedNoises: String? = null
    @get:Column(name = "HIGH_SPEED_NOISES_STATUS")
    @get:Basic
    var highSpeedNoisesStatus: String? = null
    @get:Column(name = "HIGH_SPEED_NOISES_REMARKS")
    @get:Basic
    var highSpeedNoisesRemarks: String? = null
    @get:Column(name = "OIL_LEAKS")
    @get:Basic
    var oilLeaks: String? = null
    @get:Column(name = "OIL_LEAKS_STATUS")
    @get:Basic
    var oilLeaksStatus: String? = null
    @get:Column(name = "OIL_LEAKS_REMARKS")
    @get:Basic
    var oilLeaksRemarks: String? = null
    @get:Column(name = "WATER_LEAKS")
    @get:Basic
    var waterLeaks: String? = null
    @get:Column(name = "WATER_LEAKS_STATUS")
    @get:Basic
    var waterLeaksStatus: String? = null
    @get:Column(name = "WATER_LEAKS_REMARKS")
    @get:Basic
    var waterLeaksRemarks: String? = null
    @get:Column(name = "COOLING_SYSTEM_FUNCTION")
    @get:Basic
    var coolingSystemFunction: String? = null
    @get:Column(name = "COOLING_SYSTEM_FUNCTION_STATUS")
    @get:Basic
    var coolingSystemFunctionStatus: String? = null
    @get:Column(name = "COOLING_SYSTEM_FUNCTION_REMARKS")
    @get:Basic
    var coolingSystemFunctionRemarks: String? = null
    @get:Column(name = "ENGINE_OIL_PRESSURE")
    @get:Basic
    var engineOilPressure: String? = null
    @get:Column(name = "ENGINE_OIL_PRESSURE_STATUS")
    @get:Basic
    var engineOilPressureStatus: String? = null
    @get:Column(name = "ENGINE_OIL_PRESSURE_REMARKS")
    @get:Basic
    var engineOilPressureRemarks: String? = null
    @get:Column(name = "CHARGING_SYSTEM")
    @get:Basic
    var chargingSystem: String? = null
    @get:Column(name = "CHARGING_SYSTEM_STATUS")
    @get:Basic
    var chargingSystemStatus: String? = null
    @get:Column(name = "CHARGING_SYSTEM_REMARKS")
    @get:Basic
    var chargingSystemRemarks: String? = null
    @get:Column(name = "AC_OPERATION")
    @get:Basic
    var acOperation: String? = null
    @get:Column(name = "AC_OPERATION_STATUS")
    @get:Basic
    var acOperationStatus: String? = null
    @get:Column(name = "AC_OPERATION_REMARKS")
    @get:Basic
    var acOperationRemarks: String? = null
    @get:Column(name = "POWER_STEERING_OPERATION")
    @get:Basic
    var powerSteeringOperation: String? = null
    @get:Column(name = "POWER_STEERING_OPERATION_STATUS")
    @get:Basic
    var powerSteeringOperationStatus: String? = null
    @get:Column(name = "POWER_STEERING_OPERATION_REMARKS")
    @get:Basic
    var powerSteeringOperationRemarks: String? = null
    @get:Column(name = "FUEL_PUMP")
    @get:Basic
    var fuelPump: String? = null
    @get:Column(name = "FUEL_PUMP_STATUS")
    @get:Basic
    var fuelPumpStatus: String? = null
    @get:Column(name = "FUEL_PUMP_REMARKS")
    @get:Basic
    var fuelPumpRemarks: String? = null
    @get:Column(name = "VACUUM_PUMP")
    @get:Basic
    var vacuumPump: String? = null
    @get:Column(name = "VACUUM_PUMP_STATUS")
    @get:Basic
    var vacuumPumpStatus: String? = null
    @get:Column(name = "VACUUM_PUMP_REMARKS")
    @get:Basic
    var vacuumPumpRemarks: String? = null
    @get:Column(name = "ENGINE_STOPPER")
    @get:Basic
    var engineStopper: String? = null
    @get:Column(name = "ENGINE_STOPPER_STATUS")
    @get:Basic
    var engineStopperStatus: String? = null
    @get:Column(name = "ENGINE_STOPPER_REMARKS")
    @get:Basic
    var engineStopperRemarks: String? = null
    @get:Column(name = "EXHAUST_EMISSION")
    @get:Basic
    var exhaustEmission: String? = null
    @get:Column(name = "EXHAUST_EMISSION_STATUS")
    @get:Basic
    var exhaustEmissionStatus: String? = null
    @get:Column(name = "EXHAUST_EMISSION_REMARKS")
    @get:Basic
    var exhaustEmissionRemarks: String? = null
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
        val that = o as MinistryInspectionEngineFunctioningEntity
        return id == that.id &&
                status == that.status &&
                generarInspection == that.generarInspection &&
                radiatorWater == that.radiatorWater &&
                radiatorWaterStatus == that.radiatorWaterStatus &&
                radiatorWaterRemarks == that.radiatorWaterRemarks &&
                engineOil == that.engineOil &&
                engineOilStatus == that.engineOilStatus &&
                engineOilRemarks == that.engineOilRemarks &&
                startingEngine == that.startingEngine &&
                startingEngineStatus == that.startingEngineStatus &&
                startingEngineRemarks == that.startingEngineRemarks &&
                idleSpeedNoises == that.idleSpeedNoises &&
                idleSpeedNoisesStatus == that.idleSpeedNoisesStatus &&
                idleSpeedNoisesRemarks == that.idleSpeedNoisesRemarks &&
                highSpeedNoises == that.highSpeedNoises &&
                highSpeedNoisesStatus == that.highSpeedNoisesStatus &&
                highSpeedNoisesRemarks == that.highSpeedNoisesRemarks &&
                oilLeaks == that.oilLeaks &&
                oilLeaksStatus == that.oilLeaksStatus &&
                oilLeaksRemarks == that.oilLeaksRemarks &&
                waterLeaks == that.waterLeaks &&
                waterLeaksStatus == that.waterLeaksStatus &&
                waterLeaksRemarks == that.waterLeaksRemarks &&
                coolingSystemFunction == that.coolingSystemFunction &&
                coolingSystemFunctionStatus == that.coolingSystemFunctionStatus &&
                coolingSystemFunctionRemarks == that.coolingSystemFunctionRemarks &&
                engineOilPressure == that.engineOilPressure &&
                engineOilPressureStatus == that.engineOilPressureStatus &&
                engineOilPressureRemarks == that.engineOilPressureRemarks &&
                chargingSystem == that.chargingSystem &&
                chargingSystemStatus == that.chargingSystemStatus &&
                chargingSystemRemarks == that.chargingSystemRemarks &&
                acOperation == that.acOperation &&
                acOperationStatus == that.acOperationStatus &&
                acOperationRemarks == that.acOperationRemarks &&
                powerSteeringOperation == that.powerSteeringOperation &&
                powerSteeringOperationStatus == that.powerSteeringOperationStatus &&
                powerSteeringOperationRemarks == that.powerSteeringOperationRemarks &&
                fuelPump == that.fuelPump &&
                fuelPumpStatus == that.fuelPumpStatus &&
                fuelPumpRemarks == that.fuelPumpRemarks &&
                vacuumPump == that.vacuumPump &&
                vacuumPumpStatus == that.vacuumPumpStatus &&
                vacuumPumpRemarks == that.vacuumPumpRemarks &&
                engineStopper == that.engineStopper &&
                engineStopperStatus == that.engineStopperStatus &&
                engineStopperRemarks == that.engineStopperRemarks &&
                exhaustEmission == that.exhaustEmission &&
                exhaustEmissionStatus == that.exhaustEmissionStatus &&
                exhaustEmissionRemarks == that.exhaustEmissionRemarks &&
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
        return Objects.hash(id, status, generarInspection, radiatorWater, radiatorWaterStatus, radiatorWaterRemarks,
                engineOil, engineOilStatus, engineOilRemarks, startingEngine, startingEngineStatus, startingEngineRemarks,
                idleSpeedNoises, idleSpeedNoisesStatus, idleSpeedNoisesRemarks, highSpeedNoises, highSpeedNoisesStatus,
                highSpeedNoisesRemarks, oilLeaks, oilLeaksStatus, oilLeaksRemarks, waterLeaks, waterLeaksStatus, waterLeaksRemarks,
                coolingSystemFunction, coolingSystemFunctionStatus, coolingSystemFunctionRemarks,
                engineOilPressure, engineOilPressureStatus, engineOilPressureRemarks, chargingSystem, chargingSystemStatus, chargingSystemRemarks,
                acOperation, acOperationStatus, acOperationRemarks, powerSteeringOperation, powerSteeringOperationStatus, powerSteeringOperationRemarks,
                fuelPump, fuelPumpStatus, fuelPumpRemarks, vacuumPump, vacuumPumpStatus, vacuumPumpRemarks, engineStopper, engineStopperStatus, engineStopperRemarks,
                exhaustEmission, exhaustEmissionStatus, exhaustEmissionRemarks, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn, section)
    }
}