import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, FormGroupDirective, NgForm, Validators} from '@angular/forms';
import {
    BrsLookUpRequest,
    BusinessLines,
    BusinessLinesService,
    BusinessNatures,
    BusinessNaturesService,
    Company,
    County,
    CountyService,
    Go,
    loadBrsValidations,
    loadCountyId, loadRegistrations,
    loadRegistrationsTivet,
    loadResponsesFailure,
    loadSendTokenToPhone,
    loadValidateTokenAndPhone,
    Region,
    RegionService,
    RegistrationPayloadService,
    selectBrsValidationCompany,
    selectBrsValidationStep,
    selectCountyIdData,
    selectRegistrationStateSucceeded,
    selectTokenSentStateOtpSent,
    selectValidateTokenAndPhoneValidated,
    Town,
    TownService,
    User
} from '../../core/store';
import {select, Store} from '@ngrx/store';
import {interval, Observable, PartialObserver, Subject, throwError} from 'rxjs';
import {ConfirmedValidator} from '../../core/shared/confirmed.validator';
import {takeUntil} from 'rxjs/operators';
import {ErrorStateMatcher} from "@angular/material/core";
import {NgxSpinnerService} from "ngx-spinner";
export class MyErrorStateMatcher implements ErrorStateMatcher {
    isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
        const isSubmitted = form && form.submitted;
        return !!(control && control.invalid && (control.dirty || control.touched || isSubmitted));
    }
}

@Component({
    selector: 'app-sign-up',
    templateUrl: './sign-up.component.html',
    styles: []
})
export class SignUpComponent implements OnInit {
    @Input() errorMsg: string;
    @Input() displayError: boolean;
    ispause = new Subject();
    time = 59;
    timer!: Observable<number>;
    timerObserver!: PartialObserver<number>;
    step = 0;
    isShow = true;

    public clicked = false;

    stepZeroForm!: FormGroup;
    stepOneForm!: FormGroup;
    stepTwoForm!: FormGroup;
    stepThreeForm!: FormGroup;
    stepFourForm!: FormGroup;
    stepFiveForm!: FormGroup;
    companySoFar: Partial<Company> | undefined;
    userSoFar: Partial<User> | undefined;
    // @ts-ignore
    brsLookupRequest: BrsLookUpRequest;
    businessLines$: Observable<BusinessLines[]>;
    businessNatures$: Observable<BusinessNatures[]>;
    region$: Observable<Region[]>;
    county$: Observable<County[]>;
    town$: Observable<Town[]>;
    selectedBusinessLine = 0;
    selectedBusinessNature = 0;
    selectedRegion = 0;
    selectedCounty = 0;
    selectedTown = 0;
    validationCellphone = '';
    otpSent = false;
    phoneValidated = false;
    // @ts-ignore
    company: Company;
    // @ts-ignore
    user: User;
    submitted = false;
    selectedId: number;

    loading = false;
    loadingText: string;
    validEmailRegister: boolean = false;

    validTextType: boolean = false;
    validNumberType: boolean = false;
    pattern = "https?://.+";

    emailFormControl = new FormControl('', [
        Validators.required,
        Validators.email,
    ]);

    matcher = new MyErrorStateMatcher();

    validConfirmPasswordRegister = false;
    validPasswordRegister = false;


