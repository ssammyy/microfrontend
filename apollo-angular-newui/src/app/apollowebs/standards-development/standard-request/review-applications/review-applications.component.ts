import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";
import { ReviewApplicationTask} from "../../../../core/store/data/std/std.model";
import {HttpErrorResponse} from "@angular/common/http";
import {DecisionFeedback} from "../../../../core/store/data/std/request_std.model";

@Component({
  selector: 'app-review-applications',
  templateUrl: './review-applications.component.html',
  styleUrls: ['./review-applications.component.css']
})
export class ReviewApplicationsComponent implements OnInit {

  p = 1;
  p2 = 1;
  public tcTasks: ReviewApplicationTask[] = [];
  public actionRequest: ReviewApplicationTask | undefined;

  constructor(private  standardDevelopmentService: StandardDevelopmentService) { }

  ngOnInit(): void {
    this.getApplicationsForReview();
  }

  public getApplicationsForReview(): void {
    this.standardDevelopmentService.getApplicationsForReview().subscribe(
        (response: ReviewApplicationTask[]) => {
          console.log(response);
          this.tcTasks = response;
        },
        (error: HttpErrorResponse) => {
          alert(error.message);
        }
    )
  }

  public onOpenModal(task: ReviewApplicationTask): void {
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

  public decisionOnApplications(decisionFeedback: DecisionFeedback,decision: boolean): void {
    decisionFeedback.status=decision;

    console.log(decisionFeedback);

    this.standardDevelopmentService.decisionOnApplications(decisionFeedback).subscribe(
        (response: DecisionFeedback) => {
          console.log(response);
          this.hideModel();
          this.getApplicationsForReview();
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

