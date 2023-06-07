import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {PVOCService} from "../../../../core/store/data/pvoc/pvoc.service";
import {MatDialog} from "@angular/material/dialog";
import {AddApiClientComponent} from "../../../system/clients/add-api-client/add-api-client.component";
import {AddUpdatePartnerComponent} from "../add-update-partner/add-update-partner.component";

@Component({
    selector: 'app-view-partner-details',
    templateUrl: './view-partner-details.component.html',
    styleUrls: ['./view-partner-details.component.css']
})
export class ViewPartnerDetailsComponent implements OnInit {
    partnerId: any
    partnerDetails: any
    partnerCountries: any[]
    active: Number = 0

    constructor(private routeActivated: ActivatedRoute, private pvocService: PVOCService, private dialog: MatDialog) {
    }

    ngOnInit(): void {
        this.routeActivated.paramMap.subscribe(
            res => {
                this.partnerId = res.get('id')
                this.loadData()
                this.loadPartnerCountries()
            }
        )
    }

    loadPartnerCountries() {
        this.pvocService.loadPartnerCountries()
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.partnerCountries = res.data
                    }
                }
            )

    }

    loadData() {
        this.pvocService.loadPartnerDetails(this.partnerId)
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.partnerDetails = res.data
                    } else {
                        this.pvocService.showError(res.message)
                    }
                },
                error => {
                    this.pvocService.showError(error.message)
                }
            )
    }

    addApiClient() {
        this.dialog.open(AddApiClientComponent, {
            data: {
                partnerId: this.partnerId
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

    updatePartnerDetails() {
        this.dialog.open(AddUpdatePartnerComponent, {
            data: {
                partner: this.partnerDetails,
                countries: this.partnerCountries
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

    resetApiClientSecret() {
        this.pvocService.showConfirmation("Are you sure you want to generate and send PVOC client secret? This might affect their current opperation", res => {
            if (res) {
                this.pvocService.updatePartnerApiClientSecret({}, this.partnerId)
                    .subscribe(
                        res => {
                            if (res.responseCode === "00") {
                                this.pvocService.showSuccess(res.message + " \nClient ID is " + res.data.client_id + " and the new secret is " + res.data.client_secret)
                                this.loadData()
                            } else {
                                this.pvocService.showError(res.message)
                            }
                        }
                    )
            }
        });
    }

}
