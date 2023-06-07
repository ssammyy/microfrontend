import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {DestinationInspectionService} from "../../../../../core/store/data/di/destination-inspection.service";
import {MatDialog} from "@angular/material/dialog";
import {ApproveRejectItemComponent} from "../approve-reject-item/approve-reject-item.component";

@Component({
    selector: 'app-lab-results',
    templateUrl: './lab-results.component.html',
    styleUrls: ['./lab-results.component.css']
})
export class LabResultsComponent implements OnInit {
    displayedColumns = ["orderId", "sampleNumber", "matrix", "test", "testPrice", "storageLocation", "method", "testGroup", "priority", "ts"]
    displayedColumnResults = ["orderId", "sampleNumber", "result_param", "percentMoisture", "lab_test", "numeric_result", "lab_result", "tic", "ts"]
    labResults: any
    labDocuments: any[]
    activeTab: 1
    prevPage: any;
    itemUuid: any;
    cdUuid: any

    constructor(private activatedRoute: ActivatedRoute, private dialog: MatDialog, private router: Router, private diService: DestinationInspectionService) {
    }

    ngOnInit(): void {
        this.activatedRoute.paramMap.subscribe(
            res => {
                this.itemUuid = res.get("id")
                this.prevPage = res.get("page")
                this.cdUuid = res.get("cdUuid")
                this.loadLabResults()
            }
        )
    }

    rejectItem() {
        this.dialog.open(ApproveRejectItemComponent, {
            data: {
                cdUuid: this.cdUuid,
                id: this.labResults.item_details.id
            }
        }).afterClosed()
            .subscribe(
                res => {
                    if (res) {
                        //On updating compliance status on a sampled item,user should be redirected to previous page
                        this.goBack()
                    } else {
                        this.loadLabResults()
                    }
                }
            )
    }

    downloadLabResult(fileName: any) {
        let params = {
            fileName: fileName,
            bsNumber: this.labResults.ssf_details.bsNumber
        }
        this.diService.downloadDocument("/api/v1/download/lims/lab-result/pdf", params)

    }

    loadLabPdfsResults() {
        this.diService.loadLabResultsDocuments(this.labResults.ssf_details.id)
            .subscribe(
                res => {
                    if (res.responseCode == "00") {
                        this.labDocuments = res.data
                    } else {
                        console.log(res.message)
                    }
                }
            )
    }

    resultDetails(data: any) {

    }

    goBack() {
        if ("item" === this.prevPage) {
            this.router.navigate(["/di/item", this.itemUuid])
        } else {
            this.router.navigate(["/di/checklist/details/", this.cdUuid])
        }
    }

    loadLabResults() {
        this.diService.loadLabResults(this.itemUuid)
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.labResults = res.data
                        // Load lab results
                        this.loadLabPdfsResults()
                    } else {
                        this.diService.showError(res.message, () => {
                            this.goBack()
                        })
                    }
                }
            )
    }

}
