import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpParams} from '@angular/common/http';
import {LoggedInUser, LoginCredentials} from '../auth';
import {Observable, throwError} from 'rxjs';
import {ApiEndpointService} from '../../../services/endpoints/api-endpoint.service';
import {catchError, map} from 'rxjs/operators';
import {BrsLookUpRequest, Company} from "../companies";
import {
    STA1,
    Sta10Dto,
    STA10MachineryAndPlantDto, STA10ManufacturingProcessDto,
    STA10PersonnelDto,
    STA10ProductsManufactureDto,
    STA10RawMaterialsDto,
    STA3
} from "./qa.model";

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

    public loadPermitList(permitTypeID: string): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_LIST);
        const params = new HttpParams()
            .set('permitTypeID', permitTypeID);
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

    public loadPlantList(): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PLANT_LIST);
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

    public loadInvoiceDetailsPDF(ID: string): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.INVOICE_DETAILS_PDF);
        const params = new HttpParams()
            .set('ID', ID);
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

    public savePermitSTA1(permitTypeID: string, data: STA1): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_APPLY_STA1);
        const params = new HttpParams()
            .set('permitTypeID', permitTypeID);
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

    public updatePermitSTA1(permitID: string, data: STA1): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_UPDATE_STA1);
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

    public saveProductsManufacturedDetailsSta10(qaSta10ID: string, data: STA10PersonnelDto[]): Observable<STA10ProductsManufactureDto[]> {
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

}
