package org.kebs.app.kotlin.apollo.store.model.qa

import javax.persistence.*

@Embeddable
class QuestionnaireEmbeddable {

    @Column(name = "SAVE_REASON")
    @Basic
    var saveReason: String? = null


    @Column(name = "NO_OF_FEMALE")
    @Basic
    var noOffemale: Long? = null

    @Column(name = "NO_OF_MALE")
    @Basic
    var noOfmale: Long? = null

    @Column(name = "NO_OF_PERMANENT_WORKERS")
    @Basic
    var permanentWorkers: Long? = null

    @Column(name = "NO_OF_CASUAL_WORKERS")
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

    @Column(name = "COMPLAINTS_HANDLING")
    @Basic
    var handlingComplaints: String? = null

    @Column(name = "CERTIFICATE_FILE", nullable = true)
    @Basic
    var certificateFile: String? = null

    @Column(name = "TESTING_RECORD_FILE", nullable = true)
    @Basic
    var testingRecordFile: String? = null

    @Column(name = "PROCESS_MONITORING_RECORDS_FILE", nullable = true)
    @Basic
    var processMonitoringRecordsFile: String? = null




}