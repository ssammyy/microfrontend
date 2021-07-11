export class User {
  id:number = -1;
    firstName = '';
    lastName = '';
    userName = '';
    email = '';
    credentials = '';
    cellphone = '';
    companyId?: number = -1;
    plantId?: number = -1;
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

