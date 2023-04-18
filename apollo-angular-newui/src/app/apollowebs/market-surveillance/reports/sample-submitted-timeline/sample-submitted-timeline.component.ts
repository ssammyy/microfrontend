import {Component, OnInit, QueryList, TemplateRef, ViewChild, ViewChildren} from '@angular/core';
import {Observable, Subject} from 'rxjs';
import {County, CountyService, selectUserInfo, Town, TownService} from '../../../../core/store';
import {
  AcknowledgementDto,
  ApiResponseModel, ConsumerComplaintsReportViewEntity,
  ConsumerComplaintViewSearchValues, MsDepartment, MsDivisionDetails, MsUsersDto,
  SampleSubmittedDto, SubmittedSamplesSummaryReportViewEntity, SubmittedSamplesSummaryViewSearchValues
} from '../../../../core/store/data/ms/ms.model';
import {LocalDataSource} from 'ng2-smart-table';
import {Store} from '@ngrx/store';
import {ActivatedRoute, Router} from '@angular/router';
import {FormBuilder, FormGroup} from '@angular/forms';
import {NgxSpinnerService} from 'ngx-spinner';
import {MsService} from '../../../../core/store/data/ms/ms.service';
import {DataTableDirective} from 'angular-datatables';
import {RegionsEntityDto} from '../../../../shared/models/master-data-details';

@Component({
  selector: 'app-sample-submitted-timeline',
  templateUrl: './sample-submitted-timeline.component.html',
  styleUrls: ['./sample-submitted-timeline.component.css'],
})
export class SampleSubmittedTimelineComponent implements OnInit {
  @ViewChild('editModal') editModal !: TemplateRef<any>;
  submitted = false;
  selectedCounty = 0;
  selectedTown = 0;
  departmentSelected: 0;
  selectedTownName: string;
  selectedCountyName: string;
  county$: Observable<County[]>;
  town$: Observable<Town[]>;
  loading = false;


  roles: string[];
  searchFormGroup!: FormGroup;
  dtOptions: DataTables.Settings = {};
  dtTrigger1: Subject<any> = new Subject<any>();
  dtTrigger2: Subject<any> = new Subject<any>();
  dtTrigger3: Subject<any> = new Subject<any>();
  @ViewChildren(DataTableDirective)
  dtElements: QueryList<DataTableDirective>;

  searchTypeValue = 'ACKNOWLEDGEMENT';
  endPointStatusValue = 'complaint';
  activeStatus = 'consumerComplaintsView';
  previousStatus = 'consumerComplaintsView';
  selectedBatchRefNo: string;
  searchStatus: any;
  message: any;
  personalTasks = 'false';
  defaultPageSize = 1000;
  defaultPage = 0;
  currentPage = 0;
  currentPageInternal = 0;
  submittedSamplesSummarySearchValues: SubmittedSamplesSummaryViewSearchValues;
  selectedNotification: AcknowledgementDto;
  loadedData: SubmittedSamplesSummaryReportViewEntity[] = [];
  msOfficerLists!: MsUsersDto[];
  msRegions: RegionsEntityDto[] = [];
  msDepartments: MsDepartment[] = [];
  msDivisions: MsDivisionDetails[] = [];
  totalCount = 12;
  dataSet: LocalDataSource = new LocalDataSource();
  search: Subject<string>;

  numberOfSamplesTested: number;
  averageTestingCompliance: number;
  sumOfComplianceToTesting: number;
  sumOfTCTimesB: number;
  sumOfTimeTakenToSubmitSamples: number;
  averageTimeTakenToSubmitSamples: number;
  sumOfSubmissionWithin2Days: number;
  percentageComplianceToSubmissionWithin2days: number;
  sumOfTimeTakenToForwardLetters: number;
  averageTimeTakenToForwardLetters: number;
  sumOfForwardingWithin14Days: number;
  percentageComplianceToForwardingWithin14Days: number;


