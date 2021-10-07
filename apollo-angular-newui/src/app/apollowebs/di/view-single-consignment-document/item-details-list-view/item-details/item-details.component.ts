import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {DestinationInspectionService} from "../../../../../core/store/data/di/destination-inspection.service";
import swal from "sweetalert2";
import {MatDialog} from "@angular/material/dialog";
import {MinistryInspectionRequestComponent} from "../ministry-inspection-request/ministry-inspection-request.component";
import {ChecklistDataFormComponent} from "../../checklist-data-form/checklist-data-form.component";
import {SsfDetailsFormComponent} from "../../view-inspection-details/ssf-details-form/ssf-details-form.component";
import {ApproveRejectItemComponent} from "../approve-reject-item/approve-reject-item.component";

@Component({
    selector: 'app-item-details',
    templateUrl: './item-details.component.html',
    styleUrls: ['./item-details.component.css']
})
export class ItemDetailsComponent implements OnInit {
    activeTab = 'declarationGeneralDetails'
    itemUuid: any
    cdUuid: any
    itemDetails: any
    checkListConfiguration: any
    private statuses: any[] = [];

    constructor(private activatedRoute: ActivatedRoute, private diService: DestinationInspectionService,
                private router: Router, private dialog: MatDialog) {
    }

    ngOnInit(): void {
        this.activatedRoute.paramMap.subscribe(
            res => {
                this.itemUuid = res.get("id")
                this.cdUuid = res.get("cdUuid")
                this.loadItemDetails()

            }
        )
    }
    goBack() {
        this.router.navigate(["/di", this.cdUuid])
    }
    rejectItem() {
        this.dialog.open(ApproveRejectItemComponent, {
            data: {
                uuid: this.itemUuid,
                reinspection: false,
                configs: this.checkListConfiguration
            }
        }).afterClosed()
            .subscribe(
                res => {
                    if (res) {
                        this.loadItemDetails()
                    }
                }
            )
    }

    loadLabResults() {

    }

    loadItemDetails() {
        this.diService.loadItemDetails(this.itemUuid)
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.itemDetails = res.data
                    } else {
                        swal.fire({
                            title: res.message,
                            buttonsStyling: false,
                            customClass: {
                                confirmButton: 'btn btn-success form-wizard-next-btn ',
                            },
                            icon: 'error'
                        }).then(() => {
                            this.router.navigate(["/di/", this.cdUuid])
                        });
                    }
                }
            )
    }

    downloadReport(){
        this.diService.downloadDocument("/api/v1/download/checklist/sampleSubmissionForm/" + this.itemUuid)
    }

}
