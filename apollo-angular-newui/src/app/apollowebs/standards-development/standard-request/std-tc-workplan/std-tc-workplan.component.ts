import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {Subject} from "rxjs";
import {StdTCSecWorkPlan, StdWorkPlan} from "../../../../core/store/data/std/request_std.model";
import {FormBuilder} from "@angular/forms";
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";
import {HttpErrorResponse} from "@angular/common/http";
import {DataTableDirective} from "angular-datatables";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";

@Component({
  selector: 'app-std-tc-workplan',
  templateUrl: './std-tc-workplan.component.html',
  styleUrls: ['./std-tc-workplan.component.css']
})
export class StdTcWorkplanComponent implements OnInit {
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  p = 1;
  p2 = 1;
  public secTasks: StdTCSecWorkPlan[] = [];
  public tscsecRequest !: StdTCSecWorkPlan | undefined;
  @ViewChild(DataTableDirective, {static: false})
  dtElement: DataTableDirective;
  isDtInitialized: boolean = false

  constructor(
      private formBuilder: FormBuilder,
      private  standardDevelopmentService: StandardDevelopmentService,
      private SpinnerService: NgxSpinnerService,
      private notifyService: NotificationService,
  ) {
  }

  ngOnInit(): void {
    this.getTCSECWorkPlan();

  }

  public getTCSECWorkPlan(): void {
    this.standardDevelopmentService.getTCSECWorkPlan().subscribe(
        (response: StdTCSecWorkPlan[]) => {
          console.log(response);
          this.secTasks = response;
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

  public onOpenModal(secTask: StdTCSecWorkPlan, mode: string): void {

    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    if (mode === 'edit') {
      console.log(secTask.taskId)
      this.tscsecRequest = secTask;
      button.setAttribute('data-target', '#updateNWIModal');
    }

    // @ts-ignore
    container.appendChild(button);
    button.click();

  }

  showToasterSuccess(title: string, message: string) {
    this.notifyService.showSuccess(message, title)

  }

  uploadWorkPlan(stdWorkPlan: StdWorkPlan): void {
    this.SpinnerService.show();

    console.log(stdWorkPlan);

    this.standardDevelopmentService.uploadWorkPlan(stdWorkPlan).subscribe(
        (response) => {
          console.log(response);
          this.showToasterSuccess(response.httpStatus, `Your Work plan Has Been Submitted. Prepare A Preliminary Draft`);
          this.SpinnerService.hide();
          this.getTCSECWorkPlan();
          this.hideModel();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();

          alert(error.message);
        }
    )
  }

  @ViewChild('closeModal') private closeModal: ElementRef | undefined;
  public hideModel() {
    this.closeModal?.nativeElement.click();
  }



}
