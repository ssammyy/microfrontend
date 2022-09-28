import {Component, Inject, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {DatePipe} from '@angular/common';
import {interval, Observable, Subject, throwError} from 'rxjs';
import {Store} from '@ngrx/store';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from '@angular/material/dialog';
import {ActivatedRoute, Router} from '@angular/router';
import {LocalDataSource} from 'ng2-smart-table';
import {
  County,
  CountyService,
  loadCountyId,
  selectCountyIdData,
  selectUserInfo,
  Town,
  TownService,
} from '../../../../core/store';
import {CustomeDateValidators, MsService} from '../../../../core/store/data/ms/ms.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {
  BatchFileFuelSaveDto,
  FuelBatchDetailsDto, FuelEntityDto,
  FuelInspectionDto,
  FuelInspectionScheduleListDetailsDto,
} from '../../../../core/store/data/ms/ms.model';
import {NgxSpinnerService} from 'ngx-spinner';

@Component({
  selector: 'app-epra-list',
  templateUrl: './epra-list.component.html',
  styleUrls: ['./epra-list.component.css'],
})
export class EpraListComponent implements OnInit {
  @ViewChild('editModal') editModal !: TemplateRef<any>;
  currDiv!: string;
  currDivLabel!: string;
  addNewScheduleForm!: FormGroup;
  closeBatchForm!: FormGroup;
  dataSave: FuelEntityDto;
  submitted = false;
  batchReferenceNo: string;
  teamsReferenceNo: string;
  countyReferenceNo: string;
  selectedCounty = 0;
  selectedTown = 0;
  county$: Observable<County[]>;
  town$: Observable<Town[]>;
  loading = false;

  roles: string[];

