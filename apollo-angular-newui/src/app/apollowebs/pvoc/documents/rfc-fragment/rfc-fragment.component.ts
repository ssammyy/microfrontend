import {Component, Input, OnInit} from '@angular/core';
import {BehaviorSubject} from "rxjs";
import {Router} from "@angular/router";

@Component({
    selector: 'app-rfc-fragment',
    templateUrl: './rfc-fragment.component.html',
    styleUrls: ['./rfc-fragment.component.css']
})
export class RfcFragmentComponent implements OnInit {
    @Input()
    rfcDetails: BehaviorSubject<any>
    @Input()
    certType: any
    @Input()
    isPvocOfficer = true
    @Input()
    active = 1
    rfcData: any
    settings = {
        selectMode: 'single',  // single|multi
        hideHeader: false,
        hideSubHeader: false,
        actions: false,
        noDataMessage: 'No Items found',
        columns: {
            declaredHsCode: {
                title: 'HS Code',
                type: 'string',
                filter: false
            },
            itemQuantity: {
                title: 'Quantity',
                type: 'string',
                filter: false
            },
            productDescription: {
                title: 'Description',
                type: 'string',
                filter: false
            }
        },
        pager: {
            display: true,
            perPage: 20
        }
    };

    constructor(private router: Router) {
    }

    ngOnInit(): void {
        this.rfcDetails.subscribe(
            res => {
                this.rfcData = res
            }
        )
    }

    goBack() {
        switch (this.certType) {
            case 'coc': {

                break
            }
            case 'cor': {
                this.router.navigate(["/pvoc/foreign/rfc/cor"])
                break
            }
        }
    }

    createQuery() {

    }

    downloadCorFile() {

    }

}
