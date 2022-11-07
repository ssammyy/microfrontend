package org.kebs.app.kotlin.apollo.common.dto.ms

import org.kebs.app.kotlin.apollo.common.dto.PredefinedResourcesRequiredEntityDto
import java.math.BigDecimal
import java.sql.Date
import java.sql.Timestamp
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class BatchFileFuelSaveDto(
        var county: Long? = null,
        var town: Long? = null,
        var remarks: String? = null,
)

data class TeamsFuelSaveDto(
        var teamName: String? = null,
        var assignedOfficerID: Long? = null,
        var startDate: Date? = null,
        var endDate: Date? = null,
        var remarks: String? = null,
        var countList : List<TeamsCountyFuelSaveDto>? = null,
)

data class TeamsCountyFuelSaveDto(
        var countyId: Long? = null,
        var remarks: String? = null,
)

data class WorkPlanBatchDetailsDto(
        var id: Long?= 0,
        var workPlanRegion: Long? = null,
        var createdDate: Date? = null,
        var createdStatus: Boolean? = null,
        var endedDate: Date? = null,
        var endedStatus: Boolean? = null,
        var workPlanStatus: Boolean? = null,
        var referenceNumber: String? = null,
        var userCreated: String? = null,
        var yearName: String? = null,
        var batchClosed: Boolean? = null,
)

data class FuelBatchDetailsDto(
        var id: Long?= 0,
        var region: String? = null,
        var county: String? = null,
        var town: String? = null,
        var referenceNumber: String? = null,
        var batchFileYear: String? = null,
        var batchFileMonth: String? = null,
        var remarks: String? = null,
        var batchClosed: Boolean? = null,
)
data class TeamsFuelDetailsDto(
        var id: Long?= 0,
        var referenceNumber: String? = null,
        var teamName: String? = null,
        var startDate: Date? = null,
        var endDate: Date? = null,
        var countyName : String? = null,
        var countyID : Long? = null,
)

data class WorkPlanScheduleListDetailsDto(
        var workPlanList: List<WorkPlanInspectionDto>? = null,
        var createdWorkPlan: WorkPlanBatchDetailsDto? = null
)

data class FuelInspectionScheduleListDetailsDto(
        var fuelInspectionDto: List<FuelInspectionDto>? = null,
        var fuelBatchDetailsDto: FuelBatchDetailsDto? = null,
        var fuelTeamsDto: TeamsFuelDetailsDto? = null
)

data class FuelScheduleTeamsListDetailsDto(
        var fuelTeamsDto: List<FuelTeamsDto>? = null,
        var fuelBatchDetailsDto: FuelBatchDetailsDto? = null,
        var officersList: List<MsUsersDto>? = null,
)

data class FuelScheduleCountyListDetailsDto(
        var fuelCountyDtoList: List<FuelCountyDto>? = null,
        var fuelTeamsDto: TeamsFuelDetailsDto? = null,
        var fuelBatchDetailsDto: FuelBatchDetailsDto? = null
)

data class FuelTeamsDto(
        var id: Long?= 0,
        var referenceNumber: String? = null,
        var teamName: String? = null,
        var startDate: Date? = null,
        var endDate: Date? = null,
        var officerAssignedName: String? = null
)

data class FuelCountyDto(
        var id: Long?= 0,
        var referenceNumber: String? = null,
        var countyName: String? = null,
)



data class FuelInspectionDto(
        var id: Long?= 0,
        var timelineStartDate: Date? = null,
        var timelineEndDate: Date? = null,
        var timelineOverDue: Boolean? = null,
        var referenceNumber: String? = null,
        var company: String? = null,
        var townName: String? = null,
        var companyKraPin: String? = null,
        var petroleumProduct: String? = null,
        var physicalLocation: String? = null,
        var inspectionDateFrom: Date? = null,
        var inspectionDateTo: Date? = null,
        var processStage: String? = null,
        var assignedOfficerStatus: Boolean? = null,
        var endInspectionStatus: Boolean? = null,
        var rapidTestDone: Boolean? = null,
        var sampleCollectionStatus: Boolean? = null,
        var scfUploadId: Long? = null,
        var sampleSubmittedStatus: Boolean? = null,
        var bsNumberStatus: Boolean? = null,
        var fuelCompliantStatus: Boolean? = null,
        var fuelReportId: Long? = null,
        var invoiceDocFile: Long? = null,
        var compliantStatusAdded: Boolean? = null,
        var remediationScheduledStatus: Boolean? = null,
        var remendiationCompleteStatus: Boolean? = null,
        var proFormaInvoiceStatus: Boolean? = null,
        var batchDetails: FuelBatchDetailsDto?= null,
        var teamsDetails: TeamsFuelDetailsDto?= null,
        var officersList: List<MsUsersDto>? = null,
        var remarksDetails: List<MSRemarksDto>? = null,
        var officersAssigned: MsUsersDto? = null,
        var rapidTest: FuelEntityRapidTestDto? = null,
        var rapidTestProducts: List<RapidTestProductsDetailsDto>? = null,
        var fuelUploadedFiles: List<FuelFilesFoundDto>? = null,
        var sampleCollected: SampleCollectionDto? = null,
        var sampleSubmitted: List<SampleSubmissionDto>? = null,
        var sampleLabResults: List<MSSSFLabResultsDto>? = null,
        var fuelRemediation: FuelRemediationDto? = null,
        var ssfCountAdded: Int? = null,
        var bsNumberCountAdded: Int? = null,
        var analysisLabCountDone: Int? = null,
        var quotationGeneratedStatus: Boolean? = null,
)

