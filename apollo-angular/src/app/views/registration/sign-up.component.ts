import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {
  BrsLookUpRequest,
  BusinessLines,
  BusinessLinesService,
  BusinessNatures,
  BusinessNaturesService,
  Company,
  County,
  CountyService,
  loadBrsValidations,
  loadResponsesFailure,
  Region,
  RegionService,
  RegistrationPayloadService,
  selectBrsValidationCompany,
  selectBrsValidationStep,
  Town,
  TownService,
  User
} from "../../core/store";
import {select, Store} from "@ngrx/store";
import {Observable} from "rxjs";

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styles: []
})
export class SignUpComponent implements OnInit {


  step = 0;

  stepZeroForm: FormGroup = new FormGroup({});
  stepOneForm: FormGroup = new FormGroup({});
  stepTwoForm: FormGroup = new FormGroup({});
  stepThreeForm: FormGroup = new FormGroup({});
  stepFourForm: FormGroup = new FormGroup({});
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


  constructor(
    private service: RegistrationPayloadService,
    private linesService: BusinessLinesService,
    private naturesService: BusinessNaturesService,
    private regionService: RegionService,
    private countyService: CountyService,
    private townService: TownService,
    private store$: Store<any>,
  ) {
    this.businessNatures$ = naturesService.entities$;
    this.businessLines$ = linesService.entities$;
    this.region$ = regionService.entities$;
    this.county$ = countyService.entities$;
    this.town$ = townService.entities$
    regionService.getAll().subscribe();
    countyService.getAll().subscribe();
    townService.getAll().subscribe();
    naturesService.getAll().subscribe();
    linesService.getAll().subscribe();


  }

  ngOnInit(): void {
    this.stepZeroForm = new FormGroup({
      registrationNumber: new FormControl(),
      directorIdNumber: new FormControl(),
    });
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
      credentials: new FormControl('', [Validators.required]),
      confirmCredentials: new FormControl('', [Validators.required]),
    });

  }

  updateSelectedRegion() {
    this.selectedRegion = this.stepOneForm?.get('region')?.value;
  }

  updateSelectedCounty() {
    this.selectedCounty = this.stepOneForm?.get('county')?.value;
  }

  updateSelectedTown() {
    this.selectedTown = this.stepOneForm?.get('town')?.value;
  }

  updateSelectedBusinessLine() {
    this.selectedBusinessLine = this.stepOneForm?.get('businessLines')?.value;
  }

  updateSelectedBusinessNatures() {
    this.selectedBusinessNature = this.stepOneForm?.get('businessNatures')?.value;
  }

  onClickBrsLookup(valid: boolean) {
    if (valid) {
      this.brsLookupRequest = this.stepZeroForm.value;
      console.log(`Sending ${JSON.stringify(this.brsLookupRequest)}`)
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
