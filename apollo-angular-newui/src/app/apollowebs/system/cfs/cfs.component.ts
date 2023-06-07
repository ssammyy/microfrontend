import {Component, OnInit} from '@angular/core';
import {DestinationInspectionService} from "../../../core/store/data/di/destination-inspection.service";
import {DatePipe} from "@angular/common";
import {LocalDataSource} from "ng2-smart-table";
import {MatDialog} from "@angular/material/dialog";
import {AddCfsComponent} from "./add-cfs/add-cfs.component";

@Component({
    selector: 'app-cfs',
    templateUrl: './cfs.component.html',
    styleUrls: ['./cfs.component.css']
})
export class CfsComponent implements OnInit {
    dataSet: LocalDataSource = new LocalDataSource();
    message: any
    page: number = 0
    pageSize: number = 20;
    currentPageInternal: number;
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
                {name: 'editRecord', title: '<i class="fa fa-pencil-alt">Edit</i>'},
                {name: 'deleteRecord', title: '<i class="fa fa-trash-alt">Delete</i>'},
            ],
            position: 'right' // left|right
        },
        noDataMessage: 'No CFS found',
        columns: {
            cfsName: {
                title: 'CFS Name',
                type: 'string',
                filter: false
            },
            cfsCode: {
                title: 'CFS code',
                type: 'string',
                filter: false
            },
            altCfsCode: {
                title: 'Alt CFS code',
                type: 'string',
            },
            revenueLineNumber: {
                title: 'Revenue Line No.',
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

    constructor(private dialog: MatDialog, private diSevice: DestinationInspectionService) {
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

    aadCfsItem(event) {
        this.dialog.open(AddCfsComponent, {
            data: event
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
        this.diSevice.loadCfs(this.currentPageInternal, this.pageSize)
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

    deleteCfs(data) {
        this.diSevice.showConfirmation("Are you sure you want to remove this CFS?",
            res => {
                if (res) {
                    this.diSevice.deleteCfs(data.id)
                        .subscribe(
                            delRes => {
                                if (delRes.responseCode === "00") {
                                    this.loadData()
                                    this.diSevice.showSuccess(delRes.message)
                                } else {
                                    this.diSevice.showError(delRes.message)
                                }
                            }
                        )
                }
            }
        )
    }

    auctionEvent(actions) {
        switch (actions.action) {
            case 'editRecord':
                this.aadCfsItem(actions.data)
                break
            case 'deleteRecord':
                this.deleteCfs(actions.data)
                break
        }
    }

}