data class WorkPlanInspectionDto(
        var id: Long?= 0,
        var productCategory: String? = null,
        var broadProductCategory: String? = null,
        var product: String? = null,
        var standardCategory: String? = null,
        var productSubCategory: String? = null,
        var divisionId: String? = null,
        var timelineStartDate: Date? = null,
        var timelineEndDate: Date? = null,
        var timelineOverDue: Boolean? = null,
//        var sampleSubmittedId: Long? = null,
        var division: String? = null,
        var officerName: String? = null,
        var nameActivity: String? = null,
        var targetedProducts: String? = null,
        var resourcesRequired: List<PredefinedResourcesRequiredEntityDto>? = null,
        var budget: String? = null,
        var approvedOn: Date? = null,
        var approvedStatus: Boolean? = null,
        var workPlanYearId: Long? = null,
        var clientAppealed: Boolean? = null,
        var directorRecommendationRemarksStatus: Boolean? = null,
        var hodRecommendationStatus: Boolean? = null,
        var hodRecommendationStart: Boolean? = null,
        var hodRecommendation: String? = null,
        var destructionNotificationStatus: Boolean? = null,
        var destructionNotificationDate: Date? = null,
        var hodRecommendationRemarks: String? = null,
        var preliminaryParamStatus: Boolean? = null,
        var dataReportGoodsStatus: Boolean? = null,
        var scfLabparamsStatus: Boolean? = null,
        var bsNumberStatus: Boolean? = null,
        var workPlanCompliantStatus: Boolean? = null,
        var ssfLabparamsStatus: Boolean? = null,
        var msPreliminaryReportStatus: Boolean? = null,
        var preliminaryApprovedStatus: Boolean? = null,
        var msFinalReportStatus: Boolean? = null,
        var finalApprovedStatus: Boolean? = null,
        var chargeSheetStatus: Boolean? = null,
        var investInspectReportStatus: Boolean? = null,
        var sampleCollectionStatus: Boolean? = null,
        var sampleSubmittedStatus: Boolean? = null,
        var seizureDeclarationStatus: Boolean? = null,
        var dataReportStatus: Boolean? = null,
        var approvedBy: String? = null,
        var approved: String? = null,
        var rejectedOn: Date? = null,
        var rejectedStatus: Boolean? = null,
        var submittedForApprovalStatus: Boolean? = null,
        var onsiteStartStatus: Boolean? = null,
        var onsiteStartDate: Date? = null,
        var onsiteEndDate: Date? = null,
        var sendSffDate: Date? = null,
        var sendSffStatus: Boolean? = null,
        var onsiteEndStatus: Boolean? = null,
        var destractionStatus: Boolean? = null,
        var rejectedBy: String? = null,
        var rejected: String? = null,
        var msEndProcessRemarks: String? = null,
        var rejectedRemarks: String? = null,
        var approvedRemarks: String? = null,
        var progressValue: Boolean? = null,
        var progressStep: String? = null,
        var county: String? = null,
        var subcounty: String? = null,
        var townMarketCenter: String? = null,
        var locationActivityOther: String? = null,
        var timeActivityDate: Date? = null,
        var timeDateReportSubmitted: Date? = null,
        var timeActivityRemarks: String? = null,
        var rescheduledDateNotVisited: Date? = null,
        var rescheduledDateReportSubmitted: Date? = null,
        var rescheduledActivitiesRemarks: String? = null,
        var activityUndertakenPeriod: String? = null,
        var nameHof: String? = null,
        var reviewSupervisorDate: Date? = null,
        var reviewSupervisorRemarks: String? = null,
        var destructionClientEmail: String? = null,
        var region: String? = null,
        var complaintId: Long? = null,
        var officerDetails: MsUsersDto? = null,
        var hodRmAssignedDetails: MsUsersDto? = null,
        var hofAssignedDetails: MsUsersDto? = null,
        var destructionDocId: Long? = null,
        var scfDocId: Long? = null,
        var ssfDocId: Long? = null,
        var seizureDocId: Long? = null,
        var declarationDocId: Long? = null,
        var chargeSheetDocId: Long? = null,
        var dataReportDocId: Long? = null,
        var complaintDepartment: String? = null,
        var referenceNumber: String? = null,
        var batchDetails: WorkPlanBatchDetailsDto? = null,
        var ksApplicable: StandardDetailsDto? = null,
        var remarksDetails: List<MSRemarksDto>? = null,
        var workPlanFiles: List<WorkPlanFilesFoundDto>? = null,
        var chargeSheet: ChargeSheetDto? = null,
        var seizureDeclarationDto: List<SeizureDto>? = null,
        var inspectionInvestigationDto: InspectionInvestigationReportDto? = null,
        var dataReportDto: DataReportDto? = null,
        var sampleCollected: SampleCollectionDto? = null,
        var sampleSubmitted: List<SampleSubmissionDto>? = null,
        var sampleLabResults: List<MSSSFLabResultsDto>? = null,
        var compliantStatusAdded: Boolean? = null,
        var destructionRecommended: Boolean? = null,
        var finalReportGenerated: Boolean? = null,
        var appealStatus: Boolean? = null,
        var msProcessEndedStatus: Boolean? = null,
        var preliminaryReport: PreliminaryReportDto? = null,
        var preliminaryReportFinal: PreliminaryReportDto? = null,
        var officersList: List<MsUsersDto>? = null,
        var hofList: List<MsUsersDto>? = null,
        val updateWorkPlan: WorkPlanEntityDto? = null,
        val updatedStatus: Boolean? = null,
        val resubmitStatus: Boolean? = null,
        val recommendationDoneStatus: Boolean? = null,
        var bsNumberCountAdded: Int? = null,
        var analysisLabCountDone: Int? = null,
        var productListRecommendationAddedCount: Int? = null,
        var productList: List<WorkPlanProductDto>? = null,
)

