import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {StdReviewService} from "../../../../core/store/data/std/std-review.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import { StandardsForReview} from "../../../../core/store/data/std/std.model";
import {HttpErrorResponse} from "@angular/common/http";
import {FormBuilder, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-standards-for-review',
  templateUrl: './standards-for-review.component.html',
  styleUrls: ['./standards-for-review.component.css']
})
export class StandardsForReviewComponent implements OnInit {
  dtOptions: DataTables.Settings = {};
  @ViewChildren(DataTableDirective)
  dtElements: QueryList<DataTableDirective>;
  dtTrigger: Subject<any> = new Subject<any>();
  reviewStandards: StandardsForReview[]=[];
  public actionRequest: StandardsForReview | undefined;
  public proposalFormGroup!: FormGroup;
    loadingText: string;


  constructor(
      private stdReviewService : StdReviewService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private formBuilder: FormBuilder,
  ) { }

  ngOnInit(): void {
    this.getStandardsForReview()

    this.proposalFormGroup= this.formBuilder.group({
      id: [],
      title: [],
      scope: [],
      normativeReference: [],
      symbolsAbbreviatedTerms: [],
      clause: [],
      special: [],
      standardNumber: [],
      standardType: [],
      dateFormed: [],
      documentType: []

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

  public getStandardsForReview(): void{
    this.SpinnerService.show();
    this.stdReviewService.getStandardsForReview().subscribe(
        (response: StandardsForReview[])=> {
          this.SpinnerService.hide();
          this.reviewStandards = response;
            this.rerender();
        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }

    uploadProposal(): void {
        console.log(this.proposalFormGroup.value)
        this.loadingText = "Preparing Proposal ...."
        this.SpinnerService.show();
        this.stdReviewService.standardReviewForm(this.proposalFormGroup.value).subscribe(
            (response) => {
                console.log(response);
                this.getStandardsForReview();
                this.SpinnerService.hide();
                this.showToasterSuccess(response.httpStatus, `Proposal Prepared`);

            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Preparing Proposal Try Again`);
                console.log(error.message);
            }
        );
        this.hideModelCDetails();

    }

  public onOpenModal(reviewStandard: StandardsForReview,mode:string): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
    if (mode==='comment'){
      this.actionRequest=reviewStandard;
      button.setAttribute('data-target','#commentModal');
      this.proposalFormGroup.patchValue(
          {
            title: this.actionRequest.title,
            standardNumber: this.actionRequest.standardNumber,
            documentType: this.actionRequest.documentType,
            scope: this.actionRequest.scope,
            normativeReference: this.actionRequest.normativeReference,
            symbolsAbbreviatedTerms: this.actionRequest.symbolsAbbreviatedTerms,
            clause: this.actionRequest.clause,
            special: this.actionRequest.special,
            standardType: this.actionRequest.standardType

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
        });

    }

}
