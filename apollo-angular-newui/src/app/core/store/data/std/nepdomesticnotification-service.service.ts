import { Injectable } from '@angular/core';
import {Observable, throwError} from "rxjs";
import {
  DraftNotification,
  finalSubmit,
  InboundNotification,
  InfoAvailableNo,
  InfoAvailableYes,
  NepNotification,
  RootObject
} from "./std.model";
import {ApiEndpointService} from "../../../services/endpoints/api-endpoint.service";
import {HttpClient, HttpErrorResponse, HttpEvent, HttpParams, HttpRequest} from "@angular/common/http";
import {catchError, map} from "rxjs/operators";
import {dev} from "../../../../../../../apollo-webs/src/app/shared/dev/dev";

@Injectable({
  providedIn: 'root'
})
export class NepdomesticnotificationServiceService {

  private baseUrl = `${dev.connect}`;

  constructor(private http: HttpClient) { }

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

  public reviewTasks(infoAvailableYes: InfoAvailableYes): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NEP_REJECT_REQUEST_MADE);
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

    const req = new HttpRequest('POST', `${this.baseUrl}migration/upload`, formData, {
      reportProgress: true,
      responseType: 'json'
    });

    return this.http.request(req);
  }

  getFiles(): Observable<any> {
    return this.http.get(`${this.baseUrl}migration/files`);
  }

}
