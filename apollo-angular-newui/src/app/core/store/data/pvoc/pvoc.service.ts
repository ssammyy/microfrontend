import {Injectable} from '@angular/core';
import {Observable, throwError} from "rxjs";
import {ApiEndpointService} from "../../../services/endpoints/api-endpoint.service";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {catchError, map} from "rxjs/operators";

@Injectable({
    providedIn: 'root'
})
export class PVOCService {

    constructor(private http: HttpClient) {
    }

    public checkExemptionApplicable(): Observable<any> {
        const url = ApiEndpointService.getEndpoint("/api/v1/pvoc/exemption/check/eligible");
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

    public getWaiverCategories(): Observable<any> {
        const url = ApiEndpointService.getEndpoint("/api/v1/pvoc/waiver/categories");
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

    public applyForImportExemption(data: any, files: File[]): Observable<any> {
        const url = ApiEndpointService.getEndpoint("/api/v1/pvoc/exemption/apply");
        return this.sendFiles(data, files, url)
    }

    private sendFiles(data: any, files: File[], url: string) {
        console.log(url)
        let form = new FormData()
        form.append("data", JSON.stringify(data))

        for (let filesKey in files) {
            form.append("files", filesKey);
        }
        return this.http.post<any>(url, form).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    public applyForImportWaiver(data: any, files: File[]): Observable<any> {
        const url = ApiEndpointService.getEndpoint("/api/v1/pvoc/waiver/apply");
        console.log(url)
        return this.sendFiles(data, files, url)
    }
}
