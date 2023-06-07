import {Component, OnInit} from '@angular/core';
import {LocalDataSource} from "ng2-smart-table";
import {DatePipe} from "@angular/common";
import {DestinationInspectionService} from "../../../core/store/data/di/destination-inspection.service";
import {MatDialog} from "@angular/material/dialog";
import {AddUpdateFeeComponent} from "./add-update-fee/add-update-fee.component";

@Component({
    selector: 'app-inspection-fees',
    templateUrl: './inspection-fees.component.html',
    styleUrls: ['./inspection-fees.component.css']
})
export class InspectionFeesComponent implements OnInit {

    dataSet: LocalDataSource = new LocalDataSource();
    message: any
    page: number = 0
    pageSize: number = 20;
    currentPageInternal: number = 0;
    totalCount: number;
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
                {name: 'viewRecord', title: '<i class="fa fa-eye">View</i>'},
                {name: 'editRecord', title: '<i class="fa fa-pencil-alt">Edit</i>'},
                {name: 'deleteRecord', title: '<i class="fa fa-trash-alt">Delete</i>'},
            ],
            position: 'right' // left|right
        },
        noDataMessage: 'No CFS found',
        columns: {
            name: {
                title: 'Fee Name',
                type: 'string',
                filter: false
            },
            rateType: {
                title: 'Rate Type',
                type: 'string',
                filter: false
            },
            rate: {
                title: 'Rate',
                type: 'string',
            },
            goodCode: {
                title: 'Product Code',
                type: 'string',
            },
            description: {
                title: 'Description',
                type: 'string',
                filter: false
            },
            createdOn: {
                title: 'Date Added',
                type: 'date',
                valuePrepareFunction: (date) => {
                    if (date) {
                        try {
                            return new DatePipe('en-US').transform(date, 'dd/MM/yyyy');
                        } catch (e) {
                            return date
                        }
                    }
                    return "N/A"
                },
            },
        },
        pager: {
            display: true,
            perPage: 20
        }
    };

    constructor(private diSevice: DestinationInspectionService, private dialog: MatDialog) {
    }

    ngOnInit(): void {
        this.loadData()
    }

    pageChange(pageIndex) {
        if (pageIndex) {
            this.currentPageInternal = pageIndex - 1
            this.page = pageIndex
            this.loadData()
        }
    }

    aadInspectionFeeItem(data?: any) {
        this.dialog.open(AddUpdateFeeComponent, {
            data: data ? {
                id: data.id,
                description: data.description,
                rate: data.rate ? data.rate : data.amount,
                rateType: data.rateType,
                name: data.name,
                currencyCode: data.minimumKsh ? 'KSH' : 'USD',
                minimumAmount: data.minimumKsh ? data.minimumKsh : data.minimumUsd,
                maximumAmount: data.maximumKsh ? data.maximumKsh : data.maximumUsd,
                status: data.status ? data.status : '0',
                goodCode: data.goodCode
            } : null
        }).afterClosed()
            .subscribe(
                res => {
                    if (res) {
                        this.loadData()
                    }
                }
            )
    }

    loadData() {
        this.diSevice.loadInspectionFees(this.currentPageInternal, this.pageSize)
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.dataSet.empty()
                        this.dataSet.load(res.data)
                        this.totalCount = res.totalCount
                    } else {
                        this.message = res.message
                    }
                },
                error => {
                    this.message = error.message
                }
            )
    }

    deleteInspectionFee(data: any) {
        this.diSevice.showConfirmation("Are you sure you want to remove this inspection fee?",
            res => {
                if (res) {
                    this.diSevice.deleteInspectionFee(data.id)
                        .subscribe(
                            delRes => {
                                if (delRes.responseCode === "00") {
                                    this.loadData()
                                    this.diSevice.showSuccess(delRes.message)
                                } else {
                                    this.loadData()
                                    this.diSevice.showError(delRes.message)
                                }
                            }
                        )
                }
            }
        )
    }

    viewInspectionFee(date: any) {

    }

    auctionEvent(actions) {
        switch (actions.action) {
            case "deleteRecord":
                this.deleteInspectionFee(actions.data)
                break
            case "viewRecord":
                this.viewInspectionFee(actions.data)
                break
            case "editRecord":
                this.aadInspectionFeeItem(actions.data)
                break
        }
    }

}
