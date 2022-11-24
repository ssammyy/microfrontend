import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";

@Component({
    selector: 'app-prepare-draft',
    templateUrl: './prepare-draft.component.html',
    styleUrls: ['./prepare-draft.component.css']
})
export class PrepareDraftComponent implements OnInit {
    id: string;

    constructor(private router: Router) {
    }


    ngOnInit(): void {
        this.checkForId();
        console.log(this.id)

    }


    private checkForId() {
        if (localStorage.getItem('id') != null) {
            this.id = localStorage.getItem('id');

        } else {
            this.router.navigate(['/preparePd']);

        }
    }

}
