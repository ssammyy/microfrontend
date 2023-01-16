import {Component, OnInit} from '@angular/core';
import {FinanceInvoiceService} from "../../../core/store/data/invoice/finance-invoice.service";
import {ActivatedRoute, Router} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {selectUserInfo} from "../../../core/store/data/auth";
import {Store} from "@ngrx/store";
import {DestinationInspectionService} from "../../../core/store/data/di/destination-inspection.service";
import {DataSource} from "ng2-smart-table/lib/lib/data-source/data-source";
import {LocalDataSource} from "ng2-smart-table";

@Component({
    selector: 'app-corporate-bills',
    templateUrl: './corporate-bills.component.html',
    styleUrls: ['./corporate-bills.component.css']
})
export class CorporateBillsComponent implements OnInit {
    displayedColumns = ["billNumber", "customerCode", "customerName", "billReference", "invoiceNumber", "billAmount", "penaltyAmount", "totalAmount", "paymentDate", "paymentReceipts", "actions"]
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
                {name: 'CloseBill', title: '<i class="btn btn-sm btn-primary">Close Bill</i>'}
            ],
            position: 'right' // left|right
        },
        delete: {
            deleteButtonContent: '&nbsp;&nbsp;<i class="fa fa-trash-o text-danger"></i>',
            confirmDelete: true
        },
        noDataMessage: 'No data found',
        columns: {
            billNumber: {
                title: 'Bill No.',
                type: 'string'
            },
            billReference: {
                title: 'Demand Note',
                type: 'string'
            },
            customerCode: {
                title: 'Customer Code',
                type: 'string'
            },
            customerName: {
                title: 'Customer Name',
                type: 'string'
            },
            billAmount: {
                title: 'Bill Amount',
                type: 'string'
            },
            penaltyAmount: {
                title: 'Penalty Amount',
                type: 'string'
            },
            totalAmount: {
                title: 'Total Amount',
                type: 'string'
            },
            paymentReceipts: {
                title: 'Receipt No',
                type: 'string'
            },
            paymentDate: {
                title: 'Receipt Date',
                type: 'string'
            }
        }
    }
    message: any
    billList: DataSource
    keywords: any
    page: any = 0
    pageSize = 20
    currentPage = 0
    totalCount: any
    previousStatus = 'paid'
    activeStatus = 'pending'
    active = 1
    allowedStatuses = ['paid', 'pending', 'open']
    supervisorCharge = false;

    constructor(private activatedRoute: ActivatedRoute, private dialog: MatDialog, private store$: Store<any>, private router: Router, private  fiService: FinanceInvoiceService, private diService: DestinationInspectionService) {
    }

    ngOnInit(): void {
        this.activatedRoute.paramMap.subscribe(
            res => {
                if (res.has("tab")) {
                    this.previousStatus = this.activeStatus
                    if (this.allowedStatuses.includes(res.get("tab"))) {
                        this.activeStatus = res.get("tab")
                    }
                }
                this.billList = new LocalDataSource()
                this.store$.select(selectUserInfo)
                    .subscribe((u) => {
                        this.supervisorCharge = this.diService.hasRole(['DI_OFFICER_CHARGE_READ'], u.roles)
                    })
                this.loadBills()
            }
        )
    }

    pageChange(pageIndex?: number) {
        if (pageIndex && pageIndex != this.currentPage) {
            this.currentPage = pageIndex - 1;
            console.log("Page: " + pageIndex + ":" + this.currentPage)
            this.loadBills()
        }
    }

    closeBill(billId: any) {
        if (this.supervisorCharge) {
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
        } else {
            this.fiService.showError("Only supervisors may close an open bill")
        }
    }

    loadBills(search?: boolean) {
        this.fiService.loadAllBillsStatus(search ? this.keywords : null, this.activeStatus, this.page, this.pageSize)
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.billList = res.data
                        this.totalCount = res.totalCount

                    } else {
                        this.message = res.message
                    }
                }
            )
    }

    toggleStatus(status: string): void {
        console.log(status)
        if (status !== this.activeStatus) {
            this.activeStatus = status;
            this.router.navigate([], {
                queryParams: {
                    tab: status
                }
            }).then((res) => {
                console.log(res)
            })
            this.loadBills()
        }
    }

    onCustomAction(event) {
        switch (event.action) {
            case 'CloseBill':
                this.closeBill(event.data.id)
                break
            default:
                this.message = "Invalid action type: " + event.action
        }
    }

}
