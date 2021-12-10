import {Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {StdTCDecision, StdTCTask, VoteOnNWI} from "../../../../core/store/data/std/request_std.model";
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";
import {HttpErrorResponse} from "@angular/common/http";
import {Subject} from "rxjs";
import {DataTableDirective} from "angular-datatables";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {selectUserInfo, UserEntityService} from "../../../../core/store";
import {Store} from "@ngrx/store";

@Component({
  selector: 'app-std-tc-tasks',
  templateUrl: './std-tc-tasks.component.html',
  styleUrls: ['./std-tc-tasks.component.css']
})
export class StdTcTasksComponent implements OnInit {
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  @ViewChild(DataTableDirective, {static: false})
  dtElement: DataTableDirective;
  isDtInitialized: boolean = false
  p = 1;
  p2 = 1;
  public tcTasks: StdTCTask[] = [];
  // selected item
  decision: string;
  @Input() selectSeason: string;

  ps: any;
  userId: number = Number('');

  public stdTCDecisions: StdTCDecision[] = [];
  public formActionRequest: VoteOnNWI | undefined;

  public itemId: string = "";
  public filePurposeAnnex: string = "FilePurposeAnnex";
  public relevantDocumentsNWI: string = "RelevantDocumentsNWI";


  public actionRequest: StdTCTask | undefined;

  constructor(
      private  standardDevelopmentService: StandardDevelopmentService,
      private SpinnerService: NgxSpinnerService,
      private notifyService: NotificationService,
      private store$: Store<any>,
      private service: UserEntityService,
  ) {
  }

  ngOnInit(): void {
    this.getTCTasks();
    this.decision = this.selectSeason;
    this.store$.select(selectUserInfo).pipe().subscribe((u) => {
      return this.userId = u.id;
    });


  }

  public getTCTasks(): void {
    this.standardDevelopmentService.getTCTasks().subscribe(
        (response: StdTCTask[]) => {
          console.log(response);
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

  public onOpenModal(task: StdTCTask, mode: string): void {
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    console.log(task.taskData.referenceNumber)

    this.itemId = task.taskData.referenceNumber;
    if (mode === 'edit') {
      this.actionRequest = task;

      button.setAttribute('data-target', '#voteDecisionModal');
    }

    if (mode === 'reject') {
      this.actionRequest = task;
      button.setAttribute('data-target', '#rejectionModal');
    }

    // @ts-ignore
    container.appendChild(button);
    button.click();

  }

  showToasterSuccess(title: string, message: string) {
    this.notifyService.showSuccess(message, title)

  }

  public decisionOnNWI(stdTCDecision: VoteOnNWI): void {
    this.SpinnerService.show();

    console.log(stdTCDecision);
    stdTCDecision.userId = String(this.userId);
    this.standardDevelopmentService.decisionOnNWI(stdTCDecision).subscribe(
        (response) => {
          console.log(response);
          this.showToasterSuccess(response.httpStatus, `Your Vote Has Been Submitted`);
          this.SpinnerService.hide();

          this.hideModel()
          this.getTCTasks();
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
