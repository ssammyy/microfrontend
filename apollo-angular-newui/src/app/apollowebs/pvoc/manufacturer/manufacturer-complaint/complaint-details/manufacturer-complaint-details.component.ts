import {Component, Inject, OnInit} from '@angular/core';
import {PVOCService} from "../../../../../core/store/data/pvoc/pvoc.service";
import {MAT_DIALOG_DATA} from "@angular/material/dialog";

declare global {
    interface Window {
        $: any;
    }
}

@Component({
    selector: 'app-manufacturer-complaint-details',
    templateUrl: './manufacturer-complaint-details.component.html',
    styleUrls: ['./manufacturer-complaint-details.component.css'],
})
export class ManufacturerComplaintDetailsComponent implements OnInit {
    active: number = 0
    attachments: any[]
    public settingsComplaintsFiles = {
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
                // {name: 'viewPDFRemarks', title: '<i class="btn btn-sm btn-primary">View Remarks</i>'},
                {name: 'viewPDFRecord', title: '<i class="btn btn-sm btn-primary">View</i>'},
            ],
            position: 'right', // left|right
        },
        delete: {
            deleteButtonContent: '&nbsp;&nbsp;<i class="fa fa-trash-o text-danger"></i>',
            confirmDelete: true,
        },
        noDataMessage: 'No data found',
        columns: {
            documentType: {
                title: 'File NAME',
                type: 'string',
                filter: false,
            },
            fileName: {
                title: 'DOCUMENT TYPE',
                type: 'string',
                filter: false,
            },
        },
        pager: {
            display: true,
            perPage: 20,
        },
    };


    constructor(@Inject(MAT_DIALOG_DATA) public complaintsDetails: any, private pvocService: PVOCService) {
    }

    ngOnInit(): void {
        console.log(this.complaintsDetails)
    }

    onCustomComplaintFileViewAction(data: any) {

    }


}
