import {User} from "../users";

export interface Company {
  name: string;
  kraPin: string;
  registrationNumber: string;
  postalAddress: string;
  physicalAddress: string;
  plotNumber: string;
  companyEmail: string;
  companyTelephone: string;
  yearlyTurnover: number;
  businessLines: number;
  businessNatures: number;
  buildingName: string;
  streetName: string;
  directorIdNumber: string;
  region: number;
  county: number;
  town: number;

}

export interface RegistrationPayload {
  company: Company;
  user: User;
}

export interface BrsLookUpRequest{
 registrationNumber: String;
  directorIdNumber: String;
}

export interface SendTokenToPhone{
  phone: string;
}
export interface ValidateTokenAndPhone{
  phone: string;
  token: string;
}
