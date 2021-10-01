import {Timestamp} from "rxjs";

export interface StandardRequest {
    id : number;
    name: string;
    phone: string;
    email: string;
    tc_id: number;
    department_id: number;
    product_category: number;


}

export interface Product{
    id: number;
    name: string;

}

export interface ProductSubCategory{
    id: number;
    name: string;

}

export interface Department{
    id: number;
    name: string;

}

export interface UsersEntity{
    id: number;
    lastName: string;
    firstName: string;


}

export interface TechnicalCommittee{
    id: number;
    technical_committee_no: string;
    title: string;

}
export interface StandardTasks{
    taskId: string;
    name: string;
    taskData: any;
    id: string;
}
export interface Stdtsectask{
    taskId: string;
    name: string;
    name_of_proposer: string;
    proposal_title: string;
    nwi_scope: string;
    target_date: Timestamp<number>;
    similar_standards: string;
    relevant_documents: string;
    liaison_organisation: string;
    draft_outline: string;
    organization: string;
    circulation_date: Timestamp<number>;
    closing_date: Timestamp<number>;
    tc_signature: string;
}

export interface StdTCTask{
    taskId: string;
    name: string;
    taskData: string;

}


export interface NWAJustification{
    id: number;
    meetingDate: string;
    knw: string;
    knwSecretary: string;
    sl: string;
    requestNumber: string;
    requestedBy: string;
    issuesAddressed: string;
    knwAcceptanceDate: string;
    referenceMaterial: string;
    department: string;
    taskId: string;
    status: string;
    remarks: string;
    submissionDate: string;
    knwCommittee: string;
    departmentName: string;
    comments: string;


}

export interface KNWDepartment{
    id: number;
    name: string;

}

export interface KNWCommittee{
    id: number;
    technical_committee_no: string;

}

export interface KnwSecTasks{
    taskId: string;
    name: string;
    taskData: KnwSecTaskData;
}

export interface KnwSecTaskData {
    ID: number;
    requestedBy: string;
    requestNumber: string;
    referenceMaterial: string;
    knw: string;
    knwSecretary: string;
    meetingDate: string;
    Yes: string;
    sl: string;
    department: string;
    issuesAddressed: string;
    knwAcceptanceDate: string;
    title: string;
    scope: string;
    normativeReference: string;
    symbolsAbbreviatedTerms: string;
    clause: string;
    special: string;
    knwCommittee: string;
    departmentName: string;
    comments: string;
    diJNumber: bigint;
}


export interface SPCSECTasks{
    taskId: string;
    name: string;
    taskData: SPCTaskData;
}


export interface SPCTaskData {
    requestedBy: string;
    requestNumber: string;
    referenceMaterial: string;
    knw: string;
    meetingDate: string;
    sl: string;
    department: string;
    issuesAddressed: string;
    knwAcceptanceDate: string;
    submissionDate: string;
    knwCommittee: string;
    departmentName: string;
    knwSecretary: string;
    ID: number;
    comments: string;
}

export interface NWADiSdtJustification{
    id: number;
    cost: string;
    numberOfMeetings: number;
    identifiedNeed: string;
    dateOfApproval: string;
    taskId: string;
    jstNumber: number;
}
export interface NWADiJustification{
    id: number;
    cost: string;
    numberOfMeetings: number;
    identifiedNeed: string;
    dateOfApproval: string;
    taskId: string;
    jstNumber: number;
}

export interface DISDTTasks{
    taskId: string;
    name: string;
    taskData: DisDTTaskData;
}
export interface DisDTTaskData {
    cost: number;
    numberOfMeetings: number;
    identifiedNeed: string;
    dateOfApproval: string;
    taskId: string;
    requestedBy: string;
    requestNumber: string;
    referenceMaterial: string;
    knw: string;
    meetingDate: string;
    sl: string;
    department: string;
    issuesAddressed: string;
    knwAcceptanceDate: Date;
    knwCommittee: string;
    departmentName: string;
    ID: number;
    comments: string;
    jstNumber: number;


}

export interface NWAPreliminaryDraft{
    id: number;
    title: number;
    scope: number;
    normativeReference: string;
    symbolsAbbreviatedTerms: string;
    special: string;
    clause: string;
    taskId: string;
    diJNumber: number;
}

export interface NWAWorkShopDraft{
    id: number;
    title: number;
    scope: number;
    normativeReference: string;
    symbolsAbbreviatedTerms: string;
    special: string;
    taskId: string;
}
export interface SacSecTasks{
    taskId: string;
    name: string;
    taskData: SacSeCTaskData;
}

