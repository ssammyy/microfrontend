import {Injectable} from '@angular/core';
import {
  DraughtsmanTask,
  EditorTask,
  ProofReadTask,
  StandardDraft,
  StandardTasks,
  StdTCDecision
} from "./request_std.model";
import {Observable, throwError} from "rxjs";
import {ApiEndpointService} from "../../../services/endpoints/api-endpoint.service";
import {HttpClient, HttpErrorResponse, HttpParams} from "@angular/common/http";
import {catchError, map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class PublishingService {
  protocol = `https://`;
  baseUrl = ApiEndpointService.DOMAIN.LOCAL_DEV
  private apiPublishingUrl = `${this.protocol}${this.baseUrl}/api/v1/migration/publishing/`;

  constructor(private http: HttpClient) {
  }

  public uploadStdDraft(uploadStdDraft: StandardDraft): Observable<any> {
    return this.http.post<StandardDraft>(`${this.apiPublishingUrl}` + 'submit', uploadStdDraft)
  }

  //upload justification Document
  public uploadFileDetails(draftStandardID: string, data: FormData): Observable<any> {
    const url = `${this.apiPublishingUrl}file-upload`;

    return this.http.post<any>(url, data, {
      headers: {
        'enctype': 'multipart/form-data'
      }, params: {'draftStandardID': draftStandardID}
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

  public getHOPTasks(): Observable<EditorTask[]> {
    return this.http.get<EditorTask[]>(`${this.apiPublishingUrl}` + 'getHOPTasks')
  }

  public decisionOnKSDraft(reviewTask: StdTCDecision): Observable<any> {
    return this.http.post<StandardTasks>(`${this.apiPublishingUrl}` + 'decisionOnKSDraft', reviewTask).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }


  public getEditorTasks(): Observable<EditorTask[]> {
    return this.http.get<EditorTask[]>(`${this.apiPublishingUrl}` + 'getEditorTasks')
  }


  public completeEditing(editorTask: EditorTask): Observable<any> {
    return this.http.post<EditorTask>(`${this.apiPublishingUrl}` + 'finishEditingDraft', editorTask)
  }

  public getProofReaderTasks(): Observable<ProofReadTask[]> {
    return this.http.get<ProofReadTask[]>(`${this.apiPublishingUrl}` + 'getProofReaderTasks')
  }

  public decisionOnProofReading(reviewTask: StdTCDecision): Observable<any> {
    return this.http.post<StandardTasks>(`${this.apiPublishingUrl}` + 'finishedProofReading', reviewTask)
  }

  public getDraughtsmanTasks(): Observable<DraughtsmanTask[]> {
    return this.http.get<DraughtsmanTask[]>(`${this.apiPublishingUrl}` + 'getDraughtsmanTasks')
  }

  public uploadStdDraught(uploadStdDraft: StandardDraft): Observable<any> {

    console.log(uploadStdDraft);
    return this.http.post<StandardDraft>(`${this.apiPublishingUrl}` + 'uploadDraughtChanges', uploadStdDraft)
  }

  public uploadFinishedStd(uploadStdDraft: StandardDraft): Observable<any> {

    console.log(uploadStdDraft);
    return this.http.post<StandardDraft>(`${this.apiPublishingUrl}` + 'approvedDraftStandard', uploadStdDraft)
  }

  public viewDEditedApplicationPDF(editedDraftStandardDocumentId: any): Observable<any> {
    const url = `${this.apiPublishingUrl}` + 'getDraughtsmanTasks';
    const params = new HttpParams()
        .set('editedDraftStandardDocumentId', editedDraftStandardDocumentId);
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
