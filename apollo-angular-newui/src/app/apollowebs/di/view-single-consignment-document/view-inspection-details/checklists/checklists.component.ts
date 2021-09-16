import {Component, Input, OnInit} from '@angular/core';
import {DestinationInspectionService} from "../../../../../core/store/data/di/destination-inspection.service";
import {MatDialog} from "@angular/material/dialog";
import {SsfDetailsFormComponent} from "../ssf-details-form/ssf-details-form.component";
import {ScfDetailsFormComponent} from "../scf-details-form/scf-details-form.component";

@Component({
    selector: 'app-checklists',
    templateUrl: './checklists.component.html',
    styleUrls: ['./checklists.component.css']
})
export class ChecklistsComponent implements OnInit {
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
                {name: 'sampleSubmission', title: '<i class="btn btn-sm btn-primary">SSF</i>'},
                {name: 'sampleCollection', title: '<i class="btn btn-sm btn-primary">SCF</i>'}
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
                title: '#',
                type: 'string'
            },
            checklistTypeName: {
                title: 'Checklist Type',
                type: 'string'
            },
            inspection: {
                title: 'Inspection Status',
                type: 'string'
            },
            inspectionDate: {
                title: 'Inspection Date',
                type: 'string'
            },

            feePaid: {
                title: 'Payment Status',
                type: 'string'
            },
            receiptNumber: {
                title: 'Receipt Number',
                type: 'string'
            },
            overallRemarks: {
                title: 'Remarks',
                type: 'string'
            },
            description: {
                title: 'Description',
                type: 'string'
            }
        },
        pager: {
            display: true,
            perPage: 20
        }
    };
    @Input() dataSet: any = [];
    @Input() itemUuid: any;
    @Input() configurations: any

    constructor(private diService: DestinationInspectionService, private dialog: MatDialog) {
    }

    ngOnInit(): void {
    }

    openSampleCollection(data: any) {
        this.dialog.open(ScfDetailsFormComponent, {
            data: {
                uuid: data.uuid,
                details: data
            }
        })
    }

    openSampleSubmission(data: any) {
        this.dialog.open(SsfDetailsFormComponent, {
            data: {
                uuid: data.uuid,
                details: data
            }
        })
    }

    onCustomAction(data: any) {
        switch (data.action) {
            case 'sampleCollection':
                this.openSampleCollection(data.data)
                break
            case "sampleSubmission":
                this.openSampleSubmission(data.data)
                break
            default:
                console.log("Invalid action: " + data.action)

        }
    }

}
