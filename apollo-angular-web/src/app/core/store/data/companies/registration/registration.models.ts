import {User} from "../../users";
import {Company} from "../company";

export interface RegistrationPayload {
  company: Company;
  user: User;
}

export interface BrsLookUpRequest {
  registrationNumber: String;
  directorIdNumber: String;
}

export interface SendTokenToPhone {
  phone: string;
}

export interface ValidateTokenAndPhone {
  phone: string;
  token: string;
}