    constructor(
        private service: RegistrationPayloadService,
        private linesService: BusinessLinesService,
        private naturesService: BusinessNaturesService,
        private regionService: RegionService,
        private countyService: CountyService,
        private townService: TownService,
        private formBuilder: FormBuilder,
        private store$: Store<any>,
        private SpinnerService: NgxSpinnerService,

    ) {


        this.businessNatures$ = naturesService.entities$;
        this.businessLines$ = linesService.entities$;
        this.region$ = regionService.entities$;
        this.county$ = countyService.entities$;
        this.town$ = townService.entities$;

        regionService.getAll().subscribe();
        countyService.getAll().subscribe();
        // townService.getAll().subscribe();
        naturesService.getAll().subscribe();
        linesService.getAll().subscribe();


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
        this.timer = interval(1000).pipe(takeUntil(this.ispause));

        this.timerObserver = {
            next: (_: number) => {
                if (this.time === 0) {
                    // tslint:disable-next-line:no-unused-expression
                    this.ispause.next;
                }
                this.time -= 1;
            }
        };

        this.stepZeroForm = this.formBuilder.group({
            registrationNumber: ['', Validators.required],
            directorIdNumber: ['', Validators.required]
        });

        // this.stepZeroForm = new FormGroup({
        //   registrationNumber: new FormControl('',[Validators.required]),
        //   directorIdNumber: new FormControl('',[Validators.required]),
        // });
        this.stepOneForm = new FormGroup({
            name: new FormControl('', [Validators.required]),
            registrationNumber: new FormControl('', [Validators.required]),
            kraPin: new FormControl('', [Validators.required]),
            yearlyTurnover: new FormControl('', [Validators.required]),
            directorIdNumber: new FormControl('', [Validators.required]),
            businessLines: new FormControl('', [Validators.required]),
            businessNatures: new FormControl('', [Validators.required]),
            otherCategory: new FormControl('', [])
        });

        this.stepTwoForm = new FormGroup({
            postalAddress: new FormControl(),
            physicalAddress: new FormControl('', [Validators.required]),
            plotNumber: new FormControl('', [Validators.required]),
            companyEmail: new FormControl('', [Validators.required]),
            companyTelephone: new FormControl('', [Validators.required])
        });
        this.stepThreeForm = new FormGroup({
            buildingName: new FormControl(),
            branchName: new FormControl('', [Validators.required]),
            streetName: new FormControl('', [Validators.required]),
            region: new FormControl('', [Validators.required]),
            county: new FormControl('', [Validators.required]),
            town: new FormControl('', [Validators.required])
        });

        this.stepFourForm = this.formBuilder.group({
                firstName: [],
                lastName: ['', Validators.required],
                //userName: ['', Validators.required],
                email: ['', Validators.required],
                credentials: ['', Validators.required],
                confirmCredentials: ['', [Validators.required]]
            },
            {
                validators: ConfirmedValidator('credentials', 'confirmCredentials')
            });

        this.stepFiveForm = this.formBuilder.group({
            cellphone: ['', Validators.required],
            otp: ['', Validators.required]
        });


    }

    get formStepZeroForm(): any {
        return this.stepZeroForm.controls;
    }

    get formStepOneForm(): any {
        return this.stepOneForm.controls;
    }

    get formStepTwoForm(): any {
        return this.stepTwoForm.controls;
    }

    get formStepThreeForm(): any {
        return this.stepThreeForm.controls;
    }

    get formStepFourForm(): any {
        return this.stepFourForm.controls;
    }

    get formStepFiveForm(): any {
        return this.stepFiveForm.controls;
    }

    updateSelectedRegion() {
        this.selectedRegion = this.stepThreeForm?.get('region')?.value;
    }

    updateSelectedCounty() {
        this.selectedCounty = this.stepThreeForm?.get('county')?.value;
        console.log(`county set to ${this.selectedCounty}`);
        this.store$.dispatch(loadCountyId({payload: this.selectedCounty}));
        this.store$.select(selectCountyIdData).subscribe(
            (d) => {
                if (d) {
                    console.log(`Select county inside is ${d}`);
                    return this.townService.getAll();
                } else {
                    return throwError('Invalid request, Company id is required');
                }
            }
        );

    }

    updateSelectedTown() {
        this.selectedTown = this.stepThreeForm?.get('town')?.value;
        console.log(`town set to ${this.selectedTown}`);
    }

    updateSelectedBusinessLine() {
        this.selectedBusinessLine = this.stepOneForm?.get('businessLines')?.value;
    }

