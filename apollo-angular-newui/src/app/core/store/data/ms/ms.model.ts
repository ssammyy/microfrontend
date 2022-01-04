export class Ms {
}

export class FuelBatchDetailsDto {
    id: number;
    region: string;
    county: string;
    town: string;
    referenceNumber: string;
    batchFileYear: string;
    remarks: string;
    batchClosed: boolean;
}

export class FuelEntityDto {
    company: string;
    petroleumProduct: string;
    physicalLocation: string;
    inspectionDateFrom: Date;
    inspectionDateTo: Date;
    remarks: string;
}

export class BatchFileFuelSaveDto {
    county: bigint;
    town: bigint;
    remarks: string;
}

export class FuelEntityAssignOfficerDto {
    assignedUserID: bigint;
    remarks: string;
}

export class FuelEntityRapidTestDto {
    rapidTestRemarks: string;
    rapidTestStatus: boolean;
}

export class FuelInspectionDto {
    id : bigint;
    referenceNumber : string;
    company : string;
    petroleumProduct : string;
    physicalLocation : string;
    inspectionDateFrom : Date;
    inspectionDateTo : Date;
    officersList : MsUsersDto[];
    officersAssigned : MsUsersDto;
    rapidTestStatus : Boolean;
    rapidTestRemarks : string;
    sampleCollected : SampleCollectionDto[];
    sampleSubmitted : SampleSubmissionDto;
    sampleLabResults : LabResultsDto;
}

export class LabResultsDto {
    parametersListTested : LabResultsParamDto[];
    result : string;
    method : string;
}

export class LabResultsParamDto {
    param: string;
    result: string;
    method: string;
}

export class SampleSubmissionDto {
    nameProduct : string;
    packaging : string;
    labellingIdentification : string;
    fileRefNumber : string;
    referencesStandards : string;
    sizeTestSample : bigint;
    sizeRefSample : bigint;
    condition : string;
    sampleReferences : string;
    sendersName : string;
    designation : string;
    address : string;
    sendersDate : Date;
    receiversName : string;
    testChargesKsh : number;
    receiptLpoNumber : string;
    invoiceNumber : string;
    disposal : string;
    remarks : string;
    sampleCollectionNumber : bigint;
    bsNumber : string;
    parametersList: SampleSubmissionItemsDto[];
}

export class SampleSubmissionItemsDto {
    parameters : string;
    laboratoryName : string;
}

export class SampleCollectionDto {
    nameManufacturerTrader: string;
    addressManufacturerTrader: string;
    samplingMethod: string;
    reasonsCollectingSamples: string;
    anyRemarks: string;
    designationOfficerCollectingSample: string;
    nameOfficerCollectingSample: string;
    dateOfficerCollectingSample: Date;
    nameWitness: string;
    designationWitness: string;
    dateWitness: Date;
    productsList: SampleCollectionItemsDto[];
}

export class SampleCollectionItemsDto {
    id: bigint;
    productBrandName: string;
    batchNo: string;
    batchSize: string;
    sampleSize: string;
}

export class MsUsersDto {
    id: bigint;
    firstName: string;
    lastName: string;
    userName: string;
    email: string;
    status: boolean;
}
