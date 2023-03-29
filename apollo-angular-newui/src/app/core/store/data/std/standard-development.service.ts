import {Injectable} from '@angular/core';
import {ApiEndpointService} from "../../../services/endpoints/api-endpoint.service";
import {HttpClient, HttpErrorResponse, HttpParams} from "@angular/common/http";
import {ReviewApplicationTask, StandardRequest, UsersEntity} from "./std.model";
import {Observable, throwError} from "rxjs";
import {catchError, map} from "rxjs/operators";
import {
    DecisionFeedback,
    Department,
    Document,
    HOFFeedback,
    LiaisonOrganization,
    NwiItem,
    ProductCategory,
    ProductSubCategoryB,
    StandardRequestB,
    StandardTasks,
    StdJustification,
    StdJustificationDecision,
    StdSPCSECTask,
    StdTCDecision,
    StdTCSecWorkPlan,
    StdTCTask,
    Stdtsectask,
    StdtsecTaskJustification,
    StdWorkPlan,
    TechnicalCommitteeb
} from './request_std.model';
import {Vote, VoteNwiRetrieved, VoteRetrieved, VotesNwiTally} from "./commitee-model";

@Injectable({
    providedIn: 'root'
})
export class StandardDevelopmentService {
    protocol = `https://`;
    baseUrl = ApiEndpointService.DOMAIN.LOCAL_DEV
    private apiServerUrl2 = `${this.protocol}${this.baseUrl}/api/v1/migration/anonymous/standard/dropdown/`;
    private apiMembershipToTCUrl = `${this.protocol}${this.baseUrl}/api/v1/migration/membershipToTC/`;
    private apiServerUrl = `${this.protocol}${this.baseUrl}/api/v1/migration/standard/`;
    private apiServerUrl23 = `${this.protocol}${this.baseUrl}/api/v1/migration/anonymous/standard/`;

    constructor(private http: HttpClient) {
    }

