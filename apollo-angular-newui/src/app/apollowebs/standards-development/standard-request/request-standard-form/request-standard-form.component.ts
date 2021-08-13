import {Component, OnInit} from '@angular/core';
import {
    Department,
    Product,
    ProductSubCategory,
    StandardRequest,
    TechnicalCommittee
} from "../../../../core/store/data/std/std.model";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {NgxSpinnerService} from "ngx-spinner";
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
    selector: 'app-request-standard-form',
    templateUrl: './request-standard-form.component.html',
    styleUrls: ['./request-standard-form.component.css']
})
export class RequestStandardFormComponent implements OnInit {

    public products !: Product[];
    public productsSubCategory !: ProductSubCategory[];
    public departments !: Department[];
    public technicalCommittees !: TechnicalCommittee[];

    public stdRequestFormGroup!: FormGroup;

    title = 'toaster-not';

    constructor(
        private formBuilder: FormBuilder,
        private route: ActivatedRoute,
        private router: Router,
        private standardDevelopmentService: StandardDevelopmentService,
        private SpinnerService: NgxSpinnerService,
        private notifyService: NotificationService
    ) {
    }

    ngOnInit(): void {
        //this.getStandards();
        this.getDepartments();
        //this.getTechnicalCommittee();


        this.stdRequestFormGroup = this.formBuilder.group({
            name: ['', Validators.required],
            phone: ['', Validators.required],
            email: ['', Validators.required],
            tcId: ['', Validators.required],
            departmentId: ['', Validators.required],
            productId: ['', Validators.required],
            productSubCategoryId: ['', Validators.required]
        });
    }

    showToasterSuccess(title: string, message: string) {
        this.notifyService.showSuccess(message, title)

    }

    get formStdRequest(): any {
        return this.stdRequestFormGroup.controls;
    }

    /*public getProducts(): void {
      this.standardDevelopmentService.getProducts().subscribe(
        (response: Product[]) => {
          this.products = response;
        },
        (error: HttpErrorResponse) => {
          alert(error.message);
        }
      );
    }*/

    public getDepartments(): void {
        this.standardDevelopmentService.getDepartmentsb().subscribe(
            (response: Department[]) => {
                this.departments = response;
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );
    }

    /*public getTechnicalCommittee(): void {
      this.standardDevelopmentService.getTechnicalCommittee().subscribe(
        (response: TechnicalCommittee[]) => {
          this.technicalCommittees = response;
        },
        (error: HttpErrorResponse) => {
          alert(error.message);
        }
      );
    }*/

    saveStandard(): void {
        this.SpinnerService.show();
        this.standardDevelopmentService.addStandardRequest(this.stdRequestFormGroup.value).subscribe(
            (response: StandardRequest) => {
                console.log(response);
                this.SpinnerService.hide();

                // let requestNumber = response.responseText+":"+response.body.body.requestNumber;

                this.showToasterSuccess(response.name, "Request Successfully Submitted")
                //this.router.navigate(['/login'])
                this.stdRequestFormGroup.reset()
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );
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


    onSelectProductCategory(value: any): any {
        this.standardDevelopmentService.getProductSubcategoryb(value).subscribe(
            (response: ProductSubCategory[]) => {
                console.log(response);
                this.productsSubCategory = response
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );
    }


}
