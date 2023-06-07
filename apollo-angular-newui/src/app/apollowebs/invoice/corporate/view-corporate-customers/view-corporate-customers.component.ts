import {Component, OnInit} from '@angular/core';
import {FinanceInvoiceService} from "../../../../core/store/data/invoice/finance-invoice.service";
import {MatDialog} from "@angular/material/dialog";
import {AddUpdateCorporateCustomerComponent} from "../add-update-corporate-customer/add-update-corporate-customer.component";
import {Router} from "@angular/router";

@Component({
    selector: 'app-view-corporate-customers',
    templateUrl: './view-corporate-customers.component.html',
    styleUrls: ['./view-corporate-customers.component.css']
})
export class ViewCorporateCustomersComponent implements OnInit {
    displayedColumns = ["corporateIdentifier", "corporateName", "corporateType", "corporateEmail", "corporatePhone", "corporateBillNumber", "lastPayment", "accountBlocked", "accountSuspended", "actions"]
    keywords: string
    corporatesListing: any
    page = 0
    pageSize = 20

    constructor(private fiService: FinanceInvoiceService, private dialog: MatDialog, private router: Router) {
    }

    ngOnInit(): void {
        this.loadData()
    }

    loadData() {
        this.fiService.listCorporateCustomers(this.page, this.pageSize)
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.corporatesListing = res.data
                    } else {
                        this.fiService.showError(res.message)
                    }
                },
                error => {
                    this.fiService.showError(error.toString())
                }
            )
    }

    searchPhraseChanged() {

    }

    addUpdateCorporate(corporate?: any) {
        this.dialog.open(AddUpdateCorporateCustomerComponent, {
            data: corporate
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

    viewCorporate(corporateId: any) {
        console.log(corporateId)
        this.router.navigate(["/transaction/corporate", corporateId])
    }
}
