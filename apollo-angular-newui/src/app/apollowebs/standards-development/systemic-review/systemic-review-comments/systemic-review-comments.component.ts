import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subject} from "rxjs";
import {HttpErrorResponse} from "@angular/common/http";
import {NgxSpinnerService} from "ngx-spinner";
import {StdReviewService} from "../../../../core/store/data/std/std-review.service";
import {ReviewComments, StandardReviewComments} from "../../../../core/store/data/std/std.model";
import {NotificationService} from "../../../../core/store/data/std/notification.service";

@Component({
  selector: 'app-systemic-review-comments',
  templateUrl: './systemic-review-comments.component.html',
  styleUrls: ['./systemic-review-comments.component.css']
})
export class SystemicReviewCommentsComponent implements OnInit, OnDestroy {
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  tasks: ReviewComments[] = [];
  public actionRequest: ReviewComments | undefined;
  constructor(
      private stdReviewService : StdReviewService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService
  ) { }

  ngOnInit(): void {
    this.getReviewForms();
  }
  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
  }
  public getReviewForms(): void{
    this.SpinnerService.show();
    this.stdReviewService.getReviewForms().subscribe(
        (response: ReviewComments[])=> {
          this.SpinnerService.hide();
          this.tasks = response;
        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }
  public onOpenModal(task: ReviewComments,mode:string): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
    if (mode==='comment'){
      this.actionRequest=task;
      button.setAttribute('data-target','#commentModal');
    }

    // @ts-ignore
    container.appendChild(button);
    button.click();

  }

  public submitComment(standardReviewComments: StandardReviewComments): void{
    this.SpinnerService.show();
    this.stdReviewService.commentsOnReview(standardReviewComments).subscribe(
        (response: StandardReviewComments) => {
          console.log(response);
          this.SpinnerService.hide();
          this.getReviewForms();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }
}
