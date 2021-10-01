import { Component, OnInit } from '@angular/core';
import {Subject} from "rxjs";
import {
   ApproveSACJC,
  ComJcJustificationDec
} from "../../../../core/store/data/std/std.model";
import {StdComStandardService} from "../../../../core/store/data/std/std-com-standard.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-com-std-approve-justification',
  templateUrl: './com-std-approve-justification.component.html',
  styleUrls: ['./com-std-approve-justification.component.css']
})
export class ComStdApproveJustificationComponent implements OnInit {
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  tasks: ComJcJustificationDec[] = [];
  public actionRequest: ComJcJustificationDec | undefined;
  constructor(
      private stdComStandardService:StdComStandardService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService
  ) { }

  ngOnInit(): void {
    this.getSacSecTasks();
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
  public getSacSecTasks(): void{
    this.SpinnerService.show();
    this.stdComStandardService.getSacSecTasks().subscribe(
        (response: ComJcJustificationDec[])=> {
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
    if (mode==='approvePd'){
      this.actionRequest=task;
      button.setAttribute('data-target','#approvePd');
    }

    if (mode==='reject'){
      this.actionRequest=task;
      button.setAttribute('data-target','#reject');
    }
    // @ts-ignore
    container.appendChild(button);
    button.click();

  }
  public approveJustification(approveSACJC: ApproveSACJC): void{
    this.SpinnerService.show();
    this.stdComStandardService.decisionOnAppJustification(approveSACJC).subscribe(
        (response: ComJcJustificationDec) => {
          this.SpinnerService.hide();
          this.showToasterSuccess('Success', `Justification Approved`);
          console.log(response);
          this.getSacSecTasks();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Processing Action`);
          console.log(error.message);
          this.getSacSecTasks();
          //alert(error.message);
        }
    );
  }
  public onDecisionReject(approveSACJC: ApproveSACJC): void{
    this.SpinnerService.show();
    this.stdComStandardService.decisionOnAppJustification(approveSACJC).subscribe(
        (response: ComJcJustificationDec) => {
          this.SpinnerService.hide();
          this.showToasterSuccess('Success', `Justification Rejected`);
          console.log(response);
          this.getSacSecTasks();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Processing Action`);
          console.log(error.message);
          this.getSacSecTasks();
          //alert(error.message);
        }
    );
  }

}
