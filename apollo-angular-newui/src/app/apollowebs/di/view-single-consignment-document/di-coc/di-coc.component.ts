import {Component, OnInit} from '@angular/core';
import {FileService} from 'src/app/core/services/file.service';
import * as fileSaver from 'file-saver';
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
    selector: 'app-di-coc',
    templateUrl: './di-coc.component.html',
    styleUrls: ['./di-coc.component.css']
})
export class DiCocComponent implements OnInit {
    activeTab = 'coc-details'
    cdDetailsId: any
    cocDetails: any
    documentType: any
    docType: any

    constructor(private fileService: FileService, private diService: DestinationInspectionService, private activatedRoute: ActivatedRoute, private router: Router) {
    }

    ngOnInit(): void {
        this.activatedRoute.paramMap
            .subscribe(
                res => {
                    this.cdDetailsId = res.get("id")
                    this.docType = res.get('docType')
                    this.loadCocDetails()
                }
            )
    }

    goBack() {
        this.router.navigate(["/di", this.cdDetailsId])
    }

    loadCocDetails() {
        this.diService.loadCocDetails(this.cdDetailsId, this.docType)
            .subscribe(
                res => {
                    if (res.responseCode == "00") {
                        this.cocDetails = res.data
                        switch (this.cocDetails.certificate_details.cocType) {
                            case 'COC':
                                this.documentType = 'Certificate of Compliance (COC)'
                                break
                            case 'COI':
                                this.documentType = 'Certificate Of Inspection (COI)'
                                break
                            case 'NCR':
                                this.documentType = "Non-Conformity Report (NCR)"
                                break
                            default:
                                this.documentType = "Other"
                        }
                    }
                }
            )
    }

    downloadCocFile(): void {
        if (this.cocDetails) {
            this.diService.downloadDocument("/api/v1/download/coc-coi/" + this.cocDetails.certificate_details.id)
            // this.diService.downloadDocument("/api/v1/download/coc-coi/843126")
        }
    }

}
