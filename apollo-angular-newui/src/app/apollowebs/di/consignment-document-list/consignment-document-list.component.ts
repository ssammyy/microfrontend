import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {DestinationInspectionService} from "../../../core/store/data/di/destination-inspection.service";
import {MatDialog} from "@angular/material/dialog";
import {UploadForeignFormComponent} from "./upload-foreign-form/upload-foreign-form.component";

@Component({
    selector: 'app-consignment-document-list',
    templateUrl: './consignment-document-list.component.html',
    styleUrls: ['./consignment-document-list.component.css']
})
export class ConsignmentDocumentListComponent implements OnInit {
    activeStatus: string = 'my-tasks';
    defaultPageSize: number = 20
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
                type: 'string',
                filter: false
            },
            ucrNumber: {
                title: 'UCR No',
                type: 'string'
            },
            applicationRefNo: {
                title: 'Ref No.',
                type: 'string'
            },
            applicantName: {
                title: 'Applicant Name',
                type: 'string'
            },
            applicationDate: {
                title: 'Application Date',
                type: 'string',
                filter: false
            },
            approveRejectCdDate: {
                title: 'Approval Date',
                type: 'string',
                filter: false
            },
            approvalStatus: {
                title: 'Approval Status',
                type: 'string',
                filter: false
            },
            assignedTo: {
                title: 'Assigned Officer',
                type: 'string'
            },
            cdTypeName: {
                title: 'Document Type',
                type: 'string',
                hidden: true
            },
            cdTypeDescription: {
                title: 'Document Description',
                type: 'string'
            },
            cdTypeCategory: {
                title: 'Document Category',
                type: 'string'
            }
        },
        pager: {
            display: true,
            perPage: 20
        }
    };
    dataSet: any = [];
    documentTypes: any[];
    private documentTypeUuid: string

    constructor(private dialog: MatDialog, private router: Router, private diService: DestinationInspectionService) {
    }

    ngOnInit(): void {
        this.documentTypeUuid = null;
        this.loadTypes(() => {
            this.loadData(this.documentTypeUuid, 0, this.defaultPageSize);
        })

    }

    private loadData(documentTypeUuid: string, page: number, size: number): any {

        let data = this.diService.listAssignedCd(documentTypeUuid, page, size);
        if (this.activeStatus === "completed") {
            data = this.diService.listCompletedCd(documentTypeUuid, page, size)
        } else if (this.activeStatus == "ongoing") {
            data = this.diService.listSectionOngoingCd(documentTypeUuid, page, size)
        } else if (this.activeStatus === "not-assigned") {
            data = this.diService.listManualAssignedCd(documentTypeUuid, page, size)
        }
        data.subscribe(
            result => {
                if (result.responseCode === "00") {
                    let listD: any[] = result.data;
                    // for (let i in listD) {
                    //     let docTpe = this.documentTypes.filter(dd => listD[i].cdType == dd.id)
                    //     if (docTpe.length > 0) {
                    //         // console.log(i)
                    //         listD[i]["documentType"] = docTpe[0].typeName
                    //     }
                    // }
                    this.dataSet = listD
                } else {
                    console.log(result)
                }
            }
        );
    }

    private loadTypes(fn: Function) {
        this.diService.documentTypes()
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.documentTypes = res.data;
                    } else {
                        this.documentTypes = []
                    }
                    if (fn) {
                        fn()
                    }
                }
            )
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

    public onFilterChange(event: any) {
        // console.log(event)
        if (event.target.value != this.documentTypeUuid) {
            this.documentTypeUuid = event.target.value
            this.loadData(this.documentTypeUuid, 0, this.defaultPageSize)
        }
    }

    uploadForeignCoROrCor(event: any, type: string) {
        let ref = this.dialog.open(UploadForeignFormComponent, {
            data: {
                "type": type
            }
        })
        ref.afterClosed()
            .subscribe(
                res => {
                    if (res) {
                        this.loadData(this.documentTypeUuid, 0, this.defaultPageSize)
                    }
                }
            )
    }

    toggleStatus(status: string): void {
        if (status !== this.activeStatus) {
            this.activeStatus = status;
            this.loadData(this.documentTypeUuid, 0, this.defaultPageSize)
        }
    }
}