data class FuelEntityDto(
        @NotNull(message = "Required field")
        @NotEmpty(message = "Required field")
        var company: String,

        @NotNull(message = "Required field")
        @NotEmpty(message = "Required field")
        var petroleumProduct: String,

        @NotNull(message = "Required field")
        @NotEmpty(message = "Required field")
        var physicalLocation: String,

        @NotNull(message = "Required field")
        @NotEmpty(message = "Required field")
        var inspectionDateFrom: Date,

        @NotNull(message = "Required field")
        @NotEmpty(message = "Required field")
        var inspectionDateTo: Date,

        @NotNull(message = "Required field")
        @NotEmpty(message = "Required field")
        var stationOwnerEmail: String,

        @NotNull(message = "Required field")
        @NotEmpty(message = "Required field")
        var stationKraPin: String,

        @NotNull(message = "Required field")
        @NotEmpty(message = "Required field")
        var townID: Long,

        var remarks: String? = null,
)

data class WorkPlanTownsDto(
        var countyID: Long? = null,
        var townID: Long? = null,
        var locationName: String? = null
)

data class AllWorkPlanDetails(
        var mainDetails: WorkPlanEntityDto? = null,
        var countyTownDetails: List<WorkPlanTownsDto>? = null
)

data class WorkPlanEntityDto(
        var id: Long?= 0,
        var complaintDepartment: Long? = null,
        var divisionId: Long? = null,
        var nameActivity: String? = null,
        var timeActivityDate: Date? = null,
        var county: Long? = null,
        var townMarketCenter: Long? = null,
        var locationActivityOther: String? = null,
        var standardCategory: Long? = null,
        var broadProductCategory: Long? = null,
        var productCategory: Long? = null,
        var product: Long? = null,
        var productSubCategory: Long? = null,
        var resourcesRequired: List<PredefinedResourcesRequiredEntityDto>? = null,
        var budget: String? = null,
        var remarks: String? = null,
)

data class WorkPlanScheduleApprovalDto(
        @NotNull(message = "Required field")
        var approvalStatus: Boolean,
        var remarks: String? = null,
)

data class ApprovalDto(
        @NotNull(message = "Required field")
        var approvalStatus: Boolean,
        var remarks: String? = null,
)

data class WorkPlanFinalRecommendationDto(
        @NotNull(message = "Required field")
        var recommendationId: List<RecommendationDto>,
        @NotNull(message = "Required field")
        var hodRecommendationRemarks: String,
)

data class RecommendationDto (
        var recommendationId: Long? = null,
        var recommendationName: String? = null,
)

data class WorkPlanFeedBackDto(
        @NotNull(message = "Required field")
        var hodFeedBackRemarks: String,
)

data class FuelEntityAssignOfficerDto(
        @NotNull(message = "Required field")
        var assignedUserID: Long,
        var remarks: String? = null,
)

data class FuelEntityRapidTestDto(
        @NotNull(message = "Required field")
        var rapidTestRemarks: String? = null,
        @NotNull(message = "Required field")
        var rapidTestStatus: Boolean,
)


data class MsComplaintAcceptanceStatusDto(
        var acceptanceRemarks: String? = null,
        var ogaWhereRemarks: String? = null,
        var acceptanceStatus: Boolean,
        var rejectedStatus: Boolean,
        var mandateOGAStatus: Boolean,
)

data class SSFCompliantStatusDto(
        @NotNull(message = "Required field")
        var compliantRemarks: String? = null,
        @NotNull(message = "Required field")
        var compliantStatus: Boolean,
)

data class SampleCollectionDto(
        var id: Long?= 0,
        var nameManufacturerTrader: String?= null,
        var addressManufacturerTrader: String?= null,
        var samplingMethod: String?= null,
        var reasonsCollectingSamples: String?= null,
        var anyRemarks: String?= null,
        var designationOfficerCollectingSample: String?= null,
        var nameOfficerCollectingSample: String?= null,
        var dateOfficerCollectingSample: Date?= null,
        var nameWitness: String?= null,
        var designationWitness: String?= null,
        var dateWitness: Date?= null,
        var productsList: List<SampleCollectionItemsDto>? = null,
)

data class RapidTestProductsDto(
        var id: Long?= 0,
        var productName: String?= null,
        var sampleSize: String?= null,
        var batchSize: String?= null,
        var batchNumber: String?= null,
        var sulphurMarkerTest: String?= null,
        var exportMarkerTestStatus: Int?= null,
        var domesticKeroseneMarkerTestStatus: Int?= null,
        var sulphurMarkerTestStatus: Int?= null,
        var overallComplianceStatus: Int?= null,
)

data class RapidTestProductsDetailsDto(
        var id: Long?= 0,
        var productName: String?= null,
        var sampleSize: String?= null,
        var batchSize: String?= null,
        var batchNumber: String?= null,
        var exportMarkerTest: String?= null,
        var domesticKeroseneMarkerTest: String?= null,
        var sulphurMarkerTest: String?= null,
        var exportMarkerTestStatus: Boolean,
        var domesticKeroseneMarkerTestStatus: Boolean,
        var sulphurMarkerTestStatus: Boolean,
        var overallComplianceStatus: Boolean,
)

data class SampleCollectionItemsDto(
        var id: Long?= 0,
        var productBrandName: String? = null,
        var batchNo: String? = null,
        var batchSize: String? = null,
        var sampleSize: String? = null,
        var ssfAdded: Boolean? = null,
)

