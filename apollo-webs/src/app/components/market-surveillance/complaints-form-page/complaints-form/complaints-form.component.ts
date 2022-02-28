import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {faArrowLeft, faEdit, faTrash} from '@fortawesome/free-solid-svg-icons';
import {MarketSurveillanceService} from '../../../../shared/services/market-surveillance.service';
import {NotificationService} from '../../../../shared/services/notification.service';
import {MasterDataService} from '../../../../shared/services/master-data.service';
import {
  BroadProductCategory,
  Counties,
  Department,
  DivisionDetails,
  ProductCategories,
  Products,
  ProductSubcategory,
  StandardProductCategory,
  Towns
} from '../../../../shared/models/master-data-details';
import {NgxSpinnerService} from 'ngx-spinner';
import {AlertService} from '../../../../shared/services/alert.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-complaints-form',
  templateUrl: './complaints-form.component.html',
  styleUrls: ['./complaints-form.component.css']
})
export class ComplaintsFormComponent implements OnInit {

  isLinear = false;
  page = 1;
  backIcon = faArrowLeft;
  editIcon = faEdit;
  deleteIcon = faTrash;

  public allData: any;

  public scrollAnimation: any;
  public departmentID!: bigint;

  public divisionDetails!: DivisionDetails[];
  public standardProductCategory!: StandardProductCategory[];
  public productCategories!: ProductCategories[];
  public broadProductCategory!: BroadProductCategory[];
  public products!: Products[];
  public productSubcategory!: ProductSubcategory[];
  public counties!: Counties[];
  public towns!: Towns[];
  public department?: Department[];


  selectedStandardProductCategory = 0;
  selectedBroadProductCategory = 0;
  selectedProductCategory = 0;
  selectedProductSubcategory = 0;

  // states = [];
  // cities = [];

  selectedDepartment!: Department;

  stdProductCategoryValue: StandardProductCategory[] = [];
  brdProductCategoryValue: BroadProductCategory[] = [];
  prdCategoryValue: ProductCategories[] = [];
  productValue: ProductCategories[] = [];
  prdSubcategoryValue: ProductSubcategory[] = [];

  countyValue: Counties[] = [];
  townValue: Towns[] = [];

  submitted = false;
  loading = false;

  constructor(private formBuilder: FormBuilder,
              private spinner: NgxSpinnerService,
              private router: Router,
              private notificationService: NotificationService,
              private masterDataService: MasterDataService,
              private alertService: AlertService,
              private marketSurveillanceService: MarketSurveillanceService
  ) {
  }

  public firstFormGroup!: FormGroup;
  public secondFormGroup!: FormGroup;
  public thirdFormGroup!: FormGroup;

