import {
    Component,
    ElementRef,
    OnDestroy,
    OnInit,
    QueryList,
    ViewChild,
    ViewChildren,
    ViewEncapsulation
} from '@angular/core';
import {Subject} from "rxjs";
import {NgxSpinnerService} from "ngx-spinner";
import {HttpErrorResponse} from "@angular/common/http";
import {StdIntStandardService} from "../../../../core/store/data/std/std-int-standard.service";
import {
    ComStdCommitteeRemarks, ComStdRemarks,
    Department,
    InternationalStandardsComments,
    ISAdoptionProposal, ISJustification,
    ISJustificationProposal,
    MultipleSpcApprovals, StakeholderProposalComments
} from "../../../../core/store/data/std/std.model";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {DataTableDirective} from "angular-datatables";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Store} from "@ngrx/store";
import {Router} from "@angular/router";
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";
import {DocumentDTO} from "../../../../core/store/data/levy/levy.model";
import {StdComStandardService} from "../../../../core/store/data/std/std-com-standard.service";
import swal from "sweetalert2";

@Component({
  selector: 'app-int-std-justification-app',
  templateUrl: './int-std-justification-app.component.html',
  styleUrls: ['./int-std-justification-app.component.css','../../../../../../node_modules/@ng-select/ng-select/themes/default.theme.css'],
    encapsulation: ViewEncapsulation.None
})
export class IntStdJustificationAppComponent implements OnInit,OnDestroy {
    @ViewChildren(DataTableDirective)
    dtElements: QueryList<DataTableDirective>;
    dtOptions: DataTables.Settings = {};
    dtTrigger: Subject<any> = new Subject<any>();
    dtTrigger1: Subject<any> = new Subject<any>();
    dtTrigger2: Subject<any> = new Subject<any>();
    isAdoptionProposals: ISJustification[]=[];
    public actionRequest: ISJustification | undefined;
    public prepareJustificationFormGroup!: FormGroup;
    stakeholderProposalComments: StakeholderProposalComments[] = [];
    internationalStandardsComments: InternationalStandardsComments[] = [];
    comStdCommitteeRemarks: ComStdCommitteeRemarks[] = [];
    iSJustificationProposals: ISJustificationProposal[] = [];
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
    isShowJustificationTabs= true;
    public approveSpcJustificationFormGroup!: FormGroup;
    dataSaveResourcesRequired : MultipleSpcApprovals;
    dataSaveResourcesRequiredList: MultipleSpcApprovals[]=[];
    predefinedDecisionAdded: boolean = false;
    submitted = false;
    public multipleApproveFormGroup!: FormGroup;
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
      this.approveSpcJustificationFormGroup = this.formBuilder.group({
          comments: ['', Validators.required],
          accentTo: [],
          justificationId:[],
          proposalId:[],
          draftId:[],

      });

      this.multipleApproveFormGroup= this.formBuilder.group({
          accentTo:null,
          draftId:null,
          id:null,
          docName:null,
          title:null,
          scope: null,
          circulationDate: null,
          closingDate: null
      });


      this.approve='Yes';
      this.reject='No';
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
    public getISJustification(draftId: number): void {
        this.loadingText = "Retrieving Justifications...";
        this.SpinnerService.show();
        this.stdIntStandardService.getISJustification(draftId).subscribe(
                (response: ISJustificationProposal[]) => {
                this.iSJustificationProposals = response;
                this.SpinnerService.hide();

            },
            (error: HttpErrorResponse)=>{
                this.SpinnerService.hide();
                console.log(error.message);
            }
        );
        this.isShowJustificationTabs = !this.isShowJustificationTabs;
        this.isShowRemarksTab= true;
        this.isShowMainTab= true;
        this.isShowMainTabs= true;
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
        this.isShowJustificationTabs= true;

    }
    approveSpcJustification(): void {
        this.loadingText = "Decision on Justification...";
        this.SpinnerService.show();
        this.stdIntStandardService.decisionOnJustification(this.approveSpcJustificationFormGroup.value).subscribe(
            (response ) => {
                //console.log(response);
                this.getApprovedProposals();
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
        this.hideModalApproveSpcJustification();
    }


    public onOpenModal(isAdoptionProposal: ISJustification,mode:string,comStdDraftID: number): void {
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

        if (mode === 'decisionOnJustification') {
            this.actionRequest = isAdoptionProposal;
            button.setAttribute('data-target', '#decisionOnJustification');
            this.approveSpcJustificationFormGroup.patchValue(
                {
                    requestedBy: this.actionRequest.tcSecName,
                    slNumber: this.actionRequest.id,
                    scope: this.actionRequest.scope,
                    circulationDate: this.actionRequest.circulationDate,
                    closingDate: this.actionRequest.closingDate,
                    proposalId: this.actionRequest.id,
                    draftId: this.actionRequest.draftId,

                }
            );

            // @ts-ignore
            container.appendChild(button);
            button.click();

        }
    }



    @ViewChild('closeModalApproveSPCJustification') private closeModalApproveSPCJustification: ElementRef | undefined;

    public hideModalApproveSpcJustification() {
        this.closeModalApproveSPCJustification?.nativeElement.click();
    }

    @ViewChild('closeModalMultiple') private closeModalMultiple: ElementRef | undefined;

    public hideModalMultiple() {
        this.closeModalMultiple?.nativeElement.click();
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
            }
        );
    }

    public getApprovedProposals(): void{
        this.SpinnerService.show();
        this.stdIntStandardService.getJustification().subscribe(
            (response: ISJustification[])=> {
                this.SpinnerService.hide();
                this.rerender();
                this.isAdoptionProposals = response;
                //console.log(this.isAdoptionProposals);
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
        this.isShowJustificationTabs= true;

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
        this.isShowJustificationTabs= true;

    }

    onClickAddResource(isAdoptionProposal: ISJustification ): void {
        const dataSaveResourcesRequiredTest=new MultipleSpcApprovals;
        dataSaveResourcesRequiredTest.id=isAdoptionProposal.id;
        dataSaveResourcesRequiredTest.draftId=isAdoptionProposal.draftId
        dataSaveResourcesRequiredTest.standardType=isAdoptionProposal.standardType
        dataSaveResourcesRequiredTest.standardNumber=isAdoptionProposal.standardNumber
        dataSaveResourcesRequiredTest.tcSecName=isAdoptionProposal.tcSecName
        dataSaveResourcesRequiredTest.title=isAdoptionProposal.title
        dataSaveResourcesRequiredTest.scope=isAdoptionProposal.scope
        dataSaveResourcesRequiredTest.circulationDate=isAdoptionProposal.circulationDate
        dataSaveResourcesRequiredTest.closingDate=isAdoptionProposal.closingDate
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
            this.stdIntStandardService.onClickMakeSpcDecision(this.multipleApproveFormGroup.value,this.dataSaveResourcesRequiredList).subscribe(
                (response) => {
                    this.SpinnerService.hide();
                    this.showToasterSuccess(response.httpStatus, `Multiple Decisions Submitted`);
                    this.getApprovedProposals();
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