export interface SacSeCTaskData {
    ID: number;
    title: number;
    scope: number;
    normativeReference: string;
    symbolsAbbreviatedTerms: string;
    special: string;
    taskId: string;
    clause: string;
    referenceMaterial: string;
    comments: string;

}

export interface HOPTasks{
    taskId: string;
    name: string;
    taskData: HOPTaskData;
}
export interface HOPTaskData{
    title: number;
    referenceMaterial: string;
    scope: string;
    normativeReference:string;
    issuesAddressed: string;
    clause: string;
    special:string;
    comments: string;
}
export interface NWAStandard{
    id: number;
    title: number;
    scope: number;
    normativeReference: string;
    symbolsAbbreviatedTerms: string;
    special: string;
    taskId: string;
    ksNumber: string;
}

export interface HoSicTasks{
    taskId: string;
    name: string;
    taskData: HoSicTaskData;
}
export interface HoSicTaskData{
    title: number;
    referenceMaterial: string;
    scope: string;
    normativeReference:string;
    issuesAddressed: string;
    clause: string;
    special:string;
    ksNumber: number;
    description: string;
    dateUploaded: string;
    comments: string;
}
export interface UploadNwaGazette{
    id: number;
    ksNumber: number;
    description: string;
    dateUploaded: string;
    taskId: string;
}

export interface UpdateNwaGazette{
    id: number;
    ksNumber: number;
    description: string;
    dateOfGazettement: string;
    taskId: string;
}

export interface TaskData {
    requesterSubject: string;
    requesterInstitution: string;
    requesterEmail: string;
    requesterName: string;
    requesterCountry: string;
    requesterComment: string;
    requesterPhone: string;
    feedbackProvided: string;
    requestDate : string;
}

export interface Notifications{
    taskId: string;
    name: string;
    taskData: TaskData;
}
export interface RootObject {
    requesterName: string;
    requesterComment: string;
    requesterCountry: string;
    requesterEmail: string;
    requesterInstitution: string;
    requesterPhone: string;
    requesterSubject: string;
}

export interface InfoAvailableNo{
    taskId: string;
    isAvailable: string;
}

export interface finalSubmit{
    taskId: string;
}
//********************************************************** International Standards Adoption -START **********************************************************
export interface ISAdoptionProposal{
    taskId : string;
    proposal_doc_name: string;
    title : string;
}
export interface ProposalComments{
    taskId : string;
    name: string;
    taskData: PropComments;
}
export interface PropComments{
    proposal_doc_name: string;

}
export interface ISAdoptionComments{
    taskId: string;
    adoption_proposal_comment: string;
    user_id: string;
    comment_time: string;
}
export interface ISTcSecTASKS{
    taskId : string;
    name: string;
    taskData: IsTcSecTaskData;
}
export interface IsTcSecTaskData{
    adoption_proposal_comment: string;
    user_id: string;
    comment_time: string;
    proposal_doc_name: string;
}
export interface ISAdoptionJustification{
    id: number;
    meetingDate: string;
    tc_id: string;
    tcSec_id: string;
    slNumber: string;
    requestNumber: string;
    requestedBy: string;
    issuesAddressed: string;
    tcAcceptanceDate: string;
    referenceMaterial: string;
    department: string;
    taskId: string;
    status: string;
    remarks: string;

}
export interface ListJustification{
    taskId : string;
    name: string;
    taskData: JSListTaskData;
}
export interface JSListTaskData{
    id: number;
    meetingDate: string;
    tc_id: string;
    tcSec_id: string;
    slNumber: string;
    requestNumber: string;
    requestedBy: string;
    issuesAddressed: string;
    tcAcceptanceDate: string;
    referenceMaterial: string;
    department: string;
    taskId: string;
    status: string;
    remarks: string;
    adoption_proposal_comment: string;
    user_id: string;
    comment_time: string;
    proposal_doc_name: string;
}
export interface ISSacSecTASKS{
    taskId : string;
    name: string;
    taskData: ISSacSecTaskData;
}
export interface ISSacSecTaskData{
    id: number;
    meetingDate: string;
    tc_id: string;
    tcSec_id: string;
    slNumber: string;
    requestNumber: string;
    requestedBy: string;
    issuesAddressed: string;
    tcAcceptanceDate: string;
    referenceMaterial: string;
    department: string;
    taskId: string;
    status: string;
    remarks: string;
    adoption_proposal_comment: string;
    user_id: string;
    comment_time: string;
    proposal_doc_name: string;
}

