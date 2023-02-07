import {SectionsEntityDto, StandardsDto} from '../master/master.model';
import {UserEntityDto} from '../users';

export class Qa {
}

export class UploadsDtoSTA3 {
    uploadedFiles: File[];
    sta3Status: boolean;
}

export class QRCodeScannedQADto {
    productName: string;
    tradeMark: string;
    awardedPermitNumber: string;
    dateOfIssue: Date;
    dateOfExpiry: Date;
}

export class CompanyTurnOverUpdateDto {
    companyProfileID: bigint;
    selectedFirmTypeID: bigint;
}

export class FirmTypeEntityDto {
    id: bigint;
    min: bigint;
    max: bigint;
    firmFee: bigint;
    productFee: bigint;
    extraProductFee: bigint;
    countBeforeFee: bigint;
    countBeforeFree: bigint;
    validity: bigint;
    invoiceDesc: string;
    firmType: string;
}

export class PermitEntityDto {
    id: bigint;
    firmName: string;
    permitRefNumber: string;
    productName: string;
    tradeMark: string;
    awardedPermitNumber: string;
    dateOfIssue: Date;
    dateOfExpiry: Date;
    permitStatus: string;
    userId: bigint;
    createdOn: string;
    county: string;
    town: string;
    region: string;
    divisionValue: string;
    sectionValue: string;
    permitAwardStatus: boolean;
    permitExpiredStatus: boolean;
    taskID: bigint;
    companyId: bigint;
    permitType: bigint;
    processStatusID: number;
    versionNumber: number;
}

export class MyTasksPermitEntityDto {
    id: bigint;
    firmName: string;
    permitRefNumber: string;
    productName: string;
    tradeMark: string;
    awardedPermitNumber: string;
    dateOfIssue: Date;
    dateOfExpiry: Date;
    permitStatus: string;
    userId: bigint;
    createdOn: Date;
    county: string;
    town: string;
    region: string;
    divisionValue: string;
    sectionValue: string;
    permitAwardStatus: Boolean;
    permitExpiredStatus: Boolean;
    taskID: bigint;
    companyId: bigint;
    permitType: bigint;
    processStatusID: bigint;
    versionNumber: bigint;
    encryptedPermitId: string;
    encryptedUserId: string;
}

export class ReportsPermitEntityDto {
    id: bigint;
    firmName: string;
    permitRefNumber: string;
    productName: string;
    tradeMark: string;
    awardedPermitNumber: string;
    dateOfIssue: Date;
    dateOfExpiry: Date;
    permitStatus: string;
    userId: bigint;
    createdOn: string;
    county: string;
    town: string;
    region: string;
    divisionValue: string;
    sectionValue: string;
    permitAwardStatus: boolean;
    permitExpiredStatus: boolean;
    taskID: bigint;
    companyId: bigint;
    permitType: bigint;
    processStatusID: number;
    versionNumber: number;
    firmTypeID: number
    firmTypeName: string;
    invoiceAmount: number;
    standardNumber: number;
    standardTitle: string;
    physicalAddress: string;
    telephoneNo: string;
    email: string;
    inspectionDate: Date;
    pscApprovalDate: Date;


}

export class ConsolidatedInvoiceDto {
    id: bigint;
    invoiceNumber: string;
    totalAmount: string;
    paidDate: Date;
    paidStatus: number;
    submittedStatus: number;
    receiptNo: string;
    checkedValue: boolean;
    sageInvoiceNumber: string;

}

export class RemarksAndStatusDto {
    remarksStatus: boolean;
    remarksValue: string;
}

export class PermitAllRemarksDetailsDto {
    hofQamCompleteness: RemarksAndStatusDto;
    labResultsCompleteness: RemarksAndStatusDto;
    pcmApproval: RemarksAndStatusDto;
    pscMemberApproval: RemarksAndStatusDto;
    pcmReviewApproval: RemarksAndStatusDto;
    justificationReport: RemarksAndStatusDto;
}

