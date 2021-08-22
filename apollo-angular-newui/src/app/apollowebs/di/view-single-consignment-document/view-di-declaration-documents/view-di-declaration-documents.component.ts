import {Component, OnInit} from '@angular/core';
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";
import {ActivatedRoute, Router} from "@angular/router";
import swal from "sweetalert2";

@Component({
    selector: 'app-view-di-declaration-documents',
    templateUrl: './view-di-declaration-documents.component.html',
    styleUrls: ['./view-di-declaration-documents.component.css']
})
export class ViewDiDeclarationDocumentsComponent implements OnInit {
    documentId: string
    declaration: any;
    message: string;

    constructor(private diService: DestinationInspectionService,
                private activatedRoute: ActivatedRoute,
                private router: Router
    ) {
    }

    ngOnInit(): void {
        this.activatedRoute.paramMap
            .subscribe(
                data => {
                    this.documentId = data.get("id")
                    this.loadData()
                }
            )
    }

    loadData() {
        this.diService.loadCustomsDeclaration(this.documentId)
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.declaration = res.data
                    } else {
                        swal.fire({
                            title: res.message,
                            buttonsStyling: false,
                            customClass: {
                                confirmButton: 'btn btn-success form-wizard-next-btn ',
                            },
                            icon: 'error'
                        }).then(() => {
                            this.router.navigate(["/di/", this.documentId])
                        });
                    }
                },
                err => {
                    this.message = err.message
                }
            )
    }

}
