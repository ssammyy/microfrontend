import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Store} from "@ngrx/store";
import {Router} from "@angular/router";
import {selectUserInfo} from "../../../../core/store";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {NgxSpinnerService} from "ngx-spinner";
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";
import {HttpErrorResponse} from "@angular/common/http";

declare const $: any;

@Component({
  selector: 'app-create-department',
  templateUrl: './create-department.component.html',
  styleUrls: ['./create-department.component.css']
})

export class CreateDepartmentComponent implements OnInit {
  fullname = '';
  title = 'toaster-not';

  public createDepartmentFormGroup!: FormGroup;

  constructor(
      private store$: Store<any>,
      private router: Router,
      private formBuilder: FormBuilder,
      private notifyService: NotificationService,
      private SpinnerService: NgxSpinnerService,
      private standardDevelopmentService: StandardDevelopmentService,
  ) {

  }

  ngOnInit(): void {
    this.store$.select(selectUserInfo).pipe().subscribe((u) => {
      return this.fullname = u.fullName;
    });

    this.createDepartmentFormGroup = this.formBuilder.group({
      name: ['', Validators.required],
      abbreviations: ['', Validators.required],
      codes: ['', Validators.required]
    });

  }

  get formCreateDepartment(): any {
    return this.createDepartmentFormGroup.controls;
  }

  showToasterSuccess(title: string, message: string) {
    this.notifyService.showSuccess(message, title)

  }

  saveDepartment(): void {
    this.SpinnerService.show();
    this.standardDevelopmentService.createDepartment(this.createDepartmentFormGroup.value).subscribe(
        (response) => {
          console.log(response);
          this.SpinnerService.hide();
          this.showToasterSuccess(response.httpStatus, `Department Created`);
          this.createDepartmentFormGroup.reset();
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

}
