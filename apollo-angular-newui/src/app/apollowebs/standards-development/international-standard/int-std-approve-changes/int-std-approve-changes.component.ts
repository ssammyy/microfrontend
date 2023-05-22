import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren, ViewEncapsulation} from '@angular/core';
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {
    ComStdCommitteeRemarks,
    ComStdRemarks,
    InternationalStandardsComments, ISCheckRequirements,
    StakeholderProposalComments, UsersEntity
} from "../../../../core/store/data/std/std.model";
import {FormBuilder, FormGroup} from "@angular/forms";
import {DocumentDTO} from "../../../../core/store/data/levy/levy.model";
import {Store} from "@ngrx/store";
import {Router} from "@angular/router";
import {StdIntStandardService} from "../../../../core/store/data/std/std-int-standard.service";
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";
import {StdComStandardService} from "../../../../core/store/data/std/std-com-standard.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-int-std-approve-changes',
  templateUrl: './int-std-approve-changes.component.html',
  styleUrls: ['./int-std-approve-changes.component.css','../../../../../../node_modules/@ng-select/ng-select/themes/default.theme.css'],
    encapsulation: ViewEncapsulation.None
})
export class IntStdApproveChangesComponent implements OnInit {
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
    coverDTOs: DocumentDTO[] = [];
    selectedOption = '';
    selectedType: number;
    tcSecretary: number;
    public tcSecretaries !: UsersEntity[] ;

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
    this.getStdForApproval();
    this.getTcSecDetails();
    this.approveRequirementsFormGroup = this.formBuilder.group({
      id:[],
      comments:null,
      requestId:null,
      draftId:null,
      title:null,
        accentTo:null,
        assignedTo:null,
        proposalId:null,
        standardType:null,
        draftReviewStatus:null
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

  public getStdForApproval(): void {
    this.loadingText = "Retrieving Drafts...";
    this.SpinnerService.show();
    this.stdComStandardService.getStdForApproval().subscribe(
        (response: ISCheckRequirements[]) => {
          this.isCheckRequirements = response;
          //console.log(this.isCheckRequirements)
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
    this.stdIntStandardService.getDraftComment(proposalId).subscribe(
        (response: ComStdRemarks[]) => {
          this.comStdRemarks = response;
          this.SpinnerService.hide();
          //console.log(this.comStdRemarks)
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
  public onOpenModal(iSCheckRequirement: ISCheckRequirements,mode:string,comStdDraftID: number): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
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
      this.stdComStandardService.getCoverPagesList(comStdDraftID).subscribe(
          (response: DocumentDTO[]) => {
              this.coverDTOs = response;
              this.SpinnerService.hide();
          },
          (error: HttpErrorResponse) => {
              this.SpinnerService.hide();
          }
      );
    if (mode==='approveChanges'){
      this.actionRequests=iSCheckRequirement;
      button.setAttribute('data-target','#approveChanges');

      this.approveRequirementsFormGroup.patchValue(
          {
            requestId: this.actionRequests.requestId,
            id: this.actionRequests.id,
            draftId: this.actionRequests.draftId,
              proposalId: this.actionRequests.proposalId,
              standardType: this.actionRequests.standardType,
              draftReviewStatus: this.actionRequests.draftReviewStatus

          }
      );

    }
    // @ts-ignore
    container.appendChild(button);
    button.click();

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


  toggleDisplayMainTab(){
    this.isShowMainTab = !this.isShowMainTab;
    this.isShowRemarksTab= true;
    this.isShowCommentsTab= true;
    this.isShowMainTabs= true;
  }
  displayDraftComments(draftID: number){
    //this.loadingText = "Loading ...."
    this.SpinnerService.show();
    this.stdComStandardService.getDraftComments(draftID).subscribe(
        (response: ComStdCommitteeRemarks[]) => {
          this.comStdCommitteeRemarks = response;
          this.SpinnerService.hide();
          // console.log(this.comStdCommitteeRemarks)
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
  approveProofReadStandard(): void {
    this.loadingText = "Approving Draft...";
    this.SpinnerService.show();
    this.stdComStandardService.approveProofReadStandard(this.approveRequirementsFormGroup.value).subscribe(
        (response ) => {
          //console.log(response);
          this.getStdForApproval();
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
          this.getStdForApproval();
          //alert(error.message);
        }
    );
  }
    viewCoverPages(pdfId: number, fileName: string, applicationType: string): void {
        this.SpinnerService.show();
        this.stdComStandardService.viewCoverPages(pdfId).subscribe(
            (dataPdf: any) => {
                this.SpinnerService.hide();
                this.blob = new Blob([dataPdf], {type: applicationType});
                let downloadURL = window.URL.createObjectURL(this.blob);
                const link = document.createElement('a');
                link.href = downloadURL;
                link.download = fileName;
                link.click();
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Processing Request`);
                console.log(error.message);
                this.getStdForApproval();
            }
        );
    }

    onSelected(value:string): void {
        this.selectedOption = value;
    }

    public getTcSecDetails(): void {
        this.SpinnerService.show();
        this.stdIntStandardService.getTcSecDetails().subscribe(
            (response: UsersEntity[]) => {
                this.SpinnerService.hide();
                this.tcSecretaries = response;
                // console.log(this.usersLists);
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                alert(error.message);
            }
        );
    }

}
