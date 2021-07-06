import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
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
  selectSendTokenToPhoneStateSent,
  selectValidateTokenAndPhoneValidated,
  Town,
  TownService,
  User
} from "../../core/store";
import {select, Store} from "@ngrx/store";
import {Observable, throwError} from "rxjs";

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styles: []
})
export class SignUpComponent implements OnInit {


  step = 0;

  stepZeroForm!: FormGroup;
  stepOneForm!: FormGroup;
  stepTwoForm!: FormGroup;
  stepThreeForm!: FormGroup;
  stepFourForm!: FormGroup;
  companySoFar: Partial<Company> | undefined;
  userSoFar: Partial<User> | undefined;
  // @ts-ignore
  brsLookupRequest: BrsLookUpRequest;
  businessLines$: Observable<BusinessLines[]>;
  businessNatures$: Observable<BusinessNatures[]>;
  region$: Observable<Region[]>;
  county$: Observable<County[]>;
  town$: Observable<Town[]>;
  selectedBusinessLine: number = 0;
  selectedBusinessNature: number = 0;
  selectedRegion: number = 0;
  selectedCounty: number = 0;
  selectedTown: number = 0;
  validationCellphone = '';
  otpSent: boolean;
  phoneValidated: boolean;
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
    this.otpSent = false;
    this.phoneValidated = false;

    this.businessNatures$ = naturesService.entities$;
    this.businessLines$ = linesService.entities$;
    this.region$ = regionService.entities$;
    this.county$ = countyService.entities$;
    this.town$ = townService.entities$

    regionService.getAll().subscribe();
    countyService.getAll().subscribe();
    // townService.getAll().subscribe();
    naturesService.getAll().subscribe();
    linesService.getAll().subscribe();


  }

  ngOnInit(): void {
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
    this.stepFourForm = new FormGroup({
      firstName: new FormControl(),
      lastName: new FormControl('', [Validators.required]),
      userName: new FormControl('', [Validators.required]),
      email: new FormControl('', [Validators.required]),
      cellphone: new FormControl('', [Validators.required]),
      otp: new FormControl('', [Validators.required]),
      credentials: new FormControl('', [Validators.required]),
      confirmCredentials: new FormControl('', [Validators.required]),
    });

  }

  get formStepZeroForm(): any {return this.stepZeroForm.controls;}
  get formStepOneForm(): any {return this.stepOneForm.controls;}
  get formStepTwoForm(): any {return this.stepTwoForm.controls;}
  get formStepThreeForm(): any {return this.stepThreeForm.controls;}
  get formStepFourForm(): any {return this.stepFourForm.controls;}

  updateSelectedRegion() {
    this.selectedRegion = this.stepThreeForm?.get('region')?.value;
  }

  updateSelectedCounty() {
    this.selectedCounty = this.stepThreeForm?.get('county')?.value;
    console.log(`county set to ${this.selectedCounty}`)
    this.store$.dispatch(loadCountyId({payload: this.selectedCounty}));
    this.store$.select(selectCountyIdData).subscribe(
      (d) => {
        if (d) {
          console.log(`Select county inside is ${d}`);
          return this.townService.getAll();
        } else return throwError('Invalid request, Company id is required');
      }
    );

  }

  updateSelectedTown() {
    this.selectedTown = this.stepThreeForm?.get('town')?.value;
    console.log(`town set to ${this.selectedTown}`)
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
      this.step = 0
      this.brsLookupRequest = this.stepZeroForm.value;
      // console.log(`Sending ${JSON.stringify(this.brsLookupRequest)}`)
      this.store$.dispatch(loadBrsValidations({payload: this.brsLookupRequest}));
      this.store$.pipe(select(selectBrsValidationStep)).subscribe((step: number) => {
        console.log(`step inside is ${step}`)
        return this.step = step;
      });
      this.store$.pipe(
        select(selectBrsValidationCompany)).subscribe((record: Company) => {
        this.stepOneForm.patchValue(record);
        this.stepTwoForm.patchValue(record);
        this.stepThreeForm.patchValue(record);
        this.stepFourForm.patchValue(record);
        this.companySoFar = record;
      });

      console.log(`step after is ${this.step}`)
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
        this.user = {...this.user, ...this.stepFourForm?.value}

        this.store$.dispatch(loadRegistrations({
          payload: {company: this.company, user: this.user}
        }));

        this.store$.pipe(select(selectRegistrationStateSucceeded)).subscribe((d) => {
          console.log(`status inside is ${d}`)
          if (d) {
            return this.store$.dispatch(Go({payload: '', link: 'login', redirectUrl: ''}));
          }
        });
      } else {
        this.otpSent = false;
        this.stepFourForm.get('otp')?.reset();
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
    this.store$.dispatch(loadValidateTokenAndPhone({
      payload: {
        phone: this.stepFourForm?.get('cellphone')?.value,
        token: this.stepFourForm?.get('otp')?.value,
      }
    }));
    this.store$.pipe(select(selectValidateTokenAndPhoneValidated)).subscribe((d) => {
      console.log(`status inside is ${d}`)
      return this.phoneValidated = d;
    });
    if (!this.phoneValidated) {
      this.otpSent = false;
      this.stepFourForm?.get('otp')?.reset();

      this.phoneValidated = true;

    } else {

    }
  }

  onClickSendOtp() {
    this.otpSent = true;
    this.validationCellphone = this.stepFourForm?.get('cellphone')?.value

    this.stepFourForm?.get('otp')?.reset()

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

      this.store$.pipe(select(selectSendTokenToPhoneStateSent)).subscribe((d) => {
        console.log(`value of inside is ${d}`)
        return this.otpSent = d;
      });
    }


  }

  onClickPrevious() {
    if (this.step > 1) {
      this.step = this.step - 1
    } else {
      this.step = 1
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
          this.userSoFar = this.stepFourForm?.value;
          break;
      }
      this.step += 1;
    }

  }


}
