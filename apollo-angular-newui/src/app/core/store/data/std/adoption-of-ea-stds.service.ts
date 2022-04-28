import {Injectable} from '@angular/core';
import {ApiEndpointService} from "../../../services/endpoints/api-endpoint.service";
import {HttpClient, HttpErrorResponse, HttpParams} from "@angular/common/http";
import {DecisionFeedback, SACSummary, SACSummaryTask} from "./request_std.model";
import {Observable, throwError} from "rxjs";
import {catchError, map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class AdoptionOfEaStdsService {
  protocol = `https://`;
  baseUrl = ApiEndpointService.DOMAIN.LOCAL_DEV
  private apiAdoptionToEAStandardUrl = `${this.protocol}${this.baseUrl}/api/v1/migration/adoptionToEAStandard/`;

  constructor(private http: HttpClient) {
  }

  public uploadSACSummary(sacSummary: SACSummary): Observable<any> {
    console.log(sacSummary);
    return this.http.post<SACSummary>(`${this.apiAdoptionToEAStandardUrl}` + 'submitSACSummary', sacSummary)
  }

  public getSACSummaryTask(): Observable<SACSummaryTask[]> {
    return this.http.get<SACSummaryTask[]>(`${this.apiAdoptionToEAStandardUrl}` + 'getTcSecSummaryTask')
  }

  public decisionByTCSecOnSACSummary(decisionFeedback: DecisionFeedback, sacSummaryId): Observable<any> {
    const params = new HttpParams()
        .set('decisionId', sacSummaryId)
    return this.http.post<DecisionFeedback>(`${this.apiAdoptionToEAStandardUrl}` + 'decisionByTCSecOnSACSummary', decisionFeedback, {
      params,
      responseType: 'arraybuffer' as 'json'
    }).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }


  public getSACSECTask(): Observable<SACSummary[]> {
    return this.http.get<SACSummary[]>(`${this.apiAdoptionToEAStandardUrl}` + 'getSACSummaryTask')
  }
    public decisionOnSACSummary(decisionFeedback: DecisionFeedback, decisionId): Observable<any> {
        const params = new HttpParams()
            .set('decisionId', decisionId)
        return this.http.post<DecisionFeedback>(`${this.apiAdoptionToEAStandardUrl}` + 'decisionOnSACSummary', decisionFeedback, {
            params,
            responseType: 'arraybuffer' as 'json'
        }).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }


  public decisionOnSACSEC(decisionFeedback: DecisionFeedback, decisionId): Observable<DecisionFeedback> {
    const params = new HttpParams()
        .set('decisionId', decisionId)
    return this.http.post<DecisionFeedback>(`${this.apiAdoptionToEAStandardUrl}` + 'decisionOnSACSEC', decisionFeedback, {
      params,
      responseType: 'arraybuffer' as 'json'
    }).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }

  //upload  Document
  public uploadFileDetails(SACSummaryID: string, data: FormData, doctype: string, docName: string): Observable<any> {
    const url = `${this.apiAdoptionToEAStandardUrl}file-upload`;

    return this.http.post<any>(url, data, {
      headers: {
        'enctype': 'multipart/form-data'
      }, params: {'docId': SACSummaryID, 'type': doctype, 'docName': docName}
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
    const url = `${this.apiAdoptionToEAStandardUrl}` + 'view';
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

}

