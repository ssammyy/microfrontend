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

    constructor(private fileService: FileService, private diService: DestinationInspectionService, private activatedRoute: ActivatedRoute,private router:Router) {
    }

    ngOnInit(): void {
        this.activatedRoute.paramMap
            .subscribe(
                res => {
                    this.cdDetailsId = res.get("id")
                    this.loadCocDetails()
                }
            )
    }
    goBack() {
        this.router.navigate(["/di", this.cdDetailsId])
    }
    loadCocDetails() {
        this.diService.loadCocDetails(this.cdDetailsId)
            .subscribe(
                res => {
                    if (res.responseCode == "00") {
                        this.cocDetails = res.data
                    }
                }
            )
    }

    downloadCocFile(): void {
        if(this.cocDetails) {
            this.diService.downloadDocument("/api/v1/download/coc/" + this.cocDetails.certificate_details.id)
        }
    }

}
