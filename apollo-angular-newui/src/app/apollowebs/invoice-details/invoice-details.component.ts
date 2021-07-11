import {AfterViewInit, Component, OnInit} from '@angular/core';

declare interface DataTable {
    headerRow: string[];
    footerRow: string[];
    dataRows: string[][];
}

declare const $: any;

@Component({
    selector: 'app-invoice-details',
    templateUrl: './invoice-details.component.html',
    styleUrls: ['./invoice-details.component.css']
})
export class InvoiceDetailsComponent implements OnInit, AfterViewInit {

    public dataTable: DataTable;

    constructor() {
    }

    ngOnInit(): void {
        this.dataTable = {
            headerRow:
                ['INVOICE REF NO', 'COMMODITY DESCRIPTION', 'BRAND NAME', 'TOTAL AMOUNT', 'PAID STATUS', 'VIEW'],
            footerRow:
                ['INVOICE REF NO', 'COMMODITY DESCRIPTION', 'BRAND NAME', 'TOTAL AMOUNT', 'PAID STATUS', 'VIEW'],

            dataRows: [
                ['KIMSDM#202106290C9', 'Wine', 'Wine', '191400', 'PAID', '']
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
