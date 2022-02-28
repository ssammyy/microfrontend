import {Component, ElementRef, OnInit, ViewChild, ViewEncapsulation} from '@angular/core';
import {Subject} from "rxjs";
import {UsersEntity} from "../../../core/store/data/std/std.model";
import {ManufacturePendingTask} from "../../../core/store/data/levy/levy.model";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {Store} from "@ngrx/store";
import {LevyService} from "../../../core/store/data/levy/levy.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../core/store/data/std/notification.service";
import {selectUserInfo} from "../../../core/store";
import {HttpErrorResponse} from "@angular/common/http";
import {DataTableDirective} from "angular-datatables";
import swal from "sweetalert2";

declare const $: any;
@Component({
  selector: 'app-std-levy-pending-tasks',
  templateUrl: './std-levy-pending-tasks.component.html',
  styleUrls: ['./std-levy-pending-tasks.component.css','../../../../../node_modules/@ng-select/ng-select/themes/default.theme.css'],
  encapsulation: ViewEncapsulation.None
})
export class StdLevyPendingTasksComponent implements OnInit {
  userId: number ;
  roles: string[];
  userType: number ;

  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  @ViewChild(DataTableDirective, {static: false})
  dtElement: DataTableDirective;
  isDtInitialized: boolean = false

  public users !: UsersEntity[] ;
  public approveUsersOne !: UsersEntity[] ;
  public approveUsersTwo !: UsersEntity[] ;
  public approveUsersThree !: UsersEntity[] ;
  public assignUsersOne !: UsersEntity[] ;
  public assignUsersTwo !: UsersEntity[] ;
  public assignUsersThree !: UsersEntity[] ;
  approvedUsersOne: number;
  approvedUsersTwo: number;
  approvedUsersThree: number;
  assignedUsersOne: number;
  assignedUsersTwo: number;
  assignedUsersThree: number;
  manufacturePendingTasks: ManufacturePendingTask[] = [];
  public actionRequestPending: ManufacturePendingTask | undefined;
  public scheduleVisitFormGroup!: FormGroup;
  public prepareReportFormGroup!: FormGroup;
  public prepareFeedBackFormGroup!: FormGroup;
  public approvalFormGroup!: FormGroup;
  public rejectFormGroup!: FormGroup;
  public approvalTwoFormGroup!: FormGroup;
  public approveTwoFormGroup!: FormGroup;
  public rejectTwoFormGroup!: FormGroup;
  blob: Blob;
  public uploadedFiles:  FileList;
  isShowScheduleForm = true;
  isShowReportForm= true;
  isShowApprovalForm1 = true;
  isShowRejectForm1= true;
  isShowApprovalForm2 = true;
  isShowRejectForm2= true;
  isShowSaveFeedBackForm= true;
  isShowRemarksTab= true;

  toggleDisplayScheduleForm() {
    this.isShowScheduleForm = !this.isShowScheduleForm;
    this.isShowReportForm= true;
    this.isShowRemarksTab= true;
  }
  toggleDisplayReportForm() {
    this.isShowReportForm = !this.isShowReportForm;
    this.isShowScheduleForm= true;
    this.isShowRemarksTab= true;
  }
  toggleDisplayRemarksTab(){
    this.isShowRemarksTab = !this.isShowRemarksTab;
    this.isShowReportForm=true;
    this.isShowScheduleForm= true;
  }
  toggleDisplayApprovalForm1() {
    this.isShowApprovalForm1 = !this.isShowApprovalForm1;
    this.isShowRejectForm1 = true;
  }
  toggleDisplayRejectForm1() {
    this.isShowRejectForm1 = !this.isShowRejectForm1;
    this.isShowApprovalForm1= true;
  }
  toggleDisplayApprovalForm2() {
    this.isShowApprovalForm2 = !this.isShowApprovalForm2;
    this.isShowRejectForm2 = true;
  }
  toggleDisplayRejectForm2() {
    this.isShowRejectForm2 = !this.isShowRejectForm2;
    this.isShowApprovalForm1= true;
  }
  toggleDisplaySaveFeedBackForm() {
    this.isShowSaveFeedBackForm = !this.isShowSaveFeedBackForm;
  }
  constructor(
      private router: Router,
      private store$: Store<any>,
      private formBuilder: FormBuilder,
      private levyService: LevyService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService
  ) { }

