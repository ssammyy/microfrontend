import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {
  ApproveDraft, ComJcJustificationDec,
  COMPreliminaryDraft, ComStdCommitteeRemarks, ComStdRemarks,
  Department, InternationalStandardsComments,
  ISAdoptionProposal, ISCheckRequirements,
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
import {StdComStandardService} from "../../../../core/store/data/std/std-com-standard.service";
import {DocumentDTO} from "../../../../core/store/data/levy/levy.model";
import swal from "sweetalert2";

@Component({
  selector: 'app-com-std-editor',
  templateUrl: './com-std-editor.component.html',
  styleUrls: ['./com-std-editor.component.css']
})
export class ComStdEditorComponent implements OnInit {
    @ViewChildren(DataTableDirective)
    dtElements: QueryList<DataTableDirective>;
    dtOptions: DataTables.Settings = {};
    dtTrigger: Subject<any> = new Subject<any>();
  comStdRemarks: ComStdRemarks[] = [];
  internationalStandardsComments: InternationalStandardsComments[] = [];
  comStdCommitteeRemarks: ComStdCommitteeRemarks[] = [];
  isCheckRequirements:ISCheckRequirements[]=[];
  public actionRequests: ISCheckRequirements | undefined;
    public departments !: Department[] ;
  loadingText: string;
  approve: string;
  reject: string;
  isShowRemarksTab= true;
  isShowCommentsTab= true;
  isShowCommentsTabs= true;
  isShowMainTab= true;
  isShowMainTabs= true;
  public uploadDraftStandardFormGroup!: FormGroup;
  documentDTOs: DocumentDTO[] = [];
  blob: Blob;
  public uploadedFiles:  FileList;
  constructor(
      private store$: Store<any>,
      private router: Router,
      private stdComStandardService:StdComStandardService,
      private standardDevelopmentService : StandardDevelopmentService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private formBuilder: FormBuilder
  ) { }

  ngOnInit(): void {
    this.getStdDraftEditing();
      this.getDepartments();
    this.uploadDraftStandardFormGroup = this.formBuilder.group({
      id:null,
      requestId:null,
      draftId:null,
      title:null,
      scope:null,
      normativeReference:null,
      symbolsAbbreviatedTerms:null,
      clause:null,
      special:null,
      comStdNumber:null,
      preparedBy:null,
      docName:null,
      requestNumber:null,
      departmentId:null,
      subject:null,
      description:null,
      companyName:null,
      companyPhone:null

    });
  }

  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
  }

  showToasterSuccess(title:string,message:string){
    this.notifyService.showSuccess(message, title)

  }
  showToasterError(title:string,message:string){
    this.notifyService.showError(message, title)

  }
  showToasterWarning(title:string,message:string){
    this.notifyService.showWarning(message, title)

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
  public getStdDraftEditing(): void {
    this.loadingText = "Retrieving Drafts...";
    this.SpinnerService.show();
    this.stdComStandardService.getStdDraftEditing().subscribe(
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
          // console.log(this.comStdRemarks)
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

  }


  submitDraftForEditing(): void {
    this.loadingText = "Saving...";
    this.SpinnerService.show();
    this.stdComStandardService.submitDraftForEditing(this.uploadDraftStandardFormGroup.value).subscribe(
        (response ) => {
          console.log(response);
          this.onClickSaveUploads(response.body.draftId)
          this.uploadDraftStandardFormGroup.reset();
          this.getStdDraftEditing();
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
    if (mode==='draftStandardForEditing') {
      this.actionRequests = iSCheckRequirement;
      button.setAttribute('data-target', '#draftStandardForEditing');
      this.uploadDraftStandardFormGroup.patchValue(
          {
            id: this.actionRequests.id,
            requestId: this.actionRequests.requestId,
            draftId: this.actionRequests.draftId,
            requestNumber: this.actionRequests.requestNumber,
            title: this.actionRequests.title,
            scope:this.actionRequests.scope,
            normativeReference: this.actionRequests.normativeReference,
            symbolsAbbreviatedTerms: this.actionRequests.symbolsAbbreviatedTerms,
            clause:this.actionRequests.clause,
            special:this.actionRequests.special,
            comStdNumber:this.actionRequests.comStdNumber,
            departmentId:this.actionRequests.departmentId,
            subject:this.actionRequests.subject,
            description:this.actionRequests.description
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
          this.getStdDraftEditing();
          //alert(error.message);
        }
    );
  }

  @ViewChild('closeModalStdEditing') private closeModalStdEditing: ElementRef | undefined;

  public hideModalStdEditing() {
    this.closeModalStdEditing?.nativeElement.click();
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
    });
  }

}
