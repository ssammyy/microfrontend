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
    manufacture_status: number;
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
export interface SlModel{
    id: number;
    commodity: string;
    dateOfManufacture: string;
    totalValue: number;
    nameOfBusinessProprietor: string;
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
    AllCommoditiesManufuctured: string;
    DateOfManufacture: string;
    totalValueOfManufacture: number;

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
export interface EditCompanyDTO {
    postalAddress: string;
    physicalAddress: string;
    ownership: string;
    companyId: number;
    closureOfOperations: string;
    companyName: string;
    kraPin: string;
    registrationNumber: string;
    entryNumber: string;
    processId: string;
    userType: number;
    taskType: number;
    assignedTo: number;
    taskId: string;
    accentTo: boolean;

}
export interface SiteVisitRemarks{
    siteVisitId: number;
    remarks: string;
    remarkBy: string;
    status: string;
    role: string;
    dateOfRemark: string;
}
export interface ConfirmEditCompanyDTO {
    postalAddress: string;
    physicalAddress: string;
    ownership: string;
    companyId: number;
    manufactureId: number
    closureOfOperations: string;
    processId: string;
    userType: number;
    taskType: number;
    assignedTo: number;
    taskId: string;
    accentTo: boolean;
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
    businessLineName: string;
    businessLines: number;
    businessNatureName: string;
    businessNatures: number;
    regionName: string;
    region: number;
    countyName: string;
    county: number;
    townName: string;
    town: number;
    buildingName: string;
    branchName: string;
    streetName: string;
    directorIdNumber: string;
    manufactureStatus: number;
    entryNumber: number;
    contactId: number;
    taskType: number;
    userType: number;

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
    manufacturerEntity: number;
    userType: number;
    makeRemarks: string;

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
    manufacturerEntity: number;
    status: string;
    role: string;
}

export interface ReportDecisionLevelTwo{
    taskId: string;
    accentTo: boolean;
    visitID: bigint;
    comments: string;
    manufacturerEntity: number;
    userType: number;
    status: string;
    role: string;
}

export interface SiteVisitFeedBack {
    officersFeedback: string;
    taskId: string;
    visitID: number;
    assigneeId: number;
    manufacturerEntity: number;

}
export interface DirectorsList {
    directorName: string;
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
    businessLineName: string;
    businessNatures: number;
    businessNatureName: string;
    buildingName: string;
    branchName: string;
    streetName: string;
    directorIdNumber: string;
    region: number;
    regionName: string;
    county: number;
    countyName: string;
    town: number;
    townName: string;
    manufactureStatus: number;
    entryNumber: number;
    assignStatus: number;
    assignedTo: number;
    userId: number;
    ownership: string;
    typeOfManufacture: number;

}
export interface ManufacturePendingTask {
    taskId: string;
    name: string;
    processId: string;
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
    businessLineName: string;
    buildingName: string;
    businessNatureName: string;
    branchName: string;
    streetName: string;
    directorIdNumber: string;
    region: number;
    county: number;
    regionName: string;
    town: number;
    countyName: string;
    manufactureStatus: number;
    townName: string;
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
    levyProcessInstance: string;
    levelOneId: number;
    levelOneAssignee: number;
    officersFeedback: string;
    userType: number;
    makeRemarks: string;
    comments: string;
    assistantManagerRemarks: string;
    cheifManagerRemarks: string;
    ownership: string;
    createdBy: string;
    manufactureId: string;
    approvalStatus: string;
    approvalStatusId: number;
    typeOfManufacture: number;
    rejectStatus: string;
    approvalStatusLevelTwo: string;
    rejectStatusLevelTwo: string;


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
    visitID: number;


}

export interface ManufactureCompletedTask {
    entryNumber: string;
    physicalAddress: string;
    visitDate: Timestamp<number>;
    purpose: string;
    plotNumber: string;
    kraPin: string;
    actionTaken: string;
    streetName: string;
    companyEmail: string;
    businessLines: number;
    companyId: number;
    remarks: string;
    postalAddress: string;
    companyName: string;
    directorIdNumber: number;
    companyTelephone: string;
    branchName: string;
    businessNature: string;
    visitId: number;
    registrationNumber: string;
    reportRemarks: string;
    feedBackRemarks: string;
    person: string;
    yearlyTurnOver: number;
    asstManagerRemarks: number;
    chiefManagerRemarks: string;
    businessLineName: string;
    businessNatureName: string;
    regionName: string;
    countyName: string;
    townName: string;

}
export interface UsersEntityList {

    name: string;


}
export interface UserEntityRoles {
    id: number;
}

export interface ManufacturingStatus {
    businessType: number;
}
export interface NotificationStatus {
    name: boolean;
}

export interface Branch {
    branchName: string;
}

