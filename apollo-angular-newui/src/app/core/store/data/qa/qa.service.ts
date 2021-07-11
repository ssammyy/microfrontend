import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpParams} from '@angular/common/http';
import {LoggedInUser, LoginCredentials} from '../auth';
import {Observable, throwError} from 'rxjs';
import {ApiEndpointService} from '../../../services/endpoints/api-endpoint.service';
import {catchError, map} from 'rxjs/operators';

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

    public loadDMARKPermitList(permitTypeID: string): Observable<any> {
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

}
