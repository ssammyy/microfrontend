import { Injectable } from '@angular/core';

import {ApiEndpointService} from "../../../services/endpoints/api-endpoint.service";
import {HttpClient, HttpErrorResponse, HttpEvent, HttpParams, HttpRequest} from "@angular/common/http";
import {Observable, throwError} from "rxjs";
import {catchError, map} from "rxjs/operators";
import {
    DepartmentResponse, DraftNotification,
    FeedbackEmail, finalSubmit,
    HOPTasks, InboundNotification, InfoAvailableNo,
    InfoAvailableYes, NepNotification,
    Notifications,
    NWAWorkShopDraft,
    RootObject
} from "./std.model";
import {dev} from "../../../../../../../apollo-webs/src/app/shared/dev/dev";

@Injectable({
  providedIn: 'root'
})
export class NepPointService {

    private baseUrl = `${dev.connect}`;

  constructor(private http: HttpClient) { }

  //*********************************************National Enquiry Point******************************************
  public sendGetRequest(): Observable<Notifications[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NEP_ENQUIRY_NEP_TASKS);
    const params = new HttpParams();
    return this.http.get<Notifications[]>(url, {params}).pipe();
  }

  public sendGetRequestDivision(): Observable<Notifications[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NEP_ENQUIRY_DIVISION_TASKS);
    const params = new HttpParams();
    return this.http.get<Notifications[]>(url, {params}).pipe();
  }

  public makeEnquiry(rootObject: RootObject): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NEP_MAKE_ENQUIRY);
    const params = new HttpParams();
    return this.http.post<RootObject>(url, rootObject, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }

  public infoAvailableYes(infoAvailableYes: InfoAvailableYes): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NEP_INFORMATION_AVAILABLE_YES);
    const params = new HttpParams();
    return this.http.post<RootObject>(url, infoAvailableYes, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }

  public reviewTasks(infoAvailableYes: InfoAvailableYes): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NEP_INFORMATION_AVAILABLE_YES);
    const params = new HttpParams();
    return this.http.post<RootObject>(url, infoAvailableYes, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }

  public deptResponse(departmentResponse: DepartmentResponse): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NEP_DEPARTMENT_RESPONSE);
    const params = new HttpParams();
    return this.http.post<RootObject>(url, departmentResponse, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }

  public feedbackEmail(feedbackEmail: FeedbackEmail): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NEP_FEEDBACK_EMAIL);
    const params = new HttpParams();
    return this.http.post<RootObject>(url, feedbackEmail, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }

//*********************************************National Enquiry Point******************************************

    public getNotifications(): Observable<NepNotification[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NEP_RETRIEVE_NOTIFICATIONS);
        const params = new HttpParams();
        return this.http.get<NepNotification[]>(url, {params}).pipe();
    }

    public getManagerNotifications(): Observable<InboundNotification[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NEP_MANAGER_RETRIEVE_NOTIFICATIONS);
        const params = new HttpParams();
        return this.http.get<InboundNotification[]>(url, {params}).pipe();
    }

    public acceptRequest(infoAvailableYes: InfoAvailableYes): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NEP_ACCEPT_REQUEST_MADE);
        const params = new HttpParams();
        return this.http.post<RootObject>(url, infoAvailableYes, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    public submitNo(infoAvailableNo: InfoAvailableNo): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NEP_REJECT_REQUEST_MADE);
        const params = new HttpParams();
        return this.http.post<RootObject>(url, infoAvailableNo, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    public submitFinal(infoAvailableNo: finalSubmit): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NEP_FINAL_SUBMISSION);
        const params = new HttpParams();
        return this.http.post<RootObject>(url, infoAvailableNo, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    public managerAccept(infoAvailableNo: InfoAvailableNo): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NEP_MANAGER_ACCEPT);
        const params = new HttpParams();
        return this.http.post<RootObject>(url, infoAvailableNo, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    public uploadFile(draftNotification: DraftNotification): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NEP_UPLOAD_FILE);
        const params = new HttpParams();
        return this.http.post<RootObject>(url, draftNotification, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    upload(file: File): Observable<HttpEvent<any>> {
        const formData: FormData = new FormData();

        formData.append('file', file);

        const req = new HttpRequest('POST', `${this.baseUrl}sd/upload`, formData, {
            reportProgress: true,
            responseType: 'json'
        });

        return this.http.request(req);
    }

    getFiles(): Observable<any> {
        return this.http.get(`${this.baseUrl}sd/files`);
    }

}
