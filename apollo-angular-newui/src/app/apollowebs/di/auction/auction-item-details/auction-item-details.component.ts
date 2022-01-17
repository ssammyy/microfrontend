import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";
import {MatDialog} from "@angular/material/dialog";
import {ApproveRejectApplicationComponent} from "../../ism/approve-reject-application/approve-reject-application.component";
import {ViewDemandNoteComponent} from "../../demand-note-list/view-demand-note/view-demand-note.component";
import {DatePipe} from "@angular/common";
import {AssignOfficerComponent} from "../../forms/assign-officer/assign-officer.component";
import {AssignAuctionItemComponent} from "../assign-auction-item/assign-auction-item.component";
import {AproveRejectAuctionItemComponent} from "../aprove-reject-auction-item/aprove-reject-auction-item.component";
import {GenerateDemandNoteComponent} from "../generate-demand-note/generate-demand-note.component";

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

    public attachmentSettings = {
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
                {name: 'downloadReport', title: '<i class="fa fa-download">Download</i>'}
            ],
            position: 'right' // left|right
        },
        delete: {
            deleteButtonContent: '&nbsp;&nbsp;<i class="fa fa-trash-o text-danger"></i>',
            confirmDelete: true
        },
        noDataMessage: 'No data found',
        columns: {
            name: {
                title: 'Name',
                type: 'string'
            },
            fileType: {
                title: 'File Type',
                type: 'string'
            },
            description: {
                title: 'Description',
                type: 'string'
            },
            fileSize: {
                title: 'Size',
                type: 'string'
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

    assignAuctionItem(reassign: Boolean) {
        const ref = this.dialog.open(AssignAuctionItemComponent, {
            data: {
                auctionId: this.requestId,
                reassign: reassign
            }
        });
        ref.afterClosed()
            .subscribe(
                res => {
                    if (res) {
                        this.loadData();
                    }
                }
            );
    }

    uploadAuctionAttachment(event: any) {
        if (event.target.files && event.target.files.length > 0) {
            this.diService.uploadAuctionReport(event.target.files[0], this.requestId, "Attachment")
                .subscribe(
                    res => {
                        if (res.responseCode === "00") {
                            this.diService.showSuccess(res.message)
                            this.loadData()
                        } else {
                            this.diService.showError(res.message)
                        }
                    }
                )
        }
    }

    generateInvoice() {
        this.dialog.open(GenerateDemandNoteComponent, {
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

    downloadAttachment(attachmentId: number) {
        this.diService.downloadDocument("/api/v1/download/auction/attachment/" + attachmentId + "/" + this.requestId)
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
        this.dialog.open(AproveRejectAuctionItemComponent, {
            data: {
                auctionId: this.requestId
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

    tableActions(action) {
        switch (action.action) {
            case 'downloadReport':
                this.downloadAttachment(action.data.uploadId)
                break
        }
    }

    downloadISMReport() {
        this.downloadAttachment(this.auctionDetails.auction_details.reportId)
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
