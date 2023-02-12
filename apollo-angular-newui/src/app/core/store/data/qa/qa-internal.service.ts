import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpParams} from "@angular/common/http";
import {Observable, throwError} from "rxjs";
import {
    AllInspectionDetailsApplyDto,
    HaccpImplementationDetailsApplyDto,
    InspectionDetailsDto, InspectionDetailsDtoB,
    InspectionReportProcessStepDto, OperationProcessAndControlsDetailsApplyDto,
    PermitProcessStepDto, ProductLabellingDto, StandardizationMarkSchemeDto,
    TechnicalDetailsDto
} from "./qa.model";
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

    public checkIfInspectionReportExists(permitId: string): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.QA_INTERNAL_USER_ENDPOINT.CHECK_IF_INSPECTION_REPORT_EXISTS);
        const params = new HttpParams().set('permitID', permitId);
        return this.http.post<any>(url, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public saveInspectionReportTechnicalDetails(permitId: string, data: TechnicalDetailsDto): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.QA_INTERNAL_USER_ENDPOINT.NEW_INSPECTION_TECHNICAL_REPORT);
        const params = new HttpParams()
            .set('permitID', permitId);
        return this.http.post<TechnicalDetailsDto>(url, data, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }
    public updateInspectionReportTechnicalDetails(permitId: string, data: InspectionDetailsDto): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.QA_INTERNAL_USER_ENDPOINT.TECHNICAL_REPORT_DETAILS);
        const params = new HttpParams()
            .set('permitID', permitId);
        return this.http.post<InspectionDetailsDto>(url, data, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public updateInspectionReportTechnicalDetailsB(permitId: string, data: InspectionDetailsDtoB): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.QA_INTERNAL_USER_ENDPOINT.TECHNICAL_REPORT_DETAILS_B);
        const params = new HttpParams()
            .set('permitID', permitId);
        return this.http.post<InspectionDetailsDtoB>(url, data, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }
    public updateInspectionReportProductLabelling(permitId: string,inspectionReportId: string, data: ProductLabellingDto): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.QA_INTERNAL_USER_ENDPOINT.PRODUCT_LABELLING);
        const params = new HttpParams()
            .set('permitID', permitId)
            .set('inspectionReportId', inspectionReportId);
        return this.http.post<ProductLabellingDto>(url, data, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }
    public updateInspectionReportStandardization(inspectionReportId: string, data: StandardizationMarkSchemeDto): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.QA_INTERNAL_USER_ENDPOINT.STANDARDIZATION_MARK);
        const params = new HttpParams()
            .set('inspectionReportId', inspectionReportId);
        return this.http.post<StandardizationMarkSchemeDto>(url, data, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }


    public updateInspectionReportOperation(permitId: string,inspectionReportId: string, data: OperationProcessAndControlsDetailsApplyDto): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.QA_INTERNAL_USER_ENDPOINT.OPERATION_PROCESS_CONTROLS);
        const params = new HttpParams()
            .set('permitID', permitId)
            .set('inspectionReportId', inspectionReportId);
        return this.http.post<OperationProcessAndControlsDetailsApplyDto>(url, data, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public updateInspectionReportHaccp(permitId: string,inspectionReportId: string, data: HaccpImplementationDetailsApplyDto): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.QA_INTERNAL_USER_ENDPOINT.HACCP_IMPLEMENTATION);
        const params = new HttpParams()
            .set('permitID', permitId)
            .set('inspectionReportId', inspectionReportId);
        return this.http.post<HaccpImplementationDetailsApplyDto>(url, data, {params}).pipe(
            map(function (response: any
            ) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public inspectionReportFinalSave(permitId: string,inspectionReportId: string, data: AllInspectionDetailsApplyDto): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.QA_INTERNAL_USER_ENDPOINT.FINAL_INSPECTION_REPORT_SUBMISSION);
        const params = new HttpParams()
            .set('permitID', permitId)
        return this.http.post<AllInspectionDetailsApplyDto>(url, data, {params}).pipe(
            map(function (response: any) {
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
