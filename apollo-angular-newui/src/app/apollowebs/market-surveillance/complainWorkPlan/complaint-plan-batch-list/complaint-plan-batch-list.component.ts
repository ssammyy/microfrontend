import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {
  ApiResponseModel,
  BatchFileFuelSaveDto,
  FuelBatchDetailsDto,
  WorkPlanBatchDetailsDto
} from '../../../../core/store/data/ms/ms.model';
import {Observable, Subject} from 'rxjs';
import {County, CountyService, selectUserInfo, Town, TownService} from '../../../../core/store';
import {LocalDataSource} from 'ng2-smart-table';
import {Store} from '@ngrx/store';
import {Router} from '@angular/router';
import {NgxSpinnerService} from 'ngx-spinner';
import {MsService} from '../../../../core/store/data/ms/ms.service';

@Component({
  selector: 'app-complaint-plan-batch-list',
  templateUrl: './complaint-plan-batch-list.component.html',
  styleUrls: ['./complaint-plan-batch-list.component.css']
})
export class ComplaintPlanBatchListComponent implements OnInit {
  @ViewChild('editModal') editModal !: TemplateRef<any>;
  currDiv!: string;
  currDivLabel!: string;
  addNewBatchForm!: FormGroup;
  dataSave: BatchFileFuelSaveDto;
  submitted = false;
  selectedCounty = 0;
  selectedTown = 0;
  county$: Observable<County[]>;
  town$: Observable<Town[]>;
  loading = false;

  roles: string[];

  activeStatus = 'all-workPlan-batch';
  previousStatus = 'all-workPlan-batch';
  searchStatus: any;
  personalTasks = 'false';
  defaultPageSize = 10;
  defaultPage = 0;
  currentPage = 0;
  currentPageInternal = 0;
  totalCount = 20;
  public settings = {
    selectMode: 'single',  // single|multi
    hideHeader: false,
    hideSubHeader: false,
    actions: {
      columnTitle: 'Actions',
      add: false,
      edit: false,
      delete: false,
      custom: [
        //  { name: 'editRecord', title: '<i class="btn btn-sm btn-primary">View More</i>' },
        {name: 'viewRecord', title: '<i class="btn btn-sm btn-primary" >View More</i>'},
      ],
      position: 'right', // left|right
    },
    delete: {
      deleteButtonContent: '&nbsp;&nbsp;<i class="fa fa-trash-o text-danger"></i>',
      confirmDelete: true,
    },
    noDataMessage: 'No data found',
    columns: {
      // id: {
      //   title: 'ID',
      //   type: 'string',
      //   filter: false
      // },
      referenceNumber: {
        title: 'REFERENCE NUMBER',
        type: 'string',
        filter: true,
      },
      yearName: {
        title: 'YEAR NAME',
        type: 'string',
        filter: true,
      },
      userCreated: {
        title: 'CREATED BY',
        type: 'string',
        filter: true,
      },
      createdDate: {
        title: 'CREATED DATE',
        type: 'date',
        filter: false,
      },
      endedDate: {
        title: 'ENDED DATE',
        type: 'date',
        filter: false,
      },
      // workPlanStatus: {
      //   title: 'APPROVAL STATUS',
      //   type: 'boolean',
      //   filter: true,
      // },
    },
    pager: {
      display: true,
      perPage: 20,
    },
  };
  dataSet: LocalDataSource = new LocalDataSource();
  documentTypes: any[];
  message: any;
  keywords: any;
  private documentTypeUuid: string;
  private documentTypeId: any;
  epraUser = false;
  managerPetroleumUser = false;
  ioUser = false;
  search: Subject<string>;
  loadedData: WorkPlanBatchDetailsDto[] = [];


  constructor(
      private store$: Store<any>,
      // private dialog: MatDialog,
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
  }

  ngOnInit(): void {
    this.store$.select(selectUserInfo).pipe().subscribe((u) => {
      return this.roles = u.roles;
    });
    this.loadData(this.defaultPage, this.defaultPageSize, this.activeStatus);
  }

  private loadData(page: number, records: number, routeTake: string): any {
    this.SpinnerService.show();
    this.msService.loadMSWorkPlanBatchList(String(page), String(records), routeTake, 'true').subscribe(
        (dataResponse: ApiResponseModel) => {
          if (dataResponse.responseCode === '00') {
            // console.log(dataResponse.data as ConsumerComplaintsReportViewEntity[]);
            this.loadedData = dataResponse?.data as WorkPlanBatchDetailsDto[];
            this.totalCount = dataResponse.totalCount;
          }
          this.dataSet.load(this.loadedData);
          this.SpinnerService.hide();

        },
        error => {
          this.SpinnerService.hide();
          console.log(error);
          this.msService.showError('AN ERROR OCCURRED');
        },
    );

    // let data = this.diService.listAssignedCd(documentTypeUuid, page, size, params);
    // console.log(this.activeStatus)
    // // Clear list before loading
    // this.dataSet.load([])
    // // Switch
    // if (this.activeStatus === "completed") {
    //   data = this.diService.listCompletedCd(documentTypeUuid, page, size)
    // } else if (this.activeStatus === "ongoing") {
    //   data = this.diService.listSectionOngoingCd(documentTypeUuid, page, size)
    // } else if (this.activeStatus === "not-assigned") {
    //   data = this.diService.listManualAssignedCd(documentTypeUuid, page, size)
    // }
    // data.subscribe(
    //     result => {
    //       if (result.responseCode === "00") {
    //         let listD: any[] = result.data;
    //         this.totalCount = result.totalCount
    //         this.dataSet.load(listD)
    //       } else {
    //         console.log(result)
    //       }
    //     }
    // );
  }


  toggleStatus(status: string): void {
    console.log(status);
    this.message = null;
    this.searchStatus = null;
    if (status !== this.activeStatus) {
      this.activeStatus = status;
      this.loadData(this.defaultPage, this.defaultPageSize, this.activeStatus);
    }
  }

  onManagerPetroleumChange(event: any) {
    if (this.managerPetroleumUser) {
      this.personalTasks = event.target.value;
      this.loadData(this.defaultPage, this.defaultPageSize, this.activeStatus);
    }
  }

  public onCustomAction(event: any): void {
    switch (event.action) {
      case 'viewRecord':
        this.viewRecord(event.data);
        break;
    }
  }

  viewRecord(data: FuelBatchDetailsDto) {
    console.log('TEST 101' + data.id);
    this.router.navigate([`/complaintPlan`, data.referenceNumber]);
  }

  pageChange(pageIndex?: any) {
    if (pageIndex) {
      this.currentPageInternal = pageIndex - 1;
      this.currentPage = pageIndex;
      this.loadData(this.currentPageInternal, this.defaultPageSize, this.activeStatus);
    }
  }

}
