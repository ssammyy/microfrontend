import {Injectable} from '@angular/core';
import {
    DraftEditing,
    DraftPublishing,
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
    public uploadFileDetails(draftStandardID: string, data: FormData, doctype: string): Observable<any> {
        const url = `${this.apiPublishingUrl}file-upload`;

        return this.http.post<any>(url, data, {
            headers: {
                'enctype': 'multipart/form-data'
            }, params: {'draftStandardID': draftStandardID, 'type': doctype}
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

    public getHOPTasks(): Observable<DraftPublishing[]> {
        return this.http.get<DraftPublishing[]>(`${this.apiPublishingUrl}` + 'getHOPTasks')
    }

    public getHOPTasksApproved(): Observable<DraftPublishing[]> {
        return this.http.get<DraftPublishing[]>(`${this.apiPublishingUrl}` + 'approvedForEditing')
    }

    public getHOPTasksRejected(): Observable<DraftPublishing[]> {
        return this.http.get<DraftPublishing[]>(`${this.apiPublishingUrl}` + 'rejectedForEditing')
    }

    public decisionOnKSDraft(reviewTask: DraftPublishing, decision): Observable<any> {
        const params = new HttpParams()
            .set('decision', decision)
        return this.http.post<StandardTasks>(`${this.apiPublishingUrl}` + 'submit', reviewTask, {
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


    public getEditorTasks(): Observable<DraftEditing[]> {
        return this.http.get<DraftEditing[]>(`${this.apiPublishingUrl}` + 'getEditorTasks')
    }


    public completeEditing(editorTask: EditorTask, draftStandardId): Observable<any> {
        const params = new HttpParams()
            .set('draftStandardID', draftStandardId)
        return this.http.post<EditorTask>(`${this.apiPublishingUrl}` + 'finishEditingDraft', editorTask, {
            params,
            responseType: 'arraybuffer' as 'json'
        })
    }

    public getProofReaderTasks(): Observable<DraftEditing[]> {
        return this.http.get<DraftEditing[]>(`${this.apiPublishingUrl}` + 'getProofReaderTasks')
    }

    // public decisionOnProofReading(reviewTask: StdTCDecision): Observable<any> {
    //   return this.http.post<StandardTasks>(`${this.apiPublishingUrl}` + 'finishedProofReading', reviewTask)
    // }
    public decisionOnProofReading(reviewTask: StdTCDecision, draftStandardId): Observable<any> {
        const params = new HttpParams()
            .set('draftStandardID', draftStandardId)
        return this.http.post<StandardTasks>(`${this.apiPublishingUrl}` + 'finishedProofReading', reviewTask, {
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

    public sendForDraughting(reviewTask: DraftEditing, decision): Observable<any> {
        const params = new HttpParams()
            .set('decision', decision)
        return this.http.post<DraftEditing>(`${this.apiPublishingUrl}` + 'approvedForDraughting', reviewTask, {
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

    public sendForProofreading(reviewTask: DraftEditing, decision): Observable<any> {
        const params = new HttpParams()
            .set('decision', decision)
        return this.http.post<DraftEditing>(`${this.apiPublishingUrl}` + 'approvedForProofReading', reviewTask, {
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
    public sendToOfficer(reviewTask: DraftEditing, decision): Observable<any> {
        const params = new HttpParams()
            .set('decision', decision)
        return this.http.post<DraftEditing>(`${this.apiPublishingUrl}` + 'sendToOfficer', reviewTask, {
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


    public getDraughtsmanTasks(): Observable<DraftEditing[]> {
        return this.http.get<DraftEditing[]>(`${this.apiPublishingUrl}` + 'getDraughtsmanTasks')
    }

    public uploadStdDraught(uploadStdDraft: DraftEditing, decision): Observable<any> {
        const params = new HttpParams()
            .set('decision', decision)
        return this.http.post<StandardDraft>(`${this.apiPublishingUrl}` + 'uploadDraughtChanges', uploadStdDraft, {
            params,
            responseType: 'arraybuffer' as 'json'
        })
    }

    public uploadFinishedStd(uploadStdDraft: StandardDraft): Observable<any> {

        console.log(uploadStdDraft);
        return this.http.post<StandardDraft>(`${this.apiPublishingUrl}` + 'approvedDraftStandard', uploadStdDraft)
    }

    public viewDEditedApplicationPDF(editedDraftStandardDocumentId: any, doctype: string): Observable<any> {
        const url = `${this.apiPublishingUrl}` + 'view/draftStandard';
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