    public getDepartments(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_GET_DEPARTMENT);
        const params = new HttpParams();
        return this.http.get<Department>(url, {params}).pipe();
    }

    public getTechnicalCommittee(id: bigint): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_GET_TC_COMMITTEE);
        const params = new HttpParams();
        return this.http.get<any>(url, {params}).pipe();
    }

    public addStandardRequest(standardRequest: StandardRequest): Observable<any> {

        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.REQ_STANDARD);
        const params = new HttpParams();
        return this.http.post<StandardRequest>(url, standardRequest, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    public getProducts(id: bigint): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.REQ_PRODUCTS);
        const params = new HttpParams();
        return this.http.get<any>(url, {params}).pipe();
    }

    public getProductSubcategory(id: bigint): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.REQ_PRODUCTS_SUBCATEGORY);
        const params = new HttpParams();
        return this.http.get<any>(url, {params}).pipe();
    }

    public getProductsb(id: string): any {
        return this.http.get<any>(`${this.apiServerUrl2}getProducts/${id}`)
    }

    public getDepartmentsb(): any {
        return this.http.get<Department[]>(`${this.apiServerUrl2}` + 'getDepartments')
    }

    public getTcSec(): any {
        return this.http.get<UsersEntity[]>(`${this.apiServerUrl}` + 'getAllTcSec')
    }


    public getProductSubcategoryb(id: bigint): any {
        return this.http.get<any>(`${this.apiServerUrl2}getProductCategories/${id}`)
    }

    public getTechnicalCommitteeb(id: string): any {
        return this.http.get<any>(`${this.apiServerUrl2}getTechnicalCommittee/${id}`)
    }

    public decisionOnApplications(decisionFeedback: DecisionFeedback): Observable<DecisionFeedback> {
        return this.http.post<DecisionFeedback>(`${this.apiMembershipToTCUrl}` + 'decisionOnApplicantRecommendation', decisionFeedback)
    }

    public getApplicationsForReview(): Observable<ReviewApplicationTask[]> {
        return this.http.get<ReviewApplicationTask[]>(`${this.apiMembershipToTCUrl}` + 'getApplicationsForReview')
    }

    public getHOFTasks(): Observable<StandardRequestB[]> {
        return this.http.get<StandardRequestB[]>(`${this.apiServerUrl}` + 'getAllStds')
    }

    public getTechnicalCommitteeName(id: number): Observable<string> {
        return this.http.get<string>(`${this.apiServerUrl}getTechnicalCommitteeName/${id}`)
    }

    public reviewTask(hofFeedback: HOFFeedback): Observable<any> {
        return this.http.post<HOFFeedback>(`${this.apiServerUrl}` + 'hof/review', hofFeedback)
    }

    public updateDepartmentStandardRequest(standardRequest: StandardRequest): Observable<any> {
        return this.http.post<StandardRequest>(`${this.apiServerUrl}` + 'updateDepartmentStandardRequest', standardRequest)
    }

    public getLiaisonOrganization(): any {
        return this.http.get<LiaisonOrganization[]>(`${this.apiServerUrl}` + 'getLiaisonOrganizations')

    }

    public getTCSECTasks(): Observable<StandardRequestB[]> {
        return this.http.get<StandardRequestB[]>(`${this.apiServerUrl}` + 'getAllStdsForNwi')
    }

    public getAllNwisForVoting(): Observable<Stdtsectask[]> {
        return this.http.get<Stdtsectask[]>(`${this.apiServerUrl}` + 'getAllNwis')
    }


    //retrieve vote for logged in user
    public retrieveVote(): Observable<any> {
        return this.http.get<VoteRetrieved>(`${this.apiServerUrl}` + 'getUserLoggedInBallots')

    }

    //get Votes Analysis
    public getAllVotesTally(): Observable<any> {
        const url = `${this.apiServerUrl}getAllVotesTally`;

        return this.http.get<VotesNwiTally>(url).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    //get Specific Nwi

    public getNwiById(nwiId: string): Observable<any> {

        const url = `${this.apiServerUrl}getANwiById`;
        const params = new HttpParams().set('nwiId', nwiId)
        return this.http.get<NwiItem>(url, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    public getDepartmentById(departmentId: string): any {
        const url = `${this.apiServerUrl}getDepartmentById`;
        const params = new HttpParams().set('departmentId', departmentId)
        return this.http.get<Department>(url, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );

    }

    public getJustificationByNwiId(nwiId: string): Observable<any> {
        const url = `${this.apiServerUrl}getJustificationByNwiId`;
        const params = new HttpParams().set('nwiId', nwiId)
        return this.http.get<StdJustification>(url, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    public getJustificationDecisionById(justificationId: string): Observable<any> {

        const url = `${this.apiServerUrl}getJustificationDecisionById`;
        const params = new HttpParams().set('justificationId', justificationId)
        return this.http.get<StdJustificationDecision>(url, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    public getRequestById(requestId: string): Observable<StandardRequestB[]> {
        const url = `${this.apiServerUrl}getRequestById`;
        const params = new HttpParams().set('requestId', requestId)
        return this.http.get<StandardRequestB>(url, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }


    public getApprovedNwiS(): Observable<any> {

        const url = `${this.apiServerUrl}getAllApprovedNwiS`;
        return this.http.get<NwiItem>(url).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    public getAllNwiSApprovedForJustification(): Observable<any> {

        const url = `${this.apiServerUrl}getAllNwiSApprovedForJustification`;
        return this.http.get<NwiItem>(url).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    public getRejectedNwiS(): Observable<any> {

        const url = `${this.apiServerUrl}getAllRejectedNwiS`;
        return this.http.get<NwiItem>(url).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    //get All Votes On New Work Item
    public getAllVotesOnNwi(nwiId: any): Observable<any> {
        const url = `${this.apiServerUrl}getAllVotesOnNwi`;
        const params = new HttpParams()
            .set('nwiId', nwiId)
        return this.http.get<VoteNwiRetrieved>(url, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }


    public approveNwi(nwiId: string): Observable<any> {
        const url = `${this.apiServerUrl}approveNwi`;
        return this.http.post<any>(url, nwiId, {
            params: {'nwiId': nwiId}
        }).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    public rejectNwi(nwiId: string): Observable<any> {
        const url = `${this.apiServerUrl}rejectNwi`;
        return this.http.post<any>(url, nwiId, {
            params: {'nwiId': nwiId}
        }).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    public getRejectedReviewsForStandards(): Observable<StandardRequestB[]> {
        return this.http.get<StandardRequestB[]>(`${this.apiServerUrl}` + 'getAllRejectedStdsForNwi')
    }

    public getOnHoldReviewsForStandards(): Observable<StandardRequestB[]> {
        return this.http.get<StandardRequestB[]>(`${this.apiServerUrl}` + 'getAllOnHoldStdsForNwi')
    }

    public uploadNWI(uploadNWI: Stdtsectask): Observable<any> {

        return this.http.post<Stdtsectask>(`${this.apiServerUrl}` + 'uploadNWI', uploadNWI)
    }

    //upload additional info Document Nwi
    public uploadFileDetailsNwi(draftStandardID: string, data: FormData, doctype: string, nomineeName: string): Observable<any> {
        const url = `${this.apiServerUrl}uploadNWIDocs`;

        return this.http.post<any>(url, data, {
            headers: {
                'enctype': 'multipart/form-data'
            }, params: {'nwiId': draftStandardID, 'type': doctype, 'docDescription': nomineeName}
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

    //upload additional info Document Justification
    public uploadFileDetailsJustification(justificationId: string, data: FormData, doctype: string, nomineeName: string): Observable<any> {
        const url = `${this.apiServerUrl}uploadJustificationDocs`;

        return this.http.post<any>(url, data, {
            headers: {
                'enctype': 'multipart/form-data'
            }, params: {'justificationId': justificationId, 'type': doctype, 'docDescription': nomineeName}
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


    public getTCTasks(): Observable<StdTCTask[]> {
        return this.http.get<StdTCTask[]>(`${this.apiServerUrl}` + 'getTCTasks')
    }


    public decisionOnNWI(vote: StdTCDecision): Observable<any> {
        const url = `${this.apiServerUrl}` + 'decisionOnNWI';

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

    public uploadJustification(stdJustification: StdJustification): Observable<any> {
        return this.http.post<Stdtsectask>(`${this.apiServerUrl}` + 'uploadJustification', stdJustification)
    }

    public getTCSECTasksJustification(): Observable<StdtsecTaskJustification[]> {
        return this.http.get<StdtsecTaskJustification[]>(`${this.apiServerUrl}` + 'tc-sec/tasks')
    }

    public getJustificationsPendingDecision(): Observable<StdJustification[]> {
        return this.http.get<StdJustification[]>(`${this.apiServerUrl}` + 'getJustificationsPendingDecision')
    }


    public getApprovedJustifications(): Observable<StdJustification[]> {
        return this.http.get<StdJustification[]>(`${this.apiServerUrl}` + 'getApprovedJustifications')
    }

    public getRejectedJustifications(): Observable<StdJustification[]> {
        return this.http.get<StdJustification[]>(`${this.apiServerUrl}` + 'getRejectedJustifications')
    }

    public getRejectedAmendmentJustifications(): Observable<StdJustification[]> {
        return this.http.get<StdJustification[]>(`${this.apiServerUrl}` + 'getRejectedAmendmentJustifications')
    }


    public getSPCSECTasks(): Observable<StdSPCSECTask[]> {
        return this.http.get<StdSPCSECTask[]>(`${this.apiServerUrl}` + 'spc-sec/tasks')
    }

    public decisionOnJustification(stdJustificationDecision: StdJustificationDecision): Observable<any> {
        return this.http.post<StandardTasks>(`${this.apiServerUrl}` + 'decisionOnJustification', stdJustificationDecision)
    }

    public getTCSECWorkPlan(): Observable<StdTCSecWorkPlan[]> {
        return this.http.get<StdTCSecWorkPlan[]>(`${this.apiServerUrl}` + 'getTCSECTasks')
    }

    public uploadWorkPlan(stdWorkPlan: StdWorkPlan): Observable<any> {

        console.log(stdWorkPlan);
        return this.http.post<StdWorkPlan>(`${this.apiServerUrl}` + 'uploadWorkPlan', stdWorkPlan)
    }

    public createDepartment(department: Department): Observable<any> {
        return this.http.post<Department>(`${this.apiServerUrl}` + 'createDepartment', department).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );


    }

    public createTechnicalCommittee(technicalCommitteeb: TechnicalCommitteeb): Observable<any> {
        return this.http.post<Department>(`${this.apiServerUrl}` + 'createTechnicalCommittee', technicalCommitteeb).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );


    }

    public createProductCategory(productCategory: ProductCategory): Observable<any> {
        return this.http.post<ProductCategory>(`${this.apiServerUrl}` + 'createProductCategory', productCategory).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );


    }

    public createProductSubCategory(productSubCategory: ProductSubCategoryB): Observable<any> {
        return this.http.post<ProductSubCategoryB>(`${this.apiServerUrl}` + 'createProductSubCategory', productSubCategory).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );


    }

    public getAllDepartments(): any {
        return this.http.get<Department[]>(`${this.apiServerUrl}` + 'getAllDepartments')
    }

    public getTechnicalCommittees(): any {
        return this.http.get<Department[]>(`${this.apiServerUrl}` + 'getAllTcs')
    }

    public getAllTcsForApplication(): any {
        return this.http.get<Department[]>(`${this.apiServerUrl23}` + 'getAllTcsForApplication')
    }


    public getProductCategories(): any {
        return this.http.get<Department[]>(`${this.apiServerUrl}` + 'getAllProductCategories')
    }

    public getProductSubCategories(): any {
        return this.http.get<Department[]>(`${this.apiServerUrl}` + 'getAllProductSubCategories')
    }

    public updateDepartment(department: Department): Observable<any> {
        return this.http.post<Department>(`${this.apiServerUrl}` + 'updateDepartment', department).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );


    }

    //upload additinal info Document
    public uploadFileDetails(draftStandardID: string, data: FormData, doctype: string, nomineeName: string): Observable<any> {
        const url = `${this.apiServerUrl23}file-upload`;

        return this.http.post<any>(url, data, {
            headers: {
                'enctype': 'multipart/form-data'
            }, params: {'requestId': draftStandardID, 'type': doctype, 'requesterName': nomineeName}
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

    public getAdditionalDocuments(standardId: string): Observable<any> {

        const url = `${this.apiServerUrl}getAdditionalDocuments`;
        const params = new HttpParams().set('standardId', standardId)
        return this.http.get<Document>(url, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    public getAdditionalDocumentsByProcess(standardId: string, processName: string): Observable<any> {

        const url = `${this.apiServerUrl}getAdditionalDocumentsByProcess`;
        const params = new HttpParams()
            .set('standardId', standardId)
            .set('processName', processName)

        return this.http.get<Document>(url, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }
}

