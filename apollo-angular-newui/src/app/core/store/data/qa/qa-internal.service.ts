import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpParams} from "@angular/common/http";
import {Observable, throwError} from "rxjs";
import {InspectionReportProcessStepDto, PermitProcessStepDto, TechnicalDetailsDto} from "./qa.model";
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


    public submitQaMCompleteness(permitId: string, data: any): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.QA_INTERNAL_USER_ENDPOINT.QAM_COMPLETENESS);
        const params = new HttpParams()
            .set('permitId', permitId);
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

    public saveInspectionReportTechnicalDetails(permitId: string, data: TechnicalDetailsDto): Observable<TechnicalDetailsDto> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_APPLY_STA1);
        const params = new HttpParams()
            .set('permitId', permitId);
        return this.http.post<TechnicalDetailsDto>(url, data, {params}).pipe(
            map(function (response: TechnicalDetailsDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public updateInspectionReportTechnicalDetails(inspectionReportId: string, data: TechnicalDetailsDto): Observable<TechnicalDetailsDto> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PERMIT_UPDATE_STA1);
        const params = new HttpParams()
            .set('inspectionReportId', inspectionReportId);
        return this.http.put<TechnicalDetailsDto>(url, data, {params}).pipe(
            map(function (response: TechnicalDetailsDto) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public saveInspectionReportProcessStep(data: InspectionReportProcessStepDto): Observable<any> {
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

}
