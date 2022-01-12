import {Timestamp} from "rxjs";
import {KnwSecTaskData} from "../std/std.model";


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
    entryNumber: string;
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
    assignStatus: number;
    assignedTo: number;
}
export interface StdLevyScheduleSiteVisitDTO {
    status: number;
    assistantManagerApproval: number;
    managersApproval: number;
    assistantManager: bigint;
    principalLevyOfficer:bigint;
    slManager:bigint;
    purpose:string;
    personMet:string;
    actionTaken:string;
    remarks:string;
    officersFeedback:string;
    manufacturerEntity:bigint;
    scheduledVisitDate: Timestamp<number>;
    visitDate: Timestamp<number>;
    reportDate: Timestamp<number>;
    slStatus:number;
    slStartedOn: Timestamp<number>;
    slCompletedOn: Timestamp<number>;
    slProcessInstanceId: string;
    cheifManagerRemarks: string;
    assistantManagerRemarks: string;
    createdBy: string;
    taskId: string;

}
export interface AssignCompanyTaskDTO {
    manufacturerEntity:number;
    assignedTo: number;
    companyName: string;
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
export interface VisitTask {
    taskId: string;
    name: string;
    taskData: VisitTaskData;
}
export interface VisitTaskData {
    ID: number;
    manufacturerEntity: number;
    scheduledVisitDate: string;
    meetingDate: string;
    visitDate: string;
    purpose: string;
    personMet: string;
    actionTaken: string;
    taskId: string;
    visitID: number;

}
export interface SiteVisitReport {
    meetingDate: string;
    visitDate: Timestamp<number>;
    purpose: string;
    personMet: string;
    actionTaken: string;
    taskId: string;
    visitID: number;
    assigneeId: number;

}
export interface ApproveVisitTask {
    taskId: string;
    name: string;
    taskData: ApproveVisitTaskData;
}
export interface ApproveVisitTaskData {
    ID: number;
    manufacturerEntity: number;
    scheduledVisitDate: string;
    meetingDate: string;
    visitDate: Timestamp<number>;
    purpose: string;
    personMet: string;
    actionTaken: string;
    taskId: string;
    visitID: number;

}
export interface ReportDecisionLevelOne{
    taskId: string;
    accentTo: boolean;
    visitID: bigint;
    comments: string;
    assigneeId: number;
}

export interface ReportDecisionLevelTwo{
    taskId: string;
    accentTo: boolean;
    visitID: bigint;
    comments: string;
}

export interface SiteVisitFeedBack {
    officersFeedback: string;
    taskId: string;
    visitID: number;

}
export interface ManufactureDetailList {
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
    assignStatus: number;
    assignedTo: number;
    userId: number;

}
export interface ManufacturePendingTask {
    taskId: string;
    name: string;
    taskData: ManufacturePendingTaskData;
}
export interface ManufacturePendingTaskData{
    id: number;
    companyName: string;
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
    assignStatus: number;
    assignedTo: number;
    visitID: number;
    manufacturerEntity: number;
    scheduledVisitDate: string;
    meetingDate: string;
    visitDate: Timestamp<number>;
    purpose: string;
    personMet: string;
    actionTaken: string;
    originator: number;
    contactId: number;

}
export interface ManufactureCompleteTask {
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
    assignStatus: number;
    assignedTo: number;

}

