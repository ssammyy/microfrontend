package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_FACTORY_INSPECTION_REPORT")
class FactoryInspectionReportEntity: Serializable {
    @Column(name = "ID")
    @Id
    var id: Long = 0

    @Column(name = "STATUS")
    @Basic
    var status: Long? = null

    @Column(name = "HOF_APPROVAL")
    @Basic
    var hofApproval: Int? = null

    @Column(name = "QUALITY_PROCEDURE_FINDINGS")
    @Basic
    var qualityProcedureFindings: String? = null

    @Column(name = "QUALITY_PROCEDURE_REMARKS")
    @Basic
    var qualityProcedureRemarks: String? = null

    @Column(name = "AVAILABILITY_OF_PRODUCT_STANDARDS_FINDINGS")
    @Basic
    var availabilityOfProductStandardsFindings: String? = null

    @Column(name = "AVAILABILITY_OF_PRODUCT_STANDARDS_REMARKS")
    @Basic
    var availabilityOfProductStandardsRemarks: String? = null

    @Column(name = "QUALITY_MANAGEMENT_SYSTEMS_FINDINGS")
    @Basic
    var qualityManagementSystemsFindings: String? = null

    @Column(name = "QUALITY_MANAGEMENT_SYSTEMS_REMARKS")
    @Basic
    var qualityManagementSystemsRemarks: String? = null

    @Column(name = "HACCP_FINDINGS")
    @Basic
    var haccpFindings: String? = null

    @Column(name = "HACCP_REMARKS")
    @Basic
    var haccpRemarks: String? = null

    @Column(name = "TESTING_FACILITY_FINDINGS")
    @Basic
    var testingFacilityFindings: String? = null

    @Column(name = "TESTING_FACILITY_REMARKS")
    @Basic
    var testingFacilityRemarks: String? = null

    @Column(name = "QUALITY_CONTROL_PERSONNEL_QUALIFICATIONS_FINDINGS")
    @Basic
    var qualityControlPersonnelQualificationsFindings: String? = null

    @Column(name = "QUALITY_CONTROL_PERSONNEL_QUALIFICATIONS_REMARKS")
    @Basic
    var qualityControlPersonnelQualificationsRemarks: String? = null

    @Column(name = "EQUIPMENT_CALIBRATION_FINDINGS")
    @Basic
    var equipmentCalibrationFindings: String? = null

    @Column(name = "EQUIPMENT_CALIBRATION_REMARKS")
    @Basic
    var equipmentCalibrationRemarks: String? = null

    @Column(name = "QUALITY_RECORDS_FINDINGS")
    @Basic
    var qualityRecordsFindings: String? = null

    @Column(name = "QUALITY_RECORDS_REMARKS")
    @Basic
    var qualityRecordsRemarks: String? = null

    @Column(name = "PRODUCT_LABELING_AND_IDENTIFICATION_FINDINGS")
    @Basic
    var productLabelingAndIdentificationFindings: String? = null

    @Column(name = "PRODUCT_LABELING_AND_IDENTIFICATION_REMARKS")
    @Basic
    var productLabelingAndIdentificationRemarks: String? = null

    @Column(name = "VALIDITY_OF_SMARK_PERMIT_FINDINGS")
    @Basic
    var validityOfSmarkPermitFindings: String? = null

    @Column(name = "VALIDITY_OF_SMARK_PERMIT_REMARKS")
    @Basic
    var validityOfSmarkPermitRemarks: String? = null

    @Column(name = "USE_OF_THE_SMARK_FINDINGS")
    @Basic
    var useOfTheSmarkFindings: String? = null

    @Column(name = "USE_OF_THE_SMARK_REMARKS")
    @Basic
    var useOfTheSmarkRemarks: String? = null

    @Column(name = "CHANGES_AFFECTING_PRODUCT_CERTIFICATION_FINDINGS")
    @Basic
    var changesAffectingProductCertificationFindings: String? = null

    @Column(name = "CHANGES_AFFECTING_PRODUCT_CERTIFICATION_REMARKS")
    @Basic
    var changesAffectingProductCertificationRemarks: String? = null

    @Column(name = "COMMUNICATION_OF_THE_CHANGES_TO_KEBS_FINDINGS")
    @Basic
    var communicationOfTheChangesToKebsFindings: String? = null

    @Column(name = "COMMUNICATION_OF_THE_CHANGES_TO_KEBS_REMARKS")
    @Basic
    var communicationOfTheChangesToKebsRemarks: String? = null

