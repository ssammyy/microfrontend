import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpParams} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {ApiEndpointService} from '../../../services/endpoints/api-endpoint.service';
import {catchError, map} from 'rxjs/operators';
import {
    ApiResponseModel,
    BatchFileFuelSaveDto,
    BSNumberSaveDto,
    ComplaintCustomersDto,
    ComplaintDto,
    ComplaintLocationDto,
    ComplaintDetailsDto,
    CompliantRemediationDto,
    FuelBatchDetailsDto,
    FuelEntityAssignOfficerDto,
    FuelEntityDto,
    FuelEntityRapidTestDto,
    FuelInspectionDto,
    FuelInspectionScheduleListDetailsDto,
    LaboratoryDto,
    LabResultsParamDto,
    LIMSFilesFoundDto,
    MSComplaintSubmittedSuccessful,
    MSSSFComplianceStatusDetailsDto,
    MSSSFLabResultsDto,
    MSSSFPDFListDetailsDto,
    MsUsersDto,
    NewComplaintDto,
    PDFSaveComplianceStatusDto,
    RemediationDto,
    SampleCollectionDto,
    SampleCollectionItemsDto,
    SampleSubmissionDto,
    SampleSubmissionItemsDto,
    SSFSaveComplianceStatusDto,
    AllComplaintsDetailsDto,
    MsDepartment,
    MsDivisionDetails,
    MsProducts,
    MsProductCategories,
    MsBroadProductCategory,
    MsProductSubcategory,
    ComplaintApproveDto,
    ComplaintRejectDto,
    ComplaintAdviceRejectDto,
    ComplaintAssignDto, MsStandardProductCategory, ComplaintClassificationDto, WorkPlanBatchDetailsDto,
} from './ms.model';
import swal from 'sweetalert2';
import {AbstractControl, ValidatorFn} from '@angular/forms';
import Swal from 'sweetalert2';
import {AllPermitDetailsDto} from '../qa/qa.model';

@Injectable({
    providedIn: 'root',
})
export class MsService {

    constructor(private http: HttpClient) {
    }

   public fuelBatchDetailsListExamples(): FuelBatchDetailsDto[] {
        const test101 = new FuelBatchDetailsDto();
        test101.id = 123;
        test101.region = 'test';
        test101.county = 'test';
        test101.town = 'test';
        test101.referenceNumber = 'test';
        test101.batchFileYear = 'test';
        test101.remarks = 'test';
        test101.batchClosed = true;

        const test: FuelBatchDetailsDto[] = [];
        test.push(test101);

        return test;
    }

