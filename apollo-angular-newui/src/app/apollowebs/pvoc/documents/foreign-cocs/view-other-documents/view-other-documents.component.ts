import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {PVOCService} from "../../../../../core/store/data/pvoc/pvoc.service";
import {BehaviorSubject} from "rxjs";

@Component({
    selector: 'app-view-other-documents',
    templateUrl: './view-other-documents.component.html',
    styleUrls: ['./view-other-documents.component.css']
})
export class ViewOtherDocumentsComponent implements OnInit {

    message: string
    active: any = '1'
    cocDetails: BehaviorSubject<any>
    corDetails: BehaviorSubject<any>
    documentId: any
    documentType: any
    documentTypeDesc: any

    constructor(private router: Router, private activatedRoute: ActivatedRoute, private pvocService: PVOCService) {
    }

    ngOnInit(): void {
        this.cocDetails = new BehaviorSubject<any>(null)
        this.corDetails = new BehaviorSubject<any>(null)
        this.activatedRoute.paramMap
            .subscribe(
                res => {
                    this.documentId = res.get("id")
                    this.documentType = res.get('docType')
                    if (this.documentType.toUpperCase() === "NCR-COR") {
                        this.documentTypeDesc = 'Certificate of Non-Compliance for Vehicles'
                        this.loadCorDetails()
                    } else if (this.documentType) {
                        switch (this.documentType.toUpperCase()) {
                            case 'COC':
                                this.documentTypeDesc = 'Certificate of Compliance'
                                break
                            case 'COI':
                                this.documentTypeDesc = 'Certificate of Inspection'
                                break
                            case 'NCR':
                                this.documentTypeDesc = 'Certificate of Non-Compliance'
                                break
                        }
                        this.loadCocDetails()
                    }
                }
            )
    }

    goBack() {
        this.router.navigate(["/pvoc/foreign/documents", this.documentType])
    }

    loadCocDetails() {
        this.message = null
        this.cocDetails.next(null)

        this.pvocService.loadCocDetails(this.documentId)
            .subscribe(
                res => {
                    if (res.responseCode == "00") {
                        this.cocDetails.next(res.data)
                    } else {
                        this.message = res.message
                    }
                }
            )
    }

    loadCorDetails() {
        this.message = null
        this.corDetails.next(null)

        this.pvocService.loadCorDetails(this.documentId)
            .subscribe(
                res => {
                    if (res.responseCode == "00") {
                        this.corDetails.next(res.data)
                    } else {
                        this.message = res.message
                    }
                }
            )
    }

}
