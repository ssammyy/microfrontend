import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";
import {MatDialog} from "@angular/material/dialog";
import {ApproveRejectApplicationComponent} from "../../../di/ism/approve-reject-application/approve-reject-application.component";
import {PVOCService} from "../../../../core/store/data/pvoc/pvoc.service";
import {UpdateComplaintTaskComponent} from "../update-complaint-task/update-complaint-task.component";

@Component({
    selector: 'app-view-complaint-details',
    templateUrl: './view-complaint-details.component.html',
    styleUrls: ['./view-complaint-details.component.css']
})
export class ViewComplaintDetailsComponent implements OnInit {
    requestId: any
    complaintDetails: any
    activeTab: number = 0

    constructor(private activeRoute: ActivatedRoute, private router: Router, private pvocService: PVOCService, private dialog: MatDialog) {
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
        this.router.navigate(["/pvoc/complaints"])
    }

    viewConsignmentDocument() {
        this.router.navigate(["/di/view/", this.complaintDetails.cd_uuid, '/', 'complaint', this.requestId])
    }

    approveRejectTasks(taskId: any, task: any) {
        this.dialog.open(UpdateComplaintTaskComponent, {
            data: {
                complaintId: this.requestId,
                pvoc_officer: this.complaintDetails.is_pvoc_officer,
                task: task,
                taskId: taskId
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
        this.pvocService.getComplaintApplicationDetails(this.requestId)
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.complaintDetails = res.data
                    } else {
                        this.pvocService.showError(res.message, this.goBack)
                    }
                }
            )
    }

}
