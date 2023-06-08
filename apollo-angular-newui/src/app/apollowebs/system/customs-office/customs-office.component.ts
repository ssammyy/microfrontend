import {Component, OnInit} from '@angular/core';
import {LocalDataSource} from "ng2-smart-table";
import {DatePipe} from "@angular/common";
import {DestinationInspectionService} from "../../../core/store/data/di/destination-inspection.service";
import {MatDialog} from "@angular/material/dialog";
import {AddCustomOfficeComponent} from "./add-custom-office/add-custom-office.component";

@Component({
    selector: 'app-customs-office',
    templateUrl: './customs-office.component.html',
    styleUrls: ['./customs-office.component.css']
})
export class CustomsOfficeComponent implements OnInit {

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
                {name: 'editRecord', title: '<i class="fa fa-pencil-alt">Edit</i>'},
                {name: 'deleteRecord', title: '<i class="fa fa-trash-alt">Delete</i>'},
            ],
            position: 'right' // left|right
        },
        noDataMessage: 'No Customs office found',
        columns: {
            customsOfficeCode: {
                title: 'Office Code',
                type: 'string',
            },
            customsOfficeName: {
                title: 'Office Name',
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

    addCustomOffice(event?: any) {
        this.dialog.open(AddCustomOfficeComponent, {
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

    deleteCustomOffice(data) {
        this.diSevice.showConfirmation("Are you sure you want to remove this custom office?",
            res => {
                if (res) {
                    this.diSevice.deleteCustomOffice(data.id)
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

    loadData() {
        this.diSevice.listCustomOffices(this.currentPageInternal, this.pageSize)
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

    auctionEvent(actions) {
        switch (actions.action) {
            case 'editRecord':
                this.addCustomOffice(actions.data)
                break
            case 'deleteRecord':
                this.deleteCustomOffice(actions.data)
                break
        }
    }

}