//********************************************************** International Standards Adoption -END **********************************************************
//********************************************************** Company Standards Adoption -START **********************************************************
export interface CompanyStandardRequest{
    id : number;
    companyName: string;
    departmentId: string;
    productId: string;
    productSubCategoryId: string;
    companyPhone: string;
    companyEmail: string;
    tcId: string;


}

export interface ComHodTasks{
    taskId : string;
    name: string;
    taskData: ComHodTaskData;
}
export interface ComHodTaskData{
    id : number;
    requestNumber: string;
    companyName: string;
    departmentId: number;
    departmentName: string;
    productSubCategoryId: string;
    submissionDate: string;
    companyPhone: string;
    companyEmail: string;
    productId: number;
    tcId: number;
    productName: string;
    productSubCategoryName: string;
    tcName: string;


}

export interface ComStdAction{
    id: string;
    requestNumber: string;
    dateAssigned: string;
    assignedTo: string;
    taskId: string;
}

export interface ComJcJustification{
    taskId: string;
    name: string;
    taskData: ComJcJustificationData;
}
export interface ComJcJustificationData{
    requestNumber: string;
    companyEmail: string;
    productSubCategory: string;
    companyPhone: string;
    companyName: string;
    submissionDate: string;
    department: string;
    departmentName: string;
    productSubCategoryId: string;
    dateAssigned: string;
    assignedTo: string;
    productCategory: string;
    productId: number;
    tcId: number;
    productName: string;
    productSubCategoryName: string;
    tcName: string;
    ID: number;
}

export interface ComSecTasks{
    taskId: string;
    name: string;
    taskData: ComSecTaskData;
}

export interface ComSecTaskData {
    requestedBy: string;
    requestNumber: string;
    referenceMaterial: string;
    knw: string;
    knwSecretary: string;
    meetingDate: string;
    Yes: string;
    sl: string;
    department: string;
    issuesAddressed: string;
    knwAcceptanceDate: string;
    title: string;
    scope: string;
    normativeReference: string;
    symbolsAbbreviatedTerms: string;
    clause: string;
    special: string;

}



export interface ComJcJustificationAction{
    taskId: string;
    id: string;
    meetingDate: string;
    slNumber: string;
    requestNumber: string;
    requestedBy: string;
    status: string;
    remarks: string;
    issuesAddressed: string;
    tcAcceptanceDate: string;
    referenceMaterial: string;
    reason: string;
    department: string;
    projectLeader: string;
}
export interface ComJcJustificationList{
    taskId: string;
    name: string;
    taskData: ComJcJustificationListData;
}
export interface ComJcJustificationListData{
    reason: string;
    companyName: string;
    slNumber : string;
    tcAcceptanceDate : string;
    submissionDate : string;
    issuesAddressed : string;
    assignedTo : string;
    productCategory : string;
    requestedBy : string;
    requestNumber : string;
    companyEmail : string;
    referenceMaterial : string;
    productSubCategory : string;
    companyPhone : string;
    projectLeader : string;
    meetingDate : string;
    department : string;
    dateAssigned : string;
    remarks : string;
    status : string;
    tcName : string;
    departmentName : string;
    productName : string;
    productSubCategoryName : string;
    ID: number;
}

export interface ComJcJustificationDec{
    taskId: string;
    name: string;
    taskData: ComJcJustificationDecData;
}
export interface ComJcJustificationDecData{
    reason: string;
    companyName: string;
    slNumber : string;
    tcAcceptanceDate : string;
    submissionDate : string;
    issuesAddressed : string;
    assignedTo : string;
    productCategory : string;
    requestedBy : string;
    requestNumber : string;
    companyEmail : string;
    referenceMaterial : string;
    productSubCategory : string;
    companyPhone : string;
    projectLeader : string;
    meetingDate : string;
    department : string;
    dateAssigned : string;
    remarks : string;
    status : string;
    tcName : string;
    departmentName : string;
    productName : string;
    productSubCategoryName : string;
    ID: number;
    title: number;
    scope: string;
    normativeReference:string;
    clause: string;
    special:string;
    comments: string;
}
export interface ApproveJC{
    taskId: string;
    accentTo: boolean;
    approvalID: bigint;
    comments: string;
}
export interface ApproveSACJC{
    taskId: string;
    accentTo: boolean;
    approvalID: bigint;
    comments: string;
}
export interface ApproveDraft{
    taskId: string;
    accentTo: boolean;
    approvalID: bigint;
    comments: string;
}
export interface COMPreliminaryDraft{
    id: number;
    title: number;
    scope: number;
    normativeReference: string;
    symbolsAbbreviatedTerms: string;
    special: string;
    clause: string;
    taskId: string;
    diJNumber: number;
}

