import {Component, OnInit} from '@angular/core';
import {DatePipe} from "@angular/common";
import {ActivatedRoute, Router} from "@angular/router";
import {PVOCService} from "../../../../core/store/data/pvoc/pvoc.service";
import {LocalDataSource} from "ng2-smart-table";

@Component({
    selector: 'app-list-queries',
    templateUrl: './list-queries.component.html',
    styleUrls: ['./list-queries.component.css']
})
export class ListQueriesComponent implements OnInit {

    tabs = ['assigned', 'new', 'rejected', 'approved', 'search']
    auctionType = 'assigned'
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
                {name: 'viewRecord', title: '<i class="fa fa-eye">View</i>'}
            ],
            position: 'right' // left|right
        },
        delete: {
            deleteButtonContent: '&nbsp;&nbsp;<i class="fa fa-trash-o text-danger"></i>',
            confirmDelete: true
        },
        noDataMessage: 'No queries found',
        columns: {
            serialNumber: {
                title: 'S/N',
                type: 'string',
                filter: false
            },
            queryOrigin: {
                title: 'Origin',
                type: 'string',
                filter: false
            },
            certNumber: {
                title: 'Cert No.',
                type: 'string',
                filter: false
            },
            idfNumber: {
                title: 'IDF No.',
                type: 'string',
                filter: false
            },
            ucrNumber: {
                title: 'UCR No.',
                type: 'string'
            },
            rfcNumber: {
                title: 'RFC No',
                type: 'string',
            },
            conclusionStatus: {
                title: 'Status',
                type: 'string',
                filter: false
            },

            dateClosed: {
                title: 'Closed On',
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
            dateOpened: {
                title: 'Received Date',
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
    dataSet = new LocalDataSource()

    constructor(private router: Router, private activeRouter: ActivatedRoute, private pvocService: PVOCService) {
    }

    ngOnInit(): void {
        this.activeRouter.queryParamMap.subscribe(res => {
            if (res.has("tab")) {
                let t = res.get('tab')
                if (t in this.tabs) {
                    this.auctionType = t;
                }
            }
            this.loadData()
        })
    }


    toggleStatus(status: string): void {
        console.log(status)
        if (status !== this.auctionType) {
            this.auctionType = status;
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


    searchPhraseChanged() {
        if (this.keywords && this.keywords.length > 0) {
            this.loadData()
        }
    }

    viewRiskProfile(recordId: any) {
        this.router.navigate(["/pvoc/query/view", recordId])
    }

    auctionEvent(action: any) {
        switch (action.action) {
            case "viewRecord": {
                this.viewRiskProfile(action.data.queryId)
                break
            }
        }
    }

    loadData() {
        this.dataSet.reset(true)
        if (this.keywords && this.keywords.length > 0) {
            this.searchStatus = 'Yes'
            this.auctionType = 'search'
        }
        this.pvocService.loadQueries(this.keywords, this.currentPageInternal, this.pageSize)
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
