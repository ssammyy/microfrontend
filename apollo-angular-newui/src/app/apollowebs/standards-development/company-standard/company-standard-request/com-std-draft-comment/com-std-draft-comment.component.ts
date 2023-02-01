import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {
    ApproveDraft,
    ComJcJustificationDec,
    COMPreliminaryDraft,
    DocView
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
  tasks: COMPreliminaryDraft[] = [];
  public actionRequest: COMPreliminaryDraft | undefined;
  public committeeFormGroup!: FormGroup;
  comDraftID: string;
  documentDTOs: DocumentDTO[] = [];
  docDetails: DocView[] = [];

  blob: Blob;
    loadingText: string;
    public uploadCommentsFormGroup!: FormGroup;

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

      this.uploadCommentsFormGroup = this.formBuilder.group({
          commentTitle:[],
          comClause:[],
          comParagraph:[],
          proposedChange:[],
          commentDocumentType:[],
          adoptDraft:[],
          reason:[],
          recommendations:[],
          nameOfRespondent:[],
          positionOfRespondent:[],
          nameOfOrganization:[],
          requestID:[],
          draftID:[],

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
                draftID: this.actionRequest.id
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
    });
  }

    onSubmit(): void {
        this.loadingText = "Saving...";
        this.SpinnerService.show();
        console.log(this.uploadCommentsFormGroup.value);
        this.stdComStandardService.submitDraftComments(this.uploadCommentsFormGroup.value).subscribe(
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

    // viewFile(pdfId: number,fileUploaded: File) {
    //     this.SpinnerService.hide();
    //     this.stdComStandardService.viewCompanyDraft(pdfId).subscribe(
    //         (dataPdf: any) => {
    //             const blob = new Blob([fileUploaded.slice()], {type: fileUploaded.type});
    //
    //             // tslint:disable-next-line:prefer-const
    //             let downloadURL = window.URL.createObjectURL(blob);
    //             const link = document.createElement('a');
    //             link.href = downloadURL;
    //             link.download = fileUploaded.name;
    //             link.click();
    //             console.log(fileUploaded)
    //         },
    //         (error: HttpErrorResponse) => {
    //             this.SpinnerService.hide();
    //             this.showToasterError('Error', `Error Processing Request`);
    //             console.log(error.message);
    //             this.getUploadedStdDraftForComment(this.comDraftID);
    //             //alert(error.message);
    //         }
    //     );
    // }

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

}
