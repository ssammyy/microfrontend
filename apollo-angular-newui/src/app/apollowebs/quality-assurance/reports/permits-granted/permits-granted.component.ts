import {Component, OnInit, QueryList, ViewChildren} from '@angular/core';
import {ReportsPermitEntityDto} from "../../../../core/store/data/qa/qa.model";
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {ApiEndpointService} from "../../../../core/services/endpoints/api-endpoint.service";
import {QaService} from "../../../../core/store/data/qa/qa.service";
import {Router} from "@angular/router";
import {LoadingService} from "../../../../core/services/loader/loadingservice.service";
import {NgxSpinnerService} from "ngx-spinner";
import {HttpErrorResponse} from "@angular/common/http";
import {formatDate} from "@angular/common";

@Component({
    selector: 'app-permits-granted',
    templateUrl: './permits-granted.component.html',
    styleUrls: ['./permits-granted.component.css']
})
export class PermitsGrantedComponent implements OnInit {

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


    constructor(private qaService: QaService,
                private router: Router,
                private _loading: LoadingService,
                private SpinnerService: NgxSpinnerService) {
    }

    ngOnInit(): void {
        this.dtOptions = {

            processing: true,
            dom: 'Bfrtip'
        };
        this.getAllDMarkApplicationsGranted()
        this.getAllFMarkApplicationsGranted()
        this.getAllSMarkApplicationsGranted()

    }


    public getAllDMarkApplicationsGranted(): void {
        this.SpinnerService.show();
        this.qaService.loadPermitGrantedReports(String(this.dMarkID)).subscribe(
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

    public getAllSMarkApplicationsGranted(): void {
        this.qaService.loadPermitGrantedReports(String(this.sMarkID)).subscribe(
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

    public getAllFMarkApplicationsGranted(): void {
        this.qaService.loadPermitGrantedReports(String(this.fMarkID)).subscribe(
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

}
