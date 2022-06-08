import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {FinanceInvoiceService} from "../../../../core/store/data/invoice/finance-invoice.service";
import {MatDialog} from "@angular/material/dialog";
import {ManageCorporateCustomerComponent} from "../manage-corporate-customer/manage-corporate-customer.component";
import {AddUpdateCorporateCustomerComponent} from "../add-update-corporate-customer/add-update-corporate-customer.component";

@Component({
    selector: 'app-view-corporate',
    templateUrl: './view-corporate.component.html',
    styleUrls: ['./view-corporate.component.css']
})
export class ViewCorporateComponent implements OnInit {
    displayedColumns = ["billNumber", "billReference", "invoiceNumber", "billAmount", "penaltyAmount", "totalAmount", "paymentDate", "paymentReceipts", "actions"]

    active: number = 0
    page = 0
    pageSize = 20
    corporateId: any
    corporateDetails: any
    billList: any
    paidBills: any

    constructor(private activatedRoute: ActivatedRoute, private dialog: MatDialog, private router: Router, private  fiService: FinanceInvoiceService) {
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

    goBack() {
        this.router.navigate(["/transaction/corporates-customers"])
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
        this.fiService.loadBillStatus(this.corporateId, status, this.page, this.pageSize)
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

    editAccount() {
        this.dialog.open(AddUpdateCorporateCustomerComponent, {
            data: this.corporateDetails
        })
            .afterClosed()
            .subscribe(
                res => {
                    if (res) {
                        this.loadData()
                    }
                }
            )
    }

    manageAccount() {
        this.dialog.open(ManageCorporateCustomerComponent, {
            data: {
                corporateId: this.corporateId
            }
        })
            .afterClosed()
            .subscribe(
                res => {
                    if (res) {
                        this.loadData()
                    }
                }
            )
    }

    closeBill(billId: any) {
        this.fiService.closeAndSendBillCorporateAction(billId)
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.fiService.showSuccess(res.message, null)
                    } else {
                        this.fiService.showError(res.message, null)
                    }
                }
            )
    }

    viewTransactions(billId: any) {
        this.router.navigate(["/transaction/bill/", this.corporateId, billId])
    }

}
