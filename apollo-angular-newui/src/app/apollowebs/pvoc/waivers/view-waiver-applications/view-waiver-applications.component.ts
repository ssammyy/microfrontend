import {Component, OnInit} from '@angular/core';
import {SystemService} from "../../../../core/store/data/system/system.service";
import {MatDialog} from "@angular/material/dialog";
import {PVOCService} from "../../../../core/store/data/pvoc/pvoc.service";
import {Router} from "@angular/router";

@Component({
    selector: 'app-view-waiver-applications',
    templateUrl: './view-waiver-applications.component.html',
    styleUrls: ['./view-waiver-applications.component.css']
})
export class ViewWaiverApplicationsComponent implements OnInit {
    applicationStatus: any = "new"
    waiverListing: any[]
    displayedColumns = ["serialNo", "applicantName", "address", "emailAddress", "kraPin", "submittedOn", "reviewStatus", "actions"]
    displayedColumnResults = ["serialNo", "applicantName", "address", "emailAddress", "kraPin", "submittedOn", "reviewStatus", "actions"]
    page = 0
    keywords: string | null = ""
    pageSize = 20
    currentPageInternal = 0

    constructor(private systemService: SystemService, private dialog: MatDialog, private pvocService: PVOCService, private router: Router) {
    }

    ngOnInit(): void {
        this.loadData()
    }

    toggleStatus(status: string): void {
        if (status !== this.applicationStatus) {
            this.applicationStatus = status;
            this.page = 0
            this.keywords = null
            this.currentPageInternal = 0
            this.loadData()
        }
    }

    searchPhraseChanged() {
        if (this.keywords && this.keywords.length > 0) {
            this.loadData()
        }
    }

    loadData() {
        this.pvocService.listWaiverApplications(this.keywords, this.applicationStatus, this.page, this.pageSize)
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.waiverListing = res.data
                    } else {
                        this.pvocService.showError(res.message, null)
                    }
                },
                error => {
                    this.pvocService.showError("Failed to load data with: " + error.message, null)
                }
            )
    }

    viewWaiver(recordId: any) {
        this.router.navigate(["/pvoc/waiver/application/details", recordId])
    }

}
