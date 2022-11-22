import {Component, ElementRef, OnDestroy, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {Subject} from "rxjs";
import {NgxSpinnerService} from "ngx-spinner";
import {HttpErrorResponse} from "@angular/common/http";
import {StdIntStandardService} from "../../../../core/store/data/std/std-int-standard.service";
import {
    Department,
    InternationalStandardsComments,
    ISAdoptionJustification, ISAdoptionProposal,
    ISJustificationDecision, ISJustificationProposal,
    ISSacSecTASKS, ProposalComments, StakeholderProposalComments
} from "../../../../core/store/data/std/std.model";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {DataTableDirective} from "angular-datatables";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Store} from "@ngrx/store";
import {Router} from "@angular/router";
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";

@Component({
  selector: 'app-int-std-justification-app',
  templateUrl: './int-std-justification-app.component.html',
  styleUrls: ['./int-std-justification-app.component.css']
})
export class IntStdJustificationAppComponent implements OnInit,OnDestroy {
    @ViewChildren(DataTableDirective)
    dtElements: QueryList<DataTableDirective>;
    dtOptions: DataTables.Settings = {};
    dtTrigger: Subject<any> = new Subject<any>();
    dtTrigger1: Subject<any> = new Subject<any>();
    iSJustificationProposals: ISJustificationProposal[] = [];
    public departments !: Department[] ;
    stakeholderProposalComments: StakeholderProposalComments[] = [];
    internationalStandardsComments: InternationalStandardsComments[] = [];
    public actionRequest: ISJustificationProposal | undefined;
    loadingText: string;
    approve: string;
    reject: string;
    isShowRemarksTab= true;
    isShowCommentsTab= true;
    public rejectSpcJustificationFormGroup!: FormGroup;
    public approveSpcJustificationFormGroup!: FormGroup;
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
      this.getISJustification();
      this.approveSpcJustificationFormGroup = this.formBuilder.group({
          comments: ['', Validators.required],
          accentTo: [],
          justificationId:[],
          proposalId:[]

      });

      this.rejectSpcJustificationFormGroup = this.formBuilder.group({
          comments: ['', Validators.required],
          accentTo: [],
          justificationId:[],
          proposalId:[]

      });
      this.approve='Yes';
      this.reject='No';
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
    public getISJustification(): void {
        this.loadingText = "Retrieving Justifications...";
        this.SpinnerService.show();
        this.stdIntStandardService.getISJustification().subscribe(
            (response: ISJustificationProposal[]) => {
                this.iSJustificationProposals = response;
                console.log(this.iSJustificationProposals)
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
    approveSpcJustification(): void {
        this.loadingText = "Approving Justification...";
        this.SpinnerService.show();
        this.stdIntStandardService.decisionOnJustification(this.approveSpcJustificationFormGroup.value).subscribe(
            (response ) => {
                //console.log(response);
                this.getISJustification();
                this.SpinnerService.hide();
                this.showToasterSuccess('Success', `Justification Approved`);
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Try Again`);
                console.log(error.message);
            }
        );
        this.hideModalApproveSpcJustification();
    }

    rejectSpcJustification(): void {
        this.loadingText = "Rejecting Justification...";
        this.SpinnerService.show();
        this.stdIntStandardService.decisionOnJustification(this.rejectSpcJustificationFormGroup.value).subscribe(
            (response ) => {
                //console.log(response);
                this.getISJustification();
                this.SpinnerService.hide();
                this.showToasterSuccess('Success', `Justification Rejected`);
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Try Again`);
                console.log(error.message);
            }
        );
        this.hideModalApproveSpcJustification();
    }
  public onOpenModal(iSJustificationProposal: ISJustificationProposal,mode:string): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
      if (mode==='decisionOnSpcJustification'){
          this.actionRequest=iSJustificationProposal;
          button.setAttribute('data-target','#decisionOnSpcJustification');

          this.approveSpcJustificationFormGroup.patchValue(
              {
                  accentTo: this.approve,
                  proposalId: this.actionRequest.proposalId,
                  justificationId: this.actionRequest.id
              }
          );
          this.rejectSpcJustificationFormGroup.patchValue(
              {
                  accentTo: this.reject,
                  proposalId: this.actionRequest.proposalId,
                  justificationId: this.actionRequest.id
              }
          );
      }
    // @ts-ignore
    container.appendChild(button);
    button.click();

  }



    @ViewChild('closeModalApproveSPCJustification') private closeModalApproveSPCJustification: ElementRef | undefined;

    public hideModalApproveSpcJustification() {
        this.closeModalApproveSPCJustification?.nativeElement.click();
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

}
