import {Component, OnInit} from '@angular/core';
import {DestinationInspectionService} from "../../../core/store/data/di/destination-inspection.service";
import {CurrencyFormatterComponent} from "../../../core/shared/currency-formatter/currency-formatter.component";
import {ViewDemandNoteComponent} from "../demand-note-list/view-demand-note/view-demand-note.component";
import {MatDialog} from "@angular/material/dialog";

@Component({
    selector: 'app-transaction-view',
    templateUrl: './transaction-view.component.html',
    styleUrls: ['./transaction-view.component.css']
})
export class TransactionViewComponent implements OnInit {
    activeStatus = 'pending'
    currentPage = 0
    defaultPageSize = 20
    totalCount = 20
    transactionNo = null
    transactionDate = null
    transactionStatus = null
    demandNoteTransactions = null
    transactionStats = null
    settings = {
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
                {name: 'viewRecord', title: '<i class="btn btn-sm btn-primary">View More</i>'}
            ],
            position: 'right' // left|right
        },
        delete: {
            deleteButtonContent: '&nbsp;&nbsp;<i class="fa fa-trash-o text-danger"></i>',
            confirmDelete: true
        },
        noDataMessage: 'No data found',
        columns: {
            demandNoteNumber: {
                title: 'Demand Note No.',
                type: 'string'
            },
            dateGenerated: {
                title: 'Date Generated',
                type: 'string'
            },
            nameImporter: {
                title: 'Name of Importer',
                type: 'string'
            },
            totalAmount: {
                title: 'Total Amount',
                type: 'custom',
                renderComponent: CurrencyFormatterComponent,
            },
            receiptNo: {
                title: 'Receipt No.',
                type: 'string'
            },
            swStatus: {
                title: 'Sent to SW',
                type: 'string',
                valuePrepareFunction: (status) => {
                    if (status === 1) {
                        return 'Yes'
                    }
                    return "No"
                },
            },
            paymentStatus: {
                title: 'Paid',
                type: 'string',
                valuePrepareFunction: (status) => {
                    if (status === 1) {
                        return 'Yes'
                    }
                    return "NO"
                },
            }
        },
        pager: {
            display: true,
            perPage: 20
        }
    };

    constructor(private diService: DestinationInspectionService, private dialog:MatDialog) {
    }

    ngOnInit(): void {
        this.loadData()
    }

    searchTransactions() {
        this.currentPage=0
        this.loadData()
    }

    toggleStatus(status) {
        if (status !== this.activeStatus) {
            this.activeStatus = status
            if (this.activeStatus == 'pending') {
                this.transactionStatus = 0
            } else {
                this.transactionStatus = 1
            }
            this.loadData()
        }
    }

    pageChange(pageIndex?: number) {
        if (pageIndex && pageIndex != this.currentPage) {
            this.currentPage = pageIndex - 1;
            console.log("Page: "+pageIndex+":"+this.currentPage)
            this.loadData()
            this.loadTransactionStats()
        }
    }

    loadTransactionStats() {
        this.diService.demandNoteStats(this.transactionDate)
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.transactionStats = res.data
                    } else {
                        this.transactionStats = null
                    }
                }
            )


    }

    loadData() {
        this.demandNoteTransactions = []
        this.diService.demandNoteListAndSearch(this.transactionNo, this.transactionDate, this.transactionStatus, this.currentPage, this.defaultPageSize)
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.demandNoteTransactions = res.data
                    } else {
                        this.demandNoteTransactions = null
                        this.diService.showError(res.message, null)
                    }
                },
                error => {
                    this.diService.showError("Failed to load data, please check your network connection", null)
                }
            )
    }
    viewDemandNote(demandNoteId: any){
        this.dialog.open(ViewDemandNoteComponent,{
            data: {
                id: demandNoteId
            }
        }).afterClosed()
            .subscribe(
                res=> {
                    if(res) {
                        this.loadData()
                    }
                }
            )
    }
    onCustomAction(action) {
        switch (action.action) {
            case 'viewRecord':
                this.viewDemandNote(action.data.id)
                break
        }
    }
}
