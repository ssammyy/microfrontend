import {Component, OnDestroy, OnInit, QueryList, ViewChildren} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {DataTableDirective} from 'angular-datatables';
import {Subject} from 'rxjs';
import * as CryptoJS from 'crypto-js';
import {ApiResponseModel} from '../../../core/store/data/ms/ms.model';
import {MyTasksPermitEntityDto, PermitSearchValues} from '../../../core/store/data/qa/qa.model';
import {QaInternalService} from '../../../core/store/data/qa/qa-internal.service';
import {QaService} from '../../../core/store/data/qa/qa.service';
import {NgxSpinnerService} from 'ngx-spinner';
import {HttpErrorResponse} from "@angular/common/http";
import {MasterService} from "../../../core/store/data/master/master.service";
import {CompanyDto} from "../../../core/store/data/master/master.model";

@Component({
    selector: 'app-search-permits',
    templateUrl: './search-permits.component.html',
    styleUrls: ['./search-permits.component.css'],
})
export class SearchPermitsComponent implements OnInit, OnDestroy {
    allPermitTaskData: MyTasksPermitEntityDto[];
    dtOptions: DataTables.Settings = {
        destroy: true,
    };
    @ViewChildren(DataTableDirective) dtElements: QueryList<DataTableDirective>;
    dtTrigger1: Subject<any> = new Subject<any>();
    displayUsers = false;
    form: FormGroup;
    isFirmNameSelected: boolean = false;
    permitSearchValues: PermitSearchValues;
    private destroy$: Subject<void> = new Subject<void>();
    public companies !: CompanyDto[];

    constructor(
        private formBuilder: FormBuilder,
        private router: Router,
        public internalService: QaInternalService,
        private qaService: QaService,
        private spinnerService: NgxSpinnerService,
        private masterService: MasterService
    ) {
    }

    ngOnInit(): void {
        this.getAllCompanies();
        this.form = this.formBuilder.group({
            searchType: ['', Validators.required],
            searchTerm: ['', Validators.required],
        });
    }
    onRadioChange(selectedOption: string){
        if(selectedOption=="firmName"){
            this.isFirmNameSelected = true;
        }
        else
            this.isFirmNameSelected = false;

    }

    onSubmit(isValid: boolean): void {
        if (isValid && this.form) {
            this.displayUsers = false;
            this.spinnerService.show();


            const searchType = this.form.get('searchType').value;
            const searchTerm = this.form.get('searchTerm').value;

            this.permitSearchValues = {
                productName: searchType === 'productName' ? searchTerm : undefined,
                firmName: searchType === 'firmName' ? searchTerm : undefined,
                refNumber: searchType === 'refNumber' ? searchTerm : undefined,
                tradeMark: searchType === 'tradeMark' ? searchTerm : undefined,
            };

            this.internalService.loadMySearchPermit(this.permitSearchValues).subscribe(
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

    ngOnDestroy() {
        this.destroy$.next();
        this.destroy$.complete();
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


    gotoPermitDetails(permitId: string) {
        const key = CryptoJS.enc.Utf8.parse('11A1764225B11AA1');
        const encrypted = CryptoJS.AES.encrypt(permitId, key, {
            mode: CryptoJS.mode.ECB,
            padding: CryptoJS.pad.ZeroPadding
        });
        this.router.navigate(['/permit-details-admin', encrypted.ciphertext.toString(CryptoJS.enc.Hex)])
    }


    getAllCompanies() {
        this.masterService.loadDAllCompanies().subscribe(
            (response: CompanyDto[]) => {
                this.companies = response;
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        )
    }
}
