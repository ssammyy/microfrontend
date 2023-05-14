import { Injectable } from '@angular/core';
import {
    AuthoritiesEntityDtos,
    ComDraftComment,
    ComJcJustificationDec, CommentOnProposalStakeHolder, ComStdRemarks, EditProposalComment,
    GazetteNotice,
    InternationalStandardsComments, InterNationalStdDecision,
    ISAdoptionComments,
    ISAdoptionJustification,
    ISAdoptionProposal, ISCheckRequirements, ISComments,
    ISDecision, ISDraftDecision, ISDraftDecisionStd,
    ISDraftUpload,
    ISHopTASKS,
    ISHosSicTASKS, ISJustification,
    ISJustificationDecision,
    ISJustificationProposal, ISProposalJustification,
    ISSacSecTASKS,
    ISStandard, IStandardDraftEdit, IStandardUpload,
    ISTcSecTASKS, IstProposalComment, JustificationStatus,
    ListJustification, MultipleApprovalFields, MultipleSpcApprovals, NwaRequestList,
    NWAStandard, PredefinedSDCommentsFields,
    ProposalComment,
    ProposalComments, RolesEntityDtos, SacDecision, SpcDecision,
    StakeholderProposalComments, StakeHoldersFields, StandardBody, ThisProposalComment, UsersEntity
} from "./std.model";
import {Observable, throwError} from "rxjs";
import {ApiEndpointService} from "../../../services/endpoints/api-endpoint.service";
import {HttpClient, HttpErrorResponse, HttpParams} from "@angular/common/http";
import {catchError, map} from "rxjs/operators";
import {DefaulterDetails, DocumentDTO, ManufacturingStatus, SiteVisitRemarks} from "../levy/levy.model";
import swal from "sweetalert2";
import Swal from "sweetalert2";

@Injectable({
  providedIn: 'root'
})
export class StdIntStandardService {

  constructor(private http: HttpClient) { }

