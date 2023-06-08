import {Component, OnInit} from '@angular/core';
import {PermitEntityDto, TaskDto} from '../../core/store/data/qa/qa.model';
import {QaService} from '../../core/store/data/qa/qa.service';
import {Router} from '@angular/router';
import {ApiEndpointService} from '../../core/services/endpoints/api-endpoint.service';

declare interface DataTable {
    headerRow: string[];
    footerRow: string[];
    dataRows: string[][];
}

declare const $: any;

@Component({
    selector: 'app-task-manager',
    templateUrl: './task-manager.component.html',
    styleUrls: ['./task-manager.component.css']
})
export class TaskManagerComponent implements OnInit {
    public dataTable: DataTable;
    public allTaskData: TaskDto[];
    public allPermitTaskData: PermitEntityDto[];

    draftID = String(ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.DRAFT_ID);
    fmarkID = String(ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.FMARK_TYPE_ID);
    dmarkID = String(ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.DMARK_TYPE_ID);
    smarkID = String(ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.SMARK_TYPE_ID);

    constructor(private qaService: QaService,
                private router: Router) {
    }

    ngOnInit() {
        let formattedArray = [];
        this.dataTable;

        this.qaService.qaTaskListFind().subscribe(
            (data: any) => {

                this.allPermitTaskData = data;
                // tslint:disable-next-line:max-line-length
                formattedArray = data.map(i => [i.permitRefNumber, i.createdOn, i.productName, i.tradeMark, i.awardedPermitNumber, i.dateOfIssue, i.dateOfExpiry, i.permitStatus, i.id]);

                this.dataTable = {
                    // tslint:disable-next-line:max-line-length
                    headerRow: ['Permit Ref No', 'Application Date', 'Product', 'Brand Name', 'Permit Number', 'Issue Date', 'Expiry Date', 'Status', 'Actions'],
                    footerRow: ['Permit Ref No', 'Application Date', 'Product', 'Brand Name', 'Permit Number', 'Issue Date', 'Expiry Date', 'Status', 'Actions'],
                    dataRows: formattedArray


                    // ['REFFM#202107095913D', 'Andrew Mike', '09/07/2021', 'Dassani', 'Water', '']

                };

                // this.allTaskData = data;
                // // tslint:disable-next-line:max-line-length
                // formattedArray = data.map(i => [i.taskName, i.taskCreateTime, i.permitRefNo, i.permitId]);
                //
                // this.dataTable = {
                //     headerRow: ['Task Name', 'Task Created Time', 'Permit Reference Number', 'Actions'],
                //     footerRow: ['Task Name', 'Task Created Time', 'Permit Reference Number', 'Actions'],
                //     dataRows: formattedArray
                //
                //
                //     // ['REFFM#202107095913D', 'Andrew Mike', '09/07/2021', 'Dassani', 'Water', '']
                //
                // };

            });

  }
  ngAfterViewInit() {
    $('#datatables').DataTable({
      'pagingType': 'full_numbers',
      'lengthMenu': [
        [10, 25, 50, -1],
        [10, 25, 50, 'All']
      ],
      responsive: true,
        language: {
            search: '_INPUT_',
            searchPlaceholder: 'Search records',
        }
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
