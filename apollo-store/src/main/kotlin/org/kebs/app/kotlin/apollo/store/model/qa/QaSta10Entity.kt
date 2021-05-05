package org.kebs.app.kotlin.apollo.store.model.qa

import java.io.Serializable
import java.sql.Date
import java.sql.Time
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_QA_STA10")
class QaSta10Entity : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_QA_STA10_SEQ_GEN", allocationSize = 1, sequenceName = "DAT_KEBS_QA_STA10_SEQ")
    @GeneratedValue(generator = "DAT_KEBS_QA_STA10_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = 0

    @Column(name = "PERMIT_ID")
    @Basic
    var permitId: Long? = null

    @Column(name = "FIRM_NAME")
    @Basic
    var firmName: String? = null

    @Column(name = "STATUS_COMPANY_BUSINESS_REGISTRATION")
    @Basic
    var statusCompanyBusinessRegistration: String? = null

    @Column(name = "OWNER_NAME_PROPRIETOR_DIRECTOR")
    @Basic
    var ownerNameProprietorDirector: String? = null

    @Column(name = "POSTAL_ADDRESS")
    @Basic
    var postalAddress: String? = null

    @Column(name = "TELEPHONE")
    @Basic
    var telephone: String? = null

    @Column(name = "CONTACT_PERSON")
    @Basic
    var contactPerson: String? = null

    @Column(name = "EMAIL_ADDRESS")
    @Basic
    var emailAddress: String? = null

    @Column(name = "PHYSICAL_LOCATION_MAP")
    @Basic
    var physicalLocationMap: String? = null

    @Column(name = "REGION")
    @Basic
    var region: Long? = null

    @Column(name = "COUNTY")
    @Basic
    var county: Long? = null

    @Column(name = "TOWN")
    @Basic
    var town: Long? = null

    @Column(name = "TOTAL_NUMBER_PERSONNEL")
    @Basic
    var totalNumberPersonnel: Long? = null

    @Column(name = "TOTAL_NUMBER_FEMALE")
    @Basic
    var totalNumberFemale: Long? = null

    @Column(name = "TOTAL_NUMBER_MALE")
    @Basic
    var totalNumberMale: Long? = null

    @Column(name = "TOTAL_NUMBER_PERMANENT_EMPLOYEES")
    @Basic
    var totalNumberPermanentEmployees: Long? = null

    @Column(name = "TOTAL_NUMBER_CASUAL_EMPLOYEES")
    @Basic
    var totalNumberCasualEmployees: Long? = null

    @Column(name = "AVERAGE_VOLUME_PRODUCTION_MONTH")
    @Basic
    var averageVolumeProductionMonth: Long? = null

    @Column(name = "HANDLED_MANUFACTURING_PROCESS_RAW_MATERIALS")
    @Basic
    var handledManufacturingProcessRawMaterials: String? = null

    @Column(name = "HANDLED_MANUFACTURING_PROCESS_INPROCESS_PRODUCTS")
    @Basic
    var handledManufacturingProcessInprocessProducts: String? = null

    @Column(name = "HANDLED_MANUFACTURING_PROCESS_FINAL_PRODUCT")
    @Basic
    var handledManufacturingProcessFinalProduct: String? = null

    @Column(name = "STRATEGY_INPLACE_RECALLING_PRODUCTS")
    @Basic
    var strategyInplaceRecallingProducts: String? = null

    @Column(name = "STATE_FACILITY_CONDITIONS_RAW_MATERIALS")
    @Basic
    var stateFacilityConditionsRawMaterials: String? = null

    @Column(name = "STATE_FACILITY_CONDITIONS_END_PRODUCT")
    @Basic
    var stateFacilityConditionsEndProduct: String? = null

    @Column(name = "TESTING_FACILITIES_EXIST_SPECIFY_EQUIPMENT")
    @Basic
    var testingFacilitiesExistSpecifyEquipment: String? = null

    @Column(name = "TESTING_FACILITIES_EXIST_STATE_PARAMETERS_TESTED")
    @Basic
    var testingFacilitiesExistStateParametersTested: String? = null

    @Column(name = "TESTING_FACILITIES_SPECIFY_PARAMETERS_TESTED")
    @Basic
    var testingFacilitiesSpecifyParametersTested: String? = null

    @Column(name = "CALIBRATION_EQUIPMENT_LAST_CALIBRATED")
    @Basic
    var calibrationEquipmentLastCalibrated: String? = null

    @Column(name = "HANDLING_CONSUMER_COMPLAINTS")
    @Basic
    var handlingConsumerComplaints: String? = null

    @Column(name = "COMPANY_REPRESENTATIVE")
    @Basic
    var companyRepresentative: String? = null

    @Column(name = "APPLICATION_DATE")
    @Basic
    var applicationDate: Date? = null

    @Column(name = "STATE_ADEQUACY_CONSTRUCTION_FACILITY")
    @Basic
    var stateAdequacyConstructionFacility: String? = null

    @Column(name = "STATE_ADEQUACY_PLANT_LAYOUT")
    @Basic
    var stateAdequacyPlantLayout: String? = null

    @Column(name = "STATE_ADEQUACY_SUITABILITY_LOCATION")
    @Basic
    var stateAdequacySuitabilityLocation: String? = null

    @Column(name = "STATE_ADEQUACY_SUITABILITY_EQUIPMENT")
    @Basic
    var stateAdequacySuitabilityEquipment: String? = null

    @Column(name = "HYGIENE_GENERAL_PLANT_DESCRIBE")
    @Basic
    var hygieneGeneralPlantDescribe: String? = null

    @Column(name = "STAFF_PROVIDED_NECESSARY_PROTECTIVE_CLOTHING_DESCRIBE")
    @Basic
    var staffProvidedNecessaryProtectiveClothingDescribe: String? = null

    @Column(name = "COMPLIED_RELEVANT_STATUTORY_REGULATORY_REQUIREMENTS_DESCRIBE")
    @Basic
    var compliedRelevantStatutoryRegulatoryRequirementsDescribe: String? = null

    @Column(name = "PRODUCT_LABELED_MARKED_SPECIFY")
    @Basic
    var productLabeledMarkedSpecify: String? = null

    @Column(name = "PRODUCT_LABELED_MARKED_SPECIFY_1A")
    @Basic
    var productLabeledMarkedSpecify1a: String? = null

    @Column(name = "PRODUCT_LABELED_MARKED_SPECIFY_1B")
    @Basic
    var productLabeledMarkedSpecify1b: String? = null

    @Column(name = "PRODUCT_LABELED_MARKED_SPECIFY_1C")
    @Basic
    var productLabeledMarkedSpecify1c: String? = null

    @Column(name = "PRODUCT_LABELED_MARKED_SPECIFY_1D")
    @Basic
    var productLabeledMarkedSpecify1d: String? = null

    @Column(name = "PRODUCT_LABELED_MARKED_SPECIFY_1E")
    @Basic
    var productLabeledMarkedSpecify1e: String? = null

    @Column(name = "LABELS_MARKS_COMPLY_REQUIREMENTS_RELEVANT_STANDARD")
    @Basic
    var labelsMarksComplyRequirementsRelevantStandard: String? = null

    @Column(name = "PROCESSES_PRODUCTS_IMPACT_NEGATIVELY_ENVIRONMENT")
    @Basic
    var processesProductsImpactNegativelyEnvironment: String? = null

    @Column(name = "SPECIFY_MITIGATION_MEASURES_UNDERTAKEN")
    @Basic
    var specifyMitigationMeasuresUndertaken: String? = null

    @Column(name = "RECOMMENDATIONS_CONCLUSIONS_AREAS_IMPROVEMENT")
    @Basic
    var recommendationsConclusionsAreasImprovement: String? = null

    @Column(name = "ASSESSORS_RECOMMENDATION_CERTIFICATION")
    @Basic
    var assessorsRecommendationCertification: String? = null

    @Column(name = "OFFICIAL_FILL_DATE")
    @Basic
    var officialFillDate: Date? = null

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
        val that = other as QaSta10Entity
        return id == that.id &&
                permitId == that.permitId &&
                firmName == that.firmName &&
                statusCompanyBusinessRegistration == that.statusCompanyBusinessRegistration &&
                ownerNameProprietorDirector == that.ownerNameProprietorDirector &&
                postalAddress == that.postalAddress &&
                telephone == that.telephone &&
                contactPerson == that.contactPerson &&
                emailAddress == that.emailAddress &&
                productLabeledMarkedSpecify1a == that.productLabeledMarkedSpecify1a &&
                productLabeledMarkedSpecify1b == that.productLabeledMarkedSpecify1b &&
                productLabeledMarkedSpecify1c == that.productLabeledMarkedSpecify1c &&
                productLabeledMarkedSpecify1d == that.productLabeledMarkedSpecify1d &&
                productLabeledMarkedSpecify1e == that.productLabeledMarkedSpecify1e &&
                physicalLocationMap == that.physicalLocationMap &&
                region == that.region &&
                county == that.county &&
                town == that.town &&
                totalNumberPersonnel == that.totalNumberPersonnel &&
                totalNumberFemale == that.totalNumberFemale &&
                totalNumberMale == that.totalNumberMale &&
                totalNumberPermanentEmployees == that.totalNumberPermanentEmployees &&
                totalNumberCasualEmployees == that.totalNumberCasualEmployees &&
                averageVolumeProductionMonth == that.averageVolumeProductionMonth &&
                handledManufacturingProcessRawMaterials == that.handledManufacturingProcessRawMaterials &&
                handledManufacturingProcessInprocessProducts == that.handledManufacturingProcessInprocessProducts &&
                handledManufacturingProcessFinalProduct == that.handledManufacturingProcessFinalProduct &&
                strategyInplaceRecallingProducts == that.strategyInplaceRecallingProducts &&
                stateFacilityConditionsRawMaterials == that.stateFacilityConditionsRawMaterials &&
                stateFacilityConditionsEndProduct == that.stateFacilityConditionsEndProduct &&
                testingFacilitiesExistSpecifyEquipment == that.testingFacilitiesExistSpecifyEquipment &&
                testingFacilitiesExistStateParametersTested == that.testingFacilitiesExistStateParametersTested &&
                testingFacilitiesSpecifyParametersTested == that.testingFacilitiesSpecifyParametersTested &&
                calibrationEquipmentLastCalibrated == that.calibrationEquipmentLastCalibrated &&
                handlingConsumerComplaints == that.handlingConsumerComplaints &&
                companyRepresentative == that.companyRepresentative &&
                applicationDate == that.applicationDate &&
                stateAdequacyConstructionFacility == that.stateAdequacyConstructionFacility &&
                stateAdequacyPlantLayout == that.stateAdequacyPlantLayout &&
                stateAdequacySuitabilityLocation == that.stateAdequacySuitabilityLocation &&
                stateAdequacySuitabilityEquipment == that.stateAdequacySuitabilityEquipment &&
                hygieneGeneralPlantDescribe == that.hygieneGeneralPlantDescribe &&
                staffProvidedNecessaryProtectiveClothingDescribe == that.staffProvidedNecessaryProtectiveClothingDescribe &&
                compliedRelevantStatutoryRegulatoryRequirementsDescribe == that.compliedRelevantStatutoryRegulatoryRequirementsDescribe &&
                productLabeledMarkedSpecify == that.productLabeledMarkedSpecify &&
                labelsMarksComplyRequirementsRelevantStandard == that.labelsMarksComplyRequirementsRelevantStandard &&
                processesProductsImpactNegativelyEnvironment == that.processesProductsImpactNegativelyEnvironment &&
                specifyMitigationMeasuresUndertaken == that.specifyMitigationMeasuresUndertaken &&
                recommendationsConclusionsAreasImprovement == that.recommendationsConclusionsAreasImprovement &&
                assessorsRecommendationCertification == that.assessorsRecommendationCertification &&
                officialFillDate == that.officialFillDate &&
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
            firmName,
            statusCompanyBusinessRegistration,
            ownerNameProprietorDirector,
            postalAddress,
            telephone,
            productLabeledMarkedSpecify1a,
            productLabeledMarkedSpecify1b,
            productLabeledMarkedSpecify1c,
            productLabeledMarkedSpecify1d,
            productLabeledMarkedSpecify1e,
            contactPerson,
            emailAddress,
            physicalLocationMap,
            region,
            county,
            town,
            totalNumberPersonnel,
            totalNumberFemale,
            totalNumberMale,
            totalNumberPermanentEmployees,
            totalNumberCasualEmployees,
            averageVolumeProductionMonth,
            handledManufacturingProcessRawMaterials,
            handledManufacturingProcessInprocessProducts,
            handledManufacturingProcessFinalProduct,
            strategyInplaceRecallingProducts,
            stateFacilityConditionsRawMaterials,
            stateFacilityConditionsEndProduct,
            testingFacilitiesExistSpecifyEquipment,
            testingFacilitiesExistStateParametersTested,
            testingFacilitiesSpecifyParametersTested,
            calibrationEquipmentLastCalibrated,
            handlingConsumerComplaints,
            companyRepresentative,
            applicationDate,
            stateAdequacyConstructionFacility,
            stateAdequacyPlantLayout,
            stateAdequacySuitabilityLocation,
            stateAdequacySuitabilityEquipment,
            hygieneGeneralPlantDescribe,
            staffProvidedNecessaryProtectiveClothingDescribe,
            compliedRelevantStatutoryRegulatoryRequirementsDescribe,
            productLabeledMarkedSpecify,
            labelsMarksComplyRequirementsRelevantStandard,
            processesProductsImpactNegativelyEnvironment,
            specifyMitigationMeasuresUndertaken,
            recommendationsConclusionsAreasImprovement,
            assessorsRecommendationCertification,
            officialFillDate,
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