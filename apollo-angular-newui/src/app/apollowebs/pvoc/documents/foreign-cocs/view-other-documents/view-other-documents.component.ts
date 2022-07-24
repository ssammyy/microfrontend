import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {PVOCService} from "../../../../../core/store/data/pvoc/pvoc.service";

@Component({
    selector: 'app-view-other-documents',
    templateUrl: './view-other-documents.component.html',
    styleUrls: ['./view-other-documents.component.css']
})
export class ViewOtherDocumentsComponent implements OnInit {

    message: string
    active: any = '1'
    cocDetails: any
    documentId: any
    documentType: any

    constructor(private router: Router, private activatedRoute: ActivatedRoute, private pvocService: PVOCService) {
    }

    ngOnInit(): void {
        this.activatedRoute.paramMap
            .subscribe(
                res => {
                    this.documentId = res.get("id")
                    this.documentType = res.get('docType')
                    this.loadCocDetails()
                }
            )
    }

    goBack() {
        this.router.navigate(["/pvoc/foreign/documents", this.documentType])
    }

    loadCocDetails() {
        this.message = null
        this.pvocService.loadCocDetails(this.documentId)
            .subscribe(
                res => {
                    if (res.responseCode == "00") {
                        this.cocDetails = res.data
                    } else {
                        this.message = res.message
                    }
                }
            )
    }

}
