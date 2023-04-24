import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren, ViewEncapsulation} from '@angular/core';
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {
    ComStdCommitteeRemarks,
    ComStdRemarks,
    InternationalStandardsComments,
    ISCheckRequirements,
    StakeholderProposalComments, UsersEntity
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
import swal from "sweetalert2";

@Component({
  selector: 'app-int-std-check-requirements',
  templateUrl: './int-std-check-requirements.component.html',
  styleUrls: ['./int-std-check-requirements.component.css','../../../../../../node_modules/@ng-select/ng-select/themes/default.theme.css'],
  encapsulation: ViewEncapsulation.None
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
    public usersLists !: UsersEntity[] ;
  public uploadDraftStandardFormGroup!: FormGroup;
  public approveRequirementsFormGroup!: FormGroup;
  public rejectRequirementsFormGroup!: FormGroup;
  public editDraughtFormGroup!: FormGroup;
  public draughtFormGroup!: FormGroup;
  public proofReadFormGroup!: FormGroup;
  documentDTOs: DocumentDTO[] = [];
  coverDTOs: DocumentDTO[] = [];
  fullname = '';
  blob: Blob;
    usersList: number;
  public uploadedFiles:  FileList;
  public uploadedFile:  FileList;
  public uploadDrafts:  FileList;
  public uploadProofReads:  FileList;
  public uploadStandardFile:  FileList;
  loadingText: string;
  selectedType: number;
  met: string;
  notMet: string;

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
    this.getStdRequirements();
    this.getEditorDetails();
    this.approveRequirementsFormGroup = this.formBuilder.group({
      id:[],
      comments:null,
      accentTo:null,
      requestId:null,
      draftId:null,
      title:null,
      standardNumber:null,
      scope:null,
      normativeReference:null,
      symbolsAbbreviatedTerms:null,
      clause:null,
      special:null,
      uploadDate:null,
      preparedBy:null,
      documentType:null,
      departmentId:null,
      subject:null,
      description:null,
      contactOneFullName:null,
      contactOneTelephone:null,
      contactOneEmail:null,
      standardType:null,
      companyName:[],
      companyPhone:[],
      docName:[],
      draughting:[],
      requestNumber:[],
        draftStatus:[],
        coverPageStatus:[],
        assignedTo:[]
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

  public getStdRequirements(): void {
    this.loadingText = "Retrieving Drafts...";
    this.SpinnerService.show();
    this.stdComStandardService.getStdRequirements().subscribe(
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
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
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
    if (mode==='checkRequirementsMet'){
      this.actionRequests=iSCheckRequirement;
      button.setAttribute('data-target','#checkRequirementsMet');

      this.approveRequirementsFormGroup.patchValue(
          {

            requestId: this.actionRequests.requestId,
            id: this.actionRequests.id,
            draftId: this.actionRequests.draftId,
            standardType: this.actionRequests.standardType
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
  approveRequirements(): void {
    this.loadingText = "Approving Draft...";
    this.SpinnerService.show();
    this.stdComStandardService.checkRequirements(this.approveRequirementsFormGroup.value).subscribe(
        (response ) => {
          //console.log(response);
          this.getStdRequirements();
          this.SpinnerService.hide();
          if(response.body.responseStatus=="success"){
              this.showToasterSuccess('Approved', response.body.responseMessage);
          }else if(response.body.responseStatus=="error"){
              this.showToasterError('Not Approved', response.body.responseMessage);
          }
            swal.fire({
                title: response.body.responseMsg,
                text: response.body.responseMessage,
                buttonsStyling: false,
                customClass: {
                    confirmButton: response.body.responseButton,
                },
                icon: response.body.responseStatus
            });
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Try Again`);
          console.log(error.message);
        }
    );
    this.hideModalRequirements();
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
          this.getStdRequirements();
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
          this.getStdRequirements();
        }
    );
  }

    public getEditorDetails(): void {
        this.SpinnerService.show();
        this.stdIntStandardService.getEditorDetails().subscribe(
            (response: UsersEntity[]) => {
                this.SpinnerService.hide();
                this.usersLists = response;
               // console.log(this.usersLists);
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                alert(error.message);
            }
        );
    }

}
