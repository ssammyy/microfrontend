import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {
  BusinessLines,
  BusinessLinesService,
  BusinessNatures, BusinessNaturesService,
  Company,
  CompanyService,
  County, CountyService, Go, loadCountyId, loadResponsesFailure, loadResponsesSuccess,
  Region, RegionService, selectCompanyData, selectCountyIdData,
  Town, TownService
} from '../../../core/store';
import {Observable, of, throwError} from 'rxjs';
import {Store} from '@ngrx/store';
import {catchError} from 'rxjs/operators';
import {HttpErrorResponse} from '@angular/common/http';

@Component({
  selector: 'app-company-view',
  templateUrl: './company-view.component.html',
  styleUrls: ['./company-view.component.css']
})
export class CompanyViewComponent implements OnInit {


  stepOneForm: FormGroup = new FormGroup({});
  stepTwoForm: FormGroup = new FormGroup({});
  stepThreeForm: FormGroup = new FormGroup({});
  companySoFar: Partial<Company> | undefined;
  // @ts-ignore
  company: Company;
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
  step = 1;

  constructor(
      private service: CompanyService,
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
    this.town$ = townService.entities$;
  }


  ngOnInit(): void {
    this.stepOneForm = new FormGroup({
      name: new FormControl({value: '', disabled: true}, [Validators.required]),
      registrationNumber: new FormControl({value: '', disabled: true}, [Validators.required]),
      kraPin: new FormControl({value: '', disabled: true}, [Validators.required]),
      yearlyTurnover: new FormControl({value: '', disabled: true}, [Validators.required]),
      directorIdNumber: new FormControl({value: '', disabled: true}, [Validators.required]),
      businessLines: new FormControl({value: '', disabled: true}, [Validators.required]),
      businessNatures: new FormControl({value: '', disabled: true}, [Validators.required]),
      otherBusinessNatureType: new FormControl({value: '', disabled: true}, [Validators.required]),
    });

    this.stepTwoForm = new FormGroup({
      postalAddress: new FormControl(),
      physicalAddress: new FormControl({value: '', disabled: true}, [Validators.required]),
      plotNumber: new FormControl({value: '', disabled: true}, [Validators.required]),
      companyEmail: new FormControl({value: '', disabled: true}, [Validators.required]),
      companyTelephone: new FormControl({value: '', disabled: true}, [Validators.required])
    });
    this.stepThreeForm = new FormGroup({
      buildingName: new FormControl(),
      streetName: new FormControl({value: '', disabled: true}, [Validators.required]),
      region: new FormControl({value: '', disabled: true}, [Validators.required]),
      county: new FormControl({value: '', disabled: true}, [Validators.required]),
      town: new FormControl({value: '', disabled: true}, [Validators.required])
    });
    this.store$.select(selectCompanyData).subscribe((d) => {
      console.log(d);
      return this.company = d;
    });

    this.stepOneForm.patchValue(this.company);
    this.stepTwoForm.patchValue(this.company);
    this.stepThreeForm.patchValue(this.company);
    console.log(`Select town ID inside is ${this.company.town}`);
    this.companySoFar = this.company;

    this.regionService.getAll().subscribe();
    this.countyService.getAll().subscribe();
    // townService.getAll().subscribe();
    this.naturesService.getAll().subscribe();
    this.linesService.getAll().subscribe();
    this.selectedCounty = this.company.county;
    // console.log(`county set to ${this.selectedCounty}`)
    this.store$.dispatch(loadCountyId({payload: this.company.county}));
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
    console.log(`Select town ID inside is ${this.company.town}`);
  }

  updateSelectedRegion() {
    this.selectedRegion = this.stepThreeForm?.get('region')?.value;
    console.log(`region set to ${this.selectedRegion}`);
  }

  updateSelectedCounty() {

    this.selectedCounty = this.stepThreeForm?.get('county')?.value;
    // console.log(`county set to ${this.selectedCounty}`)
    this.store$.dispatch(loadCountyId({payload: this.company.county}));
    this.store$.select(selectCountyIdData).subscribe(
        (d) => {
          if (d) {
            // console.log(`Select county inside is ${d}`);
            return this.townService.getAll();
          } else {
            return throwError('Invalid request, Company id is required');
          }
        }
    );
    console.log(`Select town ID inside is ${this.company.town}`);
    this.stepThreeForm?.get('town').setValue(this.company.town);
  }

  updateSelectedTown() {
    this.selectedTown = this.stepThreeForm?.get('town')?.value;
      // console.log(`town set to ${this.selectedTown}`);
  }

  updateSelectedBusinessLine() {
    this.selectedBusinessLine = this.stepOneForm?.get('businessLines')?.value;
  }

  updateSelectedBusinessNatures() {
    this.selectedBusinessNature = this.stepOneForm?.get('businessNatures')?.value;
  }


  onClickPrevious() {
    if (this.step > 1) {
      this.step = this.step - 1;
    } else {
      this.step = 1;
    }
  }

  onClickNext(valid: boolean) {
    // if (valid) {
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
    }
    this.step += 1;
    console.log(`Clicked and step = ${this.step}`);
    // }
  }

  onClickSave(valid: boolean) {
    if (valid) {
      this.companySoFar = {...this.companySoFar, ...this.stepThreeForm.value};
      this.company = {...this.company, ...this.companySoFar};

      this.service.update(this.company).subscribe(
          (a) => {

            this.store$.dispatch(
                loadResponsesSuccess({
                  message: {
                    response: '00',
                    payload: `Successfully saved ${a.name}`,
                    status: 200
                  }
                })
            );
            return this.store$.dispatch(Go({
              payload: null,
              link: 'dashboard/companies',
              redirectUrl: 'dashboard/companies'
            }));


          },
          catchError(
              (err: HttpErrorResponse) => {
                return of(loadResponsesFailure({
                  error: {
                    payload: err.error,
                    status: err.status,
                    response: (err.error instanceof ErrorEvent) ? `Error: ${err.error.message}` : `Error Code: ${err.status},  Message: ${err.error}`
                  }
                }));
              }));
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

  selectStepOneClass(step: number): string {
    if (step === 1) {
      return 'active';
    } else {
      return '';
    }
  }

  selectStepTwoClass(step: number): string {
    console.log(`${step}`);
    if (step === 1) {
      return 'active';
    }
    if (step === 2) {
      return 'activated';
    } else {
      return '';
    }
  }

  selectStepThreeClass(step: number): string {
    if (step === 1) {
      return 'active';
    }
    if (step === 2) {
      return 'activated';
    }
    if (step === 3) {
      return 'activated';
    } else {
      return '';
    }
  }
}

