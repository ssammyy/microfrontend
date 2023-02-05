import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpParams} from "@angular/common/http";
import {Observable, throwError} from "rxjs";
import {FirmTypeEntityDto, MyTasksPermitEntityDto, PermitEntityDto} from "./qa.model";
import {ApiEndpointService} from "../../../services/endpoints/api-endpoint.service";
import {catchError, map} from "rxjs/operators";
import {formatDate} from "@angular/common";
import {ApiResponseModel} from "../ms/ms.model";

@Injectable({
    providedIn: 'root'
})
export class QaInternalService {
    dateFormat = "yyyy-MM-dd";
    language = "en";
    constructor(private http: HttpClient) {
    }

    formatFormDate(date: Date) {
        return formatDate(date, this.dateFormat, this.language);
    }


    public loadMyTasksByPermitType(permitTypeID: number): Observable<ApiResponseModel> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.QA_INTERNAL_USER_ENDPOINT.LOAD_MY_TASK_LIST);
        const params = new HttpParams()
            .set('permitTypeID', String(permitTypeID))
        return this.http.get<ApiResponseModel>(url, {params}).pipe(
            map(function (response: ApiResponseModel) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            }),
        );
    }
    public loadPermitDetail(permitID: number): Observable<ApiResponseModel> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.QA_INTERNAL_USER_ENDPOINT.LOAD_PERMIT_DETAIL);
        const params = new HttpParams()
            .set('permitID', String(permitID))
        return this.http.get<ApiResponseModel>(url, {params}).pipe(
            map(function (response: ApiResponseModel) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            }),
        );
    }


}
