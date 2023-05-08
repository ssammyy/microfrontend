import {Component, Input, OnInit} from '@angular/core';
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

    constructor(private dialog: MatDialog) {
    }

    ngOnInit(): void {
    }

    viewQuery(data: any) {
        this.dialog.open(PvocQueryViewComponent, {
            data: data
        }).afterClosed()
            .subscribe(
                result => {

                }
            )
    }

}
