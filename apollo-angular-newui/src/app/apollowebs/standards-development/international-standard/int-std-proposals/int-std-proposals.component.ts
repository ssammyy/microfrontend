import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {
    CommentOnProposalStakeHolder, ComStdCommitteeRemarks, ComStdRemarks,
    InternationalStandardsComments,
    ISAdoptionProposal,
    StakeholderProposalComments,
    StandardReviewTasks
} from "../../../../core/store/data/std/std.model";
import {FormBuilder, FormGroup} from "@angular/forms";
import {Store} from "@ngrx/store";
import {StdReviewService} from "../../../../core/store/data/std/std-review.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {StdIntStandardService} from "../../../../core/store/data/std/std-int-standard.service";
import {HttpErrorResponse} from "@angular/common/http";
import {DocumentDTO} from "../../../../core/store/data/levy/levy.model";
import {StdComStandardService} from "../../../../core/store/data/std/std-com-standard.service";

@Component({
  selector: 'app-int-std-proposals',
  templateUrl: './int-std-proposals.component.html',
  styleUrls: ['./int-std-proposals.component.css']
})
export class IntStdProposalsComponent implements OnInit {

  @ViewChildren(DataTableDirective)
  dtElements: QueryList<DataTableDirective>;
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  dtTrigger1: Subject<any> = new Subject<any>();
  isAdoptionProposals: ISAdoptionProposal[]=[];
  public actionRequest: ISAdoptionProposal | undefined;
  public approveProposalFormGroup!: FormGroup;
  public rejectProposalFormGroup!: FormGroup;
  stakeholderProposalComments: CommentOnProposalStakeHolder[] = [];
  internationalStandardsComments: InternationalStandardsComments[] = [];
    comStdCommitteeRemarks: ComStdCommitteeRemarks[] = [];
  loadingText: string;
  approve: string;
  reject: string;
  isShowRemarksTab= true;
  isShowCommentsTab= true;
    documentDTOs: DocumentDTO[] = [];
    comStdRemarks: ComStdRemarks[] = [];
    blob: Blob;
    isShowRemarksTabs= true;
    isShowCommentsTabs= true;
    isShowMainTab= true;
    isShowMainTabs= true;

  constructor(
      private store$: Store<any>,
      private stdIntStandardService : StdIntStandardService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private formBuilder: FormBuilder,
      private stdComStandardService:StdComStandardService,
  ) { }

  ngOnInit(): void {
    this.getProposal();
    this.approveProposalFormGroup= this.formBuilder.group({
      accentTo:[],
      proposalId:[],
      comments:[],
        draftId:[],

    });
    this.rejectProposalFormGroup= this.formBuilder.group({
      accentTo:[],
      proposalId:[],
      comments:[]

    });
    this.approve='Yes';
    this.reject='No';
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
  public getProposal(): void{
    this.SpinnerService.show();
    this.stdIntStandardService.getProposal().subscribe(
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
  public onOpenModal(isAdoptionProposal: ISAdoptionProposal,mode:string,comStdDraftID: number): void {
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    if (mode === 'decisionOnProposal') {
      this.actionRequest = isAdoptionProposal;
      button.setAttribute('data-target', '#decisionOnProposal');
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
      this.approveProposalFormGroup.patchValue(
          {
            accentTo: this.approve,
            proposalId: this.actionRequest.id,
              draftId: this.actionRequest.draftId
          }
      );
      this.rejectProposalFormGroup.patchValue(
          {
            accentTo: this.reject,
            proposalId: this.actionRequest.id
          }
      );
    }

    // @ts-ignore
    container.appendChild(button);
    button.click();
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
                this.getProposal();
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


  toggleDisplayCommentsTab(id: number){
    this.loadingText = "Loading ...."
    this.SpinnerService.show();
    this.stdIntStandardService.getUserComments(id).subscribe(
        (response: InternationalStandardsComments[]) => {
          this.internationalStandardsComments = response;
          this.SpinnerService.hide();
          //console.log(this.internationalStandardsComments)
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          console.log(error.message);
        }
    );
    this.isShowCommentsTab = !this.isShowCommentsTab;
    this.isShowRemarksTab= true;

  }
  approveProposal(): void {
    this.loadingText = "Approving Proposal...";
    this.SpinnerService.show();
    this.stdIntStandardService.decisionOnProposal(this.approveProposalFormGroup.value).subscribe(
        (response ) => {
          //console.log(response);
          this.getProposal();
          this.SpinnerService.hide();
          this.showToasterSuccess('Success', `Proposal Approved`);
          this.approveProposalFormGroup.reset();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Try Again`);
          console.log(error.message);
        }
    );
    this.hideModalApproveProposal();
  }

  rejectProposal(): void {
    this.loadingText = "Rejecting Proposal...";
    this.SpinnerService.show();
    this.stdIntStandardService.decisionOnProposal(this.rejectProposalFormGroup.value).subscribe(
        (response ) => {
          //console.log(response);
          this.getProposal();
          this.SpinnerService.hide();
          this.showToasterSuccess('Success', `Proposal Rejected`);
          this.approveProposalFormGroup.reset();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Try Again`);
          console.log(error.message);
        }
    );
    this.hideModalApproveProposal();
  }

  @ViewChild('closeModalApproveProposal') private closeModalApproveProposal: ElementRef | undefined;

  public hideModalApproveProposal() {
    this.closeModalApproveProposal?.nativeElement.click();
  }

}
