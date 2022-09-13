import {Injectable} from '@angular/core';
import {Observable, throwError} from "rxjs";
import {ApiEndpointService} from "../../../services/endpoints/api-endpoint.service";
import {HttpClient, HttpErrorResponse, HttpParams} from "@angular/common/http";
import {catchError, map} from "rxjs/operators";
import {
    ApproveVisitTask,
    AssignCompanyTaskDTO, Branch, CloseCompanyDto, ClosedCompanyDTO,
    CompanyModel, CompanyOperationsDto,
    ConfirmEditCompanyDTO, DefaulterDetails,
    DirectorsList,
    DocumentDTO,
    EditCompanyDTO, EmailVerificationStatus,
    ManufactureCompletedTask,
    ManufactureDetailList,
    ManufacturePenalty,
    ManufacturePendingTask,
    ManufacturingStatus, NotificationStatus, OperationStatus,
    PaidLevy, PaymentDetails, PenaltyDetails,
    ReportDecisionLevelOne,
    ReportDecisionLevelTwo, SendEmailDto,
    SiteVisitFeedBack,
    SiteVisitRemarks,
    SiteVisitReport,
    SLevySL1, SlModel,
    StdLevyScheduleSiteVisitDTO, SuspendCompanyDto, SuspendedCompanyDTO,
    UserEntityRoles,
    UsersEntityList, VerifyEmailDto,
    VisitTask
} from "./levy.model";
import {UsersEntity} from "../std/std.model";
import swal from "sweetalert2";

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
    public getCompanySLForm(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_SL_FORM);
        const params = new HttpParams();
        return this.http.get<SlModel>(url, {params}).pipe();
    }

    public getCompanySLForms(manufactureId: any): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_SL_NT_FORM);
        const params = new HttpParams().set('manufactureId', manufactureId);
        return this.http.get<SlModel>(url, {params}).pipe();
    }



    public getManufacturerStatus(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_MANUFACTURE_STATUS);
        const params = new HttpParams();
        return this.http.get<ManufacturingStatus>(url, {params}).pipe();
    }

    public getOperationStatus(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_COMPANY_STATUS);
        const params = new HttpParams();
        return this.http.get<OperationStatus>(url, {params}).pipe();
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

    public getCompanyDirectors(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_DIRECTORS_LIST);
        const params = new HttpParams();
        return this.http.get<DirectorsList>(url, {params}).pipe();
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

    public getVisitDocumentList(visitId: any): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_VIEW_VISIT_REPORT_DOCUMENT_LIST);
        const params = new HttpParams().set('visitId', visitId);
        return this.http.get<DocumentDTO[]>(url, {params}).pipe();
    }

    public getCompanyEditedDetails(manufactureId: any): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_EDITED_COMPANY_DATA);
        const params = new HttpParams().set('manufactureId', manufactureId);
        return this.http.get<ConfirmEditCompanyDTO>(url, {params}).pipe();
    }
    public getSiteVisitRemarks(siteVisitId: any): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_SITE_VISIT_REMARKS);
        const params = new HttpParams().set('siteVisitId', siteVisitId);
        return this.http.get<SiteVisitRemarks>(url, {params}).pipe();
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
    public editCompanyDetailsConfirmLevelOne(editCompanyDTO: EditCompanyDTO): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_EDIT_COMPANY_LEVEL_ONE);
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

    public editCompanyDetailsConfirmLevelTwo(editCompanyDTO: EditCompanyDTO): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_EDIT_COMPANY_LEVEL_TWO);
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
        return this.http.get<NotificationStatus>(url, {params}).pipe();
    }

    public getBranchName(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_BRANCH_NAME);
        const params = new HttpParams();
        return this.http.get<Branch>(url, {params}).pipe();
    }

    public suspendCompanyOperations(suspendCompanyDto: SuspendCompanyDto): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_SUSPEND_OPERATIONS);
        const params = new HttpParams();
        return this.http.post<SuspendCompanyDto>(url, suspendCompanyDto, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    public resumeCompanyOperations(suspendCompanyDto: SuspendCompanyDto): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_RESUME_OPERATIONS);
        const params = new HttpParams();
        return this.http.post<SuspendCompanyDto>(url, suspendCompanyDto, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    public confirmCompanySuspension(companyOperationsDto: CompanyOperationsDto): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_APPROVE_SUSPENSION);
        const params = new HttpParams();
        return this.http.post<CompanyOperationsDto>(url, companyOperationsDto, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }
    public confirmCompanyResumption(companyOperationsDto: CompanyOperationsDto): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_APPROVE_RESUMPTION);
        const params = new HttpParams();
        return this.http.post<CompanyOperationsDto>(url, companyOperationsDto, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    public rejectCompanySuspension(companyOperationsDto: CompanyOperationsDto): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_REJECT_SUSPENSION);
        const params = new HttpParams();
        return this.http.post<CompanyOperationsDto>(url, companyOperationsDto, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    public rejectCompanyResumption(companyOperationsDto: CompanyOperationsDto): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_REJECT_RESUMPTION);
        const params = new HttpParams();
        return this.http.post<CompanyOperationsDto>(url, companyOperationsDto, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    public confirmCompanyClosure(companyOperationsDto: CompanyOperationsDto): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_APPROVE_CLOSURE);
        const params = new HttpParams();
        return this.http.post<CompanyOperationsDto>(url, companyOperationsDto, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    public rejectCompanyClosure(companyOperationsDto: CompanyOperationsDto): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_REJECT_CLOSURE);
        const params = new HttpParams();
        return this.http.post<CompanyOperationsDto>(url, companyOperationsDto, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }


    public getCompanySuspensionRequest(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_VIEW_SUSPENDED_OPERATIONS);
        const params = new HttpParams();
        return this.http.get<SuspendedCompanyDTO>(url, {params}).pipe();
    }

    public closeCompanyOperations(closeCompanyDto: CloseCompanyDto): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_CLOSE_OPERATIONS);
        const params = new HttpParams();
        return this.http.post<CloseCompanyDto>(url, closeCompanyDto, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }


    public getCompanyClosureRequest(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_VIEW_CLOSED_OPERATIONS);
        const params = new HttpParams();
        return this.http.get<ClosedCompanyDTO>(url, {params}).pipe();
    }

    public uploadWindingUpReport(operationClosureId: string, data: FormData): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_UPLOAD_WINDING_UP_REPORT);

        return this.http.post<any>(url, data, {
            headers: {
                'enctype': 'multipart/form-data'
            }, params: {'operationClosureId': operationClosureId}
        }).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }
    public getWindingUpReportDocumentList(closureID: any): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_GET_WINDING_UP_REPORT_LIST);
        const params = new HttpParams().set('closureID', closureID);
        return this.http.get<DocumentDTO[]>(url, {params}).pipe();
    }

    public windingUpReport(closureID: any): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_VIEW_WINDING_UP_REPORT);
        const params = new HttpParams().set('closureID', closureID);
        return this.http.get<any>(url, {params, responseType: 'arraybuffer' as 'json'}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    public getLevyPayments(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_PAYMENT_DETAILS);
        const params = new HttpParams();
        return this.http.get<PaymentDetails>(url, {params}).pipe();
    }

    public getLevyDefaulters(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_DEFAULTER_DETAILS);
        const params = new HttpParams();
        return this.http.get<DefaulterDetails>(url, {params}).pipe();
    }



    public getManufacturesLevyPayments(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_MANUFACTURE_PAYMENT_DETAILS);
        const params = new HttpParams();
        return this.http.get<PaymentDetails>(url, {params}).pipe();
    }

    public getManufacturesLevyPaymentsList(companyId: any): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_MANUFACTURES_PAYMENT_DETAILS);
        const params = new HttpParams().set('companyId', companyId);
        return this.http.get<PaymentDetails>(url, {params}).pipe();
    }

    // public getLevyPaymentsReceipt(id: any): Observable<any> {
    //     const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_MANUFACTURE_PAYMENT_RECEIPT);
    //     const params = new HttpParams().set('id', id);
    //     return this.http.get<PaymentDetails>(url, {params}).pipe();
    // }

    public getLevyPaymentsReceipt(id: any): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_VIEW_E_SLIP);
        const params = new HttpParams()
            .set('id', id);
        // return this.httpService.get<any>(`${this.baseUrl}/get/pdf/${fileName}`, { responseType: 'arraybuffer' as 'json' });
        return this.http.get<any>(url, {params, responseType: 'arraybuffer' as 'json'}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            }),
        );
    }

    public getLevyPenalty(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_PENALTY_DETAIL);
        const params = new HttpParams();
        return this.http.get<PenaltyDetails>(url, {params}).pipe();
    }

    public getManufacturesLevyPenalty(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_MANUFACTURE_PENALTY_DETAILS);
        const params = new HttpParams();
        return this.http.get<PenaltyDetails>(url, {params}).pipe();
    }

    public getManufacturesPayments(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_MANUFACTURE_PAY_DETAILS);
        const params = new HttpParams();
        return this.http.get<PaymentDetails>(url, {params}).pipe();
    }

    public getManufacturesLevyPenaltyList(companyId: any): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_MANUFACTURES_PENALTY_DETAILS);
        const params = new HttpParams().set('companyId', companyId);
        return this.http.get<PenaltyDetails>(url, {params}).pipe();
    }

    public getVerificationStatus(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.USER_EMAIL_VERIFICATION_STATUS);
        const params = new HttpParams();
        return this.http.get<EmailVerificationStatus>(url, {params}).pipe();
    }

    public sendEmailVerificationToken(sendEmailDto: SendEmailDto): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.SEND_EMAIL_VERIFICATION);
        const params = new HttpParams();
        return this.http.post<SendEmailDto>(url, sendEmailDto, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    public confirmEmailAddress(verifyEmailDto: VerifyEmailDto): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.CONFIRM_EMAIL_VERIFICATION_STATUS);
        const params = new HttpParams();
        return this.http.post<VerifyEmailDto>(url, verifyEmailDto, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    public generatePdf(): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_E_SLIP);

        return this.http.get<any>(url).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    public showError(message: string, fn?: Function) {
        swal.fire({
            title: message,
            buttonsStyling: false,
            customClass: {
                confirmButton: 'btn btn-success form-wizard-next-btn ',
            },
            icon: 'error',
        }).then(() => {
            if (fn) {
                fn();
            }
        });
    }







}
