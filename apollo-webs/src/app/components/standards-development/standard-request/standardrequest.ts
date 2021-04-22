import {Timestamp} from "rxjs";

export interface StandardRequest {
  id : number;
  requestNumber: string;
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

export interface Department{
  id: number;
  name: string;

}

export interface TechnicalCommittee{
  id: number;
  technical_committee_no: string;

}
