export interface Company {
    id: number;
    name: string;
    kraPin: string;
    status: number;
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
    branchName: string;
    streetName: string;
    directorIdNumber: string;
    region: number;
    county: number;
    town: number;
    otherCategory: string;
    otherBusinessNatureType: string;
    firmType: string;
}


export interface TivetEntity
{
    id:number,
    name: string,
    physicalAddress: string,
    kraPin: string,
    manufactureStatus: number,
    registrationNumber: string,
    postalAddress: string,
    plotNumber: string,
    companyEmail: string,
    companyTelephone: string,
    yearlyTurnover: number,
    businessLines: number,
    businessNatures: number,
    buildingName: string,
    userClassification: null,
    streetName:string,
    region: number,
    county: number,
    firmCategory: number,
    editStatus: number,
    town: number,
    userId: number,
    description: null,
    directorIdNumber: null,
    entryNumber: null,
    status: number,
    suspensionStatus: null,
    closureStatus: null,
    closedCommodityManufactured: null,
    closedContractsUndertaken: null,
    varField1: string,
    varField2: string,
    varField3: null,
    varField4: null,
    varField5: null,
    varField6: null,
    varField7: null,
    varField8: null,
    varField9: null,
    varField10: null,
    accentTo: null,
    taskId: null,
    taskType: null,
    assignStatus: null,
    assignedTo: null,
    createdBy:string,
    createdOn: string,
    modifiedBy: null,
    modifiedOn: null,
    deleteBy: null,
    deletedOn: null,
    factoryVisitDate: null,
    factoryVisitStatus: null,
    ownership: null,
    slBpmnProcessInstance: null,
    branchName: string,
    closureOfOperations: null,
    typeOfManufacture: null,
    businessLineName: null,
    businessNatureName: null,
    regionName: null,
    countyName: null,
    townName: null,
    otherBusinessNatureType: null,
    slFormStatus: number
}