    @Column(name = "SAMPLES_DRAWN_FINDINGS")
    @Basic
    var samplesDrawnFindings: String? = null

    @Column(name = "SAMPLES_DRAWN_REMARKS")
    @Basic
    var samplesDrawnRemarks: String? = null

    @Column(name = "DESIGN_AND_FACILITATES_CONSTRUCTION_AND_LAYOUT_OF_BUILDINGS_FINDINGS")
    @Basic
    var designAndFacilitatesConstructionAndLayoutOfBuildingsFindings: String? = null

    @Column(name = "DESIGN_AND_FACILITATES_CONSTRUCTION_AND_LAYOUT_OF_BUILDINGS_REMARKS")
    @Basic
    var designAndFacilitatesConstructionAndLayoutOfBuildingsRemarks: String? = null

    @Column(name = "CONTROL_OF_OPERATIONS_FINDINGS")
    @Basic
    var controlOfOperationsFindings: String? = null

    @Column(name = "CONTROL_OF_OPERATIONS_REMARKS")
    @Basic
    var controlOfOperationsRemarks: String? = null

    @Column(name = "MAINTENANCE_AND_SANITATION_CLEANING_PROGRAMS_FINDINGS")
    @Basic
    var maintenanceAndSanitationCleaningProgramsFindings: String? = null

    @Column(name = "MAINTENANCE_AND_SANITATION_CLEANING_PROGRAMS_REMARKS")
    @Basic
    var maintenanceAndSanitationCleaningProgramsRemarks: String? = null

    @Column(name = "PERSONAL_HYGENE_FINDINGS")
    @Basic
    var personalHygeneFindings: String? = null

    @Column(name = "PERSONAL_HYGENE_REMARKS")
    @Basic
    var personalHygeneRemarks: String? = null

    @Column(name = "TRANSPORTATION_CONVEYANCE_AND_BULK_CONTAINER_FINDINGS")
    @Basic
    var transportationConveyanceAndBulkContainerFindings: String? = null

    @Column(name = "TRANSPORTATION_CONVEYANCE_AND_BULK_CONTAINER_REMARKS")
    @Basic
    var transportationConveyanceAndBulkContainerRemarks: String? = null

    @Column(name = "PRODUCT_INFORMATION_LABELING_FINDINGS")
    @Basic
    var productInformationLabelingFindings: String? = null

    @Column(name = "PRODUCT_INFORMATION_LABELING_REMARKS")
    @Basic
    var productInformationLabelingRemarks: String? = null

    @Column(name = "TRAINING_OF_MANAGEMENT_AND_EMPLOYEES_FINDINGS")
    @Basic
    var trainingOfManagementAndEmployeesFindings: String? = null

    @Column(name = "TRAINING_OF_MANAGEMENT_AND_EMPLOYEES_REMARKS")
    @Basic
    var trainingOfManagementAndEmployeesRemarks: String? = null

    @Column(name = "USE_OF_APPROPRIATE_SECTOR_CODES_OF_HYGENE_PRACTICE_FINDINGS")
    @Basic
    var useOfAppropriateSectorCodesOfHygenePracticeFindings: String? = null

    @Column(name = "USE_OF_APPROPRIATE_SECTOR_CODES_OF_HYGENE_PRACTICE_REMARKS")
    @Basic
    var useOfAppropriateSectorCodesOfHygenePracticeRemarks: String? = null

    @Column(name = "ESTABLISHMENT_OF_HACCP_PLANS_FINDINGS")
    @Basic
    var establishmentOfHaccpPlansFindings: String? = null

    @Column(name = "ESTABLISHMENT_OF_HACCP_PLANS_REMARKS")
    @Basic
    var establishmentOfHaccpPlansRemarks: String? = null

    @Column(name = "PRODUCT_FLOW_DIAGRAM_FINDINGS")
    @Basic
    var productFlowDiagramFindings: String? = null

    @Column(name = "PRODUCT_FLOW_DIAGRAM_REMARKS")
    @Basic
    var productFlowDiagramRemarks: String? = null

    @Column(name = "EVIDENCE_OF_HAZARD_ANALYSIS_DONE_AT_EACH_STEP_OF_PRODUCTION_FINDINGS")
    @Basic
    var evidenceOfHazardAnalysisDoneAtEachStepOfProductionFindings: String? = null

