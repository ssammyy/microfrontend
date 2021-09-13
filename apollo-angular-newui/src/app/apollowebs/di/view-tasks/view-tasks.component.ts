import {Component, OnInit} from '@angular/core';
import {DestinationInspectionService} from "../../../core/store/data/di/destination-inspection.service";
import swal from "sweetalert2";
import {MatDialog} from "@angular/material/dialog";
import {ProcessRejectionComponent} from "../forms/process-rejection/process-rejection.component";
import {ViewDemandNoteComponent} from "../demand-note-list/view-demand-note/view-demand-note.component";

@Component({
    selector: 'app-view-tasks',
    templateUrl: './view-tasks.component.html',
    styleUrls: ['./view-tasks.component.css']
})
export class ViewTasksComponent implements OnInit {
    active = "consignment"
    tasks: any[]
    consignmentId: any
    consignment: any
    attachments: any[] = []

    constructor(private dialog: MatDialog, private diService: DestinationInspectionService) {
    }

    ngOnInit(): void {
        this.loadMyTasks()
    }

    viewDemandNote(demandNoteId: any) {
        this.dialog.open(ViewDemandNoteComponent, {
            data: {
                id: demandNoteId
            }
        })
    }

    approveRejectTasks(taskId: any, docUuid: any,taskTitle: any) {
        this.dialog.open(ProcessRejectionComponent, {
            data: {
                cdUuid: docUuid,
                title: taskTitle,
                taskId: taskId
            }
        }).afterClosed()
            .subscribe(res => {
                this.loadMyTasks()
            })
    }

    loadMyTasks() {
        this.diService.loadMyTasks()
            .subscribe(res => {
                if (res.responseCode == "00") {
                    this.tasks = res.data
                    if (this.tasks.length > 0) {
                        this.consignmentId = this.tasks[0].map.cdUuid
                        this.loadSelectedConsignmentDetails()
                    }
                }
            })
    }

    loadSelectedConsignmentDetails(task?: any) {
        if (task) {
            this.consignmentId = task.map.cdUuid
        }
        if (this.consignmentId) {
            this.diService.getConsignmentDetails(this.consignmentId)
                .subscribe(
                    res => {
                        if (res.responseCode == "00") {
                            this.consignment = res.data
                        } else {
                            swal.fire({
                                title: res.message,
                                buttonsStyling: false,
                                customClass: {
                                    confirmButton: 'btn btn-success form-wizard-next-btn ',
                                },
                                icon: 'error'
                            })
                        }
                    }
                )
        }
    }

}
