import {Component, Input, OnInit} from '@angular/core';
import {Router} from "@angular/router";

@Component({
    selector: 'app-other-version-details',
    templateUrl: './other-version-details.component.html',
    styleUrls: ['./other-version-details.component.css']
})
export class OtherVersionDetailsComponent implements OnInit {
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
                //  { name: 'editRecord', title: '<i class="btn btn-sm btn-primary">View More</i>' },
                {name: 'viewRecord', title: '<i class="btn btn-sm btn-primary">View More</i>'}
            ],
            position: 'right' // left|right
        },
        delete: {
            deleteButtonContent: '&nbsp;&nbsp;<i class="fa fa-trash-o text-danger"></i>',
            confirmDelete: true
        },
        noDataMessage: 'No data found',
        columns: {

            ucrNumber: {
                title: 'UCR No',
                type: 'string'
            },
            modifiedBy: {
                title: "Last Modified By",
                type: 'string'
            },
            version: {
                title: 'Version Number',
                type: 'string'
            },
        },
        pager: {
            display: true,
            perPage: 20
        }
    };
    @Input() dataSet: any = [];

    constructor(private router: Router) {
    }


    ngOnInit(): void {
    }

    onCustomAction(event: any) {
        switch (event.action) {
            case "viewRecord":
                this.router.navigate(["/di", event.data.uuid])
                break
            default:
                console.log("Ignored action: "+event.action)
        }

    }
}

