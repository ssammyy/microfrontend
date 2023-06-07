import {Component, OnInit, QueryList, TemplateRef, ViewChild, ViewChildren} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {NgxSpinnerService} from 'ngx-spinner';
import {MsService} from '../../../../core/store/data/ms/ms.service';
import {Observable, Subject} from 'rxjs';
import {
    AcknowledgementDto,
    ApiResponseModel,
    ComplaintsInvestigationListDto, ComplaintViewSearchValues, ConsumerComplaintsReportViewEntity, ConsumerComplaintViewSearchValues,
    MsDepartment, MsDivisionDetails, MsSeizedGoodsReportViewEntity,
    MsUsersDto, SeizeViewSearchValues
} from '../../../../core/store/data/ms/ms.model';
import {RegionsEntityDto} from "../../../../shared/models/master-data-details";
import {LocalDataSource} from "ng2-smart-table";
import {DataTableDirective} from "angular-datatables";
import {County, CountyService, selectUserInfo, Town, TownService} from '../../../../core/store';
import {Store} from '@ngrx/store';
import {ActivatedRoute, Router} from '@angular/router';




@Component({
  selector: 'app-complaint-monitoring',
  templateUrl: './complaint-monitoring.component.html',
  styleUrls: ['./complaint-monitoring.component.css']
})
export class ComplaintMonitoringComponent implements OnInit {
    @ViewChild('editModal') editModal !: TemplateRef<any>;
    submitted = false;
    selectedCounty = 0;
    selectedTown = 0;
    departmentSelected: 0;
    selectedTownName: string;
    selectedCountyName: string;
    county$: Observable<County[]>;
    town$: Observable<Town[]>;
    loading = false;

    roles: string[];
    searchFormGroup!: FormGroup;
    dtOptions: DataTables.Settings = {};
    dtTrigger1: Subject<any> = new Subject<any>();
    dtTrigger2: Subject<any> = new Subject<any>();
    dtTrigger3: Subject<any> = new Subject<any>();
    @ViewChildren(DataTableDirective)
    dtElements: QueryList<DataTableDirective>;

    searchTypeValue = 'ACKNOWLEDGEMENT';
    endPointStatusValue = 'complaint';
    activeStatus = 'consumerComplaintsView';
    previousStatus = 'consumerComplaintsView';
    selectedBatchRefNo: string;
    searchStatus: any;
    message: any;
    personalTasks = 'false';
    defaultPageSize = 1000;
    defaultPage = 0;
    currentPage = 0;
    currentPageInternal = 0;
    seizeViewSearchValues: SeizeViewSearchValues;
    selectedNotification: AcknowledgementDto;
    loadedData: MsSeizedGoodsReportViewEntity[] = [];
    msOfficerLists!: MsUsersDto[];
    msRegions: RegionsEntityDto[] = [];
    msDepartments: MsDepartment[] = [];
    msDivisions: MsDivisionDetails[] = [];
    totalCount = 12;
    dataSet: LocalDataSource = new LocalDataSource();
    search: Subject<string>;
    sumOfQuantity: number;
    sumOfUnit: number;
    currentLocationOfSeizedGoods: number;
    productsDueForDestruction: number;
    percentageOfProductDueForDestruction: number;
    estimatedCost: number;

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

        this.dtOptions = {
            processing: true,
            dom: 'Bfrtip',
        };

        this.store$.select(selectUserInfo).pipe().subscribe((u) => {
            return this.roles = u.roles;
        });

        this.searchFormGroup = this.formBuilder.group({
            startDate: ['', null],
            endDate: ['', null],
            sector: ['', null],
            brand: ['', null],
            marketCentre: ['', null],
            nameOutlet: ['', null],
            productsDueForDestruction: ['', null],
            officerID: ['', null],
            selectedOfficers: [[], null],
        });

