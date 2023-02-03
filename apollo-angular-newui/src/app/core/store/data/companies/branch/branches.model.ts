export interface Branches {
    id: number;
    companyProfileId: number;
    location: string;
    street: string;
    branchName: string;
    buildingName: string;
    nearestLandMark: string;
    postalAddress: string;
    telephone: string;
    emailAddress: string;
    physicalAddress: string;
    faxNo: string;
    plotNo: string;
    designation: string;
    contactPerson: string;
    status: boolean;
    descriptions: string;
    region: number;
    county: number;
    town: number;
    inspectionFeeStatus: boolean ;
    paidDate: Date;
    endingDate: Date;
    invoiceSharedId: number;
    tokenGiven: string;
    invoiceBatchID: number;
    uploadedPdfID: number;
}
