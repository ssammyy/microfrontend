import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpParams} from "@angular/common/http";
import {Observable, throwError} from "rxjs";
import {ApiEndpointService} from "../../../services/endpoints/api-endpoint.service";
import {catchError, map} from "rxjs/operators";
import {
    ApproveDraft,
    CommentOnProposalStakeHolder, COMPreliminaryDraft, DecisionOnStdDraft,
    DiSdtDECISION,
    DISDTTasks, FileData, HOPTasks,
    HoSicTasks,
    KNWCommittee, KNWDepartment,
    KnwSecTasks, NwaDecisionOnJustification, NWADiSdtJustification, NwaEditPd,
    NWAJustification, NWAJustificationDecision, NWAPDDecision, NWAPreliminaryDraft, NwaRequestList,
    NWAStandard, NwaTasks, NWAWDDecision, NWAWorkShopDraft, PreliminaryDraftTasks, SacSecTasks, SPCSECTasks,
    UpdateNwaGazette, UploadNwaGazette, UsersEntity
} from "./std.model";

@Injectable({
  providedIn: 'root'
})
export class StdNwaService {
  constructor(private http: HttpClient) { }

 // public getPublicUrl(): any{
 //     const url = ApiEndpointService.getEndpoint();
 // }

  public getKNWDepartments(): any {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_DEPARTMENTS);
    const params = new HttpParams();
    return this.http.get<KNWDepartment>(url, {params}).pipe();
  }

  public getKNWCommittee(): any {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_KNW_COMMITTEE);
    const params = new HttpParams();
    return this.http.get<KNWCommittee>(url, {params}).pipe();
  }

  public knwtasks(): Observable<KnwSecTasks[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_KNW_TASKS);
    const params = new HttpParams();
    return this.http.get<KnwSecTasks[]>(url, {params}).pipe();
  }

    public getNwaTasks(): Observable<NwaTasks[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_TASKS);
        const params = new HttpParams();
        return this.http.get<NwaTasks[]>(url, {params}).pipe();
    }

    public getWorkshopStandards(): Observable<NwaRequestList[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_VIEW_STANDARD_REQUEST);
        const params = new HttpParams();
        return this.http.get<NwaRequestList[]>(url, {params}).pipe();
    }

    public getWorkshopJustification(): Observable<NwaRequestList[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_VIEW_JUSTIFICATION);
        const params = new HttpParams();
        return this.http.get<NwaRequestList[]>(url, {params}).pipe();
    }

  public prepareJustification(nwaJustification: NWAJustification): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_PREPARE_JUSTIFICATION);
    const params = new HttpParams();
    return this.http.post<NWAJustification>(url, nwaJustification, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }

    public getJustification(requestId: any): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_GET_JUSTIFICATION);
        const params = new HttpParams().set('requestId', requestId);
        return this.http.get<NWAJustification>(url, {params}).pipe();
    }
    public getWorkshopForEditing(): Observable<NwaRequestList[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_GET_EDIT_PD);
        const params = new HttpParams();
        return this.http.get<NwaRequestList[]>(url, {params}).pipe();
    }

    public getWorkShopStdDraft(): Observable<COMPreliminaryDraft[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_GET_PD_FOR_ACTION);
        const params = new HttpParams();
        return this.http.get<COMPreliminaryDraft[]>(url, {params}).pipe();
    }
    public decisionOnDraft(approveDraft: DecisionOnStdDraft): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_DECISION_ON_STD_DR);
        const params = new HttpParams();
        return this.http.post<DecisionOnStdDraft>(url, approveDraft, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    public getWorkShopDraftForEditing(): Observable<NwaEditPd[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_GET_PD_FOR_EDITING);
        const params = new HttpParams();
        return this.http.get<NwaEditPd[]>(url, {params}).pipe();
    }

  //upload justification Document
    public uploadFileDetails(nwaJustificationID: string, data: FormData): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_UPLOAD_DATA);

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
  //   public prepareJustification(permitID: string, data: FormData): Observable<any> {
  //       const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_PREPARE_JUSTIFICATION);
  //       // const params = new HttpParams()
  //       //     .set('permitID', permitID);
  //       return this.http.post<any>(url, data, {
  //           headers: {
  //               'enctype': 'multipart/form-data'
  //           }, params: {'permitID': permitID}
  //       }).pipe(
  //           map(function (response: any) {
  //               return response;
  //           }),
  //           catchError((fault: HttpErrorResponse) => {
  //               // console.warn(`getAllFault( ${fault.message} )`);
  //               return throwError(fault);
  //           })
  //       );
  //   }
    public getWorkshopForPDraft(): Observable<NwaRequestList[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_PREPARE_PD);
        const params = new HttpParams();
        return this.http.get<NwaRequestList[]>(url, {params}).pipe();
    }


    public loadFileDetailsPDF(nwaDocumentId: any): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_UPLOAD_DATA_VIEW);
        const params = new HttpParams()
            .set('nwaDocumentId', nwaDocumentId);
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

  public getSPCSECTasks(): Observable<SPCSECTasks[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_SPC_SEC_TASKS);
    const params = new HttpParams();
    return this.http.get<SPCSECTasks[]>(url, {params}).pipe();
  }
    public viewJustificationPDF(nwaDocumentId: any): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_UPLOAD_DATA_VIEW);
        const params = new HttpParams()
            .set('nwaDocumentId', nwaDocumentId);
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
  public decisionOnJustificationKNW(nwaJustificationDecision: NWAJustificationDecision): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_DECISION_ON_JUSTIFICATION_KNW);
    const params = new HttpParams();
    return this.http.post<NWAJustification>(url, nwaJustificationDecision, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }


    public decisionOnJustification(nwaJustificationDecision: NwaDecisionOnJustification): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_DECISION_ON_JUSTIFICATION);
        const params = new HttpParams();
        return this.http.post<NwaDecisionOnJustification>(url, nwaJustificationDecision, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

  public prepareDisDtJustification(nwaDiSdtJustification: NWADiSdtJustification): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_PREPARE_DISDT_JUSTIFICATION);
    const params = new HttpParams();
    return this.http.post<NWADiSdtJustification>(url, nwaDiSdtJustification, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }
  public getDiSdtTasks(): Observable<DISDTTasks[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_DISDT_TASKS);
    const params = new HttpParams();
    return this.http.get<DISDTTasks[]>(url, {params}).pipe();
  }
    public viewDIJustificationPDF(diDocumentId: any): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_UPLOAD_DATA_VIEW_DI);
        const params = new HttpParams()
            .set('diDocumentId', diDocumentId);
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
  public decisionOnDiSdtJustification(diSdtDecision: DiSdtDECISION): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_DECISION_ON_DISDT_JUSTIFICATION);
    const params = new HttpParams();
    return this.http.post<NWADiSdtJustification>(url, diSdtDecision, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }
    public getTCSeCTasks(): Observable<PreliminaryDraftTasks[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_TC_SEC_TASKS);
        const params = new HttpParams();
        return this.http.get<PreliminaryDraftTasks[]>(url, {params}).pipe();
    }

  public preparePreliminaryDraft(nwaPreliminaryDraft: NWAPreliminaryDraft): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_PREPARE_PRELIMINARY_DRAFT);
    const params = new HttpParams();
    return this.http.post<NWAPreliminaryDraft>(url, nwaPreliminaryDraft, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }
    public editPreliminaryDraft(nwaPreliminaryDraft: NWAPreliminaryDraft): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_EDIT_PRELIMINARY_DRAFT);
        const params = new HttpParams();
        return this.http.post<NWAPreliminaryDraft>(url, nwaPreliminaryDraft, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }
    public viewPreliminaryDraftPDF(nwaPDDocumentId: any): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_UPLOAD_DATA_VIEW_PD);
        const params = new HttpParams()
            .set('nwaPDDocumentId', nwaPDDocumentId);
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
  public decisionOnPD(nwaPDDecision: NWAPDDecision): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_DECISION_ON_PRELIMINARY_DRAFT);
    const params = new HttpParams();
    return this.http.post<NWAPreliminaryDraft>(url, nwaPDDecision, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }

  public getHOPTasks(): Observable<HOPTasks[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_HOP_TASKS);
    const params = new HttpParams();
    return this.http.get<HOPTasks[]>(url, {params}).pipe();
  }

  public editWorkshopDraft(nwaWorkShopDraft: NWAWorkShopDraft): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_EDIT_WORKSHOP_DRAFT);
    const params = new HttpParams();
    return this.http.post<NWAWorkShopDraft>(url, nwaWorkShopDraft, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }
    public viewWorkshopDraftPDF(nwaWDDocumentId: any): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_UPLOAD_DATA_VIEW_WD);
        const params = new HttpParams()
            .set('nwaWDDocumentId', nwaWDDocumentId);
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
  public getSacSecTasks(): Observable<SacSecTasks[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_SAC_SEC_TASKS);
    const params = new HttpParams();
    return this.http.get<SacSecTasks[]>(url, {params}).pipe();
  }

  public decisionOnWd(nwaWDDecision: NWAWDDecision): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_DECISION_ON_WORKSHOP_DRAFT);
    const params = new HttpParams();
    return this.http.post<NWAWorkShopDraft>(url, nwaWDDecision, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }
  public uploadNwaStandard(nWAStandard: NWAStandard): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_UPLOAD_STANDARD);
    const params = new HttpParams();
    return this.http.post<NWAStandard>(url, nWAStandard, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }
  public getHoSicTasks(): Observable<HoSicTasks[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_HO_SIC_TASKS);
    const params = new HttpParams();
    return this.http.get<HoSicTasks[]>(url, {params}).pipe();
  }

    public viewStandardPDF(nwaSDocumentId: any): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_UPLOAD_DATA_VIEW_STD);
        const params = new HttpParams()
            .set('nwaSDocumentId', nwaSDocumentId);
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
  public uploadGazetteNotice(uploadNwaGazette: UploadNwaGazette): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_UPLOAD_GAZETTE_NOTICE);
    const params = new HttpParams();
    return this.http.post<UploadNwaGazette>(url, uploadNwaGazette, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }
  public updateGazettementDate(updateNwaGazette: UpdateNwaGazette): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_UPDATE_GAZETTEMENT_DATE);
    const params = new HttpParams();
    return this.http.post<UpdateNwaGazette>(url, updateNwaGazette, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }


    //Upload DI SDT Justification
    public uploadDIFileDetails(nwaDiSdtJustificationID: string, data: FormData): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_UPLOAD_DATA_DI);

        return this.http.post<any>(url, data, {
            headers: {
                'enctype': 'multipart/form-data'
            }, params: {'nwaDiSdtJustificationID': nwaDiSdtJustificationID}
        }).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    //Upload Preliminary Draft
    public uploadPDFileDetails(nwaPDid: string, data: FormData): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_UPLOAD_DATA_PD);

        return this.http.post<any>(url, data, {
            headers: {
                'enctype': 'multipart/form-data'
            }, params: {'nwaPDid': nwaPDid}
        }).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }
    public uploadWDFileDetails(nwaWDid: string, data: FormData): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_UPLOAD_DATA_WD);

        return this.http.post<any>(url, data, {
            headers: {
                'enctype': 'multipart/form-data'
            }, params: {'nwaWDid': nwaWDid}
        }).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }
    public uploadSTDFileDetails(nwaSTDid: string, data: FormData): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_UPLOAD_DATA_STD);

        return this.http.post<any>(url, data, {
            headers: {
                'enctype': 'multipart/form-data'
            }, params: {'nwaSTDid': nwaSTDid}
        }).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }
    public getKnwSecretary(): Observable<UsersEntity[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_KNW_SECRETARY);
        const params = new HttpParams();
        return this.http.get<UsersEntity[]>(url, {params}).pipe();
    }

    public getDirector(): Observable<UsersEntity[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_DI_DIRECTOR);
        const params = new HttpParams();
        return this.http.get<UsersEntity[]>(url, {params}).pipe();
    }

    public getHeadOfPublishing(): Observable<UsersEntity[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_HEAD_OF_PUBLISHING);
        const params = new HttpParams();
        return this.http.get<UsersEntity[]>(url, {params}).pipe();
    }

    public getSacSecretary(): Observable<UsersEntity[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_SAC_SECRETARY);
        const params = new HttpParams();
        return this.http.get<UsersEntity[]>(url, {params}).pipe();
    }

    public getHeadOfSic(): Observable<UsersEntity[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_HEAD_OF_SIC);
        const params = new HttpParams();
        return this.http.get<UsersEntity[]>(url, {params}).pipe();
    }

    public getSpcSecretary(): Observable<UsersEntity[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_SPC_SECRETARY);
        const params = new HttpParams();
        return this.http.get<UsersEntity[]>(url, {params}).pipe();
    }



}
