import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {
  InternationalStandardsComments,
  ISCheckRequirements,
  StakeholderProposalComments
} from "../../../../core/store/data/std/std.model";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Store} from "@ngrx/store";
import {Router} from "@angular/router";
import {StdIntStandardService} from "../../../../core/store/data/std/std-int-standard.service";
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {HttpErrorResponse} from "@angular/common/http";
import {StdComStandardService} from "../../../../core/store/data/std/std-com-standard.service";

@Component({
  selector: 'app-com-std-publishing',
  templateUrl: './com-std-publishing.component.html',
  styleUrls: ['./com-std-publishing.component.css']
})
export class ComStdPublishingComponent implements OnInit {
    @ViewChildren(DataTableDirective)
  dtElements: QueryList<DataTableDirective>;
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  dtTrigger1: Subject<any> = new Subject<any>();
  stakeholderProposalComments: StakeholderProposalComments[] = [];
  internationalStandardsComments: InternationalStandardsComments[] = [];
  isCheckRequirements:ISCheckRequirements[]=[];
  public actionRequests: ISCheckRequirements | undefined;
  loadingText: string;
  approve: string;
  reject: string;
  isShowRemarksTab= true;
  isShowCommentsTab= true;
  public approveRequirementsFormGroup!: FormGroup;
  public rejectRequirementsFormGroup!: FormGroup;
  public editDraughtFormGroup!: FormGroup;
  public draughtFormGroup!: FormGroup;
  public proofReadFormGroup!: FormGroup;
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
    this.approve='Yes';
    this.reject='No';
    this.getComPublishingTasks();
    this.approveRequirementsFormGroup = this.formBuilder.group({
      comments: ['', Validators.required],
      accentTo: [],
      justificationId:[],
      requestId:[],
      draftId:[]

    });

