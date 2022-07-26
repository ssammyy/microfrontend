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
}

export class ComplaintAdviceRejectDto {
    mandateForOga: number;
    advisedWhereToRemarks: string;
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
}

export class MSRemarksDto {
    id: number;
    remarksDescription: string;
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

export class WorkPlanListDto {
    id: number;
    referenceNumber: string;
    nameActivity: string;
    timeActivityDate: Date;
    budget: string;
    progressStep: string;
}

export class CountriesEntityDto {
    id: number;
    country: string;
    status: boolean;
}

export class WorkPlanEntityDto {
    complaintDepartment: number;
    divisionId: number;
    nameActivity: string;
    timeActivityDate: Date;
    county: number;
    townMarketCenter: number;
    locationActivityOther: string;
    standardCategory: number;
    broadProductCategory: number;
    productCategory: number;
    product: number;
    productSubCategory: number;
    resourcesRequired: string;
    budget: string;
    remarks: string;
}

// export class ComplaintsListDto {
//     referenceNumber: string;
//     complaintTitle: string;
//     targetedProducts: string;
//     transactionDate: Date;
//     progressStep: string;
// }
export class ComplaintClassificationDto {
    productClassification: number;
    broadProductCategory: number;
    productCategory: number;
    myProduct: number;
    productSubcategory: number;
    classificationRemarks: string;
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
    complaintCategory: string;
    complaintTitle: string;
    complaintDescription: string;
    standardCategory: string;
    broadProductCategory: string;
    productCategory: string;
    productSubcategory: string;
    productName: string;
    productBrand: string;
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

export class DestructionNotificationDto {
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
    // productClassification: number;
    // broadProductCategory: number;
    // productCategory: number;
    // myProduct: number;
    // productSubcategory: number;
    productBrand: string;
    complaintDescription: string;
}

export class ComplaintCustomersDto {
    firstName: string;
    lastName: string;
    phoneNumber: string;
    emailAddress: string;
    postalAddress: string;
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
}

export class InspectionInvestigationReportDto {
    id: number;
    reportReference: string;
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
    dateInvestigationInspection: Date;
    kebsInspectors: KebsOfficersName[];
    methodologyEmployed: string;
    conclusion: string;
    recommendations: string;
    statusActivity: string;
    finalRemarkHod: string;
    // remarks: string;
}

export class ComplaintLocationDto {
    county: number;
    town: number;
    marketCenter: string;
    buildingName: string;
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
    sampleSubmittedId: number;
    division: string;
    officerName: string;
    nameActivity: string;
    targetedProducts: string;
    resourcesRequired: string;
    budget: string;
    approvedOn: Date;
    approvedStatus: boolean;
    workPlanYearId: number;
    clientAppealed: boolean;
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
    onsiteStartStatus: boolean;
    onsiteStartDate: Date;
    onsiteEndDate: Date;
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
    seizureDeclarationDto: SeizureDeclarationDto;
    inspectionInvestigationDto: InspectionInvestigationReportDto;
    dataReportDto: DataReportDto;
    sampleCollected: SampleCollectionDto;
    sampleSubmitted: SampleSubmissionDto;
    sampleLabResults: MSSSFLabResultsDto;
    compliantStatusAdded: boolean;
    destructionRecommended: boolean;
    finalReportGenerated: boolean;
    appealStatus: boolean;
    msProcessEndedStatus: boolean;
    preliminaryReport: PreliminaryReportDto;
    officersList: MsUsersDto[];
    hofList: MsUsersDto[];
}

export class CountryListDto {
    name: string;
    code: string;
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

export class DataReportDto {
    id: number;
    referenceNumber: string;
    inspectorName: string;
    inspectionDate: Date;
    function: string;
    department: string;
    regionName: string;
    town: string;
    marketCenter: string;
    outletDetails: string;
    personMet: string;
    summaryFindingsActionsTaken: string;
    finalActionSeizedGoods: string;
    remarks: string;
    productsList: DataReportParamsDto[];
}


export class DataReportParamsDto {
    id: number;
    typeBrandName: string;
    localImport: string;
    complianceInspectionParameter: number;
    measurementsResults: string;
    remarks: string;
}

export class DataInspectorInvestDto {
    inspectorName: string;
    institution: string;
    designation: string;
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

export class WorkPlanFinalRecommendationDto {
    recommendationId: bigint;
    hodRecommendationRemarks: string;
}

export class MsRecommendationDto {
    id: bigint;
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

export class CompliantRemediationDto {
    proFormaInvoiceStatus: boolean;
    dateOfRemediation: Date;
    remarks: string;
    volumeFuelRemediated: number;
    subsistenceTotalNights: number;
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

export class FuelInspectionScheduleListDetailsDto {
    fuelInspectionDto: FuelInspectionDto[];
    fuelBatchDetailsDto: FuelBatchDetailsDto;
}

export class FuelInspectionDto {
    id: number;
    referenceNumber: string;
    company: string;
    petroleumProduct: string;
    physicalLocation: string;
    inspectionDateFrom: Date;
    inspectionDateTo: Date;
    processStage: string;
    assignedOfficerStatus: boolean;
    rapidTestDone: boolean;
    sampleCollectionStatus: boolean;
    sampleSubmittedStatus: boolean;
    bsNumberStatus: boolean;
    compliantStatusAdded: boolean;
    remediationScheduledStatus: boolean;
    remendiationCompleteStatus: boolean;
    proFormaInvoiceStatus: boolean;
    endInspectionStatus: boolean;
    batchDetails: FuelBatchDetailsDto;
    officersList: MsUsersDto[];
    remarksDetails: MSRemarksDto[];
    officersAssigned: MsUsersDto;
    rapidTest: FuelEntityRapidTestDto;
    sampleCollected: SampleCollectionDto;
    sampleSubmitted: SampleSubmissionDto;
    sampleLabResults: MSSSFLabResultsDto;
    fuelRemediation: FuelRemediationDto;
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
    complianceStatus: boolean;
    complianceRemarks: string;
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
    sizeTestSample: number;
    sizeRefSample: number;
    condition: string;
    sampleReferences: string;
    sendersName: string;
    designation: string;
    address: string;
    sendersDate: Date;
    receiversName: string;
    testChargesKsh: number;
    receiptLpoNumber: string;
    invoiceNumber: string;
    disposal: string;
    remarks: string;
    sampleCollectionNumber: number;
    bsNumber: string;
    parametersList: SampleSubmissionItemsDto[];
}

export class SampleSubmissionItemsDto {
    parameters: string;
    laboratoryName: string;
}

export class BSNumberSaveDto {
    bsNumber: string;
    submittedDate: Date;
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

