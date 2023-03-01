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
export interface NwaEditPd{
    draftId: number;
    title: string;
    scope: string;
    normativeReference: string;
    symbolsAbbreviatedTerms: string;
    clause: string;
    special: string;
    uploadDate: Timestamp<any>;
    deadlineDate: Timestamp<any>;
    draftNumber: string;
    remarks: string;
    requestId: number;
    departmentId: string;
    standardType: string;
    workShopDate: string;
    requestNumber: string;
    rank: string;
    name: string;
    phone: string;
    email: string;
    submissionDate: Timestamp<any>;
    productSubCategoryId: number;
    tcId: number;
    productId: number;
    organisationName: string;
    subject: string;
    description: string;
    economicEfficiency: string;
    healthSafety: string;
    environment: string;
    integration: string;
    exportMarkets: string;
    levelOfStandard: string;
    departmentName: string;
    nwaCdNumber: string;

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
    workShopDate: Timestamp<any>;
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

export interface NepPrepareDraft{
    title: string;
    scope: string;
    normativeReference: string;
    symbolsAbbreviatedTerms: string;
    clause: string;
    special: string;
    typeOfNotification: string;
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
export interface DecisionOnStdDraft{
    comments: string;
    accentTo: string;
    requestId: number;
    id: number;
    departmentId: string;
    subject: string;
    description: string;
    requestNumber: string;
    title: string;
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
    standardNumber: string;
    standardType: string;
    documentType: string;
    dateFormed: Timestamp<any>;
    circulationDate: Timestamp<any>;
    edition: string;
    scope: string;
}
export interface XStandardsForReview {
    id: number;
    title: string;
    standardNumber: string;
    standardType: string;
    documentType: string;
    dateFormed: Timestamp<any>;
    circulationDate: Timestamp<any>;
    //closingDate: string;
    nameOfTcSecretary: string;
    edition: string;
    choice: string;
    justification: string;
    nameOfRespondent: string;
    positionOfRespondent: string;
    nameOfOrganization: string;
    date: Timestamp<any>;
    scope: string;
}
export interface SRProposalComments{
    id: string;
    standardNumber: string;
    title: string;
    documentType: string;
    preparedBy: string;
    datePrepared: Timestamp<any>;
    scope: string;
    normativeReference: string;
    symbolsAbbreviatedTerms: string;
    clause: string;
    special: string;
    standardType: string;
    assignedTo: number;
    standardId: string;
    circulationDate: Timestamp<any>;
    closingDate: Timestamp<any>;
    dateFormed: Timestamp<any>;
    tcSecretary: string;
    edition: string;
    operationOption: string;
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
export interface SRCommentsOnProposal{
    id: number;
    title: string;
    standardNumber: string;
    standardType: string;
    documentType: string;
    dateFormed: Timestamp<any>;
    circulationDate: Timestamp<any>;
    closingDate: Timestamp<any>;
    nameOfTcSecretary: string;
    edition: string;
    choice: string;
    justification: string;
    nameOfRespondent: string;
    positionOfRespondent: string;
    nameOfOrganization: string;
}
export interface SRStdForRecommendation{
    id: number;
    title: string;
    scope: string;
    normativeReference: string;
    symbolsAbbreviatedTerms: string;
    clause: string;
    special: string;
    standardNumber: string;
    standardType: string;
    dateFormed: Timestamp<any>;
    userName: string;
    adoptionComment: string;
    commentTime: string;
    proposalId: string;
    documentType: string;
    paragraph: string;
    typeOfComment: string;
    proposedChange: string;
    requestNumber: string;
    feedback : number;
    subject : string;
    description : string;
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
export interface ReviewSpcDecision{
    accentTo: string;
    remarks: string;
    reviewId: number;
    standardType: string;
    title: string;
    feedback: string;
    subject: string;
    description: string;
}

export interface ReviewRecommendation {
    proposalId: number;
    recommendation: string;
    processId: string;
    reviewId: number;
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
export interface SRStdComments{
    id : string;
    reviewId : string;
    standardId : string;
    title : string;
    standardNumber : string;
    documentType : string;
    dateFormed : Timestamp<any>;
    circulationDate : Timestamp<any>;
    closingDate : Timestamp<any>;
    nameOfTcSecretary : string;
    justification : string;
    edition : string;
    nameOfRespondent : string;
    positionOfRespondent : string;
    nameOfOrganization : string;
    commentTime : Timestamp<any>;
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
export interface NepRequests{
    id : number;
    requesterid : number;
    requesterName : string;
    requesterEmail : string;
    requesterPhone : string;
    requesterInstitution : string;
    requesterCountry : string;
    requesterSubject : string;
    requesterComment : string;
    docUploadStatus : number;
    requestDate : Timestamp<any>;


}

export interface NepEnquiries{
    id: number;
    requesterName: string;
    requesterEmail: string;
    requesterPhone: string;
    requesterInstitution: string;
    requesterCountry: string;
    requesterSubject: string;
    requesterComment: string;
    requestDate: Timestamp<any>;
    status: number;
    docUploadStatus: number;
}
export interface NepInfoDto{
    requesterid : number;
    requestId: number;
    requesterFeedBack: string;
    enquiryId: number;
    feedbackSent: string;
    requesterEmail: string;
    requesterSubject:string;

}

export interface NepInfoCheckDto {
    comments: string;
    accentTo: string;
    requestId: number;
    enquiryId: number;
    feedbackSent: string;
    requesterEmail: string;
    emailAddress: string;
    subject: string;
    docUploadStatus:number;

