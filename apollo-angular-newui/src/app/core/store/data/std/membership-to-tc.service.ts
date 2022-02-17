import {Injectable} from '@angular/core';
import {ApiEndpointService} from "../../../services/endpoints/api-endpoint.service";
import {HttpClient, HttpErrorResponse, HttpParams} from "@angular/common/http";
import {
  CallForApplication,
  HOFRecommendationTask,
  ReviewApplicationTask,
  SubmitApplication,
  TCMemberDetails
} from "./request_std.model";
import {Observable, throwError} from "rxjs";
import {catchError, map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class MembershipToTcService {
  protocol = `https://`;
  baseUrl = ApiEndpointService.DOMAIN.LOCAL_DEV
  private apiMembershipToTCUrl = `${this.protocol}${this.baseUrl}/api/v1/migration/membershipToTC/`;


  constructor(private http: HttpClient) {
  }

  public uploadCallForApplications(callForApplication: CallForApplication): Observable<any> {
    console.log(callForApplication);
    return this.http.post<CallForApplication>(`${this.apiMembershipToTCUrl}` + 'submitCallForApplication', callForApplication)
  }

  public getApplicantTasks(): Observable<CallForApplication[]> {
    return this.http.get<CallForApplication[]>(`${this.apiMembershipToTCUrl}` + 'anonymous/getCallForApplications')
  }

  public onSubmitApplication(submitApplication: SubmitApplication): Observable<any> {
    console.log(submitApplication);
    return this.http.post<SubmitApplication>(`${this.apiMembershipToTCUrl}` + 'anonymous/submitTCMemberApplication', submitApplication)
  }

  public getApplicationsForReview(): Observable<ReviewApplicationTask[]> {
    return this.http.get<ReviewApplicationTask[]>(`${this.apiMembershipToTCUrl}` + 'getApplicationsForReview')
  }

  public decisionOnApplications(reviewApplicationTask: ReviewApplicationTask, tCApplicationId: number): Observable<any> {
    const params = new HttpParams()
        .set('tCApplicationId', String(tCApplicationId))
    return this.http.post<ReviewApplicationTask>(`${this.apiMembershipToTCUrl}` + 'decisionOnApplicantRecommendation',
        reviewApplicationTask, {params, responseType: 'arraybuffer' as 'json'})
        .pipe(
            map(function (response: any) {
              return response;
            }),
            catchError((fault: HttpErrorResponse) => {
              // console.warn(`getAllFault( ${fault.message} )`);
              return throwError(fault);
            })
        );
  }

  public getHOFRecommendation(): Observable<ReviewApplicationTask[]> {
    return this.http.get<ReviewApplicationTask[]>(`${this.apiMembershipToTCUrl}` + 'getRecommendationsFromHOF')
  }

  public completeReview(reviewApplicationTask: ReviewApplicationTask, tCApplicationId: number): Observable<any> {
    const params = new HttpParams()
        .set('tCApplicationId', String(tCApplicationId))
    return this.http.post<ReviewApplicationTask>(`${this.apiMembershipToTCUrl}` + 'completeSPCReview',
        reviewApplicationTask, {params, responseType: 'arraybuffer' as 'json'})
        .pipe(
            map(function (response: any) {
              return response;
            }),
            catchError((fault: HttpErrorResponse) => {
              // console.warn(`getAllFault( ${fault.message} )`);
              return throwError(fault);
            })
        );
  }

  public getRecommendationsFromSPC(): Observable<ReviewApplicationTask[]> {
    return this.http.get<ReviewApplicationTask[]>(`${this.apiMembershipToTCUrl}` + 'getRecommendationsFromSPC')
  }

  public decisionOnSPCRecommendation(reviewApplicationTask: ReviewApplicationTask, tCApplicationId: number, decision: string): Observable<any> {
    const params = new HttpParams()
        .set('tCApplicationId', String(tCApplicationId))
        .set('decision', String(decision))

    return this.http.post<ReviewApplicationTask>(`${this.apiMembershipToTCUrl}` + 'decisionOnSPCRecommendation',
        reviewApplicationTask, {params, responseType: 'arraybuffer' as 'json'})
        .pipe(
            map(function (response: any) {
              return response;
            }),
            catchError((fault: HttpErrorResponse) => {
              // console.warn(`getAllFault( ${fault.message} )`);
              return throwError(fault);
            })
        );
  }

  public getTCMemberCreationTasks(): Observable<HOFRecommendationTask[]> {
    return this.http.get<HOFRecommendationTask[]>(`${this.apiMembershipToTCUrl}` + 'getTCMemberCreationTasks')
  }


  public saveTCMember(tCMemberDetails: TCMemberDetails): Observable<any> {
    console.log(tCMemberDetails);
    return this.http.post<TCMemberDetails>(`${this.apiMembershipToTCUrl}` + 'saveTCMember', tCMemberDetails)
  }

  //upload justification Document
  public uploadFileDetails(draftStandardID: string, data: FormData, doctype: string, nomineeName: string): Observable<any> {
    const url = `${this.apiMembershipToTCUrl}anonymous/file-upload`;

    return this.http.post<any>(url, data, {
      headers: {
        'enctype': 'multipart/form-data'
      }, params: {'callForTCApplicationId': draftStandardID, 'type': doctype, 'nomineeName': nomineeName}
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

  public viewDEditedApplicationPDF(applicationId: any, doctype: string): Observable<any> {
    const url = `${this.apiMembershipToTCUrl}` + 'view/CurriculumVitae';
    const params = new HttpParams()
        .set('draftStandardId', applicationId)
        .set('doctype', doctype);

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
}
