import {Component, OnInit} from '@angular/core';
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
    selector: 'app-manifest-document',
    templateUrl: './manifest-document.component.html',
    styleUrls: ['./manifest-document.component.css']
})
export class ManifestDocumentComponent implements OnInit {

    documentUuid: string;
    manifestDetails: any;
    active: any = 0

    constructor(
        private diService: DestinationInspectionService,
        private activatedRoute: ActivatedRoute,
        private router: Router
    ) {
    }

    ngOnInit(): void {
        this.activatedRoute.paramMap
            .subscribe(
                res => {
                    this.documentUuid = res.get("id")
                    this.loadIdfDocument()
                }
            )
    }

    goBack() {
        this.router.navigate(["/di", this.documentUuid])
    }

    loadIdfDocument() {
        this.diService.consignmentManifest(this.documentUuid)
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.manifestDetails = res.data
                    } else {
                        this.diService.showError(res.message, () => {
                            this.router.navigate(["/di/", this.documentUuid])
                        })
                    }
                }
            )
    }

}
