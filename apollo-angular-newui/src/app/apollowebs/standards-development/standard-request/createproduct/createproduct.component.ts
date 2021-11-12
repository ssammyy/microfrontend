import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Department, TechnicalCommittee} from "../../../../core/store/data/std/std.model";
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";
import {Store} from "@ngrx/store";
import {Router} from "@angular/router";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {NgxSpinnerService} from "ngx-spinner";
import {HttpErrorResponse} from "@angular/common/http";
import {selectUserInfo} from "../../../../core/store";
import {Subject} from "rxjs";
import {DataTableDirective} from "angular-datatables";
import {DataHolder, StdJustification, Stdtsectask} from "../../../../core/store/data/std/request_std.model";

declare const $: any;

@Component({
    selector: 'app-createproduct',
    templateUrl: './createproduct.component.html',
    styleUrls: ['./createproduct.component.css']
})
export class CreateproductComponent implements OnInit {
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

    public createProductCategoryFormGroup!: FormGroup;

    public departments !: Department[];
    public technicalCommittees !: TechnicalCommittee[];
    public technicalCommitteesb !: TechnicalCommittee[];
    public tcsb !: DataHolder[];
    public tscsecRequestb !: DataHolder | undefined;


    constructor(
        private formBuilder: FormBuilder,
        private standardDevelopmentService: StandardDevelopmentService,
        private store$: Store<any>,
        private router: Router,
        private notifyService: NotificationService,
        private SpinnerService: NgxSpinnerService,
    ) {
    }

    ngOnInit(): void {
        this.getDepartments();
    }

    ngAfterViewInit(): void {
        this.getProductCategories();

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

        this.createProductCategoryFormGroup = this.formBuilder.group({
            departmentId: ['', Validators.required],
            technicalCommitteeId: ['', Validators.required],

            name: ['', Validators.required],
            description: ['', Validators.required]
        });
    }

  get formCreateProduct(): any {
    return this.createProductCategoryFormGroup.controls;
  }

  showToasterSuccess(title: string, message: string) {
    this.notifyService.showSuccess(message, title)

  }

  saveProductCategory(): void {
    this.SpinnerService.show();
    this.standardDevelopmentService.createProductCategory(this.createProductCategoryFormGroup.value).subscribe(
        (response) => {
          this.SpinnerService.hide();
          this.showToasterSuccess(response.httpStatus, `Product Category Created`);
            this.createProductCategoryFormGroup.reset();
            this.getProductCategories();
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

    public getProductCategories(): void {
        this.standardDevelopmentService.getProductCategories().subscribe(
            (response: DataHolder[]) => {
                this.tcsb = response;

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

    public onOpenModal(tcTask: DataHolder, mode: string): void {

        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle', 'modal');
        if (mode === 'edit') {
            this.tscsecRequestb = tcTask;
            const any = tcTask.v4
            this.standardDevelopmentService.getTechnicalCommitteeb(any).subscribe(
                (response: TechnicalCommittee[]) => {
                    this.technicalCommitteesb = response
                },
                (error: HttpErrorResponse) => {
                    alert(error.message);
                }
            );
            this.itemId = this.tscsecRequestb.id;
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

    uploadJustification(stdJustification: StdJustification): void {

        console.log(stdJustification);

        this.standardDevelopmentService.uploadJustification(stdJustification).subscribe(
            (response: Stdtsectask) => {
                console.log(response);
                // this.getTCSECTasksJustification();
                this.hideModel();
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        )
    }

}
