import {UserRegister} from "../../../../shared/models/user";

export interface StandardRequestB {
    id: number;
    requestNumber: string;
    name: string;
    phone: string;
    email: string;
    departmentId: number;
    tcId: number;
    productId: number;
    productSubCategoryId: number;
    tcName: string;
    departmentName: string;
    productName: string;
    productSubCategoryName: string;
    organisationName: string;
    subject: string;
    description: string;
    economicEfficiency: number;
    healthSafety: number;
    environment: number;
    integration: number;
    exportMarkets: number;
    levelOfStandard: string;
    status: string;
    tcSecAssigned: string;
    reviewedBy: string;
    reviewDate: Date;
    reason: string;
    desiredOutput: string;
    desiredResult: string;
    createdOn: Date;
    standardCreationDate: Date;
    ongoingStatus: string;


}

export interface ServerResponse {
    httpStatus: string;
    responseText: string;
    body: FlowableData;
}


export interface FlowableData {
    processId: string;
    isEnded: boolean;
    body: StandardRequestB;
}

export interface Product {
    id: number;
    name: string;

}

export interface ProductSubCategory {
    id: number;
    name: string;


}

export interface DataHolderB {
    department: string;
    departmentData: LiaisonOrganization;

}

export interface Department {
    id: number;
    name: string;
    abbreviations: string;
    codes: string;
    createdBy: string;
    createdOn: string;
    status: string;
    userId: number;
    varField3:string;


}
export interface TcAssignment {
    tcId: number;
    userId: string;



}

export class LiaisonOrganization {
    id?: number;
    name?: string;


}

export interface TechnicalCommitteeb {
    id: number;
    departmentId: number;
    title: string;
    technical_committee_no: string;
    parentCommitte: string;
    createdBy: string;
    createdOn: string;
    status: string;

}

export interface ProductCategory {
    id: number;
    technicalCommitteeId: number;
    name: string;
    description: string;
    createdBy: string;
    createdOn: string;
    status: string;

}

export interface ProductSubCategoryB {
    id: number;
    productId: number;
    name: string;
    description: string;
    createdBy: string;
    createdOn: string;
    status: string;

}

export interface TechnicalCommittee {
    id: number;
    technical_committee_no: string;
    title: string;

}

export interface StandardTasks {
    taskId: string;
    name: string;
    taskData: StandardRequestB;
    id: string;
}


export interface TaskData {
    phone: string;
    departmentId: string;
    name: string;
    tcId: string;
    productId: string;
    productSubCategoryId: string;
    email: string;
    requestNumber: string;
    submissionDate: string;

}

export interface StdTCSecWorkPlan {
    taskId: string;
    name: string;
    taskData: StdWorkplanData
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
    dateOfPresentation: string;
    nameOfTC: string;
    id: number;
    taskData: StandardRequestB;
}

export interface VotesDto {
    id: number;
    decision: string;
    reason: string;
    nwiId: number;
    proposalTitle: string;
    standardId: number;
    requestNumber: string;
    dateVoteWasCast: Date;
    closingDate: string;
    voteOnBehalfBy: number;
    voteOnBehalfByName: string;
    voteOnBehalfByStatus: number;

}

export interface NwiItem {
    taskId: string;
    name: string;
    purpose: string;
    nameOfProposer: string;
    proposalTitle: string;
    scope: string;
    targetDate: string;
    similarStandards: string;
    liaisonOrganisation: string;
    draftAttached: string;
    outlineAttached: string;
    draftOutlineImpossible: string;
    outlineSentLater: string;
    organization: string;
    circulationDate: string;
    closingDate: string;
    referenceNumber: string;
    dateOfPresentation: string;
    nameOfTC: string;
    id: string;
    standardId: string;
    tcSec: string;
    processStatus: string;
    pdStatus: String;
    minutesPdStatus: string;
    draftDocsPdStatus: string;
    prPdStatus: string;
    deferredDate: string;
    nameOfDepartment:string;


}


export interface NWIsForVoting {
    taskId: string;
    name: string;
    purpose: string;
    nameOfProposer: string;
    proposalTitle: string;
    scope: string;
    targetDate: string;
    similarStandards: string;
    liaisonOrganisation: string;
    draftAttached: string;
    outlineAttached: string;
    draftOutlineImpossible: string;
    outlineSentLater: string;
    organization: string;
    circulationDate: string;
    closingDate: string;
    referenceNumber: string;
    dateOfPresentation: string;
    nameOfTC: string;
    id: string;
}


export interface StdJustification {
    id: string
    taskId: string;
    spcMeetingDate: Date;
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
    ksIsoNumber: string;
    scope: string;
    purpose: string;
    intendedUsers: string;
    status: string;
    cdNumber: null;
    createdOn: Date;
    modifiedOn: Date;
    deletedOn: Date;
    nwiId: number;
    createdBy: number;


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
    id: string;
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
    taskData: StdJustification;
}

export interface StdTCDecision {
    taskId: string;
    decision: string;
    reason: string;
}

export interface VoteOnNWI {
    taskId: string;
    decision: string;
    reason: string;
    position: string;
    organization: string;
    userId: string;
    nwiId: string;
}

export interface HOFFeedback {
    taskId: string;
    sdRequestID: string;
    isTc: string;
    isTcSec: string;
    sdOutput: string;
}

export interface StandardDraft {
    ID: number;
    title: string;
    requestorId: string;
    standardOfficerId: string;
    versionNumber: number;
    standardOfficerName: string;
    proofreadStatus: string;
    editedStatus: string;
    version: number;
    draughtingStatus: string;

}

export interface HOPTasks {
    taskId: string;
    name: string;
    taskData: StandardDraft;
}


