import {Component, OnInit} from '@angular/core';
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";
import {ActivatedRoute, Router} from "@angular/router";
import swal from "sweetalert2";

@Component({
    selector: 'app-view-idf-document-details',
    templateUrl: './view-idf-document-details.component.html',
    styleUrls: ['./view-idf-document-details.component.css']
})
export class ViewIdfDocumentDetailsComponent implements OnInit {
    documentUuid: string;
    idfDetails: any;
    active: any = 'declarationGeneralDetails'

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
        this.diService.loadIdfDocumentDetails(this.documentUuid)
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.idfDetails = res.data
                    } else {
                        swal.fire({
                            title: res.message,
                            buttonsStyling: false,
                            customClass: {
                                confirmButton: 'btn btn-success form-wizard-next-btn ',
                            },
                            icon: 'error'
                        }).then(() => {
                            this.router.navigate(["/di/", this.documentUuid])
                        });
                    }
                }
            )
    }

}
