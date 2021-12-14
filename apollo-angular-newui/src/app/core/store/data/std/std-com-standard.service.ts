import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpParams} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {
  ApproveDraft,
  ApproveJC, ApproveSACJC,
  ComHodTasks,
  ComJcJustification, ComJcJustificationAction, ComJcJustificationDec,
  ComJcJustificationList, CompanyStandardRequest, COMPreliminaryDraft, ComStandardJC,
  ComStdAction,
  Department, NWAPreliminaryDraft, NWAWDDecision, NWAWorkShopDraft, Product,
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
  public formJointCommittee(comStandardJC: ComStandardJC): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_FORM_JOINT_COMMITTEE);
    const params = new HttpParams();
    return this.http.post<ComStandardJC>(url, comStandardJC, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }
  public getPlTasks(): Observable<ComHodTasks[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_PL_TASKS);
    const params = new HttpParams();
    return this.http.get<ComHodTasks[]>(url, {params}).pipe();
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

  public decisionOnJustification(approveJC: ApproveJC): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_DECISION_ON_JUSTIFICATION);
    const params = new HttpParams();
    return this.http.post<ApproveJC>(url, approveJC, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }

  public getSacSecTasks(): Observable<ComJcJustificationDec[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_SAC_SEC_TASKS);
    const params = new HttpParams();
    return this.http.get<ComJcJustificationDec[]>(url, {params}).pipe();
  }

  public decisionOnAppJustification(approveSACJC: ApproveSACJC): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_DECISION_ON_APP_JUSTIFICATION);
    const params = new HttpParams();
    return this.http.post<ApproveSACJC>(url, approveSACJC, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }



  public uploadFileDetails(comJustificationID: string, data: FormData): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_UPLOAD_JC);

    return this.http.post<any>(url, data, {
      headers: {
        'enctype': 'multipart/form-data'
      }, params: {'comJustificationID': comJustificationID}
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
  public prepareCompanyPreliminaryDraft(comPreliminaryDraft: COMPreliminaryDraft): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_PREPARE_PRELIMINARY_DRAFT);
    const params = new HttpParams();
    return this.http.post<COMPreliminaryDraft>(url, comPreliminaryDraft, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }
  //upload Draft Document
  public uploadPDFileDetails(comStdDraftID: string, data: FormData): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_UPLOAD_PD);

    return this.http.post<any>(url, data, {
      headers: {
        'enctype': 'multipart/form-data'
      }, params: {'comStdDraftID': comStdDraftID}
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

  public getJcSecTasks(): Observable<ComJcJustificationDec[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_JC_SEC_TASKS);
    const params = new HttpParams();
    return this.http.get<ComJcJustificationDec[]>(url, {params}).pipe();
  }
  public viewCompanyDraft(comDraftDocumentId: any): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_UPLOAD_DATA_VIEW_PD);
    const params = new HttpParams()
        .set('comDraftDocumentId', comDraftDocumentId);
    // return this.httpService.get<any>(`${this.baseUrl}/get/pdf/${fileName}`, { responseType: 'arraybuffer' as 'json' });
    return this.http.get<any>(url, {params, responseType: 'arraybuffer' as 'json'}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          // console.warn(`getAllFault( ${fault.message} )`);
          return throwError(fault);
        })
    );
  }


  public decisionOnDraft(approveDraft: ApproveDraft): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_DECISION_ON_DRAFT);
    const params = new HttpParams();
    return this.http.post<ApproveDraft>(url, approveDraft, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }
  public getComSecTasks(): Observable<ComJcJustificationDec[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_COM_SEC_TASKS);
    const params = new HttpParams();
    return this.http.get<ComJcJustificationDec[]>(url, {params}).pipe();
  }
  public viewCompanyStd(comStdDocumentId: any): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_UPLOAD_DATA_VIEW_STD);
    const params = new HttpParams()
        .set('comStdDocumentId', comStdDocumentId);
    // return this.httpService.get<any>(`${this.baseUrl}/get/pdf/${fileName}`, { responseType: 'arraybuffer' as 'json' });
    return this.http.get<any>(url, {params, responseType: 'arraybuffer' as 'json'}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          // console.warn(`getAllFault( ${fault.message} )`);
          return throwError(fault);
        })
    );
  }
  public companyDecisionOnDraft(approveDraft: ApproveDraft): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_COM_DECISION_ON_DRAFT);
    const params = new HttpParams();
    return this.http.post<ApproveDraft>(url, approveDraft, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }

  public prepareCompanyStandard(comJcJustificationDec: ComJcJustificationDec): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_PREPARE_COM_STANDARD);
    const params = new HttpParams();
    return this.http.post<ComJcJustificationDec>(url, comJcJustificationDec, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }
  //upload justification Document
  public uploadSDFileDetails(comStdID: string, data: FormData): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_COM_UPLOAD_SD);

    return this.http.post<any>(url, data, {
      headers: {
        'enctype': 'multipart/form-data'
      }, params: {'comStdID': comStdID}
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

  public getHopTasks(): Observable<ComJcJustificationDec[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_COM_HOP_TASKS);
    const params = new HttpParams();
    return this.http.get<ComJcJustificationDec[]>(url, {params}).pipe();
  }

  public editCompanyStandard(comJcJustificationDec: ComJcJustificationDec): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_COM_EDIT_STANDARD);
    const params = new HttpParams();
    return this.http.post<ComJcJustificationDec>(url, comJcJustificationDec, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }
  public uploadESDFileDetails(comStdID: string, data: FormData): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_COM_UPLOAD_SD_EDIT);

    return this.http.post<any>(url, data, {
      headers: {
        'enctype': 'multipart/form-data'
      }, params: {'comStdID': comStdID}
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


}
