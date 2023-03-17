import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {
  ComStdCommitteeRemarks,
  ComStdRemarks,
  InternationalStandardsComments,
  ISCheckRequirements,
  StakeholderProposalComments
} from "../../../../core/store/data/std/std.model";
import {Store} from "@ngrx/store";
import {Router} from "@angular/router";
import {StdIntStandardService} from "../../../../core/store/data/std/std-int-standard.service";
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {HttpErrorResponse} from "@angular/common/http";
import {DocumentDTO} from "../../../../core/store/data/levy/levy.model";
import {StdComStandardService} from "../../../../core/store/data/std/std-com-standard.service";

@Component({
  selector: 'app-int-std-check-requirements',
  templateUrl: './int-std-check-requirements.component.html',
  styleUrls: ['./int-std-check-requirements.component.css']
})
export class IntStdCheckRequirementsComponent implements OnInit {
  @ViewChildren(DataTableDirective)
  dtElements: QueryList<DataTableDirective>;
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  dtTrigger1: Subject<any> = new Subject<any>();
  dtTrigger2: Subject<any> = new Subject<any>();
  comStdRemarks: ComStdRemarks[] = [];
  stakeholderProposalComments: StakeholderProposalComments[] = [];
  internationalStandardsComments: InternationalStandardsComments[] = [];
  comStdCommitteeRemarks: ComStdCommitteeRemarks[] = [];
  isCheckRequirements:ISCheckRequirements[]=[];
  public actionRequests: ISCheckRequirements | undefined;
  public actionRequest: ISCheckRequirements | undefined;
  approve: string;
  reject: string;
  isShowRemarksTabs= true;
  isShowRemarksTab= true;
  isShowCommentsTab= true;
  isShowCommentsTabs= true;
  isShowMainTab= true;
  isShowMainTabs= true;
  public uploadDraftStandardFormGroup!: FormGroup;
  public approveRequirementsFormGroup!: FormGroup;
  public rejectRequirementsFormGroup!: FormGroup;
  public editDraughtFormGroup!: FormGroup;
  public draughtFormGroup!: FormGroup;
  public proofReadFormGroup!: FormGroup;
  documentDTOs: DocumentDTO[] = [];
  fullname = '';
  blob: Blob;
  public uploadedFiles:  FileList;
  public uploadedFile:  FileList;
  public uploadDrafts:  FileList;
  public uploadProofReads:  FileList;
  public uploadStandardFile:  FileList;
  loadingText: string;

  constructor(
      private store$: Store<any>,
      private router: Router,
      private stdIntStandardService:StdIntStandardService,
      private standardDevelopmentService : StandardDevelopmentService,
      private stdComStandardService:StdComStandardService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private formBuilder: FormBuilder
  ) { }

  ngOnInit(): void {
    this.approve='Yes';
    this.reject='No';
    this.getUploadedDraft();
    this.approveRequirementsFormGroup = this.formBuilder.group({
      comments: ['', Validators.required],
      accentTo: [],
        justificationId:[],
      proposalId:[],
        draftId:[]

    });

    this.rejectRequirementsFormGroup = this.formBuilder.group({
      comments: ['', Validators.required],
      accentTo: [],
        justificationId:[],
      proposalId:[],
        draftId:[]

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

  public getUploadedDraft(): void {
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
  public onOpenModal(iSCheckRequirement: ISCheckRequirements,mode:string): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
    if (mode==='checkRequirementsMet'){
      this.actionRequest=iSCheckRequirement;
      button.setAttribute('data-target','#checkRequirementsMet');

      this.approveRequirementsFormGroup.patchValue(
          {
            accentTo: this.approve,
            proposalId: this.actionRequest.proposalId,
              justificationId: this.actionRequest.justificationNo,
              draftId: this.actionRequest.id
          }
      );
      this.rejectRequirementsFormGroup.patchValue(
          {
            accentTo: this.reject,
            proposalId: this.actionRequest.proposalId,
              justificationId: this.actionRequest.justificationNo,
              draftId: this.actionRequest.id
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

  approveHopJustification(): void {
    this.loadingText = "Approving Draft...";
    this.SpinnerService.show();
    this.stdIntStandardService.decisionOnHopJustification(this.approveRequirementsFormGroup.value).subscribe(
        (response ) => {
          //console.log(response);
          this.getUploadedDraft();
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

  rejectHopJustification(): void {
    this.loadingText = "Rejecting Draft...";
    this.SpinnerService.show();
    this.stdIntStandardService.decisionOnHopJustification(this.rejectRequirementsFormGroup.value).subscribe(
        (response ) => {
          //console.log(response);
          this.getUploadedDraft();
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
}
