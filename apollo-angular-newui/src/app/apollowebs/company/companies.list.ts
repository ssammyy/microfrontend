import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {
    BusinessLines,
    BusinessLinesService, BusinessNatures, BusinessNaturesService,
    Company, CompanyService, County, CountyService,
    Go, loadCompanyId, loadResponsesFailure, loadResponsesSuccess, RegionService, Town, TownService,
} from '../../core/store';
import {Observable, of} from 'rxjs';
import {Store} from '@ngrx/store';
import {CompanyTurnOverUpdateDto, FirmTypeEntityDto} from '../../core/store/data/qa/qa.model';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {RegionsEntityDto} from '../../core/store/data/master/master.model';
import {QaService} from '../../core/store/data/qa/qa.service';
import {MsService} from '../../core/store/data/ms/ms.service';
import {NgxSpinnerService} from 'ngx-spinner';
import {catchError} from 'rxjs/operators';
import {HttpErrorResponse} from '@angular/common/http';

@Component({
    selector: 'app-companies',
    templateUrl: './companies.list.html',
    styles: [],
})
export class CompaniesList implements OnInit {
    @ViewChild('editModal') editModal !: TemplateRef<any>;
    companies$: Observable<Company[]>;
    businessLines$: Observable<BusinessLines[]>;
    businessNatures$: Observable<BusinessNatures[]>;
    filterName: string;
    p = 1;

    loadedFirmType: FirmTypeEntityDto[] = [];
    msRegions: RegionsEntityDto[] = null;
    msCountiesList: County[] = null;
    msTowns: Town[] = null;
    companySaveDetails: Company;

    submitted = false;
    currDivLabel!: string;
    currDiv!: string;
    companyDetailsForm!: FormGroup;
    updateTurnOverDetailsForm!: FormGroup;
    saveTurnOverDetails: CompanyTurnOverUpdateDto;

    selectedCounty = 0;
    selectedRegion = 0;
    selectedTown = 0;
    selectedBusinessLine = 0;
    selectedBusinessNature = 0;

    loading = false;
    loadingText: string;


    constructor(
        private qaService: QaService,
        private msService: MsService,
        private linesService: BusinessLinesService,
        private naturesService: BusinessNaturesService,
        private regionService: RegionService,
        private countyService: CountyService,
        private townService: TownService,
        private formBuilder: FormBuilder,
        private SpinnerService: NgxSpinnerService,
        private service: CompanyService,
        private store$: Store<any>,
    ) {
        this.filterName = '';
        this.businessNatures$ = naturesService.entities$;
        this.businessLines$ = linesService.entities$;
        // location.reload();
    }

    ngOnInit(): void {
        this.companies$ = this.service.entities$;
        this.service.clearCache();
        this.service.getAll().subscribe();

        this.naturesService.getAll().subscribe();
        this.linesService.getAll().subscribe();

        this.loadDataToUse();

        this.updateTurnOverDetailsForm = this.formBuilder.group({
            companyProfileID: ['', Validators.required],
            selectedFirmTypeID: ['', Validators.required],
        });

        this.companyDetailsForm = this.formBuilder.group({
            name: ['', Validators.required],
            registrationNumber: ['', Validators.required],
            kraPin: ['', Validators.required],
            yearlyTurnover: ['', Validators.required],
            directorIdNumber: ['', Validators.required],
            businessLines: ['', Validators.required],
            businessNatures: ['', Validators.required],
            otherBusinessNatureType: ['', Validators.required],
            postalAddress: ['', Validators.required],
            physicalAddress: ['', Validators.required],
            plotNumber: null,
            companyEmail: ['', Validators.required],
            companyTelephone: ['', Validators.required],
            buildingName: ['', Validators.required],
            streetName: ['', Validators.required],
            region: ['', Validators.required],
            county: ['', Validators.required],
            firmType: ['', Validators.required],
            town: ['', Validators.required],
        });
    }

