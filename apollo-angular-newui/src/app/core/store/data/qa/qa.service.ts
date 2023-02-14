import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpParams} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {ApiEndpointService} from '../../../services/endpoints/api-endpoint.service';
import {catchError, map} from 'rxjs/operators';
import {
    AllBatchInvoiceDetailsDto,
    AllPermitDetailsDto,
    AllSTA10DetailsDto, CompanyTurnOverUpdateDto, FilterDto, FirmTypeEntityDto,
    FmarkEntityDto,
    GenerateInvoiceDto, GenerateInvoiceWithWithholdingDto,
    MPesaPushDto,
    PermitDto,
    PermitEntityDetails,
    PermitEntityDto,
    PermitProcessStepDto,
    PlantDetailsDto,
    QRCodeScannedQADto,
    ResubmitApplicationDto,
    SamplesSubmittedDto,
    SSCApprovalRejectionDto,
    STA1,
    Sta10Dto,
    STA10MachineryAndPlantDto,
    STA10ManufacturingProcessDto,
    STA10PersonnelDto,
    STA10ProductsManufactureDto,
    STA10RawMaterialsDto,
    STA3,
    StgInvoiceBalanceDto,
    TaskDto,
} from './qa.model';
import {Branches, Company} from '../companies';
import Swal from 'sweetalert2';
import swal from 'sweetalert2';
import {SSFSendingComplianceStatus, WorkPlanInspectionDto} from '../ms/ms.model';
import {BusinessLines, BusinessNatures} from '../business';
import {StandardsDto} from '../master/master.model';

@Injectable({
    providedIn: 'root',
})
export class QaService {

