import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {
    ComStdCommitteeRemarks, ComStdRemarks,
    Department, InternationalStandardsComments, ISAdoptionProposal,
    ISJustificationProposal,
    StakeholderProposalComments
} from "../../../../core/store/data/std/std.model";
import {FormBuilder, FormGroup} from "@angular/forms";
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
  selector: 'app-int-std-editor',
  templateUrl: './int-std-editor.component.html',
  styleUrls: ['./int-std-editor.component.css']
})
export class IntStdEditorComponent implements OnInit {
    @ViewChildren(DataTableDirective)
    dtElements: QueryList<DataTableDirective>;
    dtOptions: DataTables.Settings = {};
    dtTrigger: Subject<any> = new Subject<any>();
    dtTrigger1: Subject<any> = new Subject<any>();
    dtTrigger2: Subject<any> = new Subject<any>();
    isAdoptionProposals: ISAdoptionProposal[]=[];
    public actionRequests: ISAdoptionProposal | undefined;
    public prepareJustificationFormGroup!: FormGroup;
    stakeholderProposalComments: StakeholderProposalComments[] = [];
    internationalStandardsComments: InternationalStandardsComments[] = [];
    comStdCommitteeRemarks: ComStdCommitteeRemarks[] = [];
    iSJustificationProposals: ISJustificationProposal[] = [];
    loadingText: string;
    approve: string;
    reject: string;
    isShowRemarksTab= true;
    isShowCommentsTab= true;
    public departments !: Department[] ;
    documentDTOs: DocumentDTO[] = [];
    comStdRemarks: ComStdRemarks[] = [];
    blob: Blob;
    public uploadedFiles:  FileList;
    public uploadedCoverPages:  FileList;
    isShowRemarksTabs= true;
    isShowCommentsTabs= true;
    isShowMainTab= true;
    isShowMainTabs= true;
    isShowJustificationTabs= true;
    public approveSpcJustificationFormGroup!: FormGroup;
    public uploadDraftStandardFormGroup!: FormGroup;
  constructor(
      private store$: Store<any>,
      private router: Router,
      private stdIntStandardService:StdIntStandardService,
      private stdComStandardService:StdComStandardService,
      private standardDevelopmentService : StandardDevelopmentService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private formBuilder: FormBuilder
  ) { }

