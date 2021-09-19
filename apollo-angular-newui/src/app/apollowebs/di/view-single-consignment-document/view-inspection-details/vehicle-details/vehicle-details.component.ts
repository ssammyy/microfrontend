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
    displayedColumns=['id','manufactureDate','registrationDate','category','compliant','ministryStation','ministryInspection','makeVehicle','odemetreReading','transmissionAutoManual','engineNoCapacity','actions']

    constructor(private diService: DestinationInspectionService, private dialog: MatDialog) {
    }

    ngOnInit(): void {
    }

    resubmitMinistryInspection(itemUuid: any, reinspection: boolean) {
        this.dialog.open(MinistryInspectionRequestComponent, {
            data: {
                uuid: itemUuid,
                reinspection: reinspection,
            }
        })
    }
    customAction(data: any,action: string) {
        switch (action) {
            case 'requestMinistryChecklist':
                this.resubmitMinistryInspection(data.id, false)
                break
            case 'downloadReport':
                this.diService.downloadDocument('/api/v1/download/motor/inspection/report/'+data.id)
                break
            case 'downloadUnfilled':
                this.diService.downloadDocument('/api/v1/download/ministry/checklist/unfilled/'+data.id)
                break
            case 'downloadChecklist':
                this.diService.downloadDocument("/api/v1/di/ministry/inspection/checklist/download/" + data.id)
                break
            default:
                console.log("Invalid action: " + action)
        }
    }

}
