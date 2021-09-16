import {Component, Input, OnInit} from '@angular/core';
import {DestinationInspectionService} from "../../../../../core/store/data/di/destination-inspection.service";
import {MatDialog} from "@angular/material/dialog";
import {SsfDetailsFormComponent} from "../ssf-details-form/ssf-details-form.component";
import {ScfDetailsFormComponent} from "../scf-details-form/scf-details-form.component";
import {ComplianceUpdateFormComponent} from "../compliance-update-form/compliance-update-form.component";

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
                {name: 'sampleSubmission', title: '<i class="btn-sm btn-primary ssf">SSF</i>'},
                {name: 'sampleCollection', title: '<i class="btn-sm btn-primary scf">SCF</i>'},
                {name: 'sampleUpdate', title: '<i class="btn-sm btn-primary ssf-update">SSF Updated</i>'}
            ],
            position: 'right' // left|right
        },
        rowClassFunction: (row)=>{
            if(row.sampleCollectionStatus==1) {
                return 'hide-scf-action'
            }

            return "hide-ssf-action"
        },
        delete: {
            deleteButtonContent: '&nbsp;&nbsp;<i class="fa fa-trash-o text-danger"></i>',
            confirmDelete: true
        },
        noDataMessage: 'No data found',
        columns: {
            itemNo: {
                title: '#',
                type: 'string'
            },
            itemHsCode: {
                title: 'HS Code',
                type: 'string'
            },
            quantity: {
                title: 'Quantity',
                type: 'string'
            },
            unitOfQuantity: {
                title: 'Unit',
                type: 'string'
            },
            countryOfOrgin: {
                title: 'Origin',
                type: 'string'
            },
            itemDescription: {
                title: 'Description',
                type: 'string'
            },
            inspectionNotificationStatus: {
                title: 'Inspection Status',
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

    openSampleUpdate(data: any) {
        this.dialog.open(ComplianceUpdateFormComponent, {
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
            case "sampleUpdate":
                this.openSampleUpdate(data.data)
            default:
                console.log("Invalid action: " + data.action)

        }
    }

}