data class RemarksToAddDto(
        var remarksDescription: String? = null,
        var remarksStatus: String? = null,
        var processID: Long? = null,
        var userId: Long? = null,
)

data class ChargeSheetDto(
        var id: Long?= 0,
        var christianName: String? = null,
        var surname: String? = null,
        var sex: String? = null,
        var nationality: String? = null,
        var age: Long? = null,
        var addressDistrict: String? = null,
        var addressLocation: String? = null,
        var firstCount: String? = null,
        var particularsOffenceOne: String? = null,
        var secondCount: String? = null,
        var particularsOffenceSecond: String? = null,
        var dateArrest: Date? = null,
        var withWarrant: String? = null,
        var applicationMadeSummonsSue: String? = null,
        var dateApprehensionCourt: Date? = null,
        var bondBailAmount: Long? = null,
        var remandedAdjourned: String? = null,
        var complainantName: String? = null,
        var complainantAddress: String? = null,
        var prosecutor: String? = null,
        var witnesses: String? = null,
        var sentence: String? = null,
        var finePaid: String? = null,
        var courtName: String? = null,
        var courtDate: Date? = null,
        var remarks: String?= null,
)

data class DataReportDto(
        var id: Long?= 0,
        var referenceNumber: String? = null,
        var inspectionDate: Date? = null,
        var inspectorName: String? = null,
        var function: String? = null,
        var department: String? = null,
        var regionName: String? = null,
        var town: String? = null,
        var marketCenter: String? = null,
        var outletDetails: String? = null,
        var personMet: String? = null,
        var summaryFindingsActionsTaken: String? = null,
        var finalActionSeizedGoods: String? = null,
        var totalComplianceScore: String? = null,
        var remarks: String? = null,
        var productsList: List<DataReportParamsDto>? = null,
)

data class DataReportParamsDto(
        var id: Long?= 0,
        var typeBrandName: String? = null,
        var localImport: String? = null,
        var complianceInspectionParameter: Int? = null,
        var measurementsResults: String? = null,
        var remarks: String? = null,
)

data class InspectionInvestigationReportDto(
        var id: Long?= 0,
        var reportReference: String? = null,
        var reportTo: String? = null,
        var reportThrough: String? = null,
        var reportFrom: String? = null,
        var reportSubject: String? = null,
        var reportTitle: String? = null,
        var reportDate: Date? = null,
        var reportRegion: String? = null,
        var reportDepartment: String? = null,
        var reportFunction: String? = null,
        var backgroundInformation: String? = null,
        var objectiveInvestigation: String? = null,
        var dateInvestigationInspection: Date? = null,
        var kebsInspectors: List<KebsOfficersName>? = null,
        var methodologyEmployed: String? = null,
        var findings: String? = null,
        var conclusion: String? = null,
        var recommendations: String? = null,
        var statusActivity: String? = null,
        var finalRemarkHod: String? = null,
        var remarks: String? = null,
)

data class KebsOfficersName (
        var inspectorName: String? = null,
        var institution: String? = null,
        var designation: String? = null,
)

data class SeizureListDto(
        var seizureList: List<SeizureDto>?= null,
)

data class SeizureDto(
        var id: Long?= 0,
        var marketTownCenter: String?=null,
        var nameOfOutlet: String?=null,
        var descriptionProductsSeized: String?=null,
        var brand: String?=null,
        var sector: String?=null,
        var reasonSeizure: String?=null,
        var nameSeizingOfficer: String?=null,
        var seizureSerial: String?=null,
        var quantity: String?=null,
        var unit: String?=null,
        var estimatedCost: String?=null,
        var currentLocation: String?=null,
        var productsDestruction: String?=null,

)

data class SeizureDeclarationDto(
        var id: Long?= 0,
        var seizureTo: String? = null,
        var seizurePremises: String? = null,
        var seizureRequirementsStandards: String? = null,
        var goodsName: String? = null,
        var goodsManufactureTrader: String? = null,
        var goodsAddress: String? = null,
        var goodsPhysical: String? = null,
        var goodsLocation: String? = null,
        var goodsMarkedBranded: String? = null,
        var goodsPhysicalSeal: String? = null,
        var descriptionGoods: String? = null,
        var goodsQuantity: String? = null,
        var goodsThereforei: String? = null,
        var nameInspector: String? = null,
        var designationInspector: String? = null,
        var dateInspector: Date? = null,
        var nameManufactureTrader: String? = null,
        var designationManufactureTrader: String? = null,
        var dateManufactureTrader: Date? = null,
        var nameWitness: String? = null,
        var designationWitness: String? = null,
        var dateWitness: Date? = null,
        var declarationTakenBy: String? = null,
        var declarationOnThe: String? = null,
        var declarationDayOf: Date? = null,
        var declarationMyName: String? = null,
        var declarationIresideAt: String? = null,
        var declarationIemployeedAs: String? = null,
        var declarationIemployeedOf: String? = null,
        var declarationSituatedAt: String? = null,
        var declarationStateThat: String? = null,
        var declarationIdNumber: String? = null,
        var remarks: String? = null,
)

data class SampleSubmissionDtoPDF(
        var id: String?= null,
        var nameProduct : String? = null,
        var packaging : String? = null,
        var labellingIdentification : String? = null,
        var fileRefNumber : String? = null,
        var referencesStandards : String? = null,
        var sizeTestSample : String? = null,
        var sizeRefSample : String? = null,
        var condition : String? = null,
        var sampleReferences : String? = null,
        var sendersName : String? = null,
        var designation : String? = null,
        var address : String? = null,
        var sendersDate : String? = null,
        var receiversName : String? = null,
        var testChargesKsh : String? = null,
        var receiptLpoNumber : String? = null,
        var invoiceNumber : String? = null,
        var disposal : String? = null,
        var remarks : String? = null,
        var sampleCollectionNumber : String? = null,
        var sampleCollectionProduct : String? = null,
        var bsNumber : String? = null,
        var parametersList: List<SampleSubmissionItemsDto>? = null,
)

