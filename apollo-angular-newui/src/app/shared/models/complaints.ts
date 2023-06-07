import {DivisionDetails, UserDetails} from './master-data-details';

export class Complaints {
  refNumber!: string;
  complainantName?: string;
  complainantEmail?: string;
  complainantPhoneNumber?: string;
  complainantPostalAddress?: string;
  complaintCategory?: string;
  complaintTitle?: string;
  complaintDescription?: string;
  broadProductCategory?: string;
  productClassification?: string;
  productSubcategory?: string;
  productName?: string;
  productBrand?: string;
  county?: string;
  town?: string;
  marketCenter?: string;
  buildingName?: string;
  date?: string;
  status?: string;
  officersList?: UserDetails[];
  divisionList?: DivisionDetails[];
  approvedStatus?: number;
  assignedIOStatus?: number;
  rejectedStatus?: number;
}


export class FindWithRefNumber {
  refNumber?: string;
}

