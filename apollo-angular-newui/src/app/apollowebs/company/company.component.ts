import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {
    BusinessLines,
    BusinessLinesService,
    BusinessNatures, BusinessNaturesService,
    Company,
    CompanyService,
    County, CountyService, loadCountyId, loadResponsesFailure, loadResponsesSuccess,
    Region, RegionService, selectCompanyData, selectCountyIdData,
    Town, TownService
} from '../../core/store';
import {Observable, of, throwError} from 'rxjs';
import {Store} from '@ngrx/store';
import {catchError, map} from 'rxjs/operators';
import {HttpErrorResponse} from '@angular/common/http';

@Component({
    selector: 'app-company',
    templateUrl: './company.component.html',
    styleUrls: ['./company.component.css']
})
export class CompanyComponent implements OnInit {

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
        this.store$.select(selectCompanyData).subscribe((d) => {
            console.log(`The id ${d.id}`);
            return this.company = d;
        });

        this.stepOneForm.patchValue(this.company);
        this.stepTwoForm.patchValue(this.company);
        this.stepThreeForm.patchValue(this.company);
        this.companySoFar = this.company;
    }

    updateSelectedRegion() {
        this.selectedRegion = this.stepThreeForm?.get('region')?.value;
        console.log(`region set to ${this.selectedRegion}`);
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
                } else {
                    return throwError('Invalid request, Company id is required');
                }
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
            }
            this.step += 1;
            console.log(`Clicked and step = ${this.step}`);
        }
    }

    onClickSave(valid: boolean) {
        if (valid) {
            this.companySoFar = {...this.companySoFar, ...this.stepThreeForm.value};
            this.company = {...this.company, ...this.companySoFar};

            this.service.update(this.company).pipe(
                map((a) => {
                    this.stepOneForm.markAsPristine();
                    this.stepOneForm.reset();
                    this.stepTwoForm.markAsPristine();
                    this.stepTwoForm.reset();
                    this.stepThreeForm.markAsPristine();
                    this.stepThreeForm.reset();
                    this.step = 0;
                    return of(loadResponsesSuccess({
                        message: {
                            response: '00',
                            payload: `Successfully saved ${a.name}`,
                            status: 200
                        }
                    }));
                }),
                catchError(
                    (err: HttpErrorResponse) => {
                        return of(loadResponsesFailure({
                            error: {
                                payload: err.error,
                                status: err.status,
                                response: (err.error instanceof ErrorEvent) ? `Error: ${err.error.message}` : `Error Code: ${err.status},  Message: ${err.error}`
                            }
                        }));
                    })).subscribe();
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
        }if (step === 2) {
            return 'activated';
        } else {
            return '';
        }
    }
    selectStepThreeClass(step: number): string {
        if (step === 1) {
            return 'active';
        }if (step === 2) {
            return 'activated';
        }if (step === 3) {
            return 'activated';
        } else {
            return '';
        }
    }
}