import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren, ViewEncapsulation} from '@angular/core';
import {Subject} from "rxjs";
import {UsersEntity} from "../../../core/store/data/std/std.model";
import {DocumentDTO, ManufacturePendingTask, SiteVisitRemarks, SlModel} from "../../../core/store/data/levy/levy.model";
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
  userType: number;
    approve: string;
    reject: string;
    plOfficer: string;
    astManager: string;
    manager: string;
    chiefManager: string;
    approveStatus: string;
    rejectStatus: string;
    role:string;

    documentDTOs: DocumentDTO[] = [];
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
    siteVisitRemarks: SiteVisitRemarks[] = [];
  public actionRequestPending: ManufacturePendingTask | undefined;
    slFormDetails !: SlModel;
  public scheduleVisitFormGroup!: FormGroup;
  public cancelVisitFormGroup!: FormGroup;
  public siteConfirmFormGroup!: FormGroup;
  public siteEditFormGroup!: FormGroup;
  public prepareReportFormGroup!: FormGroup;
  public prepareFeedBackFormGroup!: FormGroup;
  public approvalFormGroup!: FormGroup;
  public rejectFormGroup!: FormGroup;
  public approvalTwoFormGroup!: FormGroup;
  public approveTwoFormGroup!: FormGroup;
  public rejectTwoFormGroup!: FormGroup;
    public approveCompanyLevelOneFormGroup!: FormGroup;
    public rejectCompanyLevelOneFormGroup!: FormGroup;
    public approveEditRequestFormGroup!: FormGroup;
    public rejectEditRequestFormGroup!: FormGroup;
    public rejectEditRequestLevelTwoFormGroup!: FormGroup;
    public approveEditRequestLevelTwoFormGroup!: FormGroup;
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
    isShowDocumentsTab= true;
    isShowApproveRequestForm=true;
    isShowRejectRequestForm=true;
    isShowSLForm = true;
    isShowCompanyDetails=true;
    isShowCompanyRemarks=true;
    isShowSiteCancelForm=true;
    isShowSiteEdit=true;
    isShowSiteConfirmForm=true;


    toggleDisplaySLForm(manufactureId: number) {
        this.loadingText = "Loading ...."
        this.SpinnerService.show();
        this.levyService.getCompanySLForms(manufactureId).subscribe(
            (response: SlModel)=> {
                this.slFormDetails = response;
                //console.log(this.slFormDetails);
                this.SpinnerService.hide();
            },
            (error: HttpErrorResponse)=>{
                this.SpinnerService.hide();
                console.log(error.message)
                //alert(error.message);
            }
        );

        this.isShowSLForm = !this.isShowSLForm;
        this.isShowApproveRequestForm=true;
        this.isShowRejectRequestForm=true;
        this.isShowDocumentsTab=true;
        this.isShowRejectForm2 = true;
        this.isShowApprovalForm2 = true;
        this.isShowApprovalForm1= true;
        this.isShowRejectForm1= true;

    }
    toggleDisplayCompanyDetails(){
        this.isShowCompanyDetails=!this.isShowCompanyDetails;
        this.isShowCompanyRemarks=true;
        this.isShowApproveRequestForm=true;
        this.isShowRejectRequestForm=true;
        this.isShowDocumentsTab=true;
        this.isShowRejectForm2 = true;
        this.isShowApprovalForm2 = true;
        this.isShowApprovalForm1= true;
        this.isShowRejectForm1= true;
    }

    toggleDisplayCompanyRemarks(editID: number){
        this.loadingText = "Loading ...."
        this.SpinnerService.show();
        this.levyService.getComEditRemarks(editID).subscribe(
            (response: SiteVisitRemarks[]) => {
                this.siteVisitRemarks = response;
                this.SpinnerService.hide();
                console.log(this.siteVisitRemarks)
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                console.log(error.message);
            }
        );
        this.isShowCompanyRemarks = !this.isShowCompanyRemarks;
        this.isShowCompanyDetails=true;
        this.isShowRemarksTab=true;
        this.isShowReportForm=true;
        this.isShowScheduleForm= true;
        this.isShowRejectForm1 = true;
        this.isShowApprovalForm1 = true;
        this.isShowDocumentsTab = true;
        this.isShowApproveRequestForm=true;
        this.isShowRejectRequestForm=true;
        this.isShowRejectForm2 = true;
        this.isShowApprovalForm2 = true;
    }


    toggleDisplayApproveEditRequest(){
      this.isShowApproveRequestForm=!this.isShowApproveRequestForm;
        this.isShowCompanyRemarks=true;
        this.isShowCompanyDetails=true;
      this.isShowRejectRequestForm=true;
      this.isShowDocumentsTab=true;
        this.isShowRejectForm2 = true;
        this.isShowApprovalForm2 = true;
        this.isShowApprovalForm1= true;
        this.isShowRejectForm1= true;
    }
    toggleDisplayRejectEditRequest(){
        this.isShowRejectRequestForm=!this.isShowRejectRequestForm;
        this.isShowApproveRequestForm=true;
        this.isShowDocumentsTab=true;
        this.isShowCompanyDetails=true;
        this.isShowRejectForm2 = true;
        this.isShowApprovalForm2 = true;
        this.isShowApprovalForm1= true;
        this.isShowRejectForm1= true;
    }



    toggleCancelScheduleForm() {
        this.isShowSiteCancelForm = !this.isShowSiteCancelForm;
        this.isShowSiteEdit= true;
        this.isShowSiteConfirmForm= true;
        this.isShowCompanyDetails= true;
        this.isShowScheduleForm= true;
        this.isShowReportForm= true;
        this.isShowRemarksTab= true;
        this.isShowDocumentsTab= true;
        this.isShowApproveRequestForm=true;
        this.isShowRejectRequestForm=true;
        this.isShowRejectForm2 = true;
        this.isShowApprovalForm2 = true;
        this.isShowApprovalForm1= true;
        this.isShowRejectForm1= true;
    }

    toggleEditScheduleForm() {
        this.isShowSiteEdit = !this.isShowSiteEdit;
        this.isShowSiteCancelForm= true;
        this.isShowSiteConfirmForm= true;
        this.isShowScheduleForm = true;
        this.isShowReportForm= true;
        this.isShowRemarksTab= true;
        this.isShowDocumentsTab= true;
        this.isShowApproveRequestForm=true;
        this.isShowRejectRequestForm=true;
        this.isShowRejectForm2 = true;
        this.isShowApprovalForm2 = true;
        this.isShowApprovalForm1= true;
        this.isShowRejectForm1= true;
    }

    toggleConfirmScheduleForm() {
        this.isShowSiteConfirmForm = !this.isShowSiteConfirmForm;
        this.isShowSiteCancelForm= true;
        this.isShowSiteEdit= true;
        this.isShowScheduleForm = true;
        this.isShowReportForm= true;
        this.isShowRemarksTab= true;
        this.isShowDocumentsTab= true;
        this.isShowApproveRequestForm=true;
        this.isShowRejectRequestForm=true;
        this.isShowRejectForm2 = true;
        this.isShowApprovalForm2 = true;
        this.isShowApprovalForm1= true;
        this.isShowRejectForm1= true;
    }

  toggleDisplayScheduleForm() {
    this.isShowScheduleForm = !this.isShowScheduleForm;
    this.isShowReportForm= true;
    this.isShowRemarksTab= true;
    this.isShowDocumentsTab= true;
    this.isShowApproveRequestForm=true;
    this.isShowRejectRequestForm=true;
      this.isShowRejectForm2 = true;
      this.isShowApprovalForm2 = true;
      this.isShowApprovalForm1= true;
      this.isShowRejectForm1= true;
  }
  toggleDisplayReportForm() {
    this.isShowReportForm = !this.isShowReportForm;
    this.isShowScheduleForm= true;
    this.isShowRemarksTab= true;
    this.isShowDocumentsTab= true;
      this.isShowApproveRequestForm=true;
      this.isShowRejectRequestForm=true;
      this.isShowRejectForm2 = true;
      this.isShowApprovalForm2 = true;
      this.isShowApprovalForm1= true;
      this.isShowRejectForm1= true;
  }
  toggleDisplayRemarksTab(siteVisitId: number){
      this.loadingText = "Loading ...."
      this.SpinnerService.show();
      this.levyService.getSiteVisitRemarks(siteVisitId).subscribe(
          (response: SiteVisitRemarks[]) => {
              this.siteVisitRemarks = response;
              this.SpinnerService.hide();
              console.log(this.siteVisitRemarks)
          },
          (error: HttpErrorResponse) => {
              this.SpinnerService.hide();
              console.log(error.message);
          }
      );
    this.isShowRemarksTab = !this.isShowRemarksTab;
    this.isShowReportForm=true;
    this.isShowScheduleForm= true;
      this.isShowRejectForm1 = true;
      this.isShowApprovalForm1 = true;
      this.isShowDocumentsTab = true;
      this.isShowApproveRequestForm=true;
      this.isShowRejectRequestForm=true;
      this.isShowRejectForm2 = true;
      this.isShowApprovalForm2 = true;
  }
  toggleDisplayApprovalForm1() {
    this.isShowApprovalForm1 = !this.isShowApprovalForm1;
    this.isShowRejectForm1 = true;
    this.isShowRemarksTab = true;
    this.isShowDocumentsTab = true;
      this.isShowApproveRequestForm=true;
      this.isShowRejectRequestForm=true;
      this.isShowRejectForm2 = true;
      this.isShowApprovalForm2 = true;
  }
  toggleDisplayRejectForm1() {
    this.isShowRejectForm1 = !this.isShowRejectForm1;
    this.isShowApprovalForm1= true;
    this.isShowRemarksTab= true;
    this.isShowDocumentsTab= true;
      this.isShowApproveRequestForm=true;
      this.isShowRejectRequestForm=true;
      this.isShowRejectForm2 = true;
      this.isShowApprovalForm2 = true;
  }
  toggleDisplayApprovalForm2() {
    this.isShowApprovalForm2 = !this.isShowApprovalForm2;
    this.isShowRejectForm2 = true;
    this.isShowRemarksTab = true;
    this.isShowDocumentsTab = true;
      this.isShowApproveRequestForm=true;
      this.isShowRejectRequestForm=true;
      this.isShowApprovalForm1= true;
      this.isShowRejectForm1= true;
  }

  toggleDisplayRejectForm2() {
    this.isShowRejectForm2 = !this.isShowRejectForm2;
    this.isShowApprovalForm1 = true;
    this.isShowRemarksTab = true;
    this.isShowDocumentsTab = true;
      this.isShowApproveRequestForm=true;
      this.isShowRejectRequestForm=true;
      this.isShowApprovalForm2 = true;
      this.isShowApprovalForm1= true;
      this.isShowRejectForm1= true;
  }

  toggleDisplaySaveFeedBackForm() {
    this.isShowSaveFeedBackForm = !this.isShowSaveFeedBackForm;
    this.isShowRemarksTab =  true;
    this.isShowDocumentsTab =  true;
      this.isShowApproveRequestForm=true;
      this.isShowRejectRequestForm=true;
      this.isShowRejectForm2 = true;
      this.isShowApprovalForm2 = true;
      this.isShowApprovalForm1= true;
      this.isShowRejectForm1= true;
  }

    toggleDisplayDocuments(visitId: number) {
        this.loadingText = "Loading ...."
        this.SpinnerService.show();
        this.levyService.getVisitDocumentList(visitId).subscribe(
            (response: DocumentDTO[]) => {
                this.documentDTOs = response;
                this.SpinnerService.hide();
                console.log(this.documentDTOs)
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                console.log(error.message);
            }
        );
        this.isShowDocumentsTab = !this.isShowDocumentsTab;
        this.isShowRejectForm2 = true;
        this.isShowApprovalForm2 = true;
        this.isShowRemarksTab = true;
        this.isShowApproveRequestForm=true;
        this.isShowRejectRequestForm=true;
        this.isShowApprovalForm1= true;
        this.isShowRejectForm1= true;

    }

    dtOptions: DataTables.Settings = {};
    @ViewChildren(DataTableDirective)
    dtElements: QueryList<DataTableDirective>;
    dtTrigger1: Subject<any> = new Subject<any>();
    dtTrigger2: Subject<any> = new Subject<any>();
  loadingText: string;

  constructor(
      private router: Router,
      private store$: Store<any>,
      private formBuilder: FormBuilder,
      private levyService: LevyService,
      private SpinnerService: NgxSpinnerService,
      private notifyService: NotificationService
  ) {
  }

  ngOnInit(): void {
    this.getMnPendingTask();
    this.getUserRoles();
    this.getUserData();
    this.approve='true';
    this.reject='false';
    this.approveStatus='Approved';
    this.rejectStatus='Rejected';

    if (this.roles?.includes('SL_IS_PL_OFFICER')) {
      this.getApproveLevelOne();
      this.userType=61;
      this.role='PL Officer';
    }
    if (this.roles?.includes('SL_IS_ASST_MANAGER')) {
      this.getApproveLevelTwo();
      this.getAssignLevelOne();
        this.userType=62;
        this.role='Assistant Manager';
    }
    if (this.roles?.includes('SL_IS_MANAGER')) {
      this.getApproveLevelThree();
      this.getAssignLevelTwo();
        this.userType=62;
        this.role='Manager';
    }
    if (this.roles?.includes('SL_IS_CHIEF_MANAGER')) {
      this.getAssignLevelThree();
        this.userType=62;
        this.role='Chief Manager';
    }
    this.cancelVisitFormGroup= this.formBuilder.group({
        accentTo: [],
        manufacturerEntity: [],
        taskId : [],
        processId : [],
        visitID : [],
        remarks: [],
        companyName:[]
    });

      this.siteConfirmFormGroup= this.formBuilder.group({
          accentTo: [],
          manufacturerEntity: [],
          taskId : [],
          processId : [],
          scheduledVisitDate: [],
          visitID: [],
          remarks: [],
          companyName:[]
      });


      this.siteEditFormGroup= this.formBuilder.group({
          accentTo: [],
          manufacturerEntity: [],
          taskId : [],
          processId : [],
          scheduledVisitDate: [],
          visitID: [],
          remarks: [],
          companyName:[]
      });

      this.approveEditRequestFormGroup = this.formBuilder.group({
          postalAddressEdit: [],
          physicalAddressEdit: [],
          companyEmailEdit:[],
          companyTelephoneEdit:[],
          yearlyTurnoverEdit:[],
          ownershipEdit: [],
          companyId: [],
          userType:[],
          taskType:[],
          assignedTo: [],
          taskId: [],
          accentTo:[],
          processId:[],
          editID:[],
          remarks: []

      });
      this.rejectEditRequestFormGroup = this.formBuilder.group({
          postalAddressEdit: [],
          physicalAddressEdit: [],
          companyEmailEdit:[],
          companyTelephoneEdit:[],
          yearlyTurnoverEdit:[],
          ownershipEdit: [],
          companyId: [],
          userType:[],
          taskType:[],
          assignedTo: [],
          taskId: [],
          accentTo:[],
          processId:[],
          editID:[],
          remarks: []


      });

      this.approveCompanyLevelOneFormGroup = this.formBuilder.group({
          postalAddressEdit: [],
          physicalAddressEdit: [],
          companyEmailEdit:[],
          companyTelephoneEdit:[],
          yearlyTurnoverEdit:[],
          ownershipEdit: [],
          companyId: [],
          userType:[],
          taskType:[],
          assignedTo: [],
          taskId: [],
          accentTo:[],
          processId:[],
          editID:[],
          remarks: []


      });
      this.rejectCompanyLevelOneFormGroup = this.formBuilder.group({
          postalAddressEdit: [],
          physicalAddressEdit: [],
          companyEmailEdit:[],
          companyTelephoneEdit:[],
          yearlyTurnoverEdit:[],
          ownershipEdit: [],
          companyId: [],
          ownership: [],
          userType:[],
          taskType:[],
          assignedTo:[],
          taskId: [],
          accentTo:[],
          processId:[],
          editID:[],
          remarks: []


      });

      this.approveEditRequestLevelTwoFormGroup = this.formBuilder.group({
          postalAddressEdit: [],
          physicalAddressEdit: [],
          companyEmailEdit:[],
          companyTelephoneEdit:[],
          yearlyTurnoverEdit:[],
          ownershipEdit: [],
          companyId: [],
          userType:[],
          taskType:[],
          assignedTo:[],
          taskId: [],
          accentTo:[],
          processId:[],
          editID:[],
          remarks: []


      });
      this.rejectEditRequestLevelTwoFormGroup = this.formBuilder.group({
          postalAddressEdit: [],
          physicalAddressEdit: [],
          companyEmailEdit:[],
          companyTelephoneEdit:[],
          yearlyTurnoverEdit:[],
          ownershipEdit: [],
          companyId: [],
          userType:[],
          taskType:[],
          assignedTo:[],
          taskId: [],
          accentTo:[],
          processId:[],
          editID:[],
          remarks: []


      });

    this.scheduleVisitFormGroup = this.formBuilder.group({
      scheduledVisitDate: ['', Validators.required],
      manufacturerEntity: [],
      taskId: [],
      entryNumber: [],
      companyName: [],
      kraPin: [],
      registrationNumber: [],
        processId: []

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
      userType: [],
        complianceStatus:[],
        processId: []

    });
    this.prepareFeedBackFormGroup = this.formBuilder.group({
      officersFeedback: ['', Validators.required],
      taskId: [],
      visitID: [],
      assigneeId: [],
      manufacturerEntity: [],
        processId: []

    });

    this.approvalFormGroup = this.formBuilder.group({
      comments: ['', Validators.required],
      assigneeId: [],
      visitID: [],
      accentTo: ['', Validators.required],
      taskId: [],
      manufacturerEntity: [],
        role:[],
        status:[],
        processId: []

    });
    this.rejectFormGroup = this.formBuilder.group({
      comments: ['', Validators.required],
      assigneeId: [],
      visitID: [],
      accentTo: ['', Validators.required],
      taskId: [],
      manufacturerEntity: [],
        role:[],
        status:[],
        processId: []

    });

    this.approvalTwoFormGroup = this.formBuilder.group({
      comments: ['', Validators.required],
      assigneeId: [],
      visitID: [],
      accentTo: [],
      taskId: [],
      manufacturerEntity: [],
        role:[],
        status:[],
        processId: []

    });
    this.approveTwoFormGroup = this.formBuilder.group({
      comments: ['', Validators.required],
      assigneeId: [],
      visitID: [],
      accentTo: [],
      taskId: [],
      manufacturerEntity: [],
        role:[],
        status:[],
        processId: []

    });

    this.rejectTwoFormGroup = this.formBuilder.group({
      comments: ['', Validators.required],
      assigneeId: [],
      visitID: [],
      accentTo: [],
      taskId: [],
      manufacturerEntity: [],
      userType: [],
        role:[],
        status:[],
        processId: []

    });


  }
    ngOnDestroy(): void {
        this.dtTrigger1.unsubscribe();
        this.dtTrigger2.unsubscribe();
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

  public getMnPendingTask(): void {

    this.loadingText = "Retrieving Pending Tasks...";
    this.SpinnerService.show();
    this.levyService.getMnPendingTask().subscribe(
        (response: ManufacturePendingTask[]) => {
            //console.log(this.manufacturePendingTasks);
            this.manufacturePendingTasks = response;
            this.rerender();

        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
          console.log(error.message);
        }
    );
  }

  public onOpenModalPending(manufacturePendingTask: ManufacturePendingTask, mode: string): void {
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');

      if (mode === 'confirmDetails') {
          this.actionRequestPending = manufacturePendingTask;
          button.setAttribute('data-target', '#confirmDetails');
          // this.approveCompanyLevelOneFormGroup.patchValue(
          //     {
          //
          //     });
      }
      if (mode === 'approveDetails') {
          this.actionRequestPending = manufacturePendingTask;
          button.setAttribute('data-target', '#approveDetails');
      }
      if (mode === 'approveEditedDetails') {
          this.actionRequestPending = manufacturePendingTask;
          button.setAttribute('data-target', '#approveEditedDetails');
      }

    if (mode === 'viewPending') {
      this.actionRequestPending = manufacturePendingTask;
      button.setAttribute('data-target', '#viewPending');
      // this.scheduleVisitFormGroup.controls.entry.setValue(this.actionRequestPending.taskData.entryNumber)

      this.scheduleVisitFormGroup.patchValue(
          {
            companyName: this.actionRequestPending.taskData.companyName,
            entryNumber: this.actionRequestPending.taskData.entryNumber,
            kraPin: this.actionRequestPending.taskData.kraPin,
            registrationNumber: this.actionRequestPending.taskData.registrationNumber,
            manufacturerEntity: this.actionRequestPending.taskData.manufacturerEntity,
            taskId: this.actionRequestPending.taskId,
              processId: this.actionRequestPending.processId,

          });


    }
      if (mode === 'viewSchedule') {
          this.actionRequestPending = manufacturePendingTask;
          button.setAttribute('data-target', '#viewSchedule');
          // this.scheduleVisitFormGroup.controls.entry.setValue(this.actionRequestPending.taskData.entryNumber)

          this.cancelVisitFormGroup.patchValue(
              {
                  manufacturerEntity: this.actionRequestPending.taskData.manufacturerEntity,
                  taskId: this.actionRequestPending.taskId,
                  processId: this.actionRequestPending.processId,
                  visitID: this.actionRequestPending.taskData.visitID,
                  accentTo: this.reject,
                  companyName: this.actionRequestPending.taskData.companyName


              });

          this.siteConfirmFormGroup.patchValue(
              {
                  manufacturerEntity: this.actionRequestPending.taskData.manufacturerEntity,
                  taskId: this.actionRequestPending.taskId,
                  processId: this.actionRequestPending.processId,
                  accentTo: this.approve,
                  visitID: this.actionRequestPending.taskData.visitID,
                  scheduledVisitDate: this.actionRequestPending.taskData.scheduledVisitDate,
                  companyName: this.actionRequestPending.taskData.companyName

              });

          this.siteEditFormGroup.patchValue(
              {
                  manufacturerEntity: this.actionRequestPending.taskData.manufacturerEntity,
                  taskId: this.actionRequestPending.taskId,
                  processId: this.actionRequestPending.processId,
                  accentTo: this.approve,
                  visitID: this.actionRequestPending.taskData.visitID,
                  scheduledVisitDate: this.actionRequestPending.taskData.scheduledVisitDate,
                  companyName: this.actionRequestPending.taskData.companyName

              });


      }
    if (mode === 'prepareSiteVisitReport') {
      this.actionRequestPending = manufacturePendingTask;
      button.setAttribute('data-target', '#prepareSiteVisitReport');
      this.prepareReportFormGroup.patchValue(
          {
            visitID: this.actionRequestPending.taskData.visitID,
            taskId: this.actionRequestPending.taskId,
            manufacturerEntity: this.actionRequestPending.taskData.manufacturerEntity,
              userType: this.userType,
              processId: this.actionRequestPending.processId,

          }
      );

    }
    if (mode === 'reportNotification1') {
      this.actionRequestPending = manufacturePendingTask;
      button.setAttribute('data-target', '#reportNotification1');
      this.approvalFormGroup.patchValue(
          {
            visitID: this.actionRequestPending.taskData.visitID,
            taskId: this.actionRequestPending.taskId,
            manufacturerEntity: this.actionRequestPending.taskData.manufacturerEntity,
              accentTo: this.approve,
              role:this.role,
              status:this.approveStatus,
              processId: this.actionRequestPending.processId,
          }
      );
      this.rejectFormGroup.patchValue(
          {
            visitID: this.actionRequestPending.taskData.visitID,
            taskId: this.actionRequestPending.taskId,
            manufacturerEntity: this.actionRequestPending.taskData.manufacturerEntity,
              accentTo: this.reject,
              assigneeId: this.actionRequestPending.taskData.originator,
              role:this.role,
              status:this.rejectStatus,
              processId: this.actionRequestPending.processId,
          }
      );

    }
    if (mode === 'reportNotification2') {
      this.actionRequestPending = manufacturePendingTask;
      button.setAttribute('data-target', '#reportNotification2');
      this.approvalTwoFormGroup.patchValue(
          {
            visitID: this.actionRequestPending.taskData.visitID,
            taskId: this.actionRequestPending.taskId,
            manufacturerEntity: this.actionRequestPending.taskData.manufacturerEntity,
            assigneeId: this.actionRequestPending.taskData.originator,
              accentTo: this.approve,
              role:this.role,
              status:this.approveStatus,
              processId: this.actionRequestPending.processId,
          }
      );
      this.rejectTwoFormGroup.patchValue(
          {
            visitID: this.actionRequestPending.taskData.visitID,
            taskId: this.actionRequestPending.taskId,
            manufacturerEntity: this.actionRequestPending.taskData.manufacturerEntity,
            assigneeId: this.actionRequestPending.taskData.levelOneId,
              accentTo: this.reject,
              role:this.role,
              status:this.rejectStatus,
              processId: this.actionRequestPending.processId,
          }
      );
    }
    if (mode === 'managementReport') {
      this.actionRequestPending = manufacturePendingTask;
      button.setAttribute('data-target', '#managementReport');
      this.approveTwoFormGroup.patchValue(
          {
            visitID: this.actionRequestPending.taskData.visitID,
            taskId: this.actionRequestPending.taskId,
            manufacturerEntity: this.actionRequestPending.taskData.manufacturerEntity,
            assigneeId: this.actionRequestPending.taskData.originator,
              accentTo: this.approve,
              role:this.role,
              status:this.approveStatus,
              processId: this.actionRequestPending.processId,
          }
      );
      this.rejectFormGroup.patchValue(
          {
            visitID: this.actionRequestPending.taskData.visitID,
            taskId: this.actionRequestPending.taskId,
            manufacturerEntity: this.actionRequestPending.taskData.manufacturerEntity,
            assigneeId: this.actionRequestPending.taskData.originator,
              accentTo: this.reject,
              role:this.role,
              status:this.rejectStatus,
              processId: this.actionRequestPending.processId,
          }
      );
    }
    if (mode === 'draftFeedback') {
      this.actionRequestPending = manufacturePendingTask;
      button.setAttribute('data-target', '#draftFeedback');
      this.prepareFeedBackFormGroup.patchValue(
          {
            visitID: this.actionRequestPending.taskData.visitID,
            taskId: this.actionRequestPending.taskId,
            manufacturerEntity: this.actionRequestPending.taskData.manufacturerEntity,
            assigneeId: this.actionRequestPending.taskData.contactId,
              processId: this.actionRequestPending.processId,
          }
      );

    }
    // @ts-ignore
    container.appendChild(button);
    button.click();

  }
  scheduleVisit(): void {

    if (this.scheduleVisitFormGroup.get("scheduledVisitDate").value === "") {
      this.showToasterError("Error", `Please Enter A Date.`);

    } else {
      this.loadingText = "Scheduling Visit..";
      //console.log(this.scheduleVisitFormGroup.value);
      this.SpinnerService.show();

      this.levyService.scheduleSiteVisit(this.scheduleVisitFormGroup.value).subscribe(
          (response) => {
            console.log(response);
            this.getMnPendingTask();
            this.SpinnerService.hide();
            this.showToasterSuccess(response.httpStatus, `Visit Scheduled`);
            this.scheduleVisitFormGroup.reset();
          },
          (error: HttpErrorResponse) => {
            this.SpinnerService.hide();
            this.showToasterError('Error', `Error Scheduling Visit Try Again`);
            console.log(error.message);
          }
      );
      this.hideModelCloseModalPending();

      //alert(this.scheduleVisitFormGroup.get("scheduledVisitDate").value)
    }
  }
  saveReport(): void {
      this.loadingText = "Saving Report...";
    this.SpinnerService.show();
    //console.log(this.prepareReportFormGroup.value)
    this.levyService.saveSiteVisitReport(this.prepareReportFormGroup.value).subscribe(
        (response) => {
          //console.log(response);
          this.SpinnerService.hide();
            this.showToasterSuccess(response.httpStatus, `Report Prepared`);
          this.onClickSaveUploads(response.body.savedRowID)
          //this.prepareReportFormGroup.reset();
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
            this.showToasterError("Error", `Try Again`);
        }
    );
    this.hideModelCloseModalSiteVisit()
  }
  onClickSaveUploads(reportFileID: string) {
    if (this.uploadedFiles.length > 0) {
      const file = this.uploadedFiles;
      const formData = new FormData();
      for (let i = 0; i < file.length; i++) {
        console.log(file[i]);
        formData.append('docFile', file[i], file[i].name);
      }
        this.loadingText = "Saving Report...";
      this.SpinnerService.show();
      this.levyService.uploadFileDetails(reportFileID, formData).subscribe(
          (data: any) => {
              this.getMnPendingTask();
            this.SpinnerService.hide();
            this.uploadedFiles = null;
            //console.log(data);
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
    }else{
        this.getMnPendingTask();
        this.SpinnerService.hide();
    }

  }
  onDecision(): void {
      this.loadingText = "Approving Report...";
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
          this.showToasterError('Error', `Error Approving Report Try Again`);
          console.log(error.message);
        }
    );
    this.hideModelCloseModalReportOne();
  }
    cancelVisit(): void {
        this.loadingText = "Cancelling Scheduled Site Visit...";
        this.SpinnerService.show();
        this.levyService.decisionOnSiteVisitSchedule(this.cancelVisitFormGroup.value).subscribe(
            (response ) => {
                console.log(response);
                this.getMnPendingTask();
                this.SpinnerService.hide();
                this.showToasterSuccess(response.httpStatus, `Schedule Cancelled`);
                this.cancelVisitFormGroup.reset();
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Cancelling Schedule Try Again`);
                console.log(error.message);
            }
        );
        this.hideModelCloseModalScheduling();
    }

    confirmSiteVisit(): void {
        this.loadingText = "Confirming Site Visit Schedule...";
        this.SpinnerService.show();
        this.levyService.decisionOnSiteVisitSchedule(this.siteConfirmFormGroup.value).subscribe(
            (response ) => {
                console.log(response);
                this.getMnPendingTask();
                this.SpinnerService.hide();
                this.showToasterSuccess(response.httpStatus, `Site Visit Schedule Confirmed`);
                this.siteConfirmFormGroup.reset();
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Confirming Site Visit Schedule Try Again`);
                console.log(error.message);
            }
        );
        this.hideModelCloseModalScheduling();
    }

    editSiteVisit(): void {
        this.loadingText = "Editing Site Visit Schedule...";
        this.SpinnerService.show();
        this.levyService.decisionOnSiteVisitSchedule(this.siteEditFormGroup.value).subscribe(
            (response ) => {
                console.log(response);
                this.getMnPendingTask();
                this.SpinnerService.hide();
                this.showToasterSuccess(response.httpStatus, `Site Visit Schedule Edited`);
                this.siteEditFormGroup.reset();
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Editing Site Visit Schedule Try Again`);
                console.log(error.message);
            }
        );
        this.hideModelCloseModalScheduling();
    }


  onDecisionReject(): void {
      this.loadingText = "Rejecting Report...";
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
          this.showToasterError('Error', `Error Rejecting Report Try Again`);
          console.log(error.message);
        }
    );
      this.hideModelCloseModalReportOne();
  }

    onDecisionRejectTwo(): void {
        this.loadingText = "Rejecting Report...";
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
                this.showToasterError('Error', `Error Rejecting Report Try Again`);
                console.log(error.message);
            }
        );
        this.hideModelCloseModalManagement();
    }



  viewPdfFile(pdfId: number, fileName: string, applicationType: string): void {
      this.loadingText = "Loading...";
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
      this.loadingText = "Approving Report...";
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
          this.showToasterError('Error', `Error Approving Report Try Again`);
          console.log(error.message);
        }
    );
    this.hideModelCloseModalReportTwo();
  }

  onDecisionTwo(): void {
      this.loadingText = "Approving Report...";
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
          this.showToasterError('Error', `Error Approving Report Try Again`);
          console.log(error.message);
        }
    );
    this.hideModelCloseModalManagement();
  }

  onDecisionRejectLevelTwo(): void {
      this.loadingText = "Rejecting Report...";
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
          this.showToasterError('Error', `Error Rejecting Report Try Again`);
          console.log(error.message);
        }
    );
    this.hideModelCloseModalReportTwo();
  }



  saveFeedBack(): void {
      this.loadingText = "Saving Feedback...";
    this.SpinnerService.show();
    console.log(this.prepareFeedBackFormGroup.value);
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
          this.showToasterError('Error', `Error Saving FeedBack Try Again`);
          console.log(error.message);
        }
    );
    this.hideModelCloseModalFeedback();
  }
    approveCompanyLevelOne(): void {
      console.log(this.approveCompanyLevelOneFormGroup.value)
        this.loadingText = "Editing Company Details ...."
        this.SpinnerService.show();
        this.levyService.editCompanyDetailsConfirmLevelOne(this.approveCompanyLevelOneFormGroup.value).subscribe(
            (response) => {
                console.log(response);
                this.getMnPendingTask();
                this.SpinnerService.hide();
                this.showToasterSuccess(response.httpStatus, `Company Edited`);

            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Editing Company Try Again`);
                console.log(error.message);
            }
        );
        this.hideModelCloseModalCDetails();

    }
    rejectCompanyLevelOne(): void {
        this.loadingText = "Editing Company Details ...."
        this.SpinnerService.show();
        this.levyService.editCompanyDetailsConfirmLevelOne(this.rejectCompanyLevelOneFormGroup.value).subscribe(
            (response) => {
                console.log(response);
                this.getMnPendingTask();
                this.SpinnerService.hide();
                this.showToasterSuccess(response.httpStatus, `Company Edit Request Rejected`);

            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Editing Company Try Again`);
                console.log(error.message);
            }
        );
        this.hideModelCloseModalCDetails();

    }
    approveEditRequest(): void {
        this.loadingText = "Approving Edited Company Details ...."
        this.SpinnerService.show();
        this.levyService.editCompanyDetailsConfirm(this.approveEditRequestFormGroup.value).subscribe(
            (response) => {
                console.log(response);
                this.getMnPendingTask();
                this.SpinnerService.hide();
                this.showToasterSuccess(response.httpStatus, `Company Edited`);

            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Editing Company Try Again`);
                console.log(error.message);
            }
        );
        this.hideModelCloseModalADetails();

    }
    rejectEditRequest(): void {
        this.loadingText = "Rejecting Edit Company Details Request ...."
        this.SpinnerService.show();
        this.levyService.editCompanyDetailsConfirm(this.rejectEditRequestFormGroup.value).subscribe(
            (response) => {
                console.log(response);
                this.getMnPendingTask();
                this.SpinnerService.hide();
                this.showToasterSuccess(response.httpStatus, `Request has been rejected`);

            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Editing Company Try Again`);
                console.log(error.message);
            }
        );
        this.hideModelCloseModalADetails();

    }

    approveEditRequestLevelTwo(): void {
        this.loadingText = "Approving Edited Company Details ...."
        this.SpinnerService.show();
        this.levyService.editCompanyDetailsConfirmLevelTwo(this.approveEditRequestLevelTwoFormGroup.value).subscribe(
            (response) => {
                console.log(response);
                this.getMnPendingTask();
                this.SpinnerService.hide();
                this.showToasterSuccess(response.httpStatus, `Company Edited`);

            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Editing Company Try Again`);
                console.log(error.message);
            }
        );
        this.hideModelCloseModalA1Details();

    }
    rejectEditRequestLevelTwo(): void {
        this.loadingText = "Rejecting Edit Company Details Request ...."
        this.SpinnerService.show();
        this.levyService.editCompanyDetailsConfirmLevelTwo(this.rejectEditRequestLevelTwoFormGroup.value).subscribe(
            (response) => {
                console.log(response);
                this.getMnPendingTask();
                this.SpinnerService.hide();
                this.showToasterSuccess(response.httpStatus, `Request has been rejected`);

            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Editing Company Try Again`);
                console.log(error.message);
            }
        );
        this.hideModelCloseModalA1Details();

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

  @ViewChild('closeModalPending') private closeModalPending: ElementRef | undefined;

  public hideModelCloseModalPending() {
    this.closeModalPending?.nativeElement.click();
  }

    @ViewChild('closeModalCDetails') private closeModalCDetails: ElementRef | undefined;

    public hideModelCloseModalCDetails() {
        this.closeModalCDetails?.nativeElement.click();
    }

    @ViewChild('closeModalADetails') private closeModalADetails: ElementRef | undefined;

    public hideModelCloseModalADetails() {
        this.closeModalADetails?.nativeElement.click();
    }

    @ViewChild('closeModalA1Details') private closeModalA1Details: ElementRef | undefined;

    public hideModelCloseModalA1Details() {
        this.closeModalA1Details?.nativeElement.click();
    }


  @ViewChild('closeModalSiteVisit') private closeModalSiteVisit: ElementRef | undefined;

  public hideModelCloseModalSiteVisit() {
    this.closeModalSiteVisit?.nativeElement.click();
  }

    @ViewChild('closeModalReportOne') private closeModalReportOne: ElementRef | undefined;

    public hideModelCloseModalReportOne() {
        this.closeModalReportOne?.nativeElement.click();
    }

    @ViewChild('closeModalManagement') private closeModalManagement: ElementRef | undefined;

    public hideModelCloseModalManagement() {
        this.closeModalManagement?.nativeElement.click();
    }

    @ViewChild('closeModalReportTwo') private closeModalReportTwo: ElementRef | undefined;

    public hideModelCloseModalReportTwo() {
        this.closeModalReportTwo?.nativeElement.click();
    }

    @ViewChild('closeModalFeedback') private closeModalFeedback: ElementRef | undefined;

    public hideModelCloseModalFeedback() {
        this.closeModalFeedback?.nativeElement.click();
    }

    @ViewChild('closeModalScheduling') private closeModalScheduling: ElementRef | undefined;

    public hideModelCloseModalScheduling() {
        this.closeModalScheduling?.nativeElement.click();
    }



    rerender(): void {
        this.dtElements.forEach((dtElement: DataTableDirective) => {
            if (dtElement.dtInstance)
                dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
                    dtInstance.destroy();
                });
        });
        setTimeout(() => {
            this.dtTrigger1.next();
            this.dtTrigger2.next();
        });

    }
}
