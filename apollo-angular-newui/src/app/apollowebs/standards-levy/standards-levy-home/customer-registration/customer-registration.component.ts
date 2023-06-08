import {Component, OnInit} from '@angular/core';
import {
    Branch,
    CompanyModel, DirectorsList,
    ManufactureBranchDto, ManufactureDetailList,
    ManufactureInfo,
    ManufacturingBranchDto,
    ManufacturingInfo, ManufacturingStatus, NotificationForm, NotificationStatus, SlFormStatus, SlModel
} from "../../../../core/store/data/levy/levy.model";
import {ActivatedRoute, Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {LoadingService} from "../../../../core/services/loader/loadingservice.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {LevyService} from "../../../../core/store/data/levy/levy.service";
import {HttpErrorResponse} from "@angular/common/http";
import {Store} from "@ngrx/store";
import {selectCompanyInfoDtoStateData, selectUserInfo} from "../../../../core/store";
import swal from "sweetalert2";

declare const $: any;
@Component({
  selector: 'app-customer-registration',
  templateUrl: './customer-registration.component.html',
  styleUrls: ['./customer-registration.component.css']
})
export class CustomerRegistrationComponent implements OnInit {
  roles: string[];
  directors: string;
  companySoFar: Partial<CompanyModel> | undefined;
  //company: CompanyModel;
  companyDetails !: CompanyModel[];
  directorsLists !: DirectorsList[];
  manufacturingStatus !: ManufacturingStatus;
  slFormDetails !: SlModel;
  slBranchName !: Branch;
  notificationFormStatus !: NotificationStatus;
  notificationForm !: NotificationForm;
  slNotificationFormStatus !: SlFormStatus;
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
  loadingText: string;
  stepSoFar: | undefined;
  message="check";
  step = 1;
  slStatus: number;
  iconStatus: string;
    private tm: "success" ;
    private tms: "error";
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
      this.getSlForm();
      //this.getSLNotificationStatus();

      //this.getBranchName();
      this.getCompanyDirectors();



      // this.store$.select(selectCompanyInfoDtoStateData).subscribe(
      //     (d) => {
      //         if (d) {
      //             console.log(d.countSlForm);
      //             if(d.countSlForm == 0)
      //             {
      //                 this.slStatus=0;
      //             }else{
      //                 this.getCompanySLForm();
      //                 this.slStatus=1;
      //             }
      //         }
      //     });

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
      kraPin: [],
      manufacture_status: [],
        id:[]



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
      kraPin: [],
      manufacture_status: [],
        id:[]

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
          //console.log(this.manufacturingStatus);
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
          //console.log(error.message)
          //alert(error.message);
        }
    );
  }

  public getBranchName(): void{
    this.SpinnerService.show();
    this.levyService.getBranchName().subscribe(
        (response: Branch)=> {
          this.slBranchName = response;
          console.log(this.slBranchName);
            this.manufacturerInfoForm.patchValue(this.slBranchName);
            this.manufacturingInfoForm.patchValue(this.slBranchName);
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
                this.manufacturerInfoForm.patchValue(this.companyDetails);
                this.manufacturingInfoForm.patchValue(this.companyDetails);
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
           // console.log(response);
            this.SpinnerService.hide();
            this.showToasterSuccess(response.httpStatus, response.body.responseMessage);
            swal.fire({
                title: response.body.responseMsg,
              text: response.body.responseMessage,
              buttonsStyling: false,
              customClass: {
                confirmButton: response.body.responseButton,
              },
              icon: response.body.responseStatus
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

  public getCompanyDirectors(): void{
    this.loadingText = "Retrieving Directors ...."
    this.SpinnerService.show();
    this.levyService.getCompanyDirectors().subscribe(
        (response: DirectorsList[]) => {
          this.directors= Array.prototype.map.call(response, function(item) { return item.directorName; }).join(",");
          this.directors=this.directors.split(',').join(' , ')
          this.SpinnerService.hide();

        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          console.log(error.message);

        }
    );
  }

    onClickSaveManufacturing(valid: boolean): void {
        if (valid) {
            this.SpinnerService.show();
            this.levyService.addSL1Details(this.manufacturingInfoForm.value).subscribe(
                (response) => {

                    this.SpinnerService.hide();
                    this.showToasterSuccess(response.httpStatus, response.body.responseMessage);
                    swal.fire({
                        title: response.body.responseMsg,
                        text: response.body.responseMessage,
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: response.body.responseButton,
                        },
                        icon: response.body.responseStatus
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

  // onClickSaveManufacturing(valid: boolean): void {
  //   if (valid) {
  //     this.SpinnerService.show();
  //     this.levyService.addSL1Details(this.manufacturingInfoForm.value).subscribe(
  //         (response) => {
  //           console.log(response);
  //           this.SpinnerService.hide();
  //           this.showToasterSuccess(response.httpStatus, `SL1-C Details Saved..Entry number is ${response.body.entryNumber}`);
  //           swal.fire({
  //             title:'SLI-C SAVED;',
  //             text: 'Entry number is'+ '\xa0\xa0' + response.body.entryNumber + '\xa0\xa0' +'Check your E-mail for registration details.',
  //             buttonsStyling: false,
  //             customClass: {
  //               confirmButton: 'btn btn-success form-wizard-next-btn ',
  //             },
  //             icon: 'success'
  //           });
  //           this.router.navigateByUrl('/dashboard').then(r => {});
  //         },
  //         (error: HttpErrorResponse) => {
  //           this.SpinnerService.hide();
  //           console.log(error.message);
  //         }
  //     );
  //   } else {
  //
  //     this.notifyService.showError("Please Fill In All The Fields", "Error");
  //
  //   }
  //
  //
  // }


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


    public getSlForm(): void{
        this.levyService.getSlForm().subscribe(
            (response: SlFormStatus)=> {
                this.slNotificationFormStatus = response;

                if(this.slNotificationFormStatus.countSlForm==0){
                    this.slStatus=0;
                }else if(this.slNotificationFormStatus.countSlForm==1){
                    this.getCompanySLForm();
                    this.slStatus=1;
                }
                console.log(this.slNotificationFormStatus);
                //console.log(this.slNotificationFormStatus);
            },
            (error: HttpErrorResponse)=>{
                console.log(error.message)
                //alert(error.message);
            }
        );
    }

  public getSLNotificationStatus(): void{
    this.levyService.getSLNotificationStatus().subscribe(
        (response: NotificationStatus)=> {
          this.notificationFormStatus = response;
          //console.log(this.notificationFormStatus);
        },
        (error: HttpErrorResponse)=>{
          console.log(error.message)
          //alert(error.message);
        }
    );
  }


}
