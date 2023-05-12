import {Component, OnInit} from '@angular/core';
import {PVOCService} from "../../../../../core/store/data/pvoc/pvoc.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
    selector: 'app-view-cor',
    templateUrl: './view-cor.component.html',
    styleUrls: ['./view-cor.component.css']
})
export class ViewCorComponent implements OnInit {
    message: string
    corDetails: any
    documentId: any

    constructor(private router: Router, private activatedRoute: ActivatedRoute, private pvocService: PVOCService) {
    }

    ngOnInit(): void {
        this.activatedRoute.paramMap
            .subscribe(
                res => {
                    this.documentId = res.get("id")
                    this.loadCorDetails(true)
                }
            )
    }

    goBack() {
        this.router.navigate(["/pvoc/foreign/cor"])
    }

    loadCorDetails(reload: boolean) {
        if (!reload) {
            return
        }
        this.message = null
        this.pvocService.loadCorDetails(this.documentId)
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
}
