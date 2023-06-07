import {Component, OnInit} from '@angular/core';
import {PVOCService} from "../../../../core/store/data/pvoc/pvoc.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
    selector: 'app-view-query',
    templateUrl: './view-query.component.html',
    styleUrls: ['./view-query.component.css']
})
export class ViewQueryComponent implements OnInit {

    queryId: any
    queryDetails: any
    activeTab = 0

    constructor(private  pvocService: PVOCService, private activeRouter: ActivatedRoute, private router: Router) {
    }

    ngOnInit(): void {
        this.activeRouter.paramMap.subscribe(
            res => {
                this.queryId = res.get("id")
                this.loadData()
            }
        )
    }

    viewRfcDetails(rfcId: number, rfcType: string) {
        switch (rfcType) {
            case 'COR':
                this.router.navigate(['/pvoc/foreign/rfc/cor', rfcId])
                break
            default:
                this.router.navigate(['/pvoc/foreign/rfc/coc', rfcId])
                break
        }
    }

    viewCdDetails(cdId: number) {

    }

    goBack() {
        this.router.navigate(["/pvoc/query"])
    }

    loadData() {
        this.pvocService.loadQueryDetails(this.queryId)
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.queryDetails = res.data
                    } else {
                        this.pvocService.showError(res.message)
                    }
                },
                error => {
                    this.pvocService.showError("Failed to load data: " + error.toString())
                }
            )
    }

}