    @Column(name = "EVIDENCE_OF_HAZARD_ANALYSIS_DONE_AT_EACH_STEP_OF_PRODUCTION_REMARKS")
    @Basic
    var evidenceOfHazardAnalysisDoneAtEachStepOfProductionRemarks: String? = null

    @Column(name = "ESTABLISHMENT_OF_CRITICAL_CONTROL_POINTS_TO_ADDRESS_HAZARDS_FINDINGS")
    @Basic
    var establishmentOfCriticalControlPointsToAddressHazardsFindings: String? = null

    @Column(name = "ESTABLISHMENT_OF_CRITICAL_CONTROL_POINTS_TO_ADDRESS_HAZARDS_REMARKS")
    @Basic
    var establishmentOfCriticalControlPointsToAddressHazardsRemarks: String? = null

    @Column(name = "ESTABLISHMENT_OF_MONITORING_AND_CONTROL_OF_THE_CCP_FINDINGS")
    @Basic
    var establishmentOfMonitoringAndControlOfTheCcpFindings: String? = null

    @Column(name = "ESTABLISHMENT_OF_MONITORING_AND_CONTROL_OF_THE_CCP_REMARKS")
    @Basic
    var establishmentOfMonitoringAndControlOfTheCcpRemarks: String? = null

    @Column(name = "EVIDENCE_OF_CORRECTIVE_ACTIONS_FOR_EACH_CCP_FINDINGS")
    @Basic
    var evidenceOfCorrectiveActionsForEachCcpFindings: String? = null

    @Column(name = "EVIDENCE_OF_CORRECTIVE_ACTIONS_FOR_EACH_CCP_REMARKS")
    @Basic
    var evidenceOfCorrectiveActionsForEachCcpRemarks: String? = null

    @Column(name = "EVIDENCE_FOR_VERIFICATION_TO_CONFIRM_HACCP_SYSTEM_IS_WORKING_CORRECTLY_FINDINGS")
    @Basic
    var evidenceForVerificationToConfirmHaccpSystemIsWorkingCorrectlyFindings: String? = null

    @Column(name = "EVIDENCE_FOR_VERIFICATION_TO_CONFIRM_HACCP_SYSTEM_IS_WORKING_CORRECTLY_REMARKS")
    @Basic
    var evidenceForVerificationToConfirmHaccpSystemIsWorkingCorrectlyRemarks: String? = null

    @Column(name = "RECORD_KEEPING_OF_DOCUMENTS_APPROPRIATE_TO_THE_PRINCIPLE_FINDINGS")
    @Basic
    var recordKeepingOfDocumentsAppropriateToThePrincipleFindings: String? = null

    @Column(name = "RECORD_KEEPING_OF_DOCUMENTS_APPROPRIATE_TO_THE_PRINCIPLE_REMARKS")
    @Basic
    var recordKeepingOfDocumentsAppropriateToThePrincipleRemarks: String? = null

    @Column(name = "RECOMMENDATIONS")
    @Basic
    var recommendations: String? = null

    @Column(name = "INSPECTOR_COMMENTS")
    @Basic
    var inspectorComments: String? = null

    @Column(name = "SUPERVISOR_COMMENTS")
    @Basic
    var supervisorComments: String? = null

