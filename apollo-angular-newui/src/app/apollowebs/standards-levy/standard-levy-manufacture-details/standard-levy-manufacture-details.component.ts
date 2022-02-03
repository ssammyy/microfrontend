import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {Subject} from "rxjs";

import {
    ManufactureCompleteTask,
    ManufactureDetailList,
    ManufacturePendingTask, UserEntityRoles, UsersEntityList
} from "../../../core/store/data/levy/levy.model";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../core/store/data/std/notification.service";
import {LevyService} from "../../../core/store/data/levy/levy.service";
import {HttpErrorResponse} from "@angular/common/http";
import {DISDTTasks, UsersEntity} from "../../../core/store/data/std/std.model";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Store} from "@ngrx/store";
import {NgSelectModule} from '@ng-select/ng-select';
import {selectUserInfo} from "../../../core/store";
import swal from "sweetalert2";
import {Router} from "@angular/router";
declare const $: any;

@Component({
  selector: 'app-standard-levy-manufacture-details',
  templateUrl: './standard-levy-manufacture-details.component.html',
  styleUrls: ['./standard-levy-manufacture-details.component.css','../../../../../node_modules/@ng-select/ng-select/themes/default.theme.css'],
    encapsulation: ViewEncapsulation.None
})
export class StandardLevyManufactureDetailsComponent implements OnInit {
    userId: number ;
    roles: string[];
    userType: number ;
    levelOne= false;
    levelTwo=false;
    levelThree=false;
    levelFour=false;
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();

  dtOptions1:DataTables.Settings = {};
    dtTrigger1: Subject<any> = new Subject<any>();

    dtOptions2:DataTables.Settings = {};
    dtTrigger2: Subject<any> = new Subject<any>();
    public users !: UsersEntity[] ;
    public usersPls !: UsersEntity[] ;
    public usersMns !: UsersEntity[] ;
    manufactureUserDetails !: UsersEntityList;
    manufactureUserRoles !: UserEntityRoles;
    selectedUser: number;
    selectedUserPl: number;
    selectedUserMn: number;


    manufactureLists: ManufactureDetailList[] = [];
    manufacturePendingTasks: ManufacturePendingTask[] = [];
    manufactureCompleteTasks: ManufactureCompleteTask[] = [];
    public actionRequestList: ManufactureDetailList | undefined;
    public actionRequestPending: ManufacturePendingTask | undefined;
    public actionRequestComplete: ManufactureCompleteTask | undefined;
    public scheduleVisitFormGroup!: FormGroup;
    public assignCompanyTaskFormGroup!: FormGroup;
    public assignCompanyTask1FormGroup!: FormGroup;
    public assignCompanyTask2FormGroup!: FormGroup;
    public prepareReportFormGroup!: FormGroup;
    public prepareFeedBackFormGroup!: FormGroup;
    public approvalFormGroup!: FormGroup;
    public rejectFormGroup!: FormGroup;
    public approvalTwoFormGroup!: FormGroup;
    public rejectTwoFormGroup!: FormGroup;
    blob: Blob;
    public uploadedFiles:  FileList;
    isShowScheduleForm = true;
    isShowAssignForm = true;
    isShowReportForm= true;
    isShowApprovalForm1 = true;
    isShowRejectForm1= true;
    isShowApprovalForm2 = true;
    isShowRejectForm2= true;
    isShowSaveFeedBackForm= true;
    isShowAssign1Form=true;
    isShowAssign2Form=true;

