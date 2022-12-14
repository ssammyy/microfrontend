import {Injectable} from '@angular/core';
import {ApiEndpointService} from "../../../services/endpoints/api-endpoint.service";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {SchemeMembership} from "./std.model";
import {Observable, throwError} from "rxjs";
import {catchError, map} from "rxjs/operators";

@Injectable({
    providedIn: 'root'
})
export class SchemeMembershipService {
    protocol = `https://`;
    baseUrl = ApiEndpointService.DOMAIN.LOCAL_DEV
    private apiServerUrlAnonymous = `${this.protocol}${this.baseUrl}/api/v1/migration/anonymous/schemeMembership/`;
    private apiServerUrl = `${this.protocol}${this.baseUrl}/api/v1/migration/schemeMembership/`;


    constructor(private http: HttpClient) {
    }

    public addStandardRequest(schemeMembership: SchemeMembership): Observable<any> {
        const url = `${this.apiServerUrlAnonymous}join_request_received`;
        return this.http.post<SchemeMembership>(url, schemeMembership).pipe(
            map(function (response: SchemeMembership) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    public getHodTasks(): Observable<SchemeMembership[]> {
        return this.http.get<SchemeMembership[]>(`${this.apiServerUrl}` + 'hod/tasks')
    }


    public assignTask(schemeMembership: SchemeMembership): Observable<any> {
        const url = `${this.apiServerUrl}assignTask`;
        return this.http.post<SchemeMembership>(url, schemeMembership).pipe(
            map(function (response: SchemeMembership) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    public getSICTasks(): Observable<SchemeMembership[]> {
        return this.http.get<SchemeMembership[]>(`${this.apiServerUrl}` + 'sic/tasks')
    }

    public generateInvoice(schemeMembership: SchemeMembership): Observable<any> {
        const url = `${this.apiServerUrl}generateInvoice`;
        return this.http.post<SchemeMembership>(url, schemeMembership).pipe(
            map(function (response: SchemeMembership) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    public getAllPendingInvoices(): Observable<SchemeMembership[]> {
        return this.http.get<SchemeMembership[]>(`${this.apiServerUrl}` + 'sic/getAllPendingInvoices')
    }


    public updatePayment(schemeMembership: SchemeMembership): Observable<any> {
        const url = `${this.apiServerUrl}updatePayment`;
        return this.http.post<SchemeMembership>(url, schemeMembership).pipe(
            map(function (response: SchemeMembership) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    public getAllPaidInvoices(): Observable<SchemeMembership[]> {
        return this.http.get<SchemeMembership[]>(`${this.apiServerUrl}` + 'sic/getAllPaidInvoices')
    }

    public addToWebStore(schemeMembership: SchemeMembership): Observable<any> {
        const url = `${this.apiServerUrl}addToWebStore`;
        return this.http.post<SchemeMembership>(url, schemeMembership).pipe(
            map(function (response: SchemeMembership) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }


}