//********************************************************** Company Standards Adoption -END **********************************************************
//********************************************************** Systemic  Standards Standards Review -START **********************************************************
export interface ReviewedStandards{
    id: number;
    title: string;
    scope: string;
    normativeReference: string;
    symbolsAbbreviatedTerms:  string;
    clause: string;
    special: string;
    standardNumber: string;
    status: string;
    documentType: string;
    preparedBy: string;
    datePrepared: string;

}
export interface ReviewForm{
    id: number;
    title: string;
    documentType: string;
    preparedBy: string;
    datePrepared: string;
    standardNumber: string;
}

export interface ReviewComments{
    taskId: string;
    name: string;
    taskData: ReviewCommentsData;
}
export interface ReviewCommentsData{
    standardNumber: string;
    datePrepared: string;
    documentType: string;
    title: string;

}
export interface StandardReviewComments{
    id: number;
    taskId: string;
    comments: string;
    commentBy: string;
    dateOfComment: string;
}
export interface SystemicAnalyseComments{
    taskId: string;
    name: string;
    taskData: SystemicAnalyseCommentsData;

}
export interface SystemicAnalyseCommentsData{
    standardNumber: string;
    datePrepared: string;
    preparedBy: string;
    comments: string;
    commentBy: string;
    documentType: string;
    dateOfComment: string;
    title: string;
}
export interface StandardReviewRecommendations{
    id: number;
    recommendation: string;
    recommendationBy: string;
    dateOfRecommendation: string;
    taskId: string;
    accentTo: string;

}

// export interface RQNumber{
//   requestNumber: string;
// }

//********************************************************** Systemic  Standards Standards Review -END **********************************************************
//********************************************************** National Enquiry Point -START **********************************************************
export interface DepartmentResponse {
    departmentResponseID: number;
    enquiryID: number;
    feedbackProvided: string;
    taskId: string;
}

export interface FeedbackEmail {
    nepOfficerId: string;
    feedbackSent: string;
    requesterEmail: string;
    taskId: string;
}

export interface InfoAvailableYes{
    taskId: string;
    isAvailable: string;
}
//********************************************************** National Enquiry Point -END **********************************************************
//********************************************************** National Enquiry Point Domestic Notification -START **********************************************************
export interface NepNotification {
    taskId: string;
    name: string;
    taskData: NotificationBody;
}
export interface NotificationBody {
    tcSecretaryId?: number;
    nepOfficerId?: number;
    NotificationDueIndex: string;
    NotificationCategory: string;
}
export interface NepNotificationss {
    taskId: string;
    name: string;
    taskData: TaskDatas;
}
export interface TaskDatas {
    requesterSubject: string;
    requesterEmail: string;
    requesterInstitution: string;
    requesterName: string;
    requesterCountry: string;
    requesterComment: string;
    requesterPhone: string;
}
export interface DraftNotification {
    tcNotificationID: number;
    nepOfficerID: number;
    notificationDocumentIndex: string;
    taskID: string;
}

export interface NotificationData{
    tcNotificationID: number;
    tcSecretaryId: number;
    nepOfficerId: number;
    NotificationDueIndex: string;
    notificationDocumentIndex: string;
    NotificationCategory: string;
}

export interface InboundNotification{
    taskId: string;
    name: string;
    taskData: NotificationData;
}

export interface finalSubmit{
    taskId: string;
}

export interface ReviewApplicationTask
{
    taskId: string;
    name: string;
    taskData: SubmitApplication;
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
export interface DiSdtDECISION{
    taskId: string;
    accentTo: boolean;
    approvalID: bigint;
    comments: string;
    jstNumber: bigint;
}

export interface NWAJustificationDecision{
    taskId: string;
    accentTo: boolean;
    approvalID: bigint;
    comments: string;
}

export interface NWAPDDecision{
    taskId: string;
    accentTo: boolean;
    approvalID: bigint;
    comments: string;
    diJNumber: bigint;
}
export interface NWAWDDecision{
    taskId: string;
    accentTo: boolean;
    approvalID: bigint;
    comments: string;
}