   public fuelInspectionDetailsExamples(): FuelInspectionDto {
       const currentDate = new Date();
       const test101BatchDetails = new FuelBatchDetailsDto();
       test101BatchDetails.id = 123;
       test101BatchDetails.region = 'test';
       test101BatchDetails.county = 'test';
       test101BatchDetails.town = 'test';
       test101BatchDetails.referenceNumber = 'test';
       test101BatchDetails.batchFileYear = 'test';
       test101BatchDetails.remarks = 'test';
       test101BatchDetails.batchClosed = true;

       const test101MSUserDetails = new MsUsersDto();
       test101MSUserDetails.id = 123;
       test101MSUserDetails.firstName = 'TESTUSER';
       test101MSUserDetails.lastName = 'TESTUSER';
       test101MSUserDetails.userName = 'TESTUSER';
       test101MSUserDetails.email = 'TESTUSER';
       test101MSUserDetails.status = true;

       const testUserList: MsUsersDto[] = [];
       testUserList.push(test101MSUserDetails);

       const test101SampleCollectionItemsDto = new SampleCollectionItemsDto();
       test101SampleCollectionItemsDto.id = 125;
       test101SampleCollectionItemsDto.productBrandName = 'test101SampleCollectionItemsDto';
       test101SampleCollectionItemsDto.batchNo = '123';
       test101SampleCollectionItemsDto.batchSize = '6776';
       test101SampleCollectionItemsDto.sampleSize = '756';

       const testSampleCollectionItemsDto: SampleCollectionItemsDto[] = [];
       testSampleCollectionItemsDto.push(test101SampleCollectionItemsDto);

       const test101MSSampleCollectDetails = new SampleCollectionDto();
       test101MSSampleCollectDetails.nameManufacturerTrader = 'testSampleCollect';
       test101MSSampleCollectDetails.addressManufacturerTrader = 'testSampleCollect';
       test101MSSampleCollectDetails.samplingMethod = 'testSampleCollect';
       test101MSSampleCollectDetails.reasonsCollectingSamples = 'testSampleCollect';
       test101MSSampleCollectDetails.anyRemarks = 'testSampleCollect';
       test101MSSampleCollectDetails.designationOfficerCollectingSample = 'testSampleCollect';
       test101MSSampleCollectDetails.nameOfficerCollectingSample = 'testSampleCollect';
       test101MSSampleCollectDetails.dateOfficerCollectingSample = currentDate;
       test101MSSampleCollectDetails.nameWitness = 'testSampleCollect';
       test101MSSampleCollectDetails.designationWitness = 'testSampleCollect';
       test101MSSampleCollectDetails.dateWitness = currentDate;
       test101MSSampleCollectDetails.productsList = testSampleCollectionItemsDto;

       const test101SampleSubmissionItemsDto = new SampleSubmissionItemsDto();
       test101SampleSubmissionItemsDto.parameters = 'test198';
       test101SampleSubmissionItemsDto.laboratoryName = 'test198';

       const testSampleSubmissionItemsDto: SampleSubmissionItemsDto[] = [];
       testSampleSubmissionItemsDto.push(test101SampleSubmissionItemsDto);

       const test101SampleSubmissionDto = new SampleSubmissionDto();
       test101SampleSubmissionDto.nameProduct = 'stringTest';
       test101SampleSubmissionDto.packaging = 'stringTest';
       test101SampleSubmissionDto.labellingIdentification = 'stringTest';
       test101SampleSubmissionDto.fileRefNumber = 'stringTest';
       test101SampleSubmissionDto.referencesStandards = 'stringTest';
       test101SampleSubmissionDto.sizeTestSample = 455;
       test101SampleSubmissionDto.sizeRefSample = 3465;
       test101SampleSubmissionDto.condition = 'stringTest';
       test101SampleSubmissionDto.sampleReferences = 'stringTest';
       test101SampleSubmissionDto.sendersName = 'stringTest';
       test101SampleSubmissionDto.designation = 'stringTest';
       test101SampleSubmissionDto.address = 'stringTest';
       test101SampleSubmissionDto.sendersDate = currentDate;
       test101SampleSubmissionDto.receiversName = 'stringTest';
       test101SampleSubmissionDto.testChargesKsh = 4567;
       test101SampleSubmissionDto.receiptLpoNumber = 'stringTest';
       test101SampleSubmissionDto.invoiceNumber = 'stringTest';
       test101SampleSubmissionDto.disposal = 'stringTest';
       test101SampleSubmissionDto.remarks = 'stringTest';
       test101SampleSubmissionDto.sampleCollectionNumber = 75877;
       test101SampleSubmissionDto.bsNumber = 'stringTest';
       test101SampleSubmissionDto.parametersList = testSampleSubmissionItemsDto;

       const test101LabResultsParamDto = new LabResultsParamDto();
       test101LabResultsParamDto.param = 'labParam';
       test101LabResultsParamDto.result = 'labParam';
       test101LabResultsParamDto.method = 'labParam';

       const testLabResultsParamDto: LabResultsParamDto[] = [];
       testLabResultsParamDto.push(test101LabResultsParamDto);

       const limsFilesFoundDto = new LIMSFilesFoundDto();
       limsFilesFoundDto.fileSavedStatus = true;
       limsFilesFoundDto.fileName = 'labParam';

       const limsFilesFoundDtoList: LIMSFilesFoundDto[] = [];
       limsFilesFoundDtoList.push(limsFilesFoundDto);

       const msSSFPDFListDetailsDto = new MSSSFPDFListDetailsDto();
       msSSFPDFListDetailsDto.pdfSavedId = 5265;
       msSSFPDFListDetailsDto.pdfName = 'steat';
       msSSFPDFListDetailsDto.sffId = 3434;
       msSSFPDFListDetailsDto.complianceRemarks = 'steat';
       msSSFPDFListDetailsDto.complianceStatus = true;

       const msSSFPDFListDetailsDtoList: MSSSFPDFListDetailsDto[] = [];
       msSSFPDFListDetailsDtoList.push(msSSFPDFListDetailsDto);

       const msssfComplianceStatusDetailsDto = new MSSSFComplianceStatusDetailsDto();
        msssfComplianceStatusDetailsDto.sffId = 5265;
       msssfComplianceStatusDetailsDto.bsNumber = 'steat';
       msssfComplianceStatusDetailsDto.complianceRemarks = '3434';
       msssfComplianceStatusDetailsDto.complianceStatus = true;

       const test101LabResultsDto = new MSSSFLabResultsDto();
       test101LabResultsDto.ssfResultsList = msssfComplianceStatusDetailsDto;
       test101LabResultsDto.savedPDFFiles = msSSFPDFListDetailsDtoList;
       test101LabResultsDto.limsPDFFiles = limsFilesFoundDtoList;
       test101LabResultsDto.parametersListTested = testLabResultsParamDto;




        const test101 = new FuelInspectionDto();
       test101.id = 123;
       test101.referenceNumber = 'test101';
       test101.company = 'testDetails';
       test101.petroleumProduct = 'testDetails';
       test101.physicalLocation = 'testDetails';
       test101.inspectionDateFrom = currentDate;
       test101.inspectionDateTo = currentDate;
       test101.processStage = 'test Stage';
       test101.batchDetails = test101BatchDetails;
       test101.officersList = testUserList;
       test101.officersAssigned = test101MSUserDetails;
       test101.sampleCollected = test101MSSampleCollectDetails;
       test101.sampleSubmitted = test101SampleSubmissionDto;
       test101.sampleLabResults = test101LabResultsDto;


        return test101;
    }
   public fuelInspectionListExamples(): FuelInspectionScheduleListDetailsDto {
       const currentDate = new Date();
       const test101BatchDetails = new FuelBatchDetailsDto();
       test101BatchDetails.id = 123;
       test101BatchDetails.region = 'test';
       test101BatchDetails.county = 'test';
       test101BatchDetails.town = 'test';
       test101BatchDetails.referenceNumber = 'test';
       test101BatchDetails.batchFileYear = 'test';
       test101BatchDetails.remarks = 'test';
       test101BatchDetails.batchClosed = true;

        const test101 = new FuelInspectionDto();
       test101.id = 123;
       test101.referenceNumber = 'test101';
       test101.company = 'testDetails';
       test101.petroleumProduct = 'testDetails';
       test101.physicalLocation = 'testDetails';
       test101.inspectionDateFrom = currentDate;
       test101.inspectionDateTo = currentDate;
       test101.batchDetails = test101BatchDetails;
       test101.processStage = 'test Stage';

        const test: FuelInspectionDto[] = [];
        test.push(test101);

       const test102 = new FuelInspectionScheduleListDetailsDto();
       test102.fuelInspectionDto = test;
       test102.fuelBatchDetailsDto = test101BatchDetails;


        return test102;
    }

