import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {
  Department, InternationalStandardsComments,
  ISJustificationProposal,
  StakeholderProposalComments
} from "../../../../core/store/data/std/std.model";
import {FormBuilder, FormGroup} from "@angular/forms";
import {Store} from "@ngrx/store";
import {Router} from "@angular/router";
import {StdIntStandardService} from "../../../../core/store/data/std/std-int-standard.service";
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-int-std-editor',
  templateUrl: './int-std-editor.component.html',
  styleUrls: ['./int-std-editor.component.css']
})
export class IntStdEditorComponent implements OnInit {
  @ViewChildren(DataTableDirective)
  dtElements: QueryList<DataTableDirective>;
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  dtTrigger1: Subject<any> = new Subject<any>();
  iSJustificationProposals: ISJustificationProposal[] = [];
  public departments !: Department[] ;
  stakeholderProposalComments: StakeholderProposalComments[] = [];
  internationalStandardsComments: InternationalStandardsComments[] = [];
  public actionRequest: ISJustificationProposal | undefined;
  loadingText: string;
  approve: string;
  reject: string;
  isShowRemarksTab= true;
  isShowCommentsTab= true;
  public uploadDraftStandardFormGroup!: FormGroup;
  constructor(
      private store$: Store<any>,
      private router: Router,
      private stdIntStandardService:StdIntStandardService,
      private standardDevelopmentService : StandardDevelopmentService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private formBuilder: FormBuilder
  ) { }

  ngOnInit(): void {
    this.getApprovedISJustification();

    this.uploadDraftStandardFormGroup = this.formBuilder.group({
        justificationNo: [],
      proposalId:[],
      title:[],
      scope:[],
      normativeReference:[],
      symbolsAbbreviatedTerms:[],
      clause:[],
      special:[],

    });
  }
  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
    this.dtTrigger1.unsubscribe();
  }
  showToasterError(title:string,message:string){
    this.notifyService.showError(message, title)

  }
  showToasterSuccess(title:string,message:string){
    this.notifyService.showSuccess(message, title)

  }
  public getApprovedISJustification(): void {
    this.loadingText = "Retrieving Justifications...";
    this.SpinnerService.show();
    this.stdIntStandardService.getApprovedISJustification().subscribe(
        (response: ISJustificationProposal[]) => {
          this.iSJustificationProposals = response;
          console.log(this.iSJustificationProposals)
          this.rerender();
          this.SpinnerService.hide();

        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
          console.log(error.message);
        }
    );
  }
  toggleDisplayRemarksTab(proposalId: number){
    this.loadingText = "Loading ...."
    this.SpinnerService.show();
    this.stdIntStandardService.getAllComments(proposalId).subscribe(
        (response: StakeholderProposalComments[]) => {
          this.stakeholderProposalComments = response;
          this.SpinnerService.hide();
          console.log(this.stakeholderProposalComments)
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          console.log(error.message);
        }
    );
    this.isShowRemarksTab = !this.isShowRemarksTab;
    this.isShowCommentsTab= true;

  }
  toggleDisplayCommentsTab(id: number){
    this.loadingText = "Loading ...."
    this.SpinnerService.show();
    this.stdIntStandardService.getUserComments(id).subscribe(
        (response: InternationalStandardsComments[]) => {
          this.internationalStandardsComments = response;
          this.SpinnerService.hide();
          console.log(this.internationalStandardsComments)
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          console.log(error.message);
        }
    );
    this.isShowCommentsTab = !this.isShowCommentsTab;
    this.isShowRemarksTab= true;

  }

    submitDraftForEditing(): void {
    this.loadingText = "Saving...";
    this.SpinnerService.show();
    this.stdIntStandardService.submitDraftForEditing(this.uploadDraftStandardFormGroup.value).subscribe(
        (response ) => {
          console.log(response);
          this.getApprovedISJustification();
          this.SpinnerService.hide();
          this.showToasterSuccess(response.httpStatus, `Draft Prepared`);

        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Try Again`);
          console.log(error.message);
        }
    );
    this.hideModalDraftEditing();
  }

  public onOpenModal(iSJustificationProposal: ISJustificationProposal,mode:string): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
    if (mode==='draftStandardEditing') {
      this.actionRequest = iSJustificationProposal;
      button.setAttribute('data-target', '#draftStandardEditing');
      this.uploadDraftStandardFormGroup.patchValue(
          {
            proposalId: this.actionRequest.proposalId,
              justificationNo: this.actionRequest.id,
            title: this.actionRequest.title,
            scope:this.actionRequest.scope,
            normativeReference: this.actionRequest.normativeReference,
            symbolsAbbreviatedTerms: this.actionRequest.symbolsAbbreviatedTerms,
            clause:this.actionRequest.clause,
            special:this.actionRequest.special
          }
      );

    }
    // @ts-ignore
    container.appendChild(button);
    button.click();

  }
  @ViewChild('closeModalDraftEditing') private closeModalDraftEditing: ElementRef | undefined;

  public hideModalDraftEditing() {
    this.closeModalDraftEditing?.nativeElement.click();
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


}
