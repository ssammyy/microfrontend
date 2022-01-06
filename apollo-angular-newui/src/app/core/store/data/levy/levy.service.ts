import { Injectable } from '@angular/core';
import {Observable, throwError} from "rxjs";
import {ApiEndpointService} from "../../../services/endpoints/api-endpoint.service";
import {HttpClient, HttpErrorResponse, HttpParams} from "@angular/common/http";
import {catchError, map} from "rxjs/operators";
import {
    ApproveVisitTask,
    CompanyModel,
    ManufactureInfo,
    ManufacturePenalty,
    PaidLevy, ReportDecisionLevelOne, ReportDecisionLevelTwo, SiteVisitFeedBack, SiteVisitReport,
    SLevySL1,
    StdLevyScheduleSiteVisitDTO, VisitTask
} from "./levy.model";
import {DiSdtDECISION, KnwSecTasks, NWADiSdtJustification, NWAJustification} from "../std/std.model";

@Injectable({
  providedIn: 'root'
})
export class LevyService {

  constructor(private http: HttpClient) { }

  public addSL1Details(sLevySL1: SLevySL1): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_SL10_FORM);
    const params = new HttpParams();
    return this.http.post<SLevySL1>(url, sLevySL1, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }
    public getManufacturerPenaltyHistory(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_PENALTY_DETAILS);
        const params = new HttpParams();
        return this.http.get<ManufacturePenalty>(url, {params}).pipe();
    }
    public getPaidLevies(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_PAID_DETAILS);
        const params = new HttpParams();
        return this.http.get<PaidLevy>(url, {params}).pipe();
    }
    public getCompanyProfile(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_COMPANY_DETAILS);
        const params = new HttpParams();
        return this.http.get<CompanyModel>(url, {params}).pipe();
    }
    public scheduleSiteVisit(stdLevyScheduleSiteVisitDTO: StdLevyScheduleSiteVisitDTO): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_SCHEDULE_SITE_VISIT);
        const params = new HttpParams();
        return this.http.post<StdLevyScheduleSiteVisitDTO>(url, stdLevyScheduleSiteVisitDTO, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }
    public getScheduledVisits(): Observable<VisitTask[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_SCHEDULED_SITE_VISITS);
        const params = new HttpParams();
        return this.http.get<VisitTask[]>(url, {params}).pipe();
    }

    public saveSiteVisitReport(siteVisitReport: SiteVisitReport): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_SAVE_VISIT_REPORT);
        const params = new HttpParams();
        return this.http.post<SiteVisitReport>(url, siteVisitReport, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    public uploadFileDetails(reportFileID: string, data: FormData): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_SAVE_VISIT_REPORT_DOCUMENT);

        return this.http.post<any>(url, data, {
            headers: {
                'enctype': 'multipart/form-data'
            }, params: {'reportFileID': reportFileID}
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
    public getSiteReport(): Observable<ApproveVisitTask[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_SCHEDULED_SITE_VISITS_REPORT);
        const params = new HttpParams();
        return this.http.get<ApproveVisitTask[]>(url, {params}).pipe();
    }

    public levelOneDecisionOnReport(reportDecisionLevelOne: ReportDecisionLevelOne): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_SITE_VISITS_REPORT_APPROVAL_ONE);
        const params = new HttpParams();
        return this.http.post<ApproveVisitTask>(url, reportDecisionLevelOne, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }
    public viewReportDoc(reportFileID: any): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_SAVE_VISIT_REPORT_DOCUMENT);
        const params = new HttpParams()
            .set('reportFileID', reportFileID);
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
    public getSiteReportLevelTwo(): Observable<ApproveVisitTask[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_SITE_VISITS_REPORT_LEVEL_TWO);
        const params = new HttpParams();
        return this.http.get<ApproveVisitTask[]>(url, {params}).pipe();
    }

    public decisionOnSiteReportLevelTwo(reportDecisionLevelTwo: ReportDecisionLevelTwo): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_SITE_VISITS_REPORT_APPROVAL_TWO);
        const params = new HttpParams();
        return this.http.post<ApproveVisitTask>(url, reportDecisionLevelTwo, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }
    public saveSiteVisitFeedback(siteVisitFeedBack: SiteVisitFeedBack): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_SAVE_VISIT_REPORT_FEEDBACK);
        const params = new HttpParams();
        return this.http.post<SiteVisitFeedBack>(url, siteVisitFeedBack, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }
    public getSiteFeedback(): Observable<VisitTask[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_SCHEDULED_SITE_VISITS_REPORT);
        const params = new HttpParams();
        return this.http.get<VisitTask[]>(url, {params}).pipe();
    }


}
