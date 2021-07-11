export class Qa {
}

export class PermitEntityDto {
    id: bigint;
    firmName: string;
    permitRefNumber: string;
    productName: string;
    tradeMark: string;
    awardedPermitNumber: string;
    dateOfIssue: Date;
    dateOfExpiry: Date;
    permitStatus: string;
    userId: bigint;
    createdOn: Date;
    county: string;
    town: string;
    region: string;
    divisionValue: string;
    sectionValue: string;
    permitAwardStatus: boolean;
    permitExpiredStatus: boolean;
    taskID: bigint;
    companyId: bigint;
    permitType: bigint;
}

