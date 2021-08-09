import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpParams} from "@angular/common/http";
import {Observable, throwError} from "rxjs";
import {
  ReviewComments,
  ReviewedStandards,
  ReviewForm,
  StandardReviewComments, StandardReviewRecommendations, SystemicAnalyseComments
} from "./std.model";
import {ApiEndpointService} from "../../../services/endpoints/api-endpoint.service";
import {catchError, map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class StdReviewService {

  constructor(private http: HttpClient) { }


  public reviewedStandards(): Observable<ReviewedStandards[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.SR_GET_REVIEWED_STANDARDS);
    const params = new HttpParams();
    return this.http.get<ReviewedStandards[]>(url, {params}).pipe();
  }


  public reviewStandard(reviewForm: ReviewForm): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.SR_REVIEW_FORM);
    const params = new HttpParams();
    return this.http.post<ReviewForm>(url, reviewForm, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }
  public getReviewForms(): Observable<ReviewComments[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.SR_GET_REVIEW_FORM);
    const params = new HttpParams();
    return this.http.get<ReviewComments[]>(url, {params}).pipe();
  }


  public commentsOnReview(standardReviewComments: StandardReviewComments): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.SR_REVIEW_COMMENTS);
    const params = new HttpParams();
    return this.http.post<StandardReviewComments>(url, standardReviewComments, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }
  public getReviewTasks(): Observable<SystemicAnalyseComments[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.SR_GET_REVIEW_TASKS);
    const params = new HttpParams();
    return this.http.get<SystemicAnalyseComments[]>(url, {params}).pipe();
  }

  public decisionOnRecommendation(standardReviewRecommendations: StandardReviewRecommendations): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.SR_DECISION_ON_RECOMMENDATION);
    const params = new HttpParams();
    return this.http.post<StandardReviewRecommendations>(url, standardReviewRecommendations, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }

  public uploadFileDetails(nwaJustificationID: string, data: FormData): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.SR_UPLOAD_DOCUMENT);

    return this.http.post<any>(url, data, {
      headers: {
        'enctype': 'multipart/form-data'
      }, params: {'nwaJustificationID': nwaJustificationID}
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

}
