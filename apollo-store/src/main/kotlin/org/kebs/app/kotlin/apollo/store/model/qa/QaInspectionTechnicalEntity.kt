package org.kebs.app.kotlin.apollo.store.model.qa

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_QA_INSPECTION_TECHNICAL")
class QaInspectionTechnicalEntity : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(
        name = "DAT_KEBS_QA_INSPECTION_TECHNICAL_SEQ_GEN",
        allocationSize = 1,
        sequenceName = "DAT_KEBS_QA_INSPECTION_TECHNICAL_SEQ"
    )
    @GeneratedValue(generator = "DAT_KEBS_QA_INSPECTION_TECHNICAL_SEQ_GEN", strategy = GenerationType.SEQUENCE)
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

    @Column(name = "QUALITY_PROCEDURE")
    @Basic
    var qualityProcedure: String? = null

    @Column(name = "QUALITY_PROCEDURE_REMARKS")
    @Basic
    var qualityProcedureRemarks: String? = null

    @Column(name = "HANDLING_COMPLAINTS")
    @Basic
    var handlingComplaints: String? = null

    @Column(name = "HANDLING_COMPLAINTS_REMARKS")
    @Basic
    var handlingComplaintsRemarks: String? = null


    @Column(name = "QUALITY_CONTROL_PERSONNEL")
    @Basic
    var qualityControlPersonnel: String? = null

    @Column(name = "QUALITY_CONTROL_PERSONNEL_REMARKS")
    @Basic
    var qualityControlPersonnelRemarks: String? = null

    @Column(name = "FIRM_IMPLEMENTED_ANY_MANAGEMENT_SYSTEM")
    @Basic
    var firmImplementedAnyManagementSystem: String? = null

    @Column(name = "FIRM_IMPLEMENTED_ANY_MANAGEMENT_SYSTEM_REMARKS")
    @Basic
    var firmImplementedAnyManagementSystemRemarks: String? = null

    @Column(name = "INDICATE_RELEVANT_PRODUCT_STANDARD_CODES")
    @Basic
    var indicateRelevantProductStandardCodes: String? = null

    @Column(name = "INDICATE_RELEVANT_PRODUCT_STANDARD_CODES_REMARKS")
    @Basic
    var indicateRelevantProductStandardCodesRemarks: String? = null

    @Column(name = "compliance_applicable_statutory")
    @Basic
    var complianceApplicableStatutory: String? = null

    @Column(name = "compliance_applicable_statutory_remarks")
    @Basic
    var complianceApplicableStatutoryRemarks: String? = null

    @Column(name = "AVAILABILITY_PRODUCT_STANDARDS_CODES_PRACTICE")
    @Basic
    var availabilityProductStandardsCodesPractice: String? = null

    @Column(name = "AVAILABILITY_PRODUCT_STANDARDS_CODES_PRACTICE_REMARKS")
    @Basic
    var availabilityProductStandardsCodesPracticeRemarks: String? = null

    @Column(name = "PLANT_HOUSE_KEEPING")
    @Basic
    var plantHouseKeeping: String? = null

    @Column(name = "PLANT_HOUSE_KEEPING_REMARKS")
    @Basic
    var plantHouseKeepingRemarks: String? = null

    @Column(name = "QUALITY_MANAGEMENT_SYSTEMS")
    @Basic
    var qualityManagementSystems: String? = null

    @Column(name = "QUALITY_MANAGEMENT_SYSTEMS_REMARKS")
    @Basic
    var qualityManagementSystemsRemarks: String? = null

    @Column(name = "HACCP_SEE_ANNEX_II")
    @Basic
    var haccpSeeAnnexIi: String? = null

    @Column(name = "HACCP_SEE_ANNEX_II_REMARKS")
    @Basic
    var haccpSeeAnnexIiRemarks: String? = null

    @Column(name = "TESTING_FACILITY")
    @Basic
    var testingFacility: String? = null

    @Column(name = "TESTING_FACILITY_REMARKS")
    @Basic
    var testingFacilityRemarks: String? = null

    @Column(name = "QUALITY_CONTROL_PERSONNEL_QUALIFICATIONS")
    @Basic
    var qualityControlPersonnelQualifications: String? = null

    @Column(name = "QUALITY_CONTROL_PERSONNEL_QUALIFICATIONS_REMARKS")
    @Basic
    var qualityControlPersonnelQualificationsRemarks: String? = null

    @Column(name = "EQUIPMENT_CALIBRATION")
    @Basic
    var equipmentCalibration: String? = null

    @Column(name = "EQUIPMENT_CALIBRATION_REMARKS")
    @Basic
    var equipmentCalibrationRemarks: String? = null

    @Column(name = "QUALITY_RECORDS")
    @Basic
    var qualityRecords: String? = null

    @Column(name = "QUALITY_RECORDS_REMARKS")
    @Basic
    var qualityRecordsRemarks: String? = null

    @Column(name = "PRODUCT_LABELING_IDENTIFICATION")
    @Basic
    var productLabelingIdentification: String? = null

    @Column(name = "PRODUCT_LABELING_IDENTIFICATION_REMARKS")
    @Basic
    var productLabelingIdentificationRemarks: String? = null

    @Column(name = "VALIDITY_SMARK_PERMIT")
    @Basic
    var validitySmarkPermit: String? = null

    @Column(name = "VALIDITY_SMARK_PERMIT_REMARKS")
    @Basic
    var validitySmarkPermitRemarks: String? = null

    @Column(name = "USE_THE_SMARK")
    @Basic
    var useTheSmark: String? = null

    @Column(name = "USE_THE_SMARK_REMARKS")
    @Basic
    var useTheSmarkRemarks: String? = null

    @Column(name = "CHANGES_AFFECTING_PRODUCT_CERTIFICATION")
    @Basic
    var changesAffectingProductCertification: String? = null

    @Column(name = "CHANGES_AFFECTING_PRODUCT_CERTIFICATION_REMARKS")
    @Basic
    var changesAffectingProductCertificationRemarks: String? = null

    @Column(name = "CHANGES_BEEN_COMMUNICATED_KEBS")
    @Basic
    var changesBeenCommunicatedKebs: String? = null

    @Column(name = "CHANGES_BEEN_COMMUNICATED_KEBS_REMARKS")
    @Basic
    var changesBeenCommunicatedKebsRemarks: String? = null

    @Column(name = "SAMPLES_DRAWN")
    @Basic
    var samplesDrawn: String? = null

    @Column(name = "SAMPLES_DRAWN_REMARKS")
    @Basic
    var samplesDrawnRemarks: String? = null

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
