import {Component, OnInit} from '@angular/core';
import {PVOCService} from "../../../../core/store/data/pvoc/pvoc.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
    selector: 'app-view-risk-profiles',
    templateUrl: './view-risk-profiles.component.html',
    styleUrls: ['./view-risk-profiles.component.css']
})
export class ViewRiskProfilesComponent implements OnInit {
    queryId: any
    queryDetails: any
    activeTab = 0

    constructor(private  pvocService: PVOCService, private activeRouter: ActivatedRoute, private router: Router) {
    }

    ngOnInit(): void {
        this.activeRouter.paramMap.subscribe(
            res => {
                this.queryId = res.get("id")
                this.loadData()
            }
        )
    }

    goBack() {
        this.router.navigate(["/pvoc/risk/profiles"])
    }

    loadData() {
        this.pvocService.loadRiskProfileDetails(this.queryId)
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.queryDetails = res.data
                    } else {
                        this.pvocService.showError(res.message)
                    }
                },
                error => {
                    this.pvocService.showError("Failed to load data: " + error.toString())
                }
            )
    }

}
