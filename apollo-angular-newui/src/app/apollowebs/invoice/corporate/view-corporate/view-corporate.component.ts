import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {FinanceInvoiceService} from "../../../../core/store/data/invoice/finance-invoice.service";

@Component({
    selector: 'app-view-corporate',
    templateUrl: './view-corporate.component.html',
    styleUrls: ['./view-corporate.component.css']
})
export class ViewCorporateComponent implements OnInit {
    displayedColumns = ["billNumber", "invoiceNumber", "billAmount", "penaltyAmount", "totalAmount", "paymentDate", "paymentReceipts", "actions"]

    active: number = 0
    page = 0
    pageSize = 20
    corporateId: any
    corporateDetails: any
    billList: any
    paidBills: any

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private  fiService: FinanceInvoiceService) {
    }

    ngOnInit(): void {
        this.activatedRoute.paramMap.subscribe(
            res => {
                this.corporateId = res.get("id")
                this.loadData()
                this.loadBills("paid")
                this.loadBills("pending")
            }
        )
    }

    loadData() {
        this.fiService.getCorporateDetails(this.corporateId)
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.corporateDetails = res.data
                    } else {
                        this.fiService.showError(res.message, () => {
                            this.router.navigate(["/transaction/corporates-customers"])
                        })
                    }
                }
            )
    }

    loadBills(status: any) {
        this.fiService.loadBillStatus(this.corporateId,status, this.page, this.pageSize)
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        switch (status) {
                            case "paid":
                                this.paidBills = res.data.bills
                                break
                            default:
                                this.billList = res.data.bills
                        }
                    }
                }
            )
    }

    manageAccount() {

    }

    viewTransactions(billId: any) {
        this.router.navigate(["/transaction/bill/",this.corporateId, billId])
    }

}
