import {Component, Input, OnInit} from '@angular/core';
import {
    Department,
    Product,
    ProductSubCategory,
    StandardRequest,
    TechnicalCommittee
} from "../../../../core/store/data/std/std.model";
import {FormBuilder, FormControl, FormGroup, FormGroupDirective, NgForm, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {NgxSpinnerService} from "ngx-spinner";
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {HttpErrorResponse} from "@angular/common/http";
import {ErrorStateMatcher} from "@angular/material/core";
export class MyErrorStateMatcher implements ErrorStateMatcher {
    isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
        const isSubmitted = form && form.submitted;
        return !!(control && control.invalid && (control.dirty || control.touched || isSubmitted));
    }
}
@Component({
    selector: 'app-request-standard-form',
    templateUrl: './request-standard-form.component.html',
    styleUrls: ['./request-standard-form.component.css']
})
export class RequestStandardFormComponent implements OnInit {
    @Input() errorMsg: string;
    @Input() displayError: boolean;
    public products !: Product[];
    public productsSubCategory !: ProductSubCategory[];
    public departments !: Department[];
    public technicalCommittees !: TechnicalCommittee[];


    title = 'toaster-not';

    emailFormControl = new FormControl('', [
        Validators.required,
        Validators.email,
    ]);

    validEmailRegister: boolean = false;
    validConfirmPasswordRegister: boolean = false;
    validPasswordRegister: boolean = false;

    validEmailLogin: boolean = false;
    validPasswordLogin: boolean = false;

    validTextType: boolean = false;
    validEmailType: boolean = false;
    validNumberType: boolean = false;
    pattern = "https?://.+";


    matcher = new MyErrorStateMatcher();
    register : FormGroup;
    stdRequestFormGroup : FormGroup;

    login : FormGroup;
    type : FormGroup;


    constructor(
        private formBuilder: FormBuilder,
        private route: ActivatedRoute,
        private router: Router,
        private standardDevelopmentService: StandardDevelopmentService,
        private SpinnerService: NgxSpinnerService,
        private notifyService: NotificationService
    ) {
    }
    isFieldValid(form: FormGroup, field: string) {
        return !form.get(field).valid && form.get(field).touched;
    }

    displayFieldCss(form: FormGroup, field: string) {
        return {
            'has-error': this.isFieldValid(form, field),
            'has-feedback': this.isFieldValid(form, field)
        };
    }


    validateAllFormFields(formGroup: FormGroup) {
        Object.keys(formGroup.controls).forEach(field => {
            const control = formGroup.get(field);
            if (control instanceof FormControl) {
                control.markAsTouched({ onlySelf: true });
            } else if (control instanceof FormGroup) {
                this.validateAllFormFields(control);
            }
        });
    }

    ngOnInit(): void {
        //this.getStandards();
        this.getDepartments();
        //this.getTechnicalCommittee();


        this.stdRequestFormGroup = this.formBuilder.group({

            email: [null, [Validators.required,Validators.pattern("^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$")]],
            name: ['', Validators.required],
            phone: ['', Validators.required],
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

        if (this.stdRequestFormGroup.valid) {

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
        } else {
            this.validateAllFormFields(this.stdRequestFormGroup);
        }
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


    onSelectTechnicalCommittee(value: any): any {
        this.standardDevelopmentService.getProductsb(value).subscribe(
            (response: Product[]) => {
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
                this.productsSubCategory = response
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );
    }

    emailValidationRegister(e){
        var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        if (re.test(String(e).toLowerCase())) {
            this.validEmailRegister = true;
        } else {
            this.validEmailRegister = false;
        }
    }




    textValidationType(e){
        if (e) {
            this.validTextType = true;
        }else{
            this.validTextType = false;
        }
    }

    numberValidationType(e){
        if (e) {
            this.validNumberType = true;
        }else{
            this.validNumberType = false;
        }
    }


}
