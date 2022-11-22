import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {
    Department, InternationalStandardsComments,
    ProposalComments,
    StakeholderProposalComments
} from "../../../../core/store/data/std/std.model";
import {HttpErrorResponse} from "@angular/common/http";
import {Subject} from "rxjs";
import {DataTableDirective} from "angular-datatables";
import {Store} from "@ngrx/store";
import {Router} from "@angular/router";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {StdIntStandardService} from "../../../../core/store/data/std/std-int-standard.service";
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";

declare const $: any;

@Component({
  selector: 'app-int-std-tasks',
  templateUrl: './int-std-tasks.component.html',
  styleUrls: ['./int-std-tasks.component.css']
})
export class IntStdTasksComponent implements OnInit {
    @ViewChildren(DataTableDirective)
    dtElements: QueryList<DataTableDirective>;
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  dtTrigger1: Subject<any> = new Subject<any>();
  dtTrigger2: Subject<any> = new Subject<any>();
  dtTrigger3: Subject<any> = new Subject<any>();
  dtTrigger4: Subject<any> = new Subject<any>();
  dtTrigger5: Subject<any> = new Subject<any>();
  dtTrigger7: Subject<any> = new Subject<any>();
  dtTrigger8: Subject<any> = new Subject<any>();
  dtTrigger9: Subject<any> = new Subject<any>();
  dtTrigger10: Subject<any> = new Subject<any>();
  dtTrigger11: Subject<any> = new Subject<any>();
  dtTrigger12: Subject<any> = new Subject<any>();
  tasks: ProposalComments[] = [];
  public departments !: Department[] ;
  stakeholderProposalComments: StakeholderProposalComments[] = [];
    internationalStandardsComments: InternationalStandardsComments[] = [];
  public actionRequest: ProposalComments | undefined;
  loadingText: string;
  approve: string;
  reject: string;
  isShowRemarksTab= true;
  isShowCommentsTab= true;
  public approveProposalFormGroup!: FormGroup;
  public rejectProposalFormGroup!: FormGroup;
  public prepareJustificationFormGroup!: FormGroup;
  public rejectSpcJustificationFormGroup!: FormGroup;
  public approveSpcJustificationFormGroup!: FormGroup;
  public rejectSacJustificationFormGroup!: FormGroup;
  public approveSacJustificationFormGroup!: FormGroup;
  public rejectHopJustificationFormGroup!: FormGroup;
  public approveHopJustificationFormGroup!: FormGroup;
  public uploadDraftStandardFormGroup!: FormGroup;
  public draughtingFormGroup!: FormGroup;
  public proofreadingFormGroup!: FormGroup;
  public internationalStandardFormGroup!: FormGroup;
  public internationalStandardRejectFormGroup!: FormGroup;
  public noticeFormGroup!: FormGroup;
  public gazetteFormGroup!: FormGroup;

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
    this.getUserTasks();
    this.getDepartments();
    this.approve='true';
    this.reject='false';

    this.approveProposalFormGroup = this.formBuilder.group({
      comments: ['', Validators.required],
      taskId: [],
      processId:[],
      accentTo: [],
        approvalID:[]

    });

    this.rejectProposalFormGroup = this.formBuilder.group({
      comments: ['', Validators.required],
      taskId: [],
      processId:[],
      accentTo: [],
        approvalID:[]

    });

      this.approveSacJustificationFormGroup = this.formBuilder.group({
          comments: ['', Validators.required],
          taskId: [],
          processId:[],
          accentTo: [],
          approvalID:[]

      });

      this.rejectSacJustificationFormGroup = this.formBuilder.group({
          comments: ['', Validators.required],
          taskId: [],
          processId:[],
          accentTo: [],
          approvalID:[]

      });

      this.approveSpcJustificationFormGroup = this.formBuilder.group({
          comments: ['', Validators.required],
          taskId: [],
          processId:[],
          accentTo: [],
          approvalID:[],
          proposalId:[]

      });

      this.rejectSpcJustificationFormGroup = this.formBuilder.group({
          comments: ['', Validators.required],
          taskId: [],
          processId:[],
          accentTo: [],
          approvalID:[]

      });

