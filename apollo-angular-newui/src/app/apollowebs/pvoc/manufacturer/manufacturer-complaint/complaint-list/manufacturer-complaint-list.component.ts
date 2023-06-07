import {Component, OnInit} from '@angular/core';

import {LocalDataSource} from 'ng2-smart-table';
import {Router} from '@angular/router';
import {PVOCService} from "../../../../../core/store/data/pvoc/pvoc.service";
import {DatePipe} from "@angular/common";
import {MatDialog} from "@angular/material/dialog";
import {ManufacturerComplaintDetailsComponent} from "../complaint-details/manufacturer-complaint-details.component";

@Component({
    selector: 'app-manufacturer-complaint-list',
    templateUrl: './manufacturer-complaint-list.component.html',
    styleUrls: ['./manufacturer-complaint-list.component.css'],
})
export class ManufacturerComplaintListComponent implements OnInit {
    loading = false;

    roles: string[];

    activeStatus = 'new';
    previousStatus = 'new';
    searchStatus: any;
    defaultPageSize = 20;
    defaultPage = 0;
    currentPage = 0;
    currentPageInternal = 0;
    totalCount = 0;
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
                {name: 'viewRecord', title: '<i class="btn btn-sm btn-primary" >View More</i>'},
            ],
            position: 'right', // left|right
        },
        noDataMessage: 'No complaints found',
        columns: {
            refNo: {
                title: 'REFERENCE NO.',
                type: 'string',
                filter: false,
            },
            complaintTitle: {
                title: 'Complaint Title',
                type: 'string',
                filter: false,
            },
            cocNo: {
                title: 'Product Brand',
                type: 'string',
                filter: false,
            },
            pvocAgent: {
                title: 'Agent Name',
                type: 'string',
                filter: false
            },
            rfcNo: {
                title: 'RFC NO.',
                type: 'string',
                filter: false
            },
            categoryName: {
                title: 'Complaint Category',
                type: 'string',
                filter: false
            },
            complaintDate: {
                title: 'Date Received',
                type: 'date',
                filter: false,
                valuePrepareFunction: (date) => {
                    if (date) {
                        return new DatePipe('en-US').transform(date, 'dd/MM/yyyy hh:mm');
                    }
                    return ""
                },
            },
            reviewStatus: {
                title: 'Review Status',
                type: 'string',
                filter: false,
            },
        },
        pager: {
            display: true,
            perPage: 10,
        },
    };
    dataSet: LocalDataSource = new LocalDataSource();
    message: any

    constructor(private router: Router, private dialog: MatDialog, private pvoc: PVOCService) {
    }

    ngOnInit(): void {
        this.loadData(this.defaultPage, this.defaultPageSize);
    }

    pageChange(pageIndex?: any) {
        if (pageIndex) {
            this.currentPageInternal = pageIndex - 1;
            this.currentPage = pageIndex;
            this.loadData(this.currentPageInternal, this.defaultPageSize);
        }
    }

    openComplaint() {
        this.router.navigate(["/manufacturer/complaints/apply"])
            .then(() => {

            })
    }

    goBack() {
        this.router.navigate(["/company/applications"])
            .then(() => {

            })
    }

    private loadData(page: number, records: number): any {
        this.loading = true
        this.pvoc.manufacturerComplaintHistory(this.activeStatus, page, records).subscribe(
            (data) => {
                if (data.responseCode === "00") {
                    this.totalCount = data.totalCount;
                    this.dataSet.load(data.data);
                } else {
                    this.pvoc.showError(data.message, () => {
                        this.goBack()
                    })
                }
                this.loading = false
            },
            error => {
                console.log(error)
                this.pvoc.showError(error.message, () => {
                    this.goBack()
                })
            },
        );
    }


    toggleStatus(status: string): void {
        console.log(status);
        this.message = null;
        this.searchStatus = null;
        if (status !== this.activeStatus) {
            this.activeStatus = status;
            this.loadData(this.defaultPage, this.defaultPageSize);
        }
    }

    public onCustomAction(event: any): void {
        switch (event.action) {
            case 'viewRecord':
                this.viewRecord(event.data);
                break;
        }
    }

    viewRecord(data: any) {
        this.dialog.open(ManufacturerComplaintDetailsComponent, {
            data: data
        }).afterClosed()
            .subscribe(() => {
                console.log("closed")
            })

    }


}
