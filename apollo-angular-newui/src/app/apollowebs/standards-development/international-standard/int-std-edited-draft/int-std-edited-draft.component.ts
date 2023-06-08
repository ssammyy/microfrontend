import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {
    ComStdCommitteeRemarks,
    ComStdRemarks,
    InternationalStandardsComments,
    ISCheckRequirements,
    StakeholderProposalComments
} from "../../../../core/store/data/std/std.model";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Store} from "@ngrx/store";
import {Router} from "@angular/router";
import {StdIntStandardService} from "../../../../core/store/data/std/std-int-standard.service";
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {HttpErrorResponse} from "@angular/common/http";
import {DocumentDTO} from "../../../../core/store/data/levy/levy.model";
import {StdComStandardService} from "../../../../core/store/data/std/std-com-standard.service";
import swal from "sweetalert2";

@Component({
  selector: 'app-int-std-edited-draft',
  templateUrl: './int-std-edited-draft.component.html',
  styleUrls: ['./int-std-edited-draft.component.css']
})
export class IntStdEditedDraftComponent implements OnInit {
    @ViewChildren(DataTableDirective)
    dtElements: QueryList<DataTableDirective>;
    dtOptions: DataTables.Settings = {};
    dtTrigger: Subject<any> = new Subject<any>();
    dtTrigger1: Subject<any> = new Subject<any>();
    dtTrigger2: Subject<any> = new Subject<any>();
    comStdRemarks: ComStdRemarks[] = [];
    stakeholderProposalComments: StakeholderProposalComments[] = [];
    internationalStandardsComments: InternationalStandardsComments[] = [];
    comStdCommitteeRemarks: ComStdCommitteeRemarks[] = [];
    isCheckRequirements:ISCheckRequirements[]=[];
    public actionRequests: ISCheckRequirements | undefined;
    public actionRequest: ISCheckRequirements | undefined;
    approve: string;
    reject: string;
    isShowRemarksTabs= true;
    isShowRemarksTab= true;
    isShowCommentsTab= true;
    isShowCommentsTabs= true;
    isShowMainTab= true;
    isShowMainTabs= true;
    public uploadDraftStandardFormGroup!: FormGroup;
    public approveRequirementsFormGroup!: FormGroup;
    public rejectRequirementsFormGroup!: FormGroup;
    public editDraughtFormGroup!: FormGroup;
    public draughtFormGroup!: FormGroup;
    public proofReadFormGroup!: FormGroup;
    documentDTOs: DocumentDTO[] = [];
    fullname = '';
    blob: Blob;
    public uploadedFiles:  FileList;
    public uploadedFile:  FileList;
    public uploadDrafts:  FileList;
    public uploadProofReads:  FileList;
    public uploadStandardFile:  FileList;
    loadingText: string;
  constructor(
      private store$: Store<any>,
      private router: Router,
      private stdIntStandardService:StdIntStandardService,
      private standardDevelopmentService : StandardDevelopmentService,
      private stdComStandardService:StdComStandardService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private formBuilder: FormBuilder
  ) { }

    ngOnInit(): void {
        this.getStdApproved();
        this.approveRequirementsFormGroup = this.formBuilder.group({
            id:[],
            comments:null,
            accentTo:null,
            requestId:null,
            draftId:null,
            title:null,
            standardNumber:null,
            scope:null,
            normativeReference:null,
            symbolsAbbreviatedTerms:null,
            clause:null,
            special:null,
            uploadDate:null,
            preparedBy:null,
            documentType:null,
            departmentId:null,
            subject:null,
            description:null,
            contactOneFullName:null,
            contactOneTelephone:null,
            contactOneEmail:null,
            standardType:null,
            companyName:[],
            companyPhone:[],
            docName:[],
            draughting:[],
            requestNumber:[],
        });
    }
    ngOnDestroy(): void {
        this.dtTrigger.unsubscribe();
        this.dtTrigger1.unsubscribe();
    }
    showToasterError(title:string,message:string){
        this.notifyService.showError(message, title)
    }
    showToasterSuccess(title:string,message:string){
        this.notifyService.showSuccess(message, title)

    }

