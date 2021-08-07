import {Component, OnInit, Output} from '@angular/core';
import {Router} from "@angular/router";

@Component({
    selector: 'app-manufacturer-details',
    templateUrl: './manufacturer-details.component.html',
    styleUrls: ['./manufacturer-details.component.css']
})
export class ManufacturerDetailsComponent implements OnInit {
    @Output()
    companyDetails: any;

    constructor(private router: Router) {
    }

    ngOnInit(): void {
    }

    nextClicked(){
        console.log("NEXT");
        this.router.navigateByUrl("/pvoc/exceptions/goods")
    }

}
