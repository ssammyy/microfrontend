import {Component, Input, OnInit} from '@angular/core';
import {ViewCell} from "ng2-smart-table";

@Component({
    selector: 'app-consignment-status',
    template: `
        {{renderedValue}}
    `
})
export class ConsignmentStatusComponent implements ViewCell, OnInit {

    @Input() value: string | number;
    @Input() rowData: any;
    renderedValue: string

    constructor() {
    }

    ngOnInit(): void {
        switch (this.value) {
            case 0:
                this.renderedValue = "Created"
                break
            case 1:
                this.renderedValue = "Approved/Submitted"
                break
            case 10:
                this.renderedValue = "Initial Status"
                break
            case 50:
                this.renderedValue = "Draft Status"
                break
            case -1:
                this.renderedValue = "Rejected status"
                break
            default:
                this.renderedValue = "Unknown Status"
        }
    }

}
