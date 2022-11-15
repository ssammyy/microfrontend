import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {
  ReviewProposalComments,
  StakeholderProposalComments,
  StandardReviewTasks
} from "../../../../core/store/data/std/std.model";
import {StdReviewService} from "../../../../core/store/data/std/std-review.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {FormBuilder, FormGroup} from "@angular/forms";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-systemic-review-tc-sec',
  templateUrl: './systemic-review-tc-sec.component.html',
  styleUrls: ['./systemic-review-tc-sec.component.css']
})
export class SystemicReviewTcSecComponent implements OnInit {
  dtOptions: DataTables.Settings = {};
  @ViewChildren(DataTableDirective)
  dtElements: QueryList<DataTableDirective>;
  dtTrigger: Subject<any> = new Subject<any>();
  dtTrigger1: Subject<any> = new Subject<any>();
  tasks: StandardReviewTasks[]=[];
  reviewProposalComments: ReviewProposalComments[]=[];
  public actionRequest: StandardReviewTasks | undefined;
  loadingText: string;
  public isShowRecommendationsTab=true;
  public isShowProposalCommentsTab=true;
  public recommendationFormGroup!: FormGroup;

  constructor(
      private stdReviewService : StdReviewService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private formBuilder: FormBuilder,
  ) { }

  ngOnInit(): void {
    this.getTcSecTasks();
    this.recommendationFormGroup= this.formBuilder.group({
      proposalId:[],
      summaryOfRecommendations:[],
      feedback:[],
      processId:[],
      taskId:[]
    });

  }

  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
    this.dtTrigger1.unsubscribe();
  }
  showToasterSuccess(title:string,message:string){
    this.notifyService.showSuccess(message, title)

  }
  showToasterError(title:string,message:string){
    this.notifyService.showError(message, title)

  }
  public getTcSecTasks(): void{
    this.SpinnerService.show();
    this.stdReviewService.getTcSecTasks().subscribe(
        (response: StandardReviewTasks[])=> {
          this.tasks = response;
          console.log(this.tasks);
          this.SpinnerService.hide();
          this.rerender();

        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }
  public onOpenModal(StandardReviewTask: StandardReviewTasks,mode:string): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
    if (mode==='comment'){
      this.actionRequest=StandardReviewTask;
      button.setAttribute('data-target','#commentModal');
      this.recommendationFormGroup.patchValue(
          {
            taskId: this.actionRequest.taskId,
            proposalID: this.actionRequest.taskData.proposalId
          });

    }

    // @ts-ignore
    container.appendChild(button);
    button.click();

  }

  @ViewChild('closeModalCDetails') private closeModalCDetails: ElementRef | undefined;

  public hideModelCDetails() {
    this.closeModalCDetails?.nativeElement.click();
  }

  rerender(): void {
    this.dtElements.forEach((dtElement: DataTableDirective) => {
      if (dtElement.dtInstance)
        dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
          dtInstance.destroy();
        });
    });
    setTimeout(() => {
      this.dtTrigger.next();
      this.dtTrigger1.next();
    });

  }

  toggleStandardsProposalCommentsTab(proposalId: number){
    this.loadingText = "Loading ...."
    this.SpinnerService.show();
    this.stdReviewService.getStandardsProposalComments(proposalId).subscribe(
        (response: ReviewProposalComments[]) => {
          this.reviewProposalComments = response;
          this.SpinnerService.hide();
          console.log(this.reviewProposalComments)
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          console.log(error.message);
        }
    );
    this.isShowProposalCommentsTab = !this.isShowProposalCommentsTab;
    this.isShowRecommendationsTab= true;

  }

  toggleDisplayRecommendationsTab(){
    this.isShowRecommendationsTab = !this.isShowProposalCommentsTab;
    this.isShowProposalCommentsTab= true;
  }

  submitRecommendation(): void {
    console.log(this.recommendationFormGroup.value)
    this.loadingText = "Submitting Recommendation ...."
    this.SpinnerService.show();
    this.stdReviewService.makeRecommendationsOnAdoptionProposal(this.recommendationFormGroup.value).subscribe(
        (response) => {
          console.log(response);
          this.getTcSecTasks();
          this.SpinnerService.hide();
          this.showToasterSuccess(response.httpStatus, `Recommendation Submitted`);

        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Submitting Recommendation Try Again`);
          console.log(error.message);
        }
    );
    this.hideModelCDetails();

  }

}
