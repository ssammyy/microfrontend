import {Injectable} from '@angular/core';
import {ApiEndpointService} from "../../../services/endpoints/api-endpoint.service";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {DecisionFeedback, ProposalForTC, ReviewFeedbackFromSPC, ReviewFormationOFTCRequest} from "./request_std.model";
import {Observable, throwError} from "rxjs";
import {JustificationForTc} from "./formation_of_tc.model";
import {catchError, map} from "rxjs/operators";

@Injectable({
    providedIn: 'root'
})
export class FormationOfTcService {
    protocol = `https://`;
    baseUrl = ApiEndpointService.DOMAIN.LOCAL_DEV
    private apiServerUrl2 = `${this.protocol}${this.baseUrl}/api/v1/migration/anonymous/standard/dropdown/`;
    private apiMembershipToTCUrl = `${this.protocol}${this.baseUrl}/api/v1/migration/membershipToTC/`;
    private formationOfTcUrl = `${this.protocol}${this.baseUrl}/api/v1/migration/formationOfTC/`;

    constructor(private http: HttpClient) {
    }

    public uploadProposalForTC(proposalForTC: ProposalForTC): Observable<any> {

        console.log(proposalForTC);
        return this.http.post<ProposalForTC>(`${this.formationOfTcUrl}` + 'submitJustification', proposalForTC)
    }

    public reviewProposal(): Observable<ReviewFormationOFTCRequest[]> {
        return this.http.get<ReviewFormationOFTCRequest[]>(`${this.formationOfTcUrl}` + 'getSPCTasks')
    }

    public decisionOnTCProposal(decisionFeedback: DecisionFeedback): Observable<DecisionFeedback> {

        console.log(decisionFeedback);
        return this.http.post<DecisionFeedback>(`${this.formationOfTcUrl}` + 'decisionOnJustificationForTC', decisionFeedback)
    }

    public reviewFeedback(): Observable<ReviewFeedbackFromSPC[]> {
        return this.http.get<ReviewFeedbackFromSPC[]>(`${this.formationOfTcUrl}` + 'getSACTasks')
    }

    public decisionOnSPCFeedback(decisionFeedback: DecisionFeedback): Observable<DecisionFeedback> {
        console.log(decisionFeedback);
        return this.http.post<DecisionFeedback>(`${this.formationOfTcUrl}` + 'decisionOnSPCFeedback', decisionFeedback)
    }


    public tcUploadProposalForTC(justificationForTc: JustificationForTc): Observable<JustificationForTc> {
        return this.http.post<JustificationForTc>(`${this.formationOfTcUrl}` + 'submitJustification', justificationForTc)
    }

    public getAllHofJustifications(): Observable<JustificationForTc[]> {
        return this.http.get<JustificationForTc[]>(`${this.formationOfTcUrl}` + 'getAllHofJustifications')
    }

    public approveJustificationForTC(justificationForTc: JustificationForTc): Observable<JustificationForTc> {
        return this.http.post<JustificationForTc>(`${this.formationOfTcUrl}` + 'approveJustification', justificationForTc)
    }

    public rejectJustificationForTC(justificationForTc: JustificationForTc): Observable<JustificationForTc> {
        return this.http.post<JustificationForTc>(`${this.formationOfTcUrl}` + 'rejectJustification', justificationForTc)
    }

    public getAllSpcJustifications(): Observable<JustificationForTc[]> {
        return this.http.get<JustificationForTc[]>(`${this.formationOfTcUrl}` + 'getAllSpcJustifications')
    }

    public getAllJustificationsRejectedBySpc(): Observable<JustificationForTc[]> {
        return this.http.get<JustificationForTc[]>(`${this.formationOfTcUrl}` + 'getAllJustificationsRejectedBySpc')
    }

    public approveJustificationSPCForTC(justificationForTc: JustificationForTc): Observable<JustificationForTc> {
        return this.http.post<JustificationForTc>(`${this.formationOfTcUrl}` + 'approveJustificationSPCForTC', justificationForTc)

    }

    public rejectJustificationSPC(justificationForTc: JustificationForTc): Observable<JustificationForTc> {
        return this.http.post<JustificationForTc>(`${this.formationOfTcUrl}` + 'rejectJustificationSPC', justificationForTc)

    }

    public sacGetAllApprovedJustificationsBySpc(): Observable<JustificationForTc[]> {
        return this.http.get<JustificationForTc[]>(`${this.formationOfTcUrl}` + 'sacGetAllApprovedJustificationsBySpc')
    }

    public approveJustificationSAC(justificationForTc: JustificationForTc): Observable<JustificationForTc> {
        return this.http.post<JustificationForTc>(`${this.formationOfTcUrl}` + 'approveJustificationSAC', justificationForTc)

    }

    public advertiseTcToWebsite(justificationForTc: JustificationForTc): Observable<JustificationForTc> {
        return this.http.post<JustificationForTc>(`${this.formationOfTcUrl}` + 'advertiseTcToWebsite', justificationForTc)

    }

    public uploadAdditionalDocuments(proposalId: string, data: FormData, doctype: string, docDescription: string): Observable<any> {
        const url = `${this.formationOfTcUrl}uploadAdditionalDocs`;
        return this.http.post<any>(url, data, {
            headers: {
                'enctype': 'multipart/form-data'
            }, params: {'proposalId': proposalId, 'type': doctype, 'docDescription': docDescription}
        }).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

}
