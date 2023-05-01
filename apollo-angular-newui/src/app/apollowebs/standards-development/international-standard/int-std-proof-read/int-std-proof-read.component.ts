import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren, ViewEncapsulation} from '@angular/core';
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {
    ComStdCommitteeRemarks,
    ComStdRemarks,
    InternationalStandardsComments,
    ISCheckRequirements,
    StakeholderProposalComments, UsersEntity
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
  selector: 'app-int-std-proof-read',
  templateUrl: './int-std-proof-read.component.html',
  styleUrls: ['./int-std-proof-read.component.css','../../../../../../node_modules/@ng-select/ng-select/themes/default.theme.css'],
    encapsulation: ViewEncapsulation.None
})
export class IntStdProofReadComponent implements OnInit {
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
    coverDTOs: DocumentDTO[] = [];
    public usersLists !: UsersEntity[] ;
    usersList: number;
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
    this.getStdEditProofreading();
    this.getEditorDetails();
      this.proofReadFormGroup = this.formBuilder.group({
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
          assignedTo:[]

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

    public getStdEditProofreading(): void {
        this.loadingText = "Retrieving Drafts...";
        this.SpinnerService.show();
        this.stdComStandardService.getStdEditProofreading().subscribe(
            (response: ISCheckRequirements[]) => {
                this.isCheckRequirements = response;
                console.log(this.isCheckRequirements)
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
        this.stdIntStandardService.getDraftComment(proposalId).subscribe(
            (response: ComStdRemarks[]) => {
                this.comStdRemarks = response;
                this.SpinnerService.hide();
                //console.log(this.comStdRemarks)
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

    toggleDisplayMainTabs(){
        this.isShowMainTabs = !this.isShowMainTabs;
        this.isShowMainTab= true;
        this.isShowRemarksTab= true;
        this.isShowCommentsTab= true;
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
        this.stdComStandardService.getCoverPagesList(comStdDraftID).subscribe(
            (response: DocumentDTO[]) => {
                this.coverDTOs = response;
                this.SpinnerService.hide();
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
            }
        );
        if (mode==='standardProofreading'){
            this.actionRequests=iSCheckRequirement;
            button.setAttribute('data-target','#standardProofreading');

            this.proofReadFormGroup.patchValue(
                {
                    requestId: this.actionRequests.requestId,
                    draftId: this.actionRequests.draftId,
                    id: this.actionRequests.id,
                    title: this.actionRequests.title,
                    docName:this.actionRequests.documentType,
                    standardNumber:this.actionRequests.comStdNumber,
                    draughting:this.actionRequests.draughting,
                    requestNumber: this.actionRequests.requestNumber,
                    scope:this.actionRequests.scope,
                    normativeReference: this.actionRequests.normativeReference,
                    symbolsAbbreviatedTerms: this.actionRequests.symbolsAbbreviatedTerms,
                    clause:this.actionRequests.clause,
                    special:this.actionRequests.special,
                    departmentId:this.actionRequests.departmentId,
                    subject:this.actionRequests.subject,
                    description:this.actionRequests.description,
                    contactOneFullName:this.actionRequests.contactOneFullName,
                    contactOneTelephone:this.actionRequests.contactOneTelephone,
                    contactOneEmail:this.actionRequests.contactOneEmail,
                    contactTwoFullName:this.actionRequests.contactTwoFullName,
                    contactTwoTelephone:this.actionRequests.contactTwoTelephone,
                    contactTwoEmail:this.actionRequests.contactTwoEmail,
                    contactThreeFullName:this.actionRequests.contactThreeFullName,
                    contactThreeTelephone:this.actionRequests.contactThreeTelephone,
                    contactThreeEmail:this.actionRequests.contactThreeEmail,
                    companyName:this.actionRequests.companyName,
                    companyPhone:this.actionRequests.companyPhone
                }
            );

        }
        // @ts-ignore
        container.appendChild(button);
        button.click();

    }

    @ViewChild('closeModalProofReading') private closeModalProofReading: ElementRef | undefined;

    public hideModalProofReading() {
        this.closeModalProofReading?.nativeElement.click();
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



    onClickUploadDrafts(comStdDraftID: string) {
        if (this.uploadProofReads.length > 0) {
            const file = this.uploadProofReads;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }
            this.SpinnerService.show();
            this.stdComStandardService.uploadPDFileDetails(comStdDraftID, formData).subscribe(
                (data: any) => {
                    this.SpinnerService.hide();
                    this.uploadProofReads = null;
                    console.log(data);
                    swal.fire({
                        title: 'Thank you....',
                        html:'Standard Draft Uploaded',
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
    proofReadStandard(): void {
        this.loadingText = "Proof Reading Draft...";
        this.SpinnerService.show();
        const valueString=this.proofReadFormGroup.get("draftId").value
        this.stdComStandardService.proofReadStandard(this.proofReadFormGroup.value).subscribe(
            (response ) => {
                //console.log(response);
                this.getStdEditProofreading();
                this.onClickUploadDrafts(valueString)
                this.SpinnerService.hide();
                this.showToasterSuccess('Success', `Draft Updated`);
                this.editDraughtFormGroup.reset();
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Try Again`);
                console.log(error.message);
            }
        );
        this.hideModalProofReading();
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
                this.getStdEditProofreading();
                //alert(error.message);
            }
        );
    }

    @ViewChild('closeModalDrafting') private closeModalDrafting: ElementRef | undefined;

    public hideModalDrafting() {
        this.closeModalDrafting?.nativeElement.click();
    }

    viewCoverPages(pdfId: number, fileName: string, applicationType: string): void {
        this.SpinnerService.show();
        this.stdComStandardService.viewCoverPages(pdfId).subscribe(
            (dataPdf: any) => {
                this.SpinnerService.hide();
                this.blob = new Blob([dataPdf], {type: applicationType});
                let downloadURL = window.URL.createObjectURL(this.blob);
                const link = document.createElement('a');
                link.href = downloadURL;
                link.download = fileName;
                link.click();
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Processing Request`);
                console.log(error.message);
                this.getStdEditProofreading();
            }
        );
    }
    public getEditorDetails(): void {
        this.SpinnerService.show();
        this.stdIntStandardService.getEditorDetails().subscribe(
            (response: UsersEntity[]) => {
                this.SpinnerService.hide();
                this.usersLists = response;
                // console.log(this.usersLists);
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                alert(error.message);
            }
        );
    }

}