    public getStdApproved(): void {
        this.loadingText = "Retrieving Drafts...";
        this.SpinnerService.show();
        this.stdComStandardService.getStdApproved().subscribe(
            (response: ISCheckRequirements[]) => {
                this.isCheckRequirements = response;
                //console.log(this.isCheckRequirements)
                this.rerender();
                this.SpinnerService.hide();

            },
            (error: HttpErrorResponse)=>{
                this.SpinnerService.hide();
                console.log(error.message);
            }
        );
    }
    toggleDisplayRemarksTab(proposalId: number){
        this.loadingText = "Loading ...."
        this.SpinnerService.show();
        this.stdIntStandardService.getAllComments(proposalId).subscribe(
            (response: StakeholderProposalComments[]) => {
                this.stakeholderProposalComments = response;
                this.SpinnerService.hide();
                console.log(this.stakeholderProposalComments)
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                console.log(error.message);
            }
        );
        this.isShowRemarksTab = !this.isShowRemarksTab;
        this.isShowCommentsTab= true;

    }
    toggleDisplayCommentsTab(id: number){
        this.loadingText = "Loading ...."
        this.SpinnerService.show();
        this.stdIntStandardService.getUserComments(id).subscribe(
            (response: InternationalStandardsComments[]) => {
                this.internationalStandardsComments = response;
                this.SpinnerService.hide();
                console.log(this.internationalStandardsComments)
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                console.log(error.message);
            }
        );
        this.isShowCommentsTab = !this.isShowCommentsTab;
        this.isShowRemarksTab= true;

    }
    public onOpenModal(iSCheckRequirement: ISCheckRequirements,mode:string,comStdDraftID: number): void{
        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle','modal');
        this.stdComStandardService.getDraftDocumentList(comStdDraftID).subscribe(
            (response: DocumentDTO[]) => {
                this.documentDTOs = response;
                this.SpinnerService.hide();
                //console.log(this.documentDTOs)
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                //console.log(error.message);
            }
        );
        if (mode==='approveEditedDraft'){
            this.actionRequests=iSCheckRequirement;
            button.setAttribute('data-target','#approveEditedDraft');

            this.approveRequirementsFormGroup.patchValue(
                {
                    requestId: this.actionRequests.requestId,
                    id: this.actionRequests.id,
                    draftId: this.actionRequests.draftId,
                    title: this.actionRequests.title,
                    scope:this.actionRequests.scope,
                    normativeReference: this.actionRequests.normativeReference,
                    symbolsAbbreviatedTerms: this.actionRequests.symbolsAbbreviatedTerms,
                    clause:this.actionRequests.clause,
                    special:this.actionRequests.special,
                    standardNumber:this.actionRequests.comStdNumber,
                    departmentId:this.actionRequests.departmentId,
                    subject:this.actionRequests.subject,
                    description:this.actionRequests.description,
                    standardType:this.actionRequests.standardType,
                    docName:this.actionRequests.documentType,
                }
            );

        }
        // @ts-ignore
        container.appendChild(button);
        button.click();

    }

    @ViewChild('closeModalEditedDraft') private closeModalEditedDraft: ElementRef | undefined;

    public hideModalEditedDraft() {
        this.closeModalEditedDraft?.nativeElement.click();
    }