  ngOnInit(): void {
    this.getALlRecommendedDetails();

    this.firstFormGroup = this.formBuilder.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      emailAddress: ['', Validators.required],
      phoneNumber: ['', Validators.required],
      postalAddress: ['', [Validators.required]]
      // postalAddress: ['', [Validators.required, Validators.pattern('P.O.BOX [0-9]{5}')]]
    });
    this.secondFormGroup = this.formBuilder.group({
      complaintCategory: ['', Validators.required],
      complaintTitle: ['', Validators.required],
      productClassification: ['', Validators.required],
      broadProductCategory: ['', Validators.required],
      productCategory: ['', Validators.required],
      myProduct: ['', Validators.required],
      productSubcategory: ['', Validators.required],
      // productName: ['', Validators.required],
      productBrand: ['', Validators.required],
      complaintDescription: ['', Validators.required]
    });
    this.thirdFormGroup = this.formBuilder.group({
      county: ['', Validators.required],
      town: ['', Validators.required],
      marketCenter: ['', Validators.required],
      buildingName: ['', Validators.required],
    });

  }

  get formFirstGroupForm(): any {
    return this.firstFormGroup.controls;
  }

  get formSecondGroupForm(): any {
    return this.secondFormGroup.controls;
  }

  get formThirdGroupForm(): any {
    return this.thirdFormGroup.controls;
  }


  onSelectStandardProductCategory(stdID: number): any {
    this.brdProductCategoryValue = this.broadProductCategory.filter((item) => {
      return item.divisionId === Number(stdID);
    });
  }

  onSelectBroadProductCategory(brdID: number): any {
    this.prdCategoryValue = this.productCategories.filter((item) => {
      return item.broadProductCategoryId === Number(brdID);
    });
  }

  onSelectProductCategory(prdCatID: number): any {
    this.productValue = this.products.filter((item) => {
      return item.productCategoryId === Number(prdCatID);
    });
  }

  onSelectProduct(prdID: number): any {
    this.prdSubcategoryValue = this.productSubcategory.filter((item) => {
      return item.productId === Number(prdID);
    });
  }

  onSelectCounty(countyID: number): any {
    this.townValue = this.towns.filter((item) => {
      return item.countyId === Number(countyID);
    });
  }

  public getALlRecommendedDetails(): void {
    this.masterDataService.loadDepartments().subscribe(
      (data: any) => {
        this.department = data;
        console.log(data);

      },
      // tslint:disable-next-line:max-line-length
      (error: { error: { message: any; }; }) => {
        this.notificationService.showError(error.error.message, 'Access Denied');
        this.spinner.hide();
      }
    );

    this.masterDataService.loadDivisions().subscribe(
      (data: any) => {
        this.divisionDetails = data;
        console.log(data);

      },
      // tslint:disable-next-line:max-line-length
      (error: { error: { message: any; }; }) => {
        this.notificationService.showError(error.error.message, 'Access Denied');
        this.spinner.hide();
      }
    );

    this.masterDataService.loadProductSubcategory().subscribe(
      (data: any) => {
        this.productSubcategory = data;
        console.log(data);

      },
      // tslint:disable-next-line:max-line-length
      (error: { error: { message: any; }; }) => {
        this.notificationService.showError(error.error.message, 'Access Denied');
        this.spinner.hide();
      }
    );

    this.masterDataService.loadProducts().subscribe(
      (data: any) => {
        this.products = data;
        console.log(data);

      },
      // tslint:disable-next-line:max-line-length
      (error: { error: { message: any; }; }) => {
        this.notificationService.showError(error.error.message, 'Access Denied');
        this.spinner.hide();
      }
    );

    this.masterDataService.loadBroadProductCategory().subscribe(
      (data: any) => {
        this.broadProductCategory = data;
        console.log(data);

      },
      // tslint:disable-next-line:max-line-length
      (error: { error: { message: any; }; }) => {
        this.notificationService.showError(error.error.message, 'Access Denied');
        this.spinner.hide();
      }
    );

    this.masterDataService.loadProductCategories().subscribe(
      (data: any) => {
        this.productCategories = data;
        console.log(data);

      },
      // tslint:disable-next-line:max-line-length
      (error: { error: { message: any; }; }) => {
        this.notificationService.showError(error.error.message, 'Access Denied');
        this.spinner.hide();
      }
    );

    this.masterDataService.loadStandardProductCategory().subscribe(
      (data: any) => {
        this.standardProductCategory = data;
        console.log(data);

      },
      // tslint:disable-next-line:max-line-length
      (error: { error: { message: any; }; }) => {
        this.notificationService.showError(error.error.message, 'Access Denied');
        this.spinner.hide();
      }
    );


    this.masterDataService.loadCounties().subscribe(
      (data: any) => {
        this.counties = data;
        console.log(data);

      },
      // tslint:disable-next-line:max-line-length
      (error: { error: { message: any; }; }) => {
        this.notificationService.showError(error.error.message, 'Access Denied');
        this.spinner.hide();
      }
    );

    this.masterDataService.loadTowns().subscribe(
      (data: any) => {
        this.towns = data;
        console.log(data);

      },
      // tslint:disable-next-line:max-line-length
      (error: { error: { message: any; }; }) => {
        this.notificationService.showError(error.error.message, 'Access Denied');
        this.spinner.hide();
      }
    );
  }

  onClickNext(valid: boolean): void {
    // console.log('hI');
    if (valid) {
      this.page += 1;
      this.scrollToTop();
    }
    console.log(this.page);
  }

  onClickBack(valid: boolean): void {
    // console.log('hI');
    if (valid) {
      this.page -= 1;
      this.scrollToTop();
    }
    console.log(this.page);
  }

  onSubmit(): any {
    this.submitted = true;

    // reset alerts on submit
    this.alertService.clear();

    // stop here if form is invalid
    if (this.firstFormGroup.invalid) {
      return;
    }

    // stop here if form is invalid
    if (this.secondFormGroup.invalid) {
      return;
    }

    // stop here if form is invalid
    if (this.thirdFormGroup.invalid) {
      return;
    }

    // this.allData = {this?.firstFormGroup.value, this.secondFormGroup.value, this.thirdFormGroup.value}
    // console.log(allData);

    this.loading = true;
    this.marketSurveillanceService.postMSComplaint(this.firstFormGroup.value, this.secondFormGroup.value, this.thirdFormGroup.value)
      .subscribe(
        (data: any) => {
          // this.allComplaintsData = data;
          console.log(data);
          this.router.navigate(['/dashboard']);
        }
      );
  }

  public scrollToTop(): void {
    const form = document.getElementById('form-div');

    // @ts-ignore
    const position = form.pageYOffset || form.scrollTop;
    if (position === 0) {
      console.log(-Math.max(1, Math.floor(position / 10)));
      // @ts-ignore
      form.scrollBy(0, -Math.max(1, Math.floor(position / 10)));
      this.scrollAnimation = setTimeout(this.scrollToTop, 30);
    } else {
      clearTimeout(this.scrollAnimation);
    }
  }

}