  minDate = new Date();

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
        {name: 'viewRecord', title: '<i class="btn btn-sm btn-primary">View More</i>'},
      ],
      position: 'right', // left|right
    },
    rowClassFunction: (row) => {
      // console.log(row)
      if (row.data.isNcrDocument) {
        return 'risky';
      } else {
        return '';
      }

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
      company: {
        title: 'COMPANY',
        type: 'string',
        filter: false,
      },
      petroleumProduct: {
        title: 'PETROLEUM PRODUCT',
        type: 'string',
        filter: false,
      },
      physicalLocation: {
        title: 'PHYSICAL LOCATION',
        type: 'string',
        filter: false,
      },
      assignedOfficerStatus: {
        title: 'ASSIGNED OFFICER',
        type: 'boolean',
        filter: false,
      },
      inspectionDateFrom: {
        title: 'INSPECTION DATE FROM',
        type: 'data',
        valuePrepareFunction: (date) => {
          if (date) {
            // return new DatePipe('en-GB').transform(date, 'dd/MM/yyyy hh:mm');
            return date;
          }
          return '';
        },
        filter: false,
      },
      inspectionDateTo: {
        title: 'INSPECTION DATE TO',
        type: 'data',
        valuePrepareFunction: (date) => {
          if (date) {
            // return new DatePipe('en-GB').transform(date, 'dd/MM/yyyy hh:mm');
            return date;
          }
          return '';
        },
        filter: false,
      },
      processStage: {
        title: 'PROCESS STAGE',
        type: 'string',
        filter: false,
      },
      endInspectionStatus: {
        title: 'CLOSED STATUS',
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
  loadedData: FuelInspectionScheduleListDetailsDto = new FuelInspectionScheduleListDetailsDto();



  constructor(
      private store$: Store<any>,
      // private dialog: MatDialog,
      private activatedRoute: ActivatedRoute,
      private router: Router,
      private formBuilder: FormBuilder,
      private SpinnerService: NgxSpinnerService,
      private countyService: CountyService,
      private townService: TownService,
      private msService: MsService,
  ) {
    this.town$ = townService.entities$;
  }

  ngOnInit(): void {

    this.store$.select(selectUserInfo).pipe().subscribe((u) => {
      return this.roles = u.roles;
    });

    this.activatedRoute.paramMap.subscribe(
        rs => {
          this.batchReferenceNo = rs.get('batchReferenceNumber');
          this.teamsReferenceNo = rs.get('teamsReferenceNo');
          this.countyReferenceNo = rs.get('countyReferenceNo');
          this.loadData(
              this.batchReferenceNo,
              this.teamsReferenceNo,
              this.countyReferenceNo,
              this.defaultPage, this.defaultPageSize);
        },
    );


    this.addNewScheduleForm = this.formBuilder.group({
      company: ['', Validators.required],
      petroleumProduct: ['', Validators.required],
      physicalLocation: ['', Validators.required],
      inspectionDateFrom: ['', Validators.required],
      inspectionDateTo: ['', Validators.required],
      stationOwnerEmail: ['', Validators.required],
      stationKraPin: ['', Validators.required],
      townID: ['', Validators.required],
      remarks: null,
    }, { validator: [
      // Default error with this validator:  {fromToDate: true}
      CustomeDateValidators.fromToDate('inspectionDateFrom', 'inspectionDateTo'),
      // For custome error name like: {customeErrorName: true}, pass third optional parameter with custome name
      // CustomeDateValidators.fromToDate('fromDate', 'toDate', 'customeErrorName')
    ]});

    this.closeBatchForm = this.formBuilder.group({
      remarks: ['', Validators.required],
    });
  }

  goBack() {
    this.router.navigate(['/epra/details/', this.batchReferenceNo, this.teamsReferenceNo]);
  }

  private loadData(batchReferenceNumber: string, teamsReferenceNo: string, countyReferenceNo: string, page: number, records: number): any {
    this.SpinnerService.show();
    const params = {'personal': this.personalTasks};
    this.msService.msFuelInspectionList(
        batchReferenceNumber,
      teamsReferenceNo,
      countyReferenceNo,
      String(page), String(records)).subscribe(
        (data) => {
          this.loadedData = data;
          this.totalCount = this.loadedData.fuelInspectionDto.length;
          this.dataSet.load(this.loadedData.fuelInspectionDto);
          this.updateSelectedCounty(this.loadedData?.fuelTeamsDto?.countyID);
          this.SpinnerService.hide();
          console.log(data);
        },
        error => {
          this.SpinnerService.hide();
          console.log(error);
          this.msService.showError('AN ERROR OCCURRED');
        },
    );
  }


  onManagerPetroleumChange(event: any) {
    if (this.managerPetroleumUser) {
      this.personalTasks = event.target.value;
      this.loadData(this.batchReferenceNo, this.teamsReferenceNo, this.countyReferenceNo, this.defaultPage, this.defaultPageSize);
    }
  }

  toggleStatus(status: string): void {
    console.log(status);
    this.message = null;
    this.searchStatus = null;
    if (status !== this.activeStatus) {
      this.activeStatus = status;
      this.loadData(this.batchReferenceNo, this.teamsReferenceNo, this.countyReferenceNo, this.defaultPage, this.defaultPageSize);
    }
  }

  public onCustomAction(event: any): void {
    switch (event.action) {
      case 'viewRecord':
        this.viewRecord(event.data);
        break;
    }
  }

  viewRecord(data: FuelInspectionDto) {
    console.log('TEST 101 REF NO: ' + data.referenceNumber);
    this.router.navigate([`/epra/details/`, this.batchReferenceNo, this.teamsReferenceNo, this.countyReferenceNo, data.referenceNumber ]);
  }

  get formNewScheduleForm(): any {
    return this.addNewScheduleForm.controls;
  }

  get formCloseBatchForm(): any {
    return this.closeBatchForm.controls;
  }


  updateSelectedCounty(selectedCountyValue) {
    this.store$.dispatch(loadCountyId({payload: selectedCountyValue}));
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


  onClickSaveNewSchedule(valid: boolean) {
    if (valid) {
      this.SpinnerService.show();
      this.dataSave = {...this.dataSave, ...this.addNewScheduleForm.value};
      this.msService.msFuelInspectionAddSchedule(
          this.batchReferenceNo,
          this.teamsReferenceNo,
          this.countyReferenceNo,
          this.dataSave,
      ).subscribe(
          (data: any) => {
            this.addNewScheduleForm.reset();
            console.log(data);
            this.loadedData = data;
            this.totalCount = this.loadedData.fuelInspectionDto.length;
            this.dataSet.load(this.loadedData.fuelInspectionDto);
            this.SpinnerService.hide();
            this.msService.showSuccess('NEW FUEL SCHEDULE CREATED SUCCESSFUL');
          },
          error => {
            this.SpinnerService.hide();
            console.log(error);
            this.msService.showError('AN ERROR OCCURRED');
          },
      );
    }
  }

  reloadCurrentRoute() {
    location.reload();
  }

  openModalAddDetails(divVal: string): void {
    const arrHead = ['addNewScheduleDetails', 'closeBatchDetails'];
    const arrHeadSave = ['ADD NEW SCHEDULE DETAILS', 'CLOSE BATCH AND SEND TO KEBS'];

    for (let h = 0; h < arrHead.length; h++) {
      if (divVal === arrHead[h]) {
        this.currDivLabel = arrHeadSave[h];
      }
    }
    this.currDiv = divVal;
  }


  pageChange(pageIndex?: any) {
    if (pageIndex) {
      this.currentPageInternal = pageIndex - 1;
      this.currentPage = pageIndex;
      this.loadData(this.batchReferenceNo, this.teamsReferenceNo, this.countyReferenceNo, this.currentPageInternal, this.defaultPageSize);
    }
  }
}
