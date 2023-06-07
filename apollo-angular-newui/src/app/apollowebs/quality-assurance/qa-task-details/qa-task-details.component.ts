import {Component, OnInit} from '@angular/core';
import {PermitEntityDto, TaskDto} from '../../../core/store/data/qa/qa.model';
import {ApiEndpointService} from '../../../core/services/endpoints/api-endpoint.service';
import {QaService} from '../../../core/store/data/qa/qa.service';
import {Router} from '@angular/router';
import {selectUserInfo} from '../../../core/store';
import {Store} from '@ngrx/store';
import {NgxSpinnerService} from 'ngx-spinner';

declare interface DataTable {
    headerRow: string[];
    footerRow: string[];
    dataRows: string[][];
}

declare const $: any;

@Component({
    selector: 'app-qa-task-details',
    templateUrl: './qa-task-details.component.html',
    styleUrls: ['./qa-task-details.component.css'],
})

export class QaTaskDetailsComponent implements OnInit {
    public dataTable: DataTable;
    public allTaskData: TaskDto[];
    public allPermitTaskData: PermitEntityDto[];

    roles: string[];

    internalUser: boolean;
    draftID = String(ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.DRAFT_ID);
    fmarkID = String(ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.FMARK_TYPE_ID);
    dmarkID = String(ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.DMARK_TYPE_ID);
    smarkID = String(ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.SMARK_TYPE_ID);

    constructor(private qaService: QaService,
                private store$: Store<any>,
                private SpinnerService: NgxSpinnerService,
                private router: Router) {
    }

    ngOnInit(): void {
        let formattedArray = [];
        // tslint:disable-next-line:no-unused-expression
        this.dataTable;

        this.store$.select(selectUserInfo).pipe().subscribe((u) => {
            this.roles = u.roles;
            return this.roles = u.roles;
        });

        if (this.roles?.includes('QA_MANAGER_MODIFY')
            || this.roles?.includes('QA_OFFICER_MODIFY')
            || this.roles?.includes('QA_ASSESSORS_MODIFY')
        ) {
          this.internalUser = true;
            this.SpinnerService.show();
            this.qaService.qaInternalUserTaskListFind().subscribe(
                (data: any) => {
                    this.allPermitTaskData = data;
                    // tslint:disable-next-line:max-line-length
                    formattedArray = data.map(i => [i.permitType, i.permitRefNumber, i.createdOn, i.productName, i.tradeMark, i.awardedPermitNumber, i.dateOfIssue, i.dateOfExpiry, i.permitStatus, i.id]);
                    this.dataTable = {
                        // tslint:disable-next-line:max-line-length
                        headerRow: ['Actions','Permit Ref No', 'Permit Type', 'Application Date', 'Product', 'Brand Name', 'Permit Number', 'Issue Date', 'Expiry Date', 'Status'],
                        footerRow: ['Actions','Permit Ref No', 'Permit Type', 'Application Date', 'Product', 'Brand Name', 'Permit Number', 'Issue Date', 'Expiry Date', 'Status'],
                        dataRows: formattedArray,
                    };
                    this.SpinnerService.hide();
                },
                error => {
                    this.SpinnerService.hide();
                },
            );
        } else if (this.roles?.includes('PERMIT_APPLICATION')) {
          this.internalUser = false;
            this.SpinnerService.show();
            this.qaService.qaTaskListFind().subscribe(
                (data: any) => {

                    this.allPermitTaskData = data;
                    // tslint:disable-next-line:max-line-length
                    formattedArray = data.map(i => [i.permitType, i.permitRefNumber, i.createdOn, i.productName, i.tradeMark, i.awardedPermitNumber, i.dateOfIssue, i.dateOfExpiry, i.permitStatus, i.id]);

                    this.dataTable = {
                        // tslint:disable-next-line:max-line-length
                        headerRow: ['Actions','Permit Ref No', 'Permit Type', 'Application Date', 'Product', 'Brand Name', 'Permit Number', 'Issue Date', 'Expiry Date', 'Status'],
                        footerRow: ['Actions','Permit Ref No', 'Permit Type', 'Application Date', 'Product', 'Brand Name', 'Permit Number', 'Issue Date', 'Expiry Date', 'Status'],
                        dataRows: formattedArray,
                    };
                    this.SpinnerService.hide();
                },
                error => {
                    this.SpinnerService.hide();
                },
            );
        }


    }

    ngAfterViewInit() {
        $('#datatables').DataTable({
            'pagingType': 'full_numbers',
            'lengthMenu': [
                [10, 25, 50, -1],
                [10, 25, 50, 'All'],
            ],
            responsive: true,
            language: {
                search: '_INPUT_',
                searchPlaceholder: 'Search records',
            },
        });
        let table: any;
        table = $(`#datatables`).DataTable();

    }


    onSelect(rowElement: string, permitTypeID: string) {
        if (permitTypeID === this.smarkID || permitTypeID === this.fmarkID) {
            this.router.navigate(['/smarkpermitdetails'], {fragment: rowElement});
        } else if (permitTypeID === this.dmarkID) {
            this.router.navigate(['/permitdetails'], {fragment: rowElement});
        }
    }

}
