import {Injectable} from '@angular/core';
import {ApiEndpointService} from "../../../services/endpoints/api-endpoint.service";
import {HttpClient, HttpErrorResponse, HttpParams} from "@angular/common/http";
import {
    DecisionFeedback,
    Document,
    ProposalForTC,
    ReviewFeedbackFromSPC,
    ReviewFormationOFTCRequest
} from "./request_std.model";
import {Observable, throwError} from "rxjs";
import {JustificationForTc} from "./formation_of_tc.model";
import {catchError, map} from "rxjs/operators";

@Injectable({
    providedIn: 'root'
})
export class FormationOfTcService {
    protocol = `https://`;
    baseUrl = ApiEndpointService.DOMAIN.LOCAL_DEV
    private formationOfTcUrl = `${this.protocol}${this.baseUrl}/api/v1/migration/formationOfTC/`;
    constructor(private http: HttpClient) {
    }
    public uploadProposalForTC(proposalForTC: ProposalForTC): Observable<any> {

        console.log(proposalForTC);
        return this.http.post<ProposalForTC>(`${this.formationOfTcUrl}` + 'submitJustification', proposalForTC)
    }

    public editProposalForTC(proposalForTC: ProposalForTC): Observable<any> {

        console.log(proposalForTC);
        return this.http.post<ProposalForTC>(`${this.formationOfTcUrl}` + 'editJustification', proposalForTC)
    }



    public getAllHofJustifications(): Observable<JustificationForTc[]> {
        return this.http.get<JustificationForTc[]>(`${this.formationOfTcUrl}` + 'getAllHofJustifications')
    }

    public approveJustificationForTC(justificationForTc: JustificationForTc): Observable<any> {
        return this.http.post<JustificationForTc>(`${this.formationOfTcUrl}` + 'approveJustification', justificationForTc)
    }

    public rejectJustificationForTC(justificationForTc: JustificationForTc): Observable<any> {
        return this.http.post<JustificationForTc>(`${this.formationOfTcUrl}` + 'rejectJustification', justificationForTc)
    }

    public getAllSpcJustifications(): Observable<JustificationForTc[]> {
        return this.http.get<JustificationForTc[]>(`${this.formationOfTcUrl}` + 'getAllSpcJustifications')
    }

    public getAllJustificationsRejectedBySpc(): Observable<JustificationForTc[]> {
        return this.http.get<JustificationForTc[]>(`${this.formationOfTcUrl}` + 'getAllJustificationsRejectedBySpc')
    }

    public approveJustificationSPCForTC(justificationForTc: JustificationForTc): Observable<any> {
        return this.http.post<JustificationForTc>(`${this.formationOfTcUrl}` + 'approveJustificationSPC', justificationForTc)

    }

    public rejectJustificationSPC(justificationForTc: JustificationForTc): Observable<any> {
        return this.http.post<JustificationForTc>(`${this.formationOfTcUrl}` + 'rejectJustificationSPC', justificationForTc)

    }

    public sacGetAllApprovedJustificationsBySpc(): Observable<JustificationForTc[]> {
        return this.http.get<JustificationForTc[]>(`${this.formationOfTcUrl}` + 'sacGetAllApprovedJustificationsBySpc')
    }

    public sacGetAllRejectedJustificationsBySpc(): Observable<JustificationForTc[]> {
        return this.http.get<JustificationForTc[]>(`${this.formationOfTcUrl}` + 'sacGetAllRejectedJustificationsBySpc')
    }


    public approveJustificationSAC(justificationForTc: JustificationForTc): Observable<any> {
        return this.http.post<JustificationForTc>(`${this.formationOfTcUrl}` + 'approveJustificationSAC', justificationForTc)

    }

    public rejectJustificationSAC(justificationForTc: JustificationForTc): Observable<any> {
        return this.http.post<JustificationForTc>(`${this.formationOfTcUrl}` + 'rejectJustificationSAC', justificationForTc)

    }
    public sacGetAllForWebsite(): Observable<JustificationForTc[]> {
        return this.http.get<JustificationForTc[]>(`${this.formationOfTcUrl}` + 'sacGetAllForWebsite')
    }
    public sacGetAllRejected(): Observable<JustificationForTc[]> {
        return this.http.get<JustificationForTc[]>(`${this.formationOfTcUrl}` + 'sacGetAllRejected')
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

    public getAdditionalDocuments(proposalId: string): Observable<any> {

        const url = `${this.formationOfTcUrl}getAdditionalDocuments`;
        const params = new HttpParams().set('proposalId', proposalId)
        return this.http.get<Document>(url, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    public viewDocsById(docId: any): Observable<any> {
        const url = `${this.formationOfTcUrl}` + 'viewById';
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

    public getAllApprovedJustifications(): Observable<JustificationForTc[]> {
        return this.http.get<JustificationForTc[]>(`${this.formationOfTcUrl}` + 'getAllApprovedJustifications')
    }
    public getAllRejectedJustifications(): Observable<JustificationForTc[]> {
        return this.http.get<JustificationForTc[]>(`${this.formationOfTcUrl}` + 'getAllRejectedJustifications')
    }

}
