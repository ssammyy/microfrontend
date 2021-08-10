import {Component, OnDestroy, OnInit} from '@angular/core';
import {ISAdoptionJustification, ISSacSecTASKS} from "../../../core/store/data/std/std.model";
import {Subject} from "rxjs";
import {StdIntStandardService} from "../../../core/store/data/std/std-int-standard.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../core/store/data/std/notification.service";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-int-std-justification-app',
  templateUrl: './int-std-justification-app.component.html',
  styleUrls: ['./int-std-justification-app.component.css']
})
export class IntStdJustificationAppComponent implements OnInit,OnDestroy {
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  tasks: ISSacSecTASKS[] = [];
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
  public getSACSECTasks(): void{
    this.SpinnerService.show();
    this.stdIntStandardService.getSACSECTasks().subscribe(
        (response: ISSacSecTASKS[])=> {
          this.tasks = response;
          this.SpinnerService.hide();
        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
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
  public onDecision(iSAdoptionJustification: ISAdoptionJustification): void{
    this.SpinnerService.show();
    this.stdIntStandardService.approveStandard(iSAdoptionJustification).subscribe(
        (response: ISAdoptionJustification) => {
          console.log(response);
          this.SpinnerService.hide();
          this.getSACSECTasks();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }

}
