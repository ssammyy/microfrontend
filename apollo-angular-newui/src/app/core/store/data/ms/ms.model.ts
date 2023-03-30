import {County} from '../county';
import {UserDetails} from '../master/master.model';

export class Ms {
}

export class ComplaintApproveDto {
    department: number;
    division: number;
    approvedRemarks: string;
    approved: number;
}

export class ComplaintApproveRejectAdviceWhereDto {
    department: number;
    division: number;
    approvedRemarks: string;
    approved: number;
    mandateForOga: number;
    advisedWhereToRemarks: string;
    amendmentRemarks: string;
}

export class ComplaintAdviceRejectDto {
    mandateForOga: number;
    advisedWhereToRemarks: string;
    amendmentRemarks: string;
    rejectedRemarks: string;
}

export class ComplaintRejectDto {
    rejected: number;
    rejectedRemarks: string;
}

export class MSComplaintSubmittedSuccessful {
    refNumber: string;
    savedStatus: boolean;
    successMessage: string;
    errorMessage: string;
}

export class MsDepartment {
    id?: number;
    department?: string;
    descriptions?: string;
    directorateId?: number;
    status?: boolean;
}


export class KebsStandardsDto {
    id: number;
    standardTitle: string;
    standardNumber: string;
    status: boolean;
}

export class MsDivisionDetails {
    id?: string;
    division?: string;
    descriptions?: string;
    status?: string;
    departmentId?: number;
}

export class MsStandardProductCategory {
    id?: number;
    standardCategory?: string;
    standardNickname?: string;
    standardId?: number;
    status?: boolean;
}

export class MsProductCategories {
    id?: number;
    name?: string;
    status?: boolean;
    broadProductCategoryId?: number;
}

export class MsBroadProductCategory {
    id?: number;
    category?: string;
    status?: boolean;
    divisionId?: number;
}

export class PredefinedResourcesRequired {
    id: number;
    resourceName: string;
    status: boolean;
}

export class OGAEntity {
    id: number;
    ogaName: string;
    status: boolean;
}

export class WorkPlanTownsDto {
        countyID: number;
        countyName: string;
        townID: number;
        townName: string;
        locationName: string;
}

export class RecommendationDto {
    recommendationId: number;
    recommendationName: string;

}

export class AllWorkPlanDetails {
     mainDetails: WorkPlanEntityDto;
     countyTownDetails: WorkPlanTownsDto[];
}

export class MsProducts {
    id?: number;
    name?: string;
    status?: boolean;
    productCategoryId?: number;
}

export class MsProductSubcategory {
    id?: number;
    name?: string;
    status?: boolean;
    productId?: number;
}

export class NewComplaintDto {
    complaintDetails: ComplaintDto;
    customerDetails: ComplaintCustomersDto;
    locationDetails: ComplaintLocationDto;
}

export class MsNotificationTaskDto {
    id: number;
    notificationBody: NotificationBodyDto;
    notificationMsg: string;
    notificationName: string;
    notificationType: string;
    fromUserId: number;
    toUserId: number;
    readStatus: boolean;
}

export class PermitUcrSearch {
    id: number;
    permitNumber: string;
    ucrNumber: string;
    productName: string;
    validityStatus: boolean;
}

export class UcrNumberSearch {
    itemId: number;
    ucrNumber: string;
    itemDescription: string;
    quantity: number;
    packageQuantity: number;
    itemGrossWeight: string;
    hsDescription: string;
    itemHsCode: string;
}

export class NotificationBodyDto {
    taskRefNumber: string;
    fromName: string;
    toName: string;
    batchReferenceNoFound: string;
    referenceNoFound: string;
    dateAssigned: Date;
    bsNumber: string;
    processType: string;
}

export class ApiResponseModel {
    totalCount: number;
    extras: any;
    message: string;
    responseCode: string;
    data: any;
    errors: any;
    pageNo: number;
    totalPages: number;
}



export class AllComplaintsDetailsDto {
    complaintsDetails: ComplaintDetailsDto;
    acceptanceDone: boolean;
    acceptanceResults: MsComplaintAcceptanceStatusDto;
    officersList: MsUsersDto[];
    hofList: MsUsersDto[];
    officersAssigned: MsUsersDto;
    hofAssigned: MsUsersDto;
    remarksDetails: MSRemarksDto[];
    sampleCollected: SampleCollectionDto;
    sampleSubmitted: SampleSubmissionDto;
    sampleLabResults: MSSSFLabResultsDto;
    complaintProcessStatus: boolean;
    workPlanRefNumber: string;
    workPlanBatchRefNumber: string;
    // reAssigningDetails: reAssignRegionDto;

}

// export class reAssignRegionDto{
//     reassigningHODFirstName: string;
//     reassigningHODLastName: string;
//     reassigningHODEmail: string;
//     reassignedRegion: number;
// }

export class MSRemarksDto {
    id: number;
    remarksDescription: string;
    remarksStatus: string;
    processBy: string;
    processName: string;
}

export class ComplaintsTaskAndAssignedDto {
    complaintAssigned: ComplaintsListDto[];
    complaintTaskID: ComplaintsListDto[];
}

export class ComplaintsListDto {
    referenceNumber: string;
    complaintTitle: string;
    targetedProducts: string;
    transactionDate: Date;
    progressStep: string;
}

export class WorkPlanScheduleListDetailsDto {
    workPlanList: WorkPlanListDto[];
    createdWorkPlan: WorkPlanBatchDetailsDto;
}

