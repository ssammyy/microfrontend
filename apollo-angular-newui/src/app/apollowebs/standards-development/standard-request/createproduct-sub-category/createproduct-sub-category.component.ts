import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Department, Product, TechnicalCommittee} from "../../../../core/store/data/std/std.model";
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";
import {Store} from "@ngrx/store";
import {Router} from "@angular/router";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {NgxSpinnerService} from "ngx-spinner";
import {HttpErrorResponse} from "@angular/common/http";
import {selectUserInfo} from "../../../../core/store";
import {DataHolder, StdJustification, Stdtsectask} from "../../../../core/store/data/std/request_std.model";
import {Subject} from "rxjs";
import {DataTableDirective} from "angular-datatables";

declare const $: any;

@Component({
    selector: 'app-createproduct-sub-category',
    templateUrl: './createproduct-sub-category.component.html',
    styleUrls: ['./createproduct-sub-category.component.css']
})
export class CreateproductSubCategoryComponent implements OnInit {
    fullname = '';
    title = 'toaster-not';

    public createProductSubCategoryFormGroup!: FormGroup;
    public products !: Product[];
    public productsB !: Product[];

    public departments !: Department[];
    public technicalCommittees !: TechnicalCommittee[];
    public technicalCommitteesB !: TechnicalCommittee[];

    dtOptions: DataTables.Settings = {};
    dtTrigger: Subject<any> = new Subject<any>();
    @ViewChild(DataTableDirective, {static: false})
    dtElement: DataTableDirective;
    isDtInitialized: boolean = false
    public itemId: number;
    public tcsb !: DataHolder[];
    public tscsecRequestb !: DataHolder | undefined;

    constructor(private formBuilder: FormBuilder,
                private standardDevelopmentService: StandardDevelopmentService,
                private store$: Store<any>,
                private router: Router,
                private notifyService: NotificationService,
                private SpinnerService: NgxSpinnerService) {
    }

    ngOnInit(): void {
        this.getDepartments();

    }

    ngAfterViewInit(): void {
        this.getProductSubCategories();

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

    this.createProductSubCategoryFormGroup = this.formBuilder.group({
      departmentId: ['', Validators.required],
      technicalCommitteeId: ['', Validators.required],
      productId: ['', Validators.required],
      name: ['', Validators.required],
      description: ['', Validators.required]
    });
  }

  get formCreateSubCategory(): any {
    return this.createProductSubCategoryFormGroup.controls;
  }

  showToasterSuccess(title: string, message: string) {
    this.notifyService.showSuccess(message, title)

  }

  saveProductSubCategory(): void {
    this.SpinnerService.show();
    this.standardDevelopmentService.createProductSubCategory(this.createProductSubCategoryFormGroup.value).subscribe(
        (response) => {
          console.log(response);
            this.SpinnerService.hide();
            this.showToasterSuccess(response.httpStatus, `Product SubCategory Created`);
            this.createProductSubCategoryFormGroup.reset();
        },
        (error: HttpErrorResponse) => {
            this.SpinnerService.hide();
            console.log(error.message);
        }
    );
  }

    public getProductSubCategories(): void {
        this.standardDevelopmentService.getProductSubCategories().subscribe(
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
          console.log(response);
          this.technicalCommittees = response
        },
        (error: HttpErrorResponse) => {
          alert(error.message);
        }
    );
  }

  onSelectTechnicalCommittee(value: any): any {
      this.standardDevelopmentService.getProductsb(value).subscribe(
          (response: Product[]) => {
              console.log(response);
              this.products = response
          },
          (error: HttpErrorResponse) => {
              alert(error.message);
          }
      );
  }

    public onOpenModal(tcTask: DataHolder, mode: string): void {

        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle', 'modal');
        if (mode === 'edit') {
            this.tscsecRequestb = tcTask;
            const any = tcTask.v6
            this.standardDevelopmentService.getTechnicalCommitteeb(any).subscribe(
                (response: TechnicalCommittee[]) => {
                    this.technicalCommitteesB = response
                },
                (error: HttpErrorResponse) => {
                    alert(error.message);
                }
            );

            const technicalCommitteeId = tcTask.v5
            this.standardDevelopmentService.getProductsb(technicalCommitteeId).subscribe(
                (response: Product[]) => {
                    console.log(response);
                    this.productsB = response
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
