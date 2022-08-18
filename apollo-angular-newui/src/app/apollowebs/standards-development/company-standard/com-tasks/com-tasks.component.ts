import {Component, ElementRef, OnInit, ViewChild, ViewEncapsulation} from '@angular/core';
import {Subject} from "rxjs";
import {
    ComJcJustificationDec, CompanyStdRemarks,
    ComStandardJC,
    ComStdAction,
    NwaTasks,
    UsersEntity
} from "../../../../core/store/data/std/std.model";
import {Router} from "@angular/router";
import {StdComStandardService} from "../../../../core/store/data/std/std-com-standard.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {HttpErrorResponse} from "@angular/common/http";
import {DataTableDirective} from "angular-datatables";
import swal from "sweetalert2";
import {selectUserInfo} from "../../../../core/store";
import {Store} from "@ngrx/store";
import {SiteVisitRemarks} from "../../../../core/store/data/levy/levy.model";
declare const $: any;

@Component({
  selector: 'app-com-tasks',
  templateUrl: './com-tasks.component.html',
  styleUrls: ['./com-tasks.component.css','../../../../../../node_modules/@ng-select/ng-select/themes/default.theme.css'],
  encapsulation: ViewEncapsulation.None
})
export class ComTasksComponent implements OnInit {
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  tasks: ComJcJustificationDec[] = [];
  public actionRequest: ComJcJustificationDec | undefined;
  public uploadedFiles: FileList;
  public users !: UsersEntity[] ;
    companyStdRemarks: CompanyStdRemarks[] = [];
  user_id: number ;
  selectedUser: number;
  loadingText: string;
  approve: string;
  reject: string;
  public formJointCommitteeFormGroup!: FormGroup;
  public preparePreliminaryDraftFormGroup!: FormGroup;
  public approvePreliminaryDraftFormGroup!: FormGroup;
  public approveCompanyDraftFormGroup!: FormGroup;
  public rejectPreliminaryDraftFormGroup!: FormGroup;
  public rejectCompanyDraftFormGroup!: FormGroup;
    public prepareStandardFormGroup!: FormGroup;
  blob: Blob;
    isShowRemarksTab= true;
  @ViewChild(DataTableDirective, {static: false})
  dtElement: DataTableDirective;
  isDtInitialized: boolean = false
  constructor(
      private store$: Store<any>,
      private router: Router,
      private stdComStandardService:StdComStandardService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private formBuilder: FormBuilder
  ) { }

  ngOnInit(): void {
    this.getUserTasks();
    this.getUserList();
    this.approve='true';
    this.reject='false';

    this.store$.select(selectUserInfo).pipe().subscribe((u) => {
      return this.user_id = u.id;
    });

      this.prepareStandardFormGroup = this.formBuilder.group({
          title: ['', Validators.required],
          scope: ['', Validators.required],
          normativeReference: ['', Validators.required],
          referenceMaterial: ['', Validators.required],
          clause: ['', Validators.required],
          special: ['', Validators.required],
          taskId: [],
          processId: []

      });

    this.formJointCommitteeFormGroup = this.formBuilder.group({
      idOfJc: ['', Validators.required],
      taskId: ['', Validators.required],
      processId: ['', Validators.required],
      requestNumber: ['', Validators.required]

    });
    this.preparePreliminaryDraftFormGroup = this.formBuilder.group({
      title: ['', Validators.required],
      scope: ['', Validators.required],
      normativeReference: ['', Validators.required],
      symbolsAbbreviatedTerms: ['', Validators.required],
      clause: ['', Validators.required],
      special: ['', Validators.required],
      taskId: ['', Validators.required],
      processId: ['', Validators.required],
      requestNumber:['', Validators.required]

    });
    this.approvePreliminaryDraftFormGroup = this.formBuilder.group({
      comments: ['', Validators.required],
      assignedTo: [],
      processId:[],
      taskId: [],
      accentTo: [],
      approvalID:[]

    });
    this.rejectPreliminaryDraftFormGroup = this.formBuilder.group({
      comments: ['', Validators.required],
      taskId: [],
      assignedTo: [],
      processId:[],
      accentTo: [],
      approvalID:[]

    });
      this.approveCompanyDraftFormGroup = this.formBuilder.group({
          comments: ['', Validators.required],
          assignedTo: [],
          processId:[],
          taskId: [],
          accentTo: [],
          approvalID:[]

      });
      this.rejectCompanyDraftFormGroup = this.formBuilder.group({
          comments: ['', Validators.required],
          taskId: [],
          assignedTo: [],
          processId:[],
          accentTo: [],
          approvalID:[]

      });

  }
    get formPrepareSD(): any {
        return this.prepareStandardFormGroup.controls;
    }