export class FuelScheduleTeamsListDetailsDto {
    fuelTeamsDto: FuelTeamsDto[];
    fuelBatchDetailsDto: FuelBatchDetailsDto;
    officersList: MsUsersDto[];
}

export class MsDashBoardALLDto {
    officerDashBoard: MsDashBoardIODto;
    hodDashBoard: MsDashBoardHODDto;
    hofDashBoard: MsDashBoardHOFDto;
    diDashBoard: MsDashBoardDIDto;
}

export class MsDashBoardIODto {
    allocatedTaskCP: number;
    allocatedTaskWP: number;
    allocatedTaskCPWP: number;
    overdueTaskCP: number;
    overdueTaskWP: number;
    overdueTaskCPWP: number;
}

export class MsDashBoardHODDto {
    selfAssigningTaskCP: number;
    assigningHOFTaskCP: number;
    reportPendingReviewCPWP: number;
    reportPendingReviewWP: number;
    overdueJuniorTaskWP: number;
    overdueJuniorTaskCPWP: number;
}

export class MsDashBoardHOFDto {
    assigningIOTaskCP: number;
    reportPendingReviewCPWP: number;
    reportPendingReviewWP: number;
    overdueJuniorTaskWP: number;
    overdueJuniorTaskCPWP: number;
}

export class MsDashBoardDIDto {
    assigningHODTaskCP: number;
    assigningHOFTaskCP: number;
    assigningIOTaskCP: number;
    reportPendingReviewCPWP: number;
    reportPendingReviewWP: number;
    overdueJuniorTaskWP: number;
    overdueJuniorTaskCPWP: number;
}

export class WorkPlanListDto {
    id: number;
    referenceNumber: string;
    nameActivity: string;
    timeActivityDate: Date;
    budget: string;
    progressStep: string;
    batchRefNumber: string;
    product: string;
}

export class CountriesEntityDto {
    id: number;
    country: string;
    status: boolean;
}

export class WorkPlanEntityDto {
    id: number;
    complaintDepartment: number;
    divisionId: number;
    nameActivity: string;
    rationale: string;
    scopeOfCoverage: string;
    timeActivityDate: Date;
    timeActivityEndDate: Date;
    region: number;
    county: number;
    townMarketCenter: number;
    locationActivityOther: string;
    standardCategory: number;
    broadProductCategory: number;
    productCategory: number;
    product: number;
    productSubCategory: number;
    resourcesRequired: PredefinedResourcesRequired[];
    budget: string;
    remarks: string;
    standardCategoryString: string;
    broadProductCategoryString: string;
    productCategoryString: string;
    productString: string;
    productSubCategoryString: string;
    workPlanCountiesTowns: WorkPlanCountyTownDto[];
}

export class WorkPlanCountyTownDto {
    id: number;
    regionId: number;
    regionName: string;
    countyId: number;
    countyName: string;
    townsId: number;
    townsName: string;
}
export class ComplaintClassificationDto {
    productClassification: number;
    broadProductCategory: number;
    productCategory: number;
    myProduct: number;
    productSubcategory: number;
    classificationRemarks: string;
    productClassificationString: string;
    broadProductCategoryString: string;
    productCategoryString: string;
    myProductString: string;
    productSubcategoryString: string;
    standardTitle: string;
    standardNumber: string;
}

export class MsComplaintAcceptanceStatusDto {
    acceptanceRemarks: string;
    ogaWhereRemarks: string;
    acceptanceStatus: boolean;
    rejectedStatus: boolean;
    mandateOGAStatus: boolean;
}

export class ComplaintDetailsDto {
    id: number;
    refNumber: string;
    complainantName: string;
    complainantEmail: string;
    complainantPhoneNumber: string;
    complainantPostalAddress: string;
    complainantPhysicalAddress: string;
    complaintIdNumber: string;
    complaintSampleDetails: string;
    remedySought: string;
    email: string;
    nameContactPerson: string;
    phoneNumber: string;
    telephoneNumber: string;
    businessAddress: string;
    complaintCategory: string;
    complaintTitle: string;
    complaintDescription: string;
    standardCategory: string;
    broadProductCategory: string;
    productCategory: string;
    productSubcategory: string;
    productName: string;
    productBrand: string;
    region: string;
    county: string;
    town: string;
    marketCenter: string;
    buildingName: string;
    Date: Date;
    status: string;
    approvedStatus: boolean;
    assignedIOStatus: boolean;
    rejectedStatus: boolean;
    classificationDetailsStatus: boolean;
    complaintFiles: ComplaintsFilesFoundDto[];
    ksApplicable: StandardDetailsDto;
    timelineStartDate: Date;
    timelineEndDate: Date;
    timelineOverDue: boolean;
    standardTitle: string;
    standardNumber: string;
}


export class DivisionDetails {
    id?: string;
    division?: string;
    descriptions?: string;
    status?: string;
    departmentId?: number;
}

// export class UserDetails {
//     id?: number;
//     firstName?: string;
//     lastName?: string;
//     userName?: string;
//     email?: string;
//     status?: boolean;
// }

export class FindWithRefNumber {
    refNumber?: string;
}

export class WorkPlanFeedBackDto {
    hodFeedBackRemarks: string;
}

export class FieldReportBackDto {
    actionOnSeizedGoodsDetails: string;
}

export class DestructionNotificationDto {
    clientFullName: string;
    clientEmail: string;
    remarks: string;
}

