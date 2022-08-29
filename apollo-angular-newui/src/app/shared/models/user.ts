import {UserCompanyDetailsDto} from './master-data-details';

export class User {
  accessToken?: string;
  id?: bigint;
  username?: string;
  email?: string;
  fullName?: string;
  roles!: Array<string>;
}

export class UserRegister {
  id?: bigint;
  firstName?: string;
  lastName?: string;
  userName?: string;
  personalContactNumber?: string;
  userPinIdNumber?: string;
  typeOfUser?: number;
  email?: string;
  userRegNo?: string;
  enabled?: boolean;
  accountExpired?: boolean;
  accountLocked?: boolean;
  credentialsExpired?: boolean;
  status?: boolean;
  registrationDate?: Date;
  userType?: bigint;
  title?: string;
  employeeProfile?: EmployeeProfile;
  companyProfile?: UserCompanyDetailsDto;
}

export class EmployeeProfile {
  directorate?: string;
  directorateID?: number;
  department?: string;
  departmentID?: number;
  division?: string;
  divisionID?: number;
  section?: string;
  sectionID?: number;
  l1SubSubSection?: string;
  l1SubSubSectionID?: number;
  l2SubSubSection?: string;
  l2SubSubSectionID?: number;
  designation?: string;
  designationID?: number;
  profileId?: bigint;
  region?: string;
  regionID?: number;
  county?: string;
  countyID?: number;
  town?: string;
  townID?: number;
  status?: boolean;
}

