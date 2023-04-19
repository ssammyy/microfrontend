import {Component, OnDestroy, OnInit, QueryList, ViewChildren} from '@angular/core';
import {MyTasksPermitEntityDto, PermitSearchValues} from "../../../core/store/data/qa/qa.model";
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {SchemeMembershipService} from "../../../core/store/data/std/scheme-membership.service";
import {Router} from "@angular/router";
import {QaInternalService} from "../../../core/store/data/qa/qa-internal.service";
import {NgxSpinnerService} from "ngx-spinner";
import * as CryptoJS from 'crypto-js';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ApiResponseModel} from "../../../core/store/data/ms/ms.model";
import {QaService} from "../../../core/store/data/qa/qa.service";
import {takeUntil} from "rxjs/operators";

@Component({
    selector: 'app-search-permits',
    templateUrl: './search-permits.component.html',
    styleUrls: ['./search-permits.component.css']
})
export class SearchPermitsComponent implements OnInit, OnDestroy {
    public allPermitTaskData: MyTasksPermitEntityDto[];
    dtOptions: DataTables.Settings = {};
    @ViewChildren(DataTableDirective)
    dtElements: QueryList<DataTableDirective>;
    dtTrigger1: Subject<any> = new Subject<any>();
    displayUsers: boolean = false;
    form!: FormGroup;
    permitSearchValues: PermitSearchValues;

    private unsubscribe$: Subject<void> = new Subject<void>();


    constructor(private schemeMembershipService: SchemeMembershipService,
                private formBuilder: FormBuilder,
                private router: Router,
                private internalService: QaInternalService,
                private qaService: QaService,
                private spinnerService: NgxSpinnerService
    ) {
    }

    ngOnInit(): void {

        this.form = this.formBuilder.group({
            searchType: ['', Validators.required],
            searchTerm: ['', Validators.required]

        });

        this.dtOptions = {
            destroy: true,
        };

    }

    onSubmit(valid: boolean) {
        console.log(this.form.value);
        if (valid) {
            this.spinnerService.show()
            this.displayUsers = false;


            // Call your search service method here
            this.permitSearchValues = new PermitSearchValues();
            if (this.form.get("searchType").value === 'productName') {
                this.permitSearchValues.productName = this.form.get("searchTerm").value
            } else if (this.form.get("searchType").value === 'firmName') {
                this.permitSearchValues.firmName = this.form.get("searchTerm").value
            } else if (this.form.get("searchType").value === 'refNumber') {
                this.permitSearchValues.refNumber = this.form.get("searchTerm").value
            } else if (this.form.get("searchType").value === 'tradeMark') {
                this.permitSearchValues.tradeMark = this.form.get("searchTerm").value
            }

            this.internalService.loadMySearchPermit(this.permitSearchValues).pipe(takeUntil(this.unsubscribe$))
                .subscribe(
                (dataResponse: ApiResponseModel) => {
                    if (dataResponse.responseCode === '00') {
                        // console.log(dataResponse.data as ConsumerComplaintsReportViewEntity[]);
                        this.allPermitTaskData = dataResponse?.data as MyTasksPermitEntityDto[];
                        this.rerender()
                        this.displayUsers = true;
                        this.spinnerService.hide()

                    }

                    this.displayUsers = true;
                    this.spinnerService.hide()

                },
                error => {
                    this.displayUsers = true;
                    this.spinnerService.hide()


                },
            );
        } else {
            this.qaService.showError("Please Select All Required Parameters")
        }

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
            if (!this.dtTrigger1.isStopped && !this.dtTrigger1.closed) {
                this.dtTrigger1.next();
            }
        });
    }

    ngOnDestroy(): void {
        // Do not forget to unsubscribe the event
        this.dtTrigger1.unsubscribe();
        this.unsubscribe$.next();
        this.unsubscribe$.complete();


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
