import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";
import {MatDialog} from "@angular/material/dialog";
import {ApproveRejectApplicationComponent} from "../../ism/approve-reject-application/approve-reject-application.component";
import {ViewDemandNoteComponent} from "../../demand-note-list/view-demand-note/view-demand-note.component";
import {DatePipe} from "@angular/common";

@Component({
    selector: 'app-auction-item-details',
    templateUrl: './auction-item-details.component.html',
    styleUrls: ['./auction-item-details.component.css']
})
export class AuctionItemDetailsComponent implements OnInit {

    requestId: any
    auctionDetails: any
    activeTab: number = 0

    public itemSettings = {
        selectMode: 'single',  // single|multi
        hideHeader: false,
        hideSubHeader: false,
        actions: {
            columnTitle: 'Actions',
            add: false,
            edit: false,
            delete: false,
            custom: [
                // {name: 'requestMinistryChecklist', title: '<i class="btn btn-sm btn-primary">MINISTRY CHECKLIST</i>'},
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
            id: {
                title: 'Number',
                type: 'string'
            },
            serialNo: {
                title: 'Serial No.',
                type: 'string'
            },
            chassisNo: {
                title: 'Chassis No.',
                type: 'string'
            },
            itemName: {
                title: 'Item Name',
                type: 'string'
            },
            itemType: {
                title: 'Item Type',
                type: 'string'
            },
            quantity: {
                title: 'Quantity',
                type: 'string'
            }
        },
        pager: {
            display: true,
            perPage: 20
        }
    };
    // History

    public historySettings = {
        selectMode: 'single',  // single|multi
        hideHeader: false,
        hideSubHeader: false,
        actions: {
            columnTitle: 'Actions',
            add: false,
            edit: false,
            delete: false,
            custom: [
                // {name: 'requestMinistryChecklist', title: '<i class="btn btn-sm btn-primary">MINISTRY CHECKLIST</i>'},
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
            actionName: {
                title: 'Action',
                type: 'string'
            },
            description: {
                title: 'Description',
                type: 'string'
            },
            username: {
                title: 'User',
                type: 'string'
            },
            createdOn: {
                title: 'Date',
                type: 'date',
                valuePrepareFunction: (date) => {
                    if (date) {
                        return new DatePipe('en-US').transform(date, 'dd/MM/yyyy hh:mm');
                    }
                    return ""
                },
            },
        },
        pager: {
            display: true,
            perPage: 20
        }
    };

    constructor(private activeRoute: ActivatedRoute, private router: Router, private diService: DestinationInspectionService, private dialog: MatDialog) {
    }

    ngOnInit(): void {
        this.activeRoute.paramMap
            .subscribe(
                res => {
                    this.requestId = res.get("id")
                    this.loadData()
                }
            )
    }

    goBack() {
        this.router.navigate(["/di/auction/view"])
    }

    uploadAuctionReport(event: any){

    }

    generateInvoice() {

    }

    viewConsignmentDocument() {
        this.router.navigate(["/di", this.auctionDetails.cd_uuid])
    }

    viewDemandNote(demandNoteId: any) {
        this.dialog.open(ViewDemandNoteComponent, {
            data: {
                id: demandNoteId
            }
        })
    }

    approveRejectConsignment() {
        this.dialog.open(ApproveRejectApplicationComponent, {
            data: {
                requestId: this.requestId
            }
        })
            .afterClosed()
            .subscribe(
                res => {
                    if (res) {
                        this.loadData()
                    }
                }
            )
    }

    downloadISMReport() {

    }

    loadData() {
        this.diService.getAuctionItemDetails(this.requestId)
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.auctionDetails = res.data
                    } else {
                        this.diService.showError(res.message, this.goBack)
                    }
                }
            )
    }
}
