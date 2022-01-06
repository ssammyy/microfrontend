import {Component, OnInit} from '@angular/core';
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";
import {DatePipe} from "@angular/common";
import {ConsignmentStatusComponent} from "../../../../core/shared/customs/consignment-status/consignment-status.component";
import {LocalDataSource} from "ng2-smart-table";

@Component({
    selector: 'app-view-auction-items',
    templateUrl: './view-auction-items.component.html',
    styleUrls: ['./view-auction-items.component.css']
})
export class ViewAuctionItemsComponent implements OnInit {
    auctionType = 'new'
    keywords: any
    page = 0
    pageSize = 20
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
                //  { name: 'editRecord', title: '<i class="btn btn-sm btn-primary">View More</i>' },
                // {name: 'viewRecord', title: '<i class="btn btn-sm btn-primary">View More</i>'}
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
                title: 'Shipment Pport',
                type: 'string',
                filter: false
            },
            arrivalDate: {
                title: 'Arrival Date',
                type: 'date',
                valuePrepareFunction: (date) => {
                    if (date) {
                        return new DatePipe('en-US').transform(date, 'dd/MM/yyyy hh:mm');
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
                title: 'Penalty Type',
                type: 'any',
                valuePrepareFunction: (category) => {
                    if (category) {
                        return category.code
                    }
                    return "NA"
                },
            },
            auctionDate: {
                title: 'Exchange Date',
                type: 'date',
                valuePrepareFunction: (date) => {
                    if (date) {
                        return new DatePipe('en-US').transform(date, 'dd/MM/yyyy hh:mm');
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

    constructor(private diService: DestinationInspectionService) {
    }

    ngOnInit(): void {
        this.loadData()
    }

    uploadAuction(event) {

    }

    searchAuction(event){
        if(event){
            this.keywords=event.target.value
            // Start search
            if(this.keywords && this.keywords.length>0){
                this.loadData()
            }
        }
    }

    loadData() {
        this.dataSet.reset(true)
        this.diService.listAuctionItems(this.keywords, this.auctionType, this.page, this.pageSize)
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.dataSet.load(res.data).then(r => {})
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
