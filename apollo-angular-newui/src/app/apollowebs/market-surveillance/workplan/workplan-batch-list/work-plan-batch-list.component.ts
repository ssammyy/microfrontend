import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {Store} from '@ngrx/store';
import {Router} from '@angular/router';
import {FormBuilder, FormGroup} from '@angular/forms';
import {NgxSpinnerService} from 'ngx-spinner';
import {County, CountyService, selectUserInfo, Town, TownService} from '../../../../core/store';
import {MsService} from '../../../../core/store/data/ms/ms.service';
import {BatchFileFuelSaveDto, FuelBatchDetailsDto, WorkPlanBatchDetailsDto} from '../../../../core/store/data/ms/ms.model';
import {Observable, Subject} from 'rxjs';
import {LocalDataSource} from 'ng2-smart-table';

@Component({
  selector: 'app-workplan-batch-list',
  templateUrl: './work-plan-batch-list.component.html',
  styleUrls: ['./work-plan-batch-list.component.css'],
})
export class WorkPlanBatchListComponent implements OnInit {
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

  activeStatus = 'my-tasks';
  previousStatus = 'my-tasks';
  searchStatus: any;
  personalTasks = 'false';
  defaultPageSize = 20;
  defaultPage = 0;
  currentPage = 0;
  currentPageInternal = 0;
  totalCount = 12;
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
        filter: false,
      },
      yearName: {
        title: 'YEAR NAME',
        type: 'string',
        filter: false,
      },
      userCreated: {
        title: 'CREATED BY',
        type: 'string',
        filter: false,
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
      workPlanStatus: {
        title: 'APPROVAL STATUS',
        type: 'boolean',
        filter: false,
      },
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
    this.loadData(this.defaultPage, this.defaultPageSize);
  }

  private loadData(page: number, records: number): any {
    this.SpinnerService.show();
    this.msService.loadMSWorkPlanBatchList(String(page), String(records)).subscribe(
        (data) => {
          console.log(`TEST DATA===${data}`);
          this.loadedData = data;
          this.totalCount = this.loadedData.length;
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
      this.loadData(this.defaultPage, this.defaultPageSize);
    }
  }

  onManagerPetroleumChange(event: any) {
    if (this.managerPetroleumUser) {
      this.personalTasks = event.target.value;
      this.loadData(this.defaultPage, this.defaultPageSize);
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
    this.router.navigate([`/workPlan`, data.referenceNumber]);
  }

  pageChange(pageIndex?: any) {
    if (pageIndex) {
      this.currentPageInternal = pageIndex - 1;
      this.currentPage = pageIndex;
      this.loadData(this.currentPageInternal, this.defaultPageSize);
    }
  }

  addNewWorkPlan() {
    this.SpinnerService.show();
    this.msService.addNewMSWorkPlanBatch(String(this.defaultPage), String(this.defaultPageSize)).subscribe(
        (data: any) => {
          console.log(`TEST DATA===${data}`);
          this.loadedData = data;
          this.totalCount = this.loadedData.length;
          this.dataSet.load(this.loadedData);
          this.SpinnerService.hide();
          this.msService.showSuccess('NEW WORK-PLAN BATCH CREATED SUCCESSFUL');

        },
        error => {
          this.SpinnerService.hide();
          console.log(error);
          this.msService.showError('AN ERROR OCCURRED');
        },
    );
  }
}
