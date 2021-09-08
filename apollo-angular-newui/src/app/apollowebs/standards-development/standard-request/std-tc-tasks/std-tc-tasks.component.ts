import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {StandardTasks, StdTCDecision, StdTCTask, VoteOnNWI} from "../../../../core/store/data/std/request_std.model";
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";
import {HttpErrorResponse} from "@angular/common/http";
import {Subject} from "rxjs";

@Component({
  selector: 'app-std-tc-tasks',
  templateUrl: './std-tc-tasks.component.html',
  styleUrls: ['./std-tc-tasks.component.css']
})
export class StdTcTasksComponent implements OnInit {
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  p = 1;
  p2 = 1;
  public tcTasks: StdTCTask[] = [];

  public stdTCDecisions: StdTCDecision[] = [];
  public formActionRequest: VoteOnNWI | undefined;

  public itemId :string="";
  public filePurposeAnnex: string="FilePurposeAnnex";
  public relevantDocumentsNWI: string="RelevantDocumentsNWI";


  public actionRequest: StdTCTask | undefined;

  constructor(
      private  standardDevelopmentService: StandardDevelopmentService
  ) {
  }

  ngOnInit(): void {
    this.getTCTasks();

  }

  public getTCTasks(): void {
    this.standardDevelopmentService.getTCTasks().subscribe(
        (response: StdTCTask[]) => {
          console.log(response);
          this.tcTasks = response;
          this.dtTrigger.next();


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

  public decisionOnNWI(stdTCDecision : VoteOnNWI): void {

    console.log(stdTCDecision);
    stdTCDecision.userId ="1";
    this.standardDevelopmentService.decisionOnNWI(stdTCDecision).subscribe(
        (response: StandardTasks) => {
          console.log(response);
          this.hideModel()
          this.getTCTasks();
        },
        (error: HttpErrorResponse) => {
          alert(error.message);
        }
    )


  }

  @ViewChild('closeModal') private closeModal: ElementRef | undefined;
  public hideModel() {
    this.closeModal?.nativeElement.click();
  }

}
