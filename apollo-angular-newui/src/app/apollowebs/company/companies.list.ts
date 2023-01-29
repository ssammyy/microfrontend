import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {
    BusinessLinesService, BusinessNaturesService,
    Company, CompanyService, County, CountyService,
    Go, loadCompanyId, RegionService, Town, TownService
} from '../../core/store';
import {Observable} from 'rxjs';
import {Store} from '@ngrx/store';
import {CompanyTurnOverUpdateDto, FirmTypeEntityDto} from '../../core/store/data/qa/qa.model';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {RegionsEntityDto} from '../../core/store/data/master/master.model';
import {QaService} from '../../core/store/data/qa/qa.service';
import {MsService} from '../../core/store/data/ms/ms.service';
import {NgxSpinnerService} from 'ngx-spinner';

@Component({
    selector: 'app-companies',
    templateUrl: './companies.list.html',
    styles: []
})
export class CompaniesList implements OnInit {
    @ViewChild('editModal') editModal !: TemplateRef<any>;
    companies$: Observable<Company[]>;
    filterName: string;
    p = 1;

    loadedFirmType: FirmTypeEntityDto[] = [];
    msRegions: RegionsEntityDto[] = null;
    msCountiesList: County[] = null;
    msTowns: Town[] = null;

    submitted = false;
    currDivLabel!: string;
    currDiv!: string;
    companyDetailsForm!: FormGroup;
    updateTurnOverDetailsForm!: FormGroup;
    saveTurnOverDetails: CompanyTurnOverUpdateDto;

    selectedRegion = 0;
    selectedTown = 0;
    selectedBusinessLine = 0;
    selectedBusinessNature = 0;
    

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
        // location.reload();
    }

    ngOnInit(): void {
        this.companies$ = this.service.entities$;
        this.service.clearCache();
        this.service.getAll().subscribe();

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
            plotNumber: ['', Validators.required],
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

    get formCompanyDetailsForm(): any {
        return this.companyDetailsForm.controls;
    }

    get formUpdateTurnOverDetailsForm(): any {
        return this.updateTurnOverDetailsForm.controls;
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

    editRecord(record: Company) {
        this.store$.dispatch(loadCompanyId({payload: record.id, company: record}));
        this.store$.dispatch(Go({payload: null, redirectUrl: '', link: 'company'}));
    }


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
        this.currDivLabel = `COMPANY DETAILS`;
        this.currDiv = 'viewCompanyDetails';
        this.selectedRegion = record.region;
        this.companyDetailsForm.patchValue(record);
        this.companyDetailsForm.disable();
        console.log('record to view' + record);
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
}
