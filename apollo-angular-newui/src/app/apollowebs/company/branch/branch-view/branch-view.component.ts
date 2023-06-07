import {Component, OnInit} from '@angular/core';
import {Observable, of, Subject, throwError} from 'rxjs';
import {
  Branches,
  BranchesService,
  Company,
  County,
  CountyService, Go, loadBranchId, loadCompanyId, loadCountyId, loadResponsesFailure, loadResponsesSuccess,
  Region,
  RegionService, selectBranchData, selectCompanyData, selectCompanyIdData, selectCountyIdData,
  Town, TownService
} from '../../../../core/store';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {Store} from '@ngrx/store';
import {catchError} from 'rxjs/internal/operators/catchError';
import {HttpErrorResponse} from '@angular/common/http';

@Component({
  selector: 'app-branch-view',
  templateUrl: './branch-view.component.html',
  styleUrls: ['./branch-view.component.css']
})
export class BranchViewComponent implements OnInit {

  branches$: Observable<Branches[]>;
  filterName = '';
  p = 1;
  step = 1;

  stepTwoForm!: FormGroup;
  stepThreeForm!: FormGroup;

  branchSoFar: Partial<Branches> | undefined;
  // @ts-ignore
  branch: Branches;
  company$: Company | undefined;
  region$: Observable<Region[]>;
  county$: Observable<County[]>;
  town$: Observable<Town[]>;
  selectedRegion = 0;
  selectedCounty = 0;
  selectedTown = 0;
  selectedCompany = -1;
  submitted = false;

  dtTrigger: Subject<any> = new Subject<any>();

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
    this.town$ = townService.entities$;

    // townService.getAll().subscribe();

  }

  updateSelectedRegion() {
    this.selectedRegion = this.stepTwoForm?.get('region')?.value;
    console.log(`region set to ${this.selectedRegion}`);
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
          } else {
            return throwError('Invalid request, Company id is required');
          }
        }
    );
    // console.log(`Select town ID inside is ${this.branch.town}`);
    this.stepTwoForm?.get('town').setValue(this.branch.town);
  }

  updateSelectedTown() {
    this.selectedTown = this.stepTwoForm?.get('town')?.value;
    // console.log(`town set to ${this.selectedTown}`)
  }

  ngOnInit(): void {
    this.stepTwoForm = new FormGroup({
      branchName: new FormControl({value: '', disabled: true}, [Validators.required]),
      buildingName: new FormControl({value: '', disabled: true}, [Validators.required]),
      physicalAddress: new FormControl({value: '', disabled: true}, [Validators.required]),
      location: new FormControl({value: '', disabled: true}),
      postalAddress: new FormControl({value: '', disabled: true}),
      street: new FormControl({value: '', disabled: true}, [Validators.required]),
      plotNo: new FormControl({value: '', disabled: true}, [Validators.required]),
      nearestLandMark: new FormControl({value: '', disabled: true}, [Validators.required]),
      region: new FormControl({value: '', disabled: true}, [Validators.required]),
      county: new FormControl({value: '', disabled: true}, [Validators.required]),
      town: new FormControl({value: '', disabled: true}, [Validators.required]),
    });
    this.stepThreeForm = new FormGroup({
      companyProfileId: new FormControl(),
      id: new FormControl(),
      status: new FormControl({value: false, disabled: true}, [Validators.required]),
      descriptions: new FormControl({value: '', disabled: true}),
      contactPerson: new FormControl({value: '', disabled: true}, [Validators.required]),
      emailAddress: new FormControl({value: '', disabled: true}, [Validators.required]),
      telephone: new FormControl({value: '', disabled: true}, [Validators.required]),
      faxNo: new FormControl({value: '', disabled: true}),
      designation: new FormControl({value: '', disabled: true}, [Validators.required])
    });

    this.store$.select(selectCompanyIdData).subscribe((d) => {
      return this.selectedCompany = d;
    });

    this.store$.select(selectCompanyData).subscribe((d) => {
      return this.company$ = d;
    });

    this.store$.select(selectBranchData).subscribe((u) => {
      return this.branch = u;
    });
    this.stepTwoForm.patchValue(this.branch);
    this.stepThreeForm.patchValue(this.branch);
    console.log(`Select town ID inside is ${this.branch.town}`);

    this.regionService.getAll().subscribe();
    this.countyService.getAll().subscribe();

    this.selectedCounty = this.branch.county;
    this.store$.dispatch(loadCountyId({payload: this.branch.county}));
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

  editRecord(record: Branches) {
    this.stepTwoForm.patchValue(record);
    this.stepThreeForm.patchValue(record);
    this.branchSoFar = record;
    this.step = 1;

  }

  get formStepTwoForm(): any {
    return this.stepTwoForm.controls;
  }

  get formStepThreeForm(): any {
    return this.stepThreeForm.controls;
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
        this.branchSoFar = {...this.branchSoFar, ...this.stepTwoForm?.value};
        break;
      case 2:
        this.branchSoFar = {...this.branchSoFar, ...this.stepThreeForm?.value};
        break;
    }
    this.step += 1;
    // }
  }

  onClickSave(valid: boolean) {
    if (valid) {
      this.branchSoFar = {...this.branchSoFar, ...this.stepThreeForm.value};
      this.branch = {...this.branch, ...this.branchSoFar};
      this.branch.companyProfileId = this.selectedCompany;
      // if (this.stepTwoForm?.get('id')?.value === null || this.stepTwoForm?.get('id')?.value === undefined) {
      if (this.branch.id === null) {
        this.branch.companyProfileId = this.selectedCompany;
        this.service.add(this.branch).subscribe(
            (a) => {
              this.stepTwoForm.markAsPristine();
              this.stepTwoForm.reset();
              this.stepThreeForm.markAsPristine();
              this.stepThreeForm.reset();
              this.step = 0;
              return of(loadResponsesSuccess({
                message: {
                  response: '00',
                  payload: `Successfully saved ${a.buildingName}`,
                  status: 200
                }
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
      } else if (this.branch.id === undefined) {
        this.branch.companyProfileId = this.selectedCompany;
        this.service.add(this.branch).subscribe(
            (a) => {
              this.store$.dispatch(loadResponsesSuccess({
                message: {
                  response: '00',
                  payload: `Successfully saved ${a.buildingName}`,
                  status: 200
                }
              }));
              return this.store$.dispatch(Go({
                payload: null,
                link: 'company/companies/branches',
                redirectUrl: 'company/companies/branches'
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
        this.service.update(this.branch).subscribe(
            (a) => {
              this.store$.dispatch(loadResponsesSuccess({
                message: {
                  response: '00',
                  payload: `Successfully saved ${a.buildingName}`,
                  status: 200
                }
              }));
              return this.store$.dispatch(Go({
                payload: null,
                link: 'company/companies/branches',
                redirectUrl: 'company/companies/branches'
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


  onClickUsers(record: Branches) {
    this.store$.dispatch(loadCompanyId({payload: record.companyProfileId, company: this?.company$}));

    this.store$.dispatch(loadBranchId({payload: record.id, branch: record}));
    this.store$.dispatch(Go({payload: null, redirectUrl: '', link: 'company/branches/users'}));

  }

  public goBack(): void {
    // this.store$.dispatch(Back());
  }

  onClickAddBranch() {
    this.step = 1;
  }
}