  constructor(private store$: Store<any>,
              // private dialog: MatDialog,
              private activatedRoute: ActivatedRoute,
              private router: Router,
              private formBuilder: FormBuilder,
              private SpinnerService: NgxSpinnerService,
              private countyService: CountyService,
              private townService: TownService,
              private msService: MsService,
  ) {
    this.county$ = countyService.entities$;
    this.town$ = townService.entities$;
    countyService.getAll().subscribe();
    townService.getAll().subscribe();
  }

  ngOnInit(): void {

    this.dtOptions = {
      processing: true,
      dom: 'Bfrtip',
    };

    this.store$.select(selectUserInfo).pipe().subscribe((u) => {
      return this.roles = u.roles;
    });

    this.searchFormGroup = this.formBuilder.group({
      startDate: ['', null],
      endDate: ['', null],
      sampleReferences: ['', null],
      assignIO: ['', null],
      sectorID: ['', null],
    });

    this.loadData(this.defaultPage, this.defaultPageSize);
  }

  get formSearch(): any {
    return this.searchFormGroup.controls;
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
      this.dtTrigger2.next();
      this.dtTrigger3.next();
    });

  }

  private loadData(page: number, records: number): any {
    this.SpinnerService.show();
    const params = {'personal': this.personalTasks};
    this.msService.loadAllSubmittedSamplesSummaryReportList(String(page), String(records)).subscribe(
        (dataResponse: ApiResponseModel) => {
          if (dataResponse.responseCode === '00') {
            // console.log(dataResponse.data as ConsumerComplaintsReportViewEntity[]);
            this.loadedData = dataResponse?.data as SubmittedSamplesSummaryReportViewEntity[];
            this.totalCount = this.loadedData.length;
            this.calculateSampleSubmittedSummary();
            this.rerender();
            this.msService.msOfficerListDetails().subscribe(
                (dataOfficer: MsUsersDto[]) => {
                  this.msOfficerLists = dataOfficer;
                },
            );
            this.msService.msRegionListDetails().subscribe(
                (dataRegion: RegionsEntityDto[]) => {
                  this.msRegions = dataRegion;
                },
            );
            this.msService.msDepartmentListDetails().subscribe(
                (dataDep: MsDepartment[]) => {
                  this.msDepartments = dataDep;
                },
            );
            this.msService.msDivisionListDetails().subscribe(
                (dataDiv: MsDivisionDetails[]) => {
                  this.msDivisions = dataDiv;
                },
            );
          }
          this.SpinnerService.hide();
        },
        error => {
          this.SpinnerService.hide();
          console.log(error);
        },
    );
  }



  pageChange(pageIndex?: any) {
    if (pageIndex) {
      this.currentPageInternal = pageIndex - 1;
      this.currentPage = pageIndex;
      this.loadData(this.currentPageInternal, this.defaultPageSize);
    }
  }

  onSubmitSearch() {
    this.SpinnerService.show();
    this.submitted = true;
    this.submittedSamplesSummarySearchValues = this.searchFormGroup.value;
    // tslint:disable-next-line:max-line-length
    this.msService.loadSearchSubmittedSamplesSummaryViewList(String(this.defaultPage), String(this.defaultPageSize), this.submittedSamplesSummarySearchValues).subscribe(
        (data: ApiResponseModel) => {
          if (data.responseCode === '00') {
            this.loadedData = data.data;
            this.totalCount = this.loadedData.length;
            this.dataSet.load(this.loadedData);
            this.calculateSampleSubmittedSummary();
            this.rerender();
          }
          this.SpinnerService.hide();
        },
        error => {
          this.SpinnerService.hide();
          console.log(error);
        },
    );
  }

  clearSearch() {
    this.searchFormGroup.reset();
    this.submitted = false;
  }

  onChangeSelectedDepartment() {
    this.departmentSelected = this.searchFormGroup?.get('complaintDepartment')?.value;
  }

  toggleStatus(status: string): void {
    this.message = null;
    this.searchStatus = null;
    if (status !== this.activeStatus) {
      this.activeStatus = status;
      this.loadData(this.defaultPage, this.defaultPageSize);
      // this.loadData(this.defaultPage, this.defaultPageSize, this.endPointStatusValue, this.searchTypeValue);
    }
  }

  calculateSampleSubmittedSummary(){
      let arrayOfSamplesTested = [];
      let arrayOfComplianceTesting = [];
      let arrayOfTCXB = [];
      let arrayOfTimeTakenToSubmitSample = [];
      let arrayOfSubmittedWithin2Days = [];
      let arrayOfTimeTakenToForwardLetter = [];
      let arrayOfForwardingWithin14DaysOfTesting = [];

      for(let i=0; i < this.loadedData.length; i++){
        if(isNaN(Number(this.loadedData[i].noSamplesTested))){
          this.loadedData[i].noSamplesTested = '0'
        }
        arrayOfSamplesTested.push(Number(this.loadedData[i].noSamplesTested));

        if(this.loadedData[i].complianceTesting != '100'){
          this.loadedData[i].complianceTesting = '0'
        }
        arrayOfComplianceTesting.push(Number(this.loadedData[i].complianceTesting));

        if(isNaN(Number(this.loadedData[i].tcxb))){
          this.loadedData[i].tcxb = '0'
        }
        arrayOfTCXB.push(Number(this.loadedData[i].tcxb));

        if(isNaN(Number(this.loadedData[i].timeTakenSubmitSample))){
          arrayOfTimeTakenToSubmitSample.push(0);
        }else{
          arrayOfTimeTakenToSubmitSample.push(Number(this.loadedData[i].timeTakenSubmitSample)+1);
        }

        if(isNaN(Number(this.loadedData[i].submissionWithin2Days))){
          this.loadedData[i].submissionWithin2Days = '0'
        }
        arrayOfSubmittedWithin2Days.push(Number(this.loadedData[i].submissionWithin2Days));

        if(isNaN(Number(this.loadedData[i].timeTakenForwardLetters))){
          arrayOfTimeTakenToForwardLetter.push(0);
        }else{
          arrayOfTimeTakenToForwardLetter.push(Number(this.loadedData[i].timeTakenForwardLetters)+1);
        }

        if(isNaN(Number(this.loadedData[i].forwardingWithin14DaysTesting))){
          this.loadedData[i].forwardingWithin14DaysTesting = '0'
        }
        arrayOfForwardingWithin14DaysOfTesting.push(Number(this.loadedData[i].forwardingWithin14DaysTesting));

      }
      console.log("Array of submitted with 2 days"+arrayOfSubmittedWithin2Days);
      this.numberOfSamplesTested = arrayOfSamplesTested.reduce((a,b)=> a + b, 0);
      this.averageTestingCompliance = this.numberOfSamplesTested/this.loadedData.length;
      this.sumOfComplianceToTesting = arrayOfComplianceTesting.reduce((a,b)=> a + b, 0);
      this.sumOfTCTimesB = arrayOfTCXB.reduce((a,b)=> a + b, 0);
      this.sumOfTimeTakenToSubmitSamples = arrayOfTimeTakenToSubmitSample.reduce((a,b)=> a + b, 0);
      this.averageTimeTakenToSubmitSamples = this.sumOfTimeTakenToSubmitSamples/this.loadedData.length;
      this.sumOfSubmissionWithin2Days = arrayOfSubmittedWithin2Days.reduce((a,b)=> a + b, 0);
      console.log("Sum of submission within 2 days: "+this.sumOfSubmissionWithin2Days);
      this.percentageComplianceToSubmissionWithin2days = (this.sumOfSubmissionWithin2Days/this.loadedData.length)*100;
      this.sumOfTimeTakenToForwardLetters = arrayOfTimeTakenToForwardLetter.reduce((a,b)=> a + b, 0);
      this.averageTimeTakenToForwardLetters = this.sumOfTimeTakenToForwardLetters/this.loadedData.length;
      this.sumOfForwardingWithin14Days = arrayOfForwardingWithin14DaysOfTesting.reduce((a,b)=> a + b, 0);
      this.percentageComplianceToForwardingWithin14Days = (this.sumOfForwardingWithin14Days/this.loadedData.length)*100;
  }

}
