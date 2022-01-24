import {Component, OnInit} from '@angular/core';
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";
import {DatePipe} from "@angular/common";
import {ConsignmentStatusComponent} from "../../../../core/shared/customs/consignment-status/consignment-status.component";
import {LocalDataSource} from "ng2-smart-table";
import {MatDialog} from "@angular/material/dialog";
import {UploadFileComponent} from "../upload-file/upload-file.component";
import {Router} from "@angular/router";
import {AddAuctionRecordComponent} from "../add-auction-record/add-auction-record.component";
import {GenerateAuctionKraReportComponent} from "../generate-auction-kra-report/generate-auction-kra-report.component";

@Component({
    selector: 'app-view-auction-items',
    templateUrl: './view-auction-items.component.html',
    styleUrls: ['./view-auction-items.component.css']
})
export class ViewAuctionItemsComponent implements OnInit {
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
                {name: 'editRecord', title: '<i class="fa fa-pencil-alt">Edit</i>'},
                {name: 'viewRecord', title: '<i class="fa fa-eye">View</i>'}
            ],
            position: 'right' // left|right
        },
        delete: {
            deleteButtonContent: '&nbsp;&nbsp;<i class="fa fa-trash-o text-danger"></i>',
            confirmDelete: true
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
                        }catch (e) {
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
            importerPhone: {
                title: 'Importer Phone',
                type: 'string'
            },
            category: {
                title: 'Category',
                type: 'any',
                valuePrepareFunction: (category) => {
                    if (category) {
                        return category.categoryCode
                    }
                    return "NA"
                },
            },
            auctionDate: {
                title: 'Auction Date',
                type: 'date',
                valuePrepareFunction: (date) => {
                    if (date) {
                        try {
                            return new DatePipe('en-US').transform(date, 'dd/MM/yyyy');
                        }catch (e) {
                            return date
                        }
                    }
                    return ""
                },
            },
            status: {
                title: 'Status',
                type: 'custom',
                renderComponent: ConsignmentStatusComponent
            }
        },
        pager: {
            display: true,
            perPage: 20
        }
    };
    dataSet: LocalDataSource = new LocalDataSource()

    constructor(private router: Router, private diService: DestinationInspectionService, private dialog: MatDialog) {
    }

    ngOnInit(): void {
        this.loadData()
        this.loadCategories()
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

    auctionEvent(action: any) {
        switch (action.action) {
            case "viewRecord": {
                this.router.navigate(["/di/auction/details/", action.data.requestId])
                break
            }
            case "editRecord": {
                this.addUpdateItem(action.data)
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