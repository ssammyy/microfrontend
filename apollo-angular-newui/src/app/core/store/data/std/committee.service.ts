import {Injectable} from '@angular/core';
import {ApiEndpointService} from "../../../services/endpoints/api-endpoint.service";
import {HttpClient, HttpErrorResponse, HttpHeaders, HttpParams} from "@angular/common/http";
import {Observable, throwError} from "rxjs";
import {catchError, map} from "rxjs/operators";
import {
    CommentMade,
    CommentMadeRetrieved,
    Committee_Draft,
    Committee_Draft_With_Name,
    Preliminary_Draft,
    StandardDocuments
} from "./commitee-model";
import {CommentsDto} from "./std.model";

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

    public getAllNwiSForPd(): any {
        return this.http.get<any>(`${this.apiServerUrl}getAllNwiSApprovedForPd`)
    }

    public getAllNwiSApprovedForPdPendingTasks(): any {
        return this.http.get<any>(`${this.apiServerUrl}getAllNwiSApprovedForPdPendingTasks`)
    }

    public getAllSdUsers(): any {
        return this.http.get<any>(`${this.apiServerUrl}getAllSdUsers`)
    }


    //upload  Minutes For PD
    public uploadMinutesForPd(pdId: string, data: FormData, doctype: string, docName: string): Observable<any> {
        const url = `${this.apiServerUrl}upload/minutes`;
        return this.http.post<any>(url, data, {
            headers: {
                'enctype': 'multipart/form-data'
            }, params: {'pdId': pdId, 'type': doctype, 'docName': docName}
        }).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    //upload Draft Documents For PD
    public uploadDraftDocumentsForPd(pdId: string, data: FormData, doctype: string, docName: string): Observable<any> {
        const url = `${this.apiServerUrl}upload/draftDocuments`;
        return this.http.post<any>(url, data, {
            headers: {
                'enctype': 'multipart/form-data'
            }, params: {'pdId': pdId, 'type': doctype, 'docName': docName}
        }).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    //upload Preliminary Draft Name
    public preparePreliminaryDraft(preliminaryDraft: Preliminary_Draft, nwiID: string): Observable<any> {
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
                return throwError(fault);
            })
        );
    }

    //get all preliminary drafts
    public getAllPreliminaryDrafts(): any {
        return this.http.get<any>(`${this.apiServerUrl}getAllPds`)
    }


    //get all preliminary drafts pending Cds
    public getAllPdPendingCds(): any {
        return this.http.get<any>(`${this.apiServerUrl}getAllPdPendingCds`)
    }

    //make comment
    public makeCommentB(comment: CommentsDto[], preliminary_draft_id, doctype: string): Observable<any> {
        const params = new HttpParams()
            .set('docType', doctype)
            .set('preliminary_draft_id', preliminary_draft_id)
        return this.http.post<CommentsDto[]>(`${this.apiServerUrl}` + 'makeComment', comment, {params})

    }
    public makeComment(comment: CommentMade, doctype: string): Observable<any> {
        const params = new HttpParams()
            .set('docType', doctype)
        return this.http.post<CommentMade>(`${this.apiServerUrl}` + 'makeComment', comment, {params})

    }

    //edit comment
    public editComment(comment: CommentMadeRetrieved): Observable<any> {
        return this.http.post<CommentMade>(`${this.apiServerUrl}` + 'editComment', comment)

    }

    //retrieve comment for logged in user
    public retrieveComment(): Observable<any> {
        return this.http.get<CommentMadeRetrieved>(`${this.apiServerUrl}` + 'getUserLoggedInCommentsOnPD')

    }

    //delete comment
    public deleteComment(comment: CommentMadeRetrieved): Observable<any> {
        return this.http.request('POST', `${this.apiServerUrl}` + 'deleteComment', {
            headers: new HttpHeaders({
                'Content-Type': 'application/json',
            }),
            body: {id: comment.commentsId}
        });


    }

    //get All Comments On PD
    public getCommentsOnPd(PdId: any): Observable<any> {
        const url = `${this.apiServerUrl}getAllCommentsOnPd`;
        const params = new HttpParams()
            .set('preliminaryDraftId', PdId)
        return this.http.get<CommentMadeRetrieved>(url, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    //get All Docs On PD
    public getDocsOnPd(PdId: any): Observable<any> {

        const url = `${this.apiServerUrl}getAllDocumentsOnPd`;
        const params = new HttpParams()
            .set('preliminaryDraftId', PdId)
        return this.http.get<StandardDocuments>(url, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    public viewDocs(docId: any, doctype: string): Observable<any> {
        const url = `${this.apiServerUrl}` + 'view';
        const params = new HttpParams()
            .set('docId', docId)
            .set('doctype', doctype);
        return this.http.get<any>(url, {params, responseType: 'arraybuffer' as 'json'}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    public viewDocsById(docId: any): Observable<any> {
        const url = `${this.apiServerUrl}` + 'viewById';
        const params = new HttpParams()
            .set('docId', docId)
        return this.http.get<any>(url, {params, responseType: 'arraybuffer' as 'json'}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    public viewDocsByIdB(docId: any): Observable<any> {
        const url = `${this.apiServerUrl}` + 'viewByIdB';
        const params = new HttpParams().set('docId', docId)
        return this.http.get<any>(url, {params, responseType: 'text' as 'json'})

    }


    //upload  Minutes For CD
    public uploadMinutesForCd(cdId: string, data: FormData, doctype: string, docName: string): Observable<any> {
        const url = `${this.apiServerUrl}upload/cdMinutes`;
        return this.http.post<any>(url, data, {
            headers: {
                'enctype': 'multipart/form-data'
            }, params: {'cdId': cdId, 'type': doctype, 'docName': docName}
        }).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    //upload Draft Documents For CD
    public uploadCdDraftDocuments(cdId: string, data: FormData, doctype: string, docName: string): Observable<any> {
        const url = `${this.apiServerUrl}upload/cdDraftDocuments`;
        return this.http.post<any>(url, data, {
            headers: {
                'enctype': 'multipart/form-data'
            }, params: {'cdId': cdId, 'type': doctype, 'docName': docName}
        }).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    //upload Committee Draft Name
    public prepareCommitteeDraft(committeeDraft: Committee_Draft, pdId: string): Observable<any> {
        const params = new HttpParams()
            .set('pdId', pdId)
        return this.http.post<Committee_Draft>(`${this.apiServerUrl}` + 'uploadCd', committeeDraft, {params})
    }

    //upload  CD Document
    public uploadCDDocument(cdID: string, data: FormData, doctype: string, docName: string): Observable<any> {
        const url = `${this.apiServerUrl}upload/cd`;
        return this.http.post<any>(url, data, {
            headers: {
                'enctype': 'multipart/form-data'
            }, params: {'cdID': cdID, 'type': doctype, 'docName': docName}
        }).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    //get all committee drafts
    public getAllCommitteeDrafts(): any {
        return this.http.get<any>(`${this.apiServerUrl}getAllCds`)
    }

    //get user logged in comments on committee draft
    //retrieve comment
    public retrieveUserLoggedInCommentsOnCd(): Observable<any> {
        return this.http.get<CommentMadeRetrieved>(`${this.apiServerUrl}` + 'getUserLoggedInCommentsOnCD')

    }

    //get All Docs On CD
    public getDocsOnCd(CdId: any): Observable<any> {

        const url = `${this.apiServerUrl}getAllDocumentsOnCd`;
        const params = new HttpParams()
            .set('committeeDraftId', CdId)
        return this.http.get<StandardDocuments>(url, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    //get All Comments On CD
    public getCommentsOnCd(CdId: any): Observable<any> {
        const url = `${this.apiServerUrl}getAllCommentsOnCd`;
        const params = new HttpParams()
            .set('committeeDraftId', CdId)
        return this.http.get<CommentMadeRetrieved>(url, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    //delete comment
    public approveCommitteeDraft(committeeDraft: Committee_Draft_With_Name): Observable<any> {
        return this.http.request('POST', `${this.apiServerUrl}` + 'approveCD', {
            headers: new HttpHeaders({
                'Content-Type': 'application/json',
            }),
            body: {id: committeeDraft.cdid}
        });


    }


}
