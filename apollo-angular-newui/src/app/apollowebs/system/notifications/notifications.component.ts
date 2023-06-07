import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {
  ApiResponseModel, MsNotificationTaskDto,
  WorkPlanListDto, WorkPlanScheduleListDetailsDto,
} from '../../../core/store/data/ms/ms.model';
import {Observable, Subject} from 'rxjs';
import {County, CountyService, selectUserInfo, Town, TownService} from '../../../core/store';
import {LocalDataSource} from 'ng2-smart-table';
import {Store} from '@ngrx/store';
import {ActivatedRoute, Router} from '@angular/router';
import {NgxSpinnerService} from 'ngx-spinner';
import {MsService} from '../../../core/store/data/ms/ms.service';
import {UserNotificationDetailsDto} from '../../../core/store/data/master/master.model';

@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.css'],
})
export class NotificationsComponent implements OnInit {
  @ViewChild('editModal') editModal !: TemplateRef<any>;
  submitted = false;
  selectedCounty = 0;
  selectedTown = 0;
  selectedTownName: string;
  selectedCountyName: string;
  county$: Observable<County[]>;
  town$: Observable<Town[]>;
  loading = false;

  roles: string[];

  activeStatus = 'my-tasks';
  previousStatus = 'my-tasks';
  selectedBatchRefNo: string;
  searchStatus: any;
  personalTasks = 'false';
  defaultPageSize = 20;
  defaultPage = 0;
  currentPage = 0;
  currentPageInternal = 0;
  selectedNotification: UserNotificationDetailsDto;
  loadedData!: MsNotificationTaskDto[];
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
        {name: 'viewRecord', title: '<i class="btn btn-sm btn-primary" >View TASK</i>'},
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
      notificationName: {
        title: 'NAME',
        type: 'string',
        filter: true,
      },
      notificationMsg: {
        title: 'MESSAGE',
        type: 'string',
        filter: false,
      },
      readStatus: {
        title: 'READ STATUS',
        type: 'string',
        valuePrepareFunction: (dataTest) => {
          if (dataTest) {
            return 'YES';
          }
          return 'NO';
        },
        filter: false,
      },
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

    this.activatedRoute.paramMap.subscribe(
        rs => {
          this.selectedBatchRefNo = rs.get('referenceNumber');
          this.loadData(this.defaultPage, this.defaultPageSize, this.selectedBatchRefNo, this.activeStatus);
        },
    );
  }

  private loadData(page: number, records: number, referenceNumber: string, routeTake: string): any {
    this.SpinnerService.show();
    const params = {'personal': this.personalTasks};
    this.msService.loadNotificationList(String(page), String(records)).subscribe(
        (data: MsNotificationTaskDto[]) => {
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
  }


  public onCustomAction(event: any): void {
    switch (event.action) {
      case 'viewRecord':
        this.viewRecord(event.data);
        break;
    }
  }

  // tslint:disable-next-line:no-shadowed-variable
  viewRecord(data: MsNotificationTaskDto) {
    this.msService.loadNotificationRead(data?.notificationBody?.taskRefNumber).subscribe(
        (dataFound: MsNotificationTaskDto[]) => {
          this.loadedData = dataFound;
          this.totalCount = this.loadedData.length;
          this.dataSet.load(this.loadedData);
          if (data?.notificationBody?.processType === 'COMPLAINT-PLAN') {
            // tslint:disable-next-line:max-line-length
            this.router.navigate([`/complaintPlan/details/`, data?.notificationBody?.referenceNoFound, data?.notificationBody?.batchReferenceNoFound]);
          } else if (data?.notificationBody?.processType === 'WORK-PLAN') {
            // tslint:disable-next-line:max-line-length
            this.router.navigate([`/workPlan/details/`, data?.notificationBody?.referenceNoFound, data?.notificationBody?.batchReferenceNoFound]);
          } else if (data?.notificationBody?.processType === 'COMPLAINT') {
            this.router.navigate([`/complaint/details/`, data?.notificationBody?.referenceNoFound]);
          }
          this.SpinnerService.hide();
        },
        error => {
          this.SpinnerService.hide();
          console.log(error);
          // this.msService.showError('AN ERROR OCCURRED');
        },
    );
}

  pageChange(pageIndex?: any) {
    if (pageIndex) {
      this.currentPageInternal = pageIndex - 1;
      this.currentPage = pageIndex;
      this.loadData(this.currentPageInternal, this.defaultPageSize, this.selectedBatchRefNo , this.activeStatus);
    }
  }

}

