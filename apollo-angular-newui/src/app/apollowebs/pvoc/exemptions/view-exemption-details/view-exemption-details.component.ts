import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {PVOCService} from "../../../../core/store/data/pvoc/pvoc.service";
import {MatDialog} from "@angular/material/dialog";
import {ApproveRejectApplicationComponent} from "../../../di/ism/approve-reject-application/approve-reject-application.component";
import {UpdateExemptionTaskComponent} from "../update-exemption-task/update-exemption-task.component";

@Component({
    selector: 'app-view-exemption-details',
    templateUrl: './view-exemption-details.component.html',
    styleUrls: ['./view-exemption-details.component.css']
})
export class ViewExemptionDetailsComponent implements OnInit {
    sparesColumns = ["hsCode", "industrialSpares", "countryOfOrigin", "machineToFit"]
    machineryColumns = ["hsCode", "machineDescription", "countryOfOrigin", "makeModel"]
    rawMaterialColumns = ["hsCode", "rawMaterialDescription", "countryOfOrigin", "endProduct", "dutyRate", "hsDescription"]
    productsColumns = ["productName", "section", "brand", "kebsStandardizationMarkPermit", "expirelyDate"]
    requestId: any
    exemptionDetails: any
    exemptionTasks: any[]
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
        this.router.navigate(["/pvoc/exemption/applications"])
    }

    approveRejectTasks(objectId: any, exemptionId: any, task: any) {
        this.dialog.open(UpdateExemptionTaskComponent, {
            data: {
                exemptionId: exemptionId,
                taskId: objectId,
                task: task,
                section: this.exemptionDetails.is_section_officer
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
        this.router.navigate(["/di/view/", this.exemptionDetails.cd_uuid, '/', 'complaint', this.requestId])
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
        this.pvocService.getExemptionApplicationDetails(this.requestId)
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.exemptionDetails = res.data
                    } else {
                        this.pvocService.showError(res.message, this.goBack)
                    }
                }
            )
    }

}