export class ComplaintsFilesFoundDto {
    id: number;
    fileName: string;
    documentType: string;
    fileContentType: string;
}

export class  WorkPlanFilesFoundDto {
    id: number;
    fileName: string;
    documentType: string;
    fileContentType: string;
    ordinaryStatus: number;
    isUploadFinalReport: number;
    versionNumber: number;
    createdBy: string;
    createdOn: Date;
}

export class  FuelFilesFoundDto {
    id: number;
    fileName: string;
    documentType: string;
    fileContentType: string;
}

export class StandardDetailsDto {
    standardTitle: string;
    standardNumber: string;
    ics: string;
    hsCode: string;
    subCategoryId: number;
}


export class ComplaintDto {
    // complaintCategory: number;
    complaintTitle: string;
    productBrand: string;

    productName: string;
    complaintDescription: string;
    complaintSampleDetails: string;
    remedySought: string;
}

export class AcknowledgementDto {
    referenceNumber: string;
    complaintTitle: string;
    targetedProducts: string;
    transactionDate: Date;
    approvedDate: Date;
    rejectedDate: Date;
    assignedIo: number;
    acknowledgementType: string;
    region: number;
    county: number;
    town: number;
    complaintDepartment: number;
    division: number;
    timeTakenForAcknowledgement: string;
}

export class FeedbackDto {
    referenceNumber: string;
    complaintTitle: string;
    targetedProducts: string;
    transactionDate: Date;
    approvedDate: Date;
    rejectedDate: Date;
    assignedIo: number;
    acknowledgementType: string;
    region: number;
    county: number;
    town: number;
    complaintDepartment: number;
    division: number;
    timeTakenForAcknowledgement: string;
    feedbackSent: string;
    timeTakenForFeedbackSent: string;
}

export class ReportSubmittedDto {
    referenceNumber: string;
    complaintTitle: string;
    targetedProducts: string;
    transactionDate: Date;
    approvedDate: Date;
    rejectedDate: Date;
    assignedIo: number;
    acknowledgementType: string;
    region: number;
    county: number;
    town: number;
    complaintDepartment: number;
    division: number;
    timeTakenForAcknowledgement: string;
    reportSubmited: string;
    timeTakenForReportSubmission: string;
}

export class SampleSubmittedDto {
    referenceNumber: string;
    complaintTitle: string;
    targetedProducts: string;
    transactionDate: Date;
    approvedDate: Date;
    rejectedDate: Date;
    assignedIo: number;
    acknowledgementType: string;
    region: number;
    county: number;
    town: number;
    complaintDepartment: number;
    division: number;
    timeTakenForAcknowledgement: string;
    sampleSubmittedWithBsNumber: string;
    timeTakenForSampleSubmission: string;
}

export class ComplaintCustomersDto {
    firstName: string;
    lastName: string;
    phoneNumber: string;
    emailAddress: string;
    postalAddress: string;
    physicalAddress: string;
    idNumber: string;
}

export class PreliminaryReportItemsDto {
    id: number;
    marketCenter: string;
    nameOutlet: string;
    sector: string;
    dateVisit: Date;
    numberProductsPhysicalInspected: number;
    compliancePhysicalInspection: number;
    remarks: string;
    preliminaryReportID: number;
}

export class KebsOfficersName {
        inspectorName: string;
        institution: string;
        designation: string;
}

export class PreliminaryReportDto {
    id: number;
    reportTo: string;
    reportFrom: string;
    reportSubject: string;
    reportTitle: string;
    reportDate: Date;
    surveillanceDateFrom: Date;
    surveillanceDateTo: Date;
    reportBackground: string;
    kebsOfficersName: KebsOfficersName[];
    surveillanceObjective: string;
    surveillanceConclusions: string;
    surveillanceRecommendation: string;
    remarks: string;
    parametersList: PreliminaryReportItemsDto[];
    approvedStatusHofFinal: boolean;
    rejectedStatusHofFinal: boolean;
    approvedStatus: boolean;
    rejectedStatus: boolean;
    approvedStatusHodFinal: boolean;
    rejectedStatusHodFinal: boolean;
    approvedStatusHod: boolean;
    rejectedStatusHod: boolean;
    approvedStatusDirectorFinal: boolean;
    rejectedStatusDirectorFinal: boolean;
}

export class InspectionInvestigationReportDto {
    id: number;
    reportReference: string;
    reportClassification: string;
    reportTo: string;
    reportThrough: string;
    reportFrom: string;
    reportSubject: string;
    reportTitle: string;
    reportDate: Date;
    reportRegion: string;
    reportDepartment: string;
    reportFunction: string;
    backgroundInformation: string;
    objectiveInvestigation: string;
    endDateInvestigationInspection: Date;
    startDateInvestigationInspection: Date;
    kebsInspectors: KebsOfficersName[];
    methodologyEmployed: string;
    findings: string;
    conclusion: string;
    recommendations: string;
    statusActivity: string;
    finalRemarkHod: string;
    remarks: string;
    additionalInformation: FieldReportAdditionalInfo;
    additionalInformationStatus: boolean;
    bsNumbersList: string[];
    version: number;
    createdBy: string;
    createdOn: Date;
    changesMade: string;
}

export class FieldReportAdditionalInfo {
    performanceOnTestSamples: string;
    actionOnSeizedGoods: FieldReportBackDto[];
    actionOnSeizedGoodsRemarks: string;
    actionsOnRecommendationGiven: string;
    followUpActivities: string;
    others: string;
}

