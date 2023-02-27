import {Component, OnInit, ViewChild} from '@angular/core';
import {selectUserInfo} from "../../../core/store";
import {Store} from "@ngrx/store";
import {NgxSpinnerService} from "ngx-spinner";
import {Router} from "@angular/router";
import {MyTasksPermitEntityDto} from "../../../core/store/data/qa/qa.model";
import {ApiEndpointService} from "../../../core/services/endpoints/api-endpoint.service";
import {QaInternalService} from "../../../core/store/data/qa/qa-internal.service";
import {ApiResponseModel} from "../../../core/store/data/ms/ms.model";
import * as CryptoJS from 'crypto-js';
import {Subject} from "rxjs";
import {DataTableDirective} from "angular-datatables";

@Component({
    selector: 'app-smark-admin',
    templateUrl: './smark-admin.component.html',
    styleUrls: ['./smark-admin.component.css']
})
export class SmarkAdminComponent implements OnInit {
    public allPermitTaskData: MyTasksPermitEntityDto[];

    roles: string[];

    loading = false;
    loadingText: string;
    internalUser: boolean;
    draftID = String(ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.DRAFT_ID);
    fmarkID = String(ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.FMARK_TYPE_ID);
    dmarkID = String(ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.DMARK_TYPE_ID);
    smarkID = ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.SMARK_TYPE_ID;
    isDtInitialized: boolean = false
    dtOptions: DataTables.Settings = {};
    dtTrigger: Subject<any> = new Subject<any>();
    @ViewChild(DataTableDirective, {static: false})
    dtElement: DataTableDirective;

    constructor(private store$: Store<any>,
                private SpinnerService: NgxSpinnerService,
                private internalService: QaInternalService,
                private router: Router) {
    }

    ngOnInit(): void {
        this.store$.select(selectUserInfo).pipe().subscribe((u) => {
            this.roles = u.roles;
            return this.roles = u.roles;
        });
        this.getMySmarkTasks();
    }

    id: any = 'My Tasks';

    tabChange(ids: any) {
        this.id = ids;
        if (this.id == "My Tasks") {
            this.internalService.reloadCurrentRoute()
        }
    }

    public getMySmarkTasks(): void {
        this.loading = true
        this.loadingText = "Retrieving My Tasks"
        this.SpinnerService.show();
        this.internalService.loadMyTasksByPermitType(this.smarkID).subscribe(
            (dataResponse: ApiResponseModel) => {
                if (dataResponse.responseCode === '00') {
                    // console.log(dataResponse.data as ConsumerComplaintsReportViewEntity[]);
                    this.allPermitTaskData = dataResponse?.data as MyTasksPermitEntityDto[];

                    if (this.isDtInitialized) {
                        this.dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
                            dtInstance.destroy();
                            this.dtTrigger.next();
                            this.loading = false;

                        });
                    } else {
                        this.isDtInitialized = true
                        this.dtTrigger.next();
                        this.loading = false;

                    }
                }
                this.SpinnerService.hide();
            },
            error => {
                this.SpinnerService.hide();
                console.log(error);
                this.loading = false;

            },
        );
    }


    gotoPermitDetails(permitId: string) {

        var text = permitId;
        var key = '11A1764225B11AA1';
        text = CryptoJS.enc.Utf8.parse(text);
        key = CryptoJS.enc.Utf8.parse(key);
        var encrypted = CryptoJS.AES.encrypt(text, key, {mode: CryptoJS.mode.ECB, padding: CryptoJS.pad.ZeroPadding});
        encrypted = encrypted.ciphertext.toString(CryptoJS.enc.Hex);
        this.router.navigate(['/permit-details-admin', encrypted])


    }


}
