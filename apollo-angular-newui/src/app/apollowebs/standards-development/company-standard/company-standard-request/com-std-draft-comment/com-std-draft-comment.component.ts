import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {
    ApproveDraft,
    ComJcJustificationDec,
    COMPreliminaryDraft,
    DocView, PredefinedSdIntCommentsFields
} from "../../../../../core/store/data/std/std.model";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {StdComStandardService} from "../../../../../core/store/data/std/std-com-standard.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../../core/store/data/std/notification.service";
import {HttpErrorResponse} from "@angular/common/http";
import {ActivatedRoute} from "@angular/router";
import {DocumentDTO} from "../../../../../core/store/data/levy/levy.model";

@Component({
  selector: 'app-com-std-draft-comment',
  templateUrl: './com-std-draft-comment.component.html',
  styleUrls: ['./com-std-draft-comment.component.css']
})
export class ComStdDraftCommentComponent implements OnInit {
  @ViewChildren(DataTableDirective)
  dtElements: QueryList<DataTableDirective>;
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  dtTrigger1: Subject<any> = new Subject<any>();
  tasks: COMPreliminaryDraft[] = [];
  public actionRequest: COMPreliminaryDraft | undefined;
  public committeeFormGroup!: FormGroup;
  comDraftID: string;
    submitted = false;
  documentDTOs: DocumentDTO[] = [];
  docDetails: DocView[] = [];
    dataSaveResourcesRequired : PredefinedSdIntCommentsFields;
    dataSaveResourcesRequiredList: PredefinedSdIntCommentsFields[]=[];
    predefinedSDCommentsDataAdded: boolean = false;

  blob: Blob;
    loadingText: string;
    public uploadCommentsFormGroup!: FormGroup;
    docName='';

  constructor(
      private stdComStandardService:StdComStandardService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private formBuilder: FormBuilder,
      private activatedRoute: ActivatedRoute,
  ) { }

  ngOnInit(): void {
    //this.getUploadedStdDraftForComment();
    this.activatedRoute.paramMap.subscribe(
        rs => {
          this.comDraftID = rs.get('comDraftID');

        },
    );
    //console.log(this.comDraftID);
    this.getUploadedStdDraftForComment(this.comDraftID);
    this.docName='Company Standard'

      this.uploadCommentsFormGroup = this.formBuilder.group({
          commentTitle:null,
          commentDocumentType:null,
          uploadDate:null,
          nameOfRespondent:null,
          emailOfRespondent:null,
          phoneOfRespondent:null,
          nameOfOrganization:null,
          scope:null,
          clause:null,
          paragraph:null,
          typeOfComment:null,
          comment:null,
          proposedChange:null,
          observation:null,
          requestID:null,
          draftID:null

      });
  }

  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
    this.dtTrigger1.unsubscribe();
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
  public getUploadedStdDraftForComment(comDraftID: string): void{
    this.SpinnerService.show();
    this.stdComStandardService.getUploadedStdDraftForComment(comDraftID).subscribe(
        (response: COMPreliminaryDraft[])=> {
          this.SpinnerService.hide();
          this.rerender();
          this.tasks = response;
        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }

  public onOpenModal(task: COMPreliminaryDraft,mode:string,comStdDraftID: number): void{
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

    if (mode==='approveDraft'){
      this.actionRequest=task;
      button.setAttribute('data-target','#approveDraft');
        this.uploadCommentsFormGroup.patchValue(
            {
                commentTitle: this.actionRequest.title,
                requestID: this.actionRequest.requestId,
                draftID: this.actionRequest.id,
                commentDocumentType: this.docName,
                uploadDate: this.actionRequest.uploadDate
            }
        );
    }


    // @ts-ignore
    container.appendChild(button);
    button.click();

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

    onSubmit(): void {
        this.loadingText = "Saving...";
        this.SpinnerService.show();
        //console.log(this.uploadCommentsFormGroup.value);
        this.stdComStandardService.submitDraftComments(this.dataSaveResourcesRequiredList).subscribe(
            (response ) => {
                console.log(response);
                this.SpinnerService.hide();
                this.showToasterSuccess(response.httpStatus, `Comment Submitted`);
                this.getUploadedStdDraftForComment(this.comDraftID);
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Try Again`);
                console.log(error.message);
            }
        );
        this.hideModalComment();
    }

  public commentOnDraft(approveDraft: ApproveDraft): void{
    this.SpinnerService.show();
    this.stdComStandardService.commentOnDraft(approveDraft).subscribe(
        (response: ComJcJustificationDec) => {
          this.SpinnerService.hide();
          this.showToasterSuccess('Success', `Comment Uploaded`);
          console.log(response);
          this.getUploadedStdDraftForComment(this.comDraftID);
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Processing Action`);
          console.log(error.message);
          this.getUploadedStdDraftForComment(this.comDraftID);
          //alert(error.message);
        }
    );
    this.hideModalComment();
  }

  viewPdfFile(pdfId: number, fileName: string, applicationType: string): void {
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
          //console.log(this.blob);
          // this.pdfUploadsView = dataPdf;
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Processing Request`);
          console.log(error.message);
          this.getUploadedStdDraftForComment(this.comDraftID);
          //alert(error.message);
        }
    );
  }

  @ViewChild('closeModalComment') private closeModalComment: ElementRef | undefined;

  public hideModalComment() {
    this.closeModalComment?.nativeElement.click();
  }

    onClickAddResource() {
        this.dataSaveResourcesRequired = this.uploadCommentsFormGroup.value;
        console.log(this.dataSaveResourcesRequired);
        // tslint:disable-next-line:max-line-length
        this.dataSaveResourcesRequiredList.push(this.dataSaveResourcesRequired);
        //console.log(this.dataSaveResourcesRequiredList);
        this.predefinedSDCommentsDataAdded= true;
        //console.log(this.predefinedSDCommentsDataAdded);

        this.uploadCommentsFormGroup?.get('clause')?.reset();
        this.uploadCommentsFormGroup?.get('paragraph')?.reset();
        this.uploadCommentsFormGroup?.get('typeOfComment')?.reset();
        this.uploadCommentsFormGroup?.get('comment')?.reset();
        this.uploadCommentsFormGroup?.get('proposedChange')?.reset();
        this.uploadCommentsFormGroup?.get('observation')?.reset();
        this.uploadCommentsFormGroup?.get('scope')?.reset();
    }

    // Remove Form repeater values
    removeDataResource(index) {
        console.log(index);
        if (index === 0) {
            this.dataSaveResourcesRequiredList.splice(index, 1);
            this.predefinedSDCommentsDataAdded = false
        } else {
            this.dataSaveResourcesRequiredList.splice(index, index);
        }
    }

    onClickSaveWorkPlanScheduled() {
        this.submitted = true;
        console.log(this.dataSaveResourcesRequiredList.length);
        if (this.dataSaveResourcesRequiredList.length > 0) {
            this.onSubmit();
        }
    }

}
