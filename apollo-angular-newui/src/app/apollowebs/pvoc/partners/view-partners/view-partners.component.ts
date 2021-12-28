import {Component, OnInit} from '@angular/core';
import {PVOCService} from "../../../../core/store/data/pvoc/pvoc.service";
import {MatDialog} from "@angular/material/dialog";
import {AddUpdatePartnerComponent} from "../add-update-partner/add-update-partner.component";
import {Router} from "@angular/router";

@Component({
    selector: 'app-view-partners',
    templateUrl: './view-partners.component.html',
    styleUrls: ['./view-partners.component.css']
})
export class ViewPartnersComponent implements OnInit {
    keywords: string | null = null
    page: number = 0
    pageSize: number = 20
    partnerListing: any | null = null
    displayedColumns=["partnerRefNo","partnerName","partnerEmail","partnerCity","partnerCountry","partnerTelephoneNumber","actions"]


    constructor(private pvocService: PVOCService, private dialog: MatDialog, private router: Router) {
    }

    ngOnInit(): void {
        this.loadData()
    }

    loadData() {
        this.pvocService.loadPartners(this.keywords, this.page, this.pageSize)
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.partnerListing = res.data
                    } else {
                        this.pvocService.showError(res.message)
                    }
                },
                error => {
                    this.pvocService.showError(error.message)
                }
            )
    }

    addUpdatePartner(partner: any | null) {
        this.dialog.open(AddUpdatePartnerComponent, {
            data: partner
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
    viewPartner(partnerId: number){
        this.router.navigate(["/pvoc/partners/view/",partnerId])
    }
    searchPhraseChanged() {

    }
}
