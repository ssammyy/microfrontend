import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {Store} from '@ngrx/store';
import {MatDialog} from '@angular/material/dialog';
import {Observable, Subject, throwError} from 'rxjs';
import {Router} from '@angular/router';
import {MsService} from '../../../../core/store/data/ms/ms.service';
import {LocalDataSource} from 'ng2-smart-table';
import {NgxSpinnerService} from 'ngx-spinner';
import {BatchFileFuelSaveDto, FuelBatchDetailsDto} from '../../../../core/store/data/ms/ms.model';
import {EpraBatchNewComponent} from './epra-batch-new/epra-batch-new.component';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {
  County,
  CountyService,
  loadCountyId,
  selectCountyIdData,
  selectUserInfo,
  Town,
  TownService,
} from '../../../../core/store';
import {HttpErrorResponse} from '@angular/common/http';

@Component({
  selector: 'app-epra-batch-list',
  templateUrl: './epra-batch-list.component.html',
  styleUrls: ['./epra-batch-list.component.css'],
})
export class EpraBatchListComponent implements OnInit {
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
      batchFileYear: {
        title: 'BATCH FILE YEAR',
        type: 'string',
        filter: false,
      },
      batchFileMonth: {
        title: 'BATCH FILE MONTH',
        type: 'string',
        filter: false,
      }
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
  loadedData: FuelBatchDetailsDto[] = this.msService.fuelBatchDetailsListExamples();


  constructor(private store$: Store<any>,
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
    this.addNewBatchForm = this.formBuilder.group({
      county: null,
      town: null,
      remarks: ['', Validators.required],
    });
  }

  private loadData(page: number, records: number): any {
    this.SpinnerService.show();
    const params = {'personal': this.personalTasks};
    this.msService.loadMSFuelBatchList(String(page), String(records)).subscribe(
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
  }


  onManagerPetroleumChange(event: any) {
    if (this.managerPetroleumUser) {
      this.personalTasks = event.target.value;
      this.loadData(this.defaultPage, this.defaultPageSize);
    }
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

  public onCustomAction(event: any): void {
    switch (event.action) {
      case 'viewRecord':
        this.viewRecord(event.data);
        break;
    }
  }

  viewRecord(data: FuelBatchDetailsDto) {
    console.log('TEST 101' + data.id);
    this.router.navigate([`/epra`, data.referenceNumber]);
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
            this.msService.showSuccess('NEW FUEL BATCH CREATED SUCCESSFUL', this.loadData(this.defaultPage, this.defaultPageSize));

          },
          error => {
            this.SpinnerService.hide();
            console.log(error);
            this.msService.showError('AN ERROR OCCURRED');
          },
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
      this.loadData(this.currentPageInternal, this.defaultPageSize);
    }
  }
}
