

export class Levy {
}

export class UploadsDtoSTA3 {
    uploadedFiles: File[];
    sta3Status: boolean;
}

export class ManufactureInfo{
    businessCompanyName: string;
    comKraPin: string;
    lineOfBusiness: string;
    certificateOfIncorporation: string;
    companyEmail: string;
    postalAddress: string;
    telephone: string;
    mainPhysicalLocation: string;
    location: string;
    plotNumber: string;
}
export class SLevySL1{
    NameAndBusinessOfProprietors : String;
    AllCommoditiesManufuctured :String;
    DateOfManufacture :Date;
    totalValueOfManufacture : number;
    companyProfileID :number;
    nameOfBranch :String;
    location: String;
}
export class ManufacturingInfo{
    NameAndBusinessOfProprietors: string;
    AllCommoditiesManufuctured: string;
    DateOfManufacture: Date;
    totalValueOfManufacture: number;
    nameOfBranch: string;
    addressOfBranch: string;

}
export class ManufactureBranchDto {
    id: bigint;
    location: string;
    plotNumber: string;
}
export class ManufacturingBranchDto {
    id: bigint;
    nameOfBranch: string;
    addressOfBranch: string;
}
export interface ManufacturePenalty{
    id: bigint;
    manufacturerId: bigint;
    kraPin: string;
    manufacturerName: string;
    recordStatus: string;
    transactionDate: string;
    transmissionDate: string;
    penaltyPayable: bigint;
    penaltyGenerationDate: string;
    periodFrom: string;
    periodTo: string;
    status: number;
}
export interface PaidLevy {
    id: bigint;
    status: string;
    manufacturerEntity: bigint;
    paymentDate: string;
    kraPin: string;
    paymentAmount: number;
    visitStatus: number;
    officerAssigned: bigint;
    levyPaid: number;
    netLevyAmount: number;
    levyPayable: number;
    levyPenalties: number;
    levyPenaltyPaymentDate: number;
}

export interface CompanyModel {
    id: number;
    name: string;
    kraPin: string;
    status: boolean;
    registrationNumber: string;
    postalAddress: string;
    physicalAddress: string;
    plotNumber: string;
    companyEmail: string;
    companyTelephone: string;
    yearlyTurnover: number;
    businessLines: number;
    businessNatures: number;
    buildingName: string;
    branchName: string;
    streetName: string;
    directorIdNumber: string;
    region: number;
    county: number;
    town: number;
    manufactureStatus: number;
    entryNumber: number;
}
