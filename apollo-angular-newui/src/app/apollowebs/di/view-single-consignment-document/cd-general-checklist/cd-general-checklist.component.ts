import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";
import {Router} from "@angular/router";

@Component({
    selector: 'app-cd-general-checklist',
    templateUrl: './cd-general-checklist.component.html',
    styleUrls: ['./cd-general-checklist.component.css']
})
export class CdGeneralChecklistComponent implements OnInit {
    @Output() reloadHandler = new EventEmitter()
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
                // {name: 'deleteRecord', title: '<i class="btn btn-sm btn-danger">Delete</i>'},
                {name: 'viewChecklist', title: '<i class="btn btn-sm btn-primary">View Checklist</i>'}
            ],
            position: 'right' // left|right
        },
        delete: {
            deleteButtonContent: '&nbsp;&nbsp;<i class="fa fa-trash-o text-danger"></i>',
            confirmDelete: true
        },
        noDataMessage: 'No checklist filled',
        columns: {
            serialNumber: {
                title: 'SN #',
                type: 'string'
            },
            clearingAgent: {
                title: 'Clearing Agent',
                type: 'string'
            },
            customsEntryNumber: {
                title: 'Customs Entry Number',
                type: 'string'
            },
            inspectionOfficer: {
                title: 'Inspection Officer',
                type: 'string'
            },
            inspection: {
                title: 'Inspection',
                type: 'string'
            },
            inspectionDate: {
                title: 'Inspection Date',
                type: 'string'
            },
            currentStatus: {
                title: 'Status',
                type: 'string'
            },
            version: {
                title: 'Version',
                type: 'string'
            },
        },
        pager: {
            display: true,
            perPage: 20
        }
    };
    @Input() dataSet: any = [];
    @Input() documentId: any

    constructor(private diService: DestinationInspectionService, private router: Router) {
    }

    ngOnInit(): void {
    }

    onCustomAction(event) {
        switch (event.action) {
            case "download":
                this.diService.downloadDocument("/api/v1/download/attachments/" + event.data.id)
                break
            case "viewChecklist":
                this.router.navigate(['/di/checklist/details/', this.documentId], {
                    queryParams: {
                        ci: event.data.id
                    }
                });
                break
            default:
                console.log("Unsupported action: " + event.action)
        }
    }
}
