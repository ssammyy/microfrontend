import {Component, OnInit, QueryList, ViewChildren} from '@angular/core';
import {RegionDto, ReportsPermitEntityDto, SectionDto, StatusesDto} from "../../../../core/store/data/qa/qa.model";
import {ApiEndpointService} from "../../../../core/services/endpoints/api-endpoint.service";
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {QaService} from "../../../../core/store/data/qa/qa.service";
import {Router} from "@angular/router";
import {LoadingService} from "../../../../core/services/loader/loadingservice.service";
import {NgxSpinnerService} from "ngx-spinner";
import {HttpErrorResponse} from "@angular/common/http";
import {DatePipe, formatDate} from "@angular/common";
import {FormBuilder, FormGroup} from "@angular/forms";
import {UserEntityDto} from "../../../../core/store";
import {DateAdapter, MAT_DATE_FORMATS, NativeDateAdapter} from "@angular/material/core";
import swal from "sweetalert2";
import {OverlayService} from "../../../../shared/loader/overlay.service";

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
            return formatDate(date, 'yyyy-mm-dd hh:mm:ss', this.locale);
        } else {
            return date.toDateString();
        }
    }
}

@Component({
    selector: 'app-applications-received',
    templateUrl: './applications-received.component.html',
    styleUrls: ['./applications-received.component.css'],
    providers: [
        {provide: DateAdapter, useClass: PickDateAdapter},
        {provide: MAT_DATE_FORMATS, useValue: PICK_FORMATS}
    ]
})
export class ApplicationsReceivedComponent implements OnInit {
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
    today: any;
    error: boolean;
    loadingText: string;


    constructor(private qaService: QaService,
                private formBuilder: FormBuilder,
                private router: Router,
                private _loading: LoadingService,
                private datePipe: DatePipe,
                private SpinnerService: NgxSpinnerService,
                private spinnerService: OverlayService
    ) {


    }

    ngOnInit(): void {
        this.today = new Date();

        this.dtOptions = {

            processing: true,
            dom: 'Bfrtip'
        };
        this.getAllDMarkApplicationsReceived()
        this.getAllFMarkApplicationsReceived()
        this.getAllSMarkApplicationsReceived()
        this.loadAllSections()
        this.loadAllRegions()
        this.loadAllStatuses()
        this.loadAllOfficers()

        this.filterFormGroup = this.formBuilder.group({
            regionId: '',
            sectionId: '',
            statusId: '',
            officerId: '',
            category: '',
            productDescription: '',
            start: '',
            end: '',
            permitType: ''

        });


    }


    public getAllDMarkApplicationsReceived(): void {
        this.SpinnerService.show();
        this.qaService.loadPermitReports(String(this.dMarkID)).subscribe(
            (response: ReportsPermitEntityDto[]) => {
                this.allDMarkPermitData = response;
                this.rerender()
                // this.SpinnerService.hide();
                this.displayUsers = true;
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

    public getAllSMarkApplicationsReceived(): void {
        this.qaService.loadPermitReports(String(this.sMarkID)).subscribe(
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

    public getAllFMarkApplicationsReceived(): void {
        this.qaService.loadPermitReports(String(this.fMarkID)).subscribe(
            (response: ReportsPermitEntityDto[]) => {
                this.allFMarkPermitData = response;
                this.rerender()
                // this.SpinnerService.hide();
                this.fMarksRetrieved = this.allFMarkPermitData.length
                this.fMarksRetrievedData = true;
                this.displayUsers = true;
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
            this.filterFormGroup.get("start").value == '' &&
            this.filterFormGroup.get("end").value == '') {
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
        if (this.filterFormGroup.get("start").value != '') {
            if (this.filterFormGroup.get("end").value == null) {
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
            this.qaService.applyFilter(this.filterFormGroup.value).subscribe(
                (response: ReportsPermitEntityDto[]) => {
                    this.spinnerService.hide();
                    this.allDMarkPermitData = response;
                    this.rerender()
                    // this.SpinnerService.hide();
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


}
