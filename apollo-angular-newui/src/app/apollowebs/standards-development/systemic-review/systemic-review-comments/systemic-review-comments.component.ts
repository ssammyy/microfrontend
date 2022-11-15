import {Component, ElementRef, OnDestroy, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {Subject} from "rxjs";
import {HttpErrorResponse} from "@angular/common/http";
import {NgxSpinnerService} from "ngx-spinner";
import {StdReviewService} from "../../../../core/store/data/std/std-review.service";
import {ProposalComments, RevProposalComments, StandardReviewComments} from "../../../../core/store/data/std/std.model";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {DataTableDirective} from "angular-datatables";
import {FormBuilder, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-systemic-review-comments',
  templateUrl: './systemic-review-comments.component.html',
  styleUrls: ['./systemic-review-comments.component.css']
})
export class SystemicReviewCommentsComponent implements OnInit, OnDestroy {

  revProposalComments: RevProposalComments[] = [];
  public actionRequest: RevProposalComments | undefined;
  public commentFormGroup!: FormGroup;
  loadingText: string;
  dtOptions: DataTables.Settings = {};
  @ViewChildren(DataTableDirective)
  dtElements: QueryList<DataTableDirective>;
  dtTrigger: Subject<any> = new Subject<any>();
  constructor(
      private stdReviewService : StdReviewService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private formBuilder: FormBuilder,
  ) { }

  ngOnInit(): void {
    this.getStandardsProposalForComment();
    this.commentFormGroup= this.formBuilder.group({
      adoptionComment:[],
      title:[],
      documentType:[],
      paragraph:[],
      typeOfComment:[],
      proposedChange:[],
      proposalID:[]
    });
  }
  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
  }

  showToasterSuccess(title:string,message:string){
    this.notifyService.showSuccess(message, title)

  }
  showToasterError(title:string,message:string){
    this.notifyService.showError(message, title)

  }
  public getStandardsProposalForComment(): void{
    this.loadingText = "Loading Proposal ...."
    this.SpinnerService.show();
    this.stdReviewService.getStandardsProposalForComment().subscribe(
        (response: RevProposalComments[])=> {
          this.SpinnerService.hide();
          this.rerender();
          this.revProposalComments = response;
        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }
  public onOpenModal(revProposalComment: RevProposalComments,mode:string): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
    if (mode==='comment'){
      this.actionRequest=revProposalComment;
      button.setAttribute('data-target','#commentModal');
      this.commentFormGroup.patchValue(
          {
            title: this.actionRequest.title,
            proposalID: this.actionRequest.id
          });
    }

    // @ts-ignore
    container.appendChild(button);
    button.click();

  }

  public submitComment(): void{
    this.loadingText = "Saving ...."
    this.SpinnerService.show();
    this.stdReviewService.submitAPComments(this.commentFormGroup.value).subscribe(
        (response) => {
          console.log(response);
          this.SpinnerService.hide();
          this.showToasterSuccess(response.httpStatus, `Comment Submitted`);
          this.getStandardsProposalForComment();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Submitting Comment Try Again`);
          alert(error.message);
        }
    );
    this.hideModelCDetails();
    this.commentFormGroup.reset();
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
}
