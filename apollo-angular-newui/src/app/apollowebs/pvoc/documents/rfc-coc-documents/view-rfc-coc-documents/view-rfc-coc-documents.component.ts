import {Component, OnInit} from '@angular/core';
import {BehaviorSubject} from "rxjs";
import {PVOCService} from "../../../../../core/store/data/pvoc/pvoc.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
    selector: 'app-view-rfc-coc-documents',
    templateUrl: './view-rfc-coc-documents.component.html',
    styleUrls: ['./view-rfc-coc-documents.component.css']
})
export class ViewRfcCocDocumentsComponent implements OnInit {

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
                this.loadCocRfcDetails()
            }
        )
    }

    goBack() {
        this.router.navigate(["/pvoc/foreign/rfc/coc"])
    }

    reloadRfcDetails(reload) {
        this.loadCocRfcDetails()
    }

    loadCocRfcDetails() {
        this.pvocService.loadRfcCocDetails(this.rfcId).subscribe(
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
