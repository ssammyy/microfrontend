import {Injectable} from '@angular/core';
import {Observable, throwError} from "rxjs";
import {ApiEndpointService} from "../../../services/endpoints/api-endpoint.service";
import {HttpClient, HttpErrorResponse, HttpParams} from "@angular/common/http";
import {catchError, map} from "rxjs/operators";

@Injectable({
    providedIn: 'root'
})
export class PVOCService {

    constructor(private http: HttpClient) {
    }

    public checkExemptionApplicable(batchID: string): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.INVOICE_DETAILS);
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
}
