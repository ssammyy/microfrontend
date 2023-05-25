import {AfterViewInit, Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, FormGroupDirective, NgForm, Validators} from "@angular/forms";
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";
import {MembershipToTcService} from "../../../../core/store/data/std/membership-to-tc.service";
import {Store} from "@ngrx/store";
import {Router} from "@angular/router";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {NgxSpinnerService} from "ngx-spinner";
import {HttpErrorResponse} from "@angular/common/http";
import {DatePipe} from "@angular/common";
import swal from "sweetalert2";
import {TechnicalCommittee} from "../../../../core/store/data/std/std.model";
import {ErrorStateMatcher} from "@angular/material/core";
// import {CountryISO, PhoneNumberFormat, SearchCountryField} from "ngx-intl-tel-input";
import {DataHolder} from "../../../../core/store/data/std/request_std.model";
import {take, takeUntil,} from 'rxjs/operators';
import {ReplaySubject, Subject} from "rxjs";
import {MatSelect} from "@angular/material/select";

export class MyErrorStateMatcher implements ErrorStateMatcher {
    isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
        const isSubmitted = form && form.submitted;
        return !!(control && control.invalid && (control.dirty || control.touched || isSubmitted));
    }
}

export const _filter = (opt: string[], value: string): string[] => {
    const filterValue = value.toLowerCase();

    return opt.filter(item => item.toLowerCase().includes(filterValue));
};

@Component({
    selector: 'app-submit-application',
    templateUrl: './submit-application.component.html',
    styleUrls: ['./submit-application.component.css']
})
export class SubmitApplicationComponent implements OnInit,AfterViewInit {
    public submitApplicationsTask: TechnicalCommittee[] = [];
    public actionRequest: TechnicalCommittee | undefined;
    public prepareDraftStandardFormGroup!: FormGroup;
    loadingText: string;
    @ViewChild('demoForm', {
        static: false
    }) editForm: NgForm;

    public tcs: DataHolder[] = [];


    @Input() errorMsg: string;
    @Input() displayError: boolean;
    public technicalCommittees !: TechnicalCommittee[];

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
    applicationToATcFormGroup: FormGroup;


    isFormSubmitted = false;
    removeValidation = false;


    // SearchCountryField = SearchCountryField;
    // CountryISO = CountryISO;
    // PhoneNumberFormat = PhoneNumberFormat;
    // preferredCountries: CountryISO[] = [CountryISO.Kenya, CountryISO.Kenya];

    public filteredTcs: ReplaySubject<any> = new ReplaySubject(1);

    protected _onDestroy = new Subject();
    @ViewChild('singleSelect', {static: true}) singleSelect: MatSelect;

    public websiteFilterCtrl: FormControl = new FormControl();
    selectedTc: any
    @Output() onSelectionChange: EventEmitter<any> = new EventEmitter<any>();
    selectedTcId: string;


    constructor(private formBuilder: FormBuilder,
                private standardDevelopmentService: StandardDevelopmentService,
                private membershipToTcService: MembershipToTcService,
                private store$: Store<any>,
                private router: Router,
                private datePipe: DatePipe,
                private notifyService: NotificationService,
                private SpinnerService: NgxSpinnerService) {
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
        this.getTechnicalCommittee();
        this.applicationToATcFormGroup = this.formBuilder.group({
            tcId: ['', Validators.required],
            organisationClassification: ['', Validators.required],
            organisationName: ['', Validators.required],
            nomineeName: ['', Validators.required],
            position: ['', Validators.required],
            postalAddress: ['', Validators.required],
            mobileNumber: ['', Validators.required],
            email: [null, [Validators.required, Validators.pattern("^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$")]],
            authorizingPerson: ['', Validators.required],
            authorizingPersonPosition: ['', Validators.required],
            authorizingPersonEmail: [null, [Validators.required, Validators.pattern("^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$")]],
            qualification: ['', Validators.required],
            terms: ['', Validators.required],
            captcha: ['', Validators.required],


        })
        this.applicationToATcFormGroup.get('tcId').setValue(this.tcs[1]);
        this.filteredTcs.next(this.tcs.slice());
        this.websiteFilterCtrl.valueChanges
            .pipe(takeUntil(this._onDestroy))
            .subscribe(() => {
                this.filteredTc();
            });

    }


    ngAfterViewInit() {
        this.setInitialValue();
    }

    /**
     * Write code on Method
     *
     * method logical code
     */
    ngOnDestroy() {
        this._onDestroy.next();
        this._onDestroy.complete();
    }

    protected setInitialValue() {
        this.filteredTcs
            .pipe(take(1), takeUntil(this._onDestroy))
            .subscribe(() => {
                this.singleSelect.compareWith = (a: DataHolder, b: DataHolder) => a && b && a.id === b.id;
            });
    }

