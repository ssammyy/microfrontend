import {Component, Input, OnInit} from '@angular/core';
import {Department, Product, ProductSubCategory, TechnicalCommittee} from "../../../../core/store/data/std/std.model";
import {FormBuilder, FormControl, FormGroup, FormGroupDirective, NgForm, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {NgxSpinnerService} from "ngx-spinner";
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {HttpErrorResponse} from "@angular/common/http";
import {ErrorStateMatcher} from "@angular/material/core";
import swal from "sweetalert2";

import {CountryISO, PhoneNumberFormat, SearchCountryField} from "ngx-intl-tel-input";


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

    economicEfficiency: string;
    healthSafety: string;
    environment: string;
    integration: string;
    exportMarkets: string;

    ratings: string[] = ['1', '2', '3', '4', '5'];
    levelOfStandards: string[] = ['East Africa Standard', 'ARSO Standard', 'AFSEC Standard', 'Company Standard', 'Kenya Standard']
    selectedStandard = 'Kenya Standard';
    title = 'toaster-not';
    public uploadedFiles: Array<File> = [];

    emailFormControl = new FormControl('', [
        Validators.required,
        Validators.email,
    ]);

    validEmailRegister: boolean = false;

    validTextType: boolean = false;
    validNumberType: boolean = false;
    pattern = "https?://.+";


    matcher = new MyErrorStateMatcher();
    register: FormGroup;
    stdRequestFormGroup: FormGroup;


    isFormSubmitted = false;

    SearchCountryField = SearchCountryField;
    CountryISO = CountryISO;
    PhoneNumberFormat = PhoneNumberFormat;
    preferredCountries: CountryISO[] = [CountryISO.Kenya, CountryISO.Kenya];

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
                control.markAsTouched({onlySelf: true});
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

            email: [null, [Validators.required, Validators.pattern("^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$")]],
            name: ['', Validators.required],
            organisationName: ['', Validators.required],
            phone: ['', Validators.required],
            // tcId: ['', Validators.required],
            departmentId: ['', Validators.required],

            // productId: ['', Validators.required],
            // productSubCategoryId: ['', Validators.required]
            subject: ['', Validators.required],
            description: ['', Validators.required],
            economicEfficiency: ['', Validators.required],
            healthSafety: ['', Validators.required],
            environment: ['', Validators.required],
            integration: ['', Validators.required],
            exportMarkets: ['', Validators.required],
            levelOfStandard: ['', Validators.required],
            captcha: ['', Validators.required],

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

    saveStandard(formDirective): void {
        this.isFormSubmitted = true;

        if (this.stdRequestFormGroup.valid) {

            // if (this.uploadedFiles != null && this.uploadedFiles.length > 0) {
            //     this.onClickSaveUploads("568","Marvin")
            // }
           // e164Number:"+254702882256"
            let Data:any = this.stdRequestFormGroup.controls['phone'].value;
            this.stdRequestFormGroup.controls['phone'].setValue(Data.e164Number.replace('+', ''))
            this.SpinnerService.show();
            this.standardDevelopmentService.addStandardRequest(this.stdRequestFormGroup.value).subscribe(
                (response) => {

                    // let requestNumber = response.responseText+":"+response.body.body.requestNumber;

                    this.showToasterSuccess(response.httpStatus, `Successfully submitted your application`);
                    if (this.uploadedFiles != null && this.uploadedFiles.length > 0) {
                        this.onClickSaveUploads(response.body.savedRowID, this.stdRequestFormGroup.get("name").value.toString())
                        formDirective.resetForm();
                        this.stdRequestFormGroup.reset()
                        this.isFormSubmitted = false;

                    } else {
                        this.SpinnerService.hide();
                        formDirective.resetForm();
                        this.isFormSubmitted = false;
                        this.stdRequestFormGroup.reset()
                        swal.fire({
                            title: 'Your Application Has Been Submitted.',
                            buttonsStyling: false,
                            customClass: {
                                confirmButton: 'btn btn-success form-wizard-next-btn ',
                            },
                            icon: 'success'
                        });
                    }
                },
                (error: HttpErrorResponse) => {
                    this.SpinnerService.hide();

                    alert(error.message);
                }
            );

        } else {
            this.validateAllFormFields(this.stdRequestFormGroup);
        }


    }

    onClickSaveUploads(draftStandardID: string, nomineeName: string) {
        if (this.uploadedFiles.length > 0) {
            const file = this.uploadedFiles;
            const formData = new FormData();
            for (let i = 0; i < file.length; i++) {
                console.log(file[i]);
                formData.append('docFile', file[i], file[i].name);
            }
            this.SpinnerService.show();
            this.standardDevelopmentService.uploadFileDetails(draftStandardID, formData, "AdditionalInformation", nomineeName).subscribe(
                (data: any) => {
                    this.SpinnerService.hide();
                    this.uploadedFiles = [];

                    swal.fire({
                        title: 'Your Application Has Been Submitted.',
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success form-wizard-next-btn ',
                        },
                        icon: 'success'
                    });
                },
            );
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

    emailValidationRegister(e) {
        var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        if (re.test(String(e).toLowerCase())) {
            this.validEmailRegister = true;
        } else {
            this.validEmailRegister = false;
        }
    }


    textValidationType(e) {
        if (e) {
            this.validTextType = true;
        } else {
            this.validTextType = false;
        }
    }

    numberValidationType(e) {
        if (e) {
            this.validNumberType = true;
        } else {
            this.validNumberType = false;
        }
    }

    validateCaptcha() {
        this.stdRequestFormGroup.value.captcha = '';

    }


}
