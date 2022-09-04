import { Injectable } from '@angular/core';
import {
    ComJcJustificationDec,
    GazetteNotice, InternationalStandardsComments,
    ISAdoptionComments, ISAdoptionJustification,
    ISAdoptionProposal, ISDecision, ISDraftUpload, ISHopTASKS, ISHosSicTASKS, ISJustificationDecision,
    ISSacSecTASKS, ISStandard, ISTcSecTASKS, ListJustification, NWAStandard, ProposalComment,
    ProposalComments, StakeholderProposalComments
} from "./std.model";
import {Observable, throwError} from "rxjs";
import {ApiEndpointService} from "../../../services/endpoints/api-endpoint.service";
import {HttpClient, HttpErrorResponse, HttpParams} from "@angular/common/http";
import {catchError, map} from "rxjs/operators";
import {DefaulterDetails, SiteVisitRemarks} from "../levy/levy.model";

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

    public getProposal(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_VIEW_IS_PROPOSAL);
        const params = new HttpParams();
        return this.http.get<ISAdoptionProposal>(url, {params}).pipe();
    }



  public getISProposals(): Observable<ProposalComments[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_VIEW_IS_PROPOSALS);
    const params = new HttpParams();
    return this.http.get<ProposalComments[]>(url, {params}).pipe();
  }
  //view Proposal Doc
    public viewProposalPDF(isDocumentId: any): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_VIEW_IS_PROPOSALS_DOC);
        const params = new HttpParams()
            .set('isDocumentId', isDocumentId);
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

  public submitAPComments(proposalComment: ProposalComment): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_SUBMIT_AP_COMMENTS);
    const params = new HttpParams();
    return this.http.post<ProposalComment>(url, proposalComment, {params}).pipe(
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
    return this.http.post<ProposalComments>(url, iSDecision, {params}).pipe(
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
  //view Justification File
    public viewJustificationPDF(isJSDocumentId: any): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_VIEW_IS_JUSTIFICATION_DOC);
        const params = new HttpParams()
            .set('isJSDocumentId', isJSDocumentId);
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
    public decisionOnSACJustification(isJustificationDecision: ISJustificationDecision): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_SAC_DECISION_ON_JUSTIFICATION);
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

    public decisionOnHopJustification(isJustificationDecision: ISJustificationDecision): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_HOP_DECISION_ON_JUSTIFICATION);
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

    //view Standard File
    public viewStandardPDF(isStdDocumentId: any): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_VIEW_IS_STANDARD_DOC);
        const params = new HttpParams()
            .set('isStdDocumentId', isStdDocumentId);
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

    //view Standard File
    public viewStandardDPDF(isStandardID: any): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_VIEW_IS_STANDARD_GZT_DOC);
        const params = new HttpParams()
            .set('isStandardID', isStandardID);
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

    public uploadSDGZFile(isStandardID: string, data: FormData): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IS_UPLOAD_STD_GZT);

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

    public getUserTasks(): Observable<ProposalComments[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_INT_TASKS);
        const params = new HttpParams();
        return this.http.get<ProposalComments[]>(url, {params}).pipe();
    }

    public getAllComments(proposalId: any): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_PROPOSAL_COMMENTS);
        const params = new HttpParams().set('proposalId', proposalId);
        return this.http.get<StakeholderProposalComments>(url, {params}).pipe();
    }
    public getUserComments(id: any): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_JUSTIFICATION_COMMENTS);
        const params = new HttpParams().set('id', id);
        return this.http.get<InternationalStandardsComments>(url, {params}).pipe();
    }

    public editStandardDraft(iSDraftUpload: ISDraftUpload): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_UPLOAD_DRAFT_STANDARD);
        const params = new HttpParams();
        return this.http.post<ISDraftUpload>(url, iSDraftUpload, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }
    public draughtStandardDraft(iSDraftUpload: ISDraftUpload): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_UPLOAD_DRAFTING_STANDARD);
        const params = new HttpParams();
        return this.http.post<ISDraftUpload>(url, iSDraftUpload, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }
    public proofreading(iSDraftUpload: ISDraftUpload): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_UPLOAD_PROOFREADING_STANDARD);
        const params = new HttpParams();
        return this.http.post<ISDraftUpload>(url, iSDraftUpload, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }
    public uploadIStandard(iSDraftUpload: ISDraftUpload): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_UPLOAD_STANDARD);
        const params = new HttpParams();
        return this.http.post<ISDraftUpload>(url, iSDraftUpload, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }




}
