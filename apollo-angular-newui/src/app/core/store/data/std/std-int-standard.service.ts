import { Injectable } from '@angular/core';
import {
  ISAdoptionComments, ISAdoptionJustification,
  ISAdoptionProposal,
  ISSacSecTASKS, ISTcSecTASKS, ListJustification,
  ProposalComments
} from "./std.model";
import {Observable, throwError} from "rxjs";
import {ApiEndpointService} from "../../../services/endpoints/api-endpoint.service";
import {HttpClient, HttpErrorResponse, HttpParams} from "@angular/common/http";
import {catchError, map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class StdIntStandardService {

  constructor(private http: HttpClient) { }

  public prepareAdoptionProposal(iSAdoptionProposal: ISAdoptionProposal): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_PREPARE_ADOPTION_PROPOSAL);
    const params = new HttpParams();
    return this.http.post<ISAdoptionProposal>(url, iSAdoptionProposal, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }
    public uploadFileDetails(isProposalID: string, data: FormData): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_UPLOAD_DOCUMENT);

        return this.http.post<any>(url, data, {
            headers: {
                'enctype': 'multipart/form-data'
            }, params: {'isProposalID': isProposalID}
        }).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

  public getISProposals(): Observable<ProposalComments[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_VIEW_IS_PROPOSALS);
    const params = new HttpParams();
    return this.http.get<ProposalComments[]>(url, {params}).pipe();
  }

  public submitAPComments(iSAdoptionComments: ISAdoptionComments): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_SUBMIT_AP_COMMENTS);
    const params = new HttpParams();
    return this.http.post<ISAdoptionComments>(url, iSAdoptionComments, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }


  public getTCSECTasks(): Observable<ISTcSecTASKS[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_TC_SEC_TASKS);
    const params = new HttpParams();
    return this.http.get<ISTcSecTASKS[]>(url, {params}).pipe();
  }

  public decisionOnProposal(iSAdoptionProposal: ISAdoptionProposal): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_DECISION_ON_PROPOSAL);
    const params = new HttpParams();
    return this.http.post<ISAdoptionProposal>(url, iSAdoptionProposal, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }
  public prepareJustification(iSAdoptionJustification: ISAdoptionJustification): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_PREPARE_JUSTIFICATION);
    const params = new HttpParams();
    return this.http.post<ISAdoptionJustification>(url, iSAdoptionJustification, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }
  public getSPCSECTasks(): Observable<ListJustification[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_SPC_SEC_TASKS);
    const params = new HttpParams();
    return this.http.get<ListJustification[]>(url, {params}).pipe();
  }

  public decisionOnJustification(iSAdoptionJustification: ISAdoptionJustification): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_DECISION_ON_JUSTIFICATION);
    const params = new HttpParams();
    return this.http.post<ISAdoptionJustification>(url, iSAdoptionJustification, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }
  public getSACSECTasks(): Observable<ISSacSecTASKS[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_SAC_SEC_TASKS);
    const params = new HttpParams();
    return this.http.get<ISSacSecTASKS[]>(url, {params}).pipe();
  }

  public approveStandard(iSAdoptionJustification: ISAdoptionJustification): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_APPROVE_STANDARD);
    const params = new HttpParams();
    return this.http.post<ISAdoptionJustification>(url, iSAdoptionJustification, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }



}
