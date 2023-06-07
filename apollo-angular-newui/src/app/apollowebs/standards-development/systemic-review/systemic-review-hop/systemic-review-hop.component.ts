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
  selector: 'app-systemic-review-hop',
  templateUrl: './systemic-review-hop.component.html',
  styleUrls: ['./systemic-review-hop.component.css']
})
export class SystemicReviewHopComponent implements OnInit {
  dtOptions: DataTables.Settings = {};
  @ViewChildren(DataTableDirective)
  dtElements: QueryList<DataTableDirective>;
  dtTrigger: Subject<any> = new Subject<any>();
  dtTrigger1: Subject<any> = new Subject<any>();
  tasks: StandardReviewTasks[]=[];
  public actionRequest: StandardReviewTasks | undefined;
    reviewProposalComments: ReviewProposalComments[]=[];
  loadingText: string;
    public approveFormGroup!: FormGroup;
    public rejectFormGroup!: FormGroup;
    public approveDraftFormGroup!: FormGroup;
    public rejectDraftFormGroup!: FormGroup;
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
    this.getHopTasks();
      this.approve='true';
      this.reject='false';

      this.approveFormGroup= this.formBuilder.group({
          accentTo:[],
          comments:[],
          taskId:[],
          processId:[],
          reviewID:[]

      });

      this.rejectFormGroup= this.formBuilder.group({
          accentTo:[],
          comments:[],
          taskId:[],
          processId:[],
          reviewID:[]

      });

      this.approveDraftFormGroup= this.formBuilder.group({
          accentTo:[],
          comments:[],
          taskId:[],
          processId:[],
          reviewID:[]

      });

      this.rejectDraftFormGroup= this.formBuilder.group({
          accentTo:[],
          comments:[],
          taskId:[],
          processId:[],
          reviewID:[]

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
  public getHopTasks(): void{
    this.SpinnerService.show();
    this.stdReviewService.getHopTasks().subscribe(
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
      this.approveFormGroup.patchValue(
          {

              accentTo:this.approve,
              taskId: this.actionRequest.taskId,
              processId: this.actionRequest.processId,
              reviewID: this.actionRequest.taskData.reviewID,

          });

        this.rejectFormGroup.patchValue(
            {

                accentTo:this.reject,
                taskId: this.actionRequest.taskId,
                processId: this.actionRequest.processId,
                reviewID: this.actionRequest.taskData.reviewID,

            });

        this.approveDraftFormGroup.patchValue(
            {

                accentTo:this.reject,
                taskId: this.actionRequest.taskId,
                processId: this.actionRequest.processId,
                reviewID: this.actionRequest.taskData.reviewID,

            });

        this.rejectDraftFormGroup.patchValue(
            {

                accentTo:this.reject,
                taskId: this.actionRequest.taskId,
                processId: this.actionRequest.processId,
                reviewID: this.actionRequest.taskData.reviewID,

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

    approveRequirements(): void {
        this.loadingText = "Approving Draft...";
        this.SpinnerService.show();
        this.stdReviewService.checkRequirements(this.approveFormGroup.value).subscribe(
            (response ) => {
                console.log(response);
                this.getHopTasks();
                this.SpinnerService.hide();
                this.showToasterSuccess('Success', `Draft Approved`);
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Approving Draft Try Again`);
                console.log(error.message);
            }
        );
        this.hideModelCDetails();
    }
    rejectRequirements(): void {
        this.loadingText = "Rejecting Draft...";
        this.SpinnerService.show();
        this.stdReviewService.checkRequirements(this.rejectFormGroup.value).subscribe(
            (response ) => {
                console.log(response);
                this.getHopTasks();
                this.SpinnerService.hide();
                this.showToasterSuccess('Success', `Draft Rejected`);
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Rejecting Draft Try Again`);
                console.log(error.message);
            }
        );
        this.hideModelCDetails();
    }

    approveStandardDraft(): void {
        this.loadingText = "Approving Draft...";
        this.SpinnerService.show();
        this.stdReviewService.checkStandardDraft(this.approveDraftFormGroup.value).subscribe(
            (response ) => {
                console.log(response);
                this.getHopTasks();
                this.SpinnerService.hide();
                this.showToasterSuccess('Success', `Draft Approved`);
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Approving Draft Try Again`);
                console.log(error.message);
            }
        );
        this.hideModelCDetails();
    }
    rejectStandardDraft(): void {
        this.loadingText = "Rejecting Draft...";
        this.SpinnerService.show();
        this.stdReviewService.checkStandardDraft(this.rejectDraftFormGroup.value).subscribe(
            (response ) => {
                console.log(response);
                this.getHopTasks();
                this.SpinnerService.hide();
                this.showToasterSuccess('Success', `Draft Rejected`);
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Rejecting Draft Try Again`);
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