    updateSelectedBusinessNatures() {
        this.selectedBusinessNature = this.stepOneForm?.get('businessNatures')?.value;
        const ratings = [84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95];
        console.log(this.selectedBusinessNature);
        if (ratings.includes(this.selectedBusinessNature)) {
            this.isShow = !this.isShow;
        } else {
            this.isShow = true;
        }
        console.log(this.selectedBusinessNature)

    }


    onClickBrsLookup() {
        this.submitted = true;
        // stop here if form is invalid
        if (this.stepZeroForm.invalid) {
            return;
        }
        if (this.submitted) {
            this.step = 0;
            this.brsLookupRequest = this.stepZeroForm.value;
            // console.log(`Sending ${JSON.stringify(this.brsLookupRequest)}`)
            this.store$.dispatch(loadBrsValidations({payload: this.brsLookupRequest}));
            setTimeout(() => {
                this.store$.pipe(select(selectBrsValidationStep)).subscribe((step: number) => {
                    console.log(`step inside is ${step}`);
                    return this.step = step;
                });
                this.store$.pipe(
                    select(selectBrsValidationCompany)).subscribe((record: Company) => {
                    this.stepOneForm.patchValue(record);
                    this.stepTwoForm.patchValue(record);
                    this.stepThreeForm.patchValue(record);
                    this.stepFourForm.patchValue(record);
                    this.stepFiveForm.patchValue(record);
                    this.companySoFar = record;
                    this.userSoFar = record;

                });
            }, 2000);


            console.log(`step after is ${this.step}`);
        } else {
            this.store$.dispatch(loadResponsesFailure({
                error: {
                    payload: 'Some required details are missing, kindly recheck',
                    status: 100,
                    response: '05'
                }
            }));
        }


    }

    onClickRegisterCompany(valid: boolean) {
        if (valid) {
            // if (this.phoneValidated) {
            //   this.company = {...this.company, ...this.companySoFar};
            //   this.user = {...this.user, ...this.userSoFar};
            //
            //   this.store$.dispatch(loadRegistrations({
            //     payload: {company: this.company, user: this.user}
            //   }));
            //
            //   this.store$.pipe(select(selectRegistrationStateSucceeded)).subscribe((d) => {
            //     console.log(`status inside is ${d}`);
            //     if (d) {
            //       return this.store$.dispatch(Go({payload: '', link: 'login', redirectUrl: ''}));
            //     }
            //   });
            // } else {
            //   this.otpSent = false;
            //   this.stepFiveForm.get('otp')?.reset();
            //   this.store$.dispatch(loadResponsesFailure({
            //     error: {
            //       payload: 'Cellphone needs to be validated',
            //       status: 100,
            //       response: '05'
            //     }
            //   }));
            // }
        } else {
            this.store$.dispatch(loadResponsesFailure({
                error: {
                    payload: 'Some required details are missing, kindly recheck',
                    status: 100,
                    response: '05'
                }
            }));

        }

    }

    onClickValidateOtp() {
        const sendOtp = document.getElementById("sendOtp");
        this.run("Validating OTP")
        this.phoneValidated = true;
        this.store$.dispatch(loadValidateTokenAndPhone({
            payload: {
                phone: this.stepFiveForm?.get('cellphone')?.value,
                token: this.stepFiveForm?.get('otp')?.value,
            }
        }));
        this.store$.pipe(select(selectValidateTokenAndPhoneValidated)).subscribe((d) => {
            if (d) {
                this.otpSent = true;
                // this.stepFourForm?.get('otp')?.reset();
                this.phoneValidated = d;
                if (this.phoneValidated) {
                    // this.userSoFar = {...this.userSoFar, ...this.stepFourForm?.value};
                    this.userSoFar = {...this.userSoFar, ...this.stepFiveForm.value};
                    this.company = {...this.company, ...this.companySoFar};
                    this.user = {...this.user, ...this.userSoFar};

                    this.store$.dispatch(loadRegistrations({
                        payload: {company: this.company, user: this.user}
                    }));

                    this.store$.pipe(select(selectRegistrationStateSucceeded)).subscribe((succeeded) => {
                        if (succeeded) {
                            return this.store$.dispatch(Go({payload: '', link: 'login', redirectUrl: ''}));
                        }
                    });
                } else {
                    this.otpSent = false;
                    sendOtp.textContent = "Resend OTP"
                    this.stepFiveForm.get('otp')?.reset();
                    this.store$.dispatch(loadResponsesFailure({
                        error: {
                            payload: 'Cellphone needs to be validated',
                            status: 100,
                            response: '05'
                        }
                    }));
                }

            } else {
                this.otpSent = false;
                sendOtp.textContent = "Resend OTP"
                this.phoneValidated = false;
                // this.stepFourForm?.get('otp')?.reset();
                return throwError('Could not validate token');

            }
        });

    }