    reloadCompanyDetails() {
        this.store$.dispatch(Go({
            payload: null,
            link: 'dashboard/companies',
            redirectUrl: 'dashboard/companies',
        }));
    }

    get formCompanyDetailsForm(): any {
        return this.companyDetailsForm.controls;
    }

    get formUpdateTurnOverDetailsForm(): any {
        return this.updateTurnOverDetailsForm.controls;
    }

    updateSelectedRegion() {
        this.selectedRegion = this.companyDetailsForm?.get('region')?.value;
        this.companyDetailsForm.controls.county.enable();
    }

    updateSelectedCounty() {
        this.selectedCounty = this.companyDetailsForm?.get('county')?.value;
        this.companyDetailsForm.controls.townsId.enable();
        this.msTowns = this.msTowns.filter(x => String(this.selectedCounty) === String(x.countyId));
        console.log(`towns list set to ${this.msTowns}`);
    }

    updateSelectedTown() {
        this.selectedTown = this.companyDetailsForm?.get('town')?.value;
    }

    updateSelectedBusinessLine() {
        this.selectedBusinessLine = this.companyDetailsForm?.get('businessLines')?.value;
    }

    updateSelectedBusinessNatures() {
        this.selectedBusinessNature = this.companyDetailsForm?.get('businessNatures')?.value;
    }

    loadDataToUse() {
        this.msService.msRegionListDetails().subscribe(
            (dataRegions: RegionsEntityDto[]) => {
                this.msRegions = dataRegions;
            },
            error => {
                this.SpinnerService.hide();
                console.log(error);
                this.msService.showError('AN ERROR OCCURRED');
            },
        );
        this.msService.msCountiesListDetails().subscribe(
            (dataCounties: County[]) => {
                this.msCountiesList = dataCounties;
            },
            error => {
                console.log(error);
                this.msService.showError('AN ERROR OCCURRED');
            },
        );
        this.msService.msTownsListDetails().subscribe(
            (dataTowns: Town[]) => {
                this.msTowns = dataTowns;
            },
            error => {
                console.log(error);
                this.msService.showError('AN ERROR OCCURRED');
            },
        );
        // this.qaService.loadFirmPermitList().subscribe(
        //     (dataFirmType: FirmTypeEntityDto[]) => {
        //         this.loadedFirmType = dataFirmType;
        //     },
        //     error => {
        //         console.log(error);
        //         this.qaService.showError('AN ERROR OCCURRED');
        //     },
        // );
        this.qaService.loadFirmPermitList().subscribe(
            (dataFirmType: FirmTypeEntityDto[]) => {
                this.loadedFirmType = dataFirmType;
            },
            error => {
                console.log(error);
                this.qaService.showError('AN ERROR OCCURRED');
            },
        );
    }

    // editRecord(record: Company) {
    //     this.store$.dispatch(loadCompanyId({payload: record.id, company: record}));
    //     this.store$.dispatch(Go({payload: null, redirectUrl: '', link: 'company'}));
    // }


    onClickPlantDetails(record: Company) {
        this.store$.dispatch(loadCompanyId({payload: record.id, company: record}));
        this.store$.dispatch(Go({payload: null, redirectUrl: '', link: 'company/branches'}));
    }

    onClickDirectors(record: Company) {
        this.store$.dispatch(loadCompanyId({payload: record.id, company: record}));
        this.store$.dispatch(Go({payload: null, redirectUrl: '', link: 'company/directors'}));
    }

    // viewRecord(record: Company) {
    //     this.store$.dispatch(loadCompanyId({payload: record.id, company: record}));
    //     this.store$.dispatch(Go({payload: null, redirectUrl: '', link: 'company/view'}));
    // }

    viewRecord(record: Company) {
        this.currDivLabel = `COMPANY DETAILS : ${record.name}`;
        this.currDiv = 'viewCompanyDetails';
        this.selectedRegion = record.region;
        this.companyDetailsForm.patchValue(record);
        this.companyDetailsForm.disable();
        window.$('#companyDetailsModal').modal('show');
    }

