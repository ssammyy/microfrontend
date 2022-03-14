import {Component, OnInit} from '@angular/core';
import {
  CompanyModel,
  ManufactureBranchDto,
  ManufactureInfo,
  ManufacturingBranchDto,
  ManufacturingInfo, ManufacturingStatus, SlModel
} from "../../../../core/store/data/levy/levy.model";
import {ActivatedRoute, Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {LoadingService} from "../../../../core/services/loader/loadingservice.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {LevyService} from "../../../../core/store/data/levy/levy.service";
import {HttpErrorResponse} from "@angular/common/http";
import {Store} from "@ngrx/store";
import {selectUserInfo} from "../../../../core/store";
import swal from "sweetalert2";

declare const $: any;
@Component({
  selector: 'app-customer-registration',
  templateUrl: './customer-registration.component.html',
  styleUrls: ['./customer-registration.component.css']
})
export class CustomerRegistrationComponent implements OnInit {
  roles: string[];
  companySoFar: Partial<CompanyModel> | undefined;
  //company: CompanyModel;
  companyDetails !: CompanyModel[];
  manufacturingStatus !: ManufacturingStatus;
  slFormDetails !: SlModel;
  notificationFormStatus !: ManufacturingStatus;
  manufacturerInfoForm: FormGroup;
  manufacturingInfoForm: FormGroup;
  branchFormA: FormGroup;
  branchFormB: FormGroup;
  manufactureInformation: ManufactureInfo;
  manufacturingInformation: ManufacturingInfo;
  manufacturerBranchDetails: ManufactureBranchDto[] = [];
  manufacturerBranchDetail: ManufactureBranchDto;
  manufacturingBranchDetails: ManufacturingBranchDto[] = [];
  manufacturingBranchDetail: ManufacturingBranchDto;
  stepSoFar: | undefined;
  step = 1;
  constructor(
      private router: Router,
      private formBuilder: FormBuilder,
      private _loading: LoadingService,
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private route: ActivatedRoute,
      private levyService : LevyService,
      private store$: Store<any>,
  ) { }

  ngOnInit(): void {
      this.getCompanyProfile();
      this.getManufacturerStatus();
      this.getSLNotificationStatus();
      this.getCompanySLForm();

    this.manufacturerInfoForm = this.formBuilder.group({
      businessCompanyName: [],
      plotNumber: [],
      roadStreet: [],
      postalAddress: [],
      telephone: [],
      mainPhysicalLocation: [],
      NameAndBusinessOfProprietors: [],
      AllCommoditiesManufuctured: ['', Validators.required],
      DateOfManufacture: ['', Validators.required],
      totalValueOfManufacture: ['', Validators.required],
      location: ['', Validators.required],
      registrationNo: [],
      nameOfBranch: ['', Validators.required],
      companyProfileID: [],
      registrationNumber: [],
      kraPin: []



    });
    this.manufacturingInfoForm = this.formBuilder.group({
      businessCompanyName: [],
      plotNumber: [],
      roadStreet: [],
      postalAddress: [],
      telephone: [],
      mainPhysicalLocation: [],
      NameAndBusinessOfProprietors: [],
      AllCommoditiesManufuctured: ['', Validators.required],
      DateOfManufacture: ['', Validators.required],
      totalValueOfManufacture: ['', Validators.required],
      location: ['', Validators.required],
      registrationNo: [],
      nameOfBranch: ['', Validators.required],
      companyProfileID: [],
      registrationNumber: [],
      kraPin: []

    });

    this.branchFormA = this.formBuilder.group({
      location: [],
      plotNumber: []

    });

    this.branchFormB = this.formBuilder.group({
      nameOfBranch: [],
      addressOfBranch: []

    });

    this.store$.select(selectUserInfo).pipe().subscribe((u) => {
      this.roles = u.roles;
      return this.roles = u.roles;
    });
  }

  public getManufacturerStatus(): void{
    this.levyService.getManufacturerStatus().subscribe(
        (response: ManufacturingStatus)=> {
          this.manufacturingStatus = response;
          console.log(this.manufacturingStatus);
        },
        (error: HttpErrorResponse)=>{
          console.log(error.message)
          //alert(error.message);
        }
    );
  }
  public getCompanySLForm(): void{
    this.SpinnerService.show();
    this.levyService.getCompanySLForm().subscribe(
        (response: SlModel)=> {
          this.slFormDetails = response;
          console.log(this.slFormDetails);
          this.SpinnerService.hide();
        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
          console.log(error.message)
          //alert(error.message);
        }
    );
  }


    public getCompanyProfile(): void{
        this.SpinnerService.show();
        this.levyService.getCompanyProfile().subscribe(
            (response: CompanyModel[])=> {
                this.companyDetails = response;
                console.log(this.companyDetails);
                this.SpinnerService.hide();
            },
            (error: HttpErrorResponse)=>{
                this.SpinnerService.hide();
                console.log(error.message)
                //alert(error.message);
            }
        );
    }

  showToasterSuccess(title:string,message:string){
    this.notifyService.showSuccess(message, title)

  }

  get manufacturerInfo(): any {
    return this.manufacturerInfoForm.controls;
  }
  get manufacturingInfo(): any {
    return this.manufacturingInfoForm.controls;
  }

  get formBranchFormA(): any {
    return this.branchFormA.controls;
  }

  get formBranchFormB(): any {
    return this.branchFormB.controls;
  }


  onClickSaveSL1(valid: boolean): void {
    if (valid) {


      this.SpinnerService.show();
      this.levyService.addSL1Details(this.manufacturerInfoForm.value).subscribe(
          (response) => {
            console.log(response);
            this.SpinnerService.hide();
            this.showToasterSuccess(response.httpStatus, `SL1 Details Saved..Entry number is ${response.body.entryNumber}`);
            swal.fire({
              title:'SLI SAVED;',
              text: 'Entry number is'+ '\xa0\xa0' + response.body.entryNumber + '\xa0\xa0' +'Check your E-mail for registration details.',
              buttonsStyling: false,
              customClass: {
                confirmButton: 'btn btn-success form-wizard-next-btn ',
              },
              icon: 'success'
            });
            this.router.navigateByUrl('/dashboard').then(r => {});
          },
          (error: HttpErrorResponse) => {
            this.SpinnerService.hide();
            console.log(error.message);
          }
      );
    } else {

      this.notifyService.showError("Please Fill In All The Fields", "Error");

    }


  }
  onClickSaveManufacturing(valid: boolean): void {
    if (valid) {
      this.SpinnerService.show();
      this.levyService.addSL1Details(this.manufacturingInfoForm.value).subscribe(
          (response) => {
            console.log(response);
            this.SpinnerService.hide();
            this.showToasterSuccess(response.httpStatus, `SL1-C Details Saved..Entry number is ${response.body.entryNumber}`);
            swal.fire({
              title:'SLI-C SAVED;',
              text: 'Entry number is'+ '\xa0\xa0' + response.body.entryNumber + '\xa0\xa0' +'Check your E-mail for registration details.',
              buttonsStyling: false,
              customClass: {
                confirmButton: 'btn btn-success form-wizard-next-btn ',
              },
              icon: 'success'
            });
            this.router.navigateByUrl('/dashboard').then(r => {});
          },
          (error: HttpErrorResponse) => {
            this.SpinnerService.hide();
            console.log(error.message);
          }
      );
    } else {

      this.notifyService.showError("Please Fill In All The Fields", "Error");

    }


  }


  onClickAddBranches() {
    this.manufacturerBranchDetail = this.branchFormA.value;
    this.manufacturerBranchDetails.push(this.manufacturerBranchDetail);
    this.branchFormA?.get('location')?.reset();
    this.branchFormA?.get('plotNumber')?.reset();
  }
  onClickAddBranchDetails() {
    this.manufacturingBranchDetail = this.branchFormB.value;
    this.manufacturingBranchDetails.push(this.manufacturingBranchDetail);
    this.branchFormB?.get('nameOfBranch')?.reset();
    this.branchFormB?.get('addressOfBranch')?.reset();
  }
  removeBranches(index) {
    if (index === 0) {
      this.manufacturerBranchDetails.splice(index, 1);
    } else {
      this.manufacturerBranchDetails.splice(index, index);
    }
  }
  removeBranchDetails(index) {
    if (index === 0) {
      this.manufacturingBranchDetails.splice(index, 1);
    } else {
      this.manufacturingBranchDetails.splice(index, index);
    }
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
          '<div class="progress-bar progress-bar-{0}" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 0%;"></div>' +
          '</div>' +
          '<a href="{3}" target="{4}" data-notify="url"></a>' +
          '</div>'
    });
  }

  public getSLNotificationStatus(): void{
    this.levyService.getSLNotificationStatus().subscribe(
        (response: ManufacturingStatus)=> {
          this.notificationFormStatus = response;
          console.log(this.notificationFormStatus);
        },
        (error: HttpErrorResponse)=>{
          console.log(error.message)
          //alert(error.message);
        }
    );
  }


}
