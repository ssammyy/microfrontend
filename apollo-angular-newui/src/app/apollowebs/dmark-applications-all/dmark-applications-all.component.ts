import {Component, OnInit} from '@angular/core';
import {QaService} from "../../core/store/data/qa/qa.service";
import {PermitEntityDto} from "../../core/store/data/qa/qa.model";
import {Subject} from "rxjs";

declare interface DataTable {
  headerRow: string[];
  footerRow: string[];
  dataRows: PermitEntityDto[];
}
declare const $: any;
@Component({
  selector: 'app-dmark-applications-all',
  templateUrl: './dmark-applications-all.component.html',
  styleUrls: ['./dmark-applications-all.component.css']
})
export class DmarkApplicationsAllComponent implements OnInit {
  public dataTable: DataTable;
  public allPermitData: PermitEntityDto[];
  allUsers: any = [];
  dtTrigger: Subject<any> = new Subject<any>();

  constructor(private qaService: QaService) {
  }

  ngOnInit(): void {

    this.qaService.loadDMARKPermitList('1').subscribe(
        (data: any) => {
          this.allPermitData = data;
          this.allUsers = data.data;
          // this.allPermitData = this.Object.json().results;
          console.log(data);
        }
    );

    this.dataTable = {
      headerRow: ['Permit Ref No', 'Application Date', 'Product', 'Brand Name', 'Permit Number', 'Issue Date', 'Expiry Date', 'Section', 'Status', 'Actions'],
      footerRow: ['Permit Ref No', 'Application Date', 'Product', 'Brand Name', 'Permit Number', 'Issue Date', 'Expiry Date', 'Section', 'Status', 'Actions'],
      dataRows:
      this.allPermitData
      // ['REFFM#202107095913D', 'Andrew Mike', '09/07/2021', 'Dassani', 'Water', '']

    };

    console.log(this.dataTable);
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
    // Edit record
    table.on('click', '.edit', function (e) {
      let $tr = $(this).closest('tr');
      if ($($tr).hasClass('child')) {
        $tr = $tr.prev('.parent');
      }
      let data: any;
      data = table.row($tr).data();
      alert('You press on Row: ' + data[0] + ' ' + data[1] + ' ' + data[2] + '\'s row.');
      e.preventDefault();
    });
    // Delete a record
    table.on('click', '.remove', function (e) {
      const $tr = $(this).closest('tr');
      table.row($tr).remove().draw();
      e.preventDefault();
    });
    // Like record
    table.on('click', '.like', function (e) {
      alert('You clicked on Like button');
      e.preventDefault();
    });
    $('.card .material-datatables label').addClass('form-group');
  }
}
