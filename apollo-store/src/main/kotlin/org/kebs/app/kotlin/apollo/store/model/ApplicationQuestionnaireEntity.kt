package org.kebs.app.kotlin.apollo.store.model

import org.hibernate.annotations.DynamicUpdate
import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@DynamicUpdate
@Table(name = "DAT_KEBS_APPLICATION_QUESTIONNAIRE")
class ApplicationQuestionnaireEntity : Serializable{
    @Column(name = "ID")
    @Id
    @SequenceGenerator(name = "DAT_KEBS_APPLICATION_QUESTIONNAIRE_SEQ_GEN", sequenceName = "DAT_KEBS_APPLICATION_QUESTIONNAIRE_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_APPLICATION_QUESTIONNAIRE_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long? = 0

    @Column(name = "AGAINST_OR_FOR_STOCK")
    @Basic
    var againstOrForStock: String? = null

    @Column(name = "STATUS")
    @Basic
    var status: Long? = null

    @Column(name = "WORK_OR_EQUIVALENT")
    @Basic
    var workOrEquivalent: String? = null

    @Column(name = "AGAINST_OR_FOR_STOCK_MORE_INFO")
    @Basic
    var againstOrForStockMoreInfo: String? = null

    @Column(name = "WORK_OR_EQUIVALENT_MORE_INFO")
    @Basic
    var workOrEquivalentMoreInfo: String? = null

    @Column(name = "QUALITY_AUDIT_CHECKS_MORE_INFO")
    @Basic
    var qualityAuditChecksMoreInfo: String? = null

    @Column(name = "SEPARATE_ENTITY")
    @Basic
    var separateEntity: String? = null

    @Column(name = "DOUBTFUL_QUALITY")
    @Basic
    var doubtfulQuality: String? = null

    @Column(name = "STAFF_ORGANIZATION")
    @Basic
    var staffOrganization: String? = null

    @Column(name = "REPORTING_TO")
    @Basic
    var reportingTo: String? = null

    @Column(name = "CARRY_WORD_ORDER_IDENTIFICATION")
    @Basic
    var carryWordOrderIdentification: String? = null

    @Column(name = "AWARENESS_OF_TESTS")
    @Basic
    var awarenessOfTests: String? = null

    @Column(name = "INCOMING_MATERIALS")
    @Basic
    var incomingMaterials: String? = null

    @Column(name = "IN_PROCESS_APPLICATIONS")
    @Basic
    var inProcessApplications: String? = null

    @Column(name = "FINAL_PRODUCTS")
    @Basic
    var finalProducts: String? = null

    @Column(name = "MONITORED_BY_QUALITY_CONTROL_STAFF")
    @Basic
    var monitoredByQualityControlStaff: String? = null

    @Column(name = "QUALITY_AUDIT_CHECKS")
    @Basic
    var qualityAuditChecks: String? = null

    @Column(name = "INFO_ONQUALITY_CONTROL_STAFF")
    @Basic
    var infoOnqualityControlStaff: String? = null

    @Column(name = "MATERIALS_PURCHASED")
    @Basic
    var materialsPurchased: String? = null

    @Column(name = "QUALITY_ASSURANCE_METHOD")
    @Basic
    var qualityAssuranceMethod: String? = null

    @Column(name = "EXISTING_STORAGE_FACILITIES")
    @Basic
    var existingStorageFacilities: String? = null

    @Column(name = "MANUFACTURING_STEPS")
    @Basic
    var manufacturingSteps: String? = null

    @Column(name = "OPERATING_MAINTENANCE_SYSTEM")
    @Basic
    var operatingMaintenanceSystem: String? = null

    @Column(name = "QUALITY_CONTROL_SYSTEM")
    @Basic
    var qualityControlSystem: String? = null

    @Column(name = "QUALITY_MANUAL")
    @Basic
    var qualityManual: String? = null

    @Column(name = "TEST_EQUIPMENT")
    @Basic
    var testEquipment: String? = null

    @Column(name = "EXTERNAL_ARRANGEMENT_OR_TESTING")
    @Basic
    var externalArrangementOrTesting: String? = null

    @Column(name = "LEVEL_OF_DEFECTIVES")
    @Basic
    var levelOfDefectives: String? = null

    @Column(name = "LEVEL_OF_CLAIMS")
    @Basic
    var levelOfClaims: String? = null

    @Column(name = "INDEPENDENT_TESTS")
    @Basic
    var independentTests: String? = null

    @Column(name = "VAR_FIELD_1")
    @Basic
    var varField1: String? = null

    @Column(name = "VAR_FIELD_2")
    @Basic
    var varField2: String? = null

    @Column(name = "SAVE_REASON")
    @Basic
    var saveReason: String? = null

//    @Column(name = "PERMIT_ID")
//    @Basic
//    var permitId: Long? = null

//    @JoinColumn(name = "PERMIT_ID", referencedColumnName = "ID")
//    @ManyToOne
//    var permitId: PermitApplicationEntity? = null
    @Column(name = "PERMIT_ID")
    @Basic
    var permitId: Long? = null


//    @JoinColumn(name = "DMARK_FOREIGN_ID", referencedColumnName = "ID")
//    @ManyToOne
//    var dmarkForeignId: DmarkForeignApplicationsEntity? = null
    @Column(name = "DMARK_FOREIGN_ID")
    @Basic
    var dmarkForeignId: Long? = null

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

    @Column(name = "LAST_MODIFIED_BY")
    @Basic
    var lastModifiedBy: String? = null

    @Column(name = "LAST_MODIFIED_ON")
    @Basic
    var lastModifiedOn: Timestamp? = null

    @Column(name = "UPDATE_BY")
    @Basic
    var updateBy: String? = null

    @Column(name = "UPDATED_ON")
    @Basic
    var updatedOn: Timestamp? = null

    @Column(name = "DELETE_BY")
    @Basic
    var deleteBy: String? = null

    @Column(name = "DELETED_ON")
    @Basic
    var deletedOn: Timestamp? = null

    @JoinColumn(name = "MANUFACTURER_ID", referencedColumnName = "ID")
    @ManyToOne
    var datKebsManufacturersByManufacturerId: ManufacturersEntity? = null

    @Column(name = "MANUFACTURING_STEPS_FILE")
    @Basic
    var manufacturingStepsFile: String? = null

    @Column(name = "QUALITY_MANUAL_FILE")
    @Basic
    var qualityManualFile: String? = null

    @Column(name = "TEST_REPORTS_LEVEL_OF_DEFECTIVES_FILE")
    @Basic
    var testReportsLevelOfDefectivesFile: String? = null

    @Column(name = "TESTS_AGAINST_THE_STANDARD_FILE")
    @Basic
    var testsAgainstTheStandardFile: String? = null

    @Column(name = "ILLUSTRATION_FILE")
    @Basic
    var illustrationFile: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as ApplicationQuestionnaireEntity
        return id == that.id &&
                qualityAuditChecksMoreInfo == that.qualityAuditChecksMoreInfo &&
                workOrEquivalentMoreInfo == that.workOrEquivalentMoreInfo &&
                againstOrForStockMoreInfo == that.againstOrForStockMoreInfo &&
                illustrationFile == that.illustrationFile &&
                testsAgainstTheStandardFile == that.testsAgainstTheStandardFile &&
                testReportsLevelOfDefectivesFile == that.testReportsLevelOfDefectivesFile &&
                qualityManualFile == that.qualityManualFile &&
                manufacturingStepsFile == that.manufacturingStepsFile &&
                dmarkForeignId == that.dmarkForeignId &&
                againstOrForStock == that.againstOrForStock &&
                workOrEquivalent == that.workOrEquivalent &&
                separateEntity == that.separateEntity &&
                doubtfulQuality == that.doubtfulQuality &&
                staffOrganization == that.staffOrganization &&
                reportingTo == that.reportingTo &&
                carryWordOrderIdentification == that.carryWordOrderIdentification &&
                awarenessOfTests == that.awarenessOfTests &&
                incomingMaterials == that.incomingMaterials &&
                inProcessApplications == that.inProcessApplications &&
                finalProducts == that.finalProducts &&
                monitoredByQualityControlStaff == that.monitoredByQualityControlStaff &&
                qualityAuditChecks == that.qualityAuditChecks &&
                infoOnqualityControlStaff == that.infoOnqualityControlStaff &&
                materialsPurchased == that.materialsPurchased &&
                qualityAssuranceMethod == that.qualityAssuranceMethod &&
                existingStorageFacilities == that.existingStorageFacilities &&
                manufacturingSteps == that.manufacturingSteps &&
                operatingMaintenanceSystem == that.operatingMaintenanceSystem &&
                qualityControlSystem == that.qualityControlSystem &&
                qualityManual == that.qualityManual &&
                testEquipment == that.testEquipment &&
                externalArrangementOrTesting == that.externalArrangementOrTesting &&
                levelOfDefectives == that.levelOfDefectives &&
                levelOfClaims == that.levelOfClaims &&
                independentTests == that.independentTests &&
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
                lastModifiedBy == that.lastModifiedBy &&
                lastModifiedOn == that.lastModifiedOn &&
                updateBy == that.updateBy &&
                updatedOn == that.updatedOn &&
                deleteBy == that.deleteBy &&
                deletedOn == that.deletedOn
    }

    override fun hashCode(): Int {
        return Objects.hash(id, testsAgainstTheStandardFile, againstOrForStockMoreInfo, illustrationFile, dmarkForeignId, testReportsLevelOfDefectivesFile, qualityManualFile, manufacturingStepsFile, againstOrForStock, workOrEquivalent, separateEntity, doubtfulQuality, staffOrganization, reportingTo, carryWordOrderIdentification, awarenessOfTests, incomingMaterials, inProcessApplications, finalProducts, monitoredByQualityControlStaff, qualityAuditChecks, infoOnqualityControlStaff, materialsPurchased, qualityAssuranceMethod, existingStorageFacilities, manufacturingSteps, operatingMaintenanceSystem, qualityControlSystem, qualityManual, testEquipment, externalArrangementOrTesting, levelOfDefectives, levelOfClaims, independentTests, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, lastModifiedBy, lastModifiedOn, updateBy, updatedOn, deleteBy, deletedOn)
    }

}