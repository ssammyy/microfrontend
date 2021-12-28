import {Component, OnInit} from '@angular/core';
import {SystemService} from "../../../../core/store/data/system/system.service";
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";
import {MatDialog} from "@angular/material/dialog";
import {AddApiClientComponent} from "../add-api-client/add-api-client.component";
import {ViewClientCredentialsComponent} from "../view-client-credentials/view-client-credentials.component";

@Component({
    selector: 'app-view-clients',
    templateUrl: './view-clients.component.html',
    styleUrls: ['./view-clients.component.css']
})
export class ViewClientsComponent implements OnInit {
    clientListing: any[]
    displayedColumns = ["clientId", "clientName", "clientType", "clientRole", "clientType", "clientStatus", 'blockedStatus', "description"]
    displayedColumnResults = ["clientId", "clientName", "clientType", "clientRole", "clientType", "clientStatus", 'blockedStatus', "eventsUrl", "callbackUrl", "description"]
    page = 0
    keywords: string | null = ""
    pageSize = 20

    constructor(private systemService: SystemService, private dialog: MatDialog, private diService: DestinationInspectionService) {
    }

    ngOnInit(): void {
        this.loadData()
    }

    searchPhraseChanged() {

    }

    loadData() {
        this.systemService.loadApiClients(this.page, this.pageSize)
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.clientListing = res.data
                    } else {
                        this.diService.showError(res.message, null)
                    }
                },
                error => {
                    this.diService.showError("Failed to load data with: " + error.message, null)
                }
            )
    }

    viewApiClient(clientId) {

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
