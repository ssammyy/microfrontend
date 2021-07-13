import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpParams} from '@angular/common/http';
import {LoggedInUser, LoginCredentials} from '../auth';
import {Observable, throwError} from 'rxjs';
import {ApiEndpointService} from '../../../services/endpoints/api-endpoint.service';
import {catchError, map} from 'rxjs/operators';
import {BrsLookUpRequest, Company} from "../companies";
import {STA1, STA3} from "./qa.model";

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

    public saveFirmDetailsSta10(permitID: string, data: STA3): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_APPLY_STA10_FIRM_DETAILS);
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
