import { Component, OnInit } from '@angular/core';
import {
  CompanyStandardRequest,
  Department,
  Product,
  ProductSubCategory,
  TechnicalCommittee
} from "../../../../../core/store/data/std/std.model";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {StdComStandardService} from "../../../../../core/store/data/std/std-com-standard.service";
import Swal from "sweetalert2";
import {HttpErrorResponse} from "@angular/common/http";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../../core/store/data/std/notification.service";
import {StandardDevelopmentService} from "../../../../../core/store/data/std/standard-development.service";
import swal from "sweetalert2";

declare const $: any;

@Component({
  selector: 'app-cs-request-form',
  templateUrl: './cs-request-form.component.html',
  styleUrls: ['./cs-request-form.component.css']
})
export class CsRequestFormComponent implements OnInit {
  public products !: Product[] ;
  public productsSubCategory !: ProductSubCategory[] ;
  public departments !: Department[] ;
  public technicalCommittees !: TechnicalCommittee[];
  public stdRequestFormGroup!: FormGroup;
    blob: Blob;
    public uploadedFiles:  FileList;
  constructor(
      private formBuilder: FormBuilder,
      private route: ActivatedRoute,
      private router: Router,
      private stdComStandardService: StdComStandardService,
      private standardDevelopmentService: StandardDevelopmentService,
      private SpinnerService: NgxSpinnerService,
      private notifyService: NotificationService
  ) { }

  ngOnInit(): void {
      this.getDepartments();

    this.stdRequestFormGroup = this.formBuilder.group({
    companyName:['', Validators.required],
    companyPhone:['', Validators.required],
    departmentId:['', Validators.required],
    // tcId: ['', Validators.required],
    // productId: ['', Validators.required],
    // productSubCategoryId:['', Validators.required],
    companyEmail:['', Validators.required],
    subject: ['',Validators.required],
    description: ['',Validators.required],
    contactOneFullName: ['',Validators.required],
    contactOneTelephone: ['',Validators.required],
    contactOneEmail: ['',Validators.required]
    // contactTwoFullName: [],
    // contactTwoTelephone: [],
    // contactTwoEmail: [],
    // contactThreeFullName: [],
    // contactThreeTelephone: [],
    // contactThreeEmail: []
    });
  }
  get formStdRequest(): any{
    return this.stdRequestFormGroup.controls;
  }

    public getStandards(): void {
        this.stdComStandardService.getStandards().subscribe(
            (response: Product[]) => {
                this.products = response;
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );
    }




    saveStandard(): void {
    this.SpinnerService.show();
    this.stdComStandardService.addStandardRequest(this.stdRequestFormGroup.value).subscribe(
        (response) =>{

          this.SpinnerService.hide();
          console.log(response);
            this.onClickSaveUploads(response.body.id)

        },
        (error:HttpErrorResponse) =>{
          this.SpinnerService.hide();
          alert(error.message);
        }
    );
  }

    onClickSaveUploads(comStdRequestID: string) {
        if (this.uploadedFiles.length > 0) {
            const file = this.uploadedFiles;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }
            this.SpinnerService.show();
            this.stdComStandardService.uploadCommitmentLetter(comStdRequestID, formData).subscribe(
                (data: any) => {
                    this.SpinnerService.hide();
                    this.uploadedFiles = null;
                    console.log(data);
                    swal.fire({
                        title: 'Thank you....',
                        html:'Company Standard Request Submitted',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success form-wizard-next-btn ',
                        },
                        icon: 'success'
                    }).then(r => this.stdRequestFormGroup.reset());
                },
            );
        }

    }

    viewCommitmentLetter(pdfId: number, fileName: string, applicationType: string): void {
        this.SpinnerService.show();
        this.stdComStandardService.viewCommitmentLetter(pdfId).subscribe(
            (dataPdf: any) => {
                this.SpinnerService.hide();
                this.blob = new Blob([dataPdf], {type: applicationType});
                let downloadURL = window.URL.createObjectURL(this.blob);
                const link = document.createElement('a');
                link.href = downloadURL;
                link.download = fileName;
                link.click();
            },
        );
    }

    public getDepartments(): void {
        this.SpinnerService.show();
        this.stdComStandardService.getDepartments().subscribe(
            (response: Department[]) => {
                this.SpinnerService.hide();
                this.departments = response;
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                alert(error.message);
            }
        );
    }

    onSelectDepartment(value: any): any {
        this.SpinnerService.show();
        this.standardDevelopmentService.getTechnicalCommitteeb(value).subscribe(
            (response: TechnicalCommittee[]) => {
                console.log(response);
                this.SpinnerService.hide();
                this.technicalCommittees = response
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                alert(error.message);
            }
        );
    }

    onSelectTechnicalCommittee(value: any): any {
        this.SpinnerService.show();
        this.standardDevelopmentService.getProductsb(value).subscribe(
            (response: Product[]) => {
                console.log(response);
                this.SpinnerService.hide();
                this.products = response
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                alert(error.message);
            }
        );
    }


    onSelectProductCategory(value: any): any {
        this.SpinnerService.show();
        this.standardDevelopmentService.getProductSubcategoryb(value).subscribe(
            (response: ProductSubCategory[]) => {
                console.log(response);
                this.SpinnerService.hide();
                this.productsSubCategory = response
            },
            (error: HttpErrorResponse) => {
                this.SpinnerService.hide();
                alert(error.message);
            }
        );
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
