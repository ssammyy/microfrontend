import {Component, OnInit} from '@angular/core';
import {
  BusinessLines,
  BusinessLinesService,
  BusinessNatures,
  BusinessNaturesService,
  Company,
  CompanyService,
  County,
  CountyService,
  Go,
  loadCompanyId,
  loadCountyId,
  loadResponsesFailure,
  Region,
  RegionService,
  selectCountyIdData,
  Town,
  TownService
} from "../../../core/store";
import {Observable, Subject, throwError} from "rxjs";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Store} from "@ngrx/store";


@Component({
  selector: 'app-companies',
  templateUrl: './companies.list.html',
  styles: []
})
export class CompaniesList implements OnInit {
  companies$: Observable<Company[]>;
  filterName: string;
  p = 1;
  step = 1;

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
  selectedBusinessLine: number = 0;
  selectedBusinessNature: number = 0;
  selectedRegion: number = 0;
  selectedCounty: number = 0;
  selectedTown: number = 0;
  dtTrigger: Subject<any> = new Subject<any>();
  dtOptions: DataTables.Settings = {
    pagingType: 'full_numbers',
    pageLength: 5,
    paging: true,
    processing: true
  };

  constructor(
    private service: CompanyService,
    private linesService: BusinessLinesService,
    private naturesService: BusinessNaturesService,
    private regionService: RegionService,
    private countyService: CountyService,
    private townService: TownService,
    private store$: Store<any>,
  ) {
    this.filterName = '';
    this.companies$ = service.entities$;
    service.getAll().subscribe();

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
  }

  updateSelectedRegion() {
    this.selectedRegion = this.stepThreeForm?.get('region')?.value;
    console.log(`region set to ${this.selectedRegion}`)
  }

  updateSelectedCounty() {

    this.selectedCounty = this.stepThreeForm?.get('county')?.value;
    // console.log(`county set to ${this.selectedCounty}`)
    this.store$.dispatch(loadCountyId({payload: this.selectedCounty}));
    this.store$.select(selectCountyIdData).subscribe(
      (d) => {
        if (d) {
          // console.log(`Select county inside is ${d}`);
          return this.townService.getAll();
        } else return throwError('Invalid request, Company id is required');
      }
    );
  }

  updateSelectedTown() {
    this.selectedTown = this.stepThreeForm?.get('town')?.value;
    // console.log(`town set to ${this.selectedTown}`)
  }

  updateSelectedBusinessLine() {
    this.selectedBusinessLine = this.stepOneForm?.get('businessLines')?.value;
  }

  updateSelectedBusinessNatures() {
    this.selectedBusinessNature = this.stepOneForm?.get('businessNatures')?.value;
  }

  editRecord(record: Company) {
    this.stepOneForm.patchValue(record);
    this.stepTwoForm.patchValue(record);
    this.stepThreeForm.patchValue(record);
    this.companySoFar = record;

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
      }
      this.step += 1;
    }
  }

  onClickSave(valid: boolean) {
    if (valid) {
      this.companySoFar = {...this.companySoFar, ...this.stepThreeForm.value};
      this.company = {...this.company, ...this.companySoFar};

      this.service.update(this.company);
      this.step = 1;
      this.stepOneForm.markAsPristine();
      this.stepOneForm.reset();
      this.stepTwoForm.markAsPristine();
      this.stepTwoForm.reset();
      this.stepThreeForm.markAsPristine();
      this.stepThreeForm.reset();

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

  onClickPlantDetails(record: Company) {
    this.store$.dispatch(loadCompanyId({payload: record.id}));
    this.store$.dispatch(Go({payload: null, redirectUrl: '', link: 'dashboard/branches'}));
  }

  onClickDirectors(record: Company) {
    this.store$.dispatch(loadCompanyId({payload: record.id}));
    this.store$.dispatch(Go({payload: null, redirectUrl: '', link: 'dashboard/directors'}));
  }

  onClickClose() {
    this.store$.dispatch(loadCompanyId({payload: -1}));
    this.store$.dispatch(Go({payload: null, redirectUrl: '', link: 'dashboard'}));
  }

  ngOnDestroy(): void {
    // Do not forget to unsubscribe the event
    this.dtTrigger.unsubscribe();
  }
}
