import {Component, OnInit, QueryList, ViewChildren} from '@angular/core';
import {PermitEntityDto} from "../../../core/store/data/qa/qa.model";
import {QaService} from "../../../core/store/data/qa/qa.service";
import {Router} from "@angular/router";
import {ApiEndpointService} from "../../../core/services/endpoints/api-endpoint.service";
import Swal from "sweetalert2";
import swal from "sweetalert2";
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {PublicReviewDraftWithName} from "../../../core/store/data/std/commitee-model";
import {HttpErrorResponse} from "@angular/common/http";
import {NgxSpinnerService} from "ngx-spinner";

declare interface DataTable {
    headerRow: string[];
    footerRow: string[];
    dataRows: string[][];
}

declare const $: any;

@Component({
    selector: 'app-smark-applications-all',
    templateUrl: './smark-applications-all.component.html',
    styleUrls: ['./smark-applications-all.component.css']
})
export class SmarkApplicationsAllComponent implements OnInit {
    public dataTable: DataTable;
    public allPermitData: PermitEntityDto[];
    public publicReviewDrafts !: PublicReviewDraftWithName[];

    draftID = String(ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.DRAFT_ID);
    smarkID = String(ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.SMARK_TYPE_ID);
    dtOptions: DataTables.Settings = {};
    @ViewChildren(DataTableDirective)
    dtElements: QueryList<DataTableDirective>;
    dtTrigger1: Subject<any> = new Subject<any>();
    displayUsers: boolean = false;

    constructor(
        private qaService: QaService,
        private router: Router,
        private SpinnerService: NgxSpinnerService,
    ) {

    }

    ngOnInit() {
        this.getAllPermitData()

    }


    public getAllPermitData(): void {
        this.SpinnerService.show();

        this.qaService.loadPermitList(this.smarkID).subscribe(
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

    public deleteDraft(id: bigint) {
        const swalWithBootstrapButtons = Swal.mixin({
            customClass: {
                confirmButton: 'btn btn-success',
                cancelButton: 'btn btn-success'
            },
            buttonsStyling: false
        });

        swalWithBootstrapButtons.fire({
            title: 'Are you sure your want to Delete This Permit?',
            text: 'You won\'t be able to reverse this!',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Yes!',
            cancelButtonText: 'No!',
            reverseButtons: true
        }).then((result) => {
            if (result.isConfirmed) {
                this.SpinnerService.show();
                this.qaService.deletePermit(String(id), this.allPermitData).subscribe(
                    (response = 'Successful Deletion') => {
                        this.SpinnerService.hide();
                        swalWithBootstrapButtons.fire(
                            'Success!',
                            'Successfully Deleted!',
                            'success'
                        );
                        this.displayUsers= false
                        this.SpinnerService.hide();
                        this.getAllPermitData();
                    },
                );
            } else if (
                /* Read more about handling dismissals below */
                result.dismiss === swal.DismissReason.cancel
            ) {
                swalWithBootstrapButtons.fire(
                    'Cancelled',
                    'You have cancelled this operation',
                    'error'
                );
            }
        });

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
