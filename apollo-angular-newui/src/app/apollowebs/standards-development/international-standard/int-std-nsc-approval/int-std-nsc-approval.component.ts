import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren, ViewEncapsulation} from '@angular/core';
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {
    ComStdCommitteeRemarks,
    ComStdRemarks,
    InternationalStandardsComments,
    ISCheckRequirements, ISJustificationProposal, MultipleApprovalFields,
    StakeholderProposalComments
} from "../../../../core/store/data/std/std.model";
import {FormBuilder, FormGroup} from "@angular/forms";
import {DocumentDTO} from "../../../../core/store/data/levy/levy.model";
import {PublishingService} from "../../../../core/store/data/std/publishing.service";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {NgxSpinnerService} from "ngx-spinner";
import {CommitteeService} from "../../../../core/store/data/std/committee.service";
import {StdComStandardService} from "../../../../core/store/data/std/std-com-standard.service";
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";
import {StdIntStandardService} from "../../../../core/store/data/std/std-int-standard.service";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-int-std-nsc-approval',
  templateUrl: './int-std-nsc-approval.component.html',
  styleUrls: ['./int-std-nsc-approval.component.css','../../../../../../node_modules/@ng-select/ng-select/themes/default.theme.css'],
    encapsulation: ViewEncapsulation.None
})
export class IntStdNscApprovalComponent implements OnInit {
  @ViewChildren(DataTableDirective)
  dtElements: QueryList<DataTableDirective>;
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  dtTrigger1: Subject<any> = new Subject<any>();
  stakeholderProposalComments: StakeholderProposalComments[] = [];
  internationalStandardsComments: InternationalStandardsComments[] = [];
  isCheckRequirements:ISCheckRequirements[]=[];
    iSJustificationProposals: ISJustificationProposal[] = [];
  public actionRequest: ISCheckRequirements | undefined;
  comStdRemarks: ComStdRemarks[] = [];
  loadingText: string;
    dataSaveResourcesRequired : MultipleApprovalFields;
    dataSaveResourcesRequiredList: MultipleApprovalFields[]=[];
    predefinedDecisionAdded: boolean = false;
    submitted = false;

  approve: string;
  reject: string;
  blob: Blob;
  isShowRemarksTab= true;
  isShowCommentsTab= true;
  isShowMainTab= true;
  isShowMainTabs= true;
    public multipleApproveFormGroup!: FormGroup;
  public approveRequirementsFormGroup!: FormGroup;
  public rejectRequirementsFormGroup!: FormGroup;
  comStdCommitteeRemarks: ComStdCommitteeRemarks[] = [];
  public actionRequests: ISCheckRequirements | undefined;
  documentDTOs: DocumentDTO[] = [];
  coverDTOs: DocumentDTO[] = [];
  selectedType: number;
  constructor(
      private publishingService: PublishingService, private notifyService: NotificationService,
      private SpinnerService: NgxSpinnerService, private committeeService: CommitteeService,
      private stdComStandardService:StdComStandardService,
      private standardDevelopmentService : StandardDevelopmentService,
      private formBuilder: FormBuilder,
      private stdIntStandardService: StdIntStandardService
  ) { }

