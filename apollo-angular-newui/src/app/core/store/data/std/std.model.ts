import {Timestamp} from 'rxjs';

export interface StandardRequest {
    id: number;
    name: string;
    phone: string;
    email: string;
    tc_id: number;
    departmentId: number;
    product_category: number;
    taskId: string;


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

export interface UserEntity {
    id: number;
    name: string;
    email: string;


}

export interface UsersEntity {
    id: number;
    lastName: string;
    name: string;
    firstName: string;
    userTypes: number;
    email: string;


}

export interface BusinessLinesView {
    id: number;
    name: string;
}

export interface RegionView {
    id: number;
    region: string;
}

export interface TechnicalCommittee {
    id: number;
    technical_committee_no: string;
    title: string;

}

export interface StandardTasks {
    taskId: string;
    name: string;
    taskData: any;
    id: string;
}

export interface Stdtsectask {
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

export interface StdTCTask {
    taskId: string;
    name: string;
    taskData: string;

}

export interface NWAJustification {
    id: number;
    dateOfMeeting: Timestamp<any>;
    knw: string;
    knwSecretary: string;
    sl: string;
    requestNumber: string;
    requestedBy: string;
    issuesAddressed: string;
    acceptanceDate: Timestamp<any>;
    referenceMaterial: string;
    department: string;
    taskId: string;
    status: string;
    remarks: string;
    submissionDate: string;
    knwCommittee: string;
    departmentName: string;
    comments: string;
    assignedTo: number;
    requestId: number;

}

export interface KNWDepartment {
    id: number;
    name: string;

}

export interface KNWCommittee {
    id: number;
    technical_committee_no: string;
    title: string;

}

export interface NwaTasks {
    taskId: string;
    name: string;
    processId: string;
    taskData: NwaTaskData;
}

export interface NwaTaskData {
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
    diJNumber: bigint;
    submissionDate: string;
    knwCommittee: string;
    departmentName: string;
    jsUploadDocId: number;
    comments: string;
    cost: number;
    numberOfMeetings: number;
    identifiedNeed: string;
    dateOfApproval: string;
    taskId: string;
    jstNumber: number;
    ksNumber: string;
    description: string;
    dateUploaded: string;
    originator: number;
    diOriginator: number;
    vpdOriginator: number;

}

export interface KnwSecTasks {
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

export interface PreliminaryDraftTasks {
    taskId: string;
    name: string;
    taskData: PDTaskData;
}

export interface PDTaskData {
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
    cost: string;
    numberOfMeetings: number;
    identifiedNeed: string;
}

export interface SPCSECTasks {
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
    jsUploadDocId: number;
    comments: string;
}

export class FileData {
    filename?: string;
    contentType?: string;
    size?: number;
}

export interface NWADiSdtJustification {
    id: number;
    cost: string;
    numberOfMeetings: number;
    identifiedNeed: string;
    dateOfApproval: string;
    taskId: string;
    jstNumber: number;
}

export interface NWADiJustification {
    id: number;
    cost: string;
    numberOfMeetings: number;
    identifiedNeed: string;
    dateOfApproval: string;
    taskId: string;
    jstNumber: number;
}

export interface DISDTTasks {
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

export interface NWAPreliminaryDraft {
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

export interface NWAWorkShopDraft {
    id: number;
    title: number;
    scope: number;
    normativeReference: string;
    symbolsAbbreviatedTerms: string;
    special: string;
    taskId: string;
}

export interface SacSecTasks {
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
    ksNumber: string;

}

export interface HOPTasks {
    taskId: string;
    name: string;
    taskData: HOPTaskData;
}

export interface HOPTaskData {
    title: number;
    referenceMaterial: string;
    scope: string;
    normativeReference: string;
    issuesAddressed: string;
    clause: string;
    special: string;
    comments: string;
    ID: number;
    ksNumber: string;
}

export interface NWAStandard {
    id: number;
    title: number;
    scope: number;
    normativeReference: string;
    symbolsAbbreviatedTerms: string;
    special: string;
    taskId: string;
    ksNumber: string;
    processId: string;
}

export interface HoSicTasks {
    taskId: string;
    name: string;
    taskData: HoSicTaskData;
}

export interface HoSicTaskData {
    title: number;
    referenceMaterial: string;
    scope: string;
    normativeReference: string;
    issuesAddressed: string;
    clause: string;
    special: string;
    ksNumber: number;
    description: string;
    dateUploaded: string;
    comments: string;
    ID: number;
}

export interface UploadNwaGazette {
    id: number;
    ksNumber: number;
    description: string;
    dateUploaded: string;
    taskId: string;
}

export interface UpdateNwaGazette {
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
    requestDate: string;
}

export interface Notifications {
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

export interface InfoAvailableNo {
    taskId: string;
    isAvailable: string;
}

export interface finalSubmit {
    taskId: string;
}

// **********************************************************International Standards Adoption**********************************************************
export interface IstProposalComment{
    commentTitle: string;
    scope: string;
    clause: string;
    proposalID: number;
    standardNumber: string;
    commentDocumentType: string;
    recommendations: string;
    nameOfRespondent: string;
    positionOfRespondent: string;
    nameOfOrganization: string;
    preparedDate: Timestamp<any>;
    paragraph: string;
    typeOfComment: string;
    comment: string;
    proposedChange: string;
    observation: string;
}
export interface ProposalComment {
    adoption_proposal_comment: string;
    commentTitle: string;
    commentDocumentType: string;
    comNameOfOrganization: string;
    comClause: string;
    comParagraph: string;
    proposedChange: string;
    taskId: string;
    proposalID: number;
    typeOfComment: string;
    adopt: string;
    reasonsForNotAcceptance: string;
    recommendations: string;
    nameOfRespondent: string;
    positionOfRespondent: string;
    nameOfOrganization: string;
    dateOfApplication: string;
}

export interface ComDraftComment {
    commentTitle: string;
    commentDocumentType: string;
    uploadDate: Timestamp<any>;
    nameOfRespondent: string;
    emailOfRespondent: string;
    phoneOfRespondent: string;
    nameOfOrganization: string;
    clause: string;
    paragraph: string;
    typeOfComment: string;
    comment: string;
    proposedChange: string;
    observation: string;
    requestID: string;
    draftID: string;


}
export class ComStdContactFields{
    contactOneFullName: string;
    contactOneTelephone: string;
    contactOneEmail: string;
}
export class ComStdRequestFields{
    companyName: string;
    companyPhone: string;
    departmentId: number;
    companyEmail: string;
    subject: string;
    description: string;
    contactDetails: ComStdContactFields[]

}
export class PredefinedSdIntCommentsFields{
    commentTitle: string;
    commentDocumentType: string;
    uploadDate: Timestamp<any>;
    nameOfRespondent: string;
    emailOfRespondent: string;
    phoneOfRespondent: string;
    nameOfOrganization: string;
    scope: string;
    clause: string;
    paragraph: string;
    typeOfComment: string;
    comment: string;
    proposedChange: string;
    observation: string;
    requestID: number;
    draftID: number;
}
export class PredefinedSDCommentsFields {
    standardNumber: string;
    commentTitle: string;
    commentDocumentType: string;
    preparedDate: Timestamp<any>;
    docName: string;
    nameOfRespondent: string;
    emailOfRespondent: string;
    phoneOfRespondent: string;
    nameOfOrganization: string;
    scope: string;
    clause: string;
    paragraph: string;
    typeOfComment: string;
    comment: string;
    proposedChange: string;
    requestID: number;
    draftID: number;
    observation: string;
}

export interface ISAdoptionProposal {
    taskId: string;
    id: number;
    docName: string;
    title: string;
    tcSecName: string;
    circulationDate: string;
    closingDate: string;
    scope: string;
    clause: string;
    standardNumber: string;
    adoptionAcceptableAsPresented: string;
    reasonsForNotAcceptance: string;
    recommendations: string;
    nameOfRespondent: string;
    positionOfRespondent: string;
    nameOfOrganization: string;
    dateOfApplication: string;
    proposalNumber: string;
    stakeholdersList: string[];
    addStakeholdersList: string[];
    iStandardNumber: string;
    deadlineDate: Timestamp<any>;
    noOfComments: number;
    preparedDate: Timestamp<any>;
    draftId: number;
    draftNumber: string;
    draftTitle: string;
    companyName: string;
    contactOneEmail: string;
    contactOneFullName: string;
    contactOneTelephone: string;
    remarks: string;
    departmentId: number;
    subject: string;
    description: string;
    special: string;
    normativeReference: string;
}

export interface ISJustificationProposal {
    id: number;
    meetingDate: string;
    tcId: string;
    cSec: string;
    slNumber: string;
    edition: string;
    requestNumber: string;
    requestedBy: string;
    issuesAddressed: string;
    tcAcceptanceDate: string;
    referenceMaterial: string;
    department: number;
    remarks: string;
    submissionDate: string;
    tcCommittee: string;
    departmentName: string;
    positiveVotes: number;
    negativeVotes: number;
    title: string;
    scope: string;
    normativeReference: string;
    symbolsAbbreviatedTerms: string;
    clause: string;
    special: string;
    proposalId: number;
}

export interface ProposalComments {
    taskId: string;
    name: string;
    processId: string;
    taskData: PropComments;
}

export interface PropComments {
    proposal_doc_name: string;
    ID: number;
    comments: string;
    title: string;
    tcSecName: string;
    circulationDate: string;
    closingDate: string;
    scope: string;
    adoptionAcceptableAsPresented: string;
    reasonsForNotAcceptance: string;
    recommendations: string;
    nameOfRespondent: string;
    positionOfRespondent: string;
    nameOfOrganization: string;
    dateOfApplication: string;
    uploadedBy: string;
    preparedDate: string;
    proposalNumber: string;
    meetingDate: string;
    slNumber: string;
    edition: string;
    requestedBy: string;
    issuesAddressed: string;
    tcAcceptanceDate: string;
    departmentName: string;
    positiveVotes: number;
    negativeVotes: number;
    symbolsAbbreviatedTerms: string;
    normativeReference: string;
    clause: string;
    special: string;
    draftId: number;
    iSNumber: string;
    description: string;
    proposalID: number;

}

export interface Proposal {
    taskId: string;
    name: string;
    proposal_doc_name: string;
    ID: number;
    comments: string;
    title: string;
    tcSecName: string;
    circulationDate: string;
    closingDate: string;
    scope: string;
    adoptionAcceptableAsPresented: string;
    reasonsForNotAcceptance: string;
    recommendations: string;
    nameOfRespondent: string;
    positionOfRespondent: string;
    nameOfOrganization: string;
    dateOfApplication: string;


}

export interface ISAdoptionComments {
    taskId: string;
    adoption_proposal_comment: string;
    user_id: string;
    comment_time: string;
    ID: number;
}

export interface ISTcSecTASKS {
    taskId: string;
    name: string;
    taskData: IsTcSecTaskData;
}

export interface IsTcSecTaskData {
    adoption_proposal_comment: string;
    user_id: string;
    comment_time: string;
    proposal_doc_name: string;
    ID: number;
}

export interface ISDraftUpload {
    title: string;
    normativeReference: string;
    symbolsAbbreviatedTerms: string;
    clause: string;
    scope: string;
    special: string;
    proposalId: number;
    taskId: string;
    accentTo: boolean;
    approvalID: bigint;
    id: number;
}

export interface IStandardUpload {
    title: string;
    normativeReference: string;
    symbolsAbbreviatedTerms: string;
    clause: string;
    scope: string;
    special: string;
    justificationNo: number;
    taskId: string;
    accentTo: boolean;
    proposalId: number;
    requestId: number;
    id: number;
    standardNumber: string;
    comStdNumber: string;
    preparedBy: string;
    docName: string;
    departmentId: string;
    tcName: string;
    subject: string;
    description: string;
    contactOneFullName: string;
    contactOneTelephone: string;
    contactOneEmail: string;
    contactTwoFullName: string;
    contactTwoTelephone: string;
    contactTwoEmail: string;
    contactThreeFullName: string;
    contactThreeTelephone: string;
    contactThreeEmail: string;
    companyName: string;
    companyPhone: string;
}
export interface ComStdDraftEdit{
    id: number;
    requestId: number;
    title: string;
    docName: string;
    standardNumber: string;
    draughting: string;
    draftId: number;
    requestNumber: string;
    scope: string;
    normativeReference: string;
    symbolsAbbreviatedTerms: string;
    clause: string;
    special: string;
    comStdNumber: string;
    departmentId: number;
    subject: string;
    description: string;
    contactOneFullName: string;
    contactOneTelephone: string;
    contactOneEmail: string;
    contactTwoFullName: string;
    contactTwoTelephone: string;
    contactTwoEmail: string;
    contactThreeFullName: string;
    contactThreeTelephone: string;
    contactThreeEmail: string;
    companyName: string;
    companyPhone: string;
    standardType: string;
}

export interface IStandardDraftEdit {
    title: string;
    normativeReference: string;
    symbolsAbbreviatedTerms: string;
    clause: string;
    scope: string;
    special: string;
    justificationNo: number;
    id: number;
    proposalId: number;
    docName: string;
    standardNumber: string;
}
export interface InterNationalStdDecision{
    comments:string;
    accentTo:string;
    proposalId:number;
    draftId:number;
}

export interface ISDecision {
    taskId: string;
    accentTo: boolean;
    approvalID: bigint;
    comments: string;
    processId: string;
    proposalId: number;
}
export interface ISProposalJustification{
    meetingDate : string;
    slNumber : string;
    edition : string;
    department : string;
    requestedBy : string;
    scope : string;
    purposeAndApplication : string;
    intendedUsers : string;
    circulationDate : Timestamp<any>;
    closingDate : Timestamp<any>;
    tcAcceptanceDate : string;
    proposalId : number;
    draftId : number;  
}

export interface ISAdoptionJustification {
    DocDescription : string;
    proposalId : number;
    draftId: number;
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
    edition: string;
    scope: string;
    purposeAndApplication: string;
    intendedUsers: string;
    circulationDate: string;
    closingDate: string;
    positiveVotes: number;
    negativeVotes: number;
    processId: string;

}

export interface ListJustification {
    taskId: string;
    name: string;
    taskData: JSListTaskData;
}

export interface JSListTaskData {
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
    ID: number;
    tcCommittee: string;
    departmentName: string;
}

export interface ISJustificationDecision {
    taskId: string;
    processId: string;
    accentTo: boolean;
    approvalID: bigint;
    comments: string;
}

export interface ISDraftDecision {
    comments: string;
    accentTo: string;
    justificationId: number;
    id: number;
    proposalId: number;
    requestId: number;
    draftId: number;
    standardType: string;
}

export interface ISDraftDecisionStd {
    comments: string;
    accentTo: string;
    justificationId: number;
    proposalId: number;
    draftId: number;
    title: string;
    normativeReference: string;
    symbolsAbbreviatedTerms: string;
    clause: string;
    scope: string;
    special: string;
    standardNumber: string;
}

export interface ISSacSecTASKS {
    taskId: string;
    name: string;
    taskData: ISSacSecTaskData;
}

export interface ISSacSecTaskData {
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
    ID: number;
    tcCommittee: string;
    departmentName: string;
}

export interface ISHopTASKS {
    taskId: string;
    name: string;
    taskData: ISHopTaskData;
}

export interface ISHopTaskData {
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
    ID: number;
    tcCommittee: string;
    departmentName: string;
}

export interface ISStandard {
    id: number;
    title: number;
    scope: number;
    normativeReference: string;
    symbolsAbbreviatedTerms: string;
    special: string;
    taskId: string;
}

export interface ISHosSicTASKS {
    taskId: string;
    name: string;
    taskData: ISHosSicTaskData;
}

export interface ISHosSicTaskData {
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
    ID: number;
    title: number;
    scope: number;
    normativeReference: string;
    symbolsAbbreviatedTerms: string;
    special: string;
    iSNumber: string;
}

export interface GazetteNotice {
    id: number;
    description: string;
    iSNumber: string;

}

//********************************************************** International Standards Adoption -END **********************************************************
//********************************************************** Company Standards Adoption -START **********************************************************
export interface CompanyStandardRequest {
    id: number;
    companyName: string;
    departmentId: string;
    productId: string;
    productSubCategoryId: string;
    companyPhone: string;
    companyEmail: string;
    tcId: string;
    subject: string;
    description: string;
    contactOneFullName: string;
    contactOneTelephone: string;
    contactOneEmail: string;
    contactTwoFullName: string;
    contactTwoTelephone: string;
    contactTwoEmail: string;
    contactThreeFullName: string;
    contactThreeTelephone: string;
    contactThreeEmail: string;


}

export interface ComHodTasks {
    taskId: string;
    name: string;
    taskData: ComHodTaskData;
}

export interface ComHodTaskData {
    id: number;
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
    plAssigned: string;
    nameOfJc: string;


}

export interface ComStdAction {
    id: string;
    requestNumber: string;
    dateAssigned: string;
    requestId: number;
}

export interface ComStandardJC {
    id: string;
    requestNumber: string;
    idOfJc: string;
    taskId: string;
    processId: string;
    requestId: number;
    name: string[];
    names: string[];
}

export interface ComJcJustification {
    taskId: string;
    name: string;
    taskData: ComJcJustificationData;
}

export interface ComJcJustificationData {
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

export interface ComSecTasks {
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


export interface ComJcJustificationAction {
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

export interface ComJcJustificationList {
    taskId: string;
    name: string;
    taskData: ComJcJustificationListData;
}

export interface ComJcJustificationListData {
    reason: string;
    companyName: string;
    slNumber: string;
    tcAcceptanceDate: string;
    submissionDate: string;
    issuesAddressed: string;
    assignedTo: string;
    productCategory: string;
    requestedBy: string;
    requestNumber: string;
    companyEmail: string;
    referenceMaterial: string;
    productSubCategory: string;
    companyPhone: string;
    projectLeader: string;
    meetingDate: string;
    department: string;
    dateAssigned: string;
    remarks: string;
    status: string;
    tcName: string;
    departmentName: string;
    productName: string;
    productSubCategoryName: string;
    ID: number;
}

export interface ComStandard {
    id: number;
    clause: string;
    title: string;
    scope: string;
    normativeReference: string;
    symbolsAbbreviatedTerms: string;
    special: string;
    comStdNumber: string;
}

export interface ComJcJustificationDec {
    taskId: string;
    name: string;
    processId: string;
    taskData: ComJcJustificationDecData;
}

export interface DocView {

