import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {PVOCService} from "../../../core/store/data/pvoc/pvoc.service";
import {MatDialog} from "@angular/material/dialog";
import {WaiverNotApplicableComponent} from "../waivers/waiver-not-applicable/waiver-not-applicable.component";

@Component({
    selector: 'app-import-inspection',
    templateUrl: './import-inspection.component.html',
    styleUrls: ['./import-inspection.component.css']
})
export class ImportInspectionComponent implements OnInit {

    constructor(private router: Router, private pvoc: PVOCService, private dialog: MatDialog) {
    }

    ngOnInit() {
    }

    showError(message: string) {
        var ref = this.dialog.open(WaiverNotApplicableComponent, {
            data: {
                message: message
            }
        })
    }

    goTo(url: string, isExemption: boolean): void {
        if (isExemption) {
            this.pvoc.checkExemptionApplicable().subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.router.navigateByUrl(url)
                    } else {
                        this.showError(res.responseMessage);
                    }
                }
            )
        } else {
            this.router.navigateByUrl(url)
        }
    }
}