    this.rejectRequirementsFormGroup = this.formBuilder.group({
      comments: ['', Validators.required],
      accentTo: [],
      justificationId:[],
      requestId:[],
      draftId:[]

    });
    this.editDraughtFormGroup = this.formBuilder.group({
      id: [],
      requestId:[],
      justificationNo:[],
      title:[],
      scope:[],
      normativeReference:[],
      symbolsAbbreviatedTerms:[],
      clause:[],
      special:[],
      docName:[],
      standardNumber:[],
      draughting:[]

    });
    this.draughtFormGroup = this.formBuilder.group({
      id: [],
      requestId:[],
      justificationNo:[],
      title:[],
      scope:[],
      normativeReference:[],
      symbolsAbbreviatedTerms:[],
      clause:[],
      special:[],
      docName:[],
      standardNumber:[]

    });
    this.proofReadFormGroup = this.formBuilder.group({
      id: [],
      requestId:[],
      justificationNo:[],
      title:[],
      scope:[],
      normativeReference:[],
      symbolsAbbreviatedTerms:[],
      clause:[],
      special:[],
      docName:[],
      standardNumber:[]

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

  public getComPublishingTasks(): void {
    this.loadingText = "Retrieving Drafts...";
    this.SpinnerService.show();
    this.stdComStandardService.getComPublishingTasks().subscribe(
        (response: ISCheckRequirements[]) => {
          this.isCheckRequirements = response;
          console.log(this.isCheckRequirements)
          this.rerender();
          this.SpinnerService.hide();

        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
          console.log(error.message);
        }
    );
  }
  toggleDisplayRemarksTab(requestId: number){
    this.loadingText = "Loading ...."
    this.SpinnerService.show();
    this.stdComStandardService.getAllComments(requestId).subscribe(
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

  public onOpenModals(iSCheckRequirement: ISCheckRequirements,mode:string): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
    if (mode==='checkRequirementsMet'){
      this.actionRequests=iSCheckRequirement;
      button.setAttribute('data-target','#checkRequirementsMet');

      this.approveRequirementsFormGroup.patchValue(
          {
            accentTo: this.approve,
            requestId: this.actionRequests.requestId,
            justificationId: this.actionRequests.justificationNo,
            draftId: this.actionRequests.id
          }
      );
      this.rejectRequirementsFormGroup.patchValue(
          {
            accentTo: this.reject,
            requestId: this.actionRequests.requestId,
            justificationId: this.actionRequests.justificationNo,
            draftId: this.actionRequests.id
          }
      );
    }
    if (mode==='draftStandardEditing'){
      this.actionRequests=iSCheckRequirement;
      button.setAttribute('data-target','#draftStandardEditing');

      this.editDraughtFormGroup.patchValue(
          {
            accentTo: this.approve,
            requestId: this.actionRequests.requestId,
            justificationNo: this.actionRequests.justificationNo,
            id: this.actionRequests.id,
            title: this.actionRequests.title,
            scope:this.actionRequests.scope,
            normativeReference:this.actionRequests.normativeReference,
            symbolsAbbreviatedTerms: this.actionRequests.symbolsAbbreviatedTerms,
            clause: this.actionRequests.clause,
            special: this.actionRequests.special,
            docName:this.actionRequests.documentType,
            standardNumber:this.actionRequests.isNumber
          }
      );

    }
    if (mode==='standardDrafting'){
      this.actionRequests=iSCheckRequirement;
      button.setAttribute('data-target','#standardDrafting');

      this.draughtFormGroup.patchValue(
          {
            accentTo: this.approve,
            requestId: this.actionRequests.requestId,
            justificationNo: this.actionRequests.justificationNo,
            id: this.actionRequests.id,
            title: this.actionRequests.title,
            scope:this.actionRequests.scope,
            normativeReference:this.actionRequests.normativeReference,
            symbolsAbbreviatedTerms: this.actionRequests.symbolsAbbreviatedTerms,
            clause: this.actionRequests.clause,
            special: this.actionRequests.special,
            docName:this.actionRequests.documentType,
            standardNumber:this.actionRequests.isNumber
          }
      );

    }
    if (mode==='standardProofreading'){
      this.actionRequests=iSCheckRequirement;
      button.setAttribute('data-target','#standardProofreading');

      this.proofReadFormGroup.patchValue(
          {
            accentTo: this.approve,
            requestId: this.actionRequests.requestId,
            justificationNo: this.actionRequests.justificationNo,
            id: this.actionRequests.id,
            title: this.actionRequests.title,
            scope:this.actionRequests.scope,
            normativeReference:this.actionRequests.normativeReference,
            symbolsAbbreviatedTerms: this.actionRequests.symbolsAbbreviatedTerms,
            clause: this.actionRequests.clause,
            special: this.actionRequests.special,
            docName:this.actionRequests.documentType,
            standardNumber:this.actionRequests.isNumber
          }
      );

    }
    if (mode==='approveChanges'){
      this.actionRequests=iSCheckRequirement;
      button.setAttribute('data-target','#approveChanges');

      this.approveRequirementsFormGroup.patchValue(
          {
            accentTo: this.approve,
            requestId: this.actionRequests.requestId,
            justificationId: this.actionRequests.justificationNo,
            draftId: this.actionRequests.id
          }
      );
      this.rejectRequirementsFormGroup.patchValue(
          {
            accentTo: this.reject,
            requestId: this.actionRequests.requestId,
            justificationId: this.actionRequests.justificationNo,
            draftId: this.actionRequests.id
          }
      );
    }
    // @ts-ignore
    container.appendChild(button);
    button.click();

  }

  @ViewChild('closeModalRequirements') private closeModalRequirements: ElementRef | undefined;

  public hideModalRequirements() {
    this.closeModalRequirements?.nativeElement.click();
  }

  @ViewChild('closeModalDraftEditing') private closeModalDraftEditing: ElementRef | undefined;

  public hideModalDraftEditing() {
    this.closeModalDraftEditing?.nativeElement.click();
  }

  @ViewChild('closeModalDrafting') private closeModalDrafting: ElementRef | undefined;

  public hideModalDrafting() {
    this.closeModalDrafting?.nativeElement.click();
  }

  @ViewChild('closeModalProofReading') private closeModalProofReading: ElementRef | undefined;

  public hideModalProofReading() {
    this.closeModalProofReading?.nativeElement.click();
  }

  @ViewChild('closeModalChanges') private closeModalChanges: ElementRef | undefined;

  public hideModalChanges() {
    this.closeModalChanges?.nativeElement.click();
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
    this.stdComStandardService.checkRequirements(this.approveRequirementsFormGroup.value).subscribe(
        (response ) => {
          //console.log(response);
          this.getComPublishingTasks();
          this.SpinnerService.hide();
          this.showToasterSuccess('Success', `Draft Approved`);
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Try Again`);
          console.log(error.message);
        }
    );
    this.hideModalRequirements();
  }

  rejectRequirements(): void {
    this.loadingText = "Rejecting Draft...";
    this.SpinnerService.show();
    this.stdComStandardService.checkRequirements(this.rejectRequirementsFormGroup.value).subscribe(
        (response ) => {
          //console.log(response);
          this.getComPublishingTasks();
          this.SpinnerService.hide();
          this.showToasterSuccess('Success', `Draft Rejected`);
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Try Again`);
          console.log(error.message);
        }
    );
    this.hideModalRequirements();
  }

  editStandardDraft(): void {
    this.loadingText = "Approving Draft...";
    this.SpinnerService.show();
    this.stdComStandardService.editStandardDraft(this.editDraughtFormGroup.value).subscribe(
        (response ) => {
          //console.log(response);
          this.getComPublishingTasks();
          this.SpinnerService.hide();
          this.showToasterSuccess('Success', `Draft Updated`);
          this.editDraughtFormGroup.reset();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Try Again`);
          console.log(error.message);
        }
    );
    this.hideModalDraftEditing();
  }
  draughtStandard(): void {
    this.loadingText = "Approving Draft...";
    this.SpinnerService.show();
    this.stdComStandardService.draughtStandard(this.draughtFormGroup.value).subscribe(
        (response ) => {
          //console.log(response);
          this.getComPublishingTasks();
          this.SpinnerService.hide();
          this.showToasterSuccess('Success', `Draft Updated`);
          this.editDraughtFormGroup.reset();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Try Again`);
          console.log(error.message);
        }
    );
    this.hideModalDrafting();
  }

  proofReadStandard(): void {
    this.loadingText = "Approving Draft...";
    this.SpinnerService.show();
    this.stdComStandardService.proofReadStandard(this.proofReadFormGroup.value).subscribe(
        (response ) => {
          //console.log(response);
          this.getComPublishingTasks();
          this.SpinnerService.hide();
          this.showToasterSuccess('Success', `Draft Updated`);
          this.editDraughtFormGroup.reset();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Try Again`);
          console.log(error.message);
        }
    );
    this.hideModalProofReading();
  }
  approveProofReadStandard(): void {
    this.loadingText = "Approving Draft...";
    this.SpinnerService.show();
    this.stdComStandardService.approveProofReadStandard(this.approveRequirementsFormGroup.value).subscribe(
        (response ) => {
          //console.log(response);
          this.getComPublishingTasks();
          this.SpinnerService.hide();
          this.showToasterSuccess('Success', `Draft Approved`);
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Try Again`);
          console.log(error.message);
        }
    );
    this.hideModalChanges();
  }


}
