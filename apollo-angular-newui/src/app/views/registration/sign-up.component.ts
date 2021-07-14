import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
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
  loadCountyId,
  loadRegistrations,
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

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styles: []
})
export class SignUpComponent implements OnInit {

  ispause = new Subject();
  time = 30;
  timer!: Observable<number>;
  timerObserver!: PartialObserver<number>;
  step = 0;

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


  constructor(
    private service: RegistrationPayloadService,
    private linesService: BusinessLinesService,
    private naturesService: BusinessNaturesService,
    private regionService: RegionService,
    private countyService: CountyService,
    private townService: TownService,
    private formBuilder: FormBuilder,
    private store$: Store<any>,
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
      streetName: new FormControl('', [Validators.required]),
      region: new FormControl('', [Validators.required]),
      county: new FormControl('', [Validators.required]),
      town: new FormControl('', [Validators.required])
    });
    this.stepFourForm = this.formBuilder.group({
          firstName: [],
          lastName: ['', Validators.required],
          userName: ['', Validators.required],
          email: ['', Validators.required],

        },
        {
          validators: ConfirmedValidator('credentials', 'confirmCredentials')
        });

    this.stepFiveForm = this.formBuilder.group({

          cellphone: ['', Validators.required],
          otp: ['', Validators.required],
          credentials: ['', Validators.required],
          confirmCredentials: ['', [Validators.required]]
        },
        {
          validators: ConfirmedValidator('credentials', 'confirmCredentials')
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
        } else { return throwError('Invalid request, Company id is required'); }
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
      });

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
      if (this.phoneValidated) {
        this.company = {...this.company, ...this.companySoFar};
        this.user = {...this.user, ...this.stepFiveForm?.value};

        this.store$.dispatch(loadRegistrations({
          payload: {company: this.company, user: this.user}
        }));

        this.store$.pipe(select(selectRegistrationStateSucceeded)).subscribe((d) => {
          console.log(`status inside is ${d}`);
          if (d) {
            return this.store$.dispatch(Go({payload: '', link: 'login', redirectUrl: ''}));
          }
        });
      } else {
        this.otpSent = false;
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
    this.phoneValidated = true;
    this.store$.dispatch(loadValidateTokenAndPhone({
      payload: {
        phone: this.stepFiveForm?.get('cellphone')?.value,
        token: this.stepFiveForm?.get('otp')?.value,
      }
    }));
    this.store$.pipe(select(selectValidateTokenAndPhoneValidated)).subscribe((d) => {
      // console.log(`status inside is ${d}`)
      if (d) {
        this.otpSent = true;
        // this.stepFourForm?.get('otp')?.reset();
        return this.phoneValidated = d;
      } else {
          this.otpSent = false;
        this.phoneValidated = false;
        this.stepFiveForm?.get('otp')?.reset();
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
        this.otpSent = true;
        this.time = 30;
      this.timer.subscribe(this.timerObserver);
      this.validationCellphone = this.stepFiveForm?.get('cellphone')?.value;

      this.stepFiveForm?.get('otp')?.reset();

        if (
            this.validationCellphone === '' ||
      this.validationCellphone === null
    ) {
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
        console.log(`value of inside is ${d}`);
        if (d) {
          return this.otpSent = d;
        } else {
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
          this.companySoFar = {...this.companySoFar, ...this.stepFourForm?.value};
          break;
        case 5:
          this.userSoFar = this.stepFiveForm?.value;
          break;
      }
      this.step += 1;
    }

  }


}
