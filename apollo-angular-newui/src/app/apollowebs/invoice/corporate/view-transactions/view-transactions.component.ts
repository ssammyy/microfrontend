import {Component, OnInit} from '@angular/core';
import {FinanceInvoiceService} from "../../../../core/store/data/invoice/finance-invoice.service";
import {LocalDataSource} from "ng2-smart-table";
import {ActivatedRoute} from "@angular/router";
import {DatePipe} from "@angular/common";
import {ConsignmentStatusComponent} from "../../../../core/shared/customs/consignment-status/consignment-status.component";

@Component({
    selector: 'app-view-transactions',
    templateUrl: './view-transactions.component.html',
    styleUrls: ['./view-transactions.component.css']
})
export class ViewTransactionsComponent implements OnInit {
    billId: any
    corporateId: any
    page = 0
    pageSize = 20
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
    dataSet: LocalDataSource = new LocalDataSource();

    constructor(private activatedRoute: ActivatedRoute, private fiService: FinanceInvoiceService) {
    }


    ngOnInit(): void {
        this.activatedRoute.paramMap.subscribe(
            res => {
                this.billId = res.get('id')
                this.corporateId = res.get('cid')
                this.loadBillTransactions()
            }
        )

    }

    loadBillTransactions() {
        this.fiService.loadBillTransactions(this.billId, this.corporateId, this.page, this.pageSize)
            .subscribe(
                res => {
                    if (res.responseCode == "00") {
                        this.dataSet.load(res.data)
                    } else {
                        this.fiService.showError(res.message, null)
                    }
                }
            )
    }
}
