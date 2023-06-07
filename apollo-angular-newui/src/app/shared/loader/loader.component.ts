import {Component, Input, OnInit} from '@angular/core';

@Component({
    selector: 'app-spinner-overlay',

    templateUrl: './loader.component.html',
    styleUrls: ['./loader.component.scss']
})
export class LoaderComponent implements OnInit {
    @Input() public message: string;

    constructor() {
    }

    ngOnInit(): void {
    }

}
