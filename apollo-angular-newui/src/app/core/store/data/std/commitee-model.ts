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

export interface PublicReviewDraft {
    id: number;
    prdName: string;
    prdraftBy: string;
    prdpath: string;


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
