import {Injectable} from '@angular/core';
import {ApiEndpointService} from "../../../services/endpoints/api-endpoint.service";
import {HttpClient, HttpErrorResponse, HttpHeaders, HttpParams} from "@angular/common/http";
import {Observable, throwError} from "rxjs";
import {catchError, map} from "rxjs/operators";
import {
    Ballot_Draft,
    CommentMade,
    CommentMadeRetrieved,
    PublicReviewDraft, PublicReviewDraftWithName,
    StandardDocuments, Vote, VoteRetrieved
} from "./commitee-model";
import {DataHolderB, Department} from "./request_std.model";
import {AllBatchInvoiceDetailsDto, GenerateInvoiceDto, PermitEntityDto} from "../qa/qa.model";

@Injectable({
    providedIn: 'root'
})
export class BallotService {

    protocol = `https://`;
    baseUrl = ApiEndpointService.DOMAIN.LOCAL_DEV

    private apiServerUrl = `${this.protocol}${this.baseUrl}/api/v1/migration/ballot/`;

    constructor(private http: HttpClient) {
    }

    //upload  Minutes For Ballot Draft
    public uploadMinutesForBallot(prdId: string, data: FormData, doctype: string, docName: string): Observable<any> {
        const url = `${this.apiServerUrl}upload/ballotMinutes`;
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

    //upload Draft Documents For Ballot Draft
    public uploadBallotDraftDocuments(prdId: string, data: FormData, doctype: string, docName: string): Observable<any> {
        const url = `${this.apiServerUrl}upload/ballotDraftDocuments`;
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

    //upload Ballot Review Draft Name
    public prepareBallotDraft(ballotDraft: Ballot_Draft, prdId: string): Observable<any> {
        const params = new HttpParams()
            .set('prdId', prdId)
        return this.http.post<PublicReviewDraft>(`${this.apiServerUrl}` + 'prepareBallot', ballotDraft, {params})
    }

    //upload  PRD Document
    public uploadBallotDocument(ballotId: string, data: FormData, doctype: string, docName: string): Observable<any> {
        const url = `${this.apiServerUrl}upload/ballot`;
        return this.http.post<any>(url, data, {
            headers: {
                'enctype': 'multipart/form-data'
            }, params: {'ballotId': ballotId, 'type': doctype, 'docName': docName}
        }).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }


    //get all Ballot drafts
    public getAllBallotDrafts(): any {
        return this.http.get<any>(`${this.apiServerUrl}getBallots`)
    }

    //vote
    public vote(vote: Vote): Observable<any> {
        const url =`${this.apiServerUrl}` + 'voteBallot';

        return this.http.post<Vote>(url, vote).pipe(
            map(function (response: Vote) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );

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
    public sendToDepartments(department: DataHolderB, publicReviewId: string): Observable<any> {

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

    //postToWebsite
    public postToWebsite(publicReviewDraft: PublicReviewDraftWithName): Observable<any> {

        return this.http.post<PublicReviewDraft>(`${this.apiServerUrl}` + 'postToWebsite', publicReviewDraft)

    }


    //retrieve vote for logged in user
    public retrieveVote(): Observable<any> {
        return this.http.get<VoteRetrieved>(`${this.apiServerUrl}` + 'getMyBallotVotes')

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

    //get All Votes On Ballot
    public getAllVotesOnBallot(BallotId: any): Observable<any> {
        const url = `${this.apiServerUrl}getAllBallotVotes`;
        const params = new HttpParams()
            .set('ballotId', BallotId)
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


}