export interface DraftPublishing {
    id: number;
    prdID: string;
    ballotName: string;
    ballotDraftBy: string;
    createdOn: string;
    modifiedOn: string;
    deletedOn: string;
    createdBy: string;
    modifiedBy: string;
    deleteBy: string;
    approvalStatus: string;
    varField2: string;
    varField1: string;
    status: string;
}

export interface DraftEditing {
    id: number;
    title: string;
    requestorId: string;
    requestorName: string;
    standardOfficerId: string;
    standardOfficerName: string;
    versionNumber: number;
    approvalStatus: string;
    approvedBy: string;
    editedStatus: string;
    editedBY: string;
    editedDate: string;
    proofreadStatus: string;
    proofReadBy: string;
    proofReadDate: string;
    draughtingStatus: string;
    draughtingBy: string;
    draughtingDate: string;
    submission_date: string;
    status: string;
    standardType: string;
    draftId: string;


}

export interface EditorTask {
    taskId: string;
    name: string;
    taskData: StandardDraft;
}

export interface StdWorkplanData {
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
    tcName: string;
    departmentName: string;
    targetDate: string;
    id: string;
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

export interface CallForApplication {
    id: number,
    tcId: number,
    expiryDate: string,
    dateOfPublishing: string,
    description: string,
    title: string,
    status: string,
    tc: string,
    createdOn: string,
    createdBy: number,

}

export interface SubmitApplicationsTask {
    taskId: string;
    name: string;
    taskData: CallForApplication;
}

export interface ReviewApplicationTask {

    id: number,
    technicalCommittee: string,
    organisationName: string,

    organisationClassification: string,
    nomineeName: string,
    position: string,
    postalAddress: string,
    mobileNumber: string,
    email: string,
    authorizingPerson: string,
    authorizingPersonPosition: string,
    authorizingPersonEmail: string,
    qualification: string,
    commitment: string,
    tcApplicationId: number,
    dateOfApplication: string,
    status: string,
    comments_by_hof: string,
    commentsBySpc: string,
    commentsBySac: string,
    hofId: string,
    spcId: string,
    sacId: string,
    nscId: string,
    commentsByNsc: string,
    varField9: string,
    varField10: string,
    taskId: string
}

export interface HOFRecommendationTask {
    taskId: string;
    name: string;
    taskData: HOFRecommendation;
}

export interface TCMemberDetails {
    taskId: string,
    userId: string,
    tcId: string,
    tc: string,
    name: string,
    email: string,
    postalAddress: string
    mobileNumber: string
    dateOfCreation: string
    status: string

}

export interface SACSummaryTask {
    taskId: string;
    name: string;
    taskData: SACSummary;
}

export interface SACSummary {
    id: number,
    department: string,
    technicalCommittee: string,
    sl: string,
    ks: string,
    title: string,
    edition: string,
    requestedBy: string,
    issuesAddressed: string,
    referenceMaterial: string,
    backgroundInformation: string,
    dateOfApproval: string,
    approvalStatus: string,
    feedback: string,
    eacGazette: string,
    authenticText: string,
    technicalCommitteeName: string,
    departmentName: string,
    requestedByName: string


}

export interface HOFRecommendation {
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

export interface SubmitApplication {
    technicalCommittee: string;
    organization: string;
    nomineeName: string;
    position: string;
    postalAddress: string;
    mobileNumber: string;
    email: string;
    authorizingName: string;
    authorisingPersonPosition: string;
    authorisingPersonEmail: string;
    qualifications: string;
    commitment: string;
    taskId: string;
    id: number;
}

export interface ProposalForTC {
    nameOfTC: string;
    purpose: string;
    liaisonOrganisation: string;
    proposer: string;
    scope: string;
    targetDate: string;
    subject: string;
    proposedRepresentation: string;
    draftOutlineImpossible: string;
    programmeOfWork: string;
    organization: string;
    dateOfPresentation: string;
    referenceNumber: string;
}

export interface StdJustificationDecision {
    taskId: string;
    decision: string;
    referenceNo: string;
    reason: string;
    justificationId: string;
}

export interface ServerResponseProcess {
    processId: string;
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
    ID: number;
    standardOfficerName: string;
    proofreadStatus: string;
    editedStatus: string;
    version: number;
    draughtingStatus: string;
}

export interface ProofReadData {
    ID: number;
    submission_date: string;
    approved: string;
    requestorId: string;
    standardOfficerId: string;
    title: string;
    versionNumber: string;
    standardOfficerName: string;
    proofreadStatus: string;
    editedStatus: string;
    version: number;
    draughtingStatus: string;
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


export interface StdWorkPlan {
    taskId: string;
    targetDate: string;
    referenceNo: string;
    stageDate: string;
    month: string;
    stage: string;
    subStage: string;
    value: string;
    id: string;
}

export interface DraughtsmanTask {
    taskId: string;
    name: string;
    taskData: DraughtsManData;
}

export interface DataHolder {
    id: number;
    name: string;
    tc_Title: string;
    v1: string;
    v2: string;
    v3: string;
    v4: string;
    v5: string;
    v6: string;
    number_OF_MEMBERS:string;

}

export interface Document {
    id: number;
    name: string;
    fileType: string;
    document: string;
    description: string

    createdOn: string;
    createdBy: string;

}


export interface Minutes {
    id: number;
    createdOn: string;
    createdBy: string;
    committeeId: string,
    chairpersonId: string,
    chairpersonData: UserRegister,
    secretaryId: string,
    secretaryData: UserRegister,
    attendeesId: string,
    attendeesData: UserRegister,
    agenda: string,
    minutesTitle: string,
    discussion: string,
    conclusion: string,
    actionItem: string,
    adjournmentDate: string,

}