  ngOnInit(): void {
    this.getApprovedJustification();
      this.getDepartments();

      this.uploadDraftStandardFormGroup = this.formBuilder.group({
          id: [],
          requestId:[],
          draftId:[],
          title:[],
          scope:[],
          normativeReference:[],
          symbolsAbbreviatedTerms:[],
          clause:[],
          special:[],
          comStdNumber:[],
          preparedBy: [],
          docName:[],
          requestNumber:[],
          departmentId:[],
          subject:[],
          description:[],
          contactOneFullName:[],
          contactOneTelephone:[],
          contactOneEmail:[],
          companyName:[],
          companyPhone:[]

      });
  }
    ngOnDestroy(): void {
        this.dtTrigger.unsubscribe();
        this.dtTrigger1.unsubscribe();
        this.dtTrigger2.unsubscribe();
    }
    showToasterError(title:string,message:string){
        this.notifyService.showError(message, title)

    }
    showToasterSuccess(title:string,message:string){
        this.notifyService.showSuccess(message, title)

    }
    public getApprovedJustification(): void{
        this.SpinnerService.show();
        this.stdIntStandardService.getApprovedJustification().subscribe(
            (response: ISAdoptionProposal[])=> {
                this.SpinnerService.hide();
                this.rerender();
                this.isAdoptionProposals = response;
                console.log(this.isAdoptionProposals);
            },
            (error: HttpErrorResponse)=>{
                this.SpinnerService.hide();
                alert(error.message);
            }
        );
    }
    public getDepartments(): void{
        this.standardDevelopmentService.getDepartments().subscribe(
            (response: Department[])=> {
                this.departments = response;
            },
            (error: HttpErrorResponse)=>{
                alert(error.message);
            }
        );
    }
    public getISJustification(draftId: number): void {
        this.loadingText = "Retrieving Justifications...";
        this.SpinnerService.show();
        this.stdIntStandardService.getISJustification(draftId).subscribe(
            (response: ISJustificationProposal[]) => {
                this.iSJustificationProposals = response;
                console.log(this.iSJustificationProposals)
                this.rerender();
                this.SpinnerService.hide();

            },
            (error: HttpErrorResponse)=>{
                this.SpinnerService.hide();
                console.log(error.message);
            }
        );
        this.isShowJustificationTabs = !this.isShowJustificationTabs;
        this.isShowRemarksTab= true;
        this.isShowMainTab= true;
        this.isShowMainTabs= true;
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
        this.isShowJustificationTabs= true;

    }
    approveSpcJustification(): void {
        this.loadingText = "Decision on Justification...";
        this.SpinnerService.show();
        this.stdIntStandardService.decisionOnJustification(this.approveSpcJustificationFormGroup.value).subscribe(
            (response ) => {
                //console.log(response);
                this.getApprovedProposals();
                this.SpinnerService.hide();
                this.showToasterSuccess('Success', `Justification Approved`);
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Try Again`);
                console.log(error.message);
            }
        );
        this.hideModalApproveSpcJustification();
    }


    public onOpenModal(isAdoptionProposal: ISAdoptionProposal,mode:string,comStdDraftID: number): void {
        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle', 'modal');
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

        if (mode === 'decisionOnJustification') {
            this.actionRequests = isAdoptionProposal;
            button.setAttribute('data-target', '#decisionOnJustification');
            this.uploadDraftStandardFormGroup.patchValue(
                {
                    requestId: this.actionRequests.id,
                    draftId: this.actionRequests.draftId,
                    title: this.actionRequests.title,
                    scope:this.actionRequests.scope,
                    normativeReference:this.actionRequests.normativeReference,
                    special:this.actionRequests.special,
                    clause:this.actionRequests.clause,
                    comStdNumber:this.actionRequests.standardNumber,
                    contactOneFullName:this.actionRequests.contactOneFullName,
                    contactOneTelephone:this.actionRequests.contactOneTelephone,
                    contactOneEmail:this.actionRequests.contactOneEmail,
                }
            );

            // @ts-ignore
            container.appendChild(button);
            button.click();

        }
    }



    @ViewChild('closeModalApproveSPCJustification') private closeModalApproveSPCJustification: ElementRef | undefined;

    public hideModalApproveSpcJustification() {
        this.closeModalApproveSPCJustification?.nativeElement.click();
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
            }
        );
    }

    public getApprovedProposals(): void{
        this.SpinnerService.show();
        this.stdIntStandardService.getJustification().subscribe(
            (response: ISAdoptionProposal[])=> {
                this.SpinnerService.hide();
                this.rerender();
                this.isAdoptionProposals = response;
                console.log(this.isAdoptionProposals);
            },
            (error: HttpErrorResponse)=>{
                this.SpinnerService.hide();
                alert(error.message);
            }
        );
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
                this.getApprovedProposals();
                //alert(error.message);
            }
        );
    }


    displayDraftComments(draftID: number){
        //this.loadingText = "Loading ...."
        this.SpinnerService.show();
        this.stdComStandardService.getDraftComments(draftID).subscribe(
            (response: ComStdCommitteeRemarks[]) => {
                this.comStdCommitteeRemarks = response;
                this.SpinnerService.hide();
                console.log(this.comStdCommitteeRemarks)
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
        this.isShowJustificationTabs= true;

    }

    toggleDisplayRemarksTab(requestId: number){
        this.loadingText = "Loading ...."
        this.SpinnerService.show();
        this.stdIntStandardService.getDraftComment(requestId).subscribe(
            (response: ComStdRemarks[]) => {
                this.comStdRemarks = response;
                this.SpinnerService.hide();
                console.log(this.comStdRemarks)
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                console.log(error.message);
            }
        );
        this.isShowCommentsTab = !this.isShowCommentsTab;
        this.isShowRemarksTab= true;
        this.isShowMainTab= true;
        this.isShowMainTabs= true;
        this.isShowJustificationTabs= true;

    }

    submitDraftForEditing(): void {
        this.loadingText = "Saving...";
        this.SpinnerService.show();
        this.stdIntStandardService.submitDraftForEditing(this.uploadDraftStandardFormGroup.value).subscribe(
            (response ) => {
                console.log(response);
                this.onClickSaveUploads(response.body.draftId)
                this.uploadDraftStandardFormGroup.reset();
                this.getApprovedJustification();
                this.SpinnerService.hide();
                this.showToasterSuccess(response.httpStatus, `Draft Prepared`);

            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Try Again`);
                console.log(error.message);
            }
        );
        this.hideModalStdEditing();

    }

    onClickSaveUploads(comStdDraftID: string) {
        if (this.uploadedFiles.length > 0) {
            const file = this.uploadedFiles;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }
            this.SpinnerService.show();
            this.stdComStandardService.uploadPDFileDetails(comStdDraftID, formData).subscribe(
                (data: any) => {
                    this.SpinnerService.hide();
                    this.uploadedFiles = null;
                    this.showToasterSuccess(data.httpStatus, `Standard Draft Uploaded`);
                    this.onClickUploadCoverPages(comStdDraftID)

                },
            );

        }

    }

    onClickUploadCoverPages(comStdDraftID: string) {
        if (this.uploadedCoverPages.length > 0) {
            const file = this.uploadedCoverPages;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }
            this.SpinnerService.show();
            this.stdComStandardService.uploadCoverPages(comStdDraftID, formData).subscribe(
                (data: any) => {
                    this.SpinnerService.hide();
                    this.uploadedCoverPages = null;
                    console.log(data);
                    swal.fire({
                        title: 'Thank you....',
                        html:'Documents Uploaded',
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

    @ViewChild('closeModalStdEditing') private closeModalStdEditing: ElementRef | undefined;

    public hideModalStdEditing() {
        this.closeModalStdEditing?.nativeElement.click();
    }

}
