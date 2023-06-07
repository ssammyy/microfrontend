import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {RegionDto, ReportsPermitEntityDto, SectionDto, StatusesDto} from "../../../../core/store/data/qa/qa.model";
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {ApiEndpointService} from "../../../../core/services/endpoints/api-endpoint.service";
import {QaService} from "../../../../core/store/data/qa/qa.service";
import {Router} from "@angular/router";
import {LoadingService} from "../../../../core/services/loader/loadingservice.service";
import {NgxSpinnerService} from "ngx-spinner";
import {HttpErrorResponse} from "@angular/common/http";
import {DatePipe, formatDate} from "@angular/common";
import {DateAdapter, MAT_DATE_FORMATS, NativeDateAdapter} from "@angular/material/core";
import {UserEntityDto} from "../../../../core/store";
import {FormBuilder, FormGroup, NgForm} from "@angular/forms";
import {MatDateRangePicker} from "@angular/material/datepicker";
import {Moment} from "moment";
import {OverlayService} from "../../../../shared/loader/overlay.service";
import swal from "sweetalert2";

export const PICK_FORMATS = {
    parse: {dateInput: {month: 'short', year: 'numeric', day: 'numeric'}},
    display: {
        dateInput: 'input',
        monthYearLabel: {year: 'numeric', month: 'short'},
        dateA11yLabel: {year: 'numeric', month: 'long', day: 'numeric'},
        monthYearA11yLabel: {year: 'numeric', month: 'long'}
    }
};

class PickDateAdapter extends NativeDateAdapter {
    format(date: Date, displayFormat: Object): string {
        if (displayFormat === 'input') {
            return formatDate(date, "yyyy-MM-dd'T'HH:mm:ss", this.locale);
        } else {
            return date.toDateString();
        }
    }
}


@Component({
    selector: 'app-permits-deferred',
    templateUrl: './permits-deferred.component.html',
    styleUrls: ['./permits-deferred.component.css'],
    providers: [
        {provide: DateAdapter, useClass: PickDateAdapter},
        {provide: MAT_DATE_FORMATS, useValue: PICK_FORMATS}
    ]
})
export class PermitsDeferredComponent implements OnInit {

    public allDMarkPermitData: ReportsPermitEntityDto[];
    public allSMarkPermitData: ReportsPermitEntityDto[];
    public allFMarkPermitData: ReportsPermitEntityDto[];
    dtOptions: DataTables.Settings = {};
    @ViewChildren(DataTableDirective)
    dtElements: QueryList<DataTableDirective>;
    dtTrigger1: Subject<any> = new Subject<any>();
    dtTrigger2: Subject<any> = new Subject<any>();
    dtTrigger3: Subject<any> = new Subject<any>();


    displayUsers: boolean = false;
    dMarksRetrievedData: boolean = false;
    sMarksRetrievedData: boolean = false;
    fMarksRetrievedData: boolean = false;

    dateFormat = "yyyy-MM-dd";
    language = "en";
    dMarkID = ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.DMARK_TYPE_ID;
    sMarkID = ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.SMARK_TYPE_ID;
    fMarkID = ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.FMARK_TYPE_ID;
    dMarksRetrieved: number = 0;
    sMarksRetrieved: number = 0;
    fMarksRetrieved: number = 0;
    sumDMarkAmountPermit: number = 0;
    sumFMarkAmountPermit: number = 0;
    sumSMarkAmountPermit: number = 0;
    dMarkPermitsAwarded: number = 0;
    dMarkPermitsRejected: number = 0;
    sMarkPermitsAwarded: number = 0;
    sMarkPermitsRejected: number = 0;
    fMarkPermitsAwarded: number = 0;
    fMarkPermitsRejected: number = 0;

    sections: SectionDto[];
    regions: RegionDto[];
    users: UserEntityDto[];
    statuses: StatusesDto[];


    filterFormGroup: FormGroup;
    filterFormGroupSMark: FormGroup;
    filterFormGroupFMark: FormGroup;


