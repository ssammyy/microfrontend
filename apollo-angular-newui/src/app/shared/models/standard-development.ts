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

export interface TechnicalCommittee{
  id: number;
  technical_committee_no: string;

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
}

export interface NWADiSdtJustification{
  id: number;
  cost: number;
  numberOfMeetings: number;
  identifiedNeed: string;
  dateOfApproval: string;
  taskId: string;
}

export interface DISDTTasks{
  taskId: string;
  name: string;
  taskData: DisDTTaskData;
}
export interface DisDTTaskData {

  requestedBy: string;
  requestNumber: string;
  referenceMaterial: string;
  knw: string;
  meetingDate: string;
  sl: string;
  department: string;
  issuesAddressed: string;
  knwAcceptanceDate: Date;

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

  title: number;
  scope: number;
  normativeReference: string;
  symbolsAbbreviatedTerms: string;
  special: string;
  taskId: string;
  clause: string;
  referenceMaterial: string;

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
}

export interface Notification {
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
export interface InfoAvailableYes{
  taskId: string;
  isAvailable: string;
}

export interface InfoAvailableNo{
  taskId: string;
  isAvailable: string;
}



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

