import { Component, OnInit } from '@angular/core';
import {Subject} from "rxjs";

import {
    ApproveVisitTask,
    CompanyModel,
    ManufactureCompleteTask,
    ManufactureDetailList,
    ManufacturePendingTask, ReportDecisionLevelOne, ReportDecisionLevelTwo
} from "../../../core/store/data/levy/levy.model";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../core/store/data/std/notification.service";
import {LevyService} from "../../../core/store/data/levy/levy.service";
import {HttpErrorResponse} from "@angular/common/http";
import {DISDTTasks, UsersEntity} from "../../../core/store/data/std/std.model";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Store} from "@ngrx/store";
import {selectUserInfo} from "../../../core/store";
import swal from "sweetalert2";
declare const $: any;

@Component({
  selector: 'app-standard-levy-manufacture-details',
  templateUrl: './standard-levy-manufacture-details.component.html',
  styleUrls: ['./standard-levy-manufacture-details.component.css']
})
export class StandardLevyManufactureDetailsComponent implements OnInit {
    userId: number ;
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();

  dtOptions1:DataTables.Settings = {};
    dtTrigger1: Subject<any> = new Subject<any>();

    dtOptions2:DataTables.Settings = {};
    dtTrigger2: Subject<any> = new Subject<any>();
    public users !: UsersEntity[] ;
    selectedUser: number;


    manufactureLists: ManufactureDetailList[] = [];
    manufacturePendingTasks: ManufacturePendingTask[] = [];
    manufactureCompleteTasks: ManufactureCompleteTask[] = [];
    public actionRequestList: ManufactureDetailList | undefined;
    public actionRequestPending: ManufacturePendingTask | undefined;
    public actionRequestComplete: ManufactureCompleteTask | undefined;
    public scheduleVisitFormGroup!: FormGroup;
    public assignCompanyTaskFormGroup!: FormGroup;
    public prepareReportFormGroup!: FormGroup;
    public prepareFeedBackFormGroup!: FormGroup;
    blob: Blob;
    public uploadedFiles:  FileList;
    isShowScheduleForm = true;
    isShowAssignForm = true;
    isShowReportForm= true;
    isShowApprovalForm1 = true;
    isShowRejectForm1= true;
    isShowApprovalForm2 = true;
    isShowRejectForm2= true;

