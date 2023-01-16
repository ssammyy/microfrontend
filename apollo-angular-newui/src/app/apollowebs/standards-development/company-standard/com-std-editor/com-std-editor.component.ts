import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {
  ApproveDraft, ComJcJustificationDec,
  COMPreliminaryDraft,
  Department, InternationalStandardsComments,
  ISAdoptionProposal,
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
import {StdComStandardService} from "../../../../core/store/data/std/std-com-standard.service";

@Component({
  selector: 'app-com-std-editor',
  templateUrl: './com-std-editor.component.html',
  styleUrls: ['./com-std-editor.component.css']
})
export class ComStdEditorComponent implements OnInit {
  @ViewChildren(DataTableDirective)
  dtElements: QueryList<DataTableDirective>;
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  dtTrigger1: Subject<any> = new Subject<any>();
  tasks: COMPreliminaryDraft[] = [];
  public actionRequest: COMPreliminaryDraft | undefined;
  public departments !: Department[] ;
  stakeholderProposalComments: StakeholderProposalComments[] = [];
  internationalStandardsComments: InternationalStandardsComments[] = [];
  public isAdoption: ISAdoptionProposal | undefined;
  loadingText: string;
  approve: string;
  reject: string;
  isShowRemarksTab= true;
  isShowCommentsTab= true;
  isShowProposalTab=true;
  public uploadDraftStandardFormGroup!: FormGroup;
  constructor(
      private store$: Store<any>,
      private router: Router,
      private stdComStandardService:StdComStandardService,
      private standardDevelopmentService : StandardDevelopmentService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private formBuilder: FormBuilder
  ) { }

  ngOnInit(): void {
    this.getStdDraftForEditing();

    this.uploadDraftStandardFormGroup = this.formBuilder.group({
      id: [],
      requestId:[],
      title:[],
      scope:[],
      normativeReference:[],
      symbolsAbbreviatedTerms:[],
      clause:[],
      special:[],
      comStdNumber:[],
      preparedBy: [],
      docName:[],
      requestNumber:[]

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
  showToasterWarning(title:string,message:string){
    this.notifyService.showWarning(message, title)

  }
  public getStdDraftForEditing(): void{
    this.SpinnerService.show();
    this.stdComStandardService.getStdDraftForEditing().subscribe(
        (response: COMPreliminaryDraft[])=> {
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

  submitDraftForEditing(): void {
    this.loadingText = "Saving...";
    this.SpinnerService.show();
    this.stdComStandardService.submitDraftForEditing(this.uploadDraftStandardFormGroup.value).subscribe(
        (response ) => {
          console.log(response);
          this.getStdDraftForEditing();
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

  public onOpenModal(task: COMPreliminaryDraft,mode:string): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
    if (mode==='draftStandardEditing') {
      this.actionRequest = task;
      button.setAttribute('data-target', '#draftStandardEditing');
      this.uploadDraftStandardFormGroup.patchValue(
          {
            id: this.actionRequest.id,
            requestId: this.actionRequest.requestId,
            requestNumber: this.actionRequest.requestNumber,
            title: this.actionRequest.title,
            scope:this.actionRequest.scope,
            normativeReference: this.actionRequest.normativeReference,
            symbolsAbbreviatedTerms: this.actionRequest.symbolsAbbreviatedTerms,
            clause:this.actionRequest.clause,
            special:this.actionRequest.special,
            comStdNumber:this.actionRequest.comStdNumber
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
    });
  }

}
