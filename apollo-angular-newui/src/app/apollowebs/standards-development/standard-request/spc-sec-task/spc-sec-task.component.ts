import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {StdJustificationDecision, StdSPCSECTask} from "../../../../core/store/data/std/request_std.model";
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";
import {HttpErrorResponse} from "@angular/common/http";
import {Subject} from "rxjs";
import {DataTableDirective} from "angular-datatables";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";

@Component({
  selector: 'app-spc-sec-task',
  templateUrl: './spc-sec-task.component.html',
  styleUrls: ['./spc-sec-task.component.css']
})
export class SpcSecTaskComponent implements OnInit {

  p = 1;
  p2 = 1;
  public tcTasks: StdSPCSECTask[] = [];
  public actionRequest: StdSPCSECTask | undefined;
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  @ViewChild(DataTableDirective, {static: false})
  dtElement: DataTableDirective;
  isDtInitialized: boolean = false

  constructor(
      private  standardDevelopmentService: StandardDevelopmentService,
      private SpinnerService: NgxSpinnerService,
      private notifyService: NotificationService,
  ) {
  }

  ngOnInit(): void {
    this.getSPCSECTasks();
  }

  public getSPCSECTasks(): void {
    this.standardDevelopmentService.getSPCSECTasks().subscribe(
        (response: StdSPCSECTask[]) => {
          // console.log(response);
          this.tcTasks = response;
          if (this.isDtInitialized) {
            this.dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
              dtInstance.destroy();
              this.dtTrigger.next();
            });
          } else {
            this.isDtInitialized = true
            this.dtTrigger.next();
          }
        },
        (error: HttpErrorResponse) => {
          alert(error.message);
        }
    )
  }

  public onOpenModal(task: StdSPCSECTask, mode: string): void {
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    console.log(task.taskId)
    if (mode === 'edit') {
      this.actionRequest = task;
      button.setAttribute('data-target', '#justificationDecisionModal');
    }

    if (mode === 'reject') {
      this.actionRequest = task;
      button.setAttribute('data-target', '#rejectDecisionModal');
    }

    // @ts-ignore
    container.appendChild(button);
    button.click();

  }

  showToasterSuccess(title: string, message: string) {
    this.notifyService.showSuccess(message, title)

  }

  public decisionOnJustification(stdJustificationDecision: StdJustificationDecision): void {
    console.log(stdJustificationDecision);
    this.SpinnerService.show();


    this.standardDevelopmentService.decisionOnJustification(stdJustificationDecision).subscribe(
        (response) => {
          console.log(response);
          this.showToasterSuccess(response.httpStatus, `Your Decision Has Been Submitted. A Work plan now needs to be uploaded`);
          this.SpinnerService.hide();
          this.hideModel();
          this.getSPCSECTasks();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();

          alert(error.message);

        }
    )
  }

  @ViewChild('closeViewModal') private closeModal: ElementRef | undefined;
  public hideModel() {
    this.closeModal?.nativeElement.click();
  }

}
