import {Component, Input, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {MinistryInspectionRequestComponent} from "./ministry-inspection-request/ministry-inspection-request.component";
import {MatDialog} from "@angular/material/dialog";

@Component({
    selector: 'app-item-details-list-view',
    templateUrl: './item-details-list-view.component.html',
    styleUrls: ['./item-details-list-view.component.css']
})
export class ItemDetailsListViewComponent implements OnInit {
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
                // {name: 'requestMinistryChecklist', title: '<i class="btn btn-sm btn-primary">MINISTRY CHECKLIST</i>'},
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
            id: {
                title: 'Number',
                type: 'string'
            },
            itemNo: {
                title: 'Item No',
                type: 'string'
            },
            itemDescription: {
                title: 'Description',
                type: 'string'
            },
            itemHsCode: {
                title: 'HS Code',
                type: 'string'
            },
            quantity: {
                title: 'Quantity',
                type: 'string'
            },
            unitOfQuantity: {
                title: 'Unit Quantity',
                type: 'string'
            },
            countryOfOrgin: {
                title: 'Country of Origin',
                type: 'string'
            },
            inspectionNotificationStatus: {
                title: 'Inspection Status',
                type: 'string'
            }
        },
        pager: {
            display: true,
            perPage: 20
        }
    };
    @Input() dataSet: any = [];
    @Input() consignmentId: any;

    constructor(private router: Router, private dialog: MatDialog) {
    }

    ngOnInit(): void {
    }

    onCustomAction(event: any) {
        switch (event.action) {
            case 'viewRecord':
                this.router.navigate(["/di/item/", this.consignmentId, event.data.uuid])
                break
            case 'requestMinistryChecklist':
                this.dialog.open(MinistryInspectionRequestComponent, {
                    data: {
                        uuid: event.data.uuid,
                        reinspection: false,
                    }
                })
                break
            default:
                console.log("Custom: " + event.action)
        }
    }
}