  get formJointCommitteeForm(): any {
    return this.formJointCommitteeFormGroup.controls;
  }
  get formPreparePD(): any {
    return this.preparePreliminaryDraftFormGroup.controls;
  }
  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
  }
  showToasterError(title:string,message:string){
    this.notifyService.showError(message, title)
  }
  showToasterSuccess(title:string,message:string){
    this.notifyService.showSuccess(message, title)

  }
  public getUserTasks(): void {
    this.loadingText = "Retrieving Tasks...";
    this.SpinnerService.show();
    this.stdComStandardService.getUserTasks().subscribe(
        (response: ComJcJustificationDec[]) => {
          this.tasks = response;
          if (this.isDtInitialized) {
            this.dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
              dtInstance.destroy();
              this.dtTrigger.next();
              this.SpinnerService.hide();
            });
          } else {
            this.isDtInitialized = true
            this.dtTrigger.next();
            this.SpinnerService.hide();
          }

        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
          console.log(error.message);
        }
    );
  }


  public onAssign(comStdAction: ComStdAction): void{
    this.loadingText = "Assigning Task...";
    this.SpinnerService.show();
    this.stdComStandardService.assignRequest(comStdAction).subscribe(
        (response: ComStdAction) => {
          this.SpinnerService.hide();
          console.log(response);
          this.getUserTasks();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
    this.hideModelCloseModalAssignTask();
  }

  formJointCommittee(): void {
    this.loadingText = "Saving Committee List...";
    this.SpinnerService.show();
    console.log(this.formJointCommitteeFormGroup.value);
    this.stdComStandardService.formJointCommittee(this.formJointCommitteeFormGroup.value).subscribe(
        (response ) => {
          console.log(response);
          this.getUserTasks();
          this.SpinnerService.hide();
          this.showToasterSuccess('Success', `Committee List Saved`);
          this.formJointCommitteeFormGroup.reset();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Committee List Was Not Saved! Try Again`);
          console.log(error.message);
        }
    );
    this.hideModelCloseModalFormJC();
  }



  public getUserList(): void {
    this.SpinnerService.show();
    this.stdComStandardService.getUserList().subscribe(
        (response: UsersEntity[]) => {
          this.SpinnerService.hide();
          this.users = response;
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }
  uploadPreliminaryDraft(): void {
      this.loadingText = "Saving Form...";
    this.SpinnerService.show();
   // console.log(this.preparePreliminaryDraftFormGroup.value);
    this.stdComStandardService.prepareCompanyPreliminaryDraft(this.preparePreliminaryDraftFormGroup.value).subscribe(
        (response ) => {
          console.log(response);
          this.SpinnerService.hide();
          this.showToasterSuccess(response.httpStatus, `Preliminary Draft Preparation Process Started`);
          this.onClickSaveUPLOADS(response.body.savedRowID)
          this.preparePreliminaryDraftFormGroup.reset();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Preliminary Draft Was Not Uploaded Try Again`);
          console.log(error.message);
        }
    );
  }

  onClickSaveUPLOADS(comStdDraftID: string) {
    if (this.uploadedFiles.length > 0) {
      const file = this.uploadedFiles;
      const formData = new FormData();
      for (let i = 0; i < file.length; i++) {
        console.log(file[i]);
        formData.append('docFile', file[i], file[i].name);
      }
        this.loadingText = "Uploading Document...";
      this.SpinnerService.show();
      this.stdComStandardService.uploadPDFileDetails(comStdDraftID, formData).subscribe(
          (data: any) => {
            this.SpinnerService.hide();
            this.uploadedFiles = null;
            console.log(data);
            this.getUserTasks();
            swal.fire({
              title: 'Company Draft Prepared.',
              buttonsStyling: false,
              customClass: {
                confirmButton: 'btn btn-success form-wizard-next-btn ',
              },
              icon: 'success'
            });
            //this.router.navigate(['/nwaJustification']);
          },
      );
      this.hideModelCloseModalFormPD();
    }

  }
    viewPdfFile(pdfId: number, fileName: string, applicationType: string): void {
        this.loadingText = "Generating Document...";
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
            (error: HttpErrorResponse)=>{
                this.SpinnerService.hide();
                alert(error.message);
            }
        );
    }



  approvePreliminaryDraft(): void {
    this.loadingText = "Approving Company Draft...";
    this.SpinnerService.show();
    this.stdComStandardService.decisionOnDraft(this.approvePreliminaryDraftFormGroup.value).subscribe(
        (response ) => {
          //console.log(response);
          this.getUserTasks();
          this.SpinnerService.hide();
          this.showToasterSuccess('Success', `Preliminary Draft Approved`);
          this.approvePreliminaryDraftFormGroup.reset();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Try Again`);
          console.log(error.message);
        }
    );
    this.hideModalApprovePD();
  }

  rejectPreliminaryDraft(): void {
    this.loadingText = "Rejecting Preliminary Draft...";
    this.SpinnerService.show();
    this.stdComStandardService.decisionOnDraft(this.rejectPreliminaryDraftFormGroup.value).subscribe(
        (response ) => {
          //console.log(response);
          this.getUserTasks();
          this.SpinnerService.hide();
          this.showToasterSuccess('Success', `Preliminary Draft Declined`);
          this.rejectPreliminaryDraftFormGroup.reset();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Try Again`);
          console.log(error.message);
        }
    );
    this.hideModalApprovePD();
  }

    approveDraft(): void {
        this.loadingText = "Approving Company Draft...";
        this.SpinnerService.show();
        this.stdComStandardService.companyDecisionOnDraft(this.approveCompanyDraftFormGroup.value).subscribe(
            (response ) => {
                //console.log(response);
                this.getUserTasks();
                this.SpinnerService.hide();
                this.showToasterSuccess('Success', `Preliminary Draft Approved`);
                this.approveCompanyDraftFormGroup.reset();
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Try Again`);
                console.log(error.message);
            }
        );
        this.hideCloseModalApproveDraft();
    }

    rejectDraft(): void {
        this.loadingText = "Rejecting Preliminary Draft...";
        this.SpinnerService.show();
        this.stdComStandardService.companyDecisionOnDraft(this.rejectCompanyDraftFormGroup.value).subscribe(
            (response ) => {
                //console.log(response);
                this.getUserTasks();
                this.SpinnerService.hide();
                this.showToasterSuccess('Success', `Preliminary Draft Declined`);
                this.rejectCompanyDraftFormGroup.reset();
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Try Again`);
                console.log(error.message);
            }
        );
        this.hideCloseModalApproveDraft();
    }

    toggleDisplayRemarksTab(approvalID: number){
        this.loadingText = "Loading ...."
        this.SpinnerService.show();
        this.stdComStandardService.getComStandardRemarks(approvalID).subscribe(
            (response: CompanyStdRemarks[]) => {
                this.companyStdRemarks = response;
                this.SpinnerService.hide();
                console.log(this.companyStdRemarks)
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                console.log(error.message);
            }
        );
        this.isShowRemarksTab = !this.isShowRemarksTab;

    }

    onSaveStandard(): void {
        this.SpinnerService.show();
        this.loadingText = "Saving Form...";
        console.log(this.prepareStandardFormGroup.value);
        this.stdComStandardService.prepareCompanyStandard(this.prepareStandardFormGroup.value).subscribe(
            (response ) => {
                console.log(response);
                this.SpinnerService.hide();
                this.showToasterSuccess(response.httpStatus, `Company Standard Preparation Process Started`);
                this.onClickSaveUPLOADSTD(response.body.savedRowID)
                this.prepareStandardFormGroup.reset();
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error..Try Again`);
                console.log(error.message);
            }
        );
    }
    onClickSaveUPLOADSTD(comStandardID: string) {
        if (this.uploadedFiles.length > 0) {
            const file = this.uploadedFiles;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }
            this.SpinnerService.show();
            this.loadingText = "Uploading Document...";
            this.stdComStandardService.uploadSDFileDetails(comStandardID, formData).subscribe(
                (data: any) => {
                    this.SpinnerService.hide();
                    this.uploadedFiles = null;
                    console.log(data);
                    this.getUserTasks();
                    swal.fire({
                        title: 'Company Standard Uploaded.',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success form-wizard-next-btn ',
                        },
                        icon: 'success'
                    });
                    //this.router.navigate(['/nwaJustification']);
                },
            );
            this.hideCloseModalStandard();
        }

    }

  public onOpenModal(task: ComJcJustificationDec,mode:string): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
    if (mode==='approve'){
      this.actionRequest=task;
      button.setAttribute('data-target','#assignModal');
    }

    if (mode==='formJc'){
      this.actionRequest=task;
      button.setAttribute('data-target','#formJc');
      this.formJointCommitteeFormGroup.patchValue(
          {
            taskId: this.actionRequest.taskId,
            processId: this.actionRequest.processId,
            requestNumber: this.actionRequest.taskData.requestNumber
          }
      );
    }
    if (mode==='uploadDraft'){
      this.actionRequest=task;
      button.setAttribute('data-target','#uploadDraft');
      this.preparePreliminaryDraftFormGroup.patchValue(
          {
            taskId: this.actionRequest.taskId,
            processId: this.actionRequest.processId,
            requestNumber:this.actionRequest.taskData.requestNumber
          }
      );
    }
    if (mode==='approveDraft'){
      this.actionRequest=task;
      button.setAttribute('data-target','#approveDraft');
      this.approvePreliminaryDraftFormGroup.patchValue(
          {
            processId: this.actionRequest.processId,
            taskId: this.actionRequest.taskId,
            accentTo: this.approve,
            approvalID: this.actionRequest.taskData.ID
          }
      );
      this.rejectPreliminaryDraftFormGroup.patchValue(
          {
            taskId: this.actionRequest.taskId,
            processId: this.actionRequest.processId,
            accentTo: this.reject,
            approvalID: this.actionRequest.taskData.ID
          }
      );
    }
      if (mode==='approveComDraft'){
          this.actionRequest=task;
          button.setAttribute('data-target','#approveComDraft');
          this.approveCompanyDraftFormGroup.patchValue(
              {
                  processId: this.actionRequest.processId,
                  taskId: this.actionRequest.taskId,
                  accentTo: this.approve,
                  approvalID: this.actionRequest.taskData.ID
              }
          );
          this.rejectCompanyDraftFormGroup.patchValue(
              {
                  taskId: this.actionRequest.taskId,
                  processId: this.actionRequest.processId,
                  accentTo: this.reject,
                  approvalID: this.actionRequest.taskData.ID
              }
          );
      }
      if (mode==='uploadStandard'){
          this.actionRequest=task;
          button.setAttribute('data-target','#uploadStandard');
          this.preparePreliminaryDraftFormGroup.patchValue(
              {
                  taskId: this.actionRequest.taskId,
                  processId: this.actionRequest.processId
              }
          );
      }


    // @ts-ignore
    container.appendChild(button);
    button.click();

  }

  @ViewChild('closeModalAssignTask') private closeModalAssignTask: ElementRef | undefined;

  public hideModelCloseModalAssignTask() {
    this.closeModalAssignTask?.nativeElement.click();
  }

  @ViewChild('closeModalFormJC') private closeModalFormJC: ElementRef | undefined;

  public hideModelCloseModalFormJC() {
    this.closeModalFormJC?.nativeElement.click();
  }

  @ViewChild('closeModalFormPD') private closeModalFormPD: ElementRef | undefined;

  public hideModelCloseModalFormPD() {
    this.closeModalFormPD?.nativeElement.click();
  }

  @ViewChild('closeModalApprovePD') private closeModalApprovePD: ElementRef | undefined;

  public hideModalApprovePD() {
    this.closeModalApprovePD?.nativeElement.click();
  }

    @ViewChild('closeModalApproveDraft') private closeModalApproveDraft: ElementRef | undefined;

    public hideCloseModalApproveDraft() {
        this.closeModalApproveDraft?.nativeElement.click();
    }

    @ViewChild('closeModalStandard') private closeModalStandard: ElementRef | undefined;

    public hideCloseModalStandard() {
        this.closeModalStandard?.nativeElement.click();
    }


  showNotification(from: any, align: any) {
    const type = ['', 'info', 'success', 'warning', 'danger', 'rose', 'primary'];

    const color = Math.floor((Math.random() * 6) + 1);

    $.notify({
      icon: 'notifications',
      message: 'Welcome to <b>Material Dashboard</b> - a beautiful dashboard for every web developer.'
    }, {
      type: type[color],
      timer: 3000,
      placement: {
        from: from,
        align: align
      },
      template: '<div data-notify="container" class="col-xs-11 col-sm-3 alert alert-{0} alert-with-icon" role="alert">' +
          '<button mat-raised-button type="button" aria-hidden="true" class="close" data-notify="dismiss">  <i class="material-icons">close</i></button>' +
          '<i class="material-icons" data-notify="icon">notifications</i> ' +
          '<span data-notify="title"></span> ' +
          '<span data-notify="message">Ensure all required fields and items have been filled</span>' +
          '<div class="progress" data-notify="progressbar">' +
          '<div class="progress-bar progress-bar-{0}" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" ></div>' +
          '</div>' +
          '<a href="{3}" target="{4}" data-notify="url"></a>' +
          '</div>'
    });
  }



}
