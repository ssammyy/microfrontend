import {UserEntityDto} from '../users';

export class Qa {
}

export class UploadsDtoSTA3 {
    uploadedFiles: File[];
    sta3Status: boolean;
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
}

export class GenerateInvoiceDto {
    batchID: bigint;
    plantID: bigint;
    permitRefNumber: string;
    permitInvoicesID: number[];
}

export class MPesaPushDto {
    entityValueID: bigint;
    phoneNumber: string;
}


export class STA1 {
    id: bigint;
    commodityDescription: string;
    tradeMark: string;
    applicantName: string;
    sectionId: bigint;
    permitForeignStatus: number;
    attachedPlant: bigint;
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
}

export class SectionDto {
    id: bigint;
    section: string;
    divisionId: bigint;
    descriptions: string;
    status: boolean;
}

export class AllPermitDetailsDto {
    permitDetails: PermitEntityDetails;
    officerList: UserEntityDto[];
    oldVersionList: PermitEntityDto[];
    batchID: bigint;
}


export class AllSTA10DetailsDto {
    sta10FirmDetails: Sta10Dto;
    sta10PersonnelDetails: STA10PersonnelDto[];
    sta10ProductsManufactureDetails: STA10ProductsManufactureDto[];
    sta10RawMaterialsDetails: STA10RawMaterialsDto[];
    sta10MachineryAndPlantDetails: STA10MachineryAndPlantDto[];
    sta10ManufacturingProcessDetails: STA10ManufacturingProcessDto[];
}

export class PlantDetailsDto {
    id: bigint;
    companyProfileId: bigint;
    county: string;
    town: string;
    location: string;
    street: string;
    buildingName: string;
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
    paidStatus: boolean;
    submittedStatus: boolean;
    plantId: bigint;
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
}

export class AllBatchInvoiceDetailsDto {
    batchDetails: InvoiceDto;
    allRelatedBatchInvoices: PermitInvoiceDto[];
}

export class TaskDto {
    permitId: bigint;
    taskName: String;
    taskCreateTime: Date;
    permitRefNo: String;
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



