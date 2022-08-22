import {Component, ElementRef, OnInit, ViewChild, ViewEncapsulation} from '@angular/core';
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {HttpErrorResponse} from "@angular/common/http";
import {NwaTasks,UsersEntity
} from "../../../../core/store/data/std/std.model";
import {Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {StdNwaService} from "../../../../core/store/data/std/std-nwa.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import swal from "sweetalert2";
import {selectUserInfo} from "../../../../core/store";
import {Store} from "@ngrx/store";
declare const $: any;
@Component({
  selector: 'app-nwa-tasks',
  templateUrl: './nwa-tasks.component.html',
  styleUrls: ['./nwa-tasks.component.css','../../../../../../node_modules/@ng-select/ng-select/themes/default.theme.css'],
encapsulation: ViewEncapsulation.None
})
export class NwaTasksComponent implements OnInit {
  @ViewChild(DataTableDirective, {static: false})
  dtElement: DataTableDirective;
  dtTrigger: Subject<any> = new Subject<any>();
  isDtInitialized: boolean = false
  loadingText: string;
  tasks: NwaTasks[] = [];
    roles: string[];
  public actionRequest: NwaTasks | undefined;
    public knwSecretary !: UsersEntity[] ;
    public diDirector !: UsersEntity[] ;
    public headOfPublishing !: UsersEntity[] ;
    public sacSecretary !: UsersEntity[] ;
    public headOfSic !: UsersEntity[] ;
    public spcSecretary !: UsersEntity[] ;
  public uploadedFiles:  FileList;
    approve: string;
    reject: string;
    public approveReviewJustificationFormGroup!: FormGroup;
    public rejectReviewJustificationFormGroup!: FormGroup;
    public approveViewJustificationFormGroup!: FormGroup;
    public rejectViewJustificationFormGroup!: FormGroup;
    public approveDIJustificationFormGroup!: FormGroup;
    public rejectDIJustificationFormGroup!: FormGroup;
  public prepareDIJustificationFormGroup!: FormGroup;
  public preparePreliminaryDraftFormGroup!: FormGroup;
  public approvePreliminaryDraftFormGroup!: FormGroup;
  public rejectPreliminaryDraftFormGroup!: FormGroup;
    public prepareWorkShopDraftFormGroup!: FormGroup;
    public approveWorkShopDraftFormGroup!: FormGroup;
    public rejectWorkShopDraftFormGroup!: FormGroup;
    public prepareStandardFormGroup!: FormGroup;
    public uploadNoticeFormGroup!: FormGroup;
    public updateNoticeFormGroup!: FormGroup;
  blob: Blob;
  constructor(
      private router: Router,
      private formBuilder: FormBuilder,
      private stdNwaService: StdNwaService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private store$: Store<any>
  ) { }

  ngOnInit(): void {
    this.getNwaTasks();
      this.getUserRoles();
      this.getKnwSecretary();
      this.getDirector();
      this.getHeadOfPublishing();
      this.getSacSecretary();
      this.getHeadOfSic();
      this.getSpcSecretary();
      this.approve='true';
      this.reject='false';

      this.approveReviewJustificationFormGroup = this.formBuilder.group({
          comments: [],
          assignedTo: [],
          taskId: [],
          accentTo: [],
          processId:[],
          approvalID:[]

      });

      this.rejectReviewJustificationFormGroup = this.formBuilder.group({
          comments: ['', Validators.required],
          assignedTo: [],
          taskId: [],
          accentTo: [],
          processId:[],
          approvalID:[]

      });

      this.approveViewJustificationFormGroup = this.formBuilder.group({
          comments: ['', Validators.required],
          assignedTo: [],
          taskId: [],
          accentTo: [],
          processId:[],
          approvalID:[]

      });

      this.rejectViewJustificationFormGroup = this.formBuilder.group({
          comments: ['', Validators.required],
          assignedTo: [],
          taskId: [],
          accentTo: [],
          processId:[],
          approvalID:[]

      });
      // if(this.roles?.includes('KNW_SEC_SD')){
      //
      // }
      //
      // if(this.roles?.includes('DI_SDT_SD')){
      //
      // }
      //
      // if(this.roles?.includes('HOP_SD')){
      //
      // }
      //
      // if(this.roles?.includes('SAC_SEC_SD')){
      //
      // }
      //
      // if(this.roles?.includes('HO_SIC_SD')){
      //
      // }
      //
      // if(this.roles?.includes('SPC_SEC_SD')){
      //
      // }

    this.prepareDIJustificationFormGroup = this.formBuilder.group({
      cost: ['', Validators.required],
      numberOfMeetings: ['', Validators.required],
      identifiedNeed: ['', Validators.required],
      dateOfApproval: ['', Validators.required],
      taskId: [],
      jstNumber: [],
        processId: [],
        assignedTo: []

    });

      this.approveDIJustificationFormGroup = this.formBuilder.group({
          comments: ['', Validators.required],
          assignedTo: [],
          taskId: [],
          accentTo: [],
          processId:[],
          approvalID:[]

      });

      this.rejectDIJustificationFormGroup = this.formBuilder.group({
          comments: ['', Validators.required],
          assignedTo: [],
          taskId: [],
          accentTo: [],
          processId:[],
          jstNumber:[]

      });

    this.preparePreliminaryDraftFormGroup = this.formBuilder.group({
      title: ['', Validators.required],
      scope: ['', Validators.required],
      normativeReference: ['', Validators.required],
      symbolsAbbreviatedTerms: ['', Validators.required],
      clause: ['', Validators.required],
      special: ['', Validators.required],
      taskId: [],
      diJNumber: [],
        processId: [],
        assignedTo: []

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
          jstNumber:[]

      });

      this.prepareWorkShopDraftFormGroup = this.formBuilder.group({
          title: ['', Validators.required],
          scope: ['', Validators.required],
          normativeReference: ['', Validators.required],
          referenceMaterial: ['', Validators.required],
          clause: ['', Validators.required],
          special: ['', Validators.required],
          taskId: [],
          processId: [],
          assignedTo: []

      });

      this.approveWorkShopDraftFormGroup = this.formBuilder.group({
          comments: ['', Validators.required],
          assignedTo: [],
          processId:[],
          taskId: [],
          accentTo: [],
          approvalID:[]

      });

      this.rejectWorkShopDraftFormGroup = this.formBuilder.group({
          comments: ['', Validators.required],
          assignedTo: [],
          processId:[],
          taskId: [],
          accentTo: [],
          approvalID:[]

      });

      this.prepareStandardFormGroup = this.formBuilder.group({
        title: ['', Validators.required],
        scope: ['', Validators.required],
        normativeReference: ['', Validators.required],
        referenceMaterial: ['', Validators.required],
        clause: ['', Validators.required],
        special: ['', Validators.required],
        taskId: [],
        ksNumber: [],
        processId: [],
        assignedTo: []

      });

   this.uploadNoticeFormGroup = this.formBuilder.group({
       ksNumber: [],
       description: [],
       processId: [],
       taskId: []

   });

      this.updateNoticeFormGroup = this.formBuilder.group({
          ksNumber: [],
          description: [],
          processId: [],
          taskId: []

      });



  }
  showToasterSuccess(title:string,message:string){
    this.notifyService.showSuccess(message, title)

  }
  showToasterError(title:string,message:string){
    this.notifyService.showError(message, title)

  }
  showToasterWarning(title:string,message:string){
    this.notifyService.showWarning(message, title)

  }
    get approveReviewJustificationForm(): any {
        return this.approveReviewJustificationFormGroup.controls;
    }

    get rejectReviewJustificationForm(): any {
        return this.rejectReviewJustificationFormGroup.controls;
    }

  get formPrepareJustification(): any {
    return this.prepareDIJustificationFormGroup.controls;
  }
  get formPreparePD(): any {
    return this.preparePreliminaryDraftFormGroup.controls;
  }
    get formPrepareWD(): any {
        return this.prepareWorkShopDraftFormGroup.controls;
    }
    get formStandard(): any {
        return this.prepareStandardFormGroup.controls;
    }

  public getNwaTasks(): void {
    this.loadingText = "Retrieving Tasks...";
    this.SpinnerService.show();
    this.stdNwaService.getNwaTasks().subscribe(
        (response: NwaTasks[]) => {
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
  public onOpenModal(task: NwaTasks,mode:string): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
    if (mode==='viewJustification'){
      this.actionRequest=task;
      button.setAttribute('data-target','#viewJustification');
        this.approveViewJustificationFormGroup.patchValue(
            {
                accentTo: this.approve,
                taskId: this.actionRequest.taskId,
                processId: this.actionRequest.processId,
                approvalID: this.actionRequest.taskData.ID,
                assignedTo: this.actionRequest.taskData.originator
            }
        );
        this.rejectViewJustificationFormGroup.patchValue(
            {
                accentTo: this.reject,
                taskId: this.actionRequest.taskId,
                processId: this.actionRequest.processId,
                approvalID: this.actionRequest.taskData.ID,
                assignedTo: this.actionRequest.taskData.originator
            }
        );
    }

    if (mode==='reviewJustification'){
      this.actionRequest=task;
      button.setAttribute('data-target','#reviewJustification');
        this.approveReviewJustificationFormGroup.patchValue(
            {
                accentTo: this.approve,
                taskId: this.actionRequest.taskId,
                processId: this.actionRequest.processId,
                approvalID: this.actionRequest.taskData.ID
            }
        );
        this.rejectReviewJustificationFormGroup.patchValue(
            {
                accentTo: this.reject,
                taskId: this.actionRequest.taskId,
                processId: this.actionRequest.processId,
                approvalID: this.actionRequest.taskData.ID
            }
        );
    }
      if (mode==='prepDiSdt'){
          this.actionRequest=task;
          button.setAttribute('data-target','#prepDiSdt');
          this.prepareDIJustificationFormGroup.patchValue(
              {
                  taskId: this.actionRequest.taskId,
                  processId: this.actionRequest.processId,
                  jstNumber: this.actionRequest.taskData.ID
              }
          );

      }

    if (mode==='approveDiJustification'){
      this.actionRequest=task;
      button.setAttribute('data-target','#approveDiJustification');
        this.approveDIJustificationFormGroup.patchValue(
            {
                assignedTo: this.actionRequest.taskData.originator,
                taskId: this.actionRequest.taskId,
                processId: this.actionRequest.processId,
                accentTo: this.approve,
                approvalID: this.actionRequest.taskData.ID
            }
        );

        this.rejectDIJustificationFormGroup.patchValue(
            {
                assignedTo: this.actionRequest.taskData.diOriginator,
                taskId: this.actionRequest.taskId,
                processId: this.actionRequest.processId,
                accentTo: this.reject,
                jstNumber: this.actionRequest.taskData.jstNumber
            }
        );

    }


    if (mode==='preparePD'){
      this.actionRequest=task;
      button.setAttribute('data-target','#preparePD');
        this.preparePreliminaryDraftFormGroup.patchValue(
            {
                taskId: this.actionRequest.taskId,
                processId: this.actionRequest.processId,
                diJNumber: this.actionRequest.taskData.ID
            }
        );
    }
    if (mode==='approvePd'){
      this.actionRequest=task;
      button.setAttribute('data-target','#approvePd');
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
                assignedTo: this.actionRequest.taskData.originator,
                accentTo: this.reject,
                jstNumber: this.actionRequest.taskData.diJNumber
            }
        );
    }




      if (mode==='editPreliminaryDraft'){
          this.actionRequest=task;
          button.setAttribute('data-target','#editPreliminaryDraft');
          this.prepareWorkShopDraftFormGroup.patchValue(
              {
                  taskId: this.actionRequest.taskId,
                  processId: this.actionRequest.processId,
              }
          );

      }
      if (mode==='approveWorkshopDraft'){
          this.actionRequest=task;
          button.setAttribute('data-target','#approveWorkshopDraft');
          this.approveWorkShopDraftFormGroup.patchValue(
              {
                  taskId: this.actionRequest.taskId,
                  processId: this.actionRequest.processId,
                  accentTo: this.approve,
                  approvalID: this.actionRequest.taskData.ID,
              }
          );

          this.rejectWorkShopDraftFormGroup.patchValue(
              {
                  taskId: this.actionRequest.taskId,
                  processId: this.actionRequest.processId,
                  accentTo: this.reject,
                  approvalID: this.actionRequest.taskData.ID,
                  assignedTo: this.actionRequest.taskData.vpdOriginator
              }
          );
      }
      if (mode==='uploadStandard'){
          this.actionRequest=task;
          button.setAttribute('data-target','#uploadStandard');
          this.rejectWorkShopDraftFormGroup.patchValue(
              {
                  title:this.actionRequest.taskData.title,
                  scope: this.actionRequest.taskData.scope,
                  normativeReference: this.actionRequest.taskData.normativeReference,
                  referenceMaterial: this.actionRequest.taskData.referenceMaterial,
                  clause: this.actionRequest.taskData.clause,
                  special: this.actionRequest.taskData.special,
                  taskId: this.actionRequest.taskId,
                  ksNumber: this.actionRequest.taskData.ksNumber,
                  processId: this.actionRequest.processId
              });
      }

      if (mode==='notice'){
          this.actionRequest=task;
          button.setAttribute('data-target','#noticeModal');
          this.uploadNoticeFormGroup.patchValue(
              {
                  ksNumber: this.actionRequest.taskData.ksNumber,
                  processId: this.actionRequest.processId,
                  taskId: this.actionRequest.taskId
              });
      }

      if (mode==='gazetteDate'){
          this.actionRequest=task;
          button.setAttribute('data-target','#gazettementModal');
          this.updateNoticeFormGroup.patchValue(
              {
                  ksNumber: this.actionRequest.taskData.ksNumber,
                  processId: this.actionRequest.processId,
                  taskId: this.actionRequest.taskId
              });
      }


    // @ts-ignore
    container.appendChild(button);
    button.click();

  }

    onDecision(): void {
        this.loadingText = "Approving Justification...";
        this.SpinnerService.show();
        this.stdNwaService.decisionOnJustification(this.approveViewJustificationFormGroup.value).subscribe(
            (response ) => {
                console.log(response);
                this.getNwaTasks();
                this.SpinnerService.hide();
                this.showToasterSuccess('Success', `Justification Approved`);
                this.approveViewJustificationFormGroup.reset();
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Justification Was Not Approved`);
                console.log(error.message);
            }
        );
        this.hideModelCloseModalViewJustification();
    }

    rejectDecision(): void {
        this.loadingText = "Rejecting Justification...";
        this.SpinnerService.show();
        this.stdNwaService.decisionOnJustification(this.rejectViewJustificationFormGroup.value).subscribe(
            (response ) => {
                console.log(response);
                this.getNwaTasks();
                this.SpinnerService.hide();
                this.showToasterSuccess('Success', `Justification Was Declined`);
                this.rejectViewJustificationFormGroup.reset();
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Justification Was Not Approved`);
                console.log(error.message);
            }
        );
        this.hideModelCloseModalViewJustification();
    }



    approveJustification(): void {
        this.loadingText = "Approving Justification...";
        this.SpinnerService.show();
        this.stdNwaService.decisionOnJustificationKNW(this.approveReviewJustificationFormGroup.value).subscribe(
            (response ) => {
                console.log(response);
                this.getNwaTasks();
                this.SpinnerService.hide();
                this.showToasterSuccess('Success', `Justification Approved`);
                this.approveReviewJustificationFormGroup.reset();
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Justification Was Not Approved`);
                console.log(error.message);
            }
        );
        this.hideModelCloseModalReviewJustification();
    }

    rejectJustification(): void {
        this.loadingText = "Rejecting Justification...";
        this.SpinnerService.show();
        this.stdNwaService.decisionOnJustificationKNW(this.approveReviewJustificationFormGroup.value).subscribe(
            (response ) => {
                console.log(response);
                this.getNwaTasks();
                this.SpinnerService.hide();
                this.showToasterSuccess('Success', `Justification Approved`);
                this.approveReviewJustificationFormGroup.reset();
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Justification Was Not Approved`);
                console.log(error.message);
            }
        );
        this.hideModelCloseModalReviewJustification();
    }


  uploadDiSdt(): void {
      this.loadingText = "Saving...";
    this.SpinnerService.show();
    this.stdNwaService.prepareDisDtJustification(this.prepareDIJustificationFormGroup.value).subscribe(
        (response ) => {
          console.log(response);

          this.SpinnerService.hide();
          this.showToasterSuccess(response.httpStatus, `Justification For Di-SDT Approval  Process Started`);
          this.onClickSaveUPLOADS(response.body.savedRowID)
          this.prepareDIJustificationFormGroup.reset();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Justification Was Not Prepared`);
          console.log(error.message);
        }
    );
  }

    onDecisionDiJustification(): void {
        this.loadingText = "Approving Justification...";
        this.SpinnerService.show();
        this.stdNwaService.decisionOnDiSdtJustification(this.approveDIJustificationFormGroup.value).subscribe(
            (response ) => {
                console.log(response);
                this.getNwaTasks();
                this.SpinnerService.hide();
                this.showToasterSuccess('Success', `Justification Approved`);
                this.approveDIJustificationFormGroup.reset();
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Justification Was Not Approved`);
                console.log(error.message);
            }
        );
        this.hideModalApproveDiJustification();
    }

    onDecisionRejectDiJustification(): void {
        this.loadingText = "Rejecting Justification...";
        this.SpinnerService.show();
        this.stdNwaService.decisionOnDiSdtJustification(this.rejectDIJustificationFormGroup.value).subscribe(
            (response ) => {
                //console.log(response);
                this.getNwaTasks();
                this.SpinnerService.hide();
                this.showToasterSuccess('Success', `Justification Was Declined`);
                this.rejectDIJustificationFormGroup.reset();
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Justification Was Not Approved`);
                console.log(error.message);
            }
        );
        this.hideModalApproveDiJustification();
    }



    approvePreliminaryDraft(): void {
        this.loadingText = "Approving Preliminary Draft...";
        this.SpinnerService.show();
        this.stdNwaService.decisionOnPD(this.approvePreliminaryDraftFormGroup.value).subscribe(
            (response ) => {
                //console.log(response);
                this.getNwaTasks();
                this.SpinnerService.hide();
                this.showToasterSuccess('Success', `Preliminary Draft Approved`);
                this.approvePreliminaryDraftFormGroup.reset();
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Justification Was Not Approved`);
                console.log(error.message);
            }
        );
        this.hideModalApprovePD();
    }

    rejectPreliminaryDraft(): void {
        this.loadingText = "Rejecting Preliminary Draft...";
        this.SpinnerService.show();
        this.stdNwaService.decisionOnPD(this.rejectPreliminaryDraftFormGroup.value).subscribe(
            (response ) => {
                //console.log(response);
                this.getNwaTasks();
                this.SpinnerService.hide();
                this.showToasterSuccess('Success', `Preliminary Draft Declined`);
                this.rejectPreliminaryDraftFormGroup.reset();
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Rejecting Preliminary Draft`);
                console.log(error.message);
            }
        );
        this.hideModalApprovePD();
    }



  onClickSaveUPLOADS(nwaDiSdtJustificationID: string) {
    if (this.uploadedFiles.length > 0) {
      const file = this.uploadedFiles;
      const formData = new FormData();
      for (let i = 0; i < file.length; i++) {
        console.log(file[i]);
        formData.append('docFile', file[i], file[i].name);
      }

      this.SpinnerService.show();
      this.stdNwaService.uploadDIFileDetails(nwaDiSdtJustificationID, formData).subscribe(
          (data: any) => {
            this.SpinnerService.hide();
            this.showToasterSuccess(data.httpStatus, `Justification For Di-SDT Approval Prepared`);
            this.uploadedFiles = null;
           // console.log(data);
              this.getNwaTasks();
            swal.fire({
              title: 'Justification For Di-SDT Approval Prepared.',
              buttonsStyling: false,
              customClass: {
                confirmButton: 'btn btn-success ',
              },
              icon: 'success'
            });
              this.hideModelCloseModalPrepDiSdt();

          },
      );
    }

  }
    viewPreliminaryDraftPDF(pdfId: number, fileName: string, applicationType: string): void {
        this.SpinnerService.show();
        this.stdNwaService.viewPreliminaryDraftPDF(pdfId).subscribe(
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
        );
    }
    viewWDFile(pdfId: number, fileName: string, applicationType: string): void {
        this.SpinnerService.show();
        this.stdNwaService.viewWorkshopDraftPDF(pdfId).subscribe(
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
        );
    }

    uploadPreliminaryDraft(): void {
        this.loadingText = "Saving...";
        this.SpinnerService.show();
        //console.log(this.preparePreliminaryDraftFormGroup.value);
        this.stdNwaService.preparePreliminaryDraft(this.preparePreliminaryDraftFormGroup.value).subscribe(
            (response ) => {
                console.log(response);
                this.SpinnerService.hide();
                this.showToasterSuccess(response.httpStatus, `Preliminary Draft Preparation Process Started`);
                this.onClickPDUPLOADs(response.body.savedRowID)
                this.preparePreliminaryDraftFormGroup.reset();
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Preliminary Draft Was Not Prepared`);
                console.log(error.message);
            }
        );
    }


    onClickPDUPLOADs(nwaPDid: string) {
        if (this.uploadedFiles.length > 0) {
            const file = this.uploadedFiles;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }

            this.SpinnerService.show();
            this.stdNwaService.uploadPDFileDetails(nwaPDid, formData).subscribe(
                (data: any) => {
                    this.SpinnerService.hide();
                    this.showToasterSuccess(data.httpStatus, `Preliminary Draft Prepared`);
                    this.uploadedFiles = null;
                    this.getNwaTasks();
                    console.log(data);
                    swal.fire({
                        title: 'Preliminary Draft Prepared.',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success ',
                        },
                        icon: 'success'
                    });
                    this.hideModalPreparePD();

                },
            );
        }

    }
    viewDIJustificationPDF(pdfId: number, fileName: string, applicationType: string): void {
        this.loadingText = "Generating File...";
        this.SpinnerService.show();
        this.stdNwaService.viewDIJustificationPDF(pdfId).subscribe(
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
        );
    }
    editWorkshopDraft(): void {
        this.loadingText = "Saving...";
        this.SpinnerService.show();
        //console.log(this.prepareWorkShopDraftFormGroup.value);
        this.stdNwaService.editWorkshopDraft(this.prepareWorkShopDraftFormGroup.value).subscribe(
            (response ) => {
                console.log(response);
                this.SpinnerService.hide();
                this.showToasterSuccess('Success', `Uploading WorkShop Draft`);
                this.onClickSaveWD(response.body.savedRowID)
                this.prepareWorkShopDraftFormGroup.reset();
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Workshop Draft Was Not uploaded`);
                console.log(error.message);
            }
        );
    }
    onClickSaveWD(nwaWDid: string) {
        if (this.uploadedFiles.length > 0) {
            const file = this.uploadedFiles;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }

            this.SpinnerService.show();
            this.stdNwaService.uploadWDFileDetails(nwaWDid, formData).subscribe(
                (data: any) => {
                    this.SpinnerService.hide();
                    this.showToasterSuccess(data.httpStatus, `WorkShop Draft Uploaded`);
                    this.uploadedFiles = null;
                    this.getNwaTasks();
                    console.log(data);
                    this.dtTrigger.next();
                    swal.fire({
                        title: 'WorkShop Draft Uploaded.',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success ',
                        },
                        icon: 'success'
                    });
                    this.hideModalEditPD();

                },
            );
        }

    }

    decisionOnWd(): void {
        this.loadingText = "Approving Work-Shop Draft...";
        this.SpinnerService.show();
        this.stdNwaService.decisionOnWd(this.approveWorkShopDraftFormGroup.value).subscribe(
            (response ) => {
                //console.log(response);
                this.getNwaTasks();
                this.SpinnerService.hide();
                this.showToasterSuccess('Success', `Work-Shop Draft Approving`);
                this.approveWorkShopDraftFormGroup.reset();
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Approving Work-Shop Draft`);
                console.log(error.message);
            }
        );
        this.hideModalApproveWorkshopDraft();
    }

    decisionRejectWd(): void {
        this.loadingText = "Rejecting Work-Shop Draft...";
        this.SpinnerService.show();
        this.stdNwaService.decisionOnWd(this.rejectWorkShopDraftFormGroup.value).subscribe(
            (response ) => {
                //console.log(response);
                this.getNwaTasks();
                this.SpinnerService.hide();
                this.showToasterSuccess('Success', `Work-Shop Draft Declined`);
                this.rejectWorkShopDraftFormGroup.reset();
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Rejecting Work-Shop Draft`);
                console.log(error.message);
            }
        );
        this.hideModalApproveWorkshopDraft();
    }



    onNwaUpload(): void {
        this.loadingText = "Saving...";
        this.SpinnerService.show();
        //console.log(this.prepareStandardFormGroup.value);
        this.stdNwaService.uploadNwaStandard(this.prepareStandardFormGroup.value).subscribe(
            (response ) => {
                console.log(response);
                this.SpinnerService.hide();
                this.showToasterSuccess('Success', `Uploading Standard`);
                this.onClickSaveSTD(response.body.savedRowID)
                this.prepareStandardFormGroup.reset();
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Standard Was Not uploaded`);
                console.log(error.message);
            }
        );

    }

    onClickSaveSTD(nwaSTDid: string) {
        if (this.uploadedFiles.length > 0) {
            const file = this.uploadedFiles;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }

            this.SpinnerService.show();
            this.stdNwaService.uploadSTDFileDetails(nwaSTDid, formData).subscribe(
                (data: any) => {
                    this.SpinnerService.hide();
                    this.showToasterSuccess(data.httpStatus, `Standard Uploaded`);
                    this.uploadedFiles = null;
                    console.log(data);
                    this.getNwaTasks();
                    this.dtTrigger.next();
                    swal.fire({
                        title: 'Standard Uploaded.',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success ',
                        },
                        icon: 'success'
                    });

                },
            );
            this.hideModalUploadStandard();
        }

    }
    onSave(): void {
        this.loadingText = "Saving...";
        this.SpinnerService.show();
        this.stdNwaService.uploadGazetteNotice(this.uploadNoticeFormGroup.value).subscribe(
            (response ) => {
                console.log(response);
                this.SpinnerService.hide();
                this.showToasterSuccess(response.httpStatus, `Gazette Notice Uploaded`);
                this.getNwaTasks();
                this.uploadNoticeFormGroup.reset();

            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Uploading Gazette Notice`);
                console.log(error.message);
            }
        );
        this.hideModalUploadNotice();
    }

    onUpdate(): void {
        this.loadingText = "Updating...";
        this.SpinnerService.show();
        this.stdNwaService.updateGazettementDate(this.updateNoticeFormGroup.value).subscribe(
            (response ) => {
                console.log(response);
                this.SpinnerService.hide();
                this.showToasterSuccess(response.httpStatus, `Gazette Notice Updated`);
                this.getNwaTasks();
                this.updateNoticeFormGroup.reset();

            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Updating Gazette Notice`);
                console.log(error.message);
            }
        );
        this.hideModalUpdateNotice();
    }


    viewSTDFile(pdfId: number, fileName: string, applicationType: string): void {
        this.loadingText = "Generating Document...";
        this.SpinnerService.show();
        this.stdNwaService.viewStandardPDF(pdfId).subscribe(
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

    public getKnwSecretary(): void {
        this.SpinnerService.show();
        this.stdNwaService.getKnwSecretary().subscribe(
            (response: UsersEntity[]) => {
                this.SpinnerService.hide();
                this.knwSecretary = response;
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                alert(error.message);
            }
        );
    }

    public getDirector(): void {
        this.SpinnerService.show();
        this.stdNwaService.getDirector().subscribe(
            (response: UsersEntity[]) => {
                this.SpinnerService.hide();
                this.diDirector = response;
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                alert(error.message);
            }
        );
    }

    public getHeadOfPublishing(): void {
        this.SpinnerService.show();
        this.stdNwaService.getDirector().subscribe(
            (response: UsersEntity[]) => {
                this.SpinnerService.hide();
                this.headOfPublishing = response;
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                alert(error.message);
            }
        );
    }

    public getSacSecretary(): void {
        this.SpinnerService.show();
        this.stdNwaService.getDirector().subscribe(
            (response: UsersEntity[]) => {
                this.SpinnerService.hide();
                this.sacSecretary = response;
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                alert(error.message);
            }
        );
    }

    public getHeadOfSic(): void {
        this.SpinnerService.show();
        this.stdNwaService.getDirector().subscribe(
            (response: UsersEntity[]) => {
                this.SpinnerService.hide();
                this.headOfSic = response;
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                alert(error.message);
            }
        );
    }

    public getSpcSecretary(): void {
        this.SpinnerService.show();
        this.stdNwaService.getDirector().subscribe(
            (response: UsersEntity[]) => {
                this.SpinnerService.hide();
                this.spcSecretary = response;
                console.log(this.spcSecretary);
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                alert(error.message);
            }
        );
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

    viewJustificationPDF(pdfId: number, fileName: string, applicationType: string): void {
        this.loadingText = "Generating Document...";
    this.SpinnerService.show();
    this.stdNwaService.viewJustificationPDF(pdfId).subscribe(
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
    );
  }

  viewPDFile(pdfId: number, fileName: string, applicationType: string): void {
      this.loadingText = "Generating Document...";
    this.SpinnerService.show();
    this.stdNwaService.viewPreliminaryDraftPDF(pdfId).subscribe(
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
    );
  }

    public getUserRoles(): void{
        this.store$.select(selectUserInfo).pipe().subscribe((u) => {
            this.roles = u.roles;
            console.log(this.roles);
            //return this.roles = u.roles;
        });
    }

    @ViewChild('closeModalReviewJustification') private closeModalReviewJustification: ElementRef | undefined;

    public hideModelCloseModalReviewJustification() {
        this.closeModalReviewJustification?.nativeElement.click();
    }

    @ViewChild('closeModalViewJustification') private closeModalViewJustification: ElementRef | undefined;

    public hideModelCloseModalViewJustification() {
        this.closeModalViewJustification?.nativeElement.click();
    }

    @ViewChild('closeModalPrepDiSdt') private closeModalPrepDiSdt: ElementRef | undefined;

    public hideModelCloseModalPrepDiSdt() {
        this.closeModalPrepDiSdt?.nativeElement.click();
    }

    @ViewChild('closeModalApproveDiJustification') private closeModalApproveDiJustification: ElementRef | undefined;

    public hideModalApproveDiJustification() {
        this.closeModalApproveDiJustification?.nativeElement.click();
    }

    @ViewChild('closeModalPreparePD') private closeModalPreparePD: ElementRef | undefined;

    public hideModalPreparePD() {
        this.closeModalPreparePD?.nativeElement.click();
    }

    @ViewChild('closeModalApprovePD') private closeModalApprovePD: ElementRef | undefined;

    public hideModalApprovePD() {
        this.closeModalApprovePD?.nativeElement.click();
    }

    @ViewChild('closeModalEditPD') private closeModalEditPD: ElementRef | undefined;

    public hideModalEditPD() {
        this.closeModalEditPD?.nativeElement.click();
    }

    @ViewChild('closeModalWorkshopDraft') private closeModalWorkshopDraft: ElementRef | undefined;

    public hideModalWorkshopDraft() {
        this.closeModalWorkshopDraft?.nativeElement.click();
    }

    @ViewChild('closeModalApproveWorkshopDraft') private closeModalApproveWorkshopDraft: ElementRef | undefined;

    public hideModalApproveWorkshopDraft() {
        this.closeModalApproveWorkshopDraft?.nativeElement.click();
    }

    @ViewChild('closeModalUploadStandard') private closeModalUploadStandard: ElementRef | undefined;

    public hideModalUploadStandard() {
        this.closeModalUploadStandard?.nativeElement.click();
    }

    @ViewChild('closeModalUploadNotice') private closeModalUploadNotice: ElementRef | undefined;

    public hideModalUploadNotice() {
        this.closeModalUploadNotice?.nativeElement.click();
    }

    @ViewChild('closeModalUpdateNotice') private closeModalUpdateNotice: ElementRef | undefined;

    public hideModalUpdateNotice() {
        this.closeModalUpdateNotice?.nativeElement.click();
    }



}
