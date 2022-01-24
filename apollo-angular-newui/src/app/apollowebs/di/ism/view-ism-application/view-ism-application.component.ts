import {Component, Inject, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";
import { MatDialog} from "@angular/material/dialog";
import {ApproveRejectApplicationComponent} from "../approve-reject-application/approve-reject-application.component";

@Component({
    selector: 'app-view-ism-application',
    templateUrl: './view-ism-application.component.html',
    styleUrls: ['./view-ism-application.component.css']
})
export class ViewIsmApplicationComponent implements OnInit {
    requestId: any
    ismDetails: any
    activeTab: number = 1

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
        this.router.navigate(["/di/ism/requests"])
    }

    viewConsignmentDocument() {
        this.router.navigate(["/di", this.ismDetails.cd_uuid])
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
        this.diService.getIsmApplicationDetails(this.requestId)
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.ismDetails = res.data
                    } else {
                        this.diService.showError(res.message, () => {
                            this.router.navigate(["/di/ism/requests"])
                        })
                    }
                }
            )
    }

}