export class ComplaintLocationDto {
    county: number;
    town: number;
    marketCenter: string;
    buildingName: string;
    email: string;
    nameContactPerson: string;
    phoneNumber: string;
    telephoneNumber: string;
    businessAddress: string;
}

export class ConsumerComplaintViewSearchValues {
    refNumber: string;
    assignIO: number;
    startDate: Date;
    endDate: Date;
    sectorID: number;
    outletName: string;
}

export class SubmittedSamplesSummaryViewSearchValues {
    sampleReferences: string;
    assignIO: number;
    startDate: Date;
    endDate: Date;
    sectorID: number;
}

export class SeizeViewSearchValues {
    startDate: Date;
    endDate: Date;
    sector: string;
    brand: string;
    marketCentre: string;
    nameOutlet: string;
    productsDueForDestruction: string;
    productsDueForRelease: string;
}

export class MsSeizedGoodsReportViewEntity {
    id: number;
    dateofSeizure: string;
    marketCentre: string;
    nameOutlet: string;
    descriptionProductsSeized: string;
    brand: string;
    sector: string;
    quantity: string;
    unit: string;
    estimatedCost: string;
    currentLocationSeizedProducts: string;
    productsDueForDestruction: string;
    productsDueForRelease: string;
    dateofDestructed: string;
    dateofRelease: string;
    dateSeizure: Date;
    dateDestructed: Date;
    dateRelease: Date;
}

export class SubmittedSamplesSummaryReportViewEntity {
    id: number;
    sendersDate: Date;
    dateVisit: Date;
    sampleReferences: string;
    resultsDate: Date;
    resultSentDate: Date;
    officerId: number;
    complaintDepartment: number;
    dateofVisit: string;
    sampleSubmissionDate: string;
    marketCentre: string;
    nameAddressOutlet: string;
    productDescription: string;
    sector: string;
    ucrPermitNo: string;
    sourceProductEvidence: string;
    brandAndManufacturer: string;
    noSamplesTested: string;
    natureFailure: string;
    actionsTaken: string;
    dateofTestReport: string;
    dateofForwardingTestResults: string;
    complianceTesting: string;
    tcxb: string;
    timeTakenSubmitSample: string;
    submissionWithin2Days: string;
    timeTakenForwardLetters: string;
    forwardingWithin14DaysTesting: string;
    batchNoDateManufacture: string;
    sampleCollectionDate: Date;
    failedParameters: string;
}

export class FieldInspectionSummaryReportViewEntity {
    id: number;
    inspectionDate: Date;
    marketCenter: string;
    outletDetails: string;
    complaintDepartment: number;
    officerId: number;
    totalComplianceScore: string;
    reportDate: Date;
    dateofVisit: string;
    dateofSurveillanceReport: string;
    marketCentre: string;
    nameOutlet: string;
    noSamplesDrawnSubmitted: string;
    compliancePhysicalInspection: string;
    mostRecurringNonCompliant: string;
    pcxa: string;
    sectorName: string;
    noOfSamplesPhysicallyInspected: number;
    visitAspermsSchedule: string;
    timeTakenFileSurveillanceReport: string;
    filingWithin1DayafterVisit: string;
}

export class WorkPlanMonitoringToolEntity {
    officerId: number;
    regionId: number;
    complaintDepartment: number;
    timeActivityDate: Date;
    timeActivityEndDate: Date;
    id: number;
    referenceNumber: string;
    targetedMonth: string;
    productString: string;
    officers: string;
    region: string;
    county: string;
    town: string;
    july: string;
    august: string;
    september: string;
    october: string;
    november: string;
    december: string;
    january: string;
    february: string;
    march: string;
    april: string;
    may: string;
    june: string;
}

export class ConsumerComplaintsReportViewEntity {
    id: number;
    referenceNumber: string;
    complainant: string;
    natureComplaint: string;
    sector: string;
    dateReceived: string;
    dateAcknowledged: string;
    investigatingOfficer: string;
    dateCompletionInvestigation: string;
    dateFeedbackComplainant: string;
    resolution: string;
    timeTakenAcknowledge: string;
    acknowledgedWithin2DaysReceipt: string;
    timeTakenProvideFeedback: string;
    feedbackWithin5DaysCompInvestigation: string;
    timeTakenAddressComplaint: string;
    addressedWithin28DaysReceipt: string;
    assignedIo: number;
    transactionDate: Date;
    complaintDepartment: number;
}

export class ComplaintViewSearchValues {
    refNumber: string;
    assignedIo: number;
    region: number;
    complaintDepartment: number;
    division: number;
}

export class SampleProductViewSearchValues {
    refNumber: string;
    productName: string;
    bsNumber: string;
    status: string;
    region: number;
    complaintDepartment: number;
    division: number;
}

export class SeizedGoodsViewSearchValues {
    refNumber: string;
    productName: string;
    quantity: string;
    estimatedCost: string;
    currentLocation: string;
    region: number;
    complaintDepartment: number;
    division: number;
}

export class ComplaintsInvestigationListDto {
    referenceNumber: string;
    complaintTitle: string;
    targetedProducts: string;
    transactionDate: string;
    approvedDate: string;
    rejectedDate: string;
    assignedIo: string;
    status: string;
    region: string;
    county: string;
    town: string;
    complaintDepartment: string;
    division: string;
    timeTakenForAcknowledgement: string;
    feedbackSent: string;
    timeTakenForFeedbackSent: string;
    msProcess: string;
}

