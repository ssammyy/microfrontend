import {Component, OnInit} from '@angular/core';

@Component({
    selector: 'app-ncr-certificates',
    templateUrl: './ncr-certificates.component.html',
    styleUrls: ['./ncr-certificates.component.css']
})
export class NcrCertificatesComponent implements OnInit {
    documentType = "ncr"

    constructor() {
    }

    ngOnInit(): void {
    }

}
