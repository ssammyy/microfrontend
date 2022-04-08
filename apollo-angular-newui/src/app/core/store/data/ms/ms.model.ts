import {County} from '../county';

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

export class LaboratoryDto {
    id: number;
    labName: string;
    description: string;
    status: boolean;
}

export class FuelEntityDto {
    company: string;
    petroleumProduct: string;
    physicalLocation: string;
    inspectionDateFrom: Date;
    inspectionDateTo: Date;
    stationOwnerEmail: string;
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

export class CompliantRemediationDto {
    proFormaInvoiceStatus: boolean;
    dateOfRemediation: Date;
    remarks: string;
    volumeFuelRemediated: number;
    subsistenceTotalNights: number;
    transportAirTicket: number;
    transportInkm: number;
}

export class RemediationDto {
    productType: string;
    quantityOfFuel: string;
    contaminatedFuelType: string;
    applicableKenyaStandard: string;
    remediationProcedure: string;
    volumeOfProductContaminated: string;
    volumeAdded: string;
    totalVolume: string;
}


export class FuelEntityRapidTestDto {
    rapidTestRemarks: string;
    rapidTestStatus: boolean;
}

export class FuelInspectionScheduleListDetailsDto {
    fuelInspectionDto: FuelInspectionDto[];
    fuelBatchDetailsDto: FuelBatchDetailsDto;
}

export class FuelInspectionDto {
    id: number;
    referenceNumber: string;
    company: string;
    petroleumProduct: string;
    physicalLocation: string;
    inspectionDateFrom: Date;
    inspectionDateTo: Date;
    processStage: string;
    assignedOfficerStatus: boolean;
    rapidTestDone: boolean;
    sampleCollectionStatus: boolean;
    sampleSubmittedStatus: boolean;
    bsNumberStatus: boolean;
    compliantStatusAdded: boolean;
    remediationScheduledStatus: boolean;
    remendiationCompleteStatus: boolean;
    proFormaInvoiceStatus: boolean;
    endInspectionStatus: boolean;
    batchDetails: FuelBatchDetailsDto;
    officersList: MsUsersDto[];
    officersAssigned: MsUsersDto;
    rapidTest: FuelEntityRapidTestDto;
    sampleCollected: SampleCollectionDto;
    sampleSubmitted: SampleSubmissionDto;
    sampleLabResults: MSSSFLabResultsDto;
    fuelRemediation: FuelRemediationDto;
}

export class MSSSFLabResultsDto {
    ssfResultsList: MSSSFComplianceStatusDetailsDto;
    savedPDFFiles:  MSSSFPDFListDetailsDto[];
    limsPDFFiles: LIMSFilesFoundDto[];
    parametersListTested: LabResultsParamDto[];
}

export class MSSSFComplianceStatusDetailsDto {
    sffId: number;
    bsNumber: string;
    complianceRemarks: string;
    complianceStatus: boolean;
}

export class FuelRemediationDto {
    productType: string;
    applicableKenyaStandard: string;
    remediationProcedure: string;
    volumeOfProductContaminated: string;
    contaminatedFuelType: string;
    quantityOfFuel: string;
    volumeAdded: string;
    totalVolume: string;
    proFormaInvoiceStatus: boolean;
    proFormaInvoiceNo: string;
    invoiceAmount: string;
    feePaidReceiptNo: string;
    dateOfRemediation: Date;
    dateOfPayment: Date;
    invoiceCreated: boolean;
}

export class MSSSFPDFListDetailsDto {
    pdfSavedId: number;
    pdfName: string;
    sffId: number;
    complianceRemarks: string;
    complianceStatus: boolean;
}

export class LIMSFilesFoundDto {
    fileSavedStatus: boolean;
    fileName: string;
}

export class PDFSaveComplianceStatusDto {
    ssfID: number;
    bsNumber: string;
    PDFFileName: string;
    complianceStatus: boolean;
    complianceRemarks: string;
}

export class SSFSaveComplianceStatusDto {
    ssfID: number;
    bsNumber: string;
    complianceStatus: boolean;
    complianceRemarks: string;
}

export class LabResultsParamDto {
    param: string;
    result: string;
    method: string;
}

export class SampleSubmissionDto {
    id: number;
    nameProduct: string;
    packaging: string;
    labellingIdentification: string;
    fileRefNumber: string;
    referencesStandards: string;
    sizeTestSample: number;
    sizeRefSample: number;
    condition: string;
    sampleReferences: string;
    sendersName: string;
    designation: string;
    address: string;
    sendersDate: Date;
    receiversName: string;
    testChargesKsh: number;
    receiptLpoNumber: string;
    invoiceNumber: string;
    disposal: string;
    remarks: string;
    sampleCollectionNumber: number;
    bsNumber: string;
    parametersList: SampleSubmissionItemsDto[];
}

export class SampleSubmissionItemsDto {
    parameters: string;
    laboratoryName: string;
}

export class BSNumberSaveDto {
    bsNumber: string;
    submittedDate: Date;
    remarks: string;
}

export class SampleCollectionDto {
    id: string;
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
    id: number;
    productBrandName: string;
    batchNo: string;
    batchSize: string;
    sampleSize: string;
}

export class MsUsersDto {
    id: number;
    firstName: string;
    lastName: string;
    userName: string;
    email: string;
    status: boolean;
}