    requesterName: string;
    requesterPhone: string;
    requesterInstitution: string;
    requesterCountry: string;
    requesterSubject: string;
    requesterComment: string;
}

export interface Countries {
    code: string
    code3: string
    name: string
    number: string
}

export var countries: Countries [] = [
    { code: "AF", code3: "AFG", name: "Afghanistan", number: "004" },
    { code: "AL", code3: "ALB", name: "Albania", number: "008" },
    { code: "DZ", code3: "DZA", name: "Algeria", number: "012" },
    { code: "AS", code3: "ASM", name: "American Samoa", number: "016" },
    { code: "AD", code3: "AND", name: "Andorra", number: "020" },
    { code: "AO", code3: "AGO", name: "Angola", number: "024" },
    { code: "AI", code3: "AIA", name: "Anguilla", number: "660" },
    { code: "AQ", code3: "ATA", name: "Antarctica", number: "010" },
    { code: "AG", code3: "ATG", name: "Antigua and Barbuda", number: "028" },
    { code: "AR", code3: "ARG", name: "Argentina", number: "032" },
    { code: "AM", code3: "ARM", name: "Armenia", number: "051" },
    { code: "AW", code3: "ABW", name: "Aruba", number: "533" },
    { code: "AU", code3: "AUS", name: "Australia", number: "036" },
    { code: "AT", code3: "AUT", name: "Austria", number: "040" },
    { code: "AZ", code3: "AZE", name: "Azerbaijan", number: "031" },
    { code: "BS", code3: "BHS", name: "Bahamas (the)", number: "044" },
    { code: "BH", code3: "BHR", name: "Bahrain", number: "048" },
    { code: "BD", code3: "BGD", name: "Bangladesh", number: "050" },
    { code: "BB", code3: "BRB", name: "Barbados", number: "052" },
    { code: "BY", code3: "BLR", name: "Belarus", number: "112" },
    { code: "BE", code3: "BEL", name: "Belgium", number: "056" },
    { code: "BZ", code3: "BLZ", name: "Belize", number: "084" },
    { code: "BJ", code3: "BEN", name: "Benin", number: "204" },
    { code: "BM", code3: "BMU", name: "Bermuda", number: "060" },
    { code: "BT", code3: "BTN", name: "Bhutan", number: "064" },
    { code: "BO", code3: "BOL", name: "Bolivia (Plurinational State of)", number: "068" },
    { code: "BQ", code3: "BES", name: "Bonaire, Sint Eustatius and Saba", number: "535" },
    { code: "BA", code3: "BIH", name: "Bosnia and Herzegovina", number: "070" },
    { code: "BW", code3: "BWA", name: "Botswana", number: "072" },
    { code: "BV", code3: "BVT", name: "Bouvet Island", number: "074" },
    { code: "BR", code3: "BRA", name: "Brazil", number: "076" },
    { code: "IO", code3: "IOT", name: "British Indian Ocean Territory (the)", number: "086" },
    { code: "BN", code3: "BRN", name: "Brunei Darussalam", number: "096" },
    { code: "BG", code3: "BGR", name: "Bulgaria", number: "100" },
    { code: "BF", code3: "BFA", name: "Burkina Faso", number: "854" },
    { code: "BI", code3: "BDI", name: "Burundi", number: "108" },
    { code: "CV", code3: "CPV", name: "Cabo Verde", number: "132" },
    { code: "KH", code3: "KHM", name: "Cambodia", number: "116" },
    { code: "CM", code3: "CMR", name: "Cameroon", number: "120" },
    { code: "CA", code3: "CAN", name: "Canada", number: "124" },
    { code: "KY", code3: "CYM", name: "Cayman Islands (the)", number: "136" },
    { code: "CF", code3: "CAF", name: "Central African Republic (the)", number: "140" },
    { code: "TD", code3: "TCD", name: "Chad", number: "148" },
    { code: "CL", code3: "CHL", name: "Chile", number: "152" },
    { code: "CN", code3: "CHN", name: "China", number: "156" },
    { code: "CX", code3: "CXR", name: "Christmas Island", number: "162" },
    { code: "CC", code3: "CCK", name: "Cocos (Keeling) Islands (the)", number: "166" },
    { code: "CO", code3: "COL", name: "Colombia", number: "170" },
    { code: "KM", code3: "COM", name: "Comoros (the)", number: "174" },
    { code: "CD", code3: "COD", name: "Congo (the Democratic Republic of the)", number: "180" },
    { code: "CG", code3: "COG", name: "Congo (the)", number: "178" },
    { code: "CK", code3: "COK", name: "Cook Islands (the)", number: "184" },
    { code: "CR", code3: "CRI", name: "Costa Rica", number: "188" },
    { code: "HR", code3: "HRV", name: "Croatia", number: "191" },
    { code: "CU", code3: "CUB", name: "Cuba", number: "192" },
    { code: "CW", code3: "CUW", name: "Curaçao", number: "531" },
    { code: "CY", code3: "CYP", name: "Cyprus", number: "196" },
    { code: "CZ", code3: "CZE", name: "Czechia", number: "203" },
    { code: "CI", code3: "CIV", name: "Côte d'Ivoire", number: "384" },
    { code: "DK", code3: "DNK", name: "Denmark", number: "208" },
    { code: "DJ", code3: "DJI", name: "Djibouti", number: "262" },
    { code: "DM", code3: "DMA", name: "Dominica", number: "212" },
    { code: "DO", code3: "DOM", name: "Dominican Republic (the)", number: "214" },
    { code: "EC", code3: "ECU", name: "Ecuador", number: "218" },
    { code: "EG", code3: "EGY", name: "Egypt", number: "818" },
    { code: "SV", code3: "SLV", name: "El Salvador", number: "222" },
    { code: "GQ", code3: "GNQ", name: "Equatorial Guinea", number: "226" },
    { code: "ER", code3: "ERI", name: "Eritrea", number: "232" },
    { code: "EE", code3: "EST", name: "Estonia", number: "233" },
    { code: "SZ", code3: "SWZ", name: "Eswatini", number: "748" },
    { code: "ET", code3: "ETH", name: "Ethiopia", number: "231" },
    { code: "FK", code3: "FLK", name: "Falkland Islands (the) [Malvinas]", number: "238" },
    { code: "FO", code3: "FRO", name: "Faroe Islands (the)", number: "234" },
    { code: "FJ", code3: "FJI", name: "Fiji", number: "242" },
    { code: "FI", code3: "FIN", name: "Finland", number: "246" },
    { code: "FR", code3: "FRA", name: "France", number: "250" },
    { code: "GF", code3: "GUF", name: "French Guiana", number: "254" },
    { code: "PF", code3: "PYF", name: "French Polynesia", number: "258" },
    { code: "TF", code3: "ATF", name: "French Southern Territories (the)", number: "260" },
    { code: "GA", code3: "GAB", name: "Gabon", number: "266" },
    { code: "GM", code3: "GMB", name: "Gambia (the)", number: "270" },
    { code: "GE", code3: "GEO", name: "Georgia", number: "268" },
    { code: "DE", code3: "DEU", name: "Germany", number: "276" },
    { code: "GH", code3: "GHA", name: "Ghana", number: "288" },
    { code: "GI", code3: "GIB", name: "Gibraltar", number: "292" },
    { code: "GR", code3: "GRC", name: "Greece", number: "300" },
    { code: "GL", code3: "GRL", name: "Greenland", number: "304" },
    { code: "GD", code3: "GRD", name: "Grenada", number: "308" },
    { code: "GP", code3: "GLP", name: "Guadeloupe", number: "312" },
    { code: "GU", code3: "GUM", name: "Guam", number: "316" },
    { code: "GT", code3: "GTM", name: "Guatemala", number: "320" },
    { code: "GG", code3: "GGY", name: "Guernsey", number: "831" },
    { code: "GN", code3: "GIN", name: "Guinea", number: "324" },
    { code: "GW", code3: "GNB", name: "Guinea-Bissau", number: "624" },
    { code: "GY", code3: "GUY", name: "Guyana", number: "328" },
    { code: "HT", code3: "HTI", name: "Haiti", number: "332" },
    { code: "HM", code3: "HMD", name: "Heard Island and McDonald Islands", number: "334" },
    { code: "VA", code3: "VAT", name: "Holy See (the)", number: "336" },
    { code: "HN", code3: "HND", name: "Honduras", number: "340" },
    { code: "HK", code3: "HKG", name: "Hong Kong", number: "344" },
    { code: "HU", code3: "HUN", name: "Hungary", number: "348" },
    { code: "IS", code3: "ISL", name: "Iceland", number: "352" },
    { code: "IN", code3: "IND", name: "India", number: "356" },
    { code: "ID", code3: "IDN", name: "Indonesia", number: "360" },
    { code: "IR", code3: "IRN", name: "Iran (Islamic Republic of)", number: "364" },
    { code: "IQ", code3: "IRQ", name: "Iraq", number: "368" },
    { code: "IE", code3: "IRL", name: "Ireland", number: "372" },
    { code: "IM", code3: "IMN", name: "Isle of Man", number: "833" },
    { code: "IL", code3: "ISR", name: "Israel", number: "376" },
    { code: "IT", code3: "ITA", name: "Italy", number: "380" },
    { code: "JM", code3: "JAM", name: "Jamaica", number: "388" },
    { code: "JP", code3: "JPN", name: "Japan", number: "392" },
    { code: "JE", code3: "JEY", name: "Jersey", number: "832" },
    { code: "JO", code3: "JOR", name: "Jordan", number: "400" },
    { code: "KZ", code3: "KAZ", name: "Kazakhstan", number: "398" },
    { code: "KE", code3: "KEN", name: "Kenya", number: "404" },
    { code: "KI", code3: "KIR", name: "Kiribati", number: "296" },
    { code: "KP", code3: "PRK", name: "Korea (the Democratic People's Republic of)", number: "408" },
    { code: "KR", code3: "KOR", name: "Korea (the Republic of)", number: "410" },
    { code: "KW", code3: "KWT", name: "Kuwait", number: "414" },
    { code: "KG", code3: "KGZ", name: "Kyrgyzstan", number: "417" },
    { code: "LA", code3: "LAO", name: "Lao People's Democratic Republic (the)", number: "418" },
    { code: "LV", code3: "LVA", name: "Latvia", number: "428" },
    { code: "LB", code3: "LBN", name: "Lebanon", number: "422" },
    { code: "LS", code3: "LSO", name: "Lesotho", number: "426" },
    { code: "LR", code3: "LBR", name: "Liberia", number: "430" },
    { code: "LY", code3: "LBY", name: "Libya", number: "434" },
    { code: "LI", code3: "LIE", name: "Liechtenstein", number: "438" },
    { code: "LT", code3: "LTU", name: "Lithuania", number: "440" },
    { code: "LU", code3: "LUX", name: "Luxembourg", number: "442" },
    { code: "MO", code3: "MAC", name: "Macao", number: "446" },
    { code: "MG", code3: "MDG", name: "Madagascar", number: "450" },
    { code: "MW", code3: "MWI", name: "Malawi", number: "454" },
    { code: "MY", code3: "MYS", name: "Malaysia", number: "458" },
    { code: "MV", code3: "MDV", name: "Maldives", number: "462" },
    { code: "ML", code3: "MLI", name: "Mali", number: "466" },
    { code: "MT", code3: "MLT", name: "Malta", number: "470" },
    { code: "MH", code3: "MHL", name: "Marshall Islands (the)", number: "584" },
    { code: "MQ", code3: "MTQ", name: "Martinique", number: "474" },
    { code: "MR", code3: "MRT", name: "Mauritania", number: "478" },
    { code: "MU", code3: "MUS", name: "Mauritius", number: "480" },
    { code: "YT", code3: "MYT", name: "Mayotte", number: "175" },
    { code: "MX", code3: "MEX", name: "Mexico", number: "484" },
    { code: "FM", code3: "FSM", name: "Micronesia (Federated States of)", number: "583" },
    { code: "MD", code3: "MDA", name: "Moldova (the Republic of)", number: "498" },
    { code: "MC", code3: "MCO", name: "Monaco", number: "492" },
    { code: "MN", code3: "MNG", name: "Mongolia", number: "496" },
    { code: "ME", code3: "MNE", name: "Montenegro", number: "499" },
    { code: "MS", code3: "MSR", name: "Montserrat", number: "500" },
    { code: "MA", code3: "MAR", name: "Morocco", number: "504" },
    { code: "MZ", code3: "MOZ", name: "Mozambique", number: "508" },
    { code: "MM", code3: "MMR", name: "Myanmar", number: "104" },
    { code: "NA", code3: "NAM", name: "Namibia", number: "516" },
    { code: "NR", code3: "NRU", name: "Nauru", number: "520" },
    { code: "NP", code3: "NPL", name: "Nepal", number: "524" },
    { code: "NL", code3: "NLD", name: "Netherlands (the)", number: "528" },
    { code: "NC", code3: "NCL", name: "New Caledonia", number: "540" },
    { code: "NZ", code3: "NZL", name: "New Zealand", number: "554" },
    { code: "NI", code3: "NIC", name: "Nicaragua", number: "558" },
    { code: "NE", code3: "NER", name: "Niger (the)", number: "562" },
    { code: "NG", code3: "NGA", name: "Nigeria", number: "566" },
    { code: "NU", code3: "NIU", name: "Niue", number: "570" },
    { code: "NF", code3: "NFK", name: "Norfolk Island", number: "574" },
    { code: "MP", code3: "MNP", name: "Northern Mariana Islands (the)", number: "580" },
    { code: "NO", code3: "NOR", name: "Norway", number: "578" },
    { code: "OM", code3: "OMN", name: "Oman", number: "512" },
    { code: "PK", code3: "PAK", name: "Pakistan", number: "586" },
    { code: "PW", code3: "PLW", name: "Palau", number: "585" },
    { code: "PS", code3: "PSE", name: "Palestine, State of", number: "275" },
    { code: "PA", code3: "PAN", name: "Panama", number: "591" },
    { code: "PG", code3: "PNG", name: "Papua New Guinea", number: "598" },
    { code: "PY", code3: "PRY", name: "Paraguay", number: "600" },
    { code: "PE", code3: "PER", name: "Peru", number: "604" },
    { code: "PH", code3: "PHL", name: "Philippines (the)", number: "608" },
    { code: "PN", code3: "PCN", name: "Pitcairn", number: "612" },
    { code: "PL", code3: "POL", name: "Poland", number: "616" },
    { code: "PT", code3: "PRT", name: "Portugal", number: "620" },
    { code: "PR", code3: "PRI", name: "Puerto Rico", number: "630" },
    { code: "QA", code3: "QAT", name: "Qatar", number: "634" },
    { code: "MK", code3: "MKD", name: "Republic of North Macedonia", number: "807" },
    { code: "RO", code3: "ROU", name: "Romania", number: "642" },
    { code: "RU", code3: "RUS", name: "Russian Federation (the)", number: "643" },
    { code: "RW", code3: "RWA", name: "Rwanda", number: "646" },
    { code: "RE", code3: "REU", name: "Réunion", number: "638" },
    { code: "BL", code3: "BLM", name: "Saint Barthélemy", number: "652" },
    { code: "SH", code3: "SHN", name: "Saint Helena, Ascension and Tristan da Cunha", number: "654" },
    { code: "KN", code3: "KNA", name: "Saint Kitts and Nevis", number: "659" },
    { code: "LC", code3: "LCA", name: "Saint Lucia", number: "662" },
    { code: "MF", code3: "MAF", name: "Saint Martin (French part)", number: "663" },
    { code: "PM", code3: "SPM", name: "Saint Pierre and Miquelon", number: "666" },
    { code: "VC", code3: "VCT", name: "Saint Vincent and the Grenadines", number: "670" },
    { code: "WS", code3: "WSM", name: "Samoa", number: "882" },
    { code: "SM", code3: "SMR", name: "San Marino", number: "674" },
    { code: "ST", code3: "STP", name: "Sao Tome and Principe", number: "678" },
    { code: "SA", code3: "SAU", name: "Saudi Arabia", number: "682" },
    { code: "SN", code3: "SEN", name: "Senegal", number: "686" },
    { code: "RS", code3: "SRB", name: "Serbia", number: "688" },
    { code: "SC", code3: "SYC", name: "Seychelles", number: "690" },
    { code: "SL", code3: "SLE", name: "Sierra Leone", number: "694" },
    { code: "SG", code3: "SGP", name: "Singapore", number: "702" },
    { code: "SX", code3: "SXM", name: "Sint Maarten (Dutch part)", number: "534" },
    { code: "SK", code3: "SVK", name: "Slovakia", number: "703" },
    { code: "SI", code3: "SVN", name: "Slovenia", number: "705" },
    { code: "SB", code3: "SLB", name: "Solomon Islands", number: "090" },
    { code: "SO", code3: "SOM", name: "Somalia", number: "706" },
    { code: "ZA", code3: "ZAF", name: "South Africa", number: "710" },
    { code: "GS", code3: "SGS", name: "South Georgia and the South Sandwich Islands", number: "239" },
    { code: "SS", code3: "SSD", name: "South Sudan", number: "728" },
    { code: "ES", code3: "ESP", name: "Spain", number: "724" },
    { code: "LK", code3: "LKA", name: "Sri Lanka", number: "144" },
    { code: "SD", code3: "SDN", name: "Sudan (the)", number: "729" },
    { code: "SR", code3: "SUR", name: "Suriname", number: "740" },
    { code: "SJ", code3: "SJM", name: "Svalbard and Jan Mayen", number: "744" },
    { code: "SE", code3: "SWE", name: "Sweden", number: "752" },
    { code: "CH", code3: "CHE", name: "Switzerland", number: "756" },
    { code: "SY", code3: "SYR", name: "Syrian Arab Republic", number: "760" },
    { code: "TW", code3: "TWN", name: "Taiwan", number: "158" },
    { code: "TJ", code3: "TJK", name: "Tajikistan", number: "762" },
    { code: "TZ", code3: "TZA", name: "Tanzania, United Republic of", number: "834" },
    { code: "TH", code3: "THA", name: "Thailand", number: "764" },
    { code: "TL", code3: "TLS", name: "Timor-Leste", number: "626" },
    { code: "TG", code3: "TGO", name: "Togo", number: "768" },
    { code: "TK", code3: "TKL", name: "Tokelau", number: "772" },
    { code: "TO", code3: "TON", name: "Tonga", number: "776" },
    { code: "TT", code3: "TTO", name: "Trinidad and Tobago", number: "780" },
    { code: "TN", code3: "TUN", name: "Tunisia", number: "788" },
    { code: "TR", code3: "TUR", name: "Turkey", number: "792" },
    { code: "TM", code3: "TKM", name: "Turkmenistan", number: "795" },
    { code: "TC", code3: "TCA", name: "Turks and Caicos Islands (the)", number: "796" },
    { code: "TV", code3: "TUV", name: "Tuvalu", number: "798" },
    { code: "UG", code3: "UGA", name: "Uganda", number: "800" },
    { code: "UA", code3: "UKR", name: "Ukraine", number: "804" },
    { code: "AE", code3: "ARE", name: "United Arab Emirates (the)", number: "784" },
    { code: "GB", code3: "GBR", name: "United Kingdom of Great Britain and Northern Ireland (the)", number: "826" },
    { code: "UM", code3: "UMI", name: "United States Minor Outlying Islands (the)", number: "581" },
    { code: "US", code3: "USA", name: "United States of America (the)", number: "840" },
    { code: "UY", code3: "URY", name: "Uruguay", number: "858" },
    { code: "UZ", code3: "UZB", name: "Uzbekistan", number: "860" },
    { code: "VU", code3: "VUT", name: "Vanuatu", number: "548" },
    { code: "VE", code3: "VEN", name: "Venezuela (Bolivarian Republic of)", number: "862" },
    { code: "VN", code3: "VNM", name: "Viet Nam", number: "704" },
    { code: "VG", code3: "VGB", name: "Virgin Islands (British)", number: "092" },
    { code: "VI", code3: "VIR", name: "Virgin Islands (U.S.)", number: "850" },
    { code: "WF", code3: "WLF", name: "Wallis and Futuna", number: "876" },
    { code: "EH", code3: "ESH", name: "Western Sahara", number: "732" },
    { code: "YE", code3: "YEM", name: "Yemen", number: "887" },
    { code: "ZM", code3: "ZMB", name: "Zambia", number: "894" },
    { code: "ZW", code3: "ZWE", name: "Zimbabwe", number: "716" },
    { code: "AX", code3: "ALA", name: "Åland Islands", number: "248" }
];

export interface NepDraftView{
    id: number;
    title: string;
    scope: string;
    normativeReference: string;
    symbolsAbbreviatedTerms: string;
    clause: string;
    special: string;
    preparedBy: number;
    status: number;
    datePrepared: Timestamp<any>;
    notification: string;
    typeOfNotification: string;
    uploadDocument: number;
}

export interface DecisionOnNotification{
    draftId: number;
    remarks: string;
    accentTo: string;
    notification: string;
    status: number;
}



