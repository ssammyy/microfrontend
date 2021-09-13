import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpParams} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {
  ComHodTasks,
  ComJcJustification, ComJcJustificationAction,
  ComJcJustificationList, CompanyStandardRequest,
  ComStdAction,
  Department, Product,
  UsersEntity
} from './std.model';
import {ApiEndpointService} from '../../../services/endpoints/api-endpoint.service';
import {catchError, map} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class StdComStandardService {
  baseUrl = ApiEndpointService.DOMAIN.LOCAL_DEV;
  private apiServerUrl = `${this.baseUrl}/api/v1/migration/anonymous/standard/dropdown/`;
  private apiServerUrl2 = `${this.baseUrl}/api/v1/migration/standard/`;

  constructor(private http: HttpClient) {
  }

  public getStandards(): Observable<Product[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_GET_PRODUCTS);
    const params = new HttpParams();
    return this.http.get<Product[]>(url, {params}).pipe();
  }

  public getProducts(id: bigint): any {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_GET_PRODUCTS_LS);
    const params = new HttpParams();
    return this.http.get<any>(url, {params}).pipe();
  }
  public getDepartments(): Observable<Department[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_GET_DEPARTMENTS);
    const params = new HttpParams();
    return this.http.get<Department[]>(url, {params}).pipe();
  }
  public getUserList(): Observable<UsersEntity[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_GET_USERS);
    const params = new HttpParams();
    return this.http.get<UsersEntity[]>(url, {params}).pipe();
  }

  public getDepartment(): any {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_GET_DEPARTMENT);
    const params = new HttpParams();
    return this.http.get<Department[]>(url, {params}).pipe();
  }

  public getTechnicalCommittee(id: bigint): any {
    return this.http.get<any>(`${this.apiServerUrl}getTechnicalCommittee/${id}`);
  }

  public getProduct(id: bigint): any {
    return this.http.get<any>(`${this.apiServerUrl}getProducts/${id}`);
  }

  public getProductSubcategory(id: bigint): any {
    return this.http.get<any>(`${this.apiServerUrl}getProductCategories/${id}`);
  }

  // public getTechnicalCommittee(id: bigint): any {
  //   const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_GET_TC_COMMITTEE);
  //   const params = new HttpParams();
  //   return this.http.get<any>(url, {params}).pipe();
  // }

// public getProductSubcategory(id: bigint): any {
//     const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_GET_PRODUCT_CATEGORIES);
//     const params = new HttpParams();
//     return this.http.get<any>(url, {params}).pipe();
//   }

  public addStandardRequest(companyStandardRequest: CompanyStandardRequest): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_ADD_STD_REQUEST);
    const params = new HttpParams();
    return this.http.post<CompanyStandardRequest>(url, companyStandardRequest, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }

  public getHODTasks(): Observable<ComHodTasks[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_HOD_TASKS);
    const params = new HttpParams();
    return this.http.get<ComHodTasks[]>(url, {params}).pipe();
  }

  public assignRequest(comStdAction: ComStdAction): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_ASSIGN_REQUEST);
    const params = new HttpParams();
    return this.http.post<ComStdAction>(url, comStdAction, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }
  public getPlTasks(): Observable<ComJcJustification[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_PL_TASKS);
    const params = new HttpParams();
    return this.http.get<ComJcJustification[]>(url, {params}).pipe();
  }

  public prepareJustification(comJcJustificationAction: ComJcJustificationAction): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_PREPARE_JUSTIFICATION);
    const params = new HttpParams();
    return this.http.post<ComJcJustificationAction>(url, comJcJustificationAction, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }

  public getSpcSecTasks(): Observable<ComJcJustificationList[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_SPC_SEC_TASKS);
    const params = new HttpParams();
    return this.http.get<ComJcJustificationList[]>(url, {params}).pipe();
  }

  public decisionOnJustification(comJcJustification: ComJcJustification): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_DECISION_ON_JUSTIFICATION);
    const params = new HttpParams();
    return this.http.post<ComJcJustification>(url, comJcJustification, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }


}
