import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {DecisionFeedback, ReviewFormationOFTCRequest} from "../../../../core/store/data/std/request_std.model";
import {FormationOfTcService} from "../../../../core/store/data/std/formation-of-tc.service";
import {HttpErrorResponse} from "@angular/common/http";
import {Subject} from "rxjs";
import {DataTableDirective} from "angular-datatables";

@Component({
  selector: 'app-review-justification-of-tc',
  templateUrl: './review-justification-of-tc.component.html',
  styleUrls: ['./review-justification-of-tc.component.css']
})
export class ReviewJustificationOfTCComponent implements OnInit {

  p = 1;
  p2 = 1;
  public tcTasks: ReviewFormationOFTCRequest[] = [];
  public actionRequest: ReviewFormationOFTCRequest | undefined;
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  @ViewChild(DataTableDirective, {static: false})
  dtElement: DataTableDirective;
  isDtInitialized: boolean = false
  constructor(private  standardDevelopmentService: FormationOfTcService) { }

  ngOnInit(): void {
    this.reviewProposal();
  }

  public reviewProposal(): void {
    this.standardDevelopmentService.reviewProposal().subscribe(
        (response: ReviewFormationOFTCRequest[]) => {
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

  public onOpenModal(task: ReviewFormationOFTCRequest): void {
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    console.log(task.taskId);
    this.actionRequest = task;
    button.setAttribute('data-target', '#decisionModal');

    // @ts-ignore
    container.appendChild(button);
    button.click();
  }
  public onOpenSecondModal(task: ReviewFormationOFTCRequest): void {
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    console.log(task.taskId);
    this.actionRequest = task;
    button.setAttribute('data-target', '#secondModal');

    // @ts-ignore
    container.appendChild(button);
    button.click();
  }

  public decisionOnTCProposal(decisionFeedback: DecisionFeedback,decision: boolean): void {
    decisionFeedback.status=decision;

    console.log(decisionFeedback);

    this.standardDevelopmentService.decisionOnTCProposal(decisionFeedback).subscribe(
        (response: DecisionFeedback) => {
          console.log(response);
          this.hideModel();
          this.reviewProposal();
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
