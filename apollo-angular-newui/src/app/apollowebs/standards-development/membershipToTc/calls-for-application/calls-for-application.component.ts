import {Component, OnInit, ViewChild} from '@angular/core';
import {Subject} from "rxjs";
import {DataTableDirective} from "angular-datatables";
import {CallForApplication} from "../../../../core/store/data/std/request_std.model";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Store} from "@ngrx/store";
import {Router} from "@angular/router";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {NgxSpinnerService} from "ngx-spinner";
import {MembershipToTcService} from "../../../../core/store/data/std/membership-to-tc.service";
import {Department, TechnicalCommittee} from "../../../../core/store/data/std/std.model";
import {HttpErrorResponse} from "@angular/common/http";
import {selectUserInfo} from "../../../../core/store";
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";

declare const $: any;

@Component({
  selector: 'app-calls-for-application',
  templateUrl: './calls-for-application.component.html',
  styleUrls: ['./calls-for-application.component.css']
})
export class CallsForApplicationComponent implements OnInit {
  fullname = '';
  p = 1;
  p2 = 1;
  title = 'toaster-not';
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  @ViewChild(DataTableDirective, {static: false})
  dtElement: DataTableDirective;
  isDtInitialized: boolean = false
  public itemId: number;
  public departments !: Department[];
  public createApplicationFormGroup!: FormGroup;
  public technicalCommittees !: TechnicalCommittee[];


  constructor(private formBuilder: FormBuilder,
              private standardDevelopmentService: StandardDevelopmentService,
              private membershipToTcService: MembershipToTcService,
              private store$: Store<any>,
              private router: Router,
              private notifyService: NotificationService,
              private SpinnerService: NgxSpinnerService) {
  }

  ngOnInit(): void {
    this.getDepartments();

  }

  public getDepartments(): void {
    this.standardDevelopmentService.getDepartmentsb().subscribe(
        (response: Department[]) => {
          this.departments = response;
        },
        (error: HttpErrorResponse) => {
          alert(error.message);
        }
    );
    this.store$.select(selectUserInfo).pipe().subscribe((u) => {
      return this.fullname = u.fullName;
    });

    this.createApplicationFormGroup = this.formBuilder.group({
      departmentId: ['', Validators.required],
      tcId: ['', Validators.required],
      title: ['', Validators.required],
      dateOfPublishing: ['', Validators.required],
      description: ['', Validators.required]

    });
  }

  get formCreateProduct(): any {
    return this.createApplicationFormGroup.controls;
  }

  saveProductCategory(): void {
    this.SpinnerService.show();

    this.membershipToTcService.uploadCallForApplications(this.createApplicationFormGroup.value).subscribe(
        (response: CallForApplication) => {
          this.SpinnerService.hide();

          console.log(response);
          this.showToasterSuccess("Success", "Successfully Created Call For Application")
          this.createApplicationFormGroup.reset()
        },
        (error: HttpErrorResponse) => {
          this.SpinnerService.hide();
          alert(error.message);
        }
    )
  }

  showToasterSuccess(title: string, message: string) {
    this.notifyService.showSuccess(message, title)

  }

  showNotification(from: any, align: any) {
    const type = ['', 'info', 'success', 'warning', 'danger', 'rose', 'primary'];

    const color = Math.floor((Math.random() * 6) + 1);

    $.notify({
      icon: 'notifications',
      message: 'ERROR.'
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

  onSelectDepartment(value: any): any {
    this.standardDevelopmentService.getTechnicalCommitteeb(value).subscribe(
        (response: TechnicalCommittee[]) => {
          this.technicalCommittees = response
        },
        (error: HttpErrorResponse) => {
          alert(error.message);
        }
    );
  }

}