export class SelectedProductViewListDto {
    referenceNumber: string;
    divisionId: string;
    complaintDepartment: string;
    region: string;
    county: string;
    townMarketCenter: string;
    nameProduct: string;
    bsNumber: string;
    resultsAnalysis: boolean;
    analysisDone: boolean;
    complianceRemarks: string;
    status: string;
}

export class SeizedGoodsViewDto {
    referenceNumber: string;
    divisionId: string;
    complaintDepartment: string;
    region: string;
    county: string;
    townMarketCenter: string;
    descriptionProductsSeized: string;
    quantity: string;
    estimatedCost: string;
    currentLocation: string;
}

export class WorkPlanBatchDetailsDto {
    id: number;
    workPlanRegion: number;
    createdDate: Date;
    createdStatus: boolean;
    endedDate: Date;
    endedStatus: boolean;
    workPlanStatus: boolean;
    referenceNumber: string;
    userCreated: string;
    yearName: string;
    batchClosed: boolean;
}

export class WorkPlanInspectionDto {
    id: number;
    productCategory: string;
    broadProductCategory: string;
    product: string;
    standardCategory: string;
    productSubCategory: string;
    divisionId: string;
    timelineStartDate: Date;
    timelineEndDate: Date;
    timelineOverDue: boolean;
    sampleSubmittedId: number;
    division: string;
    officerName: string;
    nameActivity: string;
    rationale: string;
    scopeOfCoverage: string;
    targetedProducts: string;
    resourcesRequired: PredefinedResourcesRequired[];
    budget: string;
    approvedOn: Date;
    approvedStatus: boolean;
    workPlanYearId: number;
    clientAppealed: boolean;
    directorRecommendationRemarksStatus: boolean;
    hodRecommendationStatus: boolean;
    hodRecommendationStart: boolean;
    hodRecommendation: string;
    destructionNotificationStatus: boolean;
    destructionNotificationDate: Date;
    hodRecommendationRemarks: string;
    preliminaryParamStatus: boolean;
    dataReportGoodsStatus: boolean;
    scfLabparamsStatus: boolean;
    bsNumberStatus: boolean;
    ssfLabparamsStatus: boolean;
    msPreliminaryReportStatus: boolean;
    preliminaryApprovedStatus: boolean;
    msFinalReportStatus: boolean;
    finalApprovedStatus: boolean;
    chargeSheetStatus: boolean;
    investInspectReportStatus: boolean;
    sampleCollectionStatus: boolean;
    sampleSubmittedStatus: boolean;
    seizureDeclarationStatus: boolean;
    dataReportStatus: boolean;
    approvedBy: string;
    approved: string;
    rejectedOn: Date;
    rejectedStatus: boolean;
    submittedForApprovalStatus: boolean;
    onsiteStartStatus: boolean;
    onsiteStartDate: Date;
    onsiteEndDate: Date;
    onsiteStartDateAdded: Date;
    onsiteEndDateAdded: Date;
    onsiteTat: number;
    sendSffDate: Date;
    sendSffStatus: boolean;
    onsiteEndStatus: boolean;
    destractionStatus: boolean;
    rejectedBy: string;
    rejected: string;
    msEndProcessRemarks: string;
    rejectedRemarks: string;
    approvedRemarks: string;
    progressValue: boolean;
    progressStep: string;
    county: string;
    subcounty: string;
    townMarketCenter: string;
    locationActivityOther: string;
    timeActivityDate: Date;
    timeActivityEndDate: Date;
    timeDateReportSubmitted: Date;
    timeActivityRemarks: string;
    rescheduledDateNotVisited: Date;
    rescheduledDateReportSubmitted: Date;
    rescheduledActivitiesRemarks: string;
    activityUndertakenPeriod: string;
    nameHof: string;
    reviewSupervisorDate: Date;
    reviewSupervisorRemarks: string;
    destructionClientEmail: string;
    region: string;
    complaintId: number;
    officerDetails: MsUsersDto;
    hodRmAssignedDetails: MsUsersDto;
    hofAssignedDetails: MsUsersDto;
    destructionDocId: number;
    scfDocId: number;
    ssfDocId: number;
    seizureDocId: number;
    declarationDocId: number;
    chargeSheetDocId: number;
    dataReportDocId: number;
    complaintDepartment: string;
    referenceNumber: string;
    batchDetails: WorkPlanBatchDetailsDto;
    ksApplicable: StandardDetailsDto;
    remarksDetails: MSRemarksDto[];
    workPlanFiles: WorkPlanFilesFoundDto[];
    chargeSheet: ChargeSheetDto;
    seizureDeclarationDto: SeizureListDto[];
    inspectionInvestigationDto: InspectionInvestigationReportDto;
    preliminaryReportListDto: InspectionInvestigationReportDto[];
    dataReportDto: DataReportDto[];
    sampleCollected: SampleCollectionDto;
    sampleSubmitted: SampleSubmissionDto[];
    sampleLabResults: MSSSFLabResultsDto[];
    compliantStatusAdded: boolean;
    destructionRecommended: boolean;
    finalReportGenerated: boolean;
    appealStatus: boolean;
    msProcessEndedStatus: boolean;
    preliminaryReport: PreliminaryReportDto;
    preliminaryReportFinal: PreliminaryReportDto;
    officersList: MsUsersDto[];
    hofList: MsUsersDto[];
    updateWorkPlan: WorkPlanEntityDto;
    updatedStatus: Boolean;
    resubmitStatus: Boolean;
    recommendationDoneStatus: Boolean;
    bsNumberCountAdded: number;
    analysisLabCountDone: number;
    analysisLabCountDoneAndSent: number;
    productListRecommendationAddedCount: number;
    productList: WorkPlanProductDto[];
    workPlanCountiesTowns: WorkPlanCountyTownDto[];
    batchRefNumber: string;
    totalComplianceValue: string;
    currentDate: Date;
    latestPreliminaryReport: number;
    latestFinalPreliminaryReport: number;
    complaintReferenceNumber: string;
    overAllCompliance: number;
}

