import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {Observable, Subject} from 'rxjs';
import {County, CountyService, selectUserInfo, Town, TownService} from '../../../../core/store';
import {AcknowledgementDto, ApiResponseModel, ReportSubmittedDto} from '../../../../core/store/data/ms/ms.model';
import {LocalDataSource} from 'ng2-smart-table';
import {Store} from '@ngrx/store';
import {ActivatedRoute, Router} from '@angular/router';
import {FormBuilder} from '@angular/forms';
import {NgxSpinnerService} from 'ngx-spinner';
import {MsService} from '../../../../core/store/data/ms/ms.service';

@Component({
  selector: 'app-report-submitted-timeline',
  templateUrl: './report-submitted-timeline.component.html',
  styleUrls: ['./report-submitted-timeline.component.css']
})
export class ReportSubmittedTimelineComponent implements OnInit {

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
  defaultPageSize = 10;
  defaultPage = 0;
  currentPage = 0;
  currentPageInternal = 0;
  selectedNotification: ReportSubmittedDto;
  loadedData!: ReportSubmittedDto[];
  totalCount = 12;
  public settings = {
    selectMode: 'single',  // single|multi
    hideHeader: false,
    hideSubHeader: false,
    // actions: {
    //   columnTitle: 'Actions',
    //   add: false,
    //   edit: false,
    //   delete: false,
    //   custom: [
    //     //  { name: 'editRecord', title: '<i class="btn btn-sm btn-primary">View More</i>' },
    //     // {name: 'viewRecord', title: '<i class="btn btn-sm btn-primary" >View Message</i>'},
    //   ],
    //   position: 'right', // left|right
    // },
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
      complaintTitle: {
        title: 'COMPLAINT TITLE',
        type: 'string',
        filter: true,
      },
      targetedProducts: {
        title: 'TARGETED PRODUCTS',
        type: 'string',
        filter: true,
      },
      transactionDate: {
        title: 'TRANSACTION DATE',
        type: 'date',
        filter: true,
      },
      approvedDate: {
        title: 'APPROVED DATE',
        type: 'date',
        filter: true,
      },
      rejectedDate: {
        title: 'REJECTED DATE',
        type: 'date',
        filter: true,
      },
      acknowledgementType: {
        title: 'ACKNOWLEDGEMENT TYPE',
        type: 'string',
        filter: true,
      },
      reportSubmited: {
        title: 'REPORT SUBMMITED',
        type: 'string',
        filter: true,
      },
      timeTakenForReportSubmission: {
        title: 'TIME TAKEN',
        type: 'string',
        filter: true,
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
          this.loadData(this.defaultPage, this.defaultPageSize);
        },
    );
  }

  private loadData(page: number, records: number): any {
    this.SpinnerService.show();
    const params = {'personal': this.personalTasks};
    this.msService.loadReportSubmittedList(String(page), String(records)).subscribe(
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
          // this.msService.showError('AN ERROR OCCURRED');
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

}

