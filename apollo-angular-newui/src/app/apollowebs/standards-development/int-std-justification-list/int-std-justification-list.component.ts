import {Component, OnDestroy, OnInit} from '@angular/core';
import {StdIntStandardService} from "../../../core/store/data/std/std-int-standard.service";
import {StandardDevelopmentService} from "../../../core/store/data/std/standard-development.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../core/store/data/std/notification.service";
import {ISAdoptionJustification, ListJustification} from "../../../core/store/data/std/std.model";
import {HttpErrorResponse} from "@angular/common/http";
import {Subject} from "rxjs";

@Component({
  selector: 'app-int-std-justification-list',
  templateUrl: './int-std-justification-list.component.html',
  styleUrls: ['./int-std-justification-list.component.css']
})
export class IntStdJustificationListComponent implements OnInit, OnDestroy{
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  tasks: ListJustification[] = [];
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
  public getSPCSECTasks(): void{
    this.SpinnerService.show();
    this.stdIntStandardService.getSPCSECTasks().subscribe(
        (response: ListJustification[])=> {
          this.SpinnerService.hide();
          this.tasks = response;
        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
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
  public onDecision(iSAdoptionJustification: ISAdoptionJustification): void{
    this.SpinnerService.show();
    this.stdIntStandardService.decisionOnJustification(iSAdoptionJustification).subscribe(
        (response: ISAdoptionJustification) => {
          console.log(response);
          this.SpinnerService.hide();
          this.getSPCSECTasks();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }

}
