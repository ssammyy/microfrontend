import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {
  DecisionFeedback,
  ReviewFeedbackFromSPC,
  ReviewFormationOFTCRequest
} from "../../../../core/store/data/std/request_std.model";
import {FormationOfTcService} from "../../../../core/store/data/std/formation-of-tc.service";
import {HttpErrorResponse} from "@angular/common/http";
import {Subject} from "rxjs";
import {DataTableDirective} from "angular-datatables";
import {NgxSpinnerService} from "ngx-spinner";

declare const $: any;

@Component({
  selector: 'app-review-feedback-spc',
  templateUrl: './review-feedback-spc.component.html',
  styleUrls: ['./review-feedback-spc.component.css']
})
export class ReviewFeedbackSPCComponent implements OnInit {
  fullname = '';
  title = 'toaster-not';
  p = 1;
  p2 = 1;
  public tcTasks: ReviewFeedbackFromSPC[] = [];
  public actionRequest: ReviewFeedbackFromSPC | undefined;
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  @ViewChild(DataTableDirective, {static: false})
  dtElement: DataTableDirective;
  isDtInitialized: boolean = false

  constructor(private  standardDevelopmentService: FormationOfTcService, private SpinnerService: NgxSpinnerService,
  ) {
  }

  ngOnInit(): void {
    this.reviewFeedback();
  }

  public reviewFeedback(): void {
    this.standardDevelopmentService.reviewFeedback().subscribe(
        (response: ReviewFeedbackFromSPC[]) => {
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

  public decisionOnSPCFeedback(decisionFeedback: DecisionFeedback, decision: boolean): void {
    console.log(decisionFeedback);

    decisionFeedback.status = decision;

    this.standardDevelopmentService.decisionOnSPCFeedback(decisionFeedback).subscribe(
        (response: DecisionFeedback) => {
          console.log(response);
          this.hideModel();
          this.reviewFeedback();
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