export class CountryListDto {
    name: string;
    code: string;
}

export class WorkPlanProductDto {
        id: number;
        productName: string;
        referenceNo: string;
        recommendation: RecommendationDto[];
        destructionRecommended: boolean;
        hodRecommendationStatus: boolean;
        hodRecommendationRemarks: string;
        directorRecommendationStatus: boolean;
        directorRecommendationRemarks: string;
        clientAppealed: boolean;
        destructionStatus: boolean;
        appealStatus: boolean;
        destructionNotificationStatus: boolean;
        destructionNotificationDocId: number;
        workPlanId: number;
        ssfId: number;
        destructionClientEmail: string;
        destructionClientFullName: string;
        destructionNotificationDate: Date;
        destructionDocId: number;
        destructedStatus: boolean;
}


export class SeizureDeclarationDto {
    id: number;
    seizureTo: string;
    seizurePremises: string;
    seizureRequirementsStandards: string;
    goodsName: string;
    goodsManufactureTrader: string;
    goodsAddress: string;
    goodsPhysical: string;
    goodsLocation: string;
    goodsMarkedBranded: string;
    goodsPhysicalSeal: string;
    descriptionGoods: string;
    goodsQuantity: string;
    goodsThereforei: string;
    nameInspector: string;
    designationInspector: string;
    dateInspector: Date;
    nameManufactureTrader: string;
    designationManufactureTrader: string;
    dateManufactureTrader: Date;
    nameWitness: string;
    designationWitness: string;
    dateWitness: Date;
    declarationTakenBy: string;
    declarationOnThe: string;
    declarationDayOf: Date;
    declarationMyName: string;
    declarationIresideAt: string;
    declarationIemployeedAs: string;
    declarationIemployeedOf: string;
    declarationSituatedAt: string;
    declarationStateThat: string;
    declarationIdNumber: string;
    remarks: string;
}

//
// export class SeizureListDto {
//     seizureList: SeizureDto[];
// }

export class LaboratoryEntityDto {
        id: number;
        labName: string;
        description: string;
        status: boolean;
}

export class WorkPlanScheduleOnsiteDto {
        startDate: Date;
        endDate: Date;
        remarks: string;
}

export class SeizureListDto {
    id: number;
    docID: number;
    marketTownCenter: string;
    productField: string;
    serialNumber: string;
    nameOfOutlet: string;
    nameSeizingOfficer: string;
    additionalOutletDetails: string;
    seizureList: SeizureDto[];
}

export class SeizureDto {
    id: number;
    docID: number;
    mainSeizureID: number;
    marketTownCenter: string;
    nameOfOutlet: string;
    descriptionProductsSeized: string;
    brand: string;
    product: string;
    sector: string;
    reasonSeizure: string;
    nameSeizingOfficer: string;
    seizureSerial: string;
    quantity: string;
    unit: string;
    estimatedCost: string;
    currentLocation: string;
    productsDestruction: string;
    additionalOutletDetails: string;
    remarks: string;
}

export class DataReportDto {
    id: number;
    referenceNumber: string;
    inspectorName: string;
    inspectionDate: Date;
    function: string;
    department: string;
    regionName: string;
    county: string;
    town: string;
    marketCenter: string;
    outletName: string;
    physicalLocation: string;
    emailAddress: string;
    phoneNumber: string;
    outletDetails: string;
    mostRecurringNonCompliant: string;
    personMet: string;
    summaryFindingsActionsTaken: string;
    samplesDrawnAndSubmitted: string;
    sourceOfProductAndEvidence: string;
    finalActionSeizedGoods: string;
    totalComplianceScore: number;
    remarks: string;
    productsList: DataReportParamsDto[];
    docList: number[];
}




export class DataReportParamsDto {
    id: number;
    productName: string;
    typeBrandName: string;
    localImport: string;
    permitNumber: string;
    ucrNumber: string;
    complianceInspectionParameter: number;
    measurementsResults: string;
    remarks: string;
}

export class DataInspectorInvestDto {
    inspectorName: string;
    institution: string;
    designation: string;
}

export class BSNumberDto {
    bsNumber: string;
}

export class TeamsFuelSaveDto {
    teamName: string;
    assignedOfficerID: number;
    startDate: Date;
    endDate: Date;
    remarks: string;
    countList: TeamsCountyFuelSaveDto[];
}

export class TeamsCountyFuelSaveDto {
    countyId: number;
    remarks: string;
}

export class FuelBatchDetailsDto {
    id: number;
    region: string;
    county: string;
    town: string;
    referenceNumber: string;
    batchFileYear: string;
    batchFileMonth: string;
    remarks: string;
    batchClosed: boolean;
}

export class LaboratoryDto {
    id: number;
    labName: string;
    description: string;
    status: boolean;
}

