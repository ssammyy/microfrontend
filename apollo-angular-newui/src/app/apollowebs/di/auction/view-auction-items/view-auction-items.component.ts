import {Component, OnInit} from '@angular/core';
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";
import {DatePipe} from "@angular/common";
import {LocalDataSource} from "ng2-smart-table";
import {MatDialog} from "@angular/material/dialog";
import {UploadFileComponent} from "../upload-file/upload-file.component";
import {ActivatedRoute, Router} from "@angular/router";
import {AddAuctionRecordComponent} from "../add-auction-record/add-auction-record.component";
import {GenerateAuctionKraReportComponent} from "../generate-auction-kra-report/generate-auction-kra-report.component";

@Component({
    selector: 'app-view-auction-items',
    templateUrl: './view-auction-items.component.html',
    styleUrls: ['./view-auction-items.component.css']
})
export class ViewAuctionItemsComponent implements OnInit {
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
        rowClassFunction: (row) => {
            // console.log(row)
            if (row.data.auctionLotNo === row.data.chassisNo && row.data.blNumber === row.data.chassisNo && row.data.containerNumber === row.data.chassisNo) {
                return 'risky';
            } else {
                return ''
            }
        },
        noDataMessage: 'No data found',
        columns: {
            auctionLotNo: {
                title: 'Lot Number',
                type: 'string',
                filter: false
            },
            shipmentPort: {
                title: 'Shipment Port',
                type: 'string',
                filter: false
            },
            arrivalDate: {
                title: 'Arrival Date',
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
            importerName: {
                title: 'Importer Name',
                type: 'string',
                filter: false
            },
            location: {
                title: 'Current Location',
                type: 'string'
            },
            cfsCode: {
                title: 'CFS Station',
                type: 'string'
            },
            categoryName: {
                title: 'Category',
                type: 'string',
            },
            auctionDate: {
                title: 'Auction Date',
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
            approvedRejectedOn: {
                title: 'Approval/Rejection Date',
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
            approvalStatusDesc: {
                title: 'Status',
                type: 'string'
            },
        },
        pager: {
            display: true,
            perPage: 20
        }
    };
    dataSet: LocalDataSource = new LocalDataSource()

    constructor(private router: Router, private activeRouter: ActivatedRoute, private diService: DestinationInspectionService, private dialog: MatDialog) {
    }

    ngOnInit(): void {
        this.loadCategories()
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

    loadCategories() {
        this.diService.loadAuctionCategories()
            .subscribe(
                res => {
                    if (res.responseCode == "00") {
                        this.categories = res.data
                    }
                }
            )
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

    aadAuctionItem(event: any) {
        console.log("Add: Auction item")
        this.dialog.open(AddAuctionRecordComponent, {
            data: {
                categories: this.categories
            }
        }).afterClosed()
            .subscribe(
                res => {
                    if (res) {
                        this.loadData()
                    }
                }
            )
    }

    downloadReport(event: any) {
        this.dialog.open(GenerateAuctionKraReportComponent)
    }

    uploadAuction(event: any) {
        this.dialog.open(UploadFileComponent)
            .afterClosed()
            .subscribe(
                res => {
                    if (res) {
                        this.auctionType = 'new'
                        this.loadData()
                    }
                }
            )
    }

    searchPhraseChanged() {
        if (this.keywords && this.keywords.length > 0) {
            this.loadData()
        }
    }

    searchAuction(event) {
        if (event) {
            this.keywords = event.target.value
            this.searchStatus = 'yes'
            // Start search
            if (this.keywords && this.keywords.length > 0) {
                this.loadData()
            }
        }
    }

    addUpdateItem(data?: any) {

    }

    viewAuctionRecord(recordId: any) {
        this.router.navigate(["/di/auction/details/", recordId])
    }

    auctionEvent(action: any) {
        switch (action.action) {
            case "viewRecord": {
                this.viewAuctionRecord(action.data.requestId)
                break
            }
            case "editRecord": {
                this.addUpdateItem(action.data)
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
        this.diService.listAuctionItems(this.keywords, this.auctionType, this.currentPageInternal, this.pageSize)
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.dataSet.load(res.data).then(r => {
                        })
                        this.totalCount = res.totalCount
                    } else {
                        this.diService.showError(res.message)
                    }
                },
                error => {
                    this.diService.showError("Failed to load data: " + error.toString())
                }
            )
    }
}
