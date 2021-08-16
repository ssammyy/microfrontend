import {Injectable} from '@angular/core';
import {ApiEndpointService} from "../../../services/endpoints/api-endpoint.service";
import {HttpClient, HttpErrorResponse, HttpParams} from "@angular/common/http";
import {Department, StandardRequest} from "./std.model";
import {Observable, throwError} from "rxjs";
import {catchError, map} from "rxjs/operators";

@Injectable({
    providedIn: 'root'
})
export class StandardDevelopmentService {
    private apiServerUrl2 = `https://localhost:8006/api/v1/migration/anonymous/standard/dropdown/`;

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
}
