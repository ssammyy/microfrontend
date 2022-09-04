import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";

@Component({
    selector: 'app-waiver-application',
    templateUrl: './waiver-application.component.html',
    styleUrls: ['./waiver-application.component.css']
})
export class WaiverApplicationComponent implements OnInit {

    constructor(private router: Router) {
    }

    ngOnInit(): void {
    }

    goBack() {
        this.router.navigate(["/pvoc/applications"])
    }

}