  ngOnInit(): void {
    this.getMnPendingTask();
    this.getUserRoles();
    this.getUserData();

    this.dtOptions = {
      pagingType: 'full_numbers',
      pageLength: 10,
      processing: true
    };

    if(this.roles?.includes('SL_IS_PL_OFFICER')){
      this.getApproveLevelOne();
    }
    if(this.roles?.includes('SL_IS_ASST_MANAGER')){
      this.getApproveLevelTwo();
      this.getAssignLevelOne();
    }
    if(this.roles?.includes('SL_IS_MANAGER')){
      this.getApproveLevelThree();
      this.getAssignLevelTwo()
    }
    if(this.roles?.includes('SL_IS_CHIEF_MANAGER')){
      this.getAssignLevelThree()
    }

    this.scheduleVisitFormGroup = this.formBuilder.group({
      scheduledVisitDate: ['', Validators.required],
      manufacturerEntity: [],
      taskId: [],
      entryNumber: [],
      companyName: [],
      kraPin: [],
      registrationNumber: []

    });
    this.prepareReportFormGroup = this.formBuilder.group({
      visitDate: ['', Validators.required],
      purpose: ['', Validators.required],
      personMet: ['', Validators.required],
      actionTaken: ['', Validators.required],
      makeRemarks: ['', Validators.required],
      taskId: [],
      visitID: [],
      assigneeId: [],
      manufacturerEntity: [],
      userType: []

    });
    this.prepareFeedBackFormGroup = this.formBuilder.group({
      officersFeedback: ['', Validators.required],
      taskId: [],
      visitID: [],
      assigneeId: [],
      manufacturerEntity: []

    });

    this.approvalFormGroup = this.formBuilder.group({
      comments: ['', Validators.required],
      assigneeId: [],
      visitID: [],
      accentTo: ['', Validators.required],
      taskId: [],
      manufacturerEntity: []

    });
    this.rejectFormGroup = this.formBuilder.group({
      comments: ['', Validators.required],
      assigneeId: [],
      visitID: [],
      accentTo: ['', Validators.required],
      taskId: [],
      manufacturerEntity: []

    });

    this.approvalTwoFormGroup = this.formBuilder.group({
      comments: ['', Validators.required],
      assigneeId: [],
      visitID: [],
      accentTo: [],
      taskId: [],
      manufacturerEntity: []

    });
    this.approveTwoFormGroup = this.formBuilder.group({
      comments: ['', Validators.required],
      assigneeId: [],
      visitID: [],
      accentTo: [],
      taskId: [],
      manufacturerEntity: []

    });

    this.rejectTwoFormGroup = this.formBuilder.group({
      comments: ['', Validators.required],
      assigneeId: [],
      visitID: [],
      accentTo: [],
      taskId: [],
      manufacturerEntity: [],
      userType: []

    });


  }
  get scheduleVisitForm(): any {
    return this.scheduleVisitFormGroup.controls;
  }