    constructor(private http: HttpClient) {
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
                    if (results === true) {
                        swalWithBootstrapButtons.fire(
                            'Submitted!',
                            successMessage,
                            'success',
                        );
                    } else if (results === false) {
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

    showError(message: string, fn?: Function) {
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

    showWarning(message: string, fn?: Function) {
        swal.fire({
            title: message,
            buttonsStyling: false,
            customClass: {
                confirmButton: 'btn btn-success form-wizard-next-btn ',
            },
            icon: 'warning',
        }).then(() => {
            if (fn) {
                fn();
            }
        });
    }

    public loadInspectionFeesUploadDetailsPDF(fileID: string): Observable<any> {
        // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.COMPANY_PROFILE_ENDPOINT.VIEW_PDF_INSPECTION_FEES_INVOICE);
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


    public saveUploadFile(data: FormData): Observable<any> {
        // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(
            ApiEndpointService.COMPANY_PROFILE_ENDPOINT.UPLOAD_INSPECTION_FEES_INVOICE,
        );
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

    public qaSaveSCS(data: FormData): Observable<any> {
        // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(
            ApiEndpointService.QA_INTERNAL_USER_ENDPOINT.UPLOAD_SCHEME_OF_SUPERVISION,
        );
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

    public qaSaveUploadFile(data: FormData): Observable<any> {
        // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(
            ApiEndpointService.QA_INTERNAL_USER_ENDPOINT.UPLOAD_ATTACHMENTS,
        );
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



    public qaUpdateSection(data: any, permitID: string): Observable<any> {
        console.log(data);
        // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.QA_INTERNAL_USER_ENDPOINT.UPDATE_SECTION);
        const params = new HttpParams()
            .set('permitID', String(permitID));
        return this.http.post<any>(url, data, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public qaUpdateDifferenceStatus(data: any, permitID: string): Observable<any> {
        console.log(data);
        // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.QA_INTERNAL_USER_ENDPOINT.UPDATE_OFFICER_DIFFERENCE_STATUS);
        const params = new HttpParams()
            .set('permitID', String(permitID));
        return this.http.post<any>(url, data, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public qaPermitCompleteness(data: any, permitID: string): Observable<any> {
        console.log(data);
        // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.QA_INTERNAL_USER_ENDPOINT.QAM_COMPLETENESS);
        const params = new HttpParams()
            .set('permitID', String(permitID));
        return this.http.post<any>(url, data, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public qaPermitCompletenessUpgrade(data: any, permitID: string): Observable<any> {
        console.log(data);
        // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.COMPANY_PROFILE_ENDPOINT.UPDATE_COMPANY_TURN_OVER_REQUEST_OFFICER);
        const params = new HttpParams()
            .set('permitID', String(permitID));
        return this.http.post<any>(url, data, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public qaPermitSavePDF(data: any, permitID: string): Observable<any> {
        console.log(data);
        // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.QA_INTERNAL_USER_ENDPOINT.SAVE_PDF_LAB_RESULT);
        const params = new HttpParams()
            .set('permitID', String(permitID));
        return this.http.post<any>(url, data, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public qaPermitSavePDFCompliance(data: any, permitID: string): Observable<any> {
        console.log(data);
        // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.QA_INTERNAL_USER_ENDPOINT.SAVE_PDF_LAB_RESULT_COMPLIANCE);
        const params = new HttpParams()
            .set('permitID', String(permitID));
        return this.http.post<any>(url, data, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public qaAssignOfficer(data: any, permitID: string): Observable<any> {
        console.log(data);
        // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.QA_INTERNAL_USER_ENDPOINT.ASSIGN_OFFICER);
        const params = new HttpParams()
            .set('permitID', String(permitID));
        return this.http.post<any>(url, data, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }
    public qaRecommendationApprovalForm(data: any, permitID: string): Observable<any> {
        console.log(data);
        // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.QA_INTERNAL_USER_ENDPOINT.RECOMMENDATION_APPROVAL);
        const params = new HttpParams()
            .set('permitID', String(permitID));
        return this.http.post<any>(url, data, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public qaStandardsAdd(data: any, permitID: string): Observable<any> {
        console.log(data);
        // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.QA_INTERNAL_USER_ENDPOINT.ADD_STANDARD);
        const params = new HttpParams()
            .set('permitID', String(permitID));
        return this.http.post<any>(url, data, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public qaAddRecommendation(data: any, permitID: string): Observable<any> {
        console.log(data);
        // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.QA_INTERNAL_USER_ENDPOINT.ADD_RECOMMENDATION);
        const params = new HttpParams()
            .set('permitID', String(permitID));
        return this.http.post<any>(url, data, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public qaScheduleInspectionReport(data: any, permitID: string): Observable<any> {
        console.log(data);
        // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.QA_INTERNAL_USER_ENDPOINT.SCHEDULE_INSPECTION);
        const params = new HttpParams()
            .set('permitID', String(permitID));
        return this.http.post<any>(url, data, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public qaSSFDetails(data: any, permitID: string): Observable<any> {
        console.log(data);
        // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.QA_INTERNAL_USER_ENDPOINT.ADD_SSF_DETAILS);
        const params = new HttpParams()
            .set('permitID', String(permitID));
        return this.http.post<any>(url, data, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public qaSSFDetailsCompliance(data: any, permitID: string): Observable<any> {
        console.log(data);
        // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.QA_INTERNAL_USER_ENDPOINT.ADD_SSF_COMPLIANCE_DETAILS);
        const params = new HttpParams()
            .set('permitID', String(permitID));
        return this.http.post<any>(url, data, {params}).pipe(
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
        // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.QA_INTERNAL_USER_ENDPOINT.VIEW_PDF_LAB_RESULT);
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


    // tslint:disable-next-line:max-line-length
    public qaUpdateFirmType(data: CompanyTurnOverUpdateDto): Observable<any> {
        console.log(data);
        // tslint:disable-next-line:max-line-length
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.COMPANY_PROFILE_ENDPOINT.UPDATE_COMPANY_TURN_OVER);
        const params = new HttpParams();
        return this.http.post<any>(url, data, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public generateInspectionFees(branchID: number): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.COMPANY_PROFILE_ENDPOINT.GENERATE_INSPECTION_FEES_INVOICE);
        const params = new HttpParams()
            .set('branchID', String(branchID));
        return this.http.post<any>(url, null, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }


    public loadAllCompanyList(): Observable<Company[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.COMPANY_PROFILE_ENDPOINT.LOAD_COMPANY_LIST);
        return this.http.get<Company[]>(url).pipe(
            map(function (response: Company[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public loadFirmPermitList(): Observable<FirmTypeEntityDto[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.COMPANY_PROFILE_ENDPOINT.LOAD_FIRM_TYPE_LIST);
        return this.http.get<FirmTypeEntityDto[]>(url).pipe(
            map(function (response: FirmTypeEntityDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public loadStandardListList(): Observable<StandardsDto[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.QA_MANUFACTURE_ENDPOINT.GET_STANDARD_LIST);
        return this.http.get<StandardsDto[]>(url).pipe(
            map(function (response: StandardsDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public loadBusinessLinesList(): Observable<BusinessLines[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.COMPANY_PROFILE_ENDPOINT.LOAD_BUSINESS_LINES);
        return this.http.get<BusinessLines[]>(url).pipe(
            map(function (response: BusinessLines[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public loadBusinessNaturesList(): Observable<BusinessNatures[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.COMPANY_PROFILE_ENDPOINT.LOAD_BUSINESS_NATURES);
        return this.http.get<BusinessNatures[]>(url).pipe(
            map(function (response: BusinessNatures[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public loadInvoiceListWithNoBatchID(): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.INVOICE_LIST_NO_DETAILS);
        return this.http.get<any>(url).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public loadInvoiceListWithNoBatchIDDifference(): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.INVOICE_LIST_NO_DIFFERENCE_DETAILS);
        return this.http.get<any>(url).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public loadInvoiceListWithNoBatchIDPermitType(permitTypeID: number, branchID: number): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.INVOICE_LIST_NO_DETAILS_PERMIT_TYPE);
        const params = new HttpParams()
            .set('permitTypeID', String(permitTypeID))
            .set('branchID', String(branchID));
        return this.http.get<any>(url, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public loadAllInvoiceListCreatedByUser(): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.INVOICE_LIST_ALL_DETAILS);
        return this.http.get<any>(url).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public loadInvoiceBatchList(): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.INVOICE_LIST_DETAILS);
        return this.http.get<any>(url).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }


    public loadInvoiceDetails(batchID: string): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.INVOICE_DETAILS);
        const params = new HttpParams()
            .set('batchID', batchID);
        return this.http.get<any>(url, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public loadQRCodeDetails(permitNumber: string): Observable<QRCodeScannedQADto> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_QR_CODE_SCAN);
        const params = new HttpParams()
            .set('permitNumber', permitNumber);
        return this.http.get<QRCodeScannedQADto>(url, {params}).pipe(
            map(function (response: QRCodeScannedQADto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public loadInvoiceDetailsBalance(batchID: string): Observable<StgInvoiceBalanceDto> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.INVOICE_DETAILS_BALANCE);
        const params = new HttpParams()
            .set('batchID', batchID);
        return this.http.get<StgInvoiceBalanceDto>(url, {params}).pipe(
            map(function (response: StgInvoiceBalanceDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public loadPermitList(permitTypeID: string): Observable<PermitEntityDto[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_LIST);
        const params = new HttpParams()
            .set('permitTypeID', permitTypeID);
        return this.http.get<PermitEntityDto[]>(url, {params}).pipe(
            map(function (response: PermitEntityDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }
    public loadCloneDmarkPermitList(permitTypeID: string): Observable<PermitEntityDto[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.CLONE_LIST_DMARK);
        const params = new HttpParams()
            .set('permitTypeID', permitTypeID);
        return this.http.get<PermitEntityDto[]>(url, {params}).pipe(
            map(function (response: PermitEntityDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }
    public loadCloneSmarkPermitList(permitTypeID: string): Observable<PermitEntityDto[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.CLONE_LIST_SMARK);
        const params = new HttpParams()
            .set('permitTypeID', permitTypeID);
        return this.http.get<PermitEntityDto[]>(url, {params}).pipe(
            map(function (response: PermitEntityDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }


    public loadPermitReports(permitTypeID: string): Observable<PermitEntityDto[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_REPORTS);
        const params = new HttpParams()
            .set('permitTypeID', permitTypeID);
        return this.http.get<PermitEntityDto[]>(url, {params}).pipe(
            map(function (response: PermitEntityDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public loadPermitGrantedReports(permitTypeID: string): Observable<PermitEntityDto[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_REPORTS_ALL_AWARDED);
        const params = new HttpParams()
            .set('permitTypeID', permitTypeID);
        return this.http.get<PermitEntityDto[]>(url, {params}).pipe(
            map(function (response: PermitEntityDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public loadPermitRenewedReports(permitTypeID: string): Observable<PermitEntityDto[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_REPORTS_ALL_RENEWED);
        const params = new HttpParams()
            .set('permitTypeID', permitTypeID);
        return this.http.get<PermitEntityDto[]>(url, {params}).pipe(
            map(function (response: PermitEntityDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public loadPermitSamplesSubmittedReports(permitTypeID: string): Observable<SamplesSubmittedDto[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_REPORTS_ALL_SAMPLES_SUBMITTED);
        const params = new HttpParams()
            .set('permitTypeID', permitTypeID);
        return this.http.get<SamplesSubmittedDto[]>(url, {params}).pipe(
            map(function (response: SamplesSubmittedDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public loadPermitDejectedReports(permitTypeID: string): Observable<PermitEntityDto[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_REPORTS_ALL_DEJECTED);
        const params = new HttpParams()
            .set('permitTypeID', permitTypeID);
        return this.http.get<PermitEntityDto[]>(url, {params}).pipe(
            map(function (response: PermitEntityDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public loadStatuses(): Observable<any[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_REPORTS_ALL_STATUSES);
        return this.http.get<any[]>(url, {}).pipe(
            map(function (response: any[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public loadOfficers(): Observable<any[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_REPORTS_ALL_OFFICES);
        return this.http.get<any[]>(url, {}).pipe(
            map(function (response: any[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }


    public deletePermit(permitID: string, data: PermitEntityDto[]): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.DELETE_PERMIT);
        const params = new HttpParams()
            .set('permitID', permitID);
        return this.http.post<PermitEntityDto>(url, data, {params}).pipe(
            map(function (response: PermitEntityDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }
    public applyFilter(data: FilterDto[]): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.FILTER_REPORTS);

        return this.http.post<FilterDto>(url, data, {}).pipe(
            map(function (response: FilterDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }
    public applyFilterAwarded(data: FilterDto[]): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.FILTER_REPORTS_AWARDED);

        return this.http.post<FilterDto>(url, data, {}).pipe(
            map(function (response: FilterDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }
    public applyFilterRenewed(data: FilterDto[]): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.FILTER_REPORTS_RENEWED);

        return this.http.post<FilterDto>(url, data, {}).pipe(
            map(function (response: FilterDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }
    public applyFilterDejected(data: FilterDto[]): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.FILTER_REPORTS_DEJECTED);

        return this.http.post<FilterDto>(url, data, {}).pipe(
            map(function (response: FilterDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public loadPermitAwardedList(permitTypeID: string): Observable<PermitEntityDto[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_LIST_AWARDED);
        const params = new HttpParams()
            .set('permitTypeID', permitTypeID);
        return this.http.get<PermitEntityDto[]>(url, {params}).pipe(
            map(function (response: PermitEntityDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public loadCompletelyPermitAwardedList(permitTypeID: string): Observable<PermitEntityDto[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_COMPLETELY_LIST_AWARDED);
        const params = new HttpParams()
            .set('permitTypeID', permitTypeID);
        return this.http.get<PermitEntityDto[]>(url, {params}).pipe(
            map(function (response: PermitEntityDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public loadPermitMigratededList(permitTypeID: string): Observable<PermitEntityDto[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_LIST_MIGRATION);
        const params = new HttpParams()
            .set('permitNumber', permitTypeID);
        return this.http.get<PermitEntityDto[]>(url, {params}).pipe(
            map(function (response: PermitEntityDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }
    public loadPermitMigratededListFmark(permitTypeID: string): Observable<PermitEntityDto[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_LIST_MIGRATION_FMARK);
        const params = new HttpParams()
            .set('permitNumber', permitTypeID);
        return this.http.get<PermitEntityDto[]>(url, {params}).pipe(
            map(function (response: PermitEntityDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }
    public loadPermitMigratededListDmark(permitTypeID: string): Observable<PermitEntityDto[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_LIST_MIGRATION_DMARK);
        const params = new HttpParams()
            .set('permitNumber', permitTypeID);
        return this.http.get<PermitEntityDto[]>(url, {params}).pipe(
            map(function (response: PermitEntityDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public loadAllMyPermits(): Observable<PermitEntityDto[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_LIST_ALL);

        return this.http.get<PermitEntityDto[]>(url).pipe(
            map(function (response: PermitEntityDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public loadPermitAwardedListToGenerateFMark(permitTypeID: string): Observable<PermitEntityDto[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_LIST_TO_GENERATE_FMRK);
        const params = new HttpParams()
            .set('permitTypeID', permitTypeID);
        return this.http.get<PermitEntityDto[]>(url, {params}).pipe(
            map(function (response: PermitEntityDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }


    public loadPermitAwardedListToGenerateFMarkAllAwarded(permitTypeID: string): Observable<PermitEntityDto[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_LIST_TO_GENERATE_FMARK_ALL_AWARDED);
        const params = new HttpParams()
            .set('permitTypeID', permitTypeID);
        return this.http.get<PermitEntityDto[]>(url, {params}).pipe(
            map(function (response: PermitEntityDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public loadPlantList(): Observable<PlantDetailsDto> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PLANT_LIST);
        return this.http.get<PlantDetailsDto>(url).pipe(
            map(function (response: PlantDetailsDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public loadCountryList(): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.MASTERS_COUNTRIES);
        return this.http.get<any>(url).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public loadSectionList(): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.SECTION_LIST);
        return this.http.get<any>(url).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public loadRegionList(): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.LOAD_REGIONS);
        return this.http.get<any>(url).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public loadPermitDetails(permitID: string): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_VIEW_DETAILS);
        const params = new HttpParams()
            .set('permitID', permitID);
        return this.http.get<any>(url, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public taskListFind(): Observable<TaskDto> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.MY_TASK_LIST);
        return this.http.get<TaskDto>(url).pipe(
            map(function (response: TaskDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public qaTaskListFind(): Observable<PermitEntityDto[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.MY_TASK_LIST);
        return this.http.get<PermitEntityDto[]>(url).pipe(
            map(function (response: PermitEntityDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public qaInternalUserTaskListFind(): Observable<PermitEntityDto[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.QA_INTERNAL_USER_ENDPOINT.LOAD_MY_TASK_LIST);
        return this.http.get<PermitEntityDto[]>(url).pipe(
            map(function (response: PermitEntityDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public viewSTA1Details(permitID: string): Observable<STA1> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_VIEW_STA1);
        const params = new HttpParams()
            .set('permitID', permitID);
        return this.http.get<STA1>(url, {params}).pipe(
            map(function (response: STA1) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public viewSTA3Details(permitID: string): Observable<STA3> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_VIEW_STA3);
        const params = new HttpParams()
            .set('permitID', permitID);
        return this.http.get<STA3>(url, {params}).pipe(
            map(function (response: STA3) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public viewSTA10FirmDetails(permitID: string): Observable<Sta10Dto> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_VIEW_STA10_FIRM_DETAILS);
        const params = new HttpParams()
            .set('permitID', permitID);
        return this.http.get<Sta10Dto>(url, {params}).pipe(
            map(function (response: Sta10Dto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public viewSTA10Details(permitID: string): Observable<AllSTA10DetailsDto> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_VIEW_STA10_DETAILS);
        const params = new HttpParams()
            .set('permitID', permitID);
        return this.http.get<AllSTA10DetailsDto>(url, {params}).pipe(
            map(function (response: AllSTA10DetailsDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public addInvoiceConsolidatedDetails(data: GenerateInvoiceDto): Observable<AllBatchInvoiceDetailsDto> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.INVOICE_CONSOLIDATE_ADD);
        return this.http.post<AllBatchInvoiceDetailsDto>(url, data).pipe(
            map(function (response: AllBatchInvoiceDetailsDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public createInvoiceConsolidatedDetails(data: GenerateInvoiceWithWithholdingDto): Observable<AllBatchInvoiceDetailsDto> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.INVOICE_CONSOLIDATE_SUBMIT);
        return this.http.post<AllBatchInvoiceDetailsDto>(url, data).pipe(
            map(function (response: AllBatchInvoiceDetailsDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public createInvoiceConsolidatedDifferenceDetails(data: GenerateInvoiceWithWithholdingDto): Observable<AllBatchInvoiceDetailsDto> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.INVOICE_CONSOLIDATE_DIFFERENCE_SUBMIT);
        return this.http.post<AllBatchInvoiceDetailsDto>(url, data).pipe(
            map(function (response: AllBatchInvoiceDetailsDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public removeInvoiceFromConsolidatedDetails(data: GenerateInvoiceWithWithholdingDto): Observable<AllBatchInvoiceDetailsDto> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.INVOICE_CONSOLIDATE_REMOVE);
        return this.http.post<AllBatchInvoiceDetailsDto>(url, data).pipe(
            map(function (response: AllBatchInvoiceDetailsDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public viewSTA10PersonnelDetails(qaSta10ID: string): Observable<STA10PersonnelDto[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_VIEW_STA10_PERSONNEL_DETAILS);
        const params = new HttpParams()
            .set('qaSta10ID', qaSta10ID);
        return this.http.get<STA10PersonnelDto[]>(url, {params}).pipe(
            map(function (response: STA10PersonnelDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public viewSTA10ProductsManufactureDetails(qaSta10ID: string): Observable<STA10ProductsManufactureDto> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_VIEW_STA10_PRODUCTS_BEING_MANUFACTURED);
        const params = new HttpParams()
            .set('qaSta10ID', qaSta10ID);
        return this.http.get<STA10ProductsManufactureDto>(url, {params}).pipe(
            map(function (response: STA10ProductsManufactureDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public viewSTA10RawMaterialsDetails(qaSta10ID: string): Observable<STA10ProductsManufactureDto> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_VIEW_STA10_RAW_MATERIAL);
        const params = new HttpParams()
            .set('qaSta10ID', qaSta10ID);
        return this.http.get<STA10ProductsManufactureDto>(url, {params}).pipe(
            map(function (response: STA10ProductsManufactureDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public loadInvoiceDetailsPDF(ID: string): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.INVOICE_DETAILS_PDF);
        const params = new HttpParams()
            .set('ID', ID);
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

    public loadInvoiceBreakDownDetailsPDF(ID: string): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_INVOICE_BREAK_DOWN_DETAILS_PDF);
        const params = new HttpParams()
            .set('ID', ID);
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

    public loadCertificateDetailsPDF(permitID: string): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_CERTIFICATE_ISSUED_DETAILS_PDF);
        const params = new HttpParams()
            .set('permitID', permitID);
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

    public loadFileDetailsPDF(fileID: string): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_VIEW_PDF);
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

    public savePermitSTA1(permitTypeID: string, data: STA1): Observable<STA1> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_APPLY_STA1);
        const params = new HttpParams()
            .set('permitTypeID', permitTypeID);
        return this.http.post<STA1>(url, data, {params}).pipe(
            map(function (response: STA1) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public generatePermitFMARK(data: FmarkEntityDto): Observable<AllPermitDetailsDto> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_APPLY_FMARK);
        return this.http.post<AllPermitDetailsDto>(url, data).pipe(
            map(function (response: AllPermitDetailsDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public savePermitProcessStep(data: PermitProcessStepDto): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_PROCESS_STEP);
        return this.http.post<any>(url, data).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public updatePermitSTA1(permitID: string, data: STA1): Observable<STA1> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_UPDATE_STA1);
        const params = new HttpParams()
            .set('permitID', permitID);
        return this.http.put<STA1>(url, data, {params}).pipe(
            map(function (response: STA1) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public uploadSTA3File(permitID: string, data: FormData): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.UPLOAD_FILE_STA3);
        // const params = new HttpParams()
        //     .set('permitID', permitID);
        return this.http.post<any>(url, data, {
            headers: {
                'enctype': 'multipart/form-data',
            }, params: {'permitID': permitID},
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

    public uploadFile(permitID: string, docDesc: string, data: FormData): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.UPLOAD_FILE);
        // const params = new HttpParams()
        //     .set('permitID', permitID);
        return this.http.post<any>(url, data, {
            headers: {
                'enctype': 'multipart/form-data',
            }, params: {'permitID': permitID, 'docDesc': docDesc},
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

    public uploadSTA10File(permitID: string, data: FormData): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.UPLOAD_FILE_STA10);
        // const params = new HttpParams()
        //     .set('permitID', permitID);
        return this.http.post<any>(url, data, {
            headers: {
                'enctype': 'multipart/form-data',
            }, params: {'permitID': permitID},
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

    public updateMigratedPermit(permitId: string, permitIdBeingMigrated: string, permitDto: PermitDto): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.UPDATE_PERMIT_MIGRATED);
        // const params = new HttpParams()
        //     .set('permitID', permitID);

        const params = new HttpParams()
            .set('permitID', permitId)
            .set('permitIdBeingMigrated', permitIdBeingMigrated);
        // return this.httpService.get<any>(`${this.baseUrl}/get/pdf/${fileName}`, { responseType: 'arraybuffer' as 'json' });
        return this.http.post<PermitDto>(url, permitDto, {params, responseType: 'arraybuffer' as 'json'}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }


    public savePermitSTA3(permitID: string, data: STA3): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_APPLY_STA3);
        const params = new HttpParams()
            .set('permitID', permitID);
        return this.http.post<any>(url, data, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public pushSTKInvoicePermit(data: MPesaPushDto): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.QA_MPESA_STK_PUSH);
        return this.http.post<any>(url, data).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public saveFirmDetailsSta10(permitID: string, data: Sta10Dto): Observable<Sta10Dto> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_APPLY_STA10_FIRM_DETAILS);
        const params = new HttpParams()
            .set('permitID', permitID);
        return this.http.post<Sta10Dto>(url, data, {params}).pipe(
            map(function (response: Sta10Dto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public updateFirmDetailsSta10(permitID: string, data: Sta10Dto): Observable<Sta10Dto> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_UPDATE_STA10_FIRM_DETAILS);
        const params = new HttpParams()
            .set('permitID', permitID);
        return this.http.put<Sta10Dto>(url, data, {params}).pipe(
            map(function (response: Sta10Dto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public savePersonnelDetailsSta10(qaSta10ID: string, data: STA10PersonnelDto[]): Observable<STA10PersonnelDto[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_APPLY_STA10_PERSONNEL_DETAILS);
        const params = new HttpParams()
            .set('qaSta10ID', qaSta10ID);
        return this.http.post<STA10PersonnelDto[]>(url, data, {params}).pipe(
            map(function (response: STA10PersonnelDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public updatePersonnelDetailsSta10(qaSta10ID: string, data: STA10PersonnelDto[]): Observable<STA10PersonnelDto[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_UPDATE_STA10_PERSONNEL_DETAILS);
        const params = new HttpParams()
            .set('qaSta10ID', qaSta10ID);
        return this.http.post<STA10PersonnelDto[]>(url, data, {params}).pipe(
            map(function (response: STA10PersonnelDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    // tslint:disable-next-line:max-line-length
    public saveProductsManufacturedDetailsSta10(qaSta10ID: string, data: STA10ProductsManufactureDto[]): Observable<STA10ProductsManufactureDto[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_APPLY_STA10_PRODUCTS_BEING_MANUFACTURED);
        const params = new HttpParams()
            .set('qaSta10ID', qaSta10ID);
        return this.http.post<STA10ProductsManufactureDto[]>(url, data, {params}).pipe(
            map(function (response: STA10ProductsManufactureDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    // tslint:disable-next-line:max-line-length
    public updateProductsManufacturedDetailsSta10(qaSta10ID: string, data: STA10ProductsManufactureDto[]): Observable<STA10ProductsManufactureDto[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_UPDATE_STA10_PRODUCTS_BEING_MANUFACTURED);
        const params = new HttpParams()
            .set('qaSta10ID', qaSta10ID);
        return this.http.post<STA10ProductsManufactureDto[]>(url, data, {params}).pipe(
            map(function (response: STA10ProductsManufactureDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public saveRawMaterialsDetailsSta10(qaSta10ID: string, data: STA10RawMaterialsDto[]): Observable<STA10RawMaterialsDto[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_APPLY_STA10_RAW_MATERIAL);
        const params = new HttpParams()
            .set('qaSta10ID', qaSta10ID);
        return this.http.post<STA10RawMaterialsDto[]>(url, data, {params}).pipe(
            map(function (response: STA10RawMaterialsDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public updateRawMaterialsDetailsSta10(qaSta10ID: string, data: STA10RawMaterialsDto[]): Observable<STA10RawMaterialsDto[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_UPDATE_STA10_RAW_MATERIAL);
        const params = new HttpParams()
            .set('qaSta10ID', qaSta10ID);
        return this.http.post<STA10RawMaterialsDto[]>(url, data, {params}).pipe(
            map(function (response: STA10RawMaterialsDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }


    public saveMachineryPlantDetailsSta10(qaSta10ID: string, data: STA10MachineryAndPlantDto[]): Observable<STA10MachineryAndPlantDto[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_APPLY_STA10_MACHINERY_PLANT);
        const params = new HttpParams()
            .set('qaSta10ID', qaSta10ID);
        return this.http.post<STA10MachineryAndPlantDto[]>(url, data, {params}).pipe(
            map(function (response: STA10MachineryAndPlantDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public updateMachineryPlantDetailsSta10(qaSta10ID: string, data: STA10MachineryAndPlantDto[]): Observable<STA10MachineryAndPlantDto[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_UPDATE_STA10_MACHINERY_PLANT);
        const params = new HttpParams()
            .set('qaSta10ID', qaSta10ID);
        return this.http.post<STA10MachineryAndPlantDto[]>(url, data, {params}).pipe(
            map(function (response: STA10MachineryAndPlantDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public saveManufacturingProcessDetailsSta10(qaSta10ID: string, data: STA10ManufacturingProcessDto[]): Observable<STA10ManufacturingProcessDto[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_APPLY_STA10_MANUFACTURING_PROCESS);
        const params = new HttpParams()
            .set('qaSta10ID', qaSta10ID);
        return this.http.post<STA10ManufacturingProcessDto[]>(url, data, {params}).pipe(
            map(function (response: STA10ManufacturingProcessDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    // tslint:disable-next-line:max-line-length
    public updateManufacturingProcessDetailsSta10(qaSta10ID: string, data: STA10ManufacturingProcessDto[]): Observable<STA10ManufacturingProcessDto[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_UPDATE_STA10_MANUFACTURING_PROCESS);
        const params = new HttpParams()
            .set('qaSta10ID', qaSta10ID);
        return this.http.post<STA10ManufacturingProcessDto[]>(url, data, {params}).pipe(
            map(function (response: STA10ManufacturingProcessDto[]) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }


    public updatePermitSTA3(permitID: string, data: STA3): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_UPDATE_STA3);
        const params = new HttpParams()
            .set('permitID', permitID);
        return this.http.put<any>(url, data, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public submitPermitForReview(permitID: string): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_SUBMIT_DETAILS_FOR_REVIEW);
        const params = new HttpParams()
            .set('permitID', permitID);
        return this.http.post<any>(url, null, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public submitPermitForReviewHODQAM(permitID: string): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_SUBMIT_DETAILS_FOR_HOD_QAM_REVIEW);
        const params = new HttpParams()
            .set('permitID', permitID);
        return this.http.post<any>(url, null, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public submitPermitApplication(permitID: string): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_SUBMIT_APPLICATION);
        const params = new HttpParams()
            .set('permitID', permitID);
        return this.http.post<any>(url, null, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public submitPermitRenewApplication(permitID: string): Observable<AllPermitDetailsDto> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_RENEW);
        const params = new HttpParams()
            .set('permitID', permitID);
        return this.http.post<AllPermitDetailsDto>(url, null, {params}).pipe(
            map(function (response: AllPermitDetailsDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public submitSSCApprovalRejection(permitID: string, data: SSCApprovalRejectionDto): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_SUBMIT_SSC_APPROVAL_REJECTION);
        const params = new HttpParams()
            .set('permitID', permitID);
        return this.http.post<any>(url, data, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public submitPermitGenerateDifference(permitID: string): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.QA_MANUFACTURE_ENDPOINT.GENERATE_INVOICE_DIFFERENCE);
        const params = new HttpParams()
            .set('permitID', permitID);
        return this.http.post<any>(url, null, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public submitPermitReGenerateInvoice(permitID: string): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.QA_MANUFACTURE_ENDPOINT.RE_GENERATE_INVOICE);
        const params = new HttpParams()
            .set('permitID', permitID);
        return this.http.post<any>(url, null, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public resubmitApplicationBack(permitID: string, data: ResubmitApplicationDto): Observable<PermitEntityDetails> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_RE_SUBMIT_APPLICATION);
        const params = new HttpParams()
            .set('permitID', permitID);
        return this.http.post<PermitEntityDetails>(url, data, {params}).pipe(
            map(function (response: PermitEntityDetails) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public loadAllPayments(): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.VIEW_ALL_PAYMENTS);
        return this.http.get<any>(url).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }


}
