import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {ReviewProposalComments, StandardReviewTasks} from "../../../../core/store/data/std/std.model";
import {StdReviewService} from "../../../../core/store/data/std/std-review.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {FormBuilder, FormGroup} from "@angular/forms";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-systemic-review-sac-sec',
  templateUrl: './systemic-review-sac-sec.component.html',
  styleUrls: ['./systemic-review-sac-sec.component.css']
})
export class SystemicReviewSacSecComponent implements OnInit {
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
  public isShowRecommendationsTab=true;
  public isShowProposalCommentsTab=true;

  constructor(
      private stdReviewService : StdReviewService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private formBuilder: FormBuilder,
  ) { }

  ngOnInit(): void {
    this.getSacSecTasks();
    this.approve='true';
    this.reject='false';
    this.approveRecommendationFormGroup= this.formBuilder.group({
      proposalId:[],
      summaryOfRecommendations:[],
      feedback:[],
      processId:[],
      taskId:[],
      accentTo:[],
      comments:[],
      reviewID:[],
      taskType:[],
      title:[],
      documentType:[],
      scope:[],
      normativeReference:[],
      symbolsAbbreviatedTerms:[],
      clause:[],
      special:[],
      standardType:[],
    });

    this.rejectRecommendationFormGroup= this.formBuilder.group({
      proposalId:[],
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
  public getSacSecTasks(): void{
    this.SpinnerService.show();
    this.stdReviewService.getSacSecTasks().subscribe(
        (response: StandardReviewTasks[])=> {
          this.SpinnerService.hide();
          this.rerender();
          this.tasks = response;
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
            proposalId: this.actionRequest.taskData.reviewID,
            title:this.actionRequest.taskData.title,
            documentType:this.actionRequest.taskData.documentType,
            scope:this.actionRequest.taskData.scope,
            normativeReference:this.actionRequest.taskData.normativeReference,
            symbolsAbbreviatedTerms:this.actionRequest.taskData.symbolsAbbreviatedTerms,
            clause:this.actionRequest.taskData.clause,
            special:this.actionRequest.taskData.special,
            standardType:this.actionRequest.taskData.standardType


          }
      );
      this.rejectRecommendationFormGroup.patchValue(
          {
            accentTo: this.reject,
            taskId: this.actionRequest.taskId,
            processId: this.actionRequest.processId,
            proposalId: this.actionRequest.taskData.reviewID

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
      this.dtTrigger1.next();
    });

  }
  approveRecommendation(): void {
    this.loadingText = "Approving Recommendation...";
    this.SpinnerService.show();
    this.stdReviewService.levelUpDecisionOnRecommendations(this.approveRecommendationFormGroup.value).subscribe(
        (response ) => {
          console.log(response);
          this.getSacSecTasks();
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
    this.stdReviewService.levelUpDecisionOnRecommendations(this.rejectRecommendationFormGroup.value).subscribe(
        (response ) => {
          console.log(response);
          this.getSacSecTasks();
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
    this.isShowRecommendationsTab = !this.isShowProposalCommentsTab;
    this.isShowProposalCommentsTab= true;
  }

}
