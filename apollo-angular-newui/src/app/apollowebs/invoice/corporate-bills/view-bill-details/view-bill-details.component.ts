import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {LocalDataSource} from "ng2-smart-table";
import {FinanceInvoiceService} from "../../../../core/store/data/invoice/finance-invoice.service";

@Component({
    selector: 'app-view-bill-details',
    templateUrl: './view-bill-details.component.html',
    styleUrls: ['./view-bill-details.component.css']
})
export class ViewBillDetailsComponent implements OnInit {
    billDetails: any
    page = 0;
    pageSize = 20
    dataSet: LocalDataSource

    constructor(@Inject(MAT_DIALOG_DATA) public data: any, private fiService: FinanceInvoiceService) {
    }

    ngOnInit(): void {
        this.billDetails = this.data.billDetails
        this.dataSet = new LocalDataSource()
        this.loadBillTransactions()
    }

    loadBillTransactions() {
        this.fiService.loadBillTransactions(this.billDetails.id, this.billDetails.corporateId, this.page, this.pageSize)
            .subscribe(
                res => {
                    if (res.responseCode == "00") {
                        this.dataSet.load(res.data.transactions)
                            .then(res => {
                                // Loaded
                            })
                        this.billDetails = res.data.bill
                    } else {
                        this.fiService.showError(res.message, null)
                    }
                }
            )
    }

}
