import {Injectable} from '@angular/core';
import {ApiEndpointService} from "../../../services/endpoints/api-endpoint.service";
import {HttpClient} from "@angular/common/http";
import {DecisionFeedback, ProposalForTC, ReviewFeedbackFromSPC, ReviewFormationOFTCRequest} from "./request_std.model";
import {Observable} from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class FormationOfTcService {
    protocol = `https://`;
    baseUrl = ApiEndpointService.DOMAIN.LOCAL_DEV
    private apiServerUrl2 = `${this.protocol}${this.baseUrl}/api/v1/migration/anonymous/standard/dropdown/`;
    private apiMembershipToTCUrl = `${this.protocol}${this.baseUrl}/api/v1/migration/membershipToTC/`;
    private apiTechnicalCommitteeUrl = `${this.protocol}${this.baseUrl}/api/v1/migration/formationOfTC/`;

    constructor(private http: HttpClient) {
    }

    public uploadProposalForTC(proposalForTC: ProposalForTC): Observable<ProposalForTC> {

        console.log(proposalForTC);
        return this.http.post<ProposalForTC>(`${this.apiTechnicalCommitteeUrl}` + 'submitJustification', proposalForTC)
    }

    public reviewProposal(): Observable<ReviewFormationOFTCRequest[]> {
        return this.http.get<ReviewFormationOFTCRequest[]>(`${this.apiTechnicalCommitteeUrl}` + 'getSPCTasks')
    }

    public decisionOnTCProposal(decisionFeedback: DecisionFeedback): Observable<DecisionFeedback> {

        console.log(decisionFeedback);
        return this.http.post<DecisionFeedback>(`${this.apiTechnicalCommitteeUrl}` + 'decisionOnJustificationForTC', decisionFeedback)
    }

    public reviewFeedback(): Observable<ReviewFeedbackFromSPC[]> {
        return this.http.get<ReviewFeedbackFromSPC[]>(`${this.apiTechnicalCommitteeUrl}` + 'getSACTasks')
    }

    public decisionOnSPCFeedback(decisionFeedback: DecisionFeedback): Observable<DecisionFeedback> {
        console.log(decisionFeedback);
        return this.http.post<DecisionFeedback>(`${this.apiTechnicalCommitteeUrl}` + 'decisionOnSPCFeedback', decisionFeedback)
    }

}