    today: any;
    error: boolean;
    errorFMark: boolean;
    errorSMark: boolean;

    loadingText: string;

    arr: string[] = [];
    arrSMark: string[] = [];
    arrFMark: string[] = [];

    filterApplied: boolean;
    filterAppliedSMark: boolean;
    filterAppliedFMark: boolean;

    @ViewChild('dateRangeInput ') dateRangeInput: MatDateRangePicker<Moment>;
    private pickerDR: MatDateRangePicker<Moment>;
    @ViewChild('formDirective') private formDirective: NgForm;

    startDate: any = '';
    endDate: any = '';

    startDateSMark: any = '';
    endDateSMark: any = '';

    startDateFMark: any = '';
    endDateFMark: any = '';

    startDateB: any = '';
    endDateB: any = '';
    @ViewChild('dateRangeStart') startDateRef: ElementRef;
    @ViewChild('dateRangeEnd') endDateRef: ElementRef;
    @ViewChild('dateRangeStartSMark') startDateRefSMark: ElementRef;
    @ViewChild('dateRangeEndSMark') endDateRefSMark: ElementRef;
    @ViewChild('dateRangeStartFMark') startDateRefFMark: ElementRef;
    @ViewChild('dateRangeEndFMark') endDateRefFMark: ElementRef;


    constructor(private qaService: QaService,
                private router: Router,
                private _loading: LoadingService,
                private SpinnerService: NgxSpinnerService,
                private formBuilder: FormBuilder,
                private datePipe: DatePipe,
                private spinnerService: OverlayService) {
    }

    ngOnInit(): void {
        this.today = new Date();
        this.filterApplied = false
        this.filterAppliedSMark = false
        this.filterAppliedFMark = false
        this.dtOptions = {

            processing: true,
            dom: 'Bfrtip'
        };
        this.formInit()
        this.sMarkFormInit()
        this.fMarkFormInit()
        this.getAllDMarkApplicationsDejected()
        this.getAllFMarkApplicationsDejected()
        this.getAllSMarkApplicationsDejected()
        this.loadAllSections()
        this.loadAllRegions()
        this.loadAllStatuses()
        this.loadAllOfficers()

    }


    public getAllDMarkApplicationsDejected(): void {
        this.SpinnerService.show();
        this.qaService.loadPermitDejectedReports(String(this.dMarkID)).subscribe(
            (response: ReportsPermitEntityDto[]) => {
                this.allDMarkPermitData = response;
                this.rerender()
                // this.SpinnerService.hide();
                // this.displayUsers = true;
                this.dMarksRetrieved = this.allDMarkPermitData.length
                this.dMarksRetrievedData = true;
                this.sumDMarkAmountPermit = response.filter(item => item.invoiceAmount)
                    .reduce((sum, current) => sum + current.invoiceAmount, 0);
                this.dMarkPermitsAwarded = response.filter(item => item.permitAwardStatus == true).length
                this.dMarkPermitsRejected = response.filter(item => item.permitAwardStatus == false).length

            },
            (error: HttpErrorResponse) => {
                alert(error.message);
                this.displayUsers = true;
                this.dMarksRetrievedData = true;
                this.SpinnerService.hide();
            }
        );

    }

    public getAllSMarkApplicationsDejected(): void {
        this.qaService.loadPermitDejectedReports(String(this.sMarkID)).subscribe(
            (response: ReportsPermitEntityDto[]) => {
                this.allSMarkPermitData = response;
                this.rerender()
                this.displayUsers = true;
                this.sMarksRetrieved = this.allSMarkPermitData.length
                this.sMarksRetrievedData = true;
                this.sumSMarkAmountPermit = response.filter(item => item.invoiceAmount)
                    .reduce((sum, current) => sum + current.invoiceAmount, 0);
                this.sMarkPermitsAwarded = response.filter(item => item.permitAwardStatus == true).length
                this.sMarkPermitsRejected = response.filter(item => item.permitAwardStatus == false).length
                this.SpinnerService.hide();


            },
            (error: HttpErrorResponse) => {
                alert(error.message);
                this.displayUsers = true;
                this.sMarksRetrievedData = true;
                this.SpinnerService.hide();
            }
        );

    }

