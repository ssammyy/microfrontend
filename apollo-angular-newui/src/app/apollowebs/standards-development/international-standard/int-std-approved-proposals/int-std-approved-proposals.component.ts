import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {
  ComStdCommitteeRemarks, ComStdRemarks,
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
import {DocumentDTO} from "../../../../core/store/data/levy/levy.model";
import {StdComStandardService} from "../../../../core/store/data/std/std-com-standard.service";

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
  comStdCommitteeRemarks: ComStdCommitteeRemarks[] = [];
  loadingText: string;
  approve: string;
  reject: string;
  isShowRemarksTab= true;
  isShowCommentsTab= true;
  public departments !: Department[] ;
  documentDTOs: DocumentDTO[] = [];
  comStdRemarks: ComStdRemarks[] = [];
  blob: Blob;
  isShowRemarksTabs= true;
  isShowCommentsTabs= true;
  isShowMainTab= true;
  isShowMainTabs= true;
    public uploadedFiles:  FileList;

  constructor(
      private store$: Store<any>,
      private router: Router,
      private stdIntStandardService:StdIntStandardService,
      private stdComStandardService:StdComStandardService,
      private standardDevelopmentService : StandardDevelopmentService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private formBuilder: FormBuilder
  ) { }

  ngOnInit(): void {
    this.getApprovedProposals();
    this.getDepartments();
    this.prepareJustificationFormGroup = this.formBuilder.group({
        meetingDate: [],
        department: [],
        tcSecName: [],
        standardNumber: [],
        title: [],
        edition: [],
        requestedBy: [],
        scope: [],
        purposeAndApplication: [],
        intendedUsers: [],
        referenceMaterial: [],
        circulationDate: [],
        closingDate: [],
        tcAcceptanceDate: [],
        proposalId: [],
        draftId: [],
      knw: ['', Validators.required],
      slNumber: ['', Validators.required],
      remarks: [],
      status: [],
      issuesAddressed: ['', Validators.required],
      uploadedFiles: [],
      DocDescription: [],
      positiveVotes:[],
      negativeVotes:[],
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

  public onOpenModal(isAdoptionProposal: ISAdoptionProposal,mode:string,comStdDraftID: number): void {
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    this.stdComStandardService.getDraftDocumentList(comStdDraftID).subscribe(
        (response: DocumentDTO[]) => {
          this.documentDTOs = response;
          this.SpinnerService.hide();
          //console.log(this.documentDTOs)
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          //console.log(error.message);
        }
    );

    if (mode === 'prepareJustification') {
      this.actionRequest = isAdoptionProposal;
      button.setAttribute('data-target', '#prepareJustification');
      this.prepareJustificationFormGroup.patchValue(
          {
            requestedBy: this.actionRequest.requesterName,
            slNumber: this.actionRequest.proposalNumber,
            scope: this.actionRequest.scope,
            circulationDate: this.actionRequest.circulationDate,
            closingDate: this.actionRequest.closingDate,
            proposalId: this.actionRequest.id,
              draftId: this.actionRequest.draftId,
              tcSecName:this.actionRequest.tcSecName,
              standardNumber: this.actionRequest.standardNumber,
              title: this.actionRequest.title

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

  viewDraftFile(pdfId: number, fileName: string, applicationType: string): void {
    this.SpinnerService.show();
    this.stdComStandardService.viewCompanyDraft(pdfId).subscribe(
        (dataPdf: any) => {
          this.SpinnerService.hide();
          this.blob = new Blob([dataPdf], {type: applicationType});

          // tslint:disable-next-line:prefer-const
          let downloadURL = window.URL.createObjectURL(this.blob);
          const link = document.createElement('a');
          link.href = downloadURL;
          link.download = fileName;
          link.click();
          // this.pdfUploadsView = dataPdf;
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Processing Request`);
          console.log(error.message);
          this.getApprovedProposals();
          //alert(error.message);
        }
    );
  }


  displayDraftComments(draftID: number){
    //this.loadingText = "Loading ...."
    this.SpinnerService.show();
    this.stdComStandardService.getDraftComments(draftID).subscribe(
        (response: ComStdCommitteeRemarks[]) => {
          this.comStdCommitteeRemarks = response;
          this.SpinnerService.hide();
          console.log(this.comStdCommitteeRemarks)
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          console.log(error.message);
        }
    );
    this.isShowRemarksTab = !this.isShowRemarksTab;
    this.isShowCommentsTab= true;
    this.isShowMainTab= true;
    this.isShowMainTabs= true;

  }

  toggleDisplayRemarksTab(requestId: number){
    this.loadingText = "Loading ...."
    this.SpinnerService.show();
    this.stdIntStandardService.getDraftComment(requestId).subscribe(
        (response: ComStdRemarks[]) => {
          this.comStdRemarks = response;
          this.SpinnerService.hide();
          console.log(this.comStdRemarks)
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          console.log(error.message);
        }
    );
    this.isShowCommentsTab = !this.isShowCommentsTab;
    this.isShowRemarksTab= true;
    this.isShowMainTab= true;
    this.isShowMainTabs= true;

  }


}
