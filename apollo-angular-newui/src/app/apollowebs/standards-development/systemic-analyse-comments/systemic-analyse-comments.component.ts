import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subject} from "rxjs";
import {StdReviewService} from "../../../core/store/data/std/std-review.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../core/store/data/std/notification.service";
import {StandardReviewRecommendations, SystemicAnalyseComments} from "../../../core/store/data/std/std.model";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-systemic-analyse-comments',
  templateUrl: './systemic-analyse-comments.component.html',
  styleUrls: ['./systemic-analyse-comments.component.css']
})
export class SystemicAnalyseCommentsComponent implements OnInit,OnDestroy {
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  tasks: SystemicAnalyseComments[]=[];
  public actionRequest: SystemicAnalyseComments | undefined;
  constructor(
      private stdReviewService : StdReviewService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService
  ) { }

  ngOnInit(): void {
    this.getReviewTasks();
  }
  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
  }
  public getReviewTasks(): void{
    this.SpinnerService.show();
    this.stdReviewService.getReviewTasks().subscribe(
        (response: SystemicAnalyseComments[])=> {
          this.SpinnerService.hide();
          this.tasks = response;
        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }

  public onOpenModal(task: SystemicAnalyseComments,mode:string): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
    if (mode==='approve'){
      this.actionRequest=task;
      button.setAttribute('data-target','#approveModal');
    }
    if (mode==='reject'){
      this.actionRequest=task;
      button.setAttribute('data-target','#rejectModal');
    }

    // @ts-ignore
    container.appendChild(button);
    button.click();

  }
  public decisionOnRecommendation(standardReviewRecommendations: StandardReviewRecommendations): void{
    this.SpinnerService.show();
    this.stdReviewService.decisionOnRecommendation(standardReviewRecommendations).subscribe(
        (response: StandardReviewRecommendations) => {
          console.log(response);
          this.SpinnerService.hide();
          this.getReviewTasks();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }

}