export class FuelEntityDto {
    company: string;
    petroleumProduct: string;
    physicalLocation: string;
    inspectionDateFrom: Date;
    inspectionDateTo: Date;
    stationOwnerEmail: string;
    stationKraPin: string;
    townID: string;
    remarks: string;
}

export class BatchFileFuelSaveDto {
    county: bigint;
    town: bigint;
    remarks: string;
}

export class FuelEntityAssignOfficerDto {
    assignedUserID: bigint;
    remarks: string;
}

export class FuelScheduleCountyListDetailsDto {
    fuelCountyDtoList: FuelCountyDto[];
    fuelTeamsDto: TeamsFuelDetailsDto;
    fuelBatchDetailsDto: FuelBatchDetailsDto;
}

export class FuelCountyDto {
    id: bigint;
    referenceNumber: string;
    countyName: string;
}

export class TeamsFuelDetailsDto {
    id: bigint;
    referenceNumber: string;
    teamName: string;
    startDate: Date;
    endDate: Date;
    countyName: string;
    countyID: bigint;
}

export class FuelTeamsDto {
    id: bigint;
    referenceNumber: string;
    teamName: string;
    startDate: Date;
    endDate: Date;
    officerAssignedName: string;
}

export class WorkPlanFinalRecommendationDto {
    recommendationId: RecommendationDto[];
    hodRecommendationRemarks: string;
}

export class MsRecommendationDto {
    id: number;
    recommendationName: string;
    description: string;
    status: boolean;
}

export class WorkPlanScheduleApprovalDto {
    approvalStatus: boolean;
    remarks: string;
}

export class ApprovalDto {
    approvalStatus: boolean;
    remarks: string;
}

export class PreliminaryReportFinal {
    surveillanceConclusions: string;
    surveillanceRecommendation: string;
    remarks: string;
}

export class ChargeSheetDto {
    id: number;
    christianName: string;
    surname: string;
    sex: string;
    nationality: string;
    age: number;
    addressDistrict: string;
    addressLocation: string;
    firstCount: string;
    particularsOffenceOne: string;
    secondCount: string;
    particularsOffenceSecond: string;
    dateArrest: Date;
    withWarrant: string;
    applicationMadeSummonsSue: string;
    dateApprehensionCourt: Date;
    bondBailAmount: number;
    remandedAdjourned: string;
    complainantName: string;
    complainantAddress: string;
    prosecutor: string;
    witnesses: string;
    sentence: string;
    finePaid: string;
    courtName: string;
    courtDate: Date;
    remarks: string;
}

export class ComplaintAssignDto {
    assignedRemarks: string;
    assignedIo: bigint;
}

export class RegionReAssignDto {
    reassignedRemarks: string;
    regionID: bigint;
    countyID: bigint;
    townID: bigint;
}

export class CompliantRemediationDto {
    proFormaInvoiceStatus: boolean;
    dateOfRemediation: Date;
    remarks: string;
    volumeFuelRemediated: number;
    subsistenceTotalNights: number;
    subsistenceTotalNightsRate: number;
    transportAirTicket: number;
    transportInkm: number;
}

export class RemediationDto {
    productType: string;
    quantityOfFuel: string;
    contaminatedFuelType: string;
    applicableKenyaStandard: string;
    remediationProcedure: string;
    volumeOfProductContaminated: string;
    volumeAdded: string;
    totalVolume: string;
}


export class FuelEntityRapidTestDto {
    rapidTestRemarks: string;
    rapidTestStatus: boolean;
}

export class RapidTestProductsDto {
    id: bigint;
    productName: string;
    exportMarkerTest: string;
    domesticKeroseneMarkerTest: string;
    sulphurMarkerTest: string;
    exportMarkerTestStatus: number;
    domesticKeroseneMarkerTestStatus: number;
    sulphurMarkerTestStatus: number;
    overallComplianceStatus: number;
}

export class RapidTestProductsDetailsDto {
    id: bigint;
    productName: string;
    sampleSize: string;
    batchSize: string;
    batchNumber: string;
    sulphurMarkerTest: string;
    exportMarkerTestStatus: boolean;
    domesticKeroseneMarkerTestStatus: boolean;
    sulphurMarkerTestStatus: boolean;
    overallComplianceStatus: boolean;
}

export class FuelInspectionScheduleListDetailsDto {
    fuelInspectionDto: FuelInspectionDto[];
    fuelBatchDetailsDto: FuelBatchDetailsDto;
    fuelTeamsDto: TeamsFuelDetailsDto;
}

export class FuelInspectionDto {
    id: number;
    timelineStartDate: Date;
    timelineEndDate: Date;
    timelineOverDue: boolean;
    referenceNumber: string;
    company: string;
    townName: string;
    petroleumProduct: string;
    physicalLocation: string;
    inspectionDateFrom: Date;
    inspectionDateTo: Date;
    processStage: string;
    assignedOfficerStatus: boolean;
    rapidTestDone: boolean;
    sampleCollectionStatus: boolean;
    scfUploadId: bigint;
    sampleSubmittedStatus: boolean;
    bsNumberStatus: boolean;
    fuelCompliantStatus: boolean;
    fuelReportId: bigint;
    invoiceDocFile: bigint;
    compliantStatusAdded: boolean;
    remediationScheduledStatus: boolean;
    remendiationCompleteStatus: boolean;
    proFormaInvoiceStatus: boolean;
    endInspectionStatus: boolean;
    batchDetails: FuelBatchDetailsDto;
    teamsDetails: TeamsFuelDetailsDto;
    officersList: MsUsersDto[];
    remarksDetails: MSRemarksDto[];
    officersAssigned: MsUsersDto;
    rapidTest: FuelEntityRapidTestDto;
    rapidTestProducts: RapidTestProductsDetailsDto[];
    fuelUploadedFiles: FuelFilesFoundDto[];
    sampleCollected: SampleCollectionDto;
    sampleSubmitted: SampleSubmissionDto[];
    sampleLabResults: MSSSFLabResultsDto[];
    fuelRemediation: FuelRemediationDto;
    ssfCountAdded: number;
    bsNumberCountAdded: number;
    analysisLabCountDone: number;
    quotationGeneratedStatus: boolean;
}