data class SampleSubmissionDto(
        var id: Long?= 0,
        var nameProduct : String? = null,
        var packaging : String? = null,
        var labellingIdentification : String? = null,
        var fileRefNumber : String? = null,
        var referencesStandards : String? = null,
        var sizeTestSample : String? = null,
        var sizeRefSample : String? = null,
        var condition : String? = null,
        var sampleReferences : String? = null,
        var sendersName : String? = null,
        var designation : String? = null,
        var address : String? = null,
        var sendersDate : Date? = null,
        var receiversName : String? = null,
        var testChargesKsh : BigDecimal? = null,
        var receiptLpoNumber : String? = null,
        var invoiceNumber : String? = null,
        var disposal : String? = null,
        var remarks : String? = null,
        var sampleCollectionNumber : Long? = null,
        var sampleCollectionProduct : Long? = null,
        var bsNumber : String? = null,
        var parametersList: List<SampleSubmissionItemsDto>? = null,
)

data class SampleSubmissionItemsDto(
        var id: Long? = 0,
        var parameters : String? = null,
        var laboratoryName : String? = null,
)

data class PreliminaryReportItemsDto(
        var id: Long? = 0,
        var marketCenter: String? = null,
        var nameOutlet: String? = null,
        var sector: String? = null,
        var dateVisit: Date? = null,
        var numberProductsPhysicalInspected: Long? = null,
        var compliancePhysicalInspection: Long? = null,
        var remarks: String? = null,
        var preliminaryReportID: Long? = null,
)

data class WorkPlanProductDto(
        var id: Long? = 0,
        var productName: String? = null,
        var referenceNo: String? = null,
        var recommendation: List<RecommendationDto>? = null,
        var destructionRecommended: Boolean? = null,
        var hodRecommendationStatus: Boolean? = null,
        var hodRecommendationRemarks: String? = null,
        var directorRecommendationStatus: Boolean? = null,
        var directorRecommendationRemarks: String? = null,
        var clientAppealed: Boolean? = null,
        var destructionStatus: Boolean? = null,
        var appealStatus: Boolean? = null,
        var destructionNotificationStatus: Boolean? = null,
        var destructionNotificationDocId: Long? = null,
        var workPlanId: Long? = null,
        var ssfId: Long? = null,
        var destructionClientEmail: String? = null,
        var destructionClientFullName: String? = null,
        var destructionNotificationDate: Date? = null,
        var destructionDocId: Long? = null,
        var destructedStatus: Boolean? = null,
)

data class BSNumberSaveDto(
        @NotNull(message = "Required field")
        var bsNumber: String,
        @NotNull(message = "Required field")
        var submittedDate: Date,
        @NotNull(message = "Required field")
        var ssfID: Long,
        var remarks: String? = null,
)

data class LabResultsDto(
        var parametersListTested: List<LabResultsParamDto>? = null,
        var savedPDFFiles: List<LabResultsParamDto>? = null,
        var result : String? = null,
        var method : String? = null,
)

data class LabResultsParamDto(
        var param : String? = null,
        var result : String? = null,
        var method : String? = null,
)

data class FuelRemediationDto(
        var productType: String? = null,
        var applicableKenyaStandard: String? = null,
        var remediationProcedure: String? = null,
        var volumeOfProductContaminated: String? = null,
        var contaminatedFuelType: String? = null,
        var quantityOfFuel: String? = null,
        var volumeAdded: String? = null,
        var totalVolume: String? = null,
        var proFormaInvoiceStatus: Boolean? = null,
        var proFormaInvoiceNo: String? = null,
        var invoiceAmount: BigDecimal? = null,
        var payableAmount: BigDecimal? = null,
        var feePaidReceiptNo: String? = null,
        var dateOfRemediation: Date? = null,
        var dateOfPayment: Timestamp? = null,
        var invoiceCreated: Boolean? = null,
)

data class MSSSFLabResultsDto(
        var ssfResultsList: MSSSFComplianceStatusDetailsDto? = null,
        var savedPDFFiles:  List<MSSSFPDFListDetailsDto>? = null,
        var limsPDFFiles:  List<LIMSFilesFoundDto>? = null,
        var parametersListTested: List<LabResultsParamDto>? = null,
)

data class MSSSFComplianceStatusDetailsDto(
        var sffId: Long? = null,
        var bsNumber: String? = null,
        var complianceRemarks: String? = null,
        var complianceStatus: Boolean? = null,
        var analysisDone: Boolean? = null,
)

data class LIMSFilesFoundDto(
        var fileSavedStatus: Boolean? = null,
        var fileName: String? = null,
)

data class ComplaintsFilesFoundDto(
        var id: Long?= 0,
        var fileName: String? = null,
        var documentType: String? = null,
        var fileContentType: String? = null,
)

data class WorkPlanFilesFoundDto(
        var id: Long?= 0,
        var fileName: String? = null,
        var documentType: String? = null,
        var fileContentType: String? = null,
)

data class FuelFilesFoundDto(
        var id: Long?= 0,
        var fileName: String? = null,
        var documentType: String? = null,
        var fileContentType: String? = null,
)

