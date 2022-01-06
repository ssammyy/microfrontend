import { Component, OnInit } from '@angular/core';
import {Subject} from "rxjs";
import {ApproveDraft, ComJcJustificationDec} from "../../../../core/store/data/std/std.model";
import {StdComStandardService} from "../../../../core/store/data/std/std-com-standard.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-com-std-draft-view',
  templateUrl: './com-std-draft-view.component.html',
  styleUrls: ['./com-std-draft-view.component.css']
})
export class ComStdDraftViewComponent implements OnInit {
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  tasks: ComJcJustificationDec[] = [];
  public actionRequest: ComJcJustificationDec | undefined;
  blob: Blob;
  constructor(
      private stdComStandardService:StdComStandardService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService
  ) { }

  ngOnInit(): void {
    this.getComSecTasks();
  }

  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
  }
  showToasterError(title:string,message:string){
    this.notifyService.showError(message, title)
  }
  showToasterSuccess(title:string,message:string){
    this.notifyService.showSuccess(message, title)

  }
  public getComSecTasks(): void{
    this.SpinnerService.show();
    this.stdComStandardService.getComSecTasks().subscribe(
        (response: ComJcJustificationDec[])=> {
          this.SpinnerService.hide();
          this.dtTrigger.next();
          this.tasks = response;
        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
          console.log(error.message);
        }
    );
  }
  public onOpenModal(task: ComJcJustificationDec,mode:string): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
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
    this.stdComStandardService.companyDecisionOnDraft(approveDraft).subscribe(
        (response: ComJcJustificationDec) => {
          this.SpinnerService.hide();
          this.showToasterSuccess('Success', `Draft Approved`);
          console.log(response);
          this.getComSecTasks();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Processing Action`);
          console.log(error.message);
          this.getComSecTasks();
          //alert(error.message);
        }
    );
  }
  public onDecisionReject(approveDraft: ApproveDraft): void{
    this.SpinnerService.show();
    this.stdComStandardService.companyDecisionOnDraft(approveDraft).subscribe(
        (response: ComJcJustificationDec) => {
          this.SpinnerService.hide();
          this.showToasterSuccess('Success', `Draft Rejected`);
          console.log(response);
          this.getComSecTasks();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Processing Action`);
          console.log(error.message);
          this.getComSecTasks();
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
            this.showToasterError('Error', `Error Processing Action`);
            console.log(error.message);
        }
    );
  }
}