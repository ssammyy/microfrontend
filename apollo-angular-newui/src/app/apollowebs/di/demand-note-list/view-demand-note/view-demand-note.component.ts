import {Component, Inject, OnInit} from '@angular/core';
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
    selector: 'app-view-demand-note',
    templateUrl: './view-demand-note.component.html',
    styleUrls: ['./view-demand-note.component.css']
})
export class ViewDemandNoteComponent implements OnInit {
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
                // {name: 'viewNote', title: '<i class="btn btn-sm btn-primary">View</i>'},
                // {name: 'download', title: '<i class="btn btn-sm btn-primary">Download</i>'}
            ],
            position: 'right' // left|right
        },
        delete: {
            deleteButtonContent: '&nbsp;&nbsp;<i class="fa fa-trash-o text-danger"></i>',
            confirmDelete: true
        },
        noDataMessage: 'No data found',
        columns: {
            product: {
                title: 'PRODUCT',
                type: 'string'
            },
            cfvalue: {
                title: 'CF VALUE',
                type: 'string'
            },
            rateType: {
                title: 'RATE TYPE',
                type: 'string'
            },
            rate: {
                title: 'RATE',
                type: 'string'
            },
            amountPayable: {
                title: 'CALCULATED AMOUNT',
                type: 'string'
            },
            adjustedAmount: {
                title: 'PAYABLE AMOUNT',
                type: 'string'
            }
        },
        pager: {
            display: true,
            perPage: 20
        }
    };
    demandNoteId: any
    demandDetails: any
    message: any

    constructor(private diService: DestinationInspectionService,
                @Inject(MAT_DIALOG_DATA) public data: any) {
    }

    ngOnInit(): void {
        this.demandNoteId = this.data.id
        this.loadDemandNote()
    }

    loadDemandNote() {
        this.diService.demandNoteDetails(this.data.id)
            .subscribe(
                res => {
                    if (res.responseCode == "00") {
                        this.demandDetails = res.data
                    } else {
                        this.message = res.message
                    }
                }
            )
    }

    onCustomAction(action){

    }
}