      this.approveHopJustificationFormGroup = this.formBuilder.group({
          comments: ['', Validators.required],
          taskId: [],
          processId:[],
          accentTo: [],
          approvalID:[]

      });

      this.rejectHopJustificationFormGroup = this.formBuilder.group({
          comments: ['', Validators.required],
          taskId: [],
          processId:[],
          accentTo: [],
          approvalID:[]

      });

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
          processId:[],
          taskId:[],
          scope:[]
      });
      this.uploadDraftStandardFormGroup = this.formBuilder.group({
          taskId: [],
          processId:[],
          title:[],
          scope:[],
          normativeReference:[],
          symbolsAbbreviatedTerms:[],
          clause:[],
          special:[],

      });

      this.draughtingFormGroup = this.formBuilder.group({
          taskId: [],
          processId:[],
          id:[],
          title:[],
          scope:[],
          normativeReference:[],
          symbolsAbbreviatedTerms:[],
          clause:[],
          special:[],

      });

      this.proofreadingFormGroup = this.formBuilder.group({
          taskId: [],
          processId:[],
          id:[],
          title:[],
          scope:[],
          normativeReference:[],
          symbolsAbbreviatedTerms:[],
          clause:[],
          special:[],

      });

      this.internationalStandardFormGroup = this.formBuilder.group({
          taskId: [],
          processId:[],
          accentTo:[],
          approvalID:[],
          title:[],
          scope:[],
          normativeReference:[],
          symbolsAbbreviatedTerms:[],
          clause:[],
          special:[],
          id:[]

      });
      this.internationalStandardRejectFormGroup = this.formBuilder.group({
          taskId: [],
          processId:[],
          accentTo:[],
          approvalID:[],
          title:[],
          scope:[],
          normativeReference:[],
          symbolsAbbreviatedTerms:[],
          clause:[],
          special:[],
          id:[]

      });
      this.noticeFormGroup = this.formBuilder.group({
          taskId: [],
          processId:[],
          iSNumber:[],
          description:[]

      });
      this.gazetteFormGroup = this.formBuilder.group({
          taskId: [],
          processId:[],
          iSNumber:[],
          description:[]

      });


  }

  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
    this.dtTrigger1.unsubscribe();
    this.dtTrigger2.unsubscribe();
    this.dtTrigger3.unsubscribe();
    this.dtTrigger4.unsubscribe();
    this.dtTrigger5.unsubscribe();
    this.dtTrigger7.unsubscribe();
    this.dtTrigger8.unsubscribe();
    this.dtTrigger9.unsubscribe();
    this.dtTrigger10.unsubscribe();
    this.dtTrigger11.unsubscribe();
    this.dtTrigger12.unsubscribe();
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

  public getUserTasks(): void {
    this.loadingText = "Retrieving Tasks...";
    this.SpinnerService.show();
    this.stdIntStandardService.getUserTasks().subscribe(
        (response: ProposalComments[]) => {
          this.tasks = response;
          console.log(this.tasks)
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
  approveProposal(): void {
    this.loadingText = "Approving Proposal...";
    this.SpinnerService.show();
    this.stdIntStandardService.decisionOnProposal(this.approveProposalFormGroup.value).subscribe(
        (response ) => {
          //console.log(response);
          this.getUserTasks();
          this.SpinnerService.hide();
          this.showToasterSuccess('Success', `Proposal Approved`);
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
          this.getUserTasks();
          this.SpinnerService.hide();
          this.showToasterSuccess('Success', `Proposal Rejected`);
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Try Again`);
          console.log(error.message);
        }
    );
    this.hideModalApproveProposal();
  }
    prepareJustification(): void {
        this.loadingText = "Saving...";
        this.SpinnerService.show();
        this.stdIntStandardService.prepareJustification(this.prepareJustificationFormGroup.value).subscribe(
            (response ) => {
                console.log(response);
                this.getUserTasks();
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

    approveSpcJustification(): void {
        this.loadingText = "Approving Justification...";
        this.SpinnerService.show();
        this.stdIntStandardService.decisionOnJustification(this.approveSpcJustificationFormGroup.value).subscribe(
            (response ) => {
                //console.log(response);
                this.getUserTasks();
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
                this.getUserTasks();
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
    approveSacJustification(): void {
        this.loadingText = "Approving Justification...";
        this.SpinnerService.show();
        this.stdIntStandardService.decisionOnSACJustification(this.approveSacJustificationFormGroup.value).subscribe(
            (response ) => {
                //console.log(response);
                this.getUserTasks();
                this.SpinnerService.hide();
                this.showToasterSuccess('Success', `Justification Approved`);
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Try Again`);
                console.log(error.message);
            }
        );
        this.hideModalApproveSacJustification();
    }

    rejectSacJustification(): void {
        this.loadingText = "Rejecting Justification...";
        this.SpinnerService.show();
        this.stdIntStandardService.decisionOnSACJustification(this.rejectSacJustificationFormGroup.value).subscribe(
            (response ) => {
                //console.log(response);
                this.getUserTasks();
                this.SpinnerService.hide();
                this.showToasterSuccess('Success', `Justification Rejected`);
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Try Again`);
                console.log(error.message);
            }
        );
        this.hideModalApproveSacJustification();
    }

    approveHopJustification(): void {
        this.loadingText = "Approving Justification...";
        this.SpinnerService.show();
        this.stdIntStandardService.decisionOnHopJustification(this.approveHopJustificationFormGroup.value).subscribe(
            (response ) => {
                //console.log(response);
                this.getUserTasks();
                this.SpinnerService.hide();
                this.showToasterSuccess('Success', `Justification Approved`);
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
        this.loadingText = "Rejecting Justification...";
        this.SpinnerService.show();
        this.stdIntStandardService.decisionOnHopJustification(this.rejectHopJustificationFormGroup.value).subscribe(
            (response ) => {
                //console.log(response);
                this.getUserTasks();
                this.SpinnerService.hide();
                this.showToasterSuccess('Success', `Justification Rejected`);
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
        this.loadingText = "Saving...";
        this.SpinnerService.show();
        this.stdIntStandardService.editStandardDraft(this.uploadDraftStandardFormGroup.value).subscribe(
            (response ) => {
                console.log(response);
                this.getUserTasks();
                this.SpinnerService.hide();
                this.showToasterSuccess(response.httpStatus, `Draft Prepared`);

            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Try Again`);
                console.log(error.message);
            }
        );
        this.hideModalDraftEditing();
    }

    draughting(): void {
        this.loadingText = "Saving...";
        this.SpinnerService.show();
        this.stdIntStandardService.draughtStandardDraft(this.draughtingFormGroup.value).subscribe(
            (response ) => {
                console.log(response);
                this.getUserTasks();
                this.SpinnerService.hide();
                this.showToasterSuccess(response.httpStatus, `Draft Prepared`);

            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Try Again`);
                console.log(error.message);
            }
        );
        this.hideModalDrafting();
    }

    proofreading(): void {
        this.loadingText = "Saving...";
        this.SpinnerService.show();
        this.stdIntStandardService.proofreading(this.proofreadingFormGroup.value).subscribe(
            (response ) => {
                console.log(response);
                this.getUserTasks();
                this.SpinnerService.hide();
                this.showToasterSuccess(response.httpStatus, `Draft Prepared`);

            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Try Again`);
                console.log(error.message);
            }
        );
        this.hideModalDrafting();
    }
    uploadIStandard(): void {
        this.loadingText = "Saving...";
        this.SpinnerService.show();
        this.stdIntStandardService.uploadIStandard(this.internationalStandardFormGroup.value).subscribe(
            (response ) => {
                console.log(response);
                this.getUserTasks();
                this.SpinnerService.hide();
                this.showToasterSuccess(response.httpStatus, `Standard Uploaded`);

            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Try Again`);
                console.log(error.message);
            }
        );
        this.hideModalUploadStandard();
    }

    rejectUploadIStandard(): void {
        this.loadingText = "Saving...";
        this.SpinnerService.show();
        this.stdIntStandardService.uploadIStandard(this.internationalStandardRejectFormGroup.value).subscribe(
            (response ) => {
                console.log(response);
                this.getUserTasks();
                this.SpinnerService.hide();
                this.showToasterSuccess(response.httpStatus, `Standard Rejected`);

            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Try Again`);
                console.log(error.message);
            }
        );
        this.hideModalUploadStandard();
    }

    uploadGazetteNotice(): void {
        this.loadingText = "Saving...";
        this.SpinnerService.show();
        this.stdIntStandardService.uploadGazetteNotice(this.noticeFormGroup.value).subscribe(
            (response ) => {
                console.log(response);
                this.getUserTasks();
                this.SpinnerService.hide();
                this.showToasterSuccess(response.httpStatus, `Notice Uploaded`);

            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Try Again`);
                console.log(error.message);
            }
        );
        this.hideModalUploadNotice();
    }

    uploadGazetteDate(): void {
        this.loadingText = "Saving...";
        this.SpinnerService.show();
        this.stdIntStandardService.updateGazetteDate(this.gazetteFormGroup.value).subscribe(
            (response ) => {
                console.log(response);
                this.getUserTasks();
                this.SpinnerService.hide();
                this.showToasterSuccess(response.httpStatus, `Date Updated`);

            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Try Again`);
                console.log(error.message);
            }
        );
        this.hideModalUploadDate();
    }




  public onOpenModal(proposalComments: ProposalComments,mode:string): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
    if (mode==='decisionOnProposal'){
      this.actionRequest=proposalComments;
      button.setAttribute('data-target','#decisionOnProposal');

      this.approveProposalFormGroup.patchValue(
          {
            processId: this.actionRequest.processId,
            taskId: this.actionRequest.taskId,
            accentTo: this.approve,
              approvalID: this.actionRequest.taskData.ID
          }
      );
      this.rejectProposalFormGroup.patchValue(
          {
            processId: this.actionRequest.processId,
            taskId: this.actionRequest.taskId,
            accentTo: this.reject,
              approvalID: this.actionRequest.taskData.ID
          }
      );


    }
      if (mode==='prepareJustification') {
          this.actionRequest = proposalComments;
          button.setAttribute('data-target', '#prepareJustification');
          this.prepareJustificationFormGroup.patchValue(
              {
                  processId: this.actionRequest.processId,
                  taskId: this.actionRequest.taskId,
                  requestedBy: this.actionRequest.taskData.tcSecName,
                  slNumber: this.actionRequest.taskData.proposalNumber,
                  scope:this.actionRequest.taskData.scope,
                  circulationDate: this.actionRequest.taskData.circulationDate,
                  closingDate: this.actionRequest.taskData.closingDate

              }
          );

      }
      if (mode==='decisionOnSpcJustification'){
          this.actionRequest=proposalComments;
          button.setAttribute('data-target','#decisionOnSpcJustification');

          this.approveSpcJustificationFormGroup.patchValue(
              {
                  processId: this.actionRequest.processId,
                  taskId: this.actionRequest.taskId,
                  accentTo: this.approve,
                  proposalId: this.actionRequest.taskData.ID
              }
          );
          this.rejectSpcJustificationFormGroup.patchValue(
              {
                  processId: this.actionRequest.processId,
                  taskId: this.actionRequest.taskId,
                  accentTo: this.reject,
                  proposalId: this.actionRequest.taskData.ID
              }
          );
      }
      if (mode==='decisionOnSacJustification'){
          this.actionRequest=proposalComments;
          button.setAttribute('data-target','#decisionOnSacJustification');

          this.approveSacJustificationFormGroup.patchValue(
              {
                  processId: this.actionRequest.processId,
                  taskId: this.actionRequest.taskId,
                  accentTo: this.approve,
                  approvalID: this.actionRequest.taskData.ID
              }
          );
          this.rejectSacJustificationFormGroup.patchValue(
              {
                  processId: this.actionRequest.processId,
                  taskId: this.actionRequest.taskId,
                  accentTo: this.reject,
                  approvalID: this.actionRequest.taskData.ID
              }
          );


      }
      if (mode==='checkRequirementsMet'){
          this.actionRequest=proposalComments;
          button.setAttribute('data-target','#checkRequirementsMet');

          this.approveSacJustificationFormGroup.patchValue(
              {
                  processId: this.actionRequest.processId,
                  taskId: this.actionRequest.taskId,
                  accentTo: this.approve,
                  approvalID: this.actionRequest.taskData.ID
              }
          );
          this.rejectSacJustificationFormGroup.patchValue(
              {
                  processId: this.actionRequest.processId,
                  taskId: this.actionRequest.taskId,
                  accentTo: this.reject,
                  approvalID: this.actionRequest.taskData.ID
              }
          );
          this.approveHopJustificationFormGroup.patchValue(
              {
                  processId: this.actionRequest.processId,
                  taskId: this.actionRequest.taskId,
                  accentTo: this.approve,
                  approvalID: this.actionRequest.taskData.ID
              }

          );

          this.rejectHopJustificationFormGroup.patchValue(
              {
                  processId: this.actionRequest.processId,
                  taskId: this.actionRequest.taskId,
                  accentTo: this.reject,
                  approvalID: this.actionRequest.taskData.ID
              }

          );


      }
      if (mode==='draftStandardEditing') {
          this.actionRequest = proposalComments;
          button.setAttribute('data-target', '#draftStandardEditing');
          this.prepareJustificationFormGroup.patchValue(
              {
                  processId: this.actionRequest.processId,
                  taskId: this.actionRequest.taskId,
                  title: this.actionRequest.taskData.title,
                  scope:this.actionRequest.taskData.scope

              }
          );
          this.uploadDraftStandardFormGroup.patchValue(
              {
                  processId: this.actionRequest.processId,
                  taskId: this.actionRequest.taskId,
                  title: this.actionRequest.taskData.title,
                  scope:this.actionRequest.taskData.scope,
                  normativeReference: this.actionRequest.taskData.normativeReference,
                  symbolsAbbreviatedTerms: this.actionRequest.taskData.symbolsAbbreviatedTerms,
                  clause:this.actionRequest.taskData.clause,
                  special:this.actionRequest.taskData.special
              }
          );

      }
      if (mode==='draughting') {
          this.actionRequest = proposalComments;
          button.setAttribute('data-target', '#draughting');
          this.draughtingFormGroup.patchValue(
              {
                  processId: this.actionRequest.processId,
                  taskId: this.actionRequest.taskId,
                  title: this.actionRequest.taskData.title,
                  scope: this.actionRequest.taskData.scope,
                  normativeReference: this.actionRequest.taskData.normativeReference,
                  symbolsAbbreviatedTerms: this.actionRequest.taskData.symbolsAbbreviatedTerms,
                  clause: this.actionRequest.taskData.clause,
                  special: this.actionRequest.taskData.special,
                  id: this.actionRequest.taskData.draftId

              }
          );

      }
      if (mode==='proofreading') {
          this.actionRequest = proposalComments;
          button.setAttribute('data-target', '#proofreading');
          this.proofreadingFormGroup.patchValue(
              {
                  processId: this.actionRequest.processId,
                  taskId: this.actionRequest.taskId,
                  title: this.actionRequest.taskData.title,
                  scope: this.actionRequest.taskData.scope,
                  normativeReference: this.actionRequest.taskData.normativeReference,
                  symbolsAbbreviatedTerms: this.actionRequest.taskData.symbolsAbbreviatedTerms,
                  clause: this.actionRequest.taskData.clause,
                  special: this.actionRequest.taskData.special,
                  id: this.actionRequest.taskData.draftId

              }
          );

      }
      if (mode==='approveStandardChanges') {
          this.actionRequest = proposalComments;
          button.setAttribute('data-target', '#approveStandardChanges');
          this.internationalStandardFormGroup.patchValue(
              {
                  processId: this.actionRequest.processId,
                  taskId: this.actionRequest.taskId,
                  title: this.actionRequest.taskData.title,
                  scope: this.actionRequest.taskData.scope,
                  normativeReference: this.actionRequest.taskData.normativeReference,
                  symbolsAbbreviatedTerms: this.actionRequest.taskData.symbolsAbbreviatedTerms,
                  clause: this.actionRequest.taskData.clause,
                  special: this.actionRequest.taskData.special,
                  approvalID: this.actionRequest.taskData.ID,
                  accentTo: this.approve,
                  id: this.actionRequest.taskData.draftId

              }
          );
          this.internationalStandardRejectFormGroup.patchValue(
              {
                  processId: this.actionRequest.processId,
                  taskId: this.actionRequest.taskId,
                  title: this.actionRequest.taskData.title,
                  scope: this.actionRequest.taskData.scope,
                  normativeReference: this.actionRequest.taskData.normativeReference,
                  symbolsAbbreviatedTerms: this.actionRequest.taskData.symbolsAbbreviatedTerms,
                  clause: this.actionRequest.taskData.clause,
                  special: this.actionRequest.taskData.special,
                  approvalID: this.actionRequest.taskData.ID,
                  accentTo: this.reject,
                  id: this.actionRequest.taskData.draftId

              }
          );

      }
      if (mode==='uploadGazetteNotice') {
          this.actionRequest = proposalComments;
          button.setAttribute('data-target', '#uploadGazetteNotice');
          this.noticeFormGroup.patchValue(
              {
                  processId: this.actionRequest.processId,
                  taskId: this.actionRequest.taskId,
                  iSNumber: this.actionRequest.taskData.iSNumber

              }
          );

      }
      if (mode==='updateGazetteDate') {
          this.actionRequest = proposalComments;
          button.setAttribute('data-target', '#updateGazetteDate');
          this.gazetteFormGroup.patchValue(
              {
                  processId: this.actionRequest.processId,
                  taskId: this.actionRequest.taskId,
                  iSNumber: this.actionRequest.taskData.iSNumber

              }
          );

      }




    // @ts-ignore
    container.appendChild(button);
    button.click();

  }
  @ViewChild('closeModalApproveProposal') private closeModalApproveProposal: ElementRef | undefined;

  public hideModalApproveProposal() {
    this.closeModalApproveProposal?.nativeElement.click();
  }

    @ViewChild('closeModalPrepareJustification') private closeModalPrepareJustification: ElementRef | undefined;

    public hideModalPrepareJustification() {
        this.closeModalPrepareJustification?.nativeElement.click();
    }

    @ViewChild('closeModalApproveSPCJustification') private closeModalApproveSPCJustification: ElementRef | undefined;

    public hideModalApproveSpcJustification() {
        this.closeModalApproveSPCJustification?.nativeElement.click();
    }

    @ViewChild('closeModalApproveSACJustification') private closeModalApproveSACJustification: ElementRef | undefined;

    public hideModalApproveSacJustification() {
        this.closeModalApproveSACJustification?.nativeElement.click();
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
    @ViewChild('closeModalProofreading') private closeModalProofreading: ElementRef | undefined;

    public hideModalProofreading() {
        this.closeModalProofreading?.nativeElement.click();
    }

    @ViewChild('closeModalUploadStandard') private closeModalUploadStandard: ElementRef | undefined;

    public hideModalUploadStandard() {
        this.closeModalUploadStandard?.nativeElement.click();
    }

    @ViewChild('closeModalUploadNotice') private closeModalUploadNotice: ElementRef | undefined;

    public hideModalUploadNotice() {
        this.closeModalUploadNotice?.nativeElement.click();
    }

    @ViewChild('closeModalUploadDate') private closeModalUploadDate: ElementRef | undefined;

    public hideModalUploadDate() {
        this.closeModalUploadDate?.nativeElement.click();
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
            this.dtTrigger3.next();
            this.dtTrigger4.next();
            this.dtTrigger5.next();
            this.dtTrigger7.next();
            this.dtTrigger8.next();
            this.dtTrigger9.next();
            this.dtTrigger10.next();
            this.dtTrigger11.next();
            this.dtTrigger12.next();
        });

    }



}
