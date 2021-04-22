import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {faArrowLeft} from '@fortawesome/free-solid-svg-icons/faArrowLeft';
import {FormArray, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {User, UserRegister} from '../../../shared/models/user';
import {
  DivisionDetails,
  RolesEntityDto,
  SubSectionsL2EntityDto, UserRequestEntityDto,
  UserRequestTypesEntityDto
} from '../../../shared/models/master-data-details';
import {ActivatedRoute, Router} from '@angular/router';
import {AdministratorService} from '../../../shared/services/administrator.service';
import {MasterDataService} from '../../../shared/services/master-data.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {NgxSpinnerService} from 'ngx-spinner';
import {AccountService} from '../../../shared/services/account.service';
import {AlertService} from '../../../shared/services/alert.service';
import {NotificationService} from '../../../shared/services/notification.service';
import {Complaints} from '../../../shared/models/complaints';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {
  @ViewChild('editModal') editModal !: TemplateRef<any>;

  currDiv!: string;
  currDivLabel!: string;
  user?: User;
  userRequest!: UserRequestEntityDto;
  status!: number;

  arrowLeftIcon = faArrowLeft;
  public selectedUser: any;
  selectedValue = '0';
  checkBoxValue = false;
  public userRequestForm!: FormGroup;
  public assignRoleForm!: FormGroup;
  public assignCfsForm!: FormGroup;

  public rejectComplaintForm!: FormGroup;
  public adviceComplaintForm!: FormGroup;
  public assignIOComplaintForm!: FormGroup;
  public selectedOption!: string;
  public userRegNumber!: string;
  public userID!: string;
  public personsData: any[] = [];
  public persons!: FormArray;
  public userDetails!: UserRegister;
  public subSectionsL2EntityDto!: SubSectionsL2EntityDto[];


  divisionOptions: DivisionDetails[] = [];
  userRequestTypesEntity: UserRequestTypesEntityDto[] = [];
  cfsEntity: SubSectionsL2EntityDto[] = [];

  constructor(private router: Router,
              private administratorService: AdministratorService,
              private masterDataService: MasterDataService,
              private route: ActivatedRoute,
              private modalService: NgbModal,
              private spinner: NgxSpinnerService,
              public accountService: AccountService,
              private alertService: AlertService,
              private notificationService: NotificationService,
              private formBuilder: FormBuilder
) {
  this.user = this.accountService.userValue;
}

  get formAssignRole(): any {
    return this.assignRoleForm.controls;
  }

  get formAssignCfs(): any {
    return this.assignCfsForm.controls;
  }

  get formKEBSNotMandateIn(): any {
    return this.rejectComplaintForm.controls;
  }

  get formKEBSNotMandateButWithinOGA(): any {
    return this.adviceComplaintForm.controls;
  }

  get formUserRequest(): any {
    return this.userRequestForm.controls;
  }

  ngOnInit(): void {
    this.getSelectedUser();

    this.userRequestForm = this.formBuilder.group({
      requestId: ['', Validators.required],
      // userId: ['', Validators.required],
      description: ['', Validators.required]
      // approvedRemarks: ['', Validators.required],
    });
  }

  public onChangedCheckBox(value: boolean): void {
    console.log(value);
    this.checkBoxValue = value;
  }

  public getSelectedUser(): void {
      this.administratorService.loadUserDetails(this.user?.id).subscribe(
        (data: UserRegister) => {
          this.userDetails = data;
          if (this.userDetails.employeeProfile != null){
            this.status = 1;
          }else {
            this.status = 0;
          }
          console.log(data);
          // this.onSelectL1SubSubSection(this.userDetails?.employeeProfile?.l1SubSubSection);

        },
        // tslint:disable-next-line:max-line-length
        (error: { error: { message: any; }; }) => {
          this.notificationService.showError(error.error.message, 'Access Denied');
          this.spinner.hide();
        }
      );

      this.masterDataService.loadRequestTypeSystemAdmin( this.status).subscribe(
      (data: any) => {
        console.log(data);
        this.userRequestTypesEntity = data;
      },
      // tslint:disable-next-line:max-line-length
      (error: { error: { message: any; }; }) => {
        this.notificationService.showError(error.error.message, 'Access Denied');
        this.spinner.hide();
      }
    );
  }

  onSelectL1SubSubSection(l1SubSubSectionName: string): any {
    this.cfsEntity = this.subSectionsL2EntityDto.filter((item) => {
      return item.subSection === l1SubSubSectionName;
    });
  }

  openModal(divVal: string): void {
    const arrHead = ['selectUserRequest', 'rejectOGAComplaint', 'approveComplaint', 'assignOfficer'];
    const arrHeadSave = ['User Request', 'Not Within KEBS Mandate but Within OGA', 'KEBS Mandate', 'Assign Officer'];

    for (let h = 0; h < arrHead.length; h++) {
      if (divVal === arrHead[h]) {
        this.currDivLabel = arrHeadSave[h];
      }
    }

    this.currDiv = divVal;
    this.modalService.open(this.editModal);
  }

  // onUpdateReturnToList(): void {
  //   this.router.navigate(['/complaints-page']);
  // }

  onSubmitUserRequest(userId: any): any {
    // stop here if form is invalid
    if (this.userRequestForm.invalid) {
      return;
    }
    this.administratorService.sendUserRequest(this.userRequestForm.value, userId).subscribe(
      (data: any) => {
        console.log(data);
        // this.onUpdateReturnToList();
      },
      // tslint:disable-next-line:max-line-length
      (error: { error: { message: any; }; }) => {
        this.notificationService.showError(error.error.message, 'Access Denied');
        this.spinner.hide();
      }
    );
  }

  onSubmitAssignedCFS(userProfileId: any): void {
    this.administratorService.assignCfsToUser(userProfileId, this.assignRoleForm.get('roleID')?.value, 1).subscribe(
      (data: Complaints) => {
        console.log(data);
        // this.onUpdateReturnToList();
      },
      // tslint:disable-next-line:max-line-length
      (error: { error: { message: any; }; }) => {
        this.notificationService.showError(error.error.message, 'Access Denied');
        this.spinner.hide();
      }
    );
  }

  //
  // onRejectKEBSMandate(refNumber: string): void {
  //   this.marketSurveillanceService.updateComplaintKEBSNotWithInMandate(this.rejectComplaintForm.value, refNumber).subscribe(
  //     (data: Complaints) => {
  //       console.log(data);
  //       this.onUpdateReturnToList();
  //     },
  //     // tslint:disable-next-line:max-line-length
  //     (error: { error: { message: any; }; }) => {
  //       this.notificationService.showError(error.error.message, 'Access Denied');
  //       this.spinner.hide();
  //     }
  //   );
  // }
  //
  // onAdviceKEBSMandate(refNumber: string): void {
  //   this.marketSurveillanceService.updateComplaintKEBSNotWithInMandateButOGA(this.adviceComplaintForm.value, refNumber).subscribe(
  //     (data: Complaints) => {
  //       console.log(data);
  //       this.onUpdateReturnToList();
  //     },
  //     // tslint:disable-next-line:max-line-length
  //     (error: { error: { message: any; }; }) => {
  //       this.alertService.error(error.error.message, 'Access Denied');
  //       // this.notificationService.showError(error.error.message, 'Access Denied');
  //       this.spinner.hide();
  //     }
  //   );
  // }
  //
  // onAssignOfficer(refNumber: string): void {
  //   this.marketSurveillanceService.assignComplaintToIO(this.assignIOComplaintForm.value, refNumber).subscribe(
  //     (data: Complaints) => {
  //       console.log(data);
  //       this.onUpdateReturnToList();
  //     },
  //     // tslint:disable-next-line:max-line-length
  //     (error: { error: { message: any; }; }) => {
  //       this.alertService.error(error.error.message, 'Access Denied');
  //       // this.notificationService.showError(error.error.message, 'Access Denied');
  //       this.spinner.hide();
  //     }
  //   );
  // }
  //
  // public onAcceptComplaint(selectedComplaint: any): any {
  //   this.router.navigate(['/complaint-page-hof'], {queryParams: selectedComplaint});
  // }


}
