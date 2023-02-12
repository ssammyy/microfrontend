import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {Subject} from "rxjs";
import {
    ApproveDraft,
    ApproveSACJC,
    ComJcJustificationDec, COMPreliminaryDraft, ComStdCommitteeRemarks,
    ComStdRequest, StakeholderProposalComments
} from "../../../../core/store/data/std/std.model";
import {StdComStandardService} from "../../../../core/store/data/std/std-com-standard.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {HttpErrorResponse} from "@angular/common/http";
import {DataTableDirective} from "angular-datatables";
import {FormBuilder, FormGroup} from "@angular/forms";
import {CompanyContactDetails, DocumentDTO, JCList} from "../../../../core/store/data/levy/levy.model";
import swal from "sweetalert2";

@Component({
  selector: 'app-com-std-draft',
  templateUrl: './com-std-draft.component.html',
  styleUrls: ['./com-std-draft.component.css']
})
export class ComStdDraftComponent implements OnInit {
    @ViewChildren(DataTableDirective)
    dtElements: QueryList<DataTableDirective>;
    dtOptions: DataTables.Settings = {};
    dtTrigger: Subject<any> = new Subject<any>();
    dtTrigger1: Subject<any> = new Subject<any>();
    dtTrigger2: Subject<any> = new Subject<any>();
    tasks: COMPreliminaryDraft[] = [];
    jointCommitteeLists: JCList[] = [];
    contactPersonDetails: CompanyContactDetails[] = [];
    comStdCommitteeRemarks: ComStdCommitteeRemarks[] = [];
    public actionRequest: COMPreliminaryDraft | undefined;
    public committeeFormGroup!: FormGroup;
    public uploadDraftFormGroup!: FormGroup;
    documentDTOs: DocumentDTO[] = [];
  blob: Blob;
    loadingText: string;
    isShowCommentsTab= true;
  constructor(
      private stdComStandardService:StdComStandardService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private formBuilder: FormBuilder,
  ) { }

  ngOnInit(): void {
    this.getUploadedStdDraft();
  }
    ngOnDestroy(): void {
        this.dtTrigger.unsubscribe();
        this.dtTrigger1.unsubscribe();
        this.dtTrigger2.unsubscribe();
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
    public getUploadedStdDraft(): void{
        this.SpinnerService.show();
        this.stdComStandardService.getUploadedStdDraft().subscribe(
            (response: COMPreliminaryDraft[])=> {
                this.SpinnerService.hide();
                this.rerender();
                this.tasks = response;
                //console.log(this.tasks)
            },
            (error: HttpErrorResponse)=>{
                this.SpinnerService.hide();
                alert(error.message);
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
         this.isShowCommentsTab = !this.isShowCommentsTab;
        //this.isShowCommentsTab= true;

    }
    public getCompanyContactDetails(requestId: number): void{
        this.SpinnerService.show();
        this.stdComStandardService.getCompanyContactDetails(requestId).subscribe(
            (response: CompanyContactDetails[])=> {
                this.SpinnerService.hide();
                this.rerender();
                this.contactPersonDetails = response;
            },
            (error: HttpErrorResponse)=>{
                this.SpinnerService.hide();
                alert(error.message);
            }
        );
    }

    public getCommitteeList(requestId: number): void{
        this.SpinnerService.show();
        this.stdComStandardService.getCommitteeList(requestId).subscribe(
            (response: JCList[])=> {
                this.SpinnerService.hide();
                this.rerender();
                this.jointCommitteeLists = response;
            },
            (error: HttpErrorResponse)=>{
                this.SpinnerService.hide();
                alert(error.message);
            }
        );
    }


  public onOpenModal(task: COMPreliminaryDraft,mode:string,comStdDraftID: number,requestId: number): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
      this.getCommitteeList(requestId);
      this.getCompanyContactDetails(requestId);

      this.stdComStandardService.getDraftCommentList(comStdDraftID).subscribe(
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

    if (mode==='approveJustification'){
      this.actionRequest=task;
      button.setAttribute('data-target','#approveJustification');
    }
    if (mode==='prepPd'){
      this.actionRequest=task;
      button.setAttribute('data-target','#prepPd');
    }
    if (mode==='approveDraft'){
      this.actionRequest=task;
      button.setAttribute('data-target','#approveDraft');
    }

    if (mode==='reject'){
      this.actionRequest=task;
      button.setAttribute('data-target','#reject');
    }
    // @ts-ignore
    container.appendChild(button);
    button.click();

  }
  public decisionOnAccept(approveDraft: ApproveDraft): void{
    this.SpinnerService.show();
    this.stdComStandardService.decisionOnDraft(approveDraft).subscribe(
        (response) => {
          this.SpinnerService.hide();
            this.showToasterSuccess(response.httpStatus, response.body.responseMessage);
            swal.fire({
                text: response.body.responseMessage,
                buttonsStyling: false,
                customClass: {
                    confirmButton: 'btn btn-success form-wizard-next-btn ',
                },
                icon: 'success'
            });
          this.getUploadedStdDraft();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Processing Action`);
          console.log(error.message);
          this.getUploadedStdDraft();
          //alert(error.message);
        }
    );
    this.hideModalUploadDraft();
  }
  public onDecisionReject(approveDraft: ApproveDraft): void{
    this.SpinnerService.show();
    this.stdComStandardService.decisionOnDraft(approveDraft).subscribe(
        (response) => {
          this.SpinnerService.hide();
          this.showToasterError(response.httpStatus, response.body.responseMessage);
            swal.fire({
                text: response.body.responseMessage,
                buttonsStyling: false,
                customClass: {
                    confirmButton: 'btn btn-success form-wizard-next-btn ',
                },
                icon: 'success'
            });
          this.getUploadedStdDraft();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Processing Action`);
          console.log(error.message);
          this.getUploadedStdDraft();
          //alert(error.message);
        }
    );
      this.hideModalUploadDraft();
  }
    // toggleDisplayDocuments(comStdDraftID: number) {
    //     this.SpinnerService.show();
    //     this.stdComStandardService.getDraftDocumentList(comStdDraftID).subscribe(
    //         (response: DocumentDTO[]) => {
    //             this.documentDTOs = response;
    //             this.SpinnerService.hide();
    //             console.log(this.documentDTOs)
    //         },
    //         (error: HttpErrorResponse) => {
    //             this.SpinnerService.hide();
    //             console.log(error.message);
    //         }
    //     );
    //
    //
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
                // this.pdfUploadsView = dataPdf;
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Processing Request`);
                console.log(error.message);
                this.getUploadedStdDraft();
                //alert(error.message);
            }
        );
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
            this.dtTrigger2.next();
        });
    }

    @ViewChild('closeModalUploadDraft') private closeModalUploadDraft: ElementRef | undefined;

    public hideModalUploadDraft() {
        this.closeModalUploadDraft?.nativeElement.click();
    }

}
