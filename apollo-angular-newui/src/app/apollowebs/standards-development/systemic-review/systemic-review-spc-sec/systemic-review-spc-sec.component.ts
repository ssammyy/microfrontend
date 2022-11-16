import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {

    ReviewProposalComments, ReviewStandardsComments,
    StandardReviewTasks
} from "../../../../core/store/data/std/std.model";
import {StdReviewService} from "../../../../core/store/data/std/std-review.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {FormBuilder, FormGroup} from "@angular/forms";
import {HttpErrorResponse} from "@angular/common/http";
import {SiteVisitRemarks} from "../../../../core/store/data/levy/levy.model";

@Component({
  selector: 'app-systemic-review-spc-sec',
  templateUrl: './systemic-review-spc-sec.component.html',
  styleUrls: ['./systemic-review-spc-sec.component.css']
})
export class SystemicReviewSpcSecComponent implements OnInit {
  dtOptions: DataTables.Settings = {};
  @ViewChildren(DataTableDirective)
  dtElements: QueryList<DataTableDirective>;
  dtTrigger: Subject<any> = new Subject<any>();
  dtTrigger1: Subject<any> = new Subject<any>();
  tasks: StandardReviewTasks[]=[];
  public actionRequest: StandardReviewTasks | undefined;
  reviewProposalComments: ReviewProposalComments[]=[];
  loadingText: string;
  public approveRecommendationFormGroup!: FormGroup;
  public rejectRecommendationFormGroup!: FormGroup;
  approve: string;
  reject: string;
  taskTypeOne:number;
  taskTypeZero:number;
  public isShowRecommendationsTab=true;
  public isShowProposalCommentsTab=true;
    reviewStandardsComments: ReviewStandardsComments[] = [];

  constructor(
      private stdReviewService : StdReviewService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private formBuilder: FormBuilder,
  ) { }

  ngOnInit(): void {
    this.getSpcSecTasks();
    this.approve='true';
    this.reject='false';
    this.taskTypeOne=1;
    this.taskTypeZero=0;
    this.approveRecommendationFormGroup= this.formBuilder.group({
      summaryOfRecommendations:[],
      feedback:[],
      processId:[],
      taskId:[],
      accentTo:[],
      comments:[],
      reviewID:[],
      taskType:[]
    });

    this.rejectRecommendationFormGroup= this.formBuilder.group({
      summaryOfRecommendations:[],
      feedback:[],
      processId:[],
      taskId:[],
      accentTo:[],
      comments:[],
      reviewID:[],
      taskType:[]
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
  public getSpcSecTasks(): void{
    this.SpinnerService.show();
    this.stdReviewService.getSpcSecTasks().subscribe(
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
      this.approveRecommendationFormGroup.patchValue(
          {
            accentTo: this.approve,
            taskId: this.actionRequest.taskId,
            processId: this.actionRequest.processId,
              reviewID: this.actionRequest.taskData.reviewID


          }
      );
      this.rejectRecommendationFormGroup.patchValue(
          {
            accentTo: this.reject,
            taskId: this.actionRequest.taskId,
            processId: this.actionRequest.processId,
              reviewID: this.actionRequest.taskData.reviewID

          }
      );

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
    });

  }

  approveRecommendation(): void {
    this.loadingText = "Approving Recommendation...";
    this.SpinnerService.show();
    this.stdReviewService.decisionOnRecommendation(this.approveRecommendationFormGroup.value).subscribe(
        (response ) => {
          console.log(response);
          this.getSpcSecTasks();
          this.SpinnerService.hide();
          this.showToasterSuccess('Success', `Recommendation Approved`);
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Approving Recommendation Try Again`);
          console.log(error.message);
        }
    );
    this.hideModelCDetails();
  }
  rejectRecommendation(): void {
    this.loadingText = "Approving Recommendation...";
    this.SpinnerService.show();
    this.stdReviewService.decisionOnRecommendation(this.rejectRecommendationFormGroup.value).subscribe(
        (response ) => {
          console.log(response);
          this.getSpcSecTasks();
          this.SpinnerService.hide();
          this.showToasterSuccess('Success', `Recommendation Rejected`);
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Rejecting Recommendation Try Again`);
          console.log(error.message);
        }
    );
    this.hideModelCDetails();
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
    this.isShowRecommendationsTab = !this.isShowRecommendationsTab;
    this.isShowProposalCommentsTab= true;
  }


    toggleDisplayCommentsTab(id: number){
        this.loadingText = "Loading ...."
        this.SpinnerService.show();
        this.stdReviewService.getUserComments(id).subscribe(
            (response: ReviewStandardsComments[]) => {
                this.reviewStandardsComments = response;
                this.SpinnerService.hide();
                console.log(this.reviewStandardsComments)
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                console.log(error.message);
            }
        );


    }

}
