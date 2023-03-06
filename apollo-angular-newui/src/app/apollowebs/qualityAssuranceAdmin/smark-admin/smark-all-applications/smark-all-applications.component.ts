import {Component, OnInit, QueryList, ViewChildren} from '@angular/core';
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {SchemeMembershipService} from "../../../../core/store/data/std/scheme-membership.service";
import {NgxSpinnerService} from "ngx-spinner";
import {QaInternalService} from "../../../../core/store/data/qa/qa-internal.service";
import {ApiResponseModel} from "../../../../core/store/data/ms/ms.model";
import {MyTasksPermitEntityDto} from "../../../../core/store/data/qa/qa.model";
import {ApiEndpointService} from "../../../../core/services/endpoints/api-endpoint.service";
import {Router} from "@angular/router";
import * as CryptoJS from 'crypto-js';

@Component({
    selector: 'app-smark-all-applications',
    templateUrl: './smark-all-applications.component.html',
    styleUrls: ['./smark-all-applications.component.css']
})
export class SmarkAllApplicationsComponent implements OnInit {

    public allPermitTaskData: MyTasksPermitEntityDto[];

    dtOptions: DataTables.Settings = {};
    @ViewChildren(DataTableDirective)
    dtElements: QueryList<DataTableDirective>;
    dtTrigger1: Subject<any> = new Subject<any>();

    loading = false;
    loadingText: string;
    displayUsers: boolean = false;
    smarkID = ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.SMARK_TYPE_ID;

    public itemId: string = "";


    constructor(private schemeMembershipService: SchemeMembershipService,
                private router: Router,
                private internalService: QaInternalService,
                private SpinnerService: NgxSpinnerService,
    ) {
    }

    ngOnInit(): void {
        this.getMySmarkComplete();

    }

    public getMySmarkComplete(): void {
        this.SpinnerService.show();
        this.internalService.loadMyAllPermitByPermitType(this.smarkID).subscribe(
            (dataResponse: ApiResponseModel) => {
                if (dataResponse.responseCode === '00') {
                    // console.log(dataResponse.data as ConsumerComplaintsReportViewEntity[]);
                    this.allPermitTaskData = dataResponse?.data as MyTasksPermitEntityDto[];
                    this.rerender();
                    this.SpinnerService.hide();
                    this.displayUsers = true;

                    this.loading = false;

                }
                this.SpinnerService.hide();
                this.SpinnerService.hide()
                this.displayUsers = true;

                this.loading = false;
            },
            error => {
                this.SpinnerService.hide();
                this.SpinnerService.hide()
                this.displayUsers = true;

                this.loading = false;
            },
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

    gotoPermitDetails(permitId: string) {
        let text = permitId;
        let key = '11A1764225B11AA1';
        text = CryptoJS.enc.Utf8.parse(text);
        key = CryptoJS.enc.Utf8.parse(key);
        let encrypted = CryptoJS.AES.encrypt(text, key, {mode: CryptoJS.mode.ECB, padding: CryptoJS.pad.ZeroPadding});
        encrypted = encrypted.ciphertext.toString(CryptoJS.enc.Hex);
        this.router.navigate(['/permit-details-admin', encrypted])
    }


}
