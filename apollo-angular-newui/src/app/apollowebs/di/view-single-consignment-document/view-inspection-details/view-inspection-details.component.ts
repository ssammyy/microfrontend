import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";

@Component({
    selector: 'app-view-inspection-details',
    templateUrl: './view-inspection-details.component.html',
    styleUrls: ['./view-inspection-details.component.css']
})
export class ViewInspectionDetailsComponent implements OnInit {
    activeTab = 0
    inspectionDetails: any
    documentId: any
    checklidId: any
    sampledItems: any

    constructor(private diService: DestinationInspectionService, private  activatedRoute: ActivatedRoute, private router: Router) {
    }


    ngOnInit(): void {
        this.activatedRoute.paramMap.subscribe(
            res => {
                this.documentId = res.get("id")
                this.activatedRoute.queryParamMap.subscribe(
                    params => {
                        this.checklidId = params.get("ci")
                        this.loadInspectionDetails()
                    }
                )
            }
        )
    }

    goBack() {
        this.router.navigate(["/di", this.documentId])
    }

    downloadAllChecklist() {
        this.diService.downloadDocument("/api/v1/download/all/checklist/" + this.documentId)
    }

    loadInspectionDetails() {
        let params = {}
        if (this.checklidId) {
            params['ci'] = this.checklidId
        }
        this.diService.getDetails("/api/v1/di/consignment/document/checklist/" + this.documentId, params).subscribe(
            res => {
                if (res.responseCode == "00") {
                    this.inspectionDetails = res.data
                    this.loadSampledItems()
                } else {
                    this.diService.showError(res.message)
                }
            }
        )
    }

    loadSampledItems() {
        this.diService.getDetails("/api/v1/di/consignment/document/sampled-items/" + this.documentId, {})
            .subscribe(
                res => {
                    if (res.responseCode == "00") {
                        this.sampledItems = res.data
                    } else {
                        this.diService.showError(res.message)
                    }
                }
            )
    }

    reloadChecklist() {
        this.loadInspectionDetails()
        this.loadSampledItems()
    }

}
