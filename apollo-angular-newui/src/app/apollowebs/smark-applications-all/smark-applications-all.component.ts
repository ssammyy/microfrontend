import { Component, OnInit } from '@angular/core';
import {PermitEntityDto} from "../../core/store/data/qa/qa.model";
import {QaService} from "../../core/store/data/qa/qa.service";
import {Router} from "@angular/router";

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

  constructor(
      private qaService: QaService,
      private router: Router,
  ) {

  }

  ngOnInit() {
    let formattedArray = [];
    this.qaService.loadPermitList('2').subscribe(
        (data: any) => {

          this.allPermitData = data;
          // tslint:disable-next-line:max-line-length
          formattedArray = data.map(i => [i.permitRefNumber, i.createdOn, i.productName, i.tradeMark, i.awardedPermitNumber, i.dateOfIssue, i.dateOfExpiry, i.permitStatus, i.id]);

          this.dataTable = {
            headerRow: ['Permit Ref No', 'Application Date', 'Product', 'Brand Name', 'Permit Number', 'Issue Date', 'Expiry Date', 'Status', 'Actions'],
            footerRow: ['Permit Ref No', 'Application Date', 'Product', 'Brand Name', 'Permit Number', 'Issue Date', 'Expiry Date', 'Status', 'Actions'],
            dataRows: formattedArray


            // ['REFFM#202107095913D', 'Andrew Mike', '09/07/2021', 'Dassani', 'Water', '']

          }
        });
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

  onSelect(rowElement: string) {
    this.router.navigate(['/dmark'], {fragment: rowElement});
  }

}
