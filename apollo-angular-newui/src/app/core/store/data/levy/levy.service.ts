import { Injectable } from '@angular/core';
import {Observable, throwError} from "rxjs";
import {ApiEndpointService} from "../../../services/endpoints/api-endpoint.service";
import {HttpClient, HttpErrorResponse, HttpParams} from "@angular/common/http";
import {catchError, map} from "rxjs/operators";
import {
    ApproveVisitTask,
    AssignCompanyTaskDTO,
    CompanyModel,
    ConfirmEditCompanyDTO,
    EditCompanyDTO,
    ManufactureCompletedTask,
    ManufactureCompleteTask,
    ManufactureDetailList,
    ManufactureInfo,
    ManufacturePenalty,
    ManufacturePendingTask,
    ManufacturingStatus,
    PaidLevy,
    ReportDecisionLevelOne,
    ReportDecisionLevelTwo,
    SiteVisitFeedBack,
    SiteVisitReport,
    SLevySL1,
    StdLevyScheduleSiteVisitDTO,
    UserEntityRoles,
    UsersEntityList,
    VisitTask
} from "./levy.model";
import {UsersEntity} from "../std/std.model";

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

    public getManufacturerStatus(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_MANUFACTURE_STATUS);
        const params = new HttpParams();
        return this.http.get<ManufacturingStatus>(url, {params}).pipe();
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

    public assignCompanyTasks(assignCompanyTaskDTO: AssignCompanyTaskDTO): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_ASSIGN_COMPANY_TASK);
        const params = new HttpParams();
        return this.http.post<AssignCompanyTaskDTO>(url, assignCompanyTaskDTO, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }
    public editCompany(editCompanyDTO: EditCompanyDTO): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_EDIT_COMPANY);
        const params = new HttpParams();
        return this.http.post<EditCompanyDTO>(url, editCompanyDTO, {params}).pipe(
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
        return this.http.post<ReportDecisionLevelOne>(url, reportDecisionLevelOne, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }
    public viewReportDoc(visitID: any): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_VIEW_VISIT_REPORT_DOCUMENT);
        const params = new HttpParams().set('visitID', visitID);
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
        return this.http.post<ReportDecisionLevelTwo>(url, reportDecisionLevelTwo, {params}).pipe(
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

    public getMnCompleteTask(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_MANUFACTURE_COMPLETE_TASKS);
        const params = new HttpParams();
        return this.http.get<ManufactureCompletedTask>(url, {params}).pipe();
    }

    public getManufacturerList(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_MANUFACTURE_LIST);
        const params = new HttpParams();
        return this.http.get<ManufactureDetailList>(url, {params}).pipe();
    }

    public getMnPendingTask(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_MANUFACTURE_TASKS);
        const params = new HttpParams();
        return this.http.get<ManufacturePendingTask>(url, {params}).pipe();
    }

    public viewFeedBackReport(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_VIEW_VISIT_REPORT_FEEDBACK);
        const params = new HttpParams();
        return this.http.get<ManufacturePendingTask>(url, {params}).pipe();
    }


    public getUserList(): Observable<UsersEntity[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_LIST_OF_USERS);
        const params = new HttpParams();
        return this.http.get<UsersEntity[]>(url, {params}).pipe();
    }
    public getUserDetails(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_MANUFACTURE_USERS);
        const params = new HttpParams();
        return this.http.get<UsersEntityList>(url, {params}).pipe();
    }
    public getUserRoles(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_MANUFACTURE_ROLES);
        const params = new HttpParams();
        return this.http.get<UserEntityRoles>(url, {params}).pipe();
    }
    public getPlList(): Observable<UsersEntity[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_LIST_OF_USERS_PL);
        const params = new HttpParams();
        return this.http.get<UsersEntity[]>(url, {params}).pipe();
    }
    public getSlLvTwoList(): Observable<UsersEntity[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_LIST_OF_USERS_LV);
        const params = new HttpParams();
        return this.http.get<UsersEntity[]>(url, {params}).pipe();
    }

    // public getCompanyEditedDetails(manufactureId: any): any {
    //     const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_EDITED_COMPANY_DATA);
    //     const params = new HttpParams();
    //     return this.http.get<EditCompanyDTO>(url, {params}).pipe();
    // }

    public getCompanyEditedDetails(manufactureId: any): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_EDITED_COMPANY_DATA);
        const params = new HttpParams().set('manufactureId', manufactureId);
        return this.http.get<ConfirmEditCompanyDTO>(url, {params}).pipe();
    }

    public editCompanyDetailsConfirm(editCompanyDTO: EditCompanyDTO): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_COMPANY_EDIT_COMPANY_DATA);
        const params = new HttpParams();
        return this.http.post<EditCompanyDTO>(url, editCompanyDTO, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    public getApproveLevelOne(): Observable<UsersEntity[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_APPROVED_USERS_LEVEL_ONE);
        const params = new HttpParams();
        return this.http.get<UsersEntity[]>(url, {params}).pipe();
    }

    public getApproveLevelTwo(): Observable<UsersEntity[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_APPROVED_USERS_LEVEL_TWO);
        const params = new HttpParams();
        return this.http.get<UsersEntity[]>(url, {params}).pipe();
    }

    public getApproveLevelThree(): Observable<UsersEntity[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_APPROVED_USERS_LEVEL_THREE);
        const params = new HttpParams();
        return this.http.get<UsersEntity[]>(url, {params}).pipe();
    }

    public getAssignLevelOne(): Observable<UsersEntity[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_ASSIGN_USERS_LEVEL_ONE);
        const params = new HttpParams();
        return this.http.get<UsersEntity[]>(url, {params}).pipe();
    }

    public getAssignLevelTwo(): Observable<UsersEntity[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_ASSIGN_USERS_LEVEL_TWO);
        const params = new HttpParams();
        return this.http.get<UsersEntity[]>(url, {params}).pipe();
    }

    public getAssignLevelThree(): Observable<UsersEntity[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_ASSIGN_USERS_LEVEL_THREE);
        const params = new HttpParams();
        return this.http.get<UsersEntity[]>(url, {params}).pipe();
    }
    public getSLNotificationStatus(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_NOTIFICATION_FORM_STATUS);
        const params = new HttpParams();
        return this.http.get<ManufacturingStatus>(url, {params}).pipe();
    }




}
