package org.kebs.app.kotlin.apollo.store.model.qa

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_QA_STA3")
class QaSta3Entity : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_QA_STA3_SEQ_GEN", allocationSize = 1, sequenceName = "DAT_KEBS_QA_STA3_SEQ")
    @GeneratedValue(generator = "DAT_KEBS_QA_STA3_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = null

    @Column(name = "PERMIT_ID")
    @Basic
    var permitId: Long? = null

    @Column(name = "PERMIT_REF_NUMBER")
    @Basic
    var permitRefNumber: String? = null

    @Column(name = "PRODUCE_ORDERS_OR_STOCK")
    @Basic
    var produceOrdersOrStock: String? = null

    @Column(name = "ISSUE_WORK_ORDER_OR_EQUIVALENT")
    @Basic
    var issueWorkOrderOrEquivalent: String? = null

    @Column(name = "IDENTIFY_BATCH_AS_SEPARATE")
    @Basic
    var identifyBatchAsSeparate: String? = null

    @Column(name = "PRODUCTS_CONTAINERS_CARRY_WORKS_ORDER")
    @Basic
    var productsContainersCarryWorksOrder: String? = null

    @Column(name = "ISOLATED_CASE_DOUBTFUL_QUALITY")
    @Basic
    var isolatedCaseDoubtfulQuality: String? = null

    @Column(name = "HEAD_QA_QUALIFICATIONS_TRAINING")
    @Basic
    var headQaQualificationsTraining: String? = null

    @Column(name = "REPORTING_TO")
    @Basic
    var reportingTo: String? = null

    @Column(name = "SEPARATE_QCID")
    @Basic
    var separateQcid: String? = null

    @Column(name = "TESTS_RELEVANT_STANDARD")
    @Basic
    var testsRelevantStandard: String? = null

    @Column(name = "SPO_COMING_MATERIALS")
    @Basic
    var spoComingMaterials: String? = null

    @Column(name = "SPO_PROCESS_OPERATIONS")
    @Basic
    var spoProcessOperations: String? = null

    @Column(name = "SPO_FINAL_PRODUCTS")
    @Basic
    var spoFinalProducts: String? = null

    @Column(name = "MONITORED_QCS")
    @Basic
    var monitoredQcs: String? = null

    @Column(name = "QAUDIT_CHECKS_CARRIED")
    @Basic
    var qauditChecksCarried: String? = null

    @Column(name = "INFORMATION_QCSO")
    @Basic
    var informationQcso: String? = null

    @Column(name = "MAIN_MATERIALS_PURCHASED_SPECIFICATION")
    @Basic
    var mainMaterialsPurchasedSpecification: String? = null

    @Column(name = "ADOPTED_RECEIPT_MATERIALS")
    @Basic
    var adoptedReceiptMaterials: String? = null

    @Column(name = "STORAGE_FACILITIES_EXIST")
    @Basic
    var storageFacilitiesExist: String? = null

    @Column(name = "STEPS_MANUFACTURE")
    @Basic
    var stepsManufacture: String? = null

    @Column(name = "MAINTENANCE_SYSTEM")
    @Basic
    var maintenanceSystem: String? = null

    @Column(name = "QCS_SUPPLEMENT")
    @Basic
    var qcsSupplement: String? = null

    @Column(name = "QM_INSTRUCTIONS")
    @Basic
    var qmInstructions: String? = null

    @Column(name = "TEST_EQUIPMENT_USED")
    @Basic
    var testEquipmentUsed: String? = null

    @Column(name = "INDICATE_EXTERNAL_ARRANGEMENT")
    @Basic
    var indicateExternalArrangement: String? = null

    @Column(name = "LEVEL_DEFECTIVES_FOUND")
    @Basic
    var levelDefectivesFound: String? = null

    @Column(name = "LEVEL_CLAIMS_COMPLAINTS")
    @Basic
    var levelClaimsComplaints: String? = null

    @Column(name = "INDEPENDENT_TESTS")
    @Basic
    var independentTests: String? = null

    @Column(name = "INDICATE_STAGE_MANUFACTURE")
    @Basic
    var indicateStageManufacture: String? = null

    @Column(name = "DESCRIPTION")
    @Basic
    var description: String? = null

    @Column(name = "STATUS")
    @Basic
    var status: Int? = null

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as QaSta3Entity
        return id == that.id &&
                permitId == that.permitId &&
                produceOrdersOrStock == that.produceOrdersOrStock &&
                issueWorkOrderOrEquivalent == that.issueWorkOrderOrEquivalent &&
                identifyBatchAsSeparate == that.identifyBatchAsSeparate &&
                productsContainersCarryWorksOrder == that.productsContainersCarryWorksOrder &&
                isolatedCaseDoubtfulQuality == that.isolatedCaseDoubtfulQuality &&
                headQaQualificationsTraining == that.headQaQualificationsTraining &&
                reportingTo == that.reportingTo &&
                separateQcid == that.separateQcid &&
                testsRelevantStandard == that.testsRelevantStandard &&
                spoComingMaterials == that.spoComingMaterials &&
                spoProcessOperations == that.spoProcessOperations &&
                spoFinalProducts == that.spoFinalProducts &&
                monitoredQcs == that.monitoredQcs &&
                qauditChecksCarried == that.qauditChecksCarried &&
                informationQcso == that.informationQcso &&
                mainMaterialsPurchasedSpecification == that.mainMaterialsPurchasedSpecification &&
                adoptedReceiptMaterials == that.adoptedReceiptMaterials &&
                storageFacilitiesExist == that.storageFacilitiesExist &&
                stepsManufacture == that.stepsManufacture &&
                maintenanceSystem == that.maintenanceSystem &&
                qcsSupplement == that.qcsSupplement &&
                qmInstructions == that.qmInstructions &&
                testEquipmentUsed == that.testEquipmentUsed &&
                indicateExternalArrangement == that.indicateExternalArrangement &&
                levelDefectivesFound == that.levelDefectivesFound &&
                levelClaimsComplaints == that.levelClaimsComplaints &&
                independentTests == that.independentTests &&
                indicateStageManufacture == that.indicateStageManufacture &&
                permitRefNumber == that.permitRefNumber &&
                description == that.description &&
                status == that.status &&
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
        return Objects.hash(
            id,
            permitId,
             permitRefNumber,
            produceOrdersOrStock,
            issueWorkOrderOrEquivalent,
            identifyBatchAsSeparate,
            productsContainersCarryWorksOrder,
            isolatedCaseDoubtfulQuality,
            headQaQualificationsTraining,
            reportingTo,
            separateQcid,
            testsRelevantStandard,
            spoComingMaterials,
            spoProcessOperations,
            spoFinalProducts,
            monitoredQcs,
            qauditChecksCarried,
            informationQcso,
            mainMaterialsPurchasedSpecification,
            adoptedReceiptMaterials,
            storageFacilitiesExist,
            stepsManufacture,
            maintenanceSystem,
            qcsSupplement,
            qmInstructions,
            testEquipmentUsed,
            indicateExternalArrangement,
            levelDefectivesFound,
            levelClaimsComplaints,
            independentTests,
            indicateStageManufacture,
            description,
            status,
            varField1,
            varField2,
            varField3,
            varField4,
            varField5,
            varField6,
            varField7,
            varField8,
            varField9,
            varField10,
            createdBy,
            createdOn,
            modifiedBy,
            modifiedOn,
            deleteBy,
            deletedOn
        )
    }
}