export class MSSSFLabResultsDto {
    ssfResultsList: MSSSFComplianceStatusDetailsDto;
    savedPDFFiles:  MSSSFPDFListDetailsDto[];
    limsPDFFiles: LIMSFilesFoundDto[];
    parametersListTested: LabResultsParamDto[];
}

export class MSSSFComplianceStatusDetailsDto {
    sffId: number;
    bsNumber: string;
    complianceRemarks: string;
    complianceStatus: boolean;
    analysisDone: boolean;
    resultsSent: boolean;
}

export class SSFSendingComplianceStatus {
    ssfID: number;
    failedParameters: string;
    outLetEmail: string;
    manufactureEmail: string;
    complainantEmail: string;
}

export class FuelRemediationDto {
    productType: string;
    applicableKenyaStandard: string;
    remediationProcedure: string;
    volumeOfProductContaminated: string;
    contaminatedFuelType: string;
    quantityOfFuel: string;
    volumeAdded: string;
    totalVolume: string;
    proFormaInvoiceStatus: boolean;
    proFormaInvoiceNo: string;
    invoiceAmount: string;
    payableAmount: string;
    feePaidReceiptNo: string;
    dateOfRemediation: Date;
    dateOfPayment: Date;
    invoiceCreated: boolean;
}

export class MSSSFPDFListDetailsDto {
    pdfSavedId: number;
    pdfName: string;
    sffId: number;
    complianceRemarks: string;
    complianceStatus: boolean;
    id: number;
}

export class EndFuelDto {
    remarks: string;
}

export class LIMSFilesFoundDto {
    fileSavedStatus: boolean;
    fileName: string;
}

export class PDFSaveComplianceStatusDto {
    ssfID: number;
    bsNumber: string;
    PDFFileName: string;
    complianceStatus: boolean;
    complianceRemarks: string;
}

export class SSFSaveComplianceStatusDto {
    ssfID: number;
    bsNumber: string;
    failedParameters: string;
    complianceStatus: boolean;
    complianceRemarks: string;
    totalCompliance: string;
}

export class SSFSaveFinalComplianceStatusDto {
    ssfID: number;
    bsNumber: string;
    complianceStatus: boolean;
    complianceRemarks: string;
    totalCompliance: string;
}

export class LabResultsParamDto {
    param: string;
    result: string;
    method: string;
}

export class SampleSubmissionDto {
    id: number;
    nameProduct: string;
    packaging: string;
    labellingIdentification: string;
    fileRefNumber: string;
    referencesStandards: string;
    standardsArray: string[];
    sizeTestSample: string;
    sizeRefSample: string;
    condition: string;
    sampleReferences: string;
    sendersName: string;
    designation: string;
    address: string;
    sendersDate: Date;
    receiversDate: Date;
    receiversName: string;
    testChargesKsh: number;
    receiptLpoNumber: string;
    invoiceNumber: string;
    disposal: string;
    remarks: string;
    countryOfOrigin: string;
    sampleCollectionNumber: number;
    sampleCollectionProduct: number;
    lbIdAnyAomarking: string;
    lbIdBatchNo: string;
    lbIdContDecl: string;
    lbIdDateOfManf: Date;
    sampleCollectionDate: Date;
    lbIdExpiryDate: Date;
    lbIdTradeMark: string;
    noteTransResults: string;
    scfNo: string;
    cocNumber: string;
    bsNumber: string;
    productDescription: string;
    sourceProductEvidence: string;
    parametersList: SampleSubmissionItemsDto[];
    dataReportID: number;
    nameOutlet: string;
}

export class SampleSubmissionItemsDto {
    id: number;
    parameters: string;
    laboratoryName: string;
}

export class BSNumberSaveDto {
    bsNumber: string;
    submittedDate: Date;
    ssfID: number;
    remarks: string;
}

export class SampleCollectionDto {
    id: string;
    nameManufacturerTrader: string;
    addressManufacturerTrader: string;
    samplingMethod: string;
    reasonsCollectingSamples: string;
    anyRemarks: string;
    designationOfficerCollectingSample: string;
    nameOfficerCollectingSample: string;
    dateOfficerCollectingSample: Date;
    nameWitness: string;
    designationWitness: string;
    dateWitness: Date;
    productsList: SampleCollectionItemsDto[];
}

export class SampleCollectionItemsDto {
    id: number;
    productBrandName: string;
    batchNo: string;
    batchSize: string;
    sampleSize: string;
    ssfAdded: boolean;
}

export class MsUsersDto {
    id: number;
    firstName: string;
    lastName: string;
    userName: string;
    email: string;
    phoneNumber: string;
    status: boolean;
}

