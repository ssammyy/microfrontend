import {Component, OnInit} from '@angular/core';
import {SystemService} from "../../../../core/store/data/system/system.service";
import {MatDialog} from "@angular/material/dialog";
import {AddApiClientComponent} from "../../../system/clients/add-api-client/add-api-client.component";
import {ViewClientCredentialsComponent} from "../../../system/clients/view-client-credentials/view-client-credentials.component";
import {PVOCService} from "../../../../core/store/data/pvoc/pvoc.service";
import {Router} from "@angular/router";

@Component({
    selector: 'app-view-complaints',
    templateUrl: './view-complaints.component.html',
    styleUrls: ['./view-complaints.component.css']
})
export class ViewComplaintsComponent implements OnInit {
    applicationStatus: any = "new"
    clientListing: any[]
    displayedColumns = ["categoryName", "subCategoryName", "cocNo", "complaintName", "email", "pvocAgent", "actions"]
    displayedColumnResults = ["categoryName", "subCategoryName", "cocNo", "complaintName", "email", "phoneNo", "pvocAgent", "recommendationDesc", "actions"]
    page = 0
    keywords: string | null = ""
    pageSize = 20

    constructor(private systemService: SystemService, private dialog: MatDialog, private pvocService: PVOCService, private router: Router) {
    }

    ngOnInit(): void {
        this.loadData()
    }

    searchPhraseChanged() {
        if (this.keywords && this.keywords.length > 0) {
            this.loadData()
        }
    }

    loadData() {
        this.pvocService.listComplaintApplications(this.keywords, this.applicationStatus, this.page, this.pageSize)
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.clientListing = res.data
                    } else {
                        this.pvocService.showError(res.message, null)
                    }
                },
                error => {
                    this.pvocService.showError("Failed to load data with: " + error.message, null)
                }
            )
    }

    viewComplaint(recordId: any) {
        this.router.navigate(["/pvoc/complaints",recordId])
    }

    addApiClient(clientDetails: any) {
        this.dialog.open(AddApiClientComponent, {
            data: clientDetails
        }).afterClosed()
            .subscribe(
                res => {
                    if (res) {
                        // Show Client Credentials for display and transmission
                        this.dialog.open(ViewClientCredentialsComponent, {
                            data: res
                        })
                            .afterClosed()
                            .subscribe(
                                res => {
                                    // reload data
                                    this.loadData()
                                }
                            )
                    }
                }
            )
    }

}