    rerender(): void {
        this.dtElements.forEach((dtElement: DataTableDirective) => {
            if (dtElement.dtInstance)
                dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
                    dtInstance.destroy();
                });
        });
        setTimeout(() => {
            this.dtTrigger.next();
            this.dtTrigger1.next();
        });
    }


    toggleDisplayMainTab(){
        this.isShowMainTab = !this.isShowMainTab;
        this.isShowRemarksTab= true;
        this.isShowCommentsTab= true;
        this.isShowMainTabs= true;
    }
    displayDraftComments(draftID: number){
        //this.loadingText = "Loading ...."
        this.SpinnerService.show();
        this.stdComStandardService.getDraftComments(draftID).subscribe(
            (response: ComStdCommitteeRemarks[]) => {
                this.comStdCommitteeRemarks = response;
                this.SpinnerService.hide();
                // console.log(this.comStdCommitteeRemarks)
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                console.log(error.message);
            }
        );
        this.isShowRemarksTab = !this.isShowRemarksTab;
        this.isShowCommentsTab= true;
        this.isShowMainTab= true;
        this.isShowMainTabs= true;

    }
    toggleDisplayMainTabs(){
        this.isShowMainTabs = !this.isShowMainTabs;
        this.isShowMainTab= true;
        this.isShowRemarksTab= true;
        this.isShowCommentsTab= true;
    }
    approveEditedDraft(): void {
        this.loadingText = "Approving Edited Draft...";
        this.SpinnerService.show();
        //const valueString=this.approveRequirementsFormGroup.get("draftId").value
        this.stdComStandardService.approveEditedDraft(this.approveRequirementsFormGroup.value).subscribe(
            (response ) => {
                //console.log(response);
                //this.onClickUploadStandard(response.body.id)
                this.getStdApproved();
                this.SpinnerService.hide();
                this.showToasterSuccess('Success', `Draft Approved`);
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Try Again`);
                console.log(error.message);
            }
        );
        this.hideModalEditedDraft();
    }
    approveComEditedDraft(): void {
        this.loadingText = "Approving Edited Draft...";
        this.SpinnerService.show();
        this.stdComStandardService.approveEditedDraft(this.approveRequirementsFormGroup.value).subscribe(
            (response ) => {
                //console.log(response);
                this.onClickUploadStandard(response.body.id)
                this.getStdApproved();
                this.SpinnerService.hide();
                this.showToasterSuccess('Success', `Draft Approved`);
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Try Again`);
                console.log(error.message);
            }
        );
        this.hideModalEditedDraft();
    }
    rejectCompanyStandard(): void {
        this.loadingText = "Rejecting Edited Draft...";
        this.SpinnerService.show();
        this.stdComStandardService.rejectCompanyStandard(this.approveRequirementsFormGroup.value).subscribe(
            (response ) => {
                //console.log(response);
                this.getStdApproved();
                this.SpinnerService.hide();
                this.showToasterSuccess('Success', `Draft Rejected`);
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Try Again`);
                console.log(error.message);
            }
        );
        this.hideModalEditedDraft();
    }
    onClickUploadStandard(standardID: string) {
        if (this.uploadStandardFile.length > 0) {
            const file = this.uploadStandardFile;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }
            this.SpinnerService.show();
            this.stdComStandardService.uploadCompanyStandard(standardID, formData).subscribe(
                (data: any) => {
                    this.SpinnerService.hide();
                    this.uploadStandardFile = null;
                    console.log(data);
                    swal.fire({
                        title: 'Thank you....',
                        html:'Standard Uploaded',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success form-wizard-next-btn ',
                        },
                        icon: 'success'
                    });
                },
            );

        }

    }
    viewDraftFile(pdfId: number, fileName: string, applicationType: string): void {
        this.SpinnerService.show();
        this.stdComStandardService.viewCompanyDraft(pdfId).subscribe(
            (dataPdf: any) => {
                this.SpinnerService.hide();
                this.blob = new Blob([dataPdf], {type: applicationType});

                // tslint:disable-next-line:prefer-const
                let downloadURL = window.URL.createObjectURL(this.blob);
                const link = document.createElement('a');
                link.href = downloadURL;
                link.download = fileName;
                link.click();
                // this.pdfUploadsView = dataPdf;
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Processing Request`);
                console.log(error.message);
                this.getStdApproved();
                //alert(error.message);
            }
        );
    }

}
