import {UserEntityDto} from '../users';

export class Qa {
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
}

export class STA1 {
    commodityDescription: string;
    tradeMark: string;
    applicantName: string;
    sectionId: bigint;
    permitForeignStatus: number;
    attachedPlant: bigint;
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
    permitTypeID: bigint;
    permitAwardStatus: boolean;
    invoiceGenerated: boolean;
    approvedRejectedScheme: boolean;
    sendForPcmReview: boolean;
    sendApplication: boolean;
}


export class STA3 {
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

export class sta10Dto {
    firmName: string;
    statusCompanyBusinessRegistration: string;
    ownerNameProprietorDirector: string;
    postalAddress: string;
    contactPerson: string;
    telephone: string;
    emailAddress: string;
    physicalLocationMap: string;
    county: string;
    town: string;
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

