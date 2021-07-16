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
  selector: 'app-fmark-application',
  templateUrl: './fmark-application.component.html',
  styleUrls: ['./fmark-application.component.css']
})
export class FmarkApplicationComponent implements OnInit {
  public dataTable: DataTable;
  public allPermitData: PermitEntityDto[];

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
          formattedArray = data.map(i => [i.productName, i.tradeMark, i.awardedPermitNumber]);

          this.dataTable = {
            headerRow: ['Product', 'Brand Name', 'Permit Number'],
            footerRow: ['Product', 'Brand Name', 'Permit Number'],
            dataRows: formattedArray


            // ['REFFM#202107095913D', 'Andrew Mike', '09/07/2021', 'Dassani', 'Water', '']

          }

        });
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
    this.router.navigate(['/permitdetails'], {fragment: rowElement});
  }

}