    filepath: string;
    name: string;
    fileType: string;
    documentType: string;
    transactionDate: string;
    comDraftDocumentId: number;
    description: string;
    status: string;
    createdBy: string;
    id: bigint;
    document: Blob;
}

export interface ComJcJustificationDecData {
    reason: string;
    companyName: string;
    slNumber: string;
    tcAcceptanceDate: string;
    submissionDate: string;
    issuesAddressed: string;
    assignedTo: string;
    productCategory: string;
    requestedBy: string;
    requestNumber: string;
    companyEmail: string;
    referenceMaterial: string;
    productSubCategory: string;
    companyPhone: string;
    projectLeader: string;
    meetingDate: string;
    department: string;
    dateAssigned: string;
    remarks: string;
    status: string;
    tcName: string;
    departmentName: string;
    productName: string;
    productSubCategoryName: string;
    ID: number;
    title: number;
    scope: string;
    normativeReference: string;
    clause: string;
    special: string;
    comments: string;
    symbolsAbbreviatedTerms: string;
    draftNumber: string;
}

export interface ApproveJC {
    taskId: string;
    accentTo: boolean;
    approvalID: bigint;
    comments: string;
}

export interface ApproveSACJC {
    taskId: string;
    accentTo: boolean;
    approvalID: bigint;
    comments: string;
}

export interface ApproveDraft {
    taskId: string;
    accentTo: boolean;
    approvalID: bigint;
    requestId: bigint;
    comments: string;
    response: string;
    companyName: string;
    companyPhone: string;
    departmentId: number;
    subject: string;
    description: string;
    title: string;
}

export interface COMPreliminaryDraft {
    id: number;
    title: number;
    scope: number;
    normativeReference: string;
    symbolsAbbreviatedTerms: string;
    special: string;
    clause: string;
    taskId: string;
    processId: string;
    diJNumber: number;
    requestNumber: string;
    comStdNumber: string;
    requestId: number;
    draftNumber: string;
    status: number;
    deadlineDate: Timestamp<any>;
    departmentId: string;
    tcName: string;
    subject: string;
    description: string;
    contactOneFullName: string;
    contactOneTelephone: string;
    contactOneEmail: string;
    contactTwoFullName: string;
    contactTwoTelephone: string;
    contactTwoEmail: string;
    contactThreeFullName: string;
    contactThreeTelephone: string;
    contactThreeEmail: string;
    companyName: string;
    companyPhone: string;
    commentCount: number;
    uploadDate: Timestamp<any>;
}

export interface ComApproveDraft {
    accentTo: boolean;
    requestId: bigint;
    comments: string;
}

//********************************************************** Company Standards Adoption -END **********************************************************
//********************************************************** Systemic  Standards Standards Review -START **********************************************************
export interface ReviewedStandards {
    id: number;
    title: string;
    scope: string;
    normativeReference: string;
    symbolsAbbreviatedTerms: string;
    clause: string;
    special: string;
    standardNumber: string;
    status: string;
    documentType: string;
    preparedBy: string;
    datePrepared: string;

}

export interface ReviewForm {
    id: number;
    title: string;
    documentType: string;
    preparedBy: string;
    datePrepared: string;
    standardNumber: string;
}

export interface ReviewComments {
    taskId: string;
    name: string;
    taskData: ReviewCommentsData;
}

export interface ReviewCommentsData {
    standardNumber: string;
    datePrepared: string;
    documentType: string;
    title: string;

}

export interface StandardReviewComments {
    id: number;
    taskId: string;
    comments: string;
    commentBy: string;
    dateOfComment: string;
}

export interface SystemicAnalyseComments {
    taskId: string;
    name: string;
    taskData: SystemicAnalyseCommentsData;

}

export interface SystemicAnalyseCommentsData {
    standardNumber: string;
    datePrepared: string;
    preparedBy: string;
    comments: string;
    commentBy: string;
    documentType: string;
    dateOfComment: string;
    title: string;
}

export interface StandardReviewRecommendations {
    id: number;
    recommendation: string;
    recommendationBy: string;
    dateOfRecommendation: string;
    taskId: string;
    accentTo: string;
    comments: string;
    processId: string;
    reviewID: number;
    taskType: string;
    proposalId: number;

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

export interface InfoAvailableYes {
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

export interface NotificationData {
    tcNotificationID: number;
    tcSecretaryId: number;
    nepOfficerId: number;
    NotificationDueIndex: string;
    notificationDocumentIndex: string;
    NotificationCategory: string;
}

export interface InboundNotification {
    taskId: string;
    name: string;
    taskData: NotificationData;
}

export interface finalSubmit {
    taskId: string;
}

export interface ReviewApplicationTask {
    taskId: string;
    name: string;
    taskData: SubmitApplication;
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
}

export interface DiSdtDECISION {
    taskId: string;
    accentTo: boolean;
    approvalID: bigint;
    comments: string;
    jstNumber: bigint;
}
export interface NwaDecisionOnJustification{
    comments: string;
    accentTo: string;
    requestId: number;
}

export interface NWAJustificationDecision {
    taskId: string;
    accentTo: boolean;
    approvalID: bigint;
    comments: string;
}

export interface NWAPDDecision {
    taskId: string;
    accentTo: boolean;
    approvalID: bigint;
    comments: string;
    diJNumber: bigint;
}

export interface NWAWDDecision {
    taskId: string;
    accentTo: boolean;
    approvalID: bigint;
    comments: string;
}

export interface CompanyStdRemarks {
    approvalID: number;
    remarks: string;
    remarkBy: string;
    status: string;
    role: string;
    dateOfRemark: string;
}

export interface InternationalStandardsComments {
    id: number;
    proposalId: number;
    remarks: string;
    remarkBy: string;
    status: string;
    role: string;
    description: string;
    dateOfRemark: Timestamp<any>;
}

export interface ReviewStandardsComments {
    id: number;
    proposalId: number;
    remarks: string;
    remarkBy: string;
    status: string;
    role: string;
    description: string;
    dateOfRemark: Timestamp<any>;
}


export interface StakeholderProposalComments {
    id: number;
    user_id: number;
    adoption_proposal_comment: string;
    adoptionComment: string;
    comment_time: string;
    taskId: string;
    proposalID: number;
    commentTitle: string;
    commentDocumentType: string;
    comNameOfOrganization: string;
    clause: string;
    comClause: string;
    comParagraph: string;
    typeOfComment: string;
    proposedChange: string;
    adopt: string;
    nameOfOrganization: string;
    nameOfRespondent: string;
    positionOfRespondent: string;
    reasonsForNotAcceptance: string;
    recommendations: string;

}
export interface CommentOnProposalStakeHolder{
    id: number;
    userId: string;
    adoptionComment: string;
    commentTime: Timestamp<any>;
    proposalId: number;
    title: string;
    documentType: string;
    clause: string;
    comNameOfOrganization: string;
    paragraph: string;
    typeOfComment: string;
    proposedChange: string;
    adopt: string;
    reasonsForNotAcceptance: string;
    recommendations: string;
    nameOfRespondent: string;
    positionOfRespondent: string;
    nameOfOrganization: string;
    dateOfApplication: Timestamp<any>;
    scope: string;
    observation: string;
}

export interface StandardsForReview {
    id: number;
    title: string;
    scope: string;
    normativeReference: string;
    symbolsAbbreviatedTerms: string;
    clause: string;
    special: string;
    standardNumber: string;
    standardType: string;
    dateFormed: string;
    documentType: string;
}

export interface RevProposalComments {
    id: number;
    title: string;
    scope: string;
    normativeReference: string;
    symbolsAbbreviatedTerms: string;
    clause: string;
    special: string;
    standardNumber: string;
    standardType: string;
    reviewID: number;
}

export interface CommentsOnProposal {
    adoptionComment: string;
    title: string;
    documentType: string;
    paragraph: string;
    typeOfComment: string;
    proposedChange: string;
    proposalID: number;
}

export interface CommentOnProposal {
    adoptionComment: string;
    title: string;
    documentType: string;
    paragraph: string;
    typeOfComment: string;
    proposedChange: string;
    proposalId: number;
}

export interface StandardReviewTasks {
    taskId: string;
    name: string;
    processId: string;
    taskData: StandardReviews;

}

export interface StandardReviews {
    title: string;
    standardNumber: string;
    documentType: string;
    preparedBy: string;
    datePrepared: string;
    scope: string;
    normativeReference: string;
    symbolsAbbreviatedTerms: string;
    clause: string;
    special: string;
    standardType: string;
    reviewID: number;
    userName: string;
    adoptionComment: string;
    proposalId: number;
    paragraph: string;
    typeOfComment: string;
    proposedChange: string;
    feedback: number;
    summaryOfRecommendations: string;
    standardID: number;
    requestNumber: string;
    draftId: number;
    recommendationTime: Timestamp<any>;
    comments: string;
    userID: number;
    assignedTo: number;
    taskType: number;
    RecommendationID: number;
}

export interface ReviewProposalComments {
    id: number;
    userName: string;
    adoptionComment: string;
    commentTime: string;
    proposalId: number;
    title: string;
    documentType: string;
    clause: string;
    paragraph: string;
    typeOfComment: string;
    proposedChange: string;
}

export interface ReviewRecommendation {
    proposalId: number;
    summaryOfRecommendations: string;
    processId: string;
    taskId: string;
    feedback: number;
}

export interface GazetteStandard {
    standardID: number;
    processId: string;
    taskId: string;
}

export interface ReviewDraftEditing {
    taskId: string;
    processId: string;
    title: string;
    scope: string;
    normativeReference: string;
    symbolsAbbreviatedTerms: string;
    clause: string;
    special: string;
    standardNumber: string;
    standardType: string;
    dateFormed: string;
    documentType: string;
    draftId: number;
}

export interface ReviewDecision {
    taskId: string;
    accentTo: string;
    comments: string;
    processId: string;
    reviewID: string;

}

export interface ISCheckRequirements {
    id: number;
    title: string;
    scope: string;
    normativeReference: string;
    symbolsAbbreviatedTerms: string;
    clause: string;
    special: string;
    justificationNo: number;
    proposalId: number;
    status: string;
    uploadDate: string;
    isNumber: string;
    requestId: number;
    documentType: string;
    preparedBy: string;
    comStdNumber: string;
    requestNumber: string;
    draftId: number;
    departmentId: string;
    tcName: string;
    subject: string;
    description: string;
    contactOneFullName: string;
    contactOneTelephone: string;
    contactOneEmail: string;
    contactTwoFullName: string;
    contactTwoTelephone: string;
    contactTwoEmail: string;
    contactThreeFullName: string;
    contactThreeTelephone: string;
    contactThreeEmail: string;
    companyName: string;
    companyPhone: string;
    draftNumber: string;
    draughting: string;
    standardType: string;
}

export interface ComStdRequest {
    id: number;
    requestNumber: string;
    submissionDate: string;
    companyName: string;
    companyPhone: string;
    companyEmail: string;
    departmentName: string;
    productName: string;
    productSubCategoryName: string;
    departmentId: string;
    tcName: string;
    status: number;
    subject: string;
    description: string;
    contactOneFullName: string;
    contactOneTelephone: string;
    contactOneEmail: string;
    contactTwoFullName: string;
    contactTwoTelephone: string;
    contactTwoEmail: string;
    contactThreeFullName: string;
    contactThreeTelephone: string;
    contactThreeEmail: string;
}

export interface SchemeMembership {
    requestId: number;
    name: string;
    designationOccupation: string;
    address: string;
    email: string;
    phone: string;
    date: string;
    accountType: string;
    sicAssignedId: number;
    varField1: string;
    webStoreAccountCreationDate: Date;
    varField3: string;
    varField4: string;
    createdOn: string;
    sicAssignedDateAssigned: string;
    invoiceStatus: string;
    invoiceNumber: string;
    invoiceAmount: string;
    invoiceGeneratedDate: Date;
    invoicePaymentDate: Date;


}

export interface ComStdRemarks {
    id: number;
    requestId: number;
    remarks: string;
    remarkBy: string;
    status: number;
    role: string;
    description: string;
    dateOfRemark: Timestamp<any>;
}

export interface ComStdCommitteeRemarks {
    id: number;
    name: string;
    draftComment: string;
    commentTime: Timestamp<any>;
    requestID: number;
    draftID: number;
    commentTitle: string;
    commentDocumentType: string;
    nameOfOrganization: string;
    comClause: string;
    comParagraph: string;
    typeOfComment: string;
    proposedChange: string;
    adoptStandard: string;
    adoptDraft: string;
    reason: string;
    recommendations: string;
    nameOfRespondent: string;
    positionOfRespondent: string;
    scope: string;
    observation: string;
}

export interface CommentsOnCompanyStandard{
    id
    requestId
    remarks
    remarkBy
    status
    role
    description
    dateOfRemark
}

export interface TechnicalCommittee {
    id: number;
    technicalCommitteeNo: string;
    type: string;
    departmentId: number;
    tc: string;
    sc: string;
    wg: string;
    parentCommitte: string;
    title: string;
    status: number;
    comment: string;
    createdBy: number;
    createdOn: string;
    advertisingStatus: number;
}

export interface StandardBody{
    id : number;
    title : string;
    scope : string;
    normativeReference : string;
    symbolsAbbreviatedTerms : string;
    clause : string;
    special : string;
    standardNumber : string;
    status : string;
    standardType : string;
    description : string;
    dateFormed : Timestamp<any>;
    createdBy : string;
}

export interface NwaRequestList{
    id: number;
    requestNumber: string;
    rank: string;
    name: string;
    phone: string;
    email: string;
    submissionDate: Timestamp<any>;
    departmentId: string;
    productSubCategoryId: string;
    tcId: string;
    productId: string;
    organisationName: string;
    subject: string;
    description: string;
    economicEfficiency: string;
    healthSafety: string;
    environment: string;
    integration: string;
    exportMarkets: string;
    levelOfStandard: string;
    status: string;
    departmentName: string;
    tcSecAssigned: string;
}