    /**
     * Write code on Method
     *
     * method logical code
     */
    protected filteredTc() {
        if (!this.tcs) {
            return;
        }
        let search = this.websiteFilterCtrl.value;
        if (!search) {
            this.filteredTcs.next(this.tcs.slice());
            return;
        } else {
            search = search.toLowerCase();// Assign the selected value to the variable
        }
        this.filteredTcs.next(
            this.tcs.filter(bank => bank.tc_Title.toLowerCase().indexOf(search) > -1)
        );
    }
    onTcOptionSelected(tc: any): void {
        this.selectedTc = tc;
        console.log(tc)
    }

    get formStdApplicationToATc(): any {
        return this.applicationToATcFormGroup.controls;
    }

    public getTechnicalCommittee(): void {
        this.standardDevelopmentService.getAllTcsForApplication().subscribe(
            (response: DataHolder[]) => {
                this.tcs = response;
                this.filteredTcs.next(this.tcs);

            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        )
    }
    formatDate(date: string): string {


        const myArray = date.split("T");
        return myArray[0] // syntax for datepipe

        // return date
    }

    showToasterSuccess(title: string, message: string) {
        this.notifyService.showSuccess(message, title)
    }

    showToasterError(title: string, message: string) {
        this.notifyService.showError(message, title)

    }

    get formPrepareJustification(): any {
        return this.prepareDraftStandardFormGroup.controls;
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
            this.membershipToTcService.uploadFileDetails(draftStandardID, formData, "ApplicantCV", nomineeName).subscribe(
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

    saveApplication(formDirective): void {
        this.isFormSubmitted = true;

        this.applicationToATcFormGroup.controls['tcId'].setValue(this.selectedTc.id)
        if (this.applicationToATcFormGroup.valid) {

            if ( !this.removeValidation && this.applicationToATcFormGroup.controls['email'].value == this.applicationToATcFormGroup.controls['authorizingPersonEmail'].value) {
                this.showToasterError("Wrong Input", "Both Your Email And The Authorising Person's Email Are The Same")
            }
            if (this.uploadedFiles == null || this.uploadedFiles.length <= 0) {
                this.showToasterError("Enter CV", "Please Upload Your Cv")

            }


            else {
                let Data: any = this.applicationToATcFormGroup.controls['mobileNumber'].value;
                this.applicationToATcFormGroup.controls['mobileNumber'].setValue(Data.e164Number)
                this.SpinnerService.show();
                this.membershipToTcService.onSubmitApplication(this.applicationToATcFormGroup.value).subscribe(
                    (response) => {
                        this.showToasterSuccess(response.httpStatus, `Successfully submitted your application`);
                        if (this.uploadedFiles != null && this.uploadedFiles.length > 0) {
                            this.onClickSaveUploads(response.body.savedRowID, this.applicationToATcFormGroup.get("nomineeName").value.toString())
                            formDirective.resetForm();
                            this.applicationToATcFormGroup.reset()
                            this.isFormSubmitted = false;

                        } else {
                            this.SpinnerService.hide();
                            formDirective.resetForm();
                            this.isFormSubmitted = false;
                            this.applicationToATcFormGroup.reset()
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

            }
        } else {
            this.validateAllFormFields(this.applicationToATcFormGroup);
        }


    }

    emailValidationRegister(e) {
        const re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        this.validEmailRegister = re.test(String(e).toLowerCase());
    }


    textValidationType(e) {
        this.validTextType = !!e;
    }

    numberValidationType(e) {
        this.validNumberType = !!e;
    }

    validateCaptcha() {
        this.applicationToATcFormGroup.value.captcha = '';

    }

    onOptionChange(): void {

        const selectedOption = this.applicationToATcFormGroup.get('organisationClassification').value;
        const field1Control = this.applicationToATcFormGroup.get('authorizingPerson');
        const field2Control = this.applicationToATcFormGroup.get('authorizingPersonPosition');
        const field2Control3 = this.applicationToATcFormGroup.get('authorizingPersonEmail');

        if (selectedOption === 'Renown Professionals/experts') {
            field1Control.clearValidators();
            field2Control.clearValidators();
            field2Control3.clearValidators();
            this.removeValidation = true
        } else {
            field1Control.setValidators([Validators.required]);
            field2Control.setValidators([Validators.required]);
            field2Control3.setValidators([Validators.required]);
            this.removeValidation = false

        }

        field1Control.updateValueAndValidity();
        field2Control.updateValueAndValidity();
        field2Control3.updateValueAndValidity();

    }


}
