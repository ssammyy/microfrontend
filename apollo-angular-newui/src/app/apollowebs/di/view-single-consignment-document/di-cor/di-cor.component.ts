import {Component, OnInit} from '@angular/core';
import {FileService} from 'src/app/core/services/file.service';
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
    selector: 'app-di-cor',
    templateUrl: './di-cor.component.html',
    styleUrls: ['./di-cor.component.css']
})
export class DiCorComponent implements OnInit {
    active = 'cor-details';
    corDetails: any
    message: any
    cdUuid: any

    constructor(private activatedRoute: ActivatedRoute, private fileService: FileService, private diService: DestinationInspectionService,private router: Router) {
    }

    ngOnInit(): void {
        this.activatedRoute.paramMap
            .subscribe(
                res => {
                    this.cdUuid = res.get("id")
                    this.loadCorDetails()
                }
            )
    }
    goBack() {
        this.router.navigate(["/di", this.cdUuid])
    }
    loadCorDetails() {
        this.message = null
        this.diService.loadCorDetails(this.cdUuid)
            .subscribe(
                res => {
                    if (res.responseCode == "00") {
                        this.corDetails = res.data
                    } else {
                        this.message = res.message
                    }
                }
            )
    }

    downloadCorFile(): void {
       if(this.corDetails){
           this.diService.downloadDocument("/api/v1/download/cor/"+this.corDetails.cor_details.id)
       }

    }

}
