import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";
import {PvocQueriesComponent} from "../../../pvoc/documents/foreign-cors/pvoc-queries/pvoc-queries.component";
import {MatDialog} from "@angular/material/dialog";

@Component({
    selector: 'app-cor-fragment',
    templateUrl: './cor-fragment.component.html',
    styleUrls: ['./cor-fragment.component.css']
})
export class CorFragmentComponent implements OnInit {
    @Input()
    corDetails: any
    active: any = '0'
    @Input()
    isPvocOfficer: boolean
    @Output()
    changedReload = new EventEmitter<any>()


    constructor(private  diService: DestinationInspectionService, private dialog: MatDialog) {
    }

    ngOnInit(): void {
    }

    createQuery() {
        this.dialog.open(PvocQueriesComponent, {
            data: this.corDetails.certificate_details
        }).afterClosed()
            .subscribe(
                res => {
                    if (res) {
                        this.changedReload.emit(true)
                    }
                }
            )
    }

    downloadCorFile(): void {
        if (this.corDetails) {
            this.diService.downloadDocument("/api/v1/download/cor/" + this.corDetails.cor_details.id)
        }
    }

}
