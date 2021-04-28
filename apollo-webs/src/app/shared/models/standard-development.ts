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


