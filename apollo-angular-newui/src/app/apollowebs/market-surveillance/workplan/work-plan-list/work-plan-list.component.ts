import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {
  BatchFileFuelSaveDto,
  ComplaintsListDto,
  ComplaintsTaskAndAssignedDto,
  WorkPlanScheduleListDetailsDto,
} from '../../../../core/store/data/ms/ms.model';
import {Observable, Subject, throwError} from 'rxjs';
import {County, CountyService, loadCountyId, selectCountyIdData, selectUserInfo, Town, TownService} from '../../../../core/store';
import {LocalDataSource} from 'ng2-smart-table';
import {Store} from '@ngrx/store';
import {ActivatedRoute, Router} from '@angular/router';
import {NgxSpinnerService} from 'ngx-spinner';
import {MsService} from '../../../../core/store/data/ms/ms.service';

@Component({
  selector: 'app-work-plan-list',
  templateUrl: './work-plan-list.component.html',
  styleUrls: ['./work-plan-list.component.css'],
})
export class WorkPlanListComponent implements OnInit {
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
  selectedBatchRefNo: string;
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
      nameActivity: {
        title: 'ACTIVITY NAME',
        type: 'string',
        filter: false,
      },
      timeActivityDate: {
        title: 'ACTIVITY DATE',
        type: 'date',
        filter: false,
      },
      // complaintCategory: {
      //   title: 'Complaint Category',
      //   type: 'string',
      //   filter: false
      // },
      budget: {
        title: 'BUDGET',
        type: 'string',
        filter: false,
      },
      progressStep: {
        title: 'Status',
        type: 'string',
        filter: false,
      },
    },
    pager: {
      display: true,
      perPage: 10,
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
  loadedListData: ComplaintsTaskAndAssignedDto;
  loadedData: WorkPlanScheduleListDetailsDto;


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

    this.addNewBatchForm = this.formBuilder.group({
      county: ['', Validators.required],
      town: ['', Validators.required],
      remarks: ['', Validators.required],
    });
  }

  private loadData(page: number, records: number, referenceNumber: string, routeTake: string): any {
    this.SpinnerService.show();
    const params = {'personal': this.personalTasks};
    this.msService.loadMSWorkPlanList(String(page), String(records), referenceNumber, routeTake).subscribe(
        (data) => {
          console.log(`TEST DATA===${data}`);
          this.loadedData = data;
          this.totalCount = this.loadedData.workPlanList.length;
          this.dataSet.load(this.loadedData.workPlanList);
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


  onManagerPetroleumChange(event: any) {
    if (this.managerPetroleumUser) {
      this.personalTasks = event.target.value;
      this.loadData(this.defaultPage, this.defaultPageSize, this.selectedBatchRefNo , this.activeStatus);
    }
  }

  toggleStatus(status: string): void {
    console.log(status);
    this.message = null;
    this.searchStatus = null;
    if (status !== this.activeStatus) {
      this.activeStatus = status;
      this.loadData(this.defaultPage, this.defaultPageSize, this.selectedBatchRefNo , this.activeStatus);
    }
  }

  public onCustomAction(event: any): void {
    switch (event.action) {
      case 'viewRecord':
        this.viewRecord(event.data);
        break;
    }
  }

  viewRecord(data: ComplaintsListDto) {
    console.log('TEST 101 ' + data.referenceNumber);
    this.router.navigate([`/complaint/details/`, data.referenceNumber]);
  }

  get formNewBatchForm(): any {
    return this.addNewBatchForm.controls;
  }


  updateSelectedCounty() {
    this.selectedCounty = this.addNewBatchForm?.get('county')?.value;
    console.log(`county set to ${this.selectedCounty}`);
    this.store$.dispatch(loadCountyId({payload: this.selectedCounty}));
    this.store$.select(selectCountyIdData).subscribe(
        (d) => {
          if (d) {
            console.log(`Select county inside is ${d}`);
            return this.townService.getAll();
          } else {
            return throwError('Invalid request, County id is required');
          }
        },
    );

  }

  updateSelectedTown() {
    this.selectedTown = this.addNewBatchForm?.get('town')?.value;
    console.log(`town set to ${this.selectedTown}`);
  }

  onClickSaveNewBatch(valid: boolean) {
    if (valid) {
      this.SpinnerService.show();
      this.dataSave = {...this.dataSave, ...this.addNewBatchForm.value};
      this.msService.addNewMSFuelBatch(this.dataSave).subscribe(
          (data: any) => {
            console.log(data);
            this.SpinnerService.hide();
            this.msService.showSuccess('NEW FUEL BATCH CREATED SUCCESSFUL');
          },
          // ,
          // (err: HttpErrorResponse) => {
          //   console.warn(err.error);
          //   this.SpinnerService.hide();
          //   this.msService.showError(err.message);
          // }
      );
    }
  }

  openModalAddDetails(divVal: string): void {
    const arrHead = ['addNewBatchDetails'];
    const arrHeadSave = ['ADD NEW BATCH FILE'];

    for (let h = 0; h < arrHead.length; h++) {
      if (divVal === arrHead[h]) {
        this.currDivLabel = arrHeadSave[h];
      }
    }
    this.currDiv = divVal;
  }

  // addNewBatch(event: any, type: string) {
  //   let ref = this.dialog.open(EpraBatchNewComponent, {
  //     data: {
  //       documentType: type
  //     }
  //   })
  //   ref.afterClosed()
  //       .subscribe(
  //           res => {
  //             if (res) {
  //               this.loadData(this.defaultPage, this.defaultPageSize)
  //             }
  //           }
  //       )
  // }


  pageChange(pageIndex?: any) {
    if (pageIndex) {
      this.currentPageInternal = pageIndex - 1;
      this.currentPage = pageIndex;
      this.loadData(this.currentPageInternal, this.defaultPageSize, this.selectedBatchRefNo , this.activeStatus);
    }
  }

}