export class GenerateInvoiceDto {
    batchID: number;
    plantID: number;
    permitRefNumber: string;
    permitInvoicesID: number[];
}

export class GenerateInvoiceWithWithholdingDto {
    batchID: number;
    plantID: number;
    permitRefNumber: string;
    permitInvoicesID: number[];
    isWithHolding: number;
}

export class SSFPDFListDetailsDto {
    pdfSavedId: bigint;
    pdfName: string;
    sffId: bigint;
    complianceRemarks: string;
    complianceStatus: boolean;
}

export class PermitSSFLabResultsDto {
    ssfResultsList: SSFComplianceStatusDetailsDto[];
    labResultsList: SSFPDFListDetailsDto[];
}

export class SSFComplianceStatusDetailsDto {
    sffId: bigint;
    bsNumber: string;
    complianceRemarks: string;
    complianceStatus: boolean;
}

export class MPesaPushDto {
    entityValueID: bigint;
    phoneNumber: string;
}

export class PermitDto {
    permitId: string;
    permitIdBeingMigrated: string;
}


export class STA1 {
    id: bigint;
    commodityDescription: string;
    tradeMark: string;
    applicantName: string;
    sectionId: bigint;
    permitForeignStatus: number;
    attachedPlant: bigint;
    createFmark: number;
}

export class FmarkEntityDto {
    smarkPermitID: bigint;
}


export class PermitProcessStepDto {
    permitID: bigint;
    processStep: number;
}

export class SSCApprovalRejectionDto {
    approvedRejectedScheme: number;
    approvedRejectedSchemeRemarks: string;
}

export class ResubmitApplicationDto {
    resubmitRemarks: string;
    resubmittedDetails: string;
}


export class PermitEntityDetails {
    id: bigint;
    permitNumber: string;
    permitRefNumber: string;
    firmName: string;
    postalAddress: string;
    physicalAddress: string;
    contactPerson: string;
    telephoneNo: string;
    regionPlantValue: string;
    countyPlantValue: string;
    townPlantValue: string;
    location: string;
    street: string;
    buildingName: string;
    nearestLandMark: string;
    faxNo: string;
    plotNo: string;
    email: string;
    createdOn: Date;
    dateOfIssue: Date;
    dateOfExpiry: Date;
    commodityDescription: string;
    brandName: string;
    standardNumber: string;
    standardTitle: string;
    permitForeignStatus: boolean;
    assignOfficer: string;
    assignAssessor: string;
    divisionValue: string;
    sectionValue: string;
    inspectionDate: Date;
    inspectionScheduledStatus: boolean;
    assessmentDate: Date;
    assessmentScheduledStatus: boolean;
    processStatusName: string;
    versionNumber: bigint;
    fmarkGenerated: boolean;
    recommendationRemarks: string;
    factoryVisit: Date;
    firmTypeID: bigint;
    firmTypeName: string;
    permitTypeName: string;
    permitTypeID: number;
    permitAwardStatus: boolean;
    invoiceGenerated: boolean;
    approvedRejectedScheme: boolean;
    sendForPcmReview: boolean;
    sendApplication: boolean;
    pcmReviewApprove: boolean;
    hofQamCompletenessStatus: boolean;
    generateSchemeStatus: boolean;
    resubmitApplicationStatus: boolean;
    processStep: number;
    processStatusID: number;
    fmarkGeneratedID: number;
    oldPermitStatus: number;
    varField7: string;
    productStandards: number;
    assignOfficerStatus: boolean;
    assignOfficerID: number;
    permitGenerateDifference: boolean;
    companyId: number;
}


