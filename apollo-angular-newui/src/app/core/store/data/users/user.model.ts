export class User {
  id = null;
    firstName = '';
    lastName = '';
  userName = '';
  email = '';
  credentials = '';
  cellphone = '';
  companyId = -1;
  plantId = -1;
}

export class UserEntityDto {
    id: bigint;
    firstName: string;
    lastName: string;
    userName: string;
    userPinIdNumber: string;
    personalContactNumber: string;
    typeOfUser: number;
    email: string;
    userRegNo: string;
    enabled: boolean;
    accountExpired: boolean;
    accountLocked: boolean;
    credentialsExpired: boolean;
    status: boolean;
    registrationDate: Date;
    userType: bigint;
    title: bigint;
    directorate: bigint;
    department: bigint;
    division: bigint;
    section: bigint;
    l1SubSubSection: bigint;
    l2SubSubSection: bigint;
    designation: bigint;
    profileId: bigint;
    region: bigint;
    county: bigint;
    town: bigint;
    subRegion: bigint;
}

class EmployeeProfileDetailsDto {
    directorate: string;
    department: string;
    division: string;
    section: string;
    l1SubSubSection: string;
    l2SubSubSection: string;
    designation: string;
    profileId: bigint;
    region: string;
    county: string;
    town: string;
    status: boolean;
}

class UserCompanyDto {
    id: bigint;
    name: string;
    kraPin: string;
    userId: bigint;
    registrationNumber: string;
    postalAddress: string;
    physicalAddress: string;
    plotNumber: string;
    companyEmail: string;
    companyTelephone: string;
    yearlyTurnover: number;
    businessLines: string;
    businessNatures: string;
    buildingName: string;
    streetName: string;
    region: string;
    county: string;
    town: string;
    factoryVisitDate: Date;
    factoryVisitStatus: number;
    manufactureStatus: number;
}

export class UserDetailsDto {
    id: bigint;
    firstName: string;
    lastName: string;
    userName: string;
    email: string;
    userPinIdNumber: string;
    personalContactNumber: string;
    typeOfUser: number;
    userRegNo: string;
    enabled: boolean;
    accountExpired: boolean;
    accountLocked: boolean;
    credentialsExpired: boolean;
    status: boolean;
    registrationDate: Date;
    title: string;
    employeeProfile: EmployeeProfileDetailsDto;
    companyProfile: UserCompanyDto;
}