data class PDFSaveComplianceStatusDto(
        @NotNull(message = "Required field")
        var ssfID: Long,
        @NotNull(message = "Required field")
        var bsNumber: String,
        @NotNull(message = "Required field")
        var PDFFileName: String,
        @NotNull(message = "Required field")
        var complianceStatus: Boolean,
        @NotNull(message = "Required field")
        var complianceRemarks: String,
)

data class SSFSaveComplianceStatusDto(
        @NotNull(message = "Required field")
        var ssfID: Long,
        @NotNull(message = "Required field")
        var bsNumber: String,
        @NotNull(message = "Required field")
        var complianceStatus: Boolean,
        @NotNull(message = "Required field")
        var complianceRemarks: String,
)

data class PreliminaryReportDto(
        var id: Long? = 0,
        var reportTo: String? = null,
        var reportFrom: String? = null,
        var reportSubject: String? = null,
        var reportTitle: String? = null,
        var reportDate: Date? = null,
        var surveillanceDateFrom: Date? = null,
        var surveillanceDateTo: Date? = null,
        var reportBackground: String? = null,
        var kebsOfficersName: List<KebsOfficersName>? = null,
        var surveillanceObjective: String? = null,
        var surveillanceConclusions: String? = null,
        var surveillanceRecommendation: String? = null,
        var remarks: String? = null,
        var parametersList: List<PreliminaryReportItemsDto>? = null,
        var approvedStatusHofFinal: Boolean? = null,
        var rejectedStatusHofFinal: Boolean? = null,
        var approvedStatus: Boolean? = null,
        var rejectedStatus: Boolean? = null,
        var approvedStatusHodFinal: Boolean? = null,
        var rejectedStatusHodFinal: Boolean? = null,
        var approvedStatusHod: Boolean? = null,
        var rejectedStatusHod: Boolean? = null,

)

data class CompliantRemediationDto(
        var remarks: String? = null,
        var proFormaInvoiceStatus: Boolean? = null,
        var dateOfRemediation: Date? = null,
        var volumeFuelRemediated: Long?= null,
        var subsistenceTotalNights: Long?= null,
        var subsistenceTotalNightsRate: Long?= null,
        var transportAirTicket: Long?= null,
        var transportInkm: Long?= null,
)

data class PreliminaryReportFinalDto(
        var remarks: String? = null,
        var surveillanceConclusions: String? = null,
        var surveillanceRecommendation: String? = null,
)

data class RemediationDto(
        var productType: String? = null,
        var quantityOfFuel: String? = null,
        var contaminatedFuelType: String? = null,
        var applicableKenyaStandard: String? = null,
        var remediationProcedure: String? = null,
        var volumeOfProductContaminated: String? = null,
        var volumeAdded: String? = null,
        var totalVolume: String? = null,
)


data class MSSSFPDFListDetailsDto(
        var pdfSavedId: Long? = null,
        var pdfName: String? = null,
        var sffId: Long? = null,
        var complianceRemarks: String? = null,
        var complianceStatus: Boolean? = null,
)



data class ComplaintApproveRejectAssignDto(
        var division: Long? = null,
        var approved: Int? = 0,
        var approvedRemarks: String? = null,
        var rejected: Int? = 0,
        var rejectedRemarks: String? = null,
        var mandateForOga: Int? = 0,
        var advisedWhereToRemarks: String? = null,
        var assignedIoStatus: Int? = null,
        var assignedIoRemarks: String? = null,
        var assignedIo: Long? = null
)

data class ComplaintApproveDto(
        var department: Long? = null,
        var division: Long? = null,
        var approved: Int? = null,
        var approvedRemarks: String? = null
)

data class ComplaintRejectDto(
        var rejected: Int? = null,
        var rejectedRemarks: String? = null
)

//data class FindComplaintDetailsDto(
//        var refNumber: String? = null,
//)
data class ComplaintSearchValues(
        var refNumber: String? = null,
        var date: Date? = null
//        var approvedStatus: Int? = null,
//        var assignedIOStatus: Int? = null,
//        var rejectedStatus: Int? = null,
//        var lastName: String? = null
)

data class ComplaintAdviceRejectDto(
        var mandateForOga: Int? = null,
        var advisedWhereToRemarks: String? = null,
        var amendmentRemarks: String? = null,
        var rejectedRemarks: String? = null
)

data class ComplaintAssignDto(
        var assignedRemarks: String? = null,
        var assignedIo: Long? = null
)

data class RegionReAssignDto(
        var reassignedRemarks: String? = null,
        var countyID: Long? = null,
        var townID: Long? = null
)

data class StandardDetailsDto(
        var standardTitle: String? = null,
        var standardNumber: String? = null,
        var ics: String? = null,
        var hsCode: String? = null,
        var subCategoryId: Long? = null,
)

data class ComplaintClassificationDto(
        var productClassification: Long? = null,
        var broadProductCategory: Long? = null,
        var productCategory: Long? = null,
        var myProduct: Long? = null,
        var productSubcategory: Long? = null,
        var classificationRemarks: String? = null,
)

data class MSRemarksDto(
        var id: Long?= 0,
        var remarksDescription: String? = null,
        var remarksStatus: String? = null,
        var processBy: String? = null,
        var processName: String? = null
)


data class AllComplaintsDetailsDto(
        var complaintsDetails: ComplaintsDetailsDto? = null,
        var acceptanceDone : Boolean,
        var acceptanceResults : MsComplaintAcceptanceStatusDto? =null,
        var officersList: List<MsUsersDto>? = null,
        var hofList: List<MsUsersDto>? = null,
        var officersAssigned: MsUsersDto? = null,
        var hofAssigned: MsUsersDto? = null,
        var remarksDetails: List<MSRemarksDto>? = null,
        var sampleCollected: SampleCollectionDto? = null,
        var sampleSubmitted: SampleSubmissionDto? = null,
        var sampleLabResults: MSSSFLabResultsDto? = null,
        var complaintProcessStatus: Boolean,
        var workPlanRefNumber: String?= null,
        var workPlanBatchRefNumber: String?= null
)

