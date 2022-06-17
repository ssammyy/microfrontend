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
    acceptanceDone: Boolean;
    acceptanceResults: MsComplaintAcceptanceStatusDto;
    officersList: MsUsersDto[];
    hofList: MsUsersDto[];
    officersAssigned: MsUsersDto;
    hofAssigned: MsUsersDto;
    remarksDetails: MSRemarksDto[];
    sampleCollected: SampleCollectionDto;
    sampleSubmitted: SampleSubmissionDto;
    sampleLabResults: MSSSFLabResultsDto;
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

export class ComplaintsFilesFoundDto {
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
    department: string;
    divisionId: number;
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
    county: number;
    subcounty: string;
    townMarketCenter: number;
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
    region: number;
    complaintId: number;
    officerId: number;
    destructionDocId: number;
    complaintDepartment: number;
    referenceNumber: string;
    batchDetails: WorkPlanBatchDetailsDto;
    ksApplicable: StandardDetailsDto;
    remarksDetails: MSRemarksDto[];
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
    status: boolean;
}

