import {County} from '../county';
import {UserDetails} from '../master/master.model';

export class Ms {
}

export class ComplaintApproveDto {
    department: number;
    division: number;
    approveStatus: number;
    approvedRemarks: string;
}

export class ComplaintAdviceRejectDto {
    rejectStatus: number;
    advisedWhereToRemarks: string;
    rejectedRemarks: string;
}

export class MSComplaintSubmittedSuccessful {
    refNumber: string;
    savedStatus: boolean;
    successMessage: string;
    errorMessage: string;
}

export class MsDepartment {
    id?: number;
    department?: string;
    descriptions?: string;
    directorateId?: number;
    status?: boolean;
}

export class MsDivisionDetails {
    id?: string;
    division?: string;
    descriptions?: string;
    status?: string;
    departmentId?: number;
}

export class MsStandardProductCategory {
    id?: number;
    standardCategory?: string;
    standardNickname?: string;
    standardId?: number;
    status?: boolean;
}

export class MsProductCategories {
    id?: number;
    name?: string;
    status?: boolean;
    broadProductCategoryId?: number;
}

export class MsBroadProductCategory {
    id?: number;
    category?: string;
    status?: boolean;
    divisionId?: number;
}

export class MsProducts {
    id?: number;
    name?: string;
    status?: boolean;
    productCategoryId?: number;
}

export class MsProductSubcategory {
    id?: number;
    name?: string;
    status?: boolean;
    productId?: number;
}

export class NewComplaintDto {
    complaintDetails: ComplaintDto;
    customerDetails: ComplaintCustomersDto;
    locationDetails: ComplaintLocationDto;
}

export class ApiResponseModel {
    totalCount: number;
    extras: any;
    message: string;
    responseCode: string;
    data: any;
    errors: any;
    pageNo: number;
    totalPages: number;
}



export class AllComplaintsDetailsDto {
    complaintsDetails: ComplaintDetailsDto;
    officersList: MsUsersDto[];
    officersAssigned: MsUsersDto;
    remarksDetails: MSRemarksDto[];
    sampleCollected: SampleCollectionDto;
    sampleSubmitted: SampleSubmissionDto;
    sampleLabResults: MSSSFLabResultsDto;
}

export class MSRemarksDto {
    id: number;
    remarksDescription: string;
    processBy: string;
    processName: string;
}

export class ComplaintsListDto {
    referenceNumber: string;
    complaintTitle: string;
    targetedProducts: string;
    transactionDate: Date;
    progressStep: string;
}

export class ComplaintDetailsDto {
    id: number;
    refNumber: string;
    complainantName: string;
    complainantEmail: string;
    complainantPhoneNumber: string;
    complainantPostalAddress: string;
    complaintCategory: string;
    complaintTitle: string;
    complaintDescription: string;
    broadProductCategory: string;
    productClassification: string;
    productSubcategory: string;
    productName: string;
    productBrand: string;
    county: string;
    town: string;
    marketCenter: string;
    buildingName: string;
    date: Date;
    status: string;
    approvedStatus: number;
    assignedIOStatus: number;
    rejectedStatusStatus: number;
    complaintFiles: ComplaintsFilesFoundDto[];
}


export class DivisionDetails {
    id?: string;
    division?: string;
    descriptions?: string;
    status?: string;
    departmentId?: number;
}

// export class UserDetails {
//     id?: number;
//     firstName?: string;
//     lastName?: string;
//     userName?: string;
//     email?: string;
//     status?: boolean;
// }

export class FindWithRefNumber {
    refNumber?: string;
}

export class ComplaintsFilesFoundDto {
    id: number;
    fileName: string;
    documentType: string;
    fileContentType: string;
}


export class ComplaintDto {
    // complaintCategory: number;
    complaintTitle: string;
    // productClassification: number;
    // broadProductCategory: number;
    // productCategory: number;
    // myProduct: number;
    // productSubcategory: number;
    productBrand: string;
    complaintDescription: string;
}

export class ComplaintCustomersDto {
    firstName: string;
    lastName: string;
    phoneNumber: string;
    emailAddress: string;
    postalAddress: string;
}

export class ComplaintLocationDto {
    county: number;
    town: number;
    marketCenter: string;
    buildingName: string;
}


export class FuelBatchDetailsDto {
    id: number;
    region: string;
    county: string;
    town: string;
    referenceNumber: string;
    batchFileYear: string;
    batchFileMonth: string;
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
    remarksDetails: MSRemarksDto[];
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

