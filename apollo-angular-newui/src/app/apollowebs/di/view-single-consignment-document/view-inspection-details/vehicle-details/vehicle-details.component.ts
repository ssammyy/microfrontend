import {Component, Input, OnInit} from '@angular/core';
import {DestinationInspectionService} from "../../../../../core/store/data/di/destination-inspection.service";
import {MinistryInspectionRequestComponent} from "../../item-details-list-view/ministry-inspection-request/ministry-inspection-request.component";
import {MatDialog} from "@angular/material/dialog";

@Component({
    selector: 'app-vehicle-details',
    templateUrl: './vehicle-details.component.html',
    styleUrls: ['./vehicle-details.component.css']
})
export class VehicleDetailsComponent implements OnInit {
    @Input() inspectionDetails: any
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
                {name: 'requestMinistryChecklist', title: '<i class="btn btn-sm btn-primary">MINISTRY CHECKLIST</i>'},
                {name: 'downloadChekclist', title: '<i class="btn btn-sm btn-primary">Download</i>'}
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
            registrationDate: {
                title: 'Registration Date',
                type: 'string'
            },
            manufactureDate: {
                title: 'Manufacture Date',
                type: 'string'
            },
            category: {
                title: 'Item Category',
                type: 'string'
            },
            compliant: {
                title: 'Compliant',
                type: 'string'
            },
            ministryStation: {
                title: 'Ministry Station',
                type: 'string'
            },
            ministryInspection: {
                title: 'Ministry Inspection',
                type: 'string'
            },
            makeVehicle: {
                title: 'Make',
                type: 'string'
            },
            odemetreReading: {
                title: 'Odometer',
                type: 'string'
            },
            transmissionAutoManual: {
                title: 'Transmission',
                type: 'string'
            },
            engineNoCapacity: {
                title: 'Engine CC',
                type: 'date'
            },
            sampleUpdated: {
                title: 'Sample Updated',
                type: 'string'
            }
        },
        pager: {
            display: true,
            perPage: 20
        }
    };

    constructor(private diService: DestinationInspectionService, private dialog: MatDialog) {
    }

    ngOnInit(): void {
    }

    resubmitMinistryInspection(itemUuid: any) {
        this.dialog.open(MinistryInspectionRequestComponent, {
            data: {
                uuid: itemUuid,
                reinspection: true,
            }
        })
    }
    customAction(event: any) {
        switch (event.action) {
            case 'requestMinistryChecklist':
                this.resubmitMinistryInspection(event.data.id)
                break
            case 'downloadChekclist':
                this.diService.downloadDocument("/api/v1/di/ministry/inspection/checklist/download/" + event.data.id)
                break
            default:
                console.log("Invalid action: " + event.action)
        }
    }

}
