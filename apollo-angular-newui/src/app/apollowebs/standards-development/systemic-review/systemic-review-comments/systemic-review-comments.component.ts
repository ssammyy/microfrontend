import {Component, ElementRef, OnDestroy, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {Subject} from "rxjs";
import {HttpErrorResponse} from "@angular/common/http";
import {NgxSpinnerService} from "ngx-spinner";
import {StdReviewService} from "../../../../core/store/data/std/std-review.service";
import {
  ProposalComments,
  RevProposalComments,
  SRProposalComments,
  StandardReviewComments
} from "../../../../core/store/data/std/std.model";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {DataTableDirective} from "angular-datatables";
import {FormBuilder, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-systemic-review-comments',
  templateUrl: './systemic-review-comments.component.html',
  styleUrls: ['./systemic-review-comments.component.css']
})
export class SystemicReviewCommentsComponent implements OnInit, OnDestroy {

  revProposalComments: SRProposalComments[] = [];
  public actionRequest: SRProposalComments | undefined;
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
    this.getProposal();
    this.commentFormGroup= this.formBuilder.group({
      documentType:[],
      circulationDate:[],
      closingDate:[],
      nameOfTcSecretary:[],
      standardNumber:[],
      title:[],
      choice:[],
      justification:[],
      nameOfRespondent:[],
      positionOfRespondent:[],
      nameOfOrganization:[],
      date:[],
      id:[],
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
  public getProposal(): void{
    this.loadingText = "Loading Proposal ...."
    this.SpinnerService.show();
    this.stdReviewService.getProposal().subscribe(
        (response: SRProposalComments[])=> {
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
  public onOpenModal(revProposalComment: SRProposalComments,mode:string): void{
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
            documentType:this.actionRequest.documentType,
            circulationDate:this.actionRequest.circulationDate,
            closingDate:this.actionRequest.closingDate,
            nameOfTcSecretary:this.actionRequest.tcSecretary,
            standardNumber:this.actionRequest.standardNumber,
            title:this.actionRequest.title,
            id:this.actionRequest.id,
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
          this.getProposal();
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
