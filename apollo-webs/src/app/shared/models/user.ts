
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
}

export class EmployeeProfile {
  directorate?: string;
  department?: string;
  division?: string;
  section?: string;
  l1SubSubSection?: string;
  l2SubSubSection?: string;
  designation?: string;
  profileId?: bigint;
  region?: string;
  county?: string;
  town?: string;
  status?: boolean;
}

