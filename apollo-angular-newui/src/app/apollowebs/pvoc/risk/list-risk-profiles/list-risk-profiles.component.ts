import {Component, OnInit} from '@angular/core';
import {PVOCService} from "../../../../core/store/data/pvoc/pvoc.service";
import {MatDialog} from "@angular/material/dialog";
import {ActivatedRoute, Router} from "@angular/router";
import {DatePipe} from "@angular/common";
import {LocalDataSource} from "ng2-smart-table";
import {GenerateAuctionKraReportComponent} from "../../../di/auction/generate-auction-kra-report/generate-auction-kra-report.component";

@Component({
    selector: 'app-list-risk-profiles',
    templateUrl: './list-risk-profiles.component.html',
    styleUrls: ['./list-risk-profiles.component.css']
})
export class ListRiskProfilesComponent implements OnInit {

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
                // {name: 'editRecord', title: '<i class="fa fa-pencil-alt">Edit</i>'},
                {name: 'viewRecord', title: '<i class="fa fa-eye">View</i>'}
            ],
            position: 'right' // left|right
        },
        delete: {
            deleteButtonContent: '&nbsp;&nbsp;<i class="fa fa-trash-o text-danger"></i>',
            confirmDelete: true
        },
        noDataMessage: 'No Risk profiles found',
        columns: {
            hsCode: {
                title: 'HS Code',
                type: 'string',
                filter: false
            },
            brandName: {
                title: 'Brand Name',
                type: 'string',
                filter: false
            },
            countryOfSupply: {
                title: 'Country of Supply',
                type: 'string'
            },
            importerName: {
                title: 'Importer Name',
                type: 'string',
            },
            categorizationDate: {
                title: 'Cat. Date',
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
            receivedOn: {
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
    dataSet: LocalDataSource = new LocalDataSource()

    constructor(private router: Router, private activeRouter: ActivatedRoute, private pvocService: PVOCService, private dialog: MatDialog) {
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


    downloadReport(event: any) {
        this.dialog.open(GenerateAuctionKraReportComponent)
    }


    searchPhraseChanged() {
        if (this.keywords && this.keywords.length > 0) {
            this.loadData()
        }
    }

    viewRiskProfile(recordId: any) {
        this.router.navigate(["/pvoc/risk/profile", recordId])
    }

    auctionEvent(action: any) {
        switch (action.action) {
            case "viewRecord": {
                this.viewRiskProfile(action.data.riskId)
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
        this.pvocService.loadRiskProfiles(this.keywords, this.currentPageInternal, this.pageSize)
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
