import {AfterViewInit, Component, OnInit} from '@angular/core';

declare interface DataTable {
  headerRow: string[];
  footerRow: string[];
  dataRows: string[][];
}

declare const $: any;
@Component({
  selector: 'app-invoice',
  templateUrl: './invoice.component.html',
  styleUrls: ['./invoice.component.css']
})
export class InvoiceComponent implements OnInit, AfterViewInit {
  public dataTable: DataTable;

  ngOnInit() {
    this.dataTable = {
      headerRow: ['Invoice No', 'Receipt No', 'Date', 'Total Amount', ' Status', 'Actions'],
      footerRow: ['Invoice No', 'Receipt No', 'Date', 'Total Amount', ' Status', 'Actions'],

      dataRows: [
        ['KIMSINVOICE#20210629CDA8B', 'KIMSINVOICE#20210629CDA8B', '09/07/2021', '382800', 'Paid', '']
      ]
    };

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
