import {Component, OnInit} from '@angular/core';
import {PermitEntityDto} from "../../../core/store/data/qa/qa.model";
import {QaService} from "../../../core/store/data/qa/qa.service";
import {Router} from "@angular/router";
import {ApiEndpointService} from "../../../core/services/endpoints/api-endpoint.service";

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
    draftID = String(ApiEndpointService.QA_APPLICATION_MAP_PROPERTIES.DRAFT_ID);

  constructor(
      private qaService: QaService,
      private router: Router,
  ) {

  }

  ngOnInit() {
    let formattedArray = [];
    this.dataTable;

    this.qaService.loadPermitList('2').subscribe(
        (data: any) => {

          this.allPermitData = data;
          // tslint:disable-next-line:max-line-length
          formattedArray = data.map(i => [i.permitRefNumber, i.createdOn, i.productName, i.tradeMark, i.awardedPermitNumber, i.dateOfIssue, i.dateOfExpiry, i.permitStatus, i.id, i.processStatusID]);

            this.dataTable = {
                // tslint:disable-next-line:max-line-length
                headerRow: ['Permit Ref No', 'Application Date', 'Product', 'Brand Name', 'Permit Number', 'Issue Date', 'Expiry Date', 'Status', 'Actions'],
                footerRow: ['Permit Ref No', 'Application Date', 'Product', 'Brand Name', 'Permit Number', 'Issue Date', 'Expiry Date', 'Status', 'Actions'],
                dataRows: formattedArray


                // ['REFFM#202107095913D', 'Andrew Mike', '09/07/2021', 'Dassani', 'Water', '']

            };

        });


    // this.dataTable = {
    //   headerRow: ['Permit Ref No', 'Application Date', 'Product', 'Brand Name', 'Permit Number', 'Issue Date', 'Expiry Date', 'Status', 'Actions'],
    //   footerRow: ['Permit Ref No', 'Application Date', 'Product', 'Brand Name', 'Permit Number', 'Issue Date', 'Expiry Date', 'Status', 'Actions'],
    //   dataRows: []
    //
    //
    //   // ['REFFM#202107095913D', 'Andrew Mike', '09/07/2021', 'Dassani', 'Water', '']
    //
    // }
    // console.log(this.dataTable);
    // this.allPermitData = this.Object.json().results;
    // console.log(formattedArray);


    //
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

    onSelect(rowElement: string, processStatusID: number) {
        // if (this.draftID === processStatusID) {
        //     this.router.navigate([`/smark/newSmarkPermit`], {fragment: rowElement});
        //     //this.router.navigate(['/smark/newSmarkPermit'], { state: { id: '123' } });
        // } else {
        //     this.router.navigate([`/smarkpermitdetails`], {fragment: rowElement});
        //     //this.router.navigate([`/smarkpermitdetails`], {fragment: rowElement});
        // }
    }

    onGoToSelect(string: string, string2: string) {
        // if (this.draftID === Number(string2)) {
        //     this.router.navigate([`/smark/newSmarkPermit`], {fragment: string});
        //     //this.router.navigate(['/smark/newSmarkPermit'], { state: { id: '123' } });
        // } else {
        //     this.router.navigate([`/smarkpermitdetails`], {fragment: string});
        //     //this.router.navigate([`/smarkpermitdetails`], {fragment: rowElement});
        // }
    }
}