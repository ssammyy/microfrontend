import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {PVOCService} from "../../../../core/store/data/pvoc/pvoc.service";
import {UpdateComplaintTaskComponent} from "../update-complaint-task/update-complaint-task.component";

@Component({
    selector: 'app-view-complaint-details',
    templateUrl: './view-complaint-details.component.html',
    styleUrls: ['./view-complaint-details.component.css']
})
export class ViewComplaintDetailsComponent implements OnInit {
    requestId: any
    complaintDetails: any
    activeTab: number = 0
    attachmentSettings = {
        selectMode: 'single',  // single|multi
        hideHeader: false,
        hideSubHeader: false,
        actions: {
            columnTitle: 'Actions',
            add: false,
            edit: false,
            delete: false,
            custom: [
                {name: 'download', title: '<i class="btn btn-sm btn-primary download">Download</i>'}
            ],
            position: 'right' // left|right
        },
        delete: {
            deleteButtonContent: '&nbsp;&nbsp;<i class="fa fa-trash-o text-danger"></i>',
            confirmDelete: true
        },
        rowClassFunction: (row) => {
            let certStatus = row.status;
            if (certStatus !== '1') {
                return 'hide-download-action';
            }
            return ''
        },
        noDataMessage: 'No attachments found',
        columns: {
            fileName: {
                title: 'FILE NAME',
                type: 'string'
            },
            createdOn: {
                title: 'DATE GENERATED',
                type: 'string'
            },
            fileSize: {
                title: 'FILE SIZE',
                type: 'string'
            }
        }
    };

    constructor(private activeRoute: ActivatedRoute, private router: Router, private pvocService: PVOCService, private dialog: MatDialog) {
    }

    ngOnInit(): void {
        this.activeRoute.paramMap
            .subscribe(
                res => {
                    this.requestId = res.get("id")
                    this.loadData()
                }
            )
    }

    goBack() {
        this.router.navigate(["/pvoc/complaints"])
    }

    viewConsignmentDocument() {
        this.router.navigate(["/di/view/", this.complaintDetails.cd_uuid, '/', 'complaint', this.requestId])
    }

    approveRejectTasks(taskId: any, task: any) {
        this.dialog.open(UpdateComplaintTaskComponent, {
            data: {
                complaintId: this.requestId,
                pvoc_officer: this.complaintDetails.is_pvoc_officer,
                task: task,
                taskId: taskId
            }
        })
            .afterClosed()
            .subscribe(
                res => {
                    if (res) {
                        this.loadData()
                    }
                }
            )
    }

    downloadISMReport() {

    }

    loadData() {
        this.pvocService.getComplaintApplicationDetails(this.requestId)
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.complaintDetails = res.data
                    } else {
                        this.pvocService.showError(res.message, this.goBack)
                    }
                }
            )
    }

}
