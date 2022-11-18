import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {
  AllWorkPlanDetails,
  BatchFileFuelSaveDto,
  ComplaintsListDto,
  ComplaintsTaskAndAssignedDto,
  MsBroadProductCategory,
  MsDepartment,
  MsDivisionDetails,
  MsProducts, MsProductSubcategory,
  MsStandardProductCategory, PredefinedResourcesRequired, WorkPlanEntityDto,
  WorkPlanListDto,
  WorkPlanScheduleListDetailsDto, WorkPlanTownsDto,
} from '../../../../core/store/data/ms/ms.model';
import {Observable, Subject, throwError} from 'rxjs';
import {County, CountyService, loadCountyId, selectCountyIdData, selectUserInfo, Town, TownService} from '../../../../core/store';
import {LocalDataSource} from 'ng2-smart-table';
import {Store} from '@ngrx/store';
import {ActivatedRoute, Router} from '@angular/router';
import {NgxSpinnerService} from 'ngx-spinner';
import {MsService} from '../../../../core/store/data/ms/ms.service';
import {
  BroadProductCategory,
  ProductCategories,
  Products,
  ProductSubcategory,
  StandardProductCategory,
} from '../../../../core/store/data/master/master.model';
import {data} from 'jquery';

@Component({
  selector: 'app-work-plan-list',
  templateUrl: './work-plan-list.component.html',
  styleUrls: ['./work-plan-list.component.css'],
})
export class WorkPlanListComponent implements OnInit {
  @ViewChild('editModal') editModal !: TemplateRef<any>;
  currDiv!: string;
  currDivLabel!: string;
  addNewScheduleForm!: FormGroup;
  addChargeSheetForm!: FormGroup;
  dataSave: BatchFileFuelSaveDto;
  submitted = false;
  selectedCounty = 0;
  selectedTown = 0;
  selectedTownName: string;
  selectedCountyName: string;
  county$: Observable<County[]>;
  town$: Observable<Town[]>;
  loading = false;