    // Check if role is in required privileges
    public hasRole(privileges: string[], roles: any[]): boolean {
        for (const role of roles) {
            for (const p of privileges) {
                if (role == p) {
                    return true;
                }
            }
        }
        return false;
    }

    public reloadCurrentRoute() {
        location.reload();
    }


    showSuccessWith2Message(title: string, text: string, cancelMessage: string, successMessage: string, fn?: Function) {
        const swalWithBootstrapButtons = Swal.mixin({
            customClass: {
                confirmButton: 'btn btn-success',
                cancelButton: 'btn btn-danger',
            },
            buttonsStyling: false,
        });

        swalWithBootstrapButtons.fire({
            title: title,
            text: text,
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Yes!',
            cancelButtonText: 'No!',
            reverseButtons: true,
        }).then((result) => {
            if (result.isConfirmed) {
                if (fn) {
                    const results = fn();
                    if (results == true) {
                        swalWithBootstrapButtons.fire(
                            'Submitted!',
                            successMessage,
                            'success',
                        );
                    } else if (results == false) {
                        swalWithBootstrapButtons.fire(
                            'Submitted!',
                            'AN ERROR OCCURRED',
                            'error',
                        );
                    }
                }

            } else if (
                /* Read more about handling dismissals below */
                result.dismiss === swal.DismissReason.cancel
            ) {
                swalWithBootstrapButtons.fire(
                    'Cancelled',
                    cancelMessage,
                    'error',
                );
            }
        });
    }

