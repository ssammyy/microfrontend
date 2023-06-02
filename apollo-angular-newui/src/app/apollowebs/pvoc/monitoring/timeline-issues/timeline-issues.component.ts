import {Component, OnInit} from '@angular/core';
import {DatePipe} from "@angular/common";
import {LocalDataSource} from "ng2-smart-table";
import {ActivatedRoute, Router} from "@angular/router";
import {PVOCService} from "../../../../core/store/data/pvoc/pvoc.service";
import {MatDialog} from "@angular/material/dialog";
import {GenerateAuctionKraReportComponent} from "../../../di/auction/generate-auction-kra-report/generate-auction-kra-report.component";

@Component({
    selector: 'app-timeline-issues',
    templateUrl: './timeline-issues.component.html',
    styleUrls: ['./timeline-issues.component.css']
})
export class TimelineIssuesComponent implements OnInit {

    tabs = ['open', 'review', 'approve', 'rejected', 'search']
    activeStatus = 'open'
    searchStatus = null
    categories: any
    keywords: any
    page = 0
    pageSize = 20
    totalCount: any
    currentPageInternal: any = 0
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
                // {name: 'editRecord', title: '<i class="fa fa-pencil-alt">Edit</i>'},
                {name: 'viewRecord', title: '<i class="fa fa-eye">View</i>'}
            ],
            position: 'right' // left|right
        },
        delete: {
            deleteButtonContent: '&nbsp;&nbsp;<i class="fa fa-trash-o text-danger"></i>',
            confirmDelete: true
        },
        noDataMessage: 'No Monitoring Issue with this status',
        columns: {
            partnerName: {
                title: 'Partner Name',
                type: 'string',
                filter: false
            },
            yearMonth: {
                title: 'Year Month',
                type: 'string',
                filter: false
            },
            recordNumber: {
                title: 'Record Number',
                type: 'string'
            },
            monitoringStatusDesc: {
                title: 'Status',
                type: 'string'
            },
            createdOn: {
                title: 'Record Date',
                type: 'date',
                valuePrepareFunction: (date) => {
                    if (date) {
                        try {
                            return new DatePipe('en-US').transform(date, 'dd/MM/yyyy');
                        } catch (e) {
                            return date
                        }
                    }
                    return ""
                },
            },
            modifiedOn: {
                title: 'Last Update',
                type: 'date',
                valuePrepareFunction: (date) => {
                    if (date) {
                        try {
                            return new DatePipe('en-US').transform(date, 'dd/MM/yyyy');
                        } catch (e) {
                            return date
                        }
                    }
                    return ""
                },
            }
        },
        pager: {
            display: true,
            perPage: 20
        }
    };
    dataSet: LocalDataSource = new LocalDataSource()

    constructor(private router: Router, private activeRouter: ActivatedRoute, private pvocService: PVOCService, private dialog: MatDialog) {
    }

    ngOnInit(): void {
        this.activeRouter.queryParamMap.subscribe(res => {
            if (res.has("tab")) {
                let t = res.get('tab')
                if (t in this.tabs) {
                    this.activeStatus = t;
                }
            }
            this.loadData()
        })
    }


    toggleStatus(status: string): void {
        console.log(status)
        if (status !== this.activeStatus) {
            this.activeStatus = status;
            this.page = 0
            this.searchStatus = null
            this.keywords = null
            this.currentPageInternal = 0
            // Update URL
            this.router.navigate([], {
                queryParams: {
                    tab: status
                }
            })
            this.loadData()
        }
    }

    pageChange(pageIndex?: any) {
        if (pageIndex) {
            this.currentPageInternal = pageIndex - 1
            this.page = pageIndex
            this.loadData()
        }
    }


    downloadReport(event: any) {
        this.dialog.open(GenerateAuctionKraReportComponent)
    }


    searchPhraseChanged() {
        if (this.keywords && this.keywords.length > 0) {
            this.loadData()
        }
    }

    viewMonitoringItem(recordId: any) {
        this.router.navigate(["/monitoring/issue/", recordId])
    }

    auctionEvent(action: any) {
        switch (action.action) {
            case "viewRecord": {
                this.viewMonitoringItem(action.data.monitoringId)
                break
            }
        }
    }

    loadData() {
        this.dataSet.reset(true)
        if (this.keywords && this.keywords.length > 0) {
            this.searchStatus = 'Yes'
            this.activeStatus = 'search'
        }
        this.pvocService.loadMonitoringIssues(this.keywords, this.activeStatus, this.currentPageInternal, this.pageSize)
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.dataSet.load(res.data).then(r => {
                        })
                        this.totalCount = res.totalCount
                    } else {
                        this.pvocService.showError(res.message)
                    }
                },
                error => {
                    this.pvocService.showError("Failed to load data: " + error.toString())
                }
            )
    }

}
