import {AfterViewInit, Component, OnInit, QueryList, ViewChildren} from '@angular/core';
import {PermitEntityDto} from "../../../core/store/data/qa/qa.model";
import {QaService} from "../../../core/store/data/qa/qa.service";
import {Router} from "@angular/router";
import {ApiEndpointService} from '../../../core/services/endpoints/api-endpoint.service';
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {LoadingService} from "../../../core/services/loader/loadingservice.service";
import {NgxSpinnerService} from "ngx-spinner";
import {HttpErrorResponse} from "@angular/common/http";
import Swal from "sweetalert2";
import swal from "sweetalert2";

declare interface DataTable {
    headerRow: string[];
    footerRow: string[];
    dataRows: string[][];
}

declare const $: any;

@Component({
    selector: 'app-fmarkallapps',
    templateUrl: './fmarkallapps.component.html',
    styleUrls: ['./fmarkallapps.component.css']
})
export class FmarkallappsComponent implements OnInit {
    public dataTable: DataTable;
    public allPermitData: PermitEntityDto[];
    draftID = String(ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.DRAFT_ID);
    fmarkID = String(ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.FMARK_TYPE_ID);
    dtOptions: DataTables.Settings = {};
    @ViewChildren(DataTableDirective)
    dtElements: QueryList<DataTableDirective>;
    dtTrigger1: Subject<any> = new Subject<any>();
    displayUsers: boolean = false;

    constructor(
        private qaService: QaService,
        private router: Router,
        private _loading: LoadingService,
        private SpinnerService: NgxSpinnerService
    ) {

    }

    ngOnInit() {
        this.getAllPermitData()
    }

    public getAllPermitData(): void {
        this.SpinnerService.show();

        this.qaService.loadPermitList(String(this.fmarkID)).subscribe(
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
}
