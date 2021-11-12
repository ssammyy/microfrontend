import { Component, OnInit } from '@angular/core';
import {
  ManufactureBranchDto,
  ManufactureInfo,
  ManufacturingBranchDto,
  ManufacturingInfo
} from "../../../../core/store/data/levy/levy.model";
import {ActivatedRoute, Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {LoadingService} from "../../../../core/services/loader/loadingservice.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {LevyService} from "../../../../core/store/data/levy/levy.service";
import {HttpErrorResponse} from "@angular/common/http";
import {Store} from "@ngrx/store";
import {Company, selectCompanyData} from "../../../../core/store";

declare const $: any;
@Component({
  selector: 'app-customer-registration',
  templateUrl: './customer-registration.component.html',
  styleUrls: ['./customer-registration.component.css']
})
export class CustomerRegistrationComponent implements OnInit {
  company: Company;
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

    this.manufacturerInfoForm = this.formBuilder.group({
      businessCompanyName: ['', Validators.required],
      comKraPin: ['', Validators.required],
      lineOfBusiness: ['', Validators.required],
      certificateOfIncorporation: ['', Validators.required],
      companyEmail: ['', Validators.required],
      postalAddress: ['', Validators.required],
      telephone: ['', Validators.required],
      mainPhysicalLocation: ['', Validators.required],
      location: ['', Validators.required],
      plotNumber: ['', Validators.required],
      entryNo: ['', Validators.required]


    });
    this.manufacturingInfoForm = this.formBuilder.group({
      NameAndBusinessOfProprietors: ['', Validators.required],
      AllCommoditiesManufuctured: ['', Validators.required],
      DateOfManufacture: ['', Validators.required],
      totalValueOfManufacture: ['', Validators.required]

    });

    this.branchFormA = this.formBuilder.group({
      location: [],
      plotNumber: []

    });

    this.branchFormB = this.formBuilder.group({
      nameOfBranch: [],
      addressOfBranch: []

    });
    this.store$.select(selectCompanyData).subscribe((d) => {
      //console.log(`The id ${d.id}`);
      //console.log(this.selectCompanyData);
      return this.company = d;
    });
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
  onClickPrevious() {
    if (this.step > 1) {
      this.step = this.step - 1;
    } else {
      this.step = 1;
    }
  }

  onClickNext(valid: boolean) {
    if (valid) {
      switch (this.step) {
        case 1:
          this.stepSoFar = {...this.manufacturerInfoForm?.value};
          break;
        case 2:
          this.stepSoFar = {...this.manufacturingInfoForm?.value};
          break;

      }
      this.step += 1;
      console.log(`Clicked and step = ${this.step}`);
    }
  }

  selectStepOneClass(step: number): string {
    if (step === 1) {
      return 'active';
    } else {
      return '';
    }
  }

  selectStepTwoClass(step: number): string {
    console.log(`${step}`);
    if (step === 1) {
      return 'active';
    }
    if (step === 2) {
      return 'activated';
    } else {
      return '';
    }
  }

  onClickSaveManufacturer(): void {

      this.SpinnerService.show();
      this.levyService.addManufactureDetails(this.manufacturerInfoForm.value).subscribe(
          (response ) => {
            console.log(response);
            this.SpinnerService.hide();
            this.showToasterSuccess(response.httpStatus, `Manufacture Details Saved`);
            this.manufacturerInfoForm.reset();
          },
          (error: HttpErrorResponse) => {
            this.SpinnerService.hide();
            console.log(error.message);
          }
      );


  }

  onClickSaveManufacturing(valid: boolean) {
    if (valid) {

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


}
