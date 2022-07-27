import {Injectable} from '@angular/core';
import {ApiEndpointService} from "../../../services/endpoints/api-endpoint.service";
import {HttpClient, HttpErrorResponse, HttpParams} from "@angular/common/http";
import {DecisionFeedback, SACSummary} from "./request_std.model";
import {Observable, throwError} from "rxjs";
import {catchError, map} from "rxjs/operators";
import {Preliminary_Draft} from "./commitee-model";

@Injectable({
  providedIn: 'root'
})
export class CommitteeService {

  protocol = `https://`;
  baseUrl = ApiEndpointService.DOMAIN.LOCAL_DEV

  private apiServerUrl = `${this.protocol}${this.baseUrl}/api/v1/migration/committee/`;

  constructor(private http: HttpClient) {
  }

  public getAllNWIS(): any {
    return this.http.get<any>(`${this.apiServerUrl}getAllNwis`)
  }


  //upload  Minutes For PD
  public uploadMinutesForPd(NWIID: string, data: FormData, doctype: string, docName: string): Observable<any> {
    const url = `${this.apiServerUrl}upload/minutes`;

    return this.http.post<any>(url, data, {
      headers: {
        'enctype': 'multipart/form-data'
      }, params: {'nwiId': NWIID, 'type': doctype, 'docName': docName}
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

  //upload Draft Documents For PD
  public uploadDraftDocumentsForPd(NWIID: string, data: FormData, doctype: string, docName: string): Observable<any> {
    const url = `${this.apiServerUrl}upload/draftDocuments`;

    return this.http.post<any>(url, data, {
      headers: {
        'enctype': 'multipart/form-data'
      }, params: {'nwiId': NWIID, 'type': doctype, 'docName': docName}
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
  //upload Preliminary Draft Name
  public preparePreliminaryDraft(preliminaryDraft: Preliminary_Draft, nwiID:string): Observable<any> {
    const params = new HttpParams()
        .set('nwIId', nwiID)
    return this.http.post<Preliminary_Draft>(`${this.apiServerUrl}` + 'uploadPd', preliminaryDraft, {params})
  }


    //upload  PD Document
    public uploadPDDocument(PdID: string, data: FormData, doctype: string, docName: string): Observable<any> {
        const url = `${this.apiServerUrl}upload/pd`;

        return this.http.post<any>(url, data, {
            headers: {
                'enctype': 'multipart/form-data'
            }, params: {'pdID': PdID, 'type': doctype, 'docName': docName}
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

  public viewDocs(editedDraftStandardDocumentId: any, doctype: string): Observable<any> {
    const url = `${this.apiServerUrl}` + 'view';
    const params = new HttpParams()
        .set('draftStandardId', editedDraftStandardDocumentId)
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


  //review Preliminary Draft


}
