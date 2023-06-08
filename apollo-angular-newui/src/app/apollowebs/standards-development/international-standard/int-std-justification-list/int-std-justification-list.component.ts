import {Component, OnDestroy, OnInit} from '@angular/core';
import {NgxSpinnerService} from "ngx-spinner";
import {HttpErrorResponse} from "@angular/common/http";
import {Subject} from "rxjs";
import {StdIntStandardService} from "../../../../core/store/data/std/std-int-standard.service";
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";
import {
  ISAdoptionJustification,
  ISAdoptionProposal,
  ISDecision, ISJustificationDecision,
  ListJustification
} from "../../../../core/store/data/std/std.model";
import {NotificationService} from "../../../../core/store/data/std/notification.service";

@Component({
  selector: 'app-int-std-justification-list',
  templateUrl: './int-std-justification-list.component.html',
  styleUrls: ['./int-std-justification-list.component.css']
})
export class IntStdJustificationListComponent implements OnInit, OnDestroy{
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  tasks: ListJustification[] = [];
  blob: Blob;
  public actionRequest: ListJustification | undefined;
  constructor(
      private stdIntStandardService : StdIntStandardService,
      private standardDevelopmentService : StandardDevelopmentService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService
  ) { }

  ngOnInit(): void {
    this.getSPCSECTasks();
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
  public getSPCSECTasks(): void{
    this.SpinnerService.show();
    this.stdIntStandardService.getSPCSECTasks().subscribe(
        (response: ListJustification[])=> {
          this.SpinnerService.hide();
          this.dtTrigger.next();
          this.tasks = response;
        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }
    viewPdfFile(pdfId: number, fileName: string, applicationType: string): void {
        this.SpinnerService.show();
        this.stdIntStandardService.viewJustificationPDF(pdfId).subscribe(
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
                this.showToasterError('Error', `Error opening document`);
                alert(error.message);
            }
        );
    }
  public onOpenModal(task: ListJustification,mode:string): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
    if (mode==='approve'){
      this.actionRequest=task;
      button.setAttribute('data-target','#approveModal');
    }
    if (mode==='reject'){
      this.actionRequest=task;
      button.setAttribute('data-target','#rejectModal');
    }
    // @ts-ignore
    container.appendChild(button);
    button.click();

  }
  // onDecision
  public onDecision(isJustificationDecision: ISJustificationDecision): void{
    this.SpinnerService.show();
    this.stdIntStandardService.decisionOnJustification(isJustificationDecision).subscribe(
        (response: ISAdoptionProposal) => {
          this.SpinnerService.hide();
          this.showToasterSuccess('Success', `Justification Approved`);
          console.log(response);
          this.getSPCSECTasks();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Try Again`);
          console.log(error.message);
          this.getSPCSECTasks();
          //alert(error.message);
        }
    );
  }
  public onDecisionReject(isJustificationDecision: ISJustificationDecision): void{
    this.SpinnerService.show();
    this.stdIntStandardService.decisionOnJustification(isJustificationDecision).subscribe(
        (response: ISAdoptionProposal) => {
          this.SpinnerService.hide();
          this.showToasterSuccess('Success', `Justification Declined`);
          console.log(response);
          this.getSPCSECTasks();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Try Again`);
          console.log(error.message);
          this.getSPCSECTasks();
          //alert(error.message);
        }
    );
  }



}
