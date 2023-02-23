import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpParams} from "@angular/common/http";
import {Observable, throwError} from "rxjs";
import {
  CommentOnProposal,
  CommentsOnProposal, ComStdRemarks,
  GazetteStandard,
  InternationalStandardsComments,
  KNWCommittee,
  KNWDepartment,
  ProposalComment,
  ProposalComments,
  ReviewComments,
  ReviewDecision,
  ReviewDraftEditing,
  ReviewedStandards,
  ReviewForm,
  ReviewProposalComments,
  ReviewRecommendation,
  ReviewStandardsComments,
  RevProposalComments, SRProposalComments, SRStdComments, SRStdForRecommendation,
  StakeholderProposalComments,
  StandardReviewComments,
  StandardReviewRecommendations,
  StandardReviewTasks,
  StandardsForReview,
  SystemicAnalyseComments,
  UsersEntity
} from "./std.model";
import {ApiEndpointService} from "../../../services/endpoints/api-endpoint.service";
import {catchError, map} from "rxjs/operators";
import {EditCompanyDTO} from "../levy/levy.model";

@Injectable({
  providedIn: 'root'
})
export class StdReviewService {

  constructor(private http: HttpClient) { }


  public reviewedStandards(): Observable<ReviewedStandards[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.SR_GET_REVIEWED_STANDARDS);
    const params = new HttpParams();
    return this.http.get<ReviewedStandards[]>(url, {params}).pipe();
  }


  public reviewStandard(reviewForm: ReviewForm): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.SR_REVIEW_FORM);
    const params = new HttpParams();
    return this.http.post<ReviewForm>(url, reviewForm, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }
  public getReviewForms(): Observable<ReviewComments[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.SR_GET_REVIEW_FORM);
    const params = new HttpParams();
    return this.http.get<ReviewComments[]>(url, {params}).pipe();
  }


  public commentsOnReview(standardReviewComments: StandardReviewComments): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.SR_REVIEW_COMMENTS);
    const params = new HttpParams();
    return this.http.post<StandardReviewComments>(url, standardReviewComments, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }
  public getReviewTasks(): Observable<SystemicAnalyseComments[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.SR_GET_REVIEW_TASKS);
    const params = new HttpParams();
    return this.http.get<SystemicAnalyseComments[]>(url, {params}).pipe();
  }



  public uploadFileDetails(nwaJustificationID: string, data: FormData): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.SR_UPLOAD_DOCUMENT);

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

  public getStandardsForReview(): Observable<StandardsForReview[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.SR_SD_FOR_REVIEW_STD);
    const params = new HttpParams();
    return this.http.get<StandardsForReview[]>(url, {params}).pipe();
  }
  public getProposal(): Observable<SRProposalComments[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.SR_GET_PROPOSALS_FOR_COMMENT);
    const params = new HttpParams();
    return this.http.get<SRProposalComments[]>(url, {params}).pipe();
  }

  public standardReviewForm(standardsForReview: StandardsForReview): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.SR_INITIATE_STD_REVIEW);
    const params = new HttpParams();
    return this.http.post<StandardsForReview>(url, standardsForReview, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }

  public getStandardsProposalForComment(): Observable<RevProposalComments[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.SR_SD_PROPOSAL_FOR_COMMENTS);
    const params = new HttpParams();
    return this.http.get<RevProposalComments[]>(url, {params}).pipe();
  }

  public submitAPComments(commentsOnProposal: CommentOnProposal): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.SR_SUBMIT_PROPOSAL_COMMENTS);
    const params = new HttpParams();
    return this.http.post<CommentOnProposal>(url, commentsOnProposal, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }

  public getProposalsComments(reviewId: any): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.SR_SD_GET_PROPOSAL_COMMENTS);
    const params = new HttpParams().set('reviewId', reviewId);
    return this.http.get<SRStdComments>(url, {params}).pipe();
  }

  public getStandardsForRecommendation(): Observable<SRStdForRecommendation[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.SR_STD_FOR_RECOMMENDATION);
    const params = new HttpParams();
    return this.http.get<SRStdForRecommendation[]>(url, {params}).pipe();
  }
  public getStandardsForSpcAction(): Observable<SRStdForRecommendation[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.SR_STD_FOR_SPC_ACTION);
    const params = new HttpParams();
    return this.http.get<SRStdForRecommendation[]>(url, {params}).pipe();
  }

  public getTcSecTasks(): Observable<StandardReviewTasks[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.SR_SD_TC_SEC_TASKS);
    const params = new HttpParams();
    return this.http.get<StandardReviewTasks[]>(url, {params}).pipe();
  }

  public getDraughtsManTasks(): Observable<StandardReviewTasks[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.SR_SD_DR_MAN_TASKS);
    const params = new HttpParams();
    return this.http.get<StandardReviewTasks[]>(url, {params}).pipe();
  }

  public getProofReaderTasks(): Observable<StandardReviewTasks[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.SR_SD_PROOF_READER_TASKS);
    const params = new HttpParams();
    return this.http.get<StandardReviewTasks[]>(url, {params}).pipe();
  }

  public getEditorTasks(): Observable<StandardReviewTasks[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.SR_SD_EDITOR_TASKS);
    const params = new HttpParams();
    return this.http.get<StandardReviewTasks[]>(url, {params}).pipe();
  }

  public getSacSecTasks(): Observable<StandardReviewTasks[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.SR_SD_SAC_SEC_TASKS);
    const params = new HttpParams();
    return this.http.get<StandardReviewTasks[]>(url, {params}).pipe();
  }



  public getHopTasks(): Observable<StandardReviewTasks[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.SR_SD_HOP_TASKS);
    const params = new HttpParams();
    return this.http.get<StandardReviewTasks[]>(url, {params}).pipe();
  }

  public getSpcSecTasks(): Observable<StandardReviewTasks[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.SR_SD_SPC_SEC_TASKS);
    const params = new HttpParams();
    return this.http.get<StandardReviewTasks[]>(url, {params}).pipe();
  }
  public getHoSicTasks(): Observable<StandardReviewTasks[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.SR_SD_GAZETTE_TASKS);
    const params = new HttpParams();
    return this.http.get<StandardReviewTasks[]>(url, {params}).pipe();
  }

  public getStandardsProposalComments(proposalId: any): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.SR_SD_PROPOSAL_COMMENTS);
    const params = new HttpParams().set('proposalId', proposalId);
    return this.http.get<ReviewProposalComments>(url, {params}).pipe();
  }

  public getUserComments(id: any): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.SR_SD_USER_COMMENTS);
    const params = new HttpParams().set('id', id);
    return this.http.get<ReviewStandardsComments>(url, {params}).pipe();
  }

  public makeRecommendationsOnAdoptionProposal(reviewRecommendation: ReviewRecommendation): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.SR_SD_PROPOSAL_RECOMMENDATIONS);
    const params = new HttpParams();
    return this.http.post<ReviewRecommendation>(url, reviewRecommendation, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }

  public decisionOnRecommendation(standardReviewRecommendations: StandardReviewRecommendations): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.SR_SD_DECISION_ON_RECOMMENDATIONS);
    const params = new HttpParams();
    return this.http.post<StandardReviewRecommendations>(url, standardReviewRecommendations, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }

  public levelUpDecisionOnRecommendations(standardReviewRecommendations: StandardReviewRecommendations): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.SR_SD_PROPOSAL_RECOMMENDATIONS_DECISION_LEVEL_UP);
    const params = new HttpParams();
    return this.http.post<StandardReviewRecommendations>(url, standardReviewRecommendations, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }
  public updateGazette(gazetteStandard: GazetteStandard): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.SR_SD_UPDATE_GAZETTE);
    const params = new HttpParams();
    return this.http.post<GazetteStandard>(url, gazetteStandard, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }

  public updateGazettementDate(gazetteStandard: GazetteStandard): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.SR_SD_UPDATE_GAZETTE_DATE);
    const params = new HttpParams();
    return this.http.post<GazetteStandard>(url, gazetteStandard, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }

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

  public getKnwSecretary(): Observable<UsersEntity[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.NWA_KNW_SECRETARY);
    const params = new HttpParams();
    return this.http.get<UsersEntity[]>(url, {params}).pipe();
  }

  public submitDraftForEditing(reviewDraftEditing: ReviewDraftEditing): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.SR_SD_SUBMIT_DRAFT_FOR_EDITING);
    const params = new HttpParams();
    return this.http.post<ReviewDraftEditing>(url, reviewDraftEditing, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }

  public checkRequirements(reviewDecision: ReviewDecision): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.SR_SD_CHECK_REQUIREMENTS);
    const params = new HttpParams();
    return this.http.post<ReviewDecision>(url, reviewDecision, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }

  public editStandardDraft(reviewDraftEditing: ReviewDraftEditing): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.SR_SD_EDIT_STD_DRAFT);
    const params = new HttpParams();
    return this.http.post<ReviewDraftEditing>(url, reviewDraftEditing, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }

  public draftStandard(reviewDraftEditing: ReviewDraftEditing): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.SR_SD_DRAFT_STD);
    const params = new HttpParams();
    return this.http.post<ReviewDraftEditing>(url, reviewDraftEditing, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }

  public proofReadStandard(reviewDraftEditing: ReviewDraftEditing): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.SR_SD_PROOF_READ_STD);
    const params = new HttpParams();
    return this.http.post<ReviewDraftEditing>(url, reviewDraftEditing, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }

  public checkStandardDraft(reviewDecision: ReviewDecision): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.SR_SD_CHECK_STD_DRAFT);
    const params = new HttpParams();
    return this.http.post<ReviewDecision>(url, reviewDecision, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }






}
