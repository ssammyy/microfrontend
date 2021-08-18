import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {DestinationInspectionService} from "../../../core/store/data/di/destination-inspection.service";

@Component({
    selector: 'app-consignment-document-list',
    templateUrl: './consignment-document-list.component.html',
    styleUrls: ['./consignment-document-list.component.css']
})
export class ConsignmentDocumentListComponent implements OnInit {
    activeStatus: string = 'ongoing';
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
            id: {
                title: 'ID',
                type: 'string'
            },
            ucrNumber: {
                title: 'UCR No',
                type: 'string'
            },
            applicantName: {
                title: 'Applicant Name',
                type: 'string'
            },
            applicationDate: {
                title: 'Application Date',
                type: 'string'
            },
            approveRejectCdDate: {
                title: 'Approval Date',
                type: 'string'
            },
            approveRejectCdStatus: {
                title: 'Approval Status',
                type: 'string'
            },
            assignedTo: {
                title: 'Assigned Officer',
                type: 'string'
            }
        },
        pager: {
            display: true,
            perPage: 20
        }
    };
    dataSet: any = [];
    private documentTypeUuid: string

    constructor(private router: Router, private diService: DestinationInspectionService) {
    }

    ngOnInit(): void {
        this.documentTypeUuid = "5a015375-5661-46cf-9ea4-cc4823c7e80e"
        this.loadData(this.documentTypeUuid, 0, 10);
    }

    private loadData(documentTypeUuid: string, page: number, size: number): any {
        let data = this.diService.listAssignedCd(documentTypeUuid, page, size);
        if (this.activeStatus === "1") {
            data = this.diService.listCompletedCd(documentTypeUuid, page, size)
        } else if (this.activeStatus == "0") {
            data = this.diService.listManualAssignedCd(documentTypeUuid, page, size)
        }
        data.subscribe(
            result => {
                if (result.responseCode === "00") {
                    this.dataSet = result.data;
                } else {
                    console.log(result)
                }
            }
        );
    }

    public onCustomAction(event: any): void {
        switch (event.action) {
            case 'viewRecord':
                this.viewRecord(event.data);
                break;
        }
    }

    private viewRecord(data: any) {
        console.log(data)
        this.router.navigate([`/di`, data.uuid]);
    }

    toggleStatus(status: string): void {
        if (status != this.activeStatus) {
            this.activeStatus = status;
            this.loadData(this.documentTypeUuid, 0, 15)
        }
    }
}
