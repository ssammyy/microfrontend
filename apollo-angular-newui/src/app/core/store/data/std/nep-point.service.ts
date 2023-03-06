import { Injectable } from '@angular/core';

import {ApiEndpointService} from "../../../services/endpoints/api-endpoint.service";
import {HttpClient, HttpErrorResponse, HttpEvent, HttpParams, HttpRequest} from "@angular/common/http";
import {Observable, throwError} from "rxjs";
import {catchError, map} from "rxjs/operators";
import {
    DecisionOnNotification, DecisionOnPreparedNotification,
    DecisionOnStdDraft,
    DepartmentResponse,
    DraftNotification,
    FeedbackEmail,
    finalSubmit,
    HOPTasks,
    InboundNotification,
    InfoAvailableNo,
    InfoAvailableYes,
    ISAdoptionProposal, NepDraftView,
    NepEnquiries,
    NepInfoCheckDto,
    NepInfoDto,
    NepNotification, NepNotificationForm,
    NepPrepareDraft,
    NepRequests,
    Notifications,
    NWAWorkShopDraft,
    RootObject
} from "./std.model";
import {dev} from "../../../../shared/dev/dev";

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

    public uploadAttachment(enquiryId: string, data: FormData): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NEP_UPLOAD_ATTACHMENT);

        return this.http.post<any>(url, data, {
            headers: {
                'enctype': 'multipart/form-data'
            }, params: {'enquiryId': enquiryId}
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

    public viewUpload(enquiryId: any): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NEP_VIEW_ATTACHMENT);
        const params = new HttpParams().set('enquiryId', enquiryId);
        return this.http.get<any>(url, {params, responseType: 'arraybuffer' as 'json'}).pipe(
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
    public getNepRequests(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NEP_FETCH_ENQ);
        const params = new HttpParams();
        return this.http.get<NepEnquiries>(url, {params}).pipe();
    }

    public getNepDivisionRequests(enquiryId: any): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NEP_FETCH_REQUESTS);
        const params = new HttpParams().set('enquiryId', enquiryId);
        return this.http.get<NepRequests>(url, {params}).pipe();
    }

    public getNepDivisionResponse(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NEP_FETCH_RESPONSE);
        const params = new HttpParams();
        return this.http.get<NepRequests>(url, {params}).pipe();
    }


    public submitInfoResponse(nepInfoCheckDto: NepInfoCheckDto): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NEP_SEND_RESPONSE);
        const params = new HttpParams();
        return this.http.post<NepInfoCheckDto>(url, nepInfoCheckDto, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    public responseOnEnquiryInfo(nepInfoCheckDto: NepInfoDto): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NEP_DIV_SEND_RESPONSE);
        const params = new HttpParams();
        return this.http.post<NepInfoDto>(url, nepInfoCheckDto, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    public sendFeedBack(nepInfoCheckDto: NepInfoDto): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NEP_SEND_FEED_BACK);
        const params = new HttpParams();
        return this.http.post<NepInfoDto>(url, nepInfoCheckDto, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    public notificationOfReview(nepPrepareDraft: NepPrepareDraft): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NEP_SEND_DRAFT);
        const params = new HttpParams();
        return this.http.post<NepPrepareDraft>(url, nepPrepareDraft, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    public uploadDraft(draftId: string, data: FormData): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NEP_UPLOAD_DRAFT_ATTACHMENT);

        return this.http.post<any>(url, data, {
            headers: {
                'enctype': 'multipart/form-data'
            }, params: {'draftId': draftId}
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

    public getDraftNotification(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NEP_GET_DRAFT_NOTIFICATIONS);
        const params = new HttpParams();
        return this.http.get<NepNotificationForm>(url, {params}).pipe();
    }

    public viewDraftUpload(draftId: any): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NEP_VIEW_DRAFT_UPLOADS);
        const params = new HttpParams()
            .set('draftId', draftId);;
        return this.http.get<any>(url, {params, responseType: 'arraybuffer' as 'json'}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }
    public decisionOnReviewDraft(dec: DecisionOnPreparedNotification): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NEP_DECISION_ON_NOTIFICATION);
        const params = new HttpParams();
        return this.http.post<DecisionOnPreparedNotification>(url, dec, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    public getNotificationForApproval(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NEP_MGR_GET_NOTIFICATION);
        const params = new HttpParams();
        return this.http.get<NepDraftView>(url, {params}).pipe();
    }
    public getUploadedNotification(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NEP_UPLOADED_NOTIFICATION);
        const params = new HttpParams();
        return this.http.get<NepDraftView>(url, {params}).pipe();
    }

    public decisionOnNotification(dec: DecisionOnNotification): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NEP_MGR_DECISION_ON_NOTIFICATION);
        const params = new HttpParams();
        return this.http.post<DecisionOnNotification>(url, dec, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    public getDraftNotificationForUpload(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NEP_GET_UPLOAD_NOTIFICATION);
        const params = new HttpParams();
        return this.http.get<NepDraftView>(url, {params}).pipe();
    }

    public uploadNotification(dec: DecisionOnPreparedNotification): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NEP_UPLOAD_NOTIFICATION);
        const params = new HttpParams();
        return this.http.post<DecisionOnPreparedNotification>(url, dec, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }


}
