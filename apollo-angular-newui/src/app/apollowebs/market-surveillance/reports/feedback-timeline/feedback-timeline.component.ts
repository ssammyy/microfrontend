import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {Observable, Subject} from 'rxjs';
import {County, CountyService, selectUserInfo, Town, TownService} from '../../../../core/store';
import {
  AcknowledgementDto,
  ApiResponseModel,
  ComplaintsInvestigationListDto,
  ComplaintViewSearchValues,
  FeedbackDto, MsDepartment, MsDivisionDetails, MsUsersDto,
} from '../../../../core/store/data/ms/ms.model';
import {LocalDataSource} from 'ng2-smart-table';
import {Store} from '@ngrx/store';
import {ActivatedRoute, Router} from '@angular/router';
import {FormBuilder, FormGroup} from '@angular/forms';
import {NgxSpinnerService} from 'ngx-spinner';
import {MsService} from '../../../../core/store/data/ms/ms.service';
import {RegionsEntityDto} from '../../../../shared/models/master-data-details';

@Component({
  selector: 'app-feedback-timeline',
  templateUrl: './feedback-timeline.component.html',
  styleUrls: ['./feedback-timeline.component.css'],
})
export class FeedbackTimelineComponent implements OnInit {
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

  searchTypeValue = 'ALL_DETAILS';
  endPointStatusValue = 'complaint';
  activeStatus = 'complaints-investigations';
  previousStatus = 'complaints-investigations';
  selectedBatchRefNo: string;
  searchStatus: any;
  message: any;
  personalTasks = 'false';
  defaultPageSize = 10;
  defaultPage = 0;
  currentPage = 0;
  currentPageInternal = 0;
  complaintViewSearchValues: ComplaintViewSearchValues;
  selectedNotification: AcknowledgementDto;
  loadedData!: ComplaintsInvestigationListDto[];
  msOfficerLists!: MsUsersDto[];
  msRegions: RegionsEntityDto[] = [];
  msDepartments: MsDepartment[] = [];
  msDivisions: MsDivisionDetails[] = [];
  totalCount = 12;
  public settingsComplaintInvestigation = {
    selectMode: 'single',  // single|multi
    hideHeader: false,
    hideSubHeader: false,
    actions: {
      columnTitle: 'Actions',
      add: false,
      edit: false,
      delete: false,
      // custom: [
      //   //  { name: 'editRecord', title: '<i class="btn btn-sm btn-primary">View More</i>' },
      //   // {name: 'viewRecord', title: '<i class="btn btn-sm btn-primary" >View Message</i>'},
      // ],
      // position: 'right', // left|right
    },
    delete: {
      deleteButtonContent: '&nbsp;&nbsp;<i class="fa fa-trash-o text-danger"></i>',
      confirmDelete: true,
    },
    noDataMessage: 'No data found',
    columns: {
      referenceNumber: {
        title: 'REFERENCE NUMBER',
        type: 'string',
        filter: false,
      },
      complaintTitle: {
        title: 'COMPLAINT TITLE',
        type: 'string',
        filter: false,
      },
      targetedProducts: {
        title: 'TARGETED PRODUCTS',
        type: 'string',
        filter: false,
      },
      transactionDate: {
        title: 'TRANSACTION DATE',
        type: 'date',
        filter: false,
      },
      assignedIo: {
        title: 'OFFICER NAME',
        type: 'string',
        filter: false,
      },
      region: {
        title: 'REGION',
        type: 'string',
        filter: false,
      },
      complaintDepartment: {
        title: 'DEPARTMENT',
        type: 'string',
        filter: false,
      },
      division: {
        title: 'FUNCTION',
        type: 'string',
        filter: false,
      },
      msProcess: {
        title: 'STATUS',
        type: 'string',
        filter: false,
      },
      // timeTakenForAcknowledgement: {
      //   title: 'TIME TAKEN',
      //   type: 'string',
      //   filter: false,
      // },
    },
    pager: {
      display: true,
      perPage: 10,
    },
  };
  dataSet: LocalDataSource = new LocalDataSource();
  search: Subject<string>;

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

    this.store$.select(selectUserInfo).pipe().subscribe((u) => {
      return this.roles = u.roles;
    });

    this.searchFormGroup = this.formBuilder.group({
      refNumber: ['', null],
      assignedIo: ['', null],
      region: ['', null],
      complaintDepartment: ['', null],
      division: ['', null],
    });

    this.loadData(this.defaultPage, this.defaultPageSize, this.endPointStatusValue, this.searchTypeValue);
  }

  get formSearch(): any {
    return this.searchFormGroup.controls;
  }

  private loadData(page: number, records: number, routeTake: string, searchType: string): any {
    this.SpinnerService.show();
    const params = {'personal': this.personalTasks};
    this.msService.loadAllComplaintTimelineList(String(page), String(records), routeTake, searchType).subscribe(
        (data: ApiResponseModel) => {
          if (data.responseCode === '00') {
            this.loadedData = data.data;
            this.totalCount = this.loadedData.length;
            this.dataSet.load(this.loadedData);
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
      this.loadData(this.currentPageInternal, this.defaultPageSize, this.endPointStatusValue, this.searchTypeValue);
    }
  }

  onSubmitSearch() {
    this.SpinnerService.show();
    this.submitted = true;
    this.complaintViewSearchValues = this.searchFormGroup.value;
    // tslint:disable-next-line:max-line-length
    this.msService.loadSearchClaimViewList(String(this.defaultPage), String(this.defaultPageSize), this.complaintViewSearchValues, this.searchTypeValue).subscribe(
        (data: ApiResponseModel) => {
          if (data.responseCode === '00') {
            this.loadedData = data.data;
            this.totalCount = this.loadedData.length;
            this.dataSet.load(this.loadedData);
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

  toggleStatus(status: string, endPointStatus: string, searchType: string): void {
    this.message = null;
    this.searchStatus = null;
    if (status !== this.activeStatus) {
      this.activeStatus = status;
      this.endPointStatusValue = endPointStatus;
      this.searchTypeValue = searchType;
      // this.loadData(this.defaultPage, this.defaultPageSize);
      this.loadData(this.defaultPage, this.defaultPageSize, this.endPointStatusValue, this.searchTypeValue);
    }
  }
}
