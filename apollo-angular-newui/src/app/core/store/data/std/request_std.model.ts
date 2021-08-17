export interface StandardRequest {
    id: number;
    requestNumber:string;
    name: string;
    phone: string;
    email: string;
    departmentId: number;
    tcId: number;
    productId: number;
    productSubCategoryId: number;
    tcName:string;
    departmentName:string;
    productName:string;
    productSubCategoryName:string;
}

export interface ServerResponse {
    httpStatus:string;
    responseText:string;
    body: FlowableData;
}


export interface FlowableData {
    processId:string;
    isEnded:boolean;
    body:StandardRequest;
}

export interface Product {
    id: number;
    name: string;

}

export interface ProductSubCategory {
    id: number;
    name: string;

}

export interface Department {
    id: number;
    name: string;
}

export class LiaisonOrganization {
    id?: number;
    name?: string="hahaha";


}

export interface TechnicalCommittee {
    id: number;
    technical_committee_no: string;
    title: string;

}

export interface StandardTasks {
    taskId: string;
    name: string;
    taskData: StandardRequest;
    id: string;
}



export interface TaskData {
    phone: string;
    departmentId: string;
    name: string;
    tcId: string;
    productId:string;
    productSubCategoryId: string;
    email: string;
    requestNumber:string;
    submissionDate:string;

}

export interface StdTCSecWorkPlan {
    taskId: string;
    name: string;
    taskData:StdWorkplanData
}

export interface Stdtsectask {
    taskId: string;
    name: string;
    purpose: string;
    nameOfProposer: string;
    proposalTitle: string;
    scope: string;
    targetDate: string;
    similarStandards: string;
    liaisonOrganisation: string;
    liaisonOrganisationData: LiaisonOrganization;
    draftAttached: string;
    outlineAttached: string;
    draftOutlineImpossible: string;
    outlineSentLater: string;
    organization: string;
    circulationDate: string;
    closingDate: string;
    referenceNumber: string;
    dateOfPresentation:string;
    nameOfTC: string;
    taskData:StandardRequest;
}

export interface StdJustification{
    taskId: string;
    spcMeetingDate: string;
    departmentId: string;
    tcId: string;
    tcSecretary: string;
    sl: string;
    requestNo: string;
    title: string;
    edition: string;
    requestedBy: string;
    issuesAddressedBy: string;
    tcAcceptanceDate: string;

}


export interface StdtsecTaskJustification {
    taskId: string;
    name: string;
    taskData: justificationTaskData;

}

export interface justificationTaskData {
    nameOfTC: string,
    purpose: string,
    departmentId: string,
    circulationDate: string,
    liaisonOrganisation: string,
    draftAttached: string,
    productName: string,
    tcName: string,
    requestNumber: string,
    approved: string,
    referenceNumber: string,
    scope: string,
    rank: string,
    outlineSentLater: string,
    tcId: string,
    email: string,
    departmentName: string,
    dateOfPresentation: string,
    proposalTitle: string,
    productId: string,
    outlineAttached: string,
    targetDate: string,
    submissionDate: string,
    closingDate: string,
    draftOutlineImpossible: string,
    productSubCategoryName: string,
    similarStandards: string,
    productSubCategoryId: string,
    phone: string,
    organization: string,
    name: string,
    nameOfProposer: string,
    taskId: string;
}

export interface StdTCTask {
    taskId: string;
    name: string;
    taskData: Stdtsectask;
}

export interface StdJustificationTask {
    taskId: string;
    name: string;
    taskData: Stdtsectask;
}

export interface StdSPCSECTask {
    taskId: string;
    name: string;
    taskData: string;
}

export interface StdTCDecision {
    taskId: string;
    decision: string;
    reason:string;
}

export interface VoteOnNWI {
    taskId: string;
    decision: string;
    reason:string;
    position:string;
    organization:string;
    userId:string;
}

export interface HOFFeedback{
    taskId: string;
    sdRequestID: string;
    isTc: string;
    isTcSec: string;
    sdOutput: string;
}

export interface StandardDraft {
    title:string;
    requestorId:string;
    standardOfficerId:string;
    versionNumber:string;
}

