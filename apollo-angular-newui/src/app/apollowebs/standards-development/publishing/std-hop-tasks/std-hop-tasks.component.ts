import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {EditorTask, StdTCDecision} from "../../../../core/store/data/std/request_std.model";
import {PublishingService} from "../../../../core/store/data/std/publishing.service";
import {HttpErrorResponse} from "@angular/common/http";
import {Subject} from "rxjs";
import {DataTableDirective} from "angular-datatables";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {NgxSpinnerService} from "ngx-spinner";

@Component({
  selector: 'app-std-hop-tasks',
  templateUrl: './std-hop-tasks.component.html',
  styleUrls: ['./std-hop-tasks.component.css']
})
export class StdHopTasksComponent implements OnInit {
  p = 1;
  p2 = 1;
  tasks: EditorTask[] = [];
  public actionRequest: EditorTask | undefined;
  public formActionRequest: StdTCDecision | undefined;
  fullname = '';
  title = 'toaster-not';
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  @ViewChild(DataTableDirective, {static: false})
  dtElement: DataTableDirective;
  isDtInitialized: boolean = false
  public itemId: number;
  loadingText: string;


  constructor(private publishingService: PublishingService, private notifyService: NotificationService,
              private SpinnerService: NgxSpinnerService,) {
  }

  ngOnInit(): void {
    this.getHOPTasks();
  }

  public getHOPTasks(): void {
    this.loadingText = "Retrieving Data Please Wait ...."
    this.SpinnerService.show();
    this.publishingService.getHOPTasks().subscribe(
        (response: EditorTask[]) => {
          this.tasks = response;
          console.log(response)
          this.SpinnerService.hide();

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
          this.SpinnerService.hide();
        }
    );
  }

  showToasterSuccess(title: string, message: string) {
    this.notifyService.showSuccess(message, title)

  }

  public onOpenModal(task: EditorTask, mode: string): void {
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    console.log(task.taskId);
    if (mode === 'edit') {
      this.actionRequest = task;
      button.setAttribute('data-target', '#justificationDecisionModal');
    }

    // @ts-ignore
    container.appendChild(button);
    button.click();

  }

  public decisionOnDraft(stdTCDecision: StdTCDecision, decision: string): void {
    this.loadingText = "Submitting Decision On Draft ...."

    this.SpinnerService.show();
    stdTCDecision.decision = decision;
    this.publishingService.decisionOnKSDraft(stdTCDecision).subscribe(
        (response) => {
          console.log(response);
          this.SpinnerService.hide()
          this.showToasterSuccess(response.httpStatus, `Your Draft Has Been Submitted.`);
          this.getHOPTasks();
        },
        (error: HttpErrorResponse) => {
          alert(error.message);
          this.SpinnerService.hide()

        }
    )

  }

  @ViewChild('closeModal') private closeModal: ElementRef | undefined;

  public hideModel() {
    this.closeModal?.nativeElement.click();
  }
}
