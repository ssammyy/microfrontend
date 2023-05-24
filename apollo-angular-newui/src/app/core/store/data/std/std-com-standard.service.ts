import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpParams} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {
  ApproveDraft,
  ApproveJC,
  ApproveSACJC, ComDraftComment,
  ComHodTasks,
  ComJcJustification,
  ComJcJustificationAction,
  ComJcJustificationDec,
  ComJcJustificationList,
  CompanyStandardRequest,
  CompanyStdRemarks,
  COMPreliminaryDraft,
  ComStandard,
  ComStandardJC,
  ComStdAction, ComStdCommitteeRemarks, ComStdContactFields, ComStdDraftEdit, ComStdRemarks,
  ComStdRequest, ComStdRequestFields,
  Department, InternationalStandardsComments,
  ISCheckRequirements, ISDraftDecision, IStandardDraftEdit, IStandardUpload,
  NWAPreliminaryDraft,
  NWAWDDecision,
  NWAWorkShopDraft, PredefinedSdIntCommentsFields,
  Product, ProposalComment,
  StakeholderProposalComments, UserEntity,
  UsersEntity
} from './std.model';
import {ApiEndpointService} from '../../../services/endpoints/api-endpoint.service';
import {catchError, map} from 'rxjs/operators';
import {CompanyContactDetails, DocumentDTO, JCList, SiteVisitRemarks} from "../levy/levy.model";
import {FormGroup} from "@angular/forms";

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
  public getUserList(): Observable<UserEntity[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_GET_USERS);
    const params = new HttpParams();
    return this.http.get<UserEntity[]>(url, {params}).pipe();
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

  public addStandardRequest(companyStandardRequest: ComStdRequestFields, comStdContactFields: ComStdContactFields[]): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_ADD_STD_REQUEST);
    const params = new HttpParams();
    companyStandardRequest.contactDetails=comStdContactFields
    return this.http.post<ComStdRequestFields>(url, companyStandardRequest, {params}).pipe(
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

  public formJCommittee(comStandardJC: ComStandardJC,valueString: string[]): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_FORM_JOINT_COMMITTEE);
    const params = new HttpParams();
    comStandardJC.name=valueString
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

  public getAllStandards(): Observable<ComStandard[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_STANDARDS);
    const params = new HttpParams();
    return this.http.get<ComStandard[]>(url, {params}).pipe();
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

  public uploadCommitmentLetter(comStdRequestID: string, data: FormData): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_UPLOAD_COMMITMENT_LETTER);

    return this.http.post<any>(url, data, {
      headers: {
        'enctype': 'multipart/form-data'
      }, params: {'comStdRequestID': comStdRequestID}
    }).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }

  public viewCommitmentLetter(comStdRequestID: any): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_VIEW_COMMITMENT_LETTER);
    const params = new HttpParams().set('comStdRequestID', comStdRequestID);
    return this.http.get<any>(url, {params, responseType: 'arraybuffer' as 'json'}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
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


  public uploadCoverPages(comStdDraftID: string, data: FormData): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_UPLOAD_CP);

    return this.http.post<any>(url, data, {
      headers: {
        'enctype': 'multipart/form-data'
      }, params: {'comStdDraftID': comStdDraftID}
    }).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }

  //upload Draft Document
  public uploadCompanyStandard(standardID: string, data: FormData): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_UPLOAD_COM_STANDARD);

    return this.http.post<any>(url, data, {
      headers: {
        'enctype': 'multipart/form-data'
      }, params: {'standardID': standardID}
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

  public viewCompanyDraft(comStdDraftID: any): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_UPLOAD_DATA_VIEW_PD);
    const params = new HttpParams()
        .set('comStdDraftID', comStdDraftID);
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

  public viewCoverPages(comStdDraftID: any): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_VIEW_COVER_PAGES);
    const params = new HttpParams()
        .set('comStdDraftID', comStdDraftID);
    return this.http.get<any>(url, {params, responseType: 'arraybuffer' as 'json'}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }

  public getDraftDocumentList(comStdDraftID: any): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_COM_STD_DRAFT_DOCUMENT_LIST);
    const params = new HttpParams().set('comStdDraftID', comStdDraftID);
    return this.http.get<DocumentDTO[]>(url, {params}).pipe();
  }

  public getCoverPagesList(comStdDraftID: any): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_STD_SAC_DOCUMENT_LIST);
    const params = new HttpParams().set('comStdDraftID', comStdDraftID);
    return this.http.get<DocumentDTO[]>(url, {params}).pipe();
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

  public decisionOnComStdDraft(approveDraft: ApproveDraft): Observable<any> {
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

  public getComSecTasks(): Observable<ComJcJustificationDec[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_COM_SEC_TASKS);
    const params = new HttpParams();
    return this.http.get<ComJcJustificationDec[]>(url, {params}).pipe();
  }
  public viewCompanyStd(comStdDocumentId: any): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_UPLOAD_DATA_VIEW_STD);
    const params = new HttpParams()
        .set('comStdDocumentId', comStdDocumentId);
    return this.http.get<any>(url, {params, responseType: 'arraybuffer' as 'json'}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
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

  public prepareCompanyStandard(comPreliminaryDraft: COMPreliminaryDraft): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_PREPARE_COM_STANDARD);
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
  //upload justification Document
  public uploadSDFileDetails(comStandardID: string, data: FormData): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_COM_UPLOAD_SD);

    return this.http.post<any>(url, data, {
      headers: {
        'enctype': 'multipart/form-data'
      }, params: {'comStandardID': comStandardID}
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

  public getUserTasks(): Observable<ComJcJustificationDec[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_COM_USER_TASKS);
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

  public getComStandardRemarks(approvalID: any): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_COM_REMARKS);
    const params = new HttpParams().set('approvalID', approvalID);
    return this.http.get<CompanyStdRemarks>(url, {params}).pipe();
  }

  public getCompanyStandardRequest(): Observable<ComStdRequest[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_COM_STD_REQUEST);
    const params = new HttpParams();
    return this.http.get<ComStdRequest[]>(url, {params}).pipe();
  }
  public getCompanyStandardRequestProcess(): Observable<ComStdRequest[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_COM_STD_REQUEST_PROCESS);
    const params = new HttpParams();
    return this.http.get<ComStdRequest[]>(url, {params}).pipe();
  }

  public getUploadedStdDraft(): Observable<COMPreliminaryDraft[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_COM_STD_DRAFT);
    const params = new HttpParams();
    return this.http.get<COMPreliminaryDraft[]>(url, {params}).pipe();
  }

  public getApprovedStdDraft(comDraftID: any): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_COM_STD_APPROVED_DRAFT);
    const params = new HttpParams().set('comDraftID', comDraftID);
    return this.http.get<COMPreliminaryDraft[]>(url, {params}).pipe();
  }


  // public getUploadedStdDraftForComment(comDraftID): Observable<COMPreliminaryDraft[]> {
  //   const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_COM_STD_DRAFT_COMMENT);
  //   const params = new HttpParams();
  //   return this.http.get<COMPreliminaryDraft[]>(url, {params}).pipe();
  // }

  public getUploadedSDraftForComment(comDraftID: any): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_COM_STD_DRAFTS_COMMENT);
    const params = new HttpParams().set('comDraftID', comDraftID);
    return this.http.get<COMPreliminaryDraft[]>(url, {params}).pipe();
  }

  public getUploadedStdDraftForComment(comDraftID: any): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_COM_STD_DRAFT_COMMENT);
    const params = new HttpParams().set('comDraftID', comDraftID);
    return this.http.get<COMPreliminaryDraft[]>(url, {params}).pipe();
  }

  public commentOnDraft(approveDraft: ApproveDraft): Observable<any> {
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

  public submitDraftComment(comDraftComment: PredefinedSdIntCommentsFields[]): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_SUBMIT_DRAFT_COMMENT);
    const params = new HttpParams();
    return this.http.post<PredefinedSdIntCommentsFields[]>(url, comDraftComment, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }
  public submitDraftComments(comDraftComment: PredefinedSdIntCommentsFields[]): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_SUBMIT_DRAFT_COMMENTS);
    const params = new HttpParams();
    return this.http.post<PredefinedSdIntCommentsFields[]>(url, comDraftComment, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }
  public getStdDraftForEditing(): Observable<COMPreliminaryDraft[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_COM_STD_EDITS_DRAFT);
    const params = new HttpParams();
    return this.http.get<COMPreliminaryDraft[]>(url, {params}).pipe();
  }



  public getStdDraftEditing(): any {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_COM_STD_EDITS_DRAFT);
    const params = new HttpParams();
    return this.http.get<ISCheckRequirements>(url, {params}).pipe();
  }

  public getComPublishingTasks(): any {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_COM_STD_PB_TASKS);
    const params = new HttpParams();
    return this.http.get<ISCheckRequirements>(url, {params}).pipe();
  }

  public getAppStdPublishing(): any {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_COM_STD_SPB_TASKS);
    const params = new HttpParams();
    return this.http.get<ISCheckRequirements>(url, {params}).pipe();
  }

  public getStdApproval(): any {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_COM_STD_NSC_TASKS);
    const params = new HttpParams();
    return this.http.get<ISCheckRequirements>(url, {params}).pipe();
  }

  public getAppStd(): any {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_COM_STD_APB_TASKS);
    const params = new HttpParams();
    return this.http.get<ISCheckRequirements>(url, {params}).pipe();
  }

  public getAllComments(requestId: any): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_COM_STD_COMMENTS);
    const params = new HttpParams().set('requestId', requestId);
    return this.http.get<ComStdRemarks>(url, {params}).pipe();
  }



  public getDraftComments(draftID: any): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_COM_DRAFT_COMMENTS);
    const params = new HttpParams().set('draftID', draftID);
    return this.http.get<ComStdCommitteeRemarks>(url, {params}).pipe();
  }

  public getDraftCommentList(draftID: any): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_COM_DRAFT_COMMENTS_LIST);
    const params = new HttpParams().set('draftID', draftID);
    return this.http.get<DocumentDTO[]>(url, {params}).pipe();
  }

  public getCompanyContactDetails(requestId: any): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_CONTACT_DETAILS);
    const params = new HttpParams().set('requestId', requestId);
    return this.http.get<CompanyContactDetails[]>(url, {params}).pipe();
  }
  public getCommitteeList(requestId: any): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_COMMITTEE_LIST);
    const params = new HttpParams().set('requestId', requestId);
    return this.http.get<JCList[]>(url, {params}).pipe();
  }

  public checkRequirements(isDraftDecision: ISDraftDecision): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_HOP_DECISION_ON_JUSTIFICATION);
    const params = new HttpParams();
    return this.http.post<ISDraftDecision>(url, isDraftDecision, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }



  public submitDraftForEditing(iStandardUpload: IStandardUpload): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_COM_STD_SUBMIT_DRAFT);
    const params = new HttpParams();
    return this.http.post<IStandardUpload>(url, iStandardUpload, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }

  public editStandardDraft(comStdDraftEdit: ComStdDraftEdit): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_EDIT_APPROVED_DRAFT_STANDARD);
    const params = new HttpParams();
    return this.http.post<ComStdDraftEdit>(url, comStdDraftEdit, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }
  public assignProofReader(comStdDraftEdit: ComStdDraftEdit): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_ASSIGNED_APPROVED_DRAFT_STANDARD);
    const params = new HttpParams();
    return this.http.post<ComStdDraftEdit>(url, comStdDraftEdit, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }


  public draughtStandard(comStdDraftEdit: ComStdDraftEdit): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_UPLOAD_DRAFTING_STANDARD);
    const params = new HttpParams();
    return this.http.post<ComStdDraftEdit>(url, comStdDraftEdit, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }

  public proofReadStandard(comStdDraftEdit: ComStdDraftEdit): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_UPLOAD_PROOFREADING_STANDARD);
    const params = new HttpParams();
    return this.http.post<ComStdDraftEdit>(url, comStdDraftEdit, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }

  public approveProofReadStandard(isDraftDecision: ISDraftDecision): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_DECISION_PROOFREAD_STANDARD);
    const params = new HttpParams();
    return this.http.post<ISDraftDecision>(url, isDraftDecision, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }

  public approveEditedDraft(isDraftDecision: ISDraftDecision): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_DECISION_EDITED_STANDARD);
    const params = new HttpParams();
    return this.http.post<ISDraftDecision>(url, isDraftDecision, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }

  public rejectCompanyStandard(isDraftDecision: ISDraftDecision): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_REJECT_COM_STANDARD);
    const params = new HttpParams();
    return this.http.post<ISDraftDecision>(url, isDraftDecision, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }

  public getApproveLevelThree(): Observable<UsersEntity[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.STD_LEVY_APPROVED_USERS_LEVEL_THREE);
    const params = new HttpParams();
    return this.http.get<UsersEntity[]>(url, {params}).pipe();
  }

  public getStdRequirements(): any {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_REQUIREMENTS_LIST);
    const params = new HttpParams();
    return this.http.get<ISCheckRequirements>(url, {params}).pipe();
  }

  public getStdEditing(): any {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_STD_EDITING_LIST);
    const params = new HttpParams();
    return this.http.get<ISCheckRequirements>(url, {params}).pipe();
  }

  public getStdEditDrafting(): any {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_STD_DRAFTING_LIST);
    const params = new HttpParams();
    return this.http.get<ISCheckRequirements>(url, {params}).pipe();
  }

  public getStdEditProofreading(): any {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_STD_PROOF_READING_LIST);
    const params = new HttpParams();
    return this.http.get<ISCheckRequirements>(url, {params}).pipe();
  }

  public getStdForApproval(): any {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_STD_APPROVAL_LIST);
    const params = new HttpParams();
    return this.http.get<ISCheckRequirements>(url, {params}).pipe();
  }

  public getStdApproved(): any {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_STD_APPROVED_LIST);
    const params = new HttpParams();
    return this.http.get<ISCheckRequirements>(url, {params}).pipe();
  }



}
