import {Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";
import {HOFFeedback, StandardTasks, TaskData} from "../../../../core/store/data/std/request_std.model";
import {HttpErrorResponse} from "@angular/common/http";
import {Subject} from "rxjs";
import {DataTableDirective} from "angular-datatables";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {NgxSpinnerService} from "ngx-spinner";
import { MatRadioModule } from '@angular/material/radio';

@Component({
    selector: 'app-standard-task',
    templateUrl: './standard-task.component.html',
    styleUrls: ['./standard-task.component.css']
})
export class StandardTaskComponent implements OnInit {
    dtOptions: DataTables.Settings = {};
    dtTrigger: Subject<any> = new Subject<any>();
    @ViewChild(DataTableDirective, {static: false})
    dtElement: DataTableDirective;
    isDtInitialized: boolean = false
    // data source for the radio buttons:
    seasons: string[] = ['Develop a standard through committee draft', 'Adopt existing International Standard', 'Review existing Kenyan Standard',
        'Development of urgent Kenyan standard', 'Development of publicly available specification', 'Development of national workshop agreement'];

    // selected item
    sdOutput: string;

    // to dynamically (by code) select item
    // from the calling component add:
    @Input() selectSeason: string;

    p = 1;
    p2 = 1;
    countLine = 0;
    tasks: StandardTasks[] = [];
    public actionRequest: StandardTasks | undefined;

    public hofFeedback: HOFFeedback | undefined;

    public technicalName = "";

    public taskData: TaskData | undefined;

    constructor(private standardDevelopmentService: StandardDevelopmentService,
                private notifyService: NotificationService,
                private SpinnerService: NgxSpinnerService,

    ) {
    }

    ngOnInit(): void {
        this.getHOFTasks();
        this.sdOutput = this.selectSeason;


    }

    @ViewChild('closeModal') private closeModal: ElementRef | undefined;
  public hideModel() {
    this.closeModal?.nativeElement.click();
  }

  public getHOFTasks(): void {
    this.standardDevelopmentService.getHOFTasks().subscribe(
        (response: StandardTasks[]) => {
            this.tasks = response;

            if (this.isDtInitialized) {
                this.dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
                    dtInstance.destroy();
                    this.dtTrigger.next();
                });
            } else {
                this.isDtInitialized = true
                this.dtTrigger.next();
            }
            // this.dtTrigger.next();

            console.log(response)
            this.hideModel()
        },
        (error: HttpErrorResponse) => {
          alert(error.message);
        }
    );
  }

  public  getTechnicalCommitteeName(id:number): void {
    this.standardDevelopmentService.getTechnicalCommitteeName(id).subscribe(
        (response: string) => {
          console.log(response)
          this.technicalName = response;
        },
        (error: HttpErrorResponse) => {
          //alert(error.message);
        }
    );
  }

  public onReviewTask(hofFeedback:HOFFeedback): void {
      console.log(hofFeedback);
      this.SpinnerService.show();

      this.standardDevelopmentService.reviewTask(hofFeedback).subscribe(
          (response) => {
            console.log(response);
              this.showToasterSuccess(response.httpStatus, `Your Feedback Has Been Submitted to the TC Secretary.`);
              this.SpinnerService.hide();
            this.getHOFTasks();
          },
          (error: HttpErrorResponse) => {
            alert(error.message);
            this.SpinnerService.hide();

          }
      )
  }

  public onOpenModal(task: StandardTasks, mode: string): void {
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    console.log(task.taskId)
      if (mode === 'edit') {
          this.actionRequest = task;
          button.setAttribute('data-target', '#updateRequestModal');
      }

      // @ts-ignore
      container.appendChild(button);
      button.click();

  }

    showToasterSuccess(title: string, message: string) {
        this.notifyService.showSuccess(message, title)

    }

}

