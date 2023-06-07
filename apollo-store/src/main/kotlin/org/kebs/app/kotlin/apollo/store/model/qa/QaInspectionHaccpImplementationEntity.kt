package org.kebs.app.kotlin.apollo.store.model.qa

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_QA_INSPECTION_HACCP_IMPLEMENTATION")
class QaInspectionHaccpImplementationEntity : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(
        name = "DAT_KEBS_QA_INSPECTION_HACCP_IMPLEMENTATION_SEQ_GEN",
        allocationSize = 1,
        sequenceName = "DAT_KEBS_QA_INSPECTION_HACCP_IMPLEMENTATION_SEQ"
    )
    @GeneratedValue(
        generator = "DAT_KEBS_QA_INSPECTION_HACCP_IMPLEMENTATION_SEQ_GEN",
        strategy = GenerationType.SEQUENCE
    )
    @Id
    var id: Long? = null

    @Column(name = "PERMIT_ID")
    @Basic
    var permitId: Long? = null

    @Column(name = "INSPECTION_RECOMMENDATION_ID")
    @Basic
    var inspectionRecommendationId: Long? = null

    @Column(name = "PERMIT_REF_NUMBER")
    @Basic
    var permitRefNumber: String? = null

    @Column(name = "DESIGN_FACILITIES_CONSTRUCTION_LAYOUT")
    @Basic
    var designFacilitiesConstructionLayout: String? = null

    @Column(name = "DESIGN_FACILITIES_CONSTRUCTION_LAYOUT_REMARKS")
    @Basic
    var designFacilitiesConstructionLayoutRemarks: String? = null

    @Column(name = "DETERMINATION_CRITICAL_PARAMETERS")
    @Basic
    var determinationCriticalParameters: String? = null

    @Column(name = "DETERMINATION_CRITICAL_PARAMETERS_REMARKS")
    @Basic
    var determinationCriticalParametersRemarks: String? = null

    @Column(name = "CONTROL_OPERATIONS")
    @Basic
    var controlOperations: String? = null

    @Column(name = "CONTROL_OPERATIONS_REMARKS")
    @Basic
    var controlOperationsRemarks: String? = null

    @Column(name = "MAINTENANCE_SANITATION_CLEANING_PROGRAMS")
    @Basic
    var maintenanceSanitationCleaningPrograms: String? = null

    @Column(name = "MAINTENANCE_SANITATION_CLEANING_PROGRAMS_REMARKS")
    @Basic
    var maintenanceSanitationCleaningProgramsRemarks: String? = null

    @Column(name = "PERSONNEL_HYGIENE")
    @Basic
    var personnelHygiene: String? = null

    @Column(name = "PERSONNEL_HYGIENE_REMARKS")
    @Basic
    var personnelHygieneRemarks: String? = null

    @Column(name = "TRANSPORTATION_CONVEYANCE")
    @Basic
    var transportationConveyance: String? = null

    @Column(name = "TRANSPORTATION_CONVEYANCE_REMARKS")
    @Basic
    var transportationConveyanceRemarks: String? = null

    @Column(name = "PRODUCT_INFORMATION_LABELLING")
    @Basic
    var productInformationLabelling: String? = null

    @Column(name = "PRODUCT_INFORMATION_LABELLING_REMARKS")
    @Basic
    var productInformationLabellingRemarks: String? = null

    @Column(name = "TRAINING_MANAGEMENT")
    @Basic
    var trainingManagement: String? = null

    @Column(name = "TRAINING_MANAGEMENT_REMARKS")
    @Basic
    var trainingManagementRemarks: String? = null

    @Column(name = "APPROPRIATE_SECTOR_HYGIENE_PRACTICE")
    @Basic
    var appropriateSectorHygienePractice: String? = null

    @Column(name = "APPROPRIATE_SECTOR_HYGIENE_PRACTICE_REMARKS")
    @Basic
    var appropriateSectorHygienePracticeRemarks: String? = null

    @Column(name = "ESTABLISHMENT_HACCP_PLAN")
    @Basic
    var establishmentHaccpPlan: String? = null

    @Column(name = "ESTABLISHMENT_HACCP_PLAN_REMARKS")
    @Basic
    var establishmentHaccpPlanRemarks: String? = null

    @Column(name = "PRODUCT_FLOW_DIAGRAM")
    @Basic
    var productFlowDiagram: String? = null

    @Column(name = "PRODUCT_FLOW_DIAGRAM_REMARKS")
    @Basic
    var productFlowDiagramRemarks: String? = null

    @Column(name = "EVIDENCE_HAZARD_ANALYSIS")
    @Basic
    var evidenceHazardAnalysis: String? = null

    @Column(name = "EVIDENCE_HAZARD_ANALYSIS_REMARKS")
    @Basic
    var evidenceHazardAnalysisRemarks: String? = null

    @Column(name = "ESTABLISHMENT_CRITICAL_CONTROL_POINTS")
    @Basic
    var establishmentCriticalControlPoints: String? = null

    @Column(name = "ESTABLISHMENT_CRITICAL_CONTROL_POINTS_REMARKS")
    @Basic
    var establishmentCriticalControlPointsRemarks: String? = null

    @Column(name = "ESTABLISHMENT_MONITORING_CONTROL")
    @Basic
    var establishmentMonitoringControl: String? = null

    @Column(name = "ESTABLISHMENT_MONITORING_CONTROL_REMARKS")
    @Basic
    var establishmentMonitoringControlRemarks: String? = null

    @Column(name = "EVIDENCE_CORRECTIVE_ACTIONS")
    @Basic
    var evidenceCorrectiveActions: String? = null

    @Column(name = "EVIDENCE_CORRECTIVE_ACTIONS_REMARKS")
    @Basic
    var evidenceCorrectiveActionsRemarks: String? = null

    @Column(name = "EVIDENCE_VERIFICATION_CONFIRM_HACCP")
    @Basic
    var evidenceVerificationConfirmHaccp: String? = null

    @Column(name = "EVIDENCE_VERIFICATION_CONFIRM_HACCP_REMARKS")
    @Basic
    var evidenceVerificationConfirmHaccpRemarks: String? = null

    @Column(name = "RECORD_KEEPING_DOCUMENTS_APPROPRIATE")
    @Basic
    var recordKeepingDocumentsAppropriate: String? = null

    @Column(name = "RECORD_KEEPING_DOCUMENTS_APPROPRIATE_REMARKS")
    @Basic
    var recordKeepingDocumentsAppropriateRemarks: String? = null

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

}
