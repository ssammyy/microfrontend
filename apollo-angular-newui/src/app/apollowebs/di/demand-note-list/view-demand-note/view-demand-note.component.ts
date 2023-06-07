import {Component, Inject, OnInit} from '@angular/core';
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {CurrencyFormatterComponent} from "../../../../core/shared/currency-formatter/currency-formatter.component";

@Component({
    selector: 'app-view-demand-note',
    templateUrl: './view-demand-note.component.html',
    styleUrls: ['./view-demand-note.component.css']
})
export class ViewDemandNoteComponent implements OnInit {
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
                // {name: 'viewNote', title: '<i class="btn btn-sm btn-primary">View</i>'},
                // {name: 'download', title: '<i class="btn btn-sm btn-primary">Download</i>'}
            ],
            position: 'right' // left|right
        },
        delete: {
            deleteButtonContent: '&nbsp;&nbsp;<i class="fa fa-trash-o text-danger"></i>',
            confirmDelete: true
        },
        noDataMessage: 'No data found',
        columns: {
            feeName: {
                title: 'FEE NAME',
                type: 'string'
            },
            product: {
                title: 'PRODUCT',
                type: 'string'
            },
            "varField1": {
                title: "QUANTITY",
                type: "string"
            },
            cfvalue: {
                title: 'CF VALUE',
                type: 'custom',
                renderComponent: CurrencyFormatterComponent
            },
            rateType: {
                title: 'RATE TYPE',
                type: 'string'
            },
            rate: {
                title: 'RATE',
                type: 'string'
            },
            amountPayable: {
                title: 'CALCULATED AMOUNT',
                type: 'custom',
                renderComponent: CurrencyFormatterComponent
            },
            adjustedAmount: {
                title: 'PAYABLE AMOUNT',
                type: 'custom',
                renderComponent: CurrencyFormatterComponent
            }
        },
        pager: {
            display: true,
            perPage: 20
        }
    };
    public paymentsSettings = {
        selectMode: 'single',  // single|multi
        hideHeader: false,
        hideSubHeader: false,
        actions: {
            columnTitle: 'Actions',
            add: false,
            edit: false,
            delete: false,
            custom: [
                // {name: 'viewNote', title: '<i class="btn btn-sm btn-primary">View</i>'},
                // {name: 'download', title: '<i class="btn btn-sm btn-primary">Download</i>'}
            ],
            position: 'right' // left|right
        },
        delete: {
            deleteButtonContent: '&nbsp;&nbsp;<i class="fa fa-trash-o text-danger"></i>',
            confirmDelete: true
        },
        noDataMessage: 'No payments found',
        columns: {
            receiptNumber: {
                title: 'RECEIPT NO.',
                type: 'string'
            },
            referenceNumber: {
                title: 'REF. NO',
                type: 'string'
            },
            previousBalance: {
                title: 'BALANCE BEFORE',
                type: 'string'
            },
            amount: {
                title: 'AMOUNT PAID',
                type: 'custom',
                renderComponent: CurrencyFormatterComponent
            },
            balanceAfter: {
                title: "BALANCE AFTER",
                type: "string"
            },
            receiptDate: {
                title: 'RECEIPT DATE',
                type: 'string'
            },
            paymentSource: {
                title: 'SOURCE',
                type: 'string'
            }
        },
        pager: {
            display: true,
            perPage: 20
        }
    };
    demandNoteId: any
    demandDetails: any
    message: any
    saveLoading = false
    deleteLoading = false

    constructor(private diService: DestinationInspectionService,
                public dialogRef: MatDialogRef<any>,
                @Inject(MAT_DIALOG_DATA) public data: any) {
    }

    ngOnInit(): void {
        this.demandNoteId = this.data.id
        this.loadDemandNote()
    }

    paymentStatus(status: number): String {
        switch (status) {
            case 1:
                return "PAYMENT_COMPLETED"
            case 5:
                return "PARTIAL PAYMENT"
            default:
                return "NOT PAID"
        }
    }

    loadDemandNote() {
        this.diService.demandNoteDetails(this.data.id)
            .subscribe(
                res => {
                    if (res.responseCode == "00") {
                        this.demandDetails = res.data
                    } else {
                        this.message = res.message
                    }
                }
            )
    }

    deleteDemandNote() {
        this.deleteLoading = true
        this.diService.deleteDemandNote(this.demandNoteId)
            .subscribe(
                res => {
                    this.deleteLoading = false
                    if (res.responseCode == "00") {
                        this.diService.showSuccess(res.message, () => {
                            this.dialogRef.close(true)
                        })
                    } else {
                        this.message = res.message
                    }
                },
                error => {
                    this.deleteLoading = false
                }
            )
    }

    submitDemandNote() {
        this.saveLoading = true
        let submission = this.diService.submitDemandNote(this.demandNoteId, {})
        if (this.data.general) {
            submission = this.diService.submitOtherDemandNote(this.demandNoteId, {})
        }
        submission.subscribe(
            res => {
                this.saveLoading = false
                if (res.responseCode == "00") {
                    this.diService.showSuccess(res.message, () => {
                        this.dialogRef.close(true)
                    })
                } else {
                    this.message = res.message
                }
            },
            error => {
                this.saveLoading = false
            }
        )
    }

    formatPostingStatus(status: number) {
        switch (status) {
            case 1:
                return "POSTED"
            case -1:
                return "POSTING FAILED"
            default:
                return "PENDING"
        }
    }

    onCustomAction(action) {

    }
}