    secondsToHms(d: number) {
        d = Number(d);
        // const m = Math.floor(d % 3600 / 60);
        const s = Math.floor(d % 3600 % 60);

        // const mDisplay = m > 0 ? m + (m == 1 ? ": " : " : ") : "00";
        // const sDisplay = s > 0 ? s + (s == 1 ? "" : "") : "00";
        // tslint:disable-next-line:triple-equals
        return s > 0 ? s + (s == 1 ? '' : '') : '00';
    }

    onClickSendOtp() {
        const sendOtp = document.getElementById("sendOtp");
        this.run("Sending OTP")

        this.otpSent = true;
        this.time = 59;
        this.timer.subscribe(this.timerObserver);
        this.validationCellphone = this.stepFiveForm?.get('cellphone')?.value;

        this.stepFiveForm?.get('otp')?.reset();

        if (
            this.validationCellphone === '' ||
            this.validationCellphone === null
        ) {
            this.otpSent = false;
            sendOtp.textContent = "Resend Otp"

            this.store$.dispatch(loadResponsesFailure({
                error: {
                    payload: 'Enter a valid cellphone number',
                    status: 100,
                    response: '05'
                }
            }));

        } else {
            this.store$.dispatch(loadSendTokenToPhone({
                payload: {
                    phone: this.validationCellphone
                }
            }));

            this.store$.pipe(select(selectTokenSentStateOtpSent)).subscribe((d) => {
                if (d) {
                    return this.otpSent = d;
                } else {
                    this.otpSent = false;
                    sendOtp.textContent = "Resend Otp"

                    return throwError('Unable to send token');

                }
            });
        }


    }

    onClickPrevious() {
        if (this.step > 1) {
            this.step = this.step - 1;
        } else {
            this.step = 1;
        }
    }

    onClickNext(valid: boolean) {
        if (valid) {
            switch (this.step) {
                case 1:
                    this.companySoFar = {...this.companySoFar, ...this.stepOneForm?.value};
                    break;
                case 2:
                    this.companySoFar = {...this.companySoFar, ...this.stepTwoForm?.value};
                    break;
                case 3:
                    this.companySoFar = {...this.companySoFar, ...this.stepThreeForm?.value};
                    break;
                case 4:
                    this.userSoFar = {...this.userSoFar, ...this.stepFourForm?.value};
                    break;
                case 5:
                    this.userSoFar = this.stepFiveForm?.value;
                    break;
            }


            this.step += 1;
        }

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

    passwordValidationRegister(e) {
        if (e.length > 5) {
            this.validPasswordRegister = true;
        } else {
            this.validPasswordRegister = false;
        }
    }

    confirmPasswordValidationRegister(e) {
        if (this.stepFourForm.controls['credentials'].value === e) {
            this.validConfirmPasswordRegister = true;
        } else {
            this.validConfirmPasswordRegister = false;
        }
    }


    run(message: string): void {
        this.loading = true;
        this.loadingText = message
        this.SpinnerService.show()

        this.runAsync().then(() => {
            this.loading = false;
            this.SpinnerService.hide()
        });
    }

    runAsync(): Promise<void> {
        return new Promise((resolve, reject) => {
            setTimeout(() => {
                resolve();
            }, 2000)
        });
    }


}