    toggleDisplayScheduleForm() {
        this.isShowScheduleForm = !this.isShowScheduleForm;
        this.isShowAssignForm = true;
        this.isShowReportForm= true;
    }
    toggleDisplayAssignForm() {
        this.isShowAssignForm = !this.isShowAssignForm;
        this.isShowScheduleForm= true;
        this.isShowReportForm= true;
    }
    toggleDisplayReportForm() {
        this.isShowReportForm = !this.isShowReportForm;
        this.isShowScheduleForm= true;
        this.isShowAssignForm = true;
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
  constructor(
      private store$: Store<any>,
      private formBuilder: FormBuilder,
      private levyService: LevyService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService
  ) { }

  ngOnInit(): void {
    this.getMnCompleteTask();
    this.getManufacturerList();
    this.getMnPendingTask();
      this.getUserList();
      this.store$.select(selectUserInfo).pipe().subscribe((u) => {
          return this.userId = u.id;
      });

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
          entryNumber: []

      });
      this.prepareReportFormGroup = this.formBuilder.group({
          visitDate: ['', Validators.required],
          purpose: ['', Validators.required],
          personMet: ['', Validators.required],
          actionTaken: ['', Validators.required],
          taskId: [],
          visitID: [],
          assigneeId: []

      });
      this.prepareFeedBackFormGroup = this.formBuilder.group({
          visitDate: ['', Validators.required],
          purpose: ['', Validators.required],
          personMet: ['', Validators.required],
          actionTaken: ['', Validators.required],
          taskId: [],
          visitID: [],
          contactId: []

      });
  }

    get scheduleVisitForm(): any {
        return this.scheduleVisitFormGroup.controls;
    }

    get assignCompanyTaskForm(): any {
        return this.assignCompanyTaskFormGroup.controls;
    }
    get formPrepareReport(): any {
        return this.prepareReportFormGroup.controls;
    }
    get formPrepareFeedBack(): any {
        return this.prepareFeedBackFormGroup.controls;
    }
    showToasterSuccess(title:string,message:string){
        this.notifyService.showSuccess(message, title)

    }
    showToasterError(title:string,message:string){
        this.notifyService.showError(message, title)

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

  public getManufacturerList(): void{
    this.SpinnerService.show();
    this.levyService.getManufacturerList().subscribe(
        (response: ManufactureDetailList[])=> {
          this.SpinnerService.hide();
          this.dtTrigger.next();
          this.manufactureLists = response;
        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
          console.log(error.message);

        }
    );
  }
  public getMnPendingTask(): void{
    this.SpinnerService.show();
    this.levyService.getMnPendingTask().subscribe(
        (response: ManufacturePendingTask[])=> {
          this.SpinnerService.hide();
          this.dtTrigger1.next();
          console.log(this.manufacturePendingTasks);
          this.manufacturePendingTasks = response;
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
          this.SpinnerService.hide();
          this.dtTrigger2.next();
          this.manufactureCompleteTasks = response;
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

                this.levyService.getMnPendingTask().subscribe(
                    (response: ManufacturePendingTask[])=> {
                        this.SpinnerService.hide();
                        this.dtTrigger1.next();
                        this.manufacturePendingTasks = response;
                    },
                    (error: HttpErrorResponse)=>{
                        this.SpinnerService.hide();
                        console.log(error.message);
                    }
                );
                this.SpinnerService.hide();
                this.showToasterSuccess(response.httpStatus, `Task Assigned`);

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
    public onDecision(reportDecisionLevelOne: ReportDecisionLevelOne): void{
        this.SpinnerService.show();
        this.levyService.levelOneDecisionOnReport(reportDecisionLevelOne).subscribe(
            (response: ApproveVisitTask) => {
                this.SpinnerService.hide();
                this.showToasterSuccess('Success', `Report Approved`);
                console.log(response);
                this.getMnPendingTask();
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Report Was Not Approved; Try Again`);
                console.log(error.message);
                this.getMnPendingTask();
                //alert(error.message);
            }
        );
    }
    public onDecisionReject(reportDecisionLevelOne: ReportDecisionLevelOne): void{
        this.SpinnerService.show();
        this.levyService.levelOneDecisionOnReport(reportDecisionLevelOne).subscribe(
            (response: ApproveVisitTask) => {
                this.SpinnerService.hide();
                this.showToasterSuccess('Success', `Report Rejected`);
                console.log(response);
                this.getMnPendingTask();
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Report Was Not Approved; Try Again`);
                console.log(error.message);
                this.getMnPendingTask();
                //alert(error.message);
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
    public onDecisionLevelTwo(reportDecisionLevelTwo: ReportDecisionLevelTwo): void{
        this.SpinnerService.show();
        this.levyService.decisionOnSiteReportLevelTwo(reportDecisionLevelTwo).subscribe(
            (response: ApproveVisitTask) => {
                this.SpinnerService.hide();
                this.showToasterSuccess('Success', `Report Approved`);
                console.log(response);
                this.getMnPendingTask();
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Report Was Not Approved; Try Again`);
                console.log(error.message);
                this.getMnPendingTask();
                //alert(error.message);
            }
        );
    }
    public onDecisionRejectLevelTwo(reportDecisionLevelTwo: ReportDecisionLevelTwo): void{
        this.SpinnerService.show();
        this.levyService.decisionOnSiteReportLevelTwo(reportDecisionLevelTwo).subscribe(
            (response: ApproveVisitTask) => {
                this.SpinnerService.hide();
                this.showToasterSuccess('Success', `Report Rejected`);
                console.log(response);
                this.getMnPendingTask();
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                this.showToasterError('Error', `Report Was Not Approved; Try Again`);
                console.log(error.message);
                this.getMnPendingTask();
                //alert(error.message);
            }
        );
    }
    saveFeedBack(): void {
        this.SpinnerService.show();
        this.levyService.saveSiteVisitFeedback(this.prepareFeedBackFormGroup.value).subscribe(
            (response ) => {
                console.log(response);
                this.SpinnerService.hide();
                this.prepareFeedBackFormGroup.reset();
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
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

}
