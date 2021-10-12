import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {
  StandardTasks,
  StdJustificationDecision,
  StdSPCSECTask
} from "../../../../core/store/data/std/request_std.model";
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";
import {HttpErrorResponse} from "@angular/common/http";
import {Subject} from "rxjs";

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
  constructor(
      private  standardDevelopmentService: StandardDevelopmentService
  ) {
  }

  ngOnInit(): void {
    this.getSPCSECTasks();
  }

  public getSPCSECTasks(): void {
    this.standardDevelopmentService.getSPCSECTasks().subscribe(
        (response: StdSPCSECTask[]) => {
          console.log(response);
          this.tcTasks = response;
          this.dtTrigger.next();

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

  public decisionOnJustification(stdJustificationDecision: StdJustificationDecision): void {
    console.log(stdJustificationDecision);

    this.standardDevelopmentService.decisionOnJustification(stdJustificationDecision).subscribe(
        (response: StandardTasks) => {
          console.log(response);
          this.hideModel();
          this.getSPCSECTasks();
        },
        (error: HttpErrorResponse) => {
          alert(error.message);
        }
    )
  }

  @ViewChild('closeViewModal') private closeModal: ElementRef | undefined;
  public hideModel() {
    this.closeModal?.nativeElement.click();
  }

}
