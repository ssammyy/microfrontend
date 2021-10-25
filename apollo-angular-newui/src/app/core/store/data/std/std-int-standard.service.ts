import { Injectable } from '@angular/core';
import {
    GazetteNotice,
    ISAdoptionComments, ISAdoptionJustification,
    ISAdoptionProposal, ISDecision, ISHopTASKS, ISHosSicTASKS, ISJustificationDecision,
    ISSacSecTASKS, ISStandard, ISTcSecTASKS, ListJustification, NWAStandard,
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

  public decisionOnProposal(iSDecision: ISDecision): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_DECISION_ON_PROPOSAL);
    const params = new HttpParams();
    return this.http.post<ISAdoptionProposal>(url, iSDecision, {params}).pipe(
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
    public uploadJSFile(isJustificationID: string, data: FormData): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_UPLOAD_JS_DOCUMENT);

        return this.http.post<any>(url, data, {
            headers: {
                'enctype': 'multipart/form-data'
            }, params: {'isJustificationID': isJustificationID}
        }).pipe(
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

  public decisionOnJustification(isJustificationDecision: ISJustificationDecision): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_DECISION_ON_JUSTIFICATION);
    const params = new HttpParams();
    return this.http.post<ISAdoptionJustification>(url, isJustificationDecision, {params}).pipe(
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

  public approveStandard(isJustificationDecision: ISJustificationDecision): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_APPROVE_STANDARD);
    const params = new HttpParams();
    return this.http.post<ISAdoptionJustification>(url, isJustificationDecision, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }
    public getHOPTasks(): Observable<ISHopTASKS[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_HOP_TASKS);
        const params = new HttpParams();
        return this.http.get<ISHopTASKS[]>(url, {params}).pipe();
    }
    public uploadISStandard(isStandard: ISStandard): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IS_UPLOAD_STANDARD);
        const params = new HttpParams();
        return this.http.post<ISStandard>(url, isStandard, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }
    public uploadSDFile(isStandardID: string, data: FormData): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IS_UPLOAD_STD);

        return this.http.post<any>(url, data, {
            headers: {
                'enctype': 'multipart/form-data'
            }, params: {'isStandardID': isStandardID}
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
    public getHoSiCTasks(): Observable<ISHosSicTASKS[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_HOS_SIC_TASKS);
        const params = new HttpParams();
        return this.http.get<ISHosSicTASKS[]>(url, {params}).pipe();
    }
    public uploadGazetteNotice(gazetteNotice: GazetteNotice): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IS_UPLOAD_GAZETTE_NOTICE);
        const params = new HttpParams();
        return this.http.post<GazetteNotice>(url, gazetteNotice, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }
    public updateGazetteDate(gazetteNotice: GazetteNotice): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IS_UPDATE_GAZETTE_DATE);
        const params = new HttpParams();
        return this.http.post<GazetteNotice>(url, gazetteNotice, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }



}