  roles: string[];
  msCounties: County[] = [];
  msTowns: Town[] = [];
  msDepartments: MsDepartment[] = [];
  msDivisions: MsDivisionDetails[] = [];
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
        filter: true,
      },
      nameActivity: {
        title: 'ACTIVITY NAME',
        type: 'string',
        filter: true,
      },
      timeActivityDate: {
        title: 'ACTIVITY DATE',
        type: 'date',
        filter: true,
      },
      // complaintCategory: {
      //   title: 'Complaint Category',
      //   type: 'string',
      //   filter: false
      // },
      budget: {
        title: 'BUDGET',
        type: 'string',
        filter: true,
      },
      progressStep: {
        title: 'Status',
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
  dataSaveAllWorkPlan: AllWorkPlanDetails;

  addResourceRequiredForm!: FormGroup;
  addCountyTownForm!: FormGroup;
  predefinedResourcesRequired!: PredefinedResourcesRequired[];
  dataCountyTown: WorkPlanTownsDto;
  dataCountyTownList: WorkPlanTownsDto[] = [];
  dataSaveResourcesRequired: PredefinedResourcesRequired;
  dataSaveResourcesRequiredList: PredefinedResourcesRequired[] = [];


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

    this.addResourceRequiredForm = this.formBuilder.group({
      resourceName: ['', Validators.required],
    });

    this.addCountyTownForm = this.formBuilder.group({
      countyID: ['', Validators.required],
      townID: ['', Validators.required],
      locationName: ['', Validators.required],
      countyName: ['', Validators.required],
      townName: ['', Validators.required],
    });

    this.addNewScheduleForm = this.formBuilder.group({
      complaintDepartment: ['', Validators.required],
      divisionId: ['', Validators.required],
      nameActivity: ['', Validators.required],
      rationale: ['', Validators.required],
      scopeOfCoverage: ['', Validators.required],
      timeActivityDate: ['', Validators.required],
      county: ['', Validators.required],
      townMarketCenter: ['', Validators.required],
      locationActivityOther: ['', Validators.required],
      standardCategory: ['', Validators.required],
      broadProductCategory: ['', Validators.required],
      productCategory: ['', Validators.required],
      product: ['', Validators.required],
      productSubCategory: ['', Validators.required],
      resourcesRequired: null,
      budget: ['', Validators.required],
      // remarks: ['', Validators.required],
    });
  }

  get formAddResourceRequiredForm(): any {
    return this.addResourceRequiredForm.controls;
  }

  get formAddCountyTownForm(): any {
    return this.addCountyTownForm.controls;
  }

  private loadData(page: number, records: number, referenceNumber: string, routeTake: string): any {
    this.SpinnerService.show();
    const params = {'personal': this.personalTasks};
    this.msService.loadMSWorkPlanList(String(page), String(records), referenceNumber, routeTake, 'false').subscribe(
        (data) => {
          console.log(`TEST DATA===${data}`);
          this.loadedData = data;
          this.totalCount = this.loadedData.workPlanList.length;
          this.dataSet.load(this.loadedData.workPlanList);

            this.msService.msPredefinedResourcesRequiredListDetails().subscribe(
                (data1: PredefinedResourcesRequired[]) => {
                  this.predefinedResourcesRequired = data1;
                  console.log(data1);
                },
                error => {
                  console.log(error);
                  this.msService.showError('AN ERROR OCCURRED');
                },
            );
          this.msService.msCountiesListDetails().subscribe(
              (dataCounties: County[]) => {
                this.msCounties = dataCounties;
              },
              error => {
                console.log(error);
                this.msService.showError('AN ERROR OCCURRED');
              },
          );
          this.msService.msTownsListDetails().subscribe(
              (dataTowns: Town[]) => {
                this.msTowns = dataTowns;
              },
              error => {
                console.log(error);
                this.msService.showError('AN ERROR OCCURRED');
              },
          );
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
    this.router.navigate([`/workPlan`]);
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

  // tslint:disable-next-line:no-shadowed-variable
  viewRecord(data: WorkPlanListDto) {
    console.log('TEST 101 ' + data.referenceNumber);
    this.router.navigate([`/workPlan/details/`, data.referenceNumber, this.selectedBatchRefNo]);
  }

  get formNewScheduleForm(): any {
    return this.addNewScheduleForm.controls;
  }
  // loadNotificationRead
  onClickAddResource() {
    this.dataSaveResourcesRequired = this.addResourceRequiredForm.value;
    console.log(this.dataSaveResourcesRequired);
    // tslint:disable-next-line:max-line-length
    const  resourceName = this.dataSaveResourcesRequiredList.filter(x => String(this.dataSaveResourcesRequired.resourceName) === String(x.resourceName)).length;
    if (resourceName > 0) {
      console.log('ResourceFound =' + this.dataSaveResourcesRequired.resourceName);
      this.msService.showWarning('You have already added ' + this.dataSaveResourcesRequired.resourceName);
    } else {
      this.dataSaveResourcesRequiredList.push(this.dataSaveResourcesRequired);
      console.log('ResourceFound Not Found =' + this.dataSaveResourcesRequired.resourceName);
    }

    this.addResourceRequiredForm?.get('resourceName')?.reset();
  }

  onClickAddCountyTown() {
    this.dataCountyTown = this.addCountyTownForm.value;
    this.dataCountyTownList.push(this.dataCountyTown);
    this.addCountyTownForm.reset();
  }

  // Remove Form repeater values
  removeDataResource(index) {
    console.log(index);
    if (index === 0) {
      this.dataSaveResourcesRequiredList.splice(index, 1);
    } else {
      this.dataSaveResourcesRequiredList.splice(index, index);
    }
  }

  // Remove Form repeater values
  removeDataCountyTown(index) {
    console.log(index);
    if (index === 0) {
      this.dataCountyTownList.splice(index, 1);
    } else {
      this.dataCountyTownList.splice(index, index);
    }
  }


  updateSelectedCounty() {
    this.selectedCounty = this.addNewScheduleForm?.get('county')?.value;
    this.msService.msCountiesListDetails().subscribe(
        (dataCounties: County[]) => {
          this.selectedCountyName = dataCounties.find(x => x.id === this.selectedCounty).county;
        },
    );
  }

  updateSelectedTown() {
    this.selectedTown = this.addNewScheduleForm?.get('town')?.value;
    // // tslint:disable-next-line:no-shadowed-variable
    // this.selectedTownName = this.msTowns?.find(x => x.id === this.selectedTown).town;
    // console.log(`town set to ${this.selectedTown}`);
  }

  onClickSaveNewBatch(valid: boolean) {
    if (valid) {
      this.SpinnerService.show();
      this.dataSave = {...this.dataSave, ...this.addNewScheduleForm.value};
      this.msService.addNewMSFuelBatch(this.dataSave).subscribe(
          // tslint:disable-next-line:no-shadowed-variable
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
    const arrHead = ['addNewScheduleDetails'];
    const arrHeadSave = ['ADD NEW WORK-PLAN SCHEDULE DETAILS FILE'];

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
    this.msService.showSuccessWith2Message('Are you sure your want to Submit this Work-Plan(s), That are pending approval?', 'You won\'t be able to add new schedule after submission!',
        // tslint:disable-next-line:max-line-length
        'You can click the \'ADD NEW WORK-PLAN FILE\' button to add another Work Plan', 'YEARLY WORK-PLAN SENT FOR APPROVAL SUCCESSFUL', () => {
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
          this.msService.showSuccess('YEARLY WORK-PLAN SENT FOR APPROVAL SUCCESSFUL');
          this.router.navigate([`/workPlan`]);
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
    if (this.addNewScheduleForm.valid && this.dataSaveResourcesRequiredList.length > 0) {
      this.msService.showSuccessWith2Message('Are you sure your want to Save the Details?', 'You won\'t be able to revert back after submission!',
          // tslint:disable-next-line:max-line-length
          'You can click the \'ADD NEW WORK-PLAN FILE\' button to update details Before Saving', 'COMPLAINT SCHEDULE DETAILS SAVED SUCCESSFUL', () => {
            this.saveWorkPlanScheduled();
          });
    }
  }

  saveWorkPlanScheduled() {
    this.submitted = true;
    if (this.addNewScheduleForm.valid) {
      this.SpinnerService.show();

      this.dataSaveWorkPlan = {...this.dataSaveWorkPlan, ...this.addNewScheduleForm.value};
      this.dataSaveWorkPlan.resourcesRequired = this.dataSaveResourcesRequiredList;
      // this.dataSaveAllWorkPlan.mainDetails =  this.dataSaveWorkPlan;
      // this.dataSaveAllWorkPlan.countyTownDetails =  this.dataCountyTownList;
      // tslint:disable-next-line:max-line-length
      this.msService.msAddWorkPlanScheduleDetails(this.loadedData.createdWorkPlan.referenceNumber, this.dataSaveWorkPlan).subscribe(
          (data: any) => {
            console.log(data);
            this.SpinnerService.hide();
            // this.addNewScheduleForm.reset();
            this.msService.showSuccess('WORK PLAN SCHEDULED DETAILS, SAVED SUCCESSFULLY');
            this.loadData(this.defaultPage, this.defaultPageSize, this.selectedBatchRefNo , this.activeStatus);
          },
          error => {
            this.SpinnerService.hide();
            // this.addNewScheduleForm.reset();
            console.log(error);
            this.msService.showError('AN ERROR OCCURRED');
          },
      );

    }
  }
}