  get formPrepareReport(): any {
    return this.prepareReportFormGroup.controls;
  }
  get formPrepareFeedBack(): any {
    return this.prepareFeedBackFormGroup.controls;
  }
  get formApproval(): any {
    return this.approvalFormGroup.controls;
  }
  get formReject(): any {
    return this.rejectFormGroup.controls;
  }
  get formApprovalTwo(): any {
    return this.approvalTwoFormGroup.controls;
  }
  get formApproveTwo(): any {
    return this.approveTwoFormGroup.controls;
  }
  get formRejectTwo(): any {
    return this.rejectTwoFormGroup.controls;
  }
  showToasterSuccess(title:string,message:string){
    this.notifyService.showSuccess(message, title)

  }
  showToasterError(title:string,message:string){
    this.notifyService.showError(message, title)

  }
  public getUserData(): void{
    this.store$.select(selectUserInfo).pipe().subscribe((u) => {
      this.userId = u.id;
    });
  }
  public getUserRoles(): void{
    this.store$.select(selectUserInfo).pipe().subscribe((u) => {
      this.roles = u.roles;
      console.log(this.roles);
      //return this.roles = u.roles;
    });
  }
  public getApproveLevelOne(): void {
    this.SpinnerService.show();
    this.levyService.getApproveLevelOne().subscribe(
        (response: UsersEntity[]) => {
          this.SpinnerService.hide();
          this.approveUsersOne = response;
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }

  public getApproveLevelTwo(): void {
    this.SpinnerService.show();
    this.levyService.getApproveLevelTwo().subscribe(
        (response: UsersEntity[]) => {
          this.SpinnerService.hide();
          this.approveUsersTwo = response;
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }

  public getAssignLevelOne(): void {
    this.SpinnerService.show();
    this.levyService.getAssignLevelOne().subscribe(
        (response: UsersEntity[]) => {
          this.SpinnerService.hide();
          this.assignUsersOne = response;
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }

  public getAssignLevelTwo(): void {
    this.SpinnerService.show();
    this.levyService.getAssignLevelTwo().subscribe(
        (response: UsersEntity[]) => {
          this.SpinnerService.hide();
          this.assignUsersTwo = response;
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }

  public getAssignLevelThree(): void {
    this.SpinnerService.show();
    this.levyService.getAssignLevelThree().subscribe(
        (response: UsersEntity[]) => {
          this.SpinnerService.hide();
          this.assignUsersThree = response;
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }

  public getApproveLevelThree(): void {
    this.SpinnerService.show();
    this.levyService.getApproveLevelThree().subscribe(
        (response: UsersEntity[]) => {
          this.SpinnerService.hide();
          this.approveUsersThree = response;
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }
  public getMnPendingTask(): void{
    this.SpinnerService.show();
    this.levyService.getMnPendingTask().subscribe(
        (response: ManufacturePendingTask[])=> {
          //console.log(this.manufacturePendingTasks);
          this.manufacturePendingTasks = response;
          this.SpinnerService.hide();
          if (this.isDtInitialized) {
            this.dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
              dtInstance.destroy();
              this.dtTrigger.next();
            });
          } else {
            this.isDtInitialized = true
            this.dtTrigger.next();
          }

        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
          console.log(error.message);
        }
    );
  }

  public onOpenModalPending(manufacturePendingTask: ManufacturePendingTask,mode:string): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle','modal');
    if (mode==='viewPending'){
      this.actionRequestPending=manufacturePendingTask;
      button.setAttribute('data-target','#viewPending');
    }
    if (mode==='prepareSiteVisitReport'){
      this.actionRequestPending=manufacturePendingTask;
      button.setAttribute('data-target','#prepareSiteVisitReport');
    }
    if (mode==='reportNotification1'){
      this.actionRequestPending=manufacturePendingTask;
      button.setAttribute('data-target','#reportNotification1');
    }
    if (mode==='reportNotification2'){
      this.actionRequestPending=manufacturePendingTask;
      button.setAttribute('data-target','#reportNotification2');
    }
    if (mode==='managementReport'){
      this.actionRequestPending=manufacturePendingTask;
      button.setAttribute('data-target','#managementReport');
    }
    if (mode==='draftFeedback'){
      this.actionRequestPending=manufacturePendingTask;
      button.setAttribute('data-target','#draftFeedback');
    }
    // @ts-ignore
    container.appendChild(button);
    button.click();

  }
  scheduleVisit(): void {

    this.SpinnerService.show();
    this.levyService.scheduleSiteVisit(this.scheduleVisitFormGroup.value).subscribe(
        (response ) => {
          console.log(response);
          this.getMnPendingTask();
          this.SpinnerService.hide();
          this.showToasterSuccess(response.httpStatus, `Visit Scheduled`);
          this.scheduleVisitFormGroup.reset();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Scheduling Visit`);
          console.log(error.message);
        }
    );
  }
  saveReport(): void {
    this.SpinnerService.show();
    this.levyService.saveSiteVisitReport(this.prepareReportFormGroup.value).subscribe(
        (response ) => {
          console.log(response);
          this.getMnPendingTask();
          this.SpinnerService.hide();

          this.onClickSaveUploads(response.body.savedRowID)
          this.prepareReportFormGroup.reset();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          console.log(error.message);
        }
    );
  }
  onClickSaveUploads(reportFileID: string) {
    if (this.uploadedFiles.length > 0) {
      const file = this.uploadedFiles;
      const formData = new FormData();
      for (let i = 0; i < file.length; i++) {
        console.log(file[i]);
        formData.append('docFile', file[i], file[i].name);
      }
      this.SpinnerService.show();
      this.levyService.uploadFileDetails(reportFileID, formData).subscribe(
          (data: any) => {
            this.SpinnerService.hide();
            this.uploadedFiles = null;
            console.log(data);
            swal.fire({
              title: 'Report Prepared.',
              buttonsStyling: false,
              customClass: {
                confirmButton: 'btn btn-success form-wizard-next-btn ',
              },
              icon: 'success'
            });
          },
          (error: HttpErrorResponse) => {
            this.SpinnerService.hide();
            console.log(error.message);
          }
      );
    }

  }
  onDecision(): void {

    this.SpinnerService.show();
    this.levyService.levelOneDecisionOnReport(this.approvalFormGroup.value).subscribe(
        (response ) => {
          console.log(response);
          this.getMnPendingTask();
          this.SpinnerService.hide();
          this.showToasterSuccess(response.httpStatus, `Report Approved`);
          this.approvalFormGroup.reset();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Approving Report; Try Again`);
          console.log(error.message);
        }
    );
  }

  onDecisionReject(): void {

    this.SpinnerService.show();
    this.levyService.levelOneDecisionOnReport(this.rejectFormGroup.value).subscribe(
        (response ) => {
          console.log(response);
          this.getMnPendingTask();
          this.SpinnerService.hide();
          this.showToasterSuccess(response.httpStatus, `Report Rejected`);
          this.rejectFormGroup.reset();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Rejecting Report; Try Again`);
          console.log(error.message);
        }
    );
  }



  viewPdfFile(pdfId: number, fileName: string, applicationType: string): void {
    this.SpinnerService.show();
    this.levyService.viewReportDoc(pdfId).subscribe(
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
  onDecisionLevelTwo(): void {

    this.SpinnerService.show();
    this.levyService.decisionOnSiteReportLevelTwo(this.approvalTwoFormGroup.value).subscribe(
        (response ) => {
          console.log(response);
          this.getMnPendingTask();
          this.SpinnerService.hide();
          this.showToasterSuccess(response.httpStatus, `Report Approved`);
          this.approvalTwoFormGroup.reset();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Approving Report; Try Again`);
          console.log(error.message);
        }
    );
  }

  onDecisionTwo(): void {

    this.SpinnerService.show();
    this.levyService.decisionOnSiteReportLevelTwo(this.approveTwoFormGroup.value).subscribe(
        (response ) => {
          console.log(response);
          this.getMnPendingTask();
          this.SpinnerService.hide();
          this.showToasterSuccess(response.httpStatus, `Report Approved`);
          this.approveTwoFormGroup.reset();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Approving Report; Try Again`);
          console.log(error.message);
        }
    );
  }

  onDecisionRejectLevelTwo(): void {

    this.SpinnerService.show();
    this.levyService.decisionOnSiteReportLevelTwo(this.rejectTwoFormGroup.value).subscribe(
        (response ) => {
          console.log(response);
          this.getMnPendingTask();
          this.SpinnerService.hide();
          this.showToasterSuccess(response.httpStatus, `Report Rejected`);
          this.rejectTwoFormGroup.reset();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Rejecting Report; Try Again`);
          console.log(error.message);
        }
    );
  }



  saveFeedBack(): void {
    this.SpinnerService.show();
    this.levyService.saveSiteVisitFeedback(this.prepareFeedBackFormGroup.value).subscribe(
        (response ) => {
          console.log(response);
          this.getMnPendingTask();
          this.SpinnerService.hide();
          this.showToasterSuccess(response.httpStatus, `Feedback Saved and Sent`);
          this.prepareFeedBackFormGroup.reset();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Saving FeedBack; Try Again`);
          console.log(error.message);
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

  @ViewChild('closeModal') private closeModal: ElementRef | undefined;

  public hideModel() {
    this.closeModal?.nativeElement.click();
  }

}
