import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";
import {MatDialog} from "@angular/material/dialog";
import {PvocCocQueriesComponent} from "../../../pvoc/documents/foreign-cocs/pvoc-coc-queries/pvoc-coc-queries.component";
import {Observable} from "rxjs";

@Component({
    selector: 'app-coc-fragment',
    templateUrl: './coc-fragment.component.html',
    styleUrls: ['./coc-fragment.component.css']
})
export class CocFragmentComponent implements OnInit {
    @Input()
    cocDetailDs: Observable<any>
    @Input()
    isPvocOfficer: boolean
    @Input()
    documentType: any
    @Output()
    changedReload: EventEmitter<any> = new EventEmitter<any>()
    active: any = '1'

    cocDetails: any

    constructor(private  diService: DestinationInspectionService, private dialog: MatDialog) {
    }

    ngOnInit(): void {
        this.cocDetailDs.subscribe(
            res => {
                this.cocDetails = res
            }
        )
    }

    createQuery() {
        this.dialog.open(PvocCocQueriesComponent, {
            data: this.cocDetails.certificate_details
        }).afterClosed()
            .subscribe(
                res => {
                    if (res) {
                        this.changedReload.emit(true)
                    }
                }
            )
    }

    downloadCocFile() {
        if (this.cocDetails) {
            this.diService.downloadDocument("/api/v1/download/coc-coi/" + this.cocDetails.certificate_details.id)
        }
    }
}
