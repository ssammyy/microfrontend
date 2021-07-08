import {Component, OnInit} from '@angular/core';
import {Observable, Subject, throwError} from "rxjs";
import {
  Branches,
  BranchesService,
  County,
  CountyService,
  Go,
  loadBranchId,
  loadCompanyId,
  loadCountyId,
  loadResponsesFailure,
  Region,
  RegionService,
  selectCompanyIdData,
  selectCountyIdData,
  Town,
  TownService
} from "../../../../core/store";
import {Store} from "@ngrx/store";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-branches',
  templateUrl: './branches.list.html',
  styles: []
})
export class BranchesList implements OnInit {
  branches$: Observable<Branches[]>;
  filterName = '';
  p = 1;
  step = 1;

  stepTwoForm!: FormGroup;
  stepThreeForm!: FormGroup;

  branchSoFar: Partial<Branches> | undefined;
  // @ts-ignore
  branch: Branches;
  region$: Observable<Region[]>;
  county$: Observable<County[]>;
  town$: Observable<Town[]>;
  selectedRegion: number = 0;
  selectedCounty: number = 0;
  selectedTown: number = 0;
  selectedCompany: number = -1;
  submitted = false;

  dtTrigger: Subject<any> = new Subject<any>();
  dtOptions: DataTables.Settings = {
    pagingType: 'full_numbers',
    pageLength: 5,
    paging: true,
    processing: true
  };

  constructor(
    private service: BranchesService,
    private regionService: RegionService,
    private countyService: CountyService,
    private townService: TownService,
    private formBuilder: FormBuilder,
    private store$: Store<any>,
  ) {
    this.branches$ = service.entities$;
    service.getAll().subscribe();
    this.region$ = regionService.entities$;
    this.county$ = countyService.entities$;
    this.town$ = townService.entities$
    regionService.getAll().subscribe();
    countyService.getAll().subscribe();
    // townService.getAll().subscribe();

  }

  updateSelectedRegion() {
    this.selectedRegion = this.stepTwoForm?.get('region')?.value;
    console.log(`region set to ${this.selectedRegion}`)
  }

  updateSelectedCounty() {
    // console.log(`county set to ${this.selectedCounty}`)
    this.selectedCounty = this.stepTwoForm?.get('county')?.value;
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
    this.selectedTown = this.stepTwoForm?.get('town')?.value;
    // console.log(`town set to ${this.selectedTown}`)
  }

  ngOnInit(): void {
    this.stepTwoForm = new FormGroup({
      buildingName: new FormControl('', [Validators.required]),
      physicalAddress: new FormControl('', [Validators.required]),
      location: new FormControl(),
      postalAddress: new FormControl(),
      street: new FormControl('', [Validators.required]),
      plotNo: new FormControl('', [Validators.required]),
      nearestLandMark: new FormControl('', [Validators.required]),
      region: new FormControl('', [Validators.required]),
      county: new FormControl('', [Validators.required]),
      town: new FormControl('', [Validators.required]),
    });
    this.stepThreeForm = new FormGroup({
      companyProfileId: new FormControl(''),
      id: new FormControl(''),
      status: new FormControl('', [Validators.required]),
      descriptions: new FormControl('', [Validators.required]),
      contactPerson: new FormControl('', [Validators.required]),
      emailAddress: new FormControl('', [Validators.required]),
      telephone: new FormControl('', [Validators.required]),
      faxNo: new FormControl(''),
      designation: new FormControl('', [Validators.required])
    });

    this.store$.select(selectCompanyIdData).subscribe((d) => {
      return this.selectedCompany = d;
    })

  }

  editRecord(record: Branches) {
    this.stepTwoForm.patchValue(record);
    this.stepThreeForm.patchValue(record);
    this.branchSoFar = record;

  }

  get formStepTwoForm(): any {
    return this.stepTwoForm.controls;
  }

  get formStepThreeForm(): any {
    return this.stepThreeForm.controls;
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
          this.branchSoFar = {...this.branchSoFar, ...this.stepTwoForm?.value};
          break;
        case 2:
          this.branchSoFar = {...this.branchSoFar, ...this.stepThreeForm?.value};
          break;
      }
      this.step += 1;
    }
  }

  onClickSave(valid: boolean) {
    if (valid) {
      this.branchSoFar = {...this.branchSoFar, ...this.stepThreeForm.value};
      this.branch = {...this.branch, ...this.branchSoFar};
      // if (this.stepTwoForm?.get('id')?.value === null || this.stepTwoForm?.get('id')?.value === undefined) {
      if (this.branch.id === null || this.branch.id === undefined) {
        this.branch.companyProfileId = this.selectedCompany;
        this.service.add(this.branch);
      } else {
        this.service.update(this.branch);
      }
      this.step = 1;
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

  ngOnDestroy(): void {
    // Do not forget to unsubscribe the event
    this.dtTrigger.unsubscribe();
  }

  onClickUsers(record: Branches) {
    this.store$.dispatch(loadCompanyId({payload: record.companyProfileId}));
    this.store$.dispatch(loadBranchId({payload: record.id}));
    this.store$.dispatch(Go({payload: null, redirectUrl: '', link: 'dashboard/branches/users'}));

  }
}
