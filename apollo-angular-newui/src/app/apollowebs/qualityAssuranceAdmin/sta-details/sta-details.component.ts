import {Component, OnInit} from '@angular/core';

@Component({
    selector: 'app-sta-details',
    templateUrl: './sta-details.component.html',
    styleUrls: ['./sta-details.component.css']
})
export class StaDetailsComponent implements OnInit {

    constructor() {
    }

    ngOnInit(): void {
    }

    id: any = '';

    accordion(ids: any) {
        if (this.id == ids) {
            this.id = '';
        } else {
            this.id = ids;
        }
    }

}