data class ComplaintsDetailsDto(
        var id: Long?= 0,
        var refNumber: String? = null,
        var complainantName: String? = null,
        var complainantEmail: String? = null,
        var complainantPhoneNumber: String? = null,
        var complainantPostalAddress: String? = null,
        var complainantPhysicalAddress: String? = null,
        var complaintSampleDetails: String? = null,
        var remedySought: String? = null,
        var email: String? = null,
        var nameContactPerson: String? = null,
        var phoneNumber: String? = null,
        var telephoneNumber: String? = null,
        var businessAddress: String? = null,
        var complaintCategory: String? = null,
        var complaintTitle: String? = null,
        var complaintDescription: String? = null,
        var standardCategory: String? = null,
        var broadProductCategory: String? = null,
        var productCategory: String? = null,
        var productSubcategory: String? = null,
        var productName: String? = null,
        var productBrand: String? = null,
        var county: String? = null,
        var town: String? = null,
        var marketCenter: String? = null,
        var buildingName: String? = null,
        var date: Date? = null,
        var status: String? = null,
        var approvedStatus: Boolean? = null,
        var assignedIOStatus: Boolean? = null,
        var rejectedStatus: Boolean? = null,
        var classificationDetailsStatus: Boolean? = null,
        var complaintFiles: List<ComplaintsFilesFoundDto>? = null,
        var ksApplicable: StandardDetailsDto? = null,
        var timelineStartDate: Date? = null,
        var timelineEndDate: Date? = null,
        var timelineOverDue: Boolean? = null


)

data class ComplaintsTaskAndAssignedDto(
        var complaintAssigned: List<ComplaintsListDto>? = null,
        var complaintTaskID: List<ComplaintsListDto>? = null,
)

data class ComplaintsListDto(
        var referenceNumber: String? = null,
        var complaintTitle: String? = null,
        var targetedProducts: String? = null,
//        var complaintCategory: String? = null,
        var transactionDate: Date? = null,
        var progressStep: String? = null
)

data class MsDepartmentDto(
        var id: Long?= 0,
        val department: String? = null,
        val descriptions: String? = null,
        val directorateId: Long? = null,
        val status: Boolean? = null
)

data class MsRecommendationDto(
        var id: Long?= 0,
        var recommendationName: String? = null,
        var description: String? = null,
        var status: Boolean? = null,
)

data class MsUsersDto(
        var id: Long?= 0,
        var firstName: String? = null,
        var lastName: String? = null,
        var userName: String? = null,
        var email: String? = null,
        var phoneNumber: String? = null,
        val status: Boolean? = null
)

data class LaboratoryDto(
        var id: Long?= 0,
        var labName: String?= null,
        var description: String?= null,
        var status: Boolean?= null,
)


data class MsDivisionDto(
        var id: Long?= 0,
        val division: String? = null,
        val descriptions: String? = null,
        val status: Int? = null,
        val departmentId: Long? = null
)


data class NewComplaintDto(
        val complaintDetails: ComplaintDto,
        val customerDetails: ComplaintCustomersDto,
        val locationDetails: ComplaintLocationDto
//        val complaintFilesDetails: ComplaintFilesDto,
)

data class DestructionNotificationDto(
        val clientFullName: String? = null,
        val clientEmail: String? = null,
        val remarks: String? = null,
//        val complaintFilesDetails: ComplaintFilesDto,
)

data class EndFuelDto(
        val remarks: String? = null
)

data class ComplaintFilesDto(
        val fileDetails: List<FileDTO>? = null
)

data class FileDTO(
        var fileType: String? = null,
        var name: String? = null,
        var document: ByteArray? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FileDTO

        if (fileType != other.fileType) return false
        if (name != other.name) return false
        if (document != null) {
            if (other.document == null) return false
            if (!document.contentEquals(other.document)) return false
        } else if (other.document != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = fileType?.hashCode() ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (document?.contentHashCode() ?: 0)
        return result
    }
}

data class ComplaintDto(
        var complaintCategory: Long? = null,
        var complaintTitle: String? = null,
        var productClassification: Long? = null,
        var broadProductCategory: Long? = null,
        var productCategory: Long? = null,
        var myProduct: Long? = null,
        var productSubcategory: Long? = null,
        var productBrand: String? = null,
        var complaintDescription: String? = null,
        var complaintSampleDetails: String? = null,
        var remedySought: String? = null
)

data class ComplaintCustomersDto(
        var firstName: String? = null,
        var lastName: String? = null,
        var phoneNumber: String? = null,
        var emailAddress: String? = null,
        var postalAddress: String? = null,
        var physicalAddress: String? = null
)

data class ComplaintLocationDto(
        var county: Long? = null,
        var town: Long? = null,
        var marketCenter: String? = null,
        var buildingName: String? = null,
        var email: String? = null,
        var nameContactPerson: String? = null,
        var phoneNumber: String? = null,
        var telephoneNumber: String? = null,
        var businessAddress: String? = null
)


data class MSTypeDto(
        var uuid: String? = null,
        var typeName: String? = null,
        var markRef: String? = null,
        var description: String? = null,
        var status: Int? = null
)

data class MSComplaintSubmittedSuccessful(
        var refNumber: String? = null,
        var savedStatus: Boolean? = null,
        var successMessage: String? = null,
        var errorMessage: String? = null
)

