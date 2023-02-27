import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {

    ReviewProposalComments, ReviewStandardsComments, SRStdComments, SRStdForRecommendation,
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
    tasks: SRStdForRecommendation[]=[];
    reviewProposalComments: ReviewProposalComments[]=[];
    srStdComments: SRStdComments[]=[];
    public actionRequest: SRStdForRecommendation | undefined;
    loadingText: string;
    public isShowRecommendationsTab=true;
    public isShowProposalCommentsTab=true;
    public approveRecommendationFormGroup!: FormGroup;
    reviewStandardsComments: ReviewStandardsComments[] = [];

  constructor(
      private stdReviewService : StdReviewService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private formBuilder: FormBuilder,
  ) { }

  ngOnInit(): void {
      this.getStandardsForSpcAction();

    this.approveRecommendationFormGroup= this.formBuilder.group({
        standardType:[],
        requestNumber:[],
        id:[],
        feedback:[],
        accentTo:[],
        remarks:[],
        subject:[],
        description:[],
        title:[]

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
    public getStandardsForSpcAction(): void{
        this.SpinnerService.show();
        this.stdReviewService.getStandardsForSpcAction().subscribe(
            (response: SRStdForRecommendation[])=> {
                this.tasks = response;
                //console.log(this.tasks);
                this.SpinnerService.hide();
                this.rerender();

            },
            (error: HttpErrorResponse)=>{
                this.SpinnerService.hide();
                alert(error.message);
            }
        );
    }

    public getProposalsComments(reviewId: number): void{
        this.SpinnerService.show();
        this.stdReviewService.getProposalsComments(reviewId).subscribe(
            (response: SRStdComments[])=> {
                this.srStdComments = response;
                console.log(this.srStdComments);
                this.SpinnerService.hide();
                this.rerender();

            },
            (error: HttpErrorResponse)=>{
                this.SpinnerService.hide();
                alert(error.message);
            }
        );
    }

    public onOpenModal(StandardReviewTask: SRStdForRecommendation,mode:string): void{
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
                    id: this.actionRequest.id,
                    standardType: this.actionRequest.standardType,
                    requestNumber: this.actionRequest.requestNumber,
                    title: this.actionRequest.title,
                    feedback : this.actionRequest.feedback,
                    subject : this.actionRequest.subject,
                    description : this.actionRequest.description,
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
                //console.log(this.reviewProposalComments)
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
        //console.log(this.recommendationFormGroup.value)
        this.loadingText = "Submitting Decision ...."
        this.SpinnerService.show();
        this.stdReviewService.decisionOnStdDraft(this.approveRecommendationFormGroup.value).subscribe(
            (response) => {
                //console.log(response);
                this.getStandardsForSpcAction();
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

    toggleDisplayCommentsTab(id: number){
        this.loadingText = "Loading ...."
        this.SpinnerService.show();
        this.stdReviewService.getUserComments(id).subscribe(
            (response: ReviewStandardsComments[]) => {
                this.reviewStandardsComments = response;
                this.SpinnerService.hide();
                //console.log(this.reviewStandardsComments)
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                console.log(error.message);
            }
        );


    }

}