  ngOnInit(): void {
    this.approve='Yes';
    this.reject='No';
    this.getStdApproval();
      this.multipleApproveFormGroup= this.formBuilder.group({
          accentTo:null,
          draftId:null,
          requestId:null,
          id:null,
          standardType:null
      });
    this.approveRequirementsFormGroup = this.formBuilder.group({
      docName: null,
      title: null,
      scope: null,
      normativeReference: null,
      symbolsAbbreviatedTerms: null,
      clause: null,
      special: null,
      standardType: null,
      comments: null,
      accentTo: null,
      draftId: null,
      requestId: null,
      id: null,

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

  public getStdApproval(): void {
    this.loadingText = "Retrieving Standards...";
    this.SpinnerService.show();
    this.stdComStandardService.getStdApproval().subscribe(
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

  toggleDisplayMainTab(comStdDraftID: number){
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
    this.isShowMainTab = !this.isShowMainTab;
    this.isShowRemarksTab= true;
    this.isShowCommentsTab= true;
    this.isShowMainTabs= true;
  }
  toggleDisplayMainTabs(){
    this.isShowMainTabs = !this.isShowMainTabs;
    this.isShowMainTab= true;
    this.isShowRemarksTab= true;
    this.isShowCommentsTab= true;
  }


  toggleDisplayRemarksTab(requestId: number){
    this.loadingText = "Loading ...."
    this.SpinnerService.show();
    this.stdComStandardService.getAllComments(requestId).subscribe(
        (response: ComStdRemarks[]) => {
          this.comStdRemarks = response;
          this.SpinnerService.hide();
          //console.log(this.comStdRemarks)
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          //console.log(error.message);
        }
    );
    this.isShowCommentsTab = !this.isShowCommentsTab;
    this.isShowRemarksTab= true;
    this.isShowMainTab= true;
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
            proposalId: this.actionRequest.proposalId,
            justificationId: this.actionRequest.justificationNo,
            draftId: this.actionRequest.id,
            title:this.actionRequest.title,
            normativeReference:this.actionRequest.normativeReference,
            symbolsAbbreviatedTerms:this.actionRequest.symbolsAbbreviatedTerms,
            clause:this.actionRequest.clause,
            scope:this.actionRequest.scope,
            special:this.actionRequest.special,
            standardNumber:this.actionRequest.isNumber,
            standardType:this.actionRequest.standardType
          }
      );
      this.rejectRequirementsFormGroup.patchValue(
          {
            accentTo: this.reject,
            proposalId: this.actionRequest.proposalId,
            justificationId: this.actionRequest.justificationNo,
            draftId: this.actionRequest.id,
            title:this.actionRequest.title,
            normativeReference:this.actionRequest.normativeReference,
            symbolsAbbreviatedTerms:this.actionRequest.symbolsAbbreviatedTerms,
            clause:this.actionRequest.clause,
            scope:this.actionRequest.scope,
            special:this.actionRequest.special
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

  approveInternationalStandard(): void {
    this.loadingText = "Approving Draft...";
    this.SpinnerService.show();
    this.stdIntStandardService.approveInternationalStandardNSC(this.approveRequirementsFormGroup.value).subscribe(
        (response ) => {
          //console.log(response);
          this.getStdApproval();
          this.SpinnerService.hide();
          this.showToasterSuccess('Success', `Standard Approved `);
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Try Again`);
          console.log(error.message);
        }
    );
    this.hideModalRequirements();
  }

  rejectInternationalStandard(): void {
    this.loadingText = "Rejecting Draft...";
    this.SpinnerService.show();
    this.stdIntStandardService.approveInternationalStandardNSC(this.rejectRequirementsFormGroup.value).subscribe(
        (response ) => {
          //console.log(response);
          this.getStdApproval();
          this.SpinnerService.hide();
          this.showToasterSuccess('Success', `Standard Rejected`);
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Try Again`);
          console.log(error.message);
        }
    );
    this.hideModalRequirements();
  }
  public onOpenModals(iSCheckRequirement: ISCheckRequirements,mode:string,comStdDraftID: number): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
      this.stdIntStandardService.getISJustification(comStdDraftID).subscribe(
          (response: ISJustificationProposal[]) => {
              this.iSJustificationProposals = response;

          },
          (error: HttpErrorResponse)=>{
              console.log(error.message);
          }
      );


    if (mode==='approveStandard'){
      this.actionRequests=iSCheckRequirement;
      button.setAttribute('data-target','#approveStandard');

      this.approveRequirementsFormGroup.patchValue(
          {
            id: this.actionRequests.id,
            requestId: this.actionRequests.requestId,
            draftId: this.actionRequests.draftId,
            standardNumber:this.actionRequests.comStdNumber,
            standardType:this.actionRequests.standardType
          }
      );
    }
    // @ts-ignore
    container.appendChild(button);
    button.click();

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
          this.getStdApproval();
          //alert(error.message);
        }
    );
  }
  @ViewChild('closeModalEditedDraft') private closeModalEditedDraft: ElementRef | undefined;

  public hideModalEditedDraft() {
    this.closeModalEditedDraft?.nativeElement.click();
  }

    @ViewChild('closeModalMultiple') private closeModalMultiple: ElementRef | undefined;

    public hideModalMultiple() {
        this.closeModalMultiple?.nativeElement.click();
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
          this.getStdApproval();
        }
    );
  }
    onClickAddResource(isCheckRequirement: ISCheckRequirements ): void {
        const dataSaveResourcesRequiredTest=new MultipleApprovalFields;
        dataSaveResourcesRequiredTest.id=isCheckRequirement.id;
        dataSaveResourcesRequiredTest.draftId=isCheckRequirement.draftId
        dataSaveResourcesRequiredTest.standardType=isCheckRequirement.standardType
        dataSaveResourcesRequiredTest.comStdNumber=isCheckRequirement.comStdNumber
        dataSaveResourcesRequiredTest.title=isCheckRequirement.title
        dataSaveResourcesRequiredTest.scope=isCheckRequirement.scope
        dataSaveResourcesRequiredTest.requestId=isCheckRequirement.requestId
        dataSaveResourcesRequiredTest.accentTo=this.multipleApproveFormGroup.get("accentTo").value;
        const valueFound=this.dataSaveResourcesRequiredList.find(t=> t.id===dataSaveResourcesRequiredTest.id );
        if(dataSaveResourcesRequiredTest.accentTo!==null){

            if (valueFound === null || valueFound === undefined) {
                this.dataSaveResourcesRequiredList.push(dataSaveResourcesRequiredTest);
                console.log(this.dataSaveResourcesRequiredList);
            }else{
                const myArray = this.dataSaveResourcesRequiredList;

                const idToRemove = valueFound.id;

                const result = myArray.reduce((accumulator, currentValue) => {
                    if (currentValue.id !== idToRemove) {
                        accumulator.push(currentValue);
                    }
                    return accumulator;
                }, []);

                //console.log(result);
                this.dataSaveResourcesRequiredList=[]
                this.dataSaveResourcesRequiredList.push(...result);
                this.dataSaveResourcesRequiredList.push(dataSaveResourcesRequiredTest)
                //console.log(this.dataSaveResourcesRequiredList);
            }
        }


    }

    removeDataResource(index) {
        console.log(index);
        if (index === 0) {
            this.dataSaveResourcesRequiredList.splice(index, 1);
            this.predefinedDecisionAdded = false
        } else {
            this.dataSaveResourcesRequiredList.splice(index, index);
        }
    }

    onClickMakeDecision() {
        this.submitted = true;
        this.loadingText = "Loading ...."
        this.SpinnerService.show();
        if (this.dataSaveResourcesRequiredList.length > 0) {
            this.stdIntStandardService.onClickMakeNscDecision(this.multipleApproveFormGroup.value,this.dataSaveResourcesRequiredList).subscribe(
                (response) => {
                    this.SpinnerService.hide();
                    this.showToasterSuccess(response.httpStatus, `Multiple Decisions Submitted`);
                    this.getStdApproval();
                },
                (error: HttpErrorResponse) => {
                    this.SpinnerService.hide();
                    this.showToasterError('Error', `Error..Try Again`);
                    alert(error.message);
                }
            );
            this.hideModalMultiple();
        }
    }

}
