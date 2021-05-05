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