export class STA3 {
    id: bigint;
    produceOrdersOrStock: string;
    issueWorkOrderOrEquivalent: string;
    identifyBatchAsSeparate: string;
    productsContainersCarryWorksOrder: string;
    isolatedCaseDoubtfulQuality: string;
    headQaQualificationsTraining: string;
    reportingTo: string;
    separateQcid: string;
    testsRelevantStandard: string;
    spoComingMaterials: string;
    spoProcessOperations: string;
    spoFinalProducts: string;
    monitoredQcs: string;
    qauditChecksCarried: string;
    informationQcso: string;
    mainMaterialsPurchasedSpecification: string;
    adoptedReceiptMaterials: string;
    storageFacilitiesExist: string;
    stepsManufacture: string;
    maintenanceSystem: string;
    qcsSupplement: string;
    qmInstructions: string;
    testEquipmentUsed: string;
    indicateExternalArrangement: string;
    levelDefectivesFound: string;
    levelClaimsComplaints: string;
    independentTests: string;
    indicateStageManufacture: string;
    sta3FilesList: FilesListDto[];
}

export class SectionDto {
    id: number;
    section: string;
    divisionId: bigint;
    descriptions: string;
    status: boolean;
}

export class RegionDto {
    id: number;
    region: string;
    descriptions: string;
    status: boolean;
}


export class FilesListDto {
    id: bigint;
    name: string;
    fileType: string;
    documentType: string;
    versionNumber: number;
    document: Blob;
}

export class InvoicePerDetailsDto {
    id: bigint;
    itemDescName: string;
    itemAmount: any;
    inspectionStatus: boolean;
    permitStatus: boolean;
    fmarkStatus: boolean;
}

export class InvoiceDetailsDto {
    invoiceMasterId: bigint;
    invoiceRef: string;
    description: string;
    taxAmount: any;
    subTotalBeforeTax: any;
    totalAmount: any;
    invoiceDetailsList: InvoicePerDetailsDto[];
}

export class AllPermitDetailsDto {
    permitDetails: PermitEntityDetails;
    remarksDetails: PermitAllRemarksDetailsDto;
    invoiceDetails: InvoiceDetailsDto;
    invoiceDifferenceDetails: InvoiceDetailsDto;
    officerList: UserEntityDto[];
    oldVersionList: PermitEntityDto[];
    ordinaryFilesList: FilesListDto[];
    sta3FilesList: FilesListDto[];
    sta10FilesList: FilesListDto[];
    labResultsList: PermitSSFLabResultsDto;
    schemeOfSuperVision: FilesListDto;
    batchID: bigint;
    batchIDDifference: bigint;
    sta10DTO: AllSTA10DetailsDto;
    sta1DTO: STA1;
    sta3DTO: STA3;
    sectionList: SectionsEntityDto[];
    standardsList: StandardsDto[];
}



export class AllSTA10DetailsDto {
    sta10FirmDetails: Sta10Dto;
    sta10PersonnelDetails: STA10PersonnelDto[];
    sta10ProductsManufactureDetails: STA10ProductsManufactureDto[];
    sta10RawMaterialsDetails: STA10RawMaterialsDto[];
    sta10MachineryAndPlantDetails: STA10MachineryAndPlantDto[];
    sta10ManufacturingProcessDetails: STA10ManufacturingProcessDto[];
    sta10FilesList: FilesListDto[];
}

export class PlantDetailsDto {
    id: bigint;
    companyProfileId: bigint;
    county: string;
    town: string;
    location: string;
    street: string;
    buildingName: string;
    branchName: string;
    nearestLandMark: string;
    postalAddress: string;
    telephone: string;
    emailAddress: string;
    physicalAddress: string;
    faxNo: string;
    plotNo: string;
    designation: string;
    contactPerson: string;
}

export class StgInvoiceBalanceDto {
    batchID: bigint;
    balance: any;
}


export class InvoiceDto {
    batchID: bigint;
    firmName: string;
    postalAddress: string;
    physicalAddress: string;
    contactPerson: string;
    telephoneNo: string;
    email: string;
    invoiceNumber: string;
    receiptNo: string;
    paidDate: Date;
    totalAmount: any;
    paidStatus: any;
    submittedStatus: boolean;
    plantId: bigint;
    sageInvoiceNumber: string;
}


