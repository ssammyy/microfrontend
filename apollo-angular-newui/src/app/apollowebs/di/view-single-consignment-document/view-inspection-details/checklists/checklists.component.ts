import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {DestinationInspectionService} from "../../../../../core/store/data/di/destination-inspection.service";
import {MatDialog} from "@angular/material/dialog";
import {SsfDetailsFormComponent} from "../ssf-details-form/ssf-details-form.component";
import {ScfDetailsFormComponent} from "../scf-details-form/scf-details-form.component";
import {ComplianceUpdateFormComponent} from "../compliance-update-form/compliance-update-form.component";
import {Router} from "@angular/router";

@Component({
    selector: 'app-checklists',
    templateUrl: './checklists.component.html',
    styleUrls: ['./checklists.component.css']
})
export class ChecklistsComponent implements OnInit {
    public displayedColumns = ['itemNo', 'itemHsCode', 'quantity', 'unitOfQuantity', 'countryOfOrgin', 'itemDescription', 'inspectionNotificationStatus', 'actions']
    @Input() dataSet: any = [];
    @Input() cdUuid: any
    @Output() reloadSample = new EventEmitter<Boolean>()

    constructor(private diService: DestinationInspectionService,private router: Router, private dialog: MatDialog) {
    }

    ngOnInit(): void {
    }

    openSampleCollection(data: any) {
        this.dialog.open(ScfDetailsFormComponent, {
            data: {
                uuid: data.uuid,
                details: data
            }
        }).afterClosed()
            .subscribe(
                res => {
                    if (res) {
                        this.reloadSample.emit(true)
                    }
                }
            )
    }

    openSampleSubmission(data: any) {
        this.dialog.open(SsfDetailsFormComponent, {
            data: {
                uuid: data.uuid,
                details: data
            }
        }).afterClosed()
            .subscribe(
                res => {
                    if (res) {
                        this.reloadSample.emit(true)
                    }
                }
            )
    }

    openSampleUpdate(data: any) {
        this.dialog.open(ComplianceUpdateFormComponent, {
            data: {
                uuid: data.uuid,
                details: data
            }
        }).afterClosed()
            .subscribe(
                res => {
                    if (res) {
                        this.reloadSample.emit(true)
                    }
                }
            )
    }

    viewLabResults(data: any) {
      this.router.navigate(["/di/item/lab-results/",data.uuid,'inspection',this.cdUuid])
    }

    customAction(data: any, action: string) {
        switch (action) {
            case 'sampleCollection':
                this.openSampleCollection(data)
                break
            case 'labResults':
                this.viewLabResults(data)
                break
            case "sampleSubmission":
                this.openSampleSubmission(data)
                break
            case "sampleUpdate":
                this.openSampleUpdate(data)
                break
            case "downlodSsf":
                this.diService.downloadDocument("/api/v1/download/checklist/sampleSubmissionFormV2/" + data.id)
                break
            case "downlodScf":
                this.diService.downloadDocument("/api/v1/download/checklist/sampleCollectionForm/" + data.id)
                break
            default:
                console.log("Invalid action: " + action)

        }
    }

}
