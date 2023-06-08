import {Injectable} from '@angular/core';
import {ApiEndpointService} from "../../../services/endpoints/api-endpoint.service";
import {HttpClient, HttpErrorResponse, HttpHeaders, HttpParams} from "@angular/common/http";
import {Observable, throwError} from "rxjs";
import {catchError, map} from "rxjs/operators";
import {
    CommentMade,
    CommentMadeRetrieved,
    PublicReviewDraft, PublicReviewDraftWithName,
    StandardDocuments
} from "./commitee-model";
import {DataHolderB, Department} from "./request_std.model";
import {ISAdoptionProposal, PublicReviewDto, StakeHoldersFields} from "./std.model";

@Injectable({
    providedIn: 'root'
})
export class PublicReviewService {

    protocol = `https://`;
    baseUrl = ApiEndpointService.DOMAIN.LOCAL_DEV

    private apiServerUrl = `${this.protocol}${this.baseUrl}/api/v1/migration/publicReview/`;

    constructor(private http: HttpClient) {
    }

    //get all approved committee drafts
    public getAllApprovedCommitteeDrafts(): any {
        return this.http.get<any>(`${this.apiServerUrl}getAllApprovedCds`)
    }

    //upload  Minutes For PRD
    public uploadMinutesForPrd(cdId: string, data: FormData, doctype: string, docName: string): Observable<any> {
        const url = `${this.apiServerUrl}upload/prdMinutes`;
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

    //upload Draft Documents For PRD
    public uploadPrdDraftDocuments(cdId: string, data: FormData, doctype: string, docName: string): Observable<any> {
        const url = `${this.apiServerUrl}upload/prdDraftDocuments`;
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

    //upload Public Review Draft Name
    public preparePublicReviewDraft(publicReviewDraft: PublicReviewDraft, cdId: string): Observable<any> {
        const params = new HttpParams()
            .set('cdId', cdId)
        return this.http.post<PublicReviewDraft>(`${this.apiServerUrl}` + 'preparepr', publicReviewDraft, {params})
    }

    //upload  PRD Document
    public uploadPRDDocument(prdId: string, data: FormData, doctype: string, docName: string): Observable<any> {
        const url = `${this.apiServerUrl}upload/prd`;
        return this.http.post<any>(url, data, {
            headers: {
                'enctype': 'multipart/form-data'
            }, params: {'prdId': prdId, 'type': doctype, 'docName': docName}
        }).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }
    public uploadEditedPRDDocument(prdId: string, data: FormData, doctype: string, docName: string): Observable<any> {
        const url = `${this.apiServerUrl}upload/editedPrd`;
        return this.http.post<any>(url, data, {
            headers: {
                'enctype': 'multipart/form-data'
            }, params: {'prdId': prdId, 'type': doctype, 'docName': docName}
        }).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    //get all Public Review drafts
    public getAllPublicReviewDrafts(): any {
        return this.http.get<any>(`${this.apiServerUrl}getAllPrds`)
    }

    //make comment
    public makeCommentUnLoggedInUsers(comment: CommentMade): Observable<any> {

        return this.http.post<CommentMade>(`${this.apiServerUrl}` + 'makeComment', comment)

    }


    //sendToOrganisation
    public sendToOrganisation(publicReviewDraft: PublicReviewDraftWithName): Observable<any> {

        return this.http.post<PublicReviewDraftWithName>(`${this.apiServerUrl}` + 'sendToOrganisation', publicReviewDraft)

    }

    //sendToDepartments
    public sendToDepartments(department: DataHolderB, publicReviewId:string): Observable<any> {

        const params = new HttpParams()
            .set('publicReviewDraftId', publicReviewId)
        return this.http.post<PublicReviewDraft>(`${this.apiServerUrl}` + 'sendToDepartments', department, {params})



    }

    //upload Edited Review Draft Name
    public prepareEditedPublicReviewDraft(publicReviewDraft: PublicReviewDraft, publicReviewDraftId: string): Observable<any> {
        const params = new HttpParams()
            .set('publicReviewDraftId', publicReviewDraftId)
        return this.http.post<PublicReviewDraft>(`${this.apiServerUrl}` + 'editPrd', publicReviewDraft, {params})
    }

    public sendPublicReview(publicReviewDto: PublicReviewDto,stakeHoldersFields: StakeHoldersFields[]): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.PR_UPLOAD_STAKE_HOLDERS);
        const params = new HttpParams();
        publicReviewDto.addStakeholdersList=stakeHoldersFields
        return this.http.post<ISAdoptionProposal>(url, publicReviewDto, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    //postToWebsite
    public postToWebsite(publicReviewDraft: PublicReviewDraftWithName): Observable<any> {

        return this.http.post<PublicReviewDraft>(`${this.apiServerUrl}` + 'postToWebsite', publicReviewDraft)

    }


    //retrieve comment for logged in user
    public retrieveComment(): Observable<any> {
        return this.http.get<CommentMadeRetrieved>(`${this.apiServerUrl}` + 'getUserLoggedInCommentsOnPrd')

    }


    //get All Docs On PRD
    public getAllDocumentsOnPrd(PrdId: any): Observable<any> {

        const url = `${this.apiServerUrl}getAllDocumentsOnPrd`;
        const params = new HttpParams()
            .set('publicReviewDraftId', PrdId)
        return this.http.get<StandardDocuments>(url, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    //get All Comments On Prd
    public getAllCommentsOnPrd(PrdId: any): Observable<any> {
        const url = `${this.apiServerUrl}getAllCommentsOnPrd`;
        const params = new HttpParams()
            .set('publicReviewDraftId', PrdId)
        return this.http.get<CommentMadeRetrieved>(url, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }


    //approve prd
    public approvePublicReviewDraft(publicReviewDraft: PublicReviewDraftWithName): Observable<any> {

        return this.http.post<PublicReviewDraft>(`${this.apiServerUrl}` + 'approvePrd', publicReviewDraft)


    }

    //approve prd
    public getApprovePublicReviewDraft(): Observable<any> {

        return this.http.get<PublicReviewDraft>(`${this.apiServerUrl}` + 'getAllApprovedPrds')


    }


}