export class PermitInvoiceDto {
    permitID: bigint;
    invoiceNumber: string;
    commodityDescription: string;
    brandName: string;
    totalAmount: any;
    paidStatus: boolean;
    permitRefNumber: string;
    batchID: bigint;
    sageInvoiceNumber: string;

}

export class AllBatchInvoiceDetailsDto {
    batchDetails: InvoiceDto;
    allRelatedBatchInvoices: PermitInvoiceDto[];
    sageInvoiceNumber: string;

}

export class MPesaMessageDto {
    message: string;
}

export class TaskDto {
    permitId: bigint;
    taskName: string;
    taskCreateTime: Date;
    permitRefNo: string;
}

export class Sta10Dto {
    id: bigint;
    firmName: string;
    statusCompanyBusinessRegistration: string;
    ownerNameProprietorDirector: string;
    postalAddress: string;
    contactPerson: string;
    telephone: string;
    emailAddress: string;
    physicalLocationMap: string;
    county: number;
    town: number;
    totalNumberFemale: string;
    totalNumberMale: string;
    totalNumberPermanentEmployees: string;
    totalNumberCasualEmployees: string;
    averageVolumeProductionMonth: string;

    handledManufacturingProcessRawMaterials: string;
    handledManufacturingProcessInprocessProducts: string;
    handledManufacturingProcessFinalProduct: string;
    strategyInplaceRecallingProducts: string;
    stateFacilityConditionsRawMaterials: string;
    stateFacilityConditionsEndProduct: string;
    testingFacilitiesExistSpecifyEquipment: string;
    testingFacilitiesExistStateParametersTested: string;
    testingFacilitiesSpecifyParametersTested: string;
    calibrationEquipmentLastCalibrated: string;
    handlingConsumerComplaints: string;
    companyRepresentative: string;
    applicationDate: string;
}

export class STA10ProductsManufactureDto {
    id: bigint;
    productName: string;
    productBrand: string;
    productStandardNumber: string;
    available: boolean;
    permitNo: string;
}

export class STA10RawMaterialsDto {
    id: bigint;
    name: string;
    origin: string;
    specifications: string;
    qualityChecksTestingRecords: string;
}

export class STA10PersonnelDto {
    id: bigint;
    personnelName: string;
    qualificationInstitution: string;
    dateOfEmployment: Date;
}

export class STA10MachineryAndPlantDto {
    id: bigint;
    machineName: string;
    typeModel: string;
    countryOfOrigin: string;
}

export class STA10ManufacturingProcessDto {
    id: bigint;
    processFlowOfProduction: string;
    operations: string;
    criticalProcessParametersMonitored: string;
    frequency: string;
    processMonitoringRecords: string;
}

export interface Payments {
    id: number;
    invoiceRef: string;
    userId: number;
    permitRefNumber: string;
    receiptNo: string;
    batchInvoiceNo: number;
    paymentStatus: number;
    generatedDate: string;
    itemCount: number;
    totalAmount: number;
    taxAmount: number;
    subTotalBeforeTax: number;
    permitId: number;
}

export interface SamplesSubmittedDto {
    ssf_SUBMISSION_DATE: string;
    firm_NAME: string;
    physical_ADDRESS: string;
    telephone_NO: string;
    brand_NAME: string;
    standard_TITLE: string;
    compliance_REMARKS: string;
    compliant_STATUS: number;
    inspection_DATE: string;
    results_DATE: string;
    region: string;
    section: string;
    product: string
}

export class StatusesDto {
    id: number;
    processStatusName: string;

}

export class FilterDto {
    regionID: number;
    sectionId: number;
    statusId: number;
    officerId: number;
    category: string;
    commenceDate: Date;
    lastDate: Date;
    permitType: number;
    productDescription: string;
}

