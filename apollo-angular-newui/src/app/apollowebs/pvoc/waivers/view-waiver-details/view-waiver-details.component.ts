import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {PVOCService} from "../../../../core/store/data/pvoc/pvoc.service";
import {MatDialog} from "@angular/material/dialog";
import {ApproveRejectApplicationComponent} from "../../../di/ism/approve-reject-application/approve-reject-application.component";
import {UpdateWaiverTaskComponent} from "../update-waiver-task/update-waiver-task.component";
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";

@Component({
    selector: 'app-view-waiver-details',
    templateUrl: './view-waiver-details.component.html',
    styleUrls: ['./view-waiver-details.component.css']
})
export class ViewWaiverDetailsComponent implements OnInit {
    attachmentColumns = ["name", "description", "fileType", "actions"]
    historyColumns = ["names", "remarks", "waiverAction"]
    productsColumns = ["hsCode", "unit", "quantity", "totalAmount", "brand", "origin", "productDescription", "serialNo", "currency"]
    requestId: any
    waiverDetails: any
    activeTab: number = 0

    constructor(private activeRoute: ActivatedRoute, private router: Router, private pvocService: PVOCService,private diService: DestinationInspectionService, private dialog: MatDialog) {
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
        this.router.navigate(["/pvoc/waiver/applications"])
    }

    approveRejectTasks(objectId: any, waiverId: any, task: any) {
        this.dialog.open(UpdateWaiverTaskComponent, {
            data: {
                waiverId: waiverId,
                taskId: objectId,
                task: task,
                section: this.waiverDetails.is_wetc_officer
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

    viewConsignmentDocument() {
        this.router.navigate(["/di/view/", this.waiverDetails.cd_uuid, '/', 'complaint', this.requestId])
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

    downloadAttachment(attachmentId: any) {
        this.diService.downloadDocument("/api/v1/download/waiver/attachment/"+attachmentId)
    }

    loadData() {
        this.pvocService.getWaiverApplicationDetails(this.requestId)
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.waiverDetails = res.data
                    } else {
                        this.pvocService.showError(res.message, this.goBack)
                    }
                }
            )
    }

}
