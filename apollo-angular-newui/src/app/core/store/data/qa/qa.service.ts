import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpParams} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {ApiEndpointService} from '../../../services/endpoints/api-endpoint.service';
import {catchError, map} from 'rxjs/operators';
import {
    AllBatchInvoiceDetailsDto,
    AllPermitDetailsDto,
    AllSTA10DetailsDto,
    FmarkEntityDto,
    GenerateInvoiceDto,
    MPesaPushDto,
    PermitEntityDetails,
    PermitEntityDto,
    PermitProcessStepDto, PlantDetailsDto, QRCodeScannedQADto,
    ResubmitApplicationDto,
    SSCApprovalRejectionDto,
    STA1,
    Sta10Dto,
    STA10MachineryAndPlantDto,
    STA10ManufacturingProcessDto,
    STA10PersonnelDto,
    STA10ProductsManufactureDto,
    STA10RawMaterialsDto,
    STA3, StgInvoiceBalanceDto,
    TaskDto
} from './qa.model';

@Injectable({
    providedIn: 'root'
})
export class QaService {

    constructor(private http: HttpClient) {
    }

    public loadFirmPermitList(companyID: string): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.FIRM_PERMIT_LIST);
        const params = new HttpParams()
            .set('companyID', companyID);
        return this.http.get<any>(url, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
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
            })
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
            })
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
            })
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
            })
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
            })
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
            })
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
            })
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
            })
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
            })
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
            })
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
            })
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
            })
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
            })
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
            })
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
            })
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
            })
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
            })
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
            })
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
            })
        );
    }

    public createInvoiceConsolidatedDetails(data: GenerateInvoiceDto): Observable<AllBatchInvoiceDetailsDto> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.INVOICE_CONSOLIDATE_SUBMIT);
        return this.http.post<AllBatchInvoiceDetailsDto>(url, data).pipe(
            map(function (response: AllBatchInvoiceDetailsDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    public removeInvoiceFromConsolidatedDetails(data: GenerateInvoiceDto): Observable<AllBatchInvoiceDetailsDto> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.INVOICE_CONSOLIDATE_REMOVE);
        return this.http.post<AllBatchInvoiceDetailsDto>(url, data).pipe(
            map(function (response: AllBatchInvoiceDetailsDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
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
            })
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
            })
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
            })
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
            })
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
            })
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
            })
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
            })
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
            })
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
            })
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
            })
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
            })
        );
    }

    public uploadSTA3File(permitID: string, data: FormData): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.UPLOAD_FILE_STA3);
        // const params = new HttpParams()
        //     .set('permitID', permitID);
        return this.http.post<any>(url, data, {
            headers: {
                'enctype': 'multipart/form-data'
            }, params: {'permitID': permitID}
        }).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    public uploadFile(permitID: string, docDesc: string, data: FormData): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.UPLOAD_FILE);
        // const params = new HttpParams()
        //     .set('permitID', permitID);
        return this.http.post<any>(url, data, {
            headers: {
                'enctype': 'multipart/form-data'
            }, params: {'permitID': permitID, 'docDesc': docDesc}
        }).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    public uploadSTA10File(permitID: string, data: FormData): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.UPLOAD_FILE_STA10);
        // const params = new HttpParams()
        //     .set('permitID', permitID);
        return this.http.post<any>(url, data, {
            headers: {
                'enctype': 'multipart/form-data'
            }, params: {'permitID': permitID}
        }).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
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
            })
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
            })
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
            })
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
            })
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
            })
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
            })
        );
    }

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
            })
        );
    }

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
            })
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
            })
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
            })
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
            })
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
            })
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
            })
        );
    }

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
            })
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
            })
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
            })
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
            })
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
            })
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
            })
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
            })
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
            })
        );
    }


}
