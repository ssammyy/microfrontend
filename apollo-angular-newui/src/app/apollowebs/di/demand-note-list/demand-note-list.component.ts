import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {DestinationInspectionService} from "../../../core/store/data/di/destination-inspection.service";
import {MatDialog} from "@angular/material/dialog";
import {ViewDemandNoteComponent} from "./view-demand-note/view-demand-note.component";
import {CurrencyFormatterComponent} from "../../../core/shared/currency-formatter/currency-formatter.component";

@Component({
    selector: 'app-demand-note-list',
    templateUrl: './demand-note-list.component.html',
    styleUrls: ['./demand-note-list.component.css']
})
export class DemandNoteListComponent implements OnInit {
    @Output() reloadDemandNotes = new EventEmitter<Boolean>();
    public settings = {
        selectMode: 'single',  // single|multi
        hideHeader: false,
        hideSubHeader: false,
        actions: {
            columnTitle: 'Actions',
            add: false,
            edit: false,
            delete: false,
            custom: [
                {name: 'viewNote', title: '<i class="btn btn-sm btn-primary">View</i>'},
                {name: 'download', title: '<i class="btn btn-sm btn-primary">Download</i>'}
            ],
            position: 'right' // left|right
        },
        delete: {
            deleteButtonContent: '&nbsp;&nbsp;<i class="fa fa-trash-o text-danger"></i>',
            confirmDelete: true
        },
        noDataMessage: 'No data found',
        columns: {
            demandNoteNumber: {
                title: 'REFERENCE NUMBER',
                type: 'string'
            },
            dateGenerated: {
                title: 'DATE GENERATED',
                type: 'string'
            },
            cfvalue: {
                title: 'CF VALUE',
                type: 'custom',
                renderComponent: CurrencyFormatterComponent,
            },
            totalAmount: {
                title: 'TOTAL AMOUNT',
                type: 'custom',
                renderComponent: CurrencyFormatterComponent,
            },
            varField10: {
                title: 'Status',
                type: 'string'
            }
        },
        pager: {
            display: true,
            perPage: 20
        }
    };
    @Input() dataSet: any = [];
    @Input() cdId: any[]

    constructor(private diService: DestinationInspectionService, private dialog: MatDialog) {
    }

    ngOnInit(): void {
    }

    viewDemandNote(demandNoteId: any) {
        this.dialog.open(ViewDemandNoteComponent, {
            data: {
                id: demandNoteId
            }
        }).afterClosed()
            .subscribe(
                res => {
                    this.reloadDemandNotes.emit(res)
                }
            )
    }

    onCustomAction(action: any) {
        switch (action.action) {
            case 'download':
                this.diService.downloadDocument("/api/v1/download/demand/note/" + action.data.id)
                break
            case 'viewNote':
                this.viewDemandNote(action.data.id)
                break
        }

    }

}
