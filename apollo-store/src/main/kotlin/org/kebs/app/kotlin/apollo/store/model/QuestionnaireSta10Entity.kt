package org.kebs.app.kotlin.apollo.store.model

import org.hibernate.annotations.DynamicUpdate
import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@DynamicUpdate
@Table(name = "DAT_QUESTIONNAIRE_STA_10")
class QuestionnaireSta10Entity: Serializable {
    @Column(name = "ID")
    @Id
    @SequenceGenerator(name = "DAT_QUESTIONNAIRE_STA_10_SEQ_GEN", sequenceName = "DAT_QUESTIONNAIRE_STA_10_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_QUESTIONNAIRE_STA_10_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long? = 0

    @Column(name = "STATUS")
    @Basic
    var status: Long? = null

    @Column(name = "SAVE_REASON")
    @Basic
    var saveReason: String? = null
//
//    @Column(name = "PERMIT_ID")
//    @Basic
//    var permitId: Long? = null

//    @JoinColumn(name = "PERMIT_ID", referencedColumnName = "ID")
//    @ManyToOne
//    var permitId: PermitApplicationEntity? = null
    @Column(name = "PERMIT_ID")
    @Basic
    var permitId: Long? = null

    @Column(name = "TOTAL_NUMBER")
    @Basic
    var totalNumber: Long? = null

    @Column(name = "FEMALE")
    @Basic
    var female: Long? = null

    @Column(name = "MALE")
    @Basic
    var male: Long? = null

    @Column(name = "PERMANENT_WORKERS")
    @Basic
    var permanentWorkers: Long? = null

    @Column(name = "CASUAL_WORKERS")
    @Basic
    var casualWorkers: Long? = null

    @Column(name = "NAME")
    @Basic
    var name: String? = null

    @Column(name = "DATE_OF_EMPLOYMENT")
    @Basic
    var dateOfEmployment: String? = null

    @Column(name = "CERTIFICATE")
    @Basic
    var certificate: String? = null

    @Column(name = "PRODUCT_NAME")
    @Basic
    var productName: String? = null

    @Column(name = "BRAND_NAME")
    @Basic
    var brandName: String? = null

    @Column(name = "KS")
    @Basic
    var ks: String? = null

    @Column(name = "TITLE")
    @Basic
    var title: String? = null

    @Column(name = "KEBS_PERMIT_NO")
    @Basic
    var kebsPermitNo: String? = null

    @Column(name = "VOLUME_OF_PRODUCTION_PER_MONTH")
    @Basic
    var volumeOfProductionPerMonth: String? = null

    @Column(name = "RAW_MATERIAL")
    @Basic
    var rawMaterial: String? = null

    @Column(name = "RAW_MATERIAL_ORIGIN")
    @Basic
    var rawMaterialOrigin: String? = null

    @Column(name = "RAW_MATERIAL_SPECIFICATIONS")
    @Basic
    var rawMaterialSpecifications: String? = null

    @Column(name = "QUALITY_CHECKS")
    @Basic
    var qualityChecks: String? = null

    @Column(name = "MACHINE")
    @Basic
    var machine: String? = null

    @Column(name = "MACHINE_TYPE")
    @Basic
    var machineType: String? = null

    @Column(name = "MACHINE_MODEL")
    @Basic
    var machineModel: String? = null

    @Column(name = "MACHINE_COUNRTY_OF_ORIGIN")
    @Basic
    var machineCounrtyOfOrigin: String? = null

    @Column(name = "PROCESS_FLOW_OF_PRODUCTION")
    @Basic
    var processFlowOfProduction: String? = null

    @Column(name = "OPERATIONS")
    @Basic
    var operations: String? = null

    @Column(name = "CRITICAL_PROCESS_PARAMENTERS")
    @Basic
    var criticalProcessParamenters: String? = null

    @Column(name = "FREQUENCY")
    @Basic
    var frequency: String? = null

    @Column(name = "PROCESS_MONITORING_RECORDS")
    @Basic
    var processMonitoringRecords: String? = null

    @Column(name = "RAW_MATERIALS_HANDLING")
    @Basic
    var rawMaterialsHandling: String? = null

    @Column(name = "RAW_MATERIALS_IN_PROCESS_PRODUCT")
    @Basic
    var rawMaterialsInProcessProduct: String? = null

    @Column(name = "RAW_MATERIALS_FINAL_PRODUCT")
    @Basic
    var rawMaterialsFinalProduct: String? = null

    @Column(name = "STRATEGY_FOR_RECALLING_PRODUCTS")
    @Basic
    var strategyForRecallingProducts: String? = null

    @Column(name = "STORAGE_OF_RAW_MATERIALS")
    @Basic
    var storageOfRawMaterials: String? = null

    @Column(name = "STORAGE_OF_RAW_PRODUCTS")
    @Basic
    var storageOfRawProducts: String? = null

    @Column(name = "TESTING_EQUIPMENT")
    @Basic
    var testingEquipment: String? = null

    @Column(name = "TESTING_PARAMETERS")
    @Basic
    var testingParameters: String? = null

    @Column(name = "TESTING_ARRANGEMENTS")
    @Basic
    var testingArrangements: String? = null

    @Column(name = "CALIBRATION_OF_EQUIPMENT")
    @Basic
    var calibrationOfEquipment: String? = null

    @Column(name = "HANDLING_COMPLAINTS")
    @Basic
    var handlingComplaints: String? = null

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

    @Column(name = "CERTIFICATE_FILE", nullable = true)
    @Basic
    var certificateFile: String? = null

    @Column(name = "TESTING_RECORD_FILE", nullable = true)
    @Basic
    var testingRecordFile: String? = null

    @Column(name = "PROCESS_MONITORING_RECORDS_FILE", nullable = true)
    @Basic
    var processMonitoringRecordsFile: String? = null


    @Column(name = "FREQUENCY_UPLOAD", nullable = true)
    @Basic
    var frequencyUpload: String? = null
    @Column(name = "CRITICAL_PROCESS_PARAMETERS_UPLOAD", nullable = true)
    @Basic
    var criticalProcessParametersUpload: String? = null
    @Column(name = "OPERATIONS_UPLOAD", nullable = true)
    @Basic
    var operationsUpload: String? = null
    @Column(name = "PROCESS_FLOW_OF_PRODUCTION_UPLOAD", nullable = true)
    @Basic
    var processFlowOfProductionUpload: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as QuestionnaireSta10Entity
        return id == that.id &&
                operationsUpload == that.operationsUpload &&
                criticalProcessParametersUpload == that.criticalProcessParametersUpload &&
                frequencyUpload == that.frequencyUpload &&
                processMonitoringRecordsFile == that.processMonitoringRecordsFile &&
                testingRecordFile == that.testingRecordFile &&
                certificateFile == that.certificateFile &&
//                permitId == that.permitId &&
                processMonitoringRecords == that.processMonitoringRecords &&
                frequency == that.frequency &&
                status == that.status &&
                totalNumber == that.totalNumber &&
                female == that.female &&
                male == that.male &&
                permanentWorkers == that.permanentWorkers &&
                casualWorkers == that.casualWorkers &&
                name == that.name &&
                dateOfEmployment == that.dateOfEmployment &&
                certificate == that.certificate &&
                productName == that.productName &&
                brandName == that.brandName &&
                ks == that.ks &&
                title == that.title &&
                kebsPermitNo == that.kebsPermitNo &&
                volumeOfProductionPerMonth == that.volumeOfProductionPerMonth &&
                rawMaterial == that.rawMaterial &&
                rawMaterialOrigin == that.rawMaterialOrigin &&
                rawMaterialSpecifications == that.rawMaterialSpecifications &&
                qualityChecks == that.qualityChecks &&
                machine == that.machine &&
                machineType == that.machineType &&
                machineModel == that.machineModel &&
                machineCounrtyOfOrigin == that.machineCounrtyOfOrigin &&
                processFlowOfProduction == that.processFlowOfProduction &&
                operations == that.operations &&
                criticalProcessParamenters == that.criticalProcessParamenters &&
                rawMaterialsHandling == that.rawMaterialsHandling &&
                rawMaterialsInProcessProduct == that.rawMaterialsInProcessProduct &&
                rawMaterialsFinalProduct == that.rawMaterialsFinalProduct &&
                strategyForRecallingProducts == that.strategyForRecallingProducts &&
                storageOfRawMaterials == that.storageOfRawMaterials &&
                storageOfRawProducts == that.storageOfRawProducts &&
                testingEquipment == that.testingEquipment &&
                testingParameters == that.testingParameters &&
                testingArrangements == that.testingArrangements &&
                calibrationOfEquipment == that.calibrationOfEquipment &&
                handlingComplaints == that.handlingComplaints &&
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
        return Objects.hash(id, status, certificateFile, processFlowOfProductionUpload, operationsUpload, criticalProcessParametersUpload, frequencyUpload, processMonitoringRecordsFile, testingRecordFile, totalNumber, female, male, permanentWorkers, casualWorkers, name, dateOfEmployment, certificate, productName, brandName, ks, title, kebsPermitNo, volumeOfProductionPerMonth, rawMaterial, rawMaterialOrigin, rawMaterialSpecifications, qualityChecks, machine, machineType, machineModel, machineCounrtyOfOrigin, processFlowOfProduction, operations, criticalProcessParamenters, rawMaterialsHandling, rawMaterialsInProcessProduct, rawMaterialsFinalProduct, strategyForRecallingProducts, storageOfRawMaterials, storageOfRawProducts, testingEquipment, testingParameters, testingArrangements, calibrationOfEquipment, handlingComplaints, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn, frequency, processMonitoringRecords
//                ,permitId
        )
    }
}