export interface HOPTasks {
    taskId: string;
    name: string;
    taskData: StandardDraft;
}

export interface EditorTask {
    taskId: string;
    name: string;
    taskData: StandardDraft;
}

export interface StdWorkplanData{
    taskId: string;
    spcMeetingDate: string;
    departmentId: string;
    tcId: string;
    tcSecretary: string;
    sl: string;
    requestNo: string;
    title: string;
    edition: string;
    requestedBy: string;
    issuesAddressedBy: string;
    tcAcceptanceDate: string;
    tcName:string;
    departmentName:string;
    targetDate:string;
}

export interface ReviewFormationOFTCRequest {
    taskId: string;
    name: string;
    taskData: ProposalForTC;
}

export interface ReviewFeedbackFromSPC {
    taskId: string;
    name: string;
    taskData: ProposalForTC;
}

export interface CallForApplication{
    user_id: string,
    dateOfPublishing: string,
    tc: string
}

export interface SubmitApplicationsTask
{
    taskId: string;
    name: string;
    taskData: CallForApplication;
}

export interface ReviewApplicationTask
{
    taskId: string;
    name: string;
    taskData: SubmitApplication;
}

export interface HOFRecommendationTask
{
    taskId: string;
    name: string;
    taskData: HOFRecommendation;
}

export interface TCMemberDetails
{
    taskId: string,
    userId: string,
    tc: string,
    name: string,
    email: string
}

export interface SACSummaryTask
{
    taskId: string;
    name: string;
    taskData: SACSummary;
}

export interface SACSummary
{
    sl: string,
    ks: string,
    requestedBy: string,
    issuesAddressed: string,
    backgroundInformation: string
}

export interface HOFRecommendation
{
    mobileNumber: string,
    commitment: string,
    nomineeName: string,
    authorizingName: string,
    authorisingPersonPosition: string,
    tc: string,
    qualifications: string,
    postalAddress: string,
    user_id: string,
    organization: string,
    dateOfPublishing: string,
    authorisingPersonEmail: string,
    comment: string,
    position: string,
    technicalCommittee: string,
    readvertise: string,
    taskId: string,
    email: string
}

export interface SubmitApplication
{
    technicalCommittee: string;
    organization: string;
    nomineeName: string;
    position:string;
    postalAddress:string;
    mobileNumber:string;
    email:string;
    authorizingName:string;
    authorisingPersonPosition:string;
    authorisingPersonEmail:string;
    qualifications:string;
    commitment:string;
    taskId:string;
}

export interface ProposalForTC {
    nameOfTC: string;
    purpose: string;
    liaisonOrganisation: string;
    proposer:string;
    scope:string;
    targetDate:string;
    subject:string;
    proposedRepresentation:string;
    draftOutlineImpossible:string;
    programmeOfWork:string;
    organization:string;
    dateOfPresentation:string;
    referenceNumber:string;
}

export interface StdJustificationDecision {
    taskId: string;
    decision: string;
    referenceNo:string;
    reason:string;
}

export interface ServerResponseProcess {
    processId:string;
    isEnded: boolean;
    body: any;
}

export interface ProofReadTask {
    taskId: string;
    name: string;
    taskData: ProofReadData;
}

export interface DraughtsManData {
    submission_date: string;
    approved: string;
    requestorId: string;
    standardOfficerId: string;
    title: string;
    draught: string;
    versionNumber: string;
}

export interface ProofReadData {
    submission_date: string;
    approved: string;
    requestorId: string;
    standardOfficerId: string;
    title: string;
    versionNumber: string;
}

export interface DecisionFeedback {
    taskId: string;
    user_id: string;
    item_id: string;
    status: boolean;
    comment: string;
}
export interface DecisionFeedback {
    taskId: string;
    user_id: string;
    item_id: string;
    status: boolean;
    comment: string;
}


export interface EditorTask {
    taskId: string;
    name: string;
    taskData: StandardDraft;
}

export interface StdWorkPlan{
    taskId: string;
    targetDate: string;
    referenceNo: string;
    stageDate: string;
    month:string;
    stage:string;
    subStage:string;
    value:string;
}

export interface DraughtsmanTask {
    taskId: string;
    name: string;
    taskData: DraughtsManData;
}
