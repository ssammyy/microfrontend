export interface BusinessNatures {
  id: number;
  businessLinesId: BusinessLine;
  name: string;
  descriptions: string;
  status: boolean;
}

export interface BusinessLine {
  id: number,
  name: string,

}
