import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {NotificationService} from "../../../core/store/data/std/notification.service";

declare const $: any;

@Component({
  selector: 'app-com-std-levy-form',
  templateUrl: './com-std-levy-form.component.html',
  styleUrls: ['./com-std-levy-form.component.css']
})
export class ComStdLevyFormComponent implements OnInit {
  public notificationForm!: FormGroup;
  constructor(
      private notifyService : NotificationService,
      private formBuilder: FormBuilder,
  ) { }

  ngOnInit(): void {
    this.notificationForm = this.formBuilder.group({
      nameOfBusiness: ['', Validators.required],
      plotNumber: ['', Validators.required],
      roadOrStreet: ['', Validators.required],
      address: ['', Validators.required],
      telephoneNumber: ['', Validators.required],
      adminLocation: ['', Validators.required],
      NameAndBusinessOfProprietors: ['', Validators.required],
      AllCommoditiesManufuctured: ['', Validators.required],
      DateOfManufacture: ['', Validators.required],
      totalValueOfManufacture: ['', Validators.required],
      nameOfBranch: ['', Validators.required],
      addressOfBranch: ['', Validators.required]
    });
  }

  get formUploadNotification(): any {
    return this.notificationForm.controls;
  }

  uploadNotification(): void {

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
