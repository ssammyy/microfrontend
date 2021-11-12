import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Store} from "@ngrx/store";
import {Router} from "@angular/router";
import {selectUserInfo} from "../../../../core/store";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {NgxSpinnerService} from "ngx-spinner";
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";
import {HttpErrorResponse} from "@angular/common/http";
import {Subject} from "rxjs";
import {DataTableDirective} from "angular-datatables";
import {Department, Stdtsectask} from "../../../../core/store/data/std/request_std.model";

declare const $: any;

@Component({
  selector: 'app-create-department',
  templateUrl: './create-department.component.html',
  styleUrls: ['./create-department.component.css']
})

export class CreateDepartmentComponent implements OnInit {
  fullname = '';
  title = 'toaster-not';
  p = 1;
  p2 = 1;
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  @ViewChild(DataTableDirective, {static: false})
  dtElement: DataTableDirective;
  isDtInitialized: boolean = false
  public itemId: number;
  public departments !: Department[];

  public department !: Department | undefined;

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

  ngAfterViewInit(): void {
    this.getAllDepartments();
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
          this.getAllDepartments();
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

  public getAllDepartments(): void {
    this.standardDevelopmentService.getAllDepartments().subscribe(
        (response: Department[]) => {
          this.departments = response;

          if (this.isDtInitialized) {
            this.dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
              dtInstance.destroy();
              this.dtTrigger.next();
            });
          } else {
            this.isDtInitialized = true
            this.dtTrigger.next();
          }
        },
        (error: HttpErrorResponse) => {
          alert(error.message);
        }
    )
  }

  public onOpenModal(tcTask: Department, mode: string): void {

    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    if (mode === 'edit') {
      this.department = tcTask;
      this.itemId = this.department.id;
      button.setAttribute('data-target', '#uploadSPCJustification');
    }

    // @ts-ignore
    container.appendChild(button);
    button.click();

  }

  @ViewChild('closeModal') private closeModal: ElementRef | undefined;

  public hideModel() {
    this.closeModal?.nativeElement.click();
  }

  updateDepartment(department: Department): void {
    this.SpinnerService.show();

    this.standardDevelopmentService.updateDepartment(department).subscribe(
        (response) => {
          console.log(response);
          this.SpinnerService.hide();
          this.showToasterSuccess(response.httpStatus, `Department Details Updated`);
          this.hideModel();
          this.getAllDepartments();

        },
        (error: HttpErrorResponse) => {
          alert(error.message);
        }
    )
  }


}