        this.loadData(this.defaultPage, this.defaultPageSize);
    }

    get formSearch(): any {
        return this.searchFormGroup.controls;
    }

    rerender(): void {
        this.dtElements.forEach((dtElement: DataTableDirective) => {
            if (dtElement.dtInstance) {
                dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
                    dtInstance.destroy();
                });
            }
        });
        setTimeout(() => {
            this.dtTrigger1.next();
            this.dtTrigger2.next();
            this.dtTrigger3.next();
        });

    }

    private loadData(page: number, records: number): any {
        this.SpinnerService.show();
        const params = {'personal': this.personalTasks};
        this.msService.loadAllSeizeReportList(String(page), String(records)).subscribe(
            (dataResponse: ApiResponseModel) => {
                if (dataResponse.responseCode === '00') {
                    console.log("Seized goods found successfully");
                    // console.log(dataResponse.data as ConsumerComplaintsReportViewEntity[]);
                    this.loadedData = dataResponse?.data as MsSeizedGoodsReportViewEntity[];
                    this.totalCount = this.loadedData.length;
                    this.calculateSeizedGoodsSummary();
                    this.rerender();
                    this.msService.msOfficerListDetails().subscribe(
                        (dataOfficer: MsUsersDto[]) => {
                            this.msOfficerLists = dataOfficer;
                        },
                    );
                    this.msService.msRegionListDetails().subscribe(
                        (dataRegion: RegionsEntityDto[]) => {
                            this.msRegions = dataRegion;
                        },
                    );
                    this.msService.msDepartmentListDetails().subscribe(
                        (dataDep: MsDepartment[]) => {
                            this.msDepartments = dataDep;
                        },
                    );
                    this.msService.msDivisionListDetails().subscribe(
                        (dataDiv: MsDivisionDetails[]) => {
                            this.msDivisions = dataDiv;
                        },
                    );
                }
                this.SpinnerService.hide();
            },
            error => {
                this.SpinnerService.hide();
                console.log(error);
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

    onSubmitSearch() {
        this.SpinnerService.show();
        this.submitted = true;
        this.seizeViewSearchValues = this.searchFormGroup.value;
        // tslint:disable-next-line:max-line-length
        this.msService.loadSearchSeizeReportViewList(String(this.defaultPage), String(this.defaultPageSize), this.seizeViewSearchValues).subscribe(
            (data: ApiResponseModel) => {
                if (data.responseCode === '00') {
                    this.loadedData = data.data;
                    this.totalCount = this.loadedData.length;
                    this.dataSet.load(this.loadedData);
                    this.calculateSeizedGoodsSummary();
                    this.rerender();
                }
                this.SpinnerService.hide();
            },
            error => {
                this.SpinnerService.hide();
                console.log(error);
            },
        );
    }

    clearSearch() {
        this.searchFormGroup.reset();
        this.submitted = false;
    }

    onChangeSelectedDepartment() {
        this.departmentSelected = this.searchFormGroup?.get('complaintDepartment')?.value;
    }

    toggleStatus(status: string): void {
        this.message = null;
        this.searchStatus = null;
        if (status !== this.activeStatus) {
            this.activeStatus = status;
            this.loadData(this.defaultPage, this.defaultPageSize);
            // this.loadData(this.defaultPage, this.defaultPageSize, this.endPointStatusValue, this.searchTypeValue);
        }
    }

    calculateSeizedGoodsSummary(){
        let arrayOfQuantity = [];
        let arrayOfUnit = [];
        let arrayOfEstimatedCost = [];
        let arrayOfLocationAndSeizedGoods = [];
        let arrayOfProductsDueForDestruction = [];

        for(let i=0; i < this.loadedData.length; i++){

            if(isNaN(Number(this.loadedData[i].quantity))){
                // this.loadedData[i].quantity = '0';
            }else{
                arrayOfQuantity.push(Number(this.loadedData[i].quantity));
            }


            if(isNaN(Number(this.loadedData[i].unit))){
                // this.loadedData[i].unit = '0';
            }else{
                arrayOfUnit.push(Number(this.loadedData[i].unit));
            }


            if(isNaN(Number(this.loadedData[i].estimatedCost))){
                // this.loadedData[i].estimatedCost = '0';
            }else{
                arrayOfEstimatedCost.push(Number(this.loadedData[i].estimatedCost));
            }


            if(this.loadedData[i].productsDueForDestruction == "YES"){
                arrayOfProductsDueForDestruction.push(1);
            }

            if(this.loadedData[i].areProductsDestroyed == '1'){
                this.loadedData[i].areProductsDestroyed = 'YES';
            }else{
                this.loadedData[i].areProductsDestroyed = 'NO'
            }

        }
        this.sumOfQuantity = arrayOfQuantity.reduce((a,b)=> a + b, 0);
        // this.sumOfUnit = arrayOfUnit.reduce((a,b)=> a + b, 0);
        this.estimatedCost = arrayOfEstimatedCost.reduce((a,b)=> a + b, 0);
        // this.currentLocationOfSeizedGoods = arrayOfLocationAndSeizedGoods.reduce((a,b)=> a + b, 0);
        this.productsDueForDestruction = arrayOfProductsDueForDestruction.reduce((a,b)=> a + b, 0);
        this.percentageOfProductDueForDestruction = (this.productsDueForDestruction/this.loadedData.length)*100;

    }
}