    @JoinColumn(name = "PERMIT_APPLICATION_ID", referencedColumnName = "ID")
    @ManyToOne
    var permitApplicationId: PermitApplicationEntity? = null

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
        val that = other as FactoryInspectionReportEntity
        return id == that.id &&
                hofApproval == that.hofApproval &&
                status == that.status &&
                qualityProcedureFindings == that.qualityProcedureFindings &&
                qualityProcedureRemarks == that.qualityProcedureRemarks &&
                availabilityOfProductStandardsFindings == that.availabilityOfProductStandardsFindings &&
                availabilityOfProductStandardsRemarks == that.availabilityOfProductStandardsRemarks &&
                qualityManagementSystemsFindings == that.qualityManagementSystemsFindings &&
                qualityManagementSystemsRemarks == that.qualityManagementSystemsRemarks &&
                haccpFindings == that.haccpFindings &&
                haccpRemarks == that.haccpRemarks &&
                testingFacilityFindings == that.testingFacilityFindings &&
                testingFacilityRemarks == that.testingFacilityRemarks &&
                qualityControlPersonnelQualificationsFindings == that.qualityControlPersonnelQualificationsFindings &&
                qualityControlPersonnelQualificationsRemarks == that.qualityControlPersonnelQualificationsRemarks &&
                equipmentCalibrationFindings == that.equipmentCalibrationFindings &&
                equipmentCalibrationRemarks == that.equipmentCalibrationRemarks &&
                qualityRecordsFindings == that.qualityRecordsFindings &&
                qualityRecordsRemarks == that.qualityRecordsRemarks &&
                productLabelingAndIdentificationFindings == that.productLabelingAndIdentificationFindings &&
                productLabelingAndIdentificationRemarks == that.productLabelingAndIdentificationRemarks &&
                validityOfSmarkPermitFindings == that.validityOfSmarkPermitFindings &&
                validityOfSmarkPermitRemarks == that.validityOfSmarkPermitRemarks &&
                useOfTheSmarkFindings == that.useOfTheSmarkFindings &&
                useOfTheSmarkRemarks == that.useOfTheSmarkRemarks &&
                changesAffectingProductCertificationFindings == that.changesAffectingProductCertificationFindings &&
                changesAffectingProductCertificationRemarks == that.changesAffectingProductCertificationRemarks &&
                communicationOfTheChangesToKebsFindings == that.communicationOfTheChangesToKebsFindings &&
                communicationOfTheChangesToKebsRemarks == that.communicationOfTheChangesToKebsRemarks &&
                samplesDrawnFindings == that.samplesDrawnFindings &&
                samplesDrawnRemarks == that.samplesDrawnRemarks &&
                designAndFacilitatesConstructionAndLayoutOfBuildingsFindings == that.designAndFacilitatesConstructionAndLayoutOfBuildingsFindings &&
                designAndFacilitatesConstructionAndLayoutOfBuildingsRemarks == that.designAndFacilitatesConstructionAndLayoutOfBuildingsRemarks &&
                controlOfOperationsFindings == that.controlOfOperationsFindings &&
                controlOfOperationsRemarks == that.controlOfOperationsRemarks &&
                maintenanceAndSanitationCleaningProgramsFindings == that.maintenanceAndSanitationCleaningProgramsFindings &&
                maintenanceAndSanitationCleaningProgramsRemarks == that.maintenanceAndSanitationCleaningProgramsRemarks &&
                personalHygeneFindings == that.personalHygeneFindings &&
                personalHygeneRemarks == that.personalHygeneRemarks &&
                transportationConveyanceAndBulkContainerFindings == that.transportationConveyanceAndBulkContainerFindings &&
                transportationConveyanceAndBulkContainerRemarks == that.transportationConveyanceAndBulkContainerRemarks &&
                productInformationLabelingFindings == that.productInformationLabelingFindings &&
                productInformationLabelingRemarks == that.productInformationLabelingRemarks &&
                trainingOfManagementAndEmployeesFindings == that.trainingOfManagementAndEmployeesFindings &&
                trainingOfManagementAndEmployeesRemarks == that.trainingOfManagementAndEmployeesRemarks &&
                useOfAppropriateSectorCodesOfHygenePracticeFindings == that.useOfAppropriateSectorCodesOfHygenePracticeFindings &&
                useOfAppropriateSectorCodesOfHygenePracticeRemarks == that.useOfAppropriateSectorCodesOfHygenePracticeRemarks &&
                establishmentOfHaccpPlansFindings == that.establishmentOfHaccpPlansFindings &&
                establishmentOfHaccpPlansRemarks == that.establishmentOfHaccpPlansRemarks &&
                productFlowDiagramFindings == that.productFlowDiagramFindings &&
                productFlowDiagramRemarks == that.productFlowDiagramRemarks &&
                evidenceOfHazardAnalysisDoneAtEachStepOfProductionFindings == that.evidenceOfHazardAnalysisDoneAtEachStepOfProductionFindings &&
                evidenceOfHazardAnalysisDoneAtEachStepOfProductionRemarks == that.evidenceOfHazardAnalysisDoneAtEachStepOfProductionRemarks &&
                establishmentOfCriticalControlPointsToAddressHazardsFindings == that.establishmentOfCriticalControlPointsToAddressHazardsFindings &&
                establishmentOfCriticalControlPointsToAddressHazardsRemarks == that.establishmentOfCriticalControlPointsToAddressHazardsRemarks &&
                establishmentOfMonitoringAndControlOfTheCcpFindings == that.establishmentOfMonitoringAndControlOfTheCcpFindings &&
                establishmentOfMonitoringAndControlOfTheCcpRemarks == that.establishmentOfMonitoringAndControlOfTheCcpRemarks &&
                evidenceOfCorrectiveActionsForEachCcpFindings == that.evidenceOfCorrectiveActionsForEachCcpFindings &&
                evidenceOfCorrectiveActionsForEachCcpRemarks == that.evidenceOfCorrectiveActionsForEachCcpRemarks &&
                evidenceForVerificationToConfirmHaccpSystemIsWorkingCorrectlyFindings == that.evidenceForVerificationToConfirmHaccpSystemIsWorkingCorrectlyFindings &&
                evidenceForVerificationToConfirmHaccpSystemIsWorkingCorrectlyRemarks == that.evidenceForVerificationToConfirmHaccpSystemIsWorkingCorrectlyRemarks &&
                recordKeepingOfDocumentsAppropriateToThePrincipleFindings == that.recordKeepingOfDocumentsAppropriateToThePrincipleFindings &&
                recordKeepingOfDocumentsAppropriateToThePrincipleRemarks == that.recordKeepingOfDocumentsAppropriateToThePrincipleRemarks &&
                recommendations == that.recommendations &&
                inspectorComments == that.inspectorComments &&
                supervisorComments == that.supervisorComments &&
                permitApplicationId == that.permitApplicationId &&
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
        return Objects.hash(id, status, qualityProcedureFindings, hofApproval, qualityProcedureRemarks, availabilityOfProductStandardsFindings, availabilityOfProductStandardsRemarks, qualityManagementSystemsFindings, qualityManagementSystemsRemarks, haccpFindings, haccpRemarks, testingFacilityFindings, testingFacilityRemarks, qualityControlPersonnelQualificationsFindings, qualityControlPersonnelQualificationsRemarks, equipmentCalibrationFindings, equipmentCalibrationRemarks, qualityRecordsFindings, qualityRecordsRemarks, productLabelingAndIdentificationFindings, productLabelingAndIdentificationRemarks, validityOfSmarkPermitFindings, validityOfSmarkPermitRemarks, useOfTheSmarkFindings, useOfTheSmarkRemarks, changesAffectingProductCertificationFindings, changesAffectingProductCertificationRemarks, communicationOfTheChangesToKebsFindings, communicationOfTheChangesToKebsRemarks, samplesDrawnFindings, samplesDrawnRemarks, designAndFacilitatesConstructionAndLayoutOfBuildingsFindings, designAndFacilitatesConstructionAndLayoutOfBuildingsRemarks, controlOfOperationsFindings, controlOfOperationsRemarks, maintenanceAndSanitationCleaningProgramsFindings, maintenanceAndSanitationCleaningProgramsRemarks, personalHygeneFindings, personalHygeneRemarks, transportationConveyanceAndBulkContainerFindings, transportationConveyanceAndBulkContainerRemarks, productInformationLabelingFindings, productInformationLabelingRemarks, trainingOfManagementAndEmployeesFindings, trainingOfManagementAndEmployeesRemarks, useOfAppropriateSectorCodesOfHygenePracticeFindings, useOfAppropriateSectorCodesOfHygenePracticeRemarks, establishmentOfHaccpPlansFindings, establishmentOfHaccpPlansRemarks, productFlowDiagramFindings, productFlowDiagramRemarks, evidenceOfHazardAnalysisDoneAtEachStepOfProductionFindings, evidenceOfHazardAnalysisDoneAtEachStepOfProductionRemarks, establishmentOfCriticalControlPointsToAddressHazardsFindings, establishmentOfCriticalControlPointsToAddressHazardsRemarks, establishmentOfMonitoringAndControlOfTheCcpFindings, establishmentOfMonitoringAndControlOfTheCcpRemarks, evidenceOfCorrectiveActionsForEachCcpFindings, evidenceOfCorrectiveActionsForEachCcpRemarks, evidenceForVerificationToConfirmHaccpSystemIsWorkingCorrectlyFindings, evidenceForVerificationToConfirmHaccpSystemIsWorkingCorrectlyRemarks, recordKeepingOfDocumentsAppropriateToThePrincipleFindings, recordKeepingOfDocumentsAppropriateToThePrincipleRemarks, recommendations, inspectorComments, supervisorComments, permitApplicationId, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn)
    }
}