data class LevyPaymentDTO(
        var Id: Long? = null,
        var entryNumber: String?=null,
        var paymentDate: String?=null,
        var totalPaymentAmt: String?=null,
        var companyName: String?=null,
        var kraPin: String?=null,
        var periodFrom: String?=null,
        var periodTo: String?=null,
        var paymentSlipNo: String?=null,
        var paymentSlipDate: String?=null,
        var paymentType: String?=null,
        var totalDeclAmt: String?=null,
        var totalPenaltyAmt: String?=null,
        var bankRefNo: String?=null,
        var bankName: String?=null,
        var commodityType: String?=null,
        var qtyManf: String?=null,
        var exFactVal: String?=null,
        var levyPaid: String?=null,
        var penaltyPaid: String?=null
)

data class ActiveFirmsDTO(
        var id: Long?=null,
        var entryNumber:String?=null,
        var kraPin:String?=null,
        var name:String?=null,
        var postalAddress:String?=null,
        var adminLocation:String?=null,
        var streetName:String?=null,
        var businessLineName:String?=null,
        var regionName:String?=null,
        var townName:String?=null

)

data class RegisteredFirmsDTO(
        var id: Long?=null,
        var entryNumber:String?=null,
        var kraPin:String?=null,
        var name:String?=null,
        var postalAddress:String?=null,
        var companyTelephone:String?=null,
        var companyEmail:String?=null,
        var streetName:String?=null,
        var businessLineName:String?=null,
        var regionName:String?=null,
        var townName:String?=null,
        var CreatedOn:String?=null

)

data class DormantFirmsDTO(
        var id: Long?=null,
        var entryNumber:String?=null,
        var kraPin:String?=null,
        var name:String?=null,
        var postalAddress:String?=null,
        var streetName:String?=null,
        var businessLineName:String?=null,
        var regionName:String?=null

)

data class ClosedFirmsDTO(
        var id: Long?=null,
        var entryNumber:String?=null,
        var kraPin:String?=null,
        var name:String?=null,
        var postalAddress:String?=null,
        var streetName:String?=null,
        var businessLineName:String?=null,
        var regionName:String?=null,
        var dateOfClosure:String?=null

)

data class LevyPaymentReportDTO(
        var id: Long?=null,
        var entryNumber:String?=null,
        var kraPin:String?=null,
        var name:String?=null,
        var businessLineName:String?=null,
        var regionName:String?=null,
        var periodTo: String?=null,
        var paymentDate: String?=null,
        var levyPaid: String?=null,

)

data class LevyPenaltyReportDTO(
        var id: Long?=null,
        var entryNumber:String?=null,
        var kraPin:String?=null,
        var name:String?=null,
        var periodFrom: String?=null,
        var penaltyPaid: String?=null,
        var paymentDate: String?=null,
        var monthsLate: String?=null,
        var totalPenaltyAmt: String?=null,
        var amountDue: String?=null

        )

data class LevyPaymentsDTO(
        var Id: Long? = null,
        var entryNumber: String?=null,
        var paymentDate: String?=null,
        var totalPaymentAmt: String?=null,
        var companyName: String?=null,
        var firstName: String?=null,
        var lastName: String?=null,
        var kraPin: String?=null,
        var registrationNumber: String?=null,
        var periodFrom: String?=null,
        var periodTo: String?=null,
        var paymentSlipNo: String?=null,
        var paymentSlipDate: String?=null,
        var paymentType: String?=null,
        var totalDeclAmt: String?=null,
        var totalPenaltyAmt: String?=null,
        var bankRefNo: String?=null,
        var bankName: String?=null,
        var commodityType: String?=null,
        var qtyManf: String?=null,
        var exFactVal: String?=null,
        var levyPaid: String?=null,
        var penaltyPaid: String?=null
)

data class FuelRemediationDetailsDTO(
        var id: String? = null,
        var invoiceNumber: String? = null,
        var receiptNo: String? = null,
        var invoiceBatchNumberId: String? = null,
        var paymentStatus: String? = null,
        var amount: String? = null,
        var invoiceDate: String? = null,
        var paymentDate: String? = null,
        var transactionDate: String? = null,
        var status: String? = null,
        var remarks: String? = null,
        var varField1: String? = null,
        var varField2: String? = null,
        var varField3: String? = null,
        var varField4: String? = null,
        var varField5: String? = null,
        var varField6: String? = null,
        var varField7: String? = null,
        var varField8: String? = null,
        var varField9: String? = null,
        var varField10: String? = null,
        var createdBy: String? = null,
        var createdOn: String? = null,
        var lastModifiedBy: String? = null,
        var lastModifiedOn: String? = null,
        var updateBy: String? = null,
        var updatedOn: String? = null,
        var deleteBy: String? = null,
        var deletedOn: String? = null,
        var version: String? = null,
        var remunerationRateLiter: String? = null,
        var remunerationSubTotal: String? = null,
        var remunerationVat: String? = null,
        var remunerationTotal: String? = null,
        var volumeFuelRemediated: String? = null,
        var subsistenceTotalNights: String? = null,
        var subsistenceRate: String? = null,
        var subsistenceRateNightTotal: String? = null,
        var subsistenceVat: String? = null,
        var subsistenceTotal: String? = null,
        var transportAirTicket: String? = null,
        var transportInkm: String? = null,
        var transportRate: String? = null,
        var transportTotalKmrate: String? = null,
        var transportVat: String? = null,
        var transportTotal: String? = null,
        var transportGrandTotal: String? = null,
        var fuelInspectionId: String? = null,
        var fuelInspectionRefNumber: String? = null,
)