    showSuccess(message: string, fn?: Function) {
        swal.fire({
            title: message,
            buttonsStyling: false,
            customClass: {
                confirmButton: 'btn btn-success form-wizard-next-btn ',
            },
            icon: 'success',
        }).then(() => {
            if (fn) {
                fn();
            }
        });
    }

   public showError(message: string, fn?: Function) {
        swal.fire({
            title: message,
            buttonsStyling: false,
            customClass: {
                confirmButton: 'btn btn-success form-wizard-next-btn ',
            },
            icon: 'error',
        }).then(() => {
            if (fn) {
                fn();
            }
        });
    }
    // tslint:disable-next-line:max-line-length
    /*******************************************************************START OF MARKET SURVEILLANCE*****************************************************************************/

    public loadMSWorkPlanBatchList(page: string, records: string): Observable<WorkPlanBatchDetailsDto[]> {
        // console.log(data);
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_WORK_PLAN.ALL_BATCH_LIST);
        const params = new HttpParams()
            .set('page', page)
            .set('records', records);
        return this.http.get<WorkPlanBatchDetailsDto[]>(url, {params}).pipe(
            map(function (response: WorkPlanBatchDetailsDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public addNewMSWorkPlanBatch(page: string, records: string): Observable<WorkPlanBatchDetailsDto[]> {
        const params = new HttpParams()
            .set('page', page)
            .set('records', records);
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_WORK_PLAN.ADD_NEW_BATCH);
        return this.http.post<WorkPlanBatchDetailsDto[]>(url, null, {params}).pipe(
            map(function (response: WorkPlanBatchDetailsDto[]) {
                return response;
            })
            , catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.error} )`);
                return throwError(fault);
            }),
        );
    }



    // tslint:disable-next-line:max-line-length
    /*******************************************************************START OF COMPLAINT*****************************************************************************/


    public msDepartmentListDetails(): Observable<MsDepartment[]> {
        // console.log(data);
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_COMMON.MS_DEPARTMENTS);
        return this.http.get<MsDepartment[]>(url).pipe(
            map(function (response: MsDepartment[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public msDivisionListDetails(): Observable<MsDivisionDetails[]> {
        // console.log(data);
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_COMMON.MS_DIVISIONS);
        return this.http.get<MsDivisionDetails[]>(url).pipe(
            map(function (response: MsDivisionDetails[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public msProductListDetails(): Observable<MsProducts[]> {
        // console.log(data);
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_COMMON.MS_PRODUCTS);
        return this.http.get<MsProducts[]>(url).pipe(
            map(function (response: MsProducts[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public msProductCategoryListDetails(): Observable<MsProductCategories[]> {
        // console.log(data);
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_COMMON.MS_PRODUCT_CATEGORIES);
        return this.http.get<MsProductCategories[]>(url).pipe(
            map(function (response: MsProductCategories[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public msProductBroadCategoryListDetails(): Observable<MsBroadProductCategory[]> {
        // console.log(data);
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_COMMON.MS_BROAD_PRODUCT_CATEGORY);
        return this.http.get<MsBroadProductCategory[]>(url).pipe(
            map(function (response: MsBroadProductCategory[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public msProductSubCategoryListDetails(): Observable<MsProductSubcategory[]> {
        // console.log(data);
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_COMMON.MS_PRODUCT_SUB_CATEGORY);
        return this.http.get<MsProductSubcategory[]>(url).pipe(
            map(function (response: MsProductSubcategory[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public msProductStandardCategoryListDetails(): Observable<MsStandardProductCategory[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_COMMON.MS_STANDARD_PRODUCT_CATEGORY);
        return this.http.get<MsStandardProductCategory[]>(url).pipe(
            map(function (response: MsStandardProductCategory[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    // tslint:disable-next-line:max-line-length
    public createNewComplaint(customerDetails: ComplaintCustomersDto, complaintDetails: ComplaintDto, locationDetails: ComplaintLocationDto): Observable<MSComplaintSubmittedSuccessful> {
        const newComplaintDto = new NewComplaintDto();
        newComplaintDto.customerDetails = customerDetails;
        newComplaintDto.complaintDetails = complaintDetails;
        newComplaintDto.locationDetails = locationDetails;
        console.log(newComplaintDto);
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_COMPLAINT.CREATE_NEW_COMPLAINT);
        return this.http.post<MSComplaintSubmittedSuccessful>(url, newComplaintDto).pipe(
            map(function (response: MSComplaintSubmittedSuccessful) {
                return response;
            })
            , catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.error} )`);
                return throwError(fault);
            }),
        );
    }

    public saveComplaintFiles(data: FormData): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_COMPLAINT.UPLOAD_COMPLIANT_FILE);
        // const params = new HttpParams()
        //     .set('permitID', permitID);
        return this.http.post<any>(url, data, {
            headers: {
                'enctype': 'multipart/form-data',
            }, params: {'refNumber': 'refNumber'},
        }).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public loadMSComplaintList(page: string, records: string, routeTake: string): Observable<ApiResponseModel> {
        // console.log(data);
        let url = null;
        switch (routeTake) {
            case 'my-tasks':
                url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_COMPLAINT.MY_TASK_COMPLAINT_LIST);
                break;
            case 'on-going':
                url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_COMPLAINT.ONGOING_COMPLAINT_LIST);
                break;
            case 'new-complaint':
                url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_COMPLAINT.NEW_COMPLAINT_LIST);
                break;
            case 'completed':
                url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_COMPLAINT.COMPLETED_COMPLAINT_LIST);
                break;
        }

        const params = new HttpParams()
            .set('page', page)
            .set('records', records);
        return this.http.get<ApiResponseModel>(url, {params}).pipe(
            map(function (response: ApiResponseModel) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }


    public msComplaintDetails(referenceNo: string): Observable<AllComplaintsDetailsDto> {
        // console.log(data);
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_COMPLAINT.COMPLAINT_DETAILS);
        const params = new HttpParams()
            .set('referenceNo', referenceNo);
        return this.http.get<AllComplaintsDetailsDto>(url, {params}).pipe(
            map(function (response: AllComplaintsDetailsDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public msComplaintUpdateAcceptanceDetails(referenceNo: string, data: ComplaintApproveDto): Observable<AllComplaintsDetailsDto> {
        console.log(data);
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_COMPLAINT.COMPLAINT_DETAILS_UPDATE_ACCEPTANCE);
        const params = new HttpParams()
            .set('referenceNo', referenceNo);
        return this.http.put<AllComplaintsDetailsDto>(url, data, {params}).pipe(
            map(function (response: AllComplaintsDetailsDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public msComplaintUpdateRejectDetails(referenceNo: string, data: ComplaintRejectDto): Observable<AllComplaintsDetailsDto> {
        console.log(data);
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_COMPLAINT.COMPLAINT_DETAILS_UPDATE_REJECTION);
        const params = new HttpParams()
            .set('referenceNo', referenceNo);
        return this.http.put<AllComplaintsDetailsDto>(url, data, {params}).pipe(
            map(function (response: AllComplaintsDetailsDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public msComplaintUpdateMandateOGADetails(referenceNo: string, data: ComplaintAdviceRejectDto): Observable<AllComplaintsDetailsDto> {
        console.log(data);
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_COMPLAINT.COMPLAINT_DETAILS_UPDATE_OGA_MANDATE);
        const params = new HttpParams()
            .set('referenceNo', referenceNo);
        return this.http.put<AllComplaintsDetailsDto>(url, data, {params}).pipe(
            map(function (response: AllComplaintsDetailsDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public msComplaintUpdateAssignHOFDetails(referenceNo: string, data: ComplaintAssignDto): Observable<AllComplaintsDetailsDto> {
        console.log(data);
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_COMPLAINT.COMPLAINT_DETAILS_UPDATE_ASSIGN_HOF);
        const params = new HttpParams()
            .set('referenceNo', referenceNo);
        return this.http.put<AllComplaintsDetailsDto>(url, data, {params}).pipe(
            map(function (response: AllComplaintsDetailsDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public msComplaintUpdateAssignIODetails(referenceNo: string, data: ComplaintAssignDto): Observable<AllComplaintsDetailsDto> {
        console.log(data);
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_COMPLAINT.COMPLAINT_DETAILS_UPDATE_ASSIGN_IO);
        const params = new HttpParams()
            .set('referenceNo', referenceNo);
        return this.http.put<AllComplaintsDetailsDto>(url, data, {params}).pipe(
            map(function (response: AllComplaintsDetailsDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public msComplaintUpdateSaveClassificationDetails(referenceNo: string, data: ComplaintClassificationDto): Observable<AllComplaintsDetailsDto> {
        console.log(data);
        // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_COMPLAINT.COMPLAINT_DETAILS_ADD_CLASSIFICATION_DETAILS);
        const params = new HttpParams()
            .set('referenceNo', referenceNo);
        return this.http.put<AllComplaintsDetailsDto>(url, data, {params}).pipe(
            map(function (response: AllComplaintsDetailsDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    // tslint:disable-next-line:max-line-length
    /*******************************************************************START OF FUEL SURVEILLANCE*****************************************************************************/

    public loadMSFuelBatchList(page: string, records: string): Observable<FuelBatchDetailsDto[]> {
        // console.log(data);
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.ALL_BATCH_LIST);
        const params = new HttpParams()
            .set('page', page)
            .set('records', records);
        return this.http.get<FuelBatchDetailsDto[]>(url, {params}).pipe(
            map(function (response: FuelBatchDetailsDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public loadMSLabList(): Observable<LaboratoryDto[]> {
        // console.log(data);
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.LAB_LIST);
        return this.http.get<LaboratoryDto[]>(url).pipe(
            map(function (response: LaboratoryDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public loadFileDetailsPDF(fileID: string): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.VIEW_PDF_SAVED);
        const params = new HttpParams()
            .set('fileID', fileID);
        // return this.httpService.get<any>(`${this.baseUrl}/get/pdf/${fileName}`, { responseType: 'arraybuffer' as 'json' });
        return this.http.get<any>(url, {params, responseType: 'arraybuffer' as 'json'}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public loadFileDetailsLabResultsPDF(fileName: string, bsNumber: string): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.VIEW_PDF_LAB_RESULT);
        const params = new HttpParams()
            .set('fileName', fileName)
            .set('bsNumber', bsNumber);
        // return this.httpService.get<any>(`${this.baseUrl}/get/pdf/${fileName}`, { responseType: 'arraybuffer' as 'json' });
        return this.http.get<any>(url, {params, responseType: 'arraybuffer' as 'json'}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public loadRemediationInvoiceDetailsPDF(fuelInspectionId: string): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.VIEW_PDF_REMEDIATION_INVOICE);
        const params = new HttpParams()
            .set('fuelInspectionId', fuelInspectionId);
        // return this.httpService.get<any>(`${this.baseUrl}/get/pdf/${fileName}`, { responseType: 'arraybuffer' as 'json' });
        return this.http.get<any>(url, {params, responseType: 'arraybuffer' as 'json'}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public loadSampleCollectionDetailsPDF(sampleCollectionID: string): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.VIEW_PDF_SAMPLE_COLLECTION);
        const params = new HttpParams()
            .set('sampleCollectionID', sampleCollectionID);
        // return this.httpService.get<any>(`${this.baseUrl}/get/pdf/${fileName}`, { responseType: 'arraybuffer' as 'json' });
        return this.http.get<any>(url, {params, responseType: 'arraybuffer' as 'json'}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }




    public addNewMSFuelBatch(data: BatchFileFuelSaveDto): Observable<FuelInspectionScheduleListDetailsDto> {
        console.log(data);
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.ADD_BATCH);
        return this.http.post<FuelInspectionScheduleListDetailsDto>(url, data).pipe(
            map(function (response: FuelInspectionScheduleListDetailsDto) {
                return response;
            })
            , catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.error} )`);
                return throwError(fault);
            }),
        );
    }

    public closeMSFuelBatch(referenceNo: string): Observable<FuelBatchDetailsDto[]> {
        // console.log(data);
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.CLOSE_BATCH);
        const params = new HttpParams()
            .set('referenceNo', referenceNo);
        return this.http.put<FuelBatchDetailsDto[]>(url, null, {params}).pipe(
            map(function (response: FuelBatchDetailsDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public msFuelInspectionList(batchReferenceNo: string, page: string, records: string): Observable<FuelInspectionScheduleListDetailsDto> {
        // console.log(data);
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.INSPECTION_SCHEDULED_LIST);
        const params = new HttpParams()
            .set('batchReferenceNo', batchReferenceNo)
            .set('page', page)
            .set('records', records);
        return this.http.get<FuelInspectionScheduleListDetailsDto>(url, {params}).pipe(
            map(function (response: FuelInspectionScheduleListDetailsDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public msFuelInspectionAddSchedule(batchReferenceNo: string, data: FuelEntityDto): Observable<FuelInspectionScheduleListDetailsDto> {
        console.log(data);
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.INSPECTION_SCHEDULED_ADD_NEW);
        const params = new HttpParams()
            .set('batchReferenceNo', batchReferenceNo);
        return this.http.post<FuelInspectionScheduleListDetailsDto>(url, data, {params}).pipe(
            map(function (response: FuelInspectionScheduleListDetailsDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public msFuelInspectionScheduledDetails(batchReferenceNo: string, referenceNo: string): Observable<FuelInspectionDto> {
        // console.log(data);
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.INSPECTION_SCHEDULED_DETAILS);
        const params = new HttpParams()
            .set('batchReferenceNo', batchReferenceNo)
            .set('referenceNo', referenceNo);
        return this.http.get<FuelInspectionDto>(url, {params}).pipe(
            map(function (response: FuelInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    // tslint:disable-next-line:max-line-length
    public msFuelInspectionScheduledAssignOfficer(batchReferenceNo: string, referenceNo: string, data: FuelEntityAssignOfficerDto): Observable<FuelInspectionDto> {
        console.log(data);
        // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.INSPECTION_SCHEDULED_DETAILS_ASSIGN_OFFICER);
        const params = new HttpParams()
            .set('batchReferenceNo', batchReferenceNo)
            .set('referenceNo', referenceNo);
        return this.http.put<FuelInspectionDto>(url, data, {params}).pipe(
            map(function (response: FuelInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public msFuelInspectionScheduledRapidTest(batchReferenceNo: string, referenceNo: string, data: FuelEntityRapidTestDto): Observable<FuelInspectionDto> {
        console.log(data);
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.INSPECTION_SCHEDULED_DETAILS_RAPID_TEST);
        const params = new HttpParams()
            .set('batchReferenceNo', batchReferenceNo)
            .set('referenceNo', referenceNo);
        return this.http.put<FuelInspectionDto>(url, data, {params}).pipe(
            map(function (response: FuelInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public msFuelInspectionScheduledAddSampleCollection(batchReferenceNo: string, referenceNo: string, data: SampleCollectionDto): Observable<FuelInspectionDto> {
        console.log(data);
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.INSPECTION_SCHEDULED_DETAILS_SAMPLE_COLLECT);
        const params = new HttpParams()
            .set('batchReferenceNo', batchReferenceNo)
            .set('referenceNo', referenceNo);
        return this.http.post<FuelInspectionDto>(url, data, {params}).pipe(
            map(function (response: FuelInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public msFuelInspectionScheduledAddSampleSubmission(batchReferenceNo: string, referenceNo: string, data: SampleSubmissionDto): Observable<FuelInspectionDto> {
        console.log(data);
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.INSPECTION_SCHEDULED_DETAILS_SAMPLE_SUBMISSION);
        const params = new HttpParams()
            .set('batchReferenceNo', batchReferenceNo)
            .set('referenceNo', referenceNo);
        return this.http.post<FuelInspectionDto>(url, data, {params}).pipe(
            map(function (response: FuelInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public msFuelInspectionScheduledAddSampleSubmissionBSNumber(batchReferenceNo: string, referenceNo: string, data: BSNumberSaveDto): Observable<FuelInspectionDto> {
        console.log(data);
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.INSPECTION_SCHEDULED_DETAILS_SAMPLE_SUBMISSION_BS_NUMBER);
        const params = new HttpParams()
            .set('batchReferenceNo', batchReferenceNo)
            .set('referenceNo', referenceNo);
        return this.http.put<FuelInspectionDto>(url, data, {params}).pipe(
            map(function (response: FuelInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public msFuelInspectionScheduledSavePDFLIMS(batchReferenceNo: string, referenceNo: string, data: PDFSaveComplianceStatusDto): Observable<FuelInspectionDto> {
        console.log(data);
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.INSPECTION_SCHEDULED_DETAILS_LAB_RESULTS_SAVE_PDF);
        const params = new HttpParams()
            .set('batchReferenceNo', batchReferenceNo)
            .set('referenceNo', referenceNo);
        return this.http.put<FuelInspectionDto>(url, data, {params}).pipe(
            map(function (response: FuelInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public msFuelInspectionScheduledSaveSSFComplianceStatus(batchReferenceNo: string, referenceNo: string, data: SSFSaveComplianceStatusDto): Observable<FuelInspectionDto> {
        console.log(data);
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.INSPECTION_SCHEDULED_DETAILS_SSF_COMPLIANCE_STATUS);
        const params = new HttpParams()
            .set('batchReferenceNo', batchReferenceNo)
            .set('referenceNo', referenceNo);
        return this.http.put<FuelInspectionDto>(url, data, {params}).pipe(
            map(function (response: FuelInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public msFuelInspectionScheduledRemediation(batchReferenceNo: string, referenceNo: string, data: CompliantRemediationDto): Observable<FuelInspectionDto> {
        console.log(data);
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.INSPECTION_SCHEDULED_REMEDIATION_DATE);
        const params = new HttpParams()
            .set('batchReferenceNo', batchReferenceNo)
            .set('referenceNo', referenceNo);
        return this.http.post<FuelInspectionDto>(url, data, {params}).pipe(
            map(function (response: FuelInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public msFuelInspectionNotCompliantRemediationInvoice(batchReferenceNo: string, referenceNo: string, data: CompliantRemediationDto): Observable<FuelInspectionDto> {
        console.log(data);
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.INSPECTION_SCHEDULED_REMEDIATION_INVOICE);
        const params = new HttpParams()
            .set('batchReferenceNo', batchReferenceNo)
            .set('referenceNo', referenceNo);
        return this.http.post<FuelInspectionDto>(url, data, {params}).pipe(
            map(function (response: FuelInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public msFuelInspectionRemediation(batchReferenceNo: string, referenceNo: string, data: RemediationDto): Observable<FuelInspectionDto> {
        console.log(data);
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.INSPECTION_SCHEDULED_ADD_REMEDIATION);
        const params = new HttpParams()
            .set('batchReferenceNo', batchReferenceNo)
            .set('referenceNo', referenceNo);
        return this.http.put<FuelInspectionDto>(url, data, {params}).pipe(
            map(function (response: FuelInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public msFuelInspectionEnd(batchReferenceNo: string, referenceNo: string): Observable<FuelInspectionDto> {
        // console.log(da/ta);
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.MARKET_SURVEILLANCE_FUEL_ENDPOINT.END_INSPECTION);
        const params = new HttpParams()
            .set('batchReferenceNo', batchReferenceNo)
            .set('referenceNo', referenceNo);
        return this.http.put<FuelInspectionDto>(url, null, {params}).pipe(
            map(function (response: FuelInspectionDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

}

export class CustomeDateValidators {
    static fromToDate(fromDateField: string, toDateField: string, errorName: string = 'fromToDate'): ValidatorFn {
        return (formGroup: AbstractControl): { [key: string]: boolean } | null => {
            const fromDate = formGroup.get(fromDateField).value;
            const toDate = formGroup.get(toDateField).value;
            // Ausing the fromDate and toDate are numbers. In not convert them first after null check
            if ((fromDate !== null && toDate !== null) && fromDate > toDate) {
                return {[errorName]: true};
            }
            return null;
        };
    }
}
