import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subject} from "rxjs";
import {NgxSpinnerService} from "ngx-spinner";
import {HttpErrorResponse} from "@angular/common/http";
import {StdIntStandardService} from "../../../../core/store/data/std/std-int-standard.service";
import {
  ISAdoptionJustification, ISAdoptionProposal,
  ISJustificationDecision,
  ISSacSecTASKS
} from "../../../../core/store/data/std/std.model";
import {NotificationService} from "../../../../core/store/data/std/notification.service";

@Component({
  selector: 'app-int-std-justification-app',
  templateUrl: './int-std-justification-app.component.html',
  styleUrls: ['./int-std-justification-app.component.css']
})
export class IntStdJustificationAppComponent implements OnInit,OnDestroy {
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  tasks: ISSacSecTASKS[] = [];
  blob: Blob;
  public actionRequest: ISSacSecTASKS | undefined;
  constructor(
      private stdIntStandardService : StdIntStandardService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService
  ) { }

  ngOnInit(): void {
    this.getSACSECTasks();
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
  public getSACSECTasks(): void{
    this.SpinnerService.show();
    this.stdIntStandardService.getSACSECTasks().subscribe(
        (response: ISSacSecTASKS[])=> {
          this.tasks = response;
          this.SpinnerService.hide();
          this.dtTrigger.next();
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
  public onOpenModal(task: ISSacSecTASKS,mode:string): void{
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
    this.stdIntStandardService.approveStandard(isJustificationDecision).subscribe(
        (response: ISAdoptionProposal) => {
          this.SpinnerService.hide();
          this.showToasterSuccess('Success', `Justification For Standard Approved`);
          console.log(response);
          this.getSACSECTasks();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Try Again`);
          console.log(error.message);
          this.getSACSECTasks();
          //alert(error.message);
        }
    );
  }
  public onDecisionReject(isJustificationDecision: ISJustificationDecision): void{
    this.SpinnerService.show();
    this.stdIntStandardService.approveStandard(isJustificationDecision).subscribe(
        (response: ISAdoptionProposal) => {
          this.SpinnerService.hide();
          this.showToasterSuccess('Success', `Standard Declined`);
          console.log(response);
          this.getSACSECTasks();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Try Again`);
          console.log(error.message);
          this.getSACSECTasks();
          //alert(error.message);
        }
    );
  }

}
