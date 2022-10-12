import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {
  BatchFileFuelSaveDto,
  ComplaintsTaskAndAssignedDto, MsBroadProductCategory,
  MsDepartment,
  MsDivisionDetails, MsProducts, MsProductSubcategory, MsStandardProductCategory, WorkPlanEntityDto, WorkPlanListDto,
  WorkPlanScheduleListDetailsDto
} from '../../../../core/store/data/ms/ms.model';
import {Observable, Subject, throwError} from 'rxjs';
import {County, CountyService, loadCountyId, selectCountyIdData, selectUserInfo, Town, TownService} from '../../../../core/store';
import {
  BroadProductCategory,
  ProductCategories,
  Products,
  ProductSubcategory,
  StandardProductCategory
} from '../../../../core/store/data/master/master.model';
import {LocalDataSource} from 'ng2-smart-table';
import {Store} from '@ngrx/store';
import {ActivatedRoute, Router} from '@angular/router';
import {NgxSpinnerService} from 'ngx-spinner';
import {MsService} from '../../../../core/store/data/ms/ms.service';

@Component({
  selector: 'app-complaint-plan-list',
  templateUrl: './complaint-plan-list.component.html',
  styleUrls: ['./complaint-plan-list.component.css']
})
export class ComplaintPlanListComponent implements OnInit {
  @ViewChild('editModal') editModal !: TemplateRef<any>;
  currDiv!: string;
  currDivLabel!: string;
  addNewScheduleForm!: FormGroup;
  addChargeSheetForm!: FormGroup;
  dataSave: BatchFileFuelSaveDto;
  submitted = false;
  selectedCounty = 0;
  selectedTown = 0;
  county$: Observable<County[]>;
  town$: Observable<Town[]>;
  loading = false;

  roles: string[];
  msDepartments: MsDepartment[];
  msDivisions: MsDivisionDetails[];
  standardProductCategory!: StandardProductCategory[];
  productCategories!: ProductCategories[];
  broadProductCategory!: BroadProductCategory[];
  products!: Products[];
  productSubcategory!: ProductSubcategory[];
  standardProductCategorySelected: 0;
  productCategoriesSelected: 0;
  broadProductCategorySelected: 0;
  productsSelected: 0;
  productSubcategorySelected: 0;
  departmentSelected: 0;

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
  loadedData!: WorkPlanScheduleListDetailsDto;
  dataSaveWorkPlan: WorkPlanEntityDto;


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

