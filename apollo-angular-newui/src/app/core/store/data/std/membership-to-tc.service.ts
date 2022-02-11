import {Injectable} from '@angular/core';
import {ApiEndpointService} from "../../../services/endpoints/api-endpoint.service";
import {HttpClient} from "@angular/common/http";
import {
  CallForApplication,
  DecisionFeedback,
  HOFRecommendationTask,
  ReviewApplicationTask,
  SubmitApplication,
  SubmitApplicationsTask,
  TCMemberDetails
} from "./request_std.model";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class MembershipToTcService {
  protocol = `https://`;
  baseUrl = ApiEndpointService.DOMAIN.LOCAL_DEV
  private apiMembershipToTCUrl = `${this.protocol}${this.baseUrl}/api/v1/migration/membershipToTC/`;


  constructor(private http: HttpClient) {
  }

  public uploadCallForApplications(callForApplication: CallForApplication): Observable<any> {
    console.log(callForApplication);
    return this.http.post<CallForApplication>(`${this.apiMembershipToTCUrl}` + 'submitCallForApplication', callForApplication)
  }

  public getApplicantTasks(): Observable<SubmitApplicationsTask[]> {
    return this.http.get<SubmitApplicationsTask[]>(`${this.apiMembershipToTCUrl}` + 'getCallForApplications')
  }

  public onSubmitApplication(submitApplication: SubmitApplication): Observable<any> {
    console.log(submitApplication);
    return this.http.post<SubmitApplication>(`${this.apiMembershipToTCUrl}` + 'submitTCMemberApplication', submitApplication)
  }

  public getApplicationsForReview(): Observable<ReviewApplicationTask[]> {
    return this.http.get<ReviewApplicationTask[]>(`${this.apiMembershipToTCUrl}` + 'getApplicationsForReview')
  }

  public decisionOnApplications(decisionFeedback: DecisionFeedback): Observable<any> {
    console.log(decisionFeedback);
    return this.http.post<DecisionFeedback>(`${this.apiMembershipToTCUrl}` + 'decisionOnApplicantRecommendation', decisionFeedback)
  }

  public getHOFRecommendation(): Observable<HOFRecommendationTask[]> {
    return this.http.get<HOFRecommendationTask[]>(`${this.apiMembershipToTCUrl}` + 'getRecommendationsFromHOF')
  }

  public completeReview(id: string): any {
    return this.http.get<any>(`${this.apiMembershipToTCUrl}completeSPCReview/${id}`)
  }

  public getRecommendationsFromSPC(): Observable<HOFRecommendationTask[]> {
    return this.http.get<HOFRecommendationTask[]>(`${this.apiMembershipToTCUrl}` + 'getRecommendationsFromSPC')
  }

  public decisionOnSPCRecommendation(decisionFeedback: DecisionFeedback): Observable<any> {
    console.log(decisionFeedback);
    return this.http.post<DecisionFeedback>(`${this.apiMembershipToTCUrl}` + 'decisionOnSPCRecommendation', decisionFeedback)
  }

  public getTCMemberCreationTasks(): Observable<HOFRecommendationTask[]> {
    return this.http.get<HOFRecommendationTask[]>(`${this.apiMembershipToTCUrl}` + 'getTCMemberCreationTasks')
  }


  public saveTCMember(tCMemberDetails: TCMemberDetails): Observable<any> {
    console.log(tCMemberDetails);
    return this.http.post<TCMemberDetails>(`${this.apiMembershipToTCUrl}` + 'saveTCMember', tCMemberDetails)
  }
}
