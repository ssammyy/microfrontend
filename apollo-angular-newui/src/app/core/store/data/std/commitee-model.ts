export interface ApprovedNwiS {
    workplan_id: bigint;
    stage_Code: string;
    stage_Date: string;
    stage_Month: string;
    sub_Stage: string;
    target_Date: string;
    title: string;
    request_No: string;
    status: string;
    id: string;
    created_On: string;
    deleted_On: string;
    modified_On: string;
    name_Of_Proposer: string;
    proposal_Title: string;
    circulation_Date: string;
    closing_Date: string;
    date_Of_Presentation: string;
    draft_Attached: string;
    draft_Outline_Impossible: string;
    liason_Organization: string;
    name_Of_Tc: string;
    organization: string;
    outline_Attached: string;
    outline_Sent_Later: string;
    purpose: string;
    reference_Number: string;
    scope: string;
    similar_Standards: string;
    target_Dateb: string;
    liason_Organizationb: string;
}


export interface Preliminary_Draft {
    id: number;
    slNo: number;
    pdName: string;
    pdBy: string;
    commentsNo: string;
}

export interface Preliminary_Draft_With_Name {
    id: number;
    nwi_Id: number;
    pd_name: string;
    pd_by: string;
    status: string;
    number_OF_COMMENTS: string;

    organization: string;
    proposal_TITLE: string;
    circulation_DATE: string;
    closing_DATE: string;
}

export interface TaskData {
    id: number;
    slNo: string;
    reference: string;
    ta: string;
    ed: string;
    title: string;
    stage_date: string;


}

export interface New_work_item {
    id: number;
    slNo: string;
    reference: string;
    ta: string;
    ed: string;
    title: string;
    stage_date: string;
}

export interface Committee_Draft {
    id: number;
    slNo: number;
    cdNo: number;
    pdName: string;
    cdName: string;
    cdBy: string;


}

export interface Committee_Draft_With_Name {

    id: number;
    cdid: number;
    pdid: number;
    cdname: string;
    cdby: string;
    status: string;
    createdon: string;
    approvalstatus: string;
    numberofcomments: string;
    ks_NUMBER: string;
    organization: string;
    proposal_TITLE: string;
    circulation_DATE: string;
    closing_DATE: string;

}

export interface PublicReviewDraft {
    id: number;
    prdName: string;
    prdBy: string;
    status: string;
}

export interface PublicReviewDraftWithName {
    id: number;
    cd_Id: number;
    prd_name: string;
    prd_by: string;
    status: string;
    created_on: string;
    number_OF_COMMENTS: string;
    ks_NUMBER: string;
    var_FIELD_1: string;

    organization: string;
    proposal_TITLE: string;
    circulation_DATE: string;
    closing_DATE: string;


}

export interface PublicReviewDraftComment {
    prdId: number;
    roleId: number;
    roleName: number;
    userId: number;
    title: string;
    documentType: string;
    circulationDate: string;
    closingDate: string;
    organization: string;
    clause: string;
    commentType: string;
    comment: string;
    proposedChange: string;
    observations: string;


}

export interface TCTasks {
    taskId: string;
    name: string;
    taskData: PublicReviewDraft;
}

export interface CommentMade {
    id: number;
    userId: number;
    pdId: number;
    cdId: number;
    prdId: number;
    recipientId: number;
    title: string;
    documentType: string;
    circulationDate: Date;
    closingDate: Date;
    organization: string;
    clause: string;
    paragraph: string;
    commentType: string;
    proposedChange: string;
    observation: string;
    status: string;
    createdOn: Date;
    commentsMade: string;


}

export interface CommentMadeRetrieved {
    title: string,
    commentsId: number,
    document_type: string,
    circulation_date: string,
    closing_date: string,
    organization: string,
    paragraph: string,
    comment_type: string,
    proposed_change: string,
    observation: string,
    created_on: string,
    comments_made: string,
    comment_by: string,
    received_by: string,
    status: number,
    clause: string,
    pd_name: string
    recipient_id: number;
    cd_name: string;
    prd_name: string;

}

export interface StandardDocuments {
    id: number,
    documentType: string,
    description: string,
    sdDocumentId: number,
    name: string,
    documentTypeDef: string
}


export interface Ballot_Draft {
    id: number;
    slNo: number;
    cdNo: number;
    pdName: string;
    cdName: string;
    cdBy: string;
    approvalStatus: string;
    status: string;
    prdID: number;

}

export interface Ballot_Draft_With_Name {
    id: number;
    prd_id: number;
    ballot_name: string;
    cdby: string;
    status: string;
    createdon: string;
    number_OF_COMMENTS: string;

}

export interface Vote {
    id: number;
    ballotId: number;
    userId: number;
    approvalStatus: number;
    comment: string;
    status: string;
    createdOn: Date;


}

export interface VoteRetrieved {
    ballotId: number;
    userId: number;
    approval_STATUS: number;
    comments_BY: string;
    status: string;
    created_on: Date;
    ballot_name: string;
    comments: string;


}

export interface VotesTally {
    ballot_ID: number;
    ballotname: string;
    approved: number;
    approvedwithcomments: number;
    disapproved: number;
    abstention: number;
    status: string;
    prdID: number;


}

export interface VotesNwiTally {
    approved: number;
    notapproved: number;
    nwi_ID: number;
    nwiname: string;
    status: string

    reference_NUMBER: string;
}

export interface VoteNwiRetrieved {
    vote_By: string
    created_On: Date
    decision: string
    organization: string
    position: string
    proposal_Title: string
    reason: string

    reference_Number: string


}

