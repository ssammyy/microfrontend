import {AfterViewInit, Component, ElementRef, OnDestroy, OnInit, ViewChild, ViewEncapsulation} from '@angular/core';
import {UsersEntity} from "../../../core/store/data/std/std.model";
import {ConfirmEditCompanyDTO, ManufactureDetailList} from "../../../core/store/data/levy/levy.model";
import {FormBuilder, FormGroup} from "@angular/forms";
import {Router} from "@angular/router";
import {Store} from "@ngrx/store";
import {LevyService} from "../../../core/store/data/levy/levy.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../core/store/data/std/notification.service";
import {selectUserInfo} from "../../../core/store";
import {HttpErrorResponse} from "@angular/common/http";
import {Subject} from "rxjs";
import {DataTableDirective} from "angular-datatables";


declare const $: any;
@Component({
  selector: 'app-std-levy-applications',
  templateUrl: './std-levy-applications.component.html',
  styleUrls: ['./std-levy-applications.component.css','../../../../../node_modules/@ng-select/ng-select/themes/default.theme.css'],
  encapsulation: ViewEncapsulation.None
})
export class StdLevyApplicationsComponent implements OnInit,AfterViewInit, OnDestroy {
  userId: number ;
  roles: string[];
  userType: number ;
  levelOne= false;

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

  manufactureLists: ManufactureDetailList[] = [];
  public actionRequestList: ManufactureDetailList | undefined;
  editedCompanyData: ConfirmEditCompanyDTO;
  public assignCompanyTaskFormGroup!: FormGroup;
  public assignCompanyTask1FormGroup!: FormGroup;
  public assignCompanyTask2FormGroup!: FormGroup;

  public editCompanyFormGroup!: FormGroup;
  public editedCompanyFormGroup!: FormGroup;

  isShowAssignForm = true;
  isShowAssign1Form=true;
  isShowAssign2Form=true;
  isShowEditForm=true;
  isShowEditedForm=true;

  toggleDisplayAssignForm() {
    this.isShowAssignForm = !this.isShowAssignForm;
    this.isShowAssign1Form= true;
    this.isShowAssign2Form= true;
  }
  toggleDisplayAssignTo1Form() {
    this.isShowAssign1Form = !this.isShowAssign1Form;
    this.isShowAssignForm=true;
    this.isShowAssign2Form= true;
  }
  toggleDisplayAssignTo2Form() {
    this.isShowAssign2Form = !this.isShowAssign2Form;
    this.isShowAssignForm=true;
    this.isShowAssign1Form= true;

  }
  toggleDisplayEditForm() {
    this.isShowEditForm= !this.isShowEditForm;
    this.isShowAssignForm = true;
    this.isShowEditedForm = true;
    this.isShowAssign1Form= true;
    this.isShowAssign2Form= true;
  }

  toggleDisplayEditedForm(manufactureId: number) {
    this.SpinnerService.show();
    this.levyService.getCompanyEditedDetails(manufactureId).subscribe(
        (response: ConfirmEditCompanyDTO)=> {
          this.editedCompanyData = response;
          this.SpinnerService.hide();
          console.log(this.editedCompanyData)
        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
          console.log(error.message);
        }
    );
    this.isShowEditedForm= !this.isShowEditedForm;
    this.isShowEditForm= true;
    this.isShowAssignForm = true;
    this.isShowAssign1Form= true;
    this.isShowAssign2Form= true;
  }

  @ViewChild(DataTableDirective, {static: false})
  dtElement: DataTableDirective;
  dtOptions: DataTables.Settings[] = [];
  dtTrigger: Subject<any> = new Subject<any>();
  isDtInitialized: boolean = false
  displayTable: boolean = false;

  constructor(
      private router: Router,
      private store$: Store<any>,
      private formBuilder: FormBuilder,
      private levyService: LevyService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService
  ) {

  }

  ngOnInit(): void {
    this.getManufacturerList();
    this.getUserRoles();
    this.getUserData();

    this.dtOptions[0] = {
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

    this.editCompanyFormGroup = this.formBuilder.group({
      postalAddress: [],
      physicalAddress: [],
      companyId: [],
      ownership: []

    });

    this.editedCompanyFormGroup = this.formBuilder.group({
      postalAddress: [],
      physicalAddress: [],
      companyId: [],
      ownership: []

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

  public getManufacturerList(): void{
    this.SpinnerService.show();
    this.levyService.getManufacturerList().subscribe(
        (response: ManufactureDetailList[])=> {
          this.manufactureLists = response;
          this.SpinnerService.hide();
          this.rerender();
        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
          console.log(error.message);

        }
    );
  }

  rerender(): void {
    if (this.isDtInitialized) {
      this.dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
        dtInstance.destroy();
        this.dtTrigger.next();
      });
    } else {
      this.isDtInitialized = true
      this.dtTrigger.next();
    }
  }

  ngAfterViewInit(): void {
    this.dtTrigger.next();
  }
  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();

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

  assignCompanyTask(): void {

    this.SpinnerService.show();
    this.levyService.assignCompanyTasks(this.assignCompanyTaskFormGroup.value).subscribe(
        (response ) => {
          console.log(response);
          this.getManufacturerList();
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
  assignCompanyTaskOne(): void {
    this.SpinnerService.show();
    this.levyService.assignCompanyTasks(this.assignCompanyTask1FormGroup.value).subscribe(
        (response ) => {
          console.log(response);
          this.getManufacturerList();
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
  assignCompanyTaskTwo(): void {
    this.SpinnerService.show();
    this.levyService.assignCompanyTasks(this.assignCompanyTask2FormGroup.value).subscribe(
        (response ) => {
          console.log(response);
          this.getManufacturerList();
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
  editCompany(): void {

    this.SpinnerService.show();
    this.levyService.editCompany(this.editCompanyFormGroup.value).subscribe(
        (response ) => {
          console.log(response);
          this.getManufacturerList();
          this.SpinnerService.hide();
          this.showToasterSuccess(response.httpStatus, `Company Edited`);

        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Editing Company`);
          console.log(error.message);
        }
    );
  }

  editedCompany(): void {

    this.SpinnerService.show();
    this.levyService.editCompanyDetailsConfirm(this.editedCompanyFormGroup.value).subscribe(
        (response ) => {
          console.log(response);
          this.getManufacturerList();
          this.SpinnerService.hide();
          this.showToasterSuccess(response.httpStatus, `Company Edited`);

        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          this.showToasterError('Error', `Error Editing Company`);
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
