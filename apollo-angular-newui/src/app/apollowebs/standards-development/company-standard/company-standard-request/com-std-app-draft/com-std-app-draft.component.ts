import {Component, OnInit, QueryList, ViewChildren} from '@angular/core';
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {
    ApproveDraft,
    ComJcJustificationDec,
    COMPreliminaryDraft,
    ComStdCommitteeRemarks
} from "../../../../../core/store/data/std/std.model";
import {FormBuilder, FormGroup} from "@angular/forms";
import {StdComStandardService} from "../../../../../core/store/data/std/std-com-standard.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../../core/store/data/std/notification.service";
import {HttpErrorResponse} from "@angular/common/http";
import {DocumentDTO} from "../../../../../core/store/data/levy/levy.model";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-com-std-app-draft',
  templateUrl: './com-std-app-draft.component.html',
  styleUrls: ['./com-std-app-draft.component.css']
})
export class ComStdAppDraftComponent implements OnInit {
  @ViewChildren(DataTableDirective)
  dtElements: QueryList<DataTableDirective>;
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  dtTrigger1: Subject<any> = new Subject<any>();
  tasks: COMPreliminaryDraft[] = [];
  public actionRequest: COMPreliminaryDraft | undefined;
  public committeeFormGroup!: FormGroup;
  public uploadDraftFormGroup!: FormGroup;
  blob: Blob;
    isShowCommentsTab= true;
    comStdCommitteeRemarks: ComStdCommitteeRemarks[] = [];
    documentDTOs: DocumentDTO[] = [];
    comDraftID: string;
  constructor(
      private stdComStandardService:StdComStandardService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private formBuilder: FormBuilder,
      private activatedRoute: ActivatedRoute,
  ) { }

  ngOnInit(): void {

      this.activatedRoute.paramMap.subscribe(
          rs => {
              this.comDraftID = rs.get('comDraftID');

          },
      );

      this.getApprovedStdDraft(this.comDraftID);
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

  public getApprovedStdDraft(comDraftID: string): void{
    this.SpinnerService.show();
    this.stdComStandardService.getApprovedStdDraft(comDraftID).subscribe(
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
    this.stdComStandardService.decisionOnComStdDraft(approveDraft).subscribe(
        (response: ComJcJustificationDec) => {
          this.SpinnerService.hide();
          this.showToasterSuccess('Success', `Draft Approved`);
          console.log(response);
          this.getApprovedStdDraft(this.comDraftID);
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Processing Action`);
          console.log(error.message);
          this.getApprovedStdDraft(this.comDraftID);
          //alert(error.message);
        }
    );
  }
  public onDecisionReject(approveDraft: ApproveDraft): void{
    this.SpinnerService.show();
    this.stdComStandardService.decisionOnComStdDraft(approveDraft).subscribe(
        (response: ComJcJustificationDec) => {
          this.SpinnerService.hide();
          this.showToasterSuccess('Success', `Draft Rejected`);
          console.log(response);
          this.getApprovedStdDraft(this.comDraftID);
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Processing Action`);
          console.log(error.message);
          this.getApprovedStdDraft(this.comDraftID);
          //alert(error.message);
        }
    );
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
          // this.pdfUploadsView = dataPdf;
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Processing Request`);
          console.log(error.message);
          this.getApprovedStdDraft(this.comDraftID);
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
    });
  }

}