    this.addNewScheduleForm = this.formBuilder.group({
      complaintDepartment: ['', Validators.required],
      divisionId: ['', Validators.required],
      nameActivity: ['', Validators.required],
      timeActivityDate: ['', Validators.required],
      county: ['', Validators.required],
      townMarketCenter: ['', Validators.required],
      locationActivityOther: ['', Validators.required],
      standardCategory: ['', Validators.required],
      broadProductCategory: ['', Validators.required],
      productCategory: ['', Validators.required],
      product: ['', Validators.required],
      productSubCategory: ['', Validators.required],
      resourcesRequired: ['', Validators.required],
      budget: ['', Validators.required],
      // remarks: ['', Validators.required],
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

          switch (this.loadedData.createdWorkPlan.batchClosed) {
            case false:
              this.msService.msDepartmentListDetails().subscribe(
                  (dataDep: MsDepartment[]) => {
                    this.msDepartments = dataDep;
                    console.log(dataDep);
                  },
                  error => {
                    console.log(error);
                    this.msService.showError('AN ERROR OCCURRED');
                  },
              );
              this.msService.msDivisionListDetails().subscribe(
                  (dataDiv: MsDivisionDetails[]) => {
                    this.msDivisions = dataDiv;
                    console.log(dataDiv);
                  },
                  error => {
                    console.log(error);
                    this.msService.showError('AN ERROR OCCURRED');
                  },
              );
              this.msService.msProductStandardCategoryListDetails().subscribe(
                  (data1: MsStandardProductCategory[]) => {
                    this.standardProductCategory = data1;
                    console.log(data1);
                  },
                  error => {
                    console.log(error);
                    this.msService.showError('AN ERROR OCCURRED');
                  },
              );
              this.msService.msProductBroadCategoryListDetails().subscribe(
                  (data2: MsBroadProductCategory[]) => {
                    this.broadProductCategory = data2;
                    console.log(data2);
                  },
                  error => {
                    console.log(error);
                    this.msService.showError('AN ERROR OCCURRED');
                  },
              );
              this.msService.msProductCategoryListDetails().subscribe(
                  (data3: MsBroadProductCategory[]) => {
                    this.productCategories = data3;
                    console.log(data3);
                  },
                  error => {
                    console.log(error);
                    this.msService.showError('AN ERROR OCCURRED');
                  },
              );
              this.msService.msProductListDetails().subscribe(
                  (data4: MsProducts[]) => {
                    this.products = data4;
                    console.log(data4);
                  },
                  error => {
                    console.log(error);
                    this.msService.showError('AN ERROR OCCURRED');
                  },
              );
              this.msService.msProductSubCategoryListDetails().subscribe(
                  (data5: MsProductSubcategory[]) => {
                    this.productSubcategory = data5;
                    console.log(data5);
                  },
                  error => {
                    console.log(error);
                    this.msService.showError('AN ERROR OCCURRED');
                  },
              );
              break;
          }
          this.SpinnerService.hide();

        },
        error => {
          this.SpinnerService.hide();
          console.log(error);
          this.msService.showError('AN ERROR OCCURRED');
        },
    );
  }

  goBack() {
    // console.log('TEST 101' + this.loadedData.createdWorkPlan.referenceNumber);
    this.router.navigate([`/complaintPlan`]);
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

  viewRecord(data: WorkPlanListDto) {
    console.log('TEST 101 ' + data.referenceNumber);
    this.router.navigate([`/complaintPlan/details/`, data.referenceNumber, this.selectedBatchRefNo]);
  }

  get formNewScheduleForm(): any {
    return this.addNewScheduleForm.controls;
  }


  updateSelectedCounty() {
    this.selectedCounty = this.addNewScheduleForm?.get('county')?.value;
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
    this.selectedTown = this.addNewScheduleForm?.get('town')?.value;
    console.log(`town set to ${this.selectedTown}`);
  }

  openModalAddDetails(divVal: string): void {
    const arrHead = ['addNewScheduleDetails'];
    const arrHeadSave = ['ADD NEW COMPLAINT-PLAN SCHEDULE DETAILS FILE'];

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

  onClickCloseBatch() {
    this.msService.showSuccessWith2Message('Are you sure your want to close this Work-Plan?', 'You won\'t be able to add new schedule after submission!',
        // tslint:disable-next-line:max-line-length
        'You can click the \'ADD NEW COMPLAINT-PLAN FILE\' button to add another Work Plan', 'YEARLY COMPLAINT-PLAN SENT FOR APPROVAL SUCCESSFUL', () => {
          this.closeBatch();
        });
  }

  closeBatch() {
    this.SpinnerService.show();
    let resultStatus = false;
    console.log(this.loadedData.createdWorkPlan.referenceNumber);
    this.msService.closeMSWorkPlanBatch(this.selectedBatchRefNo).subscribe(
        (data: any) => {
          console.log(data);
          this.SpinnerService.hide();
          resultStatus  = true;
          this.msService.showSuccess('YEARLY COMPLAINT-PLAN SENT FOR APPROVAL SUCCESSFUL');
          this.router.navigate([`/complaintPlan`]);
        },
        error => {
          this.SpinnerService.hide();
          console.log(error);
          resultStatus = false;
          // this.msService.showError("AN ERROR OCCURRED")
        },
    );
    return resultStatus;
  }

  onChangeSelectedDepartment() {
    this.departmentSelected = this.addNewScheduleForm?.get('complaintDepartment')?.value;
    this.standardProductCategorySelected = this.addNewScheduleForm?.get('standardCategory')?.value;
    this.broadProductCategorySelected = this.addNewScheduleForm?.get('broadProductCategory')?.value;
    this.productCategoriesSelected = this.addNewScheduleForm?.get('productCategory')?.value;
    this.productsSelected = this.addNewScheduleForm?.get('product')?.value;
    this.productSubcategorySelected = this.addNewScheduleForm?.get('productSubcategory')?.value;
  }

  onChangeSelectedProductClassification() {
    this.standardProductCategorySelected = this.addNewScheduleForm?.get('standardCategory')?.value;
    this.broadProductCategorySelected = this.addNewScheduleForm?.get('broadProductCategory')?.value;
    this.productCategoriesSelected = this.addNewScheduleForm?.get('productCategory')?.value;
    this.productsSelected = this.addNewScheduleForm?.get('product')?.value;
    this.productSubcategorySelected = this.addNewScheduleForm?.get('productSubcategory')?.value;
  }

  onChangeSelectedBroadProductCategory() {
    this.broadProductCategorySelected = this.addNewScheduleForm?.get('broadProductCategory')?.value;
    this.productCategoriesSelected = this.addNewScheduleForm?.get('productCategory')?.value;
    this.productsSelected = this.addNewScheduleForm?.get('product')?.value;
    this.productSubcategorySelected = this.addNewScheduleForm?.get('productSubcategory')?.value;
  }

  onChangeSelectedProductCategory() {
    this.productCategoriesSelected = this.addNewScheduleForm?.get('productCategory')?.value;
    this.productsSelected = this.addNewScheduleForm?.get('product')?.value;
    this.productSubcategorySelected = this.addNewScheduleForm?.get('productSubcategory')?.value;
  }

  onChangeSelectedMyProduct() {
    this.productsSelected = this.addNewScheduleForm?.get('product')?.value;
    this.productSubcategorySelected = this.addNewScheduleForm?.get('productSubcategory')?.value;
  }

  onChangeSelectedProductSubcategory() {
    this.productSubcategorySelected = this.addNewScheduleForm?.get('productSubcategory')?.value;
  }

  onClickSaveWorkPlanScheduled() {
    this.submitted = true;
    if (this.addNewScheduleForm.invalid) {
      return;
    }
    if (this.addNewScheduleForm.valid) {
      this.SpinnerService.show();
      this.dataSaveWorkPlan = {...this.dataSaveWorkPlan, ...this.addNewScheduleForm.value};
      // tslint:disable-next-line:max-line-length
      this.msService.msAddWorkPlanScheduleDetails(this.loadedData.createdWorkPlan.referenceNumber, this.dataSaveWorkPlan).subscribe(
          (data: any) => {
            console.log(data);
            this.SpinnerService.hide();
            this.addNewScheduleForm.reset();
            this.msService.showSuccess('COMPLAINT SCHEDULED DETAILS, SAVED SUCCESSFULLY');
            this.loadData(this.defaultPage, this.defaultPageSize, this.selectedBatchRefNo , this.activeStatus);
          },
          error => {
            this.SpinnerService.hide();
            this.addNewScheduleForm.reset();
            console.log(error);
            this.msService.showError('AN ERROR OCCURRED');
          },
      );

    }
  }

}
