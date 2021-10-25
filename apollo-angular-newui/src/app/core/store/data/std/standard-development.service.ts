import {Injectable} from '@angular/core';
import {ApiEndpointService} from "../../../services/endpoints/api-endpoint.service";
import {HttpClient, HttpErrorResponse, HttpParams} from "@angular/common/http";
import {ReviewApplicationTask, StandardRequest} from "./std.model";
import {Observable, throwError} from "rxjs";
import {catchError, map} from "rxjs/operators";
import {
    DecisionFeedback,
    Department,
    HOFFeedback,
    LiaisonOrganization,
    StandardTasks,
    StdJustification,
    StdJustificationDecision,
    StdSPCSECTask,
    StdTCDecision,
    StdTCSecWorkPlan,
    StdTCTask,
    Stdtsectask,
    StdtsecTaskJustification,
    StdWorkPlan, TechnicalCommittee, TechnicalCommitteeb
} from './request_std.model';

@Injectable({
    providedIn: 'root'
})
export class StandardDevelopmentService {
    protocol = `https://`;
    baseUrl = ApiEndpointService.DOMAIN.LOCAL_DEV
    private apiServerUrl2 = `${this.protocol}${this.baseUrl}/api/v1/migration/anonymous/standard/dropdown/`;
    private apiMembershipToTCUrl = `${this.protocol}${this.baseUrl}/api/v1/migration/membershipToTC/`;
    private apiServerUrl = `${this.protocol}${this.baseUrl}/api/v1/migration/standard/`;

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

    public getProductsb(id: bigint): any {
        return this.http.get<any>(`${this.apiServerUrl2}getProducts/${id}`)
    }

    public getDepartmentsb(): any {
        return this.http.get<Department[]>(`${this.apiServerUrl2}` + 'getDepartments')
    }

    public getProductSubcategoryb(id: bigint): any {
        return this.http.get<any>(`${this.apiServerUrl2}getProductCategories/${id}`)
    }

    public getTechnicalCommitteeb(id: bigint): any {
        return this.http.get<any>(`${this.apiServerUrl2}getTechnicalCommittee/${id}`)
    }

    public decisionOnApplications(decisionFeedback: DecisionFeedback): Observable<DecisionFeedback> {
        console.log(decisionFeedback);
        return this.http.post<DecisionFeedback>(`${this.apiMembershipToTCUrl}` + 'decisionOnApplicantRecommendation', decisionFeedback)
    }

    public getApplicationsForReview(): Observable<ReviewApplicationTask[]> {
        return this.http.get<ReviewApplicationTask[]>(`${this.apiMembershipToTCUrl}` + 'getApplicationsForReview')
    }

    public getHOFTasks(): Observable<StandardTasks[]> {
        return this.http.get<StandardTasks[]>(`${this.apiServerUrl}` + 'getHOFTasks')
    }

    public getTechnicalCommitteeName(id: number): Observable<string> {
        return this.http.get<string>(`${this.apiServerUrl}getTechnicalCommitteeName/${id}`)
    }

    public reviewTask(hofFeedback: HOFFeedback): Observable<HOFFeedback> {
        return this.http.post<HOFFeedback>(`${this.apiServerUrl}` + 'hof/review', hofFeedback)
    }

    public getLiaisonOrganization(): any {
        return this.http.get<LiaisonOrganization[]>(`${this.apiServerUrl}` + 'getLiaisonOrganizations')

    }
    public getTCSECTasks(): Observable<Stdtsectask[]> {
        return this.http.get<Stdtsectask[]>(`${this.apiServerUrl}` + 'getTCSECTasks')
    }
    public uploadNWI(uploadNWI: Stdtsectask): Observable<Stdtsectask> {

        console.log(uploadNWI);
        return this.http.post<Stdtsectask>(`${this.apiServerUrl}` + 'uploadNWI', uploadNWI)
    }

    public getTCTasks(): Observable<StdTCTask[]> {
        return this.http.get<StdTCTask[]>(`${this.apiServerUrl}` + 'getTCTasks')
    }

    public decisionOnNWI(reviewTask: StdTCDecision): Observable<StandardTasks> {
        return this.http.post<StandardTasks>(`${this.apiServerUrl}` + 'decisionOnNWI', reviewTask)
    }

    public uploadJustification(stdJustification: StdJustification): Observable<Stdtsectask> {

        console.log(stdJustification);
        return this.http.post<Stdtsectask>(`${this.apiServerUrl}` + 'uploadJustification', stdJustification)
    }

    public getTCSECTasksJustification(): Observable<StdtsecTaskJustification[]> {
        return this.http.get<StdtsecTaskJustification[]>(`${this.apiServerUrl}` + 'getTCSECTasks')
    }

    public getSPCSECTasks(): Observable<StdSPCSECTask[]> {
        return this.http.get<StdSPCSECTask[]>(`${this.apiServerUrl}` + 'spc-sec/tasks')
    }

    public decisionOnJustification(stdJustificationDecision: StdJustificationDecision): Observable<StandardTasks> {
        return this.http.post<StandardTasks>(`${this.apiServerUrl}` + 'decisionOnJustification', stdJustificationDecision)
    }

    public getTCSECWorkPlan(): Observable<StdTCSecWorkPlan[]> {
        return this.http.get<StdTCSecWorkPlan[]>(`${this.apiServerUrl}` + 'getTCSECTasks')
    }

    public uploadWorkPlan(stdWorkPlan: StdWorkPlan): Observable<StdWorkPlan> {

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
}
