import {Component, OnInit} from '@angular/core';
import {PVOCService} from "../../../../../core/store/data/pvoc/pvoc.service";
import {ActivatedRoute, Router} from "@angular/router";
import {BehaviorSubject} from "rxjs";

@Component({
    selector: 'app-view-rfc-cor-documents',
    templateUrl: './view-rfc-cor-documents.component.html',
    styleUrls: ['./view-rfc-cor-documents.component.css']
})
export class ViewRfcCorDocumentsComponent implements OnInit {
    rfcId: any
    message: any
    rfcDetails: BehaviorSubject<any>

    constructor(private pvocService: PVOCService, private activeRoute: ActivatedRoute, private router: Router) {
    }

    ngOnInit(): void {
        this.rfcDetails = new BehaviorSubject(null)
        this.message = "Loading data"
        this.activeRoute.paramMap.subscribe(
            res => {
                this.rfcId = res.get("id")
                this.loadRfcDetails()
            }
        )
    }

    goBack() {
        this.router.navigate(["/pvoc/foreign/rfc/cor"])
    }

    reloadRfcDetails(reload) {
        this.loadRfcDetails()
    }

    loadRfcDetails() {
        this.pvocService.loadRfcCorDetails(this.rfcId).subscribe(
            res => {
                if (res.responseCode == '00') {
                    this.rfcDetails.next(res.data)
                    this.message = null
                } else {
                    this.rfcDetails.next(null)
                    this.message = res.message
                }
            }
        )
    }

}
