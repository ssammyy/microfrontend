import {Component, OnInit, QueryList, TemplateRef, ViewChild, ViewChildren} from '@angular/core';
import {Observable, Subject} from 'rxjs';
import {County, CountyService, Region, selectUserInfo, Town, TownService} from '../../../../core/store';
import {UserNotificationDetailsDto} from '../../../../core/store/data/master/master.model';
import {LocalDataSource} from 'ng2-smart-table';
import {Store} from '@ngrx/store';
import {ActivatedRoute, Router} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {NgxSpinnerService} from 'ngx-spinner';
import {MsService} from '../../../../core/store/data/ms/ms.service';
import {
  AcknowledgementDto,
  ApiResponseModel,
  ComplaintsInvestigationListDto, ComplaintViewSearchValues, ConsumerComplaintsReportViewEntity, ConsumerComplaintViewSearchValues,
  MsDepartment, MsDivisionDetails,
  MsUsersDto,
} from '../../../../core/store/data/ms/ms.model';
import {RegionsEntityDto} from '../../../../shared/models/master-data-details';
import {DataTableDirective} from 'angular-datatables';

@Component({
  selector: 'app-acknowledgement',
  templateUrl: './acknowledgement.component.html',
  styleUrls: ['./acknowledgement.component.css'],
})
export class AcknowledgementComponent implements OnInit {
  @ViewChild('editModal') editModal !: TemplateRef<any>;
  submitted = false;
  isEngineeringSelected: boolean = true;
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
  complaintViewSearchValues: ConsumerComplaintViewSearchValues;
  selectedNotification: AcknowledgementDto;
  loadedData: ConsumerComplaintsReportViewEntity[] = [];
  msOfficerLists!: MsUsersDto[];
  msRegions: RegionsEntityDto[] = [];
  msDepartments: MsDepartment[] = [];
  msDivisions: MsDivisionDetails[] = [];
  totalCount = 12;
  dataSet: LocalDataSource = new LocalDataSource();
  search: Subject<string>;
  complaintsReceived: number;
  complaintsClosed: number;
  percentageComplaintsClosed: number;
  noFeedbackWithin5days: number;
  percentageComplianceWithTimelines: number;
  averageFeedbackTimeDays: number;
  sumOfDaysTakenToProvideFeedback: number;
  noComplaintsAddressedWithin28Days: number;
  percentageComplianceTo28Days: number;
  averageTimeToAddressComplaints: number;
  sumOfTimeTakenToAddressComplaints: number;
  alphanumericString = '/^[0-9a-zA-Z]+$/';

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
      refNumber: ['', null],
      assignIO: ['', null],
      sectorID: ['', null],
      regionID: ['', null],
      departmentID: ['', null],
      // selectedOfficers: [[]],
      // selectedDivisions: [[]],
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
    this.msService.loadAllConsumerComplaintReportList(String(page), String(records)).subscribe(
        (dataResponse: ApiResponseModel) => {
          if (dataResponse.responseCode === '00') {
            // console.log(dataResponse.data as ConsumerComplaintsReportViewEntity[]);
            this.loadedData = dataResponse?.data as ConsumerComplaintsReportViewEntity[];
            for(let i=0; i<this.loadedData.length; i++){
              if(Number(this.loadedData[i].resolution) == 1){
                this.loadedData[i].resolution = 'RESOLVED';
              }else if(Number(this.loadedData[i].resolution) == 0){
                this.loadedData[i].resolution = 'NOT YET RESOLVED';
              }
            }
            this.totalCount = this.loadedData.length;
            this.rerender();
            this.calculateSummary();
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
    this.complaintViewSearchValues = this.searchFormGroup.value;
    // console.log('Data type of selectedOfficers:', typeof this.searchFormGroup.get('selectedOfficers').value);
    // tslint:disable-next-line:max-line-length
    this.msService.loadSearchConsumerComplaintViewList(String(this.defaultPage), String(this.defaultPageSize), this.complaintViewSearchValues).subscribe(
        (data: ApiResponseModel) => {
          if (data.responseCode === '00') {
            this.loadedData = data.data;
            for(let i=0; i<this.loadedData.length; i++){
              if(Number(this.loadedData[i].resolution) == 1){
                this.loadedData[i].resolution = 'RESOLVED';
              }else if(Number(this.loadedData[i].resolution) == 0){
                this.loadedData[i].resolution = 'NOT YET RESOLVED';
              }
            }
            this.totalCount = this.loadedData.length;
            this.dataSet.load(this.loadedData);
            this.calculateSummary();
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

  filterDivision(event){
    //console.log(event.target.value);
    if(event.target.value == 3){
      this.isEngineeringSelected = true;
    }else if(event.target.value == 4){
      this.isEngineeringSelected = false;
    }
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

  calculateSummary(){
    this.complaintsReceived = this.loadedData.length;
    let resolutionSum = 0;
    let count = 0;
    let arrayOfFeedbackWithin5days = [];
    let arrayOfNumberOfComplaintsAddressedWithin28days = [];
    let arrayOfDaysTakenToAddressComplaints = [];
    for(let i=0; i < this.loadedData.length; i++){
      if(this.loadedData[i].resolution == 'RESOLVED'){
        resolutionSum += 1;
      }else {
        resolutionSum += 0;
      }
      this.noFeedbackWithin5days += Number(this.loadedData[i].feedbackWithin5DaysCompInvestigation);
      arrayOfFeedbackWithin5days.push(Number(this.loadedData[i].feedbackWithin5DaysCompInvestigation));
      if(this.loadedData[i].resolution == 'RESOLVED'){
        arrayOfNumberOfComplaintsAddressedWithin28days.push(Number(this.loadedData[i].addressedWithin28DaysReceipt));
      }

      if(isNaN(Number(this.loadedData[i].timeTakenAddressComplaint))){
        this.loadedData[i].timeTakenAddressComplaint = '0';
        arrayOfDaysTakenToAddressComplaints.push((Number(this.loadedData[i].timeTakenAddressComplaint)));
      }else{
        arrayOfDaysTakenToAddressComplaints.push((Number(this.loadedData[i].timeTakenAddressComplaint)));
      }
      count++;
    }
    //console.log(arrayOfFeedbackWithin5days);
    this.sumOfDaysTakenToProvideFeedback = arrayOfFeedbackWithin5days.reduce((a,b)=> a + b, 0);
    //console.log("Sum of Days taken to give feedback: "+this.sumOfDaysTakenToProvideFeedback);
    this.noComplaintsAddressedWithin28Days = arrayOfNumberOfComplaintsAddressedWithin28days.reduce((a,b)=> a + b, 0);
    // console.log("Array of complaints addressed within 28 days"+arrayOfNumberOfComplaintsAddressedWithin28days);
    // console.log("Sum of Complaints addressed within 28 days: "+this.noComplaintsAddressedWithin28Days);
    this.sumOfTimeTakenToAddressComplaints = arrayOfDaysTakenToAddressComplaints.reduce((a,b)=> a + b, 0);
    // console.log("Array of Days Taken to address complains"+arrayOfDaysTakenToAddressComplaints);
    // console.log("Sum of number of days taken to address complains: "+this.sumOfTimeTakenToAddressComplaints);
    // console.log("Count "+ count);
    this.complaintsClosed = resolutionSum;
    this.percentageComplaintsClosed = (this.complaintsClosed/this.complaintsReceived)*100

    this.percentageComplianceWithTimelines = (this.sumOfDaysTakenToProvideFeedback/this.complaintsClosed)*100
    this.averageFeedbackTimeDays = this.sumOfDaysTakenToProvideFeedback/this.complaintsReceived

    this.percentageComplianceTo28Days = (this.noComplaintsAddressedWithin28Days/this.complaintsClosed)*100
    this.averageTimeToAddressComplaints = this.sumOfTimeTakenToAddressComplaints/this.complaintsReceived

  }

}