    toggleDisplayScheduleForm() {
        this.isShowScheduleForm = !this.isShowScheduleForm;
        this.isShowAssignForm = true;
        this.isShowReportForm= true;
        this.isShowAssign1Form= true;
        this.isShowAssign2Form= true;
    }
    toggleDisplayAssignForm() {
        this.isShowAssignForm = !this.isShowAssignForm;
        this.isShowScheduleForm= true;
        this.isShowReportForm= true;
        this.isShowAssign1Form= true;
        this.isShowAssign2Form= true;
    }
    toggleDisplayAssignTo1Form() {
        this.isShowAssign1Form = !this.isShowAssign1Form;
        this.isShowScheduleForm= true;
        this.isShowReportForm= true;
        this.isShowAssignForm=true;
        this.isShowAssign2Form= true;
    }
    toggleDisplayAssignTo2Form() {
        this.isShowAssign2Form = !this.isShowAssign2Form;
        this.isShowScheduleForm= true;
        this.isShowReportForm= true;
        this.isShowAssignForm=true;
        this.isShowAssign1Form= true;

    }
    toggleDisplayReportForm() {
        this.isShowReportForm = !this.isShowReportForm;
        this.isShowScheduleForm= true;
        this.isShowAssignForm = true;
        this.isShowAssign1Form= true;
        this.isShowAssign2Form= true;
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
    //this.getMnCompleteTask();
    this.getManufacturerList();
    this.getUserRoles();
    this.getUserList();
    this.getUserData();
    this.getSlLvTwoList();
    this.getPlUserList();
    //this.checkIfTrue();
    //  this.getUserDetails();


      this.scheduleVisitFormGroup = this.formBuilder.group({
          scheduledVisitDate: ['', Validators.required],
          manufacturerEntity: [],
          taskId: [],

      });
      this.assignCompanyTaskFormGroup = this.formBuilder.group({
          manufacturerEntity: [],
          assignedTo: [],
          companyName: [],
          kraPin: [],
          status: [],
          registrationNumber: [],
          postalAddress: [],
          physicalAddress: [],
          plotNumber: [],
          companyEmail: [],
          companyTelephone: [],
          yearlyTurnover: [],
          businessLines: [],
          businessNatures: [],
          buildingName: [],
          branchName: [],
          streetName: [],
          directorIdNumber: [],
          region: [],
          county: [],
          town: [],
          manufactureStatus: [],
          entryNumber: [],
          contactId: []

      });
      this.assignCompanyTask1FormGroup = this.formBuilder.group({
          manufacturerEntity: [],
          assignedTo: [],
          companyName: [],
          kraPin: [],
          status: [],
          registrationNumber: [],
          postalAddress: [],
          physicalAddress: [],
          plotNumber: [],
          companyEmail: [],
          companyTelephone: [],
          yearlyTurnover: [],
          businessLines: [],
          businessNatures: [],
          buildingName: [],
          branchName: [],
          streetName: [],
          directorIdNumber: [],
          region: [],
          county: [],
          town: [],
          manufactureStatus: [],
          entryNumber: [],
          contactId: []

      });
      this.assignCompanyTask2FormGroup = this.formBuilder.group({
          manufacturerEntity: [],
          assignedTo: [],
          companyName: [],
          kraPin: [],
          status: [],
          registrationNumber: [],
          postalAddress: [],
          physicalAddress: [],
          plotNumber: [],
          companyEmail: [],
          companyTelephone: [],
          yearlyTurnover: [],
          businessLines: [],
          businessNatures: [],
          buildingName: [],
          branchName: [],
          streetName: [],
          directorIdNumber: [],
          region: [],
          county: [],
          town: [],
          manufactureStatus: [],
          entryNumber: [],
          contactId: []

      });
      this.prepareReportFormGroup = this.formBuilder.group({
          visitDate: ['', Validators.required],
          purpose: ['', Validators.required],
          personMet: ['', Validators.required],
          actionTaken: ['', Validators.required],
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

    get assignCompanyTaskForm(): any {
        return this.assignCompanyTaskFormGroup.controls;
    }
    get assignCompanyTask1Form(): any {
        return this.assignCompanyTask1FormGroup.controls;
    }
    get assignCompanyTask2Form(): any {
        return this.assignCompanyTask2FormGroup.controls;
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

    public getUserList(): void {
        this.SpinnerService.show();
        this.levyService.getUserList().subscribe(
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

    public getPlUserList(): void {
        this.SpinnerService.show();
        this.levyService.getPlList().subscribe(
            (response: UsersEntity[]) => {
                this.SpinnerService.hide();
                this.usersPls = response;
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                alert(error.message);
            }
        );
    }

    public getSlLvTwoList(): void {
        this.SpinnerService.show();
        this.levyService.getSlLvTwoList().subscribe(
            (response: UsersEntity[]) => {
                this.SpinnerService.hide();
                this.usersMns = response;
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                alert(error.message);
            }
        );
    }

  public getManufacturerList(): void{
    this.SpinnerService.show();
    this.levyService.getManufacturerList().subscribe(
        (response: ManufactureDetailList[])=> {
          this.dtTrigger.next();
          this.manufactureLists = response;
          //console.log(response);
            this.SpinnerService.hide();
        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
          console.log(error.message);

        }
    );
  }
    public getUserDetails(): void{
        this.levyService.getUserDetails().subscribe(
        (response: UsersEntityList)=> {

            this.manufactureUserDetails = response;
            console.log(this.manufactureUserDetails);
        },
            (error: HttpErrorResponse)=>{
                console.log(error.message);
            }
        );

    }

    // public getUserRoles(): void{
    //     this.levyService.getUserRoles().subscribe(
    //         (response: UserEntityRoles)=> {
    //
    //             this.manufactureUserRoles = response;
    //             console.log(response)
    //             var found = Object.keys(response).filter(function(key) {
    //                 return response[key] === '793';
    //             });
    //
    //             if (found.length) {
    //                 alert('exists');
    //             }
    //             },
    //         (error: HttpErrorResponse)=>{
    //             console.log(error.message);
    //         }
    //     );
    //
    // }

  public getMnPendingTask(): void{
    this.SpinnerService.show();
    this.levyService.getMnPendingTask().subscribe(
        (response: ManufacturePendingTask[])=> {
          this.dtTrigger1.next();
          //console.log(this.manufacturePendingTasks);
          this.manufacturePendingTasks = response;
            this.SpinnerService.hide();
        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
            console.log(error.message);
        }
    );
  }
  public getMnCompleteTask(): void{
    this.SpinnerService.show();
    this.levyService.getMnCompleteTask().subscribe(
        (response: ManufactureCompleteTask[])=> {
          this.dtTrigger2.next();
          this.manufactureCompleteTasks = response;
            this.SpinnerService.hide();
        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
            console.log(error.message);
        }
    );
  }
    public onOpenModalList(manufactureLists: ManufactureDetailList,mode:string): void{
        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle','modal');
        if (mode==='viewList'){
            this.actionRequestList=manufactureLists;
            button.setAttribute('data-target','#viewList');
        }
        if (mode==='openSchedule'){
            this.actionRequestList=manufactureLists;
            button.setAttribute('data-target','#openSchedule');
        }
        // @ts-ignore
        container.appendChild(button);
        button.click();

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
        if (mode==='draftFeedback'){
            this.actionRequestPending=manufacturePendingTask;
            button.setAttribute('data-target','#draftFeedback');
        }
        // @ts-ignore
        container.appendChild(button);
        button.click();

    }

    public onOpenModalComplete(manufactureCompleteTask: ManufactureCompleteTask,mode:string): void{
        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle','modal');
        if (mode==='viewComplete'){
            this.actionRequestComplete=manufactureCompleteTask;
            button.setAttribute('data-target','#viewComplete');
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


    assignCompanyTask(): void {

        this.SpinnerService.show();
        this.levyService.assignCompanyTasks(this.assignCompanyTaskFormGroup.value).subscribe(
            (response ) => {
                console.log(response);
                //this.getManufacturerList();
                this.SpinnerService.hide();
                this.showToasterSuccess(response.httpStatus, `Task Assigned`);
                this.router.navigateByUrl('/slManufacturers').then(r => {});

            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Assigning Task`);
                console.log(error.message);
            }
        );
    }
    assignCompanyTaskOne(): void {
        this.SpinnerService.show();
        this.levyService.assignCompanyTasks(this.assignCompanyTask1FormGroup.value).subscribe(
            (response ) => {
                console.log(response);
                //this.getManufacturerList();
                this.SpinnerService.hide();
                this.showToasterSuccess(response.httpStatus, `Task Assigned`);
                this.router.navigateByUrl('/slManufacturers').then(r => {});
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Assigning Task`);
                console.log(error.message);
            }
        );
    }
    assignCompanyTaskTwo(): void {
        this.SpinnerService.show();
        this.levyService.assignCompanyTasks(this.assignCompanyTask2FormGroup.value).subscribe(
            (response ) => {
                console.log(response);
                //this.getManufacturerList();
                this.SpinnerService.hide();
                this.showToasterSuccess(response.httpStatus, `Task Assigned`);
                this.router.navigateByUrl('/slManufacturers').then(r => {});
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Error Assigning Task`);
                console.log(error.message);
            }
        );
    }
    saveReport(): void {
        this.SpinnerService.show();
        this.levyService.saveSiteVisitReport(this.prepareReportFormGroup.value).subscribe(
            (response ) => {
                console.log(response);
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
            );
        }

    }
    onDecision(): void {

        this.SpinnerService.show();
        this.levyService.levelOneDecisionOnReport(this.approvalFormGroup.value).subscribe(
            (response ) => {
                console.log(response);

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

    onDecisionRejectLevelTwo(): void {

        this.SpinnerService.show();
        this.levyService.decisionOnSiteReportLevelTwo(this.rejectTwoFormGroup.value).subscribe(
            (response ) => {
                console.log(response);

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
    // public checkIfTrue(){
    //     // @ts-ignore
    //     if (Object.values(this.manufactureUserRoles).indexOf(793) > -1) {
    //         console.log('has test1');
    //     }
    //     if (Object.values(obj).indexOf('test1') > -1) {
    //         console.log('has test1');
    //     }
    // }


    // public checkIfTrue()
    // {
    //
    //     if(this.manufactureUserRoles.includes('793') ){
    //      this.levelOne=true;
    //    }
    //     if(this.manufactureUserRoles.includes(794) ){
    //     this.levelTwo=true;
    // }
    //     if(this.manufactureUserRoles.includes(795) ){
    //     this.levelThree=true;
    // }
    //     if(this.manufactureUserRoles.includes(796) ){
    //     this.levelFour=true;
    // }
    // }

}
