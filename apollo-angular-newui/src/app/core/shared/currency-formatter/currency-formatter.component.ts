import {Component, Input, OnInit} from '@angular/core';
import {ViewCell} from 'ng2-smart-table';

@Component({
    selector: 'app-currency-formatter',
    template: `
        {{renderValue | currency:'KES':'symbol':'1.2-2':'en-US'}}
    `,
})
export class CurrencyFormatterComponent implements ViewCell, OnInit {
    @Input() value: string | number;
    @Input() rowData: any;
    renderValue: number;

    constructor() {
    }

    ngOnInit(): void {
        if (this.value) {
            try {
                this.renderValue = Number(this.value);
            } catch (e) {
                console.log(e);
            }
        } else {
            this.renderValue = 0.0;
        }
    }

}
