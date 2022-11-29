import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {
  Department,
  InternationalStandardsComments,
  ISAdoptionProposal, ProposalComments,
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

@Component({
  selector: 'app-int-std-approved-proposals',
  templateUrl: './int-std-approved-proposals.component.html',
  styleUrls: ['./int-std-approved-proposals.component.css']
})
export class IntStdApprovedProposalsComponent implements OnInit {
  @ViewChildren(DataTableDirective)
  dtElements: QueryList<DataTableDirective>;
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  dtTrigger1: Subject<any> = new Subject<any>();
  dtTrigger2: Subject<any> = new Subject<any>();
  isAdoptionProposals: ISAdoptionProposal[]=[];
  public actionRequest: ISAdoptionProposal | undefined;
  public prepareJustificationFormGroup!: FormGroup;
  stakeholderProposalComments: StakeholderProposalComments[] = [];
  internationalStandardsComments: InternationalStandardsComments[] = [];
  loadingText: string;
  approve: string;
  reject: string;
  isShowRemarksTab= true;
  isShowCommentsTab= true;
  public departments !: Department[] ;

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
    this.getApprovedProposals();
    this.getDepartments();
    this.prepareJustificationFormGroup = this.formBuilder.group({
      meetingDate: ['', Validators.required],
      knw: ['', Validators.required],
      slNumber: ['', Validators.required],
      requestedBy: [],
      remarks: [],
      status: [],
      department: ['', Validators.required],
      issuesAddressed: ['', Validators.required],
      tcAcceptanceDate: ['', Validators.required],
      uploadedFiles: [],
      edition: [],
      DocDescription: [],
      purposeAndApplication: [],
      intendedUsers: [],
      circulationDate:[],
      closingDate: [],
      positiveVotes:[],
      negativeVotes:[],
      scope:[],
      proposalId:[]
    });
  }
  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
    this.dtTrigger1.unsubscribe();
    this.dtTrigger2.unsubscribe();
  }
  showToasterError(title:string,message:string){
    this.notifyService.showError(message, title)
  }
  showToasterSuccess(title:string,message:string){
    this.notifyService.showSuccess(message, title)

  }
  get formPrepareJustification(): any {
    return this.prepareJustificationFormGroup.controls;
  }

  public getDepartments(): void{
    this.standardDevelopmentService.getDepartments().subscribe(
        (response: Department[])=> {
          this.departments = response;
        },
        (error: HttpErrorResponse)=>{
          alert(error.message);
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
  prepareJustification(): void {
    this.loadingText = "Saving...";
    this.SpinnerService.show();
    this.stdIntStandardService.prepareJustification(this.prepareJustificationFormGroup.value).subscribe(
        (response ) => {
          console.log(response);
          this.getApprovedProposals();
          this.SpinnerService.hide();
          this.showToasterSuccess(response.httpStatus, `Justification Prepared`);

        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Try Again`);
          console.log(error.message);
        }
    );
    this.hideModalPrepareJustification();
  }

  public onOpenModal(isAdoptionProposal: ISAdoptionProposal,mode:string): void {
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');

    if (mode === 'prepareJustification') {
      this.actionRequest = isAdoptionProposal;
      button.setAttribute('data-target', '#prepareJustification');
      this.prepareJustificationFormGroup.patchValue(
          {
            requestedBy: this.actionRequest.tcSecName,
            slNumber: this.actionRequest.proposalNumber,
            scope: this.actionRequest.scope,
            circulationDate: this.actionRequest.circulationDate,
            closingDate: this.actionRequest.closingDate,
            proposalId: this.actionRequest.id

          }
      );

      // @ts-ignore
      container.appendChild(button);
      button.click();

    }
  }

  @ViewChild('closeModalPrepareJustification') private closeModalPrepareJustification: ElementRef | undefined;

  public hideModalPrepareJustification() {
    this.closeModalPrepareJustification?.nativeElement.click();
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
      this.dtTrigger2.next();
    });

  }
  public getApprovedProposals(): void{
    this.SpinnerService.show();
    this.stdIntStandardService.getApprovedProposals().subscribe(
        (response: ISAdoptionProposal[])=> {
          this.SpinnerService.hide();
          this.rerender();
          this.isAdoptionProposals = response;
          console.log(this.isAdoptionProposals);
        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }


}