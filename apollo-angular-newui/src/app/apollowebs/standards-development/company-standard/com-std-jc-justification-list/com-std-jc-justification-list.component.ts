import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subject} from "rxjs";
import {NgxSpinnerService} from "ngx-spinner";
import {HttpErrorResponse} from "@angular/common/http";
import {
  ApproveJC,
  ComJcJustification,
  ComJcJustificationList,
  DiSdtDECISION,
  NWADiSdtJustification
} from "../../../../core/store/data/std/std.model";
import {StdComStandardService} from "../../../../core/store/data/std/std-com-standard.service";
import {NotificationService} from "../../../../core/store/data/std/notification.service";

@Component({
  selector: 'app-com-std-jc-justification-list',
  templateUrl: './com-std-jc-justification-list.component.html',
  styleUrls: ['./com-std-jc-justification-list.component.css']
})
export class ComStdJcJustificationListComponent implements OnInit ,OnDestroy{
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  tasks: ComJcJustificationList[] = [];
  public actionRequest: ComJcJustificationList | undefined;
  constructor(
      private stdComStandardService:StdComStandardService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService
  ) { }

  ngOnInit(): void {
    this.getSpcSecTasks();
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
  public getSpcSecTasks(): void{
    this.SpinnerService.show();
    this.stdComStandardService.getSpcSecTasks().subscribe(
        (response: ComJcJustificationList[])=> {
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
  public onOpenModal(task: ComJcJustificationList,mode:string): void{
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

  public approveJustification(approveJC: ApproveJC): void{
    this.SpinnerService.show();
    this.stdComStandardService.decisionOnJustification(approveJC).subscribe(
        (response: ComJcJustificationList) => {
          this.SpinnerService.hide();
          this.showToasterSuccess('Success', `Justification Approved`);
          console.log(response);
          this.getSpcSecTasks();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Processing Action`);
          console.log(error.message);
          this.getSpcSecTasks();
          //alert(error.message);
        }
    );
  }
  public onDecisionReject(approveJC: ApproveJC): void{
    this.SpinnerService.show();
    this.stdComStandardService.decisionOnJustification(approveJC).subscribe(
        (response: ComJcJustificationList) => {
          this.SpinnerService.hide();
          this.showToasterSuccess('Success', `Justification Rejected`);
          console.log(response);
          this.getSpcSecTasks();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Processing Action`);
          console.log(error.message);
          this.getSpcSecTasks();
          //alert(error.message);
        }
    );
  }

}
