import {Component, Input, OnInit} from '@angular/core';
import {DatePipe} from "@angular/common";
import {ConsignmentStatusComponent} from "../../../../../core/shared/customs/consignment-status/consignment-status.component";
import {LocalDataSource} from "ng2-smart-table";

@Component({
    selector: 'app-bill-details',
    templateUrl: './bill-details.component.html',
    styleUrls: ['./bill-details.component.css']
})
export class BillDetailsComponent implements OnInit {
    @Input()
    billDetails: any
    @Input()
    dataSet: LocalDataSource
    @Input()
    isDialog = false
    public settings = {
        selectMode: 'single',  // single|multi
        hideHeader: false,
        hideSubHeader: false,
        actions: {
            columnTitle: 'Actions',
            add: false,
            edit: false,
            delete: false,
            custom: [
                //  { name: 'editRecord', title: '<i class="btn btn-sm btn-primary">View More</i>' },
                // {name: 'viewRecord', title: '<i class="btn btn-sm btn-primary">View More</i>'}
            ],
            position: 'right' // left|right
        },
        delete: {
            deleteButtonContent: '&nbsp;&nbsp;<i class="fa fa-trash-o text-danger"></i>',
            confirmDelete: true
        },
        noDataMessage: 'No data found',
        columns: {
            invoiceNumber: {
                title: 'Invoice Number',
                type: 'string',
                filter: false
            },
            tempReceiptNumber: {
                title: 'Temporary Receipt',
                type: 'string',
            },
            paymentMethod: {
                title: 'Payment method',
                type: 'string',
                filter: false
            },
            amount: {
                title: 'Amount',
                type: 'string'
            },
            transactionType: {
                title: 'Transaction Type',
                type: 'string'
            },
            transactionDate: {
                title: 'Transaction Date',
                type: 'date',
                valuePrepareFunction: (date) => {
                    if (date) {
                        return new DatePipe('en-US').transform(date, 'dd/MM/yyyy hh:mm');
                    }
                    return ""
                },
            },
            revenueLine: {
                title: 'Revenue Line',
                type: 'string'
            },
            status: {
                title: 'Status',
                type: 'custom',
                renderComponent: ConsignmentStatusComponent
            }
        },
        pager: {
            display: true,
            perPage: 20
        }
    };

    constructor() {
    }

    ngOnInit(): void {
    }

}
