import {Component, OnInit, QueryList, ViewChildren} from '@angular/core';
import {PermitEntityDto} from "../../../../../core/store/data/qa/qa.model";
import {ApiEndpointService} from "../../../../../core/services/endpoints/api-endpoint.service";
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {QaService} from "../../../../../core/store/data/qa/qa.service";
import {Router} from "@angular/router";
import {LoadingService} from "../../../../../core/services/loader/loadingservice.service";
import {NgxSpinnerService} from "ngx-spinner";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
    selector: 'app-applications-received',
    templateUrl: './applications-received.component.html',
    styleUrls: ['./applications-received.component.css']
})
export class ApplicationsReceivedComponent implements OnInit {
    public allPermitData: PermitEntityDto[];
    dmarkID = ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.DMARK_TYPE_ID;
    dtOptions: DataTables.Settings = {};
    @ViewChildren(DataTableDirective)
    dtElements: QueryList<DataTableDirective>;
    dtTrigger1: Subject<any> = new Subject<any>();
    displayUsers: boolean = false;


    constructor(private qaService: QaService,
                private router: Router,
                private _loading: LoadingService,
                private SpinnerService: NgxSpinnerService) {
    }

    ngOnInit(): void {
        this.getAllApplicationsReceived()

    }

    public getAllApplicationsReceived(): void {
        this.SpinnerService.show();

        this.qaService.loadPermitReports(String(this.dmarkID)).subscribe(
            (response: PermitEntityDto[]) => {
                console.log(response);
                this.allPermitData = response;
                this.rerender()
                this.SpinnerService.hide();
                this.displayUsers = true;


            },
            (error: HttpErrorResponse) => {
                alert(error.message);
                this.displayUsers = true;
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


        });

    }


    ngOnDestroy(): void {
        // Do not forget to unsubscribe the event
        this.dtTrigger1.unsubscribe();


    }
}
