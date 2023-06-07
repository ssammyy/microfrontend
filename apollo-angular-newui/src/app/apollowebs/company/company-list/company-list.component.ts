import {Component, OnInit, QueryList, TemplateRef, ViewChild, ViewChildren} from '@angular/core';
import {Observable, Subject} from 'rxjs';
import {
  BusinessLines,
  BusinessLinesService,
  BusinessNatures, BusinessNaturesService,
  Company,
  CompanyService,
  County, CountyService,
  Go,
  loadCompanyId, RegionService,
  Town, TownService,
} from '../../../core/store';
import {Store} from '@ngrx/store';
import {QaService} from '../../../core/store/data/qa/qa.service';
import {ComplaintsListDto, SampleSubmissionDto} from '../../../core/store/data/ms/ms.model';
import {NgxSpinnerService} from 'ngx-spinner';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {RegionsEntityDto} from '../../../core/store/data/master/master.model';
import {MsService} from '../../../core/store/data/ms/ms.service';
import {CompanyTurnOverUpdateDto, FirmTypeEntityDto} from '../../../core/store/data/qa/qa.model';
import {DataTableDirective} from 'angular-datatables';

@Component({
  selector: 'app-company-list',
  templateUrl: './company-list.component.html',
  styleUrls: ['./company-list.component.css'],
})
export class CompanyListComponent implements OnInit {
  @ViewChild('editModal') editModal !: TemplateRef<any>;
  // companies$: Observable<Company[]>;
  businessLines$: Observable<BusinessLines[]>;
  businessNatures$: Observable<BusinessNatures[]>;
  loadedData: Company[] = [];
  loadedFirmType: FirmTypeEntityDto[] = [];
  submitted = false;
  currDivLabel!: string;
  currDiv!: string;
  companyDetailsForm!: FormGroup;
  updateTurnOverDetailsForm!: FormGroup;
  filterName: string;
  saveTurnOverDetails: CompanyTurnOverUpdateDto;
  p = 1;

  selectedCounty = 0;
  selectedRegion = 0;
  selectedTown = 0;
  selectedBusinessLine = 0;
  selectedBusinessNature = 0;

  msRegions: RegionsEntityDto[] = null;
  msCountiesList: County[] = null;
  msTowns: Town[] = null;
  msTownsOriginal: Town[] = [];
  loading = false;
  loadingText: string;

  dtOptions: DataTables.Settings = {};
  dtTrigger1: Subject<any> = new Subject<any>();
  @ViewChildren(DataTableDirective)
  dtElements: QueryList<DataTableDirective>;


  constructor(
      private service: QaService,
      private msService: MsService,
      private linesService: BusinessLinesService,
      private naturesService: BusinessNaturesService,
      private regionService: RegionService,
      private countyService: CountyService,
      private townService: TownService,
      private formBuilder: FormBuilder,
      private SpinnerService: NgxSpinnerService,
      private store$: Store<any>,

  ) {
    this.filterName = '';
    this.businessNatures$ = naturesService.entities$;
    this.businessLines$ = linesService.entities$;
    // location.reload();
  }

  ngOnInit(): void {

    this.dtOptions = {
      processing: true,
      dom: 'Bfrtip',
    };

    // this.companies$ = this.service.entities$;
    // this.service.clearCache();
    this.loadCompanies();
    this.loadDataToUse();

    this.regionService.getAll().subscribe();
    this.countyService.getAll().subscribe();
    // townService.getAll().subscribe();
    this.naturesService.getAll().subscribe();
    this.linesService.getAll().subscribe();

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

  rerender(): void {
    this.dtElements.forEach((dtElement: DataTableDirective) => {
      if (dtElement.dtInstance) {
        dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
          dtInstance.destroy();
        });
      }
    });
    setTimeout(() => {
      this.dtTrigger1.next();
    });

  }

  // tslint:disable-next-line:use-life-cycle-interface
  ngOnDestroy(): void {
    // Do not forget to unsubscribe the event
    this.dtTrigger1.unsubscribe();
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
    this.service.loadFirmPermitList().subscribe(
        (dataFirmType: FirmTypeEntityDto[]) => {
          this.loadedFirmType = dataFirmType;
        },
        error => {
          console.log(error);
          this.service.showError('AN ERROR OCCURRED');
        },
    );
  }

  loadCompanies() {
    this.loading= true;
    this.loadingText = "Retrieving Please Wait ...."
    this.SpinnerService.show();
    this.service.loadAllCompanyList().subscribe(
        (data) => {
          this.loadedData = data;
          this.rerender();
          this.SpinnerService.hide();
        },
        error => {
          this.SpinnerService.hide();
          console.log(error);
          this.service.showError('AN ERROR OCCURRED');
        },
    );
  }


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

  onClickSaveFirmType(valid: boolean) {
    this.submitted = true;
    if (valid) {
      this.msService.showSuccessWith2Message('Are you sure your want to Save the details?', 'You won\'t be able to revert back after submission!',
          // tslint:disable-next-line:max-line-length
          `You can click \'Update Firm Type\' button to updated the Details before saving`, 'PDF SAVED SUCCESSFUL', () => {
            this.saveFirmType(valid);
          });
    } else {
      this.service.showError('FILL IN ALL REQUIRED FIELD AS HIGHLIGHTED');
    }
  }

  saveFirmType(valid: boolean) {
    if (valid) {
      this.SpinnerService.show();
      this.saveTurnOverDetails = {...this.saveTurnOverDetails, ...this.updateTurnOverDetailsForm.value};

      this.service.qaUpdateFirmType(this.saveTurnOverDetails).subscribe(
          (data: any) => {
            this.SpinnerService.hide();
            this.service.showSuccess('COMPANY FIRM TYPE UPDATED SUCCESSFULLY', () => {
              this.loadCompanies();
            });
          },
          error => {
            this.SpinnerService.hide();
            console.log(error);
            this.service.showError('AN ERROR OCCURRED');
          },
      );
    }
  }


}
