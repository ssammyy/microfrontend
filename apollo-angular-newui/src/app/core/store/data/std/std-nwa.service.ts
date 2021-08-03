import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpEvent, HttpParams, HttpRequest} from "@angular/common/http";
import {Observable, throwError} from "rxjs";
import {ApiEndpointService} from "../../../services/endpoints/api-endpoint.service";
import {catchError, map} from "rxjs/operators";
import {
  DISDTTasks, HOPTasks,
  HoSicTasks,
  KNWCommittee, KNWDepartment,
  KnwSecTasks, NWADiSdtJustification,
  NWAJustification, NWAPreliminaryDraft,
  NWAStandard, NWAWorkShopDraft, SacSecTasks, SPCSECTasks,
  UpdateNwaGazette, UploadNwaGazette
} from "./std.model";
import {STA10ProductsManufactureDto} from "../qa/qa.model";

@Injectable({
  providedIn: 'root'
})
export class StdNwaService {

  constructor(private http: HttpClient) { }

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

  public getSPCSECTasks(): Observable<SPCSECTasks[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_SPC_SEC_TASKS);
    const params = new HttpParams();
    return this.http.get<SPCSECTasks[]>(url, {params}).pipe();
  }
  public decisionOnJustification(nwaJustification: NWAJustification): Observable<any> {
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
  public decisionOnDiSdtJustification(nwaDiSdtJustification: NWADiSdtJustification): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_DECISION_ON_DISDT_JUSTIFICATION);
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
  public decisionOnPD(nwaPreliminaryDraft: NWAPreliminaryDraft): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_DECISION_ON_PRELIMINARY_DRAFT);
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
  public getSacSecTasks(): Observable<SacSecTasks[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_SAC_SEC_TASKS);
    const params = new HttpParams();
    return this.http.get<SacSecTasks[]>(url, {params}).pipe();
  }

  public decisionOnWd(nwaWorkShopDraft: NWAWorkShopDraft): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_DECISION_ON_WORKSHOP_DRAFT);
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

    public uploadFileDetails(nwaJustificationID: string, data: FormData): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_UPLOAD_DATA);
        // const params = new HttpParams()
        //     .set('permitID', permitID);
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


  // upload(file: File): Observable<HttpEvent<any>> {
  //   const formData: FormData = new FormData();
  //
  //   formData.append('file', file);
  //
  //   const req = new HttpRequest('POST', `${this.apiServerUrl}`+`upload`, formData, {
  //     reportProgress: true,
  //     responseType: 'json'
  //   });
  //
  //   return this.http.request(req);
  // }
  //
  // getFiles(): Observable<any> {
  //   return this.http.get(`${this.apiServerUrl}`+`files`);
  // }

}