    public getAllFMarkApplicationsDejected(): void {
        this.qaService.loadPermitDejectedReports(String(this.fMarkID)).subscribe(
            (response: ReportsPermitEntityDto[]) => {
                this.allFMarkPermitData = response;
                this.rerender()
                // this.SpinnerService.hide();
                this.fMarksRetrieved = this.allFMarkPermitData.length
                this.fMarksRetrievedData = true;
                // this.displayUsers = true;
                this.sumFMarkAmountPermit = response.filter(item => item.invoiceAmount)
                    .reduce((sum, current) => sum + current.invoiceAmount, 0);
                this.fMarkPermitsAwarded = response.filter(item => item.permitAwardStatus == true).length
                this.fMarkPermitsRejected = response.filter(item => item.permitAwardStatus == false).length

            },
            (error: HttpErrorResponse) => {
                alert(error.message);
                this.displayUsers = true;
                this.fMarksRetrievedData = true;
                this.SpinnerService.hide();
            }
        );
    }

    rerender(): void {
        this.dtElements.forEach((dtElement: DataTableDirective) => {
            if (dtElement.dtInstance)
                dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
                    dtInstance.destroy();
                });
        });
        setTimeout(() => {
            this.dtTrigger1.next();
            this.dtTrigger2.next();
            this.dtTrigger3.next();

        });
    }


    ngOnDestroy(): void {
        // Do not forget to unsubscribe the event
        this.dtTrigger1.unsubscribe();
        this.dtTrigger2.unsubscribe();
        this.dtTrigger3.unsubscribe();


    }

    formatFormDate(date: string) {
        return formatDate(date, this.dateFormat, this.language);
    }

    calculateDiff(date1, date2) {

        let todayDate = new Date(date1);
        let sentOnDate = new Date(date2);
        sentOnDate.setDate(sentOnDate.getDate());
        let differenceInTime = todayDate.getTime() - sentOnDate.getTime();
        // To calculate the no. of days between two dates
        return Math.floor(differenceInTime / (1000 * 3600 * 24));
    }
    public loadAllSections(): void {
        this.qaService.loadSectionList().subscribe(
            (data: any) => {
                this.sections = data;
            }
        );
    }

    public loadAllRegions(): void {
        this.qaService.loadRegionList().subscribe(
            (data: any) => {
                this.regions = data;
            }
        );
    }

    public loadAllStatuses(): void {
        this.qaService.loadStatuses().subscribe(
            (data: any) => {
                this.statuses = data;
            }
        );
    }

    public loadAllOfficers(): void {
        this.qaService.loadOfficers().subscribe(
            (data: any) => {
                this.users = data;
            }
        );
    }

    //DMARK FILTERS
    applyFilter(formDirective): void {
        this.spinnerService.show("Filtering Data")
        this.error = false;
        //let us do validation
        if (this.filterFormGroup.get("regionId").value == '' &&
            this.filterFormGroup.get("sectionId").value == '' &&
            this.filterFormGroup.get("statusId").value == '' &&
            this.filterFormGroup.get("officerId").value == '' &&
            this.filterFormGroup.get("category").value == '' &&
            this.filterFormGroup.get("productDescription").value == '' &&
            this.filterFormGroup.get("commenceDate").value == '') {
            this.error = true;
            this.spinnerService.hide();

            swal.fire({
                title: 'Please Select At Least One Filter.',
                buttonsStyling: false,
                customClass: {
                    confirmButton: 'btn btn-warning form-wizard-next-btn ',
                },
                icon: 'warning'
            });

        }
        if (this.filterFormGroup.get("commenceDate").value != '') {
            if (this.filterFormGroup.get("lastDate").value == '') {
                this.spinnerService.hide();

                swal.fire({
                    title: 'Please Select The End Date.',
                    buttonsStyling: false,
                    customClass: {
                        confirmButton: 'btn btn-warning form-wizard-next-btn ',
                    },
                    icon: 'warning'
                });
                this.error = true;

            }

        }
        if (!this.error) {
            this.arr = []
            if (this.filterFormGroup.get("regionId").value != '') {
                const regionId = parseInt(this.filterFormGroup.get("regionId").value);
                const regionName = this.regions.find(x => x.id == regionId).region;
                this.arr.push("Region: " + regionName)
            }
            if (this.filterFormGroup.get("sectionId").value != '') {
                const sectionId = parseInt(this.filterFormGroup.get("sectionId").value);
                const sectionName = this.sections.find(x => x.id == sectionId).section;
                this.arr.push("Section: " + sectionName)
            }
            if (this.filterFormGroup.get("statusId").value != '') {
                const statusId = parseInt(this.filterFormGroup.get("statusId").value);
                const statusName = this.statuses.find(x => x.id == statusId).processStatusName;
                this.arr.push("Status: " + statusName)
            }
            if (this.filterFormGroup.get("category").value != '') {
                if (this.filterFormGroup.get("category").value == 'Large') {
                    this.arr.push("Category: Large")
                } else if (this.filterFormGroup.get("category").value == 'Medium') {
                    this.arr.push("Category: Medium")
                } else if (this.filterFormGroup.get("category").value == 'Small') {
                    this.arr.push("Category: Small")
                }
            }
            if (this.filterFormGroup.get("officerId").value != '') {
                const officerId = parseInt(this.filterFormGroup.get("officerId").value);
                const officerName = this.users.find(x => x.id == officerId).firstName;
                this.arr.push("Allocated Officer: " + officerName)

            }
            if (this.filterFormGroup.get("commenceDate").value != '') {
                this.arr.push("Application Date: " + this.formatFormDate(this.filterFormGroup.get("commenceDate").value)
                    + " To " + this.formatFormDate(this.filterFormGroup.get("commenceDate").value))

            }
            if (this.filterFormGroup.get("productDescription").value != '') {
                this.arr.push("Product Description: " + this.filterFormGroup.get("productDescription").value)
            }

            this.qaService.applyFilterDejected(this.filterFormGroup.value).subscribe(
                (response: ReportsPermitEntityDto[]) => {
                    this.spinnerService.hide();
                    this.allDMarkPermitData = response;
                    this.rerender()
                    this.SpinnerService.hide();
                    this.resetForm()
                    this.filterApplied = true

                    this.dMarksRetrieved = this.allDMarkPermitData.length
                    this.dMarksRetrievedData = true;
                    this.displayUsers = true;
                    this.sumDMarkAmountPermit = response.filter(item => item.invoiceAmount)
                        .reduce((sum, current) => sum + current.invoiceAmount, 0);
                    this.dMarkPermitsAwarded = response.filter(item => item.permitAwardStatus == true).length
                    this.dMarkPermitsRejected = response.filter(item => item.permitAwardStatus == false).length

                },
                (error: HttpErrorResponse) => {
                    alert(error.message);
                    this.displayUsers = true;
                    this.dMarksRetrievedData = true;
                    this.spinnerService.hide();
                }
            );
        }


    }

    resetForm() {
        this.filterFormGroup.reset()
        this.formDirective.resetForm();
        this.filterFormGroup.markAsPristine()
        this.startDateRef.nativeElement.value = '';
        this.endDateRef.nativeElement.value = '';
        this.formInit()

    }

    clearFilter() {
        this.filterApplied = false
        this.getAllDMarkApplicationsDejected()
    }

    formInit() {
        this.filterFormGroup = this.formBuilder.group({
            regionId: '',
            sectionId: '',
            statusId: '',
            officerId: '',
            category: '',
            productDescription: '',
            commenceDate: '',
            lastDate: '',
            permitType: '1'

        });
    }

    dateRangeChange(dateRangeStart: HTMLInputElement, dateRangeEnd: HTMLInputElement) {
        // console.log(dateRangeStart.value);
        // console.log(dateRangeEnd.value);
        this.startDate = dateRangeStart.value
        this.endDate = dateRangeEnd.value

    }

    //SMARK FILTERS
    sMarkFormInit() {
        this.filterFormGroupSMark = this.formBuilder.group({
            regionId: '',
            sectionId: '',
            statusId: '',
            officerId: '',
            category: '',
            productDescription: '',
            commenceDate: '',
            lastDate: '',
            permitType: '2'

        });
    }

    resetFormSMark() {
        this.filterFormGroupSMark.reset()
        this.startDateRefSMark.nativeElement.value = '';
        this.endDateRefSMark.nativeElement.value = '';
        this.sMarkFormInit()

    }

    clearFilterSMark() {
        this.filterAppliedSMark = false
        this.getAllSMarkApplicationsDejected()
    }

    dateRangeChangeSMark(dateRangeStartSMark: HTMLInputElement, dateRangeEndSMark: HTMLInputElement) {
        // console.log(dateRangeStart.value);
        // console.log(dateRangeEnd.value);
        this.startDateSMark = dateRangeStartSMark.value
        this.endDateSMark = dateRangeEndSMark.value

    }

    applyFilterSmark(formDirective): void {
        this.spinnerService.show("Filtering Data")
        this.errorSMark = false;
        console.log(this.filterFormGroupSMark.value)
        //let us do validation
        if (this.filterFormGroupSMark.get("regionId").value == '' &&
            this.filterFormGroupSMark.get("sectionId").value == '' &&
            this.filterFormGroupSMark.get("statusId").value == '' &&
            this.filterFormGroupSMark.get("officerId").value == '' &&
            this.filterFormGroupSMark.get("category").value == '' &&
            this.filterFormGroupSMark.get("productDescription").value == '' &&
            this.filterFormGroupSMark.get("commenceDate").value == '') {
            this.errorSMark = true;
            this.spinnerService.hide();

            swal.fire({
                title: 'Please Select At Least One Filter.',
                buttonsStyling: false,
                customClass: {
                    confirmButton: 'btn btn-warning form-wizard-next-btn ',
                },
                icon: 'warning'
            });

        }
        if (this.filterFormGroupSMark.get("commenceDate").value != '') {
            if (this.filterFormGroupSMark.get("lastDate").value == '') {
                this.spinnerService.hide();

                swal.fire({
                    title: 'Please Select The End Date.',
                    buttonsStyling: false,
                    customClass: {
                        confirmButton: 'btn btn-warning form-wizard-next-btn ',
                    },
                    icon: 'warning'
                });
                this.errorSMark = true;

            }

        }
        if (!this.errorSMark) {
            this.arrSMark = []
            if (this.filterFormGroupSMark.get("regionId").value != '') {
                const regionId = parseInt(this.filterFormGroupSMark.get("regionId").value);
                const regionName = this.regions.find(x => x.id == regionId).region;
                this.arrSMark.push("Region: " + regionName)
            }
            if (this.filterFormGroupSMark.get("sectionId").value != '') {
                const sectionId = parseInt(this.filterFormGroupSMark.get("sectionId").value);
                const sectionName = this.sections.find(x => x.id == sectionId).section;
                this.arrSMark.push("Section: " + sectionName)
            }
            if (this.filterFormGroupSMark.get("statusId").value != '') {
                const statusId = parseInt(this.filterFormGroupSMark.get("statusId").value);
                const statusName = this.statuses.find(x => x.id == statusId).processStatusName;
                this.arrSMark.push("Status: " + statusName)
            }
            if (this.filterFormGroupSMark.get("category").value != '') {
                if (this.filterFormGroupSMark.get("category").value == 'Large') {
                    this.arrSMark.push("Category: Large")
                } else if (this.filterFormGroupSMark.get("category").value == 'Medium') {
                    this.arrSMark.push("Category: Medium")
                } else if (this.filterFormGroupSMark.get("category").value == 'Small') {
                    this.arrSMark.push("Category: Small")
                }
            }
            if (this.filterFormGroupSMark.get("officerId").value != '') {
                const officerId = parseInt(this.filterFormGroupSMark.get("officerId").value);
                const officerName = this.users.find(x => x.id == officerId).firstName;
                this.arrSMark.push("Allocated Officer: " + officerName)

            }
            if (this.filterFormGroupSMark.get("commenceDate").value != '') {
                this.arrSMark.push("Application Date: " + this.formatFormDate(this.filterFormGroupSMark.get("commenceDate").value)
                    + " To " + this.formatFormDate(this.filterFormGroupSMark.get("commenceDate").value))

            }
            if (this.filterFormGroupSMark.get("productDescription").value != '') {
                this.arrSMark.push("Product Description: " + this.filterFormGroupSMark.get("productDescription").value)
            }

            this.qaService.applyFilter(this.filterFormGroupSMark.value).subscribe(
                (response: ReportsPermitEntityDto[]) => {
                    this.spinnerService.hide();
                    this.allSMarkPermitData = response;
                    this.rerender()
                    this.SpinnerService.hide();
                    this.resetFormSMark()
                    this.filterAppliedSMark = true
                    this.sMarksRetrieved = this.allSMarkPermitData.length
                    this.sMarksRetrievedData = true;
                    this.sumSMarkAmountPermit = response.filter(item => item.invoiceAmount)
                        .reduce((sum, current) => sum + current.invoiceAmount, 0);
                    this.sMarkPermitsAwarded = response.filter(item => item.permitAwardStatus == true).length
                    this.sMarkPermitsRejected = response.filter(item => item.permitAwardStatus == false).length

                },
                (error: HttpErrorResponse) => {
                    alert(error.message);
                    this.displayUsers = true;
                    this.sMarksRetrievedData = true;
                    this.spinnerService.hide();
                }
            );
        }


    }

    //FMARK FILTERS
    fMarkFormInit() {
        this.filterFormGroupFMark = this.formBuilder.group({
            regionId: '',
            sectionId: '',
            statusId: '',
            officerId: '',
            category: '',
            productDescription: '',
            commenceDate: '',
            lastDate: '',
            permitType: '3'

        });
    }

    resetFormFMark() {
        this.filterFormGroupFMark.reset()
        this.startDateRefFMark.nativeElement.value = '';
        this.endDateRefFMark.nativeElement.value = '';
        this.fMarkFormInit()

    }

    clearFilterFMark() {
        this.filterAppliedFMark = false
        this.getAllFMarkApplicationsDejected()
    }

    dateRangeChangeFMark(dateRangeStartFMark: HTMLInputElement, dateRangeEndFMark: HTMLInputElement) {
        // console.log(dateRangeStart.value);
        // console.log(dateRangeEnd.value);
        this.startDateFMark = dateRangeStartFMark.value
        this.endDateFMark = dateRangeEndFMark.value

    }

    applyFilterFmark(formDirective): void {
        this.spinnerService.show("Filtering Data")
        this.errorFMark = false;
        console.log(this.filterFormGroupFMark.value)
        //let us do validation
        if (this.filterFormGroupFMark.get("regionId").value == '' &&
            this.filterFormGroupFMark.get("sectionId").value == '' &&
            this.filterFormGroupFMark.get("statusId").value == '' &&
            this.filterFormGroupFMark.get("officerId").value == '' &&
            this.filterFormGroupFMark.get("category").value == '' &&
            this.filterFormGroupFMark.get("productDescription").value == '' &&
            this.filterFormGroupFMark.get("commenceDate").value == '') {
            this.errorSMark = true;
            this.spinnerService.hide();

            swal.fire({
                title: 'Please Select At Least One Filter.',
                buttonsStyling: false,
                customClass: {
                    confirmButton: 'btn btn-warning form-wizard-next-btn ',
                },
                icon: 'warning'
            });

        }
        if (this.filterFormGroupFMark.get("commenceDate").value != '') {
            if (this.filterFormGroupFMark.get("lastDate").value == '') {
                this.spinnerService.hide();

                swal.fire({
                    title: 'Please Select The End Date.',
                    buttonsStyling: false,
                    customClass: {
                        confirmButton: 'btn btn-warning form-wizard-next-btn ',
                    },
                    icon: 'warning'
                });
                this.errorFMark = true;

            }

        }
        if (!this.errorFMark) {
            this.arrFMark = []
            if (this.filterFormGroupFMark.get("regionId").value != '') {
                const regionId = parseInt(this.filterFormGroupFMark.get("regionId").value);
                const regionName = this.regions.find(x => x.id == regionId).region;
                this.arrFMark.push("Region: " + regionName)
            }
            if (this.filterFormGroupFMark.get("sectionId").value != '') {
                const sectionId = parseInt(this.filterFormGroupFMark.get("sectionId").value);
                const sectionName = this.sections.find(x => x.id == sectionId).section;
                this.arrFMark.push("Section: " + sectionName)
            }
            if (this.filterFormGroupFMark.get("statusId").value != '') {
                const statusId = parseInt(this.filterFormGroupFMark.get("statusId").value);
                const statusName = this.statuses.find(x => x.id == statusId).processStatusName;
                this.arrFMark.push("Status: " + statusName)
            }
            if (this.filterFormGroupFMark.get("category").value != '') {
                if (this.filterFormGroupFMark.get("category").value == 'Large') {
                    this.arrFMark.push("Category: Large")
                } else if (this.filterFormGroupFMark.get("category").value == 'Medium') {
                    this.arrFMark.push("Category: Medium")
                } else if (this.filterFormGroupFMark.get("category").value == 'Small') {
                    this.arrFMark.push("Category: Small")
                }
            }
            if (this.filterFormGroupFMark.get("officerId").value != '') {
                const officerId = parseInt(this.filterFormGroupFMark.get("officerId").value);
                const officerName = this.users.find(x => x.id == officerId).firstName;
                this.arrFMark.push("Allocated Officer: " + officerName)

            }
            if (this.filterFormGroupFMark.get("commenceDate").value != '') {
                this.arrFMark.push("Application Date: " + this.formatFormDate(this.filterFormGroupFMark.get("commenceDate").value)
                    + " To " + this.formatFormDate(this.filterFormGroupFMark.get("commenceDate").value))

            }
            if (this.filterFormGroupFMark.get("productDescription").value != '') {
                this.arrFMark.push("Product Description: " + this.filterFormGroupFMark.get("productDescription").value)
            }

            this.qaService.applyFilter(this.filterFormGroupFMark.value).subscribe(
                (response: ReportsPermitEntityDto[]) => {
                    this.spinnerService.hide();
                    this.allFMarkPermitData = response;
                    this.rerender()
                    this.SpinnerService.hide();
                    this.resetFormFMark()
                    this.filterAppliedFMark = true
                    this.fMarksRetrieved = this.allFMarkPermitData.length
                    this.fMarksRetrievedData = true;
                    this.sumFMarkAmountPermit = response.filter(item => item.invoiceAmount)
                        .reduce((sum, current) => sum + current.invoiceAmount, 0);
                    this.fMarkPermitsAwarded = response.filter(item => item.permitAwardStatus == true).length
                    this.fMarkPermitsRejected = response.filter(item => item.permitAwardStatus == false).length

                },
                (error: HttpErrorResponse) => {
                    alert(error.message);
                    this.displayUsers = true;
                    this.fMarksRetrievedData = true;
                    this.spinnerService.hide();
                }
            );
        }


    }


}
