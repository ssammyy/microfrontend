import {Component, OnInit, QueryList, ViewChildren} from '@angular/core';
import * as CryptoJS from 'crypto-js';
import {MyTasksPermitEntityDto} from "../../../core/store/data/qa/qa.model";
import {ApiEndpointService} from "../../../core/services/endpoints/api-endpoint.service";
import {Store} from "@ngrx/store";
import {NgxSpinnerService} from "ngx-spinner";
import {QaInternalService} from "../../../core/store/data/qa/qa-internal.service";
import {Router} from "@angular/router";
import {selectUserInfo} from "../../../core/store";
import {ApiResponseModel} from "../../../core/store/data/ms/ms.model";
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";

@Component({
    selector: 'app-fmark-admin',
    templateUrl: './fmark-admin.component.html',
    styleUrls: ['./fmark-admin.component.css']
})
export class FmarkAdminComponent implements OnInit {
    public allPermitTaskData: MyTasksPermitEntityDto[];

    roles: string[];
    dtOptions: DataTables.Settings = {};
    @ViewChildren(DataTableDirective)
    dtElements: QueryList<DataTableDirective>;
    dtTrigger1: Subject<any> = new Subject<any>();
    displayUsers: boolean = false;

    loading = false;
    loadingText: string;
    fmarkID = ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.FMARK_TYPE_ID;
    isDtInitialized: boolean = false


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
        this.getMyFmarkTasks();
    }

    id: any = 'My Tasks';

    tabChange(ids: any) {
        this.id = ids;
        if (this.id == "My Tasks") {
            this.internalService.reloadCurrentRoute()
        }
    }

    public getMyFmarkTasks(): void {
        this.loading = true
        this.loadingText = "Retrieving My Tasks"
        this.SpinnerService.show();
        this.internalService.loadMyTasksByPermitType(this.fmarkID).subscribe(
            (dataResponse: ApiResponseModel) => {
                if (dataResponse.responseCode === '00') {
                    // console.log(dataResponse.data as ConsumerComplaintsReportViewEntity[]);
                    this.allPermitTaskData = dataResponse?.data as MyTasksPermitEntityDto[];
                    this.rerender()
                    this.SpinnerService.hide()
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

    gotoPermitDetails(permitId: string) {

        let text = permitId;
        let key = '11A1764225B11AA1';
        text = CryptoJS.enc.Utf8.parse(text);
        key = CryptoJS.enc.Utf8.parse(key);
        let encrypted = CryptoJS.AES.encrypt(text, key, {mode: CryptoJS.mode.ECB, padding: CryptoJS.pad.ZeroPadding});
        encrypted = encrypted.ciphertext.toString(CryptoJS.enc.Hex);
        this.router.navigate(['/permit-details-admin', encrypted])


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


}