    editRecord(record: Company) {
        this.currDivLabel = `COMPANY DETAILS : ${record.name}`;
        this.currDiv = 'editCompanyDetails';
        this.selectedRegion = record.region;
        this.companyDetailsForm.patchValue(record);
        this.companyDetailsForm.disable();
        this.companyDetailsForm.get('kraPin').enable();
        window.$('#companyDetailsModal').modal('show');
    }


    updateTurnoverRecord(record: Company) {
        this.currDivLabel = `UPDATE FIRM TYPE DETAILS`;
        this.currDiv = 'updateTurnOver';
        this.updateTurnOverDetailsForm.reset();
        this.updateTurnOverDetailsForm?.get('companyProfileID')?.setValue(record.id);
        console.log('record to view' + record);
        window.$('#updateModal').modal('show');
    }

    onClickSaveFirmType(valid: boolean) {
        this.submitted = true;
        if (valid) {
            this.msService.showSuccessWith2Message('Are you sure your want to Save the details?', 'You won\'t be able to revert back after submission!',
                // tslint:disable-next-line:max-line-length
                `You can click \'Update Firm Type\' button to updated the Details before saving`, 'PDF SAVED SUCCESSFUL', () => {
                    this.saveFirmType(valid);
                });
        } else {
            this.msService.showError('FILL IN ALL REQUIRED FIELD AS HIGHLIGHTED');
        }
    }

    saveFirmType(valid: boolean) {
        if (valid) {
            this.SpinnerService.show();
            this.saveTurnOverDetails = {...this.saveTurnOverDetails, ...this.updateTurnOverDetailsForm.value};

            this.qaService.qaUpdateFirmType(this.saveTurnOverDetails).subscribe(
                (data: any) => {
                    this.SpinnerService.hide();
                    this.qaService.showSuccess('COMPANY FIRM TYPE UPDATED SUCCESSFULLY', () => {
                        this.reloadCompanyDetails();
                    });
                },
                error => {
                    this.SpinnerService.hide();
                    console.log(error);
                    this.qaService.showError('AN ERROR OCCURRED');
                },
            );
        }
    }

    onClickSaveCompanyDetails(valid: boolean) {
        this.submitted = true;
        if (valid) {
            this.msService.showSuccessWith2Message('Are you sure your want to Save the details?', 'You won\'t be able to revert back after submission!',
                // tslint:disable-next-line:max-line-length
                `You can click \'Edit Details\' button to updated the Details before saving`, 'PDF SAVED SUCCESSFUL', () => {
                    this.saveCompanyDetails(valid);
                });
        } else {
            this.msService.showError('FILL IN ALL REQUIRED FIELD AS HIGHLIGHTED');
        }
    }

    saveCompanyDetails(valid: boolean) {
        if (valid) {
            this.companySaveDetails = {...this.companySaveDetails, ...this.companyDetailsForm.value};

            this.service.update(this.companySaveDetails).subscribe(
                (a) => {
                    this.store$.dispatch(
                        loadResponsesSuccess({
                            message: {
                                response: '00',
                                payload: `Successfully saved ${a.name}`,
                                status: 200,
                            },
                        }),
                    );
                    return this.store$.dispatch(Go({
                        payload: null,
                        link: 'dashboard/companies',
                        redirectUrl: 'dashboard/companies',
                    }));


                },
                catchError(
                    (err: HttpErrorResponse) => {
                        return of(loadResponsesFailure({
                            error: {
                                payload: err.error,
                                status: err.status,
                                response: (err.error instanceof ErrorEvent) ? `Error: ${err.error.message}` : `Error Code: ${err.status},  Message: ${err.error}`,
                            },
                        }));
                    }));
        } else {
            this.store$.dispatch(loadResponsesFailure({
                error: {
                    payload: 'Some required details are missing, kindly recheck',
                    status: 100,
                    response: '05',
                },
            }));

        }

    }
}
