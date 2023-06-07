import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {PvocQueryViewComponent} from "../pvoc-query-view/pvoc-query-view.component";

@Component({
    selector: 'app-pvoc-query-card',
    templateUrl: './pvoc-query-card.component.html',
    styleUrls: ['./pvoc-query-card.component.css']
})
export class PvocQueryCardComponent implements OnInit {
    @Input()
    queryDetails: any
    @Output()
    queryChanged = new EventEmitter<boolean>()

    constructor(private dialog: MatDialog) {
    }

    ngOnInit(): void {
    }

    formatDocument(lnk: string) {
        if (lnk) {
            let parts = lnk.split('/')
            if (parts.length > 0) {
                return parts[parts.length - 1]
            }
        }
        return "N/A"
    }

    reloadChanged(reload: boolean) {
        if (reload) {
            this.queryChanged.emit(true)
        }
    }

    viewQuery(data: any) {
        this.dialog.open(PvocQueryViewComponent, {
            data: data
        }).afterClosed()
            .subscribe(
                result => {
                    this.queryChanged.emit(result)
                }
            )
    }

}
