import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {Subject} from "rxjs";
import {
    ComStdCommitteeRemarks,
    ComStdRemarks,
    InternationalStandardsComments, ISCheckRequirements,
    ISHopTASKS,
    StakeholderProposalComments
} from "../../../../core/store/data/std/std.model";
import {StdIntStandardService} from "../../../../core/store/data/std/std-int-standard.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {HttpErrorResponse} from "@angular/common/http";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import swal from "sweetalert2";
import {DataTableDirective} from "angular-datatables";
import {DocumentDTO} from "../../../../core/store/data/levy/levy.model";
import {PublishingService} from "../../../../core/store/data/std/publishing.service";
import {CommitteeService} from "../../../../core/store/data/std/committee.service";
import {StdComStandardService} from "../../../../core/store/data/std/std-com-standard.service";
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";

@Component({
  selector: 'app-int-std-upload-standard',
  templateUrl: './int-std-upload-standard.component.html',
  styleUrls: ['./int-std-upload-standard.component.css']
})
export class IntStdUploadStandardComponent implements OnInit {
    @ViewChildren(DataTableDirective)
    dtElements: QueryList<DataTableDirective>;
    dtOptions: DataTables.Settings = {};
    dtTrigger: Subject<any> = new Subject<any>();
    dtTrigger1: Subject<any> = new Subject<any>();
    stakeholderProposalComments: StakeholderProposalComments[] = [];
    internationalStandardsComments: InternationalStandardsComments[] = [];
    isCheckRequirements:ISCheckRequirements[]=[];
    public actionRequest: ISCheckRequirements | undefined;
    comStdRemarks: ComStdRemarks[] = [];
    loadingText: string;
    approve: string;
    reject: string;
    blob: Blob;
    isShowRemarksTab= true;
    isShowCommentsTab= true;
    isShowMainTab= true;
    isShowMainTabs= true;
    public prepareStandardFormGroup!: FormGroup;
    comStdCommitteeRemarks: ComStdCommitteeRemarks[] = [];
    public actionRequests: ISCheckRequirements | undefined;
    documentDTOs: DocumentDTO[] = [];
  public uploadedFiles:  FileList;
    public uploadStandardFile:  FileList;

  constructor(
      private publishingService: PublishingService, private notifyService: NotificationService,
      private SpinnerService: NgxSpinnerService, private committeeService: CommitteeService,
      private stdComStandardService:StdComStandardService,
      private standardDevelopmentService : StandardDevelopmentService,
      private formBuilder: FormBuilder,
      private stdIntStandardService: StdIntStandardService
  ) { }

  ngOnInit(): void {

        this.getAppStdPublishing();
        this.prepareStandardFormGroup = this.formBuilder.group({
            docName: null,
            title: null,
            scope: null,
            normativeReference: null,
            symbolsAbbreviatedTerms: null,
            clause: null,
            special: null,
            standardType: null,
            comments: null,
            accentTo: null,
            draftId: null,
            requestId: null,
            id: null,

        });
  }
  get formPrepareWD(): any {
    return this.prepareStandardFormGroup.controls;
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
    public getAppStdPublishing(): void {
        this.loadingText = "Retrieving Standards...";
        this.SpinnerService.show();
        this.stdComStandardService.getAppStd().subscribe(
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

    toggleDisplayMainTab(){
        this.isShowMainTab = !this.isShowMainTab;
        this.isShowRemarksTab= true;
        this.isShowCommentsTab= true;
        this.isShowMainTabs= true;
    }
    toggleDisplayMainTabs(){
        this.isShowMainTabs = !this.isShowMainTabs;
        this.isShowMainTab= true;
        this.isShowRemarksTab= true;
        this.isShowCommentsTab= true;
    }


    toggleDisplayRemarksTab(requestId: number){
        this.loadingText = "Loading ...."
        this.SpinnerService.show();
        this.stdComStandardService.getAllComments(requestId).subscribe(
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
    public onOpenModal(iSCheckRequirement: ISCheckRequirements,mode:string): void{
        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle','modal');
        if (mode==='checkRequirementsMet'){
            this.actionRequest=iSCheckRequirement;
            button.setAttribute('data-target','#checkRequirementsMet');

            this.prepareStandardFormGroup.patchValue(
                {
                    accentTo: this.approve,
                    proposalId: this.actionRequest.proposalId,
                    justificationId: this.actionRequest.justificationNo,
                    draftId: this.actionRequest.id,
                    title:this.actionRequest.title,
                    normativeReference:this.actionRequest.normativeReference,
                    symbolsAbbreviatedTerms:this.actionRequest.symbolsAbbreviatedTerms,
                    clause:this.actionRequest.clause,
                    scope:this.actionRequest.scope,
                    special:this.actionRequest.special,
                    standardNumber:this.actionRequest.isNumber
                }
            );

        }
        // @ts-ignore
        container.appendChild(button);
        button.click();

    }

    @ViewChild('closeModalRequirements') private closeModalRequirements: ElementRef | undefined;

    public hideModalRequirements() {
        this.closeModalRequirements?.nativeElement.click();
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

    approveInternationalStandard(): void {
        this.loadingText = "Uploading Draft...";
        this.SpinnerService.show();
        this.stdIntStandardService.uploadInternationalStandard(this.prepareStandardFormGroup.value).subscribe(
            (response ) => {
                //console.log(response);
                this.getAppStdPublishing();
                this.onClickUploadStandard(response.body.id)
                this.SpinnerService.hide();
                this.showToasterSuccess('Success', `Standard Uploaded `);
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Try Again`);
                console.log(error.message);
            }
        );
        this.hideModalRequirements();
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


    public onOpenModals(iSCheckRequirement: ISCheckRequirements,mode:string,comStdDraftID: number): void{
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

        if (mode==='approveStandard'){
            this.actionRequests=iSCheckRequirement;
            button.setAttribute('data-target','#approveStandard');

            this.prepareStandardFormGroup.patchValue(
                {
                    id: this.actionRequests.id,
                    requestId: this.actionRequests.requestId,
                    draftId: this.actionRequests.draftId,
                    standardNumber:this.actionRequests.comStdNumber,
                    title:this.actionRequest.title,
                    normativeReference:this.actionRequest.normativeReference,
                    symbolsAbbreviatedTerms:this.actionRequest.symbolsAbbreviatedTerms,
                    clause:this.actionRequest.clause,
                    scope:this.actionRequest.scope,
                    special:this.actionRequest.special,
                }
            );
        }
        // @ts-ignore
        container.appendChild(button);
        button.click();

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
                this.getAppStdPublishing();
                //alert(error.message);
            }
        );
    }
    @ViewChild('closeModalEditedDraft') private closeModalEditedDraft: ElementRef | undefined;

    public hideModalEditedDraft() {
        this.closeModalEditedDraft?.nativeElement.click();
    }

}
