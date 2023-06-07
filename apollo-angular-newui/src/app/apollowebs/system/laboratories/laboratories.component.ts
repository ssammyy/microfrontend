import {Component, OnInit} from '@angular/core';
import {LocalDataSource} from "ng2-smart-table";
import {DatePipe} from "@angular/common";
import {DestinationInspectionService} from "../../../core/store/data/di/destination-inspection.service";
import {MatDialog} from "@angular/material/dialog";
import {AddLaboratoryComponent} from "./add-laboratory/add-laboratory.component";

@Component({
    selector: 'app-laboratories',
    templateUrl: './laboratories.component.html',
    styleUrls: ['./laboratories.component.css']
})
export class LaboratoriesComponent implements OnInit {

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
        noDataMessage: 'No CFS found',
        columns: {
            labName: {
                title: 'Lab Name',
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

    aadLaboratory(event?: any) {
        this.dialog.open(AddLaboratoryComponent, {
            data: event ? {
                laboratoryName: event.labName,
                laboratoryDesc: event.description,
                status: event.status,
                id: event.id
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
        this.diSevice.listLaboratories(this.currentPageInternal, this.pageSize)
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

    deleteRecord(event) {
        this.diSevice.showConfirmation("Are you sure you want to remove this laboratory?",
            res => {
                if (res) {
                    this.diSevice.deleteLaboratory(event.id)
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

    auctionEvent(actions) {
        switch (actions.action) {
            case "editRecord":
                this.aadLaboratory(actions.data)
                break
            case "deleteRecord":
                this.deleteRecord(actions.data)
                break
        }
    }

}