    public findStandardStakeholders(): Observable<UsersEntity[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_GET_STD_STAKE_HOLDERS);
        const params = new HttpParams();
        return this.http.get<UsersEntity[]>(url, {params}).pipe();
    }

    public getEditorDetails(): Observable<UsersEntity[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_GET_STD_EDITORS);
        const params = new HttpParams();
        return this.http.get<UsersEntity[]>(url, {params}).pipe();
    }

    public getDraughtsManDetails(): Observable<UsersEntity[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_GET_STD_DRAUGHTSMANS);
        const params = new HttpParams();
        return this.http.get<UsersEntity[]>(url, {params}).pipe();
    }

    public getProofReaderDetails(): Observable<UsersEntity[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_GET_STD_PROOFREADERS);
        const params = new HttpParams();
        return this.http.get<UsersEntity[]>(url, {params}).pipe();
    }




    public getIntStandardProposals(): Observable<NwaRequestList[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_VIEW_ADOPTION_PROPOSAL_REQUEST);
        const params = new HttpParams();
        return this.http.get<NwaRequestList[]>(url, {params}).pipe();
    }

  public prepareAdoptionProposal(iSAdoptionProposal: ISAdoptionProposal,stakeHoldersFields: StakeHoldersFields[]): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_PREPARE_ADOPTION_PROPOSAL);
    const params = new HttpParams();
      iSAdoptionProposal.addStakeholdersList=stakeHoldersFields
    //iSAdoptionProposal.addStakeholdersList=valueString
    return this.http.post<ISAdoptionProposal>(url, iSAdoptionProposal, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }

    public onClickMakeSacDecision(sacDecision: SacDecision,multipleApprovalFields: MultipleApprovalFields[]): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_APPROVE_MULTIPLE_SAC_LIST);
        const params = new HttpParams();
        sacDecision.decisionList=multipleApprovalFields
        return this.http.post<SacDecision>(url, sacDecision, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    public onClickMakeNscDecision(sacDecision: SacDecision,multipleApprovalFields: MultipleApprovalFields[]): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_APPROVE_MULTIPLE_NSC_LIST);
        const params = new HttpParams();
        sacDecision.decisionList=multipleApprovalFields
        return this.http.post<SacDecision>(url, sacDecision, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    public onClickMakeSpcDecision(spcDecision: SpcDecision,multipleSpcApprovals: MultipleSpcApprovals[]): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_APPROVE_MULTIPLE_JUSTIFICATIONS);
        const params = new HttpParams();
        spcDecision.decisionList=multipleSpcApprovals
        return this.http.post<SpcDecision>(url, spcDecision, {params}).pipe(
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
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_UPLOAD_PD);

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

    public uploadFileDetails(isProposalID: string, data: FormData): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_UPLOAD_DOCUMENT);

        return this.http.post<any>(url, data, {
            headers: {
                'enctype': 'multipart/form-data'
            }, params: {'isProposalID': isProposalID}
        }).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    public getProposal(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_VIEW_IS_PROPOSAL);
        const params = new HttpParams();
        return this.http.get<ISAdoptionProposal>(url, {params}).pipe();
    }

    public getDraftComment(requestId: any): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_COM_STD_COMMENTS);
        const params = new HttpParams().set('requestId', requestId);
        return this.http.get<ComStdRemarks>(url, {params}).pipe();
    }


    public getSessionProposals(): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_VIEW_PROPOSAL);
        const params = new HttpParams();
        return this.http.get<ISAdoptionProposal>(url, {params}).pipe();
    }

    public getProposalsT(proposalId: any,commentId:any): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_VIEW_PROPOSALS);

        return this.http.get<ISAdoptionProposal>(url, {
             params: {'proposalId': proposalId,'commentId':commentId}
        }).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    public getProposals(proposalId: any,commentId:any): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_VIEW_PROPOSALS);
        //const params = new HttpParams().set('proposalId', proposalId);
        const urlAndPathVariables = `${url}/${proposalId}/${commentId}`;
        return this.http.get<ISAdoptionProposal>(urlAndPathVariables).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
        //return this.http.get<ISAdoptionProposal>(url, {params}).pipe();
    }

    public getApprovedProposals(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_APPROVED_PROPOSAL);
        const params = new HttpParams();
        return this.http.get<ISAdoptionProposal>(url, {params}).pipe();
    }


    public getWebProposals(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_VIEW_WEB_PROPOSALS);
        const params = new HttpParams();
        return this.http.get<ISAdoptionProposal>(url, {params}).pipe();
    }


    public getJustification(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_APP_JUSTIFICATION);
        const params = new HttpParams();
        return this.http.get<ISJustification>(url, {params}).pipe();
    }

    public getApprovedJustification(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_APPR_JUSTIFICATION);
        const params = new HttpParams();
        return this.http.get<ISAdoptionProposal>(url, {params}).pipe();
    }

    public getISJustification(draftId: any): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_VIEW_JUSTIFICATION);
        const params = new HttpParams().set('draftId', draftId);
        return this.http.get<ISJustificationProposal>(url, {params}).pipe();
    }
    public getProposalComments(id: any): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_VIEW_PROPOSAL_COMMENTS);
        const params = new HttpParams().set('id', id);
        return this.http.get<ISComments>(url, {params}).pipe();
    }
    public getApprovedISJustification(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_VIEW_APP_JUSTIFICATION);
        const params = new HttpParams();
        return this.http.get<ISJustificationProposal>(url, {params}).pipe();
    }

    public getUploadedDraft(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_VIEW_DRAFT_STANDARD);
        const params = new HttpParams();
        return this.http.get<ISCheckRequirements>(url, {params}).pipe();
    }

    public getIsPublishingTasks(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_VIEW_PB_STANDARD);
        const params = new HttpParams();
        return this.http.get<ISCheckRequirements>(url, {params}).pipe();
    }

    public getApprovedDraft(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_VIEW_APPROVED_DRAFT_STANDARD);
        const params = new HttpParams();
        return this.http.get<ISCheckRequirements>(url, {params}).pipe();
    }

    public editStandardDraft(iStandardDraftEdit: IStandardDraftEdit): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_EDIT_APPROVED_DRAFT_STANDARD);
        const params = new HttpParams();
        return this.http.post<IStandardDraftEdit>(url, iStandardDraftEdit, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    public getEditedDraft(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_VIEW_EDITED_DRAFT_STANDARD);
        const params = new HttpParams();
        return this.http.get<ISCheckRequirements>(url, {params}).pipe();
    }

    public draughtStandard(iStandardDraftEdit: IStandardDraftEdit): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_UPLOAD_DRAFTING_STANDARD);
        const params = new HttpParams();
        return this.http.post<IStandardDraftEdit>(url, iStandardDraftEdit, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    public getDraughtedDraft(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_VIEW_DRAUGHTED_STANDARD);
        const params = new HttpParams();
        return this.http.get<ISCheckRequirements>(url, {params}).pipe();
    }

    public proofReadStandard(iStandardDraftEdit: IStandardDraftEdit): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_UPLOAD_PROOFREADING_STANDARD);
        const params = new HttpParams();
        return this.http.post<IStandardDraftEdit>(url, iStandardDraftEdit, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    public getProofReadDraft(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_VIEW_PROOFREAD_STANDARD);
        const params = new HttpParams();
        return this.http.get<ISCheckRequirements>(url, {params}).pipe();
    }

    public approveProofReadLevel(isDraftDecision: ISDraftDecision): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_APPROVE_DRAFT_STANDARD);
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


    public getApprovedProofReadDraft(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_VIEW_EDITED_STANDARD);
        const params = new HttpParams();
        return this.http.get<ISCheckRequirements>(url, {params}).pipe();
    }

    public approveEditedStandard(isDraftDecision: ISDraftDecision): Observable<any> {
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

    public getApprovedEditedDraft(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_VIEW_EDITED_STANDARD_DRAFT);
        const params = new HttpParams();
        return this.http.get<ISCheckRequirements>(url, {params}).pipe();
    }

    public approveInternationalStandard(isDraftDecisionStd: ISDraftDecisionStd): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_APPROVE_EDITED_STANDARD_DRAFT);
        const params = new HttpParams();
        return this.http.post<ISDraftDecisionStd>(url, isDraftDecisionStd, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }
    public approveInternationalStandardNSC(isDraftDecisionStd: ISDraftDecisionStd): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_APPROVE_NSC_STANDARD_DRAFT);
        const params = new HttpParams();
        return this.http.post<ISDraftDecisionStd>(url, isDraftDecisionStd, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }
    public uploadInternationalStandard(iStandardDraftEdit: IStandardDraftEdit): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_SUBMIT_STANDARD);
        const params = new HttpParams();
        return this.http.post<IStandardDraftEdit>(url, iStandardDraftEdit, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }


    public getStandardForGazettement(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_VIEW_STANDARD_GAZETTE);
        const params = new HttpParams();
        return this.http.get<ISCheckRequirements>(url, {params}).pipe();
    }

    public uploadGazetteNotice(gazetteNotice: GazetteNotice): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_UPLOAD_STD_GAZETTE);
        const params = new HttpParams();
        return this.http.post<GazetteNotice>(url, gazetteNotice, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    // public uploadGazetteNotice(iStandardDraftEdit: IStandardDraftEdit): Observable<any> {
    //     const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_UPLOAD_STD_GAZETTE);
    //     const params = new HttpParams();
    //     return this.http.post<IStandardDraftEdit>(url, iStandardDraftEdit, {params}).pipe(
    //         map(function (response: any) {
    //             return response;
    //         }),
    //         catchError((fault: HttpErrorResponse) => {
    //             return throwError(fault);
    //         })
    //     );
    // }


  public getISProposals(): Observable<ISAdoptionProposal[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_VIEW_IS_PROPOSALS);
    const params = new HttpParams();
    return this.http.get<ISAdoptionProposal[]>(url, {params}).pipe();
  }
  //view Proposal Doc
    public viewProposalPDF(isDocumentId: any): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_VIEW_IS_PROPOSALS_DOC);
        const params = new HttpParams()
            .set('isDocumentId', isDocumentId);
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

    public submitDraftComments(comDraftComment: ThisProposalComment): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_SUBMIT_DRAFT_COMMENTS);
        const params = new HttpParams();
        return this.http.post<ThisProposalComment>(url, comDraftComment, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    public submitWebsiteComments(comDraftComment: ThisProposalComment): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_SUBMIT_WEB_COMMENTS);
        const params = new HttpParams();
        return this.http.post<ThisProposalComment>(url, comDraftComment, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    public editSubmitDraftComment(comDraftComment: EditProposalComment): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_EDIT_DRAFT_COMMENT);
        const params = new HttpParams();
        return this.http.post<EditProposalComment>(url, comDraftComment, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    public submitDraftComment(comDraftComment: ThisProposalComment): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_SUBMIT_DRAFT_COMMENT);
        const params = new HttpParams();
        return this.http.post<ThisProposalComment>(url, comDraftComment, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    showWarning(message: string, fn?: Function) {
        swal.fire({
            title: message,
            buttonsStyling: false,
            customClass: {
                confirmButton: 'btn btn-success form-wizard-next-btn ',
            },
            icon: 'warning',
        }).then(() => {
            if (fn) {
                fn();
            }
        });
    }
    showSuccessWith2Message(title: string, text: string, cancelMessage: string, successMessage: string, fn?: Function) {
        const swalWithBootstrapButtons = Swal.mixin({
            customClass: {
                confirmButton: 'btn btn-success',
                cancelButton: 'btn btn-danger',
            },
            buttonsStyling: false,
        });

        swalWithBootstrapButtons.fire({
            title: title,
            text: text,
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Confirm!',
            reverseButtons: true,
        }).then((result) => {
            if (result.isConfirmed) {
                if (fn) {
                    const results = fn();
                    if (results === true) {
                        swalWithBootstrapButtons.fire(
                            'Submitted!',
                            successMessage,
                            'success',
                        );
                    } else if (results === false) {
                        swalWithBootstrapButtons.fire(
                            'Submitted!',
                            'AN ERROR OCCURRED',
                            'error',
                        );
                    }
                }

            } else if (
                /* Read more about handling dismissals below */
                result.dismiss === swal.DismissReason.cancel
            ) {
                swalWithBootstrapButtons.fire(
                    'Cancelled',
                    cancelMessage,
                    'error',
                );
            }
        });
    }

    public getDraftDocumentList(comStdDraftID: any): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_COM_STD_DRAFT_DOCUMENT_LIST);
        const params = new HttpParams().set('comStdDraftID', comStdDraftID);
        return this.http.get<DocumentDTO[]>(url, {params}).pipe();
    }


    public submitProposalComments(istProposalComment: IstProposalComment): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_SUBMIT_PROP_COMMENTS);
        const params = new HttpParams();
        return this.http.post<IstProposalComment>(url, istProposalComment, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

  public submitAPComments(istProposalComment: IstProposalComment): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_SUBMIT_AP_COMMENTS);
    const params = new HttpParams();
    return this.http.post<IstProposalComment>(url, istProposalComment, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }


  public getTCSECTasks(): Observable<ISTcSecTASKS[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_TC_SEC_TASKS);
    const params = new HttpParams();
    return this.http.get<ISTcSecTASKS[]>(url, {params}).pipe();
  }

  public decisionOnProposal(iSDecision: InterNationalStdDecision): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_DECISION_ON_PROPOSAL);
    const params = new HttpParams();
    return this.http.post<InterNationalStdDecision>(url, iSDecision, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }
  public prepareJustification(isProposalJustification: ISProposalJustification): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_PREPARE_JUSTIFICATION);
    const params = new HttpParams();
    return this.http.post<ISProposalJustification>(url, isProposalJustification, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }

    public editJustification(isProposalJustification: ISProposalJustification): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_EDIT_JUSTIFICATION);
        const params = new HttpParams();
        return this.http.post<ISProposalJustification>(url, isProposalJustification, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }



    public uploadJSFile(isJustificationID: string, data: FormData): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_UPLOAD_JS_DOCUMENT);

        return this.http.post<any>(url, data, {
            headers: {
                'enctype': 'multipart/form-data'
            }, params: {'isJustificationID': isJustificationID}
        }).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }
  public getSPCSECTasks(): Observable<ListJustification[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_SPC_SEC_TASKS);
    const params = new HttpParams();
    return this.http.get<ListJustification[]>(url, {params}).pipe();
  }
  //view Justification File
    public viewJustificationPDF(isJSDocumentId: any): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_VIEW_IS_JUSTIFICATION_DOC);
        const params = new HttpParams()
            .set('isJSDocumentId', isJSDocumentId);
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

  public decisionOnJustification(isJustificationDecision: ISJustificationDecision): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_DECISION_ON_JUSTIFICATION);
    const params = new HttpParams();
    return this.http.post<ISAdoptionJustification>(url, isJustificationDecision, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }
    public decisionOnSACJustification(isJustificationDecision: ISJustificationDecision): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_SAC_DECISION_ON_JUSTIFICATION);
        const params = new HttpParams();
        return this.http.post<ISAdoptionJustification>(url, isJustificationDecision, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    public decisionOnHopJustification(isDraftDecision: ISDraftDecision): Observable<any> {
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
  public getSACSECTasks(): Observable<ISSacSecTASKS[]> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_SAC_SEC_TASKS);
    const params = new HttpParams();
    return this.http.get<ISSacSecTASKS[]>(url, {params}).pipe();
  }

  public approveStandard(isJustificationDecision: ISJustificationDecision): Observable<any> {
    const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_APPROVE_STANDARD);
    const params = new HttpParams();
    return this.http.post<ISAdoptionJustification>(url, isJustificationDecision, {params}).pipe(
        map(function (response: any) {
          return response;
        }),
        catchError((fault: HttpErrorResponse) => {
          return throwError(fault);
        })
    );
  }
    public getHOPTasks(): Observable<ISHopTASKS[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_HOP_TASKS);
        const params = new HttpParams();
        return this.http.get<ISHopTASKS[]>(url, {params}).pipe();
    }
    public uploadISStandard(isStandard: ISStandard): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IS_UPLOAD_STANDARD);
        const params = new HttpParams();
        return this.http.post<ISStandard>(url, isStandard, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }
    public uploadSDFile(isStandardID: string, data: FormData): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IS_UPLOAD_STD);

        return this.http.post<any>(url, data, {
            headers: {
                'enctype': 'multipart/form-data'
            }, params: {'isStandardID': isStandardID}
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
    public getHoSiCTasks(): Observable<ISHosSicTASKS[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_HOS_SIC_TASKS);
        const params = new HttpParams();
        return this.http.get<ISHosSicTASKS[]>(url, {params}).pipe();
    }

    //view Standard File
    public viewStandardPDF(isStdDocumentId: any): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_VIEW_IS_STANDARD_DOC);
        const params = new HttpParams()
            .set('isStdDocumentId', isStdDocumentId);
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


    //view Standard File
    public viewStandardDPDF(isStandardID: any): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_VIEW_IS_STANDARD_GZT_DOC);
        const params = new HttpParams()
            .set('isStandardID', isStandardID);
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

    public uploadSDGZFile(isStandardID: string, data: FormData): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IS_UPLOAD_STD_GZT);

        return this.http.post<any>(url, data, {
            headers: {
                'enctype': 'multipart/form-data'
            }, params: {'isStandardID': isStandardID}
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
    public updateGazetteDate(gazetteNotice: GazetteNotice): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IS_UPDATE_GAZETTE_DATE);
        const params = new HttpParams();
        return this.http.post<GazetteNotice>(url, gazetteNotice, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    public getUserTasks(): Observable<ProposalComments[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_INT_TASKS);
        const params = new HttpParams();
        return this.http.get<ProposalComments[]>(url, {params}).pipe();
    }

    public getAllComments(proposalId: any): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_PROPOSAL_COMMENTS);
        const params = new HttpParams().set('proposalId', proposalId);
        return this.http.get<CommentOnProposalStakeHolder>(url, {params}).pipe();
    }
    public getUserComments(id: any): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_JUSTIFICATION_COMMENTS);
        const params = new HttpParams().set('id', id);
        return this.http.get<InternationalStandardsComments>(url, {params}).pipe();
    }

    public submitDraftForEditing(iStandardUpload: IStandardUpload): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_UPLOAD_DRAFT_STANDARD);
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


    public draughtStandardDraft(iSDraftUpload: ISDraftUpload): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_UPLOAD_DRAFTING_STANDARD);
        const params = new HttpParams();
        return this.http.post<ISDraftUpload>(url, iSDraftUpload, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }
    public proofreading(iSDraftUpload: ISDraftUpload): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_UPLOAD_PROOFREADING_STANDARD);
        const params = new HttpParams();
        return this.http.post<ISDraftUpload>(url, iSDraftUpload, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }
    public uploadIStandard(iSDraftUpload: ISDraftUpload): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_UPLOAD_STANDARD);
        const params = new HttpParams();
        return this.http.post<ISDraftUpload>(url, iSDraftUpload, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    public getCompanyStandards(): Observable<StandardBody[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_VIEW_COM_STANDARDS);
        const params = new HttpParams();
        return this.http.get<StandardBody[]>(url, {params}).pipe();
    }

    public getInternationalStandards(): Observable<StandardBody[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_VIEW_INT_STANDARDS);
        const params = new HttpParams();
        return this.http.get<StandardBody[]>(url, {params}).pipe();
    }

    public getStandards(): Observable<StandardBody[]> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_VIEW_ALL_STANDARDS);
        const params = new HttpParams();
        return this.http.get<StandardBody[]>(url, {params}).pipe();
    }

    public viewStandardFile(standardId: any): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ICT_COM_VIEW_STANDARD);
        const params = new HttpParams()
            .set('standardId', standardId);
        return this.http.get<any>(url, {params, responseType: 'arraybuffer' as 'json'}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    public getAuthorities(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.LOAD_AUTHORITIES_LIST);
        const params = new HttpParams();
        return this.http.get<AuthoritiesEntityDtos>(url, {params}).pipe();
    }

    public createAuthority(authorities: AuthoritiesEntityDtos): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ADD_AUTHORITIES);
        const params = new HttpParams();
        return this.http.post<AuthoritiesEntityDtos>(url, authorities, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    public getRoles(): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.LOAD_ROLES_LIST);
        const params = new HttpParams();
        return this.http.get<RolesEntityDtos>(url, {params}).pipe();
    }

    public createRole(roles: RolesEntityDtos): Observable<any> {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ADD_ROLES);
        const params = new HttpParams();
        return this.http.post<RolesEntityDtos>(url, roles, {params}).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                return throwError(fault);
            })
        );
    }

    assignAuthorizationToRole(roleId: bigint, privilegeId: bigint, status: number): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.ASSIGN_AUTHORIZATION_TO_ROLE);
        const urlAndPathVariables = `${url}${roleId}/${privilegeId}/${status}`;
        return this.http.post<any>(urlAndPathVariables, null).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }
    loadActivePrivileges( status: number): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.AUTHORITIES_LIST_BY_STATUS);
        const urlAndPathVariables = `${url}/${status}`;
        return this.http.get<any>(urlAndPathVariables).pipe(
            map(function (response: any) {
                return response;
            }),
            catchError((fault: HttpErrorResponse) => {
                // console.warn(`getAllFault( ${fault.message} )`);
                return throwError(fault);
            })
        );
    }

    public getJustificationStatus(draftId: any): any {
        const url = ApiEndpointService.getEndpoint(ApiEndpointService.ENDPOINT.IST_VIEW_JUSTIFICATION_STATUS);
        const params = new HttpParams().set('draftId', draftId);
        return this.http.get<JustificationStatus>(url, {params}).pipe();
    }


}
