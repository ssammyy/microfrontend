import {SacSeCTaskData} from "./standard-development";